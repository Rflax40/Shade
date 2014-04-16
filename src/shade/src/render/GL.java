package shade.src.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import shade.src.resource.Resource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map.Entry;

public class GL {

    public static int preferredMag = GL11.GL_NEAREST;
    public static int preferredMin = GL11.GL_NEAREST;
    public static boolean generateMipmaps;
    private static HashMap<String, Texture> textures = new HashMap<>();

    public static void bindTexture(String t) {
        bindTexture(textures.get(t));
    }

    public static void bindTexture(Texture t) {
        bindTexture(t.id);
    }

    public static void bindTexture(int t) {
        bindTexture(t, preferredMin, preferredMag);
    }

    public static void bindTexture(int t, int min, int mag) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, t);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, mag);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, min);
        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
    }

    public static void clearTextures() {
        for (Texture t : textures.values()) {
            t.unload();
        }
        textures.clear();
        System.gc();
    }

    public static void clearTexture(Texture t) {
        t.unload();
        if (textures.containsValue(t)) {
            String key = "";
            for (Entry<String, Texture> entry : textures.entrySet()) {
                if (entry != null) {
                    if (entry.getValue().id == t.id) {
                        key = entry.getKey();
                    }
                }
            }
            textures.remove(key);
        }
    }

    public static void color(int hex) {
        color(hex, 1);
    }

    public static void color(int hex, float al) {
        float b = (hex & 0xFF) / 255f;
        float g = (hex & 0xFF00 >> 8) / 255f;
        float r = (hex & 0xFF0000 >> 16) / 255F;
        GL11.glColor4f(r, g, b, al);
    }

    public static Texture getTexture(String f) throws IOException {
        return getTexture(Resource.getResource(f).getAsStream(), f.substring(f.lastIndexOf(File.separatorChar) + 1));
    }

    public static Texture getTexture(InputStream i, String n) {
        if (!textures.containsKey(n)) {
            loadTexture(i, n);
        }
        return textures.get(n);
    }

    public static void loadTexture(InputStream i, String n) {
        try {
            BufferedImage image = ImageIO.read(i);
            boolean alpha = image.getColorModel().hasAlpha();
            ByteBuffer buffer = getImageData(image);
            int id = genTexture(GL11.GL_RGBA, image.getWidth(), image.getHeight(), alpha? GL11.GL_RGBA : GL11.GL_RGB, buffer);
            Texture tex = new Texture(id, image.getWidth(), image.getHeight());
            textures.put(n, tex);
        } catch (Exception e) {
            throw new RuntimeException("Problem loading texture: " + n, e);
        }
    }

    public static int genTexture(int internalFormat, int w, int h, int format, ByteBuffer buffer) {
        int id = GL11.glGenTextures();
        bindTexture(id);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, w, h, 0, format, GL11.GL_UNSIGNED_BYTE, buffer);
        if (generateMipmaps) {
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        }
        unbindTexture();
        return id;
    }

    public static void unbindTexture() {
        bindTexture(0);
    }

    public static ByteBuffer getImageData(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        boolean alpha = image.getColorModel().hasAlpha();
        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * (alpha? 4 : 3));
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) (pixel >> 16 & 0xFF));
                buffer.put((byte) (pixel >> 8 & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                if (alpha) {
                    buffer.put((byte) (pixel >> 24 & 0xFF));
                }
            }
        }
        buffer.flip();
        return buffer;
    }

    public static void rotate(float x, float y, float z, float rotX, float rotY, float rotZ) {
        GL11.glTranslatef(x, y, z);
        rotate(rotX, rotY, rotZ);
        GL11.glTranslatef(-x, -y, -z);
    }

    public static void rotate(float x, float y, float z) {
        GL11.glRotatef(x, 1, 0, 0);
        GL11.glRotatef(y, 0, 1, 0);
        GL11.glRotatef(z, 0, 0, 1);
    }

    public static void setPreferredFilters(int min, int mag) {
        preferredMin = min;
        preferredMag = mag;
    }
}
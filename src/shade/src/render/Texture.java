package shade.src.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import shade.src.resource.Resource;
import shade.src.resource.ResourceBased;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Texture extends ResourceBased {

    private static final Factory textureFactory = new Factory() {
        @Override
        public ResourceBased construct(Resource resource) {
            return loadTexture(resource);
        }

        @Override
        public Class getClassType() {
            return Texture.class;
        }
    };
    public static boolean generateMipMaps = true;
    public static int preferredMag = GL11.GL_NEAREST;
    public static int preferredMin = GL11.GL_NEAREST;
    public final int id;
    public final int width;
    public final int height;

    private Texture(Resource res, int i, int w, int h) {
        super(res);
        id = i;
        width = w;
        height = h;
    }

    public void drawCenteredModalRect(int ux, int uy, int uw, int uh, double x, double y, double z, double w, double h) {
        drawModalRect(ux, uy, uw, uh, x - w / 2, y + h / 2, z, w, h);
    }

    public void drawModalRect(int ux, int uy, int uw, int uh, double x, double y, double z, double w, double h) {
        double lx = (double) ux / width;
        double rx = (double) (ux + uw) / width;
        double ty = (double) uy / height;
        double by = (double) (uy + uh) / height;
        bindTexture(id);
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2d(lx, by);
        GL11.glVertex3d(x, y - h, z);
        GL11.glTexCoord2d(rx, by);
        GL11.glVertex3d(x + w, y - h, z);
        GL11.glTexCoord2d(lx, ty);
        GL11.glVertex3d(x, y, z);
        GL11.glTexCoord2d(rx, ty);
        GL11.glVertex3d(x + w, y, z);
        GL11.glEnd();
        unbindTexture();
        /*
         * GL11.glBegin(GL11.GL_LINE_LOOP); GL11.glVertex3d(x, y - h, z);
		 * GL11.glVertex3d(x + w, y - h, z); GL11.glVertex3d(x + w, y, z);
		 * GL11.glVertex3d(x, y, z); GL11.glEnd();
		 */
    }

    @Override
    public void unload() {
        super.unload();
        GL11.glDeleteTextures(id);
    }

    public static Texture getTexture(Resource resource) {
        return (Texture) getResourceBased(resource, textureFactory);
    }

    public static void bindTexture(int id) {
        bindTexture(id, preferredMag, preferredMin);
    }

    public static void bindTexture(int id, int mag, int min) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, mag);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, min);
        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
    }

    public static void unbindTexture() {
        bindTexture(0);
    }

    public static Texture loadTexture(Resource res) {
        try (InputStream in = res.getAsStream()) {
            BufferedImage image = ImageIO.read(in);
            boolean alpha = image.getColorModel().hasAlpha();
            ByteBuffer buffer = getImageData(image);
            int id = genTexture(GL11.GL_RGBA, image.getWidth(), image.getHeight(), alpha? GL11.GL_RGBA : GL11.GL_RGB, buffer);
            return new Texture(res, id, image.getWidth(), image.getHeight());
        } catch (Exception e) {
            new Exception("Problem loading texture: " + res, e).printStackTrace();
        }
        return null;
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

    public static int genTexture(int internalFormat, int w, int h, int format, ByteBuffer buffer) {
        int id = GL11.glGenTextures();
        bindTexture(id);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, w, h, 0, format, GL11.GL_UNSIGNED_BYTE, buffer);
        if (generateMipMaps) {
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        }
        unbindTexture();
        return id;
    }
}
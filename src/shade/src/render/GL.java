package shade.src.render;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

import shade.src.resource.ResourceLocator;
import shade.src.util.ByteOperations;

public class GL {

	private static int orthoPushes = 0;

	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();

	public static int preferredMag = GL11.GL_NEAREST;
	public static int preferredMin = GL11.GL_NEAREST;
	public static boolean generateMipmaps;

	public static void bindTexture(int t) {
		bindTexture(t, preferredMin, preferredMag);
	}

	public static void bindTexture(int t, int min, int mag) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, t);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, mag);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, min);
		GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
	}

	public static void bindTexture(String t) {
		bindTexture(textures.get(t));
	}

	public static void bindTexture(Texture t) {
		bindTexture(t.id);
	}

	public static void clearTextures() {
		for (Texture t : textures.values())
			t.unload();
		textures.clear();
		System.gc();
	}

	public static void clearTexture(Texture t) {
		t.unload();
		if (textures.containsValue(t)) {
			String key = "";
			for (Entry<String, Texture> entry : textures.entrySet())
				if (entry != null)
					if (entry.getValue().id == t.id)
						key = entry.getKey();
			textures.remove(key);
		}
	}

	public static void color(int hex) {
		color(hex, 1);
	}

	public static void color(int hex, float al) {
		float b = ByteOperations.getValueAt(hex, 0, 8) / 255F;
		float g = ByteOperations.getValueAt(hex, 8, 8) / 255F;
		float r = ByteOperations.getValueAt(hex, 16, 8) / 255F;
		GL11.glColor4f(r, g, b, al);
	}

	public static int genTexture(int internalformat, int w, int h, int format, ByteBuffer buffer) {
		int id = GL11.glGenTextures();
		bindTexture(id);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalformat, w, h, 0, format,
			GL11.GL_UNSIGNED_BYTE, buffer);
		if (generateMipmaps)
			GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, 4, w, h, format, GL11.GL_UNSIGNED_BYTE,
				buffer);
		unbindTexture();
		return id;
	}

	public static ByteBuffer getImageData(BufferedImage image) {
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0,
			image.getWidth());
		boolean alpha = image.getColorModel().hasAlpha();
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * (alpha ? 4 : 3));

		for (int y = 0; y < image.getHeight(); y++)
			for (int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) (pixel >> 16 & 0xFF));
				buffer.put((byte) (pixel >> 8 & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
				if (alpha)
					buffer.put((byte) (pixel >> 24 & 0xFF));
			}
		buffer.flip();
		return buffer;
	}

	public static Texture getTexture(InputStream i, String n) {
		if (!textures.containsKey(n))
			loadTexture(i, n);
		return textures.get(n);
	}

	public static Texture getTexture(String f) throws IOException {
		return getTexture(ResourceLocator.getAsStream(f),
			f.substring(f.lastIndexOf(File.separatorChar) + 1));
	}

	public static void loadTexture(InputStream i, String n) {
		try {
			BufferedImage image = ImageIO.read(i);
			boolean alpha = image.getColorModel().hasAlpha();
			ByteBuffer buffer = getImageData(image);
			int id = genTexture(GL11.GL_RGBA, image.getWidth(), image.getHeight(),
				alpha ? GL11.GL_RGBA : GL11.GL_RGB, buffer);
			Texture tex = new Texture(id, image.getWidth(), image.getHeight());
			textures.put(n, tex);
		} catch (Exception e) {
			throw new RuntimeException("Problem loading texture: " + n, e);
		}
	}

	public static void popOrtho() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		orthoPushes--;
		if (orthoPushes < 0)
			throw new RuntimeException("UNDERFLOW_ERROR");
	}

	public static void pushOrtho() {
		pushOrtho(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
	}

	public static void pushOrtho(int width, int height) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, 0, height, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		orthoPushes++;
	}

	public static void rotate(float x, float y, float z) {
		GL11.glRotatef(x, 1, 0, 0);
		GL11.glRotatef(y, 0, 1, 0);
		GL11.glRotatef(z, 0, 0, 1);
	}

	public static void rotate(float x, float y, float z, float rotx, float roty, float rotz) {
		GL11.glTranslatef(x, y, z);
		rotate(rotx, roty, rotz);
		GL11.glTranslatef(-x, -y, -z);
	}

	public static void setPreferredFilters(int min, int mag) {
		preferredMin = min;
		preferredMag = mag;
	}

	public static void unbindTexture() {
		bindTexture(0);
	}

	public static FloatBuffer toBuffer(float... floats) {
		FloatBuffer buff = BufferUtils.createFloatBuffer(floats.length);
		buff.put(floats);
		buff.flip();
		return buff;
	}

}
package shade.src.render;

import org.lwjgl.opengl.GL11;

public class GL {

    public static void color(int hex) {
        color(hex, 1);
    }

    public static void color(int hex, float al) {
        float b = (hex & 0xFF) / 255f;
        float g = ((hex & 0xFF00) >> 8) / 255f;
        float r = ((hex & 0xFF0000) >> 16) / 255F;
        GL11.glColor4f(r, g, b, al);
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
}
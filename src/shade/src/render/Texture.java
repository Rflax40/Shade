package shade.src.render;

import org.lwjgl.opengl.GL11;
import shade.src.resource.Unloadable;

public class Texture implements Unloadable {

    public final int id;
    public final int width;

    public final int height;

    public Texture(int i, int w, int h) {
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
        GL.bindTexture(id);
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
        GL.unbindTexture();
        /*
		 * GL11.glBegin(GL11.GL_LINE_LOOP); GL11.glVertex3d(x, y - h, z);
		 * GL11.glVertex3d(x + w, y - h, z); GL11.glVertex3d(x + w, y, z);
		 * GL11.glVertex3d(x, y, z); GL11.glEnd();
		 */
    }

    public void unload() {
        GL11.glDeleteTextures(id);
    }
}
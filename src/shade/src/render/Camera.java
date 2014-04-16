package shade.src.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import shade.src.sound.Listener;
import shade.src.util.Orientable;
import shade.src.util.OrientationUtils;

public class Camera extends Orientable {

    public Listener listener;

    public Camera() {
        super();
    }

    public Camera lookThrough() {
        GL11.glLoadIdentity();
        Matrix4f mx = OrientationUtils.rotationMatrixFromQuaternion(orientation);
        OrientationUtils.setCurrentGLMatrixTo(mx);
        GL11.glTranslatef(-position.x, -position.y, -position.z);
        return this;
    }

    public void reset() {
        super.reset();
        listener = new Listener();
    }
}
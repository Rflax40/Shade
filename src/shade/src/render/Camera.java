package shade.src.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.*;

import shade.src.sound.Listener;
import shade.src.util.*;

public class Camera extends Orientable {
	
	public Listener listener;

	public Camera() {
		super();
	}

	public Camera lookThrough() {
		GL11.glLoadIdentity();
		Matrix4f mx = OrientationUtils.rotationMatrixFromQuaternion(orientation);
		/*
		 * Matrix4f mx = new Matrix4f(); mx.m00 = right.x; mx.m10 = right.y;
		 * mx.m20 = right.z;
		 * 
		 * mx.m01 = up.x; mx.m11 = up.y; mx.m21 = up.z;
		 * 
		 * mx.m02 = forward.x; mx.m12 = forward.y; mx.m22 = forward.z;
		 */
		OrientationUtils.setCurrentGLMatrixTo(mx);
		GL11.glTranslatef(-position.x, -position.y, -position.z);
		return this;
	}

	public void reset() {
		super.reset();
		listener = new Listener();
	}
	
	public void rotate(Quaternion q) {
		super.rotate(q);
		
	}
}
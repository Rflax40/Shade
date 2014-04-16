package shade.src.util;

import org.lwjgl.util.vector.*;

public abstract class Orientable {

    public static final int ORI_UP = OrientationUtils.ORI_UP;
    public static final int ORI_RIGHT = OrientationUtils.ORI_RIGHT;
    public static final int ORI_FORWARD = OrientationUtils.ORI_FORWARD;
    public static final int ORI_POSITION = OrientationUtils.ORI_POSITION;
    protected Quaternion orientation;
    protected Vector3f position;
    protected Vector3f up;
    protected Vector3f right;
    protected Vector3f forward;

    public Orientable() {
        reset();
    }

    public void reset() {
        orientation = new Quaternion();
        position = new Vector3f(0, 0, 0);
        up = new Vector3f(0, 1, 0);
        right = new Vector3f(1, 0, 0);
        forward = new Vector3f(0, 0, -1);
    }

    public Vector3f getVector(int id) {
        Vector3f res = null;
        switch (id) {
            case ORI_UP:
                res = up;
                break;
            case ORI_RIGHT:
                res = right;
                break;
            case ORI_FORWARD:
                res = forward;
                break;
            case ORI_POSITION:
                res = position;
                break;
        }
        return res;
    }

    public Quaternion getOrientation() {
        return orientation;
    }

    public void rotate(Vector3f axis, float angle) {
        rotate(new Vector4f(axis.x, axis.y, axis.z, (float) Math.toRadians(angle)));
    }

    public void rotate(Vector4f axisAngle) {
        Quaternion q = new Quaternion();
        q.setFromAxisAngle(axisAngle);
        rotate(q);
    }

    public void rotate(Quaternion q) {
        Quaternion.mul(orientation, q, orientation);
        up = OrientationUtils.getVectorFromQuaternion(orientation, ORI_UP);
        right = OrientationUtils.getVectorFromQuaternion(orientation, ORI_RIGHT);
        forward = OrientationUtils.getVectorFromQuaternion(orientation, ORI_FORWARD);
        up.normalise();
        right.normalise();
        forward.normalise();
    }

    public void rotate(float x, float y, float z, float angle) {
        rotate(new Vector4f(x, y, z, (float) Math.toRadians(angle)));
    }

    public void translate(float x, float y, float z) {
        translate(new Vector3f(x, y, z));
    }

    public void translate(Vector3f delta) {
        Vector3f.add(position, delta, position);
    }

    public void translateTo(float x, float y, float z) {
        translateTo(new Vector3f(x, y, z));
    }

    public void translateTo(Vector3f newPos) {
        position = newPos;
    }
}
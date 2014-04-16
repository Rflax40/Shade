package shade.src.util;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.*;

import java.nio.*;

public class OrientationUtils {

    public static final int ORI_UP = 0;
    public static final int ORI_RIGHT = 1;
    public static final int ORI_FORWARD = 2;
    public static final int ORI_POSITION = 3;

    public static Matrix4f rotationMatrixFromQuaternion(Quaternion q) {
        float xx = q.x * q.x;
        float xy = q.x * q.y;
        float xz = q.x * q.z;
        float xw = q.x * q.w;
        float yy = q.y * q.y;
        float yz = q.y * q.z;
        float yw = q.y * q.w;
        float zz = q.z * q.z;
        float zw = q.z * q.w;
        Matrix4f mx = new Matrix4f();
        mx.m00 = 1 - 2 * (yy + zz);
        mx.m10 = 2 * (xy - zw);
        mx.m20 = 2 * (xz + yw);
        mx.m01 = 2 * (xy + zw);
        mx.m11 = 1 - 2 * (xx + zz);
        mx.m21 = 2 * (yz - xw);
        mx.m02 = 2 * (xz - yw);
        mx.m12 = 2 * (yz + xw);
        mx.m22 = 1 - 2 * (xx + yy);
        return mx;
    }

    public static void setCurrentGLMatrixTo(Matrix4f matrix) {
        FloatBuffer buff = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asFloatBuffer();
        matrix.store(buff);
        buff.flip();
        GL11.glLoadMatrix(buff);
    }

    public static Vector3f getVectorFromQuaternion(Quaternion ori, int id) {
        Vector3f res = null;
        float x = ori.x;
        float y = ori.y;
        float z = ori.z;
        float w = ori.w;
        float x1;
        float y1;
        float z1;
        switch (id) {
            case ORI_UP:
                x1 = 2 * (x * y + z * w);
                y1 = 1 - 2 * (x * x + z * z);
                z1 = 2 * (y * z - x * w);
                res = new Vector3f(x1, y1, z1);
                break;
            case ORI_RIGHT:
                x1 = 1 - 2 * (y * y + z * z);
                y1 = 2 * (x * y - z * w);
                z1 = 2 * (x * z + y * w);
                res = new Vector3f(x1, y1, z1);
                break;
            case ORI_FORWARD:
                x1 = 2 * (x * z - y * w);
                y1 = 2 * (y * z + x * w);
                z1 = 1 - 2 * (x * x + y * y);
                res = new Vector3f(x1, y1, z1);
                res.negate();
                break;
            default:
                res = null;
        }
        if (res != null) {
            res.normalise();
        }
        return res;
    }
}
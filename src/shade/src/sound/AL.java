package shade.src.sound;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

import static org.lwjgl.openal.AL10.AL_INVALID_ENUM;
import static org.lwjgl.openal.AL10.AL_INVALID_NAME;
import static org.lwjgl.openal.AL10.AL_INVALID_OPERATION;
import static org.lwjgl.openal.AL10.AL_INVALID_VALUE;
import static org.lwjgl.openal.AL10.AL_NO_ERROR;
import static org.lwjgl.openal.AL10.AL_OUT_OF_MEMORY;

public class AL {

    public static FloatBuffer toBuffer(Vector3f... vectors) {
        FloatBuffer buff = BufferUtils.createFloatBuffer(3 * vectors.length);
        for (Vector3f vector : vectors) {
            buff.put(vector.x).put(vector.y).put(vector.z);
        }
        buff.flip();
        return buff;
    }

    public static String getALError(int err) {
        switch (err) {
            case AL_NO_ERROR:
                return "AL_NO_ERROR";
            case AL_INVALID_NAME:
                return "AL_INVALID_NAME";
            case AL_INVALID_ENUM:
                return "AL_INVALID_ENUM";
            case AL_INVALID_VALUE:
                return "AL_INVALID_VALUE";
            case AL_INVALID_OPERATION:
                return "AL_INVALID_OPERATION";
            case AL_OUT_OF_MEMORY:
                return "AL_OUT_OF_MEMORY";
            default:
                return "UNKNOWN_ERROR";
        }
    }
}
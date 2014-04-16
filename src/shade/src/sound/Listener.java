package shade.src.sound;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

import static org.lwjgl.openal.AL10.AL_ORIENTATION;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_VELOCITY;
import static org.lwjgl.openal.AL10.alListener;

public class Listener extends SoundObject {

    public static Listener theListener;
    protected boolean isTheListener;
    private Vector3f up;
    private Vector3f forward;

    public Listener() {
        setPosition(new Vector3f(0, 0, 0));
        setVelocity(new Vector3f(0, 0, 0));
        setUp(new Vector3f(0, 1, 0));
        setForward(new Vector3f(0, 0, -1));
    }

    public Vector3f getForward() {
        return forward;
    }

    public void setForward(Vector3f forward) {
        this.forward = forward;
        if (isTheListener) {
            alListener(AL_ORIENTATION, toBuffer(up, forward));
        }
    }

    public void setPosition(Vector3f position) {
        super.setPosition(position);
        if (isTheListener) {
            alListener(AL_POSITION, toBuffer(position));
        }
    }

    public void setVelocity(Vector3f velocity) {
        super.setVelocity(velocity);
        if (isTheListener) {
            alListener(AL_VELOCITY, toBuffer(velocity));
        }
    }

    public Vector3f getUp() {
        return up;
    }

    public void setUp(Vector3f up) {
        this.up = up;
        if (isTheListener) {
            alListener(AL_ORIENTATION, toBuffer(up, forward));
        }
    }

    public Listener setTheListener() {
        if (theListener != null) {
            theListener.isTheListener = false;
        }
        isTheListener = true;
        alListener(AL_ORIENTATION, toBuffer(up, forward));
        alListener(AL_POSITION, toBuffer(position));
        alListener(AL_VELOCITY, toBuffer(velocity));
        return theListener = this;
    }

    public static FloatBuffer toBuffer(Vector3f... vectors) {
        FloatBuffer buff = BufferUtils.createFloatBuffer(3 * vectors.length);
        for (Vector3f vector : vectors) {
            buff.put(vector.x).put(vector.y).put(vector.z);
        }
        buff.flip();
        return buff;
    }
}
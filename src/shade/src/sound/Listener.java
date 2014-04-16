package shade.src.sound;

import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.openal.AL10.AL_ORIENTATION;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_VELOCITY;
import static org.lwjgl.openal.AL10.alListener;

public class Listener implements SoundObject {

    protected boolean isTheListener;

    private Vector3f position;
    private Vector3f velocity;
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
            alListener(AL_ORIENTATION, AL.toBuffer(up, forward));
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
        if (isTheListener) {
            alListener(AL_VELOCITY, AL.toBuffer(velocity));
        }
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        if (isTheListener) {
            alListener(AL_POSITION, AL.toBuffer(position));
        }
    }

    public Vector3f getUp() {
        return up;
    }

    public void setUp(Vector3f up) {
        this.up = up;
        if (isTheListener) {
            alListener(AL_ORIENTATION, AL.toBuffer(up, forward));
        }
    }

    public Listener setTheListener() {
        isTheListener = true;
        alListener(AL_ORIENTATION, AL.toBuffer(up, forward));
        alListener(AL_POSITION, AL.toBuffer(position));
        alListener(AL_VELOCITY, AL.toBuffer(velocity));
        return this;
    }
}
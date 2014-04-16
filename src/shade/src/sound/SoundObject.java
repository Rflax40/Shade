package shade.src.sound;

import org.lwjgl.util.vector.Vector3f;

public abstract class SoundObject {

    protected Vector3f position;
    protected Vector3f velocity;

    public void setPosition(float x, float y, float z) {
        setPosition(new Vector3f(x, y, z));
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f vector) {
        position = vector;
    }

    public void setVelocity(float x, float y, float z) {
        setVelocity(new Vector3f(x, y, z));
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f vector) {
        velocity = vector;
    }
}
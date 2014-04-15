package shade.src.sound;

import org.lwjgl.openal.*;
import org.lwjgl.util.vector.Vector3f;

import shade.src.resource.Resource;

public class SoundSource implements SoundObject, Resource {

	public final int sourceID;

	private Vector3f velocity;
	private Vector3f position;
	private float minimumRange;
	private float maximumRange;
	private float pitch;
	private float gain;

	public SoundSource() {
		sourceID = AL10.alGenSources();
		AL10.alSourcef(sourceID, AL10.AL_MIN_GAIN, 0F);
		int error = AL10.alGetError();
		assert error == AL10.AL_NO_ERROR : String.format(
			"OpenAL Error while loading source with id %i(%i)", sourceID, error);
		setVelocity(0, 0, 0);
		setPosition(0, 0, 0);
		setPitch(1);
		setGain(1);
	}

	public void addToQueue(SoundBuffer... buffers) {
		AL.addToSourceQueue(this, buffers);
	}

	public void clearQueue() {
		AL10.alSourceUnqueueBuffers(sourceID);
	}

	public float getGain() {
		return gain;
	}

	public float getMaximumRange() {
		return maximumRange;
	}

	public float getMinimumRange() {
		return minimumRange;
	}

	public float getPitch() {
		return pitch;
	}

	/**
	 * @return Time in milliseconds
	 */
	public long getPlayTime() {
		return (long) (AL10.alGetSourcef(sourceID, AL11.AL_SEC_OFFSET) * 1000);
	}

	public Vector3f getPosition() {
		return position;
	}

	public int getSampleOffset() {
		return AL10.alGetSourcei(sourceID, AL11.AL_SAMPLE_OFFSET);
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public boolean isPlaying() {
		return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	public void removeFromQueue(SoundBuffer... buffers) {
		AL.removeFromSourceQueue(this, buffers);
	}

	public SoundSource setGain(float g) {
		gain = g;
		// AL10.alSourcef(sourceID, AL10.AL_MAX_GAIN, gain);
		AL10.alSourcef(sourceID, AL10.AL_GAIN, gain);
		return this;
	}

    public SoundSource setLooping(boolean loop) {
        AL10.alSourcei(sourceID, AL10.AL_LOOPING, loop?AL10.AL_TRUE:AL10.AL_FALSE);
        return this;
    }

    public boolean isLooping() {
        return AL10.alGetSourcei(sourceID, AL10.AL_LOOPING) == AL10.AL_TRUE;
    }

	public SoundSource setMaximumRange(float mr) {
		maximumRange = mr;
		AL10.alSourcef(sourceID, AL10.AL_MAX_DISTANCE, maximumRange);
		return this;
	}

	public SoundSource setMinimumRange(float mr) {
		minimumRange = mr;
		AL10.alSourcef(sourceID, AL10.AL_REFERENCE_DISTANCE, minimumRange);
		return this;
	}

	public SoundSource setPitch(float p) {
		pitch = p;
		AL10.alSourcef(sourceID, AL10.AL_PITCH, pitch);
		return this;
	}

	public SoundSource setPosition(float x, float y, float z) {
		return setPosition(new Vector3f(x, y, z));
	}

	public SoundSource setPosition(Vector3f vector) {
		position = vector;
		AL10.alSource(sourceID, AL10.AL_POSITION, AL.toBuffer(position));
		return this;
	}

	public SoundSource setVelocity(float x, float y, float z) {
		return setVelocity(new Vector3f(x, y, z));
	}

	public SoundSource setVelocity(Vector3f vector) {
		velocity = vector;
		AL10.alSource(sourceID, AL10.AL_VELOCITY, AL.toBuffer(velocity));
		return this;
	}

	public void unload() {
		AL10.alDeleteSources(sourceID);
	}

}
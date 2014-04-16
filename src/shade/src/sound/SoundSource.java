package shade.src.sound;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import java.nio.IntBuffer;

public class SoundSource extends SoundObject {

    public final int sourceID;

    public SoundSource() {
        sourceID = AL10.alGenSources();
        AL10.alSourcef(sourceID, AL10.AL_MIN_GAIN, 0F);
        int error = AL10.alGetError();
        if (error != AL10.AL_NO_ERROR) {
            new Exception(String.format("OpenAL Error while loading source with id %d(%d)", sourceID, error)).printStackTrace();
        }
        setVelocity(0, 0, 0);
        setPosition(0, 0, 0);
        setPitch(1);
        setGain(1);
    }

    public void addToQueue(SoundBuffer... buffers) {
        for (SoundBuffer buffer : buffers) {
            AL10.alSourceQueueBuffers(sourceID, buffer.bufferID);
        }
    }

    public void removeFromQueue(SoundBuffer... buffers) {
        IntBuffer buffs = BufferUtils.createIntBuffer(buffers.length);
        for (SoundBuffer buffer : buffers) {
            buffs.put(buffer.bufferID);
        }
        buffs.flip();
        AL10.alSourceUnqueueBuffers(sourceID, buffs);
    }

    public void clearQueue() {
        AL10.alSourceUnqueueBuffers(sourceID);
    }

    public float getGain() {
        return AL10.alGetSourcef(sourceID, AL10.AL_GAIN);
    }

    public SoundSource setGain(float gain) {
        // AL10.alSourcef(sourceID, AL10.AL_MAX_GAIN, gain);
        AL10.alSourcef(sourceID, AL10.AL_GAIN, gain);
        return this;
    }

    public float getMaximumRange() {
        return AL10.alGetSourcef(sourceID, AL10.AL_MAX_DISTANCE);
    }

    public SoundSource setMaximumRange(float maximumRange) {
        AL10.alSourcef(sourceID, AL10.AL_MAX_DISTANCE, maximumRange);
        return this;
    }

    public float getMinimumRange() {
        return AL10.alGetSourcef(sourceID, AL10.AL_REFERENCE_DISTANCE);
    }

    public SoundSource setMinimumRange(float minimumRange) {
        AL10.alSourcef(sourceID, AL10.AL_REFERENCE_DISTANCE, minimumRange);
        return this;
    }

    public float getPitch() {
        return AL10.alGetSourcef(sourceID, AL10.AL_PITCH);
    }

    public SoundSource setPitch(float pitch) {
        AL10.alSourcef(sourceID, AL10.AL_PITCH, pitch);
        return this;
    }

    public float getPlayTime() {
        return AL10.alGetSourcef(sourceID, AL11.AL_SEC_OFFSET);
    }

    public void play() {
        AL10.alSourcePlay(sourceID);
    }

    public void pause() {
        AL10.alSourcePause(sourceID);
    }

    public void stop() {
        AL10.alSourceStop(sourceID);
    }

    public int getState() {
        return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE);
    }

    public boolean isLooping() {
        return AL10.alGetSourcei(sourceID, AL10.AL_LOOPING) == AL10.AL_TRUE;
    }

    public SoundSource setLooping(boolean loop) {
        AL10.alSourcei(sourceID, AL10.AL_LOOPING, loop? AL10.AL_TRUE : AL10.AL_FALSE);
        return this;
    }

    public void delete() {
        AL10.alDeleteSources(sourceID);
    }
}
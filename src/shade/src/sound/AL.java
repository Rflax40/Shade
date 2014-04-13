package shade.src.sound;

import static org.lwjgl.openal.AL10.*;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.util.vector.Vector3f;

public class AL {

	public static Listener theListener;

	public static void create() throws LWJGLException {
		org.lwjgl.openal.AL.create();
	}

	public static void destroy() {
		org.lwjgl.openal.AL.destroy();
	}

	public static void pause(SoundSource source) {
		alSourcePause(source.sourceID);
	}

	public static void play(SoundSource source) {
		alSourcePlay(source.sourceID);
	}

	public static void play(SoundSource source, SoundBuffer... buffers) {
		addToSourceQueue(source, buffers);
		play(source);
	}

	public static void addToSourceQueue(SoundSource source, SoundBuffer... buffers) {
		for (SoundBuffer buffer : buffers)
			alSourceQueueBuffers(source.sourceID, buffer.bufferID);
	}

	public static void removeFromSourceQueue(SoundSource source, SoundBuffer... buffers) {
		IntBuffer buffs = BufferUtils.createIntBuffer(buffers.length);
		for (SoundBuffer buffer : buffers)
			buffs.put(buffer.bufferID);
		buffs.flip();
		alSourceUnqueueBuffers(source.sourceID, buffs);
	}

	public static void setListener(Listener listener) {
		if (theListener != null)
			theListener.isTheListener = false;
		listener.isTheListener = true;
		theListener = listener;
	}

	public static void stop(SoundSource source) {
		alSourceStop(source.sourceID);
	}

	public static FloatBuffer toBuffer(Vector3f... vectors) {
		FloatBuffer buff = BufferUtils.createFloatBuffer(3 * vectors.length);
		for (Vector3f vector : vectors)
			buff.put(vector.x).put(vector.y).put(vector.z);
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
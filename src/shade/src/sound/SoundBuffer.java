package shade.src.sound;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import shade.src.resource.Resource;
import shade.src.resource.Unloadable;

import java.io.IOException;
import java.nio.ByteBuffer;

public class SoundBuffer implements Unloadable {

    public final int bufferID;
    public final ByteBuffer data;
    private boolean hasSet;

    public SoundBuffer(String wav) throws IOException {
        this(WaveData.create(Resource.getResource(wav).getAsStream()));
    }

    public SoundBuffer(WaveData wav) {
        bufferID = AL10.alGenBuffers();
        data = wav.data.duplicate();
        setBufferData(wav);
        wav.dispose();
    }

    public void setBufferData(WaveData wav) {
        if (!hasSet) {
            AL10.alBufferData(bufferID, wav.format, wav.data, wav.samplerate);
            hasSet = true;
        }
    }

    public void unload() {
        AL10.alDeleteBuffers(bufferID);
    }
}
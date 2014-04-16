package shade.src.sound;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import shade.src.resource.Resource;
import shade.src.resource.ResourceBased;

import java.io.InputStream;
import java.nio.ByteBuffer;

public class SoundBuffer extends ResourceBased {

    private static final Factory bufferFactory = new Factory() {
        @Override
        public ResourceBased construct(Resource resource) {
            ByteBuffer buffer = null;
            try (InputStream in = resource.getAsStream()) {
                WaveData wave = WaveData.create(in);
                buffer = wave.data.duplicate();
                int bufferID = AL10.alGenBuffers();
                AL10.alBufferData(bufferID, wave.format, wave.data, wave.samplerate);
                wave.dispose();
                return new SoundBuffer(resource, bufferID, buffer);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public Class getClassType() {
            return SoundBuffer.class;
        }
    };
    public final int bufferID;
    public final ByteBuffer data;

    private SoundBuffer(Resource res, int bID, ByteBuffer buffer) {
        super(res);
        bufferID = bID;
        data = buffer;
    }

    @Override
    public void unload() {
        super.unload();
        AL10.alDeleteBuffers(bufferID);
    }

    public static SoundBuffer getSoundBuffer(Resource resource) {
        return (SoundBuffer) getResourceBased(resource, bufferFactory);
    }
}
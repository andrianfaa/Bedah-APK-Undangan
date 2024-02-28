package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;

public class ByteBufferUtil {
    public static int getSizePrefix(ByteBuffer bb) {
        return bb.getInt(bb.position());
    }

    public static ByteBuffer removeSizePrefix(ByteBuffer bb) {
        ByteBuffer duplicate = bb.duplicate();
        duplicate.position(duplicate.position() + 4);
        return duplicate;
    }
}

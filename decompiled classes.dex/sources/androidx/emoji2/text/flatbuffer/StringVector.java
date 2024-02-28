package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;
import mt.Log1F380D;

/* compiled from: 0072 */
public final class StringVector extends BaseVector {
    private Utf8 utf8 = Utf8.getDefault();

    public StringVector __assign(int _vector, int _element_size, ByteBuffer _bb) {
        __reset(_vector, _element_size, _bb);
        return this;
    }

    public String get(int j) {
        String __string = Table.__string(__element(j), this.bb, this.utf8);
        Log1F380D.a((Object) __string);
        return __string;
    }
}

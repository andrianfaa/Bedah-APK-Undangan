package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Comparator;
import mt.Log1F380D;

/* compiled from: 0071 */
public class Table {
    protected ByteBuffer bb;
    protected int bb_pos;
    Utf8 utf8 = Utf8.getDefault();
    private int vtable_size;
    private int vtable_start;

    protected static boolean __has_identifier(ByteBuffer bb2, String ident) {
        if (ident.length() == 4) {
            for (int i = 0; i < 4; i++) {
                if (ident.charAt(i) != ((char) bb2.get(bb2.position() + 4 + i))) {
                    return false;
                }
            }
            return true;
        }
        throw new AssertionError("FlatBuffers: file identifier must be length 4");
    }

    /* access modifiers changed from: protected */
    public static int __indirect(int offset, ByteBuffer bb2) {
        return bb2.getInt(offset) + offset;
    }

    protected static int __offset(int vtable_offset, int offset, ByteBuffer bb2) {
        int capacity = bb2.capacity() - offset;
        return bb2.getShort((capacity + vtable_offset) - bb2.getInt(capacity)) + capacity;
    }

    protected static String __string(int offset, ByteBuffer bb2, Utf8 utf82) {
        int offset2 = offset + bb2.getInt(offset);
        return utf82.decodeUtf8(bb2, offset2 + 4, bb2.getInt(offset2));
    }

    protected static Table __union(Table t, int offset, ByteBuffer bb2) {
        t.__reset(__indirect(offset, bb2), bb2);
        return t;
    }

    protected static int compareStrings(int offset_1, int offset_2, ByteBuffer bb2) {
        int offset_12 = offset_1 + bb2.getInt(offset_1);
        int offset_22 = offset_2 + bb2.getInt(offset_2);
        int i = bb2.getInt(offset_12);
        int i2 = bb2.getInt(offset_22);
        int i3 = offset_12 + 4;
        int i4 = offset_22 + 4;
        int min = Math.min(i, i2);
        for (int i5 = 0; i5 < min; i5++) {
            if (bb2.get(i5 + i3) != bb2.get(i5 + i4)) {
                return bb2.get(i5 + i3) - bb2.get(i5 + i4);
            }
        }
        return i - i2;
    }

    protected static int compareStrings(int offset_1, byte[] key, ByteBuffer bb2) {
        int offset_12 = offset_1 + bb2.getInt(offset_1);
        int i = bb2.getInt(offset_12);
        int length = key.length;
        int i2 = offset_12 + 4;
        int min = Math.min(i, length);
        for (int i3 = 0; i3 < min; i3++) {
            if (bb2.get(i3 + i2) != key[i3]) {
                return bb2.get(i3 + i2) - key[i3];
            }
        }
        return i - length;
    }

    /* access modifiers changed from: protected */
    public int __indirect(int offset) {
        return this.bb.getInt(offset) + offset;
    }

    /* access modifiers changed from: protected */
    public int __offset(int vtable_offset) {
        if (vtable_offset < this.vtable_size) {
            return this.bb.getShort(this.vtable_start + vtable_offset);
        }
        return 0;
    }

    public void __reset() {
        __reset(0, (ByteBuffer) null);
    }

    /* access modifiers changed from: protected */
    public void __reset(int _i, ByteBuffer _bb) {
        this.bb = _bb;
        if (_bb != null) {
            this.bb_pos = _i;
            int i = _i - _bb.getInt(_i);
            this.vtable_start = i;
            this.vtable_size = this.bb.getShort(i);
            return;
        }
        this.bb_pos = 0;
        this.vtable_start = 0;
        this.vtable_size = 0;
    }

    /* access modifiers changed from: protected */
    public String __string(int offset) {
        String __string = __string(offset, this.bb, this.utf8);
        Log1F380D.a((Object) __string);
        return __string;
    }

    /* access modifiers changed from: protected */
    public Table __union(Table t, int offset) {
        return __union(t, offset, this.bb);
    }

    /* access modifiers changed from: protected */
    public int __vector(int offset) {
        int offset2 = offset + this.bb_pos;
        return this.bb.getInt(offset2) + offset2 + 4;
    }

    /* access modifiers changed from: protected */
    public ByteBuffer __vector_as_bytebuffer(int vector_offset, int elem_size) {
        int __offset = __offset(vector_offset);
        if (__offset == 0) {
            return null;
        }
        ByteBuffer order = this.bb.duplicate().order(ByteOrder.LITTLE_ENDIAN);
        int __vector = __vector(__offset);
        order.position(__vector);
        order.limit((__vector_len(__offset) * elem_size) + __vector);
        return order;
    }

    /* access modifiers changed from: protected */
    public ByteBuffer __vector_in_bytebuffer(ByteBuffer bb2, int vector_offset, int elem_size) {
        int __offset = __offset(vector_offset);
        if (__offset == 0) {
            return null;
        }
        int __vector = __vector(__offset);
        bb2.rewind();
        bb2.limit((__vector_len(__offset) * elem_size) + __vector);
        bb2.position(__vector);
        return bb2;
    }

    /* access modifiers changed from: protected */
    public int __vector_len(int offset) {
        int offset2 = offset + this.bb_pos;
        return this.bb.getInt(offset2 + this.bb.getInt(offset2));
    }

    public ByteBuffer getByteBuffer() {
        return this.bb;
    }

    /* access modifiers changed from: protected */
    public int keysCompare(Integer o1, Integer o2, ByteBuffer bb2) {
        return 0;
    }

    /* access modifiers changed from: protected */
    public void sortTables(int[] offsets, final ByteBuffer bb2) {
        Integer[] numArr = new Integer[offsets.length];
        for (int i = 0; i < offsets.length; i++) {
            numArr[i] = Integer.valueOf(offsets[i]);
        }
        Arrays.sort(numArr, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return Table.this.keysCompare(o1, o2, bb2);
            }
        });
        for (int i2 = 0; i2 < offsets.length; i2++) {
            offsets[i2] = numArr[i2].intValue();
        }
    }
}

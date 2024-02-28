package androidx.emoji2.text.flatbuffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import kotlin.UByte;

public class FlatBufferBuilder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    ByteBuffer bb;
    ByteBufferFactory bb_factory;
    boolean finished;
    boolean force_defaults;
    int minalign;
    boolean nested;
    int num_vtables;
    int object_start;
    int space;
    final Utf8 utf8;
    int vector_num_elems;
    int[] vtable;
    int vtable_in_use;
    int[] vtables;

    static class ByteBufferBackedInputStream extends InputStream {
        ByteBuffer buf;

        public ByteBufferBackedInputStream(ByteBuffer buf2) {
            this.buf = buf2;
        }

        public int read() throws IOException {
            try {
                return this.buf.get() & UByte.MAX_VALUE;
            } catch (BufferUnderflowException e) {
                return -1;
            }
        }
    }

    public static abstract class ByteBufferFactory {
        public abstract ByteBuffer newByteBuffer(int i);

        public void releaseByteBuffer(ByteBuffer bb) {
        }
    }

    public static final class HeapByteBufferFactory extends ByteBufferFactory {
        public static final HeapByteBufferFactory INSTANCE = new HeapByteBufferFactory();

        public ByteBuffer newByteBuffer(int capacity) {
            return ByteBuffer.allocate(capacity).order(ByteOrder.LITTLE_ENDIAN);
        }
    }

    public FlatBufferBuilder() {
        this(1024);
    }

    public FlatBufferBuilder(int initial_size) {
        this(initial_size, HeapByteBufferFactory.INSTANCE, (ByteBuffer) null, Utf8.getDefault());
    }

    public FlatBufferBuilder(int initial_size, ByteBufferFactory bb_factory2) {
        this(initial_size, bb_factory2, (ByteBuffer) null, Utf8.getDefault());
    }

    public FlatBufferBuilder(int initial_size, ByteBufferFactory bb_factory2, ByteBuffer existing_bb, Utf8 utf82) {
        this.minalign = 1;
        this.vtable = null;
        this.vtable_in_use = 0;
        this.nested = false;
        this.finished = false;
        this.vtables = new int[16];
        this.num_vtables = 0;
        this.vector_num_elems = 0;
        this.force_defaults = false;
        initial_size = initial_size <= 0 ? 1 : initial_size;
        this.bb_factory = bb_factory2;
        if (existing_bb != null) {
            this.bb = existing_bb;
            existing_bb.clear();
            this.bb.order(ByteOrder.LITTLE_ENDIAN);
        } else {
            this.bb = bb_factory2.newByteBuffer(initial_size);
        }
        this.utf8 = utf82;
        this.space = this.bb.capacity();
    }

    public FlatBufferBuilder(ByteBuffer existing_bb) {
        this(existing_bb, (ByteBufferFactory) new HeapByteBufferFactory());
    }

    public FlatBufferBuilder(ByteBuffer existing_bb, ByteBufferFactory bb_factory2) {
        this(existing_bb.capacity(), bb_factory2, existing_bb, Utf8.getDefault());
    }

    @Deprecated
    private int dataStart() {
        finished();
        return this.space;
    }

    static ByteBuffer growByteBuffer(ByteBuffer bb2, ByteBufferFactory bb_factory2) {
        int capacity = bb2.capacity();
        if ((-1073741824 & capacity) == 0) {
            int i = capacity == 0 ? 1 : capacity << 1;
            bb2.position(0);
            ByteBuffer newByteBuffer = bb_factory2.newByteBuffer(i);
            newByteBuffer.position(newByteBuffer.clear().capacity() - capacity);
            newByteBuffer.put(bb2);
            return newByteBuffer;
        }
        throw new AssertionError("FlatBuffers: cannot grow buffer beyond 2 gigabytes.");
    }

    public static boolean isFieldPresent(Table table, int offset) {
        return table.__offset(offset) != 0;
    }

    public void Nested(int obj) {
        if (obj != offset()) {
            throw new AssertionError("FlatBuffers: struct must be serialized inline.");
        }
    }

    public void addBoolean(int o, boolean x, boolean d) {
        if (this.force_defaults || x != d) {
            addBoolean(x);
            slot(o);
        }
    }

    public void addBoolean(boolean x) {
        prep(1, 0);
        putBoolean(x);
    }

    public void addByte(byte x) {
        prep(1, 0);
        putByte(x);
    }

    public void addByte(int o, byte x, int d) {
        if (this.force_defaults || x != d) {
            addByte(x);
            slot(o);
        }
    }

    public void addDouble(double x) {
        prep(8, 0);
        putDouble(x);
    }

    public void addDouble(int o, double x, double d) {
        if (this.force_defaults || x != d) {
            addDouble(x);
            slot(o);
        }
    }

    public void addFloat(float x) {
        prep(4, 0);
        putFloat(x);
    }

    public void addFloat(int o, float x, double d) {
        if (this.force_defaults || ((double) x) != d) {
            addFloat(x);
            slot(o);
        }
    }

    public void addInt(int x) {
        prep(4, 0);
        putInt(x);
    }

    public void addInt(int o, int x, int d) {
        if (this.force_defaults || x != d) {
            addInt(x);
            slot(o);
        }
    }

    public void addLong(int o, long x, long d) {
        if (this.force_defaults || x != d) {
            addLong(x);
            slot(o);
        }
    }

    public void addLong(long x) {
        prep(8, 0);
        putLong(x);
    }

    public void addOffset(int off) {
        prep(4, 0);
        if (off <= offset()) {
            putInt((offset() - off) + 4);
            return;
        }
        throw new AssertionError();
    }

    public void addOffset(int o, int x, int d) {
        if (this.force_defaults || x != d) {
            addOffset(x);
            slot(o);
        }
    }

    public void addShort(int o, short x, int d) {
        if (this.force_defaults || x != d) {
            addShort(x);
            slot(o);
        }
    }

    public void addShort(short x) {
        prep(2, 0);
        putShort(x);
    }

    public void addStruct(int voffset, int x, int d) {
        if (x != d) {
            Nested(x);
            slot(voffset);
        }
    }

    public void clear() {
        this.space = this.bb.capacity();
        this.bb.clear();
        this.minalign = 1;
        while (true) {
            int i = this.vtable_in_use;
            if (i > 0) {
                int[] iArr = this.vtable;
                int i2 = i - 1;
                this.vtable_in_use = i2;
                iArr[i2] = 0;
            } else {
                this.vtable_in_use = 0;
                this.nested = false;
                this.finished = false;
                this.object_start = 0;
                this.num_vtables = 0;
                this.vector_num_elems = 0;
                return;
            }
        }
    }

    public int createByteVector(ByteBuffer byteBuffer) {
        int remaining = byteBuffer.remaining();
        startVector(1, remaining, 1);
        ByteBuffer byteBuffer2 = this.bb;
        int i = this.space - remaining;
        this.space = i;
        byteBuffer2.position(i);
        this.bb.put(byteBuffer);
        return endVector();
    }

    public int createByteVector(byte[] arr) {
        int length = arr.length;
        startVector(1, length, 1);
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - length;
        this.space = i;
        byteBuffer.position(i);
        this.bb.put(arr);
        return endVector();
    }

    public int createByteVector(byte[] arr, int offset, int length) {
        startVector(1, length, 1);
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - length;
        this.space = i;
        byteBuffer.position(i);
        this.bb.put(arr, offset, length);
        return endVector();
    }

    public <T extends Table> int createSortedVectorOfTables(T t, int[] offsets) {
        t.sortTables(offsets, this.bb);
        return createVectorOfTables(offsets);
    }

    public int createString(CharSequence s) {
        int encodedLength = this.utf8.encodedLength(s);
        addByte((byte) 0);
        startVector(1, encodedLength, 1);
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - encodedLength;
        this.space = i;
        byteBuffer.position(i);
        this.utf8.encodeUtf8(s, this.bb);
        return endVector();
    }

    public int createString(ByteBuffer s) {
        int remaining = s.remaining();
        addByte((byte) 0);
        startVector(1, remaining, 1);
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - remaining;
        this.space = i;
        byteBuffer.position(i);
        this.bb.put(s);
        return endVector();
    }

    public ByteBuffer createUnintializedVector(int elem_size, int num_elems, int alignment) {
        int i = elem_size * num_elems;
        startVector(elem_size, num_elems, alignment);
        ByteBuffer byteBuffer = this.bb;
        int i2 = this.space - i;
        this.space = i2;
        byteBuffer.position(i2);
        ByteBuffer order = this.bb.slice().order(ByteOrder.LITTLE_ENDIAN);
        order.limit(i);
        return order;
    }

    public int createVectorOfTables(int[] offsets) {
        notNested();
        startVector(4, offsets.length, 4);
        for (int length = offsets.length - 1; length >= 0; length--) {
            addOffset(offsets[length]);
        }
        return endVector();
    }

    public ByteBuffer dataBuffer() {
        finished();
        return this.bb;
    }

    public int endTable() {
        if (this.vtable == null || !this.nested) {
            throw new AssertionError("FlatBuffers: endTable called without startTable");
        }
        addInt(0);
        int offset = offset();
        int i = this.vtable_in_use - 1;
        while (i >= 0 && this.vtable[i] == 0) {
            i--;
        }
        int i2 = i + 1;
        while (i >= 0) {
            int i3 = this.vtable[i];
            addShort((short) (i3 != 0 ? offset - i3 : 0));
            i--;
        }
        addShort((short) (offset - this.object_start));
        addShort((short) ((i2 + 2) * 2));
        int i4 = 0;
        int i5 = 0;
        loop2:
        while (true) {
            if (i5 >= this.num_vtables) {
                break;
            }
            int capacity = this.bb.capacity() - this.vtables[i5];
            int i6 = this.space;
            short s = this.bb.getShort(capacity);
            if (s == this.bb.getShort(i6)) {
                int i7 = 2;
                while (i7 < s) {
                    if (this.bb.getShort(capacity + i7) == this.bb.getShort(i6 + i7)) {
                        i7 += 2;
                    }
                }
                i4 = this.vtables[i5];
                break loop2;
            }
            i5++;
        }
        if (i4 != 0) {
            int capacity2 = this.bb.capacity() - offset;
            this.space = capacity2;
            this.bb.putInt(capacity2, i4 - offset);
        } else {
            int i8 = this.num_vtables;
            int[] iArr = this.vtables;
            if (i8 == iArr.length) {
                this.vtables = Arrays.copyOf(iArr, i8 * 2);
            }
            int[] iArr2 = this.vtables;
            int i9 = this.num_vtables;
            this.num_vtables = i9 + 1;
            iArr2[i9] = offset();
            ByteBuffer byteBuffer = this.bb;
            byteBuffer.putInt(byteBuffer.capacity() - offset, offset() - offset);
        }
        this.nested = false;
        return offset;
    }

    public int endVector() {
        if (this.nested) {
            this.nested = false;
            putInt(this.vector_num_elems);
            return offset();
        }
        throw new AssertionError("FlatBuffers: endVector called without startVector");
    }

    public void finish(int root_table) {
        finish(root_table, false);
    }

    public void finish(int root_table, String file_identifier) {
        finish(root_table, file_identifier, false);
    }

    /* access modifiers changed from: protected */
    public void finish(int root_table, String file_identifier, boolean size_prefix) {
        prep(this.minalign, (size_prefix ? 4 : 0) + 8);
        if (file_identifier.length() == 4) {
            for (int i = 3; i >= 0; i--) {
                addByte((byte) file_identifier.charAt(i));
            }
            finish(root_table, size_prefix);
            return;
        }
        throw new AssertionError("FlatBuffers: file identifier must be length 4");
    }

    /* access modifiers changed from: protected */
    public void finish(int root_table, boolean size_prefix) {
        prep(this.minalign, (size_prefix ? 4 : 0) + 4);
        addOffset(root_table);
        if (size_prefix) {
            addInt(this.bb.capacity() - this.space);
        }
        this.bb.position(this.space);
        this.finished = true;
    }

    public void finishSizePrefixed(int root_table) {
        finish(root_table, true);
    }

    public void finishSizePrefixed(int root_table, String file_identifier) {
        finish(root_table, file_identifier, true);
    }

    public void finished() {
        if (!this.finished) {
            throw new AssertionError("FlatBuffers: you can only access the serialized buffer after it has been finished by FlatBufferBuilder.finish().");
        }
    }

    public FlatBufferBuilder forceDefaults(boolean forceDefaults) {
        this.force_defaults = forceDefaults;
        return this;
    }

    public FlatBufferBuilder init(ByteBuffer existing_bb, ByteBufferFactory bb_factory2) {
        this.bb_factory = bb_factory2;
        this.bb = existing_bb;
        existing_bb.clear();
        this.bb.order(ByteOrder.LITTLE_ENDIAN);
        this.minalign = 1;
        this.space = this.bb.capacity();
        this.vtable_in_use = 0;
        this.nested = false;
        this.finished = false;
        this.object_start = 0;
        this.num_vtables = 0;
        this.vector_num_elems = 0;
        return this;
    }

    public void notNested() {
        if (this.nested) {
            throw new AssertionError("FlatBuffers: object serialization must not be nested.");
        }
    }

    public int offset() {
        return this.bb.capacity() - this.space;
    }

    public void pad(int byte_size) {
        for (int i = 0; i < byte_size; i++) {
            ByteBuffer byteBuffer = this.bb;
            int i2 = this.space - 1;
            this.space = i2;
            byteBuffer.put(i2, (byte) 0);
        }
    }

    public void prep(int size, int additional_bytes) {
        if (size > this.minalign) {
            this.minalign = size;
        }
        int i = ((~((this.bb.capacity() - this.space) + additional_bytes)) + 1) & (size - 1);
        while (this.space < i + size + additional_bytes) {
            int capacity = this.bb.capacity();
            ByteBuffer byteBuffer = this.bb;
            ByteBuffer growByteBuffer = growByteBuffer(byteBuffer, this.bb_factory);
            this.bb = growByteBuffer;
            if (byteBuffer != growByteBuffer) {
                this.bb_factory.releaseByteBuffer(byteBuffer);
            }
            this.space += this.bb.capacity() - capacity;
        }
        pad(i);
    }

    public void putBoolean(boolean x) {
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - 1;
        this.space = i;
        byteBuffer.put(i, x ? (byte) 1 : 0);
    }

    public void putByte(byte x) {
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - 1;
        this.space = i;
        byteBuffer.put(i, x);
    }

    public void putDouble(double x) {
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - 8;
        this.space = i;
        byteBuffer.putDouble(i, x);
    }

    public void putFloat(float x) {
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - 4;
        this.space = i;
        byteBuffer.putFloat(i, x);
    }

    public void putInt(int x) {
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - 4;
        this.space = i;
        byteBuffer.putInt(i, x);
    }

    public void putLong(long x) {
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - 8;
        this.space = i;
        byteBuffer.putLong(i, x);
    }

    public void putShort(short x) {
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - 2;
        this.space = i;
        byteBuffer.putShort(i, x);
    }

    public void required(int table, int field) {
        int capacity = this.bb.capacity() - table;
        if (!(this.bb.getShort((capacity - this.bb.getInt(capacity)) + field) != 0)) {
            throw new AssertionError("FlatBuffers: field " + field + " must be set");
        }
    }

    public byte[] sizedByteArray() {
        return sizedByteArray(this.space, this.bb.capacity() - this.space);
    }

    public byte[] sizedByteArray(int start, int length) {
        finished();
        byte[] bArr = new byte[length];
        this.bb.position(start);
        this.bb.get(bArr);
        return bArr;
    }

    public InputStream sizedInputStream() {
        finished();
        ByteBuffer duplicate = this.bb.duplicate();
        duplicate.position(this.space);
        duplicate.limit(this.bb.capacity());
        return new ByteBufferBackedInputStream(duplicate);
    }

    public void slot(int voffset) {
        this.vtable[voffset] = offset();
    }

    public void startTable(int numfields) {
        notNested();
        int[] iArr = this.vtable;
        if (iArr == null || iArr.length < numfields) {
            this.vtable = new int[numfields];
        }
        this.vtable_in_use = numfields;
        Arrays.fill(this.vtable, 0, numfields, 0);
        this.nested = true;
        this.object_start = offset();
    }

    public void startVector(int elem_size, int num_elems, int alignment) {
        notNested();
        this.vector_num_elems = num_elems;
        prep(4, elem_size * num_elems);
        prep(alignment, elem_size * num_elems);
        this.nested = true;
    }
}

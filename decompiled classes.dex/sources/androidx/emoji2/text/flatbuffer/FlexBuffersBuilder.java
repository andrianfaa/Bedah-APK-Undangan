package androidx.emoji2.text.flatbuffer;

import androidx.emoji2.text.flatbuffer.FlexBuffers;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class FlexBuffersBuilder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int BUILDER_FLAG_NONE = 0;
    public static final int BUILDER_FLAG_SHARE_ALL = 7;
    public static final int BUILDER_FLAG_SHARE_KEYS = 1;
    public static final int BUILDER_FLAG_SHARE_KEYS_AND_STRINGS = 3;
    public static final int BUILDER_FLAG_SHARE_KEY_VECTORS = 4;
    public static final int BUILDER_FLAG_SHARE_STRINGS = 2;
    private static final int WIDTH_16 = 1;
    private static final int WIDTH_32 = 2;
    private static final int WIDTH_64 = 3;
    private static final int WIDTH_8 = 0;
    /* access modifiers changed from: private */
    public final ReadWriteBuf bb;
    private boolean finished;
    private final int flags;
    private Comparator<Value> keyComparator;
    private final HashMap<String, Integer> keyPool;
    private final ArrayList<Value> stack;
    private final HashMap<String, Integer> stringPool;

    private static class Value {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        final double dValue;
        long iValue;
        int key;
        final int minBitWidth;
        final int type;

        static {
            Class<FlexBuffersBuilder> cls = FlexBuffersBuilder.class;
        }

        Value(int key2, int type2, int bitWidth, double dValue2) {
            this.key = key2;
            this.type = type2;
            this.minBitWidth = bitWidth;
            this.dValue = dValue2;
            this.iValue = Long.MIN_VALUE;
        }

        Value(int key2, int type2, int bitWidth, long iValue2) {
            this.key = key2;
            this.type = type2;
            this.minBitWidth = bitWidth;
            this.iValue = iValue2;
            this.dValue = Double.MIN_VALUE;
        }

        static Value blob(int key2, int position, int type2, int bitWidth) {
            return new Value(key2, type2, bitWidth, (long) position);
        }

        static Value bool(int key2, boolean b) {
            return new Value(key2, 26, 0, b ? 1 : 0);
        }

        /* access modifiers changed from: private */
        public int elemWidth(int bufSize, int elemIndex) {
            return elemWidth(this.type, this.minBitWidth, this.iValue, bufSize, elemIndex);
        }

        /* access modifiers changed from: private */
        public static int elemWidth(int type2, int minBitWidth2, long iValue2, int bufSize, int elemIndex) {
            if (FlexBuffers.isTypeInline(type2)) {
                return minBitWidth2;
            }
            for (int i = 1; i <= 32; i *= 2) {
                int widthUInBits = FlexBuffersBuilder.widthUInBits((long) ((int) (((long) ((paddingBytes(bufSize, i) + bufSize) + (elemIndex * i))) - iValue2)));
                if ((1 << widthUInBits) == ((long) i)) {
                    return widthUInBits;
                }
            }
            throw new AssertionError();
        }

        static Value float32(int key2, float value) {
            return new Value(key2, 3, 2, (double) value);
        }

        static Value float64(int key2, double value) {
            return new Value(key2, 3, 3, value);
        }

        static Value int16(int key2, int value) {
            return new Value(key2, 1, 1, (long) value);
        }

        static Value int32(int key2, int value) {
            return new Value(key2, 1, 2, (long) value);
        }

        static Value int64(int key2, long value) {
            return new Value(key2, 1, 3, value);
        }

        static Value int8(int key2, int value) {
            return new Value(key2, 1, 0, (long) value);
        }

        private static byte packedType(int bitWidth, int type2) {
            return (byte) ((type2 << 2) | bitWidth);
        }

        /* access modifiers changed from: private */
        public static int paddingBytes(int bufSize, int scalarSize) {
            return ((~bufSize) + 1) & (scalarSize - 1);
        }

        /* access modifiers changed from: private */
        public byte storedPackedType() {
            return storedPackedType(0);
        }

        /* access modifiers changed from: private */
        public byte storedPackedType(int parentBitWidth) {
            return packedType(storedWidth(parentBitWidth), this.type);
        }

        private int storedWidth(int parentBitWidth) {
            return FlexBuffers.isTypeInline(this.type) ? Math.max(this.minBitWidth, parentBitWidth) : this.minBitWidth;
        }

        static Value uInt16(int key2, int value) {
            return new Value(key2, 2, 1, (long) value);
        }

        static Value uInt32(int key2, int value) {
            return new Value(key2, 2, 2, (long) value);
        }

        static Value uInt64(int key2, long value) {
            return new Value(key2, 2, 3, value);
        }

        static Value uInt8(int key2, int value) {
            return new Value(key2, 2, 0, (long) value);
        }
    }

    public FlexBuffersBuilder() {
        this(256);
    }

    public FlexBuffersBuilder(int bufSize) {
        this((ReadWriteBuf) new ArrayReadWriteBuf(bufSize), 1);
    }

    public FlexBuffersBuilder(ReadWriteBuf bb2, int flags2) {
        this.stack = new ArrayList<>();
        this.keyPool = new HashMap<>();
        this.stringPool = new HashMap<>();
        this.finished = false;
        this.keyComparator = new Comparator<Value>() {
            public int compare(Value o1, Value o2) {
                byte b;
                byte b2;
                int i = o1.key;
                int i2 = o2.key;
                do {
                    b = FlexBuffersBuilder.this.bb.get(i);
                    b2 = FlexBuffersBuilder.this.bb.get(i2);
                    if (b == 0) {
                        return b - b2;
                    }
                    i++;
                    i2++;
                } while (b == b2);
                return b - b2;
            }
        };
        this.bb = bb2;
        this.flags = flags2;
    }

    public FlexBuffersBuilder(ByteBuffer bb2) {
        this(bb2, 1);
    }

    @Deprecated
    public FlexBuffersBuilder(ByteBuffer bb2, int flags2) {
        this((ReadWriteBuf) new ArrayReadWriteBuf(bb2.array()), flags2);
    }

    private int align(int alignment) {
        int i = 1 << alignment;
        int access$100 = Value.paddingBytes(this.bb.writePosition(), i);
        while (true) {
            int i2 = access$100 - 1;
            if (access$100 == 0) {
                return i;
            }
            this.bb.put((byte) 0);
            access$100 = i2;
        }
    }

    private Value createKeyVector(int start, int length) {
        int max = Math.max(0, widthUInBits((long) length));
        for (int i = start; i < this.stack.size(); i++) {
            max = Math.max(max, Value.elemWidth(4, 0, (long) this.stack.get(i).key, this.bb.writePosition(), i + 1));
        }
        int align = align(max);
        writeInt((long) length, align);
        int writePosition = this.bb.writePosition();
        int i2 = start;
        while (i2 < this.stack.size()) {
            if (this.stack.get(i2).key != -1) {
                writeOffset((long) this.stack.get(i2).key, align);
                i2++;
            } else {
                throw new AssertionError();
            }
        }
        return new Value(-1, FlexBuffers.toTypedVector(4, 0), max, (long) writePosition);
    }

    private Value createVector(int key, int start, int length, boolean typed, boolean fixed, Value keys) {
        int i;
        int i2;
        int i3 = length;
        Value value = keys;
        if (!fixed || typed) {
            int i4 = 0;
            int max = Math.max(0, widthUInBits((long) i3));
            int i5 = 1;
            if (value != null) {
                max = Math.max(max, value.elemWidth(this.bb.writePosition(), 0));
                i5 = 1 + 2;
            }
            int i6 = 4;
            for (int i7 = start; i7 < this.stack.size(); i7++) {
                max = Math.max(max, this.stack.get(i7).elemWidth(this.bb.writePosition(), i7 + i5));
                if (!typed) {
                    int i8 = start;
                } else if (i7 == start) {
                    i6 = this.stack.get(i7).type;
                    if (!FlexBuffers.isTypedVectorElementType(i6)) {
                        throw new FlexBuffers.FlexBufferException("TypedVector does not support this element type");
                    }
                } else if (i6 != this.stack.get(i7).type) {
                    throw new AssertionError();
                }
            }
            int i9 = start;
            if (!fixed || FlexBuffers.isTypedVectorElementType(i6)) {
                int align = align(max);
                if (value != null) {
                    writeOffset(value.iValue, align);
                    writeInt(1 << value.minBitWidth, align);
                }
                if (!fixed) {
                    writeInt((long) i3, align);
                }
                int writePosition = this.bb.writePosition();
                for (int i10 = start; i10 < this.stack.size(); i10++) {
                    writeAny(this.stack.get(i10), align);
                }
                if (!typed) {
                    for (int i11 = start; i11 < this.stack.size(); i11++) {
                        this.bb.put(this.stack.get(i11).storedPackedType(max));
                    }
                }
                if (value != null) {
                    i = 9;
                } else {
                    if (typed) {
                        if (fixed) {
                            i4 = i3;
                        }
                        i2 = FlexBuffers.toTypedVector(i6, i4);
                    } else {
                        i2 = 10;
                    }
                    i = i2;
                }
                return new Value(key, i, max, (long) writePosition);
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    private int putKey(String key) {
        if (key == null) {
            return -1;
        }
        int writePosition = this.bb.writePosition();
        if ((this.flags & 1) != 0) {
            Integer num = this.keyPool.get(key);
            if (num != null) {
                return num.intValue();
            }
            byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
            this.bb.put(bytes, 0, bytes.length);
            this.bb.put((byte) 0);
            this.keyPool.put(key, Integer.valueOf(writePosition));
            return writePosition;
        }
        byte[] bytes2 = key.getBytes(StandardCharsets.UTF_8);
        this.bb.put(bytes2, 0, bytes2.length);
        this.bb.put((byte) 0);
        this.keyPool.put(key, Integer.valueOf(writePosition));
        return writePosition;
    }

    private void putUInt(String key, long value) {
        int putKey = putKey(key);
        int widthUInBits = widthUInBits(value);
        this.stack.add(widthUInBits == 0 ? Value.uInt8(putKey, (int) value) : widthUInBits == 1 ? Value.uInt16(putKey, (int) value) : widthUInBits == 2 ? Value.uInt32(putKey, (int) value) : Value.uInt64(putKey, value));
    }

    private void putUInt64(String key, long value) {
        this.stack.add(Value.uInt64(putKey(key), value));
    }

    static int widthUInBits(long len) {
        if (len <= ((long) FlexBuffers.Unsigned.byteToUnsignedInt((byte) -1))) {
            return 0;
        }
        if (len <= ((long) FlexBuffers.Unsigned.shortToUnsignedInt(-1))) {
            return 1;
        }
        return len <= FlexBuffers.Unsigned.intToUnsignedLong(-1) ? 2 : 3;
    }

    private void writeAny(Value val, int byteWidth) {
        switch (val.type) {
            case 0:
            case 1:
            case 2:
            case 26:
                writeInt(val.iValue, byteWidth);
                return;
            case 3:
                writeDouble(val.dValue, byteWidth);
                return;
            default:
                writeOffset(val.iValue, byteWidth);
                return;
        }
    }

    private Value writeBlob(int key, byte[] blob, int type, boolean trailing) {
        int widthUInBits = widthUInBits((long) blob.length);
        writeInt((long) blob.length, align(widthUInBits));
        int writePosition = this.bb.writePosition();
        this.bb.put(blob, 0, blob.length);
        if (trailing) {
            this.bb.put((byte) 0);
        }
        return Value.blob(key, writePosition, type, widthUInBits);
    }

    private void writeDouble(double val, int byteWidth) {
        if (byteWidth == 4) {
            this.bb.putFloat((float) val);
        } else if (byteWidth == 8) {
            this.bb.putDouble(val);
        }
    }

    private void writeInt(long value, int byteWidth) {
        switch (byteWidth) {
            case 1:
                this.bb.put((byte) ((int) value));
                return;
            case 2:
                this.bb.putShort((short) ((int) value));
                return;
            case 4:
                this.bb.putInt((int) value);
                return;
            case 8:
                this.bb.putLong(value);
                return;
            default:
                return;
        }
    }

    private void writeOffset(long val, int byteWidth) {
        int writePosition = (int) (((long) this.bb.writePosition()) - val);
        if (byteWidth == 8 || ((long) writePosition) < (1 << (byteWidth * 8))) {
            writeInt((long) writePosition, byteWidth);
            return;
        }
        throw new AssertionError();
    }

    private Value writeString(int key, String s) {
        return writeBlob(key, s.getBytes(StandardCharsets.UTF_8), 5, true);
    }

    public int endMap(String key, int start) {
        int putKey = putKey(key);
        ArrayList<Value> arrayList = this.stack;
        Collections.sort(arrayList.subList(start, arrayList.size()), this.keyComparator);
        Value createKeyVector = createKeyVector(start, this.stack.size() - start);
        Value createVector = createVector(putKey, start, this.stack.size() - start, false, false, createKeyVector);
        while (this.stack.size() > start) {
            ArrayList<Value> arrayList2 = this.stack;
            arrayList2.remove(arrayList2.size() - 1);
        }
        this.stack.add(createVector);
        return (int) createVector.iValue;
    }

    public int endVector(String key, int start, boolean typed, boolean fixed) {
        Value createVector = createVector(putKey(key), start, this.stack.size() - start, typed, fixed, (Value) null);
        while (this.stack.size() > start) {
            ArrayList<Value> arrayList = this.stack;
            arrayList.remove(arrayList.size() - 1);
        }
        this.stack.add(createVector);
        return (int) createVector.iValue;
    }

    public ByteBuffer finish() {
        if (this.stack.size() == 1) {
            int align = align(this.stack.get(0).elemWidth(this.bb.writePosition(), 0));
            writeAny(this.stack.get(0), align);
            this.bb.put(this.stack.get(0).storedPackedType());
            this.bb.put((byte) align);
            this.finished = true;
            return ByteBuffer.wrap(this.bb.data(), 0, this.bb.writePosition());
        }
        throw new AssertionError();
    }

    public ReadWriteBuf getBuffer() {
        if (this.finished) {
            return this.bb;
        }
        throw new AssertionError();
    }

    public int putBlob(String key, byte[] val) {
        Value writeBlob = writeBlob(putKey(key), val, 25, false);
        this.stack.add(writeBlob);
        return (int) writeBlob.iValue;
    }

    public int putBlob(byte[] value) {
        return putBlob((String) null, value);
    }

    public void putBoolean(String key, boolean val) {
        this.stack.add(Value.bool(putKey(key), val));
    }

    public void putBoolean(boolean val) {
        putBoolean((String) null, val);
    }

    public void putFloat(double value) {
        putFloat((String) null, value);
    }

    public void putFloat(float value) {
        putFloat((String) null, value);
    }

    public void putFloat(String key, double val) {
        this.stack.add(Value.float64(putKey(key), val));
    }

    public void putFloat(String key, float val) {
        this.stack.add(Value.float32(putKey(key), val));
    }

    public void putInt(int val) {
        putInt((String) null, val);
    }

    public void putInt(long value) {
        putInt((String) null, value);
    }

    public void putInt(String key, int val) {
        putInt(key, (long) val);
    }

    public void putInt(String key, long val) {
        int putKey = putKey(key);
        if (-128 <= val && val <= 127) {
            this.stack.add(Value.int8(putKey, (int) val));
        } else if (-32768 <= val && val <= 32767) {
            this.stack.add(Value.int16(putKey, (int) val));
        } else if (-2147483648L > val || val > 2147483647L) {
            this.stack.add(Value.int64(putKey, val));
        } else {
            this.stack.add(Value.int32(putKey, (int) val));
        }
    }

    public int putString(String value) {
        return putString((String) null, value);
    }

    public int putString(String key, String val) {
        int putKey = putKey(key);
        if ((this.flags & 2) != 0) {
            Integer num = this.stringPool.get(val);
            if (num == null) {
                Value writeString = writeString(putKey, val);
                this.stringPool.put(val, Integer.valueOf((int) writeString.iValue));
                this.stack.add(writeString);
                return (int) writeString.iValue;
            }
            this.stack.add(Value.blob(putKey, num.intValue(), 5, widthUInBits((long) val.length())));
            return num.intValue();
        }
        Value writeString2 = writeString(putKey, val);
        this.stack.add(writeString2);
        return (int) writeString2.iValue;
    }

    public void putUInt(int value) {
        putUInt((String) null, (long) value);
    }

    public void putUInt(long value) {
        putUInt((String) null, value);
    }

    public void putUInt64(BigInteger value) {
        putUInt64((String) null, value.longValue());
    }

    public int startMap() {
        return this.stack.size();
    }

    public int startVector() {
        return this.stack.size();
    }
}

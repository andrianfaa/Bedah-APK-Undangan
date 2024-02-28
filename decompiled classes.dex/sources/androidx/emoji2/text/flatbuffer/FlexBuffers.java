package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import kotlin.UByte;
import kotlin.text.Typography;
import okhttp3.HttpUrl;

public class FlexBuffers {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    /* access modifiers changed from: private */
    public static final ReadBuf EMPTY_BB = new ArrayReadWriteBuf(new byte[]{0}, 1);
    public static final int FBT_BLOB = 25;
    public static final int FBT_BOOL = 26;
    public static final int FBT_FLOAT = 3;
    public static final int FBT_INDIRECT_FLOAT = 8;
    public static final int FBT_INDIRECT_INT = 6;
    public static final int FBT_INDIRECT_UINT = 7;
    public static final int FBT_INT = 1;
    public static final int FBT_KEY = 4;
    public static final int FBT_MAP = 9;
    public static final int FBT_NULL = 0;
    public static final int FBT_STRING = 5;
    public static final int FBT_UINT = 2;
    public static final int FBT_VECTOR = 10;
    public static final int FBT_VECTOR_BOOL = 36;
    public static final int FBT_VECTOR_FLOAT = 13;
    public static final int FBT_VECTOR_FLOAT2 = 18;
    public static final int FBT_VECTOR_FLOAT3 = 21;
    public static final int FBT_VECTOR_FLOAT4 = 24;
    public static final int FBT_VECTOR_INT = 11;
    public static final int FBT_VECTOR_INT2 = 16;
    public static final int FBT_VECTOR_INT3 = 19;
    public static final int FBT_VECTOR_INT4 = 22;
    public static final int FBT_VECTOR_KEY = 14;
    public static final int FBT_VECTOR_STRING_DEPRECATED = 15;
    public static final int FBT_VECTOR_UINT = 12;
    public static final int FBT_VECTOR_UINT2 = 17;
    public static final int FBT_VECTOR_UINT3 = 20;
    public static final int FBT_VECTOR_UINT4 = 23;

    public static class Blob extends Sized {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        static final Blob EMPTY = new Blob(FlexBuffers.EMPTY_BB, 1, 1);

        static {
            Class<FlexBuffers> cls = FlexBuffers.class;
        }

        Blob(ReadBuf buff, int end, int byteWidth) {
            super(buff, end, byteWidth);
        }

        public static Blob empty() {
            return EMPTY;
        }

        public ByteBuffer data() {
            ByteBuffer wrap = ByteBuffer.wrap(this.bb.data());
            wrap.position(this.end);
            wrap.limit(this.end + size());
            return wrap.asReadOnlyBuffer().slice();
        }

        public byte get(int pos) {
            if (pos >= 0 && pos <= size()) {
                return this.bb.get(this.end + pos);
            }
            throw new AssertionError();
        }

        public byte[] getBytes() {
            int size = size();
            byte[] bArr = new byte[size];
            for (int i = 0; i < size; i++) {
                bArr[i] = this.bb.get(this.end + i);
            }
            return bArr;
        }

        public /* bridge */ /* synthetic */ int size() {
            return super.size();
        }

        public String toString() {
            return this.bb.getString(this.end, size());
        }

        public StringBuilder toString(StringBuilder sb) {
            sb.append(Typography.quote);
            sb.append(this.bb.getString(this.end, size()));
            return sb.append(Typography.quote);
        }
    }

    public static class FlexBufferException extends RuntimeException {
        FlexBufferException(String msg) {
            super(msg);
        }
    }

    public static class Key extends Object {
        /* access modifiers changed from: private */
        public static final Key EMPTY = new Key(FlexBuffers.EMPTY_BB, 0, 0);

        Key(ReadBuf buff, int end, int byteWidth) {
            super(buff, end, byteWidth);
        }

        public static Key empty() {
            return EMPTY;
        }

        /* access modifiers changed from: package-private */
        public int compareTo(byte[] other) {
            byte b;
            byte b2;
            int i = this.end;
            int i2 = 0;
            do {
                b = this.bb.get(i);
                b2 = other[i2];
                if (b == 0) {
                    return b - b2;
                }
                i++;
                i2++;
                if (i2 == other.length) {
                    return b - b2;
                }
            } while (b == b2);
            return b - b2;
        }

        public boolean equals(Object obj) {
            return (obj instanceof Key) && ((Key) obj).end == this.end && ((Key) obj).byteWidth == this.byteWidth;
        }

        public int hashCode() {
            return this.end ^ this.byteWidth;
        }

        public String toString() {
            int i = this.end;
            while (this.bb.get(i) != 0) {
                i++;
            }
            return this.bb.getString(this.end, i - this.end);
        }

        public StringBuilder toString(StringBuilder sb) {
            return sb.append(toString());
        }
    }

    public static class KeyVector {
        private final TypedVector vec;

        KeyVector(TypedVector vec2) {
            this.vec = vec2;
        }

        public Key get(int pos) {
            if (pos >= size()) {
                return Key.EMPTY;
            }
            return new Key(this.vec.bb, FlexBuffers.indirect(this.vec.bb, this.vec.end + (this.vec.byteWidth * pos), this.vec.byteWidth), 1);
        }

        public int size() {
            return this.vec.size();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            for (int i = 0; i < this.vec.size(); i++) {
                this.vec.get(i).toString(sb);
                if (i != this.vec.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.append("]").toString();
        }
    }

    public static class Map extends Vector {
        private static final Map EMPTY_MAP = new Map(FlexBuffers.EMPTY_BB, 1, 1);

        Map(ReadBuf bb, int end, int byteWidth) {
            super(bb, end, byteWidth);
        }

        private int binarySearch(KeyVector keys, byte[] searchedKey) {
            int i = 0;
            int size = keys.size() - 1;
            while (i <= size) {
                int i2 = (i + size) >>> 1;
                int compareTo = keys.get(i2).compareTo(searchedKey);
                if (compareTo < 0) {
                    i = i2 + 1;
                } else if (compareTo <= 0) {
                    return i2;
                } else {
                    size = i2 - 1;
                }
            }
            return -(i + 1);
        }

        public static Map empty() {
            return EMPTY_MAP;
        }

        public Reference get(String key) {
            return get(key.getBytes(StandardCharsets.UTF_8));
        }

        public Reference get(byte[] key) {
            KeyVector keys = keys();
            int size = keys.size();
            int binarySearch = binarySearch(keys, key);
            return (binarySearch < 0 || binarySearch >= size) ? Reference.NULL_REFERENCE : get(binarySearch);
        }

        public KeyVector keys() {
            int i = this.end - (this.byteWidth * 3);
            return new KeyVector(new TypedVector(this.bb, FlexBuffers.indirect(this.bb, i, this.byteWidth), FlexBuffers.readInt(this.bb, this.byteWidth + i, this.byteWidth), 4));
        }

        public StringBuilder toString(StringBuilder builder) {
            builder.append("{ ");
            KeyVector keys = keys();
            int size = size();
            Vector values = values();
            for (int i = 0; i < size; i++) {
                builder.append(Typography.quote).append(keys.get(i).toString()).append("\" : ");
                builder.append(values.get(i).toString());
                if (i != size - 1) {
                    builder.append(", ");
                }
            }
            builder.append(" }");
            return builder;
        }

        public Vector values() {
            return new Vector(this.bb, this.end, this.byteWidth);
        }
    }

    private static abstract class Object {
        ReadBuf bb;
        int byteWidth;
        int end;

        Object(ReadBuf buff, int end2, int byteWidth2) {
            this.bb = buff;
            this.end = end2;
            this.byteWidth = byteWidth2;
        }

        public String toString() {
            return toString(new StringBuilder(128)).toString();
        }

        public abstract StringBuilder toString(StringBuilder sb);
    }

    public static class Reference {
        /* access modifiers changed from: private */
        public static final Reference NULL_REFERENCE = new Reference(FlexBuffers.EMPTY_BB, 0, 1, 0);
        private ReadBuf bb;
        private int byteWidth;
        private int end;
        private int parentWidth;
        private int type;

        Reference(ReadBuf bb2, int end2, int parentWidth2, int packedType) {
            this(bb2, end2, parentWidth2, 1 << (packedType & 3), packedType >> 2);
        }

        Reference(ReadBuf bb2, int end2, int parentWidth2, int byteWidth2, int type2) {
            this.bb = bb2;
            this.end = end2;
            this.parentWidth = parentWidth2;
            this.byteWidth = byteWidth2;
            this.type = type2;
        }

        public Blob asBlob() {
            if (!isBlob() && !isString()) {
                return Blob.empty();
            }
            ReadBuf readBuf = this.bb;
            return new Blob(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
        }

        public boolean asBoolean() {
            return isBoolean() ? this.bb.get(this.end) != 0 : asUInt() != 0;
        }

        public double asFloat() {
            int i = this.type;
            if (i == 3) {
                return FlexBuffers.readDouble(this.bb, this.end, this.parentWidth);
            }
            switch (i) {
                case 0:
                    return 0.0d;
                case 1:
                    return (double) FlexBuffers.readInt(this.bb, this.end, this.parentWidth);
                case 2:
                case 26:
                    return (double) FlexBuffers.readUInt(this.bb, this.end, this.parentWidth);
                case 5:
                    return Double.parseDouble(asString());
                case 6:
                    ReadBuf readBuf = this.bb;
                    return (double) FlexBuffers.readInt(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
                case 7:
                    ReadBuf readBuf2 = this.bb;
                    return (double) FlexBuffers.readUInt(readBuf2, FlexBuffers.indirect(readBuf2, this.end, this.parentWidth), this.byteWidth);
                case 8:
                    ReadBuf readBuf3 = this.bb;
                    return FlexBuffers.readDouble(readBuf3, FlexBuffers.indirect(readBuf3, this.end, this.parentWidth), this.byteWidth);
                case 10:
                    return (double) asVector().size();
                default:
                    return 0.0d;
            }
        }

        public int asInt() {
            int i = this.type;
            if (i == 1) {
                return FlexBuffers.readInt(this.bb, this.end, this.parentWidth);
            }
            switch (i) {
                case 0:
                    return 0;
                case 2:
                    return (int) FlexBuffers.readUInt(this.bb, this.end, this.parentWidth);
                case 3:
                    return (int) FlexBuffers.readDouble(this.bb, this.end, this.parentWidth);
                case 5:
                    return Integer.parseInt(asString());
                case 6:
                    ReadBuf readBuf = this.bb;
                    return FlexBuffers.readInt(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
                case 7:
                    ReadBuf readBuf2 = this.bb;
                    return (int) FlexBuffers.readUInt(readBuf2, FlexBuffers.indirect(readBuf2, this.end, this.parentWidth), this.parentWidth);
                case 8:
                    ReadBuf readBuf3 = this.bb;
                    return (int) FlexBuffers.readDouble(readBuf3, FlexBuffers.indirect(readBuf3, this.end, this.parentWidth), this.byteWidth);
                case 10:
                    return asVector().size();
                case 26:
                    return FlexBuffers.readInt(this.bb, this.end, this.parentWidth);
                default:
                    return 0;
            }
        }

        public Key asKey() {
            if (!isKey()) {
                return Key.empty();
            }
            ReadBuf readBuf = this.bb;
            return new Key(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
        }

        public long asLong() {
            int i = this.type;
            if (i == 1) {
                return FlexBuffers.readLong(this.bb, this.end, this.parentWidth);
            }
            switch (i) {
                case 0:
                    return 0;
                case 2:
                    return FlexBuffers.readUInt(this.bb, this.end, this.parentWidth);
                case 3:
                    return (long) FlexBuffers.readDouble(this.bb, this.end, this.parentWidth);
                case 5:
                    try {
                        return Long.parseLong(asString());
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                case 6:
                    ReadBuf readBuf = this.bb;
                    return FlexBuffers.readLong(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
                case 7:
                    ReadBuf readBuf2 = this.bb;
                    return FlexBuffers.readUInt(readBuf2, FlexBuffers.indirect(readBuf2, this.end, this.parentWidth), this.parentWidth);
                case 8:
                    ReadBuf readBuf3 = this.bb;
                    return (long) FlexBuffers.readDouble(readBuf3, FlexBuffers.indirect(readBuf3, this.end, this.parentWidth), this.byteWidth);
                case 10:
                    return (long) asVector().size();
                case 26:
                    return (long) FlexBuffers.readInt(this.bb, this.end, this.parentWidth);
                default:
                    return 0;
            }
        }

        public Map asMap() {
            if (!isMap()) {
                return Map.empty();
            }
            ReadBuf readBuf = this.bb;
            return new Map(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
        }

        public String asString() {
            if (isString()) {
                int access$200 = FlexBuffers.indirect(this.bb, this.end, this.parentWidth);
                ReadBuf readBuf = this.bb;
                int i = this.byteWidth;
                return this.bb.getString(access$200, (int) FlexBuffers.readUInt(readBuf, access$200 - i, i));
            } else if (!isKey()) {
                return HttpUrl.FRAGMENT_ENCODE_SET;
            } else {
                int access$2002 = FlexBuffers.indirect(this.bb, this.end, this.byteWidth);
                int i2 = access$2002;
                while (this.bb.get(i2) != 0) {
                    i2++;
                }
                return this.bb.getString(access$2002, i2 - access$2002);
            }
        }

        public long asUInt() {
            int i = this.type;
            if (i == 2) {
                return FlexBuffers.readUInt(this.bb, this.end, this.parentWidth);
            }
            switch (i) {
                case 0:
                    return 0;
                case 1:
                    return FlexBuffers.readLong(this.bb, this.end, this.parentWidth);
                case 3:
                    return (long) FlexBuffers.readDouble(this.bb, this.end, this.parentWidth);
                case 5:
                    return Long.parseLong(asString());
                case 6:
                    ReadBuf readBuf = this.bb;
                    return FlexBuffers.readLong(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
                case 7:
                    ReadBuf readBuf2 = this.bb;
                    return FlexBuffers.readUInt(readBuf2, FlexBuffers.indirect(readBuf2, this.end, this.parentWidth), this.byteWidth);
                case 8:
                    ReadBuf readBuf3 = this.bb;
                    return (long) FlexBuffers.readDouble(readBuf3, FlexBuffers.indirect(readBuf3, this.end, this.parentWidth), this.parentWidth);
                case 10:
                    return (long) asVector().size();
                case 26:
                    return (long) FlexBuffers.readInt(this.bb, this.end, this.parentWidth);
                default:
                    return 0;
            }
        }

        public Vector asVector() {
            if (isVector()) {
                ReadBuf readBuf = this.bb;
                return new Vector(readBuf, FlexBuffers.indirect(readBuf, this.end, this.parentWidth), this.byteWidth);
            }
            int i = this.type;
            if (i == 15) {
                ReadBuf readBuf2 = this.bb;
                return new TypedVector(readBuf2, FlexBuffers.indirect(readBuf2, this.end, this.parentWidth), this.byteWidth, 4);
            } else if (!FlexBuffers.isTypedVector(i)) {
                return Vector.empty();
            } else {
                ReadBuf readBuf3 = this.bb;
                return new TypedVector(readBuf3, FlexBuffers.indirect(readBuf3, this.end, this.parentWidth), this.byteWidth, FlexBuffers.toTypedVectorElementType(this.type));
            }
        }

        public int getType() {
            return this.type;
        }

        public boolean isBlob() {
            return this.type == 25;
        }

        public boolean isBoolean() {
            return this.type == 26;
        }

        public boolean isFloat() {
            int i = this.type;
            return i == 3 || i == 8;
        }

        public boolean isInt() {
            int i = this.type;
            return i == 1 || i == 6;
        }

        public boolean isIntOrUInt() {
            return isInt() || isUInt();
        }

        public boolean isKey() {
            return this.type == 4;
        }

        public boolean isMap() {
            return this.type == 9;
        }

        public boolean isNull() {
            return this.type == 0;
        }

        public boolean isNumeric() {
            return isIntOrUInt() || isFloat();
        }

        public boolean isString() {
            return this.type == 5;
        }

        public boolean isTypedVector() {
            return FlexBuffers.isTypedVector(this.type);
        }

        public boolean isUInt() {
            int i = this.type;
            return i == 2 || i == 7;
        }

        public boolean isVector() {
            int i = this.type;
            return i == 10 || i == 9;
        }

        public String toString() {
            return toString(new StringBuilder(128)).toString();
        }

        /* access modifiers changed from: package-private */
        public StringBuilder toString(StringBuilder sb) {
            switch (this.type) {
                case 0:
                    return sb.append("null");
                case 1:
                case 6:
                    return sb.append(asLong());
                case 2:
                case 7:
                    return sb.append(asUInt());
                case 3:
                case 8:
                    return sb.append(asFloat());
                case 4:
                    return asKey().toString(sb.append(Typography.quote)).append(Typography.quote);
                case 5:
                    return sb.append(Typography.quote).append(asString()).append(Typography.quote);
                case 9:
                    return asMap().toString(sb);
                case 10:
                    return asVector().toString(sb);
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 36:
                    return sb.append(asVector());
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                    throw new FlexBufferException("not_implemented:" + this.type);
                case 25:
                    return asBlob().toString(sb);
                case 26:
                    return sb.append(asBoolean());
                default:
                    return sb;
            }
        }
    }

    private static abstract class Sized extends Object {
        protected final int size;

        Sized(ReadBuf buff, int end, int byteWidth) {
            super(buff, end, byteWidth);
            this.size = FlexBuffers.readInt(this.bb, end - byteWidth, byteWidth);
        }

        public int size() {
            return this.size;
        }
    }

    public static class TypedVector extends Vector {
        private static final TypedVector EMPTY_VECTOR = new TypedVector(FlexBuffers.EMPTY_BB, 1, 1, 1);
        private final int elemType;

        TypedVector(ReadBuf bb, int end, int byteWidth, int elemType2) {
            super(bb, end, byteWidth);
            this.elemType = elemType2;
        }

        public static TypedVector empty() {
            return EMPTY_VECTOR;
        }

        public Reference get(int pos) {
            if (pos >= size()) {
                return Reference.NULL_REFERENCE;
            }
            return new Reference(this.bb, this.end + (this.byteWidth * pos), this.byteWidth, 1, this.elemType);
        }

        public int getElemType() {
            return this.elemType;
        }

        public boolean isEmptyVector() {
            return this == EMPTY_VECTOR;
        }
    }

    static class Unsigned {
        Unsigned() {
        }

        static int byteToUnsignedInt(byte x) {
            return x & UByte.MAX_VALUE;
        }

        static long intToUnsignedLong(int x) {
            return ((long) x) & 4294967295L;
        }

        static int shortToUnsignedInt(short x) {
            return 65535 & x;
        }
    }

    public static class Vector extends Sized {
        private static final Vector EMPTY_VECTOR = new Vector(FlexBuffers.EMPTY_BB, 1, 1);

        Vector(ReadBuf bb, int end, int byteWidth) {
            super(bb, end, byteWidth);
        }

        public static Vector empty() {
            return EMPTY_VECTOR;
        }

        public Reference get(int index) {
            long size = (long) size();
            if (((long) index) >= size) {
                return Reference.NULL_REFERENCE;
            }
            int byteToUnsignedInt = Unsigned.byteToUnsignedInt(this.bb.get((int) (((long) this.end) + (((long) this.byteWidth) * size) + ((long) index))));
            return new Reference(this.bb, this.end + (this.byteWidth * index), this.byteWidth, byteToUnsignedInt);
        }

        public boolean isEmpty() {
            return this == EMPTY_VECTOR;
        }

        public /* bridge */ /* synthetic */ int size() {
            return super.size();
        }

        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        public StringBuilder toString(StringBuilder sb) {
            sb.append("[ ");
            int size = size();
            for (int i = 0; i < size; i++) {
                get(i).toString(sb);
                if (i != size - 1) {
                    sb.append(", ");
                }
            }
            sb.append(" ]");
            return sb;
        }
    }

    public static Reference getRoot(ReadBuf buffer) {
        int limit = buffer.limit() - 1;
        byte b = buffer.get(limit);
        int i = limit - 1;
        return new Reference(buffer, i - b, b, Unsigned.byteToUnsignedInt(buffer.get(i)));
    }

    @Deprecated
    public static Reference getRoot(ByteBuffer buffer) {
        return getRoot(buffer.hasArray() ? new ArrayReadWriteBuf(buffer.array(), buffer.limit()) : new ByteBufferReadWriteBuf(buffer));
    }

    /* access modifiers changed from: private */
    public static int indirect(ReadBuf bb, int offset, int byteWidth) {
        return (int) (((long) offset) - readUInt(bb, offset, byteWidth));
    }

    static boolean isTypeInline(int type) {
        return type <= 3 || type == 26;
    }

    static boolean isTypedVector(int type) {
        return (type >= 11 && type <= 15) || type == 36;
    }

    static boolean isTypedVectorElementType(int type) {
        return (type >= 1 && type <= 4) || type == 26;
    }

    /* access modifiers changed from: private */
    public static double readDouble(ReadBuf buff, int end, int byteWidth) {
        switch (byteWidth) {
            case 4:
                return (double) buff.getFloat(end);
            case 8:
                return buff.getDouble(end);
            default:
                return -1.0d;
        }
    }

    /* access modifiers changed from: private */
    public static int readInt(ReadBuf buff, int end, int byteWidth) {
        return (int) readLong(buff, end, byteWidth);
    }

    /* access modifiers changed from: private */
    public static long readLong(ReadBuf buff, int end, int byteWidth) {
        switch (byteWidth) {
            case 1:
                return (long) buff.get(end);
            case 2:
                return (long) buff.getShort(end);
            case 4:
                return (long) buff.getInt(end);
            case 8:
                return buff.getLong(end);
            default:
                return -1;
        }
    }

    /* access modifiers changed from: private */
    public static long readUInt(ReadBuf buff, int end, int byteWidth) {
        switch (byteWidth) {
            case 1:
                return (long) Unsigned.byteToUnsignedInt(buff.get(end));
            case 2:
                return (long) Unsigned.shortToUnsignedInt(buff.getShort(end));
            case 4:
                return Unsigned.intToUnsignedLong(buff.getInt(end));
            case 8:
                return buff.getLong(end);
            default:
                return -1;
        }
    }

    static int toTypedVector(int type, int fixedLength) {
        if (isTypedVectorElementType(type)) {
            switch (fixedLength) {
                case 0:
                    return (type - 1) + 11;
                case 2:
                    return (type - 1) + 16;
                case 3:
                    return (type - 1) + 19;
                case 4:
                    return (type - 1) + 22;
                default:
                    throw new AssertionError();
            }
        } else {
            throw new AssertionError();
        }
    }

    static int toTypedVectorElementType(int original_type) {
        return (original_type - 11) + 1;
    }
}

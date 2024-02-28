package androidx.emoji2.text.flatbuffer;

import java.util.Arrays;
import kotlin.UByte;
import mt.Log1F380D;

/* compiled from: 006F */
public class ArrayReadWriteBuf implements ReadWriteBuf {
    private byte[] buffer;
    private int writePos;

    public ArrayReadWriteBuf() {
        this(10);
    }

    public ArrayReadWriteBuf(int initialCapacity) {
        this(new byte[initialCapacity]);
    }

    public ArrayReadWriteBuf(byte[] buffer2) {
        this.buffer = buffer2;
        this.writePos = 0;
    }

    public ArrayReadWriteBuf(byte[] buffer2, int startPos) {
        this.buffer = buffer2;
        this.writePos = startPos;
    }

    public byte[] data() {
        return this.buffer;
    }

    public byte get(int index) {
        return this.buffer[index];
    }

    public boolean getBoolean(int index) {
        return this.buffer[index] != 0;
    }

    public double getDouble(int index) {
        return Double.longBitsToDouble(getLong(index));
    }

    public float getFloat(int index) {
        return Float.intBitsToFloat(getInt(index));
    }

    public int getInt(int index) {
        byte[] bArr = this.buffer;
        return (bArr[index] & UByte.MAX_VALUE) | (bArr[index + 3] << 24) | ((bArr[index + 2] & UByte.MAX_VALUE) << 16) | ((bArr[index + 1] & UByte.MAX_VALUE) << 8);
    }

    public long getLong(int index) {
        byte[] bArr = this.buffer;
        int i = index + 1;
        int index2 = i + 1;
        int i2 = index2 + 1;
        int index3 = i2 + 1;
        int i3 = index3 + 1;
        int index4 = i3 + 1;
        return (((long) bArr[index]) & 255) | ((((long) bArr[i]) & 255) << 8) | ((((long) bArr[index2]) & 255) << 16) | ((((long) bArr[i2]) & 255) << 24) | ((((long) bArr[index3]) & 255) << 32) | ((((long) bArr[i3]) & 255) << 40) | ((255 & ((long) bArr[index4])) << 48) | (((long) bArr[index4 + 1]) << 56);
    }

    public short getShort(int index) {
        byte[] bArr = this.buffer;
        return (short) ((bArr[index] & UByte.MAX_VALUE) | (bArr[index + 1] << 8));
    }

    public String getString(int start, int size) {
        String decodeUtf8Array = Utf8Safe.decodeUtf8Array(this.buffer, start, size);
        Log1F380D.a((Object) decodeUtf8Array);
        return decodeUtf8Array;
    }

    public int limit() {
        return this.writePos;
    }

    public void put(byte value) {
        set(this.writePos, value);
        this.writePos++;
    }

    public void put(byte[] value, int start, int length) {
        set(this.writePos, value, start, length);
        this.writePos += length;
    }

    public void putBoolean(boolean value) {
        setBoolean(this.writePos, value);
        this.writePos++;
    }

    public void putDouble(double value) {
        setDouble(this.writePos, value);
        this.writePos += 8;
    }

    public void putFloat(float value) {
        setFloat(this.writePos, value);
        this.writePos += 4;
    }

    public void putInt(int value) {
        setInt(this.writePos, value);
        this.writePos += 4;
    }

    public void putLong(long value) {
        setLong(this.writePos, value);
        this.writePos += 8;
    }

    public void putShort(short value) {
        setShort(this.writePos, value);
        this.writePos += 2;
    }

    public boolean requestCapacity(int capacity) {
        byte[] bArr = this.buffer;
        if (bArr.length > capacity) {
            return true;
        }
        int length = bArr.length;
        this.buffer = Arrays.copyOf(bArr, (length >> 1) + length);
        return true;
    }

    public void set(int index, byte value) {
        requestCapacity(index + 1);
        this.buffer[index] = value;
    }

    public void set(int index, byte[] toCopy, int start, int length) {
        requestCapacity((length - start) + index);
        System.arraycopy(toCopy, start, this.buffer, index, length);
    }

    public void setBoolean(int index, boolean value) {
        set(index, value);
    }

    public void setDouble(int index, double value) {
        requestCapacity(index + 8);
        long doubleToRawLongBits = Double.doubleToRawLongBits(value);
        int i = (int) doubleToRawLongBits;
        byte[] bArr = this.buffer;
        int i2 = index + 1;
        bArr[index] = (byte) (i & 255);
        int index2 = i2 + 1;
        bArr[i2] = (byte) ((i >> 8) & 255);
        int i3 = index2 + 1;
        bArr[index2] = (byte) ((i >> 16) & 255);
        int index3 = i3 + 1;
        bArr[i3] = (byte) ((i >> 24) & 255);
        int i4 = (int) (doubleToRawLongBits >> 32);
        int i5 = index3 + 1;
        bArr[index3] = (byte) (i4 & 255);
        int index4 = i5 + 1;
        bArr[i5] = (byte) ((i4 >> 8) & 255);
        bArr[index4] = (byte) ((i4 >> 16) & 255);
        bArr[index4 + 1] = (byte) ((i4 >> 24) & 255);
    }

    public void setFloat(int index, float value) {
        requestCapacity(index + 4);
        int floatToRawIntBits = Float.floatToRawIntBits(value);
        byte[] bArr = this.buffer;
        int i = index + 1;
        bArr[index] = (byte) (floatToRawIntBits & 255);
        int index2 = i + 1;
        bArr[i] = (byte) ((floatToRawIntBits >> 8) & 255);
        bArr[index2] = (byte) ((floatToRawIntBits >> 16) & 255);
        bArr[index2 + 1] = (byte) ((floatToRawIntBits >> 24) & 255);
    }

    public void setInt(int index, int value) {
        requestCapacity(index + 4);
        byte[] bArr = this.buffer;
        int i = index + 1;
        bArr[index] = (byte) (value & 255);
        int index2 = i + 1;
        bArr[i] = (byte) ((value >> 8) & 255);
        bArr[index2] = (byte) ((value >> 16) & 255);
        bArr[index2 + 1] = (byte) ((value >> 24) & 255);
    }

    public void setLong(int index, long value) {
        requestCapacity(index + 8);
        int i = (int) value;
        byte[] bArr = this.buffer;
        int i2 = index + 1;
        bArr[index] = (byte) (i & 255);
        int index2 = i2 + 1;
        bArr[i2] = (byte) ((i >> 8) & 255);
        int i3 = index2 + 1;
        bArr[index2] = (byte) ((i >> 16) & 255);
        int index3 = i3 + 1;
        bArr[i3] = (byte) ((i >> 24) & 255);
        int i4 = (int) (value >> 32);
        int i5 = index3 + 1;
        bArr[index3] = (byte) (i4 & 255);
        int index4 = i5 + 1;
        bArr[i5] = (byte) ((i4 >> 8) & 255);
        bArr[index4] = (byte) ((i4 >> 16) & 255);
        bArr[index4 + 1] = (byte) ((i4 >> 24) & 255);
    }

    public void setShort(int index, short value) {
        requestCapacity(index + 2);
        byte[] bArr = this.buffer;
        bArr[index] = (byte) (value & 255);
        bArr[index + 1] = (byte) ((value >> 8) & 255);
    }

    public int writePosition() {
        return this.writePos;
    }
}

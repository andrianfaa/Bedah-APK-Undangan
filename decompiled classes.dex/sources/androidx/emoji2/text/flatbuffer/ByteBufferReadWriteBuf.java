package androidx.emoji2.text.flatbuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import mt.Log1F380D;

/* compiled from: 0070 */
public class ByteBufferReadWriteBuf implements ReadWriteBuf {
    private final ByteBuffer buffer;

    public ByteBufferReadWriteBuf(ByteBuffer bb) {
        this.buffer = bb;
        bb.order(ByteOrder.LITTLE_ENDIAN);
    }

    public byte[] data() {
        return this.buffer.array();
    }

    public byte get(int index) {
        return this.buffer.get(index);
    }

    public boolean getBoolean(int index) {
        return get(index) != 0;
    }

    public double getDouble(int index) {
        return this.buffer.getDouble(index);
    }

    public float getFloat(int index) {
        return this.buffer.getFloat(index);
    }

    public int getInt(int index) {
        return this.buffer.getInt(index);
    }

    public long getLong(int index) {
        return this.buffer.getLong(index);
    }

    public short getShort(int index) {
        return this.buffer.getShort(index);
    }

    public String getString(int start, int size) {
        String decodeUtf8Buffer = Utf8Safe.decodeUtf8Buffer(this.buffer, start, size);
        Log1F380D.a((Object) decodeUtf8Buffer);
        return decodeUtf8Buffer;
    }

    public int limit() {
        return this.buffer.limit();
    }

    public void put(byte value) {
        this.buffer.put(value);
    }

    public void put(byte[] value, int start, int length) {
        this.buffer.put(value, start, length);
    }

    public void putBoolean(boolean value) {
        this.buffer.put(value);
    }

    public void putDouble(double value) {
        this.buffer.putDouble(value);
    }

    public void putFloat(float value) {
        this.buffer.putFloat(value);
    }

    public void putInt(int value) {
        this.buffer.putInt(value);
    }

    public void putLong(long value) {
        this.buffer.putLong(value);
    }

    public void putShort(short value) {
        this.buffer.putShort(value);
    }

    public boolean requestCapacity(int capacity) {
        return capacity <= this.buffer.limit();
    }

    public void set(int index, byte value) {
        requestCapacity(index + 1);
        this.buffer.put(index, value);
    }

    public void set(int index, byte[] value, int start, int length) {
        requestCapacity((length - start) + index);
        int position = this.buffer.position();
        this.buffer.position(index);
        this.buffer.put(value, start, length);
        this.buffer.position(position);
    }

    public void setBoolean(int index, boolean value) {
        set(index, value);
    }

    public void setDouble(int index, double value) {
        requestCapacity(index + 8);
        this.buffer.putDouble(index, value);
    }

    public void setFloat(int index, float value) {
        requestCapacity(index + 4);
        this.buffer.putFloat(index, value);
    }

    public void setInt(int index, int value) {
        requestCapacity(index + 4);
        this.buffer.putInt(index, value);
    }

    public void setLong(int index, long value) {
        requestCapacity(index + 8);
        this.buffer.putLong(index, value);
    }

    public void setShort(int index, short value) {
        requestCapacity(index + 2);
        this.buffer.putShort(index, value);
    }

    public int writePosition() {
        return this.buffer.position();
    }
}

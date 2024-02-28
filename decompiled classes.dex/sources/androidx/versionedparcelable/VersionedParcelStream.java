package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcelable;
import androidx.collection.ArrayMap;
import androidx.versionedparcelable.VersionedParcel;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Set;
import mt.Log1F380D;

/* compiled from: 009C */
class VersionedParcelStream extends VersionedParcel {
    private static final int TYPE_BOOLEAN = 5;
    private static final int TYPE_BOOLEAN_ARRAY = 6;
    private static final int TYPE_DOUBLE = 7;
    private static final int TYPE_DOUBLE_ARRAY = 8;
    private static final int TYPE_FLOAT = 13;
    private static final int TYPE_FLOAT_ARRAY = 14;
    private static final int TYPE_INT = 9;
    private static final int TYPE_INT_ARRAY = 10;
    private static final int TYPE_LONG = 11;
    private static final int TYPE_LONG_ARRAY = 12;
    private static final int TYPE_NULL = 0;
    private static final int TYPE_STRING = 3;
    private static final int TYPE_STRING_ARRAY = 4;
    private static final int TYPE_SUB_BUNDLE = 1;
    private static final int TYPE_SUB_PERSISTABLE_BUNDLE = 2;
    private static final Charset UTF_16 = Charset.forName("UTF-16");
    int mCount;
    private DataInputStream mCurrentInput;
    private DataOutputStream mCurrentOutput;
    private FieldBuffer mFieldBuffer;
    private int mFieldId;
    int mFieldSize;
    private boolean mIgnoreParcelables;
    private final DataInputStream mMasterInput;
    private final DataOutputStream mMasterOutput;

    private static class FieldBuffer {
        final DataOutputStream mDataStream;
        private final int mFieldId;
        final ByteArrayOutputStream mOutput;
        private final DataOutputStream mTarget;

        FieldBuffer(int fieldId, DataOutputStream target) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            this.mOutput = byteArrayOutputStream;
            this.mDataStream = new DataOutputStream(byteArrayOutputStream);
            this.mFieldId = fieldId;
            this.mTarget = target;
        }

        /* access modifiers changed from: package-private */
        public void flushField() throws IOException {
            this.mDataStream.flush();
            int size = this.mOutput.size();
            this.mTarget.writeInt((this.mFieldId << 16) | (size >= 65535 ? 65535 : size));
            if (size >= 65535) {
                this.mTarget.writeInt(size);
            }
            this.mOutput.writeTo(this.mTarget);
        }
    }

    public VersionedParcelStream(InputStream input, OutputStream output) {
        this(input, output, new ArrayMap(), new ArrayMap(), new ArrayMap());
    }

    private VersionedParcelStream(InputStream input, OutputStream output, ArrayMap<String, Method> arrayMap, ArrayMap<String, Method> arrayMap2, ArrayMap<String, Class> arrayMap3) {
        super(arrayMap, arrayMap2, arrayMap3);
        this.mCount = 0;
        this.mFieldId = -1;
        this.mFieldSize = -1;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = input != null ? new DataInputStream(new FilterInputStream(input) {
            public int read() throws IOException {
                if (VersionedParcelStream.this.mFieldSize == -1 || VersionedParcelStream.this.mCount < VersionedParcelStream.this.mFieldSize) {
                    int read = super.read();
                    VersionedParcelStream.this.mCount++;
                    return read;
                }
                throw new IOException();
            }

            public int read(byte[] b, int off, int len) throws IOException {
                if (VersionedParcelStream.this.mFieldSize == -1 || VersionedParcelStream.this.mCount < VersionedParcelStream.this.mFieldSize) {
                    int read = super.read(b, off, len);
                    if (read > 0) {
                        VersionedParcelStream.this.mCount += read;
                    }
                    return read;
                }
                throw new IOException();
            }

            public long skip(long n) throws IOException {
                if (VersionedParcelStream.this.mFieldSize == -1 || VersionedParcelStream.this.mCount < VersionedParcelStream.this.mFieldSize) {
                    long skip = super.skip(n);
                    if (skip > 0) {
                        VersionedParcelStream.this.mCount += (int) skip;
                    }
                    return skip;
                }
                throw new IOException();
            }
        }) : null;
        this.mMasterInput = dataInputStream;
        dataOutputStream = output != null ? new DataOutputStream(output) : dataOutputStream;
        this.mMasterOutput = dataOutputStream;
        this.mCurrentInput = dataInputStream;
        this.mCurrentOutput = dataOutputStream;
    }

    private void readObject(int type, String key, Bundle b) {
        switch (type) {
            case 0:
                b.putParcelable(key, (Parcelable) null);
                return;
            case 1:
                b.putBundle(key, readBundle());
                return;
            case 2:
                b.putBundle(key, readBundle());
                return;
            case 3:
                b.putString(key, readString());
                return;
            case 4:
                b.putStringArray(key, (String[]) readArray(new String[0]));
                return;
            case 5:
                b.putBoolean(key, readBoolean());
                return;
            case 6:
                b.putBooleanArray(key, readBooleanArray());
                return;
            case 7:
                b.putDouble(key, readDouble());
                return;
            case 8:
                b.putDoubleArray(key, readDoubleArray());
                return;
            case 9:
                b.putInt(key, readInt());
                return;
            case 10:
                b.putIntArray(key, readIntArray());
                return;
            case 11:
                b.putLong(key, readLong());
                return;
            case 12:
                b.putLongArray(key, readLongArray());
                return;
            case 13:
                b.putFloat(key, readFloat());
                return;
            case 14:
                b.putFloatArray(key, readFloatArray());
                return;
            default:
                throw new RuntimeException("Unknown type " + type);
        }
    }

    private void writeObject(Object o) {
        if (o == null) {
            writeInt(0);
        } else if (o instanceof Bundle) {
            writeInt(1);
            writeBundle((Bundle) o);
        } else if (o instanceof String) {
            writeInt(3);
            writeString((String) o);
        } else if (o instanceof String[]) {
            writeInt(4);
            writeArray((String[]) o);
        } else if (o instanceof Boolean) {
            writeInt(5);
            writeBoolean(((Boolean) o).booleanValue());
        } else if (o instanceof boolean[]) {
            writeInt(6);
            writeBooleanArray((boolean[]) o);
        } else if (o instanceof Double) {
            writeInt(7);
            writeDouble(((Double) o).doubleValue());
        } else if (o instanceof double[]) {
            writeInt(8);
            writeDoubleArray((double[]) o);
        } else if (o instanceof Integer) {
            writeInt(9);
            writeInt(((Integer) o).intValue());
        } else if (o instanceof int[]) {
            writeInt(10);
            writeIntArray((int[]) o);
        } else if (o instanceof Long) {
            writeInt(11);
            writeLong(((Long) o).longValue());
        } else if (o instanceof long[]) {
            writeInt(12);
            writeLongArray((long[]) o);
        } else if (o instanceof Float) {
            writeInt(13);
            writeFloat(((Float) o).floatValue());
        } else if (o instanceof float[]) {
            writeInt(14);
            writeFloatArray((float[]) o);
        } else {
            throw new IllegalArgumentException("Unsupported type " + o.getClass());
        }
    }

    public void closeField() {
        FieldBuffer fieldBuffer = this.mFieldBuffer;
        if (fieldBuffer != null) {
            try {
                if (fieldBuffer.mOutput.size() != 0) {
                    this.mFieldBuffer.flushField();
                }
                this.mFieldBuffer = null;
            } catch (IOException e) {
                throw new VersionedParcel.ParcelException(e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public VersionedParcel createSubParcel() {
        return new VersionedParcelStream(this.mCurrentInput, this.mCurrentOutput, this.mReadCache, this.mWriteCache, this.mParcelizerCache);
    }

    public boolean isStream() {
        return true;
    }

    public boolean readBoolean() {
        try {
            return this.mCurrentInput.readBoolean();
        } catch (IOException e) {
            throw new VersionedParcel.ParcelException(e);
        }
    }

    public Bundle readBundle() {
        int readInt = readInt();
        if (readInt < 0) {
            return null;
        }
        Bundle bundle = new Bundle();
        for (int i = 0; i < readInt; i++) {
            readObject(readInt(), readString(), bundle);
        }
        return bundle;
    }

    public byte[] readByteArray() {
        try {
            int readInt = this.mCurrentInput.readInt();
            if (readInt <= 0) {
                return null;
            }
            byte[] bArr = new byte[readInt];
            this.mCurrentInput.readFully(bArr);
            return bArr;
        } catch (IOException e) {
            throw new VersionedParcel.ParcelException(e);
        }
    }

    /* access modifiers changed from: protected */
    public CharSequence readCharSequence() {
        return null;
    }

    public double readDouble() {
        try {
            return this.mCurrentInput.readDouble();
        } catch (IOException e) {
            throw new VersionedParcel.ParcelException(e);
        }
    }

    public boolean readField(int fieldId) {
        while (true) {
            try {
                int i = this.mFieldId;
                if (i == fieldId) {
                    return true;
                }
                String valueOf = String.valueOf(i);
                Log1F380D.a((Object) valueOf);
                if (valueOf.compareTo(String.valueOf(fieldId)) > 0) {
                    return false;
                }
                int i2 = this.mCount;
                int i3 = this.mFieldSize;
                if (i2 < i3) {
                    this.mMasterInput.skip((long) (i3 - i2));
                }
                this.mFieldSize = -1;
                int readInt = this.mMasterInput.readInt();
                this.mCount = 0;
                int i4 = readInt & 65535;
                if (i4 == 65535) {
                    i4 = this.mMasterInput.readInt();
                }
                this.mFieldId = 65535 & (readInt >> 16);
                this.mFieldSize = i4;
            } catch (IOException e) {
                return false;
            }
        }
    }

    public float readFloat() {
        try {
            return this.mCurrentInput.readFloat();
        } catch (IOException e) {
            throw new VersionedParcel.ParcelException(e);
        }
    }

    public int readInt() {
        try {
            return this.mCurrentInput.readInt();
        } catch (IOException e) {
            throw new VersionedParcel.ParcelException(e);
        }
    }

    public long readLong() {
        try {
            return this.mCurrentInput.readLong();
        } catch (IOException e) {
            throw new VersionedParcel.ParcelException(e);
        }
    }

    public <T extends Parcelable> T readParcelable() {
        return null;
    }

    public IBinder readStrongBinder() {
        return null;
    }

    public void setOutputField(int fieldId) {
        closeField();
        FieldBuffer fieldBuffer = new FieldBuffer(fieldId, this.mMasterOutput);
        this.mFieldBuffer = fieldBuffer;
        this.mCurrentOutput = fieldBuffer.mDataStream;
    }

    public void setSerializationFlags(boolean allowSerialization, boolean ignoreParcelables) {
        if (allowSerialization) {
            this.mIgnoreParcelables = ignoreParcelables;
            return;
        }
        throw new RuntimeException("Serialization of this object is not allowed");
    }

    public void writeBoolean(boolean val) {
        try {
            this.mCurrentOutput.writeBoolean(val);
        } catch (IOException e) {
            throw new VersionedParcel.ParcelException(e);
        }
    }

    public void writeBundle(Bundle val) {
        if (val != null) {
            try {
                Set<String> keySet = val.keySet();
                this.mCurrentOutput.writeInt(keySet.size());
                for (String str : keySet) {
                    writeString(str);
                    writeObject(val.get(str));
                }
            } catch (IOException e) {
                throw new VersionedParcel.ParcelException(e);
            }
        } else {
            this.mCurrentOutput.writeInt(-1);
        }
    }

    public void writeByteArray(byte[] b) {
        if (b != null) {
            try {
                this.mCurrentOutput.writeInt(b.length);
                this.mCurrentOutput.write(b);
            } catch (IOException e) {
                throw new VersionedParcel.ParcelException(e);
            }
        } else {
            this.mCurrentOutput.writeInt(-1);
        }
    }

    public void writeByteArray(byte[] b, int offset, int len) {
        if (b != null) {
            try {
                this.mCurrentOutput.writeInt(len);
                this.mCurrentOutput.write(b, offset, len);
            } catch (IOException e) {
                throw new VersionedParcel.ParcelException(e);
            }
        } else {
            this.mCurrentOutput.writeInt(-1);
        }
    }

    /* access modifiers changed from: protected */
    public void writeCharSequence(CharSequence charSequence) {
        if (!this.mIgnoreParcelables) {
            throw new RuntimeException("CharSequence cannot be written to an OutputStream");
        }
    }

    public void writeDouble(double val) {
        try {
            this.mCurrentOutput.writeDouble(val);
        } catch (IOException e) {
            throw new VersionedParcel.ParcelException(e);
        }
    }

    public void writeFloat(float val) {
        try {
            this.mCurrentOutput.writeFloat(val);
        } catch (IOException e) {
            throw new VersionedParcel.ParcelException(e);
        }
    }

    public void writeInt(int val) {
        try {
            this.mCurrentOutput.writeInt(val);
        } catch (IOException e) {
            throw new VersionedParcel.ParcelException(e);
        }
    }

    public void writeLong(long val) {
        try {
            this.mCurrentOutput.writeLong(val);
        } catch (IOException e) {
            throw new VersionedParcel.ParcelException(e);
        }
    }

    public void writeParcelable(Parcelable p) {
        if (!this.mIgnoreParcelables) {
            throw new RuntimeException("Parcelables cannot be written to an OutputStream");
        }
    }

    public void writeString(String val) {
        if (val != null) {
            try {
                byte[] bytes = val.getBytes(UTF_16);
                this.mCurrentOutput.writeInt(bytes.length);
                this.mCurrentOutput.write(bytes);
            } catch (IOException e) {
                throw new VersionedParcel.ParcelException(e);
            }
        } else {
            this.mCurrentOutput.writeInt(-1);
        }
    }

    public void writeStrongBinder(IBinder val) {
        if (!this.mIgnoreParcelables) {
            throw new RuntimeException("Binders cannot be written to an OutputStream");
        }
    }

    public void writeStrongInterface(IInterface val) {
        if (!this.mIgnoreParcelables) {
            throw new RuntimeException("Binders cannot be written to an OutputStream");
        }
    }

    public String readString() {
        try {
            int readInt = this.mCurrentInput.readInt();
            if (readInt <= 0) {
                return null;
            }
            byte[] bArr = new byte[readInt];
            this.mCurrentInput.readFully(bArr);
            String str = new String(bArr, UTF_16);
            Log1F380D.a((Object) str);
            return str;
        } catch (IOException e) {
            throw new VersionedParcel.ParcelException(e);
        }
    }
}

package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.SparseIntArray;
import androidx.collection.ArrayMap;
import java.lang.reflect.Method;
import mt.Log1F380D;
import okhttp3.HttpUrl;

/* compiled from: 009B */
class VersionedParcelParcel extends VersionedParcel {
    private static final boolean DEBUG = false;
    private static final String TAG = "VersionedParcelParcel";
    private int mCurrentField;
    private final int mEnd;
    private int mFieldId;
    private int mNextRead;
    private final int mOffset;
    private final Parcel mParcel;
    private final SparseIntArray mPositionLookup;
    private final String mPrefix;

    VersionedParcelParcel(Parcel p) {
        this(p, p.dataPosition(), p.dataSize(), HttpUrl.FRAGMENT_ENCODE_SET, new ArrayMap(), new ArrayMap(), new ArrayMap());
    }

    private VersionedParcelParcel(Parcel p, int offset, int end, String prefix, ArrayMap<String, Method> arrayMap, ArrayMap<String, Method> arrayMap2, ArrayMap<String, Class> arrayMap3) {
        super(arrayMap, arrayMap2, arrayMap3);
        this.mPositionLookup = new SparseIntArray();
        this.mCurrentField = -1;
        this.mNextRead = 0;
        this.mFieldId = -1;
        this.mParcel = p;
        this.mOffset = offset;
        this.mEnd = end;
        this.mNextRead = offset;
        this.mPrefix = prefix;
    }

    public void closeField() {
        int i = this.mCurrentField;
        if (i >= 0) {
            int i2 = this.mPositionLookup.get(i);
            int dataPosition = this.mParcel.dataPosition();
            this.mParcel.setDataPosition(i2);
            this.mParcel.writeInt(dataPosition - i2);
            this.mParcel.setDataPosition(dataPosition);
        }
    }

    /* access modifiers changed from: protected */
    public VersionedParcel createSubParcel() {
        Parcel parcel = this.mParcel;
        int dataPosition = parcel.dataPosition();
        int i = this.mNextRead;
        if (i == this.mOffset) {
            i = this.mEnd;
        }
        return new VersionedParcelParcel(parcel, dataPosition, i, this.mPrefix + "  ", this.mReadCache, this.mWriteCache, this.mParcelizerCache);
    }

    public boolean readBoolean() {
        return this.mParcel.readInt() != 0;
    }

    public Bundle readBundle() {
        return this.mParcel.readBundle(getClass().getClassLoader());
    }

    public byte[] readByteArray() {
        int readInt = this.mParcel.readInt();
        if (readInt < 0) {
            return null;
        }
        byte[] bArr = new byte[readInt];
        this.mParcel.readByteArray(bArr);
        return bArr;
    }

    /* access modifiers changed from: protected */
    public CharSequence readCharSequence() {
        return (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(this.mParcel);
    }

    public double readDouble() {
        return this.mParcel.readDouble();
    }

    public boolean readField(int fieldId) {
        while (this.mNextRead < this.mEnd) {
            int i = this.mFieldId;
            if (i == fieldId) {
                return true;
            }
            String valueOf = String.valueOf(i);
            Log1F380D.a((Object) valueOf);
            if (valueOf.compareTo(String.valueOf(fieldId)) > 0) {
                return false;
            }
            this.mParcel.setDataPosition(this.mNextRead);
            int readInt = this.mParcel.readInt();
            this.mFieldId = this.mParcel.readInt();
            this.mNextRead += readInt;
        }
        return this.mFieldId == fieldId;
    }

    public float readFloat() {
        return this.mParcel.readFloat();
    }

    public int readInt() {
        return this.mParcel.readInt();
    }

    public long readLong() {
        return this.mParcel.readLong();
    }

    public <T extends Parcelable> T readParcelable() {
        return this.mParcel.readParcelable(getClass().getClassLoader());
    }

    public String readString() {
        return this.mParcel.readString();
    }

    public IBinder readStrongBinder() {
        return this.mParcel.readStrongBinder();
    }

    public void setOutputField(int fieldId) {
        closeField();
        this.mCurrentField = fieldId;
        this.mPositionLookup.put(fieldId, this.mParcel.dataPosition());
        writeInt(0);
        writeInt(fieldId);
    }

    public void writeBoolean(boolean val) {
        this.mParcel.writeInt(val);
    }

    public void writeBundle(Bundle val) {
        this.mParcel.writeBundle(val);
    }

    public void writeByteArray(byte[] b) {
        if (b != null) {
            this.mParcel.writeInt(b.length);
            this.mParcel.writeByteArray(b);
            return;
        }
        this.mParcel.writeInt(-1);
    }

    public void writeByteArray(byte[] b, int offset, int len) {
        if (b != null) {
            this.mParcel.writeInt(b.length);
            this.mParcel.writeByteArray(b, offset, len);
            return;
        }
        this.mParcel.writeInt(-1);
    }

    /* access modifiers changed from: protected */
    public void writeCharSequence(CharSequence charSequence) {
        TextUtils.writeToParcel(charSequence, this.mParcel, 0);
    }

    public void writeDouble(double val) {
        this.mParcel.writeDouble(val);
    }

    public void writeFloat(float val) {
        this.mParcel.writeFloat(val);
    }

    public void writeInt(int val) {
        this.mParcel.writeInt(val);
    }

    public void writeLong(long val) {
        this.mParcel.writeLong(val);
    }

    public void writeParcelable(Parcelable p) {
        this.mParcel.writeParcelable(p, 0);
    }

    public void writeString(String val) {
        this.mParcel.writeString(val);
    }

    public void writeStrongBinder(IBinder val) {
        this.mParcel.writeStrongBinder(val);
    }

    public void writeStrongInterface(IInterface val) {
        this.mParcel.writeStrongInterface(val);
    }
}

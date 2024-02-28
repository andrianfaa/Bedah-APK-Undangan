package com.google.android.material.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;

public class ParcelableSparseIntArray extends SparseIntArray implements Parcelable {
    public static final Parcelable.Creator<ParcelableSparseIntArray> CREATOR = new Parcelable.Creator<ParcelableSparseIntArray>() {
        public ParcelableSparseIntArray createFromParcel(Parcel source) {
            int readInt = source.readInt();
            ParcelableSparseIntArray parcelableSparseIntArray = new ParcelableSparseIntArray(readInt);
            int[] iArr = new int[readInt];
            int[] iArr2 = new int[readInt];
            source.readIntArray(iArr);
            source.readIntArray(iArr2);
            for (int i = 0; i < readInt; i++) {
                parcelableSparseIntArray.put(iArr[i], iArr2[i]);
            }
            return parcelableSparseIntArray;
        }

        public ParcelableSparseIntArray[] newArray(int size) {
            return new ParcelableSparseIntArray[size];
        }
    };

    public ParcelableSparseIntArray() {
    }

    public ParcelableSparseIntArray(int initialCapacity) {
        super(initialCapacity);
    }

    public ParcelableSparseIntArray(SparseIntArray sparseIntArray) {
        for (int i = 0; i < sparseIntArray.size(); i++) {
            put(sparseIntArray.keyAt(i), sparseIntArray.valueAt(i));
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int[] iArr = new int[size()];
        int[] iArr2 = new int[size()];
        for (int i = 0; i < size(); i++) {
            iArr[i] = keyAt(i);
            iArr2[i] = valueAt(i);
        }
        dest.writeInt(size());
        dest.writeIntArray(iArr);
        dest.writeIntArray(iArr2);
    }
}

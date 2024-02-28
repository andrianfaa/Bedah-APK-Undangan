package com.google.android.material.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

public class ParcelableSparseArray extends SparseArray<Parcelable> implements Parcelable {
    public static final Parcelable.Creator<ParcelableSparseArray> CREATOR = new Parcelable.ClassLoaderCreator<ParcelableSparseArray>() {
        public ParcelableSparseArray createFromParcel(Parcel source) {
            return new ParcelableSparseArray(source, (ClassLoader) null);
        }

        public ParcelableSparseArray createFromParcel(Parcel source, ClassLoader loader) {
            return new ParcelableSparseArray(source, loader);
        }

        public ParcelableSparseArray[] newArray(int size) {
            return new ParcelableSparseArray[size];
        }
    };

    public ParcelableSparseArray() {
    }

    public ParcelableSparseArray(Parcel source, ClassLoader loader) {
        int readInt = source.readInt();
        int[] iArr = new int[readInt];
        source.readIntArray(iArr);
        Parcelable[] readParcelableArray = source.readParcelableArray(loader);
        for (int i = 0; i < readInt; i++) {
            put(iArr[i], readParcelableArray[i]);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        int size = size();
        int[] iArr = new int[size];
        Parcelable[] parcelableArr = new Parcelable[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = keyAt(i);
            parcelableArr[i] = (Parcelable) valueAt(i);
        }
        parcel.writeInt(size);
        parcel.writeIntArray(iArr);
        parcel.writeParcelableArray(parcelableArr, flags);
    }
}

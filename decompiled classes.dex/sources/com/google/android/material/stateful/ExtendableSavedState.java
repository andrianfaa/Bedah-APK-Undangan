package com.google.android.material.stateful;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.collection.SimpleArrayMap;
import androidx.customview.view.AbsSavedState;
import mt.Log1F380D;

/* compiled from: 00FE */
public class ExtendableSavedState extends AbsSavedState {
    public static final Parcelable.Creator<ExtendableSavedState> CREATOR = new Parcelable.ClassLoaderCreator<ExtendableSavedState>() {
        public ExtendableSavedState createFromParcel(Parcel in) {
            return new ExtendableSavedState(in, (ClassLoader) null);
        }

        public ExtendableSavedState createFromParcel(Parcel in, ClassLoader loader) {
            return new ExtendableSavedState(in, loader);
        }

        public ExtendableSavedState[] newArray(int size) {
            return new ExtendableSavedState[size];
        }
    };
    public final SimpleArrayMap<String, Bundle> extendableStates;

    private ExtendableSavedState(Parcel in, ClassLoader loader) {
        super(in, loader);
        int readInt = in.readInt();
        String[] strArr = new String[readInt];
        in.readStringArray(strArr);
        Bundle[] bundleArr = new Bundle[readInt];
        in.readTypedArray(bundleArr, Bundle.CREATOR);
        this.extendableStates = new SimpleArrayMap<>(readInt);
        for (int i = 0; i < readInt; i++) {
            this.extendableStates.put(strArr[i], bundleArr[i]);
        }
    }

    public ExtendableSavedState(Parcelable superState) {
        super(superState);
        this.extendableStates = new SimpleArrayMap<>();
    }

    public String toString() {
        StringBuilder append = new StringBuilder().append("ExtendableSavedState{");
        String hexString = Integer.toHexString(System.identityHashCode(this));
        Log1F380D.a((Object) hexString);
        return append.append(hexString).append(" states=").append(this.extendableStates).append("}").toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        int size = this.extendableStates.size();
        out.writeInt(size);
        String[] strArr = new String[size];
        Bundle[] bundleArr = new Bundle[size];
        for (int i = 0; i < size; i++) {
            strArr[i] = this.extendableStates.keyAt(i);
            bundleArr[i] = this.extendableStates.valueAt(i);
        }
        out.writeStringArray(strArr);
        out.writeTypedArray(bundleArr, 0);
    }
}

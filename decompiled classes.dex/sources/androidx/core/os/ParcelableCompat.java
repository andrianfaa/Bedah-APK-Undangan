package androidx.core.os;

import android.os.Parcel;
import android.os.Parcelable;

@Deprecated
public final class ParcelableCompat {

    static class ParcelableCompatCreatorHoneycombMR2<T> implements Parcelable.ClassLoaderCreator<T> {
        private final ParcelableCompatCreatorCallbacks<T> mCallbacks;

        ParcelableCompatCreatorHoneycombMR2(ParcelableCompatCreatorCallbacks<T> parcelableCompatCreatorCallbacks) {
            this.mCallbacks = parcelableCompatCreatorCallbacks;
        }

        public T createFromParcel(Parcel in) {
            return this.mCallbacks.createFromParcel(in, (ClassLoader) null);
        }

        public T createFromParcel(Parcel in, ClassLoader loader) {
            return this.mCallbacks.createFromParcel(in, loader);
        }

        public T[] newArray(int size) {
            return this.mCallbacks.newArray(size);
        }
    }

    private ParcelableCompat() {
    }

    @Deprecated
    public static <T> Parcelable.Creator<T> newCreator(ParcelableCompatCreatorCallbacks<T> parcelableCompatCreatorCallbacks) {
        return new ParcelableCompatCreatorHoneycombMR2(parcelableCompatCreatorCallbacks);
    }
}

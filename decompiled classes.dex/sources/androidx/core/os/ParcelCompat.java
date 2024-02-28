package androidx.core.os;

import android.os.Parcel;

public final class ParcelCompat {
    private ParcelCompat() {
    }

    public static boolean readBoolean(Parcel in) {
        return in.readInt() != 0;
    }

    public static void writeBoolean(Parcel out, boolean value) {
        out.writeInt(value);
    }
}

package androidx.core.app;

import android.app.PendingIntent;
import androidx.core.graphics.drawable.IconCompat;
import androidx.versionedparcelable.VersionedParcel;

public class RemoteActionCompatParcelizer {
    public static RemoteActionCompat read(VersionedParcel parcel) {
        RemoteActionCompat remoteActionCompat = new RemoteActionCompat();
        remoteActionCompat.mIcon = (IconCompat) parcel.readVersionedParcelable(remoteActionCompat.mIcon, 1);
        remoteActionCompat.mTitle = parcel.readCharSequence(remoteActionCompat.mTitle, 2);
        remoteActionCompat.mContentDescription = parcel.readCharSequence(remoteActionCompat.mContentDescription, 3);
        remoteActionCompat.mActionIntent = (PendingIntent) parcel.readParcelable(remoteActionCompat.mActionIntent, 4);
        remoteActionCompat.mEnabled = parcel.readBoolean(remoteActionCompat.mEnabled, 5);
        remoteActionCompat.mShouldShowIcon = parcel.readBoolean(remoteActionCompat.mShouldShowIcon, 6);
        return remoteActionCompat;
    }

    public static void write(RemoteActionCompat obj, VersionedParcel parcel) {
        parcel.setSerializationFlags(false, false);
        parcel.writeVersionedParcelable(obj.mIcon, 1);
        parcel.writeCharSequence(obj.mTitle, 2);
        parcel.writeCharSequence(obj.mContentDescription, 3);
        parcel.writeParcelable(obj.mActionIntent, 4);
        parcel.writeBoolean(obj.mEnabled, 5);
        parcel.writeBoolean(obj.mShouldShowIcon, 6);
    }
}

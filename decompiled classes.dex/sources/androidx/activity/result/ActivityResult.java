package androidx.activity.result;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import mt.Log1F380D;

/* compiled from: 0002 */
public final class ActivityResult implements Parcelable {
    public static final Parcelable.Creator<ActivityResult> CREATOR = new Parcelable.Creator<ActivityResult>() {
        public ActivityResult createFromParcel(Parcel in) {
            return new ActivityResult(in);
        }

        public ActivityResult[] newArray(int size) {
            return new ActivityResult[size];
        }
    };
    private final Intent mData;
    private final int mResultCode;

    public ActivityResult(int resultCode, Intent data) {
        this.mResultCode = resultCode;
        this.mData = data;
    }

    ActivityResult(Parcel in) {
        this.mResultCode = in.readInt();
        this.mData = in.readInt() == 0 ? null : (Intent) Intent.CREATOR.createFromParcel(in);
    }

    public static String resultCodeToString(int resultCode) {
        switch (resultCode) {
            case -1:
                return "RESULT_OK";
            case 0:
                return "RESULT_CANCELED";
            default:
                String valueOf = String.valueOf(resultCode);
                Log1F380D.a((Object) valueOf);
                return valueOf;
        }
    }

    public int describeContents() {
        return 0;
    }

    public Intent getData() {
        return this.mData;
    }

    public int getResultCode() {
        return this.mResultCode;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mResultCode);
        dest.writeInt(this.mData == null ? 0 : 1);
        Intent intent = this.mData;
        if (intent != null) {
            intent.writeToParcel(dest, flags);
        }
    }

    public String toString() {
        StringBuilder append = new StringBuilder().append("ActivityResult{resultCode=");
        String resultCodeToString = resultCodeToString(this.mResultCode);
        Log1F380D.a((Object) resultCodeToString);
        return append.append(resultCodeToString).append(", data=").append(this.mData).append('}').toString();
    }
}

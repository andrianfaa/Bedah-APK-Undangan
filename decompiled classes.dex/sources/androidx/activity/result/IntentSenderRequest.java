package androidx.activity.result;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Parcel;
import android.os.Parcelable;

public final class IntentSenderRequest implements Parcelable {
    public static final Parcelable.Creator<IntentSenderRequest> CREATOR = new Parcelable.Creator<IntentSenderRequest>() {
        public IntentSenderRequest createFromParcel(Parcel in) {
            return new IntentSenderRequest(in);
        }

        public IntentSenderRequest[] newArray(int size) {
            return new IntentSenderRequest[size];
        }
    };
    private final Intent mFillInIntent;
    private final int mFlagsMask;
    private final int mFlagsValues;
    private final IntentSender mIntentSender;

    public static final class Builder {
        private Intent mFillInIntent;
        private int mFlagsMask;
        private int mFlagsValues;
        private IntentSender mIntentSender;

        public Builder(PendingIntent pendingIntent) {
            this(pendingIntent.getIntentSender());
        }

        public Builder(IntentSender intentSender) {
            this.mIntentSender = intentSender;
        }

        public IntentSenderRequest build() {
            return new IntentSenderRequest(this.mIntentSender, this.mFillInIntent, this.mFlagsMask, this.mFlagsValues);
        }

        public Builder setFillInIntent(Intent fillInIntent) {
            this.mFillInIntent = fillInIntent;
            return this;
        }

        public Builder setFlags(int values, int mask) {
            this.mFlagsValues = values;
            this.mFlagsMask = mask;
            return this;
        }
    }

    IntentSenderRequest(IntentSender intentSender, Intent intent, int flagsMask, int flagsValues) {
        this.mIntentSender = intentSender;
        this.mFillInIntent = intent;
        this.mFlagsMask = flagsMask;
        this.mFlagsValues = flagsValues;
    }

    IntentSenderRequest(Parcel in) {
        this.mIntentSender = (IntentSender) in.readParcelable(IntentSender.class.getClassLoader());
        this.mFillInIntent = (Intent) in.readParcelable(Intent.class.getClassLoader());
        this.mFlagsMask = in.readInt();
        this.mFlagsValues = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public Intent getFillInIntent() {
        return this.mFillInIntent;
    }

    public int getFlagsMask() {
        return this.mFlagsMask;
    }

    public int getFlagsValues() {
        return this.mFlagsValues;
    }

    public IntentSender getIntentSender() {
        return this.mIntentSender;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mIntentSender, flags);
        dest.writeParcelable(this.mFillInIntent, flags);
        dest.writeInt(this.mFlagsMask);
        dest.writeInt(this.mFlagsValues);
    }
}

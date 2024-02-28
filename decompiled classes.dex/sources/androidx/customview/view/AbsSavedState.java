package androidx.customview.view;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class AbsSavedState implements Parcelable {
    public static final Parcelable.Creator<AbsSavedState> CREATOR = new Parcelable.ClassLoaderCreator<AbsSavedState>() {
        public AbsSavedState createFromParcel(Parcel in) {
            return createFromParcel(in, (ClassLoader) null);
        }

        public AbsSavedState createFromParcel(Parcel in, ClassLoader loader) {
            if (in.readParcelable(loader) == null) {
                return AbsSavedState.EMPTY_STATE;
            }
            throw new IllegalStateException("superState must be null");
        }

        public AbsSavedState[] newArray(int size) {
            return new AbsSavedState[size];
        }
    };
    public static final AbsSavedState EMPTY_STATE = new AbsSavedState() {
    };
    private final Parcelable mSuperState;

    private AbsSavedState() {
        this.mSuperState = null;
    }

    protected AbsSavedState(Parcel source) {
        this(source, (ClassLoader) null);
    }

    protected AbsSavedState(Parcel source, ClassLoader loader) {
        Parcelable readParcelable = source.readParcelable(loader);
        this.mSuperState = readParcelable != null ? readParcelable : EMPTY_STATE;
    }

    protected AbsSavedState(Parcelable superState) {
        if (superState != null) {
            this.mSuperState = superState != EMPTY_STATE ? superState : null;
            return;
        }
        throw new IllegalArgumentException("superState must not be null");
    }

    public int describeContents() {
        return 0;
    }

    public final Parcelable getSuperState() {
        return this.mSuperState;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mSuperState, flags);
    }
}

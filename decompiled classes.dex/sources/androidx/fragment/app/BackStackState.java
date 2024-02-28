package androidx.fragment.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import java.util.ArrayList;

final class BackStackState implements Parcelable {
    public static final Parcelable.Creator<BackStackState> CREATOR = new Parcelable.Creator<BackStackState>() {
        public BackStackState createFromParcel(Parcel in) {
            return new BackStackState(in);
        }

        public BackStackState[] newArray(int size) {
            return new BackStackState[size];
        }
    };
    private static final String TAG = "FragmentManager";
    final int mBreadCrumbShortTitleRes;
    final CharSequence mBreadCrumbShortTitleText;
    final int mBreadCrumbTitleRes;
    final CharSequence mBreadCrumbTitleText;
    final int[] mCurrentMaxLifecycleStates;
    final ArrayList<String> mFragmentWhos;
    final int mIndex;
    final String mName;
    final int[] mOldMaxLifecycleStates;
    final int[] mOps;
    final boolean mReorderingAllowed;
    final ArrayList<String> mSharedElementSourceNames;
    final ArrayList<String> mSharedElementTargetNames;
    final int mTransition;

    public BackStackState(Parcel in) {
        this.mOps = in.createIntArray();
        this.mFragmentWhos = in.createStringArrayList();
        this.mOldMaxLifecycleStates = in.createIntArray();
        this.mCurrentMaxLifecycleStates = in.createIntArray();
        this.mTransition = in.readInt();
        this.mName = in.readString();
        this.mIndex = in.readInt();
        this.mBreadCrumbTitleRes = in.readInt();
        this.mBreadCrumbTitleText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.mBreadCrumbShortTitleRes = in.readInt();
        this.mBreadCrumbShortTitleText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        this.mSharedElementSourceNames = in.createStringArrayList();
        this.mSharedElementTargetNames = in.createStringArrayList();
        this.mReorderingAllowed = in.readInt() != 0;
    }

    public BackStackState(BackStackRecord bse) {
        int size = bse.mOps.size();
        this.mOps = new int[(size * 5)];
        if (bse.mAddToBackStack) {
            this.mFragmentWhos = new ArrayList<>(size);
            this.mOldMaxLifecycleStates = new int[size];
            this.mCurrentMaxLifecycleStates = new int[size];
            int i = 0;
            int i2 = 0;
            while (i2 < size) {
                FragmentTransaction.Op op = (FragmentTransaction.Op) bse.mOps.get(i2);
                int i3 = i + 1;
                this.mOps[i] = op.mCmd;
                this.mFragmentWhos.add(op.mFragment != null ? op.mFragment.mWho : null);
                int i4 = i3 + 1;
                this.mOps[i3] = op.mEnterAnim;
                int i5 = i4 + 1;
                this.mOps[i4] = op.mExitAnim;
                int i6 = i5 + 1;
                this.mOps[i5] = op.mPopEnterAnim;
                this.mOps[i6] = op.mPopExitAnim;
                this.mOldMaxLifecycleStates[i2] = op.mOldMaxState.ordinal();
                this.mCurrentMaxLifecycleStates[i2] = op.mCurrentMaxState.ordinal();
                i2++;
                i = i6 + 1;
            }
            this.mTransition = bse.mTransition;
            this.mName = bse.mName;
            this.mIndex = bse.mIndex;
            this.mBreadCrumbTitleRes = bse.mBreadCrumbTitleRes;
            this.mBreadCrumbTitleText = bse.mBreadCrumbTitleText;
            this.mBreadCrumbShortTitleRes = bse.mBreadCrumbShortTitleRes;
            this.mBreadCrumbShortTitleText = bse.mBreadCrumbShortTitleText;
            this.mSharedElementSourceNames = bse.mSharedElementSourceNames;
            this.mSharedElementTargetNames = bse.mSharedElementTargetNames;
            this.mReorderingAllowed = bse.mReorderingAllowed;
            return;
        }
        throw new IllegalStateException("Not on back stack");
    }

    public int describeContents() {
        return 0;
    }

    public BackStackRecord instantiate(FragmentManager fm) {
        BackStackRecord backStackRecord = new BackStackRecord(fm);
        int i = 0;
        int i2 = 0;
        while (i < this.mOps.length) {
            FragmentTransaction.Op op = new FragmentTransaction.Op();
            int i3 = i + 1;
            op.mCmd = this.mOps[i];
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v(TAG, "Instantiate " + backStackRecord + " op #" + i2 + " base fragment #" + this.mOps[i3]);
            }
            String str = this.mFragmentWhos.get(i2);
            if (str != null) {
                op.mFragment = fm.findActiveFragment(str);
            } else {
                op.mFragment = null;
            }
            op.mOldMaxState = Lifecycle.State.values()[this.mOldMaxLifecycleStates[i2]];
            op.mCurrentMaxState = Lifecycle.State.values()[this.mCurrentMaxLifecycleStates[i2]];
            int i4 = i3 + 1;
            op.mEnterAnim = this.mOps[i3];
            int i5 = i4 + 1;
            op.mExitAnim = this.mOps[i4];
            int i6 = i5 + 1;
            op.mPopEnterAnim = this.mOps[i5];
            op.mPopExitAnim = this.mOps[i6];
            backStackRecord.mEnterAnim = op.mEnterAnim;
            backStackRecord.mExitAnim = op.mExitAnim;
            backStackRecord.mPopEnterAnim = op.mPopEnterAnim;
            backStackRecord.mPopExitAnim = op.mPopExitAnim;
            backStackRecord.addOp(op);
            i2++;
            i = i6 + 1;
        }
        backStackRecord.mTransition = this.mTransition;
        backStackRecord.mName = this.mName;
        backStackRecord.mIndex = this.mIndex;
        backStackRecord.mAddToBackStack = true;
        backStackRecord.mBreadCrumbTitleRes = this.mBreadCrumbTitleRes;
        backStackRecord.mBreadCrumbTitleText = this.mBreadCrumbTitleText;
        backStackRecord.mBreadCrumbShortTitleRes = this.mBreadCrumbShortTitleRes;
        backStackRecord.mBreadCrumbShortTitleText = this.mBreadCrumbShortTitleText;
        backStackRecord.mSharedElementSourceNames = this.mSharedElementSourceNames;
        backStackRecord.mSharedElementTargetNames = this.mSharedElementTargetNames;
        backStackRecord.mReorderingAllowed = this.mReorderingAllowed;
        backStackRecord.bumpBackStackNesting(1);
        return backStackRecord;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(this.mOps);
        dest.writeStringList(this.mFragmentWhos);
        dest.writeIntArray(this.mOldMaxLifecycleStates);
        dest.writeIntArray(this.mCurrentMaxLifecycleStates);
        dest.writeInt(this.mTransition);
        dest.writeString(this.mName);
        dest.writeInt(this.mIndex);
        dest.writeInt(this.mBreadCrumbTitleRes);
        TextUtils.writeToParcel(this.mBreadCrumbTitleText, dest, 0);
        dest.writeInt(this.mBreadCrumbShortTitleRes);
        TextUtils.writeToParcel(this.mBreadCrumbShortTitleText, dest, 0);
        dest.writeStringList(this.mSharedElementSourceNames);
        dest.writeStringList(this.mSharedElementTargetNames);
        dest.writeInt(this.mReorderingAllowed ? 1 : 0);
    }
}

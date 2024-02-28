package androidx.fragment.app;

import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import java.io.PrintWriter;
import java.util.ArrayList;
import mt.Log1F380D;

/* compiled from: 0075 */
final class BackStackRecord extends FragmentTransaction implements FragmentManager.BackStackEntry, FragmentManager.OpGenerator {
    private static final String TAG = "FragmentManager";
    boolean mCommitted;
    int mIndex;
    final FragmentManager mManager;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    BackStackRecord(FragmentManager manager) {
        super(manager.getFragmentFactory(), manager.getHost() != null ? manager.getHost().getContext().getClassLoader() : null);
        this.mIndex = -1;
        this.mManager = manager;
    }

    private static boolean isFragmentPostponed(FragmentTransaction.Op op) {
        Fragment fragment = op.mFragment;
        return fragment != null && fragment.mAdded && fragment.mView != null && !fragment.mDetached && !fragment.mHidden && fragment.isPostponed();
    }

    /* access modifiers changed from: package-private */
    public void bumpBackStackNesting(int amt) {
        if (this.mAddToBackStack) {
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v(TAG, "Bump nesting in " + this + " by " + amt);
            }
            int size = this.mOps.size();
            for (int i = 0; i < size; i++) {
                FragmentTransaction.Op op = (FragmentTransaction.Op) this.mOps.get(i);
                if (op.mFragment != null) {
                    op.mFragment.mBackStackNesting += amt;
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v(TAG, "Bump nesting of " + op.mFragment + " to " + op.mFragment.mBackStackNesting);
                    }
                }
            }
        }
    }

    public int commit() {
        return commitInternal(false);
    }

    public int commitAllowingStateLoss() {
        return commitInternal(true);
    }

    /* access modifiers changed from: package-private */
    public int commitInternal(boolean allowStateLoss) {
        if (!this.mCommitted) {
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v(TAG, "Commit: " + this);
                PrintWriter printWriter = new PrintWriter(new LogWriter(TAG));
                dump("  ", printWriter);
                printWriter.close();
            }
            this.mCommitted = true;
            if (this.mAddToBackStack) {
                this.mIndex = this.mManager.allocBackStackIndex();
            } else {
                this.mIndex = -1;
            }
            this.mManager.enqueueAction(this, allowStateLoss);
            return this.mIndex;
        }
        throw new IllegalStateException("commit already called");
    }

    public void commitNow() {
        disallowAddToBackStack();
        this.mManager.execSingleAction(this, false);
    }

    public void commitNowAllowingStateLoss() {
        disallowAddToBackStack();
        this.mManager.execSingleAction(this, true);
    }

    public FragmentTransaction detach(Fragment fragment) {
        if (fragment.mFragmentManager == null || fragment.mFragmentManager == this.mManager) {
            return super.detach(fragment);
        }
        throw new IllegalStateException("Cannot detach Fragment attached to a different FragmentManager. Fragment " + fragment.toString() + " is already attached to a FragmentManager.");
    }

    /* access modifiers changed from: package-private */
    public void doAddOp(int containerViewId, Fragment fragment, String tag, int opcmd) {
        super.doAddOp(containerViewId, fragment, tag, opcmd);
        fragment.mFragmentManager = this.mManager;
    }

    public void dump(String prefix, PrintWriter writer) {
        dump(prefix, writer, true);
    }

    public void dump(String prefix, PrintWriter writer, boolean full) {
        String str;
        if (full) {
            writer.print(prefix);
            writer.print("mName=");
            writer.print(this.mName);
            writer.print(" mIndex=");
            writer.print(this.mIndex);
            writer.print(" mCommitted=");
            writer.println(this.mCommitted);
            if (this.mTransition != 0) {
                writer.print(prefix);
                writer.print("mTransition=#");
                String hexString = Integer.toHexString(this.mTransition);
                Log1F380D.a((Object) hexString);
                writer.print(hexString);
            }
            if (!(this.mEnterAnim == 0 && this.mExitAnim == 0)) {
                writer.print(prefix);
                writer.print("mEnterAnim=#");
                String hexString2 = Integer.toHexString(this.mEnterAnim);
                Log1F380D.a((Object) hexString2);
                writer.print(hexString2);
                writer.print(" mExitAnim=#");
                String hexString3 = Integer.toHexString(this.mExitAnim);
                Log1F380D.a((Object) hexString3);
                writer.println(hexString3);
            }
            if (!(this.mPopEnterAnim == 0 && this.mPopExitAnim == 0)) {
                writer.print(prefix);
                writer.print("mPopEnterAnim=#");
                String hexString4 = Integer.toHexString(this.mPopEnterAnim);
                Log1F380D.a((Object) hexString4);
                writer.print(hexString4);
                writer.print(" mPopExitAnim=#");
                String hexString5 = Integer.toHexString(this.mPopExitAnim);
                Log1F380D.a((Object) hexString5);
                writer.println(hexString5);
            }
            if (!(this.mBreadCrumbTitleRes == 0 && this.mBreadCrumbTitleText == null)) {
                writer.print(prefix);
                writer.print("mBreadCrumbTitleRes=#");
                String hexString6 = Integer.toHexString(this.mBreadCrumbTitleRes);
                Log1F380D.a((Object) hexString6);
                writer.print(hexString6);
                writer.print(" mBreadCrumbTitleText=");
                writer.println(this.mBreadCrumbTitleText);
            }
            if (!(this.mBreadCrumbShortTitleRes == 0 && this.mBreadCrumbShortTitleText == null)) {
                writer.print(prefix);
                writer.print("mBreadCrumbShortTitleRes=#");
                String hexString7 = Integer.toHexString(this.mBreadCrumbShortTitleRes);
                Log1F380D.a((Object) hexString7);
                writer.print(hexString7);
                writer.print(" mBreadCrumbShortTitleText=");
                writer.println(this.mBreadCrumbShortTitleText);
            }
        }
        if (!this.mOps.isEmpty()) {
            writer.print(prefix);
            writer.println("Operations:");
            int size = this.mOps.size();
            for (int i = 0; i < size; i++) {
                FragmentTransaction.Op op = (FragmentTransaction.Op) this.mOps.get(i);
                switch (op.mCmd) {
                    case 0:
                        str = "NULL";
                        break;
                    case 1:
                        str = "ADD";
                        break;
                    case 2:
                        str = "REPLACE";
                        break;
                    case 3:
                        str = "REMOVE";
                        break;
                    case 4:
                        str = "HIDE";
                        break;
                    case 5:
                        str = "SHOW";
                        break;
                    case 6:
                        str = "DETACH";
                        break;
                    case 7:
                        str = "ATTACH";
                        break;
                    case 8:
                        str = "SET_PRIMARY_NAV";
                        break;
                    case 9:
                        str = "UNSET_PRIMARY_NAV";
                        break;
                    case 10:
                        str = "OP_SET_MAX_LIFECYCLE";
                        break;
                    default:
                        str = "cmd=" + op.mCmd;
                        break;
                }
                writer.print(prefix);
                writer.print("  Op #");
                writer.print(i);
                writer.print(": ");
                writer.print(str);
                writer.print(" ");
                writer.println(op.mFragment);
                if (full) {
                    if (!(op.mEnterAnim == 0 && op.mExitAnim == 0)) {
                        writer.print(prefix);
                        writer.print("enterAnim=#");
                        String hexString8 = Integer.toHexString(op.mEnterAnim);
                        Log1F380D.a((Object) hexString8);
                        writer.print(hexString8);
                        writer.print(" exitAnim=#");
                        String hexString9 = Integer.toHexString(op.mExitAnim);
                        Log1F380D.a((Object) hexString9);
                        writer.println(hexString9);
                    }
                    if (op.mPopEnterAnim != 0 || op.mPopExitAnim != 0) {
                        writer.print(prefix);
                        writer.print("popEnterAnim=#");
                        String hexString10 = Integer.toHexString(op.mPopEnterAnim);
                        Log1F380D.a((Object) hexString10);
                        writer.print(hexString10);
                        writer.print(" popExitAnim=#");
                        String hexString11 = Integer.toHexString(op.mPopExitAnim);
                        Log1F380D.a((Object) hexString11);
                        writer.println(hexString11);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void executeOps() {
        int size = this.mOps.size();
        for (int i = 0; i < size; i++) {
            FragmentTransaction.Op op = (FragmentTransaction.Op) this.mOps.get(i);
            Fragment fragment = op.mFragment;
            if (fragment != null) {
                fragment.setPopDirection(false);
                fragment.setNextTransition(this.mTransition);
                fragment.setSharedElementNames(this.mSharedElementSourceNames, this.mSharedElementTargetNames);
            }
            switch (op.mCmd) {
                case 1:
                    fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.setExitAnimationOrder(fragment, false);
                    this.mManager.addFragment(fragment);
                    break;
                case 3:
                    fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.removeFragment(fragment);
                    break;
                case 4:
                    fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.hideFragment(fragment);
                    break;
                case 5:
                    fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.setExitAnimationOrder(fragment, false);
                    this.mManager.showFragment(fragment);
                    break;
                case 6:
                    fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.detachFragment(fragment);
                    break;
                case 7:
                    fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.setExitAnimationOrder(fragment, false);
                    this.mManager.attachFragment(fragment);
                    break;
                case 8:
                    this.mManager.setPrimaryNavigationFragment(fragment);
                    break;
                case 9:
                    this.mManager.setPrimaryNavigationFragment((Fragment) null);
                    break;
                case 10:
                    this.mManager.setMaxLifecycle(fragment, op.mCurrentMaxState);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown cmd: " + op.mCmd);
            }
            if (!this.mReorderingAllowed && op.mCmd != 1 && fragment != null && !FragmentManager.USE_STATE_MANAGER) {
                this.mManager.moveFragmentToExpectedState(fragment);
            }
        }
        if (!this.mReorderingAllowed && !FragmentManager.USE_STATE_MANAGER) {
            FragmentManager fragmentManager = this.mManager;
            fragmentManager.moveToState(fragmentManager.mCurState, true);
        }
    }

    /* access modifiers changed from: package-private */
    public void executePopOps(boolean moveToState) {
        for (int size = this.mOps.size() - 1; size >= 0; size--) {
            FragmentTransaction.Op op = (FragmentTransaction.Op) this.mOps.get(size);
            Fragment fragment = op.mFragment;
            if (fragment != null) {
                fragment.setPopDirection(true);
                fragment.setNextTransition(FragmentManager.reverseTransit(this.mTransition));
                fragment.setSharedElementNames(this.mSharedElementTargetNames, this.mSharedElementSourceNames);
            }
            switch (op.mCmd) {
                case 1:
                    fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.setExitAnimationOrder(fragment, true);
                    this.mManager.removeFragment(fragment);
                    break;
                case 3:
                    fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.addFragment(fragment);
                    break;
                case 4:
                    fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.showFragment(fragment);
                    break;
                case 5:
                    fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.setExitAnimationOrder(fragment, true);
                    this.mManager.hideFragment(fragment);
                    break;
                case 6:
                    fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.attachFragment(fragment);
                    break;
                case 7:
                    fragment.setAnimations(op.mEnterAnim, op.mExitAnim, op.mPopEnterAnim, op.mPopExitAnim);
                    this.mManager.setExitAnimationOrder(fragment, true);
                    this.mManager.detachFragment(fragment);
                    break;
                case 8:
                    this.mManager.setPrimaryNavigationFragment((Fragment) null);
                    break;
                case 9:
                    this.mManager.setPrimaryNavigationFragment(fragment);
                    break;
                case 10:
                    this.mManager.setMaxLifecycle(fragment, op.mOldMaxState);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown cmd: " + op.mCmd);
            }
            if (!this.mReorderingAllowed && op.mCmd != 3 && fragment != null && !FragmentManager.USE_STATE_MANAGER) {
                this.mManager.moveFragmentToExpectedState(fragment);
            }
        }
        if (!this.mReorderingAllowed && moveToState && !FragmentManager.USE_STATE_MANAGER) {
            FragmentManager fragmentManager = this.mManager;
            fragmentManager.moveToState(fragmentManager.mCurState, true);
        }
    }

    /* access modifiers changed from: package-private */
    public Fragment expandOps(ArrayList<Fragment> arrayList, Fragment oldPrimaryNav) {
        int i = 0;
        while (i < this.mOps.size()) {
            FragmentTransaction.Op op = (FragmentTransaction.Op) this.mOps.get(i);
            switch (op.mCmd) {
                case 1:
                case 7:
                    arrayList.add(op.mFragment);
                    break;
                case 2:
                    Fragment fragment = op.mFragment;
                    int i2 = fragment.mContainerId;
                    boolean z = false;
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        Fragment fragment2 = arrayList.get(size);
                        if (fragment2.mContainerId == i2) {
                            if (fragment2 == fragment) {
                                z = true;
                            } else {
                                if (fragment2 == oldPrimaryNav) {
                                    this.mOps.add(i, new FragmentTransaction.Op(9, fragment2));
                                    i++;
                                    oldPrimaryNav = null;
                                }
                                FragmentTransaction.Op op2 = new FragmentTransaction.Op(3, fragment2);
                                op2.mEnterAnim = op.mEnterAnim;
                                op2.mPopEnterAnim = op.mPopEnterAnim;
                                op2.mExitAnim = op.mExitAnim;
                                op2.mPopExitAnim = op.mPopExitAnim;
                                this.mOps.add(i, op2);
                                arrayList.remove(fragment2);
                                i++;
                            }
                        }
                    }
                    if (!z) {
                        op.mCmd = 1;
                        arrayList.add(fragment);
                        break;
                    } else {
                        this.mOps.remove(i);
                        i--;
                        break;
                    }
                case 3:
                case 6:
                    arrayList.remove(op.mFragment);
                    if (op.mFragment != oldPrimaryNav) {
                        break;
                    } else {
                        this.mOps.add(i, new FragmentTransaction.Op(9, op.mFragment));
                        i++;
                        oldPrimaryNav = null;
                        break;
                    }
                case 8:
                    this.mOps.add(i, new FragmentTransaction.Op(9, oldPrimaryNav));
                    i++;
                    oldPrimaryNav = op.mFragment;
                    break;
            }
            i++;
        }
        return oldPrimaryNav;
    }

    public boolean generateOps(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        if (FragmentManager.isLoggingEnabled(2)) {
            Log.v(TAG, "Run: " + this);
        }
        arrayList.add(this);
        arrayList2.add(false);
        if (!this.mAddToBackStack) {
            return true;
        }
        this.mManager.addBackStackState(this);
        return true;
    }

    public CharSequence getBreadCrumbShortTitle() {
        return this.mBreadCrumbShortTitleRes != 0 ? this.mManager.getHost().getContext().getText(this.mBreadCrumbShortTitleRes) : this.mBreadCrumbShortTitleText;
    }

    public int getBreadCrumbShortTitleRes() {
        return this.mBreadCrumbShortTitleRes;
    }

    public CharSequence getBreadCrumbTitle() {
        return this.mBreadCrumbTitleRes != 0 ? this.mManager.getHost().getContext().getText(this.mBreadCrumbTitleRes) : this.mBreadCrumbTitleText;
    }

    public int getBreadCrumbTitleRes() {
        return this.mBreadCrumbTitleRes;
    }

    public int getId() {
        return this.mIndex;
    }

    public String getName() {
        return this.mName;
    }

    public FragmentTransaction hide(Fragment fragment) {
        if (fragment.mFragmentManager == null || fragment.mFragmentManager == this.mManager) {
            return super.hide(fragment);
        }
        throw new IllegalStateException("Cannot hide Fragment attached to a different FragmentManager. Fragment " + fragment.toString() + " is already attached to a FragmentManager.");
    }

    /* access modifiers changed from: package-private */
    public boolean interactsWith(int containerId) {
        int size = this.mOps.size();
        int i = 0;
        while (true) {
            int i2 = 0;
            if (i >= size) {
                return false;
            }
            FragmentTransaction.Op op = (FragmentTransaction.Op) this.mOps.get(i);
            if (op.mFragment != null) {
                i2 = op.mFragment.mContainerId;
            }
            if (i2 != 0 && i2 == containerId) {
                return true;
            }
            i++;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean interactsWith(ArrayList<BackStackRecord> arrayList, int startIndex, int endIndex) {
        if (endIndex == startIndex) {
            return false;
        }
        int size = this.mOps.size();
        int i = -1;
        for (int i2 = 0; i2 < size; i2++) {
            FragmentTransaction.Op op = (FragmentTransaction.Op) this.mOps.get(i2);
            int i3 = op.mFragment != null ? op.mFragment.mContainerId : 0;
            if (!(i3 == 0 || i3 == i)) {
                i = i3;
                for (int i4 = startIndex; i4 < endIndex; i4++) {
                    BackStackRecord backStackRecord = arrayList.get(i4);
                    int size2 = backStackRecord.mOps.size();
                    for (int i5 = 0; i5 < size2; i5++) {
                        FragmentTransaction.Op op2 = (FragmentTransaction.Op) backStackRecord.mOps.get(i5);
                        if ((op2.mFragment != null ? op2.mFragment.mContainerId : 0) == i3) {
                            return true;
                        }
                    }
                }
                continue;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return this.mOps.isEmpty();
    }

    /* access modifiers changed from: package-private */
    public boolean isPostponed() {
        for (int i = 0; i < this.mOps.size(); i++) {
            if (isFragmentPostponed((FragmentTransaction.Op) this.mOps.get(i))) {
                return true;
            }
        }
        return false;
    }

    public FragmentTransaction remove(Fragment fragment) {
        if (fragment.mFragmentManager == null || fragment.mFragmentManager == this.mManager) {
            return super.remove(fragment);
        }
        throw new IllegalStateException("Cannot remove Fragment attached to a different FragmentManager. Fragment " + fragment.toString() + " is already attached to a FragmentManager.");
    }

    public void runOnCommitRunnables() {
        if (this.mCommitRunnables != null) {
            for (int i = 0; i < this.mCommitRunnables.size(); i++) {
                ((Runnable) this.mCommitRunnables.get(i)).run();
            }
            this.mCommitRunnables = null;
        }
    }

    public FragmentTransaction setMaxLifecycle(Fragment fragment, Lifecycle.State state) {
        if (fragment.mFragmentManager != this.mManager) {
            throw new IllegalArgumentException("Cannot setMaxLifecycle for Fragment not attached to FragmentManager " + this.mManager);
        } else if (state == Lifecycle.State.INITIALIZED && fragment.mState > -1) {
            throw new IllegalArgumentException("Cannot set maximum Lifecycle to " + state + " after the Fragment has been created");
        } else if (state != Lifecycle.State.DESTROYED) {
            return super.setMaxLifecycle(fragment, state);
        } else {
            throw new IllegalArgumentException("Cannot set maximum Lifecycle to " + state + ". Use remove() to remove the fragment from the FragmentManager and trigger its destruction.");
        }
    }

    /* access modifiers changed from: package-private */
    public void setOnStartPostponedListener(Fragment.OnStartEnterTransitionListener listener) {
        for (int i = 0; i < this.mOps.size(); i++) {
            FragmentTransaction.Op op = (FragmentTransaction.Op) this.mOps.get(i);
            if (isFragmentPostponed(op)) {
                op.mFragment.setOnStartEnterTransitionListener(listener);
            }
        }
    }

    public FragmentTransaction setPrimaryNavigationFragment(Fragment fragment) {
        if (fragment == null || fragment.mFragmentManager == null || fragment.mFragmentManager == this.mManager) {
            return super.setPrimaryNavigationFragment(fragment);
        }
        throw new IllegalStateException("Cannot setPrimaryNavigation for Fragment attached to a different FragmentManager. Fragment " + fragment.toString() + " is already attached to a FragmentManager.");
    }

    public FragmentTransaction show(Fragment fragment) {
        if (fragment.mFragmentManager == null || fragment.mFragmentManager == this.mManager) {
            return super.show(fragment);
        }
        throw new IllegalStateException("Cannot show Fragment attached to a different FragmentManager. Fragment " + fragment.toString() + " is already attached to a FragmentManager.");
    }

    /* access modifiers changed from: package-private */
    public Fragment trackAddedFragmentsInPop(ArrayList<Fragment> arrayList, Fragment oldPrimaryNav) {
        for (int size = this.mOps.size() - 1; size >= 0; size--) {
            FragmentTransaction.Op op = (FragmentTransaction.Op) this.mOps.get(size);
            switch (op.mCmd) {
                case 1:
                case 7:
                    arrayList.remove(op.mFragment);
                    break;
                case 3:
                case 6:
                    arrayList.add(op.mFragment);
                    break;
                case 8:
                    oldPrimaryNav = null;
                    break;
                case 9:
                    oldPrimaryNav = op.mFragment;
                    break;
                case 10:
                    op.mCurrentMaxState = op.mOldMaxState;
                    break;
            }
        }
        return oldPrimaryNav;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("BackStackEntry{");
        String hexString = Integer.toHexString(System.identityHashCode(this));
        Log1F380D.a((Object) hexString);
        sb.append(hexString);
        if (this.mIndex >= 0) {
            sb.append(" #");
            sb.append(this.mIndex);
        }
        if (this.mName != null) {
            sb.append(" ");
            sb.append(this.mName);
        }
        sb.append("}");
        return sb.toString();
    }
}

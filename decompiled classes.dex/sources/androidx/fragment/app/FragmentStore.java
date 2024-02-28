package androidx.fragment.app;

import android.util.Log;
import android.view.ViewGroup;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class FragmentStore {
    private static final String TAG = "FragmentManager";
    private final HashMap<String, FragmentStateManager> mActive = new HashMap<>();
    private final ArrayList<Fragment> mAdded = new ArrayList<>();
    private FragmentManagerViewModel mNonConfig;

    FragmentStore() {
    }

    /* access modifiers changed from: package-private */
    public void addFragment(Fragment fragment) {
        if (!this.mAdded.contains(fragment)) {
            synchronized (this.mAdded) {
                this.mAdded.add(fragment);
            }
            fragment.mAdded = true;
            return;
        }
        throw new IllegalStateException("Fragment already added: " + fragment);
    }

    /* access modifiers changed from: package-private */
    public void burpActive() {
        this.mActive.values().removeAll(Collections.singleton((Object) null));
    }

    /* access modifiers changed from: package-private */
    public boolean containsActiveFragment(String who) {
        return this.mActive.get(who) != null;
    }

    /* access modifiers changed from: package-private */
    public void dispatchStateChange(int state) {
        for (FragmentStateManager next : this.mActive.values()) {
            if (next != null) {
                next.setFragmentManagerState(state);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        String str = prefix + "    ";
        if (!this.mActive.isEmpty()) {
            writer.print(prefix);
            writer.println("Active Fragments:");
            for (FragmentStateManager next : this.mActive.values()) {
                writer.print(prefix);
                if (next != null) {
                    Fragment fragment = next.getFragment();
                    writer.println(fragment);
                    fragment.dump(str, fd, writer, args);
                } else {
                    writer.println("null");
                }
            }
        }
        int size = this.mAdded.size();
        if (size > 0) {
            writer.print(prefix);
            writer.println("Added Fragments:");
            for (int i = 0; i < size; i++) {
                writer.print(prefix);
                writer.print("  #");
                writer.print(i);
                writer.print(": ");
                writer.println(this.mAdded.get(i).toString());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Fragment findActiveFragment(String who) {
        FragmentStateManager fragmentStateManager = this.mActive.get(who);
        if (fragmentStateManager != null) {
            return fragmentStateManager.getFragment();
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public Fragment findFragmentById(int id) {
        for (int size = this.mAdded.size() - 1; size >= 0; size--) {
            Fragment fragment = this.mAdded.get(size);
            if (fragment != null && fragment.mFragmentId == id) {
                return fragment;
            }
        }
        for (FragmentStateManager next : this.mActive.values()) {
            if (next != null) {
                Fragment fragment2 = next.getFragment();
                if (fragment2.mFragmentId == id) {
                    return fragment2;
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public Fragment findFragmentByTag(String tag) {
        if (tag != null) {
            for (int size = this.mAdded.size() - 1; size >= 0; size--) {
                Fragment fragment = this.mAdded.get(size);
                if (fragment != null && tag.equals(fragment.mTag)) {
                    return fragment;
                }
            }
        }
        if (tag == null) {
            return null;
        }
        for (FragmentStateManager next : this.mActive.values()) {
            if (next != null) {
                Fragment fragment2 = next.getFragment();
                if (tag.equals(fragment2.mTag)) {
                    return fragment2;
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public Fragment findFragmentByWho(String who) {
        for (FragmentStateManager next : this.mActive.values()) {
            if (next != null) {
                Fragment findFragmentByWho = next.getFragment().findFragmentByWho(who);
                Fragment fragment = findFragmentByWho;
                if (findFragmentByWho != null) {
                    return fragment;
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public int findFragmentIndexInContainer(Fragment f) {
        ViewGroup viewGroup = f.mContainer;
        if (viewGroup == null) {
            return -1;
        }
        int indexOf = this.mAdded.indexOf(f);
        for (int i = indexOf - 1; i >= 0; i--) {
            Fragment fragment = this.mAdded.get(i);
            if (fragment.mContainer == viewGroup && fragment.mView != null) {
                return viewGroup.indexOfChild(fragment.mView) + 1;
            }
        }
        for (int i2 = indexOf + 1; i2 < this.mAdded.size(); i2++) {
            Fragment fragment2 = this.mAdded.get(i2);
            if (fragment2.mContainer == viewGroup && fragment2.mView != null) {
                return viewGroup.indexOfChild(fragment2.mView);
            }
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public int getActiveFragmentCount() {
        return this.mActive.size();
    }

    /* access modifiers changed from: package-private */
    public List<FragmentStateManager> getActiveFragmentStateManagers() {
        ArrayList arrayList = new ArrayList();
        for (FragmentStateManager next : this.mActive.values()) {
            if (next != null) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public List<Fragment> getActiveFragments() {
        ArrayList arrayList = new ArrayList();
        for (FragmentStateManager next : this.mActive.values()) {
            if (next != null) {
                arrayList.add(next.getFragment());
            } else {
                arrayList.add((Object) null);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public FragmentStateManager getFragmentStateManager(String who) {
        return this.mActive.get(who);
    }

    /* access modifiers changed from: package-private */
    public List<Fragment> getFragments() {
        ArrayList arrayList;
        if (this.mAdded.isEmpty()) {
            return Collections.emptyList();
        }
        synchronized (this.mAdded) {
            arrayList = new ArrayList(this.mAdded);
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public FragmentManagerViewModel getNonConfig() {
        return this.mNonConfig;
    }

    /* access modifiers changed from: package-private */
    public void makeActive(FragmentStateManager newlyActive) {
        Fragment fragment = newlyActive.getFragment();
        if (!containsActiveFragment(fragment.mWho)) {
            this.mActive.put(fragment.mWho, newlyActive);
            if (fragment.mRetainInstanceChangedWhileDetached) {
                if (fragment.mRetainInstance) {
                    this.mNonConfig.addRetainedFragment(fragment);
                } else {
                    this.mNonConfig.removeRetainedFragment(fragment);
                }
                fragment.mRetainInstanceChangedWhileDetached = false;
            }
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v(TAG, "Added fragment to active set " + fragment);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void makeInactive(FragmentStateManager newlyInactive) {
        Fragment fragment = newlyInactive.getFragment();
        if (fragment.mRetainInstance) {
            this.mNonConfig.removeRetainedFragment(fragment);
        }
        if (this.mActive.put(fragment.mWho, (Object) null) != null && FragmentManager.isLoggingEnabled(2)) {
            Log.v(TAG, "Removed fragment from active set " + fragment);
        }
    }

    /* access modifiers changed from: package-private */
    public void moveToExpectedState() {
        Iterator<Fragment> it = this.mAdded.iterator();
        while (it.hasNext()) {
            FragmentStateManager fragmentStateManager = this.mActive.get(it.next().mWho);
            if (fragmentStateManager != null) {
                fragmentStateManager.moveToExpectedState();
            }
        }
        for (FragmentStateManager next : this.mActive.values()) {
            if (next != null) {
                next.moveToExpectedState();
                Fragment fragment = next.getFragment();
                if (fragment.mRemoving && !fragment.isInBackStack()) {
                    makeInactive(next);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void removeFragment(Fragment fragment) {
        synchronized (this.mAdded) {
            this.mAdded.remove(fragment);
        }
        fragment.mAdded = false;
    }

    /* access modifiers changed from: package-private */
    public void resetActiveFragments() {
        this.mActive.clear();
    }

    /* access modifiers changed from: package-private */
    public void restoreAddedFragments(List<String> list) {
        this.mAdded.clear();
        if (list != null) {
            for (String next : list) {
                Fragment findActiveFragment = findActiveFragment(next);
                if (findActiveFragment != null) {
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v(TAG, "restoreSaveState: added (" + next + "): " + findActiveFragment);
                    }
                    addFragment(findActiveFragment);
                } else {
                    throw new IllegalStateException("No instantiated fragment for (" + next + ")");
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public ArrayList<FragmentState> saveActiveFragments() {
        ArrayList<FragmentState> arrayList = new ArrayList<>(this.mActive.size());
        for (FragmentStateManager next : this.mActive.values()) {
            if (next != null) {
                Fragment fragment = next.getFragment();
                FragmentState saveState = next.saveState();
                arrayList.add(saveState);
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v(TAG, "Saved state of " + fragment + ": " + saveState.mSavedFragmentState);
                }
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public ArrayList<String> saveAddedFragments() {
        synchronized (this.mAdded) {
            if (this.mAdded.isEmpty()) {
                return null;
            }
            ArrayList<String> arrayList = new ArrayList<>(this.mAdded.size());
            Iterator<Fragment> it = this.mAdded.iterator();
            while (it.hasNext()) {
                Fragment next = it.next();
                arrayList.add(next.mWho);
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v(TAG, "saveAllState: adding fragment (" + next.mWho + "): " + next);
                }
            }
            return arrayList;
        }
    }

    /* access modifiers changed from: package-private */
    public void setNonConfig(FragmentManagerViewModel nonConfig) {
        this.mNonConfig = nonConfig;
    }
}

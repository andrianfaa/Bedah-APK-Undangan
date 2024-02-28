package androidx.activity;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class OnBackPressedCallback {
    private CopyOnWriteArrayList<Cancellable> mCancellables = new CopyOnWriteArrayList<>();
    private boolean mEnabled;

    public OnBackPressedCallback(boolean enabled) {
        this.mEnabled = enabled;
    }

    /* access modifiers changed from: package-private */
    public void addCancellable(Cancellable cancellable) {
        this.mCancellables.add(cancellable);
    }

    public abstract void handleOnBackPressed();

    public final boolean isEnabled() {
        return this.mEnabled;
    }

    public final void remove() {
        Iterator<Cancellable> it = this.mCancellables.iterator();
        while (it.hasNext()) {
            it.next().cancel();
        }
    }

    /* access modifiers changed from: package-private */
    public void removeCancellable(Cancellable cancellable) {
        this.mCancellables.remove(cancellable);
    }

    public final void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }
}

package androidx.lifecycle;

import android.os.Handler;
import androidx.lifecycle.Lifecycle;

public class ServiceLifecycleDispatcher {
    private final Handler mHandler = new Handler();
    private DispatchRunnable mLastDispatchRunnable;
    private final LifecycleRegistry mRegistry;

    static class DispatchRunnable implements Runnable {
        final Lifecycle.Event mEvent;
        private final LifecycleRegistry mRegistry;
        private boolean mWasExecuted = false;

        DispatchRunnable(LifecycleRegistry registry, Lifecycle.Event event) {
            this.mRegistry = registry;
            this.mEvent = event;
        }

        public void run() {
            if (!this.mWasExecuted) {
                this.mRegistry.handleLifecycleEvent(this.mEvent);
                this.mWasExecuted = true;
            }
        }
    }

    public ServiceLifecycleDispatcher(LifecycleOwner provider) {
        this.mRegistry = new LifecycleRegistry(provider);
    }

    private void postDispatchRunnable(Lifecycle.Event event) {
        DispatchRunnable dispatchRunnable = this.mLastDispatchRunnable;
        if (dispatchRunnable != null) {
            dispatchRunnable.run();
        }
        DispatchRunnable dispatchRunnable2 = new DispatchRunnable(this.mRegistry, event);
        this.mLastDispatchRunnable = dispatchRunnable2;
        this.mHandler.postAtFrontOfQueue(dispatchRunnable2);
    }

    public Lifecycle getLifecycle() {
        return this.mRegistry;
    }

    public void onServicePreSuperOnBind() {
        postDispatchRunnable(Lifecycle.Event.ON_START);
    }

    public void onServicePreSuperOnCreate() {
        postDispatchRunnable(Lifecycle.Event.ON_CREATE);
    }

    public void onServicePreSuperOnDestroy() {
        postDispatchRunnable(Lifecycle.Event.ON_STOP);
        postDispatchRunnable(Lifecycle.Event.ON_DESTROY);
    }

    public void onServicePreSuperOnStart() {
        postDispatchRunnable(Lifecycle.Event.ON_START);
    }
}

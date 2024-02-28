package androidx.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import java.util.concurrent.atomic.AtomicBoolean;

class LifecycleDispatcher {
    private static AtomicBoolean sInitialized = new AtomicBoolean(false);

    static class DispatcherActivityCallback extends EmptyActivityLifecycleCallbacks {
        DispatcherActivityCallback() {
        }

        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            ReportFragment.injectIfNeededIn(activity);
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        public void onActivityStopped(Activity activity) {
        }
    }

    private LifecycleDispatcher() {
    }

    static void init(Context context) {
        if (!sInitialized.getAndSet(true)) {
            ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(new DispatcherActivityCallback());
        }
    }
}

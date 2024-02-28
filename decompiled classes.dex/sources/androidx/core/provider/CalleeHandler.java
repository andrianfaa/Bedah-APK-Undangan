package androidx.core.provider;

import android.os.Handler;
import android.os.Looper;

class CalleeHandler {
    private CalleeHandler() {
    }

    static Handler create() {
        return Looper.myLooper() == null ? new Handler(Looper.getMainLooper()) : new Handler();
    }
}

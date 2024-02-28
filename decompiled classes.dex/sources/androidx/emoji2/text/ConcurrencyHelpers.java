package androidx.emoji2.text;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class ConcurrencyHelpers {
    private static final int FONT_LOAD_TIMEOUT_SECONDS = 15;

    static class Handler28Impl {
        private Handler28Impl() {
        }

        public static Handler createAsync(Looper looper) {
            return Handler.createAsync(looper);
        }
    }

    private ConcurrencyHelpers() {
    }

    @Deprecated
    static Executor convertHandlerToExecutor(Handler handler) {
        Objects.requireNonNull(handler);
        return new ConcurrencyHelpers$$ExternalSyntheticLambda1(handler);
    }

    static ThreadPoolExecutor createBackgroundPriorityExecutor(String name) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, 15, TimeUnit.SECONDS, new LinkedBlockingDeque(), new ConcurrencyHelpers$$ExternalSyntheticLambda0(name));
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }

    static /* synthetic */ Thread lambda$createBackgroundPriorityExecutor$0(String name, Runnable runnable) {
        Thread thread = new Thread(runnable, name);
        thread.setPriority(10);
        return thread;
    }

    static Handler mainHandlerAsync() {
        return Build.VERSION.SDK_INT >= 28 ? Handler28Impl.createAsync(Looper.getMainLooper()) : new Handler(Looper.getMainLooper());
    }
}

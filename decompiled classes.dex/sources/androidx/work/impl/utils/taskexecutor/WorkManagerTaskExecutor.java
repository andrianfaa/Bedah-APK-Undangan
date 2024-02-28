package androidx.work.impl.utils.taskexecutor;

import android.os.Handler;
import android.os.Looper;
import androidx.work.impl.utils.SerialExecutor;
import java.util.concurrent.Executor;

public class WorkManagerTaskExecutor implements TaskExecutor {
    private final SerialExecutor mBackgroundExecutor;
    private final Executor mMainThreadExecutor = new Executor() {
        public void execute(Runnable command) {
            WorkManagerTaskExecutor.this.postToMainThread(command);
        }
    };
    private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

    public WorkManagerTaskExecutor(Executor backgroundExecutor) {
        this.mBackgroundExecutor = new SerialExecutor(backgroundExecutor);
    }

    public void executeOnBackgroundThread(Runnable runnable) {
        this.mBackgroundExecutor.execute(runnable);
    }

    public SerialExecutor getBackgroundExecutor() {
        return this.mBackgroundExecutor;
    }

    public Executor getMainThreadExecutor() {
        return this.mMainThreadExecutor;
    }

    public void postToMainThread(Runnable runnable) {
        this.mMainThreadHandler.post(runnable);
    }
}

package androidx.work.impl.utils;

import androidx.work.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import mt.Log1F380D;

/* compiled from: 00E1 */
public class WorkTimer {
    private static final String TAG;
    private final ThreadFactory mBackgroundThreadFactory;
    private final ScheduledExecutorService mExecutorService;
    final Map<String, TimeLimitExceededListener> mListeners = new HashMap();
    final Object mLock = new Object();
    final Map<String, WorkTimerRunnable> mTimerMap = new HashMap();

    public interface TimeLimitExceededListener {
        void onTimeLimitExceeded(String str);
    }

    /* compiled from: 00E0 */
    public static class WorkTimerRunnable implements Runnable {
        static final String TAG = "WrkTimerRunnable";
        private final String mWorkSpecId;
        private final WorkTimer mWorkTimer;

        WorkTimerRunnable(WorkTimer workTimer, String workSpecId) {
            this.mWorkTimer = workTimer;
            this.mWorkSpecId = workSpecId;
        }

        public void run() {
            synchronized (this.mWorkTimer.mLock) {
                if (this.mWorkTimer.mTimerMap.remove(this.mWorkSpecId) != null) {
                    TimeLimitExceededListener remove = this.mWorkTimer.mListeners.remove(this.mWorkSpecId);
                    if (remove != null) {
                        remove.onTimeLimitExceeded(this.mWorkSpecId);
                    }
                } else {
                    Logger logger = Logger.get();
                    String format = String.format("Timer with %s is already marked as complete.", new Object[]{this.mWorkSpecId});
                    Log1F380D.a((Object) format);
                    logger.debug(TAG, format, new Throwable[0]);
                }
            }
        }
    }

    static {
        String tagWithPrefix = Logger.tagWithPrefix("WorkTimer");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public WorkTimer() {
        AnonymousClass1 r0 = new ThreadFactory() {
            private int mThreadsCreated = 0;

            public Thread newThread(Runnable r) {
                Thread newThread = Executors.defaultThreadFactory().newThread(r);
                newThread.setName("WorkManager-WorkTimer-thread-" + this.mThreadsCreated);
                this.mThreadsCreated++;
                return newThread;
            }
        };
        this.mBackgroundThreadFactory = r0;
        this.mExecutorService = Executors.newSingleThreadScheduledExecutor(r0);
    }

    public ScheduledExecutorService getExecutorService() {
        return this.mExecutorService;
    }

    public synchronized Map<String, TimeLimitExceededListener> getListeners() {
        return this.mListeners;
    }

    public synchronized Map<String, WorkTimerRunnable> getTimerMap() {
        return this.mTimerMap;
    }

    public void onDestroy() {
        if (!this.mExecutorService.isShutdown()) {
            this.mExecutorService.shutdownNow();
        }
    }

    public void startTimer(String workSpecId, long processingTimeMillis, TimeLimitExceededListener listener) {
        synchronized (this.mLock) {
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Starting timer for %s", new Object[]{workSpecId});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            stopTimer(workSpecId);
            WorkTimerRunnable workTimerRunnable = new WorkTimerRunnable(this, workSpecId);
            this.mTimerMap.put(workSpecId, workTimerRunnable);
            this.mListeners.put(workSpecId, listener);
            this.mExecutorService.schedule(workTimerRunnable, processingTimeMillis, TimeUnit.MILLISECONDS);
        }
    }

    public void stopTimer(String workSpecId) {
        synchronized (this.mLock) {
            if (this.mTimerMap.remove(workSpecId) != null) {
                Logger logger = Logger.get();
                String str = TAG;
                String format = String.format("Stopping timer for %s", new Object[]{workSpecId});
                Log1F380D.a((Object) format);
                logger.debug(str, format, new Throwable[0]);
                this.mListeners.remove(workSpecId);
            }
        }
    }
}

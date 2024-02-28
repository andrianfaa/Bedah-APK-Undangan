package androidx.work.impl;

import android.content.Context;
import android.os.PowerManager;
import androidx.core.content.ContextCompat;
import androidx.work.Configuration;
import androidx.work.ForegroundInfo;
import androidx.work.Logger;
import androidx.work.WorkerParameters;
import androidx.work.impl.WorkerWrapper;
import androidx.work.impl.foreground.ForegroundProcessor;
import androidx.work.impl.foreground.SystemForegroundDispatcher;
import androidx.work.impl.utils.WakeLocks;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import mt.Log1F380D;

/* compiled from: 00A9 */
public class Processor implements ExecutionListener, ForegroundProcessor {
    private static final String FOREGROUND_WAKELOCK_TAG = "ProcessorForegroundLck";
    private static final String TAG;
    private Context mAppContext;
    private Set<String> mCancelledIds;
    private Configuration mConfiguration;
    private Map<String, WorkerWrapper> mEnqueuedWorkMap = new HashMap();
    private PowerManager.WakeLock mForegroundLock;
    private Map<String, WorkerWrapper> mForegroundWorkMap = new HashMap();
    private final Object mLock;
    private final List<ExecutionListener> mOuterListeners;
    private List<Scheduler> mSchedulers;
    private WorkDatabase mWorkDatabase;
    private TaskExecutor mWorkTaskExecutor;

    private static class FutureListener implements Runnable {
        private ExecutionListener mExecutionListener;
        private ListenableFuture<Boolean> mFuture;
        private String mWorkSpecId;

        FutureListener(ExecutionListener executionListener, String workSpecId, ListenableFuture<Boolean> listenableFuture) {
            this.mExecutionListener = executionListener;
            this.mWorkSpecId = workSpecId;
            this.mFuture = listenableFuture;
        }

        public void run() {
            boolean z;
            try {
                z = ((Boolean) this.mFuture.get()).booleanValue();
            } catch (InterruptedException | ExecutionException e) {
                z = true;
            }
            this.mExecutionListener.onExecuted(this.mWorkSpecId, z);
        }
    }

    static {
        String tagWithPrefix = Logger.tagWithPrefix("Processor");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public Processor(Context appContext, Configuration configuration, TaskExecutor workTaskExecutor, WorkDatabase workDatabase, List<Scheduler> list) {
        this.mAppContext = appContext;
        this.mConfiguration = configuration;
        this.mWorkTaskExecutor = workTaskExecutor;
        this.mWorkDatabase = workDatabase;
        this.mSchedulers = list;
        this.mCancelledIds = new HashSet();
        this.mOuterListeners = new ArrayList();
        this.mForegroundLock = null;
        this.mLock = new Object();
    }

    private void stopForegroundService() {
        synchronized (this.mLock) {
            if (!(!this.mForegroundWorkMap.isEmpty())) {
                try {
                    this.mAppContext.startService(SystemForegroundDispatcher.createStopForegroundIntent(this.mAppContext));
                } catch (Throwable th) {
                    Logger.get().error(TAG, "Unable to stop foreground service", th);
                }
                PowerManager.WakeLock wakeLock = this.mForegroundLock;
                if (wakeLock != null) {
                    wakeLock.release();
                    this.mForegroundLock = null;
                }
            }
        }
    }

    public void addExecutionListener(ExecutionListener executionListener) {
        synchronized (this.mLock) {
            this.mOuterListeners.add(executionListener);
        }
    }

    public boolean hasWork() {
        boolean z;
        synchronized (this.mLock) {
            if (this.mEnqueuedWorkMap.isEmpty()) {
                if (this.mForegroundWorkMap.isEmpty()) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    public boolean isCancelled(String id) {
        boolean contains;
        synchronized (this.mLock) {
            contains = this.mCancelledIds.contains(id);
        }
        return contains;
    }

    public boolean isEnqueued(String workSpecId) {
        boolean z;
        synchronized (this.mLock) {
            if (!this.mEnqueuedWorkMap.containsKey(workSpecId)) {
                if (!this.mForegroundWorkMap.containsKey(workSpecId)) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    public boolean isEnqueuedInForeground(String workSpecId) {
        boolean containsKey;
        synchronized (this.mLock) {
            containsKey = this.mForegroundWorkMap.containsKey(workSpecId);
        }
        return containsKey;
    }

    public void removeExecutionListener(ExecutionListener executionListener) {
        synchronized (this.mLock) {
            this.mOuterListeners.remove(executionListener);
        }
    }

    public boolean startWork(String id) {
        return startWork(id, (WorkerParameters.RuntimeExtras) null);
    }

    public void stopForeground(String workSpecId) {
        synchronized (this.mLock) {
            this.mForegroundWorkMap.remove(workSpecId);
            stopForegroundService();
        }
    }

    private static boolean interrupt(String id, WorkerWrapper wrapper) {
        if (wrapper != null) {
            wrapper.interrupt();
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("WorkerWrapper interrupted for %s", new Object[]{id});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            return true;
        }
        Logger logger2 = Logger.get();
        String str2 = TAG;
        String format2 = String.format("WorkerWrapper could not be found for %s", new Object[]{id});
        Log1F380D.a((Object) format2);
        logger2.debug(str2, format2, new Throwable[0]);
        return false;
    }

    public void onExecuted(String workSpecId, boolean needsReschedule) {
        synchronized (this.mLock) {
            this.mEnqueuedWorkMap.remove(workSpecId);
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("%s %s executed; reschedule = %s", new Object[]{getClass().getSimpleName(), workSpecId, Boolean.valueOf(needsReschedule)});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            for (ExecutionListener onExecuted : this.mOuterListeners) {
                onExecuted.onExecuted(workSpecId, needsReschedule);
            }
        }
    }

    public void startForeground(String workSpecId, ForegroundInfo foregroundInfo) {
        synchronized (this.mLock) {
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Moving WorkSpec (%s) to the foreground", new Object[]{workSpecId});
            Log1F380D.a((Object) format);
            logger.info(str, format, new Throwable[0]);
            WorkerWrapper remove = this.mEnqueuedWorkMap.remove(workSpecId);
            if (remove != null) {
                if (this.mForegroundLock == null) {
                    PowerManager.WakeLock newWakeLock = WakeLocks.newWakeLock(this.mAppContext, FOREGROUND_WAKELOCK_TAG);
                    this.mForegroundLock = newWakeLock;
                    newWakeLock.acquire();
                }
                this.mForegroundWorkMap.put(workSpecId, remove);
                ContextCompat.startForegroundService(this.mAppContext, SystemForegroundDispatcher.createStartForegroundIntent(this.mAppContext, workSpecId, foregroundInfo));
            }
        }
    }

    public boolean startWork(String id, WorkerParameters.RuntimeExtras runtimeExtras) {
        synchronized (this.mLock) {
            if (isEnqueued(id)) {
                Logger logger = Logger.get();
                String str = TAG;
                String format = String.format("Work %s is already enqueued for processing", new Object[]{id});
                Log1F380D.a((Object) format);
                logger.debug(str, format, new Throwable[0]);
                return false;
            }
            WorkerWrapper build = new WorkerWrapper.Builder(this.mAppContext, this.mConfiguration, this.mWorkTaskExecutor, this, this.mWorkDatabase, id).withSchedulers(this.mSchedulers).withRuntimeExtras(runtimeExtras).build();
            ListenableFuture<Boolean> future = build.getFuture();
            future.addListener(new FutureListener(this, id, future), this.mWorkTaskExecutor.getMainThreadExecutor());
            this.mEnqueuedWorkMap.put(id, build);
            this.mWorkTaskExecutor.getBackgroundExecutor().execute(build);
            Logger logger2 = Logger.get();
            String str2 = TAG;
            String format2 = String.format("%s: processing %s", new Object[]{getClass().getSimpleName(), id});
            Log1F380D.a((Object) format2);
            logger2.debug(str2, format2, new Throwable[0]);
            return true;
        }
    }

    public boolean stopAndCancelWork(String id) {
        boolean interrupt;
        synchronized (this.mLock) {
            Logger logger = Logger.get();
            String str = TAG;
            boolean z = true;
            String format = String.format("Processor cancelling %s", new Object[]{id});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            this.mCancelledIds.add(id);
            WorkerWrapper remove = this.mForegroundWorkMap.remove(id);
            if (remove == null) {
                z = false;
            }
            boolean z2 = z;
            if (remove == null) {
                remove = this.mEnqueuedWorkMap.remove(id);
            }
            interrupt = interrupt(id, remove);
            if (z2) {
                stopForegroundService();
            }
        }
        return interrupt;
    }

    public boolean stopForegroundWork(String id) {
        boolean interrupt;
        synchronized (this.mLock) {
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Processor stopping foreground work %s", new Object[]{id});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            interrupt = interrupt(id, this.mForegroundWorkMap.remove(id));
        }
        return interrupt;
    }

    public boolean stopWork(String id) {
        boolean interrupt;
        synchronized (this.mLock) {
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Processor stopping background work %s", new Object[]{id});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            interrupt = interrupt(id, this.mEnqueuedWorkMap.remove(id));
        }
        return interrupt;
    }
}

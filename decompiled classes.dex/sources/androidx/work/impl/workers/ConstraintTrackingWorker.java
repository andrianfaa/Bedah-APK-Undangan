package androidx.work.impl.workers;

import android.content.Context;
import android.text.TextUtils;
import androidx.work.ListenableWorker;
import androidx.work.Logger;
import androidx.work.WorkerParameters;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.constraints.WorkConstraintsCallback;
import androidx.work.impl.constraints.WorkConstraintsTracker;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.utils.futures.SettableFuture;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Collections;
import java.util.List;
import mt.Log1F380D;

/* compiled from: 00E3 */
public class ConstraintTrackingWorker extends ListenableWorker implements WorkConstraintsCallback {
    public static final String ARGUMENT_CLASS_NAME = "androidx.work.impl.workers.ConstraintTrackingWorker.ARGUMENT_CLASS_NAME";
    private static final String TAG;
    volatile boolean mAreConstraintsUnmet = false;
    private ListenableWorker mDelegate;
    SettableFuture<ListenableWorker.Result> mFuture = SettableFuture.create();
    final Object mLock = new Object();
    private WorkerParameters mWorkerParameters;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("ConstraintTrkngWrkr");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public ConstraintTrackingWorker(Context appContext, WorkerParameters workerParams) {
        super(appContext, workerParams);
        this.mWorkerParameters = workerParams;
    }

    public ListenableWorker getDelegate() {
        return this.mDelegate;
    }

    public TaskExecutor getTaskExecutor() {
        return WorkManagerImpl.getInstance(getApplicationContext()).getWorkTaskExecutor();
    }

    public WorkDatabase getWorkDatabase() {
        return WorkManagerImpl.getInstance(getApplicationContext()).getWorkDatabase();
    }

    public boolean isRunInForeground() {
        ListenableWorker listenableWorker = this.mDelegate;
        return listenableWorker != null && listenableWorker.isRunInForeground();
    }

    public void onAllConstraintsMet(List<String> list) {
    }

    public void onStopped() {
        super.onStopped();
        ListenableWorker listenableWorker = this.mDelegate;
        if (listenableWorker != null && !listenableWorker.isStopped()) {
            this.mDelegate.stop();
        }
    }

    /* access modifiers changed from: package-private */
    public void setFutureFailed() {
        this.mFuture.set(ListenableWorker.Result.failure());
    }

    /* access modifiers changed from: package-private */
    public void setFutureRetry() {
        this.mFuture.set(ListenableWorker.Result.retry());
    }

    public ListenableFuture<ListenableWorker.Result> startWork() {
        getBackgroundExecutor().execute(new Runnable() {
            public void run() {
                ConstraintTrackingWorker.this.setupAndRunConstraintTrackingWork();
            }
        });
        return this.mFuture;
    }

    public void onAllConstraintsNotMet(List<String> list) {
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("Constraints changed for %s", new Object[]{list});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        synchronized (this.mLock) {
            this.mAreConstraintsUnmet = true;
        }
    }

    /* access modifiers changed from: package-private */
    public void setupAndRunConstraintTrackingWork() {
        String string = getInputData().getString(ARGUMENT_CLASS_NAME);
        if (TextUtils.isEmpty(string)) {
            Logger.get().error(TAG, "No worker to delegate to.", new Throwable[0]);
            setFutureFailed();
            return;
        }
        ListenableWorker createWorkerWithDefaultFallback = getWorkerFactory().createWorkerWithDefaultFallback(getApplicationContext(), string, this.mWorkerParameters);
        this.mDelegate = createWorkerWithDefaultFallback;
        if (createWorkerWithDefaultFallback == null) {
            Logger.get().debug(TAG, "No worker to delegate to.", new Throwable[0]);
            setFutureFailed();
            return;
        }
        WorkSpec workSpec = getWorkDatabase().workSpecDao().getWorkSpec(getId().toString());
        if (workSpec == null) {
            setFutureFailed();
            return;
        }
        WorkConstraintsTracker workConstraintsTracker = new WorkConstraintsTracker(getApplicationContext(), getTaskExecutor(), this);
        workConstraintsTracker.replace(Collections.singletonList(workSpec));
        if (workConstraintsTracker.areAllConstraintsMet(getId().toString())) {
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Constraints met for delegate %s", new Object[]{string});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            try {
                final ListenableFuture<ListenableWorker.Result> startWork = this.mDelegate.startWork();
                startWork.addListener(new Runnable() {
                    public void run() {
                        synchronized (ConstraintTrackingWorker.this.mLock) {
                            if (ConstraintTrackingWorker.this.mAreConstraintsUnmet) {
                                ConstraintTrackingWorker.this.setFutureRetry();
                            } else {
                                ConstraintTrackingWorker.this.mFuture.setFuture(startWork);
                            }
                        }
                    }
                }, getBackgroundExecutor());
            } catch (Throwable th) {
                Logger logger2 = Logger.get();
                String str2 = TAG;
                String format2 = String.format("Delegated worker %s threw exception in startWork.", new Object[]{string});
                Log1F380D.a((Object) format2);
                logger2.debug(str2, format2, th);
                synchronized (this.mLock) {
                    if (this.mAreConstraintsUnmet) {
                        Logger.get().debug(str2, "Constraints were unmet, Retrying.", new Throwable[0]);
                        setFutureRetry();
                    } else {
                        setFutureFailed();
                    }
                }
            }
        } else {
            Logger logger3 = Logger.get();
            String str3 = TAG;
            String format3 = String.format("Constraints not met for delegate %s. Requesting retry.", new Object[]{string});
            Log1F380D.a((Object) format3);
            logger3.debug(str3, format3, new Throwable[0]);
            setFutureRetry();
        }
    }
}

package androidx.work.impl.utils;

import android.content.Context;
import androidx.core.os.BuildCompat;
import androidx.work.ForegroundInfo;
import androidx.work.ForegroundUpdater;
import androidx.work.ListenableWorker;
import androidx.work.Logger;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.utils.futures.SettableFuture;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import com.google.common.util.concurrent.ListenableFuture;
import mt.Log1F380D;

/* compiled from: 00DC */
public class WorkForegroundRunnable implements Runnable {
    static final String TAG;
    final Context mContext;
    final ForegroundUpdater mForegroundUpdater;
    final SettableFuture<Void> mFuture = SettableFuture.create();
    final TaskExecutor mTaskExecutor;
    final WorkSpec mWorkSpec;
    final ListenableWorker mWorker;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("WorkForegroundRunnable");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public WorkForegroundRunnable(Context context, WorkSpec workSpec, ListenableWorker worker, ForegroundUpdater foregroundUpdater, TaskExecutor taskExecutor) {
        this.mContext = context;
        this.mWorkSpec = workSpec;
        this.mWorker = worker;
        this.mForegroundUpdater = foregroundUpdater;
        this.mTaskExecutor = taskExecutor;
    }

    public ListenableFuture<Void> getFuture() {
        return this.mFuture;
    }

    public void run() {
        if (!this.mWorkSpec.expedited || BuildCompat.isAtLeastS()) {
            this.mFuture.set(null);
            return;
        }
        final SettableFuture create = SettableFuture.create();
        this.mTaskExecutor.getMainThreadExecutor().execute(new Runnable() {
            public void run() {
                create.setFuture(WorkForegroundRunnable.this.mWorker.getForegroundInfoAsync());
            }
        });
        create.addListener(new Runnable() {
            public void run() {
                try {
                    ForegroundInfo foregroundInfo = (ForegroundInfo) create.get();
                    if (foregroundInfo != null) {
                        Logger logger = Logger.get();
                        String str = WorkForegroundRunnable.TAG;
                        String format = String.format("Updating notification for %s", new Object[]{WorkForegroundRunnable.this.mWorkSpec.workerClassName});
                        Log1F380D.a((Object) format);
                        logger.debug(str, format, new Throwable[0]);
                        WorkForegroundRunnable.this.mWorker.setRunInForeground(true);
                        WorkForegroundRunnable.this.mFuture.setFuture(WorkForegroundRunnable.this.mForegroundUpdater.setForegroundAsync(WorkForegroundRunnable.this.mContext, WorkForegroundRunnable.this.mWorker.getId(), foregroundInfo));
                        return;
                    }
                    String format2 = String.format("Worker was marked important (%s) but did not provide ForegroundInfo", new Object[]{WorkForegroundRunnable.this.mWorkSpec.workerClassName});
                    Log1F380D.a((Object) format2);
                    throw new IllegalStateException(format2);
                } catch (Throwable th) {
                    WorkForegroundRunnable.this.mFuture.setException(th);
                }
            }
        }, this.mTaskExecutor.getMainThreadExecutor());
    }
}

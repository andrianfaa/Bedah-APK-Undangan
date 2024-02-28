package androidx.work.impl.utils;

import android.content.Context;
import androidx.work.Data;
import androidx.work.Logger;
import androidx.work.ProgressUpdater;
import androidx.work.WorkInfo;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.model.WorkProgress;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.utils.futures.SettableFuture;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.UUID;
import mt.Log1F380D;

/* compiled from: 00DF */
public class WorkProgressUpdater implements ProgressUpdater {
    static final String TAG;
    final TaskExecutor mTaskExecutor;
    final WorkDatabase mWorkDatabase;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("WorkProgressUpdater");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public WorkProgressUpdater(WorkDatabase workDatabase, TaskExecutor taskExecutor) {
        this.mWorkDatabase = workDatabase;
        this.mTaskExecutor = taskExecutor;
    }

    public ListenableFuture<Void> updateProgress(Context context, final UUID id, final Data data) {
        final SettableFuture create = SettableFuture.create();
        this.mTaskExecutor.executeOnBackgroundThread(new Runnable() {
            public void run() {
                String uuid = id.toString();
                Logger logger = Logger.get();
                String str = WorkProgressUpdater.TAG;
                String format = String.format("Updating progress for %s (%s)", new Object[]{id, data});
                Log1F380D.a((Object) format);
                logger.debug(str, format, new Throwable[0]);
                WorkProgressUpdater.this.mWorkDatabase.beginTransaction();
                try {
                    WorkSpec workSpec = WorkProgressUpdater.this.mWorkDatabase.workSpecDao().getWorkSpec(uuid);
                    if (workSpec != null) {
                        if (workSpec.state == WorkInfo.State.RUNNING) {
                            WorkProgressUpdater.this.mWorkDatabase.workProgressDao().insert(new WorkProgress(uuid, data));
                        } else {
                            Logger logger2 = Logger.get();
                            String str2 = WorkProgressUpdater.TAG;
                            String format2 = String.format("Ignoring setProgressAsync(...). WorkSpec (%s) is not in a RUNNING state.", new Object[]{uuid});
                            Log1F380D.a((Object) format2);
                            logger2.warning(str2, format2, new Throwable[0]);
                        }
                        create.set(null);
                        WorkProgressUpdater.this.mWorkDatabase.setTransactionSuccessful();
                        WorkProgressUpdater.this.mWorkDatabase.endTransaction();
                        return;
                    }
                    throw new IllegalStateException("Calls to setProgressAsync() must complete before a ListenableWorker signals completion of work by returning an instance of Result.");
                } catch (Throwable th) {
                    WorkProgressUpdater.this.mWorkDatabase.endTransaction();
                    throw th;
                }
            }
        });
        return create;
    }
}

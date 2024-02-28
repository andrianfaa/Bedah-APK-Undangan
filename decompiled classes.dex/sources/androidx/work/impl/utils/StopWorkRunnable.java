package androidx.work.impl.utils;

import androidx.work.Logger;
import androidx.work.WorkInfo;
import androidx.work.impl.Processor;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.model.WorkSpecDao;
import mt.Log1F380D;

/* compiled from: 00D9 */
public class StopWorkRunnable implements Runnable {
    private static final String TAG;
    private final boolean mStopInForeground;
    private final WorkManagerImpl mWorkManagerImpl;
    private final String mWorkSpecId;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("StopWorkRunnable");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public StopWorkRunnable(WorkManagerImpl workManagerImpl, String workSpecId, boolean stopInForeground) {
        this.mWorkManagerImpl = workManagerImpl;
        this.mWorkSpecId = workSpecId;
        this.mStopInForeground = stopInForeground;
    }

    public void run() {
        boolean z;
        WorkDatabase workDatabase = this.mWorkManagerImpl.getWorkDatabase();
        Processor processor = this.mWorkManagerImpl.getProcessor();
        WorkSpecDao workSpecDao = workDatabase.workSpecDao();
        workDatabase.beginTransaction();
        try {
            boolean isEnqueuedInForeground = processor.isEnqueuedInForeground(this.mWorkSpecId);
            if (this.mStopInForeground) {
                z = this.mWorkManagerImpl.getProcessor().stopForegroundWork(this.mWorkSpecId);
            } else {
                if (!isEnqueuedInForeground && workSpecDao.getState(this.mWorkSpecId) == WorkInfo.State.RUNNING) {
                    workSpecDao.setState(WorkInfo.State.ENQUEUED, this.mWorkSpecId);
                }
                z = this.mWorkManagerImpl.getProcessor().stopWork(this.mWorkSpecId);
            }
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("StopWorkRunnable for %s; Processor.stopWork = %s", new Object[]{this.mWorkSpecId, Boolean.valueOf(z)});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            workDatabase.setTransactionSuccessful();
        } finally {
            workDatabase.endTransaction();
        }
    }
}

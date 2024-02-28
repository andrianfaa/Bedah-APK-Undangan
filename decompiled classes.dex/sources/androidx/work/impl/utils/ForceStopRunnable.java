package androidx.work.impl.utils;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.ApplicationExitInfo;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteAccessPermException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteTableLockedException;
import android.os.Build;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import androidx.core.os.BuildCompat;
import androidx.work.Configuration;
import androidx.work.InitializationExceptionHandler;
import androidx.work.Logger;
import androidx.work.WorkInfo;
import androidx.work.impl.Schedulers;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkDatabasePathHelper;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.background.systemjob.SystemJobScheduler;
import androidx.work.impl.model.WorkProgressDao;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.model.WorkSpecDao;
import java.util.List;
import java.util.concurrent.TimeUnit;
import mt.Log1F380D;

/* compiled from: 00D6 */
public class ForceStopRunnable implements Runnable {
    static final String ACTION_FORCE_STOP_RESCHEDULE = "ACTION_FORCE_STOP_RESCHEDULE";
    private static final int ALARM_ID = -1;
    private static final long BACKOFF_DURATION_MS = 300;
    static final int MAX_ATTEMPTS = 3;
    private static final String TAG;
    private static final long TEN_YEARS = TimeUnit.DAYS.toMillis(3650);
    private final Context mContext;
    private int mRetryCount = 0;
    private final WorkManagerImpl mWorkManager;

    /* compiled from: 00D5 */
    public static class BroadcastReceiver extends android.content.BroadcastReceiver {
        private static final String TAG;

        static {
            String tagWithPrefix = Logger.tagWithPrefix("ForceStopRunnable$Rcvr");
            Log1F380D.a((Object) tagWithPrefix);
            TAG = tagWithPrefix;
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null && ForceStopRunnable.ACTION_FORCE_STOP_RESCHEDULE.equals(intent.getAction())) {
                Logger.get().verbose(TAG, "Rescheduling alarm that keeps track of force-stops.", new Throwable[0]);
                ForceStopRunnable.setAlarm(context);
            }
        }
    }

    static {
        String tagWithPrefix = Logger.tagWithPrefix("ForceStopRunnable");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public ForceStopRunnable(Context context, WorkManagerImpl workManager) {
        this.mContext = context.getApplicationContext();
        this.mWorkManager = workManager;
    }

    static Intent getIntent(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(context, BroadcastReceiver.class));
        intent.setAction(ACTION_FORCE_STOP_RESCHEDULE);
        return intent;
    }

    private static PendingIntent getPendingIntent(Context context, int flags) {
        return PendingIntent.getBroadcast(context, -1, getIntent(context), flags);
    }

    static void setAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        int i = 134217728;
        if (BuildCompat.isAtLeastS()) {
            i = 134217728 | 33554432;
        }
        PendingIntent pendingIntent = getPendingIntent(context, i);
        long currentTimeMillis = System.currentTimeMillis() + TEN_YEARS;
        if (alarmManager == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(0, currentTimeMillis, pendingIntent);
        } else {
            alarmManager.set(0, currentTimeMillis, pendingIntent);
        }
    }

    public boolean cleanUp() {
        boolean z = false;
        if (Build.VERSION.SDK_INT >= 23) {
            z = SystemJobScheduler.reconcileJobs(this.mContext, this.mWorkManager);
        }
        WorkDatabase workDatabase = this.mWorkManager.getWorkDatabase();
        WorkSpecDao workSpecDao = workDatabase.workSpecDao();
        WorkProgressDao workProgressDao = workDatabase.workProgressDao();
        workDatabase.beginTransaction();
        try {
            List<WorkSpec> runningWork = workSpecDao.getRunningWork();
            boolean z2 = runningWork != null && !runningWork.isEmpty();
            if (z2) {
                for (WorkSpec next : runningWork) {
                    workSpecDao.setState(WorkInfo.State.ENQUEUED, next.id);
                    workSpecDao.markWorkSpecScheduled(next.id, -1);
                }
            }
            workProgressDao.deleteAll();
            workDatabase.setTransactionSuccessful();
            return z2 || z;
        } finally {
            workDatabase.endTransaction();
        }
    }

    public void forceStopRunnable() {
        boolean cleanUp = cleanUp();
        if (shouldRescheduleWorkers()) {
            Logger.get().debug(TAG, "Rescheduling Workers.", new Throwable[0]);
            this.mWorkManager.rescheduleEligibleWork();
            this.mWorkManager.getPreferenceUtils().setNeedsReschedule(false);
        } else if (isForceStopped()) {
            Logger.get().debug(TAG, "Application was force-stopped, rescheduling.", new Throwable[0]);
            this.mWorkManager.rescheduleEligibleWork();
        } else if (cleanUp) {
            Logger.get().debug(TAG, "Found unfinished work, scheduling it.", new Throwable[0]);
            Schedulers.schedule(this.mWorkManager.getConfiguration(), this.mWorkManager.getWorkDatabase(), this.mWorkManager.getSchedulers());
        }
    }

    public boolean isForceStopped() {
        int i = 536870912;
        try {
            if (BuildCompat.isAtLeastS()) {
                i = 536870912 | 33554432;
            }
            PendingIntent pendingIntent = getPendingIntent(this.mContext, i);
            if (Build.VERSION.SDK_INT >= 30) {
                if (pendingIntent != null) {
                    pendingIntent.cancel();
                }
                List historicalProcessExitReasons = ((ActivityManager) this.mContext.getSystemService("activity")).getHistoricalProcessExitReasons((String) null, 0, 0);
                if (historicalProcessExitReasons != null && !historicalProcessExitReasons.isEmpty()) {
                    for (int i2 = 0; i2 < historicalProcessExitReasons.size(); i2++) {
                        if (((ApplicationExitInfo) historicalProcessExitReasons.get(i2)).getReason() == 10) {
                            return true;
                        }
                    }
                }
            } else if (pendingIntent == null) {
                setAlarm(this.mContext);
                return true;
            }
            return false;
        } catch (IllegalArgumentException | SecurityException e) {
            Logger.get().warning(TAG, "Ignoring exception", e);
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean shouldRescheduleWorkers() {
        return this.mWorkManager.getPreferenceUtils().getNeedsReschedule();
    }

    public void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
        }
    }

    public boolean multiProcessChecks() {
        Configuration configuration = this.mWorkManager.getConfiguration();
        if (TextUtils.isEmpty(configuration.getDefaultProcessName())) {
            Logger.get().debug(TAG, "The default process name was not specified.", new Throwable[0]);
            return true;
        }
        boolean isDefaultProcess = ProcessUtils.isDefaultProcess(this.mContext, configuration);
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("Is default app process = %s", new Object[]{Boolean.valueOf(isDefaultProcess)});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        return isDefaultProcess;
    }

    public void run() {
        try {
            if (!multiProcessChecks()) {
                this.mWorkManager.onForceStopRunnableCompleted();
                return;
            }
            while (true) {
                WorkDatabasePathHelper.migrateDatabase(this.mContext);
                Logger.get().debug(TAG, "Performing cleanup operations.", new Throwable[0]);
                forceStopRunnable();
                break;
            }
            this.mWorkManager.onForceStopRunnableCompleted();
        } catch (SQLiteAccessPermException | SQLiteCantOpenDatabaseException | SQLiteConstraintException | SQLiteDatabaseCorruptException | SQLiteDatabaseLockedException | SQLiteTableLockedException e) {
            int i = this.mRetryCount + 1;
            this.mRetryCount = i;
            if (i >= 3) {
                Logger logger = Logger.get();
                String str = TAG;
                logger.error(str, "The file system on the device is in a bad state. WorkManager cannot access the app's internal data store.", e);
                IllegalStateException illegalStateException = new IllegalStateException("The file system on the device is in a bad state. WorkManager cannot access the app's internal data store.", e);
                InitializationExceptionHandler exceptionHandler = this.mWorkManager.getConfiguration().getExceptionHandler();
                if (exceptionHandler != null) {
                    Logger.get().debug(str, "Routing exception to the specified exception handler", illegalStateException);
                    exceptionHandler.handleException(illegalStateException);
                } else {
                    throw illegalStateException;
                }
            } else {
                long j = ((long) i) * BACKOFF_DURATION_MS;
                Logger logger2 = Logger.get();
                String str2 = TAG;
                String format = String.format("Retrying after %s", new Object[]{Long.valueOf(j)});
                Log1F380D.a((Object) format);
                logger2.debug(str2, format, e);
                sleep(((long) this.mRetryCount) * BACKOFF_DURATION_MS);
            }
        } catch (Throwable th) {
            this.mWorkManager.onForceStopRunnableCompleted();
            throw th;
        }
    }
}

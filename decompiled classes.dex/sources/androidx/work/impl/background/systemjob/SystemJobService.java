package androidx.work.impl.background.systemjob;

import android.app.Application;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.PersistableBundle;
import android.text.TextUtils;
import androidx.work.Logger;
import androidx.work.impl.ExecutionListener;
import androidx.work.impl.WorkManagerImpl;
import java.util.HashMap;
import java.util.Map;
import mt.Log1F380D;

/* compiled from: 00C4 */
public class SystemJobService extends JobService implements ExecutionListener {
    private static final String TAG;
    private final Map<String, JobParameters> mJobParameters = new HashMap();
    private WorkManagerImpl mWorkManagerImpl;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("SystemJobService");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    private static String getWorkSpecIdFromJobParameters(JobParameters parameters) {
        try {
            PersistableBundle extras = parameters.getExtras();
            if (extras == null || !extras.containsKey("EXTRA_WORK_SPEC_ID")) {
                return null;
            }
            return extras.getString("EXTRA_WORK_SPEC_ID");
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void onCreate() {
        super.onCreate();
        try {
            WorkManagerImpl instance = WorkManagerImpl.getInstance(getApplicationContext());
            this.mWorkManagerImpl = instance;
            instance.getProcessor().addExecutionListener(this);
        } catch (IllegalStateException e) {
            if (Application.class.equals(getApplication().getClass())) {
                Logger.get().warning(TAG, "Could not find WorkManager instance; this may be because an auto-backup is in progress. Ignoring JobScheduler commands for now. Please make sure that you are initializing WorkManager if you have manually disabled WorkManagerInitializer.", new Throwable[0]);
                return;
            }
            throw new IllegalStateException("WorkManager needs to be initialized via a ContentProvider#onCreate() or an Application#onCreate().");
        }
    }

    public void onDestroy() {
        super.onDestroy();
        WorkManagerImpl workManagerImpl = this.mWorkManagerImpl;
        if (workManagerImpl != null) {
            workManagerImpl.getProcessor().removeExecutionListener(this);
        }
    }

    public void onExecuted(String workSpecId, boolean needsReschedule) {
        JobParameters remove;
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("%s executed on JobScheduler", new Object[]{workSpecId});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        synchronized (this.mJobParameters) {
            remove = this.mJobParameters.remove(workSpecId);
        }
        if (remove != null) {
            jobFinished(remove, needsReschedule);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0075, code lost:
        r2 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x007a, code lost:
        if (android.os.Build.VERSION.SDK_INT < 24) goto L_0x00b0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x007c, code lost:
        r2 = new androidx.work.WorkerParameters.RuntimeExtras();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0086, code lost:
        if (r9.getTriggeredContentUris() == null) goto L_0x0093;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0088, code lost:
        r2.triggeredContentUris = java.util.Arrays.asList(r9.getTriggeredContentUris());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0097, code lost:
        if (r9.getTriggeredContentAuthorities() == null) goto L_0x00a4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0099, code lost:
        r2.triggeredContentAuthorities = java.util.Arrays.asList(r9.getTriggeredContentAuthorities());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00a8, code lost:
        if (android.os.Build.VERSION.SDK_INT < 28) goto L_0x00b0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00aa, code lost:
        r2.network = r9.getNetwork();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00b0, code lost:
        r8.mWorkManagerImpl.startWork(r0, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00b5, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onStartJob(android.app.job.JobParameters r9) {
        /*
            r8 = this;
            androidx.work.impl.WorkManagerImpl r0 = r8.mWorkManagerImpl
            r1 = 1
            r2 = 0
            if (r0 != 0) goto L_0x0017
            androidx.work.Logger r0 = androidx.work.Logger.get()
            java.lang.String r3 = TAG
            java.lang.String r4 = "WorkManager is not initialized; requesting retry."
            java.lang.Throwable[] r5 = new java.lang.Throwable[r2]
            r0.debug(r3, r4, r5)
            r8.jobFinished(r9, r1)
            return r2
        L_0x0017:
            java.lang.String r0 = getWorkSpecIdFromJobParameters(r9)
            mt.Log1F380D.a((java.lang.Object) r0)
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            if (r3 == 0) goto L_0x0032
            androidx.work.Logger r1 = androidx.work.Logger.get()
            java.lang.String r3 = TAG
            java.lang.String r4 = "WorkSpec id not found!"
            java.lang.Throwable[] r5 = new java.lang.Throwable[r2]
            r1.error(r3, r4, r5)
            return r2
        L_0x0032:
            java.util.Map<java.lang.String, android.app.job.JobParameters> r3 = r8.mJobParameters
            monitor-enter(r3)
            java.util.Map<java.lang.String, android.app.job.JobParameters> r4 = r8.mJobParameters     // Catch:{ all -> 0x00b6 }
            boolean r4 = r4.containsKey(r0)     // Catch:{ all -> 0x00b6 }
            if (r4 == 0) goto L_0x0057
            androidx.work.Logger r4 = androidx.work.Logger.get()     // Catch:{ all -> 0x00b6 }
            java.lang.String r5 = TAG     // Catch:{ all -> 0x00b6 }
            java.lang.String r6 = "Job is already being executed by SystemJobService: %s"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x00b6 }
            r1[r2] = r0     // Catch:{ all -> 0x00b6 }
            java.lang.String r1 = java.lang.String.format(r6, r1)     // Catch:{ all -> 0x00b6 }
            mt.Log1F380D.a((java.lang.Object) r1)
            java.lang.Throwable[] r6 = new java.lang.Throwable[r2]     // Catch:{ all -> 0x00b6 }
            r4.debug(r5, r1, r6)     // Catch:{ all -> 0x00b6 }
            monitor-exit(r3)     // Catch:{ all -> 0x00b6 }
            return r2
        L_0x0057:
            androidx.work.Logger r4 = androidx.work.Logger.get()     // Catch:{ all -> 0x00b6 }
            java.lang.String r5 = TAG     // Catch:{ all -> 0x00b6 }
            java.lang.String r6 = "onStartJob for %s"
            java.lang.Object[] r7 = new java.lang.Object[r1]     // Catch:{ all -> 0x00b6 }
            r7[r2] = r0     // Catch:{ all -> 0x00b6 }
            java.lang.String r6 = java.lang.String.format(r6, r7)     // Catch:{ all -> 0x00b6 }
            mt.Log1F380D.a((java.lang.Object) r6)
            java.lang.Throwable[] r2 = new java.lang.Throwable[r2]     // Catch:{ all -> 0x00b6 }
            r4.debug(r5, r6, r2)     // Catch:{ all -> 0x00b6 }
            java.util.Map<java.lang.String, android.app.job.JobParameters> r2 = r8.mJobParameters     // Catch:{ all -> 0x00b6 }
            r2.put(r0, r9)     // Catch:{ all -> 0x00b6 }
            monitor-exit(r3)     // Catch:{ all -> 0x00b6 }
            r2 = 0
            int r3 = android.os.Build.VERSION.SDK_INT
            r4 = 24
            if (r3 < r4) goto L_0x00b0
            androidx.work.WorkerParameters$RuntimeExtras r3 = new androidx.work.WorkerParameters$RuntimeExtras
            r3.<init>()
            r2 = r3
            android.net.Uri[] r3 = r9.getTriggeredContentUris()
            if (r3 == 0) goto L_0x0093
            android.net.Uri[] r3 = r9.getTriggeredContentUris()
            java.util.List r3 = java.util.Arrays.asList(r3)
            r2.triggeredContentUris = r3
        L_0x0093:
            java.lang.String[] r3 = r9.getTriggeredContentAuthorities()
            if (r3 == 0) goto L_0x00a4
            java.lang.String[] r3 = r9.getTriggeredContentAuthorities()
            java.util.List r3 = java.util.Arrays.asList(r3)
            r2.triggeredContentAuthorities = r3
        L_0x00a4:
            int r3 = android.os.Build.VERSION.SDK_INT
            r4 = 28
            if (r3 < r4) goto L_0x00b0
            android.net.Network r3 = r9.getNetwork()
            r2.network = r3
        L_0x00b0:
            androidx.work.impl.WorkManagerImpl r3 = r8.mWorkManagerImpl
            r3.startWork(r0, r2)
            return r1
        L_0x00b6:
            r1 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x00b6 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.work.impl.background.systemjob.SystemJobService.onStartJob(android.app.job.JobParameters):boolean");
    }

    public boolean onStopJob(JobParameters params) {
        if (this.mWorkManagerImpl == null) {
            Logger.get().debug(TAG, "WorkManager is not initialized; requesting retry.", new Throwable[0]);
            return true;
        }
        String workSpecIdFromJobParameters = getWorkSpecIdFromJobParameters(params);
        Log1F380D.a((Object) workSpecIdFromJobParameters);
        if (TextUtils.isEmpty(workSpecIdFromJobParameters)) {
            Logger.get().error(TAG, "WorkSpec id not found!", new Throwable[0]);
            return false;
        }
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("onStopJob for %s", new Object[]{workSpecIdFromJobParameters});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        synchronized (this.mJobParameters) {
            this.mJobParameters.remove(workSpecIdFromJobParameters);
        }
        this.mWorkManagerImpl.stopWork(workSpecIdFromJobParameters);
        return true ^ this.mWorkManagerImpl.getProcessor().isCancelled(workSpecIdFromJobParameters);
    }
}

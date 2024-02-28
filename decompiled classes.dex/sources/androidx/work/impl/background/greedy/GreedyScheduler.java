package androidx.work.impl.background.greedy;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import androidx.work.Configuration;
import androidx.work.Logger;
import androidx.work.WorkInfo;
import androidx.work.impl.ExecutionListener;
import androidx.work.impl.Scheduler;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.constraints.WorkConstraintsCallback;
import androidx.work.impl.constraints.WorkConstraintsTracker;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.utils.ProcessUtils;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mt.Log1F380D;

/* compiled from: 00B5 */
public class GreedyScheduler implements Scheduler, WorkConstraintsCallback, ExecutionListener {
    private static final String TAG;
    private final Set<WorkSpec> mConstrainedWorkSpecs = new HashSet();
    private final Context mContext;
    private DelayedWorkTracker mDelayedWorkTracker;
    Boolean mInDefaultProcess;
    private final Object mLock;
    private boolean mRegisteredExecutionListener;
    private final WorkConstraintsTracker mWorkConstraintsTracker;
    private final WorkManagerImpl mWorkManagerImpl;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("GreedyScheduler");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public GreedyScheduler(Context context, Configuration configuration, TaskExecutor taskExecutor, WorkManagerImpl workManagerImpl) {
        this.mContext = context;
        this.mWorkManagerImpl = workManagerImpl;
        this.mWorkConstraintsTracker = new WorkConstraintsTracker(context, taskExecutor, this);
        this.mDelayedWorkTracker = new DelayedWorkTracker(this, configuration.getRunnableScheduler());
        this.mLock = new Object();
    }

    public GreedyScheduler(Context context, WorkManagerImpl workManagerImpl, WorkConstraintsTracker workConstraintsTracker) {
        this.mContext = context;
        this.mWorkManagerImpl = workManagerImpl;
        this.mWorkConstraintsTracker = workConstraintsTracker;
        this.mLock = new Object();
    }

    private void checkDefaultProcess() {
        this.mInDefaultProcess = Boolean.valueOf(ProcessUtils.isDefaultProcess(this.mContext, this.mWorkManagerImpl.getConfiguration()));
    }

    private void registerExecutionListenerIfNeeded() {
        if (!this.mRegisteredExecutionListener) {
            this.mWorkManagerImpl.getProcessor().addExecutionListener(this);
            this.mRegisteredExecutionListener = true;
        }
    }

    public boolean hasLimitedSchedulingSlots() {
        return false;
    }

    public void onExecuted(String workSpecId, boolean needsReschedule) {
        removeConstraintTrackingFor(workSpecId);
    }

    public void setDelayedWorkTracker(DelayedWorkTracker delayedWorkTracker) {
        this.mDelayedWorkTracker = delayedWorkTracker;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        r1.debug(r3, r4, new java.lang.Throwable[0]);
        r7.mConstrainedWorkSpecs.remove(r2);
        r7.mWorkConstraintsTracker.replace(r7.mConstrainedWorkSpecs);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001d, code lost:
        r1 = androidx.work.Logger.get();
        r3 = TAG;
        r4 = java.lang.String.format("Stopping tracking for %s", new java.lang.Object[]{r8});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002f, code lost:
        mt.Log1F380D.a((java.lang.Object) r4);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void removeConstraintTrackingFor(java.lang.String r8) {
        /*
            r7 = this;
            java.lang.Object r0 = r7.mLock
            monitor-enter(r0)
            java.util.Set<androidx.work.impl.model.WorkSpec> r1 = r7.mConstrainedWorkSpecs     // Catch:{ all -> 0x0047 }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x0047 }
        L_0x0009:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x0047 }
            if (r2 == 0) goto L_0x0045
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x0047 }
            androidx.work.impl.model.WorkSpec r2 = (androidx.work.impl.model.WorkSpec) r2     // Catch:{ all -> 0x0047 }
            java.lang.String r3 = r2.id     // Catch:{ all -> 0x0047 }
            boolean r3 = r3.equals(r8)     // Catch:{ all -> 0x0047 }
            if (r3 == 0) goto L_0x0044
            androidx.work.Logger r1 = androidx.work.Logger.get()     // Catch:{ all -> 0x0047 }
            java.lang.String r3 = TAG     // Catch:{ all -> 0x0047 }
            java.lang.String r4 = "Stopping tracking for %s"
            r5 = 1
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x0047 }
            r6 = 0
            r5[r6] = r8     // Catch:{ all -> 0x0047 }
            java.lang.String r4 = java.lang.String.format(r4, r5)     // Catch:{ all -> 0x0047 }
            mt.Log1F380D.a((java.lang.Object) r4)
            java.lang.Throwable[] r5 = new java.lang.Throwable[r6]     // Catch:{ all -> 0x0047 }
            r1.debug(r3, r4, r5)     // Catch:{ all -> 0x0047 }
            java.util.Set<androidx.work.impl.model.WorkSpec> r1 = r7.mConstrainedWorkSpecs     // Catch:{ all -> 0x0047 }
            r1.remove(r2)     // Catch:{ all -> 0x0047 }
            androidx.work.impl.constraints.WorkConstraintsTracker r1 = r7.mWorkConstraintsTracker     // Catch:{ all -> 0x0047 }
            java.util.Set<androidx.work.impl.model.WorkSpec> r3 = r7.mConstrainedWorkSpecs     // Catch:{ all -> 0x0047 }
            r1.replace(r3)     // Catch:{ all -> 0x0047 }
            goto L_0x0045
        L_0x0044:
            goto L_0x0009
        L_0x0045:
            monitor-exit(r0)     // Catch:{ all -> 0x0047 }
            return
        L_0x0047:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0047 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.work.impl.background.greedy.GreedyScheduler.removeConstraintTrackingFor(java.lang.String):void");
    }

    public void cancel(String workSpecId) {
        if (this.mInDefaultProcess == null) {
            checkDefaultProcess();
        }
        if (!this.mInDefaultProcess.booleanValue()) {
            Logger.get().info(TAG, "Ignoring schedule request in non-main process", new Throwable[0]);
            return;
        }
        registerExecutionListenerIfNeeded();
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("Cancelling work ID %s", new Object[]{workSpecId});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        DelayedWorkTracker delayedWorkTracker = this.mDelayedWorkTracker;
        if (delayedWorkTracker != null) {
            delayedWorkTracker.unschedule(workSpecId);
        }
        this.mWorkManagerImpl.stopWork(workSpecId);
    }

    public void onAllConstraintsMet(List<String> list) {
        for (String next : list) {
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Constraints met: Scheduling work ID %s", new Object[]{next});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            this.mWorkManagerImpl.startWork(next);
        }
    }

    public void onAllConstraintsNotMet(List<String> list) {
        for (String next : list) {
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Constraints not met: Cancelling work ID %s", new Object[]{next});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            this.mWorkManagerImpl.stopWork(next);
        }
    }

    public void schedule(WorkSpec... workSpecs) {
        int i;
        WorkSpec[] workSpecArr = workSpecs;
        if (this.mInDefaultProcess == null) {
            checkDefaultProcess();
        }
        if (!this.mInDefaultProcess.booleanValue()) {
            Logger.get().info(TAG, "Ignoring schedule request in a secondary process", new Throwable[0]);
            return;
        }
        registerExecutionListenerIfNeeded();
        HashSet hashSet = new HashSet();
        HashSet hashSet2 = new HashSet();
        int length = workSpecArr.length;
        int i2 = 0;
        while (i2 < length) {
            WorkSpec workSpec = workSpecArr[i2];
            long calculateNextRunTime = workSpec.calculateNextRunTime();
            long currentTimeMillis = System.currentTimeMillis();
            if (workSpec.state != WorkInfo.State.ENQUEUED) {
                i = length;
            } else if (currentTimeMillis < calculateNextRunTime) {
                DelayedWorkTracker delayedWorkTracker = this.mDelayedWorkTracker;
                if (delayedWorkTracker != null) {
                    delayedWorkTracker.schedule(workSpec);
                    i = length;
                } else {
                    i = length;
                }
            } else if (!workSpec.hasConstraints()) {
                Logger logger = Logger.get();
                String str = TAG;
                i = length;
                String format = String.format("Starting work for %s", new Object[]{workSpec.id});
                Log1F380D.a((Object) format);
                logger.debug(str, format, new Throwable[0]);
                this.mWorkManagerImpl.startWork(workSpec.id);
            } else if (Build.VERSION.SDK_INT >= 23 && workSpec.constraints.requiresDeviceIdle()) {
                Logger logger2 = Logger.get();
                String str2 = TAG;
                String format2 = String.format("Ignoring WorkSpec %s, Requires device idle.", new Object[]{workSpec});
                Log1F380D.a((Object) format2);
                logger2.debug(str2, format2, new Throwable[0]);
                i = length;
            } else if (Build.VERSION.SDK_INT < 24 || !workSpec.constraints.hasContentUriTriggers()) {
                hashSet.add(workSpec);
                hashSet2.add(workSpec.id);
                i = length;
            } else {
                Logger logger3 = Logger.get();
                String str3 = TAG;
                String format3 = String.format("Ignoring WorkSpec %s, Requires ContentUri triggers.", new Object[]{workSpec});
                Log1F380D.a((Object) format3);
                logger3.debug(str3, format3, new Throwable[0]);
                i = length;
            }
            i2++;
            length = i;
        }
        synchronized (this.mLock) {
            if (!hashSet.isEmpty()) {
                Logger logger4 = Logger.get();
                String str4 = TAG;
                Object[] objArr = new Object[1];
                String join = TextUtils.join(",", hashSet2);
                Log1F380D.a((Object) join);
                objArr[0] = join;
                String format4 = String.format("Starting tracking for [%s]", objArr);
                Log1F380D.a((Object) format4);
                logger4.debug(str4, format4, new Throwable[0]);
                this.mConstrainedWorkSpecs.addAll(hashSet);
                this.mWorkConstraintsTracker.replace(this.mConstrainedWorkSpecs);
            }
        }
    }
}

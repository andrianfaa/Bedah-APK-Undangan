package androidx.work.impl.background.systemalarm;

import android.content.Context;
import android.os.PowerManager;
import androidx.work.Logger;
import androidx.work.impl.ExecutionListener;
import androidx.work.impl.background.systemalarm.SystemAlarmDispatcher;
import androidx.work.impl.constraints.WorkConstraintsCallback;
import androidx.work.impl.constraints.WorkConstraintsTracker;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.utils.WakeLocks;
import androidx.work.impl.utils.WorkTimer;
import java.util.Collections;
import java.util.List;
import mt.Log1F380D;

/* compiled from: 00BC */
public class DelayMetCommandHandler implements WorkConstraintsCallback, ExecutionListener, WorkTimer.TimeLimitExceededListener {
    private static final int STATE_INITIAL = 0;
    private static final int STATE_START_REQUESTED = 1;
    private static final int STATE_STOP_REQUESTED = 2;
    private static final String TAG;
    private final Context mContext;
    private int mCurrentState = 0;
    private final SystemAlarmDispatcher mDispatcher;
    private boolean mHasConstraints = false;
    private final Object mLock = new Object();
    private final int mStartId;
    private PowerManager.WakeLock mWakeLock;
    private final WorkConstraintsTracker mWorkConstraintsTracker;
    private final String mWorkSpecId;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("DelayMetCommandHandler");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    DelayMetCommandHandler(Context context, int startId, String workSpecId, SystemAlarmDispatcher dispatcher) {
        this.mContext = context;
        this.mStartId = startId;
        this.mDispatcher = dispatcher;
        this.mWorkSpecId = workSpecId;
        this.mWorkConstraintsTracker = new WorkConstraintsTracker(context, dispatcher.getTaskExecutor(), this);
    }

    public void onAllConstraintsNotMet(List<String> list) {
        stopWork();
    }

    private void cleanUp() {
        synchronized (this.mLock) {
            this.mWorkConstraintsTracker.reset();
            this.mDispatcher.getWorkTimer().stopTimer(this.mWorkSpecId);
            PowerManager.WakeLock wakeLock = this.mWakeLock;
            if (wakeLock != null && wakeLock.isHeld()) {
                Logger logger = Logger.get();
                String str = TAG;
                String format = String.format("Releasing wakelock %s for WorkSpec %s", new Object[]{this.mWakeLock, this.mWorkSpecId});
                Log1F380D.a((Object) format);
                logger.debug(str, format, new Throwable[0]);
                this.mWakeLock.release();
            }
        }
    }

    private void stopWork() {
        synchronized (this.mLock) {
            if (this.mCurrentState < 2) {
                this.mCurrentState = 2;
                Logger logger = Logger.get();
                String str = TAG;
                String format = String.format("Stopping work for WorkSpec %s", new Object[]{this.mWorkSpecId});
                Log1F380D.a((Object) format);
                logger.debug(str, format, new Throwable[0]);
                this.mDispatcher.postOnMainThread(new SystemAlarmDispatcher.AddRunnable(this.mDispatcher, CommandHandler.createStopWorkIntent(this.mContext, this.mWorkSpecId), this.mStartId));
                if (this.mDispatcher.getProcessor().isEnqueued(this.mWorkSpecId)) {
                    Logger logger2 = Logger.get();
                    String format2 = String.format("WorkSpec %s needs to be rescheduled", new Object[]{this.mWorkSpecId});
                    Log1F380D.a((Object) format2);
                    logger2.debug(str, format2, new Throwable[0]);
                    this.mDispatcher.postOnMainThread(new SystemAlarmDispatcher.AddRunnable(this.mDispatcher, CommandHandler.createScheduleWorkIntent(this.mContext, this.mWorkSpecId), this.mStartId));
                } else {
                    Logger logger3 = Logger.get();
                    String format3 = String.format("Processor does not have WorkSpec %s. No need to reschedule ", new Object[]{this.mWorkSpecId});
                    Log1F380D.a((Object) format3);
                    logger3.debug(str, format3, new Throwable[0]);
                }
            } else {
                Logger logger4 = Logger.get();
                String str2 = TAG;
                String format4 = String.format("Already stopped work for %s", new Object[]{this.mWorkSpecId});
                Log1F380D.a((Object) format4);
                logger4.debug(str2, format4, new Throwable[0]);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void handleProcessWork() {
        Context context = this.mContext;
        String format = String.format("%s (%s)", new Object[]{this.mWorkSpecId, Integer.valueOf(this.mStartId)});
        Log1F380D.a((Object) format);
        this.mWakeLock = WakeLocks.newWakeLock(context, format);
        Logger logger = Logger.get();
        String str = TAG;
        String format2 = String.format("Acquiring wakelock %s for WorkSpec %s", new Object[]{this.mWakeLock, this.mWorkSpecId});
        Log1F380D.a((Object) format2);
        logger.debug(str, format2, new Throwable[0]);
        this.mWakeLock.acquire();
        WorkSpec workSpec = this.mDispatcher.getWorkManager().getWorkDatabase().workSpecDao().getWorkSpec(this.mWorkSpecId);
        if (workSpec == null) {
            stopWork();
            return;
        }
        boolean hasConstraints = workSpec.hasConstraints();
        this.mHasConstraints = hasConstraints;
        if (!hasConstraints) {
            Logger logger2 = Logger.get();
            String format3 = String.format("No constraints for %s", new Object[]{this.mWorkSpecId});
            Log1F380D.a((Object) format3);
            logger2.debug(str, format3, new Throwable[0]);
            onAllConstraintsMet(Collections.singletonList(this.mWorkSpecId));
            return;
        }
        this.mWorkConstraintsTracker.replace(Collections.singletonList(workSpec));
    }

    public void onAllConstraintsMet(List<String> list) {
        if (list.contains(this.mWorkSpecId)) {
            synchronized (this.mLock) {
                if (this.mCurrentState == 0) {
                    this.mCurrentState = 1;
                    Logger logger = Logger.get();
                    String str = TAG;
                    String format = String.format("onAllConstraintsMet for %s", new Object[]{this.mWorkSpecId});
                    Log1F380D.a((Object) format);
                    logger.debug(str, format, new Throwable[0]);
                    if (this.mDispatcher.getProcessor().startWork(this.mWorkSpecId)) {
                        this.mDispatcher.getWorkTimer().startTimer(this.mWorkSpecId, 600000, this);
                    } else {
                        cleanUp();
                    }
                } else {
                    Logger logger2 = Logger.get();
                    String str2 = TAG;
                    String format2 = String.format("Already started work for %s", new Object[]{this.mWorkSpecId});
                    Log1F380D.a((Object) format2);
                    logger2.debug(str2, format2, new Throwable[0]);
                }
            }
        }
    }

    public void onExecuted(String workSpecId, boolean needsReschedule) {
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("onExecuted %s, %s", new Object[]{workSpecId, Boolean.valueOf(needsReschedule)});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        cleanUp();
        if (needsReschedule) {
            this.mDispatcher.postOnMainThread(new SystemAlarmDispatcher.AddRunnable(this.mDispatcher, CommandHandler.createScheduleWorkIntent(this.mContext, this.mWorkSpecId), this.mStartId));
        }
        if (this.mHasConstraints) {
            this.mDispatcher.postOnMainThread(new SystemAlarmDispatcher.AddRunnable(this.mDispatcher, CommandHandler.createConstraintsChangedIntent(this.mContext), this.mStartId));
        }
    }

    public void onTimeLimitExceeded(String workSpecId) {
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("Exceeded time limits on execution for %s", new Object[]{workSpecId});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        stopWork();
    }
}

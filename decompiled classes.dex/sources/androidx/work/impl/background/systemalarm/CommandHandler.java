package androidx.work.impl.background.systemalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.work.Logger;
import androidx.work.impl.ExecutionListener;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.background.systemalarm.SystemAlarmDispatcher;
import androidx.work.impl.model.WorkSpec;
import java.util.HashMap;
import java.util.Map;
import mt.Log1F380D;

/* compiled from: 00B7 */
public class CommandHandler implements ExecutionListener {
    static final String ACTION_CONSTRAINTS_CHANGED = "ACTION_CONSTRAINTS_CHANGED";
    static final String ACTION_DELAY_MET = "ACTION_DELAY_MET";
    static final String ACTION_EXECUTION_COMPLETED = "ACTION_EXECUTION_COMPLETED";
    static final String ACTION_RESCHEDULE = "ACTION_RESCHEDULE";
    static final String ACTION_SCHEDULE_WORK = "ACTION_SCHEDULE_WORK";
    static final String ACTION_STOP_WORK = "ACTION_STOP_WORK";
    private static final String KEY_NEEDS_RESCHEDULE = "KEY_NEEDS_RESCHEDULE";
    private static final String KEY_WORKSPEC_ID = "KEY_WORKSPEC_ID";
    private static final String TAG;
    static final long WORK_PROCESSING_TIME_IN_MS = 600000;
    private final Context mContext;
    private final Object mLock = new Object();
    private final Map<String, ExecutionListener> mPendingDelayMet = new HashMap();

    static {
        String tagWithPrefix = Logger.tagWithPrefix("CommandHandler");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    CommandHandler(Context context) {
        this.mContext = context;
    }

    static Intent createConstraintsChangedIntent(Context context) {
        Intent intent = new Intent(context, SystemAlarmService.class);
        intent.setAction(ACTION_CONSTRAINTS_CHANGED);
        return intent;
    }

    static Intent createDelayMetIntent(Context context, String workSpecId) {
        Intent intent = new Intent(context, SystemAlarmService.class);
        intent.setAction(ACTION_DELAY_MET);
        intent.putExtra(KEY_WORKSPEC_ID, workSpecId);
        return intent;
    }

    static Intent createExecutionCompletedIntent(Context context, String workSpecId, boolean needsReschedule) {
        Intent intent = new Intent(context, SystemAlarmService.class);
        intent.setAction(ACTION_EXECUTION_COMPLETED);
        intent.putExtra(KEY_WORKSPEC_ID, workSpecId);
        intent.putExtra(KEY_NEEDS_RESCHEDULE, needsReschedule);
        return intent;
    }

    static Intent createRescheduleIntent(Context context) {
        Intent intent = new Intent(context, SystemAlarmService.class);
        intent.setAction(ACTION_RESCHEDULE);
        return intent;
    }

    static Intent createScheduleWorkIntent(Context context, String workSpecId) {
        Intent intent = new Intent(context, SystemAlarmService.class);
        intent.setAction(ACTION_SCHEDULE_WORK);
        intent.putExtra(KEY_WORKSPEC_ID, workSpecId);
        return intent;
    }

    static Intent createStopWorkIntent(Context context, String workSpecId) {
        Intent intent = new Intent(context, SystemAlarmService.class);
        intent.setAction(ACTION_STOP_WORK);
        intent.putExtra(KEY_WORKSPEC_ID, workSpecId);
        return intent;
    }

    private static boolean hasKeys(Bundle bundle, String... keys) {
        if (bundle == null || bundle.isEmpty()) {
            return false;
        }
        for (String str : keys) {
            if (bundle.get(str) == null) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean hasPendingCommands() {
        boolean z;
        synchronized (this.mLock) {
            z = !this.mPendingDelayMet.isEmpty();
        }
        return z;
    }

    public void onExecuted(String workSpecId, boolean needsReschedule) {
        synchronized (this.mLock) {
            ExecutionListener remove = this.mPendingDelayMet.remove(workSpecId);
            if (remove != null) {
                remove.onExecuted(workSpecId, needsReschedule);
            }
        }
    }

    private void handleConstraintsChanged(Intent intent, int startId, SystemAlarmDispatcher dispatcher) {
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("Handling constraints changed %s", new Object[]{intent});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        new ConstraintsCommandHandler(this.mContext, startId, dispatcher).handleConstraintsChanged();
    }

    private void handleDelayMet(Intent intent, int startId, SystemAlarmDispatcher dispatcher) {
        Bundle extras = intent.getExtras();
        synchronized (this.mLock) {
            String string = extras.getString(KEY_WORKSPEC_ID);
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Handing delay met for %s", new Object[]{string});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            if (!this.mPendingDelayMet.containsKey(string)) {
                DelayMetCommandHandler delayMetCommandHandler = new DelayMetCommandHandler(this.mContext, startId, string, dispatcher);
                this.mPendingDelayMet.put(string, delayMetCommandHandler);
                delayMetCommandHandler.handleProcessWork();
            } else {
                Logger logger2 = Logger.get();
                String format2 = String.format("WorkSpec %s is already being handled for ACTION_DELAY_MET", new Object[]{string});
                Log1F380D.a((Object) format2);
                logger2.debug(str, format2, new Throwable[0]);
            }
        }
    }

    private void handleExecutionCompleted(Intent intent, int startId) {
        Bundle extras = intent.getExtras();
        String string = extras.getString(KEY_WORKSPEC_ID);
        boolean z = extras.getBoolean(KEY_NEEDS_RESCHEDULE);
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("Handling onExecutionCompleted %s, %s", new Object[]{intent, Integer.valueOf(startId)});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        onExecuted(string, z);
    }

    private void handleReschedule(Intent intent, int startId, SystemAlarmDispatcher dispatcher) {
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("Handling reschedule %s, %s", new Object[]{intent, Integer.valueOf(startId)});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        dispatcher.getWorkManager().rescheduleEligibleWork();
    }

    private void handleScheduleWorkIntent(Intent intent, int startId, SystemAlarmDispatcher dispatcher) {
        SystemAlarmDispatcher systemAlarmDispatcher = dispatcher;
        String string = intent.getExtras().getString(KEY_WORKSPEC_ID);
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("Handling schedule work for %s", new Object[]{string});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        WorkDatabase workDatabase = dispatcher.getWorkManager().getWorkDatabase();
        workDatabase.beginTransaction();
        try {
            WorkSpec workSpec = workDatabase.workSpecDao().getWorkSpec(string);
            if (workSpec == null) {
                Logger.get().warning(str, "Skipping scheduling " + string + " because it's no longer in the DB", new Throwable[0]);
                workDatabase.endTransaction();
            } else if (workSpec.state.isFinished()) {
                Logger.get().warning(str, "Skipping scheduling " + string + "because it is finished.", new Throwable[0]);
                workDatabase.endTransaction();
            } else {
                long calculateNextRunTime = workSpec.calculateNextRunTime();
                if (!workSpec.hasConstraints()) {
                    Logger logger2 = Logger.get();
                    String format2 = String.format("Setting up Alarms for %s at %s", new Object[]{string, Long.valueOf(calculateNextRunTime)});
                    Log1F380D.a((Object) format2);
                    logger2.debug(str, format2, new Throwable[0]);
                    Alarms.setAlarm(this.mContext, dispatcher.getWorkManager(), string, calculateNextRunTime);
                    int i = startId;
                } else {
                    Logger logger3 = Logger.get();
                    String format3 = String.format("Opportunistically setting an alarm for %s at %s", new Object[]{string, Long.valueOf(calculateNextRunTime)});
                    Log1F380D.a((Object) format3);
                    logger3.debug(str, format3, new Throwable[0]);
                    Alarms.setAlarm(this.mContext, dispatcher.getWorkManager(), string, calculateNextRunTime);
                    try {
                        systemAlarmDispatcher.postOnMainThread(new SystemAlarmDispatcher.AddRunnable(systemAlarmDispatcher, createConstraintsChangedIntent(this.mContext), startId));
                    } catch (Throwable th) {
                        th = th;
                        workDatabase.endTransaction();
                        throw th;
                    }
                }
                workDatabase.setTransactionSuccessful();
                workDatabase.endTransaction();
            }
        } catch (Throwable th2) {
            th = th2;
            int i2 = startId;
            workDatabase.endTransaction();
            throw th;
        }
    }

    private void handleStopWork(Intent intent, SystemAlarmDispatcher dispatcher) {
        String string = intent.getExtras().getString(KEY_WORKSPEC_ID);
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("Handing stopWork work for %s", new Object[]{string});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        dispatcher.getWorkManager().stopWork(string);
        Alarms.cancelAlarm(this.mContext, dispatcher.getWorkManager(), string);
        dispatcher.onExecuted(string, false);
    }

    /* access modifiers changed from: package-private */
    public void onHandleIntent(Intent intent, int startId, SystemAlarmDispatcher dispatcher) {
        String action = intent.getAction();
        if (ACTION_CONSTRAINTS_CHANGED.equals(action)) {
            handleConstraintsChanged(intent, startId, dispatcher);
        } else if (ACTION_RESCHEDULE.equals(action)) {
            handleReschedule(intent, startId, dispatcher);
        } else if (!hasKeys(intent.getExtras(), KEY_WORKSPEC_ID)) {
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Invalid request for %s, requires %s.", new Object[]{action, KEY_WORKSPEC_ID});
            Log1F380D.a((Object) format);
            logger.error(str, format, new Throwable[0]);
        } else if (ACTION_SCHEDULE_WORK.equals(action)) {
            handleScheduleWorkIntent(intent, startId, dispatcher);
        } else if (ACTION_DELAY_MET.equals(action)) {
            handleDelayMet(intent, startId, dispatcher);
        } else if (ACTION_STOP_WORK.equals(action)) {
            handleStopWork(intent, dispatcher);
        } else if (ACTION_EXECUTION_COMPLETED.equals(action)) {
            handleExecutionCompleted(intent, startId);
        } else {
            Logger logger2 = Logger.get();
            String str2 = TAG;
            String format2 = String.format("Ignoring intent %s", new Object[]{intent});
            Log1F380D.a((Object) format2);
            logger2.warning(str2, format2, new Throwable[0]);
        }
    }
}

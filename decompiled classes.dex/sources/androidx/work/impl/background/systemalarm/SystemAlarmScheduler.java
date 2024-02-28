package androidx.work.impl.background.systemalarm;

import android.content.Context;
import androidx.work.Logger;
import androidx.work.impl.Scheduler;
import androidx.work.impl.model.WorkSpec;
import mt.Log1F380D;

/* compiled from: 00C0 */
public class SystemAlarmScheduler implements Scheduler {
    private static final String TAG;
    private final Context mContext;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("SystemAlarmScheduler");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public SystemAlarmScheduler(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void cancel(String workSpecId) {
        this.mContext.startService(CommandHandler.createStopWorkIntent(this.mContext, workSpecId));
    }

    public boolean hasLimitedSchedulingSlots() {
        return true;
    }

    public void schedule(WorkSpec... workSpecs) {
        for (WorkSpec scheduleWorkSpec : workSpecs) {
            scheduleWorkSpec(scheduleWorkSpec);
        }
    }

    private void scheduleWorkSpec(WorkSpec workSpec) {
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("Scheduling work with workSpecId %s", new Object[]{workSpec.id});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        this.mContext.startService(CommandHandler.createScheduleWorkIntent(this.mContext, workSpec.id));
    }
}

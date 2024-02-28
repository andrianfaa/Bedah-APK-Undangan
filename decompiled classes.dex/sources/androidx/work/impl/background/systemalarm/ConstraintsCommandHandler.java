package androidx.work.impl.background.systemalarm;

import android.content.Context;
import android.content.Intent;
import androidx.work.Logger;
import androidx.work.impl.background.systemalarm.SystemAlarmDispatcher;
import androidx.work.impl.constraints.WorkConstraintsCallback;
import androidx.work.impl.constraints.WorkConstraintsTracker;
import androidx.work.impl.model.WorkSpec;
import java.util.ArrayList;
import java.util.List;
import mt.Log1F380D;

/* compiled from: 00BB */
class ConstraintsCommandHandler {
    private static final String TAG;
    private final Context mContext;
    private final SystemAlarmDispatcher mDispatcher;
    private final int mStartId;
    private final WorkConstraintsTracker mWorkConstraintsTracker;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("ConstraintsCmdHandler");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    ConstraintsCommandHandler(Context context, int startId, SystemAlarmDispatcher dispatcher) {
        this.mContext = context;
        this.mStartId = startId;
        this.mDispatcher = dispatcher;
        this.mWorkConstraintsTracker = new WorkConstraintsTracker(context, dispatcher.getTaskExecutor(), (WorkConstraintsCallback) null);
    }

    /* access modifiers changed from: package-private */
    public void handleConstraintsChanged() {
        List<WorkSpec> scheduledWork = this.mDispatcher.getWorkManager().getWorkDatabase().workSpecDao().getScheduledWork();
        ConstraintProxy.updateAll(this.mContext, scheduledWork);
        this.mWorkConstraintsTracker.replace(scheduledWork);
        ArrayList<WorkSpec> arrayList = new ArrayList<>(scheduledWork.size());
        long currentTimeMillis = System.currentTimeMillis();
        for (WorkSpec next : scheduledWork) {
            String str = next.id;
            if (currentTimeMillis >= next.calculateNextRunTime() && (!next.hasConstraints() || this.mWorkConstraintsTracker.areAllConstraintsMet(str))) {
                arrayList.add(next);
            }
        }
        for (WorkSpec workSpec : arrayList) {
            String str2 = workSpec.id;
            Intent createDelayMetIntent = CommandHandler.createDelayMetIntent(this.mContext, str2);
            Logger logger = Logger.get();
            String str3 = TAG;
            String format = String.format("Creating a delay_met command for workSpec with id (%s)", new Object[]{str2});
            Log1F380D.a((Object) format);
            logger.debug(str3, format, new Throwable[0]);
            this.mDispatcher.postOnMainThread(new SystemAlarmDispatcher.AddRunnable(this.mDispatcher, createDelayMetIntent, this.mStartId));
        }
        this.mWorkConstraintsTracker.reset();
    }
}

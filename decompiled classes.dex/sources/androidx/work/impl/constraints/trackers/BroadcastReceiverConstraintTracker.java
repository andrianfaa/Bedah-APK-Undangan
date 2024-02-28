package androidx.work.impl.constraints.trackers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.work.Logger;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import mt.Log1F380D;

/* compiled from: 00CA */
public abstract class BroadcastReceiverConstraintTracker<T> extends ConstraintTracker<T> {
    private static final String TAG;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                BroadcastReceiverConstraintTracker.this.onBroadcastReceive(context, intent);
            }
        }
    };

    static {
        String tagWithPrefix = Logger.tagWithPrefix("BrdcstRcvrCnstrntTrckr");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public BroadcastReceiverConstraintTracker(Context context, TaskExecutor taskExecutor) {
        super(context, taskExecutor);
    }

    public abstract IntentFilter getIntentFilter();

    public abstract void onBroadcastReceive(Context context, Intent intent);

    public void startTracking() {
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("%s: registering receiver", new Object[]{getClass().getSimpleName()});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        this.mAppContext.registerReceiver(this.mBroadcastReceiver, getIntentFilter());
    }

    public void stopTracking() {
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("%s: unregistering receiver", new Object[]{getClass().getSimpleName()});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        this.mAppContext.unregisterReceiver(this.mBroadcastReceiver);
    }
}

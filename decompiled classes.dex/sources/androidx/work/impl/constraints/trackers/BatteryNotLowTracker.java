package androidx.work.impl.constraints.trackers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.core.app.NotificationCompat;
import androidx.work.Logger;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import mt.Log1F380D;

/* compiled from: 00CC */
public class BatteryNotLowTracker extends BroadcastReceiverConstraintTracker<Boolean> {
    static final float BATTERY_LOW_THRESHOLD = 0.15f;
    private static final String TAG;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("BatteryNotLowTracker");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public BatteryNotLowTracker(Context context, TaskExecutor taskExecutor) {
        super(context, taskExecutor);
    }

    public Boolean getInitialState() {
        Intent registerReceiver = this.mAppContext.registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        boolean z = false;
        if (registerReceiver == null) {
            Logger.get().error(TAG, "getInitialState - null intent received", new Throwable[0]);
            return null;
        }
        int intExtra = registerReceiver.getIntExtra(NotificationCompat.CATEGORY_STATUS, -1);
        float intExtra2 = ((float) registerReceiver.getIntExtra("level", -1)) / ((float) registerReceiver.getIntExtra("scale", -1));
        if (intExtra == 1 || intExtra2 > BATTERY_LOW_THRESHOLD) {
            z = true;
        }
        return Boolean.valueOf(z);
    }

    public IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_OKAY");
        intentFilter.addAction("android.intent.action.BATTERY_LOW");
        return intentFilter;
    }

    public void onBroadcastReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Received %s", new Object[]{intent.getAction()});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            String action = intent.getAction();
            char c = 65535;
            switch (action.hashCode()) {
                case -1980154005:
                    if (action.equals("android.intent.action.BATTERY_OKAY")) {
                        c = 0;
                        break;
                    }
                    break;
                case 490310653:
                    if (action.equals("android.intent.action.BATTERY_LOW")) {
                        c = 1;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    setState(true);
                    return;
                case 1:
                    setState(false);
                    return;
                default:
                    return;
            }
        }
    }
}

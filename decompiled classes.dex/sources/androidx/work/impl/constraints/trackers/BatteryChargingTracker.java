package androidx.work.impl.constraints.trackers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.work.Logger;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import mt.Log1F380D;

/* compiled from: 00CB */
public class BatteryChargingTracker extends BroadcastReceiverConstraintTracker<Boolean> {
    private static final String TAG;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("BatteryChrgTracker");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public BatteryChargingTracker(Context context, TaskExecutor taskExecutor) {
        super(context, taskExecutor);
    }

    private boolean isBatteryChangedIntentCharging(Intent intent) {
        boolean z = true;
        if (Build.VERSION.SDK_INT >= 23) {
            int intExtra = intent.getIntExtra(NotificationCompat.CATEGORY_STATUS, -1);
            if (!(intExtra == 2 || intExtra == 5)) {
                z = false;
            }
            return z;
        }
        if (intent.getIntExtra("plugged", 0) == 0) {
            z = false;
        }
        return z;
    }

    public Boolean getInitialState() {
        Intent registerReceiver = this.mAppContext.registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver != null) {
            return Boolean.valueOf(isBatteryChangedIntentCharging(registerReceiver));
        }
        Logger.get().error(TAG, "getInitialState - null intent received", new Throwable[0]);
        return null;
    }

    public IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        if (Build.VERSION.SDK_INT >= 23) {
            intentFilter.addAction("android.os.action.CHARGING");
            intentFilter.addAction("android.os.action.DISCHARGING");
        } else {
            intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
            intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        }
        return intentFilter;
    }

    public void onBroadcastReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Received %s", new Object[]{action});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            char c = 65535;
            switch (action.hashCode()) {
                case -1886648615:
                    if (action.equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
                        c = 3;
                        break;
                    }
                    break;
                case -54942926:
                    if (action.equals("android.os.action.DISCHARGING")) {
                        c = 1;
                        break;
                    }
                    break;
                case 948344062:
                    if (action.equals("android.os.action.CHARGING")) {
                        c = 0;
                        break;
                    }
                    break;
                case 1019184907:
                    if (action.equals("android.intent.action.ACTION_POWER_CONNECTED")) {
                        c = 2;
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
                case 2:
                    setState(true);
                    return;
                case 3:
                    setState(false);
                    return;
                default:
                    return;
            }
        }
    }
}

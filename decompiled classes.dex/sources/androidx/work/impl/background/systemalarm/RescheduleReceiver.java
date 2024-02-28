package androidx.work.impl.background.systemalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.work.Logger;
import androidx.work.impl.WorkManagerImpl;
import mt.Log1F380D;

/* compiled from: 00BD */
public class RescheduleReceiver extends BroadcastReceiver {
    private static final String TAG;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("RescheduleReceiver");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public void onReceive(Context context, Intent intent) {
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("Received intent %s", new Object[]{intent});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                WorkManagerImpl.getInstance(context).setReschedulePendingResult(goAsync());
            } catch (IllegalStateException e) {
                Logger.get().error(TAG, "Cannot reschedule jobs. WorkManager needs to be initialized via a ContentProvider#onCreate() or an Application#onCreate().", e);
            }
        } else {
            context.startService(CommandHandler.createRescheduleIntent(context));
        }
    }
}

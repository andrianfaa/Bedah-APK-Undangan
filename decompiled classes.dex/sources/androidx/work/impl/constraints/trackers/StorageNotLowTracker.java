package androidx.work.impl.constraints.trackers;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.work.Logger;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import mt.Log1F380D;

/* compiled from: 00CF */
public class StorageNotLowTracker extends BroadcastReceiverConstraintTracker<Boolean> {
    private static final String TAG;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("StorageNotLowTracker");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public StorageNotLowTracker(Context context, TaskExecutor taskExecutor) {
        super(context, taskExecutor);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0037, code lost:
        if (r4.equals("android.intent.action.DEVICE_STORAGE_LOW") != false) goto L_0x003b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Boolean getInitialState() {
        /*
            r8 = this;
            android.content.Context r0 = r8.mAppContext
            android.content.IntentFilter r1 = r8.getIntentFilter()
            r2 = 0
            android.content.Intent r0 = r0.registerReceiver(r2, r1)
            r1 = 1
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r1)
            if (r0 == 0) goto L_0x0045
            java.lang.String r4 = r0.getAction()
            if (r4 != 0) goto L_0x0019
            goto L_0x0045
        L_0x0019:
            java.lang.String r4 = r0.getAction()
            r5 = -1
            int r6 = r4.hashCode()
            r7 = 0
            switch(r6) {
                case -1181163412: goto L_0x0031;
                case -730838620: goto L_0x0027;
                default: goto L_0x0026;
            }
        L_0x0026:
            goto L_0x003a
        L_0x0027:
            java.lang.String r1 = "android.intent.action.DEVICE_STORAGE_OK"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L_0x0026
            r1 = r7
            goto L_0x003b
        L_0x0031:
            java.lang.String r6 = "android.intent.action.DEVICE_STORAGE_LOW"
            boolean r4 = r4.equals(r6)
            if (r4 == 0) goto L_0x0026
            goto L_0x003b
        L_0x003a:
            r1 = r5
        L_0x003b:
            switch(r1) {
                case 0: goto L_0x0044;
                case 1: goto L_0x003f;
                default: goto L_0x003e;
            }
        L_0x003e:
            return r2
        L_0x003f:
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r7)
            return r1
        L_0x0044:
            return r3
        L_0x0045:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.work.impl.constraints.trackers.StorageNotLowTracker.getInitialState():java.lang.Boolean");
    }

    public IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.DEVICE_STORAGE_OK");
        intentFilter.addAction("android.intent.action.DEVICE_STORAGE_LOW");
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
                case -1181163412:
                    if (action.equals("android.intent.action.DEVICE_STORAGE_LOW")) {
                        c = 1;
                        break;
                    }
                    break;
                case -730838620:
                    if (action.equals("android.intent.action.DEVICE_STORAGE_OK")) {
                        c = 0;
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

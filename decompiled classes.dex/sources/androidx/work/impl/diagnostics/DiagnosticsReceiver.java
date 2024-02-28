package androidx.work.impl.diagnostics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.work.ListenableWorker;
import androidx.work.Logger;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.impl.workers.DiagnosticsWorker;
import mt.Log1F380D;

/* compiled from: 00D0 */
public class DiagnosticsReceiver extends BroadcastReceiver {
    private static final String TAG;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("DiagnosticsRcvr");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Logger.get().debug(TAG, "Requesting diagnostics", new Throwable[0]);
            try {
                WorkManager.getInstance(context).enqueue((WorkRequest) OneTimeWorkRequest.from((Class<? extends ListenableWorker>) DiagnosticsWorker.class));
            } catch (IllegalStateException e) {
                Logger.get().error(TAG, "WorkManager is not initialized", e);
            }
        }
    }
}

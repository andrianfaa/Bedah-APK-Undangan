package androidx.work.impl.constraints.controllers;

import android.content.Context;
import android.os.Build;
import androidx.work.Logger;
import androidx.work.NetworkType;
import androidx.work.impl.constraints.NetworkState;
import androidx.work.impl.constraints.trackers.Trackers;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import mt.Log1F380D;

/* compiled from: 00C7 */
public class NetworkMeteredController extends ConstraintController<NetworkState> {
    private static final String TAG;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("NetworkMeteredCtrlr");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public NetworkMeteredController(Context context, TaskExecutor taskExecutor) {
        super(Trackers.getInstance(context, taskExecutor).getNetworkStateTracker());
    }

    /* access modifiers changed from: package-private */
    public boolean hasConstraint(WorkSpec workSpec) {
        return workSpec.constraints.getRequiredNetworkType() == NetworkType.METERED;
    }

    /* access modifiers changed from: package-private */
    public boolean isConstrained(NetworkState state) {
        if (Build.VERSION.SDK_INT >= 26) {
            return !state.isConnected() || !state.isMetered();
        }
        Logger.get().debug(TAG, "Metered network constraint is not supported before API 26, only checking for connected state.", new Throwable[0]);
        return !state.isConnected();
    }
}

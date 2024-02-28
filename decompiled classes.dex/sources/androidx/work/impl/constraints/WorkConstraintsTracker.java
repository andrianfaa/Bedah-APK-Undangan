package androidx.work.impl.constraints;

import android.content.Context;
import androidx.work.Logger;
import androidx.work.impl.constraints.controllers.BatteryChargingController;
import androidx.work.impl.constraints.controllers.BatteryNotLowController;
import androidx.work.impl.constraints.controllers.ConstraintController;
import androidx.work.impl.constraints.controllers.NetworkConnectedController;
import androidx.work.impl.constraints.controllers.NetworkMeteredController;
import androidx.work.impl.constraints.controllers.NetworkNotRoamingController;
import androidx.work.impl.constraints.controllers.NetworkUnmeteredController;
import androidx.work.impl.constraints.controllers.StorageNotLowController;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import java.util.ArrayList;
import java.util.List;
import mt.Log1F380D;

/* compiled from: 00C6 */
public class WorkConstraintsTracker implements ConstraintController.OnConstraintUpdatedCallback {
    private static final String TAG;
    private final WorkConstraintsCallback mCallback;
    private final ConstraintController<?>[] mConstraintControllers;
    private final Object mLock = new Object();

    static {
        String tagWithPrefix = Logger.tagWithPrefix("WorkConstraintsTracker");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public WorkConstraintsTracker(Context context, TaskExecutor taskExecutor, WorkConstraintsCallback callback) {
        Context applicationContext = context.getApplicationContext();
        this.mCallback = callback;
        this.mConstraintControllers = new ConstraintController[]{new BatteryChargingController(applicationContext, taskExecutor), new BatteryNotLowController(applicationContext, taskExecutor), new StorageNotLowController(applicationContext, taskExecutor), new NetworkConnectedController(applicationContext, taskExecutor), new NetworkUnmeteredController(applicationContext, taskExecutor), new NetworkNotRoamingController(applicationContext, taskExecutor), new NetworkMeteredController(applicationContext, taskExecutor)};
    }

    WorkConstraintsTracker(WorkConstraintsCallback callback, ConstraintController<?>[] constraintControllerArr) {
        this.mCallback = callback;
        this.mConstraintControllers = constraintControllerArr;
    }

    public void onConstraintNotMet(List<String> list) {
        synchronized (this.mLock) {
            WorkConstraintsCallback workConstraintsCallback = this.mCallback;
            if (workConstraintsCallback != null) {
                workConstraintsCallback.onAllConstraintsNotMet(list);
            }
        }
    }

    public void replace(Iterable<WorkSpec> iterable) {
        synchronized (this.mLock) {
            for (ConstraintController<?> callback : this.mConstraintControllers) {
                callback.setCallback((ConstraintController.OnConstraintUpdatedCallback) null);
            }
            for (ConstraintController<?> replace : this.mConstraintControllers) {
                replace.replace(iterable);
            }
            for (ConstraintController<?> callback2 : this.mConstraintControllers) {
                callback2.setCallback(this);
            }
        }
    }

    public void reset() {
        synchronized (this.mLock) {
            for (ConstraintController<?> reset : this.mConstraintControllers) {
                reset.reset();
            }
        }
    }

    public boolean areAllConstraintsMet(String workSpecId) {
        synchronized (this.mLock) {
            for (ConstraintController<?> constraintController : this.mConstraintControllers) {
                if (constraintController.isWorkSpecConstrained(workSpecId)) {
                    Logger logger = Logger.get();
                    String str = TAG;
                    String format = String.format("Work %s constrained by %s", new Object[]{workSpecId, constraintController.getClass().getSimpleName()});
                    Log1F380D.a((Object) format);
                    logger.debug(str, format, new Throwable[0]);
                    return false;
                }
            }
            return true;
        }
    }

    public void onConstraintMet(List<String> list) {
        synchronized (this.mLock) {
            ArrayList arrayList = new ArrayList();
            for (String next : list) {
                if (areAllConstraintsMet(next)) {
                    Logger logger = Logger.get();
                    String str = TAG;
                    String format = String.format("Constraints met for %s", new Object[]{next});
                    Log1F380D.a((Object) format);
                    logger.debug(str, format, new Throwable[0]);
                    arrayList.add(next);
                }
            }
            WorkConstraintsCallback workConstraintsCallback = this.mCallback;
            if (workConstraintsCallback != null) {
                workConstraintsCallback.onAllConstraintsMet(arrayList);
            }
        }
    }
}

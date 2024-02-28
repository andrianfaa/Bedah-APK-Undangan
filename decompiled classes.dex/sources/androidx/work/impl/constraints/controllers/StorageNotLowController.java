package androidx.work.impl.constraints.controllers;

import android.content.Context;
import androidx.work.impl.constraints.trackers.Trackers;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;

public class StorageNotLowController extends ConstraintController<Boolean> {
    public StorageNotLowController(Context context, TaskExecutor taskExecutor) {
        super(Trackers.getInstance(context, taskExecutor).getStorageNotLowTracker());
    }

    /* access modifiers changed from: package-private */
    public boolean hasConstraint(WorkSpec workSpec) {
        return workSpec.constraints.requiresStorageNotLow();
    }

    /* access modifiers changed from: package-private */
    public boolean isConstrained(Boolean isStorageNotLow) {
        return !isStorageNotLow.booleanValue();
    }
}

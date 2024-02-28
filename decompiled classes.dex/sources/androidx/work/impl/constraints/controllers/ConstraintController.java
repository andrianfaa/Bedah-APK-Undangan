package androidx.work.impl.constraints.controllers;

import androidx.work.impl.constraints.ConstraintListener;
import androidx.work.impl.constraints.trackers.ConstraintTracker;
import androidx.work.impl.model.WorkSpec;
import java.util.ArrayList;
import java.util.List;

public abstract class ConstraintController<T> implements ConstraintListener<T> {
    private OnConstraintUpdatedCallback mCallback;
    private T mCurrentValue;
    private final List<String> mMatchingWorkSpecIds = new ArrayList();
    private ConstraintTracker<T> mTracker;

    public interface OnConstraintUpdatedCallback {
        void onConstraintMet(List<String> list);

        void onConstraintNotMet(List<String> list);
    }

    ConstraintController(ConstraintTracker<T> constraintTracker) {
        this.mTracker = constraintTracker;
    }

    private void updateCallback(OnConstraintUpdatedCallback callback, T t) {
        if (!this.mMatchingWorkSpecIds.isEmpty() && callback != null) {
            if (t == null || isConstrained(t)) {
                callback.onConstraintNotMet(this.mMatchingWorkSpecIds);
            } else {
                callback.onConstraintMet(this.mMatchingWorkSpecIds);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public abstract boolean hasConstraint(WorkSpec workSpec);

    /* access modifiers changed from: package-private */
    public abstract boolean isConstrained(T t);

    public boolean isWorkSpecConstrained(String workSpecId) {
        T t = this.mCurrentValue;
        return t != null && isConstrained(t) && this.mMatchingWorkSpecIds.contains(workSpecId);
    }

    public void onConstraintChanged(T t) {
        this.mCurrentValue = t;
        updateCallback(this.mCallback, t);
    }

    public void replace(Iterable<WorkSpec> iterable) {
        this.mMatchingWorkSpecIds.clear();
        for (WorkSpec next : iterable) {
            if (hasConstraint(next)) {
                this.mMatchingWorkSpecIds.add(next.id);
            }
        }
        if (this.mMatchingWorkSpecIds.isEmpty()) {
            this.mTracker.removeListener(this);
        } else {
            this.mTracker.addListener(this);
        }
        updateCallback(this.mCallback, this.mCurrentValue);
    }

    public void reset() {
        if (!this.mMatchingWorkSpecIds.isEmpty()) {
            this.mMatchingWorkSpecIds.clear();
            this.mTracker.removeListener(this);
        }
    }

    public void setCallback(OnConstraintUpdatedCallback callback) {
        if (this.mCallback != callback) {
            this.mCallback = callback;
            updateCallback(callback, this.mCurrentValue);
        }
    }
}

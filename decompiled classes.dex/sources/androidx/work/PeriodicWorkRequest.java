package androidx.work;

import android.os.Build;
import androidx.work.WorkRequest;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class PeriodicWorkRequest extends WorkRequest {
    public static final long MIN_PERIODIC_FLEX_MILLIS = 300000;
    public static final long MIN_PERIODIC_INTERVAL_MILLIS = 900000;

    public static final class Builder extends WorkRequest.Builder<Builder, PeriodicWorkRequest> {
        public Builder(Class<? extends ListenableWorker> cls, long repeatInterval, TimeUnit repeatIntervalTimeUnit) {
            super(cls);
            this.mWorkSpec.setPeriodic(repeatIntervalTimeUnit.toMillis(repeatInterval));
        }

        public Builder(Class<? extends ListenableWorker> cls, long repeatInterval, TimeUnit repeatIntervalTimeUnit, long flexInterval, TimeUnit flexIntervalTimeUnit) {
            super(cls);
            this.mWorkSpec.setPeriodic(repeatIntervalTimeUnit.toMillis(repeatInterval), flexIntervalTimeUnit.toMillis(flexInterval));
        }

        public Builder(Class<? extends ListenableWorker> cls, Duration repeatInterval) {
            super(cls);
            this.mWorkSpec.setPeriodic(repeatInterval.toMillis());
        }

        public Builder(Class<? extends ListenableWorker> cls, Duration repeatInterval, Duration flexInterval) {
            super(cls);
            this.mWorkSpec.setPeriodic(repeatInterval.toMillis(), flexInterval.toMillis());
        }

        /* access modifiers changed from: package-private */
        public PeriodicWorkRequest buildInternal() {
            if (this.mBackoffCriteriaSet && Build.VERSION.SDK_INT >= 23 && this.mWorkSpec.constraints.requiresDeviceIdle()) {
                throw new IllegalArgumentException("Cannot set backoff criteria on an idle mode job");
            } else if (!this.mWorkSpec.expedited) {
                return new PeriodicWorkRequest(this);
            } else {
                throw new IllegalArgumentException("PeriodicWorkRequests cannot be expedited");
            }
        }

        /* access modifiers changed from: package-private */
        public Builder getThis() {
            return this;
        }
    }

    PeriodicWorkRequest(Builder builder) {
        super(builder.mId, builder.mWorkSpec, builder.mTags);
    }
}

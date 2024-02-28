package androidx.work;

import android.os.Build;
import androidx.work.WorkInfo;
import androidx.work.impl.model.WorkSpec;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public abstract class WorkRequest {
    public static final long DEFAULT_BACKOFF_DELAY_MILLIS = 30000;
    public static final long MAX_BACKOFF_MILLIS = 18000000;
    public static final long MIN_BACKOFF_MILLIS = 10000;
    private UUID mId;
    private Set<String> mTags;
    private WorkSpec mWorkSpec;

    public static abstract class Builder<B extends Builder<?, ?>, W extends WorkRequest> {
        boolean mBackoffCriteriaSet = false;
        UUID mId = UUID.randomUUID();
        Set<String> mTags = new HashSet();
        WorkSpec mWorkSpec;
        Class<? extends ListenableWorker> mWorkerClass;

        Builder(Class<? extends ListenableWorker> cls) {
            this.mWorkerClass = cls;
            this.mWorkSpec = new WorkSpec(this.mId.toString(), cls.getName());
            addTag(cls.getName());
        }

        public final B addTag(String tag) {
            this.mTags.add(tag);
            return getThis();
        }

        public final W build() {
            W buildInternal = buildInternal();
            Constraints constraints = this.mWorkSpec.constraints;
            boolean z = (Build.VERSION.SDK_INT >= 24 && constraints.hasContentUriTriggers()) || constraints.requiresBatteryNotLow() || constraints.requiresCharging() || (Build.VERSION.SDK_INT >= 23 && constraints.requiresDeviceIdle());
            if (this.mWorkSpec.expedited) {
                if (z) {
                    throw new IllegalArgumentException("Expedited jobs only support network and storage constraints");
                } else if (this.mWorkSpec.initialDelay > 0) {
                    throw new IllegalArgumentException("Expedited jobs cannot be delayed");
                }
            }
            this.mId = UUID.randomUUID();
            WorkSpec workSpec = new WorkSpec(this.mWorkSpec);
            this.mWorkSpec = workSpec;
            workSpec.id = this.mId.toString();
            return buildInternal;
        }

        /* access modifiers changed from: package-private */
        public abstract W buildInternal();

        /* access modifiers changed from: package-private */
        public abstract B getThis();

        public final B keepResultsForAtLeast(long duration, TimeUnit timeUnit) {
            this.mWorkSpec.minimumRetentionDuration = timeUnit.toMillis(duration);
            return getThis();
        }

        public final B keepResultsForAtLeast(Duration duration) {
            this.mWorkSpec.minimumRetentionDuration = duration.toMillis();
            return getThis();
        }

        public final B setBackoffCriteria(BackoffPolicy backoffPolicy, long backoffDelay, TimeUnit timeUnit) {
            this.mBackoffCriteriaSet = true;
            this.mWorkSpec.backoffPolicy = backoffPolicy;
            this.mWorkSpec.setBackoffDelayDuration(timeUnit.toMillis(backoffDelay));
            return getThis();
        }

        public final B setBackoffCriteria(BackoffPolicy backoffPolicy, Duration duration) {
            this.mBackoffCriteriaSet = true;
            this.mWorkSpec.backoffPolicy = backoffPolicy;
            this.mWorkSpec.setBackoffDelayDuration(duration.toMillis());
            return getThis();
        }

        public final B setConstraints(Constraints constraints) {
            this.mWorkSpec.constraints = constraints;
            return getThis();
        }

        public B setExpedited(OutOfQuotaPolicy policy) {
            this.mWorkSpec.expedited = true;
            this.mWorkSpec.outOfQuotaPolicy = policy;
            return getThis();
        }

        public B setInitialDelay(long duration, TimeUnit timeUnit) {
            this.mWorkSpec.initialDelay = timeUnit.toMillis(duration);
            if (Long.MAX_VALUE - System.currentTimeMillis() > this.mWorkSpec.initialDelay) {
                return getThis();
            }
            throw new IllegalArgumentException("The given initial delay is too large and will cause an overflow!");
        }

        public B setInitialDelay(Duration duration) {
            this.mWorkSpec.initialDelay = duration.toMillis();
            if (Long.MAX_VALUE - System.currentTimeMillis() > this.mWorkSpec.initialDelay) {
                return getThis();
            }
            throw new IllegalArgumentException("The given initial delay is too large and will cause an overflow!");
        }

        public final B setInitialRunAttemptCount(int runAttemptCount) {
            this.mWorkSpec.runAttemptCount = runAttemptCount;
            return getThis();
        }

        public final B setInitialState(WorkInfo.State state) {
            this.mWorkSpec.state = state;
            return getThis();
        }

        public final B setInputData(Data inputData) {
            this.mWorkSpec.input = inputData;
            return getThis();
        }

        public final B setPeriodStartTime(long periodStartTime, TimeUnit timeUnit) {
            this.mWorkSpec.periodStartTime = timeUnit.toMillis(periodStartTime);
            return getThis();
        }

        public final B setScheduleRequestedAt(long scheduleRequestedAt, TimeUnit timeUnit) {
            this.mWorkSpec.scheduleRequestedAt = timeUnit.toMillis(scheduleRequestedAt);
            return getThis();
        }
    }

    protected WorkRequest(UUID id, WorkSpec workSpec, Set<String> set) {
        this.mId = id;
        this.mWorkSpec = workSpec;
        this.mTags = set;
    }

    public UUID getId() {
        return this.mId;
    }

    public String getStringId() {
        return this.mId.toString();
    }

    public Set<String> getTags() {
        return this.mTags;
    }

    public WorkSpec getWorkSpec() {
        return this.mWorkSpec;
    }
}

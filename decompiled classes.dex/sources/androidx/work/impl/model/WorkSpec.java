package androidx.work.impl.model;

import androidx.arch.core.util.Function;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.Logger;
import androidx.work.OutOfQuotaPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mt.Log1F380D;

/* compiled from: 00D3 */
public final class WorkSpec {
    public static final long SCHEDULE_NOT_REQUESTED_YET = -1;
    private static final String TAG;
    public static final Function<List<WorkInfoPojo>, List<WorkInfo>> WORK_INFO_MAPPER = new Function<List<WorkInfoPojo>, List<WorkInfo>>() {
        public List<WorkInfo> apply(List<WorkInfoPojo> list) {
            if (list == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList(list.size());
            for (WorkInfoPojo workInfo : list) {
                arrayList.add(workInfo.toWorkInfo());
            }
            return arrayList;
        }
    };
    public long backoffDelayDuration = WorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS;
    public BackoffPolicy backoffPolicy = BackoffPolicy.EXPONENTIAL;
    public Constraints constraints = Constraints.NONE;
    public boolean expedited;
    public long flexDuration;
    public String id;
    public long initialDelay;
    public Data input = Data.EMPTY;
    public String inputMergerClassName;
    public long intervalDuration;
    public long minimumRetentionDuration;
    public OutOfQuotaPolicy outOfQuotaPolicy = OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST;
    public Data output = Data.EMPTY;
    public long periodStartTime;
    public int runAttemptCount;
    public long scheduleRequestedAt = -1;
    public WorkInfo.State state = WorkInfo.State.ENQUEUED;
    public String workerClassName;

    public static class IdAndState {
        public String id;
        public WorkInfo.State state;

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof IdAndState)) {
                return false;
            }
            IdAndState idAndState = (IdAndState) o;
            if (this.state != idAndState.state) {
                return false;
            }
            return this.id.equals(idAndState.id);
        }

        public int hashCode() {
            return (this.id.hashCode() * 31) + this.state.hashCode();
        }
    }

    public static class WorkInfoPojo {
        public String id;
        public Data output;
        public List<Data> progress;
        public int runAttemptCount;
        public WorkInfo.State state;
        public List<String> tags;

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof WorkInfoPojo)) {
                return false;
            }
            WorkInfoPojo workInfoPojo = (WorkInfoPojo) o;
            if (this.runAttemptCount != workInfoPojo.runAttemptCount) {
                return false;
            }
            String str = this.id;
            if (str == null ? workInfoPojo.id != null : !str.equals(workInfoPojo.id)) {
                return false;
            }
            if (this.state != workInfoPojo.state) {
                return false;
            }
            Data data = this.output;
            if (data == null ? workInfoPojo.output != null : !data.equals(workInfoPojo.output)) {
                return false;
            }
            List<String> list = this.tags;
            if (list == null ? workInfoPojo.tags != null : !list.equals(workInfoPojo.tags)) {
                return false;
            }
            List<Data> list2 = this.progress;
            return list2 != null ? list2.equals(workInfoPojo.progress) : workInfoPojo.progress == null;
        }

        public int hashCode() {
            String str = this.id;
            int i = 0;
            int hashCode = (str != null ? str.hashCode() : 0) * 31;
            WorkInfo.State state2 = this.state;
            int hashCode2 = (hashCode + (state2 != null ? state2.hashCode() : 0)) * 31;
            Data data = this.output;
            int hashCode3 = (((hashCode2 + (data != null ? data.hashCode() : 0)) * 31) + this.runAttemptCount) * 31;
            List<String> list = this.tags;
            int hashCode4 = (hashCode3 + (list != null ? list.hashCode() : 0)) * 31;
            List<Data> list2 = this.progress;
            if (list2 != null) {
                i = list2.hashCode();
            }
            return hashCode4 + i;
        }

        public WorkInfo toWorkInfo() {
            List<Data> list = this.progress;
            return new WorkInfo(UUID.fromString(this.id), this.state, this.output, this.tags, (list == null || list.isEmpty()) ? Data.EMPTY : this.progress.get(0), this.runAttemptCount);
        }
    }

    static {
        String tagWithPrefix = Logger.tagWithPrefix("WorkSpec");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public WorkSpec(WorkSpec other) {
        this.id = other.id;
        this.workerClassName = other.workerClassName;
        this.state = other.state;
        this.inputMergerClassName = other.inputMergerClassName;
        this.input = new Data(other.input);
        this.output = new Data(other.output);
        this.initialDelay = other.initialDelay;
        this.intervalDuration = other.intervalDuration;
        this.flexDuration = other.flexDuration;
        this.constraints = new Constraints(other.constraints);
        this.runAttemptCount = other.runAttemptCount;
        this.backoffPolicy = other.backoffPolicy;
        this.backoffDelayDuration = other.backoffDelayDuration;
        this.periodStartTime = other.periodStartTime;
        this.minimumRetentionDuration = other.minimumRetentionDuration;
        this.scheduleRequestedAt = other.scheduleRequestedAt;
        this.expedited = other.expedited;
        this.outOfQuotaPolicy = other.outOfQuotaPolicy;
    }

    public WorkSpec(String id2, String workerClassName2) {
        this.id = id2;
        this.workerClassName = workerClassName2;
    }

    public long calculateNextRunTime() {
        boolean z = false;
        if (isBackedOff()) {
            if (this.backoffPolicy == BackoffPolicy.LINEAR) {
                z = true;
            }
            return this.periodStartTime + Math.min(WorkRequest.MAX_BACKOFF_MILLIS, z ? this.backoffDelayDuration * ((long) this.runAttemptCount) : (long) Math.scalb((float) this.backoffDelayDuration, this.runAttemptCount - 1));
        }
        long j = 0;
        if (isPeriodic()) {
            long currentTimeMillis = System.currentTimeMillis();
            long j2 = this.periodStartTime;
            long j3 = j2 == 0 ? this.initialDelay + currentTimeMillis : j2;
            long j4 = this.flexDuration;
            long j5 = this.intervalDuration;
            if (j4 != j5) {
                z = true;
            }
            if (z) {
                if (j2 == 0) {
                    j = j4 * -1;
                }
                return j5 + j3 + j;
            }
            if (j2 != 0) {
                j = j5;
            }
            return j3 + j;
        }
        long j6 = this.periodStartTime;
        if (j6 == 0) {
            j6 = System.currentTimeMillis();
        }
        return this.initialDelay + j6;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkSpec workSpec = (WorkSpec) o;
        if (this.initialDelay != workSpec.initialDelay || this.intervalDuration != workSpec.intervalDuration || this.flexDuration != workSpec.flexDuration || this.runAttemptCount != workSpec.runAttemptCount || this.backoffDelayDuration != workSpec.backoffDelayDuration || this.periodStartTime != workSpec.periodStartTime || this.minimumRetentionDuration != workSpec.minimumRetentionDuration || this.scheduleRequestedAt != workSpec.scheduleRequestedAt || this.expedited != workSpec.expedited || !this.id.equals(workSpec.id) || this.state != workSpec.state || !this.workerClassName.equals(workSpec.workerClassName)) {
            return false;
        }
        String str = this.inputMergerClassName;
        if (str == null ? workSpec.inputMergerClassName != null : !str.equals(workSpec.inputMergerClassName)) {
            return false;
        }
        if (this.input.equals(workSpec.input) && this.output.equals(workSpec.output) && this.constraints.equals(workSpec.constraints) && this.backoffPolicy == workSpec.backoffPolicy) {
            return this.outOfQuotaPolicy == workSpec.outOfQuotaPolicy;
        }
        return false;
    }

    public boolean hasConstraints() {
        return !Constraints.NONE.equals(this.constraints);
    }

    public int hashCode() {
        int hashCode = ((((this.id.hashCode() * 31) + this.state.hashCode()) * 31) + this.workerClassName.hashCode()) * 31;
        String str = this.inputMergerClassName;
        int hashCode2 = str != null ? str.hashCode() : 0;
        long j = this.initialDelay;
        long j2 = this.intervalDuration;
        long j3 = this.flexDuration;
        long j4 = this.backoffDelayDuration;
        long j5 = this.periodStartTime;
        long j6 = this.minimumRetentionDuration;
        long j7 = this.scheduleRequestedAt;
        return ((((((((((((((((((((((((((((hashCode + hashCode2) * 31) + this.input.hashCode()) * 31) + this.output.hashCode()) * 31) + ((int) (j ^ (j >>> 32)))) * 31) + ((int) (j2 ^ (j2 >>> 32)))) * 31) + ((int) (j3 ^ (j3 >>> 32)))) * 31) + this.constraints.hashCode()) * 31) + this.runAttemptCount) * 31) + this.backoffPolicy.hashCode()) * 31) + ((int) (j4 ^ (j4 >>> 32)))) * 31) + ((int) (j5 ^ (j5 >>> 32)))) * 31) + ((int) (j6 ^ (j6 >>> 32)))) * 31) + ((int) (j7 ^ (j7 >>> 32)))) * 31) + (this.expedited ? 1 : 0)) * 31) + this.outOfQuotaPolicy.hashCode();
    }

    public boolean isBackedOff() {
        return this.state == WorkInfo.State.ENQUEUED && this.runAttemptCount > 0;
    }

    public boolean isPeriodic() {
        return this.intervalDuration != 0;
    }

    public void setBackoffDelayDuration(long backoffDelayDuration2) {
        if (backoffDelayDuration2 > WorkRequest.MAX_BACKOFF_MILLIS) {
            Logger.get().warning(TAG, "Backoff delay duration exceeds maximum value", new Throwable[0]);
            backoffDelayDuration2 = WorkRequest.MAX_BACKOFF_MILLIS;
        }
        if (backoffDelayDuration2 < WorkRequest.MIN_BACKOFF_MILLIS) {
            Logger.get().warning(TAG, "Backoff delay duration less than minimum value", new Throwable[0]);
            backoffDelayDuration2 = WorkRequest.MIN_BACKOFF_MILLIS;
        }
        this.backoffDelayDuration = backoffDelayDuration2;
    }

    public String toString() {
        return "{WorkSpec: " + this.id + "}";
    }

    public void setPeriodic(long intervalDuration2) {
        if (intervalDuration2 < PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS) {
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Interval duration lesser than minimum allowed value; Changed to %s", new Object[]{Long.valueOf(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS)});
            Log1F380D.a((Object) format);
            logger.warning(str, format, new Throwable[0]);
            intervalDuration2 = PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS;
        }
        setPeriodic(intervalDuration2, intervalDuration2);
    }

    public void setPeriodic(long intervalDuration2, long flexDuration2) {
        if (intervalDuration2 < PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS) {
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Interval duration lesser than minimum allowed value; Changed to %s", new Object[]{Long.valueOf(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS)});
            Log1F380D.a((Object) format);
            logger.warning(str, format, new Throwable[0]);
            intervalDuration2 = PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS;
        }
        if (flexDuration2 < PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS) {
            Logger logger2 = Logger.get();
            String str2 = TAG;
            String format2 = String.format("Flex duration lesser than minimum allowed value; Changed to %s", new Object[]{Long.valueOf(PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS)});
            Log1F380D.a((Object) format2);
            logger2.warning(str2, format2, new Throwable[0]);
            flexDuration2 = PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS;
        }
        if (flexDuration2 > intervalDuration2) {
            Logger logger3 = Logger.get();
            String str3 = TAG;
            String format3 = String.format("Flex duration greater than interval duration; Changed to %s", new Object[]{Long.valueOf(intervalDuration2)});
            Log1F380D.a((Object) format3);
            logger3.warning(str3, format3, new Throwable[0]);
            flexDuration2 = intervalDuration2;
        }
        this.intervalDuration = intervalDuration2;
        this.flexDuration = flexDuration2;
    }
}

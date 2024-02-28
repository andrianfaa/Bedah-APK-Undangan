package androidx.work;

import android.os.Build;
import androidx.work.WorkRequest;
import java.util.ArrayList;
import java.util.List;

public final class OneTimeWorkRequest extends WorkRequest {

    public static final class Builder extends WorkRequest.Builder<Builder, OneTimeWorkRequest> {
        public Builder(Class<? extends ListenableWorker> cls) {
            super(cls);
            this.mWorkSpec.inputMergerClassName = OverwritingInputMerger.class.getName();
        }

        /* access modifiers changed from: package-private */
        public OneTimeWorkRequest buildInternal() {
            if (!this.mBackoffCriteriaSet || Build.VERSION.SDK_INT < 23 || !this.mWorkSpec.constraints.requiresDeviceIdle()) {
                return new OneTimeWorkRequest(this);
            }
            throw new IllegalArgumentException("Cannot set backoff criteria on an idle mode job");
        }

        /* access modifiers changed from: package-private */
        public Builder getThis() {
            return this;
        }

        public Builder setInputMerger(Class<? extends InputMerger> cls) {
            this.mWorkSpec.inputMergerClassName = cls.getName();
            return this;
        }
    }

    OneTimeWorkRequest(Builder builder) {
        super(builder.mId, builder.mWorkSpec, builder.mTags);
    }

    public static OneTimeWorkRequest from(Class<? extends ListenableWorker> cls) {
        return (OneTimeWorkRequest) new Builder(cls).build();
    }

    public static List<OneTimeWorkRequest> from(List<Class<? extends ListenableWorker>> list) {
        ArrayList arrayList = new ArrayList(list.size());
        for (Class<? extends ListenableWorker> builder : list) {
            arrayList.add((OneTimeWorkRequest) new Builder(builder).build());
        }
        return arrayList;
    }
}

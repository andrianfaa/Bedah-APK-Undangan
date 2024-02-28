package androidx.work;

import androidx.lifecycle.LiveData;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Collections;
import java.util.List;

public abstract class WorkContinuation {
    public static WorkContinuation combine(List<WorkContinuation> list) {
        return list.get(0).combineInternal(list);
    }

    /* access modifiers changed from: protected */
    public abstract WorkContinuation combineInternal(List<WorkContinuation> list);

    public abstract Operation enqueue();

    public abstract ListenableFuture<List<WorkInfo>> getWorkInfos();

    public abstract LiveData<List<WorkInfo>> getWorkInfosLiveData();

    public final WorkContinuation then(OneTimeWorkRequest work) {
        return then((List<OneTimeWorkRequest>) Collections.singletonList(work));
    }

    public abstract WorkContinuation then(List<OneTimeWorkRequest> list);
}

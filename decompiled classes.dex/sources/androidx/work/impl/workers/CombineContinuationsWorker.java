package androidx.work.impl.workers;

import android.content.Context;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class CombineContinuationsWorker extends Worker {
    public CombineContinuationsWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public ListenableWorker.Result doWork() {
        return ListenableWorker.Result.success(getInputData());
    }
}

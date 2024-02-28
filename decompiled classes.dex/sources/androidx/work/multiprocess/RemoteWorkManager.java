package androidx.work.multiprocess;

import android.content.Context;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkQuery;
import androidx.work.WorkRequest;
import androidx.work.impl.WorkManagerImpl;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class RemoteWorkManager {
    protected RemoteWorkManager() {
    }

    public static RemoteWorkManager getInstance(Context context) {
        RemoteWorkManager remoteWorkManager = WorkManagerImpl.getInstance(context).getRemoteWorkManager();
        if (remoteWorkManager != null) {
            return remoteWorkManager;
        }
        throw new IllegalStateException("Unable to initialize RemoteWorkManager");
    }

    public final RemoteWorkContinuation beginUniqueWork(String uniqueWorkName, ExistingWorkPolicy existingWorkPolicy, OneTimeWorkRequest work) {
        return beginUniqueWork(uniqueWorkName, existingWorkPolicy, (List<OneTimeWorkRequest>) Collections.singletonList(work));
    }

    public abstract RemoteWorkContinuation beginUniqueWork(String str, ExistingWorkPolicy existingWorkPolicy, List<OneTimeWorkRequest> list);

    public final RemoteWorkContinuation beginWith(OneTimeWorkRequest work) {
        return beginWith((List<OneTimeWorkRequest>) Collections.singletonList(work));
    }

    public abstract RemoteWorkContinuation beginWith(List<OneTimeWorkRequest> list);

    public abstract ListenableFuture<Void> cancelAllWork();

    public abstract ListenableFuture<Void> cancelAllWorkByTag(String str);

    public abstract ListenableFuture<Void> cancelUniqueWork(String str);

    public abstract ListenableFuture<Void> cancelWorkById(UUID uuid);

    public abstract ListenableFuture<Void> enqueue(WorkContinuation workContinuation);

    public abstract ListenableFuture<Void> enqueue(WorkRequest workRequest);

    public abstract ListenableFuture<Void> enqueue(List<WorkRequest> list);

    public abstract ListenableFuture<Void> enqueueUniquePeriodicWork(String str, ExistingPeriodicWorkPolicy existingPeriodicWorkPolicy, PeriodicWorkRequest periodicWorkRequest);

    public final ListenableFuture<Void> enqueueUniqueWork(String uniqueWorkName, ExistingWorkPolicy existingWorkPolicy, OneTimeWorkRequest work) {
        return enqueueUniqueWork(uniqueWorkName, existingWorkPolicy, (List<OneTimeWorkRequest>) Collections.singletonList(work));
    }

    public abstract ListenableFuture<Void> enqueueUniqueWork(String str, ExistingWorkPolicy existingWorkPolicy, List<OneTimeWorkRequest> list);

    public abstract ListenableFuture<List<WorkInfo>> getWorkInfos(WorkQuery workQuery);

    public abstract ListenableFuture<Void> setProgress(UUID uuid, Data data);
}

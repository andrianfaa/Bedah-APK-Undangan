package androidx.work;

import android.net.Network;
import android.net.Uri;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;

public final class WorkerParameters {
    private Executor mBackgroundExecutor;
    private ForegroundUpdater mForegroundUpdater;
    private UUID mId;
    private Data mInputData;
    private ProgressUpdater mProgressUpdater;
    private int mRunAttemptCount;
    private RuntimeExtras mRuntimeExtras;
    private Set<String> mTags;
    private TaskExecutor mWorkTaskExecutor;
    private WorkerFactory mWorkerFactory;

    public static class RuntimeExtras {
        public Network network;
        public List<String> triggeredContentAuthorities = Collections.emptyList();
        public List<Uri> triggeredContentUris = Collections.emptyList();
    }

    public WorkerParameters(UUID id, Data inputData, Collection<String> collection, RuntimeExtras runtimeExtras, int runAttemptCount, Executor backgroundExecutor, TaskExecutor workTaskExecutor, WorkerFactory workerFactory, ProgressUpdater progressUpdater, ForegroundUpdater foregroundUpdater) {
        this.mId = id;
        this.mInputData = inputData;
        this.mTags = new HashSet(collection);
        this.mRuntimeExtras = runtimeExtras;
        this.mRunAttemptCount = runAttemptCount;
        this.mBackgroundExecutor = backgroundExecutor;
        this.mWorkTaskExecutor = workTaskExecutor;
        this.mWorkerFactory = workerFactory;
        this.mProgressUpdater = progressUpdater;
        this.mForegroundUpdater = foregroundUpdater;
    }

    public Executor getBackgroundExecutor() {
        return this.mBackgroundExecutor;
    }

    public ForegroundUpdater getForegroundUpdater() {
        return this.mForegroundUpdater;
    }

    public UUID getId() {
        return this.mId;
    }

    public Data getInputData() {
        return this.mInputData;
    }

    public Network getNetwork() {
        return this.mRuntimeExtras.network;
    }

    public ProgressUpdater getProgressUpdater() {
        return this.mProgressUpdater;
    }

    public int getRunAttemptCount() {
        return this.mRunAttemptCount;
    }

    public RuntimeExtras getRuntimeExtras() {
        return this.mRuntimeExtras;
    }

    public Set<String> getTags() {
        return this.mTags;
    }

    public TaskExecutor getTaskExecutor() {
        return this.mWorkTaskExecutor;
    }

    public List<String> getTriggeredContentAuthorities() {
        return this.mRuntimeExtras.triggeredContentAuthorities;
    }

    public List<Uri> getTriggeredContentUris() {
        return this.mRuntimeExtras.triggeredContentUris;
    }

    public WorkerFactory getWorkerFactory() {
        return this.mWorkerFactory;
    }
}

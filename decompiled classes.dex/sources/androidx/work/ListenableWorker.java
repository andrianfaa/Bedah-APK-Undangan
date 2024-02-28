package androidx.work;

import android.content.Context;
import android.net.Network;
import android.net.Uri;
import androidx.work.impl.utils.futures.SettableFuture;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;

public abstract class ListenableWorker {
    private Context mAppContext;
    private boolean mRunInForeground;
    private volatile boolean mStopped;
    private boolean mUsed;
    private WorkerParameters mWorkerParams;

    public static abstract class Result {

        public static final class Failure extends Result {
            private final Data mOutputData;

            public Failure() {
                this(Data.EMPTY);
            }

            public Failure(Data outputData) {
                this.mOutputData = outputData;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                return this.mOutputData.equals(((Failure) o).mOutputData);
            }

            public Data getOutputData() {
                return this.mOutputData;
            }

            public int hashCode() {
                return (Failure.class.getName().hashCode() * 31) + this.mOutputData.hashCode();
            }

            public String toString() {
                return "Failure {mOutputData=" + this.mOutputData + '}';
            }
        }

        public static final class Retry extends Result {
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                return o != null && getClass() == o.getClass();
            }

            public Data getOutputData() {
                return Data.EMPTY;
            }

            public int hashCode() {
                return Retry.class.getName().hashCode();
            }

            public String toString() {
                return "Retry";
            }
        }

        public static final class Success extends Result {
            private final Data mOutputData;

            public Success() {
                this(Data.EMPTY);
            }

            public Success(Data outputData) {
                this.mOutputData = outputData;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                return this.mOutputData.equals(((Success) o).mOutputData);
            }

            public Data getOutputData() {
                return this.mOutputData;
            }

            public int hashCode() {
                return (Success.class.getName().hashCode() * 31) + this.mOutputData.hashCode();
            }

            public String toString() {
                return "Success {mOutputData=" + this.mOutputData + '}';
            }
        }

        Result() {
        }

        public static Result failure() {
            return new Failure();
        }

        public static Result failure(Data outputData) {
            return new Failure(outputData);
        }

        public static Result retry() {
            return new Retry();
        }

        public static Result success() {
            return new Success();
        }

        public static Result success(Data outputData) {
            return new Success(outputData);
        }

        public abstract Data getOutputData();
    }

    public ListenableWorker(Context appContext, WorkerParameters workerParams) {
        if (appContext == null) {
            throw new IllegalArgumentException("Application Context is null");
        } else if (workerParams != null) {
            this.mAppContext = appContext;
            this.mWorkerParams = workerParams;
        } else {
            throw new IllegalArgumentException("WorkerParameters is null");
        }
    }

    public final Context getApplicationContext() {
        return this.mAppContext;
    }

    public Executor getBackgroundExecutor() {
        return this.mWorkerParams.getBackgroundExecutor();
    }

    public ListenableFuture<ForegroundInfo> getForegroundInfoAsync() {
        SettableFuture create = SettableFuture.create();
        create.setException(new IllegalStateException("Expedited WorkRequests require a ListenableWorker to provide an implementation for `getForegroundInfoAsync()`"));
        return create;
    }

    public final UUID getId() {
        return this.mWorkerParams.getId();
    }

    public final Data getInputData() {
        return this.mWorkerParams.getInputData();
    }

    public final Network getNetwork() {
        return this.mWorkerParams.getNetwork();
    }

    public final int getRunAttemptCount() {
        return this.mWorkerParams.getRunAttemptCount();
    }

    public final Set<String> getTags() {
        return this.mWorkerParams.getTags();
    }

    public TaskExecutor getTaskExecutor() {
        return this.mWorkerParams.getTaskExecutor();
    }

    public final List<String> getTriggeredContentAuthorities() {
        return this.mWorkerParams.getTriggeredContentAuthorities();
    }

    public final List<Uri> getTriggeredContentUris() {
        return this.mWorkerParams.getTriggeredContentUris();
    }

    public WorkerFactory getWorkerFactory() {
        return this.mWorkerParams.getWorkerFactory();
    }

    public boolean isRunInForeground() {
        return this.mRunInForeground;
    }

    public final boolean isStopped() {
        return this.mStopped;
    }

    public final boolean isUsed() {
        return this.mUsed;
    }

    public void onStopped() {
    }

    public final ListenableFuture<Void> setForegroundAsync(ForegroundInfo foregroundInfo) {
        this.mRunInForeground = true;
        return this.mWorkerParams.getForegroundUpdater().setForegroundAsync(getApplicationContext(), getId(), foregroundInfo);
    }

    public ListenableFuture<Void> setProgressAsync(Data data) {
        return this.mWorkerParams.getProgressUpdater().updateProgress(getApplicationContext(), getId(), data);
    }

    public void setRunInForeground(boolean runInForeground) {
        this.mRunInForeground = runInForeground;
    }

    public final void setUsed() {
        this.mUsed = true;
    }

    public abstract ListenableFuture<Result> startWork();

    public final void stop() {
        this.mStopped = true;
        onStopped();
    }
}

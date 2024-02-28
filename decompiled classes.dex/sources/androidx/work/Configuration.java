package androidx.work;

import android.os.Build;
import androidx.work.impl.DefaultRunnableScheduler;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class Configuration {
    public static final int MIN_SCHEDULER_LIMIT = 20;
    final String mDefaultProcessName;
    final InitializationExceptionHandler mExceptionHandler;
    final Executor mExecutor;
    final InputMergerFactory mInputMergerFactory;
    private final boolean mIsUsingDefaultTaskExecutor;
    final int mLoggingLevel;
    final int mMaxJobSchedulerId;
    final int mMaxSchedulerLimit;
    final int mMinJobSchedulerId;
    final RunnableScheduler mRunnableScheduler;
    final Executor mTaskExecutor;
    final WorkerFactory mWorkerFactory;

    public static final class Builder {
        String mDefaultProcessName;
        InitializationExceptionHandler mExceptionHandler;
        Executor mExecutor;
        InputMergerFactory mInputMergerFactory;
        int mLoggingLevel;
        int mMaxJobSchedulerId;
        int mMaxSchedulerLimit;
        int mMinJobSchedulerId;
        RunnableScheduler mRunnableScheduler;
        Executor mTaskExecutor;
        WorkerFactory mWorkerFactory;

        public Builder() {
            this.mLoggingLevel = 4;
            this.mMinJobSchedulerId = 0;
            this.mMaxJobSchedulerId = Integer.MAX_VALUE;
            this.mMaxSchedulerLimit = 20;
        }

        public Builder(Configuration configuration) {
            this.mExecutor = configuration.mExecutor;
            this.mWorkerFactory = configuration.mWorkerFactory;
            this.mInputMergerFactory = configuration.mInputMergerFactory;
            this.mTaskExecutor = configuration.mTaskExecutor;
            this.mLoggingLevel = configuration.mLoggingLevel;
            this.mMinJobSchedulerId = configuration.mMinJobSchedulerId;
            this.mMaxJobSchedulerId = configuration.mMaxJobSchedulerId;
            this.mMaxSchedulerLimit = configuration.mMaxSchedulerLimit;
            this.mRunnableScheduler = configuration.mRunnableScheduler;
            this.mExceptionHandler = configuration.mExceptionHandler;
            this.mDefaultProcessName = configuration.mDefaultProcessName;
        }

        public Configuration build() {
            return new Configuration(this);
        }

        public Builder setDefaultProcessName(String processName) {
            this.mDefaultProcessName = processName;
            return this;
        }

        public Builder setExecutor(Executor executor) {
            this.mExecutor = executor;
            return this;
        }

        public Builder setInitializationExceptionHandler(InitializationExceptionHandler exceptionHandler) {
            this.mExceptionHandler = exceptionHandler;
            return this;
        }

        public Builder setInputMergerFactory(InputMergerFactory inputMergerFactory) {
            this.mInputMergerFactory = inputMergerFactory;
            return this;
        }

        public Builder setJobSchedulerJobIdRange(int minJobSchedulerId, int maxJobSchedulerId) {
            if (maxJobSchedulerId - minJobSchedulerId >= 1000) {
                this.mMinJobSchedulerId = minJobSchedulerId;
                this.mMaxJobSchedulerId = maxJobSchedulerId;
                return this;
            }
            throw new IllegalArgumentException("WorkManager needs a range of at least 1000 job ids.");
        }

        public Builder setMaxSchedulerLimit(int maxSchedulerLimit) {
            if (maxSchedulerLimit >= 20) {
                this.mMaxSchedulerLimit = Math.min(maxSchedulerLimit, 50);
                return this;
            }
            throw new IllegalArgumentException("WorkManager needs to be able to schedule at least 20 jobs in JobScheduler.");
        }

        public Builder setMinimumLoggingLevel(int loggingLevel) {
            this.mLoggingLevel = loggingLevel;
            return this;
        }

        public Builder setRunnableScheduler(RunnableScheduler runnableScheduler) {
            this.mRunnableScheduler = runnableScheduler;
            return this;
        }

        public Builder setTaskExecutor(Executor taskExecutor) {
            this.mTaskExecutor = taskExecutor;
            return this;
        }

        public Builder setWorkerFactory(WorkerFactory workerFactory) {
            this.mWorkerFactory = workerFactory;
            return this;
        }
    }

    public interface Provider {
        Configuration getWorkManagerConfiguration();
    }

    Configuration(Builder builder) {
        if (builder.mExecutor == null) {
            this.mExecutor = createDefaultExecutor(false);
        } else {
            this.mExecutor = builder.mExecutor;
        }
        if (builder.mTaskExecutor == null) {
            this.mIsUsingDefaultTaskExecutor = true;
            this.mTaskExecutor = createDefaultExecutor(true);
        } else {
            this.mIsUsingDefaultTaskExecutor = false;
            this.mTaskExecutor = builder.mTaskExecutor;
        }
        if (builder.mWorkerFactory == null) {
            this.mWorkerFactory = WorkerFactory.getDefaultWorkerFactory();
        } else {
            this.mWorkerFactory = builder.mWorkerFactory;
        }
        if (builder.mInputMergerFactory == null) {
            this.mInputMergerFactory = InputMergerFactory.getDefaultInputMergerFactory();
        } else {
            this.mInputMergerFactory = builder.mInputMergerFactory;
        }
        if (builder.mRunnableScheduler == null) {
            this.mRunnableScheduler = new DefaultRunnableScheduler();
        } else {
            this.mRunnableScheduler = builder.mRunnableScheduler;
        }
        this.mLoggingLevel = builder.mLoggingLevel;
        this.mMinJobSchedulerId = builder.mMinJobSchedulerId;
        this.mMaxJobSchedulerId = builder.mMaxJobSchedulerId;
        this.mMaxSchedulerLimit = builder.mMaxSchedulerLimit;
        this.mExceptionHandler = builder.mExceptionHandler;
        this.mDefaultProcessName = builder.mDefaultProcessName;
    }

    private Executor createDefaultExecutor(boolean isTaskExecutor) {
        return Executors.newFixedThreadPool(Math.max(2, Math.min(Runtime.getRuntime().availableProcessors() - 1, 4)), createDefaultThreadFactory(isTaskExecutor));
    }

    private ThreadFactory createDefaultThreadFactory(final boolean isTaskExecutor) {
        return new ThreadFactory() {
            private final AtomicInteger mThreadCount = new AtomicInteger(0);

            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, (isTaskExecutor ? "WM.task-" : "androidx.work-") + this.mThreadCount.incrementAndGet());
            }
        };
    }

    public String getDefaultProcessName() {
        return this.mDefaultProcessName;
    }

    public InitializationExceptionHandler getExceptionHandler() {
        return this.mExceptionHandler;
    }

    public Executor getExecutor() {
        return this.mExecutor;
    }

    public InputMergerFactory getInputMergerFactory() {
        return this.mInputMergerFactory;
    }

    public int getMaxJobSchedulerId() {
        return this.mMaxJobSchedulerId;
    }

    public int getMaxSchedulerLimit() {
        return Build.VERSION.SDK_INT == 23 ? this.mMaxSchedulerLimit / 2 : this.mMaxSchedulerLimit;
    }

    public int getMinJobSchedulerId() {
        return this.mMinJobSchedulerId;
    }

    public int getMinimumLoggingLevel() {
        return this.mLoggingLevel;
    }

    public RunnableScheduler getRunnableScheduler() {
        return this.mRunnableScheduler;
    }

    public Executor getTaskExecutor() {
        return this.mTaskExecutor;
    }

    public WorkerFactory getWorkerFactory() {
        return this.mWorkerFactory;
    }

    public boolean isUsingDefaultTaskExecutor() {
        return this.mIsUsingDefaultTaskExecutor;
    }
}

package androidx.work.impl;

import android.content.Context;
import androidx.work.Configuration;
import androidx.work.Data;
import androidx.work.InputMerger;
import androidx.work.ListenableWorker;
import androidx.work.Logger;
import androidx.work.WorkInfo;
import androidx.work.WorkerParameters;
import androidx.work.impl.background.systemalarm.RescheduleReceiver;
import androidx.work.impl.foreground.ForegroundProcessor;
import androidx.work.impl.model.DependencyDao;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.model.WorkSpecDao;
import androidx.work.impl.model.WorkTagDao;
import androidx.work.impl.utils.PackageManagerHelper;
import androidx.work.impl.utils.WorkForegroundRunnable;
import androidx.work.impl.utils.WorkForegroundUpdater;
import androidx.work.impl.utils.WorkProgressUpdater;
import androidx.work.impl.utils.futures.SettableFuture;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import mt.Log1F380D;

/* compiled from: 00B2 */
public class WorkerWrapper implements Runnable {
    static final String TAG;
    Context mAppContext;
    private Configuration mConfiguration;
    private DependencyDao mDependencyDao;
    private ForegroundProcessor mForegroundProcessor;
    SettableFuture<Boolean> mFuture = SettableFuture.create();
    ListenableFuture<ListenableWorker.Result> mInnerFuture = null;
    private volatile boolean mInterrupted;
    ListenableWorker.Result mResult = ListenableWorker.Result.failure();
    private WorkerParameters.RuntimeExtras mRuntimeExtras;
    private List<Scheduler> mSchedulers;
    private List<String> mTags;
    private WorkDatabase mWorkDatabase;
    private String mWorkDescription;
    WorkSpec mWorkSpec;
    private WorkSpecDao mWorkSpecDao;
    private String mWorkSpecId;
    private WorkTagDao mWorkTagDao;
    TaskExecutor mWorkTaskExecutor;
    ListenableWorker mWorker;

    public static class Builder {
        Context mAppContext;
        Configuration mConfiguration;
        ForegroundProcessor mForegroundProcessor;
        WorkerParameters.RuntimeExtras mRuntimeExtras = new WorkerParameters.RuntimeExtras();
        List<Scheduler> mSchedulers;
        WorkDatabase mWorkDatabase;
        String mWorkSpecId;
        TaskExecutor mWorkTaskExecutor;
        ListenableWorker mWorker;

        public Builder(Context context, Configuration configuration, TaskExecutor workTaskExecutor, ForegroundProcessor foregroundProcessor, WorkDatabase database, String workSpecId) {
            this.mAppContext = context.getApplicationContext();
            this.mWorkTaskExecutor = workTaskExecutor;
            this.mForegroundProcessor = foregroundProcessor;
            this.mConfiguration = configuration;
            this.mWorkDatabase = database;
            this.mWorkSpecId = workSpecId;
        }

        public WorkerWrapper build() {
            return new WorkerWrapper(this);
        }

        public Builder withRuntimeExtras(WorkerParameters.RuntimeExtras runtimeExtras) {
            if (runtimeExtras != null) {
                this.mRuntimeExtras = runtimeExtras;
            }
            return this;
        }

        public Builder withSchedulers(List<Scheduler> list) {
            this.mSchedulers = list;
            return this;
        }

        public Builder withWorker(ListenableWorker worker) {
            this.mWorker = worker;
            return this;
        }
    }

    static {
        String tagWithPrefix = Logger.tagWithPrefix("WorkerWrapper");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    WorkerWrapper(Builder builder) {
        this.mAppContext = builder.mAppContext;
        this.mWorkTaskExecutor = builder.mWorkTaskExecutor;
        this.mForegroundProcessor = builder.mForegroundProcessor;
        this.mWorkSpecId = builder.mWorkSpecId;
        this.mSchedulers = builder.mSchedulers;
        this.mRuntimeExtras = builder.mRuntimeExtras;
        this.mWorker = builder.mWorker;
        this.mConfiguration = builder.mConfiguration;
        WorkDatabase workDatabase = builder.mWorkDatabase;
        this.mWorkDatabase = workDatabase;
        this.mWorkSpecDao = workDatabase.workSpecDao();
        this.mDependencyDao = this.mWorkDatabase.dependencyDao();
        this.mWorkTagDao = this.mWorkDatabase.workTagDao();
    }

    private String createWorkDescription(List<String> list) {
        StringBuilder append = new StringBuilder("Work [ id=").append(this.mWorkSpecId).append(", tags={ ");
        boolean z = true;
        for (String next : list) {
            if (z) {
                z = false;
            } else {
                append.append(", ");
            }
            append.append(next);
        }
        append.append(" } ]");
        return append.toString();
    }

    private void iterativelyFailWorkAndDependents(String workSpecId) {
        LinkedList linkedList = new LinkedList();
        linkedList.add(workSpecId);
        while (!linkedList.isEmpty()) {
            String str = (String) linkedList.remove();
            if (this.mWorkSpecDao.getState(str) != WorkInfo.State.CANCELLED) {
                this.mWorkSpecDao.setState(WorkInfo.State.FAILED, str);
            }
            linkedList.addAll(this.mDependencyDao.getDependentWorkIds(str));
        }
    }

    private void rescheduleAndResolve() {
        this.mWorkDatabase.beginTransaction();
        try {
            this.mWorkSpecDao.setState(WorkInfo.State.ENQUEUED, this.mWorkSpecId);
            this.mWorkSpecDao.setPeriodStartTime(this.mWorkSpecId, System.currentTimeMillis());
            this.mWorkSpecDao.markWorkSpecScheduled(this.mWorkSpecId, -1);
            this.mWorkDatabase.setTransactionSuccessful();
        } finally {
            this.mWorkDatabase.endTransaction();
            resolve(true);
        }
    }

    private void resetPeriodicAndResolve() {
        this.mWorkDatabase.beginTransaction();
        try {
            this.mWorkSpecDao.setPeriodStartTime(this.mWorkSpecId, System.currentTimeMillis());
            this.mWorkSpecDao.setState(WorkInfo.State.ENQUEUED, this.mWorkSpecId);
            this.mWorkSpecDao.resetWorkSpecRunAttemptCount(this.mWorkSpecId);
            this.mWorkSpecDao.markWorkSpecScheduled(this.mWorkSpecId, -1);
            this.mWorkDatabase.setTransactionSuccessful();
        } finally {
            this.mWorkDatabase.endTransaction();
            resolve(false);
        }
    }

    /* JADX INFO: finally extract failed */
    private void resolve(boolean needsReschedule) {
        ListenableWorker listenableWorker;
        this.mWorkDatabase.beginTransaction();
        try {
            if (!this.mWorkDatabase.workSpecDao().hasUnfinishedWork()) {
                PackageManagerHelper.setComponentEnabled(this.mAppContext, RescheduleReceiver.class, false);
            }
            if (needsReschedule) {
                this.mWorkSpecDao.setState(WorkInfo.State.ENQUEUED, this.mWorkSpecId);
                this.mWorkSpecDao.markWorkSpecScheduled(this.mWorkSpecId, -1);
            }
            if (!(this.mWorkSpec == null || (listenableWorker = this.mWorker) == null || !listenableWorker.isRunInForeground())) {
                this.mForegroundProcessor.stopForeground(this.mWorkSpecId);
            }
            this.mWorkDatabase.setTransactionSuccessful();
            this.mWorkDatabase.endTransaction();
            this.mFuture.set(Boolean.valueOf(needsReschedule));
        } catch (Throwable th) {
            this.mWorkDatabase.endTransaction();
            throw th;
        }
    }

    private boolean trySetRunning() {
        boolean z = false;
        this.mWorkDatabase.beginTransaction();
        try {
            if (this.mWorkSpecDao.getState(this.mWorkSpecId) == WorkInfo.State.ENQUEUED) {
                this.mWorkSpecDao.setState(WorkInfo.State.RUNNING, this.mWorkSpecId);
                this.mWorkSpecDao.incrementWorkSpecRunAttemptCount(this.mWorkSpecId);
                z = true;
            }
            this.mWorkDatabase.setTransactionSuccessful();
            return z;
        } finally {
            this.mWorkDatabase.endTransaction();
        }
    }

    public ListenableFuture<Boolean> getFuture() {
        return this.mFuture;
    }

    /* access modifiers changed from: package-private */
    public void onWorkFinished() {
        if (!tryCheckForInterruptionAndResolve()) {
            this.mWorkDatabase.beginTransaction();
            try {
                WorkInfo.State state = this.mWorkSpecDao.getState(this.mWorkSpecId);
                this.mWorkDatabase.workProgressDao().delete(this.mWorkSpecId);
                if (state == null) {
                    resolve(false);
                } else if (state == WorkInfo.State.RUNNING) {
                    handleResult(this.mResult);
                } else if (!state.isFinished()) {
                    rescheduleAndResolve();
                }
                this.mWorkDatabase.setTransactionSuccessful();
            } finally {
                this.mWorkDatabase.endTransaction();
            }
        }
        List<Scheduler> list = this.mSchedulers;
        if (list != null) {
            for (Scheduler cancel : list) {
                cancel.cancel(this.mWorkSpecId);
            }
            Schedulers.schedule(this.mConfiguration, this.mWorkDatabase, this.mSchedulers);
        }
    }

    public void run() {
        List<String> tagsForWorkSpecId = this.mWorkTagDao.getTagsForWorkSpecId(this.mWorkSpecId);
        this.mTags = tagsForWorkSpecId;
        this.mWorkDescription = createWorkDescription(tagsForWorkSpecId);
        runWorker();
    }

    /* access modifiers changed from: package-private */
    public void setFailedAndResolve() {
        this.mWorkDatabase.beginTransaction();
        try {
            iterativelyFailWorkAndDependents(this.mWorkSpecId);
            this.mWorkSpecDao.setOutput(this.mWorkSpecId, ((ListenableWorker.Result.Failure) this.mResult).getOutputData());
            this.mWorkDatabase.setTransactionSuccessful();
        } finally {
            this.mWorkDatabase.endTransaction();
            resolve(false);
        }
    }

    private void handleResult(ListenableWorker.Result result) {
        if (result instanceof ListenableWorker.Result.Success) {
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Worker result SUCCESS for %s", new Object[]{this.mWorkDescription});
            Log1F380D.a((Object) format);
            logger.info(str, format, new Throwable[0]);
            if (this.mWorkSpec.isPeriodic()) {
                resetPeriodicAndResolve();
            } else {
                setSucceededAndResolve();
            }
        } else if (result instanceof ListenableWorker.Result.Retry) {
            Logger logger2 = Logger.get();
            String str2 = TAG;
            String format2 = String.format("Worker result RETRY for %s", new Object[]{this.mWorkDescription});
            Log1F380D.a((Object) format2);
            logger2.info(str2, format2, new Throwable[0]);
            rescheduleAndResolve();
        } else {
            Logger logger3 = Logger.get();
            String str3 = TAG;
            String format3 = String.format("Worker result FAILURE for %s", new Object[]{this.mWorkDescription});
            Log1F380D.a((Object) format3);
            logger3.info(str3, format3, new Throwable[0]);
            if (this.mWorkSpec.isPeriodic()) {
                resetPeriodicAndResolve();
            } else {
                setFailedAndResolve();
            }
        }
    }

    private void resolveIncorrectStatus() {
        WorkInfo.State state = this.mWorkSpecDao.getState(this.mWorkSpecId);
        if (state == WorkInfo.State.RUNNING) {
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Status for %s is RUNNING;not doing any work and rescheduling for later execution", new Object[]{this.mWorkSpecId});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            resolve(true);
            return;
        }
        Logger logger2 = Logger.get();
        String str2 = TAG;
        String format2 = String.format("Status for %s is %s; not doing any work", new Object[]{this.mWorkSpecId, state});
        Log1F380D.a((Object) format2);
        logger2.debug(str2, format2, new Throwable[0]);
        resolve(false);
    }

    private void runWorker() {
        Data data;
        if (!tryCheckForInterruptionAndResolve()) {
            this.mWorkDatabase.beginTransaction();
            try {
                WorkSpec workSpec = this.mWorkSpecDao.getWorkSpec(this.mWorkSpecId);
                this.mWorkSpec = workSpec;
                if (workSpec == null) {
                    Logger logger = Logger.get();
                    String str = TAG;
                    String format = String.format("Didn't find WorkSpec for id %s", new Object[]{this.mWorkSpecId});
                    Log1F380D.a((Object) format);
                    logger.error(str, format, new Throwable[0]);
                    resolve(false);
                    this.mWorkDatabase.setTransactionSuccessful();
                } else if (workSpec.state != WorkInfo.State.ENQUEUED) {
                    resolveIncorrectStatus();
                    this.mWorkDatabase.setTransactionSuccessful();
                    Logger logger2 = Logger.get();
                    String str2 = TAG;
                    String format2 = String.format("%s is not in ENQUEUED state. Nothing more to do.", new Object[]{this.mWorkSpec.workerClassName});
                    Log1F380D.a((Object) format2);
                    logger2.debug(str2, format2, new Throwable[0]);
                    this.mWorkDatabase.endTransaction();
                } else {
                    if (this.mWorkSpec.isPeriodic() || this.mWorkSpec.isBackedOff()) {
                        long currentTimeMillis = System.currentTimeMillis();
                        if (!(this.mWorkSpec.periodStartTime == 0) && currentTimeMillis < this.mWorkSpec.calculateNextRunTime()) {
                            Logger logger3 = Logger.get();
                            String str3 = TAG;
                            String format3 = String.format("Delaying execution for %s because it is being executed before schedule.", new Object[]{this.mWorkSpec.workerClassName});
                            Log1F380D.a((Object) format3);
                            logger3.debug(str3, format3, new Throwable[0]);
                            resolve(true);
                            this.mWorkDatabase.setTransactionSuccessful();
                            this.mWorkDatabase.endTransaction();
                            return;
                        }
                    }
                    this.mWorkDatabase.setTransactionSuccessful();
                    this.mWorkDatabase.endTransaction();
                    if (this.mWorkSpec.isPeriodic()) {
                        data = this.mWorkSpec.input;
                    } else {
                        InputMerger createInputMergerWithDefaultFallback = this.mConfiguration.getInputMergerFactory().createInputMergerWithDefaultFallback(this.mWorkSpec.inputMergerClassName);
                        if (createInputMergerWithDefaultFallback == null) {
                            Logger logger4 = Logger.get();
                            String str4 = TAG;
                            String format4 = String.format("Could not create Input Merger %s", new Object[]{this.mWorkSpec.inputMergerClassName});
                            Log1F380D.a((Object) format4);
                            logger4.error(str4, format4, new Throwable[0]);
                            setFailedAndResolve();
                            return;
                        }
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(this.mWorkSpec.input);
                        arrayList.addAll(this.mWorkSpecDao.getInputsFromPrerequisites(this.mWorkSpecId));
                        data = createInputMergerWithDefaultFallback.merge(arrayList);
                    }
                    WorkerParameters workerParameters = new WorkerParameters(UUID.fromString(this.mWorkSpecId), data, this.mTags, this.mRuntimeExtras, this.mWorkSpec.runAttemptCount, this.mConfiguration.getExecutor(), this.mWorkTaskExecutor, this.mConfiguration.getWorkerFactory(), new WorkProgressUpdater(this.mWorkDatabase, this.mWorkTaskExecutor), new WorkForegroundUpdater(this.mWorkDatabase, this.mForegroundProcessor, this.mWorkTaskExecutor));
                    if (this.mWorker == null) {
                        this.mWorker = this.mConfiguration.getWorkerFactory().createWorkerWithDefaultFallback(this.mAppContext, this.mWorkSpec.workerClassName, workerParameters);
                    }
                    ListenableWorker listenableWorker = this.mWorker;
                    if (listenableWorker == null) {
                        Logger logger5 = Logger.get();
                        String str5 = TAG;
                        String format5 = String.format("Could not create Worker %s", new Object[]{this.mWorkSpec.workerClassName});
                        Log1F380D.a((Object) format5);
                        logger5.error(str5, format5, new Throwable[0]);
                        setFailedAndResolve();
                    } else if (listenableWorker.isUsed()) {
                        Logger logger6 = Logger.get();
                        String str6 = TAG;
                        String format6 = String.format("Received an already-used Worker %s; WorkerFactory should return new instances", new Object[]{this.mWorkSpec.workerClassName});
                        Log1F380D.a((Object) format6);
                        logger6.error(str6, format6, new Throwable[0]);
                        setFailedAndResolve();
                    } else {
                        this.mWorker.setUsed();
                        if (!trySetRunning()) {
                            resolveIncorrectStatus();
                        } else if (!tryCheckForInterruptionAndResolve()) {
                            final SettableFuture create = SettableFuture.create();
                            WorkForegroundRunnable workForegroundRunnable = new WorkForegroundRunnable(this.mAppContext, this.mWorkSpec, this.mWorker, workerParameters.getForegroundUpdater(), this.mWorkTaskExecutor);
                            this.mWorkTaskExecutor.getMainThreadExecutor().execute(workForegroundRunnable);
                            final ListenableFuture<Void> future = workForegroundRunnable.getFuture();
                            future.addListener(new Runnable() {
                                public void run() {
                                    try {
                                        future.get();
                                        Logger logger = Logger.get();
                                        String str = WorkerWrapper.TAG;
                                        String format = String.format("Starting work for %s", new Object[]{WorkerWrapper.this.mWorkSpec.workerClassName});
                                        Log1F380D.a((Object) format);
                                        logger.debug(str, format, new Throwable[0]);
                                        WorkerWrapper workerWrapper = WorkerWrapper.this;
                                        workerWrapper.mInnerFuture = workerWrapper.mWorker.startWork();
                                        create.setFuture(WorkerWrapper.this.mInnerFuture);
                                    } catch (Throwable th) {
                                        create.setException(th);
                                    }
                                }
                            }, this.mWorkTaskExecutor.getMainThreadExecutor());
                            final String str7 = this.mWorkDescription;
                            create.addListener(new Runnable() {
                                public void run() {
                                    try {
                                        ListenableWorker.Result result = (ListenableWorker.Result) create.get();
                                        if (result == null) {
                                            Logger logger = Logger.get();
                                            String str = WorkerWrapper.TAG;
                                            String format = String.format("%s returned a null result. Treating it as a failure.", new Object[]{WorkerWrapper.this.mWorkSpec.workerClassName});
                                            Log1F380D.a((Object) format);
                                            logger.error(str, format, new Throwable[0]);
                                            WorkerWrapper.this.onWorkFinished();
                                        }
                                        Logger logger2 = Logger.get();
                                        String str2 = WorkerWrapper.TAG;
                                        String format2 = String.format("%s returned a %s result.", new Object[]{WorkerWrapper.this.mWorkSpec.workerClassName, result});
                                        Log1F380D.a((Object) format2);
                                        logger2.debug(str2, format2, new Throwable[0]);
                                        WorkerWrapper.this.mResult = result;
                                        WorkerWrapper.this.onWorkFinished();
                                    } catch (CancellationException e) {
                                        Logger logger3 = Logger.get();
                                        String str3 = WorkerWrapper.TAG;
                                        String format3 = String.format("%s was cancelled", new Object[]{str7});
                                        Log1F380D.a((Object) format3);
                                        logger3.info(str3, format3, e);
                                    } catch (InterruptedException | ExecutionException e2) {
                                        Logger logger4 = Logger.get();
                                        String str4 = WorkerWrapper.TAG;
                                        String format4 = String.format("%s failed because it threw an exception/error", new Object[]{str7});
                                        Log1F380D.a((Object) format4);
                                        logger4.error(str4, format4, e2);
                                    } catch (Throwable th) {
                                        WorkerWrapper.this.onWorkFinished();
                                        throw th;
                                    }
                                }
                            }, this.mWorkTaskExecutor.getBackgroundExecutor());
                        }
                    }
                }
            } finally {
                this.mWorkDatabase.endTransaction();
            }
        }
    }

    private void setSucceededAndResolve() {
        this.mWorkDatabase.beginTransaction();
        try {
            this.mWorkSpecDao.setState(WorkInfo.State.SUCCEEDED, this.mWorkSpecId);
            this.mWorkSpecDao.setOutput(this.mWorkSpecId, ((ListenableWorker.Result.Success) this.mResult).getOutputData());
            long currentTimeMillis = System.currentTimeMillis();
            for (String next : this.mDependencyDao.getDependentWorkIds(this.mWorkSpecId)) {
                if (this.mWorkSpecDao.getState(next) == WorkInfo.State.BLOCKED && this.mDependencyDao.hasCompletedAllPrerequisites(next)) {
                    Logger logger = Logger.get();
                    String str = TAG;
                    String format = String.format("Setting status to enqueued for %s", new Object[]{next});
                    Log1F380D.a((Object) format);
                    logger.info(str, format, new Throwable[0]);
                    this.mWorkSpecDao.setState(WorkInfo.State.ENQUEUED, next);
                    this.mWorkSpecDao.setPeriodStartTime(next, currentTimeMillis);
                }
            }
            this.mWorkDatabase.setTransactionSuccessful();
        } finally {
            this.mWorkDatabase.endTransaction();
            resolve(false);
        }
    }

    private boolean tryCheckForInterruptionAndResolve() {
        if (!this.mInterrupted) {
            return false;
        }
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("Work interrupted for %s", new Object[]{this.mWorkDescription});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        WorkInfo.State state = this.mWorkSpecDao.getState(this.mWorkSpecId);
        if (state == null) {
            resolve(false);
        } else {
            resolve(!state.isFinished());
        }
        return true;
    }

    public void interrupt() {
        this.mInterrupted = true;
        tryCheckForInterruptionAndResolve();
        boolean z = false;
        ListenableFuture<ListenableWorker.Result> listenableFuture = this.mInnerFuture;
        if (listenableFuture != null) {
            z = listenableFuture.isDone();
            this.mInnerFuture.cancel(true);
        }
        ListenableWorker listenableWorker = this.mWorker;
        if (listenableWorker == null || z) {
            String format = String.format("WorkSpec %s is already done. Not interrupting.", new Object[]{this.mWorkSpec});
            Log1F380D.a((Object) format);
            Logger.get().debug(TAG, format, new Throwable[0]);
            return;
        }
        listenableWorker.stop();
    }
}

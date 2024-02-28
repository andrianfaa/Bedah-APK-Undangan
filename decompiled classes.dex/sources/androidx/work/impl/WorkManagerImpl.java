package androidx.work.impl;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import androidx.arch.core.util.Function;
import androidx.core.os.BuildCompat;
import androidx.lifecycle.LiveData;
import androidx.work.Configuration;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.Logger;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.PeriodicWorkRequest;
import androidx.work.R;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkQuery;
import androidx.work.WorkRequest;
import androidx.work.WorkerParameters;
import androidx.work.impl.background.greedy.GreedyScheduler;
import androidx.work.impl.background.systemjob.SystemJobScheduler;
import androidx.work.impl.foreground.SystemForegroundDispatcher;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.utils.CancelWorkRunnable;
import androidx.work.impl.utils.ForceStopRunnable;
import androidx.work.impl.utils.LiveDataUtils;
import androidx.work.impl.utils.PreferenceUtils;
import androidx.work.impl.utils.PruneWorkRunnable;
import androidx.work.impl.utils.RawQueries;
import androidx.work.impl.utils.StartWorkRunnable;
import androidx.work.impl.utils.StatusRunnable;
import androidx.work.impl.utils.StopWorkRunnable;
import androidx.work.impl.utils.futures.SettableFuture;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import androidx.work.impl.utils.taskexecutor.WorkManagerTaskExecutor;
import androidx.work.multiprocess.RemoteWorkManager;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import mt.Log1F380D;

/* compiled from: 00AF */
public class WorkManagerImpl extends WorkManager {
    public static final int MAX_PRE_JOB_SCHEDULER_API_LEVEL = 22;
    public static final int MIN_JOB_SCHEDULER_API_LEVEL = 23;
    public static final String REMOTE_WORK_MANAGER_CLIENT = "androidx.work.multiprocess.RemoteWorkManagerClient";
    private static final String TAG;
    private static WorkManagerImpl sDefaultInstance = null;
    private static WorkManagerImpl sDelegatedInstance = null;
    private static final Object sLock = new Object();
    private Configuration mConfiguration;
    private Context mContext;
    private boolean mForceStopRunnableCompleted;
    private PreferenceUtils mPreferenceUtils;
    private Processor mProcessor;
    private volatile RemoteWorkManager mRemoteWorkManager;
    private BroadcastReceiver.PendingResult mRescheduleReceiverResult;
    private List<Scheduler> mSchedulers;
    private WorkDatabase mWorkDatabase;
    private TaskExecutor mWorkTaskExecutor;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("WorkManagerImpl");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public WorkManagerImpl(Context context, Configuration configuration, TaskExecutor workTaskExecutor) {
        this(context, configuration, workTaskExecutor, context.getResources().getBoolean(R.bool.workmanager_test_configuration));
    }

    public WorkManagerImpl(Context context, Configuration configuration, TaskExecutor workTaskExecutor, WorkDatabase database) {
        Context applicationContext = context.getApplicationContext();
        Logger.setLogger(new Logger.LogcatLogger(configuration.getMinimumLoggingLevel()));
        Context context2 = context;
        Configuration configuration2 = configuration;
        TaskExecutor taskExecutor = workTaskExecutor;
        WorkDatabase workDatabase = database;
        List<Scheduler> createSchedulers = createSchedulers(applicationContext, configuration, workTaskExecutor);
        internalInit(context2, configuration2, taskExecutor, workDatabase, createSchedulers, new Processor(context2, configuration2, taskExecutor, workDatabase, createSchedulers));
    }

    public WorkManagerImpl(Context context, Configuration configuration, TaskExecutor workTaskExecutor, WorkDatabase workDatabase, List<Scheduler> list, Processor processor) {
        internalInit(context, configuration, workTaskExecutor, workDatabase, list, processor);
    }

    public WorkManagerImpl(Context context, Configuration configuration, TaskExecutor workTaskExecutor, boolean useTestDatabase) {
        this(context, configuration, workTaskExecutor, WorkDatabase.create(context.getApplicationContext(), workTaskExecutor.getBackgroundExecutor(), useTestDatabase));
    }

    @Deprecated
    public static WorkManagerImpl getInstance() {
        synchronized (sLock) {
            WorkManagerImpl workManagerImpl = sDelegatedInstance;
            if (workManagerImpl != null) {
                return workManagerImpl;
            }
            WorkManagerImpl workManagerImpl2 = sDefaultInstance;
            return workManagerImpl2;
        }
    }

    public static WorkManagerImpl getInstance(Context context) {
        WorkManagerImpl instance;
        synchronized (sLock) {
            instance = getInstance();
            if (instance == null) {
                Context applicationContext = context.getApplicationContext();
                if (applicationContext instanceof Configuration.Provider) {
                    initialize(applicationContext, ((Configuration.Provider) applicationContext).getWorkManagerConfiguration());
                    instance = getInstance(applicationContext);
                } else {
                    throw new IllegalStateException("WorkManager is not initialized properly.  You have explicitly disabled WorkManagerInitializer in your manifest, have not manually called WorkManager#initialize at this point, and your Application does not implement Configuration.Provider.");
                }
            }
        }
        return instance;
    }

    public static void initialize(Context context, Configuration configuration) {
        synchronized (sLock) {
            WorkManagerImpl workManagerImpl = sDelegatedInstance;
            if (workManagerImpl != null) {
                if (sDefaultInstance != null) {
                    throw new IllegalStateException("WorkManager is already initialized.  Did you try to initialize it manually without disabling WorkManagerInitializer? See WorkManager#initialize(Context, Configuration) or the class level Javadoc for more information.");
                }
            }
            if (workManagerImpl == null) {
                Context context2 = context.getApplicationContext();
                if (sDefaultInstance == null) {
                    sDefaultInstance = new WorkManagerImpl(context2, configuration, new WorkManagerTaskExecutor(configuration.getTaskExecutor()));
                }
                sDelegatedInstance = sDefaultInstance;
            }
        }
    }

    private void internalInit(Context context, Configuration configuration, TaskExecutor workTaskExecutor, WorkDatabase workDatabase, List<Scheduler> list, Processor processor) {
        Context context2 = context.getApplicationContext();
        this.mContext = context2;
        this.mConfiguration = configuration;
        this.mWorkTaskExecutor = workTaskExecutor;
        this.mWorkDatabase = workDatabase;
        this.mSchedulers = list;
        this.mProcessor = processor;
        this.mPreferenceUtils = new PreferenceUtils(workDatabase);
        this.mForceStopRunnableCompleted = false;
        if (Build.VERSION.SDK_INT < 24 || !context2.isDeviceProtectedStorage()) {
            this.mWorkTaskExecutor.executeOnBackgroundThread(new ForceStopRunnable(context2, this));
            return;
        }
        throw new IllegalStateException("Cannot initialize WorkManager in direct boot mode");
    }

    public static void setDelegate(WorkManagerImpl delegate) {
        synchronized (sLock) {
            sDelegatedInstance = delegate;
        }
    }

    private void tryInitializeMultiProcessSupport() {
        try {
            this.mRemoteWorkManager = (RemoteWorkManager) Class.forName(REMOTE_WORK_MANAGER_CLIENT).getConstructor(new Class[]{Context.class, WorkManagerImpl.class}).newInstance(new Object[]{this.mContext, this});
        } catch (Throwable th) {
            Logger.get().debug(TAG, "Unable to initialize multi-process support", th);
        }
    }

    public WorkContinuation beginUniqueWork(String uniqueWorkName, ExistingWorkPolicy existingWorkPolicy, List<OneTimeWorkRequest> list) {
        if (!list.isEmpty()) {
            return new WorkContinuationImpl(this, uniqueWorkName, existingWorkPolicy, list);
        }
        throw new IllegalArgumentException("beginUniqueWork needs at least one OneTimeWorkRequest.");
    }

    public WorkContinuation beginWith(List<OneTimeWorkRequest> list) {
        if (!list.isEmpty()) {
            return new WorkContinuationImpl(this, list);
        }
        throw new IllegalArgumentException("beginWith needs at least one OneTimeWorkRequest.");
    }

    public Operation cancelAllWork() {
        CancelWorkRunnable forAll = CancelWorkRunnable.forAll(this);
        this.mWorkTaskExecutor.executeOnBackgroundThread(forAll);
        return forAll.getOperation();
    }

    public Operation cancelAllWorkByTag(String tag) {
        CancelWorkRunnable forTag = CancelWorkRunnable.forTag(tag, this);
        this.mWorkTaskExecutor.executeOnBackgroundThread(forTag);
        return forTag.getOperation();
    }

    public Operation cancelUniqueWork(String uniqueWorkName) {
        CancelWorkRunnable forName = CancelWorkRunnable.forName(uniqueWorkName, this, true);
        this.mWorkTaskExecutor.executeOnBackgroundThread(forName);
        return forName.getOperation();
    }

    public Operation cancelWorkById(UUID id) {
        CancelWorkRunnable forId = CancelWorkRunnable.forId(id, this);
        this.mWorkTaskExecutor.executeOnBackgroundThread(forId);
        return forId.getOperation();
    }

    public PendingIntent createCancelPendingIntent(UUID id) {
        Intent createCancelWorkIntent = SystemForegroundDispatcher.createCancelWorkIntent(this.mContext, id.toString());
        int i = 134217728;
        if (BuildCompat.isAtLeastS()) {
            i = 134217728 | 33554432;
        }
        return PendingIntent.getService(this.mContext, 0, createCancelWorkIntent, i);
    }

    public List<Scheduler> createSchedulers(Context context, Configuration configuration, TaskExecutor taskExecutor) {
        return Arrays.asList(new Scheduler[]{Schedulers.createBestAvailableBackgroundScheduler(context, this), new GreedyScheduler(context, configuration, taskExecutor, this)});
    }

    public WorkContinuationImpl createWorkContinuationForUniquePeriodicWork(String uniqueWorkName, ExistingPeriodicWorkPolicy existingPeriodicWorkPolicy, PeriodicWorkRequest periodicWork) {
        return new WorkContinuationImpl(this, uniqueWorkName, existingPeriodicWorkPolicy == ExistingPeriodicWorkPolicy.KEEP ? ExistingWorkPolicy.KEEP : ExistingWorkPolicy.REPLACE, Collections.singletonList(periodicWork));
    }

    public Operation enqueue(List<? extends WorkRequest> list) {
        if (!list.isEmpty()) {
            return new WorkContinuationImpl(this, list).enqueue();
        }
        throw new IllegalArgumentException("enqueue needs at least one WorkRequest.");
    }

    public Operation enqueueUniquePeriodicWork(String uniqueWorkName, ExistingPeriodicWorkPolicy existingPeriodicWorkPolicy, PeriodicWorkRequest periodicWork) {
        return createWorkContinuationForUniquePeriodicWork(uniqueWorkName, existingPeriodicWorkPolicy, periodicWork).enqueue();
    }

    public Operation enqueueUniqueWork(String uniqueWorkName, ExistingWorkPolicy existingWorkPolicy, List<OneTimeWorkRequest> list) {
        return new WorkContinuationImpl(this, uniqueWorkName, existingWorkPolicy, list).enqueue();
    }

    public Context getApplicationContext() {
        return this.mContext;
    }

    public Configuration getConfiguration() {
        return this.mConfiguration;
    }

    public ListenableFuture<Long> getLastCancelAllTimeMillis() {
        final SettableFuture create = SettableFuture.create();
        final PreferenceUtils preferenceUtils = this.mPreferenceUtils;
        this.mWorkTaskExecutor.executeOnBackgroundThread(new Runnable() {
            public void run() {
                try {
                    create.set(Long.valueOf(preferenceUtils.getLastCancelAllTimeMillis()));
                } catch (Throwable th) {
                    create.setException(th);
                }
            }
        });
        return create;
    }

    public LiveData<Long> getLastCancelAllTimeMillisLiveData() {
        return this.mPreferenceUtils.getLastCancelAllTimeMillisLiveData();
    }

    public PreferenceUtils getPreferenceUtils() {
        return this.mPreferenceUtils;
    }

    public Processor getProcessor() {
        return this.mProcessor;
    }

    public RemoteWorkManager getRemoteWorkManager() {
        if (this.mRemoteWorkManager == null) {
            synchronized (sLock) {
                if (this.mRemoteWorkManager == null) {
                    tryInitializeMultiProcessSupport();
                    if (this.mRemoteWorkManager == null) {
                        if (!TextUtils.isEmpty(this.mConfiguration.getDefaultProcessName())) {
                            throw new IllegalStateException("Invalid multiprocess configuration. Define an `implementation` dependency on :work:work-multiprocess library");
                        }
                    }
                }
            }
        }
        return this.mRemoteWorkManager;
    }

    public List<Scheduler> getSchedulers() {
        return this.mSchedulers;
    }

    public WorkDatabase getWorkDatabase() {
        return this.mWorkDatabase;
    }

    public ListenableFuture<WorkInfo> getWorkInfoById(UUID id) {
        StatusRunnable<WorkInfo> forUUID = StatusRunnable.forUUID(this, id);
        this.mWorkTaskExecutor.getBackgroundExecutor().execute(forUUID);
        return forUUID.getFuture();
    }

    public LiveData<WorkInfo> getWorkInfoByIdLiveData(UUID id) {
        return LiveDataUtils.dedupedMappedLiveDataFor(this.mWorkDatabase.workSpecDao().getWorkStatusPojoLiveDataForIds(Collections.singletonList(id.toString())), new Function<List<WorkSpec.WorkInfoPojo>, WorkInfo>() {
            public WorkInfo apply(List<WorkSpec.WorkInfoPojo> list) {
                if (list == null || list.size() <= 0) {
                    return null;
                }
                return list.get(0).toWorkInfo();
            }
        }, this.mWorkTaskExecutor);
    }

    public ListenableFuture<List<WorkInfo>> getWorkInfos(WorkQuery workQuery) {
        StatusRunnable<List<WorkInfo>> forWorkQuerySpec = StatusRunnable.forWorkQuerySpec(this, workQuery);
        this.mWorkTaskExecutor.getBackgroundExecutor().execute(forWorkQuerySpec);
        return forWorkQuerySpec.getFuture();
    }

    /* access modifiers changed from: package-private */
    public LiveData<List<WorkInfo>> getWorkInfosById(List<String> list) {
        return LiveDataUtils.dedupedMappedLiveDataFor(this.mWorkDatabase.workSpecDao().getWorkStatusPojoLiveDataForIds(list), WorkSpec.WORK_INFO_MAPPER, this.mWorkTaskExecutor);
    }

    public ListenableFuture<List<WorkInfo>> getWorkInfosByTag(String tag) {
        StatusRunnable<List<WorkInfo>> forTag = StatusRunnable.forTag(this, tag);
        this.mWorkTaskExecutor.getBackgroundExecutor().execute(forTag);
        return forTag.getFuture();
    }

    public LiveData<List<WorkInfo>> getWorkInfosByTagLiveData(String tag) {
        return LiveDataUtils.dedupedMappedLiveDataFor(this.mWorkDatabase.workSpecDao().getWorkStatusPojoLiveDataForTag(tag), WorkSpec.WORK_INFO_MAPPER, this.mWorkTaskExecutor);
    }

    public ListenableFuture<List<WorkInfo>> getWorkInfosForUniqueWork(String uniqueWorkName) {
        StatusRunnable<List<WorkInfo>> forUniqueWork = StatusRunnable.forUniqueWork(this, uniqueWorkName);
        this.mWorkTaskExecutor.getBackgroundExecutor().execute(forUniqueWork);
        return forUniqueWork.getFuture();
    }

    public LiveData<List<WorkInfo>> getWorkInfosForUniqueWorkLiveData(String uniqueWorkName) {
        return LiveDataUtils.dedupedMappedLiveDataFor(this.mWorkDatabase.workSpecDao().getWorkStatusPojoLiveDataForName(uniqueWorkName), WorkSpec.WORK_INFO_MAPPER, this.mWorkTaskExecutor);
    }

    public LiveData<List<WorkInfo>> getWorkInfosLiveData(WorkQuery workQuery) {
        return LiveDataUtils.dedupedMappedLiveDataFor(this.mWorkDatabase.rawWorkInfoDao().getWorkInfoPojosLiveData(RawQueries.workQueryToRawQuery(workQuery)), WorkSpec.WORK_INFO_MAPPER, this.mWorkTaskExecutor);
    }

    public TaskExecutor getWorkTaskExecutor() {
        return this.mWorkTaskExecutor;
    }

    public void onForceStopRunnableCompleted() {
        synchronized (sLock) {
            this.mForceStopRunnableCompleted = true;
            BroadcastReceiver.PendingResult pendingResult = this.mRescheduleReceiverResult;
            if (pendingResult != null) {
                pendingResult.finish();
                this.mRescheduleReceiverResult = null;
            }
        }
    }

    public Operation pruneWork() {
        PruneWorkRunnable pruneWorkRunnable = new PruneWorkRunnable(this);
        this.mWorkTaskExecutor.executeOnBackgroundThread(pruneWorkRunnable);
        return pruneWorkRunnable.getOperation();
    }

    public void rescheduleEligibleWork() {
        if (Build.VERSION.SDK_INT >= 23) {
            SystemJobScheduler.cancelAll(getApplicationContext());
        }
        getWorkDatabase().workSpecDao().resetScheduledState();
        Schedulers.schedule(getConfiguration(), getWorkDatabase(), getSchedulers());
    }

    public void setReschedulePendingResult(BroadcastReceiver.PendingResult rescheduleReceiverResult) {
        synchronized (sLock) {
            this.mRescheduleReceiverResult = rescheduleReceiverResult;
            if (this.mForceStopRunnableCompleted) {
                rescheduleReceiverResult.finish();
                this.mRescheduleReceiverResult = null;
            }
        }
    }

    public void startWork(String workSpecId) {
        startWork(workSpecId, (WorkerParameters.RuntimeExtras) null);
    }

    public void startWork(String workSpecId, WorkerParameters.RuntimeExtras runtimeExtras) {
        this.mWorkTaskExecutor.executeOnBackgroundThread(new StartWorkRunnable(this, workSpecId, runtimeExtras));
    }

    public void stopForegroundWork(String workSpecId) {
        this.mWorkTaskExecutor.executeOnBackgroundThread(new StopWorkRunnable(this, workSpecId, true));
    }

    public void stopWork(String workSpecId) {
        this.mWorkTaskExecutor.executeOnBackgroundThread(new StopWorkRunnable(this, workSpecId, false));
    }
}

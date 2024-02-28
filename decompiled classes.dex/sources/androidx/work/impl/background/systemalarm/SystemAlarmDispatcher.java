package androidx.work.impl.background.systemalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.text.TextUtils;
import androidx.work.Logger;
import androidx.work.impl.ExecutionListener;
import androidx.work.impl.Processor;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.utils.SerialExecutor;
import androidx.work.impl.utils.WakeLocks;
import androidx.work.impl.utils.WorkTimer;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import java.util.ArrayList;
import java.util.List;
import mt.Log1F380D;

/* compiled from: 00BF */
public class SystemAlarmDispatcher implements ExecutionListener {
    private static final int DEFAULT_START_ID = 0;
    private static final String KEY_START_ID = "KEY_START_ID";
    private static final String PROCESS_COMMAND_TAG = "ProcessCommand";
    static final String TAG;
    final CommandHandler mCommandHandler;
    private CommandsCompletedListener mCompletedListener;
    final Context mContext;
    Intent mCurrentIntent;
    final List<Intent> mIntents;
    private final Handler mMainHandler;
    private final Processor mProcessor;
    private final TaskExecutor mTaskExecutor;
    private final WorkManagerImpl mWorkManager;
    private final WorkTimer mWorkTimer;

    static class AddRunnable implements Runnable {
        private final SystemAlarmDispatcher mDispatcher;
        private final Intent mIntent;
        private final int mStartId;

        AddRunnable(SystemAlarmDispatcher dispatcher, Intent intent, int startId) {
            this.mDispatcher = dispatcher;
            this.mIntent = intent;
            this.mStartId = startId;
        }

        public void run() {
            this.mDispatcher.add(this.mIntent, this.mStartId);
        }
    }

    interface CommandsCompletedListener {
        void onAllCommandsCompleted();
    }

    static class DequeueAndCheckForCompletion implements Runnable {
        private final SystemAlarmDispatcher mDispatcher;

        DequeueAndCheckForCompletion(SystemAlarmDispatcher dispatcher) {
            this.mDispatcher = dispatcher;
        }

        public void run() {
            this.mDispatcher.dequeueAndCheckForCompletion();
        }
    }

    static {
        String tagWithPrefix = Logger.tagWithPrefix("SystemAlarmDispatcher");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    SystemAlarmDispatcher(Context context) {
        this(context, (Processor) null, (WorkManagerImpl) null);
    }

    SystemAlarmDispatcher(Context context, Processor processor, WorkManagerImpl workManager) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mCommandHandler = new CommandHandler(applicationContext);
        this.mWorkTimer = new WorkTimer();
        WorkManagerImpl instance = workManager != null ? workManager : WorkManagerImpl.getInstance(context);
        this.mWorkManager = instance;
        Processor processor2 = processor != null ? processor : instance.getProcessor();
        this.mProcessor = processor2;
        this.mTaskExecutor = instance.getWorkTaskExecutor();
        processor2.addExecutionListener(this);
        this.mIntents = new ArrayList();
        this.mCurrentIntent = null;
        this.mMainHandler = new Handler(Looper.getMainLooper());
    }

    private void assertMainThread() {
        if (this.mMainHandler.getLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Needs to be invoked on the main thread.");
        }
    }

    private boolean hasIntentWithAction(String action) {
        assertMainThread();
        synchronized (this.mIntents) {
            for (Intent action2 : this.mIntents) {
                if (action.equals(action2.getAction())) {
                    return true;
                }
            }
            return false;
        }
    }

    private void processCommand() {
        assertMainThread();
        PowerManager.WakeLock newWakeLock = WakeLocks.newWakeLock(this.mContext, PROCESS_COMMAND_TAG);
        try {
            newWakeLock.acquire();
            this.mWorkManager.getWorkTaskExecutor().executeOnBackgroundThread(new Runnable() {
                public void run() {
                    DequeueAndCheckForCompletion dequeueAndCheckForCompletion;
                    SystemAlarmDispatcher systemAlarmDispatcher;
                    synchronized (SystemAlarmDispatcher.this.mIntents) {
                        SystemAlarmDispatcher systemAlarmDispatcher2 = SystemAlarmDispatcher.this;
                        systemAlarmDispatcher2.mCurrentIntent = systemAlarmDispatcher2.mIntents.get(0);
                    }
                    if (SystemAlarmDispatcher.this.mCurrentIntent != null) {
                        String action = SystemAlarmDispatcher.this.mCurrentIntent.getAction();
                        int intExtra = SystemAlarmDispatcher.this.mCurrentIntent.getIntExtra(SystemAlarmDispatcher.KEY_START_ID, 0);
                        Logger logger = Logger.get();
                        String str = SystemAlarmDispatcher.TAG;
                        String format = String.format("Processing command %s, %s", new Object[]{SystemAlarmDispatcher.this.mCurrentIntent, Integer.valueOf(intExtra)});
                        Log1F380D.a((Object) format);
                        logger.debug(str, format, new Throwable[0]);
                        Context context = SystemAlarmDispatcher.this.mContext;
                        String format2 = String.format("%s (%s)", new Object[]{action, Integer.valueOf(intExtra)});
                        Log1F380D.a((Object) format2);
                        PowerManager.WakeLock newWakeLock = WakeLocks.newWakeLock(context, format2);
                        try {
                            Logger logger2 = Logger.get();
                            String str2 = SystemAlarmDispatcher.TAG;
                            String format3 = String.format("Acquiring operation wake lock (%s) %s", new Object[]{action, newWakeLock});
                            Log1F380D.a((Object) format3);
                            logger2.debug(str2, format3, new Throwable[0]);
                            newWakeLock.acquire();
                            SystemAlarmDispatcher.this.mCommandHandler.onHandleIntent(SystemAlarmDispatcher.this.mCurrentIntent, intExtra, SystemAlarmDispatcher.this);
                            Logger logger3 = Logger.get();
                            String str3 = SystemAlarmDispatcher.TAG;
                            String format4 = String.format("Releasing operation wake lock (%s) %s", new Object[]{action, newWakeLock});
                            Log1F380D.a((Object) format4);
                            logger3.debug(str3, format4, new Throwable[0]);
                            newWakeLock.release();
                            systemAlarmDispatcher = SystemAlarmDispatcher.this;
                            dequeueAndCheckForCompletion = new DequeueAndCheckForCompletion(SystemAlarmDispatcher.this);
                        } catch (Throwable th) {
                            Logger logger4 = Logger.get();
                            String str4 = SystemAlarmDispatcher.TAG;
                            String format5 = String.format("Releasing operation wake lock (%s) %s", new Object[]{action, newWakeLock});
                            Log1F380D.a((Object) format5);
                            logger4.debug(str4, format5, new Throwable[0]);
                            newWakeLock.release();
                            SystemAlarmDispatcher.this.postOnMainThread(new DequeueAndCheckForCompletion(SystemAlarmDispatcher.this));
                            throw th;
                        }
                        systemAlarmDispatcher.postOnMainThread(dequeueAndCheckForCompletion);
                    }
                }
            });
        } finally {
            newWakeLock.release();
        }
    }

    /* access modifiers changed from: package-private */
    public Processor getProcessor() {
        return this.mProcessor;
    }

    /* access modifiers changed from: package-private */
    public TaskExecutor getTaskExecutor() {
        return this.mTaskExecutor;
    }

    /* access modifiers changed from: package-private */
    public WorkManagerImpl getWorkManager() {
        return this.mWorkManager;
    }

    /* access modifiers changed from: package-private */
    public WorkTimer getWorkTimer() {
        return this.mWorkTimer;
    }

    /* access modifiers changed from: package-private */
    public void onDestroy() {
        Logger.get().debug(TAG, "Destroying SystemAlarmDispatcher", new Throwable[0]);
        this.mProcessor.removeExecutionListener(this);
        this.mWorkTimer.onDestroy();
        this.mCompletedListener = null;
    }

    public void onExecuted(String workSpecId, boolean needsReschedule) {
        postOnMainThread(new AddRunnable(this, CommandHandler.createExecutionCompletedIntent(this.mContext, workSpecId, needsReschedule), 0));
    }

    /* access modifiers changed from: package-private */
    public void postOnMainThread(Runnable runnable) {
        this.mMainHandler.post(runnable);
    }

    /* access modifiers changed from: package-private */
    public void setCompletedListener(CommandsCompletedListener listener) {
        if (this.mCompletedListener != null) {
            Logger.get().error(TAG, "A completion listener for SystemAlarmDispatcher already exists.", new Throwable[0]);
        } else {
            this.mCompletedListener = listener;
        }
    }

    public boolean add(Intent intent, int startId) {
        Logger logger = Logger.get();
        String str = TAG;
        boolean z = false;
        String format = String.format("Adding command %s (%s)", new Object[]{intent, Integer.valueOf(startId)});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        assertMainThread();
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            Logger.get().warning(str, "Unknown command. Ignoring", new Throwable[0]);
            return false;
        } else if ("ACTION_CONSTRAINTS_CHANGED".equals(action) && hasIntentWithAction("ACTION_CONSTRAINTS_CHANGED")) {
            return false;
        } else {
            intent.putExtra(KEY_START_ID, startId);
            synchronized (this.mIntents) {
                if (!this.mIntents.isEmpty()) {
                    z = true;
                }
                boolean z2 = z;
                this.mIntents.add(intent);
                if (!z2) {
                    processCommand();
                }
            }
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public void dequeueAndCheckForCompletion() {
        Logger logger = Logger.get();
        String str = TAG;
        logger.debug(str, "Checking if commands are complete.", new Throwable[0]);
        assertMainThread();
        synchronized (this.mIntents) {
            if (this.mCurrentIntent != null) {
                Logger logger2 = Logger.get();
                String format = String.format("Removing command %s", new Object[]{this.mCurrentIntent});
                Log1F380D.a((Object) format);
                logger2.debug(str, format, new Throwable[0]);
                if (this.mIntents.remove(0).equals(this.mCurrentIntent)) {
                    this.mCurrentIntent = null;
                } else {
                    throw new IllegalStateException("Dequeue-d command is not the first.");
                }
            }
            SerialExecutor backgroundExecutor = this.mTaskExecutor.getBackgroundExecutor();
            if (!this.mCommandHandler.hasPendingCommands() && this.mIntents.isEmpty() && !backgroundExecutor.hasPendingTasks()) {
                Logger.get().debug(str, "No more commands & intents.", new Throwable[0]);
                CommandsCompletedListener commandsCompletedListener = this.mCompletedListener;
                if (commandsCompletedListener != null) {
                    commandsCompletedListener.onAllCommandsCompleted();
                }
            } else if (!this.mIntents.isEmpty()) {
                processCommand();
            }
        }
    }
}

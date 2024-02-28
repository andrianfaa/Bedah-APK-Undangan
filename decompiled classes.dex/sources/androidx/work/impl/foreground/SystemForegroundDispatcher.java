package androidx.work.impl.foreground;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import androidx.work.ForegroundInfo;
import androidx.work.Logger;
import androidx.work.impl.ExecutionListener;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.constraints.WorkConstraintsCallback;
import androidx.work.impl.constraints.WorkConstraintsTracker;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import mt.Log1F380D;

/* compiled from: 00D1 */
public class SystemForegroundDispatcher implements WorkConstraintsCallback, ExecutionListener {
    private static final String ACTION_CANCEL_WORK = "ACTION_CANCEL_WORK";
    private static final String ACTION_NOTIFY = "ACTION_NOTIFY";
    private static final String ACTION_START_FOREGROUND = "ACTION_START_FOREGROUND";
    private static final String ACTION_STOP_FOREGROUND = "ACTION_STOP_FOREGROUND";
    private static final String KEY_FOREGROUND_SERVICE_TYPE = "KEY_FOREGROUND_SERVICE_TYPE";
    private static final String KEY_NOTIFICATION = "KEY_NOTIFICATION";
    private static final String KEY_NOTIFICATION_ID = "KEY_NOTIFICATION_ID";
    private static final String KEY_WORKSPEC_ID = "KEY_WORKSPEC_ID";
    static final String TAG;
    private Callback mCallback;
    final WorkConstraintsTracker mConstraintsTracker;
    private Context mContext;
    String mCurrentForegroundWorkSpecId;
    final Map<String, ForegroundInfo> mForegroundInfoById;
    final Object mLock = new Object();
    private final TaskExecutor mTaskExecutor;
    final Set<WorkSpec> mTrackedWorkSpecs;
    private WorkManagerImpl mWorkManagerImpl;
    final Map<String, WorkSpec> mWorkSpecById;

    interface Callback {
        void cancelNotification(int i);

        void notify(int i, Notification notification);

        void startForeground(int i, int i2, Notification notification);

        void stop();
    }

    static {
        String tagWithPrefix = Logger.tagWithPrefix("SystemFgDispatcher");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    SystemForegroundDispatcher(Context context) {
        this.mContext = context;
        WorkManagerImpl instance = WorkManagerImpl.getInstance(this.mContext);
        this.mWorkManagerImpl = instance;
        TaskExecutor workTaskExecutor = instance.getWorkTaskExecutor();
        this.mTaskExecutor = workTaskExecutor;
        this.mCurrentForegroundWorkSpecId = null;
        this.mForegroundInfoById = new LinkedHashMap();
        this.mTrackedWorkSpecs = new HashSet();
        this.mWorkSpecById = new HashMap();
        this.mConstraintsTracker = new WorkConstraintsTracker(this.mContext, workTaskExecutor, this);
        this.mWorkManagerImpl.getProcessor().addExecutionListener(this);
    }

    SystemForegroundDispatcher(Context context, WorkManagerImpl workManagerImpl, WorkConstraintsTracker tracker) {
        this.mContext = context;
        this.mWorkManagerImpl = workManagerImpl;
        this.mTaskExecutor = workManagerImpl.getWorkTaskExecutor();
        this.mCurrentForegroundWorkSpecId = null;
        this.mForegroundInfoById = new LinkedHashMap();
        this.mTrackedWorkSpecs = new HashSet();
        this.mWorkSpecById = new HashMap();
        this.mConstraintsTracker = tracker;
        this.mWorkManagerImpl.getProcessor().addExecutionListener(this);
    }

    public static Intent createNotifyIntent(Context context, String workSpecId, ForegroundInfo info) {
        Intent intent = new Intent(context, SystemForegroundService.class);
        intent.setAction(ACTION_NOTIFY);
        intent.putExtra(KEY_NOTIFICATION_ID, info.getNotificationId());
        intent.putExtra(KEY_FOREGROUND_SERVICE_TYPE, info.getForegroundServiceType());
        intent.putExtra(KEY_NOTIFICATION, info.getNotification());
        intent.putExtra(KEY_WORKSPEC_ID, workSpecId);
        return intent;
    }

    public static Intent createStartForegroundIntent(Context context, String workSpecId, ForegroundInfo info) {
        Intent intent = new Intent(context, SystemForegroundService.class);
        intent.setAction(ACTION_START_FOREGROUND);
        intent.putExtra(KEY_WORKSPEC_ID, workSpecId);
        intent.putExtra(KEY_NOTIFICATION_ID, info.getNotificationId());
        intent.putExtra(KEY_FOREGROUND_SERVICE_TYPE, info.getForegroundServiceType());
        intent.putExtra(KEY_NOTIFICATION, info.getNotification());
        intent.putExtra(KEY_WORKSPEC_ID, workSpecId);
        return intent;
    }

    public static Intent createStopForegroundIntent(Context context) {
        Intent intent = new Intent(context, SystemForegroundService.class);
        intent.setAction(ACTION_STOP_FOREGROUND);
        return intent;
    }

    /* access modifiers changed from: package-private */
    public WorkManagerImpl getWorkManager() {
        return this.mWorkManagerImpl;
    }

    /* access modifiers changed from: package-private */
    public void handleStop(Intent intent) {
        Logger.get().info(TAG, "Stopping foreground service", new Throwable[0]);
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.stop();
        }
    }

    public void onAllConstraintsMet(List<String> list) {
    }

    /* access modifiers changed from: package-private */
    public void onDestroy() {
        this.mCallback = null;
        synchronized (this.mLock) {
            this.mConstraintsTracker.reset();
        }
        this.mWorkManagerImpl.getProcessor().removeExecutionListener(this);
    }

    /* access modifiers changed from: package-private */
    public void onStartCommand(Intent intent) {
        String action = intent.getAction();
        if (ACTION_START_FOREGROUND.equals(action)) {
            handleStartForeground(intent);
            handleNotify(intent);
        } else if (ACTION_NOTIFY.equals(action)) {
            handleNotify(intent);
        } else if (ACTION_CANCEL_WORK.equals(action)) {
            handleCancelWork(intent);
        } else if (ACTION_STOP_FOREGROUND.equals(action)) {
            handleStop(intent);
        }
    }

    /* access modifiers changed from: package-private */
    public void setCallback(Callback callback) {
        if (this.mCallback != null) {
            Logger.get().error(TAG, "A callback already exists.", new Throwable[0]);
        } else {
            this.mCallback = callback;
        }
    }

    public static Intent createCancelWorkIntent(Context context, String workSpecId) {
        Intent intent = new Intent(context, SystemForegroundService.class);
        intent.setAction(ACTION_CANCEL_WORK);
        String format = String.format("workspec://%s", new Object[]{workSpecId});
        Log1F380D.a((Object) format);
        intent.setData(Uri.parse(format));
        intent.putExtra(KEY_WORKSPEC_ID, workSpecId);
        return intent;
    }

    private void handleCancelWork(Intent intent) {
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("Stopping foreground work for %s", new Object[]{intent});
        Log1F380D.a((Object) format);
        logger.info(str, format, new Throwable[0]);
        String stringExtra = intent.getStringExtra(KEY_WORKSPEC_ID);
        if (stringExtra != null && !TextUtils.isEmpty(stringExtra)) {
            this.mWorkManagerImpl.cancelWorkById(UUID.fromString(stringExtra));
        }
    }

    private void handleNotify(Intent intent) {
        int intExtra = intent.getIntExtra(KEY_NOTIFICATION_ID, 0);
        int intExtra2 = intent.getIntExtra(KEY_FOREGROUND_SERVICE_TYPE, 0);
        String stringExtra = intent.getStringExtra(KEY_WORKSPEC_ID);
        Notification notification = (Notification) intent.getParcelableExtra(KEY_NOTIFICATION);
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("Notifying with (id: %s, workSpecId: %s, notificationType: %s)", new Object[]{Integer.valueOf(intExtra), stringExtra, Integer.valueOf(intExtra2)});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        if (notification != null && this.mCallback != null) {
            this.mForegroundInfoById.put(stringExtra, new ForegroundInfo(intExtra, notification, intExtra2));
            if (TextUtils.isEmpty(this.mCurrentForegroundWorkSpecId)) {
                this.mCurrentForegroundWorkSpecId = stringExtra;
                this.mCallback.startForeground(intExtra, intExtra2, notification);
                return;
            }
            this.mCallback.notify(intExtra, notification);
            if (intExtra2 != 0 && Build.VERSION.SDK_INT >= 29) {
                int i = 0;
                for (Map.Entry<String, ForegroundInfo> value : this.mForegroundInfoById.entrySet()) {
                    i |= ((ForegroundInfo) value.getValue()).getForegroundServiceType();
                }
                ForegroundInfo foregroundInfo = this.mForegroundInfoById.get(this.mCurrentForegroundWorkSpecId);
                if (foregroundInfo != null) {
                    this.mCallback.startForeground(foregroundInfo.getNotificationId(), i, foregroundInfo.getNotification());
                }
            }
        }
    }

    private void handleStartForeground(Intent intent) {
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("Started foreground service %s", new Object[]{intent});
        Log1F380D.a((Object) format);
        logger.info(str, format, new Throwable[0]);
        final String stringExtra = intent.getStringExtra(KEY_WORKSPEC_ID);
        final WorkDatabase workDatabase = this.mWorkManagerImpl.getWorkDatabase();
        this.mTaskExecutor.executeOnBackgroundThread(new Runnable() {
            public void run() {
                WorkSpec workSpec = workDatabase.workSpecDao().getWorkSpec(stringExtra);
                if (workSpec != null && workSpec.hasConstraints()) {
                    synchronized (SystemForegroundDispatcher.this.mLock) {
                        SystemForegroundDispatcher.this.mWorkSpecById.put(stringExtra, workSpec);
                        SystemForegroundDispatcher.this.mTrackedWorkSpecs.add(workSpec);
                        SystemForegroundDispatcher.this.mConstraintsTracker.replace(SystemForegroundDispatcher.this.mTrackedWorkSpecs);
                    }
                }
            }
        });
    }

    public void onAllConstraintsNotMet(List<String> list) {
        if (!list.isEmpty()) {
            for (String next : list) {
                Logger logger = Logger.get();
                String str = TAG;
                String format = String.format("Constraints unmet for WorkSpec %s", new Object[]{next});
                Log1F380D.a((Object) format);
                logger.debug(str, format, new Throwable[0]);
                this.mWorkManagerImpl.stopForegroundWork(next);
            }
        }
    }

    public void onExecuted(String workSpecId, boolean needsReschedule) {
        Map.Entry entry;
        boolean z = false;
        synchronized (this.mLock) {
            WorkSpec remove = this.mWorkSpecById.remove(workSpecId);
            if (remove != null) {
                z = this.mTrackedWorkSpecs.remove(remove);
            }
            if (z) {
                this.mConstraintsTracker.replace(this.mTrackedWorkSpecs);
            }
        }
        ForegroundInfo remove2 = this.mForegroundInfoById.remove(workSpecId);
        if (workSpecId.equals(this.mCurrentForegroundWorkSpecId) && this.mForegroundInfoById.size() > 0) {
            Iterator<Map.Entry<String, ForegroundInfo>> it = this.mForegroundInfoById.entrySet().iterator();
            Map.Entry<String, ForegroundInfo> next = it.next();
            while (true) {
                entry = next;
                if (!it.hasNext()) {
                    break;
                }
                next = it.next();
            }
            this.mCurrentForegroundWorkSpecId = (String) entry.getKey();
            if (this.mCallback != null) {
                ForegroundInfo foregroundInfo = (ForegroundInfo) entry.getValue();
                this.mCallback.startForeground(foregroundInfo.getNotificationId(), foregroundInfo.getForegroundServiceType(), foregroundInfo.getNotification());
                this.mCallback.cancelNotification(foregroundInfo.getNotificationId());
            }
        }
        Callback callback = this.mCallback;
        if (remove2 != null && callback != null) {
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Removing Notification (id: %s, workSpecId: %s ,notificationType: %s)", new Object[]{Integer.valueOf(remove2.getNotificationId()), workSpecId, Integer.valueOf(remove2.getForegroundServiceType())});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            callback.cancelNotification(remove2.getNotificationId());
        }
    }
}

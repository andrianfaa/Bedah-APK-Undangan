package androidx.work.impl.foreground;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LifecycleService;
import androidx.work.Logger;
import androidx.work.impl.foreground.SystemForegroundDispatcher;
import mt.Log1F380D;

/* compiled from: 00D2 */
public class SystemForegroundService extends LifecycleService implements SystemForegroundDispatcher.Callback {
    private static final String TAG;
    private static SystemForegroundService sForegroundService = null;
    SystemForegroundDispatcher mDispatcher;
    private Handler mHandler;
    private boolean mIsShutdown;
    NotificationManager mNotificationManager;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("SystemFgService");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public static SystemForegroundService getInstance() {
        return sForegroundService;
    }

    private void initializeDispatcher() {
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mNotificationManager = (NotificationManager) getApplicationContext().getSystemService("notification");
        SystemForegroundDispatcher systemForegroundDispatcher = new SystemForegroundDispatcher(getApplicationContext());
        this.mDispatcher = systemForegroundDispatcher;
        systemForegroundDispatcher.setCallback(this);
    }

    public void cancelNotification(final int notificationId) {
        this.mHandler.post(new Runnable() {
            public void run() {
                SystemForegroundService.this.mNotificationManager.cancel(notificationId);
            }
        });
    }

    public void notify(final int notificationId, final Notification notification) {
        this.mHandler.post(new Runnable() {
            public void run() {
                SystemForegroundService.this.mNotificationManager.notify(notificationId, notification);
            }
        });
    }

    public void onCreate() {
        super.onCreate();
        sForegroundService = this;
        initializeDispatcher();
    }

    public void onDestroy() {
        super.onDestroy();
        this.mDispatcher.onDestroy();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (this.mIsShutdown) {
            Logger.get().info(TAG, "Re-initializing SystemForegroundService after a request to shut-down.", new Throwable[0]);
            this.mDispatcher.onDestroy();
            initializeDispatcher();
            this.mIsShutdown = false;
        }
        if (intent == null) {
            return 3;
        }
        this.mDispatcher.onStartCommand(intent);
        return 3;
    }

    public void startForeground(final int notificationId, final int notificationType, final Notification notification) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (Build.VERSION.SDK_INT >= 29) {
                    SystemForegroundService.this.startForeground(notificationId, notification, notificationType);
                } else {
                    SystemForegroundService.this.startForeground(notificationId, notification);
                }
            }
        });
    }

    public void stop() {
        this.mIsShutdown = true;
        Logger.get().debug(TAG, "All commands completed.", new Throwable[0]);
        if (Build.VERSION.SDK_INT >= 26) {
            stopForeground(true);
        }
        sForegroundService = null;
        stopSelf();
    }
}

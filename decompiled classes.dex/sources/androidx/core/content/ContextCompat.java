package androidx.core.content;

import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.app.job.JobScheduler;
import android.app.usage.UsageStatsManager;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothManager;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.RestrictionsManager;
import android.content.pm.LauncherApps;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.hardware.ConsumerIrManager;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraManager;
import android.hardware.display.DisplayManager;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.media.projection.MediaProjectionManager;
import android.media.session.MediaSessionManager;
import android.media.tv.TvInputManager;
import android.net.ConnectivityManager;
import android.net.nsd.NsdManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Process;
import android.os.UserManager;
import android.os.Vibrator;
import android.os.storage.StorageManager;
import android.print.PrintManager;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.TextServicesManager;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.os.ExecutorCompat;
import androidx.core.util.ObjectsCompat;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.Executor;
import mt.Log1F380D;

/* compiled from: 0029 */
public class ContextCompat {
    private static final String TAG = "ContextCompat";
    private static final Object sLock = new Object();
    private static final Object sSync = new Object();
    private static TypedValue sTempValue;

    static class Api16Impl {
        private Api16Impl() {
        }

        static void startActivities(Context obj, Intent[] intents, Bundle options) {
            obj.startActivities(intents, options);
        }

        static void startActivity(Context obj, Intent intent, Bundle options) {
            obj.startActivity(intent, options);
        }
    }

    static class Api19Impl {
        private Api19Impl() {
        }

        static File[] getExternalCacheDirs(Context obj) {
            return obj.getExternalCacheDirs();
        }

        static File[] getExternalFilesDirs(Context obj, String type) {
            return obj.getExternalFilesDirs(type);
        }

        static File[] getObbDirs(Context obj) {
            return obj.getObbDirs();
        }
    }

    static class Api21Impl {
        private Api21Impl() {
        }

        static File getCodeCacheDir(Context obj) {
            return obj.getCodeCacheDir();
        }

        static Drawable getDrawable(Context obj, int id) {
            return obj.getDrawable(id);
        }

        static File getNoBackupFilesDir(Context obj) {
            return obj.getNoBackupFilesDir();
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        static int getColor(Context obj, int id) {
            return obj.getColor(id);
        }

        static <T> T getSystemService(Context obj, Class<T> cls) {
            return obj.getSystemService(cls);
        }

        static String getSystemServiceName(Context obj, Class<?> cls) {
            return obj.getSystemServiceName(cls);
        }
    }

    static class Api24Impl {
        private Api24Impl() {
        }

        static Context createDeviceProtectedStorageContext(Context obj) {
            return obj.createDeviceProtectedStorageContext();
        }

        static File getDataDir(Context obj) {
            return obj.getDataDir();
        }

        static boolean isDeviceProtectedStorage(Context obj) {
            return obj.isDeviceProtectedStorage();
        }
    }

    static class Api26Impl {
        private Api26Impl() {
        }

        static ComponentName startForegroundService(Context obj, Intent service) {
            return obj.startForegroundService(service);
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static Executor getMainExecutor(Context obj) {
            return obj.getMainExecutor();
        }
    }

    static class Api30Impl {
        private Api30Impl() {
        }

        static String getAttributionTag(Context obj) {
            return obj.getAttributionTag();
        }
    }

    private static final class LegacyServiceMapHolder {
        static final HashMap<Class<?>, String> SERVICES;

        static {
            HashMap<Class<?>, String> hashMap = new HashMap<>();
            SERVICES = hashMap;
            if (Build.VERSION.SDK_INT >= 22) {
                hashMap.put(SubscriptionManager.class, "telephony_subscription_service");
                hashMap.put(UsageStatsManager.class, "usagestats");
            }
            if (Build.VERSION.SDK_INT >= 21) {
                hashMap.put(AppWidgetManager.class, "appwidget");
                hashMap.put(BatteryManager.class, "batterymanager");
                hashMap.put(CameraManager.class, "camera");
                hashMap.put(JobScheduler.class, "jobscheduler");
                hashMap.put(LauncherApps.class, "launcherapps");
                hashMap.put(MediaProjectionManager.class, "media_projection");
                hashMap.put(MediaSessionManager.class, "media_session");
                hashMap.put(RestrictionsManager.class, "restrictions");
                hashMap.put(TelecomManager.class, "telecom");
                hashMap.put(TvInputManager.class, "tv_input");
            }
            if (Build.VERSION.SDK_INT >= 19) {
                hashMap.put(AppOpsManager.class, "appops");
                hashMap.put(CaptioningManager.class, "captioning");
                hashMap.put(ConsumerIrManager.class, "consumer_ir");
                hashMap.put(PrintManager.class, "print");
            }
            if (Build.VERSION.SDK_INT >= 18) {
                hashMap.put(BluetoothManager.class, "bluetooth");
            }
            if (Build.VERSION.SDK_INT >= 17) {
                hashMap.put(DisplayManager.class, "display");
                hashMap.put(UserManager.class, "user");
            }
            if (Build.VERSION.SDK_INT >= 16) {
                hashMap.put(InputManager.class, "input");
                hashMap.put(MediaRouter.class, "media_router");
                hashMap.put(NsdManager.class, "servicediscovery");
            }
            hashMap.put(AccessibilityManager.class, "accessibility");
            hashMap.put(AccountManager.class, "account");
            hashMap.put(ActivityManager.class, "activity");
            hashMap.put(AlarmManager.class, NotificationCompat.CATEGORY_ALARM);
            hashMap.put(AudioManager.class, "audio");
            hashMap.put(ClipboardManager.class, "clipboard");
            hashMap.put(ConnectivityManager.class, "connectivity");
            hashMap.put(DevicePolicyManager.class, "device_policy");
            hashMap.put(DownloadManager.class, "download");
            hashMap.put(DropBoxManager.class, "dropbox");
            hashMap.put(InputMethodManager.class, "input_method");
            hashMap.put(KeyguardManager.class, "keyguard");
            hashMap.put(LayoutInflater.class, "layout_inflater");
            hashMap.put(LocationManager.class, "location");
            hashMap.put(NfcManager.class, "nfc");
            hashMap.put(NotificationManager.class, "notification");
            hashMap.put(PowerManager.class, "power");
            hashMap.put(SearchManager.class, "search");
            hashMap.put(SensorManager.class, "sensor");
            hashMap.put(StorageManager.class, "storage");
            hashMap.put(TelephonyManager.class, "phone");
            hashMap.put(TextServicesManager.class, "textservices");
            hashMap.put(UiModeManager.class, "uimode");
            hashMap.put(UsbManager.class, "usb");
            hashMap.put(Vibrator.class, "vibrator");
            hashMap.put(WallpaperManager.class, "wallpaper");
            hashMap.put(WifiP2pManager.class, "wifip2p");
            hashMap.put(WifiManager.class, "wifi");
            hashMap.put(WindowManager.class, "window");
        }

        private LegacyServiceMapHolder() {
        }
    }

    protected ContextCompat() {
    }

    public static int checkSelfPermission(Context context, String permission) {
        ObjectsCompat.requireNonNull(permission, "permission must be non-null");
        return context.checkPermission(permission, Process.myPid(), Process.myUid());
    }

    public static Context createDeviceProtectedStorageContext(Context context) {
        if (Build.VERSION.SDK_INT >= 24) {
            return Api24Impl.createDeviceProtectedStorageContext(context);
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002e, code lost:
        return r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.io.File createFilesDir(java.io.File r4) {
        /*
            java.lang.Object r0 = sSync
            monitor-enter(r0)
            boolean r1 = r4.exists()     // Catch:{ all -> 0x002f }
            if (r1 != 0) goto L_0x002d
            boolean r1 = r4.mkdirs()     // Catch:{ all -> 0x002f }
            if (r1 == 0) goto L_0x0011
            monitor-exit(r0)     // Catch:{ all -> 0x002f }
            return r4
        L_0x0011:
            java.lang.String r1 = "ContextCompat"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x002f }
            r2.<init>()     // Catch:{ all -> 0x002f }
            java.lang.String r3 = "Unable to create files subdir "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x002f }
            java.lang.String r3 = r4.getPath()     // Catch:{ all -> 0x002f }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x002f }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x002f }
            android.util.Log.w(r1, r2)     // Catch:{ all -> 0x002f }
        L_0x002d:
            monitor-exit(r0)     // Catch:{ all -> 0x002f }
            return r4
        L_0x002f:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002f }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.ContextCompat.createFilesDir(java.io.File):java.io.File");
    }

    public static String getAttributionTag(Context context) {
        if (Build.VERSION.SDK_INT < 30) {
            return null;
        }
        String attributionTag = Api30Impl.getAttributionTag(context);
        Log1F380D.a((Object) attributionTag);
        return attributionTag;
    }

    public static File getCodeCacheDir(Context context) {
        return Build.VERSION.SDK_INT >= 21 ? Api21Impl.getCodeCacheDir(context) : createFilesDir(new File(context.getApplicationInfo().dataDir, "code_cache"));
    }

    public static int getColor(Context context, int id) {
        return Build.VERSION.SDK_INT >= 23 ? Api23Impl.getColor(context, id) : context.getResources().getColor(id);
    }

    public static ColorStateList getColorStateList(Context context, int id) {
        return ResourcesCompat.getColorStateList(context.getResources(), id, context.getTheme());
    }

    public static File getDataDir(Context context) {
        if (Build.VERSION.SDK_INT >= 24) {
            return Api24Impl.getDataDir(context);
        }
        String str = context.getApplicationInfo().dataDir;
        if (str != null) {
            return new File(str);
        }
        return null;
    }

    public static Drawable getDrawable(Context context, int id) {
        int i;
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getDrawable(context, id);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return context.getResources().getDrawable(id);
        }
        synchronized (sLock) {
            if (sTempValue == null) {
                sTempValue = new TypedValue();
            }
            context.getResources().getValue(id, sTempValue, true);
            i = sTempValue.resourceId;
        }
        return context.getResources().getDrawable(i);
    }

    public static File[] getExternalCacheDirs(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Api19Impl.getExternalCacheDirs(context);
        }
        return new File[]{context.getExternalCacheDir()};
    }

    public static File[] getExternalFilesDirs(Context context, String type) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Api19Impl.getExternalFilesDirs(context, type);
        }
        return new File[]{context.getExternalFilesDir(type)};
    }

    public static Executor getMainExecutor(Context context) {
        return Build.VERSION.SDK_INT >= 28 ? Api28Impl.getMainExecutor(context) : ExecutorCompat.create(new Handler(context.getMainLooper()));
    }

    public static File getNoBackupFilesDir(Context context) {
        return Build.VERSION.SDK_INT >= 21 ? Api21Impl.getNoBackupFilesDir(context) : createFilesDir(new File(context.getApplicationInfo().dataDir, "no_backup"));
    }

    public static File[] getObbDirs(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Api19Impl.getObbDirs(context);
        }
        return new File[]{context.getObbDir()};
    }

    public static boolean isDeviceProtectedStorage(Context context) {
        if (Build.VERSION.SDK_INT >= 24) {
            return Api24Impl.isDeviceProtectedStorage(context);
        }
        return false;
    }

    public static boolean startActivities(Context context, Intent[] intents) {
        return startActivities(context, intents, (Bundle) null);
    }

    public static boolean startActivities(Context context, Intent[] intents, Bundle options) {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.startActivities(context, intents, options);
            return true;
        }
        context.startActivities(intents);
        return true;
    }

    public static void startActivity(Context context, Intent intent, Bundle options) {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.startActivity(context, intent, options);
        } else {
            context.startActivity(intent);
        }
    }

    public static void startForegroundService(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.startForegroundService(context, intent);
        } else {
            context.startService(intent);
        }
    }

    public static <T> T getSystemService(Context context, Class<T> cls) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.getSystemService(context, cls);
        }
        String systemServiceName = getSystemServiceName(context, cls);
        Log1F380D.a((Object) systemServiceName);
        if (systemServiceName != null) {
            return context.getSystemService(systemServiceName);
        }
        return null;
    }

    public static String getSystemServiceName(Context context, Class<?> cls) {
        if (Build.VERSION.SDK_INT < 23) {
            return LegacyServiceMapHolder.SERVICES.get(cls);
        }
        String systemServiceName = Api23Impl.getSystemServiceName(context, cls);
        Log1F380D.a((Object) systemServiceName);
        return systemServiceName;
    }
}

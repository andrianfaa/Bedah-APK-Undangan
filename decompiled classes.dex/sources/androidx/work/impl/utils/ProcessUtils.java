package androidx.work.impl.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import androidx.work.Configuration;
import androidx.work.Logger;
import java.lang.reflect.Method;
import java.util.List;
import mt.Log1F380D;

/* compiled from: 00D8 */
public class ProcessUtils {
    private static final String TAG;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("ProcessUtils");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    private ProcessUtils() {
    }

    public static String getProcessName(Context context) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        Object obj;
        if (Build.VERSION.SDK_INT >= 28) {
            String processName = Application.getProcessName();
            Log1F380D.a((Object) processName);
            return processName;
        }
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread", false, ProcessUtils.class.getClassLoader());
            if (Build.VERSION.SDK_INT >= 18) {
                Method declaredMethod = cls.getDeclaredMethod("currentProcessName", new Class[0]);
                declaredMethod.setAccessible(true);
                obj = declaredMethod.invoke((Object) null, new Object[0]);
            } else {
                Method declaredMethod2 = cls.getDeclaredMethod("currentActivityThread", new Class[0]);
                declaredMethod2.setAccessible(true);
                Method declaredMethod3 = cls.getDeclaredMethod("getProcessName", new Class[0]);
                declaredMethod3.setAccessible(true);
                obj = declaredMethod3.invoke(declaredMethod2.invoke((Object) null, new Object[0]), new Object[0]);
            }
            if (obj instanceof String) {
                return (String) obj;
            }
        } catch (Throwable th) {
            Logger.get().debug(TAG, "Unable to check ActivityThread for processName", th);
        }
        int myPid = Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (!(activityManager == null || (runningAppProcesses = activityManager.getRunningAppProcesses()) == null || runningAppProcesses.isEmpty())) {
            for (ActivityManager.RunningAppProcessInfo next : runningAppProcesses) {
                if (next.pid == myPid) {
                    return next.processName;
                }
            }
        }
        return null;
    }

    public static boolean isDefaultProcess(Context context, Configuration configuration) {
        String processName = getProcessName(context);
        Log1F380D.a((Object) processName);
        return !TextUtils.isEmpty(configuration.getDefaultProcessName()) ? TextUtils.equals(processName, configuration.getDefaultProcessName()) : TextUtils.equals(processName, context.getApplicationInfo().processName);
    }
}

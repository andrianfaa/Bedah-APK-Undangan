package androidx.work.impl.utils;

import android.content.Context;
import android.os.PowerManager;
import androidx.work.Logger;
import java.util.HashMap;
import java.util.WeakHashMap;
import mt.Log1F380D;

/* compiled from: 00DA */
public class WakeLocks {
    private static final String TAG;
    private static final WeakHashMap<PowerManager.WakeLock, String> sWakeLocks = new WeakHashMap<>();

    static {
        String tagWithPrefix = Logger.tagWithPrefix("WakeLocks");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    private WakeLocks() {
    }

    public static PowerManager.WakeLock newWakeLock(Context context, String tag) {
        String str = "WorkManager: " + tag;
        PowerManager.WakeLock newWakeLock = ((PowerManager) context.getApplicationContext().getSystemService("power")).newWakeLock(1, str);
        WeakHashMap<PowerManager.WakeLock, String> weakHashMap = sWakeLocks;
        synchronized (weakHashMap) {
            weakHashMap.put(newWakeLock, str);
        }
        return newWakeLock;
    }

    public static void checkWakeLocks() {
        HashMap hashMap = new HashMap();
        WeakHashMap<PowerManager.WakeLock, String> weakHashMap = sWakeLocks;
        synchronized (weakHashMap) {
            hashMap.putAll(weakHashMap);
        }
        for (PowerManager.WakeLock wakeLock : hashMap.keySet()) {
            if (wakeLock != null && wakeLock.isHeld()) {
                String format = String.format("WakeLock held for %s", new Object[]{hashMap.get(wakeLock)});
                Log1F380D.a((Object) format);
                Logger.get().warning(TAG, format, new Throwable[0]);
            }
        }
    }
}

package androidx.work.impl.background.systemalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.work.Logger;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.model.SystemIdInfo;
import androidx.work.impl.model.SystemIdInfoDao;
import androidx.work.impl.utils.IdGenerator;
import mt.Log1F380D;

/* compiled from: 00B6 */
class Alarms {
    private static final String TAG;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("Alarms");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    private Alarms() {
    }

    public static void setAlarm(Context context, WorkManagerImpl workManager, String workSpecId, long triggerAtMillis) {
        WorkDatabase workDatabase = workManager.getWorkDatabase();
        SystemIdInfoDao systemIdInfoDao = workDatabase.systemIdInfoDao();
        SystemIdInfo systemIdInfo = systemIdInfoDao.getSystemIdInfo(workSpecId);
        if (systemIdInfo != null) {
            cancelExactAlarm(context, workSpecId, systemIdInfo.systemId);
            setExactAlarm(context, workSpecId, systemIdInfo.systemId, triggerAtMillis);
            return;
        }
        int nextAlarmManagerId = new IdGenerator(workDatabase).nextAlarmManagerId();
        systemIdInfoDao.insertSystemIdInfo(new SystemIdInfo(workSpecId, nextAlarmManagerId));
        setExactAlarm(context, workSpecId, nextAlarmManagerId, triggerAtMillis);
    }

    private static void setExactAlarm(Context context, String workSpecId, int alarmId, long triggerAtMillis) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        int i = 134217728;
        if (Build.VERSION.SDK_INT >= 23) {
            i = 134217728 | 67108864;
        }
        PendingIntent service = PendingIntent.getService(context, alarmId, CommandHandler.createDelayMetIntent(context, workSpecId), i);
        if (alarmManager == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(0, triggerAtMillis, service);
        } else {
            alarmManager.set(0, triggerAtMillis, service);
        }
    }

    public static void cancelAlarm(Context context, WorkManagerImpl workManager, String workSpecId) {
        SystemIdInfoDao systemIdInfoDao = workManager.getWorkDatabase().systemIdInfoDao();
        SystemIdInfo systemIdInfo = systemIdInfoDao.getSystemIdInfo(workSpecId);
        if (systemIdInfo != null) {
            cancelExactAlarm(context, workSpecId, systemIdInfo.systemId);
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Removing SystemIdInfo for workSpecId (%s)", new Object[]{workSpecId});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            systemIdInfoDao.removeSystemIdInfo(workSpecId);
        }
    }

    private static void cancelExactAlarm(Context context, String workSpecId, int alarmId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        Intent createDelayMetIntent = CommandHandler.createDelayMetIntent(context, workSpecId);
        int i = 536870912;
        if (Build.VERSION.SDK_INT >= 23) {
            i = 536870912 | 67108864;
        }
        PendingIntent service = PendingIntent.getService(context, alarmId, createDelayMetIntent, i);
        if (service != null && alarmManager != null) {
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Cancelling existing alarm with (workSpecId, systemId) (%s, %s)", new Object[]{workSpecId, Integer.valueOf(alarmId)});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            alarmManager.cancel(service);
        }
    }
}

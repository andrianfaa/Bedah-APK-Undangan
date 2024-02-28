package androidx.work.impl;

import android.content.Context;
import android.os.Build;
import androidx.work.Configuration;
import androidx.work.Logger;
import androidx.work.impl.background.systemalarm.SystemAlarmScheduler;
import androidx.work.impl.background.systemalarm.SystemAlarmService;
import androidx.work.impl.background.systemjob.SystemJobScheduler;
import androidx.work.impl.background.systemjob.SystemJobService;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.model.WorkSpecDao;
import androidx.work.impl.utils.PackageManagerHelper;
import java.util.List;
import mt.Log1F380D;

/* compiled from: 00AA */
public class Schedulers {
    public static final String GCM_SCHEDULER = "androidx.work.impl.background.gcm.GcmScheduler";
    private static final String TAG;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("Schedulers");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    private Schedulers() {
    }

    static Scheduler createBestAvailableBackgroundScheduler(Context context, WorkManagerImpl workManager) {
        if (Build.VERSION.SDK_INT >= 23) {
            SystemJobScheduler systemJobScheduler = new SystemJobScheduler(context, workManager);
            PackageManagerHelper.setComponentEnabled(context, SystemJobService.class, true);
            Logger.get().debug(TAG, "Created SystemJobScheduler and enabled SystemJobService", new Throwable[0]);
            return systemJobScheduler;
        }
        Scheduler tryCreateGcmBasedScheduler = tryCreateGcmBasedScheduler(context);
        if (tryCreateGcmBasedScheduler != null) {
            return tryCreateGcmBasedScheduler;
        }
        SystemAlarmScheduler systemAlarmScheduler = new SystemAlarmScheduler(context);
        PackageManagerHelper.setComponentEnabled(context, SystemAlarmService.class, true);
        Logger.get().debug(TAG, "Created SystemAlarmScheduler", new Throwable[0]);
        return systemAlarmScheduler;
    }

    public static void schedule(Configuration configuration, WorkDatabase workDatabase, List<Scheduler> list) {
        if (list != null && list.size() != 0) {
            WorkSpecDao workSpecDao = workDatabase.workSpecDao();
            workDatabase.beginTransaction();
            try {
                List<WorkSpec> eligibleWorkForScheduling = workSpecDao.getEligibleWorkForScheduling(configuration.getMaxSchedulerLimit());
                List<WorkSpec> allEligibleWorkSpecsForScheduling = workSpecDao.getAllEligibleWorkSpecsForScheduling(200);
                if (eligibleWorkForScheduling != null && eligibleWorkForScheduling.size() > 0) {
                    long currentTimeMillis = System.currentTimeMillis();
                    for (WorkSpec workSpec : eligibleWorkForScheduling) {
                        workSpecDao.markWorkSpecScheduled(workSpec.id, currentTimeMillis);
                    }
                }
                workDatabase.setTransactionSuccessful();
                if (eligibleWorkForScheduling != null && eligibleWorkForScheduling.size() > 0) {
                    WorkSpec[] workSpecArr = (WorkSpec[]) eligibleWorkForScheduling.toArray(new WorkSpec[eligibleWorkForScheduling.size()]);
                    for (Scheduler next : list) {
                        if (next.hasLimitedSchedulingSlots()) {
                            next.schedule(workSpecArr);
                        }
                    }
                }
                if (allEligibleWorkSpecsForScheduling != null && allEligibleWorkSpecsForScheduling.size() > 0) {
                    WorkSpec[] workSpecArr2 = (WorkSpec[]) allEligibleWorkSpecsForScheduling.toArray(new WorkSpec[allEligibleWorkSpecsForScheduling.size()]);
                    for (Scheduler next2 : list) {
                        if (!next2.hasLimitedSchedulingSlots()) {
                            next2.schedule(workSpecArr2);
                        }
                    }
                }
            } finally {
                workDatabase.endTransaction();
            }
        }
    }

    private static Scheduler tryCreateGcmBasedScheduler(Context context) {
        try {
            Scheduler scheduler = (Scheduler) Class.forName(GCM_SCHEDULER).getConstructor(new Class[]{Context.class}).newInstance(new Object[]{context});
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format("Created %s", new Object[]{GCM_SCHEDULER});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            return scheduler;
        } catch (Throwable th) {
            Logger.get().debug(TAG, "Unable to create GCM Scheduler", th);
            return null;
        }
    }
}

package androidx.work.impl.workers;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import androidx.work.ListenableWorker;
import androidx.work.Logger;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.model.SystemIdInfo;
import androidx.work.impl.model.SystemIdInfoDao;
import androidx.work.impl.model.WorkNameDao;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.model.WorkSpecDao;
import androidx.work.impl.model.WorkTagDao;
import java.util.List;
import java.util.concurrent.TimeUnit;
import mt.Log1F380D;

/* compiled from: 00E4 */
public class DiagnosticsWorker extends Worker {
    private static final String TAG;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("DiagnosticsWrkr");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public DiagnosticsWorker(Context context, WorkerParameters parameters) {
        super(context, parameters);
    }

    private static String workSpecRow(WorkSpec workSpec, String name, Integer systemId, String tags) {
        String format = String.format("\n%s\t %s\t %s\t %s\t %s\t %s\t", new Object[]{workSpec.id, workSpec.workerClassName, systemId, workSpec.state.name(), name, tags});
        Log1F380D.a((Object) format);
        return format;
    }

    private static String workSpecRows(WorkNameDao workNameDao, WorkTagDao workTagDao, SystemIdInfoDao systemIdInfoDao, List<WorkSpec> list) {
        StringBuilder sb = new StringBuilder();
        String format = String.format("\n Id \t Class Name\t %s\t State\t Unique Name\t Tags\t", new Object[]{Build.VERSION.SDK_INT >= 23 ? "Job Id" : "Alarm Id"});
        Log1F380D.a((Object) format);
        sb.append(format);
        for (WorkSpec next : list) {
            Integer num = null;
            SystemIdInfo systemIdInfo = systemIdInfoDao.getSystemIdInfo(next.id);
            if (systemIdInfo != null) {
                num = Integer.valueOf(systemIdInfo.systemId);
            }
            List<String> namesForWorkSpecId = workNameDao.getNamesForWorkSpecId(next.id);
            List<String> tagsForWorkSpecId = workTagDao.getTagsForWorkSpecId(next.id);
            String join = TextUtils.join(",", namesForWorkSpecId);
            Log1F380D.a((Object) join);
            String workSpecRow = workSpecRow(next, join, num, TextUtils.join(",", tagsForWorkSpecId));
            Log1F380D.a((Object) workSpecRow);
            sb.append(workSpecRow);
        }
        return sb.toString();
    }

    public ListenableWorker.Result doWork() {
        WorkDatabase workDatabase = WorkManagerImpl.getInstance(getApplicationContext()).getWorkDatabase();
        WorkSpecDao workSpecDao = workDatabase.workSpecDao();
        WorkNameDao workNameDao = workDatabase.workNameDao();
        WorkTagDao workTagDao = workDatabase.workTagDao();
        SystemIdInfoDao systemIdInfoDao = workDatabase.systemIdInfoDao();
        List<WorkSpec> recentlyCompletedWork = workSpecDao.getRecentlyCompletedWork(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1));
        List<WorkSpec> runningWork = workSpecDao.getRunningWork();
        List<WorkSpec> allEligibleWorkSpecsForScheduling = workSpecDao.getAllEligibleWorkSpecsForScheduling(200);
        if (recentlyCompletedWork != null && !recentlyCompletedWork.isEmpty()) {
            Logger logger = Logger.get();
            String str = TAG;
            logger.info(str, "Recently completed work:\n\n", new Throwable[0]);
            Logger logger2 = Logger.get();
            String workSpecRows = workSpecRows(workNameDao, workTagDao, systemIdInfoDao, recentlyCompletedWork);
            Log1F380D.a((Object) workSpecRows);
            logger2.info(str, workSpecRows, new Throwable[0]);
        }
        if (runningWork != null && !runningWork.isEmpty()) {
            Logger logger3 = Logger.get();
            String str2 = TAG;
            logger3.info(str2, "Running work:\n\n", new Throwable[0]);
            Logger logger4 = Logger.get();
            String workSpecRows2 = workSpecRows(workNameDao, workTagDao, systemIdInfoDao, runningWork);
            Log1F380D.a((Object) workSpecRows2);
            logger4.info(str2, workSpecRows2, new Throwable[0]);
        }
        if (allEligibleWorkSpecsForScheduling != null && !allEligibleWorkSpecsForScheduling.isEmpty()) {
            Logger logger5 = Logger.get();
            String str3 = TAG;
            logger5.info(str3, "Enqueued work:\n\n", new Throwable[0]);
            Logger logger6 = Logger.get();
            String workSpecRows3 = workSpecRows(workNameDao, workTagDao, systemIdInfoDao, allEligibleWorkSpecsForScheduling);
            Log1F380D.a((Object) workSpecRows3);
            logger6.info(str3, workSpecRows3, new Throwable[0]);
        }
        return ListenableWorker.Result.success();
    }
}

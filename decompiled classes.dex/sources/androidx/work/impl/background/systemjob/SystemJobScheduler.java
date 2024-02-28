package androidx.work.impl.background.systemjob;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.PersistableBundle;
import android.text.TextUtils;
import androidx.work.Logger;
import androidx.work.OutOfQuotaPolicy;
import androidx.work.WorkInfo;
import androidx.work.impl.Scheduler;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.model.SystemIdInfo;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.model.WorkSpecDao;
import androidx.work.impl.utils.IdGenerator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import mt.Log1F380D;

/* compiled from: 00C3 */
public class SystemJobScheduler implements Scheduler {
    private static final String TAG;
    private final Context mContext;
    private final JobScheduler mJobScheduler;
    private final SystemJobInfoConverter mSystemJobInfoConverter;
    private final WorkManagerImpl mWorkManager;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("SystemJobScheduler");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public SystemJobScheduler(Context context, WorkManagerImpl workManager) {
        this(context, workManager, (JobScheduler) context.getSystemService("jobscheduler"), new SystemJobInfoConverter(context));
    }

    public SystemJobScheduler(Context context, WorkManagerImpl workManager, JobScheduler jobScheduler, SystemJobInfoConverter systemJobInfoConverter) {
        this.mContext = context;
        this.mWorkManager = workManager;
        this.mJobScheduler = jobScheduler;
        this.mSystemJobInfoConverter = systemJobInfoConverter;
    }

    public static void cancelAll(Context context) {
        List<JobInfo> pendingJobs;
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService("jobscheduler");
        if (jobScheduler != null && (pendingJobs = getPendingJobs(context, jobScheduler)) != null && !pendingJobs.isEmpty()) {
            for (JobInfo id : pendingJobs) {
                cancelJobById(jobScheduler, id.getId());
            }
        }
    }

    private static List<JobInfo> getPendingJobs(Context context, JobScheduler jobScheduler) {
        List<JobInfo> list = null;
        try {
            list = jobScheduler.getAllPendingJobs();
        } catch (Throwable th) {
            Logger.get().error(TAG, "getAllPendingJobs() is not reliable on this device.", th);
        }
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(list.size());
        ComponentName componentName = new ComponentName(context, SystemJobService.class);
        for (JobInfo next : list) {
            if (componentName.equals(next.getService())) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    private static String getWorkSpecIdFromJobInfo(JobInfo jobInfo) {
        PersistableBundle extras = jobInfo.getExtras();
        if (extras == null) {
            return null;
        }
        try {
            if (extras.containsKey("EXTRA_WORK_SPEC_ID")) {
                return extras.getString("EXTRA_WORK_SPEC_ID");
            }
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void cancel(String workSpecId) {
        List<Integer> pendingJobIds = getPendingJobIds(this.mContext, this.mJobScheduler, workSpecId);
        if (pendingJobIds != null && !pendingJobIds.isEmpty()) {
            for (Integer intValue : pendingJobIds) {
                cancelJobById(this.mJobScheduler, intValue.intValue());
            }
            this.mWorkManager.getWorkDatabase().systemIdInfoDao().removeSystemIdInfo(workSpecId);
        }
    }

    public boolean hasLimitedSchedulingSlots() {
        return true;
    }

    public void schedule(WorkSpec... workSpecs) {
        List<Integer> pendingJobIds;
        WorkDatabase workDatabase = this.mWorkManager.getWorkDatabase();
        IdGenerator idGenerator = new IdGenerator(workDatabase);
        int length = workSpecs.length;
        int i = 0;
        while (i < length) {
            WorkSpec workSpec = workSpecs[i];
            workDatabase.beginTransaction();
            try {
                WorkSpec workSpec2 = workDatabase.workSpecDao().getWorkSpec(workSpec.id);
                if (workSpec2 == null) {
                    Logger.get().warning(TAG, "Skipping scheduling " + workSpec.id + " because it's no longer in the DB", new Throwable[0]);
                    workDatabase.setTransactionSuccessful();
                } else if (workSpec2.state != WorkInfo.State.ENQUEUED) {
                    Logger.get().warning(TAG, "Skipping scheduling " + workSpec.id + " because it is no longer enqueued", new Throwable[0]);
                    workDatabase.setTransactionSuccessful();
                    workDatabase.endTransaction();
                } else {
                    SystemIdInfo systemIdInfo = workDatabase.systemIdInfoDao().getSystemIdInfo(workSpec.id);
                    int nextJobSchedulerIdWithRange = systemIdInfo != null ? systemIdInfo.systemId : idGenerator.nextJobSchedulerIdWithRange(this.mWorkManager.getConfiguration().getMinJobSchedulerId(), this.mWorkManager.getConfiguration().getMaxJobSchedulerId());
                    if (systemIdInfo == null) {
                        this.mWorkManager.getWorkDatabase().systemIdInfoDao().insertSystemIdInfo(new SystemIdInfo(workSpec.id, nextJobSchedulerIdWithRange));
                    }
                    scheduleInternal(workSpec, nextJobSchedulerIdWithRange);
                    if (Build.VERSION.SDK_INT == 23 && (pendingJobIds = getPendingJobIds(this.mContext, this.mJobScheduler, workSpec.id)) != null) {
                        int indexOf = pendingJobIds.indexOf(Integer.valueOf(nextJobSchedulerIdWithRange));
                        if (indexOf >= 0) {
                            pendingJobIds.remove(indexOf);
                        }
                        scheduleInternal(workSpec, !pendingJobIds.isEmpty() ? pendingJobIds.get(0).intValue() : idGenerator.nextJobSchedulerIdWithRange(this.mWorkManager.getConfiguration().getMinJobSchedulerId(), this.mWorkManager.getConfiguration().getMaxJobSchedulerId()));
                    }
                    workDatabase.setTransactionSuccessful();
                    workDatabase.endTransaction();
                }
                i++;
            } finally {
                workDatabase.endTransaction();
            }
        }
    }

    private static void cancelJobById(JobScheduler jobScheduler, int id) {
        try {
            jobScheduler.cancel(id);
        } catch (Throwable th) {
            Logger logger = Logger.get();
            String str = TAG;
            String format = String.format(Locale.getDefault(), "Exception while trying to cancel job (%d)", new Object[]{Integer.valueOf(id)});
            Log1F380D.a((Object) format);
            logger.error(str, format, th);
        }
    }

    private static List<Integer> getPendingJobIds(Context context, JobScheduler jobScheduler, String workSpecId) {
        List<JobInfo> pendingJobs = getPendingJobs(context, jobScheduler);
        if (pendingJobs == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(2);
        for (JobInfo next : pendingJobs) {
            String workSpecIdFromJobInfo = getWorkSpecIdFromJobInfo(next);
            Log1F380D.a((Object) workSpecIdFromJobInfo);
            if (workSpecId.equals(workSpecIdFromJobInfo)) {
                arrayList.add(Integer.valueOf(next.getId()));
            }
        }
        return arrayList;
    }

    public static boolean reconcileJobs(Context context, WorkManagerImpl workManager) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService("jobscheduler");
        List<JobInfo> pendingJobs = getPendingJobs(context, jobScheduler);
        List<String> workSpecIds = workManager.getWorkDatabase().systemIdInfoDao().getWorkSpecIds();
        HashSet hashSet = new HashSet(pendingJobs != null ? pendingJobs.size() : 0);
        if (pendingJobs != null && !pendingJobs.isEmpty()) {
            for (JobInfo next : pendingJobs) {
                String workSpecIdFromJobInfo = getWorkSpecIdFromJobInfo(next);
                Log1F380D.a((Object) workSpecIdFromJobInfo);
                if (!TextUtils.isEmpty(workSpecIdFromJobInfo)) {
                    hashSet.add(workSpecIdFromJobInfo);
                } else {
                    cancelJobById(jobScheduler, next.getId());
                }
            }
        }
        boolean z = false;
        Iterator<String> it = workSpecIds.iterator();
        while (true) {
            if (it.hasNext()) {
                if (!hashSet.contains(it.next())) {
                    Logger.get().debug(TAG, "Reconciling jobs", new Throwable[0]);
                    z = true;
                    break;
                }
            } else {
                break;
            }
        }
        if (z) {
            WorkDatabase workDatabase = workManager.getWorkDatabase();
            workDatabase.beginTransaction();
            try {
                WorkSpecDao workSpecDao = workDatabase.workSpecDao();
                for (String markWorkSpecScheduled : workSpecIds) {
                    workSpecDao.markWorkSpecScheduled(markWorkSpecScheduled, -1);
                }
                workDatabase.setTransactionSuccessful();
            } finally {
                workDatabase.endTransaction();
            }
        }
        return z;
    }

    public void scheduleInternal(WorkSpec workSpec, int jobId) {
        JobInfo convert = this.mSystemJobInfoConverter.convert(workSpec, jobId);
        Logger logger = Logger.get();
        String str = TAG;
        String format = String.format("Scheduling work ID %s Job ID %s", new Object[]{workSpec.id, Integer.valueOf(jobId)});
        Log1F380D.a((Object) format);
        logger.debug(str, format, new Throwable[0]);
        try {
            if (this.mJobScheduler.schedule(convert) == 0) {
                Logger logger2 = Logger.get();
                String format2 = String.format("Unable to schedule work ID %s", new Object[]{workSpec.id});
                Log1F380D.a((Object) format2);
                logger2.warning(str, format2, new Throwable[0]);
                if (workSpec.expedited && workSpec.outOfQuotaPolicy == OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST) {
                    workSpec.expedited = false;
                    String format3 = String.format("Scheduling a non-expedited job (work ID %s)", new Object[]{workSpec.id});
                    Log1F380D.a((Object) format3);
                    Logger.get().debug(str, format3, new Throwable[0]);
                    scheduleInternal(workSpec, jobId);
                }
            }
        } catch (IllegalStateException e) {
            List<JobInfo> pendingJobs = getPendingJobs(this.mContext, this.mJobScheduler);
            String format4 = String.format(Locale.getDefault(), "JobScheduler 100 job limit exceeded.  We count %d WorkManager jobs in JobScheduler; we have %d tracked jobs in our DB; our Configuration limit is %d.", new Object[]{Integer.valueOf(pendingJobs != null ? pendingJobs.size() : 0), Integer.valueOf(this.mWorkManager.getWorkDatabase().workSpecDao().getScheduledWork().size()), Integer.valueOf(this.mWorkManager.getConfiguration().getMaxSchedulerLimit())});
            Log1F380D.a((Object) format4);
            Logger.get().error(TAG, format4, new Throwable[0]);
            throw new IllegalStateException(format4, e);
        } catch (Throwable th) {
            Logger logger3 = Logger.get();
            String str2 = TAG;
            String format5 = String.format("Unable to schedule %s", new Object[]{workSpec});
            Log1F380D.a((Object) format5);
            logger3.error(str2, format5, th);
        }
    }
}

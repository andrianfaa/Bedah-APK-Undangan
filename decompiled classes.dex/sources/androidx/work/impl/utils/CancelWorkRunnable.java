package androidx.work.impl.utils;

import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.impl.OperationImpl;
import androidx.work.impl.Scheduler;
import androidx.work.impl.Schedulers;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.model.DependencyDao;
import androidx.work.impl.model.WorkSpecDao;
import java.util.LinkedList;
import java.util.UUID;

public abstract class CancelWorkRunnable implements Runnable {
    private final OperationImpl mOperation = new OperationImpl();

    public static CancelWorkRunnable forAll(final WorkManagerImpl workManagerImpl) {
        return new CancelWorkRunnable() {
            /* access modifiers changed from: package-private */
            public void runInternal() {
                WorkDatabase workDatabase = WorkManagerImpl.this.getWorkDatabase();
                workDatabase.beginTransaction();
                try {
                    for (String cancel : workDatabase.workSpecDao().getAllUnfinishedWork()) {
                        cancel(WorkManagerImpl.this, cancel);
                    }
                    new PreferenceUtils(WorkManagerImpl.this.getWorkDatabase()).setLastCancelAllTimeMillis(System.currentTimeMillis());
                    workDatabase.setTransactionSuccessful();
                } finally {
                    workDatabase.endTransaction();
                }
            }
        };
    }

    public static CancelWorkRunnable forId(final UUID id, final WorkManagerImpl workManagerImpl) {
        return new CancelWorkRunnable() {
            /* JADX INFO: finally extract failed */
            /* access modifiers changed from: package-private */
            public void runInternal() {
                WorkDatabase workDatabase = WorkManagerImpl.this.getWorkDatabase();
                workDatabase.beginTransaction();
                try {
                    cancel(WorkManagerImpl.this, id.toString());
                    workDatabase.setTransactionSuccessful();
                    workDatabase.endTransaction();
                    reschedulePendingWorkers(WorkManagerImpl.this);
                } catch (Throwable th) {
                    workDatabase.endTransaction();
                    throw th;
                }
            }
        };
    }

    public static CancelWorkRunnable forName(final String name, final WorkManagerImpl workManagerImpl, final boolean allowReschedule) {
        return new CancelWorkRunnable() {
            /* JADX INFO: finally extract failed */
            /* access modifiers changed from: package-private */
            public void runInternal() {
                WorkDatabase workDatabase = WorkManagerImpl.this.getWorkDatabase();
                workDatabase.beginTransaction();
                try {
                    for (String cancel : workDatabase.workSpecDao().getUnfinishedWorkWithName(name)) {
                        cancel(WorkManagerImpl.this, cancel);
                    }
                    workDatabase.setTransactionSuccessful();
                    workDatabase.endTransaction();
                    if (allowReschedule) {
                        reschedulePendingWorkers(WorkManagerImpl.this);
                    }
                } catch (Throwable th) {
                    workDatabase.endTransaction();
                    throw th;
                }
            }
        };
    }

    public static CancelWorkRunnable forTag(final String tag, final WorkManagerImpl workManagerImpl) {
        return new CancelWorkRunnable() {
            /* JADX INFO: finally extract failed */
            /* access modifiers changed from: package-private */
            public void runInternal() {
                WorkDatabase workDatabase = WorkManagerImpl.this.getWorkDatabase();
                workDatabase.beginTransaction();
                try {
                    for (String cancel : workDatabase.workSpecDao().getUnfinishedWorkWithTag(tag)) {
                        cancel(WorkManagerImpl.this, cancel);
                    }
                    workDatabase.setTransactionSuccessful();
                    workDatabase.endTransaction();
                    reschedulePendingWorkers(WorkManagerImpl.this);
                } catch (Throwable th) {
                    workDatabase.endTransaction();
                    throw th;
                }
            }
        };
    }

    private void iterativelyCancelWorkAndDependents(WorkDatabase workDatabase, String workSpecId) {
        WorkSpecDao workSpecDao = workDatabase.workSpecDao();
        DependencyDao dependencyDao = workDatabase.dependencyDao();
        LinkedList linkedList = new LinkedList();
        linkedList.add(workSpecId);
        while (!linkedList.isEmpty()) {
            String str = (String) linkedList.remove();
            WorkInfo.State state = workSpecDao.getState(str);
            if (!(state == WorkInfo.State.SUCCEEDED || state == WorkInfo.State.FAILED)) {
                workSpecDao.setState(WorkInfo.State.CANCELLED, str);
            }
            linkedList.addAll(dependencyDao.getDependentWorkIds(str));
        }
    }

    /* access modifiers changed from: package-private */
    public void cancel(WorkManagerImpl workManagerImpl, String workSpecId) {
        iterativelyCancelWorkAndDependents(workManagerImpl.getWorkDatabase(), workSpecId);
        workManagerImpl.getProcessor().stopAndCancelWork(workSpecId);
        for (Scheduler cancel : workManagerImpl.getSchedulers()) {
            cancel.cancel(workSpecId);
        }
    }

    public Operation getOperation() {
        return this.mOperation;
    }

    /* access modifiers changed from: package-private */
    public void reschedulePendingWorkers(WorkManagerImpl workManagerImpl) {
        Schedulers.schedule(workManagerImpl.getConfiguration(), workManagerImpl.getWorkDatabase(), workManagerImpl.getSchedulers());
    }

    public void run() {
        try {
            runInternal();
            this.mOperation.setState(Operation.SUCCESS);
        } catch (Throwable th) {
            this.mOperation.setState(new Operation.State.FAILURE(th));
        }
    }

    /* access modifiers changed from: package-private */
    public abstract void runInternal();
}

package androidx.work.impl;

import android.text.TextUtils;
import androidx.lifecycle.LiveData;
import androidx.work.ArrayCreatingInputMerger;
import androidx.work.ExistingWorkPolicy;
import androidx.work.Logger;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkRequest;
import androidx.work.impl.utils.EnqueueRunnable;
import androidx.work.impl.utils.StatusRunnable;
import androidx.work.impl.workers.CombineContinuationsWorker;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import mt.Log1F380D;

/* compiled from: 00AB */
public class WorkContinuationImpl extends WorkContinuation {
    private static final String TAG;
    private final List<String> mAllIds;
    private boolean mEnqueued;
    private final ExistingWorkPolicy mExistingWorkPolicy;
    private final List<String> mIds;
    private final String mName;
    private Operation mOperation;
    private final List<WorkContinuationImpl> mParents;
    private final List<? extends WorkRequest> mWork;
    private final WorkManagerImpl mWorkManagerImpl;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("WorkContinuationImpl");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public WorkContinuationImpl(WorkManagerImpl workManagerImpl, String name, ExistingWorkPolicy existingWorkPolicy, List<? extends WorkRequest> list) {
        this(workManagerImpl, name, existingWorkPolicy, list, (List<WorkContinuationImpl>) null);
    }

    public WorkContinuationImpl(WorkManagerImpl workManagerImpl, String name, ExistingWorkPolicy existingWorkPolicy, List<? extends WorkRequest> list, List<WorkContinuationImpl> list2) {
        this.mWorkManagerImpl = workManagerImpl;
        this.mName = name;
        this.mExistingWorkPolicy = existingWorkPolicy;
        this.mWork = list;
        this.mParents = list2;
        this.mIds = new ArrayList(list.size());
        this.mAllIds = new ArrayList();
        if (list2 != null) {
            for (WorkContinuationImpl workContinuationImpl : list2) {
                this.mAllIds.addAll(workContinuationImpl.mAllIds);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            String stringId = ((WorkRequest) list.get(i)).getStringId();
            this.mIds.add(stringId);
            this.mAllIds.add(stringId);
        }
    }

    public WorkContinuationImpl(WorkManagerImpl workManagerImpl, List<? extends WorkRequest> list) {
        this(workManagerImpl, (String) null, ExistingWorkPolicy.KEEP, list, (List<WorkContinuationImpl>) null);
    }

    private static boolean hasCycles(WorkContinuationImpl continuation, Set<String> set) {
        set.addAll(continuation.getIds());
        Set<String> prerequisitesFor = prerequisitesFor(continuation);
        for (String contains : set) {
            if (prerequisitesFor.contains(contains)) {
                return true;
            }
        }
        List<WorkContinuationImpl> parents = continuation.getParents();
        if (parents != null && !parents.isEmpty()) {
            for (WorkContinuationImpl hasCycles : parents) {
                if (hasCycles(hasCycles, set)) {
                    return true;
                }
            }
        }
        set.removeAll(continuation.getIds());
        return false;
    }

    public static Set<String> prerequisitesFor(WorkContinuationImpl continuation) {
        HashSet hashSet = new HashSet();
        List<WorkContinuationImpl> parents = continuation.getParents();
        if (parents != null && !parents.isEmpty()) {
            for (WorkContinuationImpl ids : parents) {
                hashSet.addAll(ids.getIds());
            }
        }
        return hashSet;
    }

    /* access modifiers changed from: protected */
    public WorkContinuation combineInternal(List<WorkContinuation> list) {
        OneTimeWorkRequest oneTimeWorkRequest = (OneTimeWorkRequest) new OneTimeWorkRequest.Builder(CombineContinuationsWorker.class).setInputMerger(ArrayCreatingInputMerger.class).build();
        ArrayList arrayList = new ArrayList(list.size());
        Iterator<WorkContinuation> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add((WorkContinuationImpl) it.next());
        }
        return new WorkContinuationImpl(this.mWorkManagerImpl, (String) null, ExistingWorkPolicy.KEEP, Collections.singletonList(oneTimeWorkRequest), arrayList);
    }

    public List<String> getAllIds() {
        return this.mAllIds;
    }

    public ExistingWorkPolicy getExistingWorkPolicy() {
        return this.mExistingWorkPolicy;
    }

    public List<String> getIds() {
        return this.mIds;
    }

    public String getName() {
        return this.mName;
    }

    public List<WorkContinuationImpl> getParents() {
        return this.mParents;
    }

    public List<? extends WorkRequest> getWork() {
        return this.mWork;
    }

    public ListenableFuture<List<WorkInfo>> getWorkInfos() {
        StatusRunnable<List<WorkInfo>> forStringIds = StatusRunnable.forStringIds(this.mWorkManagerImpl, this.mAllIds);
        this.mWorkManagerImpl.getWorkTaskExecutor().executeOnBackgroundThread(forStringIds);
        return forStringIds.getFuture();
    }

    public LiveData<List<WorkInfo>> getWorkInfosLiveData() {
        return this.mWorkManagerImpl.getWorkInfosById(this.mAllIds);
    }

    public WorkManagerImpl getWorkManagerImpl() {
        return this.mWorkManagerImpl;
    }

    public boolean hasCycles() {
        return hasCycles(this, new HashSet());
    }

    public boolean isEnqueued() {
        return this.mEnqueued;
    }

    public void markEnqueued() {
        this.mEnqueued = true;
    }

    public WorkContinuation then(List<OneTimeWorkRequest> list) {
        if (list.isEmpty()) {
            return this;
        }
        return new WorkContinuationImpl(this.mWorkManagerImpl, this.mName, ExistingWorkPolicy.KEEP, list, Collections.singletonList(this));
    }

    public Operation enqueue() {
        if (!this.mEnqueued) {
            EnqueueRunnable enqueueRunnable = new EnqueueRunnable(this);
            this.mWorkManagerImpl.getWorkTaskExecutor().executeOnBackgroundThread(enqueueRunnable);
            this.mOperation = enqueueRunnable.getOperation();
        } else {
            Logger logger = Logger.get();
            String str = TAG;
            String join = TextUtils.join(", ", this.mIds);
            Log1F380D.a((Object) join);
            String format = String.format("Already enqueued work ids (%s)", new Object[]{join});
            Log1F380D.a((Object) format);
            logger.warning(str, format, new Throwable[0]);
        }
        return this.mOperation;
    }
}

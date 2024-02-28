package androidx.room;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.LiveData;
import androidx.room.InvalidationTracker;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

class RoomTrackingLiveData<T> extends LiveData<T> {
    final Callable<T> mComputeFunction;
    final AtomicBoolean mComputing = new AtomicBoolean(false);
    private final InvalidationLiveDataContainer mContainer;
    final RoomDatabase mDatabase;
    final boolean mInTransaction;
    final AtomicBoolean mInvalid = new AtomicBoolean(true);
    final Runnable mInvalidationRunnable = new Runnable() {
        public void run() {
            boolean hasActiveObservers = RoomTrackingLiveData.this.hasActiveObservers();
            if (RoomTrackingLiveData.this.mInvalid.compareAndSet(false, true) && hasActiveObservers) {
                RoomTrackingLiveData.this.getQueryExecutor().execute(RoomTrackingLiveData.this.mRefreshRunnable);
            }
        }
    };
    final InvalidationTracker.Observer mObserver;
    final Runnable mRefreshRunnable = new Runnable() {
        /* JADX WARNING: Removed duplicated region for block: B:5:0x0026  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r6 = this;
                androidx.room.RoomTrackingLiveData r0 = androidx.room.RoomTrackingLiveData.this
                java.util.concurrent.atomic.AtomicBoolean r0 = r0.mRegisteredObserver
                r1 = 0
                r2 = 1
                boolean r0 = r0.compareAndSet(r1, r2)
                if (r0 == 0) goto L_0x001b
                androidx.room.RoomTrackingLiveData r0 = androidx.room.RoomTrackingLiveData.this
                androidx.room.RoomDatabase r0 = r0.mDatabase
                androidx.room.InvalidationTracker r0 = r0.getInvalidationTracker()
                androidx.room.RoomTrackingLiveData r3 = androidx.room.RoomTrackingLiveData.this
                androidx.room.InvalidationTracker$Observer r3 = r3.mObserver
                r0.addWeakObserver(r3)
            L_0x001b:
                r0 = 0
                androidx.room.RoomTrackingLiveData r3 = androidx.room.RoomTrackingLiveData.this
                java.util.concurrent.atomic.AtomicBoolean r3 = r3.mComputing
                boolean r3 = r3.compareAndSet(r1, r2)
                if (r3 == 0) goto L_0x005d
                r3 = 0
            L_0x0027:
                androidx.room.RoomTrackingLiveData r4 = androidx.room.RoomTrackingLiveData.this     // Catch:{ all -> 0x0054 }
                java.util.concurrent.atomic.AtomicBoolean r4 = r4.mInvalid     // Catch:{ all -> 0x0054 }
                boolean r4 = r4.compareAndSet(r2, r1)     // Catch:{ all -> 0x0054 }
                if (r4 == 0) goto L_0x0045
                r0 = 1
                androidx.room.RoomTrackingLiveData r4 = androidx.room.RoomTrackingLiveData.this     // Catch:{ Exception -> 0x003c }
                java.util.concurrent.Callable<T> r4 = r4.mComputeFunction     // Catch:{ Exception -> 0x003c }
                java.lang.Object r4 = r4.call()     // Catch:{ Exception -> 0x003c }
                r3 = r4
                goto L_0x0027
            L_0x003c:
                r2 = move-exception
                java.lang.RuntimeException r4 = new java.lang.RuntimeException     // Catch:{ all -> 0x0054 }
                java.lang.String r5 = "Exception while computing database live data."
                r4.<init>(r5, r2)     // Catch:{ all -> 0x0054 }
                throw r4     // Catch:{ all -> 0x0054 }
            L_0x0045:
                if (r0 == 0) goto L_0x004c
                androidx.room.RoomTrackingLiveData r4 = androidx.room.RoomTrackingLiveData.this     // Catch:{ all -> 0x0054 }
                r4.postValue(r3)     // Catch:{ all -> 0x0054 }
            L_0x004c:
                androidx.room.RoomTrackingLiveData r3 = androidx.room.RoomTrackingLiveData.this
                java.util.concurrent.atomic.AtomicBoolean r3 = r3.mComputing
                r3.set(r1)
                goto L_0x005d
            L_0x0054:
                r2 = move-exception
                androidx.room.RoomTrackingLiveData r3 = androidx.room.RoomTrackingLiveData.this
                java.util.concurrent.atomic.AtomicBoolean r3 = r3.mComputing
                r3.set(r1)
                throw r2
            L_0x005d:
                if (r0 == 0) goto L_0x0069
                androidx.room.RoomTrackingLiveData r3 = androidx.room.RoomTrackingLiveData.this
                java.util.concurrent.atomic.AtomicBoolean r3 = r3.mInvalid
                boolean r3 = r3.get()
                if (r3 != 0) goto L_0x001b
            L_0x0069:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.room.RoomTrackingLiveData.AnonymousClass1.run():void");
        }
    };
    final AtomicBoolean mRegisteredObserver = new AtomicBoolean(false);

    RoomTrackingLiveData(RoomDatabase database, InvalidationLiveDataContainer container, boolean inTransaction, Callable<T> callable, String[] tableNames) {
        this.mDatabase = database;
        this.mInTransaction = inTransaction;
        this.mComputeFunction = callable;
        this.mContainer = container;
        this.mObserver = new InvalidationTracker.Observer(tableNames) {
            public void onInvalidated(Set<String> set) {
                ArchTaskExecutor.getInstance().executeOnMainThread(RoomTrackingLiveData.this.mInvalidationRunnable);
            }
        };
    }

    /* access modifiers changed from: package-private */
    public Executor getQueryExecutor() {
        return this.mInTransaction ? this.mDatabase.getTransactionExecutor() : this.mDatabase.getQueryExecutor();
    }

    /* access modifiers changed from: protected */
    public void onActive() {
        super.onActive();
        this.mContainer.onActive(this);
        getQueryExecutor().execute(this.mRefreshRunnable);
    }

    /* access modifiers changed from: protected */
    public void onInactive() {
        super.onInactive();
        this.mContainer.onInactive(this);
    }
}

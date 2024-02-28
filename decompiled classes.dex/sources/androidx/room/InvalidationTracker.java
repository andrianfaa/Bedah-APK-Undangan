package androidx.room;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import androidx.arch.core.internal.SafeIterableMap;
import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

public class InvalidationTracker {
    private static final String CREATE_TRACKING_TABLE_SQL = "CREATE TEMP TABLE room_table_modification_log(table_id INTEGER PRIMARY KEY, invalidated INTEGER NOT NULL DEFAULT 0)";
    private static final String INVALIDATED_COLUMN_NAME = "invalidated";
    static final String RESET_UPDATED_TABLES_SQL = "UPDATE room_table_modification_log SET invalidated = 0 WHERE invalidated = 1 ";
    static final String SELECT_UPDATED_TABLES_SQL = "SELECT * FROM room_table_modification_log WHERE invalidated = 1;";
    private static final String TABLE_ID_COLUMN_NAME = "table_id";
    private static final String[] TRIGGERS = {"UPDATE", "DELETE", "INSERT"};
    private static final String UPDATE_TABLE_NAME = "room_table_modification_log";
    volatile SupportSQLiteStatement mCleanupStatement;
    final RoomDatabase mDatabase;
    private volatile boolean mInitialized;
    private final InvalidationLiveDataContainer mInvalidationLiveDataContainer;
    private MultiInstanceInvalidationClient mMultiInstanceInvalidationClient;
    private ObservedTableTracker mObservedTableTracker;
    final SafeIterableMap<Observer, ObserverWrapper> mObserverMap;
    AtomicBoolean mPendingRefresh;
    Runnable mRefreshRunnable;
    final HashMap<String, Integer> mTableIdLookup;
    final String[] mTableNames;
    private Map<String, Set<String>> mViewTables;

    static class ObservedTableTracker {
        static final int ADD = 1;
        static final int NO_OP = 0;
        static final int REMOVE = 2;
        boolean mNeedsSync;
        boolean mPendingSync;
        final long[] mTableObservers;
        final int[] mTriggerStateChanges;
        final boolean[] mTriggerStates;

        ObservedTableTracker(int tableCount) {
            long[] jArr = new long[tableCount];
            this.mTableObservers = jArr;
            boolean[] zArr = new boolean[tableCount];
            this.mTriggerStates = zArr;
            this.mTriggerStateChanges = new int[tableCount];
            Arrays.fill(jArr, 0);
            Arrays.fill(zArr, false);
        }

        /* access modifiers changed from: package-private */
        public int[] getTablesToSync() {
            synchronized (this) {
                if (this.mNeedsSync) {
                    if (!this.mPendingSync) {
                        int length = this.mTableObservers.length;
                        int i = 0;
                        while (true) {
                            int i2 = 1;
                            if (i < length) {
                                boolean z = this.mTableObservers[i] > 0;
                                boolean[] zArr = this.mTriggerStates;
                                if (z != zArr[i]) {
                                    int[] iArr = this.mTriggerStateChanges;
                                    if (!z) {
                                        i2 = 2;
                                    }
                                    iArr[i] = i2;
                                } else {
                                    this.mTriggerStateChanges[i] = 0;
                                }
                                zArr[i] = z;
                                i++;
                            } else {
                                this.mPendingSync = true;
                                this.mNeedsSync = false;
                                int[] iArr2 = this.mTriggerStateChanges;
                                return iArr2;
                            }
                        }
                    }
                }
                return null;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean onAdded(int... tableIds) {
            boolean z = false;
            synchronized (this) {
                for (int i : tableIds) {
                    long[] jArr = this.mTableObservers;
                    long j = jArr[i];
                    jArr[i] = 1 + j;
                    if (j == 0) {
                        this.mNeedsSync = true;
                        z = true;
                    }
                }
            }
            return z;
        }

        /* access modifiers changed from: package-private */
        public boolean onRemoved(int... tableIds) {
            boolean z = false;
            synchronized (this) {
                for (int i : tableIds) {
                    long[] jArr = this.mTableObservers;
                    long j = jArr[i];
                    jArr[i] = j - 1;
                    if (j == 1) {
                        this.mNeedsSync = true;
                        z = true;
                    }
                }
            }
            return z;
        }

        /* access modifiers changed from: package-private */
        public void onSyncCompleted() {
            synchronized (this) {
                this.mPendingSync = false;
            }
        }
    }

    public static abstract class Observer {
        final String[] mTables;

        protected Observer(String firstTable, String... rest) {
            String[] strArr = (String[]) Arrays.copyOf(rest, rest.length + 1);
            this.mTables = strArr;
            strArr[rest.length] = firstTable;
        }

        public Observer(String[] tables) {
            this.mTables = (String[]) Arrays.copyOf(tables, tables.length);
        }

        /* access modifiers changed from: package-private */
        public boolean isRemote() {
            return false;
        }

        public abstract void onInvalidated(Set<String> set);
    }

    static class ObserverWrapper {
        final Observer mObserver;
        private final Set<String> mSingleTableSet;
        final int[] mTableIds;
        private final String[] mTableNames;

        ObserverWrapper(Observer observer, int[] tableIds, String[] tableNames) {
            this.mObserver = observer;
            this.mTableIds = tableIds;
            this.mTableNames = tableNames;
            if (tableIds.length == 1) {
                HashSet hashSet = new HashSet();
                hashSet.add(tableNames[0]);
                this.mSingleTableSet = Collections.unmodifiableSet(hashSet);
                return;
            }
            this.mSingleTableSet = null;
        }

        /* access modifiers changed from: package-private */
        public void notifyByTableInvalidStatus(Set<Integer> set) {
            Set<String> set2 = null;
            int length = this.mTableIds.length;
            for (int i = 0; i < length; i++) {
                if (set.contains(Integer.valueOf(this.mTableIds[i]))) {
                    if (length == 1) {
                        set2 = this.mSingleTableSet;
                    } else {
                        if (set2 == null) {
                            set2 = new HashSet<>(length);
                        }
                        set2.add(this.mTableNames[i]);
                    }
                }
            }
            if (set2 != null) {
                this.mObserver.onInvalidated(set2);
            }
        }

        /* access modifiers changed from: package-private */
        public void notifyByTableNames(String[] tables) {
            Set<String> set = null;
            if (this.mTableNames.length == 1) {
                int length = tables.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    } else if (tables[i].equalsIgnoreCase(this.mTableNames[0])) {
                        set = this.mSingleTableSet;
                        break;
                    } else {
                        i++;
                    }
                }
            } else {
                HashSet hashSet = new HashSet();
                for (String str : tables) {
                    String[] strArr = this.mTableNames;
                    int length2 = strArr.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length2) {
                            break;
                        }
                        String str2 = strArr[i2];
                        if (str2.equalsIgnoreCase(str)) {
                            hashSet.add(str2);
                            break;
                        }
                        i2++;
                    }
                }
                if (hashSet.size() > 0) {
                    set = hashSet;
                }
            }
            if (set != null) {
                this.mObserver.onInvalidated(set);
            }
        }
    }

    static class WeakObserver extends Observer {
        final WeakReference<Observer> mDelegateRef;
        final InvalidationTracker mTracker;

        WeakObserver(InvalidationTracker tracker, Observer delegate) {
            super(delegate.mTables);
            this.mTracker = tracker;
            this.mDelegateRef = new WeakReference<>(delegate);
        }

        public void onInvalidated(Set<String> set) {
            Observer observer = (Observer) this.mDelegateRef.get();
            if (observer == null) {
                this.mTracker.removeObserver(this);
            } else {
                observer.onInvalidated(set);
            }
        }
    }

    public InvalidationTracker(RoomDatabase database, Map<String, String> map, Map<String, Set<String>> map2, String... tableNames) {
        this.mPendingRefresh = new AtomicBoolean(false);
        this.mInitialized = false;
        this.mObserverMap = new SafeIterableMap<>();
        this.mRefreshRunnable = new Runnable() {
            /* JADX INFO: finally extract failed */
            private Set<Integer> checkUpdatedTable() {
                HashSet hashSet = new HashSet();
                Cursor query = InvalidationTracker.this.mDatabase.query(new SimpleSQLiteQuery(InvalidationTracker.SELECT_UPDATED_TABLES_SQL));
                while (query.moveToNext()) {
                    try {
                        hashSet.add(Integer.valueOf(query.getInt(0)));
                    } catch (Throwable th) {
                        query.close();
                        throw th;
                    }
                }
                query.close();
                if (!hashSet.isEmpty()) {
                    InvalidationTracker.this.mCleanupStatement.executeUpdateDelete();
                }
                return hashSet;
            }

            public void run() {
                SupportSQLiteDatabase writableDatabase;
                Lock closeLock = InvalidationTracker.this.mDatabase.getCloseLock();
                Set<Integer> set = null;
                try {
                    closeLock.lock();
                    if (!InvalidationTracker.this.ensureInitialization()) {
                        closeLock.unlock();
                    } else if (!InvalidationTracker.this.mPendingRefresh.compareAndSet(true, false)) {
                        closeLock.unlock();
                    } else if (InvalidationTracker.this.mDatabase.inTransaction()) {
                        closeLock.unlock();
                    } else {
                        if (InvalidationTracker.this.mDatabase.mWriteAheadLoggingEnabled) {
                            writableDatabase = InvalidationTracker.this.mDatabase.getOpenHelper().getWritableDatabase();
                            writableDatabase.beginTransaction();
                            set = checkUpdatedTable();
                            writableDatabase.setTransactionSuccessful();
                            writableDatabase.endTransaction();
                        } else {
                            set = checkUpdatedTable();
                        }
                        closeLock.unlock();
                        if (set != null && !set.isEmpty()) {
                            synchronized (InvalidationTracker.this.mObserverMap) {
                                Iterator<Map.Entry<Observer, ObserverWrapper>> it = InvalidationTracker.this.mObserverMap.iterator();
                                while (it.hasNext()) {
                                    ((ObserverWrapper) it.next().getValue()).notifyByTableInvalidStatus(set);
                                }
                            }
                        }
                    }
                } catch (SQLiteException | IllegalStateException e) {
                    try {
                        Log.e("ROOM", "Cannot run invalidation tracker. Is the db closed?", e);
                    } catch (Throwable th) {
                        closeLock.unlock();
                        throw th;
                    }
                } catch (Throwable th2) {
                    writableDatabase.endTransaction();
                    throw th2;
                }
            }
        };
        this.mDatabase = database;
        this.mObservedTableTracker = new ObservedTableTracker(tableNames.length);
        this.mTableIdLookup = new HashMap<>();
        this.mViewTables = map2;
        this.mInvalidationLiveDataContainer = new InvalidationLiveDataContainer(database);
        int length = tableNames.length;
        this.mTableNames = new String[length];
        for (int i = 0; i < length; i++) {
            String lowerCase = tableNames[i].toLowerCase(Locale.US);
            this.mTableIdLookup.put(lowerCase, Integer.valueOf(i));
            String str = map.get(tableNames[i]);
            if (str != null) {
                this.mTableNames[i] = str.toLowerCase(Locale.US);
            } else {
                this.mTableNames[i] = lowerCase;
            }
        }
        for (Map.Entry next : map.entrySet()) {
            String lowerCase2 = ((String) next.getValue()).toLowerCase(Locale.US);
            if (this.mTableIdLookup.containsKey(lowerCase2)) {
                String lowerCase3 = ((String) next.getKey()).toLowerCase(Locale.US);
                HashMap<String, Integer> hashMap = this.mTableIdLookup;
                hashMap.put(lowerCase3, hashMap.get(lowerCase2));
            }
        }
    }

    public InvalidationTracker(RoomDatabase database, String... tableNames) {
        this(database, new HashMap(), Collections.emptyMap(), tableNames);
    }

    private static void appendTriggerName(StringBuilder builder, String tableName, String triggerType) {
        builder.append("`").append("room_table_modification_trigger_").append(tableName).append("_").append(triggerType).append("`");
    }

    private String[] resolveViews(String[] names) {
        HashSet hashSet = new HashSet();
        for (String str : names) {
            String lowerCase = str.toLowerCase(Locale.US);
            if (this.mViewTables.containsKey(lowerCase)) {
                hashSet.addAll(this.mViewTables.get(lowerCase));
            } else {
                hashSet.add(str);
            }
        }
        return (String[]) hashSet.toArray(new String[hashSet.size()]);
    }

    private void startTrackingTable(SupportSQLiteDatabase writableDb, int tableId) {
        writableDb.execSQL("INSERT OR IGNORE INTO room_table_modification_log VALUES(" + tableId + ", 0)");
        String str = this.mTableNames[tableId];
        StringBuilder sb = new StringBuilder();
        for (String str2 : TRIGGERS) {
            sb.setLength(0);
            sb.append("CREATE TEMP TRIGGER IF NOT EXISTS ");
            appendTriggerName(sb, str, str2);
            sb.append(" AFTER ").append(str2).append(" ON `").append(str).append("` BEGIN UPDATE ").append(UPDATE_TABLE_NAME).append(" SET ").append(INVALIDATED_COLUMN_NAME).append(" = 1").append(" WHERE ").append(TABLE_ID_COLUMN_NAME).append(" = ").append(tableId).append(" AND ").append(INVALIDATED_COLUMN_NAME).append(" = 0").append("; END");
            writableDb.execSQL(sb.toString());
        }
    }

    private void stopTrackingTable(SupportSQLiteDatabase writableDb, int tableId) {
        String str = this.mTableNames[tableId];
        StringBuilder sb = new StringBuilder();
        for (String appendTriggerName : TRIGGERS) {
            sb.setLength(0);
            sb.append("DROP TRIGGER IF EXISTS ");
            appendTriggerName(sb, str, appendTriggerName);
            writableDb.execSQL(sb.toString());
        }
    }

    private String[] validateAndResolveTableNames(String[] tableNames) {
        String[] resolveViews = resolveViews(tableNames);
        int length = resolveViews.length;
        int i = 0;
        while (i < length) {
            String str = resolveViews[i];
            if (this.mTableIdLookup.containsKey(str.toLowerCase(Locale.US))) {
                i++;
            } else {
                throw new IllegalArgumentException("There is no table with name " + str);
            }
        }
        return resolveViews;
    }

    public void addObserver(Observer observer) {
        ObserverWrapper putIfAbsent;
        String[] resolveViews = resolveViews(observer.mTables);
        int[] iArr = new int[resolveViews.length];
        int length = resolveViews.length;
        int i = 0;
        while (i < length) {
            Integer num = this.mTableIdLookup.get(resolveViews[i].toLowerCase(Locale.US));
            if (num != null) {
                iArr[i] = num.intValue();
                i++;
            } else {
                throw new IllegalArgumentException("There is no table with name " + resolveViews[i]);
            }
        }
        ObserverWrapper observerWrapper = new ObserverWrapper(observer, iArr, resolveViews);
        synchronized (this.mObserverMap) {
            putIfAbsent = this.mObserverMap.putIfAbsent(observer, observerWrapper);
        }
        if (putIfAbsent == null && this.mObservedTableTracker.onAdded(iArr)) {
            syncTriggers();
        }
    }

    public void addWeakObserver(Observer observer) {
        addObserver(new WeakObserver(this, observer));
    }

    @Deprecated
    public <T> LiveData<T> createLiveData(String[] tableNames, Callable<T> callable) {
        return createLiveData(tableNames, false, callable);
    }

    public <T> LiveData<T> createLiveData(String[] tableNames, boolean inTransaction, Callable<T> callable) {
        return this.mInvalidationLiveDataContainer.create(validateAndResolveTableNames(tableNames), inTransaction, callable);
    }

    /* access modifiers changed from: package-private */
    public boolean ensureInitialization() {
        if (!this.mDatabase.isOpen()) {
            return false;
        }
        if (!this.mInitialized) {
            this.mDatabase.getOpenHelper().getWritableDatabase();
        }
        if (this.mInitialized) {
            return true;
        }
        Log.e("ROOM", "database is not initialized even though it is open");
        return false;
    }

    /* access modifiers changed from: package-private */
    public void internalInit(SupportSQLiteDatabase database) {
        synchronized (this) {
            if (this.mInitialized) {
                Log.e("ROOM", "Invalidation tracker is initialized twice :/.");
                return;
            }
            database.execSQL("PRAGMA temp_store = MEMORY;");
            database.execSQL("PRAGMA recursive_triggers='ON';");
            database.execSQL(CREATE_TRACKING_TABLE_SQL);
            syncTriggers(database);
            this.mCleanupStatement = database.compileStatement(RESET_UPDATED_TABLES_SQL);
            this.mInitialized = true;
        }
    }

    public void notifyObserversByTableNames(String... tables) {
        synchronized (this.mObserverMap) {
            Iterator<Map.Entry<Observer, ObserverWrapper>> it = this.mObserverMap.iterator();
            while (it.hasNext()) {
                Map.Entry next = it.next();
                if (!((Observer) next.getKey()).isRemote()) {
                    ((ObserverWrapper) next.getValue()).notifyByTableNames(tables);
                }
            }
        }
    }

    public void refreshVersionsAsync() {
        if (this.mPendingRefresh.compareAndSet(false, true)) {
            this.mDatabase.getQueryExecutor().execute(this.mRefreshRunnable);
        }
    }

    public void refreshVersionsSync() {
        syncTriggers();
        this.mRefreshRunnable.run();
    }

    public void removeObserver(Observer observer) {
        ObserverWrapper remove;
        synchronized (this.mObserverMap) {
            remove = this.mObserverMap.remove(observer);
        }
        if (remove != null && this.mObservedTableTracker.onRemoved(remove.mTableIds)) {
            syncTriggers();
        }
    }

    /* access modifiers changed from: package-private */
    public void startMultiInstanceInvalidation(Context context, String name) {
        this.mMultiInstanceInvalidationClient = new MultiInstanceInvalidationClient(context, name, this, this.mDatabase.getQueryExecutor());
    }

    /* access modifiers changed from: package-private */
    public void stopMultiInstanceInvalidation() {
        MultiInstanceInvalidationClient multiInstanceInvalidationClient = this.mMultiInstanceInvalidationClient;
        if (multiInstanceInvalidationClient != null) {
            multiInstanceInvalidationClient.stop();
            this.mMultiInstanceInvalidationClient = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void syncTriggers() {
        if (this.mDatabase.isOpen()) {
            syncTriggers(this.mDatabase.getOpenHelper().getWritableDatabase());
        }
    }

    /* access modifiers changed from: package-private */
    public void syncTriggers(SupportSQLiteDatabase database) {
        if (!database.inTransaction()) {
            while (true) {
                try {
                    Lock closeLock = this.mDatabase.getCloseLock();
                    closeLock.lock();
                    try {
                        int[] tablesToSync = this.mObservedTableTracker.getTablesToSync();
                        if (tablesToSync == null) {
                            closeLock.unlock();
                            return;
                        }
                        int length = tablesToSync.length;
                        database.beginTransaction();
                        for (int i = 0; i < length; i++) {
                            switch (tablesToSync[i]) {
                                case 1:
                                    startTrackingTable(database, i);
                                    break;
                                case 2:
                                    stopTrackingTable(database, i);
                                    break;
                            }
                        }
                        database.setTransactionSuccessful();
                        database.endTransaction();
                        this.mObservedTableTracker.onSyncCompleted();
                        closeLock.unlock();
                    } catch (Throwable th) {
                        closeLock.unlock();
                        throw th;
                    }
                } catch (SQLiteException | IllegalStateException e) {
                    Log.e("ROOM", "Cannot run invalidation tracker. Is the db closed?", e);
                    return;
                }
            }
        }
    }
}

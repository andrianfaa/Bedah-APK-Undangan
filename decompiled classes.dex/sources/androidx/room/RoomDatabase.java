package androidx.room;

import android.app.ActivityManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Looper;
import android.util.Log;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.room.migration.Migration;
import androidx.room.util.SneakyThrow;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class RoomDatabase {
    private static final String DB_IMPL_SUFFIX = "_Impl";
    public static final int MAX_BIND_PARAMETER_CNT = 999;
    private boolean mAllowMainThreadQueries;
    private final Map<String, Object> mBackingFieldMap = new ConcurrentHashMap();
    @Deprecated
    protected List<Callback> mCallbacks;
    private final ReentrantReadWriteLock mCloseLock = new ReentrantReadWriteLock();
    @Deprecated
    protected volatile SupportSQLiteDatabase mDatabase;
    private final InvalidationTracker mInvalidationTracker = createInvalidationTracker();
    private SupportSQLiteOpenHelper mOpenHelper;
    private Executor mQueryExecutor;
    private final ThreadLocal<Integer> mSuspendingTransactionId = new ThreadLocal<>();
    private Executor mTransactionExecutor;
    boolean mWriteAheadLoggingEnabled;

    public static class Builder<T extends RoomDatabase> {
        private boolean mAllowDestructiveMigrationOnDowngrade;
        private boolean mAllowMainThreadQueries;
        private ArrayList<Callback> mCallbacks;
        private final Context mContext;
        private String mCopyFromAssetPath;
        private File mCopyFromFile;
        private final Class<T> mDatabaseClass;
        private SupportSQLiteOpenHelper.Factory mFactory;
        private JournalMode mJournalMode = JournalMode.AUTOMATIC;
        private final MigrationContainer mMigrationContainer = new MigrationContainer();
        private Set<Integer> mMigrationStartAndEndVersions;
        private Set<Integer> mMigrationsNotRequiredFrom;
        private boolean mMultiInstanceInvalidation;
        private final String mName;
        private Executor mQueryExecutor;
        private boolean mRequireMigration = true;
        private Executor mTransactionExecutor;

        Builder(Context context, Class<T> cls, String name) {
            this.mContext = context;
            this.mDatabaseClass = cls;
            this.mName = name;
        }

        public Builder<T> addCallback(Callback callback) {
            if (this.mCallbacks == null) {
                this.mCallbacks = new ArrayList<>();
            }
            this.mCallbacks.add(callback);
            return this;
        }

        public Builder<T> addMigrations(Migration... migrations) {
            if (this.mMigrationStartAndEndVersions == null) {
                this.mMigrationStartAndEndVersions = new HashSet();
            }
            for (Migration migration : migrations) {
                this.mMigrationStartAndEndVersions.add(Integer.valueOf(migration.startVersion));
                this.mMigrationStartAndEndVersions.add(Integer.valueOf(migration.endVersion));
            }
            this.mMigrationContainer.addMigrations(migrations);
            return this;
        }

        public Builder<T> allowMainThreadQueries() {
            this.mAllowMainThreadQueries = true;
            return this;
        }

        public T build() {
            Executor executor;
            if (this.mContext == null) {
                throw new IllegalArgumentException("Cannot provide null context for the database.");
            } else if (this.mDatabaseClass != null) {
                Executor executor2 = this.mQueryExecutor;
                if (executor2 == null && this.mTransactionExecutor == null) {
                    Executor iOThreadExecutor = ArchTaskExecutor.getIOThreadExecutor();
                    this.mTransactionExecutor = iOThreadExecutor;
                    this.mQueryExecutor = iOThreadExecutor;
                } else if (executor2 != null && this.mTransactionExecutor == null) {
                    this.mTransactionExecutor = executor2;
                } else if (executor2 == null && (executor = this.mTransactionExecutor) != null) {
                    this.mQueryExecutor = executor;
                }
                Set<Integer> set = this.mMigrationStartAndEndVersions;
                if (!(set == null || this.mMigrationsNotRequiredFrom == null)) {
                    for (Integer next : set) {
                        if (this.mMigrationsNotRequiredFrom.contains(next)) {
                            throw new IllegalArgumentException("Inconsistency detected. A Migration was supplied to addMigration(Migration... migrations) that has a start or end version equal to a start version supplied to fallbackToDestructiveMigrationFrom(int... startVersions). Start version: " + next);
                        }
                    }
                }
                if (this.mFactory == null) {
                    this.mFactory = new FrameworkSQLiteOpenHelperFactory();
                }
                String str = this.mCopyFromAssetPath;
                if (!(str == null && this.mCopyFromFile == null)) {
                    if (this.mName == null) {
                        throw new IllegalArgumentException("Cannot create from asset or file for an in-memory database.");
                    } else if (str == null || this.mCopyFromFile == null) {
                        this.mFactory = new SQLiteCopyOpenHelperFactory(this.mCopyFromAssetPath, this.mCopyFromFile, this.mFactory);
                    } else {
                        throw new IllegalArgumentException("Both createFromAsset() and createFromFile() was called on this Builder but the database can only be created using one of the two configurations.");
                    }
                }
                Context context = this.mContext;
                String str2 = this.mName;
                SupportSQLiteOpenHelper.Factory factory = this.mFactory;
                MigrationContainer migrationContainer = this.mMigrationContainer;
                ArrayList<Callback> arrayList = this.mCallbacks;
                boolean z = this.mAllowMainThreadQueries;
                JournalMode resolve = this.mJournalMode.resolve(context);
                Executor executor3 = this.mQueryExecutor;
                Executor executor4 = this.mTransactionExecutor;
                boolean z2 = this.mMultiInstanceInvalidation;
                boolean z3 = this.mRequireMigration;
                boolean z4 = this.mAllowDestructiveMigrationOnDowngrade;
                boolean z5 = z3;
                boolean z6 = z4;
                DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(context, str2, factory, migrationContainer, arrayList, z, resolve, executor3, executor4, z2, z5, z6, this.mMigrationsNotRequiredFrom, this.mCopyFromAssetPath, this.mCopyFromFile);
                T t = (RoomDatabase) Room.getGeneratedImplementation(this.mDatabaseClass, RoomDatabase.DB_IMPL_SUFFIX);
                t.init(databaseConfiguration);
                return t;
            } else {
                throw new IllegalArgumentException("Must provide an abstract class that extends RoomDatabase");
            }
        }

        public Builder<T> createFromAsset(String databaseFilePath) {
            this.mCopyFromAssetPath = databaseFilePath;
            return this;
        }

        public Builder<T> createFromFile(File databaseFile) {
            this.mCopyFromFile = databaseFile;
            return this;
        }

        public Builder<T> enableMultiInstanceInvalidation() {
            this.mMultiInstanceInvalidation = this.mName != null;
            return this;
        }

        public Builder<T> fallbackToDestructiveMigration() {
            this.mRequireMigration = false;
            this.mAllowDestructiveMigrationOnDowngrade = true;
            return this;
        }

        public Builder<T> fallbackToDestructiveMigrationFrom(int... startVersions) {
            if (this.mMigrationsNotRequiredFrom == null) {
                this.mMigrationsNotRequiredFrom = new HashSet(startVersions.length);
            }
            for (int valueOf : startVersions) {
                this.mMigrationsNotRequiredFrom.add(Integer.valueOf(valueOf));
            }
            return this;
        }

        public Builder<T> fallbackToDestructiveMigrationOnDowngrade() {
            this.mRequireMigration = true;
            this.mAllowDestructiveMigrationOnDowngrade = true;
            return this;
        }

        public Builder<T> openHelperFactory(SupportSQLiteOpenHelper.Factory factory) {
            this.mFactory = factory;
            return this;
        }

        public Builder<T> setJournalMode(JournalMode journalMode) {
            this.mJournalMode = journalMode;
            return this;
        }

        public Builder<T> setQueryExecutor(Executor executor) {
            this.mQueryExecutor = executor;
            return this;
        }

        public Builder<T> setTransactionExecutor(Executor executor) {
            this.mTransactionExecutor = executor;
            return this;
        }
    }

    public static abstract class Callback {
        public void onCreate(SupportSQLiteDatabase db) {
        }

        public void onDestructiveMigration(SupportSQLiteDatabase db) {
        }

        public void onOpen(SupportSQLiteDatabase db) {
        }
    }

    public enum JournalMode {
        AUTOMATIC,
        TRUNCATE,
        WRITE_AHEAD_LOGGING;

        private static boolean isLowRamDevice(ActivityManager activityManager) {
            if (Build.VERSION.SDK_INT >= 19) {
                return activityManager.isLowRamDevice();
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:5:0x000b, code lost:
            r0 = (android.app.ActivityManager) r3.getSystemService("activity");
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public androidx.room.RoomDatabase.JournalMode resolve(android.content.Context r3) {
            /*
                r2 = this;
                androidx.room.RoomDatabase$JournalMode r0 = AUTOMATIC
                if (r2 == r0) goto L_0x0005
                return r2
            L_0x0005:
                int r0 = android.os.Build.VERSION.SDK_INT
                r1 = 16
                if (r0 < r1) goto L_0x001f
                java.lang.String r0 = "activity"
                java.lang.Object r0 = r3.getSystemService(r0)
                android.app.ActivityManager r0 = (android.app.ActivityManager) r0
                if (r0 == 0) goto L_0x001f
                boolean r1 = isLowRamDevice(r0)
                if (r1 != 0) goto L_0x001f
                androidx.room.RoomDatabase$JournalMode r1 = WRITE_AHEAD_LOGGING
                return r1
            L_0x001f:
                androidx.room.RoomDatabase$JournalMode r0 = TRUNCATE
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.room.RoomDatabase.JournalMode.resolve(android.content.Context):androidx.room.RoomDatabase$JournalMode");
        }
    }

    public static class MigrationContainer {
        private HashMap<Integer, TreeMap<Integer, Migration>> mMigrations = new HashMap<>();

        private void addMigration(Migration migration) {
            int i = migration.startVersion;
            int i2 = migration.endVersion;
            TreeMap treeMap = this.mMigrations.get(Integer.valueOf(i));
            if (treeMap == null) {
                treeMap = new TreeMap();
                this.mMigrations.put(Integer.valueOf(i), treeMap);
            }
            Migration migration2 = (Migration) treeMap.get(Integer.valueOf(i2));
            if (migration2 != null) {
                Log.w("ROOM", "Overriding migration " + migration2 + " with " + migration);
            }
            treeMap.put(Integer.valueOf(i2), migration);
        }

        private List<Migration> findUpMigrationPath(List<Migration> list, boolean upgrade, int start, int end) {
            boolean z;
            do {
                if (upgrade) {
                    if (start >= end) {
                        return list;
                    }
                } else if (start <= end) {
                    return list;
                }
                TreeMap treeMap = this.mMigrations.get(Integer.valueOf(start));
                if (treeMap != null) {
                    z = false;
                    Iterator it = (upgrade ? treeMap.descendingKeySet() : treeMap.keySet()).iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        int intValue = ((Integer) it.next()).intValue();
                        boolean z2 = true;
                        if (upgrade) {
                            if (intValue > end || intValue <= start) {
                                z2 = false;
                                continue;
                            }
                        } else if (intValue < end || intValue >= start) {
                            z2 = false;
                            continue;
                        }
                        if (z2) {
                            list.add(treeMap.get(Integer.valueOf(intValue)));
                            start = intValue;
                            z = true;
                            continue;
                            break;
                        }
                    }
                } else {
                    return null;
                }
            } while (z);
            return null;
        }

        public void addMigrations(Migration... migrations) {
            for (Migration addMigration : migrations) {
                addMigration(addMigration);
            }
        }

        public List<Migration> findMigrationPath(int start, int end) {
            if (start == end) {
                return Collections.emptyList();
            }
            return findUpMigrationPath(new ArrayList(), end > start, start, end);
        }
    }

    private static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public void assertNotMainThread() {
        if (!this.mAllowMainThreadQueries && isMainThread()) {
            throw new IllegalStateException("Cannot access database on the main thread since it may potentially lock the UI for a long period of time.");
        }
    }

    public void assertNotSuspendingTransaction() {
        if (!inTransaction() && this.mSuspendingTransactionId.get() != null) {
            throw new IllegalStateException("Cannot access database on a different coroutine context inherited from a suspending transaction.");
        }
    }

    @Deprecated
    public void beginTransaction() {
        assertNotMainThread();
        SupportSQLiteDatabase writableDatabase = this.mOpenHelper.getWritableDatabase();
        this.mInvalidationTracker.syncTriggers(writableDatabase);
        writableDatabase.beginTransaction();
    }

    public abstract void clearAllTables();

    public void close() {
        if (isOpen()) {
            ReentrantReadWriteLock.WriteLock writeLock = this.mCloseLock.writeLock();
            try {
                writeLock.lock();
                this.mInvalidationTracker.stopMultiInstanceInvalidation();
                this.mOpenHelper.close();
            } finally {
                writeLock.unlock();
            }
        }
    }

    public SupportSQLiteStatement compileStatement(String sql) {
        assertNotMainThread();
        assertNotSuspendingTransaction();
        return this.mOpenHelper.getWritableDatabase().compileStatement(sql);
    }

    /* access modifiers changed from: protected */
    public abstract InvalidationTracker createInvalidationTracker();

    /* access modifiers changed from: protected */
    public abstract SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration databaseConfiguration);

    @Deprecated
    public void endTransaction() {
        this.mOpenHelper.getWritableDatabase().endTransaction();
        if (!inTransaction()) {
            this.mInvalidationTracker.refreshVersionsAsync();
        }
    }

    /* access modifiers changed from: package-private */
    public Map<String, Object> getBackingFieldMap() {
        return this.mBackingFieldMap;
    }

    /* access modifiers changed from: package-private */
    public Lock getCloseLock() {
        return this.mCloseLock.readLock();
    }

    public InvalidationTracker getInvalidationTracker() {
        return this.mInvalidationTracker;
    }

    public SupportSQLiteOpenHelper getOpenHelper() {
        return this.mOpenHelper;
    }

    public Executor getQueryExecutor() {
        return this.mQueryExecutor;
    }

    /* access modifiers changed from: package-private */
    public ThreadLocal<Integer> getSuspendingTransactionId() {
        return this.mSuspendingTransactionId;
    }

    public Executor getTransactionExecutor() {
        return this.mTransactionExecutor;
    }

    public boolean inTransaction() {
        return this.mOpenHelper.getWritableDatabase().inTransaction();
    }

    public void init(DatabaseConfiguration configuration) {
        SupportSQLiteOpenHelper createOpenHelper = createOpenHelper(configuration);
        this.mOpenHelper = createOpenHelper;
        if (createOpenHelper instanceof SQLiteCopyOpenHelper) {
            ((SQLiteCopyOpenHelper) createOpenHelper).setDatabaseConfiguration(configuration);
        }
        boolean z = false;
        if (Build.VERSION.SDK_INT >= 16) {
            z = configuration.journalMode == JournalMode.WRITE_AHEAD_LOGGING;
            this.mOpenHelper.setWriteAheadLoggingEnabled(z);
        }
        this.mCallbacks = configuration.callbacks;
        this.mQueryExecutor = configuration.queryExecutor;
        this.mTransactionExecutor = new TransactionExecutor(configuration.transactionExecutor);
        this.mAllowMainThreadQueries = configuration.allowMainThreadQueries;
        this.mWriteAheadLoggingEnabled = z;
        if (configuration.multiInstanceInvalidation) {
            this.mInvalidationTracker.startMultiInstanceInvalidation(configuration.context, configuration.name);
        }
    }

    /* access modifiers changed from: protected */
    public void internalInitInvalidationTracker(SupportSQLiteDatabase db) {
        this.mInvalidationTracker.internalInit(db);
    }

    public boolean isOpen() {
        SupportSQLiteDatabase supportSQLiteDatabase = this.mDatabase;
        return supportSQLiteDatabase != null && supportSQLiteDatabase.isOpen();
    }

    public Cursor query(SupportSQLiteQuery query) {
        return query(query, (CancellationSignal) null);
    }

    public Cursor query(SupportSQLiteQuery query, CancellationSignal signal) {
        assertNotMainThread();
        assertNotSuspendingTransaction();
        return (signal == null || Build.VERSION.SDK_INT < 16) ? this.mOpenHelper.getWritableDatabase().query(query) : this.mOpenHelper.getWritableDatabase().query(query, signal);
    }

    public Cursor query(String query, Object[] args) {
        return this.mOpenHelper.getWritableDatabase().query((SupportSQLiteQuery) new SimpleSQLiteQuery(query, args));
    }

    public <V> V runInTransaction(Callable<V> callable) {
        beginTransaction();
        try {
            V call = callable.call();
            setTransactionSuccessful();
            endTransaction();
            return call;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            SneakyThrow.reThrow(e2);
            endTransaction();
            return null;
        } catch (Throwable th) {
            endTransaction();
            throw th;
        }
    }

    public void runInTransaction(Runnable body) {
        beginTransaction();
        try {
            body.run();
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    @Deprecated
    public void setTransactionSuccessful() {
        this.mOpenHelper.getWritableDatabase().setTransactionSuccessful();
    }
}

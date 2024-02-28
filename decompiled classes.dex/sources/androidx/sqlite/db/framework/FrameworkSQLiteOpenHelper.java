package androidx.sqlite.db.framework;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.io.File;

class FrameworkSQLiteOpenHelper implements SupportSQLiteOpenHelper {
    private final SupportSQLiteOpenHelper.Callback mCallback;
    private final Context mContext;
    private OpenHelper mDelegate;
    private final Object mLock;
    private final String mName;
    private final boolean mUseNoBackupDirectory;
    private boolean mWriteAheadLoggingEnabled;

    static class OpenHelper extends SQLiteOpenHelper {
        final SupportSQLiteOpenHelper.Callback mCallback;
        final FrameworkSQLiteDatabase[] mDbRef;
        private boolean mMigrated;

        OpenHelper(Context context, String name, final FrameworkSQLiteDatabase[] dbRef, final SupportSQLiteOpenHelper.Callback callback) {
            super(context, name, (SQLiteDatabase.CursorFactory) null, callback.version, new DatabaseErrorHandler() {
                public void onCorruption(SQLiteDatabase dbObj) {
                    SupportSQLiteOpenHelper.Callback.this.onCorruption(OpenHelper.getWrappedDb(dbRef, dbObj));
                }
            });
            this.mCallback = callback;
            this.mDbRef = dbRef;
        }

        static FrameworkSQLiteDatabase getWrappedDb(FrameworkSQLiteDatabase[] refHolder, SQLiteDatabase sqLiteDatabase) {
            FrameworkSQLiteDatabase frameworkSQLiteDatabase = refHolder[0];
            if (frameworkSQLiteDatabase == null || !frameworkSQLiteDatabase.isDelegate(sqLiteDatabase)) {
                refHolder[0] = new FrameworkSQLiteDatabase(sqLiteDatabase);
            }
            return refHolder[0];
        }

        public synchronized void close() {
            super.close();
            this.mDbRef[0] = null;
        }

        /* access modifiers changed from: package-private */
        public synchronized SupportSQLiteDatabase getReadableSupportDatabase() {
            this.mMigrated = false;
            SQLiteDatabase readableDatabase = super.getReadableDatabase();
            if (this.mMigrated) {
                close();
                return getReadableSupportDatabase();
            }
            return getWrappedDb(readableDatabase);
        }

        /* access modifiers changed from: package-private */
        public FrameworkSQLiteDatabase getWrappedDb(SQLiteDatabase sqLiteDatabase) {
            return getWrappedDb(this.mDbRef, sqLiteDatabase);
        }

        /* access modifiers changed from: package-private */
        public synchronized SupportSQLiteDatabase getWritableSupportDatabase() {
            this.mMigrated = false;
            SQLiteDatabase writableDatabase = super.getWritableDatabase();
            if (this.mMigrated) {
                close();
                return getWritableSupportDatabase();
            }
            return getWrappedDb(writableDatabase);
        }

        public void onConfigure(SQLiteDatabase db) {
            this.mCallback.onConfigure(getWrappedDb(db));
        }

        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            this.mCallback.onCreate(getWrappedDb(sqLiteDatabase));
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            this.mMigrated = true;
            this.mCallback.onDowngrade(getWrappedDb(db), oldVersion, newVersion);
        }

        public void onOpen(SQLiteDatabase db) {
            if (!this.mMigrated) {
                this.mCallback.onOpen(getWrappedDb(db));
            }
        }

        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            this.mMigrated = true;
            this.mCallback.onUpgrade(getWrappedDb(sqLiteDatabase), oldVersion, newVersion);
        }
    }

    FrameworkSQLiteOpenHelper(Context context, String name, SupportSQLiteOpenHelper.Callback callback) {
        this(context, name, callback, false);
    }

    FrameworkSQLiteOpenHelper(Context context, String name, SupportSQLiteOpenHelper.Callback callback, boolean useNoBackupDirectory) {
        this.mContext = context;
        this.mName = name;
        this.mCallback = callback;
        this.mUseNoBackupDirectory = useNoBackupDirectory;
        this.mLock = new Object();
    }

    private OpenHelper getDelegate() {
        OpenHelper openHelper;
        synchronized (this.mLock) {
            if (this.mDelegate == null) {
                FrameworkSQLiteDatabase[] frameworkSQLiteDatabaseArr = new FrameworkSQLiteDatabase[1];
                if (Build.VERSION.SDK_INT < 23 || this.mName == null || !this.mUseNoBackupDirectory) {
                    this.mDelegate = new OpenHelper(this.mContext, this.mName, frameworkSQLiteDatabaseArr, this.mCallback);
                } else {
                    this.mDelegate = new OpenHelper(this.mContext, new File(this.mContext.getNoBackupFilesDir(), this.mName).getAbsolutePath(), frameworkSQLiteDatabaseArr, this.mCallback);
                }
                if (Build.VERSION.SDK_INT >= 16) {
                    this.mDelegate.setWriteAheadLoggingEnabled(this.mWriteAheadLoggingEnabled);
                }
            }
            openHelper = this.mDelegate;
        }
        return openHelper;
    }

    public void close() {
        getDelegate().close();
    }

    public String getDatabaseName() {
        return this.mName;
    }

    public SupportSQLiteDatabase getReadableDatabase() {
        return getDelegate().getReadableSupportDatabase();
    }

    public SupportSQLiteDatabase getWritableDatabase() {
        return getDelegate().getWritableSupportDatabase();
    }

    public void setWriteAheadLoggingEnabled(boolean enabled) {
        synchronized (this.mLock) {
            OpenHelper openHelper = this.mDelegate;
            if (openHelper != null) {
                openHelper.setWriteAheadLoggingEnabled(enabled);
            }
            this.mWriteAheadLoggingEnabled = enabled;
        }
    }
}

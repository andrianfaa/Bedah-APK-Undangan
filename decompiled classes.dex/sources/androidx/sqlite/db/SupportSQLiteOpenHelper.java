package androidx.sqlite.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import java.io.Closeable;
import java.io.File;

public interface SupportSQLiteOpenHelper extends Closeable {

    public static abstract class Callback {
        private static final String TAG = "SupportSQLite";
        public final int version;

        public Callback(int version2) {
            this.version = version2;
        }

        private void deleteDatabaseFile(String fileName) {
            if (!fileName.equalsIgnoreCase(":memory:") && fileName.trim().length() != 0) {
                Log.w(TAG, "deleting the database file: " + fileName);
                try {
                    if (Build.VERSION.SDK_INT >= 16) {
                        SQLiteDatabase.deleteDatabase(new File(fileName));
                        return;
                    }
                    try {
                        if (!new File(fileName).delete()) {
                            Log.e(TAG, "Could not delete the database file " + fileName);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "error while deleting corrupted database file", e);
                    }
                } catch (Exception e2) {
                    Log.w(TAG, "delete failed: ", e2);
                }
            }
        }

        public void onConfigure(SupportSQLiteDatabase db) {
        }

        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0038, code lost:
            if (r0 != null) goto L_0x003a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x003a, code lost:
            r2 = r0.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0042, code lost:
            if (r2.hasNext() != false) goto L_0x0044;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0044, code lost:
            deleteDatabaseFile((java.lang.String) r2.next().second);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0053, code lost:
            deleteDatabaseFile(r6.getPath());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x005a, code lost:
            throw r1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0031, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Removed duplicated region for block: B:8:0x0031 A[ExcHandler: all (r1v7 'th' java.lang.Throwable A[CUSTOM_DECLARE]), PHI: r0 
          PHI: (r0v7 java.util.List<android.util.Pair<java.lang.String, java.lang.String>>) = (r0v5 java.util.List<android.util.Pair<java.lang.String, java.lang.String>>), (r0v6 java.util.List<android.util.Pair<java.lang.String, java.lang.String>>), (r0v6 java.util.List<android.util.Pair<java.lang.String, java.lang.String>>) binds: [B:5:0x002b, B:10:0x0034, B:11:?] A[DONT_GENERATE, DONT_INLINE], Splitter:B:5:0x002b] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onCorruption(androidx.sqlite.db.SupportSQLiteDatabase r6) {
            /*
                r5 = this;
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                java.lang.String r1 = "Corruption reported by sqlite on database: "
                java.lang.StringBuilder r0 = r0.append(r1)
                java.lang.String r1 = r6.getPath()
                java.lang.StringBuilder r0 = r0.append(r1)
                java.lang.String r0 = r0.toString()
                java.lang.String r1 = "SupportSQLite"
                android.util.Log.e(r1, r0)
                boolean r0 = r6.isOpen()
                if (r0 != 0) goto L_0x002a
                java.lang.String r0 = r6.getPath()
                r5.deleteDatabaseFile(r0)
                return
            L_0x002a:
                r0 = 0
                java.util.List r1 = r6.getAttachedDbs()     // Catch:{ SQLiteException -> 0x0033, all -> 0x0031 }
                r0 = r1
                goto L_0x0034
            L_0x0031:
                r1 = move-exception
                goto L_0x0038
            L_0x0033:
                r1 = move-exception
            L_0x0034:
                r6.close()     // Catch:{ IOException -> 0x005b, all -> 0x0031 }
                goto L_0x005c
            L_0x0038:
                if (r0 == 0) goto L_0x0053
                java.util.Iterator r2 = r0.iterator()
            L_0x003e:
                boolean r3 = r2.hasNext()
                if (r3 == 0) goto L_0x0052
                java.lang.Object r3 = r2.next()
                android.util.Pair r3 = (android.util.Pair) r3
                java.lang.Object r4 = r3.second
                java.lang.String r4 = (java.lang.String) r4
                r5.deleteDatabaseFile(r4)
                goto L_0x003e
            L_0x0052:
                goto L_0x005a
            L_0x0053:
                java.lang.String r2 = r6.getPath()
                r5.deleteDatabaseFile(r2)
            L_0x005a:
                throw r1
            L_0x005b:
                r1 = move-exception
            L_0x005c:
                if (r0 == 0) goto L_0x0077
                java.util.Iterator r1 = r0.iterator()
            L_0x0062:
                boolean r2 = r1.hasNext()
                if (r2 == 0) goto L_0x0076
                java.lang.Object r2 = r1.next()
                android.util.Pair r2 = (android.util.Pair) r2
                java.lang.Object r3 = r2.second
                java.lang.String r3 = (java.lang.String) r3
                r5.deleteDatabaseFile(r3)
                goto L_0x0062
            L_0x0076:
                goto L_0x007f
            L_0x0077:
                java.lang.String r1 = r6.getPath()
                r5.deleteDatabaseFile(r1)
            L_0x007f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.sqlite.db.SupportSQLiteOpenHelper.Callback.onCorruption(androidx.sqlite.db.SupportSQLiteDatabase):void");
        }

        public abstract void onCreate(SupportSQLiteDatabase supportSQLiteDatabase);

        public void onDowngrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
            throw new SQLiteException("Can't downgrade database from version " + oldVersion + " to " + newVersion);
        }

        public void onOpen(SupportSQLiteDatabase db) {
        }

        public abstract void onUpgrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2);
    }

    public static class Configuration {
        public final Callback callback;
        public final Context context;
        public final String name;
        public final boolean useNoBackupDirectory;

        public static class Builder {
            Callback mCallback;
            Context mContext;
            String mName;
            boolean mUseNoBackUpDirectory;

            Builder(Context context) {
                this.mContext = context;
            }

            public Configuration build() {
                if (this.mCallback == null) {
                    throw new IllegalArgumentException("Must set a callback to create the configuration.");
                } else if (this.mContext == null) {
                    throw new IllegalArgumentException("Must set a non-null context to create the configuration.");
                } else if (!this.mUseNoBackUpDirectory || !TextUtils.isEmpty(this.mName)) {
                    return new Configuration(this.mContext, this.mName, this.mCallback, this.mUseNoBackUpDirectory);
                } else {
                    throw new IllegalArgumentException("Must set a non-null database name to a configuration that uses the no backup directory.");
                }
            }

            public Builder callback(Callback callback) {
                this.mCallback = callback;
                return this;
            }

            public Builder name(String name) {
                this.mName = name;
                return this;
            }

            public Builder noBackupDirectory(boolean useNoBackUpDirectory) {
                this.mUseNoBackUpDirectory = useNoBackUpDirectory;
                return this;
            }
        }

        Configuration(Context context2, String name2, Callback callback2) {
            this(context2, name2, callback2, false);
        }

        Configuration(Context context2, String name2, Callback callback2, boolean useNoBackupDirectory2) {
            this.context = context2;
            this.name = name2;
            this.callback = callback2;
            this.useNoBackupDirectory = useNoBackupDirectory2;
        }

        public static Builder builder(Context context2) {
            return new Builder(context2);
        }
    }

    public interface Factory {
        SupportSQLiteOpenHelper create(Configuration configuration);
    }

    void close();

    String getDatabaseName();

    SupportSQLiteDatabase getReadableDatabase();

    SupportSQLiteDatabase getWritableDatabase();

    void setWriteAheadLoggingEnabled(boolean z);
}

package androidx.room;

import android.database.Cursor;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteQuery;
import java.util.List;
import mt.Log1F380D;
import okhttp3.HttpUrl;

/* compiled from: 0090 */
public class RoomOpenHelper extends SupportSQLiteOpenHelper.Callback {
    private DatabaseConfiguration mConfiguration;
    private final Delegate mDelegate;
    private final String mIdentityHash;
    private final String mLegacyHash;

    public static abstract class Delegate {
        public final int version;

        public Delegate(int version2) {
            this.version = version2;
        }

        /* access modifiers changed from: protected */
        public abstract void createAllTables(SupportSQLiteDatabase supportSQLiteDatabase);

        /* access modifiers changed from: protected */
        public abstract void dropAllTables(SupportSQLiteDatabase supportSQLiteDatabase);

        /* access modifiers changed from: protected */
        public abstract void onCreate(SupportSQLiteDatabase supportSQLiteDatabase);

        /* access modifiers changed from: protected */
        public abstract void onOpen(SupportSQLiteDatabase supportSQLiteDatabase);

        /* access modifiers changed from: protected */
        public void onPostMigrate(SupportSQLiteDatabase database) {
        }

        /* access modifiers changed from: protected */
        public void onPreMigrate(SupportSQLiteDatabase database) {
        }

        /* access modifiers changed from: protected */
        public ValidationResult onValidateSchema(SupportSQLiteDatabase db) {
            validateMigration(db);
            return new ValidationResult(true, (String) null);
        }

        /* access modifiers changed from: protected */
        @Deprecated
        public void validateMigration(SupportSQLiteDatabase db) {
            throw new UnsupportedOperationException("validateMigration is deprecated");
        }
    }

    public static class ValidationResult {
        public final String expectedFoundMsg;
        public final boolean isValid;

        public ValidationResult(boolean isValid2, String expectedFoundMsg2) {
            this.isValid = isValid2;
            this.expectedFoundMsg = expectedFoundMsg2;
        }
    }

    public RoomOpenHelper(DatabaseConfiguration configuration, Delegate delegate, String legacyHash) {
        this(configuration, delegate, HttpUrl.FRAGMENT_ENCODE_SET, legacyHash);
    }

    public RoomOpenHelper(DatabaseConfiguration configuration, Delegate delegate, String identityHash, String legacyHash) {
        super(delegate.version);
        this.mConfiguration = configuration;
        this.mDelegate = delegate;
        this.mIdentityHash = identityHash;
        this.mLegacyHash = legacyHash;
    }

    /* JADX INFO: finally extract failed */
    private void checkIdentity(SupportSQLiteDatabase db) {
        if (hasRoomMasterTable(db)) {
            String str = null;
            Cursor query = db.query((SupportSQLiteQuery) new SimpleSQLiteQuery(RoomMasterTable.READ_QUERY));
            try {
                if (query.moveToFirst()) {
                    str = query.getString(0);
                }
                query.close();
                if (!this.mIdentityHash.equals(str) && !this.mLegacyHash.equals(str)) {
                    throw new IllegalStateException("Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number.");
                }
            } catch (Throwable th) {
                query.close();
                throw th;
            }
        } else {
            ValidationResult onValidateSchema = this.mDelegate.onValidateSchema(db);
            if (onValidateSchema.isValid) {
                this.mDelegate.onPostMigrate(db);
                updateIdentity(db);
                return;
            }
            throw new IllegalStateException("Pre-packaged database has an invalid schema: " + onValidateSchema.expectedFoundMsg);
        }
    }

    private void createMasterTableIfNotExists(SupportSQLiteDatabase db) {
        db.execSQL(RoomMasterTable.CREATE_QUERY);
    }

    private static boolean hasEmptySchema(SupportSQLiteDatabase db) {
        Cursor query = db.query("SELECT count(*) FROM sqlite_master WHERE name != 'android_metadata'");
        try {
            boolean z = false;
            if (query.moveToFirst() && query.getInt(0) == 0) {
                z = true;
            }
            return z;
        } finally {
            query.close();
        }
    }

    private static boolean hasRoomMasterTable(SupportSQLiteDatabase db) {
        Cursor query = db.query("SELECT 1 FROM sqlite_master WHERE type = 'table' AND name='room_master_table'");
        try {
            boolean z = false;
            if (query.moveToFirst() && query.getInt(0) != 0) {
                z = true;
            }
            return z;
        } finally {
            query.close();
        }
    }

    private void updateIdentity(SupportSQLiteDatabase db) {
        createMasterTableIfNotExists(db);
        String createInsertQuery = RoomMasterTable.createInsertQuery(this.mIdentityHash);
        Log1F380D.a((Object) createInsertQuery);
        db.execSQL(createInsertQuery);
    }

    public void onConfigure(SupportSQLiteDatabase db) {
        super.onConfigure(db);
    }

    public void onCreate(SupportSQLiteDatabase db) {
        boolean hasEmptySchema = hasEmptySchema(db);
        this.mDelegate.createAllTables(db);
        if (!hasEmptySchema) {
            ValidationResult onValidateSchema = this.mDelegate.onValidateSchema(db);
            if (!onValidateSchema.isValid) {
                throw new IllegalStateException("Pre-packaged database has an invalid schema: " + onValidateSchema.expectedFoundMsg);
            }
        }
        updateIdentity(db);
        this.mDelegate.onCreate(db);
    }

    public void onDowngrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void onOpen(SupportSQLiteDatabase db) {
        super.onOpen(db);
        checkIdentity(db);
        this.mDelegate.onOpen(db);
        this.mConfiguration = null;
    }

    public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
        List<Migration> findMigrationPath;
        boolean z = false;
        DatabaseConfiguration databaseConfiguration = this.mConfiguration;
        if (!(databaseConfiguration == null || (findMigrationPath = databaseConfiguration.migrationContainer.findMigrationPath(oldVersion, newVersion)) == null)) {
            this.mDelegate.onPreMigrate(db);
            for (Migration migrate : findMigrationPath) {
                migrate.migrate(db);
            }
            ValidationResult onValidateSchema = this.mDelegate.onValidateSchema(db);
            if (onValidateSchema.isValid) {
                this.mDelegate.onPostMigrate(db);
                updateIdentity(db);
                z = true;
            } else {
                throw new IllegalStateException("Migration didn't properly handle: " + onValidateSchema.expectedFoundMsg);
            }
        }
        if (!z) {
            DatabaseConfiguration databaseConfiguration2 = this.mConfiguration;
            if (databaseConfiguration2 == null || databaseConfiguration2.isMigrationRequired(oldVersion, newVersion)) {
                throw new IllegalStateException("A migration from " + oldVersion + " to " + newVersion + " was required but not found. Please provide the necessary Migration path via RoomDatabase.Builder.addMigration(Migration ...) or allow for destructive migrations via one of the RoomDatabase.Builder.fallbackToDestructiveMigration* methods.");
            }
            this.mDelegate.dropAllTables(db);
            this.mDelegate.createAllTables(db);
        }
    }
}

package androidx.room;

import android.content.Context;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

public class DatabaseConfiguration {
    public final boolean allowDestructiveMigrationOnDowngrade;
    public final boolean allowMainThreadQueries;
    public final List<RoomDatabase.Callback> callbacks;
    public final Context context;
    public final String copyFromAssetPath;
    public final File copyFromFile;
    public final RoomDatabase.JournalMode journalMode;
    private final Set<Integer> mMigrationNotRequiredFrom;
    public final RoomDatabase.MigrationContainer migrationContainer;
    public final boolean multiInstanceInvalidation;
    public final String name;
    public final Executor queryExecutor;
    public final boolean requireMigration;
    public final SupportSQLiteOpenHelper.Factory sqliteOpenHelperFactory;
    public final Executor transactionExecutor;

    @Deprecated
    public DatabaseConfiguration(Context context2, String name2, SupportSQLiteOpenHelper.Factory sqliteOpenHelperFactory2, RoomDatabase.MigrationContainer migrationContainer2, List<RoomDatabase.Callback> list, boolean allowMainThreadQueries2, RoomDatabase.JournalMode journalMode2, Executor queryExecutor2, Executor transactionExecutor2, boolean multiInstanceInvalidation2, boolean requireMigration2, boolean allowDestructiveMigrationOnDowngrade2, Set<Integer> set) {
        this(context2, name2, sqliteOpenHelperFactory2, migrationContainer2, list, allowMainThreadQueries2, journalMode2, queryExecutor2, transactionExecutor2, multiInstanceInvalidation2, requireMigration2, allowDestructiveMigrationOnDowngrade2, set, (String) null, (File) null);
    }

    public DatabaseConfiguration(Context context2, String name2, SupportSQLiteOpenHelper.Factory sqliteOpenHelperFactory2, RoomDatabase.MigrationContainer migrationContainer2, List<RoomDatabase.Callback> list, boolean allowMainThreadQueries2, RoomDatabase.JournalMode journalMode2, Executor queryExecutor2, Executor transactionExecutor2, boolean multiInstanceInvalidation2, boolean requireMigration2, boolean allowDestructiveMigrationOnDowngrade2, Set<Integer> set, String copyFromAssetPath2, File copyFromFile2) {
        this.sqliteOpenHelperFactory = sqliteOpenHelperFactory2;
        this.context = context2;
        this.name = name2;
        this.migrationContainer = migrationContainer2;
        this.callbacks = list;
        this.allowMainThreadQueries = allowMainThreadQueries2;
        this.journalMode = journalMode2;
        this.queryExecutor = queryExecutor2;
        this.transactionExecutor = transactionExecutor2;
        this.multiInstanceInvalidation = multiInstanceInvalidation2;
        this.requireMigration = requireMigration2;
        this.allowDestructiveMigrationOnDowngrade = allowDestructiveMigrationOnDowngrade2;
        this.mMigrationNotRequiredFrom = set;
        this.copyFromAssetPath = copyFromAssetPath2;
        this.copyFromFile = copyFromFile2;
    }

    @Deprecated
    public DatabaseConfiguration(Context context2, String name2, SupportSQLiteOpenHelper.Factory sqliteOpenHelperFactory2, RoomDatabase.MigrationContainer migrationContainer2, List<RoomDatabase.Callback> list, boolean allowMainThreadQueries2, RoomDatabase.JournalMode journalMode2, Executor queryExecutor2, boolean requireMigration2, Set<Integer> set) {
        this(context2, name2, sqliteOpenHelperFactory2, migrationContainer2, list, allowMainThreadQueries2, journalMode2, queryExecutor2, queryExecutor2, false, requireMigration2, false, set, (String) null, (File) null);
    }

    public boolean isMigrationRequired(int fromVersion, int toVersion) {
        Set<Integer> set;
        if (!(fromVersion > toVersion) || !this.allowDestructiveMigrationOnDowngrade) {
            return this.requireMigration && ((set = this.mMigrationNotRequiredFrom) == null || !set.contains(Integer.valueOf(fromVersion)));
        }
        return false;
    }

    @Deprecated
    public boolean isMigrationRequiredFrom(int version) {
        return isMigrationRequired(version, version + 1);
    }
}

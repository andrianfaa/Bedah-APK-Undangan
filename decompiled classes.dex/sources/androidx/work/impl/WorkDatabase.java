package androidx.work.impl;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.work.impl.WorkDatabaseMigrations;
import androidx.work.impl.model.DependencyDao;
import androidx.work.impl.model.PreferenceDao;
import androidx.work.impl.model.RawWorkInfoDao;
import androidx.work.impl.model.SystemIdInfoDao;
import androidx.work.impl.model.WorkNameDao;
import androidx.work.impl.model.WorkProgressDao;
import androidx.work.impl.model.WorkSpecDao;
import androidx.work.impl.model.WorkTagDao;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import mt.Log1F380D;

/* compiled from: 00AD */
public abstract class WorkDatabase extends RoomDatabase {
    private static final String PRUNE_SQL_FORMAT_PREFIX = "DELETE FROM workspec WHERE state IN (2, 3, 5) AND (period_start_time + minimum_retention_duration) < ";
    private static final String PRUNE_SQL_FORMAT_SUFFIX = " AND (SELECT COUNT(*)=0 FROM dependency WHERE     prerequisite_id=id AND     work_spec_id NOT IN         (SELECT id FROM workspec WHERE state IN (2, 3, 5)))";
    private static final long PRUNE_THRESHOLD_MILLIS = TimeUnit.DAYS.toMillis(1);

    public static WorkDatabase create(final Context context, Executor queryExecutor, boolean useTestDatabase) {
        RoomDatabase.Builder<WorkDatabase> builder;
        Class<WorkDatabase> cls = WorkDatabase.class;
        if (useTestDatabase) {
            builder = Room.inMemoryDatabaseBuilder(context, cls).allowMainThreadQueries();
        } else {
            String workDatabaseName = WorkDatabasePathHelper.getWorkDatabaseName();
            Log1F380D.a((Object) workDatabaseName);
            builder = Room.databaseBuilder(context, cls, workDatabaseName);
            builder.openHelperFactory(new SupportSQLiteOpenHelper.Factory() {
                public SupportSQLiteOpenHelper create(SupportSQLiteOpenHelper.Configuration configuration) {
                    SupportSQLiteOpenHelper.Configuration.Builder builder = SupportSQLiteOpenHelper.Configuration.builder(context);
                    builder.name(configuration.name).callback(configuration.callback).noBackupDirectory(true);
                    return new FrameworkSQLiteOpenHelperFactory().create(builder.build());
                }
            });
        }
        return builder.setQueryExecutor(queryExecutor).addCallback(generateCleanupCallback()).addMigrations(WorkDatabaseMigrations.MIGRATION_1_2).addMigrations(new WorkDatabaseMigrations.RescheduleMigration(context, 2, 3)).addMigrations(WorkDatabaseMigrations.MIGRATION_3_4).addMigrations(WorkDatabaseMigrations.MIGRATION_4_5).addMigrations(new WorkDatabaseMigrations.RescheduleMigration(context, 5, 6)).addMigrations(WorkDatabaseMigrations.MIGRATION_6_7).addMigrations(WorkDatabaseMigrations.MIGRATION_7_8).addMigrations(WorkDatabaseMigrations.MIGRATION_8_9).addMigrations(new WorkDatabaseMigrations.WorkMigration9To10(context)).addMigrations(new WorkDatabaseMigrations.RescheduleMigration(context, 10, 11)).addMigrations(WorkDatabaseMigrations.MIGRATION_11_12).fallbackToDestructiveMigration().build();
    }

    static RoomDatabase.Callback generateCleanupCallback() {
        return new RoomDatabase.Callback() {
            public void onOpen(SupportSQLiteDatabase db) {
                super.onOpen(db);
                db.beginTransaction();
                try {
                    String pruneSQL = WorkDatabase.getPruneSQL();
                    Log1F380D.a((Object) pruneSQL);
                    db.execSQL(pruneSQL);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }
        };
    }

    static long getPruneDate() {
        return System.currentTimeMillis() - PRUNE_THRESHOLD_MILLIS;
    }

    static String getPruneSQL() {
        return PRUNE_SQL_FORMAT_PREFIX + getPruneDate() + PRUNE_SQL_FORMAT_SUFFIX;
    }

    public abstract DependencyDao dependencyDao();

    public abstract PreferenceDao preferenceDao();

    public abstract RawWorkInfoDao rawWorkInfoDao();

    public abstract SystemIdInfoDao systemIdInfoDao();

    public abstract WorkNameDao workNameDao();

    public abstract WorkProgressDao workProgressDao();

    public abstract WorkSpecDao workSpecDao();

    public abstract WorkTagDao workTagDao();
}

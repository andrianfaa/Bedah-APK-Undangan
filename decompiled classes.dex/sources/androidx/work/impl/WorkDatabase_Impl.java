package androidx.work.impl;

import android.os.Build;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;
import androidx.room.RoomOpenHelper;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.work.impl.model.DependencyDao;
import androidx.work.impl.model.DependencyDao_Impl;
import androidx.work.impl.model.PreferenceDao;
import androidx.work.impl.model.PreferenceDao_Impl;
import androidx.work.impl.model.RawWorkInfoDao;
import androidx.work.impl.model.RawWorkInfoDao_Impl;
import androidx.work.impl.model.SystemIdInfoDao;
import androidx.work.impl.model.SystemIdInfoDao_Impl;
import androidx.work.impl.model.WorkNameDao;
import androidx.work.impl.model.WorkNameDao_Impl;
import androidx.work.impl.model.WorkProgressDao;
import androidx.work.impl.model.WorkProgressDao_Impl;
import androidx.work.impl.model.WorkSpecDao;
import androidx.work.impl.model.WorkSpecDao_Impl;
import androidx.work.impl.model.WorkTagDao;
import androidx.work.impl.model.WorkTagDao_Impl;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public final class WorkDatabase_Impl extends WorkDatabase {
    private volatile DependencyDao _dependencyDao;
    private volatile PreferenceDao _preferenceDao;
    private volatile RawWorkInfoDao _rawWorkInfoDao;
    private volatile SystemIdInfoDao _systemIdInfoDao;
    private volatile WorkNameDao _workNameDao;
    private volatile WorkProgressDao _workProgressDao;
    private volatile WorkSpecDao _workSpecDao;
    private volatile WorkTagDao _workTagDao;

    public void clearAllTables() {
        super.assertNotMainThread();
        SupportSQLiteDatabase writableDatabase = super.getOpenHelper().getWritableDatabase();
        boolean z = Build.VERSION.SDK_INT >= 21;
        if (!z) {
            try {
                writableDatabase.execSQL("PRAGMA foreign_keys = FALSE");
            } catch (Throwable th) {
                super.endTransaction();
                if (!z) {
                    writableDatabase.execSQL("PRAGMA foreign_keys = TRUE");
                }
                writableDatabase.query("PRAGMA wal_checkpoint(FULL)").close();
                if (!writableDatabase.inTransaction()) {
                    writableDatabase.execSQL("VACUUM");
                }
                throw th;
            }
        }
        super.beginTransaction();
        if (z) {
            writableDatabase.execSQL("PRAGMA defer_foreign_keys = TRUE");
        }
        writableDatabase.execSQL("DELETE FROM `Dependency`");
        writableDatabase.execSQL("DELETE FROM `WorkSpec`");
        writableDatabase.execSQL("DELETE FROM `WorkTag`");
        writableDatabase.execSQL("DELETE FROM `SystemIdInfo`");
        writableDatabase.execSQL("DELETE FROM `WorkName`");
        writableDatabase.execSQL("DELETE FROM `WorkProgress`");
        writableDatabase.execSQL("DELETE FROM `Preference`");
        super.setTransactionSuccessful();
        super.endTransaction();
        if (!z) {
            writableDatabase.execSQL("PRAGMA foreign_keys = TRUE");
        }
        writableDatabase.query("PRAGMA wal_checkpoint(FULL)").close();
        if (!writableDatabase.inTransaction()) {
            writableDatabase.execSQL("VACUUM");
        }
    }

    /* access modifiers changed from: protected */
    public InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, new HashMap(0), new HashMap(0), "Dependency", "WorkSpec", "WorkTag", "SystemIdInfo", "WorkName", "WorkProgress", "Preference");
    }

    /* access modifiers changed from: protected */
    public SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
        return configuration.sqliteOpenHelperFactory.create(SupportSQLiteOpenHelper.Configuration.builder(configuration.context).name(configuration.name).callback(new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(12) {
            public void createAllTables(SupportSQLiteDatabase _db) {
                _db.execSQL("CREATE TABLE IF NOT EXISTS `Dependency` (`work_spec_id` TEXT NOT NULL, `prerequisite_id` TEXT NOT NULL, PRIMARY KEY(`work_spec_id`, `prerequisite_id`), FOREIGN KEY(`work_spec_id`) REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE , FOREIGN KEY(`prerequisite_id`) REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
                _db.execSQL("CREATE INDEX IF NOT EXISTS `index_Dependency_work_spec_id` ON `Dependency` (`work_spec_id`)");
                _db.execSQL("CREATE INDEX IF NOT EXISTS `index_Dependency_prerequisite_id` ON `Dependency` (`prerequisite_id`)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `WorkSpec` (`id` TEXT NOT NULL, `state` INTEGER NOT NULL, `worker_class_name` TEXT NOT NULL, `input_merger_class_name` TEXT, `input` BLOB NOT NULL, `output` BLOB NOT NULL, `initial_delay` INTEGER NOT NULL, `interval_duration` INTEGER NOT NULL, `flex_duration` INTEGER NOT NULL, `run_attempt_count` INTEGER NOT NULL, `backoff_policy` INTEGER NOT NULL, `backoff_delay_duration` INTEGER NOT NULL, `period_start_time` INTEGER NOT NULL, `minimum_retention_duration` INTEGER NOT NULL, `schedule_requested_at` INTEGER NOT NULL, `run_in_foreground` INTEGER NOT NULL, `out_of_quota_policy` INTEGER NOT NULL, `required_network_type` INTEGER, `requires_charging` INTEGER NOT NULL, `requires_device_idle` INTEGER NOT NULL, `requires_battery_not_low` INTEGER NOT NULL, `requires_storage_not_low` INTEGER NOT NULL, `trigger_content_update_delay` INTEGER NOT NULL, `trigger_max_content_delay` INTEGER NOT NULL, `content_uri_triggers` BLOB, PRIMARY KEY(`id`))");
                _db.execSQL("CREATE INDEX IF NOT EXISTS `index_WorkSpec_schedule_requested_at` ON `WorkSpec` (`schedule_requested_at`)");
                _db.execSQL("CREATE INDEX IF NOT EXISTS `index_WorkSpec_period_start_time` ON `WorkSpec` (`period_start_time`)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `WorkTag` (`tag` TEXT NOT NULL, `work_spec_id` TEXT NOT NULL, PRIMARY KEY(`tag`, `work_spec_id`), FOREIGN KEY(`work_spec_id`) REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
                _db.execSQL("CREATE INDEX IF NOT EXISTS `index_WorkTag_work_spec_id` ON `WorkTag` (`work_spec_id`)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `SystemIdInfo` (`work_spec_id` TEXT NOT NULL, `system_id` INTEGER NOT NULL, PRIMARY KEY(`work_spec_id`), FOREIGN KEY(`work_spec_id`) REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `WorkName` (`name` TEXT NOT NULL, `work_spec_id` TEXT NOT NULL, PRIMARY KEY(`name`, `work_spec_id`), FOREIGN KEY(`work_spec_id`) REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
                _db.execSQL("CREATE INDEX IF NOT EXISTS `index_WorkName_work_spec_id` ON `WorkName` (`work_spec_id`)");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `WorkProgress` (`work_spec_id` TEXT NOT NULL, `progress` BLOB NOT NULL, PRIMARY KEY(`work_spec_id`), FOREIGN KEY(`work_spec_id`) REFERENCES `WorkSpec`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )");
                _db.execSQL("CREATE TABLE IF NOT EXISTS `Preference` (`key` TEXT NOT NULL, `long_value` INTEGER, PRIMARY KEY(`key`))");
                _db.execSQL(RoomMasterTable.CREATE_QUERY);
                _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c103703e120ae8cc73c9248622f3cd1e')");
            }

            public void dropAllTables(SupportSQLiteDatabase _db) {
                _db.execSQL("DROP TABLE IF EXISTS `Dependency`");
                _db.execSQL("DROP TABLE IF EXISTS `WorkSpec`");
                _db.execSQL("DROP TABLE IF EXISTS `WorkTag`");
                _db.execSQL("DROP TABLE IF EXISTS `SystemIdInfo`");
                _db.execSQL("DROP TABLE IF EXISTS `WorkName`");
                _db.execSQL("DROP TABLE IF EXISTS `WorkProgress`");
                _db.execSQL("DROP TABLE IF EXISTS `Preference`");
                if (WorkDatabase_Impl.this.mCallbacks != null) {
                    int size = WorkDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) WorkDatabase_Impl.this.mCallbacks.get(i)).onDestructiveMigration(_db);
                    }
                }
            }

            /* access modifiers changed from: protected */
            public void onCreate(SupportSQLiteDatabase _db) {
                if (WorkDatabase_Impl.this.mCallbacks != null) {
                    int size = WorkDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) WorkDatabase_Impl.this.mCallbacks.get(i)).onCreate(_db);
                    }
                }
            }

            public void onOpen(SupportSQLiteDatabase _db) {
                SupportSQLiteDatabase unused = WorkDatabase_Impl.this.mDatabase = _db;
                _db.execSQL("PRAGMA foreign_keys = ON");
                WorkDatabase_Impl.this.internalInitInvalidationTracker(_db);
                if (WorkDatabase_Impl.this.mCallbacks != null) {
                    int size = WorkDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) WorkDatabase_Impl.this.mCallbacks.get(i)).onOpen(_db);
                    }
                }
            }

            public void onPostMigrate(SupportSQLiteDatabase _db) {
            }

            public void onPreMigrate(SupportSQLiteDatabase _db) {
                DBUtil.dropFtsSyncTriggers(_db);
            }

            /* access modifiers changed from: protected */
            public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
                SupportSQLiteDatabase supportSQLiteDatabase = _db;
                HashMap hashMap = new HashMap(2);
                hashMap.put("work_spec_id", new TableInfo.Column("work_spec_id", "TEXT", true, 1, (String) null, 1));
                hashMap.put("prerequisite_id", new TableInfo.Column("prerequisite_id", "TEXT", true, 2, (String) null, 1));
                HashSet hashSet = new HashSet(2);
                hashSet.add(new TableInfo.ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList(new String[]{"work_spec_id"}), Arrays.asList(new String[]{"id"})));
                hashSet.add(new TableInfo.ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList(new String[]{"prerequisite_id"}), Arrays.asList(new String[]{"id"})));
                HashSet hashSet2 = new HashSet(2);
                hashSet2.add(new TableInfo.Index("index_Dependency_work_spec_id", false, Arrays.asList(new String[]{"work_spec_id"})));
                hashSet2.add(new TableInfo.Index("index_Dependency_prerequisite_id", false, Arrays.asList(new String[]{"prerequisite_id"})));
                TableInfo tableInfo = new TableInfo("Dependency", hashMap, hashSet, hashSet2);
                TableInfo read = TableInfo.read(supportSQLiteDatabase, "Dependency");
                if (!tableInfo.equals(read)) {
                    return new RoomOpenHelper.ValidationResult(false, "Dependency(androidx.work.impl.model.Dependency).\n Expected:\n" + tableInfo + "\n Found:\n" + read);
                }
                HashMap hashMap2 = new HashMap(25);
                hashMap2.put("id", new TableInfo.Column("id", "TEXT", true, 1, (String) null, 1));
                hashMap2.put("state", new TableInfo.Column("state", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("worker_class_name", new TableInfo.Column("worker_class_name", "TEXT", true, 0, (String) null, 1));
                hashMap2.put("input_merger_class_name", new TableInfo.Column("input_merger_class_name", "TEXT", false, 0, (String) null, 1));
                hashMap2.put("input", new TableInfo.Column("input", "BLOB", true, 0, (String) null, 1));
                hashMap2.put("output", new TableInfo.Column("output", "BLOB", true, 0, (String) null, 1));
                hashMap2.put("initial_delay", new TableInfo.Column("initial_delay", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("interval_duration", new TableInfo.Column("interval_duration", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("flex_duration", new TableInfo.Column("flex_duration", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("run_attempt_count", new TableInfo.Column("run_attempt_count", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("backoff_policy", new TableInfo.Column("backoff_policy", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("backoff_delay_duration", new TableInfo.Column("backoff_delay_duration", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("period_start_time", new TableInfo.Column("period_start_time", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("minimum_retention_duration", new TableInfo.Column("minimum_retention_duration", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("schedule_requested_at", new TableInfo.Column("schedule_requested_at", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("run_in_foreground", new TableInfo.Column("run_in_foreground", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("out_of_quota_policy", new TableInfo.Column("out_of_quota_policy", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("required_network_type", new TableInfo.Column("required_network_type", "INTEGER", false, 0, (String) null, 1));
                hashMap2.put("requires_charging", new TableInfo.Column("requires_charging", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("requires_device_idle", new TableInfo.Column("requires_device_idle", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("requires_battery_not_low", new TableInfo.Column("requires_battery_not_low", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("requires_storage_not_low", new TableInfo.Column("requires_storage_not_low", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("trigger_content_update_delay", new TableInfo.Column("trigger_content_update_delay", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("trigger_max_content_delay", new TableInfo.Column("trigger_max_content_delay", "INTEGER", true, 0, (String) null, 1));
                hashMap2.put("content_uri_triggers", new TableInfo.Column("content_uri_triggers", "BLOB", false, 0, (String) null, 1));
                HashSet hashSet3 = new HashSet(0);
                HashSet hashSet4 = new HashSet(2);
                HashMap hashMap3 = hashMap;
                hashSet4.add(new TableInfo.Index("index_WorkSpec_schedule_requested_at", false, Arrays.asList(new String[]{"schedule_requested_at"})));
                hashSet4.add(new TableInfo.Index("index_WorkSpec_period_start_time", false, Arrays.asList(new String[]{"period_start_time"})));
                TableInfo tableInfo2 = new TableInfo("WorkSpec", hashMap2, hashSet3, hashSet4);
                TableInfo read2 = TableInfo.read(supportSQLiteDatabase, "WorkSpec");
                if (!tableInfo2.equals(read2)) {
                    return new RoomOpenHelper.ValidationResult(false, "WorkSpec(androidx.work.impl.model.WorkSpec).\n Expected:\n" + tableInfo2 + "\n Found:\n" + read2);
                }
                HashMap hashMap4 = new HashMap(2);
                hashMap4.put("tag", new TableInfo.Column("tag", "TEXT", true, 1, (String) null, 1));
                hashMap4.put("work_spec_id", new TableInfo.Column("work_spec_id", "TEXT", true, 2, (String) null, 1));
                HashSet hashSet5 = new HashSet(1);
                hashSet5.add(new TableInfo.ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList(new String[]{"work_spec_id"}), Arrays.asList(new String[]{"id"})));
                TableInfo tableInfo3 = tableInfo2;
                HashSet hashSet6 = new HashSet(1);
                TableInfo tableInfo4 = read2;
                HashSet hashSet7 = hashSet;
                TableInfo tableInfo5 = tableInfo;
                hashSet6.add(new TableInfo.Index("index_WorkTag_work_spec_id", false, Arrays.asList(new String[]{"work_spec_id"})));
                TableInfo tableInfo6 = new TableInfo("WorkTag", hashMap4, hashSet5, hashSet6);
                TableInfo read3 = TableInfo.read(supportSQLiteDatabase, "WorkTag");
                if (!tableInfo6.equals(read3)) {
                    return new RoomOpenHelper.ValidationResult(false, "WorkTag(androidx.work.impl.model.WorkTag).\n Expected:\n" + tableInfo6 + "\n Found:\n" + read3);
                }
                HashMap hashMap5 = new HashMap(2);
                hashMap5.put("work_spec_id", new TableInfo.Column("work_spec_id", "TEXT", true, 1, (String) null, 1));
                HashSet hashSet8 = hashSet6;
                hashMap5.put("system_id", new TableInfo.Column("system_id", "INTEGER", true, 0, (String) null, 1));
                HashSet hashSet9 = new HashSet(1);
                hashSet9.add(new TableInfo.ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList(new String[]{"work_spec_id"}), Arrays.asList(new String[]{"id"})));
                TableInfo tableInfo7 = tableInfo6;
                HashSet hashSet10 = new HashSet(0);
                TableInfo tableInfo8 = read3;
                TableInfo tableInfo9 = new TableInfo("SystemIdInfo", hashMap5, hashSet9, hashSet10);
                TableInfo read4 = TableInfo.read(supportSQLiteDatabase, "SystemIdInfo");
                if (!tableInfo9.equals(read4)) {
                    HashSet hashSet11 = hashSet9;
                    return new RoomOpenHelper.ValidationResult(false, "SystemIdInfo(androidx.work.impl.model.SystemIdInfo).\n Expected:\n" + tableInfo9 + "\n Found:\n" + read4);
                }
                HashSet hashSet12 = hashSet9;
                HashSet hashSet13 = hashSet10;
                HashMap hashMap6 = new HashMap(2);
                TableInfo tableInfo10 = read4;
                hashMap6.put("name", new TableInfo.Column("name", "TEXT", true, 1, (String) null, 1));
                hashMap6.put("work_spec_id", new TableInfo.Column("work_spec_id", "TEXT", true, 2, (String) null, 1));
                HashSet hashSet14 = new HashSet(1);
                hashSet14.add(new TableInfo.ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList(new String[]{"work_spec_id"}), Arrays.asList(new String[]{"id"})));
                HashMap hashMap7 = hashMap5;
                HashSet hashSet15 = new HashSet(1);
                HashSet hashSet16 = hashSet2;
                TableInfo tableInfo11 = read;
                HashMap hashMap8 = hashMap2;
                hashSet15.add(new TableInfo.Index("index_WorkName_work_spec_id", false, Arrays.asList(new String[]{"work_spec_id"})));
                TableInfo tableInfo12 = new TableInfo("WorkName", hashMap6, hashSet14, hashSet15);
                TableInfo read5 = TableInfo.read(supportSQLiteDatabase, "WorkName");
                if (!tableInfo12.equals(read5)) {
                    return new RoomOpenHelper.ValidationResult(false, "WorkName(androidx.work.impl.model.WorkName).\n Expected:\n" + tableInfo12 + "\n Found:\n" + read5);
                }
                HashMap hashMap9 = new HashMap(2);
                hashMap9.put("work_spec_id", new TableInfo.Column("work_spec_id", "TEXT", true, 1, (String) null, 1));
                HashMap hashMap10 = hashMap6;
                hashMap9.put("progress", new TableInfo.Column("progress", "BLOB", true, 0, (String) null, 1));
                HashSet hashSet17 = new HashSet(1);
                hashSet17.add(new TableInfo.ForeignKey("WorkSpec", "CASCADE", "CASCADE", Arrays.asList(new String[]{"work_spec_id"}), Arrays.asList(new String[]{"id"})));
                HashSet hashSet18 = new HashSet(0);
                TableInfo tableInfo13 = new TableInfo("WorkProgress", hashMap9, hashSet17, hashSet18);
                TableInfo read6 = TableInfo.read(supportSQLiteDatabase, "WorkProgress");
                if (!tableInfo13.equals(read6)) {
                    HashSet hashSet19 = hashSet17;
                    HashSet hashSet20 = hashSet14;
                    HashSet hashSet21 = hashSet18;
                    return new RoomOpenHelper.ValidationResult(false, "WorkProgress(androidx.work.impl.model.WorkProgress).\n Expected:\n" + tableInfo13 + "\n Found:\n" + read6);
                }
                HashSet hashSet22 = hashSet17;
                HashSet hashSet23 = hashSet14;
                HashSet hashSet24 = hashSet18;
                HashMap hashMap11 = new HashMap(2);
                hashMap11.put("key", new TableInfo.Column("key", "TEXT", true, 1, (String) null, 1));
                hashMap11.put("long_value", new TableInfo.Column("long_value", "INTEGER", false, 0, (String) null, 1));
                HashSet hashSet25 = new HashSet(0);
                HashSet hashSet26 = hashSet15;
                TableInfo tableInfo14 = tableInfo12;
                TableInfo tableInfo15 = new TableInfo("Preference", hashMap11, hashSet25, new HashSet(0));
                TableInfo read7 = TableInfo.read(supportSQLiteDatabase, "Preference");
                if (!tableInfo15.equals(read7)) {
                    HashMap hashMap12 = hashMap11;
                    HashSet hashSet27 = hashSet25;
                    return new RoomOpenHelper.ValidationResult(false, "Preference(androidx.work.impl.model.Preference).\n Expected:\n" + tableInfo15 + "\n Found:\n" + read7);
                }
                HashMap hashMap13 = hashMap11;
                HashSet hashSet28 = hashSet25;
                return new RoomOpenHelper.ValidationResult(true, (String) null);
            }
        }, "c103703e120ae8cc73c9248622f3cd1e", "49f946663a8deb7054212b8adda248c6")).build());
    }

    public DependencyDao dependencyDao() {
        DependencyDao dependencyDao;
        if (this._dependencyDao != null) {
            return this._dependencyDao;
        }
        synchronized (this) {
            if (this._dependencyDao == null) {
                this._dependencyDao = new DependencyDao_Impl(this);
            }
            dependencyDao = this._dependencyDao;
        }
        return dependencyDao;
    }

    public PreferenceDao preferenceDao() {
        PreferenceDao preferenceDao;
        if (this._preferenceDao != null) {
            return this._preferenceDao;
        }
        synchronized (this) {
            if (this._preferenceDao == null) {
                this._preferenceDao = new PreferenceDao_Impl(this);
            }
            preferenceDao = this._preferenceDao;
        }
        return preferenceDao;
    }

    public RawWorkInfoDao rawWorkInfoDao() {
        RawWorkInfoDao rawWorkInfoDao;
        if (this._rawWorkInfoDao != null) {
            return this._rawWorkInfoDao;
        }
        synchronized (this) {
            if (this._rawWorkInfoDao == null) {
                this._rawWorkInfoDao = new RawWorkInfoDao_Impl(this);
            }
            rawWorkInfoDao = this._rawWorkInfoDao;
        }
        return rawWorkInfoDao;
    }

    public SystemIdInfoDao systemIdInfoDao() {
        SystemIdInfoDao systemIdInfoDao;
        if (this._systemIdInfoDao != null) {
            return this._systemIdInfoDao;
        }
        synchronized (this) {
            if (this._systemIdInfoDao == null) {
                this._systemIdInfoDao = new SystemIdInfoDao_Impl(this);
            }
            systemIdInfoDao = this._systemIdInfoDao;
        }
        return systemIdInfoDao;
    }

    public WorkNameDao workNameDao() {
        WorkNameDao workNameDao;
        if (this._workNameDao != null) {
            return this._workNameDao;
        }
        synchronized (this) {
            if (this._workNameDao == null) {
                this._workNameDao = new WorkNameDao_Impl(this);
            }
            workNameDao = this._workNameDao;
        }
        return workNameDao;
    }

    public WorkProgressDao workProgressDao() {
        WorkProgressDao workProgressDao;
        if (this._workProgressDao != null) {
            return this._workProgressDao;
        }
        synchronized (this) {
            if (this._workProgressDao == null) {
                this._workProgressDao = new WorkProgressDao_Impl(this);
            }
            workProgressDao = this._workProgressDao;
        }
        return workProgressDao;
    }

    public WorkSpecDao workSpecDao() {
        WorkSpecDao workSpecDao;
        if (this._workSpecDao != null) {
            return this._workSpecDao;
        }
        synchronized (this) {
            if (this._workSpecDao == null) {
                this._workSpecDao = new WorkSpecDao_Impl(this);
            }
            workSpecDao = this._workSpecDao;
        }
        return workSpecDao;
    }

    public WorkTagDao workTagDao() {
        WorkTagDao workTagDao;
        if (this._workTagDao != null) {
            return this._workTagDao;
        }
        synchronized (this) {
            if (this._workTagDao == null) {
                this._workTagDao = new WorkTagDao_Impl(this);
            }
            workTagDao = this._workTagDao;
        }
        return workTagDao;
    }
}

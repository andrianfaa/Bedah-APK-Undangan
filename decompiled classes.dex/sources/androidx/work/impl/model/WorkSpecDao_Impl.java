package androidx.work.impl.model;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.collection.ArrayMap;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import androidx.work.Constraints;
import androidx.work.ContentUriTriggers;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.WorkInfo;
import androidx.work.impl.model.WorkSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public final class WorkSpecDao_Impl implements WorkSpecDao {
    /* access modifiers changed from: private */
    public final RoomDatabase __db;
    private final EntityInsertionAdapter<WorkSpec> __insertionAdapterOfWorkSpec;
    private final SharedSQLiteStatement __preparedStmtOfDelete;
    private final SharedSQLiteStatement __preparedStmtOfIncrementWorkSpecRunAttemptCount;
    private final SharedSQLiteStatement __preparedStmtOfMarkWorkSpecScheduled;
    private final SharedSQLiteStatement __preparedStmtOfPruneFinishedWorkWithZeroDependentsIgnoringKeepForAtLeast;
    private final SharedSQLiteStatement __preparedStmtOfResetScheduledState;
    private final SharedSQLiteStatement __preparedStmtOfResetWorkSpecRunAttemptCount;
    private final SharedSQLiteStatement __preparedStmtOfSetOutput;
    private final SharedSQLiteStatement __preparedStmtOfSetPeriodStartTime;

    public WorkSpecDao_Impl(RoomDatabase __db2) {
        this.__db = __db2;
        this.__insertionAdapterOfWorkSpec = new EntityInsertionAdapter<WorkSpec>(__db2) {
            public void bind(SupportSQLiteStatement stmt, WorkSpec value) {
                SupportSQLiteStatement supportSQLiteStatement = stmt;
                WorkSpec workSpec = value;
                if (workSpec.id == null) {
                    supportSQLiteStatement.bindNull(1);
                } else {
                    supportSQLiteStatement.bindString(1, workSpec.id);
                }
                supportSQLiteStatement.bindLong(2, (long) WorkTypeConverters.stateToInt(workSpec.state));
                if (workSpec.workerClassName == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, workSpec.workerClassName);
                }
                if (workSpec.inputMergerClassName == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, workSpec.inputMergerClassName);
                }
                byte[] byteArrayInternal = Data.toByteArrayInternal(workSpec.input);
                if (byteArrayInternal == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindBlob(5, byteArrayInternal);
                }
                byte[] byteArrayInternal2 = Data.toByteArrayInternal(workSpec.output);
                if (byteArrayInternal2 == null) {
                    supportSQLiteStatement.bindNull(6);
                } else {
                    supportSQLiteStatement.bindBlob(6, byteArrayInternal2);
                }
                supportSQLiteStatement.bindLong(7, workSpec.initialDelay);
                supportSQLiteStatement.bindLong(8, workSpec.intervalDuration);
                supportSQLiteStatement.bindLong(9, workSpec.flexDuration);
                supportSQLiteStatement.bindLong(10, (long) workSpec.runAttemptCount);
                supportSQLiteStatement.bindLong(11, (long) WorkTypeConverters.backoffPolicyToInt(workSpec.backoffPolicy));
                supportSQLiteStatement.bindLong(12, workSpec.backoffDelayDuration);
                supportSQLiteStatement.bindLong(13, workSpec.periodStartTime);
                supportSQLiteStatement.bindLong(14, workSpec.minimumRetentionDuration);
                supportSQLiteStatement.bindLong(15, workSpec.scheduleRequestedAt);
                supportSQLiteStatement.bindLong(16, workSpec.expedited ? 1 : 0);
                supportSQLiteStatement.bindLong(17, (long) WorkTypeConverters.outOfQuotaPolicyToInt(workSpec.outOfQuotaPolicy));
                Constraints constraints = workSpec.constraints;
                if (constraints != null) {
                    supportSQLiteStatement.bindLong(18, (long) WorkTypeConverters.networkTypeToInt(constraints.getRequiredNetworkType()));
                    supportSQLiteStatement.bindLong(19, constraints.requiresCharging() ? 1 : 0);
                    supportSQLiteStatement.bindLong(20, constraints.requiresDeviceIdle() ? 1 : 0);
                    supportSQLiteStatement.bindLong(21, constraints.requiresBatteryNotLow() ? 1 : 0);
                    supportSQLiteStatement.bindLong(22, constraints.requiresStorageNotLow() ? 1 : 0);
                    supportSQLiteStatement.bindLong(23, constraints.getTriggerContentUpdateDelay());
                    supportSQLiteStatement.bindLong(24, constraints.getTriggerMaxContentDelay());
                    byte[] contentUriTriggersToByteArray = WorkTypeConverters.contentUriTriggersToByteArray(constraints.getContentUriTriggers());
                    if (contentUriTriggersToByteArray == null) {
                        supportSQLiteStatement.bindNull(25);
                    } else {
                        supportSQLiteStatement.bindBlob(25, contentUriTriggersToByteArray);
                    }
                } else {
                    supportSQLiteStatement.bindNull(18);
                    supportSQLiteStatement.bindNull(19);
                    supportSQLiteStatement.bindNull(20);
                    supportSQLiteStatement.bindNull(21);
                    supportSQLiteStatement.bindNull(22);
                    supportSQLiteStatement.bindNull(23);
                    supportSQLiteStatement.bindNull(24);
                    supportSQLiteStatement.bindNull(25);
                }
            }

            public String createQuery() {
                return "INSERT OR IGNORE INTO `WorkSpec` (`id`,`state`,`worker_class_name`,`input_merger_class_name`,`input`,`output`,`initial_delay`,`interval_duration`,`flex_duration`,`run_attempt_count`,`backoff_policy`,`backoff_delay_duration`,`period_start_time`,`minimum_retention_duration`,`schedule_requested_at`,`run_in_foreground`,`out_of_quota_policy`,`required_network_type`,`requires_charging`,`requires_device_idle`,`requires_battery_not_low`,`requires_storage_not_low`,`trigger_content_update_delay`,`trigger_max_content_delay`,`content_uri_triggers`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }
        };
        this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db2) {
            public String createQuery() {
                return "DELETE FROM workspec WHERE id=?";
            }
        };
        this.__preparedStmtOfSetOutput = new SharedSQLiteStatement(__db2) {
            public String createQuery() {
                return "UPDATE workspec SET output=? WHERE id=?";
            }
        };
        this.__preparedStmtOfSetPeriodStartTime = new SharedSQLiteStatement(__db2) {
            public String createQuery() {
                return "UPDATE workspec SET period_start_time=? WHERE id=?";
            }
        };
        this.__preparedStmtOfIncrementWorkSpecRunAttemptCount = new SharedSQLiteStatement(__db2) {
            public String createQuery() {
                return "UPDATE workspec SET run_attempt_count=run_attempt_count+1 WHERE id=?";
            }
        };
        this.__preparedStmtOfResetWorkSpecRunAttemptCount = new SharedSQLiteStatement(__db2) {
            public String createQuery() {
                return "UPDATE workspec SET run_attempt_count=0 WHERE id=?";
            }
        };
        this.__preparedStmtOfMarkWorkSpecScheduled = new SharedSQLiteStatement(__db2) {
            public String createQuery() {
                return "UPDATE workspec SET schedule_requested_at=? WHERE id=?";
            }
        };
        this.__preparedStmtOfResetScheduledState = new SharedSQLiteStatement(__db2) {
            public String createQuery() {
                return "UPDATE workspec SET schedule_requested_at=-1 WHERE state NOT IN (2, 3, 5)";
            }
        };
        this.__preparedStmtOfPruneFinishedWorkWithZeroDependentsIgnoringKeepForAtLeast = new SharedSQLiteStatement(__db2) {
            public String createQuery() {
                return "DELETE FROM workspec WHERE state IN (2, 3, 5) AND (SELECT COUNT(*)=0 FROM dependency WHERE     prerequisite_id=id AND     work_spec_id NOT IN         (SELECT id FROM workspec WHERE state IN (2, 3, 5)))";
            }
        };
    }

    /* access modifiers changed from: private */
    public void __fetchRelationshipWorkProgressAsandroidxWorkData(ArrayMap<String, ArrayList<Data>> arrayMap) {
        ArrayList arrayList;
        Set<String> keySet = arrayMap.keySet();
        if (!keySet.isEmpty()) {
            if (arrayMap.size() > 999) {
                ArrayMap arrayMap2 = new ArrayMap((int) RoomDatabase.MAX_BIND_PARAMETER_CNT);
                int i = 0;
                int i2 = 0;
                int size = arrayMap.size();
                while (i2 < size) {
                    arrayMap2.put(arrayMap.keyAt(i2), arrayMap.valueAt(i2));
                    i2++;
                    i++;
                    if (i == 999) {
                        __fetchRelationshipWorkProgressAsandroidxWorkData(arrayMap2);
                        arrayMap2 = new ArrayMap((int) RoomDatabase.MAX_BIND_PARAMETER_CNT);
                        i = 0;
                    }
                }
                if (i > 0) {
                    __fetchRelationshipWorkProgressAsandroidxWorkData(arrayMap2);
                    return;
                }
                return;
            }
            StringBuilder newStringBuilder = StringUtil.newStringBuilder();
            newStringBuilder.append("SELECT `progress`,`work_spec_id` FROM `WorkProgress` WHERE `work_spec_id` IN (");
            int size2 = keySet.size();
            StringUtil.appendPlaceholders(newStringBuilder, size2);
            newStringBuilder.append(")");
            RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(newStringBuilder.toString(), size2 + 0);
            int i3 = 1;
            for (String next : keySet) {
                if (next == null) {
                    acquire.bindNull(i3);
                } else {
                    acquire.bindString(i3, next);
                }
                i3++;
            }
            Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
            try {
                int columnIndex = CursorUtil.getColumnIndex(query, "work_spec_id");
                if (columnIndex != -1) {
                    while (query.moveToNext()) {
                        if (!query.isNull(columnIndex) && (arrayList = arrayMap.get(query.getString(columnIndex))) != null) {
                            arrayList.add(Data.fromByteArray(query.getBlob(0)));
                        }
                    }
                    query.close();
                }
            } finally {
                query.close();
            }
        }
    }

    /* access modifiers changed from: private */
    public void __fetchRelationshipWorkTagAsjavaLangString(ArrayMap<String, ArrayList<String>> arrayMap) {
        ArrayList arrayList;
        Set<String> keySet = arrayMap.keySet();
        if (!keySet.isEmpty()) {
            if (arrayMap.size() > 999) {
                ArrayMap arrayMap2 = new ArrayMap((int) RoomDatabase.MAX_BIND_PARAMETER_CNT);
                int i = 0;
                int i2 = 0;
                int size = arrayMap.size();
                while (i2 < size) {
                    arrayMap2.put(arrayMap.keyAt(i2), arrayMap.valueAt(i2));
                    i2++;
                    i++;
                    if (i == 999) {
                        __fetchRelationshipWorkTagAsjavaLangString(arrayMap2);
                        arrayMap2 = new ArrayMap((int) RoomDatabase.MAX_BIND_PARAMETER_CNT);
                        i = 0;
                    }
                }
                if (i > 0) {
                    __fetchRelationshipWorkTagAsjavaLangString(arrayMap2);
                    return;
                }
                return;
            }
            StringBuilder newStringBuilder = StringUtil.newStringBuilder();
            newStringBuilder.append("SELECT `tag`,`work_spec_id` FROM `WorkTag` WHERE `work_spec_id` IN (");
            int size2 = keySet.size();
            StringUtil.appendPlaceholders(newStringBuilder, size2);
            newStringBuilder.append(")");
            RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(newStringBuilder.toString(), size2 + 0);
            int i3 = 1;
            for (String next : keySet) {
                if (next == null) {
                    acquire.bindNull(i3);
                } else {
                    acquire.bindString(i3, next);
                }
                i3++;
            }
            Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
            try {
                int columnIndex = CursorUtil.getColumnIndex(query, "work_spec_id");
                if (columnIndex != -1) {
                    while (query.moveToNext()) {
                        if (!query.isNull(columnIndex) && (arrayList = arrayMap.get(query.getString(columnIndex))) != null) {
                            arrayList.add(query.getString(0));
                        }
                    }
                    query.close();
                }
            } finally {
                query.close();
            }
        }
    }

    public void delete(String id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDelete.acquire();
        if (id == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, id);
        }
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDelete.release(acquire);
        }
    }

    public List<WorkSpec> getAllEligibleWorkSpecsForScheduling(int maxLimit) {
        RoomSQLiteQuery roomSQLiteQuery;
        int columnIndexOrThrow;
        int columnIndexOrThrow2;
        int columnIndexOrThrow3;
        boolean z;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE state=0 ORDER BY period_start_time LIMIT ?", 1);
        acquire.bindLong(1, (long) maxLimit);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "required_network_type");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "requires_charging");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "requires_device_idle");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "requires_battery_not_low");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "requires_storage_not_low");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "trigger_content_update_delay");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "trigger_max_content_delay");
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "content_uri_triggers");
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "state");
            int columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "worker_class_name");
            Object obj = "SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE state=0 ORDER BY period_start_time LIMIT ?";
            try {
                columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "input_merger_class_name");
            } catch (Throwable th) {
                th = th;
                roomSQLiteQuery = acquire;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
            try {
                columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "input");
                columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "output");
                roomSQLiteQuery = acquire;
            } catch (Throwable th2) {
                th = th2;
                roomSQLiteQuery = acquire;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
            try {
                int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "initial_delay");
                int columnIndexOrThrow16 = CursorUtil.getColumnIndexOrThrow(query, "interval_duration");
                int columnIndexOrThrow17 = CursorUtil.getColumnIndexOrThrow(query, "flex_duration");
                int columnIndexOrThrow18 = CursorUtil.getColumnIndexOrThrow(query, "run_attempt_count");
                int columnIndexOrThrow19 = CursorUtil.getColumnIndexOrThrow(query, "backoff_policy");
                int columnIndexOrThrow20 = CursorUtil.getColumnIndexOrThrow(query, "backoff_delay_duration");
                int columnIndexOrThrow21 = CursorUtil.getColumnIndexOrThrow(query, "period_start_time");
                int columnIndexOrThrow22 = CursorUtil.getColumnIndexOrThrow(query, "minimum_retention_duration");
                int columnIndexOrThrow23 = CursorUtil.getColumnIndexOrThrow(query, "schedule_requested_at");
                int columnIndexOrThrow24 = CursorUtil.getColumnIndexOrThrow(query, "run_in_foreground");
                int columnIndexOrThrow25 = CursorUtil.getColumnIndexOrThrow(query, "out_of_quota_policy");
                int i = columnIndexOrThrow3;
                ArrayList arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    String string = query.getString(columnIndexOrThrow12);
                    String string2 = query.getString(columnIndexOrThrow14);
                    Constraints constraints = new Constraints();
                    NetworkType intToNetworkType = WorkTypeConverters.intToNetworkType(query.getInt(columnIndexOrThrow4));
                    int i2 = columnIndexOrThrow4;
                    Constraints constraints2 = constraints;
                    int i3 = columnIndexOrThrow14;
                    NetworkType networkType = intToNetworkType;
                    constraints2.setRequiredNetworkType(networkType);
                    NetworkType networkType2 = networkType;
                    boolean z2 = query.getInt(columnIndexOrThrow5) != 0;
                    constraints2.setRequiresCharging(z2);
                    boolean z3 = z2;
                    boolean z4 = query.getInt(columnIndexOrThrow6) != 0;
                    constraints2.setRequiresDeviceIdle(z4);
                    boolean z5 = z4;
                    boolean z6 = query.getInt(columnIndexOrThrow7) != 0;
                    constraints2.setRequiresBatteryNotLow(z6);
                    boolean z7 = z6;
                    boolean z8 = query.getInt(columnIndexOrThrow8) != 0;
                    constraints2.setRequiresStorageNotLow(z8);
                    int i4 = columnIndexOrThrow5;
                    int i5 = columnIndexOrThrow6;
                    long j = query.getLong(columnIndexOrThrow9);
                    constraints2.setTriggerContentUpdateDelay(j);
                    long j2 = j;
                    constraints2.setTriggerMaxContentDelay(query.getLong(columnIndexOrThrow10));
                    boolean z9 = z8;
                    ContentUriTriggers byteArrayToContentUriTriggers = WorkTypeConverters.byteArrayToContentUriTriggers(query.getBlob(columnIndexOrThrow11));
                    constraints2.setContentUriTriggers(byteArrayToContentUriTriggers);
                    ContentUriTriggers contentUriTriggers = byteArrayToContentUriTriggers;
                    int i6 = columnIndexOrThrow12;
                    String str = string2;
                    WorkSpec workSpec = new WorkSpec(string, str);
                    String str2 = str;
                    workSpec.state = WorkTypeConverters.intToState(query.getInt(columnIndexOrThrow13));
                    workSpec.inputMergerClassName = query.getString(columnIndexOrThrow);
                    byte[] blob = query.getBlob(columnIndexOrThrow2);
                    int i7 = columnIndexOrThrow;
                    workSpec.input = Data.fromByteArray(blob);
                    int i8 = i;
                    int i9 = i8;
                    workSpec.output = Data.fromByteArray(query.getBlob(i8));
                    String str3 = string;
                    int i10 = columnIndexOrThrow15;
                    int i11 = columnIndexOrThrow2;
                    workSpec.initialDelay = query.getLong(i10);
                    byte[] bArr = blob;
                    int i12 = columnIndexOrThrow16;
                    int i13 = i10;
                    workSpec.intervalDuration = query.getLong(i12);
                    int i14 = i12;
                    int i15 = columnIndexOrThrow17;
                    byte[] bArr2 = bArr;
                    workSpec.flexDuration = query.getLong(i15);
                    int i16 = columnIndexOrThrow18;
                    workSpec.runAttemptCount = query.getInt(i16);
                    int i17 = columnIndexOrThrow19;
                    int i18 = i15;
                    workSpec.backoffPolicy = WorkTypeConverters.intToBackoffPolicy(query.getInt(i17));
                    int i19 = i17;
                    int i20 = columnIndexOrThrow20;
                    int i21 = i16;
                    workSpec.backoffDelayDuration = query.getLong(i20);
                    int i22 = i14;
                    int i23 = columnIndexOrThrow21;
                    int i24 = i20;
                    workSpec.periodStartTime = query.getLong(i23);
                    int i25 = i23;
                    int i26 = columnIndexOrThrow22;
                    int i27 = i22;
                    workSpec.minimumRetentionDuration = query.getLong(i26);
                    int i28 = i26;
                    int i29 = columnIndexOrThrow23;
                    int i30 = i25;
                    workSpec.scheduleRequestedAt = query.getLong(i29);
                    int i31 = columnIndexOrThrow24;
                    if (query.getInt(i31) != 0) {
                        columnIndexOrThrow24 = i31;
                        z = true;
                    } else {
                        columnIndexOrThrow24 = i31;
                        z = false;
                    }
                    workSpec.expedited = z;
                    int i32 = columnIndexOrThrow25;
                    int i33 = i32;
                    workSpec.outOfQuotaPolicy = WorkTypeConverters.intToOutOfQuotaPolicy(query.getInt(i32));
                    workSpec.constraints = constraints2;
                    arrayList.add(workSpec);
                    columnIndexOrThrow17 = i18;
                    columnIndexOrThrow18 = i21;
                    columnIndexOrThrow20 = i24;
                    columnIndexOrThrow21 = i30;
                    columnIndexOrThrow14 = i3;
                    columnIndexOrThrow4 = i2;
                    columnIndexOrThrow5 = i4;
                    columnIndexOrThrow6 = i5;
                    columnIndexOrThrow12 = i6;
                    columnIndexOrThrow = i7;
                    i = i9;
                    columnIndexOrThrow19 = i19;
                    columnIndexOrThrow25 = i33;
                    columnIndexOrThrow23 = i29;
                    columnIndexOrThrow2 = i11;
                    columnIndexOrThrow15 = i13;
                    columnIndexOrThrow16 = i27;
                    columnIndexOrThrow22 = i28;
                }
                query.close();
                roomSQLiteQuery.release();
                return arrayList;
            } catch (Throwable th3) {
                th = th3;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            Object obj2 = "SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE state=0 ORDER BY period_start_time LIMIT ?";
            roomSQLiteQuery = acquire;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public List<String> getAllUnfinishedWork() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM workspec WHERE state NOT IN (2, 3, 5)", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(query.getString(0));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public List<String> getAllWorkSpecIds() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM workspec", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(query.getString(0));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public LiveData<List<String>> getAllWorkSpecIdsLiveData() {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM workspec", 0);
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"workspec"}, true, new Callable<List<String>>() {
            public List<String> call() throws Exception {
                Cursor query;
                WorkSpecDao_Impl.this.__db.beginTransaction();
                try {
                    query = DBUtil.query(WorkSpecDao_Impl.this.__db, acquire, false, (CancellationSignal) null);
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        arrayList.add(query.getString(0));
                    }
                    WorkSpecDao_Impl.this.__db.setTransactionSuccessful();
                    query.close();
                    WorkSpecDao_Impl.this.__db.endTransaction();
                    return arrayList;
                } catch (Throwable th) {
                    WorkSpecDao_Impl.this.__db.endTransaction();
                    throw th;
                }
            }

            /* access modifiers changed from: protected */
            public void finalize() {
                acquire.release();
            }
        });
    }

    public List<WorkSpec> getEligibleWorkForScheduling(int schedulerLimit) {
        RoomSQLiteQuery roomSQLiteQuery;
        int columnIndexOrThrow;
        int columnIndexOrThrow2;
        int columnIndexOrThrow3;
        boolean z;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE state=0 AND schedule_requested_at=-1 ORDER BY period_start_time LIMIT (SELECT MAX(?-COUNT(*), 0) FROM workspec WHERE schedule_requested_at<>-1 AND state NOT IN (2, 3, 5))", 1);
        acquire.bindLong(1, (long) schedulerLimit);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "required_network_type");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "requires_charging");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "requires_device_idle");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "requires_battery_not_low");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "requires_storage_not_low");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "trigger_content_update_delay");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "trigger_max_content_delay");
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "content_uri_triggers");
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "state");
            int columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "worker_class_name");
            Object obj = "SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE state=0 AND schedule_requested_at=-1 ORDER BY period_start_time LIMIT (SELECT MAX(?-COUNT(*), 0) FROM workspec WHERE schedule_requested_at<>-1 AND state NOT IN (2, 3, 5))";
            try {
                columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "input_merger_class_name");
            } catch (Throwable th) {
                th = th;
                roomSQLiteQuery = acquire;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
            try {
                columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "input");
                columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "output");
                roomSQLiteQuery = acquire;
            } catch (Throwable th2) {
                th = th2;
                roomSQLiteQuery = acquire;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
            try {
                int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "initial_delay");
                int columnIndexOrThrow16 = CursorUtil.getColumnIndexOrThrow(query, "interval_duration");
                int columnIndexOrThrow17 = CursorUtil.getColumnIndexOrThrow(query, "flex_duration");
                int columnIndexOrThrow18 = CursorUtil.getColumnIndexOrThrow(query, "run_attempt_count");
                int columnIndexOrThrow19 = CursorUtil.getColumnIndexOrThrow(query, "backoff_policy");
                int columnIndexOrThrow20 = CursorUtil.getColumnIndexOrThrow(query, "backoff_delay_duration");
                int columnIndexOrThrow21 = CursorUtil.getColumnIndexOrThrow(query, "period_start_time");
                int columnIndexOrThrow22 = CursorUtil.getColumnIndexOrThrow(query, "minimum_retention_duration");
                int columnIndexOrThrow23 = CursorUtil.getColumnIndexOrThrow(query, "schedule_requested_at");
                int columnIndexOrThrow24 = CursorUtil.getColumnIndexOrThrow(query, "run_in_foreground");
                int columnIndexOrThrow25 = CursorUtil.getColumnIndexOrThrow(query, "out_of_quota_policy");
                int i = columnIndexOrThrow3;
                ArrayList arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    String string = query.getString(columnIndexOrThrow12);
                    String string2 = query.getString(columnIndexOrThrow14);
                    Constraints constraints = new Constraints();
                    NetworkType intToNetworkType = WorkTypeConverters.intToNetworkType(query.getInt(columnIndexOrThrow4));
                    int i2 = columnIndexOrThrow4;
                    Constraints constraints2 = constraints;
                    int i3 = columnIndexOrThrow14;
                    NetworkType networkType = intToNetworkType;
                    constraints2.setRequiredNetworkType(networkType);
                    NetworkType networkType2 = networkType;
                    boolean z2 = query.getInt(columnIndexOrThrow5) != 0;
                    constraints2.setRequiresCharging(z2);
                    boolean z3 = z2;
                    boolean z4 = query.getInt(columnIndexOrThrow6) != 0;
                    constraints2.setRequiresDeviceIdle(z4);
                    boolean z5 = z4;
                    boolean z6 = query.getInt(columnIndexOrThrow7) != 0;
                    constraints2.setRequiresBatteryNotLow(z6);
                    boolean z7 = z6;
                    boolean z8 = query.getInt(columnIndexOrThrow8) != 0;
                    constraints2.setRequiresStorageNotLow(z8);
                    int i4 = columnIndexOrThrow5;
                    int i5 = columnIndexOrThrow6;
                    long j = query.getLong(columnIndexOrThrow9);
                    constraints2.setTriggerContentUpdateDelay(j);
                    long j2 = j;
                    constraints2.setTriggerMaxContentDelay(query.getLong(columnIndexOrThrow10));
                    boolean z9 = z8;
                    ContentUriTriggers byteArrayToContentUriTriggers = WorkTypeConverters.byteArrayToContentUriTriggers(query.getBlob(columnIndexOrThrow11));
                    constraints2.setContentUriTriggers(byteArrayToContentUriTriggers);
                    ContentUriTriggers contentUriTriggers = byteArrayToContentUriTriggers;
                    int i6 = columnIndexOrThrow12;
                    String str = string2;
                    WorkSpec workSpec = new WorkSpec(string, str);
                    String str2 = str;
                    workSpec.state = WorkTypeConverters.intToState(query.getInt(columnIndexOrThrow13));
                    workSpec.inputMergerClassName = query.getString(columnIndexOrThrow);
                    byte[] blob = query.getBlob(columnIndexOrThrow2);
                    int i7 = columnIndexOrThrow;
                    workSpec.input = Data.fromByteArray(blob);
                    int i8 = i;
                    int i9 = i8;
                    workSpec.output = Data.fromByteArray(query.getBlob(i8));
                    String str3 = string;
                    int i10 = columnIndexOrThrow15;
                    int i11 = columnIndexOrThrow2;
                    workSpec.initialDelay = query.getLong(i10);
                    byte[] bArr = blob;
                    int i12 = columnIndexOrThrow16;
                    int i13 = i10;
                    workSpec.intervalDuration = query.getLong(i12);
                    int i14 = i12;
                    int i15 = columnIndexOrThrow17;
                    byte[] bArr2 = bArr;
                    workSpec.flexDuration = query.getLong(i15);
                    int i16 = columnIndexOrThrow18;
                    workSpec.runAttemptCount = query.getInt(i16);
                    int i17 = columnIndexOrThrow19;
                    int i18 = i15;
                    workSpec.backoffPolicy = WorkTypeConverters.intToBackoffPolicy(query.getInt(i17));
                    int i19 = i17;
                    int i20 = columnIndexOrThrow20;
                    int i21 = i16;
                    workSpec.backoffDelayDuration = query.getLong(i20);
                    int i22 = i14;
                    int i23 = columnIndexOrThrow21;
                    int i24 = i20;
                    workSpec.periodStartTime = query.getLong(i23);
                    int i25 = i23;
                    int i26 = columnIndexOrThrow22;
                    int i27 = i22;
                    workSpec.minimumRetentionDuration = query.getLong(i26);
                    int i28 = i26;
                    int i29 = columnIndexOrThrow23;
                    int i30 = i25;
                    workSpec.scheduleRequestedAt = query.getLong(i29);
                    int i31 = columnIndexOrThrow24;
                    if (query.getInt(i31) != 0) {
                        columnIndexOrThrow24 = i31;
                        z = true;
                    } else {
                        columnIndexOrThrow24 = i31;
                        z = false;
                    }
                    workSpec.expedited = z;
                    int i32 = columnIndexOrThrow25;
                    int i33 = i32;
                    workSpec.outOfQuotaPolicy = WorkTypeConverters.intToOutOfQuotaPolicy(query.getInt(i32));
                    workSpec.constraints = constraints2;
                    arrayList.add(workSpec);
                    columnIndexOrThrow17 = i18;
                    columnIndexOrThrow18 = i21;
                    columnIndexOrThrow20 = i24;
                    columnIndexOrThrow21 = i30;
                    columnIndexOrThrow14 = i3;
                    columnIndexOrThrow4 = i2;
                    columnIndexOrThrow5 = i4;
                    columnIndexOrThrow6 = i5;
                    columnIndexOrThrow12 = i6;
                    columnIndexOrThrow = i7;
                    i = i9;
                    columnIndexOrThrow19 = i19;
                    columnIndexOrThrow25 = i33;
                    columnIndexOrThrow23 = i29;
                    columnIndexOrThrow2 = i11;
                    columnIndexOrThrow15 = i13;
                    columnIndexOrThrow16 = i27;
                    columnIndexOrThrow22 = i28;
                }
                query.close();
                roomSQLiteQuery.release();
                return arrayList;
            } catch (Throwable th3) {
                th = th3;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            Object obj2 = "SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE state=0 AND schedule_requested_at=-1 ORDER BY period_start_time LIMIT (SELECT MAX(?-COUNT(*), 0) FROM workspec WHERE schedule_requested_at<>-1 AND state NOT IN (2, 3, 5))";
            roomSQLiteQuery = acquire;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public List<Data> getInputsFromPrerequisites(String id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT output FROM workspec WHERE id IN (SELECT prerequisite_id FROM dependency WHERE work_spec_id=?)", 1);
        if (id == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, id);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(Data.fromByteArray(query.getBlob(0)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public List<WorkSpec> getRecentlyCompletedWork(long startingAt) {
        RoomSQLiteQuery roomSQLiteQuery;
        int columnIndexOrThrow;
        int columnIndexOrThrow2;
        int columnIndexOrThrow3;
        int columnIndexOrThrow4;
        int i;
        boolean z;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE period_start_time >= ? AND state IN (2, 3, 5) ORDER BY period_start_time DESC", 1);
        acquire.bindLong(1, startingAt);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "required_network_type");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "requires_charging");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "requires_device_idle");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "requires_battery_not_low");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "requires_storage_not_low");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "trigger_content_update_delay");
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "trigger_max_content_delay");
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "content_uri_triggers");
            int columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "state");
            Object obj = "SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE period_start_time >= ? AND state IN (2, 3, 5) ORDER BY period_start_time DESC";
            try {
                columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "worker_class_name");
            } catch (Throwable th) {
                th = th;
                roomSQLiteQuery = acquire;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
            try {
                columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "input_merger_class_name");
                columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "input");
                columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "output");
                roomSQLiteQuery = acquire;
            } catch (Throwable th2) {
                th = th2;
                roomSQLiteQuery = acquire;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
            try {
                int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "initial_delay");
                int columnIndexOrThrow16 = CursorUtil.getColumnIndexOrThrow(query, "interval_duration");
                int columnIndexOrThrow17 = CursorUtil.getColumnIndexOrThrow(query, "flex_duration");
                int columnIndexOrThrow18 = CursorUtil.getColumnIndexOrThrow(query, "run_attempt_count");
                int columnIndexOrThrow19 = CursorUtil.getColumnIndexOrThrow(query, "backoff_policy");
                int columnIndexOrThrow20 = CursorUtil.getColumnIndexOrThrow(query, "backoff_delay_duration");
                int columnIndexOrThrow21 = CursorUtil.getColumnIndexOrThrow(query, "period_start_time");
                int columnIndexOrThrow22 = CursorUtil.getColumnIndexOrThrow(query, "minimum_retention_duration");
                int columnIndexOrThrow23 = CursorUtil.getColumnIndexOrThrow(query, "schedule_requested_at");
                int columnIndexOrThrow24 = CursorUtil.getColumnIndexOrThrow(query, "run_in_foreground");
                int columnIndexOrThrow25 = CursorUtil.getColumnIndexOrThrow(query, "out_of_quota_policy");
                int i2 = columnIndexOrThrow4;
                ArrayList arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    String string = query.getString(columnIndexOrThrow13);
                    String string2 = query.getString(columnIndexOrThrow);
                    Constraints constraints = new Constraints();
                    NetworkType intToNetworkType = WorkTypeConverters.intToNetworkType(query.getInt(columnIndexOrThrow5));
                    int i3 = columnIndexOrThrow5;
                    Constraints constraints2 = constraints;
                    int i4 = columnIndexOrThrow;
                    NetworkType networkType = intToNetworkType;
                    constraints2.setRequiredNetworkType(networkType);
                    NetworkType networkType2 = networkType;
                    boolean z2 = query.getInt(columnIndexOrThrow6) != 0;
                    constraints2.setRequiresCharging(z2);
                    boolean z3 = z2;
                    boolean z4 = query.getInt(columnIndexOrThrow7) != 0;
                    constraints2.setRequiresDeviceIdle(z4);
                    boolean z5 = z4;
                    boolean z6 = query.getInt(columnIndexOrThrow8) != 0;
                    constraints2.setRequiresBatteryNotLow(z6);
                    boolean z7 = z6;
                    boolean z8 = query.getInt(columnIndexOrThrow9) != 0;
                    constraints2.setRequiresStorageNotLow(z8);
                    int i5 = columnIndexOrThrow6;
                    int i6 = columnIndexOrThrow7;
                    long j = query.getLong(columnIndexOrThrow10);
                    constraints2.setTriggerContentUpdateDelay(j);
                    long j2 = j;
                    constraints2.setTriggerMaxContentDelay(query.getLong(columnIndexOrThrow11));
                    boolean z9 = z8;
                    ContentUriTriggers byteArrayToContentUriTriggers = WorkTypeConverters.byteArrayToContentUriTriggers(query.getBlob(columnIndexOrThrow12));
                    constraints2.setContentUriTriggers(byteArrayToContentUriTriggers);
                    ContentUriTriggers contentUriTriggers = byteArrayToContentUriTriggers;
                    int i7 = columnIndexOrThrow12;
                    String str = string2;
                    WorkSpec workSpec = new WorkSpec(string, str);
                    int i8 = columnIndexOrThrow14;
                    workSpec.state = WorkTypeConverters.intToState(query.getInt(columnIndexOrThrow14));
                    workSpec.inputMergerClassName = query.getString(columnIndexOrThrow2);
                    byte[] blob = query.getBlob(columnIndexOrThrow3);
                    String str2 = str;
                    workSpec.input = Data.fromByteArray(blob);
                    int i9 = i2;
                    byte[] bArr = blob;
                    workSpec.output = Data.fromByteArray(query.getBlob(i9));
                    int i10 = columnIndexOrThrow3;
                    int i11 = columnIndexOrThrow15;
                    int i12 = columnIndexOrThrow2;
                    workSpec.initialDelay = query.getLong(i11);
                    int i13 = columnIndexOrThrow16;
                    String str3 = string;
                    workSpec.intervalDuration = query.getLong(i13);
                    int i14 = columnIndexOrThrow17;
                    int i15 = columnIndexOrThrow13;
                    workSpec.flexDuration = query.getLong(i14);
                    int i16 = columnIndexOrThrow18;
                    workSpec.runAttemptCount = query.getInt(i16);
                    int i17 = columnIndexOrThrow19;
                    int i18 = i11;
                    workSpec.backoffPolicy = WorkTypeConverters.intToBackoffPolicy(query.getInt(i17));
                    int i19 = i14;
                    int i20 = columnIndexOrThrow20;
                    int i21 = i13;
                    workSpec.backoffDelayDuration = query.getLong(i20);
                    int i22 = columnIndexOrThrow21;
                    int i23 = i16;
                    workSpec.periodStartTime = query.getLong(i22);
                    int i24 = columnIndexOrThrow22;
                    int i25 = i17;
                    workSpec.minimumRetentionDuration = query.getLong(i24);
                    int i26 = i22;
                    int i27 = columnIndexOrThrow23;
                    int i28 = i24;
                    workSpec.scheduleRequestedAt = query.getLong(i27);
                    int i29 = columnIndexOrThrow24;
                    if (query.getInt(i29) != 0) {
                        i = i20;
                        z = true;
                    } else {
                        i = i20;
                        z = false;
                    }
                    workSpec.expedited = z;
                    int i30 = columnIndexOrThrow25;
                    int i31 = i30;
                    workSpec.outOfQuotaPolicy = WorkTypeConverters.intToOutOfQuotaPolicy(query.getInt(i30));
                    workSpec.constraints = constraints2;
                    arrayList.add(workSpec);
                    i2 = i9;
                    columnIndexOrThrow16 = i21;
                    columnIndexOrThrow18 = i23;
                    columnIndexOrThrow20 = i;
                    columnIndexOrThrow = i4;
                    columnIndexOrThrow5 = i3;
                    columnIndexOrThrow6 = i5;
                    columnIndexOrThrow7 = i6;
                    columnIndexOrThrow12 = i7;
                    columnIndexOrThrow14 = i8;
                    columnIndexOrThrow3 = i10;
                    columnIndexOrThrow25 = i31;
                    columnIndexOrThrow24 = i29;
                    columnIndexOrThrow21 = i26;
                    columnIndexOrThrow2 = i12;
                    columnIndexOrThrow13 = i15;
                    columnIndexOrThrow15 = i18;
                    columnIndexOrThrow19 = i25;
                    columnIndexOrThrow22 = i28;
                    columnIndexOrThrow17 = i19;
                    columnIndexOrThrow23 = i27;
                }
                query.close();
                roomSQLiteQuery.release();
                return arrayList;
            } catch (Throwable th3) {
                th = th3;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            Object obj2 = "SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE period_start_time >= ? AND state IN (2, 3, 5) ORDER BY period_start_time DESC";
            roomSQLiteQuery = acquire;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public List<WorkSpec> getRunningWork() {
        RoomSQLiteQuery roomSQLiteQuery;
        int columnIndexOrThrow;
        int i;
        boolean z;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE state=1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "required_network_type");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "requires_charging");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "requires_device_idle");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "requires_battery_not_low");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "requires_storage_not_low");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "trigger_content_update_delay");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "trigger_max_content_delay");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "content_uri_triggers");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "state");
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "worker_class_name");
            int columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "input_merger_class_name");
            int columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "input");
            Object obj = "SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE state=1";
            try {
                columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "output");
                roomSQLiteQuery = acquire;
            } catch (Throwable th) {
                th = th;
                roomSQLiteQuery = acquire;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
            try {
                int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "initial_delay");
                int columnIndexOrThrow16 = CursorUtil.getColumnIndexOrThrow(query, "interval_duration");
                int columnIndexOrThrow17 = CursorUtil.getColumnIndexOrThrow(query, "flex_duration");
                int columnIndexOrThrow18 = CursorUtil.getColumnIndexOrThrow(query, "run_attempt_count");
                int columnIndexOrThrow19 = CursorUtil.getColumnIndexOrThrow(query, "backoff_policy");
                int columnIndexOrThrow20 = CursorUtil.getColumnIndexOrThrow(query, "backoff_delay_duration");
                int columnIndexOrThrow21 = CursorUtil.getColumnIndexOrThrow(query, "period_start_time");
                int columnIndexOrThrow22 = CursorUtil.getColumnIndexOrThrow(query, "minimum_retention_duration");
                int columnIndexOrThrow23 = CursorUtil.getColumnIndexOrThrow(query, "schedule_requested_at");
                int columnIndexOrThrow24 = CursorUtil.getColumnIndexOrThrow(query, "run_in_foreground");
                int columnIndexOrThrow25 = CursorUtil.getColumnIndexOrThrow(query, "out_of_quota_policy");
                int i2 = columnIndexOrThrow;
                ArrayList arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    String string = query.getString(columnIndexOrThrow10);
                    String string2 = query.getString(columnIndexOrThrow12);
                    Constraints constraints = new Constraints();
                    NetworkType intToNetworkType = WorkTypeConverters.intToNetworkType(query.getInt(columnIndexOrThrow2));
                    int i3 = columnIndexOrThrow2;
                    Constraints constraints2 = constraints;
                    int i4 = columnIndexOrThrow10;
                    constraints2.setRequiredNetworkType(intToNetworkType);
                    int i5 = columnIndexOrThrow3;
                    boolean z2 = query.getInt(columnIndexOrThrow3) != 0;
                    constraints2.setRequiresCharging(z2);
                    boolean z3 = z2;
                    boolean z4 = query.getInt(columnIndexOrThrow4) != 0;
                    constraints2.setRequiresDeviceIdle(z4);
                    boolean z5 = z4;
                    boolean z6 = query.getInt(columnIndexOrThrow5) != 0;
                    constraints2.setRequiresBatteryNotLow(z6);
                    boolean z7 = z6;
                    boolean z8 = query.getInt(columnIndexOrThrow6) != 0;
                    constraints2.setRequiresStorageNotLow(z8);
                    boolean z9 = z8;
                    int i6 = columnIndexOrThrow4;
                    long j = query.getLong(columnIndexOrThrow7);
                    constraints2.setTriggerContentUpdateDelay(j);
                    long j2 = j;
                    long j3 = query.getLong(columnIndexOrThrow8);
                    constraints2.setTriggerMaxContentDelay(j3);
                    long j4 = j3;
                    ContentUriTriggers byteArrayToContentUriTriggers = WorkTypeConverters.byteArrayToContentUriTriggers(query.getBlob(columnIndexOrThrow9));
                    constraints2.setContentUriTriggers(byteArrayToContentUriTriggers);
                    ContentUriTriggers contentUriTriggers = byteArrayToContentUriTriggers;
                    WorkSpec workSpec = new WorkSpec(string, string2);
                    String str = string;
                    workSpec.state = WorkTypeConverters.intToState(query.getInt(columnIndexOrThrow11));
                    workSpec.inputMergerClassName = query.getString(columnIndexOrThrow13);
                    byte[] blob = query.getBlob(columnIndexOrThrow14);
                    int i7 = columnIndexOrThrow14;
                    workSpec.input = Data.fromByteArray(blob);
                    int i8 = i2;
                    int i9 = i8;
                    workSpec.output = Data.fromByteArray(query.getBlob(i8));
                    byte[] bArr = blob;
                    int i10 = columnIndexOrThrow15;
                    int i11 = columnIndexOrThrow13;
                    workSpec.initialDelay = query.getLong(i10);
                    int i12 = columnIndexOrThrow5;
                    int i13 = columnIndexOrThrow16;
                    int i14 = columnIndexOrThrow6;
                    workSpec.intervalDuration = query.getLong(i13);
                    int i15 = i13;
                    int i16 = columnIndexOrThrow17;
                    int i17 = i12;
                    workSpec.flexDuration = query.getLong(i16);
                    int i18 = columnIndexOrThrow18;
                    workSpec.runAttemptCount = query.getInt(i18);
                    int i19 = columnIndexOrThrow19;
                    int i20 = i10;
                    workSpec.backoffPolicy = WorkTypeConverters.intToBackoffPolicy(query.getInt(i19));
                    int i21 = i19;
                    int i22 = columnIndexOrThrow20;
                    int i23 = i18;
                    workSpec.backoffDelayDuration = query.getLong(i22);
                    int i24 = i16;
                    int i25 = columnIndexOrThrow21;
                    int i26 = i15;
                    workSpec.periodStartTime = query.getLong(i25);
                    int i27 = i25;
                    int i28 = columnIndexOrThrow22;
                    int i29 = i24;
                    workSpec.minimumRetentionDuration = query.getLong(i28);
                    int i30 = i28;
                    int i31 = columnIndexOrThrow23;
                    int i32 = i27;
                    workSpec.scheduleRequestedAt = query.getLong(i31);
                    int i33 = columnIndexOrThrow24;
                    if (query.getInt(i33) != 0) {
                        i = i22;
                        z = true;
                    } else {
                        i = i22;
                        z = false;
                    }
                    workSpec.expedited = z;
                    int i34 = columnIndexOrThrow25;
                    int i35 = i34;
                    workSpec.outOfQuotaPolicy = WorkTypeConverters.intToOutOfQuotaPolicy(query.getInt(i34));
                    workSpec.constraints = constraints2;
                    arrayList.add(workSpec);
                    columnIndexOrThrow6 = i14;
                    columnIndexOrThrow18 = i23;
                    columnIndexOrThrow16 = i26;
                    columnIndexOrThrow21 = i32;
                    columnIndexOrThrow20 = i;
                    columnIndexOrThrow10 = i4;
                    columnIndexOrThrow2 = i3;
                    columnIndexOrThrow25 = i35;
                    columnIndexOrThrow3 = i5;
                    columnIndexOrThrow4 = i6;
                    columnIndexOrThrow14 = i7;
                    i2 = i9;
                    columnIndexOrThrow23 = i31;
                    columnIndexOrThrow24 = i33;
                    columnIndexOrThrow13 = i11;
                    columnIndexOrThrow5 = i17;
                    columnIndexOrThrow15 = i20;
                    columnIndexOrThrow17 = i29;
                    columnIndexOrThrow19 = i21;
                    columnIndexOrThrow22 = i30;
                }
                query.close();
                roomSQLiteQuery.release();
                return arrayList;
            } catch (Throwable th2) {
                th = th2;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            Object obj2 = "SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE state=1";
            roomSQLiteQuery = acquire;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public LiveData<Long> getScheduleRequestedAtLiveData(String id) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT schedule_requested_at FROM workspec WHERE id=?", 1);
        if (id == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, id);
        }
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"workspec"}, false, new Callable<Long>() {
            public Long call() throws Exception {
                Cursor query = DBUtil.query(WorkSpecDao_Impl.this.__db, acquire, false, (CancellationSignal) null);
                try {
                    return query.moveToFirst() ? query.isNull(0) ? null : Long.valueOf(query.getLong(0)) : null;
                } finally {
                    query.close();
                }
            }

            /* access modifiers changed from: protected */
            public void finalize() {
                acquire.release();
            }
        });
    }

    public List<WorkSpec> getScheduledWork() {
        RoomSQLiteQuery roomSQLiteQuery;
        int columnIndexOrThrow;
        int i;
        boolean z;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE state=0 AND schedule_requested_at<>-1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "required_network_type");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "requires_charging");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "requires_device_idle");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "requires_battery_not_low");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "requires_storage_not_low");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "trigger_content_update_delay");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "trigger_max_content_delay");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "content_uri_triggers");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "state");
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "worker_class_name");
            int columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "input_merger_class_name");
            int columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "input");
            Object obj = "SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE state=0 AND schedule_requested_at<>-1";
            try {
                columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "output");
                roomSQLiteQuery = acquire;
            } catch (Throwable th) {
                th = th;
                roomSQLiteQuery = acquire;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
            try {
                int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "initial_delay");
                int columnIndexOrThrow16 = CursorUtil.getColumnIndexOrThrow(query, "interval_duration");
                int columnIndexOrThrow17 = CursorUtil.getColumnIndexOrThrow(query, "flex_duration");
                int columnIndexOrThrow18 = CursorUtil.getColumnIndexOrThrow(query, "run_attempt_count");
                int columnIndexOrThrow19 = CursorUtil.getColumnIndexOrThrow(query, "backoff_policy");
                int columnIndexOrThrow20 = CursorUtil.getColumnIndexOrThrow(query, "backoff_delay_duration");
                int columnIndexOrThrow21 = CursorUtil.getColumnIndexOrThrow(query, "period_start_time");
                int columnIndexOrThrow22 = CursorUtil.getColumnIndexOrThrow(query, "minimum_retention_duration");
                int columnIndexOrThrow23 = CursorUtil.getColumnIndexOrThrow(query, "schedule_requested_at");
                int columnIndexOrThrow24 = CursorUtil.getColumnIndexOrThrow(query, "run_in_foreground");
                int columnIndexOrThrow25 = CursorUtil.getColumnIndexOrThrow(query, "out_of_quota_policy");
                int i2 = columnIndexOrThrow;
                ArrayList arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    String string = query.getString(columnIndexOrThrow10);
                    String string2 = query.getString(columnIndexOrThrow12);
                    Constraints constraints = new Constraints();
                    NetworkType intToNetworkType = WorkTypeConverters.intToNetworkType(query.getInt(columnIndexOrThrow2));
                    int i3 = columnIndexOrThrow2;
                    Constraints constraints2 = constraints;
                    int i4 = columnIndexOrThrow10;
                    constraints2.setRequiredNetworkType(intToNetworkType);
                    int i5 = columnIndexOrThrow3;
                    boolean z2 = query.getInt(columnIndexOrThrow3) != 0;
                    constraints2.setRequiresCharging(z2);
                    boolean z3 = z2;
                    boolean z4 = query.getInt(columnIndexOrThrow4) != 0;
                    constraints2.setRequiresDeviceIdle(z4);
                    boolean z5 = z4;
                    boolean z6 = query.getInt(columnIndexOrThrow5) != 0;
                    constraints2.setRequiresBatteryNotLow(z6);
                    boolean z7 = z6;
                    boolean z8 = query.getInt(columnIndexOrThrow6) != 0;
                    constraints2.setRequiresStorageNotLow(z8);
                    boolean z9 = z8;
                    int i6 = columnIndexOrThrow4;
                    long j = query.getLong(columnIndexOrThrow7);
                    constraints2.setTriggerContentUpdateDelay(j);
                    long j2 = j;
                    long j3 = query.getLong(columnIndexOrThrow8);
                    constraints2.setTriggerMaxContentDelay(j3);
                    long j4 = j3;
                    ContentUriTriggers byteArrayToContentUriTriggers = WorkTypeConverters.byteArrayToContentUriTriggers(query.getBlob(columnIndexOrThrow9));
                    constraints2.setContentUriTriggers(byteArrayToContentUriTriggers);
                    ContentUriTriggers contentUriTriggers = byteArrayToContentUriTriggers;
                    WorkSpec workSpec = new WorkSpec(string, string2);
                    String str = string;
                    workSpec.state = WorkTypeConverters.intToState(query.getInt(columnIndexOrThrow11));
                    workSpec.inputMergerClassName = query.getString(columnIndexOrThrow13);
                    byte[] blob = query.getBlob(columnIndexOrThrow14);
                    int i7 = columnIndexOrThrow14;
                    workSpec.input = Data.fromByteArray(blob);
                    int i8 = i2;
                    int i9 = i8;
                    workSpec.output = Data.fromByteArray(query.getBlob(i8));
                    byte[] bArr = blob;
                    int i10 = columnIndexOrThrow15;
                    int i11 = columnIndexOrThrow13;
                    workSpec.initialDelay = query.getLong(i10);
                    int i12 = columnIndexOrThrow5;
                    int i13 = columnIndexOrThrow16;
                    int i14 = columnIndexOrThrow6;
                    workSpec.intervalDuration = query.getLong(i13);
                    int i15 = i13;
                    int i16 = columnIndexOrThrow17;
                    int i17 = i12;
                    workSpec.flexDuration = query.getLong(i16);
                    int i18 = columnIndexOrThrow18;
                    workSpec.runAttemptCount = query.getInt(i18);
                    int i19 = columnIndexOrThrow19;
                    int i20 = i10;
                    workSpec.backoffPolicy = WorkTypeConverters.intToBackoffPolicy(query.getInt(i19));
                    int i21 = i19;
                    int i22 = columnIndexOrThrow20;
                    int i23 = i18;
                    workSpec.backoffDelayDuration = query.getLong(i22);
                    int i24 = i16;
                    int i25 = columnIndexOrThrow21;
                    int i26 = i15;
                    workSpec.periodStartTime = query.getLong(i25);
                    int i27 = i25;
                    int i28 = columnIndexOrThrow22;
                    int i29 = i24;
                    workSpec.minimumRetentionDuration = query.getLong(i28);
                    int i30 = i28;
                    int i31 = columnIndexOrThrow23;
                    int i32 = i27;
                    workSpec.scheduleRequestedAt = query.getLong(i31);
                    int i33 = columnIndexOrThrow24;
                    if (query.getInt(i33) != 0) {
                        i = i22;
                        z = true;
                    } else {
                        i = i22;
                        z = false;
                    }
                    workSpec.expedited = z;
                    int i34 = columnIndexOrThrow25;
                    int i35 = i34;
                    workSpec.outOfQuotaPolicy = WorkTypeConverters.intToOutOfQuotaPolicy(query.getInt(i34));
                    workSpec.constraints = constraints2;
                    arrayList.add(workSpec);
                    columnIndexOrThrow6 = i14;
                    columnIndexOrThrow18 = i23;
                    columnIndexOrThrow16 = i26;
                    columnIndexOrThrow21 = i32;
                    columnIndexOrThrow20 = i;
                    columnIndexOrThrow10 = i4;
                    columnIndexOrThrow2 = i3;
                    columnIndexOrThrow25 = i35;
                    columnIndexOrThrow3 = i5;
                    columnIndexOrThrow4 = i6;
                    columnIndexOrThrow14 = i7;
                    i2 = i9;
                    columnIndexOrThrow23 = i31;
                    columnIndexOrThrow24 = i33;
                    columnIndexOrThrow13 = i11;
                    columnIndexOrThrow5 = i17;
                    columnIndexOrThrow15 = i20;
                    columnIndexOrThrow17 = i29;
                    columnIndexOrThrow19 = i21;
                    columnIndexOrThrow22 = i30;
                }
                query.close();
                roomSQLiteQuery.release();
                return arrayList;
            } catch (Throwable th2) {
                th = th2;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            Object obj2 = "SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE state=0 AND schedule_requested_at<>-1";
            roomSQLiteQuery = acquire;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public WorkInfo.State getState(String id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT state FROM workspec WHERE id=?", 1);
        if (id == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, id);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            return query.moveToFirst() ? WorkTypeConverters.intToState(query.getInt(0)) : null;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public List<String> getUnfinishedWorkWithName(String name) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM workspec WHERE state NOT IN (2, 3, 5) AND id IN (SELECT work_spec_id FROM workname WHERE name=?)", 1);
        if (name == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, name);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(query.getString(0));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public List<String> getUnfinishedWorkWithTag(String tag) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id FROM workspec WHERE state NOT IN (2, 3, 5) AND id IN (SELECT work_spec_id FROM worktag WHERE tag=?)", 1);
        if (tag == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, tag);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(query.getString(0));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public WorkSpec getWorkSpec(String id) {
        RoomSQLiteQuery roomSQLiteQuery;
        int columnIndexOrThrow;
        int columnIndexOrThrow2;
        WorkSpec workSpec;
        boolean z;
        String str = id;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE id=?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "required_network_type");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "requires_charging");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "requires_device_idle");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "requires_battery_not_low");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "requires_storage_not_low");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "trigger_content_update_delay");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "trigger_max_content_delay");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "content_uri_triggers");
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "state");
            int columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "worker_class_name");
            int columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "input_merger_class_name");
            Object obj = "SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE id=?";
            try {
                columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "input");
            } catch (Throwable th) {
                th = th;
                roomSQLiteQuery = acquire;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
            try {
                columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "output");
                roomSQLiteQuery = acquire;
            } catch (Throwable th2) {
                th = th2;
                roomSQLiteQuery = acquire;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
            try {
                int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "initial_delay");
                int columnIndexOrThrow16 = CursorUtil.getColumnIndexOrThrow(query, "interval_duration");
                int columnIndexOrThrow17 = CursorUtil.getColumnIndexOrThrow(query, "flex_duration");
                int columnIndexOrThrow18 = CursorUtil.getColumnIndexOrThrow(query, "run_attempt_count");
                int columnIndexOrThrow19 = CursorUtil.getColumnIndexOrThrow(query, "backoff_policy");
                int columnIndexOrThrow20 = CursorUtil.getColumnIndexOrThrow(query, "backoff_delay_duration");
                int columnIndexOrThrow21 = CursorUtil.getColumnIndexOrThrow(query, "period_start_time");
                int columnIndexOrThrow22 = CursorUtil.getColumnIndexOrThrow(query, "minimum_retention_duration");
                int columnIndexOrThrow23 = CursorUtil.getColumnIndexOrThrow(query, "schedule_requested_at");
                int columnIndexOrThrow24 = CursorUtil.getColumnIndexOrThrow(query, "run_in_foreground");
                int columnIndexOrThrow25 = CursorUtil.getColumnIndexOrThrow(query, "out_of_quota_policy");
                if (query.moveToFirst()) {
                    String string = query.getString(columnIndexOrThrow11);
                    String string2 = query.getString(columnIndexOrThrow13);
                    Constraints constraints = new Constraints();
                    NetworkType intToNetworkType = WorkTypeConverters.intToNetworkType(query.getInt(columnIndexOrThrow3));
                    int i = columnIndexOrThrow3;
                    Constraints constraints2 = constraints;
                    int i2 = columnIndexOrThrow13;
                    NetworkType networkType = intToNetworkType;
                    constraints2.setRequiredNetworkType(networkType);
                    NetworkType networkType2 = networkType;
                    boolean z2 = query.getInt(columnIndexOrThrow4) != 0;
                    constraints2.setRequiresCharging(z2);
                    boolean z3 = z2;
                    boolean z4 = query.getInt(columnIndexOrThrow5) != 0;
                    constraints2.setRequiresDeviceIdle(z4);
                    boolean z5 = z4;
                    boolean z6 = query.getInt(columnIndexOrThrow6) != 0;
                    constraints2.setRequiresBatteryNotLow(z6);
                    boolean z7 = z6;
                    boolean z8 = query.getInt(columnIndexOrThrow7) != 0;
                    constraints2.setRequiresStorageNotLow(z8);
                    int i3 = columnIndexOrThrow4;
                    int i4 = columnIndexOrThrow5;
                    long j = query.getLong(columnIndexOrThrow8);
                    constraints2.setTriggerContentUpdateDelay(j);
                    long j2 = j;
                    long j3 = query.getLong(columnIndexOrThrow9);
                    constraints2.setTriggerMaxContentDelay(j3);
                    boolean z9 = z8;
                    ContentUriTriggers byteArrayToContentUriTriggers = WorkTypeConverters.byteArrayToContentUriTriggers(query.getBlob(columnIndexOrThrow10));
                    constraints2.setContentUriTriggers(byteArrayToContentUriTriggers);
                    ContentUriTriggers contentUriTriggers = byteArrayToContentUriTriggers;
                    int i5 = columnIndexOrThrow11;
                    String str2 = string;
                    long j4 = j3;
                    String str3 = string2;
                    long j5 = j4;
                    workSpec = new WorkSpec(str2, str3);
                    String str4 = str2;
                    workSpec.state = WorkTypeConverters.intToState(query.getInt(columnIndexOrThrow12));
                    workSpec.inputMergerClassName = query.getString(columnIndexOrThrow14);
                    byte[] blob = query.getBlob(columnIndexOrThrow);
                    int i6 = columnIndexOrThrow14;
                    workSpec.input = Data.fromByteArray(blob);
                    byte[] blob2 = query.getBlob(columnIndexOrThrow2);
                    int i7 = columnIndexOrThrow;
                    workSpec.output = Data.fromByteArray(blob2);
                    String str5 = str3;
                    int i8 = columnIndexOrThrow15;
                    int i9 = columnIndexOrThrow12;
                    workSpec.initialDelay = query.getLong(i8);
                    byte[] bArr = blob2;
                    int i10 = columnIndexOrThrow16;
                    int i11 = i8;
                    workSpec.intervalDuration = query.getLong(i10);
                    int i12 = columnIndexOrThrow17;
                    byte[] bArr2 = blob;
                    workSpec.flexDuration = query.getLong(i12);
                    int i13 = columnIndexOrThrow18;
                    workSpec.runAttemptCount = query.getInt(i13);
                    int i14 = columnIndexOrThrow19;
                    int i15 = i12;
                    workSpec.backoffPolicy = WorkTypeConverters.intToBackoffPolicy(query.getInt(i14));
                    int i16 = i14;
                    int i17 = columnIndexOrThrow20;
                    int i18 = i13;
                    workSpec.backoffDelayDuration = query.getLong(i17);
                    int i19 = i10;
                    int i20 = columnIndexOrThrow21;
                    byte[] bArr3 = bArr;
                    workSpec.periodStartTime = query.getLong(i20);
                    int i21 = i17;
                    int i22 = columnIndexOrThrow22;
                    int i23 = i20;
                    workSpec.minimumRetentionDuration = query.getLong(i22);
                    int i24 = columnIndexOrThrow23;
                    int i25 = i19;
                    workSpec.scheduleRequestedAt = query.getLong(i24);
                    int i26 = columnIndexOrThrow24;
                    if (query.getInt(i26) != 0) {
                        int i27 = i24;
                        z = true;
                    } else {
                        int i28 = i24;
                        z = false;
                    }
                    workSpec.expedited = z;
                    int i29 = i26;
                    workSpec.outOfQuotaPolicy = WorkTypeConverters.intToOutOfQuotaPolicy(query.getInt(columnIndexOrThrow25));
                    workSpec.constraints = constraints2;
                } else {
                    int i30 = columnIndexOrThrow3;
                    int i31 = columnIndexOrThrow13;
                    int i32 = columnIndexOrThrow14;
                    int i33 = columnIndexOrThrow;
                    int i34 = columnIndexOrThrow11;
                    int i35 = columnIndexOrThrow4;
                    int i36 = columnIndexOrThrow5;
                    int i37 = columnIndexOrThrow19;
                    int i38 = columnIndexOrThrow20;
                    int i39 = columnIndexOrThrow23;
                    int i40 = columnIndexOrThrow16;
                    int i41 = columnIndexOrThrow17;
                    int i42 = columnIndexOrThrow18;
                    int i43 = columnIndexOrThrow15;
                    int i44 = columnIndexOrThrow12;
                    int i45 = columnIndexOrThrow22;
                    int i46 = columnIndexOrThrow21;
                    workSpec = null;
                }
                query.close();
                roomSQLiteQuery.release();
                return workSpec;
            } catch (Throwable th3) {
                th = th3;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            Object obj2 = "SELECT `required_network_type`, `requires_charging`, `requires_device_idle`, `requires_battery_not_low`, `requires_storage_not_low`, `trigger_content_update_delay`, `trigger_max_content_delay`, `content_uri_triggers`, `WorkSpec`.`id` AS `id`, `WorkSpec`.`state` AS `state`, `WorkSpec`.`worker_class_name` AS `worker_class_name`, `WorkSpec`.`input_merger_class_name` AS `input_merger_class_name`, `WorkSpec`.`input` AS `input`, `WorkSpec`.`output` AS `output`, `WorkSpec`.`initial_delay` AS `initial_delay`, `WorkSpec`.`interval_duration` AS `interval_duration`, `WorkSpec`.`flex_duration` AS `flex_duration`, `WorkSpec`.`run_attempt_count` AS `run_attempt_count`, `WorkSpec`.`backoff_policy` AS `backoff_policy`, `WorkSpec`.`backoff_delay_duration` AS `backoff_delay_duration`, `WorkSpec`.`period_start_time` AS `period_start_time`, `WorkSpec`.`minimum_retention_duration` AS `minimum_retention_duration`, `WorkSpec`.`schedule_requested_at` AS `schedule_requested_at`, `WorkSpec`.`run_in_foreground` AS `run_in_foreground`, `WorkSpec`.`out_of_quota_policy` AS `out_of_quota_policy` FROM workspec WHERE id=?";
            roomSQLiteQuery = acquire;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public List<WorkSpec.IdAndState> getWorkSpecIdAndStatesForName(String name) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id, state FROM workspec WHERE id IN (SELECT work_spec_id FROM workname WHERE name=?)", 1);
        if (name == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, name);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "state");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                WorkSpec.IdAndState idAndState = new WorkSpec.IdAndState();
                idAndState.id = query.getString(columnIndexOrThrow);
                idAndState.state = WorkTypeConverters.intToState(query.getInt(columnIndexOrThrow2));
                arrayList.add(idAndState);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public WorkSpec[] getWorkSpecs(List<String> list) {
        RoomSQLiteQuery roomSQLiteQuery;
        int columnIndexOrThrow;
        int columnIndexOrThrow2;
        int columnIndexOrThrow3;
        int columnIndexOrThrow4;
        int columnIndexOrThrow5;
        int i;
        boolean z;
        StringBuilder newStringBuilder = StringUtil.newStringBuilder();
        newStringBuilder.append("SELECT ");
        newStringBuilder.append("*");
        newStringBuilder.append(" FROM workspec WHERE id IN (");
        int size = list.size();
        StringUtil.appendPlaceholders(newStringBuilder, size);
        newStringBuilder.append(")");
        String sb = newStringBuilder.toString();
        int i2 = size + 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(sb, i2);
        int i3 = 1;
        for (String next : list) {
            if (next == null) {
                acquire.bindNull(i3);
            } else {
                acquire.bindString(i3, next);
            }
            i3++;
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "required_network_type");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "requires_charging");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "requires_device_idle");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "requires_battery_not_low");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "requires_storage_not_low");
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "trigger_content_update_delay");
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "trigger_max_content_delay");
            int columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "content_uri_triggers");
            int columnIndexOrThrow14 = CursorUtil.getColumnIndexOrThrow(query, "id");
            StringBuilder sb2 = newStringBuilder;
            try {
                columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "state");
                int i4 = size;
            } catch (Throwable th) {
                th = th;
                int i5 = size;
                String str = sb;
                int i6 = i2;
                roomSQLiteQuery = acquire;
                int i7 = i3;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
            try {
                columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "worker_class_name");
                String str2 = sb;
                try {
                    columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "input_merger_class_name");
                    int i8 = i2;
                } catch (Throwable th2) {
                    th = th2;
                    int i9 = i2;
                    roomSQLiteQuery = acquire;
                    int i10 = i3;
                    query.close();
                    roomSQLiteQuery.release();
                    throw th;
                }
                try {
                    columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "input");
                    int i11 = i3;
                    try {
                        columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "output");
                        roomSQLiteQuery = acquire;
                    } catch (Throwable th3) {
                        th = th3;
                        roomSQLiteQuery = acquire;
                        query.close();
                        roomSQLiteQuery.release();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    roomSQLiteQuery = acquire;
                    int i12 = i3;
                    query.close();
                    roomSQLiteQuery.release();
                    throw th;
                }
            } catch (Throwable th5) {
                th = th5;
                String str3 = sb;
                int i13 = i2;
                roomSQLiteQuery = acquire;
                int i14 = i3;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
            try {
                int columnIndexOrThrow15 = CursorUtil.getColumnIndexOrThrow(query, "initial_delay");
                int columnIndexOrThrow16 = CursorUtil.getColumnIndexOrThrow(query, "interval_duration");
                int columnIndexOrThrow17 = CursorUtil.getColumnIndexOrThrow(query, "flex_duration");
                int columnIndexOrThrow18 = CursorUtil.getColumnIndexOrThrow(query, "run_attempt_count");
                int columnIndexOrThrow19 = CursorUtil.getColumnIndexOrThrow(query, "backoff_policy");
                int columnIndexOrThrow20 = CursorUtil.getColumnIndexOrThrow(query, "backoff_delay_duration");
                int columnIndexOrThrow21 = CursorUtil.getColumnIndexOrThrow(query, "period_start_time");
                int columnIndexOrThrow22 = CursorUtil.getColumnIndexOrThrow(query, "minimum_retention_duration");
                int columnIndexOrThrow23 = CursorUtil.getColumnIndexOrThrow(query, "schedule_requested_at");
                int columnIndexOrThrow24 = CursorUtil.getColumnIndexOrThrow(query, "run_in_foreground");
                int columnIndexOrThrow25 = CursorUtil.getColumnIndexOrThrow(query, "out_of_quota_policy");
                WorkSpec[] workSpecArr = new WorkSpec[query.getCount()];
                int i15 = 0;
                while (query.moveToNext()) {
                    String string = query.getString(columnIndexOrThrow14);
                    String string2 = query.getString(columnIndexOrThrow2);
                    Constraints constraints = new Constraints();
                    NetworkType intToNetworkType = WorkTypeConverters.intToNetworkType(query.getInt(columnIndexOrThrow6));
                    int i16 = columnIndexOrThrow6;
                    Constraints constraints2 = constraints;
                    int i17 = columnIndexOrThrow14;
                    NetworkType networkType = intToNetworkType;
                    constraints2.setRequiredNetworkType(networkType);
                    NetworkType networkType2 = networkType;
                    boolean z2 = query.getInt(columnIndexOrThrow7) != 0;
                    constraints2.setRequiresCharging(z2);
                    boolean z3 = z2;
                    boolean z4 = query.getInt(columnIndexOrThrow8) != 0;
                    constraints2.setRequiresDeviceIdle(z4);
                    boolean z5 = z4;
                    boolean z6 = query.getInt(columnIndexOrThrow9) != 0;
                    constraints2.setRequiresBatteryNotLow(z6);
                    boolean z7 = z6;
                    boolean z8 = query.getInt(columnIndexOrThrow10) != 0;
                    constraints2.setRequiresStorageNotLow(z8);
                    int i18 = columnIndexOrThrow7;
                    int i19 = columnIndexOrThrow8;
                    long j = query.getLong(columnIndexOrThrow11);
                    constraints2.setTriggerContentUpdateDelay(j);
                    long j2 = j;
                    constraints2.setTriggerMaxContentDelay(query.getLong(columnIndexOrThrow12));
                    boolean z9 = z8;
                    ContentUriTriggers byteArrayToContentUriTriggers = WorkTypeConverters.byteArrayToContentUriTriggers(query.getBlob(columnIndexOrThrow13));
                    constraints2.setContentUriTriggers(byteArrayToContentUriTriggers);
                    ContentUriTriggers contentUriTriggers = byteArrayToContentUriTriggers;
                    int i20 = columnIndexOrThrow2;
                    String str4 = string;
                    int i21 = columnIndexOrThrow13;
                    WorkSpec workSpec = new WorkSpec(str4, string2);
                    int i22 = columnIndexOrThrow;
                    workSpec.state = WorkTypeConverters.intToState(query.getInt(columnIndexOrThrow));
                    workSpec.inputMergerClassName = query.getString(columnIndexOrThrow3);
                    byte[] blob = query.getBlob(columnIndexOrThrow4);
                    String str5 = str4;
                    workSpec.input = Data.fromByteArray(blob);
                    byte[] blob2 = query.getBlob(columnIndexOrThrow5);
                    byte[] bArr = blob;
                    workSpec.output = Data.fromByteArray(blob2);
                    byte[] bArr2 = blob2;
                    int i23 = columnIndexOrThrow15;
                    int i24 = columnIndexOrThrow3;
                    workSpec.initialDelay = query.getLong(i23);
                    int i25 = columnIndexOrThrow16;
                    int i26 = columnIndexOrThrow4;
                    workSpec.intervalDuration = query.getLong(i25);
                    int i27 = i23;
                    int i28 = columnIndexOrThrow17;
                    int i29 = i25;
                    workSpec.flexDuration = query.getLong(i28);
                    int i30 = columnIndexOrThrow18;
                    workSpec.runAttemptCount = query.getInt(i30);
                    int i31 = columnIndexOrThrow19;
                    int i32 = i30;
                    workSpec.backoffPolicy = WorkTypeConverters.intToBackoffPolicy(query.getInt(i31));
                    int i33 = i31;
                    int i34 = columnIndexOrThrow20;
                    int i35 = i28;
                    workSpec.backoffDelayDuration = query.getLong(i34);
                    int i36 = columnIndexOrThrow21;
                    int i37 = i27;
                    workSpec.periodStartTime = query.getLong(i36);
                    int i38 = i34;
                    int i39 = columnIndexOrThrow22;
                    int i40 = i36;
                    workSpec.minimumRetentionDuration = query.getLong(i39);
                    int i41 = columnIndexOrThrow23;
                    int i42 = i39;
                    workSpec.scheduleRequestedAt = query.getLong(i41);
                    int i43 = columnIndexOrThrow24;
                    if (query.getInt(i43) != 0) {
                        i = i41;
                        z = true;
                    } else {
                        i = i41;
                        z = false;
                    }
                    workSpec.expedited = z;
                    int i44 = columnIndexOrThrow25;
                    int i45 = i44;
                    workSpec.outOfQuotaPolicy = WorkTypeConverters.intToOutOfQuotaPolicy(query.getInt(i44));
                    workSpec.constraints = constraints2;
                    workSpecArr[i15] = workSpec;
                    i15++;
                    columnIndexOrThrow3 = i24;
                    columnIndexOrThrow18 = i32;
                    columnIndexOrThrow15 = i37;
                    columnIndexOrThrow21 = i40;
                    columnIndexOrThrow22 = i42;
                    columnIndexOrThrow23 = i;
                    columnIndexOrThrow13 = i21;
                    columnIndexOrThrow14 = i17;
                    columnIndexOrThrow6 = i16;
                    columnIndexOrThrow25 = i45;
                    columnIndexOrThrow7 = i18;
                    columnIndexOrThrow8 = i19;
                    columnIndexOrThrow = i22;
                    columnIndexOrThrow19 = i33;
                    columnIndexOrThrow24 = i43;
                    columnIndexOrThrow2 = i20;
                    int i46 = i35;
                    columnIndexOrThrow20 = i38;
                    columnIndexOrThrow4 = i26;
                    columnIndexOrThrow16 = i29;
                    columnIndexOrThrow17 = i46;
                }
                query.close();
                roomSQLiteQuery.release();
                return workSpecArr;
            } catch (Throwable th6) {
                th = th6;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th7) {
            th = th7;
            StringBuilder sb3 = newStringBuilder;
            int i47 = size;
            String str6 = sb;
            int i48 = i2;
            roomSQLiteQuery = acquire;
            int i49 = i3;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public WorkSpec.WorkInfoPojo getWorkStatusPojoForId(String id) {
        Cursor query;
        WorkSpec.WorkInfoPojo workInfoPojo;
        String str = id;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id, state, output, run_attempt_count FROM workspec WHERE id=?", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            query = DBUtil.query(this.__db, acquire, true, (CancellationSignal) null);
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "state");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "output");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "run_attempt_count");
            ArrayMap arrayMap = new ArrayMap();
            ArrayMap arrayMap2 = new ArrayMap();
            while (query.moveToNext()) {
                if (!query.isNull(columnIndexOrThrow)) {
                    String string = query.getString(columnIndexOrThrow);
                    if (((ArrayList) arrayMap.get(string)) == null) {
                        arrayMap.put(string, new ArrayList());
                    }
                }
                if (!query.isNull(columnIndexOrThrow)) {
                    String string2 = query.getString(columnIndexOrThrow);
                    if (((ArrayList) arrayMap2.get(string2)) == null) {
                        arrayMap2.put(string2, new ArrayList());
                    }
                }
            }
            query.moveToPosition(-1);
            __fetchRelationshipWorkTagAsjavaLangString(arrayMap);
            __fetchRelationshipWorkProgressAsandroidxWorkData(arrayMap2);
            if (query.moveToFirst()) {
                ArrayList arrayList = null;
                if (!query.isNull(columnIndexOrThrow)) {
                    arrayList = (ArrayList) arrayMap.get(query.getString(columnIndexOrThrow));
                }
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                ArrayList arrayList2 = null;
                if (!query.isNull(columnIndexOrThrow)) {
                    arrayList2 = (ArrayList) arrayMap2.get(query.getString(columnIndexOrThrow));
                }
                if (arrayList2 == null) {
                    arrayList2 = new ArrayList();
                }
                workInfoPojo = new WorkSpec.WorkInfoPojo();
                workInfoPojo.id = query.getString(columnIndexOrThrow);
                int i = columnIndexOrThrow;
                workInfoPojo.state = WorkTypeConverters.intToState(query.getInt(columnIndexOrThrow2));
                workInfoPojo.output = Data.fromByteArray(query.getBlob(columnIndexOrThrow3));
                workInfoPojo.runAttemptCount = query.getInt(columnIndexOrThrow4);
                workInfoPojo.tags = arrayList;
                workInfoPojo.progress = arrayList2;
            } else {
                int i2 = columnIndexOrThrow;
                workInfoPojo = null;
            }
            this.__db.setTransactionSuccessful();
            query.close();
            acquire.release();
            this.__db.endTransaction();
            return workInfoPojo;
        } catch (Throwable th) {
            this.__db.endTransaction();
            throw th;
        }
    }

    public List<WorkSpec.WorkInfoPojo> getWorkStatusPojoForIds(List<String> list) {
        StringBuilder sb;
        WorkSpec.WorkInfoPojo workInfoPojo;
        int i;
        StringBuilder newStringBuilder = StringUtil.newStringBuilder();
        newStringBuilder.append("SELECT id, state, output, run_attempt_count FROM workspec WHERE id IN (");
        int size = list.size();
        StringUtil.appendPlaceholders(newStringBuilder, size);
        newStringBuilder.append(")");
        String sb2 = newStringBuilder.toString();
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(sb2, size + 0);
        int i2 = 1;
        for (String next : list) {
            if (next == null) {
                acquire.bindNull(i2);
            } else {
                acquire.bindString(i2, next);
            }
            i2++;
        }
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            Cursor query = DBUtil.query(this.__db, acquire, true, (CancellationSignal) null);
            try {
                int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
                int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "state");
                int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "output");
                int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "run_attempt_count");
                ArrayMap arrayMap = new ArrayMap();
                ArrayMap arrayMap2 = new ArrayMap();
                while (query.moveToNext()) {
                    try {
                        if (!query.isNull(columnIndexOrThrow)) {
                            String string = query.getString(columnIndexOrThrow);
                            if (((ArrayList) arrayMap.get(string)) == null) {
                                arrayMap.put(string, new ArrayList());
                            }
                        }
                        if (!query.isNull(columnIndexOrThrow)) {
                            String string2 = query.getString(columnIndexOrThrow);
                            if (((ArrayList) arrayMap2.get(string2)) == null) {
                                arrayMap2.put(string2, new ArrayList());
                            }
                        }
                    } catch (Throwable th) {
                        th = th;
                        StringBuilder sb3 = newStringBuilder;
                        int i3 = size;
                        String str = sb2;
                        query.close();
                        acquire.release();
                        throw th;
                    }
                }
                query.moveToPosition(-1);
                __fetchRelationshipWorkTagAsjavaLangString(arrayMap);
                __fetchRelationshipWorkProgressAsandroidxWorkData(arrayMap2);
                ArrayList arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    ArrayList arrayList2 = null;
                    if (!query.isNull(columnIndexOrThrow)) {
                        try {
                            sb = newStringBuilder;
                            arrayList2 = (ArrayList) arrayMap.get(query.getString(columnIndexOrThrow));
                        } catch (Throwable th2) {
                            th = th2;
                            int i4 = size;
                            String str2 = sb2;
                            query.close();
                            acquire.release();
                            throw th;
                        }
                    } else {
                        sb = newStringBuilder;
                    }
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList();
                    }
                    ArrayList arrayList3 = null;
                    try {
                        if (!query.isNull(columnIndexOrThrow)) {
                            arrayList3 = (ArrayList) arrayMap2.get(query.getString(columnIndexOrThrow));
                        }
                        if (arrayList3 == null) {
                            arrayList3 = new ArrayList();
                        }
                        workInfoPojo = new WorkSpec.WorkInfoPojo();
                        i = size;
                    } catch (Throwable th3) {
                        th = th3;
                        int i5 = size;
                        String str3 = sb2;
                        query.close();
                        acquire.release();
                        throw th;
                    }
                    try {
                        String string3 = query.getString(columnIndexOrThrow);
                        int i6 = columnIndexOrThrow;
                        WorkSpec.WorkInfoPojo workInfoPojo2 = workInfoPojo;
                        workInfoPojo2.id = string3;
                        int i7 = query.getInt(columnIndexOrThrow2);
                        String str4 = sb2;
                        workInfoPojo2.state = WorkTypeConverters.intToState(i7);
                        int i8 = i7;
                        workInfoPojo2.output = Data.fromByteArray(query.getBlob(columnIndexOrThrow3));
                        workInfoPojo2.runAttemptCount = query.getInt(columnIndexOrThrow4);
                        workInfoPojo2.tags = arrayList2;
                        workInfoPojo2.progress = arrayList3;
                        arrayList.add(workInfoPojo2);
                        newStringBuilder = sb;
                        size = i;
                        sb2 = str4;
                        columnIndexOrThrow = i6;
                    } catch (Throwable th4) {
                        th = th4;
                        query.close();
                        acquire.release();
                        throw th;
                    }
                }
                int i9 = columnIndexOrThrow;
                StringBuilder sb4 = newStringBuilder;
                int i10 = size;
                String str5 = sb2;
                this.__db.setTransactionSuccessful();
                try {
                    query.close();
                    acquire.release();
                    this.__db.endTransaction();
                    return arrayList;
                } catch (Throwable th5) {
                    th = th5;
                    this.__db.endTransaction();
                    throw th;
                }
            } catch (Throwable th6) {
                th = th6;
                StringBuilder sb5 = newStringBuilder;
                int i11 = size;
                String str6 = sb2;
                query.close();
                acquire.release();
                throw th;
            }
        } catch (Throwable th7) {
            th = th7;
            StringBuilder sb6 = newStringBuilder;
            int i12 = size;
            String str7 = sb2;
            this.__db.endTransaction();
            throw th;
        }
    }

    public List<WorkSpec.WorkInfoPojo> getWorkStatusPojoForName(String name) {
        Cursor query;
        String str = name;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id, state, output, run_attempt_count FROM workspec WHERE id IN (SELECT work_spec_id FROM workname WHERE name=?)", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            query = DBUtil.query(this.__db, acquire, true, (CancellationSignal) null);
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "state");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "output");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "run_attempt_count");
            ArrayMap arrayMap = new ArrayMap();
            ArrayMap arrayMap2 = new ArrayMap();
            while (query.moveToNext()) {
                if (!query.isNull(columnIndexOrThrow)) {
                    String string = query.getString(columnIndexOrThrow);
                    if (((ArrayList) arrayMap.get(string)) == null) {
                        arrayMap.put(string, new ArrayList());
                    }
                }
                if (!query.isNull(columnIndexOrThrow)) {
                    String string2 = query.getString(columnIndexOrThrow);
                    if (((ArrayList) arrayMap2.get(string2)) == null) {
                        arrayMap2.put(string2, new ArrayList());
                    }
                }
            }
            query.moveToPosition(-1);
            __fetchRelationshipWorkTagAsjavaLangString(arrayMap);
            __fetchRelationshipWorkProgressAsandroidxWorkData(arrayMap2);
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                ArrayList arrayList2 = null;
                if (!query.isNull(columnIndexOrThrow)) {
                    arrayList2 = (ArrayList) arrayMap.get(query.getString(columnIndexOrThrow));
                }
                if (arrayList2 == null) {
                    arrayList2 = new ArrayList();
                }
                ArrayList arrayList3 = null;
                if (!query.isNull(columnIndexOrThrow)) {
                    arrayList3 = (ArrayList) arrayMap2.get(query.getString(columnIndexOrThrow));
                }
                if (arrayList3 == null) {
                    arrayList3 = new ArrayList();
                }
                WorkSpec.WorkInfoPojo workInfoPojo = new WorkSpec.WorkInfoPojo();
                workInfoPojo.id = query.getString(columnIndexOrThrow);
                int i = query.getInt(columnIndexOrThrow2);
                workInfoPojo.state = WorkTypeConverters.intToState(i);
                int i2 = i;
                workInfoPojo.output = Data.fromByteArray(query.getBlob(columnIndexOrThrow3));
                workInfoPojo.runAttemptCount = query.getInt(columnIndexOrThrow4);
                workInfoPojo.tags = arrayList2;
                workInfoPojo.progress = arrayList3;
                arrayList.add(workInfoPojo);
                String str2 = name;
                columnIndexOrThrow = columnIndexOrThrow;
            }
            int i3 = columnIndexOrThrow;
            this.__db.setTransactionSuccessful();
            query.close();
            acquire.release();
            this.__db.endTransaction();
            return arrayList;
        } catch (Throwable th) {
            this.__db.endTransaction();
            throw th;
        }
    }

    public List<WorkSpec.WorkInfoPojo> getWorkStatusPojoForTag(String tag) {
        Cursor query;
        String str = tag;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id, state, output, run_attempt_count FROM workspec WHERE id IN (SELECT work_spec_id FROM worktag WHERE tag=?)", 1);
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            query = DBUtil.query(this.__db, acquire, true, (CancellationSignal) null);
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "state");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "output");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "run_attempt_count");
            ArrayMap arrayMap = new ArrayMap();
            ArrayMap arrayMap2 = new ArrayMap();
            while (query.moveToNext()) {
                if (!query.isNull(columnIndexOrThrow)) {
                    String string = query.getString(columnIndexOrThrow);
                    if (((ArrayList) arrayMap.get(string)) == null) {
                        arrayMap.put(string, new ArrayList());
                    }
                }
                if (!query.isNull(columnIndexOrThrow)) {
                    String string2 = query.getString(columnIndexOrThrow);
                    if (((ArrayList) arrayMap2.get(string2)) == null) {
                        arrayMap2.put(string2, new ArrayList());
                    }
                }
            }
            query.moveToPosition(-1);
            __fetchRelationshipWorkTagAsjavaLangString(arrayMap);
            __fetchRelationshipWorkProgressAsandroidxWorkData(arrayMap2);
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                ArrayList arrayList2 = null;
                if (!query.isNull(columnIndexOrThrow)) {
                    arrayList2 = (ArrayList) arrayMap.get(query.getString(columnIndexOrThrow));
                }
                if (arrayList2 == null) {
                    arrayList2 = new ArrayList();
                }
                ArrayList arrayList3 = null;
                if (!query.isNull(columnIndexOrThrow)) {
                    arrayList3 = (ArrayList) arrayMap2.get(query.getString(columnIndexOrThrow));
                }
                if (arrayList3 == null) {
                    arrayList3 = new ArrayList();
                }
                WorkSpec.WorkInfoPojo workInfoPojo = new WorkSpec.WorkInfoPojo();
                workInfoPojo.id = query.getString(columnIndexOrThrow);
                int i = query.getInt(columnIndexOrThrow2);
                workInfoPojo.state = WorkTypeConverters.intToState(i);
                int i2 = i;
                workInfoPojo.output = Data.fromByteArray(query.getBlob(columnIndexOrThrow3));
                workInfoPojo.runAttemptCount = query.getInt(columnIndexOrThrow4);
                workInfoPojo.tags = arrayList2;
                workInfoPojo.progress = arrayList3;
                arrayList.add(workInfoPojo);
                String str2 = tag;
                columnIndexOrThrow = columnIndexOrThrow;
            }
            int i3 = columnIndexOrThrow;
            this.__db.setTransactionSuccessful();
            query.close();
            acquire.release();
            this.__db.endTransaction();
            return arrayList;
        } catch (Throwable th) {
            this.__db.endTransaction();
            throw th;
        }
    }

    public LiveData<List<WorkSpec.WorkInfoPojo>> getWorkStatusPojoLiveDataForIds(List<String> list) {
        StringBuilder newStringBuilder = StringUtil.newStringBuilder();
        newStringBuilder.append("SELECT id, state, output, run_attempt_count FROM workspec WHERE id IN (");
        int size = list.size();
        StringUtil.appendPlaceholders(newStringBuilder, size);
        newStringBuilder.append(")");
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(newStringBuilder.toString(), size + 0);
        int i = 1;
        for (String next : list) {
            if (next == null) {
                acquire.bindNull(i);
            } else {
                acquire.bindString(i, next);
            }
            i++;
        }
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"WorkTag", "WorkProgress", "workspec"}, true, new Callable<List<WorkSpec.WorkInfoPojo>>() {
            public List<WorkSpec.WorkInfoPojo> call() throws Exception {
                Cursor query;
                WorkSpecDao_Impl.this.__db.beginTransaction();
                try {
                    query = DBUtil.query(WorkSpecDao_Impl.this.__db, acquire, true, (CancellationSignal) null);
                    int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
                    int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "state");
                    int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "output");
                    int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "run_attempt_count");
                    ArrayMap arrayMap = new ArrayMap();
                    ArrayMap arrayMap2 = new ArrayMap();
                    while (query.moveToNext()) {
                        if (!query.isNull(columnIndexOrThrow)) {
                            String string = query.getString(columnIndexOrThrow);
                            if (((ArrayList) arrayMap.get(string)) == null) {
                                arrayMap.put(string, new ArrayList());
                            }
                        }
                        if (!query.isNull(columnIndexOrThrow)) {
                            String string2 = query.getString(columnIndexOrThrow);
                            if (((ArrayList) arrayMap2.get(string2)) == null) {
                                arrayMap2.put(string2, new ArrayList());
                            }
                        }
                    }
                    query.moveToPosition(-1);
                    WorkSpecDao_Impl.this.__fetchRelationshipWorkTagAsjavaLangString(arrayMap);
                    WorkSpecDao_Impl.this.__fetchRelationshipWorkProgressAsandroidxWorkData(arrayMap2);
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        ArrayList arrayList2 = null;
                        if (!query.isNull(columnIndexOrThrow)) {
                            arrayList2 = (ArrayList) arrayMap.get(query.getString(columnIndexOrThrow));
                        }
                        if (arrayList2 == null) {
                            arrayList2 = new ArrayList();
                        }
                        ArrayList arrayList3 = null;
                        if (!query.isNull(columnIndexOrThrow)) {
                            arrayList3 = (ArrayList) arrayMap2.get(query.getString(columnIndexOrThrow));
                        }
                        if (arrayList3 == null) {
                            arrayList3 = new ArrayList();
                        }
                        WorkSpec.WorkInfoPojo workInfoPojo = new WorkSpec.WorkInfoPojo();
                        workInfoPojo.id = query.getString(columnIndexOrThrow);
                        workInfoPojo.state = WorkTypeConverters.intToState(query.getInt(columnIndexOrThrow2));
                        workInfoPojo.output = Data.fromByteArray(query.getBlob(columnIndexOrThrow3));
                        workInfoPojo.runAttemptCount = query.getInt(columnIndexOrThrow4);
                        workInfoPojo.tags = arrayList2;
                        workInfoPojo.progress = arrayList3;
                        arrayList.add(workInfoPojo);
                    }
                    WorkSpecDao_Impl.this.__db.setTransactionSuccessful();
                    query.close();
                    WorkSpecDao_Impl.this.__db.endTransaction();
                    return arrayList;
                } catch (Throwable th) {
                    WorkSpecDao_Impl.this.__db.endTransaction();
                    throw th;
                }
            }

            /* access modifiers changed from: protected */
            public void finalize() {
                acquire.release();
            }
        });
    }

    public LiveData<List<WorkSpec.WorkInfoPojo>> getWorkStatusPojoLiveDataForName(String name) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id, state, output, run_attempt_count FROM workspec WHERE id IN (SELECT work_spec_id FROM workname WHERE name=?)", 1);
        if (name == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, name);
        }
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"WorkTag", "WorkProgress", "workspec", "workname"}, true, new Callable<List<WorkSpec.WorkInfoPojo>>() {
            public List<WorkSpec.WorkInfoPojo> call() throws Exception {
                Cursor query;
                WorkSpecDao_Impl.this.__db.beginTransaction();
                try {
                    query = DBUtil.query(WorkSpecDao_Impl.this.__db, acquire, true, (CancellationSignal) null);
                    int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
                    int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "state");
                    int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "output");
                    int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "run_attempt_count");
                    ArrayMap arrayMap = new ArrayMap();
                    ArrayMap arrayMap2 = new ArrayMap();
                    while (query.moveToNext()) {
                        if (!query.isNull(columnIndexOrThrow)) {
                            String string = query.getString(columnIndexOrThrow);
                            if (((ArrayList) arrayMap.get(string)) == null) {
                                arrayMap.put(string, new ArrayList());
                            }
                        }
                        if (!query.isNull(columnIndexOrThrow)) {
                            String string2 = query.getString(columnIndexOrThrow);
                            if (((ArrayList) arrayMap2.get(string2)) == null) {
                                arrayMap2.put(string2, new ArrayList());
                            }
                        }
                    }
                    query.moveToPosition(-1);
                    WorkSpecDao_Impl.this.__fetchRelationshipWorkTagAsjavaLangString(arrayMap);
                    WorkSpecDao_Impl.this.__fetchRelationshipWorkProgressAsandroidxWorkData(arrayMap2);
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        ArrayList arrayList2 = null;
                        if (!query.isNull(columnIndexOrThrow)) {
                            arrayList2 = (ArrayList) arrayMap.get(query.getString(columnIndexOrThrow));
                        }
                        if (arrayList2 == null) {
                            arrayList2 = new ArrayList();
                        }
                        ArrayList arrayList3 = null;
                        if (!query.isNull(columnIndexOrThrow)) {
                            arrayList3 = (ArrayList) arrayMap2.get(query.getString(columnIndexOrThrow));
                        }
                        if (arrayList3 == null) {
                            arrayList3 = new ArrayList();
                        }
                        WorkSpec.WorkInfoPojo workInfoPojo = new WorkSpec.WorkInfoPojo();
                        workInfoPojo.id = query.getString(columnIndexOrThrow);
                        workInfoPojo.state = WorkTypeConverters.intToState(query.getInt(columnIndexOrThrow2));
                        workInfoPojo.output = Data.fromByteArray(query.getBlob(columnIndexOrThrow3));
                        workInfoPojo.runAttemptCount = query.getInt(columnIndexOrThrow4);
                        workInfoPojo.tags = arrayList2;
                        workInfoPojo.progress = arrayList3;
                        arrayList.add(workInfoPojo);
                    }
                    WorkSpecDao_Impl.this.__db.setTransactionSuccessful();
                    query.close();
                    WorkSpecDao_Impl.this.__db.endTransaction();
                    return arrayList;
                } catch (Throwable th) {
                    WorkSpecDao_Impl.this.__db.endTransaction();
                    throw th;
                }
            }

            /* access modifiers changed from: protected */
            public void finalize() {
                acquire.release();
            }
        });
    }

    public LiveData<List<WorkSpec.WorkInfoPojo>> getWorkStatusPojoLiveDataForTag(String tag) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT id, state, output, run_attempt_count FROM workspec WHERE id IN (SELECT work_spec_id FROM worktag WHERE tag=?)", 1);
        if (tag == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, tag);
        }
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"WorkTag", "WorkProgress", "workspec", "worktag"}, true, new Callable<List<WorkSpec.WorkInfoPojo>>() {
            public List<WorkSpec.WorkInfoPojo> call() throws Exception {
                Cursor query;
                WorkSpecDao_Impl.this.__db.beginTransaction();
                try {
                    query = DBUtil.query(WorkSpecDao_Impl.this.__db, acquire, true, (CancellationSignal) null);
                    int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
                    int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "state");
                    int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "output");
                    int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "run_attempt_count");
                    ArrayMap arrayMap = new ArrayMap();
                    ArrayMap arrayMap2 = new ArrayMap();
                    while (query.moveToNext()) {
                        if (!query.isNull(columnIndexOrThrow)) {
                            String string = query.getString(columnIndexOrThrow);
                            if (((ArrayList) arrayMap.get(string)) == null) {
                                arrayMap.put(string, new ArrayList());
                            }
                        }
                        if (!query.isNull(columnIndexOrThrow)) {
                            String string2 = query.getString(columnIndexOrThrow);
                            if (((ArrayList) arrayMap2.get(string2)) == null) {
                                arrayMap2.put(string2, new ArrayList());
                            }
                        }
                    }
                    query.moveToPosition(-1);
                    WorkSpecDao_Impl.this.__fetchRelationshipWorkTagAsjavaLangString(arrayMap);
                    WorkSpecDao_Impl.this.__fetchRelationshipWorkProgressAsandroidxWorkData(arrayMap2);
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        ArrayList arrayList2 = null;
                        if (!query.isNull(columnIndexOrThrow)) {
                            arrayList2 = (ArrayList) arrayMap.get(query.getString(columnIndexOrThrow));
                        }
                        if (arrayList2 == null) {
                            arrayList2 = new ArrayList();
                        }
                        ArrayList arrayList3 = null;
                        if (!query.isNull(columnIndexOrThrow)) {
                            arrayList3 = (ArrayList) arrayMap2.get(query.getString(columnIndexOrThrow));
                        }
                        if (arrayList3 == null) {
                            arrayList3 = new ArrayList();
                        }
                        WorkSpec.WorkInfoPojo workInfoPojo = new WorkSpec.WorkInfoPojo();
                        workInfoPojo.id = query.getString(columnIndexOrThrow);
                        workInfoPojo.state = WorkTypeConverters.intToState(query.getInt(columnIndexOrThrow2));
                        workInfoPojo.output = Data.fromByteArray(query.getBlob(columnIndexOrThrow3));
                        workInfoPojo.runAttemptCount = query.getInt(columnIndexOrThrow4);
                        workInfoPojo.tags = arrayList2;
                        workInfoPojo.progress = arrayList3;
                        arrayList.add(workInfoPojo);
                    }
                    WorkSpecDao_Impl.this.__db.setTransactionSuccessful();
                    query.close();
                    WorkSpecDao_Impl.this.__db.endTransaction();
                    return arrayList;
                } catch (Throwable th) {
                    WorkSpecDao_Impl.this.__db.endTransaction();
                    throw th;
                }
            }

            /* access modifiers changed from: protected */
            public void finalize() {
                acquire.release();
            }
        });
    }

    public boolean hasUnfinishedWork() {
        boolean z = false;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT COUNT(*) > 0 FROM workspec WHERE state NOT IN (2, 3, 5) LIMIT 1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (!query.moveToFirst()) {
                z = false;
            } else if (query.getInt(0) != 0) {
                z = true;
            }
            return z;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public int incrementWorkSpecRunAttemptCount(String id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfIncrementWorkSpecRunAttemptCount.acquire();
        if (id == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, id);
        }
        this.__db.beginTransaction();
        try {
            int executeUpdateDelete = acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
            return executeUpdateDelete;
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfIncrementWorkSpecRunAttemptCount.release(acquire);
        }
    }

    public void insertWorkSpec(WorkSpec workSpec) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfWorkSpec.insert(workSpec);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public int markWorkSpecScheduled(String id, long startTime) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfMarkWorkSpecScheduled.acquire();
        acquire.bindLong(1, startTime);
        if (id == null) {
            acquire.bindNull(2);
        } else {
            acquire.bindString(2, id);
        }
        this.__db.beginTransaction();
        try {
            int executeUpdateDelete = acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
            return executeUpdateDelete;
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfMarkWorkSpecScheduled.release(acquire);
        }
    }

    public void pruneFinishedWorkWithZeroDependentsIgnoringKeepForAtLeast() {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfPruneFinishedWorkWithZeroDependentsIgnoringKeepForAtLeast.acquire();
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfPruneFinishedWorkWithZeroDependentsIgnoringKeepForAtLeast.release(acquire);
        }
    }

    public int resetScheduledState() {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfResetScheduledState.acquire();
        this.__db.beginTransaction();
        try {
            int executeUpdateDelete = acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
            return executeUpdateDelete;
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfResetScheduledState.release(acquire);
        }
    }

    public int resetWorkSpecRunAttemptCount(String id) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfResetWorkSpecRunAttemptCount.acquire();
        if (id == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, id);
        }
        this.__db.beginTransaction();
        try {
            int executeUpdateDelete = acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
            return executeUpdateDelete;
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfResetWorkSpecRunAttemptCount.release(acquire);
        }
    }

    public void setOutput(String id, Data output) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfSetOutput.acquire();
        byte[] byteArrayInternal = Data.toByteArrayInternal(output);
        if (byteArrayInternal == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindBlob(1, byteArrayInternal);
        }
        if (id == null) {
            acquire.bindNull(2);
        } else {
            acquire.bindString(2, id);
        }
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfSetOutput.release(acquire);
        }
    }

    public void setPeriodStartTime(String id, long periodStartTime) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfSetPeriodStartTime.acquire();
        acquire.bindLong(1, periodStartTime);
        if (id == null) {
            acquire.bindNull(2);
        } else {
            acquire.bindString(2, id);
        }
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfSetPeriodStartTime.release(acquire);
        }
    }

    public int setState(WorkInfo.State state, String... ids) {
        this.__db.assertNotSuspendingTransaction();
        StringBuilder newStringBuilder = StringUtil.newStringBuilder();
        newStringBuilder.append("UPDATE workspec SET state=");
        newStringBuilder.append("?");
        newStringBuilder.append(" WHERE id IN (");
        StringUtil.appendPlaceholders(newStringBuilder, ids.length);
        newStringBuilder.append(")");
        SupportSQLiteStatement compileStatement = this.__db.compileStatement(newStringBuilder.toString());
        compileStatement.bindLong(1, (long) WorkTypeConverters.stateToInt(state));
        int i = 2;
        for (String str : ids) {
            if (str == null) {
                compileStatement.bindNull(i);
            } else {
                compileStatement.bindString(i, str);
            }
            i++;
        }
        this.__db.beginTransaction();
        try {
            int executeUpdateDelete = compileStatement.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
            return executeUpdateDelete;
        } finally {
            this.__db.endTransaction();
        }
    }
}

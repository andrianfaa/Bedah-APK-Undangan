package androidx.work.impl.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkDatabaseMigrations;
import androidx.work.impl.model.Preference;

public class IdGenerator {
    public static final int INITIAL_ID = 0;
    public static final String NEXT_ALARM_MANAGER_ID_KEY = "next_alarm_manager_id";
    public static final String NEXT_JOB_SCHEDULER_ID_KEY = "next_job_scheduler_id";
    public static final String PREFERENCE_FILE_KEY = "androidx.work.util.id";
    private final WorkDatabase mWorkDatabase;

    public IdGenerator(WorkDatabase workDatabase) {
        this.mWorkDatabase = workDatabase;
    }

    public static void migrateLegacyIdGenerator(Context context, SupportSQLiteDatabase sqLiteDatabase) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, 0);
        if (sharedPreferences.contains(NEXT_JOB_SCHEDULER_ID_KEY) || sharedPreferences.contains(NEXT_JOB_SCHEDULER_ID_KEY)) {
            int i = sharedPreferences.getInt(NEXT_JOB_SCHEDULER_ID_KEY, 0);
            int i2 = sharedPreferences.getInt(NEXT_ALARM_MANAGER_ID_KEY, 0);
            sqLiteDatabase.beginTransaction();
            try {
                sqLiteDatabase.execSQL(WorkDatabaseMigrations.INSERT_PREFERENCE, new Object[]{NEXT_JOB_SCHEDULER_ID_KEY, Integer.valueOf(i)});
                sqLiteDatabase.execSQL(WorkDatabaseMigrations.INSERT_PREFERENCE, new Object[]{NEXT_ALARM_MANAGER_ID_KEY, Integer.valueOf(i2)});
                sharedPreferences.edit().clear().apply();
                sqLiteDatabase.setTransactionSuccessful();
            } finally {
                sqLiteDatabase.endTransaction();
            }
        }
    }

    private int nextId(String key) {
        this.mWorkDatabase.beginTransaction();
        try {
            Long longValue = this.mWorkDatabase.preferenceDao().getLongValue(key);
            int i = 0;
            int intValue = longValue != null ? longValue.intValue() : 0;
            if (intValue != Integer.MAX_VALUE) {
                i = intValue + 1;
            }
            update(key, i);
            this.mWorkDatabase.setTransactionSuccessful();
            return intValue;
        } finally {
            this.mWorkDatabase.endTransaction();
        }
    }

    private void update(String key, int value) {
        this.mWorkDatabase.preferenceDao().insertPreference(new Preference(key, (long) value));
    }

    public int nextAlarmManagerId() {
        int nextId;
        synchronized (IdGenerator.class) {
            nextId = nextId(NEXT_ALARM_MANAGER_ID_KEY);
        }
        return nextId;
    }

    public int nextJobSchedulerIdWithRange(int minInclusive, int maxInclusive) {
        int nextId;
        synchronized (IdGenerator.class) {
            nextId = nextId(NEXT_JOB_SCHEDULER_ID_KEY);
            if (nextId < minInclusive || nextId > maxInclusive) {
                nextId = minInclusive;
                update(NEXT_JOB_SCHEDULER_ID_KEY, nextId + 1);
            }
        }
        return nextId;
    }
}

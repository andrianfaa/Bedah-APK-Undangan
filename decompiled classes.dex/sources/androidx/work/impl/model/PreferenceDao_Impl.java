package androidx.work.impl.model;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.util.concurrent.Callable;

public final class PreferenceDao_Impl implements PreferenceDao {
    /* access modifiers changed from: private */
    public final RoomDatabase __db;
    private final EntityInsertionAdapter<Preference> __insertionAdapterOfPreference;

    public PreferenceDao_Impl(RoomDatabase __db2) {
        this.__db = __db2;
        this.__insertionAdapterOfPreference = new EntityInsertionAdapter<Preference>(__db2) {
            public void bind(SupportSQLiteStatement stmt, Preference value) {
                if (value.mKey == null) {
                    stmt.bindNull(1);
                } else {
                    stmt.bindString(1, value.mKey);
                }
                if (value.mValue == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindLong(2, value.mValue.longValue());
                }
            }

            public String createQuery() {
                return "INSERT OR REPLACE INTO `Preference` (`key`,`long_value`) VALUES (?,?)";
            }
        };
    }

    public Long getLongValue(String key) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT long_value FROM Preference where `key`=?", 1);
        if (key == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, key);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            return query.moveToFirst() ? query.isNull(0) ? null : Long.valueOf(query.getLong(0)) : null;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public LiveData<Long> getObservableLongValue(String key) {
        final RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT long_value FROM Preference where `key`=?", 1);
        if (key == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, key);
        }
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"Preference"}, false, new Callable<Long>() {
            public Long call() throws Exception {
                Cursor query = DBUtil.query(PreferenceDao_Impl.this.__db, acquire, false, (CancellationSignal) null);
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

    public void insertPreference(Preference preference) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfPreference.insert(preference);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }
}

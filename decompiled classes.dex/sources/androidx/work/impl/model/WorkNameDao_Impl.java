package androidx.work.impl.model;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.util.ArrayList;
import java.util.List;

public final class WorkNameDao_Impl implements WorkNameDao {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<WorkName> __insertionAdapterOfWorkName;

    public WorkNameDao_Impl(RoomDatabase __db2) {
        this.__db = __db2;
        this.__insertionAdapterOfWorkName = new EntityInsertionAdapter<WorkName>(__db2) {
            public void bind(SupportSQLiteStatement stmt, WorkName value) {
                if (value.name == null) {
                    stmt.bindNull(1);
                } else {
                    stmt.bindString(1, value.name);
                }
                if (value.workSpecId == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.workSpecId);
                }
            }

            public String createQuery() {
                return "INSERT OR IGNORE INTO `WorkName` (`name`,`work_spec_id`) VALUES (?,?)";
            }
        };
    }

    public List<String> getNamesForWorkSpecId(String workSpecId) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT name FROM workname WHERE work_spec_id=?", 1);
        if (workSpecId == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, workSpecId);
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

    public List<String> getWorkSpecIdsWithName(String name) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT work_spec_id FROM workname WHERE name=?", 1);
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

    public void insert(WorkName workName) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfWorkName.insert(workName);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }
}

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

public final class DependencyDao_Impl implements DependencyDao {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<Dependency> __insertionAdapterOfDependency;

    public DependencyDao_Impl(RoomDatabase __db2) {
        this.__db = __db2;
        this.__insertionAdapterOfDependency = new EntityInsertionAdapter<Dependency>(__db2) {
            public void bind(SupportSQLiteStatement stmt, Dependency value) {
                if (value.workSpecId == null) {
                    stmt.bindNull(1);
                } else {
                    stmt.bindString(1, value.workSpecId);
                }
                if (value.prerequisiteId == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.prerequisiteId);
                }
            }

            public String createQuery() {
                return "INSERT OR IGNORE INTO `Dependency` (`work_spec_id`,`prerequisite_id`) VALUES (?,?)";
            }
        };
    }

    public List<String> getDependentWorkIds(String id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT work_spec_id FROM dependency WHERE prerequisite_id=?", 1);
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
                arrayList.add(query.getString(0));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public List<String> getPrerequisites(String id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT prerequisite_id FROM dependency WHERE work_spec_id=?", 1);
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
                arrayList.add(query.getString(0));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public boolean hasCompletedAllPrerequisites(String id) {
        boolean z = true;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT COUNT(*)=0 FROM dependency WHERE work_spec_id=? AND prerequisite_id IN (SELECT id FROM workspec WHERE state!=2)", 1);
        if (id == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, id);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (!query.moveToFirst()) {
                z = false;
            } else if (query.getInt(0) == 0) {
                z = false;
            }
            return z;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public boolean hasDependents(String id) {
        boolean z = true;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT COUNT(*)>0 FROM dependency WHERE prerequisite_id=?", 1);
        if (id == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, id);
        }
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (!query.moveToFirst()) {
                z = false;
            } else if (query.getInt(0) == 0) {
                z = false;
            }
            return z;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public void insertDependency(Dependency dependency) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfDependency.insert(dependency);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }
}

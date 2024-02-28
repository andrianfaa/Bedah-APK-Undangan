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

public final class WorkTagDao_Impl implements WorkTagDao {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter<WorkTag> __insertionAdapterOfWorkTag;

    public WorkTagDao_Impl(RoomDatabase __db2) {
        this.__db = __db2;
        this.__insertionAdapterOfWorkTag = new EntityInsertionAdapter<WorkTag>(__db2) {
            public void bind(SupportSQLiteStatement stmt, WorkTag value) {
                if (value.tag == null) {
                    stmt.bindNull(1);
                } else {
                    stmt.bindString(1, value.tag);
                }
                if (value.workSpecId == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindString(2, value.workSpecId);
                }
            }

            public String createQuery() {
                return "INSERT OR IGNORE INTO `WorkTag` (`tag`,`work_spec_id`) VALUES (?,?)";
            }
        };
    }

    public List<String> getTagsForWorkSpecId(String id) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT DISTINCT tag FROM worktag WHERE work_spec_id=?", 1);
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

    public List<String> getWorkSpecIdsWithTag(String tag) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT work_spec_id FROM worktag WHERE tag=?", 1);
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

    public void insert(WorkTag workTag) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfWorkTag.insert(workTag);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }
}

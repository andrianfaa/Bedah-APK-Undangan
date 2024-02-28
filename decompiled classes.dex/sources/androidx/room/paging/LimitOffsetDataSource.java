package androidx.room.paging;

import android.database.Cursor;
import androidx.paging.PositionalDataSource;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class LimitOffsetDataSource<T> extends PositionalDataSource<T> {
    private final String mCountQuery;
    private final RoomDatabase mDb;
    private final boolean mInTransaction;
    private final String mLimitOffsetQuery;
    private final InvalidationTracker.Observer mObserver;
    private final RoomSQLiteQuery mSourceQuery;

    protected LimitOffsetDataSource(RoomDatabase db, RoomSQLiteQuery query, boolean inTransaction, String... tables) {
        this.mDb = db;
        this.mSourceQuery = query;
        this.mInTransaction = inTransaction;
        this.mCountQuery = "SELECT COUNT(*) FROM ( " + query.getSql() + " )";
        this.mLimitOffsetQuery = "SELECT * FROM ( " + query.getSql() + " ) LIMIT ? OFFSET ?";
        AnonymousClass1 r0 = new InvalidationTracker.Observer(tables) {
            public void onInvalidated(Set<String> set) {
                LimitOffsetDataSource.this.invalidate();
            }
        };
        this.mObserver = r0;
        db.getInvalidationTracker().addWeakObserver(r0);
    }

    protected LimitOffsetDataSource(RoomDatabase db, SupportSQLiteQuery query, boolean inTransaction, String... tables) {
        this(db, RoomSQLiteQuery.copyFrom(query), inTransaction, tables);
    }

    private RoomSQLiteQuery getSQLiteQuery(int startPosition, int loadCount) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(this.mLimitOffsetQuery, this.mSourceQuery.getArgCount() + 2);
        acquire.copyArgumentsFrom(this.mSourceQuery);
        acquire.bindLong(acquire.getArgCount() - 1, (long) loadCount);
        acquire.bindLong(acquire.getArgCount(), (long) startPosition);
        return acquire;
    }

    /* access modifiers changed from: protected */
    public abstract List<T> convertRows(Cursor cursor);

    public int countItems() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(this.mCountQuery, this.mSourceQuery.getArgCount());
        acquire.copyArgumentsFrom(this.mSourceQuery);
        Cursor query = this.mDb.query(acquire);
        try {
            if (query.moveToFirst()) {
                return query.getInt(0);
            }
            query.close();
            acquire.release();
            return 0;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public boolean isInvalid() {
        this.mDb.getInvalidationTracker().refreshVersionsSync();
        return LimitOffsetDataSource.super.isInvalid();
    }

    public void loadInitial(PositionalDataSource.LoadInitialParams params, PositionalDataSource.LoadInitialCallback<T> loadInitialCallback) {
        List emptyList = Collections.emptyList();
        int i = 0;
        RoomSQLiteQuery roomSQLiteQuery = null;
        Cursor cursor = null;
        this.mDb.beginTransaction();
        try {
            int countItems = countItems();
            if (countItems != 0) {
                i = computeInitialLoadPosition(params, countItems);
                roomSQLiteQuery = getSQLiteQuery(i, computeInitialLoadSize(params, i, countItems));
                cursor = this.mDb.query(roomSQLiteQuery);
                List convertRows = convertRows(cursor);
                this.mDb.setTransactionSuccessful();
                emptyList = convertRows;
            }
            loadInitialCallback.onResult(emptyList, i, countItems);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            this.mDb.endTransaction();
            if (roomSQLiteQuery != null) {
                roomSQLiteQuery.release();
            }
        }
    }

    public List<T> loadRange(int startPosition, int loadCount) {
        RoomSQLiteQuery sQLiteQuery = getSQLiteQuery(startPosition, loadCount);
        if (this.mInTransaction) {
            this.mDb.beginTransaction();
            Cursor cursor = null;
            try {
                cursor = this.mDb.query(sQLiteQuery);
                List<T> convertRows = convertRows(cursor);
                this.mDb.setTransactionSuccessful();
                return convertRows;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                this.mDb.endTransaction();
                sQLiteQuery.release();
            }
        } else {
            Cursor query = this.mDb.query(sQLiteQuery);
            try {
                return convertRows(query);
            } finally {
                query.close();
                sQLiteQuery.release();
            }
        }
    }

    public void loadRange(PositionalDataSource.LoadRangeParams params, PositionalDataSource.LoadRangeCallback<T> loadRangeCallback) {
        loadRangeCallback.onResult(loadRange(params.startPosition, params.loadSize));
    }
}

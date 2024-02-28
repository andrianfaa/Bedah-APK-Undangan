package androidx.sqlite.db.framework;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteTransactionListener;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.util.Pair;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import okhttp3.HttpUrl;

class FrameworkSQLiteDatabase implements SupportSQLiteDatabase {
    private static final String[] CONFLICT_VALUES = {HttpUrl.FRAGMENT_ENCODE_SET, " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE "};
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private final SQLiteDatabase mDelegate;

    FrameworkSQLiteDatabase(SQLiteDatabase delegate) {
        this.mDelegate = delegate;
    }

    public void beginTransaction() {
        this.mDelegate.beginTransaction();
    }

    public void beginTransactionNonExclusive() {
        this.mDelegate.beginTransactionNonExclusive();
    }

    public void beginTransactionWithListener(SQLiteTransactionListener transactionListener) {
        this.mDelegate.beginTransactionWithListener(transactionListener);
    }

    public void beginTransactionWithListenerNonExclusive(SQLiteTransactionListener transactionListener) {
        this.mDelegate.beginTransactionWithListenerNonExclusive(transactionListener);
    }

    public void close() throws IOException {
        this.mDelegate.close();
    }

    public SupportSQLiteStatement compileStatement(String sql) {
        return new FrameworkSQLiteStatement(this.mDelegate.compileStatement(sql));
    }

    public int delete(String table, String whereClause, Object[] whereArgs) {
        SupportSQLiteStatement compileStatement = compileStatement("DELETE FROM " + table + (TextUtils.isEmpty(whereClause) ? HttpUrl.FRAGMENT_ENCODE_SET : " WHERE " + whereClause));
        SimpleSQLiteQuery.bind(compileStatement, whereArgs);
        return compileStatement.executeUpdateDelete();
    }

    public void disableWriteAheadLogging() {
        this.mDelegate.disableWriteAheadLogging();
    }

    public boolean enableWriteAheadLogging() {
        return this.mDelegate.enableWriteAheadLogging();
    }

    public void endTransaction() {
        this.mDelegate.endTransaction();
    }

    public void execSQL(String sql) throws SQLException {
        this.mDelegate.execSQL(sql);
    }

    public void execSQL(String sql, Object[] bindArgs) throws SQLException {
        this.mDelegate.execSQL(sql, bindArgs);
    }

    public List<Pair<String, String>> getAttachedDbs() {
        return this.mDelegate.getAttachedDbs();
    }

    public long getMaximumSize() {
        return this.mDelegate.getMaximumSize();
    }

    public long getPageSize() {
        return this.mDelegate.getPageSize();
    }

    public String getPath() {
        return this.mDelegate.getPath();
    }

    public int getVersion() {
        return this.mDelegate.getVersion();
    }

    public boolean inTransaction() {
        return this.mDelegate.inTransaction();
    }

    public long insert(String table, int conflictAlgorithm, ContentValues values) throws SQLException {
        return this.mDelegate.insertWithOnConflict(table, (String) null, values, conflictAlgorithm);
    }

    public boolean isDatabaseIntegrityOk() {
        return this.mDelegate.isDatabaseIntegrityOk();
    }

    public boolean isDbLockedByCurrentThread() {
        return this.mDelegate.isDbLockedByCurrentThread();
    }

    /* access modifiers changed from: package-private */
    public boolean isDelegate(SQLiteDatabase sqLiteDatabase) {
        return this.mDelegate == sqLiteDatabase;
    }

    public boolean isOpen() {
        return this.mDelegate.isOpen();
    }

    public boolean isReadOnly() {
        return this.mDelegate.isReadOnly();
    }

    public boolean isWriteAheadLoggingEnabled() {
        return this.mDelegate.isWriteAheadLoggingEnabled();
    }

    public boolean needUpgrade(int newVersion) {
        return this.mDelegate.needUpgrade(newVersion);
    }

    public Cursor query(final SupportSQLiteQuery supportQuery) {
        return this.mDelegate.rawQueryWithFactory(new SQLiteDatabase.CursorFactory() {
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
                supportQuery.bindTo(new FrameworkSQLiteProgram(query));
                return new SQLiteCursor(masterQuery, editTable, query);
            }
        }, supportQuery.getSql(), EMPTY_STRING_ARRAY, (String) null);
    }

    public Cursor query(final SupportSQLiteQuery supportQuery, CancellationSignal cancellationSignal) {
        return this.mDelegate.rawQueryWithFactory(new SQLiteDatabase.CursorFactory() {
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
                supportQuery.bindTo(new FrameworkSQLiteProgram(query));
                return new SQLiteCursor(masterQuery, editTable, query);
            }
        }, supportQuery.getSql(), EMPTY_STRING_ARRAY, (String) null, cancellationSignal);
    }

    public Cursor query(String query) {
        return query((SupportSQLiteQuery) new SimpleSQLiteQuery(query));
    }

    public Cursor query(String query, Object[] bindArgs) {
        return query((SupportSQLiteQuery) new SimpleSQLiteQuery(query, bindArgs));
    }

    public void setForeignKeyConstraintsEnabled(boolean enable) {
        this.mDelegate.setForeignKeyConstraintsEnabled(enable);
    }

    public void setLocale(Locale locale) {
        this.mDelegate.setLocale(locale);
    }

    public void setMaxSqlCacheSize(int cacheSize) {
        this.mDelegate.setMaxSqlCacheSize(cacheSize);
    }

    public long setMaximumSize(long numBytes) {
        return this.mDelegate.setMaximumSize(numBytes);
    }

    public void setPageSize(long numBytes) {
        this.mDelegate.setPageSize(numBytes);
    }

    public void setTransactionSuccessful() {
        this.mDelegate.setTransactionSuccessful();
    }

    public void setVersion(int version) {
        this.mDelegate.setVersion(version);
    }

    public int update(String table, int conflictAlgorithm, ContentValues values, String whereClause, Object[] whereArgs) {
        if (values == null || values.size() == 0) {
            throw new IllegalArgumentException("Empty values");
        }
        StringBuilder sb = new StringBuilder(120);
        sb.append("UPDATE ");
        sb.append(CONFLICT_VALUES[conflictAlgorithm]);
        sb.append(table);
        sb.append(" SET ");
        int size = values.size();
        int length = whereArgs == null ? size : whereArgs.length + size;
        Object[] objArr = new Object[length];
        int i = 0;
        for (String next : values.keySet()) {
            sb.append(i > 0 ? "," : HttpUrl.FRAGMENT_ENCODE_SET);
            sb.append(next);
            objArr[i] = values.get(next);
            sb.append("=?");
            i++;
        }
        if (whereArgs != null) {
            for (int i2 = size; i2 < length; i2++) {
                objArr[i2] = whereArgs[i2 - size];
            }
        }
        if (!TextUtils.isEmpty(whereClause)) {
            sb.append(" WHERE ");
            sb.append(whereClause);
        }
        SupportSQLiteStatement compileStatement = compileStatement(sb.toString());
        SimpleSQLiteQuery.bind(compileStatement, objArr);
        return compileStatement.executeUpdateDelete();
    }

    public boolean yieldIfContendedSafely() {
        return this.mDelegate.yieldIfContendedSafely();
    }

    public boolean yieldIfContendedSafely(long sleepAfterYieldDelay) {
        return this.mDelegate.yieldIfContendedSafely(sleepAfterYieldDelay);
    }
}

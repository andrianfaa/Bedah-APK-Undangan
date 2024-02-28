package androidx.sqlite.db.framework;

import android.database.sqlite.SQLiteStatement;
import androidx.sqlite.db.SupportSQLiteStatement;

class FrameworkSQLiteStatement extends FrameworkSQLiteProgram implements SupportSQLiteStatement {
    private final SQLiteStatement mDelegate;

    FrameworkSQLiteStatement(SQLiteStatement delegate) {
        super(delegate);
        this.mDelegate = delegate;
    }

    public void execute() {
        this.mDelegate.execute();
    }

    public long executeInsert() {
        return this.mDelegate.executeInsert();
    }

    public int executeUpdateDelete() {
        return this.mDelegate.executeUpdateDelete();
    }

    public long simpleQueryForLong() {
        return this.mDelegate.simpleQueryForLong();
    }

    public String simpleQueryForString() {
        return this.mDelegate.simpleQueryForString();
    }
}

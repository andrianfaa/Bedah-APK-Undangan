package androidx.room.migration;

import androidx.sqlite.db.SupportSQLiteDatabase;

public abstract class Migration {
    public final int endVersion;
    public final int startVersion;

    public Migration(int startVersion2, int endVersion2) {
        this.startVersion = startVersion2;
        this.endVersion = endVersion2;
    }

    public abstract void migrate(SupportSQLiteDatabase supportSQLiteDatabase);
}

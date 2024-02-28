package androidx.room;

import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.io.File;

class SQLiteCopyOpenHelperFactory implements SupportSQLiteOpenHelper.Factory {
    private final String mCopyFromAssetPath;
    private final File mCopyFromFile;
    private final SupportSQLiteOpenHelper.Factory mDelegate;

    SQLiteCopyOpenHelperFactory(String copyFromAssetPath, File copyFromFile, SupportSQLiteOpenHelper.Factory factory) {
        this.mCopyFromAssetPath = copyFromAssetPath;
        this.mCopyFromFile = copyFromFile;
        this.mDelegate = factory;
    }

    public SupportSQLiteOpenHelper create(SupportSQLiteOpenHelper.Configuration configuration) {
        return new SQLiteCopyOpenHelper(configuration.context, this.mCopyFromAssetPath, this.mCopyFromFile, configuration.callback.version, this.mDelegate.create(configuration));
    }
}

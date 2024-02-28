package androidx.room.util;

import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.os.Build;
import android.os.CancellationSignal;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class DBUtil {
    private DBUtil() {
    }

    public static CancellationSignal createCancellationSignal() {
        if (Build.VERSION.SDK_INT >= 16) {
            return new CancellationSignal();
        }
        return null;
    }

    /* JADX INFO: finally extract failed */
    public static void dropFtsSyncTriggers(SupportSQLiteDatabase db) {
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor query = db.query("SELECT name FROM sqlite_master WHERE type = 'trigger'");
        while (query.moveToNext()) {
            try {
                arrayList.add(query.getString(0));
            } catch (Throwable th) {
                query.close();
                throw th;
            }
        }
        query.close();
        for (String str : arrayList) {
            if (str.startsWith("room_fts_content_sync_")) {
                db.execSQL("DROP TRIGGER IF EXISTS " + str);
            }
        }
    }

    @Deprecated
    public static Cursor query(RoomDatabase db, SupportSQLiteQuery sqLiteQuery, boolean maybeCopy) {
        return query(db, sqLiteQuery, maybeCopy, (CancellationSignal) null);
    }

    public static Cursor query(RoomDatabase db, SupportSQLiteQuery sqLiteQuery, boolean maybeCopy, CancellationSignal signal) {
        Cursor query = db.query(sqLiteQuery, signal);
        if (maybeCopy && (query instanceof AbstractWindowedCursor)) {
            AbstractWindowedCursor abstractWindowedCursor = (AbstractWindowedCursor) query;
            int count = abstractWindowedCursor.getCount();
            int numRows = abstractWindowedCursor.hasWindow() ? abstractWindowedCursor.getWindow().getNumRows() : count;
            if (Build.VERSION.SDK_INT < 23 || numRows < count) {
                return CursorUtil.copyAndClose(abstractWindowedCursor);
            }
        }
        return query;
    }

    public static int readVersion(File databaseFile) throws IOException {
        FileChannel fileChannel = null;
        try {
            ByteBuffer allocate = ByteBuffer.allocate(4);
            fileChannel = new FileInputStream(databaseFile).getChannel();
            fileChannel.tryLock(60, 4, true);
            fileChannel.position(60);
            if (fileChannel.read(allocate) == 4) {
                allocate.rewind();
                return allocate.getInt();
            }
            throw new IOException("Bad database header, unable to read 4 bytes at offset 60");
        } finally {
            if (fileChannel != null) {
                fileChannel.close();
            }
        }
    }
}

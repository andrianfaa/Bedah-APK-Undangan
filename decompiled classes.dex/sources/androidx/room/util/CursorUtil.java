package androidx.room.util;

import android.database.Cursor;
import android.database.MatrixCursor;

public class CursorUtil {
    private CursorUtil() {
    }

    public static Cursor copyAndClose(Cursor c) {
        try {
            MatrixCursor matrixCursor = new MatrixCursor(c.getColumnNames(), c.getCount());
            while (c.moveToNext()) {
                Object[] objArr = new Object[c.getColumnCount()];
                for (int i = 0; i < c.getColumnCount(); i++) {
                    switch (c.getType(i)) {
                        case 0:
                            objArr[i] = null;
                            break;
                        case 1:
                            objArr[i] = Long.valueOf(c.getLong(i));
                            break;
                        case 2:
                            objArr[i] = Double.valueOf(c.getDouble(i));
                            break;
                        case 3:
                            objArr[i] = c.getString(i);
                            break;
                        case 4:
                            objArr[i] = c.getBlob(i);
                            break;
                        default:
                            throw new IllegalStateException();
                    }
                }
                matrixCursor.addRow(objArr);
            }
            return matrixCursor;
        } finally {
            c.close();
        }
    }

    public static int getColumnIndex(Cursor c, String name) {
        int columnIndex = c.getColumnIndex(name);
        return columnIndex >= 0 ? columnIndex : c.getColumnIndex("`" + name + "`");
    }

    public static int getColumnIndexOrThrow(Cursor c, String name) {
        int columnIndex = c.getColumnIndex(name);
        return columnIndex >= 0 ? columnIndex : c.getColumnIndexOrThrow("`" + name + "`");
    }
}

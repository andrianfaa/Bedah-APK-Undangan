package androidx.room.util;

import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import okhttp3.HttpUrl;

public class FtsTableInfo {
    private static final String[] FTS_OPTIONS = {"tokenize=", "compress=", "content=", "languageid=", "matchinfo=", "notindexed=", "order=", "prefix=", "uncompress="};
    public final Set<String> columns;
    public final String name;
    public final Set<String> options;

    public FtsTableInfo(String name2, Set<String> set, String createSql) {
        this.name = name2;
        this.columns = set;
        this.options = parseOptions(createSql);
    }

    public FtsTableInfo(String name2, Set<String> set, Set<String> set2) {
        this.name = name2;
        this.columns = set;
        this.options = set2;
    }

    static Set<String> parseOptions(String createStatement) {
        if (createStatement.isEmpty()) {
            return new HashSet();
        }
        String substring = createStatement.substring(createStatement.indexOf(40) + 1, createStatement.lastIndexOf(41));
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayDeque arrayDeque = new ArrayDeque();
        int i = -1;
        for (int i2 = 0; i2 < substring.length(); i2++) {
            char charAt = substring.charAt(i2);
            switch (charAt) {
                case '\"':
                case '\'':
                case '`':
                    if (!arrayDeque.isEmpty()) {
                        if (((Character) arrayDeque.peek()).charValue() != charAt) {
                            break;
                        } else {
                            arrayDeque.pop();
                            break;
                        }
                    } else {
                        arrayDeque.push(Character.valueOf(charAt));
                        break;
                    }
                case ',':
                    if (!arrayDeque.isEmpty()) {
                        break;
                    } else {
                        arrayList.add(substring.substring(i + 1, i2).trim());
                        i = i2;
                        break;
                    }
                case '[':
                    if (!arrayDeque.isEmpty()) {
                        break;
                    } else {
                        arrayDeque.push(Character.valueOf(charAt));
                        break;
                    }
                case ']':
                    if (!arrayDeque.isEmpty() && ((Character) arrayDeque.peek()).charValue() == '[') {
                        arrayDeque.pop();
                        break;
                    }
            }
        }
        arrayList.add(substring.substring(i + 1).trim());
        HashSet hashSet = new HashSet();
        for (String str : arrayList) {
            for (String startsWith : FTS_OPTIONS) {
                if (str.startsWith(startsWith)) {
                    hashSet.add(str);
                }
            }
        }
        return hashSet;
    }

    public static FtsTableInfo read(SupportSQLiteDatabase database, String tableName) {
        return new FtsTableInfo(tableName, readColumns(database, tableName), readOptions(database, tableName));
    }

    private static Set<String> readColumns(SupportSQLiteDatabase database, String tableName) {
        Cursor query = database.query("PRAGMA table_info(`" + tableName + "`)");
        HashSet hashSet = new HashSet();
        try {
            if (query.getColumnCount() > 0) {
                int columnIndex = query.getColumnIndex("name");
                while (query.moveToNext()) {
                    hashSet.add(query.getString(columnIndex));
                }
            }
            return hashSet;
        } finally {
            query.close();
        }
    }

    /* JADX INFO: finally extract failed */
    private static Set<String> readOptions(SupportSQLiteDatabase database, String tableName) {
        String str = HttpUrl.FRAGMENT_ENCODE_SET;
        Cursor query = database.query("SELECT * FROM sqlite_master WHERE `name` = '" + tableName + "'");
        try {
            if (query.moveToFirst()) {
                str = query.getString(query.getColumnIndexOrThrow("sql"));
            }
            query.close();
            return parseOptions(str);
        } catch (Throwable th) {
            query.close();
            throw th;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FtsTableInfo ftsTableInfo = (FtsTableInfo) o;
        String str = this.name;
        if (str == null ? ftsTableInfo.name != null : !str.equals(ftsTableInfo.name)) {
            return false;
        }
        Set<String> set = this.columns;
        if (set == null ? ftsTableInfo.columns != null : !set.equals(ftsTableInfo.columns)) {
            return false;
        }
        Set<String> set2 = this.options;
        return set2 != null ? set2.equals(ftsTableInfo.options) : ftsTableInfo.options == null;
    }

    public int hashCode() {
        String str = this.name;
        int i = 0;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        Set<String> set = this.columns;
        int hashCode2 = (hashCode + (set != null ? set.hashCode() : 0)) * 31;
        Set<String> set2 = this.options;
        if (set2 != null) {
            i = set2.hashCode();
        }
        return hashCode2 + i;
    }

    public String toString() {
        return "FtsTableInfo{name='" + this.name + '\'' + ", columns=" + this.columns + ", options=" + this.options + '}';
    }
}

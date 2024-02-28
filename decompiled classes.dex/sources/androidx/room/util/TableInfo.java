package androidx.room.util;

import android.database.Cursor;
import android.os.Build;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TableInfo {
    public static final int CREATED_FROM_DATABASE = 2;
    public static final int CREATED_FROM_ENTITY = 1;
    public static final int CREATED_FROM_UNKNOWN = 0;
    public final Map<String, Column> columns;
    public final Set<ForeignKey> foreignKeys;
    public final Set<Index> indices;
    public final String name;

    public static class Column {
        public final int affinity;
        public final String defaultValue;
        private final int mCreatedFrom;
        public final String name;
        public final boolean notNull;
        public final int primaryKeyPosition;
        public final String type;

        @Deprecated
        public Column(String name2, String type2, boolean notNull2, int primaryKeyPosition2) {
            this(name2, type2, notNull2, primaryKeyPosition2, (String) null, 0);
        }

        public Column(String name2, String type2, boolean notNull2, int primaryKeyPosition2, String defaultValue2, int createdFrom) {
            this.name = name2;
            this.type = type2;
            this.notNull = notNull2;
            this.primaryKeyPosition = primaryKeyPosition2;
            this.affinity = findAffinity(type2);
            this.defaultValue = defaultValue2;
            this.mCreatedFrom = createdFrom;
        }

        private static int findAffinity(String type2) {
            if (type2 == null) {
                return 5;
            }
            String upperCase = type2.toUpperCase(Locale.US);
            if (upperCase.contains("INT")) {
                return 3;
            }
            if (upperCase.contains("CHAR") || upperCase.contains("CLOB") || upperCase.contains("TEXT")) {
                return 2;
            }
            if (upperCase.contains("BLOB")) {
                return 5;
            }
            return (upperCase.contains("REAL") || upperCase.contains("FLOA") || upperCase.contains("DOUB")) ? 4 : 1;
        }

        public boolean equals(Object o) {
            String str;
            String str2;
            String str3;
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Column column = (Column) o;
            if (Build.VERSION.SDK_INT >= 20) {
                if (this.primaryKeyPosition != column.primaryKeyPosition) {
                    return false;
                }
            } else if (isPrimaryKey() != column.isPrimaryKey()) {
                return false;
            }
            if (!this.name.equals(column.name) || this.notNull != column.notNull) {
                return false;
            }
            if (this.mCreatedFrom == 1 && column.mCreatedFrom == 2 && (str3 = this.defaultValue) != null && !str3.equals(column.defaultValue)) {
                return false;
            }
            if (this.mCreatedFrom == 2 && column.mCreatedFrom == 1 && (str2 = column.defaultValue) != null && !str2.equals(this.defaultValue)) {
                return false;
            }
            int i = this.mCreatedFrom;
            if (i == 0 || i != column.mCreatedFrom || ((str = this.defaultValue) == null ? column.defaultValue == null : str.equals(column.defaultValue))) {
                return this.affinity == column.affinity;
            }
            return false;
        }

        public int hashCode() {
            return (((((this.name.hashCode() * 31) + this.affinity) * 31) + (this.notNull ? 1231 : 1237)) * 31) + this.primaryKeyPosition;
        }

        public boolean isPrimaryKey() {
            return this.primaryKeyPosition > 0;
        }

        public String toString() {
            return "Column{name='" + this.name + '\'' + ", type='" + this.type + '\'' + ", affinity='" + this.affinity + '\'' + ", notNull=" + this.notNull + ", primaryKeyPosition=" + this.primaryKeyPosition + ", defaultValue='" + this.defaultValue + '\'' + '}';
        }
    }

    public static class ForeignKey {
        public final List<String> columnNames;
        public final String onDelete;
        public final String onUpdate;
        public final List<String> referenceColumnNames;
        public final String referenceTable;

        public ForeignKey(String referenceTable2, String onDelete2, String onUpdate2, List<String> list, List<String> list2) {
            this.referenceTable = referenceTable2;
            this.onDelete = onDelete2;
            this.onUpdate = onUpdate2;
            this.columnNames = Collections.unmodifiableList(list);
            this.referenceColumnNames = Collections.unmodifiableList(list2);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ForeignKey foreignKey = (ForeignKey) o;
            if (this.referenceTable.equals(foreignKey.referenceTable) && this.onDelete.equals(foreignKey.onDelete) && this.onUpdate.equals(foreignKey.onUpdate) && this.columnNames.equals(foreignKey.columnNames)) {
                return this.referenceColumnNames.equals(foreignKey.referenceColumnNames);
            }
            return false;
        }

        public int hashCode() {
            return (((((((this.referenceTable.hashCode() * 31) + this.onDelete.hashCode()) * 31) + this.onUpdate.hashCode()) * 31) + this.columnNames.hashCode()) * 31) + this.referenceColumnNames.hashCode();
        }

        public String toString() {
            return "ForeignKey{referenceTable='" + this.referenceTable + '\'' + ", onDelete='" + this.onDelete + '\'' + ", onUpdate='" + this.onUpdate + '\'' + ", columnNames=" + this.columnNames + ", referenceColumnNames=" + this.referenceColumnNames + '}';
        }
    }

    static class ForeignKeyWithSequence implements Comparable<ForeignKeyWithSequence> {
        final String mFrom;
        final int mId;
        final int mSequence;
        final String mTo;

        ForeignKeyWithSequence(int id, int sequence, String from, String to) {
            this.mId = id;
            this.mSequence = sequence;
            this.mFrom = from;
            this.mTo = to;
        }

        public int compareTo(ForeignKeyWithSequence o) {
            int i = this.mId - o.mId;
            return i == 0 ? this.mSequence - o.mSequence : i;
        }
    }

    public static class Index {
        public static final String DEFAULT_PREFIX = "index_";
        public final List<String> columns;
        public final String name;
        public final boolean unique;

        public Index(String name2, boolean unique2, List<String> list) {
            this.name = name2;
            this.unique = unique2;
            this.columns = list;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Index index = (Index) o;
            if (this.unique == index.unique && this.columns.equals(index.columns)) {
                return this.name.startsWith(DEFAULT_PREFIX) ? index.name.startsWith(DEFAULT_PREFIX) : this.name.equals(index.name);
            }
            return false;
        }

        public int hashCode() {
            return ((((this.name.startsWith(DEFAULT_PREFIX) ? DEFAULT_PREFIX.hashCode() : this.name.hashCode()) * 31) + (this.unique ? 1 : 0)) * 31) + this.columns.hashCode();
        }

        public String toString() {
            return "Index{name='" + this.name + '\'' + ", unique=" + this.unique + ", columns=" + this.columns + '}';
        }
    }

    public TableInfo(String name2, Map<String, Column> map, Set<ForeignKey> set) {
        this(name2, map, set, Collections.emptySet());
    }

    public TableInfo(String name2, Map<String, Column> map, Set<ForeignKey> set, Set<Index> set2) {
        this.name = name2;
        this.columns = Collections.unmodifiableMap(map);
        this.foreignKeys = Collections.unmodifiableSet(set);
        this.indices = set2 == null ? null : Collections.unmodifiableSet(set2);
    }

    public static TableInfo read(SupportSQLiteDatabase database, String tableName) {
        return new TableInfo(tableName, readColumns(database, tableName), readForeignKeys(database, tableName), readIndices(database, tableName));
    }

    private static Map<String, Column> readColumns(SupportSQLiteDatabase database, String tableName) {
        Cursor query = database.query("PRAGMA table_info(`" + tableName + "`)");
        HashMap hashMap = new HashMap();
        try {
            if (query.getColumnCount() > 0) {
                int columnIndex = query.getColumnIndex("name");
                int columnIndex2 = query.getColumnIndex("type");
                int columnIndex3 = query.getColumnIndex("notnull");
                int columnIndex4 = query.getColumnIndex("pk");
                int columnIndex5 = query.getColumnIndex("dflt_value");
                while (query.moveToNext()) {
                    String string = query.getString(columnIndex);
                    int i = columnIndex;
                    Column column = r10;
                    Column column2 = new Column(string, query.getString(columnIndex2), query.getInt(columnIndex3) != 0, query.getInt(columnIndex4), query.getString(columnIndex5), 2);
                    hashMap.put(string, column);
                    columnIndex = i;
                }
                int i2 = columnIndex;
            }
            return hashMap;
        } finally {
            query.close();
        }
    }

    private static List<ForeignKeyWithSequence> readForeignKeyFieldMappings(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex("id");
        int columnIndex2 = cursor.getColumnIndex("seq");
        int columnIndex3 = cursor.getColumnIndex(TypedValues.TransitionType.S_FROM);
        int columnIndex4 = cursor.getColumnIndex(TypedValues.TransitionType.S_TO);
        int count = cursor.getCount();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            arrayList.add(new ForeignKeyWithSequence(cursor.getInt(columnIndex), cursor.getInt(columnIndex2), cursor.getString(columnIndex3), cursor.getString(columnIndex4)));
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    private static Set<ForeignKey> readForeignKeys(SupportSQLiteDatabase database, String tableName) {
        int i;
        HashSet hashSet = new HashSet();
        Cursor query = database.query("PRAGMA foreign_key_list(`" + tableName + "`)");
        try {
            int columnIndex = query.getColumnIndex("id");
            int columnIndex2 = query.getColumnIndex("seq");
            int columnIndex3 = query.getColumnIndex("table");
            int columnIndex4 = query.getColumnIndex("on_delete");
            int columnIndex5 = query.getColumnIndex("on_update");
            List<ForeignKeyWithSequence> readForeignKeyFieldMappings = readForeignKeyFieldMappings(query);
            int count = query.getCount();
            int i2 = 0;
            while (i2 < count) {
                query.moveToPosition(i2);
                if (query.getInt(columnIndex2) != 0) {
                    i = columnIndex;
                } else {
                    int i3 = query.getInt(columnIndex);
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    for (ForeignKeyWithSequence next : readForeignKeyFieldMappings) {
                        int i4 = columnIndex;
                        if (next.mId == i3) {
                            arrayList.add(next.mFrom);
                            arrayList2.add(next.mTo);
                        }
                        String str = tableName;
                        columnIndex = i4;
                    }
                    i = columnIndex;
                    hashSet.add(new ForeignKey(query.getString(columnIndex3), query.getString(columnIndex4), query.getString(columnIndex5), arrayList, arrayList2));
                }
                i2++;
                String str2 = tableName;
                columnIndex = i;
            }
            int i5 = columnIndex;
            return hashSet;
        } finally {
            query.close();
        }
    }

    private static Index readIndex(SupportSQLiteDatabase database, String name2, boolean unique) {
        Cursor query = database.query("PRAGMA index_xinfo(`" + name2 + "`)");
        try {
            int columnIndex = query.getColumnIndex("seqno");
            int columnIndex2 = query.getColumnIndex("cid");
            int columnIndex3 = query.getColumnIndex("name");
            if (!(columnIndex == -1 || columnIndex2 == -1)) {
                if (columnIndex3 != -1) {
                    TreeMap treeMap = new TreeMap();
                    while (query.moveToNext()) {
                        if (query.getInt(columnIndex2) >= 0) {
                            int i = query.getInt(columnIndex);
                            treeMap.put(Integer.valueOf(i), query.getString(columnIndex3));
                        }
                    }
                    ArrayList arrayList = new ArrayList(treeMap.size());
                    arrayList.addAll(treeMap.values());
                    Index index = new Index(name2, unique, arrayList);
                    query.close();
                    return index;
                }
            }
            return null;
        } finally {
            query.close();
        }
    }

    private static Set<Index> readIndices(SupportSQLiteDatabase database, String tableName) {
        Cursor query = database.query("PRAGMA index_list(`" + tableName + "`)");
        try {
            int columnIndex = query.getColumnIndex("name");
            int columnIndex2 = query.getColumnIndex("origin");
            int columnIndex3 = query.getColumnIndex("unique");
            if (!(columnIndex == -1 || columnIndex2 == -1)) {
                if (columnIndex3 != -1) {
                    HashSet hashSet = new HashSet();
                    while (query.moveToNext()) {
                        if ("c".equals(query.getString(columnIndex2))) {
                            String string = query.getString(columnIndex);
                            boolean z = true;
                            if (query.getInt(columnIndex3) != 1) {
                                z = false;
                            }
                            Index readIndex = readIndex(database, string, z);
                            if (readIndex == null) {
                                query.close();
                                return null;
                            }
                            hashSet.add(readIndex);
                        }
                    }
                    query.close();
                    return hashSet;
                }
            }
            return null;
        } finally {
            query.close();
        }
    }

    public boolean equals(Object o) {
        Set<Index> set;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableInfo tableInfo = (TableInfo) o;
        String str = this.name;
        if (str == null ? tableInfo.name != null : !str.equals(tableInfo.name)) {
            return false;
        }
        Map<String, Column> map = this.columns;
        if (map == null ? tableInfo.columns != null : !map.equals(tableInfo.columns)) {
            return false;
        }
        Set<ForeignKey> set2 = this.foreignKeys;
        if (set2 == null ? tableInfo.foreignKeys != null : !set2.equals(tableInfo.foreignKeys)) {
            return false;
        }
        Set<Index> set3 = this.indices;
        if (set3 == null || (set = tableInfo.indices) == null) {
            return true;
        }
        return set3.equals(set);
    }

    public int hashCode() {
        String str = this.name;
        int i = 0;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        Map<String, Column> map = this.columns;
        int hashCode2 = (hashCode + (map != null ? map.hashCode() : 0)) * 31;
        Set<ForeignKey> set = this.foreignKeys;
        if (set != null) {
            i = set.hashCode();
        }
        return hashCode2 + i;
    }

    public String toString() {
        return "TableInfo{name='" + this.name + '\'' + ", columns=" + this.columns + ", foreignKeys=" + this.foreignKeys + ", indices=" + this.indices + '}';
    }
}

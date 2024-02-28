package androidx.work.impl.model;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.collection.ArrayMap;
import androidx.lifecycle.LiveData;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.work.Data;
import androidx.work.impl.model.WorkSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public final class RawWorkInfoDao_Impl implements RawWorkInfoDao {
    /* access modifiers changed from: private */
    public final RoomDatabase __db;

    public RawWorkInfoDao_Impl(RoomDatabase __db2) {
        this.__db = __db2;
    }

    /* access modifiers changed from: private */
    public void __fetchRelationshipWorkProgressAsandroidxWorkData(ArrayMap<String, ArrayList<Data>> arrayMap) {
        ArrayList arrayList;
        Set<String> keySet = arrayMap.keySet();
        if (!keySet.isEmpty()) {
            if (arrayMap.size() > 999) {
                ArrayMap arrayMap2 = new ArrayMap((int) RoomDatabase.MAX_BIND_PARAMETER_CNT);
                int i = 0;
                int i2 = 0;
                int size = arrayMap.size();
                while (i2 < size) {
                    arrayMap2.put(arrayMap.keyAt(i2), arrayMap.valueAt(i2));
                    i2++;
                    i++;
                    if (i == 999) {
                        __fetchRelationshipWorkProgressAsandroidxWorkData(arrayMap2);
                        arrayMap2 = new ArrayMap((int) RoomDatabase.MAX_BIND_PARAMETER_CNT);
                        i = 0;
                    }
                }
                if (i > 0) {
                    __fetchRelationshipWorkProgressAsandroidxWorkData(arrayMap2);
                    return;
                }
                return;
            }
            StringBuilder newStringBuilder = StringUtil.newStringBuilder();
            newStringBuilder.append("SELECT `progress`,`work_spec_id` FROM `WorkProgress` WHERE `work_spec_id` IN (");
            int size2 = keySet.size();
            StringUtil.appendPlaceholders(newStringBuilder, size2);
            newStringBuilder.append(")");
            RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(newStringBuilder.toString(), size2 + 0);
            int i3 = 1;
            for (String next : keySet) {
                if (next == null) {
                    acquire.bindNull(i3);
                } else {
                    acquire.bindString(i3, next);
                }
                i3++;
            }
            Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
            try {
                int columnIndex = CursorUtil.getColumnIndex(query, "work_spec_id");
                if (columnIndex != -1) {
                    while (query.moveToNext()) {
                        if (!query.isNull(columnIndex) && (arrayList = arrayMap.get(query.getString(columnIndex))) != null) {
                            arrayList.add(Data.fromByteArray(query.getBlob(0)));
                        }
                    }
                    query.close();
                }
            } finally {
                query.close();
            }
        }
    }

    /* access modifiers changed from: private */
    public void __fetchRelationshipWorkTagAsjavaLangString(ArrayMap<String, ArrayList<String>> arrayMap) {
        ArrayList arrayList;
        Set<String> keySet = arrayMap.keySet();
        if (!keySet.isEmpty()) {
            if (arrayMap.size() > 999) {
                ArrayMap arrayMap2 = new ArrayMap((int) RoomDatabase.MAX_BIND_PARAMETER_CNT);
                int i = 0;
                int i2 = 0;
                int size = arrayMap.size();
                while (i2 < size) {
                    arrayMap2.put(arrayMap.keyAt(i2), arrayMap.valueAt(i2));
                    i2++;
                    i++;
                    if (i == 999) {
                        __fetchRelationshipWorkTagAsjavaLangString(arrayMap2);
                        arrayMap2 = new ArrayMap((int) RoomDatabase.MAX_BIND_PARAMETER_CNT);
                        i = 0;
                    }
                }
                if (i > 0) {
                    __fetchRelationshipWorkTagAsjavaLangString(arrayMap2);
                    return;
                }
                return;
            }
            StringBuilder newStringBuilder = StringUtil.newStringBuilder();
            newStringBuilder.append("SELECT `tag`,`work_spec_id` FROM `WorkTag` WHERE `work_spec_id` IN (");
            int size2 = keySet.size();
            StringUtil.appendPlaceholders(newStringBuilder, size2);
            newStringBuilder.append(")");
            RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(newStringBuilder.toString(), size2 + 0);
            int i3 = 1;
            for (String next : keySet) {
                if (next == null) {
                    acquire.bindNull(i3);
                } else {
                    acquire.bindString(i3, next);
                }
                i3++;
            }
            Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
            try {
                int columnIndex = CursorUtil.getColumnIndex(query, "work_spec_id");
                if (columnIndex != -1) {
                    while (query.moveToNext()) {
                        if (!query.isNull(columnIndex) && (arrayList = arrayMap.get(query.getString(columnIndex))) != null) {
                            arrayList.add(query.getString(0));
                        }
                    }
                    query.close();
                }
            } finally {
                query.close();
            }
        }
    }

    public List<WorkSpec.WorkInfoPojo> getWorkInfoPojos(SupportSQLiteQuery query) {
        this.__db.assertNotSuspendingTransaction();
        Cursor query2 = DBUtil.query(this.__db, query, true, (CancellationSignal) null);
        try {
            int columnIndex = CursorUtil.getColumnIndex(query2, "id");
            int columnIndex2 = CursorUtil.getColumnIndex(query2, "state");
            int columnIndex3 = CursorUtil.getColumnIndex(query2, "output");
            int columnIndex4 = CursorUtil.getColumnIndex(query2, "run_attempt_count");
            ArrayMap arrayMap = new ArrayMap();
            ArrayMap arrayMap2 = new ArrayMap();
            while (query2.moveToNext()) {
                if (!query2.isNull(columnIndex)) {
                    String string = query2.getString(columnIndex);
                    if (((ArrayList) arrayMap.get(string)) == null) {
                        arrayMap.put(string, new ArrayList());
                    }
                }
                if (!query2.isNull(columnIndex)) {
                    String string2 = query2.getString(columnIndex);
                    if (((ArrayList) arrayMap2.get(string2)) == null) {
                        arrayMap2.put(string2, new ArrayList());
                    }
                }
            }
            query2.moveToPosition(-1);
            __fetchRelationshipWorkTagAsjavaLangString(arrayMap);
            __fetchRelationshipWorkProgressAsandroidxWorkData(arrayMap2);
            ArrayList arrayList = new ArrayList(query2.getCount());
            while (query2.moveToNext()) {
                ArrayList arrayList2 = null;
                if (!query2.isNull(columnIndex)) {
                    arrayList2 = (ArrayList) arrayMap.get(query2.getString(columnIndex));
                }
                if (arrayList2 == null) {
                    arrayList2 = new ArrayList();
                }
                ArrayList arrayList3 = null;
                if (!query2.isNull(columnIndex)) {
                    arrayList3 = (ArrayList) arrayMap2.get(query2.getString(columnIndex));
                }
                if (arrayList3 == null) {
                    arrayList3 = new ArrayList();
                }
                WorkSpec.WorkInfoPojo workInfoPojo = new WorkSpec.WorkInfoPojo();
                if (columnIndex != -1) {
                    workInfoPojo.id = query2.getString(columnIndex);
                }
                if (columnIndex2 != -1) {
                    workInfoPojo.state = WorkTypeConverters.intToState(query2.getInt(columnIndex2));
                }
                if (columnIndex3 != -1) {
                    workInfoPojo.output = Data.fromByteArray(query2.getBlob(columnIndex3));
                }
                if (columnIndex4 != -1) {
                    workInfoPojo.runAttemptCount = query2.getInt(columnIndex4);
                }
                workInfoPojo.tags = arrayList2;
                workInfoPojo.progress = arrayList3;
                arrayList.add(workInfoPojo);
            }
            return arrayList;
        } finally {
            query2.close();
        }
    }

    public LiveData<List<WorkSpec.WorkInfoPojo>> getWorkInfoPojosLiveData(SupportSQLiteQuery query) {
        final SupportSQLiteQuery supportSQLiteQuery = query;
        return this.__db.getInvalidationTracker().createLiveData(new String[]{"WorkTag", "WorkProgress", "WorkSpec"}, false, new Callable<List<WorkSpec.WorkInfoPojo>>() {
            public List<WorkSpec.WorkInfoPojo> call() throws Exception {
                Cursor query = DBUtil.query(RawWorkInfoDao_Impl.this.__db, supportSQLiteQuery, true, (CancellationSignal) null);
                try {
                    int columnIndex = CursorUtil.getColumnIndex(query, "id");
                    int columnIndex2 = CursorUtil.getColumnIndex(query, "state");
                    int columnIndex3 = CursorUtil.getColumnIndex(query, "output");
                    int columnIndex4 = CursorUtil.getColumnIndex(query, "run_attempt_count");
                    ArrayMap arrayMap = new ArrayMap();
                    ArrayMap arrayMap2 = new ArrayMap();
                    while (query.moveToNext()) {
                        if (!query.isNull(columnIndex)) {
                            String string = query.getString(columnIndex);
                            if (((ArrayList) arrayMap.get(string)) == null) {
                                arrayMap.put(string, new ArrayList());
                            }
                        }
                        if (!query.isNull(columnIndex)) {
                            String string2 = query.getString(columnIndex);
                            if (((ArrayList) arrayMap2.get(string2)) == null) {
                                arrayMap2.put(string2, new ArrayList());
                            }
                        }
                    }
                    query.moveToPosition(-1);
                    RawWorkInfoDao_Impl.this.__fetchRelationshipWorkTagAsjavaLangString(arrayMap);
                    RawWorkInfoDao_Impl.this.__fetchRelationshipWorkProgressAsandroidxWorkData(arrayMap2);
                    ArrayList arrayList = new ArrayList(query.getCount());
                    while (query.moveToNext()) {
                        ArrayList arrayList2 = null;
                        if (!query.isNull(columnIndex)) {
                            arrayList2 = (ArrayList) arrayMap.get(query.getString(columnIndex));
                        }
                        if (arrayList2 == null) {
                            arrayList2 = new ArrayList();
                        }
                        ArrayList arrayList3 = null;
                        if (!query.isNull(columnIndex)) {
                            arrayList3 = (ArrayList) arrayMap2.get(query.getString(columnIndex));
                        }
                        if (arrayList3 == null) {
                            arrayList3 = new ArrayList();
                        }
                        WorkSpec.WorkInfoPojo workInfoPojo = new WorkSpec.WorkInfoPojo();
                        if (columnIndex != -1) {
                            workInfoPojo.id = query.getString(columnIndex);
                        }
                        if (columnIndex2 != -1) {
                            workInfoPojo.state = WorkTypeConverters.intToState(query.getInt(columnIndex2));
                        }
                        if (columnIndex3 != -1) {
                            workInfoPojo.output = Data.fromByteArray(query.getBlob(columnIndex3));
                        }
                        if (columnIndex4 != -1) {
                            workInfoPojo.runAttemptCount = query.getInt(columnIndex4);
                        }
                        workInfoPojo.tags = arrayList2;
                        workInfoPojo.progress = arrayList3;
                        arrayList.add(workInfoPojo);
                    }
                    return arrayList;
                } finally {
                    query.close();
                }
            }
        });
    }
}

package androidx.work.impl.utils;

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.work.WorkInfo;
import androidx.work.WorkQuery;
import androidx.work.impl.model.WorkTypeConverters;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class RawQueries {
    private RawQueries() {
    }

    private static void bindings(StringBuilder builder, int count) {
        if (count > 0) {
            builder.append("?");
            for (int i = 1; i < count; i++) {
                builder.append(",");
                builder.append("?");
            }
        }
    }

    public static SupportSQLiteQuery workQueryToRawQuery(WorkQuery querySpec) {
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder("SELECT * FROM workspec");
        String str = " WHERE";
        List<WorkInfo.State> states = querySpec.getStates();
        if (!states.isEmpty()) {
            ArrayList arrayList2 = new ArrayList(states.size());
            for (WorkInfo.State stateToInt : states) {
                arrayList2.add(Integer.valueOf(WorkTypeConverters.stateToInt(stateToInt)));
            }
            sb.append(str).append(" state IN (");
            bindings(sb, arrayList2.size());
            sb.append(")");
            arrayList.addAll(arrayList2);
            str = " AND";
        }
        List<UUID> ids = querySpec.getIds();
        if (!ids.isEmpty()) {
            ArrayList arrayList3 = new ArrayList(ids.size());
            for (UUID uuid : ids) {
                arrayList3.add(uuid.toString());
            }
            sb.append(str).append(" id IN (");
            bindings(sb, ids.size());
            sb.append(")");
            arrayList.addAll(arrayList3);
            str = " AND";
        }
        List<String> tags = querySpec.getTags();
        if (!tags.isEmpty()) {
            sb.append(str).append(" id IN (SELECT work_spec_id FROM worktag WHERE tag IN (");
            bindings(sb, tags.size());
            sb.append("))");
            arrayList.addAll(tags);
            str = " AND";
        }
        List<String> uniqueWorkNames = querySpec.getUniqueWorkNames();
        if (!uniqueWorkNames.isEmpty()) {
            sb.append(str).append(" id IN (SELECT work_spec_id FROM workname WHERE name IN (");
            bindings(sb, uniqueWorkNames.size());
            sb.append("))");
            arrayList.addAll(uniqueWorkNames);
        }
        sb.append(";");
        return new SimpleSQLiteQuery(sb.toString(), arrayList.toArray());
    }
}

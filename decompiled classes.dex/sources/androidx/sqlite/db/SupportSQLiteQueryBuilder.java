package androidx.sqlite.db;

import java.util.regex.Pattern;

public final class SupportSQLiteQueryBuilder {
    private static final Pattern sLimitPattern = Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");
    private Object[] mBindArgs;
    private String[] mColumns = null;
    private boolean mDistinct = false;
    private String mGroupBy = null;
    private String mHaving = null;
    private String mLimit = null;
    private String mOrderBy = null;
    private String mSelection;
    private final String mTable;

    private SupportSQLiteQueryBuilder(String table) {
        this.mTable = table;
    }

    private static void appendClause(StringBuilder s, String name, String clause) {
        if (!isEmpty(clause)) {
            s.append(name);
            s.append(clause);
        }
    }

    private static void appendColumns(StringBuilder s, String[] columns) {
        int length = columns.length;
        for (int i = 0; i < length; i++) {
            String str = columns[i];
            if (i > 0) {
                s.append(", ");
            }
            s.append(str);
        }
        s.append(' ');
    }

    public static SupportSQLiteQueryBuilder builder(String tableName) {
        return new SupportSQLiteQueryBuilder(tableName);
    }

    private static boolean isEmpty(String input) {
        return input == null || input.length() == 0;
    }

    public SupportSQLiteQueryBuilder columns(String[] columns) {
        this.mColumns = columns;
        return this;
    }

    public SupportSQLiteQuery create() {
        if (!isEmpty(this.mGroupBy) || isEmpty(this.mHaving)) {
            StringBuilder sb = new StringBuilder(120);
            sb.append("SELECT ");
            if (this.mDistinct) {
                sb.append("DISTINCT ");
            }
            String[] strArr = this.mColumns;
            if (strArr == null || strArr.length == 0) {
                sb.append(" * ");
            } else {
                appendColumns(sb, strArr);
            }
            sb.append(" FROM ");
            sb.append(this.mTable);
            appendClause(sb, " WHERE ", this.mSelection);
            appendClause(sb, " GROUP BY ", this.mGroupBy);
            appendClause(sb, " HAVING ", this.mHaving);
            appendClause(sb, " ORDER BY ", this.mOrderBy);
            appendClause(sb, " LIMIT ", this.mLimit);
            return new SimpleSQLiteQuery(sb.toString(), this.mBindArgs);
        }
        throw new IllegalArgumentException("HAVING clauses are only permitted when using a groupBy clause");
    }

    public SupportSQLiteQueryBuilder distinct() {
        this.mDistinct = true;
        return this;
    }

    public SupportSQLiteQueryBuilder groupBy(String groupBy) {
        this.mGroupBy = groupBy;
        return this;
    }

    public SupportSQLiteQueryBuilder having(String having) {
        this.mHaving = having;
        return this;
    }

    public SupportSQLiteQueryBuilder limit(String limit) {
        if (isEmpty(limit) || sLimitPattern.matcher(limit).matches()) {
            this.mLimit = limit;
            return this;
        }
        throw new IllegalArgumentException("invalid LIMIT clauses:" + limit);
    }

    public SupportSQLiteQueryBuilder orderBy(String orderBy) {
        this.mOrderBy = orderBy;
        return this;
    }

    public SupportSQLiteQueryBuilder selection(String selection, Object[] bindArgs) {
        this.mSelection = selection;
        this.mBindArgs = bindArgs;
        return this;
    }
}

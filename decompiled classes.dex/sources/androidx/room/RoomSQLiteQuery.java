package androidx.room;

import androidx.sqlite.db.SupportSQLiteProgram;
import androidx.sqlite.db.SupportSQLiteQuery;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class RoomSQLiteQuery implements SupportSQLiteQuery, SupportSQLiteProgram {
    private static final int BLOB = 5;
    static final int DESIRED_POOL_SIZE = 10;
    private static final int DOUBLE = 3;
    private static final int LONG = 2;
    private static final int NULL = 1;
    static final int POOL_LIMIT = 15;
    private static final int STRING = 4;
    static final TreeMap<Integer, RoomSQLiteQuery> sQueryPool = new TreeMap<>();
    int mArgCount;
    private final int[] mBindingTypes;
    final byte[][] mBlobBindings;
    final int mCapacity;
    final double[] mDoubleBindings;
    final long[] mLongBindings;
    private volatile String mQuery;
    final String[] mStringBindings;

    private RoomSQLiteQuery(int capacity) {
        this.mCapacity = capacity;
        int i = capacity + 1;
        this.mBindingTypes = new int[i];
        this.mLongBindings = new long[i];
        this.mDoubleBindings = new double[i];
        this.mStringBindings = new String[i];
        this.mBlobBindings = new byte[i][];
    }

    public static RoomSQLiteQuery acquire(String query, int argumentCount) {
        TreeMap<Integer, RoomSQLiteQuery> treeMap = sQueryPool;
        synchronized (treeMap) {
            Map.Entry<Integer, RoomSQLiteQuery> ceilingEntry = treeMap.ceilingEntry(Integer.valueOf(argumentCount));
            if (ceilingEntry != null) {
                treeMap.remove(ceilingEntry.getKey());
                RoomSQLiteQuery value = ceilingEntry.getValue();
                value.init(query, argumentCount);
                return value;
            }
            RoomSQLiteQuery roomSQLiteQuery = new RoomSQLiteQuery(argumentCount);
            roomSQLiteQuery.init(query, argumentCount);
            return roomSQLiteQuery;
        }
    }

    public static RoomSQLiteQuery copyFrom(SupportSQLiteQuery supportSQLiteQuery) {
        RoomSQLiteQuery acquire = acquire(supportSQLiteQuery.getSql(), supportSQLiteQuery.getArgCount());
        supportSQLiteQuery.bindTo(new SupportSQLiteProgram(acquire) {
            final /* synthetic */ RoomSQLiteQuery val$query;

            {
                this.val$query = r1;
            }

            public void bindBlob(int index, byte[] value) {
                this.val$query.bindBlob(index, value);
            }

            public void bindDouble(int index, double value) {
                this.val$query.bindDouble(index, value);
            }

            public void bindLong(int index, long value) {
                this.val$query.bindLong(index, value);
            }

            public void bindNull(int index) {
                this.val$query.bindNull(index);
            }

            public void bindString(int index, String value) {
                this.val$query.bindString(index, value);
            }

            public void clearBindings() {
                this.val$query.clearBindings();
            }

            public void close() {
            }
        });
        return acquire;
    }

    private static void prunePoolLocked() {
        TreeMap<Integer, RoomSQLiteQuery> treeMap = sQueryPool;
        if (treeMap.size() > 15) {
            int size = treeMap.size() - 10;
            Iterator<Integer> it = treeMap.descendingKeySet().iterator();
            while (true) {
                int i = size - 1;
                if (size > 0) {
                    it.next();
                    it.remove();
                    size = i;
                } else {
                    return;
                }
            }
        }
    }

    public void bindBlob(int index, byte[] value) {
        this.mBindingTypes[index] = 5;
        this.mBlobBindings[index] = value;
    }

    public void bindDouble(int index, double value) {
        this.mBindingTypes[index] = 3;
        this.mDoubleBindings[index] = value;
    }

    public void bindLong(int index, long value) {
        this.mBindingTypes[index] = 2;
        this.mLongBindings[index] = value;
    }

    public void bindNull(int index) {
        this.mBindingTypes[index] = 1;
    }

    public void bindString(int index, String value) {
        this.mBindingTypes[index] = 4;
        this.mStringBindings[index] = value;
    }

    public void bindTo(SupportSQLiteProgram program) {
        for (int i = 1; i <= this.mArgCount; i++) {
            switch (this.mBindingTypes[i]) {
                case 1:
                    program.bindNull(i);
                    break;
                case 2:
                    program.bindLong(i, this.mLongBindings[i]);
                    break;
                case 3:
                    program.bindDouble(i, this.mDoubleBindings[i]);
                    break;
                case 4:
                    program.bindString(i, this.mStringBindings[i]);
                    break;
                case 5:
                    program.bindBlob(i, this.mBlobBindings[i]);
                    break;
            }
        }
    }

    public void clearBindings() {
        Arrays.fill(this.mBindingTypes, 1);
        Arrays.fill(this.mStringBindings, (Object) null);
        Arrays.fill(this.mBlobBindings, (Object) null);
        this.mQuery = null;
    }

    public void close() {
    }

    public void copyArgumentsFrom(RoomSQLiteQuery other) {
        int argCount = other.getArgCount() + 1;
        System.arraycopy(other.mBindingTypes, 0, this.mBindingTypes, 0, argCount);
        System.arraycopy(other.mLongBindings, 0, this.mLongBindings, 0, argCount);
        System.arraycopy(other.mStringBindings, 0, this.mStringBindings, 0, argCount);
        System.arraycopy(other.mBlobBindings, 0, this.mBlobBindings, 0, argCount);
        System.arraycopy(other.mDoubleBindings, 0, this.mDoubleBindings, 0, argCount);
    }

    public int getArgCount() {
        return this.mArgCount;
    }

    public String getSql() {
        return this.mQuery;
    }

    /* access modifiers changed from: package-private */
    public void init(String query, int argCount) {
        this.mQuery = query;
        this.mArgCount = argCount;
    }

    public void release() {
        TreeMap<Integer, RoomSQLiteQuery> treeMap = sQueryPool;
        synchronized (treeMap) {
            treeMap.put(Integer.valueOf(this.mCapacity), this);
            prunePoolLocked();
        }
    }
}

package androidx.collection;

public class LongSparseArray<E> implements Cloneable {
    private static final Object DELETED = new Object();
    private boolean mGarbage;
    private long[] mKeys;
    private int mSize;
    private Object[] mValues;

    public LongSparseArray() {
        this(10);
    }

    public LongSparseArray(int initialCapacity) {
        this.mGarbage = false;
        if (initialCapacity == 0) {
            this.mKeys = ContainerHelpers.EMPTY_LONGS;
            this.mValues = ContainerHelpers.EMPTY_OBJECTS;
            return;
        }
        int initialCapacity2 = ContainerHelpers.idealLongArraySize(initialCapacity);
        this.mKeys = new long[initialCapacity2];
        this.mValues = new Object[initialCapacity2];
    }

    private void gc() {
        int i = this.mSize;
        int i2 = 0;
        long[] jArr = this.mKeys;
        Object[] objArr = this.mValues;
        for (int i3 = 0; i3 < i; i3++) {
            Object obj = objArr[i3];
            if (obj != DELETED) {
                if (i3 != i2) {
                    jArr[i2] = jArr[i3];
                    objArr[i2] = obj;
                    objArr[i3] = null;
                }
                i2++;
            }
        }
        this.mGarbage = false;
        this.mSize = i2;
    }

    public void append(long key, E e) {
        int i = this.mSize;
        if (i == 0 || key > this.mKeys[i - 1]) {
            if (this.mGarbage && i >= this.mKeys.length) {
                gc();
            }
            int i2 = this.mSize;
            if (i2 >= this.mKeys.length) {
                int idealLongArraySize = ContainerHelpers.idealLongArraySize(i2 + 1);
                long[] jArr = new long[idealLongArraySize];
                Object[] objArr = new Object[idealLongArraySize];
                long[] jArr2 = this.mKeys;
                System.arraycopy(jArr2, 0, jArr, 0, jArr2.length);
                Object[] objArr2 = this.mValues;
                System.arraycopy(objArr2, 0, objArr, 0, objArr2.length);
                this.mKeys = jArr;
                this.mValues = objArr;
            }
            this.mKeys[i2] = key;
            this.mValues[i2] = e;
            this.mSize = i2 + 1;
            return;
        }
        put(key, e);
    }

    public void clear() {
        int i = this.mSize;
        Object[] objArr = this.mValues;
        for (int i2 = 0; i2 < i; i2++) {
            objArr[i2] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }

    public LongSparseArray<E> clone() {
        try {
            LongSparseArray<E> longSparseArray = (LongSparseArray) super.clone();
            longSparseArray.mKeys = (long[]) this.mKeys.clone();
            longSparseArray.mValues = (Object[]) this.mValues.clone();
            return longSparseArray;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public boolean containsKey(long key) {
        return indexOfKey(key) >= 0;
    }

    public boolean containsValue(E e) {
        return indexOfValue(e) >= 0;
    }

    @Deprecated
    public void delete(long key) {
        remove(key);
    }

    public E get(long key) {
        return get(key, (Object) null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x000a, code lost:
        r1 = r3.mValues[r0];
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public E get(long r4, E r6) {
        /*
            r3 = this;
            long[] r0 = r3.mKeys
            int r1 = r3.mSize
            int r0 = androidx.collection.ContainerHelpers.binarySearch((long[]) r0, (int) r1, (long) r4)
            if (r0 < 0) goto L_0x0014
            java.lang.Object[] r1 = r3.mValues
            r1 = r1[r0]
            java.lang.Object r2 = DELETED
            if (r1 != r2) goto L_0x0013
            goto L_0x0014
        L_0x0013:
            return r1
        L_0x0014:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.collection.LongSparseArray.get(long, java.lang.Object):java.lang.Object");
    }

    public int indexOfKey(long key) {
        if (this.mGarbage) {
            gc();
        }
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
    }

    public int indexOfValue(E e) {
        if (this.mGarbage) {
            gc();
        }
        for (int i = 0; i < this.mSize; i++) {
            if (this.mValues[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public long keyAt(int index) {
        if (this.mGarbage) {
            gc();
        }
        return this.mKeys[index];
    }

    public void put(long key, E e) {
        int binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        if (binarySearch >= 0) {
            this.mValues[binarySearch] = e;
            return;
        }
        int i = ~binarySearch;
        int i2 = this.mSize;
        if (i < i2) {
            Object[] objArr = this.mValues;
            if (objArr[i] == DELETED) {
                this.mKeys[i] = key;
                objArr[i] = e;
                return;
            }
        }
        if (this.mGarbage && i2 >= this.mKeys.length) {
            gc();
            i = ~ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        }
        int i3 = this.mSize;
        if (i3 >= this.mKeys.length) {
            int idealLongArraySize = ContainerHelpers.idealLongArraySize(i3 + 1);
            long[] jArr = new long[idealLongArraySize];
            Object[] objArr2 = new Object[idealLongArraySize];
            long[] jArr2 = this.mKeys;
            System.arraycopy(jArr2, 0, jArr, 0, jArr2.length);
            Object[] objArr3 = this.mValues;
            System.arraycopy(objArr3, 0, objArr2, 0, objArr3.length);
            this.mKeys = jArr;
            this.mValues = objArr2;
        }
        int i4 = this.mSize;
        if (i4 - i != 0) {
            long[] jArr3 = this.mKeys;
            System.arraycopy(jArr3, i, jArr3, i + 1, i4 - i);
            Object[] objArr4 = this.mValues;
            System.arraycopy(objArr4, i, objArr4, i + 1, this.mSize - i);
        }
        this.mKeys[i] = key;
        this.mValues[i] = e;
        this.mSize++;
    }

    public void putAll(LongSparseArray<? extends E> longSparseArray) {
        int size = longSparseArray.size();
        for (int i = 0; i < size; i++) {
            put(longSparseArray.keyAt(i), longSparseArray.valueAt(i));
        }
    }

    public E putIfAbsent(long key, E e) {
        E e2 = get(key);
        if (e2 == null) {
            put(key, e);
        }
        return e2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x000a, code lost:
        r1 = r4.mValues;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void remove(long r5) {
        /*
            r4 = this;
            long[] r0 = r4.mKeys
            int r1 = r4.mSize
            int r0 = androidx.collection.ContainerHelpers.binarySearch((long[]) r0, (int) r1, (long) r5)
            if (r0 < 0) goto L_0x0017
            java.lang.Object[] r1 = r4.mValues
            r2 = r1[r0]
            java.lang.Object r3 = DELETED
            if (r2 == r3) goto L_0x0017
            r1[r0] = r3
            r1 = 1
            r4.mGarbage = r1
        L_0x0017:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.collection.LongSparseArray.remove(long):void");
    }

    public boolean remove(long key, Object value) {
        int indexOfKey = indexOfKey(key);
        if (indexOfKey < 0) {
            return false;
        }
        Object valueAt = valueAt(indexOfKey);
        if (value != valueAt && (value == null || !value.equals(valueAt))) {
            return false;
        }
        removeAt(indexOfKey);
        return true;
    }

    public void removeAt(int index) {
        Object[] objArr = this.mValues;
        Object obj = objArr[index];
        Object obj2 = DELETED;
        if (obj != obj2) {
            objArr[index] = obj2;
            this.mGarbage = true;
        }
    }

    public E replace(long key, E e) {
        int indexOfKey = indexOfKey(key);
        if (indexOfKey < 0) {
            return null;
        }
        E[] eArr = this.mValues;
        E e2 = eArr[indexOfKey];
        eArr[indexOfKey] = e;
        return e2;
    }

    public boolean replace(long key, E e, E e2) {
        int indexOfKey = indexOfKey(key);
        if (indexOfKey < 0) {
            return false;
        }
        E e3 = this.mValues[indexOfKey];
        if (e3 != e && (e == null || !e.equals(e3))) {
            return false;
        }
        this.mValues[indexOfKey] = e2;
        return true;
    }

    public void setValueAt(int index, E e) {
        if (this.mGarbage) {
            gc();
        }
        this.mValues[index] = e;
    }

    public int size() {
        if (this.mGarbage) {
            gc();
        }
        return this.mSize;
    }

    public String toString() {
        if (size() <= 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.mSize * 28);
        sb.append('{');
        for (int i = 0; i < this.mSize; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(keyAt(i));
            sb.append('=');
            Object valueAt = valueAt(i);
            if (valueAt != this) {
                sb.append(valueAt);
            } else {
                sb.append("(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public E valueAt(int index) {
        if (this.mGarbage) {
            gc();
        }
        return this.mValues[index];
    }
}

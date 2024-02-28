package androidx.collection;

import java.util.ConcurrentModificationException;
import java.util.Map;

public class SimpleArrayMap<K, V> {
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean CONCURRENT_MODIFICATION_EXCEPTIONS = true;
    private static final boolean DEBUG = false;
    private static final String TAG = "ArrayMap";
    static Object[] mBaseCache;
    static int mBaseCacheSize;
    static Object[] mTwiceBaseCache;
    static int mTwiceBaseCacheSize;
    Object[] mArray;
    int[] mHashes;
    int mSize;

    public SimpleArrayMap() {
        this.mHashes = ContainerHelpers.EMPTY_INTS;
        this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        this.mSize = 0;
    }

    public SimpleArrayMap(int capacity) {
        if (capacity == 0) {
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            allocArrays(capacity);
        }
        this.mSize = 0;
    }

    public SimpleArrayMap(SimpleArrayMap<K, V> simpleArrayMap) {
        this();
        if (simpleArrayMap != null) {
            putAll(simpleArrayMap);
        }
    }

    private void allocArrays(int size) {
        Class<SimpleArrayMap> cls = SimpleArrayMap.class;
        if (size == 8) {
            synchronized (cls) {
                Object[] objArr = mTwiceBaseCache;
                if (objArr != null) {
                    this.mArray = objArr;
                    mTwiceBaseCache = (Object[]) objArr[0];
                    this.mHashes = (int[]) objArr[1];
                    objArr[1] = null;
                    objArr[0] = null;
                    mTwiceBaseCacheSize--;
                    return;
                }
            }
        } else if (size == 4) {
            synchronized (cls) {
                Object[] objArr2 = mBaseCache;
                if (objArr2 != null) {
                    this.mArray = objArr2;
                    mBaseCache = (Object[]) objArr2[0];
                    this.mHashes = (int[]) objArr2[1];
                    objArr2[1] = null;
                    objArr2[0] = null;
                    mBaseCacheSize--;
                    return;
                }
            }
        }
        this.mHashes = new int[size];
        this.mArray = new Object[(size << 1)];
    }

    private static int binarySearchHashes(int[] hashes, int N, int hash) {
        try {
            return ContainerHelpers.binarySearch(hashes, N, hash);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ConcurrentModificationException();
        }
    }

    private static void freeArrays(int[] hashes, Object[] array, int size) {
        Class<SimpleArrayMap> cls = SimpleArrayMap.class;
        if (hashes.length == 8) {
            synchronized (cls) {
                if (mTwiceBaseCacheSize < 10) {
                    array[0] = mTwiceBaseCache;
                    array[1] = hashes;
                    for (int i = (size << 1) - 1; i >= 2; i--) {
                        array[i] = null;
                    }
                    mTwiceBaseCache = array;
                    mTwiceBaseCacheSize++;
                }
            }
        } else if (hashes.length == 4) {
            synchronized (cls) {
                if (mBaseCacheSize < 10) {
                    array[0] = mBaseCache;
                    array[1] = hashes;
                    for (int i2 = (size << 1) - 1; i2 >= 2; i2--) {
                        array[i2] = null;
                    }
                    mBaseCache = array;
                    mBaseCacheSize++;
                }
            }
        }
    }

    public void clear() {
        if (this.mSize > 0) {
            int[] iArr = this.mHashes;
            Object[] objArr = this.mArray;
            int i = this.mSize;
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            this.mSize = 0;
            freeArrays(iArr, objArr, i);
        }
        if (this.mSize > 0) {
            throw new ConcurrentModificationException();
        }
    }

    public boolean containsKey(Object key) {
        if (indexOfKey(key) >= 0) {
            return CONCURRENT_MODIFICATION_EXCEPTIONS;
        }
        return false;
    }

    public boolean containsValue(Object value) {
        if (indexOfValue(value) >= 0) {
            return CONCURRENT_MODIFICATION_EXCEPTIONS;
        }
        return false;
    }

    public void ensureCapacity(int minimumCapacity) {
        int i = this.mSize;
        if (this.mHashes.length < minimumCapacity) {
            int[] iArr = this.mHashes;
            Object[] objArr = this.mArray;
            allocArrays(minimumCapacity);
            if (this.mSize > 0) {
                System.arraycopy(iArr, 0, this.mHashes, 0, i);
                System.arraycopy(objArr, 0, this.mArray, 0, i << 1);
            }
            freeArrays(iArr, objArr, i);
        }
        if (this.mSize != i) {
            throw new ConcurrentModificationException();
        }
    }

    public boolean equals(Object object) {
        if (this == object) {
            return CONCURRENT_MODIFICATION_EXCEPTIONS;
        }
        if (object instanceof SimpleArrayMap) {
            SimpleArrayMap simpleArrayMap = (SimpleArrayMap) object;
            if (size() != simpleArrayMap.size()) {
                return false;
            }
            int i = 0;
            while (i < this.mSize) {
                try {
                    Object keyAt = keyAt(i);
                    Object valueAt = valueAt(i);
                    Object obj = simpleArrayMap.get(keyAt);
                    if (valueAt == null) {
                        if (obj != null || !simpleArrayMap.containsKey(keyAt)) {
                            return false;
                        }
                    } else if (!valueAt.equals(obj)) {
                        return false;
                    }
                    i++;
                } catch (NullPointerException e) {
                    return false;
                } catch (ClassCastException e2) {
                    return false;
                }
            }
            return CONCURRENT_MODIFICATION_EXCEPTIONS;
        } else if (!(object instanceof Map)) {
            return false;
        } else {
            Map map = (Map) object;
            if (size() != map.size()) {
                return false;
            }
            int i2 = 0;
            while (i2 < this.mSize) {
                try {
                    Object keyAt2 = keyAt(i2);
                    Object valueAt2 = valueAt(i2);
                    Object obj2 = map.get(keyAt2);
                    if (valueAt2 == null) {
                        if (obj2 != null || !map.containsKey(keyAt2)) {
                            return false;
                        }
                    } else if (!valueAt2.equals(obj2)) {
                        return false;
                    }
                    i2++;
                } catch (NullPointerException e3) {
                    return false;
                } catch (ClassCastException e4) {
                    return false;
                }
            }
            return CONCURRENT_MODIFICATION_EXCEPTIONS;
        }
    }

    public V get(Object key) {
        return getOrDefault(key, (Object) null);
    }

    public V getOrDefault(Object key, V v) {
        int indexOfKey = indexOfKey(key);
        return indexOfKey >= 0 ? this.mArray[(indexOfKey << 1) + 1] : v;
    }

    public int hashCode() {
        int[] iArr = this.mHashes;
        Object[] objArr = this.mArray;
        int i = 0;
        int i2 = 0;
        int i3 = 1;
        int i4 = this.mSize;
        while (i2 < i4) {
            Object obj = objArr[i3];
            i += iArr[i2] ^ (obj == null ? 0 : obj.hashCode());
            i2++;
            i3 += 2;
        }
        return i;
    }

    /* access modifiers changed from: package-private */
    public int indexOf(Object key, int hash) {
        int i = this.mSize;
        if (i == 0) {
            return -1;
        }
        int binarySearchHashes = binarySearchHashes(this.mHashes, i, hash);
        if (binarySearchHashes < 0 || key.equals(this.mArray[binarySearchHashes << 1])) {
            return binarySearchHashes;
        }
        int i2 = binarySearchHashes + 1;
        while (i2 < i && this.mHashes[i2] == hash) {
            if (key.equals(this.mArray[i2 << 1])) {
                return i2;
            }
            i2++;
        }
        int i3 = binarySearchHashes - 1;
        while (i3 >= 0 && this.mHashes[i3] == hash) {
            if (key.equals(this.mArray[i3 << 1])) {
                return i3;
            }
            i3--;
        }
        return ~i2;
    }

    public int indexOfKey(Object key) {
        return key == null ? indexOfNull() : indexOf(key, key.hashCode());
    }

    /* access modifiers changed from: package-private */
    public int indexOfNull() {
        int i = this.mSize;
        if (i == 0) {
            return -1;
        }
        int binarySearchHashes = binarySearchHashes(this.mHashes, i, 0);
        if (binarySearchHashes < 0 || this.mArray[binarySearchHashes << 1] == null) {
            return binarySearchHashes;
        }
        int i2 = binarySearchHashes + 1;
        while (i2 < i && this.mHashes[i2] == 0) {
            if (this.mArray[i2 << 1] == null) {
                return i2;
            }
            i2++;
        }
        int i3 = binarySearchHashes - 1;
        while (i3 >= 0 && this.mHashes[i3] == 0) {
            if (this.mArray[i3 << 1] == null) {
                return i3;
            }
            i3--;
        }
        return ~i2;
    }

    /* access modifiers changed from: package-private */
    public int indexOfValue(Object value) {
        int i = this.mSize * 2;
        Object[] objArr = this.mArray;
        if (value == null) {
            for (int i2 = 1; i2 < i; i2 += 2) {
                if (objArr[i2] == null) {
                    return i2 >> 1;
                }
            }
            return -1;
        }
        for (int i3 = 1; i3 < i; i3 += 2) {
            if (value.equals(objArr[i3])) {
                return i3 >> 1;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        if (this.mSize <= 0) {
            return CONCURRENT_MODIFICATION_EXCEPTIONS;
        }
        return false;
    }

    public K keyAt(int index) {
        return this.mArray[index << 1];
    }

    public V put(K k, V v) {
        int i;
        int i2;
        int i3 = this.mSize;
        if (k == null) {
            i2 = 0;
            i = indexOfNull();
        } else {
            i2 = k.hashCode();
            i = indexOf(k, i2);
        }
        if (i >= 0) {
            int i4 = (i << 1) + 1;
            V[] vArr = this.mArray;
            V v2 = vArr[i4];
            vArr[i4] = v;
            return v2;
        }
        int i5 = ~i;
        if (i3 >= this.mHashes.length) {
            int i6 = 4;
            if (i3 >= 8) {
                i6 = (i3 >> 1) + i3;
            } else if (i3 >= 4) {
                i6 = 8;
            }
            int[] iArr = this.mHashes;
            Object[] objArr = this.mArray;
            allocArrays(i6);
            if (i3 == this.mSize) {
                int[] iArr2 = this.mHashes;
                if (iArr2.length > 0) {
                    System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
                    System.arraycopy(objArr, 0, this.mArray, 0, objArr.length);
                }
                freeArrays(iArr, objArr, i3);
            } else {
                throw new ConcurrentModificationException();
            }
        }
        if (i5 < i3) {
            int[] iArr3 = this.mHashes;
            System.arraycopy(iArr3, i5, iArr3, i5 + 1, i3 - i5);
            Object[] objArr2 = this.mArray;
            System.arraycopy(objArr2, i5 << 1, objArr2, (i5 + 1) << 1, (this.mSize - i5) << 1);
        }
        int i7 = this.mSize;
        if (i3 == i7) {
            int[] iArr4 = this.mHashes;
            if (i5 < iArr4.length) {
                iArr4[i5] = i2;
                Object[] objArr3 = this.mArray;
                objArr3[i5 << 1] = k;
                objArr3[(i5 << 1) + 1] = v;
                this.mSize = i7 + 1;
                return null;
            }
        }
        throw new ConcurrentModificationException();
    }

    public void putAll(SimpleArrayMap<? extends K, ? extends V> simpleArrayMap) {
        int i = simpleArrayMap.mSize;
        ensureCapacity(this.mSize + i);
        if (this.mSize != 0) {
            for (int i2 = 0; i2 < i; i2++) {
                put(simpleArrayMap.keyAt(i2), simpleArrayMap.valueAt(i2));
            }
        } else if (i > 0) {
            System.arraycopy(simpleArrayMap.mHashes, 0, this.mHashes, 0, i);
            System.arraycopy(simpleArrayMap.mArray, 0, this.mArray, 0, i << 1);
            this.mSize = i;
        }
    }

    public V putIfAbsent(K k, V v) {
        V v2 = get(k);
        return v2 == null ? put(k, v) : v2;
    }

    public V remove(Object key) {
        int indexOfKey = indexOfKey(key);
        if (indexOfKey >= 0) {
            return removeAt(indexOfKey);
        }
        return null;
    }

    public boolean remove(Object key, Object value) {
        int indexOfKey = indexOfKey(key);
        if (indexOfKey < 0) {
            return false;
        }
        Object valueAt = valueAt(indexOfKey);
        if (value != valueAt && (value == null || !value.equals(valueAt))) {
            return false;
        }
        removeAt(indexOfKey);
        return CONCURRENT_MODIFICATION_EXCEPTIONS;
    }

    public V removeAt(int index) {
        int i;
        V[] vArr = this.mArray;
        V v = vArr[(index << 1) + 1];
        int i2 = this.mSize;
        if (i2 <= 1) {
            freeArrays(this.mHashes, vArr, i2);
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            i = 0;
        } else {
            i = i2 - 1;
            int[] iArr = this.mHashes;
            int i3 = 8;
            if (iArr.length <= 8 || this.mSize >= iArr.length / 3) {
                if (index < i) {
                    System.arraycopy(iArr, index + 1, iArr, index, i - index);
                    Object[] objArr = this.mArray;
                    System.arraycopy(objArr, (index + 1) << 1, objArr, index << 1, (i - index) << 1);
                }
                Object[] objArr2 = this.mArray;
                objArr2[i << 1] = null;
                objArr2[(i << 1) + 1] = null;
            } else {
                if (i2 > 8) {
                    i3 = i2 + (i2 >> 1);
                }
                int i4 = i3;
                int[] iArr2 = this.mHashes;
                Object[] objArr3 = this.mArray;
                allocArrays(i4);
                if (i2 == this.mSize) {
                    if (index > 0) {
                        System.arraycopy(iArr2, 0, this.mHashes, 0, index);
                        System.arraycopy(objArr3, 0, this.mArray, 0, index << 1);
                    }
                    if (index < i) {
                        System.arraycopy(iArr2, index + 1, this.mHashes, index, i - index);
                        System.arraycopy(objArr3, (index + 1) << 1, this.mArray, index << 1, (i - index) << 1);
                    }
                } else {
                    throw new ConcurrentModificationException();
                }
            }
        }
        if (i2 == this.mSize) {
            this.mSize = i;
            return v;
        }
        throw new ConcurrentModificationException();
    }

    public V replace(K k, V v) {
        int indexOfKey = indexOfKey(k);
        if (indexOfKey >= 0) {
            return setValueAt(indexOfKey, v);
        }
        return null;
    }

    public boolean replace(K k, V v, V v2) {
        int indexOfKey = indexOfKey(k);
        if (indexOfKey < 0) {
            return false;
        }
        V valueAt = valueAt(indexOfKey);
        if (valueAt != v && (v == null || !v.equals(valueAt))) {
            return false;
        }
        setValueAt(indexOfKey, v2);
        return CONCURRENT_MODIFICATION_EXCEPTIONS;
    }

    public V setValueAt(int index, V v) {
        int i = (index << 1) + 1;
        V[] vArr = this.mArray;
        V v2 = vArr[i];
        vArr[i] = v;
        return v2;
    }

    public int size() {
        return this.mSize;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.mSize * 28);
        sb.append('{');
        for (int i = 0; i < this.mSize; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            Object keyAt = keyAt(i);
            if (keyAt != this) {
                sb.append(keyAt);
            } else {
                sb.append("(this Map)");
            }
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

    public V valueAt(int index) {
        return this.mArray[(index << 1) + 1];
    }
}

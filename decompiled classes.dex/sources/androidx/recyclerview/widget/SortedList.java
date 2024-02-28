package androidx.recyclerview.widget;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class SortedList<T> {
    private static final int CAPACITY_GROWTH = 10;
    private static final int DELETION = 2;
    private static final int INSERTION = 1;
    public static final int INVALID_POSITION = -1;
    private static final int LOOKUP = 4;
    private static final int MIN_CAPACITY = 10;
    private BatchedCallback mBatchedCallback;
    private Callback mCallback;
    T[] mData;
    private int mNewDataStart;
    private T[] mOldData;
    private int mOldDataSize;
    private int mOldDataStart;
    private int mSize;
    private final Class<T> mTClass;

    public static class BatchedCallback<T2> extends Callback<T2> {
        private final BatchingListUpdateCallback mBatchingListUpdateCallback;
        final Callback<T2> mWrappedCallback;

        public BatchedCallback(Callback<T2> callback) {
            this.mWrappedCallback = callback;
            this.mBatchingListUpdateCallback = new BatchingListUpdateCallback(callback);
        }

        public boolean areContentsTheSame(T2 t2, T2 t22) {
            return this.mWrappedCallback.areContentsTheSame(t2, t22);
        }

        public boolean areItemsTheSame(T2 t2, T2 t22) {
            return this.mWrappedCallback.areItemsTheSame(t2, t22);
        }

        public int compare(T2 t2, T2 t22) {
            return this.mWrappedCallback.compare(t2, t22);
        }

        public void dispatchLastEvent() {
            this.mBatchingListUpdateCallback.dispatchLastEvent();
        }

        public Object getChangePayload(T2 t2, T2 t22) {
            return this.mWrappedCallback.getChangePayload(t2, t22);
        }

        public void onChanged(int position, int count) {
            this.mBatchingListUpdateCallback.onChanged(position, count, (Object) null);
        }

        public void onChanged(int position, int count, Object payload) {
            this.mBatchingListUpdateCallback.onChanged(position, count, payload);
        }

        public void onInserted(int position, int count) {
            this.mBatchingListUpdateCallback.onInserted(position, count);
        }

        public void onMoved(int fromPosition, int toPosition) {
            this.mBatchingListUpdateCallback.onMoved(fromPosition, toPosition);
        }

        public void onRemoved(int position, int count) {
            this.mBatchingListUpdateCallback.onRemoved(position, count);
        }
    }

    public static abstract class Callback<T2> implements Comparator<T2>, ListUpdateCallback {
        public abstract boolean areContentsTheSame(T2 t2, T2 t22);

        public abstract boolean areItemsTheSame(T2 t2, T2 t22);

        public abstract int compare(T2 t2, T2 t22);

        public Object getChangePayload(T2 t2, T2 t22) {
            return null;
        }

        public abstract void onChanged(int i, int i2);

        public void onChanged(int position, int count, Object payload) {
            onChanged(position, count);
        }
    }

    public SortedList(Class<T> cls, Callback<T> callback) {
        this(cls, callback, 10);
    }

    public SortedList(Class<T> cls, Callback<T> callback, int initialCapacity) {
        this.mTClass = cls;
        this.mData = (Object[]) Array.newInstance(cls, initialCapacity);
        this.mCallback = callback;
        this.mSize = 0;
    }

    private int add(T t, boolean notify) {
        int findIndexOf = findIndexOf(t, this.mData, 0, this.mSize, 1);
        if (findIndexOf == -1) {
            findIndexOf = 0;
        } else if (findIndexOf < this.mSize) {
            T t2 = this.mData[findIndexOf];
            if (this.mCallback.areItemsTheSame(t2, t)) {
                if (this.mCallback.areContentsTheSame(t2, t)) {
                    this.mData[findIndexOf] = t;
                    return findIndexOf;
                }
                this.mData[findIndexOf] = t;
                Callback callback = this.mCallback;
                callback.onChanged(findIndexOf, 1, callback.getChangePayload(t2, t));
                return findIndexOf;
            }
        }
        addToData(findIndexOf, t);
        if (notify) {
            this.mCallback.onInserted(findIndexOf, 1);
        }
        return findIndexOf;
    }

    private void addAllInternal(T[] tArr) {
        if (tArr.length >= 1) {
            int sortAndDedup = sortAndDedup(tArr);
            if (this.mSize == 0) {
                this.mData = tArr;
                this.mSize = sortAndDedup;
                this.mCallback.onInserted(0, sortAndDedup);
                return;
            }
            merge(tArr, sortAndDedup);
        }
    }

    private void addToData(int index, T t) {
        int i = this.mSize;
        if (index <= i) {
            T[] tArr = this.mData;
            if (i == tArr.length) {
                T[] tArr2 = (Object[]) Array.newInstance(this.mTClass, tArr.length + 10);
                System.arraycopy(this.mData, 0, tArr2, 0, index);
                tArr2[index] = t;
                System.arraycopy(this.mData, index, tArr2, index + 1, this.mSize - index);
                this.mData = tArr2;
            } else {
                System.arraycopy(tArr, index, tArr, index + 1, i - index);
                this.mData[index] = t;
            }
            this.mSize++;
            return;
        }
        throw new IndexOutOfBoundsException("cannot add item to " + index + " because size is " + this.mSize);
    }

    private T[] copyArray(T[] tArr) {
        T[] tArr2 = (Object[]) Array.newInstance(this.mTClass, tArr.length);
        System.arraycopy(tArr, 0, tArr2, 0, tArr.length);
        return tArr2;
    }

    private int findIndexOf(T t, T[] tArr, int left, int right, int reason) {
        while (left < right) {
            int i = (left + right) / 2;
            T t2 = tArr[i];
            int compare = this.mCallback.compare(t2, t);
            if (compare < 0) {
                left = i + 1;
            } else if (compare != 0) {
                right = i;
            } else if (this.mCallback.areItemsTheSame(t2, t)) {
                return i;
            } else {
                int linearEqualitySearch = linearEqualitySearch(t, i, left, right);
                return reason == 1 ? linearEqualitySearch == -1 ? i : linearEqualitySearch : linearEqualitySearch;
            }
        }
        if (reason == 1) {
            return left;
        }
        return -1;
    }

    private int findSameItem(T t, T[] tArr, int from, int to) {
        for (int i = from; i < to; i++) {
            if (this.mCallback.areItemsTheSame(tArr[i], t)) {
                return i;
            }
        }
        return -1;
    }

    private int linearEqualitySearch(T t, int middle, int left, int right) {
        int i = middle - 1;
        while (i >= left) {
            T t2 = this.mData[i];
            if (this.mCallback.compare(t2, t) != 0) {
                break;
            } else if (this.mCallback.areItemsTheSame(t2, t)) {
                return i;
            } else {
                i--;
            }
        }
        for (int i2 = middle + 1; i2 < right; i2++) {
            T t3 = this.mData[i2];
            if (this.mCallback.compare(t3, t) != 0) {
                return -1;
            }
            if (this.mCallback.areItemsTheSame(t3, t)) {
                return i2;
            }
        }
        return -1;
    }

    private void merge(T[] tArr, int newDataSize) {
        boolean z = !(this.mCallback instanceof BatchedCallback);
        if (z) {
            beginBatchedUpdates();
        }
        this.mOldData = this.mData;
        this.mOldDataStart = 0;
        int i = this.mSize;
        this.mOldDataSize = i;
        this.mData = (Object[]) Array.newInstance(this.mTClass, i + newDataSize + 10);
        this.mNewDataStart = 0;
        int i2 = 0;
        while (true) {
            int i3 = this.mOldDataStart;
            int i4 = this.mOldDataSize;
            if (i3 >= i4 && i2 >= newDataSize) {
                break;
            } else if (i3 == i4) {
                int i5 = newDataSize - i2;
                System.arraycopy(tArr, i2, this.mData, this.mNewDataStart, i5);
                int i6 = this.mNewDataStart + i5;
                this.mNewDataStart = i6;
                this.mSize += i5;
                this.mCallback.onInserted(i6 - i5, i5);
                break;
            } else if (i2 == newDataSize) {
                int i7 = i4 - i3;
                System.arraycopy(this.mOldData, i3, this.mData, this.mNewDataStart, i7);
                this.mNewDataStart += i7;
                break;
            } else {
                T t = this.mOldData[i3];
                T t2 = tArr[i2];
                int compare = this.mCallback.compare(t, t2);
                if (compare > 0) {
                    T[] tArr2 = this.mData;
                    int i8 = this.mNewDataStart;
                    int i9 = i8 + 1;
                    this.mNewDataStart = i9;
                    tArr2[i8] = t2;
                    this.mSize++;
                    i2++;
                    this.mCallback.onInserted(i9 - 1, 1);
                } else if (compare != 0 || !this.mCallback.areItemsTheSame(t, t2)) {
                    T[] tArr3 = this.mData;
                    int i10 = this.mNewDataStart;
                    this.mNewDataStart = i10 + 1;
                    tArr3[i10] = t;
                    this.mOldDataStart++;
                } else {
                    T[] tArr4 = this.mData;
                    int i11 = this.mNewDataStart;
                    this.mNewDataStart = i11 + 1;
                    tArr4[i11] = t2;
                    i2++;
                    this.mOldDataStart++;
                    if (!this.mCallback.areContentsTheSame(t, t2)) {
                        Callback callback = this.mCallback;
                        callback.onChanged(this.mNewDataStart - 1, 1, callback.getChangePayload(t, t2));
                    }
                }
            }
        }
        this.mOldData = null;
        if (z) {
            endBatchedUpdates();
        }
    }

    private boolean remove(T t, boolean notify) {
        int findIndexOf = findIndexOf(t, this.mData, 0, this.mSize, 2);
        if (findIndexOf == -1) {
            return false;
        }
        removeItemAtIndex(findIndexOf, notify);
        return true;
    }

    private void removeItemAtIndex(int index, boolean notify) {
        T[] tArr = this.mData;
        System.arraycopy(tArr, index + 1, tArr, index, (this.mSize - index) - 1);
        int i = this.mSize - 1;
        this.mSize = i;
        this.mData[i] = null;
        if (notify) {
            this.mCallback.onRemoved(index, 1);
        }
    }

    private void replaceAllInsert(T t) {
        T[] tArr = this.mData;
        int i = this.mNewDataStart;
        tArr[i] = t;
        int i2 = i + 1;
        this.mNewDataStart = i2;
        this.mSize++;
        this.mCallback.onInserted(i2 - 1, 1);
    }

    private void replaceAllInternal(T[] tArr) {
        boolean z = !(this.mCallback instanceof BatchedCallback);
        if (z) {
            beginBatchedUpdates();
        }
        this.mOldDataStart = 0;
        this.mOldDataSize = this.mSize;
        this.mOldData = this.mData;
        this.mNewDataStart = 0;
        int sortAndDedup = sortAndDedup(tArr);
        this.mData = (Object[]) Array.newInstance(this.mTClass, sortAndDedup);
        while (true) {
            int i = this.mNewDataStart;
            if (i >= sortAndDedup && this.mOldDataStart >= this.mOldDataSize) {
                break;
            }
            int i2 = this.mOldDataStart;
            int i3 = this.mOldDataSize;
            if (i2 >= i3) {
                int i4 = this.mNewDataStart;
                int i5 = sortAndDedup - i;
                System.arraycopy(tArr, i4, this.mData, i4, i5);
                this.mNewDataStart += i5;
                this.mSize += i5;
                this.mCallback.onInserted(i4, i5);
                break;
            } else if (i >= sortAndDedup) {
                int i6 = i3 - i2;
                this.mSize -= i6;
                this.mCallback.onRemoved(i, i6);
                break;
            } else {
                T t = this.mOldData[i2];
                T t2 = tArr[i];
                int compare = this.mCallback.compare(t, t2);
                if (compare < 0) {
                    replaceAllRemove();
                } else if (compare > 0) {
                    replaceAllInsert(t2);
                } else if (!this.mCallback.areItemsTheSame(t, t2)) {
                    replaceAllRemove();
                    replaceAllInsert(t2);
                } else {
                    T[] tArr2 = this.mData;
                    int i7 = this.mNewDataStart;
                    tArr2[i7] = t2;
                    this.mOldDataStart++;
                    this.mNewDataStart = i7 + 1;
                    if (!this.mCallback.areContentsTheSame(t, t2)) {
                        Callback callback = this.mCallback;
                        callback.onChanged(this.mNewDataStart - 1, 1, callback.getChangePayload(t, t2));
                    }
                }
            }
        }
        this.mOldData = null;
        if (z) {
            endBatchedUpdates();
        }
    }

    private void replaceAllRemove() {
        this.mSize--;
        this.mOldDataStart++;
        this.mCallback.onRemoved(this.mNewDataStart, 1);
    }

    private int sortAndDedup(T[] tArr) {
        if (tArr.length == 0) {
            return 0;
        }
        Arrays.sort(tArr, this.mCallback);
        int i = 0;
        int i2 = 1;
        for (int i3 = 1; i3 < tArr.length; i3++) {
            T t = tArr[i3];
            if (this.mCallback.compare(tArr[i], t) == 0) {
                int findSameItem = findSameItem(t, tArr, i, i2);
                if (findSameItem != -1) {
                    tArr[findSameItem] = t;
                } else {
                    if (i2 != i3) {
                        tArr[i2] = t;
                    }
                    i2++;
                }
            } else {
                if (i2 != i3) {
                    tArr[i2] = t;
                }
                i = i2;
                i2++;
            }
        }
        return i2;
    }

    private void throwIfInMutationOperation() {
        if (this.mOldData != null) {
            throw new IllegalStateException("Data cannot be mutated in the middle of a batch update operation such as addAll or replaceAll.");
        }
    }

    public int add(T t) {
        throwIfInMutationOperation();
        return add(t, true);
    }

    public void addAll(Collection<T> collection) {
        addAll(collection.toArray((Object[]) Array.newInstance(this.mTClass, collection.size())), true);
    }

    public void addAll(T... tArr) {
        addAll(tArr, false);
    }

    public void addAll(T[] tArr, boolean mayModifyInput) {
        throwIfInMutationOperation();
        if (tArr.length != 0) {
            if (mayModifyInput) {
                addAllInternal(tArr);
            } else {
                addAllInternal(copyArray(tArr));
            }
        }
    }

    public void beginBatchedUpdates() {
        throwIfInMutationOperation();
        if (!(this.mCallback instanceof BatchedCallback)) {
            if (this.mBatchedCallback == null) {
                this.mBatchedCallback = new BatchedCallback(this.mCallback);
            }
            this.mCallback = this.mBatchedCallback;
        }
    }

    public void clear() {
        throwIfInMutationOperation();
        if (this.mSize != 0) {
            int i = this.mSize;
            Arrays.fill(this.mData, 0, i, (Object) null);
            this.mSize = 0;
            this.mCallback.onRemoved(0, i);
        }
    }

    public void endBatchedUpdates() {
        throwIfInMutationOperation();
        Callback callback = this.mCallback;
        if (callback instanceof BatchedCallback) {
            ((BatchedCallback) callback).dispatchLastEvent();
        }
        Callback callback2 = this.mCallback;
        BatchedCallback batchedCallback = this.mBatchedCallback;
        if (callback2 == batchedCallback) {
            this.mCallback = batchedCallback.mWrappedCallback;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000a, code lost:
        r1 = r3.mNewDataStart;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public T get(int r4) throws java.lang.IndexOutOfBoundsException {
        /*
            r3 = this;
            int r0 = r3.mSize
            if (r4 >= r0) goto L_0x001b
            if (r4 < 0) goto L_0x001b
            T[] r0 = r3.mOldData
            if (r0 == 0) goto L_0x0016
            int r1 = r3.mNewDataStart
            if (r4 < r1) goto L_0x0016
            int r1 = r4 - r1
            int r2 = r3.mOldDataStart
            int r1 = r1 + r2
            r0 = r0[r1]
            return r0
        L_0x0016:
            T[] r0 = r3.mData
            r0 = r0[r4]
            return r0
        L_0x001b:
            java.lang.IndexOutOfBoundsException r0 = new java.lang.IndexOutOfBoundsException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Asked to get item at "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r4)
            java.lang.String r2 = " but size is "
            java.lang.StringBuilder r1 = r1.append(r2)
            int r2 = r3.mSize
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.SortedList.get(int):java.lang.Object");
    }

    public int indexOf(T t) {
        if (this.mOldData != null) {
            int findIndexOf = findIndexOf(t, this.mData, 0, this.mNewDataStart, 4);
            if (findIndexOf != -1) {
                return findIndexOf;
            }
            int findIndexOf2 = findIndexOf(t, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
            if (findIndexOf2 != -1) {
                return (findIndexOf2 - this.mOldDataStart) + this.mNewDataStart;
            }
            return -1;
        }
        return findIndexOf(t, this.mData, 0, this.mSize, 4);
    }

    public void recalculatePositionOfItemAt(int index) {
        throwIfInMutationOperation();
        Object obj = get(index);
        removeItemAtIndex(index, false);
        int add = add(obj, false);
        if (index != add) {
            this.mCallback.onMoved(index, add);
        }
    }

    public boolean remove(T t) {
        throwIfInMutationOperation();
        return remove(t, true);
    }

    public T removeItemAt(int index) {
        throwIfInMutationOperation();
        T t = get(index);
        removeItemAtIndex(index, true);
        return t;
    }

    public void replaceAll(Collection<T> collection) {
        replaceAll(collection.toArray((Object[]) Array.newInstance(this.mTClass, collection.size())), true);
    }

    public void replaceAll(T... tArr) {
        replaceAll(tArr, false);
    }

    public void replaceAll(T[] tArr, boolean mayModifyInput) {
        throwIfInMutationOperation();
        if (mayModifyInput) {
            replaceAllInternal(tArr);
        } else {
            replaceAllInternal(copyArray(tArr));
        }
    }

    public int size() {
        return this.mSize;
    }

    public void updateItemAt(int index, T t) {
        throwIfInMutationOperation();
        T t2 = get(index);
        boolean z = t2 == t || !this.mCallback.areContentsTheSame(t2, t);
        if (t2 == t || this.mCallback.compare(t2, t) != 0) {
            if (z) {
                Callback callback = this.mCallback;
                callback.onChanged(index, 1, callback.getChangePayload(t2, t));
            }
            removeItemAtIndex(index, false);
            int add = add(t, false);
            if (index != add) {
                this.mCallback.onMoved(index, add);
                return;
            }
            return;
        }
        this.mData[index] = t;
        if (z) {
            Callback callback2 = this.mCallback;
            callback2.onChanged(index, 1, callback2.getChangePayload(t2, t));
        }
    }
}

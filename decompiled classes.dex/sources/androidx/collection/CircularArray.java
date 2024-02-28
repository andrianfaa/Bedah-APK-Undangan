package androidx.collection;

public final class CircularArray<E> {
    private int mCapacityBitmask;
    private E[] mElements;
    private int mHead;
    private int mTail;

    public CircularArray() {
        this(8);
    }

    public CircularArray(int minCapacity) {
        if (minCapacity < 1) {
            throw new IllegalArgumentException("capacity must be >= 1");
        } else if (minCapacity <= 1073741824) {
            int highestOneBit = Integer.bitCount(minCapacity) != 1 ? Integer.highestOneBit(minCapacity - 1) << 1 : minCapacity;
            this.mCapacityBitmask = highestOneBit - 1;
            this.mElements = (Object[]) new Object[highestOneBit];
        } else {
            throw new IllegalArgumentException("capacity must be <= 2^30");
        }
    }

    private void doubleCapacity() {
        E[] eArr = this.mElements;
        int length = eArr.length;
        int i = this.mHead;
        int i2 = length - i;
        int i3 = length << 1;
        if (i3 >= 0) {
            E[] eArr2 = new Object[i3];
            System.arraycopy(eArr, i, eArr2, 0, i2);
            System.arraycopy(this.mElements, 0, eArr2, i2, this.mHead);
            this.mElements = (Object[]) eArr2;
            this.mHead = 0;
            this.mTail = length;
            this.mCapacityBitmask = i3 - 1;
            return;
        }
        throw new RuntimeException("Max array capacity exceeded");
    }

    public void addFirst(E e) {
        int i = (this.mHead - 1) & this.mCapacityBitmask;
        this.mHead = i;
        this.mElements[i] = e;
        if (i == this.mTail) {
            doubleCapacity();
        }
    }

    public void addLast(E e) {
        E[] eArr = this.mElements;
        int i = this.mTail;
        eArr[i] = e;
        int i2 = this.mCapacityBitmask & (i + 1);
        this.mTail = i2;
        if (i2 == this.mHead) {
            doubleCapacity();
        }
    }

    public void clear() {
        removeFromStart(size());
    }

    public E get(int n) {
        if (n >= 0 && n < size()) {
            return this.mElements[(this.mHead + n) & this.mCapacityBitmask];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public E getFirst() {
        int i = this.mHead;
        if (i != this.mTail) {
            return this.mElements[i];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public E getLast() {
        int i = this.mHead;
        int i2 = this.mTail;
        if (i != i2) {
            return this.mElements[(i2 - 1) & this.mCapacityBitmask];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public boolean isEmpty() {
        return this.mHead == this.mTail;
    }

    public E popFirst() {
        int i = this.mHead;
        if (i != this.mTail) {
            E[] eArr = this.mElements;
            E e = eArr[i];
            eArr[i] = null;
            this.mHead = (i + 1) & this.mCapacityBitmask;
            return e;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public E popLast() {
        int i = this.mHead;
        int i2 = this.mTail;
        if (i != i2) {
            int i3 = this.mCapacityBitmask & (i2 - 1);
            E[] eArr = this.mElements;
            E e = eArr[i3];
            eArr[i3] = null;
            this.mTail = i3;
            return e;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public void removeFromEnd(int numOfElements) {
        int i;
        if (numOfElements > 0) {
            if (numOfElements <= size()) {
                int i2 = 0;
                int i3 = this.mTail;
                if (numOfElements < i3) {
                    i2 = i3 - numOfElements;
                }
                int i4 = i2;
                while (true) {
                    i = this.mTail;
                    if (i4 >= i) {
                        break;
                    }
                    this.mElements[i4] = null;
                    i4++;
                }
                int i5 = i - i2;
                int numOfElements2 = numOfElements - i5;
                this.mTail = i - i5;
                if (numOfElements2 > 0) {
                    int length = this.mElements.length;
                    this.mTail = length;
                    int i6 = length - numOfElements2;
                    for (int i7 = i6; i7 < this.mTail; i7++) {
                        this.mElements[i7] = null;
                    }
                    this.mTail = i6;
                    return;
                }
                return;
            }
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public void removeFromStart(int numOfElements) {
        if (numOfElements > 0) {
            if (numOfElements <= size()) {
                int length = this.mElements.length;
                int i = this.mHead;
                if (numOfElements < length - i) {
                    length = i + numOfElements;
                }
                for (int i2 = this.mHead; i2 < length; i2++) {
                    this.mElements[i2] = null;
                }
                int i3 = this.mHead;
                int i4 = length - i3;
                int numOfElements2 = numOfElements - i4;
                this.mHead = (i3 + i4) & this.mCapacityBitmask;
                if (numOfElements2 > 0) {
                    for (int i5 = 0; i5 < numOfElements2; i5++) {
                        this.mElements[i5] = null;
                    }
                    this.mHead = numOfElements2;
                    return;
                }
                return;
            }
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public int size() {
        return (this.mTail - this.mHead) & this.mCapacityBitmask;
    }
}

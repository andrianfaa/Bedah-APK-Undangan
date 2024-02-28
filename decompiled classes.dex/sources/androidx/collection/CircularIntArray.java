package androidx.collection;

public final class CircularIntArray {
    private int mCapacityBitmask;
    private int[] mElements;
    private int mHead;
    private int mTail;

    public CircularIntArray() {
        this(8);
    }

    public CircularIntArray(int minCapacity) {
        if (minCapacity < 1) {
            throw new IllegalArgumentException("capacity must be >= 1");
        } else if (minCapacity <= 1073741824) {
            int highestOneBit = Integer.bitCount(minCapacity) != 1 ? Integer.highestOneBit(minCapacity - 1) << 1 : minCapacity;
            this.mCapacityBitmask = highestOneBit - 1;
            this.mElements = new int[highestOneBit];
        } else {
            throw new IllegalArgumentException("capacity must be <= 2^30");
        }
    }

    private void doubleCapacity() {
        int[] iArr = this.mElements;
        int length = iArr.length;
        int i = this.mHead;
        int i2 = length - i;
        int i3 = length << 1;
        if (i3 >= 0) {
            int[] iArr2 = new int[i3];
            System.arraycopy(iArr, i, iArr2, 0, i2);
            System.arraycopy(this.mElements, 0, iArr2, i2, this.mHead);
            this.mElements = iArr2;
            this.mHead = 0;
            this.mTail = length;
            this.mCapacityBitmask = i3 - 1;
            return;
        }
        throw new RuntimeException("Max array capacity exceeded");
    }

    public void addFirst(int e) {
        int i = (this.mHead - 1) & this.mCapacityBitmask;
        this.mHead = i;
        this.mElements[i] = e;
        if (i == this.mTail) {
            doubleCapacity();
        }
    }

    public void addLast(int e) {
        int[] iArr = this.mElements;
        int i = this.mTail;
        iArr[i] = e;
        int i2 = this.mCapacityBitmask & (i + 1);
        this.mTail = i2;
        if (i2 == this.mHead) {
            doubleCapacity();
        }
    }

    public void clear() {
        this.mTail = this.mHead;
    }

    public int get(int n) {
        if (n >= 0 && n < size()) {
            return this.mElements[(this.mHead + n) & this.mCapacityBitmask];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getFirst() {
        int i = this.mHead;
        if (i != this.mTail) {
            return this.mElements[i];
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getLast() {
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

    public int popFirst() {
        int i = this.mHead;
        if (i != this.mTail) {
            int i2 = this.mElements[i];
            this.mHead = (i + 1) & this.mCapacityBitmask;
            return i2;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int popLast() {
        int i = this.mHead;
        int i2 = this.mTail;
        if (i != i2) {
            int i3 = this.mCapacityBitmask & (i2 - 1);
            int i4 = this.mElements[i3];
            this.mTail = i3;
            return i4;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public void removeFromEnd(int numOfElements) {
        if (numOfElements > 0) {
            if (numOfElements <= size()) {
                this.mTail = (this.mTail - numOfElements) & this.mCapacityBitmask;
                return;
            }
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public void removeFromStart(int numOfElements) {
        if (numOfElements > 0) {
            if (numOfElements <= size()) {
                this.mHead = (this.mHead + numOfElements) & this.mCapacityBitmask;
                return;
            }
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public int size() {
        return (this.mTail - this.mHead) & this.mCapacityBitmask;
    }
}

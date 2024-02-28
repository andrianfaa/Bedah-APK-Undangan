package androidx.collection;

class ContainerHelpers {
    static final int[] EMPTY_INTS = new int[0];
    static final long[] EMPTY_LONGS = new long[0];
    static final Object[] EMPTY_OBJECTS = new Object[0];

    private ContainerHelpers() {
    }

    static int binarySearch(int[] array, int size, int value) {
        int i = 0;
        int i2 = size - 1;
        while (i <= i2) {
            int i3 = (i + i2) >>> 1;
            int i4 = array[i3];
            if (i4 < value) {
                i = i3 + 1;
            } else if (i4 <= value) {
                return i3;
            } else {
                i2 = i3 - 1;
            }
        }
        return ~i;
    }

    static int binarySearch(long[] array, int size, long value) {
        int i = 0;
        int i2 = size - 1;
        while (i <= i2) {
            int i3 = (i + i2) >>> 1;
            long j = array[i3];
            if (j < value) {
                i = i3 + 1;
            } else if (j <= value) {
                return i3;
            } else {
                i2 = i3 - 1;
            }
        }
        return ~i;
    }

    public static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static int idealByteArraySize(int need) {
        for (int i = 4; i < 32; i++) {
            if (need <= (1 << i) - 12) {
                return (1 << i) - 12;
            }
        }
        return need;
    }

    public static int idealIntArraySize(int need) {
        return idealByteArraySize(need * 4) / 4;
    }

    public static int idealLongArraySize(int need) {
        return idealByteArraySize(need * 8) / 8;
    }
}

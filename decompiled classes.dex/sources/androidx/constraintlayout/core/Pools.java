package androidx.constraintlayout.core;

final class Pools {
    private static final boolean DEBUG = false;

    interface Pool<T> {
        T acquire();

        boolean release(T t);

        void releaseAll(T[] tArr, int i);
    }

    static class SimplePool<T> implements Pool<T> {
        private final Object[] mPool;
        private int mPoolSize;

        SimplePool(int maxPoolSize) {
            if (maxPoolSize > 0) {
                this.mPool = new Object[maxPoolSize];
                return;
            }
            throw new IllegalArgumentException("The max pool size must be > 0");
        }

        private boolean isInPool(T t) {
            for (int i = 0; i < this.mPoolSize; i++) {
                if (this.mPool[i] == t) {
                    return true;
                }
            }
            return false;
        }

        public T acquire() {
            int i = this.mPoolSize;
            if (i <= 0) {
                return null;
            }
            int i2 = i - 1;
            T[] tArr = this.mPool;
            T t = tArr[i2];
            tArr[i2] = null;
            this.mPoolSize = i - 1;
            return t;
        }

        public boolean release(T t) {
            int i = this.mPoolSize;
            Object[] objArr = this.mPool;
            if (i >= objArr.length) {
                return false;
            }
            objArr[i] = t;
            this.mPoolSize = i + 1;
            return true;
        }

        public void releaseAll(T[] tArr, int count) {
            if (count > tArr.length) {
                count = tArr.length;
            }
            for (int i = 0; i < count; i++) {
                T t = tArr[i];
                int i2 = this.mPoolSize;
                Object[] objArr = this.mPool;
                if (i2 < objArr.length) {
                    objArr[i2] = t;
                    this.mPoolSize = i2 + 1;
                }
            }
        }
    }

    private Pools() {
    }
}

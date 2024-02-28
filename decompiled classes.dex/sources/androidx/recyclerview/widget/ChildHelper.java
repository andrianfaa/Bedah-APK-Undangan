package androidx.recyclerview.widget;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import mt.Log1F380D;

class ChildHelper {
    private static final boolean DEBUG = false;
    private static final String TAG = "ChildrenHelper";
    final Bucket mBucket = new Bucket();
    final Callback mCallback;
    final List<View> mHiddenViews = new ArrayList();

    /* compiled from: 008B */
    static class Bucket {
        static final int BITS_PER_WORD = 64;
        static final long LAST_BIT = Long.MIN_VALUE;
        long mData = 0;
        Bucket mNext;

        Bucket() {
        }

        private void ensureNext() {
            if (this.mNext == null) {
                this.mNext = new Bucket();
            }
        }

        /* access modifiers changed from: package-private */
        public void clear(int index) {
            if (index >= 64) {
                Bucket bucket = this.mNext;
                if (bucket != null) {
                    bucket.clear(index - 64);
                    return;
                }
                return;
            }
            this.mData &= ~(1 << index);
        }

        /* access modifiers changed from: package-private */
        public int countOnesBefore(int index) {
            Bucket bucket = this.mNext;
            return bucket == null ? index >= 64 ? Long.bitCount(this.mData) : Long.bitCount(this.mData & ((1 << index) - 1)) : index < 64 ? Long.bitCount(this.mData & ((1 << index) - 1)) : bucket.countOnesBefore(index - 64) + Long.bitCount(this.mData);
        }

        /* access modifiers changed from: package-private */
        public boolean get(int index) {
            if (index < 64) {
                return (this.mData & (1 << index)) != 0;
            }
            ensureNext();
            return this.mNext.get(index - 64);
        }

        /* access modifiers changed from: package-private */
        public void insert(int index, boolean value) {
            if (index >= 64) {
                ensureNext();
                this.mNext.insert(index - 64, value);
                return;
            }
            long j = this.mData;
            boolean z = (Long.MIN_VALUE & j) != 0;
            long j2 = (1 << index) - 1;
            this.mData = (j & j2) | ((j & (~j2)) << 1);
            if (value) {
                set(index);
            } else {
                clear(index);
            }
            if (z || this.mNext != null) {
                ensureNext();
                this.mNext.insert(0, z);
            }
        }

        /* access modifiers changed from: package-private */
        public boolean remove(int index) {
            if (index >= 64) {
                ensureNext();
                return this.mNext.remove(index - 64);
            }
            long j = 1 << index;
            long j2 = this.mData;
            boolean z = (j2 & j) != 0;
            long j3 = j2 & (~j);
            this.mData = j3;
            long j4 = j - 1;
            this.mData = (j3 & j4) | Long.rotateRight(j3 & (~j4), 1);
            Bucket bucket = this.mNext;
            if (bucket != null) {
                if (bucket.get(0)) {
                    set(63);
                }
                this.mNext.remove(0);
            }
            return z;
        }

        /* access modifiers changed from: package-private */
        public void reset() {
            this.mData = 0;
            Bucket bucket = this.mNext;
            if (bucket != null) {
                bucket.reset();
            }
        }

        /* access modifiers changed from: package-private */
        public void set(int index) {
            if (index >= 64) {
                ensureNext();
                this.mNext.set(index - 64);
                return;
            }
            this.mData |= 1 << index;
        }

        public String toString() {
            if (this.mNext == null) {
                String binaryString = Long.toBinaryString(this.mData);
                Log1F380D.a((Object) binaryString);
                return binaryString;
            }
            StringBuilder append = new StringBuilder().append(this.mNext.toString()).append("xx");
            String binaryString2 = Long.toBinaryString(this.mData);
            Log1F380D.a((Object) binaryString2);
            return append.append(binaryString2).toString();
        }
    }

    interface Callback {
        void addView(View view, int i);

        void attachViewToParent(View view, int i, ViewGroup.LayoutParams layoutParams);

        void detachViewFromParent(int i);

        View getChildAt(int i);

        int getChildCount();

        RecyclerView.ViewHolder getChildViewHolder(View view);

        int indexOfChild(View view);

        void onEnteredHiddenState(View view);

        void onLeftHiddenState(View view);

        void removeAllViews();

        void removeViewAt(int i);
    }

    ChildHelper(Callback callback) {
        this.mCallback = callback;
    }

    private int getOffset(int index) {
        if (index < 0) {
            return -1;
        }
        int childCount = this.mCallback.getChildCount();
        int i = index;
        while (i < childCount) {
            int countOnesBefore = index - (i - this.mBucket.countOnesBefore(i));
            if (countOnesBefore == 0) {
                while (this.mBucket.get(i)) {
                    i++;
                }
                return i;
            }
            i += countOnesBefore;
        }
        return -1;
    }

    private void hideViewInternal(View child) {
        this.mHiddenViews.add(child);
        this.mCallback.onEnteredHiddenState(child);
    }

    private boolean unhideViewInternal(View child) {
        if (!this.mHiddenViews.remove(child)) {
            return false;
        }
        this.mCallback.onLeftHiddenState(child);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void addView(View child, int index, boolean hidden) {
        int childCount = index < 0 ? this.mCallback.getChildCount() : getOffset(index);
        this.mBucket.insert(childCount, hidden);
        if (hidden) {
            hideViewInternal(child);
        }
        this.mCallback.addView(child, childCount);
    }

    /* access modifiers changed from: package-private */
    public void addView(View child, boolean hidden) {
        addView(child, -1, hidden);
    }

    /* access modifiers changed from: package-private */
    public void attachViewToParent(View child, int index, ViewGroup.LayoutParams layoutParams, boolean hidden) {
        int childCount = index < 0 ? this.mCallback.getChildCount() : getOffset(index);
        this.mBucket.insert(childCount, hidden);
        if (hidden) {
            hideViewInternal(child);
        }
        this.mCallback.attachViewToParent(child, childCount, layoutParams);
    }

    /* access modifiers changed from: package-private */
    public void detachViewFromParent(int index) {
        int offset = getOffset(index);
        this.mBucket.remove(offset);
        this.mCallback.detachViewFromParent(offset);
    }

    /* access modifiers changed from: package-private */
    public View findHiddenNonRemovedView(int position) {
        int size = this.mHiddenViews.size();
        for (int i = 0; i < size; i++) {
            View view = this.mHiddenViews.get(i);
            RecyclerView.ViewHolder childViewHolder = this.mCallback.getChildViewHolder(view);
            if (childViewHolder.getLayoutPosition() == position && !childViewHolder.isInvalid() && !childViewHolder.isRemoved()) {
                return view;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public View getChildAt(int index) {
        return this.mCallback.getChildAt(getOffset(index));
    }

    /* access modifiers changed from: package-private */
    public int getChildCount() {
        return this.mCallback.getChildCount() - this.mHiddenViews.size();
    }

    /* access modifiers changed from: package-private */
    public View getUnfilteredChildAt(int index) {
        return this.mCallback.getChildAt(index);
    }

    /* access modifiers changed from: package-private */
    public int getUnfilteredChildCount() {
        return this.mCallback.getChildCount();
    }

    /* access modifiers changed from: package-private */
    public void hide(View view) {
        int indexOfChild = this.mCallback.indexOfChild(view);
        if (indexOfChild >= 0) {
            this.mBucket.set(indexOfChild);
            hideViewInternal(view);
            return;
        }
        throw new IllegalArgumentException("view is not a child, cannot hide " + view);
    }

    /* access modifiers changed from: package-private */
    public int indexOfChild(View child) {
        int indexOfChild = this.mCallback.indexOfChild(child);
        if (indexOfChild != -1 && !this.mBucket.get(indexOfChild)) {
            return indexOfChild - this.mBucket.countOnesBefore(indexOfChild);
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public boolean isHidden(View view) {
        return this.mHiddenViews.contains(view);
    }

    /* access modifiers changed from: package-private */
    public void removeAllViewsUnfiltered() {
        this.mBucket.reset();
        for (int size = this.mHiddenViews.size() - 1; size >= 0; size--) {
            this.mCallback.onLeftHiddenState(this.mHiddenViews.get(size));
            this.mHiddenViews.remove(size);
        }
        this.mCallback.removeAllViews();
    }

    /* access modifiers changed from: package-private */
    public void removeView(View view) {
        int indexOfChild = this.mCallback.indexOfChild(view);
        if (indexOfChild >= 0) {
            if (this.mBucket.remove(indexOfChild)) {
                unhideViewInternal(view);
            }
            this.mCallback.removeViewAt(indexOfChild);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeViewAt(int index) {
        int offset = getOffset(index);
        View childAt = this.mCallback.getChildAt(offset);
        if (childAt != null) {
            if (this.mBucket.remove(offset)) {
                unhideViewInternal(childAt);
            }
            this.mCallback.removeViewAt(offset);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean removeViewIfHidden(View view) {
        int indexOfChild = this.mCallback.indexOfChild(view);
        if (indexOfChild == -1) {
            unhideViewInternal(view);
            return true;
        } else if (!this.mBucket.get(indexOfChild)) {
            return false;
        } else {
            this.mBucket.remove(indexOfChild);
            unhideViewInternal(view);
            this.mCallback.removeViewAt(indexOfChild);
            return true;
        }
    }

    public String toString() {
        return this.mBucket.toString() + ", hidden list:" + this.mHiddenViews.size();
    }

    /* access modifiers changed from: package-private */
    public void unhide(View view) {
        int indexOfChild = this.mCallback.indexOfChild(view);
        if (indexOfChild < 0) {
            throw new IllegalArgumentException("view is not a child, cannot hide " + view);
        } else if (this.mBucket.get(indexOfChild)) {
            this.mBucket.clear(indexOfChild);
            unhideViewInternal(view);
        } else {
            throw new RuntimeException("trying to unhide a view that was not hidden" + view);
        }
    }
}

package androidx.recyclerview.widget;

import android.view.View;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

class ViewBoundsCheck {
    static final int CVE_PVE_POS = 12;
    static final int CVE_PVS_POS = 8;
    static final int CVS_PVE_POS = 4;
    static final int CVS_PVS_POS = 0;
    static final int EQ = 2;
    static final int FLAG_CVE_EQ_PVE = 8192;
    static final int FLAG_CVE_EQ_PVS = 512;
    static final int FLAG_CVE_GT_PVE = 4096;
    static final int FLAG_CVE_GT_PVS = 256;
    static final int FLAG_CVE_LT_PVE = 16384;
    static final int FLAG_CVE_LT_PVS = 1024;
    static final int FLAG_CVS_EQ_PVE = 32;
    static final int FLAG_CVS_EQ_PVS = 2;
    static final int FLAG_CVS_GT_PVE = 16;
    static final int FLAG_CVS_GT_PVS = 1;
    static final int FLAG_CVS_LT_PVE = 64;
    static final int FLAG_CVS_LT_PVS = 4;
    static final int GT = 1;
    static final int LT = 4;
    static final int MASK = 7;
    BoundFlags mBoundFlags = new BoundFlags();
    final Callback mCallback;

    static class BoundFlags {
        int mBoundFlags = 0;
        int mChildEnd;
        int mChildStart;
        int mRvEnd;
        int mRvStart;

        BoundFlags() {
        }

        /* access modifiers changed from: package-private */
        public void addFlags(int flags) {
            this.mBoundFlags |= flags;
        }

        /* access modifiers changed from: package-private */
        public boolean boundsMatch() {
            int i = this.mBoundFlags;
            if ((i & 7) != 0 && (i & (compare(this.mChildStart, this.mRvStart) << 0)) == 0) {
                return false;
            }
            int i2 = this.mBoundFlags;
            if ((i2 & 112) != 0 && (i2 & (compare(this.mChildStart, this.mRvEnd) << 4)) == 0) {
                return false;
            }
            int i3 = this.mBoundFlags;
            if ((i3 & 1792) != 0 && (i3 & (compare(this.mChildEnd, this.mRvStart) << 8)) == 0) {
                return false;
            }
            int i4 = this.mBoundFlags;
            return (i4 & 28672) == 0 || (i4 & (compare(this.mChildEnd, this.mRvEnd) << 12)) != 0;
        }

        /* access modifiers changed from: package-private */
        public int compare(int x, int y) {
            if (x > y) {
                return 1;
            }
            return x == y ? 2 : 4;
        }

        /* access modifiers changed from: package-private */
        public void resetFlags() {
            this.mBoundFlags = 0;
        }

        /* access modifiers changed from: package-private */
        public void setBounds(int rvStart, int rvEnd, int childStart, int childEnd) {
            this.mRvStart = rvStart;
            this.mRvEnd = rvEnd;
            this.mChildStart = childStart;
            this.mChildEnd = childEnd;
        }
    }

    interface Callback {
        View getChildAt(int i);

        int getChildEnd(View view);

        int getChildStart(View view);

        int getParentEnd();

        int getParentStart();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewBounds {
    }

    ViewBoundsCheck(Callback callback) {
        this.mCallback = callback;
    }

    /* access modifiers changed from: package-private */
    public View findOneViewWithinBoundFlags(int fromIndex, int toIndex, int preferredBoundFlags, int acceptableBoundFlags) {
        int parentStart = this.mCallback.getParentStart();
        int parentEnd = this.mCallback.getParentEnd();
        int i = toIndex > fromIndex ? 1 : -1;
        View view = null;
        for (int i2 = fromIndex; i2 != toIndex; i2 += i) {
            View childAt = this.mCallback.getChildAt(i2);
            this.mBoundFlags.setBounds(parentStart, parentEnd, this.mCallback.getChildStart(childAt), this.mCallback.getChildEnd(childAt));
            if (preferredBoundFlags != 0) {
                this.mBoundFlags.resetFlags();
                this.mBoundFlags.addFlags(preferredBoundFlags);
                if (this.mBoundFlags.boundsMatch()) {
                    return childAt;
                }
            }
            if (acceptableBoundFlags != 0) {
                this.mBoundFlags.resetFlags();
                this.mBoundFlags.addFlags(acceptableBoundFlags);
                if (this.mBoundFlags.boundsMatch()) {
                    view = childAt;
                }
            }
        }
        return view;
    }

    /* access modifiers changed from: package-private */
    public boolean isViewWithinBoundFlags(View child, int boundsFlags) {
        this.mBoundFlags.setBounds(this.mCallback.getParentStart(), this.mCallback.getParentEnd(), this.mCallback.getChildStart(child), this.mCallback.getChildEnd(child));
        if (boundsFlags == 0) {
            return false;
        }
        this.mBoundFlags.resetFlags();
        this.mBoundFlags.addFlags(boundsFlags);
        return this.mBoundFlags.boundsMatch();
    }
}

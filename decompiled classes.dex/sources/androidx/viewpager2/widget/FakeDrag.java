package androidx.viewpager2.widget;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import androidx.recyclerview.widget.RecyclerView;

final class FakeDrag {
    private int mActualDraggedDistance;
    private long mFakeDragBeginTime;
    private int mMaximumVelocity;
    private final RecyclerView mRecyclerView;
    private float mRequestedDragDistance;
    private final ScrollEventAdapter mScrollEventAdapter;
    private VelocityTracker mVelocityTracker;
    private final ViewPager2 mViewPager;

    FakeDrag(ViewPager2 viewPager, ScrollEventAdapter scrollEventAdapter, RecyclerView recyclerView) {
        this.mViewPager = viewPager;
        this.mScrollEventAdapter = scrollEventAdapter;
        this.mRecyclerView = recyclerView;
    }

    private void addFakeMotionEvent(long time, int action, float x, float y) {
        MotionEvent obtain = MotionEvent.obtain(this.mFakeDragBeginTime, time, action, x, y, 0);
        this.mVelocityTracker.addMovement(obtain);
        obtain.recycle();
    }

    private void beginFakeVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
            this.mMaximumVelocity = ViewConfiguration.get(this.mViewPager.getContext()).getScaledMaximumFlingVelocity();
            return;
        }
        velocityTracker.clear();
    }

    /* access modifiers changed from: package-private */
    public boolean beginFakeDrag() {
        if (this.mScrollEventAdapter.isDragging()) {
            return false;
        }
        this.mActualDraggedDistance = 0;
        this.mRequestedDragDistance = (float) 0;
        this.mFakeDragBeginTime = SystemClock.uptimeMillis();
        beginFakeVelocityTracker();
        this.mScrollEventAdapter.notifyBeginFakeDrag();
        if (!this.mScrollEventAdapter.isIdle()) {
            this.mRecyclerView.stopScroll();
        }
        addFakeMotionEvent(this.mFakeDragBeginTime, 0, 0.0f, 0.0f);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean endFakeDrag() {
        if (!this.mScrollEventAdapter.isFakeDragging()) {
            return false;
        }
        this.mScrollEventAdapter.notifyEndFakeDrag();
        VelocityTracker velocityTracker = this.mVelocityTracker;
        velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
        if (this.mRecyclerView.fling((int) velocityTracker.getXVelocity(), (int) velocityTracker.getYVelocity())) {
            return true;
        }
        this.mViewPager.snapToPage();
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean fakeDragBy(float offsetPxFloat) {
        int i = 0;
        if (!this.mScrollEventAdapter.isFakeDragging()) {
            return false;
        }
        float f = this.mRequestedDragDistance - offsetPxFloat;
        this.mRequestedDragDistance = f;
        int round = Math.round(f - ((float) this.mActualDraggedDistance));
        this.mActualDraggedDistance += round;
        long uptimeMillis = SystemClock.uptimeMillis();
        boolean z = this.mViewPager.getOrientation() == 0;
        int i2 = z ? round : 0;
        if (!z) {
            i = round;
        }
        float f2 = 0.0f;
        float f3 = z ? this.mRequestedDragDistance : 0.0f;
        if (!z) {
            f2 = this.mRequestedDragDistance;
        }
        this.mRecyclerView.scrollBy(i2, i);
        addFakeMotionEvent(uptimeMillis, 2, f3, f2);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isFakeDragging() {
        return this.mScrollEventAdapter.isFakeDragging();
    }
}

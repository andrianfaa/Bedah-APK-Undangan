package androidx.viewpager2.widget;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.Locale;
import mt.Log1F380D;

/* compiled from: 00A1 */
final class ScrollEventAdapter extends RecyclerView.OnScrollListener {
    private static final int NO_POSITION = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_IN_PROGRESS_FAKE_DRAG = 4;
    private static final int STATE_IN_PROGRESS_IMMEDIATE_SCROLL = 3;
    private static final int STATE_IN_PROGRESS_MANUAL_DRAG = 1;
    private static final int STATE_IN_PROGRESS_SMOOTH_SCROLL = 2;
    private int mAdapterState;
    private ViewPager2.OnPageChangeCallback mCallback;
    private boolean mDataSetChangeHappened;
    private boolean mDispatchSelected;
    private int mDragStartPosition;
    private boolean mFakeDragging;
    private final LinearLayoutManager mLayoutManager;
    private final RecyclerView mRecyclerView;
    private boolean mScrollHappened;
    private int mScrollState;
    private ScrollEventValues mScrollValues = new ScrollEventValues();
    private int mTarget;
    private final ViewPager2 mViewPager;

    private static final class ScrollEventValues {
        float mOffset;
        int mOffsetPx;
        int mPosition;

        ScrollEventValues() {
        }

        /* access modifiers changed from: package-private */
        public void reset() {
            this.mPosition = -1;
            this.mOffset = 0.0f;
            this.mOffsetPx = 0;
        }
    }

    ScrollEventAdapter(ViewPager2 viewPager) {
        this.mViewPager = viewPager;
        RecyclerView recyclerView = viewPager.mRecyclerView;
        this.mRecyclerView = recyclerView;
        this.mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        resetState();
    }

    private void dispatchScrolled(int position, float offset, int offsetPx) {
        ViewPager2.OnPageChangeCallback onPageChangeCallback = this.mCallback;
        if (onPageChangeCallback != null) {
            onPageChangeCallback.onPageScrolled(position, offset, offsetPx);
        }
    }

    private void dispatchSelected(int target) {
        ViewPager2.OnPageChangeCallback onPageChangeCallback = this.mCallback;
        if (onPageChangeCallback != null) {
            onPageChangeCallback.onPageSelected(target);
        }
    }

    private void dispatchStateChanged(int state) {
        if ((this.mAdapterState != 3 || this.mScrollState != 0) && this.mScrollState != state) {
            this.mScrollState = state;
            ViewPager2.OnPageChangeCallback onPageChangeCallback = this.mCallback;
            if (onPageChangeCallback != null) {
                onPageChangeCallback.onPageScrollStateChanged(state);
            }
        }
    }

    private int getPosition() {
        return this.mLayoutManager.findFirstVisibleItemPosition();
    }

    private boolean isInAnyDraggingState() {
        int i = this.mAdapterState;
        return i == 1 || i == 4;
    }

    private void resetState() {
        this.mAdapterState = 0;
        this.mScrollState = 0;
        this.mScrollValues.reset();
        this.mDragStartPosition = -1;
        this.mTarget = -1;
        this.mDispatchSelected = false;
        this.mScrollHappened = false;
        this.mFakeDragging = false;
        this.mDataSetChangeHappened = false;
    }

    private void startDrag(boolean isFakeDrag) {
        this.mFakeDragging = isFakeDrag;
        this.mAdapterState = isFakeDrag ? 4 : 1;
        int i = this.mTarget;
        if (i != -1) {
            this.mDragStartPosition = i;
            this.mTarget = -1;
        } else if (this.mDragStartPosition == -1) {
            this.mDragStartPosition = getPosition();
        }
        dispatchStateChanged(1);
    }

    private void updateScrollEventValues() {
        int i;
        int i2;
        ScrollEventValues scrollEventValues = this.mScrollValues;
        scrollEventValues.mPosition = this.mLayoutManager.findFirstVisibleItemPosition();
        if (scrollEventValues.mPosition == -1) {
            scrollEventValues.reset();
            return;
        }
        View findViewByPosition = this.mLayoutManager.findViewByPosition(scrollEventValues.mPosition);
        if (findViewByPosition == null) {
            scrollEventValues.reset();
            return;
        }
        int leftDecorationWidth = this.mLayoutManager.getLeftDecorationWidth(findViewByPosition);
        int rightDecorationWidth = this.mLayoutManager.getRightDecorationWidth(findViewByPosition);
        int topDecorationHeight = this.mLayoutManager.getTopDecorationHeight(findViewByPosition);
        int bottomDecorationHeight = this.mLayoutManager.getBottomDecorationHeight(findViewByPosition);
        ViewGroup.LayoutParams layoutParams = findViewByPosition.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            leftDecorationWidth += marginLayoutParams.leftMargin;
            rightDecorationWidth += marginLayoutParams.rightMargin;
            topDecorationHeight += marginLayoutParams.topMargin;
            bottomDecorationHeight += marginLayoutParams.bottomMargin;
        }
        int height = findViewByPosition.getHeight() + topDecorationHeight + bottomDecorationHeight;
        int width = findViewByPosition.getWidth() + leftDecorationWidth + rightDecorationWidth;
        if (this.mLayoutManager.getOrientation() == 0) {
            i2 = width;
            i = (findViewByPosition.getLeft() - leftDecorationWidth) - this.mRecyclerView.getPaddingLeft();
            if (this.mViewPager.isRtl()) {
                i = -i;
            }
        } else {
            i2 = height;
            i = (findViewByPosition.getTop() - topDecorationHeight) - this.mRecyclerView.getPaddingTop();
        }
        scrollEventValues.mOffsetPx = -i;
        if (scrollEventValues.mOffsetPx >= 0) {
            scrollEventValues.mOffset = i2 == 0 ? 0.0f : ((float) scrollEventValues.mOffsetPx) / ((float) i2);
        } else if (new AnimateLayoutChangeDetector(this.mLayoutManager).mayHaveInterferingAnimations()) {
            throw new IllegalStateException("Page(s) contain a ViewGroup with a LayoutTransition (or animateLayoutChanges=\"true\"), which interferes with the scrolling animation. Make sure to call getLayoutTransition().setAnimateParentHierarchy(false) on all ViewGroups with a LayoutTransition before an animation is started.");
        } else {
            String format = String.format(Locale.US, "Page can only be offset by a positive amount, not by %d", new Object[]{Integer.valueOf(scrollEventValues.mOffsetPx)});
            Log1F380D.a((Object) format);
            throw new IllegalStateException(format);
        }
    }

    /* access modifiers changed from: package-private */
    public double getRelativeScrollPosition() {
        updateScrollEventValues();
        return ((double) this.mScrollValues.mPosition) + ((double) this.mScrollValues.mOffset);
    }

    /* access modifiers changed from: package-private */
    public int getScrollState() {
        return this.mScrollState;
    }

    /* access modifiers changed from: package-private */
    public boolean isDragging() {
        return this.mScrollState == 1;
    }

    /* access modifiers changed from: package-private */
    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    /* access modifiers changed from: package-private */
    public boolean isIdle() {
        return this.mScrollState == 0;
    }

    /* access modifiers changed from: package-private */
    public void notifyBeginFakeDrag() {
        this.mAdapterState = 4;
        startDrag(true);
    }

    /* access modifiers changed from: package-private */
    public void notifyDataSetChangeHappened() {
        this.mDataSetChangeHappened = true;
    }

    /* access modifiers changed from: package-private */
    public void notifyEndFakeDrag() {
        if (!isDragging() || this.mFakeDragging) {
            this.mFakeDragging = false;
            updateScrollEventValues();
            if (this.mScrollValues.mOffsetPx == 0) {
                if (this.mScrollValues.mPosition != this.mDragStartPosition) {
                    dispatchSelected(this.mScrollValues.mPosition);
                }
                dispatchStateChanged(0);
                resetState();
                return;
            }
            dispatchStateChanged(2);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyProgrammaticScroll(int target, boolean smooth) {
        this.mAdapterState = smooth ? 2 : 3;
        boolean z = false;
        this.mFakeDragging = false;
        if (this.mTarget != target) {
            z = true;
        }
        this.mTarget = target;
        dispatchStateChanged(2);
        if (z) {
            dispatchSelected(target);
        }
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (!(this.mAdapterState == 1 && this.mScrollState == 1) && newState == 1) {
            startDrag(false);
        } else if (!isInAnyDraggingState() || newState != 2) {
            if (isInAnyDraggingState() && newState == 0) {
                boolean z = false;
                updateScrollEventValues();
                if (!this.mScrollHappened) {
                    if (this.mScrollValues.mPosition != -1) {
                        dispatchScrolled(this.mScrollValues.mPosition, 0.0f, 0);
                    }
                    z = true;
                } else if (this.mScrollValues.mOffsetPx == 0) {
                    z = true;
                    if (this.mDragStartPosition != this.mScrollValues.mPosition) {
                        dispatchSelected(this.mScrollValues.mPosition);
                    }
                }
                if (z) {
                    dispatchStateChanged(0);
                    resetState();
                }
            }
            if (this.mAdapterState == 2 && newState == 0 && this.mDataSetChangeHappened) {
                updateScrollEventValues();
                if (this.mScrollValues.mOffsetPx == 0) {
                    if (this.mTarget != this.mScrollValues.mPosition) {
                        dispatchSelected(this.mScrollValues.mPosition == -1 ? 0 : this.mScrollValues.mPosition);
                    }
                    dispatchStateChanged(0);
                    resetState();
                }
            }
        } else if (this.mScrollHappened) {
            dispatchStateChanged(2);
            this.mDispatchSelected = true;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001d, code lost:
        if ((r8 < 0) == r6.mViewPager.isRtl()) goto L_0x0022;
     */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002b  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0031  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onScrolled(androidx.recyclerview.widget.RecyclerView r7, int r8, int r9) {
        /*
            r6 = this;
            r0 = 1
            r6.mScrollHappened = r0
            r6.updateScrollEventValues()
            boolean r1 = r6.mDispatchSelected
            r2 = -1
            r3 = 0
            if (r1 == 0) goto L_0x003f
            r6.mDispatchSelected = r3
            if (r9 > 0) goto L_0x0022
            if (r9 != 0) goto L_0x0020
            if (r8 >= 0) goto L_0x0016
            r1 = r0
            goto L_0x0017
        L_0x0016:
            r1 = r3
        L_0x0017:
            androidx.viewpager2.widget.ViewPager2 r4 = r6.mViewPager
            boolean r4 = r4.isRtl()
            if (r1 != r4) goto L_0x0020
            goto L_0x0022
        L_0x0020:
            r1 = r3
            goto L_0x0023
        L_0x0022:
            r1 = r0
        L_0x0023:
            if (r1 == 0) goto L_0x0031
            androidx.viewpager2.widget.ScrollEventAdapter$ScrollEventValues r4 = r6.mScrollValues
            int r4 = r4.mOffsetPx
            if (r4 == 0) goto L_0x0031
            androidx.viewpager2.widget.ScrollEventAdapter$ScrollEventValues r4 = r6.mScrollValues
            int r4 = r4.mPosition
            int r4 = r4 + r0
            goto L_0x0035
        L_0x0031:
            androidx.viewpager2.widget.ScrollEventAdapter$ScrollEventValues r4 = r6.mScrollValues
            int r4 = r4.mPosition
        L_0x0035:
            r6.mTarget = r4
            int r5 = r6.mDragStartPosition
            if (r5 == r4) goto L_0x0050
            r6.dispatchSelected(r4)
            goto L_0x0050
        L_0x003f:
            int r1 = r6.mAdapterState
            if (r1 != 0) goto L_0x0050
            androidx.viewpager2.widget.ScrollEventAdapter$ScrollEventValues r1 = r6.mScrollValues
            int r1 = r1.mPosition
            if (r1 != r2) goto L_0x004b
            r4 = r3
            goto L_0x004c
        L_0x004b:
            r4 = r1
        L_0x004c:
            r6.dispatchSelected(r4)
            goto L_0x0051
        L_0x0050:
        L_0x0051:
            androidx.viewpager2.widget.ScrollEventAdapter$ScrollEventValues r1 = r6.mScrollValues
            int r1 = r1.mPosition
            if (r1 != r2) goto L_0x0059
            r1 = r3
            goto L_0x005d
        L_0x0059:
            androidx.viewpager2.widget.ScrollEventAdapter$ScrollEventValues r1 = r6.mScrollValues
            int r1 = r1.mPosition
        L_0x005d:
            androidx.viewpager2.widget.ScrollEventAdapter$ScrollEventValues r4 = r6.mScrollValues
            float r4 = r4.mOffset
            androidx.viewpager2.widget.ScrollEventAdapter$ScrollEventValues r5 = r6.mScrollValues
            int r5 = r5.mOffsetPx
            r6.dispatchScrolled(r1, r4, r5)
            androidx.viewpager2.widget.ScrollEventAdapter$ScrollEventValues r1 = r6.mScrollValues
            int r1 = r1.mPosition
            int r4 = r6.mTarget
            if (r1 == r4) goto L_0x0072
            if (r4 != r2) goto L_0x0082
        L_0x0072:
            androidx.viewpager2.widget.ScrollEventAdapter$ScrollEventValues r1 = r6.mScrollValues
            int r1 = r1.mOffsetPx
            if (r1 != 0) goto L_0x0082
            int r1 = r6.mScrollState
            if (r1 == r0) goto L_0x0082
            r6.dispatchStateChanged(r3)
            r6.resetState()
        L_0x0082:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.viewpager2.widget.ScrollEventAdapter.onScrolled(androidx.recyclerview.widget.RecyclerView, int, int):void");
    }

    /* access modifiers changed from: package-private */
    public void setOnPageChangeCallback(ViewPager2.OnPageChangeCallback callback) {
        this.mCallback = callback;
    }
}

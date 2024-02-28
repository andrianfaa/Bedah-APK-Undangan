package androidx.recyclerview.widget;

import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class PagerSnapHelper extends SnapHelper {
    private static final int MAX_SCROLL_ON_FLING_DURATION = 100;
    private OrientationHelper mHorizontalHelper;
    private OrientationHelper mVerticalHelper;

    private int distanceToCenter(RecyclerView.LayoutManager layoutManager, View targetView, OrientationHelper helper) {
        return (helper.getDecoratedStart(targetView) + (helper.getDecoratedMeasurement(targetView) / 2)) - (helper.getStartAfterPadding() + (helper.getTotalSpace() / 2));
    }

    private View findCenterView(RecyclerView.LayoutManager layoutManager, OrientationHelper helper) {
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        }
        View view = null;
        int startAfterPadding = helper.getStartAfterPadding() + (helper.getTotalSpace() / 2);
        int i = Integer.MAX_VALUE;
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = layoutManager.getChildAt(i2);
            int abs = Math.abs((helper.getDecoratedStart(childAt) + (helper.getDecoratedMeasurement(childAt) / 2)) - startAfterPadding);
            if (abs < i) {
                i = abs;
                view = childAt;
            }
        }
        return view;
    }

    private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager layoutManager) {
        OrientationHelper orientationHelper = this.mHorizontalHelper;
        if (orientationHelper == null || orientationHelper.mLayoutManager != layoutManager) {
            this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return this.mHorizontalHelper;
    }

    private OrientationHelper getOrientationHelper(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return getVerticalHelper(layoutManager);
        }
        if (layoutManager.canScrollHorizontally()) {
            return getHorizontalHelper(layoutManager);
        }
        return null;
    }

    private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager layoutManager) {
        OrientationHelper orientationHelper = this.mVerticalHelper;
        if (orientationHelper == null || orientationHelper.mLayoutManager != layoutManager) {
            this.mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return this.mVerticalHelper;
    }

    private boolean isForwardFling(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        return layoutManager.canScrollHorizontally() ? velocityX > 0 : velocityY > 0;
    }

    private boolean isReverseLayout(RecyclerView.LayoutManager layoutManager) {
        PointF computeScrollVectorForPosition;
        int itemCount = layoutManager.getItemCount();
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) || (computeScrollVectorForPosition = ((RecyclerView.SmoothScroller.ScrollVectorProvider) layoutManager).computeScrollVectorForPosition(itemCount - 1)) == null) {
            return false;
        }
        return computeScrollVectorForPosition.x < 0.0f || computeScrollVectorForPosition.y < 0.0f;
    }

    public int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager layoutManager, View targetView) {
        int[] iArr = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            iArr[0] = distanceToCenter(layoutManager, targetView, getHorizontalHelper(layoutManager));
        } else {
            iArr[0] = 0;
        }
        if (layoutManager.canScrollVertically()) {
            iArr[1] = distanceToCenter(layoutManager, targetView, getVerticalHelper(layoutManager));
        } else {
            iArr[1] = 0;
        }
        return iArr;
    }

    /* access modifiers changed from: protected */
    public LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager layoutManager) {
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return null;
        }
        return new LinearSmoothScroller(this.mRecyclerView.getContext()) {
            /* access modifiers changed from: protected */
            public float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 100.0f / ((float) displayMetrics.densityDpi);
            }

            /* access modifiers changed from: protected */
            public int calculateTimeForScrolling(int dx) {
                return Math.min(100, super.calculateTimeForScrolling(dx));
            }

            /* access modifiers changed from: protected */
            public void onTargetFound(View targetView, RecyclerView.State state, RecyclerView.SmoothScroller.Action action) {
                PagerSnapHelper pagerSnapHelper = PagerSnapHelper.this;
                int[] calculateDistanceToFinalSnap = pagerSnapHelper.calculateDistanceToFinalSnap(pagerSnapHelper.mRecyclerView.getLayoutManager(), targetView);
                int i = calculateDistanceToFinalSnap[0];
                int i2 = calculateDistanceToFinalSnap[1];
                int calculateTimeForDeceleration = calculateTimeForDeceleration(Math.max(Math.abs(i), Math.abs(i2)));
                if (calculateTimeForDeceleration > 0) {
                    action.update(i, i2, calculateTimeForDeceleration, this.mDecelerateInterpolator);
                }
            }
        };
    }

    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return findCenterView(layoutManager, getVerticalHelper(layoutManager));
        }
        if (layoutManager.canScrollHorizontally()) {
            return findCenterView(layoutManager, getHorizontalHelper(layoutManager));
        }
        return null;
    }

    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        OrientationHelper orientationHelper;
        int itemCount = layoutManager.getItemCount();
        if (itemCount == 0 || (orientationHelper = getOrientationHelper(layoutManager)) == null) {
            return -1;
        }
        View view = null;
        int i = Integer.MIN_VALUE;
        View view2 = null;
        int i2 = Integer.MAX_VALUE;
        int childCount = layoutManager.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = layoutManager.getChildAt(i3);
            if (childAt != null) {
                int distanceToCenter = distanceToCenter(layoutManager, childAt, orientationHelper);
                if (distanceToCenter <= 0 && distanceToCenter > i) {
                    i = distanceToCenter;
                    view = childAt;
                }
                if (distanceToCenter >= 0 && distanceToCenter < i2) {
                    i2 = distanceToCenter;
                    view2 = childAt;
                }
            }
        }
        boolean isForwardFling = isForwardFling(layoutManager, velocityX, velocityY);
        if (isForwardFling && view2 != null) {
            return layoutManager.getPosition(view2);
        }
        if (!isForwardFling && view != null) {
            return layoutManager.getPosition(view);
        }
        View view3 = isForwardFling ? view : view2;
        if (view3 == null) {
            return -1;
        }
        int position = (isReverseLayout(layoutManager) == isForwardFling ? -1 : 1) + layoutManager.getPosition(view3);
        if (position < 0 || position >= itemCount) {
            return -1;
        }
        return position;
    }
}

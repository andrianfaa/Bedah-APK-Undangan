package androidx.recyclerview.widget;

import android.graphics.PointF;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class LinearSnapHelper extends SnapHelper {
    private static final float INVALID_DISTANCE = 1.0f;
    private OrientationHelper mHorizontalHelper;
    private OrientationHelper mVerticalHelper;

    private float computeDistancePerChild(RecyclerView.LayoutManager layoutManager, OrientationHelper helper) {
        int max;
        View view = null;
        View view2 = null;
        int i = Integer.MAX_VALUE;
        int i2 = Integer.MIN_VALUE;
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return 1.0f;
        }
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = layoutManager.getChildAt(i3);
            int position = layoutManager.getPosition(childAt);
            if (position != -1) {
                if (position < i) {
                    i = position;
                    view = childAt;
                }
                if (position > i2) {
                    i2 = position;
                    view2 = childAt;
                }
            }
        }
        if (view == null || view2 == null || (max = Math.max(helper.getDecoratedEnd(view), helper.getDecoratedEnd(view2)) - Math.min(helper.getDecoratedStart(view), helper.getDecoratedStart(view2))) == 0) {
            return 1.0f;
        }
        return (((float) max) * 1.0f) / ((float) ((i2 - i) + 1));
    }

    private int distanceToCenter(RecyclerView.LayoutManager layoutManager, View targetView, OrientationHelper helper) {
        return (helper.getDecoratedStart(targetView) + (helper.getDecoratedMeasurement(targetView) / 2)) - (helper.getStartAfterPadding() + (helper.getTotalSpace() / 2));
    }

    private int estimateNextPositionDiffForFling(RecyclerView.LayoutManager layoutManager, OrientationHelper helper, int velocityX, int velocityY) {
        int[] calculateScrollDistance = calculateScrollDistance(velocityX, velocityY);
        float computeDistancePerChild = computeDistancePerChild(layoutManager, helper);
        if (computeDistancePerChild <= 0.0f) {
            return 0;
        }
        return Math.round(((float) (Math.abs(calculateScrollDistance[0]) > Math.abs(calculateScrollDistance[1]) ? calculateScrollDistance[0] : calculateScrollDistance[1])) / computeDistancePerChild);
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

    private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager layoutManager) {
        OrientationHelper orientationHelper = this.mVerticalHelper;
        if (orientationHelper == null || orientationHelper.mLayoutManager != layoutManager) {
            this.mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return this.mVerticalHelper;
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
        int itemCount;
        View findSnapView;
        int position;
        PointF computeScrollVectorForPosition;
        int i;
        int i2;
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) || (itemCount = layoutManager.getItemCount()) == 0 || (findSnapView = findSnapView(layoutManager)) == null || (position = layoutManager.getPosition(findSnapView)) == -1 || (computeScrollVectorForPosition = ((RecyclerView.SmoothScroller.ScrollVectorProvider) layoutManager).computeScrollVectorForPosition(itemCount - 1)) == null) {
            return -1;
        }
        if (layoutManager.canScrollHorizontally()) {
            i = estimateNextPositionDiffForFling(layoutManager, getHorizontalHelper(layoutManager), velocityX, 0);
            if (computeScrollVectorForPosition.x < 0.0f) {
                i = -i;
            }
        } else {
            i = 0;
        }
        if (layoutManager.canScrollVertically()) {
            i2 = estimateNextPositionDiffForFling(layoutManager, getVerticalHelper(layoutManager), 0, velocityY);
            if (computeScrollVectorForPosition.y < 0.0f) {
                i2 = -i2;
            }
        } else {
            i2 = 0;
        }
        int i3 = layoutManager.canScrollVertically() ? i2 : i;
        if (i3 == 0) {
            return -1;
        }
        int i4 = position + i3;
        if (i4 < 0) {
            i4 = 0;
        }
        return i4 >= itemCount ? itemCount - 1 : i4;
    }
}

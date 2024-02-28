package com.google.android.material.navigationrail;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarMenuView;

public class NavigationRailMenuView extends NavigationBarMenuView {
    private int itemMinimumHeight = -1;
    private final FrameLayout.LayoutParams layoutParams;

    public NavigationRailMenuView(Context context) {
        super(context);
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, -2);
        this.layoutParams = layoutParams2;
        layoutParams2.gravity = 49;
        setLayoutParams(layoutParams2);
        setItemActiveIndicatorResizeable(true);
    }

    private int makeSharedHeightSpec(int parentWidthSpec, int maxHeight, int shareCount) {
        int max = maxHeight / Math.max(1, shareCount);
        int i = this.itemMinimumHeight;
        if (i == -1) {
            i = View.MeasureSpec.getSize(parentWidthSpec);
        }
        return View.MeasureSpec.makeMeasureSpec(Math.min(i, max), 0);
    }

    private int measureChildHeight(View child, int widthMeasureSpec, int heightMeasureSpec) {
        if (child.getVisibility() == 8) {
            return 0;
        }
        child.measure(widthMeasureSpec, heightMeasureSpec);
        return child.getMeasuredHeight();
    }

    private int measureSharedChildHeights(int widthMeasureSpec, int maxHeight, int shareCount, View selectedView) {
        int makeSharedHeightSpec = makeSharedHeightSpec(widthMeasureSpec, maxHeight, shareCount);
        int makeSharedHeightSpec2 = selectedView == null ? makeSharedHeightSpec(widthMeasureSpec, maxHeight, shareCount) : View.MeasureSpec.makeMeasureSpec(selectedView.getMeasuredHeight(), 0);
        int childCount = getChildCount();
        int i = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            if (childAt != selectedView) {
                i += measureChildHeight(childAt, widthMeasureSpec, makeSharedHeightSpec2);
            }
        }
        return i;
    }

    private int measureShiftingChildHeights(int widthMeasureSpec, int maxHeight, int shareCount) {
        int i = 0;
        View childAt = getChildAt(getSelectedItemPosition());
        if (childAt != null) {
            i = measureChildHeight(childAt, widthMeasureSpec, makeSharedHeightSpec(widthMeasureSpec, maxHeight, shareCount));
            maxHeight -= i;
            shareCount--;
        }
        return measureSharedChildHeights(widthMeasureSpec, maxHeight, shareCount, childAt) + i;
    }

    /* access modifiers changed from: protected */
    public NavigationBarItemView createNavigationBarItemView(Context context) {
        return new NavigationRailItemView(context);
    }

    public int getItemMinimumHeight() {
        return this.itemMinimumHeight;
    }

    /* access modifiers changed from: package-private */
    public int getMenuGravity() {
        return this.layoutParams.gravity;
    }

    /* access modifiers changed from: package-private */
    public boolean isTopGravity() {
        return (this.layoutParams.gravity & 112) == 48;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int i = right - left;
        int i2 = 0;
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (childAt.getVisibility() != 8) {
                int measuredHeight = childAt.getMeasuredHeight();
                childAt.layout(0, i2, i, measuredHeight + i2);
                i2 += measuredHeight;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = View.MeasureSpec.getSize(heightMeasureSpec);
        int size2 = getMenu().getVisibleItems().size();
        setMeasuredDimension(View.resolveSizeAndState(View.MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec, 0), View.resolveSizeAndState((size2 <= 1 || !isShifting(getLabelVisibilityMode(), size2)) ? measureSharedChildHeights(widthMeasureSpec, size, size2, (View) null) : measureShiftingChildHeights(widthMeasureSpec, size, size2), heightMeasureSpec, 0));
    }

    public void setItemMinimumHeight(int minHeight) {
        if (this.itemMinimumHeight != minHeight) {
            this.itemMinimumHeight = minHeight;
            requestLayout();
        }
    }

    /* access modifiers changed from: package-private */
    public void setMenuGravity(int gravity) {
        if (this.layoutParams.gravity != gravity) {
            this.layoutParams.gravity = gravity;
            setLayoutParams(this.layoutParams);
        }
    }
}

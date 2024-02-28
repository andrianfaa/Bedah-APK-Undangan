package com.google.android.material.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

class ViewOffsetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    private int tempLeftRightOffset = 0;
    private int tempTopBottomOffset = 0;
    private ViewOffsetHelper viewOffsetHelper;

    public ViewOffsetBehavior() {
    }

    public ViewOffsetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int getLeftAndRightOffset() {
        ViewOffsetHelper viewOffsetHelper2 = this.viewOffsetHelper;
        if (viewOffsetHelper2 != null) {
            return viewOffsetHelper2.getLeftAndRightOffset();
        }
        return 0;
    }

    public int getTopAndBottomOffset() {
        ViewOffsetHelper viewOffsetHelper2 = this.viewOffsetHelper;
        if (viewOffsetHelper2 != null) {
            return viewOffsetHelper2.getTopAndBottomOffset();
        }
        return 0;
    }

    public boolean isHorizontalOffsetEnabled() {
        ViewOffsetHelper viewOffsetHelper2 = this.viewOffsetHelper;
        return viewOffsetHelper2 != null && viewOffsetHelper2.isHorizontalOffsetEnabled();
    }

    public boolean isVerticalOffsetEnabled() {
        ViewOffsetHelper viewOffsetHelper2 = this.viewOffsetHelper;
        return viewOffsetHelper2 != null && viewOffsetHelper2.isVerticalOffsetEnabled();
    }

    /* access modifiers changed from: protected */
    public void layoutChild(CoordinatorLayout parent, V v, int layoutDirection) {
        parent.onLayoutChild(v, layoutDirection);
    }

    public boolean onLayoutChild(CoordinatorLayout parent, V v, int layoutDirection) {
        layoutChild(parent, v, layoutDirection);
        if (this.viewOffsetHelper == null) {
            this.viewOffsetHelper = new ViewOffsetHelper(v);
        }
        this.viewOffsetHelper.onViewLayout();
        this.viewOffsetHelper.applyOffsets();
        int i = this.tempTopBottomOffset;
        if (i != 0) {
            this.viewOffsetHelper.setTopAndBottomOffset(i);
            this.tempTopBottomOffset = 0;
        }
        int i2 = this.tempLeftRightOffset;
        if (i2 == 0) {
            return true;
        }
        this.viewOffsetHelper.setLeftAndRightOffset(i2);
        this.tempLeftRightOffset = 0;
        return true;
    }

    public void setHorizontalOffsetEnabled(boolean horizontalOffsetEnabled) {
        ViewOffsetHelper viewOffsetHelper2 = this.viewOffsetHelper;
        if (viewOffsetHelper2 != null) {
            viewOffsetHelper2.setHorizontalOffsetEnabled(horizontalOffsetEnabled);
        }
    }

    public boolean setLeftAndRightOffset(int offset) {
        ViewOffsetHelper viewOffsetHelper2 = this.viewOffsetHelper;
        if (viewOffsetHelper2 != null) {
            return viewOffsetHelper2.setLeftAndRightOffset(offset);
        }
        this.tempLeftRightOffset = offset;
        return false;
    }

    public boolean setTopAndBottomOffset(int offset) {
        ViewOffsetHelper viewOffsetHelper2 = this.viewOffsetHelper;
        if (viewOffsetHelper2 != null) {
            return viewOffsetHelper2.setTopAndBottomOffset(offset);
        }
        this.tempTopBottomOffset = offset;
        return false;
    }

    public void setVerticalOffsetEnabled(boolean verticalOffsetEnabled) {
        ViewOffsetHelper viewOffsetHelper2 = this.viewOffsetHelper;
        if (viewOffsetHelper2 != null) {
            viewOffsetHelper2.setVerticalOffsetEnabled(verticalOffsetEnabled);
        }
    }
}

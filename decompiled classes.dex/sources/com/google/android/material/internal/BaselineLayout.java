package com.google.android.material.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class BaselineLayout extends ViewGroup {
    private int baseline = -1;

    public BaselineLayout(Context context) {
        super(context, (AttributeSet) null, 0);
    }

    public BaselineLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public BaselineLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getBaseline() {
        return this.baseline;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int paddingLeft = getPaddingLeft();
        int paddingRight = ((right - left) - getPaddingRight()) - paddingLeft;
        int paddingTop = getPaddingTop();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != 8) {
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                int i2 = ((paddingRight - measuredWidth) / 2) + paddingLeft;
                int baseline2 = (this.baseline == -1 || childAt.getBaseline() == -1) ? paddingTop : (this.baseline + paddingTop) - childAt.getBaseline();
                childAt.layout(i2, baseline2, i2 + measuredWidth, baseline2 + measuredHeight);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int i = 0;
        int i2 = 0;
        int i3 = -1;
        int i4 = -1;
        int i5 = 0;
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != 8) {
                measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
                int baseline2 = childAt.getBaseline();
                if (baseline2 != -1) {
                    i3 = Math.max(i3, baseline2);
                    i4 = Math.max(i4, childAt.getMeasuredHeight() - baseline2);
                }
                i = Math.max(i, childAt.getMeasuredWidth());
                i2 = Math.max(i2, childAt.getMeasuredHeight());
                i5 = View.combineMeasuredStates(i5, childAt.getMeasuredState());
            }
        }
        if (i3 != -1) {
            i2 = Math.max(i2, i3 + Math.max(i4, getPaddingBottom()));
            this.baseline = i3;
        }
        setMeasuredDimension(View.resolveSizeAndState(Math.max(i, getSuggestedMinimumWidth()), widthMeasureSpec, i5), View.resolveSizeAndState(Math.max(i2, getSuggestedMinimumHeight()), heightMeasureSpec, i5 << 16));
    }
}

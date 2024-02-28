package com.google.android.material.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;

public class FlowLayout extends ViewGroup {
    private int itemSpacing;
    private int lineSpacing;
    private int rowCount;
    private boolean singleLine;

    public FlowLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.singleLine = false;
        loadFromAttributes(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.singleLine = false;
        loadFromAttributes(context, attrs);
    }

    private static int getMeasuredDimension(int size, int mode, int childrenEdge) {
        switch (mode) {
            case Integer.MIN_VALUE:
                return Math.min(childrenEdge, size);
            case BasicMeasure.EXACTLY:
                return size;
            default:
                return childrenEdge;
        }
    }

    private void loadFromAttributes(Context context, AttributeSet attrs) {
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FlowLayout, 0, 0);
        this.lineSpacing = obtainStyledAttributes.getDimensionPixelSize(R.styleable.FlowLayout_lineSpacing, 0);
        this.itemSpacing = obtainStyledAttributes.getDimensionPixelSize(R.styleable.FlowLayout_itemSpacing, 0);
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: protected */
    public int getItemSpacing() {
        return this.itemSpacing;
    }

    /* access modifiers changed from: protected */
    public int getLineSpacing() {
        return this.lineSpacing;
    }

    /* access modifiers changed from: protected */
    public int getRowCount() {
        return this.rowCount;
    }

    public int getRowIndex(View child) {
        Object tag = child.getTag(R.id.row_index_key);
        if (!(tag instanceof Integer)) {
            return -1;
        }
        return ((Integer) tag).intValue();
    }

    public boolean isSingleLine() {
        return this.singleLine;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean sizeChanged, int left, int top, int right, int bottom) {
        int i;
        boolean z = false;
        if (getChildCount() == 0) {
            this.rowCount = 0;
            return;
        }
        this.rowCount = 1;
        if (ViewCompat.getLayoutDirection(this) == 1) {
            z = true;
        }
        int paddingRight = z ? getPaddingRight() : getPaddingLeft();
        int paddingLeft = z ? getPaddingLeft() : getPaddingRight();
        int i2 = paddingRight;
        int paddingTop = getPaddingTop();
        int i3 = paddingTop;
        int i4 = (right - left) - paddingLeft;
        int i5 = 0;
        while (i5 < getChildCount()) {
            View childAt = getChildAt(i5);
            if (childAt.getVisibility() == 8) {
                childAt.setTag(R.id.row_index_key, -1);
                i = paddingRight;
            } else {
                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                int i6 = 0;
                int i7 = 0;
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    i6 = MarginLayoutParamsCompat.getMarginStart(marginLayoutParams);
                    i7 = MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams);
                }
                int measuredWidth = i2 + i6 + childAt.getMeasuredWidth();
                if (!this.singleLine && measuredWidth > i4) {
                    i2 = paddingRight;
                    paddingTop = i3 + this.lineSpacing;
                    this.rowCount++;
                }
                i = paddingRight;
                childAt.setTag(R.id.row_index_key, Integer.valueOf(this.rowCount - 1));
                int measuredWidth2 = i2 + i6 + childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight() + paddingTop;
                if (z) {
                    childAt.layout(i4 - measuredWidth2, paddingTop, (i4 - i2) - i6, measuredHeight);
                } else {
                    childAt.layout(i2 + i6, paddingTop, measuredWidth2, measuredHeight);
                }
                i2 += i6 + i7 + childAt.getMeasuredWidth() + this.itemSpacing;
                i3 = measuredHeight;
            }
            i5++;
            paddingRight = i;
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int size = View.MeasureSpec.getSize(widthMeasureSpec);
        int mode = View.MeasureSpec.getMode(widthMeasureSpec);
        int size2 = View.MeasureSpec.getSize(heightMeasureSpec);
        int mode2 = View.MeasureSpec.getMode(heightMeasureSpec);
        int i2 = (mode == Integer.MIN_VALUE || mode == 1073741824) ? size : Integer.MAX_VALUE;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int i3 = paddingTop;
        int i4 = paddingLeft;
        int i5 = 0;
        int paddingRight = i2 - getPaddingRight();
        int i6 = 0;
        while (i6 < getChildCount()) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() == 8) {
                int i7 = widthMeasureSpec;
                int i8 = heightMeasureSpec;
                i = i2;
            } else {
                measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
                i = i2;
                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                int i9 = 0;
                int i10 = 0;
                int i11 = paddingTop;
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    ViewGroup.LayoutParams layoutParams2 = layoutParams;
                    i9 = 0 + marginLayoutParams.leftMargin;
                    i10 = 0 + marginLayoutParams.rightMargin;
                } else {
                    ViewGroup.LayoutParams layoutParams3 = layoutParams;
                }
                if (paddingLeft + i9 + childAt.getMeasuredWidth() <= paddingRight || isSingleLine()) {
                    paddingTop = i11;
                } else {
                    paddingLeft = getPaddingLeft();
                    paddingTop = this.lineSpacing + i3;
                }
                int measuredWidth = paddingLeft + i9 + childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight() + paddingTop;
                if (measuredWidth > i5) {
                    i5 = measuredWidth;
                }
                int i12 = measuredHeight;
                paddingLeft += i9 + i10 + childAt.getMeasuredWidth() + this.itemSpacing;
                if (i6 == getChildCount() - 1) {
                    i5 += i10;
                    i3 = i12;
                } else {
                    i3 = i12;
                }
            }
            i6++;
            i2 = i;
        }
        int i13 = i2;
        int i14 = paddingTop;
        setMeasuredDimension(getMeasuredDimension(size, mode, i5 + getPaddingRight()), getMeasuredDimension(size2, mode2, i3 + getPaddingBottom()));
    }

    /* access modifiers changed from: protected */
    public void setItemSpacing(int itemSpacing2) {
        this.itemSpacing = itemSpacing2;
    }

    /* access modifiers changed from: protected */
    public void setLineSpacing(int lineSpacing2) {
        this.lineSpacing = lineSpacing2;
    }

    public void setSingleLine(boolean singleLine2) {
        this.singleLine = singleLine2;
    }
}

package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.R;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;

public class AlertDialogLayout extends LinearLayoutCompat {
    public AlertDialogLayout(Context context) {
        super(context);
    }

    public AlertDialogLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void forceUniformWidth(int count, int heightMeasureSpec) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), BasicMeasure.EXACTLY);
        for (int i = 0; i < count; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != 8) {
                LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams) childAt.getLayoutParams();
                if (layoutParams.width == -1) {
                    int i2 = layoutParams.height;
                    layoutParams.height = childAt.getMeasuredHeight();
                    measureChildWithMargins(childAt, makeMeasureSpec, 0, heightMeasureSpec, 0);
                    layoutParams.height = i2;
                }
            }
        }
    }

    private static int resolveMinimumHeight(View v) {
        int minimumHeight = ViewCompat.getMinimumHeight(v);
        if (minimumHeight > 0) {
            return minimumHeight;
        }
        if (v instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) v;
            if (viewGroup.getChildCount() == 1) {
                return resolveMinimumHeight(viewGroup.getChildAt(0));
            }
        }
        return 0;
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }

    private boolean tryOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int i2 = widthMeasureSpec;
        int i3 = heightMeasureSpec;
        View view = null;
        View view2 = null;
        View view3 = null;
        int childCount = getChildCount();
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = getChildAt(i4);
            if (childAt.getVisibility() != 8) {
                int id = childAt.getId();
                if (id == R.id.topPanel) {
                    view = childAt;
                } else if (id == R.id.buttonPanel) {
                    view2 = childAt;
                } else if ((id != R.id.contentPanel && id != R.id.customPanel) || view3 != null) {
                    return false;
                } else {
                    view3 = childAt;
                }
            }
        }
        int mode = View.MeasureSpec.getMode(heightMeasureSpec);
        int size = View.MeasureSpec.getSize(heightMeasureSpec);
        int mode2 = View.MeasureSpec.getMode(widthMeasureSpec);
        int i5 = 0;
        int paddingTop = getPaddingTop() + getPaddingBottom();
        if (view != null) {
            view.measure(i2, 0);
            paddingTop += view.getMeasuredHeight();
            i5 = View.combineMeasuredStates(0, view.getMeasuredState());
        }
        int i6 = 0;
        int i7 = 0;
        if (view2 != null) {
            view2.measure(i2, 0);
            i6 = resolveMinimumHeight(view2);
            i7 = view2.getMeasuredHeight() - i6;
            paddingTop += i6;
            i5 = View.combineMeasuredStates(i5, view2.getMeasuredState());
        }
        int i8 = 0;
        if (view3 != null) {
            if (mode == 0) {
                View view4 = view;
                i = 0;
            } else {
                View view5 = view;
                i = View.MeasureSpec.makeMeasureSpec(Math.max(0, size - paddingTop), mode);
            }
            view3.measure(i2, i);
            i8 = view3.getMeasuredHeight();
            paddingTop += i8;
            i5 = View.combineMeasuredStates(i5, view3.getMeasuredState());
        } else {
            View view6 = view;
        }
        int i9 = size - paddingTop;
        if (view2 != null) {
            int i10 = paddingTop - i6;
            int min = Math.min(i9, i7);
            if (min > 0) {
                i9 -= min;
                i6 += min;
            }
            view2.measure(i2, View.MeasureSpec.makeMeasureSpec(i6, BasicMeasure.EXACTLY));
            paddingTop = i10 + view2.getMeasuredHeight();
            i5 = View.combineMeasuredStates(i5, view2.getMeasuredState());
            i9 = i9;
        }
        if (view3 != null && i9 > 0) {
            int i11 = i9;
            int i12 = i9 - i11;
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i8 + i11, mode);
            view3.measure(i2, makeMeasureSpec);
            paddingTop = (paddingTop - i8) + view3.getMeasuredHeight();
            int i13 = makeMeasureSpec;
            i5 = View.combineMeasuredStates(i5, view3.getMeasuredState());
            i9 = i12;
        }
        int i14 = 0;
        int i15 = i9;
        int i16 = 0;
        while (i16 < childCount) {
            View childAt2 = getChildAt(i16);
            View view7 = view2;
            View view8 = view3;
            if (childAt2.getVisibility() != 8) {
                i14 = Math.max(i14, childAt2.getMeasuredWidth());
            }
            i16++;
            view2 = view7;
            view3 = view8;
        }
        View view9 = view2;
        View view10 = view3;
        setMeasuredDimension(View.resolveSizeAndState(i14 + getPaddingLeft() + getPaddingRight(), i2, i5), View.resolveSizeAndState(paddingTop, i3, 0));
        if (mode2 == 1073741824) {
            return true;
        }
        forceUniformWidth(childCount, i3);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int i;
        int i2;
        int i3;
        AlertDialogLayout alertDialogLayout = this;
        int paddingLeft = getPaddingLeft();
        int i4 = right - left;
        int paddingRight = i4 - getPaddingRight();
        int paddingRight2 = (i4 - paddingLeft) - getPaddingRight();
        int measuredHeight = getMeasuredHeight();
        int childCount = getChildCount();
        int gravity = getGravity();
        int i5 = gravity & 112;
        int i6 = gravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        switch (i5) {
            case 16:
                i = getPaddingTop() + (((bottom - top) - measuredHeight) / 2);
                break;
            case 80:
                i = ((getPaddingTop() + bottom) - top) - measuredHeight;
                break;
            default:
                i = getPaddingTop();
                break;
        }
        Drawable dividerDrawable = getDividerDrawable();
        int intrinsicHeight = dividerDrawable == null ? 0 : dividerDrawable.getIntrinsicHeight();
        int i7 = 0;
        while (i7 < childCount) {
            View childAt = alertDialogLayout.getChildAt(i7);
            if (childAt == null || childAt.getVisibility() == 8) {
                i2 = i7;
            } else {
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight2 = childAt.getMeasuredHeight();
                LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams) childAt.getLayoutParams();
                int i8 = layoutParams.gravity;
                int i9 = i8 < 0 ? i6 : i8;
                int layoutDirection = ViewCompat.getLayoutDirection(this);
                switch (GravityCompat.getAbsoluteGravity(i9, layoutDirection) & 7) {
                    case 1:
                        int i10 = layoutDirection;
                        i3 = ((((paddingRight2 - measuredWidth) / 2) + paddingLeft) + layoutParams.leftMargin) - layoutParams.rightMargin;
                        break;
                    case 5:
                        int i11 = layoutDirection;
                        i3 = (paddingRight - measuredWidth) - layoutParams.rightMargin;
                        break;
                    default:
                        int i12 = layoutDirection;
                        i3 = layoutParams.leftMargin + paddingLeft;
                        break;
                }
                if (alertDialogLayout.hasDividerBeforeChildAt(i7)) {
                    i += intrinsicHeight;
                }
                int i13 = i + layoutParams.topMargin;
                int i14 = i9;
                i2 = i7;
                setChildFrame(childAt, i3, i13, measuredWidth, measuredHeight2);
                i = i13 + measuredHeight2 + layoutParams.bottomMargin;
            }
            i7 = i2 + 1;
            alertDialogLayout = this;
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!tryOnMeasure(widthMeasureSpec, heightMeasureSpec)) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}

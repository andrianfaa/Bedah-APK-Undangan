package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.R;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;

public class ButtonBarLayout extends LinearLayout {
    private static final int PEEK_BUTTON_DP = 16;
    private boolean mAllowStacking;
    private int mLastWidthSize = -1;
    private boolean mStacked;

    public ButtonBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ButtonBarLayout);
        ViewCompat.saveAttributeDataForStyleable(this, context, R.styleable.ButtonBarLayout, attrs, obtainStyledAttributes, 0, 0);
        this.mAllowStacking = obtainStyledAttributes.getBoolean(R.styleable.ButtonBarLayout_allowStacking, true);
        obtainStyledAttributes.recycle();
        if (getOrientation() == 1) {
            setStacked(this.mAllowStacking);
        }
    }

    private int getNextVisibleChildIndex(int index) {
        int childCount = getChildCount();
        for (int i = index; i < childCount; i++) {
            if (getChildAt(i).getVisibility() == 0) {
                return i;
            }
        }
        return -1;
    }

    private boolean isStacked() {
        return this.mStacked;
    }

    private void setStacked(boolean stacked) {
        if (this.mStacked == stacked) {
            return;
        }
        if (!stacked || this.mAllowStacking) {
            this.mStacked = stacked;
            setOrientation(stacked);
            setGravity(stacked ? GravityCompat.END : 80);
            View findViewById = findViewById(R.id.spacer);
            if (findViewById != null) {
                findViewById.setVisibility(stacked ? 8 : 4);
            }
            for (int childCount = getChildCount() - 2; childCount >= 0; childCount--) {
                bringChildToFront(getChildAt(childCount));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int size = View.MeasureSpec.getSize(widthMeasureSpec);
        if (this.mAllowStacking) {
            if (size > this.mLastWidthSize && isStacked()) {
                setStacked(false);
            }
            this.mLastWidthSize = size;
        }
        boolean z = false;
        if (isStacked() || View.MeasureSpec.getMode(widthMeasureSpec) != 1073741824) {
            i = widthMeasureSpec;
        } else {
            i = View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE);
            z = true;
        }
        super.onMeasure(i, heightMeasureSpec);
        if (this.mAllowStacking && !isStacked()) {
            if ((-16777216 & getMeasuredWidthAndState()) == 16777216) {
                setStacked(true);
                z = true;
            }
        }
        if (z) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        int i2 = 0;
        int nextVisibleChildIndex = getNextVisibleChildIndex(0);
        if (nextVisibleChildIndex >= 0) {
            View childAt = getChildAt(nextVisibleChildIndex);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) childAt.getLayoutParams();
            i2 = 0 + getPaddingTop() + childAt.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            if (isStacked()) {
                int nextVisibleChildIndex2 = getNextVisibleChildIndex(nextVisibleChildIndex + 1);
                if (nextVisibleChildIndex2 >= 0) {
                    i2 += getChildAt(nextVisibleChildIndex2).getPaddingTop() + ((int) (getResources().getDisplayMetrics().density * 16.0f));
                }
            } else {
                i2 += getPaddingBottom();
            }
        }
        if (ViewCompat.getMinimumHeight(this) != i2) {
            setMinimumHeight(i2);
            if (heightMeasureSpec == 0) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    public void setAllowStacking(boolean allowStacking) {
        if (this.mAllowStacking != allowStacking) {
            this.mAllowStacking = allowStacking;
            if (!allowStacking && isStacked()) {
                setStacked(false);
            }
            requestLayout();
        }
    }
}

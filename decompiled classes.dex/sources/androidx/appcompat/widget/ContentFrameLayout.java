package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.core.view.ViewCompat;

public class ContentFrameLayout extends FrameLayout {
    private OnAttachListener mAttachListener;
    private final Rect mDecorPadding;
    private TypedValue mFixedHeightMajor;
    private TypedValue mFixedHeightMinor;
    private TypedValue mFixedWidthMajor;
    private TypedValue mFixedWidthMinor;
    private TypedValue mMinWidthMajor;
    private TypedValue mMinWidthMinor;

    public interface OnAttachListener {
        void onAttachedFromWindow();

        void onDetachedFromWindow();
    }

    public ContentFrameLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public ContentFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContentFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mDecorPadding = new Rect();
    }

    public void dispatchFitSystemWindows(Rect insets) {
        fitSystemWindows(insets);
    }

    public TypedValue getFixedHeightMajor() {
        if (this.mFixedHeightMajor == null) {
            this.mFixedHeightMajor = new TypedValue();
        }
        return this.mFixedHeightMajor;
    }

    public TypedValue getFixedHeightMinor() {
        if (this.mFixedHeightMinor == null) {
            this.mFixedHeightMinor = new TypedValue();
        }
        return this.mFixedHeightMinor;
    }

    public TypedValue getFixedWidthMajor() {
        if (this.mFixedWidthMajor == null) {
            this.mFixedWidthMajor = new TypedValue();
        }
        return this.mFixedWidthMajor;
    }

    public TypedValue getFixedWidthMinor() {
        if (this.mFixedWidthMinor == null) {
            this.mFixedWidthMinor = new TypedValue();
        }
        return this.mFixedWidthMinor;
    }

    public TypedValue getMinWidthMajor() {
        if (this.mMinWidthMajor == null) {
            this.mMinWidthMajor = new TypedValue();
        }
        return this.mMinWidthMajor;
    }

    public TypedValue getMinWidthMinor() {
        if (this.mMinWidthMinor == null) {
            this.mMinWidthMinor = new TypedValue();
        }
        return this.mMinWidthMinor;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        OnAttachListener onAttachListener = this.mAttachListener;
        if (onAttachListener != null) {
            onAttachListener.onAttachedFromWindow();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        OnAttachListener onAttachListener = this.mAttachListener;
        if (onAttachListener != null) {
            onAttachListener.onDetachedFromWindow();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        boolean z = displayMetrics.widthPixels < displayMetrics.heightPixels;
        int mode = View.MeasureSpec.getMode(widthMeasureSpec);
        int mode2 = View.MeasureSpec.getMode(heightMeasureSpec);
        boolean z2 = false;
        if (mode == Integer.MIN_VALUE) {
            TypedValue typedValue = z ? this.mFixedWidthMinor : this.mFixedWidthMajor;
            if (!(typedValue == null || typedValue.type == 0)) {
                int i = 0;
                if (typedValue.type == 5) {
                    i = (int) typedValue.getDimension(displayMetrics);
                } else if (typedValue.type == 6) {
                    i = (int) typedValue.getFraction((float) displayMetrics.widthPixels, (float) displayMetrics.widthPixels);
                }
                if (i > 0) {
                    widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(Math.min(i - (this.mDecorPadding.left + this.mDecorPadding.right), View.MeasureSpec.getSize(widthMeasureSpec)), BasicMeasure.EXACTLY);
                    z2 = true;
                }
            }
        }
        if (mode2 == Integer.MIN_VALUE) {
            TypedValue typedValue2 = z ? this.mFixedHeightMajor : this.mFixedHeightMinor;
            if (!(typedValue2 == null || typedValue2.type == 0)) {
                int i2 = 0;
                if (typedValue2.type == 5) {
                    i2 = (int) typedValue2.getDimension(displayMetrics);
                } else if (typedValue2.type == 6) {
                    i2 = (int) typedValue2.getFraction((float) displayMetrics.heightPixels, (float) displayMetrics.heightPixels);
                }
                if (i2 > 0) {
                    heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(Math.min(i2 - (this.mDecorPadding.top + this.mDecorPadding.bottom), View.MeasureSpec.getSize(heightMeasureSpec)), BasicMeasure.EXACTLY);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        boolean z3 = false;
        int widthMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(measuredWidth, BasicMeasure.EXACTLY);
        if (!z2 && mode == Integer.MIN_VALUE) {
            TypedValue typedValue3 = z ? this.mMinWidthMinor : this.mMinWidthMajor;
            if (!(typedValue3 == null || typedValue3.type == 0)) {
                int i3 = 0;
                if (typedValue3.type == 5) {
                    i3 = (int) typedValue3.getDimension(displayMetrics);
                } else if (typedValue3.type == 6) {
                    i3 = (int) typedValue3.getFraction((float) displayMetrics.widthPixels, (float) displayMetrics.widthPixels);
                }
                if (i3 > 0) {
                    i3 -= this.mDecorPadding.left + this.mDecorPadding.right;
                }
                if (measuredWidth < i3) {
                    widthMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(i3, BasicMeasure.EXACTLY);
                    z3 = true;
                }
            }
        }
        if (z3) {
            super.onMeasure(widthMeasureSpec2, heightMeasureSpec);
        }
    }

    public void setAttachListener(OnAttachListener attachListener) {
        this.mAttachListener = attachListener;
    }

    public void setDecorPadding(int left, int top, int right, int bottom) {
        this.mDecorPadding.set(left, top, right, bottom);
        if (ViewCompat.isLaidOut(this)) {
            requestLayout();
        }
    }
}

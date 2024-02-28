package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.R;
import androidx.core.view.ViewCompat;

public class ActionBarContainer extends FrameLayout {
    private View mActionBarView;
    Drawable mBackground;
    private View mContextView;
    private int mHeight;
    boolean mIsSplit;
    boolean mIsStacked;
    private boolean mIsTransitioning;
    Drawable mSplitBackground;
    Drawable mStackedBackground;
    private View mTabContainer;

    private static class Api21Impl {
        private Api21Impl() {
        }

        public static void invalidateOutline(ActionBarContainer drawable) {
            drawable.invalidateOutline();
        }
    }

    public ActionBarContainer(Context context) {
        this(context, (AttributeSet) null);
    }

    public ActionBarContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewCompat.setBackground(this, new ActionBarBackgroundDrawable(this));
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ActionBar);
        this.mBackground = obtainStyledAttributes.getDrawable(R.styleable.ActionBar_background);
        this.mStackedBackground = obtainStyledAttributes.getDrawable(R.styleable.ActionBar_backgroundStacked);
        this.mHeight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.ActionBar_height, -1);
        boolean z = true;
        if (getId() == R.id.split_action_bar) {
            this.mIsSplit = true;
            this.mSplitBackground = obtainStyledAttributes.getDrawable(R.styleable.ActionBar_backgroundSplit);
        }
        obtainStyledAttributes.recycle();
        if (this.mIsSplit) {
            if (this.mSplitBackground != null) {
                z = false;
            }
        } else if (!(this.mBackground == null && this.mStackedBackground == null)) {
            z = false;
        }
        setWillNotDraw(z);
    }

    private int getMeasuredHeightWithMargins(View view) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        return view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    private boolean isCollapsed(View view) {
        return view == null || view.getVisibility() == 8 || view.getMeasuredHeight() == 0;
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.mBackground;
        if (drawable != null && drawable.isStateful()) {
            this.mBackground.setState(getDrawableState());
        }
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null && drawable2.isStateful()) {
            this.mStackedBackground.setState(getDrawableState());
        }
        Drawable drawable3 = this.mSplitBackground;
        if (drawable3 != null && drawable3.isStateful()) {
            this.mSplitBackground.setState(getDrawableState());
        }
    }

    public View getTabContainer() {
        return this.mTabContainer;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
        }
        Drawable drawable3 = this.mSplitBackground;
        if (drawable3 != null) {
            drawable3.jumpToCurrentState();
        }
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBarView = findViewById(R.id.action_bar);
        this.mContextView = findViewById(R.id.action_context_bar);
    }

    public boolean onHoverEvent(MotionEvent ev) {
        super.onHoverEvent(ev);
        return true;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.mIsTransitioning || super.onInterceptTouchEvent(ev);
    }

    public void onLayout(boolean changed, int l, int t, int r, int b) {
        Drawable drawable;
        super.onLayout(changed, l, t, r, b);
        View view = this.mTabContainer;
        boolean z = (view == null || view.getVisibility() == 8) ? false : true;
        if (!(view == null || view.getVisibility() == 8)) {
            int measuredHeight = getMeasuredHeight();
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
            view.layout(l, (measuredHeight - view.getMeasuredHeight()) - layoutParams.bottomMargin, r, measuredHeight - layoutParams.bottomMargin);
        }
        boolean z2 = false;
        if (this.mIsSplit) {
            Drawable drawable2 = this.mSplitBackground;
            if (drawable2 != null) {
                drawable2.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                z2 = true;
            }
        } else {
            if (this.mBackground != null) {
                if (this.mActionBarView.getVisibility() == 0) {
                    this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
                } else {
                    View view2 = this.mContextView;
                    if (view2 == null || view2.getVisibility() != 0) {
                        this.mBackground.setBounds(0, 0, 0, 0);
                    } else {
                        this.mBackground.setBounds(this.mContextView.getLeft(), this.mContextView.getTop(), this.mContextView.getRight(), this.mContextView.getBottom());
                    }
                }
                z2 = true;
            }
            this.mIsStacked = z;
            if (z && (drawable = this.mStackedBackground) != null) {
                drawable.setBounds(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                z2 = true;
            }
        }
        if (z2) {
            invalidate();
        }
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        if (this.mActionBarView == null && View.MeasureSpec.getMode(heightMeasureSpec) == Integer.MIN_VALUE && (i = this.mHeight) >= 0) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(Math.min(i, View.MeasureSpec.getSize(heightMeasureSpec)), Integer.MIN_VALUE);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mActionBarView != null) {
            int mode = View.MeasureSpec.getMode(heightMeasureSpec);
            View view = this.mTabContainer;
            if (view != null && view.getVisibility() != 8 && mode != 1073741824) {
                setMeasuredDimension(getMeasuredWidth(), Math.min(getMeasuredHeightWithMargins(this.mTabContainer) + (!isCollapsed(this.mActionBarView) ? getMeasuredHeightWithMargins(this.mActionBarView) : !isCollapsed(this.mContextView) ? getMeasuredHeightWithMargins(this.mContextView) : 0), mode == Integer.MIN_VALUE ? View.MeasureSpec.getSize(heightMeasureSpec) : Integer.MAX_VALUE));
            }
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        return true;
    }

    public void setPrimaryBackground(Drawable bg) {
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.setCallback((Drawable.Callback) null);
            unscheduleDrawable(this.mBackground);
        }
        this.mBackground = bg;
        if (bg != null) {
            bg.setCallback(this);
            View view = this.mActionBarView;
            if (view != null) {
                this.mBackground.setBounds(view.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
            }
        }
        boolean z = true;
        if (this.mIsSplit) {
            if (this.mSplitBackground != null) {
                z = false;
            }
        } else if (!(this.mBackground == null && this.mStackedBackground == null)) {
            z = false;
        }
        setWillNotDraw(z);
        invalidate();
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.invalidateOutline(this);
        }
    }

    public void setSplitBackground(Drawable bg) {
        Drawable drawable;
        Drawable drawable2 = this.mSplitBackground;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback) null);
            unscheduleDrawable(this.mSplitBackground);
        }
        this.mSplitBackground = bg;
        boolean z = false;
        if (bg != null) {
            bg.setCallback(this);
            if (this.mIsSplit && (drawable = this.mSplitBackground) != null) {
                drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            }
        }
        if (this.mIsSplit) {
            if (this.mSplitBackground == null) {
                z = true;
            }
        } else if (this.mBackground == null && this.mStackedBackground == null) {
            z = true;
        }
        setWillNotDraw(z);
        invalidate();
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.invalidateOutline(this);
        }
    }

    public void setStackedBackground(Drawable bg) {
        Drawable drawable;
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback) null);
            unscheduleDrawable(this.mStackedBackground);
        }
        this.mStackedBackground = bg;
        if (bg != null) {
            bg.setCallback(this);
            if (this.mIsStacked && (drawable = this.mStackedBackground) != null) {
                drawable.setBounds(this.mTabContainer.getLeft(), this.mTabContainer.getTop(), this.mTabContainer.getRight(), this.mTabContainer.getBottom());
            }
        }
        boolean z = true;
        if (this.mIsSplit) {
            if (this.mSplitBackground != null) {
                z = false;
            }
        } else if (!(this.mBackground == null && this.mStackedBackground == null)) {
            z = false;
        }
        setWillNotDraw(z);
        invalidate();
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.invalidateOutline(this);
        }
    }

    public void setTabContainer(ScrollingTabContainerView tabView) {
        View view = this.mTabContainer;
        if (view != null) {
            removeView(view);
        }
        this.mTabContainer = tabView;
        if (tabView != null) {
            addView(tabView);
            ViewGroup.LayoutParams layoutParams = tabView.getLayoutParams();
            layoutParams.width = -1;
            layoutParams.height = -2;
            tabView.setAllowCollapse(false);
        }
    }

    public void setTransitioning(boolean isTransitioning) {
        this.mIsTransitioning = isTransitioning;
        setDescendantFocusability(isTransitioning ? 393216 : 262144);
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        boolean z = visibility == 0;
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.setVisible(z, false);
        }
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null) {
            drawable2.setVisible(z, false);
        }
        Drawable drawable3 = this.mSplitBackground;
        if (drawable3 != null) {
            drawable3.setVisible(z, false);
        }
    }

    public ActionMode startActionModeForChild(View child, ActionMode.Callback callback) {
        return null;
    }

    public ActionMode startActionModeForChild(View child, ActionMode.Callback callback, int type) {
        if (type != 0) {
            return super.startActionModeForChild(child, callback, type);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable who) {
        return (who == this.mBackground && !this.mIsSplit) || (who == this.mStackedBackground && this.mIsStacked) || ((who == this.mSplitBackground && this.mIsSplit) || super.verifyDrawable(who));
    }
}

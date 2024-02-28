package com.google.android.material.navigationrail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.widget.TintTypedArray;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.R;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.navigation.NavigationBarView;

public class NavigationRailView extends NavigationBarView {
    private static final int DEFAULT_HEADER_GRAVITY = 49;
    static final int DEFAULT_MENU_GRAVITY = 49;
    static final int MAX_ITEM_COUNT = 7;
    static final int NO_ITEM_MINIMUM_HEIGHT = -1;
    private View headerView;
    /* access modifiers changed from: private */
    public Boolean paddingBottomSystemWindowInsets;
    /* access modifiers changed from: private */
    public Boolean paddingTopSystemWindowInsets;
    private final int topMargin;

    public NavigationRailView(Context context) {
        this(context, (AttributeSet) null);
    }

    public NavigationRailView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.navigationRailStyle);
    }

    public NavigationRailView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.Widget_MaterialComponents_NavigationRailView);
    }

    public NavigationRailView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.paddingTopSystemWindowInsets = null;
        this.paddingBottomSystemWindowInsets = null;
        this.topMargin = getResources().getDimensionPixelSize(R.dimen.mtrl_navigation_rail_margin);
        TintTypedArray obtainTintedStyledAttributes = ThemeEnforcement.obtainTintedStyledAttributes(getContext(), attrs, R.styleable.NavigationRailView, defStyleAttr, defStyleRes, new int[0]);
        int resourceId = obtainTintedStyledAttributes.getResourceId(R.styleable.NavigationRailView_headerLayout, 0);
        if (resourceId != 0) {
            addHeaderView(resourceId);
        }
        setMenuGravity(obtainTintedStyledAttributes.getInt(R.styleable.NavigationRailView_menuGravity, 49));
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationRailView_itemMinHeight)) {
            setItemMinimumHeight(obtainTintedStyledAttributes.getDimensionPixelSize(R.styleable.NavigationRailView_itemMinHeight, -1));
        }
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationRailView_paddingTopSystemWindowInsets)) {
            this.paddingTopSystemWindowInsets = Boolean.valueOf(obtainTintedStyledAttributes.getBoolean(R.styleable.NavigationRailView_paddingTopSystemWindowInsets, false));
        }
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationRailView_paddingBottomSystemWindowInsets)) {
            this.paddingBottomSystemWindowInsets = Boolean.valueOf(obtainTintedStyledAttributes.getBoolean(R.styleable.NavigationRailView_paddingBottomSystemWindowInsets, false));
        }
        obtainTintedStyledAttributes.recycle();
        applyWindowInsets();
    }

    private void applyWindowInsets() {
        ViewUtils.doOnApplyWindowInsets(this, new ViewUtils.OnApplyWindowInsetsListener() {
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat insets, ViewUtils.RelativePadding initialPadding) {
                NavigationRailView navigationRailView = NavigationRailView.this;
                if (navigationRailView.shouldApplyWindowInsetPadding(navigationRailView.paddingTopSystemWindowInsets)) {
                    initialPadding.top += insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
                }
                NavigationRailView navigationRailView2 = NavigationRailView.this;
                if (navigationRailView2.shouldApplyWindowInsetPadding(navigationRailView2.paddingBottomSystemWindowInsets)) {
                    initialPadding.bottom += insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
                }
                boolean z = true;
                if (ViewCompat.getLayoutDirection(view) != 1) {
                    z = false;
                }
                initialPadding.start += z ? insets.getSystemWindowInsetRight() : insets.getSystemWindowInsetLeft();
                initialPadding.applyToView(view);
                return insets;
            }
        });
    }

    private NavigationRailMenuView getNavigationRailMenuView() {
        return (NavigationRailMenuView) getMenuView();
    }

    private boolean isHeaderViewVisible() {
        View view = this.headerView;
        return (view == null || view.getVisibility() == 8) ? false : true;
    }

    private int makeMinWidthSpec(int measureSpec) {
        int suggestedMinimumWidth = getSuggestedMinimumWidth();
        if (View.MeasureSpec.getMode(measureSpec) == 1073741824 || suggestedMinimumWidth <= 0) {
            return measureSpec;
        }
        return View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(measureSpec), suggestedMinimumWidth + getPaddingLeft() + getPaddingRight()), BasicMeasure.EXACTLY);
    }

    /* access modifiers changed from: private */
    public boolean shouldApplyWindowInsetPadding(Boolean paddingInsetFlag) {
        return paddingInsetFlag != null ? paddingInsetFlag.booleanValue() : ViewCompat.getFitsSystemWindows(this);
    }

    public void addHeaderView(int layoutRes) {
        addHeaderView(LayoutInflater.from(getContext()).inflate(layoutRes, this, false));
    }

    public void addHeaderView(View headerView2) {
        removeHeaderView();
        this.headerView = headerView2;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 49;
        layoutParams.topMargin = this.topMargin;
        addView(headerView2, 0, layoutParams);
    }

    /* access modifiers changed from: protected */
    public NavigationRailMenuView createNavigationBarMenuView(Context context) {
        return new NavigationRailMenuView(context);
    }

    public View getHeaderView() {
        return this.headerView;
    }

    public int getItemMinimumHeight() {
        return ((NavigationRailMenuView) getMenuView()).getItemMinimumHeight();
    }

    public int getMaxItemCount() {
        return 7;
    }

    public int getMenuGravity() {
        return getNavigationRailMenuView().getMenuGravity();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        NavigationRailMenuView navigationRailMenuView = getNavigationRailMenuView();
        int i = 0;
        if (isHeaderViewVisible()) {
            int bottom2 = this.headerView.getBottom() + this.topMargin;
            int top2 = navigationRailMenuView.getTop();
            if (top2 < bottom2) {
                i = bottom2 - top2;
            }
        } else if (navigationRailMenuView.isTopGravity()) {
            i = this.topMargin;
        }
        if (i > 0) {
            navigationRailMenuView.layout(navigationRailMenuView.getLeft(), navigationRailMenuView.getTop() + i, navigationRailMenuView.getRight(), navigationRailMenuView.getBottom() + i);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int makeMinWidthSpec = makeMinWidthSpec(widthMeasureSpec);
        super.onMeasure(makeMinWidthSpec, heightMeasureSpec);
        if (isHeaderViewVisible()) {
            measureChild(getNavigationRailMenuView(), makeMinWidthSpec, View.MeasureSpec.makeMeasureSpec((getMeasuredHeight() - this.headerView.getMeasuredHeight()) - this.topMargin, Integer.MIN_VALUE));
        }
    }

    public void removeHeaderView() {
        View view = this.headerView;
        if (view != null) {
            removeView(view);
            this.headerView = null;
        }
    }

    public void setItemMinimumHeight(int minHeight) {
        ((NavigationRailMenuView) getMenuView()).setItemMinimumHeight(minHeight);
    }

    public void setMenuGravity(int gravity) {
        getNavigationRailMenuView().setMenuGravity(gravity);
    }
}

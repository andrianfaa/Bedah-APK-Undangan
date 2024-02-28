package com.google.android.material.bottomnavigation;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.widget.TintTypedArray;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.R;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.navigation.NavigationBarMenuView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.shape.MaterialShapeDrawable;

public class BottomNavigationView extends NavigationBarView {
    static final int MAX_ITEM_COUNT = 5;

    @Deprecated
    public interface OnNavigationItemReselectedListener extends NavigationBarView.OnItemReselectedListener {
    }

    @Deprecated
    public interface OnNavigationItemSelectedListener extends NavigationBarView.OnItemSelectedListener {
    }

    public BottomNavigationView(Context context) {
        this(context, (AttributeSet) null);
    }

    public BottomNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.bottomNavigationStyle);
    }

    public BottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.Widget_Design_BottomNavigationView);
    }

    public BottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Context context2 = getContext();
        TintTypedArray obtainTintedStyledAttributes = ThemeEnforcement.obtainTintedStyledAttributes(context2, attrs, R.styleable.BottomNavigationView, defStyleAttr, defStyleRes, new int[0]);
        setItemHorizontalTranslationEnabled(obtainTintedStyledAttributes.getBoolean(R.styleable.BottomNavigationView_itemHorizontalTranslationEnabled, true));
        if (obtainTintedStyledAttributes.hasValue(R.styleable.BottomNavigationView_android_minHeight)) {
            setMinimumHeight(obtainTintedStyledAttributes.getDimensionPixelSize(R.styleable.BottomNavigationView_android_minHeight, 0));
        }
        obtainTintedStyledAttributes.recycle();
        if (shouldDrawCompatibilityTopDivider()) {
            addCompatibilityTopDivider(context2);
        }
        applyWindowInsets();
    }

    private void addCompatibilityTopDivider(Context context) {
        View view = new View(context);
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.design_bottom_navigation_shadow_color));
        view.setLayoutParams(new FrameLayout.LayoutParams(-1, getResources().getDimensionPixelSize(R.dimen.design_bottom_navigation_shadow_height)));
        addView(view);
    }

    private void applyWindowInsets() {
        ViewUtils.doOnApplyWindowInsets(this, new ViewUtils.OnApplyWindowInsetsListener() {
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat insets, ViewUtils.RelativePadding initialPadding) {
                initialPadding.bottom += insets.getSystemWindowInsetBottom();
                boolean z = true;
                if (ViewCompat.getLayoutDirection(view) != 1) {
                    z = false;
                }
                boolean z2 = z;
                int systemWindowInsetLeft = insets.getSystemWindowInsetLeft();
                int systemWindowInsetRight = insets.getSystemWindowInsetRight();
                initialPadding.start += z2 ? systemWindowInsetRight : systemWindowInsetLeft;
                initialPadding.end += z2 ? systemWindowInsetLeft : systemWindowInsetRight;
                initialPadding.applyToView(view);
                return insets;
            }
        });
    }

    private int makeMinHeightSpec(int measureSpec) {
        int suggestedMinimumHeight = getSuggestedMinimumHeight();
        if (View.MeasureSpec.getMode(measureSpec) == 1073741824 || suggestedMinimumHeight <= 0) {
            return measureSpec;
        }
        return View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(measureSpec), suggestedMinimumHeight + getPaddingTop() + getPaddingBottom()), BasicMeasure.EXACTLY);
    }

    private boolean shouldDrawCompatibilityTopDivider() {
        return Build.VERSION.SDK_INT < 21 && !(getBackground() instanceof MaterialShapeDrawable);
    }

    /* access modifiers changed from: protected */
    public NavigationBarMenuView createNavigationBarMenuView(Context context) {
        return new BottomNavigationMenuView(context);
    }

    public int getMaxItemCount() {
        return 5;
    }

    public boolean isItemHorizontalTranslationEnabled() {
        return ((BottomNavigationMenuView) getMenuView()).isItemHorizontalTranslationEnabled();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, makeMinHeightSpec(heightMeasureSpec));
    }

    public void setItemHorizontalTranslationEnabled(boolean itemHorizontalTranslationEnabled) {
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) getMenuView();
        if (bottomNavigationMenuView.isItemHorizontalTranslationEnabled() != itemHorizontalTranslationEnabled) {
            bottomNavigationMenuView.setItemHorizontalTranslationEnabled(itemHorizontalTranslationEnabled);
            getPresenter().updateMenuView(false);
        }
    }

    @Deprecated
    public void setOnNavigationItemReselectedListener(OnNavigationItemReselectedListener listener) {
        setOnItemReselectedListener(listener);
    }

    @Deprecated
    public void setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener listener) {
        setOnItemSelectedListener(listener);
    }
}

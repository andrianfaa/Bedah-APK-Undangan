package com.google.android.material.tabs;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.tabs.TabLayout;

class TabIndicatorInterpolator {
    private static final int MIN_INDICATOR_WIDTH = 24;

    TabIndicatorInterpolator() {
    }

    static RectF calculateIndicatorWidthForTab(TabLayout tabLayout, View tab) {
        return tab == null ? new RectF() : (tabLayout.isTabIndicatorFullWidth() || !(tab instanceof TabLayout.TabView)) ? new RectF((float) tab.getLeft(), (float) tab.getTop(), (float) tab.getRight(), (float) tab.getBottom()) : calculateTabViewContentBounds((TabLayout.TabView) tab, 24);
    }

    static RectF calculateTabViewContentBounds(TabLayout.TabView tabView, int minWidth) {
        int contentWidth = tabView.getContentWidth();
        int contentHeight = tabView.getContentHeight();
        int dpToPx = (int) ViewUtils.dpToPx(tabView.getContext(), minWidth);
        if (contentWidth < dpToPx) {
            contentWidth = dpToPx;
        }
        int left = (tabView.getLeft() + tabView.getRight()) / 2;
        int top = (tabView.getTop() + tabView.getBottom()) / 2;
        return new RectF((float) (left - (contentWidth / 2)), (float) (top - (contentHeight / 2)), (float) ((contentWidth / 2) + left), (float) ((left / 2) + top));
    }

    /* access modifiers changed from: package-private */
    public void setIndicatorBoundsForTab(TabLayout tabLayout, View tab, Drawable indicator) {
        RectF calculateIndicatorWidthForTab = calculateIndicatorWidthForTab(tabLayout, tab);
        indicator.setBounds((int) calculateIndicatorWidthForTab.left, indicator.getBounds().top, (int) calculateIndicatorWidthForTab.right, indicator.getBounds().bottom);
    }

    /* access modifiers changed from: package-private */
    public void updateIndicatorForOffset(TabLayout tabLayout, View startTitle, View endTitle, float offset, Drawable indicator) {
        RectF calculateIndicatorWidthForTab = calculateIndicatorWidthForTab(tabLayout, startTitle);
        RectF calculateIndicatorWidthForTab2 = calculateIndicatorWidthForTab(tabLayout, endTitle);
        indicator.setBounds(AnimationUtils.lerp((int) calculateIndicatorWidthForTab.left, (int) calculateIndicatorWidthForTab2.left, offset), indicator.getBounds().top, AnimationUtils.lerp((int) calculateIndicatorWidthForTab.right, (int) calculateIndicatorWidthForTab2.right, offset), indicator.getBounds().bottom);
    }
}

package com.google.android.material.navigationrail;

import android.content.Context;
import android.view.View;
import com.google.android.material.R;
import com.google.android.material.navigation.NavigationBarItemView;

final class NavigationRailItemView extends NavigationBarItemView {
    public NavigationRailItemView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public int getItemDefaultMarginResId() {
        return R.dimen.mtrl_navigation_rail_icon_margin;
    }

    /* access modifiers changed from: protected */
    public int getItemLayoutResId() {
        return R.layout.mtrl_navigation_rail_item;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (View.MeasureSpec.getMode(heightMeasureSpec) == 0) {
            setMeasuredDimension(getMeasuredWidthAndState(), View.resolveSizeAndState(Math.max(getMeasuredHeight(), View.MeasureSpec.getSize(heightMeasureSpec)), heightMeasureSpec, 0));
        }
    }
}

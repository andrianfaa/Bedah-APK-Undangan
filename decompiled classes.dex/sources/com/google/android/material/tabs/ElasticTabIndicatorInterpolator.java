package com.google.android.material.tabs;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.google.android.material.animation.AnimationUtils;

class ElasticTabIndicatorInterpolator extends TabIndicatorInterpolator {
    ElasticTabIndicatorInterpolator() {
    }

    private static float accInterp(float fraction) {
        return (float) (1.0d - Math.cos((((double) fraction) * 3.141592653589793d) / 2.0d));
    }

    private static float decInterp(float fraction) {
        return (float) Math.sin((((double) fraction) * 3.141592653589793d) / 2.0d);
    }

    /* access modifiers changed from: package-private */
    public void updateIndicatorForOffset(TabLayout tabLayout, View startTitle, View endTitle, float offset, Drawable indicator) {
        float f;
        float f2;
        RectF calculateIndicatorWidthForTab = calculateIndicatorWidthForTab(tabLayout, startTitle);
        RectF calculateIndicatorWidthForTab2 = calculateIndicatorWidthForTab(tabLayout, endTitle);
        if (calculateIndicatorWidthForTab.left < calculateIndicatorWidthForTab2.left) {
            f2 = accInterp(offset);
            f = decInterp(offset);
        } else {
            f2 = decInterp(offset);
            f = accInterp(offset);
        }
        indicator.setBounds(AnimationUtils.lerp((int) calculateIndicatorWidthForTab.left, (int) calculateIndicatorWidthForTab2.left, f2), indicator.getBounds().top, AnimationUtils.lerp((int) calculateIndicatorWidthForTab.right, (int) calculateIndicatorWidthForTab2.right, f), indicator.getBounds().bottom);
    }
}

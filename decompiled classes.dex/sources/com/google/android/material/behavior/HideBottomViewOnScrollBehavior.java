package com.google.android.material.behavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.animation.AnimationUtils;

public class HideBottomViewOnScrollBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    protected static final int ENTER_ANIMATION_DURATION = 225;
    protected static final int EXIT_ANIMATION_DURATION = 175;
    private static final int STATE_SCROLLED_DOWN = 1;
    private static final int STATE_SCROLLED_UP = 2;
    private int additionalHiddenOffsetY = 0;
    /* access modifiers changed from: private */
    public ViewPropertyAnimator currentAnimator;
    private int currentState = 2;
    private int height = 0;

    public HideBottomViewOnScrollBehavior() {
    }

    public HideBottomViewOnScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void animateChildTo(V v, int targetY, long duration, TimeInterpolator interpolator) {
        this.currentAnimator = v.animate().translationY((float) targetY).setInterpolator(interpolator).setDuration(duration).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                ViewPropertyAnimator unused = HideBottomViewOnScrollBehavior.this.currentAnimator = null;
            }
        });
    }

    public boolean isScrolledDown() {
        return this.currentState == 1;
    }

    public boolean isScrolledUp() {
        return this.currentState == 2;
    }

    public boolean onLayoutChild(CoordinatorLayout parent, V v, int layoutDirection) {
        this.height = v.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).bottomMargin;
        return super.onLayoutChild(parent, v, layoutDirection);
    }

    public void onNestedScroll(CoordinatorLayout coordinatorLayout, V v, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int[] consumed) {
        if (dyConsumed > 0) {
            slideDown(v);
        } else if (dyConsumed < 0) {
            slideUp(v);
        }
    }

    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v, View directTargetChild, View target, int nestedScrollAxes, int type) {
        return nestedScrollAxes == 2;
    }

    public void setAdditionalHiddenOffsetY(V v, int offset) {
        this.additionalHiddenOffsetY = offset;
        if (this.currentState == 1) {
            v.setTranslationY((float) (this.height + offset));
        }
    }

    public void slideDown(V v) {
        slideDown(v, true);
    }

    public void slideDown(V v, boolean animate) {
        if (!isScrolledDown()) {
            ViewPropertyAnimator viewPropertyAnimator = this.currentAnimator;
            if (viewPropertyAnimator != null) {
                viewPropertyAnimator.cancel();
                v.clearAnimation();
            }
            this.currentState = 1;
            int i = this.height + this.additionalHiddenOffsetY;
            if (animate) {
                animateChildTo(v, i, 175, AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR);
                return;
            }
            v.setTranslationY((float) i);
        }
    }

    public void slideUp(V v) {
        slideUp(v, true);
    }

    public void slideUp(V v, boolean animate) {
        if (!isScrolledUp()) {
            ViewPropertyAnimator viewPropertyAnimator = this.currentAnimator;
            if (viewPropertyAnimator != null) {
                viewPropertyAnimator.cancel();
                v.clearAnimation();
            }
            this.currentState = 2;
            if (animate) {
                animateChildTo(v, 0, 225, AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
                return;
            }
            v.setTranslationY((float) 0);
        }
    }
}

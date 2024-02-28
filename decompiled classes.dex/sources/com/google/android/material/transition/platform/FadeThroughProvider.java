package com.google.android.material.transition.platform;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

public final class FadeThroughProvider implements VisibilityAnimatorProvider {
    static final float FADE_THROUGH_THRESHOLD = 0.35f;
    private float progressThreshold = FADE_THROUGH_THRESHOLD;

    private static Animator createFadeThroughAnimator(final View view, float startValue, float endValue, float startFraction, float endFraction, final float originalAlpha) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        final View view2 = view;
        final float f = startValue;
        final float f2 = endValue;
        final float f3 = startFraction;
        final float f4 = endFraction;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                view2.setAlpha(TransitionUtils.lerp(f, f2, f3, f4, ((Float) animation.getAnimatedValue()).floatValue()));
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                view.setAlpha(originalAlpha);
            }
        });
        return ofFloat;
    }

    public Animator createAppear(ViewGroup sceneRoot, View view) {
        float alpha = view.getAlpha() == 0.0f ? 1.0f : view.getAlpha();
        return createFadeThroughAnimator(view, 0.0f, alpha, this.progressThreshold, 1.0f, alpha);
    }

    public Animator createDisappear(ViewGroup sceneRoot, View view) {
        float alpha = view.getAlpha() == 0.0f ? 1.0f : view.getAlpha();
        return createFadeThroughAnimator(view, alpha, 0.0f, 0.0f, this.progressThreshold, alpha);
    }

    public float getProgressThreshold() {
        return this.progressThreshold;
    }

    public void setProgressThreshold(float progressThreshold2) {
        this.progressThreshold = progressThreshold2;
    }
}

package com.google.android.material.circularreveal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import com.google.android.material.circularreveal.CircularRevealWidget;

public final class CircularRevealCompat {
    private CircularRevealCompat() {
    }

    public static Animator createCircularReveal(CircularRevealWidget view, float centerX, float centerY, float endRadius) {
        ObjectAnimator ofObject = ObjectAnimator.ofObject(view, CircularRevealWidget.CircularRevealProperty.CIRCULAR_REVEAL, CircularRevealWidget.CircularRevealEvaluator.CIRCULAR_REVEAL, new CircularRevealWidget.RevealInfo[]{new CircularRevealWidget.RevealInfo(centerX, centerY, endRadius)});
        if (Build.VERSION.SDK_INT < 21) {
            return ofObject;
        }
        CircularRevealWidget.RevealInfo revealInfo = view.getRevealInfo();
        if (revealInfo != null) {
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal((View) view, (int) centerX, (int) centerY, revealInfo.radius, endRadius);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(new Animator[]{ofObject, createCircularReveal});
            return animatorSet;
        }
        throw new IllegalStateException("Caller must set a non-null RevealInfo before calling this.");
    }

    public static Animator createCircularReveal(CircularRevealWidget view, float centerX, float centerY, float startRadius, float endRadius) {
        ObjectAnimator ofObject = ObjectAnimator.ofObject(view, CircularRevealWidget.CircularRevealProperty.CIRCULAR_REVEAL, CircularRevealWidget.CircularRevealEvaluator.CIRCULAR_REVEAL, new CircularRevealWidget.RevealInfo[]{new CircularRevealWidget.RevealInfo(centerX, centerY, startRadius), new CircularRevealWidget.RevealInfo(centerX, centerY, endRadius)});
        if (Build.VERSION.SDK_INT < 21) {
            return ofObject;
        }
        Animator createCircularReveal = ViewAnimationUtils.createCircularReveal((View) view, (int) centerX, (int) centerY, startRadius, endRadius);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{ofObject, createCircularReveal});
        return animatorSet;
    }

    public static Animator.AnimatorListener createCircularRevealListener(final CircularRevealWidget view) {
        return new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                CircularRevealWidget.this.destroyCircularRevealCache();
            }

            public void onAnimationStart(Animator animation) {
                CircularRevealWidget.this.buildCircularRevealCache();
            }
        };
    }
}

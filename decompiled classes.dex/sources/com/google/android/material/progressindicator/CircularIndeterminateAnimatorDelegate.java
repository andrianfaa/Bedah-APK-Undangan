package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.util.Property;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.google.android.material.animation.ArgbEvaluatorCompat;
import com.google.android.material.color.MaterialColors;

final class CircularIndeterminateAnimatorDelegate extends IndeterminateAnimatorDelegate<ObjectAnimator> {
    private static final Property<CircularIndeterminateAnimatorDelegate, Float> ANIMATION_FRACTION = new Property<CircularIndeterminateAnimatorDelegate, Float>(Float.class, "animationFraction") {
        public Float get(CircularIndeterminateAnimatorDelegate delegate) {
            return Float.valueOf(delegate.getAnimationFraction());
        }

        public void set(CircularIndeterminateAnimatorDelegate delegate, Float value) {
            delegate.setAnimationFraction(value.floatValue());
        }
    };
    private static final Property<CircularIndeterminateAnimatorDelegate, Float> COMPLETE_END_FRACTION = new Property<CircularIndeterminateAnimatorDelegate, Float>(Float.class, "completeEndFraction") {
        public Float get(CircularIndeterminateAnimatorDelegate delegate) {
            return Float.valueOf(delegate.getCompleteEndFraction());
        }

        public void set(CircularIndeterminateAnimatorDelegate delegate, Float value) {
            delegate.setCompleteEndFraction(value.floatValue());
        }
    };
    private static final int CONSTANT_ROTATION_DEGREES = 1520;
    private static final int[] DELAY_TO_COLLAPSE_IN_MS = {667, 2017, 3367, 4717};
    private static final int[] DELAY_TO_EXPAND_IN_MS = {0, 1350, 2700, 4050};
    private static final int[] DELAY_TO_FADE_IN_MS = {1000, 2350, 3700, 5050};
    private static final int DURATION_TO_COLLAPSE_IN_MS = 667;
    private static final int DURATION_TO_COMPLETE_END_IN_MS = 333;
    private static final int DURATION_TO_EXPAND_IN_MS = 667;
    private static final int DURATION_TO_FADE_IN_MS = 333;
    private static final int EXTRA_DEGREES_PER_CYCLE = 250;
    private static final int TAIL_DEGREES_OFFSET = -20;
    private static final int TOTAL_CYCLES = 4;
    private static final int TOTAL_DURATION_IN_MS = 5400;
    private float animationFraction;
    private ObjectAnimator animator;
    Animatable2Compat.AnimationCallback animatorCompleteCallback = null;
    /* access modifiers changed from: private */
    public final BaseProgressIndicatorSpec baseSpec;
    private ObjectAnimator completeEndAnimator;
    private float completeEndFraction;
    /* access modifiers changed from: private */
    public int indicatorColorIndexOffset = 0;
    private final FastOutSlowInInterpolator interpolator;

    public CircularIndeterminateAnimatorDelegate(CircularProgressIndicatorSpec spec) {
        super(1);
        this.baseSpec = spec;
        this.interpolator = new FastOutSlowInInterpolator();
    }

    /* access modifiers changed from: private */
    public float getAnimationFraction() {
        return this.animationFraction;
    }

    /* access modifiers changed from: private */
    public float getCompleteEndFraction() {
        return this.completeEndFraction;
    }

    private void maybeInitializeAnimators() {
        if (this.animator == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, ANIMATION_FRACTION, new float[]{0.0f, 1.0f});
            this.animator = ofFloat;
            ofFloat.setDuration(5400);
            this.animator.setInterpolator((TimeInterpolator) null);
            this.animator.setRepeatCount(-1);
            this.animator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationRepeat(Animator animation) {
                    super.onAnimationRepeat(animation);
                    CircularIndeterminateAnimatorDelegate circularIndeterminateAnimatorDelegate = CircularIndeterminateAnimatorDelegate.this;
                    int unused = circularIndeterminateAnimatorDelegate.indicatorColorIndexOffset = (circularIndeterminateAnimatorDelegate.indicatorColorIndexOffset + 4) % CircularIndeterminateAnimatorDelegate.this.baseSpec.indicatorColors.length;
                }
            });
        }
        if (this.completeEndAnimator == null) {
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, COMPLETE_END_FRACTION, new float[]{0.0f, 1.0f});
            this.completeEndAnimator = ofFloat2;
            ofFloat2.setDuration(333);
            this.completeEndAnimator.setInterpolator(this.interpolator);
            this.completeEndAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    CircularIndeterminateAnimatorDelegate.this.cancelAnimatorImmediately();
                    if (CircularIndeterminateAnimatorDelegate.this.animatorCompleteCallback != null) {
                        CircularIndeterminateAnimatorDelegate.this.animatorCompleteCallback.onAnimationEnd(CircularIndeterminateAnimatorDelegate.this.drawable);
                    }
                }
            });
        }
    }

    private void maybeUpdateSegmentColors(int playtime) {
        int i = 0;
        while (i < 4) {
            float fractionInRange = getFractionInRange(playtime, DELAY_TO_FADE_IN_MS[i], 333);
            if (fractionInRange < 0.0f || fractionInRange > 1.0f) {
                i++;
            } else {
                int length = (this.indicatorColorIndexOffset + i) % this.baseSpec.indicatorColors.length;
                int length2 = (length + 1) % this.baseSpec.indicatorColors.length;
                int compositeARGBWithAlpha = MaterialColors.compositeARGBWithAlpha(this.baseSpec.indicatorColors[length], this.drawable.getAlpha());
                int compositeARGBWithAlpha2 = MaterialColors.compositeARGBWithAlpha(this.baseSpec.indicatorColors[length2], this.drawable.getAlpha());
                this.segmentColors[0] = ArgbEvaluatorCompat.getInstance().evaluate(this.interpolator.getInterpolation(fractionInRange), Integer.valueOf(compositeARGBWithAlpha), Integer.valueOf(compositeARGBWithAlpha2)).intValue();
                return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void setCompleteEndFraction(float fraction) {
        this.completeEndFraction = fraction;
    }

    private void updateSegmentPositions(int playtime) {
        this.segmentPositions[0] = (this.animationFraction * 1520.0f) - 0.21875f;
        this.segmentPositions[1] = this.animationFraction * 1520.0f;
        for (int i = 0; i < 4; i++) {
            float fractionInRange = getFractionInRange(playtime, DELAY_TO_EXPAND_IN_MS[i], 667);
            float[] fArr = this.segmentPositions;
            fArr[1] = fArr[1] + (this.interpolator.getInterpolation(fractionInRange) * 250.0f);
            float fractionInRange2 = getFractionInRange(playtime, DELAY_TO_COLLAPSE_IN_MS[i], 667);
            float[] fArr2 = this.segmentPositions;
            fArr2[0] = fArr2[0] + (this.interpolator.getInterpolation(fractionInRange2) * 250.0f);
        }
        float[] fArr3 = this.segmentPositions;
        fArr3[0] = fArr3[0] + ((this.segmentPositions[1] - this.segmentPositions[0]) * this.completeEndFraction);
        float[] fArr4 = this.segmentPositions;
        fArr4[0] = fArr4[0] / 360.0f;
        float[] fArr5 = this.segmentPositions;
        fArr5[1] = fArr5[1] / 360.0f;
    }

    /* access modifiers changed from: package-private */
    public void cancelAnimatorImmediately() {
        ObjectAnimator objectAnimator = this.animator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    public void invalidateSpecValues() {
        resetPropertiesForNewStart();
    }

    public void registerAnimatorsCompleteCallback(Animatable2Compat.AnimationCallback callback) {
        this.animatorCompleteCallback = callback;
    }

    /* access modifiers changed from: package-private */
    public void requestCancelAnimatorAfterCurrentCycle() {
        ObjectAnimator objectAnimator = this.completeEndAnimator;
        if (objectAnimator != null && !objectAnimator.isRunning()) {
            if (this.drawable.isVisible()) {
                this.completeEndAnimator.start();
            } else {
                cancelAnimatorImmediately();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void resetPropertiesForNewStart() {
        this.indicatorColorIndexOffset = 0;
        this.segmentColors[0] = MaterialColors.compositeARGBWithAlpha(this.baseSpec.indicatorColors[0], this.drawable.getAlpha());
        this.completeEndFraction = 0.0f;
    }

    /* access modifiers changed from: package-private */
    public void setAnimationFraction(float fraction) {
        this.animationFraction = fraction;
        int i = (int) (5400.0f * fraction);
        updateSegmentPositions(i);
        maybeUpdateSegmentColors(i);
        this.drawable.invalidateSelf();
    }

    /* access modifiers changed from: package-private */
    public void startAnimator() {
        maybeInitializeAnimators();
        resetPropertiesForNewStart();
        this.animator.start();
    }

    public void unregisterAnimatorsCompleteCallback() {
        this.animatorCompleteCallback = null;
    }
}

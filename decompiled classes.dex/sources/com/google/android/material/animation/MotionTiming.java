package com.google.android.material.animation;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import mt.Log1F380D;

/* compiled from: 00E6 */
public class MotionTiming {
    private long delay = 0;
    private long duration = 300;
    private TimeInterpolator interpolator = null;
    private int repeatCount = 0;
    private int repeatMode = 1;

    public MotionTiming(long delay2, long duration2) {
        this.delay = delay2;
        this.duration = duration2;
    }

    public MotionTiming(long delay2, long duration2, TimeInterpolator interpolator2) {
        this.delay = delay2;
        this.duration = duration2;
        this.interpolator = interpolator2;
    }

    static MotionTiming createFromAnimator(ValueAnimator animator) {
        MotionTiming motionTiming = new MotionTiming(animator.getStartDelay(), animator.getDuration(), getInterpolatorCompat(animator));
        motionTiming.repeatCount = animator.getRepeatCount();
        motionTiming.repeatMode = animator.getRepeatMode();
        return motionTiming;
    }

    private static TimeInterpolator getInterpolatorCompat(ValueAnimator animator) {
        TimeInterpolator interpolator2 = animator.getInterpolator();
        return ((interpolator2 instanceof AccelerateDecelerateInterpolator) || interpolator2 == null) ? AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR : interpolator2 instanceof AccelerateInterpolator ? AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR : interpolator2 instanceof DecelerateInterpolator ? AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR : interpolator2;
    }

    public void apply(Animator animator) {
        animator.setStartDelay(getDelay());
        animator.setDuration(getDuration());
        animator.setInterpolator(getInterpolator());
        if (animator instanceof ValueAnimator) {
            ((ValueAnimator) animator).setRepeatCount(getRepeatCount());
            ((ValueAnimator) animator).setRepeatMode(getRepeatMode());
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MotionTiming)) {
            return false;
        }
        MotionTiming motionTiming = (MotionTiming) o;
        if (getDelay() == motionTiming.getDelay() && getDuration() == motionTiming.getDuration() && getRepeatCount() == motionTiming.getRepeatCount() && getRepeatMode() == motionTiming.getRepeatMode()) {
            return getInterpolator().getClass().equals(motionTiming.getInterpolator().getClass());
        }
        return false;
    }

    public long getDelay() {
        return this.delay;
    }

    public long getDuration() {
        return this.duration;
    }

    public TimeInterpolator getInterpolator() {
        TimeInterpolator timeInterpolator = this.interpolator;
        return timeInterpolator != null ? timeInterpolator : AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR;
    }

    public int getRepeatCount() {
        return this.repeatCount;
    }

    public int getRepeatMode() {
        return this.repeatMode;
    }

    public int hashCode() {
        return (((((((((int) (getDelay() ^ (getDelay() >>> 32))) * 31) + ((int) (getDuration() ^ (getDuration() >>> 32)))) * 31) + getInterpolator().getClass().hashCode()) * 31) + getRepeatCount()) * 31) + getRepeatMode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(10);
        sb.append(getClass().getName());
        sb.append('{');
        String hexString = Integer.toHexString(System.identityHashCode(this));
        Log1F380D.a((Object) hexString);
        sb.append(hexString);
        sb.append(" delay: ");
        sb.append(getDelay());
        sb.append(" duration: ");
        sb.append(getDuration());
        sb.append(" interpolator: ");
        sb.append(getInterpolator().getClass());
        sb.append(" repeatCount: ");
        sb.append(getRepeatCount());
        sb.append(" repeatMode: ");
        sb.append(getRepeatMode());
        sb.append("}\n");
        return sb.toString();
    }
}

package com.google.android.material.internal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.StateSet;
import java.util.ArrayList;

public final class StateListAnimator {
    private final Animator.AnimatorListener animationListener = new AnimatorListenerAdapter() {
        public void onAnimationEnd(Animator animator) {
            if (StateListAnimator.this.runningAnimator == animator) {
                StateListAnimator.this.runningAnimator = null;
            }
        }
    };
    private Tuple lastMatch = null;
    ValueAnimator runningAnimator = null;
    private final ArrayList<Tuple> tuples = new ArrayList<>();

    static class Tuple {
        final ValueAnimator animator;
        final int[] specs;

        Tuple(int[] specs2, ValueAnimator animator2) {
            this.specs = specs2;
            this.animator = animator2;
        }
    }

    private void cancel() {
        ValueAnimator valueAnimator = this.runningAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.runningAnimator = null;
        }
    }

    private void start(Tuple match) {
        ValueAnimator valueAnimator = match.animator;
        this.runningAnimator = valueAnimator;
        valueAnimator.start();
    }

    public void addState(int[] specs, ValueAnimator animator) {
        Tuple tuple = new Tuple(specs, animator);
        animator.addListener(this.animationListener);
        this.tuples.add(tuple);
    }

    public void jumpToCurrentState() {
        ValueAnimator valueAnimator = this.runningAnimator;
        if (valueAnimator != null) {
            valueAnimator.end();
            this.runningAnimator = null;
        }
    }

    public void setState(int[] state) {
        Tuple tuple = null;
        int size = this.tuples.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                break;
            }
            Tuple tuple2 = this.tuples.get(i);
            if (StateSet.stateSetMatches(tuple2.specs, state)) {
                tuple = tuple2;
                break;
            }
            i++;
        }
        Tuple tuple3 = this.lastMatch;
        if (tuple != tuple3) {
            if (tuple3 != null) {
                cancel();
            }
            this.lastMatch = tuple;
            if (tuple != null) {
                start(tuple);
            }
        }
    }
}

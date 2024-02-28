package com.google.android.material.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.ViewGroup;

public final class ScaleProvider implements VisibilityAnimatorProvider {
    private boolean growing;
    private float incomingEndScale;
    private float incomingStartScale;
    private float outgoingEndScale;
    private float outgoingStartScale;
    private boolean scaleOnDisappear;

    public ScaleProvider() {
        this(true);
    }

    public ScaleProvider(boolean growing2) {
        this.outgoingStartScale = 1.0f;
        this.outgoingEndScale = 1.1f;
        this.incomingStartScale = 0.8f;
        this.incomingEndScale = 1.0f;
        this.scaleOnDisappear = true;
        this.growing = growing2;
    }

    private static Animator createScaleAnimator(final View view, float startScale, float endScale) {
        final float scaleX = view.getScaleX();
        final float scaleY = view.getScaleY();
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(View.SCALE_X, new float[]{scaleX * startScale, scaleX * endScale}), PropertyValuesHolder.ofFloat(View.SCALE_Y, new float[]{scaleY * startScale, scaleY * endScale})});
        ofPropertyValuesHolder.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                view.setScaleX(scaleX);
                view.setScaleY(scaleY);
            }
        });
        return ofPropertyValuesHolder;
    }

    public Animator createAppear(ViewGroup sceneRoot, View view) {
        return this.growing ? createScaleAnimator(view, this.incomingStartScale, this.incomingEndScale) : createScaleAnimator(view, this.outgoingEndScale, this.outgoingStartScale);
    }

    public Animator createDisappear(ViewGroup sceneRoot, View view) {
        if (!this.scaleOnDisappear) {
            return null;
        }
        return this.growing ? createScaleAnimator(view, this.outgoingStartScale, this.outgoingEndScale) : createScaleAnimator(view, this.incomingEndScale, this.incomingStartScale);
    }

    public float getIncomingEndScale() {
        return this.incomingEndScale;
    }

    public float getIncomingStartScale() {
        return this.incomingStartScale;
    }

    public float getOutgoingEndScale() {
        return this.outgoingEndScale;
    }

    public float getOutgoingStartScale() {
        return this.outgoingStartScale;
    }

    public boolean isGrowing() {
        return this.growing;
    }

    public boolean isScaleOnDisappear() {
        return this.scaleOnDisappear;
    }

    public void setGrowing(boolean growing2) {
        this.growing = growing2;
    }

    public void setIncomingEndScale(float incomingEndScale2) {
        this.incomingEndScale = incomingEndScale2;
    }

    public void setIncomingStartScale(float incomingStartScale2) {
        this.incomingStartScale = incomingStartScale2;
    }

    public void setOutgoingEndScale(float outgoingEndScale2) {
        this.outgoingEndScale = outgoingEndScale2;
    }

    public void setOutgoingStartScale(float outgoingStartScale2) {
        this.outgoingStartScale = outgoingStartScale2;
    }

    public void setScaleOnDisappear(boolean scaleOnDisappear2) {
        this.scaleOnDisappear = scaleOnDisappear2;
    }
}

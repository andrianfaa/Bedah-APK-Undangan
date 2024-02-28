package com.google.android.material.progressindicator;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.os.Build;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import com.google.android.material.progressindicator.BaseProgressIndicatorSpec;

public final class IndeterminateDrawable<S extends BaseProgressIndicatorSpec> extends DrawableWithAnimatedVisibilityChange {
    private IndeterminateAnimatorDelegate<ObjectAnimator> animatorDelegate;
    private DrawingDelegate<S> drawingDelegate;

    IndeterminateDrawable(Context context, BaseProgressIndicatorSpec baseSpec, DrawingDelegate<S> drawingDelegate2, IndeterminateAnimatorDelegate<ObjectAnimator> indeterminateAnimatorDelegate) {
        super(context, baseSpec);
        setDrawingDelegate(drawingDelegate2);
        setAnimatorDelegate(indeterminateAnimatorDelegate);
    }

    public static IndeterminateDrawable<CircularProgressIndicatorSpec> createCircularDrawable(Context context, CircularProgressIndicatorSpec spec) {
        return new IndeterminateDrawable<>(context, spec, new CircularDrawingDelegate(spec), new CircularIndeterminateAnimatorDelegate(spec));
    }

    public static IndeterminateDrawable<LinearProgressIndicatorSpec> createLinearDrawable(Context context, LinearProgressIndicatorSpec spec) {
        return new IndeterminateDrawable<>(context, spec, new LinearDrawingDelegate(spec), spec.indeterminateAnimationType == 0 ? new LinearIndeterminateContiguousAnimatorDelegate(spec) : new LinearIndeterminateDisjointAnimatorDelegate(context, spec));
    }

    public /* bridge */ /* synthetic */ void clearAnimationCallbacks() {
        super.clearAnimationCallbacks();
    }

    public void draw(Canvas canvas) {
        Rect rect = new Rect();
        if (!getBounds().isEmpty() && isVisible() && canvas.getClipBounds(rect)) {
            canvas.save();
            this.drawingDelegate.validateSpecAndAdjustCanvas(canvas, getGrowFraction());
            this.drawingDelegate.fillTrack(canvas, this.paint);
            for (int i = 0; i < this.animatorDelegate.segmentColors.length; i++) {
                this.drawingDelegate.fillIndicator(canvas, this.paint, this.animatorDelegate.segmentPositions[i * 2], this.animatorDelegate.segmentPositions[(i * 2) + 1], this.animatorDelegate.segmentColors[i]);
            }
            canvas.restore();
        }
    }

    public /* bridge */ /* synthetic */ int getAlpha() {
        return super.getAlpha();
    }

    /* access modifiers changed from: package-private */
    public IndeterminateAnimatorDelegate<ObjectAnimator> getAnimatorDelegate() {
        return this.animatorDelegate;
    }

    /* access modifiers changed from: package-private */
    public DrawingDelegate<S> getDrawingDelegate() {
        return this.drawingDelegate;
    }

    public int getIntrinsicHeight() {
        return this.drawingDelegate.getPreferredHeight();
    }

    public int getIntrinsicWidth() {
        return this.drawingDelegate.getPreferredWidth();
    }

    public /* bridge */ /* synthetic */ int getOpacity() {
        return super.getOpacity();
    }

    public /* bridge */ /* synthetic */ boolean hideNow() {
        return super.hideNow();
    }

    public /* bridge */ /* synthetic */ boolean isHiding() {
        return super.isHiding();
    }

    public /* bridge */ /* synthetic */ boolean isRunning() {
        return super.isRunning();
    }

    public /* bridge */ /* synthetic */ boolean isShowing() {
        return super.isShowing();
    }

    public /* bridge */ /* synthetic */ void registerAnimationCallback(Animatable2Compat.AnimationCallback animationCallback) {
        super.registerAnimationCallback(animationCallback);
    }

    public /* bridge */ /* synthetic */ void setAlpha(int i) {
        super.setAlpha(i);
    }

    /* access modifiers changed from: package-private */
    public void setAnimatorDelegate(IndeterminateAnimatorDelegate<ObjectAnimator> indeterminateAnimatorDelegate) {
        this.animatorDelegate = indeterminateAnimatorDelegate;
        indeterminateAnimatorDelegate.registerDrawable(this);
    }

    public /* bridge */ /* synthetic */ void setColorFilter(ColorFilter colorFilter) {
        super.setColorFilter(colorFilter);
    }

    /* access modifiers changed from: package-private */
    public void setDrawingDelegate(DrawingDelegate<S> drawingDelegate2) {
        this.drawingDelegate = drawingDelegate2;
        drawingDelegate2.registerDrawable(this);
    }

    public /* bridge */ /* synthetic */ boolean setVisible(boolean z, boolean z2) {
        return super.setVisible(z, z2);
    }

    public /* bridge */ /* synthetic */ boolean setVisible(boolean z, boolean z2, boolean z3) {
        return super.setVisible(z, z2, z3);
    }

    /* access modifiers changed from: package-private */
    public boolean setVisibleInternal(boolean visible, boolean restart, boolean animate) {
        boolean visibleInternal = super.setVisibleInternal(visible, restart, animate);
        if (!isRunning()) {
            this.animatorDelegate.cancelAnimatorImmediately();
        }
        float systemAnimatorDurationScale = this.animatorDurationScaleProvider.getSystemAnimatorDurationScale(this.context.getContentResolver());
        if (visible && (animate || (Build.VERSION.SDK_INT <= 21 && systemAnimatorDurationScale > 0.0f))) {
            this.animatorDelegate.startAnimator();
        }
        return visibleInternal;
    }

    public /* bridge */ /* synthetic */ void start() {
        super.start();
    }

    public /* bridge */ /* synthetic */ void stop() {
        super.stop();
    }

    public /* bridge */ /* synthetic */ boolean unregisterAnimationCallback(Animatable2Compat.AnimationCallback animationCallback) {
        return super.unregisterAnimationCallback(animationCallback);
    }
}

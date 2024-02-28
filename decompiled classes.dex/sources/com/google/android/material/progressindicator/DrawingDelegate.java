package com.google.android.material.progressindicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.google.android.material.progressindicator.BaseProgressIndicatorSpec;

abstract class DrawingDelegate<S extends BaseProgressIndicatorSpec> {
    protected DrawableWithAnimatedVisibilityChange drawable;
    S spec;

    public DrawingDelegate(S s) {
        this.spec = s;
    }

    /* access modifiers changed from: package-private */
    public abstract void adjustCanvas(Canvas canvas, float f);

    /* access modifiers changed from: package-private */
    public abstract void fillIndicator(Canvas canvas, Paint paint, float f, float f2, int i);

    /* access modifiers changed from: package-private */
    public abstract void fillTrack(Canvas canvas, Paint paint);

    /* access modifiers changed from: package-private */
    public abstract int getPreferredHeight();

    /* access modifiers changed from: package-private */
    public abstract int getPreferredWidth();

    /* access modifiers changed from: protected */
    public void registerDrawable(DrawableWithAnimatedVisibilityChange drawable2) {
        this.drawable = drawable2;
    }

    /* access modifiers changed from: package-private */
    public void validateSpecAndAdjustCanvas(Canvas canvas, float trackThicknessFraction) {
        this.spec.validateSpec();
        adjustCanvas(canvas, trackThicknessFraction);
    }
}

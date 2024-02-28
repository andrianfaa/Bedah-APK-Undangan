package com.google.android.material.progressindicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.google.android.material.color.MaterialColors;

final class CircularDrawingDelegate extends DrawingDelegate<CircularProgressIndicatorSpec> {
    private float adjustedRadius;
    private int arcDirectionFactor = 1;
    private float displayedCornerRadius;
    private float displayedTrackThickness;

    public CircularDrawingDelegate(CircularProgressIndicatorSpec spec) {
        super(spec);
    }

    private void drawRoundedEnd(Canvas canvas, Paint paint, float trackSize, float cornerRadius, float positionInDeg) {
        canvas.save();
        canvas.rotate(positionInDeg);
        float f = this.adjustedRadius;
        canvas.drawRoundRect(new RectF(f - (trackSize / 2.0f), cornerRadius, f + (trackSize / 2.0f), -cornerRadius), cornerRadius, cornerRadius, paint);
        canvas.restore();
    }

    private int getSize() {
        return ((CircularProgressIndicatorSpec) this.spec).indicatorSize + (((CircularProgressIndicatorSpec) this.spec).indicatorInset * 2);
    }

    public void adjustCanvas(Canvas canvas, float trackThicknessFraction) {
        float f = (((float) ((CircularProgressIndicatorSpec) this.spec).indicatorSize) / 2.0f) + ((float) ((CircularProgressIndicatorSpec) this.spec).indicatorInset);
        canvas.translate(f, f);
        canvas.rotate(-90.0f);
        canvas.clipRect(-f, -f, f, f);
        this.arcDirectionFactor = ((CircularProgressIndicatorSpec) this.spec).indicatorDirection == 0 ? 1 : -1;
        this.displayedTrackThickness = ((float) ((CircularProgressIndicatorSpec) this.spec).trackThickness) * trackThicknessFraction;
        this.displayedCornerRadius = ((float) ((CircularProgressIndicatorSpec) this.spec).trackCornerRadius) * trackThicknessFraction;
        this.adjustedRadius = ((float) (((CircularProgressIndicatorSpec) this.spec).indicatorSize - ((CircularProgressIndicatorSpec) this.spec).trackThickness)) / 2.0f;
        if ((this.drawable.isShowing() && ((CircularProgressIndicatorSpec) this.spec).showAnimationBehavior == 2) || (this.drawable.isHiding() && ((CircularProgressIndicatorSpec) this.spec).hideAnimationBehavior == 1)) {
            this.adjustedRadius += ((1.0f - trackThicknessFraction) * ((float) ((CircularProgressIndicatorSpec) this.spec).trackThickness)) / 2.0f;
        } else if ((this.drawable.isShowing() && ((CircularProgressIndicatorSpec) this.spec).showAnimationBehavior == 1) || (this.drawable.isHiding() && ((CircularProgressIndicatorSpec) this.spec).hideAnimationBehavior == 2)) {
            this.adjustedRadius -= ((1.0f - trackThicknessFraction) * ((float) ((CircularProgressIndicatorSpec) this.spec).trackThickness)) / 2.0f;
        }
    }

    /* access modifiers changed from: package-private */
    public void fillIndicator(Canvas canvas, Paint paint, float startFraction, float endFraction, int color) {
        Paint paint2 = paint;
        if (startFraction != endFraction) {
            paint2.setStyle(Paint.Style.STROKE);
            paint2.setStrokeCap(Paint.Cap.BUTT);
            paint2.setAntiAlias(true);
            paint2.setColor(color);
            paint2.setStrokeWidth(this.displayedTrackThickness);
            int i = this.arcDirectionFactor;
            float f = startFraction * 360.0f * ((float) i);
            float f2 = endFraction >= startFraction ? (endFraction - startFraction) * 360.0f * ((float) i) : ((endFraction + 1.0f) - startFraction) * 360.0f * ((float) i);
            float f3 = this.adjustedRadius;
            canvas.drawArc(new RectF(-f3, -f3, f3, f3), f, f2, false, paint);
            if (this.displayedCornerRadius > 0.0f && Math.abs(f2) < 360.0f) {
                paint2.setStyle(Paint.Style.FILL);
                Canvas canvas2 = canvas;
                Paint paint3 = paint;
                drawRoundedEnd(canvas2, paint3, this.displayedTrackThickness, this.displayedCornerRadius, f);
                drawRoundedEnd(canvas2, paint3, this.displayedTrackThickness, this.displayedCornerRadius, f + f2);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void fillTrack(Canvas canvas, Paint paint) {
        int compositeARGBWithAlpha = MaterialColors.compositeARGBWithAlpha(((CircularProgressIndicatorSpec) this.spec).trackColor, this.drawable.getAlpha());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setAntiAlias(true);
        paint.setColor(compositeARGBWithAlpha);
        paint.setStrokeWidth(this.displayedTrackThickness);
        float f = this.adjustedRadius;
        canvas.drawArc(new RectF(-f, -f, f, f), 0.0f, 360.0f, false, paint);
    }

    public int getPreferredHeight() {
        return getSize();
    }

    public int getPreferredWidth() {
        return getSize();
    }
}

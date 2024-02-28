package com.google.android.material.progressindicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import com.google.android.material.color.MaterialColors;

final class LinearDrawingDelegate extends DrawingDelegate<LinearProgressIndicatorSpec> {
    private float displayedCornerRadius;
    private float displayedTrackThickness;
    private float trackLength = 300.0f;

    public LinearDrawingDelegate(LinearProgressIndicatorSpec spec) {
        super(spec);
    }

    public void adjustCanvas(Canvas canvas, float trackThicknessFraction) {
        Rect clipBounds = canvas.getClipBounds();
        this.trackLength = (float) clipBounds.width();
        float f = (float) ((LinearProgressIndicatorSpec) this.spec).trackThickness;
        canvas.translate(((float) clipBounds.left) + (((float) clipBounds.width()) / 2.0f), ((float) clipBounds.top) + (((float) clipBounds.height()) / 2.0f) + Math.max(0.0f, ((float) (clipBounds.height() - ((LinearProgressIndicatorSpec) this.spec).trackThickness)) / 2.0f));
        if (((LinearProgressIndicatorSpec) this.spec).drawHorizontallyInverse) {
            canvas.scale(-1.0f, 1.0f);
        }
        if ((this.drawable.isShowing() && ((LinearProgressIndicatorSpec) this.spec).showAnimationBehavior == 1) || (this.drawable.isHiding() && ((LinearProgressIndicatorSpec) this.spec).hideAnimationBehavior == 2)) {
            canvas.scale(1.0f, -1.0f);
        }
        if (this.drawable.isShowing() || this.drawable.isHiding()) {
            canvas.translate(0.0f, (((float) ((LinearProgressIndicatorSpec) this.spec).trackThickness) * (trackThicknessFraction - 1.0f)) / 2.0f);
        }
        float f2 = this.trackLength;
        canvas.clipRect((-f2) / 2.0f, (-f) / 2.0f, f2 / 2.0f, f / 2.0f);
        this.displayedTrackThickness = ((float) ((LinearProgressIndicatorSpec) this.spec).trackThickness) * trackThicknessFraction;
        this.displayedCornerRadius = ((float) ((LinearProgressIndicatorSpec) this.spec).trackCornerRadius) * trackThicknessFraction;
    }

    public void fillIndicator(Canvas canvas, Paint paint, float startFraction, float endFraction, int color) {
        if (startFraction != endFraction) {
            float f = this.trackLength;
            float f2 = this.displayedCornerRadius;
            float f3 = ((-f) / 2.0f) + ((f - (f2 * 2.0f)) * startFraction);
            float f4 = ((-f) / 2.0f) + ((f - (f2 * 2.0f)) * endFraction) + (f2 * 2.0f);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setColor(color);
            float f5 = this.displayedTrackThickness;
            RectF rectF = new RectF(f3, (-f5) / 2.0f, f4, f5 / 2.0f);
            float f6 = this.displayedCornerRadius;
            canvas.drawRoundRect(rectF, f6, f6, paint);
        }
    }

    /* access modifiers changed from: package-private */
    public void fillTrack(Canvas canvas, Paint paint) {
        int compositeARGBWithAlpha = MaterialColors.compositeARGBWithAlpha(((LinearProgressIndicatorSpec) this.spec).trackColor, this.drawable.getAlpha());
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(compositeARGBWithAlpha);
        float f = this.trackLength;
        float f2 = this.displayedTrackThickness;
        RectF rectF = new RectF((-f) / 2.0f, (-f2) / 2.0f, f / 2.0f, f2 / 2.0f);
        float f3 = this.displayedCornerRadius;
        canvas.drawRoundRect(rectF, f3, f3, paint);
    }

    public int getPreferredHeight() {
        return ((LinearProgressIndicatorSpec) this.spec).trackThickness;
    }

    public int getPreferredWidth() {
        return -1;
    }
}

package com.google.android.material.shadow;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.ViewCompat;

public class ShadowRenderer {
    private static final int COLOR_ALPHA_END = 0;
    private static final int COLOR_ALPHA_MIDDLE = 20;
    private static final int COLOR_ALPHA_START = 68;
    private static final int[] cornerColors = new int[4];
    private static final float[] cornerPositions = {0.0f, 0.0f, 0.5f, 1.0f};
    private static final int[] edgeColors = new int[3];
    private static final float[] edgePositions = {0.0f, 0.5f, 1.0f};
    private final Paint cornerShadowPaint;
    private final Paint edgeShadowPaint;
    private final Path scratch;
    private int shadowEndColor;
    private int shadowMiddleColor;
    private final Paint shadowPaint;
    private int shadowStartColor;
    private Paint transparentPaint;

    public ShadowRenderer() {
        this(ViewCompat.MEASURED_STATE_MASK);
    }

    public ShadowRenderer(int color) {
        this.scratch = new Path();
        this.transparentPaint = new Paint();
        this.shadowPaint = new Paint();
        setShadowColor(color);
        this.transparentPaint.setColor(0);
        Paint paint = new Paint(4);
        this.cornerShadowPaint = paint;
        paint.setStyle(Paint.Style.FILL);
        this.edgeShadowPaint = new Paint(paint);
    }

    public void drawCornerShadow(Canvas canvas, Matrix matrix, RectF bounds, int elevation, float startAngle, float sweepAngle) {
        Canvas canvas2 = canvas;
        RectF rectF = bounds;
        int i = elevation;
        float f = sweepAngle;
        boolean z = f < 0.0f;
        Path path = this.scratch;
        if (z) {
            int[] iArr = cornerColors;
            iArr[0] = 0;
            iArr[1] = this.shadowEndColor;
            iArr[2] = this.shadowMiddleColor;
            iArr[3] = this.shadowStartColor;
            float f2 = startAngle;
        } else {
            path.rewind();
            path.moveTo(bounds.centerX(), bounds.centerY());
            path.arcTo(rectF, startAngle, f);
            path.close();
            rectF.inset((float) (-i), (float) (-i));
            int[] iArr2 = cornerColors;
            iArr2[0] = 0;
            iArr2[1] = this.shadowStartColor;
            iArr2[2] = this.shadowMiddleColor;
            iArr2[3] = this.shadowEndColor;
        }
        float width = bounds.width() / 2.0f;
        if (width > 0.0f) {
            float f3 = 1.0f - (((float) i) / width);
            float[] fArr = cornerPositions;
            fArr[1] = f3;
            fArr[2] = f3 + ((1.0f - f3) / 2.0f);
            this.cornerShadowPaint.setShader(new RadialGradient(bounds.centerX(), bounds.centerY(), width, cornerColors, fArr, Shader.TileMode.CLAMP));
            canvas.save();
            canvas.concat(matrix);
            canvas2.scale(1.0f, bounds.height() / bounds.width());
            if (!z) {
                canvas2.clipPath(path, Region.Op.DIFFERENCE);
                canvas2.drawPath(path, this.transparentPaint);
            }
            canvas.drawArc(bounds, startAngle, sweepAngle, true, this.cornerShadowPaint);
            canvas.restore();
        }
    }

    public void drawEdgeShadow(Canvas canvas, Matrix transform, RectF bounds, int elevation) {
        bounds.bottom += (float) elevation;
        bounds.offset(0.0f, (float) (-elevation));
        int[] iArr = edgeColors;
        iArr[0] = this.shadowEndColor;
        iArr[1] = this.shadowMiddleColor;
        iArr[2] = this.shadowStartColor;
        this.edgeShadowPaint.setShader(new LinearGradient(bounds.left, bounds.top, bounds.left, bounds.bottom, iArr, edgePositions, Shader.TileMode.CLAMP));
        canvas.save();
        canvas.concat(transform);
        canvas.drawRect(bounds, this.edgeShadowPaint);
        canvas.restore();
    }

    public Paint getShadowPaint() {
        return this.shadowPaint;
    }

    public void setShadowColor(int color) {
        this.shadowStartColor = ColorUtils.setAlphaComponent(color, COLOR_ALPHA_START);
        this.shadowMiddleColor = ColorUtils.setAlphaComponent(color, 20);
        this.shadowEndColor = ColorUtils.setAlphaComponent(color, 0);
        this.shadowPaint.setColor(this.shadowStartColor);
    }
}

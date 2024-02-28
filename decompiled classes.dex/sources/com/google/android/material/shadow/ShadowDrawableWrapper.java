package com.google.android.material.shadow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import androidx.core.content.ContextCompat;
import com.google.android.material.R;

@Deprecated
public class ShadowDrawableWrapper extends DrawableWrapper {
    static final double COS_45 = Math.cos(Math.toRadians(45.0d));
    static final float SHADOW_BOTTOM_SCALE = 1.0f;
    static final float SHADOW_HORIZ_SCALE = 0.5f;
    static final float SHADOW_MULTIPLIER = 1.5f;
    static final float SHADOW_TOP_SCALE = 0.25f;
    private boolean addPaddingForCorners = true;
    final RectF contentBounds;
    float cornerRadius;
    final Paint cornerShadowPaint;
    Path cornerShadowPath;
    private boolean dirty = true;
    final Paint edgeShadowPaint;
    float maxShadowSize;
    private boolean printedShadowClipWarning = false;
    float rawMaxShadowSize;
    float rawShadowSize;
    private float rotation;
    private final int shadowEndColor;
    private final int shadowMiddleColor;
    float shadowSize;
    private final int shadowStartColor;

    public ShadowDrawableWrapper(Context context, Drawable content, float radius, float shadowSize2, float maxShadowSize2) {
        super(content);
        this.shadowStartColor = ContextCompat.getColor(context, R.color.design_fab_shadow_start_color);
        this.shadowMiddleColor = ContextCompat.getColor(context, R.color.design_fab_shadow_mid_color);
        this.shadowEndColor = ContextCompat.getColor(context, R.color.design_fab_shadow_end_color);
        Paint paint = new Paint(5);
        this.cornerShadowPaint = paint;
        paint.setStyle(Paint.Style.FILL);
        this.cornerRadius = (float) Math.round(radius);
        this.contentBounds = new RectF();
        Paint paint2 = new Paint(paint);
        this.edgeShadowPaint = paint2;
        paint2.setAntiAlias(false);
        setShadowSize(shadowSize2, maxShadowSize2);
    }

    private void buildComponents(Rect bounds) {
        float f = this.rawMaxShadowSize * SHADOW_MULTIPLIER;
        this.contentBounds.set(((float) bounds.left) + this.rawMaxShadowSize, ((float) bounds.top) + f, ((float) bounds.right) - this.rawMaxShadowSize, ((float) bounds.bottom) - f);
        getWrappedDrawable().setBounds((int) this.contentBounds.left, (int) this.contentBounds.top, (int) this.contentBounds.right, (int) this.contentBounds.bottom);
        buildShadowCorners();
    }

    private void buildShadowCorners() {
        int i;
        float f = this.cornerRadius;
        RectF rectF = new RectF(-f, -f, f, f);
        RectF rectF2 = new RectF(rectF);
        float f2 = this.shadowSize;
        rectF2.inset(-f2, -f2);
        Path path = this.cornerShadowPath;
        if (path == null) {
            this.cornerShadowPath = new Path();
        } else {
            path.reset();
        }
        this.cornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
        this.cornerShadowPath.moveTo(-this.cornerRadius, 0.0f);
        this.cornerShadowPath.rLineTo(-this.shadowSize, 0.0f);
        this.cornerShadowPath.arcTo(rectF2, 180.0f, 90.0f, false);
        this.cornerShadowPath.arcTo(rectF, 270.0f, -90.0f, false);
        this.cornerShadowPath.close();
        float f3 = -rectF2.top;
        if (f3 > 0.0f) {
            float f4 = this.cornerRadius / f3;
            Paint paint = this.cornerShadowPaint;
            RadialGradient radialGradient = r8;
            i = 3;
            RadialGradient radialGradient2 = new RadialGradient(0.0f, 0.0f, f3, new int[]{0, this.shadowStartColor, this.shadowMiddleColor, this.shadowEndColor}, new float[]{0.0f, f4, f4 + ((1.0f - f4) / 2.0f), 1.0f}, Shader.TileMode.CLAMP);
            paint.setShader(radialGradient);
        } else {
            i = 3;
        }
        Paint paint2 = this.edgeShadowPaint;
        float f5 = rectF.top;
        float f6 = rectF2.top;
        int[] iArr = new int[i];
        iArr[0] = this.shadowStartColor;
        iArr[1] = this.shadowMiddleColor;
        iArr[2] = this.shadowEndColor;
        float[] fArr = new float[i];
        // fill-array-data instruction
        fArr[0] = 0;
        fArr[1] = 1056964608;
        fArr[2] = 1065353216;
        paint2.setShader(new LinearGradient(0.0f, f5, 0.0f, f6, iArr, fArr, Shader.TileMode.CLAMP));
        this.edgeShadowPaint.setAntiAlias(false);
    }

    public static float calculateHorizontalPadding(float maxShadowSize2, float cornerRadius2, boolean addPaddingForCorners2) {
        return addPaddingForCorners2 ? (float) (((double) maxShadowSize2) + ((1.0d - COS_45) * ((double) cornerRadius2))) : maxShadowSize2;
    }

    public static float calculateVerticalPadding(float maxShadowSize2, float cornerRadius2, boolean addPaddingForCorners2) {
        return addPaddingForCorners2 ? (float) (((double) (SHADOW_MULTIPLIER * maxShadowSize2)) + ((1.0d - COS_45) * ((double) cornerRadius2))) : SHADOW_MULTIPLIER * maxShadowSize2;
    }

    private void drawShadow(Canvas canvas) {
        float f;
        float f2;
        float f3;
        int i;
        float f4;
        float f5;
        Canvas canvas2 = canvas;
        int save = canvas.save();
        canvas2.rotate(this.rotation, this.contentBounds.centerX(), this.contentBounds.centerY());
        float f6 = (-this.cornerRadius) - this.shadowSize;
        float f7 = this.cornerRadius;
        boolean z = true;
        boolean z2 = this.contentBounds.width() - (f7 * 2.0f) > 0.0f;
        if (this.contentBounds.height() - (f7 * 2.0f) <= 0.0f) {
            z = false;
        }
        boolean z3 = z;
        float f8 = this.rawShadowSize;
        float f9 = f8 - (SHADOW_TOP_SCALE * f8);
        float f10 = f8 - (0.5f * f8);
        float f11 = f7 / (f7 + f10);
        float f12 = f7 / (f7 + f9);
        float f13 = f7 / (f7 + (f8 - (f8 * 1.0f)));
        int save2 = canvas.save();
        canvas2.translate(this.contentBounds.left + f7, this.contentBounds.top + f7);
        canvas2.scale(f11, f12);
        canvas2.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (z2) {
            canvas2.scale(1.0f / f11, 1.0f);
            float f14 = f9;
            i = save2;
            f4 = f13;
            f2 = f12;
            f = f11;
            float f15 = f10;
            f3 = 1.0f;
            canvas.drawRect(0.0f, f6, this.contentBounds.width() - (f7 * 2.0f), -this.cornerRadius, this.edgeShadowPaint);
        } else {
            f4 = f13;
            f2 = f12;
            f = f11;
            float f16 = f9;
            float f17 = f10;
            i = save2;
            f3 = 1.0f;
        }
        canvas2.restoreToCount(i);
        int save3 = canvas.save();
        canvas2.translate(this.contentBounds.right - f7, this.contentBounds.bottom - f7);
        float f18 = f;
        canvas2.scale(f18, f4);
        canvas2.rotate(180.0f);
        canvas2.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (z2) {
            canvas2.scale(f3 / f18, f3);
            f5 = f18;
            canvas.drawRect(0.0f, f6, this.contentBounds.width() - (f7 * 2.0f), (-this.cornerRadius) + this.shadowSize, this.edgeShadowPaint);
        } else {
            f5 = f18;
        }
        canvas2.restoreToCount(save3);
        int save4 = canvas.save();
        canvas2.translate(this.contentBounds.left + f7, this.contentBounds.bottom - f7);
        canvas2.scale(f5, f4);
        canvas2.rotate(270.0f);
        canvas2.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (z3) {
            canvas2.scale(1.0f / f4, 1.0f);
            canvas.drawRect(0.0f, f6, this.contentBounds.height() - (f7 * 2.0f), -this.cornerRadius, this.edgeShadowPaint);
        }
        canvas2.restoreToCount(save4);
        int save5 = canvas.save();
        canvas2.translate(this.contentBounds.right - f7, this.contentBounds.top + f7);
        float f19 = f2;
        canvas2.scale(f5, f19);
        canvas2.rotate(90.0f);
        canvas2.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (z3) {
            canvas2.scale(1.0f / f19, 1.0f);
            float f20 = f19;
            canvas.drawRect(0.0f, f6, this.contentBounds.height() - (2.0f * f7), -this.cornerRadius, this.edgeShadowPaint);
        } else {
            float f21 = f19;
        }
        canvas2.restoreToCount(save5);
        canvas2.restoreToCount(save);
    }

    private static int toEven(float value) {
        int round = Math.round(value);
        return round % 2 == 1 ? round - 1 : round;
    }

    public void draw(Canvas canvas) {
        if (this.dirty) {
            buildComponents(getBounds());
            this.dirty = false;
        }
        drawShadow(canvas);
        super.draw(canvas);
    }

    public float getCornerRadius() {
        return this.cornerRadius;
    }

    public float getMaxShadowSize() {
        return this.rawMaxShadowSize;
    }

    public float getMinHeight() {
        float f = this.rawMaxShadowSize;
        return (this.rawMaxShadowSize * SHADOW_MULTIPLIER * 2.0f) + (Math.max(f, this.cornerRadius + ((f * SHADOW_MULTIPLIER) / 2.0f)) * 2.0f);
    }

    public float getMinWidth() {
        float f = this.rawMaxShadowSize;
        return (this.rawMaxShadowSize * 2.0f) + (Math.max(f, this.cornerRadius + (f / 2.0f)) * 2.0f);
    }

    public int getOpacity() {
        return -3;
    }

    public boolean getPadding(Rect padding) {
        int ceil = (int) Math.ceil((double) calculateVerticalPadding(this.rawMaxShadowSize, this.cornerRadius, this.addPaddingForCorners));
        int ceil2 = (int) Math.ceil((double) calculateHorizontalPadding(this.rawMaxShadowSize, this.cornerRadius, this.addPaddingForCorners));
        padding.set(ceil2, ceil, ceil2, ceil);
        return true;
    }

    public float getShadowSize() {
        return this.rawShadowSize;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect bounds) {
        this.dirty = true;
    }

    public void setAddPaddingForCorners(boolean addPaddingForCorners2) {
        this.addPaddingForCorners = addPaddingForCorners2;
        invalidateSelf();
    }

    public void setAlpha(int alpha) {
        super.setAlpha(alpha);
        this.cornerShadowPaint.setAlpha(alpha);
        this.edgeShadowPaint.setAlpha(alpha);
    }

    public void setCornerRadius(float radius) {
        float radius2 = (float) Math.round(radius);
        if (this.cornerRadius != radius2) {
            this.cornerRadius = radius2;
            this.dirty = true;
            invalidateSelf();
        }
    }

    public void setMaxShadowSize(float size) {
        setShadowSize(this.rawShadowSize, size);
    }

    public final void setRotation(float rotation2) {
        if (this.rotation != rotation2) {
            this.rotation = rotation2;
            invalidateSelf();
        }
    }

    public void setShadowSize(float size) {
        setShadowSize(size, this.rawMaxShadowSize);
    }

    public void setShadowSize(float shadowSize2, float maxShadowSize2) {
        if (shadowSize2 < 0.0f || maxShadowSize2 < 0.0f) {
            throw new IllegalArgumentException("invalid shadow size");
        }
        float shadowSize3 = (float) toEven(shadowSize2);
        float maxShadowSize3 = (float) toEven(maxShadowSize2);
        if (shadowSize3 > maxShadowSize3) {
            shadowSize3 = maxShadowSize3;
            if (!this.printedShadowClipWarning) {
                this.printedShadowClipWarning = true;
            }
        }
        if (this.rawShadowSize != shadowSize3 || this.rawMaxShadowSize != maxShadowSize3) {
            this.rawShadowSize = shadowSize3;
            this.rawMaxShadowSize = maxShadowSize3;
            this.shadowSize = (float) Math.round(SHADOW_MULTIPLIER * shadowSize3);
            this.maxShadowSize = maxShadowSize3;
            this.dirty = true;
            invalidateSelf();
        }
    }
}

package androidx.cardview.widget;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import androidx.cardview.R;

class RoundRectDrawableWithShadow extends Drawable {
    private static final double COS_45 = Math.cos(Math.toRadians(45.0d));
    private static final float SHADOW_MULTIPLIER = 1.5f;
    static RoundRectHelper sRoundRectHelper;
    private boolean mAddPaddingForCorners = true;
    private ColorStateList mBackground;
    private final RectF mCardBounds;
    private float mCornerRadius;
    private Paint mCornerShadowPaint;
    private Path mCornerShadowPath;
    private boolean mDirty = true;
    private Paint mEdgeShadowPaint;
    private final int mInsetShadow;
    private Paint mPaint;
    private boolean mPrintedShadowClipWarning = false;
    private float mRawMaxShadowSize;
    private float mRawShadowSize;
    private final int mShadowEndColor;
    private float mShadowSize;
    private final int mShadowStartColor;

    interface RoundRectHelper {
        void drawRoundRect(Canvas canvas, RectF rectF, float f, Paint paint);
    }

    RoundRectDrawableWithShadow(Resources resources, ColorStateList backgroundColor, float radius, float shadowSize, float maxShadowSize) {
        this.mShadowStartColor = resources.getColor(R.color.cardview_shadow_start_color);
        this.mShadowEndColor = resources.getColor(R.color.cardview_shadow_end_color);
        this.mInsetShadow = resources.getDimensionPixelSize(R.dimen.cardview_compat_inset_shadow);
        this.mPaint = new Paint(5);
        setBackground(backgroundColor);
        Paint paint = new Paint(5);
        this.mCornerShadowPaint = paint;
        paint.setStyle(Paint.Style.FILL);
        this.mCornerRadius = (float) ((int) (0.5f + radius));
        this.mCardBounds = new RectF();
        Paint paint2 = new Paint(this.mCornerShadowPaint);
        this.mEdgeShadowPaint = paint2;
        paint2.setAntiAlias(false);
        setShadowSize(shadowSize, maxShadowSize);
    }

    private void buildComponents(Rect bounds) {
        float f = this.mRawMaxShadowSize * SHADOW_MULTIPLIER;
        this.mCardBounds.set(((float) bounds.left) + this.mRawMaxShadowSize, ((float) bounds.top) + f, ((float) bounds.right) - this.mRawMaxShadowSize, ((float) bounds.bottom) - f);
        buildShadowCorners();
    }

    private void buildShadowCorners() {
        float f = this.mCornerRadius;
        RectF rectF = new RectF(-f, -f, f, f);
        RectF rectF2 = new RectF(rectF);
        float f2 = this.mShadowSize;
        rectF2.inset(-f2, -f2);
        Path path = this.mCornerShadowPath;
        if (path == null) {
            this.mCornerShadowPath = new Path();
        } else {
            path.reset();
        }
        this.mCornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
        this.mCornerShadowPath.moveTo(-this.mCornerRadius, 0.0f);
        this.mCornerShadowPath.rLineTo(-this.mShadowSize, 0.0f);
        this.mCornerShadowPath.arcTo(rectF2, 180.0f, 90.0f, false);
        this.mCornerShadowPath.arcTo(rectF, 270.0f, -90.0f, false);
        this.mCornerShadowPath.close();
        float f3 = this.mCornerRadius;
        float f4 = f3 / (this.mShadowSize + f3);
        Paint paint = this.mCornerShadowPaint;
        float f5 = this.mShadowSize + this.mCornerRadius;
        int i = this.mShadowStartColor;
        paint.setShader(new RadialGradient(0.0f, 0.0f, f5, new int[]{i, i, this.mShadowEndColor}, new float[]{0.0f, f4, 1.0f}, Shader.TileMode.CLAMP));
        Paint paint2 = this.mEdgeShadowPaint;
        float f6 = this.mCornerRadius;
        float f7 = this.mShadowSize;
        int i2 = this.mShadowStartColor;
        paint2.setShader(new LinearGradient(0.0f, (-f6) + f7, 0.0f, (-f6) - f7, new int[]{i2, i2, this.mShadowEndColor}, new float[]{0.0f, 0.5f, 1.0f}, Shader.TileMode.CLAMP));
        this.mEdgeShadowPaint.setAntiAlias(false);
    }

    static float calculateHorizontalPadding(float maxShadowSize, float cornerRadius, boolean addPaddingForCorners) {
        return addPaddingForCorners ? (float) (((double) maxShadowSize) + ((1.0d - COS_45) * ((double) cornerRadius))) : maxShadowSize;
    }

    static float calculateVerticalPadding(float maxShadowSize, float cornerRadius, boolean addPaddingForCorners) {
        return addPaddingForCorners ? (float) (((double) (SHADOW_MULTIPLIER * maxShadowSize)) + ((1.0d - COS_45) * ((double) cornerRadius))) : SHADOW_MULTIPLIER * maxShadowSize;
    }

    private void drawShadow(Canvas canvas) {
        float f = this.mCornerRadius;
        float f2 = (-f) - this.mShadowSize;
        float f3 = f + ((float) this.mInsetShadow) + (this.mRawShadowSize / 2.0f);
        boolean z = true;
        boolean z2 = this.mCardBounds.width() - (f3 * 2.0f) > 0.0f;
        if (this.mCardBounds.height() - (f3 * 2.0f) <= 0.0f) {
            z = false;
        }
        boolean z3 = z;
        int save = canvas.save();
        canvas.translate(this.mCardBounds.left + f3, this.mCardBounds.top + f3);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (z2) {
            canvas.drawRect(0.0f, f2, this.mCardBounds.width() - (f3 * 2.0f), -this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(save);
        int save2 = canvas.save();
        canvas.translate(this.mCardBounds.right - f3, this.mCardBounds.bottom - f3);
        canvas.rotate(180.0f);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (z2) {
            canvas.drawRect(0.0f, f2, this.mCardBounds.width() - (f3 * 2.0f), (-this.mCornerRadius) + this.mShadowSize, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(save2);
        int save3 = canvas.save();
        canvas.translate(this.mCardBounds.left + f3, this.mCardBounds.bottom - f3);
        canvas.rotate(270.0f);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (z3) {
            canvas.drawRect(0.0f, f2, this.mCardBounds.height() - (f3 * 2.0f), -this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(save3);
        int save4 = canvas.save();
        canvas.translate(this.mCardBounds.right - f3, this.mCardBounds.top + f3);
        canvas.rotate(90.0f);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (z3) {
            canvas.drawRect(0.0f, f2, this.mCardBounds.height() - (2.0f * f3), -this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(save4);
    }

    private void setBackground(ColorStateList color) {
        ColorStateList valueOf = color == null ? ColorStateList.valueOf(0) : color;
        this.mBackground = valueOf;
        this.mPaint.setColor(valueOf.getColorForState(getState(), this.mBackground.getDefaultColor()));
    }

    private void setShadowSize(float shadowSize, float maxShadowSize) {
        if (shadowSize < 0.0f) {
            throw new IllegalArgumentException("Invalid shadow size " + shadowSize + ". Must be >= 0");
        } else if (maxShadowSize >= 0.0f) {
            float shadowSize2 = (float) toEven(shadowSize);
            float maxShadowSize2 = (float) toEven(maxShadowSize);
            if (shadowSize2 > maxShadowSize2) {
                shadowSize2 = maxShadowSize2;
                if (!this.mPrintedShadowClipWarning) {
                    this.mPrintedShadowClipWarning = true;
                }
            }
            if (this.mRawShadowSize != shadowSize2 || this.mRawMaxShadowSize != maxShadowSize2) {
                this.mRawShadowSize = shadowSize2;
                this.mRawMaxShadowSize = maxShadowSize2;
                this.mShadowSize = (float) ((int) ((SHADOW_MULTIPLIER * shadowSize2) + ((float) this.mInsetShadow) + 0.5f));
                this.mDirty = true;
                invalidateSelf();
            }
        } else {
            throw new IllegalArgumentException("Invalid max shadow size " + maxShadowSize + ". Must be >= 0");
        }
    }

    private int toEven(float value) {
        int i = (int) (0.5f + value);
        return i % 2 == 1 ? i - 1 : i;
    }

    public void draw(Canvas canvas) {
        if (this.mDirty) {
            buildComponents(getBounds());
            this.mDirty = false;
        }
        canvas.translate(0.0f, this.mRawShadowSize / 2.0f);
        drawShadow(canvas);
        canvas.translate(0.0f, (-this.mRawShadowSize) / 2.0f);
        sRoundRectHelper.drawRoundRect(canvas, this.mCardBounds, this.mCornerRadius, this.mPaint);
    }

    /* access modifiers changed from: package-private */
    public ColorStateList getColor() {
        return this.mBackground;
    }

    /* access modifiers changed from: package-private */
    public float getCornerRadius() {
        return this.mCornerRadius;
    }

    /* access modifiers changed from: package-private */
    public void getMaxShadowAndCornerPadding(Rect into) {
        getPadding(into);
    }

    /* access modifiers changed from: package-private */
    public float getMaxShadowSize() {
        return this.mRawMaxShadowSize;
    }

    /* access modifiers changed from: package-private */
    public float getMinHeight() {
        float f = this.mRawMaxShadowSize;
        return (((this.mRawMaxShadowSize * SHADOW_MULTIPLIER) + ((float) this.mInsetShadow)) * 2.0f) + (Math.max(f, this.mCornerRadius + ((float) this.mInsetShadow) + ((f * SHADOW_MULTIPLIER) / 2.0f)) * 2.0f);
    }

    /* access modifiers changed from: package-private */
    public float getMinWidth() {
        float f = this.mRawMaxShadowSize;
        return ((this.mRawMaxShadowSize + ((float) this.mInsetShadow)) * 2.0f) + (Math.max(f, this.mCornerRadius + ((float) this.mInsetShadow) + (f / 2.0f)) * 2.0f);
    }

    public int getOpacity() {
        return -3;
    }

    public boolean getPadding(Rect padding) {
        int ceil = (int) Math.ceil((double) calculateVerticalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
        int ceil2 = (int) Math.ceil((double) calculateHorizontalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
        padding.set(ceil2, ceil, ceil2, ceil);
        return true;
    }

    /* access modifiers changed from: package-private */
    public float getShadowSize() {
        return this.mRawShadowSize;
    }

    public boolean isStateful() {
        ColorStateList colorStateList = this.mBackground;
        return (colorStateList != null && colorStateList.isStateful()) || super.isStateful();
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.mDirty = true;
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] stateSet) {
        ColorStateList colorStateList = this.mBackground;
        int colorForState = colorStateList.getColorForState(stateSet, colorStateList.getDefaultColor());
        if (this.mPaint.getColor() == colorForState) {
            return false;
        }
        this.mPaint.setColor(colorForState);
        this.mDirty = true;
        invalidateSelf();
        return true;
    }

    /* access modifiers changed from: package-private */
    public void setAddPaddingForCorners(boolean addPaddingForCorners) {
        this.mAddPaddingForCorners = addPaddingForCorners;
        invalidateSelf();
    }

    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
        this.mCornerShadowPaint.setAlpha(alpha);
        this.mEdgeShadowPaint.setAlpha(alpha);
    }

    /* access modifiers changed from: package-private */
    public void setColor(ColorStateList color) {
        setBackground(color);
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    /* access modifiers changed from: package-private */
    public void setCornerRadius(float radius) {
        if (radius >= 0.0f) {
            float radius2 = (float) ((int) (0.5f + radius));
            if (this.mCornerRadius != radius2) {
                this.mCornerRadius = radius2;
                this.mDirty = true;
                invalidateSelf();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Invalid radius " + radius + ". Must be >= 0");
    }

    /* access modifiers changed from: package-private */
    public void setMaxShadowSize(float size) {
        setShadowSize(this.mRawShadowSize, size);
    }

    /* access modifiers changed from: package-private */
    public void setShadowSize(float size) {
        setShadowSize(size, this.mRawMaxShadowSize);
    }
}

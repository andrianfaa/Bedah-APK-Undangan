package androidx.core.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

public abstract class RoundedBitmapDrawable extends Drawable {
    private static final int DEFAULT_PAINT_FLAGS = 3;
    private boolean mApplyGravity = true;
    final Bitmap mBitmap;
    private int mBitmapHeight;
    private final BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private float mCornerRadius;
    final Rect mDstRect = new Rect();
    private final RectF mDstRectF = new RectF();
    private int mGravity = 119;
    private boolean mIsCircular;
    private final Paint mPaint = new Paint(3);
    private final Matrix mShaderMatrix = new Matrix();
    private int mTargetDensity = 160;

    RoundedBitmapDrawable(Resources res, Bitmap bitmap) {
        if (res != null) {
            this.mTargetDensity = res.getDisplayMetrics().densityDpi;
        }
        this.mBitmap = bitmap;
        if (bitmap != null) {
            computeBitmapSize();
            this.mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            return;
        }
        this.mBitmapHeight = -1;
        this.mBitmapWidth = -1;
        this.mBitmapShader = null;
    }

    private void computeBitmapSize() {
        this.mBitmapWidth = this.mBitmap.getScaledWidth(this.mTargetDensity);
        this.mBitmapHeight = this.mBitmap.getScaledHeight(this.mTargetDensity);
    }

    private static boolean isGreaterThanZero(float toCompare) {
        return toCompare > 0.05f;
    }

    private void updateCircularCornerRadius() {
        this.mCornerRadius = (float) (Math.min(this.mBitmapHeight, this.mBitmapWidth) / 2);
    }

    public void draw(Canvas canvas) {
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null) {
            updateDstRect();
            if (this.mPaint.getShader() == null) {
                canvas.drawBitmap(bitmap, (Rect) null, this.mDstRect, this.mPaint);
                return;
            }
            RectF rectF = this.mDstRectF;
            float f = this.mCornerRadius;
            canvas.drawRoundRect(rectF, f, f, this.mPaint);
        }
    }

    public int getAlpha() {
        return this.mPaint.getAlpha();
    }

    public final Bitmap getBitmap() {
        return this.mBitmap;
    }

    public ColorFilter getColorFilter() {
        return this.mPaint.getColorFilter();
    }

    public float getCornerRadius() {
        return this.mCornerRadius;
    }

    public int getGravity() {
        return this.mGravity;
    }

    public int getIntrinsicHeight() {
        return this.mBitmapHeight;
    }

    public int getIntrinsicWidth() {
        return this.mBitmapWidth;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000c, code lost:
        r0 = r4.mBitmap;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getOpacity() {
        /*
            r4 = this;
            int r0 = r4.mGravity
            r1 = -3
            r2 = 119(0x77, float:1.67E-43)
            if (r0 != r2) goto L_0x002d
            boolean r0 = r4.mIsCircular
            if (r0 == 0) goto L_0x000c
            goto L_0x002d
        L_0x000c:
            android.graphics.Bitmap r0 = r4.mBitmap
            if (r0 == 0) goto L_0x002b
            boolean r2 = r0.hasAlpha()
            if (r2 != 0) goto L_0x002b
            android.graphics.Paint r2 = r4.mPaint
            int r2 = r2.getAlpha()
            r3 = 255(0xff, float:3.57E-43)
            if (r2 < r3) goto L_0x002b
            float r2 = r4.mCornerRadius
            boolean r2 = isGreaterThanZero(r2)
            if (r2 == 0) goto L_0x0029
            goto L_0x002b
        L_0x0029:
            r1 = -1
            goto L_0x002c
        L_0x002b:
        L_0x002c:
            return r1
        L_0x002d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.graphics.drawable.RoundedBitmapDrawable.getOpacity():int");
    }

    public final Paint getPaint() {
        return this.mPaint;
    }

    /* access modifiers changed from: package-private */
    public void gravityCompatApply(int gravity, int bitmapWidth, int bitmapHeight, Rect bounds, Rect outRect) {
        throw new UnsupportedOperationException();
    }

    public boolean hasAntiAlias() {
        return this.mPaint.isAntiAlias();
    }

    public boolean hasMipMap() {
        throw new UnsupportedOperationException();
    }

    public boolean isCircular() {
        return this.mIsCircular;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        if (this.mIsCircular) {
            updateCircularCornerRadius();
        }
        this.mApplyGravity = true;
    }

    public void setAlpha(int alpha) {
        if (alpha != this.mPaint.getAlpha()) {
            this.mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    public void setAntiAlias(boolean aa) {
        this.mPaint.setAntiAlias(aa);
        invalidateSelf();
    }

    public void setCircular(boolean circular) {
        this.mIsCircular = circular;
        this.mApplyGravity = true;
        if (circular) {
            updateCircularCornerRadius();
            this.mPaint.setShader(this.mBitmapShader);
            invalidateSelf();
            return;
        }
        setCornerRadius(0.0f);
    }

    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
        invalidateSelf();
    }

    public void setCornerRadius(float cornerRadius) {
        if (this.mCornerRadius != cornerRadius) {
            this.mIsCircular = false;
            if (isGreaterThanZero(cornerRadius)) {
                this.mPaint.setShader(this.mBitmapShader);
            } else {
                this.mPaint.setShader((Shader) null);
            }
            this.mCornerRadius = cornerRadius;
            invalidateSelf();
        }
    }

    public void setDither(boolean dither) {
        this.mPaint.setDither(dither);
        invalidateSelf();
    }

    public void setFilterBitmap(boolean filter) {
        this.mPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            this.mGravity = gravity;
            this.mApplyGravity = true;
            invalidateSelf();
        }
    }

    public void setMipMap(boolean mipMap) {
        throw new UnsupportedOperationException();
    }

    public void setTargetDensity(int density) {
        if (this.mTargetDensity != density) {
            this.mTargetDensity = density == 0 ? 160 : density;
            if (this.mBitmap != null) {
                computeBitmapSize();
            }
            invalidateSelf();
        }
    }

    public void setTargetDensity(Canvas canvas) {
        setTargetDensity(canvas.getDensity());
    }

    public void setTargetDensity(DisplayMetrics metrics) {
        setTargetDensity(metrics.densityDpi);
    }

    /* access modifiers changed from: package-private */
    public void updateDstRect() {
        if (this.mApplyGravity) {
            if (this.mIsCircular) {
                int min = Math.min(this.mBitmapWidth, this.mBitmapHeight);
                gravityCompatApply(this.mGravity, min, min, getBounds(), this.mDstRect);
                int min2 = Math.min(this.mDstRect.width(), this.mDstRect.height());
                this.mDstRect.inset(Math.max(0, (this.mDstRect.width() - min2) / 2), Math.max(0, (this.mDstRect.height() - min2) / 2));
                this.mCornerRadius = ((float) min2) * 0.5f;
            } else {
                gravityCompatApply(this.mGravity, this.mBitmapWidth, this.mBitmapHeight, getBounds(), this.mDstRect);
            }
            this.mDstRectF.set(this.mDstRect);
            if (this.mBitmapShader != null) {
                this.mShaderMatrix.setTranslate(this.mDstRectF.left, this.mDstRectF.top);
                this.mShaderMatrix.preScale(this.mDstRectF.width() / ((float) this.mBitmap.getWidth()), this.mDstRectF.height() / ((float) this.mBitmap.getHeight()));
                this.mBitmapShader.setLocalMatrix(this.mShaderMatrix);
                this.mPaint.setShader(this.mBitmapShader);
            }
            this.mApplyGravity = false;
        }
    }
}

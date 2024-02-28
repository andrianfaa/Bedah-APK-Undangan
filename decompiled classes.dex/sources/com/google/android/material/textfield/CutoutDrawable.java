package com.google.android.material.textfield;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

class CutoutDrawable extends MaterialShapeDrawable {
    private final RectF cutoutBounds;
    private final Paint cutoutPaint;

    CutoutDrawable() {
        this((ShapeAppearanceModel) null);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    CutoutDrawable(ShapeAppearanceModel shapeAppearanceModel) {
        super(shapeAppearanceModel != null ? shapeAppearanceModel : new ShapeAppearanceModel());
        this.cutoutPaint = new Paint(1);
        setPaintStyles();
        this.cutoutBounds = new RectF();
    }

    private void setPaintStyles() {
        this.cutoutPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.cutoutPaint.setColor(-1);
        this.cutoutPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    /* access modifiers changed from: protected */
    public void drawStrokeShape(Canvas canvas) {
        if (this.cutoutBounds.isEmpty()) {
            super.drawStrokeShape(canvas);
            return;
        }
        canvas.save();
        if (Build.VERSION.SDK_INT >= 26) {
            canvas.clipOutRect(this.cutoutBounds);
        } else {
            canvas.clipRect(this.cutoutBounds, Region.Op.DIFFERENCE);
        }
        super.drawStrokeShape(canvas);
        canvas.restore();
    }

    /* access modifiers changed from: package-private */
    public boolean hasCutout() {
        return !this.cutoutBounds.isEmpty();
    }

    /* access modifiers changed from: package-private */
    public void removeCutout() {
        setCutout(0.0f, 0.0f, 0.0f, 0.0f);
    }

    /* access modifiers changed from: package-private */
    public void setCutout(float left, float top, float right, float bottom) {
        if (left != this.cutoutBounds.left || top != this.cutoutBounds.top || right != this.cutoutBounds.right || bottom != this.cutoutBounds.bottom) {
            this.cutoutBounds.set(left, top, right, bottom);
            invalidateSelf();
        }
    }

    /* access modifiers changed from: package-private */
    public void setCutout(RectF bounds) {
        setCutout(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }
}

package com.google.android.material.bottomappbar;

import com.google.android.material.shape.EdgeTreatment;
import com.google.android.material.shape.ShapePath;

public class BottomAppBarTopEdgeTreatment extends EdgeTreatment implements Cloneable {
    private static final int ANGLE_LEFT = 180;
    private static final int ANGLE_UP = 270;
    private static final int ARC_HALF = 180;
    private static final int ARC_QUARTER = 90;
    private static final float ROUNDED_CORNER_FAB_OFFSET = 1.75f;
    private float cradleVerticalOffset;
    private float fabCornerSize = -1.0f;
    private float fabDiameter;
    private float fabMargin;
    private float horizontalOffset;
    private float roundedCornerRadius;

    public BottomAppBarTopEdgeTreatment(float fabMargin2, float roundedCornerRadius2, float cradleVerticalOffset2) {
        this.fabMargin = fabMargin2;
        this.roundedCornerRadius = roundedCornerRadius2;
        setCradleVerticalOffset(cradleVerticalOffset2);
        this.horizontalOffset = 0.0f;
    }

    /* access modifiers changed from: package-private */
    public float getCradleVerticalOffset() {
        return this.cradleVerticalOffset;
    }

    public void getEdgePath(float length, float center, float interpolation, ShapePath shapePath) {
        float f;
        float f2;
        float f3 = length;
        ShapePath shapePath2 = shapePath;
        float f4 = this.fabDiameter;
        if (f4 == 0.0f) {
            shapePath2.lineTo(f3, 0.0f);
            return;
        }
        float f5 = ((this.fabMargin * 2.0f) + f4) / 2.0f;
        float f6 = interpolation * this.roundedCornerRadius;
        float f7 = center + this.horizontalOffset;
        float f8 = (this.cradleVerticalOffset * interpolation) + ((1.0f - interpolation) * f5);
        if (f8 / f5 >= 1.0f) {
            shapePath2.lineTo(f3, 0.0f);
            return;
        }
        float f9 = this.fabCornerSize;
        float f10 = f9 * interpolation;
        boolean z = f9 == -1.0f || Math.abs((f9 * 2.0f) - f4) < 0.1f;
        if (!z) {
            f2 = 1.75f;
            f = 0.0f;
        } else {
            f2 = 0.0f;
            f = f8;
        }
        float f11 = f5 + f6;
        float f12 = f + f6;
        float sqrt = (float) Math.sqrt((double) ((f11 * f11) - (f12 * f12)));
        float f13 = f7 - sqrt;
        float f14 = f7 + sqrt;
        float degrees = (float) Math.toDegrees(Math.atan((double) (sqrt / f12)));
        float f15 = (90.0f - degrees) + f2;
        shapePath2.lineTo(f13, 0.0f);
        float f16 = degrees;
        float f17 = f13;
        float f18 = sqrt;
        shapePath.addArc(f13 - f6, 0.0f, f13 + f6, f6 * 2.0f, 270.0f, f16);
        if (z) {
            shapePath.addArc(f7 - f5, (-f5) - f, f7 + f5, f5 - f, 180.0f - f15, (f15 * 2.0f) - 180.0f);
        } else {
            float f19 = this.fabMargin;
            shapePath.addArc(f7 - f5, -(f10 + f19), (f7 - f5) + f19 + (f10 * 2.0f), f19 + f10, 180.0f - f15, ((f15 * 2.0f) - 180.0f) / 2.0f);
            float f20 = this.fabMargin;
            shapePath2.lineTo((f7 + f5) - (f10 + (f20 / 2.0f)), f10 + f20);
            float f21 = this.fabMargin;
            shapePath.addArc((f7 + f5) - ((f10 * 2.0f) + f21), -(f10 + f21), f7 + f5, f21 + f10, 90.0f, f15 - 0.049804688f);
        }
        shapePath.addArc(f14 - f6, 0.0f, f14 + f6, f6 * 2.0f, 270.0f - f16, f16);
        shapePath2.lineTo(f3, 0.0f);
    }

    public float getFabCornerRadius() {
        return this.fabCornerSize;
    }

    /* access modifiers changed from: package-private */
    public float getFabCradleMargin() {
        return this.fabMargin;
    }

    /* access modifiers changed from: package-private */
    public float getFabCradleRoundedCornerRadius() {
        return this.roundedCornerRadius;
    }

    public float getFabDiameter() {
        return this.fabDiameter;
    }

    public float getHorizontalOffset() {
        return this.horizontalOffset;
    }

    /* access modifiers changed from: package-private */
    public void setCradleVerticalOffset(float cradleVerticalOffset2) {
        if (cradleVerticalOffset2 >= 0.0f) {
            this.cradleVerticalOffset = cradleVerticalOffset2;
            return;
        }
        throw new IllegalArgumentException("cradleVerticalOffset must be positive.");
    }

    public void setFabCornerSize(float size) {
        this.fabCornerSize = size;
    }

    /* access modifiers changed from: package-private */
    public void setFabCradleMargin(float fabMargin2) {
        this.fabMargin = fabMargin2;
    }

    /* access modifiers changed from: package-private */
    public void setFabCradleRoundedCornerRadius(float roundedCornerRadius2) {
        this.roundedCornerRadius = roundedCornerRadius2;
    }

    public void setFabDiameter(float fabDiameter2) {
        this.fabDiameter = fabDiameter2;
    }

    /* access modifiers changed from: package-private */
    public void setHorizontalOffset(float horizontalOffset2) {
        this.horizontalOffset = horizontalOffset2;
    }
}

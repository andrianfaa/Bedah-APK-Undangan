package com.google.android.material.color;

final class ViewingConditions {
    public static final ViewingConditions DEFAULT = make(ColorUtils.whitePointD65(), (float) ((((double) ColorUtils.yFromLstar(50.0f)) * 63.66197723675813d) / 100.0d), 50.0f, 2.0f, false);
    private final float aw;
    private final float c;
    private final float fl;
    private final float flRoot;
    private final float n;
    private final float nbb;
    private final float nc;
    private final float ncb;
    private final float[] rgbD;
    private final float z;

    private ViewingConditions(float n2, float aw2, float nbb2, float ncb2, float c2, float nc2, float[] rgbD2, float fl2, float flRoot2, float z2) {
        this.n = n2;
        this.aw = aw2;
        this.nbb = nbb2;
        this.ncb = ncb2;
        this.c = c2;
        this.nc = nc2;
        this.rgbD = rgbD2;
        this.fl = fl2;
        this.flRoot = flRoot2;
        this.z = z2;
    }

    static ViewingConditions make(float[] whitePoint, float adaptingLuminance, float backgroundLstar, float surround, boolean discountingIlluminant) {
        float f = adaptingLuminance;
        float[][] fArr = Cam16.XYZ_TO_CAM16RGB;
        float[] fArr2 = whitePoint;
        float f2 = (fArr2[0] * fArr[0][0]) + (fArr2[1] * fArr[0][1]) + (fArr2[2] * fArr[0][2]);
        float f3 = (fArr2[0] * fArr[1][0]) + (fArr2[1] * fArr[1][1]) + (fArr2[2] * fArr[1][2]);
        float f4 = (fArr2[0] * fArr[2][0]) + (fArr2[1] * fArr[2][1]) + (fArr2[2] * fArr[2][2]);
        float f5 = (surround / 10.0f) + 0.8f;
        float lerp = ((double) f5) >= 0.9d ? MathUtils.lerp(0.59f, 0.69f, (f5 - 0.9f) * 10.0f) : MathUtils.lerp(0.525f, 0.59f, (f5 - 0.8f) * 10.0f);
        float exp = discountingIlluminant ? 1.0f : (1.0f - (((float) Math.exp((double) (((-f) - 42.0f) / 92.0f))) * 0.2777778f)) * f5;
        float f6 = ((double) exp) > 1.0d ? 1.0f : ((double) exp) < 0.0d ? 0.0f : exp;
        float[] fArr3 = {(((100.0f / f2) * f6) + 1.0f) - f6, (((100.0f / f3) * f6) + 1.0f) - f6, (((100.0f / f4) * f6) + 1.0f) - f6};
        float f7 = 1.0f / ((5.0f * f) + 1.0f);
        float f8 = f7 * f7 * f7 * f7;
        float f9 = 1.0f - f8;
        float f10 = f3;
        float cbrt = (f8 * f) + (0.1f * f9 * f9 * ((float) Math.cbrt(((double) f) * 5.0d)));
        float yFromLstar = ColorUtils.yFromLstar(backgroundLstar) / whitePoint[1];
        float sqrt = ((float) Math.sqrt((double) yFromLstar)) + 1.48f;
        float f11 = cbrt;
        float pow = 0.725f / ((float) Math.pow((double) yFromLstar, 0.2d));
        float f12 = pow;
        float f13 = f2;
        float[] fArr4 = {(float) Math.pow(((double) ((fArr3[0] * f11) * f2)) / 100.0d, 0.42d), (float) Math.pow(((double) ((fArr3[1] * f11) * f10)) / 100.0d, 0.42d), (float) Math.pow(((double) ((fArr3[2] * f11) * f4)) / 100.0d, 0.42d)};
        float[] fArr5 = {(fArr4[0] * 400.0f) / (fArr4[0] + 27.13f), (fArr4[1] * 400.0f) / (fArr4[1] + 27.13f), (fArr4[2] * 400.0f) / (fArr4[2] + 27.13f)};
        float f14 = f11;
        float[][] fArr6 = fArr;
        return new ViewingConditions(yFromLstar, ((fArr5[0] * 2.0f) + fArr5[1] + (fArr5[2] * 0.05f)) * pow, pow, f12, lerp, f5, fArr3, f14, (float) Math.pow((double) f14, 0.25d), sqrt);
    }

    public float getAw() {
        return this.aw;
    }

    /* access modifiers changed from: package-private */
    public float getC() {
        return this.c;
    }

    /* access modifiers changed from: package-private */
    public float getFl() {
        return this.fl;
    }

    public float getFlRoot() {
        return this.flRoot;
    }

    public float getN() {
        return this.n;
    }

    public float getNbb() {
        return this.nbb;
    }

    /* access modifiers changed from: package-private */
    public float getNc() {
        return this.nc;
    }

    /* access modifiers changed from: package-private */
    public float getNcb() {
        return this.ncb;
    }

    public float[] getRgbD() {
        return this.rgbD;
    }

    /* access modifiers changed from: package-private */
    public float getZ() {
        return this.z;
    }
}

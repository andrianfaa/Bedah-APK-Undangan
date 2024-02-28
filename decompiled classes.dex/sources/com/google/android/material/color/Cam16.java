package com.google.android.material.color;

final class Cam16 {
    static final float[][] CAM16RGB_TO_XYZ = {new float[]{1.8620678f, -1.0112547f, 0.14918678f}, new float[]{0.38752654f, 0.62144744f, -0.00897398f}, new float[]{-0.0158415f, -0.03412294f, 1.0499644f}};
    static final float[][] XYZ_TO_CAM16RGB = {new float[]{0.401288f, 0.650173f, -0.051461f}, new float[]{-0.250268f, 1.204414f, 0.045854f}, new float[]{-0.002079f, 0.048952f, 0.953127f}};
    private final float astar;
    private final float bstar;
    private final float chroma;
    private final float hue;
    private final float j;
    private final float jstar;
    private final float m;
    private final float q;
    private final float s;

    private Cam16(float hue2, float chroma2, float j2, float q2, float m2, float s2, float jstar2, float astar2, float bstar2) {
        this.hue = hue2;
        this.chroma = chroma2;
        this.j = j2;
        this.q = q2;
        this.m = m2;
        this.s = s2;
        this.jstar = jstar2;
        this.astar = astar2;
        this.bstar = bstar2;
    }

    public static Cam16 fromInt(int argb) {
        return fromIntInViewingConditions(argb, ViewingConditions.DEFAULT);
    }

    static Cam16 fromIntInViewingConditions(int argb, ViewingConditions viewingConditions) {
        int i = argb;
        int i2 = (16711680 & i) >> 16;
        int i3 = (65280 & i) >> 8;
        int i4 = i & 255;
        float linearized = ColorUtils.linearized(((float) i2) / 255.0f) * 100.0f;
        float linearized2 = ColorUtils.linearized(((float) i3) / 255.0f) * 100.0f;
        float linearized3 = ColorUtils.linearized(((float) i4) / 255.0f) * 100.0f;
        float f = (0.41233894f * linearized) + (0.35762063f * linearized2) + (0.18051042f * linearized3);
        float f2 = (0.2126f * linearized) + (0.7152f * linearized2) + (0.0722f * linearized3);
        float f3 = (0.01932141f * linearized) + (0.11916382f * linearized2) + (0.9503448f * linearized3);
        float[][] fArr = XYZ_TO_CAM16RGB;
        float f4 = (fArr[0][0] * f) + (fArr[0][1] * f2) + (fArr[0][2] * f3);
        float f5 = (fArr[1][0] * f) + (fArr[1][1] * f2) + (fArr[1][2] * f3);
        float f6 = (fArr[2][0] * f) + (fArr[2][1] * f2) + (fArr[2][2] * f3);
        float f7 = viewingConditions.getRgbD()[0] * f4;
        float f8 = viewingConditions.getRgbD()[1] * f5;
        float f9 = viewingConditions.getRgbD()[2] * f6;
        int i5 = i2;
        int i6 = i3;
        int i7 = i4;
        float pow = (float) Math.pow(((double) (viewingConditions.getFl() * Math.abs(f7))) / 100.0d, 0.42d);
        float f10 = linearized;
        float f11 = linearized3;
        float pow2 = (float) Math.pow(((double) (viewingConditions.getFl() * Math.abs(f8))) / 100.0d, 0.42d);
        float pow3 = (float) Math.pow(((double) (viewingConditions.getFl() * Math.abs(f9))) / 100.0d, 0.42d);
        float signum = ((Math.signum(f7) * 400.0f) * pow) / (pow + 27.13f);
        float signum2 = ((Math.signum(f8) * 400.0f) * pow2) / (pow2 + 27.13f);
        float signum3 = ((Math.signum(f9) * 400.0f) * pow3) / (27.13f + pow3);
        float f12 = pow;
        float f13 = pow2;
        int i8 = i6;
        float f14 = linearized2;
        float f15 = ((float) (((((double) signum) * 11.0d) + (((double) signum2) * -12.0d)) + ((double) signum3))) / 11.0f;
        float f16 = pow3;
        float f17 = ((float) (((double) (signum + signum2)) - (((double) signum3) * 2.0d))) / 9.0f;
        float f18 = (((signum * 20.0f) + (signum2 * 20.0f)) + (21.0f * signum3)) / 20.0f;
        float f19 = (((40.0f * signum) + (signum2 * 20.0f)) + signum3) / 20.0f;
        float f20 = signum;
        float f21 = signum3;
        float f22 = signum2;
        float atan2 = (float) Math.atan2((double) f17, (double) f15);
        float f23 = (atan2 * 180.0f) / 3.1415927f;
        float f24 = atan2;
        float f25 = f23 < 0.0f ? f23 + 360.0f : f23 >= 360.0f ? f23 - 360.0f : f23;
        float f26 = (f25 * 3.1415927f) / 180.0f;
        float nbb = viewingConditions.getNbb() * f19;
        float f27 = f23;
        float f28 = nbb;
        float f29 = f19;
        float f30 = f;
        float pow4 = ((float) Math.pow((double) (nbb / viewingConditions.getAw()), (double) (viewingConditions.getC() * viewingConditions.getZ()))) * 100.0f;
        float f31 = f2;
        float c = (4.0f / viewingConditions.getC()) * ((float) Math.sqrt((double) (pow4 / 100.0f))) * (viewingConditions.getAw() + 4.0f) * viewingConditions.getFlRoot();
        float f32 = ((double) f25) < 20.14d ? f25 + 360.0f : f25;
        float cos = ((float) (Math.cos(Math.toRadians((double) f32) + 2.0d) + 3.8d)) * 0.25f;
        float nc = 3846.1538f * cos * viewingConditions.getNc() * viewingConditions.getNcb();
        float f33 = f3;
        float[][] fArr2 = fArr;
        float f34 = f7;
        float f35 = f4;
        float hypot = (((float) Math.hypot((double) f15, (double) f17)) * nc) / (0.305f + f18);
        float f36 = f15;
        float f37 = f17;
        float f38 = f32;
        float f39 = f18;
        float pow5 = ((float) Math.pow(1.64d - Math.pow(0.29d, (double) viewingConditions.getN()), 0.73d)) * ((float) Math.pow((double) hypot, 0.9d));
        float sqrt = ((float) Math.sqrt(((double) pow4) / 100.0d)) * pow5;
        float flRoot = viewingConditions.getFlRoot() * sqrt;
        float f40 = (1.7f * pow4) / ((0.007f * pow4) + 1.0f);
        float f41 = pow5;
        float f42 = f39;
        float log1p = ((float) Math.log1p((double) (flRoot * 0.0228f))) * 43.85965f;
        float f43 = cos;
        float f44 = nc;
        float f45 = hypot;
        return new Cam16(f25, sqrt, pow4, c, flRoot, ((float) Math.sqrt((double) ((viewingConditions.getC() * pow5) / (viewingConditions.getAw() + 4.0f)))) * 50.0f, f40, ((float) Math.cos((double) f26)) * log1p, ((float) Math.sin((double) f26)) * log1p);
    }

    static Cam16 fromJch(float j2, float c, float h) {
        return fromJchInViewingConditions(j2, c, h, ViewingConditions.DEFAULT);
    }

    private static Cam16 fromJchInViewingConditions(float j2, float c, float h, ViewingConditions viewingConditions) {
        float f = j2;
        float c2 = (4.0f / viewingConditions.getC()) * ((float) Math.sqrt(((double) f) / 100.0d)) * (viewingConditions.getAw() + 4.0f) * viewingConditions.getFlRoot();
        float flRoot = c * viewingConditions.getFlRoot();
        float sqrt = ((float) Math.sqrt((double) ((viewingConditions.getC() * (c / ((float) Math.sqrt(((double) f) / 100.0d)))) / (viewingConditions.getAw() + 4.0f)))) * 50.0f;
        float f2 = (3.1415927f * h) / 180.0f;
        float f3 = (1.7f * f) / ((0.007f * f) + 1.0f);
        float log1p = ((float) Math.log1p(((double) flRoot) * 0.0228d)) * 43.85965f;
        return new Cam16(h, c, j2, c2, flRoot, sqrt, f3, log1p * ((float) Math.cos((double) f2)), log1p * ((float) Math.sin((double) f2)));
    }

    public static Cam16 fromUcs(float jstar2, float astar2, float bstar2) {
        return fromUcsInViewingConditions(jstar2, astar2, bstar2, ViewingConditions.DEFAULT);
    }

    public static Cam16 fromUcsInViewingConditions(float jstar2, float astar2, float bstar2, ViewingConditions viewingConditions) {
        double expm1 = (Math.expm1(Math.hypot((double) astar2, (double) bstar2) * 0.02280000038444996d) / 0.02280000038444996d) / ((double) viewingConditions.getFlRoot());
        double atan2 = Math.atan2((double) bstar2, (double) astar2) * 57.29577951308232d;
        if (atan2 < 0.0d) {
            atan2 += 360.0d;
        }
        return fromJchInViewingConditions(jstar2 / (1.0f - ((jstar2 - 100.0f) * 0.007f)), (float) expm1, (float) atan2, viewingConditions);
    }

    /* access modifiers changed from: package-private */
    public float distance(Cam16 other) {
        float jStar = getJStar() - other.getJStar();
        float aStar = getAStar() - other.getAStar();
        float bStar = getBStar() - other.getBStar();
        return (float) (Math.pow(Math.sqrt((double) ((jStar * jStar) + (aStar * aStar) + (bStar * bStar))), 0.63d) * 1.41d);
    }

    public float getAStar() {
        return this.astar;
    }

    public float getBStar() {
        return this.bstar;
    }

    public float getChroma() {
        return this.chroma;
    }

    public float getHue() {
        return this.hue;
    }

    public int getInt() {
        return viewed(ViewingConditions.DEFAULT);
    }

    public float getJ() {
        return this.j;
    }

    public float getJStar() {
        return this.jstar;
    }

    public float getM() {
        return this.m;
    }

    public float getQ() {
        return this.q;
    }

    public float getS() {
        return this.s;
    }

    /* access modifiers changed from: package-private */
    public int viewed(ViewingConditions viewingConditions) {
        float chroma2 = (((double) getChroma()) == 0.0d || ((double) getJ()) == 0.0d) ? 0.0f : getChroma() / ((float) Math.sqrt(((double) getJ()) / 100.0d));
        float pow = (float) Math.pow(((double) chroma2) / Math.pow(1.64d - Math.pow(0.29d, (double) viewingConditions.getN()), 0.73d), 1.1111111111111112d);
        float hue2 = (getHue() * 3.1415927f) / 180.0f;
        float cos = ((float) (Math.cos(((double) hue2) + 2.0d) + 3.8d)) * 0.25f;
        float aw = viewingConditions.getAw() * ((float) Math.pow(((double) getJ()) / 100.0d, (1.0d / ((double) viewingConditions.getC())) / ((double) viewingConditions.getZ())));
        float nc = 3846.1538f * cos * viewingConditions.getNc() * viewingConditions.getNcb();
        float nbb = aw / viewingConditions.getNbb();
        float sin = (float) Math.sin((double) hue2);
        float cos2 = (float) Math.cos((double) hue2);
        float f = (((0.305f + nbb) * 23.0f) * pow) / (((23.0f * nc) + ((11.0f * pow) * cos2)) + ((108.0f * pow) * sin));
        float f2 = f * cos2;
        float f3 = f * sin;
        float f4 = (((nbb * 460.0f) + (451.0f * f2)) + (288.0f * f3)) / 1403.0f;
        float f5 = (((nbb * 460.0f) - (891.0f * f2)) - (261.0f * f3)) / 1403.0f;
        float f6 = (((460.0f * nbb) - (220.0f * f2)) - (6300.0f * f3)) / 1403.0f;
        float f7 = chroma2;
        float f8 = pow;
        float max = (float) Math.max(0.0d, (((double) Math.abs(f4)) * 27.13d) / (400.0d - ((double) Math.abs(f4))));
        float f9 = nbb;
        float f10 = hue2;
        float signum = Math.signum(f4) * (100.0f / viewingConditions.getFl()) * ((float) Math.pow((double) max, 2.380952380952381d));
        float max2 = (float) Math.max(0.0d, (((double) Math.abs(f5)) * 27.13d) / (400.0d - ((double) Math.abs(f5))));
        float f11 = nc;
        float f12 = max2;
        float signum2 = Math.signum(f5) * (100.0f / viewingConditions.getFl()) * ((float) Math.pow((double) max2, 2.380952380952381d));
        float f13 = aw;
        float f14 = cos;
        float signum3 = Math.signum(f6) * (100.0f / viewingConditions.getFl()) * ((float) Math.pow((double) ((float) Math.max(0.0d, (((double) Math.abs(f6)) * 27.13d) / (400.0d - ((double) Math.abs(f6))))), 2.380952380952381d));
        float f15 = signum / viewingConditions.getRgbD()[0];
        float f16 = signum2 / viewingConditions.getRgbD()[1];
        float f17 = signum3 / viewingConditions.getRgbD()[2];
        float[][] fArr = CAM16RGB_TO_XYZ;
        float f18 = max;
        return ColorUtils.intFromXyzComponents((fArr[0][0] * f15) + (fArr[0][1] * f16) + (fArr[0][2] * f17), (fArr[1][0] * f15) + (fArr[1][1] * f16) + (fArr[1][2] * f17), (fArr[2][0] * f15) + (fArr[2][1] * f16) + (fArr[2][2] * f17));
    }
}

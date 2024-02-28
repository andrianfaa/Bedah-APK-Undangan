package androidx.core.content.res;

import androidx.core.graphics.ColorUtils;

class CamColor {
    private static final float CHROMA_SEARCH_ENDPOINT = 0.4f;
    private static final float DE_MAX = 1.0f;
    private static final float DL_MAX = 0.2f;
    private static final float LIGHTNESS_SEARCH_ENDPOINT = 0.01f;
    private final float mAstar;
    private final float mBstar;
    private final float mChroma;
    private final float mHue;
    private final float mJ;
    private final float mJstar;
    private final float mM;
    private final float mQ;
    private final float mS;

    CamColor(float hue, float chroma, float j, float q, float m, float s, float jStar, float aStar, float bStar) {
        this.mHue = hue;
        this.mChroma = chroma;
        this.mJ = j;
        this.mQ = q;
        this.mM = m;
        this.mS = s;
        this.mJstar = jStar;
        this.mAstar = aStar;
        this.mBstar = bStar;
    }

    private static CamColor findCamByJ(float hue, float chroma, float lstar) {
        float f = 0.0f;
        float f2 = 100.0f;
        float f3 = 1000.0f;
        float f4 = 1000.0f;
        CamColor camColor = null;
        while (Math.abs(f - f2) > LIGHTNESS_SEARCH_ENDPOINT) {
            float f5 = f + ((f2 - f) / 2.0f);
            int viewedInSrgb = fromJch(f5, chroma, hue).viewedInSrgb();
            float lStarFromInt = CamUtils.lStarFromInt(viewedInSrgb);
            float abs = Math.abs(lstar - lStarFromInt);
            if (abs < 0.2f) {
                CamColor fromColor = fromColor(viewedInSrgb);
                float distance = fromColor.distance(fromJch(fromColor.getJ(), fromColor.getChroma(), hue));
                if (distance <= 1.0f) {
                    f3 = abs;
                    f4 = distance;
                    camColor = fromColor;
                }
            }
            if (f3 == 0.0f && f4 == 0.0f) {
                break;
            } else if (lStarFromInt < lstar) {
                f = f5;
            } else {
                f2 = f5;
            }
        }
        return camColor;
    }

    static CamColor fromColor(int color) {
        return fromColorInViewingConditions(color, ViewingConditions.DEFAULT);
    }

    static CamColor fromColorInViewingConditions(int color, ViewingConditions viewingConditions) {
        float[] xyzFromInt = CamUtils.xyzFromInt(color);
        float[][] fArr = CamUtils.XYZ_TO_CAM16RGB;
        float f = (xyzFromInt[0] * fArr[0][0]) + (xyzFromInt[1] * fArr[0][1]) + (xyzFromInt[2] * fArr[0][2]);
        float f2 = (xyzFromInt[0] * fArr[1][0]) + (xyzFromInt[1] * fArr[1][1]) + (xyzFromInt[2] * fArr[1][2]);
        float f3 = (xyzFromInt[0] * fArr[2][0]) + (xyzFromInt[1] * fArr[2][1]) + (xyzFromInt[2] * fArr[2][2]);
        float f4 = viewingConditions.getRgbD()[0] * f;
        float f5 = viewingConditions.getRgbD()[1] * f2;
        float f6 = viewingConditions.getRgbD()[2] * f3;
        float pow = (float) Math.pow(((double) (viewingConditions.getFl() * Math.abs(f4))) / 100.0d, 0.42d);
        float pow2 = (float) Math.pow(((double) (viewingConditions.getFl() * Math.abs(f5))) / 100.0d, 0.42d);
        float pow3 = (float) Math.pow(((double) (viewingConditions.getFl() * Math.abs(f6))) / 100.0d, 0.42d);
        float signum = ((Math.signum(f4) * 400.0f) * pow) / (pow + 27.13f);
        float signum2 = ((Math.signum(f5) * 400.0f) * pow2) / (pow2 + 27.13f);
        float signum3 = ((Math.signum(f6) * 400.0f) * pow3) / (27.13f + pow3);
        float[] fArr2 = xyzFromInt;
        float[][] fArr3 = fArr;
        float f7 = ((float) (((((double) signum) * 11.0d) + (((double) signum2) * -12.0d)) + ((double) signum3))) / 11.0f;
        float f8 = f4;
        float f9 = ((float) (((double) (signum + signum2)) - (((double) signum3) * 2.0d))) / 9.0f;
        float f10 = (((signum * 20.0f) + (signum2 * 20.0f)) + (21.0f * signum3)) / 20.0f;
        float f11 = (((40.0f * signum) + (signum2 * 20.0f)) + signum3) / 20.0f;
        float f12 = f;
        float f13 = f5;
        float f14 = f6;
        float atan2 = (float) Math.atan2((double) f9, (double) f7);
        float f15 = (atan2 * 180.0f) / 3.1415927f;
        float f16 = atan2;
        float f17 = f15 < 0.0f ? f15 + 360.0f : f15 >= 360.0f ? f15 - 360.0f : f15;
        float f18 = (f17 * 3.1415927f) / 180.0f;
        float nbb = viewingConditions.getNbb() * f11;
        float f19 = f15;
        float f20 = nbb;
        float f21 = f2;
        float f22 = f3;
        float pow4 = ((float) Math.pow((double) (nbb / viewingConditions.getAw()), (double) (viewingConditions.getC() * viewingConditions.getZ()))) * 100.0f;
        float f23 = pow;
        float c = (4.0f / viewingConditions.getC()) * ((float) Math.sqrt((double) (pow4 / 100.0f))) * (viewingConditions.getAw() + 4.0f) * viewingConditions.getFlRoot();
        float cos = ((float) (Math.cos(((((double) (((double) f17) < 20.14d ? f17 + 360.0f : f17)) * 3.141592653589793d) / 180.0d) + 2.0d) + 3.8d)) * 0.25f;
        float nc = 3846.1538f * cos * viewingConditions.getNc() * viewingConditions.getNcb();
        float f24 = f7;
        float f25 = f9;
        float sqrt = (((float) Math.sqrt((double) ((f7 * f7) + (f9 * f9)))) * nc) / (0.305f + f10);
        float f26 = nc;
        float f27 = cos;
        float f28 = pow2;
        float f29 = signum2;
        float pow5 = ((float) Math.pow(1.64d - Math.pow(0.29d, (double) viewingConditions.getN()), 0.73d)) * ((float) Math.pow((double) sqrt, 0.9d));
        float sqrt2 = ((float) Math.sqrt(((double) pow4) / 100.0d)) * pow5;
        float flRoot = viewingConditions.getFlRoot() * sqrt2;
        float f30 = (1.7f * pow4) / ((0.007f * pow4) + 1.0f);
        float f31 = sqrt;
        float f32 = pow5;
        float log = ((float) Math.log((double) ((0.0228f * flRoot) + 1.0f))) * 43.85965f;
        float f33 = signum3;
        float f34 = pow3;
        float f35 = signum;
        return new CamColor(f17, sqrt2, pow4, c, flRoot, ((float) Math.sqrt((double) ((viewingConditions.getC() * pow5) / (viewingConditions.getAw() + 4.0f)))) * 50.0f, f30, ((float) Math.cos((double) f18)) * log, ((float) Math.sin((double) f18)) * log);
    }

    private static CamColor fromJch(float j, float c, float h) {
        return fromJchInFrame(j, c, h, ViewingConditions.DEFAULT);
    }

    private static CamColor fromJchInFrame(float j, float c, float h, ViewingConditions viewingConditions) {
        float f = j;
        float c2 = (4.0f / viewingConditions.getC()) * ((float) Math.sqrt(((double) f) / 100.0d)) * (viewingConditions.getAw() + 4.0f) * viewingConditions.getFlRoot();
        float flRoot = c * viewingConditions.getFlRoot();
        float sqrt = ((float) Math.sqrt((double) ((viewingConditions.getC() * (c / ((float) Math.sqrt(((double) f) / 100.0d)))) / (viewingConditions.getAw() + 4.0f)))) * 50.0f;
        float f2 = (3.1415927f * h) / 180.0f;
        float f3 = (1.7f * f) / ((0.007f * f) + 1.0f);
        float log = ((float) Math.log((((double) flRoot) * 0.0228d) + 1.0d)) * 43.85965f;
        return new CamColor(h, c, j, c2, flRoot, sqrt, f3, log * ((float) Math.cos((double) f2)), log * ((float) Math.sin((double) f2)));
    }

    static int toColor(float hue, float chroma, float lStar) {
        return toColor(hue, chroma, lStar, ViewingConditions.DEFAULT);
    }

    static int toColor(float hue, float chroma, float lstar, ViewingConditions viewingConditions) {
        if (((double) chroma) < 1.0d || ((double) Math.round(lstar)) <= 0.0d || ((double) Math.round(lstar)) >= 100.0d) {
            return CamUtils.intFromLStar(lstar);
        }
        float f = 0.0f;
        if (hue >= 0.0f) {
            f = Math.min(360.0f, hue);
        }
        float hue2 = f;
        float f2 = chroma;
        float f3 = chroma;
        float f4 = 0.0f;
        boolean z = true;
        CamColor camColor = null;
        while (Math.abs(f4 - f2) >= CHROMA_SEARCH_ENDPOINT) {
            CamColor findCamByJ = findCamByJ(hue2, f3, lstar);
            if (!z) {
                if (findCamByJ == null) {
                    f2 = f3;
                } else {
                    camColor = findCamByJ;
                    f4 = f3;
                }
                f3 = f4 + ((f2 - f4) / 2.0f);
            } else if (findCamByJ != null) {
                return findCamByJ.viewed(viewingConditions);
            } else {
                z = false;
                f3 = f4 + ((f2 - f4) / 2.0f);
            }
        }
        return camColor == null ? CamUtils.intFromLStar(lstar) : camColor.viewed(viewingConditions);
    }

    /* access modifiers changed from: package-private */
    public float distance(CamColor other) {
        float jStar = getJStar() - other.getJStar();
        float aStar = getAStar() - other.getAStar();
        float bStar = getBStar() - other.getBStar();
        return (float) (Math.pow(Math.sqrt((double) ((jStar * jStar) + (aStar * aStar) + (bStar * bStar))), 0.63d) * 1.41d);
    }

    /* access modifiers changed from: package-private */
    public float getAStar() {
        return this.mAstar;
    }

    /* access modifiers changed from: package-private */
    public float getBStar() {
        return this.mBstar;
    }

    /* access modifiers changed from: package-private */
    public float getChroma() {
        return this.mChroma;
    }

    /* access modifiers changed from: package-private */
    public float getHue() {
        return this.mHue;
    }

    /* access modifiers changed from: package-private */
    public float getJ() {
        return this.mJ;
    }

    /* access modifiers changed from: package-private */
    public float getJStar() {
        return this.mJstar;
    }

    /* access modifiers changed from: package-private */
    public float getM() {
        return this.mM;
    }

    /* access modifiers changed from: package-private */
    public float getQ() {
        return this.mQ;
    }

    /* access modifiers changed from: package-private */
    public float getS() {
        return this.mS;
    }

    /* access modifiers changed from: package-private */
    public int viewed(ViewingConditions viewingConditions) {
        float chroma = (((double) getChroma()) == 0.0d || ((double) getJ()) == 0.0d) ? 0.0f : getChroma() / ((float) Math.sqrt(((double) getJ()) / 100.0d));
        float pow = (float) Math.pow(((double) chroma) / Math.pow(1.64d - Math.pow(0.29d, (double) viewingConditions.getN()), 0.73d), 1.1111111111111112d);
        float hue = (getHue() * 3.1415927f) / 180.0f;
        float cos = ((float) (Math.cos(((double) hue) + 2.0d) + 3.8d)) * 0.25f;
        float aw = viewingConditions.getAw() * ((float) Math.pow(((double) getJ()) / 100.0d, (1.0d / ((double) viewingConditions.getC())) / ((double) viewingConditions.getZ())));
        float nc = 3846.1538f * cos * viewingConditions.getNc() * viewingConditions.getNcb();
        float nbb = aw / viewingConditions.getNbb();
        float sin = (float) Math.sin((double) hue);
        float cos2 = (float) Math.cos((double) hue);
        float f = (((0.305f + nbb) * 23.0f) * pow) / (((23.0f * nc) + ((11.0f * pow) * cos2)) + ((108.0f * pow) * sin));
        float f2 = f * cos2;
        float f3 = f * sin;
        float f4 = (((nbb * 460.0f) + (451.0f * f2)) + (288.0f * f3)) / 1403.0f;
        float f5 = (((nbb * 460.0f) - (891.0f * f2)) - (261.0f * f3)) / 1403.0f;
        float f6 = (((460.0f * nbb) - (220.0f * f2)) - (6300.0f * f3)) / 1403.0f;
        float f7 = chroma;
        float f8 = pow;
        float max = (float) Math.max(0.0d, (((double) Math.abs(f4)) * 27.13d) / (400.0d - ((double) Math.abs(f4))));
        float f9 = nbb;
        float f10 = hue;
        float signum = Math.signum(f4) * (100.0f / viewingConditions.getFl()) * ((float) Math.pow((double) max, 2.380952380952381d));
        float max2 = (float) Math.max(0.0d, (((double) Math.abs(f5)) * 27.13d) / (400.0d - ((double) Math.abs(f5))));
        float f11 = nc;
        float f12 = max2;
        float signum2 = Math.signum(f5) * (100.0f / viewingConditions.getFl()) * ((float) Math.pow((double) max2, 2.380952380952381d));
        float max3 = (float) Math.max(0.0d, (((double) Math.abs(f6)) * 27.13d) / (400.0d - ((double) Math.abs(f6))));
        float f13 = aw;
        float f14 = cos;
        float signum3 = Math.signum(f6) * (100.0f / viewingConditions.getFl()) * ((float) Math.pow((double) max3, 2.380952380952381d));
        float f15 = signum / viewingConditions.getRgbD()[0];
        float f16 = signum2 / viewingConditions.getRgbD()[1];
        float f17 = signum3 / viewingConditions.getRgbD()[2];
        float[][] fArr = CamUtils.CAM16RGB_TO_XYZ;
        float f18 = (fArr[0][0] * f15) + (fArr[0][1] * f16) + (fArr[0][2] * f17);
        float f19 = max;
        float f20 = signum;
        float f21 = max3;
        float f22 = signum3;
        float f23 = signum2;
        float f24 = f15;
        float f25 = f18;
        return ColorUtils.XYZToColor((double) f18, (double) ((fArr[1][0] * f15) + (fArr[1][1] * f16) + (fArr[1][2] * f17)), (double) ((fArr[2][0] * f15) + (fArr[2][1] * f16) + (fArr[2][2] * f17)));
    }

    /* access modifiers changed from: package-private */
    public int viewedInSrgb() {
        return viewed(ViewingConditions.DEFAULT);
    }
}

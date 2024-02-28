package com.google.android.material.color;

final class Blend {
    private static final float HARMONIZE_MAX_DEGREES = 15.0f;
    private static final float HARMONIZE_PERCENTAGE = 0.5f;

    private Blend() {
    }

    public static int blendCam16Ucs(int from, int to, float amount) {
        Cam16 fromInt = Cam16.fromInt(from);
        Cam16 fromInt2 = Cam16.fromInt(to);
        float jStar = fromInt.getJStar();
        float aStar = fromInt.getAStar();
        float bStar = fromInt.getBStar();
        return Cam16.fromUcs(((fromInt2.getJStar() - jStar) * amount) + jStar, ((fromInt2.getAStar() - aStar) * amount) + aStar, ((fromInt2.getBStar() - bStar) * amount) + bStar).getInt();
    }

    public static int blendHctHue(int from, int to, float amount) {
        return Hct.from(Cam16.fromInt(blendCam16Ucs(from, to, amount)).getHue(), Cam16.fromInt(from).getChroma(), ColorUtils.lstarFromInt(from)).toInt();
    }

    public static int harmonize(int designColor, int sourceColor) {
        Hct fromInt = Hct.fromInt(designColor);
        Hct fromInt2 = Hct.fromInt(sourceColor);
        return Hct.from(MathUtils.sanitizeDegrees(fromInt.getHue() + (rotationDirection(fromInt.getHue(), fromInt2.getHue()) * Math.min(0.5f * MathUtils.differenceDegrees(fromInt.getHue(), fromInt2.getHue()), HARMONIZE_MAX_DEGREES))), fromInt.getChroma(), fromInt.getTone()).toInt();
    }

    private static float rotationDirection(float from, float to) {
        float f = to - from;
        float f2 = (to - from) + 360.0f;
        float f3 = (to - from) - 360.0f;
        float abs = Math.abs(f);
        float abs2 = Math.abs(f2);
        float abs3 = Math.abs(f3);
        return (abs > abs2 || abs > abs3) ? (abs2 > abs || abs2 > abs3) ? ((double) f3) >= 0.0d ? 1.0f : -1.0f : ((double) f2) >= 0.0d ? 1.0f : -1.0f : ((double) f) >= 0.0d ? 1.0f : -1.0f;
    }
}

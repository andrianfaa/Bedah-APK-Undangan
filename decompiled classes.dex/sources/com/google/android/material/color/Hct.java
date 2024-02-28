package com.google.android.material.color;

final class Hct {
    private static final float CHROMA_SEARCH_ENDPOINT = 0.4f;
    private static final float DE_MAX = 1.0f;
    private static final float DE_MAX_ERROR = 1.0E-9f;
    private static final float DL_MAX = 0.2f;
    private static final float LIGHTNESS_SEARCH_ENDPOINT = 0.01f;
    private float chroma;
    private float hue;
    private float tone;

    private Hct(float hue2, float chroma2, float tone2) {
        setInternalState(gamutMap(hue2, chroma2, tone2));
    }

    private static Cam16 findCamByJ(float hue2, float chroma2, float tone2) {
        float f = 0.0f;
        float f2 = 100.0f;
        float f3 = 1000.0f;
        float f4 = 1000.0f;
        Cam16 cam16 = null;
        while (Math.abs(f - f2) > LIGHTNESS_SEARCH_ENDPOINT) {
            float f5 = f + ((f2 - f) / 2.0f);
            int i = Cam16.fromJch(f5, chroma2, hue2).getInt();
            float lstarFromInt = ColorUtils.lstarFromInt(i);
            float abs = Math.abs(tone2 - lstarFromInt);
            if (abs < 0.2f) {
                Cam16 fromInt = Cam16.fromInt(i);
                float distance = fromInt.distance(Cam16.fromJch(fromInt.getJ(), fromInt.getChroma(), hue2));
                if (distance <= 1.0f && distance <= f4) {
                    f3 = abs;
                    f4 = distance;
                    cam16 = fromInt;
                }
            }
            if (f3 == 0.0f && f4 < DE_MAX_ERROR) {
                break;
            } else if (lstarFromInt < tone2) {
                f = f5;
            } else {
                f2 = f5;
            }
        }
        return cam16;
    }

    public static Hct from(float hue2, float chroma2, float tone2) {
        return new Hct(hue2, chroma2, tone2);
    }

    public static Hct fromInt(int argb) {
        Cam16 fromInt = Cam16.fromInt(argb);
        return new Hct(fromInt.getHue(), fromInt.getChroma(), ColorUtils.lstarFromInt(argb));
    }

    private static int gamutMap(float hue2, float chroma2, float tone2) {
        return gamutMapInViewingConditions(hue2, chroma2, tone2, ViewingConditions.DEFAULT);
    }

    static int gamutMapInViewingConditions(float hue2, float chroma2, float tone2, ViewingConditions viewingConditions) {
        if (((double) chroma2) < 1.0d || ((double) Math.round(tone2)) <= 0.0d || ((double) Math.round(tone2)) >= 100.0d) {
            return ColorUtils.intFromLstar(tone2);
        }
        float hue3 = MathUtils.sanitizeDegrees(hue2);
        float f = chroma2;
        float f2 = chroma2;
        float f3 = 0.0f;
        boolean z = true;
        Cam16 cam16 = null;
        while (Math.abs(f3 - f) >= CHROMA_SEARCH_ENDPOINT) {
            Cam16 findCamByJ = findCamByJ(hue3, f2, tone2);
            if (!z) {
                if (findCamByJ == null) {
                    f = f2;
                } else {
                    cam16 = findCamByJ;
                    f3 = f2;
                }
                f2 = f3 + ((f - f3) / 2.0f);
            } else if (findCamByJ != null) {
                return findCamByJ.viewed(viewingConditions);
            } else {
                z = false;
                f2 = f3 + ((f - f3) / 2.0f);
            }
        }
        return cam16 == null ? ColorUtils.intFromLstar(tone2) : cam16.viewed(viewingConditions);
    }

    private void setInternalState(int argb) {
        Cam16 fromInt = Cam16.fromInt(argb);
        float lstarFromInt = ColorUtils.lstarFromInt(argb);
        this.hue = fromInt.getHue();
        this.chroma = fromInt.getChroma();
        this.tone = lstarFromInt;
    }

    public float getChroma() {
        return this.chroma;
    }

    public float getHue() {
        return this.hue;
    }

    public float getTone() {
        return this.tone;
    }

    public void setChroma(float newChroma) {
        setInternalState(gamutMap(this.hue, newChroma, this.tone));
    }

    public void setHue(float newHue) {
        setInternalState(gamutMap(MathUtils.sanitizeDegrees(newHue), this.chroma, this.tone));
    }

    public void setTone(float newTone) {
        setInternalState(gamutMap(this.hue, this.chroma, newTone));
    }

    public int toInt() {
        return gamutMap(this.hue, this.chroma, this.tone);
    }
}

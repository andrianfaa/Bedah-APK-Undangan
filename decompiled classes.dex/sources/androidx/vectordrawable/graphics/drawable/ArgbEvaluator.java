package androidx.vectordrawable.graphics.drawable;

import android.animation.TypeEvaluator;

public class ArgbEvaluator implements TypeEvaluator {
    private static final ArgbEvaluator sInstance = new ArgbEvaluator();

    public static ArgbEvaluator getInstance() {
        return sInstance;
    }

    public Object evaluate(float fraction, Object startValue, Object endValue) {
        int intValue = ((Integer) startValue).intValue();
        float f = ((float) ((intValue >> 24) & 255)) / 255.0f;
        int intValue2 = ((Integer) endValue).intValue();
        float pow = (float) Math.pow((double) (((float) ((intValue >> 16) & 255)) / 255.0f), 2.2d);
        float pow2 = (float) Math.pow((double) (((float) ((intValue >> 8) & 255)) / 255.0f), 2.2d);
        float pow3 = (float) Math.pow((double) (((float) (intValue & 255)) / 255.0f), 2.2d);
        float f2 = pow;
        int i = intValue;
        float f3 = f;
        float f4 = pow2;
        float f5 = pow3;
        return Integer.valueOf((Math.round(((((((float) ((intValue2 >> 24) & 255)) / 255.0f) - f) * fraction) + f) * 255.0f) << 24) | (Math.round(((float) Math.pow((double) (((((float) Math.pow((double) (((float) ((intValue2 >> 16) & 255)) / 255.0f), 2.2d)) - pow) * fraction) + pow), 0.45454545454545453d)) * 255.0f) << 16) | (Math.round(((float) Math.pow((double) (((((float) Math.pow((double) (((float) ((intValue2 >> 8) & 255)) / 255.0f), 2.2d)) - pow2) * fraction) + pow2), 0.45454545454545453d)) * 255.0f) << 8) | Math.round(((float) Math.pow((double) (((((float) Math.pow((double) (((float) (intValue2 & 255)) / 255.0f), 2.2d)) - pow3) * fraction) + pow3), 0.45454545454545453d)) * 255.0f));
    }
}

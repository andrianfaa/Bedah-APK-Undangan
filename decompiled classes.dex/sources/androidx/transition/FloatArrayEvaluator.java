package androidx.transition;

import android.animation.TypeEvaluator;

class FloatArrayEvaluator implements TypeEvaluator<float[]> {
    private float[] mArray;

    FloatArrayEvaluator(float[] reuseArray) {
        this.mArray = reuseArray;
    }

    public float[] evaluate(float fraction, float[] startValue, float[] endValue) {
        float[] fArr = this.mArray;
        if (fArr == null) {
            fArr = new float[startValue.length];
        }
        for (int i = 0; i < fArr.length; i++) {
            float f = startValue[i];
            fArr[i] = ((endValue[i] - f) * fraction) + f;
        }
        return fArr;
    }
}

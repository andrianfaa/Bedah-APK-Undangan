package androidx.transition;

import android.animation.TypeEvaluator;
import android.graphics.Rect;

class RectEvaluator implements TypeEvaluator<Rect> {
    private Rect mRect;

    RectEvaluator() {
    }

    RectEvaluator(Rect reuseRect) {
        this.mRect = reuseRect;
    }

    public Rect evaluate(float fraction, Rect startValue, Rect endValue) {
        int i = startValue.left + ((int) (((float) (endValue.left - startValue.left)) * fraction));
        int i2 = startValue.top + ((int) (((float) (endValue.top - startValue.top)) * fraction));
        int i3 = startValue.right + ((int) (((float) (endValue.right - startValue.right)) * fraction));
        int i4 = startValue.bottom + ((int) (((float) (endValue.bottom - startValue.bottom)) * fraction));
        Rect rect = this.mRect;
        if (rect == null) {
            return new Rect(i, i2, i3, i4);
        }
        rect.set(i, i2, i3, i4);
        return this.mRect;
    }
}

package androidx.transition;

import android.graphics.Rect;
import android.view.ViewGroup;

public class CircularPropagation extends VisibilityPropagation {
    private float mPropagationSpeed = 3.0f;

    private static float distance(float x1, float y1, float x2, float y2) {
        float f = x2 - x1;
        float f2 = y2 - y1;
        return (float) Math.sqrt((double) ((f * f) + (f2 * f2)));
    }

    public long getStartDelay(ViewGroup sceneRoot, Transition transition, TransitionValues startValues, TransitionValues endValues) {
        TransitionValues transitionValues;
        int i;
        int i2;
        TransitionValues transitionValues2 = startValues;
        if (transitionValues2 == null && endValues == null) {
            return 0;
        }
        int i3 = 1;
        if (endValues == null || getViewVisibility(transitionValues2) == 0) {
            transitionValues = startValues;
            i3 = -1;
        } else {
            transitionValues = endValues;
        }
        int viewX = getViewX(transitionValues);
        int viewY = getViewY(transitionValues);
        Rect epicenter = transition.getEpicenter();
        if (epicenter != null) {
            i2 = epicenter.centerX();
            i = epicenter.centerY();
            ViewGroup viewGroup = sceneRoot;
        } else {
            int[] iArr = new int[2];
            sceneRoot.getLocationOnScreen(iArr);
            int round = Math.round(((float) (iArr[0] + (sceneRoot.getWidth() / 2))) + sceneRoot.getTranslationX());
            i = Math.round(((float) (iArr[1] + (sceneRoot.getHeight() / 2))) + sceneRoot.getTranslationY());
            i2 = round;
        }
        float distance = distance((float) viewX, (float) viewY, (float) i2, (float) i) / distance(0.0f, 0.0f, (float) sceneRoot.getWidth(), (float) sceneRoot.getHeight());
        long duration = transition.getDuration();
        if (duration < 0) {
            duration = 300;
        }
        return (long) Math.round((((float) (((long) i3) * duration)) / this.mPropagationSpeed) * distance);
    }

    public void setPropagationSpeed(float propagationSpeed) {
        if (propagationSpeed != 0.0f) {
            this.mPropagationSpeed = propagationSpeed;
            return;
        }
        throw new IllegalArgumentException("propagationSpeed may not be 0");
    }
}

package androidx.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class Explode extends Visibility {
    private static final String PROPNAME_SCREEN_BOUNDS = "android:explode:screenBounds";
    private static final TimeInterpolator sAccelerate = new AccelerateInterpolator();
    private static final TimeInterpolator sDecelerate = new DecelerateInterpolator();
    private int[] mTempLoc = new int[2];

    public Explode() {
        setPropagation(new CircularPropagation());
    }

    public Explode(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPropagation(new CircularPropagation());
    }

    private static float calculateDistance(float x, float y) {
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    private static float calculateMaxDistance(View sceneRoot, int focalX, int focalY) {
        return calculateDistance((float) Math.max(focalX, sceneRoot.getWidth() - focalX), (float) Math.max(focalY, sceneRoot.getHeight() - focalY));
    }

    private void calculateOut(View sceneRoot, Rect bounds, int[] outVector) {
        int i;
        int i2;
        View view = sceneRoot;
        view.getLocationOnScreen(this.mTempLoc);
        int[] iArr = this.mTempLoc;
        int i3 = iArr[0];
        int i4 = iArr[1];
        Rect epicenter = getEpicenter();
        if (epicenter == null) {
            i2 = (sceneRoot.getWidth() / 2) + i3 + Math.round(sceneRoot.getTranslationX());
            i = (sceneRoot.getHeight() / 2) + i4 + Math.round(sceneRoot.getTranslationY());
        } else {
            i2 = epicenter.centerX();
            i = epicenter.centerY();
        }
        float centerX = (float) (bounds.centerX() - i2);
        float centerY = (float) (bounds.centerY() - i);
        if (centerX == 0.0f && centerY == 0.0f) {
            centerX = ((float) (Math.random() * 2.0d)) - 1.0f;
            Rect rect = epicenter;
            centerY = ((float) (Math.random() * 2.0d)) - 1.0f;
        } else {
            Rect rect2 = epicenter;
        }
        float calculateDistance = calculateDistance(centerX, centerY);
        float calculateMaxDistance = calculateMaxDistance(view, i2 - i3, i - i4);
        outVector[0] = Math.round(calculateMaxDistance * (centerX / calculateDistance));
        outVector[1] = Math.round(calculateMaxDistance * (centerY / calculateDistance));
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        view.getLocationOnScreen(this.mTempLoc);
        int[] iArr = this.mTempLoc;
        int i = iArr[0];
        int i2 = iArr[1];
        transitionValues.values.put(PROPNAME_SCREEN_BOUNDS, new Rect(i, i2, view.getWidth() + i, view.getHeight() + i2));
    }

    public void captureEndValues(TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        captureValues(transitionValues);
    }

    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        captureValues(transitionValues);
    }

    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        TransitionValues transitionValues = endValues;
        if (transitionValues == null) {
            return null;
        }
        Rect rect = (Rect) transitionValues.values.get(PROPNAME_SCREEN_BOUNDS);
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        calculateOut(sceneRoot, rect, this.mTempLoc);
        int[] iArr = this.mTempLoc;
        float f = translationX + ((float) iArr[0]);
        float f2 = translationY + ((float) iArr[1]);
        return TranslationAnimationCreator.createAnimation(view, endValues, rect.left, rect.top, f, f2, translationX, translationY, sDecelerate, this);
    }

    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        TransitionValues transitionValues = startValues;
        if (transitionValues == null) {
            return null;
        }
        Rect rect = (Rect) transitionValues.values.get(PROPNAME_SCREEN_BOUNDS);
        int i = rect.left;
        int i2 = rect.top;
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        float f = translationX;
        float f2 = translationY;
        int[] iArr = (int[]) transitionValues.view.getTag(R.id.transition_position);
        if (iArr != null) {
            f += (float) (iArr[0] - rect.left);
            f2 += (float) (iArr[1] - rect.top);
            rect.offsetTo(iArr[0], iArr[1]);
        }
        calculateOut(sceneRoot, rect, this.mTempLoc);
        int[] iArr2 = this.mTempLoc;
        float f3 = f + ((float) iArr2[0]);
        float f4 = f2 + ((float) iArr2[1]);
        return TranslationAnimationCreator.createAnimation(view, startValues, i, i2, translationX, translationY, f3, f4, sAccelerate, this);
    }
}

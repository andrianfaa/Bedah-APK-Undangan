package androidx.transition;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;

public class SidePropagation extends VisibilityPropagation {
    private float mPropagationSpeed = 3.0f;
    private int mSide = 80;

    private int distance(View sceneRoot, int viewX, int viewY, int epicenterX, int epicenterY, int left, int top, int right, int bottom) {
        int i;
        int i2 = this.mSide;
        int i3 = 5;
        boolean z = false;
        if (i2 == 8388611) {
            if (ViewCompat.getLayoutDirection(sceneRoot) == 1) {
                z = true;
            }
            if (!z) {
                i3 = 3;
            }
            i = i3;
        } else if (i2 == 8388613) {
            if (ViewCompat.getLayoutDirection(sceneRoot) == 1) {
                z = true;
            }
            if (z) {
                i3 = 3;
            }
            i = i3;
        } else {
            i = this.mSide;
        }
        switch (i) {
            case 3:
                return (right - viewX) + Math.abs(epicenterY - viewY);
            case 5:
                return (viewX - left) + Math.abs(epicenterY - viewY);
            case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE:
                return (bottom - viewY) + Math.abs(epicenterX - viewX);
            case 80:
                return (viewY - top) + Math.abs(epicenterX - viewX);
            default:
                return 0;
        }
    }

    private int getMaxDistance(ViewGroup sceneRoot) {
        switch (this.mSide) {
            case 3:
            case 5:
            case GravityCompat.START:
            case GravityCompat.END:
                return sceneRoot.getWidth();
            default:
                return sceneRoot.getHeight();
        }
    }

    public long getStartDelay(ViewGroup sceneRoot, Transition transition, TransitionValues startValues, TransitionValues endValues) {
        int i;
        TransitionValues transitionValues;
        int i2;
        int i3;
        TransitionValues transitionValues2 = startValues;
        if (transitionValues2 == null && endValues == null) {
            return 0;
        }
        Rect epicenter = transition.getEpicenter();
        if (endValues == null || getViewVisibility(transitionValues2) == 0) {
            i = -1;
            transitionValues = startValues;
        } else {
            i = 1;
            transitionValues = endValues;
        }
        int viewX = getViewX(transitionValues);
        int viewY = getViewY(transitionValues);
        int[] iArr = new int[2];
        sceneRoot.getLocationOnScreen(iArr);
        int round = iArr[0] + Math.round(sceneRoot.getTranslationX());
        int round2 = iArr[1] + Math.round(sceneRoot.getTranslationY());
        int width = round + sceneRoot.getWidth();
        int height = round2 + sceneRoot.getHeight();
        if (epicenter != null) {
            i3 = epicenter.centerX();
            i2 = epicenter.centerY();
        } else {
            i2 = (round2 + height) / 2;
            i3 = (round + width) / 2;
        }
        int[] iArr2 = iArr;
        TransitionValues transitionValues3 = transitionValues;
        float distance = ((float) distance(sceneRoot, viewX, viewY, i3, i2, round, round2, width, height)) / ((float) getMaxDistance(sceneRoot));
        long duration = transition.getDuration();
        if (duration < 0) {
            duration = 300;
        }
        return (long) Math.round((((float) (((long) i) * duration)) / this.mPropagationSpeed) * distance);
    }

    public void setPropagationSpeed(float propagationSpeed) {
        if (propagationSpeed != 0.0f) {
            this.mPropagationSpeed = propagationSpeed;
            return;
        }
        throw new IllegalArgumentException("propagationSpeed may not be 0");
    }

    public void setSide(int side) {
        this.mSide = side;
    }
}

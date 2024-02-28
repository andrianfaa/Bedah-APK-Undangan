package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;

public class Fade extends Visibility {
    public static final int IN = 1;
    private static final String LOG_TAG = "Fade";
    public static final int OUT = 2;
    private static final String PROPNAME_TRANSITION_ALPHA = "android:fade:transitionAlpha";

    private static class FadeAnimatorListener extends AnimatorListenerAdapter {
        private boolean mLayerTypeChanged = false;
        private final View mView;

        FadeAnimatorListener(View view) {
            this.mView = view;
        }

        public void onAnimationEnd(Animator animation) {
            ViewUtils.setTransitionAlpha(this.mView, 1.0f);
            if (this.mLayerTypeChanged) {
                this.mView.setLayerType(0, (Paint) null);
            }
        }

        public void onAnimationStart(Animator animation) {
            if (ViewCompat.hasOverlappingRendering(this.mView) && this.mView.getLayerType() == 0) {
                this.mLayerTypeChanged = true;
                this.mView.setLayerType(2, (Paint) null);
            }
        }
    }

    public Fade() {
    }

    public Fade(int fadingMode) {
        setMode(fadingMode);
    }

    public Fade(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, Styleable.FADE);
        setMode(TypedArrayUtils.getNamedInt(obtainStyledAttributes, (XmlResourceParser) attrs, "fadingMode", 0, getMode()));
        obtainStyledAttributes.recycle();
    }

    private Animator createAnimation(final View view, float startAlpha, float endAlpha) {
        if (startAlpha == endAlpha) {
            return null;
        }
        ViewUtils.setTransitionAlpha(view, startAlpha);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, ViewUtils.TRANSITION_ALPHA, new float[]{endAlpha});
        ofFloat.addListener(new FadeAnimatorListener(view));
        addListener(new TransitionListenerAdapter() {
            public void onTransitionEnd(Transition transition) {
                ViewUtils.setTransitionAlpha(view, 1.0f);
                ViewUtils.clearNonTransitionAlpha(view);
                transition.removeListener(this);
            }
        });
        return ofFloat;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0003, code lost:
        r1 = (java.lang.Float) r3.values.get(PROPNAME_TRANSITION_ALPHA);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static float getStartAlpha(androidx.transition.TransitionValues r3, float r4) {
        /*
            r0 = r4
            if (r3 == 0) goto L_0x0013
            java.util.Map<java.lang.String, java.lang.Object> r1 = r3.values
            java.lang.String r2 = "android:fade:transitionAlpha"
            java.lang.Object r1 = r1.get(r2)
            java.lang.Float r1 = (java.lang.Float) r1
            if (r1 == 0) goto L_0x0013
            float r0 = r1.floatValue()
        L_0x0013:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.transition.Fade.getStartAlpha(androidx.transition.TransitionValues, float):float");
    }

    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        transitionValues.values.put(PROPNAME_TRANSITION_ALPHA, Float.valueOf(ViewUtils.getTransitionAlpha(transitionValues.view)));
    }

    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        float startAlpha = getStartAlpha(startValues, 0.0f);
        if (startAlpha == 1.0f) {
            startAlpha = 0.0f;
        }
        return createAnimation(view, startAlpha, 1.0f);
    }

    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        ViewUtils.saveNonTransitionAlpha(view);
        return createAnimation(view, getStartAlpha(startValues, 1.0f), 0.0f);
    }
}

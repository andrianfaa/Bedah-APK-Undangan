package androidx.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.xmlpull.v1.XmlPullParser;

public class Slide extends Visibility {
    private static final String PROPNAME_SCREEN_POSITION = "android:slide:screenPosition";
    private static final TimeInterpolator sAccelerate = new AccelerateInterpolator();
    private static final CalculateSlide sCalculateBottom = new CalculateSlideVertical() {
        public float getGoneY(ViewGroup sceneRoot, View view) {
            return view.getTranslationY() + ((float) sceneRoot.getHeight());
        }
    };
    private static final CalculateSlide sCalculateEnd = new CalculateSlideHorizontal() {
        public float getGoneX(ViewGroup sceneRoot, View view) {
            boolean z = true;
            if (ViewCompat.getLayoutDirection(sceneRoot) != 1) {
                z = false;
            }
            return z ? view.getTranslationX() - ((float) sceneRoot.getWidth()) : view.getTranslationX() + ((float) sceneRoot.getWidth());
        }
    };
    private static final CalculateSlide sCalculateLeft = new CalculateSlideHorizontal() {
        public float getGoneX(ViewGroup sceneRoot, View view) {
            return view.getTranslationX() - ((float) sceneRoot.getWidth());
        }
    };
    private static final CalculateSlide sCalculateRight = new CalculateSlideHorizontal() {
        public float getGoneX(ViewGroup sceneRoot, View view) {
            return view.getTranslationX() + ((float) sceneRoot.getWidth());
        }
    };
    private static final CalculateSlide sCalculateStart = new CalculateSlideHorizontal() {
        public float getGoneX(ViewGroup sceneRoot, View view) {
            boolean z = true;
            if (ViewCompat.getLayoutDirection(sceneRoot) != 1) {
                z = false;
            }
            return z ? view.getTranslationX() + ((float) sceneRoot.getWidth()) : view.getTranslationX() - ((float) sceneRoot.getWidth());
        }
    };
    private static final CalculateSlide sCalculateTop = new CalculateSlideVertical() {
        public float getGoneY(ViewGroup sceneRoot, View view) {
            return view.getTranslationY() - ((float) sceneRoot.getHeight());
        }
    };
    private static final TimeInterpolator sDecelerate = new DecelerateInterpolator();
    private CalculateSlide mSlideCalculator = sCalculateBottom;
    private int mSlideEdge = 80;

    private interface CalculateSlide {
        float getGoneX(ViewGroup viewGroup, View view);

        float getGoneY(ViewGroup viewGroup, View view);
    }

    private static abstract class CalculateSlideHorizontal implements CalculateSlide {
        private CalculateSlideHorizontal() {
        }

        public float getGoneY(ViewGroup sceneRoot, View view) {
            return view.getTranslationY();
        }
    }

    private static abstract class CalculateSlideVertical implements CalculateSlide {
        private CalculateSlideVertical() {
        }

        public float getGoneX(ViewGroup sceneRoot, View view) {
            return view.getTranslationX();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface GravityFlag {
    }

    public Slide() {
        setSlideEdge(80);
    }

    public Slide(int slideEdge) {
        setSlideEdge(slideEdge);
    }

    public Slide(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, Styleable.SLIDE);
        int namedInt = TypedArrayUtils.getNamedInt(obtainStyledAttributes, (XmlPullParser) attrs, "slideEdge", 0, 80);
        obtainStyledAttributes.recycle();
        setSlideEdge(namedInt);
    }

    private void captureValues(TransitionValues transitionValues) {
        int[] iArr = new int[2];
        transitionValues.view.getLocationOnScreen(iArr);
        transitionValues.values.put(PROPNAME_SCREEN_POSITION, iArr);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        captureValues(transitionValues);
    }

    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        captureValues(transitionValues);
    }

    public int getSlideEdge() {
        return this.mSlideEdge;
    }

    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        ViewGroup viewGroup = sceneRoot;
        View view2 = view;
        TransitionValues transitionValues = endValues;
        if (transitionValues == null) {
            return null;
        }
        int[] iArr = (int[]) transitionValues.values.get(PROPNAME_SCREEN_POSITION);
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        float goneX = this.mSlideCalculator.getGoneX(viewGroup, view2);
        return TranslationAnimationCreator.createAnimation(view, endValues, iArr[0], iArr[1], goneX, this.mSlideCalculator.getGoneY(viewGroup, view2), translationX, translationY, sDecelerate, this);
    }

    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        ViewGroup viewGroup = sceneRoot;
        View view2 = view;
        TransitionValues transitionValues = startValues;
        if (transitionValues == null) {
            return null;
        }
        int[] iArr = (int[]) transitionValues.values.get(PROPNAME_SCREEN_POSITION);
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        float goneX = this.mSlideCalculator.getGoneX(viewGroup, view2);
        return TranslationAnimationCreator.createAnimation(view, startValues, iArr[0], iArr[1], translationX, translationY, goneX, this.mSlideCalculator.getGoneY(viewGroup, view2), sAccelerate, this);
    }

    public void setSlideEdge(int slideEdge) {
        switch (slideEdge) {
            case 3:
                this.mSlideCalculator = sCalculateLeft;
                break;
            case 5:
                this.mSlideCalculator = sCalculateRight;
                break;
            case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE:
                this.mSlideCalculator = sCalculateTop;
                break;
            case 80:
                this.mSlideCalculator = sCalculateBottom;
                break;
            case GravityCompat.START:
                this.mSlideCalculator = sCalculateStart;
                break;
            case GravityCompat.END:
                this.mSlideCalculator = sCalculateEnd;
                break;
            default:
                throw new IllegalArgumentException("Invalid slide direction");
        }
        this.mSlideEdge = slideEdge;
        SidePropagation sidePropagation = new SidePropagation();
        sidePropagation.setSide(slideEdge);
        setPropagation(sidePropagation);
    }
}

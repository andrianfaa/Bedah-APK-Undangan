package com.google.android.material.transformation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.Property;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.animation.ArgbEvaluatorCompat;
import com.google.android.material.animation.ChildrenAlphaProperty;
import com.google.android.material.animation.DrawableAlphaProperty;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.animation.MotionTiming;
import com.google.android.material.animation.Positioning;
import com.google.android.material.circularreveal.CircularRevealCompat;
import com.google.android.material.circularreveal.CircularRevealHelper;
import com.google.android.material.circularreveal.CircularRevealWidget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.math.MathUtils;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public abstract class FabTransformationBehavior extends ExpandableTransformationBehavior {
    private float dependencyOriginalTranslationX;
    private float dependencyOriginalTranslationY;
    private final int[] tmpArray = new int[2];
    private final Rect tmpRect = new Rect();
    private final RectF tmpRectF1 = new RectF();
    private final RectF tmpRectF2 = new RectF();

    protected static class FabTransformationSpec {
        public Positioning positioning;
        public MotionSpec timings;

        protected FabTransformationSpec() {
        }
    }

    public FabTransformationBehavior() {
    }

    public FabTransformationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private ViewGroup calculateChildContentContainer(View view) {
        View findViewById = view.findViewById(R.id.mtrl_child_content_container);
        return findViewById != null ? toViewGroupOrNull(findViewById) : ((view instanceof TransformationChildLayout) || (view instanceof TransformationChildCard)) ? toViewGroupOrNull(((ViewGroup) view).getChildAt(0)) : toViewGroupOrNull(view);
    }

    private void calculateChildVisibleBoundsAtEndOfExpansion(View child, FabTransformationSpec spec, MotionTiming translationXTiming, MotionTiming translationYTiming, float fromX, float fromY, float toX, float toY, RectF childBounds) {
        float calculateValueOfAnimationAtEndOfExpansion = calculateValueOfAnimationAtEndOfExpansion(spec, translationXTiming, fromX, toX);
        float calculateValueOfAnimationAtEndOfExpansion2 = calculateValueOfAnimationAtEndOfExpansion(spec, translationYTiming, fromY, toY);
        Rect rect = this.tmpRect;
        child.getWindowVisibleDisplayFrame(rect);
        RectF rectF = this.tmpRectF1;
        rectF.set(rect);
        RectF rectF2 = this.tmpRectF2;
        calculateWindowBounds(child, rectF2);
        rectF2.offset(calculateValueOfAnimationAtEndOfExpansion, calculateValueOfAnimationAtEndOfExpansion2);
        rectF2.intersect(rectF);
        childBounds.set(rectF2);
    }

    private void calculateDependencyWindowBounds(View view, RectF rect) {
        calculateWindowBounds(view, rect);
        rect.offset(this.dependencyOriginalTranslationX, this.dependencyOriginalTranslationY);
    }

    private Pair<MotionTiming, MotionTiming> calculateMotionTiming(float translationX, float translationY, boolean expanded, FabTransformationSpec spec) {
        MotionTiming motionTiming;
        MotionTiming motionTiming2;
        if (translationX == 0.0f || translationY == 0.0f) {
            motionTiming2 = spec.timings.getTiming("translationXLinear");
            motionTiming = spec.timings.getTiming("translationYLinear");
        } else if ((!expanded || translationY >= 0.0f) && (expanded || translationY <= 0.0f)) {
            motionTiming2 = spec.timings.getTiming("translationXCurveDownwards");
            motionTiming = spec.timings.getTiming("translationYCurveDownwards");
        } else {
            motionTiming2 = spec.timings.getTiming("translationXCurveUpwards");
            motionTiming = spec.timings.getTiming("translationYCurveUpwards");
        }
        return new Pair<>(motionTiming2, motionTiming);
    }

    private float calculateRevealCenterX(View dependency, View child, Positioning positioning) {
        RectF rectF = this.tmpRectF1;
        RectF rectF2 = this.tmpRectF2;
        calculateDependencyWindowBounds(dependency, rectF);
        calculateWindowBounds(child, rectF2);
        rectF2.offset(-calculateTranslationX(dependency, child, positioning), 0.0f);
        return rectF.centerX() - rectF2.left;
    }

    private float calculateRevealCenterY(View dependency, View child, Positioning positioning) {
        RectF rectF = this.tmpRectF1;
        RectF rectF2 = this.tmpRectF2;
        calculateDependencyWindowBounds(dependency, rectF);
        calculateWindowBounds(child, rectF2);
        rectF2.offset(0.0f, -calculateTranslationY(dependency, child, positioning));
        return rectF.centerY() - rectF2.top;
    }

    private float calculateTranslationX(View dependency, View child, Positioning positioning) {
        RectF rectF = this.tmpRectF1;
        RectF rectF2 = this.tmpRectF2;
        calculateDependencyWindowBounds(dependency, rectF);
        calculateWindowBounds(child, rectF2);
        float f = 0.0f;
        switch (positioning.gravity & 7) {
            case 1:
                f = rectF2.centerX() - rectF.centerX();
                break;
            case 3:
                f = rectF2.left - rectF.left;
                break;
            case 5:
                f = rectF2.right - rectF.right;
                break;
        }
        return f + positioning.xAdjustment;
    }

    private float calculateTranslationY(View dependency, View child, Positioning positioning) {
        RectF rectF = this.tmpRectF1;
        RectF rectF2 = this.tmpRectF2;
        calculateDependencyWindowBounds(dependency, rectF);
        calculateWindowBounds(child, rectF2);
        float f = 0.0f;
        switch (positioning.gravity & 112) {
            case 16:
                f = rectF2.centerY() - rectF.centerY();
                break;
            case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE:
                f = rectF2.top - rectF.top;
                break;
            case 80:
                f = rectF2.bottom - rectF.bottom;
                break;
        }
        return f + positioning.yAdjustment;
    }

    private float calculateValueOfAnimationAtEndOfExpansion(FabTransformationSpec spec, MotionTiming timing, float from, float to) {
        long delay = timing.getDelay();
        long duration = timing.getDuration();
        MotionTiming timing2 = spec.timings.getTiming("expansion");
        return AnimationUtils.lerp(from, to, timing.getInterpolator().getInterpolation(((float) (((timing2.getDelay() + timing2.getDuration()) + 17) - delay)) / ((float) duration)));
    }

    private void calculateWindowBounds(View view, RectF rect) {
        RectF rectF = rect;
        rectF.set(0.0f, 0.0f, (float) view.getWidth(), (float) view.getHeight());
        int[] iArr = this.tmpArray;
        view.getLocationInWindow(iArr);
        rectF.offsetTo((float) iArr[0], (float) iArr[1]);
        rectF.offset((float) ((int) (-view.getTranslationX())), (float) ((int) (-view.getTranslationY())));
    }

    private void createChildrenFadeAnimation(View unusedDependency, View child, boolean expanded, boolean currentlyAnimating, FabTransformationSpec spec, List<Animator> list, List<Animator.AnimatorListener> list2) {
        ViewGroup calculateChildContentContainer;
        ObjectAnimator objectAnimator;
        if (child instanceof ViewGroup) {
            if ((!(child instanceof CircularRevealWidget) || CircularRevealHelper.STRATEGY != 0) && (calculateChildContentContainer = calculateChildContentContainer(child)) != null) {
                if (expanded) {
                    if (!currentlyAnimating) {
                        ChildrenAlphaProperty.CHILDREN_ALPHA.set(calculateChildContentContainer, Float.valueOf(0.0f));
                    }
                    objectAnimator = ObjectAnimator.ofFloat(calculateChildContentContainer, ChildrenAlphaProperty.CHILDREN_ALPHA, new float[]{1.0f});
                } else {
                    objectAnimator = ObjectAnimator.ofFloat(calculateChildContentContainer, ChildrenAlphaProperty.CHILDREN_ALPHA, new float[]{0.0f});
                }
                spec.timings.getTiming("contentFade").apply(objectAnimator);
                list.add(objectAnimator);
            }
        }
    }

    private void createColorAnimation(View dependency, View child, boolean expanded, boolean currentlyAnimating, FabTransformationSpec spec, List<Animator> list, List<Animator.AnimatorListener> list2) {
        ObjectAnimator objectAnimator;
        if (child instanceof CircularRevealWidget) {
            CircularRevealWidget circularRevealWidget = (CircularRevealWidget) child;
            int backgroundTint = getBackgroundTint(dependency);
            int i = 16777215 & backgroundTint;
            if (expanded) {
                if (!currentlyAnimating) {
                    circularRevealWidget.setCircularRevealScrimColor(backgroundTint);
                }
                objectAnimator = ObjectAnimator.ofInt(circularRevealWidget, CircularRevealWidget.CircularRevealScrimColorProperty.CIRCULAR_REVEAL_SCRIM_COLOR, new int[]{i});
            } else {
                objectAnimator = ObjectAnimator.ofInt(circularRevealWidget, CircularRevealWidget.CircularRevealScrimColorProperty.CIRCULAR_REVEAL_SCRIM_COLOR, new int[]{backgroundTint});
            }
            objectAnimator.setEvaluator(ArgbEvaluatorCompat.getInstance());
            spec.timings.getTiming(TypedValues.Custom.S_COLOR).apply(objectAnimator);
            list.add(objectAnimator);
        }
    }

    private void createDependencyTranslationAnimation(View dependency, View child, boolean expanded, FabTransformationSpec spec, List<Animator> list) {
        float calculateTranslationX = calculateTranslationX(dependency, child, spec.positioning);
        float calculateTranslationY = calculateTranslationY(dependency, child, spec.positioning);
        Pair<MotionTiming, MotionTiming> calculateMotionTiming = calculateMotionTiming(calculateTranslationX, calculateTranslationY, expanded, spec);
        MotionTiming motionTiming = (MotionTiming) calculateMotionTiming.first;
        MotionTiming motionTiming2 = (MotionTiming) calculateMotionTiming.second;
        Property property = View.TRANSLATION_X;
        float[] fArr = new float[1];
        fArr[0] = expanded ? calculateTranslationX : this.dependencyOriginalTranslationX;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(dependency, property, fArr);
        Property property2 = View.TRANSLATION_Y;
        float[] fArr2 = new float[1];
        fArr2[0] = expanded ? calculateTranslationY : this.dependencyOriginalTranslationY;
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(dependency, property2, fArr2);
        motionTiming.apply(ofFloat);
        motionTiming2.apply(ofFloat2);
        list.add(ofFloat);
        list.add(ofFloat2);
    }

    private void createElevationAnimation(View dependency, View child, boolean expanded, boolean currentlyAnimating, FabTransformationSpec spec, List<Animator> list, List<Animator.AnimatorListener> list2) {
        ObjectAnimator objectAnimator;
        float elevation = ViewCompat.getElevation(child) - ViewCompat.getElevation(dependency);
        if (expanded) {
            if (!currentlyAnimating) {
                child.setTranslationZ(-elevation);
            }
            objectAnimator = ObjectAnimator.ofFloat(child, View.TRANSLATION_Z, new float[]{0.0f});
        } else {
            objectAnimator = ObjectAnimator.ofFloat(child, View.TRANSLATION_Z, new float[]{-elevation});
        }
        spec.timings.getTiming("elevation").apply(objectAnimator);
        list.add(objectAnimator);
    }

    private void createExpansionAnimation(View dependency, View child, boolean expanded, boolean currentlyAnimating, FabTransformationSpec spec, float childWidth, float childHeight, List<Animator> list, List<Animator.AnimatorListener> list2) {
        CircularRevealWidget circularRevealWidget;
        MotionTiming motionTiming;
        Animator animator;
        View view = dependency;
        View view2 = child;
        FabTransformationSpec fabTransformationSpec = spec;
        if (view2 instanceof CircularRevealWidget) {
            final CircularRevealWidget circularRevealWidget2 = (CircularRevealWidget) view2;
            float calculateRevealCenterX = calculateRevealCenterX(view, view2, fabTransformationSpec.positioning);
            float calculateRevealCenterY = calculateRevealCenterY(view, view2, fabTransformationSpec.positioning);
            ((FloatingActionButton) view).getContentRect(this.tmpRect);
            float width = ((float) this.tmpRect.width()) / 2.0f;
            MotionTiming timing = fabTransformationSpec.timings.getTiming("expansion");
            if (expanded) {
                if (!currentlyAnimating) {
                    circularRevealWidget2.setRevealInfo(new CircularRevealWidget.RevealInfo(calculateRevealCenterX, calculateRevealCenterY, width));
                }
                float f = currentlyAnimating ? circularRevealWidget2.getRevealInfo().radius : width;
                float distanceToFurthestCorner = MathUtils.distanceToFurthestCorner(calculateRevealCenterX, calculateRevealCenterY, 0.0f, 0.0f, childWidth, childHeight);
                Animator createCircularReveal = CircularRevealCompat.createCircularReveal(circularRevealWidget2, calculateRevealCenterX, calculateRevealCenterY, distanceToFurthestCorner);
                createCircularReveal.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        CircularRevealWidget.RevealInfo revealInfo = circularRevealWidget2.getRevealInfo();
                        revealInfo.radius = Float.MAX_VALUE;
                        circularRevealWidget2.setRevealInfo(revealInfo);
                    }
                });
                Animator animator2 = createCircularReveal;
                float f2 = distanceToFurthestCorner;
                motionTiming = timing;
                createPreFillRadialExpansion(child, timing.getDelay(), (int) calculateRevealCenterX, (int) calculateRevealCenterY, f, list);
                float f3 = width;
                float f4 = calculateRevealCenterY;
                float f5 = calculateRevealCenterX;
                circularRevealWidget = circularRevealWidget2;
                animator = animator2;
            } else {
                motionTiming = timing;
                float f6 = circularRevealWidget2.getRevealInfo().radius;
                float f7 = width;
                Animator createCircularReveal2 = CircularRevealCompat.createCircularReveal(circularRevealWidget2, calculateRevealCenterX, calculateRevealCenterY, f7);
                float f8 = f6;
                createPreFillRadialExpansion(child, motionTiming.getDelay(), (int) calculateRevealCenterX, (int) calculateRevealCenterY, f6, list);
                float f9 = width;
                int i = (int) calculateRevealCenterX;
                float f10 = calculateRevealCenterY;
                int i2 = (int) calculateRevealCenterY;
                float f11 = calculateRevealCenterX;
                circularRevealWidget = circularRevealWidget2;
                createPostFillRadialExpansion(child, motionTiming.getDelay(), motionTiming.getDuration(), fabTransformationSpec.timings.getTotalDuration(), i, i2, f7, list);
                animator = createCircularReveal2;
            }
            motionTiming.apply(animator);
            list.add(animator);
            list2.add(CircularRevealCompat.createCircularRevealListener(circularRevealWidget));
        }
    }

    private void createIconFadeAnimation(View dependency, final View child, boolean expanded, boolean currentlyAnimating, FabTransformationSpec spec, List<Animator> list, List<Animator.AnimatorListener> list2) {
        ObjectAnimator objectAnimator;
        if ((child instanceof CircularRevealWidget) && (dependency instanceof ImageView)) {
            final CircularRevealWidget circularRevealWidget = (CircularRevealWidget) child;
            final Drawable drawable = ((ImageView) dependency).getDrawable();
            if (drawable != null) {
                drawable.mutate();
                if (expanded) {
                    if (!currentlyAnimating) {
                        drawable.setAlpha(255);
                    }
                    objectAnimator = ObjectAnimator.ofInt(drawable, DrawableAlphaProperty.DRAWABLE_ALPHA_COMPAT, new int[]{0});
                } else {
                    objectAnimator = ObjectAnimator.ofInt(drawable, DrawableAlphaProperty.DRAWABLE_ALPHA_COMPAT, new int[]{255});
                }
                objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        child.invalidate();
                    }
                });
                spec.timings.getTiming("iconFade").apply(objectAnimator);
                list.add(objectAnimator);
                list2.add(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        circularRevealWidget.setCircularRevealOverlayDrawable((Drawable) null);
                    }

                    public void onAnimationStart(Animator animation) {
                        circularRevealWidget.setCircularRevealOverlayDrawable(drawable);
                    }
                });
            }
        }
    }

    private void createPostFillRadialExpansion(View child, long delay, long duration, long totalDuration, int revealCenterX, int revealCenterY, float toRadius, List<Animator> list) {
        if (Build.VERSION.SDK_INT >= 21 && delay + duration < totalDuration) {
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(child, revealCenterX, revealCenterY, toRadius, toRadius);
            createCircularReveal.setStartDelay(delay + duration);
            createCircularReveal.setDuration(totalDuration - (delay + duration));
            list.add(createCircularReveal);
        }
    }

    private void createPreFillRadialExpansion(View child, long delay, int revealCenterX, int revealCenterY, float fromRadius, List<Animator> list) {
        if (Build.VERSION.SDK_INT >= 21 && delay > 0) {
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(child, revealCenterX, revealCenterY, fromRadius, fromRadius);
            createCircularReveal.setStartDelay(0);
            createCircularReveal.setDuration(delay);
            list.add(createCircularReveal);
        }
    }

    private void createTranslationAnimation(View dependency, View child, boolean expanded, boolean currentlyAnimating, FabTransformationSpec spec, List<Animator> list, List<Animator.AnimatorListener> list2, RectF childBounds) {
        MotionTiming motionTiming;
        MotionTiming motionTiming2;
        ObjectAnimator objectAnimator;
        ObjectAnimator objectAnimator2;
        View view = dependency;
        View view2 = child;
        boolean z = expanded;
        FabTransformationSpec fabTransformationSpec = spec;
        List<Animator> list3 = list;
        float calculateTranslationX = calculateTranslationX(view, view2, fabTransformationSpec.positioning);
        float calculateTranslationY = calculateTranslationY(view, view2, fabTransformationSpec.positioning);
        Pair<MotionTiming, MotionTiming> calculateMotionTiming = calculateMotionTiming(calculateTranslationX, calculateTranslationY, z, fabTransformationSpec);
        MotionTiming motionTiming3 = (MotionTiming) calculateMotionTiming.first;
        MotionTiming motionTiming4 = (MotionTiming) calculateMotionTiming.second;
        if (z) {
            if (!currentlyAnimating) {
                view2.setTranslationX(-calculateTranslationX);
                view2.setTranslationY(-calculateTranslationY);
            }
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view2, View.TRANSLATION_X, new float[]{0.0f});
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view2, View.TRANSLATION_Y, new float[]{0.0f});
            motionTiming2 = motionTiming4;
            motionTiming = motionTiming3;
            float f = -calculateTranslationY;
            Pair<MotionTiming, MotionTiming> pair = calculateMotionTiming;
            float f2 = calculateTranslationX;
            calculateChildVisibleBoundsAtEndOfExpansion(child, spec, motionTiming3, motionTiming4, -calculateTranslationX, f, 0.0f, 0.0f, childBounds);
            objectAnimator2 = ofFloat;
            objectAnimator = ofFloat2;
            float f3 = calculateTranslationY;
        } else {
            motionTiming2 = motionTiming4;
            motionTiming = motionTiming3;
            Pair<MotionTiming, MotionTiming> pair2 = calculateMotionTiming;
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(view2, View.TRANSLATION_X, new float[]{-calculateTranslationX});
            objectAnimator2 = ofFloat3;
            objectAnimator = ObjectAnimator.ofFloat(view2, View.TRANSLATION_Y, new float[]{-calculateTranslationY});
        }
        motionTiming.apply(objectAnimator2);
        motionTiming2.apply(objectAnimator);
        list3.add(objectAnimator2);
        list3.add(objectAnimator);
    }

    private int getBackgroundTint(View view) {
        ColorStateList backgroundTintList = ViewCompat.getBackgroundTintList(view);
        if (backgroundTintList != null) {
            return backgroundTintList.getColorForState(view.getDrawableState(), backgroundTintList.getDefaultColor());
        }
        return 0;
    }

    private ViewGroup toViewGroupOrNull(View view) {
        if (view instanceof ViewGroup) {
            return (ViewGroup) view;
        }
        return null;
    }

    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (child.getVisibility() == 8) {
            throw new IllegalStateException("This behavior cannot be attached to a GONE view. Set the view to INVISIBLE instead.");
        } else if (!(dependency instanceof FloatingActionButton)) {
            return false;
        } else {
            int expandedComponentIdHint = ((FloatingActionButton) dependency).getExpandedComponentIdHint();
            return expandedComponentIdHint == 0 || expandedComponentIdHint == child.getId();
        }
    }

    public void onAttachedToLayoutParams(CoordinatorLayout.LayoutParams lp) {
        if (lp.dodgeInsetEdges == 0) {
            lp.dodgeInsetEdges = 80;
        }
    }

    /* access modifiers changed from: protected */
    public AnimatorSet onCreateExpandedStateChangeAnimation(View dependency, View child, boolean expanded, boolean isAnimating) {
        final boolean z = expanded;
        FabTransformationSpec onCreateMotionSpec = onCreateMotionSpec(child.getContext(), z);
        if (z) {
            this.dependencyOriginalTranslationX = dependency.getTranslationX();
            this.dependencyOriginalTranslationY = dependency.getTranslationY();
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (Build.VERSION.SDK_INT >= 21) {
            createElevationAnimation(dependency, child, expanded, isAnimating, onCreateMotionSpec, arrayList, arrayList2);
        }
        RectF rectF = this.tmpRectF1;
        View view = dependency;
        View view2 = child;
        boolean z2 = expanded;
        ArrayList arrayList3 = arrayList;
        ArrayList arrayList4 = arrayList2;
        createTranslationAnimation(view, view2, z2, isAnimating, onCreateMotionSpec, arrayList3, arrayList4, rectF);
        float width = rectF.width();
        float height = rectF.height();
        createDependencyTranslationAnimation(view, view2, z2, onCreateMotionSpec, arrayList);
        boolean z3 = isAnimating;
        FabTransformationSpec fabTransformationSpec = onCreateMotionSpec;
        createIconFadeAnimation(view, view2, z2, z3, fabTransformationSpec, arrayList3, arrayList4);
        createExpansionAnimation(view, view2, z2, z3, fabTransformationSpec, width, height, arrayList, arrayList2);
        ArrayList arrayList5 = arrayList;
        ArrayList arrayList6 = arrayList2;
        createColorAnimation(view, view2, z2, z3, fabTransformationSpec, arrayList5, arrayList6);
        createChildrenFadeAnimation(view, view2, z2, z3, fabTransformationSpec, arrayList5, arrayList6);
        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSetCompat.playTogether(animatorSet, arrayList);
        final View view3 = dependency;
        final View view4 = child;
        animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if (!z) {
                    view4.setVisibility(4);
                    view3.setAlpha(1.0f);
                    view3.setVisibility(0);
                }
            }

            public void onAnimationStart(Animator animation) {
                if (z) {
                    view4.setVisibility(0);
                    view3.setAlpha(0.0f);
                    view3.setVisibility(4);
                }
            }
        });
        int size = arrayList2.size();
        for (int i = 0; i < size; i++) {
            animatorSet.addListener((Animator.AnimatorListener) arrayList2.get(i));
        }
        return animatorSet;
    }

    /* access modifiers changed from: protected */
    public abstract FabTransformationSpec onCreateMotionSpec(Context context, boolean z);
}

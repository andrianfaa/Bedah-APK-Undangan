package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.animation.ImageMatrixProperty;
import com.google.android.material.animation.MatrixEvaluator;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.internal.StateListAnimator;
import com.google.android.material.motion.MotionUtils;
import com.google.android.material.ripple.RippleDrawableCompat;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shadow.ShadowViewDelegate;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import java.util.ArrayList;
import java.util.Iterator;

class FloatingActionButtonImpl {
    static final int ANIM_STATE_HIDING = 1;
    static final int ANIM_STATE_NONE = 0;
    static final int ANIM_STATE_SHOWING = 2;
    static final long ELEVATION_ANIM_DELAY = 100;
    static final long ELEVATION_ANIM_DURATION = 100;
    static final TimeInterpolator ELEVATION_ANIM_INTERPOLATOR = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
    static final int[] EMPTY_STATE_SET = new int[0];
    static final int[] ENABLED_STATE_SET = {16842910};
    static final int[] FOCUSED_ENABLED_STATE_SET = {16842908, 16842910};
    private static final float HIDE_ICON_SCALE = 0.4f;
    private static final float HIDE_OPACITY = 0.0f;
    private static final float HIDE_SCALE = 0.4f;
    static final int[] HOVERED_ENABLED_STATE_SET = {16843623, 16842910};
    static final int[] HOVERED_FOCUSED_ENABLED_STATE_SET = {16843623, 16842908, 16842910};
    static final int[] PRESSED_ENABLED_STATE_SET = {16842919, 16842910};
    static final float SHADOW_MULTIPLIER = 1.5f;
    private static final float SHOW_ICON_SCALE = 1.0f;
    private static final float SHOW_OPACITY = 1.0f;
    private static final float SHOW_SCALE = 1.0f;
    private static final float SPEC_HIDE_ICON_SCALE = 0.0f;
    private static final float SPEC_HIDE_SCALE = 0.0f;
    /* access modifiers changed from: private */
    public int animState = 0;
    BorderDrawable borderDrawable;
    Drawable contentBackground;
    /* access modifiers changed from: private */
    public Animator currentAnimator;
    float elevation;
    boolean ensureMinTouchTargetSize;
    private ArrayList<Animator.AnimatorListener> hideListeners;
    private MotionSpec hideMotionSpec;
    float hoveredFocusedTranslationZ;
    /* access modifiers changed from: private */
    public float imageMatrixScale = 1.0f;
    private int maxImageSize;
    int minTouchTargetSize;
    private ViewTreeObserver.OnPreDrawListener preDrawListener;
    float pressedTranslationZ;
    Drawable rippleDrawable;
    private float rotation;
    boolean shadowPaddingEnabled = true;
    final ShadowViewDelegate shadowViewDelegate;
    ShapeAppearanceModel shapeAppearance;
    MaterialShapeDrawable shapeDrawable;
    private ArrayList<Animator.AnimatorListener> showListeners;
    private MotionSpec showMotionSpec;
    private final StateListAnimator stateListAnimator;
    private final Matrix tmpMatrix = new Matrix();
    private final Rect tmpRect = new Rect();
    private final RectF tmpRectF1 = new RectF();
    private final RectF tmpRectF2 = new RectF();
    private ArrayList<InternalTransformationCallback> transformationCallbacks;
    final FloatingActionButton view;

    private class DisabledElevationAnimation extends ShadowAnimatorImpl {
        DisabledElevationAnimation() {
            super();
        }

        /* access modifiers changed from: protected */
        public float getTargetShadowSize() {
            return 0.0f;
        }
    }

    private class ElevateToHoveredFocusedTranslationZAnimation extends ShadowAnimatorImpl {
        ElevateToHoveredFocusedTranslationZAnimation() {
            super();
        }

        /* access modifiers changed from: protected */
        public float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.hoveredFocusedTranslationZ;
        }
    }

    private class ElevateToPressedTranslationZAnimation extends ShadowAnimatorImpl {
        ElevateToPressedTranslationZAnimation() {
            super();
        }

        /* access modifiers changed from: protected */
        public float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.pressedTranslationZ;
        }
    }

    interface InternalTransformationCallback {
        void onScaleChanged();

        void onTranslationChanged();
    }

    interface InternalVisibilityChangedListener {
        void onHidden();

        void onShown();
    }

    private class ResetElevationAnimation extends ShadowAnimatorImpl {
        ResetElevationAnimation() {
            super();
        }

        /* access modifiers changed from: protected */
        public float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.elevation;
        }
    }

    private abstract class ShadowAnimatorImpl extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {
        private float shadowSizeEnd;
        private float shadowSizeStart;
        private boolean validValues;

        private ShadowAnimatorImpl() {
        }

        /* access modifiers changed from: protected */
        public abstract float getTargetShadowSize();

        public void onAnimationEnd(Animator animator) {
            FloatingActionButtonImpl.this.updateShapeElevation((float) ((int) this.shadowSizeEnd));
            this.validValues = false;
        }

        public void onAnimationUpdate(ValueAnimator animator) {
            if (!this.validValues) {
                this.shadowSizeStart = FloatingActionButtonImpl.this.shapeDrawable == null ? 0.0f : FloatingActionButtonImpl.this.shapeDrawable.getElevation();
                this.shadowSizeEnd = getTargetShadowSize();
                this.validValues = true;
            }
            FloatingActionButtonImpl floatingActionButtonImpl = FloatingActionButtonImpl.this;
            float f = this.shadowSizeStart;
            floatingActionButtonImpl.updateShapeElevation((float) ((int) (f + ((this.shadowSizeEnd - f) * animator.getAnimatedFraction()))));
        }
    }

    FloatingActionButtonImpl(FloatingActionButton view2, ShadowViewDelegate shadowViewDelegate2) {
        this.view = view2;
        this.shadowViewDelegate = shadowViewDelegate2;
        StateListAnimator stateListAnimator2 = new StateListAnimator();
        this.stateListAnimator = stateListAnimator2;
        stateListAnimator2.addState(PRESSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToPressedTranslationZAnimation()));
        stateListAnimator2.addState(HOVERED_FOCUSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
        stateListAnimator2.addState(FOCUSED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
        stateListAnimator2.addState(HOVERED_ENABLED_STATE_SET, createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
        stateListAnimator2.addState(ENABLED_STATE_SET, createElevationAnimator(new ResetElevationAnimation()));
        stateListAnimator2.addState(EMPTY_STATE_SET, createElevationAnimator(new DisabledElevationAnimation()));
        this.rotation = view2.getRotation();
    }

    /* access modifiers changed from: private */
    public void calculateImageMatrixFromScale(float scale, Matrix matrix) {
        matrix.reset();
        Drawable drawable = this.view.getDrawable();
        if (drawable != null && this.maxImageSize != 0) {
            RectF rectF = this.tmpRectF1;
            RectF rectF2 = this.tmpRectF2;
            rectF.set(0.0f, 0.0f, (float) drawable.getIntrinsicWidth(), (float) drawable.getIntrinsicHeight());
            int i = this.maxImageSize;
            rectF2.set(0.0f, 0.0f, (float) i, (float) i);
            matrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.CENTER);
            int i2 = this.maxImageSize;
            matrix.postScale(scale, scale, ((float) i2) / 2.0f, ((float) i2) / 2.0f);
        }
    }

    private AnimatorSet createAnimator(MotionSpec spec, float opacity, float scale, float iconScale) {
        ArrayList arrayList = new ArrayList();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.view, View.ALPHA, new float[]{opacity});
        spec.getTiming("opacity").apply(ofFloat);
        arrayList.add(ofFloat);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.view, View.SCALE_X, new float[]{scale});
        spec.getTiming("scale").apply(ofFloat2);
        workAroundOreoBug(ofFloat2);
        arrayList.add(ofFloat2);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.view, View.SCALE_Y, new float[]{scale});
        spec.getTiming("scale").apply(ofFloat3);
        workAroundOreoBug(ofFloat3);
        arrayList.add(ofFloat3);
        calculateImageMatrixFromScale(iconScale, this.tmpMatrix);
        ObjectAnimator ofObject = ObjectAnimator.ofObject(this.view, new ImageMatrixProperty(), new MatrixEvaluator() {
            public Matrix evaluate(float fraction, Matrix startValue, Matrix endValue) {
                float unused = FloatingActionButtonImpl.this.imageMatrixScale = fraction;
                return super.evaluate(fraction, startValue, endValue);
            }
        }, new Matrix[]{new Matrix(this.tmpMatrix)});
        spec.getTiming("iconScale").apply(ofObject);
        arrayList.add(ofObject);
        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSetCompat.playTogether(animatorSet, arrayList);
        return animatorSet;
    }

    private AnimatorSet createDefaultAnimator(float targetOpacity, float targetScale, float targetIconScale) {
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        float alpha = this.view.getAlpha();
        float scaleX = this.view.getScaleX();
        float scaleY = this.view.getScaleY();
        float f = this.imageMatrixScale;
        final Matrix matrix = new Matrix(this.tmpMatrix);
        final float f2 = alpha;
        final float f3 = targetOpacity;
        final float f4 = scaleX;
        final float f5 = targetScale;
        final float f6 = scaleY;
        float f7 = alpha;
        AnonymousClass4 r14 = r0;
        final float f8 = f;
        float f9 = f;
        final float f10 = targetIconScale;
        AnonymousClass4 r0 = new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float floatValue = ((Float) animation.getAnimatedValue()).floatValue();
                FloatingActionButtonImpl.this.view.setAlpha(AnimationUtils.lerp(f2, f3, 0.0f, 0.2f, floatValue));
                FloatingActionButtonImpl.this.view.setScaleX(AnimationUtils.lerp(f4, f5, floatValue));
                FloatingActionButtonImpl.this.view.setScaleY(AnimationUtils.lerp(f6, f5, floatValue));
                float unused = FloatingActionButtonImpl.this.imageMatrixScale = AnimationUtils.lerp(f8, f10, floatValue);
                FloatingActionButtonImpl.this.calculateImageMatrixFromScale(AnimationUtils.lerp(f8, f10, floatValue), matrix);
                FloatingActionButtonImpl.this.view.setImageMatrix(matrix);
            }
        };
        ofFloat.addUpdateListener(r14);
        arrayList.add(ofFloat);
        AnimatorSetCompat.playTogether(animatorSet, arrayList);
        animatorSet.setDuration((long) MotionUtils.resolveThemeDuration(this.view.getContext(), R.attr.motionDurationLong1, this.view.getContext().getResources().getInteger(R.integer.material_motion_duration_long_1)));
        animatorSet.setInterpolator(MotionUtils.resolveThemeInterpolator(this.view.getContext(), R.attr.motionEasingStandard, AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR));
        return animatorSet;
    }

    private ValueAnimator createElevationAnimator(ShadowAnimatorImpl impl) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
        valueAnimator.setDuration(100);
        valueAnimator.addListener(impl);
        valueAnimator.addUpdateListener(impl);
        valueAnimator.setFloatValues(new float[]{0.0f, 1.0f});
        return valueAnimator;
    }

    private ViewTreeObserver.OnPreDrawListener getOrCreatePreDrawListener() {
        if (this.preDrawListener == null) {
            this.preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    FloatingActionButtonImpl.this.onPreDraw();
                    return true;
                }
            };
        }
        return this.preDrawListener;
    }

    private boolean shouldAnimateVisibilityChange() {
        return ViewCompat.isLaidOut(this.view) && !this.view.isInEditMode();
    }

    private void workAroundOreoBug(ObjectAnimator animator) {
        if (Build.VERSION.SDK_INT == 26) {
            animator.setEvaluator(new TypeEvaluator<Float>() {
                FloatEvaluator floatEvaluator = new FloatEvaluator();

                public Float evaluate(float fraction, Float startValue, Float endValue) {
                    float floatValue = this.floatEvaluator.evaluate(fraction, startValue, endValue).floatValue();
                    return Float.valueOf(floatValue < 0.1f ? 0.0f : floatValue);
                }
            });
        }
    }

    public void addOnHideAnimationListener(Animator.AnimatorListener listener) {
        if (this.hideListeners == null) {
            this.hideListeners = new ArrayList<>();
        }
        this.hideListeners.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void addOnShowAnimationListener(Animator.AnimatorListener listener) {
        if (this.showListeners == null) {
            this.showListeners = new ArrayList<>();
        }
        this.showListeners.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void addTransformationCallback(InternalTransformationCallback listener) {
        if (this.transformationCallbacks == null) {
            this.transformationCallbacks = new ArrayList<>();
        }
        this.transformationCallbacks.add(listener);
    }

    /* access modifiers changed from: package-private */
    public MaterialShapeDrawable createShapeDrawable() {
        return new MaterialShapeDrawable((ShapeAppearanceModel) Preconditions.checkNotNull(this.shapeAppearance));
    }

    /* access modifiers changed from: package-private */
    public final Drawable getContentBackground() {
        return this.contentBackground;
    }

    /* access modifiers changed from: package-private */
    public float getElevation() {
        return this.elevation;
    }

    /* access modifiers changed from: package-private */
    public boolean getEnsureMinTouchTargetSize() {
        return this.ensureMinTouchTargetSize;
    }

    /* access modifiers changed from: package-private */
    public final MotionSpec getHideMotionSpec() {
        return this.hideMotionSpec;
    }

    /* access modifiers changed from: package-private */
    public float getHoveredFocusedTranslationZ() {
        return this.hoveredFocusedTranslationZ;
    }

    /* access modifiers changed from: package-private */
    public void getPadding(Rect rect) {
        int sizeDimension = this.ensureMinTouchTargetSize ? (this.minTouchTargetSize - this.view.getSizeDimension()) / 2 : 0;
        float elevation2 = this.shadowPaddingEnabled ? getElevation() + this.pressedTranslationZ : 0.0f;
        int max = Math.max(sizeDimension, (int) Math.ceil((double) elevation2));
        int max2 = Math.max(sizeDimension, (int) Math.ceil((double) (SHADOW_MULTIPLIER * elevation2)));
        rect.set(max, max2, max, max2);
    }

    /* access modifiers changed from: package-private */
    public float getPressedTranslationZ() {
        return this.pressedTranslationZ;
    }

    /* access modifiers changed from: package-private */
    public final ShapeAppearanceModel getShapeAppearance() {
        return this.shapeAppearance;
    }

    /* access modifiers changed from: package-private */
    public final MotionSpec getShowMotionSpec() {
        return this.showMotionSpec;
    }

    /* access modifiers changed from: package-private */
    public void hide(final InternalVisibilityChangedListener listener, final boolean fromUser) {
        if (!isOrWillBeHidden()) {
            Animator animator = this.currentAnimator;
            if (animator != null) {
                animator.cancel();
            }
            if (shouldAnimateVisibilityChange()) {
                MotionSpec motionSpec = this.hideMotionSpec;
                AnimatorSet createAnimator = motionSpec != null ? createAnimator(motionSpec, 0.0f, 0.0f, 0.0f) : createDefaultAnimator(0.0f, 0.4f, 0.4f);
                createAnimator.addListener(new AnimatorListenerAdapter() {
                    private boolean cancelled;

                    public void onAnimationCancel(Animator animation) {
                        this.cancelled = true;
                    }

                    public void onAnimationEnd(Animator animation) {
                        int unused = FloatingActionButtonImpl.this.animState = 0;
                        Animator unused2 = FloatingActionButtonImpl.this.currentAnimator = null;
                        if (!this.cancelled) {
                            FloatingActionButton floatingActionButton = FloatingActionButtonImpl.this.view;
                            boolean z = fromUser;
                            floatingActionButton.internalSetVisibility(z ? 8 : 4, z);
                            InternalVisibilityChangedListener internalVisibilityChangedListener = listener;
                            if (internalVisibilityChangedListener != null) {
                                internalVisibilityChangedListener.onHidden();
                            }
                        }
                    }

                    public void onAnimationStart(Animator animation) {
                        FloatingActionButtonImpl.this.view.internalSetVisibility(0, fromUser);
                        int unused = FloatingActionButtonImpl.this.animState = 1;
                        Animator unused2 = FloatingActionButtonImpl.this.currentAnimator = animation;
                        this.cancelled = false;
                    }
                });
                ArrayList<Animator.AnimatorListener> arrayList = this.hideListeners;
                if (arrayList != null) {
                    Iterator<Animator.AnimatorListener> it = arrayList.iterator();
                    while (it.hasNext()) {
                        createAnimator.addListener(it.next());
                    }
                }
                createAnimator.start();
                return;
            }
            this.view.internalSetVisibility(fromUser ? 8 : 4, fromUser);
            if (listener != null) {
                listener.onHidden();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void initializeBackgroundDrawable(ColorStateList backgroundTint, PorterDuff.Mode backgroundTintMode, ColorStateList rippleColor, int borderWidth) {
        MaterialShapeDrawable createShapeDrawable = createShapeDrawable();
        this.shapeDrawable = createShapeDrawable;
        createShapeDrawable.setTintList(backgroundTint);
        if (backgroundTintMode != null) {
            this.shapeDrawable.setTintMode(backgroundTintMode);
        }
        this.shapeDrawable.setShadowColor(-12303292);
        this.shapeDrawable.initializeElevationOverlay(this.view.getContext());
        RippleDrawableCompat rippleDrawableCompat = new RippleDrawableCompat(this.shapeDrawable.getShapeAppearanceModel());
        rippleDrawableCompat.setTintList(RippleUtils.sanitizeRippleDrawableColor(rippleColor));
        this.rippleDrawable = rippleDrawableCompat;
        this.contentBackground = new LayerDrawable(new Drawable[]{(Drawable) Preconditions.checkNotNull(this.shapeDrawable), rippleDrawableCompat});
    }

    /* access modifiers changed from: package-private */
    public boolean isOrWillBeHidden() {
        return this.view.getVisibility() == 0 ? this.animState == 1 : this.animState != 2;
    }

    /* access modifiers changed from: package-private */
    public boolean isOrWillBeShown() {
        return this.view.getVisibility() != 0 ? this.animState == 2 : this.animState != 1;
    }

    /* access modifiers changed from: package-private */
    public void jumpDrawableToCurrentState() {
        this.stateListAnimator.jumpToCurrentState();
    }

    /* access modifiers changed from: package-private */
    public void onAttachedToWindow() {
        MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
        if (materialShapeDrawable != null) {
            MaterialShapeUtils.setParentAbsoluteElevation(this.view, materialShapeDrawable);
        }
        if (requirePreDrawListener()) {
            this.view.getViewTreeObserver().addOnPreDrawListener(getOrCreatePreDrawListener());
        }
    }

    /* access modifiers changed from: package-private */
    public void onCompatShadowChanged() {
    }

    /* access modifiers changed from: package-private */
    public void onDetachedFromWindow() {
        ViewTreeObserver viewTreeObserver = this.view.getViewTreeObserver();
        ViewTreeObserver.OnPreDrawListener onPreDrawListener = this.preDrawListener;
        if (onPreDrawListener != null) {
            viewTreeObserver.removeOnPreDrawListener(onPreDrawListener);
            this.preDrawListener = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void onDrawableStateChanged(int[] state) {
        this.stateListAnimator.setState(state);
    }

    /* access modifiers changed from: package-private */
    public void onElevationsChanged(float elevation2, float hoveredFocusedTranslationZ2, float pressedTranslationZ2) {
        updatePadding();
        updateShapeElevation(elevation2);
    }

    /* access modifiers changed from: package-private */
    public void onPaddingUpdated(Rect padding) {
        Preconditions.checkNotNull(this.contentBackground, "Didn't initialize content background");
        if (shouldAddPadding()) {
            this.shadowViewDelegate.setBackgroundDrawable(new InsetDrawable(this.contentBackground, padding.left, padding.top, padding.right, padding.bottom));
            return;
        }
        this.shadowViewDelegate.setBackgroundDrawable(this.contentBackground);
    }

    /* access modifiers changed from: package-private */
    public void onPreDraw() {
        float rotation2 = this.view.getRotation();
        if (this.rotation != rotation2) {
            this.rotation = rotation2;
            updateFromViewRotation();
        }
    }

    /* access modifiers changed from: package-private */
    public void onScaleChanged() {
        ArrayList<InternalTransformationCallback> arrayList = this.transformationCallbacks;
        if (arrayList != null) {
            Iterator<InternalTransformationCallback> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().onScaleChanged();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onTranslationChanged() {
        ArrayList<InternalTransformationCallback> arrayList = this.transformationCallbacks;
        if (arrayList != null) {
            Iterator<InternalTransformationCallback> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().onTranslationChanged();
            }
        }
    }

    public void removeOnHideAnimationListener(Animator.AnimatorListener listener) {
        ArrayList<Animator.AnimatorListener> arrayList = this.hideListeners;
        if (arrayList != null) {
            arrayList.remove(listener);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeOnShowAnimationListener(Animator.AnimatorListener listener) {
        ArrayList<Animator.AnimatorListener> arrayList = this.showListeners;
        if (arrayList != null) {
            arrayList.remove(listener);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeTransformationCallback(InternalTransformationCallback listener) {
        ArrayList<InternalTransformationCallback> arrayList = this.transformationCallbacks;
        if (arrayList != null) {
            arrayList.remove(listener);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean requirePreDrawListener() {
        return true;
    }

    /* access modifiers changed from: package-private */
    public void setBackgroundTintList(ColorStateList tint) {
        MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setTintList(tint);
        }
        BorderDrawable borderDrawable2 = this.borderDrawable;
        if (borderDrawable2 != null) {
            borderDrawable2.setBorderTint(tint);
        }
    }

    /* access modifiers changed from: package-private */
    public void setBackgroundTintMode(PorterDuff.Mode tintMode) {
        MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setTintMode(tintMode);
        }
    }

    /* access modifiers changed from: package-private */
    public final void setElevation(float elevation2) {
        if (this.elevation != elevation2) {
            this.elevation = elevation2;
            onElevationsChanged(elevation2, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
        }
    }

    /* access modifiers changed from: package-private */
    public void setEnsureMinTouchTargetSize(boolean flag) {
        this.ensureMinTouchTargetSize = flag;
    }

    /* access modifiers changed from: package-private */
    public final void setHideMotionSpec(MotionSpec spec) {
        this.hideMotionSpec = spec;
    }

    /* access modifiers changed from: package-private */
    public final void setHoveredFocusedTranslationZ(float translationZ) {
        if (this.hoveredFocusedTranslationZ != translationZ) {
            this.hoveredFocusedTranslationZ = translationZ;
            onElevationsChanged(this.elevation, translationZ, this.pressedTranslationZ);
        }
    }

    /* access modifiers changed from: package-private */
    public final void setImageMatrixScale(float scale) {
        this.imageMatrixScale = scale;
        Matrix matrix = this.tmpMatrix;
        calculateImageMatrixFromScale(scale, matrix);
        this.view.setImageMatrix(matrix);
    }

    /* access modifiers changed from: package-private */
    public final void setMaxImageSize(int maxImageSize2) {
        if (this.maxImageSize != maxImageSize2) {
            this.maxImageSize = maxImageSize2;
            updateImageMatrixScale();
        }
    }

    /* access modifiers changed from: package-private */
    public void setMinTouchTargetSize(int minTouchTargetSize2) {
        this.minTouchTargetSize = minTouchTargetSize2;
    }

    /* access modifiers changed from: package-private */
    public final void setPressedTranslationZ(float translationZ) {
        if (this.pressedTranslationZ != translationZ) {
            this.pressedTranslationZ = translationZ;
            onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, translationZ);
        }
    }

    /* access modifiers changed from: package-private */
    public void setRippleColor(ColorStateList rippleColor) {
        Drawable drawable = this.rippleDrawable;
        if (drawable != null) {
            DrawableCompat.setTintList(drawable, RippleUtils.sanitizeRippleDrawableColor(rippleColor));
        }
    }

    /* access modifiers changed from: package-private */
    public void setShadowPaddingEnabled(boolean shadowPaddingEnabled2) {
        this.shadowPaddingEnabled = shadowPaddingEnabled2;
        updatePadding();
    }

    /* access modifiers changed from: package-private */
    public final void setShapeAppearance(ShapeAppearanceModel shapeAppearance2) {
        this.shapeAppearance = shapeAppearance2;
        MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setShapeAppearanceModel(shapeAppearance2);
        }
        Drawable drawable = this.rippleDrawable;
        if (drawable instanceof Shapeable) {
            ((Shapeable) drawable).setShapeAppearanceModel(shapeAppearance2);
        }
        BorderDrawable borderDrawable2 = this.borderDrawable;
        if (borderDrawable2 != null) {
            borderDrawable2.setShapeAppearanceModel(shapeAppearance2);
        }
    }

    /* access modifiers changed from: package-private */
    public final void setShowMotionSpec(MotionSpec spec) {
        this.showMotionSpec = spec;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldAddPadding() {
        return true;
    }

    /* access modifiers changed from: package-private */
    public final boolean shouldExpandBoundsForA11y() {
        return !this.ensureMinTouchTargetSize || this.view.getSizeDimension() >= this.minTouchTargetSize;
    }

    /* access modifiers changed from: package-private */
    public void show(final InternalVisibilityChangedListener listener, final boolean fromUser) {
        if (!isOrWillBeShown()) {
            Animator animator = this.currentAnimator;
            if (animator != null) {
                animator.cancel();
            }
            boolean z = this.showMotionSpec == null;
            if (shouldAnimateVisibilityChange()) {
                if (this.view.getVisibility() != 0) {
                    float f = 0.0f;
                    this.view.setAlpha(0.0f);
                    this.view.setScaleY(z ? 0.4f : 0.0f);
                    this.view.setScaleX(z ? 0.4f : 0.0f);
                    if (z) {
                        f = 0.4f;
                    }
                    setImageMatrixScale(f);
                }
                MotionSpec motionSpec = this.showMotionSpec;
                AnimatorSet createAnimator = motionSpec != null ? createAnimator(motionSpec, 1.0f, 1.0f, 1.0f) : createDefaultAnimator(1.0f, 1.0f, 1.0f);
                createAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        int unused = FloatingActionButtonImpl.this.animState = 0;
                        Animator unused2 = FloatingActionButtonImpl.this.currentAnimator = null;
                        InternalVisibilityChangedListener internalVisibilityChangedListener = listener;
                        if (internalVisibilityChangedListener != null) {
                            internalVisibilityChangedListener.onShown();
                        }
                    }

                    public void onAnimationStart(Animator animation) {
                        FloatingActionButtonImpl.this.view.internalSetVisibility(0, fromUser);
                        int unused = FloatingActionButtonImpl.this.animState = 2;
                        Animator unused2 = FloatingActionButtonImpl.this.currentAnimator = animation;
                    }
                });
                ArrayList<Animator.AnimatorListener> arrayList = this.showListeners;
                if (arrayList != null) {
                    Iterator<Animator.AnimatorListener> it = arrayList.iterator();
                    while (it.hasNext()) {
                        createAnimator.addListener(it.next());
                    }
                }
                createAnimator.start();
                return;
            }
            this.view.internalSetVisibility(0, fromUser);
            this.view.setAlpha(1.0f);
            this.view.setScaleY(1.0f);
            this.view.setScaleX(1.0f);
            setImageMatrixScale(1.0f);
            if (listener != null) {
                listener.onShown();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void updateFromViewRotation() {
        if (Build.VERSION.SDK_INT == 19) {
            if (this.rotation % 90.0f != 0.0f) {
                if (this.view.getLayerType() != 1) {
                    this.view.setLayerType(1, (Paint) null);
                }
            } else if (this.view.getLayerType() != 0) {
                this.view.setLayerType(0, (Paint) null);
            }
        }
        MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setShadowCompatRotation((int) this.rotation);
        }
    }

    /* access modifiers changed from: package-private */
    public final void updateImageMatrixScale() {
        setImageMatrixScale(this.imageMatrixScale);
    }

    /* access modifiers changed from: package-private */
    public final void updatePadding() {
        Rect rect = this.tmpRect;
        getPadding(rect);
        onPaddingUpdated(rect);
        this.shadowViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
    }

    /* access modifiers changed from: package-private */
    public void updateShapeElevation(float elevation2) {
        MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setElevation(elevation2);
        }
    }
}

package com.google.android.material.transition;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.core.util.Preconditions;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;
import androidx.transition.ArcMotion;
import androidx.transition.PathMotion;
import androidx.transition.Transition;
import androidx.transition.TransitionValues;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import com.google.android.material.transition.TransitionUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class MaterialContainerTransform extends Transition {
    private static final ProgressThresholdsGroup DEFAULT_ENTER_THRESHOLDS = new ProgressThresholdsGroup(new ProgressThresholds(0.0f, 0.25f), new ProgressThresholds(0.0f, 1.0f), new ProgressThresholds(0.0f, 1.0f), new ProgressThresholds(0.0f, 0.75f));
    private static final ProgressThresholdsGroup DEFAULT_ENTER_THRESHOLDS_ARC = new ProgressThresholdsGroup(new ProgressThresholds(0.1f, 0.4f), new ProgressThresholds(0.1f, 1.0f), new ProgressThresholds(0.1f, 1.0f), new ProgressThresholds(0.1f, 0.9f));
    private static final ProgressThresholdsGroup DEFAULT_RETURN_THRESHOLDS = new ProgressThresholdsGroup(new ProgressThresholds(0.6f, 0.9f), new ProgressThresholds(0.0f, 1.0f), new ProgressThresholds(0.0f, 0.9f), new ProgressThresholds(0.3f, 0.9f));
    private static final ProgressThresholdsGroup DEFAULT_RETURN_THRESHOLDS_ARC = new ProgressThresholdsGroup(new ProgressThresholds(0.6f, 0.9f), new ProgressThresholds(0.0f, 0.9f), new ProgressThresholds(0.0f, 0.9f), new ProgressThresholds(0.2f, 0.9f));
    private static final float ELEVATION_NOT_SET = -1.0f;
    public static final int FADE_MODE_CROSS = 2;
    public static final int FADE_MODE_IN = 0;
    public static final int FADE_MODE_OUT = 1;
    public static final int FADE_MODE_THROUGH = 3;
    public static final int FIT_MODE_AUTO = 0;
    public static final int FIT_MODE_HEIGHT = 2;
    public static final int FIT_MODE_WIDTH = 1;
    private static final String PROP_BOUNDS = "materialContainerTransition:bounds";
    private static final String PROP_SHAPE_APPEARANCE = "materialContainerTransition:shapeAppearance";
    private static final String TAG = MaterialContainerTransform.class.getSimpleName();
    public static final int TRANSITION_DIRECTION_AUTO = 0;
    public static final int TRANSITION_DIRECTION_ENTER = 1;
    public static final int TRANSITION_DIRECTION_RETURN = 2;
    private static final String[] TRANSITION_PROPS = {PROP_BOUNDS, PROP_SHAPE_APPEARANCE};
    private boolean appliedThemeValues = false;
    private int containerColor = 0;
    private boolean drawDebugEnabled = false;
    private int drawingViewId = 16908290;
    private boolean elevationShadowEnabled;
    private int endContainerColor = 0;
    private float endElevation;
    private ShapeAppearanceModel endShapeAppearanceModel;
    private View endView;
    private int endViewId = -1;
    private int fadeMode = 0;
    private ProgressThresholds fadeProgressThresholds;
    private int fitMode = 0;
    /* access modifiers changed from: private */
    public boolean holdAtEndEnabled = false;
    private boolean pathMotionCustom = false;
    private ProgressThresholds scaleMaskProgressThresholds;
    private ProgressThresholds scaleProgressThresholds;
    private int scrimColor = 1375731712;
    private ProgressThresholds shapeMaskProgressThresholds;
    private int startContainerColor = 0;
    private float startElevation;
    private ShapeAppearanceModel startShapeAppearanceModel;
    private View startView;
    private int startViewId = -1;
    private int transitionDirection = 0;

    @Retention(RetentionPolicy.SOURCE)
    public @interface FadeMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FitMode {
    }

    public static class ProgressThresholds {
        /* access modifiers changed from: private */
        public final float end;
        /* access modifiers changed from: private */
        public final float start;

        public ProgressThresholds(float start2, float end2) {
            this.start = start2;
            this.end = end2;
        }

        public float getEnd() {
            return this.end;
        }

        public float getStart() {
            return this.start;
        }
    }

    private static class ProgressThresholdsGroup {
        /* access modifiers changed from: private */
        public final ProgressThresholds fade;
        /* access modifiers changed from: private */
        public final ProgressThresholds scale;
        /* access modifiers changed from: private */
        public final ProgressThresholds scaleMask;
        /* access modifiers changed from: private */
        public final ProgressThresholds shapeMask;

        private ProgressThresholdsGroup(ProgressThresholds fade2, ProgressThresholds scale2, ProgressThresholds scaleMask2, ProgressThresholds shapeMask2) {
            this.fade = fade2;
            this.scale = scale2;
            this.scaleMask = scaleMask2;
            this.shapeMask = shapeMask2;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TransitionDirection {
    }

    private static final class TransitionDrawable extends Drawable {
        private static final int COMPAT_SHADOW_COLOR = -7829368;
        private static final int SHADOW_COLOR = 754974720;
        private static final float SHADOW_DX_MULTIPLIER_ADJUSTMENT = 0.3f;
        private static final float SHADOW_DY_MULTIPLIER_ADJUSTMENT = 1.5f;
        private final MaterialShapeDrawable compatShadowDrawable;
        private final Paint containerPaint;
        private float currentElevation;
        private float currentElevationDy;
        private final RectF currentEndBounds;
        private final RectF currentEndBoundsMasked;
        private RectF currentMaskBounds;
        private final RectF currentStartBounds;
        private final RectF currentStartBoundsMasked;
        private final Paint debugPaint;
        private final Path debugPath;
        private final float displayHeight;
        private final float displayWidth;
        private final boolean drawDebugEnabled;
        private final boolean elevationShadowEnabled;
        private final RectF endBounds;
        private final Paint endContainerPaint;
        private final float endElevation;
        private final ShapeAppearanceModel endShapeAppearanceModel;
        /* access modifiers changed from: private */
        public final View endView;
        private final boolean entering;
        private final FadeModeEvaluator fadeModeEvaluator;
        private FadeModeResult fadeModeResult;
        private final FitModeEvaluator fitModeEvaluator;
        private FitModeResult fitModeResult;
        private final MaskEvaluator maskEvaluator;
        private final float motionPathLength;
        private final PathMeasure motionPathMeasure;
        private final float[] motionPathPosition;
        private float progress;
        private final ProgressThresholdsGroup progressThresholds;
        private final Paint scrimPaint;
        private final Paint shadowPaint;
        private final RectF startBounds;
        private final Paint startContainerPaint;
        private final float startElevation;
        private final ShapeAppearanceModel startShapeAppearanceModel;
        /* access modifiers changed from: private */
        public final View startView;

        private TransitionDrawable(PathMotion pathMotion, View startView2, RectF startBounds2, ShapeAppearanceModel startShapeAppearanceModel2, float startElevation2, View endView2, RectF endBounds2, ShapeAppearanceModel endShapeAppearanceModel2, float endElevation2, int containerColor, int startContainerColor, int endContainerColor, int scrimColor, boolean entering2, boolean elevationShadowEnabled2, FadeModeEvaluator fadeModeEvaluator2, FitModeEvaluator fitModeEvaluator2, ProgressThresholdsGroup progressThresholds2, boolean drawDebugEnabled2) {
            RectF rectF = startBounds2;
            Paint paint = new Paint();
            this.containerPaint = paint;
            Paint paint2 = new Paint();
            this.startContainerPaint = paint2;
            Paint paint3 = new Paint();
            this.endContainerPaint = paint3;
            this.shadowPaint = new Paint();
            Paint paint4 = new Paint();
            this.scrimPaint = paint4;
            this.maskEvaluator = new MaskEvaluator();
            float[] fArr = new float[2];
            this.motionPathPosition = fArr;
            MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
            this.compatShadowDrawable = materialShapeDrawable;
            Paint paint5 = new Paint();
            this.debugPaint = paint5;
            this.debugPath = new Path();
            this.startView = startView2;
            this.startBounds = rectF;
            this.startShapeAppearanceModel = startShapeAppearanceModel2;
            this.startElevation = startElevation2;
            this.endView = endView2;
            this.endBounds = endBounds2;
            this.endShapeAppearanceModel = endShapeAppearanceModel2;
            this.endElevation = endElevation2;
            this.entering = entering2;
            this.elevationShadowEnabled = elevationShadowEnabled2;
            this.fadeModeEvaluator = fadeModeEvaluator2;
            this.fitModeEvaluator = fitModeEvaluator2;
            this.progressThresholds = progressThresholds2;
            this.drawDebugEnabled = drawDebugEnabled2;
            WindowManager windowManager = (WindowManager) startView2.getContext().getSystemService("window");
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            this.displayWidth = (float) displayMetrics.widthPixels;
            this.displayHeight = (float) displayMetrics.heightPixels;
            paint.setColor(containerColor);
            paint2.setColor(startContainerColor);
            paint3.setColor(endContainerColor);
            materialShapeDrawable.setFillColor(ColorStateList.valueOf(0));
            materialShapeDrawable.setShadowCompatibilityMode(2);
            materialShapeDrawable.setShadowBitmapDrawingEnable(false);
            materialShapeDrawable.setShadowColor(COMPAT_SHADOW_COLOR);
            RectF rectF2 = new RectF(rectF);
            this.currentStartBounds = rectF2;
            this.currentStartBoundsMasked = new RectF(rectF2);
            RectF rectF3 = new RectF(rectF2);
            this.currentEndBounds = rectF3;
            this.currentEndBoundsMasked = new RectF(rectF3);
            PointF motionPathPoint = getMotionPathPoint(startBounds2);
            PointF motionPathPoint2 = getMotionPathPoint(endBounds2);
            PointF pointF = motionPathPoint;
            WindowManager windowManager2 = windowManager;
            PointF pointF2 = motionPathPoint2;
            PathMeasure pathMeasure = new PathMeasure(pathMotion.getPath(motionPathPoint.x, motionPathPoint.y, motionPathPoint2.x, motionPathPoint2.y), false);
            this.motionPathMeasure = pathMeasure;
            this.motionPathLength = pathMeasure.getLength();
            fArr[0] = startBounds2.centerX();
            fArr[1] = rectF.top;
            paint4.setStyle(Paint.Style.FILL);
            paint4.setShader(TransitionUtils.createColorShader(scrimColor));
            paint5.setStyle(Paint.Style.STROKE);
            paint5.setStrokeWidth(10.0f);
            updateProgress(0.0f);
        }

        private static float calculateElevationDxMultiplier(RectF bounds, float displayWidth2) {
            return ((bounds.centerX() / (displayWidth2 / 2.0f)) - 1.0f) * SHADOW_DX_MULTIPLIER_ADJUSTMENT;
        }

        private static float calculateElevationDyMultiplier(RectF bounds, float displayHeight2) {
            return (bounds.centerY() / displayHeight2) * SHADOW_DY_MULTIPLIER_ADJUSTMENT;
        }

        private void drawDebugCumulativePath(Canvas canvas, RectF bounds, Path path, int color) {
            PointF motionPathPoint = getMotionPathPoint(bounds);
            if (this.progress == 0.0f) {
                path.reset();
                path.moveTo(motionPathPoint.x, motionPathPoint.y);
                return;
            }
            path.lineTo(motionPathPoint.x, motionPathPoint.y);
            this.debugPaint.setColor(color);
            canvas.drawPath(path, this.debugPaint);
        }

        private void drawDebugRect(Canvas canvas, RectF bounds, int color) {
            this.debugPaint.setColor(color);
            canvas.drawRect(bounds, this.debugPaint);
        }

        private void drawElevationShadow(Canvas canvas) {
            canvas.save();
            canvas.clipPath(this.maskEvaluator.getPath(), Region.Op.DIFFERENCE);
            if (Build.VERSION.SDK_INT > 28) {
                drawElevationShadowWithPaintShadowLayer(canvas);
            } else {
                drawElevationShadowWithMaterialShapeDrawable(canvas);
            }
            canvas.restore();
        }

        private void drawElevationShadowWithMaterialShapeDrawable(Canvas canvas) {
            this.compatShadowDrawable.setBounds((int) this.currentMaskBounds.left, (int) this.currentMaskBounds.top, (int) this.currentMaskBounds.right, (int) this.currentMaskBounds.bottom);
            this.compatShadowDrawable.setElevation(this.currentElevation);
            this.compatShadowDrawable.setShadowVerticalOffset((int) this.currentElevationDy);
            this.compatShadowDrawable.setShapeAppearanceModel(this.maskEvaluator.getCurrentShapeAppearanceModel());
            this.compatShadowDrawable.draw(canvas);
        }

        private void drawElevationShadowWithPaintShadowLayer(Canvas canvas) {
            ShapeAppearanceModel currentShapeAppearanceModel = this.maskEvaluator.getCurrentShapeAppearanceModel();
            if (currentShapeAppearanceModel.isRoundRect(this.currentMaskBounds)) {
                float cornerSize = currentShapeAppearanceModel.getTopLeftCornerSize().getCornerSize(this.currentMaskBounds);
                canvas.drawRoundRect(this.currentMaskBounds, cornerSize, cornerSize, this.shadowPaint);
                return;
            }
            canvas.drawPath(this.maskEvaluator.getPath(), this.shadowPaint);
        }

        private void drawEndView(Canvas canvas) {
            maybeDrawContainerColor(canvas, this.endContainerPaint);
            TransitionUtils.transform(canvas, getBounds(), this.currentEndBounds.left, this.currentEndBounds.top, this.fitModeResult.endScale, this.fadeModeResult.endAlpha, new TransitionUtils.CanvasOperation() {
                public void run(Canvas canvas) {
                    TransitionDrawable.this.endView.draw(canvas);
                }
            });
        }

        private void drawStartView(Canvas canvas) {
            maybeDrawContainerColor(canvas, this.startContainerPaint);
            TransitionUtils.transform(canvas, getBounds(), this.currentStartBounds.left, this.currentStartBounds.top, this.fitModeResult.startScale, this.fadeModeResult.startAlpha, new TransitionUtils.CanvasOperation() {
                public void run(Canvas canvas) {
                    TransitionDrawable.this.startView.draw(canvas);
                }
            });
        }

        private static PointF getMotionPathPoint(RectF bounds) {
            return new PointF(bounds.centerX(), bounds.top);
        }

        private void maybeDrawContainerColor(Canvas canvas, Paint containerPaint2) {
            if (containerPaint2.getColor() != 0 && containerPaint2.getAlpha() > 0) {
                canvas.drawRect(getBounds(), containerPaint2);
            }
        }

        /* access modifiers changed from: private */
        public void setProgress(float progress2) {
            if (this.progress != progress2) {
                updateProgress(progress2);
            }
        }

        private void updateProgress(float progress2) {
            float f;
            float f2;
            float f3;
            float f4;
            float f5 = progress2;
            this.progress = f5;
            this.scrimPaint.setAlpha((int) (this.entering ? TransitionUtils.lerp(0.0f, 255.0f, f5) : TransitionUtils.lerp(255.0f, 0.0f, f5)));
            this.motionPathMeasure.getPosTan(this.motionPathLength * f5, this.motionPathPosition, (float[]) null);
            float[] fArr = this.motionPathPosition;
            float f6 = fArr[0];
            float f7 = fArr[1];
            if (f5 > 1.0f || f5 < 0.0f) {
                if (f5 > 1.0f) {
                    f4 = 0.99f;
                    f3 = (f5 - 1.0f) / (1.0f - 0.99f);
                } else {
                    f4 = 0.01f;
                    f3 = (f5 / 0.01f) * MaterialContainerTransform.ELEVATION_NOT_SET;
                }
                this.motionPathMeasure.getPosTan(this.motionPathLength * f4, fArr, (float[]) null);
                float[] fArr2 = this.motionPathPosition;
                f2 = f6 + ((f6 - fArr2[0]) * f3);
                f = f7 + ((f7 - fArr2[1]) * f3);
            } else {
                f2 = f6;
                f = f7;
            }
            FitModeResult evaluate = this.fitModeEvaluator.evaluate(progress2, ((Float) Preconditions.checkNotNull(Float.valueOf(this.progressThresholds.scale.start))).floatValue(), ((Float) Preconditions.checkNotNull(Float.valueOf(this.progressThresholds.scale.end))).floatValue(), this.startBounds.width(), this.startBounds.height(), this.endBounds.width(), this.endBounds.height());
            this.fitModeResult = evaluate;
            this.currentStartBounds.set(f2 - (evaluate.currentStartWidth / 2.0f), f, (this.fitModeResult.currentStartWidth / 2.0f) + f2, this.fitModeResult.currentStartHeight + f);
            this.currentEndBounds.set(f2 - (this.fitModeResult.currentEndWidth / 2.0f), f, (this.fitModeResult.currentEndWidth / 2.0f) + f2, this.fitModeResult.currentEndHeight + f);
            this.currentStartBoundsMasked.set(this.currentStartBounds);
            this.currentEndBoundsMasked.set(this.currentEndBounds);
            float floatValue = ((Float) Preconditions.checkNotNull(Float.valueOf(this.progressThresholds.scaleMask.start))).floatValue();
            float floatValue2 = ((Float) Preconditions.checkNotNull(Float.valueOf(this.progressThresholds.scaleMask.end))).floatValue();
            boolean shouldMaskStartBounds = this.fitModeEvaluator.shouldMaskStartBounds(this.fitModeResult);
            RectF rectF = shouldMaskStartBounds ? this.currentStartBoundsMasked : this.currentEndBoundsMasked;
            float lerp = TransitionUtils.lerp(0.0f, 1.0f, floatValue, floatValue2, f5);
            this.fitModeEvaluator.applyMask(rectF, shouldMaskStartBounds ? lerp : 1.0f - lerp, this.fitModeResult);
            RectF rectF2 = rectF;
            this.currentMaskBounds = new RectF(Math.min(this.currentStartBoundsMasked.left, this.currentEndBoundsMasked.left), Math.min(this.currentStartBoundsMasked.top, this.currentEndBoundsMasked.top), Math.max(this.currentStartBoundsMasked.right, this.currentEndBoundsMasked.right), Math.max(this.currentStartBoundsMasked.bottom, this.currentEndBoundsMasked.bottom));
            float f8 = floatValue2;
            float f9 = floatValue;
            this.maskEvaluator.evaluate(progress2, this.startShapeAppearanceModel, this.endShapeAppearanceModel, this.currentStartBounds, this.currentStartBoundsMasked, this.currentEndBoundsMasked, this.progressThresholds.shapeMask);
            this.currentElevation = TransitionUtils.lerp(this.startElevation, this.endElevation, f5);
            float calculateElevationDxMultiplier = calculateElevationDxMultiplier(this.currentMaskBounds, this.displayWidth);
            float calculateElevationDyMultiplier = calculateElevationDyMultiplier(this.currentMaskBounds, this.displayHeight);
            float f10 = this.currentElevation;
            float f11 = (float) ((int) (f10 * calculateElevationDyMultiplier));
            this.currentElevationDy = f11;
            this.shadowPaint.setShadowLayer(f10, (float) ((int) (f10 * calculateElevationDxMultiplier)), f11, SHADOW_COLOR);
            this.fadeModeResult = this.fadeModeEvaluator.evaluate(f5, ((Float) Preconditions.checkNotNull(Float.valueOf(this.progressThresholds.fade.start))).floatValue(), ((Float) Preconditions.checkNotNull(Float.valueOf(this.progressThresholds.fade.end))).floatValue(), 0.35f);
            if (this.startContainerPaint.getColor() != 0) {
                this.startContainerPaint.setAlpha(this.fadeModeResult.startAlpha);
            }
            if (this.endContainerPaint.getColor() != 0) {
                this.endContainerPaint.setAlpha(this.fadeModeResult.endAlpha);
            }
            invalidateSelf();
        }

        public void draw(Canvas canvas) {
            if (this.scrimPaint.getAlpha() > 0) {
                canvas.drawRect(getBounds(), this.scrimPaint);
            }
            int save = this.drawDebugEnabled ? canvas.save() : -1;
            if (this.elevationShadowEnabled && this.currentElevation > 0.0f) {
                drawElevationShadow(canvas);
            }
            this.maskEvaluator.clip(canvas);
            maybeDrawContainerColor(canvas, this.containerPaint);
            if (this.fadeModeResult.endOnTop) {
                drawStartView(canvas);
                drawEndView(canvas);
            } else {
                drawEndView(canvas);
                drawStartView(canvas);
            }
            if (this.drawDebugEnabled) {
                canvas.restoreToCount(save);
                drawDebugCumulativePath(canvas, this.currentStartBounds, this.debugPath, -65281);
                drawDebugRect(canvas, this.currentStartBoundsMasked, InputDeviceCompat.SOURCE_ANY);
                drawDebugRect(canvas, this.currentStartBounds, -16711936);
                drawDebugRect(canvas, this.currentEndBoundsMasked, -16711681);
                drawDebugRect(canvas, this.currentEndBounds, -16776961);
            }
        }

        public int getOpacity() {
            return -3;
        }

        public void setAlpha(int alpha) {
            throw new UnsupportedOperationException("Setting alpha on is not supported");
        }

        public void setColorFilter(ColorFilter colorFilter) {
            throw new UnsupportedOperationException("Setting a color filter is not supported");
        }
    }

    public MaterialContainerTransform() {
        boolean z = false;
        this.elevationShadowEnabled = Build.VERSION.SDK_INT >= 28 ? true : z;
        this.startElevation = ELEVATION_NOT_SET;
        this.endElevation = ELEVATION_NOT_SET;
    }

    public MaterialContainerTransform(Context context, boolean entering) {
        boolean z = false;
        this.elevationShadowEnabled = Build.VERSION.SDK_INT >= 28 ? true : z;
        this.startElevation = ELEVATION_NOT_SET;
        this.endElevation = ELEVATION_NOT_SET;
        maybeApplyThemeValues(context, entering);
        this.appliedThemeValues = true;
    }

    private ProgressThresholdsGroup buildThresholdsGroup(boolean entering) {
        PathMotion pathMotion = getPathMotion();
        return ((pathMotion instanceof ArcMotion) || (pathMotion instanceof MaterialArcMotion)) ? getThresholdsOrDefault(entering, DEFAULT_ENTER_THRESHOLDS_ARC, DEFAULT_RETURN_THRESHOLDS_ARC) : getThresholdsOrDefault(entering, DEFAULT_ENTER_THRESHOLDS, DEFAULT_RETURN_THRESHOLDS);
    }

    private static RectF calculateDrawableBounds(View drawingView, View boundingView, float offsetX, float offsetY) {
        if (boundingView == null) {
            return new RectF(0.0f, 0.0f, (float) drawingView.getWidth(), (float) drawingView.getHeight());
        }
        RectF locationOnScreen = TransitionUtils.getLocationOnScreen(boundingView);
        locationOnScreen.offset(offsetX, offsetY);
        return locationOnScreen;
    }

    private static ShapeAppearanceModel captureShapeAppearance(View view, RectF bounds, ShapeAppearanceModel shapeAppearanceModelOverride) {
        return TransitionUtils.convertToRelativeCornerSizes(getShapeAppearance(view, shapeAppearanceModelOverride), bounds);
    }

    private static void captureValues(TransitionValues transitionValues, View viewOverride, int viewIdOverride, ShapeAppearanceModel shapeAppearanceModelOverride) {
        if (viewIdOverride != -1) {
            transitionValues.view = TransitionUtils.findDescendantOrAncestorById(transitionValues.view, viewIdOverride);
        } else if (viewOverride != null) {
            transitionValues.view = viewOverride;
        } else if (transitionValues.view.getTag(R.id.mtrl_motion_snapshot_view) instanceof View) {
            transitionValues.view.setTag(R.id.mtrl_motion_snapshot_view, (Object) null);
            transitionValues.view = (View) transitionValues.view.getTag(R.id.mtrl_motion_snapshot_view);
        }
        View view = transitionValues.view;
        if (ViewCompat.isLaidOut(view) || view.getWidth() != 0 || view.getHeight() != 0) {
            RectF relativeBounds = view.getParent() == null ? TransitionUtils.getRelativeBounds(view) : TransitionUtils.getLocationOnScreen(view);
            transitionValues.values.put(PROP_BOUNDS, relativeBounds);
            transitionValues.values.put(PROP_SHAPE_APPEARANCE, captureShapeAppearance(view, relativeBounds, shapeAppearanceModelOverride));
        }
    }

    private static float getElevationOrDefault(float elevation, View view) {
        return elevation != ELEVATION_NOT_SET ? elevation : ViewCompat.getElevation(view);
    }

    private static ShapeAppearanceModel getShapeAppearance(View view, ShapeAppearanceModel shapeAppearanceModelOverride) {
        if (shapeAppearanceModelOverride != null) {
            return shapeAppearanceModelOverride;
        }
        if (view.getTag(R.id.mtrl_motion_snapshot_view) instanceof ShapeAppearanceModel) {
            return (ShapeAppearanceModel) view.getTag(R.id.mtrl_motion_snapshot_view);
        }
        Context context = view.getContext();
        int transitionShapeAppearanceResId = getTransitionShapeAppearanceResId(context);
        return transitionShapeAppearanceResId != -1 ? ShapeAppearanceModel.builder(context, transitionShapeAppearanceResId, 0).build() : view instanceof Shapeable ? ((Shapeable) view).getShapeAppearanceModel() : ShapeAppearanceModel.builder().build();
    }

    private ProgressThresholdsGroup getThresholdsOrDefault(boolean entering, ProgressThresholdsGroup defaultEnterThresholds, ProgressThresholdsGroup defaultReturnThresholds) {
        ProgressThresholdsGroup progressThresholdsGroup = entering ? defaultEnterThresholds : defaultReturnThresholds;
        return new ProgressThresholdsGroup((ProgressThresholds) TransitionUtils.defaultIfNull(this.fadeProgressThresholds, progressThresholdsGroup.fade), (ProgressThresholds) TransitionUtils.defaultIfNull(this.scaleProgressThresholds, progressThresholdsGroup.scale), (ProgressThresholds) TransitionUtils.defaultIfNull(this.scaleMaskProgressThresholds, progressThresholdsGroup.scaleMask), (ProgressThresholds) TransitionUtils.defaultIfNull(this.shapeMaskProgressThresholds, progressThresholdsGroup.shapeMask));
    }

    private static int getTransitionShapeAppearanceResId(Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{R.attr.transitionShapeAppearance});
        int resourceId = obtainStyledAttributes.getResourceId(0, -1);
        obtainStyledAttributes.recycle();
        return resourceId;
    }

    private boolean isEntering(RectF startBounds, RectF endBounds) {
        switch (this.transitionDirection) {
            case 0:
                return TransitionUtils.calculateArea(endBounds) > TransitionUtils.calculateArea(startBounds);
            case 1:
                return true;
            case 2:
                return false;
            default:
                throw new IllegalArgumentException("Invalid transition direction: " + this.transitionDirection);
        }
    }

    private void maybeApplyThemeValues(Context context, boolean entering) {
        TransitionUtils.maybeApplyThemeInterpolator(this, context, R.attr.motionEasingStandard, AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        TransitionUtils.maybeApplyThemeDuration(this, context, entering ? R.attr.motionDurationLong1 : R.attr.motionDurationMedium2);
        if (!this.pathMotionCustom) {
            TransitionUtils.maybeApplyThemePath(this, context, R.attr.motionPath);
        }
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues, this.endView, this.endViewId, this.endShapeAppearanceModel);
    }

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues, this.startView, this.startViewId, this.startShapeAppearanceModel);
    }

    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        View view;
        View view2;
        TransitionValues transitionValues = startValues;
        TransitionValues transitionValues2 = endValues;
        if (transitionValues == null || transitionValues2 == null) {
            return null;
        }
        RectF rectF = (RectF) transitionValues.values.get(PROP_BOUNDS);
        ShapeAppearanceModel shapeAppearanceModel = (ShapeAppearanceModel) transitionValues.values.get(PROP_SHAPE_APPEARANCE);
        if (rectF == null) {
            RectF rectF2 = rectF;
        } else if (shapeAppearanceModel == null) {
            RectF rectF3 = rectF;
        } else {
            RectF rectF4 = (RectF) transitionValues2.values.get(PROP_BOUNDS);
            ShapeAppearanceModel shapeAppearanceModel2 = (ShapeAppearanceModel) transitionValues2.values.get(PROP_SHAPE_APPEARANCE);
            if (rectF4 == null) {
                RectF rectF5 = rectF4;
                RectF rectF6 = rectF;
            } else if (shapeAppearanceModel2 == null) {
                RectF rectF7 = rectF4;
                RectF rectF8 = rectF;
            } else {
                View view3 = transitionValues.view;
                View view4 = transitionValues2.view;
                View view5 = view4.getParent() != null ? view4 : view3;
                if (this.drawingViewId == view5.getId()) {
                    view2 = (View) view5.getParent();
                    view = view5;
                } else {
                    view2 = TransitionUtils.findAncestorById(view5, this.drawingViewId);
                    view = null;
                }
                RectF locationOnScreen = TransitionUtils.getLocationOnScreen(view2);
                float f = -locationOnScreen.left;
                float f2 = -locationOnScreen.top;
                RectF calculateDrawableBounds = calculateDrawableBounds(view2, view, f, f2);
                rectF.offset(f, f2);
                rectF4.offset(f, f2);
                boolean isEntering = isEntering(rectF, rectF4);
                if (!this.appliedThemeValues) {
                    maybeApplyThemeValues(view5.getContext(), isEntering);
                }
                boolean z = isEntering;
                RectF rectF9 = calculateDrawableBounds;
                float f3 = f2;
                float f4 = f;
                RectF rectF10 = locationOnScreen;
                View view6 = view;
                final TransitionDrawable transitionDrawable = new TransitionDrawable(getPathMotion(), view3, rectF, shapeAppearanceModel, getElevationOrDefault(this.startElevation, view3), view4, rectF4, shapeAppearanceModel2, getElevationOrDefault(this.endElevation, view4), this.containerColor, this.startContainerColor, this.endContainerColor, this.scrimColor, z, this.elevationShadowEnabled, FadeModeEvaluators.get(this.fadeMode, z), FitModeEvaluators.get(this.fitMode, z, rectF, rectF4), buildThresholdsGroup(z), this.drawDebugEnabled);
                transitionDrawable.setBounds(Math.round(rectF9.left), Math.round(rectF9.top), Math.round(rectF9.right), Math.round(rectF9.bottom));
                ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        transitionDrawable.setProgress(animation.getAnimatedFraction());
                    }
                });
                View view7 = view5;
                View view8 = view4;
                final View view9 = view2;
                View view10 = view3;
                final TransitionDrawable transitionDrawable2 = transitionDrawable;
                final View view11 = view10;
                RectF rectF11 = rectF;
                final View view12 = view8;
                addListener(new TransitionListenerAdapter() {
                    public void onTransitionEnd(Transition transition) {
                        MaterialContainerTransform.this.removeListener(this);
                        if (!MaterialContainerTransform.this.holdAtEndEnabled) {
                            view11.setAlpha(1.0f);
                            view12.setAlpha(1.0f);
                            ViewUtils.getOverlay(view9).remove(transitionDrawable2);
                        }
                    }

                    public void onTransitionStart(Transition transition) {
                        ViewUtils.getOverlay(view9).add(transitionDrawable2);
                        view11.setAlpha(0.0f);
                        view12.setAlpha(0.0f);
                    }
                });
                return ofFloat;
            }
            Log.w(TAG, "Skipping due to null end bounds. Ensure end view is laid out and measured.");
            return null;
        }
        Log.w(TAG, "Skipping due to null start bounds. Ensure start view is laid out and measured.");
        return null;
    }

    public int getContainerColor() {
        return this.containerColor;
    }

    public int getDrawingViewId() {
        return this.drawingViewId;
    }

    public int getEndContainerColor() {
        return this.endContainerColor;
    }

    public float getEndElevation() {
        return this.endElevation;
    }

    public ShapeAppearanceModel getEndShapeAppearanceModel() {
        return this.endShapeAppearanceModel;
    }

    public View getEndView() {
        return this.endView;
    }

    public int getEndViewId() {
        return this.endViewId;
    }

    public int getFadeMode() {
        return this.fadeMode;
    }

    public ProgressThresholds getFadeProgressThresholds() {
        return this.fadeProgressThresholds;
    }

    public int getFitMode() {
        return this.fitMode;
    }

    public ProgressThresholds getScaleMaskProgressThresholds() {
        return this.scaleMaskProgressThresholds;
    }

    public ProgressThresholds getScaleProgressThresholds() {
        return this.scaleProgressThresholds;
    }

    public int getScrimColor() {
        return this.scrimColor;
    }

    public ProgressThresholds getShapeMaskProgressThresholds() {
        return this.shapeMaskProgressThresholds;
    }

    public int getStartContainerColor() {
        return this.startContainerColor;
    }

    public float getStartElevation() {
        return this.startElevation;
    }

    public ShapeAppearanceModel getStartShapeAppearanceModel() {
        return this.startShapeAppearanceModel;
    }

    public View getStartView() {
        return this.startView;
    }

    public int getStartViewId() {
        return this.startViewId;
    }

    public int getTransitionDirection() {
        return this.transitionDirection;
    }

    public String[] getTransitionProperties() {
        return TRANSITION_PROPS;
    }

    public boolean isDrawDebugEnabled() {
        return this.drawDebugEnabled;
    }

    public boolean isElevationShadowEnabled() {
        return this.elevationShadowEnabled;
    }

    public boolean isHoldAtEndEnabled() {
        return this.holdAtEndEnabled;
    }

    public void setAllContainerColors(int containerColor2) {
        this.containerColor = containerColor2;
        this.startContainerColor = containerColor2;
        this.endContainerColor = containerColor2;
    }

    public void setContainerColor(int containerColor2) {
        this.containerColor = containerColor2;
    }

    public void setDrawDebugEnabled(boolean drawDebugEnabled2) {
        this.drawDebugEnabled = drawDebugEnabled2;
    }

    public void setDrawingViewId(int drawingViewId2) {
        this.drawingViewId = drawingViewId2;
    }

    public void setElevationShadowEnabled(boolean elevationShadowEnabled2) {
        this.elevationShadowEnabled = elevationShadowEnabled2;
    }

    public void setEndContainerColor(int containerColor2) {
        this.endContainerColor = containerColor2;
    }

    public void setEndElevation(float endElevation2) {
        this.endElevation = endElevation2;
    }

    public void setEndShapeAppearanceModel(ShapeAppearanceModel endShapeAppearanceModel2) {
        this.endShapeAppearanceModel = endShapeAppearanceModel2;
    }

    public void setEndView(View endView2) {
        this.endView = endView2;
    }

    public void setEndViewId(int endViewId2) {
        this.endViewId = endViewId2;
    }

    public void setFadeMode(int fadeMode2) {
        this.fadeMode = fadeMode2;
    }

    public void setFadeProgressThresholds(ProgressThresholds fadeProgressThresholds2) {
        this.fadeProgressThresholds = fadeProgressThresholds2;
    }

    public void setFitMode(int fitMode2) {
        this.fitMode = fitMode2;
    }

    public void setHoldAtEndEnabled(boolean holdAtEndEnabled2) {
        this.holdAtEndEnabled = holdAtEndEnabled2;
    }

    public void setPathMotion(PathMotion pathMotion) {
        super.setPathMotion(pathMotion);
        this.pathMotionCustom = true;
    }

    public void setScaleMaskProgressThresholds(ProgressThresholds scaleMaskProgressThresholds2) {
        this.scaleMaskProgressThresholds = scaleMaskProgressThresholds2;
    }

    public void setScaleProgressThresholds(ProgressThresholds scaleProgressThresholds2) {
        this.scaleProgressThresholds = scaleProgressThresholds2;
    }

    public void setScrimColor(int scrimColor2) {
        this.scrimColor = scrimColor2;
    }

    public void setShapeMaskProgressThresholds(ProgressThresholds shapeMaskProgressThresholds2) {
        this.shapeMaskProgressThresholds = shapeMaskProgressThresholds2;
    }

    public void setStartContainerColor(int containerColor2) {
        this.startContainerColor = containerColor2;
    }

    public void setStartElevation(float startElevation2) {
        this.startElevation = startElevation2;
    }

    public void setStartShapeAppearanceModel(ShapeAppearanceModel startShapeAppearanceModel2) {
        this.startShapeAppearanceModel = startShapeAppearanceModel2;
    }

    public void setStartView(View startView2) {
        this.startView = startView2;
    }

    public void setStartViewId(int startViewId2) {
        this.startViewId = startViewId2;
    }

    public void setTransitionDirection(int transitionDirection2) {
        this.transitionDirection = transitionDirection2;
    }
}

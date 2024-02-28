package com.google.android.material.transition.platform;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.transition.PathMotion;
import android.transition.PatternPathMotion;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.TypedValue;
import android.view.View;
import androidx.core.graphics.PathParser;
import com.google.android.material.motion.MotionUtils;
import com.google.android.material.shape.AbsoluteCornerSize;
import com.google.android.material.shape.CornerSize;
import com.google.android.material.shape.RelativeCornerSize;
import com.google.android.material.shape.ShapeAppearanceModel;
import mt.Log1F380D;

/* compiled from: 010C */
class TransitionUtils {
    static final int NO_ATTR_RES_ID = 0;
    static final int NO_DURATION = -1;
    private static final int PATH_TYPE_ARC = 1;
    private static final int PATH_TYPE_LINEAR = 0;
    private static final RectF transformAlphaRectF = new RectF();

    interface CanvasOperation {
        void run(Canvas canvas);
    }

    interface CornerSizeBinaryOperator {
        CornerSize apply(CornerSize cornerSize, CornerSize cornerSize2);
    }

    private TransitionUtils() {
    }

    static float calculateArea(RectF bounds) {
        return bounds.width() * bounds.height();
    }

    static ShapeAppearanceModel convertToRelativeCornerSizes(ShapeAppearanceModel shapeAppearanceModel, final RectF bounds) {
        return shapeAppearanceModel.withTransformedCornerSizes(new ShapeAppearanceModel.CornerSizeUnaryOperator() {
            public CornerSize apply(CornerSize cornerSize) {
                return cornerSize instanceof RelativeCornerSize ? cornerSize : new RelativeCornerSize(cornerSize.getCornerSize(bounds) / bounds.height());
            }
        });
    }

    static Shader createColorShader(int color) {
        return new LinearGradient(0.0f, 0.0f, 0.0f, 0.0f, color, color, Shader.TileMode.CLAMP);
    }

    static <T> T defaultIfNull(T t, T t2) {
        return t != null ? t : t2;
    }

    /* JADX WARNING: type inference failed for: r1v2, types: [android.view.ViewParent] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static android.view.View findAncestorById(android.view.View r4, int r5) {
        /*
            android.content.res.Resources r0 = r4.getResources()
            java.lang.String r0 = r0.getResourceName(r5)
        L_0x0008:
            if (r4 == 0) goto L_0x001d
            int r1 = r4.getId()
            if (r1 != r5) goto L_0x0011
            return r4
        L_0x0011:
            android.view.ViewParent r1 = r4.getParent()
            boolean r2 = r1 instanceof android.view.View
            if (r2 == 0) goto L_0x001d
            r4 = r1
            android.view.View r4 = (android.view.View) r4
            goto L_0x0008
        L_0x001d:
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.StringBuilder r2 = r2.append(r0)
            java.lang.String r3 = " is not a valid ancestor"
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.transition.platform.TransitionUtils.findAncestorById(android.view.View, int):android.view.View");
    }

    static View findDescendantOrAncestorById(View view, int viewId) {
        View findViewById = view.findViewById(viewId);
        return findViewById != null ? findViewById : findAncestorById(view, viewId);
    }

    static RectF getLocationOnScreen(View view) {
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        int i = iArr[0];
        int i2 = iArr[1];
        return new RectF((float) i, (float) i2, (float) (view.getWidth() + i), (float) (view.getHeight() + i2));
    }

    static RectF getRelativeBounds(View view) {
        return new RectF((float) view.getLeft(), (float) view.getTop(), (float) view.getRight(), (float) view.getBottom());
    }

    static Rect getRelativeBoundsRect(View view) {
        return new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    }

    private static boolean isShapeAppearanceSignificant(ShapeAppearanceModel shapeAppearanceModel, RectF bounds) {
        return (shapeAppearanceModel.getTopLeftCornerSize().getCornerSize(bounds) == 0.0f && shapeAppearanceModel.getTopRightCornerSize().getCornerSize(bounds) == 0.0f && shapeAppearanceModel.getBottomRightCornerSize().getCornerSize(bounds) == 0.0f && shapeAppearanceModel.getBottomLeftCornerSize().getCornerSize(bounds) == 0.0f) ? false : true;
    }

    static float lerp(float startValue, float endValue, float fraction) {
        return ((endValue - startValue) * fraction) + startValue;
    }

    static float lerp(float startValue, float endValue, float startFraction, float endFraction, float fraction) {
        return lerp(startValue, endValue, startFraction, endFraction, fraction, false);
    }

    static float lerp(float startValue, float endValue, float startFraction, float endFraction, float fraction, boolean allowOvershoot) {
        return (!allowOvershoot || (fraction >= 0.0f && fraction <= 1.0f)) ? fraction < startFraction ? startValue : fraction > endFraction ? endValue : lerp(startValue, endValue, (fraction - startFraction) / (endFraction - startFraction)) : lerp(startValue, endValue, fraction);
    }

    static int lerp(int startValue, int endValue, float startFraction, float endFraction, float fraction) {
        return fraction < startFraction ? startValue : fraction > endFraction ? endValue : (int) lerp((float) startValue, (float) endValue, (fraction - startFraction) / (endFraction - startFraction));
    }

    static ShapeAppearanceModel lerp(ShapeAppearanceModel startValue, ShapeAppearanceModel endValue, RectF startBounds, RectF endBounds, float startFraction, float endFraction, float fraction) {
        if (fraction < startFraction) {
            return startValue;
        }
        if (fraction > endFraction) {
            return endValue;
        }
        final RectF rectF = startBounds;
        final RectF rectF2 = endBounds;
        final float f = startFraction;
        final float f2 = endFraction;
        final float f3 = fraction;
        return transformCornerSizes(startValue, endValue, startBounds, new CornerSizeBinaryOperator() {
            public CornerSize apply(CornerSize cornerSize1, CornerSize cornerSize2) {
                return new AbsoluteCornerSize(TransitionUtils.lerp(cornerSize1.getCornerSize(rectF), cornerSize2.getCornerSize(rectF2), f, f2, f3));
            }
        });
    }

    static void maybeAddTransition(TransitionSet transitionSet, Transition transition) {
        if (transition != null) {
            transitionSet.addTransition(transition);
        }
    }

    static boolean maybeApplyThemeDuration(Transition transition, Context context, int attrResId) {
        int resolveThemeDuration;
        if (attrResId == 0 || transition.getDuration() != -1 || (resolveThemeDuration = MotionUtils.resolveThemeDuration(context, attrResId, -1)) == -1) {
            return false;
        }
        transition.setDuration((long) resolveThemeDuration);
        return true;
    }

    static boolean maybeApplyThemeInterpolator(Transition transition, Context context, int attrResId, TimeInterpolator defaultInterpolator) {
        if (attrResId == 0 || transition.getInterpolator() != null) {
            return false;
        }
        transition.setInterpolator(MotionUtils.resolveThemeInterpolator(context, attrResId, defaultInterpolator));
        return true;
    }

    static boolean maybeApplyThemePath(Transition transition, Context context, int attrResId) {
        PathMotion resolveThemePath;
        if (attrResId == 0 || (resolveThemePath = resolveThemePath(context, attrResId)) == null) {
            return false;
        }
        transition.setPathMotion(resolveThemePath);
        return true;
    }

    static void maybeRemoveTransition(TransitionSet transitionSet, Transition transition) {
        if (transition != null) {
            transitionSet.removeTransition(transition);
        }
    }

    static PathMotion resolveThemePath(Context context, int attrResId) {
        TypedValue typedValue = new TypedValue();
        if (!context.getTheme().resolveAttribute(attrResId, typedValue, true)) {
            return null;
        }
        if (typedValue.type == 16) {
            int i = typedValue.data;
            if (i == 0) {
                return null;
            }
            if (i == 1) {
                return new MaterialArcMotion();
            }
            throw new IllegalArgumentException("Invalid motion path type: " + i);
        } else if (typedValue.type == 3) {
            String valueOf = String.valueOf(typedValue.string);
            Log1F380D.a((Object) valueOf);
            return new PatternPathMotion(PathParser.createPathFromPathData(valueOf));
        } else {
            throw new IllegalArgumentException("Motion path theme attribute must either be an enum value or path data string");
        }
    }

    private static int saveLayerAlphaCompat(Canvas canvas, Rect bounds, int alpha) {
        RectF rectF = transformAlphaRectF;
        rectF.set(bounds);
        if (Build.VERSION.SDK_INT >= 21) {
            return canvas.saveLayerAlpha(rectF, alpha);
        }
        return canvas.saveLayerAlpha(rectF.left, rectF.top, rectF.right, rectF.bottom, alpha, 31);
    }

    static void transform(Canvas canvas, Rect bounds, float dx, float dy, float scale, int alpha, CanvasOperation op) {
        if (alpha > 0) {
            int save = canvas.save();
            canvas.translate(dx, dy);
            canvas.scale(scale, scale);
            if (alpha < 255) {
                saveLayerAlphaCompat(canvas, bounds, alpha);
            }
            op.run(canvas);
            canvas.restoreToCount(save);
        }
    }

    static ShapeAppearanceModel transformCornerSizes(ShapeAppearanceModel shapeAppearanceModel1, ShapeAppearanceModel shapeAppearanceModel2, RectF shapeAppearanceModel1Bounds, CornerSizeBinaryOperator op) {
        return (isShapeAppearanceSignificant(shapeAppearanceModel1, shapeAppearanceModel1Bounds) ? shapeAppearanceModel1 : shapeAppearanceModel2).toBuilder().setTopLeftCornerSize(op.apply(shapeAppearanceModel1.getTopLeftCornerSize(), shapeAppearanceModel2.getTopLeftCornerSize())).setTopRightCornerSize(op.apply(shapeAppearanceModel1.getTopRightCornerSize(), shapeAppearanceModel2.getTopRightCornerSize())).setBottomLeftCornerSize(op.apply(shapeAppearanceModel1.getBottomLeftCornerSize(), shapeAppearanceModel2.getBottomLeftCornerSize())).setBottomRightCornerSize(op.apply(shapeAppearanceModel1.getBottomRightCornerSize(), shapeAppearanceModel2.getBottomRightCornerSize())).build();
    }
}

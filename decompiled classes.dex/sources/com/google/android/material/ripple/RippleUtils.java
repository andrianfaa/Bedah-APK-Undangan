package com.google.android.material.ripple;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.util.StateSet;
import androidx.core.graphics.ColorUtils;

public class RippleUtils {
    private static final int[] ENABLED_PRESSED_STATE_SET = {16842910, 16842919};
    private static final int[] FOCUSED_STATE_SET = {16842908};
    private static final int[] HOVERED_FOCUSED_STATE_SET = {16843623, 16842908};
    private static final int[] HOVERED_STATE_SET = {16843623};
    static final String LOG_TAG = RippleUtils.class.getSimpleName();
    private static final int[] PRESSED_STATE_SET = {16842919};
    private static final int[] SELECTED_FOCUSED_STATE_SET = {16842913, 16842908};
    private static final int[] SELECTED_HOVERED_FOCUSED_STATE_SET = {16842913, 16843623, 16842908};
    private static final int[] SELECTED_HOVERED_STATE_SET = {16842913, 16843623};
    private static final int[] SELECTED_PRESSED_STATE_SET = {16842913, 16842919};
    private static final int[] SELECTED_STATE_SET = {16842913};
    static final String TRANSPARENT_DEFAULT_COLOR_WARNING = "Use a non-transparent color for the default color as it will be used to finish ripple animations.";
    public static final boolean USE_FRAMEWORK_RIPPLE = (Build.VERSION.SDK_INT >= 21);

    private RippleUtils() {
    }

    public static ColorStateList convertToRippleDrawableColor(ColorStateList rippleColor) {
        if (USE_FRAMEWORK_RIPPLE) {
            int[][] iArr = new int[2][];
            int[] iArr2 = new int[2];
            iArr[0] = SELECTED_STATE_SET;
            iArr2[0] = getColorForState(rippleColor, SELECTED_PRESSED_STATE_SET);
            int i = 0 + 1;
            iArr[i] = StateSet.NOTHING;
            iArr2[i] = getColorForState(rippleColor, PRESSED_STATE_SET);
            int i2 = i + 1;
            return new ColorStateList(iArr, iArr2);
        }
        int[][] iArr3 = new int[10][];
        int[] iArr4 = new int[10];
        int[] iArr5 = SELECTED_PRESSED_STATE_SET;
        iArr3[0] = iArr5;
        iArr4[0] = getColorForState(rippleColor, iArr5);
        int i3 = 0 + 1;
        int[] iArr6 = SELECTED_HOVERED_FOCUSED_STATE_SET;
        iArr3[i3] = iArr6;
        iArr4[i3] = getColorForState(rippleColor, iArr6);
        int i4 = i3 + 1;
        int[] iArr7 = SELECTED_FOCUSED_STATE_SET;
        iArr3[i4] = iArr7;
        iArr4[i4] = getColorForState(rippleColor, iArr7);
        int i5 = i4 + 1;
        int[] iArr8 = SELECTED_HOVERED_STATE_SET;
        iArr3[i5] = iArr8;
        iArr4[i5] = getColorForState(rippleColor, iArr8);
        int i6 = i5 + 1;
        iArr3[i6] = SELECTED_STATE_SET;
        iArr4[i6] = 0;
        int i7 = i6 + 1;
        int[] iArr9 = PRESSED_STATE_SET;
        iArr3[i7] = iArr9;
        iArr4[i7] = getColorForState(rippleColor, iArr9);
        int i8 = i7 + 1;
        int[] iArr10 = HOVERED_FOCUSED_STATE_SET;
        iArr3[i8] = iArr10;
        iArr4[i8] = getColorForState(rippleColor, iArr10);
        int i9 = i8 + 1;
        int[] iArr11 = FOCUSED_STATE_SET;
        iArr3[i9] = iArr11;
        iArr4[i9] = getColorForState(rippleColor, iArr11);
        int i10 = i9 + 1;
        int[] iArr12 = HOVERED_STATE_SET;
        iArr3[i10] = iArr12;
        iArr4[i10] = getColorForState(rippleColor, iArr12);
        int i11 = i10 + 1;
        iArr3[i11] = StateSet.NOTHING;
        iArr4[i11] = 0;
        int i12 = i11 + 1;
        return new ColorStateList(iArr3, iArr4);
    }

    private static int doubleAlpha(int color) {
        return ColorUtils.setAlphaComponent(color, Math.min(Color.alpha(color) * 2, 255));
    }

    private static int getColorForState(ColorStateList rippleColor, int[] state) {
        int colorForState = rippleColor != null ? rippleColor.getColorForState(state, rippleColor.getDefaultColor()) : 0;
        return USE_FRAMEWORK_RIPPLE ? doubleAlpha(colorForState) : colorForState;
    }

    public static ColorStateList sanitizeRippleDrawableColor(ColorStateList rippleColor) {
        if (rippleColor == null) {
            return ColorStateList.valueOf(0);
        }
        if (Build.VERSION.SDK_INT >= 22 && Build.VERSION.SDK_INT <= 27 && Color.alpha(rippleColor.getDefaultColor()) == 0 && Color.alpha(rippleColor.getColorForState(ENABLED_PRESSED_STATE_SET, 0)) != 0) {
            Log.w(LOG_TAG, TRANSPARENT_DEFAULT_COLOR_WARNING);
        }
        return rippleColor;
    }

    public static boolean shouldDrawRippleCompat(int[] stateSet) {
        boolean z = false;
        boolean z2 = false;
        for (int i : stateSet) {
            if (i == 16842910) {
                z = true;
            } else if (i == 16842908) {
                z2 = true;
            } else if (i == 16842919) {
                z2 = true;
            } else if (i == 16843623) {
                z2 = true;
            }
        }
        return z && z2;
    }
}

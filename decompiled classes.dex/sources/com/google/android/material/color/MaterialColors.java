package com.google.android.material.color;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.R;
import com.google.android.material.resources.MaterialAttributes;

public class MaterialColors {
    public static final float ALPHA_DISABLED = 0.38f;
    public static final float ALPHA_DISABLED_LOW = 0.12f;
    public static final float ALPHA_FULL = 1.0f;
    public static final float ALPHA_LOW = 0.32f;
    public static final float ALPHA_MEDIUM = 0.54f;
    private static final int TONE_ACCENT_CONTAINER_DARK = 30;
    private static final int TONE_ACCENT_CONTAINER_LIGHT = 90;
    private static final int TONE_ACCENT_DARK = 80;
    private static final int TONE_ACCENT_LIGHT = 40;
    private static final int TONE_ON_ACCENT_CONTAINER_DARK = 90;
    private static final int TONE_ON_ACCENT_CONTAINER_LIGHT = 10;
    private static final int TONE_ON_ACCENT_DARK = 20;
    private static final int TONE_ON_ACCENT_LIGHT = 100;

    private MaterialColors() {
    }

    public static int compositeARGBWithAlpha(int originalARGB, int alpha) {
        return ColorUtils.setAlphaComponent(originalARGB, (Color.alpha(originalARGB) * alpha) / 255);
    }

    public static int getColor(Context context, int colorAttributeResId, int defaultValue) {
        TypedValue resolve = MaterialAttributes.resolve(context, colorAttributeResId);
        return resolve != null ? resolve.data : defaultValue;
    }

    public static int getColor(Context context, int colorAttributeResId, String errorMessageComponent) {
        return MaterialAttributes.resolveOrThrow(context, colorAttributeResId, errorMessageComponent);
    }

    public static int getColor(View view, int colorAttributeResId) {
        return MaterialAttributes.resolveOrThrow(view, colorAttributeResId);
    }

    public static int getColor(View view, int colorAttributeResId, int defaultValue) {
        return getColor(view.getContext(), colorAttributeResId, defaultValue);
    }

    private static int getColorRole(int color, int tone) {
        Hct fromInt = Hct.fromInt(color);
        fromInt.setTone((float) tone);
        return fromInt.toInt();
    }

    public static ColorRoles getColorRoles(int color, boolean isLightTheme) {
        return isLightTheme ? new ColorRoles(getColorRole(color, 40), getColorRole(color, 100), getColorRole(color, 90), getColorRole(color, 10)) : new ColorRoles(getColorRole(color, TONE_ACCENT_DARK), getColorRole(color, 20), getColorRole(color, 30), getColorRole(color, 90));
    }

    public static ColorRoles getColorRoles(Context context, int color) {
        return getColorRoles(color, MaterialAttributes.resolveBoolean(context, R.attr.isLightTheme, true));
    }

    public static int harmonize(int colorToHarmonize, int colorToHarmonizeWith) {
        return Blend.harmonize(colorToHarmonize, colorToHarmonizeWith);
    }

    public static int harmonizeWithPrimary(Context context, int colorToHarmonize) {
        return harmonize(colorToHarmonize, getColor(context, R.attr.colorPrimary, MaterialColors.class.getCanonicalName()));
    }

    public static boolean isColorLight(int color) {
        return color != 0 && ColorUtils.calculateLuminance(color) > 0.5d;
    }

    public static int layer(int backgroundColor, int overlayColor) {
        return ColorUtils.compositeColors(overlayColor, backgroundColor);
    }

    public static int layer(int backgroundColor, int overlayColor, float overlayAlpha) {
        return layer(backgroundColor, ColorUtils.setAlphaComponent(overlayColor, Math.round(((float) Color.alpha(overlayColor)) * overlayAlpha)));
    }

    public static int layer(View view, int backgroundColorAttributeResId, int overlayColorAttributeResId) {
        return layer(view, backgroundColorAttributeResId, overlayColorAttributeResId, 1.0f);
    }

    public static int layer(View view, int backgroundColorAttributeResId, int overlayColorAttributeResId, float overlayAlpha) {
        return layer(getColor(view, backgroundColorAttributeResId), getColor(view, overlayColorAttributeResId), overlayAlpha);
    }
}

package com.google.android.material.motion;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.TypedValue;
import androidx.core.graphics.PathParser;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.google.android.material.resources.MaterialAttributes;
import mt.Log1F380D;

/* compiled from: 00F8 */
public class MotionUtils {
    private static final String EASING_TYPE_CUBIC_BEZIER = "cubic-bezier";
    private static final String EASING_TYPE_FORMAT_END = ")";
    private static final String EASING_TYPE_FORMAT_START = "(";
    private static final String EASING_TYPE_PATH = "path";

    private MotionUtils() {
    }

    private static float getControlPoint(String[] controlPoints, int index) {
        float parseFloat = Float.parseFloat(controlPoints[index]);
        if (parseFloat >= 0.0f && parseFloat <= 1.0f) {
            return parseFloat;
        }
        throw new IllegalArgumentException("Motion easing control point value must be between 0 and 1; instead got: " + parseFloat);
    }

    private static String getEasingContent(String easingString, String easingType) {
        return easingString.substring(easingType.length() + EASING_TYPE_FORMAT_START.length(), easingString.length() - EASING_TYPE_FORMAT_END.length());
    }

    private static boolean isEasingType(String easingString, String easingType) {
        return easingString.startsWith(new StringBuilder().append(easingType).append(EASING_TYPE_FORMAT_START).toString()) && easingString.endsWith(EASING_TYPE_FORMAT_END);
    }

    public static int resolveThemeDuration(Context context, int attrResId, int defaultDuration) {
        return MaterialAttributes.resolveInteger(context, attrResId, defaultDuration);
    }

    public static TimeInterpolator resolveThemeInterpolator(Context context, int attrResId, TimeInterpolator defaultInterpolator) {
        TypedValue typedValue = new TypedValue();
        if (!context.getTheme().resolveAttribute(attrResId, typedValue, true)) {
            return defaultInterpolator;
        }
        if (typedValue.type == 3) {
            String valueOf = String.valueOf(typedValue.string);
            Log1F380D.a((Object) valueOf);
            if (isEasingType(valueOf, EASING_TYPE_CUBIC_BEZIER)) {
                String easingContent = getEasingContent(valueOf, EASING_TYPE_CUBIC_BEZIER);
                Log1F380D.a((Object) easingContent);
                String[] split = easingContent.split(",");
                if (split.length == 4) {
                    return PathInterpolatorCompat.create(getControlPoint(split, 0), getControlPoint(split, 1), getControlPoint(split, 2), getControlPoint(split, 3));
                }
                throw new IllegalArgumentException("Motion easing theme attribute must have 4 control points if using bezier curve format; instead got: " + split.length);
            } else if (isEasingType(valueOf, EASING_TYPE_PATH)) {
                String easingContent2 = getEasingContent(valueOf, EASING_TYPE_PATH);
                Log1F380D.a((Object) easingContent2);
                return PathInterpolatorCompat.create(PathParser.createPathFromPathData(easingContent2));
            } else {
                throw new IllegalArgumentException("Invalid motion easing type: " + valueOf);
            }
        } else {
            throw new IllegalArgumentException("Motion easing theme attribute must be a string");
        }
    }
}

package com.google.android.material.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.appcompat.widget.TintTypedArray;
import com.google.android.material.R;

public final class ThemeEnforcement {
    private static final int[] APPCOMPAT_CHECK_ATTRS = {R.attr.colorPrimary};
    private static final String APPCOMPAT_THEME_NAME = "Theme.AppCompat";
    private static final int[] MATERIAL_CHECK_ATTRS = {R.attr.colorPrimaryVariant};
    private static final String MATERIAL_THEME_NAME = "Theme.MaterialComponents";

    private ThemeEnforcement() {
    }

    public static void checkAppCompatTheme(Context context) {
        checkTheme(context, APPCOMPAT_CHECK_ATTRS, APPCOMPAT_THEME_NAME);
    }

    private static void checkCompatibleTheme(Context context, AttributeSet set, int defStyleAttr, int defStyleRes) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ThemeEnforcement, defStyleAttr, defStyleRes);
        boolean z = obtainStyledAttributes.getBoolean(R.styleable.ThemeEnforcement_enforceMaterialTheme, false);
        obtainStyledAttributes.recycle();
        if (z) {
            TypedValue typedValue = new TypedValue();
            if (!context.getTheme().resolveAttribute(R.attr.isMaterialTheme, typedValue, true) || (typedValue.type == 18 && typedValue.data == 0)) {
                checkMaterialTheme(context);
            }
        }
        checkAppCompatTheme(context);
    }

    public static void checkMaterialTheme(Context context) {
        checkTheme(context, MATERIAL_CHECK_ATTRS, MATERIAL_THEME_NAME);
    }

    private static void checkTextAppearance(Context context, AttributeSet set, int[] attrs, int defStyleAttr, int defStyleRes, int... textAppearanceResIndices) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ThemeEnforcement, defStyleAttr, defStyleRes);
        boolean z = false;
        if (!obtainStyledAttributes.getBoolean(R.styleable.ThemeEnforcement_enforceTextAppearance, false)) {
            obtainStyledAttributes.recycle();
            return;
        }
        if (textAppearanceResIndices != null && textAppearanceResIndices.length != 0) {
            z = isCustomTextAppearanceValid(context, set, attrs, defStyleAttr, defStyleRes, textAppearanceResIndices);
        } else if (obtainStyledAttributes.getResourceId(R.styleable.ThemeEnforcement_android_textAppearance, -1) != -1) {
            z = true;
        }
        obtainStyledAttributes.recycle();
        if (!z) {
            throw new IllegalArgumentException("This component requires that you specify a valid TextAppearance attribute. Update your app theme to inherit from Theme.MaterialComponents (or a descendant).");
        }
    }

    private static void checkTheme(Context context, int[] themeAttributes, String themeName) {
        if (!isTheme(context, themeAttributes)) {
            throw new IllegalArgumentException("The style on this component requires your app theme to be " + themeName + " (or a descendant).");
        }
    }

    public static boolean isAppCompatTheme(Context context) {
        return isTheme(context, APPCOMPAT_CHECK_ATTRS);
    }

    private static boolean isCustomTextAppearanceValid(Context context, AttributeSet set, int[] attrs, int defStyleAttr, int defStyleRes, int... textAppearanceResIndices) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes);
        for (int resourceId : textAppearanceResIndices) {
            if (obtainStyledAttributes.getResourceId(resourceId, -1) == -1) {
                obtainStyledAttributes.recycle();
                return false;
            }
        }
        obtainStyledAttributes.recycle();
        return true;
    }

    public static boolean isMaterialTheme(Context context) {
        return isTheme(context, MATERIAL_CHECK_ATTRS);
    }

    private static boolean isTheme(Context context, int[] themeAttributes) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(themeAttributes);
        for (int i = 0; i < themeAttributes.length; i++) {
            if (!obtainStyledAttributes.hasValue(i)) {
                obtainStyledAttributes.recycle();
                return false;
            }
        }
        obtainStyledAttributes.recycle();
        return true;
    }

    public static TypedArray obtainStyledAttributes(Context context, AttributeSet set, int[] attrs, int defStyleAttr, int defStyleRes, int... textAppearanceResIndices) {
        checkCompatibleTheme(context, set, defStyleAttr, defStyleRes);
        checkTextAppearance(context, set, attrs, defStyleAttr, defStyleRes, textAppearanceResIndices);
        return context.obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes);
    }

    public static TintTypedArray obtainTintedStyledAttributes(Context context, AttributeSet set, int[] attrs, int defStyleAttr, int defStyleRes, int... textAppearanceResIndices) {
        checkCompatibleTheme(context, set, defStyleAttr, defStyleRes);
        checkTextAppearance(context, set, attrs, defStyleAttr, defStyleRes, textAppearanceResIndices);
        return TintTypedArray.obtainStyledAttributes(context, set, attrs, defStyleAttr, defStyleRes);
    }
}

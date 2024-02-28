package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import androidx.appcompat.R;
import androidx.core.graphics.ColorUtils;

public class ThemeUtils {
    static final int[] ACTIVATED_STATE_SET = {16843518};
    static final int[] CHECKED_STATE_SET = {16842912};
    static final int[] DISABLED_STATE_SET = {-16842910};
    static final int[] EMPTY_STATE_SET = new int[0];
    static final int[] FOCUSED_STATE_SET = {16842908};
    static final int[] NOT_PRESSED_OR_FOCUSED_STATE_SET = {-16842919, -16842908};
    static final int[] PRESSED_STATE_SET = {16842919};
    static final int[] SELECTED_STATE_SET = {16842913};
    private static final String TAG = "ThemeUtils";
    private static final int[] TEMP_ARRAY = new int[1];
    private static final ThreadLocal<TypedValue> TL_TYPED_VALUE = new ThreadLocal<>();

    private ThemeUtils() {
    }

    public static void checkAppCompatTheme(View view, Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(R.styleable.AppCompatTheme);
        try {
            if (!obtainStyledAttributes.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
                Log.e(TAG, "View " + view.getClass() + " is an AppCompat widget that can only be used with a Theme.AppCompat theme (or descendant).");
            }
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public static ColorStateList createDisabledStateList(int textColor, int disabledTextColor) {
        int[][] iArr = new int[2][];
        int[] iArr2 = new int[2];
        iArr[0] = DISABLED_STATE_SET;
        iArr2[0] = disabledTextColor;
        int i = 0 + 1;
        iArr[i] = EMPTY_STATE_SET;
        iArr2[i] = textColor;
        int i2 = i + 1;
        return new ColorStateList(iArr, iArr2);
    }

    public static int getDisabledThemeAttrColor(Context context, int attr) {
        ColorStateList themeAttrColorStateList = getThemeAttrColorStateList(context, attr);
        if (themeAttrColorStateList != null && themeAttrColorStateList.isStateful()) {
            return themeAttrColorStateList.getColorForState(DISABLED_STATE_SET, themeAttrColorStateList.getDefaultColor());
        }
        TypedValue typedValue = getTypedValue();
        context.getTheme().resolveAttribute(16842803, typedValue, true);
        return getThemeAttrColor(context, attr, typedValue.getFloat());
    }

    public static int getThemeAttrColor(Context context, int attr) {
        int[] iArr = TEMP_ARRAY;
        iArr[0] = attr;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, (AttributeSet) null, iArr);
        try {
            return obtainStyledAttributes.getColor(0, 0);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    static int getThemeAttrColor(Context context, int attr, float alpha) {
        int themeAttrColor = getThemeAttrColor(context, attr);
        return ColorUtils.setAlphaComponent(themeAttrColor, Math.round(((float) Color.alpha(themeAttrColor)) * alpha));
    }

    public static ColorStateList getThemeAttrColorStateList(Context context, int attr) {
        int[] iArr = TEMP_ARRAY;
        iArr[0] = attr;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, (AttributeSet) null, iArr);
        try {
            return obtainStyledAttributes.getColorStateList(0);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    private static TypedValue getTypedValue() {
        ThreadLocal<TypedValue> threadLocal = TL_TYPED_VALUE;
        TypedValue typedValue = threadLocal.get();
        if (typedValue != null) {
            return typedValue;
        }
        TypedValue typedValue2 = new TypedValue();
        threadLocal.set(typedValue2);
        return typedValue2;
    }
}

package com.google.android.material.resources;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.TypedValue;
import com.google.android.material.R;

public class MaterialResources {
    private static final float FONT_SCALE_1_3 = 1.3f;
    private static final float FONT_SCALE_2_0 = 2.0f;

    private MaterialResources() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001a, code lost:
        r1 = r3.getColor(r4, -1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.content.res.ColorStateList getColorStateList(android.content.Context r2, android.content.res.TypedArray r3, int r4) {
        /*
            boolean r0 = r3.hasValue(r4)
            if (r0 == 0) goto L_0x0014
            r0 = 0
            int r0 = r3.getResourceId(r4, r0)
            if (r0 == 0) goto L_0x0014
            android.content.res.ColorStateList r1 = androidx.appcompat.content.res.AppCompatResources.getColorStateList(r2, r0)
            if (r1 == 0) goto L_0x0014
            return r1
        L_0x0014:
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 15
            if (r0 > r1) goto L_0x0026
            r0 = -1
            int r1 = r3.getColor(r4, r0)
            if (r1 == r0) goto L_0x0026
            android.content.res.ColorStateList r0 = android.content.res.ColorStateList.valueOf(r1)
            return r0
        L_0x0026:
            android.content.res.ColorStateList r0 = r3.getColorStateList(r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.resources.MaterialResources.getColorStateList(android.content.Context, android.content.res.TypedArray, int):android.content.res.ColorStateList");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001a, code lost:
        r1 = r3.getColor(r4, -1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.content.res.ColorStateList getColorStateList(android.content.Context r2, androidx.appcompat.widget.TintTypedArray r3, int r4) {
        /*
            boolean r0 = r3.hasValue(r4)
            if (r0 == 0) goto L_0x0014
            r0 = 0
            int r0 = r3.getResourceId(r4, r0)
            if (r0 == 0) goto L_0x0014
            android.content.res.ColorStateList r1 = androidx.appcompat.content.res.AppCompatResources.getColorStateList(r2, r0)
            if (r1 == 0) goto L_0x0014
            return r1
        L_0x0014:
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 15
            if (r0 > r1) goto L_0x0026
            r0 = -1
            int r1 = r3.getColor(r4, r0)
            if (r1 == r0) goto L_0x0026
            android.content.res.ColorStateList r0 = android.content.res.ColorStateList.valueOf(r1)
            return r0
        L_0x0026:
            android.content.res.ColorStateList r0 = r3.getColorStateList(r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.resources.MaterialResources.getColorStateList(android.content.Context, androidx.appcompat.widget.TintTypedArray, int):android.content.res.ColorStateList");
    }

    private static int getComplexUnit(TypedValue tv) {
        return Build.VERSION.SDK_INT >= 22 ? tv.getComplexUnit() : (tv.data >> 0) & 15;
    }

    public static int getDimensionPixelSize(Context context, TypedArray attributes, int index, int defaultValue) {
        TypedValue typedValue = new TypedValue();
        if (!attributes.getValue(index, typedValue) || typedValue.type != 2) {
            return attributes.getDimensionPixelSize(index, defaultValue);
        }
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(new int[]{typedValue.data});
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(0, defaultValue);
        obtainStyledAttributes.recycle();
        return dimensionPixelSize;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000d, code lost:
        r1 = androidx.appcompat.content.res.AppCompatResources.getDrawable(r2, (r0 = r3.getResourceId(r4, 0)));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.drawable.Drawable getDrawable(android.content.Context r2, android.content.res.TypedArray r3, int r4) {
        /*
            boolean r0 = r3.hasValue(r4)
            if (r0 == 0) goto L_0x0014
            r0 = 0
            int r0 = r3.getResourceId(r4, r0)
            if (r0 == 0) goto L_0x0014
            android.graphics.drawable.Drawable r1 = androidx.appcompat.content.res.AppCompatResources.getDrawable(r2, r0)
            if (r1 == 0) goto L_0x0014
            return r1
        L_0x0014:
            android.graphics.drawable.Drawable r0 = r3.getDrawable(r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.resources.MaterialResources.getDrawable(android.content.Context, android.content.res.TypedArray, int):android.graphics.drawable.Drawable");
    }

    static int getIndexWithValue(TypedArray attributes, int a, int b) {
        return attributes.hasValue(a) ? a : b;
    }

    public static TextAppearance getTextAppearance(Context context, TypedArray attributes, int index) {
        int resourceId;
        if (!attributes.hasValue(index) || (resourceId = attributes.getResourceId(index, 0)) == 0) {
            return null;
        }
        return new TextAppearance(context, resourceId);
    }

    public static int getUnscaledTextSize(Context context, int textAppearance, int defValue) {
        if (textAppearance == 0) {
            return defValue;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(textAppearance, R.styleable.TextAppearance);
        TypedValue typedValue = new TypedValue();
        boolean value = obtainStyledAttributes.getValue(R.styleable.TextAppearance_android_textSize, typedValue);
        obtainStyledAttributes.recycle();
        return !value ? defValue : getComplexUnit(typedValue) == 2 ? Math.round(TypedValue.complexToFloat(typedValue.data) * context.getResources().getDisplayMetrics().density) : TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics());
    }

    public static boolean isFontScaleAtLeast1_3(Context context) {
        return context.getResources().getConfiguration().fontScale >= FONT_SCALE_1_3;
    }

    public static boolean isFontScaleAtLeast2_0(Context context) {
        return context.getResources().getConfiguration().fontScale >= FONT_SCALE_2_0;
    }
}

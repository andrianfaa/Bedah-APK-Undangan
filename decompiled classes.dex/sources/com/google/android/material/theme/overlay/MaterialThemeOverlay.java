package com.google.android.material.theme.overlay;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.view.ContextThemeWrapper;
import com.google.android.material.R;

public class MaterialThemeOverlay {
    private static final int[] ANDROID_THEME_OVERLAY_ATTRS = {16842752, R.attr.theme};
    private static final int[] MATERIAL_THEME_OVERLAY_ATTR = {R.attr.materialThemeOverlay};

    private MaterialThemeOverlay() {
    }

    private static int obtainAndroidThemeOverlayId(Context context, AttributeSet attrs) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, ANDROID_THEME_OVERLAY_ATTRS);
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        int resourceId2 = obtainStyledAttributes.getResourceId(1, 0);
        obtainStyledAttributes.recycle();
        return resourceId != 0 ? resourceId : resourceId2;
    }

    private static int obtainMaterialThemeOverlayId(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, MATERIAL_THEME_OVERLAY_ATTR, defStyleAttr, defStyleRes);
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        obtainStyledAttributes.recycle();
        return resourceId;
    }

    public static Context wrap(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        int obtainMaterialThemeOverlayId = obtainMaterialThemeOverlayId(context, attrs, defStyleAttr, defStyleRes);
        boolean z = (context instanceof ContextThemeWrapper) && ((ContextThemeWrapper) context).getThemeResId() == obtainMaterialThemeOverlayId;
        if (obtainMaterialThemeOverlayId == 0 || z) {
            return context;
        }
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, obtainMaterialThemeOverlayId);
        int obtainAndroidThemeOverlayId = obtainAndroidThemeOverlayId(context, attrs);
        if (obtainAndroidThemeOverlayId != 0) {
            contextThemeWrapper.getTheme().applyStyle(obtainAndroidThemeOverlayId, true);
        }
        return contextThemeWrapper;
    }
}

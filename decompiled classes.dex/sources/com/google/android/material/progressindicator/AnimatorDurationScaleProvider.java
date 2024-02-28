package com.google.android.material.progressindicator;

import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;

public class AnimatorDurationScaleProvider {
    private static float defaultSystemAnimatorDurationScale = 1.0f;

    public static void setDefaultSystemAnimatorDurationScale(float scale) {
        defaultSystemAnimatorDurationScale = scale;
    }

    public float getSystemAnimatorDurationScale(ContentResolver contentResolver) {
        return Build.VERSION.SDK_INT >= 17 ? Settings.Global.getFloat(contentResolver, "animator_duration_scale", 1.0f) : Build.VERSION.SDK_INT == 16 ? Settings.System.getFloat(contentResolver, "animator_duration_scale", 1.0f) : defaultSystemAnimatorDurationScale;
    }
}

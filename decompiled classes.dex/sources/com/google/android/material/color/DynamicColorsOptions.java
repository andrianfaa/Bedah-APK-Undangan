package com.google.android.material.color;

import android.app.Activity;
import com.google.android.material.color.DynamicColors;

public class DynamicColorsOptions {
    /* access modifiers changed from: private */
    public static final DynamicColors.Precondition ALWAYS_ALLOW = new DynamicColors.Precondition() {
        public boolean shouldApplyDynamicColors(Activity activity, int theme) {
            return true;
        }
    };
    /* access modifiers changed from: private */
    public static final DynamicColors.OnAppliedCallback NO_OP_CALLBACK = new DynamicColors.OnAppliedCallback() {
        public void onApplied(Activity activity) {
        }
    };
    private final DynamicColors.OnAppliedCallback onAppliedCallback;
    private final DynamicColors.Precondition precondition;
    private final int themeOverlay;

    public static class Builder {
        /* access modifiers changed from: private */
        public DynamicColors.OnAppliedCallback onAppliedCallback = DynamicColorsOptions.NO_OP_CALLBACK;
        /* access modifiers changed from: private */
        public DynamicColors.Precondition precondition = DynamicColorsOptions.ALWAYS_ALLOW;
        /* access modifiers changed from: private */
        public int themeOverlay;

        public DynamicColorsOptions build() {
            return new DynamicColorsOptions(this);
        }

        public Builder setOnAppliedCallback(DynamicColors.OnAppliedCallback onAppliedCallback2) {
            this.onAppliedCallback = onAppliedCallback2;
            return this;
        }

        public Builder setPrecondition(DynamicColors.Precondition precondition2) {
            this.precondition = precondition2;
            return this;
        }

        public Builder setThemeOverlay(int themeOverlay2) {
            this.themeOverlay = themeOverlay2;
            return this;
        }
    }

    private DynamicColorsOptions(Builder builder) {
        this.themeOverlay = builder.themeOverlay;
        this.precondition = builder.precondition;
        this.onAppliedCallback = builder.onAppliedCallback;
    }

    public DynamicColors.OnAppliedCallback getOnAppliedCallback() {
        return this.onAppliedCallback;
    }

    public DynamicColors.Precondition getPrecondition() {
        return this.precondition;
    }

    public int getThemeOverlay() {
        return this.themeOverlay;
    }
}

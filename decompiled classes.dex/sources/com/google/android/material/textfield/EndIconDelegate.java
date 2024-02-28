package com.google.android.material.textfield;

import android.content.Context;
import com.google.android.material.internal.CheckableImageButton;

abstract class EndIconDelegate {
    Context context;
    final int customEndIcon;
    CheckableImageButton endIconView;
    TextInputLayout textInputLayout;

    EndIconDelegate(TextInputLayout textInputLayout2, int customEndIcon2) {
        this.textInputLayout = textInputLayout2;
        this.context = textInputLayout2.getContext();
        this.endIconView = textInputLayout2.getEndIconView();
        this.customEndIcon = customEndIcon2;
    }

    /* access modifiers changed from: package-private */
    public abstract void initialize();

    /* access modifiers changed from: package-private */
    public boolean isBoxBackgroundModeSupported(int boxBackgroundMode) {
        return true;
    }

    /* access modifiers changed from: package-private */
    public void onSuffixVisibilityChanged(boolean visible) {
    }

    /* access modifiers changed from: package-private */
    public boolean shouldTintIconOnError() {
        return false;
    }
}

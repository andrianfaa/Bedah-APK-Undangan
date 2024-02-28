package com.google.android.material.textfield;

import android.view.View;

class CustomEndIconDelegate extends EndIconDelegate {
    CustomEndIconDelegate(TextInputLayout textInputLayout, int customEndIcon) {
        super(textInputLayout, customEndIcon);
    }

    /* access modifiers changed from: package-private */
    public void initialize() {
        this.textInputLayout.setEndIconDrawable(this.customEndIcon);
        this.textInputLayout.setEndIconOnClickListener((View.OnClickListener) null);
        this.textInputLayout.setEndIconOnLongClickListener((View.OnLongClickListener) null);
    }
}

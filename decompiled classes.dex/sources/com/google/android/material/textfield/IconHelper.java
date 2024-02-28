package com.google.android.material.textfield;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.internal.CheckableImageButton;
import java.util.Arrays;

class IconHelper {
    private IconHelper() {
    }

    static void applyIconTint(TextInputLayout textInputLayout, CheckableImageButton iconView, ColorStateList iconTintList, PorterDuff.Mode iconTintMode) {
        Drawable drawable = iconView.getDrawable();
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable).mutate();
            if (iconTintList == null || !iconTintList.isStateful()) {
                DrawableCompat.setTintList(drawable, iconTintList);
            } else {
                DrawableCompat.setTintList(drawable, ColorStateList.valueOf(iconTintList.getColorForState(mergeIconState(textInputLayout, iconView), iconTintList.getDefaultColor())));
            }
            if (iconTintMode != null) {
                DrawableCompat.setTintMode(drawable, iconTintMode);
            }
        }
        if (iconView.getDrawable() != drawable) {
            iconView.setImageDrawable(drawable);
        }
    }

    private static int[] mergeIconState(TextInputLayout textInputLayout, CheckableImageButton iconView) {
        int[] drawableState = textInputLayout.getDrawableState();
        int[] drawableState2 = iconView.getDrawableState();
        int length = drawableState.length;
        int[] copyOf = Arrays.copyOf(drawableState, drawableState.length + drawableState2.length);
        System.arraycopy(drawableState2, 0, copyOf, length, drawableState2.length);
        return copyOf;
    }

    static void refreshIconDrawableState(TextInputLayout textInputLayout, CheckableImageButton iconView, ColorStateList colorStateList) {
        Drawable drawable = iconView.getDrawable();
        if (iconView.getDrawable() != null && colorStateList != null && colorStateList.isStateful()) {
            int colorForState = colorStateList.getColorForState(mergeIconState(textInputLayout, iconView), colorStateList.getDefaultColor());
            Drawable mutate = DrawableCompat.wrap(drawable).mutate();
            DrawableCompat.setTintList(mutate, ColorStateList.valueOf(colorForState));
            iconView.setImageDrawable(mutate);
        }
    }

    private static void setIconClickable(CheckableImageButton iconView, View.OnLongClickListener onLongClickListener) {
        boolean hasOnClickListeners = ViewCompat.hasOnClickListeners(iconView);
        boolean z = false;
        int i = 1;
        boolean z2 = onLongClickListener != null;
        if (hasOnClickListeners || z2) {
            z = true;
        }
        iconView.setFocusable(z);
        iconView.setClickable(hasOnClickListeners);
        iconView.setPressable(hasOnClickListeners);
        iconView.setLongClickable(z2);
        if (!z) {
            i = 2;
        }
        ViewCompat.setImportantForAccessibility(iconView, i);
    }

    static void setIconOnClickListener(CheckableImageButton iconView, View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
        iconView.setOnClickListener(onClickListener);
        setIconClickable(iconView, onLongClickListener);
    }

    static void setIconOnLongClickListener(CheckableImageButton iconView, View.OnLongClickListener onLongClickListener) {
        iconView.setOnLongClickListener(onLongClickListener);
        setIconClickable(iconView, onLongClickListener);
    }
}

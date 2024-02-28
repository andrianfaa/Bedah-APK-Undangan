package com.google.android.material.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.util.AttributeSet;
import com.google.android.material.R;
import com.google.android.material.internal.ThemeEnforcement;

public class MaterialDialogs {
    private MaterialDialogs() {
    }

    public static Rect getDialogBackgroundInsets(Context context, int defaultStyleAttribute, int defaultStyleResource) {
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, (AttributeSet) null, R.styleable.MaterialAlertDialog, defaultStyleAttribute, defaultStyleResource, new int[0]);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.MaterialAlertDialog_backgroundInsetStart, context.getResources().getDimensionPixelSize(R.dimen.mtrl_alert_dialog_background_inset_start));
        int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R.styleable.MaterialAlertDialog_backgroundInsetTop, context.getResources().getDimensionPixelSize(R.dimen.mtrl_alert_dialog_background_inset_top));
        int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(R.styleable.MaterialAlertDialog_backgroundInsetEnd, context.getResources().getDimensionPixelSize(R.dimen.mtrl_alert_dialog_background_inset_end));
        int dimensionPixelSize4 = obtainStyledAttributes.getDimensionPixelSize(R.styleable.MaterialAlertDialog_backgroundInsetBottom, context.getResources().getDimensionPixelSize(R.dimen.mtrl_alert_dialog_background_inset_bottom));
        obtainStyledAttributes.recycle();
        int i = dimensionPixelSize;
        int i2 = dimensionPixelSize3;
        if (Build.VERSION.SDK_INT >= 17 && context.getResources().getConfiguration().getLayoutDirection() == 1) {
            i = dimensionPixelSize3;
            i2 = dimensionPixelSize;
        }
        return new Rect(i, dimensionPixelSize2, i2, dimensionPixelSize4);
    }

    public static InsetDrawable insetDrawable(Drawable drawable, Rect backgroundInsets) {
        return new InsetDrawable(drawable, backgroundInsets.left, backgroundInsets.top, backgroundInsets.right, backgroundInsets.bottom);
    }
}

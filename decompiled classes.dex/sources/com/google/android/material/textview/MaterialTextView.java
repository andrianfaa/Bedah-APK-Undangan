package com.google.android.material.textview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import com.google.android.material.R;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;

public class MaterialTextView extends AppCompatTextView {
    public MaterialTextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public MaterialTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842884);
    }

    public MaterialTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MaterialTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(MaterialThemeOverlay.wrap(context, attrs, defStyleAttr, defStyleRes), attrs, defStyleAttr);
        int findViewAppearanceResourceId;
        Context context2 = getContext();
        if (canApplyTextAppearanceLineHeight(context2)) {
            Resources.Theme theme = context2.getTheme();
            if (!viewAttrsHasLineHeight(context2, theme, attrs, defStyleAttr, defStyleRes) && (findViewAppearanceResourceId = findViewAppearanceResourceId(theme, attrs, defStyleAttr, defStyleRes)) != -1) {
                applyLineHeightFromViewAppearance(theme, findViewAppearanceResourceId);
            }
        }
    }

    private void applyLineHeightFromViewAppearance(Resources.Theme theme, int resId) {
        TypedArray obtainStyledAttributes = theme.obtainStyledAttributes(resId, R.styleable.MaterialTextAppearance);
        int readFirstAvailableDimension = readFirstAvailableDimension(getContext(), obtainStyledAttributes, R.styleable.MaterialTextAppearance_android_lineHeight, R.styleable.MaterialTextAppearance_lineHeight);
        obtainStyledAttributes.recycle();
        if (readFirstAvailableDimension >= 0) {
            setLineHeight(readFirstAvailableDimension);
        }
    }

    private static boolean canApplyTextAppearanceLineHeight(Context context) {
        return MaterialAttributes.resolveBoolean(context, R.attr.textAppearanceLineHeightEnabled, true);
    }

    private static int findViewAppearanceResourceId(Resources.Theme theme, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray obtainStyledAttributes = theme.obtainStyledAttributes(attrs, R.styleable.MaterialTextView, defStyleAttr, defStyleRes);
        int resourceId = obtainStyledAttributes.getResourceId(R.styleable.MaterialTextView_android_textAppearance, -1);
        obtainStyledAttributes.recycle();
        return resourceId;
    }

    private static int readFirstAvailableDimension(Context context, TypedArray attributes, int... indices) {
        int i = -1;
        for (int i2 = 0; i2 < indices.length && i < 0; i2++) {
            i = MaterialResources.getDimensionPixelSize(context, attributes, indices[i2], -1);
        }
        return i;
    }

    private static boolean viewAttrsHasLineHeight(Context context, Resources.Theme theme, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray obtainStyledAttributes = theme.obtainStyledAttributes(attrs, R.styleable.MaterialTextView, defStyleAttr, defStyleRes);
        int readFirstAvailableDimension = readFirstAvailableDimension(context, obtainStyledAttributes, R.styleable.MaterialTextView_android_lineHeight, R.styleable.MaterialTextView_lineHeight);
        obtainStyledAttributes.recycle();
        return readFirstAvailableDimension != -1;
    }

    public void setTextAppearance(Context context, int resId) {
        super.setTextAppearance(context, resId);
        if (canApplyTextAppearanceLineHeight(context)) {
            applyLineHeightFromViewAppearance(context.getTheme(), resId);
        }
    }
}

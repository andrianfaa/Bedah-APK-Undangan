package com.google.android.material.appbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.internal.ToolbarUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;

public class MaterialToolbar extends Toolbar {
    private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_Toolbar;
    private static final ImageView.ScaleType[] LOGO_SCALE_TYPE_ARRAY = {ImageView.ScaleType.MATRIX, ImageView.ScaleType.FIT_XY, ImageView.ScaleType.FIT_START, ImageView.ScaleType.FIT_CENTER, ImageView.ScaleType.FIT_END, ImageView.ScaleType.CENTER, ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.CENTER_INSIDE};
    private Boolean logoAdjustViewBounds;
    private ImageView.ScaleType logoScaleType;
    private Integer navigationIconTint;
    private boolean subtitleCentered;
    private boolean titleCentered;

    public MaterialToolbar(Context context) {
        this(context, (AttributeSet) null);
    }

    public MaterialToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public MaterialToolbar(android.content.Context r8, android.util.AttributeSet r9, int r10) {
        /*
            r7 = this;
            int r4 = DEF_STYLE_RES
            android.content.Context r0 = com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap(r8, r9, r10, r4)
            r7.<init>(r0, r9, r10)
            android.content.Context r8 = r7.getContext()
            int[] r2 = com.google.android.material.R.styleable.MaterialToolbar
            r6 = 0
            int[] r5 = new int[r6]
            r0 = r8
            r1 = r9
            r3 = r10
            android.content.res.TypedArray r0 = com.google.android.material.internal.ThemeEnforcement.obtainStyledAttributes(r0, r1, r2, r3, r4, r5)
            int r1 = com.google.android.material.R.styleable.MaterialToolbar_navigationIconTint
            boolean r1 = r0.hasValue(r1)
            r2 = -1
            if (r1 == 0) goto L_0x002b
            int r1 = com.google.android.material.R.styleable.MaterialToolbar_navigationIconTint
            int r1 = r0.getColor(r1, r2)
            r7.setNavigationIconTint(r1)
        L_0x002b:
            int r1 = com.google.android.material.R.styleable.MaterialToolbar_titleCentered
            boolean r1 = r0.getBoolean(r1, r6)
            r7.titleCentered = r1
            int r1 = com.google.android.material.R.styleable.MaterialToolbar_subtitleCentered
            boolean r1 = r0.getBoolean(r1, r6)
            r7.subtitleCentered = r1
            int r1 = com.google.android.material.R.styleable.MaterialToolbar_logoScaleType
            int r1 = r0.getInt(r1, r2)
            if (r1 < 0) goto L_0x004c
            android.widget.ImageView$ScaleType[] r2 = LOGO_SCALE_TYPE_ARRAY
            int r3 = r2.length
            if (r1 >= r3) goto L_0x004c
            r2 = r2[r1]
            r7.logoScaleType = r2
        L_0x004c:
            int r2 = com.google.android.material.R.styleable.MaterialToolbar_logoAdjustViewBounds
            boolean r2 = r0.hasValue(r2)
            if (r2 == 0) goto L_0x0060
            int r2 = com.google.android.material.R.styleable.MaterialToolbar_logoAdjustViewBounds
            boolean r2 = r0.getBoolean(r2, r6)
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)
            r7.logoAdjustViewBounds = r2
        L_0x0060:
            r0.recycle()
            r7.initBackground(r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.appbar.MaterialToolbar.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private Pair<Integer, Integer> calculateTitleBoundLimits(TextView titleTextView, TextView subtitleTextView) {
        int measuredWidth = getMeasuredWidth();
        int i = measuredWidth / 2;
        int paddingLeft = getPaddingLeft();
        int paddingRight = measuredWidth - getPaddingRight();
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View childAt = getChildAt(i2);
            if (!(childAt.getVisibility() == 8 || childAt == titleTextView || childAt == subtitleTextView)) {
                if (childAt.getRight() < i && childAt.getRight() > paddingLeft) {
                    paddingLeft = childAt.getRight();
                }
                if (childAt.getLeft() > i && childAt.getLeft() < paddingRight) {
                    paddingRight = childAt.getLeft();
                }
            }
        }
        return new Pair<>(Integer.valueOf(paddingLeft), Integer.valueOf(paddingRight));
    }

    private void initBackground(Context context) {
        Drawable background = getBackground();
        if (background == null || (background instanceof ColorDrawable)) {
            MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
            materialShapeDrawable.setFillColor(ColorStateList.valueOf(background != null ? ((ColorDrawable) background).getColor() : 0));
            materialShapeDrawable.initializeElevationOverlay(context);
            materialShapeDrawable.setElevation(ViewCompat.getElevation(this));
            ViewCompat.setBackground(this, materialShapeDrawable);
        }
    }

    private void layoutTitleCenteredHorizontally(View titleView, Pair<Integer, Integer> pair) {
        int measuredWidth = getMeasuredWidth();
        int measuredWidth2 = titleView.getMeasuredWidth();
        int i = (measuredWidth / 2) - (measuredWidth2 / 2);
        int i2 = i + measuredWidth2;
        int max = Math.max(Math.max(((Integer) pair.first).intValue() - i, 0), Math.max(i2 - ((Integer) pair.second).intValue(), 0));
        if (max > 0) {
            i += max;
            i2 -= max;
            titleView.measure(View.MeasureSpec.makeMeasureSpec(i2 - i, BasicMeasure.EXACTLY), titleView.getMeasuredHeightAndState());
        }
        titleView.layout(i, titleView.getTop(), i2, titleView.getBottom());
    }

    private void maybeCenterTitleViews() {
        if (this.titleCentered || this.subtitleCentered) {
            TextView titleTextView = ToolbarUtils.getTitleTextView(this);
            TextView subtitleTextView = ToolbarUtils.getSubtitleTextView(this);
            if (titleTextView != null || subtitleTextView != null) {
                Pair<Integer, Integer> calculateTitleBoundLimits = calculateTitleBoundLimits(titleTextView, subtitleTextView);
                if (this.titleCentered && titleTextView != null) {
                    layoutTitleCenteredHorizontally(titleTextView, calculateTitleBoundLimits);
                }
                if (this.subtitleCentered && subtitleTextView != null) {
                    layoutTitleCenteredHorizontally(subtitleTextView, calculateTitleBoundLimits);
                }
            }
        }
    }

    private Drawable maybeTintNavigationIcon(Drawable navigationIcon) {
        if (navigationIcon == null || this.navigationIconTint == null) {
            return navigationIcon;
        }
        Drawable wrap = DrawableCompat.wrap(navigationIcon.mutate());
        DrawableCompat.setTint(wrap, this.navigationIconTint.intValue());
        return wrap;
    }

    private void updateLogoImageView() {
        ImageView logoImageView = ToolbarUtils.getLogoImageView(this);
        if (logoImageView != null) {
            Boolean bool = this.logoAdjustViewBounds;
            if (bool != null) {
                logoImageView.setAdjustViewBounds(bool.booleanValue());
            }
            ImageView.ScaleType scaleType = this.logoScaleType;
            if (scaleType != null) {
                logoImageView.setScaleType(scaleType);
            }
        }
    }

    public ImageView.ScaleType getLogoScaleType() {
        return this.logoScaleType;
    }

    public Integer getNavigationIconTint() {
        return this.navigationIconTint;
    }

    public boolean isLogoAdjustViewBounds() {
        Boolean bool = this.logoAdjustViewBounds;
        return bool != null && bool.booleanValue();
    }

    public boolean isSubtitleCentered() {
        return this.subtitleCentered;
    }

    public boolean isTitleCentered() {
        return this.titleCentered;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.setParentAbsoluteElevation(this);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        maybeCenterTitleViews();
        updateLogoImageView();
    }

    public void setElevation(float elevation) {
        super.setElevation(elevation);
        MaterialShapeUtils.setElevation(this, elevation);
    }

    public void setLogoAdjustViewBounds(boolean logoAdjustViewBounds2) {
        Boolean bool = this.logoAdjustViewBounds;
        if (bool == null || bool.booleanValue() != logoAdjustViewBounds2) {
            this.logoAdjustViewBounds = Boolean.valueOf(logoAdjustViewBounds2);
            requestLayout();
        }
    }

    public void setLogoScaleType(ImageView.ScaleType logoScaleType2) {
        if (this.logoScaleType != logoScaleType2) {
            this.logoScaleType = logoScaleType2;
            requestLayout();
        }
    }

    public void setNavigationIcon(Drawable drawable) {
        super.setNavigationIcon(maybeTintNavigationIcon(drawable));
    }

    public void setNavigationIconTint(int navigationIconTint2) {
        this.navigationIconTint = Integer.valueOf(navigationIconTint2);
        Drawable navigationIcon = getNavigationIcon();
        if (navigationIcon != null) {
            setNavigationIcon(navigationIcon);
        }
    }

    public void setSubtitleCentered(boolean subtitleCentered2) {
        if (this.subtitleCentered != subtitleCentered2) {
            this.subtitleCentered = subtitleCentered2;
            requestLayout();
        }
    }

    public void setTitleCentered(boolean titleCentered2) {
        if (this.titleCentered != titleCentered2) {
            this.titleCentered = titleCentered2;
            requestLayout();
        }
    }
}

package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.widget.TextView;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

final class CalendarItemStyle {
    private final ColorStateList backgroundColor;
    private final Rect insets;
    private final ShapeAppearanceModel itemShape;
    private final ColorStateList strokeColor;
    private final int strokeWidth;
    private final ColorStateList textColor;

    private CalendarItemStyle(ColorStateList backgroundColor2, ColorStateList textColor2, ColorStateList strokeColor2, int strokeWidth2, ShapeAppearanceModel itemShape2, Rect insets2) {
        Preconditions.checkArgumentNonnegative(insets2.left);
        Preconditions.checkArgumentNonnegative(insets2.top);
        Preconditions.checkArgumentNonnegative(insets2.right);
        Preconditions.checkArgumentNonnegative(insets2.bottom);
        this.insets = insets2;
        this.textColor = textColor2;
        this.backgroundColor = backgroundColor2;
        this.strokeColor = strokeColor2;
        this.strokeWidth = strokeWidth2;
        this.itemShape = itemShape2;
    }

    static CalendarItemStyle create(Context context, int materialCalendarItemStyle) {
        Context context2 = context;
        int i = materialCalendarItemStyle;
        Preconditions.checkArgument(i != 0, "Cannot create a CalendarItemStyle with a styleResId of 0");
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(i, R.styleable.MaterialCalendarItem);
        Rect rect = new Rect(obtainStyledAttributes.getDimensionPixelOffset(R.styleable.MaterialCalendarItem_android_insetLeft, 0), obtainStyledAttributes.getDimensionPixelOffset(R.styleable.MaterialCalendarItem_android_insetTop, 0), obtainStyledAttributes.getDimensionPixelOffset(R.styleable.MaterialCalendarItem_android_insetRight, 0), obtainStyledAttributes.getDimensionPixelOffset(R.styleable.MaterialCalendarItem_android_insetBottom, 0));
        ColorStateList colorStateList = MaterialResources.getColorStateList(context2, obtainStyledAttributes, R.styleable.MaterialCalendarItem_itemFillColor);
        ColorStateList colorStateList2 = MaterialResources.getColorStateList(context2, obtainStyledAttributes, R.styleable.MaterialCalendarItem_itemTextColor);
        ColorStateList colorStateList3 = MaterialResources.getColorStateList(context2, obtainStyledAttributes, R.styleable.MaterialCalendarItem_itemStrokeColor);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.MaterialCalendarItem_itemStrokeWidth, 0);
        int resourceId = obtainStyledAttributes.getResourceId(R.styleable.MaterialCalendarItem_itemShapeAppearance, 0);
        ShapeAppearanceModel build = ShapeAppearanceModel.builder(context2, resourceId, obtainStyledAttributes.getResourceId(R.styleable.MaterialCalendarItem_itemShapeAppearanceOverlay, 0)).build();
        obtainStyledAttributes.recycle();
        int i2 = resourceId;
        return new CalendarItemStyle(colorStateList, colorStateList2, colorStateList3, dimensionPixelSize, build, rect);
    }

    /* access modifiers changed from: package-private */
    public int getBottomInset() {
        return this.insets.bottom;
    }

    /* access modifiers changed from: package-private */
    public int getLeftInset() {
        return this.insets.left;
    }

    /* access modifiers changed from: package-private */
    public int getRightInset() {
        return this.insets.right;
    }

    /* access modifiers changed from: package-private */
    public int getTopInset() {
        return this.insets.top;
    }

    /* access modifiers changed from: package-private */
    public void styleItem(TextView item) {
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        MaterialShapeDrawable materialShapeDrawable2 = new MaterialShapeDrawable();
        materialShapeDrawable.setShapeAppearanceModel(this.itemShape);
        materialShapeDrawable2.setShapeAppearanceModel(this.itemShape);
        materialShapeDrawable.setFillColor(this.backgroundColor);
        materialShapeDrawable.setStroke((float) this.strokeWidth, this.strokeColor);
        item.setTextColor(this.textColor);
        ViewCompat.setBackground(item, new InsetDrawable(Build.VERSION.SDK_INT >= 21 ? new RippleDrawable(this.textColor.withAlpha(30), materialShapeDrawable, materialShapeDrawable2) : materialShapeDrawable, this.insets.left, this.insets.top, this.insets.right, this.insets.bottom));
    }
}

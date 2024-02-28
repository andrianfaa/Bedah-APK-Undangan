package com.google.android.material.divider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.R;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;

public class MaterialDividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_MaterialDivider;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private int color;
    private Drawable dividerDrawable;
    private int insetEnd;
    private int insetStart;
    private boolean lastItemDecorated;
    private int orientation;
    private final Rect tempRect;
    private int thickness;

    public MaterialDividerItemDecoration(Context context, int orientation2) {
        this(context, (AttributeSet) null, orientation2);
    }

    public MaterialDividerItemDecoration(Context context, AttributeSet attrs, int orientation2) {
        this(context, attrs, R.attr.materialDividerStyle, orientation2);
    }

    public MaterialDividerItemDecoration(Context context, AttributeSet attrs, int defStyleAttr, int orientation2) {
        this.tempRect = new Rect();
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attrs, R.styleable.MaterialDivider, defStyleAttr, DEF_STYLE_RES, new int[0]);
        this.color = MaterialResources.getColorStateList(context, obtainStyledAttributes, R.styleable.MaterialDivider_dividerColor).getDefaultColor();
        this.thickness = obtainStyledAttributes.getDimensionPixelSize(R.styleable.MaterialDivider_dividerThickness, context.getResources().getDimensionPixelSize(R.dimen.material_divider_thickness));
        this.insetStart = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.MaterialDivider_dividerInsetStart, 0);
        this.insetEnd = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.MaterialDivider_dividerInsetEnd, 0);
        this.lastItemDecorated = obtainStyledAttributes.getBoolean(R.styleable.MaterialDivider_lastItemDecorated, true);
        obtainStyledAttributes.recycle();
        this.dividerDrawable = new ShapeDrawable();
        setDividerColor(this.color);
        setOrientation(orientation2);
    }

    private void drawForHorizontalOrientation(Canvas canvas, RecyclerView parent) {
        int i;
        int i2;
        canvas.save();
        if (parent.getClipToPadding()) {
            i2 = parent.getPaddingTop();
            i = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), i2, parent.getWidth() - parent.getPaddingRight(), i);
        } else {
            i2 = 0;
            i = parent.getHeight();
        }
        int i3 = i2 + this.insetStart;
        int i4 = i - this.insetEnd;
        int childCount = parent.getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = parent.getChildAt(i5);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(childAt, this.tempRect);
            int round = this.tempRect.right + Math.round(childAt.getTranslationX());
            this.dividerDrawable.setBounds((round - this.dividerDrawable.getIntrinsicWidth()) - this.thickness, i3, round, i4);
            this.dividerDrawable.draw(canvas);
        }
        canvas.restore();
    }

    private void drawForVerticalOrientation(Canvas canvas, RecyclerView parent) {
        int i;
        int i2;
        canvas.save();
        if (parent.getClipToPadding()) {
            i2 = parent.getPaddingLeft();
            i = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(i2, parent.getPaddingTop(), i, parent.getHeight() - parent.getPaddingBottom());
        } else {
            i2 = 0;
            i = parent.getWidth();
        }
        boolean z = true;
        if (ViewCompat.getLayoutDirection(parent) != 1) {
            z = false;
        }
        boolean z2 = z;
        int i3 = i2 + (z2 ? this.insetEnd : this.insetStart);
        int i4 = i - (z2 ? this.insetStart : this.insetEnd);
        int childCount = parent.getChildCount();
        int i5 = this.lastItemDecorated ? childCount : childCount - 1;
        for (int i6 = 0; i6 < i5; i6++) {
            View childAt = parent.getChildAt(i6);
            parent.getDecoratedBoundsWithMargins(childAt, this.tempRect);
            int round = this.tempRect.bottom + Math.round(childAt.getTranslationY());
            this.dividerDrawable.setBounds(i3, (round - this.dividerDrawable.getIntrinsicHeight()) - this.thickness, i4, round);
            this.dividerDrawable.draw(canvas);
        }
        canvas.restore();
    }

    public int getDividerColor() {
        return this.color;
    }

    public int getDividerInsetEnd() {
        return this.insetEnd;
    }

    public int getDividerInsetStart() {
        return this.insetStart;
    }

    public int getDividerThickness() {
        return this.thickness;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, 0);
        if (this.orientation == 1) {
            outRect.bottom = this.dividerDrawable.getIntrinsicHeight() + this.thickness;
        } else {
            outRect.right = this.dividerDrawable.getIntrinsicWidth() + this.thickness;
        }
    }

    public int getOrientation() {
        return this.orientation;
    }

    public boolean isLastItemDecorated() {
        return this.lastItemDecorated;
    }

    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() != null) {
            if (this.orientation == 1) {
                drawForVerticalOrientation(canvas, parent);
            } else {
                drawForHorizontalOrientation(canvas, parent);
            }
        }
    }

    public void setDividerColor(int color2) {
        this.color = color2;
        Drawable wrap = DrawableCompat.wrap(this.dividerDrawable);
        this.dividerDrawable = wrap;
        DrawableCompat.setTint(wrap, color2);
    }

    public void setDividerColorResource(Context context, int colorId) {
        setDividerColor(ContextCompat.getColor(context, colorId));
    }

    public void setDividerInsetEnd(int insetEnd2) {
        this.insetEnd = insetEnd2;
    }

    public void setDividerInsetEndResource(Context context, int insetEndId) {
        setDividerInsetEnd(context.getResources().getDimensionPixelOffset(insetEndId));
    }

    public void setDividerInsetStart(int insetStart2) {
        this.insetStart = insetStart2;
    }

    public void setDividerInsetStartResource(Context context, int insetStartId) {
        setDividerInsetStart(context.getResources().getDimensionPixelOffset(insetStartId));
    }

    public void setDividerThickness(int thickness2) {
        this.thickness = thickness2;
    }

    public void setDividerThicknessResource(Context context, int thicknessId) {
        setDividerThickness(context.getResources().getDimensionPixelSize(thicknessId));
    }

    public void setLastItemDecorated(boolean lastItemDecorated2) {
        this.lastItemDecorated = lastItemDecorated2;
    }

    public void setOrientation(int orientation2) {
        if (orientation2 == 0 || orientation2 == 1) {
            this.orientation = orientation2;
            return;
        }
        throw new IllegalArgumentException("Invalid orientation: " + orientation2 + ". It should be either HORIZONTAL or VERTICAL");
    }
}

package com.google.android.material.card;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import com.google.android.material.R;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MaterialCardView extends CardView implements Checkable, Shapeable {
    private static final String ACCESSIBILITY_CLASS_NAME = "androidx.cardview.widget.CardView";
    private static final int[] CHECKABLE_STATE_SET = {16842911};
    public static final int CHECKED_ICON_GRAVITY_BOTTOM_END = 8388693;
    public static final int CHECKED_ICON_GRAVITY_BOTTOM_START = 8388691;
    public static final int CHECKED_ICON_GRAVITY_TOP_END = 8388661;
    public static final int CHECKED_ICON_GRAVITY_TOP_START = 8388659;
    private static final int[] CHECKED_STATE_SET = {16842912};
    private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_CardView;
    private static final int[] DRAGGED_STATE_SET = {R.attr.state_dragged};
    private static final String LOG_TAG = "MaterialCardView";
    private final MaterialCardViewHelper cardViewHelper;
    private boolean checked;
    private boolean dragged;
    private boolean isParentCardViewDoneInitializing;
    private OnCheckedChangeListener onCheckedChangeListener;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CheckedIconGravity {
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(MaterialCardView materialCardView, boolean z);
    }

    public MaterialCardView(Context context) {
        this(context, (AttributeSet) null);
    }

    public MaterialCardView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.materialCardViewStyle);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public MaterialCardView(android.content.Context r8, android.util.AttributeSet r9, int r10) {
        /*
            r7 = this;
            int r6 = DEF_STYLE_RES
            android.content.Context r0 = com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap(r8, r9, r10, r6)
            r7.<init>(r0, r9, r10)
            r0 = 0
            r7.checked = r0
            r7.dragged = r0
            r1 = 1
            r7.isParentCardViewDoneInitializing = r1
            android.content.Context r8 = r7.getContext()
            int[] r2 = com.google.android.material.R.styleable.MaterialCardView
            int[] r5 = new int[r0]
            r0 = r8
            r1 = r9
            r3 = r10
            r4 = r6
            android.content.res.TypedArray r0 = com.google.android.material.internal.ThemeEnforcement.obtainStyledAttributes(r0, r1, r2, r3, r4, r5)
            com.google.android.material.card.MaterialCardViewHelper r1 = new com.google.android.material.card.MaterialCardViewHelper
            r1.<init>(r7, r9, r10, r6)
            r7.cardViewHelper = r1
            android.content.res.ColorStateList r2 = super.getCardBackgroundColor()
            r1.setCardBackgroundColor(r2)
            int r2 = super.getContentPaddingLeft()
            int r3 = super.getContentPaddingTop()
            int r4 = super.getContentPaddingRight()
            int r5 = super.getContentPaddingBottom()
            r1.setUserContentPadding(r2, r3, r4, r5)
            r1.loadFromAttributes(r0)
            r0.recycle()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.card.MaterialCardView.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private void forceRippleRedrawIfNeeded() {
        if (Build.VERSION.SDK_INT > 26) {
            this.cardViewHelper.forceRippleRedraw();
        }
    }

    private RectF getBoundsAsRectF() {
        RectF rectF = new RectF();
        rectF.set(this.cardViewHelper.getBackground().getBounds());
        return rectF;
    }

    public ColorStateList getCardBackgroundColor() {
        return this.cardViewHelper.getCardBackgroundColor();
    }

    public ColorStateList getCardForegroundColor() {
        return this.cardViewHelper.getCardForegroundColor();
    }

    /* access modifiers changed from: package-private */
    public float getCardViewRadius() {
        return super.getRadius();
    }

    public Drawable getCheckedIcon() {
        return this.cardViewHelper.getCheckedIcon();
    }

    public int getCheckedIconGravity() {
        return this.cardViewHelper.getCheckedIconGravity();
    }

    public int getCheckedIconMargin() {
        return this.cardViewHelper.getCheckedIconMargin();
    }

    public int getCheckedIconSize() {
        return this.cardViewHelper.getCheckedIconSize();
    }

    public ColorStateList getCheckedIconTint() {
        return this.cardViewHelper.getCheckedIconTint();
    }

    public int getContentPaddingBottom() {
        return this.cardViewHelper.getUserContentPadding().bottom;
    }

    public int getContentPaddingLeft() {
        return this.cardViewHelper.getUserContentPadding().left;
    }

    public int getContentPaddingRight() {
        return this.cardViewHelper.getUserContentPadding().right;
    }

    public int getContentPaddingTop() {
        return this.cardViewHelper.getUserContentPadding().top;
    }

    public float getProgress() {
        return this.cardViewHelper.getProgress();
    }

    public float getRadius() {
        return this.cardViewHelper.getCornerRadius();
    }

    public ColorStateList getRippleColor() {
        return this.cardViewHelper.getRippleColor();
    }

    public ShapeAppearanceModel getShapeAppearanceModel() {
        return this.cardViewHelper.getShapeAppearanceModel();
    }

    @Deprecated
    public int getStrokeColor() {
        return this.cardViewHelper.getStrokeColor();
    }

    public ColorStateList getStrokeColorStateList() {
        return this.cardViewHelper.getStrokeColorStateList();
    }

    public int getStrokeWidth() {
        return this.cardViewHelper.getStrokeWidth();
    }

    public boolean isCheckable() {
        MaterialCardViewHelper materialCardViewHelper = this.cardViewHelper;
        return materialCardViewHelper != null && materialCardViewHelper.isCheckable();
    }

    public boolean isChecked() {
        return this.checked;
    }

    public boolean isDragged() {
        return this.dragged;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.setParentAbsoluteElevation(this, this.cardViewHelper.getBackground());
    }

    /* access modifiers changed from: protected */
    public int[] onCreateDrawableState(int extraSpace) {
        int[] onCreateDrawableState = super.onCreateDrawableState(extraSpace + 3);
        if (isCheckable()) {
            mergeDrawableStates(onCreateDrawableState, CHECKABLE_STATE_SET);
        }
        if (isChecked()) {
            mergeDrawableStates(onCreateDrawableState, CHECKED_STATE_SET);
        }
        if (isDragged()) {
            mergeDrawableStates(onCreateDrawableState, DRAGGED_STATE_SET);
        }
        return onCreateDrawableState;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(ACCESSIBILITY_CLASS_NAME);
        accessibilityEvent.setChecked(isChecked());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(ACCESSIBILITY_CLASS_NAME);
        info.setCheckable(isCheckable());
        info.setClickable(isClickable());
        info.setChecked(isChecked());
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.cardViewHelper.recalculateCheckedIconPosition(getMeasuredWidth(), getMeasuredHeight());
    }

    /* access modifiers changed from: package-private */
    public void setAncestorContentPadding(int left, int top, int right, int bottom) {
        super.setContentPadding(left, top, right, bottom);
    }

    public void setBackground(Drawable drawable) {
        setBackgroundDrawable(drawable);
    }

    public void setBackgroundDrawable(Drawable drawable) {
        if (this.isParentCardViewDoneInitializing) {
            if (!this.cardViewHelper.isBackgroundOverwritten()) {
                Log.i(LOG_TAG, "Setting a custom background is not supported.");
                this.cardViewHelper.setBackgroundOverwritten(true);
            }
            super.setBackgroundDrawable(drawable);
        }
    }

    /* access modifiers changed from: package-private */
    public void setBackgroundInternal(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
    }

    public void setCardBackgroundColor(int color) {
        this.cardViewHelper.setCardBackgroundColor(ColorStateList.valueOf(color));
    }

    public void setCardBackgroundColor(ColorStateList color) {
        this.cardViewHelper.setCardBackgroundColor(color);
    }

    public void setCardElevation(float elevation) {
        super.setCardElevation(elevation);
        this.cardViewHelper.updateElevation();
    }

    public void setCardForegroundColor(ColorStateList foregroundColor) {
        this.cardViewHelper.setCardForegroundColor(foregroundColor);
    }

    public void setCheckable(boolean checkable) {
        this.cardViewHelper.setCheckable(checkable);
    }

    public void setChecked(boolean checked2) {
        if (this.checked != checked2) {
            toggle();
        }
    }

    public void setCheckedIcon(Drawable checkedIcon) {
        this.cardViewHelper.setCheckedIcon(checkedIcon);
    }

    public void setCheckedIconGravity(int checkedIconGravity) {
        if (this.cardViewHelper.getCheckedIconGravity() != checkedIconGravity) {
            this.cardViewHelper.setCheckedIconGravity(checkedIconGravity);
        }
    }

    public void setCheckedIconMargin(int checkedIconMargin) {
        this.cardViewHelper.setCheckedIconMargin(checkedIconMargin);
    }

    public void setCheckedIconMarginResource(int checkedIconMarginResId) {
        if (checkedIconMarginResId != -1) {
            this.cardViewHelper.setCheckedIconMargin(getResources().getDimensionPixelSize(checkedIconMarginResId));
        }
    }

    public void setCheckedIconResource(int id) {
        this.cardViewHelper.setCheckedIcon(AppCompatResources.getDrawable(getContext(), id));
    }

    public void setCheckedIconSize(int checkedIconSize) {
        this.cardViewHelper.setCheckedIconSize(checkedIconSize);
    }

    public void setCheckedIconSizeResource(int checkedIconSizeResId) {
        if (checkedIconSizeResId != 0) {
            this.cardViewHelper.setCheckedIconSize(getResources().getDimensionPixelSize(checkedIconSizeResId));
        }
    }

    public void setCheckedIconTint(ColorStateList checkedIconTint) {
        this.cardViewHelper.setCheckedIconTint(checkedIconTint);
    }

    public void setClickable(boolean clickable) {
        super.setClickable(clickable);
        MaterialCardViewHelper materialCardViewHelper = this.cardViewHelper;
        if (materialCardViewHelper != null) {
            materialCardViewHelper.updateClickable();
        }
    }

    public void setContentPadding(int left, int top, int right, int bottom) {
        this.cardViewHelper.setUserContentPadding(left, top, right, bottom);
    }

    public void setDragged(boolean dragged2) {
        if (this.dragged != dragged2) {
            this.dragged = dragged2;
            refreshDrawableState();
            forceRippleRedrawIfNeeded();
            invalidate();
        }
    }

    public void setMaxCardElevation(float maxCardElevation) {
        super.setMaxCardElevation(maxCardElevation);
        this.cardViewHelper.updateInsets();
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    public void setPreventCornerOverlap(boolean preventCornerOverlap) {
        super.setPreventCornerOverlap(preventCornerOverlap);
        this.cardViewHelper.updateInsets();
        this.cardViewHelper.updateContentPadding();
    }

    public void setProgress(float progress) {
        this.cardViewHelper.setProgress(progress);
    }

    public void setRadius(float radius) {
        super.setRadius(radius);
        this.cardViewHelper.setCornerRadius(radius);
    }

    public void setRippleColor(ColorStateList rippleColor) {
        this.cardViewHelper.setRippleColor(rippleColor);
    }

    public void setRippleColorResource(int rippleColorResourceId) {
        this.cardViewHelper.setRippleColor(AppCompatResources.getColorStateList(getContext(), rippleColorResourceId));
    }

    public void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        if (Build.VERSION.SDK_INT >= 21) {
            setClipToOutline(shapeAppearanceModel.isRoundRect(getBoundsAsRectF()));
        }
        this.cardViewHelper.setShapeAppearanceModel(shapeAppearanceModel);
    }

    public void setStrokeColor(int strokeColor) {
        setStrokeColor(ColorStateList.valueOf(strokeColor));
    }

    public void setStrokeColor(ColorStateList strokeColor) {
        this.cardViewHelper.setStrokeColor(strokeColor);
        invalidate();
    }

    public void setStrokeWidth(int strokeWidth) {
        this.cardViewHelper.setStrokeWidth(strokeWidth);
        invalidate();
    }

    public void setUseCompatPadding(boolean useCompatPadding) {
        super.setUseCompatPadding(useCompatPadding);
        this.cardViewHelper.updateInsets();
        this.cardViewHelper.updateContentPadding();
    }

    public void toggle() {
        if (isCheckable() && isEnabled()) {
            this.checked = !this.checked;
            refreshDrawableState();
            forceRippleRedrawIfNeeded();
            this.cardViewHelper.setChecked(this.checked);
            OnCheckedChangeListener onCheckedChangeListener2 = this.onCheckedChangeListener;
            if (onCheckedChangeListener2 != null) {
                onCheckedChangeListener2.onCheckedChanged(this, this.checked);
            }
        }
    }
}

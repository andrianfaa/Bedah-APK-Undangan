package com.google.android.material.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.CompoundButton;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class MaterialButton extends AppCompatButton implements Checkable, Shapeable {
    private static final int[] CHECKABLE_STATE_SET = {16842911};
    private static final int[] CHECKED_STATE_SET = {16842912};
    private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_Button;
    public static final int ICON_GRAVITY_END = 3;
    public static final int ICON_GRAVITY_START = 1;
    public static final int ICON_GRAVITY_TEXT_END = 4;
    public static final int ICON_GRAVITY_TEXT_START = 2;
    public static final int ICON_GRAVITY_TEXT_TOP = 32;
    public static final int ICON_GRAVITY_TOP = 16;
    private static final String LOG_TAG = "MaterialButton";
    private boolean broadcasting;
    private boolean checked;
    private Drawable icon;
    private int iconGravity;
    private int iconLeft;
    private int iconPadding;
    private int iconSize;
    private ColorStateList iconTint;
    private PorterDuff.Mode iconTintMode;
    private int iconTop;
    private final MaterialButtonHelper materialButtonHelper;
    private final LinkedHashSet<OnCheckedChangeListener> onCheckedChangeListeners;
    private OnPressedChangeListener onPressedChangeListenerInternal;

    @Retention(RetentionPolicy.SOURCE)
    public @interface IconGravity {
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(MaterialButton materialButton, boolean z);
    }

    interface OnPressedChangeListener {
        void onPressedChanged(MaterialButton materialButton, boolean z);
    }

    static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, (ClassLoader) null);
            }

            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        boolean checked;

        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            if (loader == null) {
                ClassLoader loader2 = getClass().getClassLoader();
            }
            readFromParcel(source);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private void readFromParcel(Parcel in) {
            boolean z = true;
            if (in.readInt() != 1) {
                z = false;
            }
            this.checked = z;
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.checked ? 1 : 0);
        }
    }

    public MaterialButton(Context context) {
        this(context, (AttributeSet) null);
    }

    public MaterialButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.materialButtonStyle);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public MaterialButton(android.content.Context r9, android.util.AttributeSet r10, int r11) {
        /*
            r8 = this;
            int r6 = DEF_STYLE_RES
            android.content.Context r0 = com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap(r9, r10, r11, r6)
            r8.<init>(r0, r10, r11)
            java.util.LinkedHashSet r0 = new java.util.LinkedHashSet
            r0.<init>()
            r8.onCheckedChangeListeners = r0
            r7 = 0
            r8.checked = r7
            r8.broadcasting = r7
            android.content.Context r9 = r8.getContext()
            int[] r2 = com.google.android.material.R.styleable.MaterialButton
            int[] r5 = new int[r7]
            r0 = r9
            r1 = r10
            r3 = r11
            r4 = r6
            android.content.res.TypedArray r0 = com.google.android.material.internal.ThemeEnforcement.obtainStyledAttributes(r0, r1, r2, r3, r4, r5)
            int r1 = com.google.android.material.R.styleable.MaterialButton_iconPadding
            int r1 = r0.getDimensionPixelSize(r1, r7)
            r8.iconPadding = r1
            int r1 = com.google.android.material.R.styleable.MaterialButton_iconTintMode
            r2 = -1
            int r1 = r0.getInt(r1, r2)
            android.graphics.PorterDuff$Mode r2 = android.graphics.PorterDuff.Mode.SRC_IN
            android.graphics.PorterDuff$Mode r1 = com.google.android.material.internal.ViewUtils.parseTintMode(r1, r2)
            r8.iconTintMode = r1
            android.content.Context r1 = r8.getContext()
            int r2 = com.google.android.material.R.styleable.MaterialButton_iconTint
            android.content.res.ColorStateList r1 = com.google.android.material.resources.MaterialResources.getColorStateList((android.content.Context) r1, (android.content.res.TypedArray) r0, (int) r2)
            r8.iconTint = r1
            android.content.Context r1 = r8.getContext()
            int r2 = com.google.android.material.R.styleable.MaterialButton_icon
            android.graphics.drawable.Drawable r1 = com.google.android.material.resources.MaterialResources.getDrawable(r1, r0, r2)
            r8.icon = r1
            int r1 = com.google.android.material.R.styleable.MaterialButton_iconGravity
            r2 = 1
            int r1 = r0.getInteger(r1, r2)
            r8.iconGravity = r1
            int r1 = com.google.android.material.R.styleable.MaterialButton_iconSize
            int r1 = r0.getDimensionPixelSize(r1, r7)
            r8.iconSize = r1
            com.google.android.material.shape.ShapeAppearanceModel$Builder r1 = com.google.android.material.shape.ShapeAppearanceModel.builder((android.content.Context) r9, (android.util.AttributeSet) r10, (int) r11, (int) r6)
            com.google.android.material.shape.ShapeAppearanceModel r1 = r1.build()
            com.google.android.material.button.MaterialButtonHelper r3 = new com.google.android.material.button.MaterialButtonHelper
            r3.<init>(r8, r1)
            r8.materialButtonHelper = r3
            r3.loadFromAttributes(r0)
            r0.recycle()
            int r3 = r8.iconPadding
            r8.setCompoundDrawablePadding(r3)
            android.graphics.drawable.Drawable r3 = r8.icon
            if (r3 == 0) goto L_0x0086
            r7 = r2
        L_0x0086:
            r8.updateIcon(r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.button.MaterialButton.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private String getA11yClassName() {
        return (isCheckable() ? CompoundButton.class : Button.class).getName();
    }

    private Layout.Alignment getActualTextAlignment() {
        if (Build.VERSION.SDK_INT < 17) {
            return getGravityTextAlignment();
        }
        switch (getTextAlignment()) {
            case 1:
                return getGravityTextAlignment();
            case 3:
            case 6:
                return Layout.Alignment.ALIGN_OPPOSITE;
            case 4:
                return Layout.Alignment.ALIGN_CENTER;
            default:
                return Layout.Alignment.ALIGN_NORMAL;
        }
    }

    private Layout.Alignment getGravityTextAlignment() {
        switch (getGravity() & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
            case 1:
                return Layout.Alignment.ALIGN_CENTER;
            case 5:
            case GravityCompat.END:
                return Layout.Alignment.ALIGN_OPPOSITE;
            default:
                return Layout.Alignment.ALIGN_NORMAL;
        }
    }

    private int getTextHeight() {
        TextPaint paint = getPaint();
        String obj = getText().toString();
        if (getTransformationMethod() != null) {
            obj = getTransformationMethod().getTransformation(obj, this).toString();
        }
        Rect rect = new Rect();
        paint.getTextBounds(obj, 0, obj.length(), rect);
        return Math.min(rect.height(), getLayout().getHeight());
    }

    private int getTextWidth() {
        TextPaint paint = getPaint();
        String obj = getText().toString();
        if (getTransformationMethod() != null) {
            obj = getTransformationMethod().getTransformation(obj, this).toString();
        }
        return Math.min((int) paint.measureText(obj), getLayout().getEllipsizedWidth());
    }

    private boolean isIconEnd() {
        int i = this.iconGravity;
        return i == 3 || i == 4;
    }

    private boolean isIconStart() {
        int i = this.iconGravity;
        return i == 1 || i == 2;
    }

    private boolean isIconTop() {
        int i = this.iconGravity;
        return i == 16 || i == 32;
    }

    private boolean isLayoutRTL() {
        return ViewCompat.getLayoutDirection(this) == 1;
    }

    private boolean isUsingOriginalBackground() {
        MaterialButtonHelper materialButtonHelper2 = this.materialButtonHelper;
        return materialButtonHelper2 != null && !materialButtonHelper2.isBackgroundOverwritten();
    }

    private void resetIconDrawable() {
        if (isIconStart()) {
            TextViewCompat.setCompoundDrawablesRelative(this, this.icon, (Drawable) null, (Drawable) null, (Drawable) null);
        } else if (isIconEnd()) {
            TextViewCompat.setCompoundDrawablesRelative(this, (Drawable) null, (Drawable) null, this.icon, (Drawable) null);
        } else if (isIconTop()) {
            TextViewCompat.setCompoundDrawablesRelative(this, (Drawable) null, this.icon, (Drawable) null, (Drawable) null);
        }
    }

    private void updateIcon(boolean needsIconReset) {
        Drawable drawable = this.icon;
        boolean z = true;
        if (drawable != null) {
            Drawable mutate = DrawableCompat.wrap(drawable).mutate();
            this.icon = mutate;
            DrawableCompat.setTintList(mutate, this.iconTint);
            PorterDuff.Mode mode = this.iconTintMode;
            if (mode != null) {
                DrawableCompat.setTintMode(this.icon, mode);
            }
            int i = this.iconSize;
            if (i == 0) {
                i = this.icon.getIntrinsicWidth();
            }
            int i2 = this.iconSize;
            if (i2 == 0) {
                i2 = this.icon.getIntrinsicHeight();
            }
            Drawable drawable2 = this.icon;
            int i3 = this.iconLeft;
            int i4 = this.iconTop;
            drawable2.setBounds(i3, i4, i3 + i, i4 + i2);
            this.icon.setVisible(true, needsIconReset);
        }
        if (needsIconReset) {
            resetIconDrawable();
            return;
        }
        Drawable[] compoundDrawablesRelative = TextViewCompat.getCompoundDrawablesRelative(this);
        Drawable drawable3 = compoundDrawablesRelative[0];
        Drawable drawable4 = compoundDrawablesRelative[1];
        Drawable drawable5 = compoundDrawablesRelative[2];
        if ((!isIconStart() || drawable3 == this.icon) && ((!isIconEnd() || drawable5 == this.icon) && (!isIconTop() || drawable4 == this.icon))) {
            z = false;
        }
        if (z) {
            resetIconDrawable();
        }
    }

    private void updateIconPosition(int buttonWidth, int buttonHeight) {
        if (this.icon != null && getLayout() != null) {
            if (isIconStart() || isIconEnd()) {
                this.iconTop = 0;
                Layout.Alignment actualTextAlignment = getActualTextAlignment();
                int i = this.iconGravity;
                boolean z = true;
                if (i == 1 || i == 3 || ((i == 2 && actualTextAlignment == Layout.Alignment.ALIGN_NORMAL) || (this.iconGravity == 4 && actualTextAlignment == Layout.Alignment.ALIGN_OPPOSITE))) {
                    this.iconLeft = 0;
                    updateIcon(false);
                    return;
                }
                int i2 = this.iconSize;
                if (i2 == 0) {
                    i2 = this.icon.getIntrinsicWidth();
                }
                int textWidth = ((((buttonWidth - getTextWidth()) - ViewCompat.getPaddingEnd(this)) - i2) - this.iconPadding) - ViewCompat.getPaddingStart(this);
                int i3 = actualTextAlignment == Layout.Alignment.ALIGN_CENTER ? textWidth / 2 : textWidth;
                boolean isLayoutRTL = isLayoutRTL();
                if (this.iconGravity != 4) {
                    z = false;
                }
                if (isLayoutRTL != z) {
                    i3 = -i3;
                }
                if (this.iconLeft != i3) {
                    this.iconLeft = i3;
                    updateIcon(false);
                }
            } else if (isIconTop()) {
                this.iconLeft = 0;
                if (this.iconGravity == 16) {
                    this.iconTop = 0;
                    updateIcon(false);
                    return;
                }
                int i4 = this.iconSize;
                if (i4 == 0) {
                    i4 = this.icon.getIntrinsicHeight();
                }
                int textHeight = (((((buttonHeight - getTextHeight()) - getPaddingTop()) - i4) - this.iconPadding) - getPaddingBottom()) / 2;
                if (this.iconTop != textHeight) {
                    this.iconTop = textHeight;
                    updateIcon(false);
                }
            }
        }
    }

    public void addOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListeners.add(listener);
    }

    public void clearOnCheckedChangeListeners() {
        this.onCheckedChangeListeners.clear();
    }

    public ColorStateList getBackgroundTintList() {
        return getSupportBackgroundTintList();
    }

    public PorterDuff.Mode getBackgroundTintMode() {
        return getSupportBackgroundTintMode();
    }

    public int getCornerRadius() {
        if (isUsingOriginalBackground()) {
            return this.materialButtonHelper.getCornerRadius();
        }
        return 0;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public int getIconGravity() {
        return this.iconGravity;
    }

    public int getIconPadding() {
        return this.iconPadding;
    }

    public int getIconSize() {
        return this.iconSize;
    }

    public ColorStateList getIconTint() {
        return this.iconTint;
    }

    public PorterDuff.Mode getIconTintMode() {
        return this.iconTintMode;
    }

    public int getInsetBottom() {
        return this.materialButtonHelper.getInsetBottom();
    }

    public int getInsetTop() {
        return this.materialButtonHelper.getInsetTop();
    }

    public ColorStateList getRippleColor() {
        if (isUsingOriginalBackground()) {
            return this.materialButtonHelper.getRippleColor();
        }
        return null;
    }

    public ShapeAppearanceModel getShapeAppearanceModel() {
        if (isUsingOriginalBackground()) {
            return this.materialButtonHelper.getShapeAppearanceModel();
        }
        throw new IllegalStateException("Attempted to get ShapeAppearanceModel from a MaterialButton which has an overwritten background.");
    }

    public ColorStateList getStrokeColor() {
        if (isUsingOriginalBackground()) {
            return this.materialButtonHelper.getStrokeColor();
        }
        return null;
    }

    public int getStrokeWidth() {
        if (isUsingOriginalBackground()) {
            return this.materialButtonHelper.getStrokeWidth();
        }
        return 0;
    }

    public ColorStateList getSupportBackgroundTintList() {
        return isUsingOriginalBackground() ? this.materialButtonHelper.getSupportBackgroundTintList() : super.getSupportBackgroundTintList();
    }

    public PorterDuff.Mode getSupportBackgroundTintMode() {
        return isUsingOriginalBackground() ? this.materialButtonHelper.getSupportBackgroundTintMode() : super.getSupportBackgroundTintMode();
    }

    public boolean isCheckable() {
        MaterialButtonHelper materialButtonHelper2 = this.materialButtonHelper;
        return materialButtonHelper2 != null && materialButtonHelper2.isCheckable();
    }

    public boolean isChecked() {
        return this.checked;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isUsingOriginalBackground()) {
            MaterialShapeUtils.setParentAbsoluteElevation(this, this.materialButtonHelper.getMaterialShapeDrawable());
        }
    }

    /* access modifiers changed from: protected */
    public int[] onCreateDrawableState(int extraSpace) {
        int[] onCreateDrawableState = super.onCreateDrawableState(extraSpace + 2);
        if (isCheckable()) {
            mergeDrawableStates(onCreateDrawableState, CHECKABLE_STATE_SET);
        }
        if (isChecked()) {
            mergeDrawableStates(onCreateDrawableState, CHECKED_STATE_SET);
        }
        return onCreateDrawableState;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(getA11yClassName());
        accessibilityEvent.setChecked(isChecked());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(getA11yClassName());
        info.setCheckable(isCheckable());
        info.setChecked(isChecked());
        info.setClickable(isClickable());
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        MaterialButtonHelper materialButtonHelper2;
        super.onLayout(changed, left, top, right, bottom);
        if (Build.VERSION.SDK_INT == 21 && (materialButtonHelper2 = this.materialButtonHelper) != null) {
            materialButtonHelper2.updateMaskBounds(bottom - top, right - left);
        }
        updateIconPosition(getMeasuredWidth(), getMeasuredHeight());
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setChecked(savedState.checked);
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.checked = this.checked;
        return savedState;
    }

    /* access modifiers changed from: protected */
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        super.onTextChanged(charSequence, i, i1, i2);
        updateIconPosition(getMeasuredWidth(), getMeasuredHeight());
    }

    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    public void refreshDrawableState() {
        super.refreshDrawableState();
        if (this.icon != null) {
            if (this.icon.setState(getDrawableState())) {
                invalidate();
            }
        }
    }

    public void removeOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListeners.remove(listener);
    }

    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }

    public void setBackgroundColor(int color) {
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.setBackgroundColor(color);
        } else {
            super.setBackgroundColor(color);
        }
    }

    public void setBackgroundDrawable(Drawable background) {
        if (!isUsingOriginalBackground()) {
            super.setBackgroundDrawable(background);
        } else if (background != getBackground()) {
            Log.w(LOG_TAG, "MaterialButton manages its own background to control elevation, shape, color and states. Consider using backgroundTint, shapeAppearance and other attributes where available. A custom background will ignore these attributes and you should consider handling interaction states such as pressed, focused and disabled");
            this.materialButtonHelper.setBackgroundOverwritten();
            super.setBackgroundDrawable(background);
        } else {
            getBackground().setState(background.getState());
        }
    }

    public void setBackgroundResource(int backgroundResourceId) {
        Drawable drawable = null;
        if (backgroundResourceId != 0) {
            drawable = AppCompatResources.getDrawable(getContext(), backgroundResourceId);
        }
        setBackgroundDrawable(drawable);
    }

    public void setBackgroundTintList(ColorStateList tintList) {
        setSupportBackgroundTintList(tintList);
    }

    public void setBackgroundTintMode(PorterDuff.Mode tintMode) {
        setSupportBackgroundTintMode(tintMode);
    }

    public void setCheckable(boolean checkable) {
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.setCheckable(checkable);
        }
    }

    public void setChecked(boolean checked2) {
        if (isCheckable() && isEnabled() && this.checked != checked2) {
            this.checked = checked2;
            refreshDrawableState();
            if (getParent() instanceof MaterialButtonToggleGroup) {
                ((MaterialButtonToggleGroup) getParent()).onButtonCheckedStateChanged(this, this.checked);
            }
            if (!this.broadcasting) {
                this.broadcasting = true;
                Iterator it = this.onCheckedChangeListeners.iterator();
                while (it.hasNext()) {
                    ((OnCheckedChangeListener) it.next()).onCheckedChanged(this, this.checked);
                }
                this.broadcasting = false;
            }
        }
    }

    public void setCornerRadius(int cornerRadius) {
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.setCornerRadius(cornerRadius);
        }
    }

    public void setCornerRadiusResource(int cornerRadiusResourceId) {
        if (isUsingOriginalBackground()) {
            setCornerRadius(getResources().getDimensionPixelSize(cornerRadiusResourceId));
        }
    }

    public void setElevation(float elevation) {
        super.setElevation(elevation);
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.getMaterialShapeDrawable().setElevation(elevation);
        }
    }

    public void setIcon(Drawable icon2) {
        if (this.icon != icon2) {
            this.icon = icon2;
            updateIcon(true);
            updateIconPosition(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    public void setIconGravity(int iconGravity2) {
        if (this.iconGravity != iconGravity2) {
            this.iconGravity = iconGravity2;
            updateIconPosition(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    public void setIconPadding(int iconPadding2) {
        if (this.iconPadding != iconPadding2) {
            this.iconPadding = iconPadding2;
            setCompoundDrawablePadding(iconPadding2);
        }
    }

    public void setIconResource(int iconResourceId) {
        Drawable drawable = null;
        if (iconResourceId != 0) {
            drawable = AppCompatResources.getDrawable(getContext(), iconResourceId);
        }
        setIcon(drawable);
    }

    public void setIconSize(int iconSize2) {
        if (iconSize2 < 0) {
            throw new IllegalArgumentException("iconSize cannot be less than 0");
        } else if (this.iconSize != iconSize2) {
            this.iconSize = iconSize2;
            updateIcon(true);
        }
    }

    public void setIconTint(ColorStateList iconTint2) {
        if (this.iconTint != iconTint2) {
            this.iconTint = iconTint2;
            updateIcon(false);
        }
    }

    public void setIconTintMode(PorterDuff.Mode iconTintMode2) {
        if (this.iconTintMode != iconTintMode2) {
            this.iconTintMode = iconTintMode2;
            updateIcon(false);
        }
    }

    public void setIconTintResource(int iconTintResourceId) {
        setIconTint(AppCompatResources.getColorStateList(getContext(), iconTintResourceId));
    }

    public void setInsetBottom(int insetBottom) {
        this.materialButtonHelper.setInsetBottom(insetBottom);
    }

    public void setInsetTop(int insetTop) {
        this.materialButtonHelper.setInsetTop(insetTop);
    }

    /* access modifiers changed from: package-private */
    public void setInternalBackground(Drawable background) {
        super.setBackgroundDrawable(background);
    }

    /* access modifiers changed from: package-private */
    public void setOnPressedChangeListenerInternal(OnPressedChangeListener listener) {
        this.onPressedChangeListenerInternal = listener;
    }

    public void setPressed(boolean pressed) {
        OnPressedChangeListener onPressedChangeListener = this.onPressedChangeListenerInternal;
        if (onPressedChangeListener != null) {
            onPressedChangeListener.onPressedChanged(this, pressed);
        }
        super.setPressed(pressed);
    }

    public void setRippleColor(ColorStateList rippleColor) {
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.setRippleColor(rippleColor);
        }
    }

    public void setRippleColorResource(int rippleColorResourceId) {
        if (isUsingOriginalBackground()) {
            setRippleColor(AppCompatResources.getColorStateList(getContext(), rippleColorResourceId));
        }
    }

    public void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.setShapeAppearanceModel(shapeAppearanceModel);
            return;
        }
        throw new IllegalStateException("Attempted to set ShapeAppearanceModel on a MaterialButton which has an overwritten background.");
    }

    /* access modifiers changed from: package-private */
    public void setShouldDrawSurfaceColorStroke(boolean shouldDrawSurfaceColorStroke) {
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.setShouldDrawSurfaceColorStroke(shouldDrawSurfaceColorStroke);
        }
    }

    public void setStrokeColor(ColorStateList strokeColor) {
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.setStrokeColor(strokeColor);
        }
    }

    public void setStrokeColorResource(int strokeColorResourceId) {
        if (isUsingOriginalBackground()) {
            setStrokeColor(AppCompatResources.getColorStateList(getContext(), strokeColorResourceId));
        }
    }

    public void setStrokeWidth(int strokeWidth) {
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.setStrokeWidth(strokeWidth);
        }
    }

    public void setStrokeWidthResource(int strokeWidthResourceId) {
        if (isUsingOriginalBackground()) {
            setStrokeWidth(getResources().getDimensionPixelSize(strokeWidthResourceId));
        }
    }

    public void setSupportBackgroundTintList(ColorStateList tint) {
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.setSupportBackgroundTintList(tint);
        } else {
            super.setSupportBackgroundTintList(tint);
        }
    }

    public void setSupportBackgroundTintMode(PorterDuff.Mode tintMode) {
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.setSupportBackgroundTintMode(tintMode);
        } else {
            super.setSupportBackgroundTintMode(tintMode);
        }
    }

    public void setTextAlignment(int textAlignment) {
        super.setTextAlignment(textAlignment);
        updateIconPosition(getMeasuredWidth(), getMeasuredHeight());
    }

    public void toggle() {
        setChecked(!this.checked);
    }
}

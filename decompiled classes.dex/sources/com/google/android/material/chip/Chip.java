package com.google.android.material.chip;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.google.android.material.R;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.internal.MaterialCheckable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.resources.TextAppearanceFontCallback;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import java.util.List;
import okhttp3.HttpUrl;

public class Chip extends AppCompatCheckBox implements ChipDrawable.Delegate, Shapeable, MaterialCheckable<Chip> {
    private static final String BUTTON_ACCESSIBILITY_CLASS_NAME = "android.widget.Button";
    private static final int[] CHECKABLE_STATE_SET = {16842911};
    private static final int CHIP_BODY_VIRTUAL_ID = 0;
    private static final int CLOSE_ICON_VIRTUAL_ID = 1;
    private static final String COMPOUND_BUTTON_ACCESSIBILITY_CLASS_NAME = "android.widget.CompoundButton";
    private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_Chip_Action;
    /* access modifiers changed from: private */
    public static final Rect EMPTY_BOUNDS = new Rect();
    private static final String GENERIC_VIEW_ACCESSIBILITY_CLASS_NAME = "android.view.View";
    private static final int MIN_TOUCH_TARGET_DP = 48;
    private static final String NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";
    private static final String RADIO_BUTTON_ACCESSIBILITY_CLASS_NAME = "android.widget.RadioButton";
    private static final int[] SELECTED_STATE = {16842913};
    private static final String TAG = "Chip";
    private CharSequence accessibilityClassName;
    /* access modifiers changed from: private */
    public ChipDrawable chipDrawable;
    /* access modifiers changed from: private */
    public boolean closeIconFocused;
    private boolean closeIconHovered;
    private boolean closeIconPressed;
    private boolean deferredCheckedValue;
    private boolean ensureMinTouchTargetSize;
    private final TextAppearanceFontCallback fontCallback;
    private InsetDrawable insetBackgroundDrawable;
    private int lastLayoutDirection;
    private int minTouchTargetSize;
    /* access modifiers changed from: private */
    public CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
    /* access modifiers changed from: private */
    public MaterialCheckable.OnCheckedChangeListener<Chip> onCheckedChangeListenerInternal;
    /* access modifiers changed from: private */
    public View.OnClickListener onCloseIconClickListener;
    private final Rect rect;
    private final RectF rectF;
    private RippleDrawable ripple;
    private final ChipTouchHelper touchHelper;
    private boolean touchHelperEnabled;

    private class ChipTouchHelper extends ExploreByTouchHelper {
        ChipTouchHelper(Chip view) {
            super(view);
        }

        /* access modifiers changed from: protected */
        public int getVirtualViewAt(float x, float y) {
            return (!Chip.this.hasCloseIcon() || !Chip.this.getCloseIconTouchBounds().contains(x, y)) ? 0 : 1;
        }

        /* access modifiers changed from: protected */
        public void getVisibleVirtualViews(List<Integer> list) {
            list.add(0);
            if (Chip.this.hasCloseIcon() && Chip.this.isCloseIconVisible() && Chip.this.onCloseIconClickListener != null) {
                list.add(1);
            }
        }

        /* access modifiers changed from: protected */
        public boolean onPerformActionForVirtualView(int virtualViewId, int action, Bundle arguments) {
            if (action != 16) {
                return false;
            }
            if (virtualViewId == 0) {
                return Chip.this.performClick();
            }
            if (virtualViewId == 1) {
                return Chip.this.performCloseIconClick();
            }
            return false;
        }

        /* access modifiers changed from: protected */
        public void onPopulateNodeForHost(AccessibilityNodeInfoCompat node) {
            node.setCheckable(Chip.this.isCheckable());
            node.setClickable(Chip.this.isClickable());
            node.setClassName(Chip.this.getAccessibilityClassName());
            CharSequence text = Chip.this.getText();
            if (Build.VERSION.SDK_INT >= 23) {
                node.setText(text);
            } else {
                node.setContentDescription(text);
            }
        }

        /* access modifiers changed from: protected */
        public void onPopulateNodeForVirtualView(int virtualViewId, AccessibilityNodeInfoCompat node) {
            CharSequence charSequence = HttpUrl.FRAGMENT_ENCODE_SET;
            if (virtualViewId == 1) {
                CharSequence closeIconContentDescription = Chip.this.getCloseIconContentDescription();
                if (closeIconContentDescription != null) {
                    node.setContentDescription(closeIconContentDescription);
                } else {
                    CharSequence text = Chip.this.getText();
                    Context context = Chip.this.getContext();
                    int i = R.string.mtrl_chip_close_icon_content_description;
                    Object[] objArr = new Object[1];
                    if (!TextUtils.isEmpty(text)) {
                        charSequence = text;
                    }
                    objArr[0] = charSequence;
                    node.setContentDescription(context.getString(i, objArr).trim());
                }
                node.setBoundsInParent(Chip.this.getCloseIconTouchBoundsInt());
                node.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
                node.setEnabled(Chip.this.isEnabled());
                return;
            }
            node.setContentDescription(charSequence);
            node.setBoundsInParent(Chip.EMPTY_BOUNDS);
        }

        /* access modifiers changed from: protected */
        public void onVirtualViewKeyboardFocusChanged(int virtualViewId, boolean hasFocus) {
            if (virtualViewId == 1) {
                boolean unused = Chip.this.closeIconFocused = hasFocus;
                Chip.this.refreshDrawableState();
            }
        }
    }

    public Chip(Context context) {
        this(context, (AttributeSet) null);
    }

    public Chip(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.chipStyle);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Chip(android.content.Context r8, android.util.AttributeSet r9, int r10) {
        /*
            r7 = this;
            int r4 = DEF_STYLE_RES
            android.content.Context r0 = com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap(r8, r9, r10, r4)
            r7.<init>(r0, r9, r10)
            android.graphics.Rect r0 = new android.graphics.Rect
            r0.<init>()
            r7.rect = r0
            android.graphics.RectF r0 = new android.graphics.RectF
            r0.<init>()
            r7.rectF = r0
            com.google.android.material.chip.Chip$1 r0 = new com.google.android.material.chip.Chip$1
            r0.<init>()
            r7.fontCallback = r0
            android.content.Context r8 = r7.getContext()
            r7.validateAttributes(r9)
            com.google.android.material.chip.ChipDrawable r6 = com.google.android.material.chip.ChipDrawable.createFromAttributes(r8, r9, r10, r4)
            r7.initMinTouchTarget(r8, r9, r10)
            r7.setChipDrawable(r6)
            float r0 = androidx.core.view.ViewCompat.getElevation(r7)
            r6.setElevation(r0)
            int[] r2 = com.google.android.material.R.styleable.Chip
            r0 = 0
            int[] r5 = new int[r0]
            r0 = r8
            r1 = r9
            r3 = r10
            android.content.res.TypedArray r0 = com.google.android.material.internal.ThemeEnforcement.obtainStyledAttributes(r0, r1, r2, r3, r4, r5)
            int r1 = android.os.Build.VERSION.SDK_INT
            r2 = 23
            if (r1 >= r2) goto L_0x0052
            int r1 = com.google.android.material.R.styleable.Chip_android_textColor
            android.content.res.ColorStateList r1 = com.google.android.material.resources.MaterialResources.getColorStateList((android.content.Context) r8, (android.content.res.TypedArray) r0, (int) r1)
            r7.setTextColor(r1)
        L_0x0052:
            int r1 = com.google.android.material.R.styleable.Chip_shapeAppearance
            boolean r1 = r0.hasValue(r1)
            r0.recycle()
            com.google.android.material.chip.Chip$ChipTouchHelper r2 = new com.google.android.material.chip.Chip$ChipTouchHelper
            r2.<init>(r7)
            r7.touchHelper = r2
            r7.updateAccessibilityDelegate()
            if (r1 != 0) goto L_0x006a
            r7.initOutlineProvider()
        L_0x006a:
            boolean r2 = r7.deferredCheckedValue
            r7.setChecked(r2)
            java.lang.CharSequence r2 = r6.getText()
            r7.setText(r2)
            android.text.TextUtils$TruncateAt r2 = r6.getEllipsize()
            r7.setEllipsize(r2)
            r7.updateTextPaintDrawState()
            com.google.android.material.chip.ChipDrawable r2 = r7.chipDrawable
            boolean r2 = r2.shouldDrawText()
            if (r2 != 0) goto L_0x008f
            r2 = 1
            r7.setLines(r2)
            r7.setHorizontallyScrolling(r2)
        L_0x008f:
            r2 = 8388627(0x800013, float:1.175497E-38)
            r7.setGravity(r2)
            r7.updatePaddingInternal()
            boolean r2 = r7.shouldEnsureMinTouchTargetSize()
            if (r2 == 0) goto L_0x00a3
            int r2 = r7.minTouchTargetSize
            r7.setMinHeight(r2)
        L_0x00a3:
            int r2 = androidx.core.view.ViewCompat.getLayoutDirection(r7)
            r7.lastLayoutDirection = r2
            com.google.android.material.chip.Chip$2 r2 = new com.google.android.material.chip.Chip$2
            r2.<init>()
            super.setOnCheckedChangeListener(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.chip.Chip.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private void applyChipDrawable(ChipDrawable chipDrawable2) {
        chipDrawable2.setDelegate(this);
    }

    private int[] createCloseIconDrawableState() {
        int i = 0;
        if (isEnabled()) {
            i = 0 + 1;
        }
        if (this.closeIconFocused) {
            i++;
        }
        if (this.closeIconHovered) {
            i++;
        }
        if (this.closeIconPressed) {
            i++;
        }
        if (isChecked()) {
            i++;
        }
        int[] iArr = new int[i];
        int i2 = 0;
        if (isEnabled()) {
            iArr[0] = 16842910;
            i2 = 0 + 1;
        }
        if (this.closeIconFocused) {
            iArr[i2] = 16842908;
            i2++;
        }
        if (this.closeIconHovered) {
            iArr[i2] = 16843623;
            i2++;
        }
        if (this.closeIconPressed) {
            iArr[i2] = 16842919;
            i2++;
        }
        if (isChecked()) {
            iArr[i2] = 16842913;
            int i3 = i2 + 1;
        }
        return iArr;
    }

    private void ensureChipDrawableHasCallback() {
        if (getBackgroundDrawable() == this.insetBackgroundDrawable && this.chipDrawable.getCallback() == null) {
            this.chipDrawable.setCallback(this.insetBackgroundDrawable);
        }
    }

    /* access modifiers changed from: private */
    public RectF getCloseIconTouchBounds() {
        this.rectF.setEmpty();
        if (hasCloseIcon() && this.onCloseIconClickListener != null) {
            this.chipDrawable.getCloseIconTouchBounds(this.rectF);
        }
        return this.rectF;
    }

    /* access modifiers changed from: private */
    public Rect getCloseIconTouchBoundsInt() {
        RectF closeIconTouchBounds = getCloseIconTouchBounds();
        this.rect.set((int) closeIconTouchBounds.left, (int) closeIconTouchBounds.top, (int) closeIconTouchBounds.right, (int) closeIconTouchBounds.bottom);
        return this.rect;
    }

    private TextAppearance getTextAppearance() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getTextAppearance();
        }
        return null;
    }

    /* access modifiers changed from: private */
    public boolean hasCloseIcon() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        return (chipDrawable2 == null || chipDrawable2.getCloseIcon() == null) ? false : true;
    }

    private void initMinTouchTarget(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attrs, R.styleable.Chip, defStyleAttr, DEF_STYLE_RES, new int[0]);
        this.ensureMinTouchTargetSize = obtainStyledAttributes.getBoolean(R.styleable.Chip_ensureMinTouchTargetSize, false);
        this.minTouchTargetSize = (int) Math.ceil((double) obtainStyledAttributes.getDimension(R.styleable.Chip_chipMinTouchTargetSize, (float) Math.ceil((double) ViewUtils.dpToPx(getContext(), 48))));
        obtainStyledAttributes.recycle();
    }

    private void initOutlineProvider() {
        if (Build.VERSION.SDK_INT >= 21) {
            setOutlineProvider(new ViewOutlineProvider() {
                public void getOutline(View view, Outline outline) {
                    if (Chip.this.chipDrawable != null) {
                        Chip.this.chipDrawable.getOutline(outline);
                    } else {
                        outline.setAlpha(0.0f);
                    }
                }
            });
        }
    }

    private void insetChipBackgroundDrawable(int insetLeft, int insetTop, int insetRight, int insetBottom) {
        this.insetBackgroundDrawable = new InsetDrawable(this.chipDrawable, insetLeft, insetTop, insetRight, insetBottom);
    }

    private void removeBackgroundInset() {
        if (this.insetBackgroundDrawable != null) {
            this.insetBackgroundDrawable = null;
            setMinWidth(0);
            setMinHeight((int) getChipMinHeight());
            updateBackgroundDrawable();
        }
    }

    private void setCloseIconHovered(boolean hovered) {
        if (this.closeIconHovered != hovered) {
            this.closeIconHovered = hovered;
            refreshDrawableState();
        }
    }

    private void setCloseIconPressed(boolean pressed) {
        if (this.closeIconPressed != pressed) {
            this.closeIconPressed = pressed;
            refreshDrawableState();
        }
    }

    private void unapplyChipDrawable(ChipDrawable chipDrawable2) {
        if (chipDrawable2 != null) {
            chipDrawable2.setDelegate((ChipDrawable.Delegate) null);
        }
    }

    private void updateAccessibilityDelegate() {
        if (!hasCloseIcon() || !isCloseIconVisible() || this.onCloseIconClickListener == null) {
            ViewCompat.setAccessibilityDelegate(this, (AccessibilityDelegateCompat) null);
            this.touchHelperEnabled = false;
            return;
        }
        ViewCompat.setAccessibilityDelegate(this, this.touchHelper);
        this.touchHelperEnabled = true;
    }

    private void updateBackgroundDrawable() {
        if (RippleUtils.USE_FRAMEWORK_RIPPLE) {
            updateFrameworkRippleBackground();
            return;
        }
        this.chipDrawable.setUseCompatRipple(true);
        ViewCompat.setBackground(this, getBackgroundDrawable());
        updatePaddingInternal();
        ensureChipDrawableHasCallback();
    }

    private void updateFrameworkRippleBackground() {
        this.ripple = new RippleDrawable(RippleUtils.sanitizeRippleDrawableColor(this.chipDrawable.getRippleColor()), getBackgroundDrawable(), (Drawable) null);
        this.chipDrawable.setUseCompatRipple(false);
        ViewCompat.setBackground(this, this.ripple);
        updatePaddingInternal();
    }

    private void updatePaddingInternal() {
        ChipDrawable chipDrawable2;
        if (!TextUtils.isEmpty(getText()) && (chipDrawable2 = this.chipDrawable) != null) {
            int chipEndPadding = (int) (chipDrawable2.getChipEndPadding() + this.chipDrawable.getTextEndPadding() + this.chipDrawable.calculateCloseIconWidth());
            int chipStartPadding = (int) (this.chipDrawable.getChipStartPadding() + this.chipDrawable.getTextStartPadding() + this.chipDrawable.calculateChipIconWidth());
            if (this.insetBackgroundDrawable != null) {
                Rect rect2 = new Rect();
                this.insetBackgroundDrawable.getPadding(rect2);
                chipStartPadding += rect2.left;
                chipEndPadding += rect2.right;
            }
            ViewCompat.setPaddingRelative(this, chipStartPadding, getPaddingTop(), chipEndPadding, getPaddingBottom());
        }
    }

    private void updateTextPaintDrawState() {
        TextPaint paint = getPaint();
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            paint.drawableState = chipDrawable2.getState();
        }
        TextAppearance textAppearance = getTextAppearance();
        if (textAppearance != null) {
            textAppearance.updateDrawState(getContext(), paint, this.fontCallback);
        }
    }

    private void validateAttributes(AttributeSet attributeSet) {
        if (attributeSet != null) {
            if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "background") != null) {
                Log.w(TAG, "Do not set the background; Chip manages its own background drawable.");
            }
            if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableLeft") != null) {
                throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
            } else if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableStart") != null) {
                throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
            } else if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableEnd") != null) {
                throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
            } else if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableRight") != null) {
                throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
            } else if (!attributeSet.getAttributeBooleanValue(NAMESPACE_ANDROID, "singleLine", true) || attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "lines", 1) != 1 || attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "minLines", 1) != 1 || attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "maxLines", 1) != 1) {
                throw new UnsupportedOperationException("Chip does not support multi-line text");
            } else if (attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "gravity", 8388627) != 8388627) {
                Log.w(TAG, "Chip text must be vertically center and start aligned");
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean dispatchHoverEvent(MotionEvent event) {
        return !this.touchHelperEnabled ? super.dispatchHoverEvent(event) : this.touchHelper.dispatchHoverEvent(event) || super.dispatchHoverEvent(event);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (!this.touchHelperEnabled) {
            return super.dispatchKeyEvent(event);
        }
        if (!this.touchHelper.dispatchKeyEvent(event) || this.touchHelper.getKeyboardFocusedVirtualViewId() == Integer.MIN_VALUE) {
            return super.dispatchKeyEvent(event);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        boolean z = false;
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null && chipDrawable2.isCloseIconStateful()) {
            z = this.chipDrawable.setCloseIconState(createCloseIconDrawableState());
        }
        if (z) {
            invalidate();
        }
    }

    public boolean ensureAccessibleTouchTarget(int minTargetPx) {
        this.minTouchTargetSize = minTargetPx;
        int i = 0;
        if (!shouldEnsureMinTouchTargetSize()) {
            if (this.insetBackgroundDrawable != null) {
                removeBackgroundInset();
            } else {
                updateBackgroundDrawable();
            }
            return false;
        }
        int max = Math.max(0, minTargetPx - this.chipDrawable.getIntrinsicHeight());
        int max2 = Math.max(0, minTargetPx - this.chipDrawable.getIntrinsicWidth());
        if (max2 > 0 || max > 0) {
            int i2 = max2 > 0 ? max2 / 2 : 0;
            if (max > 0) {
                i = max / 2;
            }
            if (this.insetBackgroundDrawable != null) {
                Rect rect2 = new Rect();
                this.insetBackgroundDrawable.getPadding(rect2);
                if (rect2.top == i && rect2.bottom == i && rect2.left == i2 && rect2.right == i2) {
                    updateBackgroundDrawable();
                    return true;
                }
            }
            if (Build.VERSION.SDK_INT >= 16) {
                if (getMinHeight() != minTargetPx) {
                    setMinHeight(minTargetPx);
                }
                if (getMinWidth() != minTargetPx) {
                    setMinWidth(minTargetPx);
                }
            } else {
                setMinHeight(minTargetPx);
                setMinWidth(minTargetPx);
            }
            insetChipBackgroundDrawable(i2, i, i2, i);
            updateBackgroundDrawable();
            return true;
        }
        if (this.insetBackgroundDrawable != null) {
            removeBackgroundInset();
        } else {
            updateBackgroundDrawable();
        }
        return false;
    }

    public CharSequence getAccessibilityClassName() {
        if (!TextUtils.isEmpty(this.accessibilityClassName)) {
            return this.accessibilityClassName;
        }
        if (!isCheckable()) {
            return isClickable() ? BUTTON_ACCESSIBILITY_CLASS_NAME : GENERIC_VIEW_ACCESSIBILITY_CLASS_NAME;
        }
        ViewParent parent = getParent();
        return (!(parent instanceof ChipGroup) || !((ChipGroup) parent).isSingleSelection()) ? COMPOUND_BUTTON_ACCESSIBILITY_CLASS_NAME : RADIO_BUTTON_ACCESSIBILITY_CLASS_NAME;
    }

    public Drawable getBackgroundDrawable() {
        InsetDrawable insetDrawable = this.insetBackgroundDrawable;
        return insetDrawable == null ? this.chipDrawable : insetDrawable;
    }

    public Drawable getCheckedIcon() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getCheckedIcon();
        }
        return null;
    }

    public ColorStateList getCheckedIconTint() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getCheckedIconTint();
        }
        return null;
    }

    public ColorStateList getChipBackgroundColor() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getChipBackgroundColor();
        }
        return null;
    }

    public float getChipCornerRadius() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return Math.max(0.0f, chipDrawable2.getChipCornerRadius());
        }
        return 0.0f;
    }

    public Drawable getChipDrawable() {
        return this.chipDrawable;
    }

    public float getChipEndPadding() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getChipEndPadding();
        }
        return 0.0f;
    }

    public Drawable getChipIcon() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getChipIcon();
        }
        return null;
    }

    public float getChipIconSize() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getChipIconSize();
        }
        return 0.0f;
    }

    public ColorStateList getChipIconTint() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getChipIconTint();
        }
        return null;
    }

    public float getChipMinHeight() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getChipMinHeight();
        }
        return 0.0f;
    }

    public float getChipStartPadding() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getChipStartPadding();
        }
        return 0.0f;
    }

    public ColorStateList getChipStrokeColor() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getChipStrokeColor();
        }
        return null;
    }

    public float getChipStrokeWidth() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getChipStrokeWidth();
        }
        return 0.0f;
    }

    @Deprecated
    public CharSequence getChipText() {
        return getText();
    }

    public Drawable getCloseIcon() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getCloseIcon();
        }
        return null;
    }

    public CharSequence getCloseIconContentDescription() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getCloseIconContentDescription();
        }
        return null;
    }

    public float getCloseIconEndPadding() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getCloseIconEndPadding();
        }
        return 0.0f;
    }

    public float getCloseIconSize() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getCloseIconSize();
        }
        return 0.0f;
    }

    public float getCloseIconStartPadding() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getCloseIconStartPadding();
        }
        return 0.0f;
    }

    public ColorStateList getCloseIconTint() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getCloseIconTint();
        }
        return null;
    }

    public TextUtils.TruncateAt getEllipsize() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getEllipsize();
        }
        return null;
    }

    public void getFocusedRect(Rect r) {
        if (!this.touchHelperEnabled || !(this.touchHelper.getKeyboardFocusedVirtualViewId() == 1 || this.touchHelper.getAccessibilityFocusedVirtualViewId() == 1)) {
            super.getFocusedRect(r);
        } else {
            r.set(getCloseIconTouchBoundsInt());
        }
    }

    public MotionSpec getHideMotionSpec() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getHideMotionSpec();
        }
        return null;
    }

    public float getIconEndPadding() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getIconEndPadding();
        }
        return 0.0f;
    }

    public float getIconStartPadding() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getIconStartPadding();
        }
        return 0.0f;
    }

    public ColorStateList getRippleColor() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getRippleColor();
        }
        return null;
    }

    public ShapeAppearanceModel getShapeAppearanceModel() {
        return this.chipDrawable.getShapeAppearanceModel();
    }

    public MotionSpec getShowMotionSpec() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getShowMotionSpec();
        }
        return null;
    }

    public float getTextEndPadding() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getTextEndPadding();
        }
        return 0.0f;
    }

    public float getTextStartPadding() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            return chipDrawable2.getTextStartPadding();
        }
        return 0.0f;
    }

    public boolean isCheckable() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        return chipDrawable2 != null && chipDrawable2.isCheckable();
    }

    @Deprecated
    public boolean isCheckedIconEnabled() {
        return isCheckedIconVisible();
    }

    public boolean isCheckedIconVisible() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        return chipDrawable2 != null && chipDrawable2.isCheckedIconVisible();
    }

    @Deprecated
    public boolean isChipIconEnabled() {
        return isChipIconVisible();
    }

    public boolean isChipIconVisible() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        return chipDrawable2 != null && chipDrawable2.isChipIconVisible();
    }

    @Deprecated
    public boolean isCloseIconEnabled() {
        return isCloseIconVisible();
    }

    public boolean isCloseIconVisible() {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        return chipDrawable2 != null && chipDrawable2.isCloseIconVisible();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.setParentAbsoluteElevation(this, this.chipDrawable);
    }

    public void onChipDrawableSizeChange() {
        ensureAccessibleTouchTarget(this.minTouchTargetSize);
        requestLayout();
        if (Build.VERSION.SDK_INT >= 21) {
            invalidateOutline();
        }
    }

    /* access modifiers changed from: protected */
    public int[] onCreateDrawableState(int extraSpace) {
        int[] onCreateDrawableState = super.onCreateDrawableState(extraSpace + 2);
        if (isChecked()) {
            mergeDrawableStates(onCreateDrawableState, SELECTED_STATE);
        }
        if (isCheckable()) {
            mergeDrawableStates(onCreateDrawableState, CHECKABLE_STATE_SET);
        }
        return onCreateDrawableState;
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (this.touchHelperEnabled) {
            this.touchHelper.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    public boolean onHoverEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case 7:
                setCloseIconHovered(getCloseIconTouchBounds().contains(event.getX(), event.getY()));
                break;
            case 10:
                setCloseIconHovered(false);
                break;
        }
        return super.onHoverEvent(event);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(getAccessibilityClassName());
        info.setCheckable(isCheckable());
        info.setClickable(isClickable());
        if (getParent() instanceof ChipGroup) {
            ChipGroup chipGroup = (ChipGroup) getParent();
            AccessibilityNodeInfoCompat.wrap(info).setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(chipGroup.getRowIndex(this), 1, chipGroup.isSingleLine() ? chipGroup.getIndexOfChip(this) : -1, 1, false, isChecked()));
        }
    }

    public PointerIcon onResolvePointerIcon(MotionEvent event, int pointerIndex) {
        if (!getCloseIconTouchBounds().contains(event.getX(), event.getY()) || !isEnabled()) {
            return null;
        }
        return PointerIcon.getSystemIcon(getContext(), PointerIconCompat.TYPE_HAND);
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        if (this.lastLayoutDirection != layoutDirection) {
            this.lastLayoutDirection = layoutDirection;
            updatePaddingInternal();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean z = false;
        int actionMasked = event.getActionMasked();
        boolean contains = getCloseIconTouchBounds().contains(event.getX(), event.getY());
        switch (actionMasked) {
            case 0:
                if (contains) {
                    setCloseIconPressed(true);
                    z = true;
                    break;
                }
                break;
            case 1:
                if (this.closeIconPressed) {
                    performCloseIconClick();
                    z = true;
                    break;
                }
                break;
            case 2:
                if (this.closeIconPressed) {
                    if (!contains) {
                        setCloseIconPressed(false);
                    }
                    z = true;
                    break;
                }
                break;
            case 3:
                break;
        }
        setCloseIconPressed(false);
        return z || super.onTouchEvent(event);
    }

    public boolean performCloseIconClick() {
        boolean z;
        playSoundEffect(0);
        View.OnClickListener onClickListener = this.onCloseIconClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this);
            z = true;
        } else {
            z = false;
        }
        if (this.touchHelperEnabled) {
            this.touchHelper.sendEventForVirtualView(1, 1);
        }
        return z;
    }

    public void setAccessibilityClassName(CharSequence className) {
        this.accessibilityClassName = className;
    }

    public void setBackground(Drawable background) {
        if (background == getBackgroundDrawable() || background == this.ripple) {
            super.setBackground(background);
        } else {
            Log.w(TAG, "Do not set the background; Chip manages its own background drawable.");
        }
    }

    public void setBackgroundColor(int color) {
        Log.w(TAG, "Do not set the background color; Chip manages its own background drawable.");
    }

    public void setBackgroundDrawable(Drawable background) {
        if (background == getBackgroundDrawable() || background == this.ripple) {
            super.setBackgroundDrawable(background);
        } else {
            Log.w(TAG, "Do not set the background drawable; Chip manages its own background drawable.");
        }
    }

    public void setBackgroundResource(int resid) {
        Log.w(TAG, "Do not set the background resource; Chip manages its own background drawable.");
    }

    public void setBackgroundTintList(ColorStateList tint) {
        Log.w(TAG, "Do not set the background tint list; Chip manages its own background drawable.");
    }

    public void setBackgroundTintMode(PorterDuff.Mode tintMode) {
        Log.w(TAG, "Do not set the background tint mode; Chip manages its own background drawable.");
    }

    public void setCheckable(boolean checkable) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCheckable(checkable);
        }
    }

    public void setCheckableResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCheckableResource(id);
        }
    }

    public void setChecked(boolean checked) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 == null) {
            this.deferredCheckedValue = checked;
        } else if (chipDrawable2.isCheckable()) {
            super.setChecked(checked);
        }
    }

    public void setCheckedIcon(Drawable checkedIcon) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCheckedIcon(checkedIcon);
        }
    }

    @Deprecated
    public void setCheckedIconEnabled(boolean checkedIconEnabled) {
        setCheckedIconVisible(checkedIconEnabled);
    }

    @Deprecated
    public void setCheckedIconEnabledResource(int id) {
        setCheckedIconVisible(id);
    }

    public void setCheckedIconResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCheckedIconResource(id);
        }
    }

    public void setCheckedIconTint(ColorStateList checkedIconTint) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCheckedIconTint(checkedIconTint);
        }
    }

    public void setCheckedIconTintResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCheckedIconTintResource(id);
        }
    }

    public void setCheckedIconVisible(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCheckedIconVisible(id);
        }
    }

    public void setCheckedIconVisible(boolean checkedIconVisible) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCheckedIconVisible(checkedIconVisible);
        }
    }

    public void setChipBackgroundColor(ColorStateList chipBackgroundColor) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipBackgroundColor(chipBackgroundColor);
        }
    }

    public void setChipBackgroundColorResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipBackgroundColorResource(id);
        }
    }

    @Deprecated
    public void setChipCornerRadius(float chipCornerRadius) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipCornerRadius(chipCornerRadius);
        }
    }

    @Deprecated
    public void setChipCornerRadiusResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipCornerRadiusResource(id);
        }
    }

    public void setChipDrawable(ChipDrawable drawable) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != drawable) {
            unapplyChipDrawable(chipDrawable2);
            this.chipDrawable = drawable;
            drawable.setShouldDrawText(false);
            applyChipDrawable(this.chipDrawable);
            ensureAccessibleTouchTarget(this.minTouchTargetSize);
        }
    }

    public void setChipEndPadding(float chipEndPadding) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipEndPadding(chipEndPadding);
        }
    }

    public void setChipEndPaddingResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipEndPaddingResource(id);
        }
    }

    public void setChipIcon(Drawable chipIcon) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipIcon(chipIcon);
        }
    }

    @Deprecated
    public void setChipIconEnabled(boolean chipIconEnabled) {
        setChipIconVisible(chipIconEnabled);
    }

    @Deprecated
    public void setChipIconEnabledResource(int id) {
        setChipIconVisible(id);
    }

    public void setChipIconResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipIconResource(id);
        }
    }

    public void setChipIconSize(float chipIconSize) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipIconSize(chipIconSize);
        }
    }

    public void setChipIconSizeResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipIconSizeResource(id);
        }
    }

    public void setChipIconTint(ColorStateList chipIconTint) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipIconTint(chipIconTint);
        }
    }

    public void setChipIconTintResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipIconTintResource(id);
        }
    }

    public void setChipIconVisible(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipIconVisible(id);
        }
    }

    public void setChipIconVisible(boolean chipIconVisible) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipIconVisible(chipIconVisible);
        }
    }

    public void setChipMinHeight(float minHeight) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipMinHeight(minHeight);
        }
    }

    public void setChipMinHeightResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipMinHeightResource(id);
        }
    }

    public void setChipStartPadding(float chipStartPadding) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipStartPadding(chipStartPadding);
        }
    }

    public void setChipStartPaddingResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipStartPaddingResource(id);
        }
    }

    public void setChipStrokeColor(ColorStateList chipStrokeColor) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipStrokeColor(chipStrokeColor);
        }
    }

    public void setChipStrokeColorResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipStrokeColorResource(id);
        }
    }

    public void setChipStrokeWidth(float chipStrokeWidth) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipStrokeWidth(chipStrokeWidth);
        }
    }

    public void setChipStrokeWidthResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setChipStrokeWidthResource(id);
        }
    }

    @Deprecated
    public void setChipText(CharSequence chipText) {
        setText(chipText);
    }

    @Deprecated
    public void setChipTextResource(int id) {
        setText(getResources().getString(id));
    }

    public void setCloseIcon(Drawable closeIcon) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCloseIcon(closeIcon);
        }
        updateAccessibilityDelegate();
    }

    public void setCloseIconContentDescription(CharSequence closeIconContentDescription) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCloseIconContentDescription(closeIconContentDescription);
        }
    }

    @Deprecated
    public void setCloseIconEnabled(boolean closeIconEnabled) {
        setCloseIconVisible(closeIconEnabled);
    }

    @Deprecated
    public void setCloseIconEnabledResource(int id) {
        setCloseIconVisible(id);
    }

    public void setCloseIconEndPadding(float closeIconEndPadding) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCloseIconEndPadding(closeIconEndPadding);
        }
    }

    public void setCloseIconEndPaddingResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCloseIconEndPaddingResource(id);
        }
    }

    public void setCloseIconResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCloseIconResource(id);
        }
        updateAccessibilityDelegate();
    }

    public void setCloseIconSize(float closeIconSize) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCloseIconSize(closeIconSize);
        }
    }

    public void setCloseIconSizeResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCloseIconSizeResource(id);
        }
    }

    public void setCloseIconStartPadding(float closeIconStartPadding) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCloseIconStartPadding(closeIconStartPadding);
        }
    }

    public void setCloseIconStartPaddingResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCloseIconStartPaddingResource(id);
        }
    }

    public void setCloseIconTint(ColorStateList closeIconTint) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCloseIconTint(closeIconTint);
        }
    }

    public void setCloseIconTintResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCloseIconTintResource(id);
        }
    }

    public void setCloseIconVisible(int id) {
        setCloseIconVisible(getResources().getBoolean(id));
    }

    public void setCloseIconVisible(boolean closeIconVisible) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setCloseIconVisible(closeIconVisible);
        }
        updateAccessibilityDelegate();
    }

    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (left != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (right == null) {
            super.setCompoundDrawables(left, top, right, bottom);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    public void setCompoundDrawablesRelative(Drawable start, Drawable top, Drawable end, Drawable bottom) {
        if (start != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (end == null) {
            super.setCompoundDrawablesRelative(start, top, end, bottom);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int start, int top, int end, int bottom) {
        if (start != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (end == 0) {
            super.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable start, Drawable top, Drawable end, Drawable bottom) {
        if (start != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (end == null) {
            super.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    public void setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom) {
        if (left != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (right == 0) {
            super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (left != null) {
            throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
        } else if (right == null) {
            super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        } else {
            throw new UnsupportedOperationException("Please set right drawable using R.attr#closeIcon.");
        }
    }

    public void setElevation(float elevation) {
        super.setElevation(elevation);
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setElevation(elevation);
        }
    }

    public void setEllipsize(TextUtils.TruncateAt where) {
        if (this.chipDrawable != null) {
            if (where != TextUtils.TruncateAt.MARQUEE) {
                super.setEllipsize(where);
                ChipDrawable chipDrawable2 = this.chipDrawable;
                if (chipDrawable2 != null) {
                    chipDrawable2.setEllipsize(where);
                    return;
                }
                return;
            }
            throw new UnsupportedOperationException("Text within a chip are not allowed to scroll.");
        }
    }

    public void setEnsureMinTouchTargetSize(boolean flag) {
        this.ensureMinTouchTargetSize = flag;
        ensureAccessibleTouchTarget(this.minTouchTargetSize);
    }

    public void setGravity(int gravity) {
        if (gravity != 8388627) {
            Log.w(TAG, "Chip text must be vertically center and start aligned");
        } else {
            super.setGravity(gravity);
        }
    }

    public void setHideMotionSpec(MotionSpec hideMotionSpec) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setHideMotionSpec(hideMotionSpec);
        }
    }

    public void setHideMotionSpecResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setHideMotionSpecResource(id);
        }
    }

    public void setIconEndPadding(float iconEndPadding) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setIconEndPadding(iconEndPadding);
        }
    }

    public void setIconEndPaddingResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setIconEndPaddingResource(id);
        }
    }

    public void setIconStartPadding(float iconStartPadding) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setIconStartPadding(iconStartPadding);
        }
    }

    public void setIconStartPaddingResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setIconStartPaddingResource(id);
        }
    }

    public void setInternalOnCheckedChangeListener(MaterialCheckable.OnCheckedChangeListener<Chip> onCheckedChangeListener2) {
        this.onCheckedChangeListenerInternal = onCheckedChangeListener2;
    }

    public void setLayoutDirection(int layoutDirection) {
        if (this.chipDrawable != null && Build.VERSION.SDK_INT >= 17) {
            super.setLayoutDirection(layoutDirection);
        }
    }

    public void setLines(int lines) {
        if (lines <= 1) {
            super.setLines(lines);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    public void setMaxLines(int maxLines) {
        if (maxLines <= 1) {
            super.setMaxLines(maxLines);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    public void setMaxWidth(int maxWidth) {
        super.setMaxWidth(maxWidth);
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setMaxWidth(maxWidth);
        }
    }

    public void setMinLines(int minLines) {
        if (minLines <= 1) {
            super.setMinLines(minLines);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    public void setOnCloseIconClickListener(View.OnClickListener listener) {
        this.onCloseIconClickListener = listener;
        updateAccessibilityDelegate();
    }

    public void setRippleColor(ColorStateList rippleColor) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setRippleColor(rippleColor);
        }
        if (!this.chipDrawable.getUseCompatRipple()) {
            updateFrameworkRippleBackground();
        }
    }

    public void setRippleColorResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setRippleColorResource(id);
            if (!this.chipDrawable.getUseCompatRipple()) {
                updateFrameworkRippleBackground();
            }
        }
    }

    public void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        this.chipDrawable.setShapeAppearanceModel(shapeAppearanceModel);
    }

    public void setShowMotionSpec(MotionSpec showMotionSpec) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setShowMotionSpec(showMotionSpec);
        }
    }

    public void setShowMotionSpecResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setShowMotionSpecResource(id);
        }
    }

    public void setSingleLine(boolean singleLine) {
        if (singleLine) {
            super.setSingleLine(singleLine);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    public void setText(CharSequence text, TextView.BufferType type) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            if (text == null) {
                text = HttpUrl.FRAGMENT_ENCODE_SET;
            }
            super.setText(chipDrawable2.shouldDrawText() ? null : text, type);
            ChipDrawable chipDrawable3 = this.chipDrawable;
            if (chipDrawable3 != null) {
                chipDrawable3.setText(text);
            }
        }
    }

    public void setTextAppearance(int resId) {
        super.setTextAppearance(resId);
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setTextAppearanceResource(resId);
        }
        updateTextPaintDrawState();
    }

    public void setTextAppearance(Context context, int resId) {
        super.setTextAppearance(context, resId);
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setTextAppearanceResource(resId);
        }
        updateTextPaintDrawState();
    }

    public void setTextAppearance(TextAppearance textAppearance) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setTextAppearance(textAppearance);
        }
        updateTextPaintDrawState();
    }

    public void setTextAppearanceResource(int id) {
        setTextAppearance(getContext(), id);
    }

    public void setTextEndPadding(float textEndPadding) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setTextEndPadding(textEndPadding);
        }
    }

    public void setTextEndPaddingResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setTextEndPaddingResource(id);
        }
    }

    public void setTextStartPadding(float textStartPadding) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setTextStartPadding(textStartPadding);
        }
    }

    public void setTextStartPaddingResource(int id) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != null) {
            chipDrawable2.setTextStartPaddingResource(id);
        }
    }

    public boolean shouldEnsureMinTouchTargetSize() {
        return this.ensureMinTouchTargetSize;
    }
}

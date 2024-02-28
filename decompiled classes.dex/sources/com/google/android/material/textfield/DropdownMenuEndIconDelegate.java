package com.google.android.material.textfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityManagerCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.textfield.TextInputLayout;

class DropdownMenuEndIconDelegate extends EndIconDelegate {
    private static final int ANIMATION_FADE_IN_DURATION = 67;
    private static final int ANIMATION_FADE_OUT_DURATION = 50;
    /* access modifiers changed from: private */
    public static final boolean IS_LOLLIPOP = (Build.VERSION.SDK_INT >= 21);
    /* access modifiers changed from: private */
    public final TextInputLayout.AccessibilityDelegate accessibilityDelegate = new TextInputLayout.AccessibilityDelegate(this.textInputLayout) {
        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            if (!DropdownMenuEndIconDelegate.isEditable(DropdownMenuEndIconDelegate.this.textInputLayout.getEditText())) {
                info.setClassName(Spinner.class.getName());
            }
            if (info.isShowingHintText()) {
                info.setHintText((CharSequence) null);
            }
        }

        public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onPopulateAccessibilityEvent(host, event);
            AutoCompleteTextView access$000 = DropdownMenuEndIconDelegate.castAutoCompleteTextViewOrThrow(DropdownMenuEndIconDelegate.this.textInputLayout.getEditText());
            if (event.getEventType() == 1 && DropdownMenuEndIconDelegate.this.accessibilityManager.isEnabled() && !DropdownMenuEndIconDelegate.isEditable(DropdownMenuEndIconDelegate.this.textInputLayout.getEditText())) {
                DropdownMenuEndIconDelegate.this.showHideDropdown(access$000);
                DropdownMenuEndIconDelegate.this.updateDropdownPopupDirty();
            }
        }
    };
    /* access modifiers changed from: private */
    public AccessibilityManager accessibilityManager;
    private final TextInputLayout.OnEditTextAttachedListener dropdownMenuOnEditTextAttachedListener = new TextInputLayout.OnEditTextAttachedListener() {
        public void onEditTextAttached(TextInputLayout textInputLayout) {
            AutoCompleteTextView access$000 = DropdownMenuEndIconDelegate.castAutoCompleteTextViewOrThrow(textInputLayout.getEditText());
            DropdownMenuEndIconDelegate.this.setPopupBackground(access$000);
            DropdownMenuEndIconDelegate.this.addRippleEffect(access$000);
            DropdownMenuEndIconDelegate.this.setUpDropdownShowHideBehavior(access$000);
            access$000.setThreshold(0);
            access$000.removeTextChangedListener(DropdownMenuEndIconDelegate.this.exposedDropdownEndIconTextWatcher);
            access$000.addTextChangedListener(DropdownMenuEndIconDelegate.this.exposedDropdownEndIconTextWatcher);
            textInputLayout.setEndIconCheckable(true);
            textInputLayout.setErrorIconDrawable((Drawable) null);
            if (!DropdownMenuEndIconDelegate.isEditable(access$000) && DropdownMenuEndIconDelegate.this.accessibilityManager.isTouchExplorationEnabled()) {
                ViewCompat.setImportantForAccessibility(DropdownMenuEndIconDelegate.this.endIconView, 2);
            }
            textInputLayout.setTextInputAccessibilityDelegate(DropdownMenuEndIconDelegate.this.accessibilityDelegate);
            textInputLayout.setEndIconVisible(true);
        }
    };
    private long dropdownPopupActivatedAt = Long.MAX_VALUE;
    /* access modifiers changed from: private */
    public boolean dropdownPopupDirty = false;
    private final TextInputLayout.OnEndIconChangedListener endIconChangedListener = new TextInputLayout.OnEndIconChangedListener() {
        public void onEndIconChanged(TextInputLayout textInputLayout, int previousIcon) {
            final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayout.getEditText();
            if (autoCompleteTextView != null && previousIcon == 3) {
                autoCompleteTextView.post(new Runnable() {
                    public void run() {
                        autoCompleteTextView.removeTextChangedListener(DropdownMenuEndIconDelegate.this.exposedDropdownEndIconTextWatcher);
                    }
                });
                if (autoCompleteTextView.getOnFocusChangeListener() == DropdownMenuEndIconDelegate.this.onFocusChangeListener) {
                    autoCompleteTextView.setOnFocusChangeListener((View.OnFocusChangeListener) null);
                }
                autoCompleteTextView.setOnTouchListener((View.OnTouchListener) null);
                if (DropdownMenuEndIconDelegate.IS_LOLLIPOP) {
                    autoCompleteTextView.setOnDismissListener((AutoCompleteTextView.OnDismissListener) null);
                }
            }
            if (previousIcon == 3) {
                textInputLayout.removeOnAttachStateChangeListener(DropdownMenuEndIconDelegate.this.onAttachStateChangeListener);
                DropdownMenuEndIconDelegate.this.removeTouchExplorationStateChangeListenerIfNeeded();
            }
        }
    };
    /* access modifiers changed from: private */
    public final TextWatcher exposedDropdownEndIconTextWatcher = new TextWatcherAdapter() {
        public void afterTextChanged(Editable s) {
            final AutoCompleteTextView access$000 = DropdownMenuEndIconDelegate.castAutoCompleteTextViewOrThrow(DropdownMenuEndIconDelegate.this.textInputLayout.getEditText());
            if (DropdownMenuEndIconDelegate.this.accessibilityManager.isTouchExplorationEnabled() && DropdownMenuEndIconDelegate.isEditable(access$000) && !DropdownMenuEndIconDelegate.this.endIconView.hasFocus()) {
                access$000.dismissDropDown();
            }
            access$000.post(new Runnable() {
                public void run() {
                    boolean isPopupShowing = access$000.isPopupShowing();
                    DropdownMenuEndIconDelegate.this.setEndIconChecked(isPopupShowing);
                    boolean unused = DropdownMenuEndIconDelegate.this.dropdownPopupDirty = isPopupShowing;
                }
            });
        }
    };
    /* access modifiers changed from: private */
    public ValueAnimator fadeInAnim;
    private ValueAnimator fadeOutAnim;
    private StateListDrawable filledPopupBackground;
    /* access modifiers changed from: private */
    public boolean isEndIconChecked = false;
    /* access modifiers changed from: private */
    public final View.OnAttachStateChangeListener onAttachStateChangeListener = new View.OnAttachStateChangeListener() {
        public void onViewAttachedToWindow(View ignored) {
            DropdownMenuEndIconDelegate.this.addTouchExplorationStateChangeListenerIfNeeded();
        }

        public void onViewDetachedFromWindow(View ignored) {
            DropdownMenuEndIconDelegate.this.removeTouchExplorationStateChangeListenerIfNeeded();
        }
    };
    /* access modifiers changed from: private */
    public final View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            DropdownMenuEndIconDelegate.this.textInputLayout.setEndIconActivated(hasFocus);
            if (!hasFocus) {
                DropdownMenuEndIconDelegate.this.setEndIconChecked(false);
                boolean unused = DropdownMenuEndIconDelegate.this.dropdownPopupDirty = false;
            }
        }
    };
    private MaterialShapeDrawable outlinedPopupBackground;
    private final AccessibilityManagerCompat.TouchExplorationStateChangeListener touchExplorationStateChangeListener = new AccessibilityManagerCompat.TouchExplorationStateChangeListener() {
        public void onTouchExplorationStateChanged(boolean enabled) {
            AutoCompleteTextView autoCompleteTextView;
            if (DropdownMenuEndIconDelegate.this.textInputLayout != null && (autoCompleteTextView = (AutoCompleteTextView) DropdownMenuEndIconDelegate.this.textInputLayout.getEditText()) != null && !DropdownMenuEndIconDelegate.isEditable(autoCompleteTextView)) {
                ViewCompat.setImportantForAccessibility(DropdownMenuEndIconDelegate.this.endIconView, enabled ? 2 : 1);
            }
        }
    };

    DropdownMenuEndIconDelegate(TextInputLayout textInputLayout, int customEndIcon) {
        super(textInputLayout, customEndIcon);
    }

    /* access modifiers changed from: private */
    public void addRippleEffect(AutoCompleteTextView editText) {
        if (!isEditable(editText)) {
            int boxBackgroundMode = this.textInputLayout.getBoxBackgroundMode();
            MaterialShapeDrawable boxBackground = this.textInputLayout.getBoxBackground();
            int color = MaterialColors.getColor(editText, R.attr.colorControlHighlight);
            int[][] iArr = {new int[]{16842919}, new int[0]};
            if (boxBackgroundMode == 2) {
                addRippleEffectOnOutlinedLayout(editText, color, iArr, boxBackground);
            } else if (boxBackgroundMode == 1) {
                addRippleEffectOnFilledLayout(editText, color, iArr, boxBackground);
            }
        }
    }

    private void addRippleEffectOnFilledLayout(AutoCompleteTextView editText, int rippleColor, int[][] states, MaterialShapeDrawable boxBackground) {
        int boxBackgroundColor = this.textInputLayout.getBoxBackgroundColor();
        int[] iArr = {MaterialColors.layer(rippleColor, boxBackgroundColor, 0.1f), boxBackgroundColor};
        if (IS_LOLLIPOP) {
            ViewCompat.setBackground(editText, new RippleDrawable(new ColorStateList(states, iArr), boxBackground, boxBackground));
            return;
        }
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(boxBackground.getShapeAppearanceModel());
        materialShapeDrawable.setFillColor(new ColorStateList(states, iArr));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{boxBackground, materialShapeDrawable});
        int paddingStart = ViewCompat.getPaddingStart(editText);
        int paddingTop = editText.getPaddingTop();
        int paddingEnd = ViewCompat.getPaddingEnd(editText);
        int paddingBottom = editText.getPaddingBottom();
        ViewCompat.setBackground(editText, layerDrawable);
        ViewCompat.setPaddingRelative(editText, paddingStart, paddingTop, paddingEnd, paddingBottom);
    }

    private void addRippleEffectOnOutlinedLayout(AutoCompleteTextView editText, int rippleColor, int[][] states, MaterialShapeDrawable boxBackground) {
        LayerDrawable layerDrawable;
        int color = MaterialColors.getColor(editText, R.attr.colorSurface);
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(boxBackground.getShapeAppearanceModel());
        int layer = MaterialColors.layer(rippleColor, color, 0.1f);
        materialShapeDrawable.setFillColor(new ColorStateList(states, new int[]{layer, 0}));
        if (IS_LOLLIPOP) {
            materialShapeDrawable.setTint(color);
            ColorStateList colorStateList = new ColorStateList(states, new int[]{layer, color});
            MaterialShapeDrawable materialShapeDrawable2 = new MaterialShapeDrawable(boxBackground.getShapeAppearanceModel());
            materialShapeDrawable2.setTint(-1);
            layerDrawable = new LayerDrawable(new Drawable[]{new RippleDrawable(colorStateList, materialShapeDrawable, materialShapeDrawable2), boxBackground});
        } else {
            layerDrawable = new LayerDrawable(new Drawable[]{materialShapeDrawable, boxBackground});
        }
        ViewCompat.setBackground(editText, layerDrawable);
    }

    /* access modifiers changed from: private */
    public void addTouchExplorationStateChangeListenerIfNeeded() {
        if (this.accessibilityManager != null && this.textInputLayout != null && ViewCompat.isAttachedToWindow(this.textInputLayout)) {
            AccessibilityManagerCompat.addTouchExplorationStateChangeListener(this.accessibilityManager, this.touchExplorationStateChangeListener);
        }
    }

    /* access modifiers changed from: private */
    public static AutoCompleteTextView castAutoCompleteTextViewOrThrow(EditText editText) {
        if (editText instanceof AutoCompleteTextView) {
            return (AutoCompleteTextView) editText;
        }
        throw new RuntimeException("EditText needs to be an AutoCompleteTextView if an Exposed Dropdown Menu is being used.");
    }

    private ValueAnimator getAlphaAnimator(int duration, float... values) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(values);
        ofFloat.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        ofFloat.setDuration((long) duration);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                DropdownMenuEndIconDelegate.this.endIconView.setAlpha(((Float) animation.getAnimatedValue()).floatValue());
            }
        });
        return ofFloat;
    }

    private MaterialShapeDrawable getPopUpMaterialShapeDrawable(float topCornerRadius, float bottomCornerRadius, float elevation, int verticalPadding) {
        ShapeAppearanceModel build = ShapeAppearanceModel.builder().setTopLeftCornerSize(topCornerRadius).setTopRightCornerSize(topCornerRadius).setBottomLeftCornerSize(bottomCornerRadius).setBottomRightCornerSize(bottomCornerRadius).build();
        MaterialShapeDrawable createWithElevationOverlay = MaterialShapeDrawable.createWithElevationOverlay(this.context, elevation);
        createWithElevationOverlay.setShapeAppearanceModel(build);
        createWithElevationOverlay.setPadding(0, verticalPadding, 0, verticalPadding);
        return createWithElevationOverlay;
    }

    private void initAnimators() {
        this.fadeInAnim = getAlphaAnimator(67, 0.0f, 1.0f);
        ValueAnimator alphaAnimator = getAlphaAnimator(50, 1.0f, 0.0f);
        this.fadeOutAnim = alphaAnimator;
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                DropdownMenuEndIconDelegate.this.endIconView.setChecked(DropdownMenuEndIconDelegate.this.isEndIconChecked);
                DropdownMenuEndIconDelegate.this.fadeInAnim.start();
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean isDropdownPopupActive() {
        long currentTimeMillis = System.currentTimeMillis() - this.dropdownPopupActivatedAt;
        return currentTimeMillis < 0 || currentTimeMillis > 300;
    }

    /* access modifiers changed from: private */
    public static boolean isEditable(EditText editText) {
        return editText.getKeyListener() != null;
    }

    /* access modifiers changed from: private */
    public void removeTouchExplorationStateChangeListenerIfNeeded() {
        AccessibilityManager accessibilityManager2 = this.accessibilityManager;
        if (accessibilityManager2 != null) {
            AccessibilityManagerCompat.removeTouchExplorationStateChangeListener(accessibilityManager2, this.touchExplorationStateChangeListener);
        }
    }

    /* access modifiers changed from: private */
    public void setEndIconChecked(boolean checked) {
        if (this.isEndIconChecked != checked) {
            this.isEndIconChecked = checked;
            this.fadeInAnim.cancel();
            this.fadeOutAnim.start();
        }
    }

    /* access modifiers changed from: private */
    public void setPopupBackground(AutoCompleteTextView editText) {
        if (IS_LOLLIPOP) {
            int boxBackgroundMode = this.textInputLayout.getBoxBackgroundMode();
            if (boxBackgroundMode == 2) {
                editText.setDropDownBackgroundDrawable(this.outlinedPopupBackground);
            } else if (boxBackgroundMode == 1) {
                editText.setDropDownBackgroundDrawable(this.filledPopupBackground);
            }
        }
    }

    /* access modifiers changed from: private */
    public void setUpDropdownShowHideBehavior(final AutoCompleteTextView editText) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 1) {
                    if (DropdownMenuEndIconDelegate.this.isDropdownPopupActive()) {
                        boolean unused = DropdownMenuEndIconDelegate.this.dropdownPopupDirty = false;
                    }
                    DropdownMenuEndIconDelegate.this.showHideDropdown(editText);
                    DropdownMenuEndIconDelegate.this.updateDropdownPopupDirty();
                }
                return false;
            }
        });
        editText.setOnFocusChangeListener(this.onFocusChangeListener);
        if (IS_LOLLIPOP) {
            editText.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
                public void onDismiss() {
                    DropdownMenuEndIconDelegate.this.updateDropdownPopupDirty();
                    DropdownMenuEndIconDelegate.this.setEndIconChecked(false);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void showHideDropdown(AutoCompleteTextView editText) {
        if (editText != null) {
            if (isDropdownPopupActive()) {
                this.dropdownPopupDirty = false;
            }
            if (!this.dropdownPopupDirty) {
                if (IS_LOLLIPOP) {
                    setEndIconChecked(!this.isEndIconChecked);
                } else {
                    this.isEndIconChecked = !this.isEndIconChecked;
                    this.endIconView.toggle();
                }
                if (this.isEndIconChecked) {
                    editText.requestFocus();
                    editText.showDropDown();
                    return;
                }
                editText.dismissDropDown();
                return;
            }
            this.dropdownPopupDirty = false;
        }
    }

    /* access modifiers changed from: private */
    public void updateDropdownPopupDirty() {
        this.dropdownPopupDirty = true;
        this.dropdownPopupActivatedAt = System.currentTimeMillis();
    }

    /* access modifiers changed from: package-private */
    public void initialize() {
        float dimensionPixelOffset = (float) this.context.getResources().getDimensionPixelOffset(R.dimen.mtrl_shape_corner_size_small_component);
        float dimensionPixelOffset2 = (float) this.context.getResources().getDimensionPixelOffset(R.dimen.mtrl_exposed_dropdown_menu_popup_elevation);
        int dimensionPixelOffset3 = this.context.getResources().getDimensionPixelOffset(R.dimen.mtrl_exposed_dropdown_menu_popup_vertical_padding);
        MaterialShapeDrawable popUpMaterialShapeDrawable = getPopUpMaterialShapeDrawable(dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset2, dimensionPixelOffset3);
        MaterialShapeDrawable popUpMaterialShapeDrawable2 = getPopUpMaterialShapeDrawable(0.0f, dimensionPixelOffset, dimensionPixelOffset2, dimensionPixelOffset3);
        this.outlinedPopupBackground = popUpMaterialShapeDrawable;
        StateListDrawable stateListDrawable = new StateListDrawable();
        this.filledPopupBackground = stateListDrawable;
        stateListDrawable.addState(new int[]{16842922}, popUpMaterialShapeDrawable);
        this.filledPopupBackground.addState(new int[0], popUpMaterialShapeDrawable2);
        this.textInputLayout.setEndIconDrawable(this.customEndIcon == 0 ? IS_LOLLIPOP ? R.drawable.mtrl_dropdown_arrow : R.drawable.mtrl_ic_arrow_drop_down : this.customEndIcon);
        this.textInputLayout.setEndIconContentDescription(this.textInputLayout.getResources().getText(R.string.exposed_dropdown_menu_content_description));
        this.textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DropdownMenuEndIconDelegate.this.showHideDropdown((AutoCompleteTextView) DropdownMenuEndIconDelegate.this.textInputLayout.getEditText());
            }
        });
        this.textInputLayout.addOnEditTextAttachedListener(this.dropdownMenuOnEditTextAttachedListener);
        this.textInputLayout.addOnEndIconChangedListener(this.endIconChangedListener);
        initAnimators();
        this.accessibilityManager = (AccessibilityManager) this.context.getSystemService("accessibility");
        this.textInputLayout.addOnAttachStateChangeListener(this.onAttachStateChangeListener);
        addTouchExplorationStateChangeListenerIfNeeded();
    }

    /* access modifiers changed from: package-private */
    public boolean isBoxBackgroundModeSupported(int boxBackgroundMode) {
        return boxBackgroundMode != 0;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldTintIconOnError() {
        return true;
    }

    /* access modifiers changed from: package-private */
    public void updateOutlinedRippleEffect(AutoCompleteTextView editText) {
        if (!isEditable(editText) && this.textInputLayout.getBoxBackgroundMode() == 2 && (editText.getBackground() instanceof LayerDrawable)) {
            addRippleEffect(editText);
        }
    }
}

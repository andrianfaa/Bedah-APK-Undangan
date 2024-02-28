package com.google.android.material.textfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.textfield.TextInputLayout;

class ClearTextEndIconDelegate extends EndIconDelegate {
    private static final int ANIMATION_FADE_DURATION = 100;
    private static final int ANIMATION_SCALE_DURATION = 150;
    private static final float ANIMATION_SCALE_FROM_VALUE = 0.8f;
    /* access modifiers changed from: private */
    public final TextWatcher clearTextEndIconTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (ClearTextEndIconDelegate.this.textInputLayout.getSuffixText() == null) {
                ClearTextEndIconDelegate clearTextEndIconDelegate = ClearTextEndIconDelegate.this;
                clearTextEndIconDelegate.animateIcon(clearTextEndIconDelegate.shouldBeVisible());
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };
    private final TextInputLayout.OnEditTextAttachedListener clearTextOnEditTextAttachedListener = new TextInputLayout.OnEditTextAttachedListener() {
        public void onEditTextAttached(TextInputLayout textInputLayout) {
            EditText editText = textInputLayout.getEditText();
            textInputLayout.setEndIconVisible(ClearTextEndIconDelegate.this.shouldBeVisible());
            editText.setOnFocusChangeListener(ClearTextEndIconDelegate.this.onFocusChangeListener);
            ClearTextEndIconDelegate.this.endIconView.setOnFocusChangeListener(ClearTextEndIconDelegate.this.onFocusChangeListener);
            editText.removeTextChangedListener(ClearTextEndIconDelegate.this.clearTextEndIconTextWatcher);
            editText.addTextChangedListener(ClearTextEndIconDelegate.this.clearTextEndIconTextWatcher);
        }
    };
    private final TextInputLayout.OnEndIconChangedListener endIconChangedListener = new TextInputLayout.OnEndIconChangedListener() {
        public void onEndIconChanged(TextInputLayout textInputLayout, int previousIcon) {
            final EditText editText = textInputLayout.getEditText();
            if (editText != null && previousIcon == 2) {
                editText.post(new Runnable() {
                    public void run() {
                        editText.removeTextChangedListener(ClearTextEndIconDelegate.this.clearTextEndIconTextWatcher);
                        ClearTextEndIconDelegate.this.animateIcon(true);
                    }
                });
                if (editText.getOnFocusChangeListener() == ClearTextEndIconDelegate.this.onFocusChangeListener) {
                    editText.setOnFocusChangeListener((View.OnFocusChangeListener) null);
                }
                if (ClearTextEndIconDelegate.this.endIconView.getOnFocusChangeListener() == ClearTextEndIconDelegate.this.onFocusChangeListener) {
                    ClearTextEndIconDelegate.this.endIconView.setOnFocusChangeListener((View.OnFocusChangeListener) null);
                }
            }
        }
    };
    private AnimatorSet iconInAnim;
    private ValueAnimator iconOutAnim;
    /* access modifiers changed from: private */
    public final View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            ClearTextEndIconDelegate clearTextEndIconDelegate = ClearTextEndIconDelegate.this;
            clearTextEndIconDelegate.animateIcon(clearTextEndIconDelegate.shouldBeVisible());
        }
    };

    ClearTextEndIconDelegate(TextInputLayout textInputLayout, int customEndIcon) {
        super(textInputLayout, customEndIcon);
    }

    /* access modifiers changed from: private */
    public void animateIcon(boolean show) {
        boolean z = this.textInputLayout.isEndIconVisible() == show;
        if (show && !this.iconInAnim.isRunning()) {
            this.iconOutAnim.cancel();
            this.iconInAnim.start();
            if (z) {
                this.iconInAnim.end();
            }
        } else if (!show) {
            this.iconInAnim.cancel();
            this.iconOutAnim.start();
            if (z) {
                this.iconOutAnim.end();
            }
        }
    }

    private ValueAnimator getAlphaAnimator(float... values) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(values);
        ofFloat.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        ofFloat.setDuration(100);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                ClearTextEndIconDelegate.this.endIconView.setAlpha(((Float) animation.getAnimatedValue()).floatValue());
            }
        });
        return ofFloat;
    }

    private ValueAnimator getScaleAnimator() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{ANIMATION_SCALE_FROM_VALUE, 1.0f});
        ofFloat.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
        ofFloat.setDuration(150);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float floatValue = ((Float) animation.getAnimatedValue()).floatValue();
                ClearTextEndIconDelegate.this.endIconView.setScaleX(floatValue);
                ClearTextEndIconDelegate.this.endIconView.setScaleY(floatValue);
            }
        });
        return ofFloat;
    }

    private void initAnimators() {
        ValueAnimator scaleAnimator = getScaleAnimator();
        ValueAnimator alphaAnimator = getAlphaAnimator(0.0f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        this.iconInAnim = animatorSet;
        animatorSet.playTogether(new Animator[]{scaleAnimator, alphaAnimator});
        this.iconInAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
                ClearTextEndIconDelegate.this.textInputLayout.setEndIconVisible(true);
            }
        });
        ValueAnimator alphaAnimator2 = getAlphaAnimator(1.0f, 0.0f);
        this.iconOutAnim = alphaAnimator2;
        alphaAnimator2.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                ClearTextEndIconDelegate.this.textInputLayout.setEndIconVisible(false);
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean shouldBeVisible() {
        EditText editText = this.textInputLayout.getEditText();
        return editText != null && (editText.hasFocus() || this.endIconView.hasFocus()) && editText.getText().length() > 0;
    }

    /* access modifiers changed from: package-private */
    public void initialize() {
        this.textInputLayout.setEndIconDrawable(this.customEndIcon == 0 ? R.drawable.mtrl_ic_cancel : this.customEndIcon);
        this.textInputLayout.setEndIconContentDescription(this.textInputLayout.getResources().getText(R.string.clear_text_end_icon_content_description));
        this.textInputLayout.setEndIconCheckable(false);
        this.textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Editable text = ClearTextEndIconDelegate.this.textInputLayout.getEditText().getText();
                if (text != null) {
                    text.clear();
                }
                ClearTextEndIconDelegate.this.textInputLayout.refreshEndIconDrawableState();
            }
        });
        this.textInputLayout.addOnEditTextAttachedListener(this.clearTextOnEditTextAttachedListener);
        this.textInputLayout.addOnEndIconChangedListener(this.endIconChangedListener);
        initAnimators();
    }

    /* access modifiers changed from: package-private */
    public void onSuffixVisibilityChanged(boolean visible) {
        if (this.textInputLayout.getSuffixText() != null) {
            animateIcon(visible);
        }
    }
}

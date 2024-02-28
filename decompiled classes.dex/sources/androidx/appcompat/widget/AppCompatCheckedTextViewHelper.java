package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CheckedTextView;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.CheckedTextViewCompat;

class AppCompatCheckedTextViewHelper {
    private ColorStateList mCheckMarkTintList = null;
    private PorterDuff.Mode mCheckMarkTintMode = null;
    private boolean mHasCheckMarkTint = false;
    private boolean mHasCheckMarkTintMode = false;
    private boolean mSkipNextApply;
    private final CheckedTextView mView;

    AppCompatCheckedTextViewHelper(CheckedTextView view) {
        this.mView = view;
    }

    /* access modifiers changed from: package-private */
    public void applyCheckMarkTint() {
        Drawable checkMarkDrawable = CheckedTextViewCompat.getCheckMarkDrawable(this.mView);
        if (checkMarkDrawable == null) {
            return;
        }
        if (this.mHasCheckMarkTint || this.mHasCheckMarkTintMode) {
            Drawable mutate = DrawableCompat.wrap(checkMarkDrawable).mutate();
            if (this.mHasCheckMarkTint) {
                DrawableCompat.setTintList(mutate, this.mCheckMarkTintList);
            }
            if (this.mHasCheckMarkTintMode) {
                DrawableCompat.setTintMode(mutate, this.mCheckMarkTintMode);
            }
            if (mutate.isStateful()) {
                mutate.setState(this.mView.getDrawableState());
            }
            this.mView.setCheckMarkDrawable(mutate);
        }
    }

    /* access modifiers changed from: package-private */
    public ColorStateList getSupportCheckMarkTintList() {
        return this.mCheckMarkTintList;
    }

    /* access modifiers changed from: package-private */
    public PorterDuff.Mode getSupportCheckMarkTintMode() {
        return this.mCheckMarkTintMode;
    }

    /* access modifiers changed from: package-private */
    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        int resourceId;
        int resourceId2;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), attrs, R.styleable.CheckedTextView, defStyleAttr, 0);
        CheckedTextView checkedTextView = this.mView;
        ViewCompat.saveAttributeDataForStyleable(checkedTextView, checkedTextView.getContext(), R.styleable.CheckedTextView, attrs, obtainStyledAttributes.getWrappedTypeArray(), defStyleAttr, 0);
        boolean z = false;
        try {
            if (obtainStyledAttributes.hasValue(R.styleable.CheckedTextView_checkMarkCompat) && (resourceId2 = obtainStyledAttributes.getResourceId(R.styleable.CheckedTextView_checkMarkCompat, 0)) != 0) {
                try {
                    CheckedTextView checkedTextView2 = this.mView;
                    checkedTextView2.setCheckMarkDrawable(AppCompatResources.getDrawable(checkedTextView2.getContext(), resourceId2));
                    z = true;
                } catch (Resources.NotFoundException e) {
                }
            }
            if (!z) {
                if (obtainStyledAttributes.hasValue(R.styleable.CheckedTextView_android_checkMark) && (resourceId = obtainStyledAttributes.getResourceId(R.styleable.CheckedTextView_android_checkMark, 0)) != 0) {
                    CheckedTextView checkedTextView3 = this.mView;
                    checkedTextView3.setCheckMarkDrawable(AppCompatResources.getDrawable(checkedTextView3.getContext(), resourceId));
                }
            }
            if (obtainStyledAttributes.hasValue(R.styleable.CheckedTextView_checkMarkTint)) {
                CheckedTextViewCompat.setCheckMarkTintList(this.mView, obtainStyledAttributes.getColorStateList(R.styleable.CheckedTextView_checkMarkTint));
            }
            if (obtainStyledAttributes.hasValue(R.styleable.CheckedTextView_checkMarkTintMode)) {
                CheckedTextViewCompat.setCheckMarkTintMode(this.mView, DrawableUtils.parseTintMode(obtainStyledAttributes.getInt(R.styleable.CheckedTextView_checkMarkTintMode, -1), (PorterDuff.Mode) null));
            }
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    /* access modifiers changed from: package-private */
    public void onSetCheckMarkDrawable() {
        if (this.mSkipNextApply) {
            this.mSkipNextApply = false;
            return;
        }
        this.mSkipNextApply = true;
        applyCheckMarkTint();
    }

    /* access modifiers changed from: package-private */
    public void setSupportCheckMarkTintList(ColorStateList tint) {
        this.mCheckMarkTintList = tint;
        this.mHasCheckMarkTint = true;
        applyCheckMarkTint();
    }

    /* access modifiers changed from: package-private */
    public void setSupportCheckMarkTintMode(PorterDuff.Mode tintMode) {
        this.mCheckMarkTintMode = tintMode;
        this.mHasCheckMarkTintMode = true;
        applyCheckMarkTint();
    }
}

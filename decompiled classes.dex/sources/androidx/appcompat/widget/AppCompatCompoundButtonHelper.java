package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import androidx.appcompat.R;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.CompoundButtonCompat;

class AppCompatCompoundButtonHelper {
    private ColorStateList mButtonTintList = null;
    private PorterDuff.Mode mButtonTintMode = null;
    private boolean mHasButtonTint = false;
    private boolean mHasButtonTintMode = false;
    private boolean mSkipNextApply;
    private final CompoundButton mView;

    AppCompatCompoundButtonHelper(CompoundButton view) {
        this.mView = view;
    }

    /* access modifiers changed from: package-private */
    public void applyButtonTint() {
        Drawable buttonDrawable = CompoundButtonCompat.getButtonDrawable(this.mView);
        if (buttonDrawable == null) {
            return;
        }
        if (this.mHasButtonTint || this.mHasButtonTintMode) {
            Drawable mutate = DrawableCompat.wrap(buttonDrawable).mutate();
            if (this.mHasButtonTint) {
                DrawableCompat.setTintList(mutate, this.mButtonTintList);
            }
            if (this.mHasButtonTintMode) {
                DrawableCompat.setTintMode(mutate, this.mButtonTintMode);
            }
            if (mutate.isStateful()) {
                mutate.setState(this.mView.getDrawableState());
            }
            this.mView.setButtonDrawable(mutate);
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r0 = androidx.core.widget.CompoundButtonCompat.getButtonDrawable(r2.mView);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getCompoundPaddingLeft(int r3) {
        /*
            r2 = this;
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 17
            if (r0 >= r1) goto L_0x0013
            android.widget.CompoundButton r0 = r2.mView
            android.graphics.drawable.Drawable r0 = androidx.core.widget.CompoundButtonCompat.getButtonDrawable(r0)
            if (r0 == 0) goto L_0x0013
            int r1 = r0.getIntrinsicWidth()
            int r3 = r3 + r1
        L_0x0013:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.AppCompatCompoundButtonHelper.getCompoundPaddingLeft(int):int");
    }

    /* access modifiers changed from: package-private */
    public ColorStateList getSupportButtonTintList() {
        return this.mButtonTintList;
    }

    /* access modifiers changed from: package-private */
    public PorterDuff.Mode getSupportButtonTintMode() {
        return this.mButtonTintMode;
    }

    /* access modifiers changed from: package-private */
    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        int resourceId;
        int resourceId2;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), attrs, R.styleable.CompoundButton, defStyleAttr, 0);
        CompoundButton compoundButton = this.mView;
        ViewCompat.saveAttributeDataForStyleable(compoundButton, compoundButton.getContext(), R.styleable.CompoundButton, attrs, obtainStyledAttributes.getWrappedTypeArray(), defStyleAttr, 0);
        boolean z = false;
        try {
            if (obtainStyledAttributes.hasValue(R.styleable.CompoundButton_buttonCompat) && (resourceId2 = obtainStyledAttributes.getResourceId(R.styleable.CompoundButton_buttonCompat, 0)) != 0) {
                try {
                    CompoundButton compoundButton2 = this.mView;
                    compoundButton2.setButtonDrawable(AppCompatResources.getDrawable(compoundButton2.getContext(), resourceId2));
                    z = true;
                } catch (Resources.NotFoundException e) {
                }
            }
            if (!z) {
                if (obtainStyledAttributes.hasValue(R.styleable.CompoundButton_android_button) && (resourceId = obtainStyledAttributes.getResourceId(R.styleable.CompoundButton_android_button, 0)) != 0) {
                    CompoundButton compoundButton3 = this.mView;
                    compoundButton3.setButtonDrawable(AppCompatResources.getDrawable(compoundButton3.getContext(), resourceId));
                }
            }
            if (obtainStyledAttributes.hasValue(R.styleable.CompoundButton_buttonTint)) {
                CompoundButtonCompat.setButtonTintList(this.mView, obtainStyledAttributes.getColorStateList(R.styleable.CompoundButton_buttonTint));
            }
            if (obtainStyledAttributes.hasValue(R.styleable.CompoundButton_buttonTintMode)) {
                CompoundButtonCompat.setButtonTintMode(this.mView, DrawableUtils.parseTintMode(obtainStyledAttributes.getInt(R.styleable.CompoundButton_buttonTintMode, -1), (PorterDuff.Mode) null));
            }
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    /* access modifiers changed from: package-private */
    public void onSetButtonDrawable() {
        if (this.mSkipNextApply) {
            this.mSkipNextApply = false;
            return;
        }
        this.mSkipNextApply = true;
        applyButtonTint();
    }

    /* access modifiers changed from: package-private */
    public void setSupportButtonTintList(ColorStateList tint) {
        this.mButtonTintList = tint;
        this.mHasButtonTint = true;
        applyButtonTint();
    }

    /* access modifiers changed from: package-private */
    public void setSupportButtonTintMode(PorterDuff.Mode tintMode) {
        this.mButtonTintMode = tintMode;
        this.mHasButtonTintMode = true;
        applyButtonTint();
    }
}

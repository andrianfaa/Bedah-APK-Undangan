package com.google.android.material.ripple;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.TintAwareDrawable;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;

public class RippleDrawableCompat extends Drawable implements Shapeable, TintAwareDrawable {
    private RippleDrawableCompatState drawableState;

    static final class RippleDrawableCompatState extends Drawable.ConstantState {
        MaterialShapeDrawable delegate;
        boolean shouldDrawDelegate;

        public RippleDrawableCompatState(RippleDrawableCompatState orig) {
            this.delegate = (MaterialShapeDrawable) orig.delegate.getConstantState().newDrawable();
            this.shouldDrawDelegate = orig.shouldDrawDelegate;
        }

        public RippleDrawableCompatState(MaterialShapeDrawable delegate2) {
            this.delegate = delegate2;
            this.shouldDrawDelegate = false;
        }

        public int getChangingConfigurations() {
            return 0;
        }

        public RippleDrawableCompat newDrawable() {
            return new RippleDrawableCompat(new RippleDrawableCompatState(this));
        }
    }

    private RippleDrawableCompat(RippleDrawableCompatState state) {
        this.drawableState = state;
    }

    public RippleDrawableCompat(ShapeAppearanceModel shapeAppearanceModel) {
        this(new RippleDrawableCompatState(new MaterialShapeDrawable(shapeAppearanceModel)));
    }

    public void draw(Canvas canvas) {
        if (this.drawableState.shouldDrawDelegate) {
            this.drawableState.delegate.draw(canvas);
        }
    }

    public Drawable.ConstantState getConstantState() {
        return this.drawableState;
    }

    public int getOpacity() {
        return this.drawableState.delegate.getOpacity();
    }

    public ShapeAppearanceModel getShapeAppearanceModel() {
        return this.drawableState.delegate.getShapeAppearanceModel();
    }

    public boolean isStateful() {
        return true;
    }

    public RippleDrawableCompat mutate() {
        this.drawableState = new RippleDrawableCompatState(this.drawableState);
        return this;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.drawableState.delegate.setBounds(bounds);
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] stateSet) {
        boolean onStateChange = super.onStateChange(stateSet);
        if (this.drawableState.delegate.setState(stateSet)) {
            onStateChange = true;
        }
        boolean shouldDrawRippleCompat = RippleUtils.shouldDrawRippleCompat(stateSet);
        if (this.drawableState.shouldDrawDelegate == shouldDrawRippleCompat) {
            return onStateChange;
        }
        this.drawableState.shouldDrawDelegate = shouldDrawRippleCompat;
        return true;
    }

    public void setAlpha(int alpha) {
        this.drawableState.delegate.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.drawableState.delegate.setColorFilter(colorFilter);
    }

    public void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        this.drawableState.delegate.setShapeAppearanceModel(shapeAppearanceModel);
    }

    public void setTint(int tintColor) {
        this.drawableState.delegate.setTint(tintColor);
    }

    public void setTintList(ColorStateList tintList) {
        this.drawableState.delegate.setTintList(tintList);
    }

    public void setTintMode(PorterDuff.Mode tintMode) {
        this.drawableState.delegate.setTintMode(tintMode);
    }
}

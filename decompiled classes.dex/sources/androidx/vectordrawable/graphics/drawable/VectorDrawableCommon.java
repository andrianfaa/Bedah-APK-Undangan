package androidx.vectordrawable.graphics.drawable;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.graphics.drawable.TintAwareDrawable;

abstract class VectorDrawableCommon extends Drawable implements TintAwareDrawable {
    Drawable mDelegateDrawable;

    VectorDrawableCommon() {
    }

    public void applyTheme(Resources.Theme t) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            DrawableCompat.applyTheme(drawable, t);
        }
    }

    public void clearColorFilter() {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            drawable.clearColorFilter();
        } else {
            super.clearColorFilter();
        }
    }

    public Drawable getCurrent() {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? drawable.getCurrent() : super.getCurrent();
    }

    public int getMinimumHeight() {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? drawable.getMinimumHeight() : super.getMinimumHeight();
    }

    public int getMinimumWidth() {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? drawable.getMinimumWidth() : super.getMinimumWidth();
    }

    public boolean getPadding(Rect padding) {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? drawable.getPadding(padding) : super.getPadding(padding);
    }

    public int[] getState() {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? drawable.getState() : super.getState();
    }

    public Region getTransparentRegion() {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? drawable.getTransparentRegion() : super.getTransparentRegion();
    }

    public void jumpToCurrentState() {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            DrawableCompat.jumpToCurrentState(drawable);
        }
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect bounds) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            drawable.setBounds(bounds);
        } else {
            super.onBoundsChange(bounds);
        }
    }

    /* access modifiers changed from: protected */
    public boolean onLevelChange(int level) {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? drawable.setLevel(level) : super.onLevelChange(level);
    }

    public void setChangingConfigurations(int configs) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            drawable.setChangingConfigurations(configs);
        } else {
            super.setChangingConfigurations(configs);
        }
    }

    public void setColorFilter(int color, PorterDuff.Mode mode) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            drawable.setColorFilter(color, mode);
        } else {
            super.setColorFilter(color, mode);
        }
    }

    public void setFilterBitmap(boolean filter) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            drawable.setFilterBitmap(filter);
        }
    }

    public void setHotspot(float x, float y) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            DrawableCompat.setHotspot(drawable, x, y);
        }
    }

    public void setHotspotBounds(int left, int top, int right, int bottom) {
        Drawable drawable = this.mDelegateDrawable;
        if (drawable != null) {
            DrawableCompat.setHotspotBounds(drawable, left, top, right, bottom);
        }
    }

    public boolean setState(int[] stateSet) {
        Drawable drawable = this.mDelegateDrawable;
        return drawable != null ? drawable.setState(stateSet) : super.setState(stateSet);
    }
}

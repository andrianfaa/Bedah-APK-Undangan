package androidx.appcompat.graphics.drawable;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.drawable.DrawableCompat;

public class DrawableWrapper extends Drawable implements Drawable.Callback {
    private Drawable mDrawable;

    public DrawableWrapper(Drawable drawable) {
        setWrappedDrawable(drawable);
    }

    public void draw(Canvas canvas) {
        this.mDrawable.draw(canvas);
    }

    public int getChangingConfigurations() {
        return this.mDrawable.getChangingConfigurations();
    }

    public Drawable getCurrent() {
        return this.mDrawable.getCurrent();
    }

    public int getIntrinsicHeight() {
        return this.mDrawable.getIntrinsicHeight();
    }

    public int getIntrinsicWidth() {
        return this.mDrawable.getIntrinsicWidth();
    }

    public int getMinimumHeight() {
        return this.mDrawable.getMinimumHeight();
    }

    public int getMinimumWidth() {
        return this.mDrawable.getMinimumWidth();
    }

    public int getOpacity() {
        return this.mDrawable.getOpacity();
    }

    public boolean getPadding(Rect padding) {
        return this.mDrawable.getPadding(padding);
    }

    public int[] getState() {
        return this.mDrawable.getState();
    }

    public Region getTransparentRegion() {
        return this.mDrawable.getTransparentRegion();
    }

    public Drawable getWrappedDrawable() {
        return this.mDrawable;
    }

    public void invalidateDrawable(Drawable who) {
        invalidateSelf();
    }

    public boolean isAutoMirrored() {
        return DrawableCompat.isAutoMirrored(this.mDrawable);
    }

    public boolean isStateful() {
        return this.mDrawable.isStateful();
    }

    public void jumpToCurrentState() {
        this.mDrawable.jumpToCurrentState();
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect bounds) {
        this.mDrawable.setBounds(bounds);
    }

    /* access modifiers changed from: protected */
    public boolean onLevelChange(int level) {
        return this.mDrawable.setLevel(level);
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        scheduleSelf(what, when);
    }

    public void setAlpha(int alpha) {
        this.mDrawable.setAlpha(alpha);
    }

    public void setAutoMirrored(boolean mirrored) {
        DrawableCompat.setAutoMirrored(this.mDrawable, mirrored);
    }

    public void setChangingConfigurations(int configs) {
        this.mDrawable.setChangingConfigurations(configs);
    }

    public void setColorFilter(ColorFilter cf) {
        this.mDrawable.setColorFilter(cf);
    }

    public void setDither(boolean dither) {
        this.mDrawable.setDither(dither);
    }

    public void setFilterBitmap(boolean filter) {
        this.mDrawable.setFilterBitmap(filter);
    }

    public void setHotspot(float x, float y) {
        DrawableCompat.setHotspot(this.mDrawable, x, y);
    }

    public void setHotspotBounds(int left, int top, int right, int bottom) {
        DrawableCompat.setHotspotBounds(this.mDrawable, left, top, right, bottom);
    }

    public boolean setState(int[] stateSet) {
        return this.mDrawable.setState(stateSet);
    }

    public void setTint(int tint) {
        DrawableCompat.setTint(this.mDrawable, tint);
    }

    public void setTintList(ColorStateList tint) {
        DrawableCompat.setTintList(this.mDrawable, tint);
    }

    public void setTintMode(PorterDuff.Mode tintMode) {
        DrawableCompat.setTintMode(this.mDrawable, tintMode);
    }

    public boolean setVisible(boolean visible, boolean restart) {
        return super.setVisible(visible, restart) || this.mDrawable.setVisible(visible, restart);
    }

    public void setWrappedDrawable(Drawable drawable) {
        Drawable drawable2 = this.mDrawable;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback) null);
        }
        this.mDrawable = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        unscheduleSelf(what);
    }
}

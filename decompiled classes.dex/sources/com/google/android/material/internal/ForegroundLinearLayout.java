package com.google.android.material.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.view.GravityCompat;
import com.google.android.material.R;

public class ForegroundLinearLayout extends LinearLayoutCompat {
    private Drawable foreground;
    boolean foregroundBoundsChanged;
    private int foregroundGravity;
    protected boolean mForegroundInPadding;
    private final Rect overlayBounds;
    private final Rect selfBounds;

    public ForegroundLinearLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public ForegroundLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ForegroundLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.selfBounds = new Rect();
        this.overlayBounds = new Rect();
        this.foregroundGravity = 119;
        this.mForegroundInPadding = true;
        this.foregroundBoundsChanged = false;
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attrs, R.styleable.ForegroundLinearLayout, defStyle, 0, new int[0]);
        this.foregroundGravity = obtainStyledAttributes.getInt(R.styleable.ForegroundLinearLayout_android_foregroundGravity, this.foregroundGravity);
        Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.ForegroundLinearLayout_android_foreground);
        if (drawable != null) {
            setForeground(drawable);
        }
        this.mForegroundInPadding = obtainStyledAttributes.getBoolean(R.styleable.ForegroundLinearLayout_foregroundInsidePadding, true);
        obtainStyledAttributes.recycle();
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.foreground != null) {
            Drawable drawable = this.foreground;
            if (this.foregroundBoundsChanged) {
                this.foregroundBoundsChanged = false;
                Rect rect = this.selfBounds;
                Rect rect2 = this.overlayBounds;
                int right = getRight() - getLeft();
                int bottom = getBottom() - getTop();
                if (this.mForegroundInPadding) {
                    rect.set(0, 0, right, bottom);
                } else {
                    rect.set(getPaddingLeft(), getPaddingTop(), right - getPaddingRight(), bottom - getPaddingBottom());
                }
                Gravity.apply(this.foregroundGravity, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), rect, rect2);
                drawable.setBounds(rect2);
            }
            drawable.draw(canvas);
        }
    }

    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        Drawable drawable = this.foreground;
        if (drawable != null) {
            drawable.setHotspot(x, y);
        }
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.foreground;
        if (drawable != null && drawable.isStateful()) {
            this.foreground.setState(getDrawableState());
        }
    }

    public Drawable getForeground() {
        return this.foreground;
    }

    public int getForegroundGravity() {
        return this.foregroundGravity;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.foreground;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.foregroundBoundsChanged |= changed;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.foregroundBoundsChanged = true;
    }

    public void setForeground(Drawable drawable) {
        Drawable drawable2 = this.foreground;
        if (drawable2 != drawable) {
            if (drawable2 != null) {
                drawable2.setCallback((Drawable.Callback) null);
                unscheduleDrawable(this.foreground);
            }
            this.foreground = drawable;
            if (drawable != null) {
                setWillNotDraw(false);
                drawable.setCallback(this);
                if (drawable.isStateful()) {
                    drawable.setState(getDrawableState());
                }
                if (this.foregroundGravity == 119) {
                    drawable.getPadding(new Rect());
                }
            } else {
                setWillNotDraw(true);
            }
            requestLayout();
            invalidate();
        }
    }

    public void setForegroundGravity(int foregroundGravity2) {
        if (this.foregroundGravity != foregroundGravity2) {
            if ((8388615 & foregroundGravity2) == 0) {
                foregroundGravity2 |= GravityCompat.START;
            }
            if ((foregroundGravity2 & 112) == 0) {
                foregroundGravity2 |= 48;
            }
            this.foregroundGravity = foregroundGravity2;
            if (foregroundGravity2 == 119 && this.foreground != null) {
                this.foreground.getPadding(new Rect());
            }
            requestLayout();
        }
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.foreground;
    }
}

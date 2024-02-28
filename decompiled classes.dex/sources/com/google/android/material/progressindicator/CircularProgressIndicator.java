package com.google.android.material.progressindicator;

import android.content.Context;
import android.util.AttributeSet;
import com.google.android.material.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class CircularProgressIndicator extends BaseProgressIndicator<CircularProgressIndicatorSpec> {
    public static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_CircularProgressIndicator;
    public static final int INDICATOR_DIRECTION_CLOCKWISE = 0;
    public static final int INDICATOR_DIRECTION_COUNTERCLOCKWISE = 1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface IndicatorDirection {
    }

    public CircularProgressIndicator(Context context) {
        this(context, (AttributeSet) null);
    }

    public CircularProgressIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.circularProgressIndicatorStyle);
    }

    public CircularProgressIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, DEF_STYLE_RES);
        initializeDrawables();
    }

    private void initializeDrawables() {
        setIndeterminateDrawable(IndeterminateDrawable.createCircularDrawable(getContext(), (CircularProgressIndicatorSpec) this.spec));
        setProgressDrawable(DeterminateDrawable.createCircularDrawable(getContext(), (CircularProgressIndicatorSpec) this.spec));
    }

    /* access modifiers changed from: package-private */
    public CircularProgressIndicatorSpec createSpec(Context context, AttributeSet attrs) {
        return new CircularProgressIndicatorSpec(context, attrs);
    }

    public int getIndicatorDirection() {
        return ((CircularProgressIndicatorSpec) this.spec).indicatorDirection;
    }

    public int getIndicatorInset() {
        return ((CircularProgressIndicatorSpec) this.spec).indicatorInset;
    }

    public int getIndicatorSize() {
        return ((CircularProgressIndicatorSpec) this.spec).indicatorSize;
    }

    public void setIndicatorDirection(int indicatorDirection) {
        ((CircularProgressIndicatorSpec) this.spec).indicatorDirection = indicatorDirection;
        invalidate();
    }

    public void setIndicatorInset(int indicatorInset) {
        if (((CircularProgressIndicatorSpec) this.spec).indicatorInset != indicatorInset) {
            ((CircularProgressIndicatorSpec) this.spec).indicatorInset = indicatorInset;
            invalidate();
        }
    }

    public void setIndicatorSize(int indicatorSize) {
        int indicatorSize2 = Math.max(indicatorSize, getTrackThickness() * 2);
        if (((CircularProgressIndicatorSpec) this.spec).indicatorSize != indicatorSize2) {
            ((CircularProgressIndicatorSpec) this.spec).indicatorSize = indicatorSize2;
            ((CircularProgressIndicatorSpec) this.spec).validateSpec();
            invalidate();
        }
    }

    public void setTrackThickness(int trackThickness) {
        super.setTrackThickness(trackThickness);
        ((CircularProgressIndicatorSpec) this.spec).validateSpec();
    }
}

package com.google.android.material.timepicker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import java.util.ArrayList;
import java.util.List;

class ClockHandView extends View {
    private static final int ANIMATION_DURATION = 200;
    private boolean animatingOnTouchUp;
    private final float centerDotRadius;
    private boolean changedDuringTouch;
    private int circleRadius;
    private double degRad;
    private float downX;
    private float downY;
    private boolean isInTapRegion;
    private final List<OnRotateListener> listeners;
    private OnActionUpListener onActionUpListener;
    private float originalDeg;
    private final Paint paint;
    private ValueAnimator rotationAnimator;
    private int scaledTouchSlop;
    private final RectF selectorBox;
    private final int selectorRadius;
    private final int selectorStrokeWidth;

    public interface OnActionUpListener {
        void onActionUp(float f, boolean z);
    }

    public interface OnRotateListener {
        void onRotate(float f, boolean z);
    }

    public ClockHandView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ClockHandView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.materialClockStyle);
    }

    public ClockHandView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.listeners = new ArrayList();
        Paint paint2 = new Paint();
        this.paint = paint2;
        this.selectorBox = new RectF();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ClockHandView, defStyleAttr, R.style.Widget_MaterialComponents_TimePicker_Clock);
        this.circleRadius = obtainStyledAttributes.getDimensionPixelSize(R.styleable.ClockHandView_materialCircleRadius, 0);
        this.selectorRadius = obtainStyledAttributes.getDimensionPixelSize(R.styleable.ClockHandView_selectorSize, 0);
        Resources resources = getResources();
        this.selectorStrokeWidth = resources.getDimensionPixelSize(R.dimen.material_clock_hand_stroke_width);
        this.centerDotRadius = (float) resources.getDimensionPixelSize(R.dimen.material_clock_hand_center_dot_radius);
        int color = obtainStyledAttributes.getColor(R.styleable.ClockHandView_clockHandColor, 0);
        paint2.setAntiAlias(true);
        paint2.setColor(color);
        setHandRotation(0.0f);
        this.scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        ViewCompat.setImportantForAccessibility(this, 2);
        obtainStyledAttributes.recycle();
    }

    private void drawSelector(Canvas canvas) {
        Canvas canvas2 = canvas;
        int height = getHeight() / 2;
        int width = getWidth() / 2;
        float cos = ((float) width) + (((float) this.circleRadius) * ((float) Math.cos(this.degRad)));
        float sin = ((float) height) + (((float) this.circleRadius) * ((float) Math.sin(this.degRad)));
        this.paint.setStrokeWidth(0.0f);
        canvas2.drawCircle(cos, sin, (float) this.selectorRadius, this.paint);
        double sin2 = Math.sin(this.degRad);
        double cos2 = Math.cos(this.degRad);
        float f = (float) (this.circleRadius - this.selectorRadius);
        float f2 = (float) (((int) (((double) f) * cos2)) + width);
        this.paint.setStrokeWidth((float) this.selectorStrokeWidth);
        Canvas canvas3 = canvas;
        float f3 = (float) (((int) (((double) f) * sin2)) + height);
        float f4 = f2;
        float f5 = f;
        canvas3.drawLine((float) width, (float) height, f2, f3, this.paint);
        canvas2.drawCircle((float) width, (float) height, this.centerDotRadius, this.paint);
    }

    private int getDegreesFromXY(float x, float y) {
        int degrees = ((int) Math.toDegrees(Math.atan2((double) (y - ((float) (getHeight() / 2))), (double) (x - ((float) (getWidth() / 2)))))) + 90;
        return degrees < 0 ? degrees + 360 : degrees;
    }

    private Pair<Float, Float> getValuesForAnimation(float degrees) {
        float handRotation = getHandRotation();
        if (Math.abs(handRotation - degrees) > 180.0f) {
            if (handRotation > 180.0f && degrees < 180.0f) {
                degrees += 360.0f;
            }
            if (handRotation < 180.0f && degrees > 180.0f) {
                handRotation += 360.0f;
            }
        }
        return new Pair<>(Float.valueOf(handRotation), Float.valueOf(degrees));
    }

    private boolean handleTouchInput(float x, float y, boolean forceSelection, boolean touchDown, boolean actionUp) {
        int degreesFromXY = getDegreesFromXY(x, y);
        boolean z = false;
        boolean z2 = getHandRotation() != ((float) degreesFromXY);
        if (touchDown && z2) {
            return true;
        }
        if (!z2 && !forceSelection) {
            return false;
        }
        float f = (float) degreesFromXY;
        if (actionUp && this.animatingOnTouchUp) {
            z = true;
        }
        setHandRotation(f, z);
        return true;
    }

    /* access modifiers changed from: private */
    public void setHandRotationInternal(float degrees, boolean animate) {
        float degrees2 = degrees % 360.0f;
        this.originalDeg = degrees2;
        this.degRad = Math.toRadians((double) (degrees2 - 90.0f));
        float width = ((float) (getWidth() / 2)) + (((float) this.circleRadius) * ((float) Math.cos(this.degRad)));
        float height = ((float) (getHeight() / 2)) + (((float) this.circleRadius) * ((float) Math.sin(this.degRad)));
        RectF rectF = this.selectorBox;
        int i = this.selectorRadius;
        rectF.set(width - ((float) i), height - ((float) i), ((float) i) + width, ((float) i) + height);
        for (OnRotateListener onRotate : this.listeners) {
            onRotate.onRotate(degrees2, animate);
        }
        invalidate();
    }

    public void addOnRotateListener(OnRotateListener listener) {
        this.listeners.add(listener);
    }

    public RectF getCurrentSelectorBox() {
        return this.selectorBox;
    }

    public float getHandRotation() {
        return this.originalDeg;
    }

    public int getSelectorRadius() {
        return this.selectorRadius;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSelector(canvas);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setHandRotation(getHandRotation());
    }

    public boolean onTouchEvent(MotionEvent event) {
        OnActionUpListener onActionUpListener2;
        int actionMasked = event.getActionMasked();
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        float x = event.getX();
        float y = event.getY();
        boolean z4 = false;
        switch (actionMasked) {
            case 0:
                this.downX = x;
                this.downY = y;
                this.isInTapRegion = true;
                this.changedDuringTouch = false;
                z2 = true;
                break;
            case 1:
            case 2:
                int i = (int) (x - this.downX);
                int i2 = (int) (y - this.downY);
                this.isInTapRegion = (i * i) + (i2 * i2) > this.scaledTouchSlop;
                if (this.changedDuringTouch) {
                    z = true;
                }
                if (actionMasked == 1) {
                    z4 = true;
                }
                z3 = z4;
                break;
        }
        boolean handleTouchInput = handleTouchInput(x, y, z, z2, z3) | this.changedDuringTouch;
        this.changedDuringTouch = handleTouchInput;
        if (handleTouchInput && z3 && (onActionUpListener2 = this.onActionUpListener) != null) {
            onActionUpListener2.onActionUp((float) getDegreesFromXY(x, y), this.isInTapRegion);
        }
        return true;
    }

    public void setAnimateOnTouchUp(boolean animating) {
        this.animatingOnTouchUp = animating;
    }

    public void setCircleRadius(int circleRadius2) {
        this.circleRadius = circleRadius2;
        invalidate();
    }

    public void setHandRotation(float degrees) {
        setHandRotation(degrees, false);
    }

    public void setHandRotation(float degrees, boolean animate) {
        ValueAnimator valueAnimator = this.rotationAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (!animate) {
            setHandRotationInternal(degrees, false);
            return;
        }
        Pair<Float, Float> valuesForAnimation = getValuesForAnimation(degrees);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{((Float) valuesForAnimation.first).floatValue(), ((Float) valuesForAnimation.second).floatValue()});
        this.rotationAnimator = ofFloat;
        ofFloat.setDuration(200);
        this.rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                ClockHandView.this.setHandRotationInternal(((Float) animation.getAnimatedValue()).floatValue(), true);
            }
        });
        this.rotationAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animation) {
                animation.end();
            }
        });
        this.rotationAnimator.start();
    }

    public void setOnActionUpListener(OnActionUpListener listener) {
        this.onActionUpListener = listener;
    }
}

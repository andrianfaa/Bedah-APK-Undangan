package com.google.android.material.timepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.timepicker.ClockHandView;
import java.util.Arrays;

class ClockFaceView extends RadialViewGroup implements ClockHandView.OnRotateListener {
    private static final float EPSILON = 0.001f;
    private static final int INITIAL_CAPACITY = 12;
    private static final String VALUE_PLACEHOLDER = "";
    /* access modifiers changed from: private */
    public final int clockHandPadding;
    /* access modifiers changed from: private */
    public final ClockHandView clockHandView;
    private final int clockSize;
    private float currentHandRotation;
    private final int[] gradientColors;
    private final float[] gradientPositions;
    private final int minimumHeight;
    private final int minimumWidth;
    private final RectF scratch;
    private final ColorStateList textColor;
    /* access modifiers changed from: private */
    public final SparseArray<TextView> textViewPool;
    private final Rect textViewRect;
    private final AccessibilityDelegateCompat valueAccessibilityDelegate;
    private String[] values;

    public ClockFaceView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ClockFaceView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.materialClockStyle);
    }

    public ClockFaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.textViewRect = new Rect();
        this.scratch = new RectF();
        this.textViewPool = new SparseArray<>();
        this.gradientPositions = new float[]{0.0f, 0.9f, 1.0f};
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ClockFaceView, defStyleAttr, R.style.Widget_MaterialComponents_TimePicker_Clock);
        Resources resources = getResources();
        ColorStateList colorStateList = MaterialResources.getColorStateList(context, obtainStyledAttributes, R.styleable.ClockFaceView_clockNumberTextColor);
        this.textColor = colorStateList;
        LayoutInflater.from(context).inflate(R.layout.material_clockface_view, this, true);
        ClockHandView clockHandView2 = (ClockHandView) findViewById(R.id.material_clock_hand);
        this.clockHandView = clockHandView2;
        this.clockHandPadding = resources.getDimensionPixelSize(R.dimen.material_clock_hand_padding);
        int colorForState = colorStateList.getColorForState(new int[]{16842913}, colorStateList.getDefaultColor());
        this.gradientColors = new int[]{colorForState, colorForState, colorStateList.getDefaultColor()};
        clockHandView2.addOnRotateListener(this);
        int defaultColor = AppCompatResources.getColorStateList(context, R.color.material_timepicker_clockface).getDefaultColor();
        ColorStateList colorStateList2 = MaterialResources.getColorStateList(context, obtainStyledAttributes, R.styleable.ClockFaceView_clockFaceBackgroundColor);
        setBackgroundColor(colorStateList2 == null ? defaultColor : colorStateList2.getDefaultColor());
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if (!ClockFaceView.this.isShown()) {
                    return true;
                }
                ClockFaceView.this.getViewTreeObserver().removeOnPreDrawListener(this);
                ClockFaceView.this.setRadius(((ClockFaceView.this.getHeight() / 2) - ClockFaceView.this.clockHandView.getSelectorRadius()) - ClockFaceView.this.clockHandPadding);
                return true;
            }
        });
        setFocusable(true);
        obtainStyledAttributes.recycle();
        this.valueAccessibilityDelegate = new AccessibilityDelegateCompat() {
            public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
                super.onInitializeAccessibilityNodeInfo(host, info);
                int intValue = ((Integer) host.getTag(R.id.material_value_index)).intValue();
                if (intValue > 0) {
                    info.setTraversalAfter((View) ClockFaceView.this.textViewPool.get(intValue - 1));
                }
                info.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(0, 1, intValue, 1, false, host.isSelected()));
                info.setClickable(true);
                info.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
            }

            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                if (action != 16) {
                    return super.performAccessibilityAction(host, action, args);
                }
                long uptimeMillis = SystemClock.uptimeMillis();
                float x = host.getX() + (((float) host.getWidth()) / 2.0f);
                long j = uptimeMillis;
                float f = x;
                float y = host.getY() + (((float) host.getHeight()) / 2.0f);
                ClockFaceView.this.clockHandView.onTouchEvent(MotionEvent.obtain(uptimeMillis, j, 0, f, y, 0));
                ClockFaceView.this.clockHandView.onTouchEvent(MotionEvent.obtain(uptimeMillis, j, 1, f, y, 0));
                return true;
            }
        };
        String[] strArr = new String[12];
        Arrays.fill(strArr, "");
        setValues(strArr, 0);
        this.minimumHeight = resources.getDimensionPixelSize(R.dimen.material_time_picker_minimum_screen_height);
        this.minimumWidth = resources.getDimensionPixelSize(R.dimen.material_time_picker_minimum_screen_width);
        this.clockSize = resources.getDimensionPixelSize(R.dimen.material_clock_size);
    }

    private void findIntersectingTextView() {
        RectF currentSelectorBox = this.clockHandView.getCurrentSelectorBox();
        for (int i = 0; i < this.textViewPool.size(); i++) {
            TextView textView = this.textViewPool.get(i);
            if (textView != null) {
                textView.getDrawingRect(this.textViewRect);
                offsetDescendantRectToMyCoords(textView, this.textViewRect);
                textView.setSelected(currentSelectorBox.contains((float) this.textViewRect.centerX(), (float) this.textViewRect.centerY()));
                textView.getPaint().setShader(getGradientForTextView(currentSelectorBox, this.textViewRect, textView));
                textView.invalidate();
            }
        }
    }

    private RadialGradient getGradientForTextView(RectF selectorBox, Rect tvBox, TextView tv) {
        this.scratch.set(tvBox);
        this.scratch.offset((float) tv.getPaddingLeft(), (float) tv.getPaddingTop());
        if (!RectF.intersects(selectorBox, this.scratch)) {
            return null;
        }
        return new RadialGradient(selectorBox.centerX() - this.scratch.left, selectorBox.centerY() - this.scratch.top, 0.5f * selectorBox.width(), this.gradientColors, this.gradientPositions, Shader.TileMode.CLAMP);
    }

    private static float max3(float a, float b, float c) {
        return Math.max(Math.max(a, b), c);
    }

    /* JADX WARNING: type inference failed for: r5v7, types: [android.view.View] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateTextViews(int r9) {
        /*
            r8 = this;
            android.content.Context r0 = r8.getContext()
            android.view.LayoutInflater r0 = android.view.LayoutInflater.from(r0)
            android.util.SparseArray<android.widget.TextView> r1 = r8.textViewPool
            int r1 = r1.size()
            r2 = 0
        L_0x000f:
            java.lang.String[] r3 = r8.values
            int r3 = r3.length
            int r3 = java.lang.Math.max(r3, r1)
            if (r2 >= r3) goto L_0x0078
            android.util.SparseArray<android.widget.TextView> r3 = r8.textViewPool
            java.lang.Object r3 = r3.get(r2)
            android.widget.TextView r3 = (android.widget.TextView) r3
            java.lang.String[] r4 = r8.values
            int r4 = r4.length
            if (r2 < r4) goto L_0x002e
            r8.removeView(r3)
            android.util.SparseArray<android.widget.TextView> r4 = r8.textViewPool
            r4.remove(r2)
            goto L_0x0075
        L_0x002e:
            r4 = 0
            if (r3 != 0) goto L_0x0042
            int r5 = com.google.android.material.R.layout.material_clockface_textview
            android.view.View r5 = r0.inflate(r5, r8, r4)
            r3 = r5
            android.widget.TextView r3 = (android.widget.TextView) r3
            android.util.SparseArray<android.widget.TextView> r5 = r8.textViewPool
            r5.put(r2, r3)
            r8.addView(r3)
        L_0x0042:
            r3.setVisibility(r4)
            java.lang.String[] r5 = r8.values
            r5 = r5[r2]
            r3.setText(r5)
            int r5 = com.google.android.material.R.id.material_value_index
            java.lang.Integer r6 = java.lang.Integer.valueOf(r2)
            r3.setTag(r5, r6)
            androidx.core.view.AccessibilityDelegateCompat r5 = r8.valueAccessibilityDelegate
            androidx.core.view.ViewCompat.setAccessibilityDelegate(r3, r5)
            android.content.res.ColorStateList r5 = r8.textColor
            r3.setTextColor(r5)
            if (r9 == 0) goto L_0x0075
            android.content.res.Resources r5 = r8.getResources()
            r6 = 1
            java.lang.Object[] r6 = new java.lang.Object[r6]
            java.lang.String[] r7 = r8.values
            r7 = r7[r2]
            r6[r4] = r7
            java.lang.String r4 = r5.getString(r9, r6)
            r3.setContentDescription(r4)
        L_0x0075:
            int r2 = r2 + 1
            goto L_0x000f
        L_0x0078:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.timepicker.ClockFaceView.updateTextViews(int):void");
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        AccessibilityNodeInfoCompat.wrap(info).setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(1, this.values.length, false, 1));
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        findIntersectingTextView();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int max3 = (int) (((float) this.clockSize) / max3(((float) this.minimumHeight) / ((float) displayMetrics.heightPixels), ((float) this.minimumWidth) / ((float) displayMetrics.widthPixels), 1.0f));
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(max3, BasicMeasure.EXACTLY);
        setMeasuredDimension(max3, max3);
        super.onMeasure(makeMeasureSpec, makeMeasureSpec);
    }

    public void onRotate(float rotation, boolean animating) {
        if (Math.abs(this.currentHandRotation - rotation) > EPSILON) {
            this.currentHandRotation = rotation;
            findIntersectingTextView();
        }
    }

    public void setHandRotation(float rotation) {
        this.clockHandView.setHandRotation(rotation);
        findIntersectingTextView();
    }

    public void setRadius(int radius) {
        if (radius != getRadius()) {
            super.setRadius(radius);
            this.clockHandView.setCircleRadius(getRadius());
        }
    }

    public void setValues(String[] values2, int contentDescription) {
        this.values = values2;
        updateTextViews(contentDescription);
    }
}

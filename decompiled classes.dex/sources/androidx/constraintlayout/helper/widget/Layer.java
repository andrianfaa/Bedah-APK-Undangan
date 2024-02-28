package androidx.constraintlayout.helper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.R;

public class Layer extends ConstraintHelper {
    private static final String TAG = "Layer";
    private boolean mApplyElevationOnAttach;
    private boolean mApplyVisibilityOnAttach;
    protected float mComputedCenterX = Float.NaN;
    protected float mComputedCenterY = Float.NaN;
    protected float mComputedMaxX = Float.NaN;
    protected float mComputedMaxY = Float.NaN;
    protected float mComputedMinX = Float.NaN;
    protected float mComputedMinY = Float.NaN;
    ConstraintLayout mContainer;
    private float mGroupRotateAngle = Float.NaN;
    boolean mNeedBounds = true;
    private float mRotationCenterX = Float.NaN;
    private float mRotationCenterY = Float.NaN;
    private float mScaleX = 1.0f;
    private float mScaleY = 1.0f;
    private float mShiftX = 0.0f;
    private float mShiftY = 0.0f;
    View[] mViews = null;

    public Layer(Context context) {
        super(context);
    }

    public Layer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Layer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void reCacheViews() {
        if (this.mContainer != null && this.mCount != 0) {
            View[] viewArr = this.mViews;
            if (viewArr == null || viewArr.length != this.mCount) {
                this.mViews = new View[this.mCount];
            }
            for (int i = 0; i < this.mCount; i++) {
                this.mViews[i] = this.mContainer.getViewById(this.mIds[i]);
            }
        }
    }

    private void transform() {
        if (this.mContainer != null) {
            if (this.mViews == null) {
                reCacheViews();
            }
            calcCenters();
            double radians = Float.isNaN(this.mGroupRotateAngle) ? 0.0d : Math.toRadians((double) this.mGroupRotateAngle);
            float sin = (float) Math.sin(radians);
            float cos = (float) Math.cos(radians);
            float f = this.mScaleX;
            float f2 = f * cos;
            float f3 = this.mScaleY;
            float f4 = (-f3) * sin;
            float f5 = f * sin;
            float f6 = f3 * cos;
            int i = 0;
            while (i < this.mCount) {
                View view = this.mViews[i];
                float left = ((float) ((view.getLeft() + view.getRight()) / 2)) - this.mComputedCenterX;
                float top = ((float) ((view.getTop() + view.getBottom()) / 2)) - this.mComputedCenterY;
                double d = radians;
                view.setTranslationX((((f2 * left) + (f4 * top)) - left) + this.mShiftX);
                view.setTranslationY((((f5 * left) + (f6 * top)) - top) + this.mShiftY);
                view.setScaleY(this.mScaleY);
                view.setScaleX(this.mScaleX);
                if (!Float.isNaN(this.mGroupRotateAngle)) {
                    view.setRotation(this.mGroupRotateAngle);
                }
                i++;
                radians = d;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void applyLayoutFeaturesInConstraintSet(ConstraintLayout container) {
        applyLayoutFeatures(container);
    }

    /* access modifiers changed from: protected */
    public void calcCenters() {
        if (this.mContainer != null) {
            if (!this.mNeedBounds && !Float.isNaN(this.mComputedCenterX) && !Float.isNaN(this.mComputedCenterY)) {
                return;
            }
            if (Float.isNaN(this.mRotationCenterX) || Float.isNaN(this.mRotationCenterY)) {
                View[] views = getViews(this.mContainer);
                int left = views[0].getLeft();
                int top = views[0].getTop();
                int right = views[0].getRight();
                int bottom = views[0].getBottom();
                for (int i = 0; i < this.mCount; i++) {
                    View view = views[i];
                    left = Math.min(left, view.getLeft());
                    top = Math.min(top, view.getTop());
                    right = Math.max(right, view.getRight());
                    bottom = Math.max(bottom, view.getBottom());
                }
                this.mComputedMaxX = (float) right;
                this.mComputedMaxY = (float) bottom;
                this.mComputedMinX = (float) left;
                this.mComputedMinY = (float) top;
                if (Float.isNaN(this.mRotationCenterX)) {
                    this.mComputedCenterX = (float) ((left + right) / 2);
                } else {
                    this.mComputedCenterX = this.mRotationCenterX;
                }
                if (Float.isNaN(this.mRotationCenterY)) {
                    this.mComputedCenterY = (float) ((top + bottom) / 2);
                } else {
                    this.mComputedCenterY = this.mRotationCenterY;
                }
            } else {
                this.mComputedCenterY = this.mRotationCenterY;
                this.mComputedCenterX = this.mRotationCenterX;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void init(AttributeSet attrs) {
        super.init(attrs);
        this.mUseViewMeasure = false;
        if (attrs != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.ConstraintLayout_Layout);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.ConstraintLayout_Layout_android_visibility) {
                    this.mApplyVisibilityOnAttach = true;
                } else if (index == R.styleable.ConstraintLayout_Layout_android_elevation) {
                    this.mApplyElevationOnAttach = true;
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mContainer = (ConstraintLayout) getParent();
        if (this.mApplyVisibilityOnAttach || this.mApplyElevationOnAttach) {
            int visibility = getVisibility();
            float f = 0.0f;
            if (Build.VERSION.SDK_INT >= 21) {
                f = getElevation();
            }
            for (int i = 0; i < this.mCount; i++) {
                View viewById = this.mContainer.getViewById(this.mIds[i]);
                if (viewById != null) {
                    if (this.mApplyVisibilityOnAttach) {
                        viewById.setVisibility(visibility);
                    }
                    if (this.mApplyElevationOnAttach && f > 0.0f && Build.VERSION.SDK_INT >= 21) {
                        viewById.setTranslationZ(viewById.getTranslationZ() + f);
                    }
                }
            }
        }
    }

    public void setElevation(float elevation) {
        super.setElevation(elevation);
        applyLayoutFeatures();
    }

    public void setPivotX(float pivotX) {
        this.mRotationCenterX = pivotX;
        transform();
    }

    public void setPivotY(float pivotY) {
        this.mRotationCenterY = pivotY;
        transform();
    }

    public void setRotation(float angle) {
        this.mGroupRotateAngle = angle;
        transform();
    }

    public void setScaleX(float scaleX) {
        this.mScaleX = scaleX;
        transform();
    }

    public void setScaleY(float scaleY) {
        this.mScaleY = scaleY;
        transform();
    }

    public void setTranslationX(float dx) {
        this.mShiftX = dx;
        transform();
    }

    public void setTranslationY(float dy) {
        this.mShiftY = dy;
        transform();
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        applyLayoutFeatures();
    }

    public void updatePostLayout(ConstraintLayout container) {
        reCacheViews();
        this.mComputedCenterX = Float.NaN;
        this.mComputedCenterY = Float.NaN;
        ConstraintWidget constraintWidget = ((ConstraintLayout.LayoutParams) getLayoutParams()).getConstraintWidget();
        constraintWidget.setWidth(0);
        constraintWidget.setHeight(0);
        calcCenters();
        layout(((int) this.mComputedMinX) - getPaddingLeft(), ((int) this.mComputedMinY) - getPaddingTop(), ((int) this.mComputedMaxX) + getPaddingRight(), ((int) this.mComputedMaxY) + getPaddingBottom());
        transform();
    }

    public void updatePreDraw(ConstraintLayout container) {
        this.mContainer = container;
        float rotation = getRotation();
        if (rotation != 0.0f) {
            this.mGroupRotateAngle = rotation;
        } else if (!Float.isNaN(this.mGroupRotateAngle)) {
            this.mGroupRotateAngle = rotation;
        }
    }
}

package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Constraints extends ViewGroup {
    public static final String TAG = "Constraints";
    ConstraintSet myConstraintSet;

    public static class LayoutParams extends ConstraintLayout.LayoutParams {
        public float alpha = 1.0f;
        public boolean applyElevation = false;
        public float elevation = 0.0f;
        public float rotation = 0.0f;
        public float rotationX = 0.0f;
        public float rotationY = 0.0f;
        public float scaleX = 1.0f;
        public float scaleY = 1.0f;
        public float transformPivotX = 0.0f;
        public float transformPivotY = 0.0f;
        public float translationX = 0.0f;
        public float translationY = 0.0f;
        public float translationZ = 0.0f;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray obtainStyledAttributes = c.obtainStyledAttributes(attrs, R.styleable.ConstraintSet);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.ConstraintSet_android_alpha) {
                    this.alpha = obtainStyledAttributes.getFloat(index, this.alpha);
                } else if (index == R.styleable.ConstraintSet_android_elevation) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        this.elevation = obtainStyledAttributes.getFloat(index, this.elevation);
                        this.applyElevation = true;
                    }
                } else if (index == R.styleable.ConstraintSet_android_rotationX) {
                    this.rotationX = obtainStyledAttributes.getFloat(index, this.rotationX);
                } else if (index == R.styleable.ConstraintSet_android_rotationY) {
                    this.rotationY = obtainStyledAttributes.getFloat(index, this.rotationY);
                } else if (index == R.styleable.ConstraintSet_android_rotation) {
                    this.rotation = obtainStyledAttributes.getFloat(index, this.rotation);
                } else if (index == R.styleable.ConstraintSet_android_scaleX) {
                    this.scaleX = obtainStyledAttributes.getFloat(index, this.scaleX);
                } else if (index == R.styleable.ConstraintSet_android_scaleY) {
                    this.scaleY = obtainStyledAttributes.getFloat(index, this.scaleY);
                } else if (index == R.styleable.ConstraintSet_android_transformPivotX) {
                    this.transformPivotX = obtainStyledAttributes.getFloat(index, this.transformPivotX);
                } else if (index == R.styleable.ConstraintSet_android_transformPivotY) {
                    this.transformPivotY = obtainStyledAttributes.getFloat(index, this.transformPivotY);
                } else if (index == R.styleable.ConstraintSet_android_translationX) {
                    this.translationX = obtainStyledAttributes.getFloat(index, this.translationX);
                } else if (index == R.styleable.ConstraintSet_android_translationY) {
                    this.translationY = obtainStyledAttributes.getFloat(index, this.translationY);
                } else if (index == R.styleable.ConstraintSet_android_translationZ && Build.VERSION.SDK_INT >= 21) {
                    this.translationZ = obtainStyledAttributes.getFloat(index, this.translationZ);
                }
            }
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(LayoutParams source) {
            super((ConstraintLayout.LayoutParams) source);
        }
    }

    public Constraints(Context context) {
        super(context);
        super.setVisibility(8);
    }

    public Constraints(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        super.setVisibility(8);
    }

    public Constraints(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        super.setVisibility(8);
    }

    private void init(AttributeSet attrs) {
        Log.v(TAG, " ################# init");
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new ConstraintLayout.LayoutParams(p);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public ConstraintSet getConstraintSet() {
        if (this.myConstraintSet == null) {
            this.myConstraintSet = new ConstraintSet();
        }
        this.myConstraintSet.clone(this);
        return this.myConstraintSet;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
    }
}

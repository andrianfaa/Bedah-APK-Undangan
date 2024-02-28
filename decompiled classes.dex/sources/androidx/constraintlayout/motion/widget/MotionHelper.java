package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.R;
import java.util.HashMap;

public class MotionHelper extends ConstraintHelper implements MotionHelperInterface {
    private float mProgress;
    private boolean mUseOnHide = false;
    private boolean mUseOnShow = false;
    protected View[] views;

    public MotionHelper(Context context) {
        super(context);
    }

    public MotionHelper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MotionHelper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public float getProgress() {
        return this.mProgress;
    }

    /* access modifiers changed from: protected */
    public void init(AttributeSet attrs) {
        super.init(attrs);
        if (attrs != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.MotionHelper);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.MotionHelper_onShow) {
                    this.mUseOnShow = obtainStyledAttributes.getBoolean(index, this.mUseOnShow);
                } else if (index == R.styleable.MotionHelper_onHide) {
                    this.mUseOnHide = obtainStyledAttributes.getBoolean(index, this.mUseOnHide);
                }
            }
            obtainStyledAttributes.recycle();
        }
    }

    public boolean isDecorator() {
        return false;
    }

    public boolean isUseOnHide() {
        return this.mUseOnHide;
    }

    public boolean isUsedOnShow() {
        return this.mUseOnShow;
    }

    public void onFinishedMotionScene(MotionLayout motionLayout) {
    }

    public void onPostDraw(Canvas canvas) {
    }

    public void onPreDraw(Canvas canvas) {
    }

    public void onPreSetup(MotionLayout motionLayout, HashMap<View, MotionController> hashMap) {
    }

    public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {
    }

    public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
    }

    public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
    }

    public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        if (this.mCount > 0) {
            this.views = getViews((ConstraintLayout) getParent());
            for (int i = 0; i < this.mCount; i++) {
                setProgress(this.views[i], progress);
            }
            return;
        }
        ViewGroup viewGroup = (ViewGroup) getParent();
        int childCount = viewGroup.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = viewGroup.getChildAt(i2);
            if (!(childAt instanceof MotionHelper)) {
                setProgress(childAt, progress);
            }
        }
    }

    public void setProgress(View view, float progress) {
    }
}

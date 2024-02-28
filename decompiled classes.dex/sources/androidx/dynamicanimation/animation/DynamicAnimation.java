package androidx.dynamicanimation.animation;

import android.os.Looper;
import android.util.AndroidRuntimeException;
import android.view.View;
import androidx.constraintlayout.motion.widget.Key;
import androidx.core.view.ViewCompat;
import androidx.dynamicanimation.animation.AnimationHandler;
import androidx.dynamicanimation.animation.DynamicAnimation;
import java.util.ArrayList;

public abstract class DynamicAnimation<T extends DynamicAnimation<T>> implements AnimationHandler.AnimationFrameCallback {
    public static final ViewProperty ALPHA = new ViewProperty("alpha") {
        public float getValue(View view) {
            return view.getAlpha();
        }

        public void setValue(View view, float value) {
            view.setAlpha(value);
        }
    };
    public static final float MIN_VISIBLE_CHANGE_ALPHA = 0.00390625f;
    public static final float MIN_VISIBLE_CHANGE_PIXELS = 1.0f;
    public static final float MIN_VISIBLE_CHANGE_ROTATION_DEGREES = 0.1f;
    public static final float MIN_VISIBLE_CHANGE_SCALE = 0.002f;
    public static final ViewProperty ROTATION = new ViewProperty(Key.ROTATION) {
        public float getValue(View view) {
            return view.getRotation();
        }

        public void setValue(View view, float value) {
            view.setRotation(value);
        }
    };
    public static final ViewProperty ROTATION_X = new ViewProperty("rotationX") {
        public float getValue(View view) {
            return view.getRotationX();
        }

        public void setValue(View view, float value) {
            view.setRotationX(value);
        }
    };
    public static final ViewProperty ROTATION_Y = new ViewProperty("rotationY") {
        public float getValue(View view) {
            return view.getRotationY();
        }

        public void setValue(View view, float value) {
            view.setRotationY(value);
        }
    };
    public static final ViewProperty SCALE_X = new ViewProperty("scaleX") {
        public float getValue(View view) {
            return view.getScaleX();
        }

        public void setValue(View view, float value) {
            view.setScaleX(value);
        }
    };
    public static final ViewProperty SCALE_Y = new ViewProperty("scaleY") {
        public float getValue(View view) {
            return view.getScaleY();
        }

        public void setValue(View view, float value) {
            view.setScaleY(value);
        }
    };
    public static final ViewProperty SCROLL_X = new ViewProperty("scrollX") {
        public float getValue(View view) {
            return (float) view.getScrollX();
        }

        public void setValue(View view, float value) {
            view.setScrollX((int) value);
        }
    };
    public static final ViewProperty SCROLL_Y = new ViewProperty("scrollY") {
        public float getValue(View view) {
            return (float) view.getScrollY();
        }

        public void setValue(View view, float value) {
            view.setScrollY((int) value);
        }
    };
    private static final float THRESHOLD_MULTIPLIER = 0.75f;
    public static final ViewProperty TRANSLATION_X = new ViewProperty("translationX") {
        public float getValue(View view) {
            return view.getTranslationX();
        }

        public void setValue(View view, float value) {
            view.setTranslationX(value);
        }
    };
    public static final ViewProperty TRANSLATION_Y = new ViewProperty("translationY") {
        public float getValue(View view) {
            return view.getTranslationY();
        }

        public void setValue(View view, float value) {
            view.setTranslationY(value);
        }
    };
    public static final ViewProperty TRANSLATION_Z = new ViewProperty("translationZ") {
        public float getValue(View view) {
            return ViewCompat.getTranslationZ(view);
        }

        public void setValue(View view, float value) {
            ViewCompat.setTranslationZ(view, value);
        }
    };
    private static final float UNSET = Float.MAX_VALUE;
    public static final ViewProperty X = new ViewProperty("x") {
        public float getValue(View view) {
            return view.getX();
        }

        public void setValue(View view, float value) {
            view.setX(value);
        }
    };
    public static final ViewProperty Y = new ViewProperty("y") {
        public float getValue(View view) {
            return view.getY();
        }

        public void setValue(View view, float value) {
            view.setY(value);
        }
    };
    public static final ViewProperty Z = new ViewProperty("z") {
        public float getValue(View view) {
            return ViewCompat.getZ(view);
        }

        public void setValue(View view, float value) {
            ViewCompat.setZ(view, value);
        }
    };
    private final ArrayList<OnAnimationEndListener> mEndListeners;
    private long mLastFrameTime;
    float mMaxValue;
    float mMinValue;
    private float mMinVisibleChange;
    final FloatPropertyCompat mProperty;
    boolean mRunning;
    boolean mStartValueIsSet;
    final Object mTarget;
    private final ArrayList<OnAnimationUpdateListener> mUpdateListeners;
    float mValue;
    float mVelocity;

    static class MassState {
        float mValue;
        float mVelocity;

        MassState() {
        }
    }

    public interface OnAnimationEndListener {
        void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2);
    }

    public interface OnAnimationUpdateListener {
        void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2);
    }

    public static abstract class ViewProperty extends FloatPropertyCompat<View> {
        private ViewProperty(String name) {
            super(name);
        }
    }

    DynamicAnimation(final FloatValueHolder floatValueHolder) {
        this.mVelocity = 0.0f;
        this.mValue = Float.MAX_VALUE;
        this.mStartValueIsSet = false;
        this.mRunning = false;
        this.mMaxValue = Float.MAX_VALUE;
        this.mMinValue = -Float.MAX_VALUE;
        this.mLastFrameTime = 0;
        this.mEndListeners = new ArrayList<>();
        this.mUpdateListeners = new ArrayList<>();
        this.mTarget = null;
        this.mProperty = new FloatPropertyCompat("FloatValueHolder") {
            public float getValue(Object object) {
                return floatValueHolder.getValue();
            }

            public void setValue(Object object, float value) {
                floatValueHolder.setValue(value);
            }
        };
        this.mMinVisibleChange = 1.0f;
    }

    <K> DynamicAnimation(K k, FloatPropertyCompat<K> floatPropertyCompat) {
        this.mVelocity = 0.0f;
        this.mValue = Float.MAX_VALUE;
        this.mStartValueIsSet = false;
        this.mRunning = false;
        this.mMaxValue = Float.MAX_VALUE;
        this.mMinValue = -Float.MAX_VALUE;
        this.mLastFrameTime = 0;
        this.mEndListeners = new ArrayList<>();
        this.mUpdateListeners = new ArrayList<>();
        this.mTarget = k;
        this.mProperty = floatPropertyCompat;
        if (floatPropertyCompat == ROTATION || floatPropertyCompat == ROTATION_X || floatPropertyCompat == ROTATION_Y) {
            this.mMinVisibleChange = 0.1f;
        } else if (floatPropertyCompat == ALPHA) {
            this.mMinVisibleChange = 0.00390625f;
        } else if (floatPropertyCompat == SCALE_X || floatPropertyCompat == SCALE_Y) {
            this.mMinVisibleChange = 0.00390625f;
        } else {
            this.mMinVisibleChange = 1.0f;
        }
    }

    private void endAnimationInternal(boolean canceled) {
        this.mRunning = false;
        AnimationHandler.getInstance().removeCallback(this);
        this.mLastFrameTime = 0;
        this.mStartValueIsSet = false;
        for (int i = 0; i < this.mEndListeners.size(); i++) {
            if (this.mEndListeners.get(i) != null) {
                this.mEndListeners.get(i).onAnimationEnd(this, canceled, this.mValue, this.mVelocity);
            }
        }
        removeNullEntries(this.mEndListeners);
    }

    private float getPropertyValue() {
        return this.mProperty.getValue(this.mTarget);
    }

    private static <T> void removeEntry(ArrayList<T> arrayList, T t) {
        int indexOf = arrayList.indexOf(t);
        if (indexOf >= 0) {
            arrayList.set(indexOf, (Object) null);
        }
    }

    private static <T> void removeNullEntries(ArrayList<T> arrayList) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            if (arrayList.get(size) == null) {
                arrayList.remove(size);
            }
        }
    }

    private void startAnimationInternal() {
        if (!this.mRunning) {
            this.mRunning = true;
            if (!this.mStartValueIsSet) {
                this.mValue = getPropertyValue();
            }
            float f = this.mValue;
            if (f > this.mMaxValue || f < this.mMinValue) {
                throw new IllegalArgumentException("Starting value need to be in between min value and max value");
            }
            AnimationHandler.getInstance().addAnimationFrameCallback(this, 0);
        }
    }

    public T addEndListener(OnAnimationEndListener listener) {
        if (!this.mEndListeners.contains(listener)) {
            this.mEndListeners.add(listener);
        }
        return this;
    }

    public T addUpdateListener(OnAnimationUpdateListener listener) {
        if (!isRunning()) {
            if (!this.mUpdateListeners.contains(listener)) {
                this.mUpdateListeners.add(listener);
            }
            return this;
        }
        throw new UnsupportedOperationException("Error: Update listeners must be added beforethe animation.");
    }

    public void cancel() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new AndroidRuntimeException("Animations may only be canceled on the main thread");
        } else if (this.mRunning) {
            endAnimationInternal(true);
        }
    }

    public boolean doAnimationFrame(long frameTime) {
        long j = this.mLastFrameTime;
        if (j == 0) {
            this.mLastFrameTime = frameTime;
            setPropertyValue(this.mValue);
            return false;
        }
        this.mLastFrameTime = frameTime;
        boolean updateValueAndVelocity = updateValueAndVelocity(frameTime - j);
        float min = Math.min(this.mValue, this.mMaxValue);
        this.mValue = min;
        float max = Math.max(min, this.mMinValue);
        this.mValue = max;
        setPropertyValue(max);
        if (updateValueAndVelocity) {
            endAnimationInternal(false);
        }
        return updateValueAndVelocity;
    }

    /* access modifiers changed from: package-private */
    public abstract float getAcceleration(float f, float f2);

    public float getMinimumVisibleChange() {
        return this.mMinVisibleChange;
    }

    /* access modifiers changed from: package-private */
    public float getValueThreshold() {
        return this.mMinVisibleChange * 0.75f;
    }

    /* access modifiers changed from: package-private */
    public abstract boolean isAtEquilibrium(float f, float f2);

    public boolean isRunning() {
        return this.mRunning;
    }

    public void removeEndListener(OnAnimationEndListener listener) {
        removeEntry(this.mEndListeners, listener);
    }

    public void removeUpdateListener(OnAnimationUpdateListener listener) {
        removeEntry(this.mUpdateListeners, listener);
    }

    public T setMaxValue(float max) {
        this.mMaxValue = max;
        return this;
    }

    public T setMinValue(float min) {
        this.mMinValue = min;
        return this;
    }

    public T setMinimumVisibleChange(float minimumVisibleChange) {
        if (minimumVisibleChange > 0.0f) {
            this.mMinVisibleChange = minimumVisibleChange;
            setValueThreshold(0.75f * minimumVisibleChange);
            return this;
        }
        throw new IllegalArgumentException("Minimum visible change must be positive.");
    }

    /* access modifiers changed from: package-private */
    public void setPropertyValue(float value) {
        this.mProperty.setValue(this.mTarget, value);
        for (int i = 0; i < this.mUpdateListeners.size(); i++) {
            if (this.mUpdateListeners.get(i) != null) {
                this.mUpdateListeners.get(i).onAnimationUpdate(this, this.mValue, this.mVelocity);
            }
        }
        removeNullEntries(this.mUpdateListeners);
    }

    public T setStartValue(float startValue) {
        this.mValue = startValue;
        this.mStartValueIsSet = true;
        return this;
    }

    public T setStartVelocity(float startVelocity) {
        this.mVelocity = startVelocity;
        return this;
    }

    /* access modifiers changed from: package-private */
    public abstract void setValueThreshold(float f);

    public void start() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new AndroidRuntimeException("Animations may only be started on the main thread");
        } else if (!this.mRunning) {
            startAnimationInternal();
        }
    }

    /* access modifiers changed from: package-private */
    public abstract boolean updateValueAndVelocity(long j);
}

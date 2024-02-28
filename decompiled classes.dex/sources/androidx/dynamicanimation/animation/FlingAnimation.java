package androidx.dynamicanimation.animation;

import androidx.dynamicanimation.animation.DynamicAnimation;

public final class FlingAnimation extends DynamicAnimation<FlingAnimation> {
    private final DragForce mFlingForce;

    static final class DragForce implements Force {
        private static final float DEFAULT_FRICTION = -4.2f;
        private static final float VELOCITY_THRESHOLD_MULTIPLIER = 62.5f;
        private float mFriction = DEFAULT_FRICTION;
        private final DynamicAnimation.MassState mMassState = new DynamicAnimation.MassState();
        private float mVelocityThreshold;

        DragForce() {
        }

        public float getAcceleration(float position, float velocity) {
            return this.mFriction * velocity;
        }

        /* access modifiers changed from: package-private */
        public float getFrictionScalar() {
            return this.mFriction / DEFAULT_FRICTION;
        }

        public boolean isAtEquilibrium(float value, float velocity) {
            return Math.abs(velocity) < this.mVelocityThreshold;
        }

        /* access modifiers changed from: package-private */
        public void setFrictionScalar(float frictionScalar) {
            this.mFriction = DEFAULT_FRICTION * frictionScalar;
        }

        /* access modifiers changed from: package-private */
        public void setValueThreshold(float threshold) {
            this.mVelocityThreshold = VELOCITY_THRESHOLD_MULTIPLIER * threshold;
        }

        /* access modifiers changed from: package-private */
        public DynamicAnimation.MassState updateValueAndVelocity(float value, float velocity, long deltaT) {
            this.mMassState.mVelocity = (float) (((double) velocity) * Math.exp((double) ((((float) deltaT) / 1000.0f) * this.mFriction)));
            DynamicAnimation.MassState massState = this.mMassState;
            float f = this.mFriction;
            massState.mValue = (float) (((double) (value - (velocity / f))) + (((double) (velocity / f)) * Math.exp((double) ((f * ((float) deltaT)) / 1000.0f))));
            if (isAtEquilibrium(this.mMassState.mValue, this.mMassState.mVelocity)) {
                this.mMassState.mVelocity = 0.0f;
            }
            return this.mMassState;
        }
    }

    public FlingAnimation(FloatValueHolder floatValueHolder) {
        super(floatValueHolder);
        DragForce dragForce = new DragForce();
        this.mFlingForce = dragForce;
        dragForce.setValueThreshold(getValueThreshold());
    }

    public <K> FlingAnimation(K k, FloatPropertyCompat<K> floatPropertyCompat) {
        super(k, floatPropertyCompat);
        DragForce dragForce = new DragForce();
        this.mFlingForce = dragForce;
        dragForce.setValueThreshold(getValueThreshold());
    }

    /* access modifiers changed from: package-private */
    public float getAcceleration(float value, float velocity) {
        return this.mFlingForce.getAcceleration(value, velocity);
    }

    public float getFriction() {
        return this.mFlingForce.getFrictionScalar();
    }

    /* access modifiers changed from: package-private */
    public boolean isAtEquilibrium(float value, float velocity) {
        return value >= this.mMaxValue || value <= this.mMinValue || this.mFlingForce.isAtEquilibrium(value, velocity);
    }

    public FlingAnimation setFriction(float friction) {
        if (friction > 0.0f) {
            this.mFlingForce.setFrictionScalar(friction);
            return this;
        }
        throw new IllegalArgumentException("Friction must be positive");
    }

    public FlingAnimation setMaxValue(float maxValue) {
        super.setMaxValue(maxValue);
        return this;
    }

    public FlingAnimation setMinValue(float minValue) {
        super.setMinValue(minValue);
        return this;
    }

    public FlingAnimation setStartVelocity(float startVelocity) {
        super.setStartVelocity(startVelocity);
        return this;
    }

    /* access modifiers changed from: package-private */
    public void setValueThreshold(float threshold) {
        this.mFlingForce.setValueThreshold(threshold);
    }

    /* access modifiers changed from: package-private */
    public boolean updateValueAndVelocity(long deltaT) {
        DynamicAnimation.MassState updateValueAndVelocity = this.mFlingForce.updateValueAndVelocity(this.mValue, this.mVelocity, deltaT);
        this.mValue = updateValueAndVelocity.mValue;
        this.mVelocity = updateValueAndVelocity.mVelocity;
        if (this.mValue < this.mMinValue) {
            this.mValue = this.mMinValue;
            return true;
        } else if (this.mValue <= this.mMaxValue) {
            return isAtEquilibrium(this.mValue, this.mVelocity);
        } else {
            this.mValue = this.mMaxValue;
            return true;
        }
    }
}

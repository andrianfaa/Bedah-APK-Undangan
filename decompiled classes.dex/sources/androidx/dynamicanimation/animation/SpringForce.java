package androidx.dynamicanimation.animation;

import androidx.dynamicanimation.animation.DynamicAnimation;

public final class SpringForce implements Force {
    public static final float DAMPING_RATIO_HIGH_BOUNCY = 0.2f;
    public static final float DAMPING_RATIO_LOW_BOUNCY = 0.75f;
    public static final float DAMPING_RATIO_MEDIUM_BOUNCY = 0.5f;
    public static final float DAMPING_RATIO_NO_BOUNCY = 1.0f;
    public static final float STIFFNESS_HIGH = 10000.0f;
    public static final float STIFFNESS_LOW = 200.0f;
    public static final float STIFFNESS_MEDIUM = 1500.0f;
    public static final float STIFFNESS_VERY_LOW = 50.0f;
    private static final double UNSET = Double.MAX_VALUE;
    private static final double VELOCITY_THRESHOLD_MULTIPLIER = 62.5d;
    private double mDampedFreq;
    double mDampingRatio = 0.5d;
    private double mFinalPosition = Double.MAX_VALUE;
    private double mGammaMinus;
    private double mGammaPlus;
    private boolean mInitialized = false;
    private final DynamicAnimation.MassState mMassState = new DynamicAnimation.MassState();
    double mNaturalFreq = Math.sqrt(1500.0d);
    private double mValueThreshold;
    private double mVelocityThreshold;

    public SpringForce() {
    }

    public SpringForce(float finalPosition) {
        this.mFinalPosition = (double) finalPosition;
    }

    private void init() {
        if (!this.mInitialized) {
            if (this.mFinalPosition != Double.MAX_VALUE) {
                double d = this.mDampingRatio;
                if (d > 1.0d) {
                    double d2 = this.mNaturalFreq;
                    this.mGammaPlus = ((-d) * d2) + (d2 * Math.sqrt((d * d) - 1.0d));
                    double d3 = this.mDampingRatio;
                    double d4 = this.mNaturalFreq;
                    this.mGammaMinus = ((-d3) * d4) - (d4 * Math.sqrt((d3 * d3) - 1.0d));
                } else if (d >= 0.0d && d < 1.0d) {
                    this.mDampedFreq = this.mNaturalFreq * Math.sqrt(1.0d - (d * d));
                }
                this.mInitialized = true;
                return;
            }
            throw new IllegalStateException("Error: Final position of the spring must be set before the animation starts");
        }
    }

    public float getAcceleration(float lastDisplacement, float lastVelocity) {
        float lastDisplacement2 = lastDisplacement - getFinalPosition();
        double d = this.mNaturalFreq;
        return (float) (((-(d * d)) * ((double) lastDisplacement2)) - (((double) lastVelocity) * ((d * 2.0d) * this.mDampingRatio)));
    }

    public float getDampingRatio() {
        return (float) this.mDampingRatio;
    }

    public float getFinalPosition() {
        return (float) this.mFinalPosition;
    }

    public float getStiffness() {
        double d = this.mNaturalFreq;
        return (float) (d * d);
    }

    public boolean isAtEquilibrium(float value, float velocity) {
        return ((double) Math.abs(velocity)) < this.mVelocityThreshold && ((double) Math.abs(value - getFinalPosition())) < this.mValueThreshold;
    }

    public SpringForce setDampingRatio(float dampingRatio) {
        if (dampingRatio >= 0.0f) {
            this.mDampingRatio = (double) dampingRatio;
            this.mInitialized = false;
            return this;
        }
        throw new IllegalArgumentException("Damping ratio must be non-negative");
    }

    public SpringForce setFinalPosition(float finalPosition) {
        this.mFinalPosition = (double) finalPosition;
        return this;
    }

    public SpringForce setStiffness(float stiffness) {
        if (stiffness > 0.0f) {
            this.mNaturalFreq = Math.sqrt((double) stiffness);
            this.mInitialized = false;
            return this;
        }
        throw new IllegalArgumentException("Spring stiffness constant must be positive.");
    }

    /* access modifiers changed from: package-private */
    public void setValueThreshold(double threshold) {
        double abs = Math.abs(threshold);
        this.mValueThreshold = abs;
        this.mVelocityThreshold = abs * VELOCITY_THRESHOLD_MULTIPLIER;
    }

    /* access modifiers changed from: package-private */
    public DynamicAnimation.MassState updateValues(double lastDisplacement, double lastVelocity, long timeElapsed) {
        double d;
        double d2;
        init();
        double d3 = ((double) timeElapsed) / 1000.0d;
        double d4 = lastDisplacement - this.mFinalPosition;
        double d5 = this.mDampingRatio;
        if (d5 > 1.0d) {
            double d6 = this.mGammaMinus;
            double d7 = this.mGammaPlus;
            double d8 = d4 - (((d6 * d4) - lastVelocity) / (d6 - d7));
            double d9 = ((d6 * d4) - lastVelocity) / (d6 - d7);
            d = (Math.pow(2.718281828459045d, d6 * d3) * d8) + (Math.pow(2.718281828459045d, this.mGammaPlus * d3) * d9);
            double d10 = this.mGammaMinus;
            double pow = d8 * d10 * Math.pow(2.718281828459045d, d10 * d3);
            double d11 = this.mGammaPlus;
            double d12 = d4;
            d2 = pow + (d9 * d11 * Math.pow(2.718281828459045d, d11 * d3));
        } else if (d5 == 1.0d) {
            double d13 = d4;
            double d14 = this.mNaturalFreq;
            double d15 = lastVelocity + (d14 * d4);
            double pow2 = (d13 + (d15 * d3)) * Math.pow(2.718281828459045d, (-this.mNaturalFreq) * d3);
            double d16 = this.mNaturalFreq;
            double d17 = d4;
            d = Math.pow(2.718281828459045d, (-d14) * d3) * (d13 + (d15 * d3));
            d2 = (pow2 * (-d16)) + (Math.pow(2.718281828459045d, (-d16) * d3) * d15);
        } else {
            double d18 = d4;
            double d19 = 1.0d / this.mDampedFreq;
            double d20 = this.mNaturalFreq;
            double d21 = d19 * ((d5 * d20 * d4) + lastVelocity);
            double pow3 = Math.pow(2.718281828459045d, (-d5) * d20 * d3) * ((Math.cos(this.mDampedFreq * d3) * d18) + (Math.sin(this.mDampedFreq * d3) * d21));
            double d22 = this.mNaturalFreq;
            double d23 = d4;
            double d24 = this.mDampingRatio;
            double d25 = (-d22) * pow3 * d24;
            double pow4 = Math.pow(2.718281828459045d, (-d24) * d22 * d3);
            double d26 = this.mDampedFreq;
            double lastDisplacement2 = pow3;
            double sin = (-d26) * d18 * Math.sin(d26 * d3);
            double d27 = this.mDampedFreq;
            d = lastDisplacement2;
            d2 = d25 + (pow4 * (sin + (d27 * d21 * Math.cos(d27 * d3))));
        }
        this.mMassState.mValue = (float) (this.mFinalPosition + d);
        this.mMassState.mVelocity = (float) d2;
        return this.mMassState;
    }
}

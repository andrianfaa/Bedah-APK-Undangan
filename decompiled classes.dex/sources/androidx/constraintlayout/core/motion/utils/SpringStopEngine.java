package androidx.constraintlayout.core.motion.utils;

public class SpringStopEngine implements StopEngine {
    private static final double UNSET = Double.MAX_VALUE;
    private int mBoundaryMode = 0;
    double mDamping = 0.5d;
    private boolean mInitialized = false;
    private float mLastTime;
    private double mLastVelocity;
    private float mMass;
    private float mPos;
    private double mStiffness;
    private float mStopThreshold;
    private double mTargetPos;
    private float mV;

    private void compute(double dt) {
        double d = this.mStiffness;
        double d2 = this.mDamping;
        int sqrt = (int) ((9.0d / ((Math.sqrt(this.mStiffness / ((double) this.mMass)) * dt) * 4.0d)) + 1.0d);
        double d3 = dt / ((double) sqrt);
        int i = 0;
        while (i < sqrt) {
            float f = this.mPos;
            double d4 = this.mTargetPos;
            double d5 = ((double) f) - d4;
            int i2 = sqrt;
            float f2 = this.mV;
            double dt2 = d5;
            double d6 = ((-d) * d5) - (((double) f2) * d2);
            float f3 = this.mMass;
            double d7 = d2;
            double d8 = d6 / ((double) f3);
            double d9 = ((double) f2) + ((d8 * d3) / 2.0d);
            double d10 = d8;
            double d11 = d;
            double d12 = (((-((((double) f) + ((d3 * d9) / 2.0d)) - d4)) * d) - (d9 * d7)) / ((double) f3);
            double d13 = d12 * d3;
            double d14 = d9;
            double d15 = d12;
            float f4 = (float) (((double) f2) + d13);
            this.mV = f4;
            float f5 = (float) (((double) f) + ((((double) f2) + (d13 / 2.0d)) * d3));
            this.mPos = f5;
            int i3 = this.mBoundaryMode;
            if (i3 > 0) {
                if (f5 < 0.0f && (i3 & 1) == 1) {
                    this.mPos = -f5;
                    this.mV = -f4;
                }
                float f6 = this.mPos;
                if (f6 > 1.0f && (i3 & 2) == 2) {
                    this.mPos = 2.0f - f6;
                    this.mV = -this.mV;
                }
            }
            i++;
            sqrt = i2;
            d2 = d7;
            d = d11;
        }
    }

    public String debug(String desc, float time) {
        return null;
    }

    public float getAcceleration() {
        double d = this.mStiffness;
        double d2 = this.mDamping;
        return ((float) (((-d) * (((double) this.mPos) - this.mTargetPos)) - (((double) this.mV) * d2))) / this.mMass;
    }

    public float getInterpolation(float time) {
        compute((double) (time - this.mLastTime));
        this.mLastTime = time;
        return this.mPos;
    }

    public float getVelocity() {
        return 0.0f;
    }

    public float getVelocity(float t) {
        return this.mV;
    }

    public boolean isStopped() {
        double d = ((double) this.mPos) - this.mTargetPos;
        double d2 = this.mStiffness;
        double d3 = (double) this.mV;
        return Math.sqrt((((d3 * d3) * ((double) this.mMass)) + ((d2 * d) * d)) / d2) <= ((double) this.mStopThreshold);
    }

    /* access modifiers changed from: package-private */
    public void log(String str) {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        System.out.println((".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ") " + stackTraceElement.getMethodName() + "() ") + str);
    }

    public void springConfig(float currentPos, float target, float currentVelocity, float mass, float stiffness, float damping, float stopThreshold, int boundaryMode) {
        this.mTargetPos = (double) target;
        this.mDamping = (double) damping;
        this.mInitialized = false;
        this.mPos = currentPos;
        this.mLastVelocity = (double) currentVelocity;
        this.mStiffness = (double) stiffness;
        this.mMass = mass;
        this.mStopThreshold = stopThreshold;
        this.mBoundaryMode = boundaryMode;
        this.mLastTime = 0.0f;
    }
}

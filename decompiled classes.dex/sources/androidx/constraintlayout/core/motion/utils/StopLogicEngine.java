package androidx.constraintlayout.core.motion.utils;

public class StopLogicEngine implements StopEngine {
    private static final float EPSILON = 1.0E-5f;
    private boolean mBackwards = false;
    private boolean mDone = false;
    private float mLastPosition;
    private int mNumberOfStages;
    private float mStage1Duration;
    private float mStage1EndPosition;
    private float mStage1Velocity;
    private float mStage2Duration;
    private float mStage2EndPosition;
    private float mStage2Velocity;
    private float mStage3Duration;
    private float mStage3EndPosition;
    private float mStage3Velocity;
    private float mStartPosition;
    private String mType;

    private float calcY(float time) {
        this.mDone = false;
        float f = this.mStage1Duration;
        if (time <= f) {
            float f2 = this.mStage1Velocity;
            return (f2 * time) + ((((this.mStage2Velocity - f2) * time) * time) / (f * 2.0f));
        }
        int i = this.mNumberOfStages;
        if (i == 1) {
            return this.mStage1EndPosition;
        }
        float time2 = time - f;
        float f3 = this.mStage2Duration;
        if (time2 < f3) {
            float f4 = this.mStage1EndPosition;
            float f5 = this.mStage2Velocity;
            return f4 + (f5 * time2) + ((((this.mStage3Velocity - f5) * time2) * time2) / (f3 * 2.0f));
        } else if (i == 2) {
            return this.mStage2EndPosition;
        } else {
            float time3 = time2 - f3;
            float f6 = this.mStage3Duration;
            if (time3 <= f6) {
                float f7 = this.mStage2EndPosition;
                float f8 = this.mStage3Velocity;
                return (f7 + (f8 * time3)) - (((f8 * time3) * time3) / (f6 * 2.0f));
            }
            this.mDone = true;
            return this.mStage3EndPosition;
        }
    }

    private void setup(float velocity, float distance, float maxAcceleration, float maxVelocity, float maxTime) {
        float f = distance;
        float f2 = maxVelocity;
        this.mDone = false;
        float f3 = velocity == 0.0f ? 1.0E-4f : velocity;
        this.mStage1Velocity = f3;
        float f4 = f3 / maxAcceleration;
        float f5 = (f4 * f3) / 2.0f;
        if (f3 < 0.0f) {
            float sqrt = (float) Math.sqrt((double) (maxAcceleration * (f - ((((-f3) / maxAcceleration) * f3) / 2.0f))));
            if (sqrt < f2) {
                this.mType = "backward accelerate, decelerate";
                this.mNumberOfStages = 2;
                this.mStage1Velocity = f3;
                this.mStage2Velocity = sqrt;
                this.mStage3Velocity = 0.0f;
                float f6 = (sqrt - f3) / maxAcceleration;
                this.mStage1Duration = f6;
                this.mStage2Duration = sqrt / maxAcceleration;
                this.mStage1EndPosition = ((f3 + sqrt) * f6) / 2.0f;
                this.mStage2EndPosition = f;
                this.mStage3EndPosition = f;
                return;
            }
            this.mType = "backward accelerate cruse decelerate";
            this.mNumberOfStages = 3;
            this.mStage1Velocity = f3;
            this.mStage2Velocity = f2;
            this.mStage3Velocity = f2;
            float f7 = (f2 - f3) / maxAcceleration;
            this.mStage1Duration = f7;
            float f8 = f2 / maxAcceleration;
            this.mStage3Duration = f8;
            float f9 = ((f3 + f2) * f7) / 2.0f;
            float f10 = (f2 * f8) / 2.0f;
            this.mStage2Duration = ((f - f9) - f10) / f2;
            this.mStage1EndPosition = f9;
            this.mStage2EndPosition = f - f10;
            this.mStage3EndPosition = f;
        } else if (f5 >= f) {
            this.mType = "hard stop";
            this.mNumberOfStages = 1;
            this.mStage1Velocity = f3;
            this.mStage2Velocity = 0.0f;
            this.mStage1EndPosition = f;
            this.mStage1Duration = (2.0f * f) / f3;
        } else {
            float f11 = f - f5;
            float f12 = f11 / f3;
            if (f12 + f4 < maxTime) {
                this.mType = "cruse decelerate";
                this.mNumberOfStages = 2;
                this.mStage1Velocity = f3;
                this.mStage2Velocity = f3;
                this.mStage3Velocity = 0.0f;
                this.mStage1EndPosition = f11;
                this.mStage2EndPosition = f;
                this.mStage1Duration = f12;
                this.mStage2Duration = f3 / maxAcceleration;
                return;
            }
            float sqrt2 = (float) Math.sqrt((double) ((maxAcceleration * f) + ((f3 * f3) / 2.0f)));
            this.mStage1Duration = (sqrt2 - f3) / maxAcceleration;
            this.mStage2Duration = sqrt2 / maxAcceleration;
            if (sqrt2 < f2) {
                this.mType = "accelerate decelerate";
                this.mNumberOfStages = 2;
                this.mStage1Velocity = f3;
                this.mStage2Velocity = sqrt2;
                this.mStage3Velocity = 0.0f;
                float f13 = (sqrt2 - f3) / maxAcceleration;
                this.mStage1Duration = f13;
                this.mStage2Duration = sqrt2 / maxAcceleration;
                this.mStage1EndPosition = ((f3 + sqrt2) * f13) / 2.0f;
                this.mStage2EndPosition = f;
                return;
            }
            this.mType = "accelerate cruse decelerate";
            this.mNumberOfStages = 3;
            this.mStage1Velocity = f3;
            this.mStage2Velocity = f2;
            this.mStage3Velocity = f2;
            float f14 = (f2 - f3) / maxAcceleration;
            this.mStage1Duration = f14;
            float f15 = f2 / maxAcceleration;
            this.mStage3Duration = f15;
            float f16 = ((f3 + f2) * f14) / 2.0f;
            float f17 = (f2 * f15) / 2.0f;
            this.mStage2Duration = ((f - f16) - f17) / f2;
            this.mStage1EndPosition = f16;
            this.mStage2EndPosition = f - f17;
            this.mStage3EndPosition = f;
        }
    }

    public void config(float currentPos, float destination, float currentVelocity, float maxTime, float maxAcceleration, float maxVelocity) {
        boolean z = false;
        this.mDone = false;
        this.mStartPosition = currentPos;
        if (currentPos > destination) {
            z = true;
        }
        this.mBackwards = z;
        if (z) {
            setup(-currentVelocity, currentPos - destination, maxAcceleration, maxVelocity, maxTime);
            return;
        }
        setup(currentVelocity, destination - currentPos, maxAcceleration, maxVelocity, maxTime);
    }

    public String debug(String desc, float time) {
        String str = ((desc + " ===== " + this.mType + "\n") + desc + (this.mBackwards ? "backwards" : "forward ") + " time = " + time + "  stages " + this.mNumberOfStages + "\n") + desc + " dur " + this.mStage1Duration + " vel " + this.mStage1Velocity + " pos " + this.mStage1EndPosition + "\n";
        if (this.mNumberOfStages > 1) {
            str = str + desc + " dur " + this.mStage2Duration + " vel " + this.mStage2Velocity + " pos " + this.mStage2EndPosition + "\n";
        }
        if (this.mNumberOfStages > 2) {
            str = str + desc + " dur " + this.mStage3Duration + " vel " + this.mStage3Velocity + " pos " + this.mStage3EndPosition + "\n";
        }
        float f = this.mStage1Duration;
        if (time <= f) {
            return str + desc + "stage 0\n";
        }
        int i = this.mNumberOfStages;
        if (i == 1) {
            return str + desc + "end stage 0\n";
        }
        float time2 = time - f;
        float f2 = this.mStage2Duration;
        return time2 < f2 ? str + desc + " stage 1\n" : i == 2 ? str + desc + "end stage 1\n" : time2 - f2 < this.mStage3Duration ? str + desc + " stage 2\n" : str + desc + " end stage 2\n";
    }

    public float getInterpolation(float v) {
        float calcY = calcY(v);
        this.mLastPosition = v;
        return this.mBackwards ? this.mStartPosition - calcY : this.mStartPosition + calcY;
    }

    public float getVelocity() {
        return this.mBackwards ? -getVelocity(this.mLastPosition) : getVelocity(this.mLastPosition);
    }

    public float getVelocity(float x) {
        float f = this.mStage1Duration;
        if (x <= f) {
            float f2 = this.mStage1Velocity;
            return f2 + (((this.mStage2Velocity - f2) * x) / f);
        }
        int i = this.mNumberOfStages;
        if (i == 1) {
            return 0.0f;
        }
        float x2 = x - f;
        float f3 = this.mStage2Duration;
        if (x2 < f3) {
            float f4 = this.mStage2Velocity;
            return f4 + (((this.mStage3Velocity - f4) * x2) / f3);
        } else if (i == 2) {
            return this.mStage2EndPosition;
        } else {
            float x3 = x2 - f3;
            float f5 = this.mStage3Duration;
            if (x3 >= f5) {
                return this.mStage3EndPosition;
            }
            float f6 = this.mStage3Velocity;
            return f6 - ((f6 * x3) / f5);
        }
    }

    public boolean isStopped() {
        return getVelocity() < EPSILON && Math.abs(this.mStage3EndPosition - this.mLastPosition) < EPSILON;
    }
}

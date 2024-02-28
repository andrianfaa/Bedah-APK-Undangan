package androidx.constraintlayout.motion.utils;

import androidx.constraintlayout.core.motion.utils.SpringStopEngine;
import androidx.constraintlayout.core.motion.utils.StopEngine;
import androidx.constraintlayout.core.motion.utils.StopLogicEngine;
import androidx.constraintlayout.motion.widget.MotionInterpolator;

public class StopLogic extends MotionInterpolator {
    private StopEngine mEngine;
    private SpringStopEngine mSpringStopEngine;
    private StopLogicEngine mStopLogicEngine;

    public StopLogic() {
        StopLogicEngine stopLogicEngine = new StopLogicEngine();
        this.mStopLogicEngine = stopLogicEngine;
        this.mEngine = stopLogicEngine;
    }

    public void config(float currentPos, float destination, float currentVelocity, float maxTime, float maxAcceleration, float maxVelocity) {
        StopLogicEngine stopLogicEngine = this.mStopLogicEngine;
        this.mEngine = stopLogicEngine;
        stopLogicEngine.config(currentPos, destination, currentVelocity, maxTime, maxAcceleration, maxVelocity);
    }

    public String debug(String desc, float time) {
        return this.mEngine.debug(desc, time);
    }

    public float getInterpolation(float v) {
        return this.mEngine.getInterpolation(v);
    }

    public float getVelocity() {
        return this.mEngine.getVelocity();
    }

    public float getVelocity(float x) {
        return this.mEngine.getVelocity(x);
    }

    public boolean isStopped() {
        return this.mEngine.isStopped();
    }

    public void springConfig(float currentPos, float destination, float currentVelocity, float mass, float stiffness, float damping, float stopThreshold, int boundaryMode) {
        if (this.mSpringStopEngine == null) {
            this.mSpringStopEngine = new SpringStopEngine();
        }
        SpringStopEngine springStopEngine = this.mSpringStopEngine;
        this.mEngine = springStopEngine;
        springStopEngine.springConfig(currentPos, destination, currentVelocity, mass, stiffness, damping, stopThreshold, boundaryMode);
    }
}

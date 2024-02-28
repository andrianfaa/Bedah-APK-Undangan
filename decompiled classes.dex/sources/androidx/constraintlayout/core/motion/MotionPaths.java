package androidx.constraintlayout.core.motion;

import androidx.constraintlayout.core.motion.key.MotionKeyPosition;
import androidx.constraintlayout.core.motion.utils.Easing;
import java.util.Arrays;
import java.util.HashMap;

public class MotionPaths implements Comparable<MotionPaths> {
    public static final int CARTESIAN = 0;
    public static final boolean DEBUG = false;
    static final int OFF_HEIGHT = 4;
    static final int OFF_PATH_ROTATE = 5;
    static final int OFF_POSITION = 0;
    static final int OFF_WIDTH = 3;
    static final int OFF_X = 1;
    static final int OFF_Y = 2;
    public static final boolean OLD_WAY = false;
    public static final int PERPENDICULAR = 1;
    public static final int SCREEN = 2;
    public static final String TAG = "MotionPaths";
    static String[] names = {"position", "x", "y", "width", "height", "pathRotate"};
    HashMap<String, CustomVariable> customAttributes = new HashMap<>();
    float height;
    int mAnimateCircleAngleTo;
    int mAnimateRelativeTo = -1;
    int mDrawPath = 0;
    Easing mKeyFrameEasing;
    int mMode = 0;
    int mPathMotionArc = -1;
    float mPathRotate = Float.NaN;
    float mProgress = Float.NaN;
    float mRelativeAngle = Float.NaN;
    Motion mRelativeToController = null;
    double[] mTempDelta = new double[18];
    double[] mTempValue = new double[18];
    float position;
    float time;
    float width;
    float x;
    float y;

    public MotionPaths() {
    }

    public MotionPaths(int parentWidth, int parentHeight, MotionKeyPosition c, MotionPaths startTimePoint, MotionPaths endTimePoint) {
        if (startTimePoint.mAnimateRelativeTo != -1) {
            initPolar(parentWidth, parentHeight, c, startTimePoint, endTimePoint);
            return;
        }
        switch (c.mPositionType) {
            case 1:
                initPath(c, startTimePoint, endTimePoint);
                return;
            case 2:
                initScreen(parentWidth, parentHeight, c, startTimePoint, endTimePoint);
                return;
            default:
                initCartesian(c, startTimePoint, endTimePoint);
                return;
        }
    }

    private boolean diff(float a, float b) {
        return (Float.isNaN(a) || Float.isNaN(b)) ? Float.isNaN(a) != Float.isNaN(b) : Math.abs(a - b) > 1.0E-6f;
    }

    private static final float xRotate(float sin, float cos, float cx, float cy, float x2, float y2) {
        return (((x2 - cx) * cos) - ((y2 - cy) * sin)) + cx;
    }

    private static final float yRotate(float sin, float cos, float cx, float cy, float x2, float y2) {
        return ((x2 - cx) * sin) + ((y2 - cy) * cos) + cy;
    }

    public void applyParameters(MotionWidget c) {
        this.mKeyFrameEasing = Easing.getInterpolator(c.motion.mTransitionEasing);
        this.mPathMotionArc = c.motion.mPathMotionArc;
        this.mAnimateRelativeTo = c.motion.mAnimateRelativeTo;
        this.mPathRotate = c.motion.mPathRotate;
        this.mDrawPath = c.motion.mDrawPath;
        this.mAnimateCircleAngleTo = c.motion.mAnimateCircleAngleTo;
        this.mProgress = c.propertySet.mProgress;
        this.mRelativeAngle = 0.0f;
        for (String next : c.getCustomAttributeNames()) {
            CustomVariable customAttribute = c.getCustomAttribute(next);
            if (customAttribute != null && customAttribute.isContinuous()) {
                this.customAttributes.put(next, customAttribute);
            }
        }
    }

    public int compareTo(MotionPaths o) {
        return Float.compare(this.position, o.position);
    }

    public void configureRelativeTo(Motion toOrbit) {
        double[] pos = toOrbit.getPos((double) this.mProgress);
    }

    /* access modifiers changed from: package-private */
    public void different(MotionPaths points, boolean[] mask, String[] custom, boolean arcMode) {
        boolean diff = diff(this.x, points.x);
        boolean diff2 = diff(this.y, points.y);
        int i = 0 + 1;
        mask[0] = mask[0] | diff(this.position, points.position);
        int i2 = i + 1;
        mask[i] = mask[i] | diff | diff2 | arcMode;
        int i3 = i2 + 1;
        mask[i2] = mask[i2] | diff | diff2 | arcMode;
        int i4 = i3 + 1;
        mask[i3] = mask[i3] | diff(this.width, points.width);
        int i5 = i4 + 1;
        mask[i4] = mask[i4] | diff(this.height, points.height);
    }

    /* access modifiers changed from: package-private */
    public void fillStandard(double[] data, int[] toUse) {
        float[] fArr = {this.position, this.x, this.y, this.width, this.height, this.mPathRotate};
        int i = 0;
        for (int i2 = 0; i2 < toUse.length; i2++) {
            if (toUse[i2] < fArr.length) {
                data[i] = (double) fArr[toUse[i2]];
                i++;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void getBounds(int[] toUse, double[] data, float[] point, int offset) {
        float f = this.x;
        float f2 = this.y;
        float f3 = this.width;
        float f4 = this.height;
        for (int i = 0; i < toUse.length; i++) {
            float f5 = (float) data[i];
            switch (toUse[i]) {
                case 1:
                    float f6 = f5;
                    break;
                case 2:
                    float f7 = f5;
                    break;
                case 3:
                    f3 = f5;
                    break;
                case 4:
                    f4 = f5;
                    break;
            }
        }
        point[offset] = f3;
        point[offset + 1] = f4;
    }

    /* access modifiers changed from: package-private */
    public void getCenter(double p, int[] toUse, double[] data, float[] point, int offset) {
        float f;
        int[] iArr = toUse;
        float f2 = this.x;
        float f3 = this.y;
        float f4 = this.width;
        float f5 = this.height;
        for (int i = 0; i < iArr.length; i++) {
            float f6 = (float) data[i];
            switch (iArr[i]) {
                case 1:
                    f2 = f6;
                    break;
                case 2:
                    f3 = f6;
                    break;
                case 3:
                    f4 = f6;
                    break;
                case 4:
                    f5 = f6;
                    break;
            }
        }
        Motion motion = this.mRelativeToController;
        if (motion != null) {
            float[] fArr = new float[2];
            float[] fArr2 = new float[2];
            motion.getCenter(p, fArr, fArr2);
            float f7 = fArr[0];
            float f8 = fArr[1];
            float f9 = f2;
            float[] fArr3 = fArr2;
            float f10 = f2;
            float f11 = f3;
            float[] fArr4 = fArr;
            f = 2.0f;
            f3 = (float) ((((double) f8) - (((double) f9) * Math.cos((double) f11))) - ((double) (f5 / 2.0f)));
            f2 = (float) ((((double) f7) + (((double) f9) * Math.sin((double) f11))) - ((double) (f4 / 2.0f)));
        } else {
            float f12 = f2;
            f = 2.0f;
        }
        point[offset] = (f4 / f) + f2 + 0.0f;
        point[offset + 1] = (f5 / f) + f3 + 0.0f;
    }

    /* access modifiers changed from: package-private */
    public void getCenter(double p, int[] toUse, double[] data, float[] point, double[] vdata, float[] velocity) {
        int[] iArr = toUse;
        float f = this.x;
        float f2 = this.y;
        float f3 = this.width;
        float f4 = this.height;
        float f5 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        float f8 = 0.0f;
        for (int i = 0; i < iArr.length; i++) {
            float f9 = (float) data[i];
            float f10 = (float) vdata[i];
            switch (iArr[i]) {
                case 1:
                    f = f9;
                    f5 = f10;
                    break;
                case 2:
                    f2 = f9;
                    f6 = f10;
                    break;
                case 3:
                    f3 = f9;
                    f7 = f10;
                    break;
                case 4:
                    f4 = f9;
                    f8 = f10;
                    break;
            }
        }
        float f11 = (f7 / 2.0f) + f5;
        float f12 = (f8 / 2.0f) + f6;
        Motion motion = this.mRelativeToController;
        if (motion != null) {
            float[] fArr = new float[2];
            float[] fArr2 = new float[2];
            float f13 = f7;
            float f14 = f8;
            motion.getCenter(p, fArr, fArr2);
            float f15 = fArr[0];
            float f16 = fArr[1];
            float f17 = f;
            float f18 = f2;
            float f19 = f5;
            float f20 = f6;
            float[] fArr3 = fArr;
            float f21 = fArr2[0];
            float f22 = f;
            float f23 = fArr2[1];
            float f24 = f5;
            float f25 = f6;
            float f26 = f2;
            float f27 = f17;
            float[] fArr4 = fArr2;
            float f28 = f12;
            float f29 = f18;
            float f30 = f11;
            float sin = (float) ((((double) f15) + (((double) f27) * Math.sin((double) f29))) - ((double) (f3 / 2.0f)));
            float cos = (float) ((((double) f16) - (((double) f27) * Math.cos((double) f29))) - ((double) (f4 / 2.0f)));
            float f31 = f19;
            float f32 = f21;
            float f33 = f16;
            float f34 = f20;
            float f35 = f15;
            f12 = (float) ((((double) f23) - (((double) f31) * Math.cos((double) f29))) + (Math.sin((double) f29) * ((double) f34)));
            f2 = cos;
            f11 = (float) (((double) f21) + (((double) f31) * Math.sin((double) f29)) + (Math.cos((double) f29) * ((double) f34)));
            f = sin;
        } else {
            float f36 = f;
            float f37 = f2;
            float f38 = f5;
            float f39 = f6;
            float f40 = f7;
            float f41 = f8;
            float f42 = f11;
            float f43 = f12;
        }
        point[0] = (f3 / 2.0f) + f + 0.0f;
        point[1] = (f4 / 2.0f) + f2 + 0.0f;
        velocity[0] = f11;
        velocity[1] = f12;
    }

    /* access modifiers changed from: package-private */
    public void getCenterVelocity(double p, int[] toUse, double[] data, float[] point, int offset) {
        float f;
        int[] iArr = toUse;
        float f2 = this.x;
        float f3 = this.y;
        float f4 = this.width;
        float f5 = this.height;
        for (int i = 0; i < iArr.length; i++) {
            float f6 = (float) data[i];
            switch (iArr[i]) {
                case 1:
                    f2 = f6;
                    break;
                case 2:
                    f3 = f6;
                    break;
                case 3:
                    f4 = f6;
                    break;
                case 4:
                    f5 = f6;
                    break;
            }
        }
        Motion motion = this.mRelativeToController;
        if (motion != null) {
            float[] fArr = new float[2];
            float[] fArr2 = new float[2];
            motion.getCenter(p, fArr, fArr2);
            float f7 = fArr[0];
            float f8 = fArr[1];
            float f9 = f2;
            float[] fArr3 = fArr2;
            float f10 = f2;
            float f11 = f3;
            float[] fArr4 = fArr;
            f = 2.0f;
            f3 = (float) ((((double) f8) - (((double) f9) * Math.cos((double) f11))) - ((double) (f5 / 2.0f)));
            f2 = (float) ((((double) f7) + (((double) f9) * Math.sin((double) f11))) - ((double) (f4 / 2.0f)));
        } else {
            float f12 = f2;
            f = 2.0f;
        }
        point[offset] = (f4 / f) + f2 + 0.0f;
        point[offset + 1] = (f5 / f) + f3 + 0.0f;
    }

    /* access modifiers changed from: package-private */
    public int getCustomData(String name, double[] value, int offset) {
        CustomVariable customVariable = this.customAttributes.get(name);
        if (customVariable == null) {
            return 0;
        }
        if (customVariable.numberOfInterpolatedValues() == 1) {
            value[offset] = (double) customVariable.getValueToInterpolate();
            return 1;
        }
        int numberOfInterpolatedValues = customVariable.numberOfInterpolatedValues();
        float[] fArr = new float[numberOfInterpolatedValues];
        customVariable.getValuesToInterpolate(fArr);
        int i = 0;
        while (i < numberOfInterpolatedValues) {
            value[offset] = (double) fArr[i];
            i++;
            offset++;
        }
        return numberOfInterpolatedValues;
    }

    /* access modifiers changed from: package-private */
    public int getCustomDataCount(String name) {
        CustomVariable customVariable = this.customAttributes.get(name);
        if (customVariable == null) {
            return 0;
        }
        return customVariable.numberOfInterpolatedValues();
    }

    /* access modifiers changed from: package-private */
    public void getRect(int[] toUse, double[] data, float[] path, int offset) {
        float f;
        int[] iArr = toUse;
        float f2 = this.x;
        float f3 = this.y;
        float f4 = this.width;
        float f5 = this.height;
        boolean z = false;
        boolean z2 = false;
        int i = 0;
        while (true) {
            boolean z3 = z;
            if (i < iArr.length) {
                boolean z4 = z2;
                float f6 = (float) data[i];
                switch (iArr[i]) {
                    case 0:
                        float f7 = f6;
                        break;
                    case 1:
                        f2 = f6;
                        break;
                    case 2:
                        f3 = f6;
                        break;
                    case 3:
                        f4 = f6;
                        break;
                    case 4:
                        f5 = f6;
                        break;
                }
                i++;
                z = z3;
                z2 = z4;
            } else {
                boolean z5 = z2;
                Motion motion = this.mRelativeToController;
                if (motion != null) {
                    float centerX = motion.getCenterX();
                    float f8 = f3;
                    float f9 = f2;
                    float f10 = f3;
                    float f11 = centerX;
                    f = 0.0f;
                    float sin = (float) ((((double) centerX) + (((double) f9) * Math.sin((double) f3))) - ((double) (f4 / 2.0f)));
                    f3 = (float) ((((double) this.mRelativeToController.getCenterY()) - (((double) f9) * Math.cos((double) f3))) - ((double) (f5 / 2.0f)));
                    f2 = sin;
                } else {
                    float f12 = f2;
                    float f13 = f3;
                    f = 0.0f;
                }
                float f14 = f2;
                float f15 = f3;
                float f16 = f2 + f4;
                float f17 = f15;
                float f18 = f16;
                float f19 = f3 + f5;
                float f20 = f14;
                float f21 = f19;
                float f22 = f14 + (f4 / 2.0f);
                float f23 = f15 + (f5 / 2.0f);
                if (!Float.isNaN(Float.NaN)) {
                    f22 = f14 + ((f16 - f14) * Float.NaN);
                }
                if (!Float.isNaN(Float.NaN)) {
                    f23 = f15 + ((f19 - f15) * Float.NaN);
                }
                if (1.0f != 1.0f) {
                    float f24 = (f14 + f16) / 2.0f;
                    f14 = ((f14 - f24) * 1.0f) + f24;
                    f16 = ((f16 - f24) * 1.0f) + f24;
                    f18 = ((f18 - f24) * 1.0f) + f24;
                    f20 = ((f20 - f24) * 1.0f) + f24;
                }
                if (1.0f != 1.0f) {
                    float f25 = (f15 + f19) / 2.0f;
                    f15 = ((f15 - f25) * 1.0f) + f25;
                    f17 = ((f17 - f25) * 1.0f) + f25;
                    f19 = ((f19 - f25) * 1.0f) + f25;
                    f21 = ((f21 - f25) * 1.0f) + f25;
                }
                if (f != 0.0f) {
                    float f26 = f2;
                    float f27 = f3;
                    float f28 = f;
                    float f29 = f4;
                    float f30 = f5;
                    float sin2 = (float) Math.sin(Math.toRadians((double) f28));
                    float cos = (float) Math.cos(Math.toRadians((double) f28));
                    float f31 = f22;
                    float f32 = f23;
                    float f33 = f14;
                    float f34 = f15;
                    float xRotate = xRotate(sin2, cos, f31, f32, f33, f34);
                    float yRotate = yRotate(sin2, cos, f31, f32, f33, f34);
                    float f35 = f16;
                    float f36 = f17;
                    float xRotate2 = xRotate(sin2, cos, f31, f32, f35, f36);
                    float yRotate2 = yRotate(sin2, cos, f31, f32, f35, f36);
                    float f37 = f18;
                    float f38 = f19;
                    float xRotate3 = xRotate(sin2, cos, f31, f32, f37, f38);
                    float yRotate3 = yRotate(sin2, cos, f31, f32, f37, f38);
                    float f39 = f20;
                    float f40 = f21;
                    f14 = xRotate;
                    f15 = yRotate;
                    f16 = xRotate2;
                    f17 = yRotate2;
                    f18 = xRotate3;
                    f19 = yRotate3;
                    f20 = xRotate(sin2, cos, f31, f32, f39, f40);
                    f21 = yRotate(sin2, cos, f31, f32, f39, f40);
                } else {
                    float f41 = f2;
                    float f42 = f3;
                    float f43 = f5;
                    float f44 = f;
                    float f45 = f4;
                }
                int i2 = offset + 1;
                path[offset] = f14 + 0.0f;
                int i3 = i2 + 1;
                path[i2] = f15 + 0.0f;
                int i4 = i3 + 1;
                path[i3] = f16 + 0.0f;
                int i5 = i4 + 1;
                path[i4] = f17 + 0.0f;
                int i6 = i5 + 1;
                path[i5] = f18 + 0.0f;
                int i7 = i6 + 1;
                path[i6] = f19 + 0.0f;
                int i8 = i7 + 1;
                path[i7] = f20 + 0.0f;
                int i9 = i8 + 1;
                path[i8] = f21 + 0.0f;
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean hasCustomData(String name) {
        return this.customAttributes.containsKey(name);
    }

    /* access modifiers changed from: package-private */
    public void initCartesian(MotionKeyPosition c, MotionPaths startTimePoint, MotionPaths endTimePoint) {
        MotionKeyPosition motionKeyPosition = c;
        MotionPaths motionPaths = startTimePoint;
        MotionPaths motionPaths2 = endTimePoint;
        float f = ((float) motionKeyPosition.mFramePosition) / 100.0f;
        this.time = f;
        this.mDrawPath = motionKeyPosition.mDrawPath;
        float f2 = Float.isNaN(motionKeyPosition.mPercentWidth) ? f : motionKeyPosition.mPercentWidth;
        float f3 = Float.isNaN(motionKeyPosition.mPercentHeight) ? f : motionKeyPosition.mPercentHeight;
        float f4 = motionPaths2.width;
        float f5 = motionPaths.width;
        float f6 = f4 - f5;
        float f7 = motionPaths2.height;
        float f8 = motionPaths.height;
        float f9 = f7 - f8;
        this.position = this.time;
        float f10 = f;
        float f11 = motionPaths.x;
        float f12 = f;
        float f13 = motionPaths.y;
        float f14 = motionPaths2.x + (f4 / 2.0f);
        float f15 = motionPaths2.y + (f7 / 2.0f);
        float f16 = f14 - (f11 + (f5 / 2.0f));
        float f17 = f15 - (f13 + (f8 / 2.0f));
        this.x = (float) ((int) ((f11 + (f16 * f10)) - ((f6 * f2) / 2.0f)));
        this.y = (float) ((int) ((f13 + (f17 * f10)) - ((f9 * f3) / 2.0f)));
        this.width = (float) ((int) (f5 + (f6 * f2)));
        this.height = (float) ((int) (f8 + (f9 * f3)));
        float f18 = Float.isNaN(motionKeyPosition.mPercentX) ? f12 : motionKeyPosition.mPercentX;
        float f19 = Float.isNaN(motionKeyPosition.mAltPercentY) ? 0.0f : motionKeyPosition.mAltPercentY;
        float f20 = Float.isNaN(motionKeyPosition.mPercentY) ? f12 : motionKeyPosition.mPercentY;
        float f21 = Float.isNaN(motionKeyPosition.mAltPercentX) ? 0.0f : motionKeyPosition.mAltPercentX;
        this.mMode = 0;
        this.x = (float) ((int) (((motionPaths.x + (f16 * f18)) + (f17 * f21)) - ((f6 * f2) / 2.0f)));
        this.y = (float) ((int) (((motionPaths.y + (f16 * f19)) + (f17 * f20)) - ((f9 * f3) / 2.0f)));
        this.mKeyFrameEasing = Easing.getInterpolator(motionKeyPosition.mTransitionEasing);
        this.mPathMotionArc = motionKeyPosition.mPathMotionArc;
    }

    /* access modifiers changed from: package-private */
    public void initPath(MotionKeyPosition c, MotionPaths startTimePoint, MotionPaths endTimePoint) {
        MotionKeyPosition motionKeyPosition = c;
        MotionPaths motionPaths = startTimePoint;
        MotionPaths motionPaths2 = endTimePoint;
        float f = ((float) motionKeyPosition.mFramePosition) / 100.0f;
        this.time = f;
        this.mDrawPath = motionKeyPosition.mDrawPath;
        float f2 = Float.isNaN(motionKeyPosition.mPercentWidth) ? f : motionKeyPosition.mPercentWidth;
        float f3 = Float.isNaN(motionKeyPosition.mPercentHeight) ? f : motionKeyPosition.mPercentHeight;
        float f4 = motionPaths2.width - motionPaths.width;
        float f5 = motionPaths2.height - motionPaths.height;
        this.position = this.time;
        float f6 = Float.isNaN(motionKeyPosition.mPercentX) ? f : motionKeyPosition.mPercentX;
        float f7 = motionPaths.x;
        float f8 = motionPaths.width;
        float f9 = motionPaths.y;
        float f10 = f;
        float f11 = motionPaths.height;
        float f12 = motionPaths2.x + (motionPaths2.width / 2.0f);
        float f13 = motionPaths2.y + (motionPaths2.height / 2.0f);
        float f14 = f12 - ((f8 / 2.0f) + f7);
        float f15 = f13 - (f9 + (f11 / 2.0f));
        this.x = (float) ((int) ((f7 + (f14 * f6)) - ((f4 * f2) / 2.0f)));
        this.y = (float) ((int) ((f9 + (f15 * f6)) - ((f5 * f3) / 2.0f)));
        this.width = (float) ((int) (f8 + (f4 * f2)));
        this.height = (float) ((int) (f11 + (f5 * f3)));
        MotionKeyPosition motionKeyPosition2 = c;
        float f16 = Float.isNaN(motionKeyPosition2.mPercentY) ? 0.0f : motionKeyPosition2.mPercentY;
        float f17 = f12;
        this.mMode = 1;
        MotionPaths motionPaths3 = startTimePoint;
        float f18 = f13;
        float f19 = (float) ((int) ((motionPaths3.x + (f14 * f6)) - ((f4 * f2) / 2.0f)));
        this.x = f19;
        float f20 = f14;
        float f21 = (float) ((int) ((motionPaths3.y + (f15 * f6)) - ((f5 * f3) / 2.0f)));
        this.y = f21;
        this.x = f19 + ((-f15) * f16);
        this.y = f21 + (f14 * f16);
        this.mAnimateRelativeTo = this.mAnimateRelativeTo;
        this.mKeyFrameEasing = Easing.getInterpolator(motionKeyPosition2.mTransitionEasing);
        this.mPathMotionArc = motionKeyPosition2.mPathMotionArc;
    }

    /* access modifiers changed from: package-private */
    public void initPolar(int parentWidth, int parentHeight, MotionKeyPosition c, MotionPaths s, MotionPaths e) {
        float f;
        float f2;
        float f3 = ((float) c.mFramePosition) / 100.0f;
        this.time = f3;
        this.mDrawPath = c.mDrawPath;
        this.mMode = c.mPositionType;
        float f4 = Float.isNaN(c.mPercentWidth) ? f3 : c.mPercentWidth;
        float f5 = Float.isNaN(c.mPercentHeight) ? f3 : c.mPercentHeight;
        float f6 = e.width;
        float f7 = s.width;
        float f8 = e.height;
        float f9 = s.height;
        this.position = this.time;
        this.width = (float) ((int) (f7 + ((f6 - f7) * f4)));
        this.height = (float) ((int) (f9 + ((f8 - f9) * f5)));
        float f10 = 1.0f - f3;
        float f11 = f3;
        switch (c.mPositionType) {
            case 1:
                float f12 = Float.isNaN(c.mPercentX) ? f3 : c.mPercentX;
                float f13 = e.x;
                float f14 = s.x;
                this.x = (f12 * (f13 - f14)) + f14;
                float f15 = Float.isNaN(c.mPercentY) ? f3 : c.mPercentY;
                float f16 = e.y;
                float f17 = s.y;
                this.y = (f15 * (f16 - f17)) + f17;
                break;
            case 2:
                if (Float.isNaN(c.mPercentX)) {
                    float f18 = e.x;
                    float f19 = s.x;
                    f = ((f18 - f19) * f3) + f19;
                } else {
                    f = c.mPercentX * Math.min(f5, f4);
                }
                this.x = f;
                if (Float.isNaN(c.mPercentY)) {
                    float f20 = e.y;
                    float f21 = s.y;
                    f2 = ((f20 - f21) * f3) + f21;
                } else {
                    f2 = c.mPercentY;
                }
                this.y = f2;
                break;
            default:
                float f22 = Float.isNaN(c.mPercentX) ? f3 : c.mPercentX;
                float f23 = e.x;
                float f24 = s.x;
                this.x = (f22 * (f23 - f24)) + f24;
                float f25 = Float.isNaN(c.mPercentY) ? f3 : c.mPercentY;
                float f26 = e.y;
                float f27 = s.y;
                this.y = (f25 * (f26 - f27)) + f27;
                break;
        }
        this.mAnimateRelativeTo = s.mAnimateRelativeTo;
        this.mKeyFrameEasing = Easing.getInterpolator(c.mTransitionEasing);
        this.mPathMotionArc = c.mPathMotionArc;
    }

    /* access modifiers changed from: package-private */
    public void initScreen(int parentWidth, int parentHeight, MotionKeyPosition c, MotionPaths startTimePoint, MotionPaths endTimePoint) {
        MotionKeyPosition motionKeyPosition = c;
        MotionPaths motionPaths = startTimePoint;
        MotionPaths motionPaths2 = endTimePoint;
        float f = ((float) motionKeyPosition.mFramePosition) / 100.0f;
        this.time = f;
        this.mDrawPath = motionKeyPosition.mDrawPath;
        float f2 = Float.isNaN(motionKeyPosition.mPercentWidth) ? f : motionKeyPosition.mPercentWidth;
        float f3 = Float.isNaN(motionKeyPosition.mPercentHeight) ? f : motionKeyPosition.mPercentHeight;
        float f4 = motionPaths2.width;
        float f5 = motionPaths.width;
        float f6 = f4 - f5;
        float f7 = motionPaths2.height;
        float f8 = motionPaths.height;
        float f9 = f7 - f8;
        this.position = this.time;
        float f10 = f;
        float f11 = motionPaths.x;
        float f12 = f;
        float f13 = motionPaths.y;
        float f14 = motionPaths2.x + (f4 / 2.0f);
        float f15 = motionPaths2.y + (f7 / 2.0f);
        this.x = (float) ((int) ((f11 + ((f14 - (f11 + (f5 / 2.0f))) * f10)) - ((f6 * f2) / 2.0f)));
        this.y = (float) ((int) ((f13 + ((f15 - (f13 + (f8 / 2.0f))) * f10)) - ((f9 * f3) / 2.0f)));
        this.width = (float) ((int) (f5 + (f6 * f2)));
        this.height = (float) ((int) (f8 + (f9 * f3)));
        this.mMode = 2;
        if (!Float.isNaN(motionKeyPosition.mPercentX)) {
            this.x = (float) ((int) (motionKeyPosition.mPercentX * ((float) ((int) (((float) parentWidth) - this.width)))));
        } else {
            int i = parentWidth;
        }
        if (!Float.isNaN(motionKeyPosition.mPercentY)) {
            this.y = (float) ((int) (motionKeyPosition.mPercentY * ((float) ((int) (((float) parentHeight) - this.height)))));
        } else {
            int i2 = parentHeight;
        }
        this.mAnimateRelativeTo = this.mAnimateRelativeTo;
        this.mKeyFrameEasing = Easing.getInterpolator(motionKeyPosition.mTransitionEasing);
        this.mPathMotionArc = motionKeyPosition.mPathMotionArc;
    }

    /* access modifiers changed from: package-private */
    public void setBounds(float x2, float y2, float w, float h) {
        this.x = x2;
        this.y = y2;
        this.width = w;
        this.height = h;
    }

    /* access modifiers changed from: package-private */
    public void setDpDt(float locationX, float locationY, float[] mAnchorDpDt, int[] toUse, double[] deltaData, double[] data) {
        int[] iArr = toUse;
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        for (int i = 0; i < iArr.length; i++) {
            float f5 = (float) deltaData[i];
            float f6 = (float) data[i];
            switch (iArr[i]) {
                case 1:
                    f = f5;
                    break;
                case 2:
                    f2 = f5;
                    break;
                case 3:
                    f3 = f5;
                    break;
                case 4:
                    f4 = f5;
                    break;
            }
        }
        float f7 = f - ((0.0f * f3) / 2.0f);
        float f8 = f2 - ((0.0f * f4) / 2.0f);
        mAnchorDpDt[0] = ((1.0f - locationX) * f7) + ((f7 + ((0.0f + 1.0f) * f3)) * locationX) + 0.0f;
        mAnchorDpDt[1] = ((1.0f - locationY) * f8) + ((f8 + ((0.0f + 1.0f) * f4)) * locationY) + 0.0f;
    }

    /* access modifiers changed from: package-private */
    public void setView(float position2, MotionWidget view, int[] toUse, double[] data, double[] slope, double[] cycle) {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        MotionWidget motionWidget = view;
        int[] iArr = toUse;
        double[] dArr = slope;
        float f8 = this.x;
        float f9 = this.y;
        float f10 = this.width;
        float f11 = this.height;
        float f12 = 0.0f;
        float f13 = 0.0f;
        float f14 = 0.0f;
        float f15 = 0.0f;
        float f16 = 0.0f;
        float f17 = Float.NaN;
        if (iArr.length != 0) {
            f = f8;
            if (this.mTempValue.length <= iArr[iArr.length - 1]) {
                int i = iArr[iArr.length - 1] + 1;
                this.mTempValue = new double[i];
                this.mTempDelta = new double[i];
            }
        } else {
            f = f8;
        }
        float f18 = f9;
        float f19 = f10;
        Arrays.fill(this.mTempValue, Double.NaN);
        for (int i2 = 0; i2 < iArr.length; i2++) {
            this.mTempValue[iArr[i2]] = data[i2];
            this.mTempDelta[iArr[i2]] = dArr[i2];
        }
        int i3 = 0;
        float f20 = f18;
        float f21 = f19;
        while (true) {
            double[] dArr2 = this.mTempValue;
            if (i3 < dArr2.length) {
                double d = 0.0d;
                if (Double.isNaN(dArr2[i3])) {
                    if (cycle == null) {
                        f7 = f15;
                        f6 = f16;
                    } else if (cycle[i3] == 0.0d) {
                        f7 = f15;
                        f6 = f16;
                    }
                    f15 = f7;
                    f16 = f6;
                    i3++;
                    int[] iArr2 = toUse;
                }
                if (cycle != null) {
                    d = cycle[i3];
                }
                double d2 = d;
                if (Double.isNaN(this.mTempValue[i3])) {
                    double d3 = d2;
                } else {
                    double d4 = d2;
                    d2 = this.mTempValue[i3] + d2;
                }
                float f22 = (float) d2;
                f7 = f15;
                f6 = f16;
                f15 = (float) this.mTempDelta[i3];
                switch (i3) {
                    case 0:
                        f16 = f22;
                        f15 = f7;
                        continue;
                    case 1:
                        f12 = f15;
                        f = f22;
                        f15 = f7;
                        f16 = f6;
                        continue;
                    case 2:
                        f20 = f22;
                        f13 = f15;
                        f15 = f7;
                        f16 = f6;
                        continue;
                    case 3:
                        f21 = f22;
                        f14 = f15;
                        f15 = f7;
                        f16 = f6;
                        continue;
                    case 4:
                        f11 = f22;
                        float f23 = f15;
                        f16 = f6;
                        continue;
                    case 5:
                        f17 = f22;
                        f15 = f7;
                        f16 = f6;
                        continue;
                }
                f15 = f7;
                f16 = f6;
                i3++;
                int[] iArr22 = toUse;
            } else {
                float f24 = f15;
                float f25 = f16;
                Motion motion = this.mRelativeToController;
                if (motion != null) {
                    float[] fArr = new float[2];
                    float[] fArr2 = new float[2];
                    float f26 = f20;
                    motion.getCenter((double) position2, fArr, fArr2);
                    float f27 = fArr[0];
                    float f28 = fArr[1];
                    float f29 = f;
                    float f30 = f12;
                    float f31 = f13;
                    float f32 = fArr2[0];
                    float f33 = fArr2[1];
                    float[] fArr3 = fArr2;
                    float f34 = f24;
                    double d5 = (double) f27;
                    float f35 = f12;
                    float f36 = f13;
                    float f37 = f27;
                    float[] fArr4 = fArr;
                    float f38 = f26;
                    float f39 = f17;
                    float sin = (float) ((d5 + (((double) f29) * Math.sin((double) f38))) - ((double) (f21 / 2.0f)));
                    float cos = (float) ((((double) f28) - (((double) f29) * Math.cos((double) f38))) - ((double) (f11 / 2.0f)));
                    float f40 = f30;
                    f2 = f21;
                    f3 = f11;
                    float f41 = f32;
                    float f42 = f31;
                    float sin2 = (float) (((double) f32) + (((double) f40) * Math.sin((double) f38)) + (((double) f29) * Math.cos((double) f38) * ((double) f42)));
                    float f43 = f28;
                    float cos2 = (float) ((((double) f33) - (((double) f40) * Math.cos((double) f38))) + (((double) f29) * Math.sin((double) f38) * ((double) f42)));
                    float f44 = sin2;
                    float f45 = cos2;
                    f = sin;
                    float f46 = cos;
                    if (dArr.length >= 2) {
                        dArr[0] = (double) sin2;
                        dArr[1] = (double) cos2;
                    }
                    if (!Float.isNaN(f39)) {
                        float f47 = sin2;
                        float f48 = cos2;
                        float f49 = f38;
                        float f50 = f29;
                        f5 = f45;
                        motionWidget = view;
                        motionWidget.setRotationZ((float) (((double) f39) + Math.toDegrees(Math.atan2((double) f45, (double) f44))));
                    } else {
                        float f51 = sin2;
                        float f52 = cos2;
                        float f53 = f38;
                        float f54 = f29;
                        float f55 = f39;
                        motionWidget = view;
                        f5 = f45;
                    }
                    float f56 = f44;
                    f4 = f46;
                    float f57 = f5;
                } else {
                    float f58 = f20;
                    f2 = f21;
                    f3 = f11;
                    float f59 = f12;
                    float f60 = f13;
                    float f61 = f24;
                    if (!Float.isNaN(f17)) {
                        motionWidget.setRotationZ((float) (((double) 0.0f) + ((double) f17) + Math.toDegrees(Math.atan2((double) (f60 + (f61 / 2.0f)), (double) (f59 + (f14 / 2.0f))))));
                    }
                    f4 = f58;
                    float f62 = f60;
                    float f63 = f59;
                }
                int i4 = (int) (f + 0.5f);
                int i5 = (int) (f4 + 0.5f);
                int i6 = (int) (f + 0.5f + f2);
                int i7 = (int) (0.5f + f4 + f3);
                int i8 = i6 - i4;
                int i9 = i7 - i5;
                motionWidget.layout(i4, i5, i6, i7);
                return;
            }
        }
    }

    public void setupRelative(Motion mc, MotionPaths relative) {
        double d = (double) (((this.x + (this.width / 2.0f)) - relative.x) - (relative.width / 2.0f));
        double d2 = (double) (((this.y + (this.height / 2.0f)) - relative.y) - (relative.height / 2.0f));
        this.mRelativeToController = mc;
        this.x = (float) Math.hypot(d2, d);
        if (Float.isNaN(this.mRelativeAngle)) {
            this.y = (float) (Math.atan2(d2, d) + 1.5707963267948966d);
        } else {
            this.y = (float) Math.toRadians((double) this.mRelativeAngle);
        }
    }
}

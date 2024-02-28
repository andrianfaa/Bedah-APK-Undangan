package androidx.constraintlayout.core.motion;

import androidx.constraintlayout.core.motion.key.MotionKey;
import androidx.constraintlayout.core.motion.key.MotionKeyPosition;
import androidx.constraintlayout.core.motion.key.MotionKeyTrigger;
import androidx.constraintlayout.core.motion.utils.CurveFit;
import androidx.constraintlayout.core.motion.utils.DifferentialInterpolator;
import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.core.motion.utils.FloatRect;
import androidx.constraintlayout.core.motion.utils.KeyCache;
import androidx.constraintlayout.core.motion.utils.KeyCycleOscillator;
import androidx.constraintlayout.core.motion.utils.Rect;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.core.motion.utils.Utils;
import androidx.constraintlayout.core.motion.utils.VelocityMatrix;
import androidx.constraintlayout.core.motion.utils.ViewState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class Motion implements TypedValues {
    static final int BOUNCE = 4;
    private static final boolean DEBUG = false;
    public static final int DRAW_PATH_AS_CONFIGURED = 4;
    public static final int DRAW_PATH_BASIC = 1;
    public static final int DRAW_PATH_CARTESIAN = 3;
    public static final int DRAW_PATH_NONE = 0;
    public static final int DRAW_PATH_RECTANGLE = 5;
    public static final int DRAW_PATH_RELATIVE = 2;
    public static final int DRAW_PATH_SCREEN = 6;
    static final int EASE_IN = 1;
    static final int EASE_IN_OUT = 0;
    static final int EASE_OUT = 2;
    private static final boolean FAVOR_FIXED_SIZE_VIEWS = false;
    public static final int HORIZONTAL_PATH_X = 2;
    public static final int HORIZONTAL_PATH_Y = 3;
    private static final int INTERPOLATOR_REFERENCE_ID = -2;
    private static final int INTERPOLATOR_UNDEFINED = -3;
    static final int LINEAR = 3;
    static final int OVERSHOOT = 5;
    public static final int PATH_PERCENT = 0;
    public static final int PATH_PERPENDICULAR = 1;
    public static final int ROTATION_LEFT = 2;
    public static final int ROTATION_RIGHT = 1;
    private static final int SPLINE_STRING = -1;
    private static final String TAG = "MotionController";
    public static final int VERTICAL_PATH_X = 4;
    public static final int VERTICAL_PATH_Y = 5;
    private int MAX_DIMENSION = 4;
    String[] attributeTable;
    private CurveFit mArcSpline;
    private int[] mAttributeInterpolatorCount;
    private String[] mAttributeNames;
    private HashMap<String, SplineSet> mAttributesMap;
    String mConstraintTag;
    float mCurrentCenterX;
    float mCurrentCenterY;
    private int mCurveFitType = -1;
    private HashMap<String, KeyCycleOscillator> mCycleMap;
    private MotionPaths mEndMotionPath = new MotionPaths();
    private MotionConstrainedPoint mEndPoint = new MotionConstrainedPoint();
    int mId;
    private double[] mInterpolateData;
    private int[] mInterpolateVariables;
    private double[] mInterpolateVelocity;
    private ArrayList<MotionKey> mKeyList = new ArrayList<>();
    private MotionKeyTrigger[] mKeyTriggers;
    private ArrayList<MotionPaths> mMotionPaths = new ArrayList<>();
    float mMotionStagger = Float.NaN;
    private boolean mNoMovement = false;
    private int mPathMotionArc = -1;
    private DifferentialInterpolator mQuantizeMotionInterpolator = null;
    private float mQuantizeMotionPhase = Float.NaN;
    private int mQuantizeMotionSteps = -1;
    private CurveFit[] mSpline;
    float mStaggerOffset = 0.0f;
    float mStaggerScale = 1.0f;
    private MotionPaths mStartMotionPath = new MotionPaths();
    private MotionConstrainedPoint mStartPoint = new MotionConstrainedPoint();
    Rect mTempRect = new Rect();
    private HashMap<String, TimeCycleSplineSet> mTimeCycleAttributesMap;
    private int mTransformPivotTarget = -1;
    private MotionWidget mTransformPivotView = null;
    private float[] mValuesBuff = new float[4];
    private float[] mVelocity = new float[1];
    MotionWidget mView;

    public Motion(MotionWidget view) {
        setView(view);
    }

    private float getAdjustedPosition(float position, float[] velocity) {
        if (velocity != null) {
            velocity[0] = 1.0f;
        } else {
            float f = this.mStaggerScale;
            if (((double) f) != 1.0d) {
                float f2 = this.mStaggerOffset;
                if (position < f2) {
                    position = 0.0f;
                }
                if (position > f2 && ((double) position) < 1.0d) {
                    position = Math.min((position - f2) * f, 1.0f);
                }
            }
        }
        float f3 = position;
        Easing easing = this.mStartMotionPath.mKeyFrameEasing;
        float f4 = 0.0f;
        float f5 = Float.NaN;
        Iterator<MotionPaths> it = this.mMotionPaths.iterator();
        while (it.hasNext()) {
            MotionPaths next = it.next();
            if (next.mKeyFrameEasing != null) {
                if (next.time < position) {
                    easing = next.mKeyFrameEasing;
                    f4 = next.time;
                } else if (Float.isNaN(f5)) {
                    f5 = next.time;
                }
            }
        }
        if (easing != null) {
            if (Float.isNaN(f5)) {
                f5 = 1.0f;
            }
            float f6 = (position - f4) / (f5 - f4);
            f3 = ((f5 - f4) * ((float) easing.get((double) f6))) + f4;
            if (velocity != null) {
                velocity[0] = (float) easing.getDiff((double) f6);
            }
        }
        return f3;
    }

    private static DifferentialInterpolator getInterpolator(int type, String interpolatorString, int id) {
        switch (type) {
            case -1:
                final Easing interpolator = Easing.getInterpolator(interpolatorString);
                return new DifferentialInterpolator() {
                    float mX;

                    public float getInterpolation(float x) {
                        this.mX = x;
                        return (float) interpolator.get((double) x);
                    }

                    public float getVelocity() {
                        return (float) interpolator.getDiff((double) this.mX);
                    }
                };
            default:
                return null;
        }
    }

    private float getPreCycleDistance() {
        double d;
        float f;
        int i = 100;
        float[] fArr = new float[2];
        float f2 = 1.0f / ((float) (100 - 1));
        float f3 = 0.0f;
        double d2 = 0.0d;
        double d3 = 0.0d;
        int i2 = 0;
        while (i2 < i) {
            float f4 = ((float) i2) * f2;
            double d4 = (double) f4;
            Easing easing = this.mStartMotionPath.mKeyFrameEasing;
            int i3 = i;
            Iterator<MotionPaths> it = this.mMotionPaths.iterator();
            float f5 = 0.0f;
            Easing easing2 = easing;
            float f6 = Float.NaN;
            while (it.hasNext()) {
                MotionPaths next = it.next();
                Iterator<MotionPaths> it2 = it;
                if (next.mKeyFrameEasing != null) {
                    if (next.time < f4) {
                        Easing easing3 = next.mKeyFrameEasing;
                        f5 = next.time;
                        easing2 = easing3;
                    } else if (Float.isNaN(f6)) {
                        f6 = next.time;
                    }
                }
                it = it2;
            }
            if (easing2 != null) {
                if (Float.isNaN(f6)) {
                    f6 = 1.0f;
                }
                double d5 = d4;
                f = f6;
                d = (double) (((f6 - f5) * ((float) easing2.get((double) ((f4 - f5) / (f6 - f5))))) + f5);
            } else {
                f = f6;
                d = d4;
            }
            this.mSpline[0].getPos(d, this.mInterpolateData);
            float f7 = f;
            double d6 = d;
            Easing easing4 = easing2;
            float f8 = f4;
            int i4 = i2;
            this.mStartMotionPath.getCenter(d, this.mInterpolateVariables, this.mInterpolateData, fArr, 0);
            if (i4 > 0) {
                f3 = (float) (((double) f3) + Math.hypot(d3 - ((double) fArr[1]), d2 - ((double) fArr[0])));
            }
            d2 = (double) fArr[0];
            d3 = (double) fArr[1];
            i2 = i4 + 1;
            i = i3;
        }
        return f3;
    }

    private void insertKey(MotionPaths point) {
        MotionPaths motionPaths = null;
        Iterator<MotionPaths> it = this.mMotionPaths.iterator();
        while (it.hasNext()) {
            MotionPaths next = it.next();
            if (point.position == next.position) {
                motionPaths = next;
            }
        }
        if (motionPaths != null) {
            this.mMotionPaths.remove(motionPaths);
        }
        int binarySearch = Collections.binarySearch(this.mMotionPaths, point);
        if (binarySearch == 0) {
            Utils.loge(TAG, " KeyPath position \"" + point.position + "\" outside of range");
        }
        this.mMotionPaths.add((-binarySearch) - 1, point);
    }

    private void readView(MotionPaths motionPaths) {
        motionPaths.setBounds((float) this.mView.getX(), (float) this.mView.getY(), (float) this.mView.getWidth(), (float) this.mView.getHeight());
    }

    public void addKey(MotionKey key) {
        this.mKeyList.add(key);
    }

    /* access modifiers changed from: package-private */
    public void addKeys(ArrayList<MotionKey> arrayList) {
        this.mKeyList.addAll(arrayList);
    }

    /* access modifiers changed from: package-private */
    public void buildBounds(float[] bounds, int pointCount) {
        float f;
        Motion motion = this;
        int i = pointCount;
        float f2 = 1.0f;
        float f3 = 1.0f / ((float) (i - 1));
        HashMap<String, SplineSet> hashMap = motion.mAttributesMap;
        SplineSet splineSet = hashMap == null ? null : hashMap.get("translationX");
        HashMap<String, SplineSet> hashMap2 = motion.mAttributesMap;
        if (hashMap2 != null) {
            SplineSet splineSet2 = hashMap2.get("translationY");
        }
        HashMap<String, KeyCycleOscillator> hashMap3 = motion.mCycleMap;
        if (hashMap3 != null) {
            KeyCycleOscillator keyCycleOscillator = hashMap3.get("translationX");
        }
        HashMap<String, KeyCycleOscillator> hashMap4 = motion.mCycleMap;
        if (hashMap4 != null) {
            KeyCycleOscillator keyCycleOscillator2 = hashMap4.get("translationY");
        }
        int i2 = 0;
        while (i2 < i) {
            float f4 = ((float) i2) * f3;
            float f5 = motion.mStaggerScale;
            if (f5 != f2) {
                float f6 = motion.mStaggerOffset;
                if (f4 < f6) {
                    f4 = 0.0f;
                }
                if (f4 > f6 && ((double) f4) < 1.0d) {
                    f4 = Math.min((f4 - f6) * f5, f2);
                }
            }
            double d = (double) f4;
            Easing easing = motion.mStartMotionPath.mKeyFrameEasing;
            float f7 = 0.0f;
            float f8 = Float.NaN;
            Iterator<MotionPaths> it = motion.mMotionPaths.iterator();
            while (it.hasNext()) {
                MotionPaths next = it.next();
                if (next.mKeyFrameEasing != null) {
                    if (next.time < f4) {
                        Easing easing2 = next.mKeyFrameEasing;
                        f7 = next.time;
                        easing = easing2;
                    } else if (Float.isNaN(f8)) {
                        f8 = next.time;
                    }
                }
                int i3 = pointCount;
            }
            if (easing != null) {
                if (Float.isNaN(f8)) {
                    f8 = 1.0f;
                }
                f = f3;
                d = (double) (((f8 - f7) * ((float) easing.get((double) ((f4 - f7) / (f8 - f7))))) + f7);
            } else {
                f = f3;
            }
            motion.mSpline[0].getPos(d, motion.mInterpolateData);
            CurveFit curveFit = motion.mArcSpline;
            if (curveFit != null) {
                double[] dArr = motion.mInterpolateData;
                if (dArr.length > 0) {
                    curveFit.getPos(d, dArr);
                }
            }
            motion.mStartMotionPath.getBounds(motion.mInterpolateVariables, motion.mInterpolateData, bounds, i2 * 2);
            i2++;
            motion = this;
            i = pointCount;
            f3 = f;
            splineSet = splineSet;
            f2 = 1.0f;
        }
    }

    /* access modifiers changed from: package-private */
    public int buildKeyBounds(float[] keyBounds, int[] mode) {
        if (keyBounds == null) {
            return 0;
        }
        int i = 0;
        double[] timePoints = this.mSpline[0].getTimePoints();
        if (mode != null) {
            Iterator<MotionPaths> it = this.mMotionPaths.iterator();
            while (it.hasNext()) {
                mode[i] = it.next().mMode;
                i++;
            }
            i = 0;
        }
        for (double pos : timePoints) {
            this.mSpline[0].getPos(pos, this.mInterpolateData);
            this.mStartMotionPath.getBounds(this.mInterpolateVariables, this.mInterpolateData, keyBounds, i);
            i += 2;
        }
        return i / 2;
    }

    public int buildKeyFrames(float[] keyFrames, int[] mode, int[] pos) {
        if (keyFrames == null) {
            return 0;
        }
        int i = 0;
        double[] timePoints = this.mSpline[0].getTimePoints();
        if (mode != null) {
            Iterator<MotionPaths> it = this.mMotionPaths.iterator();
            while (it.hasNext()) {
                mode[i] = it.next().mMode;
                i++;
            }
            i = 0;
        }
        if (pos != null) {
            Iterator<MotionPaths> it2 = this.mMotionPaths.iterator();
            while (it2.hasNext()) {
                pos[i] = (int) (it2.next().position * 100.0f);
                i++;
            }
            i = 0;
        }
        for (int i2 = 0; i2 < timePoints.length; i2++) {
            this.mSpline[0].getPos(timePoints[i2], this.mInterpolateData);
            this.mStartMotionPath.getCenter(timePoints[i2], this.mInterpolateVariables, this.mInterpolateData, keyFrames, i);
            i += 2;
        }
        return i / 2;
    }

    public void buildPath(float[] points, int pointCount) {
        float f;
        double d;
        Motion motion = this;
        int i = pointCount;
        float f2 = 1.0f;
        float f3 = 1.0f / ((float) (i - 1));
        HashMap<String, SplineSet> hashMap = motion.mAttributesMap;
        KeyCycleOscillator keyCycleOscillator = null;
        SplineSet splineSet = hashMap == null ? null : hashMap.get("translationX");
        HashMap<String, SplineSet> hashMap2 = motion.mAttributesMap;
        SplineSet splineSet2 = hashMap2 == null ? null : hashMap2.get("translationY");
        HashMap<String, KeyCycleOscillator> hashMap3 = motion.mCycleMap;
        KeyCycleOscillator keyCycleOscillator2 = hashMap3 == null ? null : hashMap3.get("translationX");
        HashMap<String, KeyCycleOscillator> hashMap4 = motion.mCycleMap;
        if (hashMap4 != null) {
            keyCycleOscillator = hashMap4.get("translationY");
        }
        KeyCycleOscillator keyCycleOscillator3 = keyCycleOscillator;
        int i2 = 0;
        while (i2 < i) {
            float f4 = ((float) i2) * f3;
            float f5 = motion.mStaggerScale;
            if (f5 != f2) {
                float f6 = motion.mStaggerOffset;
                if (f4 < f6) {
                    f4 = 0.0f;
                }
                f = (f4 <= f6 || ((double) f4) >= 1.0d) ? f4 : Math.min((f4 - f6) * f5, f2);
            } else {
                f = f4;
            }
            double d2 = (double) f;
            Easing easing = motion.mStartMotionPath.mKeyFrameEasing;
            Iterator<MotionPaths> it = motion.mMotionPaths.iterator();
            float f7 = 0.0f;
            Easing easing2 = easing;
            float f8 = Float.NaN;
            while (it.hasNext()) {
                MotionPaths next = it.next();
                if (next.mKeyFrameEasing != null) {
                    if (next.time < f) {
                        easing2 = next.mKeyFrameEasing;
                        f7 = next.time;
                    } else if (Float.isNaN(f8)) {
                        f8 = next.time;
                    }
                }
            }
            if (easing2 != null) {
                if (Float.isNaN(f8)) {
                    f8 = 1.0f;
                }
                double d3 = d2;
                float f9 = (float) easing2.get((double) ((f - f7) / (f8 - f7)));
                float f10 = f9;
                float f11 = f8;
                d = (double) (((f8 - f7) * f9) + f7);
            } else {
                float f12 = f8;
                d = d2;
            }
            motion.mSpline[0].getPos(d, motion.mInterpolateData);
            CurveFit curveFit = motion.mArcSpline;
            if (curveFit != null) {
                double[] dArr = motion.mInterpolateData;
                if (dArr.length > 0) {
                    curveFit.getPos(d, dArr);
                }
            }
            double d4 = d;
            Easing easing3 = easing2;
            float f13 = f;
            motion.mStartMotionPath.getCenter(d, motion.mInterpolateVariables, motion.mInterpolateData, points, i2 * 2);
            if (keyCycleOscillator2 != null) {
                int i3 = i2 * 2;
                points[i3] = points[i3] + keyCycleOscillator2.get(f13);
            } else if (splineSet != null) {
                int i4 = i2 * 2;
                points[i4] = points[i4] + splineSet.get(f13);
            }
            if (keyCycleOscillator3 != null) {
                int i5 = (i2 * 2) + 1;
                points[i5] = points[i5] + keyCycleOscillator3.get(f13);
            } else if (splineSet2 != null) {
                int i6 = (i2 * 2) + 1;
                points[i6] = points[i6] + splineSet2.get(f13);
            }
            i2++;
            f2 = 1.0f;
            motion = this;
        }
    }

    public void buildRect(float p, float[] path, int offset) {
        this.mSpline[0].getPos((double) getAdjustedPosition(p, (float[]) null), this.mInterpolateData);
        this.mStartMotionPath.getRect(this.mInterpolateVariables, this.mInterpolateData, path, offset);
    }

    /* access modifiers changed from: package-private */
    public void buildRectangles(float[] path, int pointCount) {
        float f = 1.0f / ((float) (pointCount - 1));
        for (int i = 0; i < pointCount; i++) {
            this.mSpline[0].getPos((double) getAdjustedPosition(((float) i) * f, (float[]) null), this.mInterpolateData);
            this.mStartMotionPath.getRect(this.mInterpolateVariables, this.mInterpolateData, path, i * 8);
        }
    }

    /* access modifiers changed from: package-private */
    public void endTrigger(boolean start) {
    }

    public int getAnimateRelativeTo() {
        return this.mStartMotionPath.mAnimateRelativeTo;
    }

    /* access modifiers changed from: package-private */
    public int getAttributeValues(String attributeType, float[] points, int pointCount) {
        float f = 1.0f / ((float) (pointCount - 1));
        SplineSet splineSet = this.mAttributesMap.get(attributeType);
        if (splineSet == null) {
            return -1;
        }
        for (int i = 0; i < points.length; i++) {
            points[i] = splineSet.get((float) (i / (points.length - 1)));
        }
        return points.length;
    }

    public void getCenter(double p, float[] pos, float[] vel) {
        double[] dArr = new double[4];
        double[] dArr2 = new double[4];
        int[] iArr = new int[4];
        this.mSpline[0].getPos(p, dArr);
        this.mSpline[0].getSlope(p, dArr2);
        Arrays.fill(vel, 0.0f);
        this.mStartMotionPath.getCenter(p, this.mInterpolateVariables, dArr, pos, dArr2, vel);
    }

    public float getCenterX() {
        return this.mCurrentCenterX;
    }

    public float getCenterY() {
        return this.mCurrentCenterY;
    }

    /* access modifiers changed from: package-private */
    public void getDpDt(float position, float locationX, float locationY, float[] mAnchorDpDt) {
        double[] dArr;
        float f = position;
        float adjustedPosition = getAdjustedPosition(position, this.mVelocity);
        CurveFit[] curveFitArr = this.mSpline;
        if (curveFitArr != null) {
            curveFitArr[0].getSlope((double) adjustedPosition, this.mInterpolateVelocity);
            this.mSpline[0].getPos((double) adjustedPosition, this.mInterpolateData);
            float f2 = this.mVelocity[0];
            int i = 0;
            while (true) {
                dArr = this.mInterpolateVelocity;
                if (i >= dArr.length) {
                    break;
                }
                dArr[i] = dArr[i] * ((double) f2);
                i++;
            }
            CurveFit curveFit = this.mArcSpline;
            if (curveFit != null) {
                double[] dArr2 = this.mInterpolateData;
                if (dArr2.length > 0) {
                    curveFit.getPos((double) adjustedPosition, dArr2);
                    this.mArcSpline.getSlope((double) adjustedPosition, this.mInterpolateVelocity);
                    this.mStartMotionPath.setDpDt(locationX, locationY, mAnchorDpDt, this.mInterpolateVariables, this.mInterpolateVelocity, this.mInterpolateData);
                    return;
                }
                return;
            }
            this.mStartMotionPath.setDpDt(locationX, locationY, mAnchorDpDt, this.mInterpolateVariables, dArr, this.mInterpolateData);
            return;
        }
        float f3 = this.mEndMotionPath.x - this.mStartMotionPath.x;
        float f4 = this.mEndMotionPath.y - this.mStartMotionPath.y;
        float f5 = this.mEndMotionPath.width - this.mStartMotionPath.width;
        float f6 = this.mEndMotionPath.height - this.mStartMotionPath.height;
        mAnchorDpDt[0] = ((1.0f - locationX) * f3) + ((f3 + f5) * locationX);
        mAnchorDpDt[1] = ((1.0f - locationY) * f4) + ((f4 + f6) * locationY);
    }

    public int getDrawPath() {
        int i = this.mStartMotionPath.mDrawPath;
        Iterator<MotionPaths> it = this.mMotionPaths.iterator();
        while (it.hasNext()) {
            i = Math.max(i, it.next().mDrawPath);
        }
        return Math.max(i, this.mEndMotionPath.mDrawPath);
    }

    public float getFinalHeight() {
        return this.mEndMotionPath.height;
    }

    public float getFinalWidth() {
        return this.mEndMotionPath.width;
    }

    public float getFinalX() {
        return this.mEndMotionPath.x;
    }

    public float getFinalY() {
        return this.mEndMotionPath.y;
    }

    public int getId(String name) {
        return 0;
    }

    public MotionPaths getKeyFrame(int i) {
        return this.mMotionPaths.get(i);
    }

    public int getKeyFrameInfo(int type, int[] info) {
        int i = type;
        int i2 = 0;
        int i3 = 0;
        float[] fArr = new float[2];
        Iterator<MotionKey> it = this.mKeyList.iterator();
        while (it.hasNext()) {
            MotionKey next = it.next();
            if (next.mType == i || i != -1) {
                int i4 = i3;
                info[i3] = 0;
                int i5 = i3 + 1;
                info[i5] = next.mType;
                int i6 = i5 + 1;
                info[i6] = next.mFramePosition;
                float f = ((float) next.mFramePosition) / 100.0f;
                this.mSpline[0].getPos((double) f, this.mInterpolateData);
                float f2 = f;
                this.mStartMotionPath.getCenter((double) f, this.mInterpolateVariables, this.mInterpolateData, fArr, 0);
                int i7 = i6 + 1;
                info[i7] = Float.floatToIntBits(fArr[0]);
                int i8 = i7 + 1;
                info[i8] = Float.floatToIntBits(fArr[1]);
                if (next instanceof MotionKeyPosition) {
                    MotionKeyPosition motionKeyPosition = (MotionKeyPosition) next;
                    int i9 = i8 + 1;
                    info[i9] = motionKeyPosition.mPositionType;
                    int i10 = i9 + 1;
                    info[i10] = Float.floatToIntBits(motionKeyPosition.mPercentX);
                    i8 = i10 + 1;
                    info[i8] = Float.floatToIntBits(motionKeyPosition.mPercentY);
                }
                i3 = i8 + 1;
                info[i4] = i3 - i4;
                i2++;
            }
        }
        return i2;
    }

    /* access modifiers changed from: package-private */
    public float getKeyFrameParameter(int type, float x, float y) {
        float f = this.mEndMotionPath.x - this.mStartMotionPath.x;
        float f2 = this.mEndMotionPath.y - this.mStartMotionPath.y;
        float f3 = this.mStartMotionPath.x + (this.mStartMotionPath.width / 2.0f);
        float f4 = this.mStartMotionPath.y + (this.mStartMotionPath.height / 2.0f);
        float hypot = (float) Math.hypot((double) f, (double) f2);
        if (((double) hypot) < 1.0E-7d) {
            return Float.NaN;
        }
        float f5 = x - f3;
        float f6 = y - f4;
        if (((float) Math.hypot((double) f5, (double) f6)) == 0.0f) {
            return 0.0f;
        }
        float f7 = (f5 * f) + (f6 * f2);
        switch (type) {
            case 0:
                return f7 / hypot;
            case 1:
                return (float) Math.sqrt((double) ((hypot * hypot) - (f7 * f7)));
            case 2:
                return f5 / f;
            case 3:
                return f6 / f;
            case 4:
                return f5 / f2;
            case 5:
                return f6 / f2;
            default:
                return 0.0f;
        }
    }

    public int getKeyFramePositions(int[] type, float[] pos) {
        int i = 0;
        int i2 = 0;
        Iterator<MotionKey> it = this.mKeyList.iterator();
        while (it.hasNext()) {
            MotionKey next = it.next();
            int i3 = i + 1;
            type[i] = next.mFramePosition + (next.mType * 1000);
            float f = ((float) next.mFramePosition) / 100.0f;
            this.mSpline[0].getPos((double) f, this.mInterpolateData);
            this.mStartMotionPath.getCenter((double) f, this.mInterpolateVariables, this.mInterpolateData, pos, i2);
            i2 += 2;
            i = i3;
        }
        return i;
    }

    /* access modifiers changed from: package-private */
    public double[] getPos(double position) {
        this.mSpline[0].getPos(position, this.mInterpolateData);
        CurveFit curveFit = this.mArcSpline;
        if (curveFit != null) {
            double[] dArr = this.mInterpolateData;
            if (dArr.length > 0) {
                curveFit.getPos(position, dArr);
            }
        }
        return this.mInterpolateData;
    }

    /* access modifiers changed from: package-private */
    public MotionKeyPosition getPositionKeyframe(int layoutWidth, int layoutHeight, float x, float y) {
        FloatRect floatRect = new FloatRect();
        floatRect.left = this.mStartMotionPath.x;
        floatRect.top = this.mStartMotionPath.y;
        floatRect.right = floatRect.left + this.mStartMotionPath.width;
        floatRect.bottom = floatRect.top + this.mStartMotionPath.height;
        FloatRect floatRect2 = new FloatRect();
        floatRect2.left = this.mEndMotionPath.x;
        floatRect2.top = this.mEndMotionPath.y;
        floatRect2.right = floatRect2.left + this.mEndMotionPath.width;
        floatRect2.bottom = floatRect2.top + this.mEndMotionPath.height;
        Iterator<MotionKey> it = this.mKeyList.iterator();
        while (it.hasNext()) {
            MotionKey next = it.next();
            if ((next instanceof MotionKeyPosition) && ((MotionKeyPosition) next).intersects(layoutWidth, layoutHeight, floatRect, floatRect2, x, y)) {
                return (MotionKeyPosition) next;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void getPostLayoutDvDp(float position, int width, int height, float locationX, float locationY, float[] mAnchorDpDt) {
        VelocityMatrix velocityMatrix;
        float adjustedPosition = getAdjustedPosition(position, this.mVelocity);
        HashMap<String, SplineSet> hashMap = this.mAttributesMap;
        KeyCycleOscillator keyCycleOscillator = null;
        SplineSet splineSet = hashMap == null ? null : hashMap.get("translationX");
        HashMap<String, SplineSet> hashMap2 = this.mAttributesMap;
        SplineSet splineSet2 = hashMap2 == null ? null : hashMap2.get("translationY");
        HashMap<String, SplineSet> hashMap3 = this.mAttributesMap;
        SplineSet splineSet3 = hashMap3 == null ? null : hashMap3.get("rotationZ");
        HashMap<String, SplineSet> hashMap4 = this.mAttributesMap;
        SplineSet splineSet4 = hashMap4 == null ? null : hashMap4.get("scaleX");
        HashMap<String, SplineSet> hashMap5 = this.mAttributesMap;
        SplineSet splineSet5 = hashMap5 == null ? null : hashMap5.get("scaleY");
        HashMap<String, KeyCycleOscillator> hashMap6 = this.mCycleMap;
        KeyCycleOscillator keyCycleOscillator2 = hashMap6 == null ? null : hashMap6.get("translationX");
        HashMap<String, KeyCycleOscillator> hashMap7 = this.mCycleMap;
        KeyCycleOscillator keyCycleOscillator3 = hashMap7 == null ? null : hashMap7.get("translationY");
        HashMap<String, KeyCycleOscillator> hashMap8 = this.mCycleMap;
        KeyCycleOscillator keyCycleOscillator4 = hashMap8 == null ? null : hashMap8.get("rotationZ");
        HashMap<String, KeyCycleOscillator> hashMap9 = this.mCycleMap;
        KeyCycleOscillator keyCycleOscillator5 = hashMap9 == null ? null : hashMap9.get("scaleX");
        HashMap<String, KeyCycleOscillator> hashMap10 = this.mCycleMap;
        if (hashMap10 != null) {
            keyCycleOscillator = hashMap10.get("scaleY");
        }
        KeyCycleOscillator keyCycleOscillator6 = keyCycleOscillator;
        VelocityMatrix velocityMatrix2 = new VelocityMatrix();
        velocityMatrix2.clear();
        velocityMatrix2.setRotationVelocity(splineSet3, adjustedPosition);
        velocityMatrix2.setTranslationVelocity(splineSet, splineSet2, adjustedPosition);
        velocityMatrix2.setScaleVelocity(splineSet4, splineSet5, adjustedPosition);
        velocityMatrix2.setRotationVelocity(keyCycleOscillator4, adjustedPosition);
        velocityMatrix2.setTranslationVelocity(keyCycleOscillator2, keyCycleOscillator3, adjustedPosition);
        velocityMatrix2.setScaleVelocity(keyCycleOscillator5, keyCycleOscillator6, adjustedPosition);
        CurveFit curveFit = this.mArcSpline;
        if (curveFit != null) {
            double[] dArr = this.mInterpolateData;
            if (dArr.length > 0) {
                curveFit.getPos((double) adjustedPosition, dArr);
                this.mArcSpline.getSlope((double) adjustedPosition, this.mInterpolateVelocity);
                velocityMatrix = velocityMatrix2;
                KeyCycleOscillator keyCycleOscillator7 = keyCycleOscillator2;
                KeyCycleOscillator keyCycleOscillator8 = keyCycleOscillator4;
                KeyCycleOscillator keyCycleOscillator9 = keyCycleOscillator5;
                KeyCycleOscillator keyCycleOscillator10 = keyCycleOscillator6;
                this.mStartMotionPath.setDpDt(locationX, locationY, mAnchorDpDt, this.mInterpolateVariables, this.mInterpolateVelocity, this.mInterpolateData);
            } else {
                velocityMatrix = velocityMatrix2;
                KeyCycleOscillator keyCycleOscillator11 = keyCycleOscillator5;
                KeyCycleOscillator keyCycleOscillator12 = keyCycleOscillator6;
                KeyCycleOscillator keyCycleOscillator13 = keyCycleOscillator2;
                KeyCycleOscillator keyCycleOscillator14 = keyCycleOscillator4;
            }
            velocityMatrix.applyTransform(locationX, locationY, width, height, mAnchorDpDt);
            return;
        }
        VelocityMatrix velocityMatrix3 = velocityMatrix2;
        KeyCycleOscillator keyCycleOscillator15 = keyCycleOscillator5;
        KeyCycleOscillator keyCycleOscillator16 = keyCycleOscillator6;
        KeyCycleOscillator keyCycleOscillator17 = keyCycleOscillator2;
        KeyCycleOscillator keyCycleOscillator18 = keyCycleOscillator4;
        if (this.mSpline != null) {
            float adjustedPosition2 = getAdjustedPosition(adjustedPosition, this.mVelocity);
            this.mSpline[0].getSlope((double) adjustedPosition2, this.mInterpolateVelocity);
            this.mSpline[0].getPos((double) adjustedPosition2, this.mInterpolateData);
            float f = this.mVelocity[0];
            int i = 0;
            while (true) {
                double[] dArr2 = this.mInterpolateVelocity;
                if (i < dArr2.length) {
                    dArr2[i] = dArr2[i] * ((double) f);
                    i++;
                } else {
                    float f2 = locationX;
                    float f3 = locationY;
                    float f4 = f;
                    this.mStartMotionPath.setDpDt(f2, f3, mAnchorDpDt, this.mInterpolateVariables, dArr2, this.mInterpolateData);
                    velocityMatrix3.applyTransform(f2, f3, width, height, mAnchorDpDt);
                    return;
                }
            }
        } else {
            float f5 = this.mEndMotionPath.x - this.mStartMotionPath.x;
            float f6 = this.mEndMotionPath.y - this.mStartMotionPath.y;
            mAnchorDpDt[0] = ((1.0f - locationX) * f5) + ((f5 + (this.mEndMotionPath.width - this.mStartMotionPath.width)) * locationX);
            mAnchorDpDt[1] = ((1.0f - locationY) * f6) + ((f6 + (this.mEndMotionPath.height - this.mStartMotionPath.height)) * locationY);
            velocityMatrix3.clear();
            VelocityMatrix velocityMatrix4 = velocityMatrix3;
            velocityMatrix4.setRotationVelocity(splineSet3, adjustedPosition);
            velocityMatrix4.setTranslationVelocity(splineSet, splineSet2, adjustedPosition);
            velocityMatrix4.setScaleVelocity(splineSet4, splineSet5, adjustedPosition);
            velocityMatrix4.setRotationVelocity(keyCycleOscillator18, adjustedPosition);
            KeyCycleOscillator keyCycleOscillator19 = keyCycleOscillator17;
            velocityMatrix4.setTranslationVelocity(keyCycleOscillator19, keyCycleOscillator3, adjustedPosition);
            KeyCycleOscillator keyCycleOscillator20 = keyCycleOscillator16;
            velocityMatrix4.setScaleVelocity(keyCycleOscillator15, keyCycleOscillator20, adjustedPosition);
            KeyCycleOscillator keyCycleOscillator21 = keyCycleOscillator20;
            KeyCycleOscillator keyCycleOscillator22 = keyCycleOscillator19;
            VelocityMatrix velocityMatrix5 = velocityMatrix4;
            velocityMatrix4.applyTransform(locationX, locationY, width, height, mAnchorDpDt);
        }
    }

    public float getStartHeight() {
        return this.mStartMotionPath.height;
    }

    public float getStartWidth() {
        return this.mStartMotionPath.width;
    }

    public float getStartX() {
        return this.mStartMotionPath.x;
    }

    public float getStartY() {
        return this.mStartMotionPath.y;
    }

    public int getTransformPivotTarget() {
        return this.mTransformPivotTarget;
    }

    public MotionWidget getView() {
        return this.mView;
    }

    public boolean interpolate(MotionWidget child, float global_position, long time, KeyCache keyCache) {
        float f;
        float f2;
        MotionWidget motionWidget = child;
        float adjustedPosition = getAdjustedPosition(global_position, (float[]) null);
        int i = this.mQuantizeMotionSteps;
        if (i != -1) {
            float f3 = adjustedPosition;
            float f4 = 1.0f / ((float) i);
            float floor = ((float) Math.floor((double) (adjustedPosition / f4))) * f4;
            float f5 = (adjustedPosition % f4) / f4;
            if (!Float.isNaN(this.mQuantizeMotionPhase)) {
                f5 = (this.mQuantizeMotionPhase + f5) % 1.0f;
            }
            DifferentialInterpolator differentialInterpolator = this.mQuantizeMotionInterpolator;
            if (differentialInterpolator != null) {
                f2 = differentialInterpolator.getInterpolation(f5);
            } else {
                f2 = ((double) f5) > 0.5d ? 1.0f : 0.0f;
            }
            f = (f2 * f4) + floor;
        } else {
            f = adjustedPosition;
        }
        HashMap<String, SplineSet> hashMap = this.mAttributesMap;
        if (hashMap != null) {
            for (SplineSet property : hashMap.values()) {
                property.setProperty(motionWidget, f);
            }
        }
        CurveFit[] curveFitArr = this.mSpline;
        if (curveFitArr != null) {
            curveFitArr[0].getPos((double) f, this.mInterpolateData);
            this.mSpline[0].getSlope((double) f, this.mInterpolateVelocity);
            CurveFit curveFit = this.mArcSpline;
            if (curveFit != null) {
                double[] dArr = this.mInterpolateData;
                if (dArr.length > 0) {
                    curveFit.getPos((double) f, dArr);
                    this.mArcSpline.getSlope((double) f, this.mInterpolateVelocity);
                }
            }
            if (!this.mNoMovement) {
                this.mStartMotionPath.setView(f, child, this.mInterpolateVariables, this.mInterpolateData, this.mInterpolateVelocity, (double[]) null);
            }
            if (this.mTransformPivotTarget != -1) {
                if (this.mTransformPivotView == null) {
                    this.mTransformPivotView = child.getParent().findViewById(this.mTransformPivotTarget);
                }
                MotionWidget motionWidget2 = this.mTransformPivotView;
                if (motionWidget2 != null) {
                    float top = ((float) (motionWidget2.getTop() + this.mTransformPivotView.getBottom())) / 2.0f;
                    float left = ((float) (this.mTransformPivotView.getLeft() + this.mTransformPivotView.getRight())) / 2.0f;
                    if (child.getRight() - child.getLeft() > 0 && child.getBottom() - child.getTop() > 0) {
                        motionWidget.setPivotX(left - ((float) child.getLeft()));
                        motionWidget.setPivotY(top - ((float) child.getTop()));
                    }
                }
            }
            int i2 = 1;
            while (true) {
                CurveFit[] curveFitArr2 = this.mSpline;
                if (i2 >= curveFitArr2.length) {
                    break;
                }
                curveFitArr2[i2].getPos((double) f, this.mValuesBuff);
                this.mStartMotionPath.customAttributes.get(this.mAttributeNames[i2 - 1]).setInterpolatedValue(motionWidget, this.mValuesBuff);
                i2++;
            }
            if (this.mStartPoint.mVisibilityMode == 0) {
                if (f <= 0.0f) {
                    motionWidget.setVisibility(this.mStartPoint.visibility);
                } else if (f >= 1.0f) {
                    motionWidget.setVisibility(this.mEndPoint.visibility);
                } else if (this.mEndPoint.visibility != this.mStartPoint.visibility) {
                    motionWidget.setVisibility(4);
                }
            }
            if (this.mKeyTriggers != null) {
                int i3 = 0;
                while (true) {
                    MotionKeyTrigger[] motionKeyTriggerArr = this.mKeyTriggers;
                    if (i3 >= motionKeyTriggerArr.length) {
                        break;
                    }
                    motionKeyTriggerArr[i3].conditionallyFire(f, motionWidget);
                    i3++;
                }
            }
        } else {
            float f6 = this.mStartMotionPath.x + ((this.mEndMotionPath.x - this.mStartMotionPath.x) * f);
            float f7 = this.mStartMotionPath.y + ((this.mEndMotionPath.y - this.mStartMotionPath.y) * f);
            int i4 = (int) (f6 + 0.5f);
            int i5 = (int) (f7 + 0.5f);
            int i6 = (int) (f6 + 0.5f + this.mStartMotionPath.width + ((this.mEndMotionPath.width - this.mStartMotionPath.width) * f));
            int i7 = (int) (0.5f + f7 + this.mStartMotionPath.height + ((this.mEndMotionPath.height - this.mStartMotionPath.height) * f));
            int i8 = i6 - i4;
            int i9 = i7 - i5;
            motionWidget.layout(i4, i5, i6, i7);
        }
        HashMap<String, KeyCycleOscillator> hashMap2 = this.mCycleMap;
        if (hashMap2 != null) {
            for (KeyCycleOscillator next : hashMap2.values()) {
                if (next instanceof KeyCycleOscillator.PathRotateSet) {
                    double[] dArr2 = this.mInterpolateVelocity;
                    ((KeyCycleOscillator.PathRotateSet) next).setPathRotate(child, f, dArr2[0], dArr2[1]);
                } else {
                    next.setProperty(motionWidget, f);
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public String name() {
        return this.mView.getName();
    }

    /* access modifiers changed from: package-private */
    public void positionKeyframe(MotionWidget view, MotionKeyPosition key, float x, float y, String[] attribute, float[] value) {
        FloatRect floatRect = new FloatRect();
        floatRect.left = this.mStartMotionPath.x;
        floatRect.top = this.mStartMotionPath.y;
        floatRect.right = floatRect.left + this.mStartMotionPath.width;
        floatRect.bottom = floatRect.top + this.mStartMotionPath.height;
        FloatRect floatRect2 = new FloatRect();
        floatRect2.left = this.mEndMotionPath.x;
        floatRect2.top = this.mEndMotionPath.y;
        floatRect2.right = floatRect2.left + this.mEndMotionPath.width;
        floatRect2.bottom = floatRect2.top + this.mEndMotionPath.height;
        key.positionAttributes(view, floatRect, floatRect2, x, y, attribute, value);
    }

    /* access modifiers changed from: package-private */
    public void rotate(Rect rect, Rect out, int rotation, int preHeight, int preWidth) {
        switch (rotation) {
            case 1:
                int i = rect.left + rect.right;
                out.left = ((rect.top + rect.bottom) - rect.width()) / 2;
                out.top = preWidth - ((rect.height() + i) / 2);
                out.right = out.left + rect.width();
                out.bottom = out.top + rect.height();
                return;
            case 2:
                int i2 = rect.left + rect.right;
                out.left = preHeight - ((rect.width() + (rect.top + rect.bottom)) / 2);
                out.top = (i2 - rect.height()) / 2;
                out.right = out.left + rect.width();
                out.bottom = out.top + rect.height();
                return;
            case 3:
                int i3 = rect.left + rect.right;
                int i4 = rect.top + rect.bottom;
                out.left = ((rect.height() / 2) + rect.top) - (i3 / 2);
                out.top = preWidth - ((rect.height() + i3) / 2);
                out.right = out.left + rect.width();
                out.bottom = out.top + rect.height();
                return;
            case 4:
                int i5 = rect.left + rect.right;
                out.left = preHeight - ((rect.width() + (rect.bottom + rect.top)) / 2);
                out.top = (i5 - rect.height()) / 2;
                out.right = out.left + rect.width();
                out.bottom = out.top + rect.height();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: package-private */
    public void setBothStates(MotionWidget v) {
        this.mStartMotionPath.time = 0.0f;
        this.mStartMotionPath.position = 0.0f;
        this.mNoMovement = true;
        this.mStartMotionPath.setBounds((float) v.getX(), (float) v.getY(), (float) v.getWidth(), (float) v.getHeight());
        this.mEndMotionPath.setBounds((float) v.getX(), (float) v.getY(), (float) v.getWidth(), (float) v.getHeight());
        this.mStartPoint.setState(v);
        this.mEndPoint.setState(v);
    }

    public void setDrawPath(int debugMode) {
        this.mStartMotionPath.mDrawPath = debugMode;
    }

    public void setEnd(MotionWidget mw) {
        this.mEndMotionPath.time = 1.0f;
        this.mEndMotionPath.position = 1.0f;
        readView(this.mEndMotionPath);
        this.mEndMotionPath.setBounds((float) mw.getLeft(), (float) mw.getTop(), (float) mw.getWidth(), (float) mw.getHeight());
        this.mEndMotionPath.applyParameters(mw);
        this.mEndPoint.setState(mw);
    }

    public void setPathMotionArc(int arc) {
        this.mPathMotionArc = arc;
    }

    public void setStart(MotionWidget mw) {
        this.mStartMotionPath.time = 0.0f;
        this.mStartMotionPath.position = 0.0f;
        this.mStartMotionPath.setBounds((float) mw.getX(), (float) mw.getY(), (float) mw.getWidth(), (float) mw.getHeight());
        this.mStartMotionPath.applyParameters(mw);
        this.mStartPoint.setState(mw);
    }

    public void setStartState(ViewState rect, MotionWidget v, int rotation, int preWidth, int preHeight) {
        this.mStartMotionPath.time = 0.0f;
        this.mStartMotionPath.position = 0.0f;
        Rect rect2 = new Rect();
        switch (rotation) {
            case 1:
                int i = rect.left + rect.right;
                rect2.left = ((rect.top + rect.bottom) - rect.width()) / 2;
                rect2.top = preWidth - ((rect.height() + i) / 2);
                rect2.right = rect2.left + rect.width();
                rect2.bottom = rect2.top + rect.height();
                break;
            case 2:
                int i2 = rect.left + rect.right;
                rect2.left = preHeight - ((rect.width() + (rect.top + rect.bottom)) / 2);
                rect2.top = (i2 - rect.height()) / 2;
                rect2.right = rect2.left + rect.width();
                rect2.bottom = rect2.top + rect.height();
                break;
        }
        this.mStartMotionPath.setBounds((float) rect2.left, (float) rect2.top, (float) rect2.width(), (float) rect2.height());
        this.mStartPoint.setState(rect2, v, rotation, rect.rotation);
    }

    public void setTransformPivotTarget(int transformPivotTarget) {
        this.mTransformPivotTarget = transformPivotTarget;
        this.mTransformPivotView = null;
    }

    public boolean setValue(int id, float value) {
        return false;
    }

    public boolean setValue(int id, int value) {
        switch (id) {
            case 509:
                setPathMotionArc(value);
                return true;
            case TypedValues.TransitionType.TYPE_AUTO_TRANSITION /*704*/:
                return true;
            default:
                return false;
        }
    }

    public boolean setValue(int id, String value) {
        if (705 == id) {
            System.out.println("TYPE_INTERPOLATOR  " + value);
            this.mQuantizeMotionInterpolator = getInterpolator(-1, value, 0);
        }
        return false;
    }

    public boolean setValue(int id, boolean value) {
        return false;
    }

    public void setView(MotionWidget view) {
        this.mView = view;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v36, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v32, resolved type: double[][]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setup(int r30, int r31, float r32, long r33) {
        /*
            r29 = this;
            r0 = r29
            r1 = r33
            java.util.HashSet r3 = new java.util.HashSet
            r3.<init>()
            java.util.HashSet r4 = new java.util.HashSet
            r4.<init>()
            java.util.HashSet r5 = new java.util.HashSet
            r5.<init>()
            java.util.HashSet r6 = new java.util.HashSet
            r6.<init>()
            java.util.HashMap r7 = new java.util.HashMap
            r7.<init>()
            r8 = 0
            int r9 = r0.mPathMotionArc
            r10 = -1
            if (r9 == r10) goto L_0x0027
            androidx.constraintlayout.core.motion.MotionPaths r11 = r0.mStartMotionPath
            r11.mPathMotionArc = r9
        L_0x0027:
            androidx.constraintlayout.core.motion.MotionConstrainedPoint r9 = r0.mStartPoint
            androidx.constraintlayout.core.motion.MotionConstrainedPoint r11 = r0.mEndPoint
            r9.different(r11, r5)
            java.util.ArrayList<androidx.constraintlayout.core.motion.key.MotionKey> r9 = r0.mKeyList
            if (r9 == 0) goto L_0x0096
            java.util.Iterator r9 = r9.iterator()
        L_0x0036:
            boolean r11 = r9.hasNext()
            if (r11 == 0) goto L_0x0096
            java.lang.Object r11 = r9.next()
            androidx.constraintlayout.core.motion.key.MotionKey r11 = (androidx.constraintlayout.core.motion.key.MotionKey) r11
            boolean r12 = r11 instanceof androidx.constraintlayout.core.motion.key.MotionKeyPosition
            if (r12 == 0) goto L_0x006b
            r12 = r11
            androidx.constraintlayout.core.motion.key.MotionKeyPosition r12 = (androidx.constraintlayout.core.motion.key.MotionKeyPosition) r12
            androidx.constraintlayout.core.motion.MotionPaths r15 = new androidx.constraintlayout.core.motion.MotionPaths
            androidx.constraintlayout.core.motion.MotionPaths r14 = r0.mStartMotionPath
            androidx.constraintlayout.core.motion.MotionPaths r13 = r0.mEndMotionPath
            r18 = r13
            r13 = r15
            r17 = r14
            r14 = r30
            r10 = r15
            r15 = r31
            r16 = r12
            r13.<init>(r14, r15, r16, r17, r18)
            r0.insertKey(r10)
            int r10 = r12.mCurveFit
            r13 = -1
            if (r10 == r13) goto L_0x006a
            int r10 = r12.mCurveFit
            r0.mCurveFitType = r10
        L_0x006a:
            goto L_0x0094
        L_0x006b:
            boolean r10 = r11 instanceof androidx.constraintlayout.core.motion.key.MotionKeyCycle
            if (r10 == 0) goto L_0x0073
            r11.getAttributeNames(r6)
            goto L_0x0094
        L_0x0073:
            boolean r10 = r11 instanceof androidx.constraintlayout.core.motion.key.MotionKeyTimeCycle
            if (r10 == 0) goto L_0x007b
            r11.getAttributeNames(r4)
            goto L_0x0094
        L_0x007b:
            boolean r10 = r11 instanceof androidx.constraintlayout.core.motion.key.MotionKeyTrigger
            if (r10 == 0) goto L_0x008e
            if (r8 != 0) goto L_0x0087
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            r8 = r10
        L_0x0087:
            r10 = r11
            androidx.constraintlayout.core.motion.key.MotionKeyTrigger r10 = (androidx.constraintlayout.core.motion.key.MotionKeyTrigger) r10
            r8.add(r10)
            goto L_0x0094
        L_0x008e:
            r11.setInterpolation(r7)
            r11.getAttributeNames(r5)
        L_0x0094:
            r10 = -1
            goto L_0x0036
        L_0x0096:
            r9 = 0
            if (r8 == 0) goto L_0x00a3
            androidx.constraintlayout.core.motion.key.MotionKeyTrigger[] r10 = new androidx.constraintlayout.core.motion.key.MotionKeyTrigger[r9]
            java.lang.Object[] r10 = r8.toArray(r10)
            androidx.constraintlayout.core.motion.key.MotionKeyTrigger[] r10 = (androidx.constraintlayout.core.motion.key.MotionKeyTrigger[]) r10
            r0.mKeyTriggers = r10
        L_0x00a3:
            boolean r10 = r5.isEmpty()
            java.lang.String r11 = ","
            java.lang.String r12 = "CUSTOM,"
            r13 = 1
            if (r10 != 0) goto L_0x01a8
            java.util.HashMap r10 = new java.util.HashMap
            r10.<init>()
            r0.mAttributesMap = r10
            java.util.Iterator r10 = r5.iterator()
        L_0x00b9:
            boolean r14 = r10.hasNext()
            if (r14 == 0) goto L_0x013e
            java.lang.Object r14 = r10.next()
            java.lang.String r14 = (java.lang.String) r14
            boolean r15 = r14.startsWith(r12)
            if (r15 == 0) goto L_0x011d
            androidx.constraintlayout.core.motion.utils.KeyFrameArray$CustomVar r15 = new androidx.constraintlayout.core.motion.utils.KeyFrameArray$CustomVar
            r15.<init>()
            java.lang.String[] r16 = r14.split(r11)
            r9 = r16[r13]
            java.util.ArrayList<androidx.constraintlayout.core.motion.key.MotionKey> r13 = r0.mKeyList
            java.util.Iterator r13 = r13.iterator()
        L_0x00dc:
            boolean r18 = r13.hasNext()
            if (r18 == 0) goto L_0x0112
            java.lang.Object r18 = r13.next()
            r19 = r3
            r3 = r18
            androidx.constraintlayout.core.motion.key.MotionKey r3 = (androidx.constraintlayout.core.motion.key.MotionKey) r3
            r18 = r8
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.CustomVariable> r8 = r3.mCustom
            if (r8 != 0) goto L_0x00f7
            r8 = r18
            r3 = r19
            goto L_0x00dc
        L_0x00f7:
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.CustomVariable> r8 = r3.mCustom
            java.lang.Object r8 = r8.get(r9)
            androidx.constraintlayout.core.motion.CustomVariable r8 = (androidx.constraintlayout.core.motion.CustomVariable) r8
            if (r8 == 0) goto L_0x0109
            r20 = r9
            int r9 = r3.mFramePosition
            r15.append(r9, r8)
            goto L_0x010b
        L_0x0109:
            r20 = r9
        L_0x010b:
            r8 = r18
            r3 = r19
            r9 = r20
            goto L_0x00dc
        L_0x0112:
            r19 = r3
            r18 = r8
            r20 = r9
            androidx.constraintlayout.core.motion.utils.SplineSet r3 = androidx.constraintlayout.core.motion.utils.SplineSet.makeCustomSplineSet(r14, r15)
            goto L_0x0125
        L_0x011d:
            r19 = r3
            r18 = r8
            androidx.constraintlayout.core.motion.utils.SplineSet r3 = androidx.constraintlayout.core.motion.utils.SplineSet.makeSpline(r14, r1)
        L_0x0125:
            if (r3 != 0) goto L_0x012e
            r8 = r18
            r3 = r19
            r9 = 0
            r13 = 1
            goto L_0x00b9
        L_0x012e:
            r3.setType(r14)
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.utils.SplineSet> r8 = r0.mAttributesMap
            r8.put(r14, r3)
            r8 = r18
            r3 = r19
            r9 = 0
            r13 = 1
            goto L_0x00b9
        L_0x013e:
            r19 = r3
            r18 = r8
            java.util.ArrayList<androidx.constraintlayout.core.motion.key.MotionKey> r3 = r0.mKeyList
            if (r3 == 0) goto L_0x0160
            java.util.Iterator r3 = r3.iterator()
        L_0x014a:
            boolean r8 = r3.hasNext()
            if (r8 == 0) goto L_0x0160
            java.lang.Object r8 = r3.next()
            androidx.constraintlayout.core.motion.key.MotionKey r8 = (androidx.constraintlayout.core.motion.key.MotionKey) r8
            boolean r9 = r8 instanceof androidx.constraintlayout.core.motion.key.MotionKeyAttributes
            if (r9 == 0) goto L_0x015f
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.utils.SplineSet> r9 = r0.mAttributesMap
            r8.addValues(r9)
        L_0x015f:
            goto L_0x014a
        L_0x0160:
            androidx.constraintlayout.core.motion.MotionConstrainedPoint r3 = r0.mStartPoint
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.utils.SplineSet> r8 = r0.mAttributesMap
            r9 = 0
            r3.addValues(r8, r9)
            androidx.constraintlayout.core.motion.MotionConstrainedPoint r3 = r0.mEndPoint
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.utils.SplineSet> r8 = r0.mAttributesMap
            r9 = 100
            r3.addValues(r8, r9)
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.utils.SplineSet> r3 = r0.mAttributesMap
            java.util.Set r3 = r3.keySet()
            java.util.Iterator r3 = r3.iterator()
        L_0x017b:
            boolean r8 = r3.hasNext()
            if (r8 == 0) goto L_0x01ac
            java.lang.Object r8 = r3.next()
            java.lang.String r8 = (java.lang.String) r8
            r9 = 0
            boolean r10 = r7.containsKey(r8)
            if (r10 == 0) goto L_0x019a
            java.lang.Object r10 = r7.get(r8)
            java.lang.Integer r10 = (java.lang.Integer) r10
            if (r10 == 0) goto L_0x019a
            int r9 = r10.intValue()
        L_0x019a:
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.utils.SplineSet> r10 = r0.mAttributesMap
            java.lang.Object r10 = r10.get(r8)
            androidx.constraintlayout.core.motion.utils.SplineSet r10 = (androidx.constraintlayout.core.motion.utils.SplineSet) r10
            if (r10 == 0) goto L_0x01a7
            r10.setup(r9)
        L_0x01a7:
            goto L_0x017b
        L_0x01a8:
            r19 = r3
            r18 = r8
        L_0x01ac:
            boolean r3 = r4.isEmpty()
            if (r3 != 0) goto L_0x0293
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet> r3 = r0.mTimeCycleAttributesMap
            if (r3 != 0) goto L_0x01bd
            java.util.HashMap r3 = new java.util.HashMap
            r3.<init>()
            r0.mTimeCycleAttributesMap = r3
        L_0x01bd:
            java.util.Iterator r3 = r4.iterator()
        L_0x01c1:
            boolean r8 = r3.hasNext()
            if (r8 == 0) goto L_0x023d
            java.lang.Object r8 = r3.next()
            java.lang.String r8 = (java.lang.String) r8
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet> r9 = r0.mTimeCycleAttributesMap
            boolean r9 = r9.containsKey(r8)
            if (r9 == 0) goto L_0x01d6
            goto L_0x01c1
        L_0x01d6:
            r9 = 0
            boolean r10 = r8.startsWith(r12)
            if (r10 == 0) goto L_0x0226
            androidx.constraintlayout.core.motion.utils.KeyFrameArray$CustomVar r10 = new androidx.constraintlayout.core.motion.utils.KeyFrameArray$CustomVar
            r10.<init>()
            java.lang.String[] r13 = r8.split(r11)
            r14 = 1
            r13 = r13[r14]
            java.util.ArrayList<androidx.constraintlayout.core.motion.key.MotionKey> r14 = r0.mKeyList
            java.util.Iterator r14 = r14.iterator()
        L_0x01ef:
            boolean r15 = r14.hasNext()
            if (r15 == 0) goto L_0x021d
            java.lang.Object r15 = r14.next()
            androidx.constraintlayout.core.motion.key.MotionKey r15 = (androidx.constraintlayout.core.motion.key.MotionKey) r15
            r20 = r3
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.CustomVariable> r3 = r15.mCustom
            if (r3 != 0) goto L_0x0204
            r3 = r20
            goto L_0x01ef
        L_0x0204:
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.CustomVariable> r3 = r15.mCustom
            java.lang.Object r3 = r3.get(r13)
            androidx.constraintlayout.core.motion.CustomVariable r3 = (androidx.constraintlayout.core.motion.CustomVariable) r3
            if (r3 == 0) goto L_0x0216
            r21 = r4
            int r4 = r15.mFramePosition
            r10.append(r4, r3)
            goto L_0x0218
        L_0x0216:
            r21 = r4
        L_0x0218:
            r3 = r20
            r4 = r21
            goto L_0x01ef
        L_0x021d:
            r20 = r3
            r21 = r4
            androidx.constraintlayout.core.motion.utils.SplineSet r3 = androidx.constraintlayout.core.motion.utils.SplineSet.makeCustomSplineSet(r8, r10)
            goto L_0x022e
        L_0x0226:
            r20 = r3
            r21 = r4
            androidx.constraintlayout.core.motion.utils.SplineSet r3 = androidx.constraintlayout.core.motion.utils.SplineSet.makeSpline(r8, r1)
        L_0x022e:
            if (r3 != 0) goto L_0x0235
            r3 = r20
            r4 = r21
            goto L_0x01c1
        L_0x0235:
            r3.setType(r8)
            r3 = r20
            r4 = r21
            goto L_0x01c1
        L_0x023d:
            r21 = r4
            java.util.ArrayList<androidx.constraintlayout.core.motion.key.MotionKey> r3 = r0.mKeyList
            if (r3 == 0) goto L_0x0260
            java.util.Iterator r3 = r3.iterator()
        L_0x0247:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x0260
            java.lang.Object r4 = r3.next()
            androidx.constraintlayout.core.motion.key.MotionKey r4 = (androidx.constraintlayout.core.motion.key.MotionKey) r4
            boolean r8 = r4 instanceof androidx.constraintlayout.core.motion.key.MotionKeyTimeCycle
            if (r8 == 0) goto L_0x025f
            r8 = r4
            androidx.constraintlayout.core.motion.key.MotionKeyTimeCycle r8 = (androidx.constraintlayout.core.motion.key.MotionKeyTimeCycle) r8
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet> r9 = r0.mTimeCycleAttributesMap
            r8.addTimeValues(r9)
        L_0x025f:
            goto L_0x0247
        L_0x0260:
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet> r3 = r0.mTimeCycleAttributesMap
            java.util.Set r3 = r3.keySet()
            java.util.Iterator r3 = r3.iterator()
        L_0x026a:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x0295
            java.lang.Object r4 = r3.next()
            java.lang.String r4 = (java.lang.String) r4
            r8 = 0
            boolean r9 = r7.containsKey(r4)
            if (r9 == 0) goto L_0x0287
            java.lang.Object r9 = r7.get(r4)
            java.lang.Integer r9 = (java.lang.Integer) r9
            int r8 = r9.intValue()
        L_0x0287:
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet> r9 = r0.mTimeCycleAttributesMap
            java.lang.Object r9 = r9.get(r4)
            androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet r9 = (androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet) r9
            r9.setup(r8)
            goto L_0x026a
        L_0x0293:
            r21 = r4
        L_0x0295:
            java.util.ArrayList<androidx.constraintlayout.core.motion.MotionPaths> r3 = r0.mMotionPaths
            int r3 = r3.size()
            r4 = 2
            int r3 = r3 + r4
            androidx.constraintlayout.core.motion.MotionPaths[] r3 = new androidx.constraintlayout.core.motion.MotionPaths[r3]
            r8 = 1
            androidx.constraintlayout.core.motion.MotionPaths r9 = r0.mStartMotionPath
            r10 = 0
            r3[r10] = r9
            int r9 = r3.length
            r10 = 1
            int r9 = r9 - r10
            androidx.constraintlayout.core.motion.MotionPaths r10 = r0.mEndMotionPath
            r3[r9] = r10
            java.util.ArrayList<androidx.constraintlayout.core.motion.MotionPaths> r9 = r0.mMotionPaths
            int r9 = r9.size()
            if (r9 <= 0) goto L_0x02bd
            int r9 = r0.mCurveFitType
            int r10 = androidx.constraintlayout.core.motion.key.MotionKey.UNSET
            if (r9 != r10) goto L_0x02bd
            r9 = 0
            r0.mCurveFitType = r9
        L_0x02bd:
            java.util.ArrayList<androidx.constraintlayout.core.motion.MotionPaths> r9 = r0.mMotionPaths
            java.util.Iterator r9 = r9.iterator()
        L_0x02c3:
            boolean r10 = r9.hasNext()
            if (r10 == 0) goto L_0x02d5
            java.lang.Object r10 = r9.next()
            androidx.constraintlayout.core.motion.MotionPaths r10 = (androidx.constraintlayout.core.motion.MotionPaths) r10
            int r11 = r8 + 1
            r3[r8] = r10
            r8 = r11
            goto L_0x02c3
        L_0x02d5:
            r9 = 18
            java.util.HashSet r10 = new java.util.HashSet
            r10.<init>()
            androidx.constraintlayout.core.motion.MotionPaths r11 = r0.mEndMotionPath
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.CustomVariable> r11 = r11.customAttributes
            java.util.Set r11 = r11.keySet()
            java.util.Iterator r11 = r11.iterator()
        L_0x02e8:
            boolean r13 = r11.hasNext()
            if (r13 == 0) goto L_0x0319
            java.lang.Object r13 = r11.next()
            java.lang.String r13 = (java.lang.String) r13
            androidx.constraintlayout.core.motion.MotionPaths r14 = r0.mStartMotionPath
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.CustomVariable> r14 = r14.customAttributes
            boolean r14 = r14.containsKey(r13)
            if (r14 == 0) goto L_0x0318
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.StringBuilder r14 = r14.append(r12)
            java.lang.StringBuilder r14 = r14.append(r13)
            java.lang.String r14 = r14.toString()
            boolean r14 = r5.contains(r14)
            if (r14 != 0) goto L_0x0318
            r10.add(r13)
        L_0x0318:
            goto L_0x02e8
        L_0x0319:
            r11 = 0
            java.lang.String[] r12 = new java.lang.String[r11]
            java.lang.Object[] r11 = r10.toArray(r12)
            java.lang.String[] r11 = (java.lang.String[]) r11
            r0.mAttributeNames = r11
            int r11 = r11.length
            int[] r11 = new int[r11]
            r0.mAttributeInterpolatorCount = r11
            r11 = 0
        L_0x032a:
            java.lang.String[] r12 = r0.mAttributeNames
            int r13 = r12.length
            if (r11 >= r13) goto L_0x0363
            r12 = r12[r11]
            int[] r13 = r0.mAttributeInterpolatorCount
            r14 = 0
            r13[r11] = r14
            r13 = 0
        L_0x0337:
            int r14 = r3.length
            if (r13 >= r14) goto L_0x0360
            r14 = r3[r13]
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.CustomVariable> r14 = r14.customAttributes
            boolean r14 = r14.containsKey(r12)
            if (r14 == 0) goto L_0x035d
            r14 = r3[r13]
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.CustomVariable> r14 = r14.customAttributes
            java.lang.Object r14 = r14.get(r12)
            androidx.constraintlayout.core.motion.CustomVariable r14 = (androidx.constraintlayout.core.motion.CustomVariable) r14
            if (r14 == 0) goto L_0x035d
            int[] r15 = r0.mAttributeInterpolatorCount
            r20 = r15[r11]
            int r22 = r14.numberOfInterpolatedValues()
            int r20 = r20 + r22
            r15[r11] = r20
            goto L_0x0360
        L_0x035d:
            int r13 = r13 + 1
            goto L_0x0337
        L_0x0360:
            int r11 = r11 + 1
            goto L_0x032a
        L_0x0363:
            r11 = 0
            r12 = r3[r11]
            int r11 = r12.mPathMotionArc
            r12 = -1
            if (r11 == r12) goto L_0x036d
            r11 = 1
            goto L_0x036e
        L_0x036d:
            r11 = 0
        L_0x036e:
            java.lang.String[] r12 = r0.mAttributeNames
            int r12 = r12.length
            int r12 = r12 + r9
            boolean[] r12 = new boolean[r12]
            r13 = 1
        L_0x0375:
            int r14 = r3.length
            if (r13 >= r14) goto L_0x0387
            r14 = r3[r13]
            int r15 = r13 + -1
            r15 = r3[r15]
            java.lang.String[] r4 = r0.mAttributeNames
            r14.different(r15, r12, r4, r11)
            int r13 = r13 + 1
            r4 = 2
            goto L_0x0375
        L_0x0387:
            r4 = 0
            r8 = 1
        L_0x0389:
            int r13 = r12.length
            if (r8 >= r13) goto L_0x0395
            boolean r13 = r12[r8]
            if (r13 == 0) goto L_0x0392
            int r4 = r4 + 1
        L_0x0392:
            int r8 = r8 + 1
            goto L_0x0389
        L_0x0395:
            int[] r8 = new int[r4]
            r0.mInterpolateVariables = r8
            r8 = 2
            int r13 = java.lang.Math.max(r8, r4)
            double[] r8 = new double[r13]
            r0.mInterpolateData = r8
            double[] r8 = new double[r13]
            r0.mInterpolateVelocity = r8
            r4 = 0
            r8 = 1
        L_0x03a8:
            int r14 = r12.length
            if (r8 >= r14) goto L_0x03b9
            boolean r14 = r12[r8]
            if (r14 == 0) goto L_0x03b6
            int[] r14 = r0.mInterpolateVariables
            int r15 = r4 + 1
            r14[r4] = r8
            r4 = r15
        L_0x03b6:
            int r8 = r8 + 1
            goto L_0x03a8
        L_0x03b9:
            int r8 = r3.length
            int[] r14 = r0.mInterpolateVariables
            int r14 = r14.length
            r15 = 2
            int[] r1 = new int[r15]
            r2 = 1
            r1[r2] = r14
            r2 = 0
            r1[r2] = r8
            java.lang.Class r2 = java.lang.Double.TYPE
            java.lang.Object r1 = java.lang.reflect.Array.newInstance(r2, r1)
            double[][] r1 = (double[][]) r1
            int r2 = r3.length
            double[] r2 = new double[r2]
            r8 = 0
        L_0x03d2:
            int r14 = r3.length
            if (r8 >= r14) goto L_0x03ec
            r14 = r3[r8]
            r15 = r1[r8]
            r22 = r4
            int[] r4 = r0.mInterpolateVariables
            r14.fillStandard(r15, r4)
            r4 = r3[r8]
            float r4 = r4.time
            double r14 = (double) r4
            r2[r8] = r14
            int r8 = r8 + 1
            r4 = r22
            goto L_0x03d2
        L_0x03ec:
            r22 = r4
            r4 = 0
        L_0x03ef:
            int[] r8 = r0.mInterpolateVariables
            int r14 = r8.length
            if (r4 >= r14) goto L_0x044e
            r8 = r8[r4]
            java.lang.String[] r14 = androidx.constraintlayout.core.motion.MotionPaths.names
            int r14 = r14.length
            if (r8 >= r14) goto L_0x0441
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String[] r15 = androidx.constraintlayout.core.motion.MotionPaths.names
            r23 = r5
            int[] r5 = r0.mInterpolateVariables
            r5 = r5[r4]
            r5 = r15[r5]
            java.lang.StringBuilder r5 = r14.append(r5)
            java.lang.String r14 = " ["
            java.lang.StringBuilder r5 = r5.append(r14)
            java.lang.String r5 = r5.toString()
            r14 = 0
        L_0x0419:
            int r15 = r3.length
            if (r14 >= r15) goto L_0x043c
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.StringBuilder r15 = r15.append(r5)
            r24 = r1[r14]
            r25 = r7
            r26 = r8
            r7 = r24[r4]
            java.lang.StringBuilder r7 = r15.append(r7)
            java.lang.String r5 = r7.toString()
            int r14 = r14 + 1
            r7 = r25
            r8 = r26
            goto L_0x0419
        L_0x043c:
            r25 = r7
            r26 = r8
            goto L_0x0447
        L_0x0441:
            r23 = r5
            r25 = r7
            r26 = r8
        L_0x0447:
            int r4 = r4 + 1
            r5 = r23
            r7 = r25
            goto L_0x03ef
        L_0x044e:
            r23 = r5
            r25 = r7
            java.lang.String[] r4 = r0.mAttributeNames
            int r4 = r4.length
            r5 = 1
            int r4 = r4 + r5
            androidx.constraintlayout.core.motion.utils.CurveFit[] r4 = new androidx.constraintlayout.core.motion.utils.CurveFit[r4]
            r0.mSpline = r4
            r4 = 0
        L_0x045c:
            java.lang.String[] r5 = r0.mAttributeNames
            int r7 = r5.length
            if (r4 >= r7) goto L_0x04ed
            r7 = 0
            r8 = 0
            double[][] r8 = (double[][]) r8
            r14 = 0
            r5 = r5[r4]
            r15 = 0
        L_0x0469:
            r24 = r9
            int r9 = r3.length
            if (r15 >= r9) goto L_0x04c4
            r9 = r3[r15]
            boolean r9 = r9.hasCustomData(r5)
            if (r9 == 0) goto L_0x04b3
            if (r8 != 0) goto L_0x049b
            int r9 = r3.length
            double[] r14 = new double[r9]
            int r9 = r3.length
            r26 = r10
            r10 = r3[r15]
            int r10 = r10.getCustomDataCount(r5)
            r27 = r11
            r28 = r12
            r11 = 2
            int[] r12 = new int[r11]
            r11 = 1
            r12[r11] = r10
            r10 = 0
            r12[r10] = r9
            java.lang.Class r9 = java.lang.Double.TYPE
            java.lang.Object r9 = java.lang.reflect.Array.newInstance(r9, r12)
            r8 = r9
            double[][] r8 = (double[][]) r8
            goto L_0x04a1
        L_0x049b:
            r26 = r10
            r27 = r11
            r28 = r12
        L_0x04a1:
            r9 = r3[r15]
            float r9 = r9.time
            double r9 = (double) r9
            r14[r7] = r9
            r9 = r3[r15]
            r10 = r8[r7]
            r11 = 0
            r9.getCustomData(r5, r10, r11)
            int r7 = r7 + 1
            goto L_0x04b9
        L_0x04b3:
            r26 = r10
            r27 = r11
            r28 = r12
        L_0x04b9:
            int r15 = r15 + 1
            r9 = r24
            r10 = r26
            r11 = r27
            r12 = r28
            goto L_0x0469
        L_0x04c4:
            r26 = r10
            r27 = r11
            r28 = r12
            double[] r9 = java.util.Arrays.copyOf(r14, r7)
            java.lang.Object[] r10 = java.util.Arrays.copyOf(r8, r7)
            r8 = r10
            double[][] r8 = (double[][]) r8
            androidx.constraintlayout.core.motion.utils.CurveFit[] r10 = r0.mSpline
            int r11 = r4 + 1
            int r12 = r0.mCurveFitType
            androidx.constraintlayout.core.motion.utils.CurveFit r12 = androidx.constraintlayout.core.motion.utils.CurveFit.get(r12, r9, r8)
            r10[r11] = r12
            int r4 = r4 + 1
            r9 = r24
            r10 = r26
            r11 = r27
            r12 = r28
            goto L_0x045c
        L_0x04ed:
            r24 = r9
            r26 = r10
            r27 = r11
            r28 = r12
            androidx.constraintlayout.core.motion.utils.CurveFit[] r4 = r0.mSpline
            int r5 = r0.mCurveFitType
            androidx.constraintlayout.core.motion.utils.CurveFit r5 = androidx.constraintlayout.core.motion.utils.CurveFit.get(r5, r2, r1)
            r7 = 0
            r4[r7] = r5
            r4 = r3[r7]
            int r4 = r4.mPathMotionArc
            r5 = -1
            if (r4 == r5) goto L_0x054a
            int r4 = r3.length
            int[] r5 = new int[r4]
            double[] r7 = new double[r4]
            r8 = 2
            int[] r9 = new int[r8]
            r10 = 1
            r9[r10] = r8
            r8 = 0
            r9[r8] = r4
            java.lang.Class r8 = java.lang.Double.TYPE
            java.lang.Object r8 = java.lang.reflect.Array.newInstance(r8, r9)
            double[][] r8 = (double[][]) r8
            r9 = 0
        L_0x051e:
            if (r9 >= r4) goto L_0x0544
            r10 = r3[r9]
            int r10 = r10.mPathMotionArc
            r5[r9] = r10
            r10 = r3[r9]
            float r10 = r10.time
            double r10 = (double) r10
            r7[r9] = r10
            r10 = r8[r9]
            r11 = r3[r9]
            float r11 = r11.x
            double r11 = (double) r11
            r14 = 0
            r10[r14] = r11
            r10 = r8[r9]
            r11 = r3[r9]
            float r11 = r11.y
            double r11 = (double) r11
            r15 = 1
            r10[r15] = r11
            int r9 = r9 + 1
            goto L_0x051e
        L_0x0544:
            androidx.constraintlayout.core.motion.utils.CurveFit r9 = androidx.constraintlayout.core.motion.utils.CurveFit.getArc(r5, r7, r8)
            r0.mArcSpline = r9
        L_0x054a:
            r4 = 2143289344(0x7fc00000, float:NaN)
            java.util.HashMap r5 = new java.util.HashMap
            r5.<init>()
            r0.mCycleMap = r5
            java.util.ArrayList<androidx.constraintlayout.core.motion.key.MotionKey> r5 = r0.mKeyList
            if (r5 == 0) goto L_0x05c0
            java.util.Iterator r5 = r6.iterator()
        L_0x055b:
            boolean r7 = r5.hasNext()
            if (r7 == 0) goto L_0x0587
            java.lang.Object r7 = r5.next()
            java.lang.String r7 = (java.lang.String) r7
            androidx.constraintlayout.core.motion.utils.KeyCycleOscillator r8 = androidx.constraintlayout.core.motion.utils.KeyCycleOscillator.makeWidgetCycle(r7)
            if (r8 != 0) goto L_0x056e
            goto L_0x055b
        L_0x056e:
            boolean r9 = r8.variesByPath()
            if (r9 == 0) goto L_0x057e
            boolean r9 = java.lang.Float.isNaN(r4)
            if (r9 == 0) goto L_0x057e
            float r4 = r29.getPreCycleDistance()
        L_0x057e:
            r8.setType(r7)
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.utils.KeyCycleOscillator> r9 = r0.mCycleMap
            r9.put(r7, r8)
            goto L_0x055b
        L_0x0587:
            java.util.ArrayList<androidx.constraintlayout.core.motion.key.MotionKey> r5 = r0.mKeyList
            java.util.Iterator r5 = r5.iterator()
        L_0x058d:
            boolean r7 = r5.hasNext()
            if (r7 == 0) goto L_0x05a6
            java.lang.Object r7 = r5.next()
            androidx.constraintlayout.core.motion.key.MotionKey r7 = (androidx.constraintlayout.core.motion.key.MotionKey) r7
            boolean r8 = r7 instanceof androidx.constraintlayout.core.motion.key.MotionKeyCycle
            if (r8 == 0) goto L_0x05a5
            r8 = r7
            androidx.constraintlayout.core.motion.key.MotionKeyCycle r8 = (androidx.constraintlayout.core.motion.key.MotionKeyCycle) r8
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.utils.KeyCycleOscillator> r9 = r0.mCycleMap
            r8.addCycleValues(r9)
        L_0x05a5:
            goto L_0x058d
        L_0x05a6:
            java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.utils.KeyCycleOscillator> r5 = r0.mCycleMap
            java.util.Collection r5 = r5.values()
            java.util.Iterator r5 = r5.iterator()
        L_0x05b0:
            boolean r7 = r5.hasNext()
            if (r7 == 0) goto L_0x05c0
            java.lang.Object r7 = r5.next()
            androidx.constraintlayout.core.motion.utils.KeyCycleOscillator r7 = (androidx.constraintlayout.core.motion.utils.KeyCycleOscillator) r7
            r7.setup(r4)
            goto L_0x05b0
        L_0x05c0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.motion.Motion.setup(int, int, float, long):void");
    }

    public void setupRelative(Motion motionController) {
        this.mStartMotionPath.setupRelative(motionController, motionController.mStartMotionPath);
        this.mEndMotionPath.setupRelative(motionController, motionController.mEndMotionPath);
    }

    public String toString() {
        return " start: x: " + this.mStartMotionPath.x + " y: " + this.mStartMotionPath.y + " end: x: " + this.mEndMotionPath.x + " y: " + this.mEndMotionPath.y;
    }
}

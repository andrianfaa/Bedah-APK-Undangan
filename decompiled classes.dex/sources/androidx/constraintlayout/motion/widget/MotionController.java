package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import androidx.constraintlayout.core.motion.utils.CurveFit;
import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.core.motion.utils.KeyCache;
import androidx.constraintlayout.core.motion.utils.KeyCycleOscillator;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.core.motion.utils.VelocityMatrix;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.motion.utils.CustomSupport;
import androidx.constraintlayout.motion.utils.ViewOscillator;
import androidx.constraintlayout.motion.utils.ViewSpline;
import androidx.constraintlayout.motion.utils.ViewState;
import androidx.constraintlayout.motion.utils.ViewTimeCycle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import mt.Log1F380D;

/* compiled from: 0020 */
public class MotionController {
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
    private HashMap<String, ViewSpline> mAttributesMap;
    String mConstraintTag;
    float mCurrentCenterX;
    float mCurrentCenterY;
    private int mCurveFitType = -1;
    private HashMap<String, ViewOscillator> mCycleMap;
    private MotionPaths mEndMotionPath = new MotionPaths();
    private MotionConstrainedPoint mEndPoint = new MotionConstrainedPoint();
    boolean mForceMeasure = false;
    int mId;
    private double[] mInterpolateData;
    private int[] mInterpolateVariables;
    private double[] mInterpolateVelocity;
    private ArrayList<Key> mKeyList = new ArrayList<>();
    private KeyTrigger[] mKeyTriggers;
    private ArrayList<MotionPaths> mMotionPaths = new ArrayList<>();
    float mMotionStagger = Float.NaN;
    private boolean mNoMovement = false;
    private int mPathMotionArc = Key.UNSET;
    private Interpolator mQuantizeMotionInterpolator = null;
    private float mQuantizeMotionPhase = Float.NaN;
    private int mQuantizeMotionSteps = Key.UNSET;
    private CurveFit[] mSpline;
    float mStaggerOffset = 0.0f;
    float mStaggerScale = 1.0f;
    private MotionPaths mStartMotionPath = new MotionPaths();
    private MotionConstrainedPoint mStartPoint = new MotionConstrainedPoint();
    Rect mTempRect = new Rect();
    private HashMap<String, ViewTimeCycle> mTimeCycleAttributesMap;
    private int mTransformPivotTarget = Key.UNSET;
    private View mTransformPivotView = null;
    private float[] mValuesBuff = new float[4];
    private float[] mVelocity = new float[1];
    View mView;

    MotionController(View view) {
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

    private static Interpolator getInterpolator(Context context, int type, String interpolatorString, int id) {
        switch (type) {
            case -2:
                return AnimationUtils.loadInterpolator(context, id);
            case -1:
                final Easing interpolator = Easing.getInterpolator(interpolatorString);
                return new Interpolator() {
                    public float getInterpolation(float v) {
                        return (float) Easing.this.get((double) v);
                    }
                };
            case 0:
                return new AccelerateDecelerateInterpolator();
            case 1:
                return new AccelerateInterpolator();
            case 2:
                return new DecelerateInterpolator();
            case 3:
                return null;
            case 4:
                return new BounceInterpolator();
            case 5:
                return new OvershootInterpolator();
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
        int binarySearch = Collections.binarySearch(this.mMotionPaths, point);
        if (binarySearch == 0) {
            Log.e(TAG, " KeyPath position \"" + point.position + "\" outside of range");
        }
        this.mMotionPaths.add((-binarySearch) - 1, point);
    }

    private void readView(MotionPaths motionPaths) {
        motionPaths.setBounds((float) ((int) this.mView.getX()), (float) ((int) this.mView.getY()), (float) this.mView.getWidth(), (float) this.mView.getHeight());
    }

    public void addKey(Key key) {
        this.mKeyList.add(key);
    }

    /* access modifiers changed from: package-private */
    public void addKeys(ArrayList<Key> arrayList) {
        this.mKeyList.addAll(arrayList);
    }

    /* access modifiers changed from: package-private */
    public void buildBounds(float[] bounds, int pointCount) {
        float f;
        MotionController motionController = this;
        int i = pointCount;
        float f2 = 1.0f;
        float f3 = 1.0f / ((float) (i - 1));
        HashMap<String, ViewSpline> hashMap = motionController.mAttributesMap;
        SplineSet splineSet = hashMap == null ? null : hashMap.get("translationX");
        HashMap<String, ViewSpline> hashMap2 = motionController.mAttributesMap;
        if (hashMap2 != null) {
            SplineSet splineSet2 = hashMap2.get("translationY");
        }
        HashMap<String, ViewOscillator> hashMap3 = motionController.mCycleMap;
        if (hashMap3 != null) {
            ViewOscillator viewOscillator = hashMap3.get("translationX");
        }
        HashMap<String, ViewOscillator> hashMap4 = motionController.mCycleMap;
        if (hashMap4 != null) {
            ViewOscillator viewOscillator2 = hashMap4.get("translationY");
        }
        int i2 = 0;
        while (i2 < i) {
            float f4 = ((float) i2) * f3;
            float f5 = motionController.mStaggerScale;
            if (f5 != f2) {
                float f6 = motionController.mStaggerOffset;
                if (f4 < f6) {
                    f4 = 0.0f;
                }
                if (f4 > f6 && ((double) f4) < 1.0d) {
                    f4 = Math.min((f4 - f6) * f5, f2);
                }
            }
            double d = (double) f4;
            Easing easing = motionController.mStartMotionPath.mKeyFrameEasing;
            float f7 = 0.0f;
            float f8 = Float.NaN;
            Iterator<MotionPaths> it = motionController.mMotionPaths.iterator();
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
            motionController.mSpline[0].getPos(d, motionController.mInterpolateData);
            CurveFit curveFit = motionController.mArcSpline;
            if (curveFit != null) {
                double[] dArr = motionController.mInterpolateData;
                if (dArr.length > 0) {
                    curveFit.getPos(d, dArr);
                }
            }
            motionController.mStartMotionPath.getBounds(motionController.mInterpolateVariables, motionController.mInterpolateData, bounds, i2 * 2);
            i2++;
            motionController = this;
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

    /* access modifiers changed from: package-private */
    public int buildKeyFrames(float[] keyFrames, int[] mode) {
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
        for (int i2 = 0; i2 < timePoints.length; i2++) {
            this.mSpline[0].getPos(timePoints[i2], this.mInterpolateData);
            this.mStartMotionPath.getCenter(timePoints[i2], this.mInterpolateVariables, this.mInterpolateData, keyFrames, i);
            i += 2;
        }
        return i / 2;
    }

    /* access modifiers changed from: package-private */
    public void buildPath(float[] points, int pointCount) {
        float f;
        double d;
        MotionController motionController = this;
        int i = pointCount;
        float f2 = 1.0f;
        float f3 = 1.0f / ((float) (i - 1));
        HashMap<String, ViewSpline> hashMap = motionController.mAttributesMap;
        ViewOscillator viewOscillator = null;
        SplineSet splineSet = hashMap == null ? null : hashMap.get("translationX");
        HashMap<String, ViewSpline> hashMap2 = motionController.mAttributesMap;
        SplineSet splineSet2 = hashMap2 == null ? null : hashMap2.get("translationY");
        HashMap<String, ViewOscillator> hashMap3 = motionController.mCycleMap;
        ViewOscillator viewOscillator2 = hashMap3 == null ? null : hashMap3.get("translationX");
        HashMap<String, ViewOscillator> hashMap4 = motionController.mCycleMap;
        if (hashMap4 != null) {
            viewOscillator = hashMap4.get("translationY");
        }
        ViewOscillator viewOscillator3 = viewOscillator;
        int i2 = 0;
        while (i2 < i) {
            float f4 = ((float) i2) * f3;
            float f5 = motionController.mStaggerScale;
            if (f5 != f2) {
                float f6 = motionController.mStaggerOffset;
                if (f4 < f6) {
                    f4 = 0.0f;
                }
                f = (f4 <= f6 || ((double) f4) >= 1.0d) ? f4 : Math.min((f4 - f6) * f5, f2);
            } else {
                f = f4;
            }
            double d2 = (double) f;
            Easing easing = motionController.mStartMotionPath.mKeyFrameEasing;
            Iterator<MotionPaths> it = motionController.mMotionPaths.iterator();
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
            motionController.mSpline[0].getPos(d, motionController.mInterpolateData);
            CurveFit curveFit = motionController.mArcSpline;
            if (curveFit != null) {
                double[] dArr = motionController.mInterpolateData;
                if (dArr.length > 0) {
                    curveFit.getPos(d, dArr);
                }
            }
            double d4 = d;
            Easing easing3 = easing2;
            float f13 = f;
            motionController.mStartMotionPath.getCenter(d, motionController.mInterpolateVariables, motionController.mInterpolateData, points, i2 * 2);
            if (viewOscillator2 != null) {
                int i3 = i2 * 2;
                points[i3] = points[i3] + viewOscillator2.get(f13);
            } else if (splineSet != null) {
                int i4 = i2 * 2;
                points[i4] = points[i4] + splineSet.get(f13);
            }
            if (viewOscillator3 != null) {
                int i5 = (i2 * 2) + 1;
                points[i5] = points[i5] + viewOscillator3.get(f13);
            } else if (splineSet2 != null) {
                int i6 = (i2 * 2) + 1;
                points[i6] = points[i6] + splineSet2.get(f13);
            }
            i2++;
            f2 = 1.0f;
            motionController = this;
        }
    }

    /* access modifiers changed from: package-private */
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
        String name = Debug.getName(this.mView);
        Log1F380D.a((Object) name);
        if ("button".equals(name) && this.mKeyTriggers != null) {
            int i = 0;
            while (true) {
                KeyTrigger[] keyTriggerArr = this.mKeyTriggers;
                if (i < keyTriggerArr.length) {
                    keyTriggerArr[i].conditionallyFire(start ? -100.0f : 100.0f, this.mView);
                    i++;
                } else {
                    return;
                }
            }
        }
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

    /* access modifiers changed from: package-private */
    public MotionPaths getKeyFrame(int i) {
        return this.mMotionPaths.get(i);
    }

    public int getKeyFrameInfo(int type, int[] info) {
        int i = type;
        int i2 = 0;
        int i3 = 0;
        float[] fArr = new float[2];
        Iterator<Key> it = this.mKeyList.iterator();
        while (it.hasNext()) {
            Key next = it.next();
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
                if (next instanceof KeyPosition) {
                    KeyPosition keyPosition = (KeyPosition) next;
                    int i9 = i8 + 1;
                    info[i9] = keyPosition.mPositionType;
                    int i10 = i9 + 1;
                    info[i10] = Float.floatToIntBits(keyPosition.mPercentX);
                    i8 = i10 + 1;
                    info[i8] = Float.floatToIntBits(keyPosition.mPercentY);
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
        Iterator<Key> it = this.mKeyList.iterator();
        while (it.hasNext()) {
            Key next = it.next();
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
    public KeyPositionBase getPositionKeyframe(int layoutWidth, int layoutHeight, float x, float y) {
        RectF rectF = new RectF();
        rectF.left = this.mStartMotionPath.x;
        rectF.top = this.mStartMotionPath.y;
        rectF.right = rectF.left + this.mStartMotionPath.width;
        rectF.bottom = rectF.top + this.mStartMotionPath.height;
        RectF rectF2 = new RectF();
        rectF2.left = this.mEndMotionPath.x;
        rectF2.top = this.mEndMotionPath.y;
        rectF2.right = rectF2.left + this.mEndMotionPath.width;
        rectF2.bottom = rectF2.top + this.mEndMotionPath.height;
        Iterator<Key> it = this.mKeyList.iterator();
        while (it.hasNext()) {
            Key next = it.next();
            if ((next instanceof KeyPositionBase) && ((KeyPositionBase) next).intersects(layoutWidth, layoutHeight, rectF, rectF2, x, y)) {
                return (KeyPositionBase) next;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void getPostLayoutDvDp(float position, int width, int height, float locationX, float locationY, float[] mAnchorDpDt) {
        VelocityMatrix velocityMatrix;
        float adjustedPosition = getAdjustedPosition(position, this.mVelocity);
        HashMap<String, ViewSpline> hashMap = this.mAttributesMap;
        ViewOscillator viewOscillator = null;
        SplineSet splineSet = hashMap == null ? null : hashMap.get("translationX");
        HashMap<String, ViewSpline> hashMap2 = this.mAttributesMap;
        SplineSet splineSet2 = hashMap2 == null ? null : hashMap2.get("translationY");
        HashMap<String, ViewSpline> hashMap3 = this.mAttributesMap;
        SplineSet splineSet3 = hashMap3 == null ? null : hashMap3.get(Key.ROTATION);
        HashMap<String, ViewSpline> hashMap4 = this.mAttributesMap;
        SplineSet splineSet4 = hashMap4 == null ? null : hashMap4.get("scaleX");
        HashMap<String, ViewSpline> hashMap5 = this.mAttributesMap;
        SplineSet splineSet5 = hashMap5 == null ? null : hashMap5.get("scaleY");
        HashMap<String, ViewOscillator> hashMap6 = this.mCycleMap;
        ViewOscillator viewOscillator2 = hashMap6 == null ? null : hashMap6.get("translationX");
        HashMap<String, ViewOscillator> hashMap7 = this.mCycleMap;
        ViewOscillator viewOscillator3 = hashMap7 == null ? null : hashMap7.get("translationY");
        HashMap<String, ViewOscillator> hashMap8 = this.mCycleMap;
        ViewOscillator viewOscillator4 = hashMap8 == null ? null : hashMap8.get(Key.ROTATION);
        HashMap<String, ViewOscillator> hashMap9 = this.mCycleMap;
        ViewOscillator viewOscillator5 = hashMap9 == null ? null : hashMap9.get("scaleX");
        HashMap<String, ViewOscillator> hashMap10 = this.mCycleMap;
        if (hashMap10 != null) {
            viewOscillator = hashMap10.get("scaleY");
        }
        ViewOscillator viewOscillator6 = viewOscillator;
        VelocityMatrix velocityMatrix2 = new VelocityMatrix();
        velocityMatrix2.clear();
        velocityMatrix2.setRotationVelocity(splineSet3, adjustedPosition);
        velocityMatrix2.setTranslationVelocity(splineSet, splineSet2, adjustedPosition);
        velocityMatrix2.setScaleVelocity(splineSet4, splineSet5, adjustedPosition);
        velocityMatrix2.setRotationVelocity((KeyCycleOscillator) viewOscillator4, adjustedPosition);
        velocityMatrix2.setTranslationVelocity((KeyCycleOscillator) viewOscillator2, (KeyCycleOscillator) viewOscillator3, adjustedPosition);
        velocityMatrix2.setScaleVelocity((KeyCycleOscillator) viewOscillator5, (KeyCycleOscillator) viewOscillator6, adjustedPosition);
        CurveFit curveFit = this.mArcSpline;
        if (curveFit != null) {
            double[] dArr = this.mInterpolateData;
            if (dArr.length > 0) {
                curveFit.getPos((double) adjustedPosition, dArr);
                this.mArcSpline.getSlope((double) adjustedPosition, this.mInterpolateVelocity);
                velocityMatrix = velocityMatrix2;
                ViewOscillator viewOscillator7 = viewOscillator2;
                ViewOscillator viewOscillator8 = viewOscillator4;
                ViewOscillator viewOscillator9 = viewOscillator5;
                ViewOscillator viewOscillator10 = viewOscillator6;
                this.mStartMotionPath.setDpDt(locationX, locationY, mAnchorDpDt, this.mInterpolateVariables, this.mInterpolateVelocity, this.mInterpolateData);
            } else {
                velocityMatrix = velocityMatrix2;
                ViewOscillator viewOscillator11 = viewOscillator5;
                ViewOscillator viewOscillator12 = viewOscillator6;
                ViewOscillator viewOscillator13 = viewOscillator2;
                ViewOscillator viewOscillator14 = viewOscillator4;
            }
            velocityMatrix.applyTransform(locationX, locationY, width, height, mAnchorDpDt);
            return;
        }
        VelocityMatrix velocityMatrix3 = velocityMatrix2;
        ViewOscillator viewOscillator15 = viewOscillator5;
        ViewOscillator viewOscillator16 = viewOscillator6;
        ViewOscillator viewOscillator17 = viewOscillator2;
        ViewOscillator viewOscillator18 = viewOscillator4;
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
            velocityMatrix4.setRotationVelocity((KeyCycleOscillator) viewOscillator18, adjustedPosition);
            ViewOscillator viewOscillator19 = viewOscillator17;
            velocityMatrix4.setTranslationVelocity((KeyCycleOscillator) viewOscillator19, (KeyCycleOscillator) viewOscillator3, adjustedPosition);
            ViewOscillator viewOscillator20 = viewOscillator16;
            velocityMatrix4.setScaleVelocity((KeyCycleOscillator) viewOscillator15, (KeyCycleOscillator) viewOscillator20, adjustedPosition);
            ViewOscillator viewOscillator21 = viewOscillator20;
            ViewOscillator viewOscillator22 = viewOscillator19;
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

    public View getView() {
        return this.mView;
    }

    /* access modifiers changed from: package-private */
    public boolean interpolate(View child, float global_position, long time, KeyCache keyCache) {
        float f;
        ViewTimeCycle.PathRotate pathRotate;
        boolean z;
        char c;
        float f2;
        View view = child;
        float adjustedPosition = getAdjustedPosition(global_position, (float[]) null);
        if (this.mQuantizeMotionSteps != Key.UNSET) {
            float f3 = adjustedPosition;
            float f4 = 1.0f / ((float) this.mQuantizeMotionSteps);
            float floor = ((float) Math.floor((double) (adjustedPosition / f4))) * f4;
            float f5 = (adjustedPosition % f4) / f4;
            if (!Float.isNaN(this.mQuantizeMotionPhase)) {
                f5 = (this.mQuantizeMotionPhase + f5) % 1.0f;
            }
            Interpolator interpolator = this.mQuantizeMotionInterpolator;
            if (interpolator != null) {
                f2 = interpolator.getInterpolation(f5);
            } else {
                f2 = ((double) f5) > 0.5d ? 1.0f : 0.0f;
            }
            f = (f2 * f4) + floor;
        } else {
            f = adjustedPosition;
        }
        HashMap<String, ViewSpline> hashMap = this.mAttributesMap;
        if (hashMap != null) {
            for (ViewSpline property : hashMap.values()) {
                property.setProperty(view, f);
            }
        }
        HashMap<String, ViewTimeCycle> hashMap2 = this.mTimeCycleAttributesMap;
        if (hashMap2 != null) {
            boolean z2 = false;
            ViewTimeCycle.PathRotate pathRotate2 = null;
            for (ViewTimeCycle next : hashMap2.values()) {
                if (next instanceof ViewTimeCycle.PathRotate) {
                    pathRotate2 = next;
                } else {
                    z2 |= next.setProperty(child, f, time, keyCache);
                }
            }
            z = z2;
            pathRotate = pathRotate2;
        } else {
            z = false;
            pathRotate = null;
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
                this.mStartMotionPath.setView(f, child, this.mInterpolateVariables, this.mInterpolateData, this.mInterpolateVelocity, (double[]) null, this.mForceMeasure);
                this.mForceMeasure = false;
            }
            if (this.mTransformPivotTarget != Key.UNSET) {
                if (this.mTransformPivotView == null) {
                    this.mTransformPivotView = ((View) child.getParent()).findViewById(this.mTransformPivotTarget);
                }
                View view2 = this.mTransformPivotView;
                if (view2 != null) {
                    float top = ((float) (view2.getTop() + this.mTransformPivotView.getBottom())) / 2.0f;
                    float left = ((float) (this.mTransformPivotView.getLeft() + this.mTransformPivotView.getRight())) / 2.0f;
                    if (child.getRight() - child.getLeft() > 0 && child.getBottom() - child.getTop() > 0) {
                        view.setPivotX(left - ((float) child.getLeft()));
                        view.setPivotY(top - ((float) child.getTop()));
                    }
                }
            }
            HashMap<String, ViewSpline> hashMap3 = this.mAttributesMap;
            if (hashMap3 != null) {
                for (SplineSet next2 : hashMap3.values()) {
                    if (next2 instanceof ViewSpline.PathRotate) {
                        double[] dArr2 = this.mInterpolateVelocity;
                        if (dArr2.length > 1) {
                            SplineSet splineSet = next2;
                            ((ViewSpline.PathRotate) next2).setPathRotate(child, f, dArr2[0], dArr2[1]);
                        }
                    }
                    SplineSet splineSet2 = next2;
                }
            }
            if (pathRotate != null) {
                double[] dArr3 = this.mInterpolateVelocity;
                c = 1;
                z |= pathRotate.setPathRotate(child, keyCache, f, time, dArr3[0], dArr3[1]);
            } else {
                c = 1;
            }
            int i = 1;
            while (true) {
                CurveFit[] curveFitArr2 = this.mSpline;
                if (i >= curveFitArr2.length) {
                    break;
                }
                curveFitArr2[i].getPos((double) f, this.mValuesBuff);
                CustomSupport.setInterpolatedValue(this.mStartMotionPath.attributes.get(this.mAttributeNames[i - 1]), view, this.mValuesBuff);
                i++;
            }
            if (this.mStartPoint.mVisibilityMode == 0) {
                if (f <= 0.0f) {
                    view.setVisibility(this.mStartPoint.visibility);
                } else if (f >= 1.0f) {
                    view.setVisibility(this.mEndPoint.visibility);
                } else if (this.mEndPoint.visibility != this.mStartPoint.visibility) {
                    view.setVisibility(0);
                }
            }
            if (this.mKeyTriggers != null) {
                int i2 = 0;
                while (true) {
                    KeyTrigger[] keyTriggerArr = this.mKeyTriggers;
                    if (i2 >= keyTriggerArr.length) {
                        break;
                    }
                    keyTriggerArr[i2].conditionallyFire(f, view);
                    i2++;
                }
            }
        } else {
            c = 1;
            float f6 = this.mStartMotionPath.x + ((this.mEndMotionPath.x - this.mStartMotionPath.x) * f);
            float f7 = this.mStartMotionPath.y + ((this.mEndMotionPath.y - this.mStartMotionPath.y) * f);
            int i3 = (int) (f6 + 0.5f);
            int i4 = (int) (f7 + 0.5f);
            int i5 = (int) (f6 + 0.5f + this.mStartMotionPath.width + ((this.mEndMotionPath.width - this.mStartMotionPath.width) * f));
            int i6 = (int) (0.5f + f7 + this.mStartMotionPath.height + ((this.mEndMotionPath.height - this.mStartMotionPath.height) * f));
            int i7 = i5 - i3;
            int i8 = i6 - i4;
            if (this.mEndMotionPath.width == this.mStartMotionPath.width && this.mEndMotionPath.height == this.mStartMotionPath.height && !this.mForceMeasure) {
                float f8 = f6;
            } else {
                view.measure(View.MeasureSpec.makeMeasureSpec(i7, BasicMeasure.EXACTLY), View.MeasureSpec.makeMeasureSpec(i8, BasicMeasure.EXACTLY));
                float f9 = f6;
                this.mForceMeasure = false;
            }
            view.layout(i3, i4, i5, i6);
        }
        HashMap<String, ViewOscillator> hashMap4 = this.mCycleMap;
        if (hashMap4 != null) {
            for (ViewOscillator next3 : hashMap4.values()) {
                if (next3 instanceof ViewOscillator.PathRotateSet) {
                    double[] dArr4 = this.mInterpolateVelocity;
                    ((ViewOscillator.PathRotateSet) next3).setPathRotate(child, f, dArr4[0], dArr4[c]);
                } else {
                    next3.setProperty(view, f);
                }
            }
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public String name() {
        return this.mView.getContext().getResources().getResourceEntryName(this.mView.getId());
    }

    /* access modifiers changed from: package-private */
    public void positionKeyframe(View view, KeyPositionBase key, float x, float y, String[] attribute, float[] value) {
        RectF rectF = new RectF();
        rectF.left = this.mStartMotionPath.x;
        rectF.top = this.mStartMotionPath.y;
        rectF.right = rectF.left + this.mStartMotionPath.width;
        rectF.bottom = rectF.top + this.mStartMotionPath.height;
        RectF rectF2 = new RectF();
        rectF2.left = this.mEndMotionPath.x;
        rectF2.top = this.mEndMotionPath.y;
        rectF2.right = rectF2.left + this.mEndMotionPath.width;
        rectF2.bottom = rectF2.top + this.mEndMotionPath.height;
        key.positionAttributes(view, rectF, rectF2, x, y, attribute, value);
    }

    public void remeasure() {
        this.mForceMeasure = true;
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
    public void setBothStates(View v) {
        this.mStartMotionPath.time = 0.0f;
        this.mStartMotionPath.position = 0.0f;
        this.mNoMovement = true;
        this.mStartMotionPath.setBounds(v.getX(), v.getY(), (float) v.getWidth(), (float) v.getHeight());
        this.mEndMotionPath.setBounds(v.getX(), v.getY(), (float) v.getWidth(), (float) v.getHeight());
        this.mStartPoint.setState(v);
        this.mEndPoint.setState(v);
    }

    public void setDrawPath(int debugMode) {
        this.mStartMotionPath.mDrawPath = debugMode;
    }

    /* access modifiers changed from: package-private */
    public void setEndState(Rect cw, ConstraintSet constraintSet, int parentWidth, int parentHeight) {
        int i = constraintSet.mRotate;
        if (i != 0) {
            rotate(cw, this.mTempRect, i, parentWidth, parentHeight);
            cw = this.mTempRect;
        }
        this.mEndMotionPath.time = 1.0f;
        this.mEndMotionPath.position = 1.0f;
        readView(this.mEndMotionPath);
        this.mEndMotionPath.setBounds((float) cw.left, (float) cw.top, (float) cw.width(), (float) cw.height());
        this.mEndMotionPath.applyParameters(constraintSet.getParameters(this.mId));
        this.mEndPoint.setState(cw, constraintSet, i, this.mId);
    }

    public void setPathMotionArc(int arc) {
        this.mPathMotionArc = arc;
    }

    /* access modifiers changed from: package-private */
    public void setStartCurrentState(View v) {
        this.mStartMotionPath.time = 0.0f;
        this.mStartMotionPath.position = 0.0f;
        this.mStartMotionPath.setBounds(v.getX(), v.getY(), (float) v.getWidth(), (float) v.getHeight());
        this.mStartPoint.setState(v);
    }

    /* access modifiers changed from: package-private */
    public void setStartState(Rect cw, ConstraintSet constraintSet, int parentWidth, int parentHeight) {
        int i = constraintSet.mRotate;
        if (i != 0) {
            rotate(cw, this.mTempRect, i, parentWidth, parentHeight);
        }
        this.mStartMotionPath.time = 0.0f;
        this.mStartMotionPath.position = 0.0f;
        readView(this.mStartMotionPath);
        this.mStartMotionPath.setBounds((float) cw.left, (float) cw.top, (float) cw.width(), (float) cw.height());
        ConstraintSet.Constraint parameters = constraintSet.getParameters(this.mId);
        this.mStartMotionPath.applyParameters(parameters);
        this.mMotionStagger = parameters.motion.mMotionStagger;
        this.mStartPoint.setState(cw, constraintSet, i, this.mId);
        this.mTransformPivotTarget = parameters.transform.transformPivotTarget;
        this.mQuantizeMotionSteps = parameters.motion.mQuantizeMotionSteps;
        this.mQuantizeMotionPhase = parameters.motion.mQuantizeMotionPhase;
        this.mQuantizeMotionInterpolator = getInterpolator(this.mView.getContext(), parameters.motion.mQuantizeInterpolatorType, parameters.motion.mQuantizeInterpolatorString, parameters.motion.mQuantizeInterpolatorID);
    }

    public void setStartState(ViewState rect, View v, int rotation, int preWidth, int preHeight) {
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

    public void setView(View view) {
        this.mView = view;
        this.mId = view.getId();
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ConstraintLayout.LayoutParams) {
            this.mConstraintTag = ((ConstraintLayout.LayoutParams) layoutParams).getConstraintTag();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v41, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v23, resolved type: double[][]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setup(int r29, int r30, float r31, long r32) {
        /*
            r28 = this;
            r0 = r28
            java.util.HashSet r1 = new java.util.HashSet
            r1.<init>()
            java.util.HashSet r2 = new java.util.HashSet
            r2.<init>()
            java.util.HashSet r3 = new java.util.HashSet
            r3.<init>()
            java.util.HashSet r4 = new java.util.HashSet
            r4.<init>()
            java.util.HashMap r5 = new java.util.HashMap
            r5.<init>()
            r6 = 0
            int r7 = r0.mPathMotionArc
            int r8 = androidx.constraintlayout.motion.widget.Key.UNSET
            if (r7 == r8) goto L_0x0028
            androidx.constraintlayout.motion.widget.MotionPaths r7 = r0.mStartMotionPath
            int r8 = r0.mPathMotionArc
            r7.mPathMotionArc = r8
        L_0x0028:
            androidx.constraintlayout.motion.widget.MotionConstrainedPoint r7 = r0.mStartPoint
            androidx.constraintlayout.motion.widget.MotionConstrainedPoint r8 = r0.mEndPoint
            r7.different(r8, r3)
            java.util.ArrayList<androidx.constraintlayout.motion.widget.Key> r7 = r0.mKeyList
            if (r7 == 0) goto L_0x009f
            java.util.Iterator r7 = r7.iterator()
        L_0x0037:
            boolean r8 = r7.hasNext()
            if (r8 == 0) goto L_0x009c
            java.lang.Object r8 = r7.next()
            androidx.constraintlayout.motion.widget.Key r8 = (androidx.constraintlayout.motion.widget.Key) r8
            boolean r9 = r8 instanceof androidx.constraintlayout.motion.widget.KeyPosition
            if (r9 == 0) goto L_0x006e
            r9 = r8
            androidx.constraintlayout.motion.widget.KeyPosition r9 = (androidx.constraintlayout.motion.widget.KeyPosition) r9
            androidx.constraintlayout.motion.widget.MotionPaths r15 = new androidx.constraintlayout.motion.widget.MotionPaths
            androidx.constraintlayout.motion.widget.MotionPaths r14 = r0.mStartMotionPath
            androidx.constraintlayout.motion.widget.MotionPaths r13 = r0.mEndMotionPath
            r10 = r15
            r11 = r29
            r12 = r30
            r16 = r13
            r13 = r9
            r17 = r1
            r1 = r15
            r15 = r16
            r10.<init>(r11, r12, r13, r14, r15)
            r0.insertKey(r1)
            int r1 = r9.mCurveFit
            int r10 = androidx.constraintlayout.motion.widget.Key.UNSET
            if (r1 == r10) goto L_0x006d
            int r1 = r9.mCurveFit
            r0.mCurveFitType = r1
        L_0x006d:
            goto L_0x0099
        L_0x006e:
            r17 = r1
            boolean r1 = r8 instanceof androidx.constraintlayout.motion.widget.KeyCycle
            if (r1 == 0) goto L_0x0078
            r8.getAttributeNames(r4)
            goto L_0x0099
        L_0x0078:
            boolean r1 = r8 instanceof androidx.constraintlayout.motion.widget.KeyTimeCycle
            if (r1 == 0) goto L_0x0080
            r8.getAttributeNames(r2)
            goto L_0x0099
        L_0x0080:
            boolean r1 = r8 instanceof androidx.constraintlayout.motion.widget.KeyTrigger
            if (r1 == 0) goto L_0x0093
            if (r6 != 0) goto L_0x008c
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r6 = r1
        L_0x008c:
            r1 = r8
            androidx.constraintlayout.motion.widget.KeyTrigger r1 = (androidx.constraintlayout.motion.widget.KeyTrigger) r1
            r6.add(r1)
            goto L_0x0099
        L_0x0093:
            r8.setInterpolation(r5)
            r8.getAttributeNames(r3)
        L_0x0099:
            r1 = r17
            goto L_0x0037
        L_0x009c:
            r17 = r1
            goto L_0x00a1
        L_0x009f:
            r17 = r1
        L_0x00a1:
            r1 = 0
            if (r6 == 0) goto L_0x00ae
            androidx.constraintlayout.motion.widget.KeyTrigger[] r7 = new androidx.constraintlayout.motion.widget.KeyTrigger[r1]
            java.lang.Object[] r7 = r6.toArray(r7)
            androidx.constraintlayout.motion.widget.KeyTrigger[] r7 = (androidx.constraintlayout.motion.widget.KeyTrigger[]) r7
            r0.mKeyTriggers = r7
        L_0x00ae:
            boolean r7 = r3.isEmpty()
            java.lang.String r8 = ","
            java.lang.String r9 = "CUSTOM,"
            r10 = 1
            if (r7 != 0) goto L_0x018a
            java.util.HashMap r7 = new java.util.HashMap
            r7.<init>()
            r0.mAttributesMap = r7
            java.util.Iterator r7 = r3.iterator()
        L_0x00c4:
            boolean r11 = r7.hasNext()
            if (r11 == 0) goto L_0x0124
            java.lang.Object r11 = r7.next()
            java.lang.String r11 = (java.lang.String) r11
            boolean r12 = r11.startsWith(r9)
            if (r12 == 0) goto L_0x0110
            android.util.SparseArray r12 = new android.util.SparseArray
            r12.<init>()
            java.lang.String[] r13 = r11.split(r8)
            r13 = r13[r10]
            java.util.ArrayList<androidx.constraintlayout.motion.widget.Key> r14 = r0.mKeyList
            java.util.Iterator r14 = r14.iterator()
        L_0x00e7:
            boolean r15 = r14.hasNext()
            if (r15 == 0) goto L_0x010b
            java.lang.Object r15 = r14.next()
            androidx.constraintlayout.motion.widget.Key r15 = (androidx.constraintlayout.motion.widget.Key) r15
            java.util.HashMap<java.lang.String, androidx.constraintlayout.widget.ConstraintAttribute> r10 = r15.mCustomConstraints
            if (r10 != 0) goto L_0x00f9
            r10 = 1
            goto L_0x00e7
        L_0x00f9:
            java.util.HashMap<java.lang.String, androidx.constraintlayout.widget.ConstraintAttribute> r10 = r15.mCustomConstraints
            java.lang.Object r10 = r10.get(r13)
            androidx.constraintlayout.widget.ConstraintAttribute r10 = (androidx.constraintlayout.widget.ConstraintAttribute) r10
            if (r10 == 0) goto L_0x0108
            int r1 = r15.mFramePosition
            r12.append(r1, r10)
        L_0x0108:
            r1 = 0
            r10 = 1
            goto L_0x00e7
        L_0x010b:
            androidx.constraintlayout.motion.utils.ViewSpline r1 = androidx.constraintlayout.motion.utils.ViewSpline.makeCustomSpline(r11, r12)
            goto L_0x0114
        L_0x0110:
            androidx.constraintlayout.motion.utils.ViewSpline r1 = androidx.constraintlayout.motion.utils.ViewSpline.makeSpline(r11)
        L_0x0114:
            if (r1 != 0) goto L_0x0119
            r1 = 0
            r10 = 1
            goto L_0x00c4
        L_0x0119:
            r1.setType(r11)
            java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.utils.ViewSpline> r10 = r0.mAttributesMap
            r10.put(r11, r1)
            r1 = 0
            r10 = 1
            goto L_0x00c4
        L_0x0124:
            java.util.ArrayList<androidx.constraintlayout.motion.widget.Key> r1 = r0.mKeyList
            if (r1 == 0) goto L_0x0142
            java.util.Iterator r1 = r1.iterator()
        L_0x012c:
            boolean r7 = r1.hasNext()
            if (r7 == 0) goto L_0x0142
            java.lang.Object r7 = r1.next()
            androidx.constraintlayout.motion.widget.Key r7 = (androidx.constraintlayout.motion.widget.Key) r7
            boolean r10 = r7 instanceof androidx.constraintlayout.motion.widget.KeyAttributes
            if (r10 == 0) goto L_0x0141
            java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.utils.ViewSpline> r10 = r0.mAttributesMap
            r7.addValues(r10)
        L_0x0141:
            goto L_0x012c
        L_0x0142:
            androidx.constraintlayout.motion.widget.MotionConstrainedPoint r1 = r0.mStartPoint
            java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.utils.ViewSpline> r7 = r0.mAttributesMap
            r10 = 0
            r1.addValues(r7, r10)
            androidx.constraintlayout.motion.widget.MotionConstrainedPoint r1 = r0.mEndPoint
            java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.utils.ViewSpline> r7 = r0.mAttributesMap
            r10 = 100
            r1.addValues(r7, r10)
            java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.utils.ViewSpline> r1 = r0.mAttributesMap
            java.util.Set r1 = r1.keySet()
            java.util.Iterator r1 = r1.iterator()
        L_0x015d:
            boolean r7 = r1.hasNext()
            if (r7 == 0) goto L_0x018a
            java.lang.Object r7 = r1.next()
            java.lang.String r7 = (java.lang.String) r7
            r10 = 0
            boolean r11 = r5.containsKey(r7)
            if (r11 == 0) goto L_0x017c
            java.lang.Object r11 = r5.get(r7)
            java.lang.Integer r11 = (java.lang.Integer) r11
            if (r11 == 0) goto L_0x017c
            int r10 = r11.intValue()
        L_0x017c:
            java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.utils.ViewSpline> r11 = r0.mAttributesMap
            java.lang.Object r11 = r11.get(r7)
            androidx.constraintlayout.core.motion.utils.SplineSet r11 = (androidx.constraintlayout.core.motion.utils.SplineSet) r11
            if (r11 == 0) goto L_0x0189
            r11.setup(r10)
        L_0x0189:
            goto L_0x015d
        L_0x018a:
            boolean r1 = r2.isEmpty()
            if (r1 != 0) goto L_0x026c
            java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.utils.ViewTimeCycle> r1 = r0.mTimeCycleAttributesMap
            if (r1 != 0) goto L_0x019b
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
            r0.mTimeCycleAttributesMap = r1
        L_0x019b:
            java.util.Iterator r1 = r2.iterator()
        L_0x019f:
            boolean r7 = r1.hasNext()
            if (r7 == 0) goto L_0x0216
            java.lang.Object r7 = r1.next()
            java.lang.String r7 = (java.lang.String) r7
            java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.utils.ViewTimeCycle> r10 = r0.mTimeCycleAttributesMap
            boolean r10 = r10.containsKey(r7)
            if (r10 == 0) goto L_0x01b4
            goto L_0x019f
        L_0x01b4:
            r10 = 0
            boolean r11 = r7.startsWith(r9)
            if (r11 == 0) goto L_0x01fe
            android.util.SparseArray r11 = new android.util.SparseArray
            r11.<init>()
            java.lang.String[] r12 = r7.split(r8)
            r13 = 1
            r12 = r12[r13]
            java.util.ArrayList<androidx.constraintlayout.motion.widget.Key> r13 = r0.mKeyList
            java.util.Iterator r13 = r13.iterator()
        L_0x01cd:
            boolean r14 = r13.hasNext()
            if (r14 == 0) goto L_0x01f5
            java.lang.Object r14 = r13.next()
            androidx.constraintlayout.motion.widget.Key r14 = (androidx.constraintlayout.motion.widget.Key) r14
            java.util.HashMap<java.lang.String, androidx.constraintlayout.widget.ConstraintAttribute> r15 = r14.mCustomConstraints
            if (r15 != 0) goto L_0x01de
            goto L_0x01cd
        L_0x01de:
            java.util.HashMap<java.lang.String, androidx.constraintlayout.widget.ConstraintAttribute> r15 = r14.mCustomConstraints
            java.lang.Object r15 = r15.get(r12)
            androidx.constraintlayout.widget.ConstraintAttribute r15 = (androidx.constraintlayout.widget.ConstraintAttribute) r15
            if (r15 == 0) goto L_0x01f0
            r18 = r1
            int r1 = r14.mFramePosition
            r11.append(r1, r15)
            goto L_0x01f2
        L_0x01f0:
            r18 = r1
        L_0x01f2:
            r1 = r18
            goto L_0x01cd
        L_0x01f5:
            r18 = r1
            androidx.constraintlayout.motion.utils.ViewTimeCycle r1 = androidx.constraintlayout.motion.utils.ViewTimeCycle.makeCustomSpline(r7, r11)
            r11 = r32
            goto L_0x0206
        L_0x01fe:
            r18 = r1
            r11 = r32
            androidx.constraintlayout.motion.utils.ViewTimeCycle r1 = androidx.constraintlayout.motion.utils.ViewTimeCycle.makeSpline(r7, r11)
        L_0x0206:
            if (r1 != 0) goto L_0x020b
            r1 = r18
            goto L_0x019f
        L_0x020b:
            r1.setType(r7)
            java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.utils.ViewTimeCycle> r10 = r0.mTimeCycleAttributesMap
            r10.put(r7, r1)
            r1 = r18
            goto L_0x019f
        L_0x0216:
            r11 = r32
            java.util.ArrayList<androidx.constraintlayout.motion.widget.Key> r1 = r0.mKeyList
            if (r1 == 0) goto L_0x0239
            java.util.Iterator r1 = r1.iterator()
        L_0x0220:
            boolean r7 = r1.hasNext()
            if (r7 == 0) goto L_0x0239
            java.lang.Object r7 = r1.next()
            androidx.constraintlayout.motion.widget.Key r7 = (androidx.constraintlayout.motion.widget.Key) r7
            boolean r8 = r7 instanceof androidx.constraintlayout.motion.widget.KeyTimeCycle
            if (r8 == 0) goto L_0x0238
            r8 = r7
            androidx.constraintlayout.motion.widget.KeyTimeCycle r8 = (androidx.constraintlayout.motion.widget.KeyTimeCycle) r8
            java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.utils.ViewTimeCycle> r10 = r0.mTimeCycleAttributesMap
            r8.addTimeValues(r10)
        L_0x0238:
            goto L_0x0220
        L_0x0239:
            java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.utils.ViewTimeCycle> r1 = r0.mTimeCycleAttributesMap
            java.util.Set r1 = r1.keySet()
            java.util.Iterator r1 = r1.iterator()
        L_0x0243:
            boolean r7 = r1.hasNext()
            if (r7 == 0) goto L_0x026e
            java.lang.Object r7 = r1.next()
            java.lang.String r7 = (java.lang.String) r7
            r8 = 0
            boolean r10 = r5.containsKey(r7)
            if (r10 == 0) goto L_0x0260
            java.lang.Object r10 = r5.get(r7)
            java.lang.Integer r10 = (java.lang.Integer) r10
            int r8 = r10.intValue()
        L_0x0260:
            java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.utils.ViewTimeCycle> r10 = r0.mTimeCycleAttributesMap
            java.lang.Object r10 = r10.get(r7)
            androidx.constraintlayout.motion.utils.ViewTimeCycle r10 = (androidx.constraintlayout.motion.utils.ViewTimeCycle) r10
            r10.setup(r8)
            goto L_0x0243
        L_0x026c:
            r11 = r32
        L_0x026e:
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionPaths> r1 = r0.mMotionPaths
            int r1 = r1.size()
            r7 = 2
            int r1 = r1 + r7
            androidx.constraintlayout.motion.widget.MotionPaths[] r1 = new androidx.constraintlayout.motion.widget.MotionPaths[r1]
            r8 = 1
            androidx.constraintlayout.motion.widget.MotionPaths r10 = r0.mStartMotionPath
            r13 = 0
            r1[r13] = r10
            int r10 = r1.length
            r13 = 1
            int r10 = r10 - r13
            androidx.constraintlayout.motion.widget.MotionPaths r13 = r0.mEndMotionPath
            r1[r10] = r13
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionPaths> r10 = r0.mMotionPaths
            int r10 = r10.size()
            if (r10 <= 0) goto L_0x0295
            int r10 = r0.mCurveFitType
            r13 = -1
            if (r10 != r13) goto L_0x0295
            r10 = 0
            r0.mCurveFitType = r10
        L_0x0295:
            java.util.ArrayList<androidx.constraintlayout.motion.widget.MotionPaths> r10 = r0.mMotionPaths
            java.util.Iterator r10 = r10.iterator()
        L_0x029b:
            boolean r13 = r10.hasNext()
            if (r13 == 0) goto L_0x02ad
            java.lang.Object r13 = r10.next()
            androidx.constraintlayout.motion.widget.MotionPaths r13 = (androidx.constraintlayout.motion.widget.MotionPaths) r13
            int r14 = r8 + 1
            r1[r8] = r13
            r8 = r14
            goto L_0x029b
        L_0x02ad:
            r10 = 18
            java.util.HashSet r13 = new java.util.HashSet
            r13.<init>()
            androidx.constraintlayout.motion.widget.MotionPaths r14 = r0.mEndMotionPath
            java.util.LinkedHashMap<java.lang.String, androidx.constraintlayout.widget.ConstraintAttribute> r14 = r14.attributes
            java.util.Set r14 = r14.keySet()
            java.util.Iterator r14 = r14.iterator()
        L_0x02c0:
            boolean r15 = r14.hasNext()
            if (r15 == 0) goto L_0x02f2
            java.lang.Object r15 = r14.next()
            java.lang.String r15 = (java.lang.String) r15
            androidx.constraintlayout.motion.widget.MotionPaths r7 = r0.mStartMotionPath
            java.util.LinkedHashMap<java.lang.String, androidx.constraintlayout.widget.ConstraintAttribute> r7 = r7.attributes
            boolean r7 = r7.containsKey(r15)
            if (r7 == 0) goto L_0x02f0
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.StringBuilder r7 = r7.append(r9)
            java.lang.StringBuilder r7 = r7.append(r15)
            java.lang.String r7 = r7.toString()
            boolean r7 = r3.contains(r7)
            if (r7 != 0) goto L_0x02f0
            r13.add(r15)
        L_0x02f0:
            r7 = 2
            goto L_0x02c0
        L_0x02f2:
            r7 = 0
            java.lang.String[] r9 = new java.lang.String[r7]
            java.lang.Object[] r7 = r13.toArray(r9)
            java.lang.String[] r7 = (java.lang.String[]) r7
            r0.mAttributeNames = r7
            int r7 = r7.length
            int[] r7 = new int[r7]
            r0.mAttributeInterpolatorCount = r7
            r7 = 0
        L_0x0303:
            java.lang.String[] r9 = r0.mAttributeNames
            int r14 = r9.length
            if (r7 >= r14) goto L_0x0349
            r9 = r9[r7]
            int[] r14 = r0.mAttributeInterpolatorCount
            r15 = 0
            r14[r7] = r15
            r14 = 0
        L_0x0310:
            int r15 = r1.length
            if (r14 >= r15) goto L_0x0342
            r15 = r1[r14]
            java.util.LinkedHashMap<java.lang.String, androidx.constraintlayout.widget.ConstraintAttribute> r15 = r15.attributes
            boolean r15 = r15.containsKey(r9)
            if (r15 == 0) goto L_0x033b
            r15 = r1[r14]
            java.util.LinkedHashMap<java.lang.String, androidx.constraintlayout.widget.ConstraintAttribute> r15 = r15.attributes
            java.lang.Object r15 = r15.get(r9)
            androidx.constraintlayout.widget.ConstraintAttribute r15 = (androidx.constraintlayout.widget.ConstraintAttribute) r15
            if (r15 == 0) goto L_0x0338
            r19 = r2
            int[] r2 = r0.mAttributeInterpolatorCount
            r20 = r2[r7]
            int r21 = r15.numberOfInterpolatedValues()
            int r20 = r20 + r21
            r2[r7] = r20
            goto L_0x0344
        L_0x0338:
            r19 = r2
            goto L_0x033d
        L_0x033b:
            r19 = r2
        L_0x033d:
            int r14 = r14 + 1
            r2 = r19
            goto L_0x0310
        L_0x0342:
            r19 = r2
        L_0x0344:
            int r7 = r7 + 1
            r2 = r19
            goto L_0x0303
        L_0x0349:
            r19 = r2
            r2 = 0
            r7 = r1[r2]
            int r2 = r7.mPathMotionArc
            int r7 = androidx.constraintlayout.motion.widget.Key.UNSET
            if (r2 == r7) goto L_0x0356
            r2 = 1
            goto L_0x0357
        L_0x0356:
            r2 = 0
        L_0x0357:
            java.lang.String[] r7 = r0.mAttributeNames
            int r7 = r7.length
            int r7 = r7 + r10
            boolean[] r7 = new boolean[r7]
            r9 = 1
        L_0x035e:
            int r14 = r1.length
            if (r9 >= r14) goto L_0x0373
            r14 = r1[r9]
            int r15 = r9 + -1
            r15 = r1[r15]
            r20 = r3
            java.lang.String[] r3 = r0.mAttributeNames
            r14.different(r15, r7, r3, r2)
            int r9 = r9 + 1
            r3 = r20
            goto L_0x035e
        L_0x0373:
            r20 = r3
            r3 = 0
            r8 = 1
        L_0x0377:
            int r9 = r7.length
            if (r8 >= r9) goto L_0x0383
            boolean r9 = r7[r8]
            if (r9 == 0) goto L_0x0380
            int r3 = r3 + 1
        L_0x0380:
            int r8 = r8 + 1
            goto L_0x0377
        L_0x0383:
            int[] r8 = new int[r3]
            r0.mInterpolateVariables = r8
            r8 = 2
            int r9 = java.lang.Math.max(r8, r3)
            double[] r8 = new double[r9]
            r0.mInterpolateData = r8
            double[] r8 = new double[r9]
            r0.mInterpolateVelocity = r8
            r3 = 0
            r8 = 1
        L_0x0396:
            int r14 = r7.length
            if (r8 >= r14) goto L_0x03a7
            boolean r14 = r7[r8]
            if (r14 == 0) goto L_0x03a4
            int[] r14 = r0.mInterpolateVariables
            int r15 = r3 + 1
            r14[r3] = r8
            r3 = r15
        L_0x03a4:
            int r8 = r8 + 1
            goto L_0x0396
        L_0x03a7:
            int r8 = r1.length
            int[] r14 = r0.mInterpolateVariables
            int r14 = r14.length
            r21 = r2
            r15 = 2
            int[] r2 = new int[r15]
            r15 = 1
            r2[r15] = r14
            r14 = 0
            r2[r14] = r8
            java.lang.Class r8 = java.lang.Double.TYPE
            java.lang.Object r2 = java.lang.reflect.Array.newInstance(r8, r2)
            double[][] r2 = (double[][]) r2
            int r8 = r1.length
            double[] r8 = new double[r8]
            r14 = 0
        L_0x03c2:
            int r15 = r1.length
            if (r14 >= r15) goto L_0x03e2
            r15 = r1[r14]
            r22 = r3
            r3 = r2[r14]
            r23 = r5
            int[] r5 = r0.mInterpolateVariables
            r15.fillStandard(r3, r5)
            r3 = r1[r14]
            float r3 = r3.time
            r15 = r6
            double r5 = (double) r3
            r8[r14] = r5
            int r14 = r14 + 1
            r6 = r15
            r3 = r22
            r5 = r23
            goto L_0x03c2
        L_0x03e2:
            r22 = r3
            r23 = r5
            r15 = r6
            r3 = 0
        L_0x03e8:
            int[] r5 = r0.mInterpolateVariables
            int r6 = r5.length
            if (r3 >= r6) goto L_0x0447
            r5 = r5[r3]
            java.lang.String[] r6 = androidx.constraintlayout.motion.widget.MotionPaths.names
            int r6 = r6.length
            if (r5 >= r6) goto L_0x043a
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String[] r14 = androidx.constraintlayout.motion.widget.MotionPaths.names
            r24 = r5
            int[] r5 = r0.mInterpolateVariables
            r5 = r5[r3]
            r5 = r14[r5]
            java.lang.StringBuilder r5 = r6.append(r5)
            java.lang.String r6 = " ["
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r5 = r5.toString()
            r6 = 0
        L_0x0412:
            int r14 = r1.length
            if (r6 >= r14) goto L_0x0435
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.StringBuilder r14 = r14.append(r5)
            r25 = r2[r6]
            r27 = r9
            r26 = r10
            r9 = r25[r3]
            java.lang.StringBuilder r9 = r14.append(r9)
            java.lang.String r5 = r9.toString()
            int r6 = r6 + 1
            r10 = r26
            r9 = r27
            goto L_0x0412
        L_0x0435:
            r27 = r9
            r26 = r10
            goto L_0x0440
        L_0x043a:
            r24 = r5
            r27 = r9
            r26 = r10
        L_0x0440:
            int r3 = r3 + 1
            r10 = r26
            r9 = r27
            goto L_0x03e8
        L_0x0447:
            r27 = r9
            r26 = r10
            java.lang.String[] r3 = r0.mAttributeNames
            int r3 = r3.length
            r5 = 1
            int r3 = r3 + r5
            androidx.constraintlayout.core.motion.utils.CurveFit[] r3 = new androidx.constraintlayout.core.motion.utils.CurveFit[r3]
            r0.mSpline = r3
            r3 = 0
        L_0x0455:
            java.lang.String[] r5 = r0.mAttributeNames
            int r6 = r5.length
            if (r3 >= r6) goto L_0x04c5
            r6 = 0
            r9 = 0
            r10 = 0
            r5 = r5[r3]
            r14 = 0
        L_0x0460:
            r24 = r7
            int r7 = r1.length
            if (r14 >= r7) goto L_0x04a7
            r7 = r1[r14]
            boolean r7 = r7.hasCustomData(r5)
            if (r7 == 0) goto L_0x04a0
            if (r9 != 0) goto L_0x048f
            int r7 = r1.length
            double[] r10 = new double[r7]
            int r7 = r1.length
            r25 = r10
            r10 = r1[r14]
            int r10 = r10.getCustomDataCount(r5)
            r11 = 2
            int[] r12 = new int[r11]
            r11 = 1
            r12[r11] = r10
            r10 = 0
            r12[r10] = r7
            java.lang.Class r7 = java.lang.Double.TYPE
            java.lang.Object r7 = java.lang.reflect.Array.newInstance(r7, r12)
            r9 = r7
            double[][] r9 = (double[][]) r9
            r10 = r25
        L_0x048f:
            r7 = r1[r14]
            float r7 = r7.time
            double r11 = (double) r7
            r10[r6] = r11
            r7 = r1[r14]
            r11 = r9[r6]
            r12 = 0
            r7.getCustomData(r5, r11, r12)
            int r6 = r6 + 1
        L_0x04a0:
            int r14 = r14 + 1
            r11 = r32
            r7 = r24
            goto L_0x0460
        L_0x04a7:
            double[] r7 = java.util.Arrays.copyOf(r10, r6)
            java.lang.Object[] r10 = java.util.Arrays.copyOf(r9, r6)
            r9 = r10
            double[][] r9 = (double[][]) r9
            androidx.constraintlayout.core.motion.utils.CurveFit[] r10 = r0.mSpline
            int r11 = r3 + 1
            int r12 = r0.mCurveFitType
            androidx.constraintlayout.core.motion.utils.CurveFit r12 = androidx.constraintlayout.core.motion.utils.CurveFit.get(r12, r7, r9)
            r10[r11] = r12
            int r3 = r3 + 1
            r11 = r32
            r7 = r24
            goto L_0x0455
        L_0x04c5:
            r24 = r7
            androidx.constraintlayout.core.motion.utils.CurveFit[] r3 = r0.mSpline
            int r5 = r0.mCurveFitType
            androidx.constraintlayout.core.motion.utils.CurveFit r5 = androidx.constraintlayout.core.motion.utils.CurveFit.get(r5, r8, r2)
            r6 = 0
            r3[r6] = r5
            r3 = r1[r6]
            int r3 = r3.mPathMotionArc
            int r5 = androidx.constraintlayout.motion.widget.Key.UNSET
            if (r3 == r5) goto L_0x051e
            int r3 = r1.length
            int[] r5 = new int[r3]
            double[] r6 = new double[r3]
            r7 = 2
            int[] r9 = new int[r7]
            r10 = 1
            r9[r10] = r7
            r7 = 0
            r9[r7] = r3
            java.lang.Class r7 = java.lang.Double.TYPE
            java.lang.Object r7 = java.lang.reflect.Array.newInstance(r7, r9)
            double[][] r7 = (double[][]) r7
            r9 = 0
        L_0x04f1:
            if (r9 >= r3) goto L_0x0518
            r10 = r1[r9]
            int r10 = r10.mPathMotionArc
            r5[r9] = r10
            r10 = r1[r9]
            float r10 = r10.time
            double r10 = (double) r10
            r6[r9] = r10
            r10 = r7[r9]
            r11 = r1[r9]
            float r11 = r11.x
            double r11 = (double) r11
            r14 = 0
            r10[r14] = r11
            r10 = r7[r9]
            r11 = r1[r9]
            float r11 = r11.y
            double r11 = (double) r11
            r16 = 1
            r10[r16] = r11
            int r9 = r9 + 1
            goto L_0x04f1
        L_0x0518:
            androidx.constraintlayout.core.motion.utils.CurveFit r9 = androidx.constraintlayout.core.motion.utils.CurveFit.getArc(r5, r6, r7)
            r0.mArcSpline = r9
        L_0x051e:
            r3 = 2143289344(0x7fc00000, float:NaN)
            java.util.HashMap r5 = new java.util.HashMap
            r5.<init>()
            r0.mCycleMap = r5
            java.util.ArrayList<androidx.constraintlayout.motion.widget.Key> r5 = r0.mKeyList
            if (r5 == 0) goto L_0x0594
            java.util.Iterator r5 = r4.iterator()
        L_0x052f:
            boolean r6 = r5.hasNext()
            if (r6 == 0) goto L_0x055b
            java.lang.Object r6 = r5.next()
            java.lang.String r6 = (java.lang.String) r6
            androidx.constraintlayout.motion.utils.ViewOscillator r7 = androidx.constraintlayout.motion.utils.ViewOscillator.makeSpline(r6)
            if (r7 != 0) goto L_0x0542
            goto L_0x052f
        L_0x0542:
            boolean r9 = r7.variesByPath()
            if (r9 == 0) goto L_0x0552
            boolean r9 = java.lang.Float.isNaN(r3)
            if (r9 == 0) goto L_0x0552
            float r3 = r28.getPreCycleDistance()
        L_0x0552:
            r7.setType(r6)
            java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.utils.ViewOscillator> r9 = r0.mCycleMap
            r9.put(r6, r7)
            goto L_0x052f
        L_0x055b:
            java.util.ArrayList<androidx.constraintlayout.motion.widget.Key> r5 = r0.mKeyList
            java.util.Iterator r5 = r5.iterator()
        L_0x0561:
            boolean r6 = r5.hasNext()
            if (r6 == 0) goto L_0x057a
            java.lang.Object r6 = r5.next()
            androidx.constraintlayout.motion.widget.Key r6 = (androidx.constraintlayout.motion.widget.Key) r6
            boolean r7 = r6 instanceof androidx.constraintlayout.motion.widget.KeyCycle
            if (r7 == 0) goto L_0x0579
            r7 = r6
            androidx.constraintlayout.motion.widget.KeyCycle r7 = (androidx.constraintlayout.motion.widget.KeyCycle) r7
            java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.utils.ViewOscillator> r9 = r0.mCycleMap
            r7.addCycleValues(r9)
        L_0x0579:
            goto L_0x0561
        L_0x057a:
            java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.utils.ViewOscillator> r5 = r0.mCycleMap
            java.util.Collection r5 = r5.values()
            java.util.Iterator r5 = r5.iterator()
        L_0x0584:
            boolean r6 = r5.hasNext()
            if (r6 == 0) goto L_0x0594
            java.lang.Object r6 = r5.next()
            androidx.constraintlayout.motion.utils.ViewOscillator r6 = (androidx.constraintlayout.motion.utils.ViewOscillator) r6
            r6.setup(r3)
            goto L_0x0584
        L_0x0594:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionController.setup(int, int, float, long):void");
    }

    public void setupRelative(MotionController motionController) {
        this.mStartMotionPath.setupRelative(motionController, motionController.mStartMotionPath);
        this.mEndMotionPath.setupRelative(motionController, motionController.mEndMotionPath);
    }

    public String toString() {
        return " start: x: " + this.mStartMotionPath.x + " y: " + this.mStartMotionPath.y + " end: x: " + this.mEndMotionPath.x + " y: " + this.mEndMotionPath.y;
    }
}

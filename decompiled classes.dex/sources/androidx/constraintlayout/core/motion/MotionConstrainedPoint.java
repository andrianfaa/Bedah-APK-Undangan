package androidx.constraintlayout.core.motion;

import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.core.motion.utils.Rect;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.core.motion.utils.Utils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

class MotionConstrainedPoint implements Comparable<MotionConstrainedPoint> {
    static final int CARTESIAN = 2;
    public static final boolean DEBUG = false;
    static final int PERPENDICULAR = 1;
    public static final String TAG = "MotionPaths";
    static String[] names = {"position", "x", "y", "width", "height", "pathRotate"};
    private float alpha = 1.0f;
    private boolean applyElevation = false;
    private float elevation = 0.0f;
    private float height;
    private int mAnimateRelativeTo = -1;
    LinkedHashMap<String, CustomVariable> mCustomVariable = new LinkedHashMap<>();
    private int mDrawPath = 0;
    private Easing mKeyFrameEasing;
    int mMode = 0;
    private float mPathRotate = Float.NaN;
    private float mPivotX = Float.NaN;
    private float mPivotY = Float.NaN;
    private float mProgress = Float.NaN;
    double[] mTempDelta = new double[18];
    double[] mTempValue = new double[18];
    int mVisibilityMode = 0;
    private float position;
    private float rotation = 0.0f;
    private float rotationX = 0.0f;
    public float rotationY = 0.0f;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float translationX = 0.0f;
    private float translationY = 0.0f;
    private float translationZ = 0.0f;
    int visibility;
    private float width;
    private float x;
    private float y;

    private boolean diff(float a, float b) {
        return (Float.isNaN(a) || Float.isNaN(b)) ? Float.isNaN(a) != Float.isNaN(b) : Math.abs(a - b) > 1.0E-6f;
    }

    public void addValues(HashMap<String, SplineSet> hashMap, int mFramePosition) {
        for (String next : hashMap.keySet()) {
            SplineSet splineSet = hashMap.get(next);
            char c = 65535;
            switch (next.hashCode()) {
                case -1249320806:
                    if (next.equals("rotationX")) {
                        c = 2;
                        break;
                    }
                    break;
                case -1249320805:
                    if (next.equals("rotationY")) {
                        c = 3;
                        break;
                    }
                    break;
                case -1249320804:
                    if (next.equals("rotationZ")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1225497657:
                    if (next.equals("translationX")) {
                        c = 10;
                        break;
                    }
                    break;
                case -1225497656:
                    if (next.equals("translationY")) {
                        c = 11;
                        break;
                    }
                    break;
                case -1225497655:
                    if (next.equals("translationZ")) {
                        c = 12;
                        break;
                    }
                    break;
                case -1001078227:
                    if (next.equals("progress")) {
                        c = 7;
                        break;
                    }
                    break;
                case -987906986:
                    if (next.equals("pivotX")) {
                        c = 4;
                        break;
                    }
                    break;
                case -987906985:
                    if (next.equals("pivotY")) {
                        c = 5;
                        break;
                    }
                    break;
                case -908189618:
                    if (next.equals("scaleX")) {
                        c = 8;
                        break;
                    }
                    break;
                case -908189617:
                    if (next.equals("scaleY")) {
                        c = 9;
                        break;
                    }
                    break;
                case 92909918:
                    if (next.equals("alpha")) {
                        c = 0;
                        break;
                    }
                    break;
                case 803192288:
                    if (next.equals("pathRotate")) {
                        c = 6;
                        break;
                    }
                    break;
            }
            float f = 1.0f;
            float f2 = 0.0f;
            switch (c) {
                case 0:
                    if (!Float.isNaN(this.alpha)) {
                        f = this.alpha;
                    }
                    splineSet.setPoint(mFramePosition, f);
                    break;
                case 1:
                    if (!Float.isNaN(this.rotation)) {
                        f2 = this.rotation;
                    }
                    splineSet.setPoint(mFramePosition, f2);
                    break;
                case 2:
                    if (!Float.isNaN(this.rotationX)) {
                        f2 = this.rotationX;
                    }
                    splineSet.setPoint(mFramePosition, f2);
                    break;
                case 3:
                    if (!Float.isNaN(this.rotationY)) {
                        f2 = this.rotationY;
                    }
                    splineSet.setPoint(mFramePosition, f2);
                    break;
                case 4:
                    if (!Float.isNaN(this.mPivotX)) {
                        f2 = this.mPivotX;
                    }
                    splineSet.setPoint(mFramePosition, f2);
                    break;
                case 5:
                    if (!Float.isNaN(this.mPivotY)) {
                        f2 = this.mPivotY;
                    }
                    splineSet.setPoint(mFramePosition, f2);
                    break;
                case 6:
                    if (!Float.isNaN(this.mPathRotate)) {
                        f2 = this.mPathRotate;
                    }
                    splineSet.setPoint(mFramePosition, f2);
                    break;
                case 7:
                    if (!Float.isNaN(this.mProgress)) {
                        f2 = this.mProgress;
                    }
                    splineSet.setPoint(mFramePosition, f2);
                    break;
                case 8:
                    if (!Float.isNaN(this.scaleX)) {
                        f = this.scaleX;
                    }
                    splineSet.setPoint(mFramePosition, f);
                    break;
                case 9:
                    if (!Float.isNaN(this.scaleY)) {
                        f = this.scaleY;
                    }
                    splineSet.setPoint(mFramePosition, f);
                    break;
                case 10:
                    if (!Float.isNaN(this.translationX)) {
                        f2 = this.translationX;
                    }
                    splineSet.setPoint(mFramePosition, f2);
                    break;
                case 11:
                    if (!Float.isNaN(this.translationY)) {
                        f2 = this.translationY;
                    }
                    splineSet.setPoint(mFramePosition, f2);
                    break;
                case 12:
                    if (!Float.isNaN(this.translationZ)) {
                        f2 = this.translationZ;
                    }
                    splineSet.setPoint(mFramePosition, f2);
                    break;
                default:
                    if (!next.startsWith("CUSTOM")) {
                        Utils.loge("MotionPaths", "UNKNOWN spline " + next);
                        break;
                    } else {
                        String str = next.split(",")[1];
                        if (!this.mCustomVariable.containsKey(str)) {
                            break;
                        } else {
                            CustomVariable customVariable = this.mCustomVariable.get(str);
                            if (!(splineSet instanceof SplineSet.CustomSpline)) {
                                Utils.loge("MotionPaths", next + " ViewSpline not a CustomSet frame = " + mFramePosition + ", value" + customVariable.getValueToInterpolate() + splineSet);
                                break;
                            } else {
                                ((SplineSet.CustomSpline) splineSet).setPoint(mFramePosition, customVariable);
                                break;
                            }
                        }
                    }
            }
        }
    }

    public void applyParameters(MotionWidget view) {
        this.visibility = view.getVisibility();
        this.alpha = view.getVisibility() != 4 ? 0.0f : view.getAlpha();
        this.applyElevation = false;
        this.rotation = view.getRotationZ();
        this.rotationX = view.getRotationX();
        this.rotationY = view.getRotationY();
        this.scaleX = view.getScaleX();
        this.scaleY = view.getScaleY();
        this.mPivotX = view.getPivotX();
        this.mPivotY = view.getPivotY();
        this.translationX = view.getTranslationX();
        this.translationY = view.getTranslationY();
        this.translationZ = view.getTranslationZ();
        for (String next : view.getCustomAttributeNames()) {
            CustomVariable customAttribute = view.getCustomAttribute(next);
            if (customAttribute != null && customAttribute.isContinuous()) {
                this.mCustomVariable.put(next, customAttribute);
            }
        }
    }

    public int compareTo(MotionConstrainedPoint o) {
        return Float.compare(this.position, o.position);
    }

    /* access modifiers changed from: package-private */
    public void different(MotionConstrainedPoint points, HashSet<String> hashSet) {
        if (diff(this.alpha, points.alpha)) {
            hashSet.add("alpha");
        }
        if (diff(this.elevation, points.elevation)) {
            hashSet.add("translationZ");
        }
        int i = this.visibility;
        int i2 = points.visibility;
        if (i != i2 && this.mVisibilityMode == 0 && (i == 4 || i2 == 4)) {
            hashSet.add("alpha");
        }
        if (diff(this.rotation, points.rotation)) {
            hashSet.add("rotationZ");
        }
        if (!Float.isNaN(this.mPathRotate) || !Float.isNaN(points.mPathRotate)) {
            hashSet.add("pathRotate");
        }
        if (!Float.isNaN(this.mProgress) || !Float.isNaN(points.mProgress)) {
            hashSet.add("progress");
        }
        if (diff(this.rotationX, points.rotationX)) {
            hashSet.add("rotationX");
        }
        if (diff(this.rotationY, points.rotationY)) {
            hashSet.add("rotationY");
        }
        if (diff(this.mPivotX, points.mPivotX)) {
            hashSet.add("pivotX");
        }
        if (diff(this.mPivotY, points.mPivotY)) {
            hashSet.add("pivotY");
        }
        if (diff(this.scaleX, points.scaleX)) {
            hashSet.add("scaleX");
        }
        if (diff(this.scaleY, points.scaleY)) {
            hashSet.add("scaleY");
        }
        if (diff(this.translationX, points.translationX)) {
            hashSet.add("translationX");
        }
        if (diff(this.translationY, points.translationY)) {
            hashSet.add("translationY");
        }
        if (diff(this.translationZ, points.translationZ)) {
            hashSet.add("translationZ");
        }
        if (diff(this.elevation, points.elevation)) {
            hashSet.add("elevation");
        }
    }

    /* access modifiers changed from: package-private */
    public void different(MotionConstrainedPoint points, boolean[] mask, String[] custom) {
        int i = 0 + 1;
        mask[0] = mask[0] | diff(this.position, points.position);
        int i2 = i + 1;
        mask[i] = mask[i] | diff(this.x, points.x);
        int i3 = i2 + 1;
        mask[i2] = mask[i2] | diff(this.y, points.y);
        int i4 = i3 + 1;
        mask[i3] = mask[i3] | diff(this.width, points.width);
        int i5 = i4 + 1;
        mask[i4] = mask[i4] | diff(this.height, points.height);
    }

    /* access modifiers changed from: package-private */
    public void fillStandard(double[] data, int[] toUse) {
        float[] fArr = {this.position, this.x, this.y, this.width, this.height, this.alpha, this.elevation, this.rotation, this.rotationX, this.rotationY, this.scaleX, this.scaleY, this.mPivotX, this.mPivotY, this.translationX, this.translationY, this.translationZ, this.mPathRotate};
        int i = 0;
        for (int i2 = 0; i2 < toUse.length; i2++) {
            if (toUse[i2] < fArr.length) {
                data[i] = (double) fArr[toUse[i2]];
                i++;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int getCustomData(String name, double[] value, int offset) {
        CustomVariable customVariable = this.mCustomVariable.get(name);
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
        return this.mCustomVariable.get(name).numberOfInterpolatedValues();
    }

    /* access modifiers changed from: package-private */
    public boolean hasCustomData(String name) {
        return this.mCustomVariable.containsKey(name);
    }

    /* access modifiers changed from: package-private */
    public void setBounds(float x2, float y2, float w, float h) {
        this.x = x2;
        this.y = y2;
        this.width = w;
        this.height = h;
    }

    public void setState(MotionWidget view) {
        setBounds((float) view.getX(), (float) view.getY(), (float) view.getWidth(), (float) view.getHeight());
        applyParameters(view);
    }

    public void setState(Rect rect, MotionWidget view, int rotation2, float prevous) {
        setBounds((float) rect.left, (float) rect.top, (float) rect.width(), (float) rect.height());
        applyParameters(view);
        this.mPivotX = Float.NaN;
        this.mPivotY = Float.NaN;
        switch (rotation2) {
            case 1:
                this.rotation = prevous - 90.0f;
                return;
            case 2:
                this.rotation = 90.0f + prevous;
                return;
            default:
                return;
        }
    }
}

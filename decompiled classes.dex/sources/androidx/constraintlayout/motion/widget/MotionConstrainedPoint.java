package androidx.constraintlayout.motion.widget;

import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.motion.utils.ViewSpline;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintSet;
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
    LinkedHashMap<String, ConstraintAttribute> attributes = new LinkedHashMap<>();
    private float elevation = 0.0f;
    private float height;
    private int mAnimateRelativeTo = -1;
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

    public void addValues(HashMap<String, ViewSpline> hashMap, int mFramePosition) {
        for (String next : hashMap.keySet()) {
            ViewSpline viewSpline = hashMap.get(next);
            char c = 65535;
            switch (next.hashCode()) {
                case -1249320806:
                    if (next.equals("rotationX")) {
                        c = 3;
                        break;
                    }
                    break;
                case -1249320805:
                    if (next.equals("rotationY")) {
                        c = 4;
                        break;
                    }
                    break;
                case -1225497657:
                    if (next.equals("translationX")) {
                        c = 11;
                        break;
                    }
                    break;
                case -1225497656:
                    if (next.equals("translationY")) {
                        c = 12;
                        break;
                    }
                    break;
                case -1225497655:
                    if (next.equals("translationZ")) {
                        c = 13;
                        break;
                    }
                    break;
                case -1001078227:
                    if (next.equals("progress")) {
                        c = 8;
                        break;
                    }
                    break;
                case -908189618:
                    if (next.equals("scaleX")) {
                        c = 9;
                        break;
                    }
                    break;
                case -908189617:
                    if (next.equals("scaleY")) {
                        c = 10;
                        break;
                    }
                    break;
                case -760884510:
                    if (next.equals(Key.PIVOT_X)) {
                        c = 5;
                        break;
                    }
                    break;
                case -760884509:
                    if (next.equals(Key.PIVOT_Y)) {
                        c = 6;
                        break;
                    }
                    break;
                case -40300674:
                    if (next.equals(Key.ROTATION)) {
                        c = 2;
                        break;
                    }
                    break;
                case -4379043:
                    if (next.equals("elevation")) {
                        c = 1;
                        break;
                    }
                    break;
                case 37232917:
                    if (next.equals("transitionPathRotate")) {
                        c = 7;
                        break;
                    }
                    break;
                case 92909918:
                    if (next.equals("alpha")) {
                        c = 0;
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
                    viewSpline.setPoint(mFramePosition, f);
                    break;
                case 1:
                    if (!Float.isNaN(this.elevation)) {
                        f2 = this.elevation;
                    }
                    viewSpline.setPoint(mFramePosition, f2);
                    break;
                case 2:
                    if (!Float.isNaN(this.rotation)) {
                        f2 = this.rotation;
                    }
                    viewSpline.setPoint(mFramePosition, f2);
                    break;
                case 3:
                    if (!Float.isNaN(this.rotationX)) {
                        f2 = this.rotationX;
                    }
                    viewSpline.setPoint(mFramePosition, f2);
                    break;
                case 4:
                    if (!Float.isNaN(this.rotationY)) {
                        f2 = this.rotationY;
                    }
                    viewSpline.setPoint(mFramePosition, f2);
                    break;
                case 5:
                    if (!Float.isNaN(this.mPivotX)) {
                        f2 = this.mPivotX;
                    }
                    viewSpline.setPoint(mFramePosition, f2);
                    break;
                case 6:
                    if (!Float.isNaN(this.mPivotY)) {
                        f2 = this.mPivotY;
                    }
                    viewSpline.setPoint(mFramePosition, f2);
                    break;
                case 7:
                    if (!Float.isNaN(this.mPathRotate)) {
                        f2 = this.mPathRotate;
                    }
                    viewSpline.setPoint(mFramePosition, f2);
                    break;
                case 8:
                    if (!Float.isNaN(this.mProgress)) {
                        f2 = this.mProgress;
                    }
                    viewSpline.setPoint(mFramePosition, f2);
                    break;
                case 9:
                    if (!Float.isNaN(this.scaleX)) {
                        f = this.scaleX;
                    }
                    viewSpline.setPoint(mFramePosition, f);
                    break;
                case 10:
                    if (!Float.isNaN(this.scaleY)) {
                        f = this.scaleY;
                    }
                    viewSpline.setPoint(mFramePosition, f);
                    break;
                case 11:
                    if (!Float.isNaN(this.translationX)) {
                        f2 = this.translationX;
                    }
                    viewSpline.setPoint(mFramePosition, f2);
                    break;
                case 12:
                    if (!Float.isNaN(this.translationY)) {
                        f2 = this.translationY;
                    }
                    viewSpline.setPoint(mFramePosition, f2);
                    break;
                case 13:
                    if (!Float.isNaN(this.translationZ)) {
                        f2 = this.translationZ;
                    }
                    viewSpline.setPoint(mFramePosition, f2);
                    break;
                default:
                    if (!next.startsWith("CUSTOM")) {
                        Log.e("MotionPaths", "UNKNOWN spline " + next);
                        break;
                    } else {
                        String str = next.split(",")[1];
                        if (!this.attributes.containsKey(str)) {
                            break;
                        } else {
                            ConstraintAttribute constraintAttribute = this.attributes.get(str);
                            if (!(viewSpline instanceof ViewSpline.CustomSet)) {
                                Log.e("MotionPaths", next + " ViewSpline not a CustomSet frame = " + mFramePosition + ", value" + constraintAttribute.getValueToInterpolate() + viewSpline);
                                break;
                            } else {
                                ((ViewSpline.CustomSet) viewSpline).setPoint(mFramePosition, constraintAttribute);
                                break;
                            }
                        }
                    }
            }
        }
    }

    public void applyParameters(View view) {
        this.visibility = view.getVisibility();
        this.alpha = view.getVisibility() != 0 ? 0.0f : view.getAlpha();
        this.applyElevation = false;
        if (Build.VERSION.SDK_INT >= 21) {
            this.elevation = view.getElevation();
        }
        this.rotation = view.getRotation();
        this.rotationX = view.getRotationX();
        this.rotationY = view.getRotationY();
        this.scaleX = view.getScaleX();
        this.scaleY = view.getScaleY();
        this.mPivotX = view.getPivotX();
        this.mPivotY = view.getPivotY();
        this.translationX = view.getTranslationX();
        this.translationY = view.getTranslationY();
        if (Build.VERSION.SDK_INT >= 21) {
            this.translationZ = view.getTranslationZ();
        }
    }

    public void applyParameters(ConstraintSet.Constraint c) {
        this.mVisibilityMode = c.propertySet.mVisibilityMode;
        this.visibility = c.propertySet.visibility;
        this.alpha = (c.propertySet.visibility == 0 || this.mVisibilityMode != 0) ? c.propertySet.alpha : 0.0f;
        this.applyElevation = c.transform.applyElevation;
        this.elevation = c.transform.elevation;
        this.rotation = c.transform.rotation;
        this.rotationX = c.transform.rotationX;
        this.rotationY = c.transform.rotationY;
        this.scaleX = c.transform.scaleX;
        this.scaleY = c.transform.scaleY;
        this.mPivotX = c.transform.transformPivotX;
        this.mPivotY = c.transform.transformPivotY;
        this.translationX = c.transform.translationX;
        this.translationY = c.transform.translationY;
        this.translationZ = c.transform.translationZ;
        this.mKeyFrameEasing = Easing.getInterpolator(c.motion.mTransitionEasing);
        this.mPathRotate = c.motion.mPathRotate;
        this.mDrawPath = c.motion.mDrawPath;
        this.mAnimateRelativeTo = c.motion.mAnimateRelativeTo;
        this.mProgress = c.propertySet.mProgress;
        for (String next : c.mCustomConstraints.keySet()) {
            ConstraintAttribute constraintAttribute = c.mCustomConstraints.get(next);
            if (constraintAttribute.isContinuous()) {
                this.attributes.put(next, constraintAttribute);
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
            hashSet.add("elevation");
        }
        int i = this.visibility;
        int i2 = points.visibility;
        if (i != i2 && this.mVisibilityMode == 0 && (i == 0 || i2 == 0)) {
            hashSet.add("alpha");
        }
        if (diff(this.rotation, points.rotation)) {
            hashSet.add(Key.ROTATION);
        }
        if (!Float.isNaN(this.mPathRotate) || !Float.isNaN(points.mPathRotate)) {
            hashSet.add("transitionPathRotate");
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
            hashSet.add(Key.PIVOT_X);
        }
        if (diff(this.mPivotY, points.mPivotY)) {
            hashSet.add(Key.PIVOT_Y);
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
        ConstraintAttribute constraintAttribute = this.attributes.get(name);
        if (constraintAttribute.numberOfInterpolatedValues() == 1) {
            value[offset] = (double) constraintAttribute.getValueToInterpolate();
            return 1;
        }
        int numberOfInterpolatedValues = constraintAttribute.numberOfInterpolatedValues();
        float[] fArr = new float[numberOfInterpolatedValues];
        constraintAttribute.getValuesToInterpolate(fArr);
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
        return this.attributes.get(name).numberOfInterpolatedValues();
    }

    /* access modifiers changed from: package-private */
    public boolean hasCustomData(String name) {
        return this.attributes.containsKey(name);
    }

    /* access modifiers changed from: package-private */
    public void setBounds(float x2, float y2, float w, float h) {
        this.x = x2;
        this.y = y2;
        this.width = w;
        this.height = h;
    }

    public void setState(Rect rect, View view, int rotation2, float prevous) {
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

    public void setState(Rect cw, ConstraintSet constraintSet, int rotation2, int viewId) {
        setBounds((float) cw.left, (float) cw.top, (float) cw.width(), (float) cw.height());
        applyParameters(constraintSet.getParameters(viewId));
        switch (rotation2) {
            case 1:
            case 3:
                this.rotation -= 90.0f;
                return;
            case 2:
            case 4:
                float f = this.rotation + 90.0f;
                this.rotation = f;
                if (f > 180.0f) {
                    this.rotation = f - 360.0f;
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void setState(View view) {
        setBounds(view.getX(), view.getY(), (float) view.getWidth(), (float) view.getHeight());
        applyParameters(view);
    }
}

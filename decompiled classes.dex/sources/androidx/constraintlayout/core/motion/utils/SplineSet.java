package androidx.constraintlayout.core.motion.utils;

import androidx.constraintlayout.core.motion.CustomAttribute;
import androidx.constraintlayout.core.motion.CustomVariable;
import androidx.constraintlayout.core.motion.MotionWidget;
import androidx.constraintlayout.core.motion.utils.KeyFrameArray;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.core.state.WidgetFrame;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Arrays;

public abstract class SplineSet {
    private static final String TAG = "SplineSet";
    private int count;
    protected CurveFit mCurveFit;
    protected int[] mTimePoints = new int[10];
    private String mType;
    protected float[] mValues = new float[10];

    private static class CoreSpline extends SplineSet {
        long start;
        String type;

        public CoreSpline(String str, long currentTime) {
            this.type = str;
            this.start = currentTime;
        }

        public void setProperty(TypedValues widget, float t) {
            widget.setValue(widget.getId(this.type), get(t));
        }
    }

    public static class CustomSet extends SplineSet {
        String mAttributeName;
        KeyFrameArray.CustomArray mConstraintAttributeList;
        float[] mTempValues;

        public CustomSet(String attribute, KeyFrameArray.CustomArray attrList) {
            this.mAttributeName = attribute.split(",")[1];
            this.mConstraintAttributeList = attrList;
        }

        public void setPoint(int position, float value) {
            throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute)");
        }

        public void setPoint(int position, CustomAttribute value) {
            this.mConstraintAttributeList.append(position, value);
        }

        public void setProperty(WidgetFrame view, float t) {
            this.mCurveFit.getPos((double) t, this.mTempValues);
            view.setCustomValue(this.mConstraintAttributeList.valueAt(0), this.mTempValues);
        }

        public void setup(int curveType) {
            int size = this.mConstraintAttributeList.size();
            int numberOfInterpolatedValues = this.mConstraintAttributeList.valueAt(0).numberOfInterpolatedValues();
            double[] dArr = new double[size];
            this.mTempValues = new float[numberOfInterpolatedValues];
            int[] iArr = new int[2];
            iArr[1] = numberOfInterpolatedValues;
            iArr[0] = size;
            double[][] dArr2 = (double[][]) Array.newInstance(Double.TYPE, iArr);
            for (int i = 0; i < size; i++) {
                int keyAt = this.mConstraintAttributeList.keyAt(i);
                CustomAttribute valueAt = this.mConstraintAttributeList.valueAt(i);
                dArr[i] = ((double) keyAt) * 0.01d;
                valueAt.getValuesToInterpolate(this.mTempValues);
                int i2 = 0;
                while (true) {
                    float[] fArr = this.mTempValues;
                    if (i2 >= fArr.length) {
                        break;
                    }
                    dArr2[i][i2] = (double) fArr[i2];
                    i2++;
                }
            }
            this.mCurveFit = CurveFit.get(curveType, dArr, dArr2);
        }
    }

    public static class CustomSpline extends SplineSet {
        String mAttributeName;
        KeyFrameArray.CustomVar mConstraintAttributeList;
        float[] mTempValues;

        public CustomSpline(String attribute, KeyFrameArray.CustomVar attrList) {
            this.mAttributeName = attribute.split(",")[1];
            this.mConstraintAttributeList = attrList;
        }

        public void setPoint(int position, float value) {
            throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute)");
        }

        public void setPoint(int position, CustomVariable value) {
            this.mConstraintAttributeList.append(position, value);
        }

        public void setProperty(MotionWidget view, float t) {
            this.mCurveFit.getPos((double) t, this.mTempValues);
            this.mConstraintAttributeList.valueAt(0).setInterpolatedValue(view, this.mTempValues);
        }

        public void setProperty(TypedValues widget, float t) {
            setProperty((MotionWidget) widget, t);
        }

        public void setup(int curveType) {
            int size = this.mConstraintAttributeList.size();
            int numberOfInterpolatedValues = this.mConstraintAttributeList.valueAt(0).numberOfInterpolatedValues();
            double[] dArr = new double[size];
            this.mTempValues = new float[numberOfInterpolatedValues];
            int[] iArr = new int[2];
            iArr[1] = numberOfInterpolatedValues;
            iArr[0] = size;
            double[][] dArr2 = (double[][]) Array.newInstance(Double.TYPE, iArr);
            for (int i = 0; i < size; i++) {
                int keyAt = this.mConstraintAttributeList.keyAt(i);
                CustomVariable valueAt = this.mConstraintAttributeList.valueAt(i);
                dArr[i] = ((double) keyAt) * 0.01d;
                valueAt.getValuesToInterpolate(this.mTempValues);
                int i2 = 0;
                while (true) {
                    float[] fArr = this.mTempValues;
                    if (i2 >= fArr.length) {
                        break;
                    }
                    dArr2[i][i2] = (double) fArr[i2];
                    i2++;
                }
            }
            this.mCurveFit = CurveFit.get(curveType, dArr, dArr2);
        }
    }

    private static class Sort {
        private Sort() {
        }

        static void doubleQuickSort(int[] key, float[] value, int low, int hi) {
            int[] iArr = new int[(key.length + 10)];
            int i = 0 + 1;
            iArr[0] = hi;
            int i2 = i + 1;
            iArr[i] = low;
            while (i2 > 0) {
                int i3 = i2 - 1;
                int low2 = iArr[i3];
                i2 = i3 - 1;
                int hi2 = iArr[i2];
                if (low2 < hi2) {
                    int partition = partition(key, value, low2, hi2);
                    int i4 = i2 + 1;
                    iArr[i2] = partition - 1;
                    int i5 = i4 + 1;
                    iArr[i4] = low2;
                    int i6 = i5 + 1;
                    iArr[i5] = hi2;
                    i2 = i6 + 1;
                    iArr[i6] = partition + 1;
                }
            }
        }

        private static int partition(int[] array, float[] value, int low, int hi) {
            int i = array[hi];
            int i2 = low;
            for (int i3 = low; i3 < hi; i3++) {
                if (array[i3] <= i) {
                    swap(array, value, i2, i3);
                    i2++;
                }
            }
            swap(array, value, i2, hi);
            return i2;
        }

        private static void swap(int[] array, float[] value, int a, int b) {
            int i = array[a];
            array[a] = array[b];
            array[b] = i;
            float f = value[a];
            value[a] = value[b];
            value[b] = f;
        }
    }

    public static SplineSet makeCustomSpline(String str, KeyFrameArray.CustomArray attrList) {
        return new CustomSet(str, attrList);
    }

    public static SplineSet makeCustomSplineSet(String str, KeyFrameArray.CustomVar attrList) {
        return new CustomSpline(str, attrList);
    }

    public static SplineSet makeSpline(String str, long currentTime) {
        return new CoreSpline(str, currentTime);
    }

    public float get(float t) {
        return (float) this.mCurveFit.getPos((double) t, 0);
    }

    public CurveFit getCurveFit() {
        return this.mCurveFit;
    }

    public float getSlope(float t) {
        return (float) this.mCurveFit.getSlope((double) t, 0);
    }

    public void setPoint(int position, float value) {
        int[] iArr = this.mTimePoints;
        if (iArr.length < this.count + 1) {
            this.mTimePoints = Arrays.copyOf(iArr, iArr.length * 2);
            float[] fArr = this.mValues;
            this.mValues = Arrays.copyOf(fArr, fArr.length * 2);
        }
        int[] iArr2 = this.mTimePoints;
        int i = this.count;
        iArr2[i] = position;
        this.mValues[i] = value;
        this.count = i + 1;
    }

    public void setProperty(TypedValues widget, float t) {
        widget.setValue(TypedValues.AttributesType.getId(this.mType), get(t));
    }

    public void setType(String type) {
        this.mType = type;
    }

    public void setup(int curveType) {
        int i = this.count;
        if (i != 0) {
            Sort.doubleQuickSort(this.mTimePoints, this.mValues, 0, i - 1);
            int i2 = 1;
            for (int i3 = 1; i3 < this.count; i3++) {
                int[] iArr = this.mTimePoints;
                if (iArr[i3 - 1] != iArr[i3]) {
                    i2++;
                }
            }
            double[] dArr = new double[i2];
            int[] iArr2 = new int[2];
            iArr2[1] = 1;
            iArr2[0] = i2;
            double[][] dArr2 = (double[][]) Array.newInstance(Double.TYPE, iArr2);
            int i4 = 0;
            for (int i5 = 0; i5 < this.count; i5++) {
                if (i5 > 0) {
                    int[] iArr3 = this.mTimePoints;
                    if (iArr3[i5] == iArr3[i5 - 1]) {
                    }
                }
                dArr[i4] = ((double) this.mTimePoints[i5]) * 0.01d;
                dArr2[i4][0] = (double) this.mValues[i5];
                i4++;
            }
            this.mCurveFit = CurveFit.get(curveType, dArr, dArr2);
        }
    }

    public String toString() {
        String str = this.mType;
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        for (int i = 0; i < this.count; i++) {
            str = str + "[" + this.mTimePoints[i] + " , " + decimalFormat.format((double) this.mValues[i]) + "] ";
        }
        return str;
    }
}

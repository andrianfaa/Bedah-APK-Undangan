package androidx.constraintlayout.core.motion.utils;

import androidx.constraintlayout.core.motion.CustomAttribute;
import androidx.constraintlayout.core.motion.CustomVariable;
import androidx.constraintlayout.core.motion.MotionWidget;
import androidx.constraintlayout.core.motion.utils.KeyFrameArray;
import java.lang.reflect.Array;
import java.text.DecimalFormat;

public abstract class TimeCycleSplineSet {
    protected static final int CURVE_OFFSET = 2;
    protected static final int CURVE_PERIOD = 1;
    protected static final int CURVE_VALUE = 0;
    private static final String TAG = "SplineSet";
    protected static float VAL_2PI = 6.2831855f;
    protected int count;
    protected float last_cycle = Float.NaN;
    protected long last_time;
    protected float[] mCache = new float[3];
    protected boolean mContinue = false;
    protected CurveFit mCurveFit;
    protected int[] mTimePoints = new int[10];
    protected String mType;
    protected float[][] mValues = ((float[][]) Array.newInstance(Float.TYPE, new int[]{10, 3}));
    protected int mWaveShape = 0;

    public static class CustomSet extends TimeCycleSplineSet {
        String mAttributeName;
        float[] mCache;
        KeyFrameArray.CustomArray mConstraintAttributeList;
        float[] mTempValues;
        KeyFrameArray.FloatArray mWaveProperties = new KeyFrameArray.FloatArray();

        public CustomSet(String attribute, KeyFrameArray.CustomArray attrList) {
            this.mAttributeName = attribute.split(",")[1];
            this.mConstraintAttributeList = attrList;
        }

        public void setPoint(int position, float value, float period, int shape, float offset) {
            throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute,...)");
        }

        public void setPoint(int position, CustomAttribute value, float period, int shape, float offset) {
            this.mConstraintAttributeList.append(position, value);
            this.mWaveProperties.append(position, new float[]{period, offset});
            this.mWaveShape = Math.max(this.mWaveShape, shape);
        }

        public boolean setProperty(MotionWidget view, float t, long time, KeyCache cache) {
            MotionWidget motionWidget = view;
            long j = time;
            this.mCurveFit.getPos((double) t, this.mTempValues);
            float[] fArr = this.mTempValues;
            float f = fArr[fArr.length - 2];
            float f2 = fArr[fArr.length - 1];
            long j2 = j - this.last_time;
            if (Float.isNaN(this.last_cycle)) {
                this.last_cycle = cache.getFloatValue(motionWidget, this.mAttributeName, 0);
                if (Float.isNaN(this.last_cycle)) {
                    this.last_cycle = 0.0f;
                }
            } else {
                KeyCache keyCache = cache;
            }
            this.last_cycle = (float) ((((double) this.last_cycle) + ((((double) j2) * 1.0E-9d) * ((double) f))) % 1.0d);
            this.last_time = j;
            float calcWave = calcWave(this.last_cycle);
            this.mContinue = false;
            for (int i = 0; i < this.mCache.length; i++) {
                this.mContinue |= ((double) this.mTempValues[i]) != 0.0d;
                this.mCache[i] = (this.mTempValues[i] * calcWave) + f2;
            }
            motionWidget.setInterpolatedValue(this.mConstraintAttributeList.valueAt(0), this.mCache);
            if (f != 0.0f) {
                this.mContinue = true;
            }
            return this.mContinue;
        }

        public void setup(int curveType) {
            int size = this.mConstraintAttributeList.size();
            int numberOfInterpolatedValues = this.mConstraintAttributeList.valueAt(0).numberOfInterpolatedValues();
            double[] dArr = new double[size];
            this.mTempValues = new float[(numberOfInterpolatedValues + 2)];
            this.mCache = new float[numberOfInterpolatedValues];
            int[] iArr = new int[2];
            iArr[1] = numberOfInterpolatedValues + 2;
            iArr[0] = size;
            double[][] dArr2 = (double[][]) Array.newInstance(Double.TYPE, iArr);
            for (int i = 0; i < size; i++) {
                int keyAt = this.mConstraintAttributeList.keyAt(i);
                CustomAttribute valueAt = this.mConstraintAttributeList.valueAt(i);
                float[] valueAt2 = this.mWaveProperties.valueAt(i);
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
                dArr2[i][numberOfInterpolatedValues] = (double) valueAt2[0];
                dArr2[i][numberOfInterpolatedValues + 1] = (double) valueAt2[1];
            }
            this.mCurveFit = CurveFit.get(curveType, dArr, dArr2);
        }
    }

    public static class CustomVarSet extends TimeCycleSplineSet {
        String mAttributeName;
        float[] mCache;
        KeyFrameArray.CustomVar mConstraintAttributeList;
        float[] mTempValues;
        KeyFrameArray.FloatArray mWaveProperties = new KeyFrameArray.FloatArray();

        public CustomVarSet(String attribute, KeyFrameArray.CustomVar attrList) {
            this.mAttributeName = attribute.split(",")[1];
            this.mConstraintAttributeList = attrList;
        }

        public void setPoint(int position, float value, float period, int shape, float offset) {
            throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute,...)");
        }

        public void setPoint(int position, CustomVariable value, float period, int shape, float offset) {
            this.mConstraintAttributeList.append(position, value);
            this.mWaveProperties.append(position, new float[]{period, offset});
            this.mWaveShape = Math.max(this.mWaveShape, shape);
        }

        public boolean setProperty(MotionWidget view, float t, long time, KeyCache cache) {
            MotionWidget motionWidget = view;
            long j = time;
            this.mCurveFit.getPos((double) t, this.mTempValues);
            float[] fArr = this.mTempValues;
            float f = fArr[fArr.length - 2];
            float f2 = fArr[fArr.length - 1];
            long j2 = j - this.last_time;
            if (Float.isNaN(this.last_cycle)) {
                this.last_cycle = cache.getFloatValue(motionWidget, this.mAttributeName, 0);
                if (Float.isNaN(this.last_cycle)) {
                    this.last_cycle = 0.0f;
                }
            } else {
                KeyCache keyCache = cache;
            }
            this.last_cycle = (float) ((((double) this.last_cycle) + ((((double) j2) * 1.0E-9d) * ((double) f))) % 1.0d);
            this.last_time = j;
            float calcWave = calcWave(this.last_cycle);
            this.mContinue = false;
            for (int i = 0; i < this.mCache.length; i++) {
                this.mContinue |= ((double) this.mTempValues[i]) != 0.0d;
                this.mCache[i] = (this.mTempValues[i] * calcWave) + f2;
            }
            this.mConstraintAttributeList.valueAt(0).setInterpolatedValue(motionWidget, this.mCache);
            if (f != 0.0f) {
                this.mContinue = true;
            }
            return this.mContinue;
        }

        public void setup(int curveType) {
            int size = this.mConstraintAttributeList.size();
            int numberOfInterpolatedValues = this.mConstraintAttributeList.valueAt(0).numberOfInterpolatedValues();
            double[] dArr = new double[size];
            this.mTempValues = new float[(numberOfInterpolatedValues + 2)];
            this.mCache = new float[numberOfInterpolatedValues];
            int[] iArr = new int[2];
            iArr[1] = numberOfInterpolatedValues + 2;
            iArr[0] = size;
            double[][] dArr2 = (double[][]) Array.newInstance(Double.TYPE, iArr);
            for (int i = 0; i < size; i++) {
                int keyAt = this.mConstraintAttributeList.keyAt(i);
                CustomVariable valueAt = this.mConstraintAttributeList.valueAt(i);
                float[] valueAt2 = this.mWaveProperties.valueAt(i);
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
                dArr2[i][numberOfInterpolatedValues] = (double) valueAt2[0];
                dArr2[i][numberOfInterpolatedValues + 1] = (double) valueAt2[1];
            }
            this.mCurveFit = CurveFit.get(curveType, dArr, dArr2);
        }
    }

    protected static class Sort {
        protected Sort() {
        }

        static void doubleQuickSort(int[] key, float[][] value, int low, int hi) {
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

        private static int partition(int[] array, float[][] value, int low, int hi) {
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

        private static void swap(int[] array, float[][] value, int a, int b) {
            int i = array[a];
            array[a] = array[b];
            array[b] = i;
            float[] fArr = value[a];
            value[a] = value[b];
            value[b] = fArr;
        }
    }

    /* access modifiers changed from: protected */
    public float calcWave(float period) {
        float f = period;
        switch (this.mWaveShape) {
            case 1:
                return Math.signum(VAL_2PI * f);
            case 2:
                return 1.0f - Math.abs(f);
            case 3:
                return (((f * 2.0f) + 1.0f) % 2.0f) - 1.0f;
            case 4:
                return 1.0f - (((f * 2.0f) + 1.0f) % 2.0f);
            case 5:
                return (float) Math.cos((double) (VAL_2PI * f));
            case 6:
                float abs = 1.0f - Math.abs(((f * 4.0f) % 4.0f) - 2.0f);
                return 1.0f - (abs * abs);
            default:
                return (float) Math.sin((double) (VAL_2PI * f));
        }
    }

    public CurveFit getCurveFit() {
        return this.mCurveFit;
    }

    public void setPoint(int position, float value, float period, int shape, float offset) {
        int[] iArr = this.mTimePoints;
        int i = this.count;
        iArr[i] = position;
        float[] fArr = this.mValues[i];
        fArr[0] = value;
        fArr[1] = period;
        fArr[2] = offset;
        this.mWaveShape = Math.max(this.mWaveShape, shape);
        this.count++;
    }

    /* access modifiers changed from: protected */
    public void setStartTime(long currentTime) {
        this.last_time = currentTime;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public void setup(int curveType) {
        int i = this.count;
        if (i == 0) {
            System.err.println("Error no points added to " + this.mType);
            return;
        }
        Sort.doubleQuickSort(this.mTimePoints, this.mValues, 0, i - 1);
        int i2 = 0;
        int i3 = 1;
        while (true) {
            int[] iArr = this.mTimePoints;
            if (i3 >= iArr.length) {
                break;
            }
            if (iArr[i3] != iArr[i3 - 1]) {
                i2++;
            }
            i3++;
        }
        if (i2 == 0) {
            i2 = 1;
        }
        double[] dArr = new double[i2];
        int[] iArr2 = new int[2];
        iArr2[1] = 3;
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
            double[] dArr3 = dArr2[i4];
            float[] fArr = this.mValues[i5];
            dArr3[0] = (double) fArr[0];
            dArr2[i4][1] = (double) fArr[1];
            dArr2[i4][2] = (double) fArr[2];
            i4++;
        }
        this.mCurveFit = CurveFit.get(curveType, dArr, dArr2);
    }

    public String toString() {
        String str = this.mType;
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        for (int i = 0; i < this.count; i++) {
            str = str + "[" + this.mTimePoints[i] + " , " + decimalFormat.format(this.mValues[i]) + "] ";
        }
        return str;
    }
}

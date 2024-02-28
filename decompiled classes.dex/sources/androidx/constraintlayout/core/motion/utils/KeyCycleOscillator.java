package androidx.constraintlayout.core.motion.utils;

import androidx.constraintlayout.core.motion.MotionWidget;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public abstract class KeyCycleOscillator {
    private static final String TAG = "KeyCycleOscillator";
    private CurveFit mCurveFit;
    private CycleOscillator mCycleOscillator;
    private String mType;
    public int mVariesBy = 0;
    ArrayList<WavePoint> mWavePoints = new ArrayList<>();
    private int mWaveShape = 0;
    private String mWaveString = null;

    private static class CoreSpline extends KeyCycleOscillator {
        String type;
        int typeId;

        public CoreSpline(String str) {
            this.type = str;
            this.typeId = TypedValues.CycleType.getId(str);
        }

        public void setProperty(MotionWidget widget, float t) {
            widget.setValue(this.typeId, get(t));
        }
    }

    static class CycleOscillator {
        private static final String TAG = "CycleOscillator";
        static final int UNSET = -1;
        private final int OFFST = 0;
        private final int PHASE = 1;
        private final int VALUE = 2;
        CurveFit mCurveFit;
        float[] mOffset;
        Oscillator mOscillator;
        float mPathLength;
        float[] mPeriod;
        float[] mPhase;
        double[] mPosition;
        float[] mScale;
        double[] mSplineSlopeCache;
        double[] mSplineValueCache;
        float[] mValues;
        private final int mVariesBy;
        int mWaveShape;

        CycleOscillator(int waveShape, String customShape, int variesBy, int steps) {
            Oscillator oscillator = new Oscillator();
            this.mOscillator = oscillator;
            this.mWaveShape = waveShape;
            this.mVariesBy = variesBy;
            oscillator.setType(waveShape, customShape);
            this.mValues = new float[steps];
            this.mPosition = new double[steps];
            this.mPeriod = new float[steps];
            this.mOffset = new float[steps];
            this.mPhase = new float[steps];
            this.mScale = new float[steps];
        }

        public double getLastPhase() {
            return this.mSplineValueCache[1];
        }

        public double getSlope(float time) {
            CurveFit curveFit = this.mCurveFit;
            if (curveFit != null) {
                curveFit.getSlope((double) time, this.mSplineSlopeCache);
                this.mCurveFit.getPos((double) time, this.mSplineValueCache);
            } else {
                double[] dArr = this.mSplineSlopeCache;
                dArr[0] = 0.0d;
                dArr[1] = 0.0d;
                dArr[2] = 0.0d;
            }
            double value = this.mOscillator.getValue((double) time, this.mSplineValueCache[1]);
            double slope = this.mOscillator.getSlope((double) time, this.mSplineValueCache[1], this.mSplineSlopeCache[1]);
            double[] dArr2 = this.mSplineSlopeCache;
            return dArr2[0] + (dArr2[2] * value) + (this.mSplineValueCache[2] * slope);
        }

        public double getValues(float time) {
            CurveFit curveFit = this.mCurveFit;
            if (curveFit != null) {
                curveFit.getPos((double) time, this.mSplineValueCache);
            } else {
                double[] dArr = this.mSplineValueCache;
                dArr[0] = (double) this.mOffset[0];
                dArr[1] = (double) this.mPhase[0];
                dArr[2] = (double) this.mValues[0];
            }
            double[] dArr2 = this.mSplineValueCache;
            return (this.mSplineValueCache[2] * this.mOscillator.getValue((double) time, dArr2[1])) + dArr2[0];
        }

        public void setPoint(int index, int framePosition, float wavePeriod, float offset, float phase, float values) {
            this.mPosition[index] = ((double) framePosition) / 100.0d;
            this.mPeriod[index] = wavePeriod;
            this.mOffset[index] = offset;
            this.mPhase[index] = phase;
            this.mValues[index] = values;
        }

        public void setup(float pathLength) {
            this.mPathLength = pathLength;
            int length = this.mPosition.length;
            int[] iArr = new int[2];
            iArr[1] = 3;
            iArr[0] = length;
            double[][] dArr = (double[][]) Array.newInstance(Double.TYPE, iArr);
            float[] fArr = this.mValues;
            this.mSplineValueCache = new double[(fArr.length + 2)];
            this.mSplineSlopeCache = new double[(fArr.length + 2)];
            if (this.mPosition[0] > 0.0d) {
                this.mOscillator.addPoint(0.0d, this.mPeriod[0]);
            }
            double[] dArr2 = this.mPosition;
            int length2 = dArr2.length - 1;
            if (dArr2[length2] < 1.0d) {
                this.mOscillator.addPoint(1.0d, this.mPeriod[length2]);
            }
            for (int i = 0; i < dArr.length; i++) {
                dArr[i][0] = (double) this.mOffset[i];
                dArr[i][1] = (double) this.mPhase[i];
                dArr[i][2] = (double) this.mValues[i];
                this.mOscillator.addPoint(this.mPosition[i], this.mPeriod[i]);
            }
            this.mOscillator.normalize();
            double[] dArr3 = this.mPosition;
            if (dArr3.length > 1) {
                this.mCurveFit = CurveFit.get(0, dArr3, dArr);
            } else {
                this.mCurveFit = null;
            }
        }
    }

    private static class IntDoubleSort {
        private IntDoubleSort() {
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

        static void sort(int[] key, float[] value, int low, int hi) {
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

        private static void swap(int[] array, float[] value, int a, int b) {
            int i = array[a];
            array[a] = array[b];
            array[b] = i;
            float f = value[a];
            value[a] = value[b];
            value[b] = f;
        }
    }

    private static class IntFloatFloatSort {
        private IntFloatFloatSort() {
        }

        private static int partition(int[] array, float[] value1, float[] value2, int low, int hi) {
            int i = array[hi];
            int i2 = low;
            for (int i3 = low; i3 < hi; i3++) {
                if (array[i3] <= i) {
                    swap(array, value1, value2, i2, i3);
                    i2++;
                }
            }
            swap(array, value1, value2, i2, hi);
            return i2;
        }

        static void sort(int[] key, float[] value1, float[] value2, int low, int hi) {
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
                    int partition = partition(key, value1, value2, low2, hi2);
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

        private static void swap(int[] array, float[] value1, float[] value2, int a, int b) {
            int i = array[a];
            array[a] = array[b];
            array[b] = i;
            float f = value1[a];
            value1[a] = value1[b];
            value1[b] = f;
            float f2 = value2[a];
            value2[a] = value2[b];
            value2[b] = f2;
        }
    }

    public static class PathRotateSet extends KeyCycleOscillator {
        String type;
        int typeId;

        public PathRotateSet(String str) {
            this.type = str;
            this.typeId = TypedValues.CycleType.getId(str);
        }

        public void setPathRotate(MotionWidget view, float t, double dx, double dy) {
            view.setRotationZ(get(t) + ((float) Math.toDegrees(Math.atan2(dy, dx))));
        }

        public void setProperty(MotionWidget widget, float t) {
            widget.setValue(this.typeId, get(t));
        }
    }

    static class WavePoint {
        float mOffset;
        float mPeriod;
        float mPhase;
        int mPosition;
        float mValue;

        public WavePoint(int position, float period, float offset, float phase, float value) {
            this.mPosition = position;
            this.mValue = value;
            this.mOffset = offset;
            this.mPeriod = period;
            this.mPhase = phase;
        }
    }

    public static KeyCycleOscillator makeWidgetCycle(String attribute) {
        return attribute.equals("pathRotate") ? new PathRotateSet(attribute) : new CoreSpline(attribute);
    }

    public float get(float t) {
        return (float) this.mCycleOscillator.getValues(t);
    }

    public CurveFit getCurveFit() {
        return this.mCurveFit;
    }

    public float getSlope(float position) {
        return (float) this.mCycleOscillator.getSlope(position);
    }

    /* access modifiers changed from: protected */
    public void setCustom(Object custom) {
    }

    public void setPoint(int framePosition, int shape, String waveString, int variesBy, float period, float offset, float phase, float value) {
        int i = variesBy;
        this.mWavePoints.add(new WavePoint(framePosition, period, offset, phase, value));
        if (i != -1) {
            this.mVariesBy = i;
        }
        this.mWaveShape = shape;
        this.mWaveString = waveString;
    }

    public void setPoint(int framePosition, int shape, String waveString, int variesBy, float period, float offset, float phase, float value, Object custom) {
        int i = variesBy;
        this.mWavePoints.add(new WavePoint(framePosition, period, offset, phase, value));
        if (i != -1) {
            this.mVariesBy = i;
        }
        this.mWaveShape = shape;
        setCustom(custom);
        this.mWaveString = waveString;
    }

    public void setProperty(MotionWidget widget, float t) {
    }

    public void setType(String type) {
        this.mType = type;
    }

    public void setup(float pathLength) {
        int size = this.mWavePoints.size();
        if (size != 0) {
            Collections.sort(this.mWavePoints, new Comparator<WavePoint>() {
                public int compare(WavePoint lhs, WavePoint rhs) {
                    return Integer.compare(lhs.mPosition, rhs.mPosition);
                }
            });
            double[] dArr = new double[size];
            int[] iArr = new int[2];
            iArr[1] = 3;
            iArr[0] = size;
            double[][] dArr2 = (double[][]) Array.newInstance(Double.TYPE, iArr);
            this.mCycleOscillator = new CycleOscillator(this.mWaveShape, this.mWaveString, this.mVariesBy, size);
            int i = 0;
            Iterator<WavePoint> it = this.mWavePoints.iterator();
            while (it.hasNext()) {
                WavePoint next = it.next();
                dArr[i] = ((double) next.mPeriod) * 0.01d;
                dArr2[i][0] = (double) next.mValue;
                dArr2[i][1] = (double) next.mOffset;
                dArr2[i][2] = (double) next.mPhase;
                WavePoint wavePoint = next;
                this.mCycleOscillator.setPoint(i, next.mPosition, next.mPeriod, next.mOffset, next.mPhase, next.mValue);
                i++;
            }
            this.mCycleOscillator.setup(pathLength);
            this.mCurveFit = CurveFit.get(0, dArr, dArr2);
        }
    }

    public String toString() {
        String str = this.mType;
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        Iterator<WavePoint> it = this.mWavePoints.iterator();
        while (it.hasNext()) {
            WavePoint next = it.next();
            str = str + "[" + next.mPosition + " , " + decimalFormat.format((double) next.mValue) + "] ";
        }
        return str;
    }

    public boolean variesByPath() {
        return this.mVariesBy == 1;
    }
}

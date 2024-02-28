package androidx.constraintlayout.core.motion.utils;

import java.util.Arrays;
import mt.Log1F380D;

/* compiled from: 0013 */
public class Oscillator {
    public static final int BOUNCE = 6;
    public static final int COS_WAVE = 5;
    public static final int CUSTOM = 7;
    public static final int REVERSE_SAW_WAVE = 4;
    public static final int SAW_WAVE = 3;
    public static final int SIN_WAVE = 0;
    public static final int SQUARE_WAVE = 1;
    public static String TAG = "Oscillator";
    public static final int TRIANGLE_WAVE = 2;
    double PI2 = 6.283185307179586d;
    double[] mArea;
    MonotonicCurveFit mCustomCurve;
    String mCustomType;
    private boolean mNormalized = false;
    float[] mPeriod = new float[0];
    double[] mPosition = new double[0];
    int mType;

    public void addPoint(double position, float period) {
        int length = this.mPeriod.length + 1;
        int binarySearch = Arrays.binarySearch(this.mPosition, position);
        if (binarySearch < 0) {
            binarySearch = (-binarySearch) - 1;
        }
        this.mPosition = Arrays.copyOf(this.mPosition, length);
        this.mPeriod = Arrays.copyOf(this.mPeriod, length);
        this.mArea = new double[length];
        double[] dArr = this.mPosition;
        System.arraycopy(dArr, binarySearch, dArr, binarySearch + 1, (length - binarySearch) - 1);
        this.mPosition[binarySearch] = position;
        this.mPeriod[binarySearch] = period;
        this.mNormalized = false;
    }

    /* access modifiers changed from: package-private */
    public double getDP(double time) {
        double d = time <= 0.0d ? 1.0E-5d : time >= 1.0d ? 0.999999d : time;
        int binarySearch = Arrays.binarySearch(this.mPosition, d);
        if (binarySearch > 0 || binarySearch == 0) {
            return 0.0d;
        }
        int i = (-binarySearch) - 1;
        float[] fArr = this.mPeriod;
        double[] dArr = this.mPosition;
        double d2 = ((double) (fArr[i] - fArr[i - 1])) / (dArr[i] - dArr[i - 1]);
        return (d2 * d) + (((double) fArr[i - 1]) - (dArr[i - 1] * d2));
    }

    /* access modifiers changed from: package-private */
    public double getP(double time) {
        double d = time < 0.0d ? 0.0d : time > 1.0d ? 1.0d : time;
        int binarySearch = Arrays.binarySearch(this.mPosition, d);
        if (binarySearch > 0) {
            double time2 = d;
            return 1.0d;
        } else if (binarySearch != 0) {
            int i = (-binarySearch) - 1;
            double d2 = d;
            float[] fArr = this.mPeriod;
            double[] dArr = this.mPosition;
            double d3 = ((double) (fArr[i] - fArr[i - 1])) / (dArr[i] - dArr[i - 1]);
            double time3 = d;
            return this.mArea[i - 1] + ((((double) fArr[i - 1]) - (dArr[i - 1] * d3)) * (d2 - dArr[i - 1])) + ((((d2 * d2) - (dArr[i - 1] * dArr[i - 1])) * d3) / 2.0d);
        } else {
            double time4 = d;
            return 0.0d;
        }
    }

    public double getSlope(double time, double phase, double dphase) {
        double p = phase + getP(time);
        double dp = getDP(time) + dphase;
        switch (this.mType) {
            case 1:
                return 0.0d;
            case 2:
                return dp * 4.0d * Math.signum((((p * 4.0d) + 3.0d) % 4.0d) - 2.0d);
            case 3:
                return 2.0d * dp;
            case 4:
                return (-dp) * 2.0d;
            case 5:
                double d = this.PI2;
                return (-d) * dp * Math.sin(d * p);
            case 6:
                return dp * 4.0d * ((((p * 4.0d) + 2.0d) % 4.0d) - 2.0d);
            case 7:
                return this.mCustomCurve.getSlope(p % 1.0d, 0);
            default:
                double d2 = this.PI2;
                return d2 * dp * Math.cos(d2 * p);
        }
    }

    public double getValue(double time, double phase) {
        double p = getP(time) + phase;
        switch (this.mType) {
            case 1:
                return Math.signum(0.5d - (p % 1.0d));
            case 2:
                return 1.0d - Math.abs((((p * 4.0d) + 1.0d) % 4.0d) - 2.0d);
            case 3:
                return (((p * 2.0d) + 1.0d) % 2.0d) - 1.0d;
            case 4:
                return 1.0d - (((p * 2.0d) + 1.0d) % 2.0d);
            case 5:
                return Math.cos(this.PI2 * (phase + p));
            case 6:
                double abs = 1.0d - Math.abs(((p * 4.0d) % 4.0d) - 2.0d);
                return 1.0d - (abs * abs);
            case 7:
                return this.mCustomCurve.getPos(p % 1.0d, 0);
            default:
                return Math.sin(this.PI2 * p);
        }
    }

    public void normalize() {
        double d = 0.0d;
        double d2 = 0.0d;
        int i = 0;
        while (true) {
            float[] fArr = this.mPeriod;
            if (i >= fArr.length) {
                break;
            }
            d2 += (double) fArr[i];
            i++;
        }
        int i2 = 1;
        while (true) {
            float[] fArr2 = this.mPeriod;
            if (i2 >= fArr2.length) {
                break;
            }
            double[] dArr = this.mPosition;
            d += ((double) ((fArr2[i2 - 1] + fArr2[i2]) / 2.0f)) * (dArr[i2] - dArr[i2 - 1]);
            i2++;
        }
        int i3 = 0;
        while (true) {
            float[] fArr3 = this.mPeriod;
            if (i3 >= fArr3.length) {
                break;
            }
            fArr3[i3] = (float) (((double) fArr3[i3]) * (d2 / d));
            i3++;
        }
        this.mArea[0] = 0.0d;
        int i4 = 1;
        while (true) {
            float[] fArr4 = this.mPeriod;
            if (i4 < fArr4.length) {
                double[] dArr2 = this.mPosition;
                double d3 = dArr2[i4] - dArr2[i4 - 1];
                double[] dArr3 = this.mArea;
                dArr3[i4] = dArr3[i4 - 1] + (((double) ((fArr4[i4 - 1] + fArr4[i4]) / 2.0f)) * d3);
                i4++;
            } else {
                this.mNormalized = true;
                return;
            }
        }
    }

    public void setType(int type, String customType) {
        this.mType = type;
        this.mCustomType = customType;
        if (customType != null) {
            this.mCustomCurve = MonotonicCurveFit.buildWave(customType);
        }
    }

    public String toString() {
        StringBuilder append = new StringBuilder().append("pos =");
        String arrays = Arrays.toString(this.mPosition);
        Log1F380D.a((Object) arrays);
        StringBuilder append2 = append.append(arrays).append(" period=");
        String arrays2 = Arrays.toString(this.mPeriod);
        Log1F380D.a((Object) arrays2);
        return append2.append(arrays2).toString();
    }
}

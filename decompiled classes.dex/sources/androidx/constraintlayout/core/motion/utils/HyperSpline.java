package androidx.constraintlayout.core.motion.utils;

import java.lang.reflect.Array;

public class HyperSpline {
    double[][] mCtl;
    Cubic[][] mCurve;
    double[] mCurveLength;
    int mDimensionality;
    int mPoints;
    double mTotalLength;

    public static class Cubic {
        double mA;
        double mB;
        double mC;
        double mD;

        public Cubic(double a, double b, double c, double d) {
            this.mA = a;
            this.mB = b;
            this.mC = c;
            this.mD = d;
        }

        public double eval(double u) {
            return (((((this.mD * u) + this.mC) * u) + this.mB) * u) + this.mA;
        }

        public double vel(double v) {
            return (((this.mD * 3.0d * v) + (this.mC * 2.0d)) * v) + this.mB;
        }
    }

    public HyperSpline() {
    }

    public HyperSpline(double[][] points) {
        setup(points);
    }

    static Cubic[] calcNaturalCubic(int n, double[] x) {
        int i = n;
        double[] dArr = new double[i];
        double[] dArr2 = new double[i];
        double[] dArr3 = new double[i];
        int i2 = i - 1;
        dArr[0] = 0.5d;
        for (int i3 = 1; i3 < i2; i3++) {
            dArr[i3] = 1.0d / (4.0d - dArr[i3 - 1]);
        }
        dArr[i2] = 1.0d / (2.0d - dArr[i2 - 1]);
        dArr2[0] = (x[1] - x[0]) * 3.0d * dArr[0];
        for (int i4 = 1; i4 < i2; i4++) {
            dArr2[i4] = (((x[i4 + 1] - x[i4 - 1]) * 3.0d) - dArr2[i4 - 1]) * dArr[i4];
        }
        dArr2[i2] = (((x[i2] - x[i2 - 1]) * 3.0d) - dArr2[i2 - 1]) * dArr[i2];
        dArr3[i2] = dArr2[i2];
        for (int i5 = i2 - 1; i5 >= 0; i5--) {
            dArr3[i5] = dArr2[i5] - (dArr[i5] * dArr3[i5 + 1]);
        }
        Cubic[] cubicArr = new Cubic[i2];
        for (int i6 = 0; i6 < i2; i6++) {
            cubicArr[i6] = new Cubic((double) ((float) x[i6]), dArr3[i6], (((x[i6 + 1] - x[i6]) * 3.0d) - (dArr3[i6] * 2.0d)) - dArr3[i6 + 1], ((x[i6] - x[i6 + 1]) * 2.0d) + dArr3[i6] + dArr3[i6 + 1]);
        }
        return cubicArr;
    }

    public double approxLength(Cubic[] curve) {
        double d = 0.0d;
        int length = curve.length;
        double[] dArr = new double[curve.length];
        for (double d2 = 0.0d; d2 < 1.0d; d2 += 0.1d) {
            double d3 = 0.0d;
            for (int i = 0; i < curve.length; i++) {
                double d4 = dArr[i];
                double eval = curve[i].eval(d2);
                dArr[i] = eval;
                double d5 = d4 - eval;
                d3 += d5 * d5;
            }
            if (d2 > 0.0d) {
                d += Math.sqrt(d3);
            }
        }
        double d6 = 0.0d;
        for (int i2 = 0; i2 < curve.length; i2++) {
            double d7 = dArr[i2];
            double eval2 = curve[i2].eval(1.0d);
            dArr[i2] = eval2;
            double d8 = d7 - eval2;
            d6 += d8 * d8;
        }
        return d + Math.sqrt(d6);
    }

    public double getPos(double p, int splineNumber) {
        double[] dArr;
        double d = this.mTotalLength * p;
        int i = 0;
        while (true) {
            dArr = this.mCurveLength;
            if (i >= dArr.length - 1) {
                break;
            }
            double d2 = dArr[i];
            if (d2 >= d) {
                break;
            }
            d -= d2;
            i++;
        }
        return this.mCurve[splineNumber][i].eval(d / dArr[i]);
    }

    public void getPos(double p, double[] x) {
        double d = this.mTotalLength * p;
        int i = 0;
        while (true) {
            double[] dArr = this.mCurveLength;
            if (i >= dArr.length - 1) {
                break;
            }
            double d2 = dArr[i];
            if (d2 >= d) {
                break;
            }
            d -= d2;
            i++;
        }
        for (int i2 = 0; i2 < x.length; i2++) {
            x[i2] = this.mCurve[i2][i].eval(d / this.mCurveLength[i]);
        }
    }

    public void getPos(double p, float[] x) {
        double d = this.mTotalLength * p;
        int i = 0;
        while (true) {
            double[] dArr = this.mCurveLength;
            if (i >= dArr.length - 1) {
                break;
            }
            double d2 = dArr[i];
            if (d2 >= d) {
                break;
            }
            d -= d2;
            i++;
        }
        for (int i2 = 0; i2 < x.length; i2++) {
            x[i2] = (float) this.mCurve[i2][i].eval(d / this.mCurveLength[i]);
        }
    }

    public void getVelocity(double p, double[] v) {
        double d = this.mTotalLength * p;
        int i = 0;
        while (true) {
            double[] dArr = this.mCurveLength;
            if (i >= dArr.length - 1) {
                break;
            }
            double d2 = dArr[i];
            if (d2 >= d) {
                break;
            }
            d -= d2;
            i++;
        }
        for (int i2 = 0; i2 < v.length; i2++) {
            v[i2] = this.mCurve[i2][i].vel(d / this.mCurveLength[i]);
        }
    }

    public void setup(double[][] points) {
        int i;
        int length = points[0].length;
        this.mDimensionality = length;
        int length2 = points.length;
        this.mPoints = length2;
        int[] iArr = new int[2];
        iArr[1] = length2;
        iArr[0] = length;
        this.mCtl = (double[][]) Array.newInstance(Double.TYPE, iArr);
        this.mCurve = new Cubic[this.mDimensionality][];
        for (int i2 = 0; i2 < this.mDimensionality; i2++) {
            for (int i3 = 0; i3 < this.mPoints; i3++) {
                this.mCtl[i2][i3] = points[i3][i2];
            }
        }
        int i4 = 0;
        while (true) {
            i = this.mDimensionality;
            if (i4 >= i) {
                break;
            }
            Cubic[][] cubicArr = this.mCurve;
            double[] dArr = this.mCtl[i4];
            cubicArr[i4] = calcNaturalCubic(dArr.length, dArr);
            i4++;
        }
        this.mCurveLength = new double[(this.mPoints - 1)];
        this.mTotalLength = 0.0d;
        Cubic[] cubicArr2 = new Cubic[i];
        for (int i5 = 0; i5 < this.mCurveLength.length; i5++) {
            for (int i6 = 0; i6 < this.mDimensionality; i6++) {
                cubicArr2[i6] = this.mCurve[i6][i5];
            }
            double d = this.mTotalLength;
            double[] dArr2 = this.mCurveLength;
            double approxLength = approxLength(cubicArr2);
            dArr2[i5] = approxLength;
            this.mTotalLength = d + approxLength;
        }
    }
}

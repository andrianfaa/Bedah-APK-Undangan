package androidx.constraintlayout.core.motion.utils;

public class LinearCurveFit extends CurveFit {
    private static final String TAG = "LinearCurveFit";
    private boolean mExtrapolate = true;
    double[] mSlopeTemp;
    private double[] mT;
    private double mTotalLength = Double.NaN;
    private double[][] mY;

    public LinearCurveFit(double[] time, double[][] y) {
        double d;
        int i;
        double[] dArr = time;
        double[][] dArr2 = y;
        int length = dArr.length;
        char c = 0;
        int length2 = dArr2[0].length;
        this.mSlopeTemp = new double[length2];
        this.mT = dArr;
        this.mY = dArr2;
        if (length2 > 2) {
            double d2 = 0.0d;
            double d3 = 0.0d;
            double d4 = 0.0d;
            int i2 = 0;
            while (i2 < dArr.length) {
                double d5 = dArr2[i2][c];
                double d6 = dArr2[i2][c];
                if (i2 > 0) {
                    i = length2;
                    d = d5;
                    d2 += Math.hypot(d5 - d3, d6 - d4);
                } else {
                    i = length2;
                    d = d5;
                }
                d3 = d;
                d4 = d6;
                i2++;
                length2 = i;
                c = 0;
            }
            int i3 = length2;
            this.mTotalLength = 0.0d;
            return;
        }
        int i4 = length2;
    }

    private double getLength2D(double t) {
        if (Double.isNaN(this.mTotalLength)) {
            return 0.0d;
        }
        double[] dArr = this.mT;
        int length = dArr.length;
        if (t <= dArr[0]) {
            return 0.0d;
        }
        if (t >= dArr[length - 1]) {
            return this.mTotalLength;
        }
        double d = 0.0d;
        double d2 = 0.0d;
        double d3 = 0.0d;
        for (int i = 0; i < length - 1; i++) {
            double[] dArr2 = this.mY[i];
            double d4 = dArr2[0];
            double d5 = dArr2[1];
            if (i > 0) {
                double d6 = d2;
                d += Math.hypot(d4 - d2, d5 - d3);
            } else {
                double d7 = d2;
            }
            d2 = d4;
            d3 = d5;
            double[] dArr3 = this.mT;
            double d8 = dArr3[i];
            if (t == d8) {
                return d;
            }
            if (t < dArr3[i + 1]) {
                double d9 = (t - d8) / (dArr3[i + 1] - d8);
                double[][] dArr4 = this.mY;
                double[] dArr5 = dArr4[i];
                double d10 = dArr5[0];
                double d11 = dArr4[i + 1][0];
                int i2 = length;
                return d + Math.hypot(d5 - (((1.0d - d9) * dArr5[1]) + (dArr4[i + 1][1] * d9)), d4 - (((1.0d - d9) * d10) + (d11 * d9)));
            }
            int i3 = length;
        }
        return 0.0d;
    }

    public double getPos(double t, int j) {
        int i = j;
        double[] dArr = this.mT;
        int length = dArr.length;
        if (this.mExtrapolate) {
            double d = dArr[0];
            if (t <= d) {
                return this.mY[0][i] + ((t - d) * getSlope(d, i));
            }
            if (t >= dArr[length - 1]) {
                return this.mY[length - 1][i] + ((t - dArr[length - 1]) * getSlope(dArr[length - 1], i));
            }
        } else if (t <= dArr[0]) {
            return this.mY[0][i];
        } else {
            if (t >= dArr[length - 1]) {
                return this.mY[length - 1][i];
            }
        }
        for (int i2 = 0; i2 < length - 1; i2++) {
            double[] dArr2 = this.mT;
            double d2 = dArr2[i2];
            if (t == d2) {
                return this.mY[i2][i];
            }
            if (t < dArr2[i2 + 1]) {
                double d3 = (t - d2) / (dArr2[i2 + 1] - d2);
                double[][] dArr3 = this.mY;
                return ((1.0d - d3) * dArr3[i2][i]) + (dArr3[i2 + 1][i] * d3);
            }
        }
        return 0.0d;
    }

    public void getPos(double t, double[] v) {
        double[] dArr = this.mT;
        int length = dArr.length;
        int length2 = this.mY[0].length;
        if (this.mExtrapolate) {
            double d = dArr[0];
            if (t <= d) {
                getSlope(d, this.mSlopeTemp);
                for (int i = 0; i < length2; i++) {
                    v[i] = this.mY[0][i] + ((t - this.mT[0]) * this.mSlopeTemp[i]);
                }
                return;
            } else if (t >= dArr[length - 1]) {
                getSlope(dArr[length - 1], this.mSlopeTemp);
                for (int i2 = 0; i2 < length2; i2++) {
                    v[i2] = this.mY[length - 1][i2] + ((t - this.mT[length - 1]) * this.mSlopeTemp[i2]);
                }
                return;
            }
        } else if (t <= dArr[0]) {
            for (int i3 = 0; i3 < length2; i3++) {
                v[i3] = this.mY[0][i3];
            }
            return;
        } else if (t >= dArr[length - 1]) {
            for (int i4 = 0; i4 < length2; i4++) {
                v[i4] = this.mY[length - 1][i4];
            }
            return;
        }
        for (int i5 = 0; i5 < length - 1; i5++) {
            if (t == this.mT[i5]) {
                for (int i6 = 0; i6 < length2; i6++) {
                    v[i6] = this.mY[i5][i6];
                }
            }
            double[] dArr2 = this.mT;
            if (t < dArr2[i5 + 1]) {
                double d2 = dArr2[i5 + 1];
                double d3 = dArr2[i5];
                double d4 = (t - d3) / (d2 - d3);
                for (int i7 = 0; i7 < length2; i7++) {
                    double[][] dArr3 = this.mY;
                    v[i7] = ((1.0d - d4) * dArr3[i5][i7]) + (dArr3[i5 + 1][i7] * d4);
                }
                return;
            }
        }
    }

    public void getPos(double t, float[] v) {
        double[] dArr = this.mT;
        int length = dArr.length;
        int length2 = this.mY[0].length;
        if (this.mExtrapolate) {
            double d = dArr[0];
            if (t <= d) {
                getSlope(d, this.mSlopeTemp);
                for (int i = 0; i < length2; i++) {
                    v[i] = (float) (this.mY[0][i] + ((t - this.mT[0]) * this.mSlopeTemp[i]));
                }
                return;
            } else if (t >= dArr[length - 1]) {
                getSlope(dArr[length - 1], this.mSlopeTemp);
                for (int i2 = 0; i2 < length2; i2++) {
                    v[i2] = (float) (this.mY[length - 1][i2] + ((t - this.mT[length - 1]) * this.mSlopeTemp[i2]));
                }
                return;
            }
        } else if (t <= dArr[0]) {
            for (int i3 = 0; i3 < length2; i3++) {
                v[i3] = (float) this.mY[0][i3];
            }
            return;
        } else if (t >= dArr[length - 1]) {
            for (int i4 = 0; i4 < length2; i4++) {
                v[i4] = (float) this.mY[length - 1][i4];
            }
            return;
        }
        for (int i5 = 0; i5 < length - 1; i5++) {
            if (t == this.mT[i5]) {
                for (int i6 = 0; i6 < length2; i6++) {
                    v[i6] = (float) this.mY[i5][i6];
                }
            }
            double[] dArr2 = this.mT;
            if (t < dArr2[i5 + 1]) {
                double d2 = dArr2[i5 + 1];
                double d3 = dArr2[i5];
                double d4 = (t - d3) / (d2 - d3);
                for (int i7 = 0; i7 < length2; i7++) {
                    double[][] dArr3 = this.mY;
                    v[i7] = (float) (((1.0d - d4) * dArr3[i5][i7]) + (dArr3[i5 + 1][i7] * d4));
                }
                return;
            }
        }
    }

    public double getSlope(double t, int j) {
        double[] dArr = this.mT;
        int length = dArr.length;
        double d = t < dArr[0] ? dArr[0] : t >= dArr[length + -1] ? dArr[length - 1] : t;
        for (int i = 0; i < length - 1; i++) {
            double[] dArr2 = this.mT;
            if (d <= dArr2[i + 1]) {
                double d2 = dArr2[i + 1];
                double d3 = dArr2[i];
                double d4 = d2 - d3;
                double d5 = (d - d3) / d4;
                double[][] dArr3 = this.mY;
                return (dArr3[i + 1][j] - dArr3[i][j]) / d4;
            }
        }
        return 0.0d;
    }

    public void getSlope(double t, double[] v) {
        double[] dArr = this.mT;
        int length = dArr.length;
        int length2 = this.mY[0].length;
        double d = t <= dArr[0] ? dArr[0] : t >= dArr[length + -1] ? dArr[length - 1] : t;
        for (int i = 0; i < length - 1; i++) {
            double[] dArr2 = this.mT;
            if (d <= dArr2[i + 1]) {
                double d2 = dArr2[i + 1];
                double d3 = dArr2[i];
                double d4 = d2 - d3;
                double d5 = (d - d3) / d4;
                for (int i2 = 0; i2 < length2; i2++) {
                    double[][] dArr3 = this.mY;
                    v[i2] = (dArr3[i + 1][i2] - dArr3[i][i2]) / d4;
                }
                return;
            }
        }
    }

    public double[] getTimePoints() {
        return this.mT;
    }
}

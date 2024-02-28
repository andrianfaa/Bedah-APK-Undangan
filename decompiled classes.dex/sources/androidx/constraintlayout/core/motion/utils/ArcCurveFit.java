package androidx.constraintlayout.core.motion.utils;

import java.util.Arrays;

public class ArcCurveFit extends CurveFit {
    public static final int ARC_START_FLIP = 3;
    public static final int ARC_START_HORIZONTAL = 2;
    public static final int ARC_START_LINEAR = 0;
    public static final int ARC_START_VERTICAL = 1;
    private static final int START_HORIZONTAL = 2;
    private static final int START_LINEAR = 3;
    private static final int START_VERTICAL = 1;
    Arc[] mArcs;
    private boolean mExtrapolate = true;
    private final double[] mTime;

    private static class Arc {
        private static final double EPSILON = 0.001d;
        private static final String TAG = "Arc";
        private static double[] ourPercent = new double[91];
        boolean linear = false;
        double mArcDistance;
        double mArcVelocity;
        double mEllipseA;
        double mEllipseB;
        double mEllipseCenterX;
        double mEllipseCenterY;
        double[] mLut;
        double mOneOverDeltaTime;
        double mTime1;
        double mTime2;
        double mTmpCosAngle;
        double mTmpSinAngle;
        boolean mVertical;
        double mX1;
        double mX2;
        double mY1;
        double mY2;

        Arc(int mode, double t1, double t2, double x1, double y1, double x2, double y2) {
            double d;
            double d2;
            double d3;
            double d4;
            double d5;
            int i = mode;
            double d6 = t1;
            double d7 = t2;
            double d8 = x1;
            double d9 = y1;
            double d10 = x2;
            double d11 = y2;
            boolean z = false;
            int i2 = 1;
            this.mVertical = i == 1 ? true : z;
            this.mTime1 = d6;
            this.mTime2 = d7;
            this.mOneOverDeltaTime = 1.0d / (d7 - d6);
            if (3 == i) {
                this.linear = true;
            }
            double d12 = d10 - d8;
            double d13 = d11 - d9;
            if (this.linear || Math.abs(d12) < EPSILON) {
                d = d12;
                d2 = d13;
                d4 = d10;
                d3 = d9;
                d5 = d8;
            } else if (Math.abs(d13) < EPSILON) {
                d = d12;
                d2 = d13;
                d4 = d10;
                d3 = d9;
                d5 = d8;
            } else {
                this.mLut = new double[TypedValues.TYPE_TARGET];
                boolean z2 = this.mVertical;
                this.mEllipseA = ((double) (z2 ? -1 : i2)) * d12;
                this.mEllipseB = ((double) (z2 ? 1 : -1)) * d13;
                this.mEllipseCenterX = z2 ? d10 : d8;
                this.mEllipseCenterY = z2 ? d9 : y2;
                double d14 = y2;
                double d15 = d13;
                double d16 = d10;
                double d17 = d12;
                double d18 = d9;
                double d19 = d8;
                buildTable(x1, y1, x2, y2);
                this.mArcVelocity = this.mArcDistance * this.mOneOverDeltaTime;
                return;
            }
            this.linear = true;
            this.mX1 = d5;
            this.mX2 = d4;
            this.mY1 = d3;
            this.mY2 = y2;
            double d20 = d2;
            double d21 = d;
            double hypot = Math.hypot(d20, d21);
            this.mArcDistance = hypot;
            this.mArcVelocity = hypot * this.mOneOverDeltaTime;
            double d22 = this.mTime2;
            double d23 = this.mTime1;
            this.mEllipseCenterX = d21 / (d22 - d23);
            this.mEllipseCenterY = d20 / (d22 - d23);
        }

        private void buildTable(double x1, double y1, double x2, double y2) {
            double d;
            double d2;
            double d3 = x2 - x1;
            double d4 = y1 - y2;
            double d5 = 0.0d;
            double d6 = 0.0d;
            double d7 = 0.0d;
            int i = 0;
            while (true) {
                double[] dArr = ourPercent;
                if (i >= dArr.length) {
                    break;
                }
                double d8 = d7;
                double radians = Math.toRadians((((double) i) * 90.0d) / ((double) (dArr.length - 1)));
                double sin = d3 * Math.sin(radians);
                double cos = d4 * Math.cos(radians);
                if (i > 0) {
                    d2 = d3;
                    d = d4;
                    double hypot = Math.hypot(sin - d5, cos - d6) + d8;
                    ourPercent[i] = hypot;
                    d8 = hypot;
                } else {
                    d2 = d3;
                    d = d4;
                }
                d5 = sin;
                d6 = cos;
                i++;
                d7 = d8;
                d3 = d2;
                d4 = d;
            }
            double d9 = d3;
            double d10 = d4;
            double d11 = d7;
            this.mArcDistance = d7;
            int i2 = 0;
            while (true) {
                double[] dArr2 = ourPercent;
                if (i2 >= dArr2.length) {
                    break;
                }
                dArr2[i2] = dArr2[i2] / d7;
                i2++;
            }
            int i3 = 0;
            while (true) {
                double[] dArr3 = this.mLut;
                if (i3 < dArr3.length) {
                    double length = ((double) i3) / ((double) (dArr3.length - 1));
                    int binarySearch = Arrays.binarySearch(ourPercent, length);
                    if (binarySearch >= 0) {
                        this.mLut[i3] = ((double) binarySearch) / ((double) (ourPercent.length - 1));
                    } else if (binarySearch == -1) {
                        this.mLut[i3] = 0.0d;
                    } else {
                        int i4 = (-binarySearch) - 2;
                        double[] dArr4 = ourPercent;
                        double d12 = dArr4[i4];
                        double d13 = length;
                        int i5 = binarySearch;
                        this.mLut[i3] = (((double) i4) + ((length - d12) / (dArr4[(-binarySearch) - 1] - d12))) / ((double) (dArr4.length - 1));
                    }
                    i3++;
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public double getDX() {
            double d = this.mEllipseA * this.mTmpCosAngle;
            double hypot = this.mArcVelocity / Math.hypot(d, (-this.mEllipseB) * this.mTmpSinAngle);
            return this.mVertical ? (-d) * hypot : d * hypot;
        }

        /* access modifiers changed from: package-private */
        public double getDY() {
            double d = this.mEllipseA * this.mTmpCosAngle;
            double d2 = (-this.mEllipseB) * this.mTmpSinAngle;
            double hypot = this.mArcVelocity / Math.hypot(d, d2);
            return this.mVertical ? (-d2) * hypot : d2 * hypot;
        }

        public double getLinearDX(double t) {
            return this.mEllipseCenterX;
        }

        public double getLinearDY(double t) {
            return this.mEllipseCenterY;
        }

        public double getLinearX(double t) {
            double d = (t - this.mTime1) * this.mOneOverDeltaTime;
            double t2 = this.mX1;
            return t2 + ((this.mX2 - t2) * d);
        }

        public double getLinearY(double t) {
            double d = (t - this.mTime1) * this.mOneOverDeltaTime;
            double t2 = this.mY1;
            return t2 + ((this.mY2 - t2) * d);
        }

        /* access modifiers changed from: package-private */
        public double getX() {
            return this.mEllipseCenterX + (this.mEllipseA * this.mTmpSinAngle);
        }

        /* access modifiers changed from: package-private */
        public double getY() {
            return this.mEllipseCenterY + (this.mEllipseB * this.mTmpCosAngle);
        }

        /* access modifiers changed from: package-private */
        public double lookup(double v) {
            if (v <= 0.0d) {
                return 0.0d;
            }
            if (v >= 1.0d) {
                return 1.0d;
            }
            double[] dArr = this.mLut;
            double length = ((double) (dArr.length - 1)) * v;
            int i = (int) length;
            double d = dArr[i];
            return d + ((dArr[i + 1] - d) * (length - ((double) ((int) length))));
        }

        /* access modifiers changed from: package-private */
        public void setPoint(double time) {
            double lookup = lookup((this.mVertical ? this.mTime2 - time : time - this.mTime1) * this.mOneOverDeltaTime) * 1.5707963267948966d;
            this.mTmpSinAngle = Math.sin(lookup);
            this.mTmpCosAngle = Math.cos(lookup);
        }
    }

    public ArcCurveFit(int[] arcModes, double[] time, double[][] y) {
        double[] dArr = time;
        this.mTime = dArr;
        this.mArcs = new Arc[(dArr.length - 1)];
        int i = 1;
        int i2 = 1;
        int i3 = 0;
        while (true) {
            Arc[] arcArr = this.mArcs;
            if (i3 < arcArr.length) {
                int i4 = 2;
                switch (arcModes[i3]) {
                    case 0:
                        i = 3;
                        break;
                    case 1:
                        i = 1;
                        i2 = 1;
                        break;
                    case 2:
                        i = 2;
                        i2 = 2;
                        break;
                    case 3:
                        i = i2 != 1 ? 1 : i4;
                        i2 = i;
                        break;
                }
                arcArr[i3] = new Arc(i, dArr[i3], dArr[i3 + 1], y[i3][0], y[i3][1], y[i3 + 1][0], y[i3 + 1][1]);
                i3++;
            } else {
                return;
            }
        }
    }

    public double getPos(double t, int j) {
        if (this.mExtrapolate) {
            if (t < this.mArcs[0].mTime1) {
                double d = this.mArcs[0].mTime1;
                double d2 = t - this.mArcs[0].mTime1;
                if (this.mArcs[0].linear) {
                    return j == 0 ? this.mArcs[0].getLinearX(d) + (this.mArcs[0].getLinearDX(d) * d2) : this.mArcs[0].getLinearY(d) + (this.mArcs[0].getLinearDY(d) * d2);
                }
                this.mArcs[0].setPoint(d);
                return j == 0 ? this.mArcs[0].getX() + (this.mArcs[0].getDX() * d2) : this.mArcs[0].getY() + (this.mArcs[0].getDY() * d2);
            }
            Arc[] arcArr = this.mArcs;
            if (t > arcArr[arcArr.length - 1].mTime2) {
                Arc[] arcArr2 = this.mArcs;
                double d3 = arcArr2[arcArr2.length - 1].mTime2;
                double d4 = t - d3;
                Arc[] arcArr3 = this.mArcs;
                int length = arcArr3.length - 1;
                return j == 0 ? arcArr3[length].getLinearX(d3) + (this.mArcs[length].getLinearDX(d3) * d4) : arcArr3[length].getLinearY(d3) + (this.mArcs[length].getLinearDY(d3) * d4);
            }
        } else if (t < this.mArcs[0].mTime1) {
            t = this.mArcs[0].mTime1;
        } else {
            Arc[] arcArr4 = this.mArcs;
            if (t > arcArr4[arcArr4.length - 1].mTime2) {
                Arc[] arcArr5 = this.mArcs;
                t = arcArr5[arcArr5.length - 1].mTime2;
            }
        }
        int i = 0;
        while (true) {
            Arc[] arcArr6 = this.mArcs;
            if (i >= arcArr6.length) {
                return Double.NaN;
            }
            if (t > arcArr6[i].mTime2) {
                i++;
            } else if (this.mArcs[i].linear) {
                return j == 0 ? this.mArcs[i].getLinearX(t) : this.mArcs[i].getLinearY(t);
            } else {
                this.mArcs[i].setPoint(t);
                return j == 0 ? this.mArcs[i].getX() : this.mArcs[i].getY();
            }
        }
    }

    public void getPos(double t, double[] v) {
        if (!this.mExtrapolate) {
            if (t < this.mArcs[0].mTime1) {
                t = this.mArcs[0].mTime1;
            }
            Arc[] arcArr = this.mArcs;
            if (t > arcArr[arcArr.length - 1].mTime2) {
                Arc[] arcArr2 = this.mArcs;
                t = arcArr2[arcArr2.length - 1].mTime2;
            }
        } else if (t < this.mArcs[0].mTime1) {
            double d = this.mArcs[0].mTime1;
            double d2 = t - this.mArcs[0].mTime1;
            if (this.mArcs[0].linear) {
                v[0] = this.mArcs[0].getLinearX(d) + (this.mArcs[0].getLinearDX(d) * d2);
                v[1] = this.mArcs[0].getLinearY(d) + (this.mArcs[0].getLinearDY(d) * d2);
                return;
            }
            this.mArcs[0].setPoint(d);
            v[0] = this.mArcs[0].getX() + (this.mArcs[0].getDX() * d2);
            v[1] = this.mArcs[0].getY() + (this.mArcs[0].getDY() * d2);
            return;
        } else {
            Arc[] arcArr3 = this.mArcs;
            if (t > arcArr3[arcArr3.length - 1].mTime2) {
                Arc[] arcArr4 = this.mArcs;
                double d3 = arcArr4[arcArr4.length - 1].mTime2;
                double d4 = t - d3;
                Arc[] arcArr5 = this.mArcs;
                int length = arcArr5.length - 1;
                if (arcArr5[length].linear) {
                    v[0] = this.mArcs[length].getLinearX(d3) + (this.mArcs[length].getLinearDX(d3) * d4);
                    v[1] = this.mArcs[length].getLinearY(d3) + (this.mArcs[length].getLinearDY(d3) * d4);
                    return;
                }
                this.mArcs[length].setPoint(t);
                v[0] = this.mArcs[length].getX() + (this.mArcs[length].getDX() * d4);
                v[1] = this.mArcs[length].getY() + (this.mArcs[length].getDY() * d4);
                return;
            }
        }
        int i = 0;
        while (true) {
            Arc[] arcArr6 = this.mArcs;
            if (i >= arcArr6.length) {
                return;
            }
            if (t > arcArr6[i].mTime2) {
                i++;
            } else if (this.mArcs[i].linear) {
                v[0] = this.mArcs[i].getLinearX(t);
                v[1] = this.mArcs[i].getLinearY(t);
                return;
            } else {
                this.mArcs[i].setPoint(t);
                v[0] = this.mArcs[i].getX();
                v[1] = this.mArcs[i].getY();
                return;
            }
        }
    }

    public void getPos(double t, float[] v) {
        if (this.mExtrapolate) {
            if (t < this.mArcs[0].mTime1) {
                double d = this.mArcs[0].mTime1;
                double d2 = t - this.mArcs[0].mTime1;
                if (this.mArcs[0].linear) {
                    v[0] = (float) (this.mArcs[0].getLinearX(d) + (this.mArcs[0].getLinearDX(d) * d2));
                    v[1] = (float) (this.mArcs[0].getLinearY(d) + (this.mArcs[0].getLinearDY(d) * d2));
                    return;
                }
                this.mArcs[0].setPoint(d);
                v[0] = (float) (this.mArcs[0].getX() + (this.mArcs[0].getDX() * d2));
                v[1] = (float) (this.mArcs[0].getY() + (this.mArcs[0].getDY() * d2));
                return;
            }
            Arc[] arcArr = this.mArcs;
            if (t > arcArr[arcArr.length - 1].mTime2) {
                Arc[] arcArr2 = this.mArcs;
                double d3 = arcArr2[arcArr2.length - 1].mTime2;
                double d4 = t - d3;
                Arc[] arcArr3 = this.mArcs;
                int length = arcArr3.length - 1;
                if (arcArr3[length].linear) {
                    v[0] = (float) (this.mArcs[length].getLinearX(d3) + (this.mArcs[length].getLinearDX(d3) * d4));
                    v[1] = (float) (this.mArcs[length].getLinearY(d3) + (this.mArcs[length].getLinearDY(d3) * d4));
                    return;
                }
                this.mArcs[length].setPoint(t);
                v[0] = (float) this.mArcs[length].getX();
                v[1] = (float) this.mArcs[length].getY();
                return;
            }
        } else if (t < this.mArcs[0].mTime1) {
            t = this.mArcs[0].mTime1;
        } else {
            Arc[] arcArr4 = this.mArcs;
            if (t > arcArr4[arcArr4.length - 1].mTime2) {
                Arc[] arcArr5 = this.mArcs;
                t = arcArr5[arcArr5.length - 1].mTime2;
            }
        }
        int i = 0;
        while (true) {
            Arc[] arcArr6 = this.mArcs;
            if (i >= arcArr6.length) {
                return;
            }
            if (t > arcArr6[i].mTime2) {
                i++;
            } else if (this.mArcs[i].linear) {
                v[0] = (float) this.mArcs[i].getLinearX(t);
                v[1] = (float) this.mArcs[i].getLinearY(t);
                return;
            } else {
                this.mArcs[i].setPoint(t);
                v[0] = (float) this.mArcs[i].getX();
                v[1] = (float) this.mArcs[i].getY();
                return;
            }
        }
    }

    public double getSlope(double t, int j) {
        if (t < this.mArcs[0].mTime1) {
            t = this.mArcs[0].mTime1;
        }
        Arc[] arcArr = this.mArcs;
        if (t > arcArr[arcArr.length - 1].mTime2) {
            Arc[] arcArr2 = this.mArcs;
            t = arcArr2[arcArr2.length - 1].mTime2;
        }
        int i = 0;
        while (true) {
            Arc[] arcArr3 = this.mArcs;
            if (i >= arcArr3.length) {
                return Double.NaN;
            }
            if (t > arcArr3[i].mTime2) {
                i++;
            } else if (this.mArcs[i].linear) {
                return j == 0 ? this.mArcs[i].getLinearDX(t) : this.mArcs[i].getLinearDY(t);
            } else {
                this.mArcs[i].setPoint(t);
                return j == 0 ? this.mArcs[i].getDX() : this.mArcs[i].getDY();
            }
        }
    }

    public void getSlope(double t, double[] v) {
        if (t < this.mArcs[0].mTime1) {
            t = this.mArcs[0].mTime1;
        } else {
            Arc[] arcArr = this.mArcs;
            if (t > arcArr[arcArr.length - 1].mTime2) {
                Arc[] arcArr2 = this.mArcs;
                t = arcArr2[arcArr2.length - 1].mTime2;
            }
        }
        int i = 0;
        while (true) {
            Arc[] arcArr3 = this.mArcs;
            if (i >= arcArr3.length) {
                return;
            }
            if (t > arcArr3[i].mTime2) {
                i++;
            } else if (this.mArcs[i].linear) {
                v[0] = this.mArcs[i].getLinearDX(t);
                v[1] = this.mArcs[i].getLinearDY(t);
                return;
            } else {
                this.mArcs[i].setPoint(t);
                v[0] = this.mArcs[i].getDX();
                v[1] = this.mArcs[i].getDY();
                return;
            }
        }
    }

    public double[] getTimePoints() {
        return this.mTime;
    }
}

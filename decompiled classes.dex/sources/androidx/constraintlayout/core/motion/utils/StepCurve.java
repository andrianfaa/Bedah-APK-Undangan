package androidx.constraintlayout.core.motion.utils;

import java.lang.reflect.Array;
import java.util.Arrays;

public class StepCurve extends Easing {
    private static final boolean DEBUG = false;
    MonotonicCurveFit mCurveFit;

    StepCurve(String configString) {
        this.str = configString;
        double[] dArr = new double[(this.str.length() / 2)];
        int indexOf = configString.indexOf(40) + 1;
        int indexOf2 = configString.indexOf(44, indexOf);
        int i = 0;
        while (indexOf2 != -1) {
            int i2 = i + 1;
            dArr[i] = Double.parseDouble(configString.substring(indexOf, indexOf2).trim());
            int i3 = indexOf2 + 1;
            indexOf = i3;
            indexOf2 = configString.indexOf(44, i3);
            i = i2;
        }
        dArr[i] = Double.parseDouble(configString.substring(indexOf, configString.indexOf(41, indexOf)).trim());
        this.mCurveFit = genSpline(Arrays.copyOf(dArr, i + 1));
    }

    private static MonotonicCurveFit genSpline(String str) {
        String[] split = str.split("\\s+");
        double[] dArr = new double[split.length];
        for (int i = 0; i < dArr.length; i++) {
            dArr[i] = Double.parseDouble(split[i]);
        }
        return genSpline(dArr);
    }

    private static MonotonicCurveFit genSpline(double[] values) {
        double[] dArr = values;
        int length = (dArr.length * 3) - 2;
        int length2 = dArr.length - 1;
        double d = 1.0d / ((double) length2);
        int[] iArr = new int[2];
        iArr[1] = 1;
        iArr[0] = length;
        double[][] dArr2 = (double[][]) Array.newInstance(Double.TYPE, iArr);
        double[] dArr3 = new double[length];
        for (int i = 0; i < dArr.length; i++) {
            double d2 = dArr[i];
            dArr2[i + length2][0] = d2;
            dArr3[i + length2] = ((double) i) * d;
            if (i > 0) {
                dArr2[(length2 * 2) + i][0] = d2 + 1.0d;
                dArr3[(length2 * 2) + i] = (((double) i) * d) + 1.0d;
                dArr2[i - 1][0] = (d2 - 1.0d) - d;
                dArr3[i - 1] = ((((double) i) * d) - 4.0d) - d;
            }
        }
        MonotonicCurveFit monotonicCurveFit = new MonotonicCurveFit(dArr3, dArr2);
        System.out.println(" 0 " + monotonicCurveFit.getPos(0.0d, 0));
        System.out.println(" 1 " + monotonicCurveFit.getPos(1.0d, 0));
        return monotonicCurveFit;
    }

    public double get(double x) {
        return this.mCurveFit.getPos(x, 0);
    }

    public double getDiff(double x) {
        return this.mCurveFit.getSlope(x, 0);
    }
}

package androidx.constraintlayout.core.motion.utils;

import java.io.PrintStream;
import java.util.Arrays;
import mt.Log1F380D;

/* compiled from: 000F */
public class Easing {
    private static final String ACCELERATE = "cubic(0.4, 0.05, 0.8, 0.7)";
    private static final String ACCELERATE_NAME = "accelerate";
    private static final String ANTICIPATE = "cubic(0.36, 0, 0.66, -0.56)";
    private static final String ANTICIPATE_NAME = "anticipate";
    private static final String DECELERATE = "cubic(0.0, 0.0, 0.2, 0.95)";
    private static final String DECELERATE_NAME = "decelerate";
    private static final String LINEAR = "cubic(1, 1, 0, 0)";
    private static final String LINEAR_NAME = "linear";
    public static String[] NAMED_EASING = {STANDARD_NAME, ACCELERATE_NAME, DECELERATE_NAME, LINEAR_NAME};
    private static final String OVERSHOOT = "cubic(0.34, 1.56, 0.64, 1)";
    private static final String OVERSHOOT_NAME = "overshoot";
    private static final String STANDARD = "cubic(0.4, 0.0, 0.2, 1)";
    private static final String STANDARD_NAME = "standard";
    static Easing sDefault = new Easing();
    String str = "identity";

    static class CubicEasing extends Easing {
        private static double d_error = 1.0E-4d;
        private static double error = 0.01d;
        double x1;
        double x2;
        double y1;
        double y2;

        public CubicEasing(double x12, double y12, double x22, double y22) {
            setup(x12, y12, x22, y22);
        }

        CubicEasing(String configString) {
            this.str = configString;
            int indexOf = configString.indexOf(40);
            int indexOf2 = configString.indexOf(44, indexOf);
            this.x1 = Double.parseDouble(configString.substring(indexOf + 1, indexOf2).trim());
            int indexOf3 = configString.indexOf(44, indexOf2 + 1);
            this.y1 = Double.parseDouble(configString.substring(indexOf2 + 1, indexOf3).trim());
            int indexOf4 = configString.indexOf(44, indexOf3 + 1);
            this.x2 = Double.parseDouble(configString.substring(indexOf3 + 1, indexOf4).trim());
            this.y2 = Double.parseDouble(configString.substring(indexOf4 + 1, configString.indexOf(41, indexOf4 + 1)).trim());
        }

        private double getDiffX(double t) {
            double d = 1.0d - t;
            double d2 = this.x1;
            double d3 = this.x2;
            return (d * 3.0d * d * d2) + (6.0d * d * t * (d3 - d2)) + (3.0d * t * t * (1.0d - d3));
        }

        private double getDiffY(double t) {
            double d = 1.0d - t;
            double d2 = this.y1;
            double d3 = this.y2;
            return (d * 3.0d * d * d2) + (6.0d * d * t * (d3 - d2)) + (3.0d * t * t * (1.0d - d3));
        }

        private double getX(double t) {
            double d = 1.0d - t;
            return (this.x1 * d * 3.0d * d * t) + (this.x2 * 3.0d * d * t * t) + (t * t * t);
        }

        private double getY(double t) {
            double d = 1.0d - t;
            return (this.y1 * d * 3.0d * d * t) + (this.y2 * 3.0d * d * t * t) + (t * t * t);
        }

        public double get(double x) {
            if (x <= 0.0d) {
                return 0.0d;
            }
            if (x >= 1.0d) {
                return 1.0d;
            }
            double d = 0.5d;
            double d2 = 0.5d;
            while (d2 > error) {
                d2 *= 0.5d;
                d = getX(d) < x ? d + d2 : d - d2;
            }
            double x3 = getX(d - d2);
            double x4 = getX(d + d2);
            double y = getY(d - d2);
            return (((getY(d + d2) - y) * (x - x3)) / (x4 - x3)) + y;
        }

        public double getDiff(double x) {
            double d = 0.5d;
            double d2 = 0.5d;
            while (d2 > d_error) {
                d2 *= 0.5d;
                d = getX(d) < x ? d + d2 : d - d2;
            }
            double x3 = getX(d - d2);
            return (getY(d + d2) - getY(d - d2)) / (getX(d + d2) - x3);
        }

        /* access modifiers changed from: package-private */
        public void setup(double x12, double y12, double x22, double y22) {
            this.x1 = x12;
            this.y1 = y12;
            this.x2 = x22;
            this.y2 = y22;
        }
    }

    public static Easing getInterpolator(String configString) {
        if (configString == null) {
            return null;
        }
        if (configString.startsWith("cubic")) {
            return new CubicEasing(configString);
        }
        if (configString.startsWith("spline")) {
            return new StepCurve(configString);
        }
        if (configString.startsWith("Schlick")) {
            return new Schlick(configString);
        }
        char c = 65535;
        switch (configString.hashCode()) {
            case -1354466595:
                if (configString.equals(ACCELERATE_NAME)) {
                    c = 1;
                    break;
                }
                break;
            case -1263948740:
                if (configString.equals(DECELERATE_NAME)) {
                    c = 2;
                    break;
                }
                break;
            case -1197605014:
                if (configString.equals(ANTICIPATE_NAME)) {
                    c = 4;
                    break;
                }
                break;
            case -1102672091:
                if (configString.equals(LINEAR_NAME)) {
                    c = 3;
                    break;
                }
                break;
            case -749065269:
                if (configString.equals(OVERSHOOT_NAME)) {
                    c = 5;
                    break;
                }
                break;
            case 1312628413:
                if (configString.equals(STANDARD_NAME)) {
                    c = 0;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return new CubicEasing(STANDARD);
            case 1:
                return new CubicEasing(ACCELERATE);
            case 2:
                return new CubicEasing(DECELERATE);
            case 3:
                return new CubicEasing(LINEAR);
            case 4:
                return new CubicEasing(ANTICIPATE);
            case 5:
                return new CubicEasing(OVERSHOOT);
            default:
                PrintStream printStream = System.err;
                StringBuilder append = new StringBuilder().append("transitionEasing syntax error syntax:transitionEasing=\"cubic(1.0,0.5,0.0,0.6)\" or ");
                String arrays = Arrays.toString(NAMED_EASING);
                Log1F380D.a((Object) arrays);
                printStream.println(append.append(arrays).toString());
                return sDefault;
        }
    }

    public double get(double x) {
        return x;
    }

    public double getDiff(double x) {
        return 1.0d;
    }

    public String toString() {
        return this.str;
    }
}

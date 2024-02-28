package androidx.core.math;

public class MathUtils {
    private MathUtils() {
    }

    public static int addExact(int x, int y) {
        int i = x + y;
        if (((x ^ i) & (y ^ i)) >= 0) {
            return i;
        }
        throw new ArithmeticException("integer overflow");
    }

    public static long addExact(long x, long y) {
        long j = x + y;
        if (((x ^ j) & (y ^ j)) >= 0) {
            return j;
        }
        throw new ArithmeticException("long overflow");
    }

    public static double clamp(double value, double min, double max) {
        return value < min ? min : value > max ? max : value;
    }

    public static float clamp(float value, float min, float max) {
        return value < min ? min : value > max ? max : value;
    }

    public static int clamp(int value, int min, int max) {
        return value < min ? min : value > max ? max : value;
    }

    public static long clamp(long value, long min, long max) {
        return value < min ? min : value > max ? max : value;
    }

    public static int decrementExact(int a) {
        if (a != Integer.MIN_VALUE) {
            return a - 1;
        }
        throw new ArithmeticException("integer overflow");
    }

    public static long decrementExact(long a) {
        if (a != Long.MIN_VALUE) {
            return a - 1;
        }
        throw new ArithmeticException("long overflow");
    }

    public static int incrementExact(int a) {
        if (a != Integer.MAX_VALUE) {
            return a + 1;
        }
        throw new ArithmeticException("integer overflow");
    }

    public static long incrementExact(long a) {
        if (a != Long.MAX_VALUE) {
            return 1 + a;
        }
        throw new ArithmeticException("long overflow");
    }

    public static int multiplyExact(int x, int y) {
        long j = ((long) x) * ((long) y);
        if (((long) ((int) j)) == j) {
            return (int) j;
        }
        throw new ArithmeticException("integer overflow");
    }

    public static long multiplyExact(long x, long y) {
        long j = x * y;
        if (((Math.abs(x) | Math.abs(y)) >>> 31) == 0 || ((y == 0 || j / y == x) && (x != Long.MIN_VALUE || y != -1))) {
            return j;
        }
        throw new ArithmeticException("long overflow");
    }

    public static int negateExact(int a) {
        if (a != Integer.MIN_VALUE) {
            return -a;
        }
        throw new ArithmeticException("integer overflow");
    }

    public static long negateExact(long a) {
        if (a != Long.MIN_VALUE) {
            return -a;
        }
        throw new ArithmeticException("long overflow");
    }

    public static int subtractExact(int x, int y) {
        int i = x - y;
        if (((x ^ y) & (x ^ i)) >= 0) {
            return i;
        }
        throw new ArithmeticException("integer overflow");
    }

    public static long subtractExact(long x, long y) {
        long j = x - y;
        if (((x ^ y) & (x ^ j)) >= 0) {
            return j;
        }
        throw new ArithmeticException("long overflow");
    }

    public static int toIntExact(long value) {
        if (((long) ((int) value)) == value) {
            return (int) value;
        }
        throw new ArithmeticException("integer overflow");
    }
}

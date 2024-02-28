package androidx.core.util;

import android.text.TextUtils;
import java.util.Locale;
import mt.Log1F380D;

/* compiled from: 005C */
public final class Preconditions {
    private Preconditions() {
    }

    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            String valueOf = String.valueOf(errorMessage);
            Log1F380D.a((Object) valueOf);
            throw new IllegalArgumentException(valueOf);
        }
    }

    public static float checkArgumentFinite(float value, String valueName) {
        if (Float.isNaN(value)) {
            throw new IllegalArgumentException(valueName + " must not be NaN");
        } else if (!Float.isInfinite(value)) {
            return value;
        } else {
            throw new IllegalArgumentException(valueName + " must not be infinite");
        }
    }

    public static int checkArgumentNonnegative(int value) {
        if (value >= 0) {
            return value;
        }
        throw new IllegalArgumentException();
    }

    public static int checkArgumentNonnegative(int value, String errorMessage) {
        if (value >= 0) {
            return value;
        }
        throw new IllegalArgumentException(errorMessage);
    }

    public static <T> T checkNotNull(T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException();
    }

    public static void checkState(boolean expression) {
        checkState(expression, (String) null);
    }

    public static void checkState(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static <T extends CharSequence> T checkStringNotEmpty(T t) {
        if (!TextUtils.isEmpty(t)) {
            return t;
        }
        throw new IllegalArgumentException();
    }

    public static void checkArgument(boolean expression, String messageTemplate, Object... messageArgs) {
        if (!expression) {
            String format = String.format(messageTemplate, messageArgs);
            Log1F380D.a((Object) format);
            throw new IllegalArgumentException(format);
        }
    }

    public static double checkArgumentInRange(double value, double lower, double upper, String valueName) {
        if (value < lower) {
            String format = String.format(Locale.US, "%s is out of range of [%f, %f] (too low)", new Object[]{valueName, Double.valueOf(lower), Double.valueOf(upper)});
            Log1F380D.a((Object) format);
            throw new IllegalArgumentException(format);
        } else if (value <= upper) {
            return value;
        } else {
            String format2 = String.format(Locale.US, "%s is out of range of [%f, %f] (too high)", new Object[]{valueName, Double.valueOf(lower), Double.valueOf(upper)});
            Log1F380D.a((Object) format2);
            throw new IllegalArgumentException(format2);
        }
    }

    public static float checkArgumentInRange(float value, float lower, float upper, String valueName) {
        if (value < lower) {
            String format = String.format(Locale.US, "%s is out of range of [%f, %f] (too low)", new Object[]{valueName, Float.valueOf(lower), Float.valueOf(upper)});
            Log1F380D.a((Object) format);
            throw new IllegalArgumentException(format);
        } else if (value <= upper) {
            return value;
        } else {
            String format2 = String.format(Locale.US, "%s is out of range of [%f, %f] (too high)", new Object[]{valueName, Float.valueOf(lower), Float.valueOf(upper)});
            Log1F380D.a((Object) format2);
            throw new IllegalArgumentException(format2);
        }
    }

    public static int checkArgumentInRange(int value, int lower, int upper, String valueName) {
        if (value < lower) {
            String format = String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", new Object[]{valueName, Integer.valueOf(lower), Integer.valueOf(upper)});
            Log1F380D.a((Object) format);
            throw new IllegalArgumentException(format);
        } else if (value <= upper) {
            return value;
        } else {
            String format2 = String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", new Object[]{valueName, Integer.valueOf(lower), Integer.valueOf(upper)});
            Log1F380D.a((Object) format2);
            throw new IllegalArgumentException(format2);
        }
    }

    public static long checkArgumentInRange(long value, long lower, long upper, String valueName) {
        if (value < lower) {
            String format = String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", new Object[]{valueName, Long.valueOf(lower), Long.valueOf(upper)});
            Log1F380D.a((Object) format);
            throw new IllegalArgumentException(format);
        } else if (value <= upper) {
            return value;
        } else {
            String format2 = String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", new Object[]{valueName, Long.valueOf(lower), Long.valueOf(upper)});
            Log1F380D.a((Object) format2);
            throw new IllegalArgumentException(format2);
        }
    }

    public static int checkFlagsArgument(int requestedFlags, int allowedFlags) {
        if ((requestedFlags & allowedFlags) == requestedFlags) {
            return requestedFlags;
        }
        StringBuilder append = new StringBuilder().append("Requested flags 0x");
        String hexString = Integer.toHexString(requestedFlags);
        Log1F380D.a((Object) hexString);
        StringBuilder append2 = append.append(hexString).append(", but only 0x");
        String hexString2 = Integer.toHexString(allowedFlags);
        Log1F380D.a((Object) hexString2);
        throw new IllegalArgumentException(append2.append(hexString2).append(" are allowed").toString());
    }

    public static <T> T checkNotNull(T t, Object errorMessage) {
        if (t != null) {
            return t;
        }
        String valueOf = String.valueOf(errorMessage);
        Log1F380D.a((Object) valueOf);
        throw new NullPointerException(valueOf);
    }

    public static <T extends CharSequence> T checkStringNotEmpty(T t, Object errorMessage) {
        if (!TextUtils.isEmpty(t)) {
            return t;
        }
        String valueOf = String.valueOf(errorMessage);
        Log1F380D.a((Object) valueOf);
        throw new IllegalArgumentException(valueOf);
    }

    public static <T extends CharSequence> T checkStringNotEmpty(T t, String messageTemplate, Object... messageArgs) {
        if (!TextUtils.isEmpty(t)) {
            return t;
        }
        String format = String.format(messageTemplate, messageArgs);
        Log1F380D.a((Object) format);
        throw new IllegalArgumentException(format);
    }
}

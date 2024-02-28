package androidx.core.util;

import android.os.Build;
import java.util.Arrays;
import java.util.Objects;

public class ObjectsCompat {

    static class Api19Impl {
        private Api19Impl() {
        }

        static boolean equals(Object a, Object b) {
            return Objects.equals(a, b);
        }

        static int hash(Object... values) {
            return Objects.hash(values);
        }
    }

    private ObjectsCompat() {
    }

    public static boolean equals(Object a, Object b) {
        return Build.VERSION.SDK_INT >= 19 ? Api19Impl.equals(a, b) : a == b || (a != null && a.equals(b));
    }

    public static int hash(Object... values) {
        return Build.VERSION.SDK_INT >= 19 ? Api19Impl.hash(values) : Arrays.hashCode(values);
    }

    public static int hashCode(Object o) {
        if (o != null) {
            return o.hashCode();
        }
        return 0;
    }

    public static <T> T requireNonNull(T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException();
    }

    public static <T> T requireNonNull(T t, String message) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(message);
    }

    public static String toString(Object o, String nullDefault) {
        return o != null ? o.toString() : nullDefault;
    }
}

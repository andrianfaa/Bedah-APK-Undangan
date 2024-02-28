package androidx.core.view;

import android.os.Build;
import android.view.View;
import android.view.Window;

public final class WindowCompat {
    public static final int FEATURE_ACTION_BAR = 8;
    public static final int FEATURE_ACTION_BAR_OVERLAY = 9;
    public static final int FEATURE_ACTION_MODE_OVERLAY = 10;

    static class Api16Impl {
        private Api16Impl() {
        }

        static void setDecorFitsSystemWindows(Window window, boolean decorFitsSystemWindows) {
            View decorView = window.getDecorView();
            int systemUiVisibility = decorView.getSystemUiVisibility();
            decorView.setSystemUiVisibility(decorFitsSystemWindows ? systemUiVisibility & -1793 : systemUiVisibility | 1792);
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static <T> T requireViewById(Window window, int id) {
            return window.requireViewById(id);
        }
    }

    static class Api30Impl {
        private Api30Impl() {
        }

        static void setDecorFitsSystemWindows(Window window, boolean decorFitsSystemWindows) {
            window.setDecorFitsSystemWindows(decorFitsSystemWindows);
        }
    }

    private WindowCompat() {
    }

    public static WindowInsetsControllerCompat getInsetsController(Window window, View view) {
        return new WindowInsetsControllerCompat(window, view);
    }

    public static <T extends View> T requireViewById(Window window, int id) {
        if (Build.VERSION.SDK_INT >= 28) {
            return (View) Api28Impl.requireViewById(window, id);
        }
        T findViewById = window.findViewById(id);
        if (findViewById != null) {
            return findViewById;
        }
        throw new IllegalArgumentException("ID does not reference a View inside this Window");
    }

    public static void setDecorFitsSystemWindows(Window window, boolean decorFitsSystemWindows) {
        if (Build.VERSION.SDK_INT >= 30) {
            Api30Impl.setDecorFitsSystemWindows(window, decorFitsSystemWindows);
        } else if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.setDecorFitsSystemWindows(window, decorFitsSystemWindows);
        }
    }
}

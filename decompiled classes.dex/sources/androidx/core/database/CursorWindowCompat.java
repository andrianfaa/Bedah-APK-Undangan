package androidx.core.database;

import android.database.CursorWindow;
import android.os.Build;

public final class CursorWindowCompat {

    static class Api15Impl {
        private Api15Impl() {
        }

        static CursorWindow createCursorWindow(String name) {
            return new CursorWindow(name);
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static CursorWindow createCursorWindow(String name, long windowSizeBytes) {
            return new CursorWindow(name, windowSizeBytes);
        }
    }

    private CursorWindowCompat() {
    }

    public static CursorWindow create(String name, long windowSizeBytes) {
        return Build.VERSION.SDK_INT >= 28 ? Api28Impl.createCursorWindow(name, windowSizeBytes) : Build.VERSION.SDK_INT >= 15 ? Api15Impl.createCursorWindow(name) : new CursorWindow(false);
    }
}

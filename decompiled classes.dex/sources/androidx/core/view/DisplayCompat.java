package androidx.core.view;

import android.app.UiModeManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import androidx.core.util.Preconditions;
import mt.Log1F380D;

/* compiled from: 0060 */
public final class DisplayCompat {
    private static final int DISPLAY_SIZE_4K_HEIGHT = 2160;
    private static final int DISPLAY_SIZE_4K_WIDTH = 3840;

    static class Api17Impl {
        private Api17Impl() {
        }

        static void getRealSize(Display display, Point displaySize) {
            display.getRealSize(displaySize);
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        static ModeCompat getMode(Context context, Display display) {
            Display.Mode mode = display.getMode();
            Point currentDisplaySizeFromWorkarounds = DisplayCompat.getCurrentDisplaySizeFromWorkarounds(context, display);
            return (currentDisplaySizeFromWorkarounds == null || physicalSizeEquals(mode, currentDisplaySizeFromWorkarounds)) ? new ModeCompat(mode, true) : new ModeCompat(mode, currentDisplaySizeFromWorkarounds);
        }

        public static ModeCompat[] getSupportedModes(Context context, Display display) {
            Display.Mode[] supportedModes = display.getSupportedModes();
            ModeCompat[] modeCompatArr = new ModeCompat[supportedModes.length];
            Display.Mode mode = display.getMode();
            Point currentDisplaySizeFromWorkarounds = DisplayCompat.getCurrentDisplaySizeFromWorkarounds(context, display);
            if (currentDisplaySizeFromWorkarounds == null || physicalSizeEquals(mode, currentDisplaySizeFromWorkarounds)) {
                for (int i = 0; i < supportedModes.length; i++) {
                    modeCompatArr[i] = new ModeCompat(supportedModes[i], physicalSizeEquals(supportedModes[i], mode));
                }
            } else {
                for (int i2 = 0; i2 < supportedModes.length; i2++) {
                    modeCompatArr[i2] = physicalSizeEquals(supportedModes[i2], mode) ? new ModeCompat(supportedModes[i2], currentDisplaySizeFromWorkarounds) : new ModeCompat(supportedModes[i2], false);
                }
            }
            return modeCompatArr;
        }

        static boolean isCurrentModeTheLargestMode(Display display) {
            Display.Mode mode = display.getMode();
            for (Display.Mode mode2 : display.getSupportedModes()) {
                if (mode.getPhysicalHeight() < mode2.getPhysicalHeight() || mode.getPhysicalWidth() < mode2.getPhysicalWidth()) {
                    return false;
                }
            }
            return true;
        }

        static boolean physicalSizeEquals(Display.Mode mode, Point size) {
            return (mode.getPhysicalWidth() == size.x && mode.getPhysicalHeight() == size.y) || (mode.getPhysicalWidth() == size.y && mode.getPhysicalHeight() == size.x);
        }

        static boolean physicalSizeEquals(Display.Mode mode, Display.Mode otherMode) {
            return mode.getPhysicalWidth() == otherMode.getPhysicalWidth() && mode.getPhysicalHeight() == otherMode.getPhysicalHeight();
        }
    }

    public static final class ModeCompat {
        private final boolean mIsNative;
        private final Display.Mode mMode;
        private final Point mPhysicalSize;

        static class Api23Impl {
            private Api23Impl() {
            }

            static int getPhysicalHeight(Display.Mode mode) {
                return mode.getPhysicalHeight();
            }

            static int getPhysicalWidth(Display.Mode mode) {
                return mode.getPhysicalWidth();
            }
        }

        ModeCompat(Point physicalSize) {
            Preconditions.checkNotNull(physicalSize, "physicalSize == null");
            this.mPhysicalSize = physicalSize;
            this.mMode = null;
            this.mIsNative = true;
        }

        ModeCompat(Display.Mode mode, Point physicalSize) {
            Preconditions.checkNotNull(mode, "mode == null, can't wrap a null reference");
            Preconditions.checkNotNull(physicalSize, "physicalSize == null");
            this.mPhysicalSize = physicalSize;
            this.mMode = mode;
            this.mIsNative = true;
        }

        ModeCompat(Display.Mode mode, boolean isNative) {
            Preconditions.checkNotNull(mode, "mode == null, can't wrap a null reference");
            this.mPhysicalSize = new Point(Api23Impl.getPhysicalWidth(mode), Api23Impl.getPhysicalHeight(mode));
            this.mMode = mode;
            this.mIsNative = isNative;
        }

        public int getPhysicalHeight() {
            return this.mPhysicalSize.y;
        }

        public int getPhysicalWidth() {
            return this.mPhysicalSize.x;
        }

        @Deprecated
        public boolean isNative() {
            return this.mIsNative;
        }

        public Display.Mode toMode() {
            return this.mMode;
        }
    }

    private DisplayCompat() {
    }

    static Point getCurrentDisplaySizeFromWorkarounds(Context context, Display display) {
        Point parsePhysicalDisplaySizeFromSystemProperties = Build.VERSION.SDK_INT < 28 ? parsePhysicalDisplaySizeFromSystemProperties("sys.display-size", display) : parsePhysicalDisplaySizeFromSystemProperties("vendor.display-size", display);
        if (parsePhysicalDisplaySizeFromSystemProperties != null) {
            return parsePhysicalDisplaySizeFromSystemProperties;
        }
        if (!isSonyBravia4kTv(context) || !isCurrentModeTheLargestMode(display)) {
            return null;
        }
        return new Point(DISPLAY_SIZE_4K_WIDTH, DISPLAY_SIZE_4K_HEIGHT);
    }

    private static Point getDisplaySize(Context context, Display display) {
        Point currentDisplaySizeFromWorkarounds = getCurrentDisplaySizeFromWorkarounds(context, display);
        if (currentDisplaySizeFromWorkarounds != null) {
            return currentDisplaySizeFromWorkarounds;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= 17) {
            Api17Impl.getRealSize(display, point);
        } else {
            display.getSize(point);
        }
        return point;
    }

    public static ModeCompat getMode(Context context, Display display) {
        return Build.VERSION.SDK_INT >= 23 ? Api23Impl.getMode(context, display) : new ModeCompat(getDisplaySize(context, display));
    }

    public static ModeCompat[] getSupportedModes(Context context, Display display) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.getSupportedModes(context, display);
        }
        return new ModeCompat[]{getMode(context, display)};
    }

    private static String getSystemProperty(String name) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", new Class[]{String.class}).invoke(cls, new Object[]{name});
        } catch (Exception e) {
            return null;
        }
    }

    static boolean isCurrentModeTheLargestMode(Display display) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.isCurrentModeTheLargestMode(display);
        }
        return true;
    }

    private static boolean isSonyBravia4kTv(Context context) {
        return isTv(context) && "Sony".equals(Build.MANUFACTURER) && Build.MODEL.startsWith("BRAVIA") && context.getPackageManager().hasSystemFeature("com.sony.dtv.hardware.panel.qfhd");
    }

    private static boolean isTv(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService("uimode");
        return uiModeManager != null && uiModeManager.getCurrentModeType() == 4;
    }

    private static Point parseDisplaySize(String displaySize) throws NumberFormatException {
        String[] split = displaySize.trim().split("x", -1);
        if (split.length == 2) {
            int parseInt = Integer.parseInt(split[0]);
            int parseInt2 = Integer.parseInt(split[1]);
            if (parseInt > 0 && parseInt2 > 0) {
                return new Point(parseInt, parseInt2);
            }
        }
        throw new NumberFormatException();
    }

    private static Point parsePhysicalDisplaySizeFromSystemProperties(String property, Display display) {
        if (display.getDisplayId() != 0) {
            return null;
        }
        String systemProperty = getSystemProperty(property);
        Log1F380D.a((Object) systemProperty);
        if (TextUtils.isEmpty(systemProperty) || systemProperty == null) {
            return null;
        }
        try {
            return parseDisplaySize(systemProperty);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

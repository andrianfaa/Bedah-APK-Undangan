package androidx.core.hardware.display;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;
import java.util.WeakHashMap;

public final class DisplayManagerCompat {
    public static final String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";
    private static final WeakHashMap<Context, DisplayManagerCompat> sInstances = new WeakHashMap<>();
    private final Context mContext;

    static class Api17Impl {
        private Api17Impl() {
        }

        static Display getDisplay(DisplayManager displayManager, int displayId) {
            return displayManager.getDisplay(displayId);
        }

        static Display[] getDisplays(DisplayManager displayManager) {
            return displayManager.getDisplays();
        }
    }

    private DisplayManagerCompat(Context context) {
        this.mContext = context;
    }

    public static DisplayManagerCompat getInstance(Context context) {
        DisplayManagerCompat displayManagerCompat;
        WeakHashMap<Context, DisplayManagerCompat> weakHashMap = sInstances;
        synchronized (weakHashMap) {
            displayManagerCompat = weakHashMap.get(context);
            if (displayManagerCompat == null) {
                displayManagerCompat = new DisplayManagerCompat(context);
                weakHashMap.put(context, displayManagerCompat);
            }
        }
        return displayManagerCompat;
    }

    public Display getDisplay(int displayId) {
        if (Build.VERSION.SDK_INT >= 17) {
            return Api17Impl.getDisplay((DisplayManager) this.mContext.getSystemService("display"), displayId);
        }
        Display defaultDisplay = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay();
        if (defaultDisplay.getDisplayId() == displayId) {
            return defaultDisplay;
        }
        return null;
    }

    public Display[] getDisplays() {
        if (Build.VERSION.SDK_INT >= 17) {
            return Api17Impl.getDisplays((DisplayManager) this.mContext.getSystemService("display"));
        }
        return new Display[]{((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay()};
    }

    public Display[] getDisplays(String category) {
        if (Build.VERSION.SDK_INT >= 17) {
            return Api17Impl.getDisplays((DisplayManager) this.mContext.getSystemService("display"));
        }
        if (category == null) {
            return new Display[0];
        }
        return new Display[]{((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay()};
    }
}

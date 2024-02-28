package androidx.core.widget;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class PopupWindowCompat {
    private static final String TAG = "PopupWindowCompatApi21";
    private static Method sGetWindowLayoutTypeMethod;
    private static boolean sGetWindowLayoutTypeMethodAttempted;
    private static Field sOverlapAnchorField;
    private static boolean sOverlapAnchorFieldAttempted;
    private static Method sSetWindowLayoutTypeMethod;
    private static boolean sSetWindowLayoutTypeMethodAttempted;

    static class Api19Impl {
        private Api19Impl() {
        }

        static void showAsDropDown(PopupWindow popupWindow, View anchor, int xoff, int yoff, int gravity) {
            popupWindow.showAsDropDown(anchor, xoff, yoff, gravity);
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        static boolean getOverlapAnchor(PopupWindow popupWindow) {
            return popupWindow.getOverlapAnchor();
        }

        static int getWindowLayoutType(PopupWindow popupWindow) {
            return popupWindow.getWindowLayoutType();
        }

        static void setOverlapAnchor(PopupWindow popupWindow, boolean overlapAnchor) {
            popupWindow.setOverlapAnchor(overlapAnchor);
        }

        static void setWindowLayoutType(PopupWindow popupWindow, int layoutType) {
            popupWindow.setWindowLayoutType(layoutType);
        }
    }

    private PopupWindowCompat() {
    }

    public static boolean getOverlapAnchor(PopupWindow popupWindow) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.getOverlapAnchor(popupWindow);
        }
        if (Build.VERSION.SDK_INT < 21) {
            return false;
        }
        if (!sOverlapAnchorFieldAttempted) {
            try {
                Field declaredField = PopupWindow.class.getDeclaredField("mOverlapAnchor");
                sOverlapAnchorField = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                Log.i(TAG, "Could not fetch mOverlapAnchor field from PopupWindow", e);
            }
            sOverlapAnchorFieldAttempted = true;
        }
        Field field = sOverlapAnchorField;
        if (field == null) {
            return false;
        }
        try {
            return ((Boolean) field.get(popupWindow)).booleanValue();
        } catch (IllegalAccessException e2) {
            Log.i(TAG, "Could not get overlap anchor field in PopupWindow", e2);
            return false;
        }
    }

    public static int getWindowLayoutType(PopupWindow popupWindow) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.getWindowLayoutType(popupWindow);
        }
        if (!sGetWindowLayoutTypeMethodAttempted) {
            try {
                Method declaredMethod = PopupWindow.class.getDeclaredMethod("getWindowLayoutType", new Class[0]);
                sGetWindowLayoutTypeMethod = declaredMethod;
                declaredMethod.setAccessible(true);
            } catch (Exception e) {
            }
            sGetWindowLayoutTypeMethodAttempted = true;
        }
        Method method = sGetWindowLayoutTypeMethod;
        if (method != null) {
            try {
                return ((Integer) method.invoke(popupWindow, new Object[0])).intValue();
            } catch (Exception e2) {
            }
        }
        return 0;
    }

    public static void setOverlapAnchor(PopupWindow popupWindow, boolean overlapAnchor) {
        if (Build.VERSION.SDK_INT >= 23) {
            Api23Impl.setOverlapAnchor(popupWindow, overlapAnchor);
        } else if (Build.VERSION.SDK_INT >= 21) {
            if (!sOverlapAnchorFieldAttempted) {
                try {
                    Field declaredField = PopupWindow.class.getDeclaredField("mOverlapAnchor");
                    sOverlapAnchorField = declaredField;
                    declaredField.setAccessible(true);
                } catch (NoSuchFieldException e) {
                    Log.i(TAG, "Could not fetch mOverlapAnchor field from PopupWindow", e);
                }
                sOverlapAnchorFieldAttempted = true;
            }
            Field field = sOverlapAnchorField;
            if (field != null) {
                try {
                    field.set(popupWindow, Boolean.valueOf(overlapAnchor));
                } catch (IllegalAccessException e2) {
                    Log.i(TAG, "Could not set overlap anchor field in PopupWindow", e2);
                }
            }
        }
    }

    public static void setWindowLayoutType(PopupWindow popupWindow, int layoutType) {
        if (Build.VERSION.SDK_INT >= 23) {
            Api23Impl.setWindowLayoutType(popupWindow, layoutType);
            return;
        }
        if (!sSetWindowLayoutTypeMethodAttempted) {
            Class<PopupWindow> cls = PopupWindow.class;
            try {
                Method declaredMethod = cls.getDeclaredMethod("setWindowLayoutType", new Class[]{Integer.TYPE});
                sSetWindowLayoutTypeMethod = declaredMethod;
                declaredMethod.setAccessible(true);
            } catch (Exception e) {
            }
            sSetWindowLayoutTypeMethodAttempted = true;
        }
        Method method = sSetWindowLayoutTypeMethod;
        if (method != null) {
            try {
                method.invoke(popupWindow, new Object[]{Integer.valueOf(layoutType)});
            } catch (Exception e2) {
            }
        }
    }

    public static void showAsDropDown(PopupWindow popup, View anchor, int xoff, int yoff, int gravity) {
        if (Build.VERSION.SDK_INT >= 19) {
            Api19Impl.showAsDropDown(popup, anchor, xoff, yoff, gravity);
            return;
        }
        int i = xoff;
        if ((GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(anchor)) & 7) == 5) {
            i -= popup.getWidth() - anchor.getWidth();
        }
        popup.showAsDropDown(anchor, i, yoff);
    }
}

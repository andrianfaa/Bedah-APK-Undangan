package androidx.core.widget;

import android.os.Build;
import android.view.View;
import android.widget.ListPopupWindow;

public final class ListPopupWindowCompat {

    static class Api19Impl {
        private Api19Impl() {
        }

        static View.OnTouchListener createDragToOpenListener(ListPopupWindow listPopupWindow, View src) {
            return listPopupWindow.createDragToOpenListener(src);
        }
    }

    private ListPopupWindowCompat() {
    }

    public static View.OnTouchListener createDragToOpenListener(ListPopupWindow listPopupWindow, View src) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Api19Impl.createDragToOpenListener(listPopupWindow, src);
        }
        return null;
    }

    @Deprecated
    public static View.OnTouchListener createDragToOpenListener(Object listPopupWindow, View src) {
        return createDragToOpenListener((ListPopupWindow) listPopupWindow, src);
    }
}

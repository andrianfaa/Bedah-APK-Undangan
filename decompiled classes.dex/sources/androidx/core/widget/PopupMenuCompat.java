package androidx.core.widget;

import android.os.Build;
import android.view.View;
import android.widget.PopupMenu;

public final class PopupMenuCompat {

    static class Api19Impl {
        private Api19Impl() {
        }

        static View.OnTouchListener getDragToOpenListener(PopupMenu popupMenu) {
            return popupMenu.getDragToOpenListener();
        }
    }

    private PopupMenuCompat() {
    }

    public static View.OnTouchListener getDragToOpenListener(Object popupMenu) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Api19Impl.getDragToOpenListener((PopupMenu) popupMenu);
        }
        return null;
    }
}

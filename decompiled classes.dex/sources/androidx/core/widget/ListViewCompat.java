package androidx.core.widget;

import android.os.Build;
import android.view.View;
import android.widget.ListView;

public final class ListViewCompat {

    static class Api19Impl {
        private Api19Impl() {
        }

        static boolean canScrollList(ListView absListView, int direction) {
            return absListView.canScrollList(direction);
        }

        static void scrollListBy(ListView absListView, int y) {
            absListView.scrollListBy(y);
        }
    }

    private ListViewCompat() {
    }

    public static boolean canScrollList(ListView listView, int direction) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Api19Impl.canScrollList(listView, direction);
        }
        int childCount = listView.getChildCount();
        if (childCount == 0) {
            return false;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        if (direction > 0) {
            return firstVisiblePosition + childCount < listView.getCount() || listView.getChildAt(childCount + -1).getBottom() > listView.getHeight() - listView.getListPaddingBottom();
        }
        return firstVisiblePosition > 0 || listView.getChildAt(0).getTop() < listView.getListPaddingTop();
    }

    public static void scrollListBy(ListView listView, int y) {
        View childAt;
        if (Build.VERSION.SDK_INT >= 19) {
            Api19Impl.scrollListBy(listView, y);
            return;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        if (firstVisiblePosition != -1 && (childAt = listView.getChildAt(0)) != null) {
            listView.setSelectionFromTop(firstVisiblePosition, childAt.getTop() - y);
        }
    }
}

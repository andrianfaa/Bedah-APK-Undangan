package androidx.core.widget;

import android.widget.ListView;

public class ListViewAutoScrollHelper extends AutoScrollHelper {
    private final ListView mTarget;

    public ListViewAutoScrollHelper(ListView target) {
        super(target);
        this.mTarget = target;
    }

    public boolean canTargetScrollHorizontally(int direction) {
        return false;
    }

    public boolean canTargetScrollVertically(int direction) {
        ListView listView = this.mTarget;
        int count = listView.getCount();
        if (count == 0) {
            return false;
        }
        int childCount = listView.getChildCount();
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int i = firstVisiblePosition + childCount;
        if (direction > 0) {
            return i < count || listView.getChildAt(childCount + -1).getBottom() > listView.getHeight();
        }
        if (direction < 0) {
            return firstVisiblePosition > 0 || listView.getChildAt(0).getTop() < 0;
        }
        return false;
    }

    public void scrollTargetBy(int deltaX, int deltaY) {
        ListViewCompat.scrollListBy(this.mTarget, deltaY);
    }
}

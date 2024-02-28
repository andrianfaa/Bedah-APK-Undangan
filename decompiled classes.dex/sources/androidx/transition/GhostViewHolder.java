package androidx.transition;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import java.util.ArrayList;

class GhostViewHolder extends FrameLayout {
    private boolean mAttached = true;
    private ViewGroup mParent;

    GhostViewHolder(ViewGroup parent) {
        super(parent.getContext());
        setClipChildren(false);
        this.mParent = parent;
        parent.setTag(R.id.ghost_view_holder, this);
        ViewGroupUtils.getOverlay(this.mParent).add(this);
    }

    static GhostViewHolder getHolder(ViewGroup parent) {
        return (GhostViewHolder) parent.getTag(R.id.ghost_view_holder);
    }

    private int getInsertIndex(ArrayList<View> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        int i = 0;
        int childCount = getChildCount() - 1;
        while (i <= childCount) {
            int i2 = (i + childCount) / 2;
            getParents(((GhostViewPort) getChildAt(i2)).mView, arrayList2);
            if (isOnTop(arrayList, (ArrayList<View>) arrayList2)) {
                i = i2 + 1;
            } else {
                childCount = i2 - 1;
            }
            arrayList2.clear();
        }
        return i;
    }

    private static void getParents(View view, ArrayList<View> arrayList) {
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            getParents((View) parent, arrayList);
        }
        arrayList.add(view);
    }

    private static boolean isOnTop(View view, View comparedWith) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        int childCount = viewGroup.getChildCount();
        if (Build.VERSION.SDK_INT >= 21 && view.getZ() != comparedWith.getZ()) {
            return view.getZ() > comparedWith.getZ();
        }
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(ViewGroupUtils.getChildDrawingOrder(viewGroup, i));
            if (childAt == view) {
                return false;
            }
            if (childAt == comparedWith) {
                return true;
            }
        }
        return true;
    }

    private static boolean isOnTop(ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        if (arrayList.isEmpty() || arrayList2.isEmpty() || arrayList.get(0) != arrayList2.get(0)) {
            return true;
        }
        int min = Math.min(arrayList.size(), arrayList2.size());
        for (int i = 1; i < min; i++) {
            View view = arrayList.get(i);
            View view2 = arrayList2.get(i);
            if (view != view2) {
                return isOnTop(view, view2);
            }
        }
        return arrayList2.size() == min;
    }

    /* access modifiers changed from: package-private */
    public void addGhostView(GhostViewPort ghostView) {
        ArrayList arrayList = new ArrayList();
        getParents(ghostView.mView, arrayList);
        int insertIndex = getInsertIndex(arrayList);
        if (insertIndex < 0 || insertIndex >= getChildCount()) {
            addView(ghostView);
        } else {
            addView(ghostView, insertIndex);
        }
    }

    public void onViewAdded(View child) {
        if (this.mAttached) {
            super.onViewAdded(child);
            return;
        }
        throw new IllegalStateException("This GhostViewHolder is detached!");
    }

    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        if ((getChildCount() == 1 && getChildAt(0) == child) || getChildCount() == 0) {
            this.mParent.setTag(R.id.ghost_view_holder, (Object) null);
            ViewGroupUtils.getOverlay(this.mParent).remove(this);
            this.mAttached = false;
        }
    }

    /* access modifiers changed from: package-private */
    public void popToOverlayTop() {
        if (this.mAttached) {
            ViewGroupUtils.getOverlay(this.mParent).remove(this);
            ViewGroupUtils.getOverlay(this.mParent).add(this);
            return;
        }
        throw new IllegalStateException("This GhostViewHolder is detached!");
    }
}

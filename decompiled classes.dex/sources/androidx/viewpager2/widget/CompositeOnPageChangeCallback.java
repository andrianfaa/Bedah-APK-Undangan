package androidx.viewpager2.widget;

import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

final class CompositeOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
    private final List<ViewPager2.OnPageChangeCallback> mCallbacks;

    CompositeOnPageChangeCallback(int initialCapacity) {
        this.mCallbacks = new ArrayList(initialCapacity);
    }

    private void throwCallbackListModifiedWhileInUse(ConcurrentModificationException parent) {
        throw new IllegalStateException("Adding and removing callbacks during dispatch to callbacks is not supported", parent);
    }

    /* access modifiers changed from: package-private */
    public void addOnPageChangeCallback(ViewPager2.OnPageChangeCallback callback) {
        this.mCallbacks.add(callback);
    }

    public void onPageScrollStateChanged(int state) {
        try {
            for (ViewPager2.OnPageChangeCallback onPageScrollStateChanged : this.mCallbacks) {
                onPageScrollStateChanged.onPageScrollStateChanged(state);
            }
        } catch (ConcurrentModificationException e) {
            throwCallbackListModifiedWhileInUse(e);
        }
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        try {
            for (ViewPager2.OnPageChangeCallback onPageScrolled : this.mCallbacks) {
                onPageScrolled.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        } catch (ConcurrentModificationException e) {
            throwCallbackListModifiedWhileInUse(e);
        }
    }

    public void onPageSelected(int position) {
        try {
            for (ViewPager2.OnPageChangeCallback onPageSelected : this.mCallbacks) {
                onPageSelected.onPageSelected(position);
            }
        } catch (ConcurrentModificationException e) {
            throwCallbackListModifiedWhileInUse(e);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeOnPageChangeCallback(ViewPager2.OnPageChangeCallback callback) {
        this.mCallbacks.remove(callback);
    }
}

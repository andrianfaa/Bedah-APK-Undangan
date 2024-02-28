package androidx.viewpager2.widget;

import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;
import java.util.Locale;
import mt.Log1F380D;

/* compiled from: 00A0 */
final class PageTransformerAdapter extends ViewPager2.OnPageChangeCallback {
    private final LinearLayoutManager mLayoutManager;
    private ViewPager2.PageTransformer mPageTransformer;

    PageTransformerAdapter(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    /* access modifiers changed from: package-private */
    public ViewPager2.PageTransformer getPageTransformer() {
        return this.mPageTransformer;
    }

    public void onPageScrollStateChanged(int state) {
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (this.mPageTransformer != null) {
            float f = -positionOffset;
            int i = 0;
            while (i < this.mLayoutManager.getChildCount()) {
                View childAt = this.mLayoutManager.getChildAt(i);
                if (childAt != null) {
                    this.mPageTransformer.transformPage(childAt, ((float) (this.mLayoutManager.getPosition(childAt) - position)) + f);
                    i++;
                } else {
                    String format = String.format(Locale.US, "LayoutManager returned a null child at pos %d/%d while transforming pages", new Object[]{Integer.valueOf(i), Integer.valueOf(this.mLayoutManager.getChildCount())});
                    Log1F380D.a((Object) format);
                    throw new IllegalStateException(format);
                }
            }
        }
    }

    public void onPageSelected(int position) {
    }

    /* access modifiers changed from: package-private */
    public void setPageTransformer(ViewPager2.PageTransformer transformer) {
        this.mPageTransformer = transformer;
    }
}

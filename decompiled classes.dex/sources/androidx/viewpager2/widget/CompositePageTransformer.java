package androidx.viewpager2.widget;

import android.view.View;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public final class CompositePageTransformer implements ViewPager2.PageTransformer {
    private final List<ViewPager2.PageTransformer> mTransformers = new ArrayList();

    public void addTransformer(ViewPager2.PageTransformer transformer) {
        this.mTransformers.add(transformer);
    }

    public void removeTransformer(ViewPager2.PageTransformer transformer) {
        this.mTransformers.remove(transformer);
    }

    public void transformPage(View page, float position) {
        for (ViewPager2.PageTransformer transformPage : this.mTransformers) {
            transformPage.transformPage(page, position);
        }
    }
}

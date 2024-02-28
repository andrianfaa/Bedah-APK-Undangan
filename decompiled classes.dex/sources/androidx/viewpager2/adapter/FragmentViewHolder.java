package androidx.viewpager2.adapter;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public final class FragmentViewHolder extends RecyclerView.ViewHolder {
    private FragmentViewHolder(FrameLayout container) {
        super(container);
    }

    static FragmentViewHolder create(ViewGroup parent) {
        FrameLayout frameLayout = new FrameLayout(parent.getContext());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        frameLayout.setId(ViewCompat.generateViewId());
        frameLayout.setSaveEnabled(false);
        return new FragmentViewHolder(frameLayout);
    }

    /* access modifiers changed from: package-private */
    public FrameLayout getContainer() {
        return (FrameLayout) this.itemView;
    }
}

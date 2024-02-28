package androidx.recyclerview.widget;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SimpleItemAnimator extends RecyclerView.ItemAnimator {
    private static final boolean DEBUG = false;
    private static final String TAG = "SimpleItemAnimator";
    boolean mSupportsChangeAnimations = true;

    public abstract boolean animateAdd(RecyclerView.ViewHolder viewHolder);

    public boolean animateAppearance(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo preLayoutInfo, RecyclerView.ItemAnimator.ItemHolderInfo postLayoutInfo) {
        if (preLayoutInfo == null || (preLayoutInfo.left == postLayoutInfo.left && preLayoutInfo.top == postLayoutInfo.top)) {
            return animateAdd(viewHolder);
        }
        return animateMove(viewHolder, preLayoutInfo.left, preLayoutInfo.top, postLayoutInfo.left, postLayoutInfo.top);
    }

    public abstract boolean animateChange(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2, int i, int i2, int i3, int i4);

    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, RecyclerView.ItemAnimator.ItemHolderInfo preInfo, RecyclerView.ItemAnimator.ItemHolderInfo postInfo) {
        int i;
        int i2;
        int i3 = preInfo.left;
        int i4 = preInfo.top;
        if (newHolder.shouldIgnore()) {
            i2 = preInfo.left;
            i = preInfo.top;
        } else {
            i2 = postInfo.left;
            i = postInfo.top;
        }
        return animateChange(oldHolder, newHolder, i3, i4, i2, i);
    }

    public boolean animateDisappearance(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo preLayoutInfo, RecyclerView.ItemAnimator.ItemHolderInfo postLayoutInfo) {
        int i = preLayoutInfo.left;
        int i2 = preLayoutInfo.top;
        View view = viewHolder.itemView;
        int left = postLayoutInfo == null ? view.getLeft() : postLayoutInfo.left;
        int top = postLayoutInfo == null ? view.getTop() : postLayoutInfo.top;
        if (viewHolder.isRemoved() || (i == left && i2 == top)) {
            return animateRemove(viewHolder);
        }
        view.layout(left, top, view.getWidth() + left, view.getHeight() + top);
        return animateMove(viewHolder, i, i2, left, top);
    }

    public abstract boolean animateMove(RecyclerView.ViewHolder viewHolder, int i, int i2, int i3, int i4);

    public boolean animatePersistence(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo preInfo, RecyclerView.ItemAnimator.ItemHolderInfo postInfo) {
        if (preInfo.left == postInfo.left && preInfo.top == postInfo.top) {
            dispatchMoveFinished(viewHolder);
            return false;
        }
        return animateMove(viewHolder, preInfo.left, preInfo.top, postInfo.left, postInfo.top);
    }

    public abstract boolean animateRemove(RecyclerView.ViewHolder viewHolder);

    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
        return !this.mSupportsChangeAnimations || viewHolder.isInvalid();
    }

    public final void dispatchAddFinished(RecyclerView.ViewHolder item) {
        onAddFinished(item);
        dispatchAnimationFinished(item);
    }

    public final void dispatchAddStarting(RecyclerView.ViewHolder item) {
        onAddStarting(item);
    }

    public final void dispatchChangeFinished(RecyclerView.ViewHolder item, boolean oldItem) {
        onChangeFinished(item, oldItem);
        dispatchAnimationFinished(item);
    }

    public final void dispatchChangeStarting(RecyclerView.ViewHolder item, boolean oldItem) {
        onChangeStarting(item, oldItem);
    }

    public final void dispatchMoveFinished(RecyclerView.ViewHolder item) {
        onMoveFinished(item);
        dispatchAnimationFinished(item);
    }

    public final void dispatchMoveStarting(RecyclerView.ViewHolder item) {
        onMoveStarting(item);
    }

    public final void dispatchRemoveFinished(RecyclerView.ViewHolder item) {
        onRemoveFinished(item);
        dispatchAnimationFinished(item);
    }

    public final void dispatchRemoveStarting(RecyclerView.ViewHolder item) {
        onRemoveStarting(item);
    }

    public boolean getSupportsChangeAnimations() {
        return this.mSupportsChangeAnimations;
    }

    public void onAddFinished(RecyclerView.ViewHolder item) {
    }

    public void onAddStarting(RecyclerView.ViewHolder item) {
    }

    public void onChangeFinished(RecyclerView.ViewHolder item, boolean oldItem) {
    }

    public void onChangeStarting(RecyclerView.ViewHolder item, boolean oldItem) {
    }

    public void onMoveFinished(RecyclerView.ViewHolder item) {
    }

    public void onMoveStarting(RecyclerView.ViewHolder item) {
    }

    public void onRemoveFinished(RecyclerView.ViewHolder item) {
    }

    public void onRemoveStarting(RecyclerView.ViewHolder item) {
    }

    public void setSupportsChangeAnimations(boolean supportsChangeAnimations) {
        this.mSupportsChangeAnimations = supportsChangeAnimations;
    }
}

package androidx.recyclerview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = {16843284};
    public static final int HORIZONTAL = 0;
    private static final String TAG = "DividerItem";
    public static final int VERTICAL = 1;
    private final Rect mBounds = new Rect();
    private Drawable mDivider;
    private int mOrientation;

    public DividerItemDecoration(Context context, int orientation) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(ATTRS);
        Drawable drawable = obtainStyledAttributes.getDrawable(0);
        this.mDivider = drawable;
        if (drawable == null) {
            Log.w(TAG, "@android:attr/listDivider was not set in the theme used for this DividerItemDecoration. Please set that attribute all call setDrawable()");
        }
        obtainStyledAttributes.recycle();
        setOrientation(orientation);
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        int i;
        int i2;
        canvas.save();
        if (parent.getClipToPadding()) {
            i2 = parent.getPaddingTop();
            i = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), i2, parent.getWidth() - parent.getPaddingRight(), i);
        } else {
            i2 = 0;
            i = parent.getHeight();
        }
        int childCount = parent.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = parent.getChildAt(i3);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(childAt, this.mBounds);
            int round = this.mBounds.right + Math.round(childAt.getTranslationX());
            this.mDivider.setBounds(round - this.mDivider.getIntrinsicWidth(), i2, round, i);
            this.mDivider.draw(canvas);
        }
        canvas.restore();
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        int i;
        int i2;
        canvas.save();
        if (parent.getClipToPadding()) {
            i2 = parent.getPaddingLeft();
            i = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(i2, parent.getPaddingTop(), i, parent.getHeight() - parent.getPaddingBottom());
        } else {
            i2 = 0;
            i = parent.getWidth();
        }
        int childCount = parent.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = parent.getChildAt(i3);
            parent.getDecoratedBoundsWithMargins(childAt, this.mBounds);
            int round = this.mBounds.bottom + Math.round(childAt.getTranslationY());
            this.mDivider.setBounds(i2, round - this.mDivider.getIntrinsicHeight(), i, round);
            this.mDivider.draw(canvas);
        }
        canvas.restore();
    }

    public Drawable getDrawable() {
        return this.mDivider;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        Drawable drawable = this.mDivider;
        if (drawable == null) {
            outRect.set(0, 0, 0, 0);
        } else if (this.mOrientation == 1) {
            outRect.set(0, 0, 0, drawable.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, drawable.getIntrinsicWidth(), 0);
        }
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() != null && this.mDivider != null) {
            if (this.mOrientation == 1) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }
    }

    public void setDrawable(Drawable drawable) {
        if (drawable != null) {
            this.mDivider = drawable;
            return;
        }
        throw new IllegalArgumentException("Drawable cannot be null.");
    }

    public void setOrientation(int orientation) {
        if (orientation == 0 || orientation == 1) {
            this.mOrientation = orientation;
            return;
        }
        throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
    }
}

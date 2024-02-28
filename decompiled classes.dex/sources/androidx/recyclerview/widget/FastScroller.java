package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.MotionEvent;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

class FastScroller extends RecyclerView.ItemDecoration implements RecyclerView.OnItemTouchListener {
    private static final int ANIMATION_STATE_FADING_IN = 1;
    private static final int ANIMATION_STATE_FADING_OUT = 3;
    private static final int ANIMATION_STATE_IN = 2;
    private static final int ANIMATION_STATE_OUT = 0;
    private static final int DRAG_NONE = 0;
    private static final int DRAG_X = 1;
    private static final int DRAG_Y = 2;
    private static final int[] EMPTY_STATE_SET = new int[0];
    private static final int HIDE_DELAY_AFTER_DRAGGING_MS = 1200;
    private static final int HIDE_DELAY_AFTER_VISIBLE_MS = 1500;
    private static final int HIDE_DURATION_MS = 500;
    private static final int[] PRESSED_STATE_SET = {16842919};
    private static final int SCROLLBAR_FULL_OPAQUE = 255;
    private static final int SHOW_DURATION_MS = 500;
    private static final int STATE_DRAGGING = 2;
    private static final int STATE_HIDDEN = 0;
    private static final int STATE_VISIBLE = 1;
    int mAnimationState;
    private int mDragState = 0;
    private final Runnable mHideRunnable;
    float mHorizontalDragX;
    private final int[] mHorizontalRange = new int[2];
    int mHorizontalThumbCenterX;
    private final StateListDrawable mHorizontalThumbDrawable;
    private final int mHorizontalThumbHeight;
    int mHorizontalThumbWidth;
    private final Drawable mHorizontalTrackDrawable;
    private final int mHorizontalTrackHeight;
    private final int mMargin;
    private boolean mNeedHorizontalScrollbar = false;
    private boolean mNeedVerticalScrollbar = false;
    private final RecyclerView.OnScrollListener mOnScrollListener;
    private RecyclerView mRecyclerView;
    private int mRecyclerViewHeight = 0;
    private int mRecyclerViewWidth = 0;
    private final int mScrollbarMinimumRange;
    final ValueAnimator mShowHideAnimator;
    private int mState = 0;
    float mVerticalDragY;
    private final int[] mVerticalRange = new int[2];
    int mVerticalThumbCenterY;
    final StateListDrawable mVerticalThumbDrawable;
    int mVerticalThumbHeight;
    private final int mVerticalThumbWidth;
    final Drawable mVerticalTrackDrawable;
    private final int mVerticalTrackWidth;

    private class AnimatorListener extends AnimatorListenerAdapter {
        private boolean mCanceled = false;

        AnimatorListener() {
        }

        public void onAnimationCancel(Animator animation) {
            this.mCanceled = true;
        }

        public void onAnimationEnd(Animator animation) {
            if (this.mCanceled) {
                this.mCanceled = false;
            } else if (((Float) FastScroller.this.mShowHideAnimator.getAnimatedValue()).floatValue() == 0.0f) {
                FastScroller.this.mAnimationState = 0;
                FastScroller.this.setState(0);
            } else {
                FastScroller.this.mAnimationState = 2;
                FastScroller.this.requestRedraw();
            }
        }
    }

    private class AnimatorUpdater implements ValueAnimator.AnimatorUpdateListener {
        AnimatorUpdater() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int floatValue = (int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * 255.0f);
            FastScroller.this.mVerticalThumbDrawable.setAlpha(floatValue);
            FastScroller.this.mVerticalTrackDrawable.setAlpha(floatValue);
            FastScroller.this.requestRedraw();
        }
    }

    FastScroller(RecyclerView recyclerView, StateListDrawable verticalThumbDrawable, Drawable verticalTrackDrawable, StateListDrawable horizontalThumbDrawable, Drawable horizontalTrackDrawable, int defaultWidth, int scrollbarMinimumRange, int margin) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mShowHideAnimator = ofFloat;
        this.mAnimationState = 0;
        this.mHideRunnable = new Runnable() {
            public void run() {
                FastScroller.this.hide(500);
            }
        };
        this.mOnScrollListener = new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                FastScroller.this.updateScrollPosition(recyclerView.computeHorizontalScrollOffset(), recyclerView.computeVerticalScrollOffset());
            }
        };
        this.mVerticalThumbDrawable = verticalThumbDrawable;
        this.mVerticalTrackDrawable = verticalTrackDrawable;
        this.mHorizontalThumbDrawable = horizontalThumbDrawable;
        this.mHorizontalTrackDrawable = horizontalTrackDrawable;
        this.mVerticalThumbWidth = Math.max(defaultWidth, verticalThumbDrawable.getIntrinsicWidth());
        this.mVerticalTrackWidth = Math.max(defaultWidth, verticalTrackDrawable.getIntrinsicWidth());
        this.mHorizontalThumbHeight = Math.max(defaultWidth, horizontalThumbDrawable.getIntrinsicWidth());
        this.mHorizontalTrackHeight = Math.max(defaultWidth, horizontalTrackDrawable.getIntrinsicWidth());
        this.mScrollbarMinimumRange = scrollbarMinimumRange;
        this.mMargin = margin;
        verticalThumbDrawable.setAlpha(255);
        verticalTrackDrawable.setAlpha(255);
        ofFloat.addListener(new AnimatorListener());
        ofFloat.addUpdateListener(new AnimatorUpdater());
        attachToRecyclerView(recyclerView);
    }

    private void cancelHide() {
        this.mRecyclerView.removeCallbacks(this.mHideRunnable);
    }

    private void destroyCallbacks() {
        this.mRecyclerView.removeItemDecoration(this);
        this.mRecyclerView.removeOnItemTouchListener(this);
        this.mRecyclerView.removeOnScrollListener(this.mOnScrollListener);
        cancelHide();
    }

    private void drawHorizontalScrollbar(Canvas canvas) {
        int i = this.mRecyclerViewHeight;
        int i2 = this.mHorizontalThumbHeight;
        int i3 = i - i2;
        int i4 = this.mHorizontalThumbCenterX;
        int i5 = this.mHorizontalThumbWidth;
        int i6 = i4 - (i5 / 2);
        this.mHorizontalThumbDrawable.setBounds(0, 0, i5, i2);
        this.mHorizontalTrackDrawable.setBounds(0, 0, this.mRecyclerViewWidth, this.mHorizontalTrackHeight);
        canvas.translate(0.0f, (float) i3);
        this.mHorizontalTrackDrawable.draw(canvas);
        canvas.translate((float) i6, 0.0f);
        this.mHorizontalThumbDrawable.draw(canvas);
        canvas.translate((float) (-i6), (float) (-i3));
    }

    private void drawVerticalScrollbar(Canvas canvas) {
        int i = this.mRecyclerViewWidth;
        int i2 = this.mVerticalThumbWidth;
        int i3 = i - i2;
        int i4 = this.mVerticalThumbCenterY;
        int i5 = this.mVerticalThumbHeight;
        int i6 = i4 - (i5 / 2);
        this.mVerticalThumbDrawable.setBounds(0, 0, i2, i5);
        this.mVerticalTrackDrawable.setBounds(0, 0, this.mVerticalTrackWidth, this.mRecyclerViewHeight);
        if (isLayoutRTL()) {
            this.mVerticalTrackDrawable.draw(canvas);
            canvas.translate((float) this.mVerticalThumbWidth, (float) i6);
            canvas.scale(-1.0f, 1.0f);
            this.mVerticalThumbDrawable.draw(canvas);
            canvas.scale(1.0f, 1.0f);
            canvas.translate((float) (-this.mVerticalThumbWidth), (float) (-i6));
            return;
        }
        canvas.translate((float) i3, 0.0f);
        this.mVerticalTrackDrawable.draw(canvas);
        canvas.translate(0.0f, (float) i6);
        this.mVerticalThumbDrawable.draw(canvas);
        canvas.translate((float) (-i3), (float) (-i6));
    }

    private int[] getHorizontalRange() {
        int[] iArr = this.mHorizontalRange;
        int i = this.mMargin;
        iArr[0] = i;
        iArr[1] = this.mRecyclerViewWidth - i;
        return iArr;
    }

    private int[] getVerticalRange() {
        int[] iArr = this.mVerticalRange;
        int i = this.mMargin;
        iArr[0] = i;
        iArr[1] = this.mRecyclerViewHeight - i;
        return iArr;
    }

    private void horizontalScrollTo(float x) {
        int[] horizontalRange = getHorizontalRange();
        float x2 = Math.max((float) horizontalRange[0], Math.min((float) horizontalRange[1], x));
        if (Math.abs(((float) this.mHorizontalThumbCenterX) - x2) >= 2.0f) {
            int scrollTo = scrollTo(this.mHorizontalDragX, x2, horizontalRange, this.mRecyclerView.computeHorizontalScrollRange(), this.mRecyclerView.computeHorizontalScrollOffset(), this.mRecyclerViewWidth);
            if (scrollTo != 0) {
                this.mRecyclerView.scrollBy(scrollTo, 0);
            }
            this.mHorizontalDragX = x2;
        }
    }

    private boolean isLayoutRTL() {
        return ViewCompat.getLayoutDirection(this.mRecyclerView) == 1;
    }

    private void resetHideDelay(int delay) {
        cancelHide();
        this.mRecyclerView.postDelayed(this.mHideRunnable, (long) delay);
    }

    private int scrollTo(float oldDragPos, float newDragPos, int[] scrollbarRange, int scrollRange, int scrollOffset, int viewLength) {
        int i = scrollbarRange[1] - scrollbarRange[0];
        if (i == 0) {
            return 0;
        }
        int i2 = scrollRange - viewLength;
        int i3 = (int) (((float) i2) * ((newDragPos - oldDragPos) / ((float) i)));
        int i4 = scrollOffset + i3;
        if (i4 >= i2 || i4 < 0) {
            return 0;
        }
        return i3;
    }

    private void setupCallbacks() {
        this.mRecyclerView.addItemDecoration(this);
        this.mRecyclerView.addOnItemTouchListener(this);
        this.mRecyclerView.addOnScrollListener(this.mOnScrollListener);
    }

    private void verticalScrollTo(float y) {
        int[] verticalRange = getVerticalRange();
        float y2 = Math.max((float) verticalRange[0], Math.min((float) verticalRange[1], y));
        if (Math.abs(((float) this.mVerticalThumbCenterY) - y2) >= 2.0f) {
            int scrollTo = scrollTo(this.mVerticalDragY, y2, verticalRange, this.mRecyclerView.computeVerticalScrollRange(), this.mRecyclerView.computeVerticalScrollOffset(), this.mRecyclerViewHeight);
            if (scrollTo != 0) {
                this.mRecyclerView.scrollBy(0, scrollTo);
            }
            this.mVerticalDragY = y2;
        }
    }

    public void attachToRecyclerView(RecyclerView recyclerView) {
        RecyclerView recyclerView2 = this.mRecyclerView;
        if (recyclerView2 != recyclerView) {
            if (recyclerView2 != null) {
                destroyCallbacks();
            }
            this.mRecyclerView = recyclerView;
            if (recyclerView != null) {
                setupCallbacks();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Drawable getHorizontalThumbDrawable() {
        return this.mHorizontalThumbDrawable;
    }

    /* access modifiers changed from: package-private */
    public Drawable getHorizontalTrackDrawable() {
        return this.mHorizontalTrackDrawable;
    }

    /* access modifiers changed from: package-private */
    public Drawable getVerticalThumbDrawable() {
        return this.mVerticalThumbDrawable;
    }

    /* access modifiers changed from: package-private */
    public Drawable getVerticalTrackDrawable() {
        return this.mVerticalTrackDrawable;
    }

    /* access modifiers changed from: package-private */
    public void hide(int duration) {
        switch (this.mAnimationState) {
            case 1:
                this.mShowHideAnimator.cancel();
                break;
            case 2:
                break;
            default:
                return;
        }
        this.mAnimationState = 3;
        ValueAnimator valueAnimator = this.mShowHideAnimator;
        valueAnimator.setFloatValues(new float[]{((Float) valueAnimator.getAnimatedValue()).floatValue(), 0.0f});
        this.mShowHideAnimator.setDuration((long) duration);
        this.mShowHideAnimator.start();
    }

    public boolean isDragging() {
        return this.mState == 2;
    }

    /* access modifiers changed from: package-private */
    public boolean isPointInsideHorizontalThumb(float x, float y) {
        if (y >= ((float) (this.mRecyclerViewHeight - this.mHorizontalThumbHeight))) {
            int i = this.mHorizontalThumbCenterX;
            int i2 = this.mHorizontalThumbWidth;
            return x >= ((float) (i - (i2 / 2))) && x <= ((float) (i + (i2 / 2)));
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isPointInsideVerticalThumb(float x, float y) {
        if (!isLayoutRTL() ? x >= ((float) (this.mRecyclerViewWidth - this.mVerticalThumbWidth)) : x <= ((float) (this.mVerticalThumbWidth / 2))) {
            int i = this.mVerticalThumbCenterY;
            int i2 = this.mVerticalThumbHeight;
            return y >= ((float) (i - (i2 / 2))) && y <= ((float) (i + (i2 / 2)));
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isVisible() {
        return this.mState == 1;
    }

    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (this.mRecyclerViewWidth != this.mRecyclerView.getWidth() || this.mRecyclerViewHeight != this.mRecyclerView.getHeight()) {
            this.mRecyclerViewWidth = this.mRecyclerView.getWidth();
            this.mRecyclerViewHeight = this.mRecyclerView.getHeight();
            setState(0);
        } else if (this.mAnimationState != 0) {
            if (this.mNeedVerticalScrollbar) {
                drawVerticalScrollbar(canvas);
            }
            if (this.mNeedHorizontalScrollbar) {
                drawHorizontalScrollbar(canvas);
            }
        }
    }

    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent ev) {
        int i = this.mState;
        if (i != 1) {
            return i == 2;
        }
        boolean isPointInsideVerticalThumb = isPointInsideVerticalThumb(ev.getX(), ev.getY());
        boolean isPointInsideHorizontalThumb = isPointInsideHorizontalThumb(ev.getX(), ev.getY());
        if (ev.getAction() != 0 || (!isPointInsideVerticalThumb && !isPointInsideHorizontalThumb)) {
            return false;
        }
        if (isPointInsideHorizontalThumb) {
            this.mDragState = 1;
            this.mHorizontalDragX = (float) ((int) ev.getX());
        } else if (isPointInsideVerticalThumb) {
            this.mDragState = 2;
            this.mVerticalDragY = (float) ((int) ev.getY());
        }
        setState(2);
        return true;
    }

    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    public void onTouchEvent(RecyclerView recyclerView, MotionEvent me) {
        if (this.mState != 0) {
            if (me.getAction() == 0) {
                boolean isPointInsideVerticalThumb = isPointInsideVerticalThumb(me.getX(), me.getY());
                boolean isPointInsideHorizontalThumb = isPointInsideHorizontalThumb(me.getX(), me.getY());
                if (isPointInsideVerticalThumb || isPointInsideHorizontalThumb) {
                    if (isPointInsideHorizontalThumb) {
                        this.mDragState = 1;
                        this.mHorizontalDragX = (float) ((int) me.getX());
                    } else if (isPointInsideVerticalThumb) {
                        this.mDragState = 2;
                        this.mVerticalDragY = (float) ((int) me.getY());
                    }
                    setState(2);
                }
            } else if (me.getAction() == 1 && this.mState == 2) {
                this.mVerticalDragY = 0.0f;
                this.mHorizontalDragX = 0.0f;
                setState(1);
                this.mDragState = 0;
            } else if (me.getAction() == 2 && this.mState == 2) {
                show();
                if (this.mDragState == 1) {
                    horizontalScrollTo(me.getX());
                }
                if (this.mDragState == 2) {
                    verticalScrollTo(me.getY());
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void requestRedraw() {
        this.mRecyclerView.invalidate();
    }

    /* access modifiers changed from: package-private */
    public void setState(int state) {
        if (state == 2 && this.mState != 2) {
            this.mVerticalThumbDrawable.setState(PRESSED_STATE_SET);
            cancelHide();
        }
        if (state == 0) {
            requestRedraw();
        } else {
            show();
        }
        if (this.mState == 2 && state != 2) {
            this.mVerticalThumbDrawable.setState(EMPTY_STATE_SET);
            resetHideDelay(HIDE_DELAY_AFTER_DRAGGING_MS);
        } else if (state == 1) {
            resetHideDelay(HIDE_DELAY_AFTER_VISIBLE_MS);
        }
        this.mState = state;
    }

    public void show() {
        switch (this.mAnimationState) {
            case 0:
                break;
            case 3:
                this.mShowHideAnimator.cancel();
                break;
            default:
                return;
        }
        this.mAnimationState = 1;
        ValueAnimator valueAnimator = this.mShowHideAnimator;
        valueAnimator.setFloatValues(new float[]{((Float) valueAnimator.getAnimatedValue()).floatValue(), 1.0f});
        this.mShowHideAnimator.setDuration(500);
        this.mShowHideAnimator.setStartDelay(0);
        this.mShowHideAnimator.start();
    }

    /* access modifiers changed from: package-private */
    public void updateScrollPosition(int offsetX, int offsetY) {
        int computeVerticalScrollRange = this.mRecyclerView.computeVerticalScrollRange();
        int i = this.mRecyclerViewHeight;
        this.mNeedVerticalScrollbar = computeVerticalScrollRange - i > 0 && this.mRecyclerViewHeight >= this.mScrollbarMinimumRange;
        int computeHorizontalScrollRange = this.mRecyclerView.computeHorizontalScrollRange();
        int i2 = this.mRecyclerViewWidth;
        boolean z = computeHorizontalScrollRange - i2 > 0 && this.mRecyclerViewWidth >= this.mScrollbarMinimumRange;
        this.mNeedHorizontalScrollbar = z;
        boolean z2 = this.mNeedVerticalScrollbar;
        if (z2 || z) {
            if (z2) {
                this.mVerticalThumbCenterY = (int) ((((float) i) * (((float) offsetY) + (((float) i) / 2.0f))) / ((float) computeVerticalScrollRange));
                this.mVerticalThumbHeight = Math.min(i, (i * i) / computeVerticalScrollRange);
            }
            if (this.mNeedHorizontalScrollbar) {
                this.mHorizontalThumbCenterX = (int) ((((float) i2) * (((float) offsetX) + (((float) i2) / 2.0f))) / ((float) computeHorizontalScrollRange));
                this.mHorizontalThumbWidth = Math.min(i2, (i2 * i2) / computeHorizontalScrollRange);
            }
            int i3 = this.mState;
            if (i3 == 0 || i3 == 1) {
                setState(1);
            }
        } else if (this.mState != 0) {
            setState(0);
        }
    }
}

package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.R;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ItemTouchHelper extends RecyclerView.ItemDecoration implements RecyclerView.OnChildAttachStateChangeListener {
    static final int ACTION_MODE_DRAG_MASK = 16711680;
    private static final int ACTION_MODE_IDLE_MASK = 255;
    static final int ACTION_MODE_SWIPE_MASK = 65280;
    public static final int ACTION_STATE_DRAG = 2;
    public static final int ACTION_STATE_IDLE = 0;
    public static final int ACTION_STATE_SWIPE = 1;
    private static final int ACTIVE_POINTER_ID_NONE = -1;
    public static final int ANIMATION_TYPE_DRAG = 8;
    public static final int ANIMATION_TYPE_SWIPE_CANCEL = 4;
    public static final int ANIMATION_TYPE_SWIPE_SUCCESS = 2;
    private static final boolean DEBUG = false;
    static final int DIRECTION_FLAG_COUNT = 8;
    public static final int DOWN = 2;
    public static final int END = 32;
    public static final int LEFT = 4;
    private static final int PIXELS_PER_SECOND = 1000;
    public static final int RIGHT = 8;
    public static final int START = 16;
    private static final String TAG = "ItemTouchHelper";
    public static final int UP = 1;
    private int mActionState = 0;
    int mActivePointerId = -1;
    Callback mCallback;
    private RecyclerView.ChildDrawingOrderCallback mChildDrawingOrderCallback = null;
    private List<Integer> mDistances;
    private long mDragScrollStartTimeInMs;
    float mDx;
    float mDy;
    GestureDetectorCompat mGestureDetector;
    float mInitialTouchX;
    float mInitialTouchY;
    private ItemTouchHelperGestureListener mItemTouchHelperGestureListener;
    private float mMaxSwipeVelocity;
    private final RecyclerView.OnItemTouchListener mOnItemTouchListener = new RecyclerView.OnItemTouchListener() {
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
            int findPointerIndex;
            RecoverAnimation findAnimation;
            ItemTouchHelper.this.mGestureDetector.onTouchEvent(event);
            int actionMasked = event.getActionMasked();
            if (actionMasked == 0) {
                ItemTouchHelper.this.mActivePointerId = event.getPointerId(0);
                ItemTouchHelper.this.mInitialTouchX = event.getX();
                ItemTouchHelper.this.mInitialTouchY = event.getY();
                ItemTouchHelper.this.obtainVelocityTracker();
                if (ItemTouchHelper.this.mSelected == null && (findAnimation = ItemTouchHelper.this.findAnimation(event)) != null) {
                    ItemTouchHelper.this.mInitialTouchX -= findAnimation.mX;
                    ItemTouchHelper.this.mInitialTouchY -= findAnimation.mY;
                    ItemTouchHelper.this.endRecoverAnimation(findAnimation.mViewHolder, true);
                    if (ItemTouchHelper.this.mPendingCleanup.remove(findAnimation.mViewHolder.itemView)) {
                        ItemTouchHelper.this.mCallback.clearView(ItemTouchHelper.this.mRecyclerView, findAnimation.mViewHolder);
                    }
                    ItemTouchHelper.this.select(findAnimation.mViewHolder, findAnimation.mActionState);
                    ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
                    itemTouchHelper.updateDxDy(event, itemTouchHelper.mSelectedFlags, 0);
                }
            } else if (actionMasked == 3 || actionMasked == 1) {
                ItemTouchHelper.this.mActivePointerId = -1;
                ItemTouchHelper.this.select((RecyclerView.ViewHolder) null, 0);
            } else if (ItemTouchHelper.this.mActivePointerId != -1 && (findPointerIndex = event.findPointerIndex(ItemTouchHelper.this.mActivePointerId)) >= 0) {
                ItemTouchHelper.this.checkSelectForSwipe(actionMasked, event, findPointerIndex);
            }
            if (ItemTouchHelper.this.mVelocityTracker != null) {
                ItemTouchHelper.this.mVelocityTracker.addMovement(event);
            }
            return ItemTouchHelper.this.mSelected != null;
        }

        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            if (disallowIntercept) {
                ItemTouchHelper.this.select((RecyclerView.ViewHolder) null, 0);
            }
        }

        public void onTouchEvent(RecyclerView recyclerView, MotionEvent event) {
            ItemTouchHelper.this.mGestureDetector.onTouchEvent(event);
            if (ItemTouchHelper.this.mVelocityTracker != null) {
                ItemTouchHelper.this.mVelocityTracker.addMovement(event);
            }
            if (ItemTouchHelper.this.mActivePointerId != -1) {
                int actionMasked = event.getActionMasked();
                int findPointerIndex = event.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
                if (findPointerIndex >= 0) {
                    ItemTouchHelper.this.checkSelectForSwipe(actionMasked, event, findPointerIndex);
                }
                RecyclerView.ViewHolder viewHolder = ItemTouchHelper.this.mSelected;
                if (viewHolder != null) {
                    int i = 0;
                    switch (actionMasked) {
                        case 1:
                            break;
                        case 2:
                            if (findPointerIndex >= 0) {
                                ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
                                itemTouchHelper.updateDxDy(event, itemTouchHelper.mSelectedFlags, findPointerIndex);
                                ItemTouchHelper.this.moveIfNecessary(viewHolder);
                                ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
                                ItemTouchHelper.this.mScrollRunnable.run();
                                ItemTouchHelper.this.mRecyclerView.invalidate();
                                return;
                            }
                            return;
                        case 3:
                            if (ItemTouchHelper.this.mVelocityTracker != null) {
                                ItemTouchHelper.this.mVelocityTracker.clear();
                                break;
                            }
                            break;
                        case 6:
                            int actionIndex = event.getActionIndex();
                            if (event.getPointerId(actionIndex) == ItemTouchHelper.this.mActivePointerId) {
                                if (actionIndex == 0) {
                                    i = 1;
                                }
                                ItemTouchHelper.this.mActivePointerId = event.getPointerId(i);
                                ItemTouchHelper itemTouchHelper2 = ItemTouchHelper.this;
                                itemTouchHelper2.updateDxDy(event, itemTouchHelper2.mSelectedFlags, actionIndex);
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                    ItemTouchHelper.this.select((RecyclerView.ViewHolder) null, 0);
                    ItemTouchHelper.this.mActivePointerId = -1;
                }
            }
        }
    };
    View mOverdrawChild = null;
    int mOverdrawChildPosition = -1;
    final List<View> mPendingCleanup = new ArrayList();
    List<RecoverAnimation> mRecoverAnimations = new ArrayList();
    RecyclerView mRecyclerView;
    final Runnable mScrollRunnable = new Runnable() {
        public void run() {
            if (ItemTouchHelper.this.mSelected != null && ItemTouchHelper.this.scrollIfNecessary()) {
                if (ItemTouchHelper.this.mSelected != null) {
                    ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
                    itemTouchHelper.moveIfNecessary(itemTouchHelper.mSelected);
                }
                ItemTouchHelper.this.mRecyclerView.removeCallbacks(ItemTouchHelper.this.mScrollRunnable);
                ViewCompat.postOnAnimation(ItemTouchHelper.this.mRecyclerView, this);
            }
        }
    };
    RecyclerView.ViewHolder mSelected = null;
    int mSelectedFlags;
    private float mSelectedStartX;
    private float mSelectedStartY;
    private int mSlop;
    private List<RecyclerView.ViewHolder> mSwapTargets;
    private float mSwipeEscapeVelocity;
    private final float[] mTmpPosition = new float[2];
    private Rect mTmpRect;
    VelocityTracker mVelocityTracker;

    public static abstract class Callback {
        private static final int ABS_HORIZONTAL_DIR_FLAGS = 789516;
        public static final int DEFAULT_DRAG_ANIMATION_DURATION = 200;
        public static final int DEFAULT_SWIPE_ANIMATION_DURATION = 250;
        private static final long DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS = 2000;
        static final int RELATIVE_DIR_FLAGS = 3158064;
        private static final Interpolator sDragScrollInterpolator = new Interpolator() {
            public float getInterpolation(float t) {
                return t * t * t * t * t;
            }
        };
        private static final Interpolator sDragViewScrollCapInterpolator = new Interpolator() {
            public float getInterpolation(float t) {
                float t2 = t - 1.0f;
                return (t2 * t2 * t2 * t2 * t2) + 1.0f;
            }
        };
        private int mCachedMaxScrollSpeed = -1;

        public static int convertToRelativeDirection(int flags, int layoutDirection) {
            int i = flags & ABS_HORIZONTAL_DIR_FLAGS;
            if (i == 0) {
                return flags;
            }
            int flags2 = flags & (~i);
            return layoutDirection == 0 ? flags2 | (i << 2) : flags2 | ((i << 1) & -789517) | ((ABS_HORIZONTAL_DIR_FLAGS & (i << 1)) << 2);
        }

        public static ItemTouchUIUtil getDefaultUIUtil() {
            return ItemTouchUIUtilImpl.INSTANCE;
        }

        private int getMaxDragScroll(RecyclerView recyclerView) {
            if (this.mCachedMaxScrollSpeed == -1) {
                this.mCachedMaxScrollSpeed = recyclerView.getResources().getDimensionPixelSize(R.dimen.item_touch_helper_max_drag_scroll_per_frame);
            }
            return this.mCachedMaxScrollSpeed;
        }

        public static int makeFlag(int actionState, int directions) {
            return directions << (actionState * 8);
        }

        public static int makeMovementFlags(int dragFlags, int swipeFlags) {
            return makeFlag(0, swipeFlags | dragFlags) | makeFlag(1, swipeFlags) | makeFlag(2, dragFlags);
        }

        public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
            return true;
        }

        public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder selected, List<RecyclerView.ViewHolder> list, int curX, int curY) {
            int bottom;
            int abs;
            int top;
            int abs2;
            int left;
            int abs3;
            int right;
            int abs4;
            RecyclerView.ViewHolder viewHolder = selected;
            int width = curX + viewHolder.itemView.getWidth();
            int height = curY + viewHolder.itemView.getHeight();
            RecyclerView.ViewHolder viewHolder2 = null;
            int i = -1;
            int left2 = curX - viewHolder.itemView.getLeft();
            int top2 = curY - viewHolder.itemView.getTop();
            int size = list.size();
            for (int i2 = 0; i2 < size; i2++) {
                RecyclerView.ViewHolder viewHolder3 = list.get(i2);
                if (left2 > 0 && (right = viewHolder3.itemView.getRight() - width) < 0 && viewHolder3.itemView.getRight() > viewHolder.itemView.getRight() && (abs4 = Math.abs(right)) > i) {
                    i = abs4;
                    viewHolder2 = viewHolder3;
                }
                if (left2 < 0 && (left = viewHolder3.itemView.getLeft() - curX) > 0 && viewHolder3.itemView.getLeft() < viewHolder.itemView.getLeft() && (abs3 = Math.abs(left)) > i) {
                    i = abs3;
                    viewHolder2 = viewHolder3;
                }
                if (top2 < 0 && (top = viewHolder3.itemView.getTop() - curY) > 0 && viewHolder3.itemView.getTop() < viewHolder.itemView.getTop() && (abs2 = Math.abs(top)) > i) {
                    i = abs2;
                    viewHolder2 = viewHolder3;
                }
                if (top2 > 0 && (bottom = viewHolder3.itemView.getBottom() - height) < 0 && viewHolder3.itemView.getBottom() > viewHolder.itemView.getBottom() && (abs = Math.abs(bottom)) > i) {
                    i = abs;
                    viewHolder2 = viewHolder3;
                }
            }
            List<RecyclerView.ViewHolder> list2 = list;
            return viewHolder2;
        }

        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            ItemTouchUIUtilImpl.INSTANCE.clearView(viewHolder.itemView);
        }

        public int convertToAbsoluteDirection(int flags, int layoutDirection) {
            int i = flags & RELATIVE_DIR_FLAGS;
            if (i == 0) {
                return flags;
            }
            int flags2 = flags & (~i);
            return layoutDirection == 0 ? flags2 | (i >> 2) : flags2 | ((i >> 1) & -3158065) | ((RELATIVE_DIR_FLAGS & (i >> 1)) >> 2);
        }

        /* access modifiers changed from: package-private */
        public final int getAbsoluteMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return convertToAbsoluteDirection(getMovementFlags(recyclerView, viewHolder), ViewCompat.getLayoutDirection(recyclerView));
        }

        public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
            RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
            return itemAnimator == null ? animationType == 8 ? 200 : 250 : animationType == 8 ? itemAnimator.getMoveDuration() : itemAnimator.getRemoveDuration();
        }

        public int getBoundingBoxMargin() {
            return 0;
        }

        public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
            return 0.5f;
        }

        public abstract int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);

        public float getSwipeEscapeVelocity(float defaultValue) {
            return defaultValue;
        }

        public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
            return 0.5f;
        }

        public float getSwipeVelocityThreshold(float defaultValue) {
            return defaultValue;
        }

        /* access modifiers changed from: package-private */
        public boolean hasDragFlag(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return (ItemTouchHelper.ACTION_MODE_DRAG_MASK & getAbsoluteMovementFlags(recyclerView, viewHolder)) != 0;
        }

        /* access modifiers changed from: package-private */
        public boolean hasSwipeFlag(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return (65280 & getAbsoluteMovementFlags(recyclerView, viewHolder)) != 0;
        }

        public int interpolateOutOfBoundsScroll(RecyclerView recyclerView, int viewSize, int viewSizeOutOfBounds, int totalSize, long msSinceStartScroll) {
            int signum = (int) (((float) ((int) (((float) (((int) Math.signum((float) viewSizeOutOfBounds)) * getMaxDragScroll(recyclerView))) * sDragViewScrollCapInterpolator.getInterpolation(Math.min(1.0f, (((float) Math.abs(viewSizeOutOfBounds)) * 1.0f) / ((float) viewSize)))))) * sDragScrollInterpolator.getInterpolation(msSinceStartScroll > DRAG_SCROLL_ACCELERATION_LIMIT_TIME_MS ? 1.0f : ((float) msSinceStartScroll) / 2000.0f));
            return signum == 0 ? viewSizeOutOfBounds > 0 ? 1 : -1 : signum;
        }

        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        public boolean isLongPressDragEnabled() {
            return true;
        }

        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            ItemTouchUIUtilImpl.INSTANCE.onDraw(c, recyclerView, viewHolder.itemView, dX, dY, actionState, isCurrentlyActive);
        }

        public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            ItemTouchUIUtilImpl.INSTANCE.onDrawOver(c, recyclerView, viewHolder.itemView, dX, dY, actionState, isCurrentlyActive);
        }

        /* access modifiers changed from: package-private */
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.ViewHolder selected, List<RecoverAnimation> list, int actionState, float dX, float dY) {
            Canvas canvas = c;
            int size = list.size();
            for (int i = 0; i < size; i++) {
                RecoverAnimation recoverAnimation = list.get(i);
                recoverAnimation.update();
                int save = c.save();
                onChildDraw(c, parent, recoverAnimation.mViewHolder, recoverAnimation.mX, recoverAnimation.mY, recoverAnimation.mActionState, false);
                c.restoreToCount(save);
            }
            List<RecoverAnimation> list2 = list;
            if (selected != null) {
                int save2 = c.save();
                onChildDraw(c, parent, selected, dX, dY, actionState, true);
                c.restoreToCount(save2);
            }
        }

        /* access modifiers changed from: package-private */
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.ViewHolder selected, List<RecoverAnimation> list, int actionState, float dX, float dY) {
            Canvas canvas = c;
            List<RecoverAnimation> list2 = list;
            int size = list.size();
            for (int i = 0; i < size; i++) {
                RecoverAnimation recoverAnimation = list2.get(i);
                int save = c.save();
                onChildDrawOver(c, parent, recoverAnimation.mViewHolder, recoverAnimation.mX, recoverAnimation.mY, recoverAnimation.mActionState, false);
                c.restoreToCount(save);
            }
            if (selected != null) {
                int save2 = c.save();
                onChildDrawOver(c, parent, selected, dX, dY, actionState, true);
                c.restoreToCount(save2);
            }
            boolean z = false;
            for (int i2 = size - 1; i2 >= 0; i2--) {
                RecoverAnimation recoverAnimation2 = list2.get(i2);
                if (recoverAnimation2.mEnded && !recoverAnimation2.mIsPendingCleanup) {
                    list2.remove(i2);
                } else if (!recoverAnimation2.mEnded) {
                    z = true;
                }
            }
            if (z) {
                parent.invalidate();
            }
        }

        public abstract boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2);

        public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof ViewDropHandler) {
                ((ViewDropHandler) layoutManager).prepareForDrop(viewHolder.itemView, target.itemView, x, y);
                return;
            }
            if (layoutManager.canScrollHorizontally()) {
                if (layoutManager.getDecoratedLeft(target.itemView) <= recyclerView.getPaddingLeft()) {
                    recyclerView.scrollToPosition(toPos);
                }
                if (layoutManager.getDecoratedRight(target.itemView) >= recyclerView.getWidth() - recyclerView.getPaddingRight()) {
                    recyclerView.scrollToPosition(toPos);
                }
            }
            if (layoutManager.canScrollVertically()) {
                if (layoutManager.getDecoratedTop(target.itemView) <= recyclerView.getPaddingTop()) {
                    recyclerView.scrollToPosition(toPos);
                }
                if (layoutManager.getDecoratedBottom(target.itemView) >= recyclerView.getHeight() - recyclerView.getPaddingBottom()) {
                    recyclerView.scrollToPosition(toPos);
                }
            }
        }

        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (viewHolder != null) {
                ItemTouchUIUtilImpl.INSTANCE.onSelected(viewHolder.itemView);
            }
        }

        public abstract void onSwiped(RecyclerView.ViewHolder viewHolder, int i);
    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean mShouldReactToLongPress = true;

        ItemTouchHelperGestureListener() {
        }

        /* access modifiers changed from: package-private */
        public void doNotReactToLongPress() {
            this.mShouldReactToLongPress = false;
        }

        public boolean onDown(MotionEvent e) {
            return true;
        }

        public void onLongPress(MotionEvent e) {
            View findChildView;
            RecyclerView.ViewHolder childViewHolder;
            if (this.mShouldReactToLongPress && (findChildView = ItemTouchHelper.this.findChildView(e)) != null && (childViewHolder = ItemTouchHelper.this.mRecyclerView.getChildViewHolder(findChildView)) != null && ItemTouchHelper.this.mCallback.hasDragFlag(ItemTouchHelper.this.mRecyclerView, childViewHolder) && e.getPointerId(0) == ItemTouchHelper.this.mActivePointerId) {
                int findPointerIndex = e.findPointerIndex(ItemTouchHelper.this.mActivePointerId);
                float x = e.getX(findPointerIndex);
                float y = e.getY(findPointerIndex);
                ItemTouchHelper.this.mInitialTouchX = x;
                ItemTouchHelper.this.mInitialTouchY = y;
                ItemTouchHelper itemTouchHelper = ItemTouchHelper.this;
                itemTouchHelper.mDy = 0.0f;
                itemTouchHelper.mDx = 0.0f;
                if (ItemTouchHelper.this.mCallback.isLongPressDragEnabled()) {
                    ItemTouchHelper.this.select(childViewHolder, 2);
                }
            }
        }
    }

    private static class RecoverAnimation implements Animator.AnimatorListener {
        final int mActionState;
        final int mAnimationType;
        boolean mEnded = false;
        private float mFraction;
        boolean mIsPendingCleanup;
        boolean mOverridden = false;
        final float mStartDx;
        final float mStartDy;
        final float mTargetX;
        final float mTargetY;
        private final ValueAnimator mValueAnimator;
        final RecyclerView.ViewHolder mViewHolder;
        float mX;
        float mY;

        RecoverAnimation(RecyclerView.ViewHolder viewHolder, int animationType, int actionState, float startDx, float startDy, float targetX, float targetY) {
            this.mActionState = actionState;
            this.mAnimationType = animationType;
            this.mViewHolder = viewHolder;
            this.mStartDx = startDx;
            this.mStartDy = startDy;
            this.mTargetX = targetX;
            this.mTargetY = targetY;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            this.mValueAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    RecoverAnimation.this.setFraction(animation.getAnimatedFraction());
                }
            });
            ofFloat.setTarget(viewHolder.itemView);
            ofFloat.addListener(this);
            setFraction(0.0f);
        }

        public void cancel() {
            this.mValueAnimator.cancel();
        }

        public void onAnimationCancel(Animator animation) {
            setFraction(1.0f);
        }

        public void onAnimationEnd(Animator animation) {
            if (!this.mEnded) {
                this.mViewHolder.setIsRecyclable(true);
            }
            this.mEnded = true;
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationStart(Animator animation) {
        }

        public void setDuration(long duration) {
            this.mValueAnimator.setDuration(duration);
        }

        public void setFraction(float fraction) {
            this.mFraction = fraction;
        }

        public void start() {
            this.mViewHolder.setIsRecyclable(false);
            this.mValueAnimator.start();
        }

        public void update() {
            float f = this.mStartDx;
            float f2 = this.mTargetX;
            if (f == f2) {
                this.mX = this.mViewHolder.itemView.getTranslationX();
            } else {
                this.mX = f + (this.mFraction * (f2 - f));
            }
            float f3 = this.mStartDy;
            float f4 = this.mTargetY;
            if (f3 == f4) {
                this.mY = this.mViewHolder.itemView.getTranslationY();
            } else {
                this.mY = f3 + (this.mFraction * (f4 - f3));
            }
        }
    }

    public static abstract class SimpleCallback extends Callback {
        private int mDefaultDragDirs;
        private int mDefaultSwipeDirs;

        public SimpleCallback(int dragDirs, int swipeDirs) {
            this.mDefaultSwipeDirs = swipeDirs;
            this.mDefaultDragDirs = dragDirs;
        }

        public int getDragDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return this.mDefaultDragDirs;
        }

        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(getDragDirs(recyclerView, viewHolder), getSwipeDirs(recyclerView, viewHolder));
        }

        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return this.mDefaultSwipeDirs;
        }

        public void setDefaultDragDirs(int defaultDragDirs) {
            this.mDefaultDragDirs = defaultDragDirs;
        }

        public void setDefaultSwipeDirs(int defaultSwipeDirs) {
            this.mDefaultSwipeDirs = defaultSwipeDirs;
        }
    }

    public interface ViewDropHandler {
        void prepareForDrop(View view, View view2, int i, int i2);
    }

    public ItemTouchHelper(Callback callback) {
        this.mCallback = callback;
    }

    private void addChildDrawingOrderCallback() {
        if (Build.VERSION.SDK_INT < 21) {
            if (this.mChildDrawingOrderCallback == null) {
                this.mChildDrawingOrderCallback = new RecyclerView.ChildDrawingOrderCallback() {
                    public int onGetChildDrawingOrder(int childCount, int i) {
                        if (ItemTouchHelper.this.mOverdrawChild == null) {
                            return i;
                        }
                        int i2 = ItemTouchHelper.this.mOverdrawChildPosition;
                        if (i2 == -1) {
                            i2 = ItemTouchHelper.this.mRecyclerView.indexOfChild(ItemTouchHelper.this.mOverdrawChild);
                            ItemTouchHelper.this.mOverdrawChildPosition = i2;
                        }
                        return i == childCount + -1 ? i2 : i < i2 ? i : i + 1;
                    }
                };
            }
            this.mRecyclerView.setChildDrawingOrderCallback(this.mChildDrawingOrderCallback);
        }
    }

    private int checkHorizontalSwipe(RecyclerView.ViewHolder viewHolder, int flags) {
        if ((flags & 12) == 0) {
            return 0;
        }
        int i = 8;
        int i2 = this.mDx > 0.0f ? 8 : 4;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null && this.mActivePointerId > -1) {
            velocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
            float xVelocity = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
            float yVelocity = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
            if (xVelocity <= 0.0f) {
                i = 4;
            }
            int i3 = i;
            float abs = Math.abs(xVelocity);
            if ((i3 & flags) != 0 && i2 == i3 && abs >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && abs > Math.abs(yVelocity)) {
                return i3;
            }
        }
        float width = ((float) this.mRecyclerView.getWidth()) * this.mCallback.getSwipeThreshold(viewHolder);
        if ((flags & i2) == 0 || Math.abs(this.mDx) <= width) {
            return 0;
        }
        return i2;
    }

    private int checkVerticalSwipe(RecyclerView.ViewHolder viewHolder, int flags) {
        if ((flags & 3) == 0) {
            return 0;
        }
        int i = 2;
        int i2 = this.mDy > 0.0f ? 2 : 1;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null && this.mActivePointerId > -1) {
            velocityTracker.computeCurrentVelocity(1000, this.mCallback.getSwipeVelocityThreshold(this.mMaxSwipeVelocity));
            float xVelocity = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
            float yVelocity = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
            if (yVelocity <= 0.0f) {
                i = 1;
            }
            int i3 = i;
            float abs = Math.abs(yVelocity);
            if ((i3 & flags) != 0 && i3 == i2 && abs >= this.mCallback.getSwipeEscapeVelocity(this.mSwipeEscapeVelocity) && abs > Math.abs(xVelocity)) {
                return i3;
            }
        }
        float height = ((float) this.mRecyclerView.getHeight()) * this.mCallback.getSwipeThreshold(viewHolder);
        if ((flags & i2) == 0 || Math.abs(this.mDy) <= height) {
            return 0;
        }
        return i2;
    }

    private void destroyCallbacks() {
        this.mRecyclerView.removeItemDecoration(this);
        this.mRecyclerView.removeOnItemTouchListener(this.mOnItemTouchListener);
        this.mRecyclerView.removeOnChildAttachStateChangeListener(this);
        for (int size = this.mRecoverAnimations.size() - 1; size >= 0; size--) {
            this.mCallback.clearView(this.mRecyclerView, this.mRecoverAnimations.get(0).mViewHolder);
        }
        this.mRecoverAnimations.clear();
        this.mOverdrawChild = null;
        this.mOverdrawChildPosition = -1;
        releaseVelocityTracker();
        stopGestureDetection();
    }

    private List<RecyclerView.ViewHolder> findSwapTargets(RecyclerView.ViewHolder viewHolder) {
        int i;
        int i2;
        RecyclerView.ViewHolder viewHolder2 = viewHolder;
        List<RecyclerView.ViewHolder> list = this.mSwapTargets;
        if (list == null) {
            this.mSwapTargets = new ArrayList();
            this.mDistances = new ArrayList();
        } else {
            list.clear();
            this.mDistances.clear();
        }
        int boundingBoxMargin = this.mCallback.getBoundingBoxMargin();
        int round = Math.round(this.mSelectedStartX + this.mDx) - boundingBoxMargin;
        int round2 = Math.round(this.mSelectedStartY + this.mDy) - boundingBoxMargin;
        int width = viewHolder2.itemView.getWidth() + round + (boundingBoxMargin * 2);
        int height = viewHolder2.itemView.getHeight() + round2 + (boundingBoxMargin * 2);
        int i3 = (round + width) / 2;
        int i4 = (round2 + height) / 2;
        RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        int childCount = layoutManager.getChildCount();
        int i5 = 0;
        while (i5 < childCount) {
            View childAt = layoutManager.getChildAt(i5);
            if (childAt == viewHolder2.itemView) {
                i = boundingBoxMargin;
                i2 = round;
            } else if (childAt.getBottom() < round2 || childAt.getTop() > height) {
                i = boundingBoxMargin;
                i2 = round;
            } else if (childAt.getRight() < round) {
                i = boundingBoxMargin;
                i2 = round;
            } else if (childAt.getLeft() > width) {
                i = boundingBoxMargin;
                i2 = round;
            } else {
                RecyclerView.ViewHolder childViewHolder = this.mRecyclerView.getChildViewHolder(childAt);
                if (this.mCallback.canDropOver(this.mRecyclerView, this.mSelected, childViewHolder)) {
                    int abs = Math.abs(i3 - ((childAt.getLeft() + childAt.getRight()) / 2));
                    int abs2 = Math.abs(i4 - ((childAt.getTop() + childAt.getBottom()) / 2));
                    int i6 = (abs * abs) + (abs2 * abs2);
                    int i7 = abs;
                    int size = this.mSwapTargets.size();
                    i = boundingBoxMargin;
                    int i8 = 0;
                    i2 = round;
                    int i9 = 0;
                    while (true) {
                        if (i9 >= size) {
                            int i10 = size;
                            break;
                        }
                        int i11 = size;
                        if (i6 <= this.mDistances.get(i9).intValue()) {
                            break;
                        }
                        i8++;
                        i9++;
                        size = i11;
                    }
                    this.mSwapTargets.add(i8, childViewHolder);
                    this.mDistances.add(i8, Integer.valueOf(i6));
                } else {
                    i = boundingBoxMargin;
                    i2 = round;
                }
            }
            i5++;
            viewHolder2 = viewHolder;
            round = i2;
            boundingBoxMargin = i;
        }
        return this.mSwapTargets;
    }

    private RecyclerView.ViewHolder findSwipedView(MotionEvent motionEvent) {
        View findChildView;
        RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        int i = this.mActivePointerId;
        if (i == -1) {
            return null;
        }
        int findPointerIndex = motionEvent.findPointerIndex(i);
        float x = motionEvent.getX(findPointerIndex) - this.mInitialTouchX;
        float y = motionEvent.getY(findPointerIndex) - this.mInitialTouchY;
        float abs = Math.abs(x);
        float abs2 = Math.abs(y);
        int i2 = this.mSlop;
        if (abs < ((float) i2) && abs2 < ((float) i2)) {
            return null;
        }
        if (abs > abs2 && layoutManager.canScrollHorizontally()) {
            return null;
        }
        if ((abs2 <= abs || !layoutManager.canScrollVertically()) && (findChildView = findChildView(motionEvent)) != null) {
            return this.mRecyclerView.getChildViewHolder(findChildView);
        }
        return null;
    }

    private void getSelectedDxDy(float[] outPosition) {
        if ((this.mSelectedFlags & 12) != 0) {
            outPosition[0] = (this.mSelectedStartX + this.mDx) - ((float) this.mSelected.itemView.getLeft());
        } else {
            outPosition[0] = this.mSelected.itemView.getTranslationX();
        }
        if ((this.mSelectedFlags & 3) != 0) {
            outPosition[1] = (this.mSelectedStartY + this.mDy) - ((float) this.mSelected.itemView.getTop());
        } else {
            outPosition[1] = this.mSelected.itemView.getTranslationY();
        }
    }

    private static boolean hitTest(View child, float x, float y, float left, float top) {
        return x >= left && x <= ((float) child.getWidth()) + left && y >= top && y <= ((float) child.getHeight()) + top;
    }

    private void releaseVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void setupCallbacks() {
        this.mSlop = ViewConfiguration.get(this.mRecyclerView.getContext()).getScaledTouchSlop();
        this.mRecyclerView.addItemDecoration(this);
        this.mRecyclerView.addOnItemTouchListener(this.mOnItemTouchListener);
        this.mRecyclerView.addOnChildAttachStateChangeListener(this);
        startGestureDetection();
    }

    private void startGestureDetection() {
        this.mItemTouchHelperGestureListener = new ItemTouchHelperGestureListener();
        this.mGestureDetector = new GestureDetectorCompat(this.mRecyclerView.getContext(), this.mItemTouchHelperGestureListener);
    }

    private void stopGestureDetection() {
        ItemTouchHelperGestureListener itemTouchHelperGestureListener = this.mItemTouchHelperGestureListener;
        if (itemTouchHelperGestureListener != null) {
            itemTouchHelperGestureListener.doNotReactToLongPress();
            this.mItemTouchHelperGestureListener = null;
        }
        if (this.mGestureDetector != null) {
            this.mGestureDetector = null;
        }
    }

    private int swipeIfNecessary(RecyclerView.ViewHolder viewHolder) {
        if (this.mActionState == 2) {
            return 0;
        }
        int movementFlags = this.mCallback.getMovementFlags(this.mRecyclerView, viewHolder);
        int convertToAbsoluteDirection = (this.mCallback.convertToAbsoluteDirection(movementFlags, ViewCompat.getLayoutDirection(this.mRecyclerView)) & 65280) >> 8;
        if (convertToAbsoluteDirection == 0) {
            return 0;
        }
        int i = (65280 & movementFlags) >> 8;
        if (Math.abs(this.mDx) > Math.abs(this.mDy)) {
            int checkHorizontalSwipe = checkHorizontalSwipe(viewHolder, convertToAbsoluteDirection);
            int i2 = checkHorizontalSwipe;
            if (checkHorizontalSwipe > 0) {
                return (i & i2) == 0 ? Callback.convertToRelativeDirection(i2, ViewCompat.getLayoutDirection(this.mRecyclerView)) : i2;
            }
            int checkVerticalSwipe = checkVerticalSwipe(viewHolder, convertToAbsoluteDirection);
            int i3 = checkVerticalSwipe;
            if (checkVerticalSwipe > 0) {
                return i3;
            }
        } else {
            int checkVerticalSwipe2 = checkVerticalSwipe(viewHolder, convertToAbsoluteDirection);
            int i4 = checkVerticalSwipe2;
            if (checkVerticalSwipe2 > 0) {
                return i4;
            }
            int checkHorizontalSwipe2 = checkHorizontalSwipe(viewHolder, convertToAbsoluteDirection);
            int i5 = checkHorizontalSwipe2;
            if (checkHorizontalSwipe2 > 0) {
                return (i & i5) == 0 ? Callback.convertToRelativeDirection(i5, ViewCompat.getLayoutDirection(this.mRecyclerView)) : i5;
            }
        }
        return 0;
    }

    public void attachToRecyclerView(RecyclerView recyclerView) {
        RecyclerView recyclerView2 = this.mRecyclerView;
        if (recyclerView2 != recyclerView) {
            if (recyclerView2 != null) {
                destroyCallbacks();
            }
            this.mRecyclerView = recyclerView;
            if (recyclerView != null) {
                Resources resources = recyclerView.getResources();
                this.mSwipeEscapeVelocity = resources.getDimension(R.dimen.item_touch_helper_swipe_escape_velocity);
                this.mMaxSwipeVelocity = resources.getDimension(R.dimen.item_touch_helper_swipe_escape_max_velocity);
                setupCallbacks();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void checkSelectForSwipe(int action, MotionEvent motionEvent, int pointerIndex) {
        RecyclerView.ViewHolder findSwipedView;
        int absoluteMovementFlags;
        if (this.mSelected == null && action == 2 && this.mActionState != 2 && this.mCallback.isItemViewSwipeEnabled() && this.mRecyclerView.getScrollState() != 1 && (findSwipedView = findSwipedView(motionEvent)) != null && (absoluteMovementFlags = (65280 & this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, findSwipedView)) >> 8) != 0) {
            float x = motionEvent.getX(pointerIndex);
            float y = motionEvent.getY(pointerIndex);
            float f = x - this.mInitialTouchX;
            float f2 = y - this.mInitialTouchY;
            float abs = Math.abs(f);
            float abs2 = Math.abs(f2);
            int i = this.mSlop;
            if (abs >= ((float) i) || abs2 >= ((float) i)) {
                if (abs > abs2) {
                    if (f < 0.0f && (absoluteMovementFlags & 4) == 0) {
                        return;
                    }
                    if (f > 0.0f && (absoluteMovementFlags & 8) == 0) {
                        return;
                    }
                } else if (f2 < 0.0f && (absoluteMovementFlags & 1) == 0) {
                    return;
                } else {
                    if (f2 > 0.0f && (absoluteMovementFlags & 2) == 0) {
                        return;
                    }
                }
                this.mDy = 0.0f;
                this.mDx = 0.0f;
                this.mActivePointerId = motionEvent.getPointerId(0);
                select(findSwipedView, 1);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void endRecoverAnimation(RecyclerView.ViewHolder viewHolder, boolean override) {
        for (int size = this.mRecoverAnimations.size() - 1; size >= 0; size--) {
            RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(size);
            if (recoverAnimation.mViewHolder == viewHolder) {
                recoverAnimation.mOverridden |= override;
                if (!recoverAnimation.mEnded) {
                    recoverAnimation.cancel();
                }
                this.mRecoverAnimations.remove(size);
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public RecoverAnimation findAnimation(MotionEvent event) {
        if (this.mRecoverAnimations.isEmpty()) {
            return null;
        }
        View findChildView = findChildView(event);
        for (int size = this.mRecoverAnimations.size() - 1; size >= 0; size--) {
            RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(size);
            if (recoverAnimation.mViewHolder.itemView == findChildView) {
                return recoverAnimation;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public View findChildView(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        RecyclerView.ViewHolder viewHolder = this.mSelected;
        if (viewHolder != null) {
            View view = viewHolder.itemView;
            if (hitTest(view, x, y, this.mSelectedStartX + this.mDx, this.mSelectedStartY + this.mDy)) {
                return view;
            }
        }
        for (int size = this.mRecoverAnimations.size() - 1; size >= 0; size--) {
            RecoverAnimation recoverAnimation = this.mRecoverAnimations.get(size);
            View view2 = recoverAnimation.mViewHolder.itemView;
            if (hitTest(view2, x, y, recoverAnimation.mX, recoverAnimation.mY)) {
                return view2;
            }
        }
        return this.mRecyclerView.findChildViewUnder(x, y);
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.setEmpty();
    }

    /* access modifiers changed from: package-private */
    public boolean hasRunningRecoverAnim() {
        int size = this.mRecoverAnimations.size();
        for (int i = 0; i < size; i++) {
            if (!this.mRecoverAnimations.get(i).mEnded) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void moveIfNecessary(RecyclerView.ViewHolder viewHolder) {
        RecyclerView.ViewHolder viewHolder2 = viewHolder;
        if (!this.mRecyclerView.isLayoutRequested() && this.mActionState == 2) {
            float moveThreshold = this.mCallback.getMoveThreshold(viewHolder2);
            int i = (int) (this.mSelectedStartX + this.mDx);
            int i2 = (int) (this.mSelectedStartY + this.mDy);
            if (((float) Math.abs(i2 - viewHolder2.itemView.getTop())) >= ((float) viewHolder2.itemView.getHeight()) * moveThreshold || ((float) Math.abs(i - viewHolder2.itemView.getLeft())) >= ((float) viewHolder2.itemView.getWidth()) * moveThreshold) {
                List<RecyclerView.ViewHolder> findSwapTargets = findSwapTargets(viewHolder);
                if (findSwapTargets.size() != 0) {
                    RecyclerView.ViewHolder chooseDropTarget = this.mCallback.chooseDropTarget(viewHolder2, findSwapTargets, i, i2);
                    if (chooseDropTarget == null) {
                        this.mSwapTargets.clear();
                        this.mDistances.clear();
                        return;
                    }
                    int adapterPosition = chooseDropTarget.getAdapterPosition();
                    int adapterPosition2 = viewHolder.getAdapterPosition();
                    if (this.mCallback.onMove(this.mRecyclerView, viewHolder2, chooseDropTarget)) {
                        this.mCallback.onMoved(this.mRecyclerView, viewHolder, adapterPosition2, chooseDropTarget, adapterPosition, i, i2);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void obtainVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
        }
        this.mVelocityTracker = VelocityTracker.obtain();
    }

    public void onChildViewAttachedToWindow(View view) {
    }

    public void onChildViewDetachedFromWindow(View view) {
        removeChildDrawingOrderCallbackIfNecessary(view);
        RecyclerView.ViewHolder childViewHolder = this.mRecyclerView.getChildViewHolder(view);
        if (childViewHolder != null) {
            RecyclerView.ViewHolder viewHolder = this.mSelected;
            if (viewHolder == null || childViewHolder != viewHolder) {
                endRecoverAnimation(childViewHolder, false);
                if (this.mPendingCleanup.remove(childViewHolder.itemView)) {
                    this.mCallback.clearView(this.mRecyclerView, childViewHolder);
                    return;
                }
                return;
            }
            select((RecyclerView.ViewHolder) null, 0);
        }
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        this.mOverdrawChildPosition = -1;
        float f = 0.0f;
        float f2 = 0.0f;
        if (this.mSelected != null) {
            getSelectedDxDy(this.mTmpPosition);
            float[] fArr = this.mTmpPosition;
            f = fArr[0];
            f2 = fArr[1];
        }
        this.mCallback.onDraw(c, parent, this.mSelected, this.mRecoverAnimations, this.mActionState, f, f2);
    }

    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        float f = 0.0f;
        float f2 = 0.0f;
        if (this.mSelected != null) {
            getSelectedDxDy(this.mTmpPosition);
            float[] fArr = this.mTmpPosition;
            f = fArr[0];
            f2 = fArr[1];
        }
        this.mCallback.onDrawOver(c, parent, this.mSelected, this.mRecoverAnimations, this.mActionState, f, f2);
    }

    /* access modifiers changed from: package-private */
    public void postDispatchSwipe(final RecoverAnimation anim, final int swipeDir) {
        this.mRecyclerView.post(new Runnable() {
            public void run() {
                if (ItemTouchHelper.this.mRecyclerView != null && ItemTouchHelper.this.mRecyclerView.isAttachedToWindow() && !anim.mOverridden && anim.mViewHolder.getAdapterPosition() != -1) {
                    RecyclerView.ItemAnimator itemAnimator = ItemTouchHelper.this.mRecyclerView.getItemAnimator();
                    if ((itemAnimator == null || !itemAnimator.isRunning((RecyclerView.ItemAnimator.ItemAnimatorFinishedListener) null)) && !ItemTouchHelper.this.hasRunningRecoverAnim()) {
                        ItemTouchHelper.this.mCallback.onSwiped(anim.mViewHolder, swipeDir);
                    } else {
                        ItemTouchHelper.this.mRecyclerView.post(this);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void removeChildDrawingOrderCallbackIfNecessary(View view) {
        if (view == this.mOverdrawChild) {
            this.mOverdrawChild = null;
            if (this.mChildDrawingOrderCallback != null) {
                this.mRecyclerView.setChildDrawingOrderCallback((RecyclerView.ChildDrawingOrderCallback) null);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean scrollIfNecessary() {
        int height;
        int width;
        if (this.mSelected == null) {
            this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long j = this.mDragScrollStartTimeInMs;
        long j2 = j == Long.MIN_VALUE ? 0 : currentTimeMillis - j;
        RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        if (this.mTmpRect == null) {
            this.mTmpRect = new Rect();
        }
        int i = 0;
        int i2 = 0;
        layoutManager.calculateItemDecorationsForChild(this.mSelected.itemView, this.mTmpRect);
        if (layoutManager.canScrollHorizontally()) {
            int i3 = (int) (this.mSelectedStartX + this.mDx);
            int paddingLeft = (i3 - this.mTmpRect.left) - this.mRecyclerView.getPaddingLeft();
            float f = this.mDx;
            if (f < 0.0f && paddingLeft < 0) {
                i = paddingLeft;
            } else if (f > 0.0f && (width = ((this.mSelected.itemView.getWidth() + i3) + this.mTmpRect.right) - (this.mRecyclerView.getWidth() - this.mRecyclerView.getPaddingRight())) > 0) {
                i = width;
            }
        }
        if (layoutManager.canScrollVertically()) {
            int i4 = (int) (this.mSelectedStartY + this.mDy);
            int paddingTop = (i4 - this.mTmpRect.top) - this.mRecyclerView.getPaddingTop();
            float f2 = this.mDy;
            if (f2 < 0.0f && paddingTop < 0) {
                i2 = paddingTop;
            } else if (f2 > 0.0f && (height = ((this.mSelected.itemView.getHeight() + i4) + this.mTmpRect.bottom) - (this.mRecyclerView.getHeight() - this.mRecyclerView.getPaddingBottom())) > 0) {
                i2 = height;
            }
        }
        if (i != 0) {
            i = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getWidth(), i, this.mRecyclerView.getWidth(), j2);
        }
        if (i2 != 0) {
            i2 = this.mCallback.interpolateOutOfBoundsScroll(this.mRecyclerView, this.mSelected.itemView.getHeight(), i2, this.mRecyclerView.getHeight(), j2);
        }
        if (i == 0 && i2 == 0) {
            this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
            return false;
        }
        if (this.mDragScrollStartTimeInMs == Long.MIN_VALUE) {
            this.mDragScrollStartTimeInMs = currentTimeMillis;
        }
        this.mRecyclerView.scrollBy(i, i2);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void select(RecyclerView.ViewHolder selected, int actionState) {
        int i;
        boolean z;
        float f;
        float f2;
        RecyclerView.ViewHolder viewHolder = selected;
        int i2 = actionState;
        if (viewHolder != this.mSelected || i2 != this.mActionState) {
            this.mDragScrollStartTimeInMs = Long.MIN_VALUE;
            int i3 = this.mActionState;
            endRecoverAnimation(viewHolder, true);
            this.mActionState = i2;
            if (i2 == 2) {
                if (viewHolder != null) {
                    this.mOverdrawChild = viewHolder.itemView;
                    addChildDrawingOrderCallback();
                } else {
                    throw new IllegalArgumentException("Must pass a ViewHolder when dragging");
                }
            }
            int i4 = (1 << ((i2 * 8) + 8)) - 1;
            boolean z2 = false;
            if (this.mSelected != null) {
                RecyclerView.ViewHolder viewHolder2 = this.mSelected;
                if (viewHolder2.itemView.getParent() != null) {
                    int swipeIfNecessary = i3 == 2 ? 0 : swipeIfNecessary(viewHolder2);
                    releaseVelocityTracker();
                    switch (swipeIfNecessary) {
                        case 1:
                        case 2:
                            f2 = Math.signum(this.mDy) * ((float) this.mRecyclerView.getHeight());
                            f = 0.0f;
                            break;
                        case 4:
                        case 8:
                        case 16:
                        case 32:
                            f2 = 0.0f;
                            f = Math.signum(this.mDx) * ((float) this.mRecyclerView.getWidth());
                            break;
                        default:
                            f2 = 0.0f;
                            f = 0.0f;
                            break;
                    }
                    int i5 = i3 == 2 ? 8 : swipeIfNecessary > 0 ? 2 : 4;
                    getSelectedDxDy(this.mTmpPosition);
                    float[] fArr = this.mTmpPosition;
                    float f3 = fArr[0];
                    float f4 = fArr[1];
                    RecyclerView.ViewHolder viewHolder3 = viewHolder2;
                    int i6 = i3;
                    final int i7 = swipeIfNecessary;
                    i = 2;
                    final RecyclerView.ViewHolder viewHolder4 = viewHolder3;
                    AnonymousClass3 r0 = new RecoverAnimation(this, viewHolder2, i5, i3, f3, f4, f, f2) {
                        final /* synthetic */ ItemTouchHelper this$0;

                        {
                            this.this$0 = this$0;
                        }

                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (!this.mOverridden) {
                                if (i7 <= 0) {
                                    this.this$0.mCallback.clearView(this.this$0.mRecyclerView, viewHolder4);
                                } else {
                                    this.this$0.mPendingCleanup.add(viewHolder4.itemView);
                                    this.mIsPendingCleanup = true;
                                    int i = i7;
                                    if (i > 0) {
                                        this.this$0.postDispatchSwipe(this, i);
                                    }
                                }
                                if (this.this$0.mOverdrawChild == viewHolder4.itemView) {
                                    this.this$0.removeChildDrawingOrderCallbackIfNecessary(viewHolder4.itemView);
                                }
                            }
                        }
                    };
                    r0.setDuration(this.mCallback.getAnimationDuration(this.mRecyclerView, i5, f - f3, f2 - f4));
                    this.mRecoverAnimations.add(r0);
                    r0.start();
                    z2 = true;
                    RecyclerView.ViewHolder viewHolder5 = viewHolder3;
                } else {
                    int i8 = i3;
                    i = 2;
                    RecyclerView.ViewHolder viewHolder6 = viewHolder2;
                    removeChildDrawingOrderCallbackIfNecessary(viewHolder6.itemView);
                    this.mCallback.clearView(this.mRecyclerView, viewHolder6);
                }
                this.mSelected = null;
            } else {
                int i9 = i3;
                i = 2;
            }
            if (viewHolder != null) {
                this.mSelectedFlags = (this.mCallback.getAbsoluteMovementFlags(this.mRecyclerView, viewHolder) & i4) >> (this.mActionState * 8);
                this.mSelectedStartX = (float) viewHolder.itemView.getLeft();
                this.mSelectedStartY = (float) viewHolder.itemView.getTop();
                this.mSelected = viewHolder;
                if (i2 == i) {
                    z = false;
                    viewHolder.itemView.performHapticFeedback(0);
                } else {
                    z = false;
                }
            } else {
                z = false;
            }
            ViewParent parent = this.mRecyclerView.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(this.mSelected != null ? true : z);
            }
            if (!z2) {
                this.mRecyclerView.getLayoutManager().requestSimpleAnimationsInNextLayout();
            }
            this.mCallback.onSelectedChanged(this.mSelected, this.mActionState);
            this.mRecyclerView.invalidate();
        }
    }

    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        if (!this.mCallback.hasDragFlag(this.mRecyclerView, viewHolder)) {
            Log.e(TAG, "Start drag has been called but dragging is not enabled");
        } else if (viewHolder.itemView.getParent() != this.mRecyclerView) {
            Log.e(TAG, "Start drag has been called with a view holder which is not a child of the RecyclerView which is controlled by this ItemTouchHelper.");
        } else {
            obtainVelocityTracker();
            this.mDy = 0.0f;
            this.mDx = 0.0f;
            select(viewHolder, 2);
        }
    }

    public void startSwipe(RecyclerView.ViewHolder viewHolder) {
        if (!this.mCallback.hasSwipeFlag(this.mRecyclerView, viewHolder)) {
            Log.e(TAG, "Start swipe has been called but swiping is not enabled");
        } else if (viewHolder.itemView.getParent() != this.mRecyclerView) {
            Log.e(TAG, "Start swipe has been called with a view holder which is not a child of the RecyclerView controlled by this ItemTouchHelper.");
        } else {
            obtainVelocityTracker();
            this.mDy = 0.0f;
            this.mDx = 0.0f;
            select(viewHolder, 1);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateDxDy(MotionEvent ev, int directionFlags, int pointerIndex) {
        float x = ev.getX(pointerIndex);
        float y = ev.getY(pointerIndex);
        float f = x - this.mInitialTouchX;
        this.mDx = f;
        this.mDy = y - this.mInitialTouchY;
        if ((directionFlags & 4) == 0) {
            this.mDx = Math.max(0.0f, f);
        }
        if ((directionFlags & 8) == 0) {
            this.mDx = Math.min(0.0f, this.mDx);
        }
        if ((directionFlags & 1) == 0) {
            this.mDy = Math.max(0.0f, this.mDy);
        }
        if ((directionFlags & 2) == 0) {
            this.mDy = Math.min(0.0f, this.mDy);
        }
    }
}

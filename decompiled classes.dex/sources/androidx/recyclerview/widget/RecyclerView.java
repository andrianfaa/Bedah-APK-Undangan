package androidx.recyclerview.widget;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Observable;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.FocusFinder;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.OverScroller;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.os.TraceCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewConfigurationCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.EdgeEffectCompat;
import androidx.customview.view.AbsSavedState;
import androidx.recyclerview.R;
import androidx.recyclerview.widget.AdapterHelper;
import androidx.recyclerview.widget.ChildHelper;
import androidx.recyclerview.widget.GapWorker;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import androidx.recyclerview.widget.ViewBoundsCheck;
import androidx.recyclerview.widget.ViewInfoStore;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mt.Log1F380D;
import okhttp3.HttpUrl;

public class RecyclerView extends ViewGroup implements ScrollingView, NestedScrollingChild2, NestedScrollingChild3 {
    static final boolean ALLOW_SIZE_IN_UNSPECIFIED_SPEC = (Build.VERSION.SDK_INT >= 23);
    static final boolean ALLOW_THREAD_GAP_WORK = (Build.VERSION.SDK_INT >= 21);
    static final boolean DEBUG = false;
    static final int DEFAULT_ORIENTATION = 1;
    static final boolean DISPATCH_TEMP_DETACH = false;
    private static final boolean FORCE_ABS_FOCUS_SEARCH_DIRECTION = (Build.VERSION.SDK_INT <= 15);
    static final boolean FORCE_INVALIDATE_DISPLAY_LIST = (Build.VERSION.SDK_INT == 18 || Build.VERSION.SDK_INT == 19 || Build.VERSION.SDK_INT == 20);
    static final long FOREVER_NS = Long.MAX_VALUE;
    public static final int HORIZONTAL = 0;
    private static final boolean IGNORE_DETACHED_FOCUSED_CHILD = (Build.VERSION.SDK_INT <= 15);
    private static final int INVALID_POINTER = -1;
    public static final int INVALID_TYPE = -1;
    private static final Class<?>[] LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = {Context.class, AttributeSet.class, Integer.TYPE, Integer.TYPE};
    static final int MAX_SCROLL_DURATION = 2000;
    private static final int[] NESTED_SCROLLING_ATTRS = {16843830};
    public static final long NO_ID = -1;
    public static final int NO_POSITION = -1;
    static final boolean POST_UPDATES_ON_ANIMATION = (Build.VERSION.SDK_INT >= 16);
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    static final String TAG = "RecyclerView";
    public static final int TOUCH_SLOP_DEFAULT = 0;
    public static final int TOUCH_SLOP_PAGING = 1;
    static final String TRACE_BIND_VIEW_TAG = "RV OnBindView";
    static final String TRACE_CREATE_VIEW_TAG = "RV CreateView";
    private static final String TRACE_HANDLE_ADAPTER_UPDATES_TAG = "RV PartialInvalidate";
    static final String TRACE_NESTED_PREFETCH_TAG = "RV Nested Prefetch";
    private static final String TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG = "RV FullInvalidate";
    private static final String TRACE_ON_LAYOUT_TAG = "RV OnLayout";
    static final String TRACE_PREFETCH_TAG = "RV Prefetch";
    static final String TRACE_SCROLL_TAG = "RV Scroll";
    public static final int UNDEFINED_DURATION = Integer.MIN_VALUE;
    static final boolean VERBOSE_TRACING = false;
    public static final int VERTICAL = 1;
    static final Interpolator sQuinticInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            float t2 = t - 1.0f;
            return (t2 * t2 * t2 * t2 * t2) + 1.0f;
        }
    };
    RecyclerViewAccessibilityDelegate mAccessibilityDelegate;
    private final AccessibilityManager mAccessibilityManager;
    Adapter mAdapter;
    AdapterHelper mAdapterHelper;
    boolean mAdapterUpdateDuringMeasure;
    private EdgeEffect mBottomGlow;
    private ChildDrawingOrderCallback mChildDrawingOrderCallback;
    ChildHelper mChildHelper;
    boolean mClipToPadding;
    boolean mDataSetHasChangedAfterLayout;
    boolean mDispatchItemsChangedEvent;
    private int mDispatchScrollCounter;
    private int mEatenAccessibilityChangeFlags;
    private EdgeEffectFactory mEdgeEffectFactory;
    boolean mEnableFastScroller;
    boolean mFirstLayoutComplete;
    GapWorker mGapWorker;
    boolean mHasFixedSize;
    private boolean mIgnoreMotionEventTillDown;
    private int mInitialTouchX;
    private int mInitialTouchY;
    private int mInterceptRequestLayoutDepth;
    private OnItemTouchListener mInterceptingOnItemTouchListener;
    boolean mIsAttached;
    ItemAnimator mItemAnimator;
    private ItemAnimator.ItemAnimatorListener mItemAnimatorListener;
    private Runnable mItemAnimatorRunner;
    final ArrayList<ItemDecoration> mItemDecorations;
    boolean mItemsAddedOrRemoved;
    boolean mItemsChanged;
    private int mLastTouchX;
    private int mLastTouchY;
    LayoutManager mLayout;
    private int mLayoutOrScrollCounter;
    boolean mLayoutSuppressed;
    boolean mLayoutWasDefered;
    private EdgeEffect mLeftGlow;
    private final int mMaxFlingVelocity;
    private final int mMinFlingVelocity;
    private final int[] mMinMaxLayoutPositions;
    private final int[] mNestedOffsets;
    private final RecyclerViewDataObserver mObserver;
    private List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners;
    private OnFlingListener mOnFlingListener;
    private final ArrayList<OnItemTouchListener> mOnItemTouchListeners;
    final List<ViewHolder> mPendingAccessibilityImportanceChange;
    private SavedState mPendingSavedState;
    boolean mPostedAnimatorRunner;
    GapWorker.LayoutPrefetchRegistryImpl mPrefetchRegistry;
    private boolean mPreserveFocusAfterLayout;
    final Recycler mRecycler;
    RecyclerListener mRecyclerListener;
    final int[] mReusableIntPair;
    private EdgeEffect mRightGlow;
    private float mScaledHorizontalScrollFactor;
    private float mScaledVerticalScrollFactor;
    private OnScrollListener mScrollListener;
    private List<OnScrollListener> mScrollListeners;
    private final int[] mScrollOffset;
    private int mScrollPointerId;
    private int mScrollState;
    private NestedScrollingChildHelper mScrollingChildHelper;
    final State mState;
    final Rect mTempRect;
    private final Rect mTempRect2;
    final RectF mTempRectF;
    private EdgeEffect mTopGlow;
    private int mTouchSlop;
    final Runnable mUpdateChildViewsRunnable;
    private VelocityTracker mVelocityTracker;
    final ViewFlinger mViewFlinger;
    private final ViewInfoStore.ProcessCallback mViewInfoProcessCallback;
    final ViewInfoStore mViewInfoStore;

    public static abstract class Adapter<VH extends ViewHolder> {
        private boolean mHasStableIds = false;
        private final AdapterDataObservable mObservable = new AdapterDataObservable();

        public final void bindViewHolder(VH vh, int position) {
            vh.mPosition = position;
            if (hasStableIds()) {
                vh.mItemId = getItemId(position);
            }
            vh.setFlags(1, 519);
            TraceCompat.beginSection(RecyclerView.TRACE_BIND_VIEW_TAG);
            onBindViewHolder(vh, position, vh.getUnmodifiedPayloads());
            vh.clearPayload();
            ViewGroup.LayoutParams layoutParams = vh.itemView.getLayoutParams();
            if (layoutParams instanceof LayoutParams) {
                ((LayoutParams) layoutParams).mInsetsDirty = true;
            }
            TraceCompat.endSection();
        }

        public final VH createViewHolder(ViewGroup parent, int viewType) {
            try {
                TraceCompat.beginSection(RecyclerView.TRACE_CREATE_VIEW_TAG);
                VH onCreateViewHolder = onCreateViewHolder(parent, viewType);
                if (onCreateViewHolder.itemView.getParent() == null) {
                    onCreateViewHolder.mItemViewType = viewType;
                    return onCreateViewHolder;
                }
                throw new IllegalStateException("ViewHolder views must not be attached when created. Ensure that you are not passing 'true' to the attachToRoot parameter of LayoutInflater.inflate(..., boolean attachToRoot)");
            } finally {
                TraceCompat.endSection();
            }
        }

        public abstract int getItemCount();

        public long getItemId(int position) {
            return -1;
        }

        public int getItemViewType(int position) {
            return 0;
        }

        public final boolean hasObservers() {
            return this.mObservable.hasObservers();
        }

        public final boolean hasStableIds() {
            return this.mHasStableIds;
        }

        public final void notifyDataSetChanged() {
            this.mObservable.notifyChanged();
        }

        public final void notifyItemChanged(int position) {
            this.mObservable.notifyItemRangeChanged(position, 1);
        }

        public final void notifyItemChanged(int position, Object payload) {
            this.mObservable.notifyItemRangeChanged(position, 1, payload);
        }

        public final void notifyItemInserted(int position) {
            this.mObservable.notifyItemRangeInserted(position, 1);
        }

        public final void notifyItemMoved(int fromPosition, int toPosition) {
            this.mObservable.notifyItemMoved(fromPosition, toPosition);
        }

        public final void notifyItemRangeChanged(int positionStart, int itemCount) {
            this.mObservable.notifyItemRangeChanged(positionStart, itemCount);
        }

        public final void notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
            this.mObservable.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        public final void notifyItemRangeInserted(int positionStart, int itemCount) {
            this.mObservable.notifyItemRangeInserted(positionStart, itemCount);
        }

        public final void notifyItemRangeRemoved(int positionStart, int itemCount) {
            this.mObservable.notifyItemRangeRemoved(positionStart, itemCount);
        }

        public final void notifyItemRemoved(int position) {
            this.mObservable.notifyItemRangeRemoved(position, 1);
        }

        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        }

        public abstract void onBindViewHolder(VH vh, int i);

        public void onBindViewHolder(VH vh, int position, List<Object> list) {
            onBindViewHolder(vh, position);
        }

        public abstract VH onCreateViewHolder(ViewGroup viewGroup, int i);

        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        }

        public boolean onFailedToRecycleView(VH vh) {
            return false;
        }

        public void onViewAttachedToWindow(VH vh) {
        }

        public void onViewDetachedFromWindow(VH vh) {
        }

        public void onViewRecycled(VH vh) {
        }

        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            this.mObservable.registerObserver(observer);
        }

        public void setHasStableIds(boolean hasStableIds) {
            if (!hasObservers()) {
                this.mHasStableIds = hasStableIds;
                return;
            }
            throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
        }

        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            this.mObservable.unregisterObserver(observer);
        }
    }

    static class AdapterDataObservable extends Observable<AdapterDataObserver> {
        AdapterDataObservable() {
        }

        public boolean hasObservers() {
            return !this.mObservers.isEmpty();
        }

        public void notifyChanged() {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((AdapterDataObserver) this.mObservers.get(size)).onChanged();
            }
        }

        public void notifyItemMoved(int fromPosition, int toPosition) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((AdapterDataObserver) this.mObservers.get(size)).onItemRangeMoved(fromPosition, toPosition, 1);
            }
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount, (Object) null);
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((AdapterDataObserver) this.mObservers.get(size)).onItemRangeChanged(positionStart, itemCount, payload);
            }
        }

        public void notifyItemRangeInserted(int positionStart, int itemCount) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((AdapterDataObserver) this.mObservers.get(size)).onItemRangeInserted(positionStart, itemCount);
            }
        }

        public void notifyItemRangeRemoved(int positionStart, int itemCount) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((AdapterDataObserver) this.mObservers.get(size)).onItemRangeRemoved(positionStart, itemCount);
            }
        }
    }

    public static abstract class AdapterDataObserver {
        public void onChanged() {
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            onItemRangeChanged(positionStart, itemCount);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
        }
    }

    public interface ChildDrawingOrderCallback {
        int onGetChildDrawingOrder(int i, int i2);
    }

    public static class EdgeEffectFactory {
        public static final int DIRECTION_BOTTOM = 3;
        public static final int DIRECTION_LEFT = 0;
        public static final int DIRECTION_RIGHT = 2;
        public static final int DIRECTION_TOP = 1;

        @Retention(RetentionPolicy.SOURCE)
        public @interface EdgeDirection {
        }

        /* access modifiers changed from: protected */
        public EdgeEffect createEdgeEffect(RecyclerView view, int direction) {
            return new EdgeEffect(view.getContext());
        }
    }

    public static abstract class ItemAnimator {
        public static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
        public static final int FLAG_CHANGED = 2;
        public static final int FLAG_INVALIDATED = 4;
        public static final int FLAG_MOVED = 2048;
        public static final int FLAG_REMOVED = 8;
        private long mAddDuration = 120;
        private long mChangeDuration = 250;
        private ArrayList<ItemAnimatorFinishedListener> mFinishedListeners = new ArrayList<>();
        private ItemAnimatorListener mListener = null;
        private long mMoveDuration = 250;
        private long mRemoveDuration = 120;

        @Retention(RetentionPolicy.SOURCE)
        public @interface AdapterChanges {
        }

        public interface ItemAnimatorFinishedListener {
            void onAnimationsFinished();
        }

        interface ItemAnimatorListener {
            void onAnimationFinished(ViewHolder viewHolder);
        }

        public static class ItemHolderInfo {
            public int bottom;
            public int changeFlags;
            public int left;
            public int right;
            public int top;

            public ItemHolderInfo setFrom(ViewHolder holder) {
                return setFrom(holder, 0);
            }

            public ItemHolderInfo setFrom(ViewHolder holder, int flags) {
                View view = holder.itemView;
                this.left = view.getLeft();
                this.top = view.getTop();
                this.right = view.getRight();
                this.bottom = view.getBottom();
                return this;
            }
        }

        static int buildAdapterChangeFlagsForAnimations(ViewHolder viewHolder) {
            int i = viewHolder.mFlags & 14;
            if (viewHolder.isInvalid()) {
                return 4;
            }
            if ((i & 4) != 0) {
                return i;
            }
            int oldPosition = viewHolder.getOldPosition();
            int adapterPosition = viewHolder.getAdapterPosition();
            return (oldPosition == -1 || adapterPosition == -1 || oldPosition == adapterPosition) ? i : i | 2048;
        }

        public abstract boolean animateAppearance(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        public abstract boolean animateChange(ViewHolder viewHolder, ViewHolder viewHolder2, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        public abstract boolean animateDisappearance(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        public abstract boolean animatePersistence(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        public boolean canReuseUpdatedViewHolder(ViewHolder viewHolder) {
            return true;
        }

        public boolean canReuseUpdatedViewHolder(ViewHolder viewHolder, List<Object> list) {
            return canReuseUpdatedViewHolder(viewHolder);
        }

        public final void dispatchAnimationFinished(ViewHolder viewHolder) {
            onAnimationFinished(viewHolder);
            ItemAnimatorListener itemAnimatorListener = this.mListener;
            if (itemAnimatorListener != null) {
                itemAnimatorListener.onAnimationFinished(viewHolder);
            }
        }

        public final void dispatchAnimationStarted(ViewHolder viewHolder) {
            onAnimationStarted(viewHolder);
        }

        public final void dispatchAnimationsFinished() {
            int size = this.mFinishedListeners.size();
            for (int i = 0; i < size; i++) {
                this.mFinishedListeners.get(i).onAnimationsFinished();
            }
            this.mFinishedListeners.clear();
        }

        public abstract void endAnimation(ViewHolder viewHolder);

        public abstract void endAnimations();

        public long getAddDuration() {
            return this.mAddDuration;
        }

        public long getChangeDuration() {
            return this.mChangeDuration;
        }

        public long getMoveDuration() {
            return this.mMoveDuration;
        }

        public long getRemoveDuration() {
            return this.mRemoveDuration;
        }

        public abstract boolean isRunning();

        public final boolean isRunning(ItemAnimatorFinishedListener listener) {
            boolean isRunning = isRunning();
            if (listener != null) {
                if (!isRunning) {
                    listener.onAnimationsFinished();
                } else {
                    this.mFinishedListeners.add(listener);
                }
            }
            return isRunning;
        }

        public ItemHolderInfo obtainHolderInfo() {
            return new ItemHolderInfo();
        }

        public void onAnimationFinished(ViewHolder viewHolder) {
        }

        public void onAnimationStarted(ViewHolder viewHolder) {
        }

        public ItemHolderInfo recordPostLayoutInformation(State state, ViewHolder viewHolder) {
            return obtainHolderInfo().setFrom(viewHolder);
        }

        public ItemHolderInfo recordPreLayoutInformation(State state, ViewHolder viewHolder, int changeFlags, List<Object> list) {
            return obtainHolderInfo().setFrom(viewHolder);
        }

        public abstract void runPendingAnimations();

        public void setAddDuration(long addDuration) {
            this.mAddDuration = addDuration;
        }

        public void setChangeDuration(long changeDuration) {
            this.mChangeDuration = changeDuration;
        }

        /* access modifiers changed from: package-private */
        public void setListener(ItemAnimatorListener listener) {
            this.mListener = listener;
        }

        public void setMoveDuration(long moveDuration) {
            this.mMoveDuration = moveDuration;
        }

        public void setRemoveDuration(long removeDuration) {
            this.mRemoveDuration = removeDuration;
        }
    }

    private class ItemAnimatorRestoreListener implements ItemAnimator.ItemAnimatorListener {
        ItemAnimatorRestoreListener() {
        }

        public void onAnimationFinished(ViewHolder item) {
            item.setIsRecyclable(true);
            if (item.mShadowedHolder != null && item.mShadowingHolder == null) {
                item.mShadowedHolder = null;
            }
            item.mShadowingHolder = null;
            if (!item.shouldBeKeptAsChild() && !RecyclerView.this.removeAnimatingView(item.itemView) && item.isTmpDetached()) {
                RecyclerView.this.removeDetachedView(item.itemView, false);
            }
        }
    }

    public static abstract class ItemDecoration {
        @Deprecated
        public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
            outRect.set(0, 0, 0, 0);
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            getItemOffsets(outRect, ((LayoutParams) view.getLayoutParams()).getViewLayoutPosition(), parent);
        }

        @Deprecated
        public void onDraw(Canvas c, RecyclerView parent) {
        }

        public void onDraw(Canvas c, RecyclerView parent, State state) {
            onDraw(c, parent);
        }

        @Deprecated
        public void onDrawOver(Canvas c, RecyclerView parent) {
        }

        public void onDrawOver(Canvas c, RecyclerView parent, State state) {
            onDrawOver(c, parent);
        }
    }

    public static abstract class LayoutManager {
        boolean mAutoMeasure = false;
        ChildHelper mChildHelper;
        private int mHeight;
        private int mHeightMode;
        ViewBoundsCheck mHorizontalBoundCheck;
        private final ViewBoundsCheck.Callback mHorizontalBoundCheckCallback;
        boolean mIsAttachedToWindow = false;
        private boolean mItemPrefetchEnabled = true;
        private boolean mMeasurementCacheEnabled = true;
        int mPrefetchMaxCountObserved;
        boolean mPrefetchMaxObservedInInitialPrefetch;
        RecyclerView mRecyclerView;
        boolean mRequestedSimpleAnimations = false;
        SmoothScroller mSmoothScroller;
        ViewBoundsCheck mVerticalBoundCheck;
        private final ViewBoundsCheck.Callback mVerticalBoundCheckCallback;
        private int mWidth;
        private int mWidthMode;

        public interface LayoutPrefetchRegistry {
            void addPosition(int i, int i2);
        }

        public static class Properties {
            public int orientation;
            public boolean reverseLayout;
            public int spanCount;
            public boolean stackFromEnd;
        }

        public LayoutManager() {
            AnonymousClass1 r0 = new ViewBoundsCheck.Callback() {
                public View getChildAt(int index) {
                    return LayoutManager.this.getChildAt(index);
                }

                public int getChildEnd(View view) {
                    return LayoutManager.this.getDecoratedRight(view) + ((LayoutParams) view.getLayoutParams()).rightMargin;
                }

                public int getChildStart(View view) {
                    return LayoutManager.this.getDecoratedLeft(view) - ((LayoutParams) view.getLayoutParams()).leftMargin;
                }

                public int getParentEnd() {
                    return LayoutManager.this.getWidth() - LayoutManager.this.getPaddingRight();
                }

                public int getParentStart() {
                    return LayoutManager.this.getPaddingLeft();
                }
            };
            this.mHorizontalBoundCheckCallback = r0;
            AnonymousClass2 r1 = new ViewBoundsCheck.Callback() {
                public View getChildAt(int index) {
                    return LayoutManager.this.getChildAt(index);
                }

                public int getChildEnd(View view) {
                    return LayoutManager.this.getDecoratedBottom(view) + ((LayoutParams) view.getLayoutParams()).bottomMargin;
                }

                public int getChildStart(View view) {
                    return LayoutManager.this.getDecoratedTop(view) - ((LayoutParams) view.getLayoutParams()).topMargin;
                }

                public int getParentEnd() {
                    return LayoutManager.this.getHeight() - LayoutManager.this.getPaddingBottom();
                }

                public int getParentStart() {
                    return LayoutManager.this.getPaddingTop();
                }
            };
            this.mVerticalBoundCheckCallback = r1;
            this.mHorizontalBoundCheck = new ViewBoundsCheck(r0);
            this.mVerticalBoundCheck = new ViewBoundsCheck(r1);
        }

        private void addViewInt(View child, int index, boolean disappearing) {
            ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(child);
            if (disappearing || childViewHolderInt.isRemoved()) {
                this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(childViewHolderInt);
            } else {
                this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(childViewHolderInt);
            }
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            if (childViewHolderInt.wasReturnedFromScrap() || childViewHolderInt.isScrap()) {
                if (childViewHolderInt.isScrap()) {
                    childViewHolderInt.unScrap();
                } else {
                    childViewHolderInt.clearReturnedFromScrapFlag();
                }
                this.mChildHelper.attachViewToParent(child, index, child.getLayoutParams(), false);
            } else if (child.getParent() == this.mRecyclerView) {
                int indexOfChild = this.mChildHelper.indexOfChild(child);
                if (index == -1) {
                    index = this.mChildHelper.getChildCount();
                }
                if (indexOfChild == -1) {
                    throw new IllegalStateException("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:" + this.mRecyclerView.indexOfChild(child) + this.mRecyclerView.exceptionLabel());
                } else if (indexOfChild != index) {
                    this.mRecyclerView.mLayout.moveView(indexOfChild, index);
                }
            } else {
                this.mChildHelper.addView(child, index, false);
                layoutParams.mInsetsDirty = true;
                SmoothScroller smoothScroller = this.mSmoothScroller;
                if (smoothScroller != null && smoothScroller.isRunning()) {
                    this.mSmoothScroller.onChildAttachedToWindow(child);
                }
            }
            if (layoutParams.mPendingInvalidate) {
                childViewHolderInt.itemView.invalidate();
                layoutParams.mPendingInvalidate = false;
            }
        }

        public static int chooseSize(int spec, int desired, int min) {
            int mode = View.MeasureSpec.getMode(spec);
            int size = View.MeasureSpec.getSize(spec);
            switch (mode) {
                case Integer.MIN_VALUE:
                    return Math.min(size, Math.max(desired, min));
                case BasicMeasure.EXACTLY:
                    return size;
                default:
                    return Math.max(desired, min);
            }
        }

        private void detachViewInternal(int index, View view) {
            this.mChildHelper.detachViewFromParent(index);
        }

        public static int getChildMeasureSpec(int parentSize, int parentMode, int padding, int childDimension, boolean canScroll) {
            int max = Math.max(0, parentSize - padding);
            int i = 0;
            int i2 = 0;
            if (canScroll) {
                if (childDimension >= 0) {
                    i = childDimension;
                    i2 = BasicMeasure.EXACTLY;
                } else if (childDimension == -1) {
                    switch (parentMode) {
                        case Integer.MIN_VALUE:
                        case BasicMeasure.EXACTLY:
                            i = max;
                            i2 = parentMode;
                            break;
                        case 0:
                            i = 0;
                            i2 = 0;
                            break;
                    }
                } else if (childDimension == -2) {
                    i = 0;
                    i2 = 0;
                }
            } else if (childDimension >= 0) {
                i = childDimension;
                i2 = BasicMeasure.EXACTLY;
            } else if (childDimension == -1) {
                i = max;
                i2 = parentMode;
            } else if (childDimension == -2) {
                i = max;
                i2 = (parentMode == Integer.MIN_VALUE || parentMode == 1073741824) ? Integer.MIN_VALUE : 0;
            }
            return View.MeasureSpec.makeMeasureSpec(i, i2);
        }

        @Deprecated
        public static int getChildMeasureSpec(int parentSize, int padding, int childDimension, boolean canScroll) {
            int max = Math.max(0, parentSize - padding);
            int i = 0;
            int i2 = 0;
            if (canScroll) {
                if (childDimension >= 0) {
                    i = childDimension;
                    i2 = BasicMeasure.EXACTLY;
                } else {
                    i = 0;
                    i2 = 0;
                }
            } else if (childDimension >= 0) {
                i = childDimension;
                i2 = BasicMeasure.EXACTLY;
            } else if (childDimension == -1) {
                i = max;
                i2 = BasicMeasure.EXACTLY;
            } else if (childDimension == -2) {
                i = max;
                i2 = Integer.MIN_VALUE;
            }
            return View.MeasureSpec.makeMeasureSpec(i, i2);
        }

        private int[] getChildRectangleOnScreenScrollAmount(View child, Rect rect) {
            Rect rect2 = rect;
            int[] iArr = new int[2];
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int width = getWidth() - getPaddingRight();
            int height = getHeight() - getPaddingBottom();
            int left = (child.getLeft() + rect2.left) - child.getScrollX();
            int top = (child.getTop() + rect2.top) - child.getScrollY();
            int width2 = rect.width() + left;
            int min = Math.min(0, left - paddingLeft);
            int min2 = Math.min(0, top - paddingTop);
            int max = Math.max(0, width2 - width);
            int max2 = Math.max(0, (rect.height() + top) - height);
            int max3 = getLayoutDirection() == 1 ? max != 0 ? max : Math.max(min, width2 - width) : min != 0 ? min : Math.min(left - paddingLeft, max);
            int min3 = min2 != 0 ? min2 : Math.min(top - paddingTop, max2);
            iArr[0] = max3;
            iArr[1] = min3;
            return iArr;
        }

        public static Properties getProperties(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            Properties properties = new Properties();
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.RecyclerView, defStyleAttr, defStyleRes);
            properties.orientation = obtainStyledAttributes.getInt(R.styleable.RecyclerView_android_orientation, 1);
            properties.spanCount = obtainStyledAttributes.getInt(R.styleable.RecyclerView_spanCount, 1);
            properties.reverseLayout = obtainStyledAttributes.getBoolean(R.styleable.RecyclerView_reverseLayout, false);
            properties.stackFromEnd = obtainStyledAttributes.getBoolean(R.styleable.RecyclerView_stackFromEnd, false);
            obtainStyledAttributes.recycle();
            return properties;
        }

        private boolean isFocusedChildVisibleAfterScrolling(RecyclerView parent, int dx, int dy) {
            View focusedChild = parent.getFocusedChild();
            if (focusedChild == null) {
                return false;
            }
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int width = getWidth() - getPaddingRight();
            int height = getHeight() - getPaddingBottom();
            Rect rect = this.mRecyclerView.mTempRect;
            getDecoratedBoundsWithMargins(focusedChild, rect);
            return rect.left - dx < width && rect.right - dx > paddingLeft && rect.top - dy < height && rect.bottom - dy > paddingTop;
        }

        private static boolean isMeasurementUpToDate(int childSize, int spec, int dimension) {
            int mode = View.MeasureSpec.getMode(spec);
            int size = View.MeasureSpec.getSize(spec);
            if (dimension > 0 && childSize != dimension) {
                return false;
            }
            switch (mode) {
                case Integer.MIN_VALUE:
                    return size >= childSize;
                case 0:
                    return true;
                case BasicMeasure.EXACTLY:
                    return size == childSize;
                default:
                    return false;
            }
        }

        private void scrapOrRecycleView(Recycler recycler, int index, View view) {
            ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (!childViewHolderInt.shouldIgnore()) {
                if (!childViewHolderInt.isInvalid() || childViewHolderInt.isRemoved() || this.mRecyclerView.mAdapter.hasStableIds()) {
                    detachViewAt(index);
                    recycler.scrapView(view);
                    this.mRecyclerView.mViewInfoStore.onViewDetached(childViewHolderInt);
                    return;
                }
                removeViewAt(index);
                recycler.recycleViewHolderInternal(childViewHolderInt);
            }
        }

        public void addDisappearingView(View child) {
            addDisappearingView(child, -1);
        }

        public void addDisappearingView(View child, int index) {
            addViewInt(child, index, true);
        }

        public void addView(View child) {
            addView(child, -1);
        }

        public void addView(View child, int index) {
            addViewInt(child, index, false);
        }

        public void assertInLayoutOrScroll(String message) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                recyclerView.assertInLayoutOrScroll(message);
            }
        }

        public void assertNotInLayoutOrScroll(String message) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                recyclerView.assertNotInLayoutOrScroll(message);
            }
        }

        public void attachView(View child) {
            attachView(child, -1);
        }

        public void attachView(View child, int index) {
            attachView(child, index, (LayoutParams) child.getLayoutParams());
        }

        public void attachView(View child, int index, LayoutParams lp) {
            ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(child);
            if (childViewHolderInt.isRemoved()) {
                this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(childViewHolderInt);
            } else {
                this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(childViewHolderInt);
            }
            this.mChildHelper.attachViewToParent(child, index, lp, childViewHolderInt.isRemoved());
        }

        public void calculateItemDecorationsForChild(View child, Rect outRect) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView == null) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(recyclerView.getItemDecorInsetsForChild(child));
            }
        }

        public boolean canScrollHorizontally() {
            return false;
        }

        public boolean canScrollVertically() {
            return false;
        }

        public boolean checkLayoutParams(LayoutParams lp) {
            return lp != null;
        }

        public void collectAdjacentPrefetchPositions(int dx, int dy, State state, LayoutPrefetchRegistry layoutPrefetchRegistry) {
        }

        public void collectInitialPrefetchPositions(int adapterItemCount, LayoutPrefetchRegistry layoutPrefetchRegistry) {
        }

        public int computeHorizontalScrollExtent(State state) {
            return 0;
        }

        public int computeHorizontalScrollOffset(State state) {
            return 0;
        }

        public int computeHorizontalScrollRange(State state) {
            return 0;
        }

        public int computeVerticalScrollExtent(State state) {
            return 0;
        }

        public int computeVerticalScrollOffset(State state) {
            return 0;
        }

        public int computeVerticalScrollRange(State state) {
            return 0;
        }

        public void detachAndScrapAttachedViews(Recycler recycler) {
            for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                scrapOrRecycleView(recycler, childCount, getChildAt(childCount));
            }
        }

        public void detachAndScrapView(View child, Recycler recycler) {
            scrapOrRecycleView(recycler, this.mChildHelper.indexOfChild(child), child);
        }

        public void detachAndScrapViewAt(int index, Recycler recycler) {
            scrapOrRecycleView(recycler, index, getChildAt(index));
        }

        public void detachView(View child) {
            int indexOfChild = this.mChildHelper.indexOfChild(child);
            if (indexOfChild >= 0) {
                detachViewInternal(indexOfChild, child);
            }
        }

        public void detachViewAt(int index) {
            detachViewInternal(index, getChildAt(index));
        }

        /* access modifiers changed from: package-private */
        public void dispatchAttachedToWindow(RecyclerView view) {
            this.mIsAttachedToWindow = true;
            onAttachedToWindow(view);
        }

        /* access modifiers changed from: package-private */
        public void dispatchDetachedFromWindow(RecyclerView view, Recycler recycler) {
            this.mIsAttachedToWindow = false;
            onDetachedFromWindow(view, recycler);
        }

        public void endAnimation(View view) {
            if (this.mRecyclerView.mItemAnimator != null) {
                this.mRecyclerView.mItemAnimator.endAnimation(RecyclerView.getChildViewHolderInt(view));
            }
        }

        public View findContainingItemView(View view) {
            View findContainingItemView;
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView == null || (findContainingItemView = recyclerView.findContainingItemView(view)) == null || this.mChildHelper.isHidden(findContainingItemView)) {
                return null;
            }
            return findContainingItemView;
        }

        public View findViewByPosition(int position) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);
                ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(childAt);
                if (childViewHolderInt != null && childViewHolderInt.getLayoutPosition() == position && !childViewHolderInt.shouldIgnore() && (this.mRecyclerView.mState.isPreLayout() || !childViewHolderInt.isRemoved())) {
                    return childAt;
                }
            }
            return null;
        }

        public abstract LayoutParams generateDefaultLayoutParams();

        public LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
            return new LayoutParams(c, attrs);
        }

        public LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
            return lp instanceof LayoutParams ? new LayoutParams((LayoutParams) lp) : lp instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams) lp) : new LayoutParams(lp);
        }

        public int getBaseline() {
            return -1;
        }

        public int getBottomDecorationHeight(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.bottom;
        }

        public View getChildAt(int index) {
            ChildHelper childHelper = this.mChildHelper;
            if (childHelper != null) {
                return childHelper.getChildAt(index);
            }
            return null;
        }

        public int getChildCount() {
            ChildHelper childHelper = this.mChildHelper;
            if (childHelper != null) {
                return childHelper.getChildCount();
            }
            return 0;
        }

        public boolean getClipToPadding() {
            RecyclerView recyclerView = this.mRecyclerView;
            return recyclerView != null && recyclerView.mClipToPadding;
        }

        public int getColumnCountForAccessibility(Recycler recycler, State state) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView == null || recyclerView.mAdapter == null || !canScrollHorizontally()) {
                return 1;
            }
            return this.mRecyclerView.mAdapter.getItemCount();
        }

        public int getDecoratedBottom(View child) {
            return child.getBottom() + getBottomDecorationHeight(child);
        }

        public void getDecoratedBoundsWithMargins(View view, Rect outBounds) {
            RecyclerView.getDecoratedBoundsWithMarginsInt(view, outBounds);
        }

        public int getDecoratedLeft(View child) {
            return child.getLeft() - getLeftDecorationWidth(child);
        }

        public int getDecoratedMeasuredHeight(View child) {
            Rect rect = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
            return child.getMeasuredHeight() + rect.top + rect.bottom;
        }

        public int getDecoratedMeasuredWidth(View child) {
            Rect rect = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
            return child.getMeasuredWidth() + rect.left + rect.right;
        }

        public int getDecoratedRight(View child) {
            return child.getRight() + getRightDecorationWidth(child);
        }

        public int getDecoratedTop(View child) {
            return child.getTop() - getTopDecorationHeight(child);
        }

        public View getFocusedChild() {
            View focusedChild;
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView == null || (focusedChild = recyclerView.getFocusedChild()) == null || this.mChildHelper.isHidden(focusedChild)) {
                return null;
            }
            return focusedChild;
        }

        public int getHeight() {
            return this.mHeight;
        }

        public int getHeightMode() {
            return this.mHeightMode;
        }

        public int getItemCount() {
            RecyclerView recyclerView = this.mRecyclerView;
            Adapter adapter = recyclerView != null ? recyclerView.getAdapter() : null;
            if (adapter != null) {
                return adapter.getItemCount();
            }
            return 0;
        }

        public int getItemViewType(View view) {
            return RecyclerView.getChildViewHolderInt(view).getItemViewType();
        }

        public int getLayoutDirection() {
            return ViewCompat.getLayoutDirection(this.mRecyclerView);
        }

        public int getLeftDecorationWidth(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.left;
        }

        public int getMinimumHeight() {
            return ViewCompat.getMinimumHeight(this.mRecyclerView);
        }

        public int getMinimumWidth() {
            return ViewCompat.getMinimumWidth(this.mRecyclerView);
        }

        public int getPaddingBottom() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return recyclerView.getPaddingBottom();
            }
            return 0;
        }

        public int getPaddingEnd() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return ViewCompat.getPaddingEnd(recyclerView);
            }
            return 0;
        }

        public int getPaddingLeft() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return recyclerView.getPaddingLeft();
            }
            return 0;
        }

        public int getPaddingRight() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return recyclerView.getPaddingRight();
            }
            return 0;
        }

        public int getPaddingStart() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return ViewCompat.getPaddingStart(recyclerView);
            }
            return 0;
        }

        public int getPaddingTop() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return recyclerView.getPaddingTop();
            }
            return 0;
        }

        public int getPosition(View view) {
            return ((LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        }

        public int getRightDecorationWidth(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.right;
        }

        public int getRowCountForAccessibility(Recycler recycler, State state) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView == null || recyclerView.mAdapter == null || !canScrollVertically()) {
                return 1;
            }
            return this.mRecyclerView.mAdapter.getItemCount();
        }

        public int getSelectionModeForAccessibility(Recycler recycler, State state) {
            return 0;
        }

        public int getTopDecorationHeight(View child) {
            return ((LayoutParams) child.getLayoutParams()).mDecorInsets.top;
        }

        public void getTransformedBoundingBox(View child, boolean includeDecorInsets, Rect out) {
            Matrix matrix;
            if (includeDecorInsets) {
                Rect rect = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
                out.set(-rect.left, -rect.top, child.getWidth() + rect.right, child.getHeight() + rect.bottom);
            } else {
                out.set(0, 0, child.getWidth(), child.getHeight());
            }
            if (!(this.mRecyclerView == null || (matrix = child.getMatrix()) == null || matrix.isIdentity())) {
                RectF rectF = this.mRecyclerView.mTempRectF;
                rectF.set(out);
                matrix.mapRect(rectF);
                out.set((int) Math.floor((double) rectF.left), (int) Math.floor((double) rectF.top), (int) Math.ceil((double) rectF.right), (int) Math.ceil((double) rectF.bottom));
            }
            out.offset(child.getLeft(), child.getTop());
        }

        public int getWidth() {
            return this.mWidth;
        }

        public int getWidthMode() {
            return this.mWidthMode;
        }

        /* access modifiers changed from: package-private */
        public boolean hasFlexibleChildInBothOrientations() {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                ViewGroup.LayoutParams layoutParams = getChildAt(i).getLayoutParams();
                if (layoutParams.width < 0 && layoutParams.height < 0) {
                    return true;
                }
            }
            return false;
        }

        public boolean hasFocus() {
            RecyclerView recyclerView = this.mRecyclerView;
            return recyclerView != null && recyclerView.hasFocus();
        }

        public void ignoreView(View view) {
            ViewParent parent = view.getParent();
            RecyclerView recyclerView = this.mRecyclerView;
            if (parent != recyclerView || recyclerView.indexOfChild(view) == -1) {
                throw new IllegalArgumentException("View should be fully attached to be ignored" + this.mRecyclerView.exceptionLabel());
            }
            ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            childViewHolderInt.addFlags(128);
            this.mRecyclerView.mViewInfoStore.removeViewHolder(childViewHolderInt);
        }

        public boolean isAttachedToWindow() {
            return this.mIsAttachedToWindow;
        }

        public boolean isAutoMeasureEnabled() {
            return this.mAutoMeasure;
        }

        public boolean isFocused() {
            RecyclerView recyclerView = this.mRecyclerView;
            return recyclerView != null && recyclerView.isFocused();
        }

        public final boolean isItemPrefetchEnabled() {
            return this.mItemPrefetchEnabled;
        }

        public boolean isLayoutHierarchical(Recycler recycler, State state) {
            return false;
        }

        public boolean isMeasurementCacheEnabled() {
            return this.mMeasurementCacheEnabled;
        }

        public boolean isSmoothScrolling() {
            SmoothScroller smoothScroller = this.mSmoothScroller;
            return smoothScroller != null && smoothScroller.isRunning();
        }

        public boolean isViewPartiallyVisible(View child, boolean completelyVisible, boolean acceptEndPointInclusion) {
            boolean z = this.mHorizontalBoundCheck.isViewWithinBoundFlags(child, 24579) && this.mVerticalBoundCheck.isViewWithinBoundFlags(child, 24579);
            return completelyVisible ? z : !z;
        }

        public void layoutDecorated(View child, int left, int top, int right, int bottom) {
            Rect rect = ((LayoutParams) child.getLayoutParams()).mDecorInsets;
            child.layout(rect.left + left, rect.top + top, right - rect.right, bottom - rect.bottom);
        }

        public void layoutDecoratedWithMargins(View child, int left, int top, int right, int bottom) {
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            Rect rect = layoutParams.mDecorInsets;
            child.layout(rect.left + left + layoutParams.leftMargin, rect.top + top + layoutParams.topMargin, (right - rect.right) - layoutParams.rightMargin, (bottom - rect.bottom) - layoutParams.bottomMargin);
        }

        public void measureChild(View child, int widthUsed, int heightUsed) {
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            Rect itemDecorInsetsForChild = this.mRecyclerView.getItemDecorInsetsForChild(child);
            int widthUsed2 = widthUsed + itemDecorInsetsForChild.left + itemDecorInsetsForChild.right;
            int heightUsed2 = heightUsed + itemDecorInsetsForChild.top + itemDecorInsetsForChild.bottom;
            int childMeasureSpec = getChildMeasureSpec(getWidth(), getWidthMode(), getPaddingLeft() + getPaddingRight() + widthUsed2, layoutParams.width, canScrollHorizontally());
            int childMeasureSpec2 = getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom() + heightUsed2, layoutParams.height, canScrollVertically());
            if (shouldMeasureChild(child, childMeasureSpec, childMeasureSpec2, layoutParams)) {
                child.measure(childMeasureSpec, childMeasureSpec2);
            }
        }

        public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            Rect itemDecorInsetsForChild = this.mRecyclerView.getItemDecorInsetsForChild(child);
            int widthUsed2 = widthUsed + itemDecorInsetsForChild.left + itemDecorInsetsForChild.right;
            int heightUsed2 = heightUsed + itemDecorInsetsForChild.top + itemDecorInsetsForChild.bottom;
            int childMeasureSpec = getChildMeasureSpec(getWidth(), getWidthMode(), getPaddingLeft() + getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin + widthUsed2, layoutParams.width, canScrollHorizontally());
            int childMeasureSpec2 = getChildMeasureSpec(getHeight(), getHeightMode(), getPaddingTop() + getPaddingBottom() + layoutParams.topMargin + layoutParams.bottomMargin + heightUsed2, layoutParams.height, canScrollVertically());
            if (shouldMeasureChild(child, childMeasureSpec, childMeasureSpec2, layoutParams)) {
                child.measure(childMeasureSpec, childMeasureSpec2);
            }
        }

        public void moveView(int fromIndex, int toIndex) {
            View childAt = getChildAt(fromIndex);
            if (childAt != null) {
                detachViewAt(fromIndex);
                attachView(childAt, toIndex);
                return;
            }
            throw new IllegalArgumentException("Cannot move a child from non-existing index:" + fromIndex + this.mRecyclerView.toString());
        }

        public void offsetChildrenHorizontal(int dx) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                recyclerView.offsetChildrenHorizontal(dx);
            }
        }

        public void offsetChildrenVertical(int dy) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                recyclerView.offsetChildrenVertical(dy);
            }
        }

        public void onAdapterChanged(Adapter oldAdapter, Adapter newAdapter) {
        }

        public boolean onAddFocusables(RecyclerView recyclerView, ArrayList<View> arrayList, int direction, int focusableMode) {
            return false;
        }

        public void onAttachedToWindow(RecyclerView view) {
        }

        @Deprecated
        public void onDetachedFromWindow(RecyclerView view) {
        }

        public void onDetachedFromWindow(RecyclerView view, Recycler recycler) {
            onDetachedFromWindow(view);
        }

        public View onFocusSearchFailed(View focused, int direction, Recycler recycler, State state) {
            return null;
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
            onInitializeAccessibilityEvent(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, event);
        }

        public void onInitializeAccessibilityEvent(Recycler recycler, State state, AccessibilityEvent event) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null && event != null) {
                boolean z = true;
                if (!recyclerView.canScrollVertically(1) && !this.mRecyclerView.canScrollVertically(-1) && !this.mRecyclerView.canScrollHorizontally(-1) && !this.mRecyclerView.canScrollHorizontally(1)) {
                    z = false;
                }
                event.setScrollable(z);
                if (this.mRecyclerView.mAdapter != null) {
                    event.setItemCount(this.mRecyclerView.mAdapter.getItemCount());
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat info) {
            onInitializeAccessibilityNodeInfo(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, info);
        }

        public void onInitializeAccessibilityNodeInfo(Recycler recycler, State state, AccessibilityNodeInfoCompat info) {
            if (this.mRecyclerView.canScrollVertically(-1) || this.mRecyclerView.canScrollHorizontally(-1)) {
                info.addAction(8192);
                info.setScrollable(true);
            }
            if (this.mRecyclerView.canScrollVertically(1) || this.mRecyclerView.canScrollHorizontally(1)) {
                info.addAction(4096);
                info.setScrollable(true);
            }
            info.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(getRowCountForAccessibility(recycler, state), getColumnCountForAccessibility(recycler, state), isLayoutHierarchical(recycler, state), getSelectionModeForAccessibility(recycler, state)));
        }

        /* access modifiers changed from: package-private */
        public void onInitializeAccessibilityNodeInfoForItem(View host, AccessibilityNodeInfoCompat info) {
            ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(host);
            if (childViewHolderInt != null && !childViewHolderInt.isRemoved() && !this.mChildHelper.isHidden(childViewHolderInt.itemView)) {
                onInitializeAccessibilityNodeInfoForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, host, info);
            }
        }

        public void onInitializeAccessibilityNodeInfoForItem(Recycler recycler, State state, View host, AccessibilityNodeInfoCompat info) {
            int i = 0;
            int position = canScrollVertically() ? getPosition(host) : 0;
            if (canScrollHorizontally()) {
                i = getPosition(host);
            }
            info.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(position, 1, i, 1, false, false));
        }

        public View onInterceptFocusSearch(View focused, int direction) {
            return null;
        }

        public void onItemsAdded(RecyclerView recyclerView, int positionStart, int itemCount) {
        }

        public void onItemsChanged(RecyclerView recyclerView) {
        }

        public void onItemsMoved(RecyclerView recyclerView, int from, int to, int itemCount) {
        }

        public void onItemsRemoved(RecyclerView recyclerView, int positionStart, int itemCount) {
        }

        public void onItemsUpdated(RecyclerView recyclerView, int positionStart, int itemCount) {
        }

        public void onItemsUpdated(RecyclerView recyclerView, int positionStart, int itemCount, Object payload) {
            onItemsUpdated(recyclerView, positionStart, itemCount);
        }

        public void onLayoutChildren(Recycler recycler, State state) {
            Log.e(RecyclerView.TAG, "You must override onLayoutChildren(Recycler recycler, State state) ");
        }

        public void onLayoutCompleted(State state) {
        }

        public void onMeasure(Recycler recycler, State state, int widthSpec, int heightSpec) {
            this.mRecyclerView.defaultOnMeasure(widthSpec, heightSpec);
        }

        @Deprecated
        public boolean onRequestChildFocus(RecyclerView parent, View child, View focused) {
            return isSmoothScrolling() || parent.isComputingLayout();
        }

        public boolean onRequestChildFocus(RecyclerView parent, State state, View child, View focused) {
            return onRequestChildFocus(parent, child, focused);
        }

        public void onRestoreInstanceState(Parcelable state) {
        }

        public Parcelable onSaveInstanceState() {
            return null;
        }

        public void onScrollStateChanged(int state) {
        }

        /* access modifiers changed from: package-private */
        public void onSmoothScrollerStopped(SmoothScroller smoothScroller) {
            if (this.mSmoothScroller == smoothScroller) {
                this.mSmoothScroller = null;
            }
        }

        /* access modifiers changed from: package-private */
        public boolean performAccessibilityAction(int action, Bundle args) {
            return performAccessibilityAction(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, action, args);
        }

        public boolean performAccessibilityAction(Recycler recycler, State state, int action, Bundle args) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView == null) {
                return false;
            }
            int i = 0;
            int i2 = 0;
            switch (action) {
                case 4096:
                    if (recyclerView.canScrollVertically(1)) {
                        i = (getHeight() - getPaddingTop()) - getPaddingBottom();
                    }
                    if (this.mRecyclerView.canScrollHorizontally(1)) {
                        i2 = (getWidth() - getPaddingLeft()) - getPaddingRight();
                        break;
                    }
                    break;
                case 8192:
                    if (recyclerView.canScrollVertically(-1)) {
                        i = -((getHeight() - getPaddingTop()) - getPaddingBottom());
                    }
                    if (this.mRecyclerView.canScrollHorizontally(-1)) {
                        i2 = -((getWidth() - getPaddingLeft()) - getPaddingRight());
                        break;
                    }
                    break;
            }
            if (i == 0 && i2 == 0) {
                return false;
            }
            this.mRecyclerView.smoothScrollBy(i2, i, (Interpolator) null, Integer.MIN_VALUE, true);
            return true;
        }

        /* access modifiers changed from: package-private */
        public boolean performAccessibilityActionForItem(View view, int action, Bundle args) {
            return performAccessibilityActionForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, view, action, args);
        }

        public boolean performAccessibilityActionForItem(Recycler recycler, State state, View view, int action, Bundle args) {
            return false;
        }

        public void postOnAnimation(Runnable action) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                ViewCompat.postOnAnimation(recyclerView, action);
            }
        }

        public void removeAllViews() {
            for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                this.mChildHelper.removeViewAt(childCount);
            }
        }

        public void removeAndRecycleAllViews(Recycler recycler) {
            for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                if (!RecyclerView.getChildViewHolderInt(getChildAt(childCount)).shouldIgnore()) {
                    removeAndRecycleViewAt(childCount, recycler);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void removeAndRecycleScrapInt(Recycler recycler) {
            int scrapCount = recycler.getScrapCount();
            for (int i = scrapCount - 1; i >= 0; i--) {
                View scrapViewAt = recycler.getScrapViewAt(i);
                ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(scrapViewAt);
                if (!childViewHolderInt.shouldIgnore()) {
                    childViewHolderInt.setIsRecyclable(false);
                    if (childViewHolderInt.isTmpDetached()) {
                        this.mRecyclerView.removeDetachedView(scrapViewAt, false);
                    }
                    if (this.mRecyclerView.mItemAnimator != null) {
                        this.mRecyclerView.mItemAnimator.endAnimation(childViewHolderInt);
                    }
                    childViewHolderInt.setIsRecyclable(true);
                    recycler.quickRecycleScrapView(scrapViewAt);
                }
            }
            recycler.clearScrap();
            if (scrapCount > 0) {
                this.mRecyclerView.invalidate();
            }
        }

        public void removeAndRecycleView(View child, Recycler recycler) {
            removeView(child);
            recycler.recycleView(child);
        }

        public void removeAndRecycleViewAt(int index, Recycler recycler) {
            View childAt = getChildAt(index);
            removeViewAt(index);
            recycler.recycleView(childAt);
        }

        public boolean removeCallbacks(Runnable action) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return recyclerView.removeCallbacks(action);
            }
            return false;
        }

        public void removeDetachedView(View child) {
            this.mRecyclerView.removeDetachedView(child, false);
        }

        public void removeView(View child) {
            this.mChildHelper.removeView(child);
        }

        public void removeViewAt(int index) {
            if (getChildAt(index) != null) {
                this.mChildHelper.removeViewAt(index);
            }
        }

        public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate) {
            return requestChildRectangleOnScreen(parent, child, rect, immediate, false);
        }

        public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
            int[] childRectangleOnScreenScrollAmount = getChildRectangleOnScreenScrollAmount(child, rect);
            int i = childRectangleOnScreenScrollAmount[0];
            int i2 = childRectangleOnScreenScrollAmount[1];
            if ((focusedChildVisible && !isFocusedChildVisibleAfterScrolling(parent, i, i2)) || (i == 0 && i2 == 0)) {
                return false;
            }
            if (immediate) {
                parent.scrollBy(i, i2);
            } else {
                parent.smoothScrollBy(i, i2);
            }
            return true;
        }

        public void requestLayout() {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                recyclerView.requestLayout();
            }
        }

        public void requestSimpleAnimationsInNextLayout() {
            this.mRequestedSimpleAnimations = true;
        }

        public int scrollHorizontallyBy(int dx, Recycler recycler, State state) {
            return 0;
        }

        public void scrollToPosition(int position) {
        }

        public int scrollVerticallyBy(int dy, Recycler recycler, State state) {
            return 0;
        }

        @Deprecated
        public void setAutoMeasureEnabled(boolean enabled) {
            this.mAutoMeasure = enabled;
        }

        /* access modifiers changed from: package-private */
        public void setExactMeasureSpecsFrom(RecyclerView recyclerView) {
            setMeasureSpecs(View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), BasicMeasure.EXACTLY), View.MeasureSpec.makeMeasureSpec(recyclerView.getHeight(), BasicMeasure.EXACTLY));
        }

        public final void setItemPrefetchEnabled(boolean enabled) {
            if (enabled != this.mItemPrefetchEnabled) {
                this.mItemPrefetchEnabled = enabled;
                this.mPrefetchMaxCountObserved = 0;
                RecyclerView recyclerView = this.mRecyclerView;
                if (recyclerView != null) {
                    recyclerView.mRecycler.updateViewCacheSize();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void setMeasureSpecs(int wSpec, int hSpec) {
            this.mWidth = View.MeasureSpec.getSize(wSpec);
            int mode = View.MeasureSpec.getMode(wSpec);
            this.mWidthMode = mode;
            if (mode == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
                this.mWidth = 0;
            }
            this.mHeight = View.MeasureSpec.getSize(hSpec);
            int mode2 = View.MeasureSpec.getMode(hSpec);
            this.mHeightMode = mode2;
            if (mode2 == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
                this.mHeight = 0;
            }
        }

        public void setMeasuredDimension(int widthSize, int heightSize) {
            this.mRecyclerView.setMeasuredDimension(widthSize, heightSize);
        }

        public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
            setMeasuredDimension(chooseSize(wSpec, childrenBounds.width() + getPaddingLeft() + getPaddingRight(), getMinimumWidth()), chooseSize(hSpec, childrenBounds.height() + getPaddingTop() + getPaddingBottom(), getMinimumHeight()));
        }

        /* access modifiers changed from: package-private */
        public void setMeasuredDimensionFromChildren(int widthSpec, int heightSpec) {
            int childCount = getChildCount();
            if (childCount == 0) {
                this.mRecyclerView.defaultOnMeasure(widthSpec, heightSpec);
                return;
            }
            int i = Integer.MAX_VALUE;
            int i2 = Integer.MAX_VALUE;
            int i3 = Integer.MIN_VALUE;
            int i4 = Integer.MIN_VALUE;
            for (int i5 = 0; i5 < childCount; i5++) {
                View childAt = getChildAt(i5);
                Rect rect = this.mRecyclerView.mTempRect;
                getDecoratedBoundsWithMargins(childAt, rect);
                if (rect.left < i) {
                    i = rect.left;
                }
                if (rect.right > i3) {
                    i3 = rect.right;
                }
                if (rect.top < i2) {
                    i2 = rect.top;
                }
                if (rect.bottom > i4) {
                    i4 = rect.bottom;
                }
            }
            this.mRecyclerView.mTempRect.set(i, i2, i3, i4);
            setMeasuredDimension(this.mRecyclerView.mTempRect, widthSpec, heightSpec);
        }

        public void setMeasurementCacheEnabled(boolean measurementCacheEnabled) {
            this.mMeasurementCacheEnabled = measurementCacheEnabled;
        }

        /* access modifiers changed from: package-private */
        public void setRecyclerView(RecyclerView recyclerView) {
            if (recyclerView == null) {
                this.mRecyclerView = null;
                this.mChildHelper = null;
                this.mWidth = 0;
                this.mHeight = 0;
            } else {
                this.mRecyclerView = recyclerView;
                this.mChildHelper = recyclerView.mChildHelper;
                this.mWidth = recyclerView.getWidth();
                this.mHeight = recyclerView.getHeight();
            }
            this.mWidthMode = BasicMeasure.EXACTLY;
            this.mHeightMode = BasicMeasure.EXACTLY;
        }

        /* access modifiers changed from: package-private */
        public boolean shouldMeasureChild(View child, int widthSpec, int heightSpec, LayoutParams lp) {
            return child.isLayoutRequested() || !this.mMeasurementCacheEnabled || !isMeasurementUpToDate(child.getWidth(), widthSpec, lp.width) || !isMeasurementUpToDate(child.getHeight(), heightSpec, lp.height);
        }

        /* access modifiers changed from: package-private */
        public boolean shouldMeasureTwice() {
            return false;
        }

        /* access modifiers changed from: package-private */
        public boolean shouldReMeasureChild(View child, int widthSpec, int heightSpec, LayoutParams lp) {
            return !this.mMeasurementCacheEnabled || !isMeasurementUpToDate(child.getMeasuredWidth(), widthSpec, lp.width) || !isMeasurementUpToDate(child.getMeasuredHeight(), heightSpec, lp.height);
        }

        public void smoothScrollToPosition(RecyclerView recyclerView, State state, int position) {
            Log.e(RecyclerView.TAG, "You must override smoothScrollToPosition to support smooth scrolling");
        }

        public void startSmoothScroll(SmoothScroller smoothScroller) {
            SmoothScroller smoothScroller2 = this.mSmoothScroller;
            if (!(smoothScroller2 == null || smoothScroller == smoothScroller2 || !smoothScroller2.isRunning())) {
                this.mSmoothScroller.stop();
            }
            this.mSmoothScroller = smoothScroller;
            smoothScroller.start(this.mRecyclerView, this);
        }

        public void stopIgnoringView(View view) {
            ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            childViewHolderInt.stopIgnoring();
            childViewHolderInt.resetInternal();
            childViewHolderInt.addFlags(4);
        }

        /* access modifiers changed from: package-private */
        public void stopSmoothScroller() {
            SmoothScroller smoothScroller = this.mSmoothScroller;
            if (smoothScroller != null) {
                smoothScroller.stop();
            }
        }

        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        final Rect mDecorInsets = new Rect();
        boolean mInsetsDirty = true;
        boolean mPendingInvalidate = false;
        ViewHolder mViewHolder;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
        }

        public int getViewAdapterPosition() {
            return this.mViewHolder.getAdapterPosition();
        }

        public int getViewLayoutPosition() {
            return this.mViewHolder.getLayoutPosition();
        }

        @Deprecated
        public int getViewPosition() {
            return this.mViewHolder.getPosition();
        }

        public boolean isItemChanged() {
            return this.mViewHolder.isUpdated();
        }

        public boolean isItemRemoved() {
            return this.mViewHolder.isRemoved();
        }

        public boolean isViewInvalid() {
            return this.mViewHolder.isInvalid();
        }

        public boolean viewNeedsUpdate() {
            return this.mViewHolder.needsUpdate();
        }
    }

    public interface OnChildAttachStateChangeListener {
        void onChildViewAttachedToWindow(View view);

        void onChildViewDetachedFromWindow(View view);
    }

    public static abstract class OnFlingListener {
        public abstract boolean onFling(int i, int i2);
    }

    public interface OnItemTouchListener {
        boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent);

        void onRequestDisallowInterceptTouchEvent(boolean z);

        void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent);
    }

    public static abstract class OnScrollListener {
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }

    public static class RecycledViewPool {
        private static final int DEFAULT_MAX_SCRAP = 5;
        private int mAttachCount = 0;
        SparseArray<ScrapData> mScrap = new SparseArray<>();

        static class ScrapData {
            long mBindRunningAverageNs = 0;
            long mCreateRunningAverageNs = 0;
            int mMaxScrap = 5;
            final ArrayList<ViewHolder> mScrapHeap = new ArrayList<>();

            ScrapData() {
            }
        }

        private ScrapData getScrapDataForType(int viewType) {
            ScrapData scrapData = this.mScrap.get(viewType);
            if (scrapData != null) {
                return scrapData;
            }
            ScrapData scrapData2 = new ScrapData();
            this.mScrap.put(viewType, scrapData2);
            return scrapData2;
        }

        /* access modifiers changed from: package-private */
        public void attach() {
            this.mAttachCount++;
        }

        public void clear() {
            for (int i = 0; i < this.mScrap.size(); i++) {
                this.mScrap.valueAt(i).mScrapHeap.clear();
            }
        }

        /* access modifiers changed from: package-private */
        public void detach() {
            this.mAttachCount--;
        }

        /* access modifiers changed from: package-private */
        public void factorInBindTime(int viewType, long bindTimeNs) {
            ScrapData scrapDataForType = getScrapDataForType(viewType);
            scrapDataForType.mBindRunningAverageNs = runningAverage(scrapDataForType.mBindRunningAverageNs, bindTimeNs);
        }

        /* access modifiers changed from: package-private */
        public void factorInCreateTime(int viewType, long createTimeNs) {
            ScrapData scrapDataForType = getScrapDataForType(viewType);
            scrapDataForType.mCreateRunningAverageNs = runningAverage(scrapDataForType.mCreateRunningAverageNs, createTimeNs);
        }

        public ViewHolder getRecycledView(int viewType) {
            ScrapData scrapData = this.mScrap.get(viewType);
            if (scrapData == null || scrapData.mScrapHeap.isEmpty()) {
                return null;
            }
            ArrayList<ViewHolder> arrayList = scrapData.mScrapHeap;
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                if (!arrayList.get(size).isAttachedToTransitionOverlay()) {
                    return arrayList.remove(size);
                }
            }
            return null;
        }

        public int getRecycledViewCount(int viewType) {
            return getScrapDataForType(viewType).mScrapHeap.size();
        }

        /* access modifiers changed from: package-private */
        public void onAdapterChanged(Adapter oldAdapter, Adapter newAdapter, boolean compatibleWithPrevious) {
            if (oldAdapter != null) {
                detach();
            }
            if (!compatibleWithPrevious && this.mAttachCount == 0) {
                clear();
            }
            if (newAdapter != null) {
                attach();
            }
        }

        public void putRecycledView(ViewHolder scrap) {
            int itemViewType = scrap.getItemViewType();
            ArrayList<ViewHolder> arrayList = getScrapDataForType(itemViewType).mScrapHeap;
            if (this.mScrap.get(itemViewType).mMaxScrap > arrayList.size()) {
                scrap.resetInternal();
                arrayList.add(scrap);
            }
        }

        /* access modifiers changed from: package-private */
        public long runningAverage(long oldAverage, long newValue) {
            return oldAverage == 0 ? newValue : ((oldAverage / 4) * 3) + (newValue / 4);
        }

        public void setMaxRecycledViews(int viewType, int max) {
            ScrapData scrapDataForType = getScrapDataForType(viewType);
            scrapDataForType.mMaxScrap = max;
            ArrayList<ViewHolder> arrayList = scrapDataForType.mScrapHeap;
            while (arrayList.size() > max) {
                arrayList.remove(arrayList.size() - 1);
            }
        }

        /* access modifiers changed from: package-private */
        public int size() {
            int i = 0;
            for (int i2 = 0; i2 < this.mScrap.size(); i2++) {
                ArrayList<ViewHolder> arrayList = this.mScrap.valueAt(i2).mScrapHeap;
                if (arrayList != null) {
                    i += arrayList.size();
                }
            }
            return i;
        }

        /* access modifiers changed from: package-private */
        public boolean willBindInTime(int viewType, long approxCurrentNs, long deadlineNs) {
            long j = getScrapDataForType(viewType).mBindRunningAverageNs;
            return j == 0 || approxCurrentNs + j < deadlineNs;
        }

        /* access modifiers changed from: package-private */
        public boolean willCreateInTime(int viewType, long approxCurrentNs, long deadlineNs) {
            long j = getScrapDataForType(viewType).mCreateRunningAverageNs;
            return j == 0 || approxCurrentNs + j < deadlineNs;
        }
    }

    public final class Recycler {
        static final int DEFAULT_CACHE_SIZE = 2;
        final ArrayList<ViewHolder> mAttachedScrap;
        final ArrayList<ViewHolder> mCachedViews = new ArrayList<>();
        ArrayList<ViewHolder> mChangedScrap = null;
        RecycledViewPool mRecyclerPool;
        private int mRequestedCacheMax;
        private final List<ViewHolder> mUnmodifiableAttachedScrap;
        private ViewCacheExtension mViewCacheExtension;
        int mViewCacheMax;

        public Recycler() {
            ArrayList<ViewHolder> arrayList = new ArrayList<>();
            this.mAttachedScrap = arrayList;
            this.mUnmodifiableAttachedScrap = Collections.unmodifiableList(arrayList);
            this.mRequestedCacheMax = 2;
            this.mViewCacheMax = 2;
        }

        private void attachAccessibilityDelegateOnBind(ViewHolder holder) {
            if (RecyclerView.this.isAccessibilityEnabled()) {
                View view = holder.itemView;
                if (ViewCompat.getImportantForAccessibility(view) == 0) {
                    ViewCompat.setImportantForAccessibility(view, 1);
                }
                if (RecyclerView.this.mAccessibilityDelegate != null) {
                    AccessibilityDelegateCompat itemDelegate = RecyclerView.this.mAccessibilityDelegate.getItemDelegate();
                    if (itemDelegate instanceof RecyclerViewAccessibilityDelegate.ItemDelegate) {
                        ((RecyclerViewAccessibilityDelegate.ItemDelegate) itemDelegate).saveOriginalDelegate(view);
                    }
                    ViewCompat.setAccessibilityDelegate(view, itemDelegate);
                }
            }
        }

        private void invalidateDisplayListInt(ViewGroup viewGroup, boolean invalidateThis) {
            for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = viewGroup.getChildAt(childCount);
                if (childAt instanceof ViewGroup) {
                    invalidateDisplayListInt((ViewGroup) childAt, true);
                }
            }
            if (invalidateThis) {
                if (viewGroup.getVisibility() == 4) {
                    viewGroup.setVisibility(0);
                    viewGroup.setVisibility(4);
                    return;
                }
                int visibility = viewGroup.getVisibility();
                viewGroup.setVisibility(4);
                viewGroup.setVisibility(visibility);
            }
        }

        private void invalidateDisplayListInt(ViewHolder holder) {
            if (holder.itemView instanceof ViewGroup) {
                invalidateDisplayListInt((ViewGroup) holder.itemView, false);
            }
        }

        private boolean tryBindViewHolderByDeadline(ViewHolder holder, int offsetPosition, int position, long deadlineNs) {
            holder.mOwnerRecyclerView = RecyclerView.this;
            int itemViewType = holder.getItemViewType();
            long nanoTime = RecyclerView.this.getNanoTime();
            if (deadlineNs != Long.MAX_VALUE && !this.mRecyclerPool.willBindInTime(itemViewType, nanoTime, deadlineNs)) {
                return false;
            }
            RecyclerView.this.mAdapter.bindViewHolder(holder, offsetPosition);
            this.mRecyclerPool.factorInBindTime(holder.getItemViewType(), RecyclerView.this.getNanoTime() - nanoTime);
            attachAccessibilityDelegateOnBind(holder);
            if (!RecyclerView.this.mState.isPreLayout()) {
                return true;
            }
            holder.mPreLayoutPosition = position;
            return true;
        }

        /* access modifiers changed from: package-private */
        public void addViewHolderToRecycledViewPool(ViewHolder holder, boolean dispatchRecycled) {
            RecyclerView.clearNestedRecyclerViewIfNotNested(holder);
            View view = holder.itemView;
            if (RecyclerView.this.mAccessibilityDelegate != null) {
                AccessibilityDelegateCompat itemDelegate = RecyclerView.this.mAccessibilityDelegate.getItemDelegate();
                AccessibilityDelegateCompat accessibilityDelegateCompat = null;
                if (itemDelegate instanceof RecyclerViewAccessibilityDelegate.ItemDelegate) {
                    accessibilityDelegateCompat = ((RecyclerViewAccessibilityDelegate.ItemDelegate) itemDelegate).getAndRemoveOriginalDelegateForItem(view);
                }
                ViewCompat.setAccessibilityDelegate(view, accessibilityDelegateCompat);
            }
            if (dispatchRecycled) {
                dispatchViewRecycled(holder);
            }
            holder.mOwnerRecyclerView = null;
            getRecycledViewPool().putRecycledView(holder);
        }

        public void bindViewToPosition(View view, int position) {
            LayoutParams layoutParams;
            ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt != null) {
                int findPositionOffset = RecyclerView.this.mAdapterHelper.findPositionOffset(position);
                if (findPositionOffset < 0 || findPositionOffset >= RecyclerView.this.mAdapter.getItemCount()) {
                    throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + position + "(offset:" + findPositionOffset + ").state:" + RecyclerView.this.mState.getItemCount() + RecyclerView.this.exceptionLabel());
                }
                tryBindViewHolderByDeadline(childViewHolderInt, findPositionOffset, position, Long.MAX_VALUE);
                ViewGroup.LayoutParams layoutParams2 = childViewHolderInt.itemView.getLayoutParams();
                if (layoutParams2 == null) {
                    layoutParams = (LayoutParams) RecyclerView.this.generateDefaultLayoutParams();
                    childViewHolderInt.itemView.setLayoutParams(layoutParams);
                } else if (!RecyclerView.this.checkLayoutParams(layoutParams2)) {
                    layoutParams = (LayoutParams) RecyclerView.this.generateLayoutParams(layoutParams2);
                    childViewHolderInt.itemView.setLayoutParams(layoutParams);
                } else {
                    layoutParams = (LayoutParams) layoutParams2;
                }
                boolean z = true;
                layoutParams.mInsetsDirty = true;
                layoutParams.mViewHolder = childViewHolderInt;
                if (childViewHolderInt.itemView.getParent() != null) {
                    z = false;
                }
                layoutParams.mPendingInvalidate = z;
                return;
            }
            throw new IllegalArgumentException("The view does not have a ViewHolder. You cannot pass arbitrary views to this method, they should be created by the Adapter" + RecyclerView.this.exceptionLabel());
        }

        public void clear() {
            this.mAttachedScrap.clear();
            recycleAndClearCachedViews();
        }

        /* access modifiers changed from: package-private */
        public void clearOldPositions() {
            int size = this.mCachedViews.size();
            for (int i = 0; i < size; i++) {
                this.mCachedViews.get(i).clearOldPosition();
            }
            int size2 = this.mAttachedScrap.size();
            for (int i2 = 0; i2 < size2; i2++) {
                this.mAttachedScrap.get(i2).clearOldPosition();
            }
            ArrayList<ViewHolder> arrayList = this.mChangedScrap;
            if (arrayList != null) {
                int size3 = arrayList.size();
                for (int i3 = 0; i3 < size3; i3++) {
                    this.mChangedScrap.get(i3).clearOldPosition();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void clearScrap() {
            this.mAttachedScrap.clear();
            ArrayList<ViewHolder> arrayList = this.mChangedScrap;
            if (arrayList != null) {
                arrayList.clear();
            }
        }

        public int convertPreLayoutPositionToPostLayout(int position) {
            if (position >= 0 && position < RecyclerView.this.mState.getItemCount()) {
                return !RecyclerView.this.mState.isPreLayout() ? position : RecyclerView.this.mAdapterHelper.findPositionOffset(position);
            }
            throw new IndexOutOfBoundsException("invalid position " + position + ". State item count is " + RecyclerView.this.mState.getItemCount() + RecyclerView.this.exceptionLabel());
        }

        /* access modifiers changed from: package-private */
        public void dispatchViewRecycled(ViewHolder holder) {
            if (RecyclerView.this.mRecyclerListener != null) {
                RecyclerView.this.mRecyclerListener.onViewRecycled(holder);
            }
            if (RecyclerView.this.mAdapter != null) {
                RecyclerView.this.mAdapter.onViewRecycled(holder);
            }
            if (RecyclerView.this.mState != null) {
                RecyclerView.this.mViewInfoStore.removeViewHolder(holder);
            }
        }

        /* access modifiers changed from: package-private */
        public ViewHolder getChangedScrapViewForPosition(int position) {
            int findPositionOffset;
            ArrayList<ViewHolder> arrayList = this.mChangedScrap;
            if (arrayList != null) {
                int size = arrayList.size();
                int i = size;
                if (size != 0) {
                    int i2 = 0;
                    while (i2 < i) {
                        ViewHolder viewHolder = this.mChangedScrap.get(i2);
                        if (viewHolder.wasReturnedFromScrap() || viewHolder.getLayoutPosition() != position) {
                            i2++;
                        } else {
                            viewHolder.addFlags(32);
                            return viewHolder;
                        }
                    }
                    if (RecyclerView.this.mAdapter.hasStableIds() && (findPositionOffset = RecyclerView.this.mAdapterHelper.findPositionOffset(position)) > 0 && findPositionOffset < RecyclerView.this.mAdapter.getItemCount()) {
                        long itemId = RecyclerView.this.mAdapter.getItemId(findPositionOffset);
                        int i3 = 0;
                        while (i3 < i) {
                            ViewHolder viewHolder2 = this.mChangedScrap.get(i3);
                            if (viewHolder2.wasReturnedFromScrap() || viewHolder2.getItemId() != itemId) {
                                i3++;
                            } else {
                                viewHolder2.addFlags(32);
                                return viewHolder2;
                            }
                        }
                    }
                    return null;
                }
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        public RecycledViewPool getRecycledViewPool() {
            if (this.mRecyclerPool == null) {
                this.mRecyclerPool = new RecycledViewPool();
            }
            return this.mRecyclerPool;
        }

        /* access modifiers changed from: package-private */
        public int getScrapCount() {
            return this.mAttachedScrap.size();
        }

        public List<ViewHolder> getScrapList() {
            return this.mUnmodifiableAttachedScrap;
        }

        /* access modifiers changed from: package-private */
        public ViewHolder getScrapOrCachedViewForId(long id, int type, boolean dryRun) {
            for (int size = this.mAttachedScrap.size() - 1; size >= 0; size--) {
                ViewHolder viewHolder = this.mAttachedScrap.get(size);
                if (viewHolder.getItemId() == id && !viewHolder.wasReturnedFromScrap()) {
                    if (type == viewHolder.getItemViewType()) {
                        viewHolder.addFlags(32);
                        if (viewHolder.isRemoved() && !RecyclerView.this.mState.isPreLayout()) {
                            viewHolder.setFlags(2, 14);
                        }
                        return viewHolder;
                    } else if (!dryRun) {
                        this.mAttachedScrap.remove(size);
                        RecyclerView.this.removeDetachedView(viewHolder.itemView, false);
                        quickRecycleScrapView(viewHolder.itemView);
                    }
                }
            }
            for (int size2 = this.mCachedViews.size() - 1; size2 >= 0; size2--) {
                ViewHolder viewHolder2 = this.mCachedViews.get(size2);
                if (viewHolder2.getItemId() == id && !viewHolder2.isAttachedToTransitionOverlay()) {
                    if (type == viewHolder2.getItemViewType()) {
                        if (!dryRun) {
                            this.mCachedViews.remove(size2);
                        }
                        return viewHolder2;
                    } else if (!dryRun) {
                        recycleCachedViewAt(size2);
                        return null;
                    }
                }
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        public ViewHolder getScrapOrHiddenOrCachedHolderForPosition(int position, boolean dryRun) {
            View findHiddenNonRemovedView;
            int size = this.mAttachedScrap.size();
            int i = 0;
            while (i < size) {
                ViewHolder viewHolder = this.mAttachedScrap.get(i);
                if (viewHolder.wasReturnedFromScrap() || viewHolder.getLayoutPosition() != position || viewHolder.isInvalid() || (!RecyclerView.this.mState.mInPreLayout && viewHolder.isRemoved())) {
                    i++;
                } else {
                    viewHolder.addFlags(32);
                    return viewHolder;
                }
            }
            if (dryRun || (findHiddenNonRemovedView = RecyclerView.this.mChildHelper.findHiddenNonRemovedView(position)) == null) {
                int size2 = this.mCachedViews.size();
                int i2 = 0;
                while (i2 < size2) {
                    ViewHolder viewHolder2 = this.mCachedViews.get(i2);
                    if (viewHolder2.isInvalid() || viewHolder2.getLayoutPosition() != position || viewHolder2.isAttachedToTransitionOverlay()) {
                        i2++;
                    } else {
                        if (!dryRun) {
                            this.mCachedViews.remove(i2);
                        }
                        return viewHolder2;
                    }
                }
                return null;
            }
            ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(findHiddenNonRemovedView);
            RecyclerView.this.mChildHelper.unhide(findHiddenNonRemovedView);
            int indexOfChild = RecyclerView.this.mChildHelper.indexOfChild(findHiddenNonRemovedView);
            if (indexOfChild != -1) {
                RecyclerView.this.mChildHelper.detachViewFromParent(indexOfChild);
                scrapView(findHiddenNonRemovedView);
                childViewHolderInt.addFlags(8224);
                return childViewHolderInt;
            }
            throw new IllegalStateException("layout index should not be -1 after unhiding a view:" + childViewHolderInt + RecyclerView.this.exceptionLabel());
        }

        /* access modifiers changed from: package-private */
        public View getScrapViewAt(int index) {
            return this.mAttachedScrap.get(index).itemView;
        }

        public View getViewForPosition(int position) {
            return getViewForPosition(position, false);
        }

        /* access modifiers changed from: package-private */
        public View getViewForPosition(int position, boolean dryRun) {
            return tryGetViewHolderForPositionByDeadline(position, dryRun, Long.MAX_VALUE).itemView;
        }

        /* access modifiers changed from: package-private */
        public void markItemDecorInsetsDirty() {
            int size = this.mCachedViews.size();
            for (int i = 0; i < size; i++) {
                LayoutParams layoutParams = (LayoutParams) this.mCachedViews.get(i).itemView.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.mInsetsDirty = true;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void markKnownViewsInvalid() {
            int size = this.mCachedViews.size();
            for (int i = 0; i < size; i++) {
                ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder != null) {
                    viewHolder.addFlags(6);
                    viewHolder.addChangePayload((Object) null);
                }
            }
            if (RecyclerView.this.mAdapter == null || !RecyclerView.this.mAdapter.hasStableIds()) {
                recycleAndClearCachedViews();
            }
        }

        /* access modifiers changed from: package-private */
        public void offsetPositionRecordsForInsert(int insertedAt, int count) {
            int size = this.mCachedViews.size();
            for (int i = 0; i < size; i++) {
                ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder != null && viewHolder.mPosition >= insertedAt) {
                    viewHolder.offsetPosition(count, true);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void offsetPositionRecordsForMove(int from, int to) {
            int i;
            int i2;
            int i3;
            if (from < to) {
                i3 = from;
                i2 = to;
                i = -1;
            } else {
                i3 = to;
                i2 = from;
                i = 1;
            }
            int size = this.mCachedViews.size();
            for (int i4 = 0; i4 < size; i4++) {
                ViewHolder viewHolder = this.mCachedViews.get(i4);
                if (viewHolder != null && viewHolder.mPosition >= i3 && viewHolder.mPosition <= i2) {
                    if (viewHolder.mPosition == from) {
                        viewHolder.offsetPosition(to - from, false);
                    } else {
                        viewHolder.offsetPosition(i, false);
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void offsetPositionRecordsForRemove(int removedFrom, int count, boolean applyToPreLayout) {
            int i = removedFrom + count;
            for (int size = this.mCachedViews.size() - 1; size >= 0; size--) {
                ViewHolder viewHolder = this.mCachedViews.get(size);
                if (viewHolder != null) {
                    if (viewHolder.mPosition >= i) {
                        viewHolder.offsetPosition(-count, applyToPreLayout);
                    } else if (viewHolder.mPosition >= removedFrom) {
                        viewHolder.addFlags(8);
                        recycleCachedViewAt(size);
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void onAdapterChanged(Adapter oldAdapter, Adapter newAdapter, boolean compatibleWithPrevious) {
            clear();
            getRecycledViewPool().onAdapterChanged(oldAdapter, newAdapter, compatibleWithPrevious);
        }

        /* access modifiers changed from: package-private */
        public void quickRecycleScrapView(View view) {
            ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            childViewHolderInt.mScrapContainer = null;
            childViewHolderInt.mInChangeScrap = false;
            childViewHolderInt.clearReturnedFromScrapFlag();
            recycleViewHolderInternal(childViewHolderInt);
        }

        /* access modifiers changed from: package-private */
        public void recycleAndClearCachedViews() {
            for (int size = this.mCachedViews.size() - 1; size >= 0; size--) {
                recycleCachedViewAt(size);
            }
            this.mCachedViews.clear();
            if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions();
            }
        }

        /* access modifiers changed from: package-private */
        public void recycleCachedViewAt(int cachedViewIndex) {
            addViewHolderToRecycledViewPool(this.mCachedViews.get(cachedViewIndex), true);
            this.mCachedViews.remove(cachedViewIndex);
        }

        public void recycleView(View view) {
            ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt.isTmpDetached()) {
                RecyclerView.this.removeDetachedView(view, false);
            }
            if (childViewHolderInt.isScrap()) {
                childViewHolderInt.unScrap();
            } else if (childViewHolderInt.wasReturnedFromScrap()) {
                childViewHolderInt.clearReturnedFromScrapFlag();
            }
            recycleViewHolderInternal(childViewHolderInt);
            if (RecyclerView.this.mItemAnimator != null && !childViewHolderInt.isRecyclable()) {
                RecyclerView.this.mItemAnimator.endAnimation(childViewHolderInt);
            }
        }

        /* access modifiers changed from: package-private */
        public void recycleViewHolderInternal(ViewHolder holder) {
            boolean z = false;
            if (holder.isScrap() || holder.itemView.getParent() != null) {
                StringBuilder append = new StringBuilder().append("Scrapped or attached views may not be recycled. isScrap:").append(holder.isScrap()).append(" isAttached:");
                if (holder.itemView.getParent() != null) {
                    z = true;
                }
                throw new IllegalArgumentException(append.append(z).append(RecyclerView.this.exceptionLabel()).toString());
            } else if (holder.isTmpDetached()) {
                throw new IllegalArgumentException("Tmp detached view should be removed from RecyclerView before it can be recycled: " + holder + RecyclerView.this.exceptionLabel());
            } else if (!holder.shouldIgnore()) {
                boolean doesTransientStatePreventRecycling = holder.doesTransientStatePreventRecycling();
                boolean z2 = false;
                boolean z3 = false;
                if ((RecyclerView.this.mAdapter != null && doesTransientStatePreventRecycling && RecyclerView.this.mAdapter.onFailedToRecycleView(holder)) || holder.isRecyclable()) {
                    if (this.mViewCacheMax > 0 && !holder.hasAnyOfTheFlags(526)) {
                        int size = this.mCachedViews.size();
                        if (size >= this.mViewCacheMax && size > 0) {
                            recycleCachedViewAt(0);
                            size--;
                        }
                        int i = size;
                        if (RecyclerView.ALLOW_THREAD_GAP_WORK && size > 0 && !RecyclerView.this.mPrefetchRegistry.lastPrefetchIncludedPosition(holder.mPosition)) {
                            int i2 = size - 1;
                            while (i2 >= 0) {
                                if (!RecyclerView.this.mPrefetchRegistry.lastPrefetchIncludedPosition(this.mCachedViews.get(i2).mPosition)) {
                                    break;
                                }
                                i2--;
                            }
                            i = i2 + 1;
                        }
                        this.mCachedViews.add(i, holder);
                        z2 = true;
                    }
                    if (!z2) {
                        addViewHolderToRecycledViewPool(holder, true);
                        z3 = true;
                    }
                }
                RecyclerView.this.mViewInfoStore.removeViewHolder(holder);
                if (!z2 && !z3 && doesTransientStatePreventRecycling) {
                    holder.mOwnerRecyclerView = null;
                }
            } else {
                throw new IllegalArgumentException("Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle." + RecyclerView.this.exceptionLabel());
            }
        }

        /* access modifiers changed from: package-private */
        public void scrapView(View view) {
            ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (!childViewHolderInt.hasAnyOfTheFlags(12) && childViewHolderInt.isUpdated() && !RecyclerView.this.canReuseUpdatedViewHolder(childViewHolderInt)) {
                if (this.mChangedScrap == null) {
                    this.mChangedScrap = new ArrayList<>();
                }
                childViewHolderInt.setScrapContainer(this, true);
                this.mChangedScrap.add(childViewHolderInt);
            } else if (!childViewHolderInt.isInvalid() || childViewHolderInt.isRemoved() || RecyclerView.this.mAdapter.hasStableIds()) {
                childViewHolderInt.setScrapContainer(this, false);
                this.mAttachedScrap.add(childViewHolderInt);
            } else {
                throw new IllegalArgumentException("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool." + RecyclerView.this.exceptionLabel());
            }
        }

        /* access modifiers changed from: package-private */
        public void setRecycledViewPool(RecycledViewPool pool) {
            RecycledViewPool recycledViewPool = this.mRecyclerPool;
            if (recycledViewPool != null) {
                recycledViewPool.detach();
            }
            this.mRecyclerPool = pool;
            if (pool != null && RecyclerView.this.getAdapter() != null) {
                this.mRecyclerPool.attach();
            }
        }

        /* access modifiers changed from: package-private */
        public void setViewCacheExtension(ViewCacheExtension extension) {
            this.mViewCacheExtension = extension;
        }

        public void setViewCacheSize(int viewCount) {
            this.mRequestedCacheMax = viewCount;
            updateViewCacheSize();
        }

        /* access modifiers changed from: package-private */
        public ViewHolder tryGetViewHolderForPositionByDeadline(int position, boolean dryRun, long deadlineNs) {
            ViewHolder viewHolder;
            boolean z;
            LayoutParams layoutParams;
            RecyclerView findNestedRecyclerView;
            ViewCacheExtension viewCacheExtension;
            View viewForPositionAndType;
            int i = position;
            boolean z2 = dryRun;
            if (i < 0 || i >= RecyclerView.this.mState.getItemCount()) {
                throw new IndexOutOfBoundsException("Invalid item position " + i + "(" + i + "). Item count:" + RecyclerView.this.mState.getItemCount() + RecyclerView.this.exceptionLabel());
            }
            boolean z3 = false;
            ViewHolder viewHolder2 = null;
            boolean z4 = true;
            if (RecyclerView.this.mState.isPreLayout()) {
                viewHolder2 = getChangedScrapViewForPosition(position);
                z3 = viewHolder2 != null;
            }
            if (viewHolder2 == null && (viewHolder2 = getScrapOrHiddenOrCachedHolderForPosition(position, dryRun)) != null) {
                if (!validateViewHolderForOffsetPosition(viewHolder2)) {
                    if (!z2) {
                        viewHolder2.addFlags(4);
                        if (viewHolder2.isScrap()) {
                            RecyclerView.this.removeDetachedView(viewHolder2.itemView, false);
                            viewHolder2.unScrap();
                        } else if (viewHolder2.wasReturnedFromScrap()) {
                            viewHolder2.clearReturnedFromScrapFlag();
                        }
                        recycleViewHolderInternal(viewHolder2);
                    }
                    viewHolder2 = null;
                } else {
                    z3 = true;
                }
            }
            if (viewHolder2 == null) {
                int findPositionOffset = RecyclerView.this.mAdapterHelper.findPositionOffset(i);
                if (findPositionOffset < 0 || findPositionOffset >= RecyclerView.this.mAdapter.getItemCount()) {
                    throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item position " + i + "(offset:" + findPositionOffset + ").state:" + RecyclerView.this.mState.getItemCount() + RecyclerView.this.exceptionLabel());
                }
                int itemViewType = RecyclerView.this.mAdapter.getItemViewType(findPositionOffset);
                if (RecyclerView.this.mAdapter.hasStableIds() && (viewHolder2 = getScrapOrCachedViewForId(RecyclerView.this.mAdapter.getItemId(findPositionOffset), itemViewType, z2)) != null) {
                    viewHolder2.mPosition = findPositionOffset;
                    z3 = true;
                }
                if (!(viewHolder2 != null || (viewCacheExtension = this.mViewCacheExtension) == null || (viewForPositionAndType = viewCacheExtension.getViewForPositionAndType(this, i, itemViewType)) == null)) {
                    viewHolder2 = RecyclerView.this.getChildViewHolder(viewForPositionAndType);
                    if (viewHolder2 == null) {
                        throw new IllegalArgumentException("getViewForPositionAndType returned a view which does not have a ViewHolder" + RecyclerView.this.exceptionLabel());
                    } else if (viewHolder2.shouldIgnore()) {
                        throw new IllegalArgumentException("getViewForPositionAndType returned a view that is ignored. You must call stopIgnoring before returning this view." + RecyclerView.this.exceptionLabel());
                    }
                }
                if (viewHolder2 == null && (viewHolder2 = getRecycledViewPool().getRecycledView(itemViewType)) != null) {
                    viewHolder2.resetInternal();
                    if (RecyclerView.FORCE_INVALIDATE_DISPLAY_LIST) {
                        invalidateDisplayListInt(viewHolder2);
                    }
                }
                if (viewHolder2 == null) {
                    long nanoTime = RecyclerView.this.getNanoTime();
                    if (deadlineNs != Long.MAX_VALUE && !this.mRecyclerPool.willCreateInTime(itemViewType, nanoTime, deadlineNs)) {
                        return null;
                    }
                    ViewHolder createViewHolder = RecyclerView.this.mAdapter.createViewHolder(RecyclerView.this, itemViewType);
                    if (RecyclerView.ALLOW_THREAD_GAP_WORK && (findNestedRecyclerView = RecyclerView.findNestedRecyclerView(createViewHolder.itemView)) != null) {
                        createViewHolder.mNestedRecyclerView = new WeakReference<>(findNestedRecyclerView);
                    }
                    this.mRecyclerPool.factorInCreateTime(itemViewType, RecyclerView.this.getNanoTime() - nanoTime);
                    z = z3;
                    viewHolder = createViewHolder;
                } else {
                    z = z3;
                    viewHolder = viewHolder2;
                }
            } else {
                z = z3;
                viewHolder = viewHolder2;
            }
            if (z && !RecyclerView.this.mState.isPreLayout() && viewHolder.hasAnyOfTheFlags(8192)) {
                viewHolder.setFlags(0, 8192);
                if (RecyclerView.this.mState.mRunSimpleAnimations) {
                    RecyclerView.this.recordAnimationInfoIfBouncedHiddenView(viewHolder, RecyclerView.this.mItemAnimator.recordPreLayoutInformation(RecyclerView.this.mState, viewHolder, ItemAnimator.buildAdapterChangeFlagsForAnimations(viewHolder) | 4096, viewHolder.getUnmodifiedPayloads()));
                }
            }
            boolean z5 = false;
            if (RecyclerView.this.mState.isPreLayout() && viewHolder.isBound()) {
                viewHolder.mPreLayoutPosition = i;
            } else if (!viewHolder.isBound() || viewHolder.needsUpdate() || viewHolder.isInvalid()) {
                z5 = tryBindViewHolderByDeadline(viewHolder, RecyclerView.this.mAdapterHelper.findPositionOffset(i), position, deadlineNs);
            }
            ViewGroup.LayoutParams layoutParams2 = viewHolder.itemView.getLayoutParams();
            if (layoutParams2 == null) {
                layoutParams = (LayoutParams) RecyclerView.this.generateDefaultLayoutParams();
                viewHolder.itemView.setLayoutParams(layoutParams);
            } else if (!RecyclerView.this.checkLayoutParams(layoutParams2)) {
                layoutParams = (LayoutParams) RecyclerView.this.generateLayoutParams(layoutParams2);
                viewHolder.itemView.setLayoutParams(layoutParams);
            } else {
                layoutParams = (LayoutParams) layoutParams2;
            }
            layoutParams.mViewHolder = viewHolder;
            if (!z || !z5) {
                z4 = false;
            }
            layoutParams.mPendingInvalidate = z4;
            return viewHolder;
        }

        /* access modifiers changed from: package-private */
        public void unscrapView(ViewHolder holder) {
            if (holder.mInChangeScrap) {
                this.mChangedScrap.remove(holder);
            } else {
                this.mAttachedScrap.remove(holder);
            }
            holder.mScrapContainer = null;
            holder.mInChangeScrap = false;
            holder.clearReturnedFromScrapFlag();
        }

        /* access modifiers changed from: package-private */
        public void updateViewCacheSize() {
            this.mViewCacheMax = this.mRequestedCacheMax + (RecyclerView.this.mLayout != null ? RecyclerView.this.mLayout.mPrefetchMaxCountObserved : 0);
            for (int size = this.mCachedViews.size() - 1; size >= 0 && this.mCachedViews.size() > this.mViewCacheMax; size--) {
                recycleCachedViewAt(size);
            }
        }

        /* access modifiers changed from: package-private */
        public boolean validateViewHolderForOffsetPosition(ViewHolder holder) {
            if (holder.isRemoved()) {
                return RecyclerView.this.mState.isPreLayout();
            }
            if (holder.mPosition < 0 || holder.mPosition >= RecyclerView.this.mAdapter.getItemCount()) {
                throw new IndexOutOfBoundsException("Inconsistency detected. Invalid view holder adapter position" + holder + RecyclerView.this.exceptionLabel());
            } else if (!RecyclerView.this.mState.isPreLayout() && RecyclerView.this.mAdapter.getItemViewType(holder.mPosition) != holder.getItemViewType()) {
                return false;
            } else {
                if (RecyclerView.this.mAdapter.hasStableIds()) {
                    return holder.getItemId() == RecyclerView.this.mAdapter.getItemId(holder.mPosition);
                }
                return true;
            }
        }

        /* access modifiers changed from: package-private */
        public void viewRangeUpdate(int positionStart, int itemCount) {
            int i;
            int i2 = positionStart + itemCount;
            for (int size = this.mCachedViews.size() - 1; size >= 0; size--) {
                ViewHolder viewHolder = this.mCachedViews.get(size);
                if (viewHolder != null && (i = viewHolder.mPosition) >= positionStart && i < i2) {
                    viewHolder.addFlags(2);
                    recycleCachedViewAt(size);
                }
            }
        }
    }

    public interface RecyclerListener {
        void onViewRecycled(ViewHolder viewHolder);
    }

    private class RecyclerViewDataObserver extends AdapterDataObserver {
        RecyclerViewDataObserver() {
        }

        public void onChanged() {
            RecyclerView.this.assertNotInLayoutOrScroll((String) null);
            RecyclerView.this.mState.mStructureChanged = true;
            RecyclerView.this.processDataSetCompletelyChanged(true);
            if (!RecyclerView.this.mAdapterHelper.hasPendingUpdates()) {
                RecyclerView.this.requestLayout();
            }
        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            RecyclerView.this.assertNotInLayoutOrScroll((String) null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeChanged(positionStart, itemCount, payload)) {
                triggerUpdateProcessor();
            }
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            RecyclerView.this.assertNotInLayoutOrScroll((String) null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeInserted(positionStart, itemCount)) {
                triggerUpdateProcessor();
            }
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            RecyclerView.this.assertNotInLayoutOrScroll((String) null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeMoved(fromPosition, toPosition, itemCount)) {
                triggerUpdateProcessor();
            }
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            RecyclerView.this.assertNotInLayoutOrScroll((String) null);
            if (RecyclerView.this.mAdapterHelper.onItemRangeRemoved(positionStart, itemCount)) {
                triggerUpdateProcessor();
            }
        }

        /* access modifiers changed from: package-private */
        public void triggerUpdateProcessor() {
            if (!RecyclerView.POST_UPDATES_ON_ANIMATION || !RecyclerView.this.mHasFixedSize || !RecyclerView.this.mIsAttached) {
                RecyclerView.this.mAdapterUpdateDuringMeasure = true;
                RecyclerView.this.requestLayout();
                return;
            }
            RecyclerView recyclerView = RecyclerView.this;
            ViewCompat.postOnAnimation(recyclerView, recyclerView.mUpdateChildViewsRunnable);
        }
    }

    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, (ClassLoader) null);
            }

            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        Parcelable mLayoutState;

        SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            this.mLayoutState = in.readParcelable(loader != null ? loader : LayoutManager.class.getClassLoader());
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        /* access modifiers changed from: package-private */
        public void copyFrom(SavedState other) {
            this.mLayoutState = other.mLayoutState;
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.mLayoutState, 0);
        }
    }

    public static class SimpleOnItemTouchListener implements OnItemTouchListener {
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            return false;
        }

        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }

        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
    }

    public static abstract class SmoothScroller {
        private LayoutManager mLayoutManager;
        private boolean mPendingInitialRun;
        private RecyclerView mRecyclerView;
        private final Action mRecyclingAction = new Action(0, 0);
        private boolean mRunning;
        private boolean mStarted;
        private int mTargetPosition = -1;
        private View mTargetView;

        public static class Action {
            public static final int UNDEFINED_DURATION = Integer.MIN_VALUE;
            private boolean mChanged;
            private int mConsecutiveUpdates;
            private int mDuration;
            private int mDx;
            private int mDy;
            private Interpolator mInterpolator;
            private int mJumpToPosition;

            public Action(int dx, int dy) {
                this(dx, dy, Integer.MIN_VALUE, (Interpolator) null);
            }

            public Action(int dx, int dy, int duration) {
                this(dx, dy, duration, (Interpolator) null);
            }

            public Action(int dx, int dy, int duration, Interpolator interpolator) {
                this.mJumpToPosition = -1;
                this.mChanged = false;
                this.mConsecutiveUpdates = 0;
                this.mDx = dx;
                this.mDy = dy;
                this.mDuration = duration;
                this.mInterpolator = interpolator;
            }

            private void validate() {
                if (this.mInterpolator != null && this.mDuration < 1) {
                    throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
                } else if (this.mDuration < 1) {
                    throw new IllegalStateException("Scroll duration must be a positive number");
                }
            }

            public int getDuration() {
                return this.mDuration;
            }

            public int getDx() {
                return this.mDx;
            }

            public int getDy() {
                return this.mDy;
            }

            public Interpolator getInterpolator() {
                return this.mInterpolator;
            }

            /* access modifiers changed from: package-private */
            public boolean hasJumpTarget() {
                return this.mJumpToPosition >= 0;
            }

            public void jumpTo(int targetPosition) {
                this.mJumpToPosition = targetPosition;
            }

            /* access modifiers changed from: package-private */
            public void runIfNecessary(RecyclerView recyclerView) {
                if (this.mJumpToPosition >= 0) {
                    int i = this.mJumpToPosition;
                    this.mJumpToPosition = -1;
                    recyclerView.jumpToPositionForSmoothScroller(i);
                    this.mChanged = false;
                } else if (this.mChanged) {
                    validate();
                    recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
                    int i2 = this.mConsecutiveUpdates + 1;
                    this.mConsecutiveUpdates = i2;
                    if (i2 > 10) {
                        Log.e(RecyclerView.TAG, "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary");
                    }
                    this.mChanged = false;
                } else {
                    this.mConsecutiveUpdates = 0;
                }
            }

            public void setDuration(int duration) {
                this.mChanged = true;
                this.mDuration = duration;
            }

            public void setDx(int dx) {
                this.mChanged = true;
                this.mDx = dx;
            }

            public void setDy(int dy) {
                this.mChanged = true;
                this.mDy = dy;
            }

            public void setInterpolator(Interpolator interpolator) {
                this.mChanged = true;
                this.mInterpolator = interpolator;
            }

            public void update(int dx, int dy, int duration, Interpolator interpolator) {
                this.mDx = dx;
                this.mDy = dy;
                this.mDuration = duration;
                this.mInterpolator = interpolator;
                this.mChanged = true;
            }
        }

        public interface ScrollVectorProvider {
            PointF computeScrollVectorForPosition(int i);
        }

        public PointF computeScrollVectorForPosition(int targetPosition) {
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager instanceof ScrollVectorProvider) {
                return ((ScrollVectorProvider) layoutManager).computeScrollVectorForPosition(targetPosition);
            }
            Log.w(RecyclerView.TAG, "You should override computeScrollVectorForPosition when the LayoutManager does not implement " + ScrollVectorProvider.class.getCanonicalName());
            return null;
        }

        public View findViewByPosition(int position) {
            return this.mRecyclerView.mLayout.findViewByPosition(position);
        }

        public int getChildCount() {
            return this.mRecyclerView.mLayout.getChildCount();
        }

        public int getChildPosition(View view) {
            return this.mRecyclerView.getChildLayoutPosition(view);
        }

        public LayoutManager getLayoutManager() {
            return this.mLayoutManager;
        }

        public int getTargetPosition() {
            return this.mTargetPosition;
        }

        @Deprecated
        public void instantScrollToPosition(int position) {
            this.mRecyclerView.scrollToPosition(position);
        }

        public boolean isPendingInitialRun() {
            return this.mPendingInitialRun;
        }

        public boolean isRunning() {
            return this.mRunning;
        }

        /* access modifiers changed from: protected */
        public void normalize(PointF scrollVector) {
            float sqrt = (float) Math.sqrt((double) ((scrollVector.x * scrollVector.x) + (scrollVector.y * scrollVector.y)));
            scrollVector.x /= sqrt;
            scrollVector.y /= sqrt;
        }

        /* access modifiers changed from: package-private */
        public void onAnimation(int dx, int dy) {
            PointF computeScrollVectorForPosition;
            RecyclerView recyclerView = this.mRecyclerView;
            if (this.mTargetPosition == -1 || recyclerView == null) {
                stop();
            }
            if (!(!this.mPendingInitialRun || this.mTargetView != null || this.mLayoutManager == null || (computeScrollVectorForPosition = computeScrollVectorForPosition(this.mTargetPosition)) == null || (computeScrollVectorForPosition.x == 0.0f && computeScrollVectorForPosition.y == 0.0f))) {
                recyclerView.scrollStep((int) Math.signum(computeScrollVectorForPosition.x), (int) Math.signum(computeScrollVectorForPosition.y), (int[]) null);
            }
            this.mPendingInitialRun = false;
            View view = this.mTargetView;
            if (view != null) {
                if (getChildPosition(view) == this.mTargetPosition) {
                    onTargetFound(this.mTargetView, recyclerView.mState, this.mRecyclingAction);
                    this.mRecyclingAction.runIfNecessary(recyclerView);
                    stop();
                } else {
                    Log.e(RecyclerView.TAG, "Passed over target position while smooth scrolling.");
                    this.mTargetView = null;
                }
            }
            if (this.mRunning) {
                onSeekTargetStep(dx, dy, recyclerView.mState, this.mRecyclingAction);
                boolean hasJumpTarget = this.mRecyclingAction.hasJumpTarget();
                this.mRecyclingAction.runIfNecessary(recyclerView);
                if (hasJumpTarget && this.mRunning) {
                    this.mPendingInitialRun = true;
                    recyclerView.mViewFlinger.postOnAnimation();
                }
            }
        }

        /* access modifiers changed from: protected */
        public void onChildAttachedToWindow(View child) {
            if (getChildPosition(child) == getTargetPosition()) {
                this.mTargetView = child;
            }
        }

        /* access modifiers changed from: protected */
        public abstract void onSeekTargetStep(int i, int i2, State state, Action action);

        /* access modifiers changed from: protected */
        public abstract void onStart();

        /* access modifiers changed from: protected */
        public abstract void onStop();

        /* access modifiers changed from: protected */
        public abstract void onTargetFound(View view, State state, Action action);

        public void setTargetPosition(int targetPosition) {
            this.mTargetPosition = targetPosition;
        }

        /* access modifiers changed from: package-private */
        public void start(RecyclerView recyclerView, LayoutManager layoutManager) {
            recyclerView.mViewFlinger.stop();
            if (this.mStarted) {
                Log.w(RecyclerView.TAG, "An instance of " + getClass().getSimpleName() + " was started more than once. Each instance of" + getClass().getSimpleName() + " is intended to only be used once. You should create a new instance for each use.");
            }
            this.mRecyclerView = recyclerView;
            this.mLayoutManager = layoutManager;
            if (this.mTargetPosition != -1) {
                recyclerView.mState.mTargetPosition = this.mTargetPosition;
                this.mRunning = true;
                this.mPendingInitialRun = true;
                this.mTargetView = findViewByPosition(getTargetPosition());
                onStart();
                this.mRecyclerView.mViewFlinger.postOnAnimation();
                this.mStarted = true;
                return;
            }
            throw new IllegalArgumentException("Invalid target position");
        }

        /* access modifiers changed from: protected */
        public final void stop() {
            if (this.mRunning) {
                this.mRunning = false;
                onStop();
                this.mRecyclerView.mState.mTargetPosition = -1;
                this.mTargetView = null;
                this.mTargetPosition = -1;
                this.mPendingInitialRun = false;
                this.mLayoutManager.onSmoothScrollerStopped(this);
                this.mLayoutManager = null;
                this.mRecyclerView = null;
            }
        }
    }

    /* compiled from: 008D */
    public static class State {
        static final int STEP_ANIMATIONS = 4;
        static final int STEP_LAYOUT = 2;
        static final int STEP_START = 1;
        private SparseArray<Object> mData;
        int mDeletedInvisibleItemCountSincePreviousLayout = 0;
        long mFocusedItemId;
        int mFocusedItemPosition;
        int mFocusedSubChildId;
        boolean mInPreLayout = false;
        boolean mIsMeasuring = false;
        int mItemCount = 0;
        int mLayoutStep = 1;
        int mPreviousLayoutItemCount = 0;
        int mRemainingScrollHorizontal;
        int mRemainingScrollVertical;
        boolean mRunPredictiveAnimations = false;
        boolean mRunSimpleAnimations = false;
        boolean mStructureChanged = false;
        int mTargetPosition = -1;
        boolean mTrackOldChangeHolders = false;

        /* access modifiers changed from: package-private */
        public void assertLayoutStep(int accepted) {
            if ((this.mLayoutStep & accepted) == 0) {
                StringBuilder append = new StringBuilder().append("Layout state should be one of ");
                String binaryString = Integer.toBinaryString(accepted);
                Log1F380D.a((Object) binaryString);
                StringBuilder append2 = append.append(binaryString).append(" but it is ");
                String binaryString2 = Integer.toBinaryString(this.mLayoutStep);
                Log1F380D.a((Object) binaryString2);
                throw new IllegalStateException(append2.append(binaryString2).toString());
            }
        }

        public boolean didStructureChange() {
            return this.mStructureChanged;
        }

        public <T> T get(int resourceId) {
            SparseArray<Object> sparseArray = this.mData;
            if (sparseArray == null) {
                return null;
            }
            return sparseArray.get(resourceId);
        }

        public int getItemCount() {
            return this.mInPreLayout ? this.mPreviousLayoutItemCount - this.mDeletedInvisibleItemCountSincePreviousLayout : this.mItemCount;
        }

        public int getRemainingScrollHorizontal() {
            return this.mRemainingScrollHorizontal;
        }

        public int getRemainingScrollVertical() {
            return this.mRemainingScrollVertical;
        }

        public int getTargetScrollPosition() {
            return this.mTargetPosition;
        }

        public boolean hasTargetScrollPosition() {
            return this.mTargetPosition != -1;
        }

        public boolean isMeasuring() {
            return this.mIsMeasuring;
        }

        public boolean isPreLayout() {
            return this.mInPreLayout;
        }

        /* access modifiers changed from: package-private */
        public void prepareForNestedPrefetch(Adapter adapter) {
            this.mLayoutStep = 1;
            this.mItemCount = adapter.getItemCount();
            this.mInPreLayout = false;
            this.mTrackOldChangeHolders = false;
            this.mIsMeasuring = false;
        }

        public void put(int resourceId, Object data) {
            if (this.mData == null) {
                this.mData = new SparseArray<>();
            }
            this.mData.put(resourceId, data);
        }

        public void remove(int resourceId) {
            SparseArray<Object> sparseArray = this.mData;
            if (sparseArray != null) {
                sparseArray.remove(resourceId);
            }
        }

        public String toString() {
            return "State{mTargetPosition=" + this.mTargetPosition + ", mData=" + this.mData + ", mItemCount=" + this.mItemCount + ", mIsMeasuring=" + this.mIsMeasuring + ", mPreviousLayoutItemCount=" + this.mPreviousLayoutItemCount + ", mDeletedInvisibleItemCountSincePreviousLayout=" + this.mDeletedInvisibleItemCountSincePreviousLayout + ", mStructureChanged=" + this.mStructureChanged + ", mInPreLayout=" + this.mInPreLayout + ", mRunSimpleAnimations=" + this.mRunSimpleAnimations + ", mRunPredictiveAnimations=" + this.mRunPredictiveAnimations + '}';
        }

        public boolean willRunPredictiveAnimations() {
            return this.mRunPredictiveAnimations;
        }

        public boolean willRunSimpleAnimations() {
            return this.mRunSimpleAnimations;
        }
    }

    public static abstract class ViewCacheExtension {
        public abstract View getViewForPositionAndType(Recycler recycler, int i, int i2);
    }

    class ViewFlinger implements Runnable {
        private boolean mEatRunOnAnimationRequest = false;
        Interpolator mInterpolator = RecyclerView.sQuinticInterpolator;
        private int mLastFlingX;
        private int mLastFlingY;
        OverScroller mOverScroller;
        private boolean mReSchedulePostAnimationCallback = false;

        ViewFlinger() {
            this.mOverScroller = new OverScroller(RecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
        }

        private int computeScrollDuration(int dx, int dy, int vx, int vy) {
            int i;
            int abs = Math.abs(dx);
            int abs2 = Math.abs(dy);
            boolean z = abs > abs2;
            int sqrt = (int) Math.sqrt((double) ((vx * vx) + (vy * vy)));
            int sqrt2 = (int) Math.sqrt((double) ((dx * dx) + (dy * dy)));
            RecyclerView recyclerView = RecyclerView.this;
            int width = z ? recyclerView.getWidth() : recyclerView.getHeight();
            int i2 = width / 2;
            float distanceInfluenceForSnapDuration = ((float) i2) + (((float) i2) * distanceInfluenceForSnapDuration(Math.min(1.0f, (((float) sqrt2) * 1.0f) / ((float) width))));
            if (sqrt > 0) {
                i = Math.round(Math.abs(distanceInfluenceForSnapDuration / ((float) sqrt)) * 1000.0f) * 4;
            } else {
                i = (int) (((((float) (z ? abs : abs2)) / ((float) width)) + 1.0f) * 300.0f);
            }
            return Math.min(i, RecyclerView.MAX_SCROLL_DURATION);
        }

        private float distanceInfluenceForSnapDuration(float f) {
            return (float) Math.sin((double) ((f - 0.5f) * 0.47123894f));
        }

        private void internalPostOnAnimation() {
            RecyclerView.this.removeCallbacks(this);
            ViewCompat.postOnAnimation(RecyclerView.this, this);
        }

        public void fling(int velocityX, int velocityY) {
            RecyclerView.this.setScrollState(2);
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            if (this.mInterpolator != RecyclerView.sQuinticInterpolator) {
                this.mInterpolator = RecyclerView.sQuinticInterpolator;
                this.mOverScroller = new OverScroller(RecyclerView.this.getContext(), RecyclerView.sQuinticInterpolator);
            }
            this.mOverScroller.fling(0, 0, velocityX, velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation();
        }

        /* access modifiers changed from: package-private */
        public void postOnAnimation() {
            if (this.mEatRunOnAnimationRequest) {
                this.mReSchedulePostAnimationCallback = true;
            } else {
                internalPostOnAnimation();
            }
        }

        public void run() {
            boolean z;
            if (RecyclerView.this.mLayout == null) {
                stop();
                return;
            }
            this.mReSchedulePostAnimationCallback = false;
            this.mEatRunOnAnimationRequest = true;
            RecyclerView.this.consumePendingUpdateOperations();
            OverScroller overScroller = this.mOverScroller;
            if (overScroller.computeScrollOffset()) {
                int currX = overScroller.getCurrX();
                int currY = overScroller.getCurrY();
                int i = currX - this.mLastFlingX;
                int i2 = currY - this.mLastFlingY;
                this.mLastFlingX = currX;
                this.mLastFlingY = currY;
                int i3 = 0;
                int i4 = 0;
                RecyclerView.this.mReusableIntPair[0] = 0;
                RecyclerView.this.mReusableIntPair[1] = 0;
                RecyclerView recyclerView = RecyclerView.this;
                if (recyclerView.dispatchNestedPreScroll(i, i2, recyclerView.mReusableIntPair, (int[]) null, 1)) {
                    i -= RecyclerView.this.mReusableIntPair[0];
                    i2 -= RecyclerView.this.mReusableIntPair[1];
                }
                if (RecyclerView.this.getOverScrollMode() != 2) {
                    RecyclerView.this.considerReleasingGlowsOnScroll(i, i2);
                }
                if (RecyclerView.this.mAdapter != null) {
                    RecyclerView.this.mReusableIntPair[0] = 0;
                    RecyclerView.this.mReusableIntPair[1] = 0;
                    RecyclerView recyclerView2 = RecyclerView.this;
                    recyclerView2.scrollStep(i, i2, recyclerView2.mReusableIntPair);
                    i3 = RecyclerView.this.mReusableIntPair[0];
                    i4 = RecyclerView.this.mReusableIntPair[1];
                    i -= i3;
                    i2 -= i4;
                    SmoothScroller smoothScroller = RecyclerView.this.mLayout.mSmoothScroller;
                    if (smoothScroller != null && !smoothScroller.isPendingInitialRun() && smoothScroller.isRunning()) {
                        int itemCount = RecyclerView.this.mState.getItemCount();
                        if (itemCount == 0) {
                            smoothScroller.stop();
                        } else if (smoothScroller.getTargetPosition() >= itemCount) {
                            smoothScroller.setTargetPosition(itemCount - 1);
                            smoothScroller.onAnimation(i3, i4);
                        } else {
                            smoothScroller.onAnimation(i3, i4);
                        }
                    }
                }
                if (!RecyclerView.this.mItemDecorations.isEmpty()) {
                    RecyclerView.this.invalidate();
                }
                RecyclerView.this.mReusableIntPair[0] = 0;
                RecyclerView.this.mReusableIntPair[1] = 0;
                RecyclerView recyclerView3 = RecyclerView.this;
                recyclerView3.dispatchNestedScroll(i3, i4, i, i2, (int[]) null, 1, recyclerView3.mReusableIntPair);
                int i5 = i - RecyclerView.this.mReusableIntPair[0];
                int i6 = i2 - RecyclerView.this.mReusableIntPair[1];
                if (!(i3 == 0 && i4 == 0)) {
                    RecyclerView.this.dispatchOnScrolled(i3, i4);
                }
                if (!RecyclerView.this.awakenScrollBars()) {
                    RecyclerView.this.invalidate();
                }
                boolean z2 = overScroller.isFinished() || (((overScroller.getCurrX() == overScroller.getFinalX()) || i5 != 0) && ((overScroller.getCurrY() == overScroller.getFinalY()) || i6 != 0));
                SmoothScroller smoothScroller2 = RecyclerView.this.mLayout.mSmoothScroller;
                if ((smoothScroller2 != null && smoothScroller2.isPendingInitialRun()) || !z2) {
                    postOnAnimation();
                    if (RecyclerView.this.mGapWorker != null) {
                        RecyclerView.this.mGapWorker.postFromTraversal(RecyclerView.this, i3, i4);
                    }
                } else {
                    if (RecyclerView.this.getOverScrollMode() != 2) {
                        int currVelocity = (int) overScroller.getCurrVelocity();
                        int i7 = currVelocity;
                        RecyclerView.this.absorbGlows(i5 < 0 ? -currVelocity : i5 > 0 ? currVelocity : 0, i6 < 0 ? -currVelocity : i6 > 0 ? currVelocity : 0);
                    }
                    if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                        RecyclerView.this.mPrefetchRegistry.clearPrefetchPositions();
                    }
                }
            }
            SmoothScroller smoothScroller3 = RecyclerView.this.mLayout.mSmoothScroller;
            if (smoothScroller3 == null || !smoothScroller3.isPendingInitialRun()) {
                z = false;
            } else {
                z = false;
                smoothScroller3.onAnimation(0, 0);
            }
            this.mEatRunOnAnimationRequest = z;
            if (this.mReSchedulePostAnimationCallback) {
                internalPostOnAnimation();
                return;
            }
            RecyclerView.this.setScrollState(z ? 1 : 0);
            RecyclerView.this.stopNestedScroll(1);
        }

        public void smoothScrollBy(int dx, int dy, int duration, Interpolator interpolator) {
            if (duration == Integer.MIN_VALUE) {
                duration = computeScrollDuration(dx, dy, 0, 0);
            }
            if (interpolator == null) {
                interpolator = RecyclerView.sQuinticInterpolator;
            }
            if (this.mInterpolator != interpolator) {
                this.mInterpolator = interpolator;
                this.mOverScroller = new OverScroller(RecyclerView.this.getContext(), interpolator);
            }
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            RecyclerView.this.setScrollState(2);
            this.mOverScroller.startScroll(0, 0, dx, dy, duration);
            if (Build.VERSION.SDK_INT < 23) {
                this.mOverScroller.computeScrollOffset();
            }
            postOnAnimation();
        }

        public void stop() {
            RecyclerView.this.removeCallbacks(this);
            this.mOverScroller.abortAnimation();
        }
    }

    /* compiled from: 008E */
    public static abstract class ViewHolder {
        static final int FLAG_ADAPTER_FULLUPDATE = 1024;
        static final int FLAG_ADAPTER_POSITION_UNKNOWN = 512;
        static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
        static final int FLAG_BOUNCED_FROM_HIDDEN_LIST = 8192;
        static final int FLAG_BOUND = 1;
        static final int FLAG_IGNORE = 128;
        static final int FLAG_INVALID = 4;
        static final int FLAG_MOVED = 2048;
        static final int FLAG_NOT_RECYCLABLE = 16;
        static final int FLAG_REMOVED = 8;
        static final int FLAG_RETURNED_FROM_SCRAP = 32;
        static final int FLAG_TMP_DETACHED = 256;
        static final int FLAG_UPDATE = 2;
        private static final List<Object> FULLUPDATE_PAYLOADS = Collections.emptyList();
        static final int PENDING_ACCESSIBILITY_STATE_NOT_SET = -1;
        public final View itemView;
        int mFlags;
        boolean mInChangeScrap = false;
        private int mIsRecyclableCount = 0;
        long mItemId = -1;
        int mItemViewType = -1;
        WeakReference<RecyclerView> mNestedRecyclerView;
        int mOldPosition = -1;
        RecyclerView mOwnerRecyclerView;
        List<Object> mPayloads = null;
        int mPendingAccessibilityState = -1;
        int mPosition = -1;
        int mPreLayoutPosition = -1;
        Recycler mScrapContainer = null;
        ViewHolder mShadowedHolder = null;
        ViewHolder mShadowingHolder = null;
        List<Object> mUnmodifiedPayloads = null;
        private int mWasImportantForAccessibilityBeforeHidden = 0;

        public ViewHolder(View itemView2) {
            if (itemView2 != null) {
                this.itemView = itemView2;
                return;
            }
            throw new IllegalArgumentException("itemView may not be null");
        }

        private void createPayloadsIfNeeded() {
            if (this.mPayloads == null) {
                ArrayList arrayList = new ArrayList();
                this.mPayloads = arrayList;
                this.mUnmodifiedPayloads = Collections.unmodifiableList(arrayList);
            }
        }

        /* access modifiers changed from: package-private */
        public void addChangePayload(Object payload) {
            if (payload == null) {
                addFlags(1024);
            } else if ((1024 & this.mFlags) == 0) {
                createPayloadsIfNeeded();
                this.mPayloads.add(payload);
            }
        }

        /* access modifiers changed from: package-private */
        public void addFlags(int flags) {
            this.mFlags |= flags;
        }

        /* access modifiers changed from: package-private */
        public void clearOldPosition() {
            this.mOldPosition = -1;
            this.mPreLayoutPosition = -1;
        }

        /* access modifiers changed from: package-private */
        public void clearPayload() {
            List<Object> list = this.mPayloads;
            if (list != null) {
                list.clear();
            }
            this.mFlags &= -1025;
        }

        /* access modifiers changed from: package-private */
        public void clearReturnedFromScrapFlag() {
            this.mFlags &= -33;
        }

        /* access modifiers changed from: package-private */
        public void clearTmpDetachFlag() {
            this.mFlags &= -257;
        }

        /* access modifiers changed from: package-private */
        public boolean doesTransientStatePreventRecycling() {
            return (this.mFlags & 16) == 0 && ViewCompat.hasTransientState(this.itemView);
        }

        /* access modifiers changed from: package-private */
        public void flagRemovedAndOffsetPosition(int mNewPosition, int offset, boolean applyToPreLayout) {
            addFlags(8);
            offsetPosition(offset, applyToPreLayout);
            this.mPosition = mNewPosition;
        }

        public final int getAdapterPosition() {
            RecyclerView recyclerView = this.mOwnerRecyclerView;
            if (recyclerView == null) {
                return -1;
            }
            return recyclerView.getAdapterPositionFor(this);
        }

        public final long getItemId() {
            return this.mItemId;
        }

        public final int getItemViewType() {
            return this.mItemViewType;
        }

        public final int getLayoutPosition() {
            int i = this.mPreLayoutPosition;
            return i == -1 ? this.mPosition : i;
        }

        public final int getOldPosition() {
            return this.mOldPosition;
        }

        @Deprecated
        public final int getPosition() {
            int i = this.mPreLayoutPosition;
            return i == -1 ? this.mPosition : i;
        }

        /* access modifiers changed from: package-private */
        public List<Object> getUnmodifiedPayloads() {
            if ((this.mFlags & 1024) != 0) {
                return FULLUPDATE_PAYLOADS;
            }
            List<Object> list = this.mPayloads;
            return (list == null || list.size() == 0) ? FULLUPDATE_PAYLOADS : this.mUnmodifiedPayloads;
        }

        /* access modifiers changed from: package-private */
        public boolean hasAnyOfTheFlags(int flags) {
            return (this.mFlags & flags) != 0;
        }

        /* access modifiers changed from: package-private */
        public boolean isAdapterPositionUnknown() {
            return (this.mFlags & 512) != 0 || isInvalid();
        }

        /* access modifiers changed from: package-private */
        public boolean isAttachedToTransitionOverlay() {
            return (this.itemView.getParent() == null || this.itemView.getParent() == this.mOwnerRecyclerView) ? false : true;
        }

        /* access modifiers changed from: package-private */
        public boolean isBound() {
            return (this.mFlags & 1) != 0;
        }

        /* access modifiers changed from: package-private */
        public boolean isInvalid() {
            return (this.mFlags & 4) != 0;
        }

        public final boolean isRecyclable() {
            return (this.mFlags & 16) == 0 && !ViewCompat.hasTransientState(this.itemView);
        }

        /* access modifiers changed from: package-private */
        public boolean isRemoved() {
            return (this.mFlags & 8) != 0;
        }

        /* access modifiers changed from: package-private */
        public boolean isScrap() {
            return this.mScrapContainer != null;
        }

        /* access modifiers changed from: package-private */
        public boolean isTmpDetached() {
            return (this.mFlags & 256) != 0;
        }

        /* access modifiers changed from: package-private */
        public boolean isUpdated() {
            return (this.mFlags & 2) != 0;
        }

        /* access modifiers changed from: package-private */
        public boolean needsUpdate() {
            return (this.mFlags & 2) != 0;
        }

        /* access modifiers changed from: package-private */
        public void offsetPosition(int offset, boolean applyToPreLayout) {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
            }
            if (this.mPreLayoutPosition == -1) {
                this.mPreLayoutPosition = this.mPosition;
            }
            if (applyToPreLayout) {
                this.mPreLayoutPosition += offset;
            }
            this.mPosition += offset;
            if (this.itemView.getLayoutParams() != null) {
                ((LayoutParams) this.itemView.getLayoutParams()).mInsetsDirty = true;
            }
        }

        /* access modifiers changed from: package-private */
        public void onEnteredHiddenState(RecyclerView parent) {
            int i = this.mPendingAccessibilityState;
            if (i != -1) {
                this.mWasImportantForAccessibilityBeforeHidden = i;
            } else {
                this.mWasImportantForAccessibilityBeforeHidden = ViewCompat.getImportantForAccessibility(this.itemView);
            }
            parent.setChildImportantForAccessibilityInternal(this, 4);
        }

        /* access modifiers changed from: package-private */
        public void onLeftHiddenState(RecyclerView parent) {
            parent.setChildImportantForAccessibilityInternal(this, this.mWasImportantForAccessibilityBeforeHidden);
            this.mWasImportantForAccessibilityBeforeHidden = 0;
        }

        /* access modifiers changed from: package-private */
        public void resetInternal() {
            this.mFlags = 0;
            this.mPosition = -1;
            this.mOldPosition = -1;
            this.mItemId = -1;
            this.mPreLayoutPosition = -1;
            this.mIsRecyclableCount = 0;
            this.mShadowedHolder = null;
            this.mShadowingHolder = null;
            clearPayload();
            this.mWasImportantForAccessibilityBeforeHidden = 0;
            this.mPendingAccessibilityState = -1;
            RecyclerView.clearNestedRecyclerViewIfNotNested(this);
        }

        /* access modifiers changed from: package-private */
        public void saveOldPosition() {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
            }
        }

        /* access modifiers changed from: package-private */
        public void setFlags(int flags, int mask) {
            this.mFlags = (this.mFlags & (~mask)) | (flags & mask);
        }

        public final void setIsRecyclable(boolean recyclable) {
            int i = this.mIsRecyclableCount;
            int i2 = recyclable ? i - 1 : i + 1;
            this.mIsRecyclableCount = i2;
            if (i2 < 0) {
                this.mIsRecyclableCount = 0;
                Log.e("View", "isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for " + this);
            } else if (!recyclable && i2 == 1) {
                this.mFlags |= 16;
            } else if (recyclable && i2 == 0) {
                this.mFlags &= -17;
            }
        }

        /* access modifiers changed from: package-private */
        public void setScrapContainer(Recycler recycler, boolean isChangeScrap) {
            this.mScrapContainer = recycler;
            this.mInChangeScrap = isChangeScrap;
        }

        /* access modifiers changed from: package-private */
        public boolean shouldBeKeptAsChild() {
            return (this.mFlags & 16) != 0;
        }

        /* access modifiers changed from: package-private */
        public boolean shouldIgnore() {
            return (this.mFlags & 128) != 0;
        }

        /* access modifiers changed from: package-private */
        public void stopIgnoring() {
            this.mFlags &= -129;
        }

        public String toString() {
            StringBuilder append = new StringBuilder().append(getClass().isAnonymousClass() ? "ViewHolder" : getClass().getSimpleName()).append("{");
            String hexString = Integer.toHexString(hashCode());
            Log1F380D.a((Object) hexString);
            StringBuilder sb = new StringBuilder(append.append(hexString).append(" position=").append(this.mPosition).append(" id=").append(this.mItemId).append(", oldPos=").append(this.mOldPosition).append(", pLpos:").append(this.mPreLayoutPosition).toString());
            if (isScrap()) {
                sb.append(" scrap ").append(this.mInChangeScrap ? "[changeScrap]" : "[attachedScrap]");
            }
            if (isInvalid()) {
                sb.append(" invalid");
            }
            if (!isBound()) {
                sb.append(" unbound");
            }
            if (needsUpdate()) {
                sb.append(" update");
            }
            if (isRemoved()) {
                sb.append(" removed");
            }
            if (shouldIgnore()) {
                sb.append(" ignored");
            }
            if (isTmpDetached()) {
                sb.append(" tmpDetached");
            }
            if (!isRecyclable()) {
                sb.append(" not recyclable(" + this.mIsRecyclableCount + ")");
            }
            if (isAdapterPositionUnknown()) {
                sb.append(" undefined adapter position");
            }
            if (this.itemView.getParent() == null) {
                sb.append(" no parent");
            }
            sb.append("}");
            return sb.toString();
        }

        /* access modifiers changed from: package-private */
        public void unScrap() {
            this.mScrapContainer.unscrapView(this);
        }

        /* access modifiers changed from: package-private */
        public boolean wasReturnedFromScrap() {
            return (this.mFlags & 32) != 0;
        }
    }

    public RecyclerView(Context context) {
        this(context, (AttributeSet) null);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.recyclerViewStyle);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public RecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Context context2 = context;
        AttributeSet attributeSet = attrs;
        int i = defStyleAttr;
        this.mObserver = new RecyclerViewDataObserver();
        this.mRecycler = new Recycler();
        this.mViewInfoStore = new ViewInfoStore();
        this.mUpdateChildViewsRunnable = new Runnable() {
            public void run() {
                if (RecyclerView.this.mFirstLayoutComplete && !RecyclerView.this.isLayoutRequested()) {
                    if (!RecyclerView.this.mIsAttached) {
                        RecyclerView.this.requestLayout();
                    } else if (RecyclerView.this.mLayoutSuppressed) {
                        RecyclerView.this.mLayoutWasDefered = true;
                    } else {
                        RecyclerView.this.consumePendingUpdateOperations();
                    }
                }
            }
        };
        this.mTempRect = new Rect();
        this.mTempRect2 = new Rect();
        this.mTempRectF = new RectF();
        this.mItemDecorations = new ArrayList<>();
        this.mOnItemTouchListeners = new ArrayList<>();
        this.mInterceptRequestLayoutDepth = 0;
        this.mDataSetHasChangedAfterLayout = false;
        this.mDispatchItemsChangedEvent = false;
        this.mLayoutOrScrollCounter = 0;
        this.mDispatchScrollCounter = 0;
        this.mEdgeEffectFactory = new EdgeEffectFactory();
        this.mItemAnimator = new DefaultItemAnimator();
        this.mScrollState = 0;
        this.mScrollPointerId = -1;
        this.mScaledHorizontalScrollFactor = Float.MIN_VALUE;
        this.mScaledVerticalScrollFactor = Float.MIN_VALUE;
        this.mPreserveFocusAfterLayout = true;
        this.mViewFlinger = new ViewFlinger();
        this.mPrefetchRegistry = ALLOW_THREAD_GAP_WORK ? new GapWorker.LayoutPrefetchRegistryImpl() : null;
        this.mState = new State();
        this.mItemsAddedOrRemoved = false;
        this.mItemsChanged = false;
        this.mItemAnimatorListener = new ItemAnimatorRestoreListener();
        this.mPostedAnimatorRunner = false;
        this.mMinMaxLayoutPositions = new int[2];
        this.mScrollOffset = new int[2];
        this.mNestedOffsets = new int[2];
        this.mReusableIntPair = new int[2];
        this.mPendingAccessibilityImportanceChange = new ArrayList();
        this.mItemAnimatorRunner = new Runnable() {
            public void run() {
                if (RecyclerView.this.mItemAnimator != null) {
                    RecyclerView.this.mItemAnimator.runPendingAnimations();
                }
                RecyclerView.this.mPostedAnimatorRunner = false;
            }
        };
        this.mViewInfoProcessCallback = new ViewInfoStore.ProcessCallback() {
            public void processAppeared(ViewHolder viewHolder, ItemAnimator.ItemHolderInfo preInfo, ItemAnimator.ItemHolderInfo info) {
                RecyclerView.this.animateAppearance(viewHolder, preInfo, info);
            }

            public void processDisappeared(ViewHolder viewHolder, ItemAnimator.ItemHolderInfo info, ItemAnimator.ItemHolderInfo postInfo) {
                RecyclerView.this.mRecycler.unscrapView(viewHolder);
                RecyclerView.this.animateDisappearance(viewHolder, info, postInfo);
            }

            public void processPersistent(ViewHolder viewHolder, ItemAnimator.ItemHolderInfo preInfo, ItemAnimator.ItemHolderInfo postInfo) {
                viewHolder.setIsRecyclable(false);
                if (RecyclerView.this.mDataSetHasChangedAfterLayout) {
                    if (RecyclerView.this.mItemAnimator.animateChange(viewHolder, viewHolder, preInfo, postInfo)) {
                        RecyclerView.this.postAnimationRunner();
                    }
                } else if (RecyclerView.this.mItemAnimator.animatePersistence(viewHolder, preInfo, postInfo)) {
                    RecyclerView.this.postAnimationRunner();
                }
            }

            public void unused(ViewHolder viewHolder) {
                RecyclerView.this.mLayout.removeAndRecycleView(viewHolder.itemView, RecyclerView.this.mRecycler);
            }
        };
        setScrollContainer(true);
        setFocusableInTouchMode(true);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mScaledHorizontalScrollFactor = ViewConfigurationCompat.getScaledHorizontalScrollFactor(viewConfiguration, context2);
        this.mScaledVerticalScrollFactor = ViewConfigurationCompat.getScaledVerticalScrollFactor(viewConfiguration, context2);
        this.mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaxFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        setWillNotDraw(getOverScrollMode() == 2);
        this.mItemAnimator.setListener(this.mItemAnimatorListener);
        initAdapterManager();
        initChildrenHelper();
        initAutofill();
        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, 1);
        }
        this.mAccessibilityManager = (AccessibilityManager) getContext().getSystemService("accessibility");
        setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this));
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, R.styleable.RecyclerView, i, 0);
        if (Build.VERSION.SDK_INT >= 29) {
            saveAttributeDataForStyleable(context, R.styleable.RecyclerView, attrs, obtainStyledAttributes, defStyleAttr, 0);
        }
        String string = obtainStyledAttributes.getString(R.styleable.RecyclerView_layoutManager);
        int i2 = obtainStyledAttributes.getInt(R.styleable.RecyclerView_android_descendantFocusability, -1);
        if (i2 == -1) {
            setDescendantFocusability(262144);
        }
        this.mClipToPadding = obtainStyledAttributes.getBoolean(R.styleable.RecyclerView_android_clipToPadding, true);
        boolean z = obtainStyledAttributes.getBoolean(R.styleable.RecyclerView_fastScrollEnabled, false);
        this.mEnableFastScroller = z;
        if (z) {
            initFastScroller((StateListDrawable) obtainStyledAttributes.getDrawable(R.styleable.RecyclerView_fastScrollVerticalThumbDrawable), obtainStyledAttributes.getDrawable(R.styleable.RecyclerView_fastScrollVerticalTrackDrawable), (StateListDrawable) obtainStyledAttributes.getDrawable(R.styleable.RecyclerView_fastScrollHorizontalThumbDrawable), obtainStyledAttributes.getDrawable(R.styleable.RecyclerView_fastScrollHorizontalTrackDrawable));
        }
        obtainStyledAttributes.recycle();
        createLayoutManager(context, string, attrs, defStyleAttr, 0);
        boolean z2 = true;
        if (Build.VERSION.SDK_INT >= 21) {
            int[] iArr = NESTED_SCROLLING_ATTRS;
            TypedArray obtainStyledAttributes2 = context2.obtainStyledAttributes(attributeSet, iArr, i, 0);
            if (Build.VERSION.SDK_INT >= 29) {
                int i3 = i2;
                saveAttributeDataForStyleable(context, iArr, attrs, obtainStyledAttributes2, defStyleAttr, 0);
            } else {
                int i4 = i2;
            }
            z2 = obtainStyledAttributes2.getBoolean(0, true);
            obtainStyledAttributes2.recycle();
        } else {
            int i5 = i2;
        }
        setNestedScrollingEnabled(z2);
    }

    private void addAnimatingView(ViewHolder viewHolder) {
        View view = viewHolder.itemView;
        boolean z = view.getParent() == this;
        this.mRecycler.unscrapView(getChildViewHolder(view));
        if (viewHolder.isTmpDetached()) {
            this.mChildHelper.attachViewToParent(view, -1, view.getLayoutParams(), true);
        } else if (!z) {
            this.mChildHelper.addView(view, true);
        } else {
            this.mChildHelper.hide(view);
        }
    }

    private void animateChange(ViewHolder oldHolder, ViewHolder newHolder, ItemAnimator.ItemHolderInfo preInfo, ItemAnimator.ItemHolderInfo postInfo, boolean oldHolderDisappearing, boolean newHolderDisappearing) {
        oldHolder.setIsRecyclable(false);
        if (oldHolderDisappearing) {
            addAnimatingView(oldHolder);
        }
        if (oldHolder != newHolder) {
            if (newHolderDisappearing) {
                addAnimatingView(newHolder);
            }
            oldHolder.mShadowedHolder = newHolder;
            addAnimatingView(oldHolder);
            this.mRecycler.unscrapView(oldHolder);
            newHolder.setIsRecyclable(false);
            newHolder.mShadowingHolder = oldHolder;
        }
        if (this.mItemAnimator.animateChange(oldHolder, newHolder, preInfo, postInfo)) {
            postAnimationRunner();
        }
    }

    private void cancelScroll() {
        resetScroll();
        setScrollState(0);
    }

    /* JADX WARNING: type inference failed for: r1v2, types: [android.view.ViewParent] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void clearNestedRecyclerViewIfNotNested(androidx.recyclerview.widget.RecyclerView.ViewHolder r3) {
        /*
            java.lang.ref.WeakReference<androidx.recyclerview.widget.RecyclerView> r0 = r3.mNestedRecyclerView
            if (r0 == 0) goto L_0x0024
            java.lang.ref.WeakReference<androidx.recyclerview.widget.RecyclerView> r0 = r3.mNestedRecyclerView
            java.lang.Object r0 = r0.get()
            android.view.View r0 = (android.view.View) r0
        L_0x000c:
            if (r0 == 0) goto L_0x0021
            android.view.View r1 = r3.itemView
            if (r0 != r1) goto L_0x0013
            return
        L_0x0013:
            android.view.ViewParent r1 = r0.getParent()
            boolean r2 = r1 instanceof android.view.View
            if (r2 == 0) goto L_0x001f
            r0 = r1
            android.view.View r0 = (android.view.View) r0
            goto L_0x0020
        L_0x001f:
            r0 = 0
        L_0x0020:
            goto L_0x000c
        L_0x0021:
            r1 = 0
            r3.mNestedRecyclerView = r1
        L_0x0024:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.clearNestedRecyclerViewIfNotNested(androidx.recyclerview.widget.RecyclerView$ViewHolder):void");
    }

    private void createLayoutManager(Context context, String className, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Constructor<? extends U> constructor;
        if (className != null) {
            String className2 = className.trim();
            if (!className2.isEmpty()) {
                String className3 = getFullClassName(context, className2);
                try {
                    Class<? extends U> asSubclass = Class.forName(className3, false, isInEditMode() ? getClass().getClassLoader() : context.getClassLoader()).asSubclass(LayoutManager.class);
                    Object[] objArr = null;
                    try {
                        constructor = asSubclass.getConstructor(LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE);
                        objArr = new Object[]{context, attrs, Integer.valueOf(defStyleAttr), Integer.valueOf(defStyleRes)};
                    } catch (NoSuchMethodException e) {
                        constructor = asSubclass.getConstructor(new Class[0]);
                    }
                    constructor.setAccessible(true);
                    setLayoutManager((LayoutManager) constructor.newInstance(objArr));
                } catch (NoSuchMethodException e2) {
                    e2.initCause(e);
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Error creating LayoutManager " + className3, e2);
                } catch (ClassNotFoundException e3) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Unable to find LayoutManager " + className3, e3);
                } catch (InvocationTargetException e4) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Could not instantiate the LayoutManager: " + className3, e4);
                } catch (InstantiationException e5) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Could not instantiate the LayoutManager: " + className3, e5);
                } catch (IllegalAccessException e6) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Cannot access non-public constructor " + className3, e6);
                } catch (ClassCastException e7) {
                    throw new IllegalStateException(attrs.getPositionDescription() + ": Class is not a LayoutManager " + className3, e7);
                }
            }
        }
    }

    private boolean didChildRangeChange(int minPositionPreLayout, int maxPositionPreLayout) {
        findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
        int[] iArr = this.mMinMaxLayoutPositions;
        return (iArr[0] == minPositionPreLayout && iArr[1] == maxPositionPreLayout) ? false : true;
    }

    private void dispatchContentChangedIfNecessary() {
        int i = this.mEatenAccessibilityChangeFlags;
        this.mEatenAccessibilityChangeFlags = 0;
        if (i != 0 && isAccessibilityEnabled()) {
            AccessibilityEvent obtain = AccessibilityEvent.obtain();
            obtain.setEventType(2048);
            AccessibilityEventCompat.setContentChangeTypes(obtain, i);
            sendAccessibilityEventUnchecked(obtain);
        }
    }

    private void dispatchLayoutStep1() {
        boolean z = true;
        this.mState.assertLayoutStep(1);
        fillRemainingScrollValues(this.mState);
        this.mState.mIsMeasuring = false;
        startInterceptRequestLayout();
        this.mViewInfoStore.clear();
        onEnterLayoutOrScroll();
        processAdapterUpdatesAndSetAnimationFlags();
        saveFocusInfo();
        State state = this.mState;
        if (!state.mRunSimpleAnimations || !this.mItemsChanged) {
            z = false;
        }
        state.mTrackOldChangeHolders = z;
        this.mItemsChanged = false;
        this.mItemsAddedOrRemoved = false;
        State state2 = this.mState;
        state2.mInPreLayout = state2.mRunPredictiveAnimations;
        this.mState.mItemCount = this.mAdapter.getItemCount();
        findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
        if (this.mState.mRunSimpleAnimations) {
            int childCount = this.mChildHelper.getChildCount();
            for (int i = 0; i < childCount; i++) {
                ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
                if (!childViewHolderInt.shouldIgnore() && (!childViewHolderInt.isInvalid() || this.mAdapter.hasStableIds())) {
                    this.mViewInfoStore.addToPreLayout(childViewHolderInt, this.mItemAnimator.recordPreLayoutInformation(this.mState, childViewHolderInt, ItemAnimator.buildAdapterChangeFlagsForAnimations(childViewHolderInt), childViewHolderInt.getUnmodifiedPayloads()));
                    if (this.mState.mTrackOldChangeHolders && childViewHolderInt.isUpdated() && !childViewHolderInt.isRemoved() && !childViewHolderInt.shouldIgnore() && !childViewHolderInt.isInvalid()) {
                        this.mViewInfoStore.addToOldChangeHolders(getChangedHolderKey(childViewHolderInt), childViewHolderInt);
                    }
                }
            }
        }
        if (this.mState.mRunPredictiveAnimations) {
            saveOldPositions();
            boolean z2 = this.mState.mStructureChanged;
            this.mState.mStructureChanged = false;
            this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
            this.mState.mStructureChanged = z2;
            for (int i2 = 0; i2 < this.mChildHelper.getChildCount(); i2++) {
                ViewHolder childViewHolderInt2 = getChildViewHolderInt(this.mChildHelper.getChildAt(i2));
                if (!childViewHolderInt2.shouldIgnore() && !this.mViewInfoStore.isInPreLayout(childViewHolderInt2)) {
                    int buildAdapterChangeFlagsForAnimations = ItemAnimator.buildAdapterChangeFlagsForAnimations(childViewHolderInt2);
                    boolean hasAnyOfTheFlags = childViewHolderInt2.hasAnyOfTheFlags(8192);
                    if (!hasAnyOfTheFlags) {
                        buildAdapterChangeFlagsForAnimations |= 4096;
                    }
                    ItemAnimator.ItemHolderInfo recordPreLayoutInformation = this.mItemAnimator.recordPreLayoutInformation(this.mState, childViewHolderInt2, buildAdapterChangeFlagsForAnimations, childViewHolderInt2.getUnmodifiedPayloads());
                    if (hasAnyOfTheFlags) {
                        recordAnimationInfoIfBouncedHiddenView(childViewHolderInt2, recordPreLayoutInformation);
                    } else {
                        this.mViewInfoStore.addToAppearedInPreLayoutHolders(childViewHolderInt2, recordPreLayoutInformation);
                    }
                }
            }
            clearOldPositions();
        } else {
            clearOldPositions();
        }
        onExitLayoutOrScroll();
        stopInterceptRequestLayout(false);
        this.mState.mLayoutStep = 2;
    }

    private void dispatchLayoutStep2() {
        startInterceptRequestLayout();
        onEnterLayoutOrScroll();
        this.mState.assertLayoutStep(6);
        this.mAdapterHelper.consumeUpdatesInOnePass();
        this.mState.mItemCount = this.mAdapter.getItemCount();
        this.mState.mDeletedInvisibleItemCountSincePreviousLayout = 0;
        this.mState.mInPreLayout = false;
        this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
        this.mState.mStructureChanged = false;
        this.mPendingSavedState = null;
        State state = this.mState;
        state.mRunSimpleAnimations = state.mRunSimpleAnimations && this.mItemAnimator != null;
        this.mState.mLayoutStep = 4;
        onExitLayoutOrScroll();
        stopInterceptRequestLayout(false);
    }

    private void dispatchLayoutStep3() {
        this.mState.assertLayoutStep(4);
        startInterceptRequestLayout();
        onEnterLayoutOrScroll();
        this.mState.mLayoutStep = 1;
        if (this.mState.mRunSimpleAnimations) {
            for (int childCount = this.mChildHelper.getChildCount() - 1; childCount >= 0; childCount--) {
                ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(childCount));
                if (!childViewHolderInt.shouldIgnore()) {
                    long changedHolderKey = getChangedHolderKey(childViewHolderInt);
                    ItemAnimator.ItemHolderInfo recordPostLayoutInformation = this.mItemAnimator.recordPostLayoutInformation(this.mState, childViewHolderInt);
                    ViewHolder fromOldChangeHolders = this.mViewInfoStore.getFromOldChangeHolders(changedHolderKey);
                    if (fromOldChangeHolders == null || fromOldChangeHolders.shouldIgnore()) {
                        this.mViewInfoStore.addToPostLayout(childViewHolderInt, recordPostLayoutInformation);
                    } else {
                        boolean isDisappearing = this.mViewInfoStore.isDisappearing(fromOldChangeHolders);
                        boolean isDisappearing2 = this.mViewInfoStore.isDisappearing(childViewHolderInt);
                        if (!isDisappearing || fromOldChangeHolders != childViewHolderInt) {
                            ItemAnimator.ItemHolderInfo popFromPreLayout = this.mViewInfoStore.popFromPreLayout(fromOldChangeHolders);
                            this.mViewInfoStore.addToPostLayout(childViewHolderInt, recordPostLayoutInformation);
                            ItemAnimator.ItemHolderInfo popFromPostLayout = this.mViewInfoStore.popFromPostLayout(childViewHolderInt);
                            if (popFromPreLayout == null) {
                                handleMissingPreInfoForChangeError(changedHolderKey, childViewHolderInt, fromOldChangeHolders);
                            } else {
                                animateChange(fromOldChangeHolders, childViewHolderInt, popFromPreLayout, popFromPostLayout, isDisappearing, isDisappearing2);
                            }
                        } else {
                            this.mViewInfoStore.addToPostLayout(childViewHolderInt, recordPostLayoutInformation);
                        }
                    }
                }
            }
            this.mViewInfoStore.process(this.mViewInfoProcessCallback);
        }
        this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
        State state = this.mState;
        state.mPreviousLayoutItemCount = state.mItemCount;
        this.mDataSetHasChangedAfterLayout = false;
        this.mDispatchItemsChangedEvent = false;
        this.mState.mRunSimpleAnimations = false;
        this.mState.mRunPredictiveAnimations = false;
        this.mLayout.mRequestedSimpleAnimations = false;
        if (this.mRecycler.mChangedScrap != null) {
            this.mRecycler.mChangedScrap.clear();
        }
        if (this.mLayout.mPrefetchMaxObservedInInitialPrefetch) {
            this.mLayout.mPrefetchMaxCountObserved = 0;
            this.mLayout.mPrefetchMaxObservedInInitialPrefetch = false;
            this.mRecycler.updateViewCacheSize();
        }
        this.mLayout.onLayoutCompleted(this.mState);
        onExitLayoutOrScroll();
        stopInterceptRequestLayout(false);
        this.mViewInfoStore.clear();
        int[] iArr = this.mMinMaxLayoutPositions;
        if (didChildRangeChange(iArr[0], iArr[1])) {
            dispatchOnScrolled(0, 0);
        }
        recoverFocusFromState();
        resetFocusInfo();
    }

    private boolean dispatchToOnItemTouchListeners(MotionEvent e) {
        OnItemTouchListener onItemTouchListener = this.mInterceptingOnItemTouchListener;
        if (onItemTouchListener != null) {
            onItemTouchListener.onTouchEvent(this, e);
            int action = e.getAction();
            if (action == 3 || action == 1) {
                this.mInterceptingOnItemTouchListener = null;
            }
            return true;
        } else if (e.getAction() == 0) {
            return false;
        } else {
            return findInterceptingOnItemTouchListener(e);
        }
    }

    private boolean findInterceptingOnItemTouchListener(MotionEvent e) {
        int action = e.getAction();
        int size = this.mOnItemTouchListeners.size();
        int i = 0;
        while (i < size) {
            OnItemTouchListener onItemTouchListener = this.mOnItemTouchListeners.get(i);
            if (!onItemTouchListener.onInterceptTouchEvent(this, e) || action == 3) {
                i++;
            } else {
                this.mInterceptingOnItemTouchListener = onItemTouchListener;
                return true;
            }
        }
        return false;
    }

    private void findMinMaxChildLayoutPositions(int[] into) {
        int childCount = this.mChildHelper.getChildCount();
        if (childCount == 0) {
            into[0] = -1;
            into[1] = -1;
            return;
        }
        int i = Integer.MAX_VALUE;
        int i2 = Integer.MIN_VALUE;
        for (int i3 = 0; i3 < childCount; i3++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i3));
            if (!childViewHolderInt.shouldIgnore()) {
                int layoutPosition = childViewHolderInt.getLayoutPosition();
                if (layoutPosition < i) {
                    i = layoutPosition;
                }
                if (layoutPosition > i2) {
                    i2 = layoutPosition;
                }
            }
        }
        into[0] = i;
        into[1] = i2;
    }

    static RecyclerView findNestedRecyclerView(View view) {
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        if (view instanceof RecyclerView) {
            return (RecyclerView) view;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            RecyclerView findNestedRecyclerView = findNestedRecyclerView(viewGroup.getChildAt(i));
            if (findNestedRecyclerView != null) {
                return findNestedRecyclerView;
            }
        }
        return null;
    }

    private View findNextViewToFocus() {
        ViewHolder findViewHolderForAdapterPosition;
        int i = this.mState.mFocusedItemPosition != -1 ? this.mState.mFocusedItemPosition : 0;
        int itemCount = this.mState.getItemCount();
        int i2 = i;
        while (i2 < itemCount) {
            ViewHolder findViewHolderForAdapterPosition2 = findViewHolderForAdapterPosition(i2);
            if (findViewHolderForAdapterPosition2 == null) {
                break;
            } else if (findViewHolderForAdapterPosition2.itemView.hasFocusable()) {
                return findViewHolderForAdapterPosition2.itemView;
            } else {
                i2++;
            }
        }
        int min = Math.min(itemCount, i) - 1;
        while (min >= 0 && (findViewHolderForAdapterPosition = findViewHolderForAdapterPosition(min)) != null) {
            if (findViewHolderForAdapterPosition.itemView.hasFocusable()) {
                return findViewHolderForAdapterPosition.itemView;
            }
            min--;
        }
        return null;
    }

    static ViewHolder getChildViewHolderInt(View child) {
        if (child == null) {
            return null;
        }
        return ((LayoutParams) child.getLayoutParams()).mViewHolder;
    }

    static void getDecoratedBoundsWithMarginsInt(View view, Rect outBounds) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        Rect rect = layoutParams.mDecorInsets;
        outBounds.set((view.getLeft() - rect.left) - layoutParams.leftMargin, (view.getTop() - rect.top) - layoutParams.topMargin, view.getRight() + rect.right + layoutParams.rightMargin, view.getBottom() + rect.bottom + layoutParams.bottomMargin);
    }

    private int getDeepestFocusedViewWithId(View view) {
        int id = view.getId();
        while (!view.isFocused() && (view instanceof ViewGroup) && view.hasFocus()) {
            view = ((ViewGroup) view).getFocusedChild();
            if (view.getId() != -1) {
                id = view.getId();
            }
        }
        return id;
    }

    private String getFullClassName(Context context, String className) {
        return className.charAt(0) == '.' ? context.getPackageName() + className : className.contains(".") ? className : RecyclerView.class.getPackage().getName() + '.' + className;
    }

    private NestedScrollingChildHelper getScrollingChildHelper() {
        if (this.mScrollingChildHelper == null) {
            this.mScrollingChildHelper = new NestedScrollingChildHelper(this);
        }
        return this.mScrollingChildHelper;
    }

    private void handleMissingPreInfoForChangeError(long key, ViewHolder holder, ViewHolder oldChangeViewHolder) {
        int childCount = this.mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (childViewHolderInt != holder && getChangedHolderKey(childViewHolderInt) == key) {
                Adapter adapter = this.mAdapter;
                if (adapter == null || !adapter.hasStableIds()) {
                    throw new IllegalStateException("Two different ViewHolders have the same change ID. This might happen due to inconsistent Adapter update events or if the LayoutManager lays out the same View multiple times.\n ViewHolder 1:" + childViewHolderInt + " \n View Holder 2:" + holder + exceptionLabel());
                }
                throw new IllegalStateException("Two different ViewHolders have the same stable ID. Stable IDs in your adapter MUST BE unique and SHOULD NOT change.\n ViewHolder 1:" + childViewHolderInt + " \n View Holder 2:" + holder + exceptionLabel());
            }
        }
        Log.e(TAG, "Problem while matching changed view holders with the newones. The pre-layout information for the change holder " + oldChangeViewHolder + " cannot be found but it is necessary for " + holder + exceptionLabel());
    }

    private boolean hasUpdatedView() {
        int childCount = this.mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore() && childViewHolderInt.isUpdated()) {
                return true;
            }
        }
        return false;
    }

    private void initAutofill() {
        if (ViewCompat.getImportantForAutofill(this) == 0) {
            ViewCompat.setImportantForAutofill(this, 8);
        }
    }

    private void initChildrenHelper() {
        this.mChildHelper = new ChildHelper(new ChildHelper.Callback() {
            public void addView(View child, int index) {
                RecyclerView.this.addView(child, index);
                RecyclerView.this.dispatchChildAttached(child);
            }

            public void attachViewToParent(View child, int index, ViewGroup.LayoutParams layoutParams) {
                ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(child);
                if (childViewHolderInt != null) {
                    if (childViewHolderInt.isTmpDetached() || childViewHolderInt.shouldIgnore()) {
                        childViewHolderInt.clearTmpDetachFlag();
                    } else {
                        throw new IllegalArgumentException("Called attach on a child which is not detached: " + childViewHolderInt + RecyclerView.this.exceptionLabel());
                    }
                }
                RecyclerView.this.attachViewToParent(child, index, layoutParams);
            }

            public void detachViewFromParent(int offset) {
                ViewHolder childViewHolderInt;
                View childAt = getChildAt(offset);
                if (!(childAt == null || (childViewHolderInt = RecyclerView.getChildViewHolderInt(childAt)) == null)) {
                    if (!childViewHolderInt.isTmpDetached() || childViewHolderInt.shouldIgnore()) {
                        childViewHolderInt.addFlags(256);
                    } else {
                        throw new IllegalArgumentException("called detach on an already detached child " + childViewHolderInt + RecyclerView.this.exceptionLabel());
                    }
                }
                RecyclerView.this.detachViewFromParent(offset);
            }

            public View getChildAt(int offset) {
                return RecyclerView.this.getChildAt(offset);
            }

            public int getChildCount() {
                return RecyclerView.this.getChildCount();
            }

            public ViewHolder getChildViewHolder(View view) {
                return RecyclerView.getChildViewHolderInt(view);
            }

            public int indexOfChild(View view) {
                return RecyclerView.this.indexOfChild(view);
            }

            public void onEnteredHiddenState(View child) {
                ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(child);
                if (childViewHolderInt != null) {
                    childViewHolderInt.onEnteredHiddenState(RecyclerView.this);
                }
            }

            public void onLeftHiddenState(View child) {
                ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(child);
                if (childViewHolderInt != null) {
                    childViewHolderInt.onLeftHiddenState(RecyclerView.this);
                }
            }

            public void removeAllViews() {
                int childCount = getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childAt = getChildAt(i);
                    RecyclerView.this.dispatchChildDetached(childAt);
                    childAt.clearAnimation();
                }
                RecyclerView.this.removeAllViews();
            }

            public void removeViewAt(int index) {
                View childAt = RecyclerView.this.getChildAt(index);
                if (childAt != null) {
                    RecyclerView.this.dispatchChildDetached(childAt);
                    childAt.clearAnimation();
                }
                RecyclerView.this.removeViewAt(index);
            }
        });
    }

    private boolean isPreferredNextFocus(View focused, View next, int direction) {
        if (next == null || next == this || findContainingItemView(next) == null) {
            return false;
        }
        if (focused == null || findContainingItemView(focused) == null) {
            return true;
        }
        this.mTempRect.set(0, 0, focused.getWidth(), focused.getHeight());
        this.mTempRect2.set(0, 0, next.getWidth(), next.getHeight());
        offsetDescendantRectToMyCoords(focused, this.mTempRect);
        offsetDescendantRectToMyCoords(next, this.mTempRect2);
        int i = this.mLayout.getLayoutDirection() == 1 ? -1 : 1;
        int i2 = 0;
        if ((this.mTempRect.left < this.mTempRect2.left || this.mTempRect.right <= this.mTempRect2.left) && this.mTempRect.right < this.mTempRect2.right) {
            i2 = 1;
        } else if ((this.mTempRect.right > this.mTempRect2.right || this.mTempRect.left >= this.mTempRect2.right) && this.mTempRect.left > this.mTempRect2.left) {
            i2 = -1;
        }
        char c = 0;
        if ((this.mTempRect.top < this.mTempRect2.top || this.mTempRect.bottom <= this.mTempRect2.top) && this.mTempRect.bottom < this.mTempRect2.bottom) {
            c = 1;
        } else if ((this.mTempRect.bottom > this.mTempRect2.bottom || this.mTempRect.top >= this.mTempRect2.bottom) && this.mTempRect.top > this.mTempRect2.top) {
            c = 65535;
        }
        switch (direction) {
            case 1:
                return c < 0 || (c == 0 && i2 * i <= 0);
            case 2:
                return c > 0 || (c == 0 && i2 * i >= 0);
            case 17:
                return i2 < 0;
            case 33:
                return c < 0;
            case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT:
                return i2 > 0;
            case 130:
                return c > 0;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction + exceptionLabel());
        }
    }

    private void onPointerUp(MotionEvent e) {
        int actionIndex = e.getActionIndex();
        if (e.getPointerId(actionIndex) == this.mScrollPointerId) {
            int i = actionIndex == 0 ? 1 : 0;
            this.mScrollPointerId = e.getPointerId(i);
            int x = (int) (e.getX(i) + 0.5f);
            this.mLastTouchX = x;
            this.mInitialTouchX = x;
            int y = (int) (e.getY(i) + 0.5f);
            this.mLastTouchY = y;
            this.mInitialTouchY = y;
        }
    }

    private boolean predictiveItemAnimationsEnabled() {
        return this.mItemAnimator != null && this.mLayout.supportsPredictiveItemAnimations();
    }

    private void processAdapterUpdatesAndSetAnimationFlags() {
        if (this.mDataSetHasChangedAfterLayout) {
            this.mAdapterHelper.reset();
            if (this.mDispatchItemsChangedEvent) {
                this.mLayout.onItemsChanged(this);
            }
        }
        if (predictiveItemAnimationsEnabled()) {
            this.mAdapterHelper.preProcess();
        } else {
            this.mAdapterHelper.consumeUpdatesInOnePass();
        }
        boolean z = false;
        boolean z2 = this.mItemsAddedOrRemoved || this.mItemsChanged;
        this.mState.mRunSimpleAnimations = this.mFirstLayoutComplete && this.mItemAnimator != null && (this.mDataSetHasChangedAfterLayout || z2 || this.mLayout.mRequestedSimpleAnimations) && (!this.mDataSetHasChangedAfterLayout || this.mAdapter.hasStableIds());
        State state = this.mState;
        if (state.mRunSimpleAnimations && z2 && !this.mDataSetHasChangedAfterLayout && predictiveItemAnimationsEnabled()) {
            z = true;
        }
        state.mRunPredictiveAnimations = z;
    }

    private void pullGlows(float x, float overscrollX, float y, float overscrollY) {
        boolean z = false;
        if (overscrollX < 0.0f) {
            ensureLeftGlow();
            EdgeEffectCompat.onPull(this.mLeftGlow, (-overscrollX) / ((float) getWidth()), 1.0f - (y / ((float) getHeight())));
            z = true;
        } else if (overscrollX > 0.0f) {
            ensureRightGlow();
            EdgeEffectCompat.onPull(this.mRightGlow, overscrollX / ((float) getWidth()), y / ((float) getHeight()));
            z = true;
        }
        if (overscrollY < 0.0f) {
            ensureTopGlow();
            EdgeEffectCompat.onPull(this.mTopGlow, (-overscrollY) / ((float) getHeight()), x / ((float) getWidth()));
            z = true;
        } else if (overscrollY > 0.0f) {
            ensureBottomGlow();
            EdgeEffectCompat.onPull(this.mBottomGlow, overscrollY / ((float) getHeight()), 1.0f - (x / ((float) getWidth())));
            z = true;
        }
        if (z || overscrollX != 0.0f || overscrollY != 0.0f) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void recoverFocusFromState() {
        View findViewById;
        if (this.mPreserveFocusAfterLayout && this.mAdapter != null && hasFocus() && getDescendantFocusability() != 393216) {
            if (getDescendantFocusability() != 131072 || !isFocused()) {
                if (!isFocused()) {
                    View focusedChild = getFocusedChild();
                    if (!IGNORE_DETACHED_FOCUSED_CHILD || (focusedChild.getParent() != null && focusedChild.hasFocus())) {
                        if (!this.mChildHelper.isHidden(focusedChild)) {
                            return;
                        }
                    } else if (this.mChildHelper.getChildCount() == 0) {
                        requestFocus();
                        return;
                    }
                }
                ViewHolder viewHolder = null;
                if (this.mState.mFocusedItemId != -1 && this.mAdapter.hasStableIds()) {
                    viewHolder = findViewHolderForItemId(this.mState.mFocusedItemId);
                }
                View view = null;
                if (viewHolder != null && !this.mChildHelper.isHidden(viewHolder.itemView) && viewHolder.itemView.hasFocusable()) {
                    view = viewHolder.itemView;
                } else if (this.mChildHelper.getChildCount() > 0) {
                    view = findNextViewToFocus();
                }
                if (view != null) {
                    if (!(((long) this.mState.mFocusedSubChildId) == -1 || (findViewById = view.findViewById(this.mState.mFocusedSubChildId)) == null || !findViewById.isFocusable())) {
                        view = findViewById;
                    }
                    view.requestFocus();
                }
            }
        }
    }

    private void releaseGlows() {
        boolean z = false;
        EdgeEffect edgeEffect = this.mLeftGlow;
        if (edgeEffect != null) {
            edgeEffect.onRelease();
            z = this.mLeftGlow.isFinished();
        }
        EdgeEffect edgeEffect2 = this.mTopGlow;
        if (edgeEffect2 != null) {
            edgeEffect2.onRelease();
            z |= this.mTopGlow.isFinished();
        }
        EdgeEffect edgeEffect3 = this.mRightGlow;
        if (edgeEffect3 != null) {
            edgeEffect3.onRelease();
            z |= this.mRightGlow.isFinished();
        }
        EdgeEffect edgeEffect4 = this.mBottomGlow;
        if (edgeEffect4 != null) {
            edgeEffect4.onRelease();
            z |= this.mBottomGlow.isFinished();
        }
        if (z) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void requestChildOnScreen(View child, View focused) {
        View view = focused != null ? focused : child;
        this.mTempRect.set(0, 0, view.getWidth(), view.getHeight());
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof LayoutParams) {
            LayoutParams layoutParams2 = (LayoutParams) layoutParams;
            if (!layoutParams2.mInsetsDirty) {
                Rect rect = layoutParams2.mDecorInsets;
                this.mTempRect.left -= rect.left;
                this.mTempRect.right += rect.right;
                this.mTempRect.top -= rect.top;
                this.mTempRect.bottom += rect.bottom;
            }
        }
        if (focused != null) {
            offsetDescendantRectToMyCoords(focused, this.mTempRect);
            offsetRectIntoDescendantCoords(child, this.mTempRect);
        }
        this.mLayout.requestChildRectangleOnScreen(this, child, this.mTempRect, !this.mFirstLayoutComplete, focused == null);
    }

    private void resetFocusInfo() {
        this.mState.mFocusedItemId = -1;
        this.mState.mFocusedItemPosition = -1;
        this.mState.mFocusedSubChildId = -1;
    }

    private void resetScroll() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.clear();
        }
        stopNestedScroll(0);
        releaseGlows();
    }

    private void saveFocusInfo() {
        View view = null;
        if (this.mPreserveFocusAfterLayout && hasFocus() && this.mAdapter != null) {
            view = getFocusedChild();
        }
        ViewHolder findContainingViewHolder = view == null ? null : findContainingViewHolder(view);
        if (findContainingViewHolder == null) {
            resetFocusInfo();
            return;
        }
        this.mState.mFocusedItemId = this.mAdapter.hasStableIds() ? findContainingViewHolder.getItemId() : -1;
        this.mState.mFocusedItemPosition = this.mDataSetHasChangedAfterLayout ? -1 : findContainingViewHolder.isRemoved() ? findContainingViewHolder.mOldPosition : findContainingViewHolder.getAdapterPosition();
        this.mState.mFocusedSubChildId = getDeepestFocusedViewWithId(findContainingViewHolder.itemView);
    }

    private void setAdapterInternal(Adapter adapter, boolean compatibleWithPrevious, boolean removeAndRecycleViews) {
        Adapter adapter2 = this.mAdapter;
        if (adapter2 != null) {
            adapter2.unregisterAdapterDataObserver(this.mObserver);
            this.mAdapter.onDetachedFromRecyclerView(this);
        }
        if (!compatibleWithPrevious || removeAndRecycleViews) {
            removeAndRecycleViews();
        }
        this.mAdapterHelper.reset();
        Adapter adapter3 = this.mAdapter;
        this.mAdapter = adapter;
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.mObserver);
            adapter.onAttachedToRecyclerView(this);
        }
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.onAdapterChanged(adapter3, this.mAdapter);
        }
        this.mRecycler.onAdapterChanged(adapter3, this.mAdapter, compatibleWithPrevious);
        this.mState.mStructureChanged = true;
    }

    private void stopScrollersInternal() {
        this.mViewFlinger.stop();
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.stopSmoothScroller();
        }
    }

    /* access modifiers changed from: package-private */
    public void absorbGlows(int velocityX, int velocityY) {
        if (velocityX < 0) {
            ensureLeftGlow();
            if (this.mLeftGlow.isFinished()) {
                this.mLeftGlow.onAbsorb(-velocityX);
            }
        } else if (velocityX > 0) {
            ensureRightGlow();
            if (this.mRightGlow.isFinished()) {
                this.mRightGlow.onAbsorb(velocityX);
            }
        }
        if (velocityY < 0) {
            ensureTopGlow();
            if (this.mTopGlow.isFinished()) {
                this.mTopGlow.onAbsorb(-velocityY);
            }
        } else if (velocityY > 0) {
            ensureBottomGlow();
            if (this.mBottomGlow.isFinished()) {
                this.mBottomGlow.onAbsorb(velocityY);
            }
        }
        if (velocityX != 0 || velocityY != 0) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void addFocusables(ArrayList<View> arrayList, int direction, int focusableMode) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null || !layoutManager.onAddFocusables(this, arrayList, direction, focusableMode)) {
            super.addFocusables(arrayList, direction, focusableMode);
        }
    }

    public void addItemDecoration(ItemDecoration decor) {
        addItemDecoration(decor, -1);
    }

    public void addItemDecoration(ItemDecoration decor, int index) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout");
        }
        if (this.mItemDecorations.isEmpty()) {
            setWillNotDraw(false);
        }
        if (index < 0) {
            this.mItemDecorations.add(decor);
        } else {
            this.mItemDecorations.add(index, decor);
        }
        markItemDecorInsetsDirty();
        requestLayout();
    }

    public void addOnChildAttachStateChangeListener(OnChildAttachStateChangeListener listener) {
        if (this.mOnChildAttachStateListeners == null) {
            this.mOnChildAttachStateListeners = new ArrayList();
        }
        this.mOnChildAttachStateListeners.add(listener);
    }

    public void addOnItemTouchListener(OnItemTouchListener listener) {
        this.mOnItemTouchListeners.add(listener);
    }

    public void addOnScrollListener(OnScrollListener listener) {
        if (this.mScrollListeners == null) {
            this.mScrollListeners = new ArrayList();
        }
        this.mScrollListeners.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void animateAppearance(ViewHolder itemHolder, ItemAnimator.ItemHolderInfo preLayoutInfo, ItemAnimator.ItemHolderInfo postLayoutInfo) {
        itemHolder.setIsRecyclable(false);
        if (this.mItemAnimator.animateAppearance(itemHolder, preLayoutInfo, postLayoutInfo)) {
            postAnimationRunner();
        }
    }

    /* access modifiers changed from: package-private */
    public void animateDisappearance(ViewHolder holder, ItemAnimator.ItemHolderInfo preLayoutInfo, ItemAnimator.ItemHolderInfo postLayoutInfo) {
        addAnimatingView(holder);
        holder.setIsRecyclable(false);
        if (this.mItemAnimator.animateDisappearance(holder, preLayoutInfo, postLayoutInfo)) {
            postAnimationRunner();
        }
    }

    /* access modifiers changed from: package-private */
    public void assertInLayoutOrScroll(String message) {
        if (isComputingLayout()) {
            return;
        }
        if (message == null) {
            throw new IllegalStateException("Cannot call this method unless RecyclerView is computing a layout or scrolling" + exceptionLabel());
        }
        throw new IllegalStateException(message + exceptionLabel());
    }

    /* access modifiers changed from: package-private */
    public void assertNotInLayoutOrScroll(String message) {
        if (isComputingLayout()) {
            if (message == null) {
                throw new IllegalStateException("Cannot call this method while RecyclerView is computing a layout or scrolling" + exceptionLabel());
            }
            throw new IllegalStateException(message);
        } else if (this.mDispatchScrollCounter > 0) {
            Log.w(TAG, "Cannot call this method in a scroll callback. Scroll callbacks mightbe run during a measure & layout pass where you cannot change theRecyclerView data. Any method call that might change the structureof the RecyclerView or the adapter contents should be postponed tothe next frame.", new IllegalStateException(HttpUrl.FRAGMENT_ENCODE_SET + exceptionLabel()));
        }
    }

    /* access modifiers changed from: package-private */
    public boolean canReuseUpdatedViewHolder(ViewHolder viewHolder) {
        ItemAnimator itemAnimator = this.mItemAnimator;
        return itemAnimator == null || itemAnimator.canReuseUpdatedViewHolder(viewHolder, viewHolder.getUnmodifiedPayloads());
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return (p instanceof LayoutParams) && this.mLayout.checkLayoutParams((LayoutParams) p);
    }

    /* access modifiers changed from: package-private */
    public void clearOldPositions() {
        int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < unfilteredChildCount; i++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!childViewHolderInt.shouldIgnore()) {
                childViewHolderInt.clearOldPosition();
            }
        }
        this.mRecycler.clearOldPositions();
    }

    public void clearOnChildAttachStateChangeListeners() {
        List<OnChildAttachStateChangeListener> list = this.mOnChildAttachStateListeners;
        if (list != null) {
            list.clear();
        }
    }

    public void clearOnScrollListeners() {
        List<OnScrollListener> list = this.mScrollListeners;
        if (list != null) {
            list.clear();
        }
    }

    public int computeHorizontalScrollExtent() {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null && layoutManager.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollExtent(this.mState);
        }
        return 0;
    }

    public int computeHorizontalScrollOffset() {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null && layoutManager.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollOffset(this.mState);
        }
        return 0;
    }

    public int computeHorizontalScrollRange() {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null && layoutManager.canScrollHorizontally()) {
            return this.mLayout.computeHorizontalScrollRange(this.mState);
        }
        return 0;
    }

    public int computeVerticalScrollExtent() {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null && layoutManager.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollExtent(this.mState);
        }
        return 0;
    }

    public int computeVerticalScrollOffset() {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null && layoutManager.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollOffset(this.mState);
        }
        return 0;
    }

    public int computeVerticalScrollRange() {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null && layoutManager.canScrollVertically()) {
            return this.mLayout.computeVerticalScrollRange(this.mState);
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    public void considerReleasingGlowsOnScroll(int dx, int dy) {
        boolean z = false;
        EdgeEffect edgeEffect = this.mLeftGlow;
        if (edgeEffect != null && !edgeEffect.isFinished() && dx > 0) {
            this.mLeftGlow.onRelease();
            z = this.mLeftGlow.isFinished();
        }
        EdgeEffect edgeEffect2 = this.mRightGlow;
        if (edgeEffect2 != null && !edgeEffect2.isFinished() && dx < 0) {
            this.mRightGlow.onRelease();
            z |= this.mRightGlow.isFinished();
        }
        EdgeEffect edgeEffect3 = this.mTopGlow;
        if (edgeEffect3 != null && !edgeEffect3.isFinished() && dy > 0) {
            this.mTopGlow.onRelease();
            z |= this.mTopGlow.isFinished();
        }
        EdgeEffect edgeEffect4 = this.mBottomGlow;
        if (edgeEffect4 != null && !edgeEffect4.isFinished() && dy < 0) {
            this.mBottomGlow.onRelease();
            z |= this.mBottomGlow.isFinished();
        }
        if (z) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void consumePendingUpdateOperations() {
        if (!this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout) {
            TraceCompat.beginSection(TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG);
            dispatchLayout();
            TraceCompat.endSection();
        } else if (this.mAdapterHelper.hasPendingUpdates()) {
            if (this.mAdapterHelper.hasAnyUpdateTypes(4) && !this.mAdapterHelper.hasAnyUpdateTypes(11)) {
                TraceCompat.beginSection(TRACE_HANDLE_ADAPTER_UPDATES_TAG);
                startInterceptRequestLayout();
                onEnterLayoutOrScroll();
                this.mAdapterHelper.preProcess();
                if (!this.mLayoutWasDefered) {
                    if (hasUpdatedView()) {
                        dispatchLayout();
                    } else {
                        this.mAdapterHelper.consumePostponedUpdates();
                    }
                }
                stopInterceptRequestLayout(true);
                onExitLayoutOrScroll();
                TraceCompat.endSection();
            } else if (this.mAdapterHelper.hasPendingUpdates()) {
                TraceCompat.beginSection(TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG);
                dispatchLayout();
                TraceCompat.endSection();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void defaultOnMeasure(int widthSpec, int heightSpec) {
        setMeasuredDimension(LayoutManager.chooseSize(widthSpec, getPaddingLeft() + getPaddingRight(), ViewCompat.getMinimumWidth(this)), LayoutManager.chooseSize(heightSpec, getPaddingTop() + getPaddingBottom(), ViewCompat.getMinimumHeight(this)));
    }

    /* access modifiers changed from: package-private */
    public void dispatchChildAttached(View child) {
        ViewHolder childViewHolderInt = getChildViewHolderInt(child);
        onChildAttachedToWindow(child);
        Adapter adapter = this.mAdapter;
        if (!(adapter == null || childViewHolderInt == null)) {
            adapter.onViewAttachedToWindow(childViewHolderInt);
        }
        List<OnChildAttachStateChangeListener> list = this.mOnChildAttachStateListeners;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.mOnChildAttachStateListeners.get(size).onChildViewAttachedToWindow(child);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchChildDetached(View child) {
        ViewHolder childViewHolderInt = getChildViewHolderInt(child);
        onChildDetachedFromWindow(child);
        Adapter adapter = this.mAdapter;
        if (!(adapter == null || childViewHolderInt == null)) {
            adapter.onViewDetachedFromWindow(childViewHolderInt);
        }
        List<OnChildAttachStateChangeListener> list = this.mOnChildAttachStateListeners;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.mOnChildAttachStateListeners.get(size).onChildViewDetachedFromWindow(child);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchLayout() {
        if (this.mAdapter == null) {
            Log.e(TAG, "No adapter attached; skipping layout");
        } else if (this.mLayout == null) {
            Log.e(TAG, "No layout manager attached; skipping layout");
        } else {
            this.mState.mIsMeasuring = false;
            if (this.mState.mLayoutStep == 1) {
                dispatchLayoutStep1();
                this.mLayout.setExactMeasureSpecsFrom(this);
                dispatchLayoutStep2();
            } else if (!this.mAdapterHelper.hasUpdates() && this.mLayout.getWidth() == getWidth() && this.mLayout.getHeight() == getHeight()) {
                this.mLayout.setExactMeasureSpecsFrom(this);
            } else {
                this.mLayout.setExactMeasureSpecsFrom(this);
                dispatchLayoutStep2();
            }
            dispatchLayoutStep3();
        }
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return getScrollingChildHelper().dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return getScrollingChildHelper().dispatchNestedPreFling(velocityX, velocityY);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow, int type) {
        return getScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }

    public final void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type, int[] consumed) {
        getScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed);
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type) {
        return getScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnScrollStateChanged(int state) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.onScrollStateChanged(state);
        }
        onScrollStateChanged(state);
        OnScrollListener onScrollListener = this.mScrollListener;
        if (onScrollListener != null) {
            onScrollListener.onScrollStateChanged(this, state);
        }
        List<OnScrollListener> list = this.mScrollListeners;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.mScrollListeners.get(size).onScrollStateChanged(this, state);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnScrolled(int hresult, int vresult) {
        this.mDispatchScrollCounter++;
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        onScrollChanged(scrollX, scrollY, scrollX - hresult, scrollY - vresult);
        onScrolled(hresult, vresult);
        OnScrollListener onScrollListener = this.mScrollListener;
        if (onScrollListener != null) {
            onScrollListener.onScrolled(this, hresult, vresult);
        }
        List<OnScrollListener> list = this.mScrollListeners;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.mScrollListeners.get(size).onScrolled(this, hresult, vresult);
            }
        }
        this.mDispatchScrollCounter--;
    }

    /* access modifiers changed from: package-private */
    public void dispatchPendingImportantForAccessibilityChanges() {
        int i;
        for (int size = this.mPendingAccessibilityImportanceChange.size() - 1; size >= 0; size--) {
            ViewHolder viewHolder = this.mPendingAccessibilityImportanceChange.get(size);
            if (viewHolder.itemView.getParent() == this && !viewHolder.shouldIgnore() && (i = viewHolder.mPendingAccessibilityState) != -1) {
                ViewCompat.setImportantForAccessibility(viewHolder.itemView, i);
                viewHolder.mPendingAccessibilityState = -1;
            }
        }
        this.mPendingAccessibilityImportanceChange.clear();
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        onPopulateAccessibilityEvent(event);
        return true;
    }

    /* access modifiers changed from: protected */
    public void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        dispatchThawSelfOnly(sparseArray);
    }

    /* access modifiers changed from: protected */
    public void dispatchSaveInstanceState(SparseArray<Parcelable> sparseArray) {
        dispatchFreezeSelfOnly(sparseArray);
    }

    public void draw(Canvas c) {
        super.draw(c);
        int size = this.mItemDecorations.size();
        for (int i = 0; i < size; i++) {
            this.mItemDecorations.get(i).onDrawOver(c, this, this.mState);
        }
        boolean z = false;
        EdgeEffect edgeEffect = this.mLeftGlow;
        boolean z2 = true;
        if (edgeEffect != null && !edgeEffect.isFinished()) {
            int save = c.save();
            int paddingBottom = this.mClipToPadding ? getPaddingBottom() : 0;
            c.rotate(270.0f);
            c.translate((float) ((-getHeight()) + paddingBottom), 0.0f);
            EdgeEffect edgeEffect2 = this.mLeftGlow;
            z = edgeEffect2 != null && edgeEffect2.draw(c);
            c.restoreToCount(save);
        }
        EdgeEffect edgeEffect3 = this.mTopGlow;
        if (edgeEffect3 != null && !edgeEffect3.isFinished()) {
            int save2 = c.save();
            if (this.mClipToPadding) {
                c.translate((float) getPaddingLeft(), (float) getPaddingTop());
            }
            EdgeEffect edgeEffect4 = this.mTopGlow;
            z |= edgeEffect4 != null && edgeEffect4.draw(c);
            c.restoreToCount(save2);
        }
        EdgeEffect edgeEffect5 = this.mRightGlow;
        if (edgeEffect5 != null && !edgeEffect5.isFinished()) {
            int save3 = c.save();
            int width = getWidth();
            int paddingTop = this.mClipToPadding ? getPaddingTop() : 0;
            c.rotate(90.0f);
            c.translate((float) (-paddingTop), (float) (-width));
            EdgeEffect edgeEffect6 = this.mRightGlow;
            z |= edgeEffect6 != null && edgeEffect6.draw(c);
            c.restoreToCount(save3);
        }
        EdgeEffect edgeEffect7 = this.mBottomGlow;
        if (edgeEffect7 != null && !edgeEffect7.isFinished()) {
            int save4 = c.save();
            c.rotate(180.0f);
            if (this.mClipToPadding) {
                c.translate((float) ((-getWidth()) + getPaddingRight()), (float) ((-getHeight()) + getPaddingBottom()));
            } else {
                c.translate((float) (-getWidth()), (float) (-getHeight()));
            }
            EdgeEffect edgeEffect8 = this.mBottomGlow;
            if (edgeEffect8 == null || !edgeEffect8.draw(c)) {
                z2 = false;
            }
            z |= z2;
            c.restoreToCount(save4);
        }
        if (!z && this.mItemAnimator != null && this.mItemDecorations.size() > 0 && this.mItemAnimator.isRunning()) {
            z = true;
        }
        if (z) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }

    /* access modifiers changed from: package-private */
    public void ensureBottomGlow() {
        if (this.mBottomGlow == null) {
            EdgeEffect createEdgeEffect = this.mEdgeEffectFactory.createEdgeEffect(this, 3);
            this.mBottomGlow = createEdgeEffect;
            if (this.mClipToPadding) {
                createEdgeEffect.setSize((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom());
            } else {
                createEdgeEffect.setSize(getMeasuredWidth(), getMeasuredHeight());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void ensureLeftGlow() {
        if (this.mLeftGlow == null) {
            EdgeEffect createEdgeEffect = this.mEdgeEffectFactory.createEdgeEffect(this, 0);
            this.mLeftGlow = createEdgeEffect;
            if (this.mClipToPadding) {
                createEdgeEffect.setSize((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight());
            } else {
                createEdgeEffect.setSize(getMeasuredHeight(), getMeasuredWidth());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void ensureRightGlow() {
        if (this.mRightGlow == null) {
            EdgeEffect createEdgeEffect = this.mEdgeEffectFactory.createEdgeEffect(this, 2);
            this.mRightGlow = createEdgeEffect;
            if (this.mClipToPadding) {
                createEdgeEffect.setSize((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight());
            } else {
                createEdgeEffect.setSize(getMeasuredHeight(), getMeasuredWidth());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void ensureTopGlow() {
        if (this.mTopGlow == null) {
            EdgeEffect createEdgeEffect = this.mEdgeEffectFactory.createEdgeEffect(this, 1);
            this.mTopGlow = createEdgeEffect;
            if (this.mClipToPadding) {
                createEdgeEffect.setSize((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom());
            } else {
                createEdgeEffect.setSize(getMeasuredWidth(), getMeasuredHeight());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public String exceptionLabel() {
        return " " + super.toString() + ", adapter:" + this.mAdapter + ", layout:" + this.mLayout + ", context:" + getContext();
    }

    /* access modifiers changed from: package-private */
    public final void fillRemainingScrollValues(State state) {
        if (getScrollState() == 2) {
            OverScroller overScroller = this.mViewFlinger.mOverScroller;
            state.mRemainingScrollHorizontal = overScroller.getFinalX() - overScroller.getCurrX();
            state.mRemainingScrollVertical = overScroller.getFinalY() - overScroller.getCurrY();
            return;
        }
        state.mRemainingScrollHorizontal = 0;
        state.mRemainingScrollVertical = 0;
    }

    public View findChildViewUnder(float x, float y) {
        for (int childCount = this.mChildHelper.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = this.mChildHelper.getChildAt(childCount);
            float translationX = childAt.getTranslationX();
            float translationY = childAt.getTranslationY();
            if (x >= ((float) childAt.getLeft()) + translationX && x <= ((float) childAt.getRight()) + translationX && y >= ((float) childAt.getTop()) + translationY && y <= ((float) childAt.getBottom()) + translationY) {
                return childAt;
            }
        }
        return null;
    }

    public View findContainingItemView(View view) {
        ViewParent parent = view.getParent();
        while (parent != null && parent != this && (parent instanceof View)) {
            view = (View) parent;
            parent = view.getParent();
        }
        if (parent == this) {
            return view;
        }
        return null;
    }

    public ViewHolder findContainingViewHolder(View view) {
        View findContainingItemView = findContainingItemView(view);
        if (findContainingItemView == null) {
            return null;
        }
        return getChildViewHolder(findContainingItemView);
    }

    public ViewHolder findViewHolderForAdapterPosition(int position) {
        if (this.mDataSetHasChangedAfterLayout) {
            return null;
        }
        int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        ViewHolder viewHolder = null;
        for (int i = 0; i < unfilteredChildCount; i++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.isRemoved() && getAdapterPositionFor(childViewHolderInt) == position) {
                if (!this.mChildHelper.isHidden(childViewHolderInt.itemView)) {
                    return childViewHolderInt;
                }
                viewHolder = childViewHolderInt;
            }
        }
        return viewHolder;
    }

    public ViewHolder findViewHolderForItemId(long id) {
        Adapter adapter = this.mAdapter;
        if (adapter == null || !adapter.hasStableIds()) {
            return null;
        }
        int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        ViewHolder viewHolder = null;
        for (int i = 0; i < unfilteredChildCount; i++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.isRemoved() && childViewHolderInt.getItemId() == id) {
                if (!this.mChildHelper.isHidden(childViewHolderInt.itemView)) {
                    return childViewHolderInt;
                }
                viewHolder = childViewHolderInt;
            }
        }
        return viewHolder;
    }

    public ViewHolder findViewHolderForLayoutPosition(int position) {
        return findViewHolderForPosition(position, false);
    }

    @Deprecated
    public ViewHolder findViewHolderForPosition(int position) {
        return findViewHolderForPosition(position, false);
    }

    /* access modifiers changed from: package-private */
    public ViewHolder findViewHolderForPosition(int position, boolean checkNewPosition) {
        int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        ViewHolder viewHolder = null;
        for (int i = 0; i < unfilteredChildCount; i++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.isRemoved()) {
                if (checkNewPosition) {
                    if (childViewHolderInt.mPosition != position) {
                        continue;
                    }
                } else if (childViewHolderInt.getLayoutPosition() != position) {
                    continue;
                }
                if (!this.mChildHelper.isHidden(childViewHolderInt.itemView)) {
                    return childViewHolderInt;
                }
                viewHolder = childViewHolderInt;
            }
        }
        return viewHolder;
    }

    public boolean fling(int velocityX, int velocityY) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null) {
            Log.e(TAG, "Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return false;
        } else if (this.mLayoutSuppressed) {
            return false;
        } else {
            boolean canScrollHorizontally = layoutManager.canScrollHorizontally();
            boolean canScrollVertically = this.mLayout.canScrollVertically();
            if (!canScrollHorizontally || Math.abs(velocityX) < this.mMinFlingVelocity) {
                velocityX = 0;
            }
            if (!canScrollVertically || Math.abs(velocityY) < this.mMinFlingVelocity) {
                velocityY = 0;
            }
            if (!(velocityX == 0 && velocityY == 0) && !dispatchNestedPreFling((float) velocityX, (float) velocityY)) {
                boolean z = canScrollHorizontally || canScrollVertically;
                dispatchNestedFling((float) velocityX, (float) velocityY, z);
                OnFlingListener onFlingListener = this.mOnFlingListener;
                if (onFlingListener != null && onFlingListener.onFling(velocityX, velocityY)) {
                    return true;
                }
                if (z) {
                    int i = 0;
                    if (canScrollHorizontally) {
                        i = 0 | 1;
                    }
                    if (canScrollVertically) {
                        i |= 2;
                    }
                    startNestedScroll(i, 1);
                    int i2 = this.mMaxFlingVelocity;
                    int velocityX2 = Math.max(-i2, Math.min(velocityX, i2));
                    int i3 = this.mMaxFlingVelocity;
                    this.mViewFlinger.fling(velocityX2, Math.max(-i3, Math.min(velocityY, i3)));
                    return true;
                }
            }
            return false;
        }
    }

    public View focusSearch(View focused, int direction) {
        View view;
        View onInterceptFocusSearch = this.mLayout.onInterceptFocusSearch(focused, direction);
        if (onInterceptFocusSearch != null) {
            return onInterceptFocusSearch;
        }
        boolean z = true;
        boolean z2 = this.mAdapter != null && this.mLayout != null && !isComputingLayout() && !this.mLayoutSuppressed;
        FocusFinder instance = FocusFinder.getInstance();
        if (!z2 || !(direction == 2 || direction == 1)) {
            view = instance.findNextFocus(this, focused, direction);
            if (view == null && z2) {
                consumePendingUpdateOperations();
                if (findContainingItemView(focused) == null) {
                    return null;
                }
                startInterceptRequestLayout();
                view = this.mLayout.onFocusSearchFailed(focused, direction, this.mRecycler, this.mState);
                stopInterceptRequestLayout(false);
            }
        } else {
            boolean z3 = false;
            if (this.mLayout.canScrollVertically()) {
                int i = direction == 2 ? 130 : 33;
                z3 = instance.findNextFocus(this, focused, i) == null;
                if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
                    direction = i;
                }
            }
            if (!z3 && this.mLayout.canScrollHorizontally()) {
                int i2 = (direction == 2) ^ (this.mLayout.getLayoutDirection() == 1) ? 66 : 17;
                if (instance.findNextFocus(this, focused, i2) != null) {
                    z = false;
                }
                z3 = z;
                if (FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
                    direction = i2;
                }
            }
            if (z3) {
                consumePendingUpdateOperations();
                if (findContainingItemView(focused) == null) {
                    return null;
                }
                startInterceptRequestLayout();
                this.mLayout.onFocusSearchFailed(focused, direction, this.mRecycler, this.mState);
                stopInterceptRequestLayout(false);
            }
            view = instance.findNextFocus(this, focused, direction);
        }
        if (view == null || view.hasFocusable()) {
            return isPreferredNextFocus(focused, view, direction) ? view : super.focusSearch(focused, direction);
        }
        if (getFocusedChild() == null) {
            return super.focusSearch(focused, direction);
        }
        requestChildOnScreen(view, (View) null);
        return focused;
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            return layoutManager.generateDefaultLayoutParams();
        }
        throw new IllegalStateException("RecyclerView has no LayoutManager" + exceptionLabel());
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            return layoutManager.generateLayoutParams(getContext(), attrs);
        }
        throw new IllegalStateException("RecyclerView has no LayoutManager" + exceptionLabel());
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            return layoutManager.generateLayoutParams(p);
        }
        throw new IllegalStateException("RecyclerView has no LayoutManager" + exceptionLabel());
    }

    public CharSequence getAccessibilityClassName() {
        return "androidx.recyclerview.widget.RecyclerView";
    }

    public Adapter getAdapter() {
        return this.mAdapter;
    }

    /* access modifiers changed from: package-private */
    public int getAdapterPositionFor(ViewHolder viewHolder) {
        if (viewHolder.hasAnyOfTheFlags(524) || !viewHolder.isBound()) {
            return -1;
        }
        return this.mAdapterHelper.applyPendingUpdatesToPosition(viewHolder.mPosition);
    }

    public int getBaseline() {
        LayoutManager layoutManager = this.mLayout;
        return layoutManager != null ? layoutManager.getBaseline() : super.getBaseline();
    }

    /* access modifiers changed from: package-private */
    public long getChangedHolderKey(ViewHolder holder) {
        return this.mAdapter.hasStableIds() ? holder.getItemId() : (long) holder.mPosition;
    }

    public int getChildAdapterPosition(View child) {
        ViewHolder childViewHolderInt = getChildViewHolderInt(child);
        if (childViewHolderInt != null) {
            return childViewHolderInt.getAdapterPosition();
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public int getChildDrawingOrder(int childCount, int i) {
        ChildDrawingOrderCallback childDrawingOrderCallback = this.mChildDrawingOrderCallback;
        return childDrawingOrderCallback == null ? super.getChildDrawingOrder(childCount, i) : childDrawingOrderCallback.onGetChildDrawingOrder(childCount, i);
    }

    public long getChildItemId(View child) {
        ViewHolder childViewHolderInt;
        Adapter adapter = this.mAdapter;
        if (adapter == null || !adapter.hasStableIds() || (childViewHolderInt = getChildViewHolderInt(child)) == null) {
            return -1;
        }
        return childViewHolderInt.getItemId();
    }

    public int getChildLayoutPosition(View child) {
        ViewHolder childViewHolderInt = getChildViewHolderInt(child);
        if (childViewHolderInt != null) {
            return childViewHolderInt.getLayoutPosition();
        }
        return -1;
    }

    @Deprecated
    public int getChildPosition(View child) {
        return getChildAdapterPosition(child);
    }

    public ViewHolder getChildViewHolder(View child) {
        ViewParent parent = child.getParent();
        if (parent == null || parent == this) {
            return getChildViewHolderInt(child);
        }
        throw new IllegalArgumentException("View " + child + " is not a direct child of " + this);
    }

    public boolean getClipToPadding() {
        return this.mClipToPadding;
    }

    public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate() {
        return this.mAccessibilityDelegate;
    }

    public void getDecoratedBoundsWithMargins(View view, Rect outBounds) {
        getDecoratedBoundsWithMarginsInt(view, outBounds);
    }

    public EdgeEffectFactory getEdgeEffectFactory() {
        return this.mEdgeEffectFactory;
    }

    public ItemAnimator getItemAnimator() {
        return this.mItemAnimator;
    }

    /* access modifiers changed from: package-private */
    public Rect getItemDecorInsetsForChild(View child) {
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        if (!layoutParams.mInsetsDirty) {
            return layoutParams.mDecorInsets;
        }
        if (this.mState.isPreLayout() && (layoutParams.isItemChanged() || layoutParams.isViewInvalid())) {
            return layoutParams.mDecorInsets;
        }
        Rect rect = layoutParams.mDecorInsets;
        rect.set(0, 0, 0, 0);
        int size = this.mItemDecorations.size();
        for (int i = 0; i < size; i++) {
            this.mTempRect.set(0, 0, 0, 0);
            this.mItemDecorations.get(i).getItemOffsets(this.mTempRect, child, this, this.mState);
            rect.left += this.mTempRect.left;
            rect.top += this.mTempRect.top;
            rect.right += this.mTempRect.right;
            rect.bottom += this.mTempRect.bottom;
        }
        layoutParams.mInsetsDirty = false;
        return rect;
    }

    public ItemDecoration getItemDecorationAt(int index) {
        int itemDecorationCount = getItemDecorationCount();
        if (index >= 0 && index < itemDecorationCount) {
            return this.mItemDecorations.get(index);
        }
        throw new IndexOutOfBoundsException(index + " is an invalid index for size " + itemDecorationCount);
    }

    public int getItemDecorationCount() {
        return this.mItemDecorations.size();
    }

    public LayoutManager getLayoutManager() {
        return this.mLayout;
    }

    public int getMaxFlingVelocity() {
        return this.mMaxFlingVelocity;
    }

    public int getMinFlingVelocity() {
        return this.mMinFlingVelocity;
    }

    /* access modifiers changed from: package-private */
    public long getNanoTime() {
        if (ALLOW_THREAD_GAP_WORK) {
            return System.nanoTime();
        }
        return 0;
    }

    public OnFlingListener getOnFlingListener() {
        return this.mOnFlingListener;
    }

    public boolean getPreserveFocusAfterLayout() {
        return this.mPreserveFocusAfterLayout;
    }

    public RecycledViewPool getRecycledViewPool() {
        return this.mRecycler.getRecycledViewPool();
    }

    public int getScrollState() {
        return this.mScrollState;
    }

    public boolean hasFixedSize() {
        return this.mHasFixedSize;
    }

    public boolean hasNestedScrollingParent() {
        return getScrollingChildHelper().hasNestedScrollingParent();
    }

    public boolean hasNestedScrollingParent(int type) {
        return getScrollingChildHelper().hasNestedScrollingParent(type);
    }

    public boolean hasPendingAdapterUpdates() {
        return !this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout || this.mAdapterHelper.hasPendingUpdates();
    }

    /* access modifiers changed from: package-private */
    public void initAdapterManager() {
        this.mAdapterHelper = new AdapterHelper(new AdapterHelper.Callback() {
            /* access modifiers changed from: package-private */
            public void dispatchUpdate(AdapterHelper.UpdateOp op) {
                switch (op.cmd) {
                    case 1:
                        RecyclerView.this.mLayout.onItemsAdded(RecyclerView.this, op.positionStart, op.itemCount);
                        return;
                    case 2:
                        RecyclerView.this.mLayout.onItemsRemoved(RecyclerView.this, op.positionStart, op.itemCount);
                        return;
                    case 4:
                        RecyclerView.this.mLayout.onItemsUpdated(RecyclerView.this, op.positionStart, op.itemCount, op.payload);
                        return;
                    case 8:
                        RecyclerView.this.mLayout.onItemsMoved(RecyclerView.this, op.positionStart, op.itemCount, 1);
                        return;
                    default:
                        return;
                }
            }

            public ViewHolder findViewHolder(int position) {
                ViewHolder findViewHolderForPosition = RecyclerView.this.findViewHolderForPosition(position, true);
                if (findViewHolderForPosition != null && !RecyclerView.this.mChildHelper.isHidden(findViewHolderForPosition.itemView)) {
                    return findViewHolderForPosition;
                }
                return null;
            }

            public void markViewHoldersUpdated(int positionStart, int itemCount, Object payload) {
                RecyclerView.this.viewRangeUpdate(positionStart, itemCount, payload);
                RecyclerView.this.mItemsChanged = true;
            }

            public void offsetPositionsForAdd(int positionStart, int itemCount) {
                RecyclerView.this.offsetPositionRecordsForInsert(positionStart, itemCount);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }

            public void offsetPositionsForMove(int from, int to) {
                RecyclerView.this.offsetPositionRecordsForMove(from, to);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }

            public void offsetPositionsForRemovingInvisible(int start, int count) {
                RecyclerView.this.offsetPositionRecordsForRemove(start, count, true);
                RecyclerView.this.mItemsAddedOrRemoved = true;
                RecyclerView.this.mState.mDeletedInvisibleItemCountSincePreviousLayout += count;
            }

            public void offsetPositionsForRemovingLaidOutOrNewView(int positionStart, int itemCount) {
                RecyclerView.this.offsetPositionRecordsForRemove(positionStart, itemCount, false);
                RecyclerView.this.mItemsAddedOrRemoved = true;
            }

            public void onDispatchFirstPass(AdapterHelper.UpdateOp op) {
                dispatchUpdate(op);
            }

            public void onDispatchSecondPass(AdapterHelper.UpdateOp op) {
                dispatchUpdate(op);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void initFastScroller(StateListDrawable verticalThumbDrawable, Drawable verticalTrackDrawable, StateListDrawable horizontalThumbDrawable, Drawable horizontalTrackDrawable) {
        if (verticalThumbDrawable == null || verticalTrackDrawable == null || horizontalThumbDrawable == null || horizontalTrackDrawable == null) {
            throw new IllegalArgumentException("Trying to set fast scroller without both required drawables." + exceptionLabel());
        }
        Resources resources = getContext().getResources();
        new FastScroller(this, verticalThumbDrawable, verticalTrackDrawable, horizontalThumbDrawable, horizontalTrackDrawable, resources.getDimensionPixelSize(R.dimen.fastscroll_default_thickness), resources.getDimensionPixelSize(R.dimen.fastscroll_minimum_range), resources.getDimensionPixelOffset(R.dimen.fastscroll_margin));
    }

    /* access modifiers changed from: package-private */
    public void invalidateGlows() {
        this.mBottomGlow = null;
        this.mTopGlow = null;
        this.mRightGlow = null;
        this.mLeftGlow = null;
    }

    public void invalidateItemDecorations() {
        if (this.mItemDecorations.size() != 0) {
            LayoutManager layoutManager = this.mLayout;
            if (layoutManager != null) {
                layoutManager.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout");
            }
            markItemDecorInsetsDirty();
            requestLayout();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isAccessibilityEnabled() {
        AccessibilityManager accessibilityManager = this.mAccessibilityManager;
        return accessibilityManager != null && accessibilityManager.isEnabled();
    }

    public boolean isAnimating() {
        ItemAnimator itemAnimator = this.mItemAnimator;
        return itemAnimator != null && itemAnimator.isRunning();
    }

    public boolean isAttachedToWindow() {
        return this.mIsAttached;
    }

    public boolean isComputingLayout() {
        return this.mLayoutOrScrollCounter > 0;
    }

    @Deprecated
    public boolean isLayoutFrozen() {
        return isLayoutSuppressed();
    }

    public final boolean isLayoutSuppressed() {
        return this.mLayoutSuppressed;
    }

    public boolean isNestedScrollingEnabled() {
        return getScrollingChildHelper().isNestedScrollingEnabled();
    }

    /* access modifiers changed from: package-private */
    public void jumpToPositionForSmoothScroller(int position) {
        if (this.mLayout != null) {
            setScrollState(2);
            this.mLayout.scrollToPosition(position);
            awakenScrollBars();
        }
    }

    /* access modifiers changed from: package-private */
    public void markItemDecorInsetsDirty() {
        int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < unfilteredChildCount; i++) {
            ((LayoutParams) this.mChildHelper.getUnfilteredChildAt(i).getLayoutParams()).mInsetsDirty = true;
        }
        this.mRecycler.markItemDecorInsetsDirty();
    }

    /* access modifiers changed from: package-private */
    public void markKnownViewsInvalid() {
        int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < unfilteredChildCount; i++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore()) {
                childViewHolderInt.addFlags(6);
            }
        }
        markItemDecorInsetsDirty();
        this.mRecycler.markKnownViewsInvalid();
    }

    public void offsetChildrenHorizontal(int dx) {
        int childCount = this.mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            this.mChildHelper.getChildAt(i).offsetLeftAndRight(dx);
        }
    }

    public void offsetChildrenVertical(int dy) {
        int childCount = this.mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            this.mChildHelper.getChildAt(i).offsetTopAndBottom(dy);
        }
    }

    /* access modifiers changed from: package-private */
    public void offsetPositionRecordsForInsert(int positionStart, int itemCount) {
        int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < unfilteredChildCount; i++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore() && childViewHolderInt.mPosition >= positionStart) {
                childViewHolderInt.offsetPosition(itemCount, false);
                this.mState.mStructureChanged = true;
            }
        }
        this.mRecycler.offsetPositionRecordsForInsert(positionStart, itemCount);
        requestLayout();
    }

    /* access modifiers changed from: package-private */
    public void offsetPositionRecordsForMove(int from, int to) {
        int i;
        int i2;
        int i3;
        int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        if (from < to) {
            i3 = from;
            i2 = to;
            i = -1;
        } else {
            i3 = to;
            i2 = from;
            i = 1;
        }
        for (int i4 = 0; i4 < unfilteredChildCount; i4++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i4));
            if (childViewHolderInt != null && childViewHolderInt.mPosition >= i3 && childViewHolderInt.mPosition <= i2) {
                if (childViewHolderInt.mPosition == from) {
                    childViewHolderInt.offsetPosition(to - from, false);
                } else {
                    childViewHolderInt.offsetPosition(i, false);
                }
                this.mState.mStructureChanged = true;
            }
        }
        this.mRecycler.offsetPositionRecordsForMove(from, to);
        requestLayout();
    }

    /* access modifiers changed from: package-private */
    public void offsetPositionRecordsForRemove(int positionStart, int itemCount, boolean applyToPreLayout) {
        int i = positionStart + itemCount;
        int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i2 = 0; i2 < unfilteredChildCount; i2++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i2));
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore()) {
                if (childViewHolderInt.mPosition >= i) {
                    childViewHolderInt.offsetPosition(-itemCount, applyToPreLayout);
                    this.mState.mStructureChanged = true;
                } else if (childViewHolderInt.mPosition >= positionStart) {
                    childViewHolderInt.flagRemovedAndOffsetPosition(positionStart - 1, -itemCount, applyToPreLayout);
                    this.mState.mStructureChanged = true;
                }
            }
        }
        this.mRecycler.offsetPositionRecordsForRemove(positionStart, itemCount, applyToPreLayout);
        requestLayout();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mLayoutOrScrollCounter = 0;
        boolean z = true;
        this.mIsAttached = true;
        if (!this.mFirstLayoutComplete || isLayoutRequested()) {
            z = false;
        }
        this.mFirstLayoutComplete = z;
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.dispatchAttachedToWindow(this);
        }
        this.mPostedAnimatorRunner = false;
        if (ALLOW_THREAD_GAP_WORK) {
            GapWorker gapWorker = GapWorker.sGapWorker.get();
            this.mGapWorker = gapWorker;
            if (gapWorker == null) {
                this.mGapWorker = new GapWorker();
                Display display = ViewCompat.getDisplay(this);
                float f = 60.0f;
                if (!isInEditMode() && display != null) {
                    float refreshRate = display.getRefreshRate();
                    if (refreshRate >= 30.0f) {
                        f = refreshRate;
                    }
                }
                this.mGapWorker.mFrameIntervalNs = (long) (1.0E9f / f);
                GapWorker.sGapWorker.set(this.mGapWorker);
            }
            this.mGapWorker.add(this);
        }
    }

    public void onChildAttachedToWindow(View child) {
    }

    public void onChildDetachedFromWindow(View child) {
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        GapWorker gapWorker;
        super.onDetachedFromWindow();
        ItemAnimator itemAnimator = this.mItemAnimator;
        if (itemAnimator != null) {
            itemAnimator.endAnimations();
        }
        stopScroll();
        this.mIsAttached = false;
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.dispatchDetachedFromWindow(this, this.mRecycler);
        }
        this.mPendingAccessibilityImportanceChange.clear();
        removeCallbacks(this.mItemAnimatorRunner);
        this.mViewInfoStore.onDetach();
        if (ALLOW_THREAD_GAP_WORK && (gapWorker = this.mGapWorker) != null) {
            gapWorker.remove(this);
            this.mGapWorker = null;
        }
    }

    public void onDraw(Canvas c) {
        super.onDraw(c);
        int size = this.mItemDecorations.size();
        for (int i = 0; i < size; i++) {
            this.mItemDecorations.get(i).onDraw(c, this, this.mState);
        }
    }

    /* access modifiers changed from: package-private */
    public void onEnterLayoutOrScroll() {
        this.mLayoutOrScrollCounter++;
    }

    /* access modifiers changed from: package-private */
    public void onExitLayoutOrScroll() {
        onExitLayoutOrScroll(true);
    }

    /* access modifiers changed from: package-private */
    public void onExitLayoutOrScroll(boolean enableChangeEvents) {
        int i = this.mLayoutOrScrollCounter - 1;
        this.mLayoutOrScrollCounter = i;
        if (i < 1) {
            this.mLayoutOrScrollCounter = 0;
            if (enableChangeEvents) {
                dispatchContentChangedIfNecessary();
                dispatchPendingImportantForAccessibilityChanges();
            }
        }
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        float f;
        float f2;
        if (this.mLayout != null && !this.mLayoutSuppressed && event.getAction() == 8) {
            if ((event.getSource() & 2) != 0) {
                f2 = this.mLayout.canScrollVertically() ? -event.getAxisValue(9) : 0.0f;
                f = this.mLayout.canScrollHorizontally() ? event.getAxisValue(10) : 0.0f;
            } else if ((event.getSource() & 4194304) != 0) {
                float axisValue = event.getAxisValue(26);
                if (this.mLayout.canScrollVertically()) {
                    f2 = -axisValue;
                    f = 0.0f;
                } else if (this.mLayout.canScrollHorizontally()) {
                    float f3 = axisValue;
                    f2 = 0.0f;
                    f = f3;
                } else {
                    f2 = 0.0f;
                    f = 0.0f;
                }
            } else {
                f2 = 0.0f;
                f = 0.0f;
            }
            if (!(f2 == 0.0f && f == 0.0f)) {
                scrollByInternal((int) (this.mScaledHorizontalScrollFactor * f), (int) (this.mScaledVerticalScrollFactor * f2), event);
            }
        }
        return false;
    }

    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (this.mLayoutSuppressed) {
            return false;
        }
        this.mInterceptingOnItemTouchListener = null;
        if (findInterceptingOnItemTouchListener(e)) {
            cancelScroll();
            return true;
        }
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null) {
            return false;
        }
        boolean canScrollHorizontally = layoutManager.canScrollHorizontally();
        boolean canScrollVertically = this.mLayout.canScrollVertically();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(e);
        int actionMasked = e.getActionMasked();
        int actionIndex = e.getActionIndex();
        switch (actionMasked) {
            case 0:
                if (this.mIgnoreMotionEventTillDown) {
                    this.mIgnoreMotionEventTillDown = false;
                }
                this.mScrollPointerId = e.getPointerId(0);
                int x = (int) (e.getX() + 0.5f);
                this.mLastTouchX = x;
                this.mInitialTouchX = x;
                int y = (int) (e.getY() + 0.5f);
                this.mLastTouchY = y;
                this.mInitialTouchY = y;
                if (this.mScrollState == 2) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    setScrollState(1);
                    stopNestedScroll(1);
                }
                int[] iArr = this.mNestedOffsets;
                iArr[1] = 0;
                iArr[0] = 0;
                int i = 0;
                if (canScrollHorizontally) {
                    i = 0 | 1;
                }
                if (canScrollVertically) {
                    i |= 2;
                }
                startNestedScroll(i, 0);
                break;
            case 1:
                this.mVelocityTracker.clear();
                stopNestedScroll(0);
                break;
            case 2:
                int findPointerIndex = e.findPointerIndex(this.mScrollPointerId);
                if (findPointerIndex >= 0) {
                    int x2 = (int) (e.getX(findPointerIndex) + 0.5f);
                    int y2 = (int) (e.getY(findPointerIndex) + 0.5f);
                    if (this.mScrollState != 1) {
                        int i2 = x2 - this.mInitialTouchX;
                        int i3 = y2 - this.mInitialTouchY;
                        boolean z = false;
                        if (canScrollHorizontally && Math.abs(i2) > this.mTouchSlop) {
                            this.mLastTouchX = x2;
                            z = true;
                        }
                        if (canScrollVertically && Math.abs(i3) > this.mTouchSlop) {
                            this.mLastTouchY = y2;
                            z = true;
                        }
                        if (z) {
                            setScrollState(1);
                            break;
                        }
                    }
                } else {
                    Log.e(TAG, "Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?");
                    return false;
                }
                break;
            case 3:
                cancelScroll();
                break;
            case 5:
                this.mScrollPointerId = e.getPointerId(actionIndex);
                int x3 = (int) (e.getX(actionIndex) + 0.5f);
                this.mLastTouchX = x3;
                this.mInitialTouchX = x3;
                int y3 = (int) (e.getY(actionIndex) + 0.5f);
                this.mLastTouchY = y3;
                this.mInitialTouchY = y3;
                break;
            case 6:
                onPointerUp(e);
                break;
        }
        return this.mScrollState == 1;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        TraceCompat.beginSection(TRACE_ON_LAYOUT_TAG);
        dispatchLayout();
        TraceCompat.endSection();
        this.mFirstLayoutComplete = true;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthSpec, int heightSpec) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null) {
            defaultOnMeasure(widthSpec, heightSpec);
            return;
        }
        boolean z = false;
        if (layoutManager.isAutoMeasureEnabled()) {
            int mode = View.MeasureSpec.getMode(widthSpec);
            int mode2 = View.MeasureSpec.getMode(heightSpec);
            this.mLayout.onMeasure(this.mRecycler, this.mState, widthSpec, heightSpec);
            if (mode == 1073741824 && mode2 == 1073741824) {
                z = true;
            }
            if (!z && this.mAdapter != null) {
                if (this.mState.mLayoutStep == 1) {
                    dispatchLayoutStep1();
                }
                this.mLayout.setMeasureSpecs(widthSpec, heightSpec);
                this.mState.mIsMeasuring = true;
                dispatchLayoutStep2();
                this.mLayout.setMeasuredDimensionFromChildren(widthSpec, heightSpec);
                if (this.mLayout.shouldMeasureTwice()) {
                    this.mLayout.setMeasureSpecs(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), BasicMeasure.EXACTLY), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), BasicMeasure.EXACTLY));
                    this.mState.mIsMeasuring = true;
                    dispatchLayoutStep2();
                    this.mLayout.setMeasuredDimensionFromChildren(widthSpec, heightSpec);
                }
            }
        } else if (this.mHasFixedSize) {
            this.mLayout.onMeasure(this.mRecycler, this.mState, widthSpec, heightSpec);
        } else {
            if (this.mAdapterUpdateDuringMeasure) {
                startInterceptRequestLayout();
                onEnterLayoutOrScroll();
                processAdapterUpdatesAndSetAnimationFlags();
                onExitLayoutOrScroll();
                if (this.mState.mRunPredictiveAnimations) {
                    this.mState.mInPreLayout = true;
                } else {
                    this.mAdapterHelper.consumeUpdatesInOnePass();
                    this.mState.mInPreLayout = false;
                }
                this.mAdapterUpdateDuringMeasure = false;
                stopInterceptRequestLayout(false);
            } else if (this.mState.mRunPredictiveAnimations) {
                setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
                return;
            }
            Adapter adapter = this.mAdapter;
            if (adapter != null) {
                this.mState.mItemCount = adapter.getItemCount();
            } else {
                this.mState.mItemCount = 0;
            }
            startInterceptRequestLayout();
            this.mLayout.onMeasure(this.mRecycler, this.mState, widthSpec, heightSpec);
            stopInterceptRequestLayout(false);
            this.mState.mInPreLayout = false;
        }
    }

    /* access modifiers changed from: protected */
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (isComputingLayout()) {
            return false;
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        this.mPendingSavedState = savedState;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (this.mLayout != null && this.mPendingSavedState.mLayoutState != null) {
            this.mLayout.onRestoreInstanceState(this.mPendingSavedState.mLayoutState);
        }
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        SavedState savedState2 = this.mPendingSavedState;
        if (savedState2 != null) {
            savedState.copyFrom(savedState2);
        } else {
            LayoutManager layoutManager = this.mLayout;
            if (layoutManager != null) {
                savedState.mLayoutState = layoutManager.onSaveInstanceState();
            } else {
                savedState.mLayoutState = null;
            }
        }
        return savedState;
    }

    public void onScrollStateChanged(int state) {
    }

    public void onScrolled(int dx, int dy) {
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            invalidateGlows();
        }
    }

    public boolean onTouchEvent(MotionEvent e) {
        int i;
        int i2;
        int i3;
        char c;
        int i4;
        MotionEvent motionEvent = e;
        int i5 = 0;
        if (this.mLayoutSuppressed || this.mIgnoreMotionEventTillDown) {
            return false;
        }
        int i6 = 1;
        if (dispatchToOnItemTouchListeners(e)) {
            cancelScroll();
            return true;
        }
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null) {
            return false;
        }
        boolean canScrollHorizontally = layoutManager.canScrollHorizontally();
        boolean canScrollVertically = this.mLayout.canScrollVertically();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        boolean z = false;
        int actionMasked = e.getActionMasked();
        int actionIndex = e.getActionIndex();
        if (actionMasked == 0) {
            int[] iArr = this.mNestedOffsets;
            iArr[1] = 0;
            iArr[0] = 0;
        }
        MotionEvent obtain = MotionEvent.obtain(e);
        int[] iArr2 = this.mNestedOffsets;
        obtain.offsetLocation((float) iArr2[0], (float) iArr2[1]);
        switch (actionMasked) {
            case 0:
                this.mScrollPointerId = motionEvent.getPointerId(0);
                int x = (int) (e.getX() + 0.5f);
                this.mLastTouchX = x;
                this.mInitialTouchX = x;
                int y = (int) (e.getY() + 0.5f);
                this.mLastTouchY = y;
                this.mInitialTouchY = y;
                int i7 = 0;
                if (canScrollHorizontally) {
                    i7 = 0 | 1;
                }
                if (canScrollVertically) {
                    i7 |= 2;
                }
                startNestedScroll(i7, 0);
                break;
            case 1:
                this.mVelocityTracker.addMovement(obtain);
                z = true;
                this.mVelocityTracker.computeCurrentVelocity(1000, (float) this.mMaxFlingVelocity);
                float f = canScrollHorizontally ? -this.mVelocityTracker.getXVelocity(this.mScrollPointerId) : 0.0f;
                float f2 = canScrollVertically ? -this.mVelocityTracker.getYVelocity(this.mScrollPointerId) : 0.0f;
                if ((f == 0.0f && f2 == 0.0f) || !fling((int) f, (int) f2)) {
                    setScrollState(0);
                }
                resetScroll();
                break;
            case 2:
                int findPointerIndex = motionEvent.findPointerIndex(this.mScrollPointerId);
                if (findPointerIndex >= 0) {
                    int x2 = (int) (motionEvent.getX(findPointerIndex) + 0.5f);
                    int y2 = (int) (motionEvent.getY(findPointerIndex) + 0.5f);
                    int i8 = this.mLastTouchX - x2;
                    int i9 = this.mLastTouchY - y2;
                    if (this.mScrollState != 1) {
                        boolean z2 = false;
                        if (canScrollHorizontally) {
                            i8 = i8 > 0 ? Math.max(0, i8 - this.mTouchSlop) : Math.min(0, this.mTouchSlop + i8);
                            if (i8 != 0) {
                                z2 = true;
                            }
                        }
                        if (canScrollVertically) {
                            i9 = i9 > 0 ? Math.max(0, i9 - this.mTouchSlop) : Math.min(0, this.mTouchSlop + i9);
                            if (i9 != 0) {
                                z2 = true;
                            }
                        }
                        if (z2) {
                            i6 = 1;
                            setScrollState(1);
                        } else {
                            i6 = 1;
                        }
                        i2 = i8;
                        i = i9;
                    } else {
                        i2 = i8;
                        i = i9;
                    }
                    if (this.mScrollState != i6) {
                        int i10 = y2;
                        int i11 = x2;
                        int i12 = findPointerIndex;
                        break;
                    } else {
                        int[] iArr3 = this.mReusableIntPair;
                        iArr3[0] = 0;
                        iArr3[i6] = 0;
                        int i13 = y2;
                        int i14 = x2;
                        int i15 = findPointerIndex;
                        if (dispatchNestedPreScroll(canScrollHorizontally ? i2 : 0, canScrollVertically ? i : 0, iArr3, this.mScrollOffset, 0)) {
                            int[] iArr4 = this.mReusableIntPair;
                            int i16 = i2 - iArr4[0];
                            c = 1;
                            int i17 = i - iArr4[1];
                            int[] iArr5 = this.mNestedOffsets;
                            int i18 = iArr5[0];
                            int[] iArr6 = this.mScrollOffset;
                            iArr5[0] = i18 + iArr6[0];
                            iArr5[1] = iArr5[1] + iArr6[1];
                            getParent().requestDisallowInterceptTouchEvent(true);
                            i4 = i16;
                            i3 = i17;
                        } else {
                            c = 1;
                            i4 = i2;
                            i3 = i;
                        }
                        int[] iArr7 = this.mScrollOffset;
                        this.mLastTouchX = i14 - iArr7[0];
                        this.mLastTouchY = i13 - iArr7[c];
                        int i19 = canScrollHorizontally ? i4 : 0;
                        if (canScrollVertically) {
                            i5 = i3;
                        }
                        if (scrollByInternal(i19, i5, motionEvent)) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        GapWorker gapWorker = this.mGapWorker;
                        if (!(gapWorker == null || (i4 == 0 && i3 == 0))) {
                            gapWorker.postFromTraversal(this, i4, i3);
                            break;
                        }
                    }
                } else {
                    Log.e(TAG, "Error processing scroll; pointer index for id " + this.mScrollPointerId + " not found. Did any MotionEvents get skipped?");
                    return false;
                }
            case 3:
                cancelScroll();
                break;
            case 5:
                this.mScrollPointerId = motionEvent.getPointerId(actionIndex);
                int x3 = (int) (motionEvent.getX(actionIndex) + 0.5f);
                this.mLastTouchX = x3;
                this.mInitialTouchX = x3;
                int y3 = (int) (motionEvent.getY(actionIndex) + 0.5f);
                this.mLastTouchY = y3;
                this.mInitialTouchY = y3;
                break;
            case 6:
                onPointerUp(e);
                break;
        }
        if (!z) {
            this.mVelocityTracker.addMovement(obtain);
        }
        obtain.recycle();
        return true;
    }

    /* access modifiers changed from: package-private */
    public void postAnimationRunner() {
        if (!this.mPostedAnimatorRunner && this.mIsAttached) {
            ViewCompat.postOnAnimation(this, this.mItemAnimatorRunner);
            this.mPostedAnimatorRunner = true;
        }
    }

    /* access modifiers changed from: package-private */
    public void processDataSetCompletelyChanged(boolean dispatchItemsChanged) {
        this.mDispatchItemsChangedEvent |= dispatchItemsChanged;
        this.mDataSetHasChangedAfterLayout = true;
        markKnownViewsInvalid();
    }

    /* access modifiers changed from: package-private */
    public void recordAnimationInfoIfBouncedHiddenView(ViewHolder viewHolder, ItemAnimator.ItemHolderInfo animationInfo) {
        viewHolder.setFlags(0, 8192);
        if (this.mState.mTrackOldChangeHolders && viewHolder.isUpdated() && !viewHolder.isRemoved() && !viewHolder.shouldIgnore()) {
            this.mViewInfoStore.addToOldChangeHolders(getChangedHolderKey(viewHolder), viewHolder);
        }
        this.mViewInfoStore.addToPreLayout(viewHolder, animationInfo);
    }

    /* access modifiers changed from: package-private */
    public void removeAndRecycleViews() {
        ItemAnimator itemAnimator = this.mItemAnimator;
        if (itemAnimator != null) {
            itemAnimator.endAnimations();
        }
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.removeAndRecycleAllViews(this.mRecycler);
            this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
        }
        this.mRecycler.clear();
    }

    /* access modifiers changed from: package-private */
    public boolean removeAnimatingView(View view) {
        startInterceptRequestLayout();
        boolean removeViewIfHidden = this.mChildHelper.removeViewIfHidden(view);
        if (removeViewIfHidden) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(view);
            this.mRecycler.unscrapView(childViewHolderInt);
            this.mRecycler.recycleViewHolderInternal(childViewHolderInt);
        }
        stopInterceptRequestLayout(!removeViewIfHidden);
        return removeViewIfHidden;
    }

    /* access modifiers changed from: protected */
    public void removeDetachedView(View child, boolean animate) {
        ViewHolder childViewHolderInt = getChildViewHolderInt(child);
        if (childViewHolderInt != null) {
            if (childViewHolderInt.isTmpDetached()) {
                childViewHolderInt.clearTmpDetachFlag();
            } else if (!childViewHolderInt.shouldIgnore()) {
                throw new IllegalArgumentException("Called removeDetachedView with a view which is not flagged as tmp detached." + childViewHolderInt + exceptionLabel());
            }
        }
        child.clearAnimation();
        dispatchChildDetached(child);
        super.removeDetachedView(child, animate);
    }

    public void removeItemDecoration(ItemDecoration decor) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager != null) {
            layoutManager.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or layout");
        }
        this.mItemDecorations.remove(decor);
        if (this.mItemDecorations.isEmpty()) {
            setWillNotDraw(getOverScrollMode() == 2);
        }
        markItemDecorInsetsDirty();
        requestLayout();
    }

    public void removeItemDecorationAt(int index) {
        int itemDecorationCount = getItemDecorationCount();
        if (index < 0 || index >= itemDecorationCount) {
            throw new IndexOutOfBoundsException(index + " is an invalid index for size " + itemDecorationCount);
        }
        removeItemDecoration(getItemDecorationAt(index));
    }

    public void removeOnChildAttachStateChangeListener(OnChildAttachStateChangeListener listener) {
        List<OnChildAttachStateChangeListener> list = this.mOnChildAttachStateListeners;
        if (list != null) {
            list.remove(listener);
        }
    }

    public void removeOnItemTouchListener(OnItemTouchListener listener) {
        this.mOnItemTouchListeners.remove(listener);
        if (this.mInterceptingOnItemTouchListener == listener) {
            this.mInterceptingOnItemTouchListener = null;
        }
    }

    public void removeOnScrollListener(OnScrollListener listener) {
        List<OnScrollListener> list = this.mScrollListeners;
        if (list != null) {
            list.remove(listener);
        }
    }

    /* access modifiers changed from: package-private */
    public void repositionShadowingViews() {
        int childCount = this.mChildHelper.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.mChildHelper.getChildAt(i);
            ViewHolder childViewHolder = getChildViewHolder(childAt);
            if (!(childViewHolder == null || childViewHolder.mShadowingHolder == null)) {
                View view = childViewHolder.mShadowingHolder.itemView;
                int left = childAt.getLeft();
                int top = childAt.getTop();
                if (left != view.getLeft() || top != view.getTop()) {
                    view.layout(left, top, view.getWidth() + left, view.getHeight() + top);
                }
            }
        }
    }

    public void requestChildFocus(View child, View focused) {
        if (!this.mLayout.onRequestChildFocus(this, this.mState, child, focused) && focused != null) {
            requestChildOnScreen(child, focused);
        }
        super.requestChildFocus(child, focused);
    }

    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        return this.mLayout.requestChildRectangleOnScreen(this, child, rect, immediate);
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        int size = this.mOnItemTouchListeners.size();
        for (int i = 0; i < size; i++) {
            this.mOnItemTouchListeners.get(i).onRequestDisallowInterceptTouchEvent(disallowIntercept);
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    public void requestLayout() {
        if (this.mInterceptRequestLayoutDepth != 0 || this.mLayoutSuppressed) {
            this.mLayoutWasDefered = true;
        } else {
            super.requestLayout();
        }
    }

    /* access modifiers changed from: package-private */
    public void saveOldPositions() {
        int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < unfilteredChildCount; i++) {
            ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!childViewHolderInt.shouldIgnore()) {
                childViewHolderInt.saveOldPosition();
            }
        }
    }

    public void scrollBy(int x, int y) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null) {
            Log.e(TAG, "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        } else if (!this.mLayoutSuppressed) {
            boolean canScrollHorizontally = layoutManager.canScrollHorizontally();
            boolean canScrollVertically = this.mLayout.canScrollVertically();
            if (canScrollHorizontally || canScrollVertically) {
                int i = 0;
                int i2 = canScrollHorizontally ? x : 0;
                if (canScrollVertically) {
                    i = y;
                }
                scrollByInternal(i2, i, (MotionEvent) null);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00b0, code lost:
        if (r3 != 0) goto L_0x00b5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean scrollByInternal(int r22, int r23, android.view.MotionEvent r24) {
        /*
            r21 = this;
            r8 = r21
            r9 = r22
            r10 = r23
            r11 = r24
            r0 = 0
            r1 = 0
            r2 = 0
            r3 = 0
            r21.consumePendingUpdateOperations()
            androidx.recyclerview.widget.RecyclerView$Adapter r4 = r8.mAdapter
            r12 = 1
            r13 = 0
            if (r4 == 0) goto L_0x002d
            int[] r4 = r8.mReusableIntPair
            r4[r13] = r13
            r4[r12] = r13
            r8.scrollStep(r9, r10, r4)
            int[] r4 = r8.mReusableIntPair
            r2 = r4[r13]
            r3 = r4[r12]
            int r0 = r9 - r2
            int r1 = r10 - r3
            r14 = r0
            r15 = r1
            r7 = r2
            r6 = r3
            goto L_0x0031
        L_0x002d:
            r14 = r0
            r15 = r1
            r7 = r2
            r6 = r3
        L_0x0031:
            java.util.ArrayList<androidx.recyclerview.widget.RecyclerView$ItemDecoration> r0 = r8.mItemDecorations
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L_0x003c
            r21.invalidate()
        L_0x003c:
            int[] r5 = r8.mReusableIntPair
            r5[r13] = r13
            r5[r12] = r13
            int[] r4 = r8.mScrollOffset
            r16 = 0
            r0 = r21
            r1 = r7
            r2 = r6
            r3 = r14
            r17 = r4
            r4 = r15
            r18 = r5
            r5 = r17
            r19 = r6
            r6 = r16
            r20 = r7
            r7 = r18
            r0.dispatchNestedScroll(r1, r2, r3, r4, r5, r6, r7)
            int[] r0 = r8.mReusableIntPair
            r1 = r0[r13]
            int r14 = r14 - r1
            r0 = r0[r12]
            int r15 = r15 - r0
            if (r1 != 0) goto L_0x006c
            if (r0 == 0) goto L_0x006a
            goto L_0x006c
        L_0x006a:
            r0 = r13
            goto L_0x006d
        L_0x006c:
            r0 = r12
        L_0x006d:
            int r1 = r8.mLastTouchX
            int[] r2 = r8.mScrollOffset
            r3 = r2[r13]
            int r1 = r1 - r3
            r8.mLastTouchX = r1
            int r1 = r8.mLastTouchY
            r2 = r2[r12]
            int r1 = r1 - r2
            r8.mLastTouchY = r1
            int[] r1 = r8.mNestedOffsets
            r4 = r1[r13]
            int r4 = r4 + r3
            r1[r13] = r4
            r3 = r1[r12]
            int r3 = r3 + r2
            r1[r12] = r3
            int r1 = r21.getOverScrollMode()
            r2 = 2
            if (r1 == r2) goto L_0x00aa
            if (r11 == 0) goto L_0x00a7
            r1 = 8194(0x2002, float:1.1482E-41)
            boolean r1 = androidx.core.view.MotionEventCompat.isFromSource(r11, r1)
            if (r1 != 0) goto L_0x00a7
            float r1 = r24.getX()
            float r2 = (float) r14
            float r3 = r24.getY()
            float r4 = (float) r15
            r8.pullGlows(r1, r2, r3, r4)
        L_0x00a7:
            r21.considerReleasingGlowsOnScroll(r22, r23)
        L_0x00aa:
            r2 = r20
            if (r2 != 0) goto L_0x00b3
            r3 = r19
            if (r3 == 0) goto L_0x00b8
            goto L_0x00b5
        L_0x00b3:
            r3 = r19
        L_0x00b5:
            r8.dispatchOnScrolled(r2, r3)
        L_0x00b8:
            boolean r1 = r21.awakenScrollBars()
            if (r1 != 0) goto L_0x00c1
            r21.invalidate()
        L_0x00c1:
            if (r0 != 0) goto L_0x00c9
            if (r2 != 0) goto L_0x00c9
            if (r3 == 0) goto L_0x00c8
            goto L_0x00c9
        L_0x00c8:
            r12 = r13
        L_0x00c9:
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.scrollByInternal(int, int, android.view.MotionEvent):boolean");
    }

    /* access modifiers changed from: package-private */
    public void scrollStep(int dx, int dy, int[] consumed) {
        startInterceptRequestLayout();
        onEnterLayoutOrScroll();
        TraceCompat.beginSection(TRACE_SCROLL_TAG);
        fillRemainingScrollValues(this.mState);
        int i = 0;
        int i2 = 0;
        if (dx != 0) {
            i = this.mLayout.scrollHorizontallyBy(dx, this.mRecycler, this.mState);
        }
        if (dy != 0) {
            i2 = this.mLayout.scrollVerticallyBy(dy, this.mRecycler, this.mState);
        }
        TraceCompat.endSection();
        repositionShadowingViews();
        onExitLayoutOrScroll();
        stopInterceptRequestLayout(false);
        if (consumed != null) {
            consumed[0] = i;
            consumed[1] = i2;
        }
    }

    public void scrollTo(int x, int y) {
        Log.w(TAG, "RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead");
    }

    public void scrollToPosition(int position) {
        if (!this.mLayoutSuppressed) {
            stopScroll();
            LayoutManager layoutManager = this.mLayout;
            if (layoutManager == null) {
                Log.e(TAG, "Cannot scroll to position a LayoutManager set. Call setLayoutManager with a non-null argument.");
                return;
            }
            layoutManager.scrollToPosition(position);
            awakenScrollBars();
        }
    }

    public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
        if (!shouldDeferAccessibilityEvent(event)) {
            super.sendAccessibilityEventUnchecked(event);
        }
    }

    public void setAccessibilityDelegateCompat(RecyclerViewAccessibilityDelegate accessibilityDelegate) {
        this.mAccessibilityDelegate = accessibilityDelegate;
        ViewCompat.setAccessibilityDelegate(this, accessibilityDelegate);
    }

    public void setAdapter(Adapter adapter) {
        setLayoutFrozen(false);
        setAdapterInternal(adapter, false, true);
        processDataSetCompletelyChanged(false);
        requestLayout();
    }

    public void setChildDrawingOrderCallback(ChildDrawingOrderCallback childDrawingOrderCallback) {
        if (childDrawingOrderCallback != this.mChildDrawingOrderCallback) {
            this.mChildDrawingOrderCallback = childDrawingOrderCallback;
            setChildrenDrawingOrderEnabled(childDrawingOrderCallback != null);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean setChildImportantForAccessibilityInternal(ViewHolder viewHolder, int importantForAccessibility) {
        if (isComputingLayout()) {
            viewHolder.mPendingAccessibilityState = importantForAccessibility;
            this.mPendingAccessibilityImportanceChange.add(viewHolder);
            return false;
        }
        ViewCompat.setImportantForAccessibility(viewHolder.itemView, importantForAccessibility);
        return true;
    }

    public void setClipToPadding(boolean clipToPadding) {
        if (clipToPadding != this.mClipToPadding) {
            invalidateGlows();
        }
        this.mClipToPadding = clipToPadding;
        super.setClipToPadding(clipToPadding);
        if (this.mFirstLayoutComplete) {
            requestLayout();
        }
    }

    public void setEdgeEffectFactory(EdgeEffectFactory edgeEffectFactory) {
        Preconditions.checkNotNull(edgeEffectFactory);
        this.mEdgeEffectFactory = edgeEffectFactory;
        invalidateGlows();
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        this.mHasFixedSize = hasFixedSize;
    }

    public void setItemAnimator(ItemAnimator animator) {
        ItemAnimator itemAnimator = this.mItemAnimator;
        if (itemAnimator != null) {
            itemAnimator.endAnimations();
            this.mItemAnimator.setListener((ItemAnimator.ItemAnimatorListener) null);
        }
        this.mItemAnimator = animator;
        if (animator != null) {
            animator.setListener(this.mItemAnimatorListener);
        }
    }

    public void setItemViewCacheSize(int size) {
        this.mRecycler.setViewCacheSize(size);
    }

    @Deprecated
    public void setLayoutFrozen(boolean frozen) {
        suppressLayout(frozen);
    }

    public void setLayoutManager(LayoutManager layout) {
        if (layout != this.mLayout) {
            stopScroll();
            if (this.mLayout != null) {
                ItemAnimator itemAnimator = this.mItemAnimator;
                if (itemAnimator != null) {
                    itemAnimator.endAnimations();
                }
                this.mLayout.removeAndRecycleAllViews(this.mRecycler);
                this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
                this.mRecycler.clear();
                if (this.mIsAttached) {
                    this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
                }
                this.mLayout.setRecyclerView((RecyclerView) null);
                this.mLayout = null;
            } else {
                this.mRecycler.clear();
            }
            this.mChildHelper.removeAllViewsUnfiltered();
            this.mLayout = layout;
            if (layout != null) {
                if (layout.mRecyclerView == null) {
                    this.mLayout.setRecyclerView(this);
                    if (this.mIsAttached) {
                        this.mLayout.dispatchAttachedToWindow(this);
                    }
                } else {
                    throw new IllegalArgumentException("LayoutManager " + layout + " is already attached to a RecyclerView:" + layout.mRecyclerView.exceptionLabel());
                }
            }
            this.mRecycler.updateViewCacheSize();
            requestLayout();
        }
    }

    @Deprecated
    public void setLayoutTransition(LayoutTransition transition) {
        if (Build.VERSION.SDK_INT < 18) {
            if (transition == null) {
                suppressLayout(false);
                return;
            } else if (transition.getAnimator(0) == null && transition.getAnimator(1) == null && transition.getAnimator(2) == null && transition.getAnimator(3) == null && transition.getAnimator(4) == null) {
                suppressLayout(true);
                return;
            }
        }
        if (transition == null) {
            super.setLayoutTransition((LayoutTransition) null);
            return;
        }
        throw new IllegalArgumentException("Providing a LayoutTransition into RecyclerView is not supported. Please use setItemAnimator() instead for animating changes to the items in this RecyclerView");
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        getScrollingChildHelper().setNestedScrollingEnabled(enabled);
    }

    public void setOnFlingListener(OnFlingListener onFlingListener) {
        this.mOnFlingListener = onFlingListener;
    }

    @Deprecated
    public void setOnScrollListener(OnScrollListener listener) {
        this.mScrollListener = listener;
    }

    public void setPreserveFocusAfterLayout(boolean preserveFocusAfterLayout) {
        this.mPreserveFocusAfterLayout = preserveFocusAfterLayout;
    }

    public void setRecycledViewPool(RecycledViewPool pool) {
        this.mRecycler.setRecycledViewPool(pool);
    }

    public void setRecyclerListener(RecyclerListener listener) {
        this.mRecyclerListener = listener;
    }

    /* access modifiers changed from: package-private */
    public void setScrollState(int state) {
        if (state != this.mScrollState) {
            this.mScrollState = state;
            if (state != 2) {
                stopScrollersInternal();
            }
            dispatchOnScrollStateChanged(state);
        }
    }

    public void setScrollingTouchSlop(int slopConstant) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        switch (slopConstant) {
            case 0:
                break;
            case 1:
                this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
                return;
            default:
                Log.w(TAG, "setScrollingTouchSlop(): bad argument constant " + slopConstant + "; using default value");
                break;
        }
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
    }

    public void setViewCacheExtension(ViewCacheExtension extension) {
        this.mRecycler.setViewCacheExtension(extension);
    }

    /* access modifiers changed from: package-private */
    public boolean shouldDeferAccessibilityEvent(AccessibilityEvent event) {
        if (!isComputingLayout()) {
            return false;
        }
        int i = 0;
        if (event != null) {
            i = AccessibilityEventCompat.getContentChangeTypes(event);
        }
        if (i == 0) {
            i = 0;
        }
        this.mEatenAccessibilityChangeFlags |= i;
        return true;
    }

    public void smoothScrollBy(int dx, int dy) {
        smoothScrollBy(dx, dy, (Interpolator) null);
    }

    public void smoothScrollBy(int dx, int dy, Interpolator interpolator) {
        smoothScrollBy(dx, dy, interpolator, Integer.MIN_VALUE);
    }

    public void smoothScrollBy(int dx, int dy, Interpolator interpolator, int duration) {
        smoothScrollBy(dx, dy, interpolator, duration, false);
    }

    /* access modifiers changed from: package-private */
    public void smoothScrollBy(int dx, int dy, Interpolator interpolator, int duration, boolean withNestedScrolling) {
        LayoutManager layoutManager = this.mLayout;
        if (layoutManager == null) {
            Log.e(TAG, "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        } else if (!this.mLayoutSuppressed) {
            if (!layoutManager.canScrollHorizontally()) {
                dx = 0;
            }
            if (!this.mLayout.canScrollVertically()) {
                dy = 0;
            }
            if (dx != 0 || dy != 0) {
                if (duration == Integer.MIN_VALUE || duration > 0) {
                    if (withNestedScrolling) {
                        int i = 0;
                        if (dx != 0) {
                            i = 0 | 1;
                        }
                        if (dy != 0) {
                            i |= 2;
                        }
                        startNestedScroll(i, 1);
                    }
                    this.mViewFlinger.smoothScrollBy(dx, dy, duration, interpolator);
                    return;
                }
                scrollBy(dx, dy);
            }
        }
    }

    public void smoothScrollToPosition(int position) {
        if (!this.mLayoutSuppressed) {
            LayoutManager layoutManager = this.mLayout;
            if (layoutManager == null) {
                Log.e(TAG, "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            } else {
                layoutManager.smoothScrollToPosition(this, this.mState, position);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void startInterceptRequestLayout() {
        int i = this.mInterceptRequestLayoutDepth + 1;
        this.mInterceptRequestLayoutDepth = i;
        if (i == 1 && !this.mLayoutSuppressed) {
            this.mLayoutWasDefered = false;
        }
    }

    public boolean startNestedScroll(int axes) {
        return getScrollingChildHelper().startNestedScroll(axes);
    }

    public boolean startNestedScroll(int axes, int type) {
        return getScrollingChildHelper().startNestedScroll(axes, type);
    }

    /* access modifiers changed from: package-private */
    public void stopInterceptRequestLayout(boolean performLayoutChildren) {
        if (this.mInterceptRequestLayoutDepth < 1) {
            this.mInterceptRequestLayoutDepth = 1;
        }
        if (!performLayoutChildren && !this.mLayoutSuppressed) {
            this.mLayoutWasDefered = false;
        }
        if (this.mInterceptRequestLayoutDepth == 1) {
            if (performLayoutChildren && this.mLayoutWasDefered && !this.mLayoutSuppressed && this.mLayout != null && this.mAdapter != null) {
                dispatchLayout();
            }
            if (!this.mLayoutSuppressed) {
                this.mLayoutWasDefered = false;
            }
        }
        this.mInterceptRequestLayoutDepth--;
    }

    public void stopNestedScroll() {
        getScrollingChildHelper().stopNestedScroll();
    }

    public void stopNestedScroll(int type) {
        getScrollingChildHelper().stopNestedScroll(type);
    }

    public void stopScroll() {
        setScrollState(0);
        stopScrollersInternal();
    }

    public final void suppressLayout(boolean suppress) {
        if (suppress != this.mLayoutSuppressed) {
            assertNotInLayoutOrScroll("Do not suppressLayout in layout or scroll");
            if (!suppress) {
                this.mLayoutSuppressed = false;
                if (!(!this.mLayoutWasDefered || this.mLayout == null || this.mAdapter == null)) {
                    requestLayout();
                }
                this.mLayoutWasDefered = false;
                return;
            }
            long uptimeMillis = SystemClock.uptimeMillis();
            onTouchEvent(MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0));
            this.mLayoutSuppressed = true;
            this.mIgnoreMotionEventTillDown = true;
            stopScroll();
        }
    }

    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        setLayoutFrozen(false);
        setAdapterInternal(adapter, true, removeAndRecycleExistingViews);
        processDataSetCompletelyChanged(true);
        requestLayout();
    }

    /* access modifiers changed from: package-private */
    public void viewRangeUpdate(int positionStart, int itemCount, Object payload) {
        int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        int i = positionStart + itemCount;
        for (int i2 = 0; i2 < unfilteredChildCount; i2++) {
            View unfilteredChildAt = this.mChildHelper.getUnfilteredChildAt(i2);
            ViewHolder childViewHolderInt = getChildViewHolderInt(unfilteredChildAt);
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore() && childViewHolderInt.mPosition >= positionStart && childViewHolderInt.mPosition < i) {
                childViewHolderInt.addFlags(2);
                childViewHolderInt.addChangePayload(payload);
                ((LayoutParams) unfilteredChildAt.getLayoutParams()).mInsetsDirty = true;
            }
        }
        this.mRecycler.viewRangeUpdate(positionStart, itemCount);
    }
}

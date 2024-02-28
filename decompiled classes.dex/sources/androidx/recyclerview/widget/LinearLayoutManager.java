package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LinearLayoutManager extends RecyclerView.LayoutManager implements ItemTouchHelper.ViewDropHandler, RecyclerView.SmoothScroller.ScrollVectorProvider {
    static final boolean DEBUG = false;
    public static final int HORIZONTAL = 0;
    public static final int INVALID_OFFSET = Integer.MIN_VALUE;
    private static final float MAX_SCROLL_FACTOR = 0.33333334f;
    private static final String TAG = "LinearLayoutManager";
    public static final int VERTICAL = 1;
    final AnchorInfo mAnchorInfo;
    private int mInitialPrefetchItemCount;
    private boolean mLastStackFromEnd;
    private final LayoutChunkResult mLayoutChunkResult;
    private LayoutState mLayoutState;
    int mOrientation;
    OrientationHelper mOrientationHelper;
    SavedState mPendingSavedState;
    int mPendingScrollPosition;
    int mPendingScrollPositionOffset;
    private boolean mRecycleChildrenOnDetach;
    private int[] mReusableIntPair;
    private boolean mReverseLayout;
    boolean mShouldReverseLayout;
    private boolean mSmoothScrollbarEnabled;
    private boolean mStackFromEnd;

    static class AnchorInfo {
        int mCoordinate;
        boolean mLayoutFromEnd;
        OrientationHelper mOrientationHelper;
        int mPosition;
        boolean mValid;

        AnchorInfo() {
            reset();
        }

        /* access modifiers changed from: package-private */
        public void assignCoordinateFromPadding() {
            this.mCoordinate = this.mLayoutFromEnd ? this.mOrientationHelper.getEndAfterPadding() : this.mOrientationHelper.getStartAfterPadding();
        }

        public void assignFromView(View child, int position) {
            if (this.mLayoutFromEnd) {
                this.mCoordinate = this.mOrientationHelper.getDecoratedEnd(child) + this.mOrientationHelper.getTotalSpaceChange();
            } else {
                this.mCoordinate = this.mOrientationHelper.getDecoratedStart(child);
            }
            this.mPosition = position;
        }

        public void assignFromViewAndKeepVisibleRect(View child, int position) {
            int endAfterPadding;
            int totalSpaceChange = this.mOrientationHelper.getTotalSpaceChange();
            if (totalSpaceChange >= 0) {
                assignFromView(child, position);
                return;
            }
            this.mPosition = position;
            if (this.mLayoutFromEnd) {
                int endAfterPadding2 = (this.mOrientationHelper.getEndAfterPadding() - totalSpaceChange) - this.mOrientationHelper.getDecoratedEnd(child);
                this.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - endAfterPadding2;
                if (endAfterPadding2 > 0) {
                    int decoratedMeasurement = this.mOrientationHelper.getDecoratedMeasurement(child);
                    int startAfterPadding = this.mOrientationHelper.getStartAfterPadding();
                    int min = (this.mCoordinate - decoratedMeasurement) - (Math.min(this.mOrientationHelper.getDecoratedStart(child) - startAfterPadding, 0) + startAfterPadding);
                    if (min < 0) {
                        this.mCoordinate += Math.min(endAfterPadding2, -min);
                        return;
                    }
                    return;
                }
                return;
            }
            int decoratedStart = this.mOrientationHelper.getDecoratedStart(child);
            int startAfterPadding2 = decoratedStart - this.mOrientationHelper.getStartAfterPadding();
            this.mCoordinate = decoratedStart;
            if (startAfterPadding2 > 0 && (endAfterPadding = (this.mOrientationHelper.getEndAfterPadding() - Math.min(0, (this.mOrientationHelper.getEndAfterPadding() - totalSpaceChange) - this.mOrientationHelper.getDecoratedEnd(child))) - (this.mOrientationHelper.getDecoratedMeasurement(child) + decoratedStart)) < 0) {
                this.mCoordinate -= Math.min(startAfterPadding2, -endAfterPadding);
            }
        }

        /* access modifiers changed from: package-private */
        public boolean isViewValidAsAnchor(View child, RecyclerView.State state) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            return !layoutParams.isItemRemoved() && layoutParams.getViewLayoutPosition() >= 0 && layoutParams.getViewLayoutPosition() < state.getItemCount();
        }

        /* access modifiers changed from: package-private */
        public void reset() {
            this.mPosition = -1;
            this.mCoordinate = Integer.MIN_VALUE;
            this.mLayoutFromEnd = false;
            this.mValid = false;
        }

        public String toString() {
            return "AnchorInfo{mPosition=" + this.mPosition + ", mCoordinate=" + this.mCoordinate + ", mLayoutFromEnd=" + this.mLayoutFromEnd + ", mValid=" + this.mValid + '}';
        }
    }

    protected static class LayoutChunkResult {
        public int mConsumed;
        public boolean mFinished;
        public boolean mFocusable;
        public boolean mIgnoreConsumed;

        protected LayoutChunkResult() {
        }

        /* access modifiers changed from: package-private */
        public void resetInternal() {
            this.mConsumed = 0;
            this.mFinished = false;
            this.mIgnoreConsumed = false;
            this.mFocusable = false;
        }
    }

    static class LayoutState {
        static final int INVALID_LAYOUT = Integer.MIN_VALUE;
        static final int ITEM_DIRECTION_HEAD = -1;
        static final int ITEM_DIRECTION_TAIL = 1;
        static final int LAYOUT_END = 1;
        static final int LAYOUT_START = -1;
        static final int SCROLLING_OFFSET_NaN = Integer.MIN_VALUE;
        static final String TAG = "LLM#LayoutState";
        int mAvailable;
        int mCurrentPosition;
        int mExtraFillSpace = 0;
        boolean mInfinite;
        boolean mIsPreLayout = false;
        int mItemDirection;
        int mLastScrollDelta;
        int mLayoutDirection;
        int mNoRecycleSpace = 0;
        int mOffset;
        boolean mRecycle = true;
        List<RecyclerView.ViewHolder> mScrapList = null;
        int mScrollingOffset;

        LayoutState() {
        }

        private View nextViewFromScrapList() {
            int size = this.mScrapList.size();
            for (int i = 0; i < size; i++) {
                View view = this.mScrapList.get(i).itemView;
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                if (!layoutParams.isItemRemoved() && this.mCurrentPosition == layoutParams.getViewLayoutPosition()) {
                    assignPositionFromScrapList(view);
                    return view;
                }
            }
            return null;
        }

        public void assignPositionFromScrapList() {
            assignPositionFromScrapList((View) null);
        }

        public void assignPositionFromScrapList(View ignore) {
            View nextViewInLimitedList = nextViewInLimitedList(ignore);
            if (nextViewInLimitedList == null) {
                this.mCurrentPosition = -1;
            } else {
                this.mCurrentPosition = ((RecyclerView.LayoutParams) nextViewInLimitedList.getLayoutParams()).getViewLayoutPosition();
            }
        }

        /* access modifiers changed from: package-private */
        public boolean hasMore(RecyclerView.State state) {
            int i = this.mCurrentPosition;
            return i >= 0 && i < state.getItemCount();
        }

        /* access modifiers changed from: package-private */
        public void log() {
            Log.d(TAG, "avail:" + this.mAvailable + ", ind:" + this.mCurrentPosition + ", dir:" + this.mItemDirection + ", offset:" + this.mOffset + ", layoutDir:" + this.mLayoutDirection);
        }

        /* access modifiers changed from: package-private */
        public View next(RecyclerView.Recycler recycler) {
            if (this.mScrapList != null) {
                return nextViewFromScrapList();
            }
            View viewForPosition = recycler.getViewForPosition(this.mCurrentPosition);
            this.mCurrentPosition += this.mItemDirection;
            return viewForPosition;
        }

        public View nextViewInLimitedList(View ignore) {
            int viewLayoutPosition;
            int size = this.mScrapList.size();
            View view = null;
            int i = Integer.MAX_VALUE;
            for (int i2 = 0; i2 < size; i2++) {
                View view2 = this.mScrapList.get(i2).itemView;
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view2.getLayoutParams();
                if (view2 != ignore && !layoutParams.isItemRemoved() && (viewLayoutPosition = (layoutParams.getViewLayoutPosition() - this.mCurrentPosition) * this.mItemDirection) >= 0 && viewLayoutPosition < i) {
                    view = view2;
                    i = viewLayoutPosition;
                    if (viewLayoutPosition == 0) {
                        break;
                    }
                }
            }
            return view;
        }
    }

    public static class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        boolean mAnchorLayoutFromEnd;
        int mAnchorOffset;
        int mAnchorPosition;

        public SavedState() {
        }

        SavedState(Parcel in) {
            this.mAnchorPosition = in.readInt();
            this.mAnchorOffset = in.readInt();
            this.mAnchorLayoutFromEnd = in.readInt() != 1 ? false : true;
        }

        public SavedState(SavedState other) {
            this.mAnchorPosition = other.mAnchorPosition;
            this.mAnchorOffset = other.mAnchorOffset;
            this.mAnchorLayoutFromEnd = other.mAnchorLayoutFromEnd;
        }

        public int describeContents() {
            return 0;
        }

        /* access modifiers changed from: package-private */
        public boolean hasValidAnchor() {
            return this.mAnchorPosition >= 0;
        }

        /* access modifiers changed from: package-private */
        public void invalidateAnchor() {
            this.mAnchorPosition = -1;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mAnchorPosition);
            dest.writeInt(this.mAnchorOffset);
            dest.writeInt(this.mAnchorLayoutFromEnd ? 1 : 0);
        }
    }

    public LinearLayoutManager(Context context) {
        this(context, 1, false);
    }

    public LinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        this.mOrientation = 1;
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mStackFromEnd = false;
        this.mSmoothScrollbarEnabled = true;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mPendingSavedState = null;
        this.mAnchorInfo = new AnchorInfo();
        this.mLayoutChunkResult = new LayoutChunkResult();
        this.mInitialPrefetchItemCount = 2;
        this.mReusableIntPair = new int[2];
        setOrientation(orientation);
        setReverseLayout(reverseLayout);
    }

    public LinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.mOrientation = 1;
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mStackFromEnd = false;
        this.mSmoothScrollbarEnabled = true;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mPendingSavedState = null;
        this.mAnchorInfo = new AnchorInfo();
        this.mLayoutChunkResult = new LayoutChunkResult();
        this.mInitialPrefetchItemCount = 2;
        this.mReusableIntPair = new int[2];
        RecyclerView.LayoutManager.Properties properties = getProperties(context, attrs, defStyleAttr, defStyleRes);
        setOrientation(properties.orientation);
        setReverseLayout(properties.reverseLayout);
        setStackFromEnd(properties.stackFromEnd);
    }

    private int computeScrollExtent(RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }
        ensureLayoutState();
        return ScrollbarHelper.computeScrollExtent(state, this.mOrientationHelper, findFirstVisibleChildClosestToStart(!this.mSmoothScrollbarEnabled, true), findFirstVisibleChildClosestToEnd(!this.mSmoothScrollbarEnabled, true), this, this.mSmoothScrollbarEnabled);
    }

    private int computeScrollOffset(RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }
        ensureLayoutState();
        return ScrollbarHelper.computeScrollOffset(state, this.mOrientationHelper, findFirstVisibleChildClosestToStart(!this.mSmoothScrollbarEnabled, true), findFirstVisibleChildClosestToEnd(!this.mSmoothScrollbarEnabled, true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
    }

    private int computeScrollRange(RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }
        ensureLayoutState();
        return ScrollbarHelper.computeScrollRange(state, this.mOrientationHelper, findFirstVisibleChildClosestToStart(!this.mSmoothScrollbarEnabled, true), findFirstVisibleChildClosestToEnd(!this.mSmoothScrollbarEnabled, true), this, this.mSmoothScrollbarEnabled);
    }

    private View findFirstPartiallyOrCompletelyInvisibleChild() {
        return findOnePartiallyOrCompletelyInvisibleChild(0, getChildCount());
    }

    private View findFirstReferenceChild(RecyclerView.Recycler recycler, RecyclerView.State state) {
        return findReferenceChild(recycler, state, 0, getChildCount(), state.getItemCount());
    }

    private View findLastPartiallyOrCompletelyInvisibleChild() {
        return findOnePartiallyOrCompletelyInvisibleChild(getChildCount() - 1, -1);
    }

    private View findLastReferenceChild(RecyclerView.Recycler recycler, RecyclerView.State state) {
        return findReferenceChild(recycler, state, getChildCount() - 1, -1, state.getItemCount());
    }

    private View findPartiallyOrCompletelyInvisibleChildClosestToEnd() {
        return this.mShouldReverseLayout ? findFirstPartiallyOrCompletelyInvisibleChild() : findLastPartiallyOrCompletelyInvisibleChild();
    }

    private View findPartiallyOrCompletelyInvisibleChildClosestToStart() {
        return this.mShouldReverseLayout ? findLastPartiallyOrCompletelyInvisibleChild() : findFirstPartiallyOrCompletelyInvisibleChild();
    }

    private View findReferenceChildClosestToEnd(RecyclerView.Recycler recycler, RecyclerView.State state) {
        return this.mShouldReverseLayout ? findFirstReferenceChild(recycler, state) : findLastReferenceChild(recycler, state);
    }

    private View findReferenceChildClosestToStart(RecyclerView.Recycler recycler, RecyclerView.State state) {
        return this.mShouldReverseLayout ? findLastReferenceChild(recycler, state) : findFirstReferenceChild(recycler, state);
    }

    private int fixLayoutEndGap(int endOffset, RecyclerView.Recycler recycler, RecyclerView.State state, boolean canOffsetChildren) {
        int endAfterPadding;
        int endAfterPadding2 = this.mOrientationHelper.getEndAfterPadding() - endOffset;
        if (endAfterPadding2 <= 0) {
            return 0;
        }
        int i = -scrollBy(-endAfterPadding2, recycler, state);
        int endOffset2 = endOffset + i;
        if (!canOffsetChildren || (endAfterPadding = this.mOrientationHelper.getEndAfterPadding() - endOffset2) <= 0) {
            return i;
        }
        this.mOrientationHelper.offsetChildren(endAfterPadding);
        return endAfterPadding + i;
    }

    private int fixLayoutStartGap(int startOffset, RecyclerView.Recycler recycler, RecyclerView.State state, boolean canOffsetChildren) {
        int startAfterPadding;
        int startAfterPadding2 = startOffset - this.mOrientationHelper.getStartAfterPadding();
        if (startAfterPadding2 <= 0) {
            return 0;
        }
        int i = -scrollBy(startAfterPadding2, recycler, state);
        int startOffset2 = startOffset + i;
        if (!canOffsetChildren || (startAfterPadding = startOffset2 - this.mOrientationHelper.getStartAfterPadding()) <= 0) {
            return i;
        }
        this.mOrientationHelper.offsetChildren(-startAfterPadding);
        return i - startAfterPadding;
    }

    private View getChildClosestToEnd() {
        return getChildAt(this.mShouldReverseLayout ? 0 : getChildCount() - 1);
    }

    private View getChildClosestToStart() {
        return getChildAt(this.mShouldReverseLayout ? getChildCount() - 1 : 0);
    }

    private void layoutForPredictiveAnimations(RecyclerView.Recycler recycler, RecyclerView.State state, int startOffset, int endOffset) {
        RecyclerView.Recycler recycler2 = recycler;
        RecyclerView.State state2 = state;
        if (!state.willRunPredictiveAnimations() || getChildCount() == 0 || state.isPreLayout()) {
            int i = startOffset;
            int i2 = endOffset;
        } else if (!supportsPredictiveItemAnimations()) {
            int i3 = startOffset;
            int i4 = endOffset;
        } else {
            int i5 = 0;
            int i6 = 0;
            List<RecyclerView.ViewHolder> scrapList = recycler.getScrapList();
            int size = scrapList.size();
            int position = getPosition(getChildAt(0));
            for (int i7 = 0; i7 < size; i7++) {
                RecyclerView.ViewHolder viewHolder = scrapList.get(i7);
                if (!viewHolder.isRemoved()) {
                    boolean z = true;
                    if ((viewHolder.getLayoutPosition() < position) != this.mShouldReverseLayout) {
                        z = true;
                    }
                    if (z) {
                        i5 += this.mOrientationHelper.getDecoratedMeasurement(viewHolder.itemView);
                    } else {
                        i6 += this.mOrientationHelper.getDecoratedMeasurement(viewHolder.itemView);
                    }
                }
            }
            this.mLayoutState.mScrapList = scrapList;
            if (i5 > 0) {
                updateLayoutStateToFillStart(getPosition(getChildClosestToStart()), startOffset);
                this.mLayoutState.mExtraFillSpace = i5;
                this.mLayoutState.mAvailable = 0;
                this.mLayoutState.assignPositionFromScrapList();
                fill(recycler2, this.mLayoutState, state2, false);
            } else {
                int i8 = startOffset;
            }
            if (i6 > 0) {
                updateLayoutStateToFillEnd(getPosition(getChildClosestToEnd()), endOffset);
                this.mLayoutState.mExtraFillSpace = i6;
                this.mLayoutState.mAvailable = 0;
                this.mLayoutState.assignPositionFromScrapList();
                fill(recycler2, this.mLayoutState, state2, false);
            } else {
                int i9 = endOffset;
            }
            this.mLayoutState.mScrapList = null;
        }
    }

    private void logChildren() {
        Log.d(TAG, "internal representation of views on the screen");
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            Log.d(TAG, "item " + getPosition(childAt) + ", coord:" + this.mOrientationHelper.getDecoratedStart(childAt));
        }
        Log.d(TAG, "==============");
    }

    private void recycleByLayoutState(RecyclerView.Recycler recycler, LayoutState layoutState) {
        if (layoutState.mRecycle && !layoutState.mInfinite) {
            int i = layoutState.mScrollingOffset;
            int i2 = layoutState.mNoRecycleSpace;
            if (layoutState.mLayoutDirection == -1) {
                recycleViewsFromEnd(recycler, i, i2);
            } else {
                recycleViewsFromStart(recycler, i, i2);
            }
        }
    }

    private void recycleChildren(RecyclerView.Recycler recycler, int startIndex, int endIndex) {
        if (startIndex != endIndex) {
            if (endIndex > startIndex) {
                for (int i = endIndex - 1; i >= startIndex; i--) {
                    removeAndRecycleViewAt(i, recycler);
                }
                return;
            }
            for (int i2 = startIndex; i2 > endIndex; i2--) {
                removeAndRecycleViewAt(i2, recycler);
            }
        }
    }

    private void recycleViewsFromEnd(RecyclerView.Recycler recycler, int scrollingOffset, int noRecycleSpace) {
        int childCount = getChildCount();
        if (scrollingOffset >= 0) {
            int end = (this.mOrientationHelper.getEnd() - scrollingOffset) + noRecycleSpace;
            if (this.mShouldReverseLayout) {
                for (int i = 0; i < childCount; i++) {
                    View childAt = getChildAt(i);
                    if (this.mOrientationHelper.getDecoratedStart(childAt) < end || this.mOrientationHelper.getTransformedStartWithDecoration(childAt) < end) {
                        recycleChildren(recycler, 0, i);
                        return;
                    }
                }
                return;
            }
            for (int i2 = childCount - 1; i2 >= 0; i2--) {
                View childAt2 = getChildAt(i2);
                if (this.mOrientationHelper.getDecoratedStart(childAt2) < end || this.mOrientationHelper.getTransformedStartWithDecoration(childAt2) < end) {
                    recycleChildren(recycler, childCount - 1, i2);
                    return;
                }
            }
        }
    }

    private void recycleViewsFromStart(RecyclerView.Recycler recycler, int scrollingOffset, int noRecycleSpace) {
        if (scrollingOffset >= 0) {
            int i = scrollingOffset - noRecycleSpace;
            int childCount = getChildCount();
            if (this.mShouldReverseLayout) {
                for (int i2 = childCount - 1; i2 >= 0; i2--) {
                    View childAt = getChildAt(i2);
                    if (this.mOrientationHelper.getDecoratedEnd(childAt) > i || this.mOrientationHelper.getTransformedEndWithDecoration(childAt) > i) {
                        recycleChildren(recycler, childCount - 1, i2);
                        return;
                    }
                }
                return;
            }
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt2 = getChildAt(i3);
                if (this.mOrientationHelper.getDecoratedEnd(childAt2) > i || this.mOrientationHelper.getTransformedEndWithDecoration(childAt2) > i) {
                    recycleChildren(recycler, 0, i3);
                    return;
                }
            }
        }
    }

    private void resolveShouldLayoutReverse() {
        if (this.mOrientation == 1 || !isLayoutRTL()) {
            this.mShouldReverseLayout = this.mReverseLayout;
        } else {
            this.mShouldReverseLayout = !this.mReverseLayout;
        }
    }

    private boolean updateAnchorFromChildren(RecyclerView.Recycler recycler, RecyclerView.State state, AnchorInfo anchorInfo) {
        boolean z = false;
        if (getChildCount() == 0) {
            return false;
        }
        View focusedChild = getFocusedChild();
        if (focusedChild != null && anchorInfo.isViewValidAsAnchor(focusedChild, state)) {
            anchorInfo.assignFromViewAndKeepVisibleRect(focusedChild, getPosition(focusedChild));
            return true;
        } else if (this.mLastStackFromEnd != this.mStackFromEnd) {
            return false;
        } else {
            View findReferenceChildClosestToEnd = anchorInfo.mLayoutFromEnd ? findReferenceChildClosestToEnd(recycler, state) : findReferenceChildClosestToStart(recycler, state);
            if (findReferenceChildClosestToEnd == null) {
                return false;
            }
            anchorInfo.assignFromView(findReferenceChildClosestToEnd, getPosition(findReferenceChildClosestToEnd));
            if (!state.isPreLayout() && supportsPredictiveItemAnimations()) {
                if (this.mOrientationHelper.getDecoratedStart(findReferenceChildClosestToEnd) >= this.mOrientationHelper.getEndAfterPadding() || this.mOrientationHelper.getDecoratedEnd(findReferenceChildClosestToEnd) < this.mOrientationHelper.getStartAfterPadding()) {
                    z = true;
                }
                if (z) {
                    anchorInfo.mCoordinate = anchorInfo.mLayoutFromEnd ? this.mOrientationHelper.getEndAfterPadding() : this.mOrientationHelper.getStartAfterPadding();
                }
            }
            return true;
        }
    }

    private boolean updateAnchorFromPendingData(RecyclerView.State state, AnchorInfo anchorInfo) {
        int i;
        boolean z = false;
        if (state.isPreLayout() || (i = this.mPendingScrollPosition) == -1) {
            return false;
        }
        if (i < 0 || i >= state.getItemCount()) {
            this.mPendingScrollPosition = -1;
            this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
            return false;
        }
        anchorInfo.mPosition = this.mPendingScrollPosition;
        SavedState savedState = this.mPendingSavedState;
        if (savedState != null && savedState.hasValidAnchor()) {
            anchorInfo.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
            if (anchorInfo.mLayoutFromEnd) {
                anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingSavedState.mAnchorOffset;
            } else {
                anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingSavedState.mAnchorOffset;
            }
            return true;
        } else if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) {
            View findViewByPosition = findViewByPosition(this.mPendingScrollPosition);
            if (findViewByPosition == null) {
                if (getChildCount() > 0) {
                    if ((this.mPendingScrollPosition < getPosition(getChildAt(0))) == this.mShouldReverseLayout) {
                        z = true;
                    }
                    anchorInfo.mLayoutFromEnd = z;
                }
                anchorInfo.assignCoordinateFromPadding();
            } else if (this.mOrientationHelper.getDecoratedMeasurement(findViewByPosition) > this.mOrientationHelper.getTotalSpace()) {
                anchorInfo.assignCoordinateFromPadding();
                return true;
            } else if (this.mOrientationHelper.getDecoratedStart(findViewByPosition) - this.mOrientationHelper.getStartAfterPadding() < 0) {
                anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding();
                anchorInfo.mLayoutFromEnd = false;
                return true;
            } else if (this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(findViewByPosition) < 0) {
                anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding();
                anchorInfo.mLayoutFromEnd = true;
                return true;
            } else {
                anchorInfo.mCoordinate = anchorInfo.mLayoutFromEnd ? this.mOrientationHelper.getDecoratedEnd(findViewByPosition) + this.mOrientationHelper.getTotalSpaceChange() : this.mOrientationHelper.getDecoratedStart(findViewByPosition);
            }
            return true;
        } else {
            anchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
            if (this.mShouldReverseLayout) {
                anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingScrollPositionOffset;
            } else {
                anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingScrollPositionOffset;
            }
            return true;
        }
    }

    private void updateAnchorInfoForLayout(RecyclerView.Recycler recycler, RecyclerView.State state, AnchorInfo anchorInfo) {
        if (!updateAnchorFromPendingData(state, anchorInfo) && !updateAnchorFromChildren(recycler, state, anchorInfo)) {
            anchorInfo.assignCoordinateFromPadding();
            anchorInfo.mPosition = this.mStackFromEnd ? state.getItemCount() - 1 : 0;
        }
    }

    private void updateLayoutState(int layoutDirection, int requiredSpace, boolean canUseExistingSpace, RecyclerView.State state) {
        int i;
        this.mLayoutState.mInfinite = resolveIsInfinite();
        this.mLayoutState.mLayoutDirection = layoutDirection;
        int[] iArr = this.mReusableIntPair;
        boolean z = false;
        iArr[0] = 0;
        int i2 = 1;
        iArr[1] = 0;
        calculateExtraLayoutSpace(state, iArr);
        int max = Math.max(0, this.mReusableIntPair[0]);
        int max2 = Math.max(0, this.mReusableIntPair[1]);
        if (layoutDirection == 1) {
            z = true;
        }
        this.mLayoutState.mExtraFillSpace = z ? max2 : max;
        this.mLayoutState.mNoRecycleSpace = z ? max : max2;
        if (z) {
            this.mLayoutState.mExtraFillSpace += this.mOrientationHelper.getEndPadding();
            View childClosestToEnd = getChildClosestToEnd();
            LayoutState layoutState = this.mLayoutState;
            if (this.mShouldReverseLayout) {
                i2 = -1;
            }
            layoutState.mItemDirection = i2;
            this.mLayoutState.mCurrentPosition = getPosition(childClosestToEnd) + this.mLayoutState.mItemDirection;
            this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedEnd(childClosestToEnd);
            i = this.mOrientationHelper.getDecoratedEnd(childClosestToEnd) - this.mOrientationHelper.getEndAfterPadding();
        } else {
            View childClosestToStart = getChildClosestToStart();
            this.mLayoutState.mExtraFillSpace += this.mOrientationHelper.getStartAfterPadding();
            LayoutState layoutState2 = this.mLayoutState;
            if (!this.mShouldReverseLayout) {
                i2 = -1;
            }
            layoutState2.mItemDirection = i2;
            this.mLayoutState.mCurrentPosition = getPosition(childClosestToStart) + this.mLayoutState.mItemDirection;
            this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedStart(childClosestToStart);
            i = (-this.mOrientationHelper.getDecoratedStart(childClosestToStart)) + this.mOrientationHelper.getStartAfterPadding();
        }
        this.mLayoutState.mAvailable = requiredSpace;
        if (canUseExistingSpace) {
            this.mLayoutState.mAvailable -= i;
        }
        this.mLayoutState.mScrollingOffset = i;
    }

    private void updateLayoutStateToFillEnd(int itemPosition, int offset) {
        this.mLayoutState.mAvailable = this.mOrientationHelper.getEndAfterPadding() - offset;
        this.mLayoutState.mItemDirection = this.mShouldReverseLayout ? -1 : 1;
        this.mLayoutState.mCurrentPosition = itemPosition;
        this.mLayoutState.mLayoutDirection = 1;
        this.mLayoutState.mOffset = offset;
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
    }

    private void updateLayoutStateToFillEnd(AnchorInfo anchorInfo) {
        updateLayoutStateToFillEnd(anchorInfo.mPosition, anchorInfo.mCoordinate);
    }

    private void updateLayoutStateToFillStart(int itemPosition, int offset) {
        this.mLayoutState.mAvailable = offset - this.mOrientationHelper.getStartAfterPadding();
        this.mLayoutState.mCurrentPosition = itemPosition;
        this.mLayoutState.mItemDirection = this.mShouldReverseLayout ? 1 : -1;
        this.mLayoutState.mLayoutDirection = -1;
        this.mLayoutState.mOffset = offset;
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
    }

    private void updateLayoutStateToFillStart(AnchorInfo anchorInfo) {
        updateLayoutStateToFillStart(anchorInfo.mPosition, anchorInfo.mCoordinate);
    }

    public void assertNotInLayoutOrScroll(String message) {
        if (this.mPendingSavedState == null) {
            super.assertNotInLayoutOrScroll(message);
        }
    }

    /* access modifiers changed from: protected */
    public void calculateExtraLayoutSpace(RecyclerView.State state, int[] extraLayoutSpace) {
        int i = 0;
        int i2 = 0;
        int extraLayoutSpace2 = getExtraLayoutSpace(state);
        if (this.mLayoutState.mLayoutDirection == -1) {
            i = extraLayoutSpace2;
        } else {
            i2 = extraLayoutSpace2;
        }
        extraLayoutSpace[0] = i;
        extraLayoutSpace[1] = i2;
    }

    public boolean canScrollHorizontally() {
        return this.mOrientation == 0;
    }

    public boolean canScrollVertically() {
        return this.mOrientation == 1;
    }

    public void collectAdjacentPrefetchPositions(int dx, int dy, RecyclerView.State state, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int i = this.mOrientation == 0 ? dx : dy;
        if (getChildCount() != 0 && i != 0) {
            ensureLayoutState();
            updateLayoutState(i > 0 ? 1 : -1, Math.abs(i), true, state);
            collectPrefetchPositionsForLayoutState(state, this.mLayoutState, layoutPrefetchRegistry);
        }
    }

    public void collectInitialPrefetchPositions(int adapterItemCount, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int i;
        boolean z;
        SavedState savedState = this.mPendingSavedState;
        int i2 = -1;
        if (savedState == null || !savedState.hasValidAnchor()) {
            resolveShouldLayoutReverse();
            z = this.mShouldReverseLayout;
            i = this.mPendingScrollPosition == -1 ? z ? adapterItemCount - 1 : 0 : this.mPendingScrollPosition;
        } else {
            z = this.mPendingSavedState.mAnchorLayoutFromEnd;
            i = this.mPendingSavedState.mAnchorPosition;
        }
        if (!z) {
            i2 = 1;
        }
        int i3 = i;
        for (int i4 = 0; i4 < this.mInitialPrefetchItemCount && i3 >= 0 && i3 < adapterItemCount; i4++) {
            layoutPrefetchRegistry.addPosition(i3, 0);
            i3 += i2;
        }
    }

    /* access modifiers changed from: package-private */
    public void collectPrefetchPositionsForLayoutState(RecyclerView.State state, LayoutState layoutState, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int i = layoutState.mCurrentPosition;
        if (i >= 0 && i < state.getItemCount()) {
            layoutPrefetchRegistry.addPosition(i, Math.max(0, layoutState.mScrollingOffset));
        }
    }

    public int computeHorizontalScrollExtent(RecyclerView.State state) {
        return computeScrollExtent(state);
    }

    public int computeHorizontalScrollOffset(RecyclerView.State state) {
        return computeScrollOffset(state);
    }

    public int computeHorizontalScrollRange(RecyclerView.State state) {
        return computeScrollRange(state);
    }

    public PointF computeScrollVectorForPosition(int targetPosition) {
        if (getChildCount() == 0) {
            return null;
        }
        boolean z = false;
        int i = 1;
        if (targetPosition < getPosition(getChildAt(0))) {
            z = true;
        }
        if (z != this.mShouldReverseLayout) {
            i = -1;
        }
        int i2 = i;
        return this.mOrientation == 0 ? new PointF((float) i2, 0.0f) : new PointF(0.0f, (float) i2);
    }

    public int computeVerticalScrollExtent(RecyclerView.State state) {
        return computeScrollExtent(state);
    }

    public int computeVerticalScrollOffset(RecyclerView.State state) {
        return computeScrollOffset(state);
    }

    public int computeVerticalScrollRange(RecyclerView.State state) {
        return computeScrollRange(state);
    }

    /* access modifiers changed from: package-private */
    public int convertFocusDirectionToLayoutDirection(int focusDirection) {
        switch (focusDirection) {
            case 1:
                return (this.mOrientation != 1 && isLayoutRTL()) ? 1 : -1;
            case 2:
                return (this.mOrientation != 1 && isLayoutRTL()) ? -1 : 1;
            case 17:
                return this.mOrientation == 0 ? -1 : Integer.MIN_VALUE;
            case 33:
                return this.mOrientation == 1 ? -1 : Integer.MIN_VALUE;
            case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT:
                return this.mOrientation == 0 ? 1 : Integer.MIN_VALUE;
            case 130:
                return this.mOrientation == 1 ? 1 : Integer.MIN_VALUE;
            default:
                return Integer.MIN_VALUE;
        }
    }

    /* access modifiers changed from: package-private */
    public LayoutState createLayoutState() {
        return new LayoutState();
    }

    /* access modifiers changed from: package-private */
    public void ensureLayoutState() {
        if (this.mLayoutState == null) {
            this.mLayoutState = createLayoutState();
        }
    }

    /* access modifiers changed from: package-private */
    public int fill(RecyclerView.Recycler recycler, LayoutState layoutState, RecyclerView.State state, boolean stopOnFocusable) {
        int i = layoutState.mAvailable;
        if (layoutState.mScrollingOffset != Integer.MIN_VALUE) {
            if (layoutState.mAvailable < 0) {
                layoutState.mScrollingOffset += layoutState.mAvailable;
            }
            recycleByLayoutState(recycler, layoutState);
        }
        int i2 = layoutState.mAvailable + layoutState.mExtraFillSpace;
        LayoutChunkResult layoutChunkResult = this.mLayoutChunkResult;
        while (true) {
            if ((!layoutState.mInfinite && i2 <= 0) || !layoutState.hasMore(state)) {
                break;
            }
            layoutChunkResult.resetInternal();
            layoutChunk(recycler, state, layoutState, layoutChunkResult);
            if (!layoutChunkResult.mFinished) {
                layoutState.mOffset += layoutChunkResult.mConsumed * layoutState.mLayoutDirection;
                if (!layoutChunkResult.mIgnoreConsumed || layoutState.mScrapList != null || !state.isPreLayout()) {
                    layoutState.mAvailable -= layoutChunkResult.mConsumed;
                    i2 -= layoutChunkResult.mConsumed;
                }
                if (layoutState.mScrollingOffset != Integer.MIN_VALUE) {
                    layoutState.mScrollingOffset += layoutChunkResult.mConsumed;
                    if (layoutState.mAvailable < 0) {
                        layoutState.mScrollingOffset += layoutState.mAvailable;
                    }
                    recycleByLayoutState(recycler, layoutState);
                }
                if (stopOnFocusable && layoutChunkResult.mFocusable) {
                    break;
                }
            } else {
                break;
            }
        }
        return i - layoutState.mAvailable;
    }

    public int findFirstCompletelyVisibleItemPosition() {
        View findOneVisibleChild = findOneVisibleChild(0, getChildCount(), true, false);
        if (findOneVisibleChild == null) {
            return -1;
        }
        return getPosition(findOneVisibleChild);
    }

    /* access modifiers changed from: package-private */
    public View findFirstVisibleChildClosestToEnd(boolean completelyVisible, boolean acceptPartiallyVisible) {
        return this.mShouldReverseLayout ? findOneVisibleChild(0, getChildCount(), completelyVisible, acceptPartiallyVisible) : findOneVisibleChild(getChildCount() - 1, -1, completelyVisible, acceptPartiallyVisible);
    }

    /* access modifiers changed from: package-private */
    public View findFirstVisibleChildClosestToStart(boolean completelyVisible, boolean acceptPartiallyVisible) {
        return this.mShouldReverseLayout ? findOneVisibleChild(getChildCount() - 1, -1, completelyVisible, acceptPartiallyVisible) : findOneVisibleChild(0, getChildCount(), completelyVisible, acceptPartiallyVisible);
    }

    public int findFirstVisibleItemPosition() {
        View findOneVisibleChild = findOneVisibleChild(0, getChildCount(), false, true);
        if (findOneVisibleChild == null) {
            return -1;
        }
        return getPosition(findOneVisibleChild);
    }

    public int findLastCompletelyVisibleItemPosition() {
        View findOneVisibleChild = findOneVisibleChild(getChildCount() - 1, -1, true, false);
        if (findOneVisibleChild == null) {
            return -1;
        }
        return getPosition(findOneVisibleChild);
    }

    public int findLastVisibleItemPosition() {
        View findOneVisibleChild = findOneVisibleChild(getChildCount() - 1, -1, false, true);
        if (findOneVisibleChild == null) {
            return -1;
        }
        return getPosition(findOneVisibleChild);
    }

    /* access modifiers changed from: package-private */
    public View findOnePartiallyOrCompletelyInvisibleChild(int fromIndex, int toIndex) {
        int i;
        int i2;
        ensureLayoutState();
        if ((toIndex > fromIndex ? 1 : toIndex < fromIndex ? (char) 65535 : 0) == 0) {
            return getChildAt(fromIndex);
        }
        if (this.mOrientationHelper.getDecoratedStart(getChildAt(fromIndex)) < this.mOrientationHelper.getStartAfterPadding()) {
            i2 = 16644;
            i = 16388;
        } else {
            i2 = 4161;
            i = FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
        }
        return this.mOrientation == 0 ? this.mHorizontalBoundCheck.findOneViewWithinBoundFlags(fromIndex, toIndex, i2, i) : this.mVerticalBoundCheck.findOneViewWithinBoundFlags(fromIndex, toIndex, i2, i);
    }

    /* access modifiers changed from: package-private */
    public View findOneVisibleChild(int fromIndex, int toIndex, boolean completelyVisible, boolean acceptPartiallyVisible) {
        ensureLayoutState();
        int i = 0;
        int i2 = completelyVisible ? 24579 : 320;
        if (acceptPartiallyVisible) {
            i = 320;
        }
        return this.mOrientation == 0 ? this.mHorizontalBoundCheck.findOneViewWithinBoundFlags(fromIndex, toIndex, i2, i) : this.mVerticalBoundCheck.findOneViewWithinBoundFlags(fromIndex, toIndex, i2, i);
    }

    /* access modifiers changed from: package-private */
    public View findReferenceChild(RecyclerView.Recycler recycler, RecyclerView.State state, int start, int end, int itemCount) {
        ensureLayoutState();
        View view = null;
        View view2 = null;
        int startAfterPadding = this.mOrientationHelper.getStartAfterPadding();
        int endAfterPadding = this.mOrientationHelper.getEndAfterPadding();
        int i = end > start ? 1 : -1;
        for (int i2 = start; i2 != end; i2 += i) {
            View childAt = getChildAt(i2);
            int position = getPosition(childAt);
            if (position >= 0 && position < itemCount) {
                if (((RecyclerView.LayoutParams) childAt.getLayoutParams()).isItemRemoved()) {
                    if (view == null) {
                        view = childAt;
                    }
                } else if (this.mOrientationHelper.getDecoratedStart(childAt) < endAfterPadding && this.mOrientationHelper.getDecoratedEnd(childAt) >= startAfterPadding) {
                    return childAt;
                } else {
                    if (view2 == null) {
                        view2 = childAt;
                    }
                }
            }
        }
        return view2 != null ? view2 : view;
    }

    public View findViewByPosition(int position) {
        int childCount = getChildCount();
        if (childCount == 0) {
            return null;
        }
        int position2 = position - getPosition(getChildAt(0));
        if (position2 >= 0 && position2 < childCount) {
            View childAt = getChildAt(position2);
            if (getPosition(childAt) == position) {
                return childAt;
            }
        }
        return super.findViewByPosition(position);
    }

    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(-2, -2);
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public int getExtraLayoutSpace(RecyclerView.State state) {
        if (state.hasTargetScrollPosition()) {
            return this.mOrientationHelper.getTotalSpace();
        }
        return 0;
    }

    public int getInitialPrefetchItemCount() {
        return this.mInitialPrefetchItemCount;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public boolean getRecycleChildrenOnDetach() {
        return this.mRecycleChildrenOnDetach;
    }

    public boolean getReverseLayout() {
        return this.mReverseLayout;
    }

    public boolean getStackFromEnd() {
        return this.mStackFromEnd;
    }

    public boolean isAutoMeasureEnabled() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isLayoutRTL() {
        return getLayoutDirection() == 1;
    }

    public boolean isSmoothScrollbarEnabled() {
        return this.mSmoothScrollbarEnabled;
    }

    /* access modifiers changed from: package-private */
    public void layoutChunk(RecyclerView.Recycler recycler, RecyclerView.State state, LayoutState layoutState, LayoutChunkResult result) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        LayoutState layoutState2 = layoutState;
        LayoutChunkResult layoutChunkResult = result;
        View next = layoutState2.next(recycler);
        if (next == null) {
            layoutChunkResult.mFinished = true;
            return;
        }
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) next.getLayoutParams();
        if (layoutState2.mScrapList == null) {
            if (this.mShouldReverseLayout == (layoutState2.mLayoutDirection == -1)) {
                addView(next);
            } else {
                addView(next, 0);
            }
        } else {
            if (this.mShouldReverseLayout == (layoutState2.mLayoutDirection == -1)) {
                addDisappearingView(next);
            } else {
                addDisappearingView(next, 0);
            }
        }
        measureChildWithMargins(next, 0, 0);
        layoutChunkResult.mConsumed = this.mOrientationHelper.getDecoratedMeasurement(next);
        if (this.mOrientation == 1) {
            if (isLayoutRTL()) {
                i6 = getWidth() - getPaddingRight();
                i5 = i6 - this.mOrientationHelper.getDecoratedMeasurementInOther(next);
            } else {
                i5 = getPaddingLeft();
                i6 = this.mOrientationHelper.getDecoratedMeasurementInOther(next) + i5;
            }
            if (layoutState2.mLayoutDirection == -1) {
                i4 = i6;
                i3 = layoutState2.mOffset;
                i2 = i5;
                i = layoutState2.mOffset - layoutChunkResult.mConsumed;
            } else {
                int i7 = layoutState2.mOffset;
                i4 = i6;
                i3 = layoutState2.mOffset + layoutChunkResult.mConsumed;
                i2 = i5;
                i = i7;
            }
        } else {
            int paddingTop = getPaddingTop();
            int decoratedMeasurementInOther = this.mOrientationHelper.getDecoratedMeasurementInOther(next) + paddingTop;
            if (layoutState2.mLayoutDirection == -1) {
                i3 = decoratedMeasurementInOther;
                i4 = layoutState2.mOffset;
                i2 = layoutState2.mOffset - layoutChunkResult.mConsumed;
                i = paddingTop;
            } else {
                int i8 = layoutState2.mOffset;
                i3 = decoratedMeasurementInOther;
                i4 = layoutState2.mOffset + layoutChunkResult.mConsumed;
                i2 = i8;
                i = paddingTop;
            }
        }
        layoutDecoratedWithMargins(next, i2, i, i4, i3);
        if (layoutParams.isItemRemoved() || layoutParams.isItemChanged()) {
            layoutChunkResult.mIgnoreConsumed = true;
        }
        layoutChunkResult.mFocusable = next.hasFocusable();
    }

    /* access modifiers changed from: package-private */
    public void onAnchorReady(RecyclerView.Recycler recycler, RecyclerView.State state, AnchorInfo anchorInfo, int firstLayoutItemDirection) {
    }

    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        if (this.mRecycleChildrenOnDetach) {
            removeAndRecycleAllViews(recycler);
            recycler.clear();
        }
    }

    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int convertFocusDirectionToLayoutDirection;
        resolveShouldLayoutReverse();
        if (getChildCount() == 0 || (convertFocusDirectionToLayoutDirection = convertFocusDirectionToLayoutDirection(focusDirection)) == Integer.MIN_VALUE) {
            return null;
        }
        ensureLayoutState();
        updateLayoutState(convertFocusDirectionToLayoutDirection, (int) (((float) this.mOrientationHelper.getTotalSpace()) * MAX_SCROLL_FACTOR), false, state);
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
        this.mLayoutState.mRecycle = false;
        fill(recycler, this.mLayoutState, state, true);
        View findPartiallyOrCompletelyInvisibleChildClosestToStart = convertFocusDirectionToLayoutDirection == -1 ? findPartiallyOrCompletelyInvisibleChildClosestToStart() : findPartiallyOrCompletelyInvisibleChildClosestToEnd();
        View childClosestToStart = convertFocusDirectionToLayoutDirection == -1 ? getChildClosestToStart() : getChildClosestToEnd();
        if (!childClosestToStart.hasFocusable()) {
            return findPartiallyOrCompletelyInvisibleChildClosestToStart;
        }
        if (findPartiallyOrCompletelyInvisibleChildClosestToStart == null) {
            return null;
        }
        return childClosestToStart;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        if (getChildCount() > 0) {
            event.setFromIndex(findFirstVisibleItemPosition());
            event.setToIndex(findLastVisibleItemPosition());
        }
    }

    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int i;
        int i2;
        int i3;
        View findViewByPosition;
        int i4 = -1;
        if (!(this.mPendingSavedState == null && this.mPendingScrollPosition == -1) && state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        SavedState savedState = this.mPendingSavedState;
        if (savedState != null && savedState.hasValidAnchor()) {
            this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
        }
        ensureLayoutState();
        this.mLayoutState.mRecycle = false;
        resolveShouldLayoutReverse();
        View focusedChild = getFocusedChild();
        if (!this.mAnchorInfo.mValid || this.mPendingScrollPosition != -1 || this.mPendingSavedState != null) {
            this.mAnchorInfo.reset();
            this.mAnchorInfo.mLayoutFromEnd = this.mShouldReverseLayout ^ this.mStackFromEnd;
            updateAnchorInfoForLayout(recycler, state, this.mAnchorInfo);
            this.mAnchorInfo.mValid = true;
        } else if (focusedChild != null && (this.mOrientationHelper.getDecoratedStart(focusedChild) >= this.mOrientationHelper.getEndAfterPadding() || this.mOrientationHelper.getDecoratedEnd(focusedChild) <= this.mOrientationHelper.getStartAfterPadding())) {
            this.mAnchorInfo.assignFromViewAndKeepVisibleRect(focusedChild, getPosition(focusedChild));
        }
        LayoutState layoutState = this.mLayoutState;
        layoutState.mLayoutDirection = layoutState.mLastScrollDelta >= 0 ? 1 : -1;
        int[] iArr = this.mReusableIntPair;
        iArr[0] = 0;
        iArr[1] = 0;
        calculateExtraLayoutSpace(state, iArr);
        int max = Math.max(0, this.mReusableIntPair[0]) + this.mOrientationHelper.getStartAfterPadding();
        int max2 = Math.max(0, this.mReusableIntPair[1]) + this.mOrientationHelper.getEndPadding();
        if (!(!state.isPreLayout() || (i3 = this.mPendingScrollPosition) == -1 || this.mPendingScrollPositionOffset == Integer.MIN_VALUE || (findViewByPosition = findViewByPosition(i3)) == null)) {
            int endAfterPadding = this.mShouldReverseLayout ? (this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(findViewByPosition)) - this.mPendingScrollPositionOffset : this.mPendingScrollPositionOffset - (this.mOrientationHelper.getDecoratedStart(findViewByPosition) - this.mOrientationHelper.getStartAfterPadding());
            if (endAfterPadding > 0) {
                max += endAfterPadding;
            } else {
                max2 -= endAfterPadding;
            }
        }
        if (this.mAnchorInfo.mLayoutFromEnd) {
            if (this.mShouldReverseLayout) {
                i4 = 1;
            }
        } else if (!this.mShouldReverseLayout) {
            i4 = 1;
        }
        onAnchorReady(recycler, state, this.mAnchorInfo, i4);
        detachAndScrapAttachedViews(recycler);
        this.mLayoutState.mInfinite = resolveIsInfinite();
        this.mLayoutState.mIsPreLayout = state.isPreLayout();
        this.mLayoutState.mNoRecycleSpace = 0;
        if (this.mAnchorInfo.mLayoutFromEnd) {
            updateLayoutStateToFillStart(this.mAnchorInfo);
            this.mLayoutState.mExtraFillSpace = max;
            fill(recycler, this.mLayoutState, state, false);
            i2 = this.mLayoutState.mOffset;
            int i5 = this.mLayoutState.mCurrentPosition;
            if (this.mLayoutState.mAvailable > 0) {
                max2 += this.mLayoutState.mAvailable;
            }
            updateLayoutStateToFillEnd(this.mAnchorInfo);
            this.mLayoutState.mExtraFillSpace = max2;
            this.mLayoutState.mCurrentPosition += this.mLayoutState.mItemDirection;
            fill(recycler, this.mLayoutState, state, false);
            i = this.mLayoutState.mOffset;
            if (this.mLayoutState.mAvailable > 0) {
                int i6 = this.mLayoutState.mAvailable;
                updateLayoutStateToFillStart(i5, i2);
                this.mLayoutState.mExtraFillSpace = i6;
                fill(recycler, this.mLayoutState, state, false);
                i2 = this.mLayoutState.mOffset;
            }
        } else {
            updateLayoutStateToFillEnd(this.mAnchorInfo);
            this.mLayoutState.mExtraFillSpace = max2;
            fill(recycler, this.mLayoutState, state, false);
            i = this.mLayoutState.mOffset;
            int i7 = this.mLayoutState.mCurrentPosition;
            if (this.mLayoutState.mAvailable > 0) {
                max += this.mLayoutState.mAvailable;
            }
            updateLayoutStateToFillStart(this.mAnchorInfo);
            this.mLayoutState.mExtraFillSpace = max;
            this.mLayoutState.mCurrentPosition += this.mLayoutState.mItemDirection;
            fill(recycler, this.mLayoutState, state, false);
            int i8 = this.mLayoutState.mOffset;
            if (this.mLayoutState.mAvailable > 0) {
                int i9 = this.mLayoutState.mAvailable;
                updateLayoutStateToFillEnd(i7, i);
                this.mLayoutState.mExtraFillSpace = i9;
                fill(recycler, this.mLayoutState, state, false);
                i = this.mLayoutState.mOffset;
                i2 = i8;
            } else {
                i2 = i8;
            }
        }
        if (getChildCount() > 0) {
            if (this.mShouldReverseLayout ^ this.mStackFromEnd) {
                int fixLayoutEndGap = fixLayoutEndGap(i, recycler, state, true);
                int i10 = i2 + fixLayoutEndGap;
                int fixLayoutStartGap = fixLayoutStartGap(i10, recycler, state, false);
                i2 = i10 + fixLayoutStartGap;
                i = i + fixLayoutEndGap + fixLayoutStartGap;
            } else {
                int fixLayoutStartGap2 = fixLayoutStartGap(i2, recycler, state, true);
                int i11 = i + fixLayoutStartGap2;
                int fixLayoutEndGap2 = fixLayoutEndGap(i11, recycler, state, false);
                i2 = i2 + fixLayoutStartGap2 + fixLayoutEndGap2;
                i = i11 + fixLayoutEndGap2;
            }
        }
        layoutForPredictiveAnimations(recycler, state, i2, i);
        if (!state.isPreLayout()) {
            this.mOrientationHelper.onLayoutComplete();
        } else {
            this.mAnchorInfo.reset();
        }
        this.mLastStackFromEnd = this.mStackFromEnd;
    }

    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        this.mPendingSavedState = null;
        this.mPendingScrollPosition = -1;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        this.mAnchorInfo.reset();
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            this.mPendingSavedState = (SavedState) state;
            requestLayout();
        }
    }

    public Parcelable onSaveInstanceState() {
        if (this.mPendingSavedState != null) {
            return new SavedState(this.mPendingSavedState);
        }
        SavedState savedState = new SavedState();
        if (getChildCount() > 0) {
            ensureLayoutState();
            boolean z = this.mLastStackFromEnd ^ this.mShouldReverseLayout;
            savedState.mAnchorLayoutFromEnd = z;
            if (z) {
                View childClosestToEnd = getChildClosestToEnd();
                savedState.mAnchorOffset = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(childClosestToEnd);
                savedState.mAnchorPosition = getPosition(childClosestToEnd);
            } else {
                View childClosestToStart = getChildClosestToStart();
                savedState.mAnchorPosition = getPosition(childClosestToStart);
                savedState.mAnchorOffset = this.mOrientationHelper.getDecoratedStart(childClosestToStart) - this.mOrientationHelper.getStartAfterPadding();
            }
        } else {
            savedState.invalidateAnchor();
        }
        return savedState;
    }

    public void prepareForDrop(View view, View target, int x, int y) {
        assertNotInLayoutOrScroll("Cannot drop a view during a scroll or layout calculation");
        ensureLayoutState();
        resolveShouldLayoutReverse();
        int position = getPosition(view);
        int position2 = getPosition(target);
        boolean z = position < position2 ? true : true;
        if (this.mShouldReverseLayout) {
            if (z) {
                scrollToPositionWithOffset(position2, this.mOrientationHelper.getEndAfterPadding() - (this.mOrientationHelper.getDecoratedStart(target) + this.mOrientationHelper.getDecoratedMeasurement(view)));
            } else {
                scrollToPositionWithOffset(position2, this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(target));
            }
        } else if (z) {
            scrollToPositionWithOffset(position2, this.mOrientationHelper.getDecoratedStart(target));
        } else {
            scrollToPositionWithOffset(position2, this.mOrientationHelper.getDecoratedEnd(target) - this.mOrientationHelper.getDecoratedMeasurement(view));
        }
    }

    /* access modifiers changed from: package-private */
    public boolean resolveIsInfinite() {
        return this.mOrientationHelper.getMode() == 0 && this.mOrientationHelper.getEnd() == 0;
    }

    /* access modifiers changed from: package-private */
    public int scrollBy(int delta, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() == 0 || delta == 0) {
            return 0;
        }
        ensureLayoutState();
        this.mLayoutState.mRecycle = true;
        int i = delta > 0 ? 1 : -1;
        int abs = Math.abs(delta);
        updateLayoutState(i, abs, true, state);
        int fill = this.mLayoutState.mScrollingOffset + fill(recycler, this.mLayoutState, state, false);
        if (fill < 0) {
            return 0;
        }
        int i2 = abs > fill ? i * fill : delta;
        this.mOrientationHelper.offsetChildren(-i2);
        this.mLayoutState.mLastScrollDelta = i2;
        return i2;
    }

    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 1) {
            return 0;
        }
        return scrollBy(dx, recycler, state);
    }

    public void scrollToPosition(int position) {
        this.mPendingScrollPosition = position;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        SavedState savedState = this.mPendingSavedState;
        if (savedState != null) {
            savedState.invalidateAnchor();
        }
        requestLayout();
    }

    public void scrollToPositionWithOffset(int position, int offset) {
        this.mPendingScrollPosition = position;
        this.mPendingScrollPositionOffset = offset;
        SavedState savedState = this.mPendingSavedState;
        if (savedState != null) {
            savedState.invalidateAnchor();
        }
        requestLayout();
    }

    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 0) {
            return 0;
        }
        return scrollBy(dy, recycler, state);
    }

    public void setInitialPrefetchItemCount(int itemCount) {
        this.mInitialPrefetchItemCount = itemCount;
    }

    public void setOrientation(int orientation) {
        if (orientation == 0 || orientation == 1) {
            assertNotInLayoutOrScroll((String) null);
            if (orientation != this.mOrientation || this.mOrientationHelper == null) {
                OrientationHelper createOrientationHelper = OrientationHelper.createOrientationHelper(this, orientation);
                this.mOrientationHelper = createOrientationHelper;
                this.mAnchorInfo.mOrientationHelper = createOrientationHelper;
                this.mOrientation = orientation;
                requestLayout();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("invalid orientation:" + orientation);
    }

    public void setRecycleChildrenOnDetach(boolean recycleChildrenOnDetach) {
        this.mRecycleChildrenOnDetach = recycleChildrenOnDetach;
    }

    public void setReverseLayout(boolean reverseLayout) {
        assertNotInLayoutOrScroll((String) null);
        if (reverseLayout != this.mReverseLayout) {
            this.mReverseLayout = reverseLayout;
            requestLayout();
        }
    }

    public void setSmoothScrollbarEnabled(boolean enabled) {
        this.mSmoothScrollbarEnabled = enabled;
    }

    public void setStackFromEnd(boolean stackFromEnd) {
        assertNotInLayoutOrScroll((String) null);
        if (this.mStackFromEnd != stackFromEnd) {
            this.mStackFromEnd = stackFromEnd;
            requestLayout();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean shouldMeasureTwice() {
        return (getHeightMode() == 1073741824 || getWidthMode() == 1073741824 || !hasFlexibleChildInBothOrientations()) ? false : true;
    }

    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext());
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    public boolean supportsPredictiveItemAnimations() {
        return this.mPendingSavedState == null && this.mLastStackFromEnd == this.mStackFromEnd;
    }

    /* access modifiers changed from: package-private */
    public void validateChildOrder() {
        Log.d(TAG, "validating child count " + getChildCount());
        boolean z = true;
        if (getChildCount() >= 1) {
            int position = getPosition(getChildAt(0));
            int decoratedStart = this.mOrientationHelper.getDecoratedStart(getChildAt(0));
            if (this.mShouldReverseLayout) {
                int i = 1;
                while (i < getChildCount()) {
                    View childAt = getChildAt(i);
                    int position2 = getPosition(childAt);
                    int decoratedStart2 = this.mOrientationHelper.getDecoratedStart(childAt);
                    if (position2 < position) {
                        logChildren();
                        StringBuilder append = new StringBuilder().append("detected invalid position. loc invalid? ");
                        if (decoratedStart2 >= decoratedStart) {
                            z = false;
                        }
                        throw new RuntimeException(append.append(z).toString());
                    } else if (decoratedStart2 <= decoratedStart) {
                        i++;
                    } else {
                        logChildren();
                        throw new RuntimeException("detected invalid location");
                    }
                }
                return;
            }
            int i2 = 1;
            while (i2 < getChildCount()) {
                View childAt2 = getChildAt(i2);
                int position3 = getPosition(childAt2);
                int decoratedStart3 = this.mOrientationHelper.getDecoratedStart(childAt2);
                if (position3 < position) {
                    logChildren();
                    StringBuilder append2 = new StringBuilder().append("detected invalid position. loc invalid? ");
                    if (decoratedStart3 >= decoratedStart) {
                        z = false;
                    }
                    throw new RuntimeException(append2.append(z).toString());
                } else if (decoratedStart3 >= decoratedStart) {
                    i2++;
                } else {
                    logChildren();
                    throw new RuntimeException("detected invalid location");
                }
            }
        }
    }
}

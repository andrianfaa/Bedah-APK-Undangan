package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;

public class GridLayoutManager extends LinearLayoutManager {
    private static final boolean DEBUG = false;
    public static final int DEFAULT_SPAN_COUNT = -1;
    private static final String TAG = "GridLayoutManager";
    int[] mCachedBorders;
    final Rect mDecorInsets = new Rect();
    boolean mPendingSpanCountChange = false;
    final SparseIntArray mPreLayoutSpanIndexCache = new SparseIntArray();
    final SparseIntArray mPreLayoutSpanSizeCache = new SparseIntArray();
    View[] mSet;
    int mSpanCount = -1;
    SpanSizeLookup mSpanSizeLookup = new DefaultSpanSizeLookup();
    private boolean mUsingSpansToEstimateScrollBarDimensions;

    public static final class DefaultSpanSizeLookup extends SpanSizeLookup {
        public int getSpanIndex(int position, int spanCount) {
            return position % spanCount;
        }

        public int getSpanSize(int position) {
            return 1;
        }
    }

    public static class LayoutParams extends RecyclerView.LayoutParams {
        public static final int INVALID_SPAN_ID = -1;
        int mSpanIndex = -1;
        int mSpanSize = 0;

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

        public LayoutParams(RecyclerView.LayoutParams source) {
            super(source);
        }

        public int getSpanIndex() {
            return this.mSpanIndex;
        }

        public int getSpanSize() {
            return this.mSpanSize;
        }
    }

    public static abstract class SpanSizeLookup {
        private boolean mCacheSpanGroupIndices = false;
        private boolean mCacheSpanIndices = false;
        final SparseIntArray mSpanGroupIndexCache = new SparseIntArray();
        final SparseIntArray mSpanIndexCache = new SparseIntArray();

        static int findFirstKeyLessThan(SparseIntArray cache, int position) {
            int i = 0;
            int size = cache.size() - 1;
            while (i <= size) {
                int i2 = (i + size) >>> 1;
                if (cache.keyAt(i2) < position) {
                    i = i2 + 1;
                } else {
                    size = i2 - 1;
                }
            }
            int i3 = i - 1;
            if (i3 < 0 || i3 >= cache.size()) {
                return -1;
            }
            return cache.keyAt(i3);
        }

        /* access modifiers changed from: package-private */
        public int getCachedSpanGroupIndex(int position, int spanCount) {
            if (!this.mCacheSpanGroupIndices) {
                return getSpanGroupIndex(position, spanCount);
            }
            int i = this.mSpanGroupIndexCache.get(position, -1);
            if (i != -1) {
                return i;
            }
            int spanGroupIndex = getSpanGroupIndex(position, spanCount);
            this.mSpanGroupIndexCache.put(position, spanGroupIndex);
            return spanGroupIndex;
        }

        /* access modifiers changed from: package-private */
        public int getCachedSpanIndex(int position, int spanCount) {
            if (!this.mCacheSpanIndices) {
                return getSpanIndex(position, spanCount);
            }
            int i = this.mSpanIndexCache.get(position, -1);
            if (i != -1) {
                return i;
            }
            int spanIndex = getSpanIndex(position, spanCount);
            this.mSpanIndexCache.put(position, spanIndex);
            return spanIndex;
        }

        public int getSpanGroupIndex(int adapterPosition, int spanCount) {
            int i;
            int findFirstKeyLessThan;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            if (this.mCacheSpanGroupIndices && (findFirstKeyLessThan = findFirstKeyLessThan(this.mSpanGroupIndexCache, adapterPosition)) != -1) {
                i3 = this.mSpanGroupIndexCache.get(findFirstKeyLessThan);
                i4 = findFirstKeyLessThan + 1;
                i2 = getCachedSpanIndex(findFirstKeyLessThan, spanCount) + getSpanSize(findFirstKeyLessThan);
                if (i2 == spanCount) {
                    i2 = 0;
                    i3++;
                }
            }
            int spanSize = getSpanSize(adapterPosition);
            for (int i5 = i4; i5 < adapterPosition; i5++) {
                int spanSize2 = getSpanSize(i5);
                i += spanSize2;
                if (i == spanCount) {
                    i = 0;
                    i3++;
                } else if (i > spanCount) {
                    i = spanSize2;
                    i3++;
                }
            }
            return i + spanSize > spanCount ? i3 + 1 : i3;
        }

        public int getSpanIndex(int position, int spanCount) {
            int i;
            int findFirstKeyLessThan;
            int spanSize = getSpanSize(position);
            if (spanSize == spanCount) {
                return 0;
            }
            int i2 = 0;
            int i3 = 0;
            if (this.mCacheSpanIndices && (findFirstKeyLessThan = findFirstKeyLessThan(this.mSpanIndexCache, position)) >= 0) {
                i2 = this.mSpanIndexCache.get(findFirstKeyLessThan) + getSpanSize(findFirstKeyLessThan);
                i3 = findFirstKeyLessThan + 1;
            }
            for (int i4 = i3; i4 < position; i4++) {
                int spanSize2 = getSpanSize(i4);
                i += spanSize2;
                if (i == spanCount) {
                    i = 0;
                } else if (i > spanCount) {
                    i = spanSize2;
                }
            }
            if (i + spanSize <= spanCount) {
                return i;
            }
            return 0;
        }

        public abstract int getSpanSize(int i);

        public void invalidateSpanGroupIndexCache() {
            this.mSpanGroupIndexCache.clear();
        }

        public void invalidateSpanIndexCache() {
            this.mSpanIndexCache.clear();
        }

        public boolean isSpanGroupIndexCacheEnabled() {
            return this.mCacheSpanGroupIndices;
        }

        public boolean isSpanIndexCacheEnabled() {
            return this.mCacheSpanIndices;
        }

        public void setSpanGroupIndexCacheEnabled(boolean cacheSpanGroupIndices) {
            if (!cacheSpanGroupIndices) {
                this.mSpanGroupIndexCache.clear();
            }
            this.mCacheSpanGroupIndices = cacheSpanGroupIndices;
        }

        public void setSpanIndexCacheEnabled(boolean cacheSpanIndices) {
            if (!cacheSpanIndices) {
                this.mSpanGroupIndexCache.clear();
            }
            this.mCacheSpanIndices = cacheSpanIndices;
        }
    }

    public GridLayoutManager(Context context, int spanCount) {
        super(context);
        setSpanCount(spanCount);
    }

    public GridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        setSpanCount(spanCount);
    }

    public GridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setSpanCount(getProperties(context, attrs, defStyleAttr, defStyleRes).spanCount);
    }

    private void assignSpans(RecyclerView.Recycler recycler, RecyclerView.State state, int count, boolean layingOutInPrimaryDirection) {
        int i;
        int i2;
        int i3;
        if (layingOutInPrimaryDirection) {
            i3 = 0;
            i2 = count;
            i = 1;
        } else {
            i3 = count - 1;
            i2 = -1;
            i = -1;
        }
        int i4 = 0;
        for (int i5 = i3; i5 != i2; i5 += i) {
            View view = this.mSet[i5];
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.mSpanSize = getSpanSize(recycler, state, getPosition(view));
            layoutParams.mSpanIndex = i4;
            i4 += layoutParams.mSpanSize;
        }
    }

    private void cachePreLayoutSpanMapping() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            LayoutParams layoutParams = (LayoutParams) getChildAt(i).getLayoutParams();
            int viewLayoutPosition = layoutParams.getViewLayoutPosition();
            this.mPreLayoutSpanSizeCache.put(viewLayoutPosition, layoutParams.getSpanSize());
            this.mPreLayoutSpanIndexCache.put(viewLayoutPosition, layoutParams.getSpanIndex());
        }
    }

    private void calculateItemBorders(int totalSpace) {
        this.mCachedBorders = calculateItemBorders(this.mCachedBorders, this.mSpanCount, totalSpace);
    }

    static int[] calculateItemBorders(int[] cachedBorders, int spanCount, int totalSpace) {
        if (!(cachedBorders != null && cachedBorders.length == spanCount + 1 && cachedBorders[cachedBorders.length - 1] == totalSpace)) {
            cachedBorders = new int[(spanCount + 1)];
        }
        cachedBorders[0] = 0;
        int i = totalSpace / spanCount;
        int i2 = totalSpace % spanCount;
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 1; i5 <= spanCount; i5++) {
            int i6 = i;
            i4 += i2;
            if (i4 > 0 && spanCount - i4 < i2) {
                i6++;
                i4 -= spanCount;
            }
            i3 += i6;
            cachedBorders[i5] = i3;
        }
        return cachedBorders;
    }

    private void clearPreLayoutSpanMappingCache() {
        this.mPreLayoutSpanSizeCache.clear();
        this.mPreLayoutSpanIndexCache.clear();
    }

    private int computeScrollOffsetWithSpanInfo(RecyclerView.State state) {
        if (getChildCount() == 0 || state.getItemCount() == 0) {
            return 0;
        }
        ensureLayoutState();
        boolean isSmoothScrollbarEnabled = isSmoothScrollbarEnabled();
        View findFirstVisibleChildClosestToStart = findFirstVisibleChildClosestToStart(!isSmoothScrollbarEnabled, true);
        View findFirstVisibleChildClosestToEnd = findFirstVisibleChildClosestToEnd(!isSmoothScrollbarEnabled, true);
        if (findFirstVisibleChildClosestToStart == null) {
            boolean z = isSmoothScrollbarEnabled;
        } else if (findFirstVisibleChildClosestToEnd == null) {
            boolean z2 = isSmoothScrollbarEnabled;
        } else {
            int cachedSpanGroupIndex = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToStart), this.mSpanCount);
            int cachedSpanGroupIndex2 = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToEnd), this.mSpanCount);
            int min = Math.min(cachedSpanGroupIndex, cachedSpanGroupIndex2);
            int max = this.mShouldReverseLayout ? Math.max(0, ((this.mSpanSizeLookup.getCachedSpanGroupIndex(state.getItemCount() - 1, this.mSpanCount) + 1) - Math.max(cachedSpanGroupIndex, cachedSpanGroupIndex2)) - 1) : Math.max(0, min);
            if (!isSmoothScrollbarEnabled) {
                return max;
            }
            boolean z3 = isSmoothScrollbarEnabled;
            int i = max;
            return Math.round((((float) max) * (((float) Math.abs(this.mOrientationHelper.getDecoratedEnd(findFirstVisibleChildClosestToEnd) - this.mOrientationHelper.getDecoratedStart(findFirstVisibleChildClosestToStart))) / ((float) ((this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToEnd), this.mSpanCount) - this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToStart), this.mSpanCount)) + 1)))) + ((float) (this.mOrientationHelper.getStartAfterPadding() - this.mOrientationHelper.getDecoratedStart(findFirstVisibleChildClosestToStart))));
        }
        return 0;
    }

    private int computeScrollRangeWithSpanInfo(RecyclerView.State state) {
        if (getChildCount() == 0 || state.getItemCount() == 0) {
            return 0;
        }
        ensureLayoutState();
        View findFirstVisibleChildClosestToStart = findFirstVisibleChildClosestToStart(!isSmoothScrollbarEnabled(), true);
        View findFirstVisibleChildClosestToEnd = findFirstVisibleChildClosestToEnd(!isSmoothScrollbarEnabled(), true);
        if (findFirstVisibleChildClosestToStart == null || findFirstVisibleChildClosestToEnd == null) {
            return 0;
        }
        if (!isSmoothScrollbarEnabled()) {
            return this.mSpanSizeLookup.getCachedSpanGroupIndex(state.getItemCount() - 1, this.mSpanCount) + 1;
        }
        int decoratedEnd = this.mOrientationHelper.getDecoratedEnd(findFirstVisibleChildClosestToEnd) - this.mOrientationHelper.getDecoratedStart(findFirstVisibleChildClosestToStart);
        int cachedSpanGroupIndex = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToStart), this.mSpanCount);
        return (int) ((((float) decoratedEnd) / ((float) ((this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToEnd), this.mSpanCount) - cachedSpanGroupIndex) + 1))) * ((float) (this.mSpanSizeLookup.getCachedSpanGroupIndex(state.getItemCount() - 1, this.mSpanCount) + 1)));
    }

    private void ensureAnchorIsInCorrectSpan(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.AnchorInfo anchorInfo, int itemDirection) {
        boolean z = itemDirection == 1;
        int spanIndex = getSpanIndex(recycler, state, anchorInfo.mPosition);
        if (z) {
            while (spanIndex > 0 && anchorInfo.mPosition > 0) {
                anchorInfo.mPosition--;
                spanIndex = getSpanIndex(recycler, state, anchorInfo.mPosition);
            }
            return;
        }
        int itemCount = state.getItemCount() - 1;
        int i = anchorInfo.mPosition;
        int i2 = spanIndex;
        while (i < itemCount) {
            int spanIndex2 = getSpanIndex(recycler, state, i + 1);
            if (spanIndex2 <= i2) {
                break;
            }
            i++;
            i2 = spanIndex2;
        }
        anchorInfo.mPosition = i;
    }

    private void ensureViewSet() {
        View[] viewArr = this.mSet;
        if (viewArr == null || viewArr.length != this.mSpanCount) {
            this.mSet = new View[this.mSpanCount];
        }
    }

    private int getSpanGroupIndex(RecyclerView.Recycler recycler, RecyclerView.State state, int viewPosition) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getCachedSpanGroupIndex(viewPosition, this.mSpanCount);
        }
        int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(viewPosition);
        if (convertPreLayoutPositionToPostLayout != -1) {
            return this.mSpanSizeLookup.getCachedSpanGroupIndex(convertPreLayoutPositionToPostLayout, this.mSpanCount);
        }
        Log.w(TAG, "Cannot find span size for pre layout position. " + viewPosition);
        return 0;
    }

    private int getSpanIndex(RecyclerView.Recycler recycler, RecyclerView.State state, int pos) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getCachedSpanIndex(pos, this.mSpanCount);
        }
        int i = this.mPreLayoutSpanIndexCache.get(pos, -1);
        if (i != -1) {
            return i;
        }
        int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(pos);
        if (convertPreLayoutPositionToPostLayout != -1) {
            return this.mSpanSizeLookup.getCachedSpanIndex(convertPreLayoutPositionToPostLayout, this.mSpanCount);
        }
        Log.w(TAG, "Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + pos);
        return 0;
    }

    private int getSpanSize(RecyclerView.Recycler recycler, RecyclerView.State state, int pos) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getSpanSize(pos);
        }
        int i = this.mPreLayoutSpanSizeCache.get(pos, -1);
        if (i != -1) {
            return i;
        }
        int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(pos);
        if (convertPreLayoutPositionToPostLayout != -1) {
            return this.mSpanSizeLookup.getSpanSize(convertPreLayoutPositionToPostLayout);
        }
        Log.w(TAG, "Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + pos);
        return 1;
    }

    private void guessMeasurement(float maxSizeInOther, int currentOtherDirSize) {
        calculateItemBorders(Math.max(Math.round(((float) this.mSpanCount) * maxSizeInOther), currentOtherDirSize));
    }

    private void measureChild(View view, int otherDirParentSpecMode, boolean alreadyMeasured) {
        int i;
        int i2;
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        Rect rect = layoutParams.mDecorInsets;
        int i3 = rect.top + rect.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
        int i4 = rect.left + rect.right + layoutParams.leftMargin + layoutParams.rightMargin;
        int spaceForSpanRange = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
        if (this.mOrientation == 1) {
            i2 = getChildMeasureSpec(spaceForSpanRange, otherDirParentSpecMode, i4, layoutParams.width, false);
            i = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getHeightMode(), i3, layoutParams.height, true);
        } else {
            i = getChildMeasureSpec(spaceForSpanRange, otherDirParentSpecMode, i3, layoutParams.height, false);
            i2 = getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getWidthMode(), i4, layoutParams.width, true);
        }
        measureChildWithDecorationsAndMargin(view, i2, i, alreadyMeasured);
    }

    private void measureChildWithDecorationsAndMargin(View child, int widthSpec, int heightSpec, boolean alreadyMeasured) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
        if (alreadyMeasured ? shouldReMeasureChild(child, widthSpec, heightSpec, layoutParams) : shouldMeasureChild(child, widthSpec, heightSpec, layoutParams)) {
            child.measure(widthSpec, heightSpec);
        }
    }

    private void updateMeasurements() {
        calculateItemBorders(getOrientation() == 1 ? (getWidth() - getPaddingRight()) - getPaddingLeft() : (getHeight() - getPaddingBottom()) - getPaddingTop());
    }

    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        return lp instanceof LayoutParams;
    }

    /* access modifiers changed from: package-private */
    public void collectPrefetchPositionsForLayoutState(RecyclerView.State state, LinearLayoutManager.LayoutState layoutState, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int i = this.mSpanCount;
        for (int i2 = 0; i2 < this.mSpanCount && layoutState.hasMore(state) && i > 0; i2++) {
            int i3 = layoutState.mCurrentPosition;
            layoutPrefetchRegistry.addPosition(i3, Math.max(0, layoutState.mScrollingOffset));
            i -= this.mSpanSizeLookup.getSpanSize(i3);
            layoutState.mCurrentPosition += layoutState.mItemDirection;
        }
    }

    public int computeHorizontalScrollOffset(RecyclerView.State state) {
        return this.mUsingSpansToEstimateScrollBarDimensions ? computeScrollOffsetWithSpanInfo(state) : super.computeHorizontalScrollOffset(state);
    }

    public int computeHorizontalScrollRange(RecyclerView.State state) {
        return this.mUsingSpansToEstimateScrollBarDimensions ? computeScrollRangeWithSpanInfo(state) : super.computeHorizontalScrollRange(state);
    }

    public int computeVerticalScrollOffset(RecyclerView.State state) {
        return this.mUsingSpansToEstimateScrollBarDimensions ? computeScrollOffsetWithSpanInfo(state) : super.computeVerticalScrollOffset(state);
    }

    public int computeVerticalScrollRange(RecyclerView.State state) {
        return this.mUsingSpansToEstimateScrollBarDimensions ? computeScrollRangeWithSpanInfo(state) : super.computeVerticalScrollRange(state);
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
            if (position >= 0 && position < itemCount && getSpanIndex(recycler, state, position) == 0) {
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

    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return this.mOrientation == 0 ? new LayoutParams(-2, -1) : new LayoutParams(-1, -2);
    }

    public RecyclerView.LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
        return new LayoutParams(c, attrs);
    }

    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return lp instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams) lp) : new LayoutParams(lp);
    }

    public int getColumnCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 1) {
            return this.mSpanCount;
        }
        if (state.getItemCount() < 1) {
            return 0;
        }
        return getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }

    public int getRowCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 0) {
            return this.mSpanCount;
        }
        if (state.getItemCount() < 1) {
            return 0;
        }
        return getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }

    /* access modifiers changed from: package-private */
    public int getSpaceForSpanRange(int startSpan, int spanSize) {
        if (this.mOrientation != 1 || !isLayoutRTL()) {
            int[] iArr = this.mCachedBorders;
            return iArr[startSpan + spanSize] - iArr[startSpan];
        }
        int[] iArr2 = this.mCachedBorders;
        int i = this.mSpanCount;
        return iArr2[i - startSpan] - iArr2[(i - startSpan) - spanSize];
    }

    public int getSpanCount() {
        return this.mSpanCount;
    }

    public SpanSizeLookup getSpanSizeLookup() {
        return this.mSpanSizeLookup;
    }

    public boolean isUsingSpansToEstimateScrollbarDimensions() {
        return this.mUsingSpansToEstimateScrollBarDimensions;
    }

    /* access modifiers changed from: package-private */
    public void layoutChunk(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.LayoutState layoutState, LinearLayoutManager.LayoutChunkResult result) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        float f;
        boolean z;
        int i9;
        int i10;
        boolean z2;
        View next;
        RecyclerView.Recycler recycler2 = recycler;
        RecyclerView.State state2 = state;
        LinearLayoutManager.LayoutState layoutState2 = layoutState;
        LinearLayoutManager.LayoutChunkResult layoutChunkResult = result;
        int modeInOther = this.mOrientationHelper.getModeInOther();
        boolean z3 = modeInOther != 1073741824;
        int i11 = getChildCount() > 0 ? this.mCachedBorders[this.mSpanCount] : 0;
        if (z3) {
            updateMeasurements();
        }
        boolean z4 = layoutState2.mItemDirection == 1;
        int i12 = this.mSpanCount;
        if (!z4) {
            i12 = getSpanIndex(recycler2, state2, layoutState2.mCurrentPosition) + getSpanSize(recycler2, state2, layoutState2.mCurrentPosition);
            i2 = 0;
            i = 0;
        } else {
            i2 = 0;
            i = 0;
        }
        while (i2 < this.mSpanCount && layoutState2.hasMore(state2) && i12 > 0) {
            int i13 = layoutState2.mCurrentPosition;
            int spanSize = getSpanSize(recycler2, state2, i13);
            if (spanSize <= this.mSpanCount) {
                i12 -= spanSize;
                if (i12 < 0 || (next = layoutState2.next(recycler2)) == null) {
                    break;
                }
                i += spanSize;
                this.mSet[i2] = next;
                i2++;
            } else {
                throw new IllegalArgumentException("Item at position " + i13 + " requires " + spanSize + " spans but GridLayoutManager has only " + this.mSpanCount + " spans.");
            }
        }
        int i14 = i12;
        if (i2 == 0) {
            layoutChunkResult.mFinished = true;
            return;
        }
        int i15 = 0;
        assignSpans(recycler2, state2, i2, z4);
        int i16 = 0;
        float f2 = 0.0f;
        while (i16 < i2) {
            View view = this.mSet[i16];
            if (layoutState2.mScrapList != null) {
                z2 = false;
                if (z4) {
                    addDisappearingView(view);
                } else {
                    addDisappearingView(view, 0);
                }
            } else if (z4) {
                addView(view);
                z2 = false;
            } else {
                z2 = false;
                addView(view, 0);
            }
            calculateItemDecorationsForChild(view, this.mDecorInsets);
            measureChild(view, modeInOther, z2);
            int decoratedMeasurement = this.mOrientationHelper.getDecoratedMeasurement(view);
            if (decoratedMeasurement > i15) {
                i15 = decoratedMeasurement;
            }
            int i17 = i15;
            View view2 = view;
            float decoratedMeasurementInOther = (((float) this.mOrientationHelper.getDecoratedMeasurementInOther(view)) * 1.0f) / ((float) ((LayoutParams) view.getLayoutParams()).mSpanSize);
            if (decoratedMeasurementInOther > f2) {
                f2 = decoratedMeasurementInOther;
            }
            i16++;
            i15 = i17;
        }
        if (z3) {
            guessMeasurement(f2, i11);
            int i18 = 0;
            for (int i19 = 0; i19 < i2; i19++) {
                View view3 = this.mSet[i19];
                measureChild(view3, BasicMeasure.EXACTLY, true);
                int decoratedMeasurement2 = this.mOrientationHelper.getDecoratedMeasurement(view3);
                if (decoratedMeasurement2 > i18) {
                    i18 = decoratedMeasurement2;
                }
            }
            i3 = i18;
        } else {
            i3 = i15;
        }
        int i20 = 0;
        while (i20 < i2) {
            View view4 = this.mSet[i20];
            if (this.mOrientationHelper.getDecoratedMeasurement(view4) != i3) {
                LayoutParams layoutParams = (LayoutParams) view4.getLayoutParams();
                Rect rect = layoutParams.mDecorInsets;
                f = f2;
                int i21 = rect.top + rect.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
                int i22 = rect.left + rect.right + layoutParams.leftMargin + layoutParams.rightMargin;
                Rect rect2 = rect;
                int spaceForSpanRange = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
                i8 = modeInOther;
                if (this.mOrientation == 1) {
                    z = z3;
                    i10 = getChildMeasureSpec(spaceForSpanRange, BasicMeasure.EXACTLY, i22, layoutParams.width, false);
                    i9 = View.MeasureSpec.makeMeasureSpec(i3 - i21, BasicMeasure.EXACTLY);
                    LayoutParams layoutParams2 = layoutParams;
                } else {
                    z = z3;
                    i10 = View.MeasureSpec.makeMeasureSpec(i3 - i22, BasicMeasure.EXACTLY);
                    LayoutParams layoutParams3 = layoutParams;
                    i9 = getChildMeasureSpec(spaceForSpanRange, BasicMeasure.EXACTLY, i21, layoutParams.height, false);
                }
                measureChildWithDecorationsAndMargin(view4, i10, i9, true);
            } else {
                f = f2;
                i8 = modeInOther;
                z = z3;
            }
            i20++;
            RecyclerView.Recycler recycler3 = recycler;
            RecyclerView.State state3 = state;
            z3 = z;
            f2 = f;
            modeInOther = i8;
        }
        float f3 = f2;
        int i23 = modeInOther;
        boolean z5 = z3;
        layoutChunkResult.mConsumed = i3;
        int i24 = 0;
        int i25 = 0;
        int i26 = 0;
        int i27 = 0;
        if (this.mOrientation == 1) {
            if (layoutState2.mLayoutDirection == -1) {
                i27 = layoutState2.mOffset;
                i26 = i27 - i3;
            } else {
                i26 = layoutState2.mOffset;
                i27 = i26 + i3;
            }
        } else if (layoutState2.mLayoutDirection == -1) {
            i25 = layoutState2.mOffset;
            i24 = i25 - i3;
        } else {
            i24 = layoutState2.mOffset;
            i25 = i24 + i3;
        }
        int i28 = 0;
        while (i28 < i2) {
            View view5 = this.mSet[i28];
            LayoutParams layoutParams4 = (LayoutParams) view5.getLayoutParams();
            if (this.mOrientation != 1) {
                i6 = i24;
                i5 = i25;
                int paddingTop = getPaddingTop() + this.mCachedBorders[layoutParams4.mSpanIndex];
                i7 = paddingTop;
                i4 = this.mOrientationHelper.getDecoratedMeasurementInOther(view5) + paddingTop;
            } else if (isLayoutRTL()) {
                int i29 = i24;
                int i30 = i25;
                int paddingLeft = getPaddingLeft() + this.mCachedBorders[this.mSpanCount - layoutParams4.mSpanIndex];
                i6 = paddingLeft - this.mOrientationHelper.getDecoratedMeasurementInOther(view5);
                i7 = i26;
                i4 = i27;
                i5 = paddingLeft;
            } else {
                int i31 = i24;
                int i32 = i25;
                int paddingLeft2 = getPaddingLeft() + this.mCachedBorders[layoutParams4.mSpanIndex];
                i6 = paddingLeft2;
                i5 = this.mOrientationHelper.getDecoratedMeasurementInOther(view5) + paddingLeft2;
                i7 = i26;
                i4 = i27;
            }
            int i33 = i2;
            layoutDecoratedWithMargins(view5, i6, i7, i5, i4);
            if (layoutParams4.isItemRemoved() || layoutParams4.isItemChanged()) {
                layoutChunkResult.mIgnoreConsumed = true;
            }
            layoutChunkResult.mFocusable |= view5.hasFocusable();
            i28++;
            i26 = i7;
            i24 = i6;
            i25 = i5;
            i27 = i4;
            i2 = i33;
        }
        int i34 = i24;
        int i35 = i25;
        Arrays.fill(this.mSet, (Object) null);
    }

    /* access modifiers changed from: package-private */
    public void onAnchorReady(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.AnchorInfo anchorInfo, int itemDirection) {
        super.onAnchorReady(recycler, state, anchorInfo, itemDirection);
        updateMeasurements();
        if (state.getItemCount() > 0 && !state.isPreLayout()) {
            ensureAnchorIsInCorrectSpan(recycler, state, anchorInfo, itemDirection);
        }
        ensureViewSet();
    }

    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int i;
        int i2;
        int i3;
        int i4;
        View view;
        int i5;
        int i6;
        boolean z;
        RecyclerView.Recycler recycler2 = recycler;
        RecyclerView.State state2 = state;
        View findContainingItemView = findContainingItemView(focused);
        if (findContainingItemView == null) {
            return null;
        }
        LayoutParams layoutParams = (LayoutParams) findContainingItemView.getLayoutParams();
        int i7 = layoutParams.mSpanIndex;
        int i8 = layoutParams.mSpanIndex + layoutParams.mSpanSize;
        View onFocusSearchFailed = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
        if (onFocusSearchFailed == null) {
            return null;
        }
        int convertFocusDirectionToLayoutDirection = convertFocusDirectionToLayoutDirection(focusDirection);
        boolean z2 = (convertFocusDirectionToLayoutDirection == 1) != this.mShouldReverseLayout;
        if (z2) {
            i3 = getChildCount() - 1;
            i2 = -1;
            i = -1;
        } else {
            i3 = 0;
            i2 = 1;
            i = getChildCount();
        }
        boolean z3 = this.mOrientation == 1 && isLayoutRTL();
        View view2 = null;
        View view3 = null;
        int spanGroupIndex = getSpanGroupIndex(recycler2, state2, i3);
        int i9 = -1;
        LayoutParams layoutParams2 = layoutParams;
        int i10 = 0;
        View view4 = onFocusSearchFailed;
        int i11 = -1;
        int i12 = convertFocusDirectionToLayoutDirection;
        int i13 = 0;
        boolean z4 = z2;
        int i14 = i3;
        while (true) {
            if (i14 == i) {
                View view5 = findContainingItemView;
                int i15 = i9;
                int i16 = i10;
                int i17 = spanGroupIndex;
                int i18 = i3;
                break;
            }
            int i19 = i3;
            int spanGroupIndex2 = getSpanGroupIndex(recycler2, state2, i14);
            View childAt = getChildAt(i14);
            if (childAt == findContainingItemView) {
                View view6 = findContainingItemView;
                int i20 = i9;
                int i21 = i10;
                int i22 = spanGroupIndex;
                break;
            }
            if (!childAt.hasFocusable() || spanGroupIndex2 == spanGroupIndex) {
                LayoutParams layoutParams3 = (LayoutParams) childAt.getLayoutParams();
                view = findContainingItemView;
                int i23 = layoutParams3.mSpanIndex;
                i4 = spanGroupIndex;
                int i24 = spanGroupIndex2;
                int i25 = layoutParams3.mSpanIndex + layoutParams3.mSpanSize;
                if (childAt.hasFocusable() && i23 == i7 && i25 == i8) {
                    return childAt;
                }
                if ((!childAt.hasFocusable() || view2 != null) && (childAt.hasFocusable() || view3 != null)) {
                    int min = Math.min(i25, i8) - Math.max(i23, i7);
                    if (!childAt.hasFocusable()) {
                        i5 = i9;
                        if (view2 == null) {
                            i6 = i10;
                            boolean z5 = false;
                            if (isViewPartiallyVisible(childAt, false, true)) {
                                if (min > i13) {
                                    z = true;
                                } else if (min == i13) {
                                    if (i23 > i11) {
                                        z5 = true;
                                    }
                                    if (z3 == z5) {
                                        z = true;
                                    }
                                }
                            }
                        } else {
                            i6 = i10;
                        }
                    } else if (min > i10) {
                        i5 = i9;
                        i6 = i10;
                        z = true;
                    } else {
                        if (min == i10) {
                            i5 = i9;
                            if (z3 == (i23 > i9)) {
                                z = true;
                                i6 = i10;
                            }
                        } else {
                            i5 = i9;
                        }
                        i6 = i10;
                    }
                    z = false;
                } else {
                    z = true;
                    i5 = i9;
                    i6 = i10;
                }
                if (z) {
                    if (childAt.hasFocusable()) {
                        view2 = childAt;
                        i9 = layoutParams3.mSpanIndex;
                        i10 = Math.min(i25, i8) - Math.max(i23, i7);
                    } else {
                        int i26 = layoutParams3.mSpanIndex;
                        view3 = childAt;
                        i13 = Math.min(i25, i8) - Math.max(i23, i7);
                        i9 = i5;
                        i11 = i26;
                        i10 = i6;
                    }
                    i14 += i2;
                    recycler2 = recycler;
                    state2 = state;
                    i3 = i19;
                    findContainingItemView = view;
                    spanGroupIndex = i4;
                }
            } else if (view2 != null) {
                View view7 = findContainingItemView;
                int i27 = i9;
                int i28 = i10;
                int i29 = spanGroupIndex;
                break;
            } else {
                view = findContainingItemView;
                i5 = i9;
                i6 = i10;
                i4 = spanGroupIndex;
            }
            i10 = i6;
            i9 = i5;
            i14 += i2;
            recycler2 = recycler;
            state2 = state;
            i3 = i19;
            findContainingItemView = view;
            spanGroupIndex = i4;
        }
        return view2 != null ? view2 : view3;
    }

    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler recycler, RecyclerView.State state, View host, AccessibilityNodeInfoCompat info) {
        ViewGroup.LayoutParams layoutParams = host.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            super.onInitializeAccessibilityNodeInfoForItem(host, info);
            return;
        }
        LayoutParams layoutParams2 = (LayoutParams) layoutParams;
        int spanGroupIndex = getSpanGroupIndex(recycler, state, layoutParams2.getViewLayoutPosition());
        if (this.mOrientation == 0) {
            info.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(layoutParams2.getSpanIndex(), layoutParams2.getSpanSize(), spanGroupIndex, 1, false, false));
            return;
        }
        info.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(spanGroupIndex, 1, layoutParams2.getSpanIndex(), layoutParams2.getSpanSize(), false, false));
    }

    public void onItemsAdded(RecyclerView recyclerView, int positionStart, int itemCount) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    public void onItemsChanged(RecyclerView recyclerView) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    public void onItemsMoved(RecyclerView recyclerView, int from, int to, int itemCount) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    public void onItemsRemoved(RecyclerView recyclerView, int positionStart, int itemCount) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    public void onItemsUpdated(RecyclerView recyclerView, int positionStart, int itemCount, Object payload) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) {
            cachePreLayoutSpanMapping();
        }
        super.onLayoutChildren(recycler, state);
        clearPreLayoutSpanMappingCache();
    }

    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        this.mPendingSpanCountChange = false;
    }

    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        updateMeasurements();
        ensureViewSet();
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        updateMeasurements();
        ensureViewSet();
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
        int i;
        int i2;
        if (this.mCachedBorders == null) {
            super.setMeasuredDimension(childrenBounds, wSpec, hSpec);
        }
        int paddingLeft = getPaddingLeft() + getPaddingRight();
        int paddingTop = getPaddingTop() + getPaddingBottom();
        if (this.mOrientation == 1) {
            i = chooseSize(hSpec, childrenBounds.height() + paddingTop, getMinimumHeight());
            int[] iArr = this.mCachedBorders;
            i2 = chooseSize(wSpec, iArr[iArr.length - 1] + paddingLeft, getMinimumWidth());
        } else {
            int chooseSize = chooseSize(wSpec, childrenBounds.width() + paddingLeft, getMinimumWidth());
            int[] iArr2 = this.mCachedBorders;
            i2 = chooseSize;
            i = chooseSize(hSpec, iArr2[iArr2.length - 1] + paddingTop, getMinimumHeight());
        }
        setMeasuredDimension(i2, i);
    }

    public void setSpanCount(int spanCount) {
        if (spanCount != this.mSpanCount) {
            this.mPendingSpanCountChange = true;
            if (spanCount >= 1) {
                this.mSpanCount = spanCount;
                this.mSpanSizeLookup.invalidateSpanIndexCache();
                requestLayout();
                return;
            }
            throw new IllegalArgumentException("Span count should be at least 1. Provided " + spanCount);
        }
    }

    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

    public void setStackFromEnd(boolean stackFromEnd) {
        if (!stackFromEnd) {
            super.setStackFromEnd(false);
            return;
        }
        throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
    }

    public void setUsingSpansToEstimateScrollbarDimensions(boolean useSpansToEstimateScrollBarDimensions) {
        this.mUsingSpansToEstimateScrollBarDimensions = useSpansToEstimateScrollBarDimensions;
    }

    public boolean supportsPredictiveItemAnimations() {
        return this.mPendingSavedState == null && !this.mPendingSpanCountChange;
    }
}

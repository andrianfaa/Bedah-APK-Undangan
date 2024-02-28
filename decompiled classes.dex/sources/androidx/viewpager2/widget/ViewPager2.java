package androidx.viewpager2.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.R;
import androidx.viewpager2.adapter.StatefulAdapter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ViewPager2 extends ViewGroup {
    public static final int OFFSCREEN_PAGE_LIMIT_DEFAULT = -1;
    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    static boolean sFeatureEnhancedA11yEnabled = true;
    AccessibilityProvider mAccessibilityProvider;
    int mCurrentItem;
    private RecyclerView.AdapterDataObserver mCurrentItemDataSetChangeObserver = new DataSetChangeObserver() {
        public void onChanged() {
            ViewPager2.this.mCurrentItemDirty = true;
            ViewPager2.this.mScrollEventAdapter.notifyDataSetChangeHappened();
        }
    };
    boolean mCurrentItemDirty = false;
    private CompositeOnPageChangeCallback mExternalPageChangeCallbacks = new CompositeOnPageChangeCallback(3);
    private FakeDrag mFakeDragger;
    private LinearLayoutManager mLayoutManager;
    private int mOffscreenPageLimit = -1;
    private CompositeOnPageChangeCallback mPageChangeEventDispatcher;
    private PageTransformerAdapter mPageTransformerAdapter;
    private PagerSnapHelper mPagerSnapHelper;
    private Parcelable mPendingAdapterState;
    private int mPendingCurrentItem = -1;
    RecyclerView mRecyclerView;
    private RecyclerView.ItemAnimator mSavedItemAnimator = null;
    private boolean mSavedItemAnimatorPresent = false;
    ScrollEventAdapter mScrollEventAdapter;
    private final Rect mTmpChildRect = new Rect();
    private final Rect mTmpContainerRect = new Rect();
    private boolean mUserInputEnabled = true;

    private abstract class AccessibilityProvider {
        private AccessibilityProvider() {
        }

        /* access modifiers changed from: package-private */
        public boolean handlesGetAccessibilityClassName() {
            return false;
        }

        /* access modifiers changed from: package-private */
        public boolean handlesLmPerformAccessibilityAction(int action) {
            return false;
        }

        /* access modifiers changed from: package-private */
        public boolean handlesPerformAccessibilityAction(int action, Bundle arguments) {
            return false;
        }

        /* access modifiers changed from: package-private */
        public boolean handlesRvGetAccessibilityClassName() {
            return false;
        }

        /* access modifiers changed from: package-private */
        public void onAttachAdapter(RecyclerView.Adapter<?> adapter) {
        }

        /* access modifiers changed from: package-private */
        public void onDetachAdapter(RecyclerView.Adapter<?> adapter) {
        }

        /* access modifiers changed from: package-private */
        public String onGetAccessibilityClassName() {
            throw new IllegalStateException("Not implemented.");
        }

        /* access modifiers changed from: package-private */
        public void onInitialize(CompositeOnPageChangeCallback pageChangeEventDispatcher, RecyclerView recyclerView) {
        }

        /* access modifiers changed from: package-private */
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        }

        /* access modifiers changed from: package-private */
        public void onLmInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat info) {
        }

        /* access modifiers changed from: package-private */
        public boolean onLmPerformAccessibilityAction(int action) {
            throw new IllegalStateException("Not implemented.");
        }

        /* access modifiers changed from: package-private */
        public boolean onPerformAccessibilityAction(int action, Bundle arguments) {
            throw new IllegalStateException("Not implemented.");
        }

        /* access modifiers changed from: package-private */
        public void onRestorePendingState() {
        }

        /* access modifiers changed from: package-private */
        public CharSequence onRvGetAccessibilityClassName() {
            throw new IllegalStateException("Not implemented.");
        }

        /* access modifiers changed from: package-private */
        public void onRvInitializeAccessibilityEvent(AccessibilityEvent event) {
        }

        /* access modifiers changed from: package-private */
        public void onSetLayoutDirection() {
        }

        /* access modifiers changed from: package-private */
        public void onSetNewCurrentItem() {
        }

        /* access modifiers changed from: package-private */
        public void onSetOrientation() {
        }

        /* access modifiers changed from: package-private */
        public void onSetUserInputEnabled() {
        }
    }

    class BasicAccessibilityProvider extends AccessibilityProvider {
        BasicAccessibilityProvider() {
            super();
        }

        public boolean handlesLmPerformAccessibilityAction(int action) {
            return (action == 8192 || action == 4096) && !ViewPager2.this.isUserInputEnabled();
        }

        public boolean handlesRvGetAccessibilityClassName() {
            return true;
        }

        public void onLmInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat info) {
            if (!ViewPager2.this.isUserInputEnabled()) {
                info.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD);
                info.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_FORWARD);
                info.setScrollable(false);
            }
        }

        public boolean onLmPerformAccessibilityAction(int action) {
            if (handlesLmPerformAccessibilityAction(action)) {
                return false;
            }
            throw new IllegalStateException();
        }

        public CharSequence onRvGetAccessibilityClassName() {
            if (handlesRvGetAccessibilityClassName()) {
                return "androidx.viewpager.widget.ViewPager";
            }
            throw new IllegalStateException();
        }
    }

    private static abstract class DataSetChangeObserver extends RecyclerView.AdapterDataObserver {
        private DataSetChangeObserver() {
        }

        public abstract void onChanged();

        public final void onItemRangeChanged(int positionStart, int itemCount) {
            onChanged();
        }

        public final void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            onChanged();
        }

        public final void onItemRangeInserted(int positionStart, int itemCount) {
            onChanged();
        }

        public final void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            onChanged();
        }

        public final void onItemRangeRemoved(int positionStart, int itemCount) {
            onChanged();
        }
    }

    private class LinearLayoutManagerImpl extends LinearLayoutManager {
        LinearLayoutManagerImpl(Context context) {
            super(context);
        }

        /* access modifiers changed from: protected */
        public void calculateExtraLayoutSpace(RecyclerView.State state, int[] extraLayoutSpace) {
            int offscreenPageLimit = ViewPager2.this.getOffscreenPageLimit();
            if (offscreenPageLimit == -1) {
                super.calculateExtraLayoutSpace(state, extraLayoutSpace);
                return;
            }
            int pageSize = ViewPager2.this.getPageSize() * offscreenPageLimit;
            extraLayoutSpace[0] = pageSize;
            extraLayoutSpace[1] = pageSize;
        }

        public void onInitializeAccessibilityNodeInfo(RecyclerView.Recycler recycler, RecyclerView.State state, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(recycler, state, info);
            ViewPager2.this.mAccessibilityProvider.onLmInitializeAccessibilityNodeInfo(info);
        }

        public boolean performAccessibilityAction(RecyclerView.Recycler recycler, RecyclerView.State state, int action, Bundle args) {
            return ViewPager2.this.mAccessibilityProvider.handlesLmPerformAccessibilityAction(action) ? ViewPager2.this.mAccessibilityProvider.onLmPerformAccessibilityAction(action) : super.performAccessibilityAction(recycler, state, action, args);
        }

        public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
            return false;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface OffscreenPageLimit {
    }

    public static abstract class OnPageChangeCallback {
        public void onPageScrollStateChanged(int state) {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {
    }

    class PageAwareAccessibilityProvider extends AccessibilityProvider {
        private final AccessibilityViewCommand mActionPageBackward = new AccessibilityViewCommand() {
            public boolean perform(View view, AccessibilityViewCommand.CommandArguments arguments) {
                PageAwareAccessibilityProvider.this.setCurrentItemFromAccessibilityCommand(((ViewPager2) view).getCurrentItem() - 1);
                return true;
            }
        };
        private final AccessibilityViewCommand mActionPageForward = new AccessibilityViewCommand() {
            public boolean perform(View view, AccessibilityViewCommand.CommandArguments arguments) {
                PageAwareAccessibilityProvider.this.setCurrentItemFromAccessibilityCommand(((ViewPager2) view).getCurrentItem() + 1);
                return true;
            }
        };
        private RecyclerView.AdapterDataObserver mAdapterDataObserver;

        PageAwareAccessibilityProvider() {
            super();
        }

        private void addCollectionInfo(AccessibilityNodeInfo info) {
            int i = 0;
            int i2 = 0;
            if (ViewPager2.this.getAdapter() != null) {
                if (ViewPager2.this.getOrientation() == 1) {
                    i = ViewPager2.this.getAdapter().getItemCount();
                } else {
                    i2 = ViewPager2.this.getAdapter().getItemCount();
                }
            }
            AccessibilityNodeInfoCompat.wrap(info).setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(i, i2, false, 0));
        }

        private void addScrollActions(AccessibilityNodeInfo info) {
            int itemCount;
            RecyclerView.Adapter adapter = ViewPager2.this.getAdapter();
            if (adapter != null && (itemCount = adapter.getItemCount()) != 0 && ViewPager2.this.isUserInputEnabled()) {
                if (ViewPager2.this.mCurrentItem > 0) {
                    info.addAction(8192);
                }
                if (ViewPager2.this.mCurrentItem < itemCount - 1) {
                    info.addAction(4096);
                }
                info.setScrollable(true);
            }
        }

        public boolean handlesGetAccessibilityClassName() {
            return true;
        }

        public boolean handlesPerformAccessibilityAction(int action, Bundle arguments) {
            return action == 8192 || action == 4096;
        }

        public void onAttachAdapter(RecyclerView.Adapter<?> adapter) {
            updatePageAccessibilityActions();
            if (adapter != null) {
                adapter.registerAdapterDataObserver(this.mAdapterDataObserver);
            }
        }

        public void onDetachAdapter(RecyclerView.Adapter<?> adapter) {
            if (adapter != null) {
                adapter.unregisterAdapterDataObserver(this.mAdapterDataObserver);
            }
        }

        public String onGetAccessibilityClassName() {
            if (handlesGetAccessibilityClassName()) {
                return "androidx.viewpager.widget.ViewPager";
            }
            throw new IllegalStateException();
        }

        public void onInitialize(CompositeOnPageChangeCallback pageChangeEventDispatcher, RecyclerView recyclerView) {
            ViewCompat.setImportantForAccessibility(recyclerView, 2);
            this.mAdapterDataObserver = new DataSetChangeObserver() {
                public void onChanged() {
                    PageAwareAccessibilityProvider.this.updatePageAccessibilityActions();
                }
            };
            if (ViewCompat.getImportantForAccessibility(ViewPager2.this) == 0) {
                ViewCompat.setImportantForAccessibility(ViewPager2.this, 1);
            }
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            addCollectionInfo(info);
            if (Build.VERSION.SDK_INT >= 16) {
                addScrollActions(info);
            }
        }

        public boolean onPerformAccessibilityAction(int action, Bundle arguments) {
            if (handlesPerformAccessibilityAction(action, arguments)) {
                setCurrentItemFromAccessibilityCommand(action == 8192 ? ViewPager2.this.getCurrentItem() - 1 : ViewPager2.this.getCurrentItem() + 1);
                return true;
            }
            throw new IllegalStateException();
        }

        public void onRestorePendingState() {
            updatePageAccessibilityActions();
        }

        public void onRvInitializeAccessibilityEvent(AccessibilityEvent event) {
            event.setSource(ViewPager2.this);
            event.setClassName(onGetAccessibilityClassName());
        }

        public void onSetLayoutDirection() {
            updatePageAccessibilityActions();
        }

        public void onSetNewCurrentItem() {
            updatePageAccessibilityActions();
        }

        public void onSetOrientation() {
            updatePageAccessibilityActions();
        }

        public void onSetUserInputEnabled() {
            updatePageAccessibilityActions();
            if (Build.VERSION.SDK_INT < 21) {
                ViewPager2.this.sendAccessibilityEvent(2048);
            }
        }

        /* access modifiers changed from: package-private */
        public void setCurrentItemFromAccessibilityCommand(int item) {
            if (ViewPager2.this.isUserInputEnabled()) {
                ViewPager2.this.setCurrentItemInternal(item, true);
            }
        }

        /* access modifiers changed from: package-private */
        public void updatePageAccessibilityActions() {
            int itemCount;
            ViewPager2 viewPager2 = ViewPager2.this;
            int i = 16908360;
            ViewCompat.removeAccessibilityAction(viewPager2, 16908360);
            ViewCompat.removeAccessibilityAction(viewPager2, 16908361);
            ViewCompat.removeAccessibilityAction(viewPager2, 16908358);
            ViewCompat.removeAccessibilityAction(viewPager2, 16908359);
            if (ViewPager2.this.getAdapter() != null && (itemCount = ViewPager2.this.getAdapter().getItemCount()) != 0 && ViewPager2.this.isUserInputEnabled()) {
                if (ViewPager2.this.getOrientation() == 0) {
                    boolean isRtl = ViewPager2.this.isRtl();
                    int i2 = isRtl ? 16908360 : 16908361;
                    if (isRtl) {
                        i = 16908361;
                    }
                    if (ViewPager2.this.mCurrentItem < itemCount - 1) {
                        ViewCompat.replaceAccessibilityAction(viewPager2, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(i2, (CharSequence) null), (CharSequence) null, this.mActionPageForward);
                    }
                    if (ViewPager2.this.mCurrentItem > 0) {
                        ViewCompat.replaceAccessibilityAction(viewPager2, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(i, (CharSequence) null), (CharSequence) null, this.mActionPageBackward);
                        return;
                    }
                    return;
                }
                if (ViewPager2.this.mCurrentItem < itemCount - 1) {
                    ViewCompat.replaceAccessibilityAction(viewPager2, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16908359, (CharSequence) null), (CharSequence) null, this.mActionPageForward);
                }
                if (ViewPager2.this.mCurrentItem > 0) {
                    ViewCompat.replaceAccessibilityAction(viewPager2, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16908358, (CharSequence) null), (CharSequence) null, this.mActionPageBackward);
                }
            }
        }
    }

    public interface PageTransformer {
        void transformPage(View view, float f);
    }

    private class PagerSnapHelperImpl extends PagerSnapHelper {
        PagerSnapHelperImpl() {
        }

        public View findSnapView(RecyclerView.LayoutManager layoutManager) {
            if (ViewPager2.this.isFakeDragging()) {
                return null;
            }
            return super.findSnapView(layoutManager);
        }
    }

    private class RecyclerViewImpl extends RecyclerView {
        RecyclerViewImpl(Context context) {
            super(context);
        }

        public CharSequence getAccessibilityClassName() {
            return ViewPager2.this.mAccessibilityProvider.handlesRvGetAccessibilityClassName() ? ViewPager2.this.mAccessibilityProvider.onRvGetAccessibilityClassName() : super.getAccessibilityClassName();
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(event);
            event.setFromIndex(ViewPager2.this.mCurrentItem);
            event.setToIndex(ViewPager2.this.mCurrentItem);
            ViewPager2.this.mAccessibilityProvider.onRvInitializeAccessibilityEvent(event);
        }

        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return ViewPager2.this.isUserInputEnabled() && super.onInterceptTouchEvent(ev);
        }

        public boolean onTouchEvent(MotionEvent event) {
            return ViewPager2.this.isUserInputEnabled() && super.onTouchEvent(event);
        }
    }

    static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
            public SavedState createFromParcel(Parcel source) {
                return createFromParcel(source, (ClassLoader) null);
            }

            public SavedState createFromParcel(Parcel source, ClassLoader loader) {
                return Build.VERSION.SDK_INT >= 24 ? new SavedState(source, loader) : new SavedState(source);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        Parcelable mAdapterState;
        int mCurrentItem;
        int mRecyclerViewId;

        SavedState(Parcel source) {
            super(source);
            readValues(source, (ClassLoader) null);
        }

        SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            readValues(source, loader);
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        private void readValues(Parcel source, ClassLoader loader) {
            this.mRecyclerViewId = source.readInt();
            this.mCurrentItem = source.readInt();
            this.mAdapterState = source.readParcelable(loader);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.mRecyclerViewId);
            out.writeInt(this.mCurrentItem);
            out.writeParcelable(this.mAdapterState, flags);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollState {
    }

    private static class SmoothScrollToPosition implements Runnable {
        private final int mPosition;
        private final RecyclerView mRecyclerView;

        SmoothScrollToPosition(int position, RecyclerView recyclerView) {
            this.mPosition = position;
            this.mRecyclerView = recyclerView;
        }

        public void run() {
            this.mRecyclerView.smoothScrollToPosition(this.mPosition);
        }
    }

    public ViewPager2(Context context) {
        super(context);
        initialize(context, (AttributeSet) null);
    }

    public ViewPager2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public ViewPager2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    public ViewPager2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context, attrs);
    }

    private RecyclerView.OnChildAttachStateChangeListener enforceChildFillListener() {
        return new RecyclerView.OnChildAttachStateChangeListener() {
            public void onChildViewAttachedToWindow(View view) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                if (layoutParams.width != -1 || layoutParams.height != -1) {
                    throw new IllegalStateException("Pages must fill the whole ViewPager2 (use match_parent)");
                }
            }

            public void onChildViewDetachedFromWindow(View view) {
            }
        };
    }

    private void initialize(Context context, AttributeSet attrs) {
        this.mAccessibilityProvider = sFeatureEnhancedA11yEnabled ? new PageAwareAccessibilityProvider() : new BasicAccessibilityProvider();
        RecyclerViewImpl recyclerViewImpl = new RecyclerViewImpl(context);
        this.mRecyclerView = recyclerViewImpl;
        recyclerViewImpl.setId(ViewCompat.generateViewId());
        this.mRecyclerView.setDescendantFocusability(131072);
        LinearLayoutManagerImpl linearLayoutManagerImpl = new LinearLayoutManagerImpl(context);
        this.mLayoutManager = linearLayoutManagerImpl;
        this.mRecyclerView.setLayoutManager(linearLayoutManagerImpl);
        this.mRecyclerView.setScrollingTouchSlop(1);
        setOrientation(context, attrs);
        this.mRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.mRecyclerView.addOnChildAttachStateChangeListener(enforceChildFillListener());
        this.mScrollEventAdapter = new ScrollEventAdapter(this);
        this.mFakeDragger = new FakeDrag(this, this.mScrollEventAdapter, this.mRecyclerView);
        PagerSnapHelperImpl pagerSnapHelperImpl = new PagerSnapHelperImpl();
        this.mPagerSnapHelper = pagerSnapHelperImpl;
        pagerSnapHelperImpl.attachToRecyclerView(this.mRecyclerView);
        this.mRecyclerView.addOnScrollListener(this.mScrollEventAdapter);
        CompositeOnPageChangeCallback compositeOnPageChangeCallback = new CompositeOnPageChangeCallback(3);
        this.mPageChangeEventDispatcher = compositeOnPageChangeCallback;
        this.mScrollEventAdapter.setOnPageChangeCallback(compositeOnPageChangeCallback);
        AnonymousClass2 r0 = new OnPageChangeCallback() {
            public void onPageScrollStateChanged(int newState) {
                if (newState == 0) {
                    ViewPager2.this.updateCurrentItem();
                }
            }

            public void onPageSelected(int position) {
                if (ViewPager2.this.mCurrentItem != position) {
                    ViewPager2.this.mCurrentItem = position;
                    ViewPager2.this.mAccessibilityProvider.onSetNewCurrentItem();
                }
            }
        };
        AnonymousClass3 r1 = new OnPageChangeCallback() {
            public void onPageSelected(int position) {
                ViewPager2.this.clearFocus();
                if (ViewPager2.this.hasFocus()) {
                    ViewPager2.this.mRecyclerView.requestFocus(2);
                }
            }
        };
        this.mPageChangeEventDispatcher.addOnPageChangeCallback(r0);
        this.mPageChangeEventDispatcher.addOnPageChangeCallback(r1);
        this.mAccessibilityProvider.onInitialize(this.mPageChangeEventDispatcher, this.mRecyclerView);
        this.mPageChangeEventDispatcher.addOnPageChangeCallback(this.mExternalPageChangeCallbacks);
        PageTransformerAdapter pageTransformerAdapter = new PageTransformerAdapter(this.mLayoutManager);
        this.mPageTransformerAdapter = pageTransformerAdapter;
        this.mPageChangeEventDispatcher.addOnPageChangeCallback(pageTransformerAdapter);
        RecyclerView recyclerView = this.mRecyclerView;
        attachViewToParent(recyclerView, 0, recyclerView.getLayoutParams());
    }

    private void registerCurrentItemDataSetTracker(RecyclerView.Adapter<?> adapter) {
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.mCurrentItemDataSetChangeObserver);
        }
    }

    private void restorePendingState() {
        RecyclerView.Adapter adapter;
        if (this.mPendingCurrentItem != -1 && (adapter = getAdapter()) != null) {
            Parcelable parcelable = this.mPendingAdapterState;
            if (parcelable != null) {
                if (adapter instanceof StatefulAdapter) {
                    ((StatefulAdapter) adapter).restoreState(parcelable);
                }
                this.mPendingAdapterState = null;
            }
            int max = Math.max(0, Math.min(this.mPendingCurrentItem, adapter.getItemCount() - 1));
            this.mCurrentItem = max;
            this.mPendingCurrentItem = -1;
            this.mRecyclerView.scrollToPosition(max);
            this.mAccessibilityProvider.onRestorePendingState();
        }
    }

    private void setOrientation(Context context, AttributeSet attrs) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ViewPager2);
        if (Build.VERSION.SDK_INT >= 29) {
            saveAttributeDataForStyleable(context, R.styleable.ViewPager2, attrs, obtainStyledAttributes, 0, 0);
        }
        try {
            setOrientation(obtainStyledAttributes.getInt(R.styleable.ViewPager2_android_orientation, 0));
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    private void unregisterCurrentItemDataSetTracker(RecyclerView.Adapter<?> adapter) {
        if (adapter != null) {
            adapter.unregisterAdapterDataObserver(this.mCurrentItemDataSetChangeObserver);
        }
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        this.mRecyclerView.addItemDecoration(decor);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor, int index) {
        this.mRecyclerView.addItemDecoration(decor, index);
    }

    public boolean beginFakeDrag() {
        return this.mFakeDragger.beginFakeDrag();
    }

    public boolean canScrollHorizontally(int direction) {
        return this.mRecyclerView.canScrollHorizontally(direction);
    }

    public boolean canScrollVertically(int direction) {
        return this.mRecyclerView.canScrollVertically(direction);
    }

    /* access modifiers changed from: protected */
    public void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        Parcelable parcelable = sparseArray.get(getId());
        if (parcelable instanceof SavedState) {
            int i = ((SavedState) parcelable).mRecyclerViewId;
            sparseArray.put(this.mRecyclerView.getId(), sparseArray.get(i));
            sparseArray.remove(i);
        }
        super.dispatchRestoreInstanceState(sparseArray);
        restorePendingState();
    }

    public boolean endFakeDrag() {
        return this.mFakeDragger.endFakeDrag();
    }

    public boolean fakeDragBy(float offsetPxFloat) {
        return this.mFakeDragger.fakeDragBy(offsetPxFloat);
    }

    public CharSequence getAccessibilityClassName() {
        return this.mAccessibilityProvider.handlesGetAccessibilityClassName() ? this.mAccessibilityProvider.onGetAccessibilityClassName() : super.getAccessibilityClassName();
    }

    public RecyclerView.Adapter getAdapter() {
        return this.mRecyclerView.getAdapter();
    }

    public int getCurrentItem() {
        return this.mCurrentItem;
    }

    public RecyclerView.ItemDecoration getItemDecorationAt(int index) {
        return this.mRecyclerView.getItemDecorationAt(index);
    }

    public int getItemDecorationCount() {
        return this.mRecyclerView.getItemDecorationCount();
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public int getOrientation() {
        return this.mLayoutManager.getOrientation();
    }

    /* access modifiers changed from: package-private */
    public int getPageSize() {
        RecyclerView recyclerView = this.mRecyclerView;
        return getOrientation() == 0 ? (recyclerView.getWidth() - recyclerView.getPaddingLeft()) - recyclerView.getPaddingRight() : (recyclerView.getHeight() - recyclerView.getPaddingTop()) - recyclerView.getPaddingBottom();
    }

    public int getScrollState() {
        return this.mScrollEventAdapter.getScrollState();
    }

    public void invalidateItemDecorations() {
        this.mRecyclerView.invalidateItemDecorations();
    }

    public boolean isFakeDragging() {
        return this.mFakeDragger.isFakeDragging();
    }

    /* access modifiers changed from: package-private */
    public boolean isRtl() {
        return this.mLayoutManager.getLayoutDirection() == 1;
    }

    public boolean isUserInputEnabled() {
        return this.mUserInputEnabled;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        this.mAccessibilityProvider.onInitializeAccessibilityNodeInfo(info);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int measuredWidth = this.mRecyclerView.getMeasuredWidth();
        int measuredHeight = this.mRecyclerView.getMeasuredHeight();
        this.mTmpContainerRect.left = getPaddingLeft();
        this.mTmpContainerRect.right = (r - l) - getPaddingRight();
        this.mTmpContainerRect.top = getPaddingTop();
        this.mTmpContainerRect.bottom = (b - t) - getPaddingBottom();
        Gravity.apply(8388659, measuredWidth, measuredHeight, this.mTmpContainerRect, this.mTmpChildRect);
        this.mRecyclerView.layout(this.mTmpChildRect.left, this.mTmpChildRect.top, this.mTmpChildRect.right, this.mTmpChildRect.bottom);
        if (this.mCurrentItemDirty) {
            updateCurrentItem();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChild(this.mRecyclerView, widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = this.mRecyclerView.getMeasuredWidth();
        int measuredHeight = this.mRecyclerView.getMeasuredHeight();
        int measuredState = this.mRecyclerView.getMeasuredState();
        int paddingLeft = measuredWidth + getPaddingLeft() + getPaddingRight();
        int paddingTop = measuredHeight + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(resolveSizeAndState(Math.max(paddingLeft, getSuggestedMinimumWidth()), widthMeasureSpec, measuredState), resolveSizeAndState(Math.max(paddingTop, getSuggestedMinimumHeight()), heightMeasureSpec, measuredState << 16));
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mPendingCurrentItem = savedState.mCurrentItem;
        this.mPendingAdapterState = savedState.mAdapterState;
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mRecyclerViewId = this.mRecyclerView.getId();
        int i = this.mPendingCurrentItem;
        if (i == -1) {
            i = this.mCurrentItem;
        }
        savedState.mCurrentItem = i;
        Parcelable parcelable = this.mPendingAdapterState;
        if (parcelable != null) {
            savedState.mAdapterState = parcelable;
        } else {
            RecyclerView.Adapter adapter = this.mRecyclerView.getAdapter();
            if (adapter instanceof StatefulAdapter) {
                savedState.mAdapterState = ((StatefulAdapter) adapter).saveState();
            }
        }
        return savedState;
    }

    public void onViewAdded(View child) {
        throw new IllegalStateException(getClass().getSimpleName() + " does not support direct child views");
    }

    public boolean performAccessibilityAction(int action, Bundle arguments) {
        return this.mAccessibilityProvider.handlesPerformAccessibilityAction(action, arguments) ? this.mAccessibilityProvider.onPerformAccessibilityAction(action, arguments) : super.performAccessibilityAction(action, arguments);
    }

    public void registerOnPageChangeCallback(OnPageChangeCallback callback) {
        this.mExternalPageChangeCallbacks.addOnPageChangeCallback(callback);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration decor) {
        this.mRecyclerView.removeItemDecoration(decor);
    }

    public void removeItemDecorationAt(int index) {
        this.mRecyclerView.removeItemDecorationAt(index);
    }

    public void requestTransform() {
        if (this.mPageTransformerAdapter.getPageTransformer() != null) {
            double relativeScrollPosition = this.mScrollEventAdapter.getRelativeScrollPosition();
            int i = (int) relativeScrollPosition;
            float f = (float) (relativeScrollPosition - ((double) i));
            this.mPageTransformerAdapter.onPageScrolled(i, f, Math.round(((float) getPageSize()) * f));
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        RecyclerView.Adapter adapter2 = this.mRecyclerView.getAdapter();
        this.mAccessibilityProvider.onDetachAdapter(adapter2);
        unregisterCurrentItemDataSetTracker(adapter2);
        this.mRecyclerView.setAdapter(adapter);
        this.mCurrentItem = 0;
        restorePendingState();
        this.mAccessibilityProvider.onAttachAdapter(adapter);
        registerCurrentItemDataSetTracker(adapter);
    }

    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        if (!isFakeDragging()) {
            setCurrentItemInternal(item, smoothScroll);
            return;
        }
        throw new IllegalStateException("Cannot change current item when ViewPager2 is fake dragging");
    }

    /* access modifiers changed from: package-private */
    public void setCurrentItemInternal(int item, boolean smoothScroll) {
        RecyclerView.Adapter adapter = getAdapter();
        if (adapter == null) {
            if (this.mPendingCurrentItem != -1) {
                this.mPendingCurrentItem = Math.max(item, 0);
            }
        } else if (adapter.getItemCount() > 0) {
            int item2 = Math.min(Math.max(item, 0), adapter.getItemCount() - 1);
            if (item2 != this.mCurrentItem || !this.mScrollEventAdapter.isIdle()) {
                int i = this.mCurrentItem;
                if (item2 != i || !smoothScroll) {
                    double d = (double) i;
                    this.mCurrentItem = item2;
                    this.mAccessibilityProvider.onSetNewCurrentItem();
                    if (!this.mScrollEventAdapter.isIdle()) {
                        d = this.mScrollEventAdapter.getRelativeScrollPosition();
                    }
                    this.mScrollEventAdapter.notifyProgrammaticScroll(item2, smoothScroll);
                    if (!smoothScroll) {
                        this.mRecyclerView.scrollToPosition(item2);
                    } else if (Math.abs(((double) item2) - d) > 3.0d) {
                        this.mRecyclerView.scrollToPosition(((double) item2) > d ? item2 - 3 : item2 + 3);
                        this.mRecyclerView.post(new SmoothScrollToPosition(item2, this.mRecyclerView));
                    } else {
                        this.mRecyclerView.smoothScrollToPosition(item2);
                    }
                }
            }
        }
    }

    public void setLayoutDirection(int layoutDirection) {
        super.setLayoutDirection(layoutDirection);
        this.mAccessibilityProvider.onSetLayoutDirection();
    }

    public void setOffscreenPageLimit(int limit) {
        if (limit >= 1 || limit == -1) {
            this.mOffscreenPageLimit = limit;
            this.mRecyclerView.requestLayout();
            return;
        }
        throw new IllegalArgumentException("Offscreen page limit must be OFFSCREEN_PAGE_LIMIT_DEFAULT or a number > 0");
    }

    public void setOrientation(int orientation) {
        this.mLayoutManager.setOrientation(orientation);
        this.mAccessibilityProvider.onSetOrientation();
    }

    public void setPageTransformer(PageTransformer transformer) {
        if (transformer != null) {
            if (!this.mSavedItemAnimatorPresent) {
                this.mSavedItemAnimator = this.mRecyclerView.getItemAnimator();
                this.mSavedItemAnimatorPresent = true;
            }
            this.mRecyclerView.setItemAnimator((RecyclerView.ItemAnimator) null);
        } else if (this.mSavedItemAnimatorPresent) {
            this.mRecyclerView.setItemAnimator(this.mSavedItemAnimator);
            this.mSavedItemAnimator = null;
            this.mSavedItemAnimatorPresent = false;
        }
        if (transformer != this.mPageTransformerAdapter.getPageTransformer()) {
            this.mPageTransformerAdapter.setPageTransformer(transformer);
            requestTransform();
        }
    }

    public void setUserInputEnabled(boolean enabled) {
        this.mUserInputEnabled = enabled;
        this.mAccessibilityProvider.onSetUserInputEnabled();
    }

    /* access modifiers changed from: package-private */
    public void snapToPage() {
        View findSnapView = this.mPagerSnapHelper.findSnapView(this.mLayoutManager);
        if (findSnapView != null) {
            int[] calculateDistanceToFinalSnap = this.mPagerSnapHelper.calculateDistanceToFinalSnap(this.mLayoutManager, findSnapView);
            if (calculateDistanceToFinalSnap[0] != 0 || calculateDistanceToFinalSnap[1] != 0) {
                this.mRecyclerView.smoothScrollBy(calculateDistanceToFinalSnap[0], calculateDistanceToFinalSnap[1]);
            }
        }
    }

    public void unregisterOnPageChangeCallback(OnPageChangeCallback callback) {
        this.mExternalPageChangeCallbacks.removeOnPageChangeCallback(callback);
    }

    /* access modifiers changed from: package-private */
    public void updateCurrentItem() {
        PagerSnapHelper pagerSnapHelper = this.mPagerSnapHelper;
        if (pagerSnapHelper != null) {
            View findSnapView = pagerSnapHelper.findSnapView(this.mLayoutManager);
            if (findSnapView != null) {
                int position = this.mLayoutManager.getPosition(findSnapView);
                if (position != this.mCurrentItem && getScrollState() == 0) {
                    this.mPageChangeEventDispatcher.onPageSelected(position);
                }
                this.mCurrentItemDirty = false;
                return;
            }
            return;
        }
        throw new IllegalStateException("Design assumption violated.");
    }
}

package androidx.viewpager.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.Scroller;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import mt.Log1F380D;

/* compiled from: 009E */
public class ViewPager extends ViewGroup {
    private static final int CLOSE_ENOUGH = 2;
    private static final Comparator<ItemInfo> COMPARATOR = new Comparator<ItemInfo>() {
        public int compare(ItemInfo lhs, ItemInfo rhs) {
            return lhs.position - rhs.position;
        }
    };
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private static final int INVALID_POINTER = -1;
    static final int[] LAYOUT_ATTRS = {16842931};
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int MIN_FLING_VELOCITY = 400;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "ViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            float t2 = t - 1.0f;
            return (t2 * t2 * t2 * t2 * t2) + 1.0f;
        }
    };
    private static final ViewPositionComparator sPositionComparator = new ViewPositionComparator();
    private int mActivePointerId = -1;
    PagerAdapter mAdapter;
    private List<OnAdapterChangeListener> mAdapterChangeListeners;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private final Runnable mEndScrollRunnable = new Runnable() {
        public void run() {
            ViewPager.this.setScrollState(0);
            ViewPager.this.populate();
        }
    };
    private int mExpectedAdapterCount;
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout = true;
    private float mFirstOffset = -3.4028235E38f;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsScrollStarted;
    private boolean mIsUnableToDrag;
    private final ArrayList<ItemInfo> mItems = new ArrayList<>();
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset = Float.MAX_VALUE;
    private EdgeEffect mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets = false;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit = 1;
    private OnPageChangeListener mOnPageChangeListener;
    private List<OnPageChangeListener> mOnPageChangeListeners;
    private int mPageMargin;
    private PageTransformer mPageTransformer;
    private int mPageTransformerLayerType;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState = null;
    private ClassLoader mRestoredClassLoader = null;
    private int mRestoredCurItem = -1;
    private EdgeEffect mRightEdge;
    private int mScrollState = 0;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private final ItemInfo mTempItem = new ItemInfo();
    private final Rect mTempRect = new Rect();
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    @Inherited
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DecorView {
    }

    static class ItemInfo {
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;

        ItemInfo() {
        }
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        int childIndex;
        public int gravity;
        public boolean isDecor;
        boolean needsMeasure;
        int position;
        float widthFactor = 0.0f;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, ViewPager.LAYOUT_ATTRS);
            this.gravity = obtainStyledAttributes.getInteger(0, 48);
            obtainStyledAttributes.recycle();
        }
    }

    class MyAccessibilityDelegate extends AccessibilityDelegateCompat {
        MyAccessibilityDelegate() {
        }

        private boolean canScroll() {
            return ViewPager.this.mAdapter != null && ViewPager.this.mAdapter.getCount() > 1;
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(ViewPager.class.getName());
            event.setScrollable(canScroll());
            if (event.getEventType() == 4096 && ViewPager.this.mAdapter != null) {
                event.setItemCount(ViewPager.this.mAdapter.getCount());
                event.setFromIndex(ViewPager.this.mCurItem);
                event.setToIndex(ViewPager.this.mCurItem);
            }
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.setClassName(ViewPager.class.getName());
            info.setScrollable(canScroll());
            if (ViewPager.this.canScrollHorizontally(1)) {
                info.addAction(4096);
            }
            if (ViewPager.this.canScrollHorizontally(-1)) {
                info.addAction(8192);
            }
        }

        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            if (super.performAccessibilityAction(host, action, args)) {
                return true;
            }
            switch (action) {
                case 4096:
                    if (!ViewPager.this.canScrollHorizontally(1)) {
                        return false;
                    }
                    ViewPager viewPager = ViewPager.this;
                    viewPager.setCurrentItem(viewPager.mCurItem + 1);
                    return true;
                case 8192:
                    if (!ViewPager.this.canScrollHorizontally(-1)) {
                        return false;
                    }
                    ViewPager viewPager2 = ViewPager.this;
                    viewPager2.setCurrentItem(viewPager2.mCurItem - 1);
                    return true;
                default:
                    return false;
            }
        }
    }

    public interface OnAdapterChangeListener {
        void onAdapterChanged(ViewPager viewPager, PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2);
    }

    public interface OnPageChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, float f, int i2);

        void onPageSelected(int i);
    }

    public interface PageTransformer {
        void transformPage(View view, float f);
    }

    private class PagerObserver extends DataSetObserver {
        PagerObserver() {
        }

        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }

        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }

    /* compiled from: 009D */
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
        Parcelable adapterState;
        ClassLoader loader;
        int position;

        SavedState(Parcel in, ClassLoader loader2) {
            super(in, loader2);
            loader2 = loader2 == null ? getClass().getClassLoader() : loader2;
            this.position = in.readInt();
            this.adapterState = in.readParcelable(loader2);
            this.loader = loader2;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public String toString() {
            StringBuilder append = new StringBuilder().append("FragmentPager.SavedState{");
            String hexString = Integer.toHexString(System.identityHashCode(this));
            Log1F380D.a((Object) hexString);
            return append.append(hexString).append(" position=").append(this.position).append("}").toString();
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.position);
            out.writeParcelable(this.adapterState, flags);
        }
    }

    public static class SimpleOnPageChangeListener implements OnPageChangeListener {
        public void onPageScrollStateChanged(int state) {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
        }
    }

    static class ViewPositionComparator implements Comparator<View> {
        ViewPositionComparator() {
        }

        public int compare(View lhs, View rhs) {
            LayoutParams layoutParams = (LayoutParams) lhs.getLayoutParams();
            LayoutParams layoutParams2 = (LayoutParams) rhs.getLayoutParams();
            return layoutParams.isDecor != layoutParams2.isDecor ? layoutParams.isDecor ? 1 : -1 : layoutParams.position - layoutParams2.position;
        }
    }

    public ViewPager(Context context) {
        super(context);
        initViewPager();
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewPager();
    }

    private void calculatePageOffsets(ItemInfo curItem, int curIndex, ItemInfo oldCurInfo) {
        ItemInfo itemInfo;
        ItemInfo itemInfo2;
        int count = this.mAdapter.getCount();
        int clientWidth = getClientWidth();
        float f = clientWidth > 0 ? ((float) this.mPageMargin) / ((float) clientWidth) : 0.0f;
        if (oldCurInfo != null) {
            int i = oldCurInfo.position;
            if (i < curItem.position) {
                int i2 = 0;
                float f2 = oldCurInfo.offset + oldCurInfo.widthFactor + f;
                int i3 = i + 1;
                while (i3 <= curItem.position && i2 < this.mItems.size()) {
                    Object obj = this.mItems.get(i2);
                    while (true) {
                        itemInfo2 = (ItemInfo) obj;
                        if (i3 > itemInfo2.position && i2 < this.mItems.size() - 1) {
                            i2++;
                            obj = this.mItems.get(i2);
                        }
                    }
                    while (i3 < itemInfo2.position) {
                        f2 += this.mAdapter.getPageWidth(i3) + f;
                        i3++;
                    }
                    itemInfo2.offset = f2;
                    f2 += itemInfo2.widthFactor + f;
                    i3++;
                }
            } else if (i > curItem.position) {
                int size = this.mItems.size() - 1;
                float f3 = oldCurInfo.offset;
                int i4 = i - 1;
                while (i4 >= curItem.position && size >= 0) {
                    Object obj2 = this.mItems.get(size);
                    while (true) {
                        itemInfo = (ItemInfo) obj2;
                        if (i4 < itemInfo.position && size > 0) {
                            size--;
                            obj2 = this.mItems.get(size);
                        }
                    }
                    while (i4 > itemInfo.position) {
                        f3 -= this.mAdapter.getPageWidth(i4) + f;
                        i4--;
                    }
                    f3 -= itemInfo.widthFactor + f;
                    itemInfo.offset = f3;
                    i4--;
                }
            }
        }
        int size2 = this.mItems.size();
        float f4 = curItem.offset;
        int i5 = curItem.position - 1;
        this.mFirstOffset = curItem.position == 0 ? curItem.offset : -3.4028235E38f;
        this.mLastOffset = curItem.position == count + -1 ? (curItem.offset + curItem.widthFactor) - 1.0f : Float.MAX_VALUE;
        int i6 = curIndex - 1;
        while (i6 >= 0) {
            ItemInfo itemInfo3 = this.mItems.get(i6);
            while (i5 > itemInfo3.position) {
                f4 -= this.mAdapter.getPageWidth(i5) + f;
                i5--;
            }
            f4 -= itemInfo3.widthFactor + f;
            itemInfo3.offset = f4;
            if (itemInfo3.position == 0) {
                this.mFirstOffset = f4;
            }
            i6--;
            i5--;
        }
        float f5 = curItem.offset + curItem.widthFactor + f;
        int i7 = curItem.position + 1;
        int i8 = curIndex + 1;
        while (i8 < size2) {
            ItemInfo itemInfo4 = this.mItems.get(i8);
            while (i7 < itemInfo4.position) {
                f5 += this.mAdapter.getPageWidth(i7) + f;
                i7++;
            }
            if (itemInfo4.position == count - 1) {
                this.mLastOffset = (itemInfo4.widthFactor + f5) - 1.0f;
            }
            itemInfo4.offset = f5;
            f5 += itemInfo4.widthFactor + f;
            i8++;
            i7++;
        }
        this.mNeedCalculatePageOffsets = false;
    }

    private void completeScroll(boolean postEvents) {
        boolean z = this.mScrollState == 2;
        if (z) {
            setScrollingCacheEnabled(false);
            if (true ^ this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
                int scrollX = getScrollX();
                int scrollY = getScrollY();
                int currX = this.mScroller.getCurrX();
                int currY = this.mScroller.getCurrY();
                if (!(scrollX == currX && scrollY == currY)) {
                    scrollTo(currX, currY);
                    if (currX != scrollX) {
                        pageScrolled(currX);
                    }
                }
            }
        }
        this.mPopulatePending = false;
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo itemInfo = this.mItems.get(i);
            if (itemInfo.scrolling) {
                z = true;
                itemInfo.scrolling = false;
            }
        }
        if (!z) {
            return;
        }
        if (postEvents) {
            ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
        } else {
            this.mEndScrollRunnable.run();
        }
    }

    private int determineTargetPage(int currentPage, float pageOffset, int velocity, int deltaX) {
        int i;
        if (Math.abs(deltaX) <= this.mFlingDistance || Math.abs(velocity) <= this.mMinimumVelocity) {
            i = ((int) (pageOffset + (currentPage >= this.mCurItem ? 0.4f : 0.6f))) + currentPage;
        } else {
            i = velocity > 0 ? currentPage : currentPage + 1;
        }
        if (this.mItems.size() <= 0) {
            return i;
        }
        ArrayList<ItemInfo> arrayList = this.mItems;
        return Math.max(this.mItems.get(0).position, Math.min(i, arrayList.get(arrayList.size() - 1).position));
    }

    private void dispatchOnPageScrolled(int position, float offset, int offsetPixels) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(position, offset, offsetPixels);
        }
        List<OnPageChangeListener> list = this.mOnPageChangeListeners;
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                OnPageChangeListener onPageChangeListener2 = this.mOnPageChangeListeners.get(i);
                if (onPageChangeListener2 != null) {
                    onPageChangeListener2.onPageScrolled(position, offset, offsetPixels);
                }
            }
        }
        OnPageChangeListener onPageChangeListener3 = this.mInternalPageChangeListener;
        if (onPageChangeListener3 != null) {
            onPageChangeListener3.onPageScrolled(position, offset, offsetPixels);
        }
    }

    private void dispatchOnPageSelected(int position) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(position);
        }
        List<OnPageChangeListener> list = this.mOnPageChangeListeners;
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                OnPageChangeListener onPageChangeListener2 = this.mOnPageChangeListeners.get(i);
                if (onPageChangeListener2 != null) {
                    onPageChangeListener2.onPageSelected(position);
                }
            }
        }
        OnPageChangeListener onPageChangeListener3 = this.mInternalPageChangeListener;
        if (onPageChangeListener3 != null) {
            onPageChangeListener3.onPageSelected(position);
        }
    }

    private void dispatchOnScrollStateChanged(int state) {
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }
        List<OnPageChangeListener> list = this.mOnPageChangeListeners;
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                OnPageChangeListener onPageChangeListener2 = this.mOnPageChangeListeners.get(i);
                if (onPageChangeListener2 != null) {
                    onPageChangeListener2.onPageScrollStateChanged(state);
                }
            }
        }
        OnPageChangeListener onPageChangeListener3 = this.mInternalPageChangeListener;
        if (onPageChangeListener3 != null) {
            onPageChangeListener3.onPageScrollStateChanged(state);
        }
    }

    private void enableLayers(boolean enable) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setLayerType(enable ? this.mPageTransformerLayerType : 0, (Paint) null);
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private Rect getChildRectInPagerCoordinates(Rect outRect, View child) {
        if (outRect == null) {
            outRect = new Rect();
        }
        if (child == null) {
            outRect.set(0, 0, 0, 0);
            return outRect;
        }
        outRect.left = child.getLeft();
        outRect.right = child.getRight();
        outRect.top = child.getTop();
        outRect.bottom = child.getBottom();
        ViewParent parent = child.getParent();
        while ((parent instanceof ViewGroup) && parent != this) {
            ViewGroup viewGroup = (ViewGroup) parent;
            outRect.left += viewGroup.getLeft();
            outRect.right += viewGroup.getRight();
            outRect.top += viewGroup.getTop();
            outRect.bottom += viewGroup.getBottom();
            parent = viewGroup.getParent();
        }
        return outRect;
    }

    private int getClientWidth() {
        return (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
    }

    private ItemInfo infoForCurrentScrollPosition() {
        int clientWidth = getClientWidth();
        float f = 0.0f;
        float scrollX = clientWidth > 0 ? ((float) getScrollX()) / ((float) clientWidth) : 0.0f;
        if (clientWidth > 0) {
            f = ((float) this.mPageMargin) / ((float) clientWidth);
        }
        int i = -1;
        float f2 = 0.0f;
        float f3 = 0.0f;
        boolean z = true;
        ItemInfo itemInfo = null;
        int i2 = 0;
        while (i2 < this.mItems.size()) {
            ItemInfo itemInfo2 = this.mItems.get(i2);
            if (!z && itemInfo2.position != i + 1) {
                itemInfo2 = this.mTempItem;
                itemInfo2.offset = f2 + f3 + f;
                itemInfo2.position = i + 1;
                itemInfo2.widthFactor = this.mAdapter.getPageWidth(itemInfo2.position);
                i2--;
            }
            float f4 = itemInfo2.offset;
            float f5 = f4;
            float f6 = itemInfo2.widthFactor + f4 + f;
            if (!z && scrollX < f5) {
                return itemInfo;
            }
            if (scrollX < f6 || i2 == this.mItems.size() - 1) {
                return itemInfo2;
            }
            z = false;
            i = itemInfo2.position;
            f2 = f4;
            f3 = itemInfo2.widthFactor;
            itemInfo = itemInfo2;
            i2++;
        }
        return itemInfo;
    }

    private static boolean isDecorView(View view) {
        return view.getClass().getAnnotation(DecorView.class) != null;
    }

    private boolean isGutterDrag(float x, float dx) {
        return (x < ((float) this.mGutterSize) && dx > 0.0f) || (x > ((float) (getWidth() - this.mGutterSize)) && dx < 0.0f);
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int actionIndex = ev.getActionIndex();
        if (ev.getPointerId(actionIndex) == this.mActivePointerId) {
            int i = actionIndex == 0 ? 1 : 0;
            this.mLastMotionX = ev.getX(i);
            this.mActivePointerId = ev.getPointerId(i);
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    private boolean pageScrolled(int xpos) {
        if (this.mItems.size() != 0) {
            ItemInfo infoForCurrentScrollPosition = infoForCurrentScrollPosition();
            int clientWidth = getClientWidth();
            int i = this.mPageMargin;
            int i2 = clientWidth + i;
            int i3 = infoForCurrentScrollPosition.position;
            float f = ((((float) xpos) / ((float) clientWidth)) - infoForCurrentScrollPosition.offset) / (infoForCurrentScrollPosition.widthFactor + (((float) i) / ((float) clientWidth)));
            this.mCalledSuper = false;
            onPageScrolled(i3, f, (int) (((float) i2) * f));
            if (this.mCalledSuper) {
                return true;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        } else if (this.mFirstLayout) {
            return false;
        } else {
            this.mCalledSuper = false;
            onPageScrolled(0, 0.0f, 0);
            if (this.mCalledSuper) {
                return false;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        }
    }

    private boolean performDrag(float x) {
        float f = x;
        boolean z = false;
        this.mLastMotionX = f;
        float scrollX = ((float) getScrollX()) + (this.mLastMotionX - f);
        int clientWidth = getClientWidth();
        float f2 = ((float) clientWidth) * this.mFirstOffset;
        float f3 = ((float) clientWidth) * this.mLastOffset;
        boolean z2 = true;
        boolean z3 = true;
        ItemInfo itemInfo = this.mItems.get(0);
        ArrayList<ItemInfo> arrayList = this.mItems;
        ItemInfo itemInfo2 = arrayList.get(arrayList.size() - 1);
        if (itemInfo.position != 0) {
            z2 = false;
            f2 = itemInfo.offset * ((float) clientWidth);
        }
        if (itemInfo2.position != this.mAdapter.getCount() - 1) {
            z3 = false;
            f3 = itemInfo2.offset * ((float) clientWidth);
        }
        if (scrollX < f2) {
            if (z2) {
                this.mLeftEdge.onPull(Math.abs(f2 - scrollX) / ((float) clientWidth));
                z = true;
            }
            scrollX = f2;
        } else if (scrollX > f3) {
            if (z3) {
                this.mRightEdge.onPull(Math.abs(scrollX - f3) / ((float) clientWidth));
                z = true;
            }
            scrollX = f3;
        }
        this.mLastMotionX += scrollX - ((float) ((int) scrollX));
        scrollTo((int) scrollX, getScrollY());
        pageScrolled((int) scrollX);
        return z;
    }

    private void recomputeScrollPosition(int width, int oldWidth, int margin, int oldMargin) {
        if (oldWidth <= 0 || this.mItems.isEmpty()) {
            ItemInfo infoForPosition = infoForPosition(this.mCurItem);
            int paddingLeft = (int) (((float) ((width - getPaddingLeft()) - getPaddingRight())) * (infoForPosition != null ? Math.min(infoForPosition.offset, this.mLastOffset) : 0.0f));
            if (paddingLeft != getScrollX()) {
                completeScroll(false);
                scrollTo(paddingLeft, getScrollY());
            }
        } else if (!this.mScroller.isFinished()) {
            this.mScroller.setFinalX(getCurrentItem() * getClientWidth());
        } else {
            scrollTo((int) (((float) (((width - getPaddingLeft()) - getPaddingRight()) + margin)) * (((float) getScrollX()) / ((float) (((oldWidth - getPaddingLeft()) - getPaddingRight()) + oldMargin)))), getScrollY());
        }
    }

    private void removeNonDecorViews() {
        int i = 0;
        while (i < getChildCount()) {
            if (!((LayoutParams) getChildAt(i).getLayoutParams()).isDecor) {
                removeViewAt(i);
                i--;
            }
            i++;
        }
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    private boolean resetTouch() {
        this.mActivePointerId = -1;
        endDrag();
        this.mLeftEdge.onRelease();
        this.mRightEdge.onRelease();
        return this.mLeftEdge.isFinished() || this.mRightEdge.isFinished();
    }

    private void scrollToItem(int item, boolean smoothScroll, int velocity, boolean dispatchSelected) {
        ItemInfo infoForPosition = infoForPosition(item);
        int i = 0;
        if (infoForPosition != null) {
            i = (int) (((float) getClientWidth()) * Math.max(this.mFirstOffset, Math.min(infoForPosition.offset, this.mLastOffset)));
        }
        if (smoothScroll) {
            smoothScrollTo(i, 0, velocity);
            if (dispatchSelected) {
                dispatchOnPageSelected(item);
                return;
            }
            return;
        }
        if (dispatchSelected) {
            dispatchOnPageSelected(item);
        }
        completeScroll(false);
        scrollTo(i, 0);
        pageScrolled(i);
    }

    private void setScrollingCacheEnabled(boolean enabled) {
        if (this.mScrollingCacheEnabled != enabled) {
            this.mScrollingCacheEnabled = enabled;
        }
    }

    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            ArrayList<View> arrayList = this.mDrawingOrderedChildren;
            if (arrayList == null) {
                this.mDrawingOrderedChildren = new ArrayList<>();
            } else {
                arrayList.clear();
            }
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                this.mDrawingOrderedChildren.add(getChildAt(i));
            }
            Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
        }
    }

    public void addFocusables(ArrayList<View> arrayList, int direction, int focusableMode) {
        ItemInfo infoForChild;
        int size = arrayList.size();
        int descendantFocusability = getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i = 0; i < getChildCount(); i++) {
                View childAt = getChildAt(i);
                if (childAt.getVisibility() == 0 && (infoForChild = infoForChild(childAt)) != null && infoForChild.position == this.mCurItem) {
                    childAt.addFocusables(arrayList, direction, focusableMode);
                }
            }
        }
        if ((descendantFocusability == 262144 && size != arrayList.size()) || !isFocusable()) {
            return;
        }
        if (((focusableMode & 1) != 1 || !isInTouchMode() || isFocusableInTouchMode()) && arrayList != null) {
            arrayList.add(this);
        }
    }

    /* access modifiers changed from: package-private */
    public ItemInfo addNewItem(int position, int index) {
        ItemInfo itemInfo = new ItemInfo();
        itemInfo.position = position;
        itemInfo.object = this.mAdapter.instantiateItem((ViewGroup) this, position);
        itemInfo.widthFactor = this.mAdapter.getPageWidth(position);
        if (index < 0 || index >= this.mItems.size()) {
            this.mItems.add(itemInfo);
        } else {
            this.mItems.add(index, itemInfo);
        }
        return itemInfo;
    }

    public void addOnAdapterChangeListener(OnAdapterChangeListener listener) {
        if (this.mAdapterChangeListeners == null) {
            this.mAdapterChangeListeners = new ArrayList();
        }
        this.mAdapterChangeListeners.add(listener);
    }

    public void addOnPageChangeListener(OnPageChangeListener listener) {
        if (this.mOnPageChangeListeners == null) {
            this.mOnPageChangeListeners = new ArrayList();
        }
        this.mOnPageChangeListeners.add(listener);
    }

    public void addTouchables(ArrayList<View> arrayList) {
        ItemInfo infoForChild;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 0 && (infoForChild = infoForChild(childAt)) != null && infoForChild.position == this.mCurItem) {
                childAt.addTouchables(arrayList);
            }
        }
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (!checkLayoutParams(params)) {
            params = generateLayoutParams(params);
        }
        LayoutParams layoutParams = (LayoutParams) params;
        layoutParams.isDecor |= isDecorView(child);
        if (!this.mInLayout) {
            super.addView(child, index, params);
        } else if (layoutParams == null || !layoutParams.isDecor) {
            layoutParams.needsMeasure = true;
            addViewInLayout(child, index, params);
        } else {
            throw new IllegalStateException("Cannot add pager decor view during layout");
        }
    }

    public boolean arrowScroll(int direction) {
        View findFocus = findFocus();
        if (findFocus == this) {
            findFocus = null;
        } else if (findFocus != null) {
            boolean z = false;
            ViewParent parent = findFocus.getParent();
            while (true) {
                if (!(parent instanceof ViewGroup)) {
                    break;
                } else if (parent == this) {
                    z = true;
                    break;
                } else {
                    parent = parent.getParent();
                }
            }
            if (!z) {
                StringBuilder sb = new StringBuilder();
                sb.append(findFocus.getClass().getSimpleName());
                for (ViewParent parent2 = findFocus.getParent(); parent2 instanceof ViewGroup; parent2 = parent2.getParent()) {
                    sb.append(" => ").append(parent2.getClass().getSimpleName());
                }
                Log.e(TAG, "arrowScroll tried to find focus based on non-child current focused view " + sb.toString());
                findFocus = null;
            }
        }
        boolean z2 = false;
        View findNextFocus = FocusFinder.getInstance().findNextFocus(this, findFocus, direction);
        if (findNextFocus == null || findNextFocus == findFocus) {
            if (direction == 17 || direction == 1) {
                z2 = pageLeft();
            } else if (direction == 66 || direction == 2) {
                z2 = pageRight();
            }
        } else if (direction == 17) {
            z2 = (findFocus == null || getChildRectInPagerCoordinates(this.mTempRect, findNextFocus).left < getChildRectInPagerCoordinates(this.mTempRect, findFocus).left) ? findNextFocus.requestFocus() : pageLeft();
        } else if (direction == 66) {
            z2 = (findFocus == null || getChildRectInPagerCoordinates(this.mTempRect, findNextFocus).left > getChildRectInPagerCoordinates(this.mTempRect, findFocus).left) ? findNextFocus.requestFocus() : pageRight();
        }
        if (z2) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
        }
        return z2;
    }

    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return false;
        }
        this.mFakeDragging = true;
        setScrollState(1);
        this.mLastMotionX = 0.0f;
        this.mInitialMotionX = 0.0f;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 0, 0.0f, 0.0f, 0);
        this.mVelocityTracker.addMovement(obtain);
        obtain.recycle();
        this.mFakeDragBeginTime = uptimeMillis;
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        View view = v;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = viewGroup.getChildAt(childCount);
                if (x + scrollX >= childAt.getLeft() && x + scrollX < childAt.getRight() && y + scrollY >= childAt.getTop() && y + scrollY < childAt.getBottom()) {
                    if (canScroll(childAt, true, dx, (x + scrollX) - childAt.getLeft(), (y + scrollY) - childAt.getTop())) {
                        return true;
                    }
                }
            }
        }
        if (!checkV) {
            int i = dx;
        } else if (v.canScrollHorizontally(-dx)) {
            return true;
        }
        return false;
    }

    public boolean canScrollHorizontally(int direction) {
        if (this.mAdapter == null) {
            return false;
        }
        int clientWidth = getClientWidth();
        int scrollX = getScrollX();
        return direction < 0 ? scrollX > ((int) (((float) clientWidth) * this.mFirstOffset)) : direction > 0 && scrollX < ((int) (((float) clientWidth) * this.mLastOffset));
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return (p instanceof LayoutParams) && super.checkLayoutParams(p);
    }

    public void clearOnPageChangeListeners() {
        List<OnPageChangeListener> list = this.mOnPageChangeListeners;
        if (list != null) {
            list.clear();
        }
    }

    public void computeScroll() {
        this.mIsScrollStarted = true;
        if (this.mScroller.isFinished() || !this.mScroller.computeScrollOffset()) {
            completeScroll(true);
            return;
        }
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int currX = this.mScroller.getCurrX();
        int currY = this.mScroller.getCurrY();
        if (!(scrollX == currX && scrollY == currY)) {
            scrollTo(currX, currY);
            if (!pageScrolled(currX)) {
                this.mScroller.abortAnimation();
                scrollTo(0, currY);
            }
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /* access modifiers changed from: package-private */
    public void dataSetChanged() {
        int count = this.mAdapter.getCount();
        this.mExpectedAdapterCount = count;
        boolean z = this.mItems.size() < (this.mOffscreenPageLimit * 2) + 1 && this.mItems.size() < count;
        int i = this.mCurItem;
        boolean z2 = false;
        int i2 = 0;
        while (i2 < this.mItems.size()) {
            ItemInfo itemInfo = this.mItems.get(i2);
            int itemPosition = this.mAdapter.getItemPosition(itemInfo.object);
            if (itemPosition != -1) {
                if (itemPosition == -2) {
                    this.mItems.remove(i2);
                    i2--;
                    if (!z2) {
                        this.mAdapter.startUpdate((ViewGroup) this);
                        z2 = true;
                    }
                    this.mAdapter.destroyItem((ViewGroup) this, itemInfo.position, itemInfo.object);
                    z = true;
                    if (this.mCurItem == itemInfo.position) {
                        i = Math.max(0, Math.min(this.mCurItem, count - 1));
                        z = true;
                    }
                } else if (itemInfo.position != itemPosition) {
                    if (itemInfo.position == this.mCurItem) {
                        i = itemPosition;
                    }
                    itemInfo.position = itemPosition;
                    z = true;
                }
            }
            i2++;
        }
        if (z2) {
            this.mAdapter.finishUpdate((ViewGroup) this);
        }
        Collections.sort(this.mItems, COMPARATOR);
        if (z) {
            int childCount = getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                LayoutParams layoutParams = (LayoutParams) getChildAt(i3).getLayoutParams();
                if (!layoutParams.isDecor) {
                    layoutParams.widthFactor = 0.0f;
                }
            }
            setCurrentItemInternal(i, false, true);
            requestLayout();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        ItemInfo infoForChild;
        if (event.getEventType() == 4096) {
            return super.dispatchPopulateAccessibilityEvent(event);
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 0 && (infoForChild = infoForChild(childAt)) != null && infoForChild.position == this.mCurItem && childAt.dispatchPopulateAccessibilityEvent(event)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((double) ((f - 0.5f) * 0.47123894f));
    }

    public void draw(Canvas canvas) {
        PagerAdapter pagerAdapter;
        super.draw(canvas);
        boolean z = false;
        int overScrollMode = getOverScrollMode();
        if (overScrollMode == 0 || (overScrollMode == 1 && (pagerAdapter = this.mAdapter) != null && pagerAdapter.getCount() > 1)) {
            if (!this.mLeftEdge.isFinished()) {
                int save = canvas.save();
                int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
                int width = getWidth();
                canvas.rotate(270.0f);
                canvas.translate((float) ((-height) + getPaddingTop()), this.mFirstOffset * ((float) width));
                this.mLeftEdge.setSize(height, width);
                z = false | this.mLeftEdge.draw(canvas);
                canvas.restoreToCount(save);
            }
            if (!this.mRightEdge.isFinished()) {
                int save2 = canvas.save();
                int width2 = getWidth();
                int height2 = (getHeight() - getPaddingTop()) - getPaddingBottom();
                canvas.rotate(90.0f);
                canvas.translate((float) (-getPaddingTop()), (-(this.mLastOffset + 1.0f)) * ((float) width2));
                this.mRightEdge.setSize(height2, width2);
                z |= this.mRightEdge.draw(canvas);
                canvas.restoreToCount(save2);
            }
        } else {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        }
        if (z) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.mMarginDrawable;
        if (drawable != null && drawable.isStateful()) {
            drawable.setState(getDrawableState());
        }
    }

    public void endFakeDrag() {
        if (this.mFakeDragging) {
            if (this.mAdapter != null) {
                VelocityTracker velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                int xVelocity = (int) velocityTracker.getXVelocity(this.mActivePointerId);
                this.mPopulatePending = true;
                int clientWidth = getClientWidth();
                int scrollX = getScrollX();
                ItemInfo infoForCurrentScrollPosition = infoForCurrentScrollPosition();
                setCurrentItemInternal(determineTargetPage(infoForCurrentScrollPosition.position, ((((float) scrollX) / ((float) clientWidth)) - infoForCurrentScrollPosition.offset) / infoForCurrentScrollPosition.widthFactor, xVelocity, (int) (this.mLastMotionX - this.mInitialMotionX)), true, true, xVelocity);
            }
            endDrag();
            this.mFakeDragging = false;
            return;
        }
        throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }

    public boolean executeKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return false;
        }
        switch (event.getKeyCode()) {
            case 21:
                return event.hasModifiers(2) ? pageLeft() : arrowScroll(17);
            case 22:
                return event.hasModifiers(2) ? pageRight() : arrowScroll(66);
            case LockFreeTaskQueueCore.CLOSED_SHIFT /*61*/:
                if (event.hasNoModifiers()) {
                    return arrowScroll(2);
                }
                if (event.hasModifiers(1)) {
                    return arrowScroll(1);
                }
                return false;
            default:
                return false;
        }
    }

    public void fakeDragBy(float xOffset) {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        } else if (this.mAdapter != null) {
            this.mLastMotionX += xOffset;
            float scrollX = ((float) getScrollX()) - xOffset;
            int clientWidth = getClientWidth();
            float f = ((float) clientWidth) * this.mFirstOffset;
            float f2 = ((float) clientWidth) * this.mLastOffset;
            ItemInfo itemInfo = this.mItems.get(0);
            ArrayList<ItemInfo> arrayList = this.mItems;
            ItemInfo itemInfo2 = arrayList.get(arrayList.size() - 1);
            if (itemInfo.position != 0) {
                f = itemInfo.offset * ((float) clientWidth);
            }
            if (itemInfo2.position != this.mAdapter.getCount() - 1) {
                f2 = itemInfo2.offset * ((float) clientWidth);
            }
            if (scrollX < f) {
                scrollX = f;
            } else if (scrollX > f2) {
                scrollX = f2;
            }
            this.mLastMotionX += scrollX - ((float) ((int) scrollX));
            scrollTo((int) scrollX, getScrollY());
            pageScrolled((int) scrollX);
            MotionEvent obtain = MotionEvent.obtain(this.mFakeDragBeginTime, SystemClock.uptimeMillis(), 2, this.mLastMotionX, 0.0f, 0);
            this.mVelocityTracker.addMovement(obtain);
            obtain.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return generateDefaultLayoutParams();
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    /* access modifiers changed from: protected */
    public int getChildDrawingOrder(int childCount, int i) {
        return ((LayoutParams) this.mDrawingOrderedChildren.get(this.mDrawingOrder == 2 ? (childCount - 1) - i : i).getLayoutParams()).childIndex;
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    /* access modifiers changed from: package-private */
    public ItemInfo infoForAnyChild(View child) {
        while (true) {
            ViewParent parent = child.getParent();
            ViewParent viewParent = parent;
            if (parent == this) {
                return infoForChild(child);
            }
            if (viewParent == null || !(viewParent instanceof View)) {
                return null;
            }
            child = (View) viewParent;
        }
    }

    /* access modifiers changed from: package-private */
    public ItemInfo infoForChild(View child) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo itemInfo = this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(child, itemInfo.object)) {
                return itemInfo;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public ItemInfo infoForPosition(int position) {
        for (int i = 0; i < this.mItems.size(); i++) {
            ItemInfo itemInfo = this.mItems.get(i);
            if (itemInfo.position == position) {
                return itemInfo;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void initViewPager() {
        setWillNotDraw(false);
        setDescendantFocusability(262144);
        setFocusable(true);
        Context context = getContext();
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        float f = context.getResources().getDisplayMetrics().density;
        this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
        this.mMinimumVelocity = (int) (400.0f * f);
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffect(context);
        this.mRightEdge = new EdgeEffect(context);
        this.mFlingDistance = (int) (25.0f * f);
        this.mCloseEnough = (int) (2.0f * f);
        this.mDefaultGutterSize = (int) (16.0f * f);
        ViewCompat.setAccessibilityDelegate(this, new MyAccessibilityDelegate());
        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, 1);
        }
        ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener() {
            private final Rect mTempRect = new Rect();

            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat originalInsets) {
                WindowInsetsCompat onApplyWindowInsets = ViewCompat.onApplyWindowInsets(v, originalInsets);
                if (onApplyWindowInsets.isConsumed()) {
                    return onApplyWindowInsets;
                }
                Rect rect = this.mTempRect;
                rect.left = onApplyWindowInsets.getSystemWindowInsetLeft();
                rect.top = onApplyWindowInsets.getSystemWindowInsetTop();
                rect.right = onApplyWindowInsets.getSystemWindowInsetRight();
                rect.bottom = onApplyWindowInsets.getSystemWindowInsetBottom();
                int childCount = ViewPager.this.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    WindowInsetsCompat dispatchApplyWindowInsets = ViewCompat.dispatchApplyWindowInsets(ViewPager.this.getChildAt(i), onApplyWindowInsets);
                    rect.left = Math.min(dispatchApplyWindowInsets.getSystemWindowInsetLeft(), rect.left);
                    rect.top = Math.min(dispatchApplyWindowInsets.getSystemWindowInsetTop(), rect.top);
                    rect.right = Math.min(dispatchApplyWindowInsets.getSystemWindowInsetRight(), rect.right);
                    rect.bottom = Math.min(dispatchApplyWindowInsets.getSystemWindowInsetBottom(), rect.bottom);
                }
                return onApplyWindowInsets.replaceSystemWindowInsets(rect.left, rect.top, rect.right, rect.bottom);
            }
        });
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        removeCallbacks(this.mEndScrollRunnable);
        Scroller scroller = this.mScroller;
        if (scroller != null && !scroller.isFinished()) {
            this.mScroller.abortAnimation();
        }
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        float f;
        float f2;
        super.onDraw(canvas);
        if (this.mPageMargin <= 0 || this.mMarginDrawable == null || this.mItems.size() <= 0 || this.mAdapter == null) {
            Canvas canvas2 = canvas;
            return;
        }
        int scrollX = getScrollX();
        int width = getWidth();
        float f3 = ((float) this.mPageMargin) / ((float) width);
        int i = 0;
        ItemInfo itemInfo = this.mItems.get(0);
        float f4 = itemInfo.offset;
        int size = this.mItems.size();
        int i2 = itemInfo.position;
        int i3 = this.mItems.get(size - 1).position;
        int i4 = i2;
        while (i4 < i3) {
            while (i4 > itemInfo.position && i < size) {
                i++;
                itemInfo = this.mItems.get(i);
            }
            if (i4 == itemInfo.position) {
                f = (itemInfo.offset + itemInfo.widthFactor) * ((float) width);
                f4 = itemInfo.offset + itemInfo.widthFactor + f3;
            } else {
                float pageWidth = this.mAdapter.getPageWidth(i4);
                float f5 = (f4 + pageWidth) * ((float) width);
                f4 += pageWidth + f3;
                f = f5;
            }
            if (((float) this.mPageMargin) + f > ((float) scrollX)) {
                f2 = f3;
                this.mMarginDrawable.setBounds(Math.round(f), this.mTopPageBounds, Math.round(((float) this.mPageMargin) + f), this.mBottomPageBounds);
                this.mMarginDrawable.draw(canvas);
            } else {
                Canvas canvas3 = canvas;
                f2 = f3;
            }
            if (f <= ((float) (scrollX + width))) {
                i4++;
                f3 = f2;
            } else {
                return;
            }
        }
        Canvas canvas4 = canvas;
        float f6 = f3;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float f;
        MotionEvent motionEvent = ev;
        int action = ev.getAction() & 255;
        if (action == 3 || action == 1) {
            resetTouch();
            return false;
        }
        if (action != 0) {
            if (this.mIsBeingDragged) {
                return true;
            }
            if (this.mIsUnableToDrag) {
                return false;
            }
        }
        switch (action) {
            case 0:
                float x = ev.getX();
                this.mInitialMotionX = x;
                this.mLastMotionX = x;
                float y = ev.getY();
                this.mInitialMotionY = y;
                this.mLastMotionY = y;
                this.mActivePointerId = motionEvent.getPointerId(0);
                this.mIsUnableToDrag = false;
                this.mIsScrollStarted = true;
                this.mScroller.computeScrollOffset();
                if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = false;
                    populate();
                    this.mIsBeingDragged = true;
                    requestParentDisallowInterceptTouchEvent(true);
                    setScrollState(1);
                    break;
                } else {
                    completeScroll(false);
                    this.mIsBeingDragged = false;
                    break;
                }
            case 2:
                int i = this.mActivePointerId;
                if (i != -1) {
                    int findPointerIndex = motionEvent.findPointerIndex(i);
                    float x2 = motionEvent.getX(findPointerIndex);
                    float f2 = x2 - this.mLastMotionX;
                    float abs = Math.abs(f2);
                    float y2 = motionEvent.getY(findPointerIndex);
                    float abs2 = Math.abs(y2 - this.mInitialMotionY);
                    if (f2 == 0.0f || isGutterDrag(this.mLastMotionX, f2)) {
                        f = y2;
                    } else {
                        f = y2;
                        if (canScroll(this, false, (int) f2, (int) x2, (int) y2)) {
                            this.mLastMotionX = x2;
                            this.mLastMotionY = f;
                            this.mIsUnableToDrag = true;
                            return false;
                        }
                    }
                    int i2 = this.mTouchSlop;
                    if (abs > ((float) i2) && 0.5f * abs > abs2) {
                        this.mIsBeingDragged = true;
                        requestParentDisallowInterceptTouchEvent(true);
                        setScrollState(1);
                        this.mLastMotionX = f2 > 0.0f ? this.mInitialMotionX + ((float) this.mTouchSlop) : this.mInitialMotionX - ((float) this.mTouchSlop);
                        this.mLastMotionY = f;
                        setScrollingCacheEnabled(true);
                    } else if (abs2 > ((float) i2)) {
                        this.mIsUnableToDrag = true;
                    }
                    if (this.mIsBeingDragged && performDrag(x2)) {
                        ViewCompat.postInvalidateOnAnimation(this);
                        break;
                    }
                }
                break;
            case 6:
                onSecondaryPointerUp(ev);
                break;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        return this.mIsBeingDragged;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        boolean z;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int childCount = getChildCount();
        int i6 = r - l;
        int i7 = b - t;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int scrollX = getScrollX();
        int i8 = 0;
        int i9 = 0;
        while (true) {
            int i10 = 8;
            if (i9 < childCount) {
                View childAt = getChildAt(i9);
                if (childAt.getVisibility() != 8) {
                    LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                    if (layoutParams.isDecor) {
                        int i11 = layoutParams.gravity & 7;
                        int i12 = layoutParams.gravity & 112;
                        switch (i11) {
                            case 1:
                                LayoutParams layoutParams2 = layoutParams;
                                i4 = Math.max((i6 - childAt.getMeasuredWidth()) / 2, paddingLeft);
                                break;
                            case 3:
                                i4 = paddingLeft;
                                paddingLeft += childAt.getMeasuredWidth();
                                LayoutParams layoutParams3 = layoutParams;
                                break;
                            case 5:
                                i4 = (i6 - paddingRight) - childAt.getMeasuredWidth();
                                paddingRight += childAt.getMeasuredWidth();
                                LayoutParams layoutParams4 = layoutParams;
                                break;
                            default:
                                LayoutParams layoutParams5 = layoutParams;
                                i4 = paddingLeft;
                                break;
                        }
                        switch (i12) {
                            case 16:
                                i5 = Math.max((i7 - childAt.getMeasuredHeight()) / 2, paddingTop);
                                break;
                            case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE:
                                i5 = paddingTop;
                                paddingTop += childAt.getMeasuredHeight();
                                break;
                            case 80:
                                i5 = (i7 - paddingBottom) - childAt.getMeasuredHeight();
                                paddingBottom += childAt.getMeasuredHeight();
                                break;
                            default:
                                i5 = paddingTop;
                                break;
                        }
                        int i13 = i4 + scrollX;
                        childAt.layout(i13, i5, i13 + childAt.getMeasuredWidth(), i5 + childAt.getMeasuredHeight());
                        i8++;
                        paddingLeft = paddingLeft;
                        paddingTop = paddingTop;
                    } else {
                        LayoutParams layoutParams6 = layoutParams;
                    }
                }
                i9++;
            } else {
                int i14 = (i6 - paddingLeft) - paddingRight;
                int i15 = 0;
                while (i15 < childCount) {
                    View childAt2 = getChildAt(i15);
                    if (childAt2.getVisibility() != i10) {
                        LayoutParams layoutParams7 = (LayoutParams) childAt2.getLayoutParams();
                        if (!layoutParams7.isDecor) {
                            ItemInfo infoForChild = infoForChild(childAt2);
                            ItemInfo itemInfo = infoForChild;
                            if (infoForChild != null) {
                                ItemInfo itemInfo2 = itemInfo;
                                i3 = childCount;
                                int i16 = (int) (((float) i14) * itemInfo2.offset);
                                int i17 = paddingLeft + i16;
                                int i18 = paddingTop;
                                int i19 = i16;
                                if (layoutParams7.needsMeasure) {
                                    layoutParams7.needsMeasure = false;
                                    i2 = i6;
                                    i = paddingLeft;
                                    childAt2.measure(View.MeasureSpec.makeMeasureSpec((int) (((float) i14) * layoutParams7.widthFactor), BasicMeasure.EXACTLY), View.MeasureSpec.makeMeasureSpec((i7 - paddingTop) - paddingBottom, BasicMeasure.EXACTLY));
                                } else {
                                    i2 = i6;
                                    i = paddingLeft;
                                }
                                int i20 = i18;
                                childAt2.layout(i17, i20, childAt2.getMeasuredWidth() + i17, childAt2.getMeasuredHeight() + i20);
                            } else {
                                i2 = i6;
                                i = paddingLeft;
                                ItemInfo itemInfo3 = itemInfo;
                                i3 = childCount;
                            }
                        } else {
                            i3 = childCount;
                            i2 = i6;
                            i = paddingLeft;
                        }
                    } else {
                        i3 = childCount;
                        i2 = i6;
                        i = paddingLeft;
                    }
                    i15++;
                    childCount = i3;
                    i6 = i2;
                    paddingLeft = i;
                    i10 = 8;
                }
                int i21 = childCount;
                int i22 = i6;
                int i23 = paddingLeft;
                this.mTopPageBounds = paddingTop;
                this.mBottomPageBounds = i7 - paddingBottom;
                this.mDecorChildCount = i8;
                if (this.mFirstLayout) {
                    z = false;
                    scrollToItem(this.mCurItem, false, 0, false);
                } else {
                    z = false;
                }
                this.mFirstLayout = z;
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LayoutParams layoutParams;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int measuredWidth = getMeasuredWidth();
        int i6 = measuredWidth / 10;
        this.mGutterSize = Math.min(i6, this.mDefaultGutterSize);
        int paddingLeft = (measuredWidth - getPaddingLeft()) - getPaddingRight();
        int measuredHeight = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        int childCount = getChildCount();
        int i7 = 0;
        while (i7 < childCount) {
            View childAt = getChildAt(i7);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams2 = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams2 == null || !layoutParams2.isDecor) {
                    i = measuredWidth;
                    i2 = i6;
                } else {
                    int i8 = layoutParams2.gravity & 7;
                    int i9 = layoutParams2.gravity & 112;
                    int i10 = Integer.MIN_VALUE;
                    int i11 = Integer.MIN_VALUE;
                    boolean z = i9 == 48 || i9 == 80;
                    boolean z2 = i8 == 3 || i8 == 5;
                    if (z) {
                        i10 = BasicMeasure.EXACTLY;
                    } else if (z2) {
                        i11 = BasicMeasure.EXACTLY;
                    }
                    int i12 = paddingLeft;
                    int i13 = measuredHeight;
                    i = measuredWidth;
                    if (layoutParams2.width != -2) {
                        i10 = BasicMeasure.EXACTLY;
                        i3 = layoutParams2.width != -1 ? layoutParams2.width : i12;
                    } else {
                        i3 = i12;
                    }
                    if (layoutParams2.height == -2) {
                        i5 = i11;
                        i4 = i13;
                    } else if (layoutParams2.height != -1) {
                        i4 = layoutParams2.height;
                        i5 = 1073741824;
                    } else {
                        i5 = 1073741824;
                        i4 = i13;
                    }
                    i2 = i6;
                    int i14 = i3;
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(i3, i10), View.MeasureSpec.makeMeasureSpec(i4, i5));
                    if (z) {
                        measuredHeight -= childAt.getMeasuredHeight();
                    } else if (z2) {
                        paddingLeft -= childAt.getMeasuredWidth();
                    }
                }
            } else {
                i = measuredWidth;
                i2 = i6;
            }
            i7++;
            int i15 = widthMeasureSpec;
            int i16 = heightMeasureSpec;
            i6 = i2;
            measuredWidth = i;
        }
        int i17 = measuredWidth;
        int i18 = i6;
        this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(paddingLeft, BasicMeasure.EXACTLY);
        this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredHeight, BasicMeasure.EXACTLY);
        this.mInLayout = true;
        populate();
        this.mInLayout = false;
        int childCount2 = getChildCount();
        for (int i19 = 0; i19 < childCount2; i19++) {
            View childAt2 = getChildAt(i19);
            if (childAt2.getVisibility() != 8 && ((layoutParams = (LayoutParams) childAt2.getLayoutParams()) == null || !layoutParams.isDecor)) {
                childAt2.measure(View.MeasureSpec.makeMeasureSpec((int) (((float) paddingLeft) * layoutParams.widthFactor), BasicMeasure.EXACTLY), this.mChildHeightMeasureSpec);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPageScrolled(int position, float offset, int offsetPixels) {
        int i;
        if (this.mDecorChildCount > 0) {
            int scrollX = getScrollX();
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            int width = getWidth();
            int childCount = getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = getChildAt(i2);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.isDecor) {
                    switch (layoutParams.gravity & 7) {
                        case 1:
                            i = Math.max((width - childAt.getMeasuredWidth()) / 2, paddingLeft);
                            break;
                        case 3:
                            i = paddingLeft;
                            paddingLeft += childAt.getWidth();
                            break;
                        case 5:
                            i = (width - paddingRight) - childAt.getMeasuredWidth();
                            paddingRight += childAt.getMeasuredWidth();
                            break;
                        default:
                            i = paddingLeft;
                            break;
                    }
                    int left = (i + scrollX) - childAt.getLeft();
                    if (left != 0) {
                        childAt.offsetLeftAndRight(left);
                    }
                }
            }
        }
        dispatchOnPageScrolled(position, offset, offsetPixels);
        if (this.mPageTransformer != null) {
            int scrollX2 = getScrollX();
            int childCount2 = getChildCount();
            for (int i3 = 0; i3 < childCount2; i3++) {
                View childAt2 = getChildAt(i3);
                if (!((LayoutParams) childAt2.getLayoutParams()).isDecor) {
                    this.mPageTransformer.transformPage(childAt2, ((float) (childAt2.getLeft() - scrollX2)) / ((float) getClientWidth()));
                }
            }
        }
        this.mCalledSuper = true;
    }

    /* access modifiers changed from: protected */
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int i;
        int i2;
        int i3;
        ItemInfo infoForChild;
        int childCount = getChildCount();
        if ((direction & 2) != 0) {
            i3 = 0;
            i2 = 1;
            i = childCount;
        } else {
            i3 = childCount - 1;
            i2 = -1;
            i = -1;
        }
        for (int i4 = i3; i4 != i; i4 += i2) {
            View childAt = getChildAt(i4);
            if (childAt.getVisibility() == 0 && (infoForChild = infoForChild(childAt)) != null && infoForChild.position == this.mCurItem && childAt.requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
        }
        return false;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            pagerAdapter.restoreState(savedState.adapterState, savedState.loader);
            setCurrentItemInternal(savedState.position, false, true);
            return;
        }
        this.mRestoredCurItem = savedState.position;
        this.mRestoredAdapterState = savedState.adapterState;
        this.mRestoredClassLoader = savedState.loader;
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.position = this.mCurItem;
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            savedState.adapterState = pagerAdapter.saveState();
        }
        return savedState;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw) {
            int i = this.mPageMargin;
            recomputeScrollPosition(w, oldw, i, i);
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        PagerAdapter pagerAdapter;
        MotionEvent motionEvent = ev;
        if (this.mFakeDragging) {
            return true;
        }
        if ((ev.getAction() == 0 && ev.getEdgeFlags() != 0) || (pagerAdapter = this.mAdapter) == null || pagerAdapter.getCount() == 0) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int action = ev.getAction();
        boolean z = false;
        switch (action & 255) {
            case 0:
                int i = action;
                this.mScroller.abortAnimation();
                this.mPopulatePending = false;
                populate();
                float x = ev.getX();
                this.mInitialMotionX = x;
                this.mLastMotionX = x;
                float y = ev.getY();
                this.mInitialMotionY = y;
                this.mLastMotionY = y;
                this.mActivePointerId = motionEvent.getPointerId(0);
                break;
            case 1:
                if (!this.mIsBeingDragged) {
                    int i2 = action;
                    break;
                } else {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                    int xVelocity = (int) velocityTracker.getXVelocity(this.mActivePointerId);
                    this.mPopulatePending = true;
                    int clientWidth = getClientWidth();
                    int scrollX = getScrollX();
                    ItemInfo infoForCurrentScrollPosition = infoForCurrentScrollPosition();
                    int i3 = action;
                    setCurrentItemInternal(determineTargetPage(infoForCurrentScrollPosition.position, ((((float) scrollX) / ((float) clientWidth)) - infoForCurrentScrollPosition.offset) / (infoForCurrentScrollPosition.widthFactor + (((float) this.mPageMargin) / ((float) clientWidth))), xVelocity, (int) (motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX)), true, true, xVelocity);
                    z = resetTouch();
                    break;
                }
            case 2:
                if (!this.mIsBeingDragged) {
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (findPointerIndex == -1) {
                        z = resetTouch();
                        int i4 = action;
                        break;
                    } else {
                        float x2 = motionEvent.getX(findPointerIndex);
                        float abs = Math.abs(x2 - this.mLastMotionX);
                        float y2 = motionEvent.getY(findPointerIndex);
                        float abs2 = Math.abs(y2 - this.mLastMotionY);
                        if (abs > ((float) this.mTouchSlop) && abs > abs2) {
                            this.mIsBeingDragged = true;
                            requestParentDisallowInterceptTouchEvent(true);
                            float f = this.mInitialMotionX;
                            this.mLastMotionX = x2 - f > 0.0f ? f + ((float) this.mTouchSlop) : f - ((float) this.mTouchSlop);
                            this.mLastMotionY = y2;
                            setScrollState(1);
                            setScrollingCacheEnabled(true);
                            ViewParent parent = getParent();
                            if (parent != null) {
                                parent.requestDisallowInterceptTouchEvent(true);
                            }
                        }
                    }
                }
                if (!this.mIsBeingDragged) {
                    int i5 = action;
                    break;
                } else {
                    z = false | performDrag(motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId)));
                    int i6 = action;
                    break;
                }
            case 3:
                if (!this.mIsBeingDragged) {
                    int i7 = action;
                    break;
                } else {
                    scrollToItem(this.mCurItem, true, 0, false);
                    z = resetTouch();
                    int i8 = action;
                    break;
                }
            case 5:
                int actionIndex = ev.getActionIndex();
                this.mLastMotionX = motionEvent.getX(actionIndex);
                this.mActivePointerId = motionEvent.getPointerId(actionIndex);
                int i9 = action;
                break;
            case 6:
                onSecondaryPointerUp(ev);
                this.mLastMotionX = motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId));
                int i10 = action;
                break;
            default:
                int i11 = action;
                break;
        }
        if (z) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean pageLeft() {
        int i = this.mCurItem;
        if (i <= 0) {
            return false;
        }
        setCurrentItem(i - 1, true);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean pageRight() {
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter == null || this.mCurItem >= pagerAdapter.getCount() - 1) {
            return false;
        }
        setCurrentItem(this.mCurItem + 1, true);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void populate() {
        populate(this.mCurItem);
    }

    /* access modifiers changed from: package-private */
    public void populate(int newCurrentItem) {
        ItemInfo itemInfo;
        String str;
        ItemInfo infoForChild;
        int i;
        int i2;
        int i3 = newCurrentItem;
        int i4 = this.mCurItem;
        if (i4 != i3) {
            ItemInfo infoForPosition = infoForPosition(i4);
            this.mCurItem = i3;
            itemInfo = infoForPosition;
        } else {
            itemInfo = null;
        }
        if (this.mAdapter == null) {
            sortChildDrawingOrder();
        } else if (this.mPopulatePending) {
            sortChildDrawingOrder();
        } else if (getWindowToken() != null) {
            this.mAdapter.startUpdate((ViewGroup) this);
            int i5 = this.mOffscreenPageLimit;
            int max = Math.max(0, this.mCurItem - i5);
            int count = this.mAdapter.getCount();
            int min = Math.min(count - 1, this.mCurItem + i5);
            if (count == this.mExpectedAdapterCount) {
                ItemInfo itemInfo2 = null;
                int i6 = 0;
                while (true) {
                    if (i6 >= this.mItems.size()) {
                        break;
                    }
                    ItemInfo itemInfo3 = this.mItems.get(i6);
                    if (itemInfo3.position < this.mCurItem) {
                        i6++;
                    } else if (itemInfo3.position == this.mCurItem) {
                        itemInfo2 = itemInfo3;
                    }
                }
                if (itemInfo2 == null && count > 0) {
                    itemInfo2 = addNewItem(this.mCurItem, i6);
                }
                if (itemInfo2 != null) {
                    float f = 0.0f;
                    int i7 = i6 - 1;
                    ItemInfo itemInfo4 = i7 >= 0 ? this.mItems.get(i7) : null;
                    int clientWidth = getClientWidth();
                    float paddingLeft = clientWidth <= 0 ? 0.0f : (2.0f - itemInfo2.widthFactor) + (((float) getPaddingLeft()) / ((float) clientWidth));
                    int i8 = this.mCurItem - 1;
                    while (i8 >= 0) {
                        if (f < paddingLeft || i8 >= max) {
                            if (itemInfo4 == null || i8 != itemInfo4.position) {
                                f += addNewItem(i8, i7 + 1).widthFactor;
                                i6++;
                                itemInfo4 = i7 >= 0 ? this.mItems.get(i7) : null;
                                ItemInfo itemInfo5 = itemInfo4;
                            } else {
                                f += itemInfo4.widthFactor;
                                i7--;
                                itemInfo4 = i7 >= 0 ? this.mItems.get(i7) : null;
                            }
                        } else if (itemInfo4 == null) {
                            break;
                        } else if (i8 == itemInfo4.position && !itemInfo4.scrolling) {
                            this.mItems.remove(i7);
                            this.mAdapter.destroyItem((ViewGroup) this, i8, itemInfo4.object);
                            i7--;
                            i6--;
                            itemInfo4 = i7 >= 0 ? this.mItems.get(i7) : null;
                        }
                        i8--;
                        int i9 = newCurrentItem;
                    }
                    float f2 = itemInfo2.widthFactor;
                    int i10 = i6 + 1;
                    if (f2 < 2.0f) {
                        ItemInfo itemInfo6 = i10 < this.mItems.size() ? this.mItems.get(i10) : null;
                        float paddingRight = clientWidth <= 0 ? 0.0f : (((float) getPaddingRight()) / ((float) clientWidth)) + 2.0f;
                        int i11 = this.mCurItem + 1;
                        while (true) {
                            if (i11 >= count) {
                                int i12 = i5;
                                int i13 = max;
                                break;
                            }
                            if (f2 < paddingRight || i11 <= min) {
                                i2 = i5;
                                i = max;
                                if (itemInfo6 == null || i11 != itemInfo6.position) {
                                    ItemInfo addNewItem = addNewItem(i11, i10);
                                    i10++;
                                    f2 += addNewItem.widthFactor;
                                    itemInfo6 = i10 < this.mItems.size() ? this.mItems.get(i10) : null;
                                } else {
                                    f2 += itemInfo6.widthFactor;
                                    i10++;
                                    itemInfo6 = i10 < this.mItems.size() ? this.mItems.get(i10) : null;
                                }
                            } else if (itemInfo6 == null) {
                                int i14 = i5;
                                int i15 = max;
                                break;
                            } else {
                                i2 = i5;
                                if (i11 != itemInfo6.position || itemInfo6.scrolling) {
                                    i = max;
                                } else {
                                    this.mItems.remove(i10);
                                    i = max;
                                    this.mAdapter.destroyItem((ViewGroup) this, i11, itemInfo6.object);
                                    itemInfo6 = i10 < this.mItems.size() ? this.mItems.get(i10) : null;
                                }
                            }
                            i11++;
                            i5 = i2;
                            max = i;
                        }
                        ItemInfo itemInfo7 = itemInfo6;
                    } else {
                        int i16 = i5;
                        int i17 = max;
                    }
                    calculatePageOffsets(itemInfo2, i6, itemInfo);
                    this.mAdapter.setPrimaryItem((ViewGroup) this, this.mCurItem, itemInfo2.object);
                } else {
                    int i18 = i5;
                    int i19 = max;
                }
                this.mAdapter.finishUpdate((ViewGroup) this);
                int childCount = getChildCount();
                for (int i20 = 0; i20 < childCount; i20++) {
                    View childAt = getChildAt(i20);
                    LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                    layoutParams.childIndex = i20;
                    if (!layoutParams.isDecor) {
                        if (layoutParams.widthFactor == 0.0f && (infoForChild = infoForChild(childAt)) != null) {
                            layoutParams.widthFactor = infoForChild.widthFactor;
                            layoutParams.position = infoForChild.position;
                        }
                    }
                }
                sortChildDrawingOrder();
                if (hasFocus()) {
                    View findFocus = findFocus();
                    ItemInfo infoForAnyChild = findFocus != null ? infoForAnyChild(findFocus) : null;
                    if (infoForAnyChild == null || infoForAnyChild.position != this.mCurItem) {
                        int i21 = 0;
                        while (i21 < getChildCount()) {
                            View childAt2 = getChildAt(i21);
                            ItemInfo infoForChild2 = infoForChild(childAt2);
                            if (infoForChild2 == null || infoForChild2.position != this.mCurItem || !childAt2.requestFocus(2)) {
                                i21++;
                            } else {
                                return;
                            }
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            int i22 = i5;
            int i23 = max;
            try {
                str = getResources().getResourceName(getId());
            } catch (Resources.NotFoundException e) {
                String hexString = Integer.toHexString(getId());
                Log1F380D.a((Object) hexString);
                str = hexString;
            }
            throw new IllegalStateException("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: " + this.mExpectedAdapterCount + ", found: " + count + " Pager id: " + str + " Pager class: " + getClass() + " Problematic adapter: " + this.mAdapter.getClass());
        }
    }

    public void removeOnAdapterChangeListener(OnAdapterChangeListener listener) {
        List<OnAdapterChangeListener> list = this.mAdapterChangeListeners;
        if (list != null) {
            list.remove(listener);
        }
    }

    public void removeOnPageChangeListener(OnPageChangeListener listener) {
        List<OnPageChangeListener> list = this.mOnPageChangeListeners;
        if (list != null) {
            list.remove(listener);
        }
    }

    public void removeView(View view) {
        if (this.mInLayout) {
            removeViewInLayout(view);
        } else {
            super.removeView(view);
        }
    }

    public void setAdapter(PagerAdapter adapter) {
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            pagerAdapter.setViewPagerObserver((DataSetObserver) null);
            this.mAdapter.startUpdate((ViewGroup) this);
            for (int i = 0; i < this.mItems.size(); i++) {
                ItemInfo itemInfo = this.mItems.get(i);
                this.mAdapter.destroyItem((ViewGroup) this, itemInfo.position, itemInfo.object);
            }
            this.mAdapter.finishUpdate((ViewGroup) this);
            this.mItems.clear();
            removeNonDecorViews();
            this.mCurItem = 0;
            scrollTo(0, 0);
        }
        PagerAdapter pagerAdapter2 = this.mAdapter;
        this.mAdapter = adapter;
        this.mExpectedAdapterCount = 0;
        if (adapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver();
            }
            this.mAdapter.setViewPagerObserver(this.mObserver);
            this.mPopulatePending = false;
            boolean z = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else if (!z) {
                populate();
            } else {
                requestLayout();
            }
        }
        List<OnAdapterChangeListener> list = this.mAdapterChangeListeners;
        if (list != null && !list.isEmpty()) {
            int size = this.mAdapterChangeListeners.size();
            for (int i2 = 0; i2 < size; i2++) {
                this.mAdapterChangeListeners.get(i2).onAdapterChanged(this, pagerAdapter2, adapter);
            }
        }
    }

    public void setCurrentItem(int item) {
        this.mPopulatePending = false;
        setCurrentItemInternal(item, !this.mFirstLayout, false);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        this.mPopulatePending = false;
        setCurrentItemInternal(item, smoothScroll, false);
    }

    /* access modifiers changed from: package-private */
    public void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
        setCurrentItemInternal(item, smoothScroll, always, 0);
    }

    /* access modifiers changed from: package-private */
    public void setCurrentItemInternal(int item, boolean smoothScroll, boolean always, int velocity) {
        PagerAdapter pagerAdapter = this.mAdapter;
        boolean z = false;
        if (pagerAdapter == null || pagerAdapter.getCount() <= 0) {
            setScrollingCacheEnabled(false);
        } else if (always || this.mCurItem != item || this.mItems.size() == 0) {
            if (item < 0) {
                item = 0;
            } else if (item >= this.mAdapter.getCount()) {
                item = this.mAdapter.getCount() - 1;
            }
            int i = this.mOffscreenPageLimit;
            int i2 = this.mCurItem;
            if (item > i2 + i || item < i2 - i) {
                for (int i3 = 0; i3 < this.mItems.size(); i3++) {
                    this.mItems.get(i3).scrolling = true;
                }
            }
            if (this.mCurItem != item) {
                z = true;
            }
            boolean z2 = z;
            if (this.mFirstLayout) {
                this.mCurItem = item;
                if (z2) {
                    dispatchOnPageSelected(item);
                }
                requestLayout();
                return;
            }
            populate(item);
            scrollToItem(item, smoothScroll, velocity, z2);
        } else {
            setScrollingCacheEnabled(false);
        }
    }

    /* access modifiers changed from: package-private */
    public OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener listener) {
        OnPageChangeListener onPageChangeListener = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = listener;
        return onPageChangeListener;
    }

    public void setOffscreenPageLimit(int limit) {
        if (limit < 1) {
            Log.w(TAG, "Requested offscreen page limit " + limit + " too small; defaulting to " + 1);
            limit = 1;
        }
        if (limit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = limit;
            populate();
        }
    }

    @Deprecated
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public void setPageMargin(int marginPixels) {
        int i = this.mPageMargin;
        this.mPageMargin = marginPixels;
        int width = getWidth();
        recomputeScrollPosition(width, width, marginPixels, i);
        requestLayout();
    }

    public void setPageMarginDrawable(int resId) {
        setPageMarginDrawable(ContextCompat.getDrawable(getContext(), resId));
    }

    public void setPageMarginDrawable(Drawable d) {
        this.mMarginDrawable = d;
        if (d != null) {
            refreshDrawableState();
        }
        setWillNotDraw(d == null);
        invalidate();
    }

    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        setPageTransformer(reverseDrawingOrder, transformer, 2);
    }

    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer, int pageLayerType) {
        int i = 1;
        boolean z = transformer != null;
        boolean z2 = z != (this.mPageTransformer != null);
        this.mPageTransformer = transformer;
        setChildrenDrawingOrderEnabled(z);
        if (z) {
            if (reverseDrawingOrder) {
                i = 2;
            }
            this.mDrawingOrder = i;
            this.mPageTransformerLayerType = pageLayerType;
        } else {
            this.mDrawingOrder = 0;
        }
        if (z2) {
            populate();
        }
    }

    /* access modifiers changed from: package-private */
    public void setScrollState(int newState) {
        if (this.mScrollState != newState) {
            this.mScrollState = newState;
            if (this.mPageTransformer != null) {
                enableLayers(newState != 0);
            }
            dispatchOnScrollStateChanged(newState);
        }
    }

    /* access modifiers changed from: package-private */
    public void smoothScrollTo(int x, int y) {
        smoothScrollTo(x, y, 0);
    }

    /* access modifiers changed from: package-private */
    public void smoothScrollTo(int x, int y, int velocity) {
        int i;
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(false);
            return;
        }
        Scroller scroller = this.mScroller;
        if (scroller != null && !scroller.isFinished()) {
            i = this.mIsScrollStarted ? this.mScroller.getCurrX() : this.mScroller.getStartX();
            this.mScroller.abortAnimation();
            setScrollingCacheEnabled(false);
        } else {
            i = getScrollX();
        }
        int scrollY = getScrollY();
        int i2 = x - i;
        int i3 = y - scrollY;
        if (i2 == 0 && i3 == 0) {
            completeScroll(false);
            populate();
            setScrollState(0);
            return;
        }
        setScrollingCacheEnabled(true);
        setScrollState(2);
        int clientWidth = getClientWidth();
        int i4 = clientWidth / 2;
        float distanceInfluenceForSnapDuration = ((float) i4) + (((float) i4) * distanceInfluenceForSnapDuration(Math.min(1.0f, (((float) Math.abs(i2)) * 1.0f) / ((float) clientWidth))));
        int abs = Math.abs(velocity);
        int min = Math.min(abs > 0 ? Math.round(Math.abs(distanceInfluenceForSnapDuration / ((float) abs)) * 1000.0f) * 4 : (int) ((1.0f + (((float) Math.abs(i2)) / (((float) this.mPageMargin) + (((float) clientWidth) * this.mAdapter.getPageWidth(this.mCurItem))))) * 100.0f), 600);
        this.mIsScrollStarted = false;
        int i5 = abs;
        this.mScroller.startScroll(i, scrollY, i2, i3, min);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mMarginDrawable;
    }
}

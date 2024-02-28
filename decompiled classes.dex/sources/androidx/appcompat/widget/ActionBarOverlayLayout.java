package androidx.appcompat.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.OverScroller;
import androidx.appcompat.R;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.core.graphics.Insets;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActionBarOverlayLayout extends ViewGroup implements DecorContentParent, NestedScrollingParent, NestedScrollingParent2, NestedScrollingParent3 {
    private static final int ACTION_BAR_ANIMATE_DELAY = 600;
    static final int[] ATTRS = {R.attr.actionBarSize, 16842841};
    private static final String TAG = "ActionBarOverlayLayout";
    private int mActionBarHeight;
    ActionBarContainer mActionBarTop;
    private ActionBarVisibilityCallback mActionBarVisibilityCallback;
    private final Runnable mAddActionBarHideOffset;
    boolean mAnimatingForFling;
    private final Rect mBaseContentInsets;
    private WindowInsetsCompat mBaseInnerInsets;
    private final Rect mBaseInnerInsetsRect;
    private ContentFrameLayout mContent;
    private final Rect mContentInsets;
    ViewPropertyAnimator mCurrentActionBarTopAnimator;
    private DecorToolbar mDecorToolbar;
    private OverScroller mFlingEstimator;
    private boolean mHasNonEmbeddedTabs;
    private boolean mHideOnContentScroll;
    private int mHideOnContentScrollReference;
    private boolean mIgnoreWindowContentOverlay;
    private WindowInsetsCompat mInnerInsets;
    private final Rect mInnerInsetsRect;
    private final Rect mLastBaseContentInsets;
    private WindowInsetsCompat mLastBaseInnerInsets;
    private final Rect mLastBaseInnerInsetsRect;
    private WindowInsetsCompat mLastInnerInsets;
    private final Rect mLastInnerInsetsRect;
    private int mLastSystemUiVisibility;
    private boolean mOverlayMode;
    private final NestedScrollingParentHelper mParentHelper;
    private final Runnable mRemoveActionBarHideOffset;
    final AnimatorListenerAdapter mTopAnimatorListener;
    private Drawable mWindowContentOverlay;
    private int mWindowVisibility;

    public interface ActionBarVisibilityCallback {
        void enableContentAnimations(boolean z);

        void hideForSystem();

        void onContentScrollStarted();

        void onContentScrollStopped();

        void onWindowVisibilityChanged(int i);

        void showForSystem();
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
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
    }

    public ActionBarOverlayLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public ActionBarOverlayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mWindowVisibility = 0;
        this.mBaseContentInsets = new Rect();
        this.mLastBaseContentInsets = new Rect();
        this.mContentInsets = new Rect();
        this.mBaseInnerInsetsRect = new Rect();
        this.mLastBaseInnerInsetsRect = new Rect();
        this.mInnerInsetsRect = new Rect();
        this.mLastInnerInsetsRect = new Rect();
        this.mBaseInnerInsets = WindowInsetsCompat.CONSUMED;
        this.mLastBaseInnerInsets = WindowInsetsCompat.CONSUMED;
        this.mInnerInsets = WindowInsetsCompat.CONSUMED;
        this.mLastInnerInsets = WindowInsetsCompat.CONSUMED;
        this.mTopAnimatorListener = new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
                ActionBarOverlayLayout.this.mCurrentActionBarTopAnimator = null;
                ActionBarOverlayLayout.this.mAnimatingForFling = false;
            }

            public void onAnimationEnd(Animator animator) {
                ActionBarOverlayLayout.this.mCurrentActionBarTopAnimator = null;
                ActionBarOverlayLayout.this.mAnimatingForFling = false;
            }
        };
        this.mRemoveActionBarHideOffset = new Runnable() {
            public void run() {
                ActionBarOverlayLayout.this.haltActionBarHideOffsetAnimations();
                ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
                actionBarOverlayLayout.mCurrentActionBarTopAnimator = actionBarOverlayLayout.mActionBarTop.animate().translationY(0.0f).setListener(ActionBarOverlayLayout.this.mTopAnimatorListener);
            }
        };
        this.mAddActionBarHideOffset = new Runnable() {
            public void run() {
                ActionBarOverlayLayout.this.haltActionBarHideOffsetAnimations();
                ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
                actionBarOverlayLayout.mCurrentActionBarTopAnimator = actionBarOverlayLayout.mActionBarTop.animate().translationY((float) (-ActionBarOverlayLayout.this.mActionBarTop.getHeight())).setListener(ActionBarOverlayLayout.this.mTopAnimatorListener);
            }
        };
        init(context);
        this.mParentHelper = new NestedScrollingParentHelper(this);
    }

    private void addActionBarHideOffset() {
        haltActionBarHideOffsetAnimations();
        this.mAddActionBarHideOffset.run();
    }

    private boolean applyInsets(View view, Rect insets, boolean left, boolean top, boolean bottom, boolean right) {
        boolean z = false;
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (left && layoutParams.leftMargin != insets.left) {
            z = true;
            layoutParams.leftMargin = insets.left;
        }
        if (top && layoutParams.topMargin != insets.top) {
            z = true;
            layoutParams.topMargin = insets.top;
        }
        if (right && layoutParams.rightMargin != insets.right) {
            z = true;
            layoutParams.rightMargin = insets.right;
        }
        if (!bottom || layoutParams.bottomMargin == insets.bottom) {
            return z;
        }
        layoutParams.bottomMargin = insets.bottom;
        return true;
    }

    private DecorToolbar getDecorToolbar(View view) {
        if (view instanceof DecorToolbar) {
            return (DecorToolbar) view;
        }
        if (view instanceof Toolbar) {
            return ((Toolbar) view).getWrapper();
        }
        throw new IllegalStateException("Can't make a decor toolbar out of " + view.getClass().getSimpleName());
    }

    private void init(Context context) {
        TypedArray obtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(ATTRS);
        boolean z = false;
        this.mActionBarHeight = obtainStyledAttributes.getDimensionPixelSize(0, 0);
        Drawable drawable = obtainStyledAttributes.getDrawable(1);
        this.mWindowContentOverlay = drawable;
        setWillNotDraw(drawable == null);
        obtainStyledAttributes.recycle();
        if (context.getApplicationInfo().targetSdkVersion < 19) {
            z = true;
        }
        this.mIgnoreWindowContentOverlay = z;
        this.mFlingEstimator = new OverScroller(context);
    }

    private void postAddActionBarHideOffset() {
        haltActionBarHideOffsetAnimations();
        postDelayed(this.mAddActionBarHideOffset, 600);
    }

    private void postRemoveActionBarHideOffset() {
        haltActionBarHideOffsetAnimations();
        postDelayed(this.mRemoveActionBarHideOffset, 600);
    }

    private void removeActionBarHideOffset() {
        haltActionBarHideOffsetAnimations();
        this.mRemoveActionBarHideOffset.run();
    }

    private boolean shouldHideActionBarOnFling(float velocityY) {
        this.mFlingEstimator.fling(0, 0, 0, (int) velocityY, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return this.mFlingEstimator.getFinalY() > this.mActionBarTop.getHeight();
    }

    public boolean canShowOverflowMenu() {
        pullChildren();
        return this.mDecorToolbar.canShowOverflowMenu();
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public void dismissPopups() {
        pullChildren();
        this.mDecorToolbar.dismissPopupMenus();
    }

    public void draw(Canvas c) {
        super.draw(c);
        if (this.mWindowContentOverlay != null && !this.mIgnoreWindowContentOverlay) {
            int bottom = this.mActionBarTop.getVisibility() == 0 ? (int) (((float) this.mActionBarTop.getBottom()) + this.mActionBarTop.getTranslationY() + 0.5f) : 0;
            this.mWindowContentOverlay.setBounds(0, bottom, getWidth(), this.mWindowContentOverlay.getIntrinsicHeight() + bottom);
            this.mWindowContentOverlay.draw(c);
        }
    }

    /* access modifiers changed from: protected */
    public boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= 21) {
            return super.fitSystemWindows(insets);
        }
        pullChildren();
        boolean applyInsets = applyInsets(this.mActionBarTop, insets, true, true, false, true);
        this.mBaseInnerInsetsRect.set(insets);
        ViewUtils.computeFitSystemWindows(this, this.mBaseInnerInsetsRect, this.mBaseContentInsets);
        if (!this.mLastBaseInnerInsetsRect.equals(this.mBaseInnerInsetsRect)) {
            applyInsets = true;
            this.mLastBaseInnerInsetsRect.set(this.mBaseInnerInsetsRect);
        }
        if (!this.mLastBaseContentInsets.equals(this.mBaseContentInsets)) {
            applyInsets = true;
            this.mLastBaseContentInsets.set(this.mBaseContentInsets);
        }
        if (!applyInsets) {
            return true;
        }
        requestLayout();
        return true;
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public int getActionBarHideOffset() {
        ActionBarContainer actionBarContainer = this.mActionBarTop;
        if (actionBarContainer != null) {
            return -((int) actionBarContainer.getTranslationY());
        }
        return 0;
    }

    public int getNestedScrollAxes() {
        return this.mParentHelper.getNestedScrollAxes();
    }

    public CharSequence getTitle() {
        pullChildren();
        return this.mDecorToolbar.getTitle();
    }

    /* access modifiers changed from: package-private */
    public void haltActionBarHideOffsetAnimations() {
        removeCallbacks(this.mRemoveActionBarHideOffset);
        removeCallbacks(this.mAddActionBarHideOffset);
        ViewPropertyAnimator viewPropertyAnimator = this.mCurrentActionBarTopAnimator;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
    }

    public boolean hasIcon() {
        pullChildren();
        return this.mDecorToolbar.hasIcon();
    }

    public boolean hasLogo() {
        pullChildren();
        return this.mDecorToolbar.hasLogo();
    }

    public boolean hideOverflowMenu() {
        pullChildren();
        return this.mDecorToolbar.hideOverflowMenu();
    }

    public void initFeature(int windowFeature) {
        pullChildren();
        switch (windowFeature) {
            case 2:
                this.mDecorToolbar.initProgress();
                return;
            case 5:
                this.mDecorToolbar.initIndeterminateProgress();
                return;
            case AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY:
                setOverlayMode(true);
                return;
            default:
                return;
        }
    }

    public boolean isHideOnContentScrollEnabled() {
        return this.mHideOnContentScroll;
    }

    public boolean isInOverlayMode() {
        return this.mOverlayMode;
    }

    public boolean isOverflowMenuShowPending() {
        pullChildren();
        return this.mDecorToolbar.isOverflowMenuShowPending();
    }

    public boolean isOverflowMenuShowing() {
        pullChildren();
        return this.mDecorToolbar.isOverflowMenuShowing();
    }

    public WindowInsets onApplyWindowInsets(WindowInsets in) {
        pullChildren();
        WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(in, this);
        boolean applyInsets = applyInsets(this.mActionBarTop, new Rect(windowInsetsCompat.getSystemWindowInsetLeft(), windowInsetsCompat.getSystemWindowInsetTop(), windowInsetsCompat.getSystemWindowInsetRight(), windowInsetsCompat.getSystemWindowInsetBottom()), true, true, false, true);
        ViewCompat.computeSystemWindowInsets(this, windowInsetsCompat, this.mBaseContentInsets);
        WindowInsetsCompat inset = windowInsetsCompat.inset(this.mBaseContentInsets.left, this.mBaseContentInsets.top, this.mBaseContentInsets.right, this.mBaseContentInsets.bottom);
        this.mBaseInnerInsets = inset;
        if (!this.mLastBaseInnerInsets.equals(inset)) {
            applyInsets = true;
            this.mLastBaseInnerInsets = this.mBaseInnerInsets;
        }
        if (!this.mLastBaseContentInsets.equals(this.mBaseContentInsets)) {
            applyInsets = true;
            this.mLastBaseContentInsets.set(this.mBaseContentInsets);
        }
        if (applyInsets) {
            requestLayout();
        }
        return windowInsetsCompat.consumeDisplayCutout().consumeSystemWindowInsets().consumeStableInsets().toWindowInsets();
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        init(getContext());
        ViewCompat.requestApplyInsets(this);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        haltActionBarHideOffsetAnimations();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                int i2 = layoutParams.leftMargin + paddingLeft;
                int i3 = layoutParams.topMargin + paddingTop;
                childAt.layout(i2, i3, i2 + measuredWidth, i3 + measuredHeight);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        pullChildren();
        int i = 0;
        measureChildWithMargins(this.mActionBarTop, widthMeasureSpec, 0, heightMeasureSpec, 0);
        LayoutParams layoutParams = (LayoutParams) this.mActionBarTop.getLayoutParams();
        int max = Math.max(0, this.mActionBarTop.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
        int max2 = Math.max(0, this.mActionBarTop.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
        int combineMeasuredStates = View.combineMeasuredStates(0, this.mActionBarTop.getMeasuredState());
        boolean z = (ViewCompat.getWindowSystemUiVisibility(this) & 256) != 0;
        if (z) {
            i = this.mActionBarHeight;
            if (this.mHasNonEmbeddedTabs && this.mActionBarTop.getTabContainer() != null) {
                i += this.mActionBarHeight;
            }
        } else if (this.mActionBarTop.getVisibility() != 8) {
            i = this.mActionBarTop.getMeasuredHeight();
        }
        this.mContentInsets.set(this.mBaseContentInsets);
        if (Build.VERSION.SDK_INT >= 21) {
            this.mInnerInsets = this.mBaseInnerInsets;
        } else {
            this.mInnerInsetsRect.set(this.mBaseInnerInsetsRect);
        }
        if (!this.mOverlayMode && !z) {
            this.mContentInsets.top += i;
            this.mContentInsets.bottom += 0;
            if (Build.VERSION.SDK_INT >= 21) {
                this.mInnerInsets = this.mInnerInsets.inset(0, i, 0, 0);
            }
        } else if (Build.VERSION.SDK_INT >= 21) {
            this.mInnerInsets = new WindowInsetsCompat.Builder(this.mInnerInsets).setSystemWindowInsets(Insets.of(this.mInnerInsets.getSystemWindowInsetLeft(), this.mInnerInsets.getSystemWindowInsetTop() + i, this.mInnerInsets.getSystemWindowInsetRight(), this.mInnerInsets.getSystemWindowInsetBottom() + 0)).build();
        } else {
            this.mInnerInsetsRect.top += i;
            this.mInnerInsetsRect.bottom += 0;
        }
        int i2 = i;
        applyInsets(this.mContent, this.mContentInsets, true, true, true, true);
        if (Build.VERSION.SDK_INT >= 21 && !this.mLastInnerInsets.equals(this.mInnerInsets)) {
            WindowInsetsCompat windowInsetsCompat = this.mInnerInsets;
            this.mLastInnerInsets = windowInsetsCompat;
            ViewCompat.dispatchApplyWindowInsets(this.mContent, windowInsetsCompat);
        } else if (Build.VERSION.SDK_INT < 21 && !this.mLastInnerInsetsRect.equals(this.mInnerInsetsRect)) {
            this.mLastInnerInsetsRect.set(this.mInnerInsetsRect);
            this.mContent.dispatchFitSystemWindows(this.mInnerInsetsRect);
        }
        measureChildWithMargins(this.mContent, widthMeasureSpec, 0, heightMeasureSpec, 0);
        LayoutParams layoutParams2 = (LayoutParams) this.mContent.getLayoutParams();
        int max3 = Math.max(max, this.mContent.getMeasuredWidth() + layoutParams2.leftMargin + layoutParams2.rightMargin);
        int max4 = Math.max(max2, this.mContent.getMeasuredHeight() + layoutParams2.topMargin + layoutParams2.bottomMargin);
        int combineMeasuredStates2 = View.combineMeasuredStates(combineMeasuredStates, this.mContent.getMeasuredState());
        setMeasuredDimension(View.resolveSizeAndState(Math.max(max3 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), widthMeasureSpec, combineMeasuredStates2), View.resolveSizeAndState(Math.max(max4 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), heightMeasureSpec, combineMeasuredStates2 << 16));
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if (!this.mHideOnContentScroll || !consumed) {
            return false;
        }
        if (shouldHideActionBarOnFling(velocityY)) {
            addActionBarHideOffset();
        } else {
            removeActionBarHideOffset();
        }
        this.mAnimatingForFling = true;
        return true;
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed, int type) {
        if (type == 0) {
            onNestedPreScroll(target, dx, dy, consumed);
        }
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        int i = this.mHideOnContentScrollReference + dyConsumed;
        this.mHideOnContentScrollReference = i;
        setActionBarHideOffset(i);
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (type == 0) {
            onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        }
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int[] consumed) {
        onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
    }

    public void onNestedScrollAccepted(View child, View target, int axes) {
        this.mParentHelper.onNestedScrollAccepted(child, target, axes);
        this.mHideOnContentScrollReference = getActionBarHideOffset();
        haltActionBarHideOffsetAnimations();
        ActionBarVisibilityCallback actionBarVisibilityCallback = this.mActionBarVisibilityCallback;
        if (actionBarVisibilityCallback != null) {
            actionBarVisibilityCallback.onContentScrollStarted();
        }
    }

    public void onNestedScrollAccepted(View child, View target, int axes, int type) {
        if (type == 0) {
            onNestedScrollAccepted(child, target, axes);
        }
    }

    public boolean onStartNestedScroll(View child, View target, int axes) {
        if ((axes & 2) == 0 || this.mActionBarTop.getVisibility() != 0) {
            return false;
        }
        return this.mHideOnContentScroll;
    }

    public boolean onStartNestedScroll(View child, View target, int axes, int type) {
        return type == 0 && onStartNestedScroll(child, target, axes);
    }

    public void onStopNestedScroll(View target) {
        if (this.mHideOnContentScroll && !this.mAnimatingForFling) {
            if (this.mHideOnContentScrollReference <= this.mActionBarTop.getHeight()) {
                postRemoveActionBarHideOffset();
            } else {
                postAddActionBarHideOffset();
            }
        }
        ActionBarVisibilityCallback actionBarVisibilityCallback = this.mActionBarVisibilityCallback;
        if (actionBarVisibilityCallback != null) {
            actionBarVisibilityCallback.onContentScrollStopped();
        }
    }

    public void onStopNestedScroll(View target, int type) {
        if (type == 0) {
            onStopNestedScroll(target);
        }
    }

    @Deprecated
    public void onWindowSystemUiVisibilityChanged(int visible) {
        if (Build.VERSION.SDK_INT >= 16) {
            super.onWindowSystemUiVisibilityChanged(visible);
        }
        pullChildren();
        int i = this.mLastSystemUiVisibility ^ visible;
        this.mLastSystemUiVisibility = visible;
        boolean z = true;
        boolean z2 = (visible & 4) == 0;
        boolean z3 = (visible & 256) != 0;
        ActionBarVisibilityCallback actionBarVisibilityCallback = this.mActionBarVisibilityCallback;
        if (actionBarVisibilityCallback != null) {
            if (z3) {
                z = false;
            }
            actionBarVisibilityCallback.enableContentAnimations(z);
            if (z2 || !z3) {
                this.mActionBarVisibilityCallback.showForSystem();
            } else {
                this.mActionBarVisibilityCallback.hideForSystem();
            }
        }
        if ((i & 256) != 0 && this.mActionBarVisibilityCallback != null) {
            ViewCompat.requestApplyInsets(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        this.mWindowVisibility = visibility;
        ActionBarVisibilityCallback actionBarVisibilityCallback = this.mActionBarVisibilityCallback;
        if (actionBarVisibilityCallback != null) {
            actionBarVisibilityCallback.onWindowVisibilityChanged(visibility);
        }
    }

    /* access modifiers changed from: package-private */
    public void pullChildren() {
        if (this.mContent == null) {
            this.mContent = (ContentFrameLayout) findViewById(R.id.action_bar_activity_content);
            this.mActionBarTop = (ActionBarContainer) findViewById(R.id.action_bar_container);
            this.mDecorToolbar = getDecorToolbar(findViewById(R.id.action_bar));
        }
    }

    public void restoreToolbarHierarchyState(SparseArray<Parcelable> sparseArray) {
        pullChildren();
        this.mDecorToolbar.restoreHierarchyState(sparseArray);
    }

    public void saveToolbarHierarchyState(SparseArray<Parcelable> sparseArray) {
        pullChildren();
        this.mDecorToolbar.saveHierarchyState(sparseArray);
    }

    public void setActionBarHideOffset(int offset) {
        haltActionBarHideOffsetAnimations();
        this.mActionBarTop.setTranslationY((float) (-Math.max(0, Math.min(offset, this.mActionBarTop.getHeight()))));
    }

    public void setActionBarVisibilityCallback(ActionBarVisibilityCallback cb) {
        this.mActionBarVisibilityCallback = cb;
        if (getWindowToken() != null) {
            this.mActionBarVisibilityCallback.onWindowVisibilityChanged(this.mWindowVisibility);
            if (this.mLastSystemUiVisibility != 0) {
                onWindowSystemUiVisibilityChanged(this.mLastSystemUiVisibility);
                ViewCompat.requestApplyInsets(this);
            }
        }
    }

    public void setHasNonEmbeddedTabs(boolean hasNonEmbeddedTabs) {
        this.mHasNonEmbeddedTabs = hasNonEmbeddedTabs;
    }

    public void setHideOnContentScrollEnabled(boolean hideOnContentScroll) {
        if (hideOnContentScroll != this.mHideOnContentScroll) {
            this.mHideOnContentScroll = hideOnContentScroll;
            if (!hideOnContentScroll) {
                haltActionBarHideOffsetAnimations();
                setActionBarHideOffset(0);
            }
        }
    }

    public void setIcon(int resId) {
        pullChildren();
        this.mDecorToolbar.setIcon(resId);
    }

    public void setIcon(Drawable d) {
        pullChildren();
        this.mDecorToolbar.setIcon(d);
    }

    public void setLogo(int resId) {
        pullChildren();
        this.mDecorToolbar.setLogo(resId);
    }

    public void setMenu(Menu menu, MenuPresenter.Callback cb) {
        pullChildren();
        this.mDecorToolbar.setMenu(menu, cb);
    }

    public void setMenuPrepared() {
        pullChildren();
        this.mDecorToolbar.setMenuPrepared();
    }

    public void setOverlayMode(boolean overlayMode) {
        this.mOverlayMode = overlayMode;
        this.mIgnoreWindowContentOverlay = overlayMode && getContext().getApplicationInfo().targetSdkVersion < 19;
    }

    public void setShowingForActionMode(boolean showing) {
    }

    public void setUiOptions(int uiOptions) {
    }

    public void setWindowCallback(Window.Callback cb) {
        pullChildren();
        this.mDecorToolbar.setWindowCallback(cb);
    }

    public void setWindowTitle(CharSequence title) {
        pullChildren();
        this.mDecorToolbar.setWindowTitle(title);
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public boolean showOverflowMenu() {
        pullChildren();
        return this.mDecorToolbar.showOverflowMenu();
    }
}

package com.google.android.material.appbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.math.MathUtils;
import androidx.core.util.ObjectsCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AppBarLayout extends LinearLayout implements CoordinatorLayout.AttachedBehavior {
    private static final int DEF_STYLE_RES = R.style.Widget_Design_AppBarLayout;
    private static final int INVALID_SCROLL_RANGE = -1;
    static final int PENDING_ACTION_ANIMATE_ENABLED = 4;
    static final int PENDING_ACTION_COLLAPSED = 2;
    static final int PENDING_ACTION_EXPANDED = 1;
    static final int PENDING_ACTION_FORCE = 8;
    static final int PENDING_ACTION_NONE = 0;
    private Behavior behavior;
    private int currentOffset;
    private int downPreScrollRange;
    private int downScrollRange;
    private ValueAnimator elevationOverlayAnimator;
    private boolean haveChildWithInterpolator;
    private WindowInsetsCompat lastInsets;
    private boolean liftOnScroll;
    /* access modifiers changed from: private */
    public final List<LiftOnScrollListener> liftOnScrollListeners;
    private WeakReference<View> liftOnScrollTargetView;
    private int liftOnScrollTargetViewId;
    private boolean liftable;
    private boolean liftableOverride;
    private boolean lifted;
    private List<BaseOnOffsetChangedListener> listeners;
    private int pendingAction;
    /* access modifiers changed from: private */
    public Drawable statusBarForeground;
    private int[] tmpStatesArray;
    private int totalScrollRange;

    protected static class BaseBehavior<T extends AppBarLayout> extends HeaderBehavior<T> {
        private static final int MAX_OFFSET_ANIMATION_DURATION = 600;
        /* access modifiers changed from: private */
        public boolean coordinatorLayoutA11yScrollable;
        private WeakReference<View> lastNestedScrollingChildRef;
        private int lastStartedType;
        private ValueAnimator offsetAnimator;
        /* access modifiers changed from: private */
        public int offsetDelta;
        private BaseDragCallback onDragCallback;
        private SavedState savedState;

        public static abstract class BaseDragCallback<T extends AppBarLayout> {
            public abstract boolean canDrag(T t);
        }

        protected static class SavedState extends AbsSavedState {
            public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
                public SavedState createFromParcel(Parcel source) {
                    return new SavedState(source, (ClassLoader) null);
                }

                public SavedState createFromParcel(Parcel source, ClassLoader loader) {
                    return new SavedState(source, loader);
                }

                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
            boolean firstVisibleChildAtMinimumHeight;
            int firstVisibleChildIndex;
            float firstVisibleChildPercentageShown;
            boolean fullyExpanded;
            boolean fullyScrolled;

            public SavedState(Parcel source, ClassLoader loader) {
                super(source, loader);
                boolean z = true;
                this.fullyScrolled = source.readByte() != 0;
                this.fullyExpanded = source.readByte() != 0;
                this.firstVisibleChildIndex = source.readInt();
                this.firstVisibleChildPercentageShown = source.readFloat();
                this.firstVisibleChildAtMinimumHeight = source.readByte() == 0 ? false : z;
            }

            public SavedState(Parcelable superState) {
                super(superState);
            }

            public void writeToParcel(Parcel dest, int flags) {
                super.writeToParcel(dest, flags);
                dest.writeByte(this.fullyScrolled ? (byte) 1 : 0);
                dest.writeByte(this.fullyExpanded ? (byte) 1 : 0);
                dest.writeInt(this.firstVisibleChildIndex);
                dest.writeFloat(this.firstVisibleChildPercentageShown);
                dest.writeByte(this.firstVisibleChildAtMinimumHeight ? (byte) 1 : 0);
            }
        }

        public BaseBehavior() {
        }

        public BaseBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        private boolean addAccessibilityScrollActions(CoordinatorLayout coordinatorLayout, T t, View scrollingView) {
            boolean z = false;
            if (getTopBottomOffsetForScrollingSibling() != (-t.getTotalScrollRange())) {
                addActionToExpand(coordinatorLayout, t, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_FORWARD, false);
                z = true;
            }
            if (getTopBottomOffsetForScrollingSibling() == 0) {
                return z;
            }
            if (scrollingView.canScrollVertically(-1)) {
                int i = -t.getDownNestedPreScrollRange();
                if (i == 0) {
                    return z;
                }
                final CoordinatorLayout coordinatorLayout2 = coordinatorLayout;
                final T t2 = t;
                final View view = scrollingView;
                final int i2 = i;
                ViewCompat.replaceAccessibilityAction(coordinatorLayout, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD, (CharSequence) null, new AccessibilityViewCommand() {
                    public boolean perform(View view, AccessibilityViewCommand.CommandArguments arguments) {
                        BaseBehavior.this.onNestedPreScroll(coordinatorLayout2, t2, view, 0, i2, new int[]{0, 0}, 1);
                        return true;
                    }
                });
                return true;
            }
            addActionToExpand(coordinatorLayout, t, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD, true);
            return true;
        }

        private void addActionToExpand(CoordinatorLayout parent, final T t, AccessibilityNodeInfoCompat.AccessibilityActionCompat action, final boolean expand) {
            ViewCompat.replaceAccessibilityAction(parent, action, (CharSequence) null, new AccessibilityViewCommand() {
                public boolean perform(View view, AccessibilityViewCommand.CommandArguments arguments) {
                    t.setExpanded(expand);
                    return true;
                }
            });
        }

        private void animateOffsetTo(CoordinatorLayout coordinatorLayout, T t, int offset, float velocity) {
            int abs = Math.abs(getTopBottomOffsetForScrollingSibling() - offset);
            float velocity2 = Math.abs(velocity);
            animateOffsetWithDuration(coordinatorLayout, t, offset, velocity2 > 0.0f ? Math.round((((float) abs) / velocity2) * 1000.0f) * 3 : (int) ((1.0f + (((float) abs) / ((float) t.getHeight()))) * 150.0f));
        }

        private void animateOffsetWithDuration(final CoordinatorLayout coordinatorLayout, final T t, int offset, int duration) {
            int topBottomOffsetForScrollingSibling = getTopBottomOffsetForScrollingSibling();
            if (topBottomOffsetForScrollingSibling == offset) {
                ValueAnimator valueAnimator = this.offsetAnimator;
                if (valueAnimator != null && valueAnimator.isRunning()) {
                    this.offsetAnimator.cancel();
                    return;
                }
                return;
            }
            ValueAnimator valueAnimator2 = this.offsetAnimator;
            if (valueAnimator2 == null) {
                ValueAnimator valueAnimator3 = new ValueAnimator();
                this.offsetAnimator = valueAnimator3;
                valueAnimator3.setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
                this.offsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animator) {
                        BaseBehavior.this.setHeaderTopBottomOffset(coordinatorLayout, t, ((Integer) animator.getAnimatedValue()).intValue());
                    }
                });
            } else {
                valueAnimator2.cancel();
            }
            this.offsetAnimator.setDuration((long) Math.min(duration, 600));
            this.offsetAnimator.setIntValues(new int[]{topBottomOffsetForScrollingSibling, offset});
            this.offsetAnimator.start();
        }

        private int calculateSnapOffset(int value, int bottom, int top) {
            return value < (bottom + top) / 2 ? bottom : top;
        }

        private boolean canScrollChildren(CoordinatorLayout parent, T t, View directTargetChild) {
            return t.hasScrollableChildren() && parent.getHeight() - directTargetChild.getHeight() <= t.getHeight();
        }

        private static boolean checkFlag(int flags, int check) {
            return (flags & check) == check;
        }

        private boolean childrenHaveScrollFlags(AppBarLayout appBarLayout) {
            int childCount = appBarLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (((LayoutParams) appBarLayout.getChildAt(i).getLayoutParams()).scrollFlags != 0) {
                    return true;
                }
            }
            return false;
        }

        private View findFirstScrollingChild(CoordinatorLayout parent) {
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = parent.getChildAt(i);
                if ((childAt instanceof NestedScrollingChild) || (childAt instanceof ListView) || (childAt instanceof ScrollView)) {
                    return childAt;
                }
            }
            return null;
        }

        private static View getAppBarChildOnOffset(AppBarLayout layout, int offset) {
            int abs = Math.abs(offset);
            int childCount = layout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = layout.getChildAt(i);
                if (abs >= childAt.getTop() && abs <= childAt.getBottom()) {
                    return childAt;
                }
            }
            return null;
        }

        private int getChildIndexOnOffset(T t, int offset) {
            int childCount = t.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = t.getChildAt(i);
                int top = childAt.getTop();
                int bottom = childAt.getBottom();
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (checkFlag(layoutParams.getScrollFlags(), 32)) {
                    top -= layoutParams.topMargin;
                    bottom += layoutParams.bottomMargin;
                }
                if (top <= (-offset) && bottom >= (-offset)) {
                    return i;
                }
            }
            return -1;
        }

        private View getChildWithScrollingBehavior(CoordinatorLayout coordinatorLayout) {
            int childCount = coordinatorLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = coordinatorLayout.getChildAt(i);
                if (((CoordinatorLayout.LayoutParams) childAt.getLayoutParams()).getBehavior() instanceof ScrollingViewBehavior) {
                    return childAt;
                }
            }
            return null;
        }

        private int interpolateOffset(T t, int offset) {
            int abs = Math.abs(offset);
            int i = 0;
            int childCount = t.getChildCount();
            while (true) {
                if (i >= childCount) {
                    break;
                }
                View childAt = t.getChildAt(i);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                Interpolator scrollInterpolator = layoutParams.getScrollInterpolator();
                if (abs < childAt.getTop() || abs > childAt.getBottom()) {
                    i++;
                } else if (scrollInterpolator != null) {
                    int i2 = 0;
                    int scrollFlags = layoutParams.getScrollFlags();
                    if ((scrollFlags & 1) != 0) {
                        i2 = 0 + childAt.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
                        if ((scrollFlags & 2) != 0) {
                            i2 -= ViewCompat.getMinimumHeight(childAt);
                        }
                    }
                    if (ViewCompat.getFitsSystemWindows(childAt)) {
                        i2 -= t.getTopInset();
                    }
                    if (i2 > 0) {
                        return Integer.signum(offset) * (childAt.getTop() + Math.round(((float) i2) * scrollInterpolator.getInterpolation(((float) (abs - childAt.getTop())) / ((float) i2))));
                    }
                }
            }
            return offset;
        }

        private boolean shouldJumpElevationState(CoordinatorLayout parent, T t) {
            List<View> dependents = parent.getDependents(t);
            int size = dependents.size();
            for (int i = 0; i < size; i++) {
                CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) dependents.get(i).getLayoutParams()).getBehavior();
                if (behavior instanceof ScrollingViewBehavior) {
                    return ((ScrollingViewBehavior) behavior).getOverlayTop() != 0;
                }
            }
            return false;
        }

        private void snapToChildIfNeeded(CoordinatorLayout coordinatorLayout, T t) {
            int topInset = t.getTopInset() + t.getPaddingTop();
            int topBottomOffsetForScrollingSibling = getTopBottomOffsetForScrollingSibling() - topInset;
            int childIndexOnOffset = getChildIndexOnOffset(t, topBottomOffsetForScrollingSibling);
            if (childIndexOnOffset >= 0) {
                View childAt = t.getChildAt(childIndexOnOffset);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int scrollFlags = layoutParams.getScrollFlags();
                if ((scrollFlags & 17) == 17) {
                    int i = -childAt.getTop();
                    int i2 = -childAt.getBottom();
                    if (childIndexOnOffset == 0 && ViewCompat.getFitsSystemWindows(t) && ViewCompat.getFitsSystemWindows(childAt)) {
                        i -= t.getTopInset();
                    }
                    if (checkFlag(scrollFlags, 2)) {
                        i2 += ViewCompat.getMinimumHeight(childAt);
                    } else if (checkFlag(scrollFlags, 5)) {
                        int minimumHeight = ViewCompat.getMinimumHeight(childAt) + i2;
                        if (topBottomOffsetForScrollingSibling < minimumHeight) {
                            i = minimumHeight;
                        } else {
                            i2 = minimumHeight;
                        }
                    }
                    if (checkFlag(scrollFlags, 32)) {
                        i += layoutParams.topMargin;
                        i2 -= layoutParams.bottomMargin;
                    }
                    animateOffsetTo(coordinatorLayout, t, MathUtils.clamp(calculateSnapOffset(topBottomOffsetForScrollingSibling, i2, i) + topInset, -t.getTotalScrollRange(), 0), 0.0f);
                }
            }
        }

        private void updateAccessibilityActions(CoordinatorLayout coordinatorLayout, T t) {
            View childWithScrollingBehavior;
            ViewCompat.removeAccessibilityAction(coordinatorLayout, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_FORWARD.getId());
            ViewCompat.removeAccessibilityAction(coordinatorLayout, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD.getId());
            if (t.getTotalScrollRange() != 0 && (childWithScrollingBehavior = getChildWithScrollingBehavior(coordinatorLayout)) != null && childrenHaveScrollFlags(t)) {
                if (!ViewCompat.hasAccessibilityDelegate(coordinatorLayout)) {
                    ViewCompat.setAccessibilityDelegate(coordinatorLayout, new AccessibilityDelegateCompat() {
                        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
                            super.onInitializeAccessibilityNodeInfo(host, info);
                            info.setScrollable(BaseBehavior.this.coordinatorLayoutA11yScrollable);
                            info.setClassName(ScrollView.class.getName());
                        }
                    });
                }
                this.coordinatorLayoutA11yScrollable = addAccessibilityScrollActions(coordinatorLayout, t, childWithScrollingBehavior);
            }
        }

        private void updateAppBarLayoutDrawableState(CoordinatorLayout parent, T t, int offset, int direction, boolean forceJump) {
            View appBarChildOnOffset = getAppBarChildOnOffset(t, offset);
            boolean z = false;
            if (appBarChildOnOffset != null) {
                int scrollFlags = ((LayoutParams) appBarChildOnOffset.getLayoutParams()).getScrollFlags();
                if ((scrollFlags & 1) != 0) {
                    int minimumHeight = ViewCompat.getMinimumHeight(appBarChildOnOffset);
                    boolean z2 = false;
                    if (direction > 0 && (scrollFlags & 12) != 0) {
                        if ((-offset) >= (appBarChildOnOffset.getBottom() - minimumHeight) - t.getTopInset()) {
                            z2 = true;
                        }
                        z = z2;
                    } else if ((scrollFlags & 2) != 0) {
                        if ((-offset) >= (appBarChildOnOffset.getBottom() - minimumHeight) - t.getTopInset()) {
                            z2 = true;
                        }
                        z = z2;
                    }
                }
            }
            if (t.isLiftOnScroll()) {
                z = t.shouldLift(findFirstScrollingChild(parent));
            }
            boolean liftedState = t.setLiftedState(z);
            if (forceJump || (liftedState && shouldJumpElevationState(parent, t))) {
                t.jumpDrawablesToCurrentState();
            }
        }

        /* access modifiers changed from: package-private */
        public boolean canDragView(T t) {
            BaseDragCallback baseDragCallback = this.onDragCallback;
            if (baseDragCallback != null) {
                return baseDragCallback.canDrag(t);
            }
            WeakReference<View> weakReference = this.lastNestedScrollingChildRef;
            if (weakReference == null) {
                return true;
            }
            View view = (View) weakReference.get();
            return view != null && view.isShown() && !view.canScrollVertically(-1);
        }

        /* access modifiers changed from: package-private */
        public int getMaxDragOffset(T t) {
            return -t.getDownNestedScrollRange();
        }

        /* access modifiers changed from: package-private */
        public int getScrollRangeForDragFling(T t) {
            return t.getTotalScrollRange();
        }

        /* access modifiers changed from: package-private */
        public int getTopBottomOffsetForScrollingSibling() {
            return getTopAndBottomOffset() + this.offsetDelta;
        }

        /* access modifiers changed from: package-private */
        public boolean isOffsetAnimatorRunning() {
            ValueAnimator valueAnimator = this.offsetAnimator;
            return valueAnimator != null && valueAnimator.isRunning();
        }

        /* access modifiers changed from: package-private */
        public void onFlingFinished(CoordinatorLayout parent, T t) {
            snapToChildIfNeeded(parent, t);
            if (t.isLiftOnScroll()) {
                t.setLiftedState(t.shouldLift(findFirstScrollingChild(parent)));
            }
        }

        public boolean onLayoutChild(CoordinatorLayout parent, T t, int layoutDirection) {
            boolean onLayoutChild = super.onLayoutChild(parent, t, layoutDirection);
            int pendingAction = t.getPendingAction();
            SavedState savedState2 = this.savedState;
            if (savedState2 == null || (pendingAction & 8) != 0) {
                if (pendingAction != 0) {
                    boolean z = (pendingAction & 4) != 0;
                    if ((pendingAction & 2) != 0) {
                        int i = -t.getUpNestedPreScrollRange();
                        if (z) {
                            animateOffsetTo(parent, t, i, 0.0f);
                        } else {
                            setHeaderTopBottomOffset(parent, t, i);
                        }
                    } else if ((pendingAction & 1) != 0) {
                        if (z) {
                            animateOffsetTo(parent, t, 0, 0.0f);
                        } else {
                            setHeaderTopBottomOffset(parent, t, 0);
                        }
                    }
                }
            } else if (savedState2.fullyScrolled) {
                setHeaderTopBottomOffset(parent, t, -t.getTotalScrollRange());
            } else if (this.savedState.fullyExpanded) {
                setHeaderTopBottomOffset(parent, t, 0);
            } else {
                View childAt = t.getChildAt(this.savedState.firstVisibleChildIndex);
                int i2 = -childAt.getBottom();
                setHeaderTopBottomOffset(parent, t, this.savedState.firstVisibleChildAtMinimumHeight ? i2 + ViewCompat.getMinimumHeight(childAt) + t.getTopInset() : i2 + Math.round(((float) childAt.getHeight()) * this.savedState.firstVisibleChildPercentageShown));
            }
            t.resetPendingAction();
            this.savedState = null;
            setTopAndBottomOffset(MathUtils.clamp(getTopAndBottomOffset(), -t.getTotalScrollRange(), 0));
            updateAppBarLayoutDrawableState(parent, t, getTopAndBottomOffset(), 0, true);
            t.onOffsetChanged(getTopAndBottomOffset());
            updateAccessibilityActions(parent, t);
            return onLayoutChild;
        }

        public boolean onMeasureChild(CoordinatorLayout parent, T t, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
            if (((CoordinatorLayout.LayoutParams) t.getLayoutParams()).height != -2) {
                return super.onMeasureChild(parent, t, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
            }
            parent.onMeasureChild(t, parentWidthMeasureSpec, widthUsed, View.MeasureSpec.makeMeasureSpec(0, 0), heightUsed);
            return true;
        }

        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, T t, View target, int dx, int dy, int[] consumed, int type) {
            int i;
            int i2;
            if (dy != 0) {
                if (dy < 0) {
                    int i3 = -t.getTotalScrollRange();
                    i2 = i3;
                    i = t.getDownNestedPreScrollRange() + i3;
                } else {
                    i2 = -t.getUpNestedPreScrollRange();
                    i = 0;
                }
                if (i2 != i) {
                    consumed[1] = scroll(coordinatorLayout, t, dy, i2, i);
                }
            }
            if (t.isLiftOnScroll()) {
                T t2 = t;
                t.setLiftedState(t.shouldLift(target));
                return;
            }
            T t3 = t;
        }

        public void onNestedScroll(CoordinatorLayout coordinatorLayout, T t, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int[] consumed) {
            if (dyUnconsumed < 0) {
                consumed[1] = scroll(coordinatorLayout, t, dyUnconsumed, -t.getDownNestedScrollRange(), 0);
            }
            if (dyUnconsumed == 0) {
                updateAccessibilityActions(coordinatorLayout, t);
            }
        }

        public void onRestoreInstanceState(CoordinatorLayout parent, T t, Parcelable state) {
            if (state instanceof SavedState) {
                restoreScrollState((SavedState) state, true);
                super.onRestoreInstanceState(parent, t, this.savedState.getSuperState());
                return;
            }
            super.onRestoreInstanceState(parent, t, state);
            this.savedState = null;
        }

        public Parcelable onSaveInstanceState(CoordinatorLayout parent, T t) {
            Parcelable onSaveInstanceState = super.onSaveInstanceState(parent, t);
            SavedState saveScrollState = saveScrollState(onSaveInstanceState, t);
            return saveScrollState == null ? onSaveInstanceState : saveScrollState;
        }

        public boolean onStartNestedScroll(CoordinatorLayout parent, T t, View directTargetChild, View target, int nestedScrollAxes, int type) {
            ValueAnimator valueAnimator;
            boolean z = (nestedScrollAxes & 2) != 0 && (t.isLiftOnScroll() || canScrollChildren(parent, t, directTargetChild));
            if (z && (valueAnimator = this.offsetAnimator) != null) {
                valueAnimator.cancel();
            }
            this.lastNestedScrollingChildRef = null;
            this.lastStartedType = type;
            return z;
        }

        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, T t, View target, int type) {
            if (this.lastStartedType == 0 || type == 1) {
                snapToChildIfNeeded(coordinatorLayout, t);
                if (t.isLiftOnScroll()) {
                    t.setLiftedState(t.shouldLift(target));
                }
            }
            this.lastNestedScrollingChildRef = new WeakReference<>(target);
        }

        /* access modifiers changed from: package-private */
        public void restoreScrollState(SavedState state, boolean force) {
            if (this.savedState == null || force) {
                this.savedState = state;
            }
        }

        /* access modifiers changed from: package-private */
        public SavedState saveScrollState(Parcelable superState, T t) {
            int topAndBottomOffset = getTopAndBottomOffset();
            int i = 0;
            int childCount = t.getChildCount();
            while (i < childCount) {
                View childAt = t.getChildAt(i);
                int bottom = childAt.getBottom() + topAndBottomOffset;
                if (childAt.getTop() + topAndBottomOffset > 0 || bottom < 0) {
                    i++;
                } else {
                    SavedState savedState2 = new SavedState(superState == null ? AbsSavedState.EMPTY_STATE : superState);
                    boolean z = false;
                    savedState2.fullyExpanded = topAndBottomOffset == 0;
                    savedState2.fullyScrolled = !savedState2.fullyExpanded && (-topAndBottomOffset) >= t.getTotalScrollRange();
                    savedState2.firstVisibleChildIndex = i;
                    if (bottom == ViewCompat.getMinimumHeight(childAt) + t.getTopInset()) {
                        z = true;
                    }
                    savedState2.firstVisibleChildAtMinimumHeight = z;
                    savedState2.firstVisibleChildPercentageShown = ((float) bottom) / ((float) childAt.getHeight());
                    return savedState2;
                }
            }
            return null;
        }

        public void setDragCallback(BaseDragCallback callback) {
            this.onDragCallback = callback;
        }

        /* access modifiers changed from: package-private */
        public int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, T t, int newOffset, int minOffset, int maxOffset) {
            int topBottomOffsetForScrollingSibling = getTopBottomOffsetForScrollingSibling();
            int i = 0;
            if (minOffset == 0 || topBottomOffsetForScrollingSibling < minOffset || topBottomOffsetForScrollingSibling > maxOffset) {
                this.offsetDelta = 0;
            } else {
                int newOffset2 = MathUtils.clamp(newOffset, minOffset, maxOffset);
                if (topBottomOffsetForScrollingSibling != newOffset2) {
                    int interpolateOffset = t.hasChildWithInterpolator() ? interpolateOffset(t, newOffset2) : newOffset2;
                    boolean topAndBottomOffset = setTopAndBottomOffset(interpolateOffset);
                    i = topBottomOffsetForScrollingSibling - newOffset2;
                    this.offsetDelta = newOffset2 - interpolateOffset;
                    int i2 = 1;
                    if (topAndBottomOffset) {
                        for (int i3 = 0; i3 < t.getChildCount(); i3++) {
                            LayoutParams layoutParams = (LayoutParams) t.getChildAt(i3).getLayoutParams();
                            ChildScrollEffect scrollEffect = layoutParams.getScrollEffect();
                            if (!(scrollEffect == null || (layoutParams.getScrollFlags() & 1) == 0)) {
                                scrollEffect.onOffsetChanged(t, t.getChildAt(i3), (float) getTopAndBottomOffset());
                            }
                        }
                    }
                    if (!topAndBottomOffset && t.hasChildWithInterpolator()) {
                        coordinatorLayout.dispatchDependentViewsChanged(t);
                    }
                    t.onOffsetChanged(getTopAndBottomOffset());
                    if (newOffset2 < topBottomOffsetForScrollingSibling) {
                        i2 = -1;
                    }
                    updateAppBarLayoutDrawableState(coordinatorLayout, t, newOffset2, i2, false);
                }
            }
            updateAccessibilityActions(coordinatorLayout, t);
            return i;
        }
    }

    public interface BaseOnOffsetChangedListener<T extends AppBarLayout> {
        void onOffsetChanged(T t, int i);
    }

    public static class Behavior extends BaseBehavior<AppBarLayout> {

        public static abstract class DragCallback extends BaseBehavior.BaseDragCallback<AppBarLayout> {
        }

        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public /* bridge */ /* synthetic */ int getLeftAndRightOffset() {
            return super.getLeftAndRightOffset();
        }

        public /* bridge */ /* synthetic */ int getTopAndBottomOffset() {
            return super.getTopAndBottomOffset();
        }

        public /* bridge */ /* synthetic */ boolean isHorizontalOffsetEnabled() {
            return super.isHorizontalOffsetEnabled();
        }

        public /* bridge */ /* synthetic */ boolean isVerticalOffsetEnabled() {
            return super.isVerticalOffsetEnabled();
        }

        public /* bridge */ /* synthetic */ boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
            return super.onInterceptTouchEvent(coordinatorLayout, view, motionEvent);
        }

        public /* bridge */ /* synthetic */ boolean onLayoutChild(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, int i) {
            return super.onLayoutChild(coordinatorLayout, appBarLayout, i);
        }

        public /* bridge */ /* synthetic */ boolean onMeasureChild(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, int i, int i2, int i3, int i4) {
            return super.onMeasureChild(coordinatorLayout, appBarLayout, i, i2, i3, i4);
        }

        public /* bridge */ /* synthetic */ void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int i, int i2, int[] iArr, int i3) {
            super.onNestedPreScroll(coordinatorLayout, appBarLayout, view, i, i2, iArr, i3);
        }

        public /* bridge */ /* synthetic */ void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int i, int i2, int i3, int i4, int i5, int[] iArr) {
            super.onNestedScroll(coordinatorLayout, appBarLayout, view, i, i2, i3, i4, i5, iArr);
        }

        public /* bridge */ /* synthetic */ void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, Parcelable parcelable) {
            super.onRestoreInstanceState(coordinatorLayout, appBarLayout, parcelable);
        }

        public /* bridge */ /* synthetic */ Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout) {
            return super.onSaveInstanceState(coordinatorLayout, appBarLayout);
        }

        public /* bridge */ /* synthetic */ boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, View view2, int i, int i2) {
            return super.onStartNestedScroll(coordinatorLayout, appBarLayout, view, view2, i, i2);
        }

        public /* bridge */ /* synthetic */ void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int i) {
            super.onStopNestedScroll(coordinatorLayout, appBarLayout, view, i);
        }

        public /* bridge */ /* synthetic */ boolean onTouchEvent(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
            return super.onTouchEvent(coordinatorLayout, view, motionEvent);
        }

        public /* bridge */ /* synthetic */ void setDragCallback(BaseBehavior.BaseDragCallback baseDragCallback) {
            super.setDragCallback(baseDragCallback);
        }

        public /* bridge */ /* synthetic */ void setHorizontalOffsetEnabled(boolean z) {
            super.setHorizontalOffsetEnabled(z);
        }

        public /* bridge */ /* synthetic */ boolean setLeftAndRightOffset(int i) {
            return super.setLeftAndRightOffset(i);
        }

        public /* bridge */ /* synthetic */ boolean setTopAndBottomOffset(int i) {
            return super.setTopAndBottomOffset(i);
        }

        public /* bridge */ /* synthetic */ void setVerticalOffsetEnabled(boolean z) {
            super.setVerticalOffsetEnabled(z);
        }
    }

    public static abstract class ChildScrollEffect {
        public abstract void onOffsetChanged(AppBarLayout appBarLayout, View view, float f);
    }

    public static class CompressChildScrollEffect extends ChildScrollEffect {
        private static final float COMPRESS_DISTANCE_FACTOR = 0.3f;
        private final Rect ghostRect = new Rect();
        private final Rect relativeRect = new Rect();

        private static void updateRelativeRect(Rect rect, AppBarLayout appBarLayout, View child) {
            child.getDrawingRect(rect);
            appBarLayout.offsetDescendantRectToMyCoords(child, rect);
            rect.offset(0, -appBarLayout.getTopInset());
        }

        public void onOffsetChanged(AppBarLayout appBarLayout, View child, float offset) {
            updateRelativeRect(this.relativeRect, appBarLayout, child);
            float abs = ((float) this.relativeRect.top) - Math.abs(offset);
            if (abs <= 0.0f) {
                float clamp = MathUtils.clamp(Math.abs(abs / ((float) this.relativeRect.height())), 0.0f, 1.0f);
                float height = (-abs) - ((((float) this.relativeRect.height()) * COMPRESS_DISTANCE_FACTOR) * (1.0f - ((1.0f - clamp) * (1.0f - clamp))));
                child.setTranslationY(height);
                child.getDrawingRect(this.ghostRect);
                this.ghostRect.offset(0, (int) (-height));
                ViewCompat.setClipBounds(child, this.ghostRect);
                return;
            }
            ViewCompat.setClipBounds(child, (Rect) null);
            child.setTranslationY(0.0f);
        }
    }

    public static class LayoutParams extends LinearLayout.LayoutParams {
        static final int COLLAPSIBLE_FLAGS = 10;
        static final int FLAG_QUICK_RETURN = 5;
        static final int FLAG_SNAP = 17;
        private static final int SCROLL_EFFECT_COMPRESS = 1;
        private static final int SCROLL_EFFECT_NONE = 0;
        public static final int SCROLL_FLAG_ENTER_ALWAYS = 4;
        public static final int SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED = 8;
        public static final int SCROLL_FLAG_EXIT_UNTIL_COLLAPSED = 2;
        public static final int SCROLL_FLAG_NO_SCROLL = 0;
        public static final int SCROLL_FLAG_SCROLL = 1;
        public static final int SCROLL_FLAG_SNAP = 16;
        public static final int SCROLL_FLAG_SNAP_MARGINS = 32;
        private ChildScrollEffect scrollEffect;
        int scrollFlags = 1;
        Interpolator scrollInterpolator;

        @Retention(RetentionPolicy.SOURCE)
        public @interface ScrollFlags {
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, float weight) {
            super(width, height, weight);
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray obtainStyledAttributes = c.obtainStyledAttributes(attrs, R.styleable.AppBarLayout_Layout);
            this.scrollFlags = obtainStyledAttributes.getInt(R.styleable.AppBarLayout_Layout_layout_scrollFlags, 0);
            setScrollEffect(createScrollEffectFromInt(obtainStyledAttributes.getInt(R.styleable.AppBarLayout_Layout_layout_scrollEffect, 0)));
            if (obtainStyledAttributes.hasValue(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator)) {
                this.scrollInterpolator = android.view.animation.AnimationUtils.loadInterpolator(c, obtainStyledAttributes.getResourceId(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator, 0));
            }
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(LinearLayout.LayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
            this.scrollFlags = source.scrollFlags;
            this.scrollInterpolator = source.scrollInterpolator;
        }

        private ChildScrollEffect createScrollEffectFromInt(int scrollEffectInt) {
            switch (scrollEffectInt) {
                case 1:
                    return new CompressChildScrollEffect();
                default:
                    return null;
            }
        }

        public ChildScrollEffect getScrollEffect() {
            return this.scrollEffect;
        }

        public int getScrollFlags() {
            return this.scrollFlags;
        }

        public Interpolator getScrollInterpolator() {
            return this.scrollInterpolator;
        }

        /* access modifiers changed from: package-private */
        public boolean isCollapsible() {
            int i = this.scrollFlags;
            return (i & 1) == 1 && (i & 10) != 0;
        }

        public void setScrollEffect(ChildScrollEffect scrollEffect2) {
            this.scrollEffect = scrollEffect2;
        }

        public void setScrollFlags(int flags) {
            this.scrollFlags = flags;
        }

        public void setScrollInterpolator(Interpolator interpolator) {
            this.scrollInterpolator = interpolator;
        }
    }

    public interface LiftOnScrollListener {
        void onUpdate(float f, int i);
    }

    public interface OnOffsetChangedListener extends BaseOnOffsetChangedListener<AppBarLayout> {
        void onOffsetChanged(AppBarLayout appBarLayout, int i);
    }

    public static class ScrollingViewBehavior extends HeaderScrollingViewBehavior {
        public ScrollingViewBehavior() {
        }

        public ScrollingViewBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ScrollingViewBehavior_Layout);
            setOverlayTop(obtainStyledAttributes.getDimensionPixelSize(R.styleable.ScrollingViewBehavior_Layout_behavior_overlapTop, 0));
            obtainStyledAttributes.recycle();
        }

        private static int getAppBarLayoutOffset(AppBarLayout abl) {
            CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) abl.getLayoutParams()).getBehavior();
            if (behavior instanceof BaseBehavior) {
                return ((BaseBehavior) behavior).getTopBottomOffsetForScrollingSibling();
            }
            return 0;
        }

        private void offsetChildAsNeeded(View child, View dependency) {
            CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams()).getBehavior();
            if (behavior instanceof BaseBehavior) {
                ViewCompat.offsetTopAndBottom(child, (((dependency.getBottom() - child.getTop()) + ((BaseBehavior) behavior).offsetDelta) + getVerticalLayoutGap()) - getOverlapPixelsForOffset(dependency));
            }
        }

        private void updateLiftedStateIfNeeded(View child, View dependency) {
            if (dependency instanceof AppBarLayout) {
                AppBarLayout appBarLayout = (AppBarLayout) dependency;
                if (appBarLayout.isLiftOnScroll()) {
                    appBarLayout.setLiftedState(appBarLayout.shouldLift(child));
                }
            }
        }

        /* access modifiers changed from: package-private */
        public AppBarLayout findFirstDependency(List<View> list) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                View view = list.get(i);
                if (view instanceof AppBarLayout) {
                    return (AppBarLayout) view;
                }
            }
            return null;
        }

        public /* bridge */ /* synthetic */ int getLeftAndRightOffset() {
            return super.getLeftAndRightOffset();
        }

        /* access modifiers changed from: package-private */
        public float getOverlapRatioForOffset(View header) {
            int i;
            if (header instanceof AppBarLayout) {
                AppBarLayout appBarLayout = (AppBarLayout) header;
                int totalScrollRange = appBarLayout.getTotalScrollRange();
                int downNestedPreScrollRange = appBarLayout.getDownNestedPreScrollRange();
                int appBarLayoutOffset = getAppBarLayoutOffset(appBarLayout);
                if ((downNestedPreScrollRange == 0 || totalScrollRange + appBarLayoutOffset > downNestedPreScrollRange) && (i = totalScrollRange - downNestedPreScrollRange) != 0) {
                    return (((float) appBarLayoutOffset) / ((float) i)) + 1.0f;
                }
            }
            return 0.0f;
        }

        /* access modifiers changed from: package-private */
        public int getScrollRange(View v) {
            return v instanceof AppBarLayout ? ((AppBarLayout) v).getTotalScrollRange() : super.getScrollRange(v);
        }

        public /* bridge */ /* synthetic */ int getTopAndBottomOffset() {
            return super.getTopAndBottomOffset();
        }

        public /* bridge */ /* synthetic */ boolean isHorizontalOffsetEnabled() {
            return super.isHorizontalOffsetEnabled();
        }

        public /* bridge */ /* synthetic */ boolean isVerticalOffsetEnabled() {
            return super.isVerticalOffsetEnabled();
        }

        public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
            return dependency instanceof AppBarLayout;
        }

        public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
            offsetChildAsNeeded(child, dependency);
            updateLiftedStateIfNeeded(child, dependency);
            return false;
        }

        public void onDependentViewRemoved(CoordinatorLayout parent, View child, View dependency) {
            if (dependency instanceof AppBarLayout) {
                ViewCompat.removeAccessibilityAction(parent, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_FORWARD.getId());
                ViewCompat.removeAccessibilityAction(parent, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_BACKWARD.getId());
                ViewCompat.setAccessibilityDelegate(parent, (AccessibilityDelegateCompat) null);
            }
        }

        public /* bridge */ /* synthetic */ boolean onLayoutChild(CoordinatorLayout coordinatorLayout, View view, int i) {
            return super.onLayoutChild(coordinatorLayout, view, i);
        }

        public /* bridge */ /* synthetic */ boolean onMeasureChild(CoordinatorLayout coordinatorLayout, View view, int i, int i2, int i3, int i4) {
            return super.onMeasureChild(coordinatorLayout, view, i, i2, i3, i4);
        }

        public boolean onRequestChildRectangleOnScreen(CoordinatorLayout parent, View child, Rect rectangle, boolean immediate) {
            AppBarLayout findFirstDependency = findFirstDependency((List) parent.getDependencies(child));
            if (findFirstDependency != null) {
                rectangle.offset(child.getLeft(), child.getTop());
                Rect rect = this.tempRect1;
                rect.set(0, 0, parent.getWidth(), parent.getHeight());
                if (!rect.contains(rectangle)) {
                    findFirstDependency.setExpanded(false, !immediate);
                    return true;
                }
            }
            return false;
        }

        public /* bridge */ /* synthetic */ void setHorizontalOffsetEnabled(boolean z) {
            super.setHorizontalOffsetEnabled(z);
        }

        public /* bridge */ /* synthetic */ boolean setLeftAndRightOffset(int i) {
            return super.setLeftAndRightOffset(i);
        }

        public /* bridge */ /* synthetic */ boolean setTopAndBottomOffset(int i) {
            return super.setTopAndBottomOffset(i);
        }

        public /* bridge */ /* synthetic */ void setVerticalOffsetEnabled(boolean z) {
            super.setVerticalOffsetEnabled(z);
        }
    }

    public AppBarLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public AppBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.appBarLayoutStyle);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public AppBarLayout(android.content.Context r10, android.util.AttributeSet r11, int r12) {
        /*
            r9 = this;
            int r4 = DEF_STYLE_RES
            android.content.Context r0 = com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap(r10, r11, r12, r4)
            r9.<init>(r0, r11, r12)
            r6 = -1
            r9.totalScrollRange = r6
            r9.downPreScrollRange = r6
            r9.downScrollRange = r6
            r7 = 0
            r9.pendingAction = r7
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r9.liftOnScrollListeners = r0
            android.content.Context r10 = r9.getContext()
            r0 = 1
            r9.setOrientation(r0)
            int r0 = android.os.Build.VERSION.SDK_INT
            r8 = 21
            if (r0 < r8) goto L_0x0036
            android.view.ViewOutlineProvider r0 = r9.getOutlineProvider()
            android.view.ViewOutlineProvider r1 = android.view.ViewOutlineProvider.BACKGROUND
            if (r0 != r1) goto L_0x0033
            com.google.android.material.appbar.ViewUtilsLollipop.setBoundsViewOutlineProvider(r9)
        L_0x0033:
            com.google.android.material.appbar.ViewUtilsLollipop.setStateListAnimatorFromAttrs(r9, r11, r12, r4)
        L_0x0036:
            int[] r2 = com.google.android.material.R.styleable.AppBarLayout
            int[] r5 = new int[r7]
            r0 = r10
            r1 = r11
            r3 = r12
            android.content.res.TypedArray r0 = com.google.android.material.internal.ThemeEnforcement.obtainStyledAttributes(r0, r1, r2, r3, r4, r5)
            int r1 = com.google.android.material.R.styleable.AppBarLayout_android_background
            android.graphics.drawable.Drawable r1 = r0.getDrawable(r1)
            androidx.core.view.ViewCompat.setBackground(r9, r1)
            android.graphics.drawable.Drawable r1 = r9.getBackground()
            boolean r1 = r1 instanceof android.graphics.drawable.ColorDrawable
            if (r1 == 0) goto L_0x006e
            android.graphics.drawable.Drawable r1 = r9.getBackground()
            android.graphics.drawable.ColorDrawable r1 = (android.graphics.drawable.ColorDrawable) r1
            com.google.android.material.shape.MaterialShapeDrawable r2 = new com.google.android.material.shape.MaterialShapeDrawable
            r2.<init>()
            int r3 = r1.getColor()
            android.content.res.ColorStateList r3 = android.content.res.ColorStateList.valueOf(r3)
            r2.setFillColor(r3)
            r2.initializeElevationOverlay(r10)
            androidx.core.view.ViewCompat.setBackground(r9, r2)
        L_0x006e:
            int r1 = com.google.android.material.R.styleable.AppBarLayout_expanded
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x007f
            int r1 = com.google.android.material.R.styleable.AppBarLayout_expanded
            boolean r1 = r0.getBoolean(r1, r7)
            r9.setExpanded(r1, r7, r7)
        L_0x007f:
            int r1 = android.os.Build.VERSION.SDK_INT
            if (r1 < r8) goto L_0x0095
            int r1 = com.google.android.material.R.styleable.AppBarLayout_elevation
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x0095
            int r1 = com.google.android.material.R.styleable.AppBarLayout_elevation
            int r1 = r0.getDimensionPixelSize(r1, r7)
            float r1 = (float) r1
            com.google.android.material.appbar.ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator(r9, r1)
        L_0x0095:
            int r1 = android.os.Build.VERSION.SDK_INT
            r2 = 26
            if (r1 < r2) goto L_0x00bd
            int r1 = com.google.android.material.R.styleable.AppBarLayout_android_keyboardNavigationCluster
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x00ac
            int r1 = com.google.android.material.R.styleable.AppBarLayout_android_keyboardNavigationCluster
            boolean r1 = r0.getBoolean(r1, r7)
            r9.setKeyboardNavigationCluster(r1)
        L_0x00ac:
            int r1 = com.google.android.material.R.styleable.AppBarLayout_android_touchscreenBlocksFocus
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x00bd
            int r1 = com.google.android.material.R.styleable.AppBarLayout_android_touchscreenBlocksFocus
            boolean r1 = r0.getBoolean(r1, r7)
            r9.setTouchscreenBlocksFocus(r1)
        L_0x00bd:
            int r1 = com.google.android.material.R.styleable.AppBarLayout_liftOnScroll
            boolean r1 = r0.getBoolean(r1, r7)
            r9.liftOnScroll = r1
            int r1 = com.google.android.material.R.styleable.AppBarLayout_liftOnScrollTargetViewId
            int r1 = r0.getResourceId(r1, r6)
            r9.liftOnScrollTargetViewId = r1
            int r1 = com.google.android.material.R.styleable.AppBarLayout_statusBarForeground
            android.graphics.drawable.Drawable r1 = r0.getDrawable(r1)
            r9.setStatusBarForeground(r1)
            r0.recycle()
            com.google.android.material.appbar.AppBarLayout$1 r1 = new com.google.android.material.appbar.AppBarLayout$1
            r1.<init>()
            androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(r9, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.appbar.AppBarLayout.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private void clearLiftOnScrollTargetView() {
        WeakReference<View> weakReference = this.liftOnScrollTargetView;
        if (weakReference != null) {
            weakReference.clear();
        }
        this.liftOnScrollTargetView = null;
    }

    private View findLiftOnScrollTargetView(View defaultScrollingView) {
        int i;
        if (this.liftOnScrollTargetView == null && (i = this.liftOnScrollTargetViewId) != -1) {
            View view = null;
            if (defaultScrollingView != null) {
                view = defaultScrollingView.findViewById(i);
            }
            if (view == null && (getParent() instanceof ViewGroup)) {
                view = ((ViewGroup) getParent()).findViewById(this.liftOnScrollTargetViewId);
            }
            if (view != null) {
                this.liftOnScrollTargetView = new WeakReference<>(view);
            }
        }
        WeakReference<View> weakReference = this.liftOnScrollTargetView;
        if (weakReference != null) {
            return (View) weakReference.get();
        }
        return null;
    }

    private boolean hasCollapsibleChild() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (((LayoutParams) getChildAt(i).getLayoutParams()).isCollapsible()) {
                return true;
            }
        }
        return false;
    }

    private void invalidateScrollRanges() {
        Behavior behavior2 = this.behavior;
        BaseBehavior.SavedState saveScrollState = (behavior2 == null || this.totalScrollRange == -1 || this.pendingAction != 0) ? null : behavior2.saveScrollState(AbsSavedState.EMPTY_STATE, this);
        this.totalScrollRange = -1;
        this.downPreScrollRange = -1;
        this.downScrollRange = -1;
        if (saveScrollState != null) {
            this.behavior.restoreScrollState(saveScrollState, false);
        }
    }

    private void setExpanded(boolean expanded, boolean animate, boolean force) {
        int i = 0;
        int i2 = (expanded ? 1 : 2) | (animate ? 4 : 0);
        if (force) {
            i = 8;
        }
        this.pendingAction = i2 | i;
        requestLayout();
    }

    private boolean setLiftableState(boolean liftable2) {
        if (this.liftable == liftable2) {
            return false;
        }
        this.liftable = liftable2;
        refreshDrawableState();
        return true;
    }

    private boolean shouldDrawStatusBarForeground() {
        return this.statusBarForeground != null && getTopInset() > 0;
    }

    private boolean shouldOffsetFirstChild() {
        if (getChildCount() <= 0) {
            return false;
        }
        View childAt = getChildAt(0);
        return childAt.getVisibility() != 8 && !ViewCompat.getFitsSystemWindows(childAt);
    }

    private void startLiftOnScrollElevationOverlayAnimation(final MaterialShapeDrawable background, boolean lifted2) {
        float dimension = getResources().getDimension(R.dimen.design_appbar_elevation);
        float f = 0.0f;
        float f2 = lifted2 ? 0.0f : dimension;
        if (lifted2) {
            f = dimension;
        }
        ValueAnimator valueAnimator = this.elevationOverlayAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{f2, f});
        this.elevationOverlayAnimator = ofFloat;
        ofFloat.setDuration((long) getResources().getInteger(R.integer.app_bar_elevation_anim_duration));
        this.elevationOverlayAnimator.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        this.elevationOverlayAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                background.setElevation(floatValue);
                if (AppBarLayout.this.statusBarForeground instanceof MaterialShapeDrawable) {
                    ((MaterialShapeDrawable) AppBarLayout.this.statusBarForeground).setElevation(floatValue);
                }
                for (LiftOnScrollListener onUpdate : AppBarLayout.this.liftOnScrollListeners) {
                    onUpdate.onUpdate(floatValue, background.getResolvedTintColor());
                }
            }
        });
        this.elevationOverlayAnimator.start();
    }

    private void updateWillNotDraw() {
        setWillNotDraw(!shouldDrawStatusBarForeground());
    }

    public void addLiftOnScrollListener(LiftOnScrollListener liftOnScrollListener) {
        this.liftOnScrollListeners.add(liftOnScrollListener);
    }

    public void addOnOffsetChangedListener(BaseOnOffsetChangedListener listener) {
        if (this.listeners == null) {
            this.listeners = new ArrayList();
        }
        if (listener != null && !this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void addOnOffsetChangedListener(OnOffsetChangedListener listener) {
        addOnOffsetChangedListener((BaseOnOffsetChangedListener) listener);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public void clearLiftOnScrollListener() {
        this.liftOnScrollListeners.clear();
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (shouldDrawStatusBarForeground()) {
            int save = canvas.save();
            canvas.translate(0.0f, (float) (-this.currentOffset));
            this.statusBarForeground.draw(canvas);
            canvas.restoreToCount(save);
        }
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        Drawable drawable = this.statusBarForeground;
        if (drawable != null && drawable.isStateful() && drawable.setState(drawableState)) {
            invalidateDrawable(drawable);
        }
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return (Build.VERSION.SDK_INT < 19 || !(p instanceof LinearLayout.LayoutParams)) ? p instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams) p) : new LayoutParams(p) : new LayoutParams((LinearLayout.LayoutParams) p);
    }

    public CoordinatorLayout.Behavior<AppBarLayout> getBehavior() {
        Behavior behavior2 = new Behavior();
        this.behavior = behavior2;
        return behavior2;
    }

    /* access modifiers changed from: package-private */
    public int getDownNestedPreScrollRange() {
        int i = this.downPreScrollRange;
        if (i != -1) {
            return i;
        }
        int i2 = 0;
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = getChildAt(childCount);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            int measuredHeight = childAt.getMeasuredHeight();
            int i3 = layoutParams.scrollFlags;
            if ((i3 & 5) == 5) {
                int i4 = layoutParams.topMargin + layoutParams.bottomMargin;
                int minimumHeight = (i3 & 8) != 0 ? i4 + ViewCompat.getMinimumHeight(childAt) : (i3 & 2) != 0 ? i4 + (measuredHeight - ViewCompat.getMinimumHeight(childAt)) : i4 + measuredHeight;
                if (childCount == 0 && ViewCompat.getFitsSystemWindows(childAt)) {
                    minimumHeight = Math.min(minimumHeight, measuredHeight - getTopInset());
                }
                i2 += minimumHeight;
            } else if (i2 > 0) {
                break;
            }
        }
        int max = Math.max(0, i2);
        this.downPreScrollRange = max;
        return max;
    }

    /* access modifiers changed from: package-private */
    public int getDownNestedScrollRange() {
        int i = this.downScrollRange;
        if (i != -1) {
            return i;
        }
        int i2 = 0;
        int i3 = 0;
        int childCount = getChildCount();
        while (true) {
            if (i3 >= childCount) {
                break;
            }
            View childAt = getChildAt(i3);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            int measuredHeight = childAt.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            int i4 = layoutParams.scrollFlags;
            if ((i4 & 1) == 0) {
                break;
            }
            i2 += measuredHeight;
            if ((i4 & 2) != 0) {
                i2 -= ViewCompat.getMinimumHeight(childAt);
                break;
            }
            i3++;
        }
        int max = Math.max(0, i2);
        this.downScrollRange = max;
        return max;
    }

    public int getLiftOnScrollTargetViewId() {
        return this.liftOnScrollTargetViewId;
    }

    public final int getMinimumHeightForVisibleOverlappingContent() {
        int topInset = getTopInset();
        int minimumHeight = ViewCompat.getMinimumHeight(this);
        if (minimumHeight != 0) {
            return (minimumHeight * 2) + topInset;
        }
        int childCount = getChildCount();
        int minimumHeight2 = childCount >= 1 ? ViewCompat.getMinimumHeight(getChildAt(childCount - 1)) : 0;
        return minimumHeight2 != 0 ? (minimumHeight2 * 2) + topInset : getHeight() / 3;
    }

    /* access modifiers changed from: package-private */
    public int getPendingAction() {
        return this.pendingAction;
    }

    public Drawable getStatusBarForeground() {
        return this.statusBarForeground;
    }

    @Deprecated
    public float getTargetElevation() {
        return 0.0f;
    }

    /* access modifiers changed from: package-private */
    public final int getTopInset() {
        WindowInsetsCompat windowInsetsCompat = this.lastInsets;
        if (windowInsetsCompat != null) {
            return windowInsetsCompat.getSystemWindowInsetTop();
        }
        return 0;
    }

    public final int getTotalScrollRange() {
        int i = this.totalScrollRange;
        if (i != -1) {
            return i;
        }
        int i2 = 0;
        int i3 = 0;
        int childCount = getChildCount();
        while (true) {
            if (i3 >= childCount) {
                break;
            }
            View childAt = getChildAt(i3);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            int measuredHeight = childAt.getMeasuredHeight();
            int i4 = layoutParams.scrollFlags;
            if ((i4 & 1) == 0) {
                break;
            }
            i2 += layoutParams.topMargin + measuredHeight + layoutParams.bottomMargin;
            if (i3 == 0 && ViewCompat.getFitsSystemWindows(childAt)) {
                i2 -= getTopInset();
            }
            if ((i4 & 2) != 0) {
                i2 -= ViewCompat.getMinimumHeight(childAt);
                break;
            }
            i3++;
        }
        int max = Math.max(0, i2);
        this.totalScrollRange = max;
        return max;
    }

    /* access modifiers changed from: package-private */
    public int getUpNestedPreScrollRange() {
        return getTotalScrollRange();
    }

    /* access modifiers changed from: package-private */
    public boolean hasChildWithInterpolator() {
        return this.haveChildWithInterpolator;
    }

    /* access modifiers changed from: package-private */
    public boolean hasScrollableChildren() {
        return getTotalScrollRange() != 0;
    }

    public boolean isLiftOnScroll() {
        return this.liftOnScroll;
    }

    public boolean isLifted() {
        return this.lifted;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.setParentAbsoluteElevation(this);
    }

    /* access modifiers changed from: protected */
    public int[] onCreateDrawableState(int extraSpace) {
        if (this.tmpStatesArray == null) {
            this.tmpStatesArray = new int[4];
        }
        int[] iArr = this.tmpStatesArray;
        int[] onCreateDrawableState = super.onCreateDrawableState(iArr.length + extraSpace);
        iArr[0] = this.liftable ? R.attr.state_liftable : -R.attr.state_liftable;
        iArr[1] = (!this.liftable || !this.lifted) ? -R.attr.state_lifted : R.attr.state_lifted;
        iArr[2] = this.liftable ? R.attr.state_collapsible : -R.attr.state_collapsible;
        iArr[3] = (!this.liftable || !this.lifted) ? -R.attr.state_collapsed : R.attr.state_collapsed;
        return mergeDrawableStates(onCreateDrawableState, iArr);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearLiftOnScrollTargetView();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        boolean z = true;
        if (ViewCompat.getFitsSystemWindows(this) && shouldOffsetFirstChild()) {
            int topInset = getTopInset();
            for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                ViewCompat.offsetTopAndBottom(getChildAt(childCount), topInset);
            }
        }
        invalidateScrollRanges();
        this.haveChildWithInterpolator = false;
        int i = 0;
        int childCount2 = getChildCount();
        while (true) {
            if (i >= childCount2) {
                break;
            } else if (((LayoutParams) getChildAt(i).getLayoutParams()).getScrollInterpolator() != null) {
                this.haveChildWithInterpolator = true;
                break;
            } else {
                i++;
            }
        }
        Drawable drawable = this.statusBarForeground;
        if (drawable != null) {
            drawable.setBounds(0, 0, getWidth(), getTopInset());
        }
        if (!this.liftableOverride) {
            if (!this.liftOnScroll && !hasCollapsibleChild()) {
                z = false;
            }
            setLiftableState(z);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = View.MeasureSpec.getMode(heightMeasureSpec);
        if (mode != 1073741824 && ViewCompat.getFitsSystemWindows(this) && shouldOffsetFirstChild()) {
            int measuredHeight = getMeasuredHeight();
            switch (mode) {
                case Integer.MIN_VALUE:
                    measuredHeight = MathUtils.clamp(getMeasuredHeight() + getTopInset(), 0, View.MeasureSpec.getSize(heightMeasureSpec));
                    break;
                case 0:
                    measuredHeight += getTopInset();
                    break;
            }
            setMeasuredDimension(getMeasuredWidth(), measuredHeight);
        }
        invalidateScrollRanges();
    }

    /* access modifiers changed from: package-private */
    public void onOffsetChanged(int offset) {
        this.currentOffset = offset;
        if (!willNotDraw()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        List<BaseOnOffsetChangedListener> list = this.listeners;
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                BaseOnOffsetChangedListener baseOnOffsetChangedListener = this.listeners.get(i);
                if (baseOnOffsetChangedListener != null) {
                    baseOnOffsetChangedListener.onOffsetChanged(this, offset);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat insets) {
        WindowInsetsCompat windowInsetsCompat = null;
        if (ViewCompat.getFitsSystemWindows(this)) {
            windowInsetsCompat = insets;
        }
        if (!ObjectsCompat.equals(this.lastInsets, windowInsetsCompat)) {
            this.lastInsets = windowInsetsCompat;
            updateWillNotDraw();
            requestLayout();
        }
        return insets;
    }

    public boolean removeLiftOnScrollListener(LiftOnScrollListener liftOnScrollListener) {
        return this.liftOnScrollListeners.remove(liftOnScrollListener);
    }

    public void removeOnOffsetChangedListener(BaseOnOffsetChangedListener listener) {
        List<BaseOnOffsetChangedListener> list = this.listeners;
        if (list != null && listener != null) {
            list.remove(listener);
        }
    }

    public void removeOnOffsetChangedListener(OnOffsetChangedListener listener) {
        removeOnOffsetChangedListener((BaseOnOffsetChangedListener) listener);
    }

    /* access modifiers changed from: package-private */
    public void resetPendingAction() {
        this.pendingAction = 0;
    }

    public void setElevation(float elevation) {
        super.setElevation(elevation);
        MaterialShapeUtils.setElevation(this, elevation);
    }

    public void setExpanded(boolean expanded) {
        setExpanded(expanded, ViewCompat.isLaidOut(this));
    }

    public void setExpanded(boolean expanded, boolean animate) {
        setExpanded(expanded, animate, true);
    }

    public void setLiftOnScroll(boolean liftOnScroll2) {
        this.liftOnScroll = liftOnScroll2;
    }

    public void setLiftOnScrollTargetViewId(int liftOnScrollTargetViewId2) {
        this.liftOnScrollTargetViewId = liftOnScrollTargetViewId2;
        clearLiftOnScrollTargetView();
    }

    public boolean setLiftable(boolean liftable2) {
        this.liftableOverride = true;
        return setLiftableState(liftable2);
    }

    public void setLiftableOverrideEnabled(boolean enabled) {
        this.liftableOverride = enabled;
    }

    public boolean setLifted(boolean lifted2) {
        return setLiftedState(lifted2, true);
    }

    /* access modifiers changed from: package-private */
    public boolean setLiftedState(boolean lifted2) {
        return setLiftedState(lifted2, !this.liftableOverride);
    }

    /* access modifiers changed from: package-private */
    public boolean setLiftedState(boolean lifted2, boolean force) {
        if (!force || this.lifted == lifted2) {
            return false;
        }
        this.lifted = lifted2;
        refreshDrawableState();
        if (!this.liftOnScroll || !(getBackground() instanceof MaterialShapeDrawable)) {
            return true;
        }
        startLiftOnScrollElevationOverlayAnimation((MaterialShapeDrawable) getBackground(), lifted2);
        return true;
    }

    public void setOrientation(int orientation) {
        if (orientation == 1) {
            super.setOrientation(orientation);
            return;
        }
        throw new IllegalArgumentException("AppBarLayout is always vertical and does not support horizontal orientation");
    }

    public void setStatusBarForeground(Drawable drawable) {
        Drawable drawable2 = this.statusBarForeground;
        if (drawable2 != drawable) {
            Drawable drawable3 = null;
            if (drawable2 != null) {
                drawable2.setCallback((Drawable.Callback) null);
            }
            if (drawable != null) {
                drawable3 = drawable.mutate();
            }
            this.statusBarForeground = drawable3;
            if (drawable3 != null) {
                if (drawable3.isStateful()) {
                    this.statusBarForeground.setState(getDrawableState());
                }
                DrawableCompat.setLayoutDirection(this.statusBarForeground, ViewCompat.getLayoutDirection(this));
                this.statusBarForeground.setVisible(getVisibility() == 0, false);
                this.statusBarForeground.setCallback(this);
            }
            updateWillNotDraw();
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setStatusBarForegroundColor(int color) {
        setStatusBarForeground(new ColorDrawable(color));
    }

    public void setStatusBarForegroundResource(int resId) {
        setStatusBarForeground(AppCompatResources.getDrawable(getContext(), resId));
    }

    @Deprecated
    public void setTargetElevation(float elevation) {
        if (Build.VERSION.SDK_INT >= 21) {
            ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator(this, elevation);
        }
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        boolean z = visibility == 0;
        Drawable drawable = this.statusBarForeground;
        if (drawable != null) {
            drawable.setVisible(z, false);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean shouldLift(View defaultScrollingView) {
        View findLiftOnScrollTargetView = findLiftOnScrollTargetView(defaultScrollingView);
        if (findLiftOnScrollTargetView == null) {
            findLiftOnScrollTargetView = defaultScrollingView;
        }
        return findLiftOnScrollTargetView != null && (findLiftOnScrollTargetView.canScrollVertically(-1) || findLiftOnScrollTargetView.getScrollY() > 0);
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.statusBarForeground;
    }
}

package androidx.coordinatorlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.R;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Pools;
import androidx.core.view.GravityCompat;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinatorLayout extends ViewGroup implements NestedScrollingParent2, NestedScrollingParent3 {
    static final Class<?>[] CONSTRUCTOR_PARAMS = {Context.class, AttributeSet.class};
    static final int EVENT_NESTED_SCROLL = 1;
    static final int EVENT_PRE_DRAW = 0;
    static final int EVENT_VIEW_REMOVED = 2;
    static final String TAG = "CoordinatorLayout";
    static final Comparator<View> TOP_SORTED_CHILDREN_COMPARATOR;
    private static final int TYPE_ON_INTERCEPT = 0;
    private static final int TYPE_ON_TOUCH = 1;
    static final String WIDGET_PACKAGE_NAME;
    static final ThreadLocal<Map<String, Constructor<Behavior>>> sConstructors = new ThreadLocal<>();
    private static final Pools.Pool<Rect> sRectPool = new Pools.SynchronizedPool(12);
    private OnApplyWindowInsetsListener mApplyWindowInsetsListener;
    private final int[] mBehaviorConsumed;
    private View mBehaviorTouchView;
    private final DirectedAcyclicGraph<View> mChildDag;
    private final List<View> mDependencySortedChildren;
    private boolean mDisallowInterceptReset;
    private boolean mDrawStatusBarBackground;
    private boolean mIsAttachedToWindow;
    private int[] mKeylines;
    private WindowInsetsCompat mLastInsets;
    private boolean mNeedsPreDrawListener;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private View mNestedScrollingTarget;
    private final int[] mNestedScrollingV2ConsumedCompat;
    ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;
    private OnPreDrawListener mOnPreDrawListener;
    private Paint mScrimPaint;
    private Drawable mStatusBarBackground;
    private final List<View> mTempDependenciesList;
    private final List<View> mTempList1;

    public interface AttachedBehavior {
        Behavior getBehavior();
    }

    public static abstract class Behavior<V extends View> {
        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attrs) {
        }

        public static Object getTag(View child) {
            return ((LayoutParams) child.getLayoutParams()).mBehaviorTag;
        }

        public static void setTag(View child, Object tag) {
            ((LayoutParams) child.getLayoutParams()).mBehaviorTag = tag;
        }

        public boolean blocksInteractionBelow(CoordinatorLayout parent, V v) {
            return getScrimOpacity(parent, v) > 0.0f;
        }

        public boolean getInsetDodgeRect(CoordinatorLayout parent, V v, Rect rect) {
            return false;
        }

        public int getScrimColor(CoordinatorLayout parent, V v) {
            return ViewCompat.MEASURED_STATE_MASK;
        }

        public float getScrimOpacity(CoordinatorLayout parent, V v) {
            return 0.0f;
        }

        public boolean layoutDependsOn(CoordinatorLayout parent, V v, View dependency) {
            return false;
        }

        public WindowInsetsCompat onApplyWindowInsets(CoordinatorLayout coordinatorLayout, V v, WindowInsetsCompat insets) {
            return insets;
        }

        public void onAttachedToLayoutParams(LayoutParams params) {
        }

        public boolean onDependentViewChanged(CoordinatorLayout parent, V v, View dependency) {
            return false;
        }

        public void onDependentViewRemoved(CoordinatorLayout parent, V v, View dependency) {
        }

        public void onDetachedFromLayoutParams() {
        }

        public boolean onInterceptTouchEvent(CoordinatorLayout parent, V v, MotionEvent ev) {
            return false;
        }

        public boolean onLayoutChild(CoordinatorLayout parent, V v, int layoutDirection) {
            return false;
        }

        public boolean onMeasureChild(CoordinatorLayout parent, V v, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
            return false;
        }

        public boolean onNestedFling(CoordinatorLayout coordinatorLayout, V v, View target, float velocityX, float velocityY, boolean consumed) {
            return false;
        }

        public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V v, View target, float velocityX, float velocityY) {
            return false;
        }

        @Deprecated
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v, View target, int dx, int dy, int[] consumed) {
        }

        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v, View target, int dx, int dy, int[] consumed, int type) {
            if (type == 0) {
                onNestedPreScroll(coordinatorLayout, v, target, dx, dy, consumed);
            }
        }

        @Deprecated
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, V v, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        }

        @Deprecated
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, V v, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
            if (type == 0) {
                onNestedScroll(coordinatorLayout, v, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
            }
        }

        public void onNestedScroll(CoordinatorLayout coordinatorLayout, V v, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int[] consumed) {
            consumed[0] = consumed[0] + dxUnconsumed;
            consumed[1] = consumed[1] + dyUnconsumed;
            onNestedScroll(coordinatorLayout, v, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        }

        @Deprecated
        public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, V v, View directTargetChild, View target, int axes) {
        }

        public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, V v, View directTargetChild, View target, int axes, int type) {
            if (type == 0) {
                onNestedScrollAccepted(coordinatorLayout, v, directTargetChild, target, axes);
            }
        }

        public boolean onRequestChildRectangleOnScreen(CoordinatorLayout coordinatorLayout, V v, Rect rectangle, boolean immediate) {
            return false;
        }

        public void onRestoreInstanceState(CoordinatorLayout parent, V v, Parcelable state) {
        }

        public Parcelable onSaveInstanceState(CoordinatorLayout parent, V v) {
            return View.BaseSavedState.EMPTY_STATE;
        }

        @Deprecated
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v, View directTargetChild, View target, int axes) {
            return false;
        }

        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v, View directTargetChild, View target, int axes, int type) {
            if (type == 0) {
                return onStartNestedScroll(coordinatorLayout, v, directTargetChild, target, axes);
            }
            return false;
        }

        @Deprecated
        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V v, View target) {
        }

        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, V v, View target, int type) {
            if (type == 0) {
                onStopNestedScroll(coordinatorLayout, v, target);
            }
        }

        public boolean onTouchEvent(CoordinatorLayout parent, V v, MotionEvent ev) {
            return false;
        }
    }

    @Deprecated
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DefaultBehavior {
        Class<? extends Behavior> value();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DispatchChangeEvent {
    }

    private class HierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {
        HierarchyChangeListener() {
        }

        public void onChildViewAdded(View parent, View child) {
            if (CoordinatorLayout.this.mOnHierarchyChangeListener != null) {
                CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        public void onChildViewRemoved(View parent, View child) {
            CoordinatorLayout.this.onChildViewsChanged(2);
            if (CoordinatorLayout.this.mOnHierarchyChangeListener != null) {
                CoordinatorLayout.this.mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public int anchorGravity = 0;
        public int dodgeInsetEdges = 0;
        public int gravity = 0;
        public int insetEdge = 0;
        public int keyline = -1;
        View mAnchorDirectChild;
        int mAnchorId = -1;
        View mAnchorView;
        Behavior mBehavior;
        boolean mBehaviorResolved = false;
        Object mBehaviorTag;
        private boolean mDidAcceptNestedScrollNonTouch;
        private boolean mDidAcceptNestedScrollTouch;
        private boolean mDidBlockInteraction;
        private boolean mDidChangeAfterNestedScroll;
        int mInsetOffsetX;
        int mInsetOffsetY;
        final Rect mLastChildRect = new Rect();

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.CoordinatorLayout_Layout);
            this.gravity = obtainStyledAttributes.getInteger(R.styleable.CoordinatorLayout_Layout_android_layout_gravity, 0);
            this.mAnchorId = obtainStyledAttributes.getResourceId(R.styleable.CoordinatorLayout_Layout_layout_anchor, -1);
            this.anchorGravity = obtainStyledAttributes.getInteger(R.styleable.CoordinatorLayout_Layout_layout_anchorGravity, 0);
            this.keyline = obtainStyledAttributes.getInteger(R.styleable.CoordinatorLayout_Layout_layout_keyline, -1);
            this.insetEdge = obtainStyledAttributes.getInt(R.styleable.CoordinatorLayout_Layout_layout_insetEdge, 0);
            this.dodgeInsetEdges = obtainStyledAttributes.getInt(R.styleable.CoordinatorLayout_Layout_layout_dodgeInsetEdges, 0);
            boolean hasValue = obtainStyledAttributes.hasValue(R.styleable.CoordinatorLayout_Layout_layout_behavior);
            this.mBehaviorResolved = hasValue;
            if (hasValue) {
                this.mBehavior = CoordinatorLayout.parseBehavior(context, attrs, obtainStyledAttributes.getString(R.styleable.CoordinatorLayout_Layout_layout_behavior));
            }
            obtainStyledAttributes.recycle();
            Behavior behavior = this.mBehavior;
            if (behavior != null) {
                behavior.onAttachedToLayoutParams(this);
            }
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams p) {
            super(p);
        }

        public LayoutParams(LayoutParams p) {
            super(p);
        }

        private void resolveAnchorView(View forChild, CoordinatorLayout parent) {
            View findViewById = parent.findViewById(this.mAnchorId);
            this.mAnchorView = findViewById;
            if (findViewById != null) {
                if (findViewById != parent) {
                    View view = this.mAnchorView;
                    ViewParent parent2 = findViewById.getParent();
                    while (parent2 != parent && parent2 != null) {
                        if (parent2 != forChild) {
                            if (parent2 instanceof View) {
                                view = (View) parent2;
                            }
                            parent2 = parent2.getParent();
                        } else if (parent.isInEditMode()) {
                            this.mAnchorDirectChild = null;
                            this.mAnchorView = null;
                            return;
                        } else {
                            throw new IllegalStateException("Anchor must not be a descendant of the anchored view");
                        }
                    }
                    this.mAnchorDirectChild = view;
                } else if (parent.isInEditMode()) {
                    this.mAnchorDirectChild = null;
                    this.mAnchorView = null;
                } else {
                    throw new IllegalStateException("View can not be anchored to the the parent CoordinatorLayout");
                }
            } else if (parent.isInEditMode()) {
                this.mAnchorDirectChild = null;
                this.mAnchorView = null;
            } else {
                throw new IllegalStateException("Could not find CoordinatorLayout descendant view with id " + parent.getResources().getResourceName(this.mAnchorId) + " to anchor view " + forChild);
            }
        }

        private boolean shouldDodge(View other, int layoutDirection) {
            int absoluteGravity = GravityCompat.getAbsoluteGravity(((LayoutParams) other.getLayoutParams()).insetEdge, layoutDirection);
            return absoluteGravity != 0 && (GravityCompat.getAbsoluteGravity(this.dodgeInsetEdges, layoutDirection) & absoluteGravity) == absoluteGravity;
        }

        private boolean verifyAnchorView(View forChild, CoordinatorLayout parent) {
            if (this.mAnchorView.getId() != this.mAnchorId) {
                return false;
            }
            View view = this.mAnchorView;
            for (ViewParent parent2 = this.mAnchorView.getParent(); parent2 != parent; parent2 = parent2.getParent()) {
                if (parent2 == null || parent2 == forChild) {
                    this.mAnchorDirectChild = null;
                    this.mAnchorView = null;
                    return false;
                }
                if (parent2 instanceof View) {
                    view = (View) parent2;
                }
            }
            this.mAnchorDirectChild = view;
            return true;
        }

        /* access modifiers changed from: package-private */
        public boolean checkAnchorChanged() {
            return this.mAnchorView == null && this.mAnchorId != -1;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:4:0x000e, code lost:
            r0 = r1.mBehavior;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean dependsOn(androidx.coordinatorlayout.widget.CoordinatorLayout r2, android.view.View r3, android.view.View r4) {
            /*
                r1 = this;
                android.view.View r0 = r1.mAnchorDirectChild
                if (r4 == r0) goto L_0x001b
                int r0 = androidx.core.view.ViewCompat.getLayoutDirection(r2)
                boolean r0 = r1.shouldDodge(r4, r0)
                if (r0 != 0) goto L_0x001b
                androidx.coordinatorlayout.widget.CoordinatorLayout$Behavior r0 = r1.mBehavior
                if (r0 == 0) goto L_0x0019
                boolean r0 = r0.layoutDependsOn(r2, r3, r4)
                if (r0 == 0) goto L_0x0019
                goto L_0x001b
            L_0x0019:
                r0 = 0
                goto L_0x001c
            L_0x001b:
                r0 = 1
            L_0x001c:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams.dependsOn(androidx.coordinatorlayout.widget.CoordinatorLayout, android.view.View, android.view.View):boolean");
        }

        /* access modifiers changed from: package-private */
        public boolean didBlockInteraction() {
            if (this.mBehavior == null) {
                this.mDidBlockInteraction = false;
            }
            return this.mDidBlockInteraction;
        }

        /* access modifiers changed from: package-private */
        public View findAnchorView(CoordinatorLayout parent, View forChild) {
            if (this.mAnchorId == -1) {
                this.mAnchorDirectChild = null;
                this.mAnchorView = null;
                return null;
            }
            if (this.mAnchorView == null || !verifyAnchorView(forChild, parent)) {
                resolveAnchorView(forChild, parent);
            }
            return this.mAnchorView;
        }

        public int getAnchorId() {
            return this.mAnchorId;
        }

        public Behavior getBehavior() {
            return this.mBehavior;
        }

        /* access modifiers changed from: package-private */
        public boolean getChangedAfterNestedScroll() {
            return this.mDidChangeAfterNestedScroll;
        }

        /* access modifiers changed from: package-private */
        public Rect getLastChildRect() {
            return this.mLastChildRect;
        }

        /* access modifiers changed from: package-private */
        public void invalidateAnchor() {
            this.mAnchorDirectChild = null;
            this.mAnchorView = null;
        }

        /* access modifiers changed from: package-private */
        public boolean isBlockingInteractionBelow(CoordinatorLayout parent, View child) {
            boolean z = this.mDidBlockInteraction;
            if (z) {
                return true;
            }
            Behavior behavior = this.mBehavior;
            boolean blocksInteractionBelow = z | (behavior != null ? behavior.blocksInteractionBelow(parent, child) : false);
            this.mDidBlockInteraction = blocksInteractionBelow;
            return blocksInteractionBelow;
        }

        /* access modifiers changed from: package-private */
        public boolean isNestedScrollAccepted(int type) {
            switch (type) {
                case 0:
                    return this.mDidAcceptNestedScrollTouch;
                case 1:
                    return this.mDidAcceptNestedScrollNonTouch;
                default:
                    return false;
            }
        }

        /* access modifiers changed from: package-private */
        public void resetChangedAfterNestedScroll() {
            this.mDidChangeAfterNestedScroll = false;
        }

        /* access modifiers changed from: package-private */
        public void resetNestedScroll(int type) {
            setNestedScrollAccepted(type, false);
        }

        /* access modifiers changed from: package-private */
        public void resetTouchBehaviorTracking() {
            this.mDidBlockInteraction = false;
        }

        public void setAnchorId(int id) {
            invalidateAnchor();
            this.mAnchorId = id;
        }

        public void setBehavior(Behavior behavior) {
            Behavior behavior2 = this.mBehavior;
            if (behavior2 != behavior) {
                if (behavior2 != null) {
                    behavior2.onDetachedFromLayoutParams();
                }
                this.mBehavior = behavior;
                this.mBehaviorTag = null;
                this.mBehaviorResolved = true;
                if (behavior != null) {
                    behavior.onAttachedToLayoutParams(this);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void setChangedAfterNestedScroll(boolean changed) {
            this.mDidChangeAfterNestedScroll = changed;
        }

        /* access modifiers changed from: package-private */
        public void setLastChildRect(Rect r) {
            this.mLastChildRect.set(r);
        }

        /* access modifiers changed from: package-private */
        public void setNestedScrollAccepted(int type, boolean accept) {
            switch (type) {
                case 0:
                    this.mDidAcceptNestedScrollTouch = accept;
                    return;
                case 1:
                    this.mDidAcceptNestedScrollNonTouch = accept;
                    return;
                default:
                    return;
            }
        }
    }

    class OnPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
        OnPreDrawListener() {
        }

        public boolean onPreDraw() {
            CoordinatorLayout.this.onChildViewsChanged(0);
            return true;
        }
    }

    protected static class SavedState extends AbsSavedState {
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
        SparseArray<Parcelable> behaviorStates;

        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            int readInt = source.readInt();
            int[] iArr = new int[readInt];
            source.readIntArray(iArr);
            Parcelable[] readParcelableArray = source.readParcelableArray(loader);
            this.behaviorStates = new SparseArray<>(readInt);
            for (int i = 0; i < readInt; i++) {
                this.behaviorStates.append(iArr[i], readParcelableArray[i]);
            }
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            SparseArray<Parcelable> sparseArray = this.behaviorStates;
            int size = sparseArray != null ? sparseArray.size() : 0;
            dest.writeInt(size);
            int[] iArr = new int[size];
            Parcelable[] parcelableArr = new Parcelable[size];
            for (int i = 0; i < size; i++) {
                iArr[i] = this.behaviorStates.keyAt(i);
                parcelableArr[i] = this.behaviorStates.valueAt(i);
            }
            dest.writeIntArray(iArr);
            dest.writeParcelableArray(parcelableArr, flags);
        }
    }

    static class ViewElevationComparator implements Comparator<View> {
        ViewElevationComparator() {
        }

        public int compare(View lhs, View rhs) {
            float z = ViewCompat.getZ(lhs);
            float z2 = ViewCompat.getZ(rhs);
            if (z > z2) {
                return -1;
            }
            return z < z2 ? 1 : 0;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: java.lang.Class<?>[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    static {
        /*
            java.lang.Class<androidx.coordinatorlayout.widget.CoordinatorLayout> r0 = androidx.coordinatorlayout.widget.CoordinatorLayout.class
            java.lang.Package r0 = r0.getPackage()
            r1 = 0
            if (r0 == 0) goto L_0x000e
            java.lang.String r2 = r0.getName()
            goto L_0x000f
        L_0x000e:
            r2 = r1
        L_0x000f:
            WIDGET_PACKAGE_NAME = r2
            int r0 = android.os.Build.VERSION.SDK_INT
            r2 = 21
            if (r0 < r2) goto L_0x001f
            androidx.coordinatorlayout.widget.CoordinatorLayout$ViewElevationComparator r0 = new androidx.coordinatorlayout.widget.CoordinatorLayout$ViewElevationComparator
            r0.<init>()
            TOP_SORTED_CHILDREN_COMPARATOR = r0
            goto L_0x0021
        L_0x001f:
            TOP_SORTED_CHILDREN_COMPARATOR = r1
        L_0x0021:
            r0 = 2
            java.lang.Class[] r0 = new java.lang.Class[r0]
            r1 = 0
            java.lang.Class<android.content.Context> r2 = android.content.Context.class
            r0[r1] = r2
            r1 = 1
            java.lang.Class<android.util.AttributeSet> r2 = android.util.AttributeSet.class
            r0[r1] = r2
            CONSTRUCTOR_PARAMS = r0
            java.lang.ThreadLocal r0 = new java.lang.ThreadLocal
            r0.<init>()
            sConstructors = r0
            androidx.core.util.Pools$SynchronizedPool r0 = new androidx.core.util.Pools$SynchronizedPool
            r1 = 12
            r0.<init>(r1)
            sRectPool = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.coordinatorlayout.widget.CoordinatorLayout.<clinit>():void");
    }

    public CoordinatorLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public CoordinatorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.coordinatorLayoutStyle);
    }

    public CoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mDependencySortedChildren = new ArrayList();
        this.mChildDag = new DirectedAcyclicGraph<>();
        this.mTempList1 = new ArrayList();
        this.mTempDependenciesList = new ArrayList();
        this.mBehaviorConsumed = new int[2];
        this.mNestedScrollingV2ConsumedCompat = new int[2];
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        TypedArray obtainStyledAttributes = defStyleAttr == 0 ? context.obtainStyledAttributes(attrs, R.styleable.CoordinatorLayout, 0, R.style.Widget_Support_CoordinatorLayout) : context.obtainStyledAttributes(attrs, R.styleable.CoordinatorLayout, defStyleAttr, 0);
        if (Build.VERSION.SDK_INT >= 29) {
            if (defStyleAttr == 0) {
                saveAttributeDataForStyleable(context, R.styleable.CoordinatorLayout, attrs, obtainStyledAttributes, 0, R.style.Widget_Support_CoordinatorLayout);
            } else {
                saveAttributeDataForStyleable(context, R.styleable.CoordinatorLayout, attrs, obtainStyledAttributes, defStyleAttr, 0);
            }
        }
        int resourceId = obtainStyledAttributes.getResourceId(R.styleable.CoordinatorLayout_keylines, 0);
        if (resourceId != 0) {
            Resources resources = context.getResources();
            this.mKeylines = resources.getIntArray(resourceId);
            float f = resources.getDisplayMetrics().density;
            int length = this.mKeylines.length;
            for (int i = 0; i < length; i++) {
                int[] iArr = this.mKeylines;
                iArr[i] = (int) (((float) iArr[i]) * f);
            }
        }
        this.mStatusBarBackground = obtainStyledAttributes.getDrawable(R.styleable.CoordinatorLayout_statusBarBackground);
        obtainStyledAttributes.recycle();
        setupForInsets();
        super.setOnHierarchyChangeListener(new HierarchyChangeListener());
        if (ViewCompat.getImportantForAccessibility(this) == 0) {
            ViewCompat.setImportantForAccessibility(this, 1);
        }
    }

    private static Rect acquireTempRect() {
        Rect acquire = sRectPool.acquire();
        return acquire == null ? new Rect() : acquire;
    }

    private static int clamp(int value, int min, int max) {
        return value < min ? min : value > max ? max : value;
    }

    private void constrainChildRect(LayoutParams lp, Rect out, int childWidth, int childHeight) {
        int width = getWidth();
        int height = getHeight();
        int max = Math.max(getPaddingLeft() + lp.leftMargin, Math.min(out.left, ((width - getPaddingRight()) - childWidth) - lp.rightMargin));
        int max2 = Math.max(getPaddingTop() + lp.topMargin, Math.min(out.top, ((height - getPaddingBottom()) - childHeight) - lp.bottomMargin));
        out.set(max, max2, max + childWidth, max2 + childHeight);
    }

    private WindowInsetsCompat dispatchApplyWindowInsetsToBehaviors(WindowInsetsCompat insets) {
        Behavior behavior;
        if (insets.isConsumed()) {
            return insets;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (ViewCompat.getFitsSystemWindows(childAt) && (behavior = ((LayoutParams) childAt.getLayoutParams()).getBehavior()) != null) {
                insets = behavior.onApplyWindowInsets(this, childAt, insets);
                if (insets.isConsumed()) {
                    break;
                }
            }
        }
        return insets;
    }

    private void getDesiredAnchoredChildRectWithoutConstraints(View child, int layoutDirection, Rect anchorRect, Rect out, LayoutParams lp, int childWidth, int childHeight) {
        int i;
        int i2;
        int i3 = layoutDirection;
        Rect rect = anchorRect;
        LayoutParams layoutParams = lp;
        int absoluteGravity = GravityCompat.getAbsoluteGravity(resolveAnchoredChildGravity(layoutParams.gravity), i3);
        int absoluteGravity2 = GravityCompat.getAbsoluteGravity(resolveGravity(layoutParams.anchorGravity), i3);
        int i4 = absoluteGravity & 7;
        int i5 = absoluteGravity & 112;
        int i6 = absoluteGravity2 & 112;
        switch (absoluteGravity2 & 7) {
            case 1:
                i = rect.left + (anchorRect.width() / 2);
                break;
            case 5:
                i = rect.right;
                break;
            default:
                i = rect.left;
                break;
        }
        switch (i6) {
            case 16:
                i2 = rect.top + (anchorRect.height() / 2);
                break;
            case 80:
                i2 = rect.bottom;
                break;
            default:
                i2 = rect.top;
                break;
        }
        switch (i4) {
            case 1:
                i -= childWidth / 2;
                break;
            case 5:
                break;
            default:
                i -= childWidth;
                break;
        }
        switch (i5) {
            case 16:
                i2 -= childHeight / 2;
                break;
            case 80:
                break;
            default:
                i2 -= childHeight;
                break;
        }
        out.set(i, i2, i + childWidth, i2 + childHeight);
    }

    private int getKeyline(int index) {
        int[] iArr = this.mKeylines;
        if (iArr == null) {
            Log.e(TAG, "No keylines defined for " + this + " - attempted index lookup " + index);
            return 0;
        } else if (index >= 0 && index < iArr.length) {
            return iArr[index];
        } else {
            Log.e(TAG, "Keyline index " + index + " out of range for " + this);
            return 0;
        }
    }

    private void getTopSortedChildren(List<View> list) {
        list.clear();
        boolean isChildrenDrawingOrderEnabled = isChildrenDrawingOrderEnabled();
        int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            list.add(getChildAt(isChildrenDrawingOrderEnabled ? getChildDrawingOrder(childCount, i) : i));
        }
        Comparator<View> comparator = TOP_SORTED_CHILDREN_COMPARATOR;
        if (comparator != null) {
            Collections.sort(list, comparator);
        }
    }

    private boolean hasDependencies(View child) {
        return this.mChildDag.hasOutgoingEdges(child);
    }

    private void layoutChild(View child, int layoutDirection) {
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        Rect acquireTempRect = acquireTempRect();
        acquireTempRect.set(getPaddingLeft() + layoutParams.leftMargin, getPaddingTop() + layoutParams.topMargin, (getWidth() - getPaddingRight()) - layoutParams.rightMargin, (getHeight() - getPaddingBottom()) - layoutParams.bottomMargin);
        if (this.mLastInsets != null && ViewCompat.getFitsSystemWindows(this) && !ViewCompat.getFitsSystemWindows(child)) {
            acquireTempRect.left += this.mLastInsets.getSystemWindowInsetLeft();
            acquireTempRect.top += this.mLastInsets.getSystemWindowInsetTop();
            acquireTempRect.right -= this.mLastInsets.getSystemWindowInsetRight();
            acquireTempRect.bottom -= this.mLastInsets.getSystemWindowInsetBottom();
        }
        Rect acquireTempRect2 = acquireTempRect();
        GravityCompat.apply(resolveGravity(layoutParams.gravity), child.getMeasuredWidth(), child.getMeasuredHeight(), acquireTempRect, acquireTempRect2, layoutDirection);
        child.layout(acquireTempRect2.left, acquireTempRect2.top, acquireTempRect2.right, acquireTempRect2.bottom);
        releaseTempRect(acquireTempRect);
        releaseTempRect(acquireTempRect2);
    }

    private void layoutChildWithAnchor(View child, View anchor, int layoutDirection) {
        Rect acquireTempRect = acquireTempRect();
        Rect acquireTempRect2 = acquireTempRect();
        try {
            getDescendantRect(anchor, acquireTempRect);
            getDesiredAnchoredChildRect(child, layoutDirection, acquireTempRect, acquireTempRect2);
            child.layout(acquireTempRect2.left, acquireTempRect2.top, acquireTempRect2.right, acquireTempRect2.bottom);
        } finally {
            releaseTempRect(acquireTempRect);
            releaseTempRect(acquireTempRect2);
        }
    }

    private void layoutChildWithKeyline(View child, int keyline, int layoutDirection) {
        int i = layoutDirection;
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        int absoluteGravity = GravityCompat.getAbsoluteGravity(resolveKeylineGravity(layoutParams.gravity), i);
        int i2 = absoluteGravity & 7;
        int i3 = absoluteGravity & 112;
        int width = getWidth();
        int height = getHeight();
        int measuredWidth = child.getMeasuredWidth();
        int measuredHeight = child.getMeasuredHeight();
        int keyline2 = getKeyline(i == 1 ? width - keyline : keyline) - measuredWidth;
        int i4 = 0;
        switch (i2) {
            case 1:
                keyline2 += measuredWidth / 2;
                break;
            case 5:
                keyline2 += measuredWidth;
                break;
        }
        switch (i3) {
            case 16:
                i4 = 0 + (measuredHeight / 2);
                break;
            case 80:
                i4 = 0 + measuredHeight;
                break;
        }
        int max = Math.max(getPaddingLeft() + layoutParams.leftMargin, Math.min(keyline2, ((width - getPaddingRight()) - measuredWidth) - layoutParams.rightMargin));
        int max2 = Math.max(getPaddingTop() + layoutParams.topMargin, Math.min(i4, ((height - getPaddingBottom()) - measuredHeight) - layoutParams.bottomMargin));
        child.layout(max, max2, max + measuredWidth, max2 + measuredHeight);
    }

    private void offsetChildByInset(View child, Rect inset, int layoutDirection) {
        int width;
        int i;
        int height;
        int i2;
        if (ViewCompat.isLaidOut(child) && child.getWidth() > 0 && child.getHeight() > 0) {
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            Behavior behavior = layoutParams.getBehavior();
            Rect acquireTempRect = acquireTempRect();
            Rect acquireTempRect2 = acquireTempRect();
            acquireTempRect2.set(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
            if (behavior == null || !behavior.getInsetDodgeRect(this, child, acquireTempRect)) {
                acquireTempRect.set(acquireTempRect2);
            } else if (!acquireTempRect2.contains(acquireTempRect)) {
                throw new IllegalArgumentException("Rect should be within the child's bounds. Rect:" + acquireTempRect.toShortString() + " | Bounds:" + acquireTempRect2.toShortString());
            }
            releaseTempRect(acquireTempRect2);
            if (acquireTempRect.isEmpty()) {
                releaseTempRect(acquireTempRect);
                return;
            }
            int absoluteGravity = GravityCompat.getAbsoluteGravity(layoutParams.dodgeInsetEdges, layoutDirection);
            boolean z = false;
            if ((absoluteGravity & 48) == 48 && (i2 = (acquireTempRect.top - layoutParams.topMargin) - layoutParams.mInsetOffsetY) < inset.top) {
                setInsetOffsetY(child, inset.top - i2);
                z = true;
            }
            if ((absoluteGravity & 80) == 80 && (height = ((getHeight() - acquireTempRect.bottom) - layoutParams.bottomMargin) + layoutParams.mInsetOffsetY) < inset.bottom) {
                setInsetOffsetY(child, height - inset.bottom);
                z = true;
            }
            if (!z) {
                setInsetOffsetY(child, 0);
            }
            boolean z2 = false;
            if ((absoluteGravity & 3) == 3 && (i = (acquireTempRect.left - layoutParams.leftMargin) - layoutParams.mInsetOffsetX) < inset.left) {
                setInsetOffsetX(child, inset.left - i);
                z2 = true;
            }
            if ((absoluteGravity & 5) == 5 && (width = ((getWidth() - acquireTempRect.right) - layoutParams.rightMargin) + layoutParams.mInsetOffsetX) < inset.right) {
                setInsetOffsetX(child, width - inset.right);
                z2 = true;
            }
            if (!z2) {
                setInsetOffsetX(child, 0);
            }
            releaseTempRect(acquireTempRect);
        }
    }

    static Behavior parseBehavior(Context context, AttributeSet attrs, String name) {
        String str;
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        if (name.startsWith(".")) {
            str = context.getPackageName() + name;
        } else if (name.indexOf(46) >= 0) {
            str = name;
        } else {
            String str2 = WIDGET_PACKAGE_NAME;
            str = !TextUtils.isEmpty(str2) ? str2 + '.' + name : name;
        }
        try {
            ThreadLocal<Map<String, Constructor<Behavior>>> threadLocal = sConstructors;
            Map map = threadLocal.get();
            if (map == null) {
                map = new HashMap();
                threadLocal.set(map);
            }
            Constructor<?> constructor = (Constructor) map.get(str);
            if (constructor == null) {
                constructor = Class.forName(str, false, context.getClassLoader()).getConstructor(CONSTRUCTOR_PARAMS);
                constructor.setAccessible(true);
                map.put(str, constructor);
            }
            return (Behavior) constructor.newInstance(new Object[]{context, attrs});
        } catch (Exception e) {
            throw new RuntimeException("Could not inflate Behavior subclass " + str, e);
        }
    }

    private boolean performIntercept(MotionEvent ev, int type) {
        MotionEvent motionEvent = ev;
        boolean z = false;
        boolean z2 = false;
        MotionEvent motionEvent2 = null;
        int actionMasked = ev.getActionMasked();
        List<View> list = this.mTempList1;
        getTopSortedChildren(list);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            View view = list.get(i);
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            Behavior behavior = layoutParams.getBehavior();
            if ((z || z2) && actionMasked != 0) {
                if (behavior != null) {
                    if (motionEvent2 == null) {
                        long uptimeMillis = SystemClock.uptimeMillis();
                        motionEvent2 = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                    }
                    switch (type) {
                        case 0:
                            behavior.onInterceptTouchEvent(this, view, motionEvent2);
                            break;
                        case 1:
                            behavior.onTouchEvent(this, view, motionEvent2);
                            break;
                    }
                }
            } else {
                if (!z && behavior != null) {
                    switch (type) {
                        case 0:
                            z = behavior.onInterceptTouchEvent(this, view, motionEvent);
                            break;
                        case 1:
                            z = behavior.onTouchEvent(this, view, motionEvent);
                            break;
                    }
                    if (z) {
                        this.mBehaviorTouchView = view;
                    }
                }
                boolean didBlockInteraction = layoutParams.didBlockInteraction();
                boolean isBlockingInteractionBelow = layoutParams.isBlockingInteractionBelow(this, view);
                z2 = isBlockingInteractionBelow && !didBlockInteraction;
                if (isBlockingInteractionBelow && !z2) {
                    list.clear();
                    return z;
                }
            }
        }
        list.clear();
        return z;
    }

    private void prepareChildren() {
        this.mDependencySortedChildren.clear();
        this.mChildDag.clear();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            LayoutParams resolvedLayoutParams = getResolvedLayoutParams(childAt);
            resolvedLayoutParams.findAnchorView(this, childAt);
            this.mChildDag.addNode(childAt);
            for (int i2 = 0; i2 < childCount; i2++) {
                if (i2 != i) {
                    View childAt2 = getChildAt(i2);
                    if (resolvedLayoutParams.dependsOn(this, childAt, childAt2)) {
                        if (!this.mChildDag.contains(childAt2)) {
                            this.mChildDag.addNode(childAt2);
                        }
                        this.mChildDag.addEdge(childAt2, childAt);
                    }
                }
            }
        }
        this.mDependencySortedChildren.addAll(this.mChildDag.getSortedList());
        Collections.reverse(this.mDependencySortedChildren);
    }

    private static void releaseTempRect(Rect rect) {
        rect.setEmpty();
        sRectPool.release(rect);
    }

    private void resetTouchBehaviors(boolean notifyOnInterceptTouchEvent) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            Behavior behavior = ((LayoutParams) childAt.getLayoutParams()).getBehavior();
            if (behavior != null) {
                long uptimeMillis = SystemClock.uptimeMillis();
                MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                if (notifyOnInterceptTouchEvent) {
                    behavior.onInterceptTouchEvent(this, childAt, obtain);
                } else {
                    behavior.onTouchEvent(this, childAt, obtain);
                }
                obtain.recycle();
            }
        }
        for (int i2 = 0; i2 < childCount; i2++) {
            ((LayoutParams) getChildAt(i2).getLayoutParams()).resetTouchBehaviorTracking();
        }
        this.mBehaviorTouchView = null;
        this.mDisallowInterceptReset = false;
    }

    private static int resolveAnchoredChildGravity(int gravity) {
        if (gravity == 0) {
            return 17;
        }
        return gravity;
    }

    private static int resolveGravity(int gravity) {
        if ((gravity & 7) == 0) {
            gravity |= GravityCompat.START;
        }
        return (gravity & 112) == 0 ? gravity | 48 : gravity;
    }

    private static int resolveKeylineGravity(int gravity) {
        if (gravity == 0) {
            return 8388661;
        }
        return gravity;
    }

    private void setInsetOffsetX(View child, int offsetX) {
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        if (layoutParams.mInsetOffsetX != offsetX) {
            ViewCompat.offsetLeftAndRight(child, offsetX - layoutParams.mInsetOffsetX);
            layoutParams.mInsetOffsetX = offsetX;
        }
    }

    private void setInsetOffsetY(View child, int offsetY) {
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        if (layoutParams.mInsetOffsetY != offsetY) {
            ViewCompat.offsetTopAndBottom(child, offsetY - layoutParams.mInsetOffsetY);
            layoutParams.mInsetOffsetY = offsetY;
        }
    }

    private void setupForInsets() {
        if (Build.VERSION.SDK_INT >= 21) {
            if (ViewCompat.getFitsSystemWindows(this)) {
                if (this.mApplyWindowInsetsListener == null) {
                    this.mApplyWindowInsetsListener = new OnApplyWindowInsetsListener() {
                        public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                            return CoordinatorLayout.this.setWindowInsets(insets);
                        }
                    };
                }
                ViewCompat.setOnApplyWindowInsetsListener(this, this.mApplyWindowInsetsListener);
                setSystemUiVisibility(1280);
                return;
            }
            ViewCompat.setOnApplyWindowInsetsListener(this, (OnApplyWindowInsetsListener) null);
        }
    }

    /* access modifiers changed from: package-private */
    public void addPreDrawListener() {
        if (this.mIsAttachedToWindow) {
            if (this.mOnPreDrawListener == null) {
                this.mOnPreDrawListener = new OnPreDrawListener();
            }
            getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
        }
        this.mNeedsPreDrawListener = true;
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return (p instanceof LayoutParams) && super.checkLayoutParams(p);
    }

    public void dispatchDependentViewsChanged(View view) {
        List incomingEdges = this.mChildDag.getIncomingEdges(view);
        if (incomingEdges != null && !incomingEdges.isEmpty()) {
            for (int i = 0; i < incomingEdges.size(); i++) {
                View view2 = (View) incomingEdges.get(i);
                Behavior behavior = ((LayoutParams) view2.getLayoutParams()).getBehavior();
                if (behavior != null) {
                    behavior.onDependentViewChanged(this, view2, view);
                }
            }
        }
    }

    public boolean doViewsOverlap(View first, View second) {
        boolean z = false;
        if (first.getVisibility() != 0 || second.getVisibility() != 0) {
            return false;
        }
        Rect acquireTempRect = acquireTempRect();
        getChildRect(first, first.getParent() != this, acquireTempRect);
        Rect acquireTempRect2 = acquireTempRect();
        getChildRect(second, second.getParent() != this, acquireTempRect2);
        try {
            if (acquireTempRect.left <= acquireTempRect2.right && acquireTempRect.top <= acquireTempRect2.bottom && acquireTempRect.right >= acquireTempRect2.left && acquireTempRect.bottom >= acquireTempRect2.top) {
                z = true;
            }
            return z;
        } finally {
            releaseTempRect(acquireTempRect);
            releaseTempRect(acquireTempRect2);
        }
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        if (layoutParams.mBehavior != null) {
            float scrimOpacity = layoutParams.mBehavior.getScrimOpacity(this, child);
            if (scrimOpacity > 0.0f) {
                if (this.mScrimPaint == null) {
                    this.mScrimPaint = new Paint();
                }
                this.mScrimPaint.setColor(layoutParams.mBehavior.getScrimColor(this, child));
                this.mScrimPaint.setAlpha(clamp(Math.round(255.0f * scrimOpacity), 0, 255));
                int save = canvas.save();
                if (child.isOpaque()) {
                    canvas.clipRect((float) child.getLeft(), (float) child.getTop(), (float) child.getRight(), (float) child.getBottom(), Region.Op.DIFFERENCE);
                }
                canvas.drawRect((float) getPaddingLeft(), (float) getPaddingTop(), (float) (getWidth() - getPaddingRight()), (float) (getHeight() - getPaddingBottom()), this.mScrimPaint);
                canvas.restoreToCount(save);
            }
        }
        return super.drawChild(canvas, child, drawingTime);
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        boolean z = false;
        Drawable drawable = this.mStatusBarBackground;
        if (drawable != null && drawable.isStateful()) {
            z = false | drawable.setState(drawableState);
        }
        if (z) {
            invalidate();
        }
    }

    /* access modifiers changed from: package-private */
    public void ensurePreDrawListener() {
        boolean z = false;
        int childCount = getChildCount();
        int i = 0;
        while (true) {
            if (i >= childCount) {
                break;
            } else if (hasDependencies(getChildAt(i))) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        if (z == this.mNeedsPreDrawListener) {
            return;
        }
        if (z) {
            addPreDrawListener();
        } else {
            removePreDrawListener();
        }
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams ? new LayoutParams((LayoutParams) p) : p instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams) p) : new LayoutParams(p);
    }

    /* access modifiers changed from: package-private */
    public void getChildRect(View child, boolean transform, Rect out) {
        if (child.isLayoutRequested() || child.getVisibility() == 8) {
            out.setEmpty();
        } else if (transform) {
            getDescendantRect(child, out);
        } else {
            out.set(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
        }
    }

    public List<View> getDependencies(View child) {
        List<View> outgoingEdges = this.mChildDag.getOutgoingEdges(child);
        this.mTempDependenciesList.clear();
        if (outgoingEdges != null) {
            this.mTempDependenciesList.addAll(outgoingEdges);
        }
        return this.mTempDependenciesList;
    }

    /* access modifiers changed from: package-private */
    public final List<View> getDependencySortedChildren() {
        prepareChildren();
        return Collections.unmodifiableList(this.mDependencySortedChildren);
    }

    public List<View> getDependents(View child) {
        List incomingEdges = this.mChildDag.getIncomingEdges(child);
        this.mTempDependenciesList.clear();
        if (incomingEdges != null) {
            this.mTempDependenciesList.addAll(incomingEdges);
        }
        return this.mTempDependenciesList;
    }

    /* access modifiers changed from: package-private */
    public void getDescendantRect(View descendant, Rect out) {
        ViewGroupUtils.getDescendantRect(this, descendant, out);
    }

    /* access modifiers changed from: package-private */
    public void getDesiredAnchoredChildRect(View child, int layoutDirection, Rect anchorRect, Rect out) {
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        int measuredWidth = child.getMeasuredWidth();
        int measuredHeight = child.getMeasuredHeight();
        getDesiredAnchoredChildRectWithoutConstraints(child, layoutDirection, anchorRect, out, layoutParams, measuredWidth, measuredHeight);
        constrainChildRect(layoutParams, out, measuredWidth, measuredHeight);
    }

    /* access modifiers changed from: package-private */
    public void getLastChildRect(View child, Rect out) {
        out.set(((LayoutParams) child.getLayoutParams()).getLastChildRect());
    }

    public final WindowInsetsCompat getLastWindowInsets() {
        return this.mLastInsets;
    }

    public int getNestedScrollAxes() {
        return this.mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    /* access modifiers changed from: package-private */
    public LayoutParams getResolvedLayoutParams(View child) {
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        if (!layoutParams.mBehaviorResolved) {
            if (child instanceof AttachedBehavior) {
                Behavior behavior = ((AttachedBehavior) child).getBehavior();
                if (behavior == null) {
                    Log.e(TAG, "Attached behavior class is null");
                }
                layoutParams.setBehavior(behavior);
                layoutParams.mBehaviorResolved = true;
            } else {
                DefaultBehavior defaultBehavior = null;
                for (Class cls = child.getClass(); cls != null; cls = cls.getSuperclass()) {
                    DefaultBehavior defaultBehavior2 = (DefaultBehavior) cls.getAnnotation(DefaultBehavior.class);
                    defaultBehavior = defaultBehavior2;
                    if (defaultBehavior2 != null) {
                        break;
                    }
                }
                if (defaultBehavior != null) {
                    try {
                        layoutParams.setBehavior((Behavior) defaultBehavior.value().getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
                    } catch (Exception e) {
                        Log.e(TAG, "Default behavior class " + defaultBehavior.value().getName() + " could not be instantiated. Did you forget a default constructor?", e);
                    }
                }
                layoutParams.mBehaviorResolved = true;
            }
        }
        return layoutParams;
    }

    public Drawable getStatusBarBackground() {
        return this.mStatusBarBackground;
    }

    /* access modifiers changed from: protected */
    public int getSuggestedMinimumHeight() {
        return Math.max(super.getSuggestedMinimumHeight(), getPaddingTop() + getPaddingBottom());
    }

    /* access modifiers changed from: protected */
    public int getSuggestedMinimumWidth() {
        return Math.max(super.getSuggestedMinimumWidth(), getPaddingLeft() + getPaddingRight());
    }

    public boolean isPointInChildBounds(View child, int x, int y) {
        Rect acquireTempRect = acquireTempRect();
        getDescendantRect(child, acquireTempRect);
        try {
            return acquireTempRect.contains(x, y);
        } finally {
            releaseTempRect(acquireTempRect);
        }
    }

    /* access modifiers changed from: package-private */
    public void offsetChildToAnchor(View child, int layoutDirection) {
        Behavior behavior;
        View view = child;
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        if (layoutParams.mAnchorView != null) {
            Rect acquireTempRect = acquireTempRect();
            Rect acquireTempRect2 = acquireTempRect();
            Rect acquireTempRect3 = acquireTempRect();
            getDescendantRect(layoutParams.mAnchorView, acquireTempRect);
            boolean z = false;
            getChildRect(view, false, acquireTempRect2);
            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();
            int i = measuredHeight;
            getDesiredAnchoredChildRectWithoutConstraints(child, layoutDirection, acquireTempRect, acquireTempRect3, layoutParams, measuredWidth, measuredHeight);
            if (!(acquireTempRect3.left == acquireTempRect2.left && acquireTempRect3.top == acquireTempRect2.top)) {
                z = true;
            }
            boolean z2 = z;
            constrainChildRect(layoutParams, acquireTempRect3, measuredWidth, i);
            int i2 = acquireTempRect3.left - acquireTempRect2.left;
            int i3 = acquireTempRect3.top - acquireTempRect2.top;
            if (i2 != 0) {
                ViewCompat.offsetLeftAndRight(view, i2);
            }
            if (i3 != 0) {
                ViewCompat.offsetTopAndBottom(view, i3);
            }
            if (z2 && (behavior = layoutParams.getBehavior()) != null) {
                behavior.onDependentViewChanged(this, view, layoutParams.mAnchorView);
            }
            releaseTempRect(acquireTempRect);
            releaseTempRect(acquireTempRect2);
            releaseTempRect(acquireTempRect3);
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        resetTouchBehaviors(false);
        if (this.mNeedsPreDrawListener) {
            if (this.mOnPreDrawListener == null) {
                this.mOnPreDrawListener = new OnPreDrawListener();
            }
            getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
        }
        if (this.mLastInsets == null && ViewCompat.getFitsSystemWindows(this)) {
            ViewCompat.requestApplyInsets(this);
        }
        this.mIsAttachedToWindow = true;
    }

    /* access modifiers changed from: package-private */
    public final void onChildViewsChanged(int type) {
        boolean z;
        int layoutDirection = ViewCompat.getLayoutDirection(this);
        int size = this.mDependencySortedChildren.size();
        Rect acquireTempRect = acquireTempRect();
        Rect acquireTempRect2 = acquireTempRect();
        Rect acquireTempRect3 = acquireTempRect();
        for (int i = 0; i < size; i++) {
            View view = this.mDependencySortedChildren.get(i);
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            if (type != 0 || view.getVisibility() != 8) {
                for (int i2 = 0; i2 < i; i2++) {
                    if (layoutParams.mAnchorDirectChild == this.mDependencySortedChildren.get(i2)) {
                        offsetChildToAnchor(view, layoutDirection);
                    }
                }
                getChildRect(view, true, acquireTempRect2);
                if (layoutParams.insetEdge != 0 && !acquireTempRect2.isEmpty()) {
                    int absoluteGravity = GravityCompat.getAbsoluteGravity(layoutParams.insetEdge, layoutDirection);
                    switch (absoluteGravity & 112) {
                        case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE:
                            acquireTempRect.top = Math.max(acquireTempRect.top, acquireTempRect2.bottom);
                            break;
                        case 80:
                            acquireTempRect.bottom = Math.max(acquireTempRect.bottom, getHeight() - acquireTempRect2.top);
                            break;
                    }
                    switch (absoluteGravity & 7) {
                        case 3:
                            acquireTempRect.left = Math.max(acquireTempRect.left, acquireTempRect2.right);
                            break;
                        case 5:
                            acquireTempRect.right = Math.max(acquireTempRect.right, getWidth() - acquireTempRect2.left);
                            break;
                    }
                }
                if (layoutParams.dodgeInsetEdges != 0 && view.getVisibility() == 0) {
                    offsetChildByInset(view, acquireTempRect, layoutDirection);
                }
                if (type != 2) {
                    getLastChildRect(view, acquireTempRect3);
                    if (!acquireTempRect3.equals(acquireTempRect2)) {
                        recordLastChildRect(view, acquireTempRect2);
                    }
                }
                for (int i3 = i + 1; i3 < size; i3++) {
                    View view2 = this.mDependencySortedChildren.get(i3);
                    LayoutParams layoutParams2 = (LayoutParams) view2.getLayoutParams();
                    Behavior behavior = layoutParams2.getBehavior();
                    if (behavior != null && behavior.layoutDependsOn(this, view2, view)) {
                        if (type != 0 || !layoutParams2.getChangedAfterNestedScroll()) {
                            switch (type) {
                                case 2:
                                    behavior.onDependentViewRemoved(this, view2, view);
                                    z = true;
                                    break;
                                default:
                                    z = behavior.onDependentViewChanged(this, view2, view);
                                    break;
                            }
                            if (type == 1) {
                                layoutParams2.setChangedAfterNestedScroll(z);
                            }
                        } else {
                            layoutParams2.resetChangedAfterNestedScroll();
                        }
                    }
                }
            }
        }
        releaseTempRect(acquireTempRect);
        releaseTempRect(acquireTempRect2);
        releaseTempRect(acquireTempRect3);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        resetTouchBehaviors(false);
        if (this.mNeedsPreDrawListener && this.mOnPreDrawListener != null) {
            getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
        }
        View view = this.mNestedScrollingTarget;
        if (view != null) {
            onStopNestedScroll(view);
        }
        this.mIsAttachedToWindow = false;
    }

    public void onDraw(Canvas c) {
        super.onDraw(c);
        if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
            WindowInsetsCompat windowInsetsCompat = this.mLastInsets;
            int systemWindowInsetTop = windowInsetsCompat != null ? windowInsetsCompat.getSystemWindowInsetTop() : 0;
            if (systemWindowInsetTop > 0) {
                this.mStatusBarBackground.setBounds(0, 0, getWidth(), systemWindowInsetTop);
                this.mStatusBarBackground.draw(c);
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int actionMasked = ev.getActionMasked();
        if (actionMasked == 0) {
            resetTouchBehaviors(true);
        }
        boolean performIntercept = performIntercept(ev, 0);
        if (actionMasked == 1 || actionMasked == 3) {
            resetTouchBehaviors(true);
        }
        return performIntercept;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        Behavior behavior;
        int layoutDirection = ViewCompat.getLayoutDirection(this);
        int size = this.mDependencySortedChildren.size();
        for (int i = 0; i < size; i++) {
            View view = this.mDependencySortedChildren.get(i);
            if (view.getVisibility() != 8 && ((behavior = ((LayoutParams) view.getLayoutParams()).getBehavior()) == null || !behavior.onLayoutChild(this, view, layoutDirection))) {
                onLayoutChild(view, layoutDirection);
            }
        }
    }

    public void onLayoutChild(View child, int layoutDirection) {
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        if (layoutParams.checkAnchorChanged()) {
            throw new IllegalStateException("An anchor may not be changed after CoordinatorLayout measurement begins before layout is complete.");
        } else if (layoutParams.mAnchorView != null) {
            layoutChildWithAnchor(child, layoutParams.mAnchorView, layoutDirection);
        } else if (layoutParams.keyline >= 0) {
            layoutChildWithKeyline(child, layoutParams.keyline, layoutDirection);
        } else {
            layoutChild(child, layoutDirection);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x013b, code lost:
        if (r29.onMeasureChild(r35, r21, r26, r23, r28, 0) == false) goto L_0x014c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMeasure(int r36, int r37) {
        /*
            r35 = this;
            r7 = r35
            r35.prepareChildren()
            r35.ensurePreDrawListener()
            int r8 = r35.getPaddingLeft()
            int r9 = r35.getPaddingTop()
            int r10 = r35.getPaddingRight()
            int r11 = r35.getPaddingBottom()
            int r12 = androidx.core.view.ViewCompat.getLayoutDirection(r35)
            r0 = 1
            if (r12 != r0) goto L_0x0021
            r1 = r0
            goto L_0x0022
        L_0x0021:
            r1 = 0
        L_0x0022:
            r14 = r1
            int r15 = android.view.View.MeasureSpec.getMode(r36)
            int r16 = android.view.View.MeasureSpec.getSize(r36)
            int r6 = android.view.View.MeasureSpec.getMode(r37)
            int r17 = android.view.View.MeasureSpec.getSize(r37)
            int r18 = r8 + r10
            int r19 = r9 + r11
            int r1 = r35.getSuggestedMinimumWidth()
            int r2 = r35.getSuggestedMinimumHeight()
            r3 = 0
            androidx.core.view.WindowInsetsCompat r4 = r7.mLastInsets
            if (r4 == 0) goto L_0x004b
            boolean r4 = androidx.core.view.ViewCompat.getFitsSystemWindows(r35)
            if (r4 == 0) goto L_0x004b
            goto L_0x004c
        L_0x004b:
            r0 = 0
        L_0x004c:
            r20 = r0
            java.util.List<android.view.View> r0 = r7.mDependencySortedChildren
            int r5 = r0.size()
            r0 = 0
            r4 = r0
            r34 = r3
            r3 = r1
            r1 = r34
        L_0x005b:
            if (r4 >= r5) goto L_0x0196
            java.util.List<android.view.View> r0 = r7.mDependencySortedChildren
            java.lang.Object r0 = r0.get(r4)
            r21 = r0
            android.view.View r21 = (android.view.View) r21
            int r0 = r21.getVisibility()
            r13 = 8
            if (r0 != r13) goto L_0x0079
            r24 = r4
            r25 = r5
            r27 = r6
            r22 = 0
            goto L_0x018e
        L_0x0079:
            android.view.ViewGroup$LayoutParams r0 = r21.getLayoutParams()
            r13 = r0
            androidx.coordinatorlayout.widget.CoordinatorLayout$LayoutParams r13 = (androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams) r13
            r0 = 0
            r23 = r0
            int r0 = r13.keyline
            if (r0 < 0) goto L_0x00cd
            if (r15 == 0) goto L_0x00cd
            int r0 = r13.keyline
            int r0 = r7.getKeyline(r0)
            r24 = r1
            int r1 = r13.gravity
            int r1 = resolveKeylineGravity(r1)
            int r1 = androidx.core.view.GravityCompat.getAbsoluteGravity(r1, r12)
            r1 = r1 & 7
            r25 = r2
            r2 = 3
            if (r1 != r2) goto L_0x00a4
            if (r14 == 0) goto L_0x00a9
        L_0x00a4:
            r2 = 5
            if (r1 != r2) goto L_0x00b6
            if (r14 == 0) goto L_0x00b6
        L_0x00a9:
            int r2 = r16 - r10
            int r2 = r2 - r0
            r27 = r3
            r3 = 0
            int r2 = java.lang.Math.max(r3, r2)
            r23 = r2
            goto L_0x00d4
        L_0x00b6:
            r27 = r3
            if (r1 != r2) goto L_0x00bc
            if (r14 == 0) goto L_0x00c1
        L_0x00bc:
            r2 = 3
            if (r1 != r2) goto L_0x00cb
            if (r14 == 0) goto L_0x00cb
        L_0x00c1:
            int r2 = r0 - r8
            r3 = 0
            int r2 = java.lang.Math.max(r3, r2)
            r23 = r2
            goto L_0x00d4
        L_0x00cb:
            r3 = 0
            goto L_0x00d4
        L_0x00cd:
            r24 = r1
            r25 = r2
            r27 = r3
            r3 = 0
        L_0x00d4:
            r0 = r36
            r1 = r37
            if (r20 == 0) goto L_0x010d
            boolean r2 = androidx.core.view.ViewCompat.getFitsSystemWindows(r21)
            if (r2 != 0) goto L_0x010d
            androidx.core.view.WindowInsetsCompat r2 = r7.mLastInsets
            int r2 = r2.getSystemWindowInsetLeft()
            androidx.core.view.WindowInsetsCompat r3 = r7.mLastInsets
            int r3 = r3.getSystemWindowInsetRight()
            int r2 = r2 + r3
            androidx.core.view.WindowInsetsCompat r3 = r7.mLastInsets
            int r3 = r3.getSystemWindowInsetTop()
            r26 = r0
            androidx.core.view.WindowInsetsCompat r0 = r7.mLastInsets
            int r0 = r0.getSystemWindowInsetBottom()
            int r3 = r3 + r0
            int r0 = r16 - r2
            int r0 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r15)
            r26 = r0
            int r0 = r17 - r3
            int r1 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r6)
            r28 = r1
            goto L_0x0111
        L_0x010d:
            r26 = r0
            r28 = r1
        L_0x0111:
            androidx.coordinatorlayout.widget.CoordinatorLayout$Behavior r29 = r13.getBehavior()
            if (r29 == 0) goto L_0x013e
            r30 = 0
            r0 = r29
            r3 = r24
            r1 = r35
            r31 = r25
            r2 = r21
            r33 = r3
            r32 = r27
            r22 = 0
            r3 = r26
            r24 = r4
            r4 = r23
            r25 = r5
            r5 = r28
            r27 = r6
            r6 = r30
            boolean r0 = r0.onMeasureChild(r1, r2, r3, r4, r5, r6)
            if (r0 != 0) goto L_0x015a
            goto L_0x014c
        L_0x013e:
            r33 = r24
            r31 = r25
            r32 = r27
            r22 = 0
            r24 = r4
            r25 = r5
            r27 = r6
        L_0x014c:
            r5 = 0
            r0 = r35
            r1 = r21
            r2 = r26
            r3 = r23
            r4 = r28
            r0.onMeasureChild(r1, r2, r3, r4, r5)
        L_0x015a:
            int r0 = r21.getMeasuredWidth()
            int r0 = r18 + r0
            int r1 = r13.leftMargin
            int r0 = r0 + r1
            int r1 = r13.rightMargin
            int r0 = r0 + r1
            r1 = r32
            int r0 = java.lang.Math.max(r1, r0)
            int r1 = r21.getMeasuredHeight()
            int r1 = r19 + r1
            int r2 = r13.topMargin
            int r1 = r1 + r2
            int r2 = r13.bottomMargin
            int r1 = r1 + r2
            r2 = r31
            int r1 = java.lang.Math.max(r2, r1)
            int r2 = r21.getMeasuredState()
            r3 = r33
            int r2 = android.view.View.combineMeasuredStates(r3, r2)
            r3 = r0
            r34 = r2
            r2 = r1
            r1 = r34
        L_0x018e:
            int r4 = r24 + 1
            r5 = r25
            r6 = r27
            goto L_0x005b
        L_0x0196:
            r24 = r4
            r25 = r5
            r27 = r6
            r34 = r3
            r3 = r1
            r1 = r34
            r0 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r0 = r0 & r3
            r4 = r36
            int r0 = android.view.View.resolveSizeAndState(r1, r4, r0)
            int r5 = r3 << 16
            r6 = r37
            int r5 = android.view.View.resolveSizeAndState(r2, r6, r5)
            r7.setMeasuredDimension(r0, r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.coordinatorlayout.widget.CoordinatorLayout.onMeasure(int, int):void");
    }

    public void onMeasureChild(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Behavior behavior;
        int childCount = getChildCount();
        boolean z = false;
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.isNestedScrollAccepted(0) && (behavior = layoutParams.getBehavior()) != null) {
                    z = behavior.onNestedFling(this, childAt, target, velocityX, velocityY, consumed) | z;
                }
            }
        }
        if (z) {
            onChildViewsChanged(1);
        }
        return z;
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Behavior behavior;
        boolean z = false;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.isNestedScrollAccepted(0) && (behavior = layoutParams.getBehavior()) != null) {
                    z |= behavior.onNestedPreFling(this, childAt, target, velocityX, velocityY);
                }
            }
        }
        return z;
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        onNestedPreScroll(target, dx, dy, consumed, 0);
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed, int type) {
        int childCount = getChildCount();
        int i = 0;
        int i2 = 0;
        boolean z = false;
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.isNestedScrollAccepted(type)) {
                    Behavior behavior = layoutParams.getBehavior();
                    if (behavior != null) {
                        int[] iArr = this.mBehaviorConsumed;
                        iArr[0] = 0;
                        iArr[1] = 0;
                        LayoutParams layoutParams2 = layoutParams;
                        behavior.onNestedPreScroll(this, childAt, target, dx, dy, iArr, type);
                        int[] iArr2 = this.mBehaviorConsumed;
                        int max = dx > 0 ? Math.max(i, iArr2[0]) : Math.min(i, iArr2[0]);
                        int[] iArr3 = this.mBehaviorConsumed;
                        i = max;
                        i2 = dy > 0 ? Math.max(i2, iArr3[1]) : Math.min(i2, iArr3[1]);
                        z = true;
                    } else {
                        LayoutParams layoutParams3 = layoutParams;
                    }
                }
            }
        }
        consumed[0] = i;
        consumed[1] = i2;
        if (z) {
            onChildViewsChanged(1);
        }
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, 0);
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, 0, this.mNestedScrollingV2ConsumedCompat);
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int[] consumed) {
        int i;
        int childCount = getChildCount();
        boolean z = false;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i4 < childCount) {
            View childAt = getChildAt(i4);
            if (childAt.getVisibility() == 8) {
                i = childCount;
            } else {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (!layoutParams.isNestedScrollAccepted(type)) {
                    i = childCount;
                } else {
                    Behavior behavior = layoutParams.getBehavior();
                    if (behavior != null) {
                        int[] iArr = this.mBehaviorConsumed;
                        iArr[0] = 0;
                        iArr[1] = 0;
                        LayoutParams layoutParams2 = layoutParams;
                        i = childCount;
                        behavior.onNestedScroll(this, childAt, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, iArr);
                        int[] iArr2 = this.mBehaviorConsumed;
                        int max = dxUnconsumed > 0 ? Math.max(i2, iArr2[0]) : Math.min(i2, iArr2[0]);
                        int[] iArr3 = this.mBehaviorConsumed;
                        i2 = max;
                        i3 = dyUnconsumed > 0 ? Math.max(i3, iArr3[1]) : Math.min(i3, iArr3[1]);
                        z = true;
                    } else {
                        LayoutParams layoutParams3 = layoutParams;
                        i = childCount;
                    }
                }
            }
            i4++;
            childCount = i;
        }
        int i5 = childCount;
        consumed[0] = consumed[0] + i2;
        consumed[1] = consumed[1] + i3;
        if (z) {
            onChildViewsChanged(1);
        }
    }

    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        onNestedScrollAccepted(child, target, nestedScrollAxes, 0);
    }

    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes, int type) {
        Behavior behavior;
        View view = target;
        int i = type;
        this.mNestedScrollingParentHelper.onNestedScrollAccepted(child, view, nestedScrollAxes, i);
        this.mNestedScrollingTarget = view;
        int childCount = getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            if (layoutParams.isNestedScrollAccepted(i) && (behavior = layoutParams.getBehavior()) != null) {
                behavior.onNestedScrollAccepted(this, childAt, child, target, nestedScrollAxes, type);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        Parcelable parcelable;
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        SparseArray<Parcelable> sparseArray = savedState.behaviorStates;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            int id = childAt.getId();
            Behavior behavior = getResolvedLayoutParams(childAt).getBehavior();
            if (!(id == -1 || behavior == null || (parcelable = sparseArray.get(id)) == null)) {
                behavior.onRestoreInstanceState(this, childAt, parcelable);
            }
        }
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState;
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        SparseArray<Parcelable> sparseArray = new SparseArray<>();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            int id = childAt.getId();
            Behavior behavior = ((LayoutParams) childAt.getLayoutParams()).getBehavior();
            if (!(id == -1 || behavior == null || (onSaveInstanceState = behavior.onSaveInstanceState(this, childAt)) == null)) {
                sparseArray.append(id, onSaveInstanceState);
            }
        }
        savedState.behaviorStates = sparseArray;
        return savedState;
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return onStartNestedScroll(child, target, nestedScrollAxes, 0);
    }

    public boolean onStartNestedScroll(View child, View target, int axes, int type) {
        int i = type;
        int childCount = getChildCount();
        boolean z = false;
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                Behavior behavior = layoutParams.getBehavior();
                if (behavior != null) {
                    boolean onStartNestedScroll = behavior.onStartNestedScroll(this, childAt, child, target, axes, type);
                    layoutParams.setNestedScrollAccepted(i, onStartNestedScroll);
                    z |= onStartNestedScroll;
                } else {
                    layoutParams.setNestedScrollAccepted(i, false);
                }
            }
        }
        return z;
    }

    public void onStopNestedScroll(View target) {
        onStopNestedScroll(target, 0);
    }

    public void onStopNestedScroll(View target, int type) {
        this.mNestedScrollingParentHelper.onStopNestedScroll(target, type);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            if (layoutParams.isNestedScrollAccepted(type)) {
                Behavior behavior = layoutParams.getBehavior();
                if (behavior != null) {
                    behavior.onStopNestedScroll(this, childAt, target, type);
                }
                layoutParams.resetNestedScroll(type);
                layoutParams.resetChangedAfterNestedScroll();
            }
        }
        this.mNestedScrollingTarget = null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0015, code lost:
        if (r6 != false) goto L_0x0017;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r19) {
        /*
            r18 = this;
            r0 = r18
            r1 = r19
            r2 = 0
            r3 = 0
            r4 = 0
            int r5 = r19.getActionMasked()
            android.view.View r6 = r0.mBehaviorTouchView
            r7 = 1
            if (r6 != 0) goto L_0x0017
            boolean r6 = r0.performIntercept(r1, r7)
            r3 = r6
            if (r6 == 0) goto L_0x002b
        L_0x0017:
            android.view.View r6 = r0.mBehaviorTouchView
            android.view.ViewGroup$LayoutParams r6 = r6.getLayoutParams()
            androidx.coordinatorlayout.widget.CoordinatorLayout$LayoutParams r6 = (androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams) r6
            androidx.coordinatorlayout.widget.CoordinatorLayout$Behavior r8 = r6.getBehavior()
            if (r8 == 0) goto L_0x002b
            android.view.View r9 = r0.mBehaviorTouchView
            boolean r2 = r8.onTouchEvent(r0, r9, r1)
        L_0x002b:
            android.view.View r6 = r0.mBehaviorTouchView
            if (r6 != 0) goto L_0x0035
            boolean r6 = super.onTouchEvent(r19)
            r2 = r2 | r6
            goto L_0x004c
        L_0x0035:
            if (r3 == 0) goto L_0x004c
            if (r4 != 0) goto L_0x0049
            long r16 = android.os.SystemClock.uptimeMillis()
            r12 = 3
            r13 = 0
            r14 = 0
            r15 = 0
            r8 = r16
            r10 = r16
            android.view.MotionEvent r4 = android.view.MotionEvent.obtain(r8, r10, r12, r13, r14, r15)
        L_0x0049:
            super.onTouchEvent(r4)
        L_0x004c:
            if (r4 == 0) goto L_0x0051
            r4.recycle()
        L_0x0051:
            if (r5 == r7) goto L_0x0056
            r6 = 3
            if (r5 != r6) goto L_0x005a
        L_0x0056:
            r6 = 0
            r0.resetTouchBehaviors(r6)
        L_0x005a:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.coordinatorlayout.widget.CoordinatorLayout.onTouchEvent(android.view.MotionEvent):boolean");
    }

    /* access modifiers changed from: package-private */
    public void recordLastChildRect(View child, Rect r) {
        ((LayoutParams) child.getLayoutParams()).setLastChildRect(r);
    }

    /* access modifiers changed from: package-private */
    public void removePreDrawListener() {
        if (this.mIsAttachedToWindow && this.mOnPreDrawListener != null) {
            getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
        }
        this.mNeedsPreDrawListener = false;
    }

    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        Behavior behavior = ((LayoutParams) child.getLayoutParams()).getBehavior();
        if (behavior == null || !behavior.onRequestChildRectangleOnScreen(this, child, rectangle, immediate)) {
            return super.requestChildRectangleOnScreen(child, rectangle, immediate);
        }
        return true;
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
        if (disallowIntercept && !this.mDisallowInterceptReset) {
            resetTouchBehaviors(false);
            this.mDisallowInterceptReset = true;
        }
    }

    public void setFitsSystemWindows(boolean fitSystemWindows) {
        super.setFitsSystemWindows(fitSystemWindows);
        setupForInsets();
    }

    public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener) {
        this.mOnHierarchyChangeListener = onHierarchyChangeListener;
    }

    public void setStatusBarBackground(Drawable bg) {
        Drawable drawable = this.mStatusBarBackground;
        if (drawable != bg) {
            Drawable drawable2 = null;
            if (drawable != null) {
                drawable.setCallback((Drawable.Callback) null);
            }
            if (bg != null) {
                drawable2 = bg.mutate();
            }
            this.mStatusBarBackground = drawable2;
            if (drawable2 != null) {
                if (drawable2.isStateful()) {
                    this.mStatusBarBackground.setState(getDrawableState());
                }
                DrawableCompat.setLayoutDirection(this.mStatusBarBackground, ViewCompat.getLayoutDirection(this));
                this.mStatusBarBackground.setVisible(getVisibility() == 0, false);
                this.mStatusBarBackground.setCallback(this);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setStatusBarBackgroundColor(int color) {
        setStatusBarBackground(new ColorDrawable(color));
    }

    public void setStatusBarBackgroundResource(int resId) {
        setStatusBarBackground(resId != 0 ? ContextCompat.getDrawable(getContext(), resId) : null);
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        boolean z = visibility == 0;
        Drawable drawable = this.mStatusBarBackground;
        if (drawable != null && drawable.isVisible() != z) {
            this.mStatusBarBackground.setVisible(z, false);
        }
    }

    /* access modifiers changed from: package-private */
    public final WindowInsetsCompat setWindowInsets(WindowInsetsCompat insets) {
        if (ObjectsCompat.equals(this.mLastInsets, insets)) {
            return insets;
        }
        this.mLastInsets = insets;
        boolean z = true;
        boolean z2 = insets != null && insets.getSystemWindowInsetTop() > 0;
        this.mDrawStatusBarBackground = z2;
        if (z2 || getBackground() != null) {
            z = false;
        }
        setWillNotDraw(z);
        WindowInsetsCompat insets2 = dispatchApplyWindowInsetsToBehaviors(insets);
        requestLayout();
        return insets2;
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mStatusBarBackground;
    }
}

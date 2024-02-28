package androidx.drawerlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.Openable;
import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.R;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.CharCompanionObject;
import mt.Log1F380D;

/* compiled from: 006D */
public class DrawerLayout extends ViewGroup implements Openable {
    private static final String ACCESSIBILITY_CLASS_NAME = "androidx.drawerlayout.widget.DrawerLayout";
    private static final boolean ALLOW_EDGE_LOCK = false;
    static final boolean CAN_HIDE_DESCENDANTS = (Build.VERSION.SDK_INT >= 19);
    private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
    private static final int DEFAULT_SCRIM_COLOR = -1728053248;
    static final int[] LAYOUT_ATTRS = {16842931};
    public static final int LOCK_MODE_LOCKED_CLOSED = 1;
    public static final int LOCK_MODE_LOCKED_OPEN = 2;
    public static final int LOCK_MODE_UNDEFINED = 3;
    public static final int LOCK_MODE_UNLOCKED = 0;
    private static final int MIN_DRAWER_MARGIN = 64;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int PEEK_DELAY = 160;
    private static final boolean SET_DRAWER_SHADOW_FROM_ELEVATION = (Build.VERSION.SDK_INT >= 21);
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "DrawerLayout";
    private static final int[] THEME_ATTRS = {16843828};
    private static final float TOUCH_SLOP_SENSITIVITY = 1.0f;
    private static boolean sEdgeSizeUsingSystemGestureInsets;
    private final AccessibilityViewCommand mActionDismiss;
    private final ChildAccessibilityDelegate mChildAccessibilityDelegate;
    private Rect mChildHitRect;
    private Matrix mChildInvertedMatrix;
    private boolean mChildrenCanceledTouch;
    private boolean mDrawStatusBarBackground;
    private float mDrawerElevation;
    private int mDrawerState;
    private boolean mFirstLayout;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private Object mLastInsets;
    private final ViewDragCallback mLeftCallback;
    private final ViewDragHelper mLeftDragger;
    private DrawerListener mListener;
    private List<DrawerListener> mListeners;
    private int mLockModeEnd;
    private int mLockModeLeft;
    private int mLockModeRight;
    private int mLockModeStart;
    private int mMinDrawerMargin;
    private final ArrayList<View> mNonDrawerViews;
    private final ViewDragCallback mRightCallback;
    private final ViewDragHelper mRightDragger;
    private int mScrimColor;
    private float mScrimOpacity;
    private Paint mScrimPaint;
    private Drawable mShadowEnd;
    private Drawable mShadowLeft;
    private Drawable mShadowLeftResolved;
    private Drawable mShadowRight;
    private Drawable mShadowRightResolved;
    private Drawable mShadowStart;
    private Drawable mStatusBarBackground;
    private CharSequence mTitleLeft;
    private CharSequence mTitleRight;

    class AccessibilityDelegate extends AccessibilityDelegateCompat {
        private final Rect mTmpRect = new Rect();

        AccessibilityDelegate() {
        }

        private void addChildrenForAccessibility(AccessibilityNodeInfoCompat info, ViewGroup v) {
            int childCount = v.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = v.getChildAt(i);
                if (DrawerLayout.includeChildForAccessibility(childAt)) {
                    info.addChild(childAt);
                }
            }
        }

        private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat dest, AccessibilityNodeInfoCompat src) {
            Rect rect = this.mTmpRect;
            src.getBoundsInScreen(rect);
            dest.setBoundsInScreen(rect);
            dest.setVisibleToUser(src.isVisibleToUser());
            dest.setPackageName(src.getPackageName());
            dest.setClassName(src.getClassName());
            dest.setContentDescription(src.getContentDescription());
            dest.setEnabled(src.isEnabled());
            dest.setFocused(src.isFocused());
            dest.setAccessibilityFocused(src.isAccessibilityFocused());
            dest.setSelected(src.isSelected());
            dest.addAction(src.getActions());
        }

        public boolean dispatchPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
            CharSequence drawerTitle;
            if (event.getEventType() != 32) {
                return super.dispatchPopulateAccessibilityEvent(host, event);
            }
            List text = event.getText();
            View findVisibleDrawer = DrawerLayout.this.findVisibleDrawer();
            if (findVisibleDrawer == null || (drawerTitle = DrawerLayout.this.getDrawerTitle(DrawerLayout.this.getDrawerViewAbsoluteGravity(findVisibleDrawer))) == null) {
                return DrawerLayout.CHILDREN_DISALLOW_INTERCEPT;
            }
            text.add(drawerTitle);
            return DrawerLayout.CHILDREN_DISALLOW_INTERCEPT;
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(DrawerLayout.ACCESSIBILITY_CLASS_NAME);
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            if (DrawerLayout.CAN_HIDE_DESCENDANTS) {
                super.onInitializeAccessibilityNodeInfo(host, info);
            } else {
                AccessibilityNodeInfoCompat obtain = AccessibilityNodeInfoCompat.obtain(info);
                super.onInitializeAccessibilityNodeInfo(host, obtain);
                info.setSource(host);
                ViewParent parentForAccessibility = ViewCompat.getParentForAccessibility(host);
                if (parentForAccessibility instanceof View) {
                    info.setParent((View) parentForAccessibility);
                }
                copyNodeInfoNoChildren(info, obtain);
                obtain.recycle();
                addChildrenForAccessibility(info, (ViewGroup) host);
            }
            info.setClassName(DrawerLayout.ACCESSIBILITY_CLASS_NAME);
            info.setFocusable(false);
            info.setFocused(false);
            info.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_FOCUS);
            info.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLEAR_FOCUS);
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
            if (DrawerLayout.CAN_HIDE_DESCENDANTS || DrawerLayout.includeChildForAccessibility(child)) {
                return super.onRequestSendAccessibilityEvent(host, child, event);
            }
            return false;
        }
    }

    static final class ChildAccessibilityDelegate extends AccessibilityDelegateCompat {
        ChildAccessibilityDelegate() {
        }

        public void onInitializeAccessibilityNodeInfo(View child, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(child, info);
            if (!DrawerLayout.includeChildForAccessibility(child)) {
                info.setParent((View) null);
            }
        }
    }

    public interface DrawerListener {
        void onDrawerClosed(View view);

        void onDrawerOpened(View view);

        void onDrawerSlide(View view, float f);

        void onDrawerStateChanged(int i);
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        private static final int FLAG_IS_CLOSING = 4;
        private static final int FLAG_IS_OPENED = 1;
        private static final int FLAG_IS_OPENING = 2;
        public int gravity;
        boolean isPeeking;
        float onScreen;
        int openState;

        public LayoutParams(int width, int height) {
            super(width, height);
            this.gravity = 0;
        }

        public LayoutParams(int width, int height, int gravity2) {
            this(width, height);
            this.gravity = gravity2;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.gravity = 0;
            TypedArray obtainStyledAttributes = c.obtainStyledAttributes(attrs, DrawerLayout.LAYOUT_ATTRS);
            this.gravity = obtainStyledAttributes.getInt(0, 0);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            this.gravity = 0;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
            this.gravity = 0;
        }

        public LayoutParams(LayoutParams source) {
            super(source);
            this.gravity = 0;
            this.gravity = source.gravity;
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
        int lockModeEnd;
        int lockModeLeft;
        int lockModeRight;
        int lockModeStart;
        int openDrawerGravity = 0;

        public SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            this.openDrawerGravity = in.readInt();
            this.lockModeLeft = in.readInt();
            this.lockModeRight = in.readInt();
            this.lockModeStart = in.readInt();
            this.lockModeEnd = in.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.openDrawerGravity);
            dest.writeInt(this.lockModeLeft);
            dest.writeInt(this.lockModeRight);
            dest.writeInt(this.lockModeStart);
            dest.writeInt(this.lockModeEnd);
        }
    }

    public static abstract class SimpleDrawerListener implements DrawerListener {
        public void onDrawerClosed(View drawerView) {
        }

        public void onDrawerOpened(View drawerView) {
        }

        public void onDrawerSlide(View drawerView, float slideOffset) {
        }

        public void onDrawerStateChanged(int newState) {
        }
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {
        private final int mAbsGravity;
        private ViewDragHelper mDragger;
        private final Runnable mPeekRunnable = new Runnable() {
            public void run() {
                ViewDragCallback.this.peekDrawer();
            }
        };

        ViewDragCallback(int gravity) {
            this.mAbsGravity = gravity;
        }

        private void closeOtherDrawer() {
            int i = 3;
            if (this.mAbsGravity == 3) {
                i = 5;
            }
            View findDrawerWithGravity = DrawerLayout.this.findDrawerWithGravity(i);
            if (findDrawerWithGravity != null) {
                DrawerLayout.this.closeDrawer(findDrawerWithGravity);
            }
        }

        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(child, 3)) {
                return Math.max(-child.getWidth(), Math.min(left, 0));
            }
            int width = DrawerLayout.this.getWidth();
            return Math.max(width - child.getWidth(), Math.min(left, width));
        }

        public int clampViewPositionVertical(View child, int top, int dy) {
            return child.getTop();
        }

        public int getViewHorizontalDragRange(View child) {
            if (DrawerLayout.this.isDrawerView(child)) {
                return child.getWidth();
            }
            return 0;
        }

        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            View findDrawerWithGravity = (edgeFlags & 1) == 1 ? DrawerLayout.this.findDrawerWithGravity(3) : DrawerLayout.this.findDrawerWithGravity(5);
            if (findDrawerWithGravity != null && DrawerLayout.this.getDrawerLockMode(findDrawerWithGravity) == 0) {
                this.mDragger.captureChildView(findDrawerWithGravity, pointerId);
            }
        }

        public boolean onEdgeLock(int edgeFlags) {
            return false;
        }

        public void onEdgeTouched(int edgeFlags, int pointerId) {
            DrawerLayout.this.postDelayed(this.mPeekRunnable, 160);
        }

        public void onViewCaptured(View capturedChild, int activePointerId) {
            ((LayoutParams) capturedChild.getLayoutParams()).isPeeking = false;
            closeOtherDrawer();
        }

        public void onViewDragStateChanged(int state) {
            DrawerLayout.this.updateDrawerState(state, this.mDragger.getCapturedView());
        }

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            int width = changedView.getWidth();
            float width2 = DrawerLayout.this.checkDrawerViewAbsoluteGravity(changedView, 3) ? ((float) (width + left)) / ((float) width) : ((float) (DrawerLayout.this.getWidth() - left)) / ((float) width);
            DrawerLayout.this.setDrawerViewOffset(changedView, width2);
            changedView.setVisibility(width2 == 0.0f ? 4 : 0);
            DrawerLayout.this.invalidate();
        }

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int i;
            float drawerViewOffset = DrawerLayout.this.getDrawerViewOffset(releasedChild);
            int width = releasedChild.getWidth();
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(releasedChild, 3)) {
                i = (xvel > 0.0f || (xvel == 0.0f && drawerViewOffset > 0.5f)) ? 0 : -width;
            } else {
                int width2 = DrawerLayout.this.getWidth();
                i = (xvel < 0.0f || (xvel == 0.0f && drawerViewOffset > 0.5f)) ? width2 - width : width2;
            }
            this.mDragger.settleCapturedViewAt(i, releasedChild.getTop());
            DrawerLayout.this.invalidate();
        }

        /* access modifiers changed from: package-private */
        public void peekDrawer() {
            View view;
            int i;
            int edgeSize = this.mDragger.getEdgeSize();
            int i2 = 0;
            boolean z = this.mAbsGravity == 3;
            if (z) {
                view = DrawerLayout.this.findDrawerWithGravity(3);
                if (view != null) {
                    i2 = -view.getWidth();
                }
                i = i2 + edgeSize;
            } else {
                view = DrawerLayout.this.findDrawerWithGravity(5);
                i = DrawerLayout.this.getWidth() - edgeSize;
            }
            if (view == null) {
                return;
            }
            if (((z && view.getLeft() < i) || (!z && view.getLeft() > i)) && DrawerLayout.this.getDrawerLockMode(view) == 0) {
                this.mDragger.smoothSlideViewTo(view, i, view.getTop());
                ((LayoutParams) view.getLayoutParams()).isPeeking = DrawerLayout.CHILDREN_DISALLOW_INTERCEPT;
                DrawerLayout.this.invalidate();
                closeOtherDrawer();
                DrawerLayout.this.cancelChildViewTouch();
            }
        }

        public void removeCallbacks() {
            DrawerLayout.this.removeCallbacks(this.mPeekRunnable);
        }

        public void setDragger(ViewDragHelper dragger) {
            this.mDragger = dragger;
        }

        public boolean tryCaptureView(View child, int pointerId) {
            if (!DrawerLayout.this.isDrawerView(child) || !DrawerLayout.this.checkDrawerViewAbsoluteGravity(child, this.mAbsGravity) || DrawerLayout.this.getDrawerLockMode(child) != 0) {
                return false;
            }
            return DrawerLayout.CHILDREN_DISALLOW_INTERCEPT;
        }
    }

    static {
        boolean z = CHILDREN_DISALLOW_INTERCEPT;
        if (Build.VERSION.SDK_INT < 29) {
            z = false;
        }
        sEdgeSizeUsingSystemGestureInsets = z;
    }

    public DrawerLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public DrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.drawerLayoutStyle);
    }

    /* JADX INFO: finally extract failed */
    public DrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mChildAccessibilityDelegate = new ChildAccessibilityDelegate();
        this.mScrimColor = DEFAULT_SCRIM_COLOR;
        this.mScrimPaint = new Paint();
        this.mFirstLayout = CHILDREN_DISALLOW_INTERCEPT;
        this.mLockModeLeft = 3;
        this.mLockModeRight = 3;
        this.mLockModeStart = 3;
        this.mLockModeEnd = 3;
        this.mShadowStart = null;
        this.mShadowEnd = null;
        this.mShadowLeft = null;
        this.mShadowRight = null;
        this.mActionDismiss = new AccessibilityViewCommand() {
            public boolean perform(View view, AccessibilityViewCommand.CommandArguments arguments) {
                if (!DrawerLayout.this.isDrawerOpen(view) || DrawerLayout.this.getDrawerLockMode(view) == 2) {
                    return false;
                }
                DrawerLayout.this.closeDrawer(view);
                return DrawerLayout.CHILDREN_DISALLOW_INTERCEPT;
            }
        };
        setDescendantFocusability(262144);
        float f = getResources().getDisplayMetrics().density;
        this.mMinDrawerMargin = (int) ((64.0f * f) + 0.5f);
        float f2 = 400.0f * f;
        ViewDragCallback viewDragCallback = new ViewDragCallback(3);
        this.mLeftCallback = viewDragCallback;
        ViewDragCallback viewDragCallback2 = new ViewDragCallback(5);
        this.mRightCallback = viewDragCallback2;
        ViewDragHelper create = ViewDragHelper.create(this, 1.0f, viewDragCallback);
        this.mLeftDragger = create;
        create.setEdgeTrackingEnabled(1);
        create.setMinVelocity(f2);
        viewDragCallback.setDragger(create);
        ViewDragHelper create2 = ViewDragHelper.create(this, 1.0f, viewDragCallback2);
        this.mRightDragger = create2;
        create2.setEdgeTrackingEnabled(2);
        create2.setMinVelocity(f2);
        viewDragCallback2.setDragger(create2);
        setFocusableInTouchMode(CHILDREN_DISALLOW_INTERCEPT);
        ViewCompat.setImportantForAccessibility(this, 1);
        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate());
        setMotionEventSplittingEnabled(false);
        if (ViewCompat.getFitsSystemWindows(this)) {
            if (Build.VERSION.SDK_INT >= 21) {
                setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                    public WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
                        ((DrawerLayout) view).setChildInsets(insets, insets.getSystemWindowInsetTop() > 0 ? DrawerLayout.CHILDREN_DISALLOW_INTERCEPT : false);
                        return insets.consumeSystemWindowInsets();
                    }
                });
                setSystemUiVisibility(1280);
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes(THEME_ATTRS);
                try {
                    this.mStatusBarBackground = obtainStyledAttributes.getDrawable(0);
                } finally {
                    obtainStyledAttributes.recycle();
                }
            } else {
                this.mStatusBarBackground = null;
            }
        }
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attrs, R.styleable.DrawerLayout, defStyleAttr, 0);
        try {
            if (obtainStyledAttributes2.hasValue(R.styleable.DrawerLayout_elevation)) {
                this.mDrawerElevation = obtainStyledAttributes2.getDimension(R.styleable.DrawerLayout_elevation, 0.0f);
            } else {
                this.mDrawerElevation = getResources().getDimension(R.dimen.def_drawer_elevation);
            }
            obtainStyledAttributes2.recycle();
            this.mNonDrawerViews = new ArrayList<>();
        } catch (Throwable th) {
            obtainStyledAttributes2.recycle();
            throw th;
        }
    }

    private boolean dispatchTransformedGenericPointerEvent(MotionEvent event, View child) {
        if (!child.getMatrix().isIdentity()) {
            MotionEvent transformedMotionEvent = getTransformedMotionEvent(event, child);
            boolean dispatchGenericMotionEvent = child.dispatchGenericMotionEvent(transformedMotionEvent);
            transformedMotionEvent.recycle();
            return dispatchGenericMotionEvent;
        }
        float scrollX = (float) (getScrollX() - child.getLeft());
        float scrollY = (float) (getScrollY() - child.getTop());
        event.offsetLocation(scrollX, scrollY);
        boolean dispatchGenericMotionEvent2 = child.dispatchGenericMotionEvent(event);
        event.offsetLocation(-scrollX, -scrollY);
        return dispatchGenericMotionEvent2;
    }

    private MotionEvent getTransformedMotionEvent(MotionEvent event, View child) {
        MotionEvent obtain = MotionEvent.obtain(event);
        obtain.offsetLocation((float) (getScrollX() - child.getLeft()), (float) (getScrollY() - child.getTop()));
        Matrix matrix = child.getMatrix();
        if (!matrix.isIdentity()) {
            if (this.mChildInvertedMatrix == null) {
                this.mChildInvertedMatrix = new Matrix();
            }
            matrix.invert(this.mChildInvertedMatrix);
            obtain.transform(this.mChildInvertedMatrix);
        }
        return obtain;
    }

    static String gravityToString(int gravity) {
        if ((gravity & 3) == 3) {
            return "LEFT";
        }
        if ((gravity & 5) == 5) {
            return "RIGHT";
        }
        String hexString = Integer.toHexString(gravity);
        Log1F380D.a((Object) hexString);
        return hexString;
    }

    private static boolean hasOpaqueBackground(View v) {
        Drawable background = v.getBackground();
        if (background == null || background.getOpacity() != -1) {
            return false;
        }
        return CHILDREN_DISALLOW_INTERCEPT;
    }

    private boolean hasPeekingDrawer() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (((LayoutParams) getChildAt(i).getLayoutParams()).isPeeking) {
                return CHILDREN_DISALLOW_INTERCEPT;
            }
        }
        return false;
    }

    private boolean hasVisibleDrawer() {
        if (findVisibleDrawer() != null) {
            return CHILDREN_DISALLOW_INTERCEPT;
        }
        return false;
    }

    static boolean includeChildForAccessibility(View child) {
        if (ViewCompat.getImportantForAccessibility(child) == 4 || ViewCompat.getImportantForAccessibility(child) == 2) {
            return false;
        }
        return CHILDREN_DISALLOW_INTERCEPT;
    }

    private boolean isInBoundsOfChild(float x, float y, View child) {
        if (this.mChildHitRect == null) {
            this.mChildHitRect = new Rect();
        }
        child.getHitRect(this.mChildHitRect);
        return this.mChildHitRect.contains((int) x, (int) y);
    }

    private void mirror(Drawable drawable, int layoutDirection) {
        if (drawable != null && DrawableCompat.isAutoMirrored(drawable)) {
            DrawableCompat.setLayoutDirection(drawable, layoutDirection);
        }
    }

    private Drawable resolveLeftShadow() {
        int layoutDirection = ViewCompat.getLayoutDirection(this);
        if (layoutDirection == 0) {
            Drawable drawable = this.mShadowStart;
            if (drawable != null) {
                mirror(drawable, layoutDirection);
                return this.mShadowStart;
            }
        } else {
            Drawable drawable2 = this.mShadowEnd;
            if (drawable2 != null) {
                mirror(drawable2, layoutDirection);
                return this.mShadowEnd;
            }
        }
        return this.mShadowLeft;
    }

    private Drawable resolveRightShadow() {
        int layoutDirection = ViewCompat.getLayoutDirection(this);
        if (layoutDirection == 0) {
            Drawable drawable = this.mShadowEnd;
            if (drawable != null) {
                mirror(drawable, layoutDirection);
                return this.mShadowEnd;
            }
        } else {
            Drawable drawable2 = this.mShadowStart;
            if (drawable2 != null) {
                mirror(drawable2, layoutDirection);
                return this.mShadowStart;
            }
        }
        return this.mShadowRight;
    }

    private void resolveShadowDrawables() {
        if (!SET_DRAWER_SHADOW_FROM_ELEVATION) {
            this.mShadowLeftResolved = resolveLeftShadow();
            this.mShadowRightResolved = resolveRightShadow();
        }
    }

    private void updateChildAccessibilityAction(View child) {
        ViewCompat.removeAccessibilityAction(child, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_DISMISS.getId());
        if (isDrawerOpen(child) && getDrawerLockMode(child) != 2) {
            ViewCompat.replaceAccessibilityAction(child, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_DISMISS, (CharSequence) null, this.mActionDismiss);
        }
    }

    private void updateChildrenImportantForAccessibility(View drawerView, boolean isDrawerOpen) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if ((isDrawerOpen || isDrawerView(childAt)) && (!isDrawerOpen || childAt != drawerView)) {
                ViewCompat.setImportantForAccessibility(childAt, 4);
            } else {
                ViewCompat.setImportantForAccessibility(childAt, 1);
            }
        }
    }

    public void addDrawerListener(DrawerListener listener) {
        if (listener != null) {
            if (this.mListeners == null) {
                this.mListeners = new ArrayList();
            }
            this.mListeners.add(listener);
        }
    }

    public void addFocusables(ArrayList<View> arrayList, int direction, int focusableMode) {
        if (getDescendantFocusability() != 393216) {
            int childCount = getChildCount();
            boolean z = false;
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);
                if (!isDrawerView(childAt)) {
                    this.mNonDrawerViews.add(childAt);
                } else if (isDrawerOpen(childAt)) {
                    z = CHILDREN_DISALLOW_INTERCEPT;
                    childAt.addFocusables(arrayList, direction, focusableMode);
                }
            }
            if (!z) {
                int size = this.mNonDrawerViews.size();
                for (int i2 = 0; i2 < size; i2++) {
                    View view = this.mNonDrawerViews.get(i2);
                    if (view.getVisibility() == 0) {
                        view.addFocusables(arrayList, direction, focusableMode);
                    }
                }
            }
            this.mNonDrawerViews.clear();
        }
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (findOpenDrawer() != null || isDrawerView(child)) {
            ViewCompat.setImportantForAccessibility(child, 4);
        } else {
            ViewCompat.setImportantForAccessibility(child, 1);
        }
        if (!CAN_HIDE_DESCENDANTS) {
            ViewCompat.setAccessibilityDelegate(child, this.mChildAccessibilityDelegate);
        }
    }

    /* access modifiers changed from: package-private */
    public void cancelChildViewTouch() {
        if (!this.mChildrenCanceledTouch) {
            long uptimeMillis = SystemClock.uptimeMillis();
            MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).dispatchTouchEvent(obtain);
            }
            obtain.recycle();
            this.mChildrenCanceledTouch = CHILDREN_DISALLOW_INTERCEPT;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean checkDrawerViewAbsoluteGravity(View drawerView, int checkFor) {
        if ((getDrawerViewAbsoluteGravity(drawerView) & checkFor) == checkFor) {
            return CHILDREN_DISALLOW_INTERCEPT;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        if (!(p instanceof LayoutParams) || !super.checkLayoutParams(p)) {
            return false;
        }
        return CHILDREN_DISALLOW_INTERCEPT;
    }

    public void close() {
        closeDrawer((int) GravityCompat.START);
    }

    public void closeDrawer(int gravity) {
        closeDrawer(gravity, (boolean) CHILDREN_DISALLOW_INTERCEPT);
    }

    public void closeDrawer(View drawerView) {
        closeDrawer(drawerView, (boolean) CHILDREN_DISALLOW_INTERCEPT);
    }

    public void closeDrawer(View drawerView, boolean animate) {
        if (isDrawerView(drawerView)) {
            LayoutParams layoutParams = (LayoutParams) drawerView.getLayoutParams();
            if (this.mFirstLayout) {
                layoutParams.onScreen = 0.0f;
                layoutParams.openState = 0;
            } else if (animate) {
                layoutParams.openState = 4 | layoutParams.openState;
                if (checkDrawerViewAbsoluteGravity(drawerView, 3)) {
                    this.mLeftDragger.smoothSlideViewTo(drawerView, -drawerView.getWidth(), drawerView.getTop());
                } else {
                    this.mRightDragger.smoothSlideViewTo(drawerView, getWidth(), drawerView.getTop());
                }
            } else {
                moveDrawerToOffset(drawerView, 0.0f);
                updateDrawerState(0, drawerView);
                drawerView.setVisibility(4);
            }
            invalidate();
            return;
        }
        throw new IllegalArgumentException("View " + drawerView + " is not a sliding drawer");
    }

    public void closeDrawers() {
        closeDrawers(false);
    }

    /* access modifiers changed from: package-private */
    public void closeDrawers(boolean peekingOnly) {
        boolean z = false;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            if (isDrawerView(childAt) && (!peekingOnly || layoutParams.isPeeking)) {
                z = checkDrawerViewAbsoluteGravity(childAt, 3) ? z | this.mLeftDragger.smoothSlideViewTo(childAt, -childAt.getWidth(), childAt.getTop()) : z | this.mRightDragger.smoothSlideViewTo(childAt, getWidth(), childAt.getTop());
                layoutParams.isPeeking = false;
            }
        }
        this.mLeftCallback.removeCallbacks();
        this.mRightCallback.removeCallbacks();
        if (z) {
            invalidate();
        }
    }

    public void computeScroll() {
        int childCount = getChildCount();
        float f = 0.0f;
        for (int i = 0; i < childCount; i++) {
            f = Math.max(f, ((LayoutParams) getChildAt(i).getLayoutParams()).onScreen);
        }
        this.mScrimOpacity = f;
        boolean continueSettling = this.mLeftDragger.continueSettling(CHILDREN_DISALLOW_INTERCEPT);
        boolean continueSettling2 = this.mRightDragger.continueSettling(CHILDREN_DISALLOW_INTERCEPT);
        if (continueSettling || continueSettling2) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if ((event.getSource() & 2) == 0 || event.getAction() == 10 || this.mScrimOpacity <= 0.0f) {
            return super.dispatchGenericMotionEvent(event);
        }
        int childCount = getChildCount();
        if (childCount == 0) {
            return false;
        }
        float x = event.getX();
        float y = event.getY();
        for (int i = childCount - 1; i >= 0; i--) {
            View childAt = getChildAt(i);
            if (isInBoundsOfChild(x, y, childAt) && !isContentView(childAt) && dispatchTransformedGenericPointerEvent(event, childAt)) {
                return CHILDREN_DISALLOW_INTERCEPT;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnDrawerClosed(View drawerView) {
        View rootView;
        LayoutParams layoutParams = (LayoutParams) drawerView.getLayoutParams();
        if ((layoutParams.openState & 1) == 1) {
            layoutParams.openState = 0;
            List<DrawerListener> list = this.mListeners;
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    this.mListeners.get(size).onDrawerClosed(drawerView);
                }
            }
            updateChildrenImportantForAccessibility(drawerView, false);
            updateChildAccessibilityAction(drawerView);
            if (hasWindowFocus() && (rootView = getRootView()) != null) {
                rootView.sendAccessibilityEvent(32);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnDrawerOpened(View drawerView) {
        LayoutParams layoutParams = (LayoutParams) drawerView.getLayoutParams();
        if ((layoutParams.openState & 1) == 0) {
            layoutParams.openState = 1;
            List<DrawerListener> list = this.mListeners;
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    this.mListeners.get(size).onDrawerOpened(drawerView);
                }
            }
            updateChildrenImportantForAccessibility(drawerView, CHILDREN_DISALLOW_INTERCEPT);
            updateChildAccessibilityAction(drawerView);
            if (hasWindowFocus()) {
                sendAccessibilityEvent(32);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnDrawerSlide(View drawerView, float slideOffset) {
        List<DrawerListener> list = this.mListeners;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.mListeners.get(size).onDrawerSlide(drawerView, slideOffset);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        int i;
        int i2;
        Canvas canvas2 = canvas;
        View view = child;
        int height = getHeight();
        boolean isContentView = isContentView(view);
        int i3 = 0;
        int width = getWidth();
        int save = canvas.save();
        if (isContentView) {
            int childCount = getChildCount();
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = getChildAt(i4);
                if (childAt != view && childAt.getVisibility() == 0 && hasOpaqueBackground(childAt) && isDrawerView(childAt) && childAt.getHeight() >= height) {
                    if (checkDrawerViewAbsoluteGravity(childAt, 3)) {
                        int right = childAt.getRight();
                        if (right > i3) {
                            i3 = right;
                        }
                    } else {
                        int left = childAt.getLeft();
                        if (left < width) {
                            width = left;
                        }
                    }
                }
            }
            canvas2.clipRect(i3, 0, width, getHeight());
            i2 = i3;
            i = width;
        } else {
            i2 = 0;
            i = width;
        }
        boolean drawChild = super.drawChild(canvas, child, drawingTime);
        canvas2.restoreToCount(save);
        float f = this.mScrimOpacity;
        if (f > 0.0f && isContentView) {
            int i5 = this.mScrimColor;
            int i6 = (int) (((float) ((-16777216 & i5) >>> 24)) * f);
            int i7 = (i6 << 24) | (i5 & ViewCompat.MEASURED_SIZE_MASK);
            this.mScrimPaint.setColor(i7);
            int i8 = i7;
            int i9 = i6;
            canvas.drawRect((float) i2, 0.0f, (float) i, (float) getHeight(), this.mScrimPaint);
        } else if (this.mShadowLeftResolved != null && checkDrawerViewAbsoluteGravity(view, 3)) {
            int intrinsicWidth = this.mShadowLeftResolved.getIntrinsicWidth();
            int right2 = child.getRight();
            float max = Math.max(0.0f, Math.min(((float) right2) / ((float) this.mLeftDragger.getEdgeSize()), 1.0f));
            int i10 = intrinsicWidth;
            this.mShadowLeftResolved.setBounds(right2, child.getTop(), right2 + intrinsicWidth, child.getBottom());
            this.mShadowLeftResolved.setAlpha((int) (255.0f * max));
            this.mShadowLeftResolved.draw(canvas2);
        } else if (this.mShadowRightResolved != null && checkDrawerViewAbsoluteGravity(view, 5)) {
            int intrinsicWidth2 = this.mShadowRightResolved.getIntrinsicWidth();
            int left2 = child.getLeft();
            int width2 = getWidth() - left2;
            float max2 = Math.max(0.0f, Math.min(((float) width2) / ((float) this.mRightDragger.getEdgeSize()), 1.0f));
            int i11 = intrinsicWidth2;
            int i12 = width2;
            this.mShadowRightResolved.setBounds(left2 - intrinsicWidth2, child.getTop(), left2, child.getBottom());
            this.mShadowRightResolved.setAlpha((int) (255.0f * max2));
            this.mShadowRightResolved.draw(canvas2);
        }
        return drawChild;
    }

    /* access modifiers changed from: package-private */
    public View findDrawerWithGravity(int gravity) {
        int absoluteGravity = GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(this)) & 7;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if ((getDrawerViewAbsoluteGravity(childAt) & 7) == absoluteGravity) {
                return childAt;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public View findOpenDrawer() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if ((((LayoutParams) childAt.getLayoutParams()).openState & 1) == 1) {
                return childAt;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public View findVisibleDrawer() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (isDrawerView(childAt) && isDrawerVisible(childAt)) {
                return childAt;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* access modifiers changed from: protected */
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams ? new LayoutParams((LayoutParams) p) : p instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams) p) : new LayoutParams(p);
    }

    public float getDrawerElevation() {
        if (SET_DRAWER_SHADOW_FROM_ELEVATION) {
            return this.mDrawerElevation;
        }
        return 0.0f;
    }

    public int getDrawerLockMode(int edgeGravity) {
        int layoutDirection = ViewCompat.getLayoutDirection(this);
        switch (edgeGravity) {
            case 3:
                int i = this.mLockModeLeft;
                if (i != 3) {
                    return i;
                }
                int i2 = layoutDirection == 0 ? this.mLockModeStart : this.mLockModeEnd;
                if (i2 != 3) {
                    return i2;
                }
                return 0;
            case 5:
                int i3 = this.mLockModeRight;
                if (i3 != 3) {
                    return i3;
                }
                int i4 = layoutDirection == 0 ? this.mLockModeEnd : this.mLockModeStart;
                if (i4 != 3) {
                    return i4;
                }
                return 0;
            case GravityCompat.START:
                int i5 = this.mLockModeStart;
                if (i5 != 3) {
                    return i5;
                }
                int i6 = layoutDirection == 0 ? this.mLockModeLeft : this.mLockModeRight;
                if (i6 != 3) {
                    return i6;
                }
                return 0;
            case GravityCompat.END:
                int i7 = this.mLockModeEnd;
                if (i7 != 3) {
                    return i7;
                }
                int i8 = layoutDirection == 0 ? this.mLockModeRight : this.mLockModeLeft;
                if (i8 != 3) {
                    return i8;
                }
                return 0;
            default:
                return 0;
        }
    }

    public int getDrawerLockMode(View drawerView) {
        if (isDrawerView(drawerView)) {
            return getDrawerLockMode(((LayoutParams) drawerView.getLayoutParams()).gravity);
        }
        throw new IllegalArgumentException("View " + drawerView + " is not a drawer");
    }

    public CharSequence getDrawerTitle(int edgeGravity) {
        int absoluteGravity = GravityCompat.getAbsoluteGravity(edgeGravity, ViewCompat.getLayoutDirection(this));
        if (absoluteGravity == 3) {
            return this.mTitleLeft;
        }
        if (absoluteGravity == 5) {
            return this.mTitleRight;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public int getDrawerViewAbsoluteGravity(View drawerView) {
        return GravityCompat.getAbsoluteGravity(((LayoutParams) drawerView.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(this));
    }

    /* access modifiers changed from: package-private */
    public float getDrawerViewOffset(View drawerView) {
        return ((LayoutParams) drawerView.getLayoutParams()).onScreen;
    }

    public Drawable getStatusBarBackgroundDrawable() {
        return this.mStatusBarBackground;
    }

    /* access modifiers changed from: package-private */
    public boolean isContentView(View child) {
        if (((LayoutParams) child.getLayoutParams()).gravity == 0) {
            return CHILDREN_DISALLOW_INTERCEPT;
        }
        return false;
    }

    public boolean isDrawerOpen(int drawerGravity) {
        View findDrawerWithGravity = findDrawerWithGravity(drawerGravity);
        if (findDrawerWithGravity != null) {
            return isDrawerOpen(findDrawerWithGravity);
        }
        return false;
    }

    public boolean isDrawerOpen(View drawer) {
        if (!isDrawerView(drawer)) {
            throw new IllegalArgumentException("View " + drawer + " is not a drawer");
        } else if ((((LayoutParams) drawer.getLayoutParams()).openState & 1) == 1) {
            return CHILDREN_DISALLOW_INTERCEPT;
        } else {
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isDrawerView(View child) {
        int absoluteGravity = GravityCompat.getAbsoluteGravity(((LayoutParams) child.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(child));
        if ((absoluteGravity & 3) == 0 && (absoluteGravity & 5) == 0) {
            return false;
        }
        return CHILDREN_DISALLOW_INTERCEPT;
    }

    public boolean isDrawerVisible(int drawerGravity) {
        View findDrawerWithGravity = findDrawerWithGravity(drawerGravity);
        if (findDrawerWithGravity != null) {
            return isDrawerVisible(findDrawerWithGravity);
        }
        return false;
    }

    public boolean isDrawerVisible(View drawer) {
        if (!isDrawerView(drawer)) {
            throw new IllegalArgumentException("View " + drawer + " is not a drawer");
        } else if (((LayoutParams) drawer.getLayoutParams()).onScreen > 0.0f) {
            return CHILDREN_DISALLOW_INTERCEPT;
        } else {
            return false;
        }
    }

    public boolean isOpen() {
        return isDrawerOpen((int) GravityCompat.START);
    }

    /* access modifiers changed from: package-private */
    public void moveDrawerToOffset(View drawerView, float slideOffset) {
        float drawerViewOffset = getDrawerViewOffset(drawerView);
        int width = drawerView.getWidth();
        int i = ((int) (((float) width) * slideOffset)) - ((int) (((float) width) * drawerViewOffset));
        drawerView.offsetLeftAndRight(checkDrawerViewAbsoluteGravity(drawerView, 3) ? i : -i);
        setDrawerViewOffset(drawerView, slideOffset);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = CHILDREN_DISALLOW_INTERCEPT;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = CHILDREN_DISALLOW_INTERCEPT;
    }

    public void onDraw(Canvas c) {
        int i;
        super.onDraw(c);
        if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                Object obj = this.mLastInsets;
                i = obj != null ? ((WindowInsets) obj).getSystemWindowInsetTop() : 0;
            } else {
                i = 0;
            }
            if (i > 0) {
                this.mStatusBarBackground.setBounds(0, 0, getWidth(), i);
                this.mStatusBarBackground.draw(c);
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        View findTopChildUnder;
        int actionMasked = ev.getActionMasked();
        boolean shouldInterceptTouchEvent = this.mLeftDragger.shouldInterceptTouchEvent(ev) | this.mRightDragger.shouldInterceptTouchEvent(ev);
        boolean z = false;
        switch (actionMasked) {
            case 0:
                float x = ev.getX();
                float y = ev.getY();
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                if (this.mScrimOpacity > 0.0f && (findTopChildUnder = this.mLeftDragger.findTopChildUnder((int) x, (int) y)) != null && isContentView(findTopChildUnder)) {
                    z = CHILDREN_DISALLOW_INTERCEPT;
                }
                this.mChildrenCanceledTouch = false;
                break;
            case 1:
            case 3:
                closeDrawers(CHILDREN_DISALLOW_INTERCEPT);
                this.mChildrenCanceledTouch = false;
                break;
            case 2:
                if (this.mLeftDragger.checkTouchSlop(3)) {
                    this.mLeftCallback.removeCallbacks();
                    this.mRightCallback.removeCallbacks();
                    break;
                }
                break;
        }
        if (shouldInterceptTouchEvent || z || hasPeekingDrawer() || this.mChildrenCanceledTouch) {
            return CHILDREN_DISALLOW_INTERCEPT;
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !hasVisibleDrawer()) {
            return super.onKeyDown(keyCode, event);
        }
        event.startTracking();
        return CHILDREN_DISALLOW_INTERCEPT;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyUp(keyCode, event);
        }
        View findVisibleDrawer = findVisibleDrawer();
        if (findVisibleDrawer != null && getDrawerLockMode(findVisibleDrawer) == 0) {
            closeDrawers();
        }
        if (findVisibleDrawer != null) {
            return CHILDREN_DISALLOW_INTERCEPT;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        WindowInsets rootWindowInsets;
        float f;
        int i;
        boolean z = CHILDREN_DISALLOW_INTERCEPT;
        this.mInLayout = CHILDREN_DISALLOW_INTERCEPT;
        int i2 = r - l;
        int childCount = getChildCount();
        int i3 = 0;
        while (i3 < childCount) {
            View childAt = getChildAt(i3);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (isContentView(childAt)) {
                    childAt.layout(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.leftMargin + childAt.getMeasuredWidth(), layoutParams.topMargin + childAt.getMeasuredHeight());
                } else {
                    int measuredWidth = childAt.getMeasuredWidth();
                    int measuredHeight = childAt.getMeasuredHeight();
                    if (checkDrawerViewAbsoluteGravity(childAt, 3)) {
                        i = (-measuredWidth) + ((int) (((float) measuredWidth) * layoutParams.onScreen));
                        f = ((float) (measuredWidth + i)) / ((float) measuredWidth);
                    } else {
                        i = i2 - ((int) (((float) measuredWidth) * layoutParams.onScreen));
                        f = ((float) (i2 - i)) / ((float) measuredWidth);
                    }
                    boolean z2 = f != layoutParams.onScreen ? z : false;
                    switch (layoutParams.gravity & 112) {
                        case 16:
                            int i4 = b - t;
                            int i5 = (i4 - measuredHeight) / 2;
                            if (i5 < layoutParams.topMargin) {
                                i5 = layoutParams.topMargin;
                            } else if (i5 + measuredHeight > i4 - layoutParams.bottomMargin) {
                                i5 = (i4 - layoutParams.bottomMargin) - measuredHeight;
                            }
                            childAt.layout(i, i5, i + measuredWidth, i5 + measuredHeight);
                            break;
                        case 80:
                            int i6 = b - t;
                            childAt.layout(i, (i6 - layoutParams.bottomMargin) - childAt.getMeasuredHeight(), i + measuredWidth, i6 - layoutParams.bottomMargin);
                            break;
                        default:
                            childAt.layout(i, layoutParams.topMargin, i + measuredWidth, layoutParams.topMargin + measuredHeight);
                            break;
                    }
                    if (z2) {
                        setDrawerViewOffset(childAt, f);
                    }
                    int i7 = layoutParams.onScreen > 0.0f ? 0 : 4;
                    if (childAt.getVisibility() != i7) {
                        childAt.setVisibility(i7);
                    }
                }
            }
            i3++;
            z = CHILDREN_DISALLOW_INTERCEPT;
        }
        if (sEdgeSizeUsingSystemGestureInsets && (rootWindowInsets = getRootWindowInsets()) != null) {
            Insets systemGestureInsets = WindowInsetsCompat.toWindowInsetsCompat(rootWindowInsets).getSystemGestureInsets();
            ViewDragHelper viewDragHelper = this.mLeftDragger;
            viewDragHelper.setEdgeSize(Math.max(viewDragHelper.getDefaultEdgeSize(), systemGestureInsets.left));
            ViewDragHelper viewDragHelper2 = this.mRightDragger;
            viewDragHelper2.setEdgeSize(Math.max(viewDragHelper2.getDefaultEdgeSize(), systemGestureInsets.right));
        }
        this.mInLayout = false;
        this.mFirstLayout = false;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        View findDrawerWithGravity;
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (!(savedState.openDrawerGravity == 0 || (findDrawerWithGravity = findDrawerWithGravity(savedState.openDrawerGravity)) == null)) {
            openDrawer(findDrawerWithGravity);
        }
        if (savedState.lockModeLeft != 3) {
            setDrawerLockMode(savedState.lockModeLeft, 3);
        }
        if (savedState.lockModeRight != 3) {
            setDrawerLockMode(savedState.lockModeRight, 5);
        }
        if (savedState.lockModeStart != 3) {
            setDrawerLockMode(savedState.lockModeStart, (int) GravityCompat.START);
        }
        if (savedState.lockModeEnd != 3) {
            setDrawerLockMode(savedState.lockModeEnd, (int) GravityCompat.END);
        }
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        resolveShadowDrawables();
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        int childCount = getChildCount();
        int i = 0;
        while (true) {
            if (i >= childCount) {
                break;
            }
            LayoutParams layoutParams = (LayoutParams) getChildAt(i).getLayoutParams();
            boolean z = false;
            boolean z2 = layoutParams.openState == 1;
            if (layoutParams.openState == 2) {
                z = true;
            }
            if (z2 || z) {
                savedState.openDrawerGravity = layoutParams.gravity;
            } else {
                i++;
            }
        }
        savedState.lockModeLeft = this.mLockModeLeft;
        savedState.lockModeRight = this.mLockModeRight;
        savedState.lockModeStart = this.mLockModeStart;
        savedState.lockModeEnd = this.mLockModeEnd;
        return savedState;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        View findOpenDrawer;
        this.mLeftDragger.processTouchEvent(ev);
        this.mRightDragger.processTouchEvent(ev);
        boolean z = false;
        switch (ev.getAction() & 255) {
            case 0:
                float x = ev.getX();
                float y = ev.getY();
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                this.mChildrenCanceledTouch = false;
                break;
            case 1:
                float x2 = ev.getX();
                float y2 = ev.getY();
                boolean z2 = CHILDREN_DISALLOW_INTERCEPT;
                View findTopChildUnder = this.mLeftDragger.findTopChildUnder((int) x2, (int) y2);
                if (findTopChildUnder != null && isContentView(findTopChildUnder)) {
                    float f = x2 - this.mInitialMotionX;
                    float f2 = y2 - this.mInitialMotionY;
                    int touchSlop = this.mLeftDragger.getTouchSlop();
                    if ((f * f) + (f2 * f2) < ((float) (touchSlop * touchSlop)) && (findOpenDrawer = findOpenDrawer()) != null) {
                        if (getDrawerLockMode(findOpenDrawer) == 2) {
                            z = true;
                        }
                        z2 = z;
                    }
                }
                closeDrawers(z2);
                break;
            case 3:
                closeDrawers(CHILDREN_DISALLOW_INTERCEPT);
                this.mChildrenCanceledTouch = false;
                break;
        }
        return CHILDREN_DISALLOW_INTERCEPT;
    }

    public void open() {
        openDrawer((int) GravityCompat.START);
    }

    public void openDrawer(int gravity) {
        openDrawer(gravity, (boolean) CHILDREN_DISALLOW_INTERCEPT);
    }

    public void openDrawer(View drawerView) {
        openDrawer(drawerView, (boolean) CHILDREN_DISALLOW_INTERCEPT);
    }

    public void openDrawer(View drawerView, boolean animate) {
        if (isDrawerView(drawerView)) {
            LayoutParams layoutParams = (LayoutParams) drawerView.getLayoutParams();
            if (this.mFirstLayout) {
                layoutParams.onScreen = 1.0f;
                layoutParams.openState = 1;
                updateChildrenImportantForAccessibility(drawerView, CHILDREN_DISALLOW_INTERCEPT);
                updateChildAccessibilityAction(drawerView);
            } else if (animate) {
                layoutParams.openState |= 2;
                if (checkDrawerViewAbsoluteGravity(drawerView, 3)) {
                    this.mLeftDragger.smoothSlideViewTo(drawerView, 0, drawerView.getTop());
                } else {
                    this.mRightDragger.smoothSlideViewTo(drawerView, getWidth() - drawerView.getWidth(), drawerView.getTop());
                }
            } else {
                moveDrawerToOffset(drawerView, 1.0f);
                updateDrawerState(0, drawerView);
                drawerView.setVisibility(0);
            }
            invalidate();
            return;
        }
        throw new IllegalArgumentException("View " + drawerView + " is not a sliding drawer");
    }

    public void removeDrawerListener(DrawerListener listener) {
        List<DrawerListener> list;
        if (listener != null && (list = this.mListeners) != null) {
            list.remove(listener);
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
        if (disallowIntercept) {
            closeDrawers(CHILDREN_DISALLOW_INTERCEPT);
        }
    }

    public void requestLayout() {
        if (!this.mInLayout) {
            super.requestLayout();
        }
    }

    public void setChildInsets(Object insets, boolean draw) {
        this.mLastInsets = insets;
        this.mDrawStatusBarBackground = draw;
        setWillNotDraw((draw || getBackground() != null) ? false : CHILDREN_DISALLOW_INTERCEPT);
        requestLayout();
    }

    public void setDrawerElevation(float elevation) {
        this.mDrawerElevation = elevation;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (isDrawerView(childAt)) {
                ViewCompat.setElevation(childAt, this.mDrawerElevation);
            }
        }
    }

    @Deprecated
    public void setDrawerListener(DrawerListener listener) {
        DrawerListener drawerListener = this.mListener;
        if (drawerListener != null) {
            removeDrawerListener(drawerListener);
        }
        if (listener != null) {
            addDrawerListener(listener);
        }
        this.mListener = listener;
    }

    public void setDrawerLockMode(int lockMode) {
        setDrawerLockMode(lockMode, 3);
        setDrawerLockMode(lockMode, 5);
    }

    public void setDrawerLockMode(int lockMode, int edgeGravity) {
        int absoluteGravity = GravityCompat.getAbsoluteGravity(edgeGravity, ViewCompat.getLayoutDirection(this));
        switch (edgeGravity) {
            case 3:
                this.mLockModeLeft = lockMode;
                break;
            case 5:
                this.mLockModeRight = lockMode;
                break;
            case GravityCompat.START:
                this.mLockModeStart = lockMode;
                break;
            case GravityCompat.END:
                this.mLockModeEnd = lockMode;
                break;
        }
        if (lockMode != 0) {
            (absoluteGravity == 3 ? this.mLeftDragger : this.mRightDragger).cancel();
        }
        switch (lockMode) {
            case 1:
                View findDrawerWithGravity = findDrawerWithGravity(absoluteGravity);
                if (findDrawerWithGravity != null) {
                    closeDrawer(findDrawerWithGravity);
                    return;
                }
                return;
            case 2:
                View findDrawerWithGravity2 = findDrawerWithGravity(absoluteGravity);
                if (findDrawerWithGravity2 != null) {
                    openDrawer(findDrawerWithGravity2);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void setDrawerLockMode(int lockMode, View drawerView) {
        if (isDrawerView(drawerView)) {
            setDrawerLockMode(lockMode, ((LayoutParams) drawerView.getLayoutParams()).gravity);
            return;
        }
        throw new IllegalArgumentException("View " + drawerView + " is not a drawer with appropriate layout_gravity");
    }

    public void setDrawerShadow(int resId, int gravity) {
        setDrawerShadow(ContextCompat.getDrawable(getContext(), resId), gravity);
    }

    public void setDrawerShadow(Drawable shadowDrawable, int gravity) {
        if (!SET_DRAWER_SHADOW_FROM_ELEVATION) {
            if ((gravity & GravityCompat.START) == 8388611) {
                this.mShadowStart = shadowDrawable;
            } else if ((gravity & GravityCompat.END) == 8388613) {
                this.mShadowEnd = shadowDrawable;
            } else if ((gravity & 3) == 3) {
                this.mShadowLeft = shadowDrawable;
            } else if ((gravity & 5) == 5) {
                this.mShadowRight = shadowDrawable;
            } else {
                return;
            }
            resolveShadowDrawables();
            invalidate();
        }
    }

    public void setDrawerTitle(int edgeGravity, CharSequence title) {
        int absoluteGravity = GravityCompat.getAbsoluteGravity(edgeGravity, ViewCompat.getLayoutDirection(this));
        if (absoluteGravity == 3) {
            this.mTitleLeft = title;
        } else if (absoluteGravity == 5) {
            this.mTitleRight = title;
        }
    }

    /* access modifiers changed from: package-private */
    public void setDrawerViewOffset(View drawerView, float slideOffset) {
        LayoutParams layoutParams = (LayoutParams) drawerView.getLayoutParams();
        if (slideOffset != layoutParams.onScreen) {
            layoutParams.onScreen = slideOffset;
            dispatchOnDrawerSlide(drawerView, slideOffset);
        }
    }

    public void setScrimColor(int color) {
        this.mScrimColor = color;
        invalidate();
    }

    public void setStatusBarBackground(int resId) {
        this.mStatusBarBackground = resId != 0 ? ContextCompat.getDrawable(getContext(), resId) : null;
        invalidate();
    }

    public void setStatusBarBackground(Drawable bg) {
        this.mStatusBarBackground = bg;
        invalidate();
    }

    public void setStatusBarBackgroundColor(int color) {
        this.mStatusBarBackground = new ColorDrawable(color);
        invalidate();
    }

    /* access modifiers changed from: package-private */
    public void updateDrawerState(int activeState, View activeDrawer) {
        int viewDragState = this.mLeftDragger.getViewDragState();
        int viewDragState2 = this.mRightDragger.getViewDragState();
        int i = (viewDragState == 1 || viewDragState2 == 1) ? 1 : (viewDragState == 2 || viewDragState2 == 2) ? 2 : 0;
        if (activeDrawer != null && activeState == 0) {
            LayoutParams layoutParams = (LayoutParams) activeDrawer.getLayoutParams();
            if (layoutParams.onScreen == 0.0f) {
                dispatchOnDrawerClosed(activeDrawer);
            } else if (layoutParams.onScreen == 1.0f) {
                dispatchOnDrawerOpened(activeDrawer);
            }
        }
        if (i != this.mDrawerState) {
            this.mDrawerState = i;
            List<DrawerListener> list = this.mListeners;
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    this.mListeners.get(size).onDrawerStateChanged(i);
                }
            }
        }
    }

    public void closeDrawer(int gravity, boolean animate) {
        View findDrawerWithGravity = findDrawerWithGravity(gravity);
        if (findDrawerWithGravity != null) {
            closeDrawer(findDrawerWithGravity, animate);
            return;
        }
        StringBuilder append = new StringBuilder().append("No drawer view found with gravity ");
        String gravityToString = gravityToString(gravity);
        Log1F380D.a((Object) gravityToString);
        throw new IllegalArgumentException(append.append(gravityToString).toString());
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean z;
        int i;
        int i2;
        boolean z2;
        DrawerLayout drawerLayout = this;
        int mode = View.MeasureSpec.getMode(widthMeasureSpec);
        int mode2 = View.MeasureSpec.getMode(heightMeasureSpec);
        int size = View.MeasureSpec.getSize(widthMeasureSpec);
        int size2 = View.MeasureSpec.getSize(heightMeasureSpec);
        char c = CharCompanionObject.MIN_VALUE;
        if (!(mode == 1073741824 && mode2 == 1073741824)) {
            if (isInEditMode()) {
                if (mode == 0) {
                    size = 300;
                }
                if (mode2 == 0) {
                    size2 = 300;
                }
            } else {
                int i3 = mode;
                throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
            }
        }
        drawerLayout.setMeasuredDimension(size, size2);
        boolean z3 = (drawerLayout.mLastInsets == null || !ViewCompat.getFitsSystemWindows(this)) ? false : CHILDREN_DISALLOW_INTERCEPT;
        int layoutDirection = ViewCompat.getLayoutDirection(this);
        boolean z4 = false;
        boolean z5 = false;
        int childCount = getChildCount();
        int i4 = 0;
        while (i4 < childCount) {
            View childAt = drawerLayout.getChildAt(i4);
            if (childAt.getVisibility() == 8) {
                i2 = mode;
                i = mode2;
                char c2 = c;
                z = z3;
            } else {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (z3) {
                    int absoluteGravity = GravityCompat.getAbsoluteGravity(layoutParams.gravity, layoutDirection);
                    if (!ViewCompat.getFitsSystemWindows(childAt)) {
                        i2 = mode;
                        i = mode2;
                        z = z3;
                        if (Build.VERSION.SDK_INT >= 21) {
                            WindowInsets windowInsets = (WindowInsets) drawerLayout.mLastInsets;
                            if (absoluteGravity == 3) {
                                z2 = false;
                                windowInsets = windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft(), windowInsets.getSystemWindowInsetTop(), 0, windowInsets.getSystemWindowInsetBottom());
                            } else {
                                z2 = false;
                                if (absoluteGravity == 5) {
                                    windowInsets = windowInsets.replaceSystemWindowInsets(0, windowInsets.getSystemWindowInsetTop(), windowInsets.getSystemWindowInsetRight(), windowInsets.getSystemWindowInsetBottom());
                                }
                            }
                            layoutParams.leftMargin = windowInsets.getSystemWindowInsetLeft();
                            layoutParams.topMargin = windowInsets.getSystemWindowInsetTop();
                            layoutParams.rightMargin = windowInsets.getSystemWindowInsetRight();
                            layoutParams.bottomMargin = windowInsets.getSystemWindowInsetBottom();
                        } else {
                            z2 = false;
                        }
                    } else if (Build.VERSION.SDK_INT >= 21) {
                        WindowInsets windowInsets2 = (WindowInsets) drawerLayout.mLastInsets;
                        if (absoluteGravity == 3) {
                            i2 = mode;
                            i = mode2;
                            z = z3;
                            windowInsets2 = windowInsets2.replaceSystemWindowInsets(windowInsets2.getSystemWindowInsetLeft(), windowInsets2.getSystemWindowInsetTop(), 0, windowInsets2.getSystemWindowInsetBottom());
                        } else {
                            i2 = mode;
                            i = mode2;
                            z = z3;
                            if (absoluteGravity == 5) {
                                windowInsets2 = windowInsets2.replaceSystemWindowInsets(0, windowInsets2.getSystemWindowInsetTop(), windowInsets2.getSystemWindowInsetRight(), windowInsets2.getSystemWindowInsetBottom());
                            }
                        }
                        childAt.dispatchApplyWindowInsets(windowInsets2);
                        z2 = false;
                    } else {
                        i2 = mode;
                        i = mode2;
                        z = z3;
                        z2 = false;
                    }
                } else {
                    i2 = mode;
                    i = mode2;
                    z = z3;
                    z2 = false;
                }
                if (drawerLayout.isContentView(childAt)) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec((size - layoutParams.leftMargin) - layoutParams.rightMargin, BasicMeasure.EXACTLY), View.MeasureSpec.makeMeasureSpec((size2 - layoutParams.topMargin) - layoutParams.bottomMargin, BasicMeasure.EXACTLY));
                } else if (drawerLayout.isDrawerView(childAt)) {
                    if (SET_DRAWER_SHADOW_FROM_ELEVATION) {
                        float elevation = ViewCompat.getElevation(childAt);
                        float f = drawerLayout.mDrawerElevation;
                        if (elevation != f) {
                            ViewCompat.setElevation(childAt, f);
                        }
                    }
                    int drawerViewAbsoluteGravity = drawerLayout.getDrawerViewAbsoluteGravity(childAt) & 7;
                    boolean z6 = drawerViewAbsoluteGravity == 3 ? CHILDREN_DISALLOW_INTERCEPT : z2;
                    if ((!z6 || !z4) && (z6 || !z5)) {
                        if (z6) {
                            z4 = CHILDREN_DISALLOW_INTERCEPT;
                        } else {
                            z5 = CHILDREN_DISALLOW_INTERCEPT;
                        }
                        childAt.measure(getChildMeasureSpec(widthMeasureSpec, drawerLayout.mMinDrawerMargin + layoutParams.leftMargin + layoutParams.rightMargin, layoutParams.width), getChildMeasureSpec(heightMeasureSpec, layoutParams.topMargin + layoutParams.bottomMargin, layoutParams.height));
                        i4++;
                        drawerLayout = this;
                        mode = i2;
                        mode2 = i;
                        z3 = z;
                        c = CharCompanionObject.MIN_VALUE;
                    } else {
                        StringBuilder append = new StringBuilder().append("Child drawer has absolute gravity ");
                        String gravityToString = gravityToString(drawerViewAbsoluteGravity);
                        Log1F380D.a((Object) gravityToString);
                        throw new IllegalStateException(append.append(gravityToString).append(" but this ").append(TAG).append(" already has a drawer view along that edge").toString());
                    }
                } else {
                    int i5 = widthMeasureSpec;
                    int i6 = heightMeasureSpec;
                    throw new IllegalStateException("Child " + childAt + " at index " + i4 + " does not have a valid layout_gravity - must be Gravity.LEFT, Gravity.RIGHT or Gravity.NO_GRAVITY");
                }
            }
            int i7 = widthMeasureSpec;
            int i8 = heightMeasureSpec;
            i4++;
            drawerLayout = this;
            mode = i2;
            mode2 = i;
            z3 = z;
            c = CharCompanionObject.MIN_VALUE;
        }
    }

    public void openDrawer(int gravity, boolean animate) {
        View findDrawerWithGravity = findDrawerWithGravity(gravity);
        if (findDrawerWithGravity != null) {
            openDrawer(findDrawerWithGravity, animate);
            return;
        }
        StringBuilder append = new StringBuilder().append("No drawer view found with gravity ");
        String gravityToString = gravityToString(gravity);
        Log1F380D.a((Object) gravityToString);
        throw new IllegalArgumentException(append.append(gravityToString).toString());
    }
}

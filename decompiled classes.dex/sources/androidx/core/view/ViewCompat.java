package androidx.core.view;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContentInfo;
import android.view.Display;
import android.view.KeyEvent;
import android.view.OnReceiveContentListener;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeProvider;
import androidx.collection.SimpleArrayMap;
import androidx.core.R;
import androidx.core.util.Preconditions;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.WindowInsetsAnimationCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import mt.Log1F380D;

/* compiled from: 0061 */
public class ViewCompat {
    private static final int[] ACCESSIBILITY_ACTIONS_RESOURCE_IDS = {R.id.accessibility_custom_action_0, R.id.accessibility_custom_action_1, R.id.accessibility_custom_action_2, R.id.accessibility_custom_action_3, R.id.accessibility_custom_action_4, R.id.accessibility_custom_action_5, R.id.accessibility_custom_action_6, R.id.accessibility_custom_action_7, R.id.accessibility_custom_action_8, R.id.accessibility_custom_action_9, R.id.accessibility_custom_action_10, R.id.accessibility_custom_action_11, R.id.accessibility_custom_action_12, R.id.accessibility_custom_action_13, R.id.accessibility_custom_action_14, R.id.accessibility_custom_action_15, R.id.accessibility_custom_action_16, R.id.accessibility_custom_action_17, R.id.accessibility_custom_action_18, R.id.accessibility_custom_action_19, R.id.accessibility_custom_action_20, R.id.accessibility_custom_action_21, R.id.accessibility_custom_action_22, R.id.accessibility_custom_action_23, R.id.accessibility_custom_action_24, R.id.accessibility_custom_action_25, R.id.accessibility_custom_action_26, R.id.accessibility_custom_action_27, R.id.accessibility_custom_action_28, R.id.accessibility_custom_action_29, R.id.accessibility_custom_action_30, R.id.accessibility_custom_action_31};
    public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
    public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
    public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 2;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;
    @Deprecated
    public static final int LAYER_TYPE_HARDWARE = 2;
    @Deprecated
    public static final int LAYER_TYPE_NONE = 0;
    @Deprecated
    public static final int LAYER_TYPE_SOFTWARE = 1;
    public static final int LAYOUT_DIRECTION_INHERIT = 2;
    public static final int LAYOUT_DIRECTION_LOCALE = 3;
    public static final int LAYOUT_DIRECTION_LTR = 0;
    public static final int LAYOUT_DIRECTION_RTL = 1;
    @Deprecated
    public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;
    @Deprecated
    public static final int MEASURED_SIZE_MASK = 16777215;
    @Deprecated
    public static final int MEASURED_STATE_MASK = -16777216;
    @Deprecated
    public static final int MEASURED_STATE_TOO_SMALL = 16777216;
    private static final OnReceiveContentViewBehavior NO_OP_ON_RECEIVE_CONTENT_VIEW_BEHAVIOR = new ViewCompat$$ExternalSyntheticLambda0();
    @Deprecated
    public static final int OVER_SCROLL_ALWAYS = 0;
    @Deprecated
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
    @Deprecated
    public static final int OVER_SCROLL_NEVER = 2;
    public static final int SCROLL_AXIS_HORIZONTAL = 1;
    public static final int SCROLL_AXIS_NONE = 0;
    public static final int SCROLL_AXIS_VERTICAL = 2;
    public static final int SCROLL_INDICATOR_BOTTOM = 2;
    public static final int SCROLL_INDICATOR_END = 32;
    public static final int SCROLL_INDICATOR_LEFT = 4;
    public static final int SCROLL_INDICATOR_RIGHT = 8;
    public static final int SCROLL_INDICATOR_START = 16;
    public static final int SCROLL_INDICATOR_TOP = 1;
    private static final String TAG = "ViewCompat";
    public static final int TYPE_NON_TOUCH = 1;
    public static final int TYPE_TOUCH = 0;
    private static boolean sAccessibilityDelegateCheckFailed = false;
    private static Field sAccessibilityDelegateField;
    private static final AccessibilityPaneVisibilityManager sAccessibilityPaneVisibilityManager = new AccessibilityPaneVisibilityManager();
    private static Method sChildrenDrawingOrderMethod;
    private static Method sDispatchFinishTemporaryDetach;
    private static Method sDispatchStartTemporaryDetach;
    private static Field sMinHeightField;
    private static boolean sMinHeightFieldFetched;
    private static Field sMinWidthField;
    private static boolean sMinWidthFieldFetched;
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private static boolean sTempDetachBound;
    private static ThreadLocal<Rect> sThreadLocalRect;
    private static WeakHashMap<View, String> sTransitionNameMap;
    private static WeakHashMap<View, ViewPropertyAnimatorCompat> sViewPropertyAnimatorMap = null;

    static class AccessibilityPaneVisibilityManager implements ViewTreeObserver.OnGlobalLayoutListener, View.OnAttachStateChangeListener {
        private final WeakHashMap<View, Boolean> mPanesToVisible = new WeakHashMap<>();

        AccessibilityPaneVisibilityManager() {
        }

        private void checkPaneVisibility(View pane, boolean oldVisibility) {
            boolean z = pane.isShown() && pane.getWindowVisibility() == 0;
            if (oldVisibility != z) {
                ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(pane, z ? 16 : 32);
                this.mPanesToVisible.put(pane, Boolean.valueOf(z));
            }
        }

        private void registerForLayoutCallback(View view) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        private void unregisterForLayoutCallback(View view) {
            Api16Impl.removeOnGlobalLayoutListener(view.getViewTreeObserver(), this);
        }

        /* access modifiers changed from: package-private */
        public void addAccessibilityPane(View pane) {
            this.mPanesToVisible.put(pane, Boolean.valueOf(pane.isShown() && pane.getWindowVisibility() == 0));
            pane.addOnAttachStateChangeListener(this);
            if (Api19Impl.isAttachedToWindow(pane)) {
                registerForLayoutCallback(pane);
            }
        }

        public void onGlobalLayout() {
            if (Build.VERSION.SDK_INT < 28) {
                for (Map.Entry next : this.mPanesToVisible.entrySet()) {
                    checkPaneVisibility((View) next.getKey(), ((Boolean) next.getValue()).booleanValue());
                }
            }
        }

        public void onViewAttachedToWindow(View view) {
            registerForLayoutCallback(view);
        }

        public void onViewDetachedFromWindow(View view) {
        }

        /* access modifiers changed from: package-private */
        public void removeAccessibilityPane(View pane) {
            this.mPanesToVisible.remove(pane);
            pane.removeOnAttachStateChangeListener(this);
            unregisterForLayoutCallback(pane);
        }
    }

    static abstract class AccessibilityViewProperty<T> {
        private final int mContentChangeType;
        private final int mFrameworkMinimumSdk;
        private final int mTagKey;
        private final Class<T> mType;

        AccessibilityViewProperty(int tagKey, Class<T> cls, int frameworkMinimumSdk) {
            this(tagKey, cls, 0, frameworkMinimumSdk);
        }

        AccessibilityViewProperty(int tagKey, Class<T> cls, int contentChangeType, int frameworkMinimumSdk) {
            this.mTagKey = tagKey;
            this.mType = cls;
            this.mContentChangeType = contentChangeType;
            this.mFrameworkMinimumSdk = frameworkMinimumSdk;
        }

        private boolean extrasAvailable() {
            return Build.VERSION.SDK_INT >= 19;
        }

        private boolean frameworkAvailable() {
            return Build.VERSION.SDK_INT >= this.mFrameworkMinimumSdk;
        }

        /* access modifiers changed from: package-private */
        public boolean booleanNullToFalseEquals(Boolean a, Boolean b) {
            return (a != null && a.booleanValue()) == (b != null && b.booleanValue());
        }

        /* access modifiers changed from: package-private */
        public abstract T frameworkGet(View view);

        /* access modifiers changed from: package-private */
        public abstract void frameworkSet(View view, T t);

        /* access modifiers changed from: package-private */
        public T get(View view) {
            if (frameworkAvailable()) {
                return frameworkGet(view);
            }
            if (!extrasAvailable()) {
                return null;
            }
            T tag = view.getTag(this.mTagKey);
            if (this.mType.isInstance(tag)) {
                return tag;
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        public void set(View view, T t) {
            if (frameworkAvailable()) {
                frameworkSet(view, t);
            } else if (extrasAvailable() && shouldUpdate(get(view), t)) {
                ViewCompat.ensureAccessibilityDelegateCompat(view);
                view.setTag(this.mTagKey, t);
                ViewCompat.notifyViewAccessibilityStateChangedIfNeeded(view, this.mContentChangeType);
            }
        }

        /* access modifiers changed from: package-private */
        public boolean shouldUpdate(T t, T t2) {
            return !t2.equals(t);
        }
    }

    static class Api15Impl {
        private Api15Impl() {
        }

        static boolean hasOnClickListeners(View view) {
            return view.hasOnClickListeners();
        }
    }

    static class Api16Impl {
        private Api16Impl() {
        }

        static AccessibilityNodeProvider getAccessibilityNodeProvider(View view) {
            return view.getAccessibilityNodeProvider();
        }

        static boolean getFitsSystemWindows(View view) {
            return view.getFitsSystemWindows();
        }

        static int getImportantForAccessibility(View view) {
            return view.getImportantForAccessibility();
        }

        static int getMinimumHeight(View view) {
            return view.getMinimumHeight();
        }

        static int getMinimumWidth(View view) {
            return view.getMinimumWidth();
        }

        static ViewParent getParentForAccessibility(View view) {
            return view.getParentForAccessibility();
        }

        static int getWindowSystemUiVisibility(View view) {
            return view.getWindowSystemUiVisibility();
        }

        static boolean hasOverlappingRendering(View view) {
            return view.hasOverlappingRendering();
        }

        static boolean hasTransientState(View view) {
            return view.hasTransientState();
        }

        static boolean performAccessibilityAction(View view, int action, Bundle arguments) {
            return view.performAccessibilityAction(action, arguments);
        }

        static void postInvalidateOnAnimation(View view) {
            view.postInvalidateOnAnimation();
        }

        static void postInvalidateOnAnimation(View view, int left, int top, int right, int bottom) {
            view.postInvalidateOnAnimation(left, top, right, bottom);
        }

        static void postOnAnimation(View view, Runnable action) {
            view.postOnAnimation(action);
        }

        static void postOnAnimationDelayed(View view, Runnable action, long delayMillis) {
            view.postOnAnimationDelayed(action, delayMillis);
        }

        static void removeOnGlobalLayoutListener(ViewTreeObserver viewTreeObserver, ViewTreeObserver.OnGlobalLayoutListener victim) {
            viewTreeObserver.removeOnGlobalLayoutListener(victim);
        }

        static void requestFitSystemWindows(View view) {
            view.requestFitSystemWindows();
        }

        static void setBackground(View view, Drawable background) {
            view.setBackground(background);
        }

        static void setHasTransientState(View view, boolean hasTransientState) {
            view.setHasTransientState(hasTransientState);
        }

        static void setImportantForAccessibility(View view, int mode) {
            view.setImportantForAccessibility(mode);
        }
    }

    static class Api17Impl {
        private Api17Impl() {
        }

        static int generateViewId() {
            return View.generateViewId();
        }

        static Display getDisplay(View view) {
            return view.getDisplay();
        }

        static int getLabelFor(View view) {
            return view.getLabelFor();
        }

        static int getLayoutDirection(View view) {
            return view.getLayoutDirection();
        }

        static int getPaddingEnd(View view) {
            return view.getPaddingEnd();
        }

        static int getPaddingStart(View view) {
            return view.getPaddingStart();
        }

        static boolean isPaddingRelative(View view) {
            return view.isPaddingRelative();
        }

        static void setLabelFor(View view, int id) {
            view.setLabelFor(id);
        }

        static void setLayerPaint(View view, Paint paint) {
            view.setLayerPaint(paint);
        }

        static void setLayoutDirection(View view, int layoutDirection) {
            view.setLayoutDirection(layoutDirection);
        }

        static void setPaddingRelative(View view, int start, int top, int end, int bottom) {
            view.setPaddingRelative(start, top, end, bottom);
        }
    }

    static class Api18Impl {
        private Api18Impl() {
        }

        static Rect getClipBounds(View view) {
            return view.getClipBounds();
        }

        static boolean isInLayout(View view) {
            return view.isInLayout();
        }

        static void setClipBounds(View view, Rect clipBounds) {
            view.setClipBounds(clipBounds);
        }
    }

    static class Api19Impl {
        private Api19Impl() {
        }

        static int getAccessibilityLiveRegion(View view) {
            return view.getAccessibilityLiveRegion();
        }

        static boolean isAttachedToWindow(View view) {
            return view.isAttachedToWindow();
        }

        static boolean isLaidOut(View view) {
            return view.isLaidOut();
        }

        static boolean isLayoutDirectionResolved(View view) {
            return view.isLayoutDirectionResolved();
        }

        static void notifySubtreeAccessibilityStateChanged(ViewParent viewParent, View child, View source, int changeType) {
            viewParent.notifySubtreeAccessibilityStateChanged(child, source, changeType);
        }

        static void setAccessibilityLiveRegion(View view, int mode) {
            view.setAccessibilityLiveRegion(mode);
        }

        static void setContentChangeTypes(AccessibilityEvent accessibilityEvent, int changeTypes) {
            accessibilityEvent.setContentChangeTypes(changeTypes);
        }
    }

    static class Api20Impl {
        private Api20Impl() {
        }

        static WindowInsets dispatchApplyWindowInsets(View view, WindowInsets insets) {
            return view.dispatchApplyWindowInsets(insets);
        }

        static WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
            return view.onApplyWindowInsets(insets);
        }

        static void requestApplyInsets(View view) {
            view.requestApplyInsets();
        }
    }

    private static class Api21Impl {
        private Api21Impl() {
        }

        static void callCompatInsetAnimationCallback(WindowInsets insets, View v) {
            View.OnApplyWindowInsetsListener onApplyWindowInsetsListener = (View.OnApplyWindowInsetsListener) v.getTag(R.id.tag_window_insets_animation_callback);
            if (onApplyWindowInsetsListener != null) {
                onApplyWindowInsetsListener.onApplyWindowInsets(v, insets);
            }
        }

        static WindowInsetsCompat computeSystemWindowInsets(View v, WindowInsetsCompat insets, Rect outLocalInsets) {
            WindowInsets windowInsets = insets.toWindowInsets();
            if (windowInsets != null) {
                return WindowInsetsCompat.toWindowInsetsCompat(v.computeSystemWindowInsets(windowInsets, outLocalInsets), v);
            }
            outLocalInsets.setEmpty();
            return insets;
        }

        static boolean dispatchNestedFling(View view, float velocityX, float velocityY, boolean consumed) {
            return view.dispatchNestedFling(velocityX, velocityY, consumed);
        }

        static boolean dispatchNestedPreFling(View view, float velocityX, float velocityY) {
            return view.dispatchNestedPreFling(velocityX, velocityY);
        }

        static boolean dispatchNestedPreScroll(View view, int dx, int dy, int[] consumed, int[] offsetInWindow) {
            return view.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
        }

        static boolean dispatchNestedScroll(View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
            return view.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
        }

        static ColorStateList getBackgroundTintList(View view) {
            return view.getBackgroundTintList();
        }

        static PorterDuff.Mode getBackgroundTintMode(View view) {
            return view.getBackgroundTintMode();
        }

        static float getElevation(View view) {
            return view.getElevation();
        }

        public static WindowInsetsCompat getRootWindowInsets(View v) {
            return WindowInsetsCompat.Api21ReflectionHolder.getRootWindowInsets(v);
        }

        static String getTransitionName(View view) {
            return view.getTransitionName();
        }

        static float getTranslationZ(View view) {
            return view.getTranslationZ();
        }

        static float getZ(View view) {
            return view.getZ();
        }

        static boolean hasNestedScrollingParent(View view) {
            return view.hasNestedScrollingParent();
        }

        static boolean isImportantForAccessibility(View view) {
            return view.isImportantForAccessibility();
        }

        static boolean isNestedScrollingEnabled(View view) {
            return view.isNestedScrollingEnabled();
        }

        static void setBackgroundTintList(View view, ColorStateList tint) {
            view.setBackgroundTintList(tint);
        }

        static void setBackgroundTintMode(View view, PorterDuff.Mode tintMode) {
            view.setBackgroundTintMode(tintMode);
        }

        static void setElevation(View view, float elevation) {
            view.setElevation(elevation);
        }

        static void setNestedScrollingEnabled(View view, boolean enabled) {
            view.setNestedScrollingEnabled(enabled);
        }

        static void setOnApplyWindowInsetsListener(final View v, final OnApplyWindowInsetsListener listener) {
            if (Build.VERSION.SDK_INT < 30) {
                v.setTag(R.id.tag_on_apply_window_listener, listener);
            }
            if (listener == null) {
                v.setOnApplyWindowInsetsListener((View.OnApplyWindowInsetsListener) v.getTag(R.id.tag_window_insets_animation_callback));
            } else {
                v.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                    WindowInsetsCompat mLastInsets = null;

                    public WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
                        WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(insets, view);
                        if (Build.VERSION.SDK_INT < 30) {
                            Api21Impl.callCompatInsetAnimationCallback(insets, v);
                            if (windowInsetsCompat.equals(this.mLastInsets)) {
                                return listener.onApplyWindowInsets(view, windowInsetsCompat).toWindowInsets();
                            }
                        }
                        this.mLastInsets = windowInsetsCompat;
                        WindowInsetsCompat onApplyWindowInsets = listener.onApplyWindowInsets(view, windowInsetsCompat);
                        if (Build.VERSION.SDK_INT >= 30) {
                            return onApplyWindowInsets.toWindowInsets();
                        }
                        ViewCompat.requestApplyInsets(view);
                        return onApplyWindowInsets.toWindowInsets();
                    }
                });
            }
        }

        static void setTransitionName(View view, String transitionName) {
            view.setTransitionName(transitionName);
        }

        static void setTranslationZ(View view, float translationZ) {
            view.setTranslationZ(translationZ);
        }

        static void setZ(View view, float z) {
            view.setZ(z);
        }

        static boolean startNestedScroll(View view, int axes) {
            return view.startNestedScroll(axes);
        }

        static void stopNestedScroll(View view) {
            view.stopNestedScroll();
        }
    }

    private static class Api23Impl {
        private Api23Impl() {
        }

        public static WindowInsetsCompat getRootWindowInsets(View v) {
            WindowInsets rootWindowInsets = v.getRootWindowInsets();
            if (rootWindowInsets == null) {
                return null;
            }
            WindowInsetsCompat windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(rootWindowInsets);
            windowInsetsCompat.setRootWindowInsets(windowInsetsCompat);
            windowInsetsCompat.copyRootViewBounds(v.getRootView());
            return windowInsetsCompat;
        }

        static int getScrollIndicators(View view) {
            return view.getScrollIndicators();
        }

        static void setScrollIndicators(View view, int indicators) {
            view.setScrollIndicators(indicators);
        }

        static void setScrollIndicators(View view, int indicators, int mask) {
            view.setScrollIndicators(indicators, mask);
        }
    }

    static class Api24Impl {
        private Api24Impl() {
        }

        static void cancelDragAndDrop(View view) {
            view.cancelDragAndDrop();
        }

        static void dispatchFinishTemporaryDetach(View view) {
            view.dispatchFinishTemporaryDetach();
        }

        static void dispatchStartTemporaryDetach(View view) {
            view.dispatchStartTemporaryDetach();
        }

        static void setPointerIcon(View view, PointerIcon pointerIcon) {
            view.setPointerIcon(pointerIcon);
        }

        static boolean startDragAndDrop(View view, ClipData data, View.DragShadowBuilder shadowBuilder, Object myLocalState, int flags) {
            return view.startDragAndDrop(data, shadowBuilder, myLocalState, flags);
        }

        static void updateDragShadow(View view, View.DragShadowBuilder shadowBuilder) {
            view.updateDragShadow(shadowBuilder);
        }
    }

    static class Api26Impl {
        private Api26Impl() {
        }

        static void addKeyboardNavigationClusters(View view, Collection<View> collection, int direction) {
            view.addKeyboardNavigationClusters(collection, direction);
        }

        static int getImportantForAutofill(View view) {
            return view.getImportantForAutofill();
        }

        static int getNextClusterForwardId(View view) {
            return view.getNextClusterForwardId();
        }

        static boolean hasExplicitFocusable(View view) {
            return view.hasExplicitFocusable();
        }

        static boolean isFocusedByDefault(View view) {
            return view.isFocusedByDefault();
        }

        static boolean isImportantForAutofill(View view) {
            return view.isImportantForAutofill();
        }

        static boolean isKeyboardNavigationCluster(View view) {
            return view.isKeyboardNavigationCluster();
        }

        static View keyboardNavigationClusterSearch(View view, View currentCluster, int direction) {
            return view.keyboardNavigationClusterSearch(currentCluster, direction);
        }

        static boolean restoreDefaultFocus(View view) {
            return view.restoreDefaultFocus();
        }

        static void setAutofillHints(View view, String... autofillHints) {
            view.setAutofillHints(autofillHints);
        }

        static void setFocusedByDefault(View view, boolean isFocusedByDefault) {
            view.setFocusedByDefault(isFocusedByDefault);
        }

        static void setImportantForAutofill(View view, int mode) {
            view.setImportantForAutofill(mode);
        }

        static void setKeyboardNavigationCluster(View view, boolean isCluster) {
            view.setKeyboardNavigationCluster(isCluster);
        }

        static void setNextClusterForwardId(View view, int nextClusterForwardId) {
            view.setNextClusterForwardId(nextClusterForwardId);
        }

        static void setTooltipText(View view, CharSequence tooltipText) {
            view.setTooltipText(tooltipText);
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static void addOnUnhandledKeyEventListener(View v, OnUnhandledKeyEventListenerCompat listener) {
            SimpleArrayMap simpleArrayMap = (SimpleArrayMap) v.getTag(R.id.tag_unhandled_key_listeners);
            if (simpleArrayMap == null) {
                simpleArrayMap = new SimpleArrayMap();
                v.setTag(R.id.tag_unhandled_key_listeners, simpleArrayMap);
            }
            Objects.requireNonNull(listener);
            ViewCompat$Api28Impl$$ExternalSyntheticLambda0 viewCompat$Api28Impl$$ExternalSyntheticLambda0 = new ViewCompat$Api28Impl$$ExternalSyntheticLambda0(listener);
            simpleArrayMap.put(listener, viewCompat$Api28Impl$$ExternalSyntheticLambda0);
            v.addOnUnhandledKeyEventListener(viewCompat$Api28Impl$$ExternalSyntheticLambda0);
        }

        static CharSequence getAccessibilityPaneTitle(View view) {
            return view.getAccessibilityPaneTitle();
        }

        static boolean isAccessibilityHeading(View view) {
            return view.isAccessibilityHeading();
        }

        static boolean isScreenReaderFocusable(View view) {
            return view.isScreenReaderFocusable();
        }

        static void removeOnUnhandledKeyEventListener(View v, OnUnhandledKeyEventListenerCompat listener) {
            View.OnUnhandledKeyEventListener onUnhandledKeyEventListener;
            SimpleArrayMap simpleArrayMap = (SimpleArrayMap) v.getTag(R.id.tag_unhandled_key_listeners);
            if (simpleArrayMap != null && (onUnhandledKeyEventListener = (View.OnUnhandledKeyEventListener) simpleArrayMap.get(listener)) != null) {
                v.removeOnUnhandledKeyEventListener(onUnhandledKeyEventListener);
            }
        }

        static <T> T requireViewById(View view, int id) {
            return view.requireViewById(id);
        }

        static void setAccessibilityHeading(View view, boolean isHeading) {
            view.setAccessibilityHeading(isHeading);
        }

        static void setAccessibilityPaneTitle(View view, CharSequence accessibilityPaneTitle) {
            view.setAccessibilityPaneTitle(accessibilityPaneTitle);
        }

        static void setScreenReaderFocusable(View view, boolean screenReaderFocusable) {
            view.setScreenReaderFocusable(screenReaderFocusable);
        }
    }

    private static class Api29Impl {
        private Api29Impl() {
        }

        static View.AccessibilityDelegate getAccessibilityDelegate(View view) {
            return view.getAccessibilityDelegate();
        }

        static List<Rect> getSystemGestureExclusionRects(View view) {
            return view.getSystemGestureExclusionRects();
        }

        static void saveAttributeDataForStyleable(View view, Context context, int[] styleable, AttributeSet attrs, TypedArray t, int defStyleAttr, int defStyleRes) {
            view.saveAttributeDataForStyleable(context, styleable, attrs, t, defStyleAttr, defStyleRes);
        }

        static void setSystemGestureExclusionRects(View view, List<Rect> list) {
            view.setSystemGestureExclusionRects(list);
        }
    }

    private static class Api30Impl {
        private Api30Impl() {
        }

        static CharSequence getStateDescription(View view) {
            return view.getStateDescription();
        }

        public static WindowInsetsControllerCompat getWindowInsetsController(View view) {
            WindowInsetsController windowInsetsController = view.getWindowInsetsController();
            if (windowInsetsController != null) {
                return WindowInsetsControllerCompat.toWindowInsetsControllerCompat(windowInsetsController);
            }
            return null;
        }

        static void setStateDescription(View view, CharSequence stateDescription) {
            view.setStateDescription(stateDescription);
        }
    }

    private static final class Api31Impl {
        private Api31Impl() {
        }

        public static String[] getReceiveContentMimeTypes(View view) {
            return view.getReceiveContentMimeTypes();
        }

        public static ContentInfoCompat performReceiveContent(View view, ContentInfoCompat payload) {
            ContentInfo contentInfo = payload.toContentInfo();
            ContentInfo performReceiveContent = view.performReceiveContent(contentInfo);
            if (performReceiveContent == null) {
                return null;
            }
            return performReceiveContent == contentInfo ? payload : ContentInfoCompat.toContentInfoCompat(performReceiveContent);
        }

        public static void setOnReceiveContentListener(View view, String[] mimeTypes, OnReceiveContentListener listener) {
            if (listener == null) {
                view.setOnReceiveContentListener(mimeTypes, (OnReceiveContentListener) null);
            } else {
                view.setOnReceiveContentListener(mimeTypes, new OnReceiveContentListenerAdapter(listener));
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusDirection {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusRealDirection {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusRelativeDirection {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NestedScrollType {
    }

    private static final class OnReceiveContentListenerAdapter implements OnReceiveContentListener {
        private final OnReceiveContentListener mJetpackListener;

        OnReceiveContentListenerAdapter(OnReceiveContentListener jetpackListener) {
            this.mJetpackListener = jetpackListener;
        }

        public ContentInfo onReceiveContent(View view, ContentInfo platPayload) {
            ContentInfoCompat contentInfoCompat = ContentInfoCompat.toContentInfoCompat(platPayload);
            ContentInfoCompat onReceiveContent = this.mJetpackListener.onReceiveContent(view, contentInfoCompat);
            if (onReceiveContent == null) {
                return null;
            }
            return onReceiveContent == contentInfoCompat ? platPayload : onReceiveContent.toContentInfo();
        }
    }

    public interface OnUnhandledKeyEventListenerCompat {
        boolean onUnhandledKeyEvent(View view, KeyEvent keyEvent);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollAxis {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollIndicators {
    }

    static class UnhandledKeyEventManager {
        private static final ArrayList<WeakReference<View>> sViewsWithListeners = new ArrayList<>();
        private SparseArray<WeakReference<View>> mCapturedKeys = null;
        private WeakReference<KeyEvent> mLastDispatchedPreViewKeyEvent = null;
        private WeakHashMap<View, Boolean> mViewsContainingListeners = null;

        UnhandledKeyEventManager() {
        }

        static UnhandledKeyEventManager at(View root) {
            UnhandledKeyEventManager unhandledKeyEventManager = (UnhandledKeyEventManager) root.getTag(R.id.tag_unhandled_key_event_manager);
            if (unhandledKeyEventManager != null) {
                return unhandledKeyEventManager;
            }
            UnhandledKeyEventManager unhandledKeyEventManager2 = new UnhandledKeyEventManager();
            root.setTag(R.id.tag_unhandled_key_event_manager, unhandledKeyEventManager2);
            return unhandledKeyEventManager2;
        }

        private View dispatchInOrder(View view, KeyEvent event) {
            WeakHashMap<View, Boolean> weakHashMap = this.mViewsContainingListeners;
            if (weakHashMap == null || !weakHashMap.containsKey(view)) {
                return null;
            }
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                    View dispatchInOrder = dispatchInOrder(viewGroup.getChildAt(childCount), event);
                    if (dispatchInOrder != null) {
                        return dispatchInOrder;
                    }
                }
            }
            if (onUnhandledKeyEvent(view, event)) {
                return view;
            }
            return null;
        }

        private SparseArray<WeakReference<View>> getCapturedKeys() {
            if (this.mCapturedKeys == null) {
                this.mCapturedKeys = new SparseArray<>();
            }
            return this.mCapturedKeys;
        }

        private boolean onUnhandledKeyEvent(View v, KeyEvent event) {
            ArrayList arrayList = (ArrayList) v.getTag(R.id.tag_unhandled_key_listeners);
            if (arrayList == null) {
                return false;
            }
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                if (((OnUnhandledKeyEventListenerCompat) arrayList.get(size)).onUnhandledKeyEvent(v, event)) {
                    return true;
                }
            }
            return false;
        }

        private void recalcViewsWithUnhandled() {
            WeakHashMap<View, Boolean> weakHashMap = this.mViewsContainingListeners;
            if (weakHashMap != null) {
                weakHashMap.clear();
            }
            ArrayList<WeakReference<View>> arrayList = sViewsWithListeners;
            if (!arrayList.isEmpty()) {
                synchronized (arrayList) {
                    if (this.mViewsContainingListeners == null) {
                        this.mViewsContainingListeners = new WeakHashMap<>();
                    }
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        ArrayList<WeakReference<View>> arrayList2 = sViewsWithListeners;
                        View view = (View) arrayList2.get(size).get();
                        if (view == null) {
                            arrayList2.remove(size);
                        } else {
                            this.mViewsContainingListeners.put(view, Boolean.TRUE);
                            for (ViewParent parent = view.getParent(); parent instanceof View; parent = parent.getParent()) {
                                this.mViewsContainingListeners.put((View) parent, Boolean.TRUE);
                            }
                        }
                    }
                }
            }
        }

        static void registerListeningView(View v) {
            ArrayList<WeakReference<View>> arrayList = sViewsWithListeners;
            synchronized (arrayList) {
                Iterator<WeakReference<View>> it = arrayList.iterator();
                while (it.hasNext()) {
                    if (it.next().get() == v) {
                        return;
                    }
                }
                sViewsWithListeners.add(new WeakReference(v));
            }
        }

        static void unregisterListeningView(View v) {
            synchronized (sViewsWithListeners) {
                int i = 0;
                while (true) {
                    ArrayList<WeakReference<View>> arrayList = sViewsWithListeners;
                    if (i >= arrayList.size()) {
                        return;
                    }
                    if (arrayList.get(i).get() == v) {
                        arrayList.remove(i);
                        return;
                    }
                    i++;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public boolean dispatch(View root, KeyEvent event) {
            if (event.getAction() == 0) {
                recalcViewsWithUnhandled();
            }
            View dispatchInOrder = dispatchInOrder(root, event);
            if (event.getAction() == 0) {
                int keyCode = event.getKeyCode();
                if (dispatchInOrder != null && !KeyEvent.isModifierKey(keyCode)) {
                    getCapturedKeys().put(keyCode, new WeakReference(dispatchInOrder));
                }
            }
            return dispatchInOrder != null;
        }

        /* access modifiers changed from: package-private */
        public boolean preDispatch(KeyEvent event) {
            int indexOfKey;
            WeakReference<KeyEvent> weakReference = this.mLastDispatchedPreViewKeyEvent;
            if (weakReference != null && weakReference.get() == event) {
                return false;
            }
            this.mLastDispatchedPreViewKeyEvent = new WeakReference<>(event);
            WeakReference weakReference2 = null;
            SparseArray<WeakReference<View>> capturedKeys = getCapturedKeys();
            if (event.getAction() == 1 && (indexOfKey = capturedKeys.indexOfKey(event.getKeyCode())) >= 0) {
                weakReference2 = capturedKeys.valueAt(indexOfKey);
                capturedKeys.removeAt(indexOfKey);
            }
            if (weakReference2 == null) {
                weakReference2 = capturedKeys.get(event.getKeyCode());
            }
            if (weakReference2 == null) {
                return false;
            }
            View view = (View) weakReference2.get();
            if (view != null && ViewCompat.isAttachedToWindow(view)) {
                onUnhandledKeyEvent(view, event);
            }
            return true;
        }
    }

    @Deprecated
    protected ViewCompat() {
    }

    private static AccessibilityViewProperty<Boolean> accessibilityHeadingProperty() {
        return new AccessibilityViewProperty<Boolean>(R.id.tag_accessibility_heading, Boolean.class, 28) {
            /* access modifiers changed from: package-private */
            public Boolean frameworkGet(View view) {
                return Boolean.valueOf(Api28Impl.isAccessibilityHeading(view));
            }

            /* access modifiers changed from: package-private */
            public void frameworkSet(View view, Boolean value) {
                Api28Impl.setAccessibilityHeading(view, value.booleanValue());
            }

            /* access modifiers changed from: package-private */
            public boolean shouldUpdate(Boolean oldValue, Boolean newValue) {
                return !booleanNullToFalseEquals(oldValue, newValue);
            }
        };
    }

    public static int addAccessibilityAction(View view, CharSequence label, AccessibilityViewCommand command) {
        int availableActionIdFromResources = getAvailableActionIdFromResources(view, label);
        if (availableActionIdFromResources != -1) {
            addAccessibilityAction(view, new AccessibilityNodeInfoCompat.AccessibilityActionCompat(availableActionIdFromResources, label, command));
        }
        return availableActionIdFromResources;
    }

    private static void addAccessibilityAction(View view, AccessibilityNodeInfoCompat.AccessibilityActionCompat action) {
        if (Build.VERSION.SDK_INT >= 21) {
            ensureAccessibilityDelegateCompat(view);
            removeActionWithId(action.getId(), view);
            getActionList(view).add(action);
            notifyViewAccessibilityStateChangedIfNeeded(view, 0);
        }
    }

    public static void addKeyboardNavigationClusters(View view, Collection<View> collection, int direction) {
        if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.addKeyboardNavigationClusters(view, collection, direction);
        }
    }

    public static void addOnUnhandledKeyEventListener(View v, OnUnhandledKeyEventListenerCompat listener) {
        if (Build.VERSION.SDK_INT >= 28) {
            Api28Impl.addOnUnhandledKeyEventListener(v, listener);
            return;
        }
        ArrayList arrayList = (ArrayList) v.getTag(R.id.tag_unhandled_key_listeners);
        if (arrayList == null) {
            arrayList = new ArrayList();
            v.setTag(R.id.tag_unhandled_key_listeners, arrayList);
        }
        arrayList.add(listener);
        if (arrayList.size() == 1) {
            UnhandledKeyEventManager.registerListeningView(v);
        }
    }

    public static ViewPropertyAnimatorCompat animate(View view) {
        if (sViewPropertyAnimatorMap == null) {
            sViewPropertyAnimatorMap = new WeakHashMap<>();
        }
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = sViewPropertyAnimatorMap.get(view);
        if (viewPropertyAnimatorCompat != null) {
            return viewPropertyAnimatorCompat;
        }
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat2 = new ViewPropertyAnimatorCompat(view);
        sViewPropertyAnimatorMap.put(view, viewPropertyAnimatorCompat2);
        return viewPropertyAnimatorCompat2;
    }

    private static void bindTempDetach() {
        try {
            sDispatchStartTemporaryDetach = View.class.getDeclaredMethod("dispatchStartTemporaryDetach", new Class[0]);
            sDispatchFinishTemporaryDetach = View.class.getDeclaredMethod("dispatchFinishTemporaryDetach", new Class[0]);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "Couldn't find method", e);
        }
        sTempDetachBound = true;
    }

    @Deprecated
    public static boolean canScrollHorizontally(View view, int direction) {
        return view.canScrollHorizontally(direction);
    }

    @Deprecated
    public static boolean canScrollVertically(View view, int direction) {
        return view.canScrollVertically(direction);
    }

    public static void cancelDragAndDrop(View v) {
        if (Build.VERSION.SDK_INT >= 24) {
            Api24Impl.cancelDragAndDrop(v);
        }
    }

    @Deprecated
    public static int combineMeasuredStates(int curState, int newState) {
        return View.combineMeasuredStates(curState, newState);
    }

    private static void compatOffsetLeftAndRight(View view, int offset) {
        view.offsetLeftAndRight(offset);
        if (view.getVisibility() == 0) {
            tickleInvalidationFlag(view);
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                tickleInvalidationFlag((View) parent);
            }
        }
    }

    private static void compatOffsetTopAndBottom(View view, int offset) {
        view.offsetTopAndBottom(offset);
        if (view.getVisibility() == 0) {
            tickleInvalidationFlag(view);
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                tickleInvalidationFlag((View) parent);
            }
        }
    }

    public static WindowInsetsCompat computeSystemWindowInsets(View view, WindowInsetsCompat insets, Rect outLocalInsets) {
        return Build.VERSION.SDK_INT >= 21 ? Api21Impl.computeSystemWindowInsets(view, insets, outLocalInsets) : insets;
    }

    public static WindowInsetsCompat dispatchApplyWindowInsets(View view, WindowInsetsCompat insets) {
        WindowInsets windowInsets;
        if (Build.VERSION.SDK_INT >= 21 && (windowInsets = insets.toWindowInsets()) != null) {
            WindowInsets dispatchApplyWindowInsets = Api20Impl.dispatchApplyWindowInsets(view, windowInsets);
            if (!dispatchApplyWindowInsets.equals(windowInsets)) {
                return WindowInsetsCompat.toWindowInsetsCompat(dispatchApplyWindowInsets, view);
            }
        }
        return insets;
    }

    public static void dispatchFinishTemporaryDetach(View view) {
        if (Build.VERSION.SDK_INT >= 24) {
            Api24Impl.dispatchFinishTemporaryDetach(view);
            return;
        }
        if (!sTempDetachBound) {
            bindTempDetach();
        }
        Method method = sDispatchFinishTemporaryDetach;
        if (method != null) {
            try {
                method.invoke(view, new Object[0]);
            } catch (Exception e) {
                Log.d(TAG, "Error calling dispatchFinishTemporaryDetach", e);
            }
        } else {
            view.onFinishTemporaryDetach();
        }
    }

    public static boolean dispatchNestedFling(View view, float velocityX, float velocityY, boolean consumed) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.dispatchNestedFling(view, velocityX, velocityY, consumed);
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild) view).dispatchNestedFling(velocityX, velocityY, consumed);
        }
        return false;
    }

    public static boolean dispatchNestedPreFling(View view, float velocityX, float velocityY) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.dispatchNestedPreFling(view, velocityX, velocityY);
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild) view).dispatchNestedPreFling(velocityX, velocityY);
        }
        return false;
    }

    public static boolean dispatchNestedPreScroll(View view, int dx, int dy, int[] consumed, int[] offsetInWindow) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.dispatchNestedPreScroll(view, dx, dy, consumed, offsetInWindow);
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild) view).dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
        }
        return false;
    }

    public static boolean dispatchNestedPreScroll(View view, int dx, int dy, int[] consumed, int[] offsetInWindow, int type) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2) view).dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
        }
        if (type == 0) {
            return dispatchNestedPreScroll(view, dx, dy, consumed, offsetInWindow);
        }
        return false;
    }

    public static void dispatchNestedScroll(View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type, int[] consumed) {
        View view2 = view;
        if (view2 instanceof NestedScrollingChild3) {
            ((NestedScrollingChild3) view2).dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed);
        } else {
            dispatchNestedScroll(view, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
        }
    }

    public static boolean dispatchNestedScroll(View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.dispatchNestedScroll(view, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild) view).dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
        }
        return false;
    }

    public static boolean dispatchNestedScroll(View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2) view).dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
        }
        if (type == 0) {
            return dispatchNestedScroll(view, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
        }
        return false;
    }

    public static void dispatchStartTemporaryDetach(View view) {
        if (Build.VERSION.SDK_INT >= 24) {
            Api24Impl.dispatchStartTemporaryDetach(view);
            return;
        }
        if (!sTempDetachBound) {
            bindTempDetach();
        }
        Method method = sDispatchStartTemporaryDetach;
        if (method != null) {
            try {
                method.invoke(view, new Object[0]);
            } catch (Exception e) {
                Log.d(TAG, "Error calling dispatchStartTemporaryDetach", e);
            }
        } else {
            view.onStartTemporaryDetach();
        }
    }

    static boolean dispatchUnhandledKeyEventBeforeCallback(View root, KeyEvent evt) {
        if (Build.VERSION.SDK_INT >= 28) {
            return false;
        }
        return UnhandledKeyEventManager.at(root).dispatch(root, evt);
    }

    static boolean dispatchUnhandledKeyEventBeforeHierarchy(View root, KeyEvent evt) {
        if (Build.VERSION.SDK_INT >= 28) {
            return false;
        }
        return UnhandledKeyEventManager.at(root).preDispatch(evt);
    }

    public static void enableAccessibleClickableSpanSupport(View view) {
        if (Build.VERSION.SDK_INT >= 19) {
            ensureAccessibilityDelegateCompat(view);
        }
    }

    static void ensureAccessibilityDelegateCompat(View v) {
        AccessibilityDelegateCompat accessibilityDelegate = getAccessibilityDelegate(v);
        if (accessibilityDelegate == null) {
            accessibilityDelegate = new AccessibilityDelegateCompat();
        }
        setAccessibilityDelegate(v, accessibilityDelegate);
    }

    public static int generateViewId() {
        AtomicInteger atomicInteger;
        int i;
        int i2;
        if (Build.VERSION.SDK_INT >= 17) {
            return Api17Impl.generateViewId();
        }
        do {
            atomicInteger = sNextGeneratedId;
            i = atomicInteger.get();
            i2 = i + 1;
            if (i2 > 16777215) {
                i2 = 1;
            }
        } while (!atomicInteger.compareAndSet(i, i2));
        return i;
    }

    public static AccessibilityDelegateCompat getAccessibilityDelegate(View view) {
        View.AccessibilityDelegate accessibilityDelegateInternal = getAccessibilityDelegateInternal(view);
        if (accessibilityDelegateInternal == null) {
            return null;
        }
        return accessibilityDelegateInternal instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter ? ((AccessibilityDelegateCompat.AccessibilityDelegateAdapter) accessibilityDelegateInternal).mCompat : new AccessibilityDelegateCompat(accessibilityDelegateInternal);
    }

    private static View.AccessibilityDelegate getAccessibilityDelegateInternal(View v) {
        return Build.VERSION.SDK_INT >= 29 ? Api29Impl.getAccessibilityDelegate(v) : getAccessibilityDelegateThroughReflection(v);
    }

    private static View.AccessibilityDelegate getAccessibilityDelegateThroughReflection(View v) {
        if (sAccessibilityDelegateCheckFailed) {
            return null;
        }
        if (sAccessibilityDelegateField == null) {
            try {
                Field declaredField = View.class.getDeclaredField("mAccessibilityDelegate");
                sAccessibilityDelegateField = declaredField;
                declaredField.setAccessible(true);
            } catch (Throwable th) {
                sAccessibilityDelegateCheckFailed = true;
                return null;
            }
        }
        try {
            Object obj = sAccessibilityDelegateField.get(v);
            if (obj instanceof View.AccessibilityDelegate) {
                return (View.AccessibilityDelegate) obj;
            }
            return null;
        } catch (Throwable th2) {
            sAccessibilityDelegateCheckFailed = true;
            return null;
        }
    }

    public static int getAccessibilityLiveRegion(View view) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Api19Impl.getAccessibilityLiveRegion(view);
        }
        return 0;
    }

    public static AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
        AccessibilityNodeProvider accessibilityNodeProvider;
        if (Build.VERSION.SDK_INT < 16 || (accessibilityNodeProvider = Api16Impl.getAccessibilityNodeProvider(view)) == null) {
            return null;
        }
        return new AccessibilityNodeProviderCompat(accessibilityNodeProvider);
    }

    public static CharSequence getAccessibilityPaneTitle(View view) {
        return paneTitleProperty().get(view);
    }

    private static List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> getActionList(View view) {
        ArrayList arrayList = (ArrayList) view.getTag(R.id.tag_accessibility_actions);
        if (arrayList != null) {
            return arrayList;
        }
        ArrayList arrayList2 = new ArrayList();
        view.setTag(R.id.tag_accessibility_actions, arrayList2);
        return arrayList2;
    }

    @Deprecated
    public static float getAlpha(View view) {
        return view.getAlpha();
    }

    private static int getAvailableActionIdFromResources(View view, CharSequence label) {
        int i = -1;
        List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> actionList = getActionList(view);
        for (int i2 = 0; i2 < actionList.size(); i2++) {
            if (TextUtils.equals(label, actionList.get(i2).getLabel())) {
                return actionList.get(i2).getId();
            }
        }
        int i3 = 0;
        while (true) {
            int[] iArr = ACCESSIBILITY_ACTIONS_RESOURCE_IDS;
            if (i3 >= iArr.length || i != -1) {
                return i;
            }
            int i4 = iArr[i3];
            boolean z = true;
            for (int i5 = 0; i5 < actionList.size(); i5++) {
                z &= actionList.get(i5).getId() != i4;
            }
            if (z) {
                i = i4;
            }
            i3++;
        }
        return i;
    }

    public static ColorStateList getBackgroundTintList(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getBackgroundTintList(view);
        }
        if (view instanceof TintableBackgroundView) {
            return ((TintableBackgroundView) view).getSupportBackgroundTintList();
        }
        return null;
    }

    public static PorterDuff.Mode getBackgroundTintMode(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getBackgroundTintMode(view);
        }
        if (view instanceof TintableBackgroundView) {
            return ((TintableBackgroundView) view).getSupportBackgroundTintMode();
        }
        return null;
    }

    public static Rect getClipBounds(View view) {
        if (Build.VERSION.SDK_INT >= 18) {
            return Api18Impl.getClipBounds(view);
        }
        return null;
    }

    public static Display getDisplay(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            return Api17Impl.getDisplay(view);
        }
        if (isAttachedToWindow(view)) {
            return ((WindowManager) view.getContext().getSystemService("window")).getDefaultDisplay();
        }
        return null;
    }

    public static float getElevation(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getElevation(view);
        }
        return 0.0f;
    }

    private static Rect getEmptyTempRect() {
        if (sThreadLocalRect == null) {
            sThreadLocalRect = new ThreadLocal<>();
        }
        Rect rect = sThreadLocalRect.get();
        if (rect == null) {
            rect = new Rect();
            sThreadLocalRect.set(rect);
        }
        rect.setEmpty();
        return rect;
    }

    private static OnReceiveContentViewBehavior getFallback(View view) {
        return view instanceof OnReceiveContentViewBehavior ? (OnReceiveContentViewBehavior) view : NO_OP_ON_RECEIVE_CONTENT_VIEW_BEHAVIOR;
    }

    public static boolean getFitsSystemWindows(View v) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.getFitsSystemWindows(v);
        }
        return false;
    }

    public static int getImportantForAccessibility(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.getImportantForAccessibility(view);
        }
        return 0;
    }

    public static int getImportantForAutofill(View v) {
        if (Build.VERSION.SDK_INT >= 26) {
            return Api26Impl.getImportantForAutofill(v);
        }
        return 0;
    }

    public static int getLabelFor(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            return Api17Impl.getLabelFor(view);
        }
        return 0;
    }

    @Deprecated
    public static int getLayerType(View view) {
        return view.getLayerType();
    }

    public static int getLayoutDirection(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            return Api17Impl.getLayoutDirection(view);
        }
        return 0;
    }

    @Deprecated
    public static Matrix getMatrix(View view) {
        return view.getMatrix();
    }

    @Deprecated
    public static int getMeasuredHeightAndState(View view) {
        return view.getMeasuredHeightAndState();
    }

    @Deprecated
    public static int getMeasuredState(View view) {
        return view.getMeasuredState();
    }

    @Deprecated
    public static int getMeasuredWidthAndState(View view) {
        return view.getMeasuredWidthAndState();
    }

    public static int getMinimumHeight(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.getMinimumHeight(view);
        }
        if (!sMinHeightFieldFetched) {
            try {
                Field declaredField = View.class.getDeclaredField("mMinHeight");
                sMinHeightField = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException e) {
            }
            sMinHeightFieldFetched = true;
        }
        Field field = sMinHeightField;
        if (field == null) {
            return 0;
        }
        try {
            return ((Integer) field.get(view)).intValue();
        } catch (Exception e2) {
            return 0;
        }
    }

    public static int getMinimumWidth(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.getMinimumWidth(view);
        }
        if (!sMinWidthFieldFetched) {
            try {
                Field declaredField = View.class.getDeclaredField("mMinWidth");
                sMinWidthField = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException e) {
            }
            sMinWidthFieldFetched = true;
        }
        Field field = sMinWidthField;
        if (field == null) {
            return 0;
        }
        try {
            return ((Integer) field.get(view)).intValue();
        } catch (Exception e2) {
            return 0;
        }
    }

    public static int getNextClusterForwardId(View view) {
        if (Build.VERSION.SDK_INT >= 26) {
            return Api26Impl.getNextClusterForwardId(view);
        }
        return -1;
    }

    public static String[] getOnReceiveContentMimeTypes(View view) {
        return Build.VERSION.SDK_INT >= 31 ? Api31Impl.getReceiveContentMimeTypes(view) : (String[]) view.getTag(R.id.tag_on_receive_content_mime_types);
    }

    @Deprecated
    public static int getOverScrollMode(View v) {
        return v.getOverScrollMode();
    }

    public static int getPaddingEnd(View view) {
        return Build.VERSION.SDK_INT >= 17 ? Api17Impl.getPaddingEnd(view) : view.getPaddingRight();
    }

    public static int getPaddingStart(View view) {
        return Build.VERSION.SDK_INT >= 17 ? Api17Impl.getPaddingStart(view) : view.getPaddingLeft();
    }

    public static ViewParent getParentForAccessibility(View view) {
        return Build.VERSION.SDK_INT >= 16 ? Api16Impl.getParentForAccessibility(view) : view.getParent();
    }

    @Deprecated
    public static float getPivotX(View view) {
        return view.getPivotX();
    }

    @Deprecated
    public static float getPivotY(View view) {
        return view.getPivotY();
    }

    public static WindowInsetsCompat getRootWindowInsets(View view) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.getRootWindowInsets(view);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getRootWindowInsets(view);
        }
        return null;
    }

    @Deprecated
    public static float getRotation(View view) {
        return view.getRotation();
    }

    @Deprecated
    public static float getRotationX(View view) {
        return view.getRotationX();
    }

    @Deprecated
    public static float getRotationY(View view) {
        return view.getRotationY();
    }

    @Deprecated
    public static float getScaleX(View view) {
        return view.getScaleX();
    }

    @Deprecated
    public static float getScaleY(View view) {
        return view.getScaleY();
    }

    public static int getScrollIndicators(View view) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.getScrollIndicators(view);
        }
        return 0;
    }

    public static CharSequence getStateDescription(View view) {
        return stateDescriptionProperty().get(view);
    }

    public static List<Rect> getSystemGestureExclusionRects(View view) {
        return Build.VERSION.SDK_INT >= 29 ? Api29Impl.getSystemGestureExclusionRects(view) : Collections.emptyList();
    }

    public static String getTransitionName(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            String transitionName = Api21Impl.getTransitionName(view);
            Log1F380D.a((Object) transitionName);
            return transitionName;
        }
        WeakHashMap<View, String> weakHashMap = sTransitionNameMap;
        if (weakHashMap == null) {
            return null;
        }
        return weakHashMap.get(view);
    }

    @Deprecated
    public static float getTranslationX(View view) {
        return view.getTranslationX();
    }

    @Deprecated
    public static float getTranslationY(View view) {
        return view.getTranslationY();
    }

    public static float getTranslationZ(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getTranslationZ(view);
        }
        return 0.0f;
    }

    @Deprecated
    public static WindowInsetsControllerCompat getWindowInsetsController(View view) {
        if (Build.VERSION.SDK_INT >= 30) {
            return Api30Impl.getWindowInsetsController(view);
        }
        for (Context context = view.getContext(); context instanceof ContextWrapper; context = ((ContextWrapper) context).getBaseContext()) {
            if (context instanceof Activity) {
                Window window = ((Activity) context).getWindow();
                if (window != null) {
                    return WindowCompat.getInsetsController(window, view);
                }
                return null;
            }
        }
        return null;
    }

    @Deprecated
    public static int getWindowSystemUiVisibility(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.getWindowSystemUiVisibility(view);
        }
        return 0;
    }

    @Deprecated
    public static float getX(View view) {
        return view.getX();
    }

    @Deprecated
    public static float getY(View view) {
        return view.getY();
    }

    public static float getZ(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getZ(view);
        }
        return 0.0f;
    }

    public static boolean hasAccessibilityDelegate(View view) {
        return getAccessibilityDelegateInternal(view) != null;
    }

    public static boolean hasExplicitFocusable(View view) {
        return Build.VERSION.SDK_INT >= 26 ? Api26Impl.hasExplicitFocusable(view) : view.hasFocusable();
    }

    public static boolean hasNestedScrollingParent(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.hasNestedScrollingParent(view);
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild) view).hasNestedScrollingParent();
        }
        return false;
    }

    public static boolean hasNestedScrollingParent(View view, int type) {
        if (view instanceof NestedScrollingChild2) {
            ((NestedScrollingChild2) view).hasNestedScrollingParent(type);
            return false;
        } else if (type == 0) {
            return hasNestedScrollingParent(view);
        } else {
            return false;
        }
    }

    public static boolean hasOnClickListeners(View view) {
        if (Build.VERSION.SDK_INT >= 15) {
            return Api15Impl.hasOnClickListeners(view);
        }
        return false;
    }

    public static boolean hasOverlappingRendering(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.hasOverlappingRendering(view);
        }
        return true;
    }

    public static boolean hasTransientState(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.hasTransientState(view);
        }
        return false;
    }

    public static boolean isAccessibilityHeading(View view) {
        Boolean bool = accessibilityHeadingProperty().get(view);
        return bool != null && bool.booleanValue();
    }

    public static boolean isAttachedToWindow(View view) {
        return Build.VERSION.SDK_INT >= 19 ? Api19Impl.isAttachedToWindow(view) : view.getWindowToken() != null;
    }

    public static boolean isFocusedByDefault(View view) {
        if (Build.VERSION.SDK_INT >= 26) {
            return Api26Impl.isFocusedByDefault(view);
        }
        return false;
    }

    public static boolean isImportantForAccessibility(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.isImportantForAccessibility(view);
        }
        return true;
    }

    public static boolean isImportantForAutofill(View v) {
        if (Build.VERSION.SDK_INT >= 26) {
            return Api26Impl.isImportantForAutofill(v);
        }
        return true;
    }

    public static boolean isInLayout(View view) {
        if (Build.VERSION.SDK_INT >= 18) {
            return Api18Impl.isInLayout(view);
        }
        return false;
    }

    public static boolean isKeyboardNavigationCluster(View view) {
        if (Build.VERSION.SDK_INT >= 26) {
            return Api26Impl.isKeyboardNavigationCluster(view);
        }
        return false;
    }

    public static boolean isLaidOut(View view) {
        return Build.VERSION.SDK_INT >= 19 ? Api19Impl.isLaidOut(view) : view.getWidth() > 0 && view.getHeight() > 0;
    }

    public static boolean isLayoutDirectionResolved(View view) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Api19Impl.isLayoutDirectionResolved(view);
        }
        return false;
    }

    public static boolean isNestedScrollingEnabled(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.isNestedScrollingEnabled(view);
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild) view).isNestedScrollingEnabled();
        }
        return false;
    }

    @Deprecated
    public static boolean isOpaque(View view) {
        return view.isOpaque();
    }

    public static boolean isPaddingRelative(View view) {
        if (Build.VERSION.SDK_INT >= 17) {
            return Api17Impl.isPaddingRelative(view);
        }
        return false;
    }

    public static boolean isScreenReaderFocusable(View view) {
        Boolean bool = screenReaderFocusableProperty().get(view);
        return bool != null && bool.booleanValue();
    }

    @Deprecated
    public static void jumpDrawablesToCurrentState(View v) {
        v.jumpDrawablesToCurrentState();
    }

    public static View keyboardNavigationClusterSearch(View view, View currentCluster, int direction) {
        if (Build.VERSION.SDK_INT >= 26) {
            return Api26Impl.keyboardNavigationClusterSearch(view, currentCluster, direction);
        }
        return null;
    }

    static /* synthetic */ ContentInfoCompat lambda$static$0(ContentInfoCompat payload) {
        return payload;
    }

    static void notifyViewAccessibilityStateChangedIfNeeded(View view, int changeType) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) view.getContext().getSystemService("accessibility");
        if (accessibilityManager.isEnabled()) {
            boolean z = getAccessibilityPaneTitle(view) != null && view.isShown() && view.getWindowVisibility() == 0;
            int i = 32;
            if (getAccessibilityLiveRegion(view) != 0 || z) {
                AccessibilityEvent obtain = AccessibilityEvent.obtain();
                if (!z) {
                    i = 2048;
                }
                obtain.setEventType(i);
                Api19Impl.setContentChangeTypes(obtain, changeType);
                if (z) {
                    obtain.getText().add(getAccessibilityPaneTitle(view));
                    setViewImportanceForAccessibilityIfNeeded(view);
                }
                view.sendAccessibilityEventUnchecked(obtain);
            } else if (changeType == 32) {
                AccessibilityEvent obtain2 = AccessibilityEvent.obtain();
                view.onInitializeAccessibilityEvent(obtain2);
                obtain2.setEventType(32);
                Api19Impl.setContentChangeTypes(obtain2, changeType);
                obtain2.setSource(view);
                view.onPopulateAccessibilityEvent(obtain2);
                obtain2.getText().add(getAccessibilityPaneTitle(view));
                accessibilityManager.sendAccessibilityEvent(obtain2);
            } else if (view.getParent() != null) {
                try {
                    Api19Impl.notifySubtreeAccessibilityStateChanged(view.getParent(), view, view, changeType);
                } catch (AbstractMethodError e) {
                    Log.e(TAG, view.getParent().getClass().getSimpleName() + " does not fully implement ViewParent", e);
                }
            }
        }
    }

    public static void offsetLeftAndRight(View view, int offset) {
        if (Build.VERSION.SDK_INT >= 23) {
            view.offsetLeftAndRight(offset);
        } else if (Build.VERSION.SDK_INT >= 21) {
            Rect emptyTempRect = getEmptyTempRect();
            boolean z = false;
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                View view2 = (View) parent;
                emptyTempRect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                z = !emptyTempRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            }
            compatOffsetLeftAndRight(view, offset);
            if (z && emptyTempRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View) parent).invalidate(emptyTempRect);
            }
        } else {
            compatOffsetLeftAndRight(view, offset);
        }
    }

    public static void offsetTopAndBottom(View view, int offset) {
        if (Build.VERSION.SDK_INT >= 23) {
            view.offsetTopAndBottom(offset);
        } else if (Build.VERSION.SDK_INT >= 21) {
            Rect emptyTempRect = getEmptyTempRect();
            boolean z = false;
            ViewParent parent = view.getParent();
            if (parent instanceof View) {
                View view2 = (View) parent;
                emptyTempRect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                z = !emptyTempRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            }
            compatOffsetTopAndBottom(view, offset);
            if (z && emptyTempRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View) parent).invalidate(emptyTempRect);
            }
        } else {
            compatOffsetTopAndBottom(view, offset);
        }
    }

    public static WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat insets) {
        WindowInsets windowInsets;
        if (Build.VERSION.SDK_INT >= 21 && (windowInsets = insets.toWindowInsets()) != null) {
            WindowInsets onApplyWindowInsets = Api20Impl.onApplyWindowInsets(view, windowInsets);
            if (!onApplyWindowInsets.equals(windowInsets)) {
                return WindowInsetsCompat.toWindowInsetsCompat(onApplyWindowInsets, view);
            }
        }
        return insets;
    }

    @Deprecated
    public static void onInitializeAccessibilityEvent(View v, AccessibilityEvent event) {
        v.onInitializeAccessibilityEvent(event);
    }

    public static void onInitializeAccessibilityNodeInfo(View v, AccessibilityNodeInfoCompat info) {
        v.onInitializeAccessibilityNodeInfo(info.unwrap());
    }

    @Deprecated
    public static void onPopulateAccessibilityEvent(View v, AccessibilityEvent event) {
        v.onPopulateAccessibilityEvent(event);
    }

    private static AccessibilityViewProperty<CharSequence> paneTitleProperty() {
        return new AccessibilityViewProperty<CharSequence>(R.id.tag_accessibility_pane_title, CharSequence.class, 8, 28) {
            /* access modifiers changed from: package-private */
            public CharSequence frameworkGet(View view) {
                return Api28Impl.getAccessibilityPaneTitle(view);
            }

            /* access modifiers changed from: package-private */
            public void frameworkSet(View view, CharSequence value) {
                Api28Impl.setAccessibilityPaneTitle(view, value);
            }

            /* access modifiers changed from: package-private */
            public boolean shouldUpdate(CharSequence oldValue, CharSequence newValue) {
                return !TextUtils.equals(oldValue, newValue);
            }
        };
    }

    public static boolean performAccessibilityAction(View view, int action, Bundle arguments) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.performAccessibilityAction(view, action, arguments);
        }
        return false;
    }

    public static ContentInfoCompat performReceiveContent(View view, ContentInfoCompat payload) {
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "performReceiveContent: " + payload + ", view=" + view.getClass().getSimpleName() + "[" + view.getId() + "]");
        }
        if (Build.VERSION.SDK_INT >= 31) {
            return Api31Impl.performReceiveContent(view, payload);
        }
        OnReceiveContentListener onReceiveContentListener = (OnReceiveContentListener) view.getTag(R.id.tag_on_receive_content_listener);
        if (onReceiveContentListener == null) {
            return getFallback(view).onReceiveContent(payload);
        }
        ContentInfoCompat onReceiveContent = onReceiveContentListener.onReceiveContent(view, payload);
        if (onReceiveContent == null) {
            return null;
        }
        return getFallback(view).onReceiveContent(onReceiveContent);
    }

    public static void postInvalidateOnAnimation(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.postInvalidateOnAnimation(view);
        } else {
            view.postInvalidate();
        }
    }

    public static void postInvalidateOnAnimation(View view, int left, int top, int right, int bottom) {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.postInvalidateOnAnimation(view, left, top, right, bottom);
        } else {
            view.postInvalidate(left, top, right, bottom);
        }
    }

    public static void postOnAnimation(View view, Runnable action) {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.postOnAnimation(view, action);
        } else {
            view.postDelayed(action, ValueAnimator.getFrameDelay());
        }
    }

    public static void postOnAnimationDelayed(View view, Runnable action, long delayMillis) {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.postOnAnimationDelayed(view, action, delayMillis);
        } else {
            view.postDelayed(action, ValueAnimator.getFrameDelay() + delayMillis);
        }
    }

    public static void removeAccessibilityAction(View view, int actionId) {
        if (Build.VERSION.SDK_INT >= 21) {
            removeActionWithId(actionId, view);
            notifyViewAccessibilityStateChangedIfNeeded(view, 0);
        }
    }

    private static void removeActionWithId(int actionId, View view) {
        List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> actionList = getActionList(view);
        for (int i = 0; i < actionList.size(); i++) {
            if (actionList.get(i).getId() == actionId) {
                actionList.remove(i);
                return;
            }
        }
    }

    public static void removeOnUnhandledKeyEventListener(View v, OnUnhandledKeyEventListenerCompat listener) {
        if (Build.VERSION.SDK_INT >= 28) {
            Api28Impl.removeOnUnhandledKeyEventListener(v, listener);
            return;
        }
        ArrayList arrayList = (ArrayList) v.getTag(R.id.tag_unhandled_key_listeners);
        if (arrayList != null) {
            arrayList.remove(listener);
            if (arrayList.size() == 0) {
                UnhandledKeyEventManager.unregisterListeningView(v);
            }
        }
    }

    public static void replaceAccessibilityAction(View view, AccessibilityNodeInfoCompat.AccessibilityActionCompat replacedAction, CharSequence label, AccessibilityViewCommand command) {
        if (command == null && label == null) {
            removeAccessibilityAction(view, replacedAction.getId());
        } else {
            addAccessibilityAction(view, replacedAction.createReplacementAction(label, command));
        }
    }

    public static void requestApplyInsets(View view) {
        if (Build.VERSION.SDK_INT >= 20) {
            Api20Impl.requestApplyInsets(view);
        } else if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.requestFitSystemWindows(view);
        }
    }

    public static <T extends View> T requireViewById(View view, int id) {
        if (Build.VERSION.SDK_INT >= 28) {
            return (View) Api28Impl.requireViewById(view, id);
        }
        T findViewById = view.findViewById(id);
        if (findViewById != null) {
            return findViewById;
        }
        throw new IllegalArgumentException("ID does not reference a View inside this View");
    }

    @Deprecated
    public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
        return View.resolveSizeAndState(size, measureSpec, childMeasuredState);
    }

    public static boolean restoreDefaultFocus(View view) {
        return Build.VERSION.SDK_INT >= 26 ? Api26Impl.restoreDefaultFocus(view) : view.requestFocus();
    }

    public static void saveAttributeDataForStyleable(View view, Context context, int[] styleable, AttributeSet attrs, TypedArray t, int defStyleAttr, int defStyleRes) {
        if (Build.VERSION.SDK_INT >= 29) {
            Api29Impl.saveAttributeDataForStyleable(view, context, styleable, attrs, t, defStyleAttr, defStyleRes);
        }
    }

    private static AccessibilityViewProperty<Boolean> screenReaderFocusableProperty() {
        return new AccessibilityViewProperty<Boolean>(R.id.tag_screen_reader_focusable, Boolean.class, 28) {
            /* access modifiers changed from: package-private */
            public Boolean frameworkGet(View view) {
                return Boolean.valueOf(Api28Impl.isScreenReaderFocusable(view));
            }

            /* access modifiers changed from: package-private */
            public void frameworkSet(View view, Boolean value) {
                Api28Impl.setScreenReaderFocusable(view, value.booleanValue());
            }

            /* access modifiers changed from: package-private */
            public boolean shouldUpdate(Boolean oldValue, Boolean newValue) {
                return !booleanNullToFalseEquals(oldValue, newValue);
            }
        };
    }

    public static void setAccessibilityDelegate(View v, AccessibilityDelegateCompat delegate) {
        if (delegate == null && (getAccessibilityDelegateInternal(v) instanceof AccessibilityDelegateCompat.AccessibilityDelegateAdapter)) {
            delegate = new AccessibilityDelegateCompat();
        }
        v.setAccessibilityDelegate(delegate == null ? null : delegate.getBridge());
    }

    public static void setAccessibilityHeading(View view, boolean isHeading) {
        accessibilityHeadingProperty().set(view, Boolean.valueOf(isHeading));
    }

    public static void setAccessibilityLiveRegion(View view, int mode) {
        if (Build.VERSION.SDK_INT >= 19) {
            Api19Impl.setAccessibilityLiveRegion(view, mode);
        }
    }

    public static void setAccessibilityPaneTitle(View view, CharSequence accessibilityPaneTitle) {
        if (Build.VERSION.SDK_INT >= 19) {
            paneTitleProperty().set(view, accessibilityPaneTitle);
            if (accessibilityPaneTitle != null) {
                sAccessibilityPaneVisibilityManager.addAccessibilityPane(view);
            } else {
                sAccessibilityPaneVisibilityManager.removeAccessibilityPane(view);
            }
        }
    }

    @Deprecated
    public static void setActivated(View view, boolean activated) {
        view.setActivated(activated);
    }

    @Deprecated
    public static void setAlpha(View view, float value) {
        view.setAlpha(value);
    }

    public static void setAutofillHints(View v, String... autofillHints) {
        if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.setAutofillHints(v, autofillHints);
        }
    }

    public static void setBackground(View view, Drawable background) {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.setBackground(view, background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    public static void setBackgroundTintList(View view, ColorStateList tintList) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setBackgroundTintList(view, tintList);
            if (Build.VERSION.SDK_INT == 21) {
                Drawable background = view.getBackground();
                boolean z = (Api21Impl.getBackgroundTintList(view) == null && Api21Impl.getBackgroundTintMode(view) == null) ? false : true;
                if (background != null && z) {
                    if (background.isStateful()) {
                        background.setState(view.getDrawableState());
                    }
                    Api16Impl.setBackground(view, background);
                }
            }
        } else if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView) view).setSupportBackgroundTintList(tintList);
        }
    }

    public static void setBackgroundTintMode(View view, PorterDuff.Mode mode) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setBackgroundTintMode(view, mode);
            if (Build.VERSION.SDK_INT == 21) {
                Drawable background = view.getBackground();
                boolean z = (Api21Impl.getBackgroundTintList(view) == null && Api21Impl.getBackgroundTintMode(view) == null) ? false : true;
                if (background != null && z) {
                    if (background.isStateful()) {
                        background.setState(view.getDrawableState());
                    }
                    Api16Impl.setBackground(view, background);
                }
            }
        } else if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView) view).setSupportBackgroundTintMode(mode);
        }
    }

    @Deprecated
    public static void setChildrenDrawingOrderEnabled(ViewGroup viewGroup, boolean enabled) {
        if (sChildrenDrawingOrderMethod == null) {
            Class<ViewGroup> cls = ViewGroup.class;
            try {
                sChildrenDrawingOrderMethod = cls.getDeclaredMethod("setChildrenDrawingOrderEnabled", new Class[]{Boolean.TYPE});
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "Unable to find childrenDrawingOrderEnabled", e);
            }
            sChildrenDrawingOrderMethod.setAccessible(true);
        }
        try {
            sChildrenDrawingOrderMethod.invoke(viewGroup, new Object[]{Boolean.valueOf(enabled)});
        } catch (IllegalAccessException e2) {
            Log.e(TAG, "Unable to invoke childrenDrawingOrderEnabled", e2);
        } catch (IllegalArgumentException e3) {
            Log.e(TAG, "Unable to invoke childrenDrawingOrderEnabled", e3);
        } catch (InvocationTargetException e4) {
            Log.e(TAG, "Unable to invoke childrenDrawingOrderEnabled", e4);
        }
    }

    public static void setClipBounds(View view, Rect clipBounds) {
        if (Build.VERSION.SDK_INT >= 18) {
            Api18Impl.setClipBounds(view, clipBounds);
        }
    }

    public static void setElevation(View view, float elevation) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setElevation(view, elevation);
        }
    }

    @Deprecated
    public static void setFitsSystemWindows(View view, boolean fitSystemWindows) {
        view.setFitsSystemWindows(fitSystemWindows);
    }

    public static void setFocusedByDefault(View view, boolean isFocusedByDefault) {
        if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.setFocusedByDefault(view, isFocusedByDefault);
        }
    }

    public static void setHasTransientState(View view, boolean hasTransientState) {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.setHasTransientState(view, hasTransientState);
        }
    }

    public static void setImportantForAccessibility(View view, int mode) {
        if (Build.VERSION.SDK_INT >= 19) {
            Api16Impl.setImportantForAccessibility(view, mode);
        } else if (Build.VERSION.SDK_INT >= 16) {
            if (mode == 4) {
                mode = 2;
            }
            Api16Impl.setImportantForAccessibility(view, mode);
        }
    }

    public static void setImportantForAutofill(View v, int mode) {
        if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.setImportantForAutofill(v, mode);
        }
    }

    public static void setKeyboardNavigationCluster(View view, boolean isCluster) {
        if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.setKeyboardNavigationCluster(view, isCluster);
        }
    }

    public static void setLabelFor(View view, int labeledId) {
        if (Build.VERSION.SDK_INT >= 17) {
            Api17Impl.setLabelFor(view, labeledId);
        }
    }

    public static void setLayerPaint(View view, Paint paint) {
        if (Build.VERSION.SDK_INT >= 17) {
            Api17Impl.setLayerPaint(view, paint);
            return;
        }
        view.setLayerType(view.getLayerType(), paint);
        view.invalidate();
    }

    @Deprecated
    public static void setLayerType(View view, int layerType, Paint paint) {
        view.setLayerType(layerType, paint);
    }

    public static void setLayoutDirection(View view, int layoutDirection) {
        if (Build.VERSION.SDK_INT >= 17) {
            Api17Impl.setLayoutDirection(view, layoutDirection);
        }
    }

    public static void setNestedScrollingEnabled(View view, boolean enabled) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setNestedScrollingEnabled(view, enabled);
        } else if (view instanceof NestedScrollingChild) {
            ((NestedScrollingChild) view).setNestedScrollingEnabled(enabled);
        }
    }

    public static void setNextClusterForwardId(View view, int nextClusterForwardId) {
        if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.setNextClusterForwardId(view, nextClusterForwardId);
        }
    }

    public static void setOnApplyWindowInsetsListener(View v, OnApplyWindowInsetsListener listener) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setOnApplyWindowInsetsListener(v, listener);
        }
    }

    @Deprecated
    public static void setOverScrollMode(View v, int overScrollMode) {
        v.setOverScrollMode(overScrollMode);
    }

    public static void setPaddingRelative(View view, int start, int top, int end, int bottom) {
        if (Build.VERSION.SDK_INT >= 17) {
            Api17Impl.setPaddingRelative(view, start, top, end, bottom);
        } else {
            view.setPadding(start, top, end, bottom);
        }
    }

    @Deprecated
    public static void setPivotX(View view, float value) {
        view.setPivotX(value);
    }

    @Deprecated
    public static void setPivotY(View view, float value) {
        view.setPivotY(value);
    }

    public static void setPointerIcon(View view, PointerIconCompat pointerIcon) {
        if (Build.VERSION.SDK_INT >= 24) {
            Api24Impl.setPointerIcon(view, (PointerIcon) (pointerIcon != null ? pointerIcon.getPointerIcon() : null));
        }
    }

    @Deprecated
    public static void setRotation(View view, float value) {
        view.setRotation(value);
    }

    @Deprecated
    public static void setRotationX(View view, float value) {
        view.setRotationX(value);
    }

    @Deprecated
    public static void setRotationY(View view, float value) {
        view.setRotationY(value);
    }

    @Deprecated
    public static void setSaveFromParentEnabled(View v, boolean enabled) {
        v.setSaveFromParentEnabled(enabled);
    }

    @Deprecated
    public static void setScaleX(View view, float value) {
        view.setScaleX(value);
    }

    @Deprecated
    public static void setScaleY(View view, float value) {
        view.setScaleY(value);
    }

    public static void setScreenReaderFocusable(View view, boolean screenReaderFocusable) {
        screenReaderFocusableProperty().set(view, Boolean.valueOf(screenReaderFocusable));
    }

    public static void setScrollIndicators(View view, int indicators) {
        if (Build.VERSION.SDK_INT >= 23) {
            Api23Impl.setScrollIndicators(view, indicators);
        }
    }

    public static void setScrollIndicators(View view, int indicators, int mask) {
        if (Build.VERSION.SDK_INT >= 23) {
            Api23Impl.setScrollIndicators(view, indicators, mask);
        }
    }

    public static void setStateDescription(View view, CharSequence stateDescription) {
        if (Build.VERSION.SDK_INT >= 19) {
            stateDescriptionProperty().set(view, stateDescription);
        }
    }

    public static void setSystemGestureExclusionRects(View view, List<Rect> list) {
        if (Build.VERSION.SDK_INT >= 29) {
            Api29Impl.setSystemGestureExclusionRects(view, list);
        }
    }

    public static void setTooltipText(View view, CharSequence tooltipText) {
        if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.setTooltipText(view, tooltipText);
        }
    }

    public static void setTransitionName(View view, String transitionName) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setTransitionName(view, transitionName);
            return;
        }
        if (sTransitionNameMap == null) {
            sTransitionNameMap = new WeakHashMap<>();
        }
        sTransitionNameMap.put(view, transitionName);
    }

    @Deprecated
    public static void setTranslationX(View view, float value) {
        view.setTranslationX(value);
    }

    @Deprecated
    public static void setTranslationY(View view, float value) {
        view.setTranslationY(value);
    }

    public static void setTranslationZ(View view, float translationZ) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setTranslationZ(view, translationZ);
        }
    }

    private static void setViewImportanceForAccessibilityIfNeeded(View view) {
        if (getImportantForAccessibility(view) == 0) {
            setImportantForAccessibility(view, 1);
        }
        for (ViewParent parent = view.getParent(); parent instanceof View; parent = parent.getParent()) {
            if (getImportantForAccessibility((View) parent) == 4) {
                setImportantForAccessibility(view, 2);
                return;
            }
        }
    }

    public static void setWindowInsetsAnimationCallback(View view, WindowInsetsAnimationCompat.Callback callback) {
        WindowInsetsAnimationCompat.setCallback(view, callback);
    }

    @Deprecated
    public static void setX(View view, float value) {
        view.setX(value);
    }

    @Deprecated
    public static void setY(View view, float value) {
        view.setY(value);
    }

    public static void setZ(View view, float z) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setZ(view, z);
        }
    }

    public static boolean startDragAndDrop(View v, ClipData data, View.DragShadowBuilder shadowBuilder, Object myLocalState, int flags) {
        return Build.VERSION.SDK_INT >= 24 ? Api24Impl.startDragAndDrop(v, data, shadowBuilder, myLocalState, flags) : v.startDrag(data, shadowBuilder, myLocalState, flags);
    }

    public static boolean startNestedScroll(View view, int axes) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.startNestedScroll(view, axes);
        }
        if (view instanceof NestedScrollingChild) {
            return ((NestedScrollingChild) view).startNestedScroll(axes);
        }
        return false;
    }

    public static boolean startNestedScroll(View view, int axes, int type) {
        if (view instanceof NestedScrollingChild2) {
            return ((NestedScrollingChild2) view).startNestedScroll(axes, type);
        }
        if (type == 0) {
            return startNestedScroll(view, axes);
        }
        return false;
    }

    private static AccessibilityViewProperty<CharSequence> stateDescriptionProperty() {
        return new AccessibilityViewProperty<CharSequence>(R.id.tag_state_description, CharSequence.class, 64, 30) {
            /* access modifiers changed from: package-private */
            public CharSequence frameworkGet(View view) {
                return Api30Impl.getStateDescription(view);
            }

            /* access modifiers changed from: package-private */
            public void frameworkSet(View view, CharSequence value) {
                Api30Impl.setStateDescription(view, value);
            }

            /* access modifiers changed from: package-private */
            public boolean shouldUpdate(CharSequence oldValue, CharSequence newValue) {
                return !TextUtils.equals(oldValue, newValue);
            }
        };
    }

    public static void stopNestedScroll(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.stopNestedScroll(view);
        } else if (view instanceof NestedScrollingChild) {
            ((NestedScrollingChild) view).stopNestedScroll();
        }
    }

    public static void stopNestedScroll(View view, int type) {
        if (view instanceof NestedScrollingChild2) {
            ((NestedScrollingChild2) view).stopNestedScroll(type);
        } else if (type == 0) {
            stopNestedScroll(view);
        }
    }

    private static void tickleInvalidationFlag(View view) {
        float translationY = view.getTranslationY();
        view.setTranslationY(1.0f + translationY);
        view.setTranslationY(translationY);
    }

    public static void updateDragShadow(View v, View.DragShadowBuilder shadowBuilder) {
        if (Build.VERSION.SDK_INT >= 24) {
            Api24Impl.updateDragShadow(v, shadowBuilder);
        }
    }

    public static void setOnReceiveContentListener(View view, String[] mimeTypes, OnReceiveContentListener listener) {
        if (Build.VERSION.SDK_INT >= 31) {
            Api31Impl.setOnReceiveContentListener(view, mimeTypes, listener);
            return;
        }
        String[] mimeTypes2 = (mimeTypes == null || mimeTypes.length == 0) ? null : mimeTypes;
        int i = 0;
        if (listener != null) {
            Preconditions.checkArgument(mimeTypes2 != null, "When the listener is set, MIME types must also be set");
        }
        if (mimeTypes2 != null) {
            boolean z = false;
            int length = mimeTypes2.length;
            while (true) {
                if (i >= length) {
                    break;
                } else if (mimeTypes2[i].startsWith("*")) {
                    z = true;
                    break;
                } else {
                    i++;
                }
            }
            StringBuilder append = new StringBuilder().append("A MIME type set here must not start with *: ");
            String arrays = Arrays.toString(mimeTypes2);
            Log1F380D.a((Object) arrays);
            Preconditions.checkArgument(!z, append.append(arrays).toString());
        }
        view.setTag(R.id.tag_on_receive_content_mime_types, mimeTypes2);
        view.setTag(R.id.tag_on_receive_content_listener, listener);
    }
}

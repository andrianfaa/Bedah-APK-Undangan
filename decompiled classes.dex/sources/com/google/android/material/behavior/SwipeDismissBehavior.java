package com.google.android.material.behavior;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.customview.widget.ViewDragHelper;

public class SwipeDismissBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    private static final float DEFAULT_ALPHA_END_DISTANCE = 0.5f;
    private static final float DEFAULT_ALPHA_START_DISTANCE = 0.0f;
    private static final float DEFAULT_DRAG_DISMISS_THRESHOLD = 0.5f;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    public static final int SWIPE_DIRECTION_ANY = 2;
    public static final int SWIPE_DIRECTION_END_TO_START = 1;
    public static final int SWIPE_DIRECTION_START_TO_END = 0;
    float alphaEndSwipeDistance = 0.5f;
    float alphaStartSwipeDistance = 0.0f;
    private final ViewDragHelper.Callback dragCallback = new ViewDragHelper.Callback() {
        private static final int INVALID_POINTER_ID = -1;
        private int activePointerId = -1;
        private int originalCapturedViewLeft;

        private boolean shouldDismiss(View child, float xvel) {
            if (xvel != 0.0f) {
                boolean z = ViewCompat.getLayoutDirection(child) == 1;
                if (SwipeDismissBehavior.this.swipeDirection == 2) {
                    return true;
                }
                if (SwipeDismissBehavior.this.swipeDirection == 0) {
                    if (z) {
                        if (xvel >= 0.0f) {
                            return false;
                        }
                    } else if (xvel <= 0.0f) {
                        return false;
                    }
                    return true;
                } else if (SwipeDismissBehavior.this.swipeDirection != 1) {
                    return false;
                } else {
                    if (z) {
                        if (xvel <= 0.0f) {
                            return false;
                        }
                    } else if (xvel >= 0.0f) {
                        return false;
                    }
                    return true;
                }
            } else {
                return Math.abs(child.getLeft() - this.originalCapturedViewLeft) >= Math.round(((float) child.getWidth()) * SwipeDismissBehavior.this.dragDismissThreshold);
            }
        }

        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int i;
            int i2;
            boolean z = ViewCompat.getLayoutDirection(child) == 1;
            if (SwipeDismissBehavior.this.swipeDirection == 0) {
                if (z) {
                    i2 = this.originalCapturedViewLeft - child.getWidth();
                    i = this.originalCapturedViewLeft;
                } else {
                    i2 = this.originalCapturedViewLeft;
                    i = this.originalCapturedViewLeft + child.getWidth();
                }
            } else if (SwipeDismissBehavior.this.swipeDirection != 1) {
                i2 = this.originalCapturedViewLeft - child.getWidth();
                i = this.originalCapturedViewLeft + child.getWidth();
            } else if (z) {
                i2 = this.originalCapturedViewLeft;
                i = this.originalCapturedViewLeft + child.getWidth();
            } else {
                i2 = this.originalCapturedViewLeft - child.getWidth();
                i = this.originalCapturedViewLeft;
            }
            return SwipeDismissBehavior.clamp(i2, left, i);
        }

        public int clampViewPositionVertical(View child, int top, int dy) {
            return child.getTop();
        }

        public int getViewHorizontalDragRange(View child) {
            return child.getWidth();
        }

        public void onViewCaptured(View capturedChild, int activePointerId2) {
            this.activePointerId = activePointerId2;
            this.originalCapturedViewLeft = capturedChild.getLeft();
            ViewParent parent = capturedChild.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }

        public void onViewDragStateChanged(int state) {
            if (SwipeDismissBehavior.this.listener != null) {
                SwipeDismissBehavior.this.listener.onDragStateChanged(state);
            }
        }

        public void onViewPositionChanged(View child, int left, int top, int dx, int dy) {
            float width = ((float) this.originalCapturedViewLeft) + (((float) child.getWidth()) * SwipeDismissBehavior.this.alphaStartSwipeDistance);
            float width2 = ((float) this.originalCapturedViewLeft) + (((float) child.getWidth()) * SwipeDismissBehavior.this.alphaEndSwipeDistance);
            if (((float) left) <= width) {
                child.setAlpha(1.0f);
            } else if (((float) left) >= width2) {
                child.setAlpha(0.0f);
            } else {
                child.setAlpha(SwipeDismissBehavior.clamp(0.0f, 1.0f - SwipeDismissBehavior.fraction(width, width2, (float) left), 1.0f));
            }
        }

        public void onViewReleased(View child, float xvel, float yvel) {
            int i;
            this.activePointerId = -1;
            int width = child.getWidth();
            boolean z = false;
            if (shouldDismiss(child, xvel)) {
                int left = child.getLeft();
                int i2 = this.originalCapturedViewLeft;
                i = left < i2 ? i2 - width : i2 + width;
                z = true;
            } else {
                i = this.originalCapturedViewLeft;
            }
            if (SwipeDismissBehavior.this.viewDragHelper.settleCapturedViewAt(i, child.getTop())) {
                ViewCompat.postOnAnimation(child, new SettleRunnable(child, z));
            } else if (z && SwipeDismissBehavior.this.listener != null) {
                SwipeDismissBehavior.this.listener.onDismiss(child);
            }
        }

        public boolean tryCaptureView(View child, int pointerId) {
            int i = this.activePointerId;
            return (i == -1 || i == pointerId) && SwipeDismissBehavior.this.canSwipeDismissView(child);
        }
    };
    float dragDismissThreshold = 0.5f;
    private boolean interceptingEvents;
    OnDismissListener listener;
    private float sensitivity = 0.0f;
    private boolean sensitivitySet;
    int swipeDirection = 2;
    ViewDragHelper viewDragHelper;

    public interface OnDismissListener {
        void onDismiss(View view);

        void onDragStateChanged(int i);
    }

    private class SettleRunnable implements Runnable {
        private final boolean dismiss;
        private final View view;

        SettleRunnable(View view2, boolean dismiss2) {
            this.view = view2;
            this.dismiss = dismiss2;
        }

        public void run() {
            if (SwipeDismissBehavior.this.viewDragHelper != null && SwipeDismissBehavior.this.viewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(this.view, this);
            } else if (this.dismiss && SwipeDismissBehavior.this.listener != null) {
                SwipeDismissBehavior.this.listener.onDismiss(this.view);
            }
        }
    }

    static float clamp(float min, float value, float max) {
        return Math.min(Math.max(min, value), max);
    }

    static int clamp(int min, int value, int max) {
        return Math.min(Math.max(min, value), max);
    }

    private void ensureViewDragHelper(ViewGroup parent) {
        if (this.viewDragHelper == null) {
            this.viewDragHelper = this.sensitivitySet ? ViewDragHelper.create(parent, this.sensitivity, this.dragCallback) : ViewDragHelper.create(parent, this.dragCallback);
        }
    }

    static float fraction(float startValue, float endValue, float value) {
        return (value - startValue) / (endValue - startValue);
    }

    private void updateAccessibilityActions(View child) {
        ViewCompat.removeAccessibilityAction(child, 1048576);
        if (canSwipeDismissView(child)) {
            ViewCompat.replaceAccessibilityAction(child, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_DISMISS, (CharSequence) null, new AccessibilityViewCommand() {
                public boolean perform(View view, AccessibilityViewCommand.CommandArguments arguments) {
                    boolean z = false;
                    if (!SwipeDismissBehavior.this.canSwipeDismissView(view)) {
                        return false;
                    }
                    boolean z2 = ViewCompat.getLayoutDirection(view) == 1;
                    if ((SwipeDismissBehavior.this.swipeDirection == 0 && z2) || (SwipeDismissBehavior.this.swipeDirection == 1 && !z2)) {
                        z = true;
                    }
                    int width = view.getWidth();
                    if (z) {
                        width = -width;
                    }
                    ViewCompat.offsetLeftAndRight(view, width);
                    view.setAlpha(0.0f);
                    if (SwipeDismissBehavior.this.listener != null) {
                        SwipeDismissBehavior.this.listener.onDismiss(view);
                    }
                    return true;
                }
            });
        }
    }

    public boolean canSwipeDismissView(View view) {
        return true;
    }

    public int getDragState() {
        ViewDragHelper viewDragHelper2 = this.viewDragHelper;
        if (viewDragHelper2 != null) {
            return viewDragHelper2.getViewDragState();
        }
        return 0;
    }

    public OnDismissListener getListener() {
        return this.listener;
    }

    public boolean onInterceptTouchEvent(CoordinatorLayout parent, V v, MotionEvent event) {
        boolean z = this.interceptingEvents;
        switch (event.getActionMasked()) {
            case 0:
                this.interceptingEvents = parent.isPointInChildBounds(v, (int) event.getX(), (int) event.getY());
                z = this.interceptingEvents;
                break;
            case 1:
            case 3:
                this.interceptingEvents = false;
                break;
        }
        if (!z) {
            return false;
        }
        ensureViewDragHelper(parent);
        return this.viewDragHelper.shouldInterceptTouchEvent(event);
    }

    public boolean onLayoutChild(CoordinatorLayout parent, V v, int layoutDirection) {
        boolean onLayoutChild = super.onLayoutChild(parent, v, layoutDirection);
        if (ViewCompat.getImportantForAccessibility(v) == 0) {
            ViewCompat.setImportantForAccessibility(v, 1);
            updateAccessibilityActions(v);
        }
        return onLayoutChild;
    }

    public boolean onTouchEvent(CoordinatorLayout parent, V v, MotionEvent event) {
        ViewDragHelper viewDragHelper2 = this.viewDragHelper;
        if (viewDragHelper2 == null) {
            return false;
        }
        viewDragHelper2.processTouchEvent(event);
        return true;
    }

    public void setDragDismissDistance(float distance) {
        this.dragDismissThreshold = clamp(0.0f, distance, 1.0f);
    }

    public void setEndAlphaSwipeDistance(float fraction) {
        this.alphaEndSwipeDistance = clamp(0.0f, fraction, 1.0f);
    }

    public void setListener(OnDismissListener listener2) {
        this.listener = listener2;
    }

    public void setSensitivity(float sensitivity2) {
        this.sensitivity = sensitivity2;
        this.sensitivitySet = true;
    }

    public void setStartAlphaSwipeDistance(float fraction) {
        this.alphaStartSwipeDistance = clamp(0.0f, fraction, 1.0f);
    }

    public void setSwipeDirection(int direction) {
        this.swipeDirection = direction;
    }
}

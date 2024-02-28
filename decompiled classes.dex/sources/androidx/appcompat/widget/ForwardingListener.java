package androidx.appcompat.widget;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import androidx.appcompat.view.menu.ShowableListMenu;

public abstract class ForwardingListener implements View.OnTouchListener, View.OnAttachStateChangeListener {
    private int mActivePointerId;
    private Runnable mDisallowIntercept;
    private boolean mForwarding;
    private final int mLongPressTimeout;
    private final float mScaledTouchSlop;
    final View mSrc;
    private final int mTapTimeout;
    private final int[] mTmpLocation = new int[2];
    private Runnable mTriggerLongPress;

    private class DisallowIntercept implements Runnable {
        DisallowIntercept() {
        }

        public void run() {
            ViewParent parent = ForwardingListener.this.mSrc.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
    }

    private class TriggerLongPress implements Runnable {
        TriggerLongPress() {
        }

        public void run() {
            ForwardingListener.this.onLongPress();
        }
    }

    public ForwardingListener(View src) {
        this.mSrc = src;
        src.setLongClickable(true);
        src.addOnAttachStateChangeListener(this);
        this.mScaledTouchSlop = (float) ViewConfiguration.get(src.getContext()).getScaledTouchSlop();
        int tapTimeout = ViewConfiguration.getTapTimeout();
        this.mTapTimeout = tapTimeout;
        this.mLongPressTimeout = (tapTimeout + ViewConfiguration.getLongPressTimeout()) / 2;
    }

    private void clearCallbacks() {
        Runnable runnable = this.mTriggerLongPress;
        if (runnable != null) {
            this.mSrc.removeCallbacks(runnable);
        }
        Runnable runnable2 = this.mDisallowIntercept;
        if (runnable2 != null) {
            this.mSrc.removeCallbacks(runnable2);
        }
    }

    private boolean onTouchForwarded(MotionEvent srcEvent) {
        DropDownListView dropDownListView;
        View view = this.mSrc;
        ShowableListMenu popup = getPopup();
        if (popup == null || !popup.isShowing() || (dropDownListView = (DropDownListView) popup.getListView()) == null || !dropDownListView.isShown()) {
            return false;
        }
        MotionEvent obtainNoHistory = MotionEvent.obtainNoHistory(srcEvent);
        toGlobalMotionEvent(view, obtainNoHistory);
        toLocalMotionEvent(dropDownListView, obtainNoHistory);
        boolean onForwardedEvent = dropDownListView.onForwardedEvent(obtainNoHistory, this.mActivePointerId);
        obtainNoHistory.recycle();
        int actionMasked = srcEvent.getActionMasked();
        return onForwardedEvent && (actionMasked != 1 && actionMasked != 3);
    }

    private boolean onTouchObserved(MotionEvent srcEvent) {
        View view = this.mSrc;
        if (!view.isEnabled()) {
            return false;
        }
        switch (srcEvent.getActionMasked()) {
            case 0:
                this.mActivePointerId = srcEvent.getPointerId(0);
                if (this.mDisallowIntercept == null) {
                    this.mDisallowIntercept = new DisallowIntercept();
                }
                view.postDelayed(this.mDisallowIntercept, (long) this.mTapTimeout);
                if (this.mTriggerLongPress == null) {
                    this.mTriggerLongPress = new TriggerLongPress();
                }
                view.postDelayed(this.mTriggerLongPress, (long) this.mLongPressTimeout);
                break;
            case 1:
            case 3:
                clearCallbacks();
                break;
            case 2:
                int findPointerIndex = srcEvent.findPointerIndex(this.mActivePointerId);
                if (findPointerIndex >= 0 && !pointInView(view, srcEvent.getX(findPointerIndex), srcEvent.getY(findPointerIndex), this.mScaledTouchSlop)) {
                    clearCallbacks();
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                }
        }
        return false;
    }

    private static boolean pointInView(View view, float localX, float localY, float slop) {
        return localX >= (-slop) && localY >= (-slop) && localX < ((float) (view.getRight() - view.getLeft())) + slop && localY < ((float) (view.getBottom() - view.getTop())) + slop;
    }

    private boolean toGlobalMotionEvent(View view, MotionEvent event) {
        int[] iArr = this.mTmpLocation;
        view.getLocationOnScreen(iArr);
        event.offsetLocation((float) iArr[0], (float) iArr[1]);
        return true;
    }

    private boolean toLocalMotionEvent(View view, MotionEvent event) {
        int[] iArr = this.mTmpLocation;
        view.getLocationOnScreen(iArr);
        event.offsetLocation((float) (-iArr[0]), (float) (-iArr[1]));
        return true;
    }

    public abstract ShowableListMenu getPopup();

    /* access modifiers changed from: protected */
    public boolean onForwardingStarted() {
        ShowableListMenu popup = getPopup();
        if (popup == null || popup.isShowing()) {
            return true;
        }
        popup.show();
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean onForwardingStopped() {
        ShowableListMenu popup = getPopup();
        if (popup == null || !popup.isShowing()) {
            return true;
        }
        popup.dismiss();
        return true;
    }

    /* access modifiers changed from: package-private */
    public void onLongPress() {
        clearCallbacks();
        View view = this.mSrc;
        if (view.isEnabled() && !view.isLongClickable() && onForwardingStarted()) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            long uptimeMillis = SystemClock.uptimeMillis();
            MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
            view.onTouchEvent(obtain);
            obtain.recycle();
            this.mForwarding = true;
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        boolean z;
        MotionEvent motionEvent = event;
        boolean z2 = this.mForwarding;
        if (z2) {
            z = onTouchForwarded(motionEvent) || !onForwardingStopped();
        } else {
            z = onTouchObserved(motionEvent) && onForwardingStarted();
            if (z) {
                long uptimeMillis = SystemClock.uptimeMillis();
                MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                this.mSrc.onTouchEvent(obtain);
                obtain.recycle();
            }
        }
        this.mForwarding = z;
        return z || z2;
    }

    public void onViewAttachedToWindow(View v) {
    }

    public void onViewDetachedFromWindow(View v) {
        this.mForwarding = false;
        this.mActivePointerId = -1;
        Runnable runnable = this.mDisallowIntercept;
        if (runnable != null) {
            this.mSrc.removeCallbacks(runnable);
        }
    }
}

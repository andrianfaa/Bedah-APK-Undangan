package androidx.appcompat.widget;

import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityManager;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewConfigurationCompat;

class TooltipCompatHandler implements View.OnLongClickListener, View.OnHoverListener, View.OnAttachStateChangeListener {
    private static final long HOVER_HIDE_TIMEOUT_MS = 15000;
    private static final long HOVER_HIDE_TIMEOUT_SHORT_MS = 3000;
    private static final long LONG_CLICK_HIDE_TIMEOUT_MS = 2500;
    private static final String TAG = "TooltipCompatHandler";
    private static TooltipCompatHandler sActiveHandler;
    private static TooltipCompatHandler sPendingHandler;
    private final View mAnchor;
    private int mAnchorX;
    private int mAnchorY;
    private boolean mForceNextChangeSignificant;
    private boolean mFromTouch;
    private final Runnable mHideRunnable = new TooltipCompatHandler$$ExternalSyntheticLambda1(this);
    private final int mHoverSlop;
    private TooltipPopup mPopup;
    private final Runnable mShowRunnable = new TooltipCompatHandler$$ExternalSyntheticLambda0(this);
    private final CharSequence mTooltipText;

    private TooltipCompatHandler(View anchor, CharSequence tooltipText) {
        this.mAnchor = anchor;
        this.mTooltipText = tooltipText;
        this.mHoverSlop = ViewConfigurationCompat.getScaledHoverSlop(ViewConfiguration.get(anchor.getContext()));
        forceNextChangeSignificant();
        anchor.setOnLongClickListener(this);
        anchor.setOnHoverListener(this);
    }

    private void cancelPendingShow() {
        this.mAnchor.removeCallbacks(this.mShowRunnable);
    }

    private void forceNextChangeSignificant() {
        this.mForceNextChangeSignificant = true;
    }

    private void scheduleShow() {
        this.mAnchor.postDelayed(this.mShowRunnable, (long) ViewConfiguration.getLongPressTimeout());
    }

    private static void setPendingHandler(TooltipCompatHandler handler) {
        TooltipCompatHandler tooltipCompatHandler = sPendingHandler;
        if (tooltipCompatHandler != null) {
            tooltipCompatHandler.cancelPendingShow();
        }
        sPendingHandler = handler;
        if (handler != null) {
            handler.scheduleShow();
        }
    }

    public static void setTooltipText(View view, CharSequence tooltipText) {
        TooltipCompatHandler tooltipCompatHandler = sPendingHandler;
        if (tooltipCompatHandler != null && tooltipCompatHandler.mAnchor == view) {
            setPendingHandler((TooltipCompatHandler) null);
        }
        if (TextUtils.isEmpty(tooltipText)) {
            TooltipCompatHandler tooltipCompatHandler2 = sActiveHandler;
            if (tooltipCompatHandler2 != null && tooltipCompatHandler2.mAnchor == view) {
                tooltipCompatHandler2.hide();
            }
            view.setOnLongClickListener((View.OnLongClickListener) null);
            view.setLongClickable(false);
            view.setOnHoverListener((View.OnHoverListener) null);
            return;
        }
        new TooltipCompatHandler(view, tooltipText);
    }

    private boolean updateAnchorPos(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (!this.mForceNextChangeSignificant && Math.abs(x - this.mAnchorX) <= this.mHoverSlop && Math.abs(y - this.mAnchorY) <= this.mHoverSlop) {
            return false;
        }
        this.mAnchorX = x;
        this.mAnchorY = y;
        this.mForceNextChangeSignificant = false;
        return true;
    }

    /* access modifiers changed from: package-private */
    public void hide() {
        if (sActiveHandler == this) {
            sActiveHandler = null;
            TooltipPopup tooltipPopup = this.mPopup;
            if (tooltipPopup != null) {
                tooltipPopup.hide();
                this.mPopup = null;
                forceNextChangeSignificant();
                this.mAnchor.removeOnAttachStateChangeListener(this);
            } else {
                Log.e(TAG, "sActiveHandler.mPopup == null");
            }
        }
        if (sPendingHandler == this) {
            setPendingHandler((TooltipCompatHandler) null);
        }
        this.mAnchor.removeCallbacks(this.mHideRunnable);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$androidx-appcompat-widget-TooltipCompatHandler  reason: not valid java name */
    public /* synthetic */ void m3lambda$new$0$androidxappcompatwidgetTooltipCompatHandler() {
        show(false);
    }

    public boolean onHover(View v, MotionEvent event) {
        if (this.mPopup != null && this.mFromTouch) {
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.mAnchor.getContext().getSystemService("accessibility");
        if (accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case 7:
                if (this.mAnchor.isEnabled() && this.mPopup == null && updateAnchorPos(event)) {
                    setPendingHandler(this);
                    break;
                }
            case 10:
                forceNextChangeSignificant();
                hide();
                break;
        }
        return false;
    }

    public boolean onLongClick(View v) {
        this.mAnchorX = v.getWidth() / 2;
        this.mAnchorY = v.getHeight() / 2;
        show(true);
        return true;
    }

    public void onViewAttachedToWindow(View v) {
    }

    public void onViewDetachedFromWindow(View v) {
        hide();
    }

    /* access modifiers changed from: package-private */
    public void show(boolean fromTouch) {
        if (ViewCompat.isAttachedToWindow(this.mAnchor)) {
            setPendingHandler((TooltipCompatHandler) null);
            TooltipCompatHandler tooltipCompatHandler = sActiveHandler;
            if (tooltipCompatHandler != null) {
                tooltipCompatHandler.hide();
            }
            sActiveHandler = this;
            this.mFromTouch = fromTouch;
            TooltipPopup tooltipPopup = new TooltipPopup(this.mAnchor.getContext());
            this.mPopup = tooltipPopup;
            tooltipPopup.show(this.mAnchor, this.mAnchorX, this.mAnchorY, this.mFromTouch, this.mTooltipText);
            this.mAnchor.addOnAttachStateChangeListener(this);
            long longPressTimeout = this.mFromTouch ? LONG_CLICK_HIDE_TIMEOUT_MS : (ViewCompat.getWindowSystemUiVisibility(this.mAnchor) & 1) == 1 ? HOVER_HIDE_TIMEOUT_SHORT_MS - ((long) ViewConfiguration.getLongPressTimeout()) : HOVER_HIDE_TIMEOUT_MS - ((long) ViewConfiguration.getLongPressTimeout());
            this.mAnchor.removeCallbacks(this.mHideRunnable);
            this.mAnchor.postDelayed(this.mHideRunnable, longPressTimeout);
        }
    }
}

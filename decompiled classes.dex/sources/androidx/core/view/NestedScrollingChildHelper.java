package androidx.core.view;

import android.view.View;
import android.view.ViewParent;

public class NestedScrollingChildHelper {
    private boolean mIsNestedScrollingEnabled;
    private ViewParent mNestedScrollingParentNonTouch;
    private ViewParent mNestedScrollingParentTouch;
    private int[] mTempNestedScrollConsumed;
    private final View mView;

    public NestedScrollingChildHelper(View view) {
        this.mView = view;
    }

    private boolean dispatchNestedScrollInternal(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type, int[] consumed) {
        int i;
        int i2;
        int[] iArr;
        int[] iArr2 = offsetInWindow;
        if (isNestedScrollingEnabled()) {
            ViewParent nestedScrollingParentForType = getNestedScrollingParentForType(type);
            if (nestedScrollingParentForType == null) {
                return false;
            }
            if (dxConsumed != 0 || dyConsumed != 0 || dxUnconsumed != 0 || dyUnconsumed != 0) {
                if (iArr2 != null) {
                    this.mView.getLocationInWindow(iArr2);
                    i2 = iArr2[0];
                    i = iArr2[1];
                } else {
                    i2 = 0;
                    i = 0;
                }
                if (consumed == null) {
                    int[] tempNestedScrollConsumed = getTempNestedScrollConsumed();
                    tempNestedScrollConsumed[0] = 0;
                    tempNestedScrollConsumed[1] = 0;
                    iArr = tempNestedScrollConsumed;
                } else {
                    iArr = consumed;
                }
                ViewParentCompat.onNestedScroll(nestedScrollingParentForType, this.mView, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, iArr);
                if (iArr2 != null) {
                    this.mView.getLocationInWindow(iArr2);
                    iArr2[0] = iArr2[0] - i2;
                    iArr2[1] = iArr2[1] - i;
                }
                return true;
            } else if (iArr2 != null) {
                iArr2[0] = 0;
                iArr2[1] = 0;
            }
        } else {
            int i3 = type;
        }
        return false;
    }

    private ViewParent getNestedScrollingParentForType(int type) {
        switch (type) {
            case 0:
                return this.mNestedScrollingParentTouch;
            case 1:
                return this.mNestedScrollingParentNonTouch;
            default:
                return null;
        }
    }

    private int[] getTempNestedScrollConsumed() {
        if (this.mTempNestedScrollConsumed == null) {
            this.mTempNestedScrollConsumed = new int[2];
        }
        return this.mTempNestedScrollConsumed;
    }

    private void setNestedScrollingParentForType(int type, ViewParent p) {
        switch (type) {
            case 0:
                this.mNestedScrollingParentTouch = p;
                return;
            case 1:
                this.mNestedScrollingParentNonTouch = p;
                return;
            default:
                return;
        }
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        ViewParent nestedScrollingParentForType;
        if (!isNestedScrollingEnabled() || (nestedScrollingParentForType = getNestedScrollingParentForType(0)) == null) {
            return false;
        }
        return ViewParentCompat.onNestedFling(nestedScrollingParentForType, this.mView, velocityX, velocityY, consumed);
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        ViewParent nestedScrollingParentForType;
        if (!isNestedScrollingEnabled() || (nestedScrollingParentForType = getNestedScrollingParentForType(0)) == null) {
            return false;
        }
        return ViewParentCompat.onNestedPreFling(nestedScrollingParentForType, this.mView, velocityX, velocityY);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, 0);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow, int type) {
        int i;
        int i2;
        int[] iArr = offsetInWindow;
        if (isNestedScrollingEnabled()) {
            ViewParent nestedScrollingParentForType = getNestedScrollingParentForType(type);
            if (nestedScrollingParentForType == null) {
                return false;
            }
            if (dx != 0 || dy != 0) {
                if (iArr != null) {
                    this.mView.getLocationInWindow(iArr);
                    i2 = iArr[0];
                    i = iArr[1];
                } else {
                    i2 = 0;
                    i = 0;
                }
                int[] tempNestedScrollConsumed = consumed == null ? getTempNestedScrollConsumed() : consumed;
                tempNestedScrollConsumed[0] = 0;
                tempNestedScrollConsumed[1] = 0;
                ViewParentCompat.onNestedPreScroll(nestedScrollingParentForType, this.mView, dx, dy, tempNestedScrollConsumed, type);
                if (iArr != null) {
                    this.mView.getLocationInWindow(iArr);
                    iArr[0] = iArr[0] - i2;
                    iArr[1] = iArr[1] - i;
                }
                return (tempNestedScrollConsumed[0] == 0 && tempNestedScrollConsumed[1] == 0) ? false : true;
            } else if (iArr != null) {
                iArr[0] = 0;
                iArr[1] = 0;
            }
        } else {
            int i3 = type;
        }
        return false;
    }

    public void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type, int[] consumed) {
        dispatchNestedScrollInternal(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed);
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return dispatchNestedScrollInternal(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, 0, (int[]) null);
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type) {
        return dispatchNestedScrollInternal(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, (int[]) null);
    }

    public boolean hasNestedScrollingParent() {
        return hasNestedScrollingParent(0);
    }

    public boolean hasNestedScrollingParent(int type) {
        return getNestedScrollingParentForType(type) != null;
    }

    public boolean isNestedScrollingEnabled() {
        return this.mIsNestedScrollingEnabled;
    }

    public void onDetachedFromWindow() {
        ViewCompat.stopNestedScroll(this.mView);
    }

    public void onStopNestedScroll(View child) {
        ViewCompat.stopNestedScroll(this.mView);
    }

    public void setNestedScrollingEnabled(boolean enabled) {
        if (this.mIsNestedScrollingEnabled) {
            ViewCompat.stopNestedScroll(this.mView);
        }
        this.mIsNestedScrollingEnabled = enabled;
    }

    public boolean startNestedScroll(int axes) {
        return startNestedScroll(axes, 0);
    }

    public boolean startNestedScroll(int axes, int type) {
        if (hasNestedScrollingParent(type)) {
            return true;
        }
        if (!isNestedScrollingEnabled()) {
            return false;
        }
        View view = this.mView;
        for (ViewParent parent = this.mView.getParent(); parent != null; parent = parent.getParent()) {
            if (ViewParentCompat.onStartNestedScroll(parent, view, this.mView, axes, type)) {
                setNestedScrollingParentForType(type, parent);
                ViewParentCompat.onNestedScrollAccepted(parent, view, this.mView, axes, type);
                return true;
            }
            if (parent instanceof View) {
                view = (View) parent;
            }
        }
        return false;
    }

    public void stopNestedScroll() {
        stopNestedScroll(0);
    }

    public void stopNestedScroll(int type) {
        ViewParent nestedScrollingParentForType = getNestedScrollingParentForType(type);
        if (nestedScrollingParentForType != null) {
            ViewParentCompat.onStopNestedScroll(nestedScrollingParentForType, this.mView, type);
            setNestedScrollingParentForType(type, (ViewParent) null);
        }
    }
}

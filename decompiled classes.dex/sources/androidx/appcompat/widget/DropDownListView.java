package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.R;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.widget.ListViewAutoScrollHelper;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class DropDownListView extends ListView {
    public static final int INVALID_POSITION = -1;
    public static final int NO_POSITION = -1;
    private ViewPropertyAnimatorCompat mClickAnimation;
    private boolean mDrawsInPressedState;
    private boolean mHijackFocus;
    private Field mIsChildViewEnabled;
    private boolean mListSelectionHidden;
    private int mMotionPosition;
    ResolveHoverRunnable mResolveHoverRunnable;
    private ListViewAutoScrollHelper mScrollHelper;
    private int mSelectionBottomPadding = 0;
    private int mSelectionLeftPadding = 0;
    private int mSelectionRightPadding = 0;
    private int mSelectionTopPadding = 0;
    private GateKeeperDrawable mSelector;
    private final Rect mSelectorRect = new Rect();

    static class Api21Impl {
        private Api21Impl() {
        }

        static void drawableHotspotChanged(View view, float x, float y) {
            view.drawableHotspotChanged(x, y);
        }
    }

    static class Api30Impl {
        private static boolean sHasMethods = true;
        private static Method sPositionSelector;
        private static Method sSetNextSelectedPositionInt;
        private static Method sSetSelectedPositionInt;

        static {
            Class<AbsListView> cls = AbsListView.class;
            try {
                Method declaredMethod = cls.getDeclaredMethod("positionSelector", new Class[]{Integer.TYPE, View.class, Boolean.TYPE, Float.TYPE, Float.TYPE});
                sPositionSelector = declaredMethod;
                declaredMethod.setAccessible(true);
                Method declaredMethod2 = AdapterView.class.getDeclaredMethod("setSelectedPositionInt", new Class[]{Integer.TYPE});
                sSetSelectedPositionInt = declaredMethod2;
                declaredMethod2.setAccessible(true);
                Method declaredMethod3 = AdapterView.class.getDeclaredMethod("setNextSelectedPositionInt", new Class[]{Integer.TYPE});
                sSetNextSelectedPositionInt = declaredMethod3;
                declaredMethod3.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        private Api30Impl() {
        }

        static boolean canPositionSelectorForHoveredItem() {
            return sHasMethods;
        }

        static void positionSelectorForHoveredItem(DropDownListView view, int position, View sel) {
            try {
                sPositionSelector.invoke(view, new Object[]{Integer.valueOf(position), sel, false, -1, -1});
                sSetSelectedPositionInt.invoke(view, new Object[]{Integer.valueOf(position)});
                sSetNextSelectedPositionInt.invoke(view, new Object[]{Integer.valueOf(position)});
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e2) {
                e2.printStackTrace();
            }
        }
    }

    private static class GateKeeperDrawable extends DrawableWrapper {
        private boolean mEnabled = true;

        GateKeeperDrawable(Drawable drawable) {
            super(drawable);
        }

        public void draw(Canvas canvas) {
            if (this.mEnabled) {
                super.draw(canvas);
            }
        }

        /* access modifiers changed from: package-private */
        public void setEnabled(boolean enabled) {
            this.mEnabled = enabled;
        }

        public void setHotspot(float x, float y) {
            if (this.mEnabled) {
                super.setHotspot(x, y);
            }
        }

        public void setHotspotBounds(int left, int top, int right, int bottom) {
            if (this.mEnabled) {
                super.setHotspotBounds(left, top, right, bottom);
            }
        }

        public boolean setState(int[] stateSet) {
            if (this.mEnabled) {
                return super.setState(stateSet);
            }
            return false;
        }

        public boolean setVisible(boolean visible, boolean restart) {
            if (this.mEnabled) {
                return super.setVisible(visible, restart);
            }
            return false;
        }
    }

    private class ResolveHoverRunnable implements Runnable {
        ResolveHoverRunnable() {
        }

        public void cancel() {
            DropDownListView.this.mResolveHoverRunnable = null;
            DropDownListView.this.removeCallbacks(this);
        }

        public void post() {
            DropDownListView.this.post(this);
        }

        public void run() {
            DropDownListView.this.mResolveHoverRunnable = null;
            DropDownListView.this.drawableStateChanged();
        }
    }

    DropDownListView(Context context, boolean hijackFocus) {
        super(context, (AttributeSet) null, R.attr.dropDownListViewStyle);
        this.mHijackFocus = hijackFocus;
        setCacheColorHint(0);
        try {
            Field declaredField = AbsListView.class.getDeclaredField("mIsChildViewEnabled");
            this.mIsChildViewEnabled = declaredField;
            declaredField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void clearPressedItem() {
        this.mDrawsInPressedState = false;
        setPressed(false);
        drawableStateChanged();
        View childAt = getChildAt(this.mMotionPosition - getFirstVisiblePosition());
        if (childAt != null) {
            childAt.setPressed(false);
        }
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.mClickAnimation;
        if (viewPropertyAnimatorCompat != null) {
            viewPropertyAnimatorCompat.cancel();
            this.mClickAnimation = null;
        }
    }

    private void clickPressedItem(View child, int position) {
        performItemClick(child, position, getItemIdAtPosition(position));
    }

    private void drawSelectorCompat(Canvas canvas) {
        Drawable selector;
        if (!this.mSelectorRect.isEmpty() && (selector = getSelector()) != null) {
            selector.setBounds(this.mSelectorRect);
            selector.draw(canvas);
        }
    }

    private void positionSelectorCompat(int position, View sel) {
        Rect rect = this.mSelectorRect;
        rect.set(sel.getLeft(), sel.getTop(), sel.getRight(), sel.getBottom());
        rect.left -= this.mSelectionLeftPadding;
        rect.top -= this.mSelectionTopPadding;
        rect.right += this.mSelectionRightPadding;
        rect.bottom += this.mSelectionBottomPadding;
        try {
            boolean z = this.mIsChildViewEnabled.getBoolean(this);
            if (sel.isEnabled() != z) {
                this.mIsChildViewEnabled.set(this, Boolean.valueOf(!z));
                if (position != -1) {
                    refreshDrawableState();
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void positionSelectorLikeFocusCompat(int position, View sel) {
        Drawable selector = getSelector();
        boolean z = true;
        boolean z2 = (selector == null || position == -1) ? false : true;
        if (z2) {
            selector.setVisible(false, false);
        }
        positionSelectorCompat(position, sel);
        if (z2) {
            Rect rect = this.mSelectorRect;
            float exactCenterX = rect.exactCenterX();
            float exactCenterY = rect.exactCenterY();
            if (getVisibility() != 0) {
                z = false;
            }
            selector.setVisible(z, false);
            DrawableCompat.setHotspot(selector, exactCenterX, exactCenterY);
        }
    }

    private void positionSelectorLikeTouchCompat(int position, View sel, float x, float y) {
        positionSelectorLikeFocusCompat(position, sel);
        Drawable selector = getSelector();
        if (selector != null && position != -1) {
            DrawableCompat.setHotspot(selector, x, y);
        }
    }

    private void setPressedItem(View child, int position, float x, float y) {
        View childAt;
        this.mDrawsInPressedState = true;
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.drawableHotspotChanged(this, x, y);
        }
        if (!isPressed()) {
            setPressed(true);
        }
        layoutChildren();
        int i = this.mMotionPosition;
        if (!(i == -1 || (childAt = getChildAt(i - getFirstVisiblePosition())) == null || childAt == child || !childAt.isPressed())) {
            childAt.setPressed(false);
        }
        this.mMotionPosition = position;
        float left = x - ((float) child.getLeft());
        float top = y - ((float) child.getTop());
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.drawableHotspotChanged(child, left, top);
        }
        if (!child.isPressed()) {
            child.setPressed(true);
        }
        positionSelectorLikeTouchCompat(position, child, x, y);
        setSelectorEnabled(false);
        refreshDrawableState();
    }

    private void setSelectorEnabled(boolean enabled) {
        GateKeeperDrawable gateKeeperDrawable = this.mSelector;
        if (gateKeeperDrawable != null) {
            gateKeeperDrawable.setEnabled(enabled);
        }
    }

    private boolean touchModeDrawsInPressedStateCompat() {
        return this.mDrawsInPressedState;
    }

    private void updateSelectorStateCompat() {
        Drawable selector = getSelector();
        if (selector != null && touchModeDrawsInPressedStateCompat() && isPressed()) {
            selector.setState(getDrawableState());
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        drawSelectorCompat(canvas);
        super.dispatchDraw(canvas);
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        if (this.mResolveHoverRunnable == null) {
            super.drawableStateChanged();
            setSelectorEnabled(true);
            updateSelectorStateCompat();
        }
    }

    public boolean hasFocus() {
        return this.mHijackFocus || super.hasFocus();
    }

    public boolean hasWindowFocus() {
        return this.mHijackFocus || super.hasWindowFocus();
    }

    public boolean isFocused() {
        return this.mHijackFocus || super.isFocused();
    }

    public boolean isInTouchMode() {
        return (this.mHijackFocus && this.mListSelectionHidden) || super.isInTouchMode();
    }

    public int lookForSelectablePosition(int position, boolean lookDown) {
        int position2;
        ListAdapter adapter = getAdapter();
        if (adapter == null || isInTouchMode()) {
            return -1;
        }
        int count = adapter.getCount();
        if (!getAdapter().areAllItemsEnabled()) {
            if (lookDown) {
                position2 = Math.max(0, position);
                while (position2 < count && !adapter.isEnabled(position2)) {
                    position2++;
                }
            } else {
                int position3 = Math.min(position, count - 1);
                while (position2 >= 0 && !adapter.isEnabled(position2)) {
                    position3 = position2 - 1;
                }
            }
            if (position2 < 0 || position2 >= count) {
                return -1;
            }
            return position2;
        } else if (position < 0 || position >= count) {
            return -1;
        } else {
            return position;
        }
    }

    public int measureHeightOfChildrenCompat(int widthMeasureSpec, int startPosition, int endPosition, int maxHeight, int disallowPartialChildPosition) {
        int i;
        ViewGroup.LayoutParams layoutParams;
        int i2;
        int i3 = maxHeight;
        int i4 = disallowPartialChildPosition;
        int listPaddingTop = getListPaddingTop();
        int listPaddingBottom = getListPaddingBottom();
        int dividerHeight = getDividerHeight();
        Drawable divider = getDivider();
        ListAdapter adapter = getAdapter();
        if (adapter == null) {
            return listPaddingTop + listPaddingBottom;
        }
        int i5 = listPaddingTop + listPaddingBottom;
        int i6 = (dividerHeight <= 0 || divider == null) ? 0 : dividerHeight;
        int i7 = 0;
        View view = null;
        int i8 = 0;
        int count = adapter.getCount();
        int i9 = 0;
        while (i9 < count) {
            int itemViewType = adapter.getItemViewType(i9);
            if (itemViewType != i8) {
                view = null;
                i8 = itemViewType;
            }
            view = adapter.getView(i9, view, this);
            ViewGroup.LayoutParams layoutParams2 = view.getLayoutParams();
            if (layoutParams2 == null) {
                i = listPaddingTop;
                layoutParams = generateDefaultLayoutParams();
                view.setLayoutParams(layoutParams);
            } else {
                i = listPaddingTop;
                layoutParams = layoutParams2;
            }
            int i10 = listPaddingBottom;
            if (layoutParams.height > 0) {
                ViewGroup.LayoutParams layoutParams3 = layoutParams;
                i2 = View.MeasureSpec.makeMeasureSpec(layoutParams.height, BasicMeasure.EXACTLY);
            } else {
                ViewGroup.LayoutParams layoutParams4 = layoutParams;
                i2 = View.MeasureSpec.makeMeasureSpec(0, 0);
            }
            view.measure(widthMeasureSpec, i2);
            view.forceLayout();
            if (i9 > 0) {
                i5 += i6;
            }
            i5 += view.getMeasuredHeight();
            if (i5 >= i3) {
                return (i4 < 0 || i9 <= i4 || i7 <= 0 || i5 == i3) ? i3 : i7;
            }
            if (i4 >= 0 && i9 >= i4) {
                i7 = i5;
            }
            i9++;
            listPaddingBottom = i10;
            listPaddingTop = i;
        }
        return i5;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mResolveHoverRunnable = null;
        super.onDetachedFromWindow();
    }

    public boolean onForwardedEvent(MotionEvent event, int activePointerId) {
        boolean z = true;
        boolean z2 = false;
        int actionMasked = event.getActionMasked();
        switch (actionMasked) {
            case 1:
                z = false;
                break;
            case 2:
                break;
            case 3:
                z = false;
                break;
        }
        int findPointerIndex = event.findPointerIndex(activePointerId);
        if (findPointerIndex < 0) {
            z = false;
        } else {
            int x = (int) event.getX(findPointerIndex);
            int y = (int) event.getY(findPointerIndex);
            int pointToPosition = pointToPosition(x, y);
            if (pointToPosition == -1) {
                z2 = true;
            } else {
                View childAt = getChildAt(pointToPosition - getFirstVisiblePosition());
                setPressedItem(childAt, pointToPosition, (float) x, (float) y);
                z = true;
                if (actionMasked == 1) {
                    clickPressedItem(childAt, pointToPosition);
                }
            }
        }
        if (!z || z2) {
            clearPressedItem();
        }
        if (z) {
            if (this.mScrollHelper == null) {
                this.mScrollHelper = new ListViewAutoScrollHelper(this);
            }
            this.mScrollHelper.setEnabled(true);
            this.mScrollHelper.onTouch(this, event);
        } else {
            ListViewAutoScrollHelper listViewAutoScrollHelper = this.mScrollHelper;
            if (listViewAutoScrollHelper != null) {
                listViewAutoScrollHelper.setEnabled(false);
            }
        }
        return z;
    }

    public boolean onHoverEvent(MotionEvent ev) {
        if (Build.VERSION.SDK_INT < 26) {
            return super.onHoverEvent(ev);
        }
        int actionMasked = ev.getActionMasked();
        if (actionMasked == 10 && this.mResolveHoverRunnable == null) {
            ResolveHoverRunnable resolveHoverRunnable = new ResolveHoverRunnable();
            this.mResolveHoverRunnable = resolveHoverRunnable;
            resolveHoverRunnable.post();
        }
        boolean onHoverEvent = super.onHoverEvent(ev);
        if (actionMasked == 9 || actionMasked == 7) {
            int pointToPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
            if (!(pointToPosition == -1 || pointToPosition == getSelectedItemPosition())) {
                View childAt = getChildAt(pointToPosition - getFirstVisiblePosition());
                if (childAt.isEnabled()) {
                    requestFocus();
                    if (Build.VERSION.SDK_INT < 30 || !Api30Impl.canPositionSelectorForHoveredItem()) {
                        setSelectionFromTop(pointToPosition, childAt.getTop() - getTop());
                    } else {
                        Api30Impl.positionSelectorForHoveredItem(this, pointToPosition, childAt);
                    }
                }
                updateSelectorStateCompat();
            }
        } else {
            setSelection(-1);
        }
        return onHoverEvent;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case 0:
                this.mMotionPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
                break;
        }
        ResolveHoverRunnable resolveHoverRunnable = this.mResolveHoverRunnable;
        if (resolveHoverRunnable != null) {
            resolveHoverRunnable.cancel();
        }
        return super.onTouchEvent(ev);
    }

    /* access modifiers changed from: package-private */
    public void setListSelectionHidden(boolean hideListSelection) {
        this.mListSelectionHidden = hideListSelection;
    }

    public void setSelector(Drawable sel) {
        GateKeeperDrawable gateKeeperDrawable = sel != null ? new GateKeeperDrawable(sel) : null;
        this.mSelector = gateKeeperDrawable;
        super.setSelector(gateKeeperDrawable);
        Rect rect = new Rect();
        if (sel != null) {
            sel.getPadding(rect);
        }
        this.mSelectionLeftPadding = rect.left;
        this.mSelectionTopPadding = rect.top;
        this.mSelectionRightPadding = rect.right;
        this.mSelectionBottomPadding = rect.bottom;
    }
}

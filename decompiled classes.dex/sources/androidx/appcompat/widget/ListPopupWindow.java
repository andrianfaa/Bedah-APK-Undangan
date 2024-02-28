package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import androidx.appcompat.R;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.PopupWindowCompat;
import java.lang.reflect.Method;

public class ListPopupWindow implements ShowableListMenu {
    private static final boolean DEBUG = false;
    static final int EXPAND_LIST_TIMEOUT = 250;
    public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
    public static final int INPUT_METHOD_NEEDED = 1;
    public static final int INPUT_METHOD_NOT_NEEDED = 2;
    public static final int MATCH_PARENT = -1;
    public static final int POSITION_PROMPT_ABOVE = 0;
    public static final int POSITION_PROMPT_BELOW = 1;
    private static final String TAG = "ListPopupWindow";
    public static final int WRAP_CONTENT = -2;
    private static Method sGetMaxAvailableHeightMethod;
    private static Method sSetClipToWindowEnabledMethod;
    private static Method sSetEpicenterBoundsMethod;
    private ListAdapter mAdapter;
    private Context mContext;
    private boolean mDropDownAlwaysVisible;
    private View mDropDownAnchorView;
    private int mDropDownGravity;
    private int mDropDownHeight;
    private int mDropDownHorizontalOffset;
    DropDownListView mDropDownList;
    private Drawable mDropDownListHighlight;
    private int mDropDownVerticalOffset;
    private boolean mDropDownVerticalOffsetSet;
    private int mDropDownWidth;
    private int mDropDownWindowLayoutType;
    private Rect mEpicenterBounds;
    private boolean mForceIgnoreOutsideTouch;
    final Handler mHandler;
    private final ListSelectorHider mHideSelector;
    private AdapterView.OnItemClickListener mItemClickListener;
    private AdapterView.OnItemSelectedListener mItemSelectedListener;
    int mListItemExpandMaximum;
    private boolean mModal;
    private DataSetObserver mObserver;
    private boolean mOverlapAnchor;
    private boolean mOverlapAnchorSet;
    PopupWindow mPopup;
    private int mPromptPosition;
    private View mPromptView;
    final ResizePopupRunnable mResizePopupRunnable;
    private final PopupScrollListener mScrollListener;
    private Runnable mShowDropDownRunnable;
    private final Rect mTempRect;
    private final PopupTouchInterceptor mTouchInterceptor;

    static class Api24Impl {
        private Api24Impl() {
        }

        static int getMaxAvailableHeight(PopupWindow popupWindow, View anchor, int yOffset, boolean ignoreBottomDecorations) {
            return popupWindow.getMaxAvailableHeight(anchor, yOffset, ignoreBottomDecorations);
        }
    }

    static class Api29Impl {
        private Api29Impl() {
        }

        static void setEpicenterBounds(PopupWindow popupWindow, Rect bounds) {
            popupWindow.setEpicenterBounds(bounds);
        }

        static void setIsClippedToScreen(PopupWindow popupWindow, boolean enabled) {
            popupWindow.setIsClippedToScreen(enabled);
        }
    }

    private class ListSelectorHider implements Runnable {
        ListSelectorHider() {
        }

        public void run() {
            ListPopupWindow.this.clearListSelection();
        }
    }

    private class PopupDataSetObserver extends DataSetObserver {
        PopupDataSetObserver() {
        }

        public void onChanged() {
            if (ListPopupWindow.this.isShowing()) {
                ListPopupWindow.this.show();
            }
        }

        public void onInvalidated() {
            ListPopupWindow.this.dismiss();
        }
    }

    private class PopupScrollListener implements AbsListView.OnScrollListener {
        PopupScrollListener() {
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == 1 && !ListPopupWindow.this.isInputMethodNotNeeded() && ListPopupWindow.this.mPopup.getContentView() != null) {
                ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable);
                ListPopupWindow.this.mResizePopupRunnable.run();
            }
        }
    }

    private class PopupTouchInterceptor implements View.OnTouchListener {
        PopupTouchInterceptor() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (action == 0 && ListPopupWindow.this.mPopup != null && ListPopupWindow.this.mPopup.isShowing() && x >= 0 && x < ListPopupWindow.this.mPopup.getWidth() && y >= 0 && y < ListPopupWindow.this.mPopup.getHeight()) {
                ListPopupWindow.this.mHandler.postDelayed(ListPopupWindow.this.mResizePopupRunnable, 250);
                return false;
            } else if (action != 1) {
                return false;
            } else {
                ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable);
                return false;
            }
        }
    }

    private class ResizePopupRunnable implements Runnable {
        ResizePopupRunnable() {
        }

        public void run() {
            if (ListPopupWindow.this.mDropDownList != null && ViewCompat.isAttachedToWindow(ListPopupWindow.this.mDropDownList) && ListPopupWindow.this.mDropDownList.getCount() > ListPopupWindow.this.mDropDownList.getChildCount() && ListPopupWindow.this.mDropDownList.getChildCount() <= ListPopupWindow.this.mListItemExpandMaximum) {
                ListPopupWindow.this.mPopup.setInputMethodMode(2);
                ListPopupWindow.this.show();
            }
        }
    }

    static {
        if (Build.VERSION.SDK_INT <= 28) {
            Class<PopupWindow> cls = PopupWindow.class;
            try {
                sSetClipToWindowEnabledMethod = cls.getDeclaredMethod("setClipToScreenEnabled", new Class[]{Boolean.TYPE});
            } catch (NoSuchMethodException e) {
                Log.i(TAG, "Could not find method setClipToScreenEnabled() on PopupWindow. Oh well.");
            }
            try {
                sSetEpicenterBoundsMethod = PopupWindow.class.getDeclaredMethod("setEpicenterBounds", new Class[]{Rect.class});
            } catch (NoSuchMethodException e2) {
                Log.i(TAG, "Could not find method setEpicenterBounds(Rect) on PopupWindow. Oh well.");
            }
        }
        if (Build.VERSION.SDK_INT <= 23) {
            Class<PopupWindow> cls2 = PopupWindow.class;
            try {
                sGetMaxAvailableHeightMethod = cls2.getDeclaredMethod("getMaxAvailableHeight", new Class[]{View.class, Integer.TYPE, Boolean.TYPE});
            } catch (NoSuchMethodException e3) {
                Log.i(TAG, "Could not find method getMaxAvailableHeight(View, int, boolean) on PopupWindow. Oh well.");
            }
        }
    }

    public ListPopupWindow(Context context) {
        this(context, (AttributeSet) null, R.attr.listPopupWindowStyle);
    }

    public ListPopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.listPopupWindowStyle);
    }

    public ListPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ListPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.mDropDownHeight = -2;
        this.mDropDownWidth = -2;
        this.mDropDownWindowLayoutType = PointerIconCompat.TYPE_HAND;
        this.mDropDownGravity = 0;
        this.mDropDownAlwaysVisible = false;
        this.mForceIgnoreOutsideTouch = false;
        this.mListItemExpandMaximum = Integer.MAX_VALUE;
        this.mPromptPosition = 0;
        this.mResizePopupRunnable = new ResizePopupRunnable();
        this.mTouchInterceptor = new PopupTouchInterceptor();
        this.mScrollListener = new PopupScrollListener();
        this.mHideSelector = new ListSelectorHider();
        this.mTempRect = new Rect();
        this.mContext = context;
        this.mHandler = new Handler(context.getMainLooper());
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ListPopupWindow, defStyleAttr, defStyleRes);
        this.mDropDownHorizontalOffset = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownHorizontalOffset, 0);
        int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownVerticalOffset, 0);
        this.mDropDownVerticalOffset = dimensionPixelOffset;
        if (dimensionPixelOffset != 0) {
            this.mDropDownVerticalOffsetSet = true;
        }
        obtainStyledAttributes.recycle();
        AppCompatPopupWindow appCompatPopupWindow = new AppCompatPopupWindow(context, attrs, defStyleAttr, defStyleRes);
        this.mPopup = appCompatPopupWindow;
        appCompatPopupWindow.setInputMethodMode(1);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v13, resolved type: androidx.appcompat.widget.DropDownListView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v14, resolved type: androidx.appcompat.widget.DropDownListView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v21, resolved type: android.widget.LinearLayout} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v15, resolved type: androidx.appcompat.widget.DropDownListView} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int buildDropDown() {
        /*
            r15 = this;
            r0 = 0
            androidx.appcompat.widget.DropDownListView r1 = r15.mDropDownList
            r2 = -1
            r3 = 0
            r4 = 1
            if (r1 != 0) goto L_0x00c2
            android.content.Context r1 = r15.mContext
            androidx.appcompat.widget.ListPopupWindow$2 r5 = new androidx.appcompat.widget.ListPopupWindow$2
            r5.<init>()
            r15.mShowDropDownRunnable = r5
            boolean r5 = r15.mModal
            r5 = r5 ^ r4
            androidx.appcompat.widget.DropDownListView r5 = r15.createDropDownListView(r1, r5)
            r15.mDropDownList = r5
            android.graphics.drawable.Drawable r6 = r15.mDropDownListHighlight
            if (r6 == 0) goto L_0x0021
            r5.setSelector(r6)
        L_0x0021:
            androidx.appcompat.widget.DropDownListView r5 = r15.mDropDownList
            android.widget.ListAdapter r6 = r15.mAdapter
            r5.setAdapter(r6)
            androidx.appcompat.widget.DropDownListView r5 = r15.mDropDownList
            android.widget.AdapterView$OnItemClickListener r6 = r15.mItemClickListener
            r5.setOnItemClickListener(r6)
            androidx.appcompat.widget.DropDownListView r5 = r15.mDropDownList
            r5.setFocusable(r4)
            androidx.appcompat.widget.DropDownListView r5 = r15.mDropDownList
            r5.setFocusableInTouchMode(r4)
            androidx.appcompat.widget.DropDownListView r5 = r15.mDropDownList
            androidx.appcompat.widget.ListPopupWindow$3 r6 = new androidx.appcompat.widget.ListPopupWindow$3
            r6.<init>()
            r5.setOnItemSelectedListener(r6)
            androidx.appcompat.widget.DropDownListView r5 = r15.mDropDownList
            androidx.appcompat.widget.ListPopupWindow$PopupScrollListener r6 = r15.mScrollListener
            r5.setOnScrollListener(r6)
            android.widget.AdapterView$OnItemSelectedListener r5 = r15.mItemSelectedListener
            if (r5 == 0) goto L_0x0053
            androidx.appcompat.widget.DropDownListView r6 = r15.mDropDownList
            r6.setOnItemSelectedListener(r5)
        L_0x0053:
            androidx.appcompat.widget.DropDownListView r5 = r15.mDropDownList
            android.view.View r6 = r15.mPromptView
            if (r6 == 0) goto L_0x00bc
            android.widget.LinearLayout r7 = new android.widget.LinearLayout
            r7.<init>(r1)
            r7.setOrientation(r4)
            android.widget.LinearLayout$LayoutParams r8 = new android.widget.LinearLayout$LayoutParams
            r9 = 1065353216(0x3f800000, float:1.0)
            r8.<init>(r2, r3, r9)
            int r9 = r15.mPromptPosition
            switch(r9) {
                case 0: goto L_0x008f;
                case 1: goto L_0x0088;
                default: goto L_0x006d;
            }
        L_0x006d:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "Invalid hint position "
            java.lang.StringBuilder r9 = r9.append(r10)
            int r10 = r15.mPromptPosition
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.String r9 = r9.toString()
            java.lang.String r10 = "ListPopupWindow"
            android.util.Log.e(r10, r9)
            goto L_0x0096
        L_0x0088:
            r7.addView(r5, r8)
            r7.addView(r6)
            goto L_0x0096
        L_0x008f:
            r7.addView(r6)
            r7.addView(r5, r8)
        L_0x0096:
            int r9 = r15.mDropDownWidth
            if (r9 < 0) goto L_0x009f
            r9 = -2147483648(0xffffffff80000000, float:-0.0)
            int r10 = r15.mDropDownWidth
            goto L_0x00a1
        L_0x009f:
            r9 = 0
            r10 = 0
        L_0x00a1:
            int r11 = android.view.View.MeasureSpec.makeMeasureSpec(r10, r9)
            r12 = 0
            r6.measure(r11, r3)
            android.view.ViewGroup$LayoutParams r13 = r6.getLayoutParams()
            r8 = r13
            android.widget.LinearLayout$LayoutParams r8 = (android.widget.LinearLayout.LayoutParams) r8
            int r13 = r6.getMeasuredHeight()
            int r14 = r8.topMargin
            int r13 = r13 + r14
            int r14 = r8.bottomMargin
            int r13 = r13 + r14
            r5 = r7
            r0 = r13
        L_0x00bc:
            android.widget.PopupWindow r7 = r15.mPopup
            r7.setContentView(r5)
            goto L_0x00e1
        L_0x00c2:
            android.widget.PopupWindow r1 = r15.mPopup
            android.view.View r1 = r1.getContentView()
            r5 = r1
            android.view.ViewGroup r5 = (android.view.ViewGroup) r5
            android.view.View r1 = r15.mPromptView
            if (r1 == 0) goto L_0x00e1
            android.view.ViewGroup$LayoutParams r6 = r1.getLayoutParams()
            android.widget.LinearLayout$LayoutParams r6 = (android.widget.LinearLayout.LayoutParams) r6
            int r7 = r1.getMeasuredHeight()
            int r8 = r6.topMargin
            int r7 = r7 + r8
            int r8 = r6.bottomMargin
            int r0 = r7 + r8
        L_0x00e1:
            android.widget.PopupWindow r1 = r15.mPopup
            android.graphics.drawable.Drawable r1 = r1.getBackground()
            if (r1 == 0) goto L_0x0103
            android.graphics.Rect r6 = r15.mTempRect
            r1.getPadding(r6)
            android.graphics.Rect r6 = r15.mTempRect
            int r6 = r6.top
            android.graphics.Rect r7 = r15.mTempRect
            int r7 = r7.bottom
            int r6 = r6 + r7
            boolean r7 = r15.mDropDownVerticalOffsetSet
            if (r7 != 0) goto L_0x0109
            android.graphics.Rect r7 = r15.mTempRect
            int r7 = r7.top
            int r7 = -r7
            r15.mDropDownVerticalOffset = r7
            goto L_0x0109
        L_0x0103:
            android.graphics.Rect r6 = r15.mTempRect
            r6.setEmpty()
            r6 = 0
        L_0x0109:
            android.widget.PopupWindow r7 = r15.mPopup
            int r7 = r7.getInputMethodMode()
            r8 = 2
            if (r7 != r8) goto L_0x0113
            r3 = r4
        L_0x0113:
            android.view.View r4 = r15.getAnchorView()
            int r7 = r15.mDropDownVerticalOffset
            int r4 = r15.getMaxAvailableHeight(r4, r7, r3)
            boolean r7 = r15.mDropDownAlwaysVisible
            if (r7 != 0) goto L_0x018b
            int r7 = r15.mDropDownHeight
            if (r7 != r2) goto L_0x0126
            goto L_0x018b
        L_0x0126:
            int r2 = r15.mDropDownWidth
            r7 = 1073741824(0x40000000, float:2.0)
            switch(r2) {
                case -2: goto L_0x014d;
                case -1: goto L_0x0132;
                default: goto L_0x012d;
            }
        L_0x012d:
            int r2 = android.view.View.MeasureSpec.makeMeasureSpec(r2, r7)
            goto L_0x016a
        L_0x0132:
            android.content.Context r2 = r15.mContext
            android.content.res.Resources r2 = r2.getResources()
            android.util.DisplayMetrics r2 = r2.getDisplayMetrics()
            int r2 = r2.widthPixels
            android.graphics.Rect r8 = r15.mTempRect
            int r8 = r8.left
            android.graphics.Rect r9 = r15.mTempRect
            int r9 = r9.right
            int r8 = r8 + r9
            int r2 = r2 - r8
            int r2 = android.view.View.MeasureSpec.makeMeasureSpec(r2, r7)
            goto L_0x016a
        L_0x014d:
            android.content.Context r2 = r15.mContext
            android.content.res.Resources r2 = r2.getResources()
            android.util.DisplayMetrics r2 = r2.getDisplayMetrics()
            int r2 = r2.widthPixels
            android.graphics.Rect r7 = r15.mTempRect
            int r7 = r7.left
            android.graphics.Rect r8 = r15.mTempRect
            int r8 = r8.right
            int r7 = r7 + r8
            int r2 = r2 - r7
            r7 = -2147483648(0xffffffff80000000, float:-0.0)
            int r2 = android.view.View.MeasureSpec.makeMeasureSpec(r2, r7)
        L_0x016a:
            androidx.appcompat.widget.DropDownListView r7 = r15.mDropDownList
            r9 = 0
            r10 = -1
            int r11 = r4 - r0
            r12 = -1
            r8 = r2
            int r7 = r7.measureHeightOfChildrenCompat(r8, r9, r10, r11, r12)
            if (r7 <= 0) goto L_0x0188
            androidx.appcompat.widget.DropDownListView r8 = r15.mDropDownList
            int r8 = r8.getPaddingTop()
            androidx.appcompat.widget.DropDownListView r9 = r15.mDropDownList
            int r9 = r9.getPaddingBottom()
            int r8 = r8 + r9
            int r9 = r6 + r8
            int r0 = r0 + r9
        L_0x0188:
            int r8 = r7 + r0
            return r8
        L_0x018b:
            int r2 = r4 + r6
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.ListPopupWindow.buildDropDown():int");
    }

    private int getMaxAvailableHeight(View anchor, int yOffset, boolean ignoreBottomDecorations) {
        if (Build.VERSION.SDK_INT > 23) {
            return Api24Impl.getMaxAvailableHeight(this.mPopup, anchor, yOffset, ignoreBottomDecorations);
        }
        Method method = sGetMaxAvailableHeightMethod;
        if (method != null) {
            try {
                return ((Integer) method.invoke(this.mPopup, new Object[]{anchor, Integer.valueOf(yOffset), Boolean.valueOf(ignoreBottomDecorations)})).intValue();
            } catch (Exception e) {
                Log.i(TAG, "Could not call getMaxAvailableHeightMethod(View, int, boolean) on PopupWindow. Using the public version.");
            }
        }
        return this.mPopup.getMaxAvailableHeight(anchor, yOffset);
    }

    private static boolean isConfirmKey(int keyCode) {
        return keyCode == 66 || keyCode == 23;
    }

    private void removePromptView() {
        View view = this.mPromptView;
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this.mPromptView);
            }
        }
    }

    private void setPopupClipToScreenEnabled(boolean clip) {
        if (Build.VERSION.SDK_INT <= 28) {
            Method method = sSetClipToWindowEnabledMethod;
            if (method != null) {
                try {
                    method.invoke(this.mPopup, new Object[]{Boolean.valueOf(clip)});
                } catch (Exception e) {
                    Log.i(TAG, "Could not call setClipToScreenEnabled() on PopupWindow. Oh well.");
                }
            }
        } else {
            Api29Impl.setIsClippedToScreen(this.mPopup, clip);
        }
    }

    public void clearListSelection() {
        DropDownListView dropDownListView = this.mDropDownList;
        if (dropDownListView != null) {
            dropDownListView.setListSelectionHidden(true);
            dropDownListView.requestLayout();
        }
    }

    public View.OnTouchListener createDragToOpenListener(View src) {
        return new ForwardingListener(src) {
            public ListPopupWindow getPopup() {
                return ListPopupWindow.this;
            }
        };
    }

    /* access modifiers changed from: package-private */
    public DropDownListView createDropDownListView(Context context, boolean hijackFocus) {
        return new DropDownListView(context, hijackFocus);
    }

    public void dismiss() {
        this.mPopup.dismiss();
        removePromptView();
        this.mPopup.setContentView((View) null);
        this.mDropDownList = null;
        this.mHandler.removeCallbacks(this.mResizePopupRunnable);
    }

    public View getAnchorView() {
        return this.mDropDownAnchorView;
    }

    public int getAnimationStyle() {
        return this.mPopup.getAnimationStyle();
    }

    public Drawable getBackground() {
        return this.mPopup.getBackground();
    }

    public Rect getEpicenterBounds() {
        if (this.mEpicenterBounds != null) {
            return new Rect(this.mEpicenterBounds);
        }
        return null;
    }

    public int getHeight() {
        return this.mDropDownHeight;
    }

    public int getHorizontalOffset() {
        return this.mDropDownHorizontalOffset;
    }

    public int getInputMethodMode() {
        return this.mPopup.getInputMethodMode();
    }

    public ListView getListView() {
        return this.mDropDownList;
    }

    public int getPromptPosition() {
        return this.mPromptPosition;
    }

    public Object getSelectedItem() {
        if (!isShowing()) {
            return null;
        }
        return this.mDropDownList.getSelectedItem();
    }

    public long getSelectedItemId() {
        if (!isShowing()) {
            return Long.MIN_VALUE;
        }
        return this.mDropDownList.getSelectedItemId();
    }

    public int getSelectedItemPosition() {
        if (!isShowing()) {
            return -1;
        }
        return this.mDropDownList.getSelectedItemPosition();
    }

    public View getSelectedView() {
        if (!isShowing()) {
            return null;
        }
        return this.mDropDownList.getSelectedView();
    }

    public int getSoftInputMode() {
        return this.mPopup.getSoftInputMode();
    }

    public int getVerticalOffset() {
        if (!this.mDropDownVerticalOffsetSet) {
            return 0;
        }
        return this.mDropDownVerticalOffset;
    }

    public int getWidth() {
        return this.mDropDownWidth;
    }

    public boolean isDropDownAlwaysVisible() {
        return this.mDropDownAlwaysVisible;
    }

    public boolean isInputMethodNotNeeded() {
        return this.mPopup.getInputMethodMode() == 2;
    }

    public boolean isModal() {
        return this.mModal;
    }

    public boolean isShowing() {
        return this.mPopup.isShowing();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isShowing() && keyCode != 62 && (this.mDropDownList.getSelectedItemPosition() >= 0 || !isConfirmKey(keyCode))) {
            int selectedItemPosition = this.mDropDownList.getSelectedItemPosition();
            boolean z = !this.mPopup.isAboveAnchor();
            ListAdapter listAdapter = this.mAdapter;
            int i = Integer.MAX_VALUE;
            int i2 = Integer.MIN_VALUE;
            if (listAdapter != null) {
                boolean areAllItemsEnabled = listAdapter.areAllItemsEnabled();
                i = areAllItemsEnabled ? 0 : this.mDropDownList.lookForSelectablePosition(0, true);
                i2 = areAllItemsEnabled ? listAdapter.getCount() - 1 : this.mDropDownList.lookForSelectablePosition(listAdapter.getCount() - 1, false);
            }
            if ((!z || keyCode != 19 || selectedItemPosition > i) && (z || keyCode != 20 || selectedItemPosition < i2)) {
                this.mDropDownList.setListSelectionHidden(false);
                if (this.mDropDownList.onKeyDown(keyCode, event)) {
                    this.mPopup.setInputMethodMode(2);
                    this.mDropDownList.requestFocusFromTouch();
                    show();
                    switch (keyCode) {
                        case 19:
                        case 20:
                        case 23:
                        case ConstraintLayout.LayoutParams.Table.LAYOUT_WRAP_BEHAVIOR_IN_PARENT /*66*/:
                            return true;
                    }
                } else if (!z || keyCode != 20) {
                    return !z && keyCode == 19 && selectedItemPosition == i;
                } else {
                    if (selectedItemPosition == i2) {
                        return true;
                    }
                }
            } else {
                clearListSelection();
                this.mPopup.setInputMethodMode(1);
                show();
                return true;
            }
        }
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !isShowing()) {
            return false;
        }
        View view = this.mDropDownAnchorView;
        if (event.getAction() == 0 && event.getRepeatCount() == 0) {
            KeyEvent.DispatcherState keyDispatcherState = view.getKeyDispatcherState();
            if (keyDispatcherState != null) {
                keyDispatcherState.startTracking(event, this);
            }
            return true;
        } else if (event.getAction() != 1) {
            return false;
        } else {
            KeyEvent.DispatcherState keyDispatcherState2 = view.getKeyDispatcherState();
            if (keyDispatcherState2 != null) {
                keyDispatcherState2.handleUpEvent(event);
            }
            if (!event.isTracking() || event.isCanceled()) {
                return false;
            }
            dismiss();
            return true;
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!isShowing() || this.mDropDownList.getSelectedItemPosition() < 0) {
            return false;
        }
        boolean onKeyUp = this.mDropDownList.onKeyUp(keyCode, event);
        if (onKeyUp && isConfirmKey(keyCode)) {
            dismiss();
        }
        return onKeyUp;
    }

    public boolean performItemClick(int position) {
        if (!isShowing()) {
            return false;
        }
        if (this.mItemClickListener == null) {
            return true;
        }
        DropDownListView dropDownListView = this.mDropDownList;
        View childAt = dropDownListView.getChildAt(position - dropDownListView.getFirstVisiblePosition());
        DropDownListView dropDownListView2 = dropDownListView;
        View view = childAt;
        int i = position;
        this.mItemClickListener.onItemClick(dropDownListView2, view, i, dropDownListView.getAdapter().getItemId(position));
        return true;
    }

    public void postShow() {
        this.mHandler.post(this.mShowDropDownRunnable);
    }

    public void setAdapter(ListAdapter adapter) {
        DataSetObserver dataSetObserver = this.mObserver;
        if (dataSetObserver == null) {
            this.mObserver = new PopupDataSetObserver();
        } else {
            ListAdapter listAdapter = this.mAdapter;
            if (listAdapter != null) {
                listAdapter.unregisterDataSetObserver(dataSetObserver);
            }
        }
        this.mAdapter = adapter;
        if (adapter != null) {
            adapter.registerDataSetObserver(this.mObserver);
        }
        DropDownListView dropDownListView = this.mDropDownList;
        if (dropDownListView != null) {
            dropDownListView.setAdapter(this.mAdapter);
        }
    }

    public void setAnchorView(View anchor) {
        this.mDropDownAnchorView = anchor;
    }

    public void setAnimationStyle(int animationStyle) {
        this.mPopup.setAnimationStyle(animationStyle);
    }

    public void setBackgroundDrawable(Drawable d) {
        this.mPopup.setBackgroundDrawable(d);
    }

    public void setContentWidth(int width) {
        Drawable background = this.mPopup.getBackground();
        if (background != null) {
            background.getPadding(this.mTempRect);
            this.mDropDownWidth = this.mTempRect.left + this.mTempRect.right + width;
            return;
        }
        setWidth(width);
    }

    public void setDropDownAlwaysVisible(boolean dropDownAlwaysVisible) {
        this.mDropDownAlwaysVisible = dropDownAlwaysVisible;
    }

    public void setDropDownGravity(int gravity) {
        this.mDropDownGravity = gravity;
    }

    public void setEpicenterBounds(Rect bounds) {
        this.mEpicenterBounds = bounds != null ? new Rect(bounds) : null;
    }

    public void setForceIgnoreOutsideTouch(boolean forceIgnoreOutsideTouch) {
        this.mForceIgnoreOutsideTouch = forceIgnoreOutsideTouch;
    }

    public void setHeight(int height) {
        if (height >= 0 || -2 == height || -1 == height) {
            this.mDropDownHeight = height;
            return;
        }
        throw new IllegalArgumentException("Invalid height. Must be a positive value, MATCH_PARENT, or WRAP_CONTENT.");
    }

    public void setHorizontalOffset(int offset) {
        this.mDropDownHorizontalOffset = offset;
    }

    public void setInputMethodMode(int mode) {
        this.mPopup.setInputMethodMode(mode);
    }

    /* access modifiers changed from: package-private */
    public void setListItemExpandMax(int max) {
        this.mListItemExpandMaximum = max;
    }

    public void setListSelector(Drawable selector) {
        this.mDropDownListHighlight = selector;
    }

    public void setModal(boolean modal) {
        this.mModal = modal;
        this.mPopup.setFocusable(modal);
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        this.mPopup.setOnDismissListener(listener);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener clickListener) {
        this.mItemClickListener = clickListener;
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener selectedListener) {
        this.mItemSelectedListener = selectedListener;
    }

    public void setOverlapAnchor(boolean overlapAnchor) {
        this.mOverlapAnchorSet = true;
        this.mOverlapAnchor = overlapAnchor;
    }

    public void setPromptPosition(int position) {
        this.mPromptPosition = position;
    }

    public void setPromptView(View prompt) {
        boolean isShowing = isShowing();
        if (isShowing) {
            removePromptView();
        }
        this.mPromptView = prompt;
        if (isShowing) {
            show();
        }
    }

    public void setSelection(int position) {
        DropDownListView dropDownListView = this.mDropDownList;
        if (isShowing() && dropDownListView != null) {
            dropDownListView.setListSelectionHidden(false);
            dropDownListView.setSelection(position);
            if (dropDownListView.getChoiceMode() != 0) {
                dropDownListView.setItemChecked(position, true);
            }
        }
    }

    public void setSoftInputMode(int mode) {
        this.mPopup.setSoftInputMode(mode);
    }

    public void setVerticalOffset(int offset) {
        this.mDropDownVerticalOffset = offset;
        this.mDropDownVerticalOffsetSet = true;
    }

    public void setWidth(int width) {
        this.mDropDownWidth = width;
    }

    public void setWindowLayoutType(int layoutType) {
        this.mDropDownWindowLayoutType = layoutType;
    }

    public void show() {
        int i;
        int buildDropDown = buildDropDown();
        boolean isInputMethodNotNeeded = isInputMethodNotNeeded();
        PopupWindowCompat.setWindowLayoutType(this.mPopup, this.mDropDownWindowLayoutType);
        boolean z = true;
        if (!this.mPopup.isShowing()) {
            int i2 = this.mDropDownWidth;
            int width = i2 == -1 ? -1 : i2 == -2 ? getAnchorView().getWidth() : this.mDropDownWidth;
            int i3 = this.mDropDownHeight;
            int i4 = i3 == -1 ? -1 : i3 == -2 ? buildDropDown : this.mDropDownHeight;
            this.mPopup.setWidth(width);
            this.mPopup.setHeight(i4);
            setPopupClipToScreenEnabled(true);
            this.mPopup.setOutsideTouchable(!this.mForceIgnoreOutsideTouch && !this.mDropDownAlwaysVisible);
            this.mPopup.setTouchInterceptor(this.mTouchInterceptor);
            if (this.mOverlapAnchorSet) {
                PopupWindowCompat.setOverlapAnchor(this.mPopup, this.mOverlapAnchor);
            }
            if (Build.VERSION.SDK_INT <= 28) {
                Method method = sSetEpicenterBoundsMethod;
                if (method != null) {
                    try {
                        method.invoke(this.mPopup, new Object[]{this.mEpicenterBounds});
                    } catch (Exception e) {
                        Log.e(TAG, "Could not invoke setEpicenterBounds on PopupWindow", e);
                    }
                }
            } else {
                Api29Impl.setEpicenterBounds(this.mPopup, this.mEpicenterBounds);
            }
            PopupWindowCompat.showAsDropDown(this.mPopup, getAnchorView(), this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset, this.mDropDownGravity);
            this.mDropDownList.setSelection(-1);
            if (!this.mModal || this.mDropDownList.isInTouchMode()) {
                clearListSelection();
            }
            if (!this.mModal) {
                this.mHandler.post(this.mHideSelector);
            }
        } else if (ViewCompat.isAttachedToWindow(getAnchorView())) {
            int i5 = this.mDropDownWidth;
            int width2 = i5 == -1 ? -1 : i5 == -2 ? getAnchorView().getWidth() : this.mDropDownWidth;
            int i6 = this.mDropDownHeight;
            if (i6 == -1) {
                i = isInputMethodNotNeeded ? buildDropDown : -1;
                if (isInputMethodNotNeeded) {
                    this.mPopup.setWidth(this.mDropDownWidth == -1 ? -1 : 0);
                    this.mPopup.setHeight(0);
                } else {
                    this.mPopup.setWidth(this.mDropDownWidth == -1 ? -1 : 0);
                    this.mPopup.setHeight(-1);
                }
            } else {
                i = i6 == -2 ? buildDropDown : this.mDropDownHeight;
            }
            PopupWindow popupWindow = this.mPopup;
            if (this.mForceIgnoreOutsideTouch || this.mDropDownAlwaysVisible) {
                z = false;
            }
            popupWindow.setOutsideTouchable(z);
            this.mPopup.update(getAnchorView(), this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset, width2 < 0 ? -1 : width2, i < 0 ? -1 : i);
        }
    }
}

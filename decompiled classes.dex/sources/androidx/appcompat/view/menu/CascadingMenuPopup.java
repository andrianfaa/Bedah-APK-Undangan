package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.widget.MenuItemHoverListener;
import androidx.appcompat.widget.MenuPopupWindow;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

final class CascadingMenuPopup extends MenuPopup implements MenuPresenter, View.OnKeyListener, PopupWindow.OnDismissListener {
    static final int HORIZ_POSITION_LEFT = 0;
    static final int HORIZ_POSITION_RIGHT = 1;
    private static final int ITEM_LAYOUT = R.layout.abc_cascading_menu_item_layout;
    static final int SUBMENU_TIMEOUT_MS = 200;
    private View mAnchorView;
    private final View.OnAttachStateChangeListener mAttachStateChangeListener = new View.OnAttachStateChangeListener() {
        public void onViewAttachedToWindow(View v) {
        }

        public void onViewDetachedFromWindow(View v) {
            if (CascadingMenuPopup.this.mTreeObserver != null) {
                if (!CascadingMenuPopup.this.mTreeObserver.isAlive()) {
                    CascadingMenuPopup.this.mTreeObserver = v.getViewTreeObserver();
                }
                CascadingMenuPopup.this.mTreeObserver.removeGlobalOnLayoutListener(CascadingMenuPopup.this.mGlobalLayoutListener);
            }
            v.removeOnAttachStateChangeListener(this);
        }
    };
    private final Context mContext;
    private int mDropDownGravity = 0;
    private boolean mForceShowIcon;
    final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            if (CascadingMenuPopup.this.isShowing() && CascadingMenuPopup.this.mShowingMenus.size() > 0 && !CascadingMenuPopup.this.mShowingMenus.get(0).window.isModal()) {
                View view = CascadingMenuPopup.this.mShownAnchorView;
                if (view == null || !view.isShown()) {
                    CascadingMenuPopup.this.dismiss();
                    return;
                }
                for (CascadingMenuInfo cascadingMenuInfo : CascadingMenuPopup.this.mShowingMenus) {
                    cascadingMenuInfo.window.show();
                }
            }
        }
    };
    private boolean mHasXOffset;
    private boolean mHasYOffset;
    private int mLastPosition;
    private final MenuItemHoverListener mMenuItemHoverListener = new MenuItemHoverListener() {
        public void onItemHoverEnter(final MenuBuilder menu, final MenuItem item) {
            CascadingMenuPopup.this.mSubMenuHoverHandler.removeCallbacksAndMessages((Object) null);
            int i = -1;
            int i2 = 0;
            int size = CascadingMenuPopup.this.mShowingMenus.size();
            while (true) {
                if (i2 >= size) {
                    break;
                } else if (menu == CascadingMenuPopup.this.mShowingMenus.get(i2).menu) {
                    i = i2;
                    break;
                } else {
                    i2++;
                }
            }
            if (i != -1) {
                int i3 = i + 1;
                final CascadingMenuInfo cascadingMenuInfo = i3 < CascadingMenuPopup.this.mShowingMenus.size() ? CascadingMenuPopup.this.mShowingMenus.get(i3) : null;
                CascadingMenuPopup.this.mSubMenuHoverHandler.postAtTime(new Runnable() {
                    public void run() {
                        if (cascadingMenuInfo != null) {
                            CascadingMenuPopup.this.mShouldCloseImmediately = true;
                            cascadingMenuInfo.menu.close(false);
                            CascadingMenuPopup.this.mShouldCloseImmediately = false;
                        }
                        if (item.isEnabled() && item.hasSubMenu()) {
                            menu.performItemAction(item, 4);
                        }
                    }
                }, menu, SystemClock.uptimeMillis() + 200);
            }
        }

        public void onItemHoverExit(MenuBuilder menu, MenuItem item) {
            CascadingMenuPopup.this.mSubMenuHoverHandler.removeCallbacksAndMessages(menu);
        }
    };
    private final int mMenuMaxWidth;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private final boolean mOverflowOnly;
    private final List<MenuBuilder> mPendingMenus = new ArrayList();
    private final int mPopupStyleAttr;
    private final int mPopupStyleRes;
    private MenuPresenter.Callback mPresenterCallback;
    private int mRawDropDownGravity = 0;
    boolean mShouldCloseImmediately;
    private boolean mShowTitle;
    final List<CascadingMenuInfo> mShowingMenus = new ArrayList();
    View mShownAnchorView;
    final Handler mSubMenuHoverHandler;
    ViewTreeObserver mTreeObserver;
    private int mXOffset;
    private int mYOffset;

    private static class CascadingMenuInfo {
        public final MenuBuilder menu;
        public final int position;
        public final MenuPopupWindow window;

        public CascadingMenuInfo(MenuPopupWindow window2, MenuBuilder menu2, int position2) {
            this.window = window2;
            this.menu = menu2;
            this.position = position2;
        }

        public ListView getListView() {
            return this.window.getListView();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface HorizPosition {
    }

    public CascadingMenuPopup(Context context, View anchor, int popupStyleAttr, int popupStyleRes, boolean overflowOnly) {
        this.mContext = context;
        this.mAnchorView = anchor;
        this.mPopupStyleAttr = popupStyleAttr;
        this.mPopupStyleRes = popupStyleRes;
        this.mOverflowOnly = overflowOnly;
        this.mForceShowIcon = false;
        this.mLastPosition = getInitialMenuPosition();
        Resources resources = context.getResources();
        this.mMenuMaxWidth = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(R.dimen.abc_config_prefDialogWidth));
        this.mSubMenuHoverHandler = new Handler();
    }

    private MenuPopupWindow createPopupWindow() {
        MenuPopupWindow menuPopupWindow = new MenuPopupWindow(this.mContext, (AttributeSet) null, this.mPopupStyleAttr, this.mPopupStyleRes);
        menuPopupWindow.setHoverListener(this.mMenuItemHoverListener);
        menuPopupWindow.setOnItemClickListener(this);
        menuPopupWindow.setOnDismissListener(this);
        menuPopupWindow.setAnchorView(this.mAnchorView);
        menuPopupWindow.setDropDownGravity(this.mDropDownGravity);
        menuPopupWindow.setModal(true);
        menuPopupWindow.setInputMethodMode(2);
        return menuPopupWindow;
    }

    private int findIndexOfAddedMenu(MenuBuilder menu) {
        int size = this.mShowingMenus.size();
        for (int i = 0; i < size; i++) {
            if (menu == this.mShowingMenus.get(i).menu) {
                return i;
            }
        }
        return -1;
    }

    private MenuItem findMenuItemForSubmenu(MenuBuilder parent, MenuBuilder submenu) {
        int size = parent.size();
        for (int i = 0; i < size; i++) {
            MenuItem item = parent.getItem(i);
            if (item.hasSubMenu() && submenu == item.getSubMenu()) {
                return item;
            }
        }
        return null;
    }

    /* JADX WARNING: type inference failed for: r6v4, types: [android.widget.ListAdapter] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.view.View findParentViewForSubmenu(androidx.appcompat.view.menu.CascadingMenuPopup.CascadingMenuInfo r11, androidx.appcompat.view.menu.MenuBuilder r12) {
        /*
            r10 = this;
            androidx.appcompat.view.menu.MenuBuilder r0 = r11.menu
            android.view.MenuItem r0 = r10.findMenuItemForSubmenu(r0, r12)
            r1 = 0
            if (r0 != 0) goto L_0x000a
            return r1
        L_0x000a:
            android.widget.ListView r2 = r11.getListView()
            android.widget.ListAdapter r3 = r2.getAdapter()
            boolean r4 = r3 instanceof android.widget.HeaderViewListAdapter
            if (r4 == 0) goto L_0x0025
            r4 = r3
            android.widget.HeaderViewListAdapter r4 = (android.widget.HeaderViewListAdapter) r4
            int r5 = r4.getHeadersCount()
            android.widget.ListAdapter r6 = r4.getWrappedAdapter()
            r4 = r6
            androidx.appcompat.view.menu.MenuAdapter r4 = (androidx.appcompat.view.menu.MenuAdapter) r4
            goto L_0x0029
        L_0x0025:
            r5 = 0
            r4 = r3
            androidx.appcompat.view.menu.MenuAdapter r4 = (androidx.appcompat.view.menu.MenuAdapter) r4
        L_0x0029:
            r6 = -1
            r7 = 0
            int r8 = r4.getCount()
        L_0x002f:
            if (r7 >= r8) goto L_0x003c
            androidx.appcompat.view.menu.MenuItemImpl r9 = r4.getItem((int) r7)
            if (r0 != r9) goto L_0x0039
            r6 = r7
            goto L_0x003c
        L_0x0039:
            int r7 = r7 + 1
            goto L_0x002f
        L_0x003c:
            r7 = -1
            if (r6 != r7) goto L_0x0040
            return r1
        L_0x0040:
            int r6 = r6 + r5
            int r7 = r2.getFirstVisiblePosition()
            int r7 = r6 - r7
            if (r7 < 0) goto L_0x0055
            int r8 = r2.getChildCount()
            if (r7 < r8) goto L_0x0050
            goto L_0x0055
        L_0x0050:
            android.view.View r1 = r2.getChildAt(r7)
            return r1
        L_0x0055:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.view.menu.CascadingMenuPopup.findParentViewForSubmenu(androidx.appcompat.view.menu.CascadingMenuPopup$CascadingMenuInfo, androidx.appcompat.view.menu.MenuBuilder):android.view.View");
    }

    private int getInitialMenuPosition() {
        return ViewCompat.getLayoutDirection(this.mAnchorView) == 1 ? 0 : 1;
    }

    private int getNextMenuPosition(int nextMenuWidth) {
        List<CascadingMenuInfo> list = this.mShowingMenus;
        ListView listView = list.get(list.size() - 1).getListView();
        int[] iArr = new int[2];
        listView.getLocationOnScreen(iArr);
        Rect rect = new Rect();
        this.mShownAnchorView.getWindowVisibleDisplayFrame(rect);
        return this.mLastPosition == 1 ? (iArr[0] + listView.getWidth()) + nextMenuWidth > rect.right ? 0 : 1 : iArr[0] - nextMenuWidth < 0 ? 1 : 0;
    }

    private void showMenu(MenuBuilder menu) {
        View view;
        CascadingMenuInfo cascadingMenuInfo;
        int i;
        int i2;
        MenuBuilder menuBuilder = menu;
        LayoutInflater from = LayoutInflater.from(this.mContext);
        MenuAdapter menuAdapter = new MenuAdapter(menuBuilder, from, this.mOverflowOnly, ITEM_LAYOUT);
        if (!isShowing() && this.mForceShowIcon) {
            menuAdapter.setForceShowIcon(true);
        } else if (isShowing()) {
            menuAdapter.setForceShowIcon(MenuPopup.shouldPreserveIconSpacing(menu));
        }
        int measureIndividualMenuWidth = measureIndividualMenuWidth(menuAdapter, (ViewGroup) null, this.mContext, this.mMenuMaxWidth);
        MenuPopupWindow createPopupWindow = createPopupWindow();
        createPopupWindow.setAdapter(menuAdapter);
        createPopupWindow.setContentWidth(measureIndividualMenuWidth);
        createPopupWindow.setDropDownGravity(this.mDropDownGravity);
        if (this.mShowingMenus.size() > 0) {
            List<CascadingMenuInfo> list = this.mShowingMenus;
            cascadingMenuInfo = list.get(list.size() - 1);
            view = findParentViewForSubmenu(cascadingMenuInfo, menuBuilder);
        } else {
            cascadingMenuInfo = null;
            view = null;
        }
        if (view != null) {
            createPopupWindow.setTouchModal(false);
            createPopupWindow.setEnterTransition((Object) null);
            int nextMenuPosition = getNextMenuPosition(measureIndividualMenuWidth);
            boolean z = nextMenuPosition == 1;
            this.mLastPosition = nextMenuPosition;
            if (Build.VERSION.SDK_INT >= 26) {
                createPopupWindow.setAnchorView(view);
                i2 = 0;
                i = 0;
            } else {
                int[] iArr = new int[2];
                this.mAnchorView.getLocationOnScreen(iArr);
                int[] iArr2 = new int[2];
                view.getLocationOnScreen(iArr2);
                if ((this.mDropDownGravity & 7) == 5) {
                    iArr[0] = iArr[0] + this.mAnchorView.getWidth();
                    iArr2[0] = iArr2[0] + view.getWidth();
                }
                i2 = iArr2[0] - iArr[0];
                i = iArr2[1] - iArr[1];
            }
            createPopupWindow.setHorizontalOffset((this.mDropDownGravity & 5) == 5 ? z ? i2 + measureIndividualMenuWidth : i2 - view.getWidth() : z ? view.getWidth() + i2 : i2 - measureIndividualMenuWidth);
            createPopupWindow.setOverlapAnchor(true);
            createPopupWindow.setVerticalOffset(i);
        } else {
            if (this.mHasXOffset) {
                createPopupWindow.setHorizontalOffset(this.mXOffset);
            }
            if (this.mHasYOffset) {
                createPopupWindow.setVerticalOffset(this.mYOffset);
            }
            createPopupWindow.setEpicenterBounds(getEpicenterBounds());
        }
        this.mShowingMenus.add(new CascadingMenuInfo(createPopupWindow, menuBuilder, this.mLastPosition));
        createPopupWindow.show();
        ListView listView = createPopupWindow.getListView();
        listView.setOnKeyListener(this);
        if (cascadingMenuInfo == null && this.mShowTitle && menu.getHeaderTitle() != null) {
            FrameLayout frameLayout = (FrameLayout) from.inflate(R.layout.abc_popup_menu_header_item_layout, listView, false);
            frameLayout.setEnabled(false);
            ((TextView) frameLayout.findViewById(16908310)).setText(menu.getHeaderTitle());
            listView.addHeaderView(frameLayout, (Object) null, false);
            createPopupWindow.show();
        }
    }

    public void addMenu(MenuBuilder menu) {
        menu.addMenuPresenter(this, this.mContext);
        if (isShowing()) {
            showMenu(menu);
        } else {
            this.mPendingMenus.add(menu);
        }
    }

    /* access modifiers changed from: protected */
    public boolean closeMenuOnSubMenuOpened() {
        return false;
    }

    public void dismiss() {
        int size = this.mShowingMenus.size();
        if (size > 0) {
            CascadingMenuInfo[] cascadingMenuInfoArr = (CascadingMenuInfo[]) this.mShowingMenus.toArray(new CascadingMenuInfo[size]);
            for (int i = size - 1; i >= 0; i--) {
                CascadingMenuInfo cascadingMenuInfo = cascadingMenuInfoArr[i];
                if (cascadingMenuInfo.window.isShowing()) {
                    cascadingMenuInfo.window.dismiss();
                }
            }
        }
    }

    public boolean flagActionItems() {
        return false;
    }

    public ListView getListView() {
        if (this.mShowingMenus.isEmpty()) {
            return null;
        }
        List<CascadingMenuInfo> list = this.mShowingMenus;
        return list.get(list.size() - 1).getListView();
    }

    public boolean isShowing() {
        return this.mShowingMenus.size() > 0 && this.mShowingMenus.get(0).window.isShowing();
    }

    public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        int findIndexOfAddedMenu = findIndexOfAddedMenu(menu);
        if (findIndexOfAddedMenu >= 0) {
            int i = findIndexOfAddedMenu + 1;
            if (i < this.mShowingMenus.size()) {
                this.mShowingMenus.get(i).menu.close(false);
            }
            CascadingMenuInfo remove = this.mShowingMenus.remove(findIndexOfAddedMenu);
            remove.menu.removeMenuPresenter(this);
            if (this.mShouldCloseImmediately) {
                remove.window.setExitTransition((Object) null);
                remove.window.setAnimationStyle(0);
            }
            remove.window.dismiss();
            int size = this.mShowingMenus.size();
            if (size > 0) {
                this.mLastPosition = this.mShowingMenus.get(size - 1).position;
            } else {
                this.mLastPosition = getInitialMenuPosition();
            }
            if (size == 0) {
                dismiss();
                MenuPresenter.Callback callback = this.mPresenterCallback;
                if (callback != null) {
                    callback.onCloseMenu(menu, true);
                }
                ViewTreeObserver viewTreeObserver = this.mTreeObserver;
                if (viewTreeObserver != null) {
                    if (viewTreeObserver.isAlive()) {
                        this.mTreeObserver.removeGlobalOnLayoutListener(this.mGlobalLayoutListener);
                    }
                    this.mTreeObserver = null;
                }
                this.mShownAnchorView.removeOnAttachStateChangeListener(this.mAttachStateChangeListener);
                this.mOnDismissListener.onDismiss();
            } else if (allMenusAreClosing) {
                this.mShowingMenus.get(0).menu.close(false);
            }
        }
    }

    public void onDismiss() {
        CascadingMenuInfo cascadingMenuInfo = null;
        int i = 0;
        int size = this.mShowingMenus.size();
        while (true) {
            if (i >= size) {
                break;
            }
            CascadingMenuInfo cascadingMenuInfo2 = this.mShowingMenus.get(i);
            if (!cascadingMenuInfo2.window.isShowing()) {
                cascadingMenuInfo = cascadingMenuInfo2;
                break;
            }
            i++;
        }
        if (cascadingMenuInfo != null) {
            cascadingMenuInfo.menu.close(false);
        }
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() != 1 || keyCode != 82) {
            return false;
        }
        dismiss();
        return true;
    }

    public void onRestoreInstanceState(Parcelable state) {
    }

    public Parcelable onSaveInstanceState() {
        return null;
    }

    public boolean onSubMenuSelected(SubMenuBuilder subMenu) {
        for (CascadingMenuInfo next : this.mShowingMenus) {
            if (subMenu == next.menu) {
                next.getListView().requestFocus();
                return true;
            }
        }
        if (!subMenu.hasVisibleItems()) {
            return false;
        }
        addMenu(subMenu);
        MenuPresenter.Callback callback = this.mPresenterCallback;
        if (callback != null) {
            callback.onOpenSubMenu(subMenu);
        }
        return true;
    }

    public void setAnchorView(View anchor) {
        if (this.mAnchorView != anchor) {
            this.mAnchorView = anchor;
            this.mDropDownGravity = GravityCompat.getAbsoluteGravity(this.mRawDropDownGravity, ViewCompat.getLayoutDirection(anchor));
        }
    }

    public void setCallback(MenuPresenter.Callback cb) {
        this.mPresenterCallback = cb;
    }

    public void setForceShowIcon(boolean forceShow) {
        this.mForceShowIcon = forceShow;
    }

    public void setGravity(int dropDownGravity) {
        if (this.mRawDropDownGravity != dropDownGravity) {
            this.mRawDropDownGravity = dropDownGravity;
            this.mDropDownGravity = GravityCompat.getAbsoluteGravity(dropDownGravity, ViewCompat.getLayoutDirection(this.mAnchorView));
        }
    }

    public void setHorizontalOffset(int x) {
        this.mHasXOffset = true;
        this.mXOffset = x;
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        this.mOnDismissListener = listener;
    }

    public void setShowTitle(boolean showTitle) {
        this.mShowTitle = showTitle;
    }

    public void setVerticalOffset(int y) {
        this.mHasYOffset = true;
        this.mYOffset = y;
    }

    public void show() {
        if (!isShowing()) {
            for (MenuBuilder showMenu : this.mPendingMenus) {
                showMenu(showMenu);
            }
            this.mPendingMenus.clear();
            View view = this.mAnchorView;
            this.mShownAnchorView = view;
            if (view != null) {
                boolean z = this.mTreeObserver == null;
                ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
                this.mTreeObserver = viewTreeObserver;
                if (z) {
                    viewTreeObserver.addOnGlobalLayoutListener(this.mGlobalLayoutListener);
                }
                this.mShownAnchorView.addOnAttachStateChangeListener(this.mAttachStateChangeListener);
            }
        }
    }

    public void updateMenuView(boolean cleared) {
        for (CascadingMenuInfo listView : this.mShowingMenus) {
            toMenuAdapter(listView.getListView().getAdapter()).notifyDataSetChanged();
        }
    }
}

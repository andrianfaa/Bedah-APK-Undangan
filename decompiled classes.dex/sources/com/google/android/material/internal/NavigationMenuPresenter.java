package com.google.android.material.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import com.google.android.material.R;
import java.util.ArrayList;

public class NavigationMenuPresenter implements MenuPresenter {
    public static final int NO_TEXT_APPEARANCE_SET = 0;
    private static final String STATE_ADAPTER = "android:menu:adapter";
    private static final String STATE_HEADER = "android:menu:header";
    private static final String STATE_HIERARCHY = "android:menu:list";
    NavigationMenuAdapter adapter;
    private MenuPresenter.Callback callback;
    int dividerInsetEnd;
    int dividerInsetStart;
    boolean hasCustomItemIconSize;
    LinearLayout headerLayout;
    ColorStateList iconTintList;
    private int id;
    boolean isBehindStatusBar = true;
    Drawable itemBackground;
    RippleDrawable itemForeground;
    int itemHorizontalPadding;
    int itemIconPadding;
    int itemIconSize;
    /* access modifiers changed from: private */
    public int itemMaxLines;
    int itemVerticalPadding;
    LayoutInflater layoutInflater;
    MenuBuilder menu;
    private NavigationMenuView menuView;
    final View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            NavigationMenuPresenter.this.setUpdateSuspended(true);
            MenuItemImpl itemData = ((NavigationMenuItemView) view).getItemData();
            boolean performItemAction = NavigationMenuPresenter.this.menu.performItemAction(itemData, NavigationMenuPresenter.this, 0);
            boolean z = false;
            if (itemData != null && itemData.isCheckable() && performItemAction) {
                NavigationMenuPresenter.this.adapter.setCheckedItem(itemData);
                z = true;
            }
            NavigationMenuPresenter.this.setUpdateSuspended(false);
            if (z) {
                NavigationMenuPresenter.this.updateMenuView(false);
            }
        }
    };
    private int overScrollMode = -1;
    int paddingSeparator;
    private int paddingTopDefault;
    ColorStateList subheaderColor;
    int subheaderInsetEnd;
    int subheaderInsetStart;
    int subheaderTextAppearance = 0;
    int textAppearance = 0;
    ColorStateList textColor;

    private static class HeaderViewHolder extends ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class NavigationMenuAdapter extends RecyclerView.Adapter<ViewHolder> {
        private static final String STATE_ACTION_VIEWS = "android:menu:action_views";
        private static final String STATE_CHECKED_ITEM = "android:menu:checked";
        private static final int VIEW_TYPE_HEADER = 3;
        private static final int VIEW_TYPE_NORMAL = 0;
        private static final int VIEW_TYPE_SEPARATOR = 2;
        private static final int VIEW_TYPE_SUBHEADER = 1;
        private MenuItemImpl checkedItem;
        private final ArrayList<NavigationMenuItem> items = new ArrayList<>();
        private boolean updateSuspended;

        NavigationMenuAdapter() {
            prepareMenuItems();
        }

        private void appendTransparentIconIfMissing(int startIndex, int endIndex) {
            for (int i = startIndex; i < endIndex; i++) {
                ((NavigationMenuTextItem) this.items.get(i)).needsEmptyIcon = true;
            }
        }

        private void prepareMenuItems() {
            if (!this.updateSuspended) {
                this.updateSuspended = true;
                this.items.clear();
                this.items.add(new NavigationMenuHeaderItem());
                int i = -1;
                int i2 = 0;
                boolean z = false;
                int i3 = 0;
                int size = NavigationMenuPresenter.this.menu.getVisibleItems().size();
                while (true) {
                    boolean z2 = false;
                    if (i3 < size) {
                        MenuItemImpl menuItemImpl = NavigationMenuPresenter.this.menu.getVisibleItems().get(i3);
                        if (menuItemImpl.isChecked()) {
                            setCheckedItem(menuItemImpl);
                        }
                        if (menuItemImpl.isCheckable()) {
                            menuItemImpl.setExclusiveCheckable(false);
                        }
                        if (menuItemImpl.hasSubMenu()) {
                            SubMenu subMenu = menuItemImpl.getSubMenu();
                            if (subMenu.hasVisibleItems()) {
                                if (i3 != 0) {
                                    this.items.add(new NavigationMenuSeparatorItem(NavigationMenuPresenter.this.paddingSeparator, 0));
                                }
                                this.items.add(new NavigationMenuTextItem(menuItemImpl));
                                boolean z3 = false;
                                int size2 = this.items.size();
                                int size3 = subMenu.size();
                                for (int i4 = 0; i4 < size3; i4++) {
                                    MenuItemImpl menuItemImpl2 = (MenuItemImpl) subMenu.getItem(i4);
                                    if (menuItemImpl2.isVisible()) {
                                        if (!z3 && menuItemImpl2.getIcon() != null) {
                                            z3 = true;
                                        }
                                        if (menuItemImpl2.isCheckable()) {
                                            menuItemImpl2.setExclusiveCheckable(false);
                                        }
                                        if (menuItemImpl.isChecked()) {
                                            setCheckedItem(menuItemImpl);
                                        }
                                        this.items.add(new NavigationMenuTextItem(menuItemImpl2));
                                    }
                                }
                                if (z3) {
                                    appendTransparentIconIfMissing(size2, this.items.size());
                                }
                            }
                        } else {
                            int groupId = menuItemImpl.getGroupId();
                            if (groupId != i) {
                                i2 = this.items.size();
                                if (menuItemImpl.getIcon() != null) {
                                    z2 = true;
                                }
                                z = z2;
                                if (i3 != 0) {
                                    i2++;
                                    this.items.add(new NavigationMenuSeparatorItem(NavigationMenuPresenter.this.paddingSeparator, NavigationMenuPresenter.this.paddingSeparator));
                                }
                            } else if (!z && menuItemImpl.getIcon() != null) {
                                z = true;
                                appendTransparentIconIfMissing(i2, this.items.size());
                            }
                            NavigationMenuTextItem navigationMenuTextItem = new NavigationMenuTextItem(menuItemImpl);
                            navigationMenuTextItem.needsEmptyIcon = z;
                            this.items.add(navigationMenuTextItem);
                            i = groupId;
                        }
                        i3++;
                    } else {
                        this.updateSuspended = false;
                        return;
                    }
                }
            }
        }

        public Bundle createInstanceState() {
            Bundle bundle = new Bundle();
            MenuItemImpl menuItemImpl = this.checkedItem;
            if (menuItemImpl != null) {
                bundle.putInt(STATE_CHECKED_ITEM, menuItemImpl.getItemId());
            }
            SparseArray sparseArray = new SparseArray();
            int size = this.items.size();
            for (int i = 0; i < size; i++) {
                NavigationMenuItem navigationMenuItem = this.items.get(i);
                if (navigationMenuItem instanceof NavigationMenuTextItem) {
                    MenuItemImpl menuItem = ((NavigationMenuTextItem) navigationMenuItem).getMenuItem();
                    View actionView = menuItem != null ? menuItem.getActionView() : null;
                    if (actionView != null) {
                        ParcelableSparseArray parcelableSparseArray = new ParcelableSparseArray();
                        actionView.saveHierarchyState(parcelableSparseArray);
                        sparseArray.put(menuItem.getItemId(), parcelableSparseArray);
                    }
                }
            }
            bundle.putSparseParcelableArray(STATE_ACTION_VIEWS, sparseArray);
            return bundle;
        }

        public MenuItemImpl getCheckedItem() {
            return this.checkedItem;
        }

        public int getItemCount() {
            return this.items.size();
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public int getItemViewType(int position) {
            NavigationMenuItem navigationMenuItem = this.items.get(position);
            if (navigationMenuItem instanceof NavigationMenuSeparatorItem) {
                return 2;
            }
            if (navigationMenuItem instanceof NavigationMenuHeaderItem) {
                return 3;
            }
            if (navigationMenuItem instanceof NavigationMenuTextItem) {
                return ((NavigationMenuTextItem) navigationMenuItem).getMenuItem().hasSubMenu() ? 1 : 0;
            }
            throw new RuntimeException("Unknown item type.");
        }

        /* access modifiers changed from: package-private */
        public int getRowCount() {
            int i = NavigationMenuPresenter.this.headerLayout.getChildCount() == 0 ? 0 : 1;
            for (int i2 = 0; i2 < NavigationMenuPresenter.this.adapter.getItemCount(); i2++) {
                if (NavigationMenuPresenter.this.adapter.getItemViewType(i2) == 0) {
                    i++;
                }
            }
            return i;
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case 0:
                    NavigationMenuItemView navigationMenuItemView = (NavigationMenuItemView) holder.itemView;
                    navigationMenuItemView.setIconTintList(NavigationMenuPresenter.this.iconTintList);
                    if (NavigationMenuPresenter.this.textAppearance != 0) {
                        navigationMenuItemView.setTextAppearance(NavigationMenuPresenter.this.textAppearance);
                    }
                    if (NavigationMenuPresenter.this.textColor != null) {
                        navigationMenuItemView.setTextColor(NavigationMenuPresenter.this.textColor);
                    }
                    ViewCompat.setBackground(navigationMenuItemView, NavigationMenuPresenter.this.itemBackground != null ? NavigationMenuPresenter.this.itemBackground.getConstantState().newDrawable() : null);
                    if (NavigationMenuPresenter.this.itemForeground != null) {
                        navigationMenuItemView.setForeground(NavigationMenuPresenter.this.itemForeground.getConstantState().newDrawable());
                    }
                    NavigationMenuTextItem navigationMenuTextItem = (NavigationMenuTextItem) this.items.get(position);
                    navigationMenuItemView.setNeedsEmptyIcon(navigationMenuTextItem.needsEmptyIcon);
                    navigationMenuItemView.setPadding(NavigationMenuPresenter.this.itemHorizontalPadding, NavigationMenuPresenter.this.itemVerticalPadding, NavigationMenuPresenter.this.itemHorizontalPadding, NavigationMenuPresenter.this.itemVerticalPadding);
                    navigationMenuItemView.setIconPadding(NavigationMenuPresenter.this.itemIconPadding);
                    if (NavigationMenuPresenter.this.hasCustomItemIconSize) {
                        navigationMenuItemView.setIconSize(NavigationMenuPresenter.this.itemIconSize);
                    }
                    navigationMenuItemView.setMaxLines(NavigationMenuPresenter.this.itemMaxLines);
                    navigationMenuItemView.initialize(navigationMenuTextItem.getMenuItem(), 0);
                    return;
                case 1:
                    TextView textView = (TextView) holder.itemView;
                    textView.setText(((NavigationMenuTextItem) this.items.get(position)).getMenuItem().getTitle());
                    if (NavigationMenuPresenter.this.subheaderTextAppearance != 0) {
                        TextViewCompat.setTextAppearance(textView, NavigationMenuPresenter.this.subheaderTextAppearance);
                    }
                    textView.setPadding(NavigationMenuPresenter.this.subheaderInsetStart, textView.getPaddingTop(), NavigationMenuPresenter.this.subheaderInsetEnd, textView.getPaddingBottom());
                    if (NavigationMenuPresenter.this.subheaderColor != null) {
                        textView.setTextColor(NavigationMenuPresenter.this.subheaderColor);
                        return;
                    }
                    return;
                case 2:
                    NavigationMenuSeparatorItem navigationMenuSeparatorItem = (NavigationMenuSeparatorItem) this.items.get(position);
                    holder.itemView.setPadding(NavigationMenuPresenter.this.dividerInsetStart, navigationMenuSeparatorItem.getPaddingTop(), NavigationMenuPresenter.this.dividerInsetEnd, navigationMenuSeparatorItem.getPaddingBottom());
                    return;
                default:
                    return;
            }
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0:
                    return new NormalViewHolder(NavigationMenuPresenter.this.layoutInflater, parent, NavigationMenuPresenter.this.onClickListener);
                case 1:
                    return new SubheaderViewHolder(NavigationMenuPresenter.this.layoutInflater, parent);
                case 2:
                    return new SeparatorViewHolder(NavigationMenuPresenter.this.layoutInflater, parent);
                case 3:
                    return new HeaderViewHolder(NavigationMenuPresenter.this.headerLayout);
                default:
                    return null;
            }
        }

        public void onViewRecycled(ViewHolder holder) {
            if (holder instanceof NormalViewHolder) {
                ((NavigationMenuItemView) holder.itemView).recycle();
            }
        }

        public void restoreInstanceState(Bundle state) {
            MenuItemImpl menuItem;
            View actionView;
            ParcelableSparseArray parcelableSparseArray;
            MenuItemImpl menuItem2;
            int i = state.getInt(STATE_CHECKED_ITEM, 0);
            if (i != 0) {
                this.updateSuspended = true;
                int i2 = 0;
                int size = this.items.size();
                while (true) {
                    if (i2 >= size) {
                        break;
                    }
                    NavigationMenuItem navigationMenuItem = this.items.get(i2);
                    if ((navigationMenuItem instanceof NavigationMenuTextItem) && (menuItem2 = ((NavigationMenuTextItem) navigationMenuItem).getMenuItem()) != null && menuItem2.getItemId() == i) {
                        setCheckedItem(menuItem2);
                        break;
                    }
                    i2++;
                }
                this.updateSuspended = false;
                prepareMenuItems();
            }
            SparseArray sparseParcelableArray = state.getSparseParcelableArray(STATE_ACTION_VIEWS);
            if (sparseParcelableArray != null) {
                int size2 = this.items.size();
                for (int i3 = 0; i3 < size2; i3++) {
                    NavigationMenuItem navigationMenuItem2 = this.items.get(i3);
                    if (!(!(navigationMenuItem2 instanceof NavigationMenuTextItem) || (menuItem = ((NavigationMenuTextItem) navigationMenuItem2).getMenuItem()) == null || (actionView = menuItem.getActionView()) == null || (parcelableSparseArray = (ParcelableSparseArray) sparseParcelableArray.get(menuItem.getItemId())) == null)) {
                        actionView.restoreHierarchyState(parcelableSparseArray);
                    }
                }
            }
        }

        public void setCheckedItem(MenuItemImpl checkedItem2) {
            if (this.checkedItem != checkedItem2 && checkedItem2.isCheckable()) {
                MenuItemImpl menuItemImpl = this.checkedItem;
                if (menuItemImpl != null) {
                    menuItemImpl.setChecked(false);
                }
                this.checkedItem = checkedItem2;
                checkedItem2.setChecked(true);
            }
        }

        public void setUpdateSuspended(boolean updateSuspended2) {
            this.updateSuspended = updateSuspended2;
        }

        public void update() {
            prepareMenuItems();
            notifyDataSetChanged();
        }
    }

    private static class NavigationMenuHeaderItem implements NavigationMenuItem {
        NavigationMenuHeaderItem() {
        }
    }

    private interface NavigationMenuItem {
    }

    private static class NavigationMenuSeparatorItem implements NavigationMenuItem {
        private final int paddingBottom;
        private final int paddingTop;

        public NavigationMenuSeparatorItem(int paddingTop2, int paddingBottom2) {
            this.paddingTop = paddingTop2;
            this.paddingBottom = paddingBottom2;
        }

        public int getPaddingBottom() {
            return this.paddingBottom;
        }

        public int getPaddingTop() {
            return this.paddingTop;
        }
    }

    private static class NavigationMenuTextItem implements NavigationMenuItem {
        private final MenuItemImpl menuItem;
        boolean needsEmptyIcon;

        NavigationMenuTextItem(MenuItemImpl item) {
            this.menuItem = item;
        }

        public MenuItemImpl getMenuItem() {
            return this.menuItem;
        }
    }

    private class NavigationMenuViewAccessibilityDelegate extends RecyclerViewAccessibilityDelegate {
        NavigationMenuViewAccessibilityDelegate(RecyclerView recyclerView) {
            super(recyclerView);
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            info.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(NavigationMenuPresenter.this.adapter.getRowCount(), 0, false));
        }
    }

    private static class NormalViewHolder extends ViewHolder {
        public NormalViewHolder(LayoutInflater inflater, ViewGroup parent, View.OnClickListener listener) {
            super(inflater.inflate(R.layout.design_navigation_item, parent, false));
            this.itemView.setOnClickListener(listener);
        }
    }

    private static class SeparatorViewHolder extends ViewHolder {
        public SeparatorViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.design_navigation_item_separator, parent, false));
        }
    }

    private static class SubheaderViewHolder extends ViewHolder {
        public SubheaderViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.design_navigation_item_subheader, parent, false));
        }
    }

    private static abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private void updateTopPadding() {
        int i = 0;
        if (this.headerLayout.getChildCount() == 0 && this.isBehindStatusBar) {
            i = this.paddingTopDefault;
        }
        NavigationMenuView navigationMenuView = this.menuView;
        navigationMenuView.setPadding(0, i, 0, navigationMenuView.getPaddingBottom());
    }

    public void addHeaderView(View view) {
        this.headerLayout.addView(view);
        NavigationMenuView navigationMenuView = this.menuView;
        navigationMenuView.setPadding(0, 0, 0, navigationMenuView.getPaddingBottom());
    }

    public boolean collapseItemActionView(MenuBuilder menu2, MenuItemImpl item) {
        return false;
    }

    public void dispatchApplyWindowInsets(WindowInsetsCompat insets) {
        int systemWindowInsetTop = insets.getSystemWindowInsetTop();
        if (this.paddingTopDefault != systemWindowInsetTop) {
            this.paddingTopDefault = systemWindowInsetTop;
            updateTopPadding();
        }
        NavigationMenuView navigationMenuView = this.menuView;
        navigationMenuView.setPadding(0, navigationMenuView.getPaddingTop(), 0, insets.getSystemWindowInsetBottom());
        ViewCompat.dispatchApplyWindowInsets(this.headerLayout, insets);
    }

    public boolean expandItemActionView(MenuBuilder menu2, MenuItemImpl item) {
        return false;
    }

    public boolean flagActionItems() {
        return false;
    }

    public MenuItemImpl getCheckedItem() {
        return this.adapter.getCheckedItem();
    }

    public int getDividerInsetEnd() {
        return this.dividerInsetEnd;
    }

    public int getDividerInsetStart() {
        return this.dividerInsetStart;
    }

    public int getHeaderCount() {
        return this.headerLayout.getChildCount();
    }

    public View getHeaderView(int index) {
        return this.headerLayout.getChildAt(index);
    }

    public int getId() {
        return this.id;
    }

    public Drawable getItemBackground() {
        return this.itemBackground;
    }

    public int getItemHorizontalPadding() {
        return this.itemHorizontalPadding;
    }

    public int getItemIconPadding() {
        return this.itemIconPadding;
    }

    public int getItemMaxLines() {
        return this.itemMaxLines;
    }

    public ColorStateList getItemTextColor() {
        return this.textColor;
    }

    public ColorStateList getItemTintList() {
        return this.iconTintList;
    }

    public int getItemVerticalPadding() {
        return this.itemVerticalPadding;
    }

    public MenuView getMenuView(ViewGroup root) {
        if (this.menuView == null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) this.layoutInflater.inflate(R.layout.design_navigation_menu, root, false);
            this.menuView = navigationMenuView;
            navigationMenuView.setAccessibilityDelegateCompat(new NavigationMenuViewAccessibilityDelegate(this.menuView));
            if (this.adapter == null) {
                this.adapter = new NavigationMenuAdapter();
            }
            int i = this.overScrollMode;
            if (i != -1) {
                this.menuView.setOverScrollMode(i);
            }
            this.headerLayout = (LinearLayout) this.layoutInflater.inflate(R.layout.design_navigation_item_header, this.menuView, false);
            this.menuView.setAdapter(this.adapter);
        }
        return this.menuView;
    }

    public int getSubheaderInsetEnd() {
        return this.subheaderInsetEnd;
    }

    public int getSubheaderInsetStart() {
        return this.subheaderInsetStart;
    }

    public View inflateHeaderView(int res) {
        View inflate = this.layoutInflater.inflate(res, this.headerLayout, false);
        addHeaderView(inflate);
        return inflate;
    }

    public void initForMenu(Context context, MenuBuilder menu2) {
        this.layoutInflater = LayoutInflater.from(context);
        this.menu = menu2;
        this.paddingSeparator = context.getResources().getDimensionPixelOffset(R.dimen.design_navigation_separator_vertical_padding);
    }

    public boolean isBehindStatusBar() {
        return this.isBehindStatusBar;
    }

    public void onCloseMenu(MenuBuilder menu2, boolean allMenusAreClosing) {
        MenuPresenter.Callback callback2 = this.callback;
        if (callback2 != null) {
            callback2.onCloseMenu(menu2, allMenusAreClosing);
        }
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            SparseArray sparseParcelableArray = bundle.getSparseParcelableArray("android:menu:list");
            if (sparseParcelableArray != null) {
                this.menuView.restoreHierarchyState(sparseParcelableArray);
            }
            Bundle bundle2 = bundle.getBundle(STATE_ADAPTER);
            if (bundle2 != null) {
                this.adapter.restoreInstanceState(bundle2);
            }
            SparseArray sparseParcelableArray2 = bundle.getSparseParcelableArray(STATE_HEADER);
            if (sparseParcelableArray2 != null) {
                this.headerLayout.restoreHierarchyState(sparseParcelableArray2);
            }
        }
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        if (this.menuView != null) {
            SparseArray sparseArray = new SparseArray();
            this.menuView.saveHierarchyState(sparseArray);
            bundle.putSparseParcelableArray("android:menu:list", sparseArray);
        }
        NavigationMenuAdapter navigationMenuAdapter = this.adapter;
        if (navigationMenuAdapter != null) {
            bundle.putBundle(STATE_ADAPTER, navigationMenuAdapter.createInstanceState());
        }
        if (this.headerLayout != null) {
            SparseArray sparseArray2 = new SparseArray();
            this.headerLayout.saveHierarchyState(sparseArray2);
            bundle.putSparseParcelableArray(STATE_HEADER, sparseArray2);
        }
        return bundle;
    }

    public boolean onSubMenuSelected(SubMenuBuilder subMenu) {
        return false;
    }

    public void removeHeaderView(View view) {
        this.headerLayout.removeView(view);
        if (this.headerLayout.getChildCount() == 0) {
            NavigationMenuView navigationMenuView = this.menuView;
            navigationMenuView.setPadding(0, this.paddingTopDefault, 0, navigationMenuView.getPaddingBottom());
        }
    }

    public void setBehindStatusBar(boolean behindStatusBar) {
        if (this.isBehindStatusBar != behindStatusBar) {
            this.isBehindStatusBar = behindStatusBar;
            updateTopPadding();
        }
    }

    public void setCallback(MenuPresenter.Callback cb) {
        this.callback = cb;
    }

    public void setCheckedItem(MenuItemImpl item) {
        this.adapter.setCheckedItem(item);
    }

    public void setDividerInsetEnd(int dividerInsetEnd2) {
        this.dividerInsetEnd = dividerInsetEnd2;
        updateMenuView(false);
    }

    public void setDividerInsetStart(int dividerInsetStart2) {
        this.dividerInsetStart = dividerInsetStart2;
        updateMenuView(false);
    }

    public void setId(int id2) {
        this.id = id2;
    }

    public void setItemBackground(Drawable itemBackground2) {
        this.itemBackground = itemBackground2;
        updateMenuView(false);
    }

    public void setItemForeground(RippleDrawable itemForeground2) {
        this.itemForeground = itemForeground2;
        updateMenuView(false);
    }

    public void setItemHorizontalPadding(int itemHorizontalPadding2) {
        this.itemHorizontalPadding = itemHorizontalPadding2;
        updateMenuView(false);
    }

    public void setItemIconPadding(int itemIconPadding2) {
        this.itemIconPadding = itemIconPadding2;
        updateMenuView(false);
    }

    public void setItemIconSize(int itemIconSize2) {
        if (this.itemIconSize != itemIconSize2) {
            this.itemIconSize = itemIconSize2;
            this.hasCustomItemIconSize = true;
            updateMenuView(false);
        }
    }

    public void setItemIconTintList(ColorStateList tint) {
        this.iconTintList = tint;
        updateMenuView(false);
    }

    public void setItemMaxLines(int itemMaxLines2) {
        this.itemMaxLines = itemMaxLines2;
        updateMenuView(false);
    }

    public void setItemTextAppearance(int resId) {
        this.textAppearance = resId;
        updateMenuView(false);
    }

    public void setItemTextColor(ColorStateList textColor2) {
        this.textColor = textColor2;
        updateMenuView(false);
    }

    public void setItemVerticalPadding(int itemVerticalPadding2) {
        this.itemVerticalPadding = itemVerticalPadding2;
        updateMenuView(false);
    }

    public void setOverScrollMode(int overScrollMode2) {
        this.overScrollMode = overScrollMode2;
        NavigationMenuView navigationMenuView = this.menuView;
        if (navigationMenuView != null) {
            navigationMenuView.setOverScrollMode(overScrollMode2);
        }
    }

    public void setSubheaderColor(ColorStateList subheaderColor2) {
        this.subheaderColor = subheaderColor2;
        updateMenuView(false);
    }

    public void setSubheaderInsetEnd(int subheaderInsetEnd2) {
        this.subheaderInsetEnd = subheaderInsetEnd2;
        updateMenuView(false);
    }

    public void setSubheaderInsetStart(int subheaderInsetStart2) {
        this.subheaderInsetStart = subheaderInsetStart2;
        updateMenuView(false);
    }

    public void setSubheaderTextAppearance(int resId) {
        this.subheaderTextAppearance = resId;
        updateMenuView(false);
    }

    public void setUpdateSuspended(boolean updateSuspended) {
        NavigationMenuAdapter navigationMenuAdapter = this.adapter;
        if (navigationMenuAdapter != null) {
            navigationMenuAdapter.setUpdateSuspended(updateSuspended);
        }
    }

    public void updateMenuView(boolean cleared) {
        NavigationMenuAdapter navigationMenuAdapter = this.adapter;
        if (navigationMenuAdapter != null) {
            navigationMenuAdapter.update();
        }
    }
}

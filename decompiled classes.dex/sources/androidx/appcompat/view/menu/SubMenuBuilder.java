package androidx.appcompat.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.appcompat.view.menu.MenuBuilder;

public class SubMenuBuilder extends MenuBuilder implements SubMenu {
    private MenuItemImpl mItem;
    private MenuBuilder mParentMenu;

    public SubMenuBuilder(Context context, MenuBuilder parentMenu, MenuItemImpl item) {
        super(context);
        this.mParentMenu = parentMenu;
        this.mItem = item;
    }

    public boolean collapseItemActionView(MenuItemImpl item) {
        return this.mParentMenu.collapseItemActionView(item);
    }

    /* access modifiers changed from: package-private */
    public boolean dispatchMenuItemSelected(MenuBuilder menu, MenuItem item) {
        return super.dispatchMenuItemSelected(menu, item) || this.mParentMenu.dispatchMenuItemSelected(menu, item);
    }

    public boolean expandItemActionView(MenuItemImpl item) {
        return this.mParentMenu.expandItemActionView(item);
    }

    public String getActionViewStatesKey() {
        MenuItemImpl menuItemImpl = this.mItem;
        int itemId = menuItemImpl != null ? menuItemImpl.getItemId() : 0;
        if (itemId == 0) {
            return null;
        }
        return super.getActionViewStatesKey() + ":" + itemId;
    }

    public MenuItem getItem() {
        return this.mItem;
    }

    public Menu getParentMenu() {
        return this.mParentMenu;
    }

    public MenuBuilder getRootMenu() {
        return this.mParentMenu.getRootMenu();
    }

    public boolean isGroupDividerEnabled() {
        return this.mParentMenu.isGroupDividerEnabled();
    }

    public boolean isQwertyMode() {
        return this.mParentMenu.isQwertyMode();
    }

    public boolean isShortcutsVisible() {
        return this.mParentMenu.isShortcutsVisible();
    }

    public void setCallback(MenuBuilder.Callback callback) {
        this.mParentMenu.setCallback(callback);
    }

    public void setGroupDividerEnabled(boolean groupDividerEnabled) {
        this.mParentMenu.setGroupDividerEnabled(groupDividerEnabled);
    }

    public SubMenu setHeaderIcon(int iconRes) {
        return (SubMenu) super.setHeaderIconInt(iconRes);
    }

    public SubMenu setHeaderIcon(Drawable icon) {
        return (SubMenu) super.setHeaderIconInt(icon);
    }

    public SubMenu setHeaderTitle(int titleRes) {
        return (SubMenu) super.setHeaderTitleInt(titleRes);
    }

    public SubMenu setHeaderTitle(CharSequence title) {
        return (SubMenu) super.setHeaderTitleInt(title);
    }

    public SubMenu setHeaderView(View view) {
        return (SubMenu) super.setHeaderViewInt(view);
    }

    public SubMenu setIcon(int iconRes) {
        this.mItem.setIcon(iconRes);
        return this;
    }

    public SubMenu setIcon(Drawable icon) {
        this.mItem.setIcon(icon);
        return this;
    }

    public void setQwertyMode(boolean isQwerty) {
        this.mParentMenu.setQwertyMode(isQwerty);
    }

    public void setShortcutsVisible(boolean shortcutsVisible) {
        this.mParentMenu.setShortcutsVisible(shortcutsVisible);
    }
}

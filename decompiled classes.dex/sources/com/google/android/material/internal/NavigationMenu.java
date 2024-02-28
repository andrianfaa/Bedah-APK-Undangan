package com.google.android.material.internal;

import android.content.Context;
import android.view.SubMenu;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;

public class NavigationMenu extends MenuBuilder {
    public NavigationMenu(Context context) {
        super(context);
    }

    public SubMenu addSubMenu(int group, int id, int categoryOrder, CharSequence title) {
        MenuItemImpl menuItemImpl = (MenuItemImpl) addInternal(group, id, categoryOrder, title);
        NavigationSubMenu navigationSubMenu = new NavigationSubMenu(getContext(), this, menuItemImpl);
        menuItemImpl.setSubMenu(navigationSubMenu);
        return navigationSubMenu;
    }
}

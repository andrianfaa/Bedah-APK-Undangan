package androidx.appcompat.view.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import androidx.core.internal.view.SupportMenu;

public class MenuWrapperICS extends BaseMenuWrapper implements Menu {
    private final SupportMenu mWrappedObject;

    public MenuWrapperICS(Context context, SupportMenu object) {
        super(context);
        if (object != null) {
            this.mWrappedObject = object;
            return;
        }
        throw new IllegalArgumentException("Wrapped Object can not be null.");
    }

    public MenuItem add(int titleRes) {
        return getMenuItemWrapper(this.mWrappedObject.add(titleRes));
    }

    public MenuItem add(int groupId, int itemId, int order, int titleRes) {
        return getMenuItemWrapper(this.mWrappedObject.add(groupId, itemId, order, titleRes));
    }

    public MenuItem add(int groupId, int itemId, int order, CharSequence title) {
        return getMenuItemWrapper(this.mWrappedObject.add(groupId, itemId, order, title));
    }

    public MenuItem add(CharSequence title) {
        return getMenuItemWrapper(this.mWrappedObject.add(title));
    }

    public int addIntentOptions(int groupId, int itemId, int order, ComponentName caller, Intent[] specifics, Intent intent, int flags, MenuItem[] outSpecificItems) {
        MenuItem[] menuItemArr = outSpecificItems;
        MenuItem[] menuItemArr2 = null;
        if (menuItemArr != null) {
            menuItemArr2 = new MenuItem[menuItemArr.length];
        }
        int addIntentOptions = this.mWrappedObject.addIntentOptions(groupId, itemId, order, caller, specifics, intent, flags, menuItemArr2);
        if (menuItemArr2 != null) {
            int length = menuItemArr2.length;
            for (int i = 0; i < length; i++) {
                menuItemArr[i] = getMenuItemWrapper(menuItemArr2[i]);
            }
        }
        return addIntentOptions;
    }

    public SubMenu addSubMenu(int titleRes) {
        return getSubMenuWrapper(this.mWrappedObject.addSubMenu(titleRes));
    }

    public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes) {
        return getSubMenuWrapper(this.mWrappedObject.addSubMenu(groupId, itemId, order, titleRes));
    }

    public SubMenu addSubMenu(int groupId, int itemId, int order, CharSequence title) {
        return getSubMenuWrapper(this.mWrappedObject.addSubMenu(groupId, itemId, order, title));
    }

    public SubMenu addSubMenu(CharSequence title) {
        return getSubMenuWrapper(this.mWrappedObject.addSubMenu(title));
    }

    public void clear() {
        internalClear();
        this.mWrappedObject.clear();
    }

    public void close() {
        this.mWrappedObject.close();
    }

    public MenuItem findItem(int id) {
        return getMenuItemWrapper(this.mWrappedObject.findItem(id));
    }

    public MenuItem getItem(int index) {
        return getMenuItemWrapper(this.mWrappedObject.getItem(index));
    }

    public boolean hasVisibleItems() {
        return this.mWrappedObject.hasVisibleItems();
    }

    public boolean isShortcutKey(int keyCode, KeyEvent event) {
        return this.mWrappedObject.isShortcutKey(keyCode, event);
    }

    public boolean performIdentifierAction(int id, int flags) {
        return this.mWrappedObject.performIdentifierAction(id, flags);
    }

    public boolean performShortcut(int keyCode, KeyEvent event, int flags) {
        return this.mWrappedObject.performShortcut(keyCode, event, flags);
    }

    public void removeGroup(int groupId) {
        internalRemoveGroup(groupId);
        this.mWrappedObject.removeGroup(groupId);
    }

    public void removeItem(int id) {
        internalRemoveItem(id);
        this.mWrappedObject.removeItem(id);
    }

    public void setGroupCheckable(int group, boolean checkable, boolean exclusive) {
        this.mWrappedObject.setGroupCheckable(group, checkable, exclusive);
    }

    public void setGroupEnabled(int group, boolean enabled) {
        this.mWrappedObject.setGroupEnabled(group, enabled);
    }

    public void setGroupVisible(int group, boolean visible) {
        this.mWrappedObject.setGroupVisible(group, visible);
    }

    public void setQwertyMode(boolean isQwerty) {
        this.mWrappedObject.setQwertyMode(isQwerty);
    }

    public int size() {
        return this.mWrappedObject.size();
    }
}

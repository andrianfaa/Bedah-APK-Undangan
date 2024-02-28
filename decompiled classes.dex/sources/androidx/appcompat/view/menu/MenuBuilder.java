package androidx.appcompat.view.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ActionProvider;
import androidx.core.view.ViewConfigurationCompat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MenuBuilder implements SupportMenu {
    private static final String ACTION_VIEW_STATES_KEY = "android:menu:actionviewstates";
    private static final String EXPANDED_ACTION_VIEW_ID = "android:menu:expandedactionview";
    private static final String PRESENTER_KEY = "android:menu:presenters";
    private static final String TAG = "MenuBuilder";
    private static final int[] sCategoryToOrder = {1, 4, 5, 3, 2, 0};
    private ArrayList<MenuItemImpl> mActionItems;
    private Callback mCallback;
    private final Context mContext;
    private ContextMenu.ContextMenuInfo mCurrentMenuInfo;
    private int mDefaultShowAsAction = 0;
    private MenuItemImpl mExpandedItem;
    private boolean mGroupDividerEnabled = false;
    Drawable mHeaderIcon;
    CharSequence mHeaderTitle;
    View mHeaderView;
    private boolean mIsActionItemsStale;
    private boolean mIsClosing = false;
    private boolean mIsVisibleItemsStale;
    private ArrayList<MenuItemImpl> mItems;
    private boolean mItemsChangedWhileDispatchPrevented = false;
    private ArrayList<MenuItemImpl> mNonActionItems;
    private boolean mOptionalIconsVisible = false;
    private boolean mOverrideVisibleItems;
    private CopyOnWriteArrayList<WeakReference<MenuPresenter>> mPresenters = new CopyOnWriteArrayList<>();
    private boolean mPreventDispatchingItemsChanged = false;
    private boolean mQwertyMode;
    private final Resources mResources;
    private boolean mShortcutsVisible;
    private boolean mStructureChangedWhileDispatchPrevented = false;
    private ArrayList<MenuItemImpl> mTempShortcutItemList = new ArrayList<>();
    private ArrayList<MenuItemImpl> mVisibleItems;

    public interface Callback {
        boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem);

        void onMenuModeChange(MenuBuilder menuBuilder);
    }

    public interface ItemInvoker {
        boolean invokeItem(MenuItemImpl menuItemImpl);
    }

    public MenuBuilder(Context context) {
        this.mContext = context;
        this.mResources = context.getResources();
        this.mItems = new ArrayList<>();
        this.mVisibleItems = new ArrayList<>();
        this.mIsVisibleItemsStale = true;
        this.mActionItems = new ArrayList<>();
        this.mNonActionItems = new ArrayList<>();
        this.mIsActionItemsStale = true;
        setShortcutsVisibleInner(true);
    }

    private MenuItemImpl createNewMenuItem(int group, int id, int categoryOrder, int ordering, CharSequence title, int defaultShowAsAction) {
        return new MenuItemImpl(this, group, id, categoryOrder, ordering, title, defaultShowAsAction);
    }

    private void dispatchPresenterUpdate(boolean cleared) {
        if (!this.mPresenters.isEmpty()) {
            stopDispatchingItemsChanged();
            Iterator<WeakReference<MenuPresenter>> it = this.mPresenters.iterator();
            while (it.hasNext()) {
                WeakReference next = it.next();
                MenuPresenter menuPresenter = (MenuPresenter) next.get();
                if (menuPresenter == null) {
                    this.mPresenters.remove(next);
                } else {
                    menuPresenter.updateMenuView(cleared);
                }
            }
            startDispatchingItemsChanged();
        }
    }

    private void dispatchRestoreInstanceState(Bundle state) {
        Parcelable parcelable;
        SparseArray sparseParcelableArray = state.getSparseParcelableArray(PRESENTER_KEY);
        if (sparseParcelableArray != null && !this.mPresenters.isEmpty()) {
            Iterator<WeakReference<MenuPresenter>> it = this.mPresenters.iterator();
            while (it.hasNext()) {
                WeakReference next = it.next();
                MenuPresenter menuPresenter = (MenuPresenter) next.get();
                if (menuPresenter == null) {
                    this.mPresenters.remove(next);
                } else {
                    int id = menuPresenter.getId();
                    if (id > 0 && (parcelable = (Parcelable) sparseParcelableArray.get(id)) != null) {
                        menuPresenter.onRestoreInstanceState(parcelable);
                    }
                }
            }
        }
    }

    private void dispatchSaveInstanceState(Bundle outState) {
        Parcelable onSaveInstanceState;
        if (!this.mPresenters.isEmpty()) {
            SparseArray sparseArray = new SparseArray();
            Iterator<WeakReference<MenuPresenter>> it = this.mPresenters.iterator();
            while (it.hasNext()) {
                WeakReference next = it.next();
                MenuPresenter menuPresenter = (MenuPresenter) next.get();
                if (menuPresenter == null) {
                    this.mPresenters.remove(next);
                } else {
                    int id = menuPresenter.getId();
                    if (id > 0 && (onSaveInstanceState = menuPresenter.onSaveInstanceState()) != null) {
                        sparseArray.put(id, onSaveInstanceState);
                    }
                }
            }
            outState.putSparseParcelableArray(PRESENTER_KEY, sparseArray);
        }
    }

    private boolean dispatchSubMenuSelected(SubMenuBuilder subMenu, MenuPresenter preferredPresenter) {
        if (this.mPresenters.isEmpty()) {
            return false;
        }
        boolean z = false;
        if (preferredPresenter != null) {
            z = preferredPresenter.onSubMenuSelected(subMenu);
        }
        Iterator<WeakReference<MenuPresenter>> it = this.mPresenters.iterator();
        while (it.hasNext()) {
            WeakReference next = it.next();
            MenuPresenter menuPresenter = (MenuPresenter) next.get();
            if (menuPresenter == null) {
                this.mPresenters.remove(next);
            } else if (!z) {
                z = menuPresenter.onSubMenuSelected(subMenu);
            }
        }
        return z;
    }

    private static int findInsertIndex(ArrayList<MenuItemImpl> arrayList, int ordering) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            if (arrayList.get(size).getOrdering() <= ordering) {
                return size + 1;
            }
        }
        return 0;
    }

    private static int getOrdering(int categoryOrder) {
        int i = (-65536 & categoryOrder) >> 16;
        if (i >= 0) {
            int[] iArr = sCategoryToOrder;
            if (i < iArr.length) {
                return (iArr[i] << 16) | (65535 & categoryOrder);
            }
        }
        throw new IllegalArgumentException("order does not contain a valid category.");
    }

    private void removeItemAtInt(int index, boolean updateChildrenOnMenuViews) {
        if (index >= 0 && index < this.mItems.size()) {
            this.mItems.remove(index);
            if (updateChildrenOnMenuViews) {
                onItemsChanged(true);
            }
        }
    }

    private void setHeaderInternal(int titleRes, CharSequence title, int iconRes, Drawable icon, View view) {
        Resources resources = getResources();
        if (view != null) {
            this.mHeaderView = view;
            this.mHeaderTitle = null;
            this.mHeaderIcon = null;
        } else {
            if (titleRes > 0) {
                this.mHeaderTitle = resources.getText(titleRes);
            } else if (title != null) {
                this.mHeaderTitle = title;
            }
            if (iconRes > 0) {
                this.mHeaderIcon = ContextCompat.getDrawable(getContext(), iconRes);
            } else if (icon != null) {
                this.mHeaderIcon = icon;
            }
            this.mHeaderView = null;
        }
        onItemsChanged(false);
    }

    private void setShortcutsVisibleInner(boolean shortcutsVisible) {
        boolean z = true;
        if (!shortcutsVisible || this.mResources.getConfiguration().keyboard == 1 || !ViewConfigurationCompat.shouldShowMenuShortcutsWhenKeyboardPresent(ViewConfiguration.get(this.mContext), this.mContext)) {
            z = false;
        }
        this.mShortcutsVisible = z;
    }

    public MenuItem add(int titleRes) {
        return addInternal(0, 0, 0, this.mResources.getString(titleRes));
    }

    public MenuItem add(int group, int id, int categoryOrder, int title) {
        return addInternal(group, id, categoryOrder, this.mResources.getString(title));
    }

    public MenuItem add(int group, int id, int categoryOrder, CharSequence title) {
        return addInternal(group, id, categoryOrder, title);
    }

    public MenuItem add(CharSequence title) {
        return addInternal(0, 0, 0, title);
    }

    public int addIntentOptions(int group, int id, int categoryOrder, ComponentName caller, Intent[] specifics, Intent intent, int flags, MenuItem[] outSpecificItems) {
        Intent[] intentArr = specifics;
        PackageManager packageManager = this.mContext.getPackageManager();
        int i = 0;
        Intent intent2 = intent;
        List<ResolveInfo> queryIntentActivityOptions = packageManager.queryIntentActivityOptions(caller, intentArr, intent2, 0);
        if (queryIntentActivityOptions != null) {
            i = queryIntentActivityOptions.size();
        }
        if ((flags & 1) == 0) {
            removeGroup(group);
        }
        for (int i2 = 0; i2 < i; i2++) {
            ResolveInfo resolveInfo = queryIntentActivityOptions.get(i2);
            Intent intent3 = new Intent(resolveInfo.specificIndex < 0 ? intent2 : intentArr[resolveInfo.specificIndex]);
            intent3.setComponent(new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName, resolveInfo.activityInfo.name));
            MenuItem intent4 = add(group, id, categoryOrder, resolveInfo.loadLabel(packageManager)).setIcon(resolveInfo.loadIcon(packageManager)).setIntent(intent3);
            if (outSpecificItems != null && resolveInfo.specificIndex >= 0) {
                outSpecificItems[resolveInfo.specificIndex] = intent4;
            }
        }
        int i3 = group;
        int i4 = id;
        int i5 = categoryOrder;
        return i;
    }

    /* access modifiers changed from: protected */
    public MenuItem addInternal(int group, int id, int categoryOrder, CharSequence title) {
        int ordering = getOrdering(categoryOrder);
        MenuItemImpl createNewMenuItem = createNewMenuItem(group, id, categoryOrder, ordering, title, this.mDefaultShowAsAction);
        ContextMenu.ContextMenuInfo contextMenuInfo = this.mCurrentMenuInfo;
        if (contextMenuInfo != null) {
            createNewMenuItem.setMenuInfo(contextMenuInfo);
        }
        ArrayList<MenuItemImpl> arrayList = this.mItems;
        arrayList.add(findInsertIndex(arrayList, ordering), createNewMenuItem);
        onItemsChanged(true);
        return createNewMenuItem;
    }

    public void addMenuPresenter(MenuPresenter presenter) {
        addMenuPresenter(presenter, this.mContext);
    }

    public void addMenuPresenter(MenuPresenter presenter, Context menuContext) {
        this.mPresenters.add(new WeakReference(presenter));
        presenter.initForMenu(menuContext, this);
        this.mIsActionItemsStale = true;
    }

    public SubMenu addSubMenu(int titleRes) {
        return addSubMenu(0, 0, 0, (CharSequence) this.mResources.getString(titleRes));
    }

    public SubMenu addSubMenu(int group, int id, int categoryOrder, int title) {
        return addSubMenu(group, id, categoryOrder, (CharSequence) this.mResources.getString(title));
    }

    public SubMenu addSubMenu(int group, int id, int categoryOrder, CharSequence title) {
        MenuItemImpl menuItemImpl = (MenuItemImpl) addInternal(group, id, categoryOrder, title);
        SubMenuBuilder subMenuBuilder = new SubMenuBuilder(this.mContext, this, menuItemImpl);
        menuItemImpl.setSubMenu(subMenuBuilder);
        return subMenuBuilder;
    }

    public SubMenu addSubMenu(CharSequence title) {
        return addSubMenu(0, 0, 0, title);
    }

    public void changeMenuMode() {
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onMenuModeChange(this);
        }
    }

    public void clear() {
        MenuItemImpl menuItemImpl = this.mExpandedItem;
        if (menuItemImpl != null) {
            collapseItemActionView(menuItemImpl);
        }
        this.mItems.clear();
        onItemsChanged(true);
    }

    public void clearAll() {
        this.mPreventDispatchingItemsChanged = true;
        clear();
        clearHeader();
        this.mPresenters.clear();
        this.mPreventDispatchingItemsChanged = false;
        this.mItemsChangedWhileDispatchPrevented = false;
        this.mStructureChangedWhileDispatchPrevented = false;
        onItemsChanged(true);
    }

    public void clearHeader() {
        this.mHeaderIcon = null;
        this.mHeaderTitle = null;
        this.mHeaderView = null;
        onItemsChanged(false);
    }

    public void close() {
        close(true);
    }

    public final void close(boolean closeAllMenus) {
        if (!this.mIsClosing) {
            this.mIsClosing = true;
            Iterator<WeakReference<MenuPresenter>> it = this.mPresenters.iterator();
            while (it.hasNext()) {
                WeakReference next = it.next();
                MenuPresenter menuPresenter = (MenuPresenter) next.get();
                if (menuPresenter == null) {
                    this.mPresenters.remove(next);
                } else {
                    menuPresenter.onCloseMenu(this, closeAllMenus);
                }
            }
            this.mIsClosing = false;
        }
    }

    public boolean collapseItemActionView(MenuItemImpl item) {
        if (this.mPresenters.isEmpty() || this.mExpandedItem != item) {
            return false;
        }
        boolean z = false;
        stopDispatchingItemsChanged();
        Iterator<WeakReference<MenuPresenter>> it = this.mPresenters.iterator();
        while (it.hasNext()) {
            WeakReference next = it.next();
            MenuPresenter menuPresenter = (MenuPresenter) next.get();
            if (menuPresenter == null) {
                this.mPresenters.remove(next);
            } else {
                boolean collapseItemActionView = menuPresenter.collapseItemActionView(this, item);
                z = collapseItemActionView;
                if (collapseItemActionView) {
                    break;
                }
            }
        }
        startDispatchingItemsChanged();
        if (z) {
            this.mExpandedItem = null;
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public boolean dispatchMenuItemSelected(MenuBuilder menu, MenuItem item) {
        Callback callback = this.mCallback;
        return callback != null && callback.onMenuItemSelected(menu, item);
    }

    public boolean expandItemActionView(MenuItemImpl item) {
        if (this.mPresenters.isEmpty()) {
            return false;
        }
        boolean z = false;
        stopDispatchingItemsChanged();
        Iterator<WeakReference<MenuPresenter>> it = this.mPresenters.iterator();
        while (it.hasNext()) {
            WeakReference next = it.next();
            MenuPresenter menuPresenter = (MenuPresenter) next.get();
            if (menuPresenter == null) {
                this.mPresenters.remove(next);
            } else {
                boolean expandItemActionView = menuPresenter.expandItemActionView(this, item);
                z = expandItemActionView;
                if (expandItemActionView) {
                    break;
                }
            }
        }
        startDispatchingItemsChanged();
        if (z) {
            this.mExpandedItem = item;
        }
        return z;
    }

    public int findGroupIndex(int group) {
        return findGroupIndex(group, 0);
    }

    public int findGroupIndex(int group, int start) {
        int size = size();
        if (start < 0) {
            start = 0;
        }
        for (int i = start; i < size; i++) {
            if (this.mItems.get(i).getGroupId() == group) {
                return i;
            }
        }
        return -1;
    }

    public MenuItem findItem(int id) {
        MenuItem findItem;
        int size = size();
        for (int i = 0; i < size; i++) {
            MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.getItemId() == id) {
                return menuItemImpl;
            }
            if (menuItemImpl.hasSubMenu() && (findItem = menuItemImpl.getSubMenu().findItem(id)) != null) {
                return findItem;
            }
        }
        return null;
    }

    public int findItemIndex(int id) {
        int size = size();
        for (int i = 0; i < size; i++) {
            if (this.mItems.get(i).getItemId() == id) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public MenuItemImpl findItemWithShortcutForKey(int keyCode, KeyEvent event) {
        ArrayList<MenuItemImpl> arrayList = this.mTempShortcutItemList;
        arrayList.clear();
        findItemsWithShortcutForKey(arrayList, keyCode, event);
        if (arrayList.isEmpty()) {
            return null;
        }
        int metaState = event.getMetaState();
        KeyCharacterMap.KeyData keyData = new KeyCharacterMap.KeyData();
        event.getKeyData(keyData);
        int size = arrayList.size();
        if (size == 1) {
            return arrayList.get(0);
        }
        boolean isQwertyMode = isQwertyMode();
        for (int i = 0; i < size; i++) {
            MenuItemImpl menuItemImpl = arrayList.get(i);
            char alphabeticShortcut = isQwertyMode ? menuItemImpl.getAlphabeticShortcut() : menuItemImpl.getNumericShortcut();
            if ((alphabeticShortcut == keyData.meta[0] && (metaState & 2) == 0) || ((alphabeticShortcut == keyData.meta[2] && (metaState & 2) != 0) || (isQwertyMode && alphabeticShortcut == 8 && keyCode == 67))) {
                return menuItemImpl;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void findItemsWithShortcutForKey(List<MenuItemImpl> list, int keyCode, KeyEvent event) {
        List<MenuItemImpl> list2 = list;
        int i = keyCode;
        KeyEvent keyEvent = event;
        boolean isQwertyMode = isQwertyMode();
        int modifiers = event.getModifiers();
        KeyCharacterMap.KeyData keyData = new KeyCharacterMap.KeyData();
        if (keyEvent.getKeyData(keyData) || i == 67) {
            int size = this.mItems.size();
            for (int i2 = 0; i2 < size; i2++) {
                MenuItemImpl menuItemImpl = this.mItems.get(i2);
                if (menuItemImpl.hasSubMenu()) {
                    ((MenuBuilder) menuItemImpl.getSubMenu()).findItemsWithShortcutForKey(list2, i, keyEvent);
                }
                char alphabeticShortcut = isQwertyMode ? menuItemImpl.getAlphabeticShortcut() : menuItemImpl.getNumericShortcut();
                if (((modifiers & SupportMenu.SUPPORTED_MODIFIERS_MASK) == (69647 & (isQwertyMode ? menuItemImpl.getAlphabeticModifiers() : menuItemImpl.getNumericModifiers()))) && alphabeticShortcut != 0 && ((alphabeticShortcut == keyData.meta[0] || alphabeticShortcut == keyData.meta[2] || (isQwertyMode && alphabeticShortcut == 8 && i == 67)) && menuItemImpl.isEnabled())) {
                    list2.add(menuItemImpl);
                }
            }
        }
    }

    public void flagActionItems() {
        ArrayList<MenuItemImpl> visibleItems = getVisibleItems();
        if (this.mIsActionItemsStale) {
            boolean z = false;
            Iterator<WeakReference<MenuPresenter>> it = this.mPresenters.iterator();
            while (it.hasNext()) {
                WeakReference next = it.next();
                MenuPresenter menuPresenter = (MenuPresenter) next.get();
                if (menuPresenter == null) {
                    this.mPresenters.remove(next);
                } else {
                    z |= menuPresenter.flagActionItems();
                }
            }
            if (z) {
                this.mActionItems.clear();
                this.mNonActionItems.clear();
                int size = visibleItems.size();
                for (int i = 0; i < size; i++) {
                    MenuItemImpl menuItemImpl = visibleItems.get(i);
                    if (menuItemImpl.isActionButton()) {
                        this.mActionItems.add(menuItemImpl);
                    } else {
                        this.mNonActionItems.add(menuItemImpl);
                    }
                }
            } else {
                this.mActionItems.clear();
                this.mNonActionItems.clear();
                this.mNonActionItems.addAll(getVisibleItems());
            }
            this.mIsActionItemsStale = false;
        }
    }

    public ArrayList<MenuItemImpl> getActionItems() {
        flagActionItems();
        return this.mActionItems;
    }

    /* access modifiers changed from: protected */
    public String getActionViewStatesKey() {
        return ACTION_VIEW_STATES_KEY;
    }

    public Context getContext() {
        return this.mContext;
    }

    public MenuItemImpl getExpandedItem() {
        return this.mExpandedItem;
    }

    public Drawable getHeaderIcon() {
        return this.mHeaderIcon;
    }

    public CharSequence getHeaderTitle() {
        return this.mHeaderTitle;
    }

    public View getHeaderView() {
        return this.mHeaderView;
    }

    public MenuItem getItem(int index) {
        return this.mItems.get(index);
    }

    public ArrayList<MenuItemImpl> getNonActionItems() {
        flagActionItems();
        return this.mNonActionItems;
    }

    /* access modifiers changed from: package-private */
    public boolean getOptionalIconsVisible() {
        return this.mOptionalIconsVisible;
    }

    /* access modifiers changed from: package-private */
    public Resources getResources() {
        return this.mResources;
    }

    public MenuBuilder getRootMenu() {
        return this;
    }

    public ArrayList<MenuItemImpl> getVisibleItems() {
        if (!this.mIsVisibleItemsStale) {
            return this.mVisibleItems;
        }
        this.mVisibleItems.clear();
        int size = this.mItems.size();
        for (int i = 0; i < size; i++) {
            MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.isVisible()) {
                this.mVisibleItems.add(menuItemImpl);
            }
        }
        this.mIsVisibleItemsStale = false;
        this.mIsActionItemsStale = true;
        return this.mVisibleItems;
    }

    public boolean hasVisibleItems() {
        if (this.mOverrideVisibleItems) {
            return true;
        }
        int size = size();
        for (int i = 0; i < size; i++) {
            if (this.mItems.get(i).isVisible()) {
                return true;
            }
        }
        return false;
    }

    public boolean isGroupDividerEnabled() {
        return this.mGroupDividerEnabled;
    }

    /* access modifiers changed from: package-private */
    public boolean isQwertyMode() {
        return this.mQwertyMode;
    }

    public boolean isShortcutKey(int keyCode, KeyEvent event) {
        return findItemWithShortcutForKey(keyCode, event) != null;
    }

    public boolean isShortcutsVisible() {
        return this.mShortcutsVisible;
    }

    /* access modifiers changed from: package-private */
    public void onItemActionRequestChanged(MenuItemImpl item) {
        this.mIsActionItemsStale = true;
        onItemsChanged(true);
    }

    /* access modifiers changed from: package-private */
    public void onItemVisibleChanged(MenuItemImpl item) {
        this.mIsVisibleItemsStale = true;
        onItemsChanged(true);
    }

    public void onItemsChanged(boolean structureChanged) {
        if (!this.mPreventDispatchingItemsChanged) {
            if (structureChanged) {
                this.mIsVisibleItemsStale = true;
                this.mIsActionItemsStale = true;
            }
            dispatchPresenterUpdate(structureChanged);
            return;
        }
        this.mItemsChangedWhileDispatchPrevented = true;
        if (structureChanged) {
            this.mStructureChangedWhileDispatchPrevented = true;
        }
    }

    public boolean performIdentifierAction(int id, int flags) {
        return performItemAction(findItem(id), flags);
    }

    public boolean performItemAction(MenuItem item, int flags) {
        return performItemAction(item, (MenuPresenter) null, flags);
    }

    public boolean performItemAction(MenuItem item, MenuPresenter preferredPresenter, int flags) {
        MenuItemImpl menuItemImpl = (MenuItemImpl) item;
        if (menuItemImpl == null || !menuItemImpl.isEnabled()) {
            return false;
        }
        boolean invoke = menuItemImpl.invoke();
        ActionProvider supportActionProvider = menuItemImpl.getSupportActionProvider();
        boolean z = supportActionProvider != null && supportActionProvider.hasSubMenu();
        if (menuItemImpl.hasCollapsibleActionView()) {
            invoke |= menuItemImpl.expandActionView();
            if (invoke) {
                close(true);
            }
        } else if (menuItemImpl.hasSubMenu() || z) {
            if ((flags & 4) == 0) {
                close(false);
            }
            if (!menuItemImpl.hasSubMenu()) {
                menuItemImpl.setSubMenu(new SubMenuBuilder(getContext(), this, menuItemImpl));
            }
            SubMenuBuilder subMenuBuilder = (SubMenuBuilder) menuItemImpl.getSubMenu();
            if (z) {
                supportActionProvider.onPrepareSubMenu(subMenuBuilder);
            }
            invoke |= dispatchSubMenuSelected(subMenuBuilder, preferredPresenter);
            if (!invoke) {
                close(true);
            }
        } else if ((flags & 1) == 0) {
            close(true);
        }
        return invoke;
    }

    public boolean performShortcut(int keyCode, KeyEvent event, int flags) {
        MenuItemImpl findItemWithShortcutForKey = findItemWithShortcutForKey(keyCode, event);
        boolean z = false;
        if (findItemWithShortcutForKey != null) {
            z = performItemAction(findItemWithShortcutForKey, flags);
        }
        if ((flags & 2) != 0) {
            close(true);
        }
        return z;
    }

    public void removeGroup(int group) {
        int findGroupIndex = findGroupIndex(group);
        if (findGroupIndex >= 0) {
            int size = this.mItems.size() - findGroupIndex;
            int i = 0;
            while (true) {
                int i2 = i + 1;
                if (i >= size || this.mItems.get(findGroupIndex).getGroupId() != group) {
                    onItemsChanged(true);
                } else {
                    removeItemAtInt(findGroupIndex, false);
                    i = i2;
                }
            }
            onItemsChanged(true);
        }
    }

    public void removeItem(int id) {
        removeItemAtInt(findItemIndex(id), true);
    }

    public void removeItemAt(int index) {
        removeItemAtInt(index, true);
    }

    public void removeMenuPresenter(MenuPresenter presenter) {
        Iterator<WeakReference<MenuPresenter>> it = this.mPresenters.iterator();
        while (it.hasNext()) {
            WeakReference next = it.next();
            MenuPresenter menuPresenter = (MenuPresenter) next.get();
            if (menuPresenter == null || menuPresenter == presenter) {
                this.mPresenters.remove(next);
            }
        }
    }

    public void restoreActionViewStates(Bundle states) {
        MenuItem findItem;
        if (states != null) {
            SparseArray sparseParcelableArray = states.getSparseParcelableArray(getActionViewStatesKey());
            int size = size();
            for (int i = 0; i < size; i++) {
                MenuItem item = getItem(i);
                View actionView = item.getActionView();
                if (!(actionView == null || actionView.getId() == -1)) {
                    actionView.restoreHierarchyState(sparseParcelableArray);
                }
                if (item.hasSubMenu()) {
                    ((SubMenuBuilder) item.getSubMenu()).restoreActionViewStates(states);
                }
            }
            int i2 = states.getInt(EXPANDED_ACTION_VIEW_ID);
            if (i2 > 0 && (findItem = findItem(i2)) != null) {
                findItem.expandActionView();
            }
        }
    }

    public void restorePresenterStates(Bundle state) {
        dispatchRestoreInstanceState(state);
    }

    public void saveActionViewStates(Bundle outStates) {
        SparseArray sparseArray = null;
        int size = size();
        for (int i = 0; i < size; i++) {
            MenuItem item = getItem(i);
            View actionView = item.getActionView();
            if (!(actionView == null || actionView.getId() == -1)) {
                if (sparseArray == null) {
                    sparseArray = new SparseArray();
                }
                actionView.saveHierarchyState(sparseArray);
                if (item.isActionViewExpanded()) {
                    outStates.putInt(EXPANDED_ACTION_VIEW_ID, item.getItemId());
                }
            }
            if (item.hasSubMenu()) {
                ((SubMenuBuilder) item.getSubMenu()).saveActionViewStates(outStates);
            }
        }
        if (sparseArray != null) {
            outStates.putSparseParcelableArray(getActionViewStatesKey(), sparseArray);
        }
    }

    public void savePresenterStates(Bundle outState) {
        dispatchSaveInstanceState(outState);
    }

    public void setCallback(Callback cb) {
        this.mCallback = cb;
    }

    public void setCurrentMenuInfo(ContextMenu.ContextMenuInfo menuInfo) {
        this.mCurrentMenuInfo = menuInfo;
    }

    public MenuBuilder setDefaultShowAsAction(int defaultShowAsAction) {
        this.mDefaultShowAsAction = defaultShowAsAction;
        return this;
    }

    /* access modifiers changed from: package-private */
    public void setExclusiveItemChecked(MenuItem item) {
        int groupId = item.getGroupId();
        int size = this.mItems.size();
        stopDispatchingItemsChanged();
        for (int i = 0; i < size; i++) {
            MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.getGroupId() == groupId && menuItemImpl.isExclusiveCheckable() && menuItemImpl.isCheckable()) {
                menuItemImpl.setCheckedInt(menuItemImpl == item);
            }
        }
        startDispatchingItemsChanged();
    }

    public void setGroupCheckable(int group, boolean checkable, boolean exclusive) {
        int size = this.mItems.size();
        for (int i = 0; i < size; i++) {
            MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.getGroupId() == group) {
                menuItemImpl.setExclusiveCheckable(exclusive);
                menuItemImpl.setCheckable(checkable);
            }
        }
    }

    public void setGroupDividerEnabled(boolean enabled) {
        this.mGroupDividerEnabled = enabled;
    }

    public void setGroupEnabled(int group, boolean enabled) {
        int size = this.mItems.size();
        for (int i = 0; i < size; i++) {
            MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.getGroupId() == group) {
                menuItemImpl.setEnabled(enabled);
            }
        }
    }

    public void setGroupVisible(int group, boolean visible) {
        int size = this.mItems.size();
        boolean z = false;
        for (int i = 0; i < size; i++) {
            MenuItemImpl menuItemImpl = this.mItems.get(i);
            if (menuItemImpl.getGroupId() == group && menuItemImpl.setVisibleInt(visible)) {
                z = true;
            }
        }
        if (z) {
            onItemsChanged(true);
        }
    }

    /* access modifiers changed from: protected */
    public MenuBuilder setHeaderIconInt(int iconRes) {
        setHeaderInternal(0, (CharSequence) null, iconRes, (Drawable) null, (View) null);
        return this;
    }

    /* access modifiers changed from: protected */
    public MenuBuilder setHeaderIconInt(Drawable icon) {
        setHeaderInternal(0, (CharSequence) null, 0, icon, (View) null);
        return this;
    }

    /* access modifiers changed from: protected */
    public MenuBuilder setHeaderTitleInt(int titleRes) {
        setHeaderInternal(titleRes, (CharSequence) null, 0, (Drawable) null, (View) null);
        return this;
    }

    /* access modifiers changed from: protected */
    public MenuBuilder setHeaderTitleInt(CharSequence title) {
        setHeaderInternal(0, title, 0, (Drawable) null, (View) null);
        return this;
    }

    /* access modifiers changed from: protected */
    public MenuBuilder setHeaderViewInt(View view) {
        setHeaderInternal(0, (CharSequence) null, 0, (Drawable) null, view);
        return this;
    }

    public void setOptionalIconsVisible(boolean visible) {
        this.mOptionalIconsVisible = visible;
    }

    public void setOverrideVisibleItems(boolean override) {
        this.mOverrideVisibleItems = override;
    }

    public void setQwertyMode(boolean isQwerty) {
        this.mQwertyMode = isQwerty;
        onItemsChanged(false);
    }

    public void setShortcutsVisible(boolean shortcutsVisible) {
        if (this.mShortcutsVisible != shortcutsVisible) {
            setShortcutsVisibleInner(shortcutsVisible);
            onItemsChanged(false);
        }
    }

    public int size() {
        return this.mItems.size();
    }

    public void startDispatchingItemsChanged() {
        this.mPreventDispatchingItemsChanged = false;
        if (this.mItemsChangedWhileDispatchPrevented) {
            this.mItemsChangedWhileDispatchPrevented = false;
            onItemsChanged(this.mStructureChangedWhileDispatchPrevented);
        }
    }

    public void stopDispatchingItemsChanged() {
        if (!this.mPreventDispatchingItemsChanged) {
            this.mPreventDispatchingItemsChanged = true;
            this.mItemsChangedWhileDispatchPrevented = false;
            this.mStructureChangedWhileDispatchPrevented = false;
        }
    }
}

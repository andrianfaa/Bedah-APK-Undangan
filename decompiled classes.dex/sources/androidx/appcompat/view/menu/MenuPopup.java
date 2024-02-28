package androidx.appcompat.view.menu;

import android.content.Context;
import android.graphics.Rect;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.PopupWindow;

abstract class MenuPopup implements ShowableListMenu, MenuPresenter, AdapterView.OnItemClickListener {
    private Rect mEpicenterBounds;

    MenuPopup() {
    }

    protected static int measureIndividualMenuWidth(ListAdapter adapter, ViewGroup parent, Context context, int maxAllowedWidth) {
        int i = 0;
        View view = null;
        int i2 = 0;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
        int count = adapter.getCount();
        for (int i3 = 0; i3 < count; i3++) {
            int itemViewType = adapter.getItemViewType(i3);
            if (itemViewType != i2) {
                i2 = itemViewType;
                view = null;
            }
            if (parent == null) {
                parent = new FrameLayout(context);
            }
            view = adapter.getView(i3, view, parent);
            view.measure(makeMeasureSpec, makeMeasureSpec2);
            int measuredWidth = view.getMeasuredWidth();
            if (measuredWidth >= maxAllowedWidth) {
                return maxAllowedWidth;
            }
            if (measuredWidth > i) {
                i = measuredWidth;
            }
        }
        return i;
    }

    protected static boolean shouldPreserveIconSpacing(MenuBuilder menu) {
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            MenuItem item = menu.getItem(i);
            if (item.isVisible() && item.getIcon() != null) {
                return true;
            }
        }
        return false;
    }

    protected static MenuAdapter toMenuAdapter(ListAdapter adapter) {
        return adapter instanceof HeaderViewListAdapter ? (MenuAdapter) ((HeaderViewListAdapter) adapter).getWrappedAdapter() : (MenuAdapter) adapter;
    }

    public abstract void addMenu(MenuBuilder menuBuilder);

    /* access modifiers changed from: protected */
    public boolean closeMenuOnSubMenuOpened() {
        return true;
    }

    public boolean collapseItemActionView(MenuBuilder menu, MenuItemImpl item) {
        return false;
    }

    public boolean expandItemActionView(MenuBuilder menu, MenuItemImpl item) {
        return false;
    }

    public Rect getEpicenterBounds() {
        return this.mEpicenterBounds;
    }

    public int getId() {
        return 0;
    }

    public MenuView getMenuView(ViewGroup root) {
        throw new UnsupportedOperationException("MenuPopups manage their own views");
    }

    public void initForMenu(Context context, MenuBuilder menu) {
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        ListAdapter listAdapter = (ListAdapter) adapterView.getAdapter();
        toMenuAdapter(listAdapter).mAdapterMenu.performItemAction((MenuItem) listAdapter.getItem(position), this, closeMenuOnSubMenuOpened() ? 0 : 4);
    }

    public abstract void setAnchorView(View view);

    public void setEpicenterBounds(Rect bounds) {
        this.mEpicenterBounds = bounds;
    }

    public abstract void setForceShowIcon(boolean z);

    public abstract void setGravity(int i);

    public abstract void setHorizontalOffset(int i);

    public abstract void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener);

    public abstract void setShowTitle(boolean z);

    public abstract void setVerticalOffset(int i);
}

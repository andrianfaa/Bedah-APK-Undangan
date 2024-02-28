package androidx.appcompat.view.menu;

import android.content.DialogInterface;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.R;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.core.view.PointerIconCompat;

class MenuDialogHelper implements DialogInterface.OnKeyListener, DialogInterface.OnClickListener, DialogInterface.OnDismissListener, MenuPresenter.Callback {
    private AlertDialog mDialog;
    private MenuBuilder mMenu;
    ListMenuPresenter mPresenter;
    private MenuPresenter.Callback mPresenterCallback;

    public MenuDialogHelper(MenuBuilder menu) {
        this.mMenu = menu;
    }

    public void dismiss() {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public void onClick(DialogInterface dialog, int which) {
        this.mMenu.performItemAction((MenuItemImpl) this.mPresenter.getAdapter().getItem(which), 0);
    }

    public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        if (allMenusAreClosing || menu == this.mMenu) {
            dismiss();
        }
        MenuPresenter.Callback callback = this.mPresenterCallback;
        if (callback != null) {
            callback.onCloseMenu(menu, allMenusAreClosing);
        }
    }

    public void onDismiss(DialogInterface dialog) {
        this.mPresenter.onCloseMenu(this.mMenu, true);
    }

    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        Window window;
        View decorView;
        KeyEvent.DispatcherState keyDispatcherState;
        View decorView2;
        KeyEvent.DispatcherState keyDispatcherState2;
        if (keyCode == 82 || keyCode == 4) {
            if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                Window window2 = this.mDialog.getWindow();
                if (!(window2 == null || (decorView2 = window2.getDecorView()) == null || (keyDispatcherState2 = decorView2.getKeyDispatcherState()) == null)) {
                    keyDispatcherState2.startTracking(event, this);
                    return true;
                }
            } else if (event.getAction() == 1 && !event.isCanceled() && (window = this.mDialog.getWindow()) != null && (decorView = window.getDecorView()) != null && (keyDispatcherState = decorView.getKeyDispatcherState()) != null && keyDispatcherState.isTracking(event)) {
                this.mMenu.close(true);
                dialog.dismiss();
                return true;
            }
        }
        return this.mMenu.performShortcut(keyCode, event, 0);
    }

    public boolean onOpenSubMenu(MenuBuilder subMenu) {
        MenuPresenter.Callback callback = this.mPresenterCallback;
        if (callback != null) {
            return callback.onOpenSubMenu(subMenu);
        }
        return false;
    }

    public void setPresenterCallback(MenuPresenter.Callback cb) {
        this.mPresenterCallback = cb;
    }

    public void show(IBinder windowToken) {
        MenuBuilder menuBuilder = this.mMenu;
        AlertDialog.Builder builder = new AlertDialog.Builder(menuBuilder.getContext());
        ListMenuPresenter listMenuPresenter = new ListMenuPresenter(builder.getContext(), R.layout.abc_list_menu_item_layout);
        this.mPresenter = listMenuPresenter;
        listMenuPresenter.setCallback(this);
        this.mMenu.addMenuPresenter(this.mPresenter);
        builder.setAdapter(this.mPresenter.getAdapter(), this);
        View headerView = menuBuilder.getHeaderView();
        if (headerView != null) {
            builder.setCustomTitle(headerView);
        } else {
            builder.setIcon(menuBuilder.getHeaderIcon()).setTitle(menuBuilder.getHeaderTitle());
        }
        builder.setOnKeyListener(this);
        AlertDialog create = builder.create();
        this.mDialog = create;
        create.setOnDismissListener(this);
        WindowManager.LayoutParams attributes = this.mDialog.getWindow().getAttributes();
        attributes.type = PointerIconCompat.TYPE_HELP;
        if (windowToken != null) {
            attributes.token = windowToken;
        }
        attributes.flags |= 131072;
        this.mDialog.show();
    }
}

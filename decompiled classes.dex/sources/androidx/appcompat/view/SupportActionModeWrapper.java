package androidx.appcompat.view;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.menu.MenuItemWrapperICS;
import androidx.appcompat.view.menu.MenuWrapperICS;
import androidx.collection.SimpleArrayMap;
import androidx.core.internal.view.SupportMenu;
import androidx.core.internal.view.SupportMenuItem;
import java.util.ArrayList;

public class SupportActionModeWrapper extends ActionMode {
    final Context mContext;
    final ActionMode mWrappedObject;

    public static class CallbackWrapper implements ActionMode.Callback {
        final ArrayList<SupportActionModeWrapper> mActionModes = new ArrayList<>();
        final Context mContext;
        final SimpleArrayMap<Menu, Menu> mMenus = new SimpleArrayMap<>();
        final ActionMode.Callback mWrappedCallback;

        public CallbackWrapper(Context context, ActionMode.Callback supportCallback) {
            this.mContext = context;
            this.mWrappedCallback = supportCallback;
        }

        private Menu getMenuWrapper(Menu menu) {
            Menu menu2 = this.mMenus.get(menu);
            if (menu2 != null) {
                return menu2;
            }
            MenuWrapperICS menuWrapperICS = new MenuWrapperICS(this.mContext, (SupportMenu) menu);
            this.mMenus.put(menu, menuWrapperICS);
            return menuWrapperICS;
        }

        public android.view.ActionMode getActionModeWrapper(ActionMode mode) {
            int size = this.mActionModes.size();
            for (int i = 0; i < size; i++) {
                SupportActionModeWrapper supportActionModeWrapper = this.mActionModes.get(i);
                if (supportActionModeWrapper != null && supportActionModeWrapper.mWrappedObject == mode) {
                    return supportActionModeWrapper;
                }
            }
            SupportActionModeWrapper supportActionModeWrapper2 = new SupportActionModeWrapper(this.mContext, mode);
            this.mActionModes.add(supportActionModeWrapper2);
            return supportActionModeWrapper2;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return this.mWrappedCallback.onActionItemClicked(getActionModeWrapper(mode), new MenuItemWrapperICS(this.mContext, (SupportMenuItem) item));
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return this.mWrappedCallback.onCreateActionMode(getActionModeWrapper(mode), getMenuWrapper(menu));
        }

        public void onDestroyActionMode(ActionMode mode) {
            this.mWrappedCallback.onDestroyActionMode(getActionModeWrapper(mode));
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return this.mWrappedCallback.onPrepareActionMode(getActionModeWrapper(mode), getMenuWrapper(menu));
        }
    }

    public SupportActionModeWrapper(Context context, ActionMode supportActionMode) {
        this.mContext = context;
        this.mWrappedObject = supportActionMode;
    }

    public void finish() {
        this.mWrappedObject.finish();
    }

    public View getCustomView() {
        return this.mWrappedObject.getCustomView();
    }

    public Menu getMenu() {
        return new MenuWrapperICS(this.mContext, (SupportMenu) this.mWrappedObject.getMenu());
    }

    public MenuInflater getMenuInflater() {
        return this.mWrappedObject.getMenuInflater();
    }

    public CharSequence getSubtitle() {
        return this.mWrappedObject.getSubtitle();
    }

    public Object getTag() {
        return this.mWrappedObject.getTag();
    }

    public CharSequence getTitle() {
        return this.mWrappedObject.getTitle();
    }

    public boolean getTitleOptionalHint() {
        return this.mWrappedObject.getTitleOptionalHint();
    }

    public void invalidate() {
        this.mWrappedObject.invalidate();
    }

    public boolean isTitleOptional() {
        return this.mWrappedObject.isTitleOptional();
    }

    public void setCustomView(View view) {
        this.mWrappedObject.setCustomView(view);
    }

    public void setSubtitle(int resId) {
        this.mWrappedObject.setSubtitle(resId);
    }

    public void setSubtitle(CharSequence subtitle) {
        this.mWrappedObject.setSubtitle(subtitle);
    }

    public void setTag(Object tag) {
        this.mWrappedObject.setTag(tag);
    }

    public void setTitle(int resId) {
        this.mWrappedObject.setTitle(resId);
    }

    public void setTitle(CharSequence title) {
        this.mWrappedObject.setTitle(title);
    }

    public void setTitleOptionalHint(boolean titleOptional) {
        this.mWrappedObject.setTitleOptionalHint(titleOptional);
    }
}

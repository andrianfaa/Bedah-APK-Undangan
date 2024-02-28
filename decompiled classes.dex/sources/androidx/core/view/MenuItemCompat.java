package androidx.core.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.core.internal.view.SupportMenuItem;

public final class MenuItemCompat {
    @Deprecated
    public static final int SHOW_AS_ACTION_ALWAYS = 2;
    @Deprecated
    public static final int SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW = 8;
    @Deprecated
    public static final int SHOW_AS_ACTION_IF_ROOM = 1;
    @Deprecated
    public static final int SHOW_AS_ACTION_NEVER = 0;
    @Deprecated
    public static final int SHOW_AS_ACTION_WITH_TEXT = 4;
    private static final String TAG = "MenuItemCompat";

    static class Api26Impl {
        private Api26Impl() {
        }

        static int getAlphabeticModifiers(MenuItem menuItem) {
            return menuItem.getAlphabeticModifiers();
        }

        static CharSequence getContentDescription(MenuItem menuItem) {
            return menuItem.getContentDescription();
        }

        static ColorStateList getIconTintList(MenuItem menuItem) {
            return menuItem.getIconTintList();
        }

        static PorterDuff.Mode getIconTintMode(MenuItem menuItem) {
            return menuItem.getIconTintMode();
        }

        static int getNumericModifiers(MenuItem menuItem) {
            return menuItem.getNumericModifiers();
        }

        static CharSequence getTooltipText(MenuItem menuItem) {
            return menuItem.getTooltipText();
        }

        static MenuItem setAlphabeticShortcut(MenuItem menuItem, char alphaChar, int alphaModifiers) {
            return menuItem.setAlphabeticShortcut(alphaChar, alphaModifiers);
        }

        static MenuItem setContentDescription(MenuItem menuItem, CharSequence contentDescription) {
            return menuItem.setContentDescription(contentDescription);
        }

        static MenuItem setIconTintList(MenuItem menuItem, ColorStateList tint) {
            return menuItem.setIconTintList(tint);
        }

        static MenuItem setIconTintMode(MenuItem menuItem, PorterDuff.Mode tintMode) {
            return menuItem.setIconTintMode(tintMode);
        }

        static MenuItem setNumericShortcut(MenuItem menuItem, char numericChar, int numericModifiers) {
            return menuItem.setNumericShortcut(numericChar, numericModifiers);
        }

        static MenuItem setShortcut(MenuItem menuItem, char numericChar, char alphaChar, int numericModifiers, int alphaModifiers) {
            return menuItem.setShortcut(numericChar, alphaChar, numericModifiers, alphaModifiers);
        }

        static MenuItem setTooltipText(MenuItem menuItem, CharSequence tooltipText) {
            return menuItem.setTooltipText(tooltipText);
        }
    }

    @Deprecated
    public interface OnActionExpandListener {
        boolean onMenuItemActionCollapse(MenuItem menuItem);

        boolean onMenuItemActionExpand(MenuItem menuItem);
    }

    private MenuItemCompat() {
    }

    @Deprecated
    public static boolean collapseActionView(MenuItem item) {
        return item.collapseActionView();
    }

    @Deprecated
    public static boolean expandActionView(MenuItem item) {
        return item.expandActionView();
    }

    public static ActionProvider getActionProvider(MenuItem item) {
        if (item instanceof SupportMenuItem) {
            return ((SupportMenuItem) item).getSupportActionProvider();
        }
        Log.w(TAG, "getActionProvider: item does not implement SupportMenuItem; returning null");
        return null;
    }

    @Deprecated
    public static View getActionView(MenuItem item) {
        return item.getActionView();
    }

    public static int getAlphabeticModifiers(MenuItem item) {
        if (item instanceof SupportMenuItem) {
            return ((SupportMenuItem) item).getAlphabeticModifiers();
        }
        if (Build.VERSION.SDK_INT >= 26) {
            return Api26Impl.getAlphabeticModifiers(item);
        }
        return 0;
    }

    public static CharSequence getContentDescription(MenuItem item) {
        if (item instanceof SupportMenuItem) {
            return ((SupportMenuItem) item).getContentDescription();
        }
        if (Build.VERSION.SDK_INT >= 26) {
            return Api26Impl.getContentDescription(item);
        }
        return null;
    }

    public static ColorStateList getIconTintList(MenuItem item) {
        if (item instanceof SupportMenuItem) {
            return ((SupportMenuItem) item).getIconTintList();
        }
        if (Build.VERSION.SDK_INT >= 26) {
            return Api26Impl.getIconTintList(item);
        }
        return null;
    }

    public static PorterDuff.Mode getIconTintMode(MenuItem item) {
        if (item instanceof SupportMenuItem) {
            return ((SupportMenuItem) item).getIconTintMode();
        }
        if (Build.VERSION.SDK_INT >= 26) {
            return Api26Impl.getIconTintMode(item);
        }
        return null;
    }

    public static int getNumericModifiers(MenuItem item) {
        if (item instanceof SupportMenuItem) {
            return ((SupportMenuItem) item).getNumericModifiers();
        }
        if (Build.VERSION.SDK_INT >= 26) {
            return Api26Impl.getNumericModifiers(item);
        }
        return 0;
    }

    public static CharSequence getTooltipText(MenuItem item) {
        if (item instanceof SupportMenuItem) {
            return ((SupportMenuItem) item).getTooltipText();
        }
        if (Build.VERSION.SDK_INT >= 26) {
            return Api26Impl.getTooltipText(item);
        }
        return null;
    }

    @Deprecated
    public static boolean isActionViewExpanded(MenuItem item) {
        return item.isActionViewExpanded();
    }

    public static MenuItem setActionProvider(MenuItem item, ActionProvider provider) {
        if (item instanceof SupportMenuItem) {
            return ((SupportMenuItem) item).setSupportActionProvider(provider);
        }
        Log.w(TAG, "setActionProvider: item does not implement SupportMenuItem; ignoring");
        return item;
    }

    @Deprecated
    public static MenuItem setActionView(MenuItem item, int resId) {
        return item.setActionView(resId);
    }

    @Deprecated
    public static MenuItem setActionView(MenuItem item, View view) {
        return item.setActionView(view);
    }

    public static void setAlphabeticShortcut(MenuItem item, char alphaChar, int alphaModifiers) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setAlphabeticShortcut(alphaChar, alphaModifiers);
        } else if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.setAlphabeticShortcut(item, alphaChar, alphaModifiers);
        }
    }

    public static void setContentDescription(MenuItem item, CharSequence contentDescription) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setContentDescription(contentDescription);
        } else if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.setContentDescription(item, contentDescription);
        }
    }

    public static void setIconTintList(MenuItem item, ColorStateList tint) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setIconTintList(tint);
        } else if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.setIconTintList(item, tint);
        }
    }

    public static void setIconTintMode(MenuItem item, PorterDuff.Mode tintMode) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setIconTintMode(tintMode);
        } else if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.setIconTintMode(item, tintMode);
        }
    }

    public static void setNumericShortcut(MenuItem item, char numericChar, int numericModifiers) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setNumericShortcut(numericChar, numericModifiers);
        } else if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.setNumericShortcut(item, numericChar, numericModifiers);
        }
    }

    @Deprecated
    public static MenuItem setOnActionExpandListener(MenuItem item, final OnActionExpandListener listener) {
        return item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return OnActionExpandListener.this.onMenuItemActionCollapse(item);
            }

            public boolean onMenuItemActionExpand(MenuItem item) {
                return OnActionExpandListener.this.onMenuItemActionExpand(item);
            }
        });
    }

    public static void setShortcut(MenuItem item, char numericChar, char alphaChar, int numericModifiers, int alphaModifiers) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setShortcut(numericChar, alphaChar, numericModifiers, alphaModifiers);
        } else if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.setShortcut(item, numericChar, alphaChar, numericModifiers, alphaModifiers);
        }
    }

    @Deprecated
    public static void setShowAsAction(MenuItem item, int actionEnum) {
        item.setShowAsAction(actionEnum);
    }

    public static void setTooltipText(MenuItem item, CharSequence tooltipText) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setTooltipText(tooltipText);
        } else if (Build.VERSION.SDK_INT >= 26) {
            Api26Impl.setTooltipText(item, tooltipText);
        }
    }
}

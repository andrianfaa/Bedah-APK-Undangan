package androidx.appcompat.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.ActionBarDrawerToggleHoneycomb;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class ActionBarDrawerToggle implements DrawerLayout.DrawerListener {
    private final Delegate mActivityImpl;
    private final int mCloseDrawerContentDescRes;
    boolean mDrawerIndicatorEnabled;
    private final DrawerLayout mDrawerLayout;
    private boolean mDrawerSlideAnimationEnabled;
    private boolean mHasCustomUpIndicator;
    private Drawable mHomeAsUpIndicator;
    private final int mOpenDrawerContentDescRes;
    private DrawerArrowDrawable mSlider;
    View.OnClickListener mToolbarNavigationClickListener;
    private boolean mWarnedForDisplayHomeAsUp;

    public interface Delegate {
        Context getActionBarThemedContext();

        Drawable getThemeUpIndicator();

        boolean isNavigationVisible();

        void setActionBarDescription(int i);

        void setActionBarUpIndicator(Drawable drawable, int i);
    }

    public interface DelegateProvider {
        Delegate getDrawerToggleDelegate();
    }

    private static class FrameworkActionBarDelegate implements Delegate {
        private final Activity mActivity;
        private ActionBarDrawerToggleHoneycomb.SetIndicatorInfo mSetIndicatorInfo;

        static class Api18Impl {
            private Api18Impl() {
            }

            static void setHomeActionContentDescription(ActionBar actionBar, int resId) {
                actionBar.setHomeActionContentDescription(resId);
            }

            static void setHomeAsUpIndicator(ActionBar actionBar, Drawable indicator) {
                actionBar.setHomeAsUpIndicator(indicator);
            }
        }

        FrameworkActionBarDelegate(Activity activity) {
            this.mActivity = activity;
        }

        public Context getActionBarThemedContext() {
            ActionBar actionBar = this.mActivity.getActionBar();
            return actionBar != null ? actionBar.getThemedContext() : this.mActivity;
        }

        public Drawable getThemeUpIndicator() {
            if (Build.VERSION.SDK_INT < 18) {
                return ActionBarDrawerToggleHoneycomb.getThemeUpIndicator(this.mActivity);
            }
            TypedArray obtainStyledAttributes = getActionBarThemedContext().obtainStyledAttributes((AttributeSet) null, new int[]{16843531}, 16843470, 0);
            Drawable drawable = obtainStyledAttributes.getDrawable(0);
            obtainStyledAttributes.recycle();
            return drawable;
        }

        public boolean isNavigationVisible() {
            ActionBar actionBar = this.mActivity.getActionBar();
            return (actionBar == null || (actionBar.getDisplayOptions() & 4) == 0) ? false : true;
        }

        public void setActionBarDescription(int contentDescRes) {
            if (Build.VERSION.SDK_INT >= 18) {
                ActionBar actionBar = this.mActivity.getActionBar();
                if (actionBar != null) {
                    Api18Impl.setHomeActionContentDescription(actionBar, contentDescRes);
                    return;
                }
                return;
            }
            this.mSetIndicatorInfo = ActionBarDrawerToggleHoneycomb.setActionBarDescription(this.mSetIndicatorInfo, this.mActivity, contentDescRes);
        }

        public void setActionBarUpIndicator(Drawable themeImage, int contentDescRes) {
            ActionBar actionBar = this.mActivity.getActionBar();
            if (actionBar == null) {
                return;
            }
            if (Build.VERSION.SDK_INT >= 18) {
                Api18Impl.setHomeAsUpIndicator(actionBar, themeImage);
                Api18Impl.setHomeActionContentDescription(actionBar, contentDescRes);
                return;
            }
            actionBar.setDisplayShowHomeEnabled(true);
            this.mSetIndicatorInfo = ActionBarDrawerToggleHoneycomb.setActionBarUpIndicator(this.mActivity, themeImage, contentDescRes);
            actionBar.setDisplayShowHomeEnabled(false);
        }
    }

    static class ToolbarCompatDelegate implements Delegate {
        final CharSequence mDefaultContentDescription;
        final Drawable mDefaultUpIndicator;
        final Toolbar mToolbar;

        ToolbarCompatDelegate(Toolbar toolbar) {
            this.mToolbar = toolbar;
            this.mDefaultUpIndicator = toolbar.getNavigationIcon();
            this.mDefaultContentDescription = toolbar.getNavigationContentDescription();
        }

        public Context getActionBarThemedContext() {
            return this.mToolbar.getContext();
        }

        public Drawable getThemeUpIndicator() {
            return this.mDefaultUpIndicator;
        }

        public boolean isNavigationVisible() {
            return true;
        }

        public void setActionBarDescription(int contentDescRes) {
            if (contentDescRes == 0) {
                this.mToolbar.setNavigationContentDescription(this.mDefaultContentDescription);
            } else {
                this.mToolbar.setNavigationContentDescription(contentDescRes);
            }
        }

        public void setActionBarUpIndicator(Drawable upDrawable, int contentDescRes) {
            this.mToolbar.setNavigationIcon(upDrawable);
            setActionBarDescription(contentDescRes);
        }
    }

    ActionBarDrawerToggle(Activity activity, Toolbar toolbar, DrawerLayout drawerLayout, DrawerArrowDrawable slider, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        this.mDrawerSlideAnimationEnabled = true;
        this.mDrawerIndicatorEnabled = true;
        this.mWarnedForDisplayHomeAsUp = false;
        if (toolbar != null) {
            this.mActivityImpl = new ToolbarCompatDelegate(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (ActionBarDrawerToggle.this.mDrawerIndicatorEnabled) {
                        ActionBarDrawerToggle.this.toggle();
                    } else if (ActionBarDrawerToggle.this.mToolbarNavigationClickListener != null) {
                        ActionBarDrawerToggle.this.mToolbarNavigationClickListener.onClick(v);
                    }
                }
            });
        } else if (activity instanceof DelegateProvider) {
            this.mActivityImpl = ((DelegateProvider) activity).getDrawerToggleDelegate();
        } else {
            this.mActivityImpl = new FrameworkActionBarDelegate(activity);
        }
        this.mDrawerLayout = drawerLayout;
        this.mOpenDrawerContentDescRes = openDrawerContentDescRes;
        this.mCloseDrawerContentDescRes = closeDrawerContentDescRes;
        if (slider == null) {
            this.mSlider = new DrawerArrowDrawable(this.mActivityImpl.getActionBarThemedContext());
        } else {
            this.mSlider = slider;
        }
        this.mHomeAsUpIndicator = getThemeUpIndicator();
    }

    public ActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        this(activity, (Toolbar) null, drawerLayout, (DrawerArrowDrawable) null, openDrawerContentDescRes, closeDrawerContentDescRes);
    }

    public ActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        this(activity, toolbar, drawerLayout, (DrawerArrowDrawable) null, openDrawerContentDescRes, closeDrawerContentDescRes);
    }

    private void setPosition(float position) {
        if (position == 1.0f) {
            this.mSlider.setVerticalMirror(true);
        } else if (position == 0.0f) {
            this.mSlider.setVerticalMirror(false);
        }
        this.mSlider.setProgress(position);
    }

    public DrawerArrowDrawable getDrawerArrowDrawable() {
        return this.mSlider;
    }

    /* access modifiers changed from: package-private */
    public Drawable getThemeUpIndicator() {
        return this.mActivityImpl.getThemeUpIndicator();
    }

    public View.OnClickListener getToolbarNavigationClickListener() {
        return this.mToolbarNavigationClickListener;
    }

    public boolean isDrawerIndicatorEnabled() {
        return this.mDrawerIndicatorEnabled;
    }

    public boolean isDrawerSlideAnimationEnabled() {
        return this.mDrawerSlideAnimationEnabled;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (!this.mHasCustomUpIndicator) {
            this.mHomeAsUpIndicator = getThemeUpIndicator();
        }
        syncState();
    }

    public void onDrawerClosed(View drawerView) {
        setPosition(0.0f);
        if (this.mDrawerIndicatorEnabled) {
            setActionBarDescription(this.mOpenDrawerContentDescRes);
        }
    }

    public void onDrawerOpened(View drawerView) {
        setPosition(1.0f);
        if (this.mDrawerIndicatorEnabled) {
            setActionBarDescription(this.mCloseDrawerContentDescRes);
        }
    }

    public void onDrawerSlide(View drawerView, float slideOffset) {
        if (this.mDrawerSlideAnimationEnabled) {
            setPosition(Math.min(1.0f, Math.max(0.0f, slideOffset)));
        } else {
            setPosition(0.0f);
        }
    }

    public void onDrawerStateChanged(int newState) {
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == null || item.getItemId() != 16908332 || !this.mDrawerIndicatorEnabled) {
            return false;
        }
        toggle();
        return true;
    }

    /* access modifiers changed from: package-private */
    public void setActionBarDescription(int contentDescRes) {
        this.mActivityImpl.setActionBarDescription(contentDescRes);
    }

    /* access modifiers changed from: package-private */
    public void setActionBarUpIndicator(Drawable upDrawable, int contentDescRes) {
        if (!this.mWarnedForDisplayHomeAsUp && !this.mActivityImpl.isNavigationVisible()) {
            Log.w("ActionBarDrawerToggle", "DrawerToggle may not show up because NavigationIcon is not visible. You may need to call actionbar.setDisplayHomeAsUpEnabled(true);");
            this.mWarnedForDisplayHomeAsUp = true;
        }
        this.mActivityImpl.setActionBarUpIndicator(upDrawable, contentDescRes);
    }

    public void setDrawerArrowDrawable(DrawerArrowDrawable drawable) {
        this.mSlider = drawable;
        syncState();
    }

    public void setDrawerIndicatorEnabled(boolean enable) {
        if (enable != this.mDrawerIndicatorEnabled) {
            if (enable) {
                setActionBarUpIndicator(this.mSlider, this.mDrawerLayout.isDrawerOpen((int) GravityCompat.START) ? this.mCloseDrawerContentDescRes : this.mOpenDrawerContentDescRes);
            } else {
                setActionBarUpIndicator(this.mHomeAsUpIndicator, 0);
            }
            this.mDrawerIndicatorEnabled = enable;
        }
    }

    public void setDrawerSlideAnimationEnabled(boolean enabled) {
        this.mDrawerSlideAnimationEnabled = enabled;
        if (!enabled) {
            setPosition(0.0f);
        }
    }

    public void setHomeAsUpIndicator(int resId) {
        Drawable drawable = null;
        if (resId != 0) {
            drawable = this.mDrawerLayout.getResources().getDrawable(resId);
        }
        setHomeAsUpIndicator(drawable);
    }

    public void setHomeAsUpIndicator(Drawable indicator) {
        if (indicator == null) {
            this.mHomeAsUpIndicator = getThemeUpIndicator();
            this.mHasCustomUpIndicator = false;
        } else {
            this.mHomeAsUpIndicator = indicator;
            this.mHasCustomUpIndicator = true;
        }
        if (!this.mDrawerIndicatorEnabled) {
            setActionBarUpIndicator(this.mHomeAsUpIndicator, 0);
        }
    }

    public void setToolbarNavigationClickListener(View.OnClickListener onToolbarNavigationClickListener) {
        this.mToolbarNavigationClickListener = onToolbarNavigationClickListener;
    }

    public void syncState() {
        if (this.mDrawerLayout.isDrawerOpen((int) GravityCompat.START)) {
            setPosition(1.0f);
        } else {
            setPosition(0.0f);
        }
        if (this.mDrawerIndicatorEnabled) {
            setActionBarUpIndicator(this.mSlider, this.mDrawerLayout.isDrawerOpen((int) GravityCompat.START) ? this.mCloseDrawerContentDescRes : this.mOpenDrawerContentDescRes);
        }
    }

    /* access modifiers changed from: package-private */
    public void toggle() {
        int drawerLockMode = this.mDrawerLayout.getDrawerLockMode((int) GravityCompat.START);
        if (this.mDrawerLayout.isDrawerVisible((int) GravityCompat.START) && drawerLockMode != 2) {
            this.mDrawerLayout.closeDrawer((int) GravityCompat.START);
        } else if (drawerLockMode != 1) {
            this.mDrawerLayout.openDrawer((int) GravityCompat.START);
        }
    }
}

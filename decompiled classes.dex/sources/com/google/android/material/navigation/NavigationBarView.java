package com.google.android.material.navigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class NavigationBarView extends FrameLayout {
    public static final int LABEL_VISIBILITY_AUTO = -1;
    public static final int LABEL_VISIBILITY_LABELED = 1;
    public static final int LABEL_VISIBILITY_SELECTED = 0;
    public static final int LABEL_VISIBILITY_UNLABELED = 2;
    private static final int MENU_PRESENTER_ID = 1;
    private ColorStateList itemRippleColor;
    private final NavigationBarMenu menu;
    private MenuInflater menuInflater;
    private final NavigationBarMenuView menuView;
    private final NavigationBarPresenter presenter;
    /* access modifiers changed from: private */
    public OnItemReselectedListener reselectedListener;
    /* access modifiers changed from: private */
    public OnItemSelectedListener selectedListener;

    @Retention(RetentionPolicy.SOURCE)
    public @interface LabelVisibility {
    }

    public interface OnItemReselectedListener {
        void onNavigationItemReselected(MenuItem menuItem);
    }

    public interface OnItemSelectedListener {
        boolean onNavigationItemSelected(MenuItem menuItem);
    }

    static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, (ClassLoader) null);
            }

            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        Bundle menuPresenterState;

        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            readFromParcel(source, loader == null ? getClass().getClassLoader() : loader);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private void readFromParcel(Parcel in, ClassLoader loader) {
            this.menuPresenterState = in.readBundle(loader);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBundle(this.menuPresenterState);
        }
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public NavigationBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(MaterialThemeOverlay.wrap(context, attrs, defStyleAttr, defStyleRes), attrs, defStyleAttr);
        NavigationBarPresenter navigationBarPresenter = new NavigationBarPresenter();
        this.presenter = navigationBarPresenter;
        Context context2 = getContext();
        TintTypedArray obtainTintedStyledAttributes = ThemeEnforcement.obtainTintedStyledAttributes(context2, attrs, R.styleable.NavigationBarView, defStyleAttr, defStyleRes, R.styleable.NavigationBarView_itemTextAppearanceInactive, R.styleable.NavigationBarView_itemTextAppearanceActive);
        NavigationBarMenu navigationBarMenu = new NavigationBarMenu(context2, getClass(), getMaxItemCount());
        this.menu = navigationBarMenu;
        NavigationBarMenuView createNavigationBarMenuView = createNavigationBarMenuView(context2);
        this.menuView = createNavigationBarMenuView;
        navigationBarPresenter.setMenuView(createNavigationBarMenuView);
        navigationBarPresenter.setId(1);
        createNavigationBarMenuView.setPresenter(navigationBarPresenter);
        navigationBarMenu.addMenuPresenter(navigationBarPresenter);
        navigationBarPresenter.initForMenu(getContext(), navigationBarMenu);
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationBarView_itemIconTint)) {
            createNavigationBarMenuView.setIconTintList(obtainTintedStyledAttributes.getColorStateList(R.styleable.NavigationBarView_itemIconTint));
        } else {
            createNavigationBarMenuView.setIconTintList(createNavigationBarMenuView.createDefaultColorStateList(16842808));
        }
        setItemIconSize(obtainTintedStyledAttributes.getDimensionPixelSize(R.styleable.NavigationBarView_itemIconSize, getResources().getDimensionPixelSize(R.dimen.mtrl_navigation_bar_item_default_icon_size)));
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationBarView_itemTextAppearanceInactive)) {
            setItemTextAppearanceInactive(obtainTintedStyledAttributes.getResourceId(R.styleable.NavigationBarView_itemTextAppearanceInactive, 0));
        }
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationBarView_itemTextAppearanceActive)) {
            setItemTextAppearanceActive(obtainTintedStyledAttributes.getResourceId(R.styleable.NavigationBarView_itemTextAppearanceActive, 0));
        }
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationBarView_itemTextColor)) {
            setItemTextColor(obtainTintedStyledAttributes.getColorStateList(R.styleable.NavigationBarView_itemTextColor));
        }
        if (getBackground() == null || (getBackground() instanceof ColorDrawable)) {
            ViewCompat.setBackground(this, createMaterialShapeDrawableBackground(context2));
        }
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationBarView_itemPaddingTop)) {
            setItemPaddingTop(obtainTintedStyledAttributes.getDimensionPixelSize(R.styleable.NavigationBarView_itemPaddingTop, 0));
        }
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationBarView_itemPaddingBottom)) {
            setItemPaddingBottom(obtainTintedStyledAttributes.getDimensionPixelSize(R.styleable.NavigationBarView_itemPaddingBottom, 0));
        }
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationBarView_elevation)) {
            setElevation((float) obtainTintedStyledAttributes.getDimensionPixelSize(R.styleable.NavigationBarView_elevation, 0));
        }
        ColorStateList colorStateList = MaterialResources.getColorStateList(context2, obtainTintedStyledAttributes, R.styleable.NavigationBarView_backgroundTint);
        DrawableCompat.setTintList(getBackground().mutate(), colorStateList);
        setLabelVisibilityMode(obtainTintedStyledAttributes.getInteger(R.styleable.NavigationBarView_labelVisibilityMode, -1));
        int resourceId = obtainTintedStyledAttributes.getResourceId(R.styleable.NavigationBarView_itemBackground, 0);
        if (resourceId != 0) {
            createNavigationBarMenuView.setItemBackgroundRes(resourceId);
        } else {
            setItemRippleColor(MaterialResources.getColorStateList(context2, obtainTintedStyledAttributes, R.styleable.NavigationBarView_itemRippleColor));
        }
        int resourceId2 = obtainTintedStyledAttributes.getResourceId(R.styleable.NavigationBarView_itemActiveIndicatorStyle, 0);
        if (resourceId2 != 0) {
            setItemActiveIndicatorEnabled(true);
            TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(resourceId2, R.styleable.NavigationBarActiveIndicator);
            setItemActiveIndicatorWidth(obtainStyledAttributes.getDimensionPixelSize(R.styleable.NavigationBarActiveIndicator_android_width, 0));
            setItemActiveIndicatorHeight(obtainStyledAttributes.getDimensionPixelSize(R.styleable.NavigationBarActiveIndicator_android_height, 0));
            setItemActiveIndicatorMarginHorizontal(obtainStyledAttributes.getDimensionPixelOffset(R.styleable.NavigationBarActiveIndicator_marginHorizontal, 0));
            setItemActiveIndicatorColor(MaterialResources.getColorStateList(context2, obtainStyledAttributes, R.styleable.NavigationBarActiveIndicator_android_color));
            ColorStateList colorStateList2 = colorStateList;
            setItemActiveIndicatorShapeAppearance(ShapeAppearanceModel.builder(context2, obtainStyledAttributes.getResourceId(R.styleable.NavigationBarActiveIndicator_shapeAppearance, 0), 0).build());
            obtainStyledAttributes.recycle();
        } else {
            ColorStateList colorStateList3 = colorStateList;
        }
        if (obtainTintedStyledAttributes.hasValue(R.styleable.NavigationBarView_menu)) {
            inflateMenu(obtainTintedStyledAttributes.getResourceId(R.styleable.NavigationBarView_menu, 0));
        }
        obtainTintedStyledAttributes.recycle();
        addView(createNavigationBarMenuView);
        navigationBarMenu.setCallback(new MenuBuilder.Callback() {
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                if (NavigationBarView.this.reselectedListener == null || item.getItemId() != NavigationBarView.this.getSelectedItemId()) {
                    return NavigationBarView.this.selectedListener != null && !NavigationBarView.this.selectedListener.onNavigationItemSelected(item);
                }
                NavigationBarView.this.reselectedListener.onNavigationItemReselected(item);
                return true;
            }

            public void onMenuModeChange(MenuBuilder menu) {
            }
        });
    }

    private MaterialShapeDrawable createMaterialShapeDrawableBackground(Context context) {
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        Drawable background = getBackground();
        if (background instanceof ColorDrawable) {
            materialShapeDrawable.setFillColor(ColorStateList.valueOf(((ColorDrawable) background).getColor()));
        }
        materialShapeDrawable.initializeElevationOverlay(context);
        return materialShapeDrawable;
    }

    private MenuInflater getMenuInflater() {
        if (this.menuInflater == null) {
            this.menuInflater = new SupportMenuInflater(getContext());
        }
        return this.menuInflater;
    }

    /* access modifiers changed from: protected */
    public abstract NavigationBarMenuView createNavigationBarMenuView(Context context);

    public BadgeDrawable getBadge(int menuItemId) {
        return this.menuView.getBadge(menuItemId);
    }

    public ColorStateList getItemActiveIndicatorColor() {
        return this.menuView.getItemActiveIndicatorColor();
    }

    public int getItemActiveIndicatorHeight() {
        return this.menuView.getItemActiveIndicatorHeight();
    }

    public int getItemActiveIndicatorMarginHorizontal() {
        return this.menuView.getItemActiveIndicatorMarginHorizontal();
    }

    public ShapeAppearanceModel getItemActiveIndicatorShapeAppearance() {
        return this.menuView.getItemActiveIndicatorShapeAppearance();
    }

    public int getItemActiveIndicatorWidth() {
        return this.menuView.getItemActiveIndicatorWidth();
    }

    public Drawable getItemBackground() {
        return this.menuView.getItemBackground();
    }

    @Deprecated
    public int getItemBackgroundResource() {
        return this.menuView.getItemBackgroundRes();
    }

    public int getItemIconSize() {
        return this.menuView.getItemIconSize();
    }

    public ColorStateList getItemIconTintList() {
        return this.menuView.getIconTintList();
    }

    public int getItemPaddingBottom() {
        return this.menuView.getItemPaddingBottom();
    }

    public int getItemPaddingTop() {
        return this.menuView.getItemPaddingTop();
    }

    public ColorStateList getItemRippleColor() {
        return this.itemRippleColor;
    }

    public int getItemTextAppearanceActive() {
        return this.menuView.getItemTextAppearanceActive();
    }

    public int getItemTextAppearanceInactive() {
        return this.menuView.getItemTextAppearanceInactive();
    }

    public ColorStateList getItemTextColor() {
        return this.menuView.getItemTextColor();
    }

    public int getLabelVisibilityMode() {
        return this.menuView.getLabelVisibilityMode();
    }

    public abstract int getMaxItemCount();

    public Menu getMenu() {
        return this.menu;
    }

    public MenuView getMenuView() {
        return this.menuView;
    }

    public BadgeDrawable getOrCreateBadge(int menuItemId) {
        return this.menuView.getOrCreateBadge(menuItemId);
    }

    public NavigationBarPresenter getPresenter() {
        return this.presenter;
    }

    public int getSelectedItemId() {
        return this.menuView.getSelectedItemId();
    }

    public void inflateMenu(int resId) {
        this.presenter.setUpdateSuspended(true);
        getMenuInflater().inflate(resId, this.menu);
        this.presenter.setUpdateSuspended(false);
        this.presenter.updateMenuView(true);
    }

    public boolean isItemActiveIndicatorEnabled() {
        return this.menuView.getItemActiveIndicatorEnabled();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.setParentAbsoluteElevation(this);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.menu.restorePresenterStates(savedState.menuPresenterState);
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.menuPresenterState = new Bundle();
        this.menu.savePresenterStates(savedState.menuPresenterState);
        return savedState;
    }

    public void removeBadge(int menuItemId) {
        this.menuView.removeBadge(menuItemId);
    }

    public void setElevation(float elevation) {
        if (Build.VERSION.SDK_INT >= 21) {
            super.setElevation(elevation);
        }
        MaterialShapeUtils.setElevation(this, elevation);
    }

    public void setItemActiveIndicatorColor(ColorStateList csl) {
        this.menuView.setItemActiveIndicatorColor(csl);
    }

    public void setItemActiveIndicatorEnabled(boolean enabled) {
        this.menuView.setItemActiveIndicatorEnabled(enabled);
    }

    public void setItemActiveIndicatorHeight(int height) {
        this.menuView.setItemActiveIndicatorHeight(height);
    }

    public void setItemActiveIndicatorMarginHorizontal(int horizontalMargin) {
        this.menuView.setItemActiveIndicatorMarginHorizontal(horizontalMargin);
    }

    public void setItemActiveIndicatorShapeAppearance(ShapeAppearanceModel shapeAppearance) {
        this.menuView.setItemActiveIndicatorShapeAppearance(shapeAppearance);
    }

    public void setItemActiveIndicatorWidth(int width) {
        this.menuView.setItemActiveIndicatorWidth(width);
    }

    public void setItemBackground(Drawable background) {
        this.menuView.setItemBackground(background);
        this.itemRippleColor = null;
    }

    public void setItemBackgroundResource(int resId) {
        this.menuView.setItemBackgroundRes(resId);
        this.itemRippleColor = null;
    }

    public void setItemIconSize(int iconSize) {
        this.menuView.setItemIconSize(iconSize);
    }

    public void setItemIconSizeRes(int iconSizeRes) {
        setItemIconSize(getResources().getDimensionPixelSize(iconSizeRes));
    }

    public void setItemIconTintList(ColorStateList tint) {
        this.menuView.setIconTintList(tint);
    }

    public void setItemOnTouchListener(int menuItemId, View.OnTouchListener onTouchListener) {
        this.menuView.setItemOnTouchListener(menuItemId, onTouchListener);
    }

    public void setItemPaddingBottom(int paddingBottom) {
        this.menuView.setItemPaddingBottom(paddingBottom);
    }

    public void setItemPaddingTop(int paddingTop) {
        this.menuView.setItemPaddingTop(paddingTop);
    }

    public void setItemRippleColor(ColorStateList itemRippleColor2) {
        if (this.itemRippleColor != itemRippleColor2) {
            this.itemRippleColor = itemRippleColor2;
            if (itemRippleColor2 == null) {
                this.menuView.setItemBackground((Drawable) null);
                return;
            }
            ColorStateList convertToRippleDrawableColor = RippleUtils.convertToRippleDrawableColor(itemRippleColor2);
            if (Build.VERSION.SDK_INT >= 21) {
                this.menuView.setItemBackground(new RippleDrawable(convertToRippleDrawableColor, (Drawable) null, (Drawable) null));
                return;
            }
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadius(1.0E-5f);
            Drawable wrap = DrawableCompat.wrap(gradientDrawable);
            DrawableCompat.setTintList(wrap, convertToRippleDrawableColor);
            this.menuView.setItemBackground(wrap);
        } else if (itemRippleColor2 == null && this.menuView.getItemBackground() != null) {
            this.menuView.setItemBackground((Drawable) null);
        }
    }

    public void setItemTextAppearanceActive(int textAppearanceRes) {
        this.menuView.setItemTextAppearanceActive(textAppearanceRes);
    }

    public void setItemTextAppearanceInactive(int textAppearanceRes) {
        this.menuView.setItemTextAppearanceInactive(textAppearanceRes);
    }

    public void setItemTextColor(ColorStateList textColor) {
        this.menuView.setItemTextColor(textColor);
    }

    public void setLabelVisibilityMode(int labelVisibilityMode) {
        if (this.menuView.getLabelVisibilityMode() != labelVisibilityMode) {
            this.menuView.setLabelVisibilityMode(labelVisibilityMode);
            this.presenter.updateMenuView(false);
        }
    }

    public void setOnItemReselectedListener(OnItemReselectedListener listener) {
        this.reselectedListener = listener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.selectedListener = listener;
    }

    public void setSelectedItemId(int itemId) {
        MenuItem findItem = this.menu.findItem(itemId);
        if (findItem != null && !this.menu.performItemAction(findItem, this.presenter, 0)) {
            findItem.setChecked(true);
        }
    }
}

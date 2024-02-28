package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.CollapsibleActionView;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.SubMenuBuilder;
import androidx.appcompat.widget.ActionMenuView;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuHostHelper;
import androidx.core.view.MenuProvider;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Toolbar extends ViewGroup implements MenuHost {
    private static final String TAG = "Toolbar";
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    int mButtonGravity;
    ImageButton mCollapseButtonView;
    private CharSequence mCollapseDescription;
    private Drawable mCollapseIcon;
    private boolean mCollapsible;
    private int mContentInsetEndWithActions;
    private int mContentInsetStartWithNavigation;
    private RtlSpacingHelper mContentInsets;
    private boolean mEatingHover;
    private boolean mEatingTouch;
    View mExpandedActionView;
    private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private int mGravity;
    private final ArrayList<View> mHiddenViews;
    private ImageView mLogoView;
    private int mMaxButtonHeight;
    private MenuBuilder.Callback mMenuBuilderCallback;
    final MenuHostHelper mMenuHostHelper;
    private ActionMenuView mMenuView;
    private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener;
    private ImageButton mNavButtonView;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private ActionMenuPresenter mOuterActionMenuPresenter;
    private Context mPopupContext;
    private int mPopupTheme;
    private ArrayList<MenuItem> mProvidedMenuItems;
    private final Runnable mShowOverflowMenuRunnable;
    private CharSequence mSubtitleText;
    private int mSubtitleTextAppearance;
    private ColorStateList mSubtitleTextColor;
    private TextView mSubtitleTextView;
    private final int[] mTempMargins;
    private final ArrayList<View> mTempViews;
    private int mTitleMarginBottom;
    private int mTitleMarginEnd;
    private int mTitleMarginStart;
    private int mTitleMarginTop;
    private CharSequence mTitleText;
    private int mTitleTextAppearance;
    private ColorStateList mTitleTextColor;
    private TextView mTitleTextView;
    private ToolbarWidgetWrapper mWrapper;

    private class ExpandedActionViewMenuPresenter implements MenuPresenter {
        MenuItemImpl mCurrentExpandedItem;
        MenuBuilder mMenu;

        ExpandedActionViewMenuPresenter() {
        }

        public boolean collapseItemActionView(MenuBuilder menu, MenuItemImpl item) {
            if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView) Toolbar.this.mExpandedActionView).onActionViewCollapsed();
            }
            Toolbar toolbar = Toolbar.this;
            toolbar.removeView(toolbar.mExpandedActionView);
            Toolbar toolbar2 = Toolbar.this;
            toolbar2.removeView(toolbar2.mCollapseButtonView);
            Toolbar.this.mExpandedActionView = null;
            Toolbar.this.addChildrenForExpandedActionView();
            this.mCurrentExpandedItem = null;
            Toolbar.this.requestLayout();
            item.setActionViewExpanded(false);
            return true;
        }

        public boolean expandItemActionView(MenuBuilder menu, MenuItemImpl item) {
            Toolbar.this.ensureCollapseButtonView();
            ViewParent parent = Toolbar.this.mCollapseButtonView.getParent();
            Toolbar toolbar = Toolbar.this;
            if (parent != toolbar) {
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeView(toolbar.mCollapseButtonView);
                }
                Toolbar toolbar2 = Toolbar.this;
                toolbar2.addView(toolbar2.mCollapseButtonView);
            }
            Toolbar.this.mExpandedActionView = item.getActionView();
            this.mCurrentExpandedItem = item;
            ViewParent parent2 = Toolbar.this.mExpandedActionView.getParent();
            Toolbar toolbar3 = Toolbar.this;
            if (parent2 != toolbar3) {
                if (parent2 instanceof ViewGroup) {
                    ((ViewGroup) parent2).removeView(toolbar3.mExpandedActionView);
                }
                LayoutParams generateDefaultLayoutParams = Toolbar.this.generateDefaultLayoutParams();
                generateDefaultLayoutParams.gravity = 8388611 | (Toolbar.this.mButtonGravity & 112);
                generateDefaultLayoutParams.mViewType = 2;
                Toolbar.this.mExpandedActionView.setLayoutParams(generateDefaultLayoutParams);
                Toolbar toolbar4 = Toolbar.this;
                toolbar4.addView(toolbar4.mExpandedActionView);
            }
            Toolbar.this.removeChildrenForExpandedActionView();
            Toolbar.this.requestLayout();
            item.setActionViewExpanded(true);
            if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView) Toolbar.this.mExpandedActionView).onActionViewExpanded();
            }
            return true;
        }

        public boolean flagActionItems() {
            return false;
        }

        public int getId() {
            return 0;
        }

        public MenuView getMenuView(ViewGroup root) {
            return null;
        }

        public void initForMenu(Context context, MenuBuilder menu) {
            MenuItemImpl menuItemImpl;
            MenuBuilder menuBuilder = this.mMenu;
            if (!(menuBuilder == null || (menuItemImpl = this.mCurrentExpandedItem) == null)) {
                menuBuilder.collapseItemActionView(menuItemImpl);
            }
            this.mMenu = menu;
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        }

        public void onRestoreInstanceState(Parcelable state) {
        }

        public Parcelable onSaveInstanceState() {
            return null;
        }

        public boolean onSubMenuSelected(SubMenuBuilder subMenu) {
            return false;
        }

        public void setCallback(MenuPresenter.Callback cb) {
        }

        public void updateMenuView(boolean cleared) {
            if (this.mCurrentExpandedItem != null) {
                boolean z = false;
                MenuBuilder menuBuilder = this.mMenu;
                if (menuBuilder != null) {
                    int size = menuBuilder.size();
                    int i = 0;
                    while (true) {
                        if (i >= size) {
                            break;
                        } else if (this.mMenu.getItem(i) == this.mCurrentExpandedItem) {
                            z = true;
                            break;
                        } else {
                            i++;
                        }
                    }
                }
                if (!z) {
                    collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
                }
            }
        }
    }

    public static class LayoutParams extends ActionBar.LayoutParams {
        static final int CUSTOM = 0;
        static final int EXPANDED = 2;
        static final int SYSTEM = 1;
        int mViewType;

        public LayoutParams(int gravity) {
            this(-2, -1, gravity);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.mViewType = 0;
            this.gravity = 8388627;
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.mViewType = 0;
            this.gravity = gravity;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.mViewType = 0;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            this.mViewType = 0;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super((ViewGroup.LayoutParams) source);
            this.mViewType = 0;
            copyMarginsFromCompat(source);
        }

        public LayoutParams(ActionBar.LayoutParams source) {
            super(source);
            this.mViewType = 0;
        }

        public LayoutParams(LayoutParams source) {
            super((ActionBar.LayoutParams) source);
            this.mViewType = 0;
            this.mViewType = source.mViewType;
        }

        /* access modifiers changed from: package-private */
        public void copyMarginsFromCompat(ViewGroup.MarginLayoutParams source) {
            this.leftMargin = source.leftMargin;
            this.topMargin = source.topMargin;
            this.rightMargin = source.rightMargin;
            this.bottomMargin = source.bottomMargin;
        }
    }

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    public static class SavedState extends AbsSavedState {
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
        int expandedMenuItemId;
        boolean isOverflowOpen;

        public SavedState(Parcel source) {
            this(source, (ClassLoader) null);
        }

        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            this.expandedMenuItemId = source.readInt();
            this.isOverflowOpen = source.readInt() != 0;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.expandedMenuItemId);
            out.writeInt(this.isOverflowOpen ? 1 : 0);
        }
    }

    public Toolbar(Context context) {
        this(context, (AttributeSet) null);
    }

    public Toolbar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public Toolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mGravity = 8388627;
        this.mTempViews = new ArrayList<>();
        this.mHiddenViews = new ArrayList<>();
        this.mTempMargins = new int[2];
        this.mMenuHostHelper = new MenuHostHelper(new Toolbar$$ExternalSyntheticLambda0(this));
        this.mProvidedMenuItems = new ArrayList<>();
        this.mMenuViewItemClickListener = new ActionMenuView.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (Toolbar.this.mMenuHostHelper.onMenuItemSelected(item)) {
                    return true;
                }
                if (Toolbar.this.mOnMenuItemClickListener != null) {
                    return Toolbar.this.mOnMenuItemClickListener.onMenuItemClick(item);
                }
                return false;
            }
        };
        this.mShowOverflowMenuRunnable = new Runnable() {
            public void run() {
                Toolbar.this.showOverflowMenu();
            }
        };
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(getContext(), attrs, R.styleable.Toolbar, defStyleAttr, 0);
        ViewCompat.saveAttributeDataForStyleable(this, context, R.styleable.Toolbar, attrs, obtainStyledAttributes.getWrappedTypeArray(), defStyleAttr, 0);
        this.mTitleTextAppearance = obtainStyledAttributes.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
        this.mSubtitleTextAppearance = obtainStyledAttributes.getResourceId(R.styleable.Toolbar_subtitleTextAppearance, 0);
        this.mGravity = obtainStyledAttributes.getInteger(R.styleable.Toolbar_android_gravity, this.mGravity);
        this.mButtonGravity = obtainStyledAttributes.getInteger(R.styleable.Toolbar_buttonGravity, 48);
        int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMargin, 0);
        dimensionPixelOffset = obtainStyledAttributes.hasValue(R.styleable.Toolbar_titleMargins) ? obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMargins, dimensionPixelOffset) : dimensionPixelOffset;
        this.mTitleMarginBottom = dimensionPixelOffset;
        this.mTitleMarginTop = dimensionPixelOffset;
        this.mTitleMarginEnd = dimensionPixelOffset;
        this.mTitleMarginStart = dimensionPixelOffset;
        int dimensionPixelOffset2 = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginStart, -1);
        if (dimensionPixelOffset2 >= 0) {
            this.mTitleMarginStart = dimensionPixelOffset2;
        }
        int dimensionPixelOffset3 = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginEnd, -1);
        if (dimensionPixelOffset3 >= 0) {
            this.mTitleMarginEnd = dimensionPixelOffset3;
        }
        int dimensionPixelOffset4 = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginTop, -1);
        if (dimensionPixelOffset4 >= 0) {
            this.mTitleMarginTop = dimensionPixelOffset4;
        }
        int dimensionPixelOffset5 = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginBottom, -1);
        if (dimensionPixelOffset5 >= 0) {
            this.mTitleMarginBottom = dimensionPixelOffset5;
        }
        this.mMaxButtonHeight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.Toolbar_maxButtonHeight, -1);
        int dimensionPixelOffset6 = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStart, Integer.MIN_VALUE);
        int dimensionPixelOffset7 = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEnd, Integer.MIN_VALUE);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.Toolbar_contentInsetLeft, 0);
        int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R.styleable.Toolbar_contentInsetRight, 0);
        ensureContentInsets();
        this.mContentInsets.setAbsolute(dimensionPixelSize, dimensionPixelSize2);
        if (!(dimensionPixelOffset6 == Integer.MIN_VALUE && dimensionPixelOffset7 == Integer.MIN_VALUE)) {
            this.mContentInsets.setRelative(dimensionPixelOffset6, dimensionPixelOffset7);
        }
        this.mContentInsetStartWithNavigation = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStartWithNavigation, Integer.MIN_VALUE);
        this.mContentInsetEndWithActions = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEndWithActions, Integer.MIN_VALUE);
        this.mCollapseIcon = obtainStyledAttributes.getDrawable(R.styleable.Toolbar_collapseIcon);
        this.mCollapseDescription = obtainStyledAttributes.getText(R.styleable.Toolbar_collapseContentDescription);
        CharSequence text = obtainStyledAttributes.getText(R.styleable.Toolbar_title);
        if (!TextUtils.isEmpty(text)) {
            setTitle(text);
        }
        CharSequence text2 = obtainStyledAttributes.getText(R.styleable.Toolbar_subtitle);
        if (!TextUtils.isEmpty(text2)) {
            setSubtitle(text2);
        }
        this.mPopupContext = getContext();
        int i = dimensionPixelOffset;
        setPopupTheme(obtainStyledAttributes.getResourceId(R.styleable.Toolbar_popupTheme, 0));
        Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.Toolbar_navigationIcon);
        if (drawable != null) {
            setNavigationIcon(drawable);
        }
        CharSequence text3 = obtainStyledAttributes.getText(R.styleable.Toolbar_navigationContentDescription);
        if (!TextUtils.isEmpty(text3)) {
            setNavigationContentDescription(text3);
        }
        Drawable drawable2 = drawable;
        Drawable drawable3 = obtainStyledAttributes.getDrawable(R.styleable.Toolbar_logo);
        if (drawable3 != null) {
            setLogo(drawable3);
        }
        Drawable drawable4 = drawable3;
        CharSequence text4 = obtainStyledAttributes.getText(R.styleable.Toolbar_logoDescription);
        if (!TextUtils.isEmpty(text4)) {
            setLogoDescription(text4);
        }
        CharSequence charSequence = text4;
        if (obtainStyledAttributes.hasValue(R.styleable.Toolbar_titleTextColor)) {
            setTitleTextColor(obtainStyledAttributes.getColorStateList(R.styleable.Toolbar_titleTextColor));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Toolbar_subtitleTextColor)) {
            setSubtitleTextColor(obtainStyledAttributes.getColorStateList(R.styleable.Toolbar_subtitleTextColor));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Toolbar_menu)) {
            int i2 = dimensionPixelOffset2;
            inflateMenu(obtainStyledAttributes.getResourceId(R.styleable.Toolbar_menu, 0));
        } else {
            int i3 = dimensionPixelOffset2;
        }
        obtainStyledAttributes.recycle();
    }

    private void addCustomViewsWithGravity(List<View> list, int gravity) {
        boolean z = true;
        if (ViewCompat.getLayoutDirection(this) != 1) {
            z = false;
        }
        boolean z2 = z;
        int childCount = getChildCount();
        int absoluteGravity = GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(this));
        list.clear();
        if (z2) {
            for (int i = childCount - 1; i >= 0; i--) {
                View childAt = getChildAt(i);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams.mViewType == 0 && shouldLayout(childAt) && getChildHorizontalGravity(layoutParams.gravity) == absoluteGravity) {
                    list.add(childAt);
                }
            }
            return;
        }
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt2 = getChildAt(i2);
            LayoutParams layoutParams2 = (LayoutParams) childAt2.getLayoutParams();
            if (layoutParams2.mViewType == 0 && shouldLayout(childAt2) && getChildHorizontalGravity(layoutParams2.gravity) == absoluteGravity) {
                list.add(childAt2);
            }
        }
    }

    private void addSystemView(View v, boolean allowHide) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        LayoutParams generateDefaultLayoutParams = layoutParams == null ? generateDefaultLayoutParams() : !checkLayoutParams(layoutParams) ? generateLayoutParams(layoutParams) : (LayoutParams) layoutParams;
        generateDefaultLayoutParams.mViewType = 1;
        if (!allowHide || this.mExpandedActionView == null) {
            addView(v, generateDefaultLayoutParams);
            return;
        }
        v.setLayoutParams(generateDefaultLayoutParams);
        this.mHiddenViews.add(v);
    }

    private void ensureContentInsets() {
        if (this.mContentInsets == null) {
            this.mContentInsets = new RtlSpacingHelper();
        }
    }

    private void ensureLogoView() {
        if (this.mLogoView == null) {
            this.mLogoView = new AppCompatImageView(getContext());
        }
    }

    private void ensureMenu() {
        ensureMenuView();
        if (this.mMenuView.peekMenu() == null) {
            MenuBuilder menuBuilder = (MenuBuilder) this.mMenuView.getMenu();
            if (this.mExpandedMenuPresenter == null) {
                this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
            }
            this.mMenuView.setExpandedActionViewsExclusive(true);
            menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        }
    }

    private void ensureMenuView() {
        if (this.mMenuView == null) {
            ActionMenuView actionMenuView = new ActionMenuView(getContext());
            this.mMenuView = actionMenuView;
            actionMenuView.setPopupTheme(this.mPopupTheme);
            this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
            this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
            LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = 8388613 | (this.mButtonGravity & 112);
            this.mMenuView.setLayoutParams(generateDefaultLayoutParams);
            addSystemView(this.mMenuView, false);
        }
    }

    private void ensureNavButtonView() {
        if (this.mNavButtonView == null) {
            this.mNavButtonView = new AppCompatImageButton(getContext(), (AttributeSet) null, R.attr.toolbarNavigationButtonStyle);
            LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = 8388611 | (this.mButtonGravity & 112);
            this.mNavButtonView.setLayoutParams(generateDefaultLayoutParams);
        }
    }

    private int getChildHorizontalGravity(int gravity) {
        int layoutDirection = ViewCompat.getLayoutDirection(this);
        int absoluteGravity = GravityCompat.getAbsoluteGravity(gravity, layoutDirection) & 7;
        switch (absoluteGravity) {
            case 1:
            case 3:
            case 5:
                return absoluteGravity;
            default:
                return layoutDirection == 1 ? 5 : 3;
        }
    }

    private int getChildTop(View child, int alignmentHeight) {
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        int measuredHeight = child.getMeasuredHeight();
        int i = alignmentHeight > 0 ? (measuredHeight - alignmentHeight) / 2 : 0;
        switch (getChildVerticalGravity(layoutParams.gravity)) {
            case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE /*48*/:
                return getPaddingTop() - i;
            case 80:
                return (((getHeight() - getPaddingBottom()) - measuredHeight) - layoutParams.bottomMargin) - i;
            default:
                int paddingTop = getPaddingTop();
                int paddingBottom = getPaddingBottom();
                int height = getHeight();
                int i2 = (((height - paddingTop) - paddingBottom) - measuredHeight) / 2;
                if (i2 < layoutParams.topMargin) {
                    i2 = layoutParams.topMargin;
                } else {
                    int i3 = (((height - paddingBottom) - measuredHeight) - i2) - paddingTop;
                    if (i3 < layoutParams.bottomMargin) {
                        i2 = Math.max(0, i2 - (layoutParams.bottomMargin - i3));
                    }
                }
                return paddingTop + i2;
        }
    }

    private int getChildVerticalGravity(int gravity) {
        int i = gravity & 112;
        switch (i) {
            case 16:
            case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE /*48*/:
            case 80:
                return i;
            default:
                return this.mGravity & 112;
        }
    }

    private ArrayList<MenuItem> getCurrentMenuItems() {
        ArrayList<MenuItem> arrayList = new ArrayList<>();
        Menu menu = getMenu();
        for (int i = 0; i < menu.size(); i++) {
            arrayList.add(menu.getItem(i));
        }
        return arrayList;
    }

    private int getHorizontalMargins(View v) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginStart(marginLayoutParams) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams);
    }

    private MenuInflater getMenuInflater() {
        return new SupportMenuInflater(getContext());
    }

    private int getVerticalMargins(View v) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        return marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
    }

    private int getViewListMeasuredWidth(List<View> list, int[] collapsingMargins) {
        int i = collapsingMargins[0];
        int i2 = collapsingMargins[1];
        int i3 = 0;
        int size = list.size();
        for (int i4 = 0; i4 < size; i4++) {
            View view = list.get(i4);
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            int i5 = layoutParams.leftMargin - i;
            int i6 = layoutParams.rightMargin - i2;
            int max = Math.max(0, i5);
            int max2 = Math.max(0, i6);
            i = Math.max(0, -i5);
            i2 = Math.max(0, -i6);
            i3 += view.getMeasuredWidth() + max + max2;
        }
        return i3;
    }

    private boolean isChildOrHidden(View child) {
        return child.getParent() == this || this.mHiddenViews.contains(child);
    }

    private int layoutChildLeft(View child, int left, int[] collapsingMargins, int alignmentHeight) {
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        int i = layoutParams.leftMargin - collapsingMargins[0];
        int left2 = left + Math.max(0, i);
        collapsingMargins[0] = Math.max(0, -i);
        int childTop = getChildTop(child, alignmentHeight);
        int measuredWidth = child.getMeasuredWidth();
        child.layout(left2, childTop, left2 + measuredWidth, child.getMeasuredHeight() + childTop);
        return left2 + layoutParams.rightMargin + measuredWidth;
    }

    private int layoutChildRight(View child, int right, int[] collapsingMargins, int alignmentHeight) {
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        int i = layoutParams.rightMargin - collapsingMargins[1];
        int right2 = right - Math.max(0, i);
        collapsingMargins[1] = Math.max(0, -i);
        int childTop = getChildTop(child, alignmentHeight);
        int measuredWidth = child.getMeasuredWidth();
        child.layout(right2 - measuredWidth, childTop, right2, child.getMeasuredHeight() + childTop);
        return right2 - (layoutParams.leftMargin + measuredWidth);
    }

    private int measureChildCollapseMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed, int[] collapsingMargins) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        int i = marginLayoutParams.leftMargin - collapsingMargins[0];
        int i2 = marginLayoutParams.rightMargin - collapsingMargins[1];
        int max = Math.max(0, i) + Math.max(0, i2);
        collapsingMargins[0] = Math.max(0, -i);
        collapsingMargins[1] = Math.max(0, -i2);
        child.measure(getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight() + max + widthUsed, marginLayoutParams.width), getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + heightUsed, marginLayoutParams.height));
        return child.getMeasuredWidth() + max;
    }

    private void measureChildConstrained(View child, int parentWidthSpec, int widthUsed, int parentHeightSpec, int heightUsed, int heightConstraint) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        int childMeasureSpec = getChildMeasureSpec(parentWidthSpec, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + widthUsed, marginLayoutParams.width);
        int childMeasureSpec2 = getChildMeasureSpec(parentHeightSpec, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + heightUsed, marginLayoutParams.height);
        int mode = View.MeasureSpec.getMode(childMeasureSpec2);
        if (mode != 1073741824 && heightConstraint >= 0) {
            childMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(mode != 0 ? Math.min(View.MeasureSpec.getSize(childMeasureSpec2), heightConstraint) : heightConstraint, BasicMeasure.EXACTLY);
        }
        child.measure(childMeasureSpec, childMeasureSpec2);
    }

    private void onCreateMenu() {
        Menu menu = getMenu();
        ArrayList<MenuItem> currentMenuItems = getCurrentMenuItems();
        this.mMenuHostHelper.onCreateMenu(menu, getMenuInflater());
        ArrayList<MenuItem> currentMenuItems2 = getCurrentMenuItems();
        currentMenuItems2.removeAll(currentMenuItems);
        this.mProvidedMenuItems = currentMenuItems2;
        this.mMenuHostHelper.onPrepareMenu(menu);
    }

    private void postShowOverflowMenu() {
        removeCallbacks(this.mShowOverflowMenuRunnable);
        post(this.mShowOverflowMenuRunnable);
    }

    private boolean shouldCollapse() {
        if (!this.mCollapsible) {
            return false;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (shouldLayout(childAt) && childAt.getMeasuredWidth() > 0 && childAt.getMeasuredHeight() > 0) {
                return false;
            }
        }
        return true;
    }

    private boolean shouldLayout(View view) {
        return (view == null || view.getParent() != this || view.getVisibility() == 8) ? false : true;
    }

    /* access modifiers changed from: package-private */
    public void addChildrenForExpandedActionView() {
        for (int size = this.mHiddenViews.size() - 1; size >= 0; size--) {
            addView(this.mHiddenViews.get(size));
        }
        this.mHiddenViews.clear();
    }

    public void addMenuProvider(MenuProvider provider) {
        this.mMenuHostHelper.addMenuProvider(provider);
    }

    public void addMenuProvider(MenuProvider provider, LifecycleOwner owner) {
        this.mMenuHostHelper.addMenuProvider(provider, owner);
    }

    public void addMenuProvider(MenuProvider provider, LifecycleOwner owner, Lifecycle.State state) {
        this.mMenuHostHelper.addMenuProvider(provider, owner, state);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r0 = r1.mMenuView;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean canShowOverflowMenu() {
        /*
            r1 = this;
            int r0 = r1.getVisibility()
            if (r0 != 0) goto L_0x0012
            androidx.appcompat.widget.ActionMenuView r0 = r1.mMenuView
            if (r0 == 0) goto L_0x0012
            boolean r0 = r0.isOverflowReserved()
            if (r0 == 0) goto L_0x0012
            r0 = 1
            goto L_0x0013
        L_0x0012:
            r0 = 0
        L_0x0013:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.Toolbar.canShowOverflowMenu():boolean");
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p) && (p instanceof LayoutParams);
    }

    public void collapseActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        MenuItemImpl menuItemImpl = expandedActionViewMenuPresenter == null ? null : expandedActionViewMenuPresenter.mCurrentExpandedItem;
        if (menuItemImpl != null) {
            menuItemImpl.collapseActionView();
        }
    }

    public void dismissPopupMenus() {
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            actionMenuView.dismissPopupMenus();
        }
    }

    /* access modifiers changed from: package-private */
    public void ensureCollapseButtonView() {
        if (this.mCollapseButtonView == null) {
            AppCompatImageButton appCompatImageButton = new AppCompatImageButton(getContext(), (AttributeSet) null, R.attr.toolbarNavigationButtonStyle);
            this.mCollapseButtonView = appCompatImageButton;
            appCompatImageButton.setImageDrawable(this.mCollapseIcon);
            this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
            LayoutParams generateDefaultLayoutParams = generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = 8388611 | (this.mButtonGravity & 112);
            generateDefaultLayoutParams.mViewType = 2;
            this.mCollapseButtonView.setLayoutParams(generateDefaultLayoutParams);
            this.mCollapseButtonView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Toolbar.this.collapseActionView();
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams ? new LayoutParams((LayoutParams) p) : p instanceof ActionBar.LayoutParams ? new LayoutParams((ActionBar.LayoutParams) p) : p instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams) p) : new LayoutParams(p);
    }

    public CharSequence getCollapseContentDescription() {
        ImageButton imageButton = this.mCollapseButtonView;
        if (imageButton != null) {
            return imageButton.getContentDescription();
        }
        return null;
    }

    public Drawable getCollapseIcon() {
        ImageButton imageButton = this.mCollapseButtonView;
        if (imageButton != null) {
            return imageButton.getDrawable();
        }
        return null;
    }

    public int getContentInsetEnd() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        if (rtlSpacingHelper != null) {
            return rtlSpacingHelper.getEnd();
        }
        return 0;
    }

    public int getContentInsetEndWithActions() {
        int i = this.mContentInsetEndWithActions;
        return i != Integer.MIN_VALUE ? i : getContentInsetEnd();
    }

    public int getContentInsetLeft() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        if (rtlSpacingHelper != null) {
            return rtlSpacingHelper.getLeft();
        }
        return 0;
    }

    public int getContentInsetRight() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        if (rtlSpacingHelper != null) {
            return rtlSpacingHelper.getRight();
        }
        return 0;
    }

    public int getContentInsetStart() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        if (rtlSpacingHelper != null) {
            return rtlSpacingHelper.getStart();
        }
        return 0;
    }

    public int getContentInsetStartWithNavigation() {
        int i = this.mContentInsetStartWithNavigation;
        return i != Integer.MIN_VALUE ? i : getContentInsetStart();
    }

    public int getCurrentContentInsetEnd() {
        boolean z = false;
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            MenuBuilder peekMenu = actionMenuView.peekMenu();
            z = peekMenu != null && peekMenu.hasVisibleItems();
        }
        return z ? Math.max(getContentInsetEnd(), Math.max(this.mContentInsetEndWithActions, 0)) : getContentInsetEnd();
    }

    public int getCurrentContentInsetLeft() {
        return ViewCompat.getLayoutDirection(this) == 1 ? getCurrentContentInsetEnd() : getCurrentContentInsetStart();
    }

    public int getCurrentContentInsetRight() {
        return ViewCompat.getLayoutDirection(this) == 1 ? getCurrentContentInsetStart() : getCurrentContentInsetEnd();
    }

    public int getCurrentContentInsetStart() {
        return getNavigationIcon() != null ? Math.max(getContentInsetStart(), Math.max(this.mContentInsetStartWithNavigation, 0)) : getContentInsetStart();
    }

    public Drawable getLogo() {
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            return imageView.getDrawable();
        }
        return null;
    }

    public CharSequence getLogoDescription() {
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            return imageView.getContentDescription();
        }
        return null;
    }

    public Menu getMenu() {
        ensureMenu();
        return this.mMenuView.getMenu();
    }

    /* access modifiers changed from: package-private */
    public View getNavButtonView() {
        return this.mNavButtonView;
    }

    public CharSequence getNavigationContentDescription() {
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            return imageButton.getContentDescription();
        }
        return null;
    }

    public Drawable getNavigationIcon() {
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            return imageButton.getDrawable();
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public ActionMenuPresenter getOuterActionMenuPresenter() {
        return this.mOuterActionMenuPresenter;
    }

    public Drawable getOverflowIcon() {
        ensureMenu();
        return this.mMenuView.getOverflowIcon();
    }

    /* access modifiers changed from: package-private */
    public Context getPopupContext() {
        return this.mPopupContext;
    }

    public int getPopupTheme() {
        return this.mPopupTheme;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitleText;
    }

    /* access modifiers changed from: package-private */
    public final TextView getSubtitleTextView() {
        return this.mSubtitleTextView;
    }

    public CharSequence getTitle() {
        return this.mTitleText;
    }

    public int getTitleMarginBottom() {
        return this.mTitleMarginBottom;
    }

    public int getTitleMarginEnd() {
        return this.mTitleMarginEnd;
    }

    public int getTitleMarginStart() {
        return this.mTitleMarginStart;
    }

    public int getTitleMarginTop() {
        return this.mTitleMarginTop;
    }

    /* access modifiers changed from: package-private */
    public final TextView getTitleTextView() {
        return this.mTitleTextView;
    }

    public DecorToolbar getWrapper() {
        if (this.mWrapper == null) {
            this.mWrapper = new ToolbarWidgetWrapper(this, true);
        }
        return this.mWrapper;
    }

    public boolean hasExpandedActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        return (expandedActionViewMenuPresenter == null || expandedActionViewMenuPresenter.mCurrentExpandedItem == null) ? false : true;
    }

    public boolean hideOverflowMenu() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.hideOverflowMenu();
    }

    public void inflateMenu(int resId) {
        getMenuInflater().inflate(resId, getMenu());
    }

    public void invalidateMenu() {
        Iterator<MenuItem> it = this.mProvidedMenuItems.iterator();
        while (it.hasNext()) {
            getMenu().removeItem(it.next().getItemId());
        }
        onCreateMenu();
    }

    public boolean isOverflowMenuShowPending() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.isOverflowMenuShowPending();
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.isOverflowMenuShowing();
    }

    public boolean isTitleTruncated() {
        Layout layout;
        TextView textView = this.mTitleTextView;
        if (textView == null || (layout = textView.getLayout()) == null) {
            return false;
        }
        int lineCount = layout.getLineCount();
        for (int i = 0; i < lineCount; i++) {
            if (layout.getEllipsisCount(i) > 0) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mShowOverflowMenuRunnable);
    }

    public boolean onHoverEvent(MotionEvent ev) {
        int actionMasked = ev.getActionMasked();
        if (actionMasked == 9) {
            this.mEatingHover = false;
        }
        if (!this.mEatingHover) {
            boolean onHoverEvent = super.onHoverEvent(ev);
            if (actionMasked == 9 && !onHoverEvent) {
                this.mEatingHover = true;
            }
        }
        if (actionMasked == 10 || actionMasked == 3) {
            this.mEatingHover = false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x015e, code lost:
        if (r0.mTitleTextView.getMeasuredWidth() <= 0) goto L_0x0163;
     */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0179  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0196  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x01a7  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x01d9  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0259  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onLayout(boolean r34, int r35, int r36, int r37, int r38) {
        /*
            r33 = this;
            r0 = r33
            int r1 = androidx.core.view.ViewCompat.getLayoutDirection(r33)
            r2 = 1
            r3 = 0
            if (r1 != r2) goto L_0x000c
            r1 = r2
            goto L_0x000d
        L_0x000c:
            r1 = r3
        L_0x000d:
            int r4 = r33.getWidth()
            int r5 = r33.getHeight()
            int r6 = r33.getPaddingLeft()
            int r7 = r33.getPaddingRight()
            int r8 = r33.getPaddingTop()
            int r9 = r33.getPaddingBottom()
            r10 = r6
            int r11 = r4 - r7
            int[] r12 = r0.mTempMargins
            r12[r2] = r3
            r12[r3] = r3
            int r13 = androidx.core.view.ViewCompat.getMinimumHeight(r33)
            if (r13 < 0) goto L_0x003b
            int r14 = r38 - r36
            int r14 = java.lang.Math.min(r13, r14)
            goto L_0x003c
        L_0x003b:
            r14 = r3
        L_0x003c:
            android.widget.ImageButton r15 = r0.mNavButtonView
            boolean r15 = r0.shouldLayout(r15)
            if (r15 == 0) goto L_0x0053
            if (r1 == 0) goto L_0x004d
            android.widget.ImageButton r15 = r0.mNavButtonView
            int r11 = r0.layoutChildRight(r15, r11, r12, r14)
            goto L_0x0053
        L_0x004d:
            android.widget.ImageButton r15 = r0.mNavButtonView
            int r10 = r0.layoutChildLeft(r15, r10, r12, r14)
        L_0x0053:
            android.widget.ImageButton r15 = r0.mCollapseButtonView
            boolean r15 = r0.shouldLayout(r15)
            if (r15 == 0) goto L_0x006a
            if (r1 == 0) goto L_0x0064
            android.widget.ImageButton r15 = r0.mCollapseButtonView
            int r11 = r0.layoutChildRight(r15, r11, r12, r14)
            goto L_0x006a
        L_0x0064:
            android.widget.ImageButton r15 = r0.mCollapseButtonView
            int r10 = r0.layoutChildLeft(r15, r10, r12, r14)
        L_0x006a:
            androidx.appcompat.widget.ActionMenuView r15 = r0.mMenuView
            boolean r15 = r0.shouldLayout(r15)
            if (r15 == 0) goto L_0x0081
            if (r1 == 0) goto L_0x007b
            androidx.appcompat.widget.ActionMenuView r15 = r0.mMenuView
            int r10 = r0.layoutChildLeft(r15, r10, r12, r14)
            goto L_0x0081
        L_0x007b:
            androidx.appcompat.widget.ActionMenuView r15 = r0.mMenuView
            int r11 = r0.layoutChildRight(r15, r11, r12, r14)
        L_0x0081:
            int r15 = r33.getCurrentContentInsetLeft()
            int r16 = r33.getCurrentContentInsetRight()
            int r2 = r15 - r10
            int r2 = java.lang.Math.max(r3, r2)
            r12[r3] = r2
            int r2 = r4 - r7
            int r2 = r2 - r11
            int r2 = r16 - r2
            int r2 = java.lang.Math.max(r3, r2)
            r17 = 1
            r12[r17] = r2
            int r2 = java.lang.Math.max(r10, r15)
            int r10 = r4 - r7
            int r10 = r10 - r16
            int r10 = java.lang.Math.min(r11, r10)
            android.view.View r11 = r0.mExpandedActionView
            boolean r11 = r0.shouldLayout(r11)
            if (r11 == 0) goto L_0x00c1
            if (r1 == 0) goto L_0x00bb
            android.view.View r11 = r0.mExpandedActionView
            int r10 = r0.layoutChildRight(r11, r10, r12, r14)
            goto L_0x00c1
        L_0x00bb:
            android.view.View r11 = r0.mExpandedActionView
            int r2 = r0.layoutChildLeft(r11, r2, r12, r14)
        L_0x00c1:
            android.widget.ImageView r11 = r0.mLogoView
            boolean r11 = r0.shouldLayout(r11)
            if (r11 == 0) goto L_0x00d8
            if (r1 == 0) goto L_0x00d2
            android.widget.ImageView r11 = r0.mLogoView
            int r10 = r0.layoutChildRight(r11, r10, r12, r14)
            goto L_0x00d8
        L_0x00d2:
            android.widget.ImageView r11 = r0.mLogoView
            int r2 = r0.layoutChildLeft(r11, r2, r12, r14)
        L_0x00d8:
            android.widget.TextView r11 = r0.mTitleTextView
            boolean r11 = r0.shouldLayout(r11)
            android.widget.TextView r3 = r0.mSubtitleTextView
            boolean r3 = r0.shouldLayout(r3)
            r19 = 0
            if (r11 == 0) goto L_0x0105
            r20 = r13
            android.widget.TextView r13 = r0.mTitleTextView
            android.view.ViewGroup$LayoutParams r13 = r13.getLayoutParams()
            androidx.appcompat.widget.Toolbar$LayoutParams r13 = (androidx.appcompat.widget.Toolbar.LayoutParams) r13
            r21 = r15
            int r15 = r13.topMargin
            r22 = r7
            android.widget.TextView r7 = r0.mTitleTextView
            int r7 = r7.getMeasuredHeight()
            int r15 = r15 + r7
            int r7 = r13.bottomMargin
            int r15 = r15 + r7
            int r19 = r19 + r15
            goto L_0x010b
        L_0x0105:
            r22 = r7
            r20 = r13
            r21 = r15
        L_0x010b:
            if (r3 == 0) goto L_0x0123
            android.widget.TextView r7 = r0.mSubtitleTextView
            android.view.ViewGroup$LayoutParams r7 = r7.getLayoutParams()
            androidx.appcompat.widget.Toolbar$LayoutParams r7 = (androidx.appcompat.widget.Toolbar.LayoutParams) r7
            int r13 = r7.topMargin
            android.widget.TextView r15 = r0.mSubtitleTextView
            int r15 = r15.getMeasuredHeight()
            int r13 = r13 + r15
            int r15 = r7.bottomMargin
            int r13 = r13 + r15
            int r19 = r19 + r13
        L_0x0123:
            if (r11 != 0) goto L_0x0136
            if (r3 == 0) goto L_0x0128
            goto L_0x0136
        L_0x0128:
            r27 = r1
            r25 = r4
            r29 = r5
            r26 = r6
            r31 = r8
            r28 = r14
            goto L_0x02d2
        L_0x0136:
            if (r11 == 0) goto L_0x013b
            android.widget.TextView r7 = r0.mTitleTextView
            goto L_0x013d
        L_0x013b:
            android.widget.TextView r7 = r0.mSubtitleTextView
        L_0x013d:
            if (r3 == 0) goto L_0x0142
            android.widget.TextView r13 = r0.mSubtitleTextView
            goto L_0x0144
        L_0x0142:
            android.widget.TextView r13 = r0.mTitleTextView
        L_0x0144:
            android.view.ViewGroup$LayoutParams r15 = r7.getLayoutParams()
            androidx.appcompat.widget.Toolbar$LayoutParams r15 = (androidx.appcompat.widget.Toolbar.LayoutParams) r15
            android.view.ViewGroup$LayoutParams r23 = r13.getLayoutParams()
            r24 = r7
            r7 = r23
            androidx.appcompat.widget.Toolbar$LayoutParams r7 = (androidx.appcompat.widget.Toolbar.LayoutParams) r7
            if (r11 == 0) goto L_0x0161
            r23 = r13
            android.widget.TextView r13 = r0.mTitleTextView
            int r13 = r13.getMeasuredWidth()
            if (r13 > 0) goto L_0x016d
            goto L_0x0163
        L_0x0161:
            r23 = r13
        L_0x0163:
            if (r3 == 0) goto L_0x016f
            android.widget.TextView r13 = r0.mSubtitleTextView
            int r13 = r13.getMeasuredWidth()
            if (r13 <= 0) goto L_0x016f
        L_0x016d:
            r13 = 1
            goto L_0x0170
        L_0x016f:
            r13 = 0
        L_0x0170:
            r25 = r4
            int r4 = r0.mGravity
            r4 = r4 & 112(0x70, float:1.57E-43)
            switch(r4) {
                case 48: goto L_0x01a7;
                case 80: goto L_0x0196;
                default: goto L_0x0179;
            }
        L_0x0179:
            r26 = r6
            int r4 = r5 - r8
            int r4 = r4 - r9
            int r6 = r4 - r19
            int r6 = r6 / 2
            r27 = r4
            int r4 = r15.topMargin
            r28 = r14
            int r14 = r0.mTitleMarginTop
            int r4 = r4 + r14
            if (r6 >= r4) goto L_0x01b8
            int r4 = r15.topMargin
            int r14 = r0.mTitleMarginTop
            int r6 = r4 + r14
            r29 = r5
            goto L_0x01d4
        L_0x0196:
            int r4 = r5 - r9
            r26 = r6
            int r6 = r7.bottomMargin
            int r4 = r4 - r6
            int r6 = r0.mTitleMarginBottom
            int r4 = r4 - r6
            int r4 = r4 - r19
            r29 = r5
            r28 = r14
            goto L_0x01d7
        L_0x01a7:
            r26 = r6
            int r4 = r33.getPaddingTop()
            int r6 = r15.topMargin
            int r4 = r4 + r6
            int r6 = r0.mTitleMarginTop
            int r4 = r4 + r6
            r29 = r5
            r28 = r14
            goto L_0x01d7
        L_0x01b8:
            int r4 = r5 - r9
            int r4 = r4 - r19
            int r4 = r4 - r6
            int r4 = r4 - r8
            int r14 = r15.bottomMargin
            r29 = r5
            int r5 = r0.mTitleMarginBottom
            int r14 = r14 + r5
            if (r4 >= r14) goto L_0x01d4
            int r5 = r7.bottomMargin
            int r14 = r0.mTitleMarginBottom
            int r5 = r5 + r14
            int r5 = r5 - r4
            int r5 = r6 - r5
            r14 = 0
            int r6 = java.lang.Math.max(r14, r5)
        L_0x01d4:
            int r4 = r8 + r6
        L_0x01d7:
            if (r1 == 0) goto L_0x0259
            if (r13 == 0) goto L_0x01de
            int r5 = r0.mTitleMarginStart
            goto L_0x01df
        L_0x01de:
            r5 = 0
        L_0x01df:
            r6 = 1
            r14 = r12[r6]
            int r5 = r5 - r14
            r14 = 0
            int r17 = java.lang.Math.max(r14, r5)
            int r10 = r10 - r17
            r27 = r1
            int r1 = -r5
            int r1 = java.lang.Math.max(r14, r1)
            r12[r6] = r1
            r1 = r10
            r6 = r10
            if (r11 == 0) goto L_0x0222
            android.widget.TextView r14 = r0.mTitleTextView
            android.view.ViewGroup$LayoutParams r14 = r14.getLayoutParams()
            androidx.appcompat.widget.Toolbar$LayoutParams r14 = (androidx.appcompat.widget.Toolbar.LayoutParams) r14
            r18 = r5
            android.widget.TextView r5 = r0.mTitleTextView
            int r5 = r5.getMeasuredWidth()
            int r5 = r1 - r5
            r30 = r7
            android.widget.TextView r7 = r0.mTitleTextView
            int r7 = r7.getMeasuredHeight()
            int r7 = r7 + r4
            r31 = r8
            android.widget.TextView r8 = r0.mTitleTextView
            r8.layout(r5, r4, r1, r7)
            int r8 = r0.mTitleMarginEnd
            int r1 = r5 - r8
            int r8 = r14.bottomMargin
            int r4 = r7 + r8
            goto L_0x0228
        L_0x0222:
            r18 = r5
            r30 = r7
            r31 = r8
        L_0x0228:
            if (r3 == 0) goto L_0x0250
            android.widget.TextView r5 = r0.mSubtitleTextView
            android.view.ViewGroup$LayoutParams r5 = r5.getLayoutParams()
            androidx.appcompat.widget.Toolbar$LayoutParams r5 = (androidx.appcompat.widget.Toolbar.LayoutParams) r5
            int r7 = r5.topMargin
            int r4 = r4 + r7
            android.widget.TextView r7 = r0.mSubtitleTextView
            int r7 = r7.getMeasuredWidth()
            int r7 = r6 - r7
            android.widget.TextView r8 = r0.mSubtitleTextView
            int r8 = r8.getMeasuredHeight()
            int r8 = r8 + r4
            android.widget.TextView r14 = r0.mSubtitleTextView
            r14.layout(r7, r4, r6, r8)
            int r14 = r0.mTitleMarginEnd
            int r6 = r6 - r14
            int r14 = r5.bottomMargin
            int r4 = r8 + r14
        L_0x0250:
            if (r13 == 0) goto L_0x0257
            int r5 = java.lang.Math.min(r1, r6)
            r10 = r5
        L_0x0257:
            goto L_0x02d2
        L_0x0259:
            r27 = r1
            r30 = r7
            r31 = r8
            if (r13 == 0) goto L_0x0264
            int r14 = r0.mTitleMarginStart
            goto L_0x0265
        L_0x0264:
            r14 = 0
        L_0x0265:
            r1 = 0
            r5 = r12[r1]
            int r14 = r14 - r5
            int r5 = java.lang.Math.max(r1, r14)
            int r2 = r2 + r5
            int r5 = -r14
            int r5 = java.lang.Math.max(r1, r5)
            r12[r1] = r5
            r1 = r2
            r5 = r2
            if (r11 == 0) goto L_0x029f
            android.widget.TextView r6 = r0.mTitleTextView
            android.view.ViewGroup$LayoutParams r6 = r6.getLayoutParams()
            androidx.appcompat.widget.Toolbar$LayoutParams r6 = (androidx.appcompat.widget.Toolbar.LayoutParams) r6
            android.widget.TextView r7 = r0.mTitleTextView
            int r7 = r7.getMeasuredWidth()
            int r7 = r7 + r1
            android.widget.TextView r8 = r0.mTitleTextView
            int r8 = r8.getMeasuredHeight()
            int r8 = r8 + r4
            r18 = r2
            android.widget.TextView r2 = r0.mTitleTextView
            r2.layout(r1, r4, r7, r8)
            int r2 = r0.mTitleMarginEnd
            int r1 = r7 + r2
            int r2 = r6.bottomMargin
            int r4 = r8 + r2
            goto L_0x02a1
        L_0x029f:
            r18 = r2
        L_0x02a1:
            if (r3 == 0) goto L_0x02c9
            android.widget.TextView r2 = r0.mSubtitleTextView
            android.view.ViewGroup$LayoutParams r2 = r2.getLayoutParams()
            androidx.appcompat.widget.Toolbar$LayoutParams r2 = (androidx.appcompat.widget.Toolbar.LayoutParams) r2
            int r6 = r2.topMargin
            int r4 = r4 + r6
            android.widget.TextView r6 = r0.mSubtitleTextView
            int r6 = r6.getMeasuredWidth()
            int r6 = r6 + r5
            android.widget.TextView r7 = r0.mSubtitleTextView
            int r7 = r7.getMeasuredHeight()
            int r7 = r7 + r4
            android.widget.TextView r8 = r0.mSubtitleTextView
            r8.layout(r5, r4, r6, r7)
            int r8 = r0.mTitleMarginEnd
            int r5 = r6 + r8
            int r8 = r2.bottomMargin
            int r4 = r7 + r8
        L_0x02c9:
            if (r13 == 0) goto L_0x02d0
            int r2 = java.lang.Math.max(r1, r5)
            goto L_0x02d2
        L_0x02d0:
            r2 = r18
        L_0x02d2:
            java.util.ArrayList<android.view.View> r1 = r0.mTempViews
            r4 = 3
            r0.addCustomViewsWithGravity(r1, r4)
            java.util.ArrayList<android.view.View> r1 = r0.mTempViews
            int r1 = r1.size()
            r4 = 0
        L_0x02df:
            if (r4 >= r1) goto L_0x02f2
            java.util.ArrayList<android.view.View> r5 = r0.mTempViews
            java.lang.Object r5 = r5.get(r4)
            android.view.View r5 = (android.view.View) r5
            r6 = r28
            int r2 = r0.layoutChildLeft(r5, r2, r12, r6)
            int r4 = r4 + 1
            goto L_0x02df
        L_0x02f2:
            r6 = r28
            java.util.ArrayList<android.view.View> r4 = r0.mTempViews
            r5 = 5
            r0.addCustomViewsWithGravity(r4, r5)
            java.util.ArrayList<android.view.View> r4 = r0.mTempViews
            int r4 = r4.size()
            r5 = 0
        L_0x0301:
            if (r5 >= r4) goto L_0x0312
            java.util.ArrayList<android.view.View> r7 = r0.mTempViews
            java.lang.Object r7 = r7.get(r5)
            android.view.View r7 = (android.view.View) r7
            int r10 = r0.layoutChildRight(r7, r10, r12, r6)
            int r5 = r5 + 1
            goto L_0x0301
        L_0x0312:
            java.util.ArrayList<android.view.View> r5 = r0.mTempViews
            r7 = 1
            r0.addCustomViewsWithGravity(r5, r7)
            java.util.ArrayList<android.view.View> r5 = r0.mTempViews
            int r5 = r0.getViewListMeasuredWidth(r5, r12)
            int r7 = r25 - r26
            int r7 = r7 - r22
            int r7 = r7 / 2
            int r7 = r26 + r7
            int r8 = r5 / 2
            int r13 = r7 - r8
            int r14 = r13 + r5
            if (r13 >= r2) goto L_0x0330
            r13 = r2
            goto L_0x0335
        L_0x0330:
            if (r14 <= r10) goto L_0x0335
            int r15 = r14 - r10
            int r13 = r13 - r15
        L_0x0335:
            java.util.ArrayList<android.view.View> r15 = r0.mTempViews
            int r15 = r15.size()
            r17 = 0
            r32 = r17
            r17 = r1
            r1 = r32
        L_0x0343:
            if (r1 >= r15) goto L_0x0358
            r18 = r2
            java.util.ArrayList<android.view.View> r2 = r0.mTempViews
            java.lang.Object r2 = r2.get(r1)
            android.view.View r2 = (android.view.View) r2
            int r13 = r0.layoutChildLeft(r2, r13, r12, r6)
            int r1 = r1 + 1
            r2 = r18
            goto L_0x0343
        L_0x0358:
            java.util.ArrayList<android.view.View> r1 = r0.mTempViews
            r1.clear()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.Toolbar.onLayout(boolean, int, int, int, int):void");
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        char c;
        char c2;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6 = 0;
        int i7 = 0;
        int[] iArr = this.mTempMargins;
        if (ViewUtils.isLayoutRtl(this)) {
            c2 = 1;
            c = 0;
        } else {
            c2 = 0;
            c = 1;
        }
        int i8 = 0;
        if (shouldLayout(this.mNavButtonView)) {
            measureChildConstrained(this.mNavButtonView, widthMeasureSpec, 0, heightMeasureSpec, 0, this.mMaxButtonHeight);
            i8 = this.mNavButtonView.getMeasuredWidth() + getHorizontalMargins(this.mNavButtonView);
            i6 = Math.max(0, this.mNavButtonView.getMeasuredHeight() + getVerticalMargins(this.mNavButtonView));
            i7 = View.combineMeasuredStates(0, this.mNavButtonView.getMeasuredState());
        }
        if (shouldLayout(this.mCollapseButtonView)) {
            measureChildConstrained(this.mCollapseButtonView, widthMeasureSpec, 0, heightMeasureSpec, 0, this.mMaxButtonHeight);
            i8 = this.mCollapseButtonView.getMeasuredWidth() + getHorizontalMargins(this.mCollapseButtonView);
            i6 = Math.max(i6, this.mCollapseButtonView.getMeasuredHeight() + getVerticalMargins(this.mCollapseButtonView));
            i7 = View.combineMeasuredStates(i7, this.mCollapseButtonView.getMeasuredState());
        }
        int currentContentInsetStart = getCurrentContentInsetStart();
        int max = 0 + Math.max(currentContentInsetStart, i8);
        iArr[c2] = Math.max(0, currentContentInsetStart - i8);
        if (shouldLayout(this.mMenuView)) {
            char c3 = c2;
            i = 0;
            measureChildConstrained(this.mMenuView, widthMeasureSpec, max, heightMeasureSpec, 0, this.mMaxButtonHeight);
            int measuredWidth = this.mMenuView.getMeasuredWidth() + getHorizontalMargins(this.mMenuView);
            i6 = Math.max(i6, this.mMenuView.getMeasuredHeight() + getVerticalMargins(this.mMenuView));
            i7 = View.combineMeasuredStates(i7, this.mMenuView.getMeasuredState());
            i2 = measuredWidth;
        } else {
            char c4 = c2;
            i = 0;
            i2 = 0;
        }
        int currentContentInsetEnd = getCurrentContentInsetEnd();
        int max2 = max + Math.max(currentContentInsetEnd, i2);
        iArr[c] = Math.max(i, currentContentInsetEnd - i2);
        if (shouldLayout(this.mExpandedActionView)) {
            int i9 = currentContentInsetEnd;
            int i10 = i2;
            max2 += measureChildCollapseMargins(this.mExpandedActionView, widthMeasureSpec, max2, heightMeasureSpec, 0, iArr);
            i6 = Math.max(i6, this.mExpandedActionView.getMeasuredHeight() + getVerticalMargins(this.mExpandedActionView));
            i7 = View.combineMeasuredStates(i7, this.mExpandedActionView.getMeasuredState());
        } else {
            int i11 = currentContentInsetEnd;
            int i12 = i2;
        }
        if (shouldLayout(this.mLogoView)) {
            max2 += measureChildCollapseMargins(this.mLogoView, widthMeasureSpec, max2, heightMeasureSpec, 0, iArr);
            i6 = Math.max(i6, this.mLogoView.getMeasuredHeight() + getVerticalMargins(this.mLogoView));
            i7 = View.combineMeasuredStates(i7, this.mLogoView.getMeasuredState());
        }
        int childCount = getChildCount();
        int i13 = i7;
        int i14 = i6;
        int i15 = max2;
        int i16 = 0;
        while (i16 < childCount) {
            View childAt = getChildAt(i16);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            if (layoutParams.mViewType != 0) {
                LayoutParams layoutParams2 = layoutParams;
                View view = childAt;
                i5 = i13;
                i4 = childCount;
            } else if (!shouldLayout(childAt)) {
                i5 = i13;
                i4 = childCount;
            } else {
                LayoutParams layoutParams3 = layoutParams;
                View view2 = childAt;
                i4 = childCount;
                i15 += measureChildCollapseMargins(childAt, widthMeasureSpec, i15, heightMeasureSpec, 0, iArr);
                View view3 = view2;
                i14 = Math.max(i14, view2.getMeasuredHeight() + getVerticalMargins(view3));
                i13 = View.combineMeasuredStates(i13, view3.getMeasuredState());
                i16++;
                childCount = i4;
            }
            i13 = i5;
            i16++;
            childCount = i4;
        }
        int i17 = i13;
        int i18 = childCount;
        int i19 = 0;
        int i20 = 0;
        int i21 = this.mTitleMarginTop + this.mTitleMarginBottom;
        int i22 = this.mTitleMarginStart + this.mTitleMarginEnd;
        if (shouldLayout(this.mTitleTextView)) {
            int measureChildCollapseMargins = measureChildCollapseMargins(this.mTitleTextView, widthMeasureSpec, i15 + i22, heightMeasureSpec, i21, iArr);
            i19 = this.mTitleTextView.getMeasuredWidth() + getHorizontalMargins(this.mTitleTextView);
            i20 = this.mTitleTextView.getMeasuredHeight() + getVerticalMargins(this.mTitleTextView);
            i17 = View.combineMeasuredStates(i17, this.mTitleTextView.getMeasuredState());
        }
        if (shouldLayout(this.mSubtitleTextView)) {
            i19 = Math.max(i19, measureChildCollapseMargins(this.mSubtitleTextView, widthMeasureSpec, i15 + i22, heightMeasureSpec, i20 + i21, iArr));
            int measuredHeight = i20 + this.mSubtitleTextView.getMeasuredHeight() + getVerticalMargins(this.mSubtitleTextView);
            i17 = View.combineMeasuredStates(i17, this.mSubtitleTextView.getMeasuredState());
            i3 = measuredHeight;
        } else {
            i3 = i20;
        }
        setMeasuredDimension(View.resolveSizeAndState(Math.max(i15 + i19 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), widthMeasureSpec, -16777216 & i17), shouldCollapse() ? 0 : View.resolveSizeAndState(Math.max(Math.max(i14, i3) + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), heightMeasureSpec, i17 << 16));
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        MenuItem findItem;
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        ActionMenuView actionMenuView = this.mMenuView;
        MenuBuilder peekMenu = actionMenuView != null ? actionMenuView.peekMenu() : null;
        if (!(savedState.expandedMenuItemId == 0 || this.mExpandedMenuPresenter == null || peekMenu == null || (findItem = peekMenu.findItem(savedState.expandedMenuItemId)) == null)) {
            findItem.expandActionView();
        }
        if (savedState.isOverflowOpen) {
            postShowOverflowMenu();
        }
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        if (Build.VERSION.SDK_INT >= 17) {
            super.onRtlPropertiesChanged(layoutDirection);
        }
        ensureContentInsets();
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        boolean z = true;
        if (layoutDirection != 1) {
            z = false;
        }
        rtlSpacingHelper.setDirection(z);
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        if (!(expandedActionViewMenuPresenter == null || expandedActionViewMenuPresenter.mCurrentExpandedItem == null)) {
            savedState.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
        }
        savedState.isOverflowOpen = isOverflowMenuShowing();
        return savedState;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        int actionMasked = ev.getActionMasked();
        if (actionMasked == 0) {
            this.mEatingTouch = false;
        }
        if (!this.mEatingTouch) {
            boolean onTouchEvent = super.onTouchEvent(ev);
            if (actionMasked == 0 && !onTouchEvent) {
                this.mEatingTouch = true;
            }
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.mEatingTouch = false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void removeChildrenForExpandedActionView() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = getChildAt(childCount);
            if (!(((LayoutParams) childAt.getLayoutParams()).mViewType == 2 || childAt == this.mMenuView)) {
                removeViewAt(childCount);
                this.mHiddenViews.add(childAt);
            }
        }
    }

    public void removeMenuProvider(MenuProvider provider) {
        this.mMenuHostHelper.removeMenuProvider(provider);
    }

    public void setCollapseContentDescription(int resId) {
        setCollapseContentDescription(resId != 0 ? getContext().getText(resId) : null);
    }

    public void setCollapseContentDescription(CharSequence description) {
        if (!TextUtils.isEmpty(description)) {
            ensureCollapseButtonView();
        }
        ImageButton imageButton = this.mCollapseButtonView;
        if (imageButton != null) {
            imageButton.setContentDescription(description);
        }
    }

    public void setCollapseIcon(int resId) {
        setCollapseIcon(AppCompatResources.getDrawable(getContext(), resId));
    }

    public void setCollapseIcon(Drawable icon) {
        if (icon != null) {
            ensureCollapseButtonView();
            this.mCollapseButtonView.setImageDrawable(icon);
            return;
        }
        ImageButton imageButton = this.mCollapseButtonView;
        if (imageButton != null) {
            imageButton.setImageDrawable(this.mCollapseIcon);
        }
    }

    public void setCollapsible(boolean collapsible) {
        this.mCollapsible = collapsible;
        requestLayout();
    }

    public void setContentInsetEndWithActions(int insetEndWithActions) {
        if (insetEndWithActions < 0) {
            insetEndWithActions = Integer.MIN_VALUE;
        }
        if (insetEndWithActions != this.mContentInsetEndWithActions) {
            this.mContentInsetEndWithActions = insetEndWithActions;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    public void setContentInsetStartWithNavigation(int insetStartWithNavigation) {
        if (insetStartWithNavigation < 0) {
            insetStartWithNavigation = Integer.MIN_VALUE;
        }
        if (insetStartWithNavigation != this.mContentInsetStartWithNavigation) {
            this.mContentInsetStartWithNavigation = insetStartWithNavigation;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    public void setContentInsetsAbsolute(int contentInsetLeft, int contentInsetRight) {
        ensureContentInsets();
        this.mContentInsets.setAbsolute(contentInsetLeft, contentInsetRight);
    }

    public void setContentInsetsRelative(int contentInsetStart, int contentInsetEnd) {
        ensureContentInsets();
        this.mContentInsets.setRelative(contentInsetStart, contentInsetEnd);
    }

    public void setLogo(int resId) {
        setLogo(AppCompatResources.getDrawable(getContext(), resId));
    }

    public void setLogo(Drawable drawable) {
        if (drawable != null) {
            ensureLogoView();
            if (!isChildOrHidden(this.mLogoView)) {
                addSystemView(this.mLogoView, true);
            }
        } else {
            ImageView imageView = this.mLogoView;
            if (imageView != null && isChildOrHidden(imageView)) {
                removeView(this.mLogoView);
                this.mHiddenViews.remove(this.mLogoView);
            }
        }
        ImageView imageView2 = this.mLogoView;
        if (imageView2 != null) {
            imageView2.setImageDrawable(drawable);
        }
    }

    public void setLogoDescription(int resId) {
        setLogoDescription(getContext().getText(resId));
    }

    public void setLogoDescription(CharSequence description) {
        if (!TextUtils.isEmpty(description)) {
            ensureLogoView();
        }
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            imageView.setContentDescription(description);
        }
    }

    public void setMenu(MenuBuilder menu, ActionMenuPresenter outerPresenter) {
        if (menu != null || this.mMenuView != null) {
            ensureMenuView();
            MenuBuilder peekMenu = this.mMenuView.peekMenu();
            if (peekMenu != menu) {
                if (peekMenu != null) {
                    peekMenu.removeMenuPresenter(this.mOuterActionMenuPresenter);
                    peekMenu.removeMenuPresenter(this.mExpandedMenuPresenter);
                }
                if (this.mExpandedMenuPresenter == null) {
                    this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
                }
                outerPresenter.setExpandedActionViewsExclusive(true);
                if (menu != null) {
                    menu.addMenuPresenter(outerPresenter, this.mPopupContext);
                    menu.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
                } else {
                    outerPresenter.initForMenu(this.mPopupContext, (MenuBuilder) null);
                    this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, (MenuBuilder) null);
                    outerPresenter.updateMenuView(true);
                    this.mExpandedMenuPresenter.updateMenuView(true);
                }
                this.mMenuView.setPopupTheme(this.mPopupTheme);
                this.mMenuView.setPresenter(outerPresenter);
                this.mOuterActionMenuPresenter = outerPresenter;
            }
        }
    }

    public void setMenuCallbacks(MenuPresenter.Callback pcb, MenuBuilder.Callback mcb) {
        this.mActionMenuPresenterCallback = pcb;
        this.mMenuBuilderCallback = mcb;
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            actionMenuView.setMenuCallbacks(pcb, mcb);
        }
    }

    public void setNavigationContentDescription(int resId) {
        setNavigationContentDescription(resId != 0 ? getContext().getText(resId) : null);
    }

    public void setNavigationContentDescription(CharSequence description) {
        if (!TextUtils.isEmpty(description)) {
            ensureNavButtonView();
        }
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            imageButton.setContentDescription(description);
            TooltipCompat.setTooltipText(this.mNavButtonView, description);
        }
    }

    public void setNavigationIcon(int resId) {
        setNavigationIcon(AppCompatResources.getDrawable(getContext(), resId));
    }

    public void setNavigationIcon(Drawable icon) {
        if (icon != null) {
            ensureNavButtonView();
            if (!isChildOrHidden(this.mNavButtonView)) {
                addSystemView(this.mNavButtonView, true);
            }
        } else {
            ImageButton imageButton = this.mNavButtonView;
            if (imageButton != null && isChildOrHidden(imageButton)) {
                removeView(this.mNavButtonView);
                this.mHiddenViews.remove(this.mNavButtonView);
            }
        }
        ImageButton imageButton2 = this.mNavButtonView;
        if (imageButton2 != null) {
            imageButton2.setImageDrawable(icon);
        }
    }

    public void setNavigationOnClickListener(View.OnClickListener listener) {
        ensureNavButtonView();
        this.mNavButtonView.setOnClickListener(listener);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.mOnMenuItemClickListener = listener;
    }

    public void setOverflowIcon(Drawable icon) {
        ensureMenu();
        this.mMenuView.setOverflowIcon(icon);
    }

    public void setPopupTheme(int resId) {
        if (this.mPopupTheme != resId) {
            this.mPopupTheme = resId;
            if (resId == 0) {
                this.mPopupContext = getContext();
            } else {
                this.mPopupContext = new ContextThemeWrapper(getContext(), resId);
            }
        }
    }

    public void setSubtitle(int resId) {
        setSubtitle(getContext().getText(resId));
    }

    public void setSubtitle(CharSequence subtitle) {
        if (!TextUtils.isEmpty(subtitle)) {
            if (this.mSubtitleTextView == null) {
                Context context = getContext();
                AppCompatTextView appCompatTextView = new AppCompatTextView(context);
                this.mSubtitleTextView = appCompatTextView;
                appCompatTextView.setSingleLine();
                this.mSubtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                int i = this.mSubtitleTextAppearance;
                if (i != 0) {
                    this.mSubtitleTextView.setTextAppearance(context, i);
                }
                ColorStateList colorStateList = this.mSubtitleTextColor;
                if (colorStateList != null) {
                    this.mSubtitleTextView.setTextColor(colorStateList);
                }
            }
            if (!isChildOrHidden(this.mSubtitleTextView)) {
                addSystemView(this.mSubtitleTextView, true);
            }
        } else {
            TextView textView = this.mSubtitleTextView;
            if (textView != null && isChildOrHidden(textView)) {
                removeView(this.mSubtitleTextView);
                this.mHiddenViews.remove(this.mSubtitleTextView);
            }
        }
        TextView textView2 = this.mSubtitleTextView;
        if (textView2 != null) {
            textView2.setText(subtitle);
        }
        this.mSubtitleText = subtitle;
    }

    public void setSubtitleTextAppearance(Context context, int resId) {
        this.mSubtitleTextAppearance = resId;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextAppearance(context, resId);
        }
    }

    public void setSubtitleTextColor(int color) {
        setSubtitleTextColor(ColorStateList.valueOf(color));
    }

    public void setSubtitleTextColor(ColorStateList color) {
        this.mSubtitleTextColor = color;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextColor(color);
        }
    }

    public void setTitle(int resId) {
        setTitle(getContext().getText(resId));
    }

    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            if (this.mTitleTextView == null) {
                Context context = getContext();
                AppCompatTextView appCompatTextView = new AppCompatTextView(context);
                this.mTitleTextView = appCompatTextView;
                appCompatTextView.setSingleLine();
                this.mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                int i = this.mTitleTextAppearance;
                if (i != 0) {
                    this.mTitleTextView.setTextAppearance(context, i);
                }
                ColorStateList colorStateList = this.mTitleTextColor;
                if (colorStateList != null) {
                    this.mTitleTextView.setTextColor(colorStateList);
                }
            }
            if (!isChildOrHidden(this.mTitleTextView)) {
                addSystemView(this.mTitleTextView, true);
            }
        } else {
            TextView textView = this.mTitleTextView;
            if (textView != null && isChildOrHidden(textView)) {
                removeView(this.mTitleTextView);
                this.mHiddenViews.remove(this.mTitleTextView);
            }
        }
        TextView textView2 = this.mTitleTextView;
        if (textView2 != null) {
            textView2.setText(title);
        }
        this.mTitleText = title;
    }

    public void setTitleMargin(int start, int top, int end, int bottom) {
        this.mTitleMarginStart = start;
        this.mTitleMarginTop = top;
        this.mTitleMarginEnd = end;
        this.mTitleMarginBottom = bottom;
        requestLayout();
    }

    public void setTitleMarginBottom(int margin) {
        this.mTitleMarginBottom = margin;
        requestLayout();
    }

    public void setTitleMarginEnd(int margin) {
        this.mTitleMarginEnd = margin;
        requestLayout();
    }

    public void setTitleMarginStart(int margin) {
        this.mTitleMarginStart = margin;
        requestLayout();
    }

    public void setTitleMarginTop(int margin) {
        this.mTitleMarginTop = margin;
        requestLayout();
    }

    public void setTitleTextAppearance(Context context, int resId) {
        this.mTitleTextAppearance = resId;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextAppearance(context, resId);
        }
    }

    public void setTitleTextColor(int color) {
        setTitleTextColor(ColorStateList.valueOf(color));
    }

    public void setTitleTextColor(ColorStateList color) {
        this.mTitleTextColor = color;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextColor(color);
        }
    }

    public boolean showOverflowMenu() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.showOverflowMenu();
    }
}

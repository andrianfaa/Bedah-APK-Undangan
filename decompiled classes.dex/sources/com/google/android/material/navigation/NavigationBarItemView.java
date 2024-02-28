package com.google.android.material.navigation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.motion.MotionUtils;
import com.google.android.material.resources.MaterialResources;

public abstract class NavigationBarItemView extends FrameLayout implements MenuView.ItemView {
    private static final ActiveIndicatorTransform ACTIVE_INDICATOR_LABELED_TRANSFORM = new ActiveIndicatorTransform();
    private static final ActiveIndicatorTransform ACTIVE_INDICATOR_UNLABELED_TRANSFORM = new ActiveIndicatorUnlabeledTransform();
    private static final int[] CHECKED_STATE_SET = {16842912};
    private static final int INVALID_ITEM_POSITION = -1;
    private ValueAnimator activeIndicatorAnimator;
    private int activeIndicatorDesiredHeight = 0;
    private int activeIndicatorDesiredWidth = 0;
    private boolean activeIndicatorEnabled = false;
    private int activeIndicatorMarginHorizontal = 0;
    private float activeIndicatorProgress = 0.0f;
    private boolean activeIndicatorResizeable = false;
    private ActiveIndicatorTransform activeIndicatorTransform = ACTIVE_INDICATOR_LABELED_TRANSFORM;
    private final View activeIndicatorView;
    private BadgeDrawable badgeDrawable;
    /* access modifiers changed from: private */
    public final ImageView icon;
    private final FrameLayout iconContainer;
    private ColorStateList iconTint;
    private boolean initialized = false;
    private boolean isShifting;
    private MenuItemImpl itemData;
    private int itemPaddingBottom;
    private int itemPaddingTop;
    private int itemPosition = -1;
    private final ViewGroup labelGroup;
    private int labelVisibilityMode;
    private final TextView largeLabel;
    private Drawable originalIconDrawable;
    private float scaleDownFactor;
    private float scaleUpFactor;
    private float shiftAmount;
    private final TextView smallLabel;
    private Drawable wrappedIconDrawable;

    private static class ActiveIndicatorTransform {
        private static final float ALPHA_FRACTION = 0.2f;
        private static final float SCALE_X_HIDDEN = 0.4f;
        private static final float SCALE_X_SHOWN = 1.0f;

        private ActiveIndicatorTransform() {
        }

        /* access modifiers changed from: protected */
        public float calculateAlpha(float progress, float targetValue) {
            return AnimationUtils.lerp(0.0f, 1.0f, targetValue == 0.0f ? 0.8f : 0.0f, targetValue == 0.0f ? 1.0f : 0.2f, progress);
        }

        /* access modifiers changed from: protected */
        public float calculateScaleX(float progress, float targetValue) {
            return AnimationUtils.lerp((float) SCALE_X_HIDDEN, 1.0f, progress);
        }

        /* access modifiers changed from: protected */
        public float calculateScaleY(float progress, float targetValue) {
            return 1.0f;
        }

        public void updateForProgress(float progress, float targetValue, View indicator) {
            indicator.setScaleX(calculateScaleX(progress, targetValue));
            indicator.setScaleY(calculateScaleY(progress, targetValue));
            indicator.setAlpha(calculateAlpha(progress, targetValue));
        }
    }

    private static class ActiveIndicatorUnlabeledTransform extends ActiveIndicatorTransform {
        private ActiveIndicatorUnlabeledTransform() {
            super();
        }

        /* access modifiers changed from: protected */
        public float calculateScaleY(float progress, float targetValue) {
            return calculateScaleX(progress, targetValue);
        }
    }

    public NavigationBarItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(getItemLayoutResId(), this, true);
        this.iconContainer = (FrameLayout) findViewById(R.id.navigation_bar_item_icon_container);
        this.activeIndicatorView = findViewById(R.id.navigation_bar_item_active_indicator_view);
        ImageView imageView = (ImageView) findViewById(R.id.navigation_bar_item_icon_view);
        this.icon = imageView;
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.navigation_bar_item_labels_group);
        this.labelGroup = viewGroup;
        TextView textView = (TextView) findViewById(R.id.navigation_bar_item_small_label_view);
        this.smallLabel = textView;
        TextView textView2 = (TextView) findViewById(R.id.navigation_bar_item_large_label_view);
        this.largeLabel = textView2;
        setBackgroundResource(getItemBackgroundResId());
        this.itemPaddingTop = getResources().getDimensionPixelSize(getItemDefaultMarginResId());
        this.itemPaddingBottom = viewGroup.getPaddingBottom();
        ViewCompat.setImportantForAccessibility(textView, 2);
        ViewCompat.setImportantForAccessibility(textView2, 2);
        setFocusable(true);
        calculateTextScaleFactors(textView.getTextSize(), textView2.getTextSize());
        if (imageView != null) {
            imageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (NavigationBarItemView.this.icon.getVisibility() == 0) {
                        NavigationBarItemView navigationBarItemView = NavigationBarItemView.this;
                        navigationBarItemView.tryUpdateBadgeBounds(navigationBarItemView.icon);
                    }
                }
            });
        }
    }

    private void calculateTextScaleFactors(float smallLabelSize, float largeLabelSize) {
        this.shiftAmount = smallLabelSize - largeLabelSize;
        this.scaleUpFactor = (largeLabelSize * 1.0f) / smallLabelSize;
        this.scaleDownFactor = (1.0f * smallLabelSize) / largeLabelSize;
    }

    private FrameLayout getCustomParentForBadge(View anchorView) {
        if (anchorView != this.icon || !BadgeUtils.USE_COMPAT_PARENT) {
            return null;
        }
        return (FrameLayout) this.icon.getParent();
    }

    private View getIconOrContainer() {
        FrameLayout frameLayout = this.iconContainer;
        return frameLayout != null ? frameLayout : this.icon;
    }

    private int getItemVisiblePosition() {
        ViewGroup viewGroup = (ViewGroup) getParent();
        int indexOfChild = viewGroup.indexOfChild(this);
        int i = 0;
        for (int i2 = 0; i2 < indexOfChild; i2++) {
            View childAt = viewGroup.getChildAt(i2);
            if ((childAt instanceof NavigationBarItemView) && childAt.getVisibility() == 0) {
                i++;
            }
        }
        return i;
    }

    private int getSuggestedIconHeight() {
        int i = 0;
        BadgeDrawable badgeDrawable2 = this.badgeDrawable;
        if (badgeDrawable2 != null) {
            i = badgeDrawable2.getMinimumHeight() / 2;
        }
        return Math.max(i, ((FrameLayout.LayoutParams) getIconOrContainer().getLayoutParams()).topMargin) + this.icon.getMeasuredWidth() + i;
    }

    private int getSuggestedIconWidth() {
        BadgeDrawable badgeDrawable2 = this.badgeDrawable;
        int minimumWidth = badgeDrawable2 == null ? 0 : badgeDrawable2.getMinimumWidth() - this.badgeDrawable.getHorizontalOffset();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getIconOrContainer().getLayoutParams();
        return Math.max(minimumWidth, layoutParams.leftMargin) + this.icon.getMeasuredWidth() + Math.max(minimumWidth, layoutParams.rightMargin);
    }

    private boolean hasBadge() {
        return this.badgeDrawable != null;
    }

    private boolean isActiveIndicatorResizeableAndUnlabeled() {
        return this.activeIndicatorResizeable && this.labelVisibilityMode == 2;
    }

    private void maybeAnimateActiveIndicatorToProgress(final float newProgress) {
        if (!this.activeIndicatorEnabled || !this.initialized || !ViewCompat.isAttachedToWindow(this)) {
            setActiveIndicatorProgress(newProgress, newProgress);
            return;
        }
        ValueAnimator valueAnimator = this.activeIndicatorAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.activeIndicatorAnimator = null;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.activeIndicatorProgress, newProgress});
        this.activeIndicatorAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                NavigationBarItemView.this.setActiveIndicatorProgress(((Float) animation.getAnimatedValue()).floatValue(), newProgress);
            }
        });
        this.activeIndicatorAnimator.setInterpolator(MotionUtils.resolveThemeInterpolator(getContext(), R.attr.motionEasingStandard, AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR));
        this.activeIndicatorAnimator.setDuration((long) MotionUtils.resolveThemeDuration(getContext(), R.attr.motionDurationLong1, getResources().getInteger(R.integer.material_motion_duration_long_1)));
        this.activeIndicatorAnimator.start();
    }

    private void refreshChecked() {
        MenuItemImpl menuItemImpl = this.itemData;
        if (menuItemImpl != null) {
            setChecked(menuItemImpl.isChecked());
        }
    }

    /* access modifiers changed from: private */
    public void setActiveIndicatorProgress(float progress, float target) {
        View view = this.activeIndicatorView;
        if (view != null) {
            this.activeIndicatorTransform.updateForProgress(progress, target, view);
        }
        this.activeIndicatorProgress = progress;
    }

    private static void setTextAppearanceWithoutFontScaling(TextView textView, int textAppearance) {
        TextViewCompat.setTextAppearance(textView, textAppearance);
        int unscaledTextSize = MaterialResources.getUnscaledTextSize(textView.getContext(), textAppearance, 0);
        if (unscaledTextSize != 0) {
            textView.setTextSize(0, (float) unscaledTextSize);
        }
    }

    private static void setViewScaleValues(View view, float scaleX, float scaleY, int visibility) {
        view.setScaleX(scaleX);
        view.setScaleY(scaleY);
        view.setVisibility(visibility);
    }

    private static void setViewTopMarginAndGravity(View view, int topMargin, int gravity) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.topMargin = topMargin;
        layoutParams.bottomMargin = topMargin;
        layoutParams.gravity = gravity;
        view.setLayoutParams(layoutParams);
    }

    private void tryAttachBadgeToAnchor(View anchorView) {
        if (hasBadge() && anchorView != null) {
            setClipChildren(false);
            setClipToPadding(false);
            BadgeUtils.attachBadgeDrawable(this.badgeDrawable, anchorView, getCustomParentForBadge(anchorView));
        }
    }

    private void tryRemoveBadgeFromAnchor(View anchorView) {
        if (hasBadge()) {
            if (anchorView != null) {
                setClipChildren(true);
                setClipToPadding(true);
                BadgeUtils.detachBadgeDrawable(this.badgeDrawable, anchorView);
            }
            this.badgeDrawable = null;
        }
    }

    /* access modifiers changed from: private */
    public void tryUpdateBadgeBounds(View anchorView) {
        if (hasBadge()) {
            BadgeUtils.setBadgeDrawableBounds(this.badgeDrawable, anchorView, getCustomParentForBadge(anchorView));
        }
    }

    /* access modifiers changed from: private */
    public void updateActiveIndicatorLayoutParams(int availableWidth) {
        if (this.activeIndicatorView != null) {
            int min = Math.min(this.activeIndicatorDesiredWidth, availableWidth - (this.activeIndicatorMarginHorizontal * 2));
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.activeIndicatorView.getLayoutParams();
            layoutParams.height = isActiveIndicatorResizeableAndUnlabeled() ? min : this.activeIndicatorDesiredHeight;
            layoutParams.width = min;
            this.activeIndicatorView.setLayoutParams(layoutParams);
        }
    }

    private void updateActiveIndicatorTransform() {
        if (isActiveIndicatorResizeableAndUnlabeled()) {
            this.activeIndicatorTransform = ACTIVE_INDICATOR_UNLABELED_TRANSFORM;
        } else {
            this.activeIndicatorTransform = ACTIVE_INDICATOR_LABELED_TRANSFORM;
        }
    }

    private static void updateViewPaddingBottom(View view, int paddingBottom) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), paddingBottom);
    }

    /* access modifiers changed from: package-private */
    public void clear() {
        removeBadge();
        this.itemData = null;
        this.activeIndicatorProgress = 0.0f;
        this.initialized = false;
    }

    public Drawable getActiveIndicatorDrawable() {
        View view = this.activeIndicatorView;
        if (view == null) {
            return null;
        }
        return view.getBackground();
    }

    public BadgeDrawable getBadge() {
        return this.badgeDrawable;
    }

    /* access modifiers changed from: protected */
    public int getItemBackgroundResId() {
        return R.drawable.mtrl_navigation_bar_item_background;
    }

    public MenuItemImpl getItemData() {
        return this.itemData;
    }

    /* access modifiers changed from: protected */
    public int getItemDefaultMarginResId() {
        return R.dimen.mtrl_navigation_bar_item_default_margin;
    }

    /* access modifiers changed from: protected */
    public abstract int getItemLayoutResId();

    public int getItemPosition() {
        return this.itemPosition;
    }

    /* access modifiers changed from: protected */
    public int getSuggestedMinimumHeight() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.labelGroup.getLayoutParams();
        return getSuggestedIconHeight() + layoutParams.topMargin + this.labelGroup.getMeasuredHeight() + layoutParams.bottomMargin;
    }

    /* access modifiers changed from: protected */
    public int getSuggestedMinimumWidth() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.labelGroup.getLayoutParams();
        return Math.max(getSuggestedIconWidth(), layoutParams.leftMargin + this.labelGroup.getMeasuredWidth() + layoutParams.rightMargin);
    }

    public void initialize(MenuItemImpl itemData2, int menuType) {
        this.itemData = itemData2;
        setCheckable(itemData2.isCheckable());
        setChecked(itemData2.isChecked());
        setEnabled(itemData2.isEnabled());
        setIcon(itemData2.getIcon());
        setTitle(itemData2.getTitle());
        setId(itemData2.getItemId());
        if (!TextUtils.isEmpty(itemData2.getContentDescription())) {
            setContentDescription(itemData2.getContentDescription());
        }
        CharSequence tooltipText = !TextUtils.isEmpty(itemData2.getTooltipText()) ? itemData2.getTooltipText() : itemData2.getTitle();
        if (Build.VERSION.SDK_INT < 21 || Build.VERSION.SDK_INT > 23) {
            TooltipCompat.setTooltipText(this, tooltipText);
        }
        setVisibility(itemData2.isVisible() ? 0 : 8);
        this.initialized = true;
    }

    public int[] onCreateDrawableState(int extraSpace) {
        int[] onCreateDrawableState = super.onCreateDrawableState(extraSpace + 1);
        MenuItemImpl menuItemImpl = this.itemData;
        if (menuItemImpl != null && menuItemImpl.isCheckable() && this.itemData.isChecked()) {
            mergeDrawableStates(onCreateDrawableState, CHECKED_STATE_SET);
        }
        return onCreateDrawableState;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        BadgeDrawable badgeDrawable2 = this.badgeDrawable;
        if (badgeDrawable2 != null && badgeDrawable2.isVisible()) {
            CharSequence title = this.itemData.getTitle();
            if (!TextUtils.isEmpty(this.itemData.getContentDescription())) {
                title = this.itemData.getContentDescription();
            }
            info.setContentDescription(title + ", " + this.badgeDrawable.getContentDescription());
        }
        AccessibilityNodeInfoCompat wrap = AccessibilityNodeInfoCompat.wrap(info);
        wrap.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(0, 1, getItemVisiblePosition(), 1, false, isSelected()));
        if (isSelected()) {
            wrap.setClickable(false);
            wrap.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
        }
        wrap.setRoleDescription(getResources().getString(R.string.item_view_role_description));
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        final int i = w;
        post(new Runnable() {
            public void run() {
                NavigationBarItemView.this.updateActiveIndicatorLayoutParams(i);
            }
        });
    }

    public boolean prefersCondensedTitle() {
        return false;
    }

    /* access modifiers changed from: package-private */
    public void removeBadge() {
        tryRemoveBadgeFromAnchor(this.icon);
    }

    public void setActiveIndicatorDrawable(Drawable activeIndicatorDrawable) {
        View view = this.activeIndicatorView;
        if (view != null) {
            view.setBackgroundDrawable(activeIndicatorDrawable);
        }
    }

    public void setActiveIndicatorEnabled(boolean enabled) {
        this.activeIndicatorEnabled = enabled;
        View view = this.activeIndicatorView;
        if (view != null) {
            view.setVisibility(enabled ? 0 : 8);
            requestLayout();
        }
    }

    public void setActiveIndicatorHeight(int height) {
        this.activeIndicatorDesiredHeight = height;
        updateActiveIndicatorLayoutParams(getWidth());
    }

    public void setActiveIndicatorMarginHorizontal(int marginHorizontal) {
        this.activeIndicatorMarginHorizontal = marginHorizontal;
        updateActiveIndicatorLayoutParams(getWidth());
    }

    public void setActiveIndicatorResizeable(boolean resizeable) {
        this.activeIndicatorResizeable = resizeable;
    }

    public void setActiveIndicatorWidth(int width) {
        this.activeIndicatorDesiredWidth = width;
        updateActiveIndicatorLayoutParams(getWidth());
    }

    /* access modifiers changed from: package-private */
    public void setBadge(BadgeDrawable badgeDrawable2) {
        if (this.badgeDrawable != badgeDrawable2) {
            if (hasBadge() && this.icon != null) {
                Log.w("NavigationBar", "Multiple badges shouldn't be attached to one item.");
                tryRemoveBadgeFromAnchor(this.icon);
            }
            this.badgeDrawable = badgeDrawable2;
            ImageView imageView = this.icon;
            if (imageView != null) {
                tryAttachBadgeToAnchor(imageView);
            }
        }
    }

    public void setCheckable(boolean checkable) {
        refreshDrawableState();
    }

    public void setChecked(boolean checked) {
        TextView textView = this.largeLabel;
        textView.setPivotX((float) (textView.getWidth() / 2));
        TextView textView2 = this.largeLabel;
        textView2.setPivotY((float) textView2.getBaseline());
        TextView textView3 = this.smallLabel;
        textView3.setPivotX((float) (textView3.getWidth() / 2));
        TextView textView4 = this.smallLabel;
        textView4.setPivotY((float) textView4.getBaseline());
        maybeAnimateActiveIndicatorToProgress(checked ? 1.0f : 0.0f);
        switch (this.labelVisibilityMode) {
            case -1:
                if (!this.isShifting) {
                    updateViewPaddingBottom(this.labelGroup, this.itemPaddingBottom);
                    if (!checked) {
                        setViewTopMarginAndGravity(getIconOrContainer(), this.itemPaddingTop, 49);
                        TextView textView5 = this.largeLabel;
                        float f = this.scaleDownFactor;
                        setViewScaleValues(textView5, f, f, 4);
                        setViewScaleValues(this.smallLabel, 1.0f, 1.0f, 0);
                        break;
                    } else {
                        setViewTopMarginAndGravity(getIconOrContainer(), (int) (((float) this.itemPaddingTop) + this.shiftAmount), 49);
                        setViewScaleValues(this.largeLabel, 1.0f, 1.0f, 0);
                        TextView textView6 = this.smallLabel;
                        float f2 = this.scaleUpFactor;
                        setViewScaleValues(textView6, f2, f2, 4);
                        break;
                    }
                } else {
                    if (checked) {
                        setViewTopMarginAndGravity(getIconOrContainer(), this.itemPaddingTop, 49);
                        updateViewPaddingBottom(this.labelGroup, this.itemPaddingBottom);
                        this.largeLabel.setVisibility(0);
                    } else {
                        setViewTopMarginAndGravity(getIconOrContainer(), this.itemPaddingTop, 17);
                        updateViewPaddingBottom(this.labelGroup, 0);
                        this.largeLabel.setVisibility(4);
                    }
                    this.smallLabel.setVisibility(4);
                    break;
                }
            case 0:
                if (checked) {
                    setViewTopMarginAndGravity(getIconOrContainer(), this.itemPaddingTop, 49);
                    updateViewPaddingBottom(this.labelGroup, this.itemPaddingBottom);
                    this.largeLabel.setVisibility(0);
                } else {
                    setViewTopMarginAndGravity(getIconOrContainer(), this.itemPaddingTop, 17);
                    updateViewPaddingBottom(this.labelGroup, 0);
                    this.largeLabel.setVisibility(4);
                }
                this.smallLabel.setVisibility(4);
                break;
            case 1:
                updateViewPaddingBottom(this.labelGroup, this.itemPaddingBottom);
                if (!checked) {
                    setViewTopMarginAndGravity(getIconOrContainer(), this.itemPaddingTop, 49);
                    TextView textView7 = this.largeLabel;
                    float f3 = this.scaleDownFactor;
                    setViewScaleValues(textView7, f3, f3, 4);
                    setViewScaleValues(this.smallLabel, 1.0f, 1.0f, 0);
                    break;
                } else {
                    setViewTopMarginAndGravity(getIconOrContainer(), (int) (((float) this.itemPaddingTop) + this.shiftAmount), 49);
                    setViewScaleValues(this.largeLabel, 1.0f, 1.0f, 0);
                    TextView textView8 = this.smallLabel;
                    float f4 = this.scaleUpFactor;
                    setViewScaleValues(textView8, f4, f4, 4);
                    break;
                }
            case 2:
                setViewTopMarginAndGravity(getIconOrContainer(), this.itemPaddingTop, 17);
                this.largeLabel.setVisibility(8);
                this.smallLabel.setVisibility(8);
                break;
        }
        refreshDrawableState();
        setSelected(checked);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.smallLabel.setEnabled(enabled);
        this.largeLabel.setEnabled(enabled);
        this.icon.setEnabled(enabled);
        if (enabled) {
            ViewCompat.setPointerIcon(this, PointerIconCompat.getSystemIcon(getContext(), PointerIconCompat.TYPE_HAND));
        } else {
            ViewCompat.setPointerIcon(this, (PointerIconCompat) null);
        }
    }

    public void setIcon(Drawable iconDrawable) {
        if (iconDrawable != this.originalIconDrawable) {
            this.originalIconDrawable = iconDrawable;
            if (iconDrawable != null) {
                Drawable.ConstantState constantState = iconDrawable.getConstantState();
                iconDrawable = DrawableCompat.wrap(constantState == null ? iconDrawable : constantState.newDrawable()).mutate();
                this.wrappedIconDrawable = iconDrawable;
                ColorStateList colorStateList = this.iconTint;
                if (colorStateList != null) {
                    DrawableCompat.setTintList(iconDrawable, colorStateList);
                }
            }
            this.icon.setImageDrawable(iconDrawable);
        }
    }

    public void setIconSize(int iconSize) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.icon.getLayoutParams();
        layoutParams.width = iconSize;
        layoutParams.height = iconSize;
        this.icon.setLayoutParams(layoutParams);
    }

    public void setIconTintList(ColorStateList tint) {
        Drawable drawable;
        this.iconTint = tint;
        if (this.itemData != null && (drawable = this.wrappedIconDrawable) != null) {
            DrawableCompat.setTintList(drawable, tint);
            this.wrappedIconDrawable.invalidateSelf();
        }
    }

    public void setItemBackground(int background) {
        setItemBackground(background == 0 ? null : ContextCompat.getDrawable(getContext(), background));
    }

    public void setItemBackground(Drawable background) {
        if (!(background == null || background.getConstantState() == null)) {
            background = background.getConstantState().newDrawable().mutate();
        }
        ViewCompat.setBackground(this, background);
    }

    public void setItemPaddingBottom(int paddingBottom) {
        if (this.itemPaddingBottom != paddingBottom) {
            this.itemPaddingBottom = paddingBottom;
            refreshChecked();
        }
    }

    public void setItemPaddingTop(int paddingTop) {
        if (this.itemPaddingTop != paddingTop) {
            this.itemPaddingTop = paddingTop;
            refreshChecked();
        }
    }

    public void setItemPosition(int position) {
        this.itemPosition = position;
    }

    public void setLabelVisibilityMode(int mode) {
        if (this.labelVisibilityMode != mode) {
            this.labelVisibilityMode = mode;
            updateActiveIndicatorTransform();
            updateActiveIndicatorLayoutParams(getWidth());
            refreshChecked();
        }
    }

    public void setShifting(boolean shifting) {
        if (this.isShifting != shifting) {
            this.isShifting = shifting;
            refreshChecked();
        }
    }

    public void setShortcut(boolean showShortcut, char shortcutKey) {
    }

    public void setTextAppearanceActive(int activeTextAppearance) {
        setTextAppearanceWithoutFontScaling(this.largeLabel, activeTextAppearance);
        calculateTextScaleFactors(this.smallLabel.getTextSize(), this.largeLabel.getTextSize());
    }

    public void setTextAppearanceInactive(int inactiveTextAppearance) {
        setTextAppearanceWithoutFontScaling(this.smallLabel, inactiveTextAppearance);
        calculateTextScaleFactors(this.smallLabel.getTextSize(), this.largeLabel.getTextSize());
    }

    public void setTextColor(ColorStateList color) {
        if (color != null) {
            this.smallLabel.setTextColor(color);
            this.largeLabel.setTextColor(color);
        }
    }

    public void setTitle(CharSequence title) {
        this.smallLabel.setText(title);
        this.largeLabel.setText(title);
        MenuItemImpl menuItemImpl = this.itemData;
        if (menuItemImpl == null || TextUtils.isEmpty(menuItemImpl.getContentDescription())) {
            setContentDescription(title);
        }
        MenuItemImpl menuItemImpl2 = this.itemData;
        CharSequence tooltipText = (menuItemImpl2 == null || TextUtils.isEmpty(menuItemImpl2.getTooltipText())) ? title : this.itemData.getTooltipText();
        if (Build.VERSION.SDK_INT < 21 || Build.VERSION.SDK_INT > 23) {
            TooltipCompat.setTooltipText(this, tooltipText);
        }
    }

    public boolean showsIcon() {
        return true;
    }
}

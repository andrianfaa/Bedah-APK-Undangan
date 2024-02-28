package com.google.android.material.tabs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.TooltipCompat;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Pools;
import androidx.core.view.GravityCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.MaterialShapeUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

@ViewPager.DecorView
public class TabLayout extends HorizontalScrollView {
    private static final int ANIMATION_DURATION = 300;
    static final int DEFAULT_GAP_TEXT_ICON = 8;
    private static final int DEFAULT_HEIGHT = 48;
    private static final int DEFAULT_HEIGHT_WITH_TEXT_ICON = 72;
    private static final int DEF_STYLE_RES = R.style.Widget_Design_TabLayout;
    static final int FIXED_WRAP_GUTTER_MIN = 16;
    public static final int GRAVITY_CENTER = 1;
    public static final int GRAVITY_FILL = 0;
    public static final int GRAVITY_START = 2;
    public static final int INDICATOR_ANIMATION_MODE_ELASTIC = 1;
    public static final int INDICATOR_ANIMATION_MODE_FADE = 2;
    public static final int INDICATOR_ANIMATION_MODE_LINEAR = 0;
    public static final int INDICATOR_GRAVITY_BOTTOM = 0;
    public static final int INDICATOR_GRAVITY_CENTER = 1;
    public static final int INDICATOR_GRAVITY_STRETCH = 3;
    public static final int INDICATOR_GRAVITY_TOP = 2;
    private static final int INVALID_WIDTH = -1;
    private static final String LOG_TAG = "TabLayout";
    public static final int MODE_AUTO = 2;
    public static final int MODE_FIXED = 1;
    public static final int MODE_SCROLLABLE = 0;
    public static final int TAB_LABEL_VISIBILITY_LABELED = 1;
    public static final int TAB_LABEL_VISIBILITY_UNLABELED = 0;
    private static final int TAB_MIN_WIDTH_MARGIN = 56;
    private static final Pools.Pool<Tab> tabPool = new Pools.SynchronizedPool(16);
    private AdapterChangeListener adapterChangeListener;
    private int contentInsetStart;
    private BaseOnTabSelectedListener currentVpSelectedListener;
    boolean inlineLabel;
    int mode;
    private TabLayoutOnPageChangeListener pageChangeListener;
    private PagerAdapter pagerAdapter;
    private DataSetObserver pagerAdapterObserver;
    private final int requestedTabMaxWidth;
    private final int requestedTabMinWidth;
    private ValueAnimator scrollAnimator;
    private final int scrollableTabMinWidth;
    private BaseOnTabSelectedListener selectedListener;
    private final ArrayList<BaseOnTabSelectedListener> selectedListeners;
    private Tab selectedTab;
    private boolean setupViewPagerImplicitly;
    final SlidingTabIndicator slidingTabIndicator;
    final int tabBackgroundResId;
    int tabGravity;
    ColorStateList tabIconTint;
    PorterDuff.Mode tabIconTintMode;
    int tabIndicatorAnimationDuration;
    int tabIndicatorAnimationMode;
    boolean tabIndicatorFullWidth;
    int tabIndicatorGravity;
    int tabIndicatorHeight;
    /* access modifiers changed from: private */
    public TabIndicatorInterpolator tabIndicatorInterpolator;
    int tabMaxWidth;
    int tabPaddingBottom;
    int tabPaddingEnd;
    int tabPaddingStart;
    int tabPaddingTop;
    ColorStateList tabRippleColorStateList;
    Drawable tabSelectedIndicator;
    /* access modifiers changed from: private */
    public int tabSelectedIndicatorColor;
    int tabTextAppearance;
    ColorStateList tabTextColors;
    float tabTextMultiLineSize;
    float tabTextSize;
    private final Pools.Pool<TabView> tabViewPool;
    private final ArrayList<Tab> tabs;
    boolean unboundedRipple;
    ViewPager viewPager;

    private class AdapterChangeListener implements ViewPager.OnAdapterChangeListener {
        private boolean autoRefresh;

        AdapterChangeListener() {
        }

        public void onAdapterChanged(ViewPager viewPager, PagerAdapter oldAdapter, PagerAdapter newAdapter) {
            if (TabLayout.this.viewPager == viewPager) {
                TabLayout.this.setPagerAdapter(newAdapter, this.autoRefresh);
            }
        }

        /* access modifiers changed from: package-private */
        public void setAutoRefresh(boolean autoRefresh2) {
            this.autoRefresh = autoRefresh2;
        }
    }

    @Deprecated
    public interface BaseOnTabSelectedListener<T extends Tab> {
        void onTabReselected(T t);

        void onTabSelected(T t);

        void onTabUnselected(T t);
    }

    public @interface LabelVisibility {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    public interface OnTabSelectedListener extends BaseOnTabSelectedListener<Tab> {
    }

    private class PagerAdapterObserver extends DataSetObserver {
        PagerAdapterObserver() {
        }

        public void onChanged() {
            TabLayout.this.populateFromPagerAdapter();
        }

        public void onInvalidated() {
            TabLayout.this.populateFromPagerAdapter();
        }
    }

    class SlidingTabIndicator extends LinearLayout {
        ValueAnimator indicatorAnimator;
        private int layoutDirection = -1;
        int selectedPosition = -1;
        float selectionOffset;

        SlidingTabIndicator(Context context) {
            super(context);
            setWillNotDraw(false);
        }

        /* access modifiers changed from: private */
        public void jumpIndicatorToSelectedPosition() {
            View childAt = getChildAt(this.selectedPosition);
            TabIndicatorInterpolator access$1300 = TabLayout.this.tabIndicatorInterpolator;
            TabLayout tabLayout = TabLayout.this;
            access$1300.setIndicatorBoundsForTab(tabLayout, childAt, tabLayout.tabSelectedIndicator);
        }

        /* access modifiers changed from: private */
        public void tweenIndicatorPosition(View startTitle, View endTitle, float fraction) {
            if (startTitle != null && startTitle.getWidth() > 0) {
                TabIndicatorInterpolator access$1300 = TabLayout.this.tabIndicatorInterpolator;
                TabLayout tabLayout = TabLayout.this;
                access$1300.updateIndicatorForOffset(tabLayout, startTitle, endTitle, fraction, tabLayout.tabSelectedIndicator);
            } else {
                TabLayout.this.tabSelectedIndicator.setBounds(-1, TabLayout.this.tabSelectedIndicator.getBounds().top, -1, TabLayout.this.tabSelectedIndicator.getBounds().bottom);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }

        private void updateOrRecreateIndicatorAnimation(boolean recreateAnimation, final int position, int duration) {
            final View childAt = getChildAt(this.selectedPosition);
            final View childAt2 = getChildAt(position);
            if (childAt2 == null) {
                jumpIndicatorToSelectedPosition();
                return;
            }
            AnonymousClass1 r2 = new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    SlidingTabIndicator.this.tweenIndicatorPosition(childAt, childAt2, valueAnimator.getAnimatedFraction());
                }
            };
            if (recreateAnimation) {
                ValueAnimator valueAnimator = new ValueAnimator();
                this.indicatorAnimator = valueAnimator;
                valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                valueAnimator.setDuration((long) duration);
                valueAnimator.setFloatValues(new float[]{0.0f, 1.0f});
                valueAnimator.addUpdateListener(r2);
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        SlidingTabIndicator.this.selectedPosition = position;
                    }

                    public void onAnimationStart(Animator animator) {
                        SlidingTabIndicator.this.selectedPosition = position;
                    }
                });
                valueAnimator.start();
                return;
            }
            this.indicatorAnimator.removeAllUpdateListeners();
            this.indicatorAnimator.addUpdateListener(r2);
        }

        /* access modifiers changed from: package-private */
        public void animateIndicatorToPosition(int position, int duration) {
            ValueAnimator valueAnimator = this.indicatorAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
            }
            updateOrRecreateIndicatorAnimation(true, position, duration);
        }

        /* access modifiers changed from: package-private */
        public boolean childrenNeedLayout() {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (getChildAt(i).getWidth() <= 0) {
                    return true;
                }
            }
            return false;
        }

        public void draw(Canvas canvas) {
            int height = TabLayout.this.tabSelectedIndicator.getBounds().height();
            if (height < 0) {
                height = TabLayout.this.tabSelectedIndicator.getIntrinsicHeight();
            }
            int i = 0;
            int i2 = 0;
            switch (TabLayout.this.tabIndicatorGravity) {
                case 0:
                    i = getHeight() - height;
                    i2 = getHeight();
                    break;
                case 1:
                    i = (getHeight() - height) / 2;
                    i2 = (getHeight() + height) / 2;
                    break;
                case 2:
                    i = 0;
                    i2 = height;
                    break;
                case 3:
                    i = 0;
                    i2 = getHeight();
                    break;
            }
            if (TabLayout.this.tabSelectedIndicator.getBounds().width() > 0) {
                Rect bounds = TabLayout.this.tabSelectedIndicator.getBounds();
                TabLayout.this.tabSelectedIndicator.setBounds(bounds.left, i, bounds.right, i2);
                Drawable drawable = TabLayout.this.tabSelectedIndicator;
                if (TabLayout.this.tabSelectedIndicatorColor != 0) {
                    drawable = DrawableCompat.wrap(drawable);
                    if (Build.VERSION.SDK_INT == 21) {
                        drawable.setColorFilter(TabLayout.this.tabSelectedIndicatorColor, PorterDuff.Mode.SRC_IN);
                    } else {
                        DrawableCompat.setTint(drawable, TabLayout.this.tabSelectedIndicatorColor);
                    }
                } else if (Build.VERSION.SDK_INT == 21) {
                    drawable.setColorFilter((ColorFilter) null);
                } else {
                    DrawableCompat.setTintList(drawable, (ColorStateList) null);
                }
                drawable.draw(canvas);
            }
            super.draw(canvas);
        }

        /* access modifiers changed from: package-private */
        public float getIndicatorPosition() {
            return ((float) this.selectedPosition) + this.selectionOffset;
        }

        /* access modifiers changed from: protected */
        public void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            ValueAnimator valueAnimator = this.indicatorAnimator;
            if (valueAnimator == null || !valueAnimator.isRunning()) {
                jumpIndicatorToSelectedPosition();
            } else {
                updateOrRecreateIndicatorAnimation(false, this.selectedPosition, -1);
            }
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (View.MeasureSpec.getMode(widthMeasureSpec) == 1073741824) {
                if (TabLayout.this.tabGravity == 1 || TabLayout.this.mode == 2) {
                    int childCount = getChildCount();
                    int i = 0;
                    int i2 = childCount;
                    for (int i3 = 0; i3 < i2; i3++) {
                        View childAt = getChildAt(i3);
                        if (childAt.getVisibility() == 0) {
                            i = Math.max(i, childAt.getMeasuredWidth());
                        }
                    }
                    if (i > 0) {
                        boolean z = false;
                        if (i * childCount <= getMeasuredWidth() - (((int) ViewUtils.dpToPx(getContext(), 16)) * 2)) {
                            for (int i4 = 0; i4 < childCount; i4++) {
                                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getChildAt(i4).getLayoutParams();
                                if (layoutParams.width != i || layoutParams.weight != 0.0f) {
                                    layoutParams.width = i;
                                    layoutParams.weight = 0.0f;
                                    z = true;
                                }
                            }
                        } else {
                            TabLayout.this.tabGravity = 0;
                            TabLayout.this.updateTabViews(false);
                            z = true;
                        }
                        if (z) {
                            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                        }
                    }
                }
            }
        }

        public void onRtlPropertiesChanged(int layoutDirection2) {
            super.onRtlPropertiesChanged(layoutDirection2);
            if (Build.VERSION.SDK_INT < 23 && this.layoutDirection != layoutDirection2) {
                requestLayout();
                this.layoutDirection = layoutDirection2;
            }
        }

        /* access modifiers changed from: package-private */
        public void setIndicatorPositionFromTabPosition(int position, float positionOffset) {
            ValueAnimator valueAnimator = this.indicatorAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
            }
            this.selectedPosition = position;
            this.selectionOffset = positionOffset;
            tweenIndicatorPosition(getChildAt(position), getChildAt(this.selectedPosition + 1), this.selectionOffset);
        }

        /* access modifiers changed from: package-private */
        public void setSelectedIndicatorHeight(int height) {
            Rect bounds = TabLayout.this.tabSelectedIndicator.getBounds();
            TabLayout.this.tabSelectedIndicator.setBounds(bounds.left, 0, bounds.right, height);
            requestLayout();
        }
    }

    public static class Tab {
        public static final int INVALID_POSITION = -1;
        /* access modifiers changed from: private */
        public CharSequence contentDesc;
        private View customView;
        private Drawable icon;
        /* access modifiers changed from: private */
        public int id = -1;
        /* access modifiers changed from: private */
        public int labelVisibilityMode = 1;
        public TabLayout parent;
        private int position = -1;
        private Object tag;
        /* access modifiers changed from: private */
        public CharSequence text;
        public TabView view;

        public BadgeDrawable getBadge() {
            return this.view.getBadge();
        }

        public CharSequence getContentDescription() {
            TabView tabView = this.view;
            if (tabView == null) {
                return null;
            }
            return tabView.getContentDescription();
        }

        public View getCustomView() {
            return this.customView;
        }

        public Drawable getIcon() {
            return this.icon;
        }

        public int getId() {
            return this.id;
        }

        public BadgeDrawable getOrCreateBadge() {
            return this.view.getOrCreateBadge();
        }

        public int getPosition() {
            return this.position;
        }

        public int getTabLabelVisibility() {
            return this.labelVisibilityMode;
        }

        public Object getTag() {
            return this.tag;
        }

        public CharSequence getText() {
            return this.text;
        }

        public boolean isSelected() {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                int selectedTabPosition = tabLayout.getSelectedTabPosition();
                return selectedTabPosition != -1 && selectedTabPosition == this.position;
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public void removeBadge() {
            this.view.removeBadge();
        }

        /* access modifiers changed from: package-private */
        public void reset() {
            this.parent = null;
            this.view = null;
            this.tag = null;
            this.icon = null;
            this.id = -1;
            this.text = null;
            this.contentDesc = null;
            this.position = -1;
            this.customView = null;
        }

        public void select() {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                tabLayout.selectTab(this);
                return;
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setContentDescription(int resId) {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                return setContentDescription(tabLayout.getResources().getText(resId));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setContentDescription(CharSequence contentDesc2) {
            this.contentDesc = contentDesc2;
            updateView();
            return this;
        }

        public Tab setCustomView(int resId) {
            return setCustomView(LayoutInflater.from(this.view.getContext()).inflate(resId, this.view, false));
        }

        public Tab setCustomView(View view2) {
            this.customView = view2;
            updateView();
            return this;
        }

        public Tab setIcon(int resId) {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                return setIcon(AppCompatResources.getDrawable(tabLayout.getContext(), resId));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setIcon(Drawable icon2) {
            this.icon = icon2;
            if (this.parent.tabGravity == 1 || this.parent.mode == 2) {
                this.parent.updateTabViews(true);
            }
            updateView();
            if (BadgeUtils.USE_COMPAT_PARENT && this.view.hasBadgeDrawable() && this.view.badgeDrawable.isVisible()) {
                this.view.invalidate();
            }
            return this;
        }

        public Tab setId(int id2) {
            this.id = id2;
            TabView tabView = this.view;
            if (tabView != null) {
                tabView.setId(id2);
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public void setPosition(int position2) {
            this.position = position2;
        }

        public Tab setTabLabelVisibility(int mode) {
            this.labelVisibilityMode = mode;
            if (this.parent.tabGravity == 1 || this.parent.mode == 2) {
                this.parent.updateTabViews(true);
            }
            updateView();
            if (BadgeUtils.USE_COMPAT_PARENT && this.view.hasBadgeDrawable() && this.view.badgeDrawable.isVisible()) {
                this.view.invalidate();
            }
            return this;
        }

        public Tab setTag(Object tag2) {
            this.tag = tag2;
            return this;
        }

        public Tab setText(int resId) {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                return setText(tabLayout.getResources().getText(resId));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setText(CharSequence text2) {
            if (TextUtils.isEmpty(this.contentDesc) && !TextUtils.isEmpty(text2)) {
                this.view.setContentDescription(text2);
            }
            this.text = text2;
            updateView();
            return this;
        }

        /* access modifiers changed from: package-private */
        public void updateView() {
            TabView tabView = this.view;
            if (tabView != null) {
                tabView.update();
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TabGravity {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TabIndicatorAnimationMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TabIndicatorGravity {
    }

    public static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private int previousScrollState;
        private int scrollState;
        private final WeakReference<TabLayout> tabLayoutRef;

        public TabLayoutOnPageChangeListener(TabLayout tabLayout) {
            this.tabLayoutRef = new WeakReference<>(tabLayout);
        }

        public void onPageScrollStateChanged(int state) {
            this.previousScrollState = this.scrollState;
            this.scrollState = state;
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            TabLayout tabLayout = (TabLayout) this.tabLayoutRef.get();
            if (tabLayout != null) {
                int i = this.scrollState;
                boolean z = false;
                boolean z2 = i != 2 || this.previousScrollState == 1;
                if (!(i == 2 && this.previousScrollState == 0)) {
                    z = true;
                }
                tabLayout.setScrollPosition(position, positionOffset, z2, z);
            }
        }

        public void onPageSelected(int position) {
            TabLayout tabLayout = (TabLayout) this.tabLayoutRef.get();
            if (tabLayout != null && tabLayout.getSelectedTabPosition() != position && position < tabLayout.getTabCount()) {
                int i = this.scrollState;
                tabLayout.selectTab(tabLayout.getTabAt(position), i == 0 || (i == 2 && this.previousScrollState == 0));
            }
        }

        /* access modifiers changed from: package-private */
        public void reset() {
            this.scrollState = 0;
            this.previousScrollState = 0;
        }
    }

    public final class TabView extends LinearLayout {
        private View badgeAnchorView;
        /* access modifiers changed from: private */
        public BadgeDrawable badgeDrawable;
        private Drawable baseBackgroundDrawable;
        private ImageView customIconView;
        private TextView customTextView;
        private View customView;
        private int defaultMaxLines = 2;
        private ImageView iconView;
        private Tab tab;
        private TextView textView;

        public TabView(Context context) {
            super(context);
            updateBackgroundDrawable(context);
            ViewCompat.setPaddingRelative(this, TabLayout.this.tabPaddingStart, TabLayout.this.tabPaddingTop, TabLayout.this.tabPaddingEnd, TabLayout.this.tabPaddingBottom);
            setGravity(17);
            setOrientation(TabLayout.this.inlineLabel ^ true ? 1 : 0);
            setClickable(true);
            ViewCompat.setPointerIcon(this, PointerIconCompat.getSystemIcon(getContext(), PointerIconCompat.TYPE_HAND));
        }

        private void addOnLayoutChangeListener(final View view) {
            if (view != null) {
                view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        if (view.getVisibility() == 0) {
                            TabView.this.tryUpdateBadgeDrawableBounds(view);
                        }
                    }
                });
            }
        }

        private float approximateLineWidth(Layout layout, int line, float textSize) {
            return layout.getLineWidth(line) * (textSize / layout.getPaint().getTextSize());
        }

        private void clipViewToPaddingForBadge(boolean flag) {
            setClipChildren(flag);
            setClipToPadding(flag);
            ViewGroup viewGroup = (ViewGroup) getParent();
            if (viewGroup != null) {
                viewGroup.setClipChildren(flag);
                viewGroup.setClipToPadding(flag);
            }
        }

        private FrameLayout createPreApi18BadgeAnchorRoot() {
            FrameLayout frameLayout = new FrameLayout(getContext());
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
            return frameLayout;
        }

        /* access modifiers changed from: private */
        public void drawBackground(Canvas canvas) {
            Drawable drawable = this.baseBackgroundDrawable;
            if (drawable != null) {
                drawable.setBounds(getLeft(), getTop(), getRight(), getBottom());
                this.baseBackgroundDrawable.draw(canvas);
            }
        }

        /* access modifiers changed from: private */
        public BadgeDrawable getBadge() {
            return this.badgeDrawable;
        }

        private FrameLayout getCustomParentForBadge(View anchor) {
            if ((anchor == this.iconView || anchor == this.textView) && BadgeUtils.USE_COMPAT_PARENT) {
                return (FrameLayout) anchor.getParent();
            }
            return null;
        }

        /* access modifiers changed from: private */
        public BadgeDrawable getOrCreateBadge() {
            if (this.badgeDrawable == null) {
                this.badgeDrawable = BadgeDrawable.create(getContext());
            }
            tryUpdateBadgeAnchor();
            BadgeDrawable badgeDrawable2 = this.badgeDrawable;
            if (badgeDrawable2 != null) {
                return badgeDrawable2;
            }
            throw new IllegalStateException("Unable to create badge");
        }

        /* access modifiers changed from: private */
        public boolean hasBadgeDrawable() {
            return this.badgeDrawable != null;
        }

        private void inflateAndAddDefaultIconView() {
            ViewGroup viewGroup = this;
            if (BadgeUtils.USE_COMPAT_PARENT) {
                viewGroup = createPreApi18BadgeAnchorRoot();
                addView(viewGroup, 0);
            }
            ImageView imageView = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.design_layout_tab_icon, viewGroup, false);
            this.iconView = imageView;
            viewGroup.addView(imageView, 0);
        }

        private void inflateAndAddDefaultTextView() {
            ViewGroup viewGroup = this;
            if (BadgeUtils.USE_COMPAT_PARENT) {
                viewGroup = createPreApi18BadgeAnchorRoot();
                addView(viewGroup);
            }
            TextView textView2 = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.design_layout_tab_text, viewGroup, false);
            this.textView = textView2;
            viewGroup.addView(textView2);
        }

        /* access modifiers changed from: private */
        public void removeBadge() {
            if (this.badgeAnchorView != null) {
                tryRemoveBadgeFromAnchor();
            }
            this.badgeDrawable = null;
        }

        private void tryAttachBadgeToAnchor(View anchorView) {
            if (hasBadgeDrawable() && anchorView != null) {
                clipViewToPaddingForBadge(false);
                BadgeUtils.attachBadgeDrawable(this.badgeDrawable, anchorView, getCustomParentForBadge(anchorView));
                this.badgeAnchorView = anchorView;
            }
        }

        private void tryRemoveBadgeFromAnchor() {
            if (hasBadgeDrawable()) {
                clipViewToPaddingForBadge(true);
                View view = this.badgeAnchorView;
                if (view != null) {
                    BadgeUtils.detachBadgeDrawable(this.badgeDrawable, view);
                    this.badgeAnchorView = null;
                }
            }
        }

        private void tryUpdateBadgeAnchor() {
            Tab tab2;
            Tab tab3;
            if (hasBadgeDrawable()) {
                if (this.customView != null) {
                    tryRemoveBadgeFromAnchor();
                } else if (this.iconView != null && (tab3 = this.tab) != null && tab3.getIcon() != null) {
                    View view = this.badgeAnchorView;
                    ImageView imageView = this.iconView;
                    if (view != imageView) {
                        tryRemoveBadgeFromAnchor();
                        tryAttachBadgeToAnchor(this.iconView);
                        return;
                    }
                    tryUpdateBadgeDrawableBounds(imageView);
                } else if (this.textView == null || (tab2 = this.tab) == null || tab2.getTabLabelVisibility() != 1) {
                    tryRemoveBadgeFromAnchor();
                } else {
                    View view2 = this.badgeAnchorView;
                    TextView textView2 = this.textView;
                    if (view2 != textView2) {
                        tryRemoveBadgeFromAnchor();
                        tryAttachBadgeToAnchor(this.textView);
                        return;
                    }
                    tryUpdateBadgeDrawableBounds(textView2);
                }
            }
        }

        /* access modifiers changed from: private */
        public void tryUpdateBadgeDrawableBounds(View anchor) {
            if (hasBadgeDrawable() && anchor == this.badgeAnchorView) {
                BadgeUtils.setBadgeDrawableBounds(this.badgeDrawable, anchor, getCustomParentForBadge(anchor));
            }
        }

        /* access modifiers changed from: private */
        public void updateBackgroundDrawable(Context context) {
            Drawable drawable;
            GradientDrawable gradientDrawable = null;
            if (TabLayout.this.tabBackgroundResId != 0) {
                Drawable drawable2 = AppCompatResources.getDrawable(context, TabLayout.this.tabBackgroundResId);
                this.baseBackgroundDrawable = drawable2;
                if (drawable2 != null && drawable2.isStateful()) {
                    this.baseBackgroundDrawable.setState(getDrawableState());
                }
            } else {
                this.baseBackgroundDrawable = null;
            }
            Drawable gradientDrawable2 = new GradientDrawable();
            ((GradientDrawable) gradientDrawable2).setColor(0);
            if (TabLayout.this.tabRippleColorStateList != null) {
                GradientDrawable gradientDrawable3 = new GradientDrawable();
                gradientDrawable3.setCornerRadius(1.0E-5f);
                gradientDrawable3.setColor(-1);
                ColorStateList convertToRippleDrawableColor = RippleUtils.convertToRippleDrawableColor(TabLayout.this.tabRippleColorStateList);
                if (Build.VERSION.SDK_INT >= 21) {
                    GradientDrawable gradientDrawable4 = TabLayout.this.unboundedRipple ? null : gradientDrawable2;
                    if (!TabLayout.this.unboundedRipple) {
                        gradientDrawable = gradientDrawable3;
                    }
                    drawable = new RippleDrawable(convertToRippleDrawableColor, gradientDrawable4, gradientDrawable);
                } else {
                    Drawable wrap = DrawableCompat.wrap(gradientDrawable3);
                    DrawableCompat.setTintList(wrap, convertToRippleDrawableColor);
                    drawable = new LayerDrawable(new Drawable[]{gradientDrawable2, wrap});
                }
            } else {
                drawable = gradientDrawable2;
            }
            ViewCompat.setBackground(this, drawable);
            TabLayout.this.invalidate();
        }

        private void updateTextAndIcon(TextView textView2, ImageView iconView2) {
            Tab tab2 = this.tab;
            CharSequence charSequence = null;
            Drawable mutate = (tab2 == null || tab2.getIcon() == null) ? null : DrawableCompat.wrap(this.tab.getIcon()).mutate();
            if (mutate != null) {
                DrawableCompat.setTintList(mutate, TabLayout.this.tabIconTint);
                if (TabLayout.this.tabIconTintMode != null) {
                    DrawableCompat.setTintMode(mutate, TabLayout.this.tabIconTintMode);
                }
            }
            Tab tab3 = this.tab;
            CharSequence text = tab3 != null ? tab3.getText() : null;
            if (iconView2 != null) {
                if (mutate != null) {
                    iconView2.setImageDrawable(mutate);
                    iconView2.setVisibility(0);
                    setVisibility(0);
                } else {
                    iconView2.setVisibility(8);
                    iconView2.setImageDrawable((Drawable) null);
                }
            }
            boolean z = !TextUtils.isEmpty(text);
            if (textView2 != null) {
                if (z) {
                    textView2.setText(text);
                    if (this.tab.labelVisibilityMode == 1) {
                        textView2.setVisibility(0);
                    } else {
                        textView2.setVisibility(8);
                    }
                    setVisibility(0);
                } else {
                    textView2.setVisibility(8);
                    textView2.setText((CharSequence) null);
                }
            }
            if (iconView2 != null) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) iconView2.getLayoutParams();
                int i = 0;
                if (z && iconView2.getVisibility() == 0) {
                    i = (int) ViewUtils.dpToPx(getContext(), 8);
                }
                if (TabLayout.this.inlineLabel) {
                    if (i != MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams)) {
                        MarginLayoutParamsCompat.setMarginEnd(marginLayoutParams, i);
                        marginLayoutParams.bottomMargin = 0;
                        iconView2.setLayoutParams(marginLayoutParams);
                        iconView2.requestLayout();
                    }
                } else if (i != marginLayoutParams.bottomMargin) {
                    marginLayoutParams.bottomMargin = i;
                    MarginLayoutParamsCompat.setMarginEnd(marginLayoutParams, 0);
                    iconView2.setLayoutParams(marginLayoutParams);
                    iconView2.requestLayout();
                }
            }
            Tab tab4 = this.tab;
            if (tab4 != null) {
                charSequence = tab4.contentDesc;
            }
            if (Build.VERSION.SDK_INT < 21 || Build.VERSION.SDK_INT > 23) {
                TooltipCompat.setTooltipText(this, z ? text : charSequence);
            }
        }

        /* access modifiers changed from: protected */
        public void drawableStateChanged() {
            super.drawableStateChanged();
            boolean z = false;
            int[] drawableState = getDrawableState();
            Drawable drawable = this.baseBackgroundDrawable;
            if (drawable != null && drawable.isStateful()) {
                z = false | this.baseBackgroundDrawable.setState(drawableState);
            }
            if (z) {
                invalidate();
                TabLayout.this.invalidate();
            }
        }

        /* access modifiers changed from: package-private */
        public int getContentHeight() {
            boolean z = false;
            int i = 0;
            int i2 = 0;
            View[] viewArr = {this.textView, this.iconView, this.customView};
            for (int i3 = 0; i3 < 3; i3++) {
                View view = viewArr[i3];
                if (view != null && view.getVisibility() == 0) {
                    int top = view.getTop();
                    if (z) {
                        top = Math.min(i, top);
                    }
                    i = top;
                    int bottom = view.getBottom();
                    if (z) {
                        bottom = Math.max(i2, bottom);
                    }
                    i2 = bottom;
                    z = true;
                }
            }
            return i2 - i;
        }

        /* access modifiers changed from: package-private */
        public int getContentWidth() {
            boolean z = false;
            int i = 0;
            int i2 = 0;
            View[] viewArr = {this.textView, this.iconView, this.customView};
            for (int i3 = 0; i3 < 3; i3++) {
                View view = viewArr[i3];
                if (view != null && view.getVisibility() == 0) {
                    int left = view.getLeft();
                    if (z) {
                        left = Math.min(i, left);
                    }
                    i = left;
                    int right = view.getRight();
                    if (z) {
                        right = Math.max(i2, right);
                    }
                    i2 = right;
                    z = true;
                }
            }
            return i2 - i;
        }

        public Tab getTab() {
            return this.tab;
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            BadgeDrawable badgeDrawable2 = this.badgeDrawable;
            if (badgeDrawable2 != null && badgeDrawable2.isVisible()) {
                info.setContentDescription(getContentDescription() + ", " + this.badgeDrawable.getContentDescription());
            }
            AccessibilityNodeInfoCompat wrap = AccessibilityNodeInfoCompat.wrap(info);
            wrap.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(0, 1, this.tab.getPosition(), 1, false, isSelected()));
            if (isSelected()) {
                wrap.setClickable(false);
                wrap.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
            }
            wrap.setRoleDescription(getResources().getString(R.string.item_view_role_description));
        }

        public void onMeasure(int origWidthMeasureSpec, int origHeightMeasureSpec) {
            Layout layout;
            int size = View.MeasureSpec.getSize(origWidthMeasureSpec);
            int mode = View.MeasureSpec.getMode(origWidthMeasureSpec);
            int tabMaxWidth = TabLayout.this.getTabMaxWidth();
            int i = origHeightMeasureSpec;
            int makeMeasureSpec = (tabMaxWidth <= 0 || (mode != 0 && size <= tabMaxWidth)) ? origWidthMeasureSpec : View.MeasureSpec.makeMeasureSpec(TabLayout.this.tabMaxWidth, Integer.MIN_VALUE);
            super.onMeasure(makeMeasureSpec, i);
            if (this.textView != null) {
                float f = TabLayout.this.tabTextSize;
                int i2 = this.defaultMaxLines;
                ImageView imageView = this.iconView;
                if (imageView == null || imageView.getVisibility() != 0) {
                    TextView textView2 = this.textView;
                    if (textView2 != null && textView2.getLineCount() > 1) {
                        f = TabLayout.this.tabTextMultiLineSize;
                    }
                } else {
                    i2 = 1;
                }
                float textSize = this.textView.getTextSize();
                int lineCount = this.textView.getLineCount();
                int maxLines = TextViewCompat.getMaxLines(this.textView);
                if (f != textSize || (maxLines >= 0 && i2 != maxLines)) {
                    boolean z = true;
                    if (TabLayout.this.mode == 1 && f > textSize && lineCount == 1 && ((layout = this.textView.getLayout()) == null || approximateLineWidth(layout, 0, f) > ((float) ((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight())))) {
                        z = false;
                    }
                    if (z) {
                        this.textView.setTextSize(0, f);
                        this.textView.setMaxLines(i2);
                        super.onMeasure(makeMeasureSpec, i);
                    }
                }
            }
        }

        public boolean performClick() {
            boolean performClick = super.performClick();
            if (this.tab == null) {
                return performClick;
            }
            if (!performClick) {
                playSoundEffect(0);
            }
            this.tab.select();
            return true;
        }

        /* access modifiers changed from: package-private */
        public void reset() {
            setTab((Tab) null);
            setSelected(false);
        }

        public void setSelected(boolean selected) {
            boolean z = isSelected() != selected;
            super.setSelected(selected);
            if (z && selected && Build.VERSION.SDK_INT < 16) {
                sendAccessibilityEvent(4);
            }
            TextView textView2 = this.textView;
            if (textView2 != null) {
                textView2.setSelected(selected);
            }
            ImageView imageView = this.iconView;
            if (imageView != null) {
                imageView.setSelected(selected);
            }
            View view = this.customView;
            if (view != null) {
                view.setSelected(selected);
            }
        }

        /* access modifiers changed from: package-private */
        public void setTab(Tab tab2) {
            if (tab2 != this.tab) {
                this.tab = tab2;
                update();
            }
        }

        /* access modifiers changed from: package-private */
        public final void update() {
            Tab tab2 = this.tab;
            View customView2 = tab2 != null ? tab2.getCustomView() : null;
            if (customView2 != null) {
                ViewParent parent = customView2.getParent();
                if (parent != this) {
                    if (parent != null) {
                        ((ViewGroup) parent).removeView(customView2);
                    }
                    addView(customView2);
                }
                this.customView = customView2;
                TextView textView2 = this.textView;
                if (textView2 != null) {
                    textView2.setVisibility(8);
                }
                ImageView imageView = this.iconView;
                if (imageView != null) {
                    imageView.setVisibility(8);
                    this.iconView.setImageDrawable((Drawable) null);
                }
                TextView textView3 = (TextView) customView2.findViewById(16908308);
                this.customTextView = textView3;
                if (textView3 != null) {
                    this.defaultMaxLines = TextViewCompat.getMaxLines(textView3);
                }
                this.customIconView = (ImageView) customView2.findViewById(16908294);
            } else {
                View view = this.customView;
                if (view != null) {
                    removeView(view);
                    this.customView = null;
                }
                this.customTextView = null;
                this.customIconView = null;
            }
            if (this.customView == null) {
                if (this.iconView == null) {
                    inflateAndAddDefaultIconView();
                }
                if (this.textView == null) {
                    inflateAndAddDefaultTextView();
                    this.defaultMaxLines = TextViewCompat.getMaxLines(this.textView);
                }
                TextViewCompat.setTextAppearance(this.textView, TabLayout.this.tabTextAppearance);
                if (TabLayout.this.tabTextColors != null) {
                    this.textView.setTextColor(TabLayout.this.tabTextColors);
                }
                updateTextAndIcon(this.textView, this.iconView);
                tryUpdateBadgeAnchor();
                addOnLayoutChangeListener(this.iconView);
                addOnLayoutChangeListener(this.textView);
            } else {
                TextView textView4 = this.customTextView;
                if (!(textView4 == null && this.customIconView == null)) {
                    updateTextAndIcon(textView4, this.customIconView);
                }
            }
            if (tab2 != null && !TextUtils.isEmpty(tab2.contentDesc)) {
                setContentDescription(tab2.contentDesc);
            }
            setSelected(tab2 != null && tab2.isSelected());
        }

        /* access modifiers changed from: package-private */
        public final void updateOrientation() {
            setOrientation(TabLayout.this.inlineLabel ^ true ? 1 : 0);
            TextView textView2 = this.customTextView;
            if (textView2 == null && this.customIconView == null) {
                updateTextAndIcon(this.textView, this.iconView);
            } else {
                updateTextAndIcon(textView2, this.customIconView);
            }
        }
    }

    public static class ViewPagerOnTabSelectedListener implements OnTabSelectedListener {
        private final ViewPager viewPager;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager2) {
            this.viewPager = viewPager2;
        }

        public void onTabReselected(Tab tab) {
        }

        public void onTabSelected(Tab tab) {
            this.viewPager.setCurrentItem(tab.getPosition());
        }

        public void onTabUnselected(Tab tab) {
        }
    }

    public TabLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.tabStyle);
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public TabLayout(android.content.Context r11, android.util.AttributeSet r12, int r13) {
        /*
            r10 = this;
            int r4 = DEF_STYLE_RES
            android.content.Context r0 = com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap(r11, r12, r13, r4)
            r10.<init>(r0, r12, r13)
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r10.tabs = r0
            android.graphics.drawable.GradientDrawable r0 = new android.graphics.drawable.GradientDrawable
            r0.<init>()
            r10.tabSelectedIndicator = r0
            r6 = 0
            r10.tabSelectedIndicatorColor = r6
            r0 = 2147483647(0x7fffffff, float:NaN)
            r10.tabMaxWidth = r0
            r7 = -1
            r10.tabIndicatorHeight = r7
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r10.selectedListeners = r0
            androidx.core.util.Pools$SimplePool r0 = new androidx.core.util.Pools$SimplePool
            r1 = 12
            r0.<init>(r1)
            r10.tabViewPool = r0
            android.content.Context r11 = r10.getContext()
            r10.setHorizontalScrollBarEnabled(r6)
            com.google.android.material.tabs.TabLayout$SlidingTabIndicator r8 = new com.google.android.material.tabs.TabLayout$SlidingTabIndicator
            r8.<init>(r11)
            r10.slidingTabIndicator = r8
            android.widget.FrameLayout$LayoutParams r0 = new android.widget.FrameLayout$LayoutParams
            r1 = -2
            r0.<init>(r1, r7)
            super.addView(r8, r6, r0)
            int[] r2 = com.google.android.material.R.styleable.TabLayout
            r9 = 1
            int[] r5 = new int[r9]
            int r0 = com.google.android.material.R.styleable.TabLayout_tabTextAppearance
            r5[r6] = r0
            r0 = r11
            r1 = r12
            r3 = r13
            android.content.res.TypedArray r0 = com.google.android.material.internal.ThemeEnforcement.obtainStyledAttributes(r0, r1, r2, r3, r4, r5)
            android.graphics.drawable.Drawable r1 = r10.getBackground()
            boolean r1 = r1 instanceof android.graphics.drawable.ColorDrawable
            if (r1 == 0) goto L_0x0084
            android.graphics.drawable.Drawable r1 = r10.getBackground()
            android.graphics.drawable.ColorDrawable r1 = (android.graphics.drawable.ColorDrawable) r1
            com.google.android.material.shape.MaterialShapeDrawable r2 = new com.google.android.material.shape.MaterialShapeDrawable
            r2.<init>()
            int r3 = r1.getColor()
            android.content.res.ColorStateList r3 = android.content.res.ColorStateList.valueOf(r3)
            r2.setFillColor(r3)
            r2.initializeElevationOverlay(r11)
            float r3 = androidx.core.view.ViewCompat.getElevation(r10)
            r2.setElevation(r3)
            androidx.core.view.ViewCompat.setBackground(r10, r2)
        L_0x0084:
            int r1 = com.google.android.material.R.styleable.TabLayout_tabIndicator
            android.graphics.drawable.Drawable r1 = com.google.android.material.resources.MaterialResources.getDrawable(r11, r0, r1)
            r10.setSelectedTabIndicator((android.graphics.drawable.Drawable) r1)
            int r1 = com.google.android.material.R.styleable.TabLayout_tabIndicatorColor
            int r1 = r0.getColor(r1, r6)
            r10.setSelectedTabIndicatorColor(r1)
            int r1 = com.google.android.material.R.styleable.TabLayout_tabIndicatorHeight
            int r1 = r0.getDimensionPixelSize(r1, r7)
            r8.setSelectedIndicatorHeight(r1)
            int r1 = com.google.android.material.R.styleable.TabLayout_tabIndicatorGravity
            int r1 = r0.getInt(r1, r6)
            r10.setSelectedTabIndicatorGravity(r1)
            int r1 = com.google.android.material.R.styleable.TabLayout_tabIndicatorAnimationMode
            int r1 = r0.getInt(r1, r6)
            r10.setTabIndicatorAnimationMode(r1)
            int r1 = com.google.android.material.R.styleable.TabLayout_tabIndicatorFullWidth
            boolean r1 = r0.getBoolean(r1, r9)
            r10.setTabIndicatorFullWidth(r1)
            int r1 = com.google.android.material.R.styleable.TabLayout_tabPadding
            int r1 = r0.getDimensionPixelSize(r1, r6)
            r10.tabPaddingBottom = r1
            r10.tabPaddingEnd = r1
            r10.tabPaddingTop = r1
            r10.tabPaddingStart = r1
            int r1 = com.google.android.material.R.styleable.TabLayout_tabPaddingStart
            int r2 = r10.tabPaddingStart
            int r1 = r0.getDimensionPixelSize(r1, r2)
            r10.tabPaddingStart = r1
            int r1 = com.google.android.material.R.styleable.TabLayout_tabPaddingTop
            int r2 = r10.tabPaddingTop
            int r1 = r0.getDimensionPixelSize(r1, r2)
            r10.tabPaddingTop = r1
            int r1 = com.google.android.material.R.styleable.TabLayout_tabPaddingEnd
            int r2 = r10.tabPaddingEnd
            int r1 = r0.getDimensionPixelSize(r1, r2)
            r10.tabPaddingEnd = r1
            int r1 = com.google.android.material.R.styleable.TabLayout_tabPaddingBottom
            int r2 = r10.tabPaddingBottom
            int r1 = r0.getDimensionPixelSize(r1, r2)
            r10.tabPaddingBottom = r1
            int r1 = com.google.android.material.R.styleable.TabLayout_tabTextAppearance
            int r2 = com.google.android.material.R.style.TextAppearance_Design_Tab
            int r1 = r0.getResourceId(r1, r2)
            r10.tabTextAppearance = r1
            int[] r2 = androidx.appcompat.R.styleable.TextAppearance
            android.content.res.TypedArray r1 = r11.obtainStyledAttributes(r1, r2)
            int r2 = androidx.appcompat.R.styleable.TextAppearance_android_textSize     // Catch:{ all -> 0x01c2 }
            int r2 = r1.getDimensionPixelSize(r2, r6)     // Catch:{ all -> 0x01c2 }
            float r2 = (float) r2     // Catch:{ all -> 0x01c2 }
            r10.tabTextSize = r2     // Catch:{ all -> 0x01c2 }
            int r2 = androidx.appcompat.R.styleable.TextAppearance_android_textColor     // Catch:{ all -> 0x01c2 }
            android.content.res.ColorStateList r2 = com.google.android.material.resources.MaterialResources.getColorStateList((android.content.Context) r11, (android.content.res.TypedArray) r1, (int) r2)     // Catch:{ all -> 0x01c2 }
            r10.tabTextColors = r2     // Catch:{ all -> 0x01c2 }
            r1.recycle()
            int r2 = com.google.android.material.R.styleable.TabLayout_tabTextColor
            boolean r2 = r0.hasValue(r2)
            if (r2 == 0) goto L_0x0125
            int r2 = com.google.android.material.R.styleable.TabLayout_tabTextColor
            android.content.res.ColorStateList r2 = com.google.android.material.resources.MaterialResources.getColorStateList((android.content.Context) r11, (android.content.res.TypedArray) r0, (int) r2)
            r10.tabTextColors = r2
        L_0x0125:
            int r2 = com.google.android.material.R.styleable.TabLayout_tabSelectedTextColor
            boolean r2 = r0.hasValue(r2)
            if (r2 == 0) goto L_0x013f
            int r2 = com.google.android.material.R.styleable.TabLayout_tabSelectedTextColor
            int r2 = r0.getColor(r2, r6)
            android.content.res.ColorStateList r3 = r10.tabTextColors
            int r3 = r3.getDefaultColor()
            android.content.res.ColorStateList r3 = createColorStateList(r3, r2)
            r10.tabTextColors = r3
        L_0x013f:
            int r2 = com.google.android.material.R.styleable.TabLayout_tabIconTint
            android.content.res.ColorStateList r2 = com.google.android.material.resources.MaterialResources.getColorStateList((android.content.Context) r11, (android.content.res.TypedArray) r0, (int) r2)
            r10.tabIconTint = r2
            int r2 = com.google.android.material.R.styleable.TabLayout_tabIconTintMode
            int r2 = r0.getInt(r2, r7)
            r3 = 0
            android.graphics.PorterDuff$Mode r2 = com.google.android.material.internal.ViewUtils.parseTintMode(r2, r3)
            r10.tabIconTintMode = r2
            int r2 = com.google.android.material.R.styleable.TabLayout_tabRippleColor
            android.content.res.ColorStateList r2 = com.google.android.material.resources.MaterialResources.getColorStateList((android.content.Context) r11, (android.content.res.TypedArray) r0, (int) r2)
            r10.tabRippleColorStateList = r2
            int r2 = com.google.android.material.R.styleable.TabLayout_tabIndicatorAnimationDuration
            r3 = 300(0x12c, float:4.2E-43)
            int r2 = r0.getInt(r2, r3)
            r10.tabIndicatorAnimationDuration = r2
            int r2 = com.google.android.material.R.styleable.TabLayout_tabMinWidth
            int r2 = r0.getDimensionPixelSize(r2, r7)
            r10.requestedTabMinWidth = r2
            int r2 = com.google.android.material.R.styleable.TabLayout_tabMaxWidth
            int r2 = r0.getDimensionPixelSize(r2, r7)
            r10.requestedTabMaxWidth = r2
            int r2 = com.google.android.material.R.styleable.TabLayout_tabBackground
            int r2 = r0.getResourceId(r2, r6)
            r10.tabBackgroundResId = r2
            int r2 = com.google.android.material.R.styleable.TabLayout_tabContentStart
            int r2 = r0.getDimensionPixelSize(r2, r6)
            r10.contentInsetStart = r2
            int r2 = com.google.android.material.R.styleable.TabLayout_tabMode
            int r2 = r0.getInt(r2, r9)
            r10.mode = r2
            int r2 = com.google.android.material.R.styleable.TabLayout_tabGravity
            int r2 = r0.getInt(r2, r6)
            r10.tabGravity = r2
            int r2 = com.google.android.material.R.styleable.TabLayout_tabInlineLabel
            boolean r2 = r0.getBoolean(r2, r6)
            r10.inlineLabel = r2
            int r2 = com.google.android.material.R.styleable.TabLayout_tabUnboundedRipple
            boolean r2 = r0.getBoolean(r2, r6)
            r10.unboundedRipple = r2
            r0.recycle()
            android.content.res.Resources r2 = r10.getResources()
            int r3 = com.google.android.material.R.dimen.design_tab_text_size_2line
            int r3 = r2.getDimensionPixelSize(r3)
            float r3 = (float) r3
            r10.tabTextMultiLineSize = r3
            int r3 = com.google.android.material.R.dimen.design_tab_scrollable_min_width
            int r3 = r2.getDimensionPixelSize(r3)
            r10.scrollableTabMinWidth = r3
            r10.applyModeAndGravity()
            return
        L_0x01c2:
            r2 = move-exception
            r1.recycle()
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.tabs.TabLayout.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private void addTabFromItemView(TabItem item) {
        Tab newTab = newTab();
        if (item.text != null) {
            newTab.setText(item.text);
        }
        if (item.icon != null) {
            newTab.setIcon(item.icon);
        }
        if (item.customLayout != 0) {
            newTab.setCustomView(item.customLayout);
        }
        if (!TextUtils.isEmpty(item.getContentDescription())) {
            newTab.setContentDescription(item.getContentDescription());
        }
        addTab(newTab);
    }

    private void addTabView(Tab tab) {
        TabView tabView = tab.view;
        tabView.setSelected(false);
        tabView.setActivated(false);
        this.slidingTabIndicator.addView(tabView, tab.getPosition(), createLayoutParamsForTabs());
    }

    private void addViewInternal(View child) {
        if (child instanceof TabItem) {
            addTabFromItemView((TabItem) child);
            return;
        }
        throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
    }

    private void animateToTab(int newPosition) {
        if (newPosition != -1) {
            if (getWindowToken() == null || !ViewCompat.isLaidOut(this) || this.slidingTabIndicator.childrenNeedLayout()) {
                setScrollPosition(newPosition, 0.0f, true);
                return;
            }
            int scrollX = getScrollX();
            int calculateScrollXForTab = calculateScrollXForTab(newPosition, 0.0f);
            if (scrollX != calculateScrollXForTab) {
                ensureScrollAnimator();
                this.scrollAnimator.setIntValues(new int[]{scrollX, calculateScrollXForTab});
                this.scrollAnimator.start();
            }
            this.slidingTabIndicator.animateIndicatorToPosition(newPosition, this.tabIndicatorAnimationDuration);
        }
    }

    private void applyGravityForModeScrollable(int tabGravity2) {
        switch (tabGravity2) {
            case 0:
                Log.w(LOG_TAG, "MODE_SCROLLABLE + GRAVITY_FILL is not supported, GRAVITY_START will be used instead");
                break;
            case 1:
                this.slidingTabIndicator.setGravity(1);
                return;
            case 2:
                break;
            default:
                return;
        }
        this.slidingTabIndicator.setGravity(GravityCompat.START);
    }

    private void applyModeAndGravity() {
        int i = 0;
        int i2 = this.mode;
        if (i2 == 0 || i2 == 2) {
            i = Math.max(0, this.contentInsetStart - this.tabPaddingStart);
        }
        ViewCompat.setPaddingRelative(this.slidingTabIndicator, i, 0, 0, 0);
        switch (this.mode) {
            case 0:
                applyGravityForModeScrollable(this.tabGravity);
                break;
            case 1:
            case 2:
                if (this.tabGravity == 2) {
                    Log.w(LOG_TAG, "GRAVITY_START is not supported with the current tab mode, GRAVITY_CENTER will be used instead");
                }
                this.slidingTabIndicator.setGravity(1);
                break;
        }
        updateTabViews(true);
    }

    private int calculateScrollXForTab(int position, float positionOffset) {
        View childAt;
        int i = this.mode;
        int i2 = 0;
        if ((i != 0 && i != 2) || (childAt = this.slidingTabIndicator.getChildAt(position)) == null) {
            return 0;
        }
        View childAt2 = position + 1 < this.slidingTabIndicator.getChildCount() ? this.slidingTabIndicator.getChildAt(position + 1) : null;
        int width = childAt.getWidth();
        if (childAt2 != null) {
            i2 = childAt2.getWidth();
        }
        int left = (childAt.getLeft() + (width / 2)) - (getWidth() / 2);
        int i3 = (int) (((float) (width + i2)) * 0.5f * positionOffset);
        return ViewCompat.getLayoutDirection(this) == 0 ? left + i3 : left - i3;
    }

    private void configureTab(Tab tab, int position) {
        tab.setPosition(position);
        this.tabs.add(position, tab);
        int size = this.tabs.size();
        for (int i = position + 1; i < size; i++) {
            this.tabs.get(i).setPosition(i);
        }
    }

    private static ColorStateList createColorStateList(int defaultColor, int selectedColor) {
        int[][] iArr = new int[2][];
        int[] iArr2 = new int[2];
        iArr[0] = SELECTED_STATE_SET;
        iArr2[0] = selectedColor;
        int i = 0 + 1;
        iArr[i] = EMPTY_STATE_SET;
        iArr2[i] = defaultColor;
        int i2 = i + 1;
        return new ColorStateList(iArr, iArr2);
    }

    private LinearLayout.LayoutParams createLayoutParamsForTabs() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -1);
        updateTabViewLayoutParams(layoutParams);
        return layoutParams;
    }

    private TabView createTabView(Tab tab) {
        Pools.Pool<TabView> pool = this.tabViewPool;
        TabView acquire = pool != null ? pool.acquire() : null;
        if (acquire == null) {
            acquire = new TabView(getContext());
        }
        acquire.setTab(tab);
        acquire.setFocusable(true);
        acquire.setMinimumWidth(getTabMinWidth());
        if (TextUtils.isEmpty(tab.contentDesc)) {
            acquire.setContentDescription(tab.text);
        } else {
            acquire.setContentDescription(tab.contentDesc);
        }
        return acquire;
    }

    private void dispatchTabReselected(Tab tab) {
        for (int size = this.selectedListeners.size() - 1; size >= 0; size--) {
            this.selectedListeners.get(size).onTabReselected(tab);
        }
    }

    private void dispatchTabSelected(Tab tab) {
        for (int size = this.selectedListeners.size() - 1; size >= 0; size--) {
            this.selectedListeners.get(size).onTabSelected(tab);
        }
    }

    private void dispatchTabUnselected(Tab tab) {
        for (int size = this.selectedListeners.size() - 1; size >= 0; size--) {
            this.selectedListeners.get(size).onTabUnselected(tab);
        }
    }

    private void ensureScrollAnimator() {
        if (this.scrollAnimator == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.scrollAnimator = valueAnimator;
            valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            this.scrollAnimator.setDuration((long) this.tabIndicatorAnimationDuration);
            this.scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animator) {
                    TabLayout.this.scrollTo(((Integer) animator.getAnimatedValue()).intValue(), 0);
                }
            });
        }
    }

    private int getDefaultHeight() {
        boolean z = false;
        int i = 0;
        int size = this.tabs.size();
        while (true) {
            if (i < size) {
                Tab tab = this.tabs.get(i);
                if (tab != null && tab.getIcon() != null && !TextUtils.isEmpty(tab.getText())) {
                    z = true;
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        if (!z || this.inlineLabel) {
            return 48;
        }
        return DEFAULT_HEIGHT_WITH_TEXT_ICON;
    }

    private int getTabMinWidth() {
        int i = this.requestedTabMinWidth;
        if (i != -1) {
            return i;
        }
        int i2 = this.mode;
        if (i2 == 0 || i2 == 2) {
            return this.scrollableTabMinWidth;
        }
        return 0;
    }

    private int getTabScrollRange() {
        return Math.max(0, ((this.slidingTabIndicator.getWidth() - getWidth()) - getPaddingLeft()) - getPaddingRight());
    }

    private boolean isScrollingEnabled() {
        return getTabMode() == 0 || getTabMode() == 2;
    }

    private void removeTabViewAt(int position) {
        TabView tabView = (TabView) this.slidingTabIndicator.getChildAt(position);
        this.slidingTabIndicator.removeViewAt(position);
        if (tabView != null) {
            tabView.reset();
            this.tabViewPool.release(tabView);
        }
        requestLayout();
    }

    private void setSelectedTabView(int position) {
        int childCount = this.slidingTabIndicator.getChildCount();
        if (position < childCount) {
            int i = 0;
            while (i < childCount) {
                View childAt = this.slidingTabIndicator.getChildAt(i);
                boolean z = false;
                childAt.setSelected(i == position);
                if (i == position) {
                    z = true;
                }
                childAt.setActivated(z);
                i++;
            }
        }
    }

    private void setupWithViewPager(ViewPager viewPager2, boolean autoRefresh, boolean implicitSetup) {
        ViewPager viewPager3 = this.viewPager;
        if (viewPager3 != null) {
            TabLayoutOnPageChangeListener tabLayoutOnPageChangeListener = this.pageChangeListener;
            if (tabLayoutOnPageChangeListener != null) {
                viewPager3.removeOnPageChangeListener(tabLayoutOnPageChangeListener);
            }
            AdapterChangeListener adapterChangeListener2 = this.adapterChangeListener;
            if (adapterChangeListener2 != null) {
                this.viewPager.removeOnAdapterChangeListener(adapterChangeListener2);
            }
        }
        BaseOnTabSelectedListener baseOnTabSelectedListener = this.currentVpSelectedListener;
        if (baseOnTabSelectedListener != null) {
            removeOnTabSelectedListener(baseOnTabSelectedListener);
            this.currentVpSelectedListener = null;
        }
        if (viewPager2 != null) {
            this.viewPager = viewPager2;
            if (this.pageChangeListener == null) {
                this.pageChangeListener = new TabLayoutOnPageChangeListener(this);
            }
            this.pageChangeListener.reset();
            viewPager2.addOnPageChangeListener(this.pageChangeListener);
            ViewPagerOnTabSelectedListener viewPagerOnTabSelectedListener = new ViewPagerOnTabSelectedListener(viewPager2);
            this.currentVpSelectedListener = viewPagerOnTabSelectedListener;
            addOnTabSelectedListener((BaseOnTabSelectedListener) viewPagerOnTabSelectedListener);
            PagerAdapter adapter = viewPager2.getAdapter();
            if (adapter != null) {
                setPagerAdapter(adapter, autoRefresh);
            }
            if (this.adapterChangeListener == null) {
                this.adapterChangeListener = new AdapterChangeListener();
            }
            this.adapterChangeListener.setAutoRefresh(autoRefresh);
            viewPager2.addOnAdapterChangeListener(this.adapterChangeListener);
            setScrollPosition(viewPager2.getCurrentItem(), 0.0f, true);
        } else {
            this.viewPager = null;
            setPagerAdapter((PagerAdapter) null, false);
        }
        this.setupViewPagerImplicitly = implicitSetup;
    }

    private void updateAllTabs() {
        int size = this.tabs.size();
        for (int i = 0; i < size; i++) {
            this.tabs.get(i).updateView();
        }
    }

    private void updateTabViewLayoutParams(LinearLayout.LayoutParams lp) {
        if (this.mode == 1 && this.tabGravity == 0) {
            lp.width = 0;
            lp.weight = 1.0f;
            return;
        }
        lp.width = -2;
        lp.weight = 0.0f;
    }

    @Deprecated
    public void addOnTabSelectedListener(BaseOnTabSelectedListener listener) {
        if (!this.selectedListeners.contains(listener)) {
            this.selectedListeners.add(listener);
        }
    }

    public void addOnTabSelectedListener(OnTabSelectedListener listener) {
        addOnTabSelectedListener((BaseOnTabSelectedListener) listener);
    }

    public void addTab(Tab tab) {
        addTab(tab, this.tabs.isEmpty());
    }

    public void addTab(Tab tab, int position) {
        addTab(tab, position, this.tabs.isEmpty());
    }

    public void addTab(Tab tab, int position, boolean setSelected) {
        if (tab.parent == this) {
            configureTab(tab, position);
            addTabView(tab);
            if (setSelected) {
                tab.select();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
    }

    public void addTab(Tab tab, boolean setSelected) {
        addTab(tab, this.tabs.size(), setSelected);
    }

    public void addView(View child) {
        addViewInternal(child);
    }

    public void addView(View child, int index) {
        addViewInternal(child);
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        addViewInternal(child);
    }

    public void addView(View child, ViewGroup.LayoutParams params) {
        addViewInternal(child);
    }

    public void clearOnTabSelectedListeners() {
        this.selectedListeners.clear();
    }

    /* access modifiers changed from: protected */
    public Tab createTabFromPool() {
        Tab acquire = tabPool.acquire();
        return acquire == null ? new Tab() : acquire;
    }

    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return generateDefaultLayoutParams();
    }

    public int getSelectedTabPosition() {
        Tab tab = this.selectedTab;
        if (tab != null) {
            return tab.getPosition();
        }
        return -1;
    }

    public Tab getTabAt(int index) {
        if (index < 0 || index >= getTabCount()) {
            return null;
        }
        return this.tabs.get(index);
    }

    public int getTabCount() {
        return this.tabs.size();
    }

    public int getTabGravity() {
        return this.tabGravity;
    }

    public ColorStateList getTabIconTint() {
        return this.tabIconTint;
    }

    public int getTabIndicatorAnimationMode() {
        return this.tabIndicatorAnimationMode;
    }

    public int getTabIndicatorGravity() {
        return this.tabIndicatorGravity;
    }

    /* access modifiers changed from: package-private */
    public int getTabMaxWidth() {
        return this.tabMaxWidth;
    }

    public int getTabMode() {
        return this.mode;
    }

    public ColorStateList getTabRippleColor() {
        return this.tabRippleColorStateList;
    }

    public Drawable getTabSelectedIndicator() {
        return this.tabSelectedIndicator;
    }

    public ColorStateList getTabTextColors() {
        return this.tabTextColors;
    }

    public boolean hasUnboundedRipple() {
        return this.unboundedRipple;
    }

    public boolean isInlineLabel() {
        return this.inlineLabel;
    }

    public boolean isTabIndicatorFullWidth() {
        return this.tabIndicatorFullWidth;
    }

    public Tab newTab() {
        Tab createTabFromPool = createTabFromPool();
        createTabFromPool.parent = this;
        createTabFromPool.view = createTabView(createTabFromPool);
        if (createTabFromPool.id != -1) {
            createTabFromPool.view.setId(createTabFromPool.id);
        }
        return createTabFromPool;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.setParentAbsoluteElevation(this);
        if (this.viewPager == null) {
            ViewParent parent = getParent();
            if (parent instanceof ViewPager) {
                setupWithViewPager((ViewPager) parent, true, true);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.setupViewPagerImplicitly) {
            setupWithViewPager((ViewPager) null);
            this.setupViewPagerImplicitly = false;
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        for (int i = 0; i < this.slidingTabIndicator.getChildCount(); i++) {
            View childAt = this.slidingTabIndicator.getChildAt(i);
            if (childAt instanceof TabView) {
                ((TabView) childAt).drawBackground(canvas);
            }
        }
        super.onDraw(canvas);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        AccessibilityNodeInfoCompat.wrap(info).setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(1, getTabCount(), false, 1));
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        return isScrollingEnabled() && super.onInterceptTouchEvent(event);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int round = Math.round(ViewUtils.dpToPx(getContext(), getDefaultHeight()));
        boolean z = false;
        switch (View.MeasureSpec.getMode(heightMeasureSpec)) {
            case Integer.MIN_VALUE:
                if (getChildCount() == 1 && View.MeasureSpec.getSize(heightMeasureSpec) >= round) {
                    getChildAt(0).setMinimumHeight(round);
                    break;
                }
            case 0:
                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(getPaddingTop() + round + getPaddingBottom(), BasicMeasure.EXACTLY);
                break;
        }
        int size = View.MeasureSpec.getSize(widthMeasureSpec);
        if (View.MeasureSpec.getMode(widthMeasureSpec) != 0) {
            int i = this.requestedTabMaxWidth;
            if (i <= 0) {
                i = (int) (((float) size) - ViewUtils.dpToPx(getContext(), TAB_MIN_WIDTH_MARGIN));
            }
            this.tabMaxWidth = i;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 1) {
            View childAt = getChildAt(0);
            boolean z2 = false;
            switch (this.mode) {
                case 0:
                case 2:
                    if (childAt.getMeasuredWidth() < getMeasuredWidth()) {
                        z = true;
                    }
                    z2 = z;
                    break;
                case 1:
                    if (childAt.getMeasuredWidth() != getMeasuredWidth()) {
                        z = true;
                    }
                    z2 = z;
                    break;
            }
            if (z2) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), BasicMeasure.EXACTLY), getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), childAt.getLayoutParams().height));
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() != 8 || isScrollingEnabled()) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void populateFromPagerAdapter() {
        int currentItem;
        removeAllTabs();
        PagerAdapter pagerAdapter2 = this.pagerAdapter;
        if (pagerAdapter2 != null) {
            int count = pagerAdapter2.getCount();
            for (int i = 0; i < count; i++) {
                addTab(newTab().setText(this.pagerAdapter.getPageTitle(i)), false);
            }
            ViewPager viewPager2 = this.viewPager;
            if (viewPager2 != null && count > 0 && (currentItem = viewPager2.getCurrentItem()) != getSelectedTabPosition() && currentItem < getTabCount()) {
                selectTab(getTabAt(currentItem));
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean releaseFromTabPool(Tab tab) {
        return tabPool.release(tab);
    }

    public void removeAllTabs() {
        for (int childCount = this.slidingTabIndicator.getChildCount() - 1; childCount >= 0; childCount--) {
            removeTabViewAt(childCount);
        }
        Iterator<Tab> it = this.tabs.iterator();
        while (it.hasNext()) {
            Tab next = it.next();
            it.remove();
            next.reset();
            releaseFromTabPool(next);
        }
        this.selectedTab = null;
    }

    @Deprecated
    public void removeOnTabSelectedListener(BaseOnTabSelectedListener listener) {
        this.selectedListeners.remove(listener);
    }

    public void removeOnTabSelectedListener(OnTabSelectedListener listener) {
        removeOnTabSelectedListener((BaseOnTabSelectedListener) listener);
    }

    public void removeTab(Tab tab) {
        if (tab.parent == this) {
            removeTabAt(tab.getPosition());
            return;
        }
        throw new IllegalArgumentException("Tab does not belong to this TabLayout.");
    }

    public void removeTabAt(int position) {
        Tab tab = this.selectedTab;
        int position2 = tab != null ? tab.getPosition() : 0;
        removeTabViewAt(position);
        Tab remove = this.tabs.remove(position);
        if (remove != null) {
            remove.reset();
            releaseFromTabPool(remove);
        }
        int size = this.tabs.size();
        for (int i = position; i < size; i++) {
            this.tabs.get(i).setPosition(i);
        }
        if (position2 == position) {
            selectTab(this.tabs.isEmpty() ? null : this.tabs.get(Math.max(0, position - 1)));
        }
    }

    public void selectTab(Tab tab) {
        selectTab(tab, true);
    }

    public void selectTab(Tab tab, boolean updateIndicator) {
        Tab tab2 = this.selectedTab;
        if (tab2 != tab) {
            int position = tab != null ? tab.getPosition() : -1;
            if (updateIndicator) {
                if ((tab2 == null || tab2.getPosition() == -1) && position != -1) {
                    setScrollPosition(position, 0.0f, true);
                } else {
                    animateToTab(position);
                }
                if (position != -1) {
                    setSelectedTabView(position);
                }
            }
            this.selectedTab = tab;
            if (tab2 != null) {
                dispatchTabUnselected(tab2);
            }
            if (tab != null) {
                dispatchTabSelected(tab);
            }
        } else if (tab2 != null) {
            dispatchTabReselected(tab);
            animateToTab(tab.getPosition());
        }
    }

    public void setElevation(float elevation) {
        super.setElevation(elevation);
        MaterialShapeUtils.setElevation(this, elevation);
    }

    public void setInlineLabel(boolean inline) {
        if (this.inlineLabel != inline) {
            this.inlineLabel = inline;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); i++) {
                View childAt = this.slidingTabIndicator.getChildAt(i);
                if (childAt instanceof TabView) {
                    ((TabView) childAt).updateOrientation();
                }
            }
            applyModeAndGravity();
        }
    }

    public void setInlineLabelResource(int inlineResourceId) {
        setInlineLabel(getResources().getBoolean(inlineResourceId));
    }

    @Deprecated
    public void setOnTabSelectedListener(BaseOnTabSelectedListener listener) {
        BaseOnTabSelectedListener baseOnTabSelectedListener = this.selectedListener;
        if (baseOnTabSelectedListener != null) {
            removeOnTabSelectedListener(baseOnTabSelectedListener);
        }
        this.selectedListener = listener;
        if (listener != null) {
            addOnTabSelectedListener(listener);
        }
    }

    @Deprecated
    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
        setOnTabSelectedListener((BaseOnTabSelectedListener) listener);
    }

    /* access modifiers changed from: package-private */
    public void setPagerAdapter(PagerAdapter adapter, boolean addObserver) {
        DataSetObserver dataSetObserver;
        PagerAdapter pagerAdapter2 = this.pagerAdapter;
        if (!(pagerAdapter2 == null || (dataSetObserver = this.pagerAdapterObserver) == null)) {
            pagerAdapter2.unregisterDataSetObserver(dataSetObserver);
        }
        this.pagerAdapter = adapter;
        if (addObserver && adapter != null) {
            if (this.pagerAdapterObserver == null) {
                this.pagerAdapterObserver = new PagerAdapterObserver();
            }
            adapter.registerDataSetObserver(this.pagerAdapterObserver);
        }
        populateFromPagerAdapter();
    }

    /* access modifiers changed from: package-private */
    public void setScrollAnimatorListener(Animator.AnimatorListener listener) {
        ensureScrollAnimator();
        this.scrollAnimator.addListener(listener);
    }

    public void setScrollPosition(int position, float positionOffset, boolean updateSelectedText) {
        setScrollPosition(position, positionOffset, updateSelectedText, true);
    }

    public void setScrollPosition(int position, float positionOffset, boolean updateSelectedText, boolean updateIndicatorPosition) {
        int round = Math.round(((float) position) + positionOffset);
        if (round >= 0 && round < this.slidingTabIndicator.getChildCount()) {
            if (updateIndicatorPosition) {
                this.slidingTabIndicator.setIndicatorPositionFromTabPosition(position, positionOffset);
            }
            ValueAnimator valueAnimator = this.scrollAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.scrollAnimator.cancel();
            }
            scrollTo(position < 0 ? 0 : calculateScrollXForTab(position, positionOffset), 0);
            if (updateSelectedText) {
                setSelectedTabView(round);
            }
        }
    }

    public void setSelectedTabIndicator(int tabSelectedIndicatorResourceId) {
        if (tabSelectedIndicatorResourceId != 0) {
            setSelectedTabIndicator(AppCompatResources.getDrawable(getContext(), tabSelectedIndicatorResourceId));
        } else {
            setSelectedTabIndicator((Drawable) null);
        }
    }

    public void setSelectedTabIndicator(Drawable tabSelectedIndicator2) {
        if (this.tabSelectedIndicator != tabSelectedIndicator2) {
            Drawable gradientDrawable = tabSelectedIndicator2 != null ? tabSelectedIndicator2 : new GradientDrawable();
            this.tabSelectedIndicator = gradientDrawable;
            int i = this.tabIndicatorHeight;
            if (i == -1) {
                i = gradientDrawable.getIntrinsicHeight();
            }
            this.slidingTabIndicator.setSelectedIndicatorHeight(i);
        }
    }

    public void setSelectedTabIndicatorColor(int color) {
        this.tabSelectedIndicatorColor = color;
        updateTabViews(false);
    }

    public void setSelectedTabIndicatorGravity(int indicatorGravity) {
        if (this.tabIndicatorGravity != indicatorGravity) {
            this.tabIndicatorGravity = indicatorGravity;
            ViewCompat.postInvalidateOnAnimation(this.slidingTabIndicator);
        }
    }

    @Deprecated
    public void setSelectedTabIndicatorHeight(int height) {
        this.tabIndicatorHeight = height;
        this.slidingTabIndicator.setSelectedIndicatorHeight(height);
    }

    public void setTabGravity(int gravity) {
        if (this.tabGravity != gravity) {
            this.tabGravity = gravity;
            applyModeAndGravity();
        }
    }

    public void setTabIconTint(ColorStateList iconTint) {
        if (this.tabIconTint != iconTint) {
            this.tabIconTint = iconTint;
            updateAllTabs();
        }
    }

    public void setTabIconTintResource(int iconTintResourceId) {
        setTabIconTint(AppCompatResources.getColorStateList(getContext(), iconTintResourceId));
    }

    public void setTabIndicatorAnimationMode(int tabIndicatorAnimationMode2) {
        this.tabIndicatorAnimationMode = tabIndicatorAnimationMode2;
        switch (tabIndicatorAnimationMode2) {
            case 0:
                this.tabIndicatorInterpolator = new TabIndicatorInterpolator();
                return;
            case 1:
                this.tabIndicatorInterpolator = new ElasticTabIndicatorInterpolator();
                return;
            case 2:
                this.tabIndicatorInterpolator = new FadeTabIndicatorInterpolator();
                return;
            default:
                throw new IllegalArgumentException(tabIndicatorAnimationMode2 + " is not a valid TabIndicatorAnimationMode");
        }
    }

    public void setTabIndicatorFullWidth(boolean tabIndicatorFullWidth2) {
        this.tabIndicatorFullWidth = tabIndicatorFullWidth2;
        this.slidingTabIndicator.jumpIndicatorToSelectedPosition();
        ViewCompat.postInvalidateOnAnimation(this.slidingTabIndicator);
    }

    public void setTabMode(int mode2) {
        if (mode2 != this.mode) {
            this.mode = mode2;
            applyModeAndGravity();
        }
    }

    public void setTabRippleColor(ColorStateList color) {
        if (this.tabRippleColorStateList != color) {
            this.tabRippleColorStateList = color;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); i++) {
                View childAt = this.slidingTabIndicator.getChildAt(i);
                if (childAt instanceof TabView) {
                    ((TabView) childAt).updateBackgroundDrawable(getContext());
                }
            }
        }
    }

    public void setTabRippleColorResource(int tabRippleColorResourceId) {
        setTabRippleColor(AppCompatResources.getColorStateList(getContext(), tabRippleColorResourceId));
    }

    public void setTabTextColors(int normalColor, int selectedColor) {
        setTabTextColors(createColorStateList(normalColor, selectedColor));
    }

    public void setTabTextColors(ColorStateList textColor) {
        if (this.tabTextColors != textColor) {
            this.tabTextColors = textColor;
            updateAllTabs();
        }
    }

    @Deprecated
    public void setTabsFromPagerAdapter(PagerAdapter adapter) {
        setPagerAdapter(adapter, false);
    }

    public void setUnboundedRipple(boolean unboundedRipple2) {
        if (this.unboundedRipple != unboundedRipple2) {
            this.unboundedRipple = unboundedRipple2;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); i++) {
                View childAt = this.slidingTabIndicator.getChildAt(i);
                if (childAt instanceof TabView) {
                    ((TabView) childAt).updateBackgroundDrawable(getContext());
                }
            }
        }
    }

    public void setUnboundedRippleResource(int unboundedRippleResourceId) {
        setUnboundedRipple(getResources().getBoolean(unboundedRippleResourceId));
    }

    public void setupWithViewPager(ViewPager viewPager2) {
        setupWithViewPager(viewPager2, true);
    }

    public void setupWithViewPager(ViewPager viewPager2, boolean autoRefresh) {
        setupWithViewPager(viewPager2, autoRefresh, false);
    }

    public boolean shouldDelayChildPressedState() {
        return getTabScrollRange() > 0;
    }

    /* access modifiers changed from: package-private */
    public void updateTabViews(boolean requestLayout) {
        for (int i = 0; i < this.slidingTabIndicator.getChildCount(); i++) {
            View childAt = this.slidingTabIndicator.getChildAt(i);
            childAt.setMinimumWidth(getTabMinWidth());
            updateTabViewLayoutParams((LinearLayout.LayoutParams) childAt.getLayoutParams());
            if (requestLayout) {
                childAt.requestLayout();
            }
        }
    }
}

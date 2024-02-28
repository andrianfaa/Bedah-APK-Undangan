package com.google.android.material.appbar;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.math.MathUtils;
import androidx.core.util.ObjectsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.elevation.ElevationOverlayProvider;
import com.google.android.material.internal.CollapsingTextHelper;
import com.google.android.material.internal.DescendantOffsetUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CollapsingToolbarLayout extends FrameLayout {
    private static final int DEFAULT_SCRIM_ANIMATION_DURATION = 600;
    private static final int DEF_STYLE_RES = R.style.Widget_Design_CollapsingToolbar;
    public static final int TITLE_COLLAPSE_MODE_FADE = 1;
    public static final int TITLE_COLLAPSE_MODE_SCALE = 0;
    final CollapsingTextHelper collapsingTextHelper;
    private boolean collapsingTitleEnabled;
    private Drawable contentScrim;
    int currentOffset;
    private boolean drawCollapsingTitle;
    private View dummyView;
    final ElevationOverlayProvider elevationOverlayProvider;
    private int expandedMarginBottom;
    private int expandedMarginEnd;
    private int expandedMarginStart;
    private int expandedMarginTop;
    private int extraMultilineHeight;
    private boolean extraMultilineHeightEnabled;
    private boolean forceApplySystemWindowInsetTop;
    WindowInsetsCompat lastInsets;
    private AppBarLayout.OnOffsetChangedListener onOffsetChangedListener;
    private boolean refreshToolbar;
    private int scrimAlpha;
    private long scrimAnimationDuration;
    private ValueAnimator scrimAnimator;
    private int scrimVisibleHeightTrigger;
    private boolean scrimsAreShown;
    Drawable statusBarScrim;
    private int titleCollapseMode;
    private final Rect tmpRect;
    private ViewGroup toolbar;
    private View toolbarDirectChild;
    private int toolbarId;
    private int topInsetApplied;

    public static class LayoutParams extends FrameLayout.LayoutParams {
        public static final int COLLAPSE_MODE_OFF = 0;
        public static final int COLLAPSE_MODE_PARALLAX = 2;
        public static final int COLLAPSE_MODE_PIN = 1;
        private static final float DEFAULT_PARALLAX_MULTIPLIER = 0.5f;
        int collapseMode = 0;
        float parallaxMult = 0.5f;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height, gravity);
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray obtainStyledAttributes = c.obtainStyledAttributes(attrs, R.styleable.CollapsingToolbarLayout_Layout);
            this.collapseMode = obtainStyledAttributes.getInt(R.styleable.CollapsingToolbarLayout_Layout_layout_collapseMode, 0);
            setParallaxMultiplier(obtainStyledAttributes.getFloat(R.styleable.CollapsingToolbarLayout_Layout_layout_collapseParallaxMultiplier, 0.5f));
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(FrameLayout.LayoutParams source) {
            super(source);
        }

        public int getCollapseMode() {
            return this.collapseMode;
        }

        public float getParallaxMultiplier() {
            return this.parallaxMult;
        }

        public void setCollapseMode(int collapseMode2) {
            this.collapseMode = collapseMode2;
        }

        public void setParallaxMultiplier(float multiplier) {
            this.parallaxMult = multiplier;
        }
    }

    private class OffsetUpdateListener implements AppBarLayout.OnOffsetChangedListener {
        OffsetUpdateListener() {
        }

        public void onOffsetChanged(AppBarLayout layout, int verticalOffset) {
            CollapsingToolbarLayout.this.currentOffset = verticalOffset;
            int systemWindowInsetTop = CollapsingToolbarLayout.this.lastInsets != null ? CollapsingToolbarLayout.this.lastInsets.getSystemWindowInsetTop() : 0;
            int childCount = CollapsingToolbarLayout.this.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = CollapsingToolbarLayout.this.getChildAt(i);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                ViewOffsetHelper viewOffsetHelper = CollapsingToolbarLayout.getViewOffsetHelper(childAt);
                switch (layoutParams.collapseMode) {
                    case 1:
                        viewOffsetHelper.setTopAndBottomOffset(MathUtils.clamp(-verticalOffset, 0, CollapsingToolbarLayout.this.getMaxOffsetForPinChild(childAt)));
                        break;
                    case 2:
                        viewOffsetHelper.setTopAndBottomOffset(Math.round(((float) (-verticalOffset)) * layoutParams.parallaxMult));
                        break;
                }
            }
            CollapsingToolbarLayout.this.updateScrimVisibility();
            if (CollapsingToolbarLayout.this.statusBarScrim != null && systemWindowInsetTop > 0) {
                ViewCompat.postInvalidateOnAnimation(CollapsingToolbarLayout.this);
            }
            int height = CollapsingToolbarLayout.this.getHeight();
            int minimumHeight = (height - ViewCompat.getMinimumHeight(CollapsingToolbarLayout.this)) - systemWindowInsetTop;
            CollapsingToolbarLayout.this.collapsingTextHelper.setFadeModeStartFraction(Math.min(1.0f, ((float) (height - CollapsingToolbarLayout.this.getScrimVisibleHeightTrigger())) / ((float) minimumHeight)));
            CollapsingToolbarLayout.this.collapsingTextHelper.setCurrentOffsetY(CollapsingToolbarLayout.this.currentOffset + minimumHeight);
            CollapsingToolbarLayout.this.collapsingTextHelper.setExpansionFraction(((float) Math.abs(verticalOffset)) / ((float) minimumHeight));
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TitleCollapseMode {
    }

    public CollapsingToolbarLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public CollapsingToolbarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.collapsingToolbarLayoutStyle);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public CollapsingToolbarLayout(android.content.Context r11, android.util.AttributeSet r12, int r13) {
        /*
            r10 = this;
            int r4 = DEF_STYLE_RES
            android.content.Context r0 = com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap(r11, r12, r13, r4)
            r10.<init>(r0, r12, r13)
            r6 = 1
            r10.refreshToolbar = r6
            android.graphics.Rect r0 = new android.graphics.Rect
            r0.<init>()
            r10.tmpRect = r0
            r7 = -1
            r10.scrimVisibleHeightTrigger = r7
            r8 = 0
            r10.topInsetApplied = r8
            r10.extraMultilineHeight = r8
            android.content.Context r11 = r10.getContext()
            com.google.android.material.internal.CollapsingTextHelper r9 = new com.google.android.material.internal.CollapsingTextHelper
            r9.<init>(r10)
            r10.collapsingTextHelper = r9
            android.animation.TimeInterpolator r0 = com.google.android.material.animation.AnimationUtils.DECELERATE_INTERPOLATOR
            r9.setTextSizeInterpolator(r0)
            r9.setRtlTextDirectionHeuristicsEnabled(r8)
            com.google.android.material.elevation.ElevationOverlayProvider r0 = new com.google.android.material.elevation.ElevationOverlayProvider
            r0.<init>(r11)
            r10.elevationOverlayProvider = r0
            int[] r2 = com.google.android.material.R.styleable.CollapsingToolbarLayout
            int[] r5 = new int[r8]
            r0 = r11
            r1 = r12
            r3 = r13
            android.content.res.TypedArray r0 = com.google.android.material.internal.ThemeEnforcement.obtainStyledAttributes(r0, r1, r2, r3, r4, r5)
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_expandedTitleGravity
            r2 = 8388691(0x800053, float:1.175506E-38)
            int r1 = r0.getInt(r1, r2)
            r9.setExpandedTextGravity(r1)
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_collapsedTitleGravity
            r2 = 8388627(0x800013, float:1.175497E-38)
            int r1 = r0.getInt(r1, r2)
            r9.setCollapsedTextGravity(r1)
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_expandedTitleMargin
            int r1 = r0.getDimensionPixelSize(r1, r8)
            r10.expandedMarginBottom = r1
            r10.expandedMarginEnd = r1
            r10.expandedMarginTop = r1
            r10.expandedMarginStart = r1
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x0076
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart
            int r1 = r0.getDimensionPixelSize(r1, r8)
            r10.expandedMarginStart = r1
        L_0x0076:
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x0086
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd
            int r1 = r0.getDimensionPixelSize(r1, r8)
            r10.expandedMarginEnd = r1
        L_0x0086:
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x0096
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop
            int r1 = r0.getDimensionPixelSize(r1, r8)
            r10.expandedMarginTop = r1
        L_0x0096:
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x00a6
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom
            int r1 = r0.getDimensionPixelSize(r1, r8)
            r10.expandedMarginBottom = r1
        L_0x00a6:
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_titleEnabled
            boolean r1 = r0.getBoolean(r1, r6)
            r10.collapsingTitleEnabled = r1
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_title
            java.lang.CharSequence r1 = r0.getText(r1)
            r10.setTitle(r1)
            int r1 = com.google.android.material.R.style.TextAppearance_Design_CollapsingToolbar_Expanded
            r9.setExpandedTextAppearance(r1)
            int r1 = androidx.appcompat.R.style.TextAppearance_AppCompat_Widget_ActionBar_Title
            r9.setCollapsedTextAppearance(r1)
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x00d2
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance
            int r1 = r0.getResourceId(r1, r8)
            r9.setExpandedTextAppearance(r1)
        L_0x00d2:
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x00e3
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance
            int r1 = r0.getResourceId(r1, r8)
            r9.setCollapsedTextAppearance(r1)
        L_0x00e3:
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_expandedTitleTextColor
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x00f4
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_expandedTitleTextColor
            android.content.res.ColorStateList r1 = com.google.android.material.resources.MaterialResources.getColorStateList((android.content.Context) r11, (android.content.res.TypedArray) r0, (int) r1)
            r9.setExpandedTextColor(r1)
        L_0x00f4:
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_collapsedTitleTextColor
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x0105
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_collapsedTitleTextColor
            android.content.res.ColorStateList r1 = com.google.android.material.resources.MaterialResources.getColorStateList((android.content.Context) r11, (android.content.res.TypedArray) r0, (int) r1)
            r9.setCollapsedTextColor(r1)
        L_0x0105:
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_scrimVisibleHeightTrigger
            int r1 = r0.getDimensionPixelSize(r1, r7)
            r10.scrimVisibleHeightTrigger = r1
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_maxLines
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x011e
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_maxLines
            int r1 = r0.getInt(r1, r6)
            r9.setMaxLines(r1)
        L_0x011e:
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_titlePositionInterpolator
            boolean r1 = r0.hasValue(r1)
            if (r1 == 0) goto L_0x0133
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_titlePositionInterpolator
            int r1 = r0.getResourceId(r1, r8)
            android.view.animation.Interpolator r1 = android.view.animation.AnimationUtils.loadInterpolator(r11, r1)
            r9.setPositionInterpolator(r1)
        L_0x0133:
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_scrimAnimationDuration
            r2 = 600(0x258, float:8.41E-43)
            int r1 = r0.getInt(r1, r2)
            long r1 = (long) r1
            r10.scrimAnimationDuration = r1
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_contentScrim
            android.graphics.drawable.Drawable r1 = r0.getDrawable(r1)
            r10.setContentScrim(r1)
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_statusBarScrim
            android.graphics.drawable.Drawable r1 = r0.getDrawable(r1)
            r10.setStatusBarScrim(r1)
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_titleCollapseMode
            int r1 = r0.getInt(r1, r8)
            r10.setTitleCollapseMode(r1)
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_toolbarId
            int r1 = r0.getResourceId(r1, r7)
            r10.toolbarId = r1
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_forceApplySystemWindowInsetTop
            boolean r1 = r0.getBoolean(r1, r8)
            r10.forceApplySystemWindowInsetTop = r1
            int r1 = com.google.android.material.R.styleable.CollapsingToolbarLayout_extraMultilineHeightEnabled
            boolean r1 = r0.getBoolean(r1, r8)
            r10.extraMultilineHeightEnabled = r1
            r0.recycle()
            r10.setWillNotDraw(r8)
            com.google.android.material.appbar.CollapsingToolbarLayout$1 r1 = new com.google.android.material.appbar.CollapsingToolbarLayout$1
            r1.<init>()
            androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(r10, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.appbar.CollapsingToolbarLayout.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private void animateScrim(int targetAlpha) {
        ensureToolbar();
        ValueAnimator valueAnimator = this.scrimAnimator;
        if (valueAnimator == null) {
            ValueAnimator valueAnimator2 = new ValueAnimator();
            this.scrimAnimator = valueAnimator2;
            valueAnimator2.setInterpolator(targetAlpha > this.scrimAlpha ? AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR : AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
            this.scrimAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animator) {
                    CollapsingToolbarLayout.this.setScrimAlpha(((Integer) animator.getAnimatedValue()).intValue());
                }
            });
        } else if (valueAnimator.isRunning()) {
            this.scrimAnimator.cancel();
        }
        this.scrimAnimator.setDuration(this.scrimAnimationDuration);
        this.scrimAnimator.setIntValues(new int[]{this.scrimAlpha, targetAlpha});
        this.scrimAnimator.start();
    }

    private void disableLiftOnScrollIfNeeded(AppBarLayout appBarLayout) {
        if (isTitleCollapseFadeMode()) {
            appBarLayout.setLiftOnScroll(false);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v0, resolved type: android.view.View} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v7, resolved type: android.view.ViewGroup} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void ensureToolbar() {
        /*
            r5 = this;
            boolean r0 = r5.refreshToolbar
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            r0 = 0
            r5.toolbar = r0
            r5.toolbarDirectChild = r0
            int r0 = r5.toolbarId
            r1 = -1
            if (r0 == r1) goto L_0x001f
            android.view.View r0 = r5.findViewById(r0)
            android.view.ViewGroup r0 = (android.view.ViewGroup) r0
            r5.toolbar = r0
            if (r0 == 0) goto L_0x001f
            android.view.View r0 = r5.findDirectChild(r0)
            r5.toolbarDirectChild = r0
        L_0x001f:
            android.view.ViewGroup r0 = r5.toolbar
            if (r0 != 0) goto L_0x003e
            r0 = 0
            r1 = 0
            int r2 = r5.getChildCount()
        L_0x0029:
            if (r1 >= r2) goto L_0x003c
            android.view.View r3 = r5.getChildAt(r1)
            boolean r4 = isToolbar(r3)
            if (r4 == 0) goto L_0x0039
            r0 = r3
            android.view.ViewGroup r0 = (android.view.ViewGroup) r0
            goto L_0x003c
        L_0x0039:
            int r1 = r1 + 1
            goto L_0x0029
        L_0x003c:
            r5.toolbar = r0
        L_0x003e:
            r5.updateDummyView()
            r0 = 0
            r5.refreshToolbar = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.appbar.CollapsingToolbarLayout.ensureToolbar():void");
    }

    private View findDirectChild(View descendant) {
        View view = descendant;
        ViewParent parent = descendant.getParent();
        while (parent != this && parent != null) {
            if (parent instanceof View) {
                view = (View) parent;
            }
            parent = parent.getParent();
        }
        return view;
    }

    private static int getHeightWithMargins(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            return view.getMeasuredHeight();
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
        return view.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
    }

    private static CharSequence getToolbarTitle(View view) {
        if (view instanceof Toolbar) {
            return ((Toolbar) view).getTitle();
        }
        if (Build.VERSION.SDK_INT < 21 || !(view instanceof android.widget.Toolbar)) {
            return null;
        }
        return ((android.widget.Toolbar) view).getTitle();
    }

    static ViewOffsetHelper getViewOffsetHelper(View view) {
        ViewOffsetHelper viewOffsetHelper = (ViewOffsetHelper) view.getTag(R.id.view_offset_helper);
        if (viewOffsetHelper != null) {
            return viewOffsetHelper;
        }
        ViewOffsetHelper viewOffsetHelper2 = new ViewOffsetHelper(view);
        view.setTag(R.id.view_offset_helper, viewOffsetHelper2);
        return viewOffsetHelper2;
    }

    private boolean isTitleCollapseFadeMode() {
        return this.titleCollapseMode == 1;
    }

    private static boolean isToolbar(View view) {
        return (view instanceof Toolbar) || (Build.VERSION.SDK_INT >= 21 && (view instanceof android.widget.Toolbar));
    }

    private boolean isToolbarChild(View child) {
        View view = this.toolbarDirectChild;
        return (view == null || view == this) ? child == this.toolbar : child == view;
    }

    private void updateCollapsedBounds(boolean isRtl) {
        int i;
        int i2;
        int i3;
        int i4;
        View view = this.toolbarDirectChild;
        if (view == null) {
            view = this.toolbar;
        }
        int maxOffsetForPinChild = getMaxOffsetForPinChild(view);
        DescendantOffsetUtils.getDescendantRect(this, this.dummyView, this.tmpRect);
        ViewGroup viewGroup = this.toolbar;
        if (viewGroup instanceof Toolbar) {
            Toolbar toolbar2 = (Toolbar) viewGroup;
            i3 = toolbar2.getTitleMarginStart();
            i2 = toolbar2.getTitleMarginEnd();
            i = toolbar2.getTitleMarginTop();
            i4 = toolbar2.getTitleMarginBottom();
        } else {
            if (Build.VERSION.SDK_INT >= 24) {
                ViewGroup viewGroup2 = this.toolbar;
                if (viewGroup2 instanceof android.widget.Toolbar) {
                    android.widget.Toolbar toolbar3 = (android.widget.Toolbar) viewGroup2;
                    i3 = toolbar3.getTitleMarginStart();
                    i2 = toolbar3.getTitleMarginEnd();
                    i = toolbar3.getTitleMarginTop();
                    i4 = toolbar3.getTitleMarginBottom();
                }
            }
            i3 = 0;
            i2 = 0;
            i = 0;
            i4 = 0;
        }
        this.collapsingTextHelper.setCollapsedBounds(this.tmpRect.left + (isRtl ? i2 : i3), this.tmpRect.top + maxOffsetForPinChild + i, this.tmpRect.right - (isRtl ? i3 : i2), (this.tmpRect.bottom + maxOffsetForPinChild) - i4);
    }

    private void updateContentDescriptionFromTitle() {
        setContentDescription(getTitle());
    }

    private void updateContentScrimBounds(Drawable contentScrim2, int width, int height) {
        updateContentScrimBounds(contentScrim2, this.toolbar, width, height);
    }

    private void updateContentScrimBounds(Drawable contentScrim2, View toolbar2, int width, int height) {
        contentScrim2.setBounds(0, 0, width, (!isTitleCollapseFadeMode() || toolbar2 == null || !this.collapsingTitleEnabled) ? height : toolbar2.getBottom());
    }

    private void updateDummyView() {
        View view;
        if (!this.collapsingTitleEnabled && (view = this.dummyView) != null) {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this.dummyView);
            }
        }
        if (this.collapsingTitleEnabled && this.toolbar != null) {
            if (this.dummyView == null) {
                this.dummyView = new View(getContext());
            }
            if (this.dummyView.getParent() == null) {
                this.toolbar.addView(this.dummyView, -1, -1);
            }
        }
    }

    private void updateTextBounds(int left, int top, int right, int bottom, boolean forceRecalculate) {
        View view;
        if (this.collapsingTitleEnabled && (view = this.dummyView) != null) {
            boolean z = false;
            boolean z2 = ViewCompat.isAttachedToWindow(view) && this.dummyView.getVisibility() == 0;
            this.drawCollapsingTitle = z2;
            if (z2 || forceRecalculate) {
                if (ViewCompat.getLayoutDirection(this) == 1) {
                    z = true;
                }
                boolean z3 = z;
                updateCollapsedBounds(z3);
                this.collapsingTextHelper.setExpandedBounds(z3 ? this.expandedMarginEnd : this.expandedMarginStart, this.tmpRect.top + this.expandedMarginTop, (right - left) - (z3 ? this.expandedMarginStart : this.expandedMarginEnd), (bottom - top) - this.expandedMarginBottom);
                this.collapsingTextHelper.recalculate(forceRecalculate);
            }
        }
    }

    private void updateTitleFromToolbarIfNeeded() {
        if (this.toolbar != null && this.collapsingTitleEnabled && TextUtils.isEmpty(this.collapsingTextHelper.getText())) {
            setTitle(getToolbarTitle(this.toolbar));
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public void draw(Canvas canvas) {
        Drawable drawable;
        super.draw(canvas);
        ensureToolbar();
        if (this.toolbar == null && (drawable = this.contentScrim) != null && this.scrimAlpha > 0) {
            drawable.mutate().setAlpha(this.scrimAlpha);
            this.contentScrim.draw(canvas);
        }
        if (this.collapsingTitleEnabled && this.drawCollapsingTitle) {
            if (this.toolbar == null || this.contentScrim == null || this.scrimAlpha <= 0 || !isTitleCollapseFadeMode() || this.collapsingTextHelper.getExpansionFraction() >= this.collapsingTextHelper.getFadeModeThresholdFraction()) {
                this.collapsingTextHelper.draw(canvas);
            } else {
                int save = canvas.save();
                canvas.clipRect(this.contentScrim.getBounds(), Region.Op.DIFFERENCE);
                this.collapsingTextHelper.draw(canvas);
                canvas.restoreToCount(save);
            }
        }
        if (this.statusBarScrim != null && this.scrimAlpha > 0) {
            WindowInsetsCompat windowInsetsCompat = this.lastInsets;
            int systemWindowInsetTop = windowInsetsCompat != null ? windowInsetsCompat.getSystemWindowInsetTop() : 0;
            if (systemWindowInsetTop > 0) {
                this.statusBarScrim.setBounds(0, -this.currentOffset, getWidth(), systemWindowInsetTop - this.currentOffset);
                this.statusBarScrim.mutate().setAlpha(this.scrimAlpha);
                this.statusBarScrim.draw(canvas);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean z = false;
        if (this.contentScrim != null && this.scrimAlpha > 0 && isToolbarChild(child)) {
            updateContentScrimBounds(this.contentScrim, child, getWidth(), getHeight());
            this.contentScrim.mutate().setAlpha(this.scrimAlpha);
            this.contentScrim.draw(canvas);
            z = true;
        }
        return super.drawChild(canvas, child, drawingTime) || z;
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        boolean z = false;
        Drawable drawable = this.statusBarScrim;
        if (drawable != null && drawable.isStateful()) {
            z = false | drawable.setState(drawableState);
        }
        Drawable drawable2 = this.contentScrim;
        if (drawable2 != null && drawable2.isStateful()) {
            z |= drawable2.setState(drawableState);
        }
        CollapsingTextHelper collapsingTextHelper2 = this.collapsingTextHelper;
        if (collapsingTextHelper2 != null) {
            z |= collapsingTextHelper2.setState(drawableState);
        }
        if (z) {
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* access modifiers changed from: protected */
    public FrameLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public int getCollapsedTitleGravity() {
        return this.collapsingTextHelper.getCollapsedTextGravity();
    }

    public Typeface getCollapsedTitleTypeface() {
        return this.collapsingTextHelper.getCollapsedTypeface();
    }

    public Drawable getContentScrim() {
        return this.contentScrim;
    }

    public int getExpandedTitleGravity() {
        return this.collapsingTextHelper.getExpandedTextGravity();
    }

    public int getExpandedTitleMarginBottom() {
        return this.expandedMarginBottom;
    }

    public int getExpandedTitleMarginEnd() {
        return this.expandedMarginEnd;
    }

    public int getExpandedTitleMarginStart() {
        return this.expandedMarginStart;
    }

    public int getExpandedTitleMarginTop() {
        return this.expandedMarginTop;
    }

    public Typeface getExpandedTitleTypeface() {
        return this.collapsingTextHelper.getExpandedTypeface();
    }

    public int getHyphenationFrequency() {
        return this.collapsingTextHelper.getHyphenationFrequency();
    }

    public int getLineCount() {
        return this.collapsingTextHelper.getLineCount();
    }

    public float getLineSpacingAdd() {
        return this.collapsingTextHelper.getLineSpacingAdd();
    }

    public float getLineSpacingMultiplier() {
        return this.collapsingTextHelper.getLineSpacingMultiplier();
    }

    public int getMaxLines() {
        return this.collapsingTextHelper.getMaxLines();
    }

    /* access modifiers changed from: package-private */
    public final int getMaxOffsetForPinChild(View child) {
        return ((getHeight() - getViewOffsetHelper(child).getLayoutTop()) - child.getHeight()) - ((LayoutParams) child.getLayoutParams()).bottomMargin;
    }

    /* access modifiers changed from: package-private */
    public int getScrimAlpha() {
        return this.scrimAlpha;
    }

    public long getScrimAnimationDuration() {
        return this.scrimAnimationDuration;
    }

    public int getScrimVisibleHeightTrigger() {
        int i = this.scrimVisibleHeightTrigger;
        if (i >= 0) {
            return i + this.topInsetApplied + this.extraMultilineHeight;
        }
        WindowInsetsCompat windowInsetsCompat = this.lastInsets;
        int systemWindowInsetTop = windowInsetsCompat != null ? windowInsetsCompat.getSystemWindowInsetTop() : 0;
        int minimumHeight = ViewCompat.getMinimumHeight(this);
        return minimumHeight > 0 ? Math.min((minimumHeight * 2) + systemWindowInsetTop, getHeight()) : getHeight() / 3;
    }

    public Drawable getStatusBarScrim() {
        return this.statusBarScrim;
    }

    public CharSequence getTitle() {
        if (this.collapsingTitleEnabled) {
            return this.collapsingTextHelper.getText();
        }
        return null;
    }

    public int getTitleCollapseMode() {
        return this.titleCollapseMode;
    }

    public TimeInterpolator getTitlePositionInterpolator() {
        return this.collapsingTextHelper.getPositionInterpolator();
    }

    public boolean isExtraMultilineHeightEnabled() {
        return this.extraMultilineHeightEnabled;
    }

    public boolean isForceApplySystemWindowInsetTop() {
        return this.forceApplySystemWindowInsetTop;
    }

    public boolean isRtlTextDirectionHeuristicsEnabled() {
        return this.collapsingTextHelper.isRtlTextDirectionHeuristicsEnabled();
    }

    public boolean isTitleEnabled() {
        return this.collapsingTitleEnabled;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {
            AppBarLayout appBarLayout = (AppBarLayout) parent;
            disableLiftOnScrollIfNeeded(appBarLayout);
            ViewCompat.setFitsSystemWindows(this, ViewCompat.getFitsSystemWindows(appBarLayout));
            if (this.onOffsetChangedListener == null) {
                this.onOffsetChangedListener = new OffsetUpdateListener();
            }
            appBarLayout.addOnOffsetChangedListener(this.onOffsetChangedListener);
            ViewCompat.requestApplyInsets(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.collapsingTextHelper.maybeUpdateFontWeightAdjustment(newConfig);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        ViewParent parent = getParent();
        AppBarLayout.OnOffsetChangedListener onOffsetChangedListener2 = this.onOffsetChangedListener;
        if (onOffsetChangedListener2 != null && (parent instanceof AppBarLayout)) {
            ((AppBarLayout) parent).removeOnOffsetChangedListener(onOffsetChangedListener2);
        }
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        WindowInsetsCompat windowInsetsCompat = this.lastInsets;
        if (windowInsetsCompat != null) {
            int systemWindowInsetTop = windowInsetsCompat.getSystemWindowInsetTop();
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);
                if (!ViewCompat.getFitsSystemWindows(childAt) && childAt.getTop() < systemWindowInsetTop) {
                    ViewCompat.offsetTopAndBottom(childAt, systemWindowInsetTop);
                }
            }
        }
        int childCount2 = getChildCount();
        for (int i2 = 0; i2 < childCount2; i2++) {
            getViewOffsetHelper(getChildAt(i2)).onViewLayout();
        }
        updateTextBounds(left, top, right, bottom, false);
        updateTitleFromToolbarIfNeeded();
        updateScrimVisibility();
        int childCount3 = getChildCount();
        for (int i3 = 0; i3 < childCount3; i3++) {
            getViewOffsetHelper(getChildAt(i3)).applyOffsets();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ensureToolbar();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = View.MeasureSpec.getMode(heightMeasureSpec);
        WindowInsetsCompat windowInsetsCompat = this.lastInsets;
        int systemWindowInsetTop = windowInsetsCompat != null ? windowInsetsCompat.getSystemWindowInsetTop() : 0;
        if ((mode == 0 || this.forceApplySystemWindowInsetTop) && systemWindowInsetTop > 0) {
            this.topInsetApplied = systemWindowInsetTop;
            super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() + systemWindowInsetTop, BasicMeasure.EXACTLY));
        }
        if (this.extraMultilineHeightEnabled && this.collapsingTextHelper.getMaxLines() > 1) {
            updateTitleFromToolbarIfNeeded();
            updateTextBounds(0, 0, getMeasuredWidth(), getMeasuredHeight(), true);
            int expandedLineCount = this.collapsingTextHelper.getExpandedLineCount();
            if (expandedLineCount > 1) {
                this.extraMultilineHeight = (expandedLineCount - 1) * Math.round(this.collapsingTextHelper.getExpandedTextFullHeight());
                super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() + this.extraMultilineHeight, BasicMeasure.EXACTLY));
            }
        }
        ViewGroup viewGroup = this.toolbar;
        if (viewGroup != null) {
            View view = this.toolbarDirectChild;
            if (view == null || view == this) {
                setMinimumHeight(getHeightWithMargins(viewGroup));
            } else {
                setMinimumHeight(getHeightWithMargins(view));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Drawable drawable = this.contentScrim;
        if (drawable != null) {
            updateContentScrimBounds(drawable, w, h);
        }
    }

    /* access modifiers changed from: package-private */
    public WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat insets) {
        WindowInsetsCompat windowInsetsCompat = null;
        if (ViewCompat.getFitsSystemWindows(this)) {
            windowInsetsCompat = insets;
        }
        if (!ObjectsCompat.equals(this.lastInsets, windowInsetsCompat)) {
            this.lastInsets = windowInsetsCompat;
            requestLayout();
        }
        return insets.consumeSystemWindowInsets();
    }

    public void setCollapsedTitleGravity(int gravity) {
        this.collapsingTextHelper.setCollapsedTextGravity(gravity);
    }

    public void setCollapsedTitleTextAppearance(int resId) {
        this.collapsingTextHelper.setCollapsedTextAppearance(resId);
    }

    public void setCollapsedTitleTextColor(int color) {
        setCollapsedTitleTextColor(ColorStateList.valueOf(color));
    }

    public void setCollapsedTitleTextColor(ColorStateList colors) {
        this.collapsingTextHelper.setCollapsedTextColor(colors);
    }

    public void setCollapsedTitleTypeface(Typeface typeface) {
        this.collapsingTextHelper.setCollapsedTypeface(typeface);
    }

    public void setContentScrim(Drawable drawable) {
        Drawable drawable2 = this.contentScrim;
        if (drawable2 != drawable) {
            Drawable drawable3 = null;
            if (drawable2 != null) {
                drawable2.setCallback((Drawable.Callback) null);
            }
            if (drawable != null) {
                drawable3 = drawable.mutate();
            }
            this.contentScrim = drawable3;
            if (drawable3 != null) {
                updateContentScrimBounds(drawable3, getWidth(), getHeight());
                this.contentScrim.setCallback(this);
                this.contentScrim.setAlpha(this.scrimAlpha);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setContentScrimColor(int color) {
        setContentScrim(new ColorDrawable(color));
    }

    public void setContentScrimResource(int resId) {
        setContentScrim(ContextCompat.getDrawable(getContext(), resId));
    }

    public void setExpandedTitleColor(int color) {
        setExpandedTitleTextColor(ColorStateList.valueOf(color));
    }

    public void setExpandedTitleGravity(int gravity) {
        this.collapsingTextHelper.setExpandedTextGravity(gravity);
    }

    public void setExpandedTitleMargin(int start, int top, int end, int bottom) {
        this.expandedMarginStart = start;
        this.expandedMarginTop = top;
        this.expandedMarginEnd = end;
        this.expandedMarginBottom = bottom;
        requestLayout();
    }

    public void setExpandedTitleMarginBottom(int margin) {
        this.expandedMarginBottom = margin;
        requestLayout();
    }

    public void setExpandedTitleMarginEnd(int margin) {
        this.expandedMarginEnd = margin;
        requestLayout();
    }

    public void setExpandedTitleMarginStart(int margin) {
        this.expandedMarginStart = margin;
        requestLayout();
    }

    public void setExpandedTitleMarginTop(int margin) {
        this.expandedMarginTop = margin;
        requestLayout();
    }

    public void setExpandedTitleTextAppearance(int resId) {
        this.collapsingTextHelper.setExpandedTextAppearance(resId);
    }

    public void setExpandedTitleTextColor(ColorStateList colors) {
        this.collapsingTextHelper.setExpandedTextColor(colors);
    }

    public void setExpandedTitleTypeface(Typeface typeface) {
        this.collapsingTextHelper.setExpandedTypeface(typeface);
    }

    public void setExtraMultilineHeightEnabled(boolean extraMultilineHeightEnabled2) {
        this.extraMultilineHeightEnabled = extraMultilineHeightEnabled2;
    }

    public void setForceApplySystemWindowInsetTop(boolean forceApplySystemWindowInsetTop2) {
        this.forceApplySystemWindowInsetTop = forceApplySystemWindowInsetTop2;
    }

    public void setHyphenationFrequency(int hyphenationFrequency) {
        this.collapsingTextHelper.setHyphenationFrequency(hyphenationFrequency);
    }

    public void setLineSpacingAdd(float spacingAdd) {
        this.collapsingTextHelper.setLineSpacingAdd(spacingAdd);
    }

    public void setLineSpacingMultiplier(float spacingMultiplier) {
        this.collapsingTextHelper.setLineSpacingMultiplier(spacingMultiplier);
    }

    public void setMaxLines(int maxLines) {
        this.collapsingTextHelper.setMaxLines(maxLines);
    }

    public void setRtlTextDirectionHeuristicsEnabled(boolean rtlTextDirectionHeuristicsEnabled) {
        this.collapsingTextHelper.setRtlTextDirectionHeuristicsEnabled(rtlTextDirectionHeuristicsEnabled);
    }

    /* access modifiers changed from: package-private */
    public void setScrimAlpha(int alpha) {
        ViewGroup viewGroup;
        if (alpha != this.scrimAlpha) {
            if (!(this.contentScrim == null || (viewGroup = this.toolbar) == null)) {
                ViewCompat.postInvalidateOnAnimation(viewGroup);
            }
            this.scrimAlpha = alpha;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setScrimAnimationDuration(long duration) {
        this.scrimAnimationDuration = duration;
    }

    public void setScrimVisibleHeightTrigger(int height) {
        if (this.scrimVisibleHeightTrigger != height) {
            this.scrimVisibleHeightTrigger = height;
            updateScrimVisibility();
        }
    }

    public void setScrimsShown(boolean shown) {
        setScrimsShown(shown, ViewCompat.isLaidOut(this) && !isInEditMode());
    }

    public void setScrimsShown(boolean shown, boolean animate) {
        if (this.scrimsAreShown != shown) {
            int i = 255;
            if (animate) {
                if (!shown) {
                    i = 0;
                }
                animateScrim(i);
            } else {
                if (!shown) {
                    i = 0;
                }
                setScrimAlpha(i);
            }
            this.scrimsAreShown = shown;
        }
    }

    public void setStatusBarScrim(Drawable drawable) {
        Drawable drawable2 = this.statusBarScrim;
        if (drawable2 != drawable) {
            Drawable drawable3 = null;
            if (drawable2 != null) {
                drawable2.setCallback((Drawable.Callback) null);
            }
            if (drawable != null) {
                drawable3 = drawable.mutate();
            }
            this.statusBarScrim = drawable3;
            if (drawable3 != null) {
                if (drawable3.isStateful()) {
                    this.statusBarScrim.setState(getDrawableState());
                }
                DrawableCompat.setLayoutDirection(this.statusBarScrim, ViewCompat.getLayoutDirection(this));
                this.statusBarScrim.setVisible(getVisibility() == 0, false);
                this.statusBarScrim.setCallback(this);
                this.statusBarScrim.setAlpha(this.scrimAlpha);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setStatusBarScrimColor(int color) {
        setStatusBarScrim(new ColorDrawable(color));
    }

    public void setStatusBarScrimResource(int resId) {
        setStatusBarScrim(ContextCompat.getDrawable(getContext(), resId));
    }

    public void setTitle(CharSequence title) {
        this.collapsingTextHelper.setText(title);
        updateContentDescriptionFromTitle();
    }

    public void setTitleCollapseMode(int titleCollapseMode2) {
        this.titleCollapseMode = titleCollapseMode2;
        boolean isTitleCollapseFadeMode = isTitleCollapseFadeMode();
        this.collapsingTextHelper.setFadeModeEnabled(isTitleCollapseFadeMode);
        ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {
            disableLiftOnScrollIfNeeded((AppBarLayout) parent);
        }
        if (isTitleCollapseFadeMode && this.contentScrim == null) {
            setContentScrimColor(this.elevationOverlayProvider.compositeOverlayWithThemeSurfaceColorIfNeeded(getResources().getDimension(R.dimen.design_appbar_elevation)));
        }
    }

    public void setTitleEnabled(boolean enabled) {
        if (enabled != this.collapsingTitleEnabled) {
            this.collapsingTitleEnabled = enabled;
            updateContentDescriptionFromTitle();
            updateDummyView();
            requestLayout();
        }
    }

    public void setTitlePositionInterpolator(TimeInterpolator interpolator) {
        this.collapsingTextHelper.setPositionInterpolator(interpolator);
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        boolean z = visibility == 0;
        Drawable drawable = this.statusBarScrim;
        if (!(drawable == null || drawable.isVisible() == z)) {
            this.statusBarScrim.setVisible(z, false);
        }
        Drawable drawable2 = this.contentScrim;
        if (drawable2 != null && drawable2.isVisible() != z) {
            this.contentScrim.setVisible(z, false);
        }
    }

    /* access modifiers changed from: package-private */
    public final void updateScrimVisibility() {
        if (this.contentScrim != null || this.statusBarScrim != null) {
            setScrimsShown(getHeight() + this.currentOffset < getScrimVisibleHeightTrigger());
        }
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.contentScrim || who == this.statusBarScrim;
    }
}

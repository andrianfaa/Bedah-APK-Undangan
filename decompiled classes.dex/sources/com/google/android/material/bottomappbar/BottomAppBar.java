package com.google.android.material.bottomappbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.animation.TransformationCallback;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BottomAppBar extends Toolbar implements CoordinatorLayout.AttachedBehavior {
    private static final long ANIMATION_DURATION = 300;
    private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_BottomAppBar;
    public static final int FAB_ALIGNMENT_MODE_CENTER = 0;
    public static final int FAB_ALIGNMENT_MODE_END = 1;
    public static final int FAB_ANIMATION_MODE_SCALE = 0;
    public static final int FAB_ANIMATION_MODE_SLIDE = 1;
    private static final int NO_MENU_RES_ID = 0;
    private int animatingModeChangeCounter;
    private ArrayList<AnimationListener> animationListeners;
    private Behavior behavior;
    /* access modifiers changed from: private */
    public int bottomInset;
    /* access modifiers changed from: private */
    public int fabAlignmentMode;
    AnimatorListenerAdapter fabAnimationListener;
    private int fabAnimationMode;
    /* access modifiers changed from: private */
    public boolean fabAttached;
    /* access modifiers changed from: private */
    public final int fabOffsetEndMode;
    TransformationCallback<FloatingActionButton> fabTransformationCallback;
    private boolean hideOnScroll;
    /* access modifiers changed from: private */
    public int leftInset;
    /* access modifiers changed from: private */
    public final MaterialShapeDrawable materialShapeDrawable;
    /* access modifiers changed from: private */
    public boolean menuAnimatingWithFabAlignmentMode;
    /* access modifiers changed from: private */
    public Animator menuAnimator;
    /* access modifiers changed from: private */
    public Animator modeAnimator;
    private Integer navigationIconTint;
    /* access modifiers changed from: private */
    public final boolean paddingBottomSystemWindowInsets;
    /* access modifiers changed from: private */
    public final boolean paddingLeftSystemWindowInsets;
    /* access modifiers changed from: private */
    public final boolean paddingRightSystemWindowInsets;
    /* access modifiers changed from: private */
    public int pendingMenuResId;
    /* access modifiers changed from: private */
    public int rightInset;

    interface AnimationListener {
        void onAnimationEnd(BottomAppBar bottomAppBar);

        void onAnimationStart(BottomAppBar bottomAppBar);
    }

    public static class Behavior extends HideBottomViewOnScrollBehavior<BottomAppBar> {
        /* access modifiers changed from: private */
        public final Rect fabContentRect = new Rect();
        private final View.OnLayoutChangeListener fabLayoutListener = new View.OnLayoutChangeListener() {
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                View view = v;
                BottomAppBar bottomAppBar = (BottomAppBar) Behavior.this.viewRef.get();
                if (bottomAppBar == null || !(view instanceof FloatingActionButton)) {
                    v.removeOnLayoutChangeListener(this);
                    return;
                }
                FloatingActionButton floatingActionButton = (FloatingActionButton) view;
                floatingActionButton.getMeasuredContentRect(Behavior.this.fabContentRect);
                int height = Behavior.this.fabContentRect.height();
                bottomAppBar.setFabDiameter(height);
                bottomAppBar.setFabCornerSize(floatingActionButton.getShapeAppearanceModel().getTopLeftCornerSize().getCornerSize(new RectF(Behavior.this.fabContentRect)));
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) v.getLayoutParams();
                if (Behavior.this.originalBottomMargin == 0) {
                    layoutParams.bottomMargin = bottomAppBar.getBottomInset() + (bottomAppBar.getResources().getDimensionPixelOffset(R.dimen.mtrl_bottomappbar_fab_bottom_margin) - ((floatingActionButton.getMeasuredHeight() - height) / 2));
                    layoutParams.leftMargin = bottomAppBar.getLeftInset();
                    layoutParams.rightMargin = bottomAppBar.getRightInset();
                    if (ViewUtils.isLayoutRtl(floatingActionButton)) {
                        layoutParams.leftMargin += bottomAppBar.fabOffsetEndMode;
                    } else {
                        layoutParams.rightMargin += bottomAppBar.fabOffsetEndMode;
                    }
                }
            }
        };
        /* access modifiers changed from: private */
        public int originalBottomMargin;
        /* access modifiers changed from: private */
        public WeakReference<BottomAppBar> viewRef;

        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public boolean onLayoutChild(CoordinatorLayout parent, BottomAppBar child, int layoutDirection) {
            this.viewRef = new WeakReference<>(child);
            View access$3100 = child.findDependentView();
            if (access$3100 != null && !ViewCompat.isLaidOut(access$3100)) {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) access$3100.getLayoutParams();
                layoutParams.anchorGravity = 49;
                this.originalBottomMargin = layoutParams.bottomMargin;
                if (access$3100 instanceof FloatingActionButton) {
                    FloatingActionButton floatingActionButton = (FloatingActionButton) access$3100;
                    if (floatingActionButton.getShowMotionSpec() == null) {
                        floatingActionButton.setShowMotionSpecResource(R.animator.mtrl_fab_show_motion_spec);
                    }
                    if (floatingActionButton.getHideMotionSpec() == null) {
                        floatingActionButton.setHideMotionSpecResource(R.animator.mtrl_fab_hide_motion_spec);
                    }
                    floatingActionButton.addOnLayoutChangeListener(this.fabLayoutListener);
                    child.addFabAnimationListeners(floatingActionButton);
                }
                child.setCutoutState();
            }
            parent.onLayoutChild(child, layoutDirection);
            return super.onLayoutChild(parent, child, layoutDirection);
        }

        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, BottomAppBar child, View directTargetChild, View target, int axes, int type) {
            return child.getHideOnScroll() && super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FabAlignmentMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FabAnimationMode {
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
        int fabAlignmentMode;
        boolean fabAttached;

        public SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            this.fabAlignmentMode = in.readInt();
            this.fabAttached = in.readInt() != 0;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.fabAlignmentMode);
            out.writeInt(this.fabAttached ? 1 : 0);
        }
    }

    public BottomAppBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public BottomAppBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.bottomAppBarStyle);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public BottomAppBar(android.content.Context r12, android.util.AttributeSet r13, int r14) {
        /*
            r11 = this;
            int r6 = DEF_STYLE_RES
            android.content.Context r0 = com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap(r12, r13, r14, r6)
            r11.<init>(r0, r13, r14)
            com.google.android.material.shape.MaterialShapeDrawable r7 = new com.google.android.material.shape.MaterialShapeDrawable
            r7.<init>()
            r11.materialShapeDrawable = r7
            r8 = 0
            r11.animatingModeChangeCounter = r8
            r11.pendingMenuResId = r8
            r11.menuAnimatingWithFabAlignmentMode = r8
            r0 = 1
            r11.fabAttached = r0
            com.google.android.material.bottomappbar.BottomAppBar$1 r0 = new com.google.android.material.bottomappbar.BottomAppBar$1
            r0.<init>()
            r11.fabAnimationListener = r0
            com.google.android.material.bottomappbar.BottomAppBar$2 r0 = new com.google.android.material.bottomappbar.BottomAppBar$2
            r0.<init>()
            r11.fabTransformationCallback = r0
            android.content.Context r12 = r11.getContext()
            int[] r2 = com.google.android.material.R.styleable.BottomAppBar
            int[] r5 = new int[r8]
            r0 = r12
            r1 = r13
            r3 = r14
            r4 = r6
            android.content.res.TypedArray r0 = com.google.android.material.internal.ThemeEnforcement.obtainStyledAttributes(r0, r1, r2, r3, r4, r5)
            int r1 = com.google.android.material.R.styleable.BottomAppBar_backgroundTint
            android.content.res.ColorStateList r1 = com.google.android.material.resources.MaterialResources.getColorStateList((android.content.Context) r12, (android.content.res.TypedArray) r0, (int) r1)
            int r2 = com.google.android.material.R.styleable.BottomAppBar_navigationIconTint
            boolean r2 = r0.hasValue(r2)
            if (r2 == 0) goto L_0x0050
            int r2 = com.google.android.material.R.styleable.BottomAppBar_navigationIconTint
            r3 = -1
            int r2 = r0.getColor(r2, r3)
            r11.setNavigationIconTint(r2)
        L_0x0050:
            int r2 = com.google.android.material.R.styleable.BottomAppBar_elevation
            int r2 = r0.getDimensionPixelSize(r2, r8)
            int r3 = com.google.android.material.R.styleable.BottomAppBar_fabCradleMargin
            int r3 = r0.getDimensionPixelOffset(r3, r8)
            float r3 = (float) r3
            int r4 = com.google.android.material.R.styleable.BottomAppBar_fabCradleRoundedCornerRadius
            int r4 = r0.getDimensionPixelOffset(r4, r8)
            float r4 = (float) r4
            int r5 = com.google.android.material.R.styleable.BottomAppBar_fabCradleVerticalOffset
            int r5 = r0.getDimensionPixelOffset(r5, r8)
            float r5 = (float) r5
            int r9 = com.google.android.material.R.styleable.BottomAppBar_fabAlignmentMode
            int r9 = r0.getInt(r9, r8)
            r11.fabAlignmentMode = r9
            int r9 = com.google.android.material.R.styleable.BottomAppBar_fabAnimationMode
            int r9 = r0.getInt(r9, r8)
            r11.fabAnimationMode = r9
            int r9 = com.google.android.material.R.styleable.BottomAppBar_hideOnScroll
            boolean r9 = r0.getBoolean(r9, r8)
            r11.hideOnScroll = r9
            int r9 = com.google.android.material.R.styleable.BottomAppBar_paddingBottomSystemWindowInsets
            boolean r9 = r0.getBoolean(r9, r8)
            r11.paddingBottomSystemWindowInsets = r9
            int r9 = com.google.android.material.R.styleable.BottomAppBar_paddingLeftSystemWindowInsets
            boolean r9 = r0.getBoolean(r9, r8)
            r11.paddingLeftSystemWindowInsets = r9
            int r9 = com.google.android.material.R.styleable.BottomAppBar_paddingRightSystemWindowInsets
            boolean r8 = r0.getBoolean(r9, r8)
            r11.paddingRightSystemWindowInsets = r8
            r0.recycle()
            android.content.res.Resources r8 = r11.getResources()
            int r9 = com.google.android.material.R.dimen.mtrl_bottomappbar_fabOffsetEndMode
            int r8 = r8.getDimensionPixelOffset(r9)
            r11.fabOffsetEndMode = r8
            com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment r8 = new com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment
            r8.<init>(r3, r4, r5)
            com.google.android.material.shape.ShapeAppearanceModel$Builder r9 = com.google.android.material.shape.ShapeAppearanceModel.builder()
            com.google.android.material.shape.ShapeAppearanceModel$Builder r9 = r9.setTopEdge(r8)
            com.google.android.material.shape.ShapeAppearanceModel r9 = r9.build()
            r7.setShapeAppearanceModel(r9)
            r10 = 2
            r7.setShadowCompatibilityMode(r10)
            android.graphics.Paint$Style r10 = android.graphics.Paint.Style.FILL
            r7.setPaintStyle(r10)
            r7.initializeElevationOverlay(r12)
            float r10 = (float) r2
            r11.setElevation(r10)
            androidx.core.graphics.drawable.DrawableCompat.setTintList(r7, r1)
            androidx.core.view.ViewCompat.setBackground(r11, r7)
            com.google.android.material.bottomappbar.BottomAppBar$3 r7 = new com.google.android.material.bottomappbar.BottomAppBar$3
            r7.<init>()
            com.google.android.material.internal.ViewUtils.doOnApplyWindowInsets(r11, r13, r14, r6, r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.bottomappbar.BottomAppBar.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    /* access modifiers changed from: private */
    public void addFabAnimationListeners(FloatingActionButton fab) {
        fab.addOnHideAnimationListener(this.fabAnimationListener);
        fab.addOnShowAnimationListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
                BottomAppBar.this.fabAnimationListener.onAnimationStart(animation);
                FloatingActionButton access$2200 = BottomAppBar.this.findDependentFab();
                if (access$2200 != null) {
                    access$2200.setTranslationX(BottomAppBar.this.getFabTranslationX());
                }
            }
        });
        fab.addTransformationCallback(this.fabTransformationCallback);
    }

    /* access modifiers changed from: private */
    public void cancelAnimations() {
        Animator animator = this.menuAnimator;
        if (animator != null) {
            animator.cancel();
        }
        Animator animator2 = this.modeAnimator;
        if (animator2 != null) {
            animator2.cancel();
        }
    }

    private void createFabTranslationXAnimation(int targetMode, List<Animator> list) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(findDependentFab(), "translationX", new float[]{getFabTranslationX(targetMode)});
        ofFloat.setDuration(ANIMATION_DURATION);
        list.add(ofFloat);
    }

    private void createMenuViewTranslationAnimation(final int targetMode, final boolean targetAttached, List<Animator> list) {
        final ActionMenuView actionMenuView = getActionMenuView();
        if (actionMenuView != null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(actionMenuView, "alpha", new float[]{1.0f});
            if (Math.abs(actionMenuView.getTranslationX() - ((float) getActionMenuViewTranslationX(actionMenuView, targetMode, targetAttached))) > 1.0f) {
                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(actionMenuView, "alpha", new float[]{0.0f});
                ofFloat2.addListener(new AnimatorListenerAdapter() {
                    public boolean cancelled;

                    public void onAnimationCancel(Animator animation) {
                        this.cancelled = true;
                    }

                    public void onAnimationEnd(Animator animation) {
                        if (!this.cancelled) {
                            boolean z = BottomAppBar.this.pendingMenuResId != 0;
                            BottomAppBar bottomAppBar = BottomAppBar.this;
                            bottomAppBar.replaceMenu(bottomAppBar.pendingMenuResId);
                            BottomAppBar.this.translateActionMenuView(actionMenuView, targetMode, targetAttached, z);
                        }
                    }
                });
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(150);
                animatorSet.playSequentially(new Animator[]{ofFloat2, ofFloat});
                list.add(animatorSet);
            } else if (actionMenuView.getAlpha() < 1.0f) {
                list.add(ofFloat);
            }
        }
    }

    /* access modifiers changed from: private */
    public void dispatchAnimationEnd() {
        ArrayList<AnimationListener> arrayList;
        int i = this.animatingModeChangeCounter - 1;
        this.animatingModeChangeCounter = i;
        if (i == 0 && (arrayList = this.animationListeners) != null) {
            Iterator<AnimationListener> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().onAnimationEnd(this);
            }
        }
    }

    /* access modifiers changed from: private */
    public void dispatchAnimationStart() {
        ArrayList<AnimationListener> arrayList;
        int i = this.animatingModeChangeCounter;
        this.animatingModeChangeCounter = i + 1;
        if (i == 0 && (arrayList = this.animationListeners) != null) {
            Iterator<AnimationListener> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().onAnimationStart(this);
            }
        }
    }

    /* access modifiers changed from: private */
    public FloatingActionButton findDependentFab() {
        View findDependentView = findDependentView();
        if (findDependentView instanceof FloatingActionButton) {
            return (FloatingActionButton) findDependentView;
        }
        return null;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x001e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.view.View findDependentView() {
        /*
            r5 = this;
            android.view.ViewParent r0 = r5.getParent()
            boolean r0 = r0 instanceof androidx.coordinatorlayout.widget.CoordinatorLayout
            r1 = 0
            if (r0 != 0) goto L_0x000a
            return r1
        L_0x000a:
            android.view.ViewParent r0 = r5.getParent()
            androidx.coordinatorlayout.widget.CoordinatorLayout r0 = (androidx.coordinatorlayout.widget.CoordinatorLayout) r0
            java.util.List r0 = r0.getDependents(r5)
            java.util.Iterator r2 = r0.iterator()
        L_0x0018:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x002f
            java.lang.Object r3 = r2.next()
            android.view.View r3 = (android.view.View) r3
            boolean r4 = r3 instanceof com.google.android.material.floatingactionbutton.FloatingActionButton
            if (r4 != 0) goto L_0x002e
            boolean r4 = r3 instanceof com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
            if (r4 == 0) goto L_0x002d
            goto L_0x002e
        L_0x002d:
            goto L_0x0018
        L_0x002e:
            return r3
        L_0x002f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.bottomappbar.BottomAppBar.findDependentView():android.view.View");
    }

    private ActionMenuView getActionMenuView() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof ActionMenuView) {
                return (ActionMenuView) childAt;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public int getBottomInset() {
        return this.bottomInset;
    }

    /* access modifiers changed from: private */
    public float getFabTranslationX() {
        return getFabTranslationX(this.fabAlignmentMode);
    }

    /* access modifiers changed from: private */
    public float getFabTranslationX(int fabAlignmentMode2) {
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int i = 1;
        if (fabAlignmentMode2 != 1) {
            return 0.0f;
        }
        int measuredWidth = (getMeasuredWidth() / 2) - (this.fabOffsetEndMode + (isLayoutRtl ? this.leftInset : this.rightInset));
        if (isLayoutRtl) {
            i = -1;
        }
        return (float) (measuredWidth * i);
    }

    private float getFabTranslationY() {
        return -getTopEdgeTreatment().getCradleVerticalOffset();
    }

    /* access modifiers changed from: private */
    public int getLeftInset() {
        return this.leftInset;
    }

    /* access modifiers changed from: private */
    public int getRightInset() {
        return this.rightInset;
    }

    /* access modifiers changed from: private */
    public BottomAppBarTopEdgeTreatment getTopEdgeTreatment() {
        return (BottomAppBarTopEdgeTreatment) this.materialShapeDrawable.getShapeAppearanceModel().getTopEdge();
    }

    private boolean isFabVisibleOrWillBeShown() {
        FloatingActionButton findDependentFab = findDependentFab();
        return findDependentFab != null && findDependentFab.isOrWillBeShown();
    }

    /* access modifiers changed from: private */
    public void maybeAnimateMenuView(int targetMode, boolean newFabAttached) {
        if (!ViewCompat.isLaidOut(this)) {
            this.menuAnimatingWithFabAlignmentMode = false;
            replaceMenu(this.pendingMenuResId);
            return;
        }
        Animator animator = this.menuAnimator;
        if (animator != null) {
            animator.cancel();
        }
        ArrayList arrayList = new ArrayList();
        if (!isFabVisibleOrWillBeShown()) {
            targetMode = 0;
            newFabAttached = false;
        }
        createMenuViewTranslationAnimation(targetMode, newFabAttached, arrayList);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        this.menuAnimator = animatorSet;
        animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                BottomAppBar.this.dispatchAnimationEnd();
                boolean unused = BottomAppBar.this.menuAnimatingWithFabAlignmentMode = false;
                Animator unused2 = BottomAppBar.this.menuAnimator = null;
            }

            public void onAnimationStart(Animator animation) {
                BottomAppBar.this.dispatchAnimationStart();
            }
        });
        this.menuAnimator.start();
    }

    private void maybeAnimateModeChange(int targetMode) {
        if (this.fabAlignmentMode != targetMode && ViewCompat.isLaidOut(this)) {
            Animator animator = this.modeAnimator;
            if (animator != null) {
                animator.cancel();
            }
            ArrayList arrayList = new ArrayList();
            if (this.fabAnimationMode == 1) {
                createFabTranslationXAnimation(targetMode, arrayList);
            } else {
                createFabDefaultXAnimation(targetMode, arrayList);
            }
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(arrayList);
            this.modeAnimator = animatorSet;
            animatorSet.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    BottomAppBar.this.dispatchAnimationEnd();
                    Animator unused = BottomAppBar.this.modeAnimator = null;
                }

                public void onAnimationStart(Animator animation) {
                    BottomAppBar.this.dispatchAnimationStart();
                }
            });
            this.modeAnimator.start();
        }
    }

    private Drawable maybeTintNavigationIcon(Drawable navigationIcon) {
        if (navigationIcon == null || this.navigationIconTint == null) {
            return navigationIcon;
        }
        Drawable wrap = DrawableCompat.wrap(navigationIcon.mutate());
        DrawableCompat.setTint(wrap, this.navigationIconTint.intValue());
        return wrap;
    }

    /* access modifiers changed from: private */
    public void setActionMenuViewPosition() {
        ActionMenuView actionMenuView = getActionMenuView();
        if (actionMenuView != null && this.menuAnimator == null) {
            actionMenuView.setAlpha(1.0f);
            if (!isFabVisibleOrWillBeShown()) {
                translateActionMenuView(actionMenuView, 0, false);
            } else {
                translateActionMenuView(actionMenuView, this.fabAlignmentMode, this.fabAttached);
            }
        }
    }

    /* access modifiers changed from: private */
    public void setCutoutState() {
        getTopEdgeTreatment().setHorizontalOffset(getFabTranslationX());
        View findDependentView = findDependentView();
        this.materialShapeDrawable.setInterpolation((!this.fabAttached || !isFabVisibleOrWillBeShown()) ? 0.0f : 1.0f);
        if (findDependentView != null) {
            findDependentView.setTranslationY(getFabTranslationY());
            findDependentView.setTranslationX(getFabTranslationX());
        }
    }

    private void translateActionMenuView(ActionMenuView actionMenuView, int fabAlignmentMode2, boolean fabAttached2) {
        translateActionMenuView(actionMenuView, fabAlignmentMode2, fabAttached2, false);
    }

    /* access modifiers changed from: private */
    public void translateActionMenuView(final ActionMenuView actionMenuView, final int fabAlignmentMode2, final boolean fabAttached2, boolean shouldWaitForMenuReplacement) {
        AnonymousClass8 r0 = new Runnable() {
            public void run() {
                ActionMenuView actionMenuView = actionMenuView;
                actionMenuView.setTranslationX((float) BottomAppBar.this.getActionMenuViewTranslationX(actionMenuView, fabAlignmentMode2, fabAttached2));
            }
        };
        if (shouldWaitForMenuReplacement) {
            actionMenuView.post(r0);
        } else {
            r0.run();
        }
    }

    /* access modifiers changed from: package-private */
    public void addAnimationListener(AnimationListener listener) {
        if (this.animationListeners == null) {
            this.animationListeners = new ArrayList<>();
        }
        this.animationListeners.add(listener);
    }

    /* access modifiers changed from: protected */
    public void createFabDefaultXAnimation(final int targetMode, List<Animator> list) {
        FloatingActionButton findDependentFab = findDependentFab();
        if (findDependentFab != null && !findDependentFab.isOrWillBeHidden()) {
            dispatchAnimationStart();
            findDependentFab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                public void onHidden(FloatingActionButton fab) {
                    fab.setTranslationX(BottomAppBar.this.getFabTranslationX(targetMode));
                    fab.show(new FloatingActionButton.OnVisibilityChangedListener() {
                        public void onShown(FloatingActionButton fab) {
                            BottomAppBar.this.dispatchAnimationEnd();
                        }
                    });
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public int getActionMenuViewTranslationX(ActionMenuView actionMenuView, int fabAlignmentMode2, boolean fabAttached2) {
        if (fabAlignmentMode2 != 1 || !fabAttached2) {
            return 0;
        }
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int measuredWidth = isLayoutRtl ? getMeasuredWidth() : 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if ((childAt.getLayoutParams() instanceof Toolbar.LayoutParams) && (((Toolbar.LayoutParams) childAt.getLayoutParams()).gravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 8388611) {
                measuredWidth = isLayoutRtl ? Math.min(measuredWidth, childAt.getLeft()) : Math.max(measuredWidth, childAt.getRight());
            }
        }
        return measuredWidth - ((isLayoutRtl ? actionMenuView.getRight() : actionMenuView.getLeft()) + (isLayoutRtl ? this.rightInset : -this.leftInset));
    }

    public ColorStateList getBackgroundTint() {
        return this.materialShapeDrawable.getTintList();
    }

    public Behavior getBehavior() {
        if (this.behavior == null) {
            this.behavior = new Behavior();
        }
        return this.behavior;
    }

    public float getCradleVerticalOffset() {
        return getTopEdgeTreatment().getCradleVerticalOffset();
    }

    public int getFabAlignmentMode() {
        return this.fabAlignmentMode;
    }

    public int getFabAnimationMode() {
        return this.fabAnimationMode;
    }

    public float getFabCradleMargin() {
        return getTopEdgeTreatment().getFabCradleMargin();
    }

    public float getFabCradleRoundedCornerRadius() {
        return getTopEdgeTreatment().getFabCradleRoundedCornerRadius();
    }

    public boolean getHideOnScroll() {
        return this.hideOnScroll;
    }

    public boolean isScrolledDown() {
        return getBehavior().isScrolledDown();
    }

    public boolean isScrolledUp() {
        return getBehavior().isScrolledUp();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.setParentAbsoluteElevation(this, this.materialShapeDrawable);
        if (getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).setClipChildren(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            cancelAnimations();
            setCutoutState();
        }
        setActionMenuViewPosition();
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.fabAlignmentMode = savedState.fabAlignmentMode;
        this.fabAttached = savedState.fabAttached;
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.fabAlignmentMode = this.fabAlignmentMode;
        savedState.fabAttached = this.fabAttached;
        return savedState;
    }

    public void performHide() {
        performHide(true);
    }

    public void performHide(boolean animate) {
        getBehavior().slideDown(this, animate);
    }

    public void performShow() {
        performShow(true);
    }

    public void performShow(boolean animate) {
        getBehavior().slideUp(this, animate);
    }

    /* access modifiers changed from: package-private */
    public void removeAnimationListener(AnimationListener listener) {
        ArrayList<AnimationListener> arrayList = this.animationListeners;
        if (arrayList != null) {
            arrayList.remove(listener);
        }
    }

    public void replaceMenu(int newMenu) {
        if (newMenu != 0) {
            this.pendingMenuResId = 0;
            getMenu().clear();
            inflateMenu(newMenu);
        }
    }

    public void setBackgroundTint(ColorStateList backgroundTint) {
        DrawableCompat.setTintList(this.materialShapeDrawable, backgroundTint);
    }

    public void setCradleVerticalOffset(float verticalOffset) {
        if (verticalOffset != getCradleVerticalOffset()) {
            getTopEdgeTreatment().setCradleVerticalOffset(verticalOffset);
            this.materialShapeDrawable.invalidateSelf();
            setCutoutState();
        }
    }

    public void setElevation(float elevation) {
        this.materialShapeDrawable.setElevation(elevation);
        getBehavior().setAdditionalHiddenOffsetY(this, this.materialShapeDrawable.getShadowRadius() - this.materialShapeDrawable.getShadowOffsetY());
    }

    public void setFabAlignmentMode(int fabAlignmentMode2) {
        setFabAlignmentModeAndReplaceMenu(fabAlignmentMode2, 0);
    }

    public void setFabAlignmentModeAndReplaceMenu(int fabAlignmentMode2, int newMenu) {
        this.pendingMenuResId = newMenu;
        this.menuAnimatingWithFabAlignmentMode = true;
        maybeAnimateMenuView(fabAlignmentMode2, this.fabAttached);
        maybeAnimateModeChange(fabAlignmentMode2);
        this.fabAlignmentMode = fabAlignmentMode2;
    }

    public void setFabAnimationMode(int fabAnimationMode2) {
        this.fabAnimationMode = fabAnimationMode2;
    }

    /* access modifiers changed from: package-private */
    public void setFabCornerSize(float radius) {
        if (radius != getTopEdgeTreatment().getFabCornerRadius()) {
            getTopEdgeTreatment().setFabCornerSize(radius);
            this.materialShapeDrawable.invalidateSelf();
        }
    }

    public void setFabCradleMargin(float cradleMargin) {
        if (cradleMargin != getFabCradleMargin()) {
            getTopEdgeTreatment().setFabCradleMargin(cradleMargin);
            this.materialShapeDrawable.invalidateSelf();
        }
    }

    public void setFabCradleRoundedCornerRadius(float roundedCornerRadius) {
        if (roundedCornerRadius != getFabCradleRoundedCornerRadius()) {
            getTopEdgeTreatment().setFabCradleRoundedCornerRadius(roundedCornerRadius);
            this.materialShapeDrawable.invalidateSelf();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean setFabDiameter(int diameter) {
        if (((float) diameter) == getTopEdgeTreatment().getFabDiameter()) {
            return false;
        }
        getTopEdgeTreatment().setFabDiameter((float) diameter);
        this.materialShapeDrawable.invalidateSelf();
        return true;
    }

    public void setHideOnScroll(boolean hide) {
        this.hideOnScroll = hide;
    }

    public void setNavigationIcon(Drawable drawable) {
        super.setNavigationIcon(maybeTintNavigationIcon(drawable));
    }

    public void setNavigationIconTint(int navigationIconTint2) {
        this.navigationIconTint = Integer.valueOf(navigationIconTint2);
        Drawable navigationIcon = getNavigationIcon();
        if (navigationIcon != null) {
            setNavigationIcon(navigationIcon);
        }
    }

    public void setSubtitle(CharSequence subtitle) {
    }

    public void setTitle(CharSequence title) {
    }
}

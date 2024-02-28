package com.google.android.material.snackbar;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.SnackbarManager;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import mt.Log1F380D;

public abstract class BaseTransientBottomBar<B extends BaseTransientBottomBar<B>> {
    static final int ANIMATION_DURATION = 250;
    static final int ANIMATION_FADE_DURATION = 180;
    private static final int ANIMATION_FADE_IN_DURATION = 150;
    private static final int ANIMATION_FADE_OUT_DURATION = 75;
    public static final int ANIMATION_MODE_FADE = 1;
    public static final int ANIMATION_MODE_SLIDE = 0;
    private static final float ANIMATION_SCALE_FROM_VALUE = 0.8f;
    public static final int LENGTH_INDEFINITE = -2;
    public static final int LENGTH_LONG = 0;
    public static final int LENGTH_SHORT = -1;
    static final int MSG_DISMISS = 1;
    static final int MSG_SHOW = 0;
    private static final int[] SNACKBAR_STYLE_ATTR = {R.attr.snackbarStyle};
    /* access modifiers changed from: private */
    public static final String TAG = BaseTransientBottomBar.class.getSimpleName();
    /* access modifiers changed from: private */
    public static final boolean USE_OFFSET_API = (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT <= 19);
    static final Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    ((BaseTransientBottomBar) message.obj).showView();
                    return true;
                case 1:
                    ((BaseTransientBottomBar) message.obj).hideView(message.arg1);
                    return true;
                default:
                    return false;
            }
        }
    });
    private final AccessibilityManager accessibilityManager;
    private Anchor anchor;
    /* access modifiers changed from: private */
    public boolean anchorViewLayoutListenerEnabled;
    private Behavior behavior;
    private final Runnable bottomMarginGestureInsetRunnable;
    private List<BaseCallback<B>> callbacks;
    /* access modifiers changed from: private */
    public final ContentViewCallback contentViewCallback;
    /* access modifiers changed from: private */
    public final Context context;
    private int duration;
    private int extraBottomMarginAnchorView;
    /* access modifiers changed from: private */
    public int extraBottomMarginGestureInset;
    /* access modifiers changed from: private */
    public int extraBottomMarginWindowInset;
    /* access modifiers changed from: private */
    public int extraLeftMarginWindowInset;
    /* access modifiers changed from: private */
    public int extraRightMarginWindowInset;
    private boolean gestureInsetBottomIgnored;
    SnackbarManager.Callback managerCallback;
    private boolean pendingShowingView;
    private final ViewGroup targetParent;
    protected final SnackbarBaseLayout view;

    static class Anchor implements View.OnAttachStateChangeListener, ViewTreeObserver.OnGlobalLayoutListener {
        private final WeakReference<View> anchorView;
        private final WeakReference<BaseTransientBottomBar> transientBottomBar;

        private Anchor(BaseTransientBottomBar transientBottomBar2, View anchorView2) {
            this.transientBottomBar = new WeakReference<>(transientBottomBar2);
            this.anchorView = new WeakReference<>(anchorView2);
        }

        static Anchor anchor(BaseTransientBottomBar transientBottomBar2, View anchorView2) {
            Anchor anchor = new Anchor(transientBottomBar2, anchorView2);
            if (ViewCompat.isAttachedToWindow(anchorView2)) {
                ViewUtils.addOnGlobalLayoutListener(anchorView2, anchor);
            }
            anchorView2.addOnAttachStateChangeListener(anchor);
            return anchor;
        }

        private boolean unanchorIfNoTransientBottomBar() {
            if (this.transientBottomBar.get() != null) {
                return false;
            }
            unanchor();
            return true;
        }

        /* access modifiers changed from: package-private */
        public View getAnchorView() {
            return (View) this.anchorView.get();
        }

        public void onGlobalLayout() {
            if (!unanchorIfNoTransientBottomBar() && ((BaseTransientBottomBar) this.transientBottomBar.get()).anchorViewLayoutListenerEnabled) {
                ((BaseTransientBottomBar) this.transientBottomBar.get()).recalculateAndUpdateMargins();
            }
        }

        public void onViewAttachedToWindow(View anchorView2) {
            if (!unanchorIfNoTransientBottomBar()) {
                ViewUtils.addOnGlobalLayoutListener(anchorView2, this);
            }
        }

        public void onViewDetachedFromWindow(View anchorView2) {
            if (!unanchorIfNoTransientBottomBar()) {
                ViewUtils.removeOnGlobalLayoutListener(anchorView2, (ViewTreeObserver.OnGlobalLayoutListener) this);
            }
        }

        /* access modifiers changed from: package-private */
        public void unanchor() {
            if (this.anchorView.get() != null) {
                ((View) this.anchorView.get()).removeOnAttachStateChangeListener(this);
                ViewUtils.removeOnGlobalLayoutListener((View) this.anchorView.get(), (ViewTreeObserver.OnGlobalLayoutListener) this);
            }
            this.anchorView.clear();
            this.transientBottomBar.clear();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationMode {
    }

    public static abstract class BaseCallback<B> {
        public static final int DISMISS_EVENT_ACTION = 1;
        public static final int DISMISS_EVENT_CONSECUTIVE = 4;
        public static final int DISMISS_EVENT_MANUAL = 3;
        public static final int DISMISS_EVENT_SWIPE = 0;
        public static final int DISMISS_EVENT_TIMEOUT = 2;

        @Retention(RetentionPolicy.SOURCE)
        public @interface DismissEvent {
        }

        public void onDismissed(B b, int event) {
        }

        public void onShown(B b) {
        }
    }

    public static class Behavior extends SwipeDismissBehavior<View> {
        private final BehaviorDelegate delegate = new BehaviorDelegate(this);

        /* access modifiers changed from: private */
        public void setBaseTransientBottomBar(BaseTransientBottomBar<?> baseTransientBottomBar) {
            this.delegate.setBaseTransientBottomBar(baseTransientBottomBar);
        }

        public boolean canSwipeDismissView(View child) {
            return this.delegate.canSwipeDismissView(child);
        }

        public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent event) {
            this.delegate.onInterceptTouchEvent(parent, child, event);
            return super.onInterceptTouchEvent(parent, child, event);
        }
    }

    public static class BehaviorDelegate {
        private SnackbarManager.Callback managerCallback;

        public BehaviorDelegate(SwipeDismissBehavior<?> swipeDismissBehavior) {
            swipeDismissBehavior.setStartAlphaSwipeDistance(0.1f);
            swipeDismissBehavior.setEndAlphaSwipeDistance(0.6f);
            swipeDismissBehavior.setSwipeDirection(0);
        }

        public boolean canSwipeDismissView(View child) {
            return child instanceof SnackbarBaseLayout;
        }

        public void onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent event) {
            switch (event.getActionMasked()) {
                case 0:
                    if (parent.isPointInChildBounds(child, (int) event.getX(), (int) event.getY())) {
                        SnackbarManager.getInstance().pauseTimeout(this.managerCallback);
                        return;
                    }
                    return;
                case 1:
                case 3:
                    SnackbarManager.getInstance().restoreTimeoutIfPaused(this.managerCallback);
                    return;
                default:
                    return;
            }
        }

        public void setBaseTransientBottomBar(BaseTransientBottomBar<?> baseTransientBottomBar) {
            this.managerCallback = baseTransientBottomBar.managerCallback;
        }
    }

    @Deprecated
    public interface ContentViewCallback extends ContentViewCallback {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    protected static class SnackbarBaseLayout extends FrameLayout {
        private static final View.OnTouchListener consumeAllTouchListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        };
        private final float actionTextColorAlpha;
        private boolean addingToTargetParent;
        private int animationMode;
        private final float backgroundOverlayColorAlpha;
        private ColorStateList backgroundTint;
        private PorterDuff.Mode backgroundTintMode;
        private BaseTransientBottomBar<?> baseTransientBottomBar;
        private final int maxInlineActionWidth;
        private final int maxWidth;
        /* access modifiers changed from: private */
        public Rect originalMargins;

        protected SnackbarBaseLayout(Context context) {
            this(context, (AttributeSet) null);
        }

        protected SnackbarBaseLayout(Context context, AttributeSet attrs) {
            super(MaterialThemeOverlay.wrap(context, attrs, 0, 0), attrs);
            Context context2 = getContext();
            TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attrs, R.styleable.SnackbarLayout);
            if (obtainStyledAttributes.hasValue(R.styleable.SnackbarLayout_elevation)) {
                ViewCompat.setElevation(this, (float) obtainStyledAttributes.getDimensionPixelSize(R.styleable.SnackbarLayout_elevation, 0));
            }
            this.animationMode = obtainStyledAttributes.getInt(R.styleable.SnackbarLayout_animationMode, 0);
            this.backgroundOverlayColorAlpha = obtainStyledAttributes.getFloat(R.styleable.SnackbarLayout_backgroundOverlayColorAlpha, 1.0f);
            setBackgroundTintList(MaterialResources.getColorStateList(context2, obtainStyledAttributes, R.styleable.SnackbarLayout_backgroundTint));
            setBackgroundTintMode(ViewUtils.parseTintMode(obtainStyledAttributes.getInt(R.styleable.SnackbarLayout_backgroundTintMode, -1), PorterDuff.Mode.SRC_IN));
            this.actionTextColorAlpha = obtainStyledAttributes.getFloat(R.styleable.SnackbarLayout_actionTextColorAlpha, 1.0f);
            this.maxWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1);
            this.maxInlineActionWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SnackbarLayout_maxActionInlineWidth, -1);
            obtainStyledAttributes.recycle();
            setOnTouchListener(consumeAllTouchListener);
            setFocusable(true);
            if (getBackground() == null) {
                ViewCompat.setBackground(this, createThemedBackground());
            }
        }

        private Drawable createThemedBackground() {
            float dimension = getResources().getDimension(R.dimen.mtrl_snackbar_background_corner_radius);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(0);
            gradientDrawable.setCornerRadius(dimension);
            gradientDrawable.setColor(MaterialColors.layer(this, R.attr.colorSurface, R.attr.colorOnSurface, getBackgroundOverlayColorAlpha()));
            if (this.backgroundTint == null) {
                return DrawableCompat.wrap(gradientDrawable);
            }
            Drawable wrap = DrawableCompat.wrap(gradientDrawable);
            DrawableCompat.setTintList(wrap, this.backgroundTint);
            return wrap;
        }

        /* access modifiers changed from: private */
        public void setBaseTransientBottomBar(BaseTransientBottomBar<?> baseTransientBottomBar2) {
            this.baseTransientBottomBar = baseTransientBottomBar2;
        }

        private void updateOriginalMargins(ViewGroup.MarginLayoutParams params) {
            this.originalMargins = new Rect(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin);
        }

        /* access modifiers changed from: package-private */
        public void addToTargetParent(ViewGroup targetParent) {
            this.addingToTargetParent = true;
            targetParent.addView(this);
            this.addingToTargetParent = false;
        }

        /* access modifiers changed from: package-private */
        public float getActionTextColorAlpha() {
            return this.actionTextColorAlpha;
        }

        /* access modifiers changed from: package-private */
        public int getAnimationMode() {
            return this.animationMode;
        }

        /* access modifiers changed from: package-private */
        public float getBackgroundOverlayColorAlpha() {
            return this.backgroundOverlayColorAlpha;
        }

        /* access modifiers changed from: package-private */
        public int getMaxInlineActionWidth() {
            return this.maxInlineActionWidth;
        }

        /* access modifiers changed from: package-private */
        public int getMaxWidth() {
            return this.maxWidth;
        }

        /* access modifiers changed from: protected */
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            BaseTransientBottomBar<?> baseTransientBottomBar2 = this.baseTransientBottomBar;
            if (baseTransientBottomBar2 != null) {
                baseTransientBottomBar2.onAttachedToWindow();
            }
            ViewCompat.requestApplyInsets(this);
        }

        /* access modifiers changed from: protected */
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            BaseTransientBottomBar<?> baseTransientBottomBar2 = this.baseTransientBottomBar;
            if (baseTransientBottomBar2 != null) {
                baseTransientBottomBar2.onDetachedFromWindow();
            }
        }

        /* access modifiers changed from: protected */
        public void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            BaseTransientBottomBar<?> baseTransientBottomBar2 = this.baseTransientBottomBar;
            if (baseTransientBottomBar2 != null) {
                baseTransientBottomBar2.onLayoutChange();
            }
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int i;
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (this.maxWidth > 0 && getMeasuredWidth() > (i = this.maxWidth)) {
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(i, BasicMeasure.EXACTLY), heightMeasureSpec);
            }
        }

        /* access modifiers changed from: package-private */
        public void setAnimationMode(int animationMode2) {
            this.animationMode = animationMode2;
        }

        public void setBackground(Drawable drawable) {
            setBackgroundDrawable(drawable);
        }

        public void setBackgroundDrawable(Drawable drawable) {
            if (!(drawable == null || this.backgroundTint == null)) {
                drawable = DrawableCompat.wrap(drawable.mutate());
                DrawableCompat.setTintList(drawable, this.backgroundTint);
                DrawableCompat.setTintMode(drawable, this.backgroundTintMode);
            }
            super.setBackgroundDrawable(drawable);
        }

        public void setBackgroundTintList(ColorStateList backgroundTint2) {
            this.backgroundTint = backgroundTint2;
            if (getBackground() != null) {
                Drawable wrap = DrawableCompat.wrap(getBackground().mutate());
                DrawableCompat.setTintList(wrap, backgroundTint2);
                DrawableCompat.setTintMode(wrap, this.backgroundTintMode);
                if (wrap != getBackground()) {
                    super.setBackgroundDrawable(wrap);
                }
            }
        }

        public void setBackgroundTintMode(PorterDuff.Mode backgroundTintMode2) {
            this.backgroundTintMode = backgroundTintMode2;
            if (getBackground() != null) {
                Drawable wrap = DrawableCompat.wrap(getBackground().mutate());
                DrawableCompat.setTintMode(wrap, backgroundTintMode2);
                if (wrap != getBackground()) {
                    super.setBackgroundDrawable(wrap);
                }
            }
        }

        public void setLayoutParams(ViewGroup.LayoutParams params) {
            super.setLayoutParams(params);
            if (!this.addingToTargetParent && (params instanceof ViewGroup.MarginLayoutParams)) {
                updateOriginalMargins((ViewGroup.MarginLayoutParams) params);
                BaseTransientBottomBar<?> baseTransientBottomBar2 = this.baseTransientBottomBar;
                if (baseTransientBottomBar2 != null) {
                    baseTransientBottomBar2.updateMargins();
                }
            }
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            setOnTouchListener(onClickListener != null ? null : consumeAllTouchListener);
            super.setOnClickListener(onClickListener);
        }
    }

    protected BaseTransientBottomBar(Context context2, ViewGroup parent, View content, ContentViewCallback contentViewCallback2) {
        this.anchorViewLayoutListenerEnabled = false;
        this.bottomMarginGestureInsetRunnable = new Runnable() {
            public void run() {
                int access$100;
                if (BaseTransientBottomBar.this.view != null && BaseTransientBottomBar.this.context != null && (access$100 = (BaseTransientBottomBar.this.getScreenHeight() - BaseTransientBottomBar.this.getViewAbsoluteBottom()) + ((int) BaseTransientBottomBar.this.view.getTranslationY())) < BaseTransientBottomBar.this.extraBottomMarginGestureInset) {
                    ViewGroup.LayoutParams layoutParams = BaseTransientBottomBar.this.view.getLayoutParams();
                    if (!(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
                        String access$400 = BaseTransientBottomBar.TAG;
                        Log1F380D.a((Object) access$400);
                        Log.w(access$400, "Unable to apply gesture inset because layout params are not MarginLayoutParams");
                        return;
                    }
                    ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin += BaseTransientBottomBar.this.extraBottomMarginGestureInset - access$100;
                    BaseTransientBottomBar.this.view.requestLayout();
                }
            }
        };
        this.managerCallback = new SnackbarManager.Callback() {
            public void dismiss(int event) {
                BaseTransientBottomBar.handler.sendMessage(BaseTransientBottomBar.handler.obtainMessage(1, event, 0, BaseTransientBottomBar.this));
            }

            public void show() {
                BaseTransientBottomBar.handler.sendMessage(BaseTransientBottomBar.handler.obtainMessage(0, BaseTransientBottomBar.this));
            }
        };
        if (parent == null) {
            throw new IllegalArgumentException("Transient bottom bar must have non-null parent");
        } else if (content == null) {
            throw new IllegalArgumentException("Transient bottom bar must have non-null content");
        } else if (contentViewCallback2 != null) {
            this.targetParent = parent;
            this.contentViewCallback = contentViewCallback2;
            this.context = context2;
            ThemeEnforcement.checkAppCompatTheme(context2);
            SnackbarBaseLayout snackbarBaseLayout = (SnackbarBaseLayout) LayoutInflater.from(context2).inflate(getSnackbarBaseLayoutResId(), parent, false);
            this.view = snackbarBaseLayout;
            snackbarBaseLayout.setBaseTransientBottomBar(this);
            if (content instanceof SnackbarContentLayout) {
                ((SnackbarContentLayout) content).updateActionTextColorAlphaIfNeeded(snackbarBaseLayout.getActionTextColorAlpha());
                ((SnackbarContentLayout) content).setMaxInlineActionWidth(snackbarBaseLayout.getMaxInlineActionWidth());
            }
            snackbarBaseLayout.addView(content);
            ViewCompat.setAccessibilityLiveRegion(snackbarBaseLayout, 1);
            ViewCompat.setImportantForAccessibility(snackbarBaseLayout, 1);
            ViewCompat.setFitsSystemWindows(snackbarBaseLayout, true);
            ViewCompat.setOnApplyWindowInsetsListener(snackbarBaseLayout, new OnApplyWindowInsetsListener() {
                public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                    int unused = BaseTransientBottomBar.this.extraBottomMarginWindowInset = insets.getSystemWindowInsetBottom();
                    int unused2 = BaseTransientBottomBar.this.extraLeftMarginWindowInset = insets.getSystemWindowInsetLeft();
                    int unused3 = BaseTransientBottomBar.this.extraRightMarginWindowInset = insets.getSystemWindowInsetRight();
                    BaseTransientBottomBar.this.updateMargins();
                    return insets;
                }
            });
            ViewCompat.setAccessibilityDelegate(snackbarBaseLayout, new AccessibilityDelegateCompat() {
                public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
                    super.onInitializeAccessibilityNodeInfo(host, info);
                    info.addAction(1048576);
                    info.setDismissable(true);
                }

                public boolean performAccessibilityAction(View host, int action, Bundle args) {
                    if (action != 1048576) {
                        return super.performAccessibilityAction(host, action, args);
                    }
                    BaseTransientBottomBar.this.dismiss();
                    return true;
                }
            });
            this.accessibilityManager = (AccessibilityManager) context2.getSystemService("accessibility");
        } else {
            throw new IllegalArgumentException("Transient bottom bar must have non-null callback");
        }
    }

    protected BaseTransientBottomBar(ViewGroup parent, View content, ContentViewCallback contentViewCallback2) {
        this(parent.getContext(), parent, content, contentViewCallback2);
    }

    private void animateViewOut(int event) {
        if (this.view.getAnimationMode() == 1) {
            startFadeOutAnimation(event);
        } else {
            startSlideOutAnimation(event);
        }
    }

    private int calculateBottomMarginForAnchorView() {
        if (getAnchorView() == null) {
            return 0;
        }
        int[] iArr = new int[2];
        getAnchorView().getLocationOnScreen(iArr);
        int i = iArr[1];
        int[] iArr2 = new int[2];
        this.targetParent.getLocationOnScreen(iArr2);
        return (iArr2[1] + this.targetParent.getHeight()) - i;
    }

    private ValueAnimator getAlphaAnimator(float... alphaValues) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(alphaValues);
        ofFloat.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                BaseTransientBottomBar.this.view.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        return ofFloat;
    }

    private ValueAnimator getScaleAnimator(float... scaleValues) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(scaleValues);
        ofFloat.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                BaseTransientBottomBar.this.view.setScaleX(floatValue);
                BaseTransientBottomBar.this.view.setScaleY(floatValue);
            }
        });
        return ofFloat;
    }

    /* access modifiers changed from: private */
    public int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) this.context.getSystemService("window")).getDefaultDisplay().getRealMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private int getTranslationYBottom() {
        int height = this.view.getHeight();
        ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
        return layoutParams instanceof ViewGroup.MarginLayoutParams ? height + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin : height;
    }

    /* access modifiers changed from: private */
    public int getViewAbsoluteBottom() {
        int[] iArr = new int[2];
        this.view.getLocationOnScreen(iArr);
        return iArr[1] + this.view.getHeight();
    }

    private boolean isSwipeDismissable() {
        ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
        return (layoutParams instanceof CoordinatorLayout.LayoutParams) && (((CoordinatorLayout.LayoutParams) layoutParams).getBehavior() instanceof SwipeDismissBehavior);
    }

    /* access modifiers changed from: private */
    public void recalculateAndUpdateMargins() {
        this.extraBottomMarginAnchorView = calculateBottomMarginForAnchorView();
        updateMargins();
    }

    private void setUpBehavior(CoordinatorLayout.LayoutParams lp) {
        CoordinatorLayout.LayoutParams layoutParams = lp;
        SwipeDismissBehavior swipeDismissBehavior = this.behavior;
        if (swipeDismissBehavior == null) {
            swipeDismissBehavior = getNewBehavior();
        }
        if (swipeDismissBehavior instanceof Behavior) {
            ((Behavior) swipeDismissBehavior).setBaseTransientBottomBar(this);
        }
        swipeDismissBehavior.setListener(new SwipeDismissBehavior.OnDismissListener() {
            public void onDismiss(View view) {
                if (view.getParent() != null) {
                    view.setVisibility(8);
                }
                BaseTransientBottomBar.this.dispatchDismiss(0);
            }

            public void onDragStateChanged(int state) {
                switch (state) {
                    case 0:
                        SnackbarManager.getInstance().restoreTimeoutIfPaused(BaseTransientBottomBar.this.managerCallback);
                        return;
                    case 1:
                    case 2:
                        SnackbarManager.getInstance().pauseTimeout(BaseTransientBottomBar.this.managerCallback);
                        return;
                    default:
                        return;
                }
            }
        });
        layoutParams.setBehavior(swipeDismissBehavior);
        if (getAnchorView() == null) {
            layoutParams.insetEdge = 80;
        }
    }

    private boolean shouldUpdateGestureInset() {
        return this.extraBottomMarginGestureInset > 0 && !this.gestureInsetBottomIgnored && isSwipeDismissable();
    }

    private void showViewImpl() {
        if (shouldAnimate()) {
            animateViewIn();
            return;
        }
        if (this.view.getParent() != null) {
            this.view.setVisibility(0);
        }
        onViewShown();
    }

    /* access modifiers changed from: private */
    public void startFadeInAnimation() {
        ValueAnimator alphaAnimator = getAlphaAnimator(0.0f, 1.0f);
        ValueAnimator scaleAnimator = getScaleAnimator(ANIMATION_SCALE_FROM_VALUE, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{alphaAnimator, scaleAnimator});
        animatorSet.setDuration(150);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                BaseTransientBottomBar.this.onViewShown();
            }
        });
        animatorSet.start();
    }

    private void startFadeOutAnimation(final int event) {
        ValueAnimator alphaAnimator = getAlphaAnimator(1.0f, 0.0f);
        alphaAnimator.setDuration(75);
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                BaseTransientBottomBar.this.onViewHidden(event);
            }
        });
        alphaAnimator.start();
    }

    /* access modifiers changed from: private */
    public void startSlideInAnimation() {
        int translationYBottom = getTranslationYBottom();
        if (USE_OFFSET_API) {
            ViewCompat.offsetTopAndBottom(this.view, translationYBottom);
        } else {
            this.view.setTranslationY((float) translationYBottom);
        }
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(new int[]{translationYBottom, 0});
        valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        valueAnimator.setDuration(250);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                BaseTransientBottomBar.this.onViewShown();
            }

            public void onAnimationStart(Animator animator) {
                BaseTransientBottomBar.this.contentViewCallback.animateContentIn(70, BaseTransientBottomBar.ANIMATION_FADE_DURATION);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(translationYBottom) {
            private int previousAnimatedIntValue;
            final /* synthetic */ int val$translationYBottom;

            {
                this.val$translationYBottom = r2;
                this.previousAnimatedIntValue = r2;
            }

            public void onAnimationUpdate(ValueAnimator animator) {
                int intValue = ((Integer) animator.getAnimatedValue()).intValue();
                if (BaseTransientBottomBar.USE_OFFSET_API) {
                    ViewCompat.offsetTopAndBottom(BaseTransientBottomBar.this.view, intValue - this.previousAnimatedIntValue);
                } else {
                    BaseTransientBottomBar.this.view.setTranslationY((float) intValue);
                }
                this.previousAnimatedIntValue = intValue;
            }
        });
        valueAnimator.start();
    }

    private void startSlideOutAnimation(final int event) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(new int[]{0, getTranslationYBottom()});
        valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        valueAnimator.setDuration(250);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                BaseTransientBottomBar.this.onViewHidden(event);
            }

            public void onAnimationStart(Animator animator) {
                BaseTransientBottomBar.this.contentViewCallback.animateContentOut(0, BaseTransientBottomBar.ANIMATION_FADE_DURATION);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private int previousAnimatedIntValue = 0;

            public void onAnimationUpdate(ValueAnimator animator) {
                int intValue = ((Integer) animator.getAnimatedValue()).intValue();
                if (BaseTransientBottomBar.USE_OFFSET_API) {
                    ViewCompat.offsetTopAndBottom(BaseTransientBottomBar.this.view, intValue - this.previousAnimatedIntValue);
                } else {
                    BaseTransientBottomBar.this.view.setTranslationY((float) intValue);
                }
                this.previousAnimatedIntValue = intValue;
            }
        });
        valueAnimator.start();
    }

    /* access modifiers changed from: private */
    public void updateMargins() {
        ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
        if (!(layoutParams instanceof ViewGroup.MarginLayoutParams) || this.view.originalMargins == null) {
            Log.w(TAG, "Unable to update margins because layout params are not MarginLayoutParams");
        } else if (this.view.getParent() != null) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.bottomMargin = this.view.originalMargins.bottom + (getAnchorView() != null ? this.extraBottomMarginAnchorView : this.extraBottomMarginWindowInset);
            marginLayoutParams.leftMargin = this.view.originalMargins.left + this.extraLeftMarginWindowInset;
            marginLayoutParams.rightMargin = this.view.originalMargins.right + this.extraRightMarginWindowInset;
            marginLayoutParams.topMargin = this.view.originalMargins.top;
            this.view.requestLayout();
            if (Build.VERSION.SDK_INT >= 29 && shouldUpdateGestureInset()) {
                this.view.removeCallbacks(this.bottomMarginGestureInsetRunnable);
                this.view.post(this.bottomMarginGestureInsetRunnable);
            }
        }
    }

    public B addCallback(BaseCallback<B> baseCallback) {
        if (baseCallback == null) {
            return this;
        }
        if (this.callbacks == null) {
            this.callbacks = new ArrayList();
        }
        this.callbacks.add(baseCallback);
        return this;
    }

    /* access modifiers changed from: package-private */
    public void animateViewIn() {
        this.view.post(new Runnable() {
            public void run() {
                if (BaseTransientBottomBar.this.view != null) {
                    if (BaseTransientBottomBar.this.view.getParent() != null) {
                        BaseTransientBottomBar.this.view.setVisibility(0);
                    }
                    if (BaseTransientBottomBar.this.view.getAnimationMode() == 1) {
                        BaseTransientBottomBar.this.startFadeInAnimation();
                    } else {
                        BaseTransientBottomBar.this.startSlideInAnimation();
                    }
                }
            }
        });
    }

    public void dismiss() {
        dispatchDismiss(3);
    }

    /* access modifiers changed from: protected */
    public void dispatchDismiss(int event) {
        SnackbarManager.getInstance().dismiss(this.managerCallback, event);
    }

    public View getAnchorView() {
        Anchor anchor2 = this.anchor;
        if (anchor2 == null) {
            return null;
        }
        return anchor2.getAnchorView();
    }

    public int getAnimationMode() {
        return this.view.getAnimationMode();
    }

    public Behavior getBehavior() {
        return this.behavior;
    }

    public Context getContext() {
        return this.context;
    }

    public int getDuration() {
        return this.duration;
    }

    /* access modifiers changed from: protected */
    public SwipeDismissBehavior<? extends View> getNewBehavior() {
        return new Behavior();
    }

    /* access modifiers changed from: protected */
    public int getSnackbarBaseLayoutResId() {
        return hasSnackbarStyleAttr() ? R.layout.mtrl_layout_snackbar : R.layout.design_layout_snackbar;
    }

    public View getView() {
        return this.view;
    }

    /* access modifiers changed from: protected */
    public boolean hasSnackbarStyleAttr() {
        TypedArray obtainStyledAttributes = this.context.obtainStyledAttributes(SNACKBAR_STYLE_ATTR);
        int resourceId = obtainStyledAttributes.getResourceId(0, -1);
        obtainStyledAttributes.recycle();
        return resourceId != -1;
    }

    /* access modifiers changed from: package-private */
    public final void hideView(int event) {
        if (!shouldAnimate() || this.view.getVisibility() != 0) {
            onViewHidden(event);
        } else {
            animateViewOut(event);
        }
    }

    public boolean isAnchorViewLayoutListenerEnabled() {
        return this.anchorViewLayoutListenerEnabled;
    }

    public boolean isGestureInsetBottomIgnored() {
        return this.gestureInsetBottomIgnored;
    }

    public boolean isShown() {
        return SnackbarManager.getInstance().isCurrent(this.managerCallback);
    }

    public boolean isShownOrQueued() {
        return SnackbarManager.getInstance().isCurrentOrNext(this.managerCallback);
    }

    /* access modifiers changed from: package-private */
    public void onAttachedToWindow() {
        WindowInsets rootWindowInsets;
        if (Build.VERSION.SDK_INT >= 29 && (rootWindowInsets = this.view.getRootWindowInsets()) != null) {
            this.extraBottomMarginGestureInset = rootWindowInsets.getMandatorySystemGestureInsets().bottom;
            updateMargins();
        }
    }

    /* access modifiers changed from: package-private */
    public void onDetachedFromWindow() {
        if (isShownOrQueued()) {
            handler.post(new Runnable() {
                public void run() {
                    BaseTransientBottomBar.this.onViewHidden(3);
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    public void onLayoutChange() {
        if (this.pendingShowingView) {
            showViewImpl();
            this.pendingShowingView = false;
        }
    }

    /* access modifiers changed from: package-private */
    public void onViewHidden(int event) {
        SnackbarManager.getInstance().onDismissed(this.managerCallback);
        List<BaseCallback<B>> list = this.callbacks;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.callbacks.get(size).onDismissed(this, event);
            }
        }
        ViewParent parent = this.view.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(this.view);
        }
    }

    /* access modifiers changed from: package-private */
    public void onViewShown() {
        SnackbarManager.getInstance().onShown(this.managerCallback);
        List<BaseCallback<B>> list = this.callbacks;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.callbacks.get(size).onShown(this);
            }
        }
    }

    public B removeCallback(BaseCallback<B> baseCallback) {
        List<BaseCallback<B>> list;
        if (baseCallback == null || (list = this.callbacks) == null) {
            return this;
        }
        list.remove(baseCallback);
        return this;
    }

    public B setAnchorView(int anchorViewId) {
        View findViewById = this.targetParent.findViewById(anchorViewId);
        if (findViewById != null) {
            return setAnchorView(findViewById);
        }
        throw new IllegalArgumentException("Unable to find anchor view with id: " + anchorViewId);
    }

    public B setAnchorView(View anchorView) {
        Anchor anchor2 = this.anchor;
        if (anchor2 != null) {
            anchor2.unanchor();
        }
        this.anchor = anchorView == null ? null : Anchor.anchor(this, anchorView);
        return this;
    }

    public void setAnchorViewLayoutListenerEnabled(boolean anchorViewLayoutListenerEnabled2) {
        this.anchorViewLayoutListenerEnabled = anchorViewLayoutListenerEnabled2;
    }

    public B setAnimationMode(int animationMode) {
        this.view.setAnimationMode(animationMode);
        return this;
    }

    public B setBehavior(Behavior behavior2) {
        this.behavior = behavior2;
        return this;
    }

    public B setDuration(int duration2) {
        this.duration = duration2;
        return this;
    }

    public B setGestureInsetBottomIgnored(boolean gestureInsetBottomIgnored2) {
        this.gestureInsetBottomIgnored = gestureInsetBottomIgnored2;
        return this;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldAnimate() {
        AccessibilityManager accessibilityManager2 = this.accessibilityManager;
        if (accessibilityManager2 == null) {
            return true;
        }
        List<AccessibilityServiceInfo> enabledAccessibilityServiceList = accessibilityManager2.getEnabledAccessibilityServiceList(1);
        return enabledAccessibilityServiceList != null && enabledAccessibilityServiceList.isEmpty();
    }

    public void show() {
        SnackbarManager.getInstance().show(getDuration(), this.managerCallback);
    }

    /* access modifiers changed from: package-private */
    public final void showView() {
        if (this.view.getParent() == null) {
            ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
            if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
                setUpBehavior((CoordinatorLayout.LayoutParams) layoutParams);
            }
            this.view.addToTargetParent(this.targetParent);
            recalculateAndUpdateMargins();
            this.view.setVisibility(4);
        }
        if (ViewCompat.isLaidOut(this.view)) {
            showViewImpl();
        } else {
            this.pendingShowingView = true;
        }
    }
}

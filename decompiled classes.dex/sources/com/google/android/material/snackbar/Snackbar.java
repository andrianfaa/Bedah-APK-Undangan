package com.google.android.material.snackbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;
import com.google.android.material.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;

public class Snackbar extends BaseTransientBottomBar<Snackbar> {
    private static final int[] SNACKBAR_BUTTON_STYLE_ATTR = {R.attr.snackbarButtonStyle};
    private static final int[] SNACKBAR_CONTENT_STYLE_ATTRS = {R.attr.snackbarButtonStyle, R.attr.snackbarTextViewStyle};
    private final AccessibilityManager accessibilityManager;
    private BaseTransientBottomBar.BaseCallback<Snackbar> callback;
    private boolean hasAction;

    public static class Callback extends BaseTransientBottomBar.BaseCallback<Snackbar> {
        public static final int DISMISS_EVENT_ACTION = 1;
        public static final int DISMISS_EVENT_CONSECUTIVE = 4;
        public static final int DISMISS_EVENT_MANUAL = 3;
        public static final int DISMISS_EVENT_SWIPE = 0;
        public static final int DISMISS_EVENT_TIMEOUT = 2;

        public void onDismissed(Snackbar transientBottomBar, int event) {
        }

        public void onShown(Snackbar sb) {
        }
    }

    public static final class SnackbarLayout extends BaseTransientBottomBar.SnackbarBaseLayout {
        public SnackbarLayout(Context context) {
            super(context);
        }

        public SnackbarLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int childCount = getChildCount();
            int measuredWidth = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);
                if (childAt.getLayoutParams().width == -1) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth, BasicMeasure.EXACTLY), View.MeasureSpec.makeMeasureSpec(childAt.getMeasuredHeight(), BasicMeasure.EXACTLY));
                }
            }
        }

        public /* bridge */ /* synthetic */ void setBackground(Drawable drawable) {
            super.setBackground(drawable);
        }

        public /* bridge */ /* synthetic */ void setBackgroundDrawable(Drawable drawable) {
            super.setBackgroundDrawable(drawable);
        }

        public /* bridge */ /* synthetic */ void setBackgroundTintList(ColorStateList colorStateList) {
            super.setBackgroundTintList(colorStateList);
        }

        public /* bridge */ /* synthetic */ void setBackgroundTintMode(PorterDuff.Mode mode) {
            super.setBackgroundTintMode(mode);
        }

        public /* bridge */ /* synthetic */ void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
            super.setLayoutParams(layoutParams);
        }

        public /* bridge */ /* synthetic */ void setOnClickListener(View.OnClickListener onClickListener) {
            super.setOnClickListener(onClickListener);
        }
    }

    private Snackbar(Context context, ViewGroup parent, View content, ContentViewCallback contentViewCallback) {
        super(context, parent, content, contentViewCallback);
        this.accessibilityManager = (AccessibilityManager) parent.getContext().getSystemService("accessibility");
    }

    /* JADX WARNING: type inference failed for: r1v4, types: [android.view.ViewParent] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.view.ViewGroup findSuitableParent(android.view.View r3) {
        /*
            r0 = 0
        L_0x0001:
            boolean r1 = r3 instanceof androidx.coordinatorlayout.widget.CoordinatorLayout
            if (r1 == 0) goto L_0x0009
            r1 = r3
            android.view.ViewGroup r1 = (android.view.ViewGroup) r1
            return r1
        L_0x0009:
            boolean r1 = r3 instanceof android.widget.FrameLayout
            if (r1 == 0) goto L_0x001d
            int r1 = r3.getId()
            r2 = 16908290(0x1020002, float:2.3877235E-38)
            if (r1 != r2) goto L_0x001a
            r1 = r3
            android.view.ViewGroup r1 = (android.view.ViewGroup) r1
            return r1
        L_0x001a:
            r0 = r3
            android.view.ViewGroup r0 = (android.view.ViewGroup) r0
        L_0x001d:
            if (r3 == 0) goto L_0x002d
            android.view.ViewParent r1 = r3.getParent()
            boolean r2 = r1 instanceof android.view.View
            if (r2 == 0) goto L_0x002b
            r2 = r1
            android.view.View r2 = (android.view.View) r2
            goto L_0x002c
        L_0x002b:
            r2 = 0
        L_0x002c:
            r3 = r2
        L_0x002d:
            if (r3 != 0) goto L_0x0030
            return r0
        L_0x0030:
            goto L_0x0001
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.snackbar.Snackbar.findSuitableParent(android.view.View):android.view.ViewGroup");
    }

    private Button getActionView() {
        return getContentLayout().getActionView();
    }

    private SnackbarContentLayout getContentLayout() {
        return (SnackbarContentLayout) this.view.getChildAt(0);
    }

    private TextView getMessageView() {
        return getContentLayout().getMessageView();
    }

    @Deprecated
    protected static boolean hasSnackbarButtonStyleAttr(Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(SNACKBAR_BUTTON_STYLE_ATTR);
        int resourceId = obtainStyledAttributes.getResourceId(0, -1);
        obtainStyledAttributes.recycle();
        return resourceId != -1;
    }

    private static boolean hasSnackbarContentStyleAttrs(Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(SNACKBAR_CONTENT_STYLE_ATTRS);
        int resourceId = obtainStyledAttributes.getResourceId(0, -1);
        int resourceId2 = obtainStyledAttributes.getResourceId(1, -1);
        obtainStyledAttributes.recycle();
        return (resourceId == -1 || resourceId2 == -1) ? false : true;
    }

    public static Snackbar make(Context context, View view, CharSequence text, int duration) {
        return makeInternal(context, view, text, duration);
    }

    public static Snackbar make(View view, int resId, int duration) {
        return make(view, view.getResources().getText(resId), duration);
    }

    public static Snackbar make(View view, CharSequence text, int duration) {
        return makeInternal((Context) null, view, text, duration);
    }

    private static Snackbar makeInternal(Context context, View view, CharSequence text, int duration) {
        ViewGroup findSuitableParent = findSuitableParent(view);
        if (findSuitableParent != null) {
            if (context == null) {
                context = findSuitableParent.getContext();
            }
            SnackbarContentLayout snackbarContentLayout = (SnackbarContentLayout) LayoutInflater.from(context).inflate(hasSnackbarContentStyleAttrs(context) ? R.layout.mtrl_layout_snackbar_include : R.layout.design_layout_snackbar_include, findSuitableParent, false);
            Snackbar snackbar = new Snackbar(context, findSuitableParent, snackbarContentLayout, snackbarContentLayout);
            snackbar.setText(text);
            snackbar.setDuration(duration);
            return snackbar;
        }
        throw new IllegalArgumentException("No suitable parent found from the given view. Please provide a valid view.");
    }

    public void dismiss() {
        super.dismiss();
    }

    public int getDuration() {
        int duration = super.getDuration();
        if (duration == -2) {
            return -2;
        }
        if (Build.VERSION.SDK_INT >= 29) {
            return this.accessibilityManager.getRecommendedTimeoutMillis(duration, (this.hasAction ? 4 : 0) | 1 | 2);
        } else if (!this.hasAction || !this.accessibilityManager.isTouchExplorationEnabled()) {
            return duration;
        } else {
            return -2;
        }
    }

    public boolean isShown() {
        return super.isShown();
    }

    public Snackbar setAction(int resId, View.OnClickListener listener) {
        return setAction(getContext().getText(resId), listener);
    }

    public Snackbar setAction(CharSequence text, final View.OnClickListener listener) {
        Button actionView = getActionView();
        if (TextUtils.isEmpty(text) || listener == null) {
            actionView.setVisibility(8);
            actionView.setOnClickListener((View.OnClickListener) null);
            this.hasAction = false;
        } else {
            this.hasAction = true;
            actionView.setVisibility(0);
            actionView.setText(text);
            actionView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    listener.onClick(view);
                    Snackbar.this.dispatchDismiss(1);
                }
            });
        }
        return this;
    }

    public Snackbar setActionTextColor(int color) {
        getActionView().setTextColor(color);
        return this;
    }

    public Snackbar setActionTextColor(ColorStateList colors) {
        getActionView().setTextColor(colors);
        return this;
    }

    public Snackbar setBackgroundTint(int color) {
        return setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public Snackbar setBackgroundTintList(ColorStateList colorStateList) {
        this.view.setBackgroundTintList(colorStateList);
        return this;
    }

    public Snackbar setBackgroundTintMode(PorterDuff.Mode mode) {
        this.view.setBackgroundTintMode(mode);
        return this;
    }

    @Deprecated
    public Snackbar setCallback(Callback callback2) {
        BaseTransientBottomBar.BaseCallback<Snackbar> baseCallback = this.callback;
        if (baseCallback != null) {
            removeCallback(baseCallback);
        }
        if (callback2 != null) {
            addCallback(callback2);
        }
        this.callback = callback2;
        return this;
    }

    public Snackbar setMaxInlineActionWidth(int width) {
        getContentLayout().setMaxInlineActionWidth(width);
        return this;
    }

    public Snackbar setText(int resId) {
        return setText(getContext().getText(resId));
    }

    public Snackbar setText(CharSequence message) {
        getMessageView().setText(message);
        return this;
    }

    public Snackbar setTextColor(int color) {
        getMessageView().setTextColor(color);
        return this;
    }

    public Snackbar setTextColor(ColorStateList colors) {
        getMessageView().setTextColor(colors);
        return this;
    }

    public Snackbar setTextMaxLines(int maxLines) {
        getMessageView().setMaxLines(maxLines);
        return this;
    }

    public void show() {
        super.show();
    }
}

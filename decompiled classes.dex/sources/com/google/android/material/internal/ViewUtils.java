package com.google.android.material.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.R;

public class ViewUtils {

    public interface OnApplyWindowInsetsListener {
        WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, RelativePadding relativePadding);
    }

    public static class RelativePadding {
        public int bottom;
        public int end;
        public int start;
        public int top;

        public RelativePadding(int start2, int top2, int end2, int bottom2) {
            this.start = start2;
            this.top = top2;
            this.end = end2;
            this.bottom = bottom2;
        }

        public RelativePadding(RelativePadding other) {
            this.start = other.start;
            this.top = other.top;
            this.end = other.end;
            this.bottom = other.bottom;
        }

        public void applyToView(View view) {
            ViewCompat.setPaddingRelative(view, this.start, this.top, this.end, this.bottom);
        }
    }

    private ViewUtils() {
    }

    public static void addOnGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener victim) {
        if (view != null) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(victim);
        }
    }

    public static void doOnApplyWindowInsets(View view, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        doOnApplyWindowInsets(view, attrs, defStyleAttr, defStyleRes, (OnApplyWindowInsetsListener) null);
    }

    public static void doOnApplyWindowInsets(View view, AttributeSet attrs, int defStyleAttr, int defStyleRes, final OnApplyWindowInsetsListener listener) {
        TypedArray obtainStyledAttributes = view.getContext().obtainStyledAttributes(attrs, R.styleable.Insets, defStyleAttr, defStyleRes);
        final boolean z = obtainStyledAttributes.getBoolean(R.styleable.Insets_paddingBottomSystemWindowInsets, false);
        final boolean z2 = obtainStyledAttributes.getBoolean(R.styleable.Insets_paddingLeftSystemWindowInsets, false);
        final boolean z3 = obtainStyledAttributes.getBoolean(R.styleable.Insets_paddingRightSystemWindowInsets, false);
        obtainStyledAttributes.recycle();
        doOnApplyWindowInsets(view, new OnApplyWindowInsetsListener() {
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat insets, RelativePadding initialPadding) {
                if (z) {
                    initialPadding.bottom += insets.getSystemWindowInsetBottom();
                }
                boolean isLayoutRtl = ViewUtils.isLayoutRtl(view);
                if (z2) {
                    if (isLayoutRtl) {
                        initialPadding.end += insets.getSystemWindowInsetLeft();
                    } else {
                        initialPadding.start += insets.getSystemWindowInsetLeft();
                    }
                }
                if (z3) {
                    if (isLayoutRtl) {
                        initialPadding.start += insets.getSystemWindowInsetRight();
                    } else {
                        initialPadding.end += insets.getSystemWindowInsetRight();
                    }
                }
                initialPadding.applyToView(view);
                OnApplyWindowInsetsListener onApplyWindowInsetsListener = listener;
                return onApplyWindowInsetsListener != null ? onApplyWindowInsetsListener.onApplyWindowInsets(view, insets, initialPadding) : insets;
            }
        });
    }

    public static void doOnApplyWindowInsets(View view, final OnApplyWindowInsetsListener listener) {
        final RelativePadding relativePadding = new RelativePadding(ViewCompat.getPaddingStart(view), view.getPaddingTop(), ViewCompat.getPaddingEnd(view), view.getPaddingBottom());
        ViewCompat.setOnApplyWindowInsetsListener(view, new androidx.core.view.OnApplyWindowInsetsListener() {
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat insets) {
                return OnApplyWindowInsetsListener.this.onApplyWindowInsets(view, insets, new RelativePadding(relativePadding));
            }
        });
        requestApplyInsetsWhenAttached(view);
    }

    public static float dpToPx(Context context, int dp) {
        return TypedValue.applyDimension(1, (float) dp, context.getResources().getDisplayMetrics());
    }

    public static Integer getBackgroundColor(View view) {
        if (view.getBackground() instanceof ColorDrawable) {
            return Integer.valueOf(((ColorDrawable) view.getBackground()).getColor());
        }
        return null;
    }

    public static ViewGroup getContentView(View view) {
        if (view == null) {
            return null;
        }
        View rootView = view.getRootView();
        ViewGroup viewGroup = (ViewGroup) rootView.findViewById(16908290);
        if (viewGroup != null) {
            return viewGroup;
        }
        if (rootView == view || !(rootView instanceof ViewGroup)) {
            return null;
        }
        return (ViewGroup) rootView;
    }

    public static ViewOverlayImpl getContentViewOverlay(View view) {
        return getOverlay(getContentView(view));
    }

    public static ViewOverlayImpl getOverlay(View view) {
        if (view == null) {
            return null;
        }
        return Build.VERSION.SDK_INT >= 18 ? new ViewOverlayApi18(view) : ViewOverlayApi14.createFrom(view);
    }

    public static float getParentAbsoluteElevation(View view) {
        float f = 0.0f;
        for (ViewParent parent = view.getParent(); parent instanceof View; parent = parent.getParent()) {
            f += ViewCompat.getElevation((View) parent);
        }
        return f;
    }

    public static boolean isLayoutRtl(View view) {
        return ViewCompat.getLayoutDirection(view) == 1;
    }

    public static PorterDuff.Mode parseTintMode(int value, PorterDuff.Mode defaultMode) {
        switch (value) {
            case 3:
                return PorterDuff.Mode.SRC_OVER;
            case 5:
                return PorterDuff.Mode.SRC_IN;
            case 9:
                return PorterDuff.Mode.SRC_ATOP;
            case 14:
                return PorterDuff.Mode.MULTIPLY;
            case 15:
                return PorterDuff.Mode.SCREEN;
            case 16:
                return PorterDuff.Mode.ADD;
            default:
                return defaultMode;
        }
    }

    public static void removeOnGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener victim) {
        if (view != null) {
            removeOnGlobalLayoutListener(view.getViewTreeObserver(), victim);
        }
    }

    public static void removeOnGlobalLayoutListener(ViewTreeObserver viewTreeObserver, ViewTreeObserver.OnGlobalLayoutListener victim) {
        if (Build.VERSION.SDK_INT >= 16) {
            viewTreeObserver.removeOnGlobalLayoutListener(victim);
        } else {
            viewTreeObserver.removeGlobalOnLayoutListener(victim);
        }
    }

    public static void requestApplyInsetsWhenAttached(View view) {
        if (ViewCompat.isAttachedToWindow(view)) {
            ViewCompat.requestApplyInsets(view);
        } else {
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                public void onViewAttachedToWindow(View v) {
                    v.removeOnAttachStateChangeListener(this);
                    ViewCompat.requestApplyInsets(v);
                }

                public void onViewDetachedFromWindow(View v) {
                }
            });
        }
    }

    public static void requestFocusAndShowKeyboard(final View view) {
        view.requestFocus();
        view.post(new Runnable() {
            public void run() {
                ((InputMethodManager) view.getContext().getSystemService("input_method")).showSoftInput(view, 1);
            }
        });
    }
}

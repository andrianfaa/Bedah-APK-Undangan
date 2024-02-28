package androidx.core.view;

import android.os.Build;
import android.view.ViewGroup;

public final class MarginLayoutParamsCompat {

    static class Api17Impl {
        private Api17Impl() {
        }

        static int getLayoutDirection(ViewGroup.MarginLayoutParams marginLayoutParams) {
            return marginLayoutParams.getLayoutDirection();
        }

        static int getMarginEnd(ViewGroup.MarginLayoutParams marginLayoutParams) {
            return marginLayoutParams.getMarginEnd();
        }

        static int getMarginStart(ViewGroup.MarginLayoutParams marginLayoutParams) {
            return marginLayoutParams.getMarginStart();
        }

        static boolean isMarginRelative(ViewGroup.MarginLayoutParams marginLayoutParams) {
            return marginLayoutParams.isMarginRelative();
        }

        static void resolveLayoutDirection(ViewGroup.MarginLayoutParams marginLayoutParams, int layoutDirection) {
            marginLayoutParams.resolveLayoutDirection(layoutDirection);
        }

        static void setLayoutDirection(ViewGroup.MarginLayoutParams marginLayoutParams, int layoutDirection) {
            marginLayoutParams.setLayoutDirection(layoutDirection);
        }

        static void setMarginEnd(ViewGroup.MarginLayoutParams marginLayoutParams, int end) {
            marginLayoutParams.setMarginEnd(end);
        }

        static void setMarginStart(ViewGroup.MarginLayoutParams marginLayoutParams, int start) {
            marginLayoutParams.setMarginStart(start);
        }
    }

    private MarginLayoutParamsCompat() {
    }

    public static int getLayoutDirection(ViewGroup.MarginLayoutParams lp) {
        int layoutDirection = Build.VERSION.SDK_INT >= 17 ? Api17Impl.getLayoutDirection(lp) : 0;
        if (layoutDirection == 0 || layoutDirection == 1) {
            return layoutDirection;
        }
        return 0;
    }

    public static int getMarginEnd(ViewGroup.MarginLayoutParams lp) {
        return Build.VERSION.SDK_INT >= 17 ? Api17Impl.getMarginEnd(lp) : lp.rightMargin;
    }

    public static int getMarginStart(ViewGroup.MarginLayoutParams lp) {
        return Build.VERSION.SDK_INT >= 17 ? Api17Impl.getMarginStart(lp) : lp.leftMargin;
    }

    public static boolean isMarginRelative(ViewGroup.MarginLayoutParams lp) {
        if (Build.VERSION.SDK_INT >= 17) {
            return Api17Impl.isMarginRelative(lp);
        }
        return false;
    }

    public static void resolveLayoutDirection(ViewGroup.MarginLayoutParams lp, int layoutDirection) {
        if (Build.VERSION.SDK_INT >= 17) {
            Api17Impl.resolveLayoutDirection(lp, layoutDirection);
        }
    }

    public static void setLayoutDirection(ViewGroup.MarginLayoutParams lp, int layoutDirection) {
        if (Build.VERSION.SDK_INT >= 17) {
            Api17Impl.setLayoutDirection(lp, layoutDirection);
        }
    }

    public static void setMarginEnd(ViewGroup.MarginLayoutParams lp, int marginEnd) {
        if (Build.VERSION.SDK_INT >= 17) {
            Api17Impl.setMarginEnd(lp, marginEnd);
        } else {
            lp.rightMargin = marginEnd;
        }
    }

    public static void setMarginStart(ViewGroup.MarginLayoutParams lp, int marginStart) {
        if (Build.VERSION.SDK_INT >= 17) {
            Api17Impl.setMarginStart(lp, marginStart);
        } else {
            lp.leftMargin = marginStart;
        }
    }
}

package com.google.android.material.color;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.Window;

final class ThemeUtils {
    private ThemeUtils() {
    }

    static void applyThemeOverlay(Context context, int theme) {
        Resources.Theme windowDecorViewTheme;
        context.getTheme().applyStyle(theme, true);
        if ((context instanceof Activity) && (windowDecorViewTheme = getWindowDecorViewTheme((Activity) context)) != null) {
            windowDecorViewTheme.applyStyle(theme, true);
        }
    }

    private static Resources.Theme getWindowDecorViewTheme(Activity activity) {
        View peekDecorView;
        Context context;
        Window window = activity.getWindow();
        if (window == null || (peekDecorView = window.peekDecorView()) == null || (context = peekDecorView.getContext()) == null) {
            return null;
        }
        return context.getTheme();
    }
}

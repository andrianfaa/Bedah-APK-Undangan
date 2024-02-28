package com.google.android.material.internal;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ToolbarUtils {
    private static final Comparator<View> VIEW_TOP_COMPARATOR = new Comparator<View>() {
        public int compare(View view1, View view2) {
            return view1.getTop() - view2.getTop();
        }
    };

    private ToolbarUtils() {
    }

    public static ActionMenuItemView getActionMenuItemView(Toolbar toolbar, int menuItemId) {
        ActionMenuView actionMenuView = getActionMenuView(toolbar);
        if (actionMenuView == null) {
            return null;
        }
        for (int i = 0; i < actionMenuView.getChildCount(); i++) {
            View childAt = actionMenuView.getChildAt(i);
            if (childAt instanceof ActionMenuItemView) {
                ActionMenuItemView actionMenuItemView = (ActionMenuItemView) childAt;
                if (actionMenuItemView.getItemData().getItemId() == menuItemId) {
                    return actionMenuItemView;
                }
            }
        }
        return null;
    }

    public static ActionMenuView getActionMenuView(Toolbar toolbar) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View childAt = toolbar.getChildAt(i);
            if (childAt instanceof ActionMenuView) {
                return (ActionMenuView) childAt;
            }
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0013, code lost:
        r3 = (android.widget.ImageView) r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.widget.ImageView getImageView(androidx.appcompat.widget.Toolbar r7, android.graphics.drawable.Drawable r8) {
        /*
            r0 = 0
            if (r8 != 0) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
        L_0x0005:
            int r2 = r7.getChildCount()
            if (r1 >= r2) goto L_0x0034
            android.view.View r2 = r7.getChildAt(r1)
            boolean r3 = r2 instanceof android.widget.ImageView
            if (r3 == 0) goto L_0x0031
            r3 = r2
            android.widget.ImageView r3 = (android.widget.ImageView) r3
            android.graphics.drawable.Drawable r4 = r3.getDrawable()
            if (r4 == 0) goto L_0x0031
            android.graphics.drawable.Drawable$ConstantState r5 = r4.getConstantState()
            if (r5 == 0) goto L_0x0031
            android.graphics.drawable.Drawable$ConstantState r5 = r4.getConstantState()
            android.graphics.drawable.Drawable$ConstantState r6 = r8.getConstantState()
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x0031
            return r3
        L_0x0031:
            int r1 = r1 + 1
            goto L_0x0005
        L_0x0034:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.internal.ToolbarUtils.getImageView(androidx.appcompat.widget.Toolbar, android.graphics.drawable.Drawable):android.widget.ImageView");
    }

    public static ImageView getLogoImageView(Toolbar toolbar) {
        return getImageView(toolbar, toolbar.getLogo());
    }

    public static ImageButton getNavigationIconButton(Toolbar toolbar) {
        Drawable navigationIcon = toolbar.getNavigationIcon();
        if (navigationIcon == null) {
            return null;
        }
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View childAt = toolbar.getChildAt(i);
            if (childAt instanceof ImageButton) {
                ImageButton imageButton = (ImageButton) childAt;
                if (imageButton.getDrawable() == navigationIcon) {
                    return imageButton;
                }
            }
        }
        return null;
    }

    public static View getSecondaryActionMenuItemView(Toolbar toolbar) {
        ActionMenuView actionMenuView = getActionMenuView(toolbar);
        if (actionMenuView == null || actionMenuView.getChildCount() <= 1) {
            return null;
        }
        return actionMenuView.getChildAt(0);
    }

    public static TextView getSubtitleTextView(Toolbar toolbar) {
        List<TextView> textViewsWithText = getTextViewsWithText(toolbar, toolbar.getSubtitle());
        if (textViewsWithText.isEmpty()) {
            return null;
        }
        return (TextView) Collections.max(textViewsWithText, VIEW_TOP_COMPARATOR);
    }

    private static List<TextView> getTextViewsWithText(Toolbar toolbar, CharSequence text) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View childAt = toolbar.getChildAt(i);
            if (childAt instanceof TextView) {
                TextView textView = (TextView) childAt;
                if (TextUtils.equals(textView.getText(), text)) {
                    arrayList.add(textView);
                }
            }
        }
        return arrayList;
    }

    public static TextView getTitleTextView(Toolbar toolbar) {
        List<TextView> textViewsWithText = getTextViewsWithText(toolbar, toolbar.getTitle());
        if (textViewsWithText.isEmpty()) {
            return null;
        }
        return (TextView) Collections.min(textViewsWithText, VIEW_TOP_COMPARATOR);
    }
}

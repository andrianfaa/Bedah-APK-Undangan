package com.google.android.material.bottomnavigation;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarMenuView;

public class BottomNavigationMenuView extends NavigationBarMenuView {
    private final int activeItemMaxWidth;
    private final int activeItemMinWidth;
    private final int inactiveItemMaxWidth;
    private final int inactiveItemMinWidth;
    private boolean itemHorizontalTranslationEnabled;
    private int[] tempChildWidths = new int[5];

    public BottomNavigationMenuView(Context context) {
        super(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        setLayoutParams(layoutParams);
        Resources resources = getResources();
        this.inactiveItemMaxWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_max_width);
        this.inactiveItemMinWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_item_min_width);
        this.activeItemMaxWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_item_max_width);
        this.activeItemMinWidth = resources.getDimensionPixelSize(R.dimen.design_bottom_navigation_active_item_min_width);
    }

    /* access modifiers changed from: protected */
    public NavigationBarItemView createNavigationBarItemView(Context context) {
        return new BottomNavigationItemView(context);
    }

    public boolean isItemHorizontalTranslationEnabled() {
        return this.itemHorizontalTranslationEnabled;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int i = right - left;
        int i2 = bottom - top;
        int i3 = 0;
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = getChildAt(i4);
            if (childAt.getVisibility() != 8) {
                if (ViewCompat.getLayoutDirection(this) == 1) {
                    childAt.layout((i - i3) - childAt.getMeasuredWidth(), 0, i - i3, i2);
                } else {
                    childAt.layout(i3, 0, childAt.getMeasuredWidth() + i3, i2);
                }
                i3 += childAt.getMeasuredWidth();
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x010b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMeasure(int r22, int r23) {
        /*
            r21 = this;
            r0 = r21
            androidx.appcompat.view.menu.MenuBuilder r1 = r21.getMenu()
            int r2 = android.view.View.MeasureSpec.getSize(r22)
            java.util.ArrayList r3 = r1.getVisibleItems()
            int r3 = r3.size()
            int r4 = r21.getChildCount()
            int r5 = android.view.View.MeasureSpec.getSize(r23)
            r6 = 1073741824(0x40000000, float:2.0)
            int r7 = android.view.View.MeasureSpec.makeMeasureSpec(r5, r6)
            int r8 = r21.getLabelVisibilityMode()
            boolean r8 = r0.isShifting(r8, r3)
            r10 = 8
            if (r8 == 0) goto L_0x00cd
            boolean r8 = r21.isItemHorizontalTranslationEnabled()
            if (r8 == 0) goto L_0x00c8
            int r8 = r21.getSelectedItemPosition()
            android.view.View r8 = r0.getChildAt(r8)
            int r12 = r0.activeItemMinWidth
            int r13 = r8.getVisibility()
            if (r13 == r10) goto L_0x0055
            int r13 = r0.activeItemMaxWidth
            r14 = -2147483648(0xffffffff80000000, float:-0.0)
            int r13 = android.view.View.MeasureSpec.makeMeasureSpec(r13, r14)
            r8.measure(r13, r7)
            int r13 = r8.getMeasuredWidth()
            int r12 = java.lang.Math.max(r12, r13)
        L_0x0055:
            int r13 = r8.getVisibility()
            if (r13 == r10) goto L_0x005d
            r13 = 1
            goto L_0x005e
        L_0x005d:
            r13 = 0
        L_0x005e:
            int r13 = r3 - r13
            int r14 = r0.inactiveItemMinWidth
            int r14 = r14 * r13
            int r14 = r2 - r14
            int r15 = r0.activeItemMaxWidth
            int r15 = java.lang.Math.min(r12, r15)
            int r15 = java.lang.Math.min(r14, r15)
            int r16 = r2 - r15
            if (r13 != 0) goto L_0x0076
            r17 = 1
            goto L_0x0078
        L_0x0076:
            r17 = r13
        L_0x0078:
            int r6 = r16 / r17
            int r9 = r0.inactiveItemMaxWidth
            int r9 = java.lang.Math.min(r6, r9)
            int r17 = r2 - r15
            int r18 = r9 * r13
            int r17 = r17 - r18
            r18 = 0
            r11 = r18
        L_0x008a:
            if (r11 >= r4) goto L_0x00c5
            android.view.View r19 = r0.getChildAt(r11)
            r20 = r1
            int r1 = r19.getVisibility()
            if (r1 == r10) goto L_0x00b7
            int[] r1 = r0.tempChildWidths
            int r10 = r21.getSelectedItemPosition()
            if (r11 != r10) goto L_0x00a2
            r10 = r15
            goto L_0x00a3
        L_0x00a2:
            r10 = r9
        L_0x00a3:
            r1[r11] = r10
            if (r17 <= 0) goto L_0x00b4
            int[] r1 = r0.tempChildWidths
            r10 = r1[r11]
            r18 = 1
            int r10 = r10 + 1
            r1[r11] = r10
            int r17 = r17 + -1
            goto L_0x00be
        L_0x00b4:
            r18 = 1
            goto L_0x00be
        L_0x00b7:
            r18 = 1
            int[] r1 = r0.tempChildWidths
            r10 = 0
            r1[r11] = r10
        L_0x00be:
            int r11 = r11 + 1
            r1 = r20
            r10 = 8
            goto L_0x008a
        L_0x00c5:
            r20 = r1
            goto L_0x0107
        L_0x00c8:
            r20 = r1
            r18 = 1
            goto L_0x00d1
        L_0x00cd:
            r20 = r1
            r18 = 1
        L_0x00d1:
            if (r3 != 0) goto L_0x00d6
            r11 = r18
            goto L_0x00d7
        L_0x00d6:
            r11 = r3
        L_0x00d7:
            int r1 = r2 / r11
            int r6 = r0.activeItemMaxWidth
            int r6 = java.lang.Math.min(r1, r6)
            int r8 = r6 * r3
            int r8 = r2 - r8
            r9 = 0
        L_0x00e4:
            if (r9 >= r4) goto L_0x0107
            android.view.View r10 = r0.getChildAt(r9)
            int r10 = r10.getVisibility()
            r11 = 8
            if (r10 == r11) goto L_0x00ff
            int[] r10 = r0.tempChildWidths
            r10[r9] = r6
            if (r8 <= 0) goto L_0x0104
            int r11 = r6 + 1
            r10[r9] = r11
            int r8 = r8 + -1
            goto L_0x0104
        L_0x00ff:
            int[] r10 = r0.tempChildWidths
            r11 = 0
            r10[r9] = r11
        L_0x0104:
            int r9 = r9 + 1
            goto L_0x00e4
        L_0x0107:
            r1 = 0
            r6 = 0
        L_0x0109:
            if (r6 >= r4) goto L_0x0137
            android.view.View r8 = r0.getChildAt(r6)
            int r9 = r8.getVisibility()
            r10 = 8
            if (r9 != r10) goto L_0x0118
            goto L_0x0134
        L_0x0118:
            int[] r9 = r0.tempChildWidths
            r9 = r9[r6]
            r11 = 1073741824(0x40000000, float:2.0)
            int r9 = android.view.View.MeasureSpec.makeMeasureSpec(r9, r11)
            r8.measure(r9, r7)
            android.view.ViewGroup$LayoutParams r9 = r8.getLayoutParams()
            int r11 = r8.getMeasuredWidth()
            r9.width = r11
            int r11 = r8.getMeasuredWidth()
            int r1 = r1 + r11
        L_0x0134:
            int r6 = r6 + 1
            goto L_0x0109
        L_0x0137:
            r6 = 1073741824(0x40000000, float:2.0)
            int r6 = android.view.View.MeasureSpec.makeMeasureSpec(r1, r6)
            r8 = 0
            int r6 = android.view.View.resolveSizeAndState(r1, r6, r8)
            r9 = r23
            int r8 = android.view.View.resolveSizeAndState(r5, r9, r8)
            r0.setMeasuredDimension(r6, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.bottomnavigation.BottomNavigationMenuView.onMeasure(int, int):void");
    }

    public void setItemHorizontalTranslationEnabled(boolean itemHorizontalTranslationEnabled2) {
        this.itemHorizontalTranslationEnabled = itemHorizontalTranslationEnabled2;
    }
}

package androidx.viewpager2.widget;

import android.animation.LayoutTransition;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

final class AnimateLayoutChangeDetector {
    private static final ViewGroup.MarginLayoutParams ZERO_MARGIN_LAYOUT_PARAMS;
    private LinearLayoutManager mLayoutManager;

    static {
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(-1, -1);
        ZERO_MARGIN_LAYOUT_PARAMS = marginLayoutParams;
        marginLayoutParams.setMargins(0, 0, 0, 0);
    }

    AnimateLayoutChangeDetector(LinearLayoutManager llm) {
        this.mLayoutManager = llm;
    }

    private boolean arePagesLaidOutContiguously() {
        int i;
        int i2;
        int childCount = this.mLayoutManager.getChildCount();
        if (childCount == 0) {
            return true;
        }
        boolean z = this.mLayoutManager.getOrientation() == 0;
        int[] iArr = new int[2];
        iArr[1] = 2;
        iArr[0] = childCount;
        int[][] iArr2 = (int[][]) Array.newInstance(Integer.TYPE, iArr);
        int i3 = 0;
        while (i3 < childCount) {
            View childAt = this.mLayoutManager.getChildAt(i3);
            if (childAt != null) {
                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                ViewGroup.MarginLayoutParams marginLayoutParams = layoutParams instanceof ViewGroup.MarginLayoutParams ? (ViewGroup.MarginLayoutParams) layoutParams : ZERO_MARGIN_LAYOUT_PARAMS;
                iArr2[i3][0] = z ? childAt.getLeft() - marginLayoutParams.leftMargin : childAt.getTop() - marginLayoutParams.topMargin;
                int[] iArr3 = iArr2[i3];
                if (z) {
                    i2 = childAt.getRight();
                    i = marginLayoutParams.rightMargin;
                } else {
                    i2 = childAt.getBottom();
                    i = marginLayoutParams.bottomMargin;
                }
                iArr3[1] = i2 + i;
                i3++;
            } else {
                throw new IllegalStateException("null view contained in the view hierarchy");
            }
        }
        Arrays.sort(iArr2, new Comparator<int[]>() {
            public int compare(int[] lhs, int[] rhs) {
                return lhs[0] - rhs[0];
            }
        });
        for (int i4 = 1; i4 < childCount; i4++) {
            if (iArr2[i4 - 1][1] != iArr2[i4][0]) {
                return false;
            }
        }
        return iArr2[0][0] <= 0 && iArr2[childCount + -1][1] >= iArr2[0][1] - iArr2[0][0];
    }

    private boolean hasRunningChangingLayoutTransition() {
        int childCount = this.mLayoutManager.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (hasRunningChangingLayoutTransition(this.mLayoutManager.getChildAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasRunningChangingLayoutTransition(View view) {
        if (!(view instanceof ViewGroup)) {
            return false;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        LayoutTransition layoutTransition = viewGroup.getLayoutTransition();
        if (layoutTransition != null && layoutTransition.isChangingLayout()) {
            return true;
        }
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (hasRunningChangingLayoutTransition(viewGroup.getChildAt(i))) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean mayHaveInterferingAnimations() {
        return (!arePagesLaidOutContiguously() || this.mLayoutManager.getChildCount() <= 1) && hasRunningChangingLayoutTransition();
    }
}

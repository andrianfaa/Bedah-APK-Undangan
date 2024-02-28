package androidx.core.view;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.R;
import mt.Log1F380D;

/* compiled from: 0062 */
public final class ViewGroupCompat {
    public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;
    public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;

    static class Api18Impl {
        private Api18Impl() {
        }

        static int getLayoutMode(ViewGroup viewGroup) {
            return viewGroup.getLayoutMode();
        }

        static void setLayoutMode(ViewGroup viewGroup, int layoutMode) {
            viewGroup.setLayoutMode(layoutMode);
        }
    }

    static class Api21Impl {
        private Api21Impl() {
        }

        static int getNestedScrollAxes(ViewGroup viewGroup) {
            return viewGroup.getNestedScrollAxes();
        }

        static boolean isTransitionGroup(ViewGroup viewGroup) {
            return viewGroup.isTransitionGroup();
        }

        static void setTransitionGroup(ViewGroup viewGroup, boolean isTransitionGroup) {
            viewGroup.setTransitionGroup(isTransitionGroup);
        }
    }

    private ViewGroupCompat() {
    }

    public static int getLayoutMode(ViewGroup group) {
        if (Build.VERSION.SDK_INT >= 18) {
            return Api18Impl.getLayoutMode(group);
        }
        return 0;
    }

    public static int getNestedScrollAxes(ViewGroup group) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getNestedScrollAxes(group);
        }
        if (group instanceof NestedScrollingParent) {
            return ((NestedScrollingParent) group).getNestedScrollAxes();
        }
        return 0;
    }

    public static boolean isTransitionGroup(ViewGroup group) {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.isTransitionGroup(group);
        }
        Boolean bool = (Boolean) group.getTag(R.id.tag_transition_group);
        if ((bool == null || !bool.booleanValue()) && group.getBackground() == null) {
            String transitionName = ViewCompat.getTransitionName(group);
            Log1F380D.a((Object) transitionName);
            return transitionName != null;
        }
    }

    @Deprecated
    public static boolean onRequestSendAccessibilityEvent(ViewGroup group, View child, AccessibilityEvent event) {
        return group.onRequestSendAccessibilityEvent(child, event);
    }

    public static void setLayoutMode(ViewGroup group, int mode) {
        if (Build.VERSION.SDK_INT >= 18) {
            Api18Impl.setLayoutMode(group, mode);
        }
    }

    @Deprecated
    public static void setMotionEventSplittingEnabled(ViewGroup group, boolean split) {
        group.setMotionEventSplittingEnabled(split);
    }

    public static void setTransitionGroup(ViewGroup group, boolean isTransitionGroup) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setTransitionGroup(group, isTransitionGroup);
        } else {
            group.setTag(R.id.tag_transition_group, Boolean.valueOf(isTransitionGroup));
        }
    }
}

package androidx.core.view.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build;
import android.view.accessibility.AccessibilityManager;
import java.util.List;

public final class AccessibilityManagerCompat {

    @Deprecated
    public interface AccessibilityStateChangeListener {
        @Deprecated
        void onAccessibilityStateChanged(boolean z);
    }

    @Deprecated
    public static abstract class AccessibilityStateChangeListenerCompat implements AccessibilityStateChangeListener {
    }

    private static class AccessibilityStateChangeListenerWrapper implements AccessibilityManager.AccessibilityStateChangeListener {
        AccessibilityStateChangeListener mListener;

        AccessibilityStateChangeListenerWrapper(AccessibilityStateChangeListener listener) {
            this.mListener = listener;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof AccessibilityStateChangeListenerWrapper)) {
                return false;
            }
            return this.mListener.equals(((AccessibilityStateChangeListenerWrapper) o).mListener);
        }

        public int hashCode() {
            return this.mListener.hashCode();
        }

        public void onAccessibilityStateChanged(boolean enabled) {
            this.mListener.onAccessibilityStateChanged(enabled);
        }
    }

    static class Api19Impl {
        private Api19Impl() {
        }

        static boolean addTouchExplorationStateChangeListenerWrapper(AccessibilityManager accessibilityManager, TouchExplorationStateChangeListener listener) {
            return accessibilityManager.addTouchExplorationStateChangeListener(new TouchExplorationStateChangeListenerWrapper(listener));
        }

        static boolean removeTouchExplorationStateChangeListenerWrapper(AccessibilityManager accessibilityManager, TouchExplorationStateChangeListener listener) {
            return accessibilityManager.removeTouchExplorationStateChangeListener(new TouchExplorationStateChangeListenerWrapper(listener));
        }
    }

    public interface TouchExplorationStateChangeListener {
        void onTouchExplorationStateChanged(boolean z);
    }

    private static final class TouchExplorationStateChangeListenerWrapper implements AccessibilityManager.TouchExplorationStateChangeListener {
        final TouchExplorationStateChangeListener mListener;

        TouchExplorationStateChangeListenerWrapper(TouchExplorationStateChangeListener listener) {
            this.mListener = listener;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof TouchExplorationStateChangeListenerWrapper)) {
                return false;
            }
            return this.mListener.equals(((TouchExplorationStateChangeListenerWrapper) o).mListener);
        }

        public int hashCode() {
            return this.mListener.hashCode();
        }

        public void onTouchExplorationStateChanged(boolean enabled) {
            this.mListener.onTouchExplorationStateChanged(enabled);
        }
    }

    private AccessibilityManagerCompat() {
    }

    @Deprecated
    public static boolean addAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityStateChangeListener listener) {
        if (listener == null) {
            return false;
        }
        return manager.addAccessibilityStateChangeListener(new AccessibilityStateChangeListenerWrapper(listener));
    }

    public static boolean addTouchExplorationStateChangeListener(AccessibilityManager manager, TouchExplorationStateChangeListener listener) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Api19Impl.addTouchExplorationStateChangeListenerWrapper(manager, listener);
        }
        return false;
    }

    @Deprecated
    public static List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager manager, int feedbackTypeFlags) {
        return manager.getEnabledAccessibilityServiceList(feedbackTypeFlags);
    }

    @Deprecated
    public static List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager manager) {
        return manager.getInstalledAccessibilityServiceList();
    }

    @Deprecated
    public static boolean isTouchExplorationEnabled(AccessibilityManager manager) {
        return manager.isTouchExplorationEnabled();
    }

    @Deprecated
    public static boolean removeAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityStateChangeListener listener) {
        if (listener == null) {
            return false;
        }
        return manager.removeAccessibilityStateChangeListener(new AccessibilityStateChangeListenerWrapper(listener));
    }

    public static boolean removeTouchExplorationStateChangeListener(AccessibilityManager manager, TouchExplorationStateChangeListener listener) {
        if (Build.VERSION.SDK_INT >= 19) {
            return Api19Impl.removeTouchExplorationStateChangeListenerWrapper(manager, listener);
        }
        return false;
    }
}

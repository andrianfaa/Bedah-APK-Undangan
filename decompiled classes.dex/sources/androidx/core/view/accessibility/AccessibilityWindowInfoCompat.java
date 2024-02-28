package androidx.core.view.accessibility;

import android.graphics.Rect;
import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import mt.Log1F380D;

/* compiled from: 0065 */
public class AccessibilityWindowInfoCompat {
    public static final int TYPE_ACCESSIBILITY_OVERLAY = 4;
    public static final int TYPE_APPLICATION = 1;
    public static final int TYPE_INPUT_METHOD = 2;
    public static final int TYPE_SPLIT_SCREEN_DIVIDER = 5;
    public static final int TYPE_SYSTEM = 3;
    private static final int UNDEFINED = -1;
    private final Object mInfo;

    private static class Api21Impl {
        private Api21Impl() {
        }

        static void getBoundsInScreen(AccessibilityWindowInfo info, Rect outBounds) {
            info.getBoundsInScreen(outBounds);
        }

        static AccessibilityWindowInfo getChild(AccessibilityWindowInfo info, int index) {
            return info.getChild(index);
        }

        static int getChildCount(AccessibilityWindowInfo info) {
            return info.getChildCount();
        }

        static int getId(AccessibilityWindowInfo info) {
            return info.getId();
        }

        static int getLayer(AccessibilityWindowInfo info) {
            return info.getLayer();
        }

        static AccessibilityWindowInfo getParent(AccessibilityWindowInfo info) {
            return info.getParent();
        }

        static AccessibilityNodeInfo getRoot(AccessibilityWindowInfo info) {
            return info.getRoot();
        }

        static int getType(AccessibilityWindowInfo info) {
            return info.getType();
        }

        static boolean isAccessibilityFocused(AccessibilityWindowInfo info) {
            return info.isAccessibilityFocused();
        }

        static boolean isActive(AccessibilityWindowInfo info) {
            return info.isActive();
        }

        static boolean isFocused(AccessibilityWindowInfo info) {
            return info.isFocused();
        }

        static AccessibilityWindowInfo obtain() {
            return AccessibilityWindowInfo.obtain();
        }

        static AccessibilityWindowInfo obtain(AccessibilityWindowInfo info) {
            return AccessibilityWindowInfo.obtain(info);
        }

        static void recycle(AccessibilityWindowInfo info) {
            info.recycle();
        }
    }

    private static class Api24Impl {
        private Api24Impl() {
        }

        static AccessibilityNodeInfo getAnchor(AccessibilityWindowInfo info) {
            return info.getAnchor();
        }

        static CharSequence getTitle(AccessibilityWindowInfo info) {
            return info.getTitle();
        }
    }

    private AccessibilityWindowInfoCompat(Object info) {
        this.mInfo = info;
    }

    public static AccessibilityWindowInfoCompat obtain() {
        if (Build.VERSION.SDK_INT >= 21) {
            return wrapNonNullInstance(Api21Impl.obtain());
        }
        return null;
    }

    public static AccessibilityWindowInfoCompat obtain(AccessibilityWindowInfoCompat info) {
        if (Build.VERSION.SDK_INT < 21 || info == null) {
            return null;
        }
        return wrapNonNullInstance(Api21Impl.obtain((AccessibilityWindowInfo) info.mInfo));
    }

    private static String typeToString(int type) {
        switch (type) {
            case 1:
                return "TYPE_APPLICATION";
            case 2:
                return "TYPE_INPUT_METHOD";
            case 3:
                return "TYPE_SYSTEM";
            case 4:
                return "TYPE_ACCESSIBILITY_OVERLAY";
            default:
                return "<UNKNOWN>";
        }
    }

    static AccessibilityWindowInfoCompat wrapNonNullInstance(Object object) {
        if (object != null) {
            return new AccessibilityWindowInfoCompat(object);
        }
        return null;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof AccessibilityWindowInfoCompat)) {
            return false;
        }
        AccessibilityWindowInfoCompat accessibilityWindowInfoCompat = (AccessibilityWindowInfoCompat) obj;
        Object obj2 = this.mInfo;
        return obj2 == null ? accessibilityWindowInfoCompat.mInfo == null : obj2.equals(accessibilityWindowInfoCompat.mInfo);
    }

    public AccessibilityNodeInfoCompat getAnchor() {
        if (Build.VERSION.SDK_INT >= 24) {
            return AccessibilityNodeInfoCompat.wrapNonNullInstance(Api24Impl.getAnchor((AccessibilityWindowInfo) this.mInfo));
        }
        return null;
    }

    public void getBoundsInScreen(Rect outBounds) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.getBoundsInScreen((AccessibilityWindowInfo) this.mInfo, outBounds);
        }
    }

    public AccessibilityWindowInfoCompat getChild(int index) {
        if (Build.VERSION.SDK_INT >= 21) {
            return wrapNonNullInstance(Api21Impl.getChild((AccessibilityWindowInfo) this.mInfo, index));
        }
        return null;
    }

    public int getChildCount() {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getChildCount((AccessibilityWindowInfo) this.mInfo);
        }
        return 0;
    }

    public int getId() {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getId((AccessibilityWindowInfo) this.mInfo);
        }
        return -1;
    }

    public int getLayer() {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getLayer((AccessibilityWindowInfo) this.mInfo);
        }
        return -1;
    }

    public AccessibilityWindowInfoCompat getParent() {
        if (Build.VERSION.SDK_INT >= 21) {
            return wrapNonNullInstance(Api21Impl.getParent((AccessibilityWindowInfo) this.mInfo));
        }
        return null;
    }

    public AccessibilityNodeInfoCompat getRoot() {
        if (Build.VERSION.SDK_INT >= 21) {
            return AccessibilityNodeInfoCompat.wrapNonNullInstance(Api21Impl.getRoot((AccessibilityWindowInfo) this.mInfo));
        }
        return null;
    }

    public CharSequence getTitle() {
        if (Build.VERSION.SDK_INT >= 24) {
            return Api24Impl.getTitle((AccessibilityWindowInfo) this.mInfo);
        }
        return null;
    }

    public int getType() {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.getType((AccessibilityWindowInfo) this.mInfo);
        }
        return -1;
    }

    public int hashCode() {
        Object obj = this.mInfo;
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    public boolean isAccessibilityFocused() {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.isAccessibilityFocused((AccessibilityWindowInfo) this.mInfo);
        }
        return true;
    }

    public boolean isActive() {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.isActive((AccessibilityWindowInfo) this.mInfo);
        }
        return true;
    }

    public boolean isFocused() {
        if (Build.VERSION.SDK_INT >= 21) {
            return Api21Impl.isFocused((AccessibilityWindowInfo) this.mInfo);
        }
        return true;
    }

    public void recycle() {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.recycle((AccessibilityWindowInfo) this.mInfo);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Rect rect = new Rect();
        getBoundsInScreen(rect);
        sb.append("AccessibilityWindowInfo[");
        sb.append("id=").append(getId());
        StringBuilder append = sb.append(", type=");
        String typeToString = typeToString(getType());
        Log1F380D.a((Object) typeToString);
        append.append(typeToString);
        sb.append(", layer=").append(getLayer());
        sb.append(", bounds=").append(rect);
        sb.append(", focused=").append(isFocused());
        sb.append(", active=").append(isActive());
        boolean z = true;
        sb.append(", hasParent=").append(getParent() != null);
        StringBuilder append2 = sb.append(", hasChildren=");
        if (getChildCount() <= 0) {
            z = false;
        }
        append2.append(z);
        sb.append(']');
        return sb.toString();
    }
}

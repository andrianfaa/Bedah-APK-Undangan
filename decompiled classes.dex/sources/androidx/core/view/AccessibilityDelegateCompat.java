package androidx.core.view;

import android.os.Build;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import androidx.core.R;
import androidx.core.view.accessibility.AccessibilityClickableSpanCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

public class AccessibilityDelegateCompat {
    private static final View.AccessibilityDelegate DEFAULT_DELEGATE = new View.AccessibilityDelegate();
    private final View.AccessibilityDelegate mBridge;
    private final View.AccessibilityDelegate mOriginalDelegate;

    static final class AccessibilityDelegateAdapter extends View.AccessibilityDelegate {
        final AccessibilityDelegateCompat mCompat;

        AccessibilityDelegateAdapter(AccessibilityDelegateCompat compat) {
            this.mCompat = compat;
        }

        public boolean dispatchPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
            return this.mCompat.dispatchPopulateAccessibilityEvent(host, event);
        }

        public AccessibilityNodeProvider getAccessibilityNodeProvider(View host) {
            AccessibilityNodeProviderCompat accessibilityNodeProvider = this.mCompat.getAccessibilityNodeProvider(host);
            if (accessibilityNodeProvider != null) {
                return (AccessibilityNodeProvider) accessibilityNodeProvider.getProvider();
            }
            return null;
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            this.mCompat.onInitializeAccessibilityEvent(host, event);
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
            AccessibilityNodeInfoCompat wrap = AccessibilityNodeInfoCompat.wrap(info);
            wrap.setScreenReaderFocusable(ViewCompat.isScreenReaderFocusable(host));
            wrap.setHeading(ViewCompat.isAccessibilityHeading(host));
            wrap.setPaneTitle(ViewCompat.getAccessibilityPaneTitle(host));
            wrap.setStateDescription(ViewCompat.getStateDescription(host));
            this.mCompat.onInitializeAccessibilityNodeInfo(host, wrap);
            wrap.addSpansToExtras(info.getText(), host);
            List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> actionList = AccessibilityDelegateCompat.getActionList(host);
            for (int i = 0; i < actionList.size(); i++) {
                wrap.addAction(actionList.get(i));
            }
        }

        public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
            this.mCompat.onPopulateAccessibilityEvent(host, event);
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
            return this.mCompat.onRequestSendAccessibilityEvent(host, child, event);
        }

        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            return this.mCompat.performAccessibilityAction(host, action, args);
        }

        public void sendAccessibilityEvent(View host, int eventType) {
            this.mCompat.sendAccessibilityEvent(host, eventType);
        }

        public void sendAccessibilityEventUnchecked(View host, AccessibilityEvent event) {
            this.mCompat.sendAccessibilityEventUnchecked(host, event);
        }
    }

    static class Api16Impl {
        private Api16Impl() {
        }

        static AccessibilityNodeProvider getAccessibilityNodeProvider(View.AccessibilityDelegate accessibilityDelegate, View host) {
            return accessibilityDelegate.getAccessibilityNodeProvider(host);
        }

        static boolean performAccessibilityAction(View.AccessibilityDelegate accessibilityDelegate, View host, int action, Bundle args) {
            return accessibilityDelegate.performAccessibilityAction(host, action, args);
        }
    }

    public AccessibilityDelegateCompat() {
        this(DEFAULT_DELEGATE);
    }

    public AccessibilityDelegateCompat(View.AccessibilityDelegate originalDelegate) {
        this.mOriginalDelegate = originalDelegate;
        this.mBridge = new AccessibilityDelegateAdapter(this);
    }

    static List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> getActionList(View view) {
        List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> list = (List) view.getTag(R.id.tag_accessibility_actions);
        return list == null ? Collections.emptyList() : list;
    }

    private boolean isSpanStillValid(ClickableSpan span, View view) {
        if (span == null) {
            return false;
        }
        ClickableSpan[] clickableSpans = AccessibilityNodeInfoCompat.getClickableSpans(view.createAccessibilityNodeInfo().getText());
        int i = 0;
        while (clickableSpans != null && i < clickableSpans.length) {
            if (span.equals(clickableSpans[i])) {
                return true;
            }
            i++;
        }
        return false;
    }

    private boolean performClickableSpanAction(int clickableSpanId, View host) {
        WeakReference weakReference;
        SparseArray sparseArray = (SparseArray) host.getTag(R.id.tag_accessibility_clickable_spans);
        if (sparseArray == null || (weakReference = (WeakReference) sparseArray.get(clickableSpanId)) == null) {
            return false;
        }
        ClickableSpan clickableSpan = (ClickableSpan) weakReference.get();
        if (!isSpanStillValid(clickableSpan, host)) {
            return false;
        }
        clickableSpan.onClick(host);
        return true;
    }

    public boolean dispatchPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
        return this.mOriginalDelegate.dispatchPopulateAccessibilityEvent(host, event);
    }

    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View host) {
        AccessibilityNodeProvider accessibilityNodeProvider;
        if (Build.VERSION.SDK_INT < 16 || (accessibilityNodeProvider = Api16Impl.getAccessibilityNodeProvider(this.mOriginalDelegate, host)) == null) {
            return null;
        }
        return new AccessibilityNodeProviderCompat(accessibilityNodeProvider);
    }

    /* access modifiers changed from: package-private */
    public View.AccessibilityDelegate getBridge() {
        return this.mBridge;
    }

    public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
        this.mOriginalDelegate.onInitializeAccessibilityEvent(host, event);
    }

    public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
        this.mOriginalDelegate.onInitializeAccessibilityNodeInfo(host, info.unwrap());
    }

    public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
        this.mOriginalDelegate.onPopulateAccessibilityEvent(host, event);
    }

    public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
        return this.mOriginalDelegate.onRequestSendAccessibilityEvent(host, child, event);
    }

    public boolean performAccessibilityAction(View host, int action, Bundle args) {
        boolean z = false;
        List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> actionList = getActionList(host);
        int i = 0;
        while (true) {
            if (i >= actionList.size()) {
                break;
            }
            AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat = actionList.get(i);
            if (accessibilityActionCompat.getId() == action) {
                z = accessibilityActionCompat.perform(host, args);
                break;
            }
            i++;
        }
        if (!z && Build.VERSION.SDK_INT >= 16) {
            z = Api16Impl.performAccessibilityAction(this.mOriginalDelegate, host, action, args);
        }
        return (z || action != R.id.accessibility_action_clickable_span || args == null) ? z : performClickableSpanAction(args.getInt(AccessibilityClickableSpanCompat.SPAN_ID, -1), host);
    }

    public void sendAccessibilityEvent(View host, int eventType) {
        this.mOriginalDelegate.sendAccessibilityEvent(host, eventType);
    }

    public void sendAccessibilityEventUnchecked(View host, AccessibilityEvent event) {
        this.mOriginalDelegate.sendAccessibilityEventUnchecked(host, event);
    }
}

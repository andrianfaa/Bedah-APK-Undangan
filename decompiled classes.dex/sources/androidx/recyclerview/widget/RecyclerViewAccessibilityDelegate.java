package androidx.recyclerview.widget;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import java.util.Map;
import java.util.WeakHashMap;

public class RecyclerViewAccessibilityDelegate extends AccessibilityDelegateCompat {
    private final ItemDelegate mItemDelegate;
    final RecyclerView mRecyclerView;

    public static class ItemDelegate extends AccessibilityDelegateCompat {
        private Map<View, AccessibilityDelegateCompat> mOriginalItemDelegates = new WeakHashMap();
        final RecyclerViewAccessibilityDelegate mRecyclerViewDelegate;

        public ItemDelegate(RecyclerViewAccessibilityDelegate recyclerViewDelegate) {
            this.mRecyclerViewDelegate = recyclerViewDelegate;
        }

        public boolean dispatchPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
            AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(host);
            return accessibilityDelegateCompat != null ? accessibilityDelegateCompat.dispatchPopulateAccessibilityEvent(host, event) : super.dispatchPopulateAccessibilityEvent(host, event);
        }

        public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View host) {
            AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(host);
            return accessibilityDelegateCompat != null ? accessibilityDelegateCompat.getAccessibilityNodeProvider(host) : super.getAccessibilityNodeProvider(host);
        }

        /* access modifiers changed from: package-private */
        public AccessibilityDelegateCompat getAndRemoveOriginalDelegateForItem(View itemView) {
            return this.mOriginalItemDelegates.remove(itemView);
        }

        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(host);
            if (accessibilityDelegateCompat != null) {
                accessibilityDelegateCompat.onInitializeAccessibilityEvent(host, event);
            } else {
                super.onInitializeAccessibilityEvent(host, event);
            }
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            if (this.mRecyclerViewDelegate.shouldIgnore() || this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager() == null) {
                super.onInitializeAccessibilityNodeInfo(host, info);
                return;
            }
            this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager().onInitializeAccessibilityNodeInfoForItem(host, info);
            AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(host);
            if (accessibilityDelegateCompat != null) {
                accessibilityDelegateCompat.onInitializeAccessibilityNodeInfo(host, info);
            } else {
                super.onInitializeAccessibilityNodeInfo(host, info);
            }
        }

        public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
            AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(host);
            if (accessibilityDelegateCompat != null) {
                accessibilityDelegateCompat.onPopulateAccessibilityEvent(host, event);
            } else {
                super.onPopulateAccessibilityEvent(host, event);
            }
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child, AccessibilityEvent event) {
            AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(host);
            return accessibilityDelegateCompat != null ? accessibilityDelegateCompat.onRequestSendAccessibilityEvent(host, child, event) : super.onRequestSendAccessibilityEvent(host, child, event);
        }

        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            if (this.mRecyclerViewDelegate.shouldIgnore() || this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager() == null) {
                return super.performAccessibilityAction(host, action, args);
            }
            AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(host);
            if (accessibilityDelegateCompat != null) {
                if (accessibilityDelegateCompat.performAccessibilityAction(host, action, args)) {
                    return true;
                }
            } else if (super.performAccessibilityAction(host, action, args)) {
                return true;
            }
            return this.mRecyclerViewDelegate.mRecyclerView.getLayoutManager().performAccessibilityActionForItem(host, action, args);
        }

        /* access modifiers changed from: package-private */
        public void saveOriginalDelegate(View itemView) {
            AccessibilityDelegateCompat accessibilityDelegate = ViewCompat.getAccessibilityDelegate(itemView);
            if (accessibilityDelegate != null && accessibilityDelegate != this) {
                this.mOriginalItemDelegates.put(itemView, accessibilityDelegate);
            }
        }

        public void sendAccessibilityEvent(View host, int eventType) {
            AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(host);
            if (accessibilityDelegateCompat != null) {
                accessibilityDelegateCompat.sendAccessibilityEvent(host, eventType);
            } else {
                super.sendAccessibilityEvent(host, eventType);
            }
        }

        public void sendAccessibilityEventUnchecked(View host, AccessibilityEvent event) {
            AccessibilityDelegateCompat accessibilityDelegateCompat = this.mOriginalItemDelegates.get(host);
            if (accessibilityDelegateCompat != null) {
                accessibilityDelegateCompat.sendAccessibilityEventUnchecked(host, event);
            } else {
                super.sendAccessibilityEventUnchecked(host, event);
            }
        }
    }

    public RecyclerViewAccessibilityDelegate(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        AccessibilityDelegateCompat itemDelegate = getItemDelegate();
        if (itemDelegate == null || !(itemDelegate instanceof ItemDelegate)) {
            this.mItemDelegate = new ItemDelegate(this);
        } else {
            this.mItemDelegate = (ItemDelegate) itemDelegate;
        }
    }

    public AccessibilityDelegateCompat getItemDelegate() {
        return this.mItemDelegate;
    }

    public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(host, event);
        if ((host instanceof RecyclerView) && !shouldIgnore()) {
            RecyclerView recyclerView = (RecyclerView) host;
            if (recyclerView.getLayoutManager() != null) {
                recyclerView.getLayoutManager().onInitializeAccessibilityEvent(event);
            }
        }
    }

    public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
        super.onInitializeAccessibilityNodeInfo(host, info);
        if (!shouldIgnore() && this.mRecyclerView.getLayoutManager() != null) {
            this.mRecyclerView.getLayoutManager().onInitializeAccessibilityNodeInfo(info);
        }
    }

    public boolean performAccessibilityAction(View host, int action, Bundle args) {
        if (super.performAccessibilityAction(host, action, args)) {
            return true;
        }
        if (shouldIgnore() || this.mRecyclerView.getLayoutManager() == null) {
            return false;
        }
        return this.mRecyclerView.getLayoutManager().performAccessibilityAction(action, args);
    }

    /* access modifiers changed from: package-private */
    public boolean shouldIgnore() {
        return this.mRecyclerView.hasPendingAdapterUpdates();
    }
}

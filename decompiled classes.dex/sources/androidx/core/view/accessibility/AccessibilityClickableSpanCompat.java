package androidx.core.view.accessibility;

import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.view.View;

public final class AccessibilityClickableSpanCompat extends ClickableSpan {
    public static final String SPAN_ID = "ACCESSIBILITY_CLICKABLE_SPAN_ID";
    private final int mClickableSpanActionId;
    private final AccessibilityNodeInfoCompat mNodeInfoCompat;
    private final int mOriginalClickableSpanId;

    public AccessibilityClickableSpanCompat(int originalClickableSpanId, AccessibilityNodeInfoCompat nodeInfoCompat, int clickableSpanActionId) {
        this.mOriginalClickableSpanId = originalClickableSpanId;
        this.mNodeInfoCompat = nodeInfoCompat;
        this.mClickableSpanActionId = clickableSpanActionId;
    }

    public void onClick(View unused) {
        Bundle bundle = new Bundle();
        bundle.putInt(SPAN_ID, this.mOriginalClickableSpanId);
        this.mNodeInfoCompat.performAction(this.mClickableSpanActionId, bundle);
    }
}

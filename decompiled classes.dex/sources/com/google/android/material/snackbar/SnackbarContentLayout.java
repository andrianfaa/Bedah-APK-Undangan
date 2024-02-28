package com.google.android.material.snackbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;

public class SnackbarContentLayout extends LinearLayout implements ContentViewCallback {
    private Button actionView;
    private int maxInlineActionWidth;
    private TextView messageView;

    public SnackbarContentLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public SnackbarContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private static void updateTopBottomPadding(View view, int topPadding, int bottomPadding) {
        if (ViewCompat.isPaddingRelative(view)) {
            ViewCompat.setPaddingRelative(view, ViewCompat.getPaddingStart(view), topPadding, ViewCompat.getPaddingEnd(view), bottomPadding);
        } else {
            view.setPadding(view.getPaddingLeft(), topPadding, view.getPaddingRight(), bottomPadding);
        }
    }

    private boolean updateViewsWithinLayout(int orientation, int messagePadTop, int messagePadBottom) {
        boolean z = false;
        if (orientation != getOrientation()) {
            setOrientation(orientation);
            z = true;
        }
        if (this.messageView.getPaddingTop() == messagePadTop && this.messageView.getPaddingBottom() == messagePadBottom) {
            return z;
        }
        updateTopBottomPadding(this.messageView, messagePadTop, messagePadBottom);
        return true;
    }

    public void animateContentIn(int delay, int duration) {
        this.messageView.setAlpha(0.0f);
        this.messageView.animate().alpha(1.0f).setDuration((long) duration).setStartDelay((long) delay).start();
        if (this.actionView.getVisibility() == 0) {
            this.actionView.setAlpha(0.0f);
            this.actionView.animate().alpha(1.0f).setDuration((long) duration).setStartDelay((long) delay).start();
        }
    }

    public void animateContentOut(int delay, int duration) {
        this.messageView.setAlpha(1.0f);
        this.messageView.animate().alpha(0.0f).setDuration((long) duration).setStartDelay((long) delay).start();
        if (this.actionView.getVisibility() == 0) {
            this.actionView.setAlpha(1.0f);
            this.actionView.animate().alpha(0.0f).setDuration((long) duration).setStartDelay((long) delay).start();
        }
    }

    public Button getActionView() {
        return this.actionView;
    }

    public TextView getMessageView() {
        return this.messageView;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.messageView = (TextView) findViewById(R.id.snackbar_text);
        this.actionView = (Button) findViewById(R.id.snackbar_action);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getOrientation() != 1) {
            int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical_2lines);
            int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical);
            boolean z = this.messageView.getLayout().getLineCount() > 1;
            boolean z2 = false;
            if (!z || this.maxInlineActionWidth <= 0 || this.actionView.getMeasuredWidth() <= this.maxInlineActionWidth) {
                int i = z ? dimensionPixelSize : dimensionPixelSize2;
                if (updateViewsWithinLayout(0, i, i)) {
                    z2 = true;
                }
            } else if (updateViewsWithinLayout(1, dimensionPixelSize, dimensionPixelSize - dimensionPixelSize2)) {
                z2 = true;
            }
            if (z2) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    public void setMaxInlineActionWidth(int width) {
        this.maxInlineActionWidth = width;
    }

    /* access modifiers changed from: package-private */
    public void updateActionTextColorAlphaIfNeeded(float actionTextColorAlpha) {
        if (actionTextColorAlpha != 1.0f) {
            this.actionView.setTextColor(MaterialColors.layer(MaterialColors.getColor(this, R.attr.colorSurface), this.actionView.getCurrentTextColor(), actionTextColorAlpha));
        }
    }
}

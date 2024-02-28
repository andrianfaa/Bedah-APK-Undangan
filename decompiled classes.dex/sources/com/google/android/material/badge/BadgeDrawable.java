package com.google.android.material.badge;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.badge.BadgeState;
import com.google.android.material.internal.TextDrawableHelper;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.shape.MaterialShapeDrawable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.Locale;
import mt.Log1F380D;
import okhttp3.HttpUrl;

/* compiled from: 00E7 */
public class BadgeDrawable extends Drawable implements TextDrawableHelper.TextDrawableDelegate {
    public static final int BOTTOM_END = 8388693;
    public static final int BOTTOM_START = 8388691;
    static final String DEFAULT_EXCEED_MAX_BADGE_NUMBER_SUFFIX = "+";
    private static final int DEFAULT_STYLE = R.style.Widget_MaterialComponents_Badge;
    private static final int DEFAULT_THEME_ATTR = R.attr.badgeStyle;
    private static final int MAX_CIRCULAR_BADGE_NUMBER_COUNT = 9;
    public static final int TOP_END = 8388661;
    public static final int TOP_START = 8388659;
    private WeakReference<View> anchorViewRef;
    private final Rect badgeBounds = new Rect();
    private float badgeCenterX;
    private float badgeCenterY;
    private final WeakReference<Context> contextRef;
    private float cornerRadius;
    private WeakReference<FrameLayout> customBadgeParentRef;
    private float halfBadgeHeight;
    private float halfBadgeWidth;
    private int maxBadgeNumber;
    private final MaterialShapeDrawable shapeDrawable = new MaterialShapeDrawable();
    private final BadgeState state;
    private final TextDrawableHelper textDrawableHelper;

    @Retention(RetentionPolicy.SOURCE)
    public @interface BadgeGravity {
    }

    private BadgeDrawable(Context context, int badgeResId, int defStyleAttr, int defStyleRes, BadgeState.State savedState) {
        this.contextRef = new WeakReference<>(context);
        ThemeEnforcement.checkMaterialTheme(context);
        TextDrawableHelper textDrawableHelper2 = new TextDrawableHelper(this);
        this.textDrawableHelper = textDrawableHelper2;
        textDrawableHelper2.getTextPaint().setTextAlign(Paint.Align.CENTER);
        setTextAppearanceResource(R.style.TextAppearance_MaterialComponents_Badge);
        this.state = new BadgeState(context, badgeResId, defStyleAttr, defStyleRes, savedState);
        restoreState();
    }

    private void calculateCenterAndBounds(Context context, Rect anchorRect, View anchorView) {
        int totalVerticalOffsetForState = getTotalVerticalOffsetForState();
        switch (this.state.getBadgeGravity()) {
            case 8388691:
            case 8388693:
                this.badgeCenterY = (float) (anchorRect.bottom - totalVerticalOffsetForState);
                break;
            default:
                this.badgeCenterY = (float) (anchorRect.top + totalVerticalOffsetForState);
                break;
        }
        if (getNumber() <= 9) {
            float f = !hasNumber() ? this.state.badgeRadius : this.state.badgeWithTextRadius;
            this.cornerRadius = f;
            this.halfBadgeHeight = f;
            this.halfBadgeWidth = f;
        } else {
            float f2 = this.state.badgeWithTextRadius;
            this.cornerRadius = f2;
            this.halfBadgeHeight = f2;
            this.halfBadgeWidth = (this.textDrawableHelper.getTextWidth(getBadgeText()) / 2.0f) + this.state.badgeWidePadding;
        }
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(hasNumber() ? R.dimen.mtrl_badge_text_horizontal_edge_offset : R.dimen.mtrl_badge_horizontal_edge_offset);
        int totalHorizontalOffsetForState = getTotalHorizontalOffsetForState();
        switch (this.state.getBadgeGravity()) {
            case 8388659:
            case 8388691:
                this.badgeCenterX = ViewCompat.getLayoutDirection(anchorView) == 0 ? (((float) anchorRect.left) - this.halfBadgeWidth) + ((float) dimensionPixelSize) + ((float) totalHorizontalOffsetForState) : ((((float) anchorRect.right) + this.halfBadgeWidth) - ((float) dimensionPixelSize)) - ((float) totalHorizontalOffsetForState);
                return;
            default:
                this.badgeCenterX = ViewCompat.getLayoutDirection(anchorView) == 0 ? ((((float) anchorRect.right) + this.halfBadgeWidth) - ((float) dimensionPixelSize)) - ((float) totalHorizontalOffsetForState) : (((float) anchorRect.left) - this.halfBadgeWidth) + ((float) dimensionPixelSize) + ((float) totalHorizontalOffsetForState);
                return;
        }
    }

    public static BadgeDrawable create(Context context) {
        return new BadgeDrawable(context, 0, DEFAULT_THEME_ATTR, DEFAULT_STYLE, (BadgeState.State) null);
    }

    public static BadgeDrawable createFromResource(Context context, int id) {
        return new BadgeDrawable(context, id, DEFAULT_THEME_ATTR, DEFAULT_STYLE, (BadgeState.State) null);
    }

    static BadgeDrawable createFromSavedState(Context context, BadgeState.State savedState) {
        return new BadgeDrawable(context, 0, DEFAULT_THEME_ATTR, DEFAULT_STYLE, savedState);
    }

    private void drawText(Canvas canvas) {
        Rect rect = new Rect();
        String badgeText = getBadgeText();
        this.textDrawableHelper.getTextPaint().getTextBounds(badgeText, 0, badgeText.length(), rect);
        canvas.drawText(badgeText, this.badgeCenterX, this.badgeCenterY + ((float) (rect.height() / 2)), this.textDrawableHelper.getTextPaint());
    }

    private String getBadgeText() {
        if (getNumber() <= this.maxBadgeNumber) {
            return NumberFormat.getInstance(this.state.getNumberLocale()).format((long) getNumber());
        }
        Context context = (Context) this.contextRef.get();
        if (context == null) {
            return HttpUrl.FRAGMENT_ENCODE_SET;
        }
        String format = String.format(this.state.getNumberLocale(), context.getString(R.string.mtrl_exceed_max_badge_number_suffix), new Object[]{Integer.valueOf(this.maxBadgeNumber), DEFAULT_EXCEED_MAX_BADGE_NUMBER_SUFFIX});
        Log1F380D.a((Object) format);
        return format;
    }

    private int getTotalHorizontalOffsetForState() {
        return this.state.getAdditionalHorizontalOffset() + (hasNumber() ? this.state.getHorizontalOffsetWithText() : this.state.getHorizontalOffsetWithoutText());
    }

    private int getTotalVerticalOffsetForState() {
        return this.state.getAdditionalVerticalOffset() + (hasNumber() ? this.state.getVerticalOffsetWithText() : this.state.getVerticalOffsetWithoutText());
    }

    private void onAlphaUpdated() {
        this.textDrawableHelper.getTextPaint().setAlpha(getAlpha());
        invalidateSelf();
    }

    private void onBackgroundColorUpdated() {
        ColorStateList valueOf = ColorStateList.valueOf(this.state.getBackgroundColor());
        if (this.shapeDrawable.getFillColor() != valueOf) {
            this.shapeDrawable.setFillColor(valueOf);
            invalidateSelf();
        }
    }

    private void onBadgeGravityUpdated() {
        WeakReference<View> weakReference = this.anchorViewRef;
        if (weakReference != null && weakReference.get() != null) {
            View view = (View) this.anchorViewRef.get();
            WeakReference<FrameLayout> weakReference2 = this.customBadgeParentRef;
            updateBadgeCoordinates(view, weakReference2 != null ? (FrameLayout) weakReference2.get() : null);
        }
    }

    private void onBadgeTextColorUpdated() {
        this.textDrawableHelper.getTextPaint().setColor(this.state.getBadgeTextColor());
        invalidateSelf();
    }

    private void onMaxCharacterCountUpdated() {
        updateMaxBadgeNumber();
        this.textDrawableHelper.setTextWidthDirty(true);
        updateCenterAndBounds();
        invalidateSelf();
    }

    private void onNumberUpdated() {
        this.textDrawableHelper.setTextWidthDirty(true);
        updateCenterAndBounds();
        invalidateSelf();
    }

    private void onVisibilityUpdated() {
        boolean isVisible = this.state.isVisible();
        setVisible(isVisible, false);
        if (BadgeUtils.USE_COMPAT_PARENT && getCustomBadgeParent() != null && !isVisible) {
            ((ViewGroup) getCustomBadgeParent().getParent()).invalidate();
        }
    }

    private void restoreState() {
        onMaxCharacterCountUpdated();
        onNumberUpdated();
        onAlphaUpdated();
        onBackgroundColorUpdated();
        onBadgeTextColorUpdated();
        onBadgeGravityUpdated();
        updateCenterAndBounds();
        onVisibilityUpdated();
    }

    private void setTextAppearance(TextAppearance textAppearance) {
        Context context;
        if (this.textDrawableHelper.getTextAppearance() != textAppearance && (context = (Context) this.contextRef.get()) != null) {
            this.textDrawableHelper.setTextAppearance(textAppearance, context);
            updateCenterAndBounds();
        }
    }

    private void setTextAppearanceResource(int id) {
        Context context = (Context) this.contextRef.get();
        if (context != null) {
            setTextAppearance(new TextAppearance(context, id));
        }
    }

    private void tryWrapAnchorInCompatParent(final View anchorView) {
        ViewGroup viewGroup = (ViewGroup) anchorView.getParent();
        if (viewGroup == null || viewGroup.getId() != R.id.mtrl_anchor_parent) {
            WeakReference<FrameLayout> weakReference = this.customBadgeParentRef;
            if (weakReference == null || weakReference.get() != viewGroup) {
                updateAnchorParentToNotClip(anchorView);
                final FrameLayout frameLayout = new FrameLayout(anchorView.getContext());
                frameLayout.setId(R.id.mtrl_anchor_parent);
                frameLayout.setClipChildren(false);
                frameLayout.setClipToPadding(false);
                frameLayout.setLayoutParams(anchorView.getLayoutParams());
                frameLayout.setMinimumWidth(anchorView.getWidth());
                frameLayout.setMinimumHeight(anchorView.getHeight());
                int indexOfChild = viewGroup.indexOfChild(anchorView);
                viewGroup.removeViewAt(indexOfChild);
                anchorView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
                frameLayout.addView(anchorView);
                viewGroup.addView(frameLayout, indexOfChild);
                this.customBadgeParentRef = new WeakReference<>(frameLayout);
                frameLayout.post(new Runnable() {
                    public void run() {
                        BadgeDrawable.this.updateBadgeCoordinates(anchorView, frameLayout);
                    }
                });
            }
        }
    }

    private static void updateAnchorParentToNotClip(View anchorView) {
        ViewGroup viewGroup = (ViewGroup) anchorView.getParent();
        viewGroup.setClipChildren(false);
        viewGroup.setClipToPadding(false);
    }

    private void updateCenterAndBounds() {
        Context context = (Context) this.contextRef.get();
        WeakReference<View> weakReference = this.anchorViewRef;
        FrameLayout frameLayout = null;
        View view = weakReference != null ? (View) weakReference.get() : null;
        if (context != null && view != null) {
            Rect rect = new Rect();
            rect.set(this.badgeBounds);
            Rect rect2 = new Rect();
            view.getDrawingRect(rect2);
            WeakReference<FrameLayout> weakReference2 = this.customBadgeParentRef;
            if (weakReference2 != null) {
                frameLayout = (FrameLayout) weakReference2.get();
            }
            if (frameLayout != null || BadgeUtils.USE_COMPAT_PARENT) {
                (frameLayout == null ? (ViewGroup) view.getParent() : frameLayout).offsetDescendantRectToMyCoords(view, rect2);
            }
            calculateCenterAndBounds(context, rect2, view);
            BadgeUtils.updateBadgeBounds(this.badgeBounds, this.badgeCenterX, this.badgeCenterY, this.halfBadgeWidth, this.halfBadgeHeight);
            this.shapeDrawable.setCornerSize(this.cornerRadius);
            if (!rect.equals(this.badgeBounds)) {
                this.shapeDrawable.setBounds(this.badgeBounds);
            }
        }
    }

    private void updateMaxBadgeNumber() {
        this.maxBadgeNumber = ((int) Math.pow(10.0d, ((double) getMaxCharacterCount()) - 1.0d)) - 1;
    }

    public void clearNumber() {
        if (hasNumber()) {
            this.state.clearNumber();
            onNumberUpdated();
        }
    }

    public void draw(Canvas canvas) {
        if (!getBounds().isEmpty() && getAlpha() != 0 && isVisible()) {
            this.shapeDrawable.draw(canvas);
            if (hasNumber()) {
                drawText(canvas);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int getAdditionalHorizontalOffset() {
        return this.state.getAdditionalHorizontalOffset();
    }

    /* access modifiers changed from: package-private */
    public int getAdditionalVerticalOffset() {
        return this.state.getAdditionalVerticalOffset();
    }

    public int getAlpha() {
        return this.state.getAlpha();
    }

    public int getBackgroundColor() {
        return this.shapeDrawable.getFillColor().getDefaultColor();
    }

    public int getBadgeGravity() {
        return this.state.getBadgeGravity();
    }

    public Locale getBadgeNumberLocale() {
        return this.state.getNumberLocale();
    }

    public int getBadgeTextColor() {
        return this.textDrawableHelper.getTextPaint().getColor();
    }

    public CharSequence getContentDescription() {
        Context context;
        if (!isVisible()) {
            return null;
        }
        if (!hasNumber()) {
            return this.state.getContentDescriptionNumberless();
        }
        if (this.state.getContentDescriptionQuantityStrings() == 0 || (context = (Context) this.contextRef.get()) == null) {
            return null;
        }
        if (getNumber() <= this.maxBadgeNumber) {
            return context.getResources().getQuantityString(this.state.getContentDescriptionQuantityStrings(), getNumber(), new Object[]{Integer.valueOf(getNumber())});
        }
        return context.getString(this.state.getContentDescriptionExceedsMaxBadgeNumberStringResource(), new Object[]{Integer.valueOf(this.maxBadgeNumber)});
    }

    public FrameLayout getCustomBadgeParent() {
        WeakReference<FrameLayout> weakReference = this.customBadgeParentRef;
        if (weakReference != null) {
            return (FrameLayout) weakReference.get();
        }
        return null;
    }

    public int getHorizontalOffset() {
        return this.state.getHorizontalOffsetWithoutText();
    }

    public int getHorizontalOffsetWithText() {
        return this.state.getHorizontalOffsetWithText();
    }

    public int getHorizontalOffsetWithoutText() {
        return this.state.getHorizontalOffsetWithoutText();
    }

    public int getIntrinsicHeight() {
        return this.badgeBounds.height();
    }

    public int getIntrinsicWidth() {
        return this.badgeBounds.width();
    }

    public int getMaxCharacterCount() {
        return this.state.getMaxCharacterCount();
    }

    public int getNumber() {
        if (hasNumber()) {
            return this.state.getNumber();
        }
        return 0;
    }

    public int getOpacity() {
        return -3;
    }

    /* access modifiers changed from: package-private */
    public BadgeState.State getSavedState() {
        return this.state.getOverridingState();
    }

    public int getVerticalOffset() {
        return this.state.getVerticalOffsetWithoutText();
    }

    public int getVerticalOffsetWithText() {
        return this.state.getVerticalOffsetWithText();
    }

    public int getVerticalOffsetWithoutText() {
        return this.state.getVerticalOffsetWithoutText();
    }

    public boolean hasNumber() {
        return this.state.hasNumber();
    }

    public boolean isStateful() {
        return false;
    }

    public boolean onStateChange(int[] state2) {
        return super.onStateChange(state2);
    }

    public void onTextSizeChange() {
        invalidateSelf();
    }

    /* access modifiers changed from: package-private */
    public void setAdditionalHorizontalOffset(int px) {
        this.state.setAdditionalHorizontalOffset(px);
        updateCenterAndBounds();
    }

    /* access modifiers changed from: package-private */
    public void setAdditionalVerticalOffset(int px) {
        this.state.setAdditionalVerticalOffset(px);
        updateCenterAndBounds();
    }

    public void setAlpha(int alpha) {
        this.state.setAlpha(alpha);
        onAlphaUpdated();
    }

    public void setBackgroundColor(int backgroundColor) {
        this.state.setBackgroundColor(backgroundColor);
        onBackgroundColorUpdated();
    }

    public void setBadgeGravity(int gravity) {
        if (this.state.getBadgeGravity() != gravity) {
            this.state.setBadgeGravity(gravity);
            onBadgeGravityUpdated();
        }
    }

    public void setBadgeNumberLocale(Locale locale) {
        if (!locale.equals(this.state.getNumberLocale())) {
            this.state.setNumberLocale(locale);
            invalidateSelf();
        }
    }

    public void setBadgeTextColor(int badgeTextColor) {
        if (this.textDrawableHelper.getTextPaint().getColor() != badgeTextColor) {
            this.state.setBadgeTextColor(badgeTextColor);
            onBadgeTextColorUpdated();
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public void setContentDescriptionExceedsMaxBadgeNumberStringResource(int stringsResource) {
        this.state.setContentDescriptionExceedsMaxBadgeNumberStringResource(stringsResource);
    }

    public void setContentDescriptionNumberless(CharSequence charSequence) {
        this.state.setContentDescriptionNumberless(charSequence);
    }

    public void setContentDescriptionQuantityStringsResource(int stringsResource) {
        this.state.setContentDescriptionQuantityStringsResource(stringsResource);
    }

    public void setHorizontalOffset(int px) {
        setHorizontalOffsetWithoutText(px);
        setHorizontalOffsetWithText(px);
    }

    public void setHorizontalOffsetWithText(int px) {
        this.state.setHorizontalOffsetWithText(px);
        updateCenterAndBounds();
    }

    public void setHorizontalOffsetWithoutText(int px) {
        this.state.setHorizontalOffsetWithoutText(px);
        updateCenterAndBounds();
    }

    public void setMaxCharacterCount(int maxCharacterCount) {
        if (this.state.getMaxCharacterCount() != maxCharacterCount) {
            this.state.setMaxCharacterCount(maxCharacterCount);
            onMaxCharacterCountUpdated();
        }
    }

    public void setNumber(int number) {
        int number2 = Math.max(0, number);
        if (this.state.getNumber() != number2) {
            this.state.setNumber(number2);
            onNumberUpdated();
        }
    }

    public void setVerticalOffset(int px) {
        setVerticalOffsetWithoutText(px);
        setVerticalOffsetWithText(px);
    }

    public void setVerticalOffsetWithText(int px) {
        this.state.setVerticalOffsetWithText(px);
        updateCenterAndBounds();
    }

    public void setVerticalOffsetWithoutText(int px) {
        this.state.setVerticalOffsetWithoutText(px);
        updateCenterAndBounds();
    }

    public void setVisible(boolean visible) {
        this.state.setVisible(visible);
        onVisibilityUpdated();
    }

    public void updateBadgeCoordinates(View anchorView) {
        updateBadgeCoordinates(anchorView, (FrameLayout) null);
    }

    @Deprecated
    public void updateBadgeCoordinates(View anchorView, ViewGroup customBadgeParent) {
        if (customBadgeParent instanceof FrameLayout) {
            updateBadgeCoordinates(anchorView, (FrameLayout) customBadgeParent);
            return;
        }
        throw new IllegalArgumentException("customBadgeParent must be a FrameLayout");
    }

    public void updateBadgeCoordinates(View anchorView, FrameLayout customBadgeParent) {
        this.anchorViewRef = new WeakReference<>(anchorView);
        if (!BadgeUtils.USE_COMPAT_PARENT || customBadgeParent != null) {
            this.customBadgeParentRef = new WeakReference<>(customBadgeParent);
        } else {
            tryWrapAnchorInCompatParent(anchorView);
        }
        if (!BadgeUtils.USE_COMPAT_PARENT) {
            updateAnchorParentToNotClip(anchorView);
        }
        updateCenterAndBounds();
        invalidateSelf();
    }
}

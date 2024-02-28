package androidx.cardview.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;

class CardViewApi21Impl implements CardViewImpl {
    CardViewApi21Impl() {
    }

    private RoundRectDrawable getCardBackground(CardViewDelegate cardView) {
        return (RoundRectDrawable) cardView.getCardBackground();
    }

    public ColorStateList getBackgroundColor(CardViewDelegate cardView) {
        return getCardBackground(cardView).getColor();
    }

    public float getElevation(CardViewDelegate cardView) {
        return cardView.getCardView().getElevation();
    }

    public float getMaxElevation(CardViewDelegate cardView) {
        return getCardBackground(cardView).getPadding();
    }

    public float getMinHeight(CardViewDelegate cardView) {
        return getRadius(cardView) * 2.0f;
    }

    public float getMinWidth(CardViewDelegate cardView) {
        return getRadius(cardView) * 2.0f;
    }

    public float getRadius(CardViewDelegate cardView) {
        return getCardBackground(cardView).getRadius();
    }

    public void initStatic() {
    }

    public void initialize(CardViewDelegate cardView, Context context, ColorStateList backgroundColor, float radius, float elevation, float maxElevation) {
        cardView.setCardBackground(new RoundRectDrawable(backgroundColor, radius));
        View cardView2 = cardView.getCardView();
        cardView2.setClipToOutline(true);
        cardView2.setElevation(elevation);
        setMaxElevation(cardView, maxElevation);
    }

    public void onCompatPaddingChanged(CardViewDelegate cardView) {
        setMaxElevation(cardView, getMaxElevation(cardView));
    }

    public void onPreventCornerOverlapChanged(CardViewDelegate cardView) {
        setMaxElevation(cardView, getMaxElevation(cardView));
    }

    public void setBackgroundColor(CardViewDelegate cardView, ColorStateList color) {
        getCardBackground(cardView).setColor(color);
    }

    public void setElevation(CardViewDelegate cardView, float elevation) {
        cardView.getCardView().setElevation(elevation);
    }

    public void setMaxElevation(CardViewDelegate cardView, float maxElevation) {
        getCardBackground(cardView).setPadding(maxElevation, cardView.getUseCompatPadding(), cardView.getPreventCornerOverlap());
        updatePadding(cardView);
    }

    public void setRadius(CardViewDelegate cardView, float radius) {
        getCardBackground(cardView).setRadius(radius);
    }

    public void updatePadding(CardViewDelegate cardView) {
        if (!cardView.getUseCompatPadding()) {
            cardView.setShadowPadding(0, 0, 0, 0);
            return;
        }
        float maxElevation = getMaxElevation(cardView);
        float radius = getRadius(cardView);
        int ceil = (int) Math.ceil((double) RoundRectDrawableWithShadow.calculateHorizontalPadding(maxElevation, radius, cardView.getPreventCornerOverlap()));
        int ceil2 = (int) Math.ceil((double) RoundRectDrawableWithShadow.calculateVerticalPadding(maxElevation, radius, cardView.getPreventCornerOverlap()));
        cardView.setShadowPadding(ceil, ceil2, ceil, ceil2);
    }
}

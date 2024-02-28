package androidx.cardview.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.cardview.widget.RoundRectDrawableWithShadow;

class CardViewBaseImpl implements CardViewImpl {
    final RectF mCornerRect = new RectF();

    CardViewBaseImpl() {
    }

    private RoundRectDrawableWithShadow createBackground(Context context, ColorStateList backgroundColor, float radius, float elevation, float maxElevation) {
        return new RoundRectDrawableWithShadow(context.getResources(), backgroundColor, radius, elevation, maxElevation);
    }

    private RoundRectDrawableWithShadow getShadowBackground(CardViewDelegate cardView) {
        return (RoundRectDrawableWithShadow) cardView.getCardBackground();
    }

    public ColorStateList getBackgroundColor(CardViewDelegate cardView) {
        return getShadowBackground(cardView).getColor();
    }

    public float getElevation(CardViewDelegate cardView) {
        return getShadowBackground(cardView).getShadowSize();
    }

    public float getMaxElevation(CardViewDelegate cardView) {
        return getShadowBackground(cardView).getMaxShadowSize();
    }

    public float getMinHeight(CardViewDelegate cardView) {
        return getShadowBackground(cardView).getMinHeight();
    }

    public float getMinWidth(CardViewDelegate cardView) {
        return getShadowBackground(cardView).getMinWidth();
    }

    public float getRadius(CardViewDelegate cardView) {
        return getShadowBackground(cardView).getCornerRadius();
    }

    public void initStatic() {
        RoundRectDrawableWithShadow.sRoundRectHelper = new RoundRectDrawableWithShadow.RoundRectHelper() {
            public void drawRoundRect(Canvas canvas, RectF bounds, float cornerRadius, Paint paint) {
                Canvas canvas2 = canvas;
                RectF rectF = bounds;
                float f = cornerRadius * 2.0f;
                float width = (bounds.width() - f) - 1.0f;
                float height = (bounds.height() - f) - 1.0f;
                if (cornerRadius >= 1.0f) {
                    float f2 = cornerRadius + 0.5f;
                    CardViewBaseImpl.this.mCornerRect.set(-f2, -f2, f2, f2);
                    int save = canvas.save();
                    canvas2.translate(rectF.left + f2, rectF.top + f2);
                    canvas.drawArc(CardViewBaseImpl.this.mCornerRect, 180.0f, 90.0f, true, paint);
                    canvas2.translate(width, 0.0f);
                    canvas2.rotate(90.0f);
                    Paint paint2 = paint;
                    canvas.drawArc(CardViewBaseImpl.this.mCornerRect, 180.0f, 90.0f, true, paint2);
                    canvas2.translate(height, 0.0f);
                    canvas2.rotate(90.0f);
                    canvas.drawArc(CardViewBaseImpl.this.mCornerRect, 180.0f, 90.0f, true, paint2);
                    canvas2.translate(width, 0.0f);
                    canvas2.rotate(90.0f);
                    canvas.drawArc(CardViewBaseImpl.this.mCornerRect, 180.0f, 90.0f, true, paint2);
                    canvas2.restoreToCount(save);
                    canvas.drawRect((rectF.left + f2) - 1.0f, rectF.top, (rectF.right - f2) + 1.0f, rectF.top + f2, paint);
                    canvas.drawRect((rectF.left + f2) - 1.0f, rectF.bottom - f2, (rectF.right - f2) + 1.0f, rectF.bottom, paint);
                }
                canvas.drawRect(rectF.left, rectF.top + cornerRadius, rectF.right, rectF.bottom - cornerRadius, paint);
            }
        };
    }

    public void initialize(CardViewDelegate cardView, Context context, ColorStateList backgroundColor, float radius, float elevation, float maxElevation) {
        RoundRectDrawableWithShadow createBackground = createBackground(context, backgroundColor, radius, elevation, maxElevation);
        createBackground.setAddPaddingForCorners(cardView.getPreventCornerOverlap());
        cardView.setCardBackground(createBackground);
        updatePadding(cardView);
    }

    public void onCompatPaddingChanged(CardViewDelegate cardView) {
    }

    public void onPreventCornerOverlapChanged(CardViewDelegate cardView) {
        getShadowBackground(cardView).setAddPaddingForCorners(cardView.getPreventCornerOverlap());
        updatePadding(cardView);
    }

    public void setBackgroundColor(CardViewDelegate cardView, ColorStateList color) {
        getShadowBackground(cardView).setColor(color);
    }

    public void setElevation(CardViewDelegate cardView, float elevation) {
        getShadowBackground(cardView).setShadowSize(elevation);
    }

    public void setMaxElevation(CardViewDelegate cardView, float maxElevation) {
        getShadowBackground(cardView).setMaxShadowSize(maxElevation);
        updatePadding(cardView);
    }

    public void setRadius(CardViewDelegate cardView, float radius) {
        getShadowBackground(cardView).setCornerRadius(radius);
        updatePadding(cardView);
    }

    public void updatePadding(CardViewDelegate cardView) {
        Rect rect = new Rect();
        getShadowBackground(cardView).getMaxShadowAndCornerPadding(rect);
        cardView.setMinWidthHeightInternal((int) Math.ceil((double) getMinWidth(cardView)), (int) Math.ceil((double) getMinHeight(cardView)));
        cardView.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
    }
}

package com.google.android.material.tooltip;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.TextDrawableHelper;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.shape.EdgeTreatment;
import com.google.android.material.shape.MarkerEdgeTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.OffsetEdgeTreatment;

public class TooltipDrawable extends MaterialShapeDrawable implements TextDrawableHelper.TextDrawableDelegate {
    private static final int DEFAULT_STYLE = R.style.Widget_MaterialComponents_Tooltip;
    private static final int DEFAULT_THEME_ATTR = R.attr.tooltipStyle;
    private int arrowSize;
    private final View.OnLayoutChangeListener attachedViewLayoutChangeListener;
    private final Context context;
    private final Rect displayFrame;
    private final Paint.FontMetrics fontMetrics = new Paint.FontMetrics();
    private float labelOpacity;
    private int layoutMargin;
    private int locationOnScreenX;
    private int minHeight;
    private int minWidth;
    private int padding;
    private CharSequence text;
    private final TextDrawableHelper textDrawableHelper;
    private final float tooltipPivotX;
    private float tooltipPivotY;
    private float tooltipScaleX;
    private float tooltipScaleY;

    private TooltipDrawable(Context context2, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context2, attrs, defStyleAttr, defStyleRes);
        TextDrawableHelper textDrawableHelper2 = new TextDrawableHelper(this);
        this.textDrawableHelper = textDrawableHelper2;
        this.attachedViewLayoutChangeListener = new View.OnLayoutChangeListener() {
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                TooltipDrawable.this.updateLocationOnScreen(v);
            }
        };
        this.displayFrame = new Rect();
        this.tooltipScaleX = 1.0f;
        this.tooltipScaleY = 1.0f;
        this.tooltipPivotX = 0.5f;
        this.tooltipPivotY = 0.5f;
        this.labelOpacity = 1.0f;
        this.context = context2;
        textDrawableHelper2.getTextPaint().density = context2.getResources().getDisplayMetrics().density;
        textDrawableHelper2.getTextPaint().setTextAlign(Paint.Align.CENTER);
    }

    private float calculatePointerOffset() {
        if (((this.displayFrame.right - getBounds().right) - this.locationOnScreenX) - this.layoutMargin < 0) {
            return (float) (((this.displayFrame.right - getBounds().right) - this.locationOnScreenX) - this.layoutMargin);
        }
        if (((this.displayFrame.left - getBounds().left) - this.locationOnScreenX) + this.layoutMargin > 0) {
            return (float) (((this.displayFrame.left - getBounds().left) - this.locationOnScreenX) + this.layoutMargin);
        }
        return 0.0f;
    }

    private float calculateTextCenterFromBaseline() {
        this.textDrawableHelper.getTextPaint().getFontMetrics(this.fontMetrics);
        return (this.fontMetrics.descent + this.fontMetrics.ascent) / 2.0f;
    }

    private float calculateTextOriginAndAlignment(Rect bounds) {
        return ((float) bounds.centerY()) - calculateTextCenterFromBaseline();
    }

    public static TooltipDrawable create(Context context2) {
        return createFromAttributes(context2, (AttributeSet) null, DEFAULT_THEME_ATTR, DEFAULT_STYLE);
    }

    public static TooltipDrawable createFromAttributes(Context context2, AttributeSet attrs) {
        return createFromAttributes(context2, attrs, DEFAULT_THEME_ATTR, DEFAULT_STYLE);
    }

    public static TooltipDrawable createFromAttributes(Context context2, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TooltipDrawable tooltipDrawable = new TooltipDrawable(context2, attrs, defStyleAttr, defStyleRes);
        tooltipDrawable.loadFromAttributes(attrs, defStyleAttr, defStyleRes);
        return tooltipDrawable;
    }

    private EdgeTreatment createMarkerEdge() {
        float width = ((float) (((double) getBounds().width()) - (((double) this.arrowSize) * Math.sqrt(2.0d)))) / 2.0f;
        return new OffsetEdgeTreatment(new MarkerEdgeTreatment((float) this.arrowSize), Math.min(Math.max(-calculatePointerOffset(), -width), width));
    }

    private void drawText(Canvas canvas) {
        if (this.text != null) {
            Rect bounds = getBounds();
            int calculateTextOriginAndAlignment = (int) calculateTextOriginAndAlignment(bounds);
            if (this.textDrawableHelper.getTextAppearance() != null) {
                this.textDrawableHelper.getTextPaint().drawableState = getState();
                this.textDrawableHelper.updateTextPaintDrawState(this.context);
                this.textDrawableHelper.getTextPaint().setAlpha((int) (this.labelOpacity * 255.0f));
            }
            CharSequence charSequence = this.text;
            canvas.drawText(charSequence, 0, charSequence.length(), (float) bounds.centerX(), (float) calculateTextOriginAndAlignment, this.textDrawableHelper.getTextPaint());
        }
    }

    private float getTextWidth() {
        CharSequence charSequence = this.text;
        if (charSequence == null) {
            return 0.0f;
        }
        return this.textDrawableHelper.getTextWidth(charSequence.toString());
    }

    private void loadFromAttributes(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Class<TooltipDrawable> cls = TooltipDrawable.class;
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(this.context, attrs, R.styleable.Tooltip, defStyleAttr, defStyleRes, new int[0]);
        this.arrowSize = this.context.getResources().getDimensionPixelSize(R.dimen.mtrl_tooltip_arrowSize);
        setShapeAppearanceModel(getShapeAppearanceModel().toBuilder().setBottomEdge(createMarkerEdge()).build());
        setText(obtainStyledAttributes.getText(R.styleable.Tooltip_android_text));
        TextAppearance textAppearance = MaterialResources.getTextAppearance(this.context, obtainStyledAttributes, R.styleable.Tooltip_android_textAppearance);
        if (textAppearance != null && obtainStyledAttributes.hasValue(R.styleable.Tooltip_android_textColor)) {
            textAppearance.setTextColor(MaterialResources.getColorStateList(this.context, obtainStyledAttributes, R.styleable.Tooltip_android_textColor));
        }
        setTextAppearance(textAppearance);
        setFillColor(ColorStateList.valueOf(obtainStyledAttributes.getColor(R.styleable.Tooltip_backgroundTint, MaterialColors.layer(ColorUtils.setAlphaComponent(MaterialColors.getColor(this.context, 16842801, cls.getCanonicalName()), 229), ColorUtils.setAlphaComponent(MaterialColors.getColor(this.context, R.attr.colorOnBackground, cls.getCanonicalName()), 153)))));
        setStrokeColor(ColorStateList.valueOf(MaterialColors.getColor(this.context, R.attr.colorSurface, cls.getCanonicalName())));
        this.padding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.Tooltip_android_padding, 0);
        this.minWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.Tooltip_android_minWidth, 0);
        this.minHeight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.Tooltip_android_minHeight, 0);
        this.layoutMargin = obtainStyledAttributes.getDimensionPixelSize(R.styleable.Tooltip_android_layout_margin, 0);
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: private */
    public void updateLocationOnScreen(View v) {
        int[] iArr = new int[2];
        v.getLocationOnScreen(iArr);
        this.locationOnScreenX = iArr[0];
        v.getWindowVisibleDisplayFrame(this.displayFrame);
    }

    public void detachView(View view) {
        if (view != null) {
            view.removeOnLayoutChangeListener(this.attachedViewLayoutChangeListener);
        }
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.scale(this.tooltipScaleX, this.tooltipScaleY, ((float) getBounds().left) + (((float) getBounds().width()) * 0.5f), ((float) getBounds().top) + (((float) getBounds().height()) * this.tooltipPivotY));
        canvas.translate(calculatePointerOffset(), (float) (-((((double) this.arrowSize) * Math.sqrt(2.0d)) - ((double) this.arrowSize))));
        super.draw(canvas);
        drawText(canvas);
        canvas.restore();
    }

    public int getIntrinsicHeight() {
        return (int) Math.max(this.textDrawableHelper.getTextPaint().getTextSize(), (float) this.minHeight);
    }

    public int getIntrinsicWidth() {
        return (int) Math.max(((float) (this.padding * 2)) + getTextWidth(), (float) this.minWidth);
    }

    public int getLayoutMargin() {
        return this.layoutMargin;
    }

    public int getMinHeight() {
        return this.minHeight;
    }

    public int getMinWidth() {
        return this.minWidth;
    }

    public CharSequence getText() {
        return this.text;
    }

    public TextAppearance getTextAppearance() {
        return this.textDrawableHelper.getTextAppearance();
    }

    public int getTextPadding() {
        return this.padding;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        setShapeAppearanceModel(getShapeAppearanceModel().toBuilder().setBottomEdge(createMarkerEdge()).build());
    }

    public boolean onStateChange(int[] state) {
        return super.onStateChange(state);
    }

    public void onTextSizeChange() {
        invalidateSelf();
    }

    public void setLayoutMargin(int layoutMargin2) {
        this.layoutMargin = layoutMargin2;
        invalidateSelf();
    }

    public void setMinHeight(int minHeight2) {
        this.minHeight = minHeight2;
        invalidateSelf();
    }

    public void setMinWidth(int minWidth2) {
        this.minWidth = minWidth2;
        invalidateSelf();
    }

    public void setRelativeToView(View view) {
        if (view != null) {
            updateLocationOnScreen(view);
            view.addOnLayoutChangeListener(this.attachedViewLayoutChangeListener);
        }
    }

    public void setRevealFraction(float fraction) {
        this.tooltipPivotY = 1.2f;
        this.tooltipScaleX = fraction;
        this.tooltipScaleY = fraction;
        this.labelOpacity = AnimationUtils.lerp(0.0f, 1.0f, 0.19f, 1.0f, fraction);
        invalidateSelf();
    }

    public void setText(CharSequence text2) {
        if (!TextUtils.equals(this.text, text2)) {
            this.text = text2;
            this.textDrawableHelper.setTextWidthDirty(true);
            invalidateSelf();
        }
    }

    public void setTextAppearance(TextAppearance textAppearance) {
        this.textDrawableHelper.setTextAppearance(textAppearance, this.context);
    }

    public void setTextAppearanceResource(int id) {
        setTextAppearance(new TextAppearance(this.context, id));
    }

    public void setTextPadding(int padding2) {
        this.padding = padding2;
        invalidateSelf();
    }

    public void setTextResource(int id) {
        setText(this.context.getResources().getString(id));
    }
}

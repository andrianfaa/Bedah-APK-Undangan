package com.google.android.material.internal;

import android.animation.TimeInterpolator;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.math.MathUtils;
import androidx.core.text.TextDirectionHeuristicsCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.StaticLayoutBuilderCompat;
import com.google.android.material.resources.CancelableFontCallback;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.resources.TypefaceUtils;

public final class CollapsingTextHelper {
    private static final boolean DEBUG_DRAW = false;
    private static final Paint DEBUG_DRAW_PAINT = null;
    private static final String ELLIPSIS_NORMAL = "…";
    private static final float FADE_MODE_THRESHOLD_FRACTION_RELATIVE = 0.5f;
    private static final String TAG = "CollapsingTextHelper";
    private static final boolean USE_SCALING_TEXTURE = (Build.VERSION.SDK_INT < 18);
    private boolean boundsChanged;
    private final Rect collapsedBounds;
    private float collapsedDrawX;
    private float collapsedDrawY;
    private CancelableFontCallback collapsedFontCallback;
    private float collapsedLetterSpacing;
    private ColorStateList collapsedShadowColor;
    private float collapsedShadowDx;
    private float collapsedShadowDy;
    private float collapsedShadowRadius;
    private float collapsedTextBlend;
    private ColorStateList collapsedTextColor;
    private int collapsedTextGravity = 16;
    private float collapsedTextSize = 15.0f;
    private float collapsedTextWidth;
    private Typeface collapsedTypeface;
    private Typeface collapsedTypefaceBold;
    private Typeface collapsedTypefaceDefault;
    private final RectF currentBounds;
    private float currentDrawX;
    private float currentDrawY;
    private float currentLetterSpacing;
    private int currentOffsetY;
    private int currentShadowColor;
    private float currentShadowDx;
    private float currentShadowDy;
    private float currentShadowRadius;
    private float currentTextSize;
    private Typeface currentTypeface;
    private boolean drawTitle;
    private final Rect expandedBounds;
    private float expandedDrawX;
    private float expandedDrawY;
    private CancelableFontCallback expandedFontCallback;
    private float expandedFraction;
    private float expandedLetterSpacing;
    private int expandedLineCount;
    private ColorStateList expandedShadowColor;
    private float expandedShadowDx;
    private float expandedShadowDy;
    private float expandedShadowRadius;
    private float expandedTextBlend;
    private ColorStateList expandedTextColor;
    private int expandedTextGravity = 16;
    private float expandedTextSize = 15.0f;
    private Bitmap expandedTitleTexture;
    private Typeface expandedTypeface;
    private Typeface expandedTypefaceBold;
    private Typeface expandedTypefaceDefault;
    private boolean fadeModeEnabled;
    private float fadeModeStartFraction;
    private float fadeModeThresholdFraction;
    private int hyphenationFrequency = StaticLayoutBuilderCompat.DEFAULT_HYPHENATION_FREQUENCY;
    private boolean isRtl;
    private boolean isRtlTextDirectionHeuristicsEnabled = true;
    private float lineSpacingAdd = 0.0f;
    private float lineSpacingMultiplier = 1.0f;
    private int maxLines = 1;
    private TimeInterpolator positionInterpolator;
    private float scale;
    private int[] state;
    private CharSequence text;
    private StaticLayout textLayout;
    private final TextPaint textPaint;
    private TimeInterpolator textSizeInterpolator;
    private CharSequence textToDraw;
    private CharSequence textToDrawCollapsed;
    private Paint texturePaint;
    private final TextPaint tmpPaint;
    private boolean useTexture;
    private final View view;

    public CollapsingTextHelper(View view2) {
        this.view = view2;
        TextPaint textPaint2 = new TextPaint(129);
        this.textPaint = textPaint2;
        this.tmpPaint = new TextPaint(textPaint2);
        this.collapsedBounds = new Rect();
        this.expandedBounds = new Rect();
        this.currentBounds = new RectF();
        this.fadeModeThresholdFraction = calculateFadeModeThresholdFraction();
        maybeUpdateFontWeightAdjustment(view2.getContext().getResources().getConfiguration());
    }

    private static int blendARGB(int color1, int color2, float ratio) {
        float f = 1.0f - ratio;
        return Color.argb(Math.round((((float) Color.alpha(color1)) * f) + (((float) Color.alpha(color2)) * ratio)), Math.round((((float) Color.red(color1)) * f) + (((float) Color.red(color2)) * ratio)), Math.round((((float) Color.green(color1)) * f) + (((float) Color.green(color2)) * ratio)), Math.round((((float) Color.blue(color1)) * f) + (((float) Color.blue(color2)) * ratio)));
    }

    private void calculateBaseOffsets(boolean forceRecalculate) {
        StaticLayout staticLayout;
        calculateUsingTextSize(1.0f, forceRecalculate);
        CharSequence charSequence = this.textToDraw;
        if (!(charSequence == null || (staticLayout = this.textLayout) == null)) {
            this.textToDrawCollapsed = TextUtils.ellipsize(charSequence, this.textPaint, (float) staticLayout.getWidth(), TextUtils.TruncateAt.END);
        }
        CharSequence charSequence2 = this.textToDrawCollapsed;
        float f = 0.0f;
        if (charSequence2 != null) {
            this.collapsedTextWidth = measureTextWidth(this.textPaint, charSequence2);
        } else {
            this.collapsedTextWidth = 0.0f;
        }
        int absoluteGravity = GravityCompat.getAbsoluteGravity(this.collapsedTextGravity, this.isRtl ? 1 : 0);
        switch (absoluteGravity & 112) {
            case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE:
                this.collapsedDrawY = (float) this.collapsedBounds.top;
                break;
            case 80:
                this.collapsedDrawY = ((float) this.collapsedBounds.bottom) + this.textPaint.ascent();
                break;
            default:
                this.collapsedDrawY = ((float) this.collapsedBounds.centerY()) - ((this.textPaint.descent() - this.textPaint.ascent()) / 2.0f);
                break;
        }
        switch (absoluteGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
            case 1:
                this.collapsedDrawX = ((float) this.collapsedBounds.centerX()) - (this.collapsedTextWidth / 2.0f);
                break;
            case 5:
                this.collapsedDrawX = ((float) this.collapsedBounds.right) - this.collapsedTextWidth;
                break;
            default:
                this.collapsedDrawX = (float) this.collapsedBounds.left;
                break;
        }
        calculateUsingTextSize(0.0f, forceRecalculate);
        StaticLayout staticLayout2 = this.textLayout;
        if (staticLayout2 != null) {
            f = (float) staticLayout2.getHeight();
        }
        float f2 = 0.0f;
        StaticLayout staticLayout3 = this.textLayout;
        if (staticLayout3 == null || this.maxLines <= 1) {
            CharSequence charSequence3 = this.textToDraw;
            if (charSequence3 != null) {
                f2 = measureTextWidth(this.textPaint, charSequence3);
            }
        } else {
            f2 = (float) staticLayout3.getWidth();
        }
        StaticLayout staticLayout4 = this.textLayout;
        this.expandedLineCount = staticLayout4 != null ? staticLayout4.getLineCount() : 0;
        int absoluteGravity2 = GravityCompat.getAbsoluteGravity(this.expandedTextGravity, this.isRtl ? 1 : 0);
        switch (absoluteGravity2 & 112) {
            case ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE:
                this.expandedDrawY = (float) this.expandedBounds.top;
                break;
            case 80:
                this.expandedDrawY = (((float) this.expandedBounds.bottom) - f) + this.textPaint.descent();
                break;
            default:
                this.expandedDrawY = ((float) this.expandedBounds.centerY()) - (f / 2.0f);
                break;
        }
        switch (8388615 & absoluteGravity2) {
            case 1:
                this.expandedDrawX = ((float) this.expandedBounds.centerX()) - (f2 / 2.0f);
                break;
            case 5:
                this.expandedDrawX = ((float) this.expandedBounds.right) - f2;
                break;
            default:
                this.expandedDrawX = (float) this.expandedBounds.left;
                break;
        }
        clearTexture();
        setInterpolatedTextSize(this.expandedFraction);
    }

    private void calculateCurrentOffsets() {
        calculateOffsets(this.expandedFraction);
    }

    private float calculateFadeModeTextAlpha(float fraction) {
        float f = this.fadeModeThresholdFraction;
        return fraction <= f ? AnimationUtils.lerp(1.0f, 0.0f, this.fadeModeStartFraction, f, fraction) : AnimationUtils.lerp(0.0f, 1.0f, f, 1.0f, fraction);
    }

    private float calculateFadeModeThresholdFraction() {
        float f = this.fadeModeStartFraction;
        return f + ((1.0f - f) * 0.5f);
    }

    private boolean calculateIsRtl(CharSequence text2) {
        boolean isDefaultIsRtl = isDefaultIsRtl();
        return this.isRtlTextDirectionHeuristicsEnabled ? isTextDirectionHeuristicsIsRtl(text2, isDefaultIsRtl) : isDefaultIsRtl;
    }

    private void calculateOffsets(float fraction) {
        float f;
        interpolateBounds(fraction);
        if (!this.fadeModeEnabled) {
            f = fraction;
            this.currentDrawX = lerp(this.expandedDrawX, this.collapsedDrawX, fraction, this.positionInterpolator);
            this.currentDrawY = lerp(this.expandedDrawY, this.collapsedDrawY, fraction, this.positionInterpolator);
            setInterpolatedTextSize(fraction);
        } else if (fraction < this.fadeModeThresholdFraction) {
            f = 0.0f;
            this.currentDrawX = this.expandedDrawX;
            this.currentDrawY = this.expandedDrawY;
            setInterpolatedTextSize(0.0f);
        } else {
            f = 1.0f;
            this.currentDrawX = this.collapsedDrawX;
            this.currentDrawY = this.collapsedDrawY - ((float) Math.max(0, this.currentOffsetY));
            setInterpolatedTextSize(1.0f);
        }
        setCollapsedTextBlend(1.0f - lerp(0.0f, 1.0f, 1.0f - fraction, AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR));
        setExpandedTextBlend(lerp(1.0f, 0.0f, fraction, AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR));
        if (this.collapsedTextColor != this.expandedTextColor) {
            this.textPaint.setColor(blendARGB(getCurrentExpandedTextColor(), getCurrentCollapsedTextColor(), f));
        } else {
            this.textPaint.setColor(getCurrentCollapsedTextColor());
        }
        if (Build.VERSION.SDK_INT >= 21) {
            float f2 = this.collapsedLetterSpacing;
            float f3 = this.expandedLetterSpacing;
            if (f2 != f3) {
                this.textPaint.setLetterSpacing(lerp(f3, f2, fraction, AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR));
            } else {
                this.textPaint.setLetterSpacing(f2);
            }
        }
        this.currentShadowRadius = lerp(this.expandedShadowRadius, this.collapsedShadowRadius, fraction, (TimeInterpolator) null);
        this.currentShadowDx = lerp(this.expandedShadowDx, this.collapsedShadowDx, fraction, (TimeInterpolator) null);
        this.currentShadowDy = lerp(this.expandedShadowDy, this.collapsedShadowDy, fraction, (TimeInterpolator) null);
        int blendARGB = blendARGB(getCurrentColor(this.expandedShadowColor), getCurrentColor(this.collapsedShadowColor), fraction);
        this.currentShadowColor = blendARGB;
        this.textPaint.setShadowLayer(this.currentShadowRadius, this.currentShadowDx, this.currentShadowDy, blendARGB);
        if (this.fadeModeEnabled) {
            int alpha = this.textPaint.getAlpha();
            this.textPaint.setAlpha((int) (calculateFadeModeTextAlpha(fraction) * ((float) alpha)));
        }
        ViewCompat.postInvalidateOnAnimation(this.view);
    }

    private void calculateUsingTextSize(float fraction) {
        calculateUsingTextSize(fraction, false);
    }

    private void calculateUsingTextSize(float fraction, boolean forceRecalculate) {
        float f;
        float f2;
        float f3;
        if (this.text != null) {
            float width = (float) this.collapsedBounds.width();
            float width2 = (float) this.expandedBounds.width();
            boolean z = false;
            if (isClose(fraction, 1.0f)) {
                f3 = this.collapsedTextSize;
                f2 = this.collapsedLetterSpacing;
                this.scale = 1.0f;
                Typeface typeface = this.currentTypeface;
                Typeface typeface2 = this.collapsedTypeface;
                if (typeface != typeface2) {
                    this.currentTypeface = typeface2;
                    z = true;
                }
                f = width;
            } else {
                f3 = this.expandedTextSize;
                f2 = this.expandedLetterSpacing;
                Typeface typeface3 = this.currentTypeface;
                Typeface typeface4 = this.expandedTypeface;
                if (typeface3 != typeface4) {
                    this.currentTypeface = typeface4;
                    z = true;
                }
                if (isClose(fraction, 0.0f)) {
                    this.scale = 1.0f;
                } else {
                    this.scale = lerp(this.expandedTextSize, this.collapsedTextSize, fraction, this.textSizeInterpolator) / this.expandedTextSize;
                }
                float f4 = this.collapsedTextSize / this.expandedTextSize;
                float f5 = width2 * f4;
                if (forceRecalculate) {
                    f = width2;
                } else {
                    f = f5 > width ? Math.min(width / f4, width2) : width2;
                }
            }
            int i = 1;
            boolean z2 = false;
            if (f > 0.0f) {
                z = ((this.currentTextSize > f3 ? 1 : (this.currentTextSize == f3 ? 0 : -1)) != 0) || ((this.currentLetterSpacing > f2 ? 1 : (this.currentLetterSpacing == f2 ? 0 : -1)) != 0) || this.boundsChanged || z;
                this.currentTextSize = f3;
                this.currentLetterSpacing = f2;
                this.boundsChanged = false;
            }
            if (this.textToDraw == null || z) {
                this.textPaint.setTextSize(this.currentTextSize);
                this.textPaint.setTypeface(this.currentTypeface);
                if (Build.VERSION.SDK_INT >= 21) {
                    this.textPaint.setLetterSpacing(this.currentLetterSpacing);
                }
                TextPaint textPaint2 = this.textPaint;
                if (this.scale != 1.0f) {
                    z2 = true;
                }
                textPaint2.setLinearText(z2);
                this.isRtl = calculateIsRtl(this.text);
                if (shouldDrawMultiline()) {
                    i = this.maxLines;
                }
                StaticLayout createStaticLayout = createStaticLayout(i, f, this.isRtl);
                this.textLayout = createStaticLayout;
                this.textToDraw = createStaticLayout.getText();
            }
        }
    }

    private void clearTexture() {
        Bitmap bitmap = this.expandedTitleTexture;
        if (bitmap != null) {
            bitmap.recycle();
            this.expandedTitleTexture = null;
        }
    }

    private StaticLayout createStaticLayout(int maxLines2, float availableWidth, boolean isRtl2) {
        Layout.Alignment alignment;
        StaticLayout staticLayout = null;
        if (maxLines2 == 1) {
            try {
                alignment = Layout.Alignment.ALIGN_NORMAL;
            } catch (StaticLayoutBuilderCompat.StaticLayoutBuilderCompatException e) {
                Log.e(TAG, e.getCause().getMessage(), e);
            }
        } else {
            alignment = getMultilineTextLayoutAlignment();
        }
        staticLayout = StaticLayoutBuilderCompat.obtain(this.text, this.textPaint, (int) availableWidth).setEllipsize(TextUtils.TruncateAt.END).setIsRtl(isRtl2).setAlignment(alignment).setIncludePad(false).setMaxLines(maxLines2).setLineSpacing(this.lineSpacingAdd, this.lineSpacingMultiplier).setHyphenationFrequency(this.hyphenationFrequency).build();
        return (StaticLayout) Preconditions.checkNotNull(staticLayout);
    }

    private void drawMultilineTransition(Canvas canvas, float currentExpandedX, float y) {
        int alpha = this.textPaint.getAlpha();
        canvas.translate(currentExpandedX, y);
        this.textPaint.setAlpha((int) (this.expandedTextBlend * ((float) alpha)));
        if (Build.VERSION.SDK_INT >= 31) {
            TextPaint textPaint2 = this.textPaint;
            textPaint2.setShadowLayer(this.currentShadowRadius, this.currentShadowDx, this.currentShadowDy, MaterialColors.compositeARGBWithAlpha(this.currentShadowColor, textPaint2.getAlpha()));
        }
        this.textLayout.draw(canvas);
        this.textPaint.setAlpha((int) (this.collapsedTextBlend * ((float) alpha)));
        if (Build.VERSION.SDK_INT >= 31) {
            TextPaint textPaint3 = this.textPaint;
            textPaint3.setShadowLayer(this.currentShadowRadius, this.currentShadowDx, this.currentShadowDy, MaterialColors.compositeARGBWithAlpha(this.currentShadowColor, textPaint3.getAlpha()));
        }
        int lineBaseline = this.textLayout.getLineBaseline(0);
        CharSequence charSequence = this.textToDrawCollapsed;
        canvas.drawText(charSequence, 0, charSequence.length(), 0.0f, (float) lineBaseline, this.textPaint);
        if (Build.VERSION.SDK_INT >= 31) {
            this.textPaint.setShadowLayer(this.currentShadowRadius, this.currentShadowDx, this.currentShadowDy, this.currentShadowColor);
        }
        if (!this.fadeModeEnabled) {
            String trim = this.textToDrawCollapsed.toString().trim();
            if (trim.endsWith(ELLIPSIS_NORMAL)) {
                trim = trim.substring(0, trim.length() - 1);
            }
            this.textPaint.setAlpha(alpha);
            canvas.drawText(trim, 0, Math.min(this.textLayout.getLineEnd(0), trim.length()), 0.0f, (float) lineBaseline, this.textPaint);
        }
    }

    private void ensureExpandedTexture() {
        if (this.expandedTitleTexture == null && !this.expandedBounds.isEmpty() && !TextUtils.isEmpty(this.textToDraw)) {
            calculateOffsets(0.0f);
            int width = this.textLayout.getWidth();
            int height = this.textLayout.getHeight();
            if (width > 0 && height > 0) {
                this.expandedTitleTexture = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                this.textLayout.draw(new Canvas(this.expandedTitleTexture));
                if (this.texturePaint == null) {
                    this.texturePaint = new Paint(3);
                }
            }
        }
    }

    private float getCollapsedTextLeftBound(int width, int gravity) {
        return (gravity == 17 || (gravity & 7) == 1) ? (((float) width) / 2.0f) - (this.collapsedTextWidth / 2.0f) : ((gravity & GravityCompat.END) == 8388613 || (gravity & 5) == 5) ? this.isRtl ? (float) this.collapsedBounds.left : ((float) this.collapsedBounds.right) - this.collapsedTextWidth : this.isRtl ? ((float) this.collapsedBounds.right) - this.collapsedTextWidth : (float) this.collapsedBounds.left;
    }

    private float getCollapsedTextRightBound(RectF bounds, int width, int gravity) {
        return (gravity == 17 || (gravity & 7) == 1) ? (((float) width) / 2.0f) + (this.collapsedTextWidth / 2.0f) : ((gravity & GravityCompat.END) == 8388613 || (gravity & 5) == 5) ? this.isRtl ? bounds.left + this.collapsedTextWidth : (float) this.collapsedBounds.right : this.isRtl ? (float) this.collapsedBounds.right : bounds.left + this.collapsedTextWidth;
    }

    private int getCurrentColor(ColorStateList colorStateList) {
        if (colorStateList == null) {
            return 0;
        }
        int[] iArr = this.state;
        return iArr != null ? colorStateList.getColorForState(iArr, 0) : colorStateList.getDefaultColor();
    }

    private int getCurrentExpandedTextColor() {
        return getCurrentColor(this.expandedTextColor);
    }

    private Layout.Alignment getMultilineTextLayoutAlignment() {
        switch (GravityCompat.getAbsoluteGravity(this.expandedTextGravity, this.isRtl ? 1 : 0) & 7) {
            case 1:
                return Layout.Alignment.ALIGN_CENTER;
            case 5:
                return this.isRtl ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
            default:
                return this.isRtl ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_NORMAL;
        }
    }

    private void getTextPaintCollapsed(TextPaint textPaint2) {
        textPaint2.setTextSize(this.collapsedTextSize);
        textPaint2.setTypeface(this.collapsedTypeface);
        if (Build.VERSION.SDK_INT >= 21) {
            textPaint2.setLetterSpacing(this.collapsedLetterSpacing);
        }
    }

    private void getTextPaintExpanded(TextPaint textPaint2) {
        textPaint2.setTextSize(this.expandedTextSize);
        textPaint2.setTypeface(this.expandedTypeface);
        if (Build.VERSION.SDK_INT >= 21) {
            textPaint2.setLetterSpacing(this.expandedLetterSpacing);
        }
    }

    private void interpolateBounds(float fraction) {
        if (this.fadeModeEnabled) {
            this.currentBounds.set(fraction < this.fadeModeThresholdFraction ? this.expandedBounds : this.collapsedBounds);
            return;
        }
        this.currentBounds.left = lerp((float) this.expandedBounds.left, (float) this.collapsedBounds.left, fraction, this.positionInterpolator);
        this.currentBounds.top = lerp(this.expandedDrawY, this.collapsedDrawY, fraction, this.positionInterpolator);
        this.currentBounds.right = lerp((float) this.expandedBounds.right, (float) this.collapsedBounds.right, fraction, this.positionInterpolator);
        this.currentBounds.bottom = lerp((float) this.expandedBounds.bottom, (float) this.collapsedBounds.bottom, fraction, this.positionInterpolator);
    }

    private static boolean isClose(float value, float targetValue) {
        return Math.abs(value - targetValue) < 1.0E-5f;
    }

    private boolean isDefaultIsRtl() {
        return ViewCompat.getLayoutDirection(this.view) == 1;
    }

    private boolean isTextDirectionHeuristicsIsRtl(CharSequence text2, boolean defaultIsRtl) {
        return (defaultIsRtl ? TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL : TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR).isRtl(text2, 0, text2.length());
    }

    private static float lerp(float startValue, float endValue, float fraction, TimeInterpolator interpolator) {
        if (interpolator != null) {
            fraction = interpolator.getInterpolation(fraction);
        }
        return AnimationUtils.lerp(startValue, endValue, fraction);
    }

    private float measureTextWidth(TextPaint textPaint2, CharSequence textToDraw2) {
        return textPaint2.measureText(textToDraw2, 0, textToDraw2.length());
    }

    private static boolean rectEquals(Rect r, int left, int top, int right, int bottom) {
        return r.left == left && r.top == top && r.right == right && r.bottom == bottom;
    }

    private void setCollapsedTextBlend(float blend) {
        this.collapsedTextBlend = blend;
        ViewCompat.postInvalidateOnAnimation(this.view);
    }

    private boolean setCollapsedTypefaceInternal(Typeface typeface) {
        CancelableFontCallback cancelableFontCallback = this.collapsedFontCallback;
        if (cancelableFontCallback != null) {
            cancelableFontCallback.cancel();
        }
        if (this.collapsedTypefaceDefault == typeface) {
            return false;
        }
        this.collapsedTypefaceDefault = typeface;
        Typeface maybeCopyWithFontWeightAdjustment = TypefaceUtils.maybeCopyWithFontWeightAdjustment(this.view.getContext().getResources().getConfiguration(), typeface);
        this.collapsedTypefaceBold = maybeCopyWithFontWeightAdjustment;
        if (maybeCopyWithFontWeightAdjustment == null) {
            maybeCopyWithFontWeightAdjustment = this.collapsedTypefaceDefault;
        }
        this.collapsedTypeface = maybeCopyWithFontWeightAdjustment;
        return true;
    }

    private void setExpandedTextBlend(float blend) {
        this.expandedTextBlend = blend;
        ViewCompat.postInvalidateOnAnimation(this.view);
    }

    private boolean setExpandedTypefaceInternal(Typeface typeface) {
        CancelableFontCallback cancelableFontCallback = this.expandedFontCallback;
        if (cancelableFontCallback != null) {
            cancelableFontCallback.cancel();
        }
        if (this.expandedTypefaceDefault == typeface) {
            return false;
        }
        this.expandedTypefaceDefault = typeface;
        Typeface maybeCopyWithFontWeightAdjustment = TypefaceUtils.maybeCopyWithFontWeightAdjustment(this.view.getContext().getResources().getConfiguration(), typeface);
        this.expandedTypefaceBold = maybeCopyWithFontWeightAdjustment;
        if (maybeCopyWithFontWeightAdjustment == null) {
            maybeCopyWithFontWeightAdjustment = this.expandedTypefaceDefault;
        }
        this.expandedTypeface = maybeCopyWithFontWeightAdjustment;
        return true;
    }

    private void setInterpolatedTextSize(float fraction) {
        calculateUsingTextSize(fraction);
        boolean z = USE_SCALING_TEXTURE && this.scale != 1.0f;
        this.useTexture = z;
        if (z) {
            ensureExpandedTexture();
        }
        ViewCompat.postInvalidateOnAnimation(this.view);
    }

    private boolean shouldDrawMultiline() {
        return this.maxLines > 1 && (!this.isRtl || this.fadeModeEnabled) && !this.useTexture;
    }

    public void draw(Canvas canvas) {
        int save = canvas.save();
        if (this.textToDraw != null && this.drawTitle) {
            this.textPaint.setTextSize(this.currentTextSize);
            float f = this.currentDrawX;
            float f2 = this.currentDrawY;
            boolean z = this.useTexture && this.expandedTitleTexture != null;
            float f3 = this.scale;
            if (f3 != 1.0f && !this.fadeModeEnabled) {
                canvas.scale(f3, f3, f, f2);
            }
            if (z) {
                canvas.drawBitmap(this.expandedTitleTexture, f, f2, this.texturePaint);
                canvas.restoreToCount(save);
                return;
            }
            if (!shouldDrawMultiline() || (this.fadeModeEnabled && this.expandedFraction <= this.fadeModeThresholdFraction)) {
                canvas.translate(f, f2);
                this.textLayout.draw(canvas);
            } else {
                drawMultilineTransition(canvas, this.currentDrawX - ((float) this.textLayout.getLineStart(0)), f2);
            }
            canvas.restoreToCount(save);
        }
    }

    public void getCollapsedTextActualBounds(RectF bounds, int labelWidth, int textGravity) {
        this.isRtl = calculateIsRtl(this.text);
        bounds.left = getCollapsedTextLeftBound(labelWidth, textGravity);
        bounds.top = (float) this.collapsedBounds.top;
        bounds.right = getCollapsedTextRightBound(bounds, labelWidth, textGravity);
        bounds.bottom = ((float) this.collapsedBounds.top) + getCollapsedTextHeight();
    }

    public ColorStateList getCollapsedTextColor() {
        return this.collapsedTextColor;
    }

    public int getCollapsedTextGravity() {
        return this.collapsedTextGravity;
    }

    public float getCollapsedTextHeight() {
        getTextPaintCollapsed(this.tmpPaint);
        return -this.tmpPaint.ascent();
    }

    public float getCollapsedTextSize() {
        return this.collapsedTextSize;
    }

    public Typeface getCollapsedTypeface() {
        Typeface typeface = this.collapsedTypeface;
        return typeface != null ? typeface : Typeface.DEFAULT;
    }

    public int getCurrentCollapsedTextColor() {
        return getCurrentColor(this.collapsedTextColor);
    }

    public int getExpandedLineCount() {
        return this.expandedLineCount;
    }

    public ColorStateList getExpandedTextColor() {
        return this.expandedTextColor;
    }

    public float getExpandedTextFullHeight() {
        getTextPaintExpanded(this.tmpPaint);
        return (-this.tmpPaint.ascent()) + this.tmpPaint.descent();
    }

    public int getExpandedTextGravity() {
        return this.expandedTextGravity;
    }

    public float getExpandedTextHeight() {
        getTextPaintExpanded(this.tmpPaint);
        return -this.tmpPaint.ascent();
    }

    public float getExpandedTextSize() {
        return this.expandedTextSize;
    }

    public Typeface getExpandedTypeface() {
        Typeface typeface = this.expandedTypeface;
        return typeface != null ? typeface : Typeface.DEFAULT;
    }

    public float getExpansionFraction() {
        return this.expandedFraction;
    }

    public float getFadeModeThresholdFraction() {
        return this.fadeModeThresholdFraction;
    }

    public int getHyphenationFrequency() {
        return this.hyphenationFrequency;
    }

    public int getLineCount() {
        StaticLayout staticLayout = this.textLayout;
        if (staticLayout != null) {
            return staticLayout.getLineCount();
        }
        return 0;
    }

    public float getLineSpacingAdd() {
        return this.textLayout.getSpacingAdd();
    }

    public float getLineSpacingMultiplier() {
        return this.textLayout.getSpacingMultiplier();
    }

    public int getMaxLines() {
        return this.maxLines;
    }

    public TimeInterpolator getPositionInterpolator() {
        return this.positionInterpolator;
    }

    public CharSequence getText() {
        return this.text;
    }

    public boolean isRtlTextDirectionHeuristicsEnabled() {
        return this.isRtlTextDirectionHeuristicsEnabled;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000a, code lost:
        r0 = r1.expandedTextColor;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean isStateful() {
        /*
            r1 = this;
            android.content.res.ColorStateList r0 = r1.collapsedTextColor
            if (r0 == 0) goto L_0x000a
            boolean r0 = r0.isStateful()
            if (r0 != 0) goto L_0x0014
        L_0x000a:
            android.content.res.ColorStateList r0 = r1.expandedTextColor
            if (r0 == 0) goto L_0x0016
            boolean r0 = r0.isStateful()
            if (r0 == 0) goto L_0x0016
        L_0x0014:
            r0 = 1
            goto L_0x0017
        L_0x0016:
            r0 = 0
        L_0x0017:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.internal.CollapsingTextHelper.isStateful():boolean");
    }

    public void maybeUpdateFontWeightAdjustment(Configuration configuration) {
        if (Build.VERSION.SDK_INT >= 31) {
            Typeface typeface = this.collapsedTypefaceDefault;
            if (typeface != null) {
                this.collapsedTypefaceBold = TypefaceUtils.maybeCopyWithFontWeightAdjustment(configuration, typeface);
            }
            Typeface typeface2 = this.expandedTypefaceDefault;
            if (typeface2 != null) {
                this.expandedTypefaceBold = TypefaceUtils.maybeCopyWithFontWeightAdjustment(configuration, typeface2);
            }
            Typeface typeface3 = this.collapsedTypefaceBold;
            if (typeface3 == null) {
                typeface3 = this.collapsedTypefaceDefault;
            }
            this.collapsedTypeface = typeface3;
            Typeface typeface4 = this.expandedTypefaceBold;
            if (typeface4 == null) {
                typeface4 = this.expandedTypefaceDefault;
            }
            this.expandedTypeface = typeface4;
            recalculate(true);
        }
    }

    /* access modifiers changed from: package-private */
    public void onBoundsChanged() {
        this.drawTitle = this.collapsedBounds.width() > 0 && this.collapsedBounds.height() > 0 && this.expandedBounds.width() > 0 && this.expandedBounds.height() > 0;
    }

    public void recalculate() {
        recalculate(false);
    }

    public void recalculate(boolean forceRecalculate) {
        if ((this.view.getHeight() > 0 && this.view.getWidth() > 0) || forceRecalculate) {
            calculateBaseOffsets(forceRecalculate);
            calculateCurrentOffsets();
        }
    }

    public void setCollapsedBounds(int left, int top, int right, int bottom) {
        if (!rectEquals(this.collapsedBounds, left, top, right, bottom)) {
            this.collapsedBounds.set(left, top, right, bottom);
            this.boundsChanged = true;
            onBoundsChanged();
        }
    }

    public void setCollapsedBounds(Rect bounds) {
        setCollapsedBounds(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    public void setCollapsedTextAppearance(int resId) {
        TextAppearance textAppearance = new TextAppearance(this.view.getContext(), resId);
        if (textAppearance.getTextColor() != null) {
            this.collapsedTextColor = textAppearance.getTextColor();
        }
        if (textAppearance.getTextSize() != 0.0f) {
            this.collapsedTextSize = textAppearance.getTextSize();
        }
        if (textAppearance.shadowColor != null) {
            this.collapsedShadowColor = textAppearance.shadowColor;
        }
        this.collapsedShadowDx = textAppearance.shadowDx;
        this.collapsedShadowDy = textAppearance.shadowDy;
        this.collapsedShadowRadius = textAppearance.shadowRadius;
        this.collapsedLetterSpacing = textAppearance.letterSpacing;
        CancelableFontCallback cancelableFontCallback = this.collapsedFontCallback;
        if (cancelableFontCallback != null) {
            cancelableFontCallback.cancel();
        }
        this.collapsedFontCallback = new CancelableFontCallback(new CancelableFontCallback.ApplyFont() {
            public void apply(Typeface font) {
                CollapsingTextHelper.this.setCollapsedTypeface(font);
            }
        }, textAppearance.getFallbackFont());
        textAppearance.getFontAsync(this.view.getContext(), this.collapsedFontCallback);
        recalculate();
    }

    public void setCollapsedTextColor(ColorStateList textColor) {
        if (this.collapsedTextColor != textColor) {
            this.collapsedTextColor = textColor;
            recalculate();
        }
    }

    public void setCollapsedTextGravity(int gravity) {
        if (this.collapsedTextGravity != gravity) {
            this.collapsedTextGravity = gravity;
            recalculate();
        }
    }

    public void setCollapsedTextSize(float textSize) {
        if (this.collapsedTextSize != textSize) {
            this.collapsedTextSize = textSize;
            recalculate();
        }
    }

    public void setCollapsedTypeface(Typeface typeface) {
        if (setCollapsedTypefaceInternal(typeface)) {
            recalculate();
        }
    }

    public void setCurrentOffsetY(int currentOffsetY2) {
        this.currentOffsetY = currentOffsetY2;
    }

    public void setExpandedBounds(int left, int top, int right, int bottom) {
        if (!rectEquals(this.expandedBounds, left, top, right, bottom)) {
            this.expandedBounds.set(left, top, right, bottom);
            this.boundsChanged = true;
            onBoundsChanged();
        }
    }

    public void setExpandedBounds(Rect bounds) {
        setExpandedBounds(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    public void setExpandedLetterSpacing(float letterSpacing) {
        if (this.expandedLetterSpacing != letterSpacing) {
            this.expandedLetterSpacing = letterSpacing;
            recalculate();
        }
    }

    public void setExpandedTextAppearance(int resId) {
        TextAppearance textAppearance = new TextAppearance(this.view.getContext(), resId);
        if (textAppearance.getTextColor() != null) {
            this.expandedTextColor = textAppearance.getTextColor();
        }
        if (textAppearance.getTextSize() != 0.0f) {
            this.expandedTextSize = textAppearance.getTextSize();
        }
        if (textAppearance.shadowColor != null) {
            this.expandedShadowColor = textAppearance.shadowColor;
        }
        this.expandedShadowDx = textAppearance.shadowDx;
        this.expandedShadowDy = textAppearance.shadowDy;
        this.expandedShadowRadius = textAppearance.shadowRadius;
        this.expandedLetterSpacing = textAppearance.letterSpacing;
        CancelableFontCallback cancelableFontCallback = this.expandedFontCallback;
        if (cancelableFontCallback != null) {
            cancelableFontCallback.cancel();
        }
        this.expandedFontCallback = new CancelableFontCallback(new CancelableFontCallback.ApplyFont() {
            public void apply(Typeface font) {
                CollapsingTextHelper.this.setExpandedTypeface(font);
            }
        }, textAppearance.getFallbackFont());
        textAppearance.getFontAsync(this.view.getContext(), this.expandedFontCallback);
        recalculate();
    }

    public void setExpandedTextColor(ColorStateList textColor) {
        if (this.expandedTextColor != textColor) {
            this.expandedTextColor = textColor;
            recalculate();
        }
    }

    public void setExpandedTextGravity(int gravity) {
        if (this.expandedTextGravity != gravity) {
            this.expandedTextGravity = gravity;
            recalculate();
        }
    }

    public void setExpandedTextSize(float textSize) {
        if (this.expandedTextSize != textSize) {
            this.expandedTextSize = textSize;
            recalculate();
        }
    }

    public void setExpandedTypeface(Typeface typeface) {
        if (setExpandedTypefaceInternal(typeface)) {
            recalculate();
        }
    }

    public void setExpansionFraction(float fraction) {
        float fraction2 = MathUtils.clamp(fraction, 0.0f, 1.0f);
        if (fraction2 != this.expandedFraction) {
            this.expandedFraction = fraction2;
            calculateCurrentOffsets();
        }
    }

    public void setFadeModeEnabled(boolean fadeModeEnabled2) {
        this.fadeModeEnabled = fadeModeEnabled2;
    }

    public void setFadeModeStartFraction(float fadeModeStartFraction2) {
        this.fadeModeStartFraction = fadeModeStartFraction2;
        this.fadeModeThresholdFraction = calculateFadeModeThresholdFraction();
    }

    public void setHyphenationFrequency(int hyphenationFrequency2) {
        this.hyphenationFrequency = hyphenationFrequency2;
    }

    public void setLineSpacingAdd(float spacingAdd) {
        this.lineSpacingAdd = spacingAdd;
    }

    public void setLineSpacingMultiplier(float spacingMultiplier) {
        this.lineSpacingMultiplier = spacingMultiplier;
    }

    public void setMaxLines(int maxLines2) {
        if (maxLines2 != this.maxLines) {
            this.maxLines = maxLines2;
            clearTexture();
            recalculate();
        }
    }

    public void setPositionInterpolator(TimeInterpolator interpolator) {
        this.positionInterpolator = interpolator;
        recalculate();
    }

    public void setRtlTextDirectionHeuristicsEnabled(boolean rtlTextDirectionHeuristicsEnabled) {
        this.isRtlTextDirectionHeuristicsEnabled = rtlTextDirectionHeuristicsEnabled;
    }

    public final boolean setState(int[] state2) {
        this.state = state2;
        if (!isStateful()) {
            return false;
        }
        recalculate();
        return true;
    }

    public void setText(CharSequence text2) {
        if (text2 == null || !TextUtils.equals(this.text, text2)) {
            this.text = text2;
            this.textToDraw = null;
            clearTexture();
            recalculate();
        }
    }

    public void setTextSizeInterpolator(TimeInterpolator interpolator) {
        this.textSizeInterpolator = interpolator;
        recalculate();
    }

    public void setTypefaces(Typeface typeface) {
        boolean collapsedTypefaceInternal = setCollapsedTypefaceInternal(typeface);
        boolean expandedTypefaceInternal = setExpandedTypefaceInternal(typeface);
        if (collapsedTypefaceInternal || expandedTypefaceInternal) {
            recalculate();
        }
    }
}

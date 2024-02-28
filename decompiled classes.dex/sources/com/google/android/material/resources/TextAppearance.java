package com.google.android.material.resources;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;

public class TextAppearance {
    private static final String TAG = "TextAppearance";
    private static final int TYPEFACE_MONOSPACE = 3;
    private static final int TYPEFACE_SANS = 1;
    private static final int TYPEFACE_SERIF = 2;
    /* access modifiers changed from: private */
    public Typeface font;
    public final String fontFamily;
    private final int fontFamilyResourceId;
    /* access modifiers changed from: private */
    public boolean fontResolved = false;
    public final boolean hasLetterSpacing;
    public final float letterSpacing;
    public final ColorStateList shadowColor;
    public final float shadowDx;
    public final float shadowDy;
    public final float shadowRadius;
    public final boolean textAllCaps;
    private ColorStateList textColor;
    public final ColorStateList textColorHint;
    public final ColorStateList textColorLink;
    private float textSize;
    public final int textStyle;
    public final int typeface;

    public TextAppearance(Context context, int id) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(id, R.styleable.TextAppearance);
        setTextSize(obtainStyledAttributes.getDimension(R.styleable.TextAppearance_android_textSize, 0.0f));
        setTextColor(MaterialResources.getColorStateList(context, obtainStyledAttributes, R.styleable.TextAppearance_android_textColor));
        this.textColorHint = MaterialResources.getColorStateList(context, obtainStyledAttributes, R.styleable.TextAppearance_android_textColorHint);
        this.textColorLink = MaterialResources.getColorStateList(context, obtainStyledAttributes, R.styleable.TextAppearance_android_textColorLink);
        this.textStyle = obtainStyledAttributes.getInt(R.styleable.TextAppearance_android_textStyle, 0);
        this.typeface = obtainStyledAttributes.getInt(R.styleable.TextAppearance_android_typeface, 1);
        int indexWithValue = MaterialResources.getIndexWithValue(obtainStyledAttributes, R.styleable.TextAppearance_fontFamily, R.styleable.TextAppearance_android_fontFamily);
        this.fontFamilyResourceId = obtainStyledAttributes.getResourceId(indexWithValue, 0);
        this.fontFamily = obtainStyledAttributes.getString(indexWithValue);
        this.textAllCaps = obtainStyledAttributes.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
        this.shadowColor = MaterialResources.getColorStateList(context, obtainStyledAttributes, R.styleable.TextAppearance_android_shadowColor);
        this.shadowDx = obtainStyledAttributes.getFloat(R.styleable.TextAppearance_android_shadowDx, 0.0f);
        this.shadowDy = obtainStyledAttributes.getFloat(R.styleable.TextAppearance_android_shadowDy, 0.0f);
        this.shadowRadius = obtainStyledAttributes.getFloat(R.styleable.TextAppearance_android_shadowRadius, 0.0f);
        obtainStyledAttributes.recycle();
        if (Build.VERSION.SDK_INT >= 21) {
            TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(id, R.styleable.MaterialTextAppearance);
            this.hasLetterSpacing = obtainStyledAttributes2.hasValue(R.styleable.MaterialTextAppearance_android_letterSpacing);
            this.letterSpacing = obtainStyledAttributes2.getFloat(R.styleable.MaterialTextAppearance_android_letterSpacing, 0.0f);
            obtainStyledAttributes2.recycle();
            return;
        }
        this.hasLetterSpacing = false;
        this.letterSpacing = 0.0f;
    }

    private void createFallbackFont() {
        String str;
        if (this.font == null && (str = this.fontFamily) != null) {
            this.font = Typeface.create(str, this.textStyle);
        }
        if (this.font == null) {
            switch (this.typeface) {
                case 1:
                    this.font = Typeface.SANS_SERIF;
                    break;
                case 2:
                    this.font = Typeface.SERIF;
                    break;
                case 3:
                    this.font = Typeface.MONOSPACE;
                    break;
                default:
                    this.font = Typeface.DEFAULT;
                    break;
            }
            this.font = Typeface.create(this.font, this.textStyle);
        }
    }

    private boolean shouldLoadFontSynchronously(Context context) {
        if (TextAppearanceConfig.shouldLoadFontSynchronously()) {
            return true;
        }
        int i = this.fontFamilyResourceId;
        return (i != 0 ? ResourcesCompat.getCachedFont(context, i) : null) != null;
    }

    public Typeface getFallbackFont() {
        createFallbackFont();
        return this.font;
    }

    public Typeface getFont(Context context) {
        if (this.fontResolved) {
            return this.font;
        }
        if (!context.isRestricted()) {
            try {
                Typeface font2 = ResourcesCompat.getFont(context, this.fontFamilyResourceId);
                this.font = font2;
                if (font2 != null) {
                    this.font = Typeface.create(font2, this.textStyle);
                }
            } catch (Resources.NotFoundException | UnsupportedOperationException e) {
            } catch (Exception e2) {
                Log.d(TAG, "Error loading font " + this.fontFamily, e2);
            }
        }
        createFallbackFont();
        this.fontResolved = true;
        return this.font;
    }

    public void getFontAsync(final Context context, final TextPaint textPaint, final TextAppearanceFontCallback callback) {
        updateTextPaintMeasureState(context, textPaint, getFallbackFont());
        getFontAsync(context, new TextAppearanceFontCallback() {
            public void onFontRetrievalFailed(int i) {
                callback.onFontRetrievalFailed(i);
            }

            public void onFontRetrieved(Typeface typeface, boolean fontResolvedSynchronously) {
                TextAppearance.this.updateTextPaintMeasureState(context, textPaint, typeface);
                callback.onFontRetrieved(typeface, fontResolvedSynchronously);
            }
        });
    }

    public void getFontAsync(Context context, final TextAppearanceFontCallback callback) {
        if (shouldLoadFontSynchronously(context)) {
            getFont(context);
        } else {
            createFallbackFont();
        }
        int i = this.fontFamilyResourceId;
        if (i == 0) {
            this.fontResolved = true;
        }
        if (this.fontResolved) {
            callback.onFontRetrieved(this.font, true);
            return;
        }
        try {
            ResourcesCompat.getFont(context, i, new ResourcesCompat.FontCallback() {
                public void onFontRetrievalFailed(int reason) {
                    boolean unused = TextAppearance.this.fontResolved = true;
                    callback.onFontRetrievalFailed(reason);
                }

                public void onFontRetrieved(Typeface typeface) {
                    TextAppearance textAppearance = TextAppearance.this;
                    Typeface unused = textAppearance.font = Typeface.create(typeface, textAppearance.textStyle);
                    boolean unused2 = TextAppearance.this.fontResolved = true;
                    callback.onFontRetrieved(TextAppearance.this.font, false);
                }
            }, (Handler) null);
        } catch (Resources.NotFoundException e) {
            this.fontResolved = true;
            callback.onFontRetrievalFailed(1);
        } catch (Exception e2) {
            Log.d(TAG, "Error loading font " + this.fontFamily, e2);
            this.fontResolved = true;
            callback.onFontRetrievalFailed(-3);
        }
    }

    public ColorStateList getTextColor() {
        return this.textColor;
    }

    public float getTextSize() {
        return this.textSize;
    }

    public void setTextColor(ColorStateList textColor2) {
        this.textColor = textColor2;
    }

    public void setTextSize(float textSize2) {
        this.textSize = textSize2;
    }

    public void updateDrawState(Context context, TextPaint textPaint, TextAppearanceFontCallback callback) {
        updateMeasureState(context, textPaint, callback);
        ColorStateList colorStateList = this.textColor;
        textPaint.setColor(colorStateList != null ? colorStateList.getColorForState(textPaint.drawableState, this.textColor.getDefaultColor()) : ViewCompat.MEASURED_STATE_MASK);
        float f = this.shadowRadius;
        float f2 = this.shadowDx;
        float f3 = this.shadowDy;
        ColorStateList colorStateList2 = this.shadowColor;
        textPaint.setShadowLayer(f, f2, f3, colorStateList2 != null ? colorStateList2.getColorForState(textPaint.drawableState, this.shadowColor.getDefaultColor()) : 0);
    }

    public void updateMeasureState(Context context, TextPaint textPaint, TextAppearanceFontCallback callback) {
        if (shouldLoadFontSynchronously(context)) {
            updateTextPaintMeasureState(context, textPaint, getFont(context));
        } else {
            getFontAsync(context, textPaint, callback);
        }
    }

    public void updateTextPaintMeasureState(Context context, TextPaint textPaint, Typeface typeface2) {
        Typeface maybeCopyWithFontWeightAdjustment = TypefaceUtils.maybeCopyWithFontWeightAdjustment(context, typeface2);
        if (maybeCopyWithFontWeightAdjustment != null) {
            typeface2 = maybeCopyWithFontWeightAdjustment;
        }
        textPaint.setTypeface(typeface2);
        int i = this.textStyle & (~typeface2.getStyle());
        textPaint.setFakeBoldText((i & 1) != 0);
        textPaint.setTextSkewX((i & 2) != 0 ? -0.25f : 0.0f);
        textPaint.setTextSize(this.textSize);
        if (Build.VERSION.SDK_INT >= 21 && this.hasLetterSpacing) {
            textPaint.setLetterSpacing(this.letterSpacing);
        }
    }
}

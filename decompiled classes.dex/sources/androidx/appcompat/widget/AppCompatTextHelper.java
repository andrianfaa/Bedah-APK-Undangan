package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.LocaleList;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.widget.AutoSizeableTextView;
import androidx.core.widget.TextViewCompat;
import java.lang.ref.WeakReference;
import java.util.Locale;

class AppCompatTextHelper {
    private static final int MONOSPACE = 3;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int TEXT_FONT_WEIGHT_UNSPECIFIED = -1;
    private boolean mAsyncFontPending;
    private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
    private TintInfo mDrawableBottomTint;
    private TintInfo mDrawableEndTint;
    private TintInfo mDrawableLeftTint;
    private TintInfo mDrawableRightTint;
    private TintInfo mDrawableStartTint;
    private TintInfo mDrawableTint;
    private TintInfo mDrawableTopTint;
    private Typeface mFontTypeface;
    private int mFontWeight = -1;
    private int mStyle = 0;
    private final TextView mView;

    static class Api17Impl {
        private Api17Impl() {
        }

        static Drawable[] getCompoundDrawablesRelative(TextView textView) {
            return textView.getCompoundDrawablesRelative();
        }

        static void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView, Drawable start, Drawable top, Drawable end, Drawable bottom) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);
        }

        static void setTextLocale(TextView textView, Locale locale) {
            textView.setTextLocale(locale);
        }
    }

    static class Api21Impl {
        private Api21Impl() {
        }

        static Locale forLanguageTag(String languageTag) {
            return Locale.forLanguageTag(languageTag);
        }
    }

    static class Api24Impl {
        private Api24Impl() {
        }

        static LocaleList forLanguageTags(String list) {
            return LocaleList.forLanguageTags(list);
        }

        static void setTextLocales(TextView textView, LocaleList locales) {
            textView.setTextLocales(locales);
        }
    }

    static class Api26Impl {
        private Api26Impl() {
        }

        static int getAutoSizeStepGranularity(TextView textView) {
            return textView.getAutoSizeStepGranularity();
        }

        static void setAutoSizeTextTypeUniformWithConfiguration(TextView textView, int autoSizeMinTextSize, int autoSizeMaxTextSize, int autoSizeStepGranularity, int unit) {
            textView.setAutoSizeTextTypeUniformWithConfiguration(autoSizeMinTextSize, autoSizeMaxTextSize, autoSizeStepGranularity, unit);
        }

        static void setAutoSizeTextTypeUniformWithPresetSizes(TextView textView, int[] presetSizes, int unit) {
            textView.setAutoSizeTextTypeUniformWithPresetSizes(presetSizes, unit);
        }

        static boolean setFontVariationSettings(TextView textView, String fontVariationSettings) {
            return textView.setFontVariationSettings(fontVariationSettings);
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static Typeface create(Typeface family, int weight, boolean italic) {
            return Typeface.create(family, weight, italic);
        }
    }

    AppCompatTextHelper(TextView view) {
        this.mView = view;
        this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(view);
    }

    private void applyCompoundDrawableTint(Drawable drawable, TintInfo info) {
        if (drawable != null && info != null) {
            AppCompatDrawableManager.tintDrawable(drawable, info, this.mView.getDrawableState());
        }
    }

    private static TintInfo createTintInfo(Context context, AppCompatDrawableManager drawableManager, int drawableId) {
        ColorStateList tintList = drawableManager.getTintList(context, drawableId);
        if (tintList == null) {
            return null;
        }
        TintInfo tintInfo = new TintInfo();
        tintInfo.mHasTintList = true;
        tintInfo.mTintList = tintList;
        return tintInfo;
    }

    private void setCompoundDrawables(Drawable drawableLeft, Drawable drawableTop, Drawable drawableRight, Drawable drawableBottom, Drawable drawableStart, Drawable drawableEnd) {
        if (Build.VERSION.SDK_INT >= 17 && (drawableStart != null || drawableEnd != null)) {
            Drawable[] compoundDrawablesRelative = Api17Impl.getCompoundDrawablesRelative(this.mView);
            Api17Impl.setCompoundDrawablesRelativeWithIntrinsicBounds(this.mView, drawableStart != null ? drawableStart : compoundDrawablesRelative[0], drawableTop != null ? drawableTop : compoundDrawablesRelative[1], drawableEnd != null ? drawableEnd : compoundDrawablesRelative[2], drawableBottom != null ? drawableBottom : compoundDrawablesRelative[3]);
        } else if (drawableLeft != null || drawableTop != null || drawableRight != null || drawableBottom != null) {
            if (Build.VERSION.SDK_INT >= 17) {
                Drawable[] compoundDrawablesRelative2 = Api17Impl.getCompoundDrawablesRelative(this.mView);
                if (!(compoundDrawablesRelative2[0] == null && compoundDrawablesRelative2[2] == null)) {
                    Api17Impl.setCompoundDrawablesRelativeWithIntrinsicBounds(this.mView, compoundDrawablesRelative2[0], drawableTop != null ? drawableTop : compoundDrawablesRelative2[1], compoundDrawablesRelative2[2], drawableBottom != null ? drawableBottom : compoundDrawablesRelative2[3]);
                    return;
                }
            }
            Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
            this.mView.setCompoundDrawablesWithIntrinsicBounds(drawableLeft != null ? drawableLeft : compoundDrawables[0], drawableTop != null ? drawableTop : compoundDrawables[1], drawableRight != null ? drawableRight : compoundDrawables[2], drawableBottom != null ? drawableBottom : compoundDrawables[3]);
        }
    }

    private void setCompoundTints() {
        TintInfo tintInfo = this.mDrawableTint;
        this.mDrawableLeftTint = tintInfo;
        this.mDrawableTopTint = tintInfo;
        this.mDrawableRightTint = tintInfo;
        this.mDrawableBottomTint = tintInfo;
        this.mDrawableStartTint = tintInfo;
        this.mDrawableEndTint = tintInfo;
    }

    private void setTextSizeInternal(int unit, float size) {
        this.mAutoSizeTextHelper.setTextSizeInternal(unit, size);
    }

    private void updateTypefaceAndStyle(Context context, TintTypedArray a) {
        String string;
        this.mStyle = a.getInt(R.styleable.TextAppearance_android_textStyle, this.mStyle);
        boolean z = false;
        if (Build.VERSION.SDK_INT >= 28) {
            int i = a.getInt(R.styleable.TextAppearance_android_textFontWeight, -1);
            this.mFontWeight = i;
            if (i != -1) {
                this.mStyle = (this.mStyle & 2) | 0;
            }
        }
        if (a.hasValue(R.styleable.TextAppearance_android_fontFamily) || a.hasValue(R.styleable.TextAppearance_fontFamily)) {
            this.mFontTypeface = null;
            int i2 = a.hasValue(R.styleable.TextAppearance_fontFamily) ? R.styleable.TextAppearance_fontFamily : R.styleable.TextAppearance_android_fontFamily;
            final int i3 = this.mFontWeight;
            final int i4 = this.mStyle;
            if (!context.isRestricted()) {
                final WeakReference weakReference = new WeakReference(this.mView);
                try {
                    Typeface font = a.getFont(i2, this.mStyle, new ResourcesCompat.FontCallback() {
                        public void onFontRetrievalFailed(int reason) {
                        }

                        public void onFontRetrieved(Typeface typeface) {
                            int i;
                            if (Build.VERSION.SDK_INT >= 28 && (i = i3) != -1) {
                                typeface = Api28Impl.create(typeface, i, (i4 & 2) != 0);
                            }
                            AppCompatTextHelper.this.onAsyncTypefaceReceived(weakReference, typeface);
                        }
                    });
                    if (font != null) {
                        if (Build.VERSION.SDK_INT < 28 || this.mFontWeight == -1) {
                            this.mFontTypeface = font;
                        } else {
                            this.mFontTypeface = Api28Impl.create(Typeface.create(font, 0), this.mFontWeight, (this.mStyle & 2) != 0);
                        }
                    }
                    this.mAsyncFontPending = this.mFontTypeface == null;
                } catch (Resources.NotFoundException | UnsupportedOperationException e) {
                }
            }
            if (this.mFontTypeface == null && (string = a.getString(i2)) != null) {
                if (Build.VERSION.SDK_INT < 28 || this.mFontWeight == -1) {
                    this.mFontTypeface = Typeface.create(string, this.mStyle);
                    return;
                }
                Typeface create = Typeface.create(string, 0);
                int i5 = this.mFontWeight;
                if ((this.mStyle & 2) != 0) {
                    z = true;
                }
                this.mFontTypeface = Api28Impl.create(create, i5, z);
            }
        } else if (a.hasValue(R.styleable.TextAppearance_android_typeface)) {
            this.mAsyncFontPending = false;
            switch (a.getInt(R.styleable.TextAppearance_android_typeface, 1)) {
                case 1:
                    this.mFontTypeface = Typeface.SANS_SERIF;
                    return;
                case 2:
                    this.mFontTypeface = Typeface.SERIF;
                    return;
                case 3:
                    this.mFontTypeface = Typeface.MONOSPACE;
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void applyCompoundDrawablesTints() {
        if (!(this.mDrawableLeftTint == null && this.mDrawableTopTint == null && this.mDrawableRightTint == null && this.mDrawableBottomTint == null)) {
            Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
            applyCompoundDrawableTint(compoundDrawables[0], this.mDrawableLeftTint);
            applyCompoundDrawableTint(compoundDrawables[1], this.mDrawableTopTint);
            applyCompoundDrawableTint(compoundDrawables[2], this.mDrawableRightTint);
            applyCompoundDrawableTint(compoundDrawables[3], this.mDrawableBottomTint);
        }
        if (Build.VERSION.SDK_INT < 17) {
            return;
        }
        if (this.mDrawableStartTint != null || this.mDrawableEndTint != null) {
            Drawable[] compoundDrawablesRelative = Api17Impl.getCompoundDrawablesRelative(this.mView);
            applyCompoundDrawableTint(compoundDrawablesRelative[0], this.mDrawableStartTint);
            applyCompoundDrawableTint(compoundDrawablesRelative[2], this.mDrawableEndTint);
        }
    }

    /* access modifiers changed from: package-private */
    public void autoSizeText() {
        this.mAutoSizeTextHelper.autoSizeText();
    }

    /* access modifiers changed from: package-private */
    public int getAutoSizeMaxTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMaxTextSize();
    }

    /* access modifiers changed from: package-private */
    public int getAutoSizeMinTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMinTextSize();
    }

    /* access modifiers changed from: package-private */
    public int getAutoSizeStepGranularity() {
        return this.mAutoSizeTextHelper.getAutoSizeStepGranularity();
    }

    /* access modifiers changed from: package-private */
    public int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
    }

    /* access modifiers changed from: package-private */
    public int getAutoSizeTextType() {
        return this.mAutoSizeTextHelper.getAutoSizeTextType();
    }

    /* access modifiers changed from: package-private */
    public ColorStateList getCompoundDrawableTintList() {
        TintInfo tintInfo = this.mDrawableTint;
        if (tintInfo != null) {
            return tintInfo.mTintList;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public PorterDuff.Mode getCompoundDrawableTintMode() {
        TintInfo tintInfo = this.mDrawableTint;
        if (tintInfo != null) {
            return tintInfo.mTintMode;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public boolean isAutoSizeEnabled() {
        return this.mAutoSizeTextHelper.isAutoSizeEnabled();
    }

    /* access modifiers changed from: package-private */
    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        boolean z;
        boolean z2;
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        AppCompatDrawableManager appCompatDrawableManager;
        String str;
        AppCompatDrawableManager appCompatDrawableManager2;
        AttributeSet attributeSet = attrs;
        int i = defStyleAttr;
        Context context = this.mView.getContext();
        AppCompatDrawableManager appCompatDrawableManager3 = AppCompatDrawableManager.get();
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.AppCompatTextHelper, i, 0);
        TextView textView = this.mView;
        ViewCompat.saveAttributeDataForStyleable(textView, textView.getContext(), R.styleable.AppCompatTextHelper, attrs, obtainStyledAttributes.getWrappedTypeArray(), defStyleAttr, 0);
        int resourceId = obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_textAppearance, -1);
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableLeft)) {
            this.mDrawableLeftTint = createTintInfo(context, appCompatDrawableManager3, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableLeft, 0));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableTop)) {
            this.mDrawableTopTint = createTintInfo(context, appCompatDrawableManager3, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableTop, 0));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableRight)) {
            this.mDrawableRightTint = createTintInfo(context, appCompatDrawableManager3, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableRight, 0));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableBottom)) {
            this.mDrawableBottomTint = createTintInfo(context, appCompatDrawableManager3, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableBottom, 0));
        }
        if (Build.VERSION.SDK_INT >= 17) {
            if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableStart)) {
                this.mDrawableStartTint = createTintInfo(context, appCompatDrawableManager3, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableStart, 0));
            }
            if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableEnd)) {
                this.mDrawableEndTint = createTintInfo(context, appCompatDrawableManager3, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableEnd, 0));
            }
        }
        obtainStyledAttributes.recycle();
        boolean z3 = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
        boolean z4 = false;
        boolean z5 = false;
        ColorStateList colorStateList3 = null;
        ColorStateList colorStateList4 = null;
        ColorStateList colorStateList5 = null;
        String str2 = null;
        String str3 = null;
        if (resourceId != -1) {
            TintTypedArray obtainStyledAttributes2 = TintTypedArray.obtainStyledAttributes(context, resourceId, R.styleable.TextAppearance);
            if (!z3 && obtainStyledAttributes2.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                z5 = true;
                z4 = obtainStyledAttributes2.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
            }
            updateTypefaceAndStyle(context, obtainStyledAttributes2);
            if (Build.VERSION.SDK_INT < 23) {
                if (obtainStyledAttributes2.hasValue(R.styleable.TextAppearance_android_textColor)) {
                    colorStateList3 = obtainStyledAttributes2.getColorStateList(R.styleable.TextAppearance_android_textColor);
                }
                if (obtainStyledAttributes2.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                    colorStateList4 = obtainStyledAttributes2.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
                }
                if (obtainStyledAttributes2.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
                    colorStateList5 = obtainStyledAttributes2.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
                }
            }
            if (obtainStyledAttributes2.hasValue(R.styleable.TextAppearance_textLocale)) {
                str3 = obtainStyledAttributes2.getString(R.styleable.TextAppearance_textLocale);
            }
            if (Build.VERSION.SDK_INT >= 26 && obtainStyledAttributes2.hasValue(R.styleable.TextAppearance_fontVariationSettings)) {
                str2 = obtainStyledAttributes2.getString(R.styleable.TextAppearance_fontVariationSettings);
            }
            obtainStyledAttributes2.recycle();
        }
        TintTypedArray obtainStyledAttributes3 = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.TextAppearance, i, 0);
        if (z3 || !obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            z2 = z4;
            z = z5;
        } else {
            z2 = obtainStyledAttributes3.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
            z = true;
        }
        if (Build.VERSION.SDK_INT < 23) {
            if (obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_textColor)) {
                colorStateList3 = obtainStyledAttributes3.getColorStateList(R.styleable.TextAppearance_android_textColor);
            }
            if (obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                colorStateList4 = obtainStyledAttributes3.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
            }
            if (obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
                colorStateList = colorStateList3;
                colorStateList2 = obtainStyledAttributes3.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
            } else {
                ColorStateList colorStateList6 = colorStateList5;
                colorStateList = colorStateList3;
                colorStateList2 = colorStateList6;
            }
        } else {
            ColorStateList colorStateList7 = colorStateList5;
            colorStateList = colorStateList3;
            colorStateList2 = colorStateList7;
        }
        String string = obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_textLocale) ? obtainStyledAttributes3.getString(R.styleable.TextAppearance_textLocale) : str3;
        String string2 = (Build.VERSION.SDK_INT < 26 || !obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_fontVariationSettings)) ? str2 : obtainStyledAttributes3.getString(R.styleable.TextAppearance_fontVariationSettings);
        int i2 = resourceId;
        if (Build.VERSION.SDK_INT < 28) {
            appCompatDrawableManager = appCompatDrawableManager3;
        } else if (!obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_textSize)) {
            appCompatDrawableManager = appCompatDrawableManager3;
        } else if (obtainStyledAttributes3.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, -1) == 0) {
            appCompatDrawableManager = appCompatDrawableManager3;
            this.mView.setTextSize(0, 0.0f);
        } else {
            appCompatDrawableManager = appCompatDrawableManager3;
        }
        updateTypefaceAndStyle(context, obtainStyledAttributes3);
        obtainStyledAttributes3.recycle();
        if (colorStateList != null) {
            this.mView.setTextColor(colorStateList);
        }
        if (colorStateList4 != null) {
            this.mView.setHintTextColor(colorStateList4);
        }
        if (colorStateList2 != null) {
            this.mView.setLinkTextColor(colorStateList2);
        }
        if (!z3 && z) {
            setAllCaps(z2);
        }
        Typeface typeface = this.mFontTypeface;
        if (typeface != null) {
            if (this.mFontWeight == -1) {
                this.mView.setTypeface(typeface, this.mStyle);
            } else {
                this.mView.setTypeface(typeface);
            }
        }
        if (string2 != null) {
            Api26Impl.setFontVariationSettings(this.mView, string2);
        }
        if (string != null) {
            if (Build.VERSION.SDK_INT >= 24) {
                Api24Impl.setTextLocales(this.mView, Api24Impl.forLanguageTags(string));
            } else if (Build.VERSION.SDK_INT >= 21) {
                Api17Impl.setTextLocale(this.mView, Api21Impl.forLanguageTag(string.split(",")[0]));
            }
        }
        this.mAutoSizeTextHelper.loadFromAttributes(attributeSet, i);
        if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            str = string;
            ColorStateList colorStateList8 = colorStateList2;
            ColorStateList colorStateList9 = colorStateList4;
        } else if (this.mAutoSizeTextHelper.getAutoSizeTextType() != 0) {
            int[] autoSizeTextAvailableSizes = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
            if (autoSizeTextAvailableSizes.length <= 0) {
                str = string;
                ColorStateList colorStateList10 = colorStateList2;
                ColorStateList colorStateList11 = colorStateList4;
            } else if (((float) Api26Impl.getAutoSizeStepGranularity(this.mView)) != -1.0f) {
                str = string;
                ColorStateList colorStateList12 = colorStateList2;
                ColorStateList colorStateList13 = colorStateList4;
                Api26Impl.setAutoSizeTextTypeUniformWithConfiguration(this.mView, this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
            } else {
                str = string;
                ColorStateList colorStateList14 = colorStateList2;
                ColorStateList colorStateList15 = colorStateList4;
                Api26Impl.setAutoSizeTextTypeUniformWithPresetSizes(this.mView, autoSizeTextAvailableSizes, 0);
            }
        } else {
            str = string;
            ColorStateList colorStateList16 = colorStateList2;
            ColorStateList colorStateList17 = colorStateList4;
        }
        TintTypedArray obtainStyledAttributes4 = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.AppCompatTextView);
        Drawable drawable = null;
        Drawable drawable2 = null;
        Drawable drawable3 = null;
        Drawable drawable4 = null;
        int resourceId2 = obtainStyledAttributes4.getResourceId(R.styleable.AppCompatTextView_drawableLeftCompat, -1);
        if (resourceId2 != -1) {
            appCompatDrawableManager2 = appCompatDrawableManager;
            drawable3 = appCompatDrawableManager2.getDrawable(context, resourceId2);
        } else {
            appCompatDrawableManager2 = appCompatDrawableManager;
        }
        int i3 = resourceId2;
        Drawable drawable5 = null;
        int resourceId3 = obtainStyledAttributes4.getResourceId(R.styleable.AppCompatTextView_drawableTopCompat, -1);
        if (resourceId3 != -1) {
            drawable4 = appCompatDrawableManager2.getDrawable(context, resourceId3);
        }
        int i4 = resourceId3;
        int resourceId4 = obtainStyledAttributes4.getResourceId(R.styleable.AppCompatTextView_drawableRightCompat, -1);
        if (resourceId4 != -1) {
            drawable5 = appCompatDrawableManager2.getDrawable(context, resourceId4);
        }
        int i5 = resourceId4;
        int resourceId5 = obtainStyledAttributes4.getResourceId(R.styleable.AppCompatTextView_drawableBottomCompat, -1);
        Drawable drawable6 = resourceId5 != -1 ? appCompatDrawableManager2.getDrawable(context, resourceId5) : null;
        int resourceId6 = obtainStyledAttributes4.getResourceId(R.styleable.AppCompatTextView_drawableStartCompat, -1);
        if (resourceId6 != -1) {
            drawable = appCompatDrawableManager2.getDrawable(context, resourceId6);
        }
        int i6 = resourceId5;
        int resourceId7 = obtainStyledAttributes4.getResourceId(R.styleable.AppCompatTextView_drawableEndCompat, -1);
        if (resourceId7 != -1) {
            drawable2 = appCompatDrawableManager2.getDrawable(context, resourceId7);
        }
        int i7 = resourceId7;
        String str4 = str;
        AppCompatDrawableManager appCompatDrawableManager4 = appCompatDrawableManager2;
        int i8 = resourceId6;
        ColorStateList colorStateList18 = colorStateList;
        String str5 = string2;
        boolean z6 = z3;
        setCompoundDrawables(drawable3, drawable4, drawable5, drawable6, drawable, drawable2);
        if (obtainStyledAttributes4.hasValue(R.styleable.AppCompatTextView_drawableTint)) {
            TextViewCompat.setCompoundDrawableTintList(this.mView, obtainStyledAttributes4.getColorStateList(R.styleable.AppCompatTextView_drawableTint));
        }
        if (obtainStyledAttributes4.hasValue(R.styleable.AppCompatTextView_drawableTintMode)) {
            TextViewCompat.setCompoundDrawableTintMode(this.mView, DrawableUtils.parseTintMode(obtainStyledAttributes4.getInt(R.styleable.AppCompatTextView_drawableTintMode, -1), (PorterDuff.Mode) null));
        }
        int dimensionPixelSize = obtainStyledAttributes4.getDimensionPixelSize(R.styleable.AppCompatTextView_firstBaselineToTopHeight, -1);
        int dimensionPixelSize2 = obtainStyledAttributes4.getDimensionPixelSize(R.styleable.AppCompatTextView_lastBaselineToBottomHeight, -1);
        int dimensionPixelSize3 = obtainStyledAttributes4.getDimensionPixelSize(R.styleable.AppCompatTextView_lineHeight, -1);
        obtainStyledAttributes4.recycle();
        if (dimensionPixelSize != -1) {
            TextViewCompat.setFirstBaselineToTopHeight(this.mView, dimensionPixelSize);
        }
        if (dimensionPixelSize2 != -1) {
            TextViewCompat.setLastBaselineToBottomHeight(this.mView, dimensionPixelSize2);
        }
        if (dimensionPixelSize3 != -1) {
            TextViewCompat.setLineHeight(this.mView, dimensionPixelSize3);
        }
    }

    /* access modifiers changed from: package-private */
    public void onAsyncTypefaceReceived(WeakReference<TextView> weakReference, final Typeface typeface) {
        if (this.mAsyncFontPending) {
            this.mFontTypeface = typeface;
            final TextView textView = (TextView) weakReference.get();
            if (textView == null) {
                return;
            }
            if (ViewCompat.isAttachedToWindow(textView)) {
                final int i = this.mStyle;
                textView.post(new Runnable() {
                    public void run() {
                        textView.setTypeface(typeface, i);
                    }
                });
                return;
            }
            textView.setTypeface(typeface, this.mStyle);
        }
    }

    /* access modifiers changed from: package-private */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            autoSizeText();
        }
    }

    /* access modifiers changed from: package-private */
    public void onSetCompoundDrawables() {
        applyCompoundDrawablesTints();
    }

    /* access modifiers changed from: package-private */
    public void onSetTextAppearance(Context context, int resId) {
        String string;
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        ColorStateList colorStateList3;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, resId, R.styleable.TextAppearance);
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            setAllCaps(obtainStyledAttributes.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
        }
        if (Build.VERSION.SDK_INT < 23) {
            if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textColor) && (colorStateList3 = obtainStyledAttributes.getColorStateList(R.styleable.TextAppearance_android_textColor)) != null) {
                this.mView.setTextColor(colorStateList3);
            }
            if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textColorLink) && (colorStateList2 = obtainStyledAttributes.getColorStateList(R.styleable.TextAppearance_android_textColorLink)) != null) {
                this.mView.setLinkTextColor(colorStateList2);
            }
            if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textColorHint) && (colorStateList = obtainStyledAttributes.getColorStateList(R.styleable.TextAppearance_android_textColorHint)) != null) {
                this.mView.setHintTextColor(colorStateList);
            }
        }
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textSize) && obtainStyledAttributes.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, -1) == 0) {
            this.mView.setTextSize(0, 0.0f);
        }
        updateTypefaceAndStyle(context, obtainStyledAttributes);
        if (Build.VERSION.SDK_INT >= 26 && obtainStyledAttributes.hasValue(R.styleable.TextAppearance_fontVariationSettings) && (string = obtainStyledAttributes.getString(R.styleable.TextAppearance_fontVariationSettings)) != null) {
            Api26Impl.setFontVariationSettings(this.mView, string);
        }
        obtainStyledAttributes.recycle();
        Typeface typeface = this.mFontTypeface;
        if (typeface != null) {
            this.mView.setTypeface(typeface, this.mStyle);
        }
    }

    /* access modifiers changed from: package-private */
    public void populateSurroundingTextIfNeeded(TextView textView, InputConnection inputConnection, EditorInfo editorInfo) {
        if (Build.VERSION.SDK_INT < 30 && inputConnection != null) {
            EditorInfoCompat.setInitialSurroundingText(editorInfo, textView.getText());
        }
    }

    /* access modifiers changed from: package-private */
    public void setAllCaps(boolean allCaps) {
        this.mView.setAllCaps(allCaps);
    }

    /* access modifiers changed from: package-private */
    public void setAutoSizeTextTypeUniformWithConfiguration(int autoSizeMinTextSize, int autoSizeMaxTextSize, int autoSizeStepGranularity, int unit) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(autoSizeMinTextSize, autoSizeMaxTextSize, autoSizeStepGranularity, unit);
    }

    /* access modifiers changed from: package-private */
    public void setAutoSizeTextTypeUniformWithPresetSizes(int[] presetSizes, int unit) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(presetSizes, unit);
    }

    /* access modifiers changed from: package-private */
    public void setAutoSizeTextTypeWithDefaults(int autoSizeTextType) {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(autoSizeTextType);
    }

    /* access modifiers changed from: package-private */
    public void setCompoundDrawableTintList(ColorStateList tintList) {
        if (this.mDrawableTint == null) {
            this.mDrawableTint = new TintInfo();
        }
        this.mDrawableTint.mTintList = tintList;
        this.mDrawableTint.mHasTintList = tintList != null;
        setCompoundTints();
    }

    /* access modifiers changed from: package-private */
    public void setCompoundDrawableTintMode(PorterDuff.Mode tintMode) {
        if (this.mDrawableTint == null) {
            this.mDrawableTint = new TintInfo();
        }
        this.mDrawableTint.mTintMode = tintMode;
        this.mDrawableTint.mHasTintMode = tintMode != null;
        setCompoundTints();
    }

    /* access modifiers changed from: package-private */
    public void setTextSize(int unit, float size) {
        if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && !isAutoSizeEnabled()) {
            setTextSizeInternal(unit, size);
        }
    }
}

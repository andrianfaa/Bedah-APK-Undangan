package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.core.view.ViewCompat;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import mt.Log1F380D;

/* compiled from: 0007 */
class AppCompatTextViewAutoSizeHelper {
    private static final int DEFAULT_AUTO_SIZE_GRANULARITY_IN_PX = 1;
    private static final int DEFAULT_AUTO_SIZE_MAX_TEXT_SIZE_IN_SP = 112;
    private static final int DEFAULT_AUTO_SIZE_MIN_TEXT_SIZE_IN_SP = 12;
    private static final String TAG = "ACTVAutoSizeHelper";
    private static final RectF TEMP_RECTF = new RectF();
    static final float UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE = -1.0f;
    private static final int VERY_WIDE = 1048576;
    private static ConcurrentHashMap<String, Field> sTextViewFieldByNameCache = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Method> sTextViewMethodByNameCache = new ConcurrentHashMap<>();
    private float mAutoSizeMaxTextSizeInPx = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
    private float mAutoSizeMinTextSizeInPx = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
    private float mAutoSizeStepGranularityInPx = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
    private int[] mAutoSizeTextSizesInPx = new int[0];
    private int mAutoSizeTextType = 0;
    private final Context mContext;
    private boolean mHasPresetAutoSizeValues = false;
    private final Impl mImpl;
    private boolean mNeedsAutoSizeText = false;
    private TextPaint mTempTextPaint;
    private final TextView mTextView;

    private static final class Api16Impl {
        private Api16Impl() {
        }

        static StaticLayout createStaticLayoutForMeasuring(CharSequence text, Layout.Alignment alignment, int availableWidth, TextView textView, TextPaint tempTextPaint) {
            return new StaticLayout(text, tempTextPaint, availableWidth, alignment, textView.getLineSpacingMultiplier(), textView.getLineSpacingExtra(), textView.getIncludeFontPadding());
        }

        static int getMaxLines(TextView textView) {
            return textView.getMaxLines();
        }
    }

    private static final class Api18Impl {
        private Api18Impl() {
        }

        static boolean isInLayout(View view) {
            return view.isInLayout();
        }
    }

    private static final class Api23Impl {
        private Api23Impl() {
        }

        static StaticLayout createStaticLayoutForMeasuring(CharSequence text, Layout.Alignment alignment, int availableWidth, int maxLines, TextView textView, TextPaint tempTextPaint, Impl impl) {
            StaticLayout.Builder obtain = StaticLayout.Builder.obtain(text, 0, text.length(), tempTextPaint, availableWidth);
            obtain.setAlignment(alignment).setLineSpacing(textView.getLineSpacingExtra(), textView.getLineSpacingMultiplier()).setIncludePad(textView.getIncludeFontPadding()).setBreakStrategy(textView.getBreakStrategy()).setHyphenationFrequency(textView.getHyphenationFrequency()).setMaxLines(maxLines == -1 ? Integer.MAX_VALUE : maxLines);
            try {
                impl.computeAndSetTextDirection(obtain, textView);
            } catch (ClassCastException e) {
                Log.w(AppCompatTextViewAutoSizeHelper.TAG, "Failed to obtain TextDirectionHeuristic, auto size may be incorrect");
            }
            return obtain.build();
        }
    }

    private static class Impl {
        Impl() {
        }

        /* access modifiers changed from: package-private */
        public void computeAndSetTextDirection(StaticLayout.Builder layoutBuilder, TextView textView) {
        }

        /* access modifiers changed from: package-private */
        public boolean isHorizontallyScrollable(TextView textView) {
            return ((Boolean) AppCompatTextViewAutoSizeHelper.invokeAndReturnWithDefault(textView, "getHorizontallyScrolling", false)).booleanValue();
        }
    }

    private static class Impl23 extends Impl {
        Impl23() {
        }

        /* access modifiers changed from: package-private */
        public void computeAndSetTextDirection(StaticLayout.Builder layoutBuilder, TextView textView) {
            layoutBuilder.setTextDirection((TextDirectionHeuristic) AppCompatTextViewAutoSizeHelper.invokeAndReturnWithDefault(textView, "getTextDirectionHeuristic", TextDirectionHeuristics.FIRSTSTRONG_LTR));
        }
    }

    private static class Impl29 extends Impl23 {
        Impl29() {
        }

        /* access modifiers changed from: package-private */
        public void computeAndSetTextDirection(StaticLayout.Builder layoutBuilder, TextView textView) {
            layoutBuilder.setTextDirection(textView.getTextDirectionHeuristic());
        }

        /* access modifiers changed from: package-private */
        public boolean isHorizontallyScrollable(TextView textView) {
            return textView.isHorizontallyScrollable();
        }
    }

    AppCompatTextViewAutoSizeHelper(TextView textView) {
        this.mTextView = textView;
        this.mContext = textView.getContext();
        if (Build.VERSION.SDK_INT >= 29) {
            this.mImpl = new Impl29();
        } else if (Build.VERSION.SDK_INT >= 23) {
            this.mImpl = new Impl23();
        } else {
            this.mImpl = new Impl();
        }
    }

    private static <T> T accessAndReturnWithDefault(Object object, String fieldName, T t) {
        try {
            Field textViewField = getTextViewField(fieldName);
            return textViewField == null ? t : textViewField.get(object);
        } catch (IllegalAccessException e) {
            Log.w(TAG, "Failed to access TextView#" + fieldName + " member", e);
            return t;
        }
    }

    private int[] cleanupAutoSizePresetSizes(int[] presetValues) {
        if (r0 == 0) {
            return presetValues;
        }
        Arrays.sort(presetValues);
        ArrayList arrayList = new ArrayList();
        for (int i : presetValues) {
            if (i > 0 && Collections.binarySearch(arrayList, Integer.valueOf(i)) < 0) {
                arrayList.add(Integer.valueOf(i));
            }
        }
        if (r0 == arrayList.size()) {
            return presetValues;
        }
        int size = arrayList.size();
        int[] iArr = new int[size];
        for (int i2 = 0; i2 < size; i2++) {
            iArr[i2] = ((Integer) arrayList.get(i2)).intValue();
        }
        return iArr;
    }

    private void clearAutoSizeConfiguration() {
        this.mAutoSizeTextType = 0;
        this.mAutoSizeMinTextSizeInPx = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        this.mAutoSizeMaxTextSizeInPx = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        this.mAutoSizeStepGranularityInPx = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        this.mAutoSizeTextSizesInPx = new int[0];
        this.mNeedsAutoSizeText = false;
    }

    private StaticLayout createStaticLayoutForMeasuringPre16(CharSequence text, Layout.Alignment alignment, int availableWidth) {
        return new StaticLayout(text, this.mTempTextPaint, availableWidth, alignment, ((Float) accessAndReturnWithDefault(this.mTextView, "mSpacingMult", Float.valueOf(1.0f))).floatValue(), ((Float) accessAndReturnWithDefault(this.mTextView, "mSpacingAdd", Float.valueOf(0.0f))).floatValue(), ((Boolean) accessAndReturnWithDefault(this.mTextView, "mIncludePad", true)).booleanValue());
    }

    private int findLargestTextSizeWhichFits(RectF availableSpace) {
        int length = this.mAutoSizeTextSizesInPx.length;
        if (length != 0) {
            int i = 0;
            int i2 = 0 + 1;
            int i3 = length - 1;
            while (i2 <= i3) {
                int i4 = (i2 + i3) / 2;
                if (suggestedSizeFitsInSpace(this.mAutoSizeTextSizesInPx[i4], availableSpace)) {
                    i = i2;
                    i2 = i4 + 1;
                } else {
                    i3 = i4 - 1;
                    i = i3;
                }
            }
            return this.mAutoSizeTextSizesInPx[i];
        }
        throw new IllegalStateException("No available text sizes to choose from.");
    }

    private static Field getTextViewField(String fieldName) {
        try {
            Field field = sTextViewFieldByNameCache.get(fieldName);
            if (field == null && (field = TextView.class.getDeclaredField(fieldName)) != null) {
                field.setAccessible(true);
                sTextViewFieldByNameCache.put(fieldName, field);
            }
            return field;
        } catch (NoSuchFieldException e) {
            Log.w(TAG, "Failed to access TextView#" + fieldName + " member", e);
            return null;
        }
    }

    private static Method getTextViewMethod(String methodName) {
        try {
            Method method = sTextViewMethodByNameCache.get(methodName);
            if (method == null && (method = TextView.class.getDeclaredMethod(methodName, new Class[0])) != null) {
                method.setAccessible(true);
                sTextViewMethodByNameCache.put(methodName, method);
            }
            return method;
        } catch (Exception e) {
            Log.w(TAG, "Failed to retrieve TextView#" + methodName + "() method", e);
            return null;
        }
    }

    static <T> T invokeAndReturnWithDefault(Object object, String methodName, T t) {
        try {
            T invoke = getTextViewMethod(methodName).invoke(object, new Object[0]);
            if (invoke != null || 0 == 0) {
                return invoke;
            }
        } catch (Exception e) {
            Log.w(TAG, "Failed to invoke TextView#" + methodName + "() method", e);
            if (0 != 0 || 1 == 0) {
                return null;
            }
        } catch (Throwable th) {
            if (0 == 0 && 1 != 0) {
                T t2 = t;
            }
            throw th;
        }
        return t;
    }

    private void setRawTextSize(float size) {
        if (size != this.mTextView.getPaint().getTextSize()) {
            this.mTextView.getPaint().setTextSize(size);
            boolean z = false;
            if (Build.VERSION.SDK_INT >= 18) {
                z = Api18Impl.isInLayout(this.mTextView);
            }
            if (this.mTextView.getLayout() != null) {
                this.mNeedsAutoSizeText = false;
                try {
                    Method textViewMethod = getTextViewMethod("nullLayouts");
                    if (textViewMethod != null) {
                        textViewMethod.invoke(this.mTextView, new Object[0]);
                    }
                } catch (Exception e) {
                    Log.w(TAG, "Failed to invoke TextView#nullLayouts() method", e);
                }
                if (!z) {
                    this.mTextView.requestLayout();
                } else {
                    this.mTextView.forceLayout();
                }
                this.mTextView.invalidate();
            }
        }
    }

    private boolean setupAutoSizeText() {
        if (!supportsAutoSizeText() || this.mAutoSizeTextType != 1) {
            this.mNeedsAutoSizeText = false;
        } else {
            if (!this.mHasPresetAutoSizeValues || this.mAutoSizeTextSizesInPx.length == 0) {
                int floor = ((int) Math.floor((double) ((this.mAutoSizeMaxTextSizeInPx - this.mAutoSizeMinTextSizeInPx) / this.mAutoSizeStepGranularityInPx))) + 1;
                int[] iArr = new int[floor];
                for (int i = 0; i < floor; i++) {
                    iArr[i] = Math.round(this.mAutoSizeMinTextSizeInPx + (((float) i) * this.mAutoSizeStepGranularityInPx));
                }
                this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(iArr);
            }
            this.mNeedsAutoSizeText = true;
        }
        return this.mNeedsAutoSizeText;
    }

    private void setupAutoSizeUniformPresetSizes(TypedArray textSizes) {
        int length = textSizes.length();
        int[] iArr = new int[length];
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                iArr[i] = textSizes.getDimensionPixelSize(i, -1);
            }
            this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(iArr);
            setupAutoSizeUniformPresetSizesConfiguration();
        }
    }

    private boolean setupAutoSizeUniformPresetSizesConfiguration() {
        int[] iArr = this.mAutoSizeTextSizesInPx;
        int length = iArr.length;
        boolean z = length > 0;
        this.mHasPresetAutoSizeValues = z;
        if (z) {
            this.mAutoSizeTextType = 1;
            this.mAutoSizeMinTextSizeInPx = (float) iArr[0];
            this.mAutoSizeMaxTextSizeInPx = (float) iArr[length - 1];
            this.mAutoSizeStepGranularityInPx = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        }
        return z;
    }

    private boolean suggestedSizeFitsInSpace(int suggestedSizeInPx, RectF availableSpace) {
        CharSequence transformation;
        CharSequence text = this.mTextView.getText();
        TransformationMethod transformationMethod = this.mTextView.getTransformationMethod();
        if (!(transformationMethod == null || (transformation = transformationMethod.getTransformation(text, this.mTextView)) == null)) {
            text = transformation;
        }
        int maxLines = Build.VERSION.SDK_INT >= 16 ? Api16Impl.getMaxLines(this.mTextView) : -1;
        initTempTextPaint(suggestedSizeInPx);
        StaticLayout createLayout = createLayout(text, (Layout.Alignment) invokeAndReturnWithDefault(this.mTextView, "getLayoutAlignment", Layout.Alignment.ALIGN_NORMAL), Math.round(availableSpace.right), maxLines);
        return (maxLines == -1 || (createLayout.getLineCount() <= maxLines && createLayout.getLineEnd(createLayout.getLineCount() - 1) == text.length())) && ((float) createLayout.getHeight()) <= availableSpace.bottom;
    }

    private boolean supportsAutoSizeText() {
        return !(this.mTextView instanceof AppCompatEditText);
    }

    private void validateAndSetAutoSizeTextTypeUniformConfiguration(float autoSizeMinTextSizeInPx, float autoSizeMaxTextSizeInPx, float autoSizeStepGranularityInPx) throws IllegalArgumentException {
        if (autoSizeMinTextSizeInPx <= 0.0f) {
            throw new IllegalArgumentException("Minimum auto-size text size (" + autoSizeMinTextSizeInPx + "px) is less or equal to (0px)");
        } else if (autoSizeMaxTextSizeInPx <= autoSizeMinTextSizeInPx) {
            throw new IllegalArgumentException("Maximum auto-size text size (" + autoSizeMaxTextSizeInPx + "px) is less or equal to minimum auto-size text size (" + autoSizeMinTextSizeInPx + "px)");
        } else if (autoSizeStepGranularityInPx > 0.0f) {
            this.mAutoSizeTextType = 1;
            this.mAutoSizeMinTextSizeInPx = autoSizeMinTextSizeInPx;
            this.mAutoSizeMaxTextSizeInPx = autoSizeMaxTextSizeInPx;
            this.mAutoSizeStepGranularityInPx = autoSizeStepGranularityInPx;
            this.mHasPresetAutoSizeValues = false;
        } else {
            throw new IllegalArgumentException("The auto-size step granularity (" + autoSizeStepGranularityInPx + "px) is less or equal to (0px)");
        }
    }

    /* access modifiers changed from: package-private */
    public void autoSizeText() {
        if (isAutoSizeEnabled()) {
            if (this.mNeedsAutoSizeText) {
                if (this.mTextView.getMeasuredHeight() > 0 && this.mTextView.getMeasuredWidth() > 0) {
                    int measuredWidth = this.mImpl.isHorizontallyScrollable(this.mTextView) ? 1048576 : (this.mTextView.getMeasuredWidth() - this.mTextView.getTotalPaddingLeft()) - this.mTextView.getTotalPaddingRight();
                    int height = (this.mTextView.getHeight() - this.mTextView.getCompoundPaddingBottom()) - this.mTextView.getCompoundPaddingTop();
                    if (measuredWidth > 0 && height > 0) {
                        RectF rectF = TEMP_RECTF;
                        synchronized (rectF) {
                            rectF.setEmpty();
                            rectF.right = (float) measuredWidth;
                            rectF.bottom = (float) height;
                            float findLargestTextSizeWhichFits = (float) findLargestTextSizeWhichFits(rectF);
                            if (findLargestTextSizeWhichFits != this.mTextView.getTextSize()) {
                                setTextSizeInternal(0, findLargestTextSizeWhichFits);
                            }
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            this.mNeedsAutoSizeText = true;
        }
    }

    /* access modifiers changed from: package-private */
    public StaticLayout createLayout(CharSequence text, Layout.Alignment alignment, int availableWidth, int maxLines) {
        if (Build.VERSION.SDK_INT < 23) {
            return Build.VERSION.SDK_INT >= 16 ? Api16Impl.createStaticLayoutForMeasuring(text, alignment, availableWidth, this.mTextView, this.mTempTextPaint) : createStaticLayoutForMeasuringPre16(text, alignment, availableWidth);
        }
        return Api23Impl.createStaticLayoutForMeasuring(text, alignment, availableWidth, maxLines, this.mTextView, this.mTempTextPaint, this.mImpl);
    }

    /* access modifiers changed from: package-private */
    public int getAutoSizeMaxTextSize() {
        return Math.round(this.mAutoSizeMaxTextSizeInPx);
    }

    /* access modifiers changed from: package-private */
    public int getAutoSizeMinTextSize() {
        return Math.round(this.mAutoSizeMinTextSizeInPx);
    }

    /* access modifiers changed from: package-private */
    public int getAutoSizeStepGranularity() {
        return Math.round(this.mAutoSizeStepGranularityInPx);
    }

    /* access modifiers changed from: package-private */
    public int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextSizesInPx;
    }

    /* access modifiers changed from: package-private */
    public int getAutoSizeTextType() {
        return this.mAutoSizeTextType;
    }

    /* access modifiers changed from: package-private */
    public void initTempTextPaint(int suggestedSizeInPx) {
        TextPaint textPaint = this.mTempTextPaint;
        if (textPaint == null) {
            this.mTempTextPaint = new TextPaint();
        } else {
            textPaint.reset();
        }
        this.mTempTextPaint.set(this.mTextView.getPaint());
        this.mTempTextPaint.setTextSize((float) suggestedSizeInPx);
    }

    /* access modifiers changed from: package-private */
    public boolean isAutoSizeEnabled() {
        return supportsAutoSizeText() && this.mAutoSizeTextType != 0;
    }

    /* access modifiers changed from: package-private */
    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        int resourceId;
        float f = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        float f2 = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        float f3 = UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(attrs, R.styleable.AppCompatTextView, defStyleAttr, 0);
        TextView textView = this.mTextView;
        ViewCompat.saveAttributeDataForStyleable(textView, textView.getContext(), R.styleable.AppCompatTextView, attrs, obtainStyledAttributes, defStyleAttr, 0);
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextView_autoSizeTextType)) {
            this.mAutoSizeTextType = obtainStyledAttributes.getInt(R.styleable.AppCompatTextView_autoSizeTextType, 0);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextView_autoSizeStepGranularity)) {
            f3 = obtainStyledAttributes.getDimension(R.styleable.AppCompatTextView_autoSizeStepGranularity, UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextView_autoSizeMinTextSize)) {
            f = obtainStyledAttributes.getDimension(R.styleable.AppCompatTextView_autoSizeMinTextSize, UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextView_autoSizeMaxTextSize)) {
            f2 = obtainStyledAttributes.getDimension(R.styleable.AppCompatTextView_autoSizeMaxTextSize, UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextView_autoSizePresetSizes) && (resourceId = obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextView_autoSizePresetSizes, 0)) > 0) {
            TypedArray obtainTypedArray = obtainStyledAttributes.getResources().obtainTypedArray(resourceId);
            setupAutoSizeUniformPresetSizes(obtainTypedArray);
            obtainTypedArray.recycle();
        }
        obtainStyledAttributes.recycle();
        if (!supportsAutoSizeText()) {
            this.mAutoSizeTextType = 0;
        } else if (this.mAutoSizeTextType == 1) {
            if (!this.mHasPresetAutoSizeValues) {
                DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                if (f == UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE) {
                    f = TypedValue.applyDimension(2, 12.0f, displayMetrics);
                }
                if (f2 == UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE) {
                    f2 = TypedValue.applyDimension(2, 112.0f, displayMetrics);
                }
                if (f3 == UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE) {
                    f3 = 1.0f;
                }
                validateAndSetAutoSizeTextTypeUniformConfiguration(f, f2, f3);
            }
            setupAutoSizeText();
        }
    }

    /* access modifiers changed from: package-private */
    public void setAutoSizeTextTypeUniformWithConfiguration(int autoSizeMinTextSize, int autoSizeMaxTextSize, int autoSizeStepGranularity, int unit) throws IllegalArgumentException {
        if (supportsAutoSizeText()) {
            DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
            validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(unit, (float) autoSizeMinTextSize, displayMetrics), TypedValue.applyDimension(unit, (float) autoSizeMaxTextSize, displayMetrics), TypedValue.applyDimension(unit, (float) autoSizeStepGranularity, displayMetrics));
            if (setupAutoSizeText()) {
                autoSizeText();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setAutoSizeTextTypeUniformWithPresetSizes(int[] presetSizes, int unit) throws IllegalArgumentException {
        if (supportsAutoSizeText()) {
            int length = presetSizes.length;
            if (length > 0) {
                int[] iArr = new int[length];
                if (unit == 0) {
                    iArr = Arrays.copyOf(presetSizes, length);
                } else {
                    DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                    for (int i = 0; i < length; i++) {
                        iArr[i] = Math.round(TypedValue.applyDimension(unit, (float) presetSizes[i], displayMetrics));
                    }
                }
                this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(iArr);
                if (!setupAutoSizeUniformPresetSizesConfiguration()) {
                    StringBuilder append = new StringBuilder().append("None of the preset sizes is valid: ");
                    String arrays = Arrays.toString(presetSizes);
                    Log1F380D.a((Object) arrays);
                    throw new IllegalArgumentException(append.append(arrays).toString());
                }
            } else {
                this.mHasPresetAutoSizeValues = false;
            }
            if (setupAutoSizeText()) {
                autoSizeText();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setAutoSizeTextTypeWithDefaults(int autoSizeTextType) {
        if (supportsAutoSizeText()) {
            switch (autoSizeTextType) {
                case 0:
                    clearAutoSizeConfiguration();
                    return;
                case 1:
                    DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                    validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(2, 12.0f, displayMetrics), TypedValue.applyDimension(2, 112.0f, displayMetrics), 1.0f);
                    if (setupAutoSizeText()) {
                        autoSizeText();
                        return;
                    }
                    return;
                default:
                    throw new IllegalArgumentException("Unknown auto-size text type: " + autoSizeTextType);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setTextSizeInternal(int unit, float size) {
        Context context = this.mContext;
        setRawTextSize(TypedValue.applyDimension(unit, size, (context == null ? Resources.getSystem() : context.getResources()).getDisplayMetrics()));
    }
}

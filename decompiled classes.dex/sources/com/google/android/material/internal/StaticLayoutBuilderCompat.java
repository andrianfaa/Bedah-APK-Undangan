package com.google.android.material.internal;

import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import androidx.core.util.Preconditions;
import java.lang.reflect.Constructor;
import okhttp3.HttpUrl;

final class StaticLayoutBuilderCompat {
    static final int DEFAULT_HYPHENATION_FREQUENCY = (Build.VERSION.SDK_INT >= 23 ? 1 : 0);
    static final float DEFAULT_LINE_SPACING_ADD = 0.0f;
    static final float DEFAULT_LINE_SPACING_MULTIPLIER = 1.0f;
    private static final String TEXT_DIRS_CLASS = "android.text.TextDirectionHeuristics";
    private static final String TEXT_DIR_CLASS = "android.text.TextDirectionHeuristic";
    private static final String TEXT_DIR_CLASS_LTR = "LTR";
    private static final String TEXT_DIR_CLASS_RTL = "RTL";
    private static Constructor<StaticLayout> constructor;
    private static boolean initialized;
    private static Object textDirection;
    private Layout.Alignment alignment;
    private TextUtils.TruncateAt ellipsize;
    private int end;
    private int hyphenationFrequency;
    private boolean includePad;
    private boolean isRtl;
    private float lineSpacingAdd;
    private float lineSpacingMultiplier;
    private int maxLines;
    private final TextPaint paint;
    private CharSequence source;
    private int start = 0;
    private final int width;

    static class StaticLayoutBuilderCompatException extends Exception {
        StaticLayoutBuilderCompatException(Throwable cause) {
            super("Error thrown initializing StaticLayout " + cause.getMessage(), cause);
        }
    }

    private StaticLayoutBuilderCompat(CharSequence source2, TextPaint paint2, int width2) {
        this.source = source2;
        this.paint = paint2;
        this.width = width2;
        this.end = source2.length();
        this.alignment = Layout.Alignment.ALIGN_NORMAL;
        this.maxLines = Integer.MAX_VALUE;
        this.lineSpacingAdd = 0.0f;
        this.lineSpacingMultiplier = 1.0f;
        this.hyphenationFrequency = DEFAULT_HYPHENATION_FREQUENCY;
        this.includePad = true;
        this.ellipsize = null;
    }

    private void createConstructorWithReflection() throws StaticLayoutBuilderCompatException {
        Class cls;
        if (!initialized) {
            try {
                boolean z = this.isRtl && Build.VERSION.SDK_INT >= 23;
                if (Build.VERSION.SDK_INT >= 18) {
                    cls = TextDirectionHeuristic.class;
                    textDirection = z ? TextDirectionHeuristics.RTL : TextDirectionHeuristics.LTR;
                } else {
                    ClassLoader classLoader = StaticLayoutBuilderCompat.class.getClassLoader();
                    String str = this.isRtl ? TEXT_DIR_CLASS_RTL : TEXT_DIR_CLASS_LTR;
                    Class<?> loadClass = classLoader.loadClass(TEXT_DIR_CLASS);
                    Class<?> loadClass2 = classLoader.loadClass(TEXT_DIRS_CLASS);
                    textDirection = loadClass2.getField(str).get(loadClass2);
                    cls = loadClass;
                }
                Constructor<StaticLayout> declaredConstructor = StaticLayout.class.getDeclaredConstructor(new Class[]{CharSequence.class, Integer.TYPE, Integer.TYPE, TextPaint.class, Integer.TYPE, Layout.Alignment.class, cls, Float.TYPE, Float.TYPE, Boolean.TYPE, TextUtils.TruncateAt.class, Integer.TYPE, Integer.TYPE});
                constructor = declaredConstructor;
                declaredConstructor.setAccessible(true);
                initialized = true;
            } catch (Exception e) {
                throw new StaticLayoutBuilderCompatException(e);
            }
        }
    }

    public static StaticLayoutBuilderCompat obtain(CharSequence source2, TextPaint paint2, int width2) {
        return new StaticLayoutBuilderCompat(source2, paint2, width2);
    }

    public StaticLayout build() throws StaticLayoutBuilderCompatException {
        if (this.source == null) {
            this.source = HttpUrl.FRAGMENT_ENCODE_SET;
        }
        int max = Math.max(0, this.width);
        CharSequence charSequence = this.source;
        if (this.maxLines == 1) {
            charSequence = TextUtils.ellipsize(this.source, this.paint, (float) max, this.ellipsize);
        }
        this.end = Math.min(charSequence.length(), this.end);
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.isRtl && this.maxLines == 1) {
                this.alignment = Layout.Alignment.ALIGN_OPPOSITE;
            }
            StaticLayout.Builder obtain = StaticLayout.Builder.obtain(charSequence, this.start, this.end, this.paint, max);
            obtain.setAlignment(this.alignment);
            obtain.setIncludePad(this.includePad);
            obtain.setTextDirection(this.isRtl ? TextDirectionHeuristics.RTL : TextDirectionHeuristics.LTR);
            TextUtils.TruncateAt truncateAt = this.ellipsize;
            if (truncateAt != null) {
                obtain.setEllipsize(truncateAt);
            }
            obtain.setMaxLines(this.maxLines);
            float f = this.lineSpacingAdd;
            if (!(f == 0.0f && this.lineSpacingMultiplier == 1.0f)) {
                obtain.setLineSpacing(f, this.lineSpacingMultiplier);
            }
            if (this.maxLines > 1) {
                obtain.setHyphenationFrequency(this.hyphenationFrequency);
            }
            return obtain.build();
        }
        createConstructorWithReflection();
        try {
            return (StaticLayout) ((Constructor) Preconditions.checkNotNull(constructor)).newInstance(new Object[]{charSequence, Integer.valueOf(this.start), Integer.valueOf(this.end), this.paint, Integer.valueOf(max), this.alignment, Preconditions.checkNotNull(textDirection), Float.valueOf(1.0f), Float.valueOf(0.0f), Boolean.valueOf(this.includePad), null, Integer.valueOf(max), Integer.valueOf(this.maxLines)});
        } catch (Exception e) {
            throw new StaticLayoutBuilderCompatException(e);
        }
    }

    public StaticLayoutBuilderCompat setAlignment(Layout.Alignment alignment2) {
        this.alignment = alignment2;
        return this;
    }

    public StaticLayoutBuilderCompat setEllipsize(TextUtils.TruncateAt ellipsize2) {
        this.ellipsize = ellipsize2;
        return this;
    }

    public StaticLayoutBuilderCompat setEnd(int end2) {
        this.end = end2;
        return this;
    }

    public StaticLayoutBuilderCompat setHyphenationFrequency(int hyphenationFrequency2) {
        this.hyphenationFrequency = hyphenationFrequency2;
        return this;
    }

    public StaticLayoutBuilderCompat setIncludePad(boolean includePad2) {
        this.includePad = includePad2;
        return this;
    }

    public StaticLayoutBuilderCompat setIsRtl(boolean isRtl2) {
        this.isRtl = isRtl2;
        return this;
    }

    public StaticLayoutBuilderCompat setLineSpacing(float spacingAdd, float lineSpacingMultiplier2) {
        this.lineSpacingAdd = spacingAdd;
        this.lineSpacingMultiplier = lineSpacingMultiplier2;
        return this;
    }

    public StaticLayoutBuilderCompat setMaxLines(int maxLines2) {
        this.maxLines = maxLines2;
        return this;
    }

    public StaticLayoutBuilderCompat setStart(int start2) {
        this.start = start2;
        return this;
    }
}

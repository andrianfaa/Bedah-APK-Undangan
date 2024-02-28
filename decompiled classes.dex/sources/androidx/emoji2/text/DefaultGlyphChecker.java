package androidx.emoji2.text;

import android.os.Build;
import android.text.TextPaint;
import androidx.core.graphics.PaintCompat;
import androidx.emoji2.text.EmojiCompat;

class DefaultGlyphChecker implements EmojiCompat.GlyphChecker {
    private static final int PAINT_TEXT_SIZE = 10;
    private static final ThreadLocal<StringBuilder> sStringBuilder = new ThreadLocal<>();
    private final TextPaint mTextPaint;

    DefaultGlyphChecker() {
        TextPaint textPaint = new TextPaint();
        this.mTextPaint = textPaint;
        textPaint.setTextSize(10.0f);
    }

    private static StringBuilder getStringBuilder() {
        ThreadLocal<StringBuilder> threadLocal = sStringBuilder;
        if (threadLocal.get() == null) {
            threadLocal.set(new StringBuilder());
        }
        return threadLocal.get();
    }

    public boolean hasGlyph(CharSequence charSequence, int start, int end, int sdkAdded) {
        if (Build.VERSION.SDK_INT < 23 && sdkAdded > Build.VERSION.SDK_INT) {
            return false;
        }
        StringBuilder stringBuilder = getStringBuilder();
        stringBuilder.setLength(0);
        while (start < end) {
            stringBuilder.append(charSequence.charAt(start));
            start++;
        }
        return PaintCompat.hasGlyph(this.mTextPaint, stringBuilder.toString());
    }
}

package androidx.core.graphics;

import android.graphics.BlendMode;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import androidx.core.util.Pair;

public final class PaintCompat {
    private static final String EM_STRING = "m";
    private static final String TOFU_STRING = "󟿽";
    private static final ThreadLocal<Pair<Rect, Rect>> sRectThreadLocal = new ThreadLocal<>();

    static class Api23Impl {
        private Api23Impl() {
        }

        static boolean hasGlyph(Paint paint, String string) {
            return paint.hasGlyph(string);
        }
    }

    static class Api29Impl {
        private Api29Impl() {
        }

        static void setBlendMode(Paint paint, Object blendmode) {
            paint.setBlendMode((BlendMode) blendmode);
        }
    }

    private PaintCompat() {
    }

    public static boolean hasGlyph(Paint paint, String string) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.hasGlyph(paint, string);
        }
        int length = string.length();
        if (length == 1 && Character.isWhitespace(string.charAt(0))) {
            return true;
        }
        float measureText = paint.measureText(TOFU_STRING);
        float measureText2 = paint.measureText(EM_STRING);
        float measureText3 = paint.measureText(string);
        if (measureText3 == 0.0f) {
            return false;
        }
        if (string.codePointCount(0, string.length()) > 1) {
            if (measureText3 > 2.0f * measureText2) {
                return false;
            }
            float f = 0.0f;
            int i = 0;
            while (i < length) {
                int charCount = Character.charCount(string.codePointAt(i));
                f += paint.measureText(string, i, i + charCount);
                i += charCount;
            }
            if (measureText3 >= f) {
                return false;
            }
        }
        if (measureText3 != measureText) {
            return true;
        }
        Pair<Rect, Rect> obtainEmptyRects = obtainEmptyRects();
        paint.getTextBounds(TOFU_STRING, 0, TOFU_STRING.length(), (Rect) obtainEmptyRects.first);
        paint.getTextBounds(string, 0, length, (Rect) obtainEmptyRects.second);
        return true ^ ((Rect) obtainEmptyRects.first).equals(obtainEmptyRects.second);
    }

    private static Pair<Rect, Rect> obtainEmptyRects() {
        ThreadLocal<Pair<Rect, Rect>> threadLocal = sRectThreadLocal;
        Pair<Rect, Rect> pair = threadLocal.get();
        if (pair == null) {
            Pair<Rect, Rect> pair2 = new Pair<>(new Rect(), new Rect());
            threadLocal.set(pair2);
            return pair2;
        }
        ((Rect) pair.first).setEmpty();
        ((Rect) pair.second).setEmpty();
        return pair;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: android.graphics.PorterDuffXfermode} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: android.graphics.PorterDuffXfermode} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: android.graphics.PorterDuffXfermode} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v5, resolved type: android.graphics.PorterDuffXfermode} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v6, resolved type: android.graphics.PorterDuffXfermode} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean setBlendMode(android.graphics.Paint r4, androidx.core.graphics.BlendModeCompat r5) {
        /*
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 1
            r2 = 0
            r3 = 29
            if (r0 < r3) goto L_0x0015
            if (r5 == 0) goto L_0x000f
            java.lang.Object r2 = androidx.core.graphics.BlendModeUtils.Api29Impl.obtainBlendModeFromCompat(r5)
            goto L_0x0010
        L_0x000f:
        L_0x0010:
            r0 = r2
            androidx.core.graphics.PaintCompat.Api29Impl.setBlendMode(r4, r0)
            return r1
        L_0x0015:
            if (r5 == 0) goto L_0x002a
            android.graphics.PorterDuff$Mode r0 = androidx.core.graphics.BlendModeUtils.obtainPorterDuffFromCompat(r5)
            if (r0 == 0) goto L_0x0022
            android.graphics.PorterDuffXfermode r2 = new android.graphics.PorterDuffXfermode
            r2.<init>(r0)
        L_0x0022:
            r4.setXfermode(r2)
            if (r0 == 0) goto L_0x0028
            goto L_0x0029
        L_0x0028:
            r1 = 0
        L_0x0029:
            return r1
        L_0x002a:
            r4.setXfermode(r2)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.graphics.PaintCompat.setBlendMode(android.graphics.Paint, androidx.core.graphics.BlendModeCompat):boolean");
    }
}

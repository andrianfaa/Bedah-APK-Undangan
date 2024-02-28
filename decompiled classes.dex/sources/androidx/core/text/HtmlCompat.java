package androidx.core.text;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import mt.Log1F380D;

/* compiled from: 0054 */
public final class HtmlCompat {
    public static final int FROM_HTML_MODE_COMPACT = 63;
    public static final int FROM_HTML_MODE_LEGACY = 0;
    public static final int FROM_HTML_OPTION_USE_CSS_COLORS = 256;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE = 32;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_DIV = 16;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_HEADING = 2;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_LIST = 8;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM = 4;
    public static final int FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH = 1;
    public static final int TO_HTML_PARAGRAPH_LINES_CONSECUTIVE = 0;
    public static final int TO_HTML_PARAGRAPH_LINES_INDIVIDUAL = 1;

    /* compiled from: 0053 */
    static class Api24Impl {
        private Api24Impl() {
        }

        static Spanned fromHtml(String source, int flags) {
            return Html.fromHtml(source, flags);
        }

        static Spanned fromHtml(String source, int flags, Html.ImageGetter imageGetter, Html.TagHandler tagHandler) {
            return Html.fromHtml(source, flags, imageGetter, tagHandler);
        }

        static String toHtml(Spanned text, int option) {
            String html = Html.toHtml(text, option);
            Log1F380D.a((Object) html);
            return html;
        }
    }

    private HtmlCompat() {
    }

    public static Spanned fromHtml(String source, int flags) {
        return Build.VERSION.SDK_INT >= 24 ? Api24Impl.fromHtml(source, flags) : Html.fromHtml(source);
    }

    public static Spanned fromHtml(String source, int flags, Html.ImageGetter imageGetter, Html.TagHandler tagHandler) {
        return Build.VERSION.SDK_INT >= 24 ? Api24Impl.fromHtml(source, flags, imageGetter, tagHandler) : Html.fromHtml(source, imageGetter, tagHandler);
    }

    public static String toHtml(Spanned text, int options) {
        if (Build.VERSION.SDK_INT >= 24) {
            String html = Api24Impl.toHtml(text, options);
            Log1F380D.a((Object) html);
            return html;
        }
        String html2 = Html.toHtml(text);
        Log1F380D.a((Object) html2);
        return html2;
    }
}

package androidx.core.text;

import android.os.Build;
import android.text.TextUtils;
import java.util.Locale;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import mt.Log1F380D;
import okhttp3.HttpUrl;

/* compiled from: 0058 */
public final class TextUtilsCompat {
    private static final String ARAB_SCRIPT_SUBTAG = "Arab";
    private static final String HEBR_SCRIPT_SUBTAG = "Hebr";
    private static final Locale ROOT = new Locale(HttpUrl.FRAGMENT_ENCODE_SET, HttpUrl.FRAGMENT_ENCODE_SET);

    static class Api17Impl {
        private Api17Impl() {
        }

        static int getLayoutDirectionFromLocale(Locale locale) {
            return TextUtils.getLayoutDirectionFromLocale(locale);
        }
    }

    private TextUtilsCompat() {
    }

    private static int getLayoutDirectionFromFirstChar(Locale locale) {
        switch (Character.getDirectionality(locale.getDisplayName(locale).charAt(0))) {
            case 1:
            case 2:
                return 1;
            default:
                return 0;
        }
    }

    public static int getLayoutDirectionFromLocale(Locale locale) {
        if (Build.VERSION.SDK_INT >= 17) {
            return Api17Impl.getLayoutDirectionFromLocale(locale);
        }
        if (locale == null || locale.equals(ROOT)) {
            return 0;
        }
        String maximizeAndGetScript = ICUCompat.maximizeAndGetScript(locale);
        Log1F380D.a((Object) maximizeAndGetScript);
        if (maximizeAndGetScript == null) {
            return getLayoutDirectionFromFirstChar(locale);
        }
        return (maximizeAndGetScript.equalsIgnoreCase(ARAB_SCRIPT_SUBTAG) || maximizeAndGetScript.equalsIgnoreCase(HEBR_SCRIPT_SUBTAG)) ? 1 : 0;
    }

    public static String htmlEncode(String s) {
        if (Build.VERSION.SDK_INT >= 17) {
            String htmlEncode = TextUtils.htmlEncode(s);
            Log1F380D.a((Object) htmlEncode);
            return htmlEncode;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char charAt = s.charAt(i);
            switch (charAt) {
                case '\"':
                    sb.append("&quot;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '\'':
                    sb.append("&#39;");
                    break;
                case LockFreeTaskQueueCore.FROZEN_SHIFT /*60*/:
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                default:
                    sb.append(charAt);
                    break;
            }
        }
        return sb.toString();
    }
}

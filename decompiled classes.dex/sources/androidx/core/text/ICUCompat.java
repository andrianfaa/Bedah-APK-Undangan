package androidx.core.text;

import android.icu.util.ULocale;
import android.os.Build;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import mt.Log1F380D;

/* compiled from: 0056 */
public final class ICUCompat {
    private static final String TAG = "ICUCompat";
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;

    static class Api21Impl {
        private Api21Impl() {
        }

        static String getScript(Locale locale) {
            return locale.getScript();
        }
    }

    static class Api24Impl {
        private Api24Impl() {
        }

        static ULocale addLikelySubtags(Object loc) {
            return ULocale.addLikelySubtags((ULocale) loc);
        }

        static ULocale forLocale(Locale loc) {
            return ULocale.forLocale(loc);
        }

        static String getScript(Object uLocale) {
            return ((ULocale) uLocale).getScript();
        }
    }

    static {
        if (Build.VERSION.SDK_INT < 21) {
            try {
                Class<?> cls = Class.forName("libcore.icu.ICU");
                sGetScriptMethod = cls.getMethod("getScript", new Class[]{String.class});
                sAddLikelySubtagsMethod = cls.getMethod("addLikelySubtags", new Class[]{String.class});
            } catch (Exception e) {
                sGetScriptMethod = null;
                sAddLikelySubtagsMethod = null;
                Log.w(TAG, e);
            }
        } else if (Build.VERSION.SDK_INT < 24) {
            try {
                sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", new Class[]{Locale.class});
            } catch (Exception e2) {
                throw new IllegalStateException(e2);
            }
        }
    }

    private ICUCompat() {
    }

    private static String addLikelySubtagsBelowApi21(Locale locale) {
        String locale2 = locale.toString();
        try {
            Method method = sAddLikelySubtagsMethod;
            if (method != null) {
                return (String) method.invoke((Object) null, new Object[]{locale2});
            }
        } catch (IllegalAccessException e) {
            Log.w(TAG, e);
        } catch (InvocationTargetException e2) {
            Log.w(TAG, e2);
        }
        return locale2;
    }

    private static String getScriptBelowApi21(String localeStr) {
        try {
            Method method = sGetScriptMethod;
            if (method != null) {
                return (String) method.invoke((Object) null, new Object[]{localeStr});
            }
        } catch (IllegalAccessException e) {
            Log.w(TAG, e);
        } catch (InvocationTargetException e2) {
            Log.w(TAG, e2);
        }
        return null;
    }

    public static String maximizeAndGetScript(Locale locale) {
        if (Build.VERSION.SDK_INT >= 24) {
            String script = Api24Impl.getScript(Api24Impl.addLikelySubtags(Api24Impl.forLocale(locale)));
            Log1F380D.a((Object) script);
            return script;
        } else if (Build.VERSION.SDK_INT >= 21) {
            try {
                String script2 = Api21Impl.getScript((Locale) sAddLikelySubtagsMethod.invoke((Object) null, new Object[]{locale}));
                Log1F380D.a((Object) script2);
                return script2;
            } catch (InvocationTargetException e) {
                Log.w(TAG, e);
                String script3 = Api21Impl.getScript(locale);
                Log1F380D.a((Object) script3);
                return script3;
            } catch (IllegalAccessException e2) {
                Log.w(TAG, e2);
                String script32 = Api21Impl.getScript(locale);
                Log1F380D.a((Object) script32);
                return script32;
            }
        } else {
            String addLikelySubtagsBelowApi21 = addLikelySubtagsBelowApi21(locale);
            Log1F380D.a((Object) addLikelySubtagsBelowApi21);
            if (addLikelySubtagsBelowApi21 == null) {
                return null;
            }
            String scriptBelowApi21 = getScriptBelowApi21(addLikelySubtagsBelowApi21);
            Log1F380D.a((Object) scriptBelowApi21);
            return scriptBelowApi21;
        }
    }
}

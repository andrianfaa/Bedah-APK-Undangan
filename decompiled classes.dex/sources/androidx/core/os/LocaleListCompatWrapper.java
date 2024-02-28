package androidx.core.os;

import android.os.Build;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import mt.Log1F380D;
import okhttp3.HttpUrl;

/* compiled from: 004A */
final class LocaleListCompatWrapper implements LocaleListInterface {
    private static final Locale EN_LATN = LocaleListCompat.forLanguageTagCompat("en-Latn");
    private static final Locale LOCALE_AR_XB = new Locale("ar", "XB");
    private static final Locale LOCALE_EN_XA = new Locale("en", "XA");
    private static final Locale[] sEmptyList = new Locale[0];
    private final Locale[] mList;
    private final String mStringRepresentation;

    static class Api21Impl {
        private Api21Impl() {
        }

        static String getScript(Locale locale) {
            return locale.getScript();
        }
    }

    LocaleListCompatWrapper(Locale... list) {
        if (list.length == 0) {
            this.mList = sEmptyList;
            this.mStringRepresentation = HttpUrl.FRAGMENT_ENCODE_SET;
            return;
        }
        ArrayList arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < list.length) {
            Locale locale = list[i];
            if (locale != null) {
                if (!hashSet.contains(locale)) {
                    Locale locale2 = (Locale) locale.clone();
                    arrayList.add(locale2);
                    toLanguageTag(sb, locale2);
                    if (i < list.length - 1) {
                        sb.append(',');
                    }
                    hashSet.add(locale2);
                }
                i++;
            } else {
                throw new NullPointerException("list[" + i + "] is null");
            }
        }
        this.mList = (Locale[]) arrayList.toArray(new Locale[0]);
        this.mStringRepresentation = sb.toString();
    }

    private Locale computeFirstMatch(Collection<String> collection, boolean assumeEnglishIsSupported) {
        int computeFirstMatchIndex = computeFirstMatchIndex(collection, assumeEnglishIsSupported);
        if (computeFirstMatchIndex == -1) {
            return null;
        }
        return this.mList[computeFirstMatchIndex];
    }

    private int computeFirstMatchIndex(Collection<String> collection, boolean assumeEnglishIsSupported) {
        Locale[] localeArr = this.mList;
        if (localeArr.length == 1) {
            return 0;
        }
        if (localeArr.length == 0) {
            return -1;
        }
        int i = Integer.MAX_VALUE;
        if (assumeEnglishIsSupported) {
            int findFirstMatchIndex = findFirstMatchIndex(EN_LATN);
            if (findFirstMatchIndex == 0) {
                return 0;
            }
            if (findFirstMatchIndex < Integer.MAX_VALUE) {
                i = findFirstMatchIndex;
            }
        }
        for (String forLanguageTagCompat : collection) {
            int findFirstMatchIndex2 = findFirstMatchIndex(LocaleListCompat.forLanguageTagCompat(forLanguageTagCompat));
            if (findFirstMatchIndex2 == 0) {
                return 0;
            }
            if (findFirstMatchIndex2 < i) {
                i = findFirstMatchIndex2;
            }
        }
        if (i == Integer.MAX_VALUE) {
            return 0;
        }
        return i;
    }

    private int findFirstMatchIndex(Locale supportedLocale) {
        int i = 0;
        while (true) {
            Locale[] localeArr = this.mList;
            if (i >= localeArr.length) {
                return Integer.MAX_VALUE;
            }
            if (matchScore(supportedLocale, localeArr[i]) > 0) {
                return i;
            }
            i++;
        }
    }

    private static String getLikelyScript(Locale locale) {
        if (Build.VERSION.SDK_INT < 21) {
            return HttpUrl.FRAGMENT_ENCODE_SET;
        }
        String script = Api21Impl.getScript(locale);
        Log1F380D.a((Object) script);
        return !script.isEmpty() ? script : HttpUrl.FRAGMENT_ENCODE_SET;
    }

    private static boolean isPseudoLocale(Locale locale) {
        return LOCALE_EN_XA.equals(locale) || LOCALE_AR_XB.equals(locale);
    }

    static void toLanguageTag(StringBuilder builder, Locale locale) {
        builder.append(locale.getLanguage());
        String country = locale.getCountry();
        if (country != null && !country.isEmpty()) {
            builder.append('-');
            builder.append(locale.getCountry());
        }
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LocaleListCompatWrapper)) {
            return false;
        }
        Locale[] localeArr = ((LocaleListCompatWrapper) other).mList;
        if (this.mList.length != localeArr.length) {
            return false;
        }
        int i = 0;
        while (true) {
            Locale[] localeArr2 = this.mList;
            if (i >= localeArr2.length) {
                return true;
            }
            if (!localeArr2[i].equals(localeArr[i])) {
                return false;
            }
            i++;
        }
    }

    public Locale get(int index) {
        if (index >= 0) {
            Locale[] localeArr = this.mList;
            if (index < localeArr.length) {
                return localeArr[index];
            }
        }
        return null;
    }

    public Locale getFirstMatch(String[] supportedLocales) {
        return computeFirstMatch(Arrays.asList(supportedLocales), false);
    }

    public Object getLocaleList() {
        return null;
    }

    public int hashCode() {
        int i = 1;
        for (Locale hashCode : this.mList) {
            i = (i * 31) + hashCode.hashCode();
        }
        return i;
    }

    public int indexOf(Locale locale) {
        int i = 0;
        while (true) {
            Locale[] localeArr = this.mList;
            if (i >= localeArr.length) {
                return -1;
            }
            if (localeArr[i].equals(locale)) {
                return i;
            }
            i++;
        }
    }

    public boolean isEmpty() {
        return this.mList.length == 0;
    }

    public int size() {
        return this.mList.length;
    }

    public String toLanguageTags() {
        return this.mStringRepresentation;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int i = 0;
        while (true) {
            Locale[] localeArr = this.mList;
            if (i < localeArr.length) {
                sb.append(localeArr[i]);
                if (i < this.mList.length - 1) {
                    sb.append(',');
                }
                i++;
            } else {
                sb.append("]");
                return sb.toString();
            }
        }
    }

    private static int matchScore(Locale supported, Locale desired) {
        if (supported.equals(desired)) {
            return 1;
        }
        if (!supported.getLanguage().equals(desired.getLanguage()) || isPseudoLocale(supported) || isPseudoLocale(desired)) {
            return 0;
        }
        String likelyScript = getLikelyScript(supported);
        Log1F380D.a((Object) likelyScript);
        if (likelyScript.isEmpty()) {
            String country = supported.getCountry();
            return (country.isEmpty() || country.equals(desired.getCountry())) ? 1 : 0;
        }
        String likelyScript2 = getLikelyScript(desired);
        Log1F380D.a((Object) likelyScript2);
        return likelyScript.equals(likelyScript2) ? 1 : 0;
    }
}

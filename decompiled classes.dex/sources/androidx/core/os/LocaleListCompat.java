package androidx.core.os;

import android.os.Build;
import android.os.LocaleList;
import java.util.Locale;

public final class LocaleListCompat {
    private static final LocaleListCompat sEmptyLocaleList = create(new Locale[0]);
    private final LocaleListInterface mImpl;

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

        static LocaleList createLocaleList(Locale... list) {
            return new LocaleList(list);
        }

        static LocaleList getAdjustedDefault() {
            return LocaleList.getAdjustedDefault();
        }

        static LocaleList getDefault() {
            return LocaleList.getDefault();
        }
    }

    private LocaleListCompat(LocaleListInterface impl) {
        this.mImpl = impl;
    }

    public static LocaleListCompat create(Locale... localeList) {
        return Build.VERSION.SDK_INT >= 24 ? wrap(Api24Impl.createLocaleList(localeList)) : new LocaleListCompat(new LocaleListCompatWrapper(localeList));
    }

    static Locale forLanguageTagCompat(String str) {
        if (str.contains("-")) {
            String[] split = str.split("-", -1);
            if (split.length > 2) {
                return new Locale(split[0], split[1], split[2]);
            }
            if (split.length > 1) {
                return new Locale(split[0], split[1]);
            }
            if (split.length == 1) {
                return new Locale(split[0]);
            }
        } else if (!str.contains("_")) {
            return new Locale(str);
        } else {
            String[] split2 = str.split("_", -1);
            if (split2.length > 2) {
                return new Locale(split2[0], split2[1], split2[2]);
            }
            if (split2.length > 1) {
                return new Locale(split2[0], split2[1]);
            }
            if (split2.length == 1) {
                return new Locale(split2[0]);
            }
        }
        throw new IllegalArgumentException("Can not parse language tag: [" + str + "]");
    }

    public static LocaleListCompat forLanguageTags(String list) {
        if (list == null || list.isEmpty()) {
            return getEmptyLocaleList();
        }
        String[] split = list.split(",", -1);
        Locale[] localeArr = new Locale[split.length];
        for (int i = 0; i < localeArr.length; i++) {
            localeArr[i] = Build.VERSION.SDK_INT >= 21 ? Api21Impl.forLanguageTag(split[i]) : forLanguageTagCompat(split[i]);
        }
        return create(localeArr);
    }

    public static LocaleListCompat getAdjustedDefault() {
        if (Build.VERSION.SDK_INT >= 24) {
            return wrap(Api24Impl.getAdjustedDefault());
        }
        return create(Locale.getDefault());
    }

    public static LocaleListCompat getDefault() {
        if (Build.VERSION.SDK_INT >= 24) {
            return wrap(Api24Impl.getDefault());
        }
        return create(Locale.getDefault());
    }

    public static LocaleListCompat getEmptyLocaleList() {
        return sEmptyLocaleList;
    }

    public static LocaleListCompat wrap(LocaleList localeList) {
        return new LocaleListCompat(new LocaleListPlatformWrapper(localeList));
    }

    @Deprecated
    public static LocaleListCompat wrap(Object localeList) {
        return wrap((LocaleList) localeList);
    }

    public boolean equals(Object other) {
        return (other instanceof LocaleListCompat) && this.mImpl.equals(((LocaleListCompat) other).mImpl);
    }

    public Locale get(int index) {
        return this.mImpl.get(index);
    }

    public Locale getFirstMatch(String[] supportedLocales) {
        return this.mImpl.getFirstMatch(supportedLocales);
    }

    public int hashCode() {
        return this.mImpl.hashCode();
    }

    public int indexOf(Locale locale) {
        return this.mImpl.indexOf(locale);
    }

    public boolean isEmpty() {
        return this.mImpl.isEmpty();
    }

    public int size() {
        return this.mImpl.size();
    }

    public String toLanguageTags() {
        return this.mImpl.toLanguageTags();
    }

    public String toString() {
        return this.mImpl.toString();
    }

    public Object unwrap() {
        return this.mImpl.getLocaleList();
    }
}

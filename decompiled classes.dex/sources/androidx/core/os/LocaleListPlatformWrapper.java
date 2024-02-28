package androidx.core.os;

import android.os.LocaleList;
import java.util.Locale;

final class LocaleListPlatformWrapper implements LocaleListInterface {
    private final LocaleList mLocaleList;

    LocaleListPlatformWrapper(Object localeList) {
        this.mLocaleList = (LocaleList) localeList;
    }

    public boolean equals(Object other) {
        return this.mLocaleList.equals(((LocaleListInterface) other).getLocaleList());
    }

    public Locale get(int index) {
        return this.mLocaleList.get(index);
    }

    public Locale getFirstMatch(String[] supportedLocales) {
        return this.mLocaleList.getFirstMatch(supportedLocales);
    }

    public Object getLocaleList() {
        return this.mLocaleList;
    }

    public int hashCode() {
        return this.mLocaleList.hashCode();
    }

    public int indexOf(Locale locale) {
        return this.mLocaleList.indexOf(locale);
    }

    public boolean isEmpty() {
        return this.mLocaleList.isEmpty();
    }

    public int size() {
        return this.mLocaleList.size();
    }

    public String toLanguageTags() {
        return this.mLocaleList.toLanguageTags();
    }

    public String toString() {
        return this.mLocaleList.toString();
    }
}

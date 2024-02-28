package okhttp3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import mt.Log1F380D;
import okhttp3.internal.HostnamesKt;
import okhttp3.internal.Util;
import okhttp3.internal.http.DatesKt;
import okhttp3.internal.publicsuffix.PublicSuffixDatabase;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u0000 &2\u00020\u0001:\u0002%&BO\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\n\u0012\u0006\u0010\f\u001a\u00020\n\u0012\u0006\u0010\r\u001a\u00020\n¢\u0006\u0002\u0010\u000eJ\r\u0010\u0007\u001a\u00020\u0003H\u0007¢\u0006\u0002\b\u0012J\u0013\u0010\u0013\u001a\u00020\n2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001H\u0002J\r\u0010\u0005\u001a\u00020\u0006H\u0007¢\u0006\u0002\b\u0015J\b\u0010\u0016\u001a\u00020\u0017H\u0017J\r\u0010\r\u001a\u00020\nH\u0007¢\u0006\u0002\b\u0018J\r\u0010\u000b\u001a\u00020\nH\u0007¢\u0006\u0002\b\u0019J\u000e\u0010\u001a\u001a\u00020\n2\u0006\u0010\u001b\u001a\u00020\u001cJ\r\u0010\u0002\u001a\u00020\u0003H\u0007¢\u0006\u0002\b\u001dJ\r\u0010\b\u001a\u00020\u0003H\u0007¢\u0006\u0002\b\u001eJ\r\u0010\f\u001a\u00020\nH\u0007¢\u0006\u0002\b\u001fJ\r\u0010\t\u001a\u00020\nH\u0007¢\u0006\u0002\b J\b\u0010!\u001a\u00020\u0003H\u0016J\u0015\u0010!\u001a\u00020\u00032\u0006\u0010\"\u001a\u00020\nH\u0000¢\u0006\u0002\b#J\r\u0010\u0004\u001a\u00020\u0003H\u0007¢\u0006\u0002\b$R\u0013\u0010\u0007\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u000fR\u0013\u0010\u0005\u001a\u00020\u00068\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0010R\u0013\u0010\r\u001a\u00020\n8\u0007¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u0011R\u0013\u0010\u000b\u001a\u00020\n8\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0011R\u0013\u0010\u0002\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u000fR\u0013\u0010\b\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u000fR\u0013\u0010\f\u001a\u00020\n8\u0007¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0011R\u0013\u0010\t\u001a\u00020\n8\u0007¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0011R\u0013\u0010\u0004\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u000f¨\u0006'"}, d2 = {"Lokhttp3/Cookie;", "", "name", "", "value", "expiresAt", "", "domain", "path", "secure", "", "httpOnly", "persistent", "hostOnly", "(Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;ZZZZ)V", "()Ljava/lang/String;", "()J", "()Z", "-deprecated_domain", "equals", "other", "-deprecated_expiresAt", "hashCode", "", "-deprecated_hostOnly", "-deprecated_httpOnly", "matches", "url", "Lokhttp3/HttpUrl;", "-deprecated_name", "-deprecated_path", "-deprecated_persistent", "-deprecated_secure", "toString", "forObsoleteRfc2965", "toString$okhttp", "-deprecated_value", "Builder", "Companion", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: 01B6 */
public final class Cookie {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final Pattern DAY_OF_MONTH_PATTERN = Pattern.compile("(\\d{1,2})[^\\d]*");
    /* access modifiers changed from: private */
    public static final Pattern MONTH_PATTERN = Pattern.compile("(?i)(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec).*");
    /* access modifiers changed from: private */
    public static final Pattern TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})[^\\d]*");
    /* access modifiers changed from: private */
    public static final Pattern YEAR_PATTERN = Pattern.compile("(\\d{2,4})[^\\d]*");
    private final String domain;
    private final long expiresAt;
    private final boolean hostOnly;
    private final boolean httpOnly;
    private final String name;
    private final String path;
    private final boolean persistent;
    private final boolean secure;
    private final String value;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0003\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0004J\u0018\u0010\u0003\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u000e\u0010\u0005\u001a\u00020\u00002\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0011\u001a\u00020\u00002\u0006\u0010\u0003\u001a\u00020\u0004J\u0006\u0010\t\u001a\u00020\u0000J\u000e\u0010\n\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u0004J\u000e\u0010\u000b\u001a\u00020\u00002\u0006\u0010\u000b\u001a\u00020\u0004J\u0006\u0010\r\u001a\u00020\u0000J\u000e\u0010\u000e\u001a\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lokhttp3/Cookie$Builder;", "", "()V", "domain", "", "expiresAt", "", "hostOnly", "", "httpOnly", "name", "path", "persistent", "secure", "value", "build", "Lokhttp3/Cookie;", "hostOnlyDomain", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: 01B4 */
    public static final class Builder {
        private String domain;
        private long expiresAt = DatesKt.MAX_DATE;
        private boolean hostOnly;
        private boolean httpOnly;
        private String name;
        private String path = "/";
        private boolean persistent;
        private boolean secure;
        private String value;

        private final Builder domain(String domain2, boolean hostOnly2) {
            Builder builder = this;
            String canonicalHost = HostnamesKt.toCanonicalHost(domain2);
            Log1F380D.a((Object) canonicalHost);
            if (canonicalHost != null) {
                builder.domain = canonicalHost;
                builder.hostOnly = hostOnly2;
                return this;
            }
            throw new IllegalArgumentException("unexpected domain: " + domain2);
        }

        public final Cookie build() {
            String str = this.name;
            if (str != null) {
                String str2 = this.value;
                if (str2 != null) {
                    long j = this.expiresAt;
                    String str3 = this.domain;
                    if (str3 != null) {
                        return new Cookie(str, str2, j, str3, this.path, this.secure, this.httpOnly, this.persistent, this.hostOnly, (DefaultConstructorMarker) null);
                    }
                    throw new NullPointerException("builder.domain == null");
                }
                throw new NullPointerException("builder.value == null");
            }
            throw new NullPointerException("builder.name == null");
        }

        public final Builder domain(String domain2) {
            Intrinsics.checkNotNullParameter(domain2, "domain");
            return domain(domain2, false);
        }

        public final Builder expiresAt(long expiresAt2) {
            Builder builder = this;
            long j = expiresAt2;
            if (j <= 0) {
                j = Long.MIN_VALUE;
            }
            if (j > DatesKt.MAX_DATE) {
                j = DatesKt.MAX_DATE;
            }
            builder.expiresAt = j;
            builder.persistent = true;
            return this;
        }

        public final Builder hostOnlyDomain(String domain2) {
            Intrinsics.checkNotNullParameter(domain2, "domain");
            return domain(domain2, true);
        }

        public final Builder httpOnly() {
            this.httpOnly = true;
            return this;
        }

        public final Builder name(String name2) {
            Intrinsics.checkNotNullParameter(name2, "name");
            Builder builder = this;
            if (Intrinsics.areEqual((Object) StringsKt.trim((CharSequence) name2).toString(), (Object) name2)) {
                builder.name = name2;
                return this;
            }
            throw new IllegalArgumentException("name is not trimmed".toString());
        }

        public final Builder path(String path2) {
            Intrinsics.checkNotNullParameter(path2, "path");
            Builder builder = this;
            if (StringsKt.startsWith$default(path2, "/", false, 2, (Object) null)) {
                builder.path = path2;
                return this;
            }
            throw new IllegalArgumentException("path must start with '/'".toString());
        }

        public final Builder secure() {
            this.secure = true;
            return this;
        }

        public final Builder value(String value2) {
            Intrinsics.checkNotNullParameter(value2, "value");
            Builder builder = this;
            if (Intrinsics.areEqual((Object) StringsKt.trim((CharSequence) value2).toString(), (Object) value2)) {
                builder.value = value2;
                return this;
            }
            throw new IllegalArgumentException("value is not trimmed".toString());
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J(\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0018\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\fH\u0002J'\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\fH\u0000¢\u0006\u0002\b\u001bJ\u001a\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\fH\u0007J\u001e\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00150\u001d2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001e\u001a\u00020\u001fH\u0007J\u0010\u0010 \u001a\u00020\f2\u0006\u0010!\u001a\u00020\fH\u0002J \u0010\"\u001a\u00020\u00172\u0006\u0010!\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\nH\u0002J\u0010\u0010#\u001a\u00020\u00172\u0006\u0010!\u001a\u00020\fH\u0002J\u0018\u0010$\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010%\u001a\u00020\fH\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006&"}, d2 = {"Lokhttp3/Cookie$Companion;", "", "()V", "DAY_OF_MONTH_PATTERN", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "MONTH_PATTERN", "TIME_PATTERN", "YEAR_PATTERN", "dateCharacterOffset", "", "input", "", "pos", "limit", "invert", "", "domainMatch", "urlHost", "domain", "parse", "Lokhttp3/Cookie;", "currentTimeMillis", "", "url", "Lokhttp3/HttpUrl;", "setCookie", "parse$okhttp", "parseAll", "", "headers", "Lokhttp3/Headers;", "parseDomain", "s", "parseExpires", "parseMaxAge", "pathMatch", "path", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: 01B5 */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private final int dateCharacterOffset(String input, int pos, int limit, boolean invert) {
            for (int i = pos; i < limit; i++) {
                char charAt = input.charAt(i);
                if (((charAt < ' ' && charAt != 9) || charAt >= 127 || ('0' <= charAt && '9' >= charAt) || (('a' <= charAt && 'z' >= charAt) || (('A' <= charAt && 'Z' >= charAt) || charAt == ':'))) == (!invert)) {
                    return i;
                }
            }
            return limit;
        }

        /* access modifiers changed from: private */
        public final boolean domainMatch(String urlHost, String domain) {
            if (Intrinsics.areEqual((Object) urlHost, (Object) domain)) {
                return true;
            }
            return StringsKt.endsWith$default(urlHost, domain, false, 2, (Object) null) && urlHost.charAt((urlHost.length() - domain.length()) - 1) == '.' && !Util.canParseAsIpAddress(urlHost);
        }

        private final String parseDomain(String s) {
            if (!StringsKt.endsWith$default(s, ".", false, 2, (Object) null)) {
                String removePrefix = StringsKt.removePrefix(s, (CharSequence) ".");
                Log1F380D.a((Object) removePrefix);
                String canonicalHost = HostnamesKt.toCanonicalHost(removePrefix);
                if (canonicalHost != null) {
                    return canonicalHost;
                }
                throw new IllegalArgumentException();
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }

        private final long parseExpires(String s, int pos, int limit) {
            String str = s;
            int i = limit;
            int dateCharacterOffset = dateCharacterOffset(str, pos, i, false);
            int i2 = -1;
            int i3 = -1;
            int i4 = -1;
            int i5 = -1;
            int i6 = -1;
            int i7 = -1;
            Matcher matcher = Cookie.TIME_PATTERN.matcher(str);
            while (dateCharacterOffset < i) {
                int dateCharacterOffset2 = dateCharacterOffset(str, dateCharacterOffset + 1, i, true);
                matcher.region(dateCharacterOffset, dateCharacterOffset2);
                if (i2 == -1 && matcher.usePattern(Cookie.TIME_PATTERN).matches()) {
                    String group = matcher.group(1);
                    Intrinsics.checkNotNullExpressionValue(group, "matcher.group(1)");
                    i2 = Integer.parseInt(group);
                    String group2 = matcher.group(2);
                    Intrinsics.checkNotNullExpressionValue(group2, "matcher.group(2)");
                    int parseInt = Integer.parseInt(group2);
                    String group3 = matcher.group(3);
                    Intrinsics.checkNotNullExpressionValue(group3, "matcher.group(3)");
                    i4 = Integer.parseInt(group3);
                    i3 = parseInt;
                } else if (i5 == -1 && matcher.usePattern(Cookie.DAY_OF_MONTH_PATTERN).matches()) {
                    String group4 = matcher.group(1);
                    Intrinsics.checkNotNullExpressionValue(group4, "matcher.group(1)");
                    i5 = Integer.parseInt(group4);
                } else if (i6 == -1 && matcher.usePattern(Cookie.MONTH_PATTERN).matches()) {
                    String group5 = matcher.group(1);
                    Intrinsics.checkNotNullExpressionValue(group5, "matcher.group(1)");
                    Locale locale = Locale.US;
                    Intrinsics.checkNotNullExpressionValue(locale, "Locale.US");
                    if (group5 != null) {
                        String lowerCase = group5.toLowerCase(locale);
                        Intrinsics.checkNotNullExpressionValue(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
                        String str2 = lowerCase;
                        String pattern = Cookie.MONTH_PATTERN.pattern();
                        Intrinsics.checkNotNullExpressionValue(pattern, "MONTH_PATTERN.pattern()");
                        i6 = StringsKt.indexOf$default((CharSequence) pattern, str2, 0, false, 6, (Object) null) / 4;
                    } else {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                } else if (i7 == -1 && matcher.usePattern(Cookie.YEAR_PATTERN).matches()) {
                    String group6 = matcher.group(1);
                    Intrinsics.checkNotNullExpressionValue(group6, "matcher.group(1)");
                    i7 = Integer.parseInt(group6);
                }
                dateCharacterOffset = dateCharacterOffset(str, dateCharacterOffset2 + 1, i, false);
            }
            if (70 <= i7 && 99 >= i7) {
                i7 += 1900;
            }
            if (i7 >= 0 && 69 >= i7) {
                i7 += 2000;
            }
            if (i7 >= 1601) {
                if (i6 != -1) {
                    if (1 <= i5 && 31 >= i5) {
                        if (i2 >= 0 && 23 >= i2) {
                            if (i3 >= 0 && 59 >= i3) {
                                if (i4 >= 0 && 59 >= i4) {
                                    GregorianCalendar gregorianCalendar = new GregorianCalendar(Util.UTC);
                                    gregorianCalendar.setLenient(false);
                                    gregorianCalendar.set(1, i7);
                                    gregorianCalendar.set(2, i6 - 1);
                                    gregorianCalendar.set(5, i5);
                                    gregorianCalendar.set(11, i2);
                                    gregorianCalendar.set(12, i3);
                                    gregorianCalendar.set(13, i4);
                                    gregorianCalendar.set(14, 0);
                                    return gregorianCalendar.getTimeInMillis();
                                }
                                throw new IllegalArgumentException("Failed requirement.".toString());
                            }
                            throw new IllegalArgumentException("Failed requirement.".toString());
                        }
                        throw new IllegalArgumentException("Failed requirement.".toString());
                    }
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }

        private final long parseMaxAge(String s) {
            try {
                long parseLong = Long.parseLong(s);
                if (parseLong <= 0) {
                    return Long.MIN_VALUE;
                }
                return parseLong;
            } catch (NumberFormatException e) {
                if (new Regex("-?\\d+").matches(s)) {
                    return StringsKt.startsWith$default(s, "-", false, 2, (Object) null) ? Long.MIN_VALUE : Long.MAX_VALUE;
                }
                throw e;
            }
        }

        /* access modifiers changed from: private */
        public final boolean pathMatch(HttpUrl url, String path) {
            String encodedPath = url.encodedPath();
            if (Intrinsics.areEqual((Object) encodedPath, (Object) path)) {
                return true;
            }
            return StringsKt.startsWith$default(encodedPath, path, false, 2, (Object) null) && (StringsKt.endsWith$default(path, "/", false, 2, (Object) null) || encodedPath.charAt(path.length()) == '/');
        }

        @JvmStatic
        public final Cookie parse(HttpUrl url, String setCookie) {
            Intrinsics.checkNotNullParameter(url, "url");
            Intrinsics.checkNotNullParameter(setCookie, "setCookie");
            return parse$okhttp(System.currentTimeMillis(), url, setCookie);
        }

        @JvmStatic
        public final List<Cookie> parseAll(HttpUrl url, Headers headers) {
            Intrinsics.checkNotNullParameter(url, "url");
            Intrinsics.checkNotNullParameter(headers, "headers");
            List<String> values = headers.values("Set-Cookie");
            List list = null;
            int size = values.size();
            for (int i = 0; i < size; i++) {
                Cookie parse = parse(url, values.get(i));
                if (parse != null) {
                    if (list == null) {
                        list = new ArrayList();
                    }
                    list.add(parse);
                }
            }
            if (list == null) {
                return CollectionsKt.emptyList();
            }
            List<Cookie> unmodifiableList = Collections.unmodifiableList(list);
            Intrinsics.checkNotNullExpressionValue(unmodifiableList, "Collections.unmodifiableList(cookies)");
            return unmodifiableList;
        }

        public final Cookie parse$okhttp(long currentTimeMillis, HttpUrl url, String setCookie) {
            long j;
            String str;
            String str2;
            int i;
            int i2;
            boolean z;
            String str3 = setCookie;
            Intrinsics.checkNotNullParameter(url, "url");
            Intrinsics.checkNotNullParameter(str3, "setCookie");
            String str4 = setCookie;
            int delimiterOffset$default = Util.delimiterOffset$default(str4, ';', 0, 0, 6, (Object) null);
            int delimiterOffset$default2 = Util.delimiterOffset$default(str4, '=', 0, delimiterOffset$default, 2, (Object) null);
            if (delimiterOffset$default2 == delimiterOffset$default) {
                return null;
            }
            boolean z2 = true;
            String trimSubstring$default = Util.trimSubstring$default(str3, 0, delimiterOffset$default2, 1, (Object) null);
            Log1F380D.a((Object) trimSubstring$default);
            if (trimSubstring$default.length() == 0) {
                int i3 = delimiterOffset$default2;
                return null;
            } else if (Util.indexOfControlOrNonAscii(trimSubstring$default) != -1) {
                int i4 = delimiterOffset$default2;
                return null;
            } else {
                String trimSubstring = Util.trimSubstring(str3, delimiterOffset$default2 + 1, delimiterOffset$default);
                Log1F380D.a((Object) trimSubstring);
                if (Util.indexOfControlOrNonAscii(trimSubstring) != -1) {
                    return null;
                }
                int length = setCookie.length();
                long j2 = -1;
                boolean z3 = false;
                boolean z4 = false;
                boolean z5 = true;
                boolean z6 = false;
                int i5 = delimiterOffset$default + 1;
                long j3 = 253402300799999L;
                String str5 = null;
                String str6 = null;
                while (i5 < length) {
                    int delimiterOffset = Util.delimiterOffset(str3, ';', i5, length);
                    int delimiterOffset2 = Util.delimiterOffset(str3, '=', i5, delimiterOffset);
                    String trimSubstring2 = Util.trimSubstring(str3, i5, delimiterOffset2);
                    Log1F380D.a((Object) trimSubstring2);
                    if (delimiterOffset2 < delimiterOffset) {
                        str2 = Util.trimSubstring(str3, delimiterOffset2 + 1, delimiterOffset);
                        Log1F380D.a((Object) str2);
                    } else {
                        str2 = HttpUrl.FRAGMENT_ENCODE_SET;
                    }
                    String str7 = str2;
                    if (StringsKt.equals(trimSubstring2, "expires", z2)) {
                        try {
                            i2 = delimiterOffset$default2;
                            i = length;
                            try {
                                j3 = parseExpires(str7, 0, str7.length());
                                z6 = true;
                                z = true;
                            } catch (IllegalArgumentException e) {
                                z = true;
                                i5 = delimiterOffset + 1;
                                z2 = z;
                                delimiterOffset$default2 = i2;
                                length = i;
                            }
                        } catch (IllegalArgumentException e2) {
                            i2 = delimiterOffset$default2;
                            i = length;
                            String str8 = str7;
                            z = true;
                            i5 = delimiterOffset + 1;
                            z2 = z;
                            delimiterOffset$default2 = i2;
                            length = i;
                        }
                    } else {
                        i2 = delimiterOffset$default2;
                        i = length;
                        String str9 = str7;
                        if (StringsKt.equals(trimSubstring2, "max-age", true)) {
                            try {
                                z6 = true;
                                j2 = parseMaxAge(str9);
                                z = true;
                            } catch (NumberFormatException e3) {
                                z = true;
                            }
                        } else if (StringsKt.equals(trimSubstring2, "domain", true)) {
                            try {
                                str6 = parseDomain(str9);
                                z5 = false;
                                z = true;
                            } catch (IllegalArgumentException e4) {
                                z = true;
                            }
                        } else {
                            z = true;
                            if (StringsKt.equals(trimSubstring2, "path", true)) {
                                str5 = str9;
                            } else if (StringsKt.equals(trimSubstring2, "secure", true)) {
                                z3 = true;
                            } else if (StringsKt.equals(trimSubstring2, "httponly", true)) {
                                z4 = true;
                            }
                        }
                    }
                    i5 = delimiterOffset + 1;
                    z2 = z;
                    delimiterOffset$default2 = i2;
                    length = i;
                }
                int i6 = delimiterOffset$default2;
                int i7 = length;
                if (j2 == Long.MIN_VALUE) {
                    j = Long.MIN_VALUE;
                } else if (j2 != -1) {
                    long j4 = currentTimeMillis + (j2 <= 9223372036854775L ? ((long) 1000) * j2 : Long.MAX_VALUE);
                    j = (j4 < currentTimeMillis || j4 > DatesKt.MAX_DATE) ? 253402300799999L : j4;
                } else {
                    j = j3;
                }
                String host = url.host();
                if (str6 == null) {
                    str6 = host;
                } else if (!domainMatch(host, str6)) {
                    return null;
                }
                if (host.length() != str6.length() && PublicSuffixDatabase.Companion.get().getEffectiveTldPlusOne(str6) == null) {
                    return null;
                }
                String str10 = "/";
                if (str5 == null || !StringsKt.startsWith$default(str5, str10, false, 2, (Object) null)) {
                    String encodedPath = url.encodedPath();
                    int lastIndexOf$default = StringsKt.lastIndexOf$default((CharSequence) encodedPath, '/', 0, false, 6, (Object) null);
                    if (lastIndexOf$default != 0) {
                        if (encodedPath != null) {
                            str10 = encodedPath.substring(0, lastIndexOf$default);
                            Intrinsics.checkNotNullExpressionValue(str10, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                        } else {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                    }
                    str = str10;
                } else {
                    str = str5;
                }
                int i8 = i5;
                return new Cookie(trimSubstring$default, trimSubstring, j, str6, str, z3, z4, z6, z5, (DefaultConstructorMarker) null);
            }
        }
    }

    private Cookie(String name2, String value2, long expiresAt2, String domain2, String path2, boolean secure2, boolean httpOnly2, boolean persistent2, boolean hostOnly2) {
        this.name = name2;
        this.value = value2;
        this.expiresAt = expiresAt2;
        this.domain = domain2;
        this.path = path2;
        this.secure = secure2;
        this.httpOnly = httpOnly2;
        this.persistent = persistent2;
        this.hostOnly = hostOnly2;
    }

    public /* synthetic */ Cookie(String name2, String value2, long expiresAt2, String domain2, String path2, boolean secure2, boolean httpOnly2, boolean persistent2, boolean hostOnly2, DefaultConstructorMarker $constructor_marker) {
        this(name2, value2, expiresAt2, domain2, path2, secure2, httpOnly2, persistent2, hostOnly2);
    }

    @JvmStatic
    public static final Cookie parse(HttpUrl httpUrl, String str) {
        return Companion.parse(httpUrl, str);
    }

    @JvmStatic
    public static final List<Cookie> parseAll(HttpUrl httpUrl, Headers headers) {
        return Companion.parseAll(httpUrl, headers);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "domain", imports = {}))
    /* renamed from: -deprecated_domain  reason: not valid java name */
    public final String m1620deprecated_domain() {
        return this.domain;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "expiresAt", imports = {}))
    /* renamed from: -deprecated_expiresAt  reason: not valid java name */
    public final long m1621deprecated_expiresAt() {
        return this.expiresAt;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "hostOnly", imports = {}))
    /* renamed from: -deprecated_hostOnly  reason: not valid java name */
    public final boolean m1622deprecated_hostOnly() {
        return this.hostOnly;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "httpOnly", imports = {}))
    /* renamed from: -deprecated_httpOnly  reason: not valid java name */
    public final boolean m1623deprecated_httpOnly() {
        return this.httpOnly;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "name", imports = {}))
    /* renamed from: -deprecated_name  reason: not valid java name */
    public final String m1624deprecated_name() {
        return this.name;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "path", imports = {}))
    /* renamed from: -deprecated_path  reason: not valid java name */
    public final String m1625deprecated_path() {
        return this.path;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "persistent", imports = {}))
    /* renamed from: -deprecated_persistent  reason: not valid java name */
    public final boolean m1626deprecated_persistent() {
        return this.persistent;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "secure", imports = {}))
    /* renamed from: -deprecated_secure  reason: not valid java name */
    public final boolean m1627deprecated_secure() {
        return this.secure;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "value", imports = {}))
    /* renamed from: -deprecated_value  reason: not valid java name */
    public final String m1628deprecated_value() {
        return this.value;
    }

    public final String domain() {
        return this.domain;
    }

    public boolean equals(Object other) {
        return (other instanceof Cookie) && Intrinsics.areEqual((Object) ((Cookie) other).name, (Object) this.name) && Intrinsics.areEqual((Object) ((Cookie) other).value, (Object) this.value) && ((Cookie) other).expiresAt == this.expiresAt && Intrinsics.areEqual((Object) ((Cookie) other).domain, (Object) this.domain) && Intrinsics.areEqual((Object) ((Cookie) other).path, (Object) this.path) && ((Cookie) other).secure == this.secure && ((Cookie) other).httpOnly == this.httpOnly && ((Cookie) other).persistent == this.persistent && ((Cookie) other).hostOnly == this.hostOnly;
    }

    public final long expiresAt() {
        return this.expiresAt;
    }

    public int hashCode() {
        return (((((((((((((((((17 * 31) + this.name.hashCode()) * 31) + this.value.hashCode()) * 31) + Long.hashCode(this.expiresAt)) * 31) + this.domain.hashCode()) * 31) + this.path.hashCode()) * 31) + Boolean.hashCode(this.secure)) * 31) + Boolean.hashCode(this.httpOnly)) * 31) + Boolean.hashCode(this.persistent)) * 31) + Boolean.hashCode(this.hostOnly);
    }

    public final boolean hostOnly() {
        return this.hostOnly;
    }

    public final boolean httpOnly() {
        return this.httpOnly;
    }

    public final boolean matches(HttpUrl url) {
        Intrinsics.checkNotNullParameter(url, "url");
        if ((this.hostOnly ? Intrinsics.areEqual((Object) url.host(), (Object) this.domain) : Companion.domainMatch(url.host(), this.domain)) && Companion.pathMatch(url, this.path)) {
            return !this.secure || url.isHttps();
        }
        return false;
    }

    public final String name() {
        return this.name;
    }

    public final String path() {
        return this.path;
    }

    public final boolean persistent() {
        return this.persistent;
    }

    public final boolean secure() {
        return this.secure;
    }

    public String toString() {
        return toString$okhttp(false);
    }

    public final String toString$okhttp(boolean forObsoleteRfc2965) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append('=');
        sb.append(this.value);
        if (this.persistent) {
            if (this.expiresAt == Long.MIN_VALUE) {
                sb.append("; max-age=0");
            } else {
                StringBuilder append = sb.append("; expires=");
                String httpDateString = DatesKt.toHttpDateString(new Date(this.expiresAt));
                Log1F380D.a((Object) httpDateString);
                append.append(httpDateString);
            }
        }
        if (!this.hostOnly) {
            sb.append("; domain=");
            if (forObsoleteRfc2965) {
                sb.append(".");
            }
            sb.append(this.domain);
        }
        sb.append("; path=").append(this.path);
        if (this.secure) {
            sb.append("; secure");
        }
        if (this.httpOnly) {
            sb.append("; httponly");
        }
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "toString()");
        return sb2;
    }

    public final String value() {
        return this.value;
    }
}

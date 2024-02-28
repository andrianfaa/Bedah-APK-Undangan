package okhttp3;

import androidx.core.text.HtmlCompat;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.UByte;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.RangesKt;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import kotlin.text.Typography;
import mt.Log1F380D;
import okhttp3.internal.HostnamesKt;
import okhttp3.internal.Util;
import okhttp3.internal.publicsuffix.PublicSuffixDatabase;
import okio.Buffer;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\"\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 J2\u00020\u0001:\u0002IJBa\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\n\u0012\u0010\u0010\u000b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0018\u00010\n\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\r\u001a\u00020\u0003¢\u0006\u0002\u0010\u000eJ\u000f\u0010\u000f\u001a\u0004\u0018\u00010\u0003H\u0007¢\u0006\u0002\b!J\r\u0010\u0011\u001a\u00020\u0003H\u0007¢\u0006\u0002\b\"J\r\u0010\u0012\u001a\u00020\u0003H\u0007¢\u0006\u0002\b#J\u0013\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\nH\u0007¢\u0006\u0002\b$J\u000f\u0010\u0015\u001a\u0004\u0018\u00010\u0003H\u0007¢\u0006\u0002\b%J\r\u0010\u0016\u001a\u00020\u0003H\u0007¢\u0006\u0002\b&J\u0013\u0010'\u001a\u00020\u00182\b\u0010(\u001a\u0004\u0018\u00010\u0001H\u0002J\u000f\u0010\f\u001a\u0004\u0018\u00010\u0003H\u0007¢\u0006\u0002\b)J\b\u0010*\u001a\u00020\bH\u0016J\r\u0010\u0006\u001a\u00020\u0003H\u0007¢\u0006\u0002\b+J\u0006\u0010,\u001a\u00020-J\u0010\u0010,\u001a\u0004\u0018\u00010-2\u0006\u0010.\u001a\u00020\u0003J\r\u0010\u0005\u001a\u00020\u0003H\u0007¢\u0006\u0002\b/J\u0013\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\nH\u0007¢\u0006\u0002\b0J\r\u0010\u001a\u001a\u00020\bH\u0007¢\u0006\u0002\b1J\r\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\b2J\u000f\u0010\u001c\u001a\u0004\u0018\u00010\u0003H\u0007¢\u0006\u0002\b3J\u0010\u00104\u001a\u0004\u0018\u00010\u00032\u0006\u00105\u001a\u00020\u0003J\u000e\u00106\u001a\u00020\u00032\u0006\u00107\u001a\u00020\bJ\u0013\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00030\u001eH\u0007¢\u0006\u0002\b8J\u0010\u00109\u001a\u0004\u0018\u00010\u00032\u0006\u00107\u001a\u00020\bJ\u0016\u0010:\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00030\n2\u0006\u00105\u001a\u00020\u0003J\r\u0010 \u001a\u00020\bH\u0007¢\u0006\u0002\b;J\u0006\u0010<\u001a\u00020\u0003J\u0010\u0010=\u001a\u0004\u0018\u00010\u00002\u0006\u0010.\u001a\u00020\u0003J\r\u0010\u0002\u001a\u00020\u0003H\u0007¢\u0006\u0002\b>J\b\u0010?\u001a\u00020\u0003H\u0016J\r\u0010@\u001a\u00020AH\u0007¢\u0006\u0002\bBJ\r\u0010C\u001a\u00020DH\u0007¢\u0006\u0002\b\rJ\b\u0010E\u001a\u0004\u0018\u00010\u0003J\r\u0010B\u001a\u00020AH\u0007¢\u0006\u0002\bFJ\r\u0010\r\u001a\u00020DH\u0007¢\u0006\u0002\bGJ\r\u0010\u0004\u001a\u00020\u0003H\u0007¢\u0006\u0002\bHR\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u00038G¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0011\u001a\u00020\u00038G¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\u0012\u001a\u00020\u00038G¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0010R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\n8G¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u0013\u0010\u0015\u001a\u0004\u0018\u00010\u00038G¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0010R\u0011\u0010\u0016\u001a\u00020\u00038G¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0010R\u0015\u0010\f\u001a\u0004\u0018\u00010\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0010R\u0013\u0010\u0006\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0010R\u0011\u0010\u0017\u001a\u00020\u0018¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0019R\u0013\u0010\u0005\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0010R\u0019\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\n8\u0007¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0014R\u0011\u0010\u001a\u001a\u00020\b8G¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u0013\u0010\u0007\u001a\u00020\b8\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u001bR\u0013\u0010\u001c\u001a\u0004\u0018\u00010\u00038G¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u0010R\u0018\u0010\u000b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0018\u00010\nX\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00030\u001e8G¢\u0006\u0006\u001a\u0004\b\u001d\u0010\u001fR\u0011\u0010 \u001a\u00020\b8G¢\u0006\u0006\u001a\u0004\b \u0010\u001bR\u0013\u0010\u0002\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0010R\u000e\u0010\r\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0013\u0010\u0004\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0010¨\u0006K"}, d2 = {"Lokhttp3/HttpUrl;", "", "scheme", "", "username", "password", "host", "port", "", "pathSegments", "", "queryNamesAndValues", "fragment", "url", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/util/List;Ljava/util/List;Ljava/lang/String;Ljava/lang/String;)V", "encodedFragment", "()Ljava/lang/String;", "encodedPassword", "encodedPath", "encodedPathSegments", "()Ljava/util/List;", "encodedQuery", "encodedUsername", "isHttps", "", "()Z", "pathSize", "()I", "query", "queryParameterNames", "", "()Ljava/util/Set;", "querySize", "-deprecated_encodedFragment", "-deprecated_encodedPassword", "-deprecated_encodedPath", "-deprecated_encodedPathSegments", "-deprecated_encodedQuery", "-deprecated_encodedUsername", "equals", "other", "-deprecated_fragment", "hashCode", "-deprecated_host", "newBuilder", "Lokhttp3/HttpUrl$Builder;", "link", "-deprecated_password", "-deprecated_pathSegments", "-deprecated_pathSize", "-deprecated_port", "-deprecated_query", "queryParameter", "name", "queryParameterName", "index", "-deprecated_queryParameterNames", "queryParameterValue", "queryParameterValues", "-deprecated_querySize", "redact", "resolve", "-deprecated_scheme", "toString", "toUri", "Ljava/net/URI;", "uri", "toUrl", "Ljava/net/URL;", "topPrivateDomain", "-deprecated_uri", "-deprecated_url", "-deprecated_username", "Builder", "Companion", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: HttpUrl.kt */
public final class HttpUrl {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String FORM_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#&!$(),~";
    public static final String FRAGMENT_ENCODE_SET = "";
    public static final String FRAGMENT_ENCODE_SET_URI = " \"#<>\\^`{|}";
    /* access modifiers changed from: private */
    public static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    public static final String PASSWORD_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
    public static final String PATH_SEGMENT_ENCODE_SET = " \"<>^`{}|/\\?#";
    public static final String PATH_SEGMENT_ENCODE_SET_URI = "[]";
    public static final String QUERY_COMPONENT_ENCODE_SET = " !\"#$&'(),/:;<=>?@[]\\^`{|}~";
    public static final String QUERY_COMPONENT_ENCODE_SET_URI = "\\^`{|}";
    public static final String QUERY_COMPONENT_REENCODE_SET = " \"'<>#&=";
    public static final String QUERY_ENCODE_SET = " \"'<>#";
    public static final String USERNAME_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
    private final String fragment;
    private final String host;
    private final boolean isHttps;
    private final String password;
    private final List<String> pathSegments;
    private final int port;
    private final List<String> queryNamesAndValues;
    private final String scheme;
    private final String url;
    private final String username;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010!\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0017\u0018\u0000 V2\u00020\u0001:\u0001VB\u0005¢\u0006\u0002\u0010\u0002J\u000e\u0010#\u001a\u00020\u00002\u0006\u0010$\u001a\u00020\u0004J\u000e\u0010%\u001a\u00020\u00002\u0006\u0010\f\u001a\u00020\u0004J\u0018\u0010&\u001a\u00020\u00002\u0006\u0010'\u001a\u00020\u00042\b\u0010(\u001a\u0004\u0018\u00010\u0004J\u000e\u0010)\u001a\u00020\u00002\u0006\u0010*\u001a\u00020\u0004J\u000e\u0010+\u001a\u00020\u00002\u0006\u0010,\u001a\u00020\u0004J\u0018\u0010+\u001a\u00020\u00002\u0006\u0010,\u001a\u00020\u00042\u0006\u0010-\u001a\u00020.H\u0002J\u0018\u0010/\u001a\u00020\u00002\u0006\u00100\u001a\u00020\u00042\b\u00101\u001a\u0004\u0018\u00010\u0004J\u0006\u00102\u001a\u000203J\b\u00104\u001a\u00020\u001bH\u0002J\u0010\u0010\u0003\u001a\u00020\u00002\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\t\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0004J\u000e\u00105\u001a\u00020\u00002\u0006\u00105\u001a\u00020\u0004J\u0010\u00106\u001a\u00020\u00002\b\u00106\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u0014\u001a\u00020\u00002\u0006\u0010\u0014\u001a\u00020\u0004J\u0010\u00107\u001a\u00020\u00002\b\u00107\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0017\u001a\u00020\u0004J\u0010\u00108\u001a\u00020.2\u0006\u00109\u001a\u00020\u0004H\u0002J\u0010\u0010:\u001a\u00020.2\u0006\u00109\u001a\u00020\u0004H\u0002J\u001f\u0010;\u001a\u00020\u00002\b\u0010<\u001a\u0004\u0018\u0001032\u0006\u00109\u001a\u00020\u0004H\u0000¢\u0006\u0002\b=J\u000e\u0010>\u001a\u00020\u00002\u0006\u0010>\u001a\u00020\u0004J\b\u0010?\u001a\u00020@H\u0002J\u000e\u0010\u001a\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\u001bJ0\u0010A\u001a\u00020@2\u0006\u00109\u001a\u00020\u00042\u0006\u0010B\u001a\u00020\u001b2\u0006\u0010C\u001a\u00020\u001b2\u0006\u0010D\u001a\u00020.2\u0006\u0010-\u001a\u00020.H\u0002J\u0010\u0010E\u001a\u00020\u00002\b\u0010E\u001a\u0004\u0018\u00010\u0004J\r\u0010F\u001a\u00020\u0000H\u0000¢\u0006\u0002\bGJ\u0010\u0010H\u001a\u00020@2\u0006\u0010I\u001a\u00020\u0004H\u0002J\u000e\u0010J\u001a\u00020\u00002\u0006\u0010'\u001a\u00020\u0004J\u000e\u0010K\u001a\u00020\u00002\u0006\u00100\u001a\u00020\u0004J\u000e\u0010L\u001a\u00020\u00002\u0006\u0010M\u001a\u00020\u001bJ \u0010N\u001a\u00020@2\u0006\u00109\u001a\u00020\u00042\u0006\u0010O\u001a\u00020\u001b2\u0006\u0010C\u001a\u00020\u001bH\u0002J\u000e\u0010 \u001a\u00020\u00002\u0006\u0010 \u001a\u00020\u0004J\u0016\u0010P\u001a\u00020\u00002\u0006\u0010M\u001a\u00020\u001b2\u0006\u0010$\u001a\u00020\u0004J\u0018\u0010Q\u001a\u00020\u00002\u0006\u0010'\u001a\u00020\u00042\b\u0010(\u001a\u0004\u0018\u00010\u0004J\u0016\u0010R\u001a\u00020\u00002\u0006\u0010M\u001a\u00020\u001b2\u0006\u0010*\u001a\u00020\u0004J\u0018\u0010S\u001a\u00020\u00002\u0006\u00100\u001a\u00020\u00042\b\u00101\u001a\u0004\u0018\u00010\u0004J\b\u0010T\u001a\u00020\u0004H\u0016J\u000e\u0010U\u001a\u00020\u00002\u0006\u0010U\u001a\u00020\u0004R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001a\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\rX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR$\u0010\u0010\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u000f\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0014\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0006\"\u0004\b\u0016\u0010\bR\u001c\u0010\u0017\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0006\"\u0004\b\u0019\u0010\bR\u001a\u0010\u001a\u001a\u00020\u001bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u001c\u0010 \u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u0006\"\u0004\b\"\u0010\b¨\u0006W"}, d2 = {"Lokhttp3/HttpUrl$Builder;", "", "()V", "encodedFragment", "", "getEncodedFragment$okhttp", "()Ljava/lang/String;", "setEncodedFragment$okhttp", "(Ljava/lang/String;)V", "encodedPassword", "getEncodedPassword$okhttp", "setEncodedPassword$okhttp", "encodedPathSegments", "", "getEncodedPathSegments$okhttp", "()Ljava/util/List;", "encodedQueryNamesAndValues", "getEncodedQueryNamesAndValues$okhttp", "setEncodedQueryNamesAndValues$okhttp", "(Ljava/util/List;)V", "encodedUsername", "getEncodedUsername$okhttp", "setEncodedUsername$okhttp", "host", "getHost$okhttp", "setHost$okhttp", "port", "", "getPort$okhttp", "()I", "setPort$okhttp", "(I)V", "scheme", "getScheme$okhttp", "setScheme$okhttp", "addEncodedPathSegment", "encodedPathSegment", "addEncodedPathSegments", "addEncodedQueryParameter", "encodedName", "encodedValue", "addPathSegment", "pathSegment", "addPathSegments", "pathSegments", "alreadyEncoded", "", "addQueryParameter", "name", "value", "build", "Lokhttp3/HttpUrl;", "effectivePort", "encodedPath", "encodedQuery", "fragment", "isDot", "input", "isDotDot", "parse", "base", "parse$okhttp", "password", "pop", "", "push", "pos", "limit", "addTrailingSlash", "query", "reencodeForUri", "reencodeForUri$okhttp", "removeAllCanonicalQueryParameters", "canonicalName", "removeAllEncodedQueryParameters", "removeAllQueryParameters", "removePathSegment", "index", "resolvePath", "startPos", "setEncodedPathSegment", "setEncodedQueryParameter", "setPathSegment", "setQueryParameter", "toString", "username", "Companion", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: 01BE */
    public static final class Builder {
        public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
        public static final String INVALID_HOST = "Invalid URL host";
        private String encodedFragment;
        private String encodedPassword = HttpUrl.FRAGMENT_ENCODE_SET;
        private final List<String> encodedPathSegments;
        private List<String> encodedQueryNamesAndValues;
        private String encodedUsername = HttpUrl.FRAGMENT_ENCODE_SET;
        private String host;
        private int port = -1;
        private String scheme;

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J \u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u0006H\u0002J \u0010\n\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u0006H\u0002J \u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u0006H\u0002J\u001c\u0010\f\u001a\u00020\u0006*\u00020\u00042\u0006\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u0006H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lokhttp3/HttpUrl$Builder$Companion;", "", "()V", "INVALID_HOST", "", "parsePort", "", "input", "pos", "limit", "portColonOffset", "schemeDelimiterOffset", "slashCount", "okhttp"}, k = 1, mv = {1, 4, 0})
        /* compiled from: 01BD */
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
                this();
            }

            /* access modifiers changed from: private */
            public final int parsePort(String input, int pos, int limit) {
                try {
                    String canonicalize$okhttp$default = Companion.canonicalize$okhttp$default(HttpUrl.Companion, input, pos, limit, HttpUrl.FRAGMENT_ENCODE_SET, false, false, false, false, (Charset) null, 248, (Object) null);
                    Log1F380D.a((Object) canonicalize$okhttp$default);
                    int parseInt = Integer.parseInt(canonicalize$okhttp$default);
                    if (1 <= parseInt && 65535 >= parseInt) {
                        return parseInt;
                    }
                    return -1;
                } catch (NumberFormatException e) {
                    return -1;
                }
            }

            /* access modifiers changed from: private */
            /* JADX WARNING: Can't fix incorrect switch cases order */
            /* JADX WARNING: Code restructure failed: missing block: B:5:0x000d, code lost:
                if (r0 >= r6) goto L_0x001a;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public final int portColonOffset(java.lang.String r4, int r5, int r6) {
                /*
                    r3 = this;
                    r0 = r5
                L_0x0001:
                    if (r0 >= r6) goto L_0x001e
                    char r1 = r4.charAt(r0)
                    switch(r1) {
                        case 58: goto L_0x0019;
                        case 91: goto L_0x000b;
                        default: goto L_0x000a;
                    }
                L_0x000a:
                    goto L_0x001a
                L_0x000b:
                    int r0 = r0 + 1
                    if (r0 >= r6) goto L_0x001a
                    char r1 = r4.charAt(r0)
                    r2 = 93
                    if (r1 != r2) goto L_0x0018
                    goto L_0x001a
                L_0x0018:
                    goto L_0x000b
                L_0x0019:
                    return r0
                L_0x001a:
                    int r0 = r0 + 1
                    goto L_0x0001
                L_0x001e:
                    return r6
                */
                throw new UnsupportedOperationException("Method not decompiled: okhttp3.HttpUrl.Builder.Companion.portColonOffset(java.lang.String, int, int):int");
            }

            /* access modifiers changed from: private */
            public final int schemeDelimiterOffset(String input, int pos, int limit) {
                if (limit - pos < 2) {
                    return -1;
                }
                char charAt = input.charAt(pos);
                if ((Intrinsics.compare((int) charAt, 97) < 0 || Intrinsics.compare((int) charAt, 122) > 0) && (Intrinsics.compare((int) charAt, 65) < 0 || Intrinsics.compare((int) charAt, 90) > 0)) {
                    return -1;
                }
                int i = pos + 1;
                while (i < limit) {
                    char charAt2 = input.charAt(i);
                    if (('a' <= charAt2 && 'z' >= charAt2) || (('A' <= charAt2 && 'Z' >= charAt2) || (('0' <= charAt2 && '9' >= charAt2) || charAt2 == '+' || charAt2 == '-' || charAt2 == '.'))) {
                        i++;
                    } else if (charAt2 == ':') {
                        return i;
                    } else {
                        return -1;
                    }
                }
                return -1;
            }

            /* access modifiers changed from: private */
            public final int slashCount(String $this$slashCount, int pos, int limit) {
                int i = 0;
                for (int i2 = pos; i2 < limit; i2++) {
                    char charAt = $this$slashCount.charAt(i2);
                    if (charAt != '\\' && charAt != '/') {
                        break;
                    }
                    i++;
                }
                return i;
            }
        }

        public Builder() {
            List<String> arrayList = new ArrayList<>();
            this.encodedPathSegments = arrayList;
            arrayList.add(HttpUrl.FRAGMENT_ENCODE_SET);
        }

        private final Builder addPathSegments(String pathSegments, boolean alreadyEncoded) {
            Builder builder = this;
            int i = 0;
            do {
                int delimiterOffset = Util.delimiterOffset(pathSegments, "/\\", i, pathSegments.length());
                builder.push(pathSegments, i, delimiterOffset, delimiterOffset < pathSegments.length(), alreadyEncoded);
                i = delimiterOffset + 1;
            } while (i <= pathSegments.length());
            return this;
        }

        private final int effectivePort() {
            int i = this.port;
            if (i != -1) {
                return i;
            }
            Companion companion = HttpUrl.Companion;
            String str = this.scheme;
            Intrinsics.checkNotNull(str);
            return companion.defaultPort(str);
        }

        private final boolean isDot(String input) {
            return Intrinsics.areEqual((Object) input, (Object) ".") || StringsKt.equals(input, "%2e", true);
        }

        private final boolean isDotDot(String input) {
            return Intrinsics.areEqual((Object) input, (Object) "..") || StringsKt.equals(input, "%2e.", true) || StringsKt.equals(input, ".%2e", true) || StringsKt.equals(input, "%2e%2e", true);
        }

        private final void pop() {
            List<String> list = this.encodedPathSegments;
            if (!(list.remove(list.size() - 1).length() == 0) || !(!this.encodedPathSegments.isEmpty())) {
                this.encodedPathSegments.add(HttpUrl.FRAGMENT_ENCODE_SET);
                return;
            }
            List<String> list2 = this.encodedPathSegments;
            list2.set(list2.size() - 1, HttpUrl.FRAGMENT_ENCODE_SET);
        }

        private final void push(String input, int pos, int limit, boolean addTrailingSlash, boolean alreadyEncoded) {
            String canonicalize$okhttp$default = Companion.canonicalize$okhttp$default(HttpUrl.Companion, input, pos, limit, HttpUrl.PATH_SEGMENT_ENCODE_SET, alreadyEncoded, false, false, false, (Charset) null, 240, (Object) null);
            Log1F380D.a((Object) canonicalize$okhttp$default);
            if (!isDot(canonicalize$okhttp$default)) {
                if (isDotDot(canonicalize$okhttp$default)) {
                    pop();
                    return;
                }
                List<String> list = this.encodedPathSegments;
                if (list.get(list.size() - 1).length() == 0) {
                    List<String> list2 = this.encodedPathSegments;
                    list2.set(list2.size() - 1, canonicalize$okhttp$default);
                } else {
                    this.encodedPathSegments.add(canonicalize$okhttp$default);
                }
                if (addTrailingSlash) {
                    this.encodedPathSegments.add(HttpUrl.FRAGMENT_ENCODE_SET);
                }
            }
        }

        private final void removeAllCanonicalQueryParameters(String canonicalName) {
            List<String> list = this.encodedQueryNamesAndValues;
            Intrinsics.checkNotNull(list);
            IntProgression step = RangesKt.step(RangesKt.downTo(list.size() - 2, 0), 2);
            int first = step.getFirst();
            int last = step.getLast();
            int step2 = step.getStep();
            if (step2 >= 0) {
                if (first > last) {
                    return;
                }
            } else if (first < last) {
                return;
            }
            while (true) {
                List<String> list2 = this.encodedQueryNamesAndValues;
                Intrinsics.checkNotNull(list2);
                if (Intrinsics.areEqual((Object) canonicalName, (Object) list2.get(first))) {
                    List<String> list3 = this.encodedQueryNamesAndValues;
                    Intrinsics.checkNotNull(list3);
                    list3.remove(first + 1);
                    List<String> list4 = this.encodedQueryNamesAndValues;
                    Intrinsics.checkNotNull(list4);
                    list4.remove(first);
                    List<String> list5 = this.encodedQueryNamesAndValues;
                    Intrinsics.checkNotNull(list5);
                    if (list5.isEmpty()) {
                        List list6 = null;
                        this.encodedQueryNamesAndValues = null;
                        return;
                    }
                }
                if (first != last) {
                    first += step2;
                } else {
                    return;
                }
            }
        }

        private final void resolvePath(String input, int startPos, int limit) {
            int i = startPos;
            if (i != limit) {
                char charAt = input.charAt(i);
                if (charAt == '/' || charAt == '\\') {
                    this.encodedPathSegments.clear();
                    this.encodedPathSegments.add(HttpUrl.FRAGMENT_ENCODE_SET);
                    i++;
                } else {
                    List<String> list = this.encodedPathSegments;
                    list.set(list.size() - 1, HttpUrl.FRAGMENT_ENCODE_SET);
                }
                int i2 = i;
                while (i2 < limit) {
                    int delimiterOffset = Util.delimiterOffset(input, "/\\", i2, limit);
                    boolean z = delimiterOffset < limit;
                    push(input, i2, delimiterOffset, z, true);
                    i2 = delimiterOffset;
                    if (z) {
                        i2++;
                    }
                }
            }
        }

        public final Builder addEncodedPathSegment(String encodedPathSegment) {
            Intrinsics.checkNotNullParameter(encodedPathSegment, "encodedPathSegment");
            push(encodedPathSegment, 0, encodedPathSegment.length(), false, true);
            return this;
        }

        public final Builder addEncodedPathSegments(String encodedPathSegments2) {
            Intrinsics.checkNotNullParameter(encodedPathSegments2, "encodedPathSegments");
            return addPathSegments(encodedPathSegments2, true);
        }

        public final Builder addPathSegment(String pathSegment) {
            Intrinsics.checkNotNullParameter(pathSegment, "pathSegment");
            push(pathSegment, 0, pathSegment.length(), false, false);
            return this;
        }

        public final Builder addPathSegments(String pathSegments) {
            Intrinsics.checkNotNullParameter(pathSegments, "pathSegments");
            return addPathSegments(pathSegments, false);
        }

        public final Builder encodedPath(String encodedPath) {
            Intrinsics.checkNotNullParameter(encodedPath, "encodedPath");
            Builder builder = this;
            if (StringsKt.startsWith$default(encodedPath, "/", false, 2, (Object) null)) {
                builder.resolvePath(encodedPath, 0, encodedPath.length());
                return this;
            }
            throw new IllegalArgumentException(("unexpected encodedPath: " + encodedPath).toString());
        }

        public final String getEncodedFragment$okhttp() {
            return this.encodedFragment;
        }

        public final String getEncodedPassword$okhttp() {
            return this.encodedPassword;
        }

        public final List<String> getEncodedPathSegments$okhttp() {
            return this.encodedPathSegments;
        }

        public final List<String> getEncodedQueryNamesAndValues$okhttp() {
            return this.encodedQueryNamesAndValues;
        }

        public final String getEncodedUsername$okhttp() {
            return this.encodedUsername;
        }

        public final String getHost$okhttp() {
            return this.host;
        }

        public final int getPort$okhttp() {
            return this.port;
        }

        public final String getScheme$okhttp() {
            return this.scheme;
        }

        public final Builder port(int port2) {
            Builder builder = this;
            boolean z = true;
            if (1 > port2 || 65535 < port2) {
                z = false;
            }
            if (z) {
                builder.port = port2;
                return this;
            }
            throw new IllegalArgumentException(("unexpected port: " + port2).toString());
        }

        public final Builder removePathSegment(int index) {
            Builder builder = this;
            builder.encodedPathSegments.remove(index);
            if (builder.encodedPathSegments.isEmpty()) {
                builder.encodedPathSegments.add(HttpUrl.FRAGMENT_ENCODE_SET);
            }
            return this;
        }

        public final Builder scheme(String scheme2) {
            Intrinsics.checkNotNullParameter(scheme2, "scheme");
            Builder builder = this;
            if (StringsKt.equals(scheme2, "http", true)) {
                builder.scheme = "http";
            } else if (StringsKt.equals(scheme2, "https", true)) {
                builder.scheme = "https";
            } else {
                throw new IllegalArgumentException("unexpected scheme: " + scheme2);
            }
            return this;
        }

        public final void setEncodedFragment$okhttp(String str) {
            this.encodedFragment = str;
        }

        public final void setEncodedPassword$okhttp(String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.encodedPassword = str;
        }

        public final void setEncodedQueryNamesAndValues$okhttp(List<String> list) {
            this.encodedQueryNamesAndValues = list;
        }

        public final Builder setEncodedQueryParameter(String encodedName, String encodedValue) {
            Intrinsics.checkNotNullParameter(encodedName, "encodedName");
            Builder builder = this;
            builder.removeAllEncodedQueryParameters(encodedName);
            builder.addEncodedQueryParameter(encodedName, encodedValue);
            return this;
        }

        public final void setEncodedUsername$okhttp(String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.encodedUsername = str;
        }

        public final void setHost$okhttp(String str) {
            this.host = str;
        }

        public final void setPort$okhttp(int i) {
            this.port = i;
        }

        public final Builder setQueryParameter(String name, String value) {
            Intrinsics.checkNotNullParameter(name, "name");
            Builder builder = this;
            builder.removeAllQueryParameters(name);
            builder.addQueryParameter(name, value);
            return this;
        }

        public final void setScheme$okhttp(String str) {
            this.scheme = str;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:14:0x003a, code lost:
            if ((r8.encodedPassword.length() > 0) != false) goto L_0x003c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x009f, code lost:
            if (r3 != r4.defaultPort(r5)) goto L_0x00a1;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.String toString() {
            /*
                r8 = this;
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                r1 = r0
                r2 = 0
                java.lang.String r3 = r8.scheme
                if (r3 == 0) goto L_0x0014
                r1.append(r3)
                java.lang.String r3 = "://"
                r1.append(r3)
                goto L_0x0019
            L_0x0014:
                java.lang.String r3 = "//"
                r1.append(r3)
            L_0x0019:
                java.lang.String r3 = r8.encodedUsername
                java.lang.CharSequence r3 = (java.lang.CharSequence) r3
                int r3 = r3.length()
                r4 = 1
                r5 = 0
                if (r3 <= 0) goto L_0x0028
                r3 = r4
                goto L_0x0029
            L_0x0028:
                r3 = r5
            L_0x0029:
                r6 = 58
                if (r3 != 0) goto L_0x003c
                java.lang.String r3 = r8.encodedPassword
                java.lang.CharSequence r3 = (java.lang.CharSequence) r3
                int r3 = r3.length()
                if (r3 <= 0) goto L_0x0039
                r3 = r4
                goto L_0x003a
            L_0x0039:
                r3 = r5
            L_0x003a:
                if (r3 == 0) goto L_0x005c
            L_0x003c:
                java.lang.String r3 = r8.encodedUsername
                r1.append(r3)
                java.lang.String r3 = r8.encodedPassword
                java.lang.CharSequence r3 = (java.lang.CharSequence) r3
                int r3 = r3.length()
                if (r3 <= 0) goto L_0x004c
                goto L_0x004d
            L_0x004c:
                r4 = r5
            L_0x004d:
                if (r4 == 0) goto L_0x0057
                r1.append(r6)
                java.lang.String r3 = r8.encodedPassword
                r1.append(r3)
            L_0x0057:
                r3 = 64
                r1.append(r3)
            L_0x005c:
                java.lang.String r3 = r8.host
                if (r3 == 0) goto L_0x0082
                kotlin.jvm.internal.Intrinsics.checkNotNull(r3)
                java.lang.CharSequence r3 = (java.lang.CharSequence) r3
                r4 = 2
                r7 = 0
                boolean r3 = kotlin.text.StringsKt.contains$default((java.lang.CharSequence) r3, (char) r6, (boolean) r5, (int) r4, (java.lang.Object) r7)
                if (r3 == 0) goto L_0x007d
                r3 = 91
                r1.append(r3)
                java.lang.String r3 = r8.host
                r1.append(r3)
                r3 = 93
                r1.append(r3)
                goto L_0x0082
            L_0x007d:
                java.lang.String r3 = r8.host
                r1.append(r3)
            L_0x0082:
                int r3 = r8.port
                r4 = -1
                if (r3 != r4) goto L_0x008c
                java.lang.String r3 = r8.scheme
                if (r3 == 0) goto L_0x00a7
            L_0x008c:
                int r3 = r8.effectivePort()
                java.lang.String r4 = r8.scheme
                if (r4 == 0) goto L_0x00a1
                okhttp3.HttpUrl$Companion r4 = okhttp3.HttpUrl.Companion
                java.lang.String r5 = r8.scheme
                kotlin.jvm.internal.Intrinsics.checkNotNull(r5)
                int r4 = r4.defaultPort(r5)
                if (r3 == r4) goto L_0x00a7
            L_0x00a1:
                r1.append(r6)
                r1.append(r3)
            L_0x00a7:
                okhttp3.HttpUrl$Companion r3 = okhttp3.HttpUrl.Companion
                java.util.List<java.lang.String> r4 = r8.encodedPathSegments
                r3.toPathString$okhttp(r4, r1)
                java.util.List<java.lang.String> r3 = r8.encodedQueryNamesAndValues
                if (r3 == 0) goto L_0x00c1
                r3 = 63
                r1.append(r3)
                okhttp3.HttpUrl$Companion r3 = okhttp3.HttpUrl.Companion
                java.util.List<java.lang.String> r4 = r8.encodedQueryNamesAndValues
                kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
                r3.toQueryString$okhttp(r4, r1)
            L_0x00c1:
                java.lang.String r3 = r8.encodedFragment
                if (r3 == 0) goto L_0x00cf
                r3 = 35
                r1.append(r3)
                java.lang.String r3 = r8.encodedFragment
                r1.append(r3)
            L_0x00cf:
                java.lang.String r0 = r0.toString()
                java.lang.String r1 = "StringBuilder().apply(builderAction).toString()"
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r1)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.HttpUrl.Builder.toString():java.lang.String");
        }

        public final Builder addEncodedQueryParameter(String encodedName, String encodedValue) {
            String str;
            Intrinsics.checkNotNullParameter(encodedName, "encodedName");
            Builder builder = this;
            if (builder.encodedQueryNamesAndValues == null) {
                builder.encodedQueryNamesAndValues = new ArrayList();
            }
            List<String> list = builder.encodedQueryNamesAndValues;
            Intrinsics.checkNotNull(list);
            String canonicalize$okhttp$default = Companion.canonicalize$okhttp$default(HttpUrl.Companion, encodedName, 0, 0, HttpUrl.QUERY_COMPONENT_REENCODE_SET, true, false, true, false, (Charset) null, 211, (Object) null);
            Log1F380D.a((Object) canonicalize$okhttp$default);
            list.add(canonicalize$okhttp$default);
            List<String> list2 = builder.encodedQueryNamesAndValues;
            Intrinsics.checkNotNull(list2);
            if (encodedValue != null) {
                str = Companion.canonicalize$okhttp$default(HttpUrl.Companion, encodedValue, 0, 0, HttpUrl.QUERY_COMPONENT_REENCODE_SET, true, false, true, false, (Charset) null, 211, (Object) null);
                Log1F380D.a((Object) str);
            } else {
                str = null;
            }
            list2.add(str);
            return this;
        }

        public final Builder addQueryParameter(String name, String value) {
            String str;
            Intrinsics.checkNotNullParameter(name, "name");
            Builder builder = this;
            if (builder.encodedQueryNamesAndValues == null) {
                builder.encodedQueryNamesAndValues = new ArrayList();
            }
            List<String> list = builder.encodedQueryNamesAndValues;
            Intrinsics.checkNotNull(list);
            String canonicalize$okhttp$default = Companion.canonicalize$okhttp$default(HttpUrl.Companion, name, 0, 0, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, false, true, false, (Charset) null, 219, (Object) null);
            Log1F380D.a((Object) canonicalize$okhttp$default);
            list.add(canonicalize$okhttp$default);
            List<String> list2 = builder.encodedQueryNamesAndValues;
            Intrinsics.checkNotNull(list2);
            if (value != null) {
                str = Companion.canonicalize$okhttp$default(HttpUrl.Companion, value, 0, 0, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, false, true, false, (Charset) null, 219, (Object) null);
                Log1F380D.a((Object) str);
            } else {
                str = null;
            }
            list2.add(str);
            return this;
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v8, resolved type: java.util.Collection} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v10, resolved type: java.util.List} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final okhttp3.HttpUrl build() {
            /*
                r24 = this;
                r0 = r24
                java.lang.String r2 = r0.scheme
                if (r2 == 0) goto L_0x00ef
                okhttp3.HttpUrl$Companion r3 = okhttp3.HttpUrl.Companion
                java.lang.String r4 = r0.encodedUsername
                r5 = 0
                r6 = 0
                r7 = 0
                r8 = 7
                r9 = 0
                java.lang.String r3 = okhttp3.HttpUrl.Companion.percentDecode$okhttp$default(r3, r4, r5, r6, r7, r8, r9)
                mt.Log1F380D.a((java.lang.Object) r3)
                okhttp3.HttpUrl$Companion r4 = okhttp3.HttpUrl.Companion
                java.lang.String r5 = r0.encodedPassword
                r8 = 0
                r9 = 7
                r10 = 0
                java.lang.String r4 = okhttp3.HttpUrl.Companion.percentDecode$okhttp$default(r4, r5, r6, r7, r8, r9, r10)
                mt.Log1F380D.a((java.lang.Object) r4)
                java.lang.String r5 = r0.host
                if (r5 == 0) goto L_0x00e5
                int r6 = r24.effectivePort()
                java.util.List<java.lang.String> r1 = r0.encodedPathSegments
                java.lang.Iterable r1 = (java.lang.Iterable) r1
                r7 = 0
                java.util.ArrayList r8 = new java.util.ArrayList
                r9 = 10
                int r10 = kotlin.collections.CollectionsKt.collectionSizeOrDefault(r1, r9)
                r8.<init>(r10)
                java.util.Collection r8 = (java.util.Collection) r8
                r10 = r1
                r11 = 0
                java.util.Iterator r12 = r10.iterator()
            L_0x0045:
                boolean r13 = r12.hasNext()
                if (r13 == 0) goto L_0x006e
                java.lang.Object r13 = r12.next()
                r21 = r13
                java.lang.String r21 = (java.lang.String) r21
                r22 = 0
                okhttp3.HttpUrl$Companion r14 = okhttp3.HttpUrl.Companion
                r16 = 0
                r17 = 0
                r18 = 0
                r19 = 7
                r20 = 0
                r15 = r21
                java.lang.String r14 = okhttp3.HttpUrl.Companion.percentDecode$okhttp$default(r14, r15, r16, r17, r18, r19, r20)
                mt.Log1F380D.a((java.lang.Object) r14)
                r8.add(r14)
                goto L_0x0045
            L_0x006e:
                r7 = r8
                java.util.List r7 = (java.util.List) r7
                java.util.List<java.lang.String> r1 = r0.encodedQueryNamesAndValues
                r8 = 0
                if (r1 == 0) goto L_0x00c0
                java.lang.Iterable r1 = (java.lang.Iterable) r1
                r10 = 0
                java.util.ArrayList r11 = new java.util.ArrayList
                int r9 = kotlin.collections.CollectionsKt.collectionSizeOrDefault(r1, r9)
                r11.<init>(r9)
                r9 = r11
                java.util.Collection r9 = (java.util.Collection) r9
                r11 = r1
                r12 = 0
                java.util.Iterator r13 = r11.iterator()
            L_0x008d:
                boolean r14 = r13.hasNext()
                if (r14 == 0) goto L_0x00ba
                java.lang.Object r14 = r13.next()
                r22 = r14
                java.lang.String r22 = (java.lang.String) r22
                r23 = 0
                if (r22 == 0) goto L_0x00b5
                okhttp3.HttpUrl$Companion r15 = okhttp3.HttpUrl.Companion
                r17 = 0
                r18 = 0
                r19 = 1
                r20 = 3
                r21 = 0
                r16 = r22
                java.lang.String r15 = okhttp3.HttpUrl.Companion.percentDecode$okhttp$default(r15, r16, r17, r18, r19, r20, r21)
                mt.Log1F380D.a((java.lang.Object) r15)
                goto L_0x00b6
            L_0x00b5:
                r15 = r8
            L_0x00b6:
                r9.add(r15)
                goto L_0x008d
            L_0x00ba:
                r1 = r9
                java.util.List r1 = (java.util.List) r1
                r9 = r1
                goto L_0x00c1
            L_0x00c0:
                r9 = r8
            L_0x00c1:
                java.lang.String r11 = r0.encodedFragment
                if (r11 == 0) goto L_0x00d6
                okhttp3.HttpUrl$Companion r10 = okhttp3.HttpUrl.Companion
                r12 = 0
                r13 = 0
                r14 = 0
                r15 = 7
                r16 = 0
                java.lang.String r1 = okhttp3.HttpUrl.Companion.percentDecode$okhttp$default(r10, r11, r12, r13, r14, r15, r16)
                mt.Log1F380D.a((java.lang.Object) r1)
                r10 = r1
                goto L_0x00d7
            L_0x00d6:
                r10 = r8
            L_0x00d7:
                java.lang.String r11 = r24.toString()
                okhttp3.HttpUrl r12 = new okhttp3.HttpUrl
                r1 = r12
                r8 = r9
                r9 = r10
                r10 = r11
                r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10)
                return r12
            L_0x00e5:
                java.lang.IllegalStateException r1 = new java.lang.IllegalStateException
                java.lang.String r2 = "host == null"
                r1.<init>(r2)
                java.lang.Throwable r1 = (java.lang.Throwable) r1
                throw r1
            L_0x00ef:
                java.lang.IllegalStateException r1 = new java.lang.IllegalStateException
                java.lang.String r2 = "scheme == null"
                r1.<init>(r2)
                java.lang.Throwable r1 = (java.lang.Throwable) r1
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.HttpUrl.Builder.build():okhttp3.HttpUrl");
        }

        public final Builder encodedFragment(String encodedFragment2) {
            String str;
            Builder builder = this;
            if (encodedFragment2 != null) {
                str = Companion.canonicalize$okhttp$default(HttpUrl.Companion, encodedFragment2, 0, 0, HttpUrl.FRAGMENT_ENCODE_SET, true, false, false, true, (Charset) null, 179, (Object) null);
                Log1F380D.a((Object) str);
            } else {
                str = null;
            }
            builder.encodedFragment = str;
            return this;
        }

        public final Builder encodedPassword(String encodedPassword2) {
            Intrinsics.checkNotNullParameter(encodedPassword2, "encodedPassword");
            String canonicalize$okhttp$default = Companion.canonicalize$okhttp$default(HttpUrl.Companion, encodedPassword2, 0, 0, " \"':;<=>@[]^`{}|/\\?#", true, false, false, false, (Charset) null, 243, (Object) null);
            Log1F380D.a((Object) canonicalize$okhttp$default);
            this.encodedPassword = canonicalize$okhttp$default;
            return this;
        }

        public final Builder encodedQuery(String encodedQuery) {
            List<String> list;
            Builder builder = this;
            if (encodedQuery != null) {
                String canonicalize$okhttp$default = Companion.canonicalize$okhttp$default(HttpUrl.Companion, encodedQuery, 0, 0, HttpUrl.QUERY_ENCODE_SET, true, false, true, false, (Charset) null, 211, (Object) null);
                Log1F380D.a((Object) canonicalize$okhttp$default);
                if (canonicalize$okhttp$default != null) {
                    list = HttpUrl.Companion.toQueryNamesAndValues$okhttp(canonicalize$okhttp$default);
                    builder.encodedQueryNamesAndValues = list;
                    return this;
                }
            }
            list = null;
            builder.encodedQueryNamesAndValues = list;
            return this;
        }

        public final Builder encodedUsername(String encodedUsername2) {
            Intrinsics.checkNotNullParameter(encodedUsername2, "encodedUsername");
            String canonicalize$okhttp$default = Companion.canonicalize$okhttp$default(HttpUrl.Companion, encodedUsername2, 0, 0, " \"':;<=>@[]^`{}|/\\?#", true, false, false, false, (Charset) null, 243, (Object) null);
            Log1F380D.a((Object) canonicalize$okhttp$default);
            this.encodedUsername = canonicalize$okhttp$default;
            return this;
        }

        public final Builder fragment(String fragment) {
            String str;
            Builder builder = this;
            if (fragment != null) {
                str = Companion.canonicalize$okhttp$default(HttpUrl.Companion, fragment, 0, 0, HttpUrl.FRAGMENT_ENCODE_SET, false, false, false, true, (Charset) null, 187, (Object) null);
                Log1F380D.a((Object) str);
            } else {
                str = null;
            }
            builder.encodedFragment = str;
            return this;
        }

        public final Builder host(String host2) {
            Intrinsics.checkNotNullParameter(host2, "host");
            Builder builder = this;
            String percentDecode$okhttp$default = Companion.percentDecode$okhttp$default(HttpUrl.Companion, host2, 0, 0, false, 7, (Object) null);
            Log1F380D.a((Object) percentDecode$okhttp$default);
            String canonicalHost = HostnamesKt.toCanonicalHost(percentDecode$okhttp$default);
            if (canonicalHost != null) {
                builder.host = canonicalHost;
                return this;
            }
            throw new IllegalArgumentException("unexpected host: " + host2);
        }

        public final Builder parse$okhttp(HttpUrl base, String input) {
            int i;
            int i2;
            String str;
            int i3;
            boolean z;
            int i4;
            int i5;
            String str2;
            char c;
            char c2;
            String str3;
            int i6;
            int i7;
            String str4 = input;
            Intrinsics.checkNotNullParameter(str4, "input");
            int indexOfFirstNonAsciiWhitespace$default = Util.indexOfFirstNonAsciiWhitespace$default(str4, 0, 0, 3, (Object) null);
            int indexOfLastNonAsciiWhitespace$default = Util.indexOfLastNonAsciiWhitespace$default(str4, indexOfFirstNonAsciiWhitespace$default, 0, 2, (Object) null);
            Companion companion = Companion;
            int access$schemeDelimiterOffset = companion.schemeDelimiterOffset(str4, indexOfFirstNonAsciiWhitespace$default, indexOfLastNonAsciiWhitespace$default);
            String str5 = "(this as java.lang.Strin…ing(startIndex, endIndex)";
            char c3 = 65535;
            boolean z2 = true;
            if (access$schemeDelimiterOffset != -1) {
                if (StringsKt.startsWith(str4, "https:", indexOfFirstNonAsciiWhitespace$default, true)) {
                    this.scheme = "https";
                    indexOfFirstNonAsciiWhitespace$default += "https:".length();
                } else if (StringsKt.startsWith(str4, "http:", indexOfFirstNonAsciiWhitespace$default, true)) {
                    this.scheme = "http";
                    indexOfFirstNonAsciiWhitespace$default += "http:".length();
                } else {
                    StringBuilder append = new StringBuilder().append("Expected URL scheme 'http' or 'https' but was '");
                    String substring = str4.substring(0, access$schemeDelimiterOffset);
                    Intrinsics.checkNotNullExpressionValue(substring, str5);
                    throw new IllegalArgumentException(append.append(substring).append("'").toString());
                }
            } else if (base != null) {
                this.scheme = base.scheme();
            } else {
                throw new IllegalArgumentException("Expected URL scheme 'http' or 'https' but no colon was found");
            }
            int access$slashCount = companion.slashCount(str4, indexOfFirstNonAsciiWhitespace$default, indexOfLastNonAsciiWhitespace$default);
            char c4 = '#';
            if (access$slashCount >= 2 || base == null || (!Intrinsics.areEqual((Object) base.scheme(), (Object) this.scheme))) {
                int i8 = indexOfFirstNonAsciiWhitespace$default + access$slashCount;
                boolean z3 = false;
                boolean z4 = false;
                while (true) {
                    int delimiterOffset = Util.delimiterOffset(str4, "@/\\?#", i8, indexOfLastNonAsciiWhitespace$default);
                    switch (delimiterOffset != indexOfLastNonAsciiWhitespace$default ? str4.charAt(delimiterOffset) : c3) {
                        case 65535:
                        case '#':
                        case '/':
                        case HtmlCompat.FROM_HTML_MODE_COMPACT:
                        case '\\':
                            int i9 = access$slashCount;
                            boolean z5 = z2;
                            String str6 = str5;
                            int i10 = access$schemeDelimiterOffset;
                            i = indexOfLastNonAsciiWhitespace$default;
                            int i11 = delimiterOffset;
                            Companion companion2 = Companion;
                            int i12 = i8;
                            int access$portColonOffset = companion2.portColonOffset(str4, i12, i11);
                            if (access$portColonOffset + 1 < i11) {
                                String percentDecode$okhttp$default = Companion.percentDecode$okhttp$default(HttpUrl.Companion, input, i12, access$portColonOffset, false, 4, (Object) null);
                                Log1F380D.a((Object) percentDecode$okhttp$default);
                                this.host = HostnamesKt.toCanonicalHost(percentDecode$okhttp$default);
                                int access$parsePort = companion2.parsePort(str4, access$portColonOffset + 1, i11);
                                this.port = access$parsePort;
                                if (access$parsePort != -1 ? z5 : false) {
                                    str = str6;
                                } else {
                                    StringBuilder append2 = new StringBuilder().append("Invalid URL port: \"");
                                    String substring2 = str4.substring(access$portColonOffset + 1, i11);
                                    Intrinsics.checkNotNullExpressionValue(substring2, str6);
                                    throw new IllegalArgumentException(append2.append(substring2).append(Typography.quote).toString().toString());
                                }
                            } else {
                                str = str6;
                                String percentDecode$okhttp$default2 = Companion.percentDecode$okhttp$default(HttpUrl.Companion, input, i12, access$portColonOffset, false, 4, (Object) null);
                                Log1F380D.a((Object) percentDecode$okhttp$default2);
                                this.host = HostnamesKt.toCanonicalHost(percentDecode$okhttp$default2);
                                Companion companion3 = HttpUrl.Companion;
                                String str7 = this.scheme;
                                Intrinsics.checkNotNull(str7);
                                this.port = companion3.defaultPort(str7);
                            }
                            if (this.host != null ? z5 : false) {
                                indexOfFirstNonAsciiWhitespace$default = i11;
                                break;
                            } else {
                                StringBuilder append3 = new StringBuilder().append("Invalid URL host: \"");
                                String substring3 = str4.substring(i12, access$portColonOffset);
                                Intrinsics.checkNotNullExpressionValue(substring3, str);
                                throw new IllegalArgumentException(append3.append(substring3).append(Typography.quote).toString().toString());
                            }
                        case '@':
                            if (!z4) {
                                int delimiterOffset2 = Util.delimiterOffset(str4, ':', i8, delimiterOffset);
                                String str8 = "%40";
                                int i13 = delimiterOffset;
                                int i14 = i8;
                                i4 = access$slashCount;
                                z = z2;
                                str3 = str5;
                                i3 = access$schemeDelimiterOffset;
                                String canonicalize$okhttp$default = Companion.canonicalize$okhttp$default(HttpUrl.Companion, input, i8, delimiterOffset2, " \"':;<=>@[]^`{}|/\\?#", true, false, false, false, (Charset) null, 240, (Object) null);
                                Log1F380D.a((Object) canonicalize$okhttp$default);
                                this.encodedUsername = z3 ? this.encodedUsername + str8 + canonicalize$okhttp$default : canonicalize$okhttp$default;
                                int i15 = delimiterOffset2;
                                int i16 = i13;
                                if (i15 != i16) {
                                    z4 = true;
                                    int i17 = i15;
                                    String str9 = canonicalize$okhttp$default;
                                    String canonicalize$okhttp$default2 = Companion.canonicalize$okhttp$default(HttpUrl.Companion, input, i15 + 1, i16, " \"':;<=>@[]^`{}|/\\?#", true, false, false, false, (Charset) null, 240, (Object) null);
                                    Log1F380D.a((Object) canonicalize$okhttp$default2);
                                    this.encodedPassword = canonicalize$okhttp$default2;
                                } else {
                                    int i18 = i15;
                                    String str10 = canonicalize$okhttp$default;
                                }
                                z3 = true;
                                i6 = indexOfLastNonAsciiWhitespace$default;
                                i7 = i16;
                            } else {
                                i4 = access$slashCount;
                                z = z2;
                                str3 = str5;
                                i3 = access$schemeDelimiterOffset;
                                int i19 = delimiterOffset;
                                StringBuilder append4 = new StringBuilder().append(this.encodedPassword).append("%40");
                                i6 = indexOfLastNonAsciiWhitespace$default;
                                i7 = i19;
                                String canonicalize$okhttp$default3 = Companion.canonicalize$okhttp$default(HttpUrl.Companion, input, i8, i19, " \"':;<=>@[]^`{}|/\\?#", true, false, false, false, (Charset) null, 240, (Object) null);
                                Log1F380D.a((Object) canonicalize$okhttp$default3);
                                this.encodedPassword = append4.append(canonicalize$okhttp$default3).toString();
                            }
                            i8 = i7 + 1;
                            i5 = i6;
                            str2 = str3;
                            c2 = 65535;
                            c = '#';
                            continue;
                        default:
                            c = c4;
                            i4 = access$slashCount;
                            z = z2;
                            c2 = c3;
                            str2 = str5;
                            i3 = access$schemeDelimiterOffset;
                            i5 = indexOfLastNonAsciiWhitespace$default;
                            int i20 = delimiterOffset;
                            int i21 = i8;
                            continue;
                    }
                    c3 = c2;
                    c4 = c;
                    str5 = str2;
                    indexOfLastNonAsciiWhitespace$default = i5;
                    access$slashCount = i4;
                    z2 = z;
                    access$schemeDelimiterOffset = i3;
                }
            } else {
                this.encodedUsername = base.encodedUsername();
                this.encodedPassword = base.encodedPassword();
                this.host = base.host();
                this.port = base.port();
                this.encodedPathSegments.clear();
                this.encodedPathSegments.addAll(base.encodedPathSegments());
                if (indexOfFirstNonAsciiWhitespace$default == indexOfLastNonAsciiWhitespace$default || str4.charAt(indexOfFirstNonAsciiWhitespace$default) == '#') {
                    encodedQuery(base.encodedQuery());
                }
                int i22 = access$slashCount;
                int i23 = access$schemeDelimiterOffset;
                i = indexOfLastNonAsciiWhitespace$default;
            }
            int i24 = i;
            int delimiterOffset3 = Util.delimiterOffset(str4, "?#", indexOfFirstNonAsciiWhitespace$default, i24);
            resolvePath(str4, indexOfFirstNonAsciiWhitespace$default, delimiterOffset3);
            int i25 = delimiterOffset3;
            if (i25 >= i24 || str4.charAt(i25) != '?') {
                i2 = i25;
            } else {
                int delimiterOffset4 = Util.delimiterOffset(str4, '#', i25, i24);
                Companion companion4 = HttpUrl.Companion;
                int i26 = i25;
                String canonicalize$okhttp$default4 = Companion.canonicalize$okhttp$default(HttpUrl.Companion, input, i25 + 1, delimiterOffset4, HttpUrl.QUERY_ENCODE_SET, true, false, true, false, (Charset) null, 208, (Object) null);
                Log1F380D.a((Object) canonicalize$okhttp$default4);
                this.encodedQueryNamesAndValues = companion4.toQueryNamesAndValues$okhttp(canonicalize$okhttp$default4);
                i2 = delimiterOffset4;
            }
            if (i2 >= i24 || str4.charAt(i2) != '#') {
                int i27 = i2;
            } else {
                int i28 = i2;
                String canonicalize$okhttp$default5 = Companion.canonicalize$okhttp$default(HttpUrl.Companion, input, i2 + 1, i24, HttpUrl.FRAGMENT_ENCODE_SET, true, false, false, true, (Charset) null, 176, (Object) null);
                Log1F380D.a((Object) canonicalize$okhttp$default5);
                this.encodedFragment = canonicalize$okhttp$default5;
            }
            return this;
        }

        public final Builder password(String password) {
            Intrinsics.checkNotNullParameter(password, "password");
            String canonicalize$okhttp$default = Companion.canonicalize$okhttp$default(HttpUrl.Companion, password, 0, 0, " \"':;<=>@[]^`{}|/\\?#", false, false, false, false, (Charset) null, 251, (Object) null);
            Log1F380D.a((Object) canonicalize$okhttp$default);
            this.encodedPassword = canonicalize$okhttp$default;
            return this;
        }

        public final Builder query(String query) {
            List<String> list;
            Builder builder = this;
            if (query != null) {
                String canonicalize$okhttp$default = Companion.canonicalize$okhttp$default(HttpUrl.Companion, query, 0, 0, HttpUrl.QUERY_ENCODE_SET, false, false, true, false, (Charset) null, 219, (Object) null);
                Log1F380D.a((Object) canonicalize$okhttp$default);
                if (canonicalize$okhttp$default != null) {
                    list = HttpUrl.Companion.toQueryNamesAndValues$okhttp(canonicalize$okhttp$default);
                    builder.encodedQueryNamesAndValues = list;
                    return this;
                }
            }
            list = null;
            builder.encodedQueryNamesAndValues = list;
            return this;
        }

        public final Builder reencodeForUri$okhttp() {
            String str;
            String str2;
            Builder builder = this;
            String str3 = builder.host;
            String str4 = null;
            if (str3 != null) {
                str = new Regex("[\"<>^`{|}]").replace((CharSequence) str3, HttpUrl.FRAGMENT_ENCODE_SET);
            } else {
                str = null;
            }
            builder.host = str;
            int size = builder.encodedPathSegments.size();
            for (int i = 0; i < size; i++) {
                List<String> list = builder.encodedPathSegments;
                String canonicalize$okhttp$default = Companion.canonicalize$okhttp$default(HttpUrl.Companion, builder.encodedPathSegments.get(i), 0, 0, HttpUrl.PATH_SEGMENT_ENCODE_SET_URI, true, true, false, false, (Charset) null, 227, (Object) null);
                Log1F380D.a((Object) canonicalize$okhttp$default);
                list.set(i, canonicalize$okhttp$default);
            }
            List<String> list2 = builder.encodedQueryNamesAndValues;
            if (list2 != null) {
                int size2 = list2.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    String str5 = list2.get(i2);
                    if (str5 != null) {
                        str2 = Companion.canonicalize$okhttp$default(HttpUrl.Companion, str5, 0, 0, HttpUrl.QUERY_COMPONENT_ENCODE_SET_URI, true, true, true, false, (Charset) null, 195, (Object) null);
                        Log1F380D.a((Object) str2);
                    } else {
                        str2 = null;
                    }
                    list2.set(i2, str2);
                }
            }
            String str6 = builder.encodedFragment;
            if (str6 != null) {
                str4 = Companion.canonicalize$okhttp$default(HttpUrl.Companion, str6, 0, 0, HttpUrl.FRAGMENT_ENCODE_SET_URI, true, true, false, true, (Charset) null, 163, (Object) null);
                Log1F380D.a((Object) str4);
            }
            builder.encodedFragment = str4;
            return this;
        }

        public final Builder removeAllEncodedQueryParameters(String encodedName) {
            Intrinsics.checkNotNullParameter(encodedName, "encodedName");
            Builder builder = this;
            if (builder.encodedQueryNamesAndValues == null) {
                return builder;
            }
            String canonicalize$okhttp$default = Companion.canonicalize$okhttp$default(HttpUrl.Companion, encodedName, 0, 0, HttpUrl.QUERY_COMPONENT_REENCODE_SET, true, false, true, false, (Charset) null, 211, (Object) null);
            Log1F380D.a((Object) canonicalize$okhttp$default);
            builder.removeAllCanonicalQueryParameters(canonicalize$okhttp$default);
            return this;
        }

        public final Builder removeAllQueryParameters(String name) {
            Intrinsics.checkNotNullParameter(name, "name");
            Builder builder = this;
            if (builder.encodedQueryNamesAndValues == null) {
                return builder;
            }
            String canonicalize$okhttp$default = Companion.canonicalize$okhttp$default(HttpUrl.Companion, name, 0, 0, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, false, true, false, (Charset) null, 219, (Object) null);
            Log1F380D.a((Object) canonicalize$okhttp$default);
            builder.removeAllCanonicalQueryParameters(canonicalize$okhttp$default);
            return this;
        }

        public final Builder setEncodedPathSegment(int index, String encodedPathSegment) {
            String str = encodedPathSegment;
            Intrinsics.checkNotNullParameter(str, "encodedPathSegment");
            Builder builder = this;
            String canonicalize$okhttp$default = Companion.canonicalize$okhttp$default(HttpUrl.Companion, encodedPathSegment, 0, 0, HttpUrl.PATH_SEGMENT_ENCODE_SET, true, false, false, false, (Charset) null, 243, (Object) null);
            Log1F380D.a((Object) canonicalize$okhttp$default);
            builder.encodedPathSegments.set(index, canonicalize$okhttp$default);
            if (!builder.isDot(canonicalize$okhttp$default) && !builder.isDotDot(canonicalize$okhttp$default)) {
                return this;
            }
            throw new IllegalArgumentException(("unexpected path segment: " + str).toString());
        }

        public final Builder setPathSegment(int index, String pathSegment) {
            String str = pathSegment;
            Intrinsics.checkNotNullParameter(str, "pathSegment");
            Builder builder = this;
            String canonicalize$okhttp$default = Companion.canonicalize$okhttp$default(HttpUrl.Companion, pathSegment, 0, 0, HttpUrl.PATH_SEGMENT_ENCODE_SET, false, false, false, false, (Charset) null, 251, (Object) null);
            Log1F380D.a((Object) canonicalize$okhttp$default);
            if (!builder.isDot(canonicalize$okhttp$default) && !builder.isDotDot(canonicalize$okhttp$default)) {
                builder.encodedPathSegments.set(index, canonicalize$okhttp$default);
                return this;
            }
            int i = index;
            throw new IllegalArgumentException(("unexpected path segment: " + str).toString());
        }

        public final Builder username(String username) {
            Intrinsics.checkNotNullParameter(username, "username");
            String canonicalize$okhttp$default = Companion.canonicalize$okhttp$default(HttpUrl.Companion, username, 0, 0, " \"':;<=>@[]^`{}|/\\?#", false, false, false, false, (Charset) null, 251, (Object) null);
            Log1F380D.a((Object) canonicalize$okhttp$default);
            this.encodedUsername = canonicalize$okhttp$default;
            return this;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0019\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0004H\u0007J\u0017\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0007¢\u0006\u0002\b\u0018J\u0017\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0019\u001a\u00020\u001aH\u0007¢\u0006\u0002\b\u0018J\u0015\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\u0004H\u0007¢\u0006\u0002\b\u0018J\u0017\u0010\u001b\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0019\u001a\u00020\u0004H\u0007¢\u0006\u0002\b\u001cJa\u0010\u001d\u001a\u00020\u0004*\u00020\u00042\b\b\u0002\u0010\u001e\u001a\u00020\u00122\b\b\u0002\u0010\u001f\u001a\u00020\u00122\u0006\u0010 \u001a\u00020\u00042\b\b\u0002\u0010!\u001a\u00020\"2\b\b\u0002\u0010#\u001a\u00020\"2\b\b\u0002\u0010$\u001a\u00020\"2\b\b\u0002\u0010%\u001a\u00020\"2\n\b\u0002\u0010&\u001a\u0004\u0018\u00010'H\u0000¢\u0006\u0002\b(J\u001c\u0010)\u001a\u00020\"*\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u0012H\u0002J/\u0010*\u001a\u00020\u0004*\u00020\u00042\b\b\u0002\u0010\u001e\u001a\u00020\u00122\b\b\u0002\u0010\u001f\u001a\u00020\u00122\b\b\u0002\u0010$\u001a\u00020\"H\u0000¢\u0006\u0002\b+J\u0011\u0010,\u001a\u00020\u0015*\u00020\u0004H\u0007¢\u0006\u0002\b\u0014J\u0013\u0010-\u001a\u0004\u0018\u00010\u0015*\u00020\u0017H\u0007¢\u0006\u0002\b\u0014J\u0013\u0010-\u001a\u0004\u0018\u00010\u0015*\u00020\u001aH\u0007¢\u0006\u0002\b\u0014J\u0013\u0010-\u001a\u0004\u0018\u00010\u0015*\u00020\u0004H\u0007¢\u0006\u0002\b\u001bJ#\u0010.\u001a\u00020/*\b\u0012\u0004\u0012\u00020\u0004002\n\u00101\u001a\u000602j\u0002`3H\u0000¢\u0006\u0002\b4J\u0019\u00105\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000406*\u00020\u0004H\u0000¢\u0006\u0002\b7J%\u00108\u001a\u00020/*\n\u0012\u0006\u0012\u0004\u0018\u00010\u0004002\n\u00101\u001a\u000602j\u0002`3H\u0000¢\u0006\u0002\b9JV\u0010:\u001a\u00020/*\u00020;2\u0006\u0010<\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u00122\u0006\u0010 \u001a\u00020\u00042\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\"2\u0006\u0010$\u001a\u00020\"2\u0006\u0010%\u001a\u00020\"2\b\u0010&\u001a\u0004\u0018\u00010'H\u0002J,\u0010=\u001a\u00020/*\u00020;2\u0006\u0010>\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u00122\u0006\u0010$\u001a\u00020\"H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006?"}, d2 = {"Lokhttp3/HttpUrl$Companion;", "", "()V", "FORM_ENCODE_SET", "", "FRAGMENT_ENCODE_SET", "FRAGMENT_ENCODE_SET_URI", "HEX_DIGITS", "", "PASSWORD_ENCODE_SET", "PATH_SEGMENT_ENCODE_SET", "PATH_SEGMENT_ENCODE_SET_URI", "QUERY_COMPONENT_ENCODE_SET", "QUERY_COMPONENT_ENCODE_SET_URI", "QUERY_COMPONENT_REENCODE_SET", "QUERY_ENCODE_SET", "USERNAME_ENCODE_SET", "defaultPort", "", "scheme", "get", "Lokhttp3/HttpUrl;", "uri", "Ljava/net/URI;", "-deprecated_get", "url", "Ljava/net/URL;", "parse", "-deprecated_parse", "canonicalize", "pos", "limit", "encodeSet", "alreadyEncoded", "", "strict", "plusIsSpace", "unicodeAllowed", "charset", "Ljava/nio/charset/Charset;", "canonicalize$okhttp", "isPercentEncoded", "percentDecode", "percentDecode$okhttp", "toHttpUrl", "toHttpUrlOrNull", "toPathString", "", "", "out", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "toPathString$okhttp", "toQueryNamesAndValues", "", "toQueryNamesAndValues$okhttp", "toQueryString", "toQueryString$okhttp", "writeCanonicalized", "Lokio/Buffer;", "input", "writePercentDecoded", "encoded", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: HttpUrl.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        public static /* synthetic */ String canonicalize$okhttp$default(Companion companion, String str, int i, int i2, String str2, boolean z, boolean z2, boolean z3, boolean z4, Charset charset, int i3, Object obj) {
            Charset charset2;
            int i4 = i3;
            int i5 = (i4 & 1) != 0 ? 0 : i;
            int length = (i4 & 2) != 0 ? str.length() : i2;
            boolean z5 = (i4 & 8) != 0 ? false : z;
            boolean z6 = (i4 & 16) != 0 ? false : z2;
            boolean z7 = (i4 & 32) != 0 ? false : z3;
            boolean z8 = (i4 & 64) != 0 ? false : z4;
            if ((i4 & 128) != 0) {
                Charset charset3 = null;
                charset2 = null;
            } else {
                charset2 = charset;
            }
            return companion.canonicalize$okhttp(str, i5, length, str2, z5, z6, z7, z8, charset2);
        }

        private final boolean isPercentEncoded(String $this$isPercentEncoded, int pos, int limit) {
            return pos + 2 < limit && $this$isPercentEncoded.charAt(pos) == '%' && Util.parseHexDigit($this$isPercentEncoded.charAt(pos + 1)) != -1 && Util.parseHexDigit($this$isPercentEncoded.charAt(pos + 2)) != -1;
        }

        public static /* synthetic */ String percentDecode$okhttp$default(Companion companion, String str, int i, int i2, boolean z, int i3, Object obj) {
            if ((i3 & 1) != 0) {
                i = 0;
            }
            if ((i3 & 2) != 0) {
                i2 = str.length();
            }
            if ((i3 & 4) != 0) {
                z = false;
            }
            return companion.percentDecode$okhttp(str, i, i2, z);
        }

        private final void writeCanonicalized(Buffer $this$writeCanonicalized, String input, int pos, int limit, String encodeSet, boolean alreadyEncoded, boolean strict, boolean plusIsSpace, boolean unicodeAllowed, Charset charset) {
            Buffer buffer = $this$writeCanonicalized;
            String str = input;
            int i = limit;
            Charset charset2 = charset;
            Buffer buffer2 = null;
            int i2 = pos;
            while (i2 < i) {
                if (str != null) {
                    int codePointAt = input.codePointAt(i2);
                    if (!alreadyEncoded || !(codePointAt == 9 || codePointAt == 10 || codePointAt == 12 || codePointAt == 13)) {
                        if (codePointAt == 43 && plusIsSpace) {
                            $this$writeCanonicalized.writeUtf8(alreadyEncoded ? "+" : "%2B");
                        } else if (codePointAt < 32 || codePointAt == 127 || ((codePointAt >= 128 && !unicodeAllowed) || StringsKt.contains$default((CharSequence) encodeSet, (char) codePointAt, false, 2, (Object) null) || (codePointAt == 37 && (!alreadyEncoded || (strict && !isPercentEncoded(input, i2, i)))))) {
                            if (buffer2 == null) {
                                buffer2 = new Buffer();
                            }
                            if (charset2 == null || Intrinsics.areEqual((Object) charset2, (Object) StandardCharsets.UTF_8)) {
                                buffer2.writeUtf8CodePoint(codePointAt);
                            } else {
                                buffer2.writeString(input, i2, Character.charCount(codePointAt) + i2, charset2);
                            }
                            while (!buffer2.exhausted()) {
                                byte readByte = buffer2.readByte() & UByte.MAX_VALUE;
                                $this$writeCanonicalized.writeByte(37);
                                $this$writeCanonicalized.writeByte((int) HttpUrl.HEX_DIGITS[(readByte >> 4) & 15]);
                                $this$writeCanonicalized.writeByte((int) HttpUrl.HEX_DIGITS[readByte & 15]);
                            }
                        } else {
                            $this$writeCanonicalized.writeUtf8CodePoint(codePointAt);
                        }
                    }
                    i2 += Character.charCount(codePointAt);
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
            }
        }

        private final void writePercentDecoded(Buffer $this$writePercentDecoded, String encoded, int pos, int limit, boolean plusIsSpace) {
            int i = pos;
            while (i < limit) {
                if (encoded != null) {
                    int codePointAt = encoded.codePointAt(i);
                    if (codePointAt == 37 && i + 2 < limit) {
                        int parseHexDigit = Util.parseHexDigit(encoded.charAt(i + 1));
                        int parseHexDigit2 = Util.parseHexDigit(encoded.charAt(i + 2));
                        if (!(parseHexDigit == -1 || parseHexDigit2 == -1)) {
                            $this$writePercentDecoded.writeByte((parseHexDigit << 4) + parseHexDigit2);
                            i = i + 2 + Character.charCount(codePointAt);
                        }
                    } else if (codePointAt == 43 && plusIsSpace) {
                        $this$writePercentDecoded.writeByte(32);
                        i++;
                    }
                    $this$writePercentDecoded.writeUtf8CodePoint(codePointAt);
                    i += Character.charCount(codePointAt);
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
            }
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "moved to extension function", replaceWith = @ReplaceWith(expression = "url.toHttpUrl()", imports = {"okhttp3.HttpUrl.Companion.toHttpUrl"}))
        /* renamed from: -deprecated_get  reason: not valid java name */
        public final HttpUrl m1660deprecated_get(String url) {
            Intrinsics.checkNotNullParameter(url, "url");
            return get(url);
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "moved to extension function", replaceWith = @ReplaceWith(expression = "uri.toHttpUrlOrNull()", imports = {"okhttp3.HttpUrl.Companion.toHttpUrlOrNull"}))
        /* renamed from: -deprecated_get  reason: not valid java name */
        public final HttpUrl m1661deprecated_get(URI uri) {
            Intrinsics.checkNotNullParameter(uri, "uri");
            return get(uri);
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "moved to extension function", replaceWith = @ReplaceWith(expression = "url.toHttpUrlOrNull()", imports = {"okhttp3.HttpUrl.Companion.toHttpUrlOrNull"}))
        /* renamed from: -deprecated_get  reason: not valid java name */
        public final HttpUrl m1662deprecated_get(URL url) {
            Intrinsics.checkNotNullParameter(url, "url");
            return get(url);
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "moved to extension function", replaceWith = @ReplaceWith(expression = "url.toHttpUrlOrNull()", imports = {"okhttp3.HttpUrl.Companion.toHttpUrlOrNull"}))
        /* renamed from: -deprecated_parse  reason: not valid java name */
        public final HttpUrl m1663deprecated_parse(String url) {
            Intrinsics.checkNotNullParameter(url, "url");
            return parse(url);
        }

        public final String canonicalize$okhttp(String $this$canonicalize, int pos, int limit, String encodeSet, boolean alreadyEncoded, boolean strict, boolean plusIsSpace, boolean unicodeAllowed, Charset charset) {
            String str = $this$canonicalize;
            int i = limit;
            String str2 = encodeSet;
            Intrinsics.checkNotNullParameter(str, "$this$canonicalize");
            Intrinsics.checkNotNullParameter(str2, "encodeSet");
            int i2 = pos;
            while (i2 < i) {
                int codePointAt = str.codePointAt(i2);
                if (codePointAt < 32 || codePointAt == 127 || ((codePointAt >= 128 && !unicodeAllowed) || StringsKt.contains$default((CharSequence) str2, (char) codePointAt, false, 2, (Object) null) || ((codePointAt == 37 && (!alreadyEncoded || (strict && !isPercentEncoded(str, i2, i)))) || (codePointAt == 43 && plusIsSpace)))) {
                    Buffer buffer = new Buffer();
                    buffer.writeUtf8(str, pos, i2);
                    Buffer buffer2 = buffer;
                    writeCanonicalized(buffer, $this$canonicalize, i2, limit, encodeSet, alreadyEncoded, strict, plusIsSpace, unicodeAllowed, charset);
                    return buffer2.readUtf8();
                }
                i2 += Character.charCount(codePointAt);
                int i3 = codePointAt;
            }
            String substring = $this$canonicalize.substring(pos, limit);
            Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            return substring;
        }

        @JvmStatic
        public final int defaultPort(String scheme) {
            Intrinsics.checkNotNullParameter(scheme, "scheme");
            switch (scheme.hashCode()) {
                case 3213448:
                    if (scheme.equals("http")) {
                        return 80;
                    }
                    break;
                case 99617003:
                    if (scheme.equals("https")) {
                        return 443;
                    }
                    break;
            }
            return -1;
        }

        @JvmStatic
        public final HttpUrl get(String $this$toHttpUrl) {
            Intrinsics.checkNotNullParameter($this$toHttpUrl, "$this$toHttpUrl");
            return new Builder().parse$okhttp((HttpUrl) null, $this$toHttpUrl).build();
        }

        @JvmStatic
        public final HttpUrl get(URI $this$toHttpUrlOrNull) {
            Intrinsics.checkNotNullParameter($this$toHttpUrlOrNull, "$this$toHttpUrlOrNull");
            String uri = $this$toHttpUrlOrNull.toString();
            Intrinsics.checkNotNullExpressionValue(uri, "toString()");
            return parse(uri);
        }

        @JvmStatic
        public final HttpUrl get(URL $this$toHttpUrlOrNull) {
            Intrinsics.checkNotNullParameter($this$toHttpUrlOrNull, "$this$toHttpUrlOrNull");
            String url = $this$toHttpUrlOrNull.toString();
            Intrinsics.checkNotNullExpressionValue(url, "toString()");
            return parse(url);
        }

        @JvmStatic
        public final HttpUrl parse(String $this$toHttpUrlOrNull) {
            Intrinsics.checkNotNullParameter($this$toHttpUrlOrNull, "$this$toHttpUrlOrNull");
            try {
                return get($this$toHttpUrlOrNull);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        public final String percentDecode$okhttp(String $this$percentDecode, int pos, int limit, boolean plusIsSpace) {
            Intrinsics.checkNotNullParameter($this$percentDecode, "$this$percentDecode");
            for (int i = pos; i < limit; i++) {
                char charAt = $this$percentDecode.charAt(i);
                if (charAt == '%' || (charAt == '+' && plusIsSpace)) {
                    Buffer buffer = new Buffer();
                    buffer.writeUtf8($this$percentDecode, pos, i);
                    writePercentDecoded(buffer, $this$percentDecode, i, limit, plusIsSpace);
                    return buffer.readUtf8();
                }
            }
            String substring = $this$percentDecode.substring(pos, limit);
            Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            return substring;
        }

        public final void toPathString$okhttp(List<String> $this$toPathString, StringBuilder out) {
            Intrinsics.checkNotNullParameter($this$toPathString, "$this$toPathString");
            Intrinsics.checkNotNullParameter(out, "out");
            int size = $this$toPathString.size();
            for (int i = 0; i < size; i++) {
                out.append('/');
                out.append($this$toPathString.get(i));
            }
        }

        public final List<String> toQueryNamesAndValues$okhttp(String $this$toQueryNamesAndValues) {
            Intrinsics.checkNotNullParameter($this$toQueryNamesAndValues, "$this$toQueryNamesAndValues");
            List<String> arrayList = new ArrayList<>();
            int i = 0;
            while (i <= $this$toQueryNamesAndValues.length()) {
                int indexOf$default = StringsKt.indexOf$default((CharSequence) $this$toQueryNamesAndValues, (char) Typography.amp, i, false, 4, (Object) null);
                if (indexOf$default == -1) {
                    indexOf$default = $this$toQueryNamesAndValues.length();
                }
                int i2 = indexOf$default;
                int indexOf$default2 = StringsKt.indexOf$default((CharSequence) $this$toQueryNamesAndValues, '=', i, false, 4, (Object) null);
                if (indexOf$default2 == -1 || indexOf$default2 > i2) {
                    String substring = $this$toQueryNamesAndValues.substring(i, i2);
                    Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    arrayList.add(substring);
                    arrayList.add((Object) null);
                } else {
                    String substring2 = $this$toQueryNamesAndValues.substring(i, indexOf$default2);
                    Intrinsics.checkNotNullExpressionValue(substring2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    arrayList.add(substring2);
                    String substring3 = $this$toQueryNamesAndValues.substring(indexOf$default2 + 1, i2);
                    Intrinsics.checkNotNullExpressionValue(substring3, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    arrayList.add(substring3);
                }
                i = i2 + 1;
            }
            return arrayList;
        }

        public final void toQueryString$okhttp(List<String> $this$toQueryString, StringBuilder out) {
            Intrinsics.checkNotNullParameter($this$toQueryString, "$this$toQueryString");
            Intrinsics.checkNotNullParameter(out, "out");
            IntProgression step = RangesKt.step((IntProgression) RangesKt.until(0, $this$toQueryString.size()), 2);
            int first = step.getFirst();
            int last = step.getLast();
            int step2 = step.getStep();
            if (step2 >= 0) {
                if (first > last) {
                    return;
                }
            } else if (first < last) {
                return;
            }
            while (true) {
                String str = $this$toQueryString.get(first);
                String str2 = $this$toQueryString.get(first + 1);
                if (first > 0) {
                    out.append(Typography.amp);
                }
                out.append(str);
                if (str2 != null) {
                    out.append('=');
                    out.append(str2);
                }
                if (first != last) {
                    first += step2;
                } else {
                    return;
                }
            }
        }
    }

    public HttpUrl(String scheme2, String username2, String password2, String host2, int port2, List<String> pathSegments2, List<String> queryNamesAndValues2, String fragment2, String url2) {
        Intrinsics.checkNotNullParameter(scheme2, "scheme");
        Intrinsics.checkNotNullParameter(username2, "username");
        Intrinsics.checkNotNullParameter(password2, "password");
        Intrinsics.checkNotNullParameter(host2, "host");
        Intrinsics.checkNotNullParameter(pathSegments2, "pathSegments");
        Intrinsics.checkNotNullParameter(url2, "url");
        this.scheme = scheme2;
        this.username = username2;
        this.password = password2;
        this.host = host2;
        this.port = port2;
        this.pathSegments = pathSegments2;
        this.queryNamesAndValues = queryNamesAndValues2;
        this.fragment = fragment2;
        this.url = url2;
        this.isHttps = Intrinsics.areEqual((Object) scheme2, (Object) "https");
    }

    @JvmStatic
    public static final int defaultPort(String str) {
        return Companion.defaultPort(str);
    }

    @JvmStatic
    public static final HttpUrl get(String str) {
        return Companion.get(str);
    }

    @JvmStatic
    public static final HttpUrl get(URI uri) {
        return Companion.get(uri);
    }

    @JvmStatic
    public static final HttpUrl get(URL url2) {
        return Companion.get(url2);
    }

    @JvmStatic
    public static final HttpUrl parse(String str) {
        return Companion.parse(str);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "encodedFragment", imports = {}))
    /* renamed from: -deprecated_encodedFragment  reason: not valid java name */
    public final String m1641deprecated_encodedFragment() {
        return encodedFragment();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "encodedPassword", imports = {}))
    /* renamed from: -deprecated_encodedPassword  reason: not valid java name */
    public final String m1642deprecated_encodedPassword() {
        return encodedPassword();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "encodedPath", imports = {}))
    /* renamed from: -deprecated_encodedPath  reason: not valid java name */
    public final String m1643deprecated_encodedPath() {
        return encodedPath();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "encodedPathSegments", imports = {}))
    /* renamed from: -deprecated_encodedPathSegments  reason: not valid java name */
    public final List<String> m1644deprecated_encodedPathSegments() {
        return encodedPathSegments();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "encodedQuery", imports = {}))
    /* renamed from: -deprecated_encodedQuery  reason: not valid java name */
    public final String m1645deprecated_encodedQuery() {
        return encodedQuery();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "encodedUsername", imports = {}))
    /* renamed from: -deprecated_encodedUsername  reason: not valid java name */
    public final String m1646deprecated_encodedUsername() {
        return encodedUsername();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "fragment", imports = {}))
    /* renamed from: -deprecated_fragment  reason: not valid java name */
    public final String m1647deprecated_fragment() {
        return this.fragment;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "host", imports = {}))
    /* renamed from: -deprecated_host  reason: not valid java name */
    public final String m1648deprecated_host() {
        return this.host;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "password", imports = {}))
    /* renamed from: -deprecated_password  reason: not valid java name */
    public final String m1649deprecated_password() {
        return this.password;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "pathSegments", imports = {}))
    /* renamed from: -deprecated_pathSegments  reason: not valid java name */
    public final List<String> m1650deprecated_pathSegments() {
        return this.pathSegments;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "pathSize", imports = {}))
    /* renamed from: -deprecated_pathSize  reason: not valid java name */
    public final int m1651deprecated_pathSize() {
        return pathSize();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "port", imports = {}))
    /* renamed from: -deprecated_port  reason: not valid java name */
    public final int m1652deprecated_port() {
        return this.port;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "query", imports = {}))
    /* renamed from: -deprecated_query  reason: not valid java name */
    public final String m1653deprecated_query() {
        return query();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "queryParameterNames", imports = {}))
    /* renamed from: -deprecated_queryParameterNames  reason: not valid java name */
    public final Set<String> m1654deprecated_queryParameterNames() {
        return queryParameterNames();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "querySize", imports = {}))
    /* renamed from: -deprecated_querySize  reason: not valid java name */
    public final int m1655deprecated_querySize() {
        return querySize();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "scheme", imports = {}))
    /* renamed from: -deprecated_scheme  reason: not valid java name */
    public final String m1656deprecated_scheme() {
        return this.scheme;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to toUri()", replaceWith = @ReplaceWith(expression = "toUri()", imports = {}))
    /* renamed from: -deprecated_uri  reason: not valid java name */
    public final URI m1657deprecated_uri() {
        return uri();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to toUrl()", replaceWith = @ReplaceWith(expression = "toUrl()", imports = {}))
    /* renamed from: -deprecated_url  reason: not valid java name */
    public final URL m1658deprecated_url() {
        return url();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "username", imports = {}))
    /* renamed from: -deprecated_username  reason: not valid java name */
    public final String m1659deprecated_username() {
        return this.username;
    }

    public final String encodedFragment() {
        if (this.fragment == null) {
            return null;
        }
        int indexOf$default = StringsKt.indexOf$default((CharSequence) this.url, '#', 0, false, 6, (Object) null) + 1;
        String str = this.url;
        if (str != null) {
            String substring = str.substring(indexOf$default);
            Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.String).substring(startIndex)");
            return substring;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public final String encodedPassword() {
        if (this.password.length() == 0) {
            return FRAGMENT_ENCODE_SET;
        }
        int indexOf$default = StringsKt.indexOf$default((CharSequence) this.url, ':', this.scheme.length() + 3, false, 4, (Object) null) + 1;
        int indexOf$default2 = StringsKt.indexOf$default((CharSequence) this.url, '@', 0, false, 6, (Object) null);
        String str = this.url;
        if (str != null) {
            String substring = str.substring(indexOf$default, indexOf$default2);
            Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            return substring;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public final String encodedPath() {
        int indexOf$default = StringsKt.indexOf$default((CharSequence) this.url, '/', this.scheme.length() + 3, false, 4, (Object) null);
        String str = this.url;
        int delimiterOffset = Util.delimiterOffset(str, "?#", indexOf$default, str.length());
        String str2 = this.url;
        if (str2 != null) {
            String substring = str2.substring(indexOf$default, delimiterOffset);
            Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            return substring;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public final List<String> encodedPathSegments() {
        int indexOf$default = StringsKt.indexOf$default((CharSequence) this.url, '/', this.scheme.length() + 3, false, 4, (Object) null);
        String str = this.url;
        int delimiterOffset = Util.delimiterOffset(str, "?#", indexOf$default, str.length());
        List<String> arrayList = new ArrayList<>();
        int i = indexOf$default;
        while (i < delimiterOffset) {
            int i2 = i + 1;
            int delimiterOffset2 = Util.delimiterOffset(this.url, '/', i2, delimiterOffset);
            String str2 = this.url;
            if (str2 != null) {
                String substring = str2.substring(i2, delimiterOffset2);
                Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                arrayList.add(substring);
                i = delimiterOffset2;
            } else {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
        }
        return arrayList;
    }

    public final String encodedQuery() {
        if (this.queryNamesAndValues == null) {
            return null;
        }
        int indexOf$default = StringsKt.indexOf$default((CharSequence) this.url, '?', 0, false, 6, (Object) null) + 1;
        String str = this.url;
        int delimiterOffset = Util.delimiterOffset(str, '#', indexOf$default, str.length());
        String str2 = this.url;
        if (str2 != null) {
            String substring = str2.substring(indexOf$default, delimiterOffset);
            Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            return substring;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public final String encodedUsername() {
        if (this.username.length() == 0) {
            return FRAGMENT_ENCODE_SET;
        }
        int length = this.scheme.length() + 3;
        String str = this.url;
        int delimiterOffset = Util.delimiterOffset(str, ":@", length, str.length());
        String str2 = this.url;
        if (str2 != null) {
            String substring = str2.substring(length, delimiterOffset);
            Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            return substring;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
    }

    public boolean equals(Object other) {
        return (other instanceof HttpUrl) && Intrinsics.areEqual((Object) ((HttpUrl) other).url, (Object) this.url);
    }

    public final String fragment() {
        return this.fragment;
    }

    public int hashCode() {
        return this.url.hashCode();
    }

    public final String host() {
        return this.host;
    }

    public final boolean isHttps() {
        return this.isHttps;
    }

    public final Builder newBuilder() {
        Builder builder = new Builder();
        builder.setScheme$okhttp(this.scheme);
        builder.setEncodedUsername$okhttp(encodedUsername());
        builder.setEncodedPassword$okhttp(encodedPassword());
        builder.setHost$okhttp(this.host);
        builder.setPort$okhttp(this.port != Companion.defaultPort(this.scheme) ? this.port : -1);
        builder.getEncodedPathSegments$okhttp().clear();
        builder.getEncodedPathSegments$okhttp().addAll(encodedPathSegments());
        builder.encodedQuery(encodedQuery());
        builder.setEncodedFragment$okhttp(encodedFragment());
        return builder;
    }

    public final Builder newBuilder(String link) {
        Intrinsics.checkNotNullParameter(link, "link");
        try {
            return new Builder().parse$okhttp(this, link);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public final String password() {
        return this.password;
    }

    public final List<String> pathSegments() {
        return this.pathSegments;
    }

    public final int pathSize() {
        return this.pathSegments.size();
    }

    public final int port() {
        return this.port;
    }

    public final String query() {
        if (this.queryNamesAndValues == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Companion.toQueryString$okhttp(this.queryNamesAndValues, sb);
        return sb.toString();
    }

    public final String queryParameter(String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        List<String> list = this.queryNamesAndValues;
        if (list == null) {
            return null;
        }
        IntProgression step = RangesKt.step((IntProgression) RangesKt.until(0, list.size()), 2);
        int first = step.getFirst();
        int last = step.getLast();
        int step2 = step.getStep();
        if (step2 < 0 ? first >= last : first <= last) {
            while (!Intrinsics.areEqual((Object) name, (Object) this.queryNamesAndValues.get(first))) {
                if (first != last) {
                    first += step2;
                }
            }
            return this.queryNamesAndValues.get(first + 1);
        }
        return null;
    }

    public final String queryParameterName(int index) {
        List<String> list = this.queryNamesAndValues;
        if (list != null) {
            String str = list.get(index * 2);
            Intrinsics.checkNotNull(str);
            return str;
        }
        throw new IndexOutOfBoundsException();
    }

    public final Set<String> queryParameterNames() {
        if (this.queryNamesAndValues == null) {
            return SetsKt.emptySet();
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        IntProgression step = RangesKt.step((IntProgression) RangesKt.until(0, this.queryNamesAndValues.size()), 2);
        int first = step.getFirst();
        int last = step.getLast();
        int step2 = step.getStep();
        if (step2 < 0 ? first >= last : first <= last) {
            while (true) {
                String str = this.queryNamesAndValues.get(first);
                Intrinsics.checkNotNull(str);
                linkedHashSet.add(str);
                if (first == last) {
                    break;
                }
                first += step2;
            }
        }
        Set<String> unmodifiableSet = Collections.unmodifiableSet(linkedHashSet);
        Intrinsics.checkNotNullExpressionValue(unmodifiableSet, "Collections.unmodifiableSet(result)");
        return unmodifiableSet;
    }

    public final String queryParameterValue(int index) {
        List<String> list = this.queryNamesAndValues;
        if (list != null) {
            return list.get((index * 2) + 1);
        }
        throw new IndexOutOfBoundsException();
    }

    public final List<String> queryParameterValues(String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        if (this.queryNamesAndValues == null) {
            return CollectionsKt.emptyList();
        }
        List arrayList = new ArrayList();
        IntProgression step = RangesKt.step((IntProgression) RangesKt.until(0, this.queryNamesAndValues.size()), 2);
        int first = step.getFirst();
        int last = step.getLast();
        int step2 = step.getStep();
        if (step2 < 0 ? first >= last : first <= last) {
            while (true) {
                if (Intrinsics.areEqual((Object) name, (Object) this.queryNamesAndValues.get(first))) {
                    arrayList.add(this.queryNamesAndValues.get(first + 1));
                }
                if (first == last) {
                    break;
                }
                first += step2;
            }
        }
        List<String> unmodifiableList = Collections.unmodifiableList(arrayList);
        Intrinsics.checkNotNullExpressionValue(unmodifiableList, "Collections.unmodifiableList(result)");
        return unmodifiableList;
    }

    public final int querySize() {
        List<String> list = this.queryNamesAndValues;
        if (list != null) {
            return list.size() / 2;
        }
        return 0;
    }

    public final String redact() {
        Builder newBuilder = newBuilder("/...");
        Intrinsics.checkNotNull(newBuilder);
        return newBuilder.username(FRAGMENT_ENCODE_SET).password(FRAGMENT_ENCODE_SET).build().toString();
    }

    public final HttpUrl resolve(String link) {
        Intrinsics.checkNotNullParameter(link, "link");
        Builder newBuilder = newBuilder(link);
        if (newBuilder != null) {
            return newBuilder.build();
        }
        return null;
    }

    public final String scheme() {
        return this.scheme;
    }

    public String toString() {
        return this.url;
    }

    public final String topPrivateDomain() {
        if (Util.canParseAsIpAddress(this.host)) {
            return null;
        }
        return PublicSuffixDatabase.Companion.get().getEffectiveTldPlusOne(this.host);
    }

    public final URI uri() {
        String builder = newBuilder().reencodeForUri$okhttp().toString();
        try {
            return new URI(builder);
        } catch (URISyntaxException e) {
            try {
                URI create = URI.create(new Regex("[\\u0000-\\u001F\\u007F-\\u009F\\p{javaWhitespace}]").replace((CharSequence) builder, FRAGMENT_ENCODE_SET));
                Intrinsics.checkNotNullExpressionValue(create, "try {\n        val stripp…e) // Unexpected!\n      }");
                return create;
            } catch (Exception e2) {
                throw new RuntimeException(e);
            }
        }
    }

    public final URL url() {
        try {
            return new URL(this.url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public final String username() {
        return this.username;
    }
}

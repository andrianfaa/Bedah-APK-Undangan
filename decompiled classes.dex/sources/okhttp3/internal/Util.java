package okhttp3.internal;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import mt.Log1F380D;
import okhttp3.EventListener;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http2.Header;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Options;
import okio.Source;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000¸\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\n\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\f\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010$\n\u0002\b\b\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a \u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019\u001a\u001e\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u00172\u0006\u0010\u001e\u001a\u00020\u0017\u001a'\u0010\u001f\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u00112\u0012\u0010 \u001a\n\u0012\u0006\b\u0001\u0012\u00020\"0!\"\u00020\"¢\u0006\u0002\u0010#\u001a\u001a\u0010$\u001a\u00020\u001b2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u001b0&H\bø\u0001\u0000\u001a-\u0010'\u001a\b\u0012\u0004\u0012\u0002H)0(\"\u0004\b\u0000\u0010)2\u0012\u0010*\u001a\n\u0012\u0006\b\u0001\u0012\u0002H)0!\"\u0002H)H\u0007¢\u0006\u0002\u0010+\u001a1\u0010,\u001a\u0004\u0018\u0001H)\"\u0004\b\u0000\u0010)2\u0006\u0010-\u001a\u00020\"2\f\u0010.\u001a\b\u0012\u0004\u0012\u0002H)0/2\u0006\u00100\u001a\u00020\u0011¢\u0006\u0002\u00101\u001a\u0016\u00102\u001a\u0002032\u0006\u0010\u0015\u001a\u00020\u00112\u0006\u00104\u001a\u00020\u000f\u001a\"\u00105\u001a\u00020\u001b2\u0006\u0010\u0015\u001a\u00020\u00112\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u001b0&H\bø\u0001\u0000\u001a%\u00106\u001a\u00020\u001b\"\u0004\b\u0000\u00107*\b\u0012\u0004\u0012\u0002H7082\u0006\u00109\u001a\u0002H7H\u0000¢\u0006\u0002\u0010:\u001a\u0015\u0010;\u001a\u00020\u0014*\u00020<2\u0006\u0010=\u001a\u00020\u0014H\u0004\u001a\u0015\u0010;\u001a\u00020\u0017*\u00020\u00142\u0006\u0010=\u001a\u00020\u0017H\u0004\u001a\u0015\u0010;\u001a\u00020\u0014*\u00020>2\u0006\u0010=\u001a\u00020\u0014H\u0004\u001a\n\u0010?\u001a\u00020@*\u00020A\u001a\r\u0010B\u001a\u00020\u001b*\u00020\"H\b\u001a\r\u0010C\u001a\u00020\u001b*\u00020\"H\b\u001a\n\u0010D\u001a\u00020\u000f*\u00020\u0011\u001a\u0012\u0010E\u001a\u00020\u000f*\u00020F2\u0006\u0010G\u001a\u00020F\u001a\n\u0010H\u001a\u00020\u001b*\u00020I\u001a\n\u0010H\u001a\u00020\u001b*\u00020J\u001a\n\u0010H\u001a\u00020\u001b*\u00020K\u001a#\u0010L\u001a\b\u0012\u0004\u0012\u00020\u00110!*\b\u0012\u0004\u0012\u00020\u00110!2\u0006\u0010M\u001a\u00020\u0011¢\u0006\u0002\u0010N\u001a&\u0010O\u001a\u00020\u0014*\u00020\u00112\u0006\u0010P\u001a\u00020Q2\b\b\u0002\u0010R\u001a\u00020\u00142\b\b\u0002\u0010S\u001a\u00020\u0014\u001a&\u0010O\u001a\u00020\u0014*\u00020\u00112\u0006\u0010T\u001a\u00020\u00112\b\b\u0002\u0010R\u001a\u00020\u00142\b\b\u0002\u0010S\u001a\u00020\u0014\u001a\u001a\u0010U\u001a\u00020\u000f*\u00020V2\u0006\u0010W\u001a\u00020\u00142\u0006\u0010X\u001a\u00020\u0019\u001a;\u0010Y\u001a\b\u0012\u0004\u0012\u0002H)0(\"\u0004\b\u0000\u0010)*\b\u0012\u0004\u0012\u0002H)0Z2\u0017\u0010[\u001a\u0013\u0012\u0004\u0012\u0002H)\u0012\u0004\u0012\u00020\u000f0\\¢\u0006\u0002\b]H\bø\u0001\u0000\u001a5\u0010^\u001a\u00020\u000f*\b\u0012\u0004\u0012\u00020\u00110!2\u000e\u0010G\u001a\n\u0012\u0004\u0012\u00020\u0011\u0018\u00010!2\u000e\u0010_\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00110`¢\u0006\u0002\u0010a\u001a\n\u0010b\u001a\u00020\u0017*\u00020c\u001a+\u0010d\u001a\u00020\u0014*\b\u0012\u0004\u0012\u00020\u00110!2\u0006\u0010M\u001a\u00020\u00112\f\u0010_\u001a\b\u0012\u0004\u0012\u00020\u00110`¢\u0006\u0002\u0010e\u001a\n\u0010f\u001a\u00020\u0014*\u00020\u0011\u001a\u001e\u0010g\u001a\u00020\u0014*\u00020\u00112\b\b\u0002\u0010R\u001a\u00020\u00142\b\b\u0002\u0010S\u001a\u00020\u0014\u001a\u001e\u0010h\u001a\u00020\u0014*\u00020\u00112\b\b\u0002\u0010R\u001a\u00020\u00142\b\b\u0002\u0010S\u001a\u00020\u0014\u001a\u0014\u0010i\u001a\u00020\u0014*\u00020\u00112\b\b\u0002\u0010R\u001a\u00020\u0014\u001a9\u0010j\u001a\b\u0012\u0004\u0012\u00020\u00110!*\b\u0012\u0004\u0012\u00020\u00110!2\f\u0010G\u001a\b\u0012\u0004\u0012\u00020\u00110!2\u000e\u0010_\u001a\n\u0012\u0006\b\u0000\u0012\u00020\u00110`¢\u0006\u0002\u0010k\u001a\u0012\u0010l\u001a\u00020\u000f*\u00020m2\u0006\u0010n\u001a\u00020o\u001a\u0012\u0010p\u001a\u00020\u000f*\u00020K2\u0006\u0010q\u001a\u00020r\u001a\r\u0010s\u001a\u00020\u001b*\u00020\"H\b\u001a\r\u0010t\u001a\u00020\u001b*\u00020\"H\b\u001a\n\u0010u\u001a\u00020\u0014*\u00020Q\u001a\n\u0010v\u001a\u00020\u0011*\u00020K\u001a\u0012\u0010w\u001a\u00020x*\u00020r2\u0006\u0010y\u001a\u00020x\u001a\n\u0010z\u001a\u00020\u0014*\u00020r\u001a\u0012\u0010{\u001a\u00020\u0014*\u00020|2\u0006\u0010}\u001a\u00020<\u001a\u001a\u0010{\u001a\u00020\u000f*\u00020V2\u0006\u0010\u0016\u001a\u00020\u00142\u0006\u0010X\u001a\u00020\u0019\u001a\u0010\u0010~\u001a\b\u0012\u0004\u0012\u000200(*\u00020\u0003\u001a\u0011\u0010\u0001\u001a\u00020\u0003*\b\u0012\u0004\u0012\u000200(\u001a\u000b\u0010\u0001\u001a\u00020\u0011*\u00020\u0014\u001a\u000b\u0010\u0001\u001a\u00020\u0011*\u00020\u0017\u001a\u0016\u0010\u0001\u001a\u00020\u0011*\u00020F2\t\b\u0002\u0010\u0001\u001a\u00020\u000f\u001a\u001d\u0010\u0001\u001a\b\u0012\u0004\u0012\u0002H)0(\"\u0004\b\u0000\u0010)*\b\u0012\u0004\u0012\u0002H)0(\u001a7\u0010\u0001\u001a\u0011\u0012\u0005\u0012\u0003H\u0001\u0012\u0005\u0012\u0003H\u00010\u0001\"\u0005\b\u0000\u0010\u0001\"\u0005\b\u0001\u0010\u0001*\u0011\u0012\u0005\u0012\u0003H\u0001\u0012\u0005\u0012\u0003H\u00010\u0001\u001a\u0014\u0010\u0001\u001a\u00020\u0017*\u00020\u00112\u0007\u0010\u0001\u001a\u00020\u0017\u001a\u0016\u0010\u0001\u001a\u00020\u0014*\u0004\u0018\u00010\u00112\u0007\u0010\u0001\u001a\u00020\u0014\u001a\u001f\u0010\u0001\u001a\u00020\u0011*\u00020\u00112\b\b\u0002\u0010R\u001a\u00020\u00142\b\b\u0002\u0010S\u001a\u00020\u0014\u001a\u000e\u0010\u0001\u001a\u00020\u001b*\u00020\"H\b\u001a'\u0010\u0001\u001a\u00030\u0001*\b0\u0001j\u0003`\u00012\u0013\u0010\u0001\u001a\u000e\u0012\n\u0012\b0\u0001j\u0003`\u00010(\u001a\u0015\u0010\u0001\u001a\u00020\u001b*\u00030\u00012\u0007\u0010\u0001\u001a\u00020\u0014\"\u0010\u0010\u0000\u001a\u00020\u00018\u0006X\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\u0004\u001a\u00020\u00058\u0006X\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\u0006\u001a\u00020\u00078\u0006X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\n\u001a\u00020\u000b8\u0006X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\u000e\u001a\u00020\u000f8\u0000X\u0004¢\u0006\u0002\n\u0000\"\u0010\u0010\u0010\u001a\u00020\u00118\u0000X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0012\u001a\u00020\u0011XT¢\u0006\u0002\n\u0000\u0002\u0007\n\u0005\b20\u0001¨\u0006\u0001"}, d2 = {"EMPTY_BYTE_ARRAY", "", "EMPTY_HEADERS", "Lokhttp3/Headers;", "EMPTY_REQUEST", "Lokhttp3/RequestBody;", "EMPTY_RESPONSE", "Lokhttp3/ResponseBody;", "UNICODE_BOMS", "Lokio/Options;", "UTC", "Ljava/util/TimeZone;", "VERIFY_AS_IP_ADDRESS", "Lkotlin/text/Regex;", "assertionsEnabled", "", "okHttpName", "", "userAgent", "checkDuration", "", "name", "duration", "", "unit", "Ljava/util/concurrent/TimeUnit;", "checkOffsetAndCount", "", "arrayLength", "offset", "count", "format", "args", "", "", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "ignoreIoExceptions", "block", "Lkotlin/Function0;", "immutableListOf", "", "T", "elements", "([Ljava/lang/Object;)Ljava/util/List;", "readFieldOrNull", "instance", "fieldType", "Ljava/lang/Class;", "fieldName", "(Ljava/lang/Object;Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Object;", "threadFactory", "Ljava/util/concurrent/ThreadFactory;", "daemon", "threadName", "addIfAbsent", "E", "", "element", "(Ljava/util/List;Ljava/lang/Object;)V", "and", "", "mask", "", "asFactory", "Lokhttp3/EventListener$Factory;", "Lokhttp3/EventListener;", "assertThreadDoesntHoldLock", "assertThreadHoldsLock", "canParseAsIpAddress", "canReuseConnectionFor", "Lokhttp3/HttpUrl;", "other", "closeQuietly", "Ljava/io/Closeable;", "Ljava/net/ServerSocket;", "Ljava/net/Socket;", "concat", "value", "([Ljava/lang/String;Ljava/lang/String;)[Ljava/lang/String;", "delimiterOffset", "delimiter", "", "startIndex", "endIndex", "delimiters", "discard", "Lokio/Source;", "timeout", "timeUnit", "filterList", "", "predicate", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "hasIntersection", "comparator", "Ljava/util/Comparator;", "([Ljava/lang/String;[Ljava/lang/String;Ljava/util/Comparator;)Z", "headersContentLength", "Lokhttp3/Response;", "indexOf", "([Ljava/lang/String;Ljava/lang/String;Ljava/util/Comparator;)I", "indexOfControlOrNonAscii", "indexOfFirstNonAsciiWhitespace", "indexOfLastNonAsciiWhitespace", "indexOfNonWhitespace", "intersect", "([Ljava/lang/String;[Ljava/lang/String;Ljava/util/Comparator;)[Ljava/lang/String;", "isCivilized", "Lokhttp3/internal/io/FileSystem;", "file", "Ljava/io/File;", "isHealthy", "source", "Lokio/BufferedSource;", "notify", "notifyAll", "parseHexDigit", "peerName", "readBomAsCharset", "Ljava/nio/charset/Charset;", "default", "readMedium", "skipAll", "Lokio/Buffer;", "b", "toHeaderList", "Lokhttp3/internal/http2/Header;", "toHeaders", "toHexString", "toHostHeader", "includeDefaultPort", "toImmutableList", "toImmutableMap", "", "K", "V", "toLongOrDefault", "defaultValue", "toNonNegativeInt", "trimSubstring", "wait", "withSuppressed", "", "Ljava/lang/Exception;", "Lkotlin/Exception;", "suppressed", "writeMedium", "Lokio/BufferedSink;", "medium", "okhttp"}, k = 2, mv = {1, 4, 0})
/* compiled from: 01C2 */
public final class Util {
    public static final byte[] EMPTY_BYTE_ARRAY;
    public static final Headers EMPTY_HEADERS = Headers.Companion.of(new String[0]);
    public static final RequestBody EMPTY_REQUEST;
    public static final ResponseBody EMPTY_RESPONSE;
    private static final Options UNICODE_BOMS = Options.Companion.of(ByteString.Companion.decodeHex("efbbbf"), ByteString.Companion.decodeHex("feff"), ByteString.Companion.decodeHex("fffe"), ByteString.Companion.decodeHex("0000ffff"), ByteString.Companion.decodeHex("ffff0000"));
    public static final TimeZone UTC;
    private static final Regex VERIFY_AS_IP_ADDRESS = new Regex("([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
    public static final boolean assertionsEnabled = false;
    public static final String okHttpName;
    public static final String userAgent = "okhttp/4.9.0";

    static {
        byte[] bArr = new byte[0];
        EMPTY_BYTE_ARRAY = bArr;
        EMPTY_RESPONSE = ResponseBody.Companion.create$default(ResponseBody.Companion, bArr, (MediaType) null, 1, (Object) null);
        EMPTY_REQUEST = RequestBody.Companion.create$default(RequestBody.Companion, bArr, (MediaType) null, 0, 0, 7, (Object) null);
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Intrinsics.checkNotNull(timeZone);
        UTC = timeZone;
        Class<OkHttpClient> cls = OkHttpClient.class;
        String name = OkHttpClient.class.getName();
        Intrinsics.checkNotNullExpressionValue(name, "OkHttpClient::class.java.name");
        String removePrefix = StringsKt.removePrefix(name, (CharSequence) "okhttp3.");
        Log1F380D.a((Object) removePrefix);
        String removeSuffix = StringsKt.removeSuffix(removePrefix, (CharSequence) "Client");
        Log1F380D.a((Object) removeSuffix);
        okHttpName = removeSuffix;
    }

    public static final <E> void addIfAbsent(List<E> $this$addIfAbsent, E element) {
        Intrinsics.checkNotNullParameter($this$addIfAbsent, "$this$addIfAbsent");
        if (!$this$addIfAbsent.contains(element)) {
            $this$addIfAbsent.add(element);
        }
    }

    public static final int and(byte $this$and, int mask) {
        return $this$and & mask;
    }

    public static final int and(short $this$and, int mask) {
        return $this$and & mask;
    }

    public static final long and(int $this$and, long mask) {
        return ((long) $this$and) & mask;
    }

    public static final EventListener.Factory asFactory(EventListener $this$asFactory) {
        Intrinsics.checkNotNullParameter($this$asFactory, "$this$asFactory");
        return new Util$asFactory$1($this$asFactory);
    }

    public static final void assertThreadDoesntHoldLock(Object $this$assertThreadDoesntHoldLock) {
        Intrinsics.checkNotNullParameter($this$assertThreadDoesntHoldLock, "$this$assertThreadDoesntHoldLock");
        if (assertionsEnabled && Thread.holdsLock($this$assertThreadDoesntHoldLock)) {
            StringBuilder append = new StringBuilder().append("Thread ");
            Thread currentThread = Thread.currentThread();
            Intrinsics.checkNotNullExpressionValue(currentThread, "Thread.currentThread()");
            throw new AssertionError(append.append(currentThread.getName()).append(" MUST NOT hold lock on ").append($this$assertThreadDoesntHoldLock).toString());
        }
    }

    public static final void assertThreadHoldsLock(Object $this$assertThreadHoldsLock) {
        Intrinsics.checkNotNullParameter($this$assertThreadHoldsLock, "$this$assertThreadHoldsLock");
        if (assertionsEnabled && !Thread.holdsLock($this$assertThreadHoldsLock)) {
            StringBuilder append = new StringBuilder().append("Thread ");
            Thread currentThread = Thread.currentThread();
            Intrinsics.checkNotNullExpressionValue(currentThread, "Thread.currentThread()");
            throw new AssertionError(append.append(currentThread.getName()).append(" MUST hold lock on ").append($this$assertThreadHoldsLock).toString());
        }
    }

    public static final boolean canParseAsIpAddress(String $this$canParseAsIpAddress) {
        Intrinsics.checkNotNullParameter($this$canParseAsIpAddress, "$this$canParseAsIpAddress");
        return VERIFY_AS_IP_ADDRESS.matches($this$canParseAsIpAddress);
    }

    public static final boolean canReuseConnectionFor(HttpUrl $this$canReuseConnectionFor, HttpUrl other) {
        Intrinsics.checkNotNullParameter($this$canReuseConnectionFor, "$this$canReuseConnectionFor");
        Intrinsics.checkNotNullParameter(other, "other");
        return Intrinsics.areEqual((Object) $this$canReuseConnectionFor.host(), (Object) other.host()) && $this$canReuseConnectionFor.port() == other.port() && Intrinsics.areEqual((Object) $this$canReuseConnectionFor.scheme(), (Object) other.scheme());
    }

    public static final int checkDuration(String name, long duration, TimeUnit unit) {
        Intrinsics.checkNotNullParameter(name, "name");
        boolean z = true;
        if (duration >= 0) {
            if (unit != null) {
                long millis = unit.toMillis(duration);
                if (millis <= ((long) Integer.MAX_VALUE)) {
                    if (millis == 0 && duration > 0) {
                        z = false;
                    }
                    if (z) {
                        return (int) millis;
                    }
                    throw new IllegalArgumentException((name + " too small.").toString());
                }
                throw new IllegalArgumentException((name + " too large.").toString());
            }
            throw new IllegalStateException("unit == null".toString());
        }
        throw new IllegalStateException((name + " < 0").toString());
    }

    public static final void checkOffsetAndCount(long arrayLength, long offset, long count) {
        if ((offset | count) < 0 || offset > arrayLength || arrayLength - offset < count) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static final void closeQuietly(Closeable $this$closeQuietly) {
        Intrinsics.checkNotNullParameter($this$closeQuietly, "$this$closeQuietly");
        try {
            $this$closeQuietly.close();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
        }
    }

    public static final void closeQuietly(ServerSocket $this$closeQuietly) {
        Intrinsics.checkNotNullParameter($this$closeQuietly, "$this$closeQuietly");
        try {
            $this$closeQuietly.close();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
        }
    }

    public static final void closeQuietly(Socket $this$closeQuietly) {
        Intrinsics.checkNotNullParameter($this$closeQuietly, "$this$closeQuietly");
        try {
            $this$closeQuietly.close();
        } catch (AssertionError e) {
            throw e;
        } catch (RuntimeException e2) {
            throw e2;
        } catch (Exception e3) {
        }
    }

    public static final String[] concat(String[] $this$concat, String value) {
        Intrinsics.checkNotNullParameter($this$concat, "$this$concat");
        Intrinsics.checkNotNullParameter(value, "value");
        Object[] copyOf = Arrays.copyOf($this$concat, $this$concat.length + 1);
        Intrinsics.checkNotNullExpressionValue(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        String[] strArr = (String[]) copyOf;
        strArr[ArraysKt.getLastIndex((T[]) strArr)] = value;
        if (strArr != null) {
            return strArr;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<kotlin.String>");
    }

    public static final int delimiterOffset(String $this$delimiterOffset, char delimiter, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$delimiterOffset, "$this$delimiterOffset");
        for (int i = startIndex; i < endIndex; i++) {
            if ($this$delimiterOffset.charAt(i) == delimiter) {
                return i;
            }
        }
        return endIndex;
    }

    public static final int delimiterOffset(String $this$delimiterOffset, String delimiters, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$delimiterOffset, "$this$delimiterOffset");
        Intrinsics.checkNotNullParameter(delimiters, "delimiters");
        for (int i = startIndex; i < endIndex; i++) {
            if (StringsKt.contains$default((CharSequence) delimiters, $this$delimiterOffset.charAt(i), false, 2, (Object) null)) {
                return i;
            }
        }
        return endIndex;
    }

    public static /* synthetic */ int delimiterOffset$default(String str, char c, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = str.length();
        }
        return delimiterOffset(str, c, i, i2);
    }

    public static /* synthetic */ int delimiterOffset$default(String str, String str2, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = str.length();
        }
        return delimiterOffset(str, str2, i, i2);
    }

    public static final boolean discard(Source $this$discard, int timeout, TimeUnit timeUnit) {
        Intrinsics.checkNotNullParameter($this$discard, "$this$discard");
        Intrinsics.checkNotNullParameter(timeUnit, "timeUnit");
        try {
            return skipAll($this$discard, timeout, timeUnit);
        } catch (IOException e) {
            return false;
        }
    }

    public static final <T> List<T> filterList(Iterable<? extends T> $this$filterList, Function1<? super T, Boolean> predicate) {
        Intrinsics.checkNotNullParameter($this$filterList, "$this$filterList");
        Intrinsics.checkNotNullParameter(predicate, "predicate");
        List<T> emptyList = CollectionsKt.emptyList();
        for (Object next : $this$filterList) {
            if (predicate.invoke(next).booleanValue()) {
                if (emptyList.isEmpty()) {
                    emptyList = new ArrayList<>();
                }
                if (emptyList != null) {
                    TypeIntrinsics.asMutableList(emptyList).add(next);
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableList<T>");
                }
            }
        }
        return emptyList;
    }

    public static final boolean hasIntersection(String[] $this$hasIntersection, String[] other, Comparator<? super String> comparator) {
        Intrinsics.checkNotNullParameter($this$hasIntersection, "$this$hasIntersection");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        if (!($this$hasIntersection.length == 0) && other != null) {
            if (!(other.length == 0)) {
                for (String str : $this$hasIntersection) {
                    for (String compare : other) {
                        if (comparator.compare(str, compare) == 0) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }
        return false;
    }

    public static final long headersContentLength(Response $this$headersContentLength) {
        Intrinsics.checkNotNullParameter($this$headersContentLength, "$this$headersContentLength");
        String str = $this$headersContentLength.headers().get("Content-Length");
        if (str != null) {
            return toLongOrDefault(str, -1);
        }
        return -1;
    }

    public static final void ignoreIoExceptions(Function0<Unit> block) {
        Intrinsics.checkNotNullParameter(block, "block");
        try {
            block.invoke();
        } catch (IOException e) {
        }
    }

    @SafeVarargs
    public static final <T> List<T> immutableListOf(T... elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        Object[] objArr = (Object[]) elements.clone();
        List<T> unmodifiableList = Collections.unmodifiableList(CollectionsKt.listOf(Arrays.copyOf(objArr, objArr.length)));
        Intrinsics.checkNotNullExpressionValue(unmodifiableList, "Collections.unmodifiable…istOf(*elements.clone()))");
        return unmodifiableList;
    }

    public static final int indexOf(String[] $this$indexOf, String value, Comparator<String> comparator) {
        Intrinsics.checkNotNullParameter($this$indexOf, "$this$indexOf");
        Intrinsics.checkNotNullParameter(value, "value");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        String[] strArr = $this$indexOf;
        int length = strArr.length;
        for (int i = 0; i < length; i++) {
            if (comparator.compare(strArr[i], value) == 0) {
                return i;
            }
        }
        return -1;
    }

    public static final int indexOfControlOrNonAscii(String $this$indexOfControlOrNonAscii) {
        Intrinsics.checkNotNullParameter($this$indexOfControlOrNonAscii, "$this$indexOfControlOrNonAscii");
        int length = $this$indexOfControlOrNonAscii.length();
        for (int i = 0; i < length; i++) {
            char charAt = $this$indexOfControlOrNonAscii.charAt(i);
            if (Intrinsics.compare((int) charAt, 31) <= 0 || Intrinsics.compare((int) charAt, 127) >= 0) {
                return i;
            }
        }
        return -1;
    }

    public static final int indexOfFirstNonAsciiWhitespace(String $this$indexOfFirstNonAsciiWhitespace, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$indexOfFirstNonAsciiWhitespace, "$this$indexOfFirstNonAsciiWhitespace");
        int i = startIndex;
        while (i < endIndex) {
            switch ($this$indexOfFirstNonAsciiWhitespace.charAt(i)) {
                case 9:
                case 10:
                case 12:
                case 13:
                case ' ':
                    i++;
                default:
                    return i;
            }
        }
        return endIndex;
    }

    public static /* synthetic */ int indexOfFirstNonAsciiWhitespace$default(String str, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = str.length();
        }
        return indexOfFirstNonAsciiWhitespace(str, i, i2);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final int indexOfLastNonAsciiWhitespace(java.lang.String r2, int r3, int r4) {
        /*
            java.lang.String r0 = "$this$indexOfLastNonAsciiWhitespace"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r0)
            int r0 = r4 + -1
            if (r0 < r3) goto L_0x001a
        L_0x0009:
            char r1 = r2.charAt(r0)
            switch(r1) {
                case 9: goto L_0x0013;
                case 10: goto L_0x0013;
                case 12: goto L_0x0013;
                case 13: goto L_0x0013;
                case 32: goto L_0x0013;
                default: goto L_0x0010;
            }
        L_0x0010:
            int r1 = r0 + 1
            return r1
        L_0x0013:
            if (r0 == r3) goto L_0x001a
            int r0 = r0 + -1
            goto L_0x0009
        L_0x001a:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.Util.indexOfLastNonAsciiWhitespace(java.lang.String, int, int):int");
    }

    public static /* synthetic */ int indexOfLastNonAsciiWhitespace$default(String str, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = str.length();
        }
        return indexOfLastNonAsciiWhitespace(str, i, i2);
    }

    public static final int indexOfNonWhitespace(String $this$indexOfNonWhitespace, int startIndex) {
        Intrinsics.checkNotNullParameter($this$indexOfNonWhitespace, "$this$indexOfNonWhitespace");
        int length = $this$indexOfNonWhitespace.length();
        for (int i = startIndex; i < length; i++) {
            char charAt = $this$indexOfNonWhitespace.charAt(i);
            if (charAt != ' ' && charAt != 9) {
                return i;
            }
        }
        return $this$indexOfNonWhitespace.length();
    }

    public static /* synthetic */ int indexOfNonWhitespace$default(String str, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 0;
        }
        return indexOfNonWhitespace(str, i);
    }

    public static final String[] intersect(String[] $this$intersect, String[] other, Comparator<? super String> comparator) {
        Intrinsics.checkNotNullParameter($this$intersect, "$this$intersect");
        Intrinsics.checkNotNullParameter(other, "other");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        List arrayList = new ArrayList();
        for (String str : $this$intersect) {
            int length = other.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (comparator.compare(str, other[i]) == 0) {
                    arrayList.add(str);
                    break;
                } else {
                    i++;
                }
            }
        }
        Object[] array = arrayList.toArray(new String[0]);
        if (array != null) {
            return (String[]) array;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002f, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0030, code lost:
        kotlin.io.CloseableKt.closeFinally(r0, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0033, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final boolean isCivilized(okhttp3.internal.io.FileSystem r5, java.io.File r6) {
        /*
            java.lang.String r0 = "$this$isCivilized"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r5, r0)
            java.lang.String r0 = "file"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
            okio.Sink r0 = r5.sink(r6)
            java.io.Closeable r0 = (java.io.Closeable) r0
            r1 = 0
            r2 = r1
            java.lang.Throwable r2 = (java.lang.Throwable) r2
            r2 = r0
            okio.Sink r2 = (okio.Sink) r2     // Catch:{ all -> 0x002d }
            r3 = 0
            r5.delete(r6)     // Catch:{ IOException -> 0x0021 }
            r2 = 1
            kotlin.io.CloseableKt.closeFinally(r0, r1)
            return r2
        L_0x0021:
            r4 = move-exception
            kotlin.Unit r2 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x002d }
            kotlin.io.CloseableKt.closeFinally(r0, r1)
            r5.delete(r6)
            r0 = 0
            return r0
        L_0x002d:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x002f }
        L_0x002f:
            r2 = move-exception
            kotlin.io.CloseableKt.closeFinally(r0, r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.Util.isCivilized(okhttp3.internal.io.FileSystem, java.io.File):boolean");
    }

    public static final boolean isHealthy(Socket $this$isHealthy, BufferedSource source) {
        int soTimeout;
        Intrinsics.checkNotNullParameter($this$isHealthy, "$this$isHealthy");
        Intrinsics.checkNotNullParameter(source, "source");
        try {
            soTimeout = $this$isHealthy.getSoTimeout();
            $this$isHealthy.setSoTimeout(1);
            boolean z = !source.exhausted();
            $this$isHealthy.setSoTimeout(soTimeout);
            return z;
        } catch (SocketTimeoutException e) {
            return true;
        } catch (IOException e2) {
            return false;
        } catch (Throwable th) {
            $this$isHealthy.setSoTimeout(soTimeout);
            throw th;
        }
    }

    public static final void notify(Object $this$notify) {
        Intrinsics.checkNotNullParameter($this$notify, "$this$notify");
        $this$notify.notify();
    }

    public static final void notifyAll(Object $this$notifyAll) {
        Intrinsics.checkNotNullParameter($this$notifyAll, "$this$notifyAll");
        $this$notifyAll.notifyAll();
    }

    public static final int parseHexDigit(char $this$parseHexDigit) {
        if ('0' <= $this$parseHexDigit && '9' >= $this$parseHexDigit) {
            return $this$parseHexDigit - '0';
        }
        if ('a' <= $this$parseHexDigit && 'f' >= $this$parseHexDigit) {
            return ($this$parseHexDigit - 'a') + 10;
        }
        if ('A' <= $this$parseHexDigit && 'F' >= $this$parseHexDigit) {
            return ($this$parseHexDigit - 'A') + 10;
        }
        return -1;
    }

    public static final String peerName(Socket $this$peerName) {
        Intrinsics.checkNotNullParameter($this$peerName, "$this$peerName");
        SocketAddress remoteSocketAddress = $this$peerName.getRemoteSocketAddress();
        if (!(remoteSocketAddress instanceof InetSocketAddress)) {
            return remoteSocketAddress.toString();
        }
        String hostName = ((InetSocketAddress) remoteSocketAddress).getHostName();
        Intrinsics.checkNotNullExpressionValue(hostName, "address.hostName");
        return hostName;
    }

    public static final Charset readBomAsCharset(BufferedSource $this$readBomAsCharset, Charset charset) throws IOException {
        Intrinsics.checkNotNullParameter($this$readBomAsCharset, "$this$readBomAsCharset");
        Intrinsics.checkNotNullParameter(charset, "default");
        switch ($this$readBomAsCharset.select(UNICODE_BOMS)) {
            case -1:
                return charset;
            case 0:
                Charset charset2 = StandardCharsets.UTF_8;
                Intrinsics.checkNotNullExpressionValue(charset2, "UTF_8");
                return charset2;
            case 1:
                Charset charset3 = StandardCharsets.UTF_16BE;
                Intrinsics.checkNotNullExpressionValue(charset3, "UTF_16BE");
                return charset3;
            case 2:
                Charset charset4 = StandardCharsets.UTF_16LE;
                Intrinsics.checkNotNullExpressionValue(charset4, "UTF_16LE");
                return charset4;
            case 3:
                return Charsets.INSTANCE.UTF32_BE();
            case 4:
                return Charsets.INSTANCE.UTF32_LE();
            default:
                throw new AssertionError();
        }
    }

    public static final <T> T readFieldOrNull(Object instance, Class<T> fieldType, String fieldName) {
        Object readFieldOrNull;
        Intrinsics.checkNotNullParameter(instance, "instance");
        Intrinsics.checkNotNullParameter(fieldType, "fieldType");
        Intrinsics.checkNotNullParameter(fieldName, "fieldName");
        Class<?> cls = instance.getClass();
        while (!Intrinsics.areEqual((Object) cls, (Object) Object.class)) {
            try {
                Field declaredField = cls.getDeclaredField(fieldName);
                Intrinsics.checkNotNullExpressionValue(declaredField, "field");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(instance);
                if (!fieldType.isInstance(obj)) {
                    return null;
                }
                return fieldType.cast(obj);
            } catch (NoSuchFieldException e) {
                Class<? super Object> superclass = cls.getSuperclass();
                Intrinsics.checkNotNullExpressionValue(superclass, "c.superclass");
                cls = superclass;
            }
        }
        if (!(true ^ Intrinsics.areEqual((Object) fieldName, (Object) "delegate")) || (readFieldOrNull = readFieldOrNull(instance, Object.class, "delegate")) == null) {
            return null;
        }
        return readFieldOrNull(readFieldOrNull, fieldType, fieldName);
    }

    public static final int readMedium(BufferedSource $this$readMedium) throws IOException {
        Intrinsics.checkNotNullParameter($this$readMedium, "$this$readMedium");
        return (and($this$readMedium.readByte(), 255) << 16) | (and($this$readMedium.readByte(), 255) << 8) | and($this$readMedium.readByte(), 255);
    }

    public static final int skipAll(Buffer $this$skipAll, byte b) {
        Intrinsics.checkNotNullParameter($this$skipAll, "$this$skipAll");
        int i = 0;
        while (!$this$skipAll.exhausted() && $this$skipAll.getByte(0) == b) {
            i++;
            $this$skipAll.readByte();
        }
        return i;
    }

    public static final boolean skipAll(Source $this$skipAll, int duration, TimeUnit timeUnit) throws IOException {
        Intrinsics.checkNotNullParameter($this$skipAll, "$this$skipAll");
        Intrinsics.checkNotNullParameter(timeUnit, "timeUnit");
        long nanoTime = System.nanoTime();
        long deadlineNanoTime = $this$skipAll.timeout().hasDeadline() ? $this$skipAll.timeout().deadlineNanoTime() - nanoTime : Long.MAX_VALUE;
        $this$skipAll.timeout().deadlineNanoTime(Math.min(deadlineNanoTime, timeUnit.toNanos((long) duration)) + nanoTime);
        try {
            Buffer buffer = new Buffer();
            while ($this$skipAll.read(buffer, 8192) != -1) {
                buffer.clear();
            }
            if (deadlineNanoTime == Long.MAX_VALUE) {
                $this$skipAll.timeout().clearDeadline();
                return true;
            }
            $this$skipAll.timeout().deadlineNanoTime(nanoTime + deadlineNanoTime);
            return true;
        } catch (InterruptedIOException e) {
            if (deadlineNanoTime == Long.MAX_VALUE) {
                $this$skipAll.timeout().clearDeadline();
            } else {
                $this$skipAll.timeout().deadlineNanoTime(nanoTime + deadlineNanoTime);
            }
            return false;
        } catch (Throwable th) {
            if (deadlineNanoTime == Long.MAX_VALUE) {
                $this$skipAll.timeout().clearDeadline();
            } else {
                $this$skipAll.timeout().deadlineNanoTime(nanoTime + deadlineNanoTime);
            }
            throw th;
        }
    }

    public static final ThreadFactory threadFactory(String name, boolean daemon) {
        Intrinsics.checkNotNullParameter(name, "name");
        return new Util$threadFactory$1(name, daemon);
    }

    public static final void threadName(String name, Function0<Unit> block) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(block, "block");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkNotNullExpressionValue(currentThread, "currentThread");
        String name2 = currentThread.getName();
        currentThread.setName(name);
        try {
            block.invoke();
        } finally {
            InlineMarker.finallyStart(1);
            currentThread.setName(name2);
            InlineMarker.finallyEnd(1);
        }
    }

    public static final List<Header> toHeaderList(Headers $this$toHeaderList) {
        Intrinsics.checkNotNullParameter($this$toHeaderList, "$this$toHeaderList");
        Iterable until = RangesKt.until(0, $this$toHeaderList.size());
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(until, 10));
        Iterator it = until.iterator();
        while (it.hasNext()) {
            int nextInt = ((IntIterator) it).nextInt();
            arrayList.add(new Header($this$toHeaderList.name(nextInt), $this$toHeaderList.value(nextInt)));
        }
        return (List) arrayList;
    }

    public static final Headers toHeaders(List<Header> $this$toHeaders) {
        Intrinsics.checkNotNullParameter($this$toHeaders, "$this$toHeaders");
        Headers.Builder builder = new Headers.Builder();
        for (Header next : $this$toHeaders) {
            builder.addLenient$okhttp(next.component1().utf8(), next.component2().utf8());
        }
        return builder.build();
    }

    public static final String toHostHeader(HttpUrl $this$toHostHeader, boolean includeDefaultPort) {
        Intrinsics.checkNotNullParameter($this$toHostHeader, "$this$toHostHeader");
        String host = StringsKt.contains$default((CharSequence) $this$toHostHeader.host(), (CharSequence) ":", false, 2, (Object) null) ? '[' + $this$toHostHeader.host() + ']' : $this$toHostHeader.host();
        return (includeDefaultPort || $this$toHostHeader.port() != HttpUrl.Companion.defaultPort($this$toHostHeader.scheme())) ? host + ':' + $this$toHostHeader.port() : host;
    }

    public static final <T> List<T> toImmutableList(List<? extends T> $this$toImmutableList) {
        Intrinsics.checkNotNullParameter($this$toImmutableList, "$this$toImmutableList");
        List<T> unmodifiableList = Collections.unmodifiableList(CollectionsKt.toMutableList($this$toImmutableList));
        Intrinsics.checkNotNullExpressionValue(unmodifiableList, "Collections.unmodifiableList(toMutableList())");
        return unmodifiableList;
    }

    public static final <K, V> Map<K, V> toImmutableMap(Map<K, ? extends V> $this$toImmutableMap) {
        Intrinsics.checkNotNullParameter($this$toImmutableMap, "$this$toImmutableMap");
        if ($this$toImmutableMap.isEmpty()) {
            return MapsKt.emptyMap();
        }
        Map<K, V> unmodifiableMap = Collections.unmodifiableMap(new LinkedHashMap($this$toImmutableMap));
        Intrinsics.checkNotNullExpressionValue(unmodifiableMap, "Collections.unmodifiableMap(LinkedHashMap(this))");
        return unmodifiableMap;
    }

    public static final long toLongOrDefault(String $this$toLongOrDefault, long defaultValue) {
        Intrinsics.checkNotNullParameter($this$toLongOrDefault, "$this$toLongOrDefault");
        try {
            return Long.parseLong($this$toLongOrDefault);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static final int toNonNegativeInt(String $this$toNonNegativeInt, int defaultValue) {
        if ($this$toNonNegativeInt == null) {
            return defaultValue;
        }
        try {
            long parseLong = Long.parseLong($this$toNonNegativeInt);
            if (parseLong > ((long) Integer.MAX_VALUE)) {
                return Integer.MAX_VALUE;
            }
            if (parseLong < 0) {
                return 0;
            }
            return (int) parseLong;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static final String trimSubstring(String $this$trimSubstring, int startIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$trimSubstring, "$this$trimSubstring");
        int indexOfFirstNonAsciiWhitespace = indexOfFirstNonAsciiWhitespace($this$trimSubstring, startIndex, endIndex);
        String substring = $this$trimSubstring.substring(indexOfFirstNonAsciiWhitespace, indexOfLastNonAsciiWhitespace($this$trimSubstring, indexOfFirstNonAsciiWhitespace, endIndex));
        Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return substring;
    }

    public static final void wait(Object $this$wait) {
        Intrinsics.checkNotNullParameter($this$wait, "$this$wait");
        $this$wait.wait();
    }

    public static final Throwable withSuppressed(Exception $this$withSuppressed, List<? extends Exception> suppressed) {
        Intrinsics.checkNotNullParameter($this$withSuppressed, "$this$withSuppressed");
        Intrinsics.checkNotNullParameter(suppressed, "suppressed");
        Exception exc = $this$withSuppressed;
        if (suppressed.size() > 1) {
            System.out.println(suppressed);
        }
        for (Exception addSuppressed : suppressed) {
            ExceptionsKt.addSuppressed(exc, addSuppressed);
        }
        return $this$withSuppressed;
    }

    public static final void writeMedium(BufferedSink $this$writeMedium, int medium) throws IOException {
        Intrinsics.checkNotNullParameter($this$writeMedium, "$this$writeMedium");
        $this$writeMedium.writeByte((medium >>> 16) & 255);
        $this$writeMedium.writeByte((medium >>> 8) & 255);
        $this$writeMedium.writeByte(medium & 255);
    }

    public static final String format(String format, Object... args) {
        Intrinsics.checkNotNullParameter(format, "format");
        Intrinsics.checkNotNullParameter(args, "args");
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        Locale locale = Locale.US;
        Object[] copyOf = Arrays.copyOf(args, args.length);
        String format2 = String.format(locale, format, Arrays.copyOf(copyOf, copyOf.length));
        Log1F380D.a((Object) format2);
        Intrinsics.checkNotNullExpressionValue(format2, "java.lang.String.format(locale, format, *args)");
        return format2;
    }

    public static final String toHexString(int $this$toHexString) {
        String hexString = Integer.toHexString($this$toHexString);
        Log1F380D.a((Object) hexString);
        Intrinsics.checkNotNullExpressionValue(hexString, "Integer.toHexString(this)");
        return hexString;
    }

    public static final String toHexString(long $this$toHexString) {
        String hexString = Long.toHexString($this$toHexString);
        Log1F380D.a((Object) hexString);
        Intrinsics.checkNotNullExpressionValue(hexString, "java.lang.Long.toHexString(this)");
        return hexString;
    }

    public static /* synthetic */ String toHostHeader$default(HttpUrl httpUrl, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        String hostHeader = toHostHeader(httpUrl, z);
        Log1F380D.a((Object) hostHeader);
        return hostHeader;
    }

    public static /* synthetic */ String trimSubstring$default(String str, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = str.length();
        }
        String trimSubstring = trimSubstring(str, i, i2);
        Log1F380D.a((Object) trimSubstring);
        return trimSubstring;
    }
}

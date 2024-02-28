package okhttp3.internal.http;

import java.io.IOException;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import mt.Log1F380D;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okio.GzipSource;
import okio.Okio;
import okio.Source;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0002J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lokhttp3/internal/http/BridgeInterceptor;", "Lokhttp3/Interceptor;", "cookieJar", "Lokhttp3/CookieJar;", "(Lokhttp3/CookieJar;)V", "cookieHeader", "", "cookies", "", "Lokhttp3/Cookie;", "intercept", "Lokhttp3/Response;", "chain", "Lokhttp3/Interceptor$Chain;", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: 01CE */
public final class BridgeInterceptor implements Interceptor {
    private final CookieJar cookieJar;

    public BridgeInterceptor(CookieJar cookieJar2) {
        Intrinsics.checkNotNullParameter(cookieJar2, "cookieJar");
        this.cookieJar = cookieJar2;
    }

    private final String cookieHeader(List<Cookie> cookies) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = sb;
        int i = 0;
        for (Object next : cookies) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            Cookie cookie = (Cookie) next;
            if (i > 0) {
                sb2.append("; ");
            }
            sb2.append(cookie.name()).append('=').append(cookie.value());
            i = i2;
        }
        String sb3 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        ResponseBody body;
        Interceptor.Chain chain2 = chain;
        Intrinsics.checkNotNullParameter(chain2, "chain");
        Request request = chain.request();
        Request.Builder newBuilder = request.newBuilder();
        RequestBody body2 = request.body();
        if (body2 != null) {
            MediaType contentType = body2.contentType();
            if (contentType != null) {
                newBuilder.header("Content-Type", contentType.toString());
            }
            long contentLength = body2.contentLength();
            if (contentLength != -1) {
                String valueOf = String.valueOf(contentLength);
                Log1F380D.a((Object) valueOf);
                newBuilder.header("Content-Length", valueOf);
                newBuilder.removeHeader("Transfer-Encoding");
            } else {
                newBuilder.header("Transfer-Encoding", "chunked");
                newBuilder.removeHeader("Content-Length");
            }
        }
        if (request.header("Host") == null) {
            String hostHeader$default = Util.toHostHeader$default(request.url(), false, 1, (Object) null);
            Log1F380D.a((Object) hostHeader$default);
            newBuilder.header("Host", hostHeader$default);
        }
        if (request.header("Connection") == null) {
            newBuilder.header("Connection", "Keep-Alive");
        }
        boolean z = false;
        if (request.header("Accept-Encoding") == null && request.header("Range") == null) {
            z = true;
            newBuilder.header("Accept-Encoding", "gzip");
        }
        List<Cookie> loadForRequest = this.cookieJar.loadForRequest(request.url());
        if (!loadForRequest.isEmpty()) {
            newBuilder.header("Cookie", cookieHeader(loadForRequest));
        }
        if (request.header("User-Agent") == null) {
            newBuilder.header("User-Agent", Util.userAgent);
        }
        Response proceed = chain2.proceed(newBuilder.build());
        HttpHeaders.receiveHeaders(this.cookieJar, request.url(), proceed.headers());
        Response.Builder request2 = proceed.newBuilder().request(request);
        if (z) {
            String header$default = Response.header$default(proceed, "Content-Encoding", (String) null, 2, (Object) null);
            Log1F380D.a((Object) header$default);
            if (StringsKt.equals("gzip", header$default, true) && HttpHeaders.promisesBody(proceed) && (body = proceed.body()) != null) {
                GzipSource gzipSource = new GzipSource(body.source());
                request2.headers(proceed.headers().newBuilder().removeAll("Content-Encoding").removeAll("Content-Length").build());
                String header$default2 = Response.header$default(proceed, "Content-Type", (String) null, 2, (Object) null);
                Log1F380D.a((Object) header$default2);
                request2.body(new RealResponseBody(header$default2, -1, Okio.buffer((Source) gzipSource)));
            }
        }
        return request2.build();
    }
}

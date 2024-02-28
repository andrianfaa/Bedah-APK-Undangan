package okhttp3.internal.cache;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import java.io.Closeable;
import java.io.IOException;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import mt.Log1F380D;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheStrategy;
import okhttp3.internal.connection.RealCall;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.RealResponseBody;
import okio.Okio;
import okio.Sink;
import okio.Source;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004J\u001a\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\bH\u0002J\u0010\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eH\u0016R\u0016\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0010"}, d2 = {"Lokhttp3/internal/cache/CacheInterceptor;", "Lokhttp3/Interceptor;", "cache", "Lokhttp3/Cache;", "(Lokhttp3/Cache;)V", "getCache$okhttp", "()Lokhttp3/Cache;", "cacheWritingResponse", "Lokhttp3/Response;", "cacheRequest", "Lokhttp3/internal/cache/CacheRequest;", "response", "intercept", "chain", "Lokhttp3/Interceptor$Chain;", "Companion", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: 01C4 */
public final class CacheInterceptor implements Interceptor {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private final Cache cache;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0004H\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\u0010\u0010\u000b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\u0014\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\rH\u0002¨\u0006\u000f"}, d2 = {"Lokhttp3/internal/cache/CacheInterceptor$Companion;", "", "()V", "combine", "Lokhttp3/Headers;", "cachedHeaders", "networkHeaders", "isContentSpecificHeader", "", "fieldName", "", "isEndToEnd", "stripBody", "Lokhttp3/Response;", "response", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: CacheInterceptor.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        /* access modifiers changed from: private */
        public final Headers combine(Headers cachedHeaders, Headers networkHeaders) {
            Headers.Builder builder = new Headers.Builder();
            int size = cachedHeaders.size();
            for (int i = 0; i < size; i++) {
                String name = cachedHeaders.name(i);
                String value = cachedHeaders.value(i);
                if ((!StringsKt.equals("Warning", name, true) || !StringsKt.startsWith$default(value, "1", false, 2, (Object) null)) && (isContentSpecificHeader(name) || !isEndToEnd(name) || networkHeaders.get(name) == null)) {
                    builder.addLenient$okhttp(name, value);
                }
            }
            int size2 = networkHeaders.size();
            for (int i2 = 0; i2 < size2; i2++) {
                String name2 = networkHeaders.name(i2);
                if (!isContentSpecificHeader(name2) && isEndToEnd(name2)) {
                    builder.addLenient$okhttp(name2, networkHeaders.value(i2));
                }
            }
            return builder.build();
        }

        private final boolean isContentSpecificHeader(String fieldName) {
            return StringsKt.equals("Content-Length", fieldName, true) || StringsKt.equals("Content-Encoding", fieldName, true) || StringsKt.equals("Content-Type", fieldName, true);
        }

        private final boolean isEndToEnd(String fieldName) {
            return !StringsKt.equals("Connection", fieldName, true) && !StringsKt.equals("Keep-Alive", fieldName, true) && !StringsKt.equals("Proxy-Authenticate", fieldName, true) && !StringsKt.equals("Proxy-Authorization", fieldName, true) && !StringsKt.equals("TE", fieldName, true) && !StringsKt.equals("Trailers", fieldName, true) && !StringsKt.equals("Transfer-Encoding", fieldName, true) && !StringsKt.equals("Upgrade", fieldName, true);
        }

        /* access modifiers changed from: private */
        public final Response stripBody(Response response) {
            return (response != null ? response.body() : null) != null ? response.newBuilder().body((ResponseBody) null).build() : response;
        }
    }

    public CacheInterceptor(Cache cache2) {
        this.cache = cache2;
    }

    private final Response cacheWritingResponse(CacheRequest cacheRequest, Response response) throws IOException {
        if (cacheRequest == null) {
            return response;
        }
        Sink body = cacheRequest.body();
        ResponseBody body2 = response.body();
        Intrinsics.checkNotNull(body2);
        CacheInterceptor$cacheWritingResponse$cacheWritingSource$1 cacheInterceptor$cacheWritingResponse$cacheWritingSource$1 = new CacheInterceptor$cacheWritingResponse$cacheWritingSource$1(body2.source(), cacheRequest, Okio.buffer(body));
        String header$default = Response.header$default(response, "Content-Type", (String) null, 2, (Object) null);
        Log1F380D.a((Object) header$default);
        return response.newBuilder().body(new RealResponseBody(header$default, response.body().contentLength(), Okio.buffer((Source) cacheInterceptor$cacheWritingResponse$cacheWritingSource$1))).build();
    }

    public final Cache getCache$okhttp() {
        return this.cache;
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        EventListener eventListener;
        ResponseBody body;
        ResponseBody body2;
        Intrinsics.checkNotNullParameter(chain, "chain");
        Call call = chain.call();
        Cache cache2 = this.cache;
        Response response = cache2 != null ? cache2.get$okhttp(chain.request()) : null;
        CacheStrategy compute = new CacheStrategy.Factory(System.currentTimeMillis(), chain.request(), response).compute();
        Request networkRequest = compute.getNetworkRequest();
        Response cacheResponse = compute.getCacheResponse();
        Cache cache3 = this.cache;
        if (cache3 != null) {
            cache3.trackResponse$okhttp(compute);
        }
        RealCall realCall = (RealCall) (!(call instanceof RealCall) ? null : call);
        if (realCall == null || (eventListener = realCall.getEventListener$okhttp()) == null) {
            eventListener = EventListener.NONE;
        }
        if (!(response == null || cacheResponse != null || (body2 = response.body()) == null)) {
            Util.closeQuietly((Closeable) body2);
        }
        if (networkRequest == null && cacheResponse == null) {
            Response build = new Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1).code(TypedValues.PositionType.TYPE_PERCENT_HEIGHT).message("Unsatisfiable Request (only-if-cached)").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1).receivedResponseAtMillis(System.currentTimeMillis()).build();
            eventListener.satisfactionFailure(call, build);
            return build;
        } else if (networkRequest == null) {
            Intrinsics.checkNotNull(cacheResponse);
            Response build2 = cacheResponse.newBuilder().cacheResponse(Companion.stripBody(cacheResponse)).build();
            eventListener.cacheHit(call, build2);
            return build2;
        } else {
            if (cacheResponse != null) {
                eventListener.cacheConditionalHit(call, cacheResponse);
            } else if (this.cache != null) {
                eventListener.cacheMiss(call);
            }
            Response response2 = null;
            try {
                Response proceed = chain.proceed(networkRequest);
                if (!(proceed != null || response == null || body == null)) {
                }
                if (cacheResponse != null) {
                    if (proceed == null || proceed.code() != 304) {
                        ResponseBody body3 = cacheResponse.body();
                        if (body3 != null) {
                            Util.closeQuietly((Closeable) body3);
                        }
                    } else {
                        Response.Builder newBuilder = cacheResponse.newBuilder();
                        Companion companion = Companion;
                        Response build3 = newBuilder.headers(companion.combine(cacheResponse.headers(), proceed.headers())).sentRequestAtMillis(proceed.sentRequestAtMillis()).receivedResponseAtMillis(proceed.receivedResponseAtMillis()).cacheResponse(companion.stripBody(cacheResponse)).networkResponse(companion.stripBody(proceed)).build();
                        ResponseBody body4 = proceed.body();
                        Intrinsics.checkNotNull(body4);
                        body4.close();
                        Cache cache4 = this.cache;
                        Intrinsics.checkNotNull(cache4);
                        cache4.trackConditionalCacheHit$okhttp();
                        this.cache.update$okhttp(cacheResponse, build3);
                        eventListener.cacheHit(call, build3);
                        return build3;
                    }
                }
                Intrinsics.checkNotNull(proceed);
                Response.Builder newBuilder2 = proceed.newBuilder();
                Companion companion2 = Companion;
                Response build4 = newBuilder2.cacheResponse(companion2.stripBody(cacheResponse)).networkResponse(companion2.stripBody(proceed)).build();
                if (this.cache != null) {
                    if (HttpHeaders.promisesBody(build4) && CacheStrategy.Companion.isCacheable(build4, networkRequest)) {
                        Response cacheWritingResponse = cacheWritingResponse(this.cache.put$okhttp(build4), build4);
                        Response response3 = cacheWritingResponse;
                        if (cacheResponse != null) {
                            eventListener.cacheMiss(call);
                        }
                        return cacheWritingResponse;
                    } else if (HttpMethod.INSTANCE.invalidatesCache(networkRequest.method())) {
                        try {
                            this.cache.remove$okhttp(networkRequest);
                        } catch (IOException e) {
                        }
                    }
                }
                return build4;
            } finally {
                if (!(response == null || (body = response.body()) == null)) {
                    Util.closeQuietly((Closeable) body);
                }
            }
        }
    }
}

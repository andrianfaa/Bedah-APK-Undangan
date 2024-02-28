package okhttp3.internal.http;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import java.util.List;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import mt.Log1F380D;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okhttp3.internal.connection.Exchange;
import okhttp3.internal.connection.RealCall;
import okhttp3.internal.connection.RouteException;
import okhttp3.internal.http2.ConnectionShutdownException;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001a\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0002J\u0010\u0010\u000e\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0012H\u0002J(\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u0012H\u0002J\u0018\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u0006H\u0002J\u0018\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u001d\u001a\u00020\u001cH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lokhttp3/internal/http/RetryAndFollowUpInterceptor;", "Lokhttp3/Interceptor;", "client", "Lokhttp3/OkHttpClient;", "(Lokhttp3/OkHttpClient;)V", "buildRedirectRequest", "Lokhttp3/Request;", "userResponse", "Lokhttp3/Response;", "method", "", "followUpRequest", "exchange", "Lokhttp3/internal/connection/Exchange;", "intercept", "chain", "Lokhttp3/Interceptor$Chain;", "isRecoverable", "", "e", "Ljava/io/IOException;", "requestSendStarted", "recover", "call", "Lokhttp3/internal/connection/RealCall;", "userRequest", "requestIsOneShot", "retryAfter", "", "defaultDelay", "Companion", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: 01D1 */
public final class RetryAndFollowUpInterceptor implements Interceptor {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final int MAX_FOLLOW_UPS = 20;
    private final OkHttpClient client;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lokhttp3/internal/http/RetryAndFollowUpInterceptor$Companion;", "", "()V", "MAX_FOLLOW_UPS", "", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: RetryAndFollowUpInterceptor.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }

    public RetryAndFollowUpInterceptor(OkHttpClient client2) {
        Intrinsics.checkNotNullParameter(client2, "client");
        this.client = client2;
    }

    private final Request buildRedirectRequest(Response userResponse, String method) {
        HttpUrl resolve;
        RequestBody requestBody = null;
        if (!this.client.followRedirects()) {
            return null;
        }
        String header$default = Response.header$default(userResponse, "Location", (String) null, 2, (Object) null);
        Log1F380D.a((Object) header$default);
        if (header$default == null || (resolve = userResponse.request().url().resolve(header$default)) == null) {
            return null;
        }
        if (!Intrinsics.areEqual((Object) resolve.scheme(), (Object) userResponse.request().url().scheme()) && !this.client.followSslRedirects()) {
            return null;
        }
        Request.Builder newBuilder = userResponse.request().newBuilder();
        if (HttpMethod.permitsRequestBody(method)) {
            int code = userResponse.code();
            boolean z = HttpMethod.INSTANCE.redirectsWithBody(method) || code == 308 || code == 307;
            if (!HttpMethod.INSTANCE.redirectsToGet(method) || code == 308 || code == 307) {
                if (z) {
                    requestBody = userResponse.request().body();
                }
                newBuilder.method(method, requestBody);
            } else {
                newBuilder.method("GET", (RequestBody) null);
            }
            if (!z) {
                newBuilder.removeHeader("Transfer-Encoding");
                newBuilder.removeHeader("Content-Length");
                newBuilder.removeHeader("Content-Type");
            }
        }
        if (!Util.canReuseConnectionFor(userResponse.request().url(), resolve)) {
            newBuilder.removeHeader("Authorization");
        }
        return newBuilder.url(resolve).build();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0003, code lost:
        r1 = r10.getConnection$okhttp();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final okhttp3.Request followUpRequest(okhttp3.Response r9, okhttp3.internal.connection.Exchange r10) throws java.io.IOException {
        /*
            r8 = this;
            r0 = 0
            if (r10 == 0) goto L_0x000e
            okhttp3.internal.connection.RealConnection r1 = r10.getConnection$okhttp()
            if (r1 == 0) goto L_0x000e
            okhttp3.Route r1 = r1.route()
            goto L_0x000f
        L_0x000e:
            r1 = r0
        L_0x000f:
            int r2 = r9.code()
            okhttp3.Request r3 = r9.request()
            java.lang.String r3 = r3.method()
            switch(r2) {
                case 300: goto L_0x00c9;
                case 301: goto L_0x00c9;
                case 302: goto L_0x00c9;
                case 303: goto L_0x00c9;
                case 307: goto L_0x00c9;
                case 308: goto L_0x00c9;
                case 401: goto L_0x00be;
                case 407: goto L_0x009a;
                case 408: goto L_0x0064;
                case 421: goto L_0x003d;
                case 503: goto L_0x001f;
                default: goto L_0x001e;
            }
        L_0x001e:
            return r0
        L_0x001f:
            okhttp3.Response r4 = r9.priorResponse()
            if (r4 == 0) goto L_0x002e
            int r5 = r4.code()
            r6 = 503(0x1f7, float:7.05E-43)
            if (r5 != r6) goto L_0x002e
            return r0
        L_0x002e:
            r5 = 2147483647(0x7fffffff, float:NaN)
            int r5 = r8.retryAfter(r9, r5)
            if (r5 != 0) goto L_0x003c
            okhttp3.Request r0 = r9.request()
            return r0
        L_0x003c:
            return r0
        L_0x003d:
            okhttp3.Request r4 = r9.request()
            okhttp3.RequestBody r4 = r4.body()
            if (r4 == 0) goto L_0x004e
            boolean r5 = r4.isOneShot()
            if (r5 == 0) goto L_0x004e
            return r0
        L_0x004e:
            if (r10 == 0) goto L_0x0063
            boolean r5 = r10.isCoalescedConnection$okhttp()
            if (r5 != 0) goto L_0x0057
            goto L_0x0063
        L_0x0057:
            okhttp3.internal.connection.RealConnection r0 = r10.getConnection$okhttp()
            r0.noCoalescedConnections$okhttp()
            okhttp3.Request r0 = r9.request()
            return r0
        L_0x0063:
            return r0
        L_0x0064:
            okhttp3.OkHttpClient r4 = r8.client
            boolean r4 = r4.retryOnConnectionFailure()
            if (r4 != 0) goto L_0x006d
            return r0
        L_0x006d:
            okhttp3.Request r4 = r9.request()
            okhttp3.RequestBody r4 = r4.body()
            if (r4 == 0) goto L_0x007e
            boolean r5 = r4.isOneShot()
            if (r5 == 0) goto L_0x007e
            return r0
        L_0x007e:
            okhttp3.Response r5 = r9.priorResponse()
            if (r5 == 0) goto L_0x008d
            int r6 = r5.code()
            r7 = 408(0x198, float:5.72E-43)
            if (r6 != r7) goto L_0x008d
            return r0
        L_0x008d:
            r6 = 0
            int r6 = r8.retryAfter(r9, r6)
            if (r6 <= 0) goto L_0x0095
            return r0
        L_0x0095:
            okhttp3.Request r0 = r9.request()
            return r0
        L_0x009a:
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)
            java.net.Proxy r0 = r1.proxy()
            java.net.Proxy$Type r4 = r0.type()
            java.net.Proxy$Type r5 = java.net.Proxy.Type.HTTP
            if (r4 != r5) goto L_0x00b4
            okhttp3.OkHttpClient r4 = r8.client
            okhttp3.Authenticator r4 = r4.proxyAuthenticator()
            okhttp3.Request r4 = r4.authenticate(r1, r9)
            return r4
        L_0x00b4:
            java.net.ProtocolException r4 = new java.net.ProtocolException
            java.lang.String r5 = "Received HTTP_PROXY_AUTH (407) code while not using proxy"
            r4.<init>(r5)
            java.lang.Throwable r4 = (java.lang.Throwable) r4
            throw r4
        L_0x00be:
            okhttp3.OkHttpClient r0 = r8.client
            okhttp3.Authenticator r0 = r0.authenticator()
            okhttp3.Request r0 = r0.authenticate(r1, r9)
            return r0
        L_0x00c9:
            okhttp3.Request r0 = r8.buildRedirectRequest(r9, r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http.RetryAndFollowUpInterceptor.followUpRequest(okhttp3.Response, okhttp3.internal.connection.Exchange):okhttp3.Request");
    }

    private final boolean isRecoverable(IOException e, boolean requestSendStarted) {
        if (e instanceof ProtocolException) {
            return false;
        }
        return e instanceof InterruptedIOException ? (e instanceof SocketTimeoutException) && !requestSendStarted : (!(e instanceof SSLHandshakeException) || !(e.getCause() instanceof CertificateException)) && !(e instanceof SSLPeerUnverifiedException);
    }

    private final boolean recover(IOException e, RealCall call, Request userRequest, boolean requestSendStarted) {
        if (!this.client.retryOnConnectionFailure()) {
            return false;
        }
        return (!requestSendStarted || !requestIsOneShot(e, userRequest)) && isRecoverable(e, requestSendStarted) && call.retryAfterFailure();
    }

    private final boolean requestIsOneShot(IOException e, Request userRequest) {
        RequestBody body = userRequest.body();
        return (body != null && body.isOneShot()) || (e instanceof FileNotFoundException);
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        boolean z;
        Interceptor.Chain chain2 = chain;
        Intrinsics.checkNotNullParameter(chain2, "chain");
        RealInterceptorChain realInterceptorChain = (RealInterceptorChain) chain2;
        Request request$okhttp = ((RealInterceptorChain) chain2).getRequest$okhttp();
        RealCall call$okhttp = realInterceptorChain.getCall$okhttp();
        List emptyList = CollectionsKt.emptyList();
        boolean z2 = true;
        Response response = null;
        int i = 0;
        Request request = request$okhttp;
        while (true) {
            call$okhttp.enterNetworkInterceptorExchange(request, z2);
            boolean z3 = true;
            try {
                if (!call$okhttp.isCanceled()) {
                    z = false;
                    Response proceed = realInterceptorChain.proceed(request);
                    z2 = true;
                    if (response != null) {
                        proceed = proceed.newBuilder().priorResponse(response.newBuilder().body((ResponseBody) null).build()).build();
                    }
                    Exchange interceptorScopedExchange$okhttp = call$okhttp.getInterceptorScopedExchange$okhttp();
                    Request followUpRequest = followUpRequest(proceed, interceptorScopedExchange$okhttp);
                    if (followUpRequest == null) {
                        if (interceptorScopedExchange$okhttp != null && interceptorScopedExchange$okhttp.isDuplex$okhttp()) {
                            call$okhttp.timeoutEarlyExit();
                        }
                        call$okhttp.exitNetworkInterceptorExchange$okhttp(false);
                        return proceed;
                    }
                    RequestBody body = followUpRequest.body();
                    if (body == null || !body.isOneShot()) {
                        ResponseBody body2 = proceed.body();
                        if (body2 != null) {
                            Util.closeQuietly((Closeable) body2);
                        }
                        i++;
                        if (i <= 20) {
                            request = followUpRequest;
                            response = proceed;
                        } else {
                            Exchange exchange = interceptorScopedExchange$okhttp;
                            throw new ProtocolException("Too many follow-up requests: " + i);
                        }
                    } else {
                        z3 = false;
                        return proceed;
                    }
                } else {
                    throw new IOException("Canceled");
                }
            } catch (RouteException e) {
                RouteException routeException = e;
                if (recover(routeException.getLastConnectException(), call$okhttp, request, false)) {
                    emptyList = CollectionsKt.plus(emptyList, routeException.getFirstConnectException());
                    z2 = false;
                } else {
                    throw Util.withSuppressed(routeException.getFirstConnectException(), emptyList);
                }
            } catch (IOException e2) {
                IOException iOException = e2;
                if (!(iOException instanceof ConnectionShutdownException)) {
                    z = true;
                }
                if (recover(iOException, call$okhttp, request, z)) {
                    emptyList = CollectionsKt.plus(emptyList, iOException);
                    z2 = false;
                } else {
                    throw Util.withSuppressed(iOException, emptyList);
                }
            } finally {
                call$okhttp.exitNetworkInterceptorExchange$okhttp(z3);
            }
        }
    }

    private final int retryAfter(Response userResponse, int defaultDelay) {
        String header$default = Response.header$default(userResponse, "Retry-After", (String) null, 2, (Object) null);
        Log1F380D.a((Object) header$default);
        if (header$default == null) {
            return defaultDelay;
        }
        if (!new Regex("\\d+").matches(header$default)) {
            return Integer.MAX_VALUE;
        }
        Integer valueOf = Integer.valueOf(header$default);
        Intrinsics.checkNotNullExpressionValue(valueOf, "Integer.valueOf(header)");
        return valueOf.intValue();
    }
}

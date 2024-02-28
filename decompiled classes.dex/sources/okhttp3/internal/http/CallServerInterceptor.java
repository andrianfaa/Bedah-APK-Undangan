package okhttp3.internal.http;

import kotlin.Metadata;
import okhttp3.Interceptor;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lokhttp3/internal/http/CallServerInterceptor;", "Lokhttp3/Interceptor;", "forWebSocket", "", "(Z)V", "intercept", "Lokhttp3/Response;", "chain", "Lokhttp3/Interceptor$Chain;", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: 01CF */
public final class CallServerInterceptor implements Interceptor {
    private final boolean forWebSocket;

    public CallServerInterceptor(boolean forWebSocket2) {
        this.forWebSocket = forWebSocket2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0168, code lost:
        if (kotlin.text.StringsKt.equals("close", r14, true) != false) goto L_0x016c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public okhttp3.Response intercept(okhttp3.Interceptor.Chain r19) throws java.io.IOException {
        /*
            r18 = this;
            r0 = r19
            java.lang.String r1 = "chain"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r0, r1)
            r1 = r0
            okhttp3.internal.http.RealInterceptorChain r1 = (okhttp3.internal.http.RealInterceptorChain) r1
            okhttp3.internal.connection.Exchange r2 = r1.getExchange$okhttp()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r2)
            okhttp3.Request r3 = r1.getRequest$okhttp()
            okhttp3.RequestBody r4 = r3.body()
            long r5 = java.lang.System.currentTimeMillis()
            r2.writeRequestHeaders(r3)
            r7 = 1
            r8 = 0
            r9 = r8
            okhttp3.Response$Builder r9 = (okhttp3.Response.Builder) r9
            java.lang.String r10 = r3.method()
            boolean r10 = okhttp3.internal.http.HttpMethod.permitsRequestBody(r10)
            r11 = 0
            r12 = 1
            if (r10 == 0) goto L_0x0083
            if (r4 == 0) goto L_0x0083
            java.lang.String r10 = "Expect"
            java.lang.String r10 = r3.header(r10)
            java.lang.String r13 = "100-continue"
            boolean r10 = kotlin.text.StringsKt.equals(r13, r10, r12)
            if (r10 == 0) goto L_0x004c
            r2.flushRequest()
            okhttp3.Response$Builder r9 = r2.readResponseHeaders(r12)
            r2.responseHeadersStart()
            r7 = 0
        L_0x004c:
            if (r9 != 0) goto L_0x0072
            boolean r10 = r4.isDuplex()
            if (r10 == 0) goto L_0x0063
            r2.flushRequest()
            okio.Sink r10 = r2.createRequestBody(r3, r12)
            okio.BufferedSink r10 = okio.Okio.buffer((okio.Sink) r10)
            r4.writeTo(r10)
            goto L_0x0086
        L_0x0063:
            okio.Sink r10 = r2.createRequestBody(r3, r11)
            okio.BufferedSink r10 = okio.Okio.buffer((okio.Sink) r10)
            r4.writeTo(r10)
            r10.close()
            goto L_0x0086
        L_0x0072:
            r2.noRequestBody()
            okhttp3.internal.connection.RealConnection r10 = r2.getConnection$okhttp()
            boolean r10 = r10.isMultiplexed$okhttp()
            if (r10 != 0) goto L_0x0082
            r2.noNewExchangesOnConnection()
        L_0x0082:
            goto L_0x0086
        L_0x0083:
            r2.noRequestBody()
        L_0x0086:
            if (r4 == 0) goto L_0x008f
            boolean r10 = r4.isDuplex()
            if (r10 != 0) goto L_0x0092
        L_0x008f:
            r2.finishRequest()
        L_0x0092:
            if (r9 != 0) goto L_0x00a2
            okhttp3.Response$Builder r10 = r2.readResponseHeaders(r11)
            kotlin.jvm.internal.Intrinsics.checkNotNull(r10)
            r9 = r10
            if (r7 == 0) goto L_0x00a2
            r2.responseHeadersStart()
            r7 = 0
        L_0x00a2:
            okhttp3.Response$Builder r10 = r9.request(r3)
            okhttp3.internal.connection.RealConnection r13 = r2.getConnection$okhttp()
            okhttp3.Handshake r13 = r13.handshake()
            okhttp3.Response$Builder r10 = r10.handshake(r13)
            okhttp3.Response$Builder r10 = r10.sentRequestAtMillis(r5)
            long r13 = java.lang.System.currentTimeMillis()
            okhttp3.Response$Builder r10 = r10.receivedResponseAtMillis(r13)
            okhttp3.Response r10 = r10.build()
            int r13 = r10.code()
            r14 = 100
            if (r13 != r14) goto L_0x0112
            okhttp3.Response$Builder r11 = r2.readResponseHeaders(r11)
            kotlin.jvm.internal.Intrinsics.checkNotNull(r11)
            r9 = r11
            if (r7 == 0) goto L_0x00e3
            r2.responseHeadersStart()
        L_0x00e3:
            okhttp3.Response$Builder r11 = r9.request(r3)
            okhttp3.internal.connection.RealConnection r14 = r2.getConnection$okhttp()
            okhttp3.Handshake r14 = r14.handshake()
            okhttp3.Response$Builder r11 = r11.handshake(r14)
            okhttp3.Response$Builder r11 = r11.sentRequestAtMillis(r5)
            long r14 = java.lang.System.currentTimeMillis()
            okhttp3.Response$Builder r11 = r11.receivedResponseAtMillis(r14)
            okhttp3.Response r10 = r11.build()
            int r13 = r10.code()
        L_0x0112:
            r2.responseHeadersEnd(r10)
            r11 = r18
            boolean r14 = r11.forWebSocket
            if (r14 == 0) goto L_0x0132
            r14 = 101(0x65, float:1.42E-43)
            if (r13 != r14) goto L_0x0132
            okhttp3.Response$Builder r14 = r10.newBuilder()
            okhttp3.ResponseBody r15 = okhttp3.internal.Util.EMPTY_RESPONSE
            okhttp3.Response$Builder r14 = r14.body(r15)
            okhttp3.Response r14 = r14.build()
            goto L_0x0146
        L_0x0132:
            okhttp3.Response$Builder r14 = r10.newBuilder()
            okhttp3.ResponseBody r15 = r2.openResponseBody(r10)
            okhttp3.Response$Builder r14 = r14.body(r15)
            okhttp3.Response r14 = r14.build()
        L_0x0146:
            r10 = r14
            okhttp3.Request r14 = r10.request()
            java.lang.String r15 = "Connection"
            java.lang.String r14 = r14.header(r15)
            java.lang.String r8 = "close"
            boolean r14 = kotlin.text.StringsKt.equals(r8, r14, r12)
            if (r14 != 0) goto L_0x016b
            r14 = 2
            r0 = 0
            java.lang.String r14 = okhttp3.Response.header$default(r10, r15, r0, r14, r0)
            mt.Log1F380D.a((java.lang.Object) r14)
            boolean r8 = kotlin.text.StringsKt.equals(r8, r14, r12)
            if (r8 == 0) goto L_0x016f
            goto L_0x016c
        L_0x016b:
            r0 = 0
        L_0x016c:
            r2.noNewExchangesOnConnection()
        L_0x016f:
            r8 = 204(0xcc, float:2.86E-43)
            if (r13 == r8) goto L_0x0177
            r8 = 205(0xcd, float:2.87E-43)
            if (r13 != r8) goto L_0x01bd
        L_0x0177:
            okhttp3.ResponseBody r8 = r10.body()
            if (r8 == 0) goto L_0x0182
            long r14 = r8.contentLength()
            goto L_0x0184
        L_0x0182:
            r14 = -1
        L_0x0184:
            r16 = 0
            int r8 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
            if (r8 <= 0) goto L_0x01bd
            java.net.ProtocolException r8 = new java.net.ProtocolException
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r14 = "HTTP "
            java.lang.StringBuilder r12 = r12.append(r14)
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.String r14 = " had non-zero Content-Length: "
            java.lang.StringBuilder r12 = r12.append(r14)
            okhttp3.ResponseBody r14 = r10.body()
            if (r14 == 0) goto L_0x01af
            long r14 = r14.contentLength()
            java.lang.Long r0 = java.lang.Long.valueOf(r14)
        L_0x01af:
            java.lang.StringBuilder r0 = r12.append(r0)
            java.lang.String r0 = r0.toString()
            r8.<init>(r0)
            java.lang.Throwable r8 = (java.lang.Throwable) r8
            throw r8
        L_0x01bd:
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http.CallServerInterceptor.intercept(okhttp3.Interceptor$Chain):okhttp3.Response");
    }
}

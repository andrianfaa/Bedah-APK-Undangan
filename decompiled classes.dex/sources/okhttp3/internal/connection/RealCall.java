package okhttp3.internal.connection;

import androidx.core.app.NotificationCompat;
import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import mt.Log1F380D;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CertificatePinner;
import okhttp3.Dispatcher;
import okhttp3.EventListener;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.http.BridgeInterceptor;
import okhttp3.internal.http.CallServerInterceptor;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import okhttp3.internal.platform.Platform;
import okio.AsyncTimeout;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000§\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006*\u0001.\u0018\u00002\u00020\u0001:\u0002deB\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u000e\u00101\u001a\u0002022\u0006\u0010\u0010\u001a\u00020\u000fJ!\u00103\u001a\u0002H4\"\n\b\u0000\u00104*\u0004\u0018\u0001052\u0006\u00106\u001a\u0002H4H\u0002¢\u0006\u0002\u00107J\b\u00108\u001a\u000202H\u0002J\b\u00109\u001a\u000202H\u0016J\b\u0010:\u001a\u00020\u0000H\u0016J\u0010\u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020>H\u0002J\u0010\u0010?\u001a\u0002022\u0006\u0010@\u001a\u00020AH\u0016J\u0016\u0010B\u001a\u0002022\u0006\u0010C\u001a\u00020\u00052\u0006\u0010D\u001a\u00020\u0007J\b\u0010E\u001a\u00020FH\u0016J\u0015\u0010G\u001a\u0002022\u0006\u0010H\u001a\u00020\u0007H\u0000¢\u0006\u0002\bIJ\r\u0010J\u001a\u00020FH\u0000¢\u0006\u0002\bKJ\u0015\u0010L\u001a\u00020\u001e2\u0006\u0010M\u001a\u00020NH\u0000¢\u0006\u0002\bOJ\b\u0010P\u001a\u00020\u0007H\u0016J\b\u0010Q\u001a\u00020\u0007H\u0016J;\u0010R\u001a\u0002H4\"\n\b\u0000\u00104*\u0004\u0018\u0001052\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010S\u001a\u00020\u00072\u0006\u0010T\u001a\u00020\u00072\u0006\u00106\u001a\u0002H4H\u0000¢\u0006\u0004\bU\u0010VJ\u0019\u0010W\u001a\u0004\u0018\u0001052\b\u00106\u001a\u0004\u0018\u000105H\u0000¢\u0006\u0002\bXJ\r\u0010Y\u001a\u00020ZH\u0000¢\u0006\u0002\b[J\u000f\u0010\\\u001a\u0004\u0018\u00010]H\u0000¢\u0006\u0002\b^J\b\u0010C\u001a\u00020\u0005H\u0016J\u0006\u0010_\u001a\u00020\u0007J\b\u0010-\u001a\u00020`H\u0016J\u0006\u00100\u001a\u000202J!\u0010a\u001a\u0002H4\"\n\b\u0000\u00104*\u0004\u0018\u0001052\u0006\u0010b\u001a\u0002H4H\u0002¢\u0006\u0002\u00107J\b\u0010c\u001a\u00020ZH\u0002R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\"\u0010\u0010\u001a\u0004\u0018\u00010\u000f2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u000fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0012\"\u0004\b\u0017\u0010\u0018R\u0014\u0010\u0019\u001a\u00020\u001aX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u001eX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001f\u001a\u0004\u0018\u00010 X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\"\u0010&\u001a\u0004\u0018\u00010\u001e2\b\u0010\u000e\u001a\u0004\u0018\u00010\u001e@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b'\u0010(R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u000e\u0010+\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010-\u001a\u00020.X\u0004¢\u0006\u0004\n\u0002\u0010/R\u000e\u00100\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000¨\u0006f"}, d2 = {"Lokhttp3/internal/connection/RealCall;", "Lokhttp3/Call;", "client", "Lokhttp3/OkHttpClient;", "originalRequest", "Lokhttp3/Request;", "forWebSocket", "", "(Lokhttp3/OkHttpClient;Lokhttp3/Request;Z)V", "callStackTrace", "", "canceled", "getClient", "()Lokhttp3/OkHttpClient;", "<set-?>", "Lokhttp3/internal/connection/RealConnection;", "connection", "getConnection", "()Lokhttp3/internal/connection/RealConnection;", "connectionPool", "Lokhttp3/internal/connection/RealConnectionPool;", "connectionToCancel", "getConnectionToCancel", "setConnectionToCancel", "(Lokhttp3/internal/connection/RealConnection;)V", "eventListener", "Lokhttp3/EventListener;", "getEventListener$okhttp", "()Lokhttp3/EventListener;", "exchange", "Lokhttp3/internal/connection/Exchange;", "exchangeFinder", "Lokhttp3/internal/connection/ExchangeFinder;", "executed", "Ljava/util/concurrent/atomic/AtomicBoolean;", "expectMoreExchanges", "getForWebSocket", "()Z", "interceptorScopedExchange", "getInterceptorScopedExchange$okhttp", "()Lokhttp3/internal/connection/Exchange;", "getOriginalRequest", "()Lokhttp3/Request;", "requestBodyOpen", "responseBodyOpen", "timeout", "okhttp3/internal/connection/RealCall$timeout$1", "Lokhttp3/internal/connection/RealCall$timeout$1;", "timeoutEarlyExit", "acquireConnectionNoEvents", "", "callDone", "E", "Ljava/io/IOException;", "e", "(Ljava/io/IOException;)Ljava/io/IOException;", "callStart", "cancel", "clone", "createAddress", "Lokhttp3/Address;", "url", "Lokhttp3/HttpUrl;", "enqueue", "responseCallback", "Lokhttp3/Callback;", "enterNetworkInterceptorExchange", "request", "newExchangeFinder", "execute", "Lokhttp3/Response;", "exitNetworkInterceptorExchange", "closeExchange", "exitNetworkInterceptorExchange$okhttp", "getResponseWithInterceptorChain", "getResponseWithInterceptorChain$okhttp", "initExchange", "chain", "Lokhttp3/internal/http/RealInterceptorChain;", "initExchange$okhttp", "isCanceled", "isExecuted", "messageDone", "requestDone", "responseDone", "messageDone$okhttp", "(Lokhttp3/internal/connection/Exchange;ZZLjava/io/IOException;)Ljava/io/IOException;", "noMoreExchanges", "noMoreExchanges$okhttp", "redactedUrl", "", "redactedUrl$okhttp", "releaseConnectionNoEvents", "Ljava/net/Socket;", "releaseConnectionNoEvents$okhttp", "retryAfterFailure", "Lokio/AsyncTimeout;", "timeoutExit", "cause", "toLoggableString", "AsyncCall", "CallReference", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: RealCall.kt */
public final class RealCall implements Call {
    private Object callStackTrace;
    private volatile boolean canceled;
    private final OkHttpClient client;
    private RealConnection connection;
    private final RealConnectionPool connectionPool;
    private volatile RealConnection connectionToCancel;
    private final EventListener eventListener;
    private volatile Exchange exchange;
    private ExchangeFinder exchangeFinder;
    private final AtomicBoolean executed = new AtomicBoolean();
    private boolean expectMoreExchanges = true;
    private final boolean forWebSocket;
    private Exchange interceptorScopedExchange;
    private final Request originalRequest;
    private boolean requestBodyOpen;
    private boolean responseBodyOpen;
    /* access modifiers changed from: private */
    public final RealCall$timeout$1 timeout;
    private boolean timeoutEarlyExit;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019J\u0012\u0010\u001a\u001a\u00020\u00172\n\u0010\u001b\u001a\u00060\u0000R\u00020\u0006J\b\u0010\u001c\u001a\u00020\u0017H\u0016R\u0011\u0010\u0005\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u001e\u0010\u000b\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\n@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u000f8F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0012\u001a\u00020\u00138F¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lokhttp3/internal/connection/RealCall$AsyncCall;", "Ljava/lang/Runnable;", "responseCallback", "Lokhttp3/Callback;", "(Lokhttp3/internal/connection/RealCall;Lokhttp3/Callback;)V", "call", "Lokhttp3/internal/connection/RealCall;", "getCall", "()Lokhttp3/internal/connection/RealCall;", "<set-?>", "Ljava/util/concurrent/atomic/AtomicInteger;", "callsPerHost", "getCallsPerHost", "()Ljava/util/concurrent/atomic/AtomicInteger;", "host", "", "getHost", "()Ljava/lang/String;", "request", "Lokhttp3/Request;", "getRequest", "()Lokhttp3/Request;", "executeOn", "", "executorService", "Ljava/util/concurrent/ExecutorService;", "reuseCallsPerHostFrom", "other", "run", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: 01CC */
    public final class AsyncCall implements Runnable {
        private volatile AtomicInteger callsPerHost = new AtomicInteger(0);
        private final Callback responseCallback;
        final /* synthetic */ RealCall this$0;

        public AsyncCall(RealCall this$02, Callback responseCallback2) {
            Intrinsics.checkNotNullParameter(responseCallback2, "responseCallback");
            this.this$0 = this$02;
            this.responseCallback = responseCallback2;
        }

        public final void executeOn(ExecutorService executorService) {
            Intrinsics.checkNotNullParameter(executorService, "executorService");
            Dispatcher dispatcher = this.this$0.getClient().dispatcher();
            if (!Util.assertionsEnabled || !Thread.holdsLock(dispatcher)) {
                try {
                    executorService.execute(this);
                } catch (RejectedExecutionException e) {
                    InterruptedIOException interruptedIOException = new InterruptedIOException("executor rejected");
                    interruptedIOException.initCause(e);
                    this.this$0.noMoreExchanges$okhttp(interruptedIOException);
                    this.responseCallback.onFailure(this.this$0, interruptedIOException);
                    this.this$0.getClient().dispatcher().finished$okhttp(this);
                } catch (Throwable th) {
                    this.this$0.getClient().dispatcher().finished$okhttp(this);
                    throw th;
                }
            } else {
                StringBuilder append = new StringBuilder().append("Thread ");
                Thread currentThread = Thread.currentThread();
                Intrinsics.checkNotNullExpressionValue(currentThread, "Thread.currentThread()");
                throw new AssertionError(append.append(currentThread.getName()).append(" MUST NOT hold lock on ").append(dispatcher).toString());
            }
        }

        public final RealCall getCall() {
            return this.this$0;
        }

        public final AtomicInteger getCallsPerHost() {
            return this.callsPerHost;
        }

        public final String getHost() {
            return this.this$0.getOriginalRequest().url().host();
        }

        public final Request getRequest() {
            return this.this$0.getOriginalRequest();
        }

        public final void reuseCallsPerHostFrom(AsyncCall other) {
            Intrinsics.checkNotNullParameter(other, "other");
            this.callsPerHost = other.callsPerHost;
        }

        public void run() {
            Dispatcher dispatcher;
            Thread currentThread = Thread.currentThread();
            Intrinsics.checkNotNullExpressionValue(currentThread, "currentThread");
            String name = currentThread.getName();
            currentThread.setName("OkHttp " + this.this$0.redactedUrl$okhttp());
            boolean z = false;
            try {
                this.this$0.timeout.enter();
                z = true;
                this.responseCallback.onResponse(this.this$0, this.this$0.getResponseWithInterceptorChain$okhttp());
                dispatcher = this.this$0.getClient().dispatcher();
            } catch (IOException e) {
                if (z) {
                    Platform platform = Platform.Companion.get();
                    StringBuilder append = new StringBuilder().append("Callback failure for ");
                    String access$toLoggableString = this.this$0.toLoggableString();
                    Log1F380D.a((Object) access$toLoggableString);
                    platform.log(append.append(access$toLoggableString).toString(), 4, e);
                } else {
                    this.responseCallback.onFailure(this.this$0, e);
                }
                dispatcher = this.this$0.getClient().dispatcher();
            } catch (Throwable th) {
                currentThread.setName(name);
                throw th;
            }
            dispatcher.finished$okhttp(this);
            currentThread.setName(name);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\b\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0017\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"Lokhttp3/internal/connection/RealCall$CallReference;", "Ljava/lang/ref/WeakReference;", "Lokhttp3/internal/connection/RealCall;", "referent", "callStackTrace", "", "(Lokhttp3/internal/connection/RealCall;Ljava/lang/Object;)V", "getCallStackTrace", "()Ljava/lang/Object;", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: RealCall.kt */
    public static final class CallReference extends WeakReference<RealCall> {
        private final Object callStackTrace;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public CallReference(RealCall referent, Object callStackTrace2) {
            super(referent);
            Intrinsics.checkNotNullParameter(referent, "referent");
            this.callStackTrace = callStackTrace2;
        }

        public final Object getCallStackTrace() {
            return this.callStackTrace;
        }
    }

    public RealCall(OkHttpClient client2, Request originalRequest2, boolean forWebSocket2) {
        Intrinsics.checkNotNullParameter(client2, "client");
        Intrinsics.checkNotNullParameter(originalRequest2, "originalRequest");
        this.client = client2;
        this.originalRequest = originalRequest2;
        this.forWebSocket = forWebSocket2;
        this.connectionPool = client2.connectionPool().getDelegate$okhttp();
        this.eventListener = client2.eventListenerFactory().create(this);
        RealCall$timeout$1 realCall$timeout$1 = new RealCall$timeout$1(this);
        realCall$timeout$1.timeout((long) client2.callTimeoutMillis(), TimeUnit.MILLISECONDS);
        Unit unit = Unit.INSTANCE;
        this.timeout = realCall$timeout$1;
    }

    private final <E extends IOException> E callDone(E e) {
        Socket releaseConnectionNoEvents$okhttp;
        if (!Util.assertionsEnabled || !Thread.holdsLock(this)) {
            RealConnection realConnection = this.connection;
            if (realConnection != null) {
                RealConnection realConnection2 = realConnection;
                if (!Util.assertionsEnabled || !Thread.holdsLock(realConnection2)) {
                    synchronized (realConnection) {
                        releaseConnectionNoEvents$okhttp = releaseConnectionNoEvents$okhttp();
                    }
                    Socket socket = releaseConnectionNoEvents$okhttp;
                    if (this.connection == null) {
                        if (socket != null) {
                            Util.closeQuietly(socket);
                        }
                        this.eventListener.connectionReleased(this, realConnection);
                    } else {
                        if (!(socket == null)) {
                            throw new IllegalStateException("Check failed.".toString());
                        }
                    }
                } else {
                    StringBuilder append = new StringBuilder().append("Thread ");
                    Thread currentThread = Thread.currentThread();
                    Intrinsics.checkNotNullExpressionValue(currentThread, "Thread.currentThread()");
                    throw new AssertionError(append.append(currentThread.getName()).append(" MUST NOT hold lock on ").append(realConnection2).toString());
                }
            }
            E timeoutExit = timeoutExit(e);
            if (e != null) {
                Intrinsics.checkNotNull(timeoutExit);
                this.eventListener.callFailed(this, timeoutExit);
            } else {
                this.eventListener.callEnd(this);
            }
            return timeoutExit;
        }
        StringBuilder append2 = new StringBuilder().append("Thread ");
        Thread currentThread2 = Thread.currentThread();
        Intrinsics.checkNotNullExpressionValue(currentThread2, "Thread.currentThread()");
        throw new AssertionError(append2.append(currentThread2.getName()).append(" MUST NOT hold lock on ").append(this).toString());
    }

    private final void callStart() {
        this.callStackTrace = Platform.Companion.get().getStackTraceForCloseable("response.body().close()");
        this.eventListener.callStart(this);
    }

    private final Address createAddress(HttpUrl url) {
        SSLSocketFactory sSLSocketFactory = null;
        HostnameVerifier hostnameVerifier = null;
        CertificatePinner certificatePinner = null;
        if (url.isHttps()) {
            sSLSocketFactory = this.client.sslSocketFactory();
            hostnameVerifier = this.client.hostnameVerifier();
            certificatePinner = this.client.certificatePinner();
        }
        return new Address(url.host(), url.port(), this.client.dns(), this.client.socketFactory(), sSLSocketFactory, hostnameVerifier, certificatePinner, this.client.proxyAuthenticator(), this.client.proxy(), this.client.protocols(), this.client.connectionSpecs(), this.client.proxySelector());
    }

    private final <E extends IOException> E timeoutExit(E cause) {
        if (this.timeoutEarlyExit || !this.timeout.exit()) {
            return cause;
        }
        E interruptedIOException = new InterruptedIOException("timeout");
        if (cause != null) {
            interruptedIOException.initCause((Throwable) cause);
        }
        return (IOException) interruptedIOException;
    }

    /* access modifiers changed from: private */
    public final String toLoggableString() {
        return (isCanceled() ? "canceled " : HttpUrl.FRAGMENT_ENCODE_SET) + (this.forWebSocket ? "web socket" : NotificationCompat.CATEGORY_CALL) + " to " + redactedUrl$okhttp();
    }

    public final void acquireConnectionNoEvents(RealConnection connection2) {
        Intrinsics.checkNotNullParameter(connection2, "connection");
        RealConnection realConnection = connection2;
        if (!Util.assertionsEnabled || Thread.holdsLock(realConnection)) {
            if (this.connection == null) {
                this.connection = connection2;
                connection2.getCalls().add(new CallReference(this, this.callStackTrace));
                return;
            }
            throw new IllegalStateException("Check failed.".toString());
        }
        StringBuilder append = new StringBuilder().append("Thread ");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkNotNullExpressionValue(currentThread, "Thread.currentThread()");
        throw new AssertionError(append.append(currentThread.getName()).append(" MUST hold lock on ").append(realConnection).toString());
    }

    public void cancel() {
        if (!this.canceled) {
            this.canceled = true;
            Exchange exchange2 = this.exchange;
            if (exchange2 != null) {
                exchange2.cancel();
            }
            RealConnection realConnection = this.connectionToCancel;
            if (realConnection != null) {
                realConnection.cancel();
            }
            this.eventListener.canceled(this);
        }
    }

    public RealCall clone() {
        return new RealCall(this.client, this.originalRequest, this.forWebSocket);
    }

    public void enqueue(Callback responseCallback) {
        Intrinsics.checkNotNullParameter(responseCallback, "responseCallback");
        if (this.executed.compareAndSet(false, true)) {
            callStart();
            this.client.dispatcher().enqueue$okhttp(new AsyncCall(this, responseCallback));
            return;
        }
        throw new IllegalStateException("Already Executed".toString());
    }

    public final void enterNetworkInterceptorExchange(Request request, boolean newExchangeFinder) {
        Intrinsics.checkNotNullParameter(request, "request");
        if (this.interceptorScopedExchange == null) {
            synchronized (this) {
                if (!(!this.responseBodyOpen)) {
                    throw new IllegalStateException("cannot make a new request because the previous response is still open: please call response.close()".toString());
                } else if (true ^ this.requestBodyOpen) {
                    Unit unit = Unit.INSTANCE;
                } else {
                    throw new IllegalStateException("Check failed.".toString());
                }
            }
            if (newExchangeFinder) {
                this.exchangeFinder = new ExchangeFinder(this.connectionPool, createAddress(request.url()), this, this.eventListener);
                return;
            }
            return;
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    public Response execute() {
        if (this.executed.compareAndSet(false, true)) {
            this.timeout.enter();
            callStart();
            try {
                this.client.dispatcher().executed$okhttp(this);
                return getResponseWithInterceptorChain$okhttp();
            } finally {
                this.client.dispatcher().finished$okhttp(this);
            }
        } else {
            throw new IllegalStateException("Already Executed".toString());
        }
    }

    public final void exitNetworkInterceptorExchange$okhttp(boolean closeExchange) {
        Exchange exchange2;
        synchronized (this) {
            if (this.expectMoreExchanges) {
                Unit unit = Unit.INSTANCE;
            } else {
                throw new IllegalStateException("released".toString());
            }
        }
        if (closeExchange && (exchange2 = this.exchange) != null) {
            exchange2.detachWithViolence();
        }
        Exchange exchange3 = null;
        this.interceptorScopedExchange = null;
    }

    public final OkHttpClient getClient() {
        return this.client;
    }

    public final RealConnection getConnection() {
        return this.connection;
    }

    public final RealConnection getConnectionToCancel() {
        return this.connectionToCancel;
    }

    public final EventListener getEventListener$okhttp() {
        return this.eventListener;
    }

    public final boolean getForWebSocket() {
        return this.forWebSocket;
    }

    public final Exchange getInterceptorScopedExchange$okhttp() {
        return this.interceptorScopedExchange;
    }

    public final Request getOriginalRequest() {
        return this.originalRequest;
    }

    public final Response getResponseWithInterceptorChain$okhttp() throws IOException {
        List arrayList = new ArrayList();
        CollectionsKt.addAll(arrayList, this.client.interceptors());
        arrayList.add(new RetryAndFollowUpInterceptor(this.client));
        arrayList.add(new BridgeInterceptor(this.client.cookieJar()));
        arrayList.add(new CacheInterceptor(this.client.cache()));
        arrayList.add(ConnectInterceptor.INSTANCE);
        if (!this.forWebSocket) {
            CollectionsKt.addAll(arrayList, this.client.networkInterceptors());
        }
        arrayList.add(new CallServerInterceptor(this.forWebSocket));
        try {
            Response proceed = new RealInterceptorChain(this, arrayList, 0, (Exchange) null, this.originalRequest, this.client.connectTimeoutMillis(), this.client.readTimeoutMillis(), this.client.writeTimeoutMillis()).proceed(this.originalRequest);
            if (!isCanceled()) {
                noMoreExchanges$okhttp((IOException) null);
                return proceed;
            }
            Util.closeQuietly((Closeable) proceed);
            throw new IOException("Canceled");
        } catch (IOException e) {
            IOException noMoreExchanges$okhttp = noMoreExchanges$okhttp(e);
            throw (noMoreExchanges$okhttp == null ? new NullPointerException("null cannot be cast to non-null type kotlin.Throwable") : noMoreExchanges$okhttp);
        } catch (Throwable th) {
            if (1 == 0) {
                noMoreExchanges$okhttp((IOException) null);
            }
            throw th;
        }
    }

    public final Exchange initExchange$okhttp(RealInterceptorChain chain) {
        Intrinsics.checkNotNullParameter(chain, "chain");
        synchronized (this) {
            if (!this.expectMoreExchanges) {
                throw new IllegalStateException("released".toString());
            } else if (!(!this.responseBodyOpen)) {
                throw new IllegalStateException("Check failed.".toString());
            } else if (!this.requestBodyOpen) {
                Unit unit = Unit.INSTANCE;
            } else {
                throw new IllegalStateException("Check failed.".toString());
            }
        }
        ExchangeFinder exchangeFinder2 = this.exchangeFinder;
        Intrinsics.checkNotNull(exchangeFinder2);
        Exchange exchange2 = new Exchange(this, this.eventListener, exchangeFinder2, exchangeFinder2.find(this.client, chain));
        this.interceptorScopedExchange = exchange2;
        this.exchange = exchange2;
        synchronized (this) {
            this.requestBodyOpen = true;
            this.responseBodyOpen = true;
            Unit unit2 = Unit.INSTANCE;
        }
        if (!this.canceled) {
            return exchange2;
        }
        throw new IOException("Canceled");
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public boolean isExecuted() {
        return this.executed.get();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0021, code lost:
        if (r7.responseBodyOpen != false) goto L_0x0023;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <E extends java.io.IOException> E messageDone$okhttp(okhttp3.internal.connection.Exchange r8, boolean r9, boolean r10, E r11) {
        /*
            r7 = this;
            java.lang.String r0 = "exchange"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r8, r0)
            okhttp3.internal.connection.Exchange r0 = r7.exchange
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r8, (java.lang.Object) r0)
            r1 = 1
            r0 = r0 ^ r1
            if (r0 == 0) goto L_0x0010
            return r11
        L_0x0010:
            r0 = 0
            r2 = 0
            monitor-enter(r7)
            r3 = 0
            if (r9 == 0) goto L_0x001d
            boolean r4 = r7.requestBodyOpen     // Catch:{ all -> 0x001b }
            if (r4 != 0) goto L_0x0023
            goto L_0x001d
        L_0x001b:
            r1 = move-exception
            goto L_0x005f
        L_0x001d:
            if (r10 == 0) goto L_0x0045
            boolean r4 = r7.responseBodyOpen     // Catch:{ all -> 0x001b }
            if (r4 == 0) goto L_0x0045
        L_0x0023:
            r4 = 0
            if (r9 == 0) goto L_0x0028
            r7.requestBodyOpen = r4     // Catch:{ all -> 0x001b }
        L_0x0028:
            if (r10 == 0) goto L_0x002c
            r7.responseBodyOpen = r4     // Catch:{ all -> 0x001b }
        L_0x002c:
            boolean r5 = r7.requestBodyOpen     // Catch:{ all -> 0x001b }
            if (r5 != 0) goto L_0x0036
            boolean r6 = r7.responseBodyOpen     // Catch:{ all -> 0x001b }
            if (r6 != 0) goto L_0x0036
            r6 = r1
            goto L_0x0037
        L_0x0036:
            r6 = r4
        L_0x0037:
            r0 = r6
            if (r5 != 0) goto L_0x0043
            boolean r5 = r7.responseBodyOpen     // Catch:{ all -> 0x001b }
            if (r5 != 0) goto L_0x0043
            boolean r5 = r7.expectMoreExchanges     // Catch:{ all -> 0x001b }
            if (r5 != 0) goto L_0x0043
            goto L_0x0044
        L_0x0043:
            r1 = r4
        L_0x0044:
            r2 = r1
        L_0x0045:
            kotlin.Unit r1 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x001b }
            monitor-exit(r7)
            if (r0 == 0) goto L_0x0057
            r1 = 0
            r3 = r1
            okhttp3.internal.connection.Exchange r3 = (okhttp3.internal.connection.Exchange) r3
            r7.exchange = r1
            okhttp3.internal.connection.RealConnection r1 = r7.connection
            if (r1 == 0) goto L_0x0057
            r1.incrementSuccessCount$okhttp()
        L_0x0057:
            if (r2 == 0) goto L_0x005e
            java.io.IOException r1 = r7.callDone(r11)
            return r1
        L_0x005e:
            return r11
        L_0x005f:
            monitor-exit(r7)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.connection.RealCall.messageDone$okhttp(okhttp3.internal.connection.Exchange, boolean, boolean, java.io.IOException):java.io.IOException");
    }

    public final IOException noMoreExchanges$okhttp(IOException e) {
        boolean z = false;
        synchronized (this) {
            if (this.expectMoreExchanges) {
                boolean z2 = false;
                this.expectMoreExchanges = false;
                if (!this.requestBodyOpen && !this.responseBodyOpen) {
                    z2 = true;
                }
                z = z2;
            }
            Unit unit = Unit.INSTANCE;
        }
        return z ? callDone(e) : e;
    }

    public final String redactedUrl$okhttp() {
        return this.originalRequest.url().redact();
    }

    public final Socket releaseConnectionNoEvents$okhttp() {
        RealConnection realConnection = this.connection;
        Intrinsics.checkNotNull(realConnection);
        RealConnection realConnection2 = realConnection;
        if (!Util.assertionsEnabled || Thread.holdsLock(realConnection2)) {
            List<Reference<RealCall>> calls = realConnection.getCalls();
            int i = 0;
            Iterator<Reference<RealCall>> it = calls.iterator();
            while (true) {
                if (!it.hasNext()) {
                    i = -1;
                    break;
                } else if (Intrinsics.areEqual((Object) (RealCall) it.next().get(), (Object) this)) {
                    break;
                } else {
                    i++;
                }
            }
            int i2 = i;
            if (i2 != -1) {
                calls.remove(i2);
                RealConnection realConnection3 = null;
                this.connection = null;
                if (calls.isEmpty()) {
                    realConnection.setIdleAtNs$okhttp(System.nanoTime());
                    if (this.connectionPool.connectionBecameIdle(realConnection)) {
                        return realConnection.socket();
                    }
                }
                return null;
            }
            throw new IllegalStateException("Check failed.".toString());
        }
        StringBuilder append = new StringBuilder().append("Thread ");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkNotNullExpressionValue(currentThread, "Thread.currentThread()");
        throw new AssertionError(append.append(currentThread.getName()).append(" MUST hold lock on ").append(realConnection2).toString());
    }

    public Request request() {
        return this.originalRequest;
    }

    public final boolean retryAfterFailure() {
        ExchangeFinder exchangeFinder2 = this.exchangeFinder;
        Intrinsics.checkNotNull(exchangeFinder2);
        return exchangeFinder2.retryAfterFailure();
    }

    public final void setConnectionToCancel(RealConnection realConnection) {
        this.connectionToCancel = realConnection;
    }

    public AsyncTimeout timeout() {
        return this.timeout;
    }

    public final void timeoutEarlyExit() {
        if (!this.timeoutEarlyExit) {
            this.timeoutEarlyExit = true;
            this.timeout.exit();
            return;
        }
        throw new IllegalStateException("Check failed.".toString());
    }
}

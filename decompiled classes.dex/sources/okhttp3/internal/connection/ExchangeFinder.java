package okhttp3.internal.connection;

import androidx.core.app.NotificationCompat;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Address;
import okhttp3.EventListener;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RouteSelector;
import okhttp3.internal.http.ExchangeCodec;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http2.ConnectionShutdownException;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.StreamResetException;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0016\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cJ0\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u000e2\u0006\u0010 \u001a\u00020\u000e2\u0006\u0010!\u001a\u00020\u000e2\u0006\u0010\"\u001a\u00020\u000e2\u0006\u0010#\u001a\u00020$H\u0002J8\u0010%\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u000e2\u0006\u0010 \u001a\u00020\u000e2\u0006\u0010!\u001a\u00020\u000e2\u0006\u0010\"\u001a\u00020\u000e2\u0006\u0010#\u001a\u00020$2\u0006\u0010&\u001a\u00020$H\u0002J\u0006\u0010'\u001a\u00020$J\n\u0010(\u001a\u0004\u0018\u00010\u0010H\u0002J\u000e\u0010)\u001a\u00020$2\u0006\u0010*\u001a\u00020+J\u000e\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020/R\u0014\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u000e¢\u0006\u0002\n\u0000¨\u00060"}, d2 = {"Lokhttp3/internal/connection/ExchangeFinder;", "", "connectionPool", "Lokhttp3/internal/connection/RealConnectionPool;", "address", "Lokhttp3/Address;", "call", "Lokhttp3/internal/connection/RealCall;", "eventListener", "Lokhttp3/EventListener;", "(Lokhttp3/internal/connection/RealConnectionPool;Lokhttp3/Address;Lokhttp3/internal/connection/RealCall;Lokhttp3/EventListener;)V", "getAddress$okhttp", "()Lokhttp3/Address;", "connectionShutdownCount", "", "nextRouteToTry", "Lokhttp3/Route;", "otherFailureCount", "refusedStreamCount", "routeSelection", "Lokhttp3/internal/connection/RouteSelector$Selection;", "routeSelector", "Lokhttp3/internal/connection/RouteSelector;", "find", "Lokhttp3/internal/http/ExchangeCodec;", "client", "Lokhttp3/OkHttpClient;", "chain", "Lokhttp3/internal/http/RealInterceptorChain;", "findConnection", "Lokhttp3/internal/connection/RealConnection;", "connectTimeout", "readTimeout", "writeTimeout", "pingIntervalMillis", "connectionRetryEnabled", "", "findHealthyConnection", "doExtensiveHealthChecks", "retryAfterFailure", "retryRoute", "sameHostAndPort", "url", "Lokhttp3/HttpUrl;", "trackFailure", "", "e", "Ljava/io/IOException;", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: ExchangeFinder.kt */
public final class ExchangeFinder {
    private final Address address;
    private final RealCall call;
    private final RealConnectionPool connectionPool;
    private int connectionShutdownCount;
    private final EventListener eventListener;
    private Route nextRouteToTry;
    private int otherFailureCount;
    private int refusedStreamCount;
    private RouteSelector.Selection routeSelection;
    private RouteSelector routeSelector;

    public ExchangeFinder(RealConnectionPool connectionPool2, Address address2, RealCall call2, EventListener eventListener2) {
        Intrinsics.checkNotNullParameter(connectionPool2, "connectionPool");
        Intrinsics.checkNotNullParameter(address2, "address");
        Intrinsics.checkNotNullParameter(call2, NotificationCompat.CATEGORY_CALL);
        Intrinsics.checkNotNullParameter(eventListener2, "eventListener");
        this.connectionPool = connectionPool2;
        this.address = address2;
        this.call = call2;
        this.eventListener = eventListener2;
    }

    /* JADX INFO: finally extract failed */
    private final RealConnection findConnection(int connectTimeout, int readTimeout, int writeTimeout, int pingIntervalMillis, boolean connectionRetryEnabled) throws IOException {
        Route route;
        List<Route> list;
        if (!this.call.isCanceled()) {
            RealConnection connection = this.call.getConnection();
            boolean z = true;
            if (connection != null) {
                Socket socket = null;
                synchronized (connection) {
                    if (connection.getNoNewExchanges() || !sameHostAndPort(connection.route().address().url())) {
                        socket = this.call.releaseConnectionNoEvents$okhttp();
                    }
                    Unit unit = Unit.INSTANCE;
                }
                if (this.call.getConnection() != null) {
                    if (socket != null) {
                        z = false;
                    }
                    if (z) {
                        return connection;
                    }
                    throw new IllegalStateException("Check failed.".toString());
                }
                if (socket != null) {
                    Util.closeQuietly(socket);
                }
                this.eventListener.connectionReleased(this.call, connection);
            }
            this.refusedStreamCount = 0;
            this.connectionShutdownCount = 0;
            this.otherFailureCount = 0;
            if (this.connectionPool.callAcquirePooledConnection(this.address, this.call, (List<Route>) null, false)) {
                RealConnection connection2 = this.call.getConnection();
                Intrinsics.checkNotNull(connection2);
                this.eventListener.connectionAcquired(this.call, connection2);
                return connection2;
            }
            Route route2 = this.nextRouteToTry;
            if (route2 != null) {
                list = null;
                Intrinsics.checkNotNull(route2);
                route = route2;
                Route route3 = null;
                this.nextRouteToTry = null;
            } else {
                RouteSelector.Selection selection = this.routeSelection;
                if (selection != null) {
                    Intrinsics.checkNotNull(selection);
                    if (selection.hasNext()) {
                        list = null;
                        RouteSelector.Selection selection2 = this.routeSelection;
                        Intrinsics.checkNotNull(selection2);
                        route = selection2.next();
                    }
                }
                RouteSelector routeSelector2 = this.routeSelector;
                if (routeSelector2 == null) {
                    routeSelector2 = new RouteSelector(this.address, this.call.getClient().getRouteDatabase(), this.call, this.eventListener);
                    this.routeSelector = routeSelector2;
                }
                RouteSelector.Selection next = routeSelector2.next();
                this.routeSelection = next;
                List<Route> routes = next.getRoutes();
                if (this.call.isCanceled()) {
                    throw new IOException("Canceled");
                } else if (this.connectionPool.callAcquirePooledConnection(this.address, this.call, routes, false)) {
                    RealConnection connection3 = this.call.getConnection();
                    Intrinsics.checkNotNull(connection3);
                    this.eventListener.connectionAcquired(this.call, connection3);
                    return connection3;
                } else {
                    List<Route> list2 = routes;
                    route = next.next();
                    list = list2;
                }
            }
            RealConnection realConnection = new RealConnection(this.connectionPool, route);
            this.call.setConnectionToCancel(realConnection);
            try {
                realConnection.connect(connectTimeout, readTimeout, writeTimeout, pingIntervalMillis, connectionRetryEnabled, this.call, this.eventListener);
                RealConnection realConnection2 = null;
                this.call.setConnectionToCancel((RealConnection) null);
                this.call.getClient().getRouteDatabase().connected(realConnection.route());
                if (this.connectionPool.callAcquirePooledConnection(this.address, this.call, list, true)) {
                    RealConnection connection4 = this.call.getConnection();
                    Intrinsics.checkNotNull(connection4);
                    this.nextRouteToTry = route;
                    Util.closeQuietly(realConnection.socket());
                    this.eventListener.connectionAcquired(this.call, connection4);
                    return connection4;
                }
                synchronized (realConnection) {
                    this.connectionPool.put(realConnection);
                    this.call.acquireConnectionNoEvents(realConnection);
                    Unit unit2 = Unit.INSTANCE;
                }
                this.eventListener.connectionAcquired(this.call, realConnection);
                return realConnection;
            } catch (Throwable th) {
                RealConnection realConnection3 = null;
                this.call.setConnectionToCancel((RealConnection) null);
                throw th;
            }
        } else {
            throw new IOException("Canceled");
        }
    }

    private final RealConnection findHealthyConnection(int connectTimeout, int readTimeout, int writeTimeout, int pingIntervalMillis, boolean connectionRetryEnabled, boolean doExtensiveHealthChecks) throws IOException {
        while (true) {
            RealConnection findConnection = findConnection(connectTimeout, readTimeout, writeTimeout, pingIntervalMillis, connectionRetryEnabled);
            if (findConnection.isHealthy(doExtensiveHealthChecks)) {
                return findConnection;
            }
            findConnection.noNewExchanges$okhttp();
            if (this.nextRouteToTry == null) {
                RouteSelector.Selection selection = this.routeSelection;
                boolean z = true;
                if (!(selection != null ? selection.hasNext() : true)) {
                    RouteSelector routeSelector2 = this.routeSelector;
                    if (routeSelector2 != null) {
                        z = routeSelector2.hasNext();
                    }
                    if (!z) {
                        throw new IOException("exhausted all routes");
                    }
                } else {
                    continue;
                }
            }
        }
    }

    private final Route retryRoute() {
        RealConnection connection;
        if (this.refusedStreamCount > 1 || this.connectionShutdownCount > 1 || this.otherFailureCount > 0 || (connection = this.call.getConnection()) == null) {
            return null;
        }
        synchronized (connection) {
            if (connection.getRouteFailureCount$okhttp() != 0) {
                return null;
            }
            if (!Util.canReuseConnectionFor(connection.route().address().url(), this.address.url())) {
                return null;
            }
            Route route = connection.route();
            return route;
        }
    }

    public final ExchangeCodec find(OkHttpClient client, RealInterceptorChain chain) {
        Intrinsics.checkNotNullParameter(client, "client");
        Intrinsics.checkNotNullParameter(chain, "chain");
        try {
            return findHealthyConnection(chain.getConnectTimeoutMillis$okhttp(), chain.getReadTimeoutMillis$okhttp(), chain.getWriteTimeoutMillis$okhttp(), client.pingIntervalMillis(), client.retryOnConnectionFailure(), !Intrinsics.areEqual((Object) chain.getRequest$okhttp().method(), (Object) "GET")).newCodec$okhttp(client, chain);
        } catch (RouteException e) {
            trackFailure(e.getLastConnectException());
            throw e;
        } catch (IOException e2) {
            trackFailure(e2);
            throw new RouteException(e2);
        }
    }

    public final Address getAddress$okhttp() {
        return this.address;
    }

    public final boolean retryAfterFailure() {
        RouteSelector routeSelector2;
        if (this.refusedStreamCount == 0 && this.connectionShutdownCount == 0 && this.otherFailureCount == 0) {
            return false;
        }
        if (this.nextRouteToTry != null) {
            return true;
        }
        Route retryRoute = retryRoute();
        if (retryRoute != null) {
            this.nextRouteToTry = retryRoute;
            return true;
        }
        RouteSelector.Selection selection = this.routeSelection;
        if ((selection == null || !selection.hasNext()) && (routeSelector2 = this.routeSelector) != null) {
            return routeSelector2.hasNext();
        }
        return true;
    }

    public final boolean sameHostAndPort(HttpUrl url) {
        Intrinsics.checkNotNullParameter(url, "url");
        HttpUrl url2 = this.address.url();
        return url.port() == url2.port() && Intrinsics.areEqual((Object) url.host(), (Object) url2.host());
    }

    public final void trackFailure(IOException e) {
        Intrinsics.checkNotNullParameter(e, "e");
        Route route = null;
        this.nextRouteToTry = null;
        if ((e instanceof StreamResetException) && ((StreamResetException) e).errorCode == ErrorCode.REFUSED_STREAM) {
            this.refusedStreamCount++;
        } else if (e instanceof ConnectionShutdownException) {
            this.connectionShutdownCount++;
        } else {
            this.otherFailureCount++;
        }
    }
}

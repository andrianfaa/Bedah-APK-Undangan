package okhttp3.internal.connection;

import androidx.core.app.NotificationCompat;
import java.lang.ref.Reference;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Address;
import okhttp3.ConnectionPool;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.concurrent.TaskQueue;
import okhttp3.internal.concurrent.TaskRunner;
import okhttp3.internal.connection.RealCall;
import okhttp3.internal.platform.Platform;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0005*\u0001\u000e\u0018\u0000 (2\u00020\u0001:\u0001(B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ.\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u000e\u0010\u001a\u001a\n\u0012\u0004\u0012\u00020\u001c\u0018\u00010\u001b2\u0006\u0010\u001d\u001a\u00020\u0015J\u000e\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020\u0007J\u000e\u0010 \u001a\u00020\u00152\u0006\u0010!\u001a\u00020\u0012J\u0006\u0010\"\u001a\u00020\u0005J\u0006\u0010#\u001a\u00020$J\u0006\u0010%\u001a\u00020\u0005J\u0018\u0010&\u001a\u00020\u00052\u0006\u0010!\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020\u0007H\u0002J\u000e\u0010'\u001a\u00020$2\u0006\u0010!\u001a\u00020\u0012R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0004\n\u0002\u0010\u000fR\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006)"}, d2 = {"Lokhttp3/internal/connection/RealConnectionPool;", "", "taskRunner", "Lokhttp3/internal/concurrent/TaskRunner;", "maxIdleConnections", "", "keepAliveDuration", "", "timeUnit", "Ljava/util/concurrent/TimeUnit;", "(Lokhttp3/internal/concurrent/TaskRunner;IJLjava/util/concurrent/TimeUnit;)V", "cleanupQueue", "Lokhttp3/internal/concurrent/TaskQueue;", "cleanupTask", "okhttp3/internal/connection/RealConnectionPool$cleanupTask$1", "Lokhttp3/internal/connection/RealConnectionPool$cleanupTask$1;", "connections", "Ljava/util/concurrent/ConcurrentLinkedQueue;", "Lokhttp3/internal/connection/RealConnection;", "keepAliveDurationNs", "callAcquirePooledConnection", "", "address", "Lokhttp3/Address;", "call", "Lokhttp3/internal/connection/RealCall;", "routes", "", "Lokhttp3/Route;", "requireMultiplexed", "cleanup", "now", "connectionBecameIdle", "connection", "connectionCount", "evictAll", "", "idleConnectionCount", "pruneAndGetAllocationCount", "put", "Companion", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: RealConnectionPool.kt */
public final class RealConnectionPool {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private final TaskQueue cleanupQueue;
    private final RealConnectionPool$cleanupTask$1 cleanupTask = new RealConnectionPool$cleanupTask$1(this, Util.okHttpName + " ConnectionPool");
    private final ConcurrentLinkedQueue<RealConnection> connections = new ConcurrentLinkedQueue<>();
    private final long keepAliveDurationNs;
    private final int maxIdleConnections;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006¨\u0006\u0007"}, d2 = {"Lokhttp3/internal/connection/RealConnectionPool$Companion;", "", "()V", "get", "Lokhttp3/internal/connection/RealConnectionPool;", "connectionPool", "Lokhttp3/ConnectionPool;", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: RealConnectionPool.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        public final RealConnectionPool get(ConnectionPool connectionPool) {
            Intrinsics.checkNotNullParameter(connectionPool, "connectionPool");
            return connectionPool.getDelegate$okhttp();
        }
    }

    public RealConnectionPool(TaskRunner taskRunner, int maxIdleConnections2, long keepAliveDuration, TimeUnit timeUnit) {
        Intrinsics.checkNotNullParameter(taskRunner, "taskRunner");
        Intrinsics.checkNotNullParameter(timeUnit, "timeUnit");
        this.maxIdleConnections = maxIdleConnections2;
        this.keepAliveDurationNs = timeUnit.toNanos(keepAliveDuration);
        this.cleanupQueue = taskRunner.newQueue();
        if (!(keepAliveDuration > 0)) {
            throw new IllegalArgumentException(("keepAliveDuration <= 0: " + keepAliveDuration).toString());
        }
    }

    private final int pruneAndGetAllocationCount(RealConnection connection, long now) {
        RealConnection realConnection = connection;
        if (!Util.assertionsEnabled || Thread.holdsLock(realConnection)) {
            List<Reference<RealCall>> calls = connection.getCalls();
            int i = 0;
            while (i < calls.size()) {
                Reference reference = calls.get(i);
                if (reference.get() != null) {
                    i++;
                } else if (reference != null) {
                    Platform.Companion.get().logCloseableLeak("A connection to " + connection.route().address().url() + " was leaked. " + "Did you forget to close a response body?", ((RealCall.CallReference) reference).getCallStackTrace());
                    calls.remove(i);
                    connection.setNoNewExchanges(true);
                    if (calls.isEmpty()) {
                        connection.setIdleAtNs$okhttp(now - this.keepAliveDurationNs);
                        return 0;
                    }
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type okhttp3.internal.connection.RealCall.CallReference");
                }
            }
            return calls.size();
        }
        StringBuilder append = new StringBuilder().append("Thread ");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkNotNullExpressionValue(currentThread, "Thread.currentThread()");
        throw new AssertionError(append.append(currentThread.getName()).append(" MUST hold lock on ").append(realConnection).toString());
    }

    public final boolean callAcquirePooledConnection(Address address, RealCall call, List<Route> routes, boolean requireMultiplexed) {
        Intrinsics.checkNotNullParameter(address, "address");
        Intrinsics.checkNotNullParameter(call, NotificationCompat.CATEGORY_CALL);
        Iterator<RealConnection> it = this.connections.iterator();
        while (it.hasNext()) {
            RealConnection next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "connection");
            synchronized (next) {
                if (requireMultiplexed) {
                    if (!next.isMultiplexed$okhttp()) {
                        Unit unit = Unit.INSTANCE;
                    }
                }
                if (next.isEligible$okhttp(address, routes)) {
                    call.acquireConnectionNoEvents(next);
                    return true;
                }
                Unit unit2 = Unit.INSTANCE;
            }
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:53:0x008e, code lost:
        okhttp3.internal.Util.closeQuietly(r5.socket());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x009b, code lost:
        if (r13.connections.isEmpty() == false) goto L_0x00a2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x009d, code lost:
        r13.cleanupQueue.cancelAll();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00a2, code lost:
        return 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final long cleanup(long r14) {
        /*
            r13 = this;
            r0 = 0
            r1 = 0
            r2 = 0
            okhttp3.internal.connection.RealConnection r2 = (okhttp3.internal.connection.RealConnection) r2
            r3 = -9223372036854775808
            java.util.concurrent.ConcurrentLinkedQueue<okhttp3.internal.connection.RealConnection> r5 = r13.connections
            java.util.Iterator r5 = r5.iterator()
        L_0x000d:
            boolean r6 = r5.hasNext()
            if (r6 == 0) goto L_0x004b
            java.lang.Object r6 = r5.next()
            okhttp3.internal.connection.RealConnection r6 = (okhttp3.internal.connection.RealConnection) r6
            java.lang.String r7 = "connection"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r6, r7)
            monitor-enter(r6)
            r7 = 0
            int r8 = r13.pruneAndGetAllocationCount(r6, r14)     // Catch:{ all -> 0x0046 }
            if (r8 <= 0) goto L_0x002f
            int r8 = r0 + 1
            java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x002d }
            r0 = r8
            goto L_0x0044
        L_0x002d:
            r0 = move-exception
            goto L_0x0049
        L_0x002f:
            int r1 = r1 + 1
            long r8 = r6.getIdleAtNs$okhttp()     // Catch:{ all -> 0x0046 }
            long r8 = r14 - r8
            int r10 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
            if (r10 <= 0) goto L_0x0040
            r3 = r8
            r2 = r6
            kotlin.Unit r10 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x0046 }
            goto L_0x0042
        L_0x0040:
            kotlin.Unit r10 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x0046 }
        L_0x0042:
        L_0x0044:
            monitor-exit(r6)
            goto L_0x000d
        L_0x0046:
            r5 = move-exception
            r8 = r0
            r0 = r5
        L_0x0049:
            monitor-exit(r6)
            throw r0
        L_0x004b:
            long r5 = r13.keepAliveDurationNs
            int r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r7 >= 0) goto L_0x0062
            int r7 = r13.maxIdleConnections
            if (r1 <= r7) goto L_0x0058
            goto L_0x0062
        L_0x0058:
            if (r1 <= 0) goto L_0x005c
            long r5 = r5 - r3
            return r5
        L_0x005c:
            if (r0 <= 0) goto L_0x005f
            return r5
        L_0x005f:
            r5 = -1
            return r5
        L_0x0062:
            kotlin.jvm.internal.Intrinsics.checkNotNull(r2)
            r5 = r2
            monitor-enter(r5)
            r6 = 0
            java.util.List r7 = r5.getCalls()     // Catch:{ all -> 0x00a3 }
            java.util.Collection r7 = (java.util.Collection) r7     // Catch:{ all -> 0x00a3 }
            boolean r7 = r7.isEmpty()     // Catch:{ all -> 0x00a3 }
            r8 = 1
            r7 = r7 ^ r8
            r9 = 0
            if (r7 == 0) goto L_0x007a
            monitor-exit(r5)
            return r9
        L_0x007a:
            long r11 = r5.getIdleAtNs$okhttp()     // Catch:{ all -> 0x00a3 }
            long r11 = r11 + r3
            int r7 = (r11 > r14 ? 1 : (r11 == r14 ? 0 : -1))
            if (r7 == 0) goto L_0x0085
            monitor-exit(r5)
            return r9
        L_0x0085:
            r5.setNoNewExchanges(r8)     // Catch:{ all -> 0x00a3 }
            java.util.concurrent.ConcurrentLinkedQueue<okhttp3.internal.connection.RealConnection> r7 = r13.connections     // Catch:{ all -> 0x00a3 }
            r7.remove(r2)     // Catch:{ all -> 0x00a3 }
            monitor-exit(r5)
            java.net.Socket r6 = r5.socket()
            okhttp3.internal.Util.closeQuietly((java.net.Socket) r6)
            java.util.concurrent.ConcurrentLinkedQueue<okhttp3.internal.connection.RealConnection> r6 = r13.connections
            boolean r6 = r6.isEmpty()
            if (r6 == 0) goto L_0x00a2
            okhttp3.internal.concurrent.TaskQueue r6 = r13.cleanupQueue
            r6.cancelAll()
        L_0x00a2:
            return r9
        L_0x00a3:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.connection.RealConnectionPool.cleanup(long):long");
    }

    public final boolean connectionBecameIdle(RealConnection connection) {
        Intrinsics.checkNotNullParameter(connection, "connection");
        RealConnection realConnection = connection;
        if (Util.assertionsEnabled && !Thread.holdsLock(realConnection)) {
            StringBuilder append = new StringBuilder().append("Thread ");
            Thread currentThread = Thread.currentThread();
            Intrinsics.checkNotNullExpressionValue(currentThread, "Thread.currentThread()");
            throw new AssertionError(append.append(currentThread.getName()).append(" MUST hold lock on ").append(realConnection).toString());
        } else if (connection.getNoNewExchanges() || this.maxIdleConnections == 0) {
            connection.setNoNewExchanges(true);
            this.connections.remove(connection);
            if (!this.connections.isEmpty()) {
                return true;
            }
            this.cleanupQueue.cancelAll();
            return true;
        } else {
            TaskQueue.schedule$default(this.cleanupQueue, this.cleanupTask, 0, 2, (Object) null);
            return false;
        }
    }

    public final int connectionCount() {
        return this.connections.size();
    }

    public final void evictAll() {
        Socket socket;
        Iterator<RealConnection> it = this.connections.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "connections.iterator()");
        while (it.hasNext()) {
            RealConnection next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "connection");
            synchronized (next) {
                if (next.getCalls().isEmpty()) {
                    it.remove();
                    next.setNoNewExchanges(true);
                    socket = next.socket();
                } else {
                    socket = null;
                }
            }
            Socket socket2 = socket;
            if (socket2 != null) {
                Util.closeQuietly(socket2);
            }
        }
        if (this.connections.isEmpty()) {
            this.cleanupQueue.cancelAll();
        }
    }

    public final int idleConnectionCount() {
        boolean isEmpty;
        Iterable<RealConnection> iterable = this.connections;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return 0;
        }
        int i = 0;
        for (RealConnection realConnection : iterable) {
            Intrinsics.checkNotNullExpressionValue(realConnection, "it");
            synchronized (realConnection) {
                isEmpty = realConnection.getCalls().isEmpty();
            }
            if (isEmpty && (i = i + 1) < 0) {
                CollectionsKt.throwCountOverflow();
            }
        }
        return i;
    }

    public final void put(RealConnection connection) {
        Intrinsics.checkNotNullParameter(connection, "connection");
        RealConnection realConnection = connection;
        if (!Util.assertionsEnabled || Thread.holdsLock(realConnection)) {
            this.connections.add(connection);
            TaskQueue.schedule$default(this.cleanupQueue, this.cleanupTask, 0, 2, (Object) null);
            return;
        }
        StringBuilder append = new StringBuilder().append("Thread ");
        Thread currentThread = Thread.currentThread();
        Intrinsics.checkNotNullExpressionValue(currentThread, "Thread.currentThread()");
        throw new AssertionError(append.append(currentThread.getName()).append(" MUST hold lock on ").append(realConnection).toString());
    }
}

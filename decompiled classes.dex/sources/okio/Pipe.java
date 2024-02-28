package okio;

import java.util.concurrent.TimeUnit;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010!\u001a\u00020\"J\u000e\u0010#\u001a\u00020\"2\u0006\u0010\u0017\u001a\u00020\u0010J\r\u0010\u0017\u001a\u00020\u0010H\u0007¢\u0006\u0002\b$J\r\u0010\u001b\u001a\u00020\u001cH\u0007¢\u0006\u0002\b%J&\u0010&\u001a\u00020\"*\u00020\u00102\u0017\u0010'\u001a\u0013\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\"0(¢\u0006\u0002\b)H\bR\u0014\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0013\u0010\u0017\u001a\u00020\u00108G¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0012R\u001a\u0010\u0018\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\f\"\u0004\b\u001a\u0010\u000eR\u0013\u0010\u001b\u001a\u00020\u001c8G¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001dR\u001a\u0010\u001e\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\f\"\u0004\b \u0010\u000e¨\u0006*"}, d2 = {"Lokio/Pipe;", "", "maxBufferSize", "", "(J)V", "buffer", "Lokio/Buffer;", "getBuffer$okio", "()Lokio/Buffer;", "canceled", "", "getCanceled$okio", "()Z", "setCanceled$okio", "(Z)V", "foldedSink", "Lokio/Sink;", "getFoldedSink$okio", "()Lokio/Sink;", "setFoldedSink$okio", "(Lokio/Sink;)V", "getMaxBufferSize$okio", "()J", "sink", "sinkClosed", "getSinkClosed$okio", "setSinkClosed$okio", "source", "Lokio/Source;", "()Lokio/Source;", "sourceClosed", "getSourceClosed$okio", "setSourceClosed$okio", "cancel", "", "fold", "-deprecated_sink", "-deprecated_source", "forward", "block", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "okio"}, k = 1, mv = {1, 4, 0})
/* compiled from: Pipe.kt */
public final class Pipe {
    private final Buffer buffer = new Buffer();
    private boolean canceled;
    private Sink foldedSink;
    private final long maxBufferSize;
    private final Sink sink;
    private boolean sinkClosed;
    private final Source source;
    private boolean sourceClosed;

    public Pipe(long maxBufferSize2) {
        this.maxBufferSize = maxBufferSize2;
        if (maxBufferSize2 >= 1) {
            this.sink = new Pipe$sink$1(this);
            this.source = new Pipe$source$1(this);
            return;
        }
        throw new IllegalArgumentException(("maxBufferSize < 1: " + maxBufferSize2).toString());
    }

    /* access modifiers changed from: private */
    public final void forward(Sink $this$forward, Function1<? super Sink, Unit> block) {
        Sink sink2 = $this$forward;
        Function1<? super Sink, Unit> function1 = block;
        Timeout timeout = $this$forward.timeout();
        Timeout timeout2 = sink().timeout();
        long timeoutNanos = timeout.timeoutNanos();
        timeout.timeout(Timeout.Companion.minTimeout(timeout2.timeoutNanos(), timeout.timeoutNanos()), TimeUnit.NANOSECONDS);
        if (timeout.hasDeadline()) {
            long deadlineNanoTime = timeout.deadlineNanoTime();
            if (timeout2.hasDeadline()) {
                timeout.deadlineNanoTime(Math.min(timeout.deadlineNanoTime(), timeout2.deadlineNanoTime()));
            }
            try {
                function1.invoke(sink2);
                InlineMarker.finallyStart(1);
                timeout.timeout(timeoutNanos, TimeUnit.NANOSECONDS);
                if (timeout2.hasDeadline()) {
                    timeout.deadlineNanoTime(deadlineNanoTime);
                }
                InlineMarker.finallyEnd(1);
            } catch (Throwable th) {
                Throwable th2 = th;
                InlineMarker.finallyStart(1);
                timeout.timeout(timeoutNanos, TimeUnit.NANOSECONDS);
                if (timeout2.hasDeadline()) {
                    timeout.deadlineNanoTime(deadlineNanoTime);
                }
                InlineMarker.finallyEnd(1);
                throw th2;
            }
        } else {
            if (timeout2.hasDeadline()) {
                timeout.deadlineNanoTime(timeout2.deadlineNanoTime());
            }
            try {
                function1.invoke(sink2);
                InlineMarker.finallyStart(1);
                timeout.timeout(timeoutNanos, TimeUnit.NANOSECONDS);
                if (timeout2.hasDeadline()) {
                    timeout.clearDeadline();
                }
                InlineMarker.finallyEnd(1);
            } catch (Throwable th3) {
                Throwable th4 = th3;
                InlineMarker.finallyStart(1);
                timeout.timeout(timeoutNanos, TimeUnit.NANOSECONDS);
                if (timeout2.hasDeadline()) {
                    timeout.clearDeadline();
                }
                InlineMarker.finallyEnd(1);
                throw th4;
            }
        }
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "sink", imports = {}))
    /* renamed from: -deprecated_sink  reason: not valid java name */
    public final Sink m1741deprecated_sink() {
        return this.sink;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "source", imports = {}))
    /* renamed from: -deprecated_source  reason: not valid java name */
    public final Source m1742deprecated_source() {
        return this.source;
    }

    public final void cancel() {
        synchronized (this.buffer) {
            this.canceled = true;
            this.buffer.clear();
            Buffer buffer2 = this.buffer;
            if (buffer2 != null) {
                buffer2.notifyAll();
                Unit unit = Unit.INSTANCE;
            } else {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.Object");
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        r10.write(r1, r1.size());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0052, code lost:
        if (r0 == false) goto L_0x0058;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0054, code lost:
        r10.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0058, code lost:
        r10.flush();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0061, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0066, code lost:
        monitor-enter(r9.buffer);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
        r9.sourceClosed = true;
        r6 = r9.buffer;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x006c, code lost:
        if (r6 == null) goto L_0x006e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0075, code lost:
        throw new java.lang.NullPointerException("null cannot be cast to non-null type java.lang.Object");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0076, code lost:
        r6.notifyAll();
        r6 = kotlin.Unit.INSTANCE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x007e, code lost:
        throw r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void fold(okio.Sink r10) throws java.io.IOException {
        /*
            r9 = this;
            java.lang.String r0 = "sink"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r10, r0)
        L_0x0006:
            r0 = 0
            r1 = 0
            okio.Buffer r2 = r9.buffer
            r3 = 0
            monitor-enter(r2)
            r4 = 0
            okio.Sink r5 = r9.foldedSink     // Catch:{ all -> 0x00a6 }
            r6 = 1
            if (r5 != 0) goto L_0x0015
            r5 = r6
            goto L_0x0016
        L_0x0015:
            r5 = 0
        L_0x0016:
            if (r5 == 0) goto L_0x0096
            boolean r5 = r9.canceled     // Catch:{ all -> 0x00a6 }
            if (r5 != 0) goto L_0x008a
            okio.Buffer r5 = r9.buffer     // Catch:{ all -> 0x00a6 }
            boolean r5 = r5.exhausted()     // Catch:{ all -> 0x00a6 }
            if (r5 == 0) goto L_0x002a
            r9.sourceClosed = r6     // Catch:{ all -> 0x00a6 }
            r9.foldedSink = r10     // Catch:{ all -> 0x00a6 }
            monitor-exit(r2)
            return
        L_0x002a:
            boolean r5 = r9.sinkClosed     // Catch:{ all -> 0x00a6 }
            r0 = r5
            okio.Buffer r5 = new okio.Buffer     // Catch:{ all -> 0x00a6 }
            r5.<init>()     // Catch:{ all -> 0x00a6 }
            r1 = r5
            okio.Buffer r5 = r9.buffer     // Catch:{ all -> 0x00a6 }
            long r7 = r5.size()     // Catch:{ all -> 0x00a6 }
            r1.write((okio.Buffer) r5, (long) r7)     // Catch:{ all -> 0x00a6 }
            okio.Buffer r5 = r9.buffer     // Catch:{ all -> 0x00a6 }
            if (r5 == 0) goto L_0x0082
            java.lang.Object r5 = (java.lang.Object) r5     // Catch:{ all -> 0x00a6 }
            r5.notifyAll()     // Catch:{ all -> 0x00a6 }
            kotlin.Unit r4 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x00a6 }
            monitor-exit(r2)
            r2 = 0
            long r3 = r1.size()     // Catch:{ all -> 0x0061 }
            r10.write(r1, r3)     // Catch:{ all -> 0x0061 }
            if (r0 == 0) goto L_0x0058
            r10.close()     // Catch:{ all -> 0x0061 }
            goto L_0x005b
        L_0x0058:
            r10.flush()     // Catch:{ all -> 0x0061 }
        L_0x005b:
            r2 = 1
            goto L_0x0006
        L_0x0061:
            r3 = move-exception
            okio.Buffer r4 = r9.buffer
            r5 = 0
            monitor-enter(r4)
            r7 = 0
            r9.sourceClosed = r6     // Catch:{ all -> 0x007f }
            okio.Buffer r6 = r9.buffer     // Catch:{ all -> 0x007f }
            if (r6 != 0) goto L_0x0076
            java.lang.NullPointerException r3 = new java.lang.NullPointerException     // Catch:{ all -> 0x007f }
            java.lang.String r6 = "null cannot be cast to non-null type java.lang.Object"
            r3.<init>(r6)     // Catch:{ all -> 0x007f }
            throw r3     // Catch:{ all -> 0x007f }
        L_0x0076:
            java.lang.Object r6 = (java.lang.Object) r6     // Catch:{ all -> 0x007f }
            r6.notifyAll()     // Catch:{ all -> 0x007f }
            kotlin.Unit r6 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x007f }
            monitor-exit(r4)
            throw r3
        L_0x007f:
            r3 = move-exception
            monitor-exit(r4)
            throw r3
        L_0x0082:
            java.lang.NullPointerException r5 = new java.lang.NullPointerException     // Catch:{ all -> 0x00a6 }
            java.lang.String r6 = "null cannot be cast to non-null type java.lang.Object"
            r5.<init>(r6)     // Catch:{ all -> 0x00a6 }
            throw r5     // Catch:{ all -> 0x00a6 }
        L_0x008a:
            r9.foldedSink = r10     // Catch:{ all -> 0x00a6 }
            java.io.IOException r5 = new java.io.IOException     // Catch:{ all -> 0x00a6 }
            java.lang.String r6 = "canceled"
            r5.<init>(r6)     // Catch:{ all -> 0x00a6 }
            java.lang.Throwable r5 = (java.lang.Throwable) r5     // Catch:{ all -> 0x00a6 }
            throw r5     // Catch:{ all -> 0x00a6 }
        L_0x0096:
            r5 = 0
            java.lang.String r6 = "sink already folded"
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException     // Catch:{ all -> 0x00a6 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x00a6 }
            r5.<init>(r6)     // Catch:{ all -> 0x00a6 }
            java.lang.Throwable r5 = (java.lang.Throwable) r5     // Catch:{ all -> 0x00a6 }
            throw r5     // Catch:{ all -> 0x00a6 }
        L_0x00a6:
            r4 = move-exception
            monitor-exit(r2)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Pipe.fold(okio.Sink):void");
    }

    public final Buffer getBuffer$okio() {
        return this.buffer;
    }

    public final boolean getCanceled$okio() {
        return this.canceled;
    }

    public final Sink getFoldedSink$okio() {
        return this.foldedSink;
    }

    public final long getMaxBufferSize$okio() {
        return this.maxBufferSize;
    }

    public final boolean getSinkClosed$okio() {
        return this.sinkClosed;
    }

    public final boolean getSourceClosed$okio() {
        return this.sourceClosed;
    }

    public final void setCanceled$okio(boolean z) {
        this.canceled = z;
    }

    public final void setFoldedSink$okio(Sink sink2) {
        this.foldedSink = sink2;
    }

    public final void setSinkClosed$okio(boolean z) {
        this.sinkClosed = z;
    }

    public final void setSourceClosed$okio(boolean z) {
        this.sourceClosed = z;
    }

    public final Sink sink() {
        return this.sink;
    }

    public final Source source() {
        return this.source;
    }
}

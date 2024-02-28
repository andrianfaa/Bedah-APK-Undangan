package okio;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0005H\u0016J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\u0018\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"okio/Pipe$sink$1", "Lokio/Sink;", "timeout", "Lokio/Timeout;", "close", "", "flush", "write", "source", "Lokio/Buffer;", "byteCount", "", "okio"}, k = 1, mv = {1, 4, 0})
/* compiled from: Pipe.kt */
public final class Pipe$sink$1 implements Sink {
    final /* synthetic */ Pipe this$0;
    private final Timeout timeout = new Timeout();

    Pipe$sink$1(Pipe this$02) {
        this.this$0 = this$02;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x005c, code lost:
        if (r0 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x005e, code lost:
        r1 = r15.this$0;
        r2 = r0;
        r4 = r2.timeout();
        r5 = r1.sink().timeout();
        r7 = r4.timeoutNanos();
        r4.timeout(okio.Timeout.Companion.minTimeout(r5.timeoutNanos(), r4.timeoutNanos()), java.util.concurrent.TimeUnit.NANOSECONDS);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x008a, code lost:
        if (r4.hasDeadline() == false) goto L_0x00cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x008c, code lost:
        r9 = r4.deadlineNanoTime();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0094, code lost:
        if (r5.hasDeadline() == false) goto L_0x00a5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0096, code lost:
        r4.deadlineNanoTime(java.lang.Math.min(r4.deadlineNanoTime(), r5.deadlineNanoTime()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00bb, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00bc, code lost:
        r4.timeout(r7, java.util.concurrent.TimeUnit.NANOSECONDS);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00c5, code lost:
        if (r5.hasDeadline() != false) goto L_0x00c7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00c7, code lost:
        r4.deadlineNanoTime(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00ca, code lost:
        throw r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00cf, code lost:
        if (r5.hasDeadline() == false) goto L_0x00d8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00d1, code lost:
        r4.deadlineNanoTime(r5.deadlineNanoTime());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00f2, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00f3, code lost:
        r4.timeout(r7, java.util.concurrent.TimeUnit.NANOSECONDS);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00fc, code lost:
        if (r5.hasDeadline() != false) goto L_0x00fe;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00fe, code lost:
        r4.clearDeadline();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0101, code lost:
        throw r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() {
        /*
            r15 = this;
            r0 = 0
            okio.Sink r0 = (okio.Sink) r0
            okio.Pipe r1 = r15.this$0
            okio.Buffer r1 = r1.getBuffer$okio()
            r2 = 0
            monitor-enter(r1)
            r3 = 0
            okio.Pipe r4 = r15.this$0     // Catch:{ all -> 0x010c }
            boolean r4 = r4.getSinkClosed$okio()     // Catch:{ all -> 0x010c }
            if (r4 == 0) goto L_0x0016
            monitor-exit(r1)
            return
        L_0x0016:
            okio.Pipe r4 = r15.this$0     // Catch:{ all -> 0x010c }
            okio.Sink r4 = r4.getFoldedSink$okio()     // Catch:{ all -> 0x010c }
            if (r4 == 0) goto L_0x0021
            r5 = 0
            r0 = r4
            goto L_0x0058
        L_0x0021:
            okio.Pipe r4 = r15.this$0     // Catch:{ all -> 0x010c }
            boolean r4 = r4.getSourceClosed$okio()     // Catch:{ all -> 0x010c }
            if (r4 == 0) goto L_0x0045
            okio.Pipe r4 = r15.this$0     // Catch:{ all -> 0x010c }
            okio.Buffer r4 = r4.getBuffer$okio()     // Catch:{ all -> 0x010c }
            long r4 = r4.size()     // Catch:{ all -> 0x010c }
            r6 = 0
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 > 0) goto L_0x003a
            goto L_0x0045
        L_0x003a:
            java.io.IOException r4 = new java.io.IOException     // Catch:{ all -> 0x010c }
            java.lang.String r5 = "source is closed"
            r4.<init>(r5)     // Catch:{ all -> 0x010c }
            java.lang.Throwable r4 = (java.lang.Throwable) r4     // Catch:{ all -> 0x010c }
            throw r4     // Catch:{ all -> 0x010c }
        L_0x0045:
            okio.Pipe r4 = r15.this$0     // Catch:{ all -> 0x010c }
            r5 = 1
            r4.setSinkClosed$okio(r5)     // Catch:{ all -> 0x010c }
            okio.Pipe r4 = r15.this$0     // Catch:{ all -> 0x010c }
            okio.Buffer r4 = r4.getBuffer$okio()     // Catch:{ all -> 0x010c }
            if (r4 == 0) goto L_0x0104
            java.lang.Object r4 = (java.lang.Object) r4     // Catch:{ all -> 0x010c }
            r4.notifyAll()     // Catch:{ all -> 0x010c }
        L_0x0058:
            kotlin.Unit r3 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x010c }
            monitor-exit(r1)
            if (r0 == 0) goto L_0x0102
            okio.Pipe r1 = r15.this$0
            r2 = r0
            r3 = 0
            okio.Timeout r4 = r2.timeout()
            okio.Sink r5 = r1.sink()
            okio.Timeout r5 = r5.timeout()
            r6 = 0
            long r7 = r4.timeoutNanos()
            okio.Timeout$Companion r9 = okio.Timeout.Companion
            long r10 = r5.timeoutNanos()
            long r12 = r4.timeoutNanos()
            long r9 = r9.minTimeout(r10, r12)
            java.util.concurrent.TimeUnit r11 = java.util.concurrent.TimeUnit.NANOSECONDS
            r4.timeout(r9, r11)
            boolean r9 = r4.hasDeadline()
            if (r9 == 0) goto L_0x00cb
            long r9 = r4.deadlineNanoTime()
            boolean r11 = r5.hasDeadline()
            if (r11 == 0) goto L_0x00a5
            long r11 = r4.deadlineNanoTime()
            long r13 = r5.deadlineNanoTime()
            long r11 = java.lang.Math.min(r11, r13)
            r4.deadlineNanoTime(r11)
        L_0x00a5:
            r11 = 0
            r12 = r2
            r13 = 0
            r12.close()     // Catch:{ all -> 0x00bb }
            java.util.concurrent.TimeUnit r11 = java.util.concurrent.TimeUnit.NANOSECONDS
            r4.timeout(r7, r11)
            boolean r11 = r5.hasDeadline()
            if (r11 == 0) goto L_0x00ba
            r4.deadlineNanoTime(r9)
        L_0x00ba:
            goto L_0x00ee
        L_0x00bb:
            r11 = move-exception
            java.util.concurrent.TimeUnit r12 = java.util.concurrent.TimeUnit.NANOSECONDS
            r4.timeout(r7, r12)
            boolean r12 = r5.hasDeadline()
            if (r12 == 0) goto L_0x00ca
            r4.deadlineNanoTime(r9)
        L_0x00ca:
            throw r11
        L_0x00cb:
            boolean r9 = r5.hasDeadline()
            if (r9 == 0) goto L_0x00d8
            long r9 = r5.deadlineNanoTime()
            r4.deadlineNanoTime(r9)
        L_0x00d8:
            r9 = 0
            r10 = r2
            r11 = 0
            r10.close()     // Catch:{ all -> 0x00f2 }
            java.util.concurrent.TimeUnit r9 = java.util.concurrent.TimeUnit.NANOSECONDS
            r4.timeout(r7, r9)
            boolean r9 = r5.hasDeadline()
            if (r9 == 0) goto L_0x00ed
            r4.clearDeadline()
        L_0x00ed:
        L_0x00ee:
            goto L_0x0103
        L_0x00f2:
            r9 = move-exception
            java.util.concurrent.TimeUnit r10 = java.util.concurrent.TimeUnit.NANOSECONDS
            r4.timeout(r7, r10)
            boolean r10 = r5.hasDeadline()
            if (r10 == 0) goto L_0x0101
            r4.clearDeadline()
        L_0x0101:
            throw r9
        L_0x0102:
        L_0x0103:
            return
        L_0x0104:
            java.lang.NullPointerException r4 = new java.lang.NullPointerException     // Catch:{ all -> 0x010c }
            java.lang.String r5 = "null cannot be cast to non-null type java.lang.Object"
            r4.<init>(r5)     // Catch:{ all -> 0x010c }
            throw r4     // Catch:{ all -> 0x010c }
        L_0x010c:
            r3 = move-exception
            monitor-exit(r1)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Pipe$sink$1.close():void");
    }

    public void flush() {
        Sink sink = null;
        synchronized (this.this$0.getBuffer$okio()) {
            if (!(!this.this$0.getSinkClosed$okio())) {
                throw new IllegalStateException("closed".toString());
            } else if (!this.this$0.getCanceled$okio()) {
                Sink foldedSink$okio = this.this$0.getFoldedSink$okio();
                if (foldedSink$okio != null) {
                    sink = foldedSink$okio;
                } else if (this.this$0.getSourceClosed$okio()) {
                    if (this.this$0.getBuffer$okio().size() > 0) {
                        throw new IOException("source is closed");
                    }
                }
                Unit unit = Unit.INSTANCE;
            } else {
                throw new IOException("canceled");
            }
        }
        if (sink != null) {
            Pipe pipe = this.this$0;
            Sink sink2 = sink;
            Timeout timeout2 = sink2.timeout();
            Timeout timeout3 = pipe.sink().timeout();
            long timeoutNanos = timeout2.timeoutNanos();
            timeout2.timeout(Timeout.Companion.minTimeout(timeout3.timeoutNanos(), timeout2.timeoutNanos()), TimeUnit.NANOSECONDS);
            if (timeout2.hasDeadline()) {
                long deadlineNanoTime = timeout2.deadlineNanoTime();
                if (timeout3.hasDeadline()) {
                    timeout2.deadlineNanoTime(Math.min(timeout2.deadlineNanoTime(), timeout3.deadlineNanoTime()));
                }
                try {
                    sink2.flush();
                } finally {
                    timeout2.timeout(timeoutNanos, TimeUnit.NANOSECONDS);
                    if (timeout3.hasDeadline()) {
                        timeout2.deadlineNanoTime(deadlineNanoTime);
                    }
                }
            } else {
                if (timeout3.hasDeadline()) {
                    timeout2.deadlineNanoTime(timeout3.deadlineNanoTime());
                }
                try {
                    sink2.flush();
                } finally {
                    timeout2.timeout(timeoutNanos, TimeUnit.NANOSECONDS);
                    if (timeout3.hasDeadline()) {
                        timeout2.clearDeadline();
                    }
                }
            }
        }
    }

    public Timeout timeout() {
        return this.timeout;
    }

    public void write(Buffer source, long byteCount) {
        Buffer buffer = source;
        Intrinsics.checkNotNullParameter(buffer, "source");
        long j = byteCount;
        Sink sink = null;
        synchronized (this.this$0.getBuffer$okio()) {
            if (!(!this.this$0.getSinkClosed$okio())) {
                throw new IllegalStateException("closed".toString());
            } else if (!this.this$0.getCanceled$okio()) {
                while (true) {
                    if (j <= 0) {
                        break;
                    }
                    Sink foldedSink$okio = this.this$0.getFoldedSink$okio();
                    if (foldedSink$okio != null) {
                        sink = foldedSink$okio;
                        break;
                    } else if (!this.this$0.getSourceClosed$okio()) {
                        long maxBufferSize$okio = this.this$0.getMaxBufferSize$okio() - this.this$0.getBuffer$okio().size();
                        if (maxBufferSize$okio == 0) {
                            this.timeout.waitUntilNotified(this.this$0.getBuffer$okio());
                            if (this.this$0.getCanceled$okio()) {
                                throw new IOException("canceled");
                            }
                        } else {
                            long min = Math.min(maxBufferSize$okio, j);
                            this.this$0.getBuffer$okio().write(buffer, min);
                            j -= min;
                            Buffer buffer$okio = this.this$0.getBuffer$okio();
                            if (buffer$okio != null) {
                                buffer$okio.notifyAll();
                            } else {
                                throw new NullPointerException("null cannot be cast to non-null type java.lang.Object");
                            }
                        }
                    } else {
                        throw new IOException("source is closed");
                    }
                }
                Unit unit = Unit.INSTANCE;
            } else {
                throw new IOException("canceled");
            }
        }
        if (sink != null) {
            Sink sink2 = sink;
            Pipe pipe = this.this$0;
            Timeout timeout2 = sink2.timeout();
            Timeout timeout3 = pipe.sink().timeout();
            long timeoutNanos = timeout2.timeoutNanos();
            Pipe pipe2 = pipe;
            timeout2.timeout(Timeout.Companion.minTimeout(timeout3.timeoutNanos(), timeout2.timeoutNanos()), TimeUnit.NANOSECONDS);
            if (timeout2.hasDeadline()) {
                long deadlineNanoTime = timeout2.deadlineNanoTime();
                if (timeout3.hasDeadline()) {
                    timeout2.deadlineNanoTime(Math.min(timeout2.deadlineNanoTime(), timeout3.deadlineNanoTime()));
                }
                try {
                    sink2.write(buffer, j);
                    timeout2.timeout(timeoutNanos, TimeUnit.NANOSECONDS);
                    if (timeout3.hasDeadline()) {
                        timeout2.deadlineNanoTime(deadlineNanoTime);
                    }
                } catch (Throwable th) {
                    Throwable th2 = th;
                    timeout2.timeout(timeoutNanos, TimeUnit.NANOSECONDS);
                    if (timeout3.hasDeadline()) {
                        timeout2.deadlineNanoTime(deadlineNanoTime);
                    }
                    throw th2;
                }
            } else {
                if (timeout3.hasDeadline()) {
                    timeout2.deadlineNanoTime(timeout3.deadlineNanoTime());
                }
                try {
                    sink2.write(buffer, j);
                    timeout2.timeout(timeoutNanos, TimeUnit.NANOSECONDS);
                    if (timeout3.hasDeadline()) {
                        timeout2.clearDeadline();
                    }
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    timeout2.timeout(timeoutNanos, TimeUnit.NANOSECONDS);
                    if (timeout3.hasDeadline()) {
                        timeout2.clearDeadline();
                    }
                    throw th4;
                }
            }
        }
    }
}

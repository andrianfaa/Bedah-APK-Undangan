package okhttp3.internal.http2;

import kotlin.Metadata;
import okhttp3.internal.concurrent.Task;
import okio.Buffer;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004¸\u0006\u0000"}, d2 = {"okhttp3/internal/concurrent/TaskQueue$execute$1", "Lokhttp3/internal/concurrent/Task;", "runOnce", "", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: TaskQueue.kt */
public final class Http2Connection$pushDataLater$$inlined$execute$1 extends Task {
    final /* synthetic */ Buffer $buffer$inlined;
    final /* synthetic */ int $byteCount$inlined;
    final /* synthetic */ boolean $cancelable;
    final /* synthetic */ boolean $inFinished$inlined;
    final /* synthetic */ String $name;
    final /* synthetic */ int $streamId$inlined;
    final /* synthetic */ Http2Connection this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public Http2Connection$pushDataLater$$inlined$execute$1(String $captured_local_variable$1, boolean $captured_local_variable$2, String $super_call_param$3, boolean $super_call_param$4, Http2Connection http2Connection, int i, Buffer buffer, int i2, boolean z) {
        super($super_call_param$3, $super_call_param$4);
        this.$name = $captured_local_variable$1;
        this.$cancelable = $captured_local_variable$2;
        this.this$0 = http2Connection;
        this.$streamId$inlined = i;
        this.$buffer$inlined = buffer;
        this.$byteCount$inlined = i2;
        this.$inFinished$inlined = z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        return -1;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long runOnce() {
        /*
            r8 = this;
            r0 = 0
            r1 = 0
            r2 = 0
            okhttp3.internal.http2.Http2Connection r3 = r8.this$0     // Catch:{ IOException -> 0x0046 }
            okhttp3.internal.http2.PushObserver r3 = r3.pushObserver     // Catch:{ IOException -> 0x0046 }
            int r4 = r8.$streamId$inlined     // Catch:{ IOException -> 0x0046 }
            okio.Buffer r5 = r8.$buffer$inlined     // Catch:{ IOException -> 0x0046 }
            okio.BufferedSource r5 = (okio.BufferedSource) r5     // Catch:{ IOException -> 0x0046 }
            int r6 = r8.$byteCount$inlined     // Catch:{ IOException -> 0x0046 }
            boolean r7 = r8.$inFinished$inlined     // Catch:{ IOException -> 0x0046 }
            boolean r3 = r3.onData(r4, r5, r6, r7)     // Catch:{ IOException -> 0x0046 }
            if (r3 == 0) goto L_0x0027
            okhttp3.internal.http2.Http2Connection r4 = r8.this$0     // Catch:{ IOException -> 0x0046 }
            okhttp3.internal.http2.Http2Writer r4 = r4.getWriter()     // Catch:{ IOException -> 0x0046 }
            int r5 = r8.$streamId$inlined     // Catch:{ IOException -> 0x0046 }
            okhttp3.internal.http2.ErrorCode r6 = okhttp3.internal.http2.ErrorCode.CANCEL     // Catch:{ IOException -> 0x0046 }
            r4.rstStream(r5, r6)     // Catch:{ IOException -> 0x0046 }
        L_0x0027:
            if (r3 != 0) goto L_0x002d
            boolean r4 = r8.$inFinished$inlined     // Catch:{ IOException -> 0x0046 }
            if (r4 == 0) goto L_0x0041
        L_0x002d:
            okhttp3.internal.http2.Http2Connection r4 = r8.this$0     // Catch:{ IOException -> 0x0046 }
            monitor-enter(r4)     // Catch:{ IOException -> 0x0046 }
            r5 = 0
            okhttp3.internal.http2.Http2Connection r6 = r8.this$0     // Catch:{ all -> 0x0043 }
            java.util.Set r6 = r6.currentPushRequests     // Catch:{ all -> 0x0043 }
            int r7 = r8.$streamId$inlined     // Catch:{ all -> 0x0043 }
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ all -> 0x0043 }
            r6.remove(r7)     // Catch:{ all -> 0x0043 }
            monitor-exit(r4)     // Catch:{ IOException -> 0x0046 }
        L_0x0041:
            goto L_0x0047
        L_0x0043:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ IOException -> 0x0046 }
            throw r5     // Catch:{ IOException -> 0x0046 }
        L_0x0046:
            r2 = move-exception
        L_0x0047:
            r0 = -1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection$pushDataLater$$inlined$execute$1.runOnce():long");
    }
}

package okhttp3.internal.http2;

import java.util.List;
import kotlin.Metadata;
import okhttp3.internal.concurrent.Task;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004¸\u0006\u0000"}, d2 = {"okhttp3/internal/concurrent/TaskQueue$execute$1", "Lokhttp3/internal/concurrent/Task;", "runOnce", "", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: TaskQueue.kt */
public final class Http2Connection$pushRequestLater$$inlined$execute$1 extends Task {
    final /* synthetic */ boolean $cancelable;
    final /* synthetic */ String $name;
    final /* synthetic */ List $requestHeaders$inlined;
    final /* synthetic */ int $streamId$inlined;
    final /* synthetic */ Http2Connection this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public Http2Connection$pushRequestLater$$inlined$execute$1(String $captured_local_variable$1, boolean $captured_local_variable$2, String $super_call_param$3, boolean $super_call_param$4, Http2Connection http2Connection, int i, List list) {
        super($super_call_param$3, $super_call_param$4);
        this.$name = $captured_local_variable$1;
        this.$cancelable = $captured_local_variable$2;
        this.this$0 = http2Connection;
        this.$streamId$inlined = i;
        this.$requestHeaders$inlined = list;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        return -1;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long runOnce() {
        /*
            r8 = this;
            r0 = 0
            okhttp3.internal.http2.Http2Connection r1 = r8.this$0
            okhttp3.internal.http2.PushObserver r1 = r1.pushObserver
            int r2 = r8.$streamId$inlined
            java.util.List r3 = r8.$requestHeaders$inlined
            boolean r1 = r1.onRequest(r2, r3)
            r2 = 0
            r3 = 0
            if (r1 == 0) goto L_0x003b
            okhttp3.internal.http2.Http2Connection r4 = r8.this$0     // Catch:{ IOException -> 0x0039 }
            okhttp3.internal.http2.Http2Writer r4 = r4.getWriter()     // Catch:{ IOException -> 0x0039 }
            int r5 = r8.$streamId$inlined     // Catch:{ IOException -> 0x0039 }
            okhttp3.internal.http2.ErrorCode r6 = okhttp3.internal.http2.ErrorCode.CANCEL     // Catch:{ IOException -> 0x0039 }
            r4.rstStream(r5, r6)     // Catch:{ IOException -> 0x0039 }
            okhttp3.internal.http2.Http2Connection r4 = r8.this$0     // Catch:{ IOException -> 0x0039 }
            monitor-enter(r4)     // Catch:{ IOException -> 0x0039 }
            r5 = 0
            okhttp3.internal.http2.Http2Connection r6 = r8.this$0     // Catch:{ all -> 0x0036 }
            java.util.Set r6 = r6.currentPushRequests     // Catch:{ all -> 0x0036 }
            int r7 = r8.$streamId$inlined     // Catch:{ all -> 0x0036 }
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ all -> 0x0036 }
            r6.remove(r7)     // Catch:{ all -> 0x0036 }
            monitor-exit(r4)     // Catch:{ IOException -> 0x0039 }
            goto L_0x003b
        L_0x0036:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ IOException -> 0x0039 }
            throw r5     // Catch:{ IOException -> 0x0039 }
        L_0x0039:
            r3 = move-exception
            goto L_0x003c
        L_0x003b:
        L_0x003c:
            r0 = -1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection$pushRequestLater$$inlined$execute$1.runOnce():long");
    }
}

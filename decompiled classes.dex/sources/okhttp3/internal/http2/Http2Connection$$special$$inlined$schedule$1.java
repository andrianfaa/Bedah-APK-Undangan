package okhttp3.internal.http2;

import java.io.IOException;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.internal.concurrent.Task;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004¸\u0006\u0000"}, d2 = {"okhttp3/internal/concurrent/TaskQueue$schedule$2", "Lokhttp3/internal/concurrent/Task;", "runOnce", "", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: TaskQueue.kt */
public final class Http2Connection$$special$$inlined$schedule$1 extends Task {
    final /* synthetic */ String $name;
    final /* synthetic */ long $pingIntervalNanos$inlined;
    final /* synthetic */ Http2Connection this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public Http2Connection$$special$$inlined$schedule$1(String $captured_local_variable$1, String $super_call_param$2, Http2Connection http2Connection, long j) {
        super($super_call_param$2, false, 2, (DefaultConstructorMarker) null);
        this.$name = $captured_local_variable$1;
        this.this$0 = http2Connection;
        this.$pingIntervalNanos$inlined = j;
    }

    public long runOnce() {
        boolean z;
        synchronized (this.this$0) {
            if (this.this$0.intervalPongsReceived < this.this$0.intervalPingsSent) {
                z = true;
            } else {
                Http2Connection http2Connection = this.this$0;
                http2Connection.intervalPingsSent = http2Connection.intervalPingsSent + 1;
                z = false;
            }
        }
        if (z) {
            this.this$0.failConnection((IOException) null);
            return -1;
        }
        this.this$0.writePing(false, 1, 0);
        return this.$pingIntervalNanos$inlined;
    }
}

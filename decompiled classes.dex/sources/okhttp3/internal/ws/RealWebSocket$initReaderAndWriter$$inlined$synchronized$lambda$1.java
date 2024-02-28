package okhttp3.internal.ws;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.internal.concurrent.Task;
import okhttp3.internal.ws.RealWebSocket;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004¸\u0006\u0005"}, d2 = {"okhttp3/internal/concurrent/TaskQueue$schedule$2", "Lokhttp3/internal/concurrent/Task;", "runOnce", "", "okhttp", "okhttp3/internal/ws/RealWebSocket$$special$$inlined$schedule$1"}, k = 1, mv = {1, 4, 0})
/* compiled from: TaskQueue.kt */
public final class RealWebSocket$initReaderAndWriter$$inlined$synchronized$lambda$1 extends Task {
    final /* synthetic */ WebSocketExtensions $extensions$inlined;
    final /* synthetic */ String $name;
    final /* synthetic */ String $name$inlined;
    final /* synthetic */ long $pingIntervalNanos$inlined;
    final /* synthetic */ RealWebSocket.Streams $streams$inlined;
    final /* synthetic */ RealWebSocket this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public RealWebSocket$initReaderAndWriter$$inlined$synchronized$lambda$1(String $captured_local_variable$1, String $super_call_param$2, long j, RealWebSocket realWebSocket, String str, RealWebSocket.Streams streams, WebSocketExtensions webSocketExtensions) {
        super($super_call_param$2, false, 2, (DefaultConstructorMarker) null);
        this.$name = $captured_local_variable$1;
        this.$pingIntervalNanos$inlined = j;
        this.this$0 = realWebSocket;
        this.$name$inlined = str;
        this.$streams$inlined = streams;
        this.$extensions$inlined = webSocketExtensions;
    }

    public long runOnce() {
        this.this$0.writePingFrame$okhttp();
        return this.$pingIntervalNanos$inlined;
    }
}

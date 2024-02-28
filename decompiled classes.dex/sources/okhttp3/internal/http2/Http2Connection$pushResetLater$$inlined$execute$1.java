package okhttp3.internal.http2;

import kotlin.Metadata;
import kotlin.Unit;
import okhttp3.internal.concurrent.Task;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004¸\u0006\u0000"}, d2 = {"okhttp3/internal/concurrent/TaskQueue$execute$1", "Lokhttp3/internal/concurrent/Task;", "runOnce", "", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: TaskQueue.kt */
public final class Http2Connection$pushResetLater$$inlined$execute$1 extends Task {
    final /* synthetic */ boolean $cancelable;
    final /* synthetic */ ErrorCode $errorCode$inlined;
    final /* synthetic */ String $name;
    final /* synthetic */ int $streamId$inlined;
    final /* synthetic */ Http2Connection this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public Http2Connection$pushResetLater$$inlined$execute$1(String $captured_local_variable$1, boolean $captured_local_variable$2, String $super_call_param$3, boolean $super_call_param$4, Http2Connection http2Connection, int i, ErrorCode errorCode) {
        super($super_call_param$3, $super_call_param$4);
        this.$name = $captured_local_variable$1;
        this.$cancelable = $captured_local_variable$2;
        this.this$0 = http2Connection;
        this.$streamId$inlined = i;
        this.$errorCode$inlined = errorCode;
    }

    public long runOnce() {
        this.this$0.pushObserver.onReset(this.$streamId$inlined, this.$errorCode$inlined);
        synchronized (this.this$0) {
            this.this$0.currentPushRequests.remove(Integer.valueOf(this.$streamId$inlined));
            Unit unit = Unit.INSTANCE;
        }
        return -1;
    }
}

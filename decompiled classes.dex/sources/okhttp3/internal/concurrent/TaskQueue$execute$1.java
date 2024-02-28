package okhttp3.internal.concurrent;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, d2 = {"okhttp3/internal/concurrent/TaskQueue$execute$1", "Lokhttp3/internal/concurrent/Task;", "runOnce", "", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: TaskQueue.kt */
public final class TaskQueue$execute$1 extends Task {
    final /* synthetic */ Function0 $block;
    final /* synthetic */ boolean $cancelable;
    final /* synthetic */ String $name;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public TaskQueue$execute$1(Function0 $captured_local_variable$0, String $captured_local_variable$1, boolean $captured_local_variable$2, String $super_call_param$3, boolean $super_call_param$4) {
        super($super_call_param$3, $super_call_param$4);
        this.$block = $captured_local_variable$0;
        this.$name = $captured_local_variable$1;
        this.$cancelable = $captured_local_variable$2;
    }

    public long runOnce() {
        this.$block.invoke();
        return -1;
    }
}

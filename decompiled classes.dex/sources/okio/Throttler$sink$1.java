package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, d2 = {"okio/Throttler$sink$1", "Lokio/ForwardingSink;", "write", "", "source", "Lokio/Buffer;", "byteCount", "", "okio"}, k = 1, mv = {1, 4, 0})
/* compiled from: Throttler.kt */
public final class Throttler$sink$1 extends ForwardingSink {
    final /* synthetic */ Sink $sink;
    final /* synthetic */ Throttler this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    Throttler$sink$1(Throttler this$02, Sink $captured_local_variable$1, Sink $super_call_param$2) {
        super($super_call_param$2);
        this.this$0 = this$02;
        this.$sink = $captured_local_variable$1;
    }

    public void write(Buffer source, long byteCount) throws IOException {
        Intrinsics.checkNotNullParameter(source, "source");
        long j = byteCount;
        while (j > 0) {
            try {
                long take$okio = this.this$0.take$okio(j);
                super.write(source, take$okio);
                j -= take$okio;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InterruptedIOException("interrupted");
            }
        }
    }
}

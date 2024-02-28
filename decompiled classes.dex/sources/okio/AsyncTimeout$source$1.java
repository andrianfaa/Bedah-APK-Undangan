package okio;

import java.io.IOException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000+\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0005H\u0016J\b\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\fH\u0016¨\u0006\r"}, d2 = {"okio/AsyncTimeout$source$1", "Lokio/Source;", "close", "", "read", "", "sink", "Lokio/Buffer;", "byteCount", "timeout", "Lokio/AsyncTimeout;", "toString", "", "okio"}, k = 1, mv = {1, 4, 0})
/* compiled from: AsyncTimeout.kt */
public final class AsyncTimeout$source$1 implements Source {
    final /* synthetic */ Source $source;
    final /* synthetic */ AsyncTimeout this$0;

    AsyncTimeout$source$1(AsyncTimeout this$02, Source $captured_local_variable$1) {
        this.this$0 = this$02;
        this.$source = $captured_local_variable$1;
    }

    public void close() {
        AsyncTimeout asyncTimeout = this.this$0;
        asyncTimeout.enter();
        try {
            this.$source.close();
            Unit unit = Unit.INSTANCE;
            if (asyncTimeout.exit()) {
                throw asyncTimeout.access$newTimeoutException((IOException) null);
            }
        } catch (IOException e) {
            throw (!asyncTimeout.exit() ? e : asyncTimeout.access$newTimeoutException(e));
        } catch (Throwable th) {
            if (!asyncTimeout.exit() || 0 == 0) {
                throw th;
            }
            throw asyncTimeout.access$newTimeoutException((IOException) null);
        }
    }

    public long read(Buffer sink, long byteCount) {
        Intrinsics.checkNotNullParameter(sink, "sink");
        AsyncTimeout asyncTimeout = this.this$0;
        asyncTimeout.enter();
        try {
            long read = this.$source.read(sink, byteCount);
            if (!asyncTimeout.exit()) {
                return read;
            }
            throw asyncTimeout.access$newTimeoutException((IOException) null);
        } catch (IOException e) {
            throw (!asyncTimeout.exit() ? e : asyncTimeout.access$newTimeoutException(e));
        } catch (Throwable th) {
            if (!asyncTimeout.exit() || 0 == 0) {
                throw th;
            }
            throw asyncTimeout.access$newTimeoutException((IOException) null);
        }
    }

    public AsyncTimeout timeout() {
        return this.this$0;
    }

    public String toString() {
        return "AsyncTimeout.source(" + this.$source + ')';
    }
}

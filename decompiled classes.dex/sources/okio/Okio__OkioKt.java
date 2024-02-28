package okio;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\r\u0010\u0000\u001a\u00020\u0001H\u0007¢\u0006\u0002\b\u0002\u001a\n\u0010\u0003\u001a\u00020\u0004*\u00020\u0001\u001a\n\u0010\u0003\u001a\u00020\u0005*\u00020\u0006¨\u0006\u0007"}, d2 = {"blackholeSink", "Lokio/Sink;", "blackhole", "buffer", "Lokio/BufferedSink;", "Lokio/BufferedSource;", "Lokio/Source;", "okio"}, k = 5, mv = {1, 4, 0}, xs = "okio/Okio")
/* compiled from: Okio.kt */
final /* synthetic */ class Okio__OkioKt {
    public static final Sink blackhole() {
        return new BlackholeSink();
    }

    public static final BufferedSink buffer(Sink $this$buffer) {
        Intrinsics.checkNotNullParameter($this$buffer, "$this$buffer");
        return new RealBufferedSink($this$buffer);
    }

    public static final BufferedSource buffer(Source $this$buffer) {
        Intrinsics.checkNotNullParameter($this$buffer, "$this$buffer");
        return new RealBufferedSource($this$buffer);
    }
}

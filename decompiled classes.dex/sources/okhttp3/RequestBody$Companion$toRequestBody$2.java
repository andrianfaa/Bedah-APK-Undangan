package okhttp3;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okio.BufferedSink;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\n\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\n"}, d2 = {"okhttp3/RequestBody$Companion$toRequestBody$2", "Lokhttp3/RequestBody;", "contentLength", "", "contentType", "Lokhttp3/MediaType;", "writeTo", "", "sink", "Lokio/BufferedSink;", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: RequestBody.kt */
public final class RequestBody$Companion$toRequestBody$2 extends RequestBody {
    final /* synthetic */ int $byteCount;
    final /* synthetic */ MediaType $contentType;
    final /* synthetic */ int $offset;
    final /* synthetic */ byte[] $this_toRequestBody;

    RequestBody$Companion$toRequestBody$2(byte[] $receiver, MediaType $captured_local_variable$1, int $captured_local_variable$2, int $captured_local_variable$3) {
        this.$this_toRequestBody = $receiver;
        this.$contentType = $captured_local_variable$1;
        this.$byteCount = $captured_local_variable$2;
        this.$offset = $captured_local_variable$3;
    }

    public long contentLength() {
        return (long) this.$byteCount;
    }

    public MediaType contentType() {
        return this.$contentType;
    }

    public void writeTo(BufferedSink sink) {
        Intrinsics.checkNotNullParameter(sink, "sink");
        sink.write(this.$this_toRequestBody, this.$offset, this.$byteCount);
    }
}

package okio;

import java.io.IOException;
import java.io.InputStream;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\b\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0003H\u0016J \u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u0003H\u0016J\b\u0010\u000b\u001a\u00020\fH\u0016¨\u0006\r"}, d2 = {"okio/RealBufferedSource$inputStream$1", "Ljava/io/InputStream;", "available", "", "close", "", "read", "data", "", "offset", "byteCount", "toString", "", "okio"}, k = 1, mv = {1, 4, 0})
/* compiled from: RealBufferedSource.kt */
public final class RealBufferedSource$inputStream$1 extends InputStream {
    final /* synthetic */ RealBufferedSource this$0;

    RealBufferedSource$inputStream$1(RealBufferedSource this$02) {
        this.this$0 = this$02;
    }

    public int available() {
        if (!this.this$0.closed) {
            return (int) Math.min(this.this$0.bufferField.size(), (long) Integer.MAX_VALUE);
        }
        throw new IOException("closed");
    }

    public void close() {
        this.this$0.close();
    }

    public int read() {
        if (this.this$0.closed) {
            throw new IOException("closed");
        } else if (this.this$0.bufferField.size() == 0 && this.this$0.source.read(this.this$0.bufferField, (long) 8192) == -1) {
            return -1;
        } else {
            return this.this$0.bufferField.readByte() & UByte.MAX_VALUE;
        }
    }

    public int read(byte[] data, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter(data, "data");
        if (!this.this$0.closed) {
            Util.checkOffsetAndCount((long) data.length, (long) offset, (long) byteCount);
            if (this.this$0.bufferField.size() == 0 && this.this$0.source.read(this.this$0.bufferField, (long) 8192) == -1) {
                return -1;
            }
            return this.this$0.bufferField.read(data, offset, byteCount);
        }
        throw new IOException("closed");
    }

    public String toString() {
        return this.this$0 + ".inputStream()";
    }
}

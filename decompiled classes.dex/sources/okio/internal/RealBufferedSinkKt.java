package okio.internal;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import java.io.EOFException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.RealBufferedSink;
import okio.Source;
import okio.Timeout;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0015\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\b\u001a\r\u0010\u0003\u001a\u00020\u0004*\u00020\u0002H\b\u001a\r\u0010\u0005\u001a\u00020\u0004*\u00020\u0002H\b\u001a\r\u0010\u0006\u001a\u00020\u0001*\u00020\u0002H\b\u001a\r\u0010\u0007\u001a\u00020\b*\u00020\u0002H\b\u001a\r\u0010\t\u001a\u00020\n*\u00020\u0002H\b\u001a\u0015\u0010\u000b\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\f\u001a\u00020\rH\b\u001a%\u0010\u000b\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\b\u001a\u001d\u0010\u000b\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\f\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u0012H\b\u001a\u0015\u0010\u000b\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0014H\b\u001a%\u0010\u000b\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\b\u001a\u001d\u0010\u000b\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\f\u001a\u00020\u00152\u0006\u0010\u0010\u001a\u00020\u0012H\b\u001a\u0015\u0010\u0016\u001a\u00020\u0012*\u00020\u00022\u0006\u0010\f\u001a\u00020\u0015H\b\u001a\u0015\u0010\u0017\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u000fH\b\u001a\u0015\u0010\u0019\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0012H\b\u001a\u0015\u0010\u001b\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0012H\b\u001a\u0015\u0010\u001c\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u000fH\b\u001a\u0015\u0010\u001e\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u000fH\b\u001a\u0015\u0010\u001f\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0012H\b\u001a\u0015\u0010 \u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0012H\b\u001a\u0015\u0010!\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\"\u001a\u00020\u000fH\b\u001a\u0015\u0010#\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\"\u001a\u00020\u000fH\b\u001a\u0015\u0010$\u001a\u00020\u0004*\u00020\u00022\u0006\u0010%\u001a\u00020\nH\b\u001a%\u0010$\u001a\u00020\u0004*\u00020\u00022\u0006\u0010%\u001a\u00020\n2\u0006\u0010&\u001a\u00020\u000f2\u0006\u0010'\u001a\u00020\u000fH\b\u001a\u0015\u0010(\u001a\u00020\u0004*\u00020\u00022\u0006\u0010)\u001a\u00020\u000fH\b¨\u0006*"}, d2 = {"commonClose", "", "Lokio/RealBufferedSink;", "commonEmit", "Lokio/BufferedSink;", "commonEmitCompleteSegments", "commonFlush", "commonTimeout", "Lokio/Timeout;", "commonToString", "", "commonWrite", "source", "", "offset", "", "byteCount", "Lokio/Buffer;", "", "byteString", "Lokio/ByteString;", "Lokio/Source;", "commonWriteAll", "commonWriteByte", "b", "commonWriteDecimalLong", "v", "commonWriteHexadecimalUnsignedLong", "commonWriteInt", "i", "commonWriteIntLe", "commonWriteLong", "commonWriteLongLe", "commonWriteShort", "s", "commonWriteShortLe", "commonWriteUtf8", "string", "beginIndex", "endIndex", "commonWriteUtf8CodePoint", "codePoint", "okio"}, k = 2, mv = {1, 4, 0})
/* compiled from: RealBufferedSink.kt */
public final class RealBufferedSinkKt {
    public static final void commonClose(RealBufferedSink $this$commonClose) {
        Intrinsics.checkNotNullParameter($this$commonClose, "$this$commonClose");
        if (!$this$commonClose.closed) {
            Throwable th = null;
            try {
                if ($this$commonClose.bufferField.size() > 0) {
                    $this$commonClose.sink.write($this$commonClose.bufferField, $this$commonClose.bufferField.size());
                }
            } catch (Throwable th2) {
                th = th2;
            }
            try {
                $this$commonClose.sink.close();
            } catch (Throwable th3) {
                if (th == null) {
                    th = th3;
                }
            }
            $this$commonClose.closed = true;
            if (th != null) {
                throw th;
            }
        }
    }

    public static final BufferedSink commonEmit(RealBufferedSink $this$commonEmit) {
        Intrinsics.checkNotNullParameter($this$commonEmit, "$this$commonEmit");
        if (!$this$commonEmit.closed) {
            long size = $this$commonEmit.bufferField.size();
            if (size > 0) {
                $this$commonEmit.sink.write($this$commonEmit.bufferField, size);
            }
            return $this$commonEmit;
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final BufferedSink commonEmitCompleteSegments(RealBufferedSink $this$commonEmitCompleteSegments) {
        Intrinsics.checkNotNullParameter($this$commonEmitCompleteSegments, "$this$commonEmitCompleteSegments");
        if (!$this$commonEmitCompleteSegments.closed) {
            long completeSegmentByteCount = $this$commonEmitCompleteSegments.bufferField.completeSegmentByteCount();
            if (completeSegmentByteCount > 0) {
                $this$commonEmitCompleteSegments.sink.write($this$commonEmitCompleteSegments.bufferField, completeSegmentByteCount);
            }
            return $this$commonEmitCompleteSegments;
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final void commonFlush(RealBufferedSink $this$commonFlush) {
        Intrinsics.checkNotNullParameter($this$commonFlush, "$this$commonFlush");
        if (!$this$commonFlush.closed) {
            if ($this$commonFlush.bufferField.size() > 0) {
                $this$commonFlush.sink.write($this$commonFlush.bufferField, $this$commonFlush.bufferField.size());
            }
            $this$commonFlush.sink.flush();
            return;
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final Timeout commonTimeout(RealBufferedSink $this$commonTimeout) {
        Intrinsics.checkNotNullParameter($this$commonTimeout, "$this$commonTimeout");
        return $this$commonTimeout.sink.timeout();
    }

    public static final String commonToString(RealBufferedSink $this$commonToString) {
        Intrinsics.checkNotNullParameter($this$commonToString, "$this$commonToString");
        return "buffer(" + $this$commonToString.sink + ')';
    }

    public static final BufferedSink commonWrite(RealBufferedSink $this$commonWrite, ByteString byteString) {
        Intrinsics.checkNotNullParameter($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(byteString, "byteString");
        if (!$this$commonWrite.closed) {
            $this$commonWrite.bufferField.write(byteString);
            return $this$commonWrite.emitCompleteSegments();
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final BufferedSink commonWrite(RealBufferedSink $this$commonWrite, ByteString byteString, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(byteString, "byteString");
        if (!$this$commonWrite.closed) {
            $this$commonWrite.bufferField.write(byteString, offset, byteCount);
            return $this$commonWrite.emitCompleteSegments();
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final BufferedSink commonWrite(RealBufferedSink $this$commonWrite, Source source, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(source, "source");
        long j = byteCount;
        while (j > 0) {
            long read = source.read($this$commonWrite.bufferField, j);
            if (read != -1) {
                j -= read;
                $this$commonWrite.emitCompleteSegments();
            } else {
                throw new EOFException();
            }
        }
        return $this$commonWrite;
    }

    public static final BufferedSink commonWrite(RealBufferedSink $this$commonWrite, byte[] source) {
        Intrinsics.checkNotNullParameter($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(source, "source");
        if (!$this$commonWrite.closed) {
            $this$commonWrite.bufferField.write(source);
            return $this$commonWrite.emitCompleteSegments();
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final BufferedSink commonWrite(RealBufferedSink $this$commonWrite, byte[] source, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(source, "source");
        if (!$this$commonWrite.closed) {
            $this$commonWrite.bufferField.write(source, offset, byteCount);
            return $this$commonWrite.emitCompleteSegments();
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final void commonWrite(RealBufferedSink $this$commonWrite, Buffer source, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(source, "source");
        if (!$this$commonWrite.closed) {
            $this$commonWrite.bufferField.write(source, byteCount);
            $this$commonWrite.emitCompleteSegments();
            return;
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final long commonWriteAll(RealBufferedSink $this$commonWriteAll, Source source) {
        Intrinsics.checkNotNullParameter($this$commonWriteAll, "$this$commonWriteAll");
        Intrinsics.checkNotNullParameter(source, "source");
        long j = 0;
        while (true) {
            long read = source.read($this$commonWriteAll.bufferField, (long) 8192);
            if (read == -1) {
                return j;
            }
            j += read;
            $this$commonWriteAll.emitCompleteSegments();
        }
    }

    public static final BufferedSink commonWriteByte(RealBufferedSink $this$commonWriteByte, int b) {
        Intrinsics.checkNotNullParameter($this$commonWriteByte, "$this$commonWriteByte");
        if (!$this$commonWriteByte.closed) {
            $this$commonWriteByte.bufferField.writeByte(b);
            return $this$commonWriteByte.emitCompleteSegments();
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final BufferedSink commonWriteDecimalLong(RealBufferedSink $this$commonWriteDecimalLong, long v) {
        Intrinsics.checkNotNullParameter($this$commonWriteDecimalLong, "$this$commonWriteDecimalLong");
        if (!$this$commonWriteDecimalLong.closed) {
            $this$commonWriteDecimalLong.bufferField.writeDecimalLong(v);
            return $this$commonWriteDecimalLong.emitCompleteSegments();
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final BufferedSink commonWriteHexadecimalUnsignedLong(RealBufferedSink $this$commonWriteHexadecimalUnsignedLong, long v) {
        Intrinsics.checkNotNullParameter($this$commonWriteHexadecimalUnsignedLong, "$this$commonWriteHexadecimalUnsignedLong");
        if (!$this$commonWriteHexadecimalUnsignedLong.closed) {
            $this$commonWriteHexadecimalUnsignedLong.bufferField.writeHexadecimalUnsignedLong(v);
            return $this$commonWriteHexadecimalUnsignedLong.emitCompleteSegments();
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final BufferedSink commonWriteInt(RealBufferedSink $this$commonWriteInt, int i) {
        Intrinsics.checkNotNullParameter($this$commonWriteInt, "$this$commonWriteInt");
        if (!$this$commonWriteInt.closed) {
            $this$commonWriteInt.bufferField.writeInt(i);
            return $this$commonWriteInt.emitCompleteSegments();
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final BufferedSink commonWriteIntLe(RealBufferedSink $this$commonWriteIntLe, int i) {
        Intrinsics.checkNotNullParameter($this$commonWriteIntLe, "$this$commonWriteIntLe");
        if (!$this$commonWriteIntLe.closed) {
            $this$commonWriteIntLe.bufferField.writeIntLe(i);
            return $this$commonWriteIntLe.emitCompleteSegments();
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final BufferedSink commonWriteLong(RealBufferedSink $this$commonWriteLong, long v) {
        Intrinsics.checkNotNullParameter($this$commonWriteLong, "$this$commonWriteLong");
        if (!$this$commonWriteLong.closed) {
            $this$commonWriteLong.bufferField.writeLong(v);
            return $this$commonWriteLong.emitCompleteSegments();
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final BufferedSink commonWriteLongLe(RealBufferedSink $this$commonWriteLongLe, long v) {
        Intrinsics.checkNotNullParameter($this$commonWriteLongLe, "$this$commonWriteLongLe");
        if (!$this$commonWriteLongLe.closed) {
            $this$commonWriteLongLe.bufferField.writeLongLe(v);
            return $this$commonWriteLongLe.emitCompleteSegments();
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final BufferedSink commonWriteShort(RealBufferedSink $this$commonWriteShort, int s) {
        Intrinsics.checkNotNullParameter($this$commonWriteShort, "$this$commonWriteShort");
        if (!$this$commonWriteShort.closed) {
            $this$commonWriteShort.bufferField.writeShort(s);
            return $this$commonWriteShort.emitCompleteSegments();
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final BufferedSink commonWriteShortLe(RealBufferedSink $this$commonWriteShortLe, int s) {
        Intrinsics.checkNotNullParameter($this$commonWriteShortLe, "$this$commonWriteShortLe");
        if (!$this$commonWriteShortLe.closed) {
            $this$commonWriteShortLe.bufferField.writeShortLe(s);
            return $this$commonWriteShortLe.emitCompleteSegments();
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final BufferedSink commonWriteUtf8(RealBufferedSink $this$commonWriteUtf8, String string) {
        Intrinsics.checkNotNullParameter($this$commonWriteUtf8, "$this$commonWriteUtf8");
        Intrinsics.checkNotNullParameter(string, TypedValues.Custom.S_STRING);
        if (!$this$commonWriteUtf8.closed) {
            $this$commonWriteUtf8.bufferField.writeUtf8(string);
            return $this$commonWriteUtf8.emitCompleteSegments();
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final BufferedSink commonWriteUtf8(RealBufferedSink $this$commonWriteUtf8, String string, int beginIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$commonWriteUtf8, "$this$commonWriteUtf8");
        Intrinsics.checkNotNullParameter(string, TypedValues.Custom.S_STRING);
        if (!$this$commonWriteUtf8.closed) {
            $this$commonWriteUtf8.bufferField.writeUtf8(string, beginIndex, endIndex);
            return $this$commonWriteUtf8.emitCompleteSegments();
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final BufferedSink commonWriteUtf8CodePoint(RealBufferedSink $this$commonWriteUtf8CodePoint, int codePoint) {
        Intrinsics.checkNotNullParameter($this$commonWriteUtf8CodePoint, "$this$commonWriteUtf8CodePoint");
        if (!$this$commonWriteUtf8CodePoint.closed) {
            $this$commonWriteUtf8CodePoint.bufferField.writeUtf8CodePoint(codePoint);
            return $this$commonWriteUtf8CodePoint.emitCompleteSegments();
        }
        throw new IllegalStateException("closed".toString());
    }
}

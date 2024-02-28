package okio;

import java.io.EOFException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import mt.Log1F380D;
import okio.internal.BufferKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\n\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\rH\u0016J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0012H\u0016J \u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u0012H\u0016J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0015\u001a\u00020\u0012H\u0016J\u0010\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u001a\u001a\u00020\u0018H\u0016J\u0018\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u0015\u001a\u00020\u0012H\u0016J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\b\u0010\u001d\u001a\u00020\rH\u0016J\b\u0010\u001e\u001a\u00020\u0001H\u0016J\u0018\u0010\u001f\u001a\u00020\r2\u0006\u0010 \u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J(\u0010\u001f\u001a\u00020\r2\u0006\u0010 \u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\"H\u0016J\u0010\u0010$\u001a\u00020\"2\u0006\u0010%\u001a\u00020&H\u0016J\u0010\u0010$\u001a\u00020\"2\u0006\u0010%\u001a\u00020'H\u0016J \u0010$\u001a\u00020\"2\u0006\u0010%\u001a\u00020'2\u0006\u0010 \u001a\u00020\"2\u0006\u0010#\u001a\u00020\"H\u0016J\u0018\u0010$\u001a\u00020\u00122\u0006\u0010%\u001a\u00020\u00062\u0006\u0010#\u001a\u00020\u0012H\u0016J\u0010\u0010(\u001a\u00020\u00122\u0006\u0010%\u001a\u00020)H\u0016J\b\u0010*\u001a\u00020\u0014H\u0016J\b\u0010+\u001a\u00020'H\u0016J\u0010\u0010+\u001a\u00020'2\u0006\u0010#\u001a\u00020\u0012H\u0016J\b\u0010,\u001a\u00020\u0018H\u0016J\u0010\u0010,\u001a\u00020\u00182\u0006\u0010#\u001a\u00020\u0012H\u0016J\b\u0010-\u001a\u00020\u0012H\u0016J\u0010\u0010.\u001a\u00020\u000f2\u0006\u0010%\u001a\u00020'H\u0016J\u0018\u0010.\u001a\u00020\u000f2\u0006\u0010%\u001a\u00020\u00062\u0006\u0010#\u001a\u00020\u0012H\u0016J\b\u0010/\u001a\u00020\u0012H\u0016J\b\u00100\u001a\u00020\"H\u0016J\b\u00101\u001a\u00020\"H\u0016J\b\u00102\u001a\u00020\u0012H\u0016J\b\u00103\u001a\u00020\u0012H\u0016J\b\u00104\u001a\u000205H\u0016J\b\u00106\u001a\u000205H\u0016J\u0010\u00107\u001a\u0002082\u0006\u00109\u001a\u00020:H\u0016J\u0018\u00107\u001a\u0002082\u0006\u0010#\u001a\u00020\u00122\u0006\u00109\u001a\u00020:H\u0016J\b\u0010;\u001a\u000208H\u0016J\u0010\u0010;\u001a\u0002082\u0006\u0010#\u001a\u00020\u0012H\u0016J\b\u0010<\u001a\u00020\"H\u0016J\n\u0010=\u001a\u0004\u0018\u000108H\u0016J\b\u0010>\u001a\u000208H\u0016J\u0010\u0010>\u001a\u0002082\u0006\u0010?\u001a\u00020\u0012H\u0016J\u0010\u0010@\u001a\u00020\r2\u0006\u0010#\u001a\u00020\u0012H\u0016J\u0010\u0010A\u001a\u00020\u000f2\u0006\u0010#\u001a\u00020\u0012H\u0016J\u0010\u0010B\u001a\u00020\"2\u0006\u0010C\u001a\u00020DH\u0016J\u0010\u0010E\u001a\u00020\u000f2\u0006\u0010#\u001a\u00020\u0012H\u0016J\b\u0010F\u001a\u00020GH\u0016J\b\u0010H\u001a\u000208H\u0016R\u001b\u0010\u0005\u001a\u00020\u00068Ö\u0002X\u0004¢\u0006\f\u0012\u0004\b\u0007\u0010\b\u001a\u0004\b\t\u0010\nR\u0010\u0010\u000b\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\f\u001a\u00020\r8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006I"}, d2 = {"Lokio/RealBufferedSource;", "Lokio/BufferedSource;", "source", "Lokio/Source;", "(Lokio/Source;)V", "buffer", "Lokio/Buffer;", "getBuffer$annotations", "()V", "getBuffer", "()Lokio/Buffer;", "bufferField", "closed", "", "close", "", "exhausted", "indexOf", "", "b", "", "fromIndex", "toIndex", "bytes", "Lokio/ByteString;", "indexOfElement", "targetBytes", "inputStream", "Ljava/io/InputStream;", "isOpen", "peek", "rangeEquals", "offset", "bytesOffset", "", "byteCount", "read", "sink", "Ljava/nio/ByteBuffer;", "", "readAll", "Lokio/Sink;", "readByte", "readByteArray", "readByteString", "readDecimalLong", "readFully", "readHexadecimalUnsignedLong", "readInt", "readIntLe", "readLong", "readLongLe", "readShort", "", "readShortLe", "readString", "", "charset", "Ljava/nio/charset/Charset;", "readUtf8", "readUtf8CodePoint", "readUtf8Line", "readUtf8LineStrict", "limit", "request", "require", "select", "options", "Lokio/Options;", "skip", "timeout", "Lokio/Timeout;", "toString", "okio"}, k = 1, mv = {1, 4, 0})
/* compiled from: 01EF */
public final class RealBufferedSource implements BufferedSource {
    public final Buffer bufferField = new Buffer();
    public boolean closed;
    public final Source source;

    public RealBufferedSource(Source source2) {
        Intrinsics.checkNotNullParameter(source2, "source");
        this.source = source2;
    }

    public static /* synthetic */ void getBuffer$annotations() {
    }

    public Buffer buffer() {
        return this.bufferField;
    }

    public void close() {
        if (!this.closed) {
            this.closed = true;
            this.source.close();
            this.bufferField.clear();
        }
    }

    public boolean exhausted() {
        if (!this.closed) {
            return this.bufferField.exhausted() && this.source.read(this.bufferField, (long) 8192) == -1;
        }
        throw new IllegalStateException("closed".toString());
    }

    public Buffer getBuffer() {
        return this.bufferField;
    }

    public long indexOf(byte b) {
        return indexOf(b, 0, Long.MAX_VALUE);
    }

    public long indexOf(byte b, long fromIndex) {
        return indexOf(b, fromIndex, Long.MAX_VALUE);
    }

    public long indexOf(byte b, long fromIndex, long toIndex) {
        long j = toIndex;
        long j2 = fromIndex;
        boolean z = true;
        if (!this.closed) {
            if (0 > j2 || j < j2) {
                z = false;
            }
            if (z) {
                long j3 = j2;
                while (j3 < j) {
                    long indexOf = this.bufferField.indexOf(b, j3, toIndex);
                    if (indexOf != -1) {
                        return indexOf;
                    }
                    long size = this.bufferField.size();
                    if (size >= j || this.source.read(this.bufferField, (long) 8192) == -1) {
                        return -1;
                    }
                    j3 = Math.max(j3, size);
                }
                return -1;
            }
            throw new IllegalArgumentException(("fromIndex=" + j2 + " toIndex=" + j).toString());
        }
        throw new IllegalStateException("closed".toString());
    }

    public long indexOf(ByteString bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return indexOf(bytes, 0);
    }

    public long indexOf(ByteString bytes, long fromIndex) {
        ByteString byteString = bytes;
        Intrinsics.checkNotNullParameter(byteString, "bytes");
        long j = fromIndex;
        if (!this.closed) {
            while (true) {
                long indexOf = this.bufferField.indexOf(byteString, j);
                if (indexOf != -1) {
                    return indexOf;
                }
                long size = this.bufferField.size();
                if (this.source.read(this.bufferField, (long) 8192) == -1) {
                    return -1;
                }
                j = Math.max(j, (size - ((long) bytes.size())) + 1);
            }
        } else {
            throw new IllegalStateException("closed".toString());
        }
    }

    public long indexOfElement(ByteString targetBytes) {
        Intrinsics.checkNotNullParameter(targetBytes, "targetBytes");
        return indexOfElement(targetBytes, 0);
    }

    public long indexOfElement(ByteString targetBytes, long fromIndex) {
        ByteString byteString = targetBytes;
        Intrinsics.checkNotNullParameter(byteString, "targetBytes");
        long j = fromIndex;
        if (!this.closed) {
            while (true) {
                long indexOfElement = this.bufferField.indexOfElement(byteString, j);
                if (indexOfElement != -1) {
                    return indexOfElement;
                }
                long size = this.bufferField.size();
                if (this.source.read(this.bufferField, (long) 8192) == -1) {
                    return -1;
                }
                j = Math.max(j, size);
            }
        } else {
            throw new IllegalStateException("closed".toString());
        }
    }

    public InputStream inputStream() {
        return new RealBufferedSource$inputStream$1(this);
    }

    public boolean isOpen() {
        return !this.closed;
    }

    public BufferedSource peek() {
        return Okio.buffer((Source) new PeekSource(this));
    }

    public boolean rangeEquals(long offset, ByteString bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return rangeEquals(offset, bytes, 0, bytes.size());
    }

    public boolean rangeEquals(long offset, ByteString bytes, int bytesOffset, int byteCount) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (!(!this.closed)) {
            throw new IllegalStateException("closed".toString());
        } else if (offset < 0 || bytesOffset < 0 || byteCount < 0 || bytes.size() - bytesOffset < byteCount) {
            return false;
        } else {
            for (int i = 0; i < byteCount; i++) {
                long j = ((long) i) + offset;
                if (!request(1 + j)) {
                    return false;
                }
                if (this.bufferField.getByte(j) != bytes.getByte(bytesOffset + i)) {
                    return false;
                }
            }
            return true;
        }
    }

    public int read(ByteBuffer sink) {
        Intrinsics.checkNotNullParameter(sink, "sink");
        if (this.bufferField.size() == 0 && this.source.read(this.bufferField, (long) 8192) == -1) {
            return -1;
        }
        return this.bufferField.read(sink);
    }

    public int read(byte[] sink) {
        Intrinsics.checkNotNullParameter(sink, "sink");
        return read(sink, 0, sink.length);
    }

    public int read(byte[] sink, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter(sink, "sink");
        Util.checkOffsetAndCount((long) sink.length, (long) offset, (long) byteCount);
        if (this.bufferField.size() == 0 && this.source.read(this.bufferField, (long) 8192) == -1) {
            return -1;
        }
        long j = (long) byteCount;
        return this.bufferField.read(sink, offset, (int) Math.min(j, this.bufferField.size()));
    }

    public long read(Buffer sink, long byteCount) {
        Intrinsics.checkNotNullParameter(sink, "sink");
        if (!(byteCount >= 0)) {
            throw new IllegalArgumentException(("byteCount < 0: " + byteCount).toString());
        } else if (!(!this.closed)) {
            throw new IllegalStateException("closed".toString());
        } else if (this.bufferField.size() == 0 && this.source.read(this.bufferField, (long) 8192) == -1) {
            return -1;
        } else {
            return this.bufferField.read(sink, Math.min(byteCount, this.bufferField.size()));
        }
    }

    public long readAll(Sink sink) {
        Intrinsics.checkNotNullParameter(sink, "sink");
        long j = 0;
        while (this.source.read(this.bufferField, (long) 8192) != -1) {
            long completeSegmentByteCount = this.bufferField.completeSegmentByteCount();
            if (completeSegmentByteCount > 0) {
                j += completeSegmentByteCount;
                sink.write(this.bufferField, completeSegmentByteCount);
            }
        }
        if (this.bufferField.size() <= 0) {
            return j;
        }
        long size = j + this.bufferField.size();
        sink.write(this.bufferField, this.bufferField.size());
        return size;
    }

    public byte readByte() {
        require(1);
        return this.bufferField.readByte();
    }

    public byte[] readByteArray() {
        this.bufferField.writeAll(this.source);
        return this.bufferField.readByteArray();
    }

    public byte[] readByteArray(long byteCount) {
        require(byteCount);
        return this.bufferField.readByteArray(byteCount);
    }

    public ByteString readByteString() {
        this.bufferField.writeAll(this.source);
        return this.bufferField.readByteString();
    }

    public ByteString readByteString(long byteCount) {
        require(byteCount);
        return this.bufferField.readByteString(byteCount);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0033, code lost:
        if (r4 == 0) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0036, code lost:
        r3 = new java.lang.StringBuilder().append("Expected leading [0-9] or '-' character but was 0x");
        r7 = java.lang.Integer.toString(r6, kotlin.text.CharsKt.checkRadix(kotlin.text.CharsKt.checkRadix(16)));
        mt.Log1F380D.a((java.lang.Object) r7);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r7, "java.lang.Integer.toStri…(this, checkRadix(radix))");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0066, code lost:
        throw new java.lang.NumberFormatException(r3.append(r7).toString());
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long readDecimalLong() {
        /*
            r10 = this;
            r0 = r10
            r1 = 0
            r2 = 1
            r0.require(r2)
            r4 = 0
        L_0x0009:
            long r6 = r4 + r2
            boolean r6 = r0.request(r6)
            if (r6 == 0) goto L_0x0067
            r6 = r0
            r7 = 0
            okio.Buffer r6 = r6.bufferField
            byte r6 = r6.getByte(r4)
            r7 = 48
            byte r7 = (byte) r7
            if (r6 < r7) goto L_0x0023
            r7 = 57
            byte r7 = (byte) r7
            if (r6 <= r7) goto L_0x002f
        L_0x0023:
            r7 = 0
            int r9 = (r4 > r7 ? 1 : (r4 == r7 ? 0 : -1))
            if (r9 != 0) goto L_0x0031
            r9 = 45
            byte r9 = (byte) r9
            if (r6 == r9) goto L_0x002f
            goto L_0x0031
        L_0x002f:
            long r4 = r4 + r2
            goto L_0x0009
        L_0x0031:
            int r2 = (r4 > r7 ? 1 : (r4 == r7 ? 0 : -1))
            if (r2 == 0) goto L_0x0036
            goto L_0x0067
        L_0x0036:
            java.lang.NumberFormatException r2 = new java.lang.NumberFormatException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r7 = "Expected leading [0-9] or '-' character but was 0x"
            java.lang.StringBuilder r3 = r3.append(r7)
            r7 = 16
            int r7 = kotlin.text.CharsKt.checkRadix(r7)
            int r7 = kotlin.text.CharsKt.checkRadix(r7)
            java.lang.String r7 = java.lang.Integer.toString(r6, r7)
            mt.Log1F380D.a((java.lang.Object) r7)
            java.lang.String r8 = "java.lang.Integer.toStri…(this, checkRadix(radix))"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r7, r8)
            java.lang.StringBuilder r3 = r3.append(r7)
            java.lang.String r3 = r3.toString()
            r2.<init>(r3)
            java.lang.Throwable r2 = (java.lang.Throwable) r2
            throw r2
        L_0x0067:
            r2 = r0
            r3 = 0
            okio.Buffer r2 = r2.bufferField
            long r0 = r2.readDecimalLong()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.RealBufferedSource.readDecimalLong():long");
    }

    public void readFully(Buffer sink, long byteCount) {
        Intrinsics.checkNotNullParameter(sink, "sink");
        try {
            require(byteCount);
            this.bufferField.readFully(sink, byteCount);
        } catch (EOFException e) {
            sink.writeAll(this.bufferField);
            throw e;
        }
    }

    public void readFully(byte[] sink) {
        Intrinsics.checkNotNullParameter(sink, "sink");
        try {
            require((long) sink.length);
            this.bufferField.readFully(sink);
        } catch (EOFException e) {
            int i = 0;
            while (this.bufferField.size() > 0) {
                int read = this.bufferField.read(sink, i, (int) this.bufferField.size());
                if (read != -1) {
                    i += read;
                } else {
                    throw new AssertionError();
                }
            }
            throw e;
        }
    }

    public int readInt() {
        require(4);
        return this.bufferField.readInt();
    }

    public int readIntLe() {
        require(4);
        return this.bufferField.readIntLe();
    }

    public long readLong() {
        require(8);
        return this.bufferField.readLong();
    }

    public long readLongLe() {
        require(8);
        return this.bufferField.readLongLe();
    }

    public short readShort() {
        require(2);
        return this.bufferField.readShort();
    }

    public short readShortLe() {
        require(2);
        return this.bufferField.readShortLe();
    }

    public String readString(long byteCount, Charset charset) {
        Intrinsics.checkNotNullParameter(charset, "charset");
        require(byteCount);
        return this.bufferField.readString(byteCount, charset);
    }

    public String readString(Charset charset) {
        Intrinsics.checkNotNullParameter(charset, "charset");
        this.bufferField.writeAll(this.source);
        return this.bufferField.readString(charset);
    }

    public String readUtf8() {
        this.bufferField.writeAll(this.source);
        return this.bufferField.readUtf8();
    }

    public String readUtf8(long byteCount) {
        require(byteCount);
        return this.bufferField.readUtf8(byteCount);
    }

    public int readUtf8CodePoint() {
        require(1);
        byte b = this.bufferField.getByte(0);
        if ((b & 224) == 192) {
            require(2);
        } else if ((b & 240) == 224) {
            require(3);
        } else if ((b & 248) == 240) {
            require(4);
        }
        return this.bufferField.readUtf8CodePoint();
    }

    public String readUtf8LineStrict() {
        return readUtf8LineStrict(Long.MAX_VALUE);
    }

    public boolean request(long byteCount) {
        if (!(byteCount >= 0)) {
            throw new IllegalArgumentException(("byteCount < 0: " + byteCount).toString());
        } else if (!this.closed) {
            while (this.bufferField.size() < byteCount) {
                if (this.source.read(this.bufferField, (long) 8192) == -1) {
                    return false;
                }
            }
            return true;
        } else {
            throw new IllegalStateException("closed".toString());
        }
    }

    public void require(long byteCount) {
        if (!request(byteCount)) {
            throw new EOFException();
        }
    }

    public int select(Options options) {
        Intrinsics.checkNotNullParameter(options, "options");
        if (!this.closed) {
            do {
                int selectPrefix = BufferKt.selectPrefix(this.bufferField, options, true);
                switch (selectPrefix) {
                    case -2:
                        break;
                    case -1:
                        return -1;
                    default:
                        this.bufferField.skip((long) options.getByteStrings$okio()[selectPrefix].size());
                        return selectPrefix;
                }
            } while (this.source.read(this.bufferField, (long) 8192) != -1);
            return -1;
        }
        throw new IllegalStateException("closed".toString());
    }

    public void skip(long byteCount) {
        long j = byteCount;
        if (!this.closed) {
            while (j > 0) {
                if (this.bufferField.size() == 0 && this.source.read(this.bufferField, (long) 8192) == -1) {
                    throw new EOFException();
                }
                long min = Math.min(j, this.bufferField.size());
                this.bufferField.skip(min);
                j -= min;
            }
            return;
        }
        throw new IllegalStateException("closed".toString());
    }

    public Timeout timeout() {
        return this.source.timeout();
    }

    public String toString() {
        return "buffer(" + this.source + ')';
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0043  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long readHexadecimalUnsignedLong() {
        /*
            r8 = this;
            r0 = r8
            r1 = 0
            r2 = 1
            r0.require(r2)
            r2 = 0
        L_0x0008:
            int r3 = r2 + 1
            long r3 = (long) r3
            boolean r3 = r0.request(r3)
            if (r3 == 0) goto L_0x0074
            r3 = r0
            r4 = 0
            okio.Buffer r3 = r3.bufferField
            long r4 = (long) r2
            byte r3 = r3.getByte(r4)
            r4 = 48
            byte r4 = (byte) r4
            if (r3 < r4) goto L_0x0027
            r4 = 57
            byte r4 = (byte) r4
            if (r3 <= r4) goto L_0x003c
        L_0x0027:
            r4 = 97
            byte r4 = (byte) r4
            if (r3 < r4) goto L_0x0031
            r4 = 102(0x66, float:1.43E-43)
            byte r4 = (byte) r4
            if (r3 <= r4) goto L_0x003c
        L_0x0031:
            r4 = 65
            byte r4 = (byte) r4
            if (r3 < r4) goto L_0x0040
            r4 = 70
            byte r4 = (byte) r4
            if (r3 <= r4) goto L_0x003c
            goto L_0x0040
        L_0x003c:
            int r2 = r2 + 1
            goto L_0x0008
        L_0x0040:
            if (r2 == 0) goto L_0x0043
            goto L_0x0074
        L_0x0043:
            java.lang.NumberFormatException r4 = new java.lang.NumberFormatException
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Expected leading [0-9a-fA-F] character but was 0x"
            java.lang.StringBuilder r5 = r5.append(r6)
            r6 = 16
            int r6 = kotlin.text.CharsKt.checkRadix(r6)
            int r6 = kotlin.text.CharsKt.checkRadix(r6)
            java.lang.String r6 = java.lang.Integer.toString(r3, r6)
            mt.Log1F380D.a((java.lang.Object) r6)
            java.lang.String r7 = "java.lang.Integer.toStri…(this, checkRadix(radix))"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r6, r7)
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r5 = r5.toString()
            r4.<init>(r5)
            java.lang.Throwable r4 = (java.lang.Throwable) r4
            throw r4
        L_0x0074:
            r3 = r0
            r4 = 0
            okio.Buffer r3 = r3.bufferField
            long r0 = r3.readHexadecimalUnsignedLong()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.RealBufferedSource.readHexadecimalUnsignedLong():long");
    }

    public String readUtf8Line() {
        long indexOf = indexOf((byte) 10);
        if (indexOf != -1) {
            String readUtf8Line = BufferKt.readUtf8Line(this.bufferField, indexOf);
            Log1F380D.a((Object) readUtf8Line);
            return readUtf8Line;
        } else if (this.bufferField.size() != 0) {
            return readUtf8(this.bufferField.size());
        } else {
            return null;
        }
    }

    public String readUtf8LineStrict(long limit) {
        long j = limit;
        if (j >= 0) {
            long j2 = j == Long.MAX_VALUE ? Long.MAX_VALUE : j + 1;
            byte b = (byte) 10;
            byte b2 = b;
            long indexOf = indexOf(b, 0, j2);
            if (indexOf != -1) {
                String readUtf8Line = BufferKt.readUtf8Line(this.bufferField, indexOf);
                Log1F380D.a((Object) readUtf8Line);
                return readUtf8Line;
            } else if (j2 >= Long.MAX_VALUE || !request(j2) || this.bufferField.getByte(j2 - 1) != ((byte) 13) || !request(1 + j2) || this.bufferField.getByte(j2) != b2) {
                Buffer buffer = new Buffer();
                this.bufferField.copyTo(buffer, 0, Math.min((long) 32, this.bufferField.size()));
                throw new EOFException("\\n not found: limit=" + Math.min(this.bufferField.size(), j) + " content=" + buffer.readByteString().hex() + "…");
            } else {
                String readUtf8Line2 = BufferKt.readUtf8Line(this.bufferField, j2);
                Log1F380D.a((Object) readUtf8Line2);
                return readUtf8Line2;
            }
        } else {
            throw new IllegalArgumentException(("limit < 0: " + j).toString());
        }
    }
}

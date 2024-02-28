package okio.internal;

import java.io.EOFException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import mt.Log1F380D;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import okio.Options;
import okio.PeekSource;
import okio.RealBufferedSource;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import okio.Util;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000j\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\n\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\b\u001a\r\u0010\u0003\u001a\u00020\u0004*\u00020\u0002H\b\u001a%\u0010\u0005\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u0006H\b\u001a\u001d\u0010\u0005\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\u0006H\b\u001a\u001d\u0010\r\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\u0006H\b\u001a\r\u0010\u000f\u001a\u00020\u0010*\u00020\u0002H\b\u001a-\u0010\u0011\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0014H\b\u001a%\u0010\u0016\u001a\u00020\u0014*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0012\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0014H\b\u001a\u001d\u0010\u0016\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u00192\u0006\u0010\u0015\u001a\u00020\u0006H\b\u001a\u0015\u0010\u001a\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u001bH\b\u001a\r\u0010\u001c\u001a\u00020\b*\u00020\u0002H\b\u001a\r\u0010\u001d\u001a\u00020\u0018*\u00020\u0002H\b\u001a\u0015\u0010\u001d\u001a\u00020\u0018*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0006H\b\u001a\r\u0010\u001e\u001a\u00020\f*\u00020\u0002H\b\u001a\u0015\u0010\u001e\u001a\u00020\f*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0006H\b\u001a\r\u0010\u001f\u001a\u00020\u0006*\u00020\u0002H\b\u001a\u0015\u0010 \u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u0018H\b\u001a\u001d\u0010 \u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u00192\u0006\u0010\u0015\u001a\u00020\u0006H\b\u001a\r\u0010!\u001a\u00020\u0006*\u00020\u0002H\b\u001a\r\u0010\"\u001a\u00020\u0014*\u00020\u0002H\b\u001a\r\u0010#\u001a\u00020\u0014*\u00020\u0002H\b\u001a\r\u0010$\u001a\u00020\u0006*\u00020\u0002H\b\u001a\r\u0010%\u001a\u00020\u0006*\u00020\u0002H\b\u001a\r\u0010&\u001a\u00020'*\u00020\u0002H\b\u001a\r\u0010(\u001a\u00020'*\u00020\u0002H\b\u001a\r\u0010)\u001a\u00020**\u00020\u0002H\b\u001a\u0015\u0010)\u001a\u00020**\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0006H\b\u001a\r\u0010+\u001a\u00020\u0014*\u00020\u0002H\b\u001a\u000f\u0010,\u001a\u0004\u0018\u00010**\u00020\u0002H\b\u001a\u0015\u0010-\u001a\u00020**\u00020\u00022\u0006\u0010.\u001a\u00020\u0006H\b\u001a\u0015\u0010/\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0006H\b\u001a\u0015\u00100\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0006H\b\u001a\u0015\u00101\u001a\u00020\u0014*\u00020\u00022\u0006\u00102\u001a\u000203H\b\u001a\u0015\u00104\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0006H\b\u001a\r\u00105\u001a\u000206*\u00020\u0002H\b\u001a\r\u00107\u001a\u00020**\u00020\u0002H\b¨\u00068"}, d2 = {"commonClose", "", "Lokio/RealBufferedSource;", "commonExhausted", "", "commonIndexOf", "", "b", "", "fromIndex", "toIndex", "bytes", "Lokio/ByteString;", "commonIndexOfElement", "targetBytes", "commonPeek", "Lokio/BufferedSource;", "commonRangeEquals", "offset", "bytesOffset", "", "byteCount", "commonRead", "sink", "", "Lokio/Buffer;", "commonReadAll", "Lokio/Sink;", "commonReadByte", "commonReadByteArray", "commonReadByteString", "commonReadDecimalLong", "commonReadFully", "commonReadHexadecimalUnsignedLong", "commonReadInt", "commonReadIntLe", "commonReadLong", "commonReadLongLe", "commonReadShort", "", "commonReadShortLe", "commonReadUtf8", "", "commonReadUtf8CodePoint", "commonReadUtf8Line", "commonReadUtf8LineStrict", "limit", "commonRequest", "commonRequire", "commonSelect", "options", "Lokio/Options;", "commonSkip", "commonTimeout", "Lokio/Timeout;", "commonToString", "okio"}, k = 2, mv = {1, 4, 0})
/* compiled from: 01F2 */
public final class RealBufferedSourceKt {
    public static final void commonClose(RealBufferedSource $this$commonClose) {
        Intrinsics.checkNotNullParameter($this$commonClose, "$this$commonClose");
        if (!$this$commonClose.closed) {
            $this$commonClose.closed = true;
            $this$commonClose.source.close();
            $this$commonClose.bufferField.clear();
        }
    }

    public static final boolean commonExhausted(RealBufferedSource $this$commonExhausted) {
        Intrinsics.checkNotNullParameter($this$commonExhausted, "$this$commonExhausted");
        if (!$this$commonExhausted.closed) {
            return $this$commonExhausted.bufferField.exhausted() && $this$commonExhausted.source.read($this$commonExhausted.bufferField, (long) 8192) == -1;
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final long commonIndexOf(RealBufferedSource $this$commonIndexOf, byte b, long fromIndex, long toIndex) {
        RealBufferedSource realBufferedSource = $this$commonIndexOf;
        long j = toIndex;
        Intrinsics.checkNotNullParameter(realBufferedSource, "$this$commonIndexOf");
        long j2 = fromIndex;
        boolean z = true;
        if (!realBufferedSource.closed) {
            if (0 > j2 || j < j2) {
                z = false;
            }
            if (z) {
                long j3 = j2;
                while (j3 < j) {
                    long indexOf = $this$commonIndexOf.bufferField.indexOf(b, j3, toIndex);
                    if (indexOf != -1) {
                        return indexOf;
                    }
                    long size = $this$commonIndexOf.bufferField.size();
                    if (size >= j || realBufferedSource.source.read($this$commonIndexOf.bufferField, (long) 8192) == -1) {
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

    public static final long commonIndexOf(RealBufferedSource $this$commonIndexOf, ByteString bytes, long fromIndex) {
        RealBufferedSource realBufferedSource = $this$commonIndexOf;
        ByteString byteString = bytes;
        Intrinsics.checkNotNullParameter($this$commonIndexOf, "$this$commonIndexOf");
        Intrinsics.checkNotNullParameter(byteString, "bytes");
        long j = fromIndex;
        if (!realBufferedSource.closed) {
            while (true) {
                long indexOf = $this$commonIndexOf.bufferField.indexOf(byteString, j);
                if (indexOf != -1) {
                    return indexOf;
                }
                long size = $this$commonIndexOf.bufferField.size();
                if (realBufferedSource.source.read($this$commonIndexOf.bufferField, (long) 8192) == -1) {
                    return -1;
                }
                j = Math.max(j, (size - ((long) bytes.size())) + 1);
            }
        } else {
            throw new IllegalStateException("closed".toString());
        }
    }

    public static final long commonIndexOfElement(RealBufferedSource $this$commonIndexOfElement, ByteString targetBytes, long fromIndex) {
        RealBufferedSource realBufferedSource = $this$commonIndexOfElement;
        ByteString byteString = targetBytes;
        Intrinsics.checkNotNullParameter($this$commonIndexOfElement, "$this$commonIndexOfElement");
        Intrinsics.checkNotNullParameter(byteString, "targetBytes");
        long j = fromIndex;
        if (!realBufferedSource.closed) {
            while (true) {
                long indexOfElement = $this$commonIndexOfElement.bufferField.indexOfElement(byteString, j);
                if (indexOfElement != -1) {
                    return indexOfElement;
                }
                long size = $this$commonIndexOfElement.bufferField.size();
                if (realBufferedSource.source.read($this$commonIndexOfElement.bufferField, (long) 8192) == -1) {
                    return -1;
                }
                j = Math.max(j, size);
            }
        } else {
            throw new IllegalStateException("closed".toString());
        }
    }

    public static final BufferedSource commonPeek(RealBufferedSource $this$commonPeek) {
        Intrinsics.checkNotNullParameter($this$commonPeek, "$this$commonPeek");
        return Okio.buffer((Source) new PeekSource($this$commonPeek));
    }

    public static final boolean commonRangeEquals(RealBufferedSource $this$commonRangeEquals, long offset, ByteString bytes, int bytesOffset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRangeEquals, "$this$commonRangeEquals");
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (!(!$this$commonRangeEquals.closed)) {
            throw new IllegalStateException("closed".toString());
        } else if (offset < 0 || bytesOffset < 0 || byteCount < 0 || bytes.size() - bytesOffset < byteCount) {
            return false;
        } else {
            for (int i = 0; i < byteCount; i++) {
                long j = ((long) i) + offset;
                if (!$this$commonRangeEquals.request(1 + j) || $this$commonRangeEquals.bufferField.getByte(j) != bytes.getByte(bytesOffset + i)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static final int commonRead(RealBufferedSource $this$commonRead, byte[] sink, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRead, "$this$commonRead");
        Intrinsics.checkNotNullParameter(sink, "sink");
        Util.checkOffsetAndCount((long) sink.length, (long) offset, (long) byteCount);
        if ($this$commonRead.bufferField.size() == 0 && $this$commonRead.source.read($this$commonRead.bufferField, (long) 8192) == -1) {
            return -1;
        }
        long j = (long) byteCount;
        return $this$commonRead.bufferField.read(sink, offset, (int) Math.min(j, $this$commonRead.bufferField.size()));
    }

    public static final long commonRead(RealBufferedSource $this$commonRead, Buffer sink, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRead, "$this$commonRead");
        Intrinsics.checkNotNullParameter(sink, "sink");
        if (!(byteCount >= 0)) {
            throw new IllegalArgumentException(("byteCount < 0: " + byteCount).toString());
        } else if (!(!$this$commonRead.closed)) {
            throw new IllegalStateException("closed".toString());
        } else if ($this$commonRead.bufferField.size() == 0 && $this$commonRead.source.read($this$commonRead.bufferField, (long) 8192) == -1) {
            return -1;
        } else {
            return $this$commonRead.bufferField.read(sink, Math.min(byteCount, $this$commonRead.bufferField.size()));
        }
    }

    public static final long commonReadAll(RealBufferedSource $this$commonReadAll, Sink sink) {
        Intrinsics.checkNotNullParameter($this$commonReadAll, "$this$commonReadAll");
        Intrinsics.checkNotNullParameter(sink, "sink");
        long j = 0;
        while ($this$commonReadAll.source.read($this$commonReadAll.bufferField, (long) 8192) != -1) {
            long completeSegmentByteCount = $this$commonReadAll.bufferField.completeSegmentByteCount();
            if (completeSegmentByteCount > 0) {
                j += completeSegmentByteCount;
                sink.write($this$commonReadAll.bufferField, completeSegmentByteCount);
            }
        }
        if ($this$commonReadAll.bufferField.size() <= 0) {
            return j;
        }
        long size = j + $this$commonReadAll.bufferField.size();
        sink.write($this$commonReadAll.bufferField, $this$commonReadAll.bufferField.size());
        return size;
    }

    public static final byte commonReadByte(RealBufferedSource $this$commonReadByte) {
        Intrinsics.checkNotNullParameter($this$commonReadByte, "$this$commonReadByte");
        $this$commonReadByte.require(1);
        return $this$commonReadByte.bufferField.readByte();
    }

    public static final byte[] commonReadByteArray(RealBufferedSource $this$commonReadByteArray) {
        Intrinsics.checkNotNullParameter($this$commonReadByteArray, "$this$commonReadByteArray");
        $this$commonReadByteArray.bufferField.writeAll($this$commonReadByteArray.source);
        return $this$commonReadByteArray.bufferField.readByteArray();
    }

    public static final byte[] commonReadByteArray(RealBufferedSource $this$commonReadByteArray, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonReadByteArray, "$this$commonReadByteArray");
        $this$commonReadByteArray.require(byteCount);
        return $this$commonReadByteArray.bufferField.readByteArray(byteCount);
    }

    public static final ByteString commonReadByteString(RealBufferedSource $this$commonReadByteString) {
        Intrinsics.checkNotNullParameter($this$commonReadByteString, "$this$commonReadByteString");
        $this$commonReadByteString.bufferField.writeAll($this$commonReadByteString.source);
        return $this$commonReadByteString.bufferField.readByteString();
    }

    public static final ByteString commonReadByteString(RealBufferedSource $this$commonReadByteString, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonReadByteString, "$this$commonReadByteString");
        $this$commonReadByteString.require(byteCount);
        return $this$commonReadByteString.bufferField.readByteString(byteCount);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0037, code lost:
        if (r3 == 0) goto L_0x003a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003a, code lost:
        r2 = new java.lang.StringBuilder().append("Expected leading [0-9] or '-' character but was 0x");
        r6 = java.lang.Integer.toString(r5, kotlin.text.CharsKt.checkRadix(kotlin.text.CharsKt.checkRadix(16)));
        mt.Log1F380D.a((java.lang.Object) r6);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r6, "java.lang.Integer.toStri…(this, checkRadix(radix))");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x006a, code lost:
        throw new java.lang.NumberFormatException(r2.append(r6).toString());
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final long commonReadDecimalLong(okio.RealBufferedSource r9) {
        /*
            r0 = 0
            java.lang.String r1 = "$this$commonReadDecimalLong"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r9, r1)
            r1 = 1
            r9.require(r1)
            r3 = 0
        L_0x000d:
            long r5 = r3 + r1
            boolean r5 = r9.request(r5)
            if (r5 == 0) goto L_0x006b
            r5 = r9
            r6 = 0
            okio.Buffer r5 = r5.bufferField
            byte r5 = r5.getByte(r3)
            r6 = 48
            byte r6 = (byte) r6
            if (r5 < r6) goto L_0x0027
            r6 = 57
            byte r6 = (byte) r6
            if (r5 <= r6) goto L_0x0033
        L_0x0027:
            r6 = 0
            int r8 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1))
            if (r8 != 0) goto L_0x0035
            r8 = 45
            byte r8 = (byte) r8
            if (r5 == r8) goto L_0x0033
            goto L_0x0035
        L_0x0033:
            long r3 = r3 + r1
            goto L_0x000d
        L_0x0035:
            int r1 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1))
            if (r1 == 0) goto L_0x003a
            goto L_0x006b
        L_0x003a:
            java.lang.NumberFormatException r1 = new java.lang.NumberFormatException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r6 = "Expected leading [0-9] or '-' character but was 0x"
            java.lang.StringBuilder r2 = r2.append(r6)
            r6 = 16
            int r6 = kotlin.text.CharsKt.checkRadix(r6)
            int r6 = kotlin.text.CharsKt.checkRadix(r6)
            java.lang.String r6 = java.lang.Integer.toString(r5, r6)
            mt.Log1F380D.a((java.lang.Object) r6)
            java.lang.String r7 = "java.lang.Integer.toStri…(this, checkRadix(radix))"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r6, r7)
            java.lang.StringBuilder r2 = r2.append(r6)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            java.lang.Throwable r1 = (java.lang.Throwable) r1
            throw r1
        L_0x006b:
            r1 = r9
            r2 = 0
            okio.Buffer r1 = r1.bufferField
            long r1 = r1.readDecimalLong()
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.internal.RealBufferedSourceKt.commonReadDecimalLong(okio.RealBufferedSource):long");
    }

    public static final void commonReadFully(RealBufferedSource $this$commonReadFully, Buffer sink, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonReadFully, "$this$commonReadFully");
        Intrinsics.checkNotNullParameter(sink, "sink");
        try {
            $this$commonReadFully.require(byteCount);
            $this$commonReadFully.bufferField.readFully(sink, byteCount);
        } catch (EOFException e) {
            sink.writeAll($this$commonReadFully.bufferField);
            throw e;
        }
    }

    public static final void commonReadFully(RealBufferedSource $this$commonReadFully, byte[] sink) {
        Intrinsics.checkNotNullParameter($this$commonReadFully, "$this$commonReadFully");
        Intrinsics.checkNotNullParameter(sink, "sink");
        try {
            $this$commonReadFully.require((long) sink.length);
            $this$commonReadFully.bufferField.readFully(sink);
        } catch (EOFException e) {
            int i = 0;
            while ($this$commonReadFully.bufferField.size() > 0) {
                int read = $this$commonReadFully.bufferField.read(sink, i, (int) $this$commonReadFully.bufferField.size());
                if (read != -1) {
                    i += read;
                } else {
                    throw new AssertionError();
                }
            }
            throw e;
        }
    }

    public static final int commonReadInt(RealBufferedSource $this$commonReadInt) {
        Intrinsics.checkNotNullParameter($this$commonReadInt, "$this$commonReadInt");
        $this$commonReadInt.require(4);
        return $this$commonReadInt.bufferField.readInt();
    }

    public static final int commonReadIntLe(RealBufferedSource $this$commonReadIntLe) {
        Intrinsics.checkNotNullParameter($this$commonReadIntLe, "$this$commonReadIntLe");
        $this$commonReadIntLe.require(4);
        return $this$commonReadIntLe.bufferField.readIntLe();
    }

    public static final long commonReadLong(RealBufferedSource $this$commonReadLong) {
        Intrinsics.checkNotNullParameter($this$commonReadLong, "$this$commonReadLong");
        $this$commonReadLong.require(8);
        return $this$commonReadLong.bufferField.readLong();
    }

    public static final long commonReadLongLe(RealBufferedSource $this$commonReadLongLe) {
        Intrinsics.checkNotNullParameter($this$commonReadLongLe, "$this$commonReadLongLe");
        $this$commonReadLongLe.require(8);
        return $this$commonReadLongLe.bufferField.readLongLe();
    }

    public static final short commonReadShort(RealBufferedSource $this$commonReadShort) {
        Intrinsics.checkNotNullParameter($this$commonReadShort, "$this$commonReadShort");
        $this$commonReadShort.require(2);
        return $this$commonReadShort.bufferField.readShort();
    }

    public static final short commonReadShortLe(RealBufferedSource $this$commonReadShortLe) {
        Intrinsics.checkNotNullParameter($this$commonReadShortLe, "$this$commonReadShortLe");
        $this$commonReadShortLe.require(2);
        return $this$commonReadShortLe.bufferField.readShortLe();
    }

    public static final String commonReadUtf8(RealBufferedSource $this$commonReadUtf8) {
        Intrinsics.checkNotNullParameter($this$commonReadUtf8, "$this$commonReadUtf8");
        $this$commonReadUtf8.bufferField.writeAll($this$commonReadUtf8.source);
        return $this$commonReadUtf8.bufferField.readUtf8();
    }

    public static final String commonReadUtf8(RealBufferedSource $this$commonReadUtf8, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonReadUtf8, "$this$commonReadUtf8");
        $this$commonReadUtf8.require(byteCount);
        return $this$commonReadUtf8.bufferField.readUtf8(byteCount);
    }

    public static final int commonReadUtf8CodePoint(RealBufferedSource $this$commonReadUtf8CodePoint) {
        Intrinsics.checkNotNullParameter($this$commonReadUtf8CodePoint, "$this$commonReadUtf8CodePoint");
        $this$commonReadUtf8CodePoint.require(1);
        byte b = $this$commonReadUtf8CodePoint.bufferField.getByte(0);
        if ((b & 224) == 192) {
            $this$commonReadUtf8CodePoint.require(2);
        } else if ((b & 240) == 224) {
            $this$commonReadUtf8CodePoint.require(3);
        } else if ((b & 248) == 240) {
            $this$commonReadUtf8CodePoint.require(4);
        }
        return $this$commonReadUtf8CodePoint.bufferField.readUtf8CodePoint();
    }

    public static final boolean commonRequest(RealBufferedSource $this$commonRequest, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRequest, "$this$commonRequest");
        if (!(byteCount >= 0)) {
            throw new IllegalArgumentException(("byteCount < 0: " + byteCount).toString());
        } else if (!$this$commonRequest.closed) {
            while ($this$commonRequest.bufferField.size() < byteCount) {
                if ($this$commonRequest.source.read($this$commonRequest.bufferField, (long) 8192) == -1) {
                    return false;
                }
            }
            return true;
        } else {
            throw new IllegalStateException("closed".toString());
        }
    }

    public static final void commonRequire(RealBufferedSource $this$commonRequire, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRequire, "$this$commonRequire");
        if (!$this$commonRequire.request(byteCount)) {
            throw new EOFException();
        }
    }

    public static final int commonSelect(RealBufferedSource $this$commonSelect, Options options) {
        Intrinsics.checkNotNullParameter($this$commonSelect, "$this$commonSelect");
        Intrinsics.checkNotNullParameter(options, "options");
        if (!$this$commonSelect.closed) {
            do {
                int selectPrefix = BufferKt.selectPrefix($this$commonSelect.bufferField, options, true);
                switch (selectPrefix) {
                    case -2:
                        break;
                    case -1:
                        return -1;
                    default:
                        $this$commonSelect.bufferField.skip((long) options.getByteStrings$okio()[selectPrefix].size());
                        return selectPrefix;
                }
            } while ($this$commonSelect.source.read($this$commonSelect.bufferField, (long) 8192) != -1);
            return -1;
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final void commonSkip(RealBufferedSource $this$commonSkip, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonSkip, "$this$commonSkip");
        long j = byteCount;
        if (!$this$commonSkip.closed) {
            while (j > 0) {
                if ($this$commonSkip.bufferField.size() == 0 && $this$commonSkip.source.read($this$commonSkip.bufferField, (long) 8192) == -1) {
                    throw new EOFException();
                }
                long min = Math.min(j, $this$commonSkip.bufferField.size());
                $this$commonSkip.bufferField.skip(min);
                j -= min;
            }
            return;
        }
        throw new IllegalStateException("closed".toString());
    }

    public static final Timeout commonTimeout(RealBufferedSource $this$commonTimeout) {
        Intrinsics.checkNotNullParameter($this$commonTimeout, "$this$commonTimeout");
        return $this$commonTimeout.source.timeout();
    }

    public static final String commonToString(RealBufferedSource $this$commonToString) {
        Intrinsics.checkNotNullParameter($this$commonToString, "$this$commonToString");
        return "buffer(" + $this$commonToString.source + ')';
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0047  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final long commonReadHexadecimalUnsignedLong(okio.RealBufferedSource r7) {
        /*
            r0 = 0
            java.lang.String r1 = "$this$commonReadHexadecimalUnsignedLong"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r7, r1)
            r1 = 1
            r7.require(r1)
            r1 = 0
        L_0x000c:
            int r2 = r1 + 1
            long r2 = (long) r2
            boolean r2 = r7.request(r2)
            if (r2 == 0) goto L_0x0078
            r2 = r7
            r3 = 0
            okio.Buffer r2 = r2.bufferField
            long r3 = (long) r1
            byte r2 = r2.getByte(r3)
            r3 = 48
            byte r3 = (byte) r3
            if (r2 < r3) goto L_0x002b
            r3 = 57
            byte r3 = (byte) r3
            if (r2 <= r3) goto L_0x0040
        L_0x002b:
            r3 = 97
            byte r3 = (byte) r3
            if (r2 < r3) goto L_0x0035
            r3 = 102(0x66, float:1.43E-43)
            byte r3 = (byte) r3
            if (r2 <= r3) goto L_0x0040
        L_0x0035:
            r3 = 65
            byte r3 = (byte) r3
            if (r2 < r3) goto L_0x0044
            r3 = 70
            byte r3 = (byte) r3
            if (r2 <= r3) goto L_0x0040
            goto L_0x0044
        L_0x0040:
            int r1 = r1 + 1
            goto L_0x000c
        L_0x0044:
            if (r1 == 0) goto L_0x0047
            goto L_0x0078
        L_0x0047:
            java.lang.NumberFormatException r3 = new java.lang.NumberFormatException
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Expected leading [0-9a-fA-F] character but was 0x"
            java.lang.StringBuilder r4 = r4.append(r5)
            r5 = 16
            int r5 = kotlin.text.CharsKt.checkRadix(r5)
            int r5 = kotlin.text.CharsKt.checkRadix(r5)
            java.lang.String r5 = java.lang.Integer.toString(r2, r5)
            mt.Log1F380D.a((java.lang.Object) r5)
            java.lang.String r6 = "java.lang.Integer.toStri…(this, checkRadix(radix))"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r5, r6)
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            r3.<init>(r4)
            java.lang.Throwable r3 = (java.lang.Throwable) r3
            throw r3
        L_0x0078:
            r2 = r7
            r3 = 0
            okio.Buffer r2 = r2.bufferField
            long r2 = r2.readHexadecimalUnsignedLong()
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.internal.RealBufferedSourceKt.commonReadHexadecimalUnsignedLong(okio.RealBufferedSource):long");
    }

    public static final String commonReadUtf8Line(RealBufferedSource $this$commonReadUtf8Line) {
        Intrinsics.checkNotNullParameter($this$commonReadUtf8Line, "$this$commonReadUtf8Line");
        long indexOf = $this$commonReadUtf8Line.indexOf((byte) 10);
        if (indexOf != -1) {
            String readUtf8Line = BufferKt.readUtf8Line($this$commonReadUtf8Line.bufferField, indexOf);
            Log1F380D.a((Object) readUtf8Line);
            return readUtf8Line;
        } else if ($this$commonReadUtf8Line.bufferField.size() != 0) {
            return $this$commonReadUtf8Line.readUtf8($this$commonReadUtf8Line.bufferField.size());
        } else {
            return null;
        }
    }

    public static final String commonReadUtf8LineStrict(RealBufferedSource $this$commonReadUtf8LineStrict, long limit) {
        RealBufferedSource realBufferedSource = $this$commonReadUtf8LineStrict;
        long j = limit;
        Intrinsics.checkNotNullParameter(realBufferedSource, "$this$commonReadUtf8LineStrict");
        if (j >= 0) {
            long j2 = j == Long.MAX_VALUE ? Long.MAX_VALUE : j + 1;
            byte b = (byte) 10;
            byte b2 = b;
            long indexOf = $this$commonReadUtf8LineStrict.indexOf(b, 0, j2);
            if (indexOf != -1) {
                String readUtf8Line = BufferKt.readUtf8Line($this$commonReadUtf8LineStrict.bufferField, indexOf);
                Log1F380D.a((Object) readUtf8Line);
                return readUtf8Line;
            } else if (j2 >= Long.MAX_VALUE || !realBufferedSource.request(j2) || $this$commonReadUtf8LineStrict.bufferField.getByte(j2 - 1) != ((byte) 13) || !realBufferedSource.request(1 + j2) || $this$commonReadUtf8LineStrict.bufferField.getByte(j2) != b2) {
                Buffer buffer = new Buffer();
                $this$commonReadUtf8LineStrict.bufferField.copyTo(buffer, 0, Math.min((long) 32, $this$commonReadUtf8LineStrict.bufferField.size()));
                throw new EOFException("\\n not found: limit=" + Math.min($this$commonReadUtf8LineStrict.bufferField.size(), j) + " content=" + buffer.readByteString().hex() + "…");
            } else {
                String readUtf8Line2 = BufferKt.readUtf8Line($this$commonReadUtf8LineStrict.bufferField, j2);
                Log1F380D.a((Object) readUtf8Line2);
                return readUtf8Line2;
            }
        } else {
            throw new IllegalArgumentException(("limit < 0: " + j).toString());
        }
    }
}

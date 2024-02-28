package okio.internal;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.work.WorkRequest;
import java.io.EOFException;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.jvm.internal.CharCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Typography;
import mt.Log1F380D;
import okhttp3.HttpUrl;
import okhttp3.internal.connection.RealConnection;
import okio.Buffer;
import okio.ByteString;
import okio.Options;
import okio.Platform;
import okio.Segment;
import okio.SegmentPool;
import okio.SegmentedByteString;
import okio.Sink;
import okio.Source;
import okio.Utf8;
import okio.Util;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000v\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\n\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a0\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\bH\u0000\u001a\r\u0010\u0011\u001a\u00020\u0012*\u00020\u0013H\b\u001a\r\u0010\u0014\u001a\u00020\u0005*\u00020\u0013H\b\u001a\r\u0010\u0015\u001a\u00020\u0013*\u00020\u0013H\b\u001a%\u0010\u0016\u001a\u00020\u0013*\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u0005H\b\u001a\u0017\u0010\u001a\u001a\u00020\n*\u00020\u00132\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\b\u001a\u0015\u0010\u001d\u001a\u00020\u001e*\u00020\u00132\u0006\u0010\u001f\u001a\u00020\u0005H\b\u001a\r\u0010 \u001a\u00020\b*\u00020\u0013H\b\u001a%\u0010!\u001a\u00020\u0005*\u00020\u00132\u0006\u0010\"\u001a\u00020\u001e2\u0006\u0010#\u001a\u00020\u00052\u0006\u0010$\u001a\u00020\u0005H\b\u001a\u001d\u0010!\u001a\u00020\u0005*\u00020\u00132\u0006\u0010\u000e\u001a\u00020%2\u0006\u0010#\u001a\u00020\u0005H\b\u001a\u001d\u0010&\u001a\u00020\u0005*\u00020\u00132\u0006\u0010'\u001a\u00020%2\u0006\u0010#\u001a\u00020\u0005H\b\u001a-\u0010(\u001a\u00020\n*\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020%2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\bH\b\u001a\u0015\u0010)\u001a\u00020\b*\u00020\u00132\u0006\u0010*\u001a\u00020\u0001H\b\u001a%\u0010)\u001a\u00020\b*\u00020\u00132\u0006\u0010*\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\bH\b\u001a\u001d\u0010)\u001a\u00020\u0005*\u00020\u00132\u0006\u0010*\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\b\u001a\u0015\u0010+\u001a\u00020\u0005*\u00020\u00132\u0006\u0010*\u001a\u00020,H\b\u001a\r\u0010-\u001a\u00020\u001e*\u00020\u0013H\b\u001a\r\u0010.\u001a\u00020\u0001*\u00020\u0013H\b\u001a\u0015\u0010.\u001a\u00020\u0001*\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\b\u001a\r\u0010/\u001a\u00020%*\u00020\u0013H\b\u001a\u0015\u0010/\u001a\u00020%*\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\b\u001a\r\u00100\u001a\u00020\u0005*\u00020\u0013H\b\u001a\u0015\u00101\u001a\u00020\u0012*\u00020\u00132\u0006\u0010*\u001a\u00020\u0001H\b\u001a\u001d\u00101\u001a\u00020\u0012*\u00020\u00132\u0006\u0010*\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\b\u001a\r\u00102\u001a\u00020\u0005*\u00020\u0013H\b\u001a\r\u00103\u001a\u00020\b*\u00020\u0013H\b\u001a\r\u00104\u001a\u00020\u0005*\u00020\u0013H\b\u001a\r\u00105\u001a\u000206*\u00020\u0013H\b\u001a\u0015\u00107\u001a\u000208*\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\b\u001a\r\u00109\u001a\u00020\b*\u00020\u0013H\b\u001a\u000f\u0010:\u001a\u0004\u0018\u000108*\u00020\u0013H\b\u001a\u0015\u0010;\u001a\u000208*\u00020\u00132\u0006\u0010<\u001a\u00020\u0005H\b\u001a\u0015\u0010=\u001a\u00020\b*\u00020\u00132\u0006\u0010>\u001a\u00020?H\b\u001a\u0015\u0010@\u001a\u00020\u0012*\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\b\u001a\r\u0010A\u001a\u00020%*\u00020\u0013H\b\u001a\u0015\u0010A\u001a\u00020%*\u00020\u00132\u0006\u0010\u0019\u001a\u00020\bH\b\u001a\u0015\u0010B\u001a\u00020\f*\u00020\u00132\u0006\u0010C\u001a\u00020\bH\b\u001a\u0015\u0010D\u001a\u00020\u0013*\u00020\u00132\u0006\u0010E\u001a\u00020\u0001H\b\u001a%\u0010D\u001a\u00020\u0013*\u00020\u00132\u0006\u0010E\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\bH\b\u001a\u001d\u0010D\u001a\u00020\u0012*\u00020\u00132\u0006\u0010E\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0005H\b\u001a)\u0010D\u001a\u00020\u0013*\u00020\u00132\u0006\u0010F\u001a\u00020%2\b\b\u0002\u0010\u0018\u001a\u00020\b2\b\b\u0002\u0010\u0019\u001a\u00020\bH\b\u001a\u001d\u0010D\u001a\u00020\u0013*\u00020\u00132\u0006\u0010E\u001a\u00020G2\u0006\u0010\u0019\u001a\u00020\u0005H\b\u001a\u0015\u0010H\u001a\u00020\u0005*\u00020\u00132\u0006\u0010E\u001a\u00020GH\b\u001a\u0015\u0010I\u001a\u00020\u0013*\u00020\u00132\u0006\u0010\"\u001a\u00020\bH\b\u001a\u0015\u0010J\u001a\u00020\u0013*\u00020\u00132\u0006\u0010K\u001a\u00020\u0005H\b\u001a\u0015\u0010L\u001a\u00020\u0013*\u00020\u00132\u0006\u0010K\u001a\u00020\u0005H\b\u001a\u0015\u0010M\u001a\u00020\u0013*\u00020\u00132\u0006\u0010N\u001a\u00020\bH\b\u001a\u0015\u0010O\u001a\u00020\u0013*\u00020\u00132\u0006\u0010K\u001a\u00020\u0005H\b\u001a\u0015\u0010P\u001a\u00020\u0013*\u00020\u00132\u0006\u0010Q\u001a\u00020\bH\b\u001a%\u0010R\u001a\u00020\u0013*\u00020\u00132\u0006\u0010S\u001a\u0002082\u0006\u0010T\u001a\u00020\b2\u0006\u0010U\u001a\u00020\bH\b\u001a\u0015\u0010V\u001a\u00020\u0013*\u00020\u00132\u0006\u0010W\u001a\u00020\bH\b\u001a\u0014\u0010X\u001a\u000208*\u00020\u00132\u0006\u0010Y\u001a\u00020\u0005H\u0000\u001a?\u0010Z\u001a\u0002H[\"\u0004\b\u0000\u0010[*\u00020\u00132\u0006\u0010#\u001a\u00020\u00052\u001a\u0010\\\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u00010\f\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H[0]H\bø\u0001\u0000¢\u0006\u0002\u0010^\u001a\u001e\u0010_\u001a\u00020\b*\u00020\u00132\u0006\u0010>\u001a\u00020?2\b\b\u0002\u0010`\u001a\u00020\nH\u0000\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\bXT¢\u0006\u0002\n\u0000\u0002\u0007\n\u0005\b20\u0001¨\u0006a"}, d2 = {"HEX_DIGIT_BYTES", "", "getHEX_DIGIT_BYTES", "()[B", "OVERFLOW_DIGIT_START", "", "OVERFLOW_ZONE", "SEGMENTING_THRESHOLD", "", "rangeEquals", "", "segment", "Lokio/Segment;", "segmentPos", "bytes", "bytesOffset", "bytesLimit", "commonClear", "", "Lokio/Buffer;", "commonCompleteSegmentByteCount", "commonCopy", "commonCopyTo", "out", "offset", "byteCount", "commonEquals", "other", "", "commonGet", "", "pos", "commonHashCode", "commonIndexOf", "b", "fromIndex", "toIndex", "Lokio/ByteString;", "commonIndexOfElement", "targetBytes", "commonRangeEquals", "commonRead", "sink", "commonReadAll", "Lokio/Sink;", "commonReadByte", "commonReadByteArray", "commonReadByteString", "commonReadDecimalLong", "commonReadFully", "commonReadHexadecimalUnsignedLong", "commonReadInt", "commonReadLong", "commonReadShort", "", "commonReadUtf8", "", "commonReadUtf8CodePoint", "commonReadUtf8Line", "commonReadUtf8LineStrict", "limit", "commonSelect", "options", "Lokio/Options;", "commonSkip", "commonSnapshot", "commonWritableSegment", "minimumCapacity", "commonWrite", "source", "byteString", "Lokio/Source;", "commonWriteAll", "commonWriteByte", "commonWriteDecimalLong", "v", "commonWriteHexadecimalUnsignedLong", "commonWriteInt", "i", "commonWriteLong", "commonWriteShort", "s", "commonWriteUtf8", "string", "beginIndex", "endIndex", "commonWriteUtf8CodePoint", "codePoint", "readUtf8Line", "newline", "seek", "T", "lambda", "Lkotlin/Function2;", "(Lokio/Buffer;JLkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "selectPrefix", "selectTruncated", "okio"}, k = 2, mv = {1, 4, 0})
/* compiled from: 01F0 */
public final class BufferKt {
    private static final byte[] HEX_DIGIT_BYTES = Platform.asUtf8ToByteArray("0123456789abcdef");
    public static final long OVERFLOW_DIGIT_START = -7;
    public static final long OVERFLOW_ZONE = -922337203685477580L;
    public static final int SEGMENTING_THRESHOLD = 4096;

    public static final void commonClear(Buffer $this$commonClear) {
        Intrinsics.checkNotNullParameter($this$commonClear, "$this$commonClear");
        $this$commonClear.skip($this$commonClear.size());
    }

    public static final long commonCompleteSegmentByteCount(Buffer $this$commonCompleteSegmentByteCount) {
        Intrinsics.checkNotNullParameter($this$commonCompleteSegmentByteCount, "$this$commonCompleteSegmentByteCount");
        long size = $this$commonCompleteSegmentByteCount.size();
        if (size == 0) {
            return 0;
        }
        Segment segment = $this$commonCompleteSegmentByteCount.head;
        Intrinsics.checkNotNull(segment);
        Segment segment2 = segment.prev;
        Intrinsics.checkNotNull(segment2);
        return (segment2.limit >= 8192 || !segment2.owner) ? size : size - ((long) (segment2.limit - segment2.pos));
    }

    public static final Buffer commonCopy(Buffer $this$commonCopy) {
        Intrinsics.checkNotNullParameter($this$commonCopy, "$this$commonCopy");
        Buffer buffer = new Buffer();
        if ($this$commonCopy.size() == 0) {
            return buffer;
        }
        Segment segment = $this$commonCopy.head;
        Intrinsics.checkNotNull(segment);
        Segment sharedCopy = segment.sharedCopy();
        buffer.head = sharedCopy;
        sharedCopy.prev = buffer.head;
        sharedCopy.next = sharedCopy.prev;
        for (Segment segment2 = segment.next; segment2 != segment; segment2 = segment2.next) {
            Segment segment3 = sharedCopy.prev;
            Intrinsics.checkNotNull(segment3);
            Intrinsics.checkNotNull(segment2);
            segment3.push(segment2.sharedCopy());
        }
        buffer.setSize$okio($this$commonCopy.size());
        return buffer;
    }

    public static final Buffer commonCopyTo(Buffer $this$commonCopyTo, Buffer out, long offset, long byteCount) {
        Buffer buffer = $this$commonCopyTo;
        Buffer buffer2 = out;
        Intrinsics.checkNotNullParameter($this$commonCopyTo, "$this$commonCopyTo");
        Intrinsics.checkNotNullParameter(out, "out");
        long j = offset;
        long j2 = byteCount;
        Util.checkOffsetAndCount($this$commonCopyTo.size(), j, j2);
        if (j2 == 0) {
            return buffer;
        }
        out.setSize$okio(out.size() + j2);
        Segment segment = buffer.head;
        while (true) {
            Intrinsics.checkNotNull(segment);
            if (j < ((long) (segment.limit - segment.pos))) {
                break;
            }
            j -= (long) (segment.limit - segment.pos);
            segment = segment.next;
        }
        while (j2 > 0) {
            Intrinsics.checkNotNull(segment);
            Segment sharedCopy = segment.sharedCopy();
            sharedCopy.pos += (int) j;
            sharedCopy.limit = Math.min(sharedCopy.pos + ((int) j2), sharedCopy.limit);
            if (buffer2.head == null) {
                sharedCopy.prev = sharedCopy;
                sharedCopy.next = sharedCopy.prev;
                buffer2.head = sharedCopy.next;
            } else {
                Segment segment2 = buffer2.head;
                Intrinsics.checkNotNull(segment2);
                Segment segment3 = segment2.prev;
                Intrinsics.checkNotNull(segment3);
                segment3.push(sharedCopy);
            }
            j2 -= (long) (sharedCopy.limit - sharedCopy.pos);
            j = 0;
            segment = segment.next;
        }
        return buffer;
    }

    /* JADX WARNING: type inference failed for: r22v0, types: [java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final boolean commonEquals(okio.Buffer r21, java.lang.Object r22) {
        /*
            r0 = r21
            r1 = r22
            r2 = 0
            java.lang.String r3 = "$this$commonEquals"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r0, r3)
            r3 = 1
            if (r0 != r1) goto L_0x000e
            return r3
        L_0x000e:
            boolean r4 = r1 instanceof okio.Buffer
            r5 = 0
            if (r4 != 0) goto L_0x0014
            return r5
        L_0x0014:
            long r6 = r21.size()
            r4 = r1
            okio.Buffer r4 = (okio.Buffer) r4
            long r8 = r4.size()
            int r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r4 == 0) goto L_0x0024
            return r5
        L_0x0024:
            long r6 = r21.size()
            r8 = 0
            int r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r4 != 0) goto L_0x002f
            return r3
        L_0x002f:
            okio.Segment r4 = r0.head
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
            r6 = r1
            okio.Buffer r6 = (okio.Buffer) r6
            okio.Segment r6 = r6.head
            kotlin.jvm.internal.Intrinsics.checkNotNull(r6)
            int r7 = r4.pos
            int r10 = r6.pos
            r11 = 0
            r13 = 0
        L_0x0044:
            long r15 = r21.size()
            int r15 = (r11 > r15 ? 1 : (r11 == r15 ? 0 : -1))
            if (r15 >= 0) goto L_0x0094
            int r15 = r4.limit
            int r15 = r15 - r7
            int r8 = r6.limit
            int r8 = r8 - r10
            int r8 = java.lang.Math.min(r15, r8)
            long r13 = (long) r8
            r8 = 0
        L_0x0059:
            int r15 = (r8 > r13 ? 1 : (r8 == r13 ? 0 : -1))
            if (r15 >= 0) goto L_0x0077
            byte[] r15 = r4.data
            int r17 = r7 + 1
            byte r7 = r15[r7]
            byte[] r15 = r6.data
            int r18 = r10 + 1
            byte r10 = r15[r10]
            if (r7 == r10) goto L_0x006c
            return r5
        L_0x006c:
            r19 = 1
            long r19 = r8 + r19
            r7 = r17
            r10 = r18
            r8 = r19
            goto L_0x0059
        L_0x0077:
            int r8 = r4.limit
            if (r7 != r8) goto L_0x0083
            okio.Segment r8 = r4.next
            kotlin.jvm.internal.Intrinsics.checkNotNull(r8)
            r4 = r8
            int r7 = r4.pos
        L_0x0083:
            int r8 = r6.limit
            if (r10 != r8) goto L_0x0090
            okio.Segment r8 = r6.next
            kotlin.jvm.internal.Intrinsics.checkNotNull(r8)
            r6 = r8
            int r8 = r6.pos
            r10 = r8
        L_0x0090:
            long r11 = r11 + r13
            r8 = 0
            goto L_0x0044
        L_0x0094:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.internal.BufferKt.commonEquals(okio.Buffer, java.lang.Object):boolean");
    }

    public static final byte commonGet(Buffer $this$commonGet, long pos) {
        Intrinsics.checkNotNullParameter($this$commonGet, "$this$commonGet");
        Util.checkOffsetAndCount($this$commonGet.size(), pos, 1);
        Buffer buffer = $this$commonGet;
        Segment segment = buffer.head;
        if (segment == null) {
            Segment segment2 = null;
            Intrinsics.checkNotNull(segment2);
            return segment2.data[(int) ((((long) segment2.pos) + pos) - -1)];
        } else if (buffer.size() - pos < pos) {
            long size = buffer.size();
            while (size > pos) {
                Segment segment3 = segment.prev;
                Intrinsics.checkNotNull(segment3);
                segment = segment3;
                size -= (long) (segment.limit - segment.pos);
            }
            Segment segment4 = segment;
            Intrinsics.checkNotNull(segment4);
            return segment4.data[(int) ((((long) segment4.pos) + pos) - size)];
        } else {
            long j = 0;
            while (true) {
                long j2 = ((long) (segment.limit - segment.pos)) + j;
                if (j2 > pos) {
                    Segment segment5 = segment;
                    Intrinsics.checkNotNull(segment5);
                    return segment5.data[(int) ((((long) segment5.pos) + pos) - j)];
                }
                Segment segment6 = segment.next;
                Intrinsics.checkNotNull(segment6);
                segment = segment6;
                j = j2;
            }
        }
    }

    public static final int commonHashCode(Buffer $this$commonHashCode) {
        Intrinsics.checkNotNullParameter($this$commonHashCode, "$this$commonHashCode");
        Segment segment = $this$commonHashCode.head;
        if (segment == null) {
            return 0;
        }
        int i = 1;
        do {
            int i2 = segment.limit;
            for (int i3 = segment.pos; i3 < i2; i3++) {
                i = (i * 31) + segment.data[i3];
            }
            Segment segment2 = segment.next;
            Intrinsics.checkNotNull(segment2);
            segment = segment2;
        } while (segment != $this$commonHashCode.head);
        return i;
    }

    public static final long commonIndexOf(Buffer $this$commonIndexOf, byte b, long fromIndex, long toIndex) {
        byte b2 = b;
        Intrinsics.checkNotNullParameter($this$commonIndexOf, "$this$commonIndexOf");
        long j = fromIndex;
        long j2 = toIndex;
        if (0 <= j && j2 >= j) {
            if (j2 > $this$commonIndexOf.size()) {
                j2 = $this$commonIndexOf.size();
            }
            if (j == j2) {
                return -1;
            }
            long j3 = j;
            Buffer buffer = $this$commonIndexOf;
            boolean z = false;
            Segment segment = buffer.head;
            if (segment == null) {
                Buffer buffer2 = buffer;
                Segment segment2 = null;
                return -1;
            } else if (buffer.size() - j3 < j3) {
                long size = buffer.size();
                while (size > j3) {
                    Segment segment3 = segment.prev;
                    Intrinsics.checkNotNull(segment3);
                    segment = segment3;
                    size -= (long) (segment.limit - segment.pos);
                }
                Segment segment4 = segment;
                long j4 = size;
                boolean z2 = false;
                if (segment4 != null) {
                    long j5 = j4;
                    Segment segment5 = segment4;
                    while (j5 < j2) {
                        Buffer buffer3 = buffer;
                        byte[] bArr = segment5.data;
                        Segment segment6 = segment4;
                        boolean z3 = z2;
                        boolean z4 = z;
                        Segment segment7 = segment;
                        int min = (int) Math.min((long) segment5.limit, (((long) segment5.pos) + j2) - j5);
                        for (int i = (int) ((((long) segment5.pos) + j) - j5); i < min; i++) {
                            if (bArr[i] == b2) {
                                return ((long) (i - segment5.pos)) + j5;
                            }
                        }
                        j5 += (long) (segment5.limit - segment5.pos);
                        j = j5;
                        Segment segment8 = segment5.next;
                        Intrinsics.checkNotNull(segment8);
                        segment5 = segment8;
                        Buffer buffer4 = $this$commonIndexOf;
                        buffer = buffer3;
                        segment4 = segment6;
                        z2 = z3;
                        z = z4;
                        segment = segment7;
                    }
                    Buffer buffer5 = buffer;
                    return -1;
                }
                Buffer buffer6 = buffer;
                return -1;
            } else {
                Buffer buffer7 = buffer;
                long j6 = 0;
                while (true) {
                    long j7 = ((long) (segment.limit - segment.pos)) + j6;
                    if (j7 > j3) {
                        break;
                    }
                    long j8 = j6;
                    Segment segment9 = segment.next;
                    Intrinsics.checkNotNull(segment9);
                    segment = segment9;
                    j6 = j7;
                }
                Segment segment10 = segment;
                long j9 = j6;
                if (segment10 != null) {
                    Segment segment11 = segment10;
                    long j10 = j9;
                    while (j10 < j2) {
                        byte[] bArr2 = segment11.data;
                        long j11 = j6;
                        Segment segment12 = segment10;
                        long j12 = j9;
                        int min2 = (int) Math.min((long) segment11.limit, (((long) segment11.pos) + j2) - j10);
                        for (int i2 = (int) ((((long) segment11.pos) + j) - j10); i2 < min2; i2++) {
                            if (bArr2[i2] == b2) {
                                return ((long) (i2 - segment11.pos)) + j10;
                            }
                        }
                        j10 += (long) (segment11.limit - segment11.pos);
                        j = j10;
                        Segment segment13 = segment11.next;
                        Intrinsics.checkNotNull(segment13);
                        segment11 = segment13;
                        j6 = j11;
                        segment10 = segment12;
                        j9 = j12;
                    }
                    long j13 = j6;
                    return -1;
                }
                long j14 = j6;
                return -1;
            }
        } else {
            throw new IllegalArgumentException(("size=" + $this$commonIndexOf.size() + " fromIndex=" + j + " toIndex=" + j2).toString());
        }
    }

    public static final long commonIndexOf(Buffer $this$commonIndexOf, ByteString bytes, long fromIndex) {
        Intrinsics.checkNotNullParameter($this$commonIndexOf, "$this$commonIndexOf");
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        long j = fromIndex;
        if (bytes.size() > 0) {
            if (j >= 0) {
                long j2 = j;
                Buffer buffer = $this$commonIndexOf;
                boolean z = false;
                Segment segment = buffer.head;
                if (segment == null) {
                    Buffer buffer2 = buffer;
                    Segment segment2 = null;
                    return -1;
                } else if (buffer.size() - j2 < j2) {
                    long size = buffer.size();
                    while (size > j2) {
                        Segment segment3 = segment.prev;
                        Intrinsics.checkNotNull(segment3);
                        segment = segment3;
                        size -= (long) (segment.limit - segment.pos);
                    }
                    Segment segment4 = segment;
                    long j3 = size;
                    if (segment4 != null) {
                        long j4 = j3;
                        byte[] internalArray$okio = bytes.internalArray$okio();
                        byte b = internalArray$okio[0];
                        int size2 = bytes.size();
                        Buffer buffer3 = buffer;
                        long size3 = ($this$commonIndexOf.size() - ((long) size2)) + 1;
                        Segment segment5 = segment4;
                        while (j4 < size3) {
                            byte[] bArr = segment5.data;
                            boolean z2 = z;
                            Segment segment6 = segment;
                            long j5 = j3;
                            Segment segment7 = segment4;
                            int min = (int) Math.min((long) segment5.limit, (((long) segment5.pos) + size3) - j4);
                            for (int i = (int) ((((long) segment5.pos) + j) - j4); i < min; i++) {
                                if (bArr[i] == b && rangeEquals(segment5, i + 1, internalArray$okio, 1, size2)) {
                                    return ((long) (i - segment5.pos)) + j4;
                                }
                            }
                            j4 += (long) (segment5.limit - segment5.pos);
                            j = j4;
                            Segment segment8 = segment5.next;
                            Intrinsics.checkNotNull(segment8);
                            segment5 = segment8;
                            Buffer buffer4 = $this$commonIndexOf;
                            ByteString byteString = bytes;
                            segment4 = segment7;
                            z = z2;
                            segment = segment6;
                            j3 = j5;
                        }
                        return -1;
                    }
                    Buffer buffer5 = buffer;
                    return -1;
                } else {
                    Buffer buffer6 = buffer;
                    long j6 = 0;
                    while (true) {
                        long j7 = ((long) (segment.limit - segment.pos)) + j6;
                        if (j7 > j2) {
                            break;
                        }
                        long j8 = j6;
                        Segment segment9 = segment.next;
                        Intrinsics.checkNotNull(segment9);
                        segment = segment9;
                        j6 = j7;
                        j2 = j2;
                    }
                    Segment segment10 = segment;
                    long j9 = j6;
                    boolean z3 = false;
                    if (segment10 == null) {
                        return -1;
                    }
                    Segment segment11 = segment10;
                    long j10 = j9;
                    byte[] internalArray$okio2 = bytes.internalArray$okio();
                    byte b2 = internalArray$okio2[0];
                    long j11 = j6;
                    int size4 = bytes.size();
                    Segment segment12 = segment10;
                    long size5 = ($this$commonIndexOf.size() - ((long) size4)) + 1;
                    while (j10 < size5) {
                        byte[] bArr2 = segment11.data;
                        boolean z4 = z3;
                        long j12 = j2;
                        long j13 = j9;
                        int min2 = (int) Math.min((long) segment11.limit, (((long) segment11.pos) + size5) - j10);
                        for (int i2 = (int) ((((long) segment11.pos) + j) - j10); i2 < min2; i2++) {
                            if (bArr2[i2] == b2) {
                                if (rangeEquals(segment11, i2 + 1, internalArray$okio2, 1, size4)) {
                                    return ((long) (i2 - segment11.pos)) + j10;
                                }
                            }
                        }
                        j10 += (long) (segment11.limit - segment11.pos);
                        j = j10;
                        Segment segment13 = segment11.next;
                        Intrinsics.checkNotNull(segment13);
                        segment11 = segment13;
                        z3 = z4;
                        j2 = j12;
                        j9 = j13;
                    }
                    return -1;
                }
            } else {
                throw new IllegalArgumentException(("fromIndex < 0: " + j).toString());
            }
        } else {
            throw new IllegalArgumentException("bytes is empty".toString());
        }
    }

    public static final long commonIndexOfElement(Buffer $this$commonIndexOfElement, ByteString targetBytes, long fromIndex) {
        ByteString byteString = targetBytes;
        boolean z = false;
        Intrinsics.checkNotNullParameter($this$commonIndexOfElement, "$this$commonIndexOfElement");
        Intrinsics.checkNotNullParameter(byteString, "targetBytes");
        long j = fromIndex;
        if (j >= 0) {
            long j2 = j;
            Buffer buffer = $this$commonIndexOfElement;
            Segment segment = buffer.head;
            if (segment == null) {
                Segment segment2 = null;
                return -1;
            } else if (buffer.size() - j2 < j2) {
                long size = buffer.size();
                while (size > j2) {
                    Segment segment3 = segment.prev;
                    Intrinsics.checkNotNull(segment3);
                    segment = segment3;
                    size -= (long) (segment.limit - segment.pos);
                }
                Segment segment4 = segment;
                long j3 = size;
                if (segment4 == null) {
                    return -1;
                }
                Segment segment5 = segment4;
                long j4 = j3;
                if (targetBytes.size() == 2) {
                    byte b = byteString.getByte(0);
                    byte b2 = byteString.getByte(1);
                    Segment segment6 = segment5;
                    while (j4 < $this$commonIndexOfElement.size()) {
                        boolean z2 = z;
                        byte[] bArr = segment6.data;
                        Buffer buffer2 = buffer;
                        int i = (int) ((((long) segment6.pos) + j) - j4);
                        int i2 = segment6.limit;
                        while (i < i2) {
                            int i3 = i2;
                            byte b3 = bArr[i];
                            if (b3 == b || b3 == b2) {
                                byte[] bArr2 = bArr;
                                int i4 = i;
                                return ((long) (i - segment6.pos)) + j4;
                            }
                            i++;
                            i2 = i3;
                        }
                        byte[] bArr3 = bArr;
                        int i5 = i;
                        int i6 = i2;
                        j4 += (long) (segment6.limit - segment6.pos);
                        j = j4;
                        Segment segment7 = segment6.next;
                        Intrinsics.checkNotNull(segment7);
                        segment6 = segment7;
                        Buffer buffer3 = $this$commonIndexOfElement;
                        z = z2;
                        buffer = buffer2;
                    }
                    boolean z3 = z;
                    Buffer buffer4 = buffer;
                    return -1;
                }
                Buffer buffer5 = buffer;
                byte[] internalArray$okio = targetBytes.internalArray$okio();
                Segment segment8 = segment5;
                while (j4 < $this$commonIndexOfElement.size()) {
                    byte[] bArr4 = segment8.data;
                    int i7 = (int) ((((long) segment8.pos) + j) - j4);
                    int i8 = segment8.limit;
                    while (i7 < i8) {
                        byte b4 = bArr4[i7];
                        byte[] bArr5 = bArr4;
                        long j5 = j;
                        for (byte b5 : internalArray$okio) {
                            if (b4 == b5) {
                                return ((long) (i7 - segment8.pos)) + j4;
                            }
                        }
                        i7++;
                        bArr4 = bArr5;
                        j = j5;
                    }
                    byte[] bArr6 = bArr4;
                    long j6 = j;
                    j4 += (long) (segment8.limit - segment8.pos);
                    j = j4;
                    Segment segment9 = segment8.next;
                    Intrinsics.checkNotNull(segment9);
                    segment8 = segment9;
                }
                long j7 = j;
                Segment segment10 = segment8;
                return -1;
            } else {
                Buffer buffer6 = buffer;
                long j8 = 0;
                while (true) {
                    long j9 = ((long) (segment.limit - segment.pos)) + j8;
                    if (j9 > j2) {
                        break;
                    }
                    long j10 = j8;
                    Segment segment11 = segment.next;
                    Intrinsics.checkNotNull(segment11);
                    segment = segment11;
                    j8 = j9;
                    byteString = targetBytes;
                }
                Segment segment12 = segment;
                long j11 = j8;
                if (segment12 == null) {
                    return -1;
                }
                Segment segment13 = segment12;
                long j12 = j11;
                if (targetBytes.size() == 2) {
                    byte b6 = byteString.getByte(0);
                    byte b7 = byteString.getByte(1);
                    while (j12 < $this$commonIndexOfElement.size()) {
                        byte[] bArr7 = segment13.data;
                        long j13 = j8;
                        int i9 = (int) ((((long) segment13.pos) + j) - j12);
                        int i10 = segment13.limit;
                        while (i9 < i10) {
                            int i11 = i10;
                            byte b8 = bArr7[i9];
                            if (b8 == b6 || b8 == b7) {
                                byte[] bArr8 = bArr7;
                                int i12 = i9;
                                return ((long) (i9 - segment13.pos)) + j12;
                            }
                            i9++;
                            i10 = i11;
                        }
                        byte[] bArr9 = bArr7;
                        int i13 = i9;
                        int i14 = i10;
                        j12 += (long) (segment13.limit - segment13.pos);
                        j = j12;
                        Segment segment14 = segment13.next;
                        Intrinsics.checkNotNull(segment14);
                        segment13 = segment14;
                        ByteString byteString2 = targetBytes;
                        j8 = j13;
                    }
                    long j14 = j8;
                    Segment segment15 = segment12;
                    return -1;
                }
                long j15 = j8;
                byte[] internalArray$okio2 = targetBytes.internalArray$okio();
                while (j12 < $this$commonIndexOfElement.size()) {
                    byte[] bArr10 = segment13.data;
                    int i15 = (int) ((((long) segment13.pos) + j) - j12);
                    int i16 = segment13.limit;
                    while (i15 < i16) {
                        byte b9 = bArr10[i15];
                        byte[] bArr11 = bArr10;
                        int length = internalArray$okio2.length;
                        Segment segment16 = segment12;
                        int i17 = 0;
                        while (i17 < length) {
                            int i18 = length;
                            byte b10 = internalArray$okio2[i17];
                            if (b9 == b10) {
                                byte[] bArr12 = internalArray$okio2;
                                byte b11 = b10;
                                return ((long) (i15 - segment13.pos)) + j12;
                            }
                            byte[] bArr13 = internalArray$okio2;
                            byte b12 = b10;
                            i17++;
                            length = i18;
                        }
                        byte[] bArr14 = internalArray$okio2;
                        i15++;
                        bArr10 = bArr11;
                        segment12 = segment16;
                    }
                    byte[] bArr15 = internalArray$okio2;
                    byte[] bArr16 = bArr10;
                    Segment segment17 = segment12;
                    j12 += (long) (segment13.limit - segment13.pos);
                    j = j12;
                    Segment segment18 = segment13.next;
                    Intrinsics.checkNotNull(segment18);
                    segment13 = segment18;
                    internalArray$okio2 = bArr15;
                }
                byte[] bArr17 = internalArray$okio2;
                Segment segment19 = segment12;
                return -1;
            }
        } else {
            throw new IllegalArgumentException(("fromIndex < 0: " + j).toString());
        }
    }

    public static final boolean commonRangeEquals(Buffer $this$commonRangeEquals, long offset, ByteString bytes, int bytesOffset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRangeEquals, "$this$commonRangeEquals");
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (offset < 0 || bytesOffset < 0 || byteCount < 0 || $this$commonRangeEquals.size() - offset < ((long) byteCount) || bytes.size() - bytesOffset < byteCount) {
            return false;
        }
        for (int i = 0; i < byteCount; i++) {
            if ($this$commonRangeEquals.getByte(((long) i) + offset) != bytes.getByte(bytesOffset + i)) {
                return false;
            }
        }
        return true;
    }

    public static final int commonRead(Buffer $this$commonRead, byte[] sink) {
        Intrinsics.checkNotNullParameter($this$commonRead, "$this$commonRead");
        Intrinsics.checkNotNullParameter(sink, "sink");
        return $this$commonRead.read(sink, 0, sink.length);
    }

    public static final int commonRead(Buffer $this$commonRead, byte[] sink, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRead, "$this$commonRead");
        Intrinsics.checkNotNullParameter(sink, "sink");
        Util.checkOffsetAndCount((long) sink.length, (long) offset, (long) byteCount);
        Segment segment = $this$commonRead.head;
        if (segment == null) {
            return -1;
        }
        int min = Math.min(byteCount, segment.limit - segment.pos);
        ArraysKt.copyInto(segment.data, sink, offset, segment.pos, segment.pos + min);
        segment.pos += min;
        $this$commonRead.setSize$okio($this$commonRead.size() - ((long) min));
        if (segment.pos == segment.limit) {
            $this$commonRead.head = segment.pop();
            SegmentPool.recycle(segment);
        }
        return min;
    }

    public static final long commonRead(Buffer $this$commonRead, Buffer sink, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRead, "$this$commonRead");
        Intrinsics.checkNotNullParameter(sink, "sink");
        long j = byteCount;
        if (!(j >= 0)) {
            throw new IllegalArgumentException(("byteCount < 0: " + j).toString());
        } else if ($this$commonRead.size() == 0) {
            return -1;
        } else {
            if (j > $this$commonRead.size()) {
                j = $this$commonRead.size();
            }
            sink.write($this$commonRead, j);
            return j;
        }
    }

    public static final long commonReadAll(Buffer $this$commonReadAll, Sink sink) {
        Intrinsics.checkNotNullParameter($this$commonReadAll, "$this$commonReadAll");
        Intrinsics.checkNotNullParameter(sink, "sink");
        long size = $this$commonReadAll.size();
        if (size > 0) {
            sink.write($this$commonReadAll, size);
        }
        return size;
    }

    public static final byte commonReadByte(Buffer $this$commonReadByte) {
        Intrinsics.checkNotNullParameter($this$commonReadByte, "$this$commonReadByte");
        if ($this$commonReadByte.size() != 0) {
            Segment segment = $this$commonReadByte.head;
            Intrinsics.checkNotNull(segment);
            int i = segment.pos;
            int i2 = segment.limit;
            int i3 = i + 1;
            byte b = segment.data[i];
            $this$commonReadByte.setSize$okio($this$commonReadByte.size() - 1);
            if (i3 == i2) {
                $this$commonReadByte.head = segment.pop();
                SegmentPool.recycle(segment);
            } else {
                segment.pos = i3;
            }
            return b;
        }
        throw new EOFException();
    }

    public static final byte[] commonReadByteArray(Buffer $this$commonReadByteArray) {
        Intrinsics.checkNotNullParameter($this$commonReadByteArray, "$this$commonReadByteArray");
        return $this$commonReadByteArray.readByteArray($this$commonReadByteArray.size());
    }

    public static final byte[] commonReadByteArray(Buffer $this$commonReadByteArray, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonReadByteArray, "$this$commonReadByteArray");
        if (!(byteCount >= 0 && byteCount <= ((long) Integer.MAX_VALUE))) {
            throw new IllegalArgumentException(("byteCount: " + byteCount).toString());
        } else if ($this$commonReadByteArray.size() >= byteCount) {
            byte[] bArr = new byte[((int) byteCount)];
            $this$commonReadByteArray.readFully(bArr);
            return bArr;
        } else {
            throw new EOFException();
        }
    }

    public static final ByteString commonReadByteString(Buffer $this$commonReadByteString) {
        Intrinsics.checkNotNullParameter($this$commonReadByteString, "$this$commonReadByteString");
        return $this$commonReadByteString.readByteString($this$commonReadByteString.size());
    }

    public static final ByteString commonReadByteString(Buffer $this$commonReadByteString, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonReadByteString, "$this$commonReadByteString");
        if (!(byteCount >= 0 && byteCount <= ((long) Integer.MAX_VALUE))) {
            throw new IllegalArgumentException(("byteCount: " + byteCount).toString());
        } else if ($this$commonReadByteString.size() < byteCount) {
            throw new EOFException();
        } else if (byteCount < ((long) 4096)) {
            return new ByteString($this$commonReadByteString.readByteArray(byteCount));
        } else {
            ByteString snapshot = $this$commonReadByteString.snapshot((int) byteCount);
            ByteString byteString = snapshot;
            $this$commonReadByteString.skip(byteCount);
            return snapshot;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00d4, code lost:
        if (r11 != r12) goto L_0x00e2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00d6, code lost:
        r1 = r18;
        r1.head = r9.pop();
        okio.SegmentPool.recycle(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00e2, code lost:
        r1 = r18;
        r9.pos = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00e7, code lost:
        if (r6 != false) goto L_0x00f2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00eb, code lost:
        if (r1.head != null) goto L_0x00ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00f2, code lost:
        r1.setSize$okio(r18.size() - ((long) r4));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00fb, code lost:
        if (r5 == false) goto L_0x00ff;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:?, code lost:
        return -r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:?, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final long commonReadDecimalLong(okio.Buffer r18) {
        /*
            r0 = r18
            r1 = 0
            java.lang.String r2 = "$this$commonReadDecimalLong"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r0, r2)
            long r2 = r18.size()
            r4 = 0
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 == 0) goto L_0x0101
            r2 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = -7
        L_0x0019:
            okio.Segment r9 = r0.head
            kotlin.jvm.internal.Intrinsics.checkNotNull(r9)
            byte[] r10 = r9.data
            int r11 = r9.pos
            int r12 = r9.limit
        L_0x0025:
            if (r11 >= r12) goto L_0x00cf
            byte r13 = r10[r11]
            r14 = 48
            byte r14 = (byte) r14
            if (r13 < r14) goto L_0x0089
            r15 = 57
            byte r15 = (byte) r15
            if (r13 > r15) goto L_0x0089
            int r14 = r14 - r13
            r15 = -922337203685477580(0xf333333333333334, double:-8.390303882365713E246)
            int r17 = (r2 > r15 ? 1 : (r2 == r15 ? 0 : -1))
            if (r17 < 0) goto L_0x0053
            int r15 = (r2 > r15 ? 1 : (r2 == r15 ? 0 : -1))
            if (r15 != 0) goto L_0x0048
            r15 = r1
            long r0 = (long) r14
            int r0 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1))
            if (r0 >= 0) goto L_0x0049
            goto L_0x0054
        L_0x0048:
            r15 = r1
        L_0x0049:
            r0 = 10
            long r2 = r2 * r0
            long r0 = (long) r14
            long r2 = r2 + r0
            r16 = r6
            r17 = r10
            goto L_0x0099
        L_0x0053:
            r15 = r1
        L_0x0054:
            okio.Buffer r0 = new okio.Buffer
            r0.<init>()
            okio.Buffer r0 = r0.writeDecimalLong((long) r2)
            okio.Buffer r0 = r0.writeByte((int) r13)
            if (r5 != 0) goto L_0x0066
            r0.readByte()
        L_0x0066:
            java.lang.NumberFormatException r1 = new java.lang.NumberFormatException
            r16 = r6
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            r17 = r10
            java.lang.String r10 = "Number too large: "
            java.lang.StringBuilder r6 = r6.append(r10)
            java.lang.String r10 = r0.readUtf8()
            java.lang.StringBuilder r6 = r6.append(r10)
            java.lang.String r6 = r6.toString()
            r1.<init>(r6)
            java.lang.Throwable r1 = (java.lang.Throwable) r1
            throw r1
        L_0x0089:
            r15 = r1
            r16 = r6
            r17 = r10
            r0 = 45
            byte r0 = (byte) r0
            if (r13 != r0) goto L_0x00a8
            if (r4 != 0) goto L_0x00a8
            r5 = 1
            r0 = 1
            long r7 = r7 - r0
        L_0x0099:
            int r11 = r11 + 1
            int r4 = r4 + 1
            r0 = r18
            r1 = r15
            r6 = r16
            r10 = r17
            goto L_0x0025
        L_0x00a8:
            if (r4 == 0) goto L_0x00ad
            r0 = 1
            r6 = r0
            goto L_0x00d4
        L_0x00ad:
            java.lang.NumberFormatException r0 = new java.lang.NumberFormatException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r6 = "Expected leading [0-9] or '-' character but was 0x"
            java.lang.StringBuilder r1 = r1.append(r6)
            java.lang.String r6 = okio.Util.toHexString((byte) r13)
            mt.Log1F380D.a((java.lang.Object) r6)
            java.lang.StringBuilder r1 = r1.append(r6)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            java.lang.Throwable r0 = (java.lang.Throwable) r0
            throw r0
        L_0x00cf:
            r15 = r1
            r16 = r6
            r17 = r10
        L_0x00d4:
            if (r11 != r12) goto L_0x00e2
            okio.Segment r0 = r9.pop()
            r1 = r18
            r1.head = r0
            okio.SegmentPool.recycle(r9)
            goto L_0x00e6
        L_0x00e2:
            r1 = r18
            r9.pos = r11
        L_0x00e6:
            if (r6 != 0) goto L_0x00f2
            okio.Segment r0 = r1.head
            if (r0 != 0) goto L_0x00ee
            goto L_0x00f2
        L_0x00ee:
            r0 = r1
            r1 = r15
            goto L_0x0019
        L_0x00f2:
            long r9 = r18.size()
            long r11 = (long) r4
            long r9 = r9 - r11
            r1.setSize$okio(r9)
            if (r5 == 0) goto L_0x00ff
            r9 = r2
            goto L_0x0100
        L_0x00ff:
            long r9 = -r2
        L_0x0100:
            return r9
        L_0x0101:
            r15 = r1
            r1 = r0
            java.io.EOFException r0 = new java.io.EOFException
            r0.<init>()
            java.lang.Throwable r0 = (java.lang.Throwable) r0
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.internal.BufferKt.commonReadDecimalLong(okio.Buffer):long");
    }

    public static final void commonReadFully(Buffer $this$commonReadFully, Buffer sink, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonReadFully, "$this$commonReadFully");
        Intrinsics.checkNotNullParameter(sink, "sink");
        if ($this$commonReadFully.size() >= byteCount) {
            sink.write($this$commonReadFully, byteCount);
        } else {
            sink.write($this$commonReadFully, $this$commonReadFully.size());
            throw new EOFException();
        }
    }

    public static final void commonReadFully(Buffer $this$commonReadFully, byte[] sink) {
        Intrinsics.checkNotNullParameter($this$commonReadFully, "$this$commonReadFully");
        Intrinsics.checkNotNullParameter(sink, "sink");
        int i = 0;
        while (i < sink.length) {
            int read = $this$commonReadFully.read(sink, i, sink.length - i);
            if (read != -1) {
                i += read;
            } else {
                throw new EOFException();
            }
        }
    }

    public static final int commonReadInt(Buffer $this$commonReadInt) {
        Intrinsics.checkNotNullParameter($this$commonReadInt, "$this$commonReadInt");
        if ($this$commonReadInt.size() >= 4) {
            Segment segment = $this$commonReadInt.head;
            Intrinsics.checkNotNull(segment);
            int i = segment.pos;
            int i2 = segment.limit;
            if (((long) (i2 - i)) < 4) {
                return (($this$commonReadInt.readByte() & UByte.MAX_VALUE) << 24) | (($this$commonReadInt.readByte() & UByte.MAX_VALUE) << 16) | (($this$commonReadInt.readByte() & UByte.MAX_VALUE) << 8) | ($this$commonReadInt.readByte() & UByte.MAX_VALUE);
            }
            byte[] bArr = segment.data;
            int i3 = i + 1;
            int i4 = i3 + 1;
            byte b = ((bArr[i] & UByte.MAX_VALUE) << 24) | ((bArr[i3] & UByte.MAX_VALUE) << 16);
            int i5 = i4 + 1;
            byte b2 = b | ((bArr[i4] & UByte.MAX_VALUE) << 8);
            int i6 = i5 + 1;
            byte b3 = b2 | (bArr[i5] & UByte.MAX_VALUE);
            $this$commonReadInt.setSize$okio($this$commonReadInt.size() - 4);
            if (i6 == i2) {
                $this$commonReadInt.head = segment.pop();
                SegmentPool.recycle(segment);
            } else {
                segment.pos = i6;
            }
            return b3;
        }
        throw new EOFException();
    }

    public static final long commonReadLong(Buffer $this$commonReadLong) {
        Buffer buffer = $this$commonReadLong;
        Intrinsics.checkNotNullParameter($this$commonReadLong, "$this$commonReadLong");
        if ($this$commonReadLong.size() >= 8) {
            Segment segment = buffer.head;
            Intrinsics.checkNotNull(segment);
            int i = segment.pos;
            int i2 = segment.limit;
            if (((long) (i2 - i)) < 8) {
                return ((((long) $this$commonReadLong.readInt()) & 4294967295L) << 32) | (((long) $this$commonReadLong.readInt()) & 4294967295L);
            }
            byte[] bArr = segment.data;
            int i3 = i + 1;
            int i4 = i3 + 1;
            long j = (((long) bArr[i3]) & 255) << 48;
            int i5 = i4 + 1;
            int i6 = i5 + 1;
            int i7 = i6 + 1;
            int i8 = i7 + 1;
            long j2 = j | ((255 & ((long) bArr[i])) << 56) | ((255 & ((long) bArr[i4])) << 40) | ((((long) bArr[i5]) & 255) << 32) | ((255 & ((long) bArr[i6])) << 24) | ((((long) bArr[i7]) & 255) << 16);
            int i9 = i8 + 1;
            int i10 = i9 + 1;
            long j3 = j2 | ((255 & ((long) bArr[i8])) << 8) | (((long) bArr[i9]) & 255);
            $this$commonReadLong.setSize$okio($this$commonReadLong.size() - 8);
            if (i10 == i2) {
                buffer.head = segment.pop();
                SegmentPool.recycle(segment);
            } else {
                segment.pos = i10;
            }
            return j3;
        }
        throw new EOFException();
    }

    public static final short commonReadShort(Buffer $this$commonReadShort) {
        Intrinsics.checkNotNullParameter($this$commonReadShort, "$this$commonReadShort");
        if ($this$commonReadShort.size() >= 2) {
            Segment segment = $this$commonReadShort.head;
            Intrinsics.checkNotNull(segment);
            int i = segment.pos;
            int i2 = segment.limit;
            if (i2 - i < 2) {
                return (short) ((($this$commonReadShort.readByte() & UByte.MAX_VALUE) << 8) | ($this$commonReadShort.readByte() & UByte.MAX_VALUE));
            }
            byte[] bArr = segment.data;
            int i3 = i + 1;
            int i4 = i3 + 1;
            byte b = ((bArr[i] & UByte.MAX_VALUE) << 8) | (bArr[i3] & UByte.MAX_VALUE);
            $this$commonReadShort.setSize$okio($this$commonReadShort.size() - 2);
            if (i4 == i2) {
                $this$commonReadShort.head = segment.pop();
                SegmentPool.recycle(segment);
            } else {
                segment.pos = i4;
            }
            return (short) b;
        }
        throw new EOFException();
    }

    public static final int commonSelect(Buffer $this$commonSelect, Options options) {
        Intrinsics.checkNotNullParameter($this$commonSelect, "$this$commonSelect");
        Intrinsics.checkNotNullParameter(options, "options");
        int selectPrefix$default = selectPrefix$default($this$commonSelect, options, false, 2, (Object) null);
        if (selectPrefix$default == -1) {
            return -1;
        }
        $this$commonSelect.skip((long) options.getByteStrings$okio()[selectPrefix$default].size());
        return selectPrefix$default;
    }

    public static final void commonSkip(Buffer $this$commonSkip, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonSkip, "$this$commonSkip");
        long j = byteCount;
        while (j > 0) {
            Segment segment = $this$commonSkip.head;
            if (segment != null) {
                int min = (int) Math.min(j, (long) (segment.limit - segment.pos));
                $this$commonSkip.setSize$okio($this$commonSkip.size() - ((long) min));
                j -= (long) min;
                segment.pos += min;
                if (segment.pos == segment.limit) {
                    $this$commonSkip.head = segment.pop();
                    SegmentPool.recycle(segment);
                }
            } else {
                throw new EOFException();
            }
        }
    }

    public static final ByteString commonSnapshot(Buffer $this$commonSnapshot) {
        Intrinsics.checkNotNullParameter($this$commonSnapshot, "$this$commonSnapshot");
        if ($this$commonSnapshot.size() <= ((long) Integer.MAX_VALUE)) {
            return $this$commonSnapshot.snapshot((int) $this$commonSnapshot.size());
        }
        throw new IllegalStateException(("size > Int.MAX_VALUE: " + $this$commonSnapshot.size()).toString());
    }

    public static final ByteString commonSnapshot(Buffer $this$commonSnapshot, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonSnapshot, "$this$commonSnapshot");
        if (byteCount == 0) {
            return ByteString.EMPTY;
        }
        Util.checkOffsetAndCount($this$commonSnapshot.size(), 0, (long) byteCount);
        int i = 0;
        int i2 = 0;
        Segment segment = $this$commonSnapshot.head;
        while (i < byteCount) {
            Intrinsics.checkNotNull(segment);
            if (segment.limit != segment.pos) {
                i += segment.limit - segment.pos;
                i2++;
                segment = segment.next;
            } else {
                throw new AssertionError("s.limit == s.pos");
            }
        }
        byte[][] bArr = new byte[i2][];
        int[] iArr = new int[(i2 * 2)];
        int i3 = 0;
        int i4 = 0;
        Segment segment2 = $this$commonSnapshot.head;
        while (i3 < byteCount) {
            Intrinsics.checkNotNull(segment2);
            bArr[i4] = segment2.data;
            i3 += segment2.limit - segment2.pos;
            iArr[i4] = Math.min(i3, byteCount);
            iArr[((Object[]) bArr).length + i4] = segment2.pos;
            segment2.shared = true;
            i4++;
            segment2 = segment2.next;
        }
        return new SegmentedByteString(bArr, iArr);
    }

    public static final Segment commonWritableSegment(Buffer $this$commonWritableSegment, int minimumCapacity) {
        Intrinsics.checkNotNullParameter($this$commonWritableSegment, "$this$commonWritableSegment");
        boolean z = true;
        if (minimumCapacity < 1 || minimumCapacity > 8192) {
            z = false;
        }
        if (!z) {
            throw new IllegalArgumentException("unexpected capacity".toString());
        } else if ($this$commonWritableSegment.head == null) {
            Segment take = SegmentPool.take();
            $this$commonWritableSegment.head = take;
            take.prev = take;
            take.next = take;
            return take;
        } else {
            Segment segment = $this$commonWritableSegment.head;
            Intrinsics.checkNotNull(segment);
            Segment segment2 = segment.prev;
            Intrinsics.checkNotNull(segment2);
            return (segment2.limit + minimumCapacity > 8192 || !segment2.owner) ? segment2.push(SegmentPool.take()) : segment2;
        }
    }

    public static final Buffer commonWrite(Buffer $this$commonWrite, ByteString byteString, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(byteString, "byteString");
        byteString.write$okio($this$commonWrite, offset, byteCount);
        return $this$commonWrite;
    }

    public static final Buffer commonWrite(Buffer $this$commonWrite, Source source, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(source, "source");
        long j = byteCount;
        while (j > 0) {
            long read = source.read($this$commonWrite, j);
            if (read != -1) {
                j -= read;
            } else {
                throw new EOFException();
            }
        }
        return $this$commonWrite;
    }

    public static final Buffer commonWrite(Buffer $this$commonWrite, byte[] source) {
        Intrinsics.checkNotNullParameter($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(source, "source");
        return $this$commonWrite.write(source, 0, source.length);
    }

    public static final Buffer commonWrite(Buffer $this$commonWrite, byte[] source, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(source, "source");
        int i = offset;
        Util.checkOffsetAndCount((long) source.length, (long) i, (long) byteCount);
        int i2 = i + byteCount;
        while (i < i2) {
            Segment writableSegment$okio = $this$commonWrite.writableSegment$okio(1);
            int min = Math.min(i2 - i, 8192 - writableSegment$okio.limit);
            ArraysKt.copyInto(source, writableSegment$okio.data, writableSegment$okio.limit, i, i + min);
            i += min;
            writableSegment$okio.limit += min;
        }
        $this$commonWrite.setSize$okio($this$commonWrite.size() + ((long) byteCount));
        return $this$commonWrite;
    }

    public static final void commonWrite(Buffer $this$commonWrite, Buffer source, long byteCount) {
        Segment segment;
        Intrinsics.checkNotNullParameter($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(source, "source");
        long j = byteCount;
        if (source != $this$commonWrite) {
            Util.checkOffsetAndCount(source.size(), 0, j);
            while (j > 0) {
                Segment segment2 = source.head;
                Intrinsics.checkNotNull(segment2);
                int i = segment2.limit;
                Segment segment3 = source.head;
                Intrinsics.checkNotNull(segment3);
                if (j < ((long) (i - segment3.pos))) {
                    if ($this$commonWrite.head != null) {
                        Segment segment4 = $this$commonWrite.head;
                        Intrinsics.checkNotNull(segment4);
                        segment = segment4.prev;
                    } else {
                        segment = null;
                    }
                    if (segment != null && segment.owner) {
                        if ((((long) segment.limit) + j) - ((long) (segment.shared ? 0 : segment.pos)) <= ((long) 8192)) {
                            Segment segment5 = source.head;
                            Intrinsics.checkNotNull(segment5);
                            segment5.writeTo(segment, (int) j);
                            source.setSize$okio(source.size() - j);
                            $this$commonWrite.setSize$okio($this$commonWrite.size() + j);
                            return;
                        }
                    }
                    Segment segment6 = source.head;
                    Intrinsics.checkNotNull(segment6);
                    source.head = segment6.split((int) j);
                }
                Segment segment7 = source.head;
                Intrinsics.checkNotNull(segment7);
                long j2 = (long) (segment7.limit - segment7.pos);
                source.head = segment7.pop();
                if ($this$commonWrite.head == null) {
                    $this$commonWrite.head = segment7;
                    segment7.prev = segment7;
                    segment7.next = segment7.prev;
                } else {
                    Segment segment8 = $this$commonWrite.head;
                    Intrinsics.checkNotNull(segment8);
                    Segment segment9 = segment8.prev;
                    Intrinsics.checkNotNull(segment9);
                    segment9.push(segment7).compact();
                }
                source.setSize$okio(source.size() - j2);
                $this$commonWrite.setSize$okio($this$commonWrite.size() + j2);
                j -= j2;
            }
            return;
        }
        throw new IllegalArgumentException("source == this".toString());
    }

    public static /* synthetic */ Buffer commonWrite$default(Buffer $this$commonWrite, ByteString byteString, int offset, int byteCount, int i, Object obj) {
        if ((i & 2) != 0) {
            offset = 0;
        }
        if ((i & 4) != 0) {
            byteCount = byteString.size();
        }
        Intrinsics.checkNotNullParameter($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(byteString, "byteString");
        byteString.write$okio($this$commonWrite, offset, byteCount);
        return $this$commonWrite;
    }

    public static final long commonWriteAll(Buffer $this$commonWriteAll, Source source) {
        Intrinsics.checkNotNullParameter($this$commonWriteAll, "$this$commonWriteAll");
        Intrinsics.checkNotNullParameter(source, "source");
        long j = 0;
        while (true) {
            long read = source.read($this$commonWriteAll, (long) 8192);
            if (read == -1) {
                return j;
            }
            j += read;
        }
    }

    public static final Buffer commonWriteByte(Buffer $this$commonWriteByte, int b) {
        Intrinsics.checkNotNullParameter($this$commonWriteByte, "$this$commonWriteByte");
        Segment writableSegment$okio = $this$commonWriteByte.writableSegment$okio(1);
        byte[] bArr = writableSegment$okio.data;
        int i = writableSegment$okio.limit;
        writableSegment$okio.limit = i + 1;
        bArr[i] = (byte) b;
        $this$commonWriteByte.setSize$okio($this$commonWriteByte.size() + 1);
        return $this$commonWriteByte;
    }

    public static final Buffer commonWriteDecimalLong(Buffer $this$commonWriteDecimalLong, long v) {
        Buffer buffer = $this$commonWriteDecimalLong;
        Intrinsics.checkNotNullParameter(buffer, "$this$commonWriteDecimalLong");
        long j = v;
        if (j == 0) {
            return buffer.writeByte(48);
        }
        boolean z = false;
        if (j < 0) {
            j = -j;
            if (j < 0) {
                return buffer.writeUtf8("-9223372036854775808");
            }
            z = true;
        }
        int i = j < 100000000 ? j < WorkRequest.MIN_BACKOFF_MILLIS ? j < 100 ? j < 10 ? 1 : 2 : j < 1000 ? 3 : 4 : j < 1000000 ? j < 100000 ? 5 : 6 : j < 10000000 ? 7 : 8 : j < 1000000000000L ? j < RealConnection.IDLE_CONNECTION_HEALTHY_NS ? j < 1000000000 ? 9 : 10 : j < 100000000000L ? 11 : 12 : j < 1000000000000000L ? j < 10000000000000L ? 13 : j < 100000000000000L ? 14 : 15 : j < 100000000000000000L ? j < 10000000000000000L ? 16 : 17 : j < 1000000000000000000L ? 18 : 19;
        if (z) {
            i++;
        }
        Segment writableSegment$okio = buffer.writableSegment$okio(i);
        byte[] bArr = writableSegment$okio.data;
        int i2 = writableSegment$okio.limit + i;
        while (j != 0) {
            long j2 = (long) 10;
            i2--;
            bArr[i2] = getHEX_DIGIT_BYTES()[(int) (j % j2)];
            j /= j2;
        }
        if (z) {
            bArr[i2 - 1] = (byte) 45;
        }
        writableSegment$okio.limit += i;
        buffer.setSize$okio($this$commonWriteDecimalLong.size() + ((long) i));
        return buffer;
    }

    public static final Buffer commonWriteHexadecimalUnsignedLong(Buffer $this$commonWriteHexadecimalUnsignedLong, long v) {
        Buffer buffer = $this$commonWriteHexadecimalUnsignedLong;
        Intrinsics.checkNotNullParameter(buffer, "$this$commonWriteHexadecimalUnsignedLong");
        long j = v;
        if (j == 0) {
            return buffer.writeByte(48);
        }
        long j2 = j;
        long j3 = j2 | (j2 >>> 1);
        long j4 = j3 | (j3 >>> 2);
        long j5 = j4 | (j4 >>> 4);
        long j6 = j5 | (j5 >>> 8);
        long j7 = j6 | (j6 >>> 16);
        long j8 = j7 | (j7 >>> 32);
        long j9 = j8 - ((j8 >>> 1) & 6148914691236517205L);
        long j10 = ((j9 >>> 2) & 3689348814741910323L) + (3689348814741910323L & j9);
        long j11 = ((j10 >>> 4) + j10) & 1085102592571150095L;
        long j12 = j11 + (j11 >>> 8);
        long j13 = j12 + (j12 >>> 16);
        int i = (int) ((((long) 3) + ((j13 & 63) + (63 & (j13 >>> 32)))) / ((long) 4));
        Segment writableSegment$okio = buffer.writableSegment$okio(i);
        byte[] bArr = writableSegment$okio.data;
        int i2 = writableSegment$okio.limit;
        for (int i3 = (writableSegment$okio.limit + i) - 1; i3 >= i2; i3--) {
            bArr[i3] = getHEX_DIGIT_BYTES()[(int) (15 & j)];
            j >>>= 4;
        }
        writableSegment$okio.limit += i;
        buffer.setSize$okio($this$commonWriteHexadecimalUnsignedLong.size() + ((long) i));
        return buffer;
    }

    public static final Buffer commonWriteInt(Buffer $this$commonWriteInt, int i) {
        Intrinsics.checkNotNullParameter($this$commonWriteInt, "$this$commonWriteInt");
        Segment writableSegment$okio = $this$commonWriteInt.writableSegment$okio(4);
        byte[] bArr = writableSegment$okio.data;
        int i2 = writableSegment$okio.limit;
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((i >>> 24) & 255);
        int i4 = i3 + 1;
        bArr[i3] = (byte) ((i >>> 16) & 255);
        int i5 = i4 + 1;
        bArr[i4] = (byte) ((i >>> 8) & 255);
        bArr[i5] = (byte) (i & 255);
        writableSegment$okio.limit = i5 + 1;
        $this$commonWriteInt.setSize$okio($this$commonWriteInt.size() + 4);
        return $this$commonWriteInt;
    }

    public static final Buffer commonWriteLong(Buffer $this$commonWriteLong, long v) {
        Intrinsics.checkNotNullParameter($this$commonWriteLong, "$this$commonWriteLong");
        Segment writableSegment$okio = $this$commonWriteLong.writableSegment$okio(8);
        byte[] bArr = writableSegment$okio.data;
        int i = writableSegment$okio.limit;
        int i2 = i + 1;
        bArr[i] = (byte) ((int) ((v >>> 56) & 255));
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((int) ((v >>> 48) & 255));
        int i4 = i3 + 1;
        bArr[i3] = (byte) ((int) ((v >>> 40) & 255));
        int i5 = i4 + 1;
        bArr[i4] = (byte) ((int) ((v >>> 32) & 255));
        int i6 = i5 + 1;
        bArr[i5] = (byte) ((int) ((v >>> 24) & 255));
        int i7 = i6 + 1;
        bArr[i6] = (byte) ((int) ((v >>> 16) & 255));
        int i8 = i7 + 1;
        bArr[i7] = (byte) ((int) ((v >>> 8) & 255));
        bArr[i8] = (byte) ((int) (v & 255));
        writableSegment$okio.limit = i8 + 1;
        $this$commonWriteLong.setSize$okio($this$commonWriteLong.size() + 8);
        return $this$commonWriteLong;
    }

    public static final Buffer commonWriteShort(Buffer $this$commonWriteShort, int s) {
        Intrinsics.checkNotNullParameter($this$commonWriteShort, "$this$commonWriteShort");
        Segment writableSegment$okio = $this$commonWriteShort.writableSegment$okio(2);
        byte[] bArr = writableSegment$okio.data;
        int i = writableSegment$okio.limit;
        int i2 = i + 1;
        bArr[i] = (byte) ((s >>> 8) & 255);
        bArr[i2] = (byte) (s & 255);
        writableSegment$okio.limit = i2 + 1;
        $this$commonWriteShort.setSize$okio($this$commonWriteShort.size() + 2);
        return $this$commonWriteShort;
    }

    public static final Buffer commonWriteUtf8(Buffer $this$commonWriteUtf8, String string, int beginIndex, int endIndex) {
        int i;
        Buffer buffer = $this$commonWriteUtf8;
        String str = string;
        int i2 = beginIndex;
        int i3 = endIndex;
        Intrinsics.checkNotNullParameter(buffer, "$this$commonWriteUtf8");
        Intrinsics.checkNotNullParameter(str, TypedValues.Custom.S_STRING);
        int i4 = 1;
        if (i2 >= 0) {
            if (i3 >= i2) {
                if (i3 <= string.length()) {
                    int i5 = beginIndex;
                    while (i5 < i3) {
                        char charAt = str.charAt(i5);
                        if (charAt < 128) {
                            Segment writableSegment$okio = buffer.writableSegment$okio(i4);
                            byte[] bArr = writableSegment$okio.data;
                            int i6 = writableSegment$okio.limit - i5;
                            int min = Math.min(i3, 8192 - i6);
                            int i7 = i5 + 1;
                            bArr[i5 + i6] = (byte) charAt;
                            while (i7 < min) {
                                char charAt2 = str.charAt(i7);
                                if (charAt2 >= 128) {
                                    break;
                                }
                                bArr[i7 + i6] = (byte) charAt2;
                                i7++;
                            }
                            int i8 = (i7 + i6) - writableSegment$okio.limit;
                            writableSegment$okio.limit += i8;
                            buffer.setSize$okio(((long) i8) + $this$commonWriteUtf8.size());
                            i5 = i7;
                            i = 1;
                        } else if (charAt < 2048) {
                            Segment writableSegment$okio2 = buffer.writableSegment$okio(2);
                            writableSegment$okio2.data[writableSegment$okio2.limit] = (byte) ((charAt >> 6) | 192);
                            writableSegment$okio2.data[writableSegment$okio2.limit + 1] = (byte) (128 | (charAt & '?'));
                            writableSegment$okio2.limit += 2;
                            buffer.setSize$okio($this$commonWriteUtf8.size() + 2);
                            i5++;
                            i = 1;
                        } else if (charAt < 55296 || charAt > 57343) {
                            Segment writableSegment$okio3 = buffer.writableSegment$okio(3);
                            writableSegment$okio3.data[writableSegment$okio3.limit] = (byte) ((charAt >> 12) | 224);
                            i = 1;
                            writableSegment$okio3.data[writableSegment$okio3.limit + 1] = (byte) ((63 & (charAt >> 6)) | 128);
                            writableSegment$okio3.data[writableSegment$okio3.limit + 2] = (byte) ((charAt & '?') | 128);
                            writableSegment$okio3.limit += 3;
                            buffer.setSize$okio($this$commonWriteUtf8.size() + 3);
                            i5++;
                        } else {
                            char charAt3 = i5 + 1 < i3 ? str.charAt(i5 + 1) : 0;
                            if (charAt > 56319 || 56320 > charAt3 || 57343 < charAt3) {
                                buffer.writeByte(63);
                                i5++;
                                i = 1;
                            } else {
                                int i9 = (((charAt & 1023) << 10) | (charAt3 & 1023)) + CharCompanionObject.MIN_VALUE;
                                Segment writableSegment$okio4 = buffer.writableSegment$okio(4);
                                writableSegment$okio4.data[writableSegment$okio4.limit] = (byte) ((i9 >> 18) | 240);
                                writableSegment$okio4.data[writableSegment$okio4.limit + 1] = (byte) (((i9 >> 12) & 63) | 128);
                                writableSegment$okio4.data[writableSegment$okio4.limit + 2] = (byte) (((i9 >> 6) & 63) | 128);
                                writableSegment$okio4.data[writableSegment$okio4.limit + 3] = (byte) (128 | (i9 & 63));
                                writableSegment$okio4.limit += 4;
                                buffer.setSize$okio($this$commonWriteUtf8.size() + 4);
                                i5 += 2;
                                i = 1;
                            }
                        }
                        i4 = i;
                    }
                    return buffer;
                }
                throw new IllegalArgumentException(("endIndex > string.length: " + i3 + " > " + string.length()).toString());
            }
            throw new IllegalArgumentException(("endIndex < beginIndex: " + i3 + " < " + i2).toString());
        }
        throw new IllegalArgumentException(("beginIndex < 0: " + i2).toString());
    }

    public static final byte[] getHEX_DIGIT_BYTES() {
        return HEX_DIGIT_BYTES;
    }

    public static final boolean rangeEquals(Segment segment, int segmentPos, byte[] bytes, int bytesOffset, int bytesLimit) {
        Intrinsics.checkNotNullParameter(segment, "segment");
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        Segment segment2 = segment;
        int i = segmentPos;
        int i2 = segment2.limit;
        byte[] bArr = segment2.data;
        for (int i3 = bytesOffset; i3 < bytesLimit; i3++) {
            if (i == i2) {
                Segment segment3 = segment2.next;
                Intrinsics.checkNotNull(segment3);
                segment2 = segment3;
                bArr = segment2.data;
                i = segment2.pos;
                i2 = segment2.limit;
            }
            if (bArr[i] != bytes[i3]) {
                return false;
            }
            i++;
        }
        return true;
    }

    public static final String readUtf8Line(Buffer $this$readUtf8Line, long newline) {
        Intrinsics.checkNotNullParameter($this$readUtf8Line, "$this$readUtf8Line");
        if (newline <= 0 || $this$readUtf8Line.getByte(newline - 1) != ((byte) 13)) {
            String readUtf8 = $this$readUtf8Line.readUtf8(newline);
            $this$readUtf8Line.skip(1);
            return readUtf8;
        }
        String readUtf82 = $this$readUtf8Line.readUtf8(newline - 1);
        $this$readUtf8Line.skip(2);
        return readUtf82;
    }

    public static final <T> T seek(Buffer $this$seek, long fromIndex, Function2<? super Segment, ? super Long, ? extends T> lambda) {
        Intrinsics.checkNotNullParameter($this$seek, "$this$seek");
        Intrinsics.checkNotNullParameter(lambda, "lambda");
        Segment segment = $this$seek.head;
        if (segment == null) {
            return lambda.invoke(null, -1L);
        }
        if ($this$seek.size() - fromIndex < fromIndex) {
            long size = $this$seek.size();
            while (size > fromIndex) {
                Segment segment2 = segment.prev;
                Intrinsics.checkNotNull(segment2);
                segment = segment2;
                size -= (long) (segment.limit - segment.pos);
            }
            return lambda.invoke(segment, Long.valueOf(size));
        }
        long j = 0;
        while (true) {
            long j2 = ((long) (segment.limit - segment.pos)) + j;
            if (j2 > fromIndex) {
                return lambda.invoke(segment, Long.valueOf(j));
            }
            Segment segment3 = segment.next;
            Intrinsics.checkNotNull(segment3);
            segment = segment3;
            j = j2;
        }
    }

    public static final int selectPrefix(Buffer $this$selectPrefix, Options options, boolean selectTruncated) {
        int i;
        int i2;
        Buffer buffer = $this$selectPrefix;
        Intrinsics.checkNotNullParameter(buffer, "$this$selectPrefix");
        Intrinsics.checkNotNullParameter(options, "options");
        Segment segment = buffer.head;
        int i3 = -1;
        if (segment == null) {
            return selectTruncated ? -2 : -1;
        }
        Segment segment2 = segment;
        byte[] bArr = segment.data;
        int i4 = segment.pos;
        int i5 = segment.limit;
        int[] trie$okio = options.getTrie$okio();
        int i6 = 0;
        int i7 = -1;
        loop0:
        while (true) {
            int i8 = i6 + 1;
            int i9 = trie$okio[i6];
            int i10 = i8 + 1;
            int i11 = trie$okio[i8];
            if (i11 != i3) {
                i7 = i11;
            }
            if (segment2 == null) {
                break;
            }
            if (i9 < 0) {
                int i12 = i10 + (i9 * -1);
                while (true) {
                    int i13 = i4 + 1;
                    int i14 = i10 + 1;
                    if ((bArr[i4] & UByte.MAX_VALUE) != trie$okio[i10]) {
                        return i7;
                    }
                    boolean z = i14 == i12;
                    if (i13 == i5) {
                        Intrinsics.checkNotNull(segment2);
                        Segment segment3 = segment2.next;
                        Intrinsics.checkNotNull(segment3);
                        segment2 = segment3;
                        i2 = segment2.pos;
                        bArr = segment2.data;
                        i5 = segment2.limit;
                        if (segment2 == segment) {
                            if (!z) {
                                int i15 = i2;
                                int i16 = i14;
                                break loop0;
                            }
                            segment2 = null;
                        }
                    } else {
                        i2 = i13;
                    }
                    if (z) {
                        i = trie$okio[i14];
                        i4 = i2;
                        break;
                    }
                    i4 = i2;
                    i10 = i14;
                    Buffer buffer2 = $this$selectPrefix;
                }
            } else {
                int i17 = i9;
                int i18 = i4 + 1;
                byte b = bArr[i4] & UByte.MAX_VALUE;
                int i19 = i10 + i17;
                while (i10 != i19) {
                    if (b == trie$okio[i10]) {
                        i = trie$okio[i10 + i17];
                        if (i18 == i5) {
                            Segment segment4 = segment2.next;
                            Intrinsics.checkNotNull(segment4);
                            segment2 = segment4;
                            int i20 = segment2.pos;
                            bArr = segment2.data;
                            i5 = segment2.limit;
                            if (segment2 == segment) {
                                segment2 = null;
                                i4 = i20;
                                int i21 = i10;
                            } else {
                                i4 = i20;
                                int i22 = i10;
                            }
                        } else {
                            i4 = i18;
                            int i23 = i10;
                        }
                    } else {
                        i10++;
                    }
                }
                return i7;
            }
            if (i >= 0) {
                return i;
            }
            i6 = -i;
            i3 = -1;
            Buffer buffer3 = $this$selectPrefix;
        }
        if (selectTruncated) {
            return -2;
        }
        return i7;
    }

    public static /* synthetic */ int selectPrefix$default(Buffer buffer, Options options, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return selectPrefix(buffer, options, z);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00b3, code lost:
        if (r9 != r10) goto L_0x00bf;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00b5, code lost:
        r15.head = r7.pop();
        okio.SegmentPool.recycle(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00bf, code lost:
        r7.pos = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00c2, code lost:
        if (r6 != false) goto L_0x00c8;
     */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x008f  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0091 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final long commonReadHexadecimalUnsignedLong(okio.Buffer r15) {
        /*
            r0 = 0
            java.lang.String r1 = "$this$commonReadHexadecimalUnsignedLong"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r15, r1)
            long r1 = r15.size()
            r3 = 0
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 == 0) goto L_0x00d2
            r1 = 0
            r5 = 0
            r6 = 0
        L_0x0014:
            okio.Segment r7 = r15.head
            kotlin.jvm.internal.Intrinsics.checkNotNull(r7)
            byte[] r8 = r7.data
            int r9 = r7.pos
            int r10 = r7.limit
        L_0x0020:
            if (r9 >= r10) goto L_0x00b3
            r11 = 0
            byte r12 = r8[r9]
            r13 = 48
            byte r13 = (byte) r13
            if (r12 < r13) goto L_0x0032
            r14 = 57
            byte r14 = (byte) r14
            if (r12 > r14) goto L_0x0032
            int r11 = r12 - r13
            goto L_0x004f
        L_0x0032:
            r13 = 97
            byte r13 = (byte) r13
            if (r12 < r13) goto L_0x0041
            r14 = 102(0x66, float:1.43E-43)
            byte r14 = (byte) r14
            if (r12 > r14) goto L_0x0041
            int r13 = r12 - r13
            int r11 = r13 + 10
            goto L_0x004f
        L_0x0041:
            r13 = 65
            byte r13 = (byte) r13
            if (r12 < r13) goto L_0x008d
            r14 = 70
            byte r14 = (byte) r14
            if (r12 > r14) goto L_0x008d
            int r13 = r12 - r13
            int r11 = r13 + 10
        L_0x004f:
            r13 = -1152921504606846976(0xf000000000000000, double:-3.105036184601418E231)
            long r13 = r13 & r1
            int r13 = (r13 > r3 ? 1 : (r13 == r3 ? 0 : -1))
            if (r13 != 0) goto L_0x0061
            r13 = 4
            long r1 = r1 << r13
            long r13 = (long) r11
            long r1 = r1 | r13
            int r9 = r9 + 1
            int r5 = r5 + 1
            goto L_0x0020
        L_0x0061:
            okio.Buffer r3 = new okio.Buffer
            r3.<init>()
            okio.Buffer r3 = r3.writeHexadecimalUnsignedLong((long) r1)
            okio.Buffer r3 = r3.writeByte((int) r12)
            java.lang.NumberFormatException r4 = new java.lang.NumberFormatException
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = "Number too large: "
            java.lang.StringBuilder r13 = r13.append(r14)
            java.lang.String r14 = r3.readUtf8()
            java.lang.StringBuilder r13 = r13.append(r14)
            java.lang.String r13 = r13.toString()
            r4.<init>(r13)
            java.lang.Throwable r4 = (java.lang.Throwable) r4
            throw r4
        L_0x008d:
            if (r5 == 0) goto L_0x0091
            r6 = 1
            goto L_0x00b3
        L_0x0091:
            java.lang.NumberFormatException r3 = new java.lang.NumberFormatException
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r13 = "Expected leading [0-9a-fA-F] character but was 0x"
            java.lang.StringBuilder r4 = r4.append(r13)
            java.lang.String r13 = okio.Util.toHexString((byte) r12)
            mt.Log1F380D.a((java.lang.Object) r13)
            java.lang.StringBuilder r4 = r4.append(r13)
            java.lang.String r4 = r4.toString()
            r3.<init>(r4)
            java.lang.Throwable r3 = (java.lang.Throwable) r3
            throw r3
        L_0x00b3:
            if (r9 != r10) goto L_0x00bf
            okio.Segment r11 = r7.pop()
            r15.head = r11
            okio.SegmentPool.recycle(r7)
            goto L_0x00c1
        L_0x00bf:
            r7.pos = r9
        L_0x00c1:
            if (r6 != 0) goto L_0x00c8
            okio.Segment r11 = r15.head
            if (r11 != 0) goto L_0x0014
        L_0x00c8:
            long r3 = r15.size()
            long r7 = (long) r5
            long r3 = r3 - r7
            r15.setSize$okio(r3)
            return r1
        L_0x00d2:
            java.io.EOFException r1 = new java.io.EOFException
            r1.<init>()
            java.lang.Throwable r1 = (java.lang.Throwable) r1
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.internal.BufferKt.commonReadHexadecimalUnsignedLong(okio.Buffer):long");
    }

    public static final String commonReadUtf8(Buffer $this$commonReadUtf8, long byteCount) {
        Intrinsics.checkNotNullParameter($this$commonReadUtf8, "$this$commonReadUtf8");
        if (!(byteCount >= 0 && byteCount <= ((long) Integer.MAX_VALUE))) {
            throw new IllegalArgumentException(("byteCount: " + byteCount).toString());
        } else if ($this$commonReadUtf8.size() < byteCount) {
            throw new EOFException();
        } else if (byteCount == 0) {
            return HttpUrl.FRAGMENT_ENCODE_SET;
        } else {
            Segment segment = $this$commonReadUtf8.head;
            Intrinsics.checkNotNull(segment);
            if (((long) segment.pos) + byteCount > ((long) segment.limit)) {
                String commonToUtf8String$default = _Utf8Kt.commonToUtf8String$default($this$commonReadUtf8.readByteArray(byteCount), 0, 0, 3, (Object) null);
                Log1F380D.a((Object) commonToUtf8String$default);
                return commonToUtf8String$default;
            }
            String commonToUtf8String = _Utf8Kt.commonToUtf8String(segment.data, segment.pos, segment.pos + ((int) byteCount));
            Log1F380D.a((Object) commonToUtf8String);
            segment.pos += (int) byteCount;
            $this$commonReadUtf8.setSize$okio($this$commonReadUtf8.size() - byteCount);
            if (segment.pos == segment.limit) {
                $this$commonReadUtf8.head = segment.pop();
                SegmentPool.recycle(segment);
            }
            return commonToUtf8String;
        }
    }

    public static final int commonReadUtf8CodePoint(Buffer $this$commonReadUtf8CodePoint) {
        byte b;
        int i;
        byte b2;
        Intrinsics.checkNotNullParameter($this$commonReadUtf8CodePoint, "$this$commonReadUtf8CodePoint");
        if ($this$commonReadUtf8CodePoint.size() != 0) {
            byte b3 = $this$commonReadUtf8CodePoint.getByte(0);
            if ((128 & b3) == 0) {
                b2 = b3 & ByteCompanionObject.MAX_VALUE;
                i = 1;
                b = 0;
            } else if ((224 & b3) == 192) {
                b2 = b3 & 31;
                i = 2;
                b = ByteCompanionObject.MIN_VALUE;
            } else if ((240 & b3) == 224) {
                b2 = b3 & 15;
                i = 3;
                b = UByte.MIN_VALUE;
            } else if ((248 & b3) == 240) {
                b2 = b3 & 7;
                i = 4;
                b = UByte.MIN_VALUE;
            } else {
                $this$commonReadUtf8CodePoint.skip(1);
                return Utf8.REPLACEMENT_CODE_POINT;
            }
            if ($this$commonReadUtf8CodePoint.size() >= ((long) i)) {
                int i2 = 1;
                while (i2 < i) {
                    byte b4 = $this$commonReadUtf8CodePoint.getByte((long) i2);
                    if ((192 & b4) == 128) {
                        b2 = (b2 << 6) | (63 & b4);
                        i2++;
                    } else {
                        $this$commonReadUtf8CodePoint.skip((long) i2);
                        return Utf8.REPLACEMENT_CODE_POINT;
                    }
                }
                $this$commonReadUtf8CodePoint.skip((long) i);
                if (b2 > 1114111) {
                    return Utf8.REPLACEMENT_CODE_POINT;
                }
                return ((55296 <= b2 && 57343 >= b2) || b2 < b) ? Utf8.REPLACEMENT_CODE_POINT : b2;
            }
            StringBuilder append = new StringBuilder().append("size < ").append(i).append(": ").append($this$commonReadUtf8CodePoint.size()).append(" (to read code point prefixed 0x");
            String hexString = Util.toHexString(b3);
            Log1F380D.a((Object) hexString);
            throw new EOFException(append.append(hexString).append(')').toString());
        }
        throw new EOFException();
    }

    public static final String commonReadUtf8Line(Buffer $this$commonReadUtf8Line) {
        Intrinsics.checkNotNullParameter($this$commonReadUtf8Line, "$this$commonReadUtf8Line");
        long indexOf = $this$commonReadUtf8Line.indexOf((byte) 10);
        if (indexOf != -1) {
            String readUtf8Line = readUtf8Line($this$commonReadUtf8Line, indexOf);
            Log1F380D.a((Object) readUtf8Line);
            return readUtf8Line;
        } else if ($this$commonReadUtf8Line.size() != 0) {
            return $this$commonReadUtf8Line.readUtf8($this$commonReadUtf8Line.size());
        } else {
            return null;
        }
    }

    public static final String commonReadUtf8LineStrict(Buffer $this$commonReadUtf8LineStrict, long limit) {
        Buffer buffer = $this$commonReadUtf8LineStrict;
        long j = limit;
        Intrinsics.checkNotNullParameter(buffer, "$this$commonReadUtf8LineStrict");
        if (j >= 0) {
            long j2 = Long.MAX_VALUE;
            if (j != Long.MAX_VALUE) {
                j2 = j + 1;
            }
            long j3 = j2;
            byte b = (byte) 10;
            long indexOf = $this$commonReadUtf8LineStrict.indexOf(b, 0, j3);
            if (indexOf != -1) {
                String readUtf8Line = readUtf8Line(buffer, indexOf);
                Log1F380D.a((Object) readUtf8Line);
                return readUtf8Line;
            } else if (j3 < $this$commonReadUtf8LineStrict.size() && buffer.getByte(j3 - 1) == ((byte) 13) && buffer.getByte(j3) == b) {
                String readUtf8Line2 = readUtf8Line(buffer, j3);
                Log1F380D.a((Object) readUtf8Line2);
                return readUtf8Line2;
            } else {
                Buffer buffer2 = new Buffer();
                long j4 = indexOf;
                $this$commonReadUtf8LineStrict.copyTo(buffer2, 0, Math.min((long) 32, $this$commonReadUtf8LineStrict.size()));
                throw new EOFException("\\n not found: limit=" + Math.min($this$commonReadUtf8LineStrict.size(), j) + " content=" + buffer2.readByteString().hex() + Typography.ellipsis);
            }
        } else {
            throw new IllegalArgumentException(("limit < 0: " + j).toString());
        }
    }

    public static final Buffer commonWriteUtf8CodePoint(Buffer $this$commonWriteUtf8CodePoint, int codePoint) {
        Intrinsics.checkNotNullParameter($this$commonWriteUtf8CodePoint, "$this$commonWriteUtf8CodePoint");
        if (codePoint < 128) {
            $this$commonWriteUtf8CodePoint.writeByte(codePoint);
        } else if (codePoint < 2048) {
            Segment writableSegment$okio = $this$commonWriteUtf8CodePoint.writableSegment$okio(2);
            writableSegment$okio.data[writableSegment$okio.limit] = (byte) ((codePoint >> 6) | 192);
            writableSegment$okio.data[writableSegment$okio.limit + 1] = (byte) (128 | (codePoint & 63));
            writableSegment$okio.limit += 2;
            $this$commonWriteUtf8CodePoint.setSize$okio($this$commonWriteUtf8CodePoint.size() + 2);
        } else if (55296 <= codePoint && 57343 >= codePoint) {
            $this$commonWriteUtf8CodePoint.writeByte(63);
        } else if (codePoint < 65536) {
            Segment writableSegment$okio2 = $this$commonWriteUtf8CodePoint.writableSegment$okio(3);
            writableSegment$okio2.data[writableSegment$okio2.limit] = (byte) ((codePoint >> 12) | 224);
            writableSegment$okio2.data[writableSegment$okio2.limit + 1] = (byte) ((63 & (codePoint >> 6)) | 128);
            writableSegment$okio2.data[writableSegment$okio2.limit + 2] = (byte) (128 | (codePoint & 63));
            writableSegment$okio2.limit += 3;
            $this$commonWriteUtf8CodePoint.setSize$okio($this$commonWriteUtf8CodePoint.size() + 3);
        } else if (codePoint <= 1114111) {
            Segment writableSegment$okio3 = $this$commonWriteUtf8CodePoint.writableSegment$okio(4);
            writableSegment$okio3.data[writableSegment$okio3.limit] = (byte) ((codePoint >> 18) | 240);
            writableSegment$okio3.data[writableSegment$okio3.limit + 1] = (byte) (((codePoint >> 12) & 63) | 128);
            writableSegment$okio3.data[writableSegment$okio3.limit + 2] = (byte) (((codePoint >> 6) & 63) | 128);
            writableSegment$okio3.data[writableSegment$okio3.limit + 3] = (byte) (128 | (codePoint & 63));
            writableSegment$okio3.limit += 4;
            $this$commonWriteUtf8CodePoint.setSize$okio($this$commonWriteUtf8CodePoint.size() + 4);
        } else {
            StringBuilder append = new StringBuilder().append("Unexpected code point: 0x");
            String hexString = Util.toHexString(codePoint);
            Log1F380D.a((Object) hexString);
            throw new IllegalArgumentException(append.append(hexString).toString());
        }
        return $this$commonWriteUtf8CodePoint;
    }
}

package okio.internal;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import okio.Buffer;
import okio.ByteString;
import okio.Segment;
import okio.SegmentedByteString;
import okio.Util;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000R\n\u0000\n\u0002\u0010\b\n\u0002\u0010\u0015\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a$\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0000\u001a\u0017\u0010\u0006\u001a\u00020\u0007*\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\b\u001a\r\u0010\u000b\u001a\u00020\u0001*\u00020\bH\b\u001a\r\u0010\f\u001a\u00020\u0001*\u00020\bH\b\u001a\u0015\u0010\r\u001a\u00020\u000e*\u00020\b2\u0006\u0010\u000f\u001a\u00020\u0001H\b\u001a-\u0010\u0010\u001a\u00020\u0007*\u00020\b2\u0006\u0010\u0011\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u0001H\b\u001a-\u0010\u0010\u001a\u00020\u0007*\u00020\b2\u0006\u0010\u0011\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u00152\u0006\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u0001H\b\u001a\u001d\u0010\u0016\u001a\u00020\u0015*\u00020\b2\u0006\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\u0001H\b\u001a\r\u0010\u0019\u001a\u00020\u0012*\u00020\bH\b\u001a%\u0010\u001a\u001a\u00020\u001b*\u00020\b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u0001H\b\u001a]\u0010\u001e\u001a\u00020\u001b*\u00020\b2K\u0010\u001f\u001aG\u0012\u0013\u0012\u00110\u0012¢\u0006\f\b!\u0012\b\b\"\u0012\u0004\b\b(#\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b!\u0012\b\b\"\u0012\u0004\b\b(\u0011\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b!\u0012\b\b\"\u0012\u0004\b\b(\u0014\u0012\u0004\u0012\u00020\u001b0 H\bø\u0001\u0000\u001aj\u0010\u001e\u001a\u00020\u001b*\u00020\b2\u0006\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\u00012K\u0010\u001f\u001aG\u0012\u0013\u0012\u00110\u0012¢\u0006\f\b!\u0012\b\b\"\u0012\u0004\b\b(#\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b!\u0012\b\b\"\u0012\u0004\b\b(\u0011\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b!\u0012\b\b\"\u0012\u0004\b\b(\u0014\u0012\u0004\u0012\u00020\u001b0 H\b\u001a\u0014\u0010$\u001a\u00020\u0001*\u00020\b2\u0006\u0010\u000f\u001a\u00020\u0001H\u0000\u0002\u0007\n\u0005\b20\u0001¨\u0006%"}, d2 = {"binarySearch", "", "", "value", "fromIndex", "toIndex", "commonEquals", "", "Lokio/SegmentedByteString;", "other", "", "commonGetSize", "commonHashCode", "commonInternalGet", "", "pos", "commonRangeEquals", "offset", "", "otherOffset", "byteCount", "Lokio/ByteString;", "commonSubstring", "beginIndex", "endIndex", "commonToByteArray", "commonWrite", "", "buffer", "Lokio/Buffer;", "forEachSegment", "action", "Lkotlin/Function3;", "Lkotlin/ParameterName;", "name", "data", "segment", "okio"}, k = 2, mv = {1, 4, 0})
/* compiled from: SegmentedByteString.kt */
public final class SegmentedByteStringKt {
    public static final int binarySearch(int[] $this$binarySearch, int value, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter($this$binarySearch, "$this$binarySearch");
        int i = fromIndex;
        int i2 = toIndex - 1;
        while (i <= i2) {
            int i3 = (i + i2) >>> 1;
            int i4 = $this$binarySearch[i3];
            if (i4 < value) {
                i = i3 + 1;
            } else if (i4 <= value) {
                return i3;
            } else {
                i2 = i3 - 1;
            }
        }
        return (-i) - 1;
    }

    public static final boolean commonEquals(SegmentedByteString $this$commonEquals, Object other) {
        Intrinsics.checkNotNullParameter($this$commonEquals, "$this$commonEquals");
        if (other == $this$commonEquals) {
            return true;
        }
        if (other instanceof ByteString) {
            return ((ByteString) other).size() == $this$commonEquals.size() && $this$commonEquals.rangeEquals(0, (ByteString) other, 0, $this$commonEquals.size());
        }
        return false;
    }

    public static final int commonGetSize(SegmentedByteString $this$commonGetSize) {
        Intrinsics.checkNotNullParameter($this$commonGetSize, "$this$commonGetSize");
        return $this$commonGetSize.getDirectory$okio()[((Object[]) $this$commonGetSize.getSegments$okio()).length - 1];
    }

    public static final int commonHashCode(SegmentedByteString $this$commonHashCode) {
        SegmentedByteString segmentedByteString = $this$commonHashCode;
        Intrinsics.checkNotNullParameter(segmentedByteString, "$this$commonHashCode");
        int hashCode$okio = $this$commonHashCode.getHashCode$okio();
        if (hashCode$okio != 0) {
            return hashCode$okio;
        }
        int i = 1;
        SegmentedByteString segmentedByteString2 = $this$commonHashCode;
        int length = ((Object[]) segmentedByteString2.getSegments$okio()).length;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            int i4 = segmentedByteString2.getDirectory$okio()[length + i3];
            int i5 = segmentedByteString2.getDirectory$okio()[i3];
            byte[] bArr = segmentedByteString2.getSegments$okio()[i3];
            int i6 = i4;
            int i7 = i6 + (i5 - i2);
            for (int i8 = i6; i8 < i7; i8++) {
                i = (i * 31) + bArr[i8];
            }
            i2 = i5;
        }
        segmentedByteString.setHashCode$okio(i);
        return i;
    }

    public static final byte commonInternalGet(SegmentedByteString $this$commonInternalGet, int pos) {
        Intrinsics.checkNotNullParameter($this$commonInternalGet, "$this$commonInternalGet");
        Util.checkOffsetAndCount((long) $this$commonInternalGet.getDirectory$okio()[((Object[]) $this$commonInternalGet.getSegments$okio()).length - 1], (long) pos, 1);
        int segment = segment($this$commonInternalGet, pos);
        return $this$commonInternalGet.getSegments$okio()[segment][(pos - (segment == 0 ? 0 : $this$commonInternalGet.getDirectory$okio()[segment - 1])) + $this$commonInternalGet.getDirectory$okio()[((Object[]) $this$commonInternalGet.getSegments$okio()).length + segment]];
    }

    public static final boolean commonRangeEquals(SegmentedByteString $this$commonRangeEquals, int offset, ByteString other, int otherOffset, int byteCount) {
        int i = offset;
        ByteString byteString = other;
        boolean z = false;
        Intrinsics.checkNotNullParameter($this$commonRangeEquals, "$this$commonRangeEquals");
        Intrinsics.checkNotNullParameter(byteString, "other");
        if (i < 0) {
            return false;
        }
        if (i > $this$commonRangeEquals.size() - byteCount) {
            return false;
        }
        int i2 = otherOffset;
        int i3 = i + byteCount;
        SegmentedByteString segmentedByteString = $this$commonRangeEquals;
        int segment = segment(segmentedByteString, i);
        int i4 = offset;
        while (i4 < i3) {
            int i5 = segment == 0 ? 0 : segmentedByteString.getDirectory$okio()[segment - 1];
            int i6 = segmentedByteString.getDirectory$okio()[((Object[]) segmentedByteString.getSegments$okio()).length + segment];
            int min = Math.min(i3, i5 + (segmentedByteString.getDirectory$okio()[segment] - i5)) - i4;
            int i7 = min;
            boolean z2 = z;
            if (!byteString.rangeEquals(i2, segmentedByteString.getSegments$okio()[segment], (i4 - i5) + i6, i7)) {
                return false;
            }
            i2 += i7;
            i4 += min;
            segment++;
            int i8 = offset;
            z = z2;
        }
        return true;
    }

    public static final boolean commonRangeEquals(SegmentedByteString $this$commonRangeEquals, int offset, byte[] other, int otherOffset, int byteCount) {
        int i = offset;
        byte[] bArr = other;
        int i2 = otherOffset;
        Intrinsics.checkNotNullParameter($this$commonRangeEquals, "$this$commonRangeEquals");
        Intrinsics.checkNotNullParameter(bArr, "other");
        if (i < 0 || i > $this$commonRangeEquals.size() - byteCount || i2 < 0 || i2 > bArr.length - byteCount) {
            return false;
        }
        int i3 = otherOffset;
        int i4 = i + byteCount;
        SegmentedByteString segmentedByteString = $this$commonRangeEquals;
        int segment = segment(segmentedByteString, i);
        int i5 = offset;
        while (i5 < i4) {
            int i6 = segment == 0 ? 0 : segmentedByteString.getDirectory$okio()[segment - 1];
            int i7 = segmentedByteString.getDirectory$okio()[((Object[]) segmentedByteString.getSegments$okio()).length + segment];
            int min = Math.min(i4, i6 + (segmentedByteString.getDirectory$okio()[segment] - i6)) - i5;
            int i8 = min;
            if (!Util.arrayRangeEquals(segmentedByteString.getSegments$okio()[segment], i7 + (i5 - i6), bArr, i3, i8)) {
                return false;
            }
            i3 += i8;
            i5 += min;
            segment++;
            int i9 = offset;
            int i10 = otherOffset;
        }
        return true;
    }

    public static final ByteString commonSubstring(SegmentedByteString $this$commonSubstring, int beginIndex, int endIndex) {
        int i;
        Intrinsics.checkNotNullParameter($this$commonSubstring, "$this$commonSubstring");
        int i2 = 0;
        boolean z = true;
        if (beginIndex >= 0) {
            if (endIndex <= $this$commonSubstring.size()) {
                int i3 = endIndex - beginIndex;
                if (i3 < 0) {
                    z = false;
                }
                if (!z) {
                    throw new IllegalArgumentException(("endIndex=" + endIndex + " < beginIndex=" + beginIndex).toString());
                } else if (beginIndex == 0 && endIndex == $this$commonSubstring.size()) {
                    return $this$commonSubstring;
                } else {
                    if (beginIndex == endIndex) {
                        return ByteString.EMPTY;
                    }
                    int segment = segment($this$commonSubstring, beginIndex);
                    int segment2 = segment($this$commonSubstring, endIndex - 1);
                    byte[][] bArr = (byte[][]) ArraysKt.copyOfRange((T[]) (Object[]) $this$commonSubstring.getSegments$okio(), segment, segment2 + 1);
                    int[] iArr = new int[(((Object[]) bArr).length * 2)];
                    int i4 = 0;
                    if (segment <= segment2) {
                        int i5 = segment;
                        while (true) {
                            iArr[i4] = Math.min($this$commonSubstring.getDirectory$okio()[i5] - beginIndex, i3);
                            i = i4 + 1;
                            iArr[i4 + ((Object[]) bArr).length] = $this$commonSubstring.getDirectory$okio()[((Object[]) $this$commonSubstring.getSegments$okio()).length + i5];
                            if (i5 == segment2) {
                                break;
                            }
                            i5++;
                            i4 = i;
                        }
                        int i6 = i;
                    }
                    if (segment != 0) {
                        i2 = $this$commonSubstring.getDirectory$okio()[segment - 1];
                    }
                    int length = ((Object[]) bArr).length;
                    iArr[length] = iArr[length] + (beginIndex - i2);
                    return new SegmentedByteString(bArr, iArr);
                }
            } else {
                throw new IllegalArgumentException(("endIndex=" + endIndex + " > length(" + $this$commonSubstring.size() + ')').toString());
            }
        } else {
            throw new IllegalArgumentException(("beginIndex=" + beginIndex + " < 0").toString());
        }
    }

    public static final byte[] commonToByteArray(SegmentedByteString $this$commonToByteArray) {
        Intrinsics.checkNotNullParameter($this$commonToByteArray, "$this$commonToByteArray");
        byte[] bArr = new byte[$this$commonToByteArray.size()];
        int i = 0;
        SegmentedByteString segmentedByteString = $this$commonToByteArray;
        int length = ((Object[]) segmentedByteString.getSegments$okio()).length;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            int i4 = segmentedByteString.getDirectory$okio()[length + i3];
            int i5 = segmentedByteString.getDirectory$okio()[i3];
            int i6 = i5 - i2;
            int i7 = i4;
            ArraysKt.copyInto(segmentedByteString.getSegments$okio()[i3], bArr, i, i7, i7 + i6);
            i += i6;
            i2 = i5;
        }
        return bArr;
    }

    public static final void commonWrite(SegmentedByteString $this$commonWrite, Buffer buffer, int offset, int byteCount) {
        Buffer buffer2 = buffer;
        int i = offset;
        Intrinsics.checkNotNullParameter($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(buffer2, "buffer");
        int i2 = i + byteCount;
        SegmentedByteString segmentedByteString = $this$commonWrite;
        int segment = segment(segmentedByteString, i);
        int i3 = offset;
        while (i3 < i2) {
            int i4 = segment == 0 ? 0 : segmentedByteString.getDirectory$okio()[segment - 1];
            int i5 = segmentedByteString.getDirectory$okio()[((Object[]) segmentedByteString.getSegments$okio()).length + segment];
            int min = Math.min(i2, i4 + (segmentedByteString.getDirectory$okio()[segment] - i4)) - i3;
            int i6 = (i3 - i4) + i5;
            Segment segment2 = new Segment(segmentedByteString.getSegments$okio()[segment], i6, i6 + min, true, false);
            if (buffer2.head == null) {
                segment2.prev = segment2;
                segment2.next = segment2.prev;
                buffer2.head = segment2.next;
            } else {
                Segment segment3 = buffer2.head;
                Intrinsics.checkNotNull(segment3);
                Segment segment4 = segment3.prev;
                Intrinsics.checkNotNull(segment4);
                segment4.push(segment2);
            }
            i3 += min;
            segment++;
            int i7 = offset;
        }
        buffer2.setSize$okio(buffer.size() + ((long) $this$commonWrite.size()));
    }

    /* access modifiers changed from: private */
    public static final void forEachSegment(SegmentedByteString $this$forEachSegment, int beginIndex, int endIndex, Function3<? super byte[], ? super Integer, ? super Integer, Unit> action) {
        int segment = segment($this$forEachSegment, beginIndex);
        int i = beginIndex;
        while (i < endIndex) {
            int i2 = segment == 0 ? 0 : $this$forEachSegment.getDirectory$okio()[segment - 1];
            int i3 = $this$forEachSegment.getDirectory$okio()[((Object[]) $this$forEachSegment.getSegments$okio()).length + segment];
            int min = Math.min(endIndex, i2 + ($this$forEachSegment.getDirectory$okio()[segment] - i2)) - i;
            action.invoke($this$forEachSegment.getSegments$okio()[segment], Integer.valueOf((i - i2) + i3), Integer.valueOf(min));
            i += min;
            segment++;
        }
    }

    public static final void forEachSegment(SegmentedByteString $this$forEachSegment, Function3<? super byte[], ? super Integer, ? super Integer, Unit> action) {
        Intrinsics.checkNotNullParameter($this$forEachSegment, "$this$forEachSegment");
        Intrinsics.checkNotNullParameter(action, "action");
        int length = ((Object[]) $this$forEachSegment.getSegments$okio()).length;
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = $this$forEachSegment.getDirectory$okio()[length + i2];
            int i4 = $this$forEachSegment.getDirectory$okio()[i2];
            action.invoke($this$forEachSegment.getSegments$okio()[i2], Integer.valueOf(i3), Integer.valueOf(i4 - i));
            i = i4;
        }
    }

    public static final int segment(SegmentedByteString $this$segment, int pos) {
        Intrinsics.checkNotNullParameter($this$segment, "$this$segment");
        int binarySearch = binarySearch($this$segment.getDirectory$okio(), pos + 1, 0, ((Object[]) $this$segment.getSegments$okio()).length);
        return binarySearch >= 0 ? binarySearch : ~binarySearch;
    }
}

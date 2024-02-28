package okio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import okio.internal.SegmentedByteStringKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u0005\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u001d\b\u0000\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0011\u001a\u00020\u0010H\u0016J\u0015\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u0010H\u0010¢\u0006\u0002\b\u0014J\u0013\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0002J\r\u0010\u0019\u001a\u00020\u001aH\u0010¢\u0006\u0002\b\u001bJ\b\u0010\u001c\u001a\u00020\u001aH\u0016J\b\u0010\u001d\u001a\u00020\u0010H\u0016J\u001d\u0010\u001e\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u001f\u001a\u00020\u0001H\u0010¢\u0006\u0002\b J\u0018\u0010!\u001a\u00020\u001a2\u0006\u0010\u0017\u001a\u00020\u00042\u0006\u0010\"\u001a\u00020\u001aH\u0016J\r\u0010#\u001a\u00020\u0004H\u0010¢\u0006\u0002\b$J\u0015\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\u001aH\u0010¢\u0006\u0002\b(J\u0018\u0010)\u001a\u00020\u001a2\u0006\u0010\u0017\u001a\u00020\u00042\u0006\u0010\"\u001a\u00020\u001aH\u0016J(\u0010*\u001a\u00020\u00162\u0006\u0010+\u001a\u00020\u001a2\u0006\u0010\u0017\u001a\u00020\u00042\u0006\u0010,\u001a\u00020\u001a2\u0006\u0010-\u001a\u00020\u001aH\u0016J(\u0010*\u001a\u00020\u00162\u0006\u0010+\u001a\u00020\u001a2\u0006\u0010\u0017\u001a\u00020\u00012\u0006\u0010,\u001a\u00020\u001a2\u0006\u0010-\u001a\u00020\u001aH\u0016J\u0010\u0010.\u001a\u00020\u00102\u0006\u0010/\u001a\u000200H\u0016J\u0018\u00101\u001a\u00020\u00012\u0006\u00102\u001a\u00020\u001a2\u0006\u00103\u001a\u00020\u001aH\u0016J\b\u00104\u001a\u00020\u0001H\u0016J\b\u00105\u001a\u00020\u0001H\u0016J\b\u00106\u001a\u00020\u0004H\u0016J\b\u00107\u001a\u00020\u0001H\u0002J\b\u00108\u001a\u00020\u0010H\u0016J\u0010\u00109\u001a\u00020:2\u0006\u0010;\u001a\u00020<H\u0016J%\u00109\u001a\u00020:2\u0006\u0010=\u001a\u00020>2\u0006\u0010+\u001a\u00020\u001a2\u0006\u0010-\u001a\u00020\u001aH\u0010¢\u0006\u0002\b?J\b\u0010@\u001a\u00020AH\u0002R\u0014\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001c\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0004¢\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000b¨\u0006B"}, d2 = {"Lokio/SegmentedByteString;", "Lokio/ByteString;", "segments", "", "", "directory", "", "([[B[I)V", "getDirectory$okio", "()[I", "getSegments$okio", "()[[B", "[[B", "asByteBuffer", "Ljava/nio/ByteBuffer;", "base64", "", "base64Url", "digest", "algorithm", "digest$okio", "equals", "", "other", "", "getSize", "", "getSize$okio", "hashCode", "hex", "hmac", "key", "hmac$okio", "indexOf", "fromIndex", "internalArray", "internalArray$okio", "internalGet", "", "pos", "internalGet$okio", "lastIndexOf", "rangeEquals", "offset", "otherOffset", "byteCount", "string", "charset", "Ljava/nio/charset/Charset;", "substring", "beginIndex", "endIndex", "toAsciiLowercase", "toAsciiUppercase", "toByteArray", "toByteString", "toString", "write", "", "out", "Ljava/io/OutputStream;", "buffer", "Lokio/Buffer;", "write$okio", "writeReplace", "Ljava/lang/Object;", "okio"}, k = 1, mv = {1, 4, 0})
/* compiled from: SegmentedByteString.kt */
public final class SegmentedByteString extends ByteString {
    private final transient int[] directory;
    private final transient byte[][] segments;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public SegmentedByteString(byte[][] segments2, int[] directory2) {
        super(ByteString.EMPTY.getData$okio());
        Intrinsics.checkNotNullParameter(segments2, "segments");
        Intrinsics.checkNotNullParameter(directory2, "directory");
        this.segments = segments2;
        this.directory = directory2;
    }

    private final ByteString toByteString() {
        return new ByteString(toByteArray());
    }

    private final Object writeReplace() {
        ByteString byteString = toByteString();
        if (byteString != null) {
            return byteString;
        }
        throw new NullPointerException("null cannot be cast to non-null type java.lang.Object");
    }

    public ByteBuffer asByteBuffer() {
        ByteBuffer asReadOnlyBuffer = ByteBuffer.wrap(toByteArray()).asReadOnlyBuffer();
        Intrinsics.checkNotNullExpressionValue(asReadOnlyBuffer, "ByteBuffer.wrap(toByteArray()).asReadOnlyBuffer()");
        return asReadOnlyBuffer;
    }

    public String base64() {
        return toByteString().base64();
    }

    public String base64Url() {
        return toByteString().base64Url();
    }

    public ByteString digest$okio(String algorithm) {
        Intrinsics.checkNotNullParameter(algorithm, "algorithm");
        MessageDigest instance = MessageDigest.getInstance(algorithm);
        int length = ((Object[]) getSegments$okio()).length;
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = getDirectory$okio()[length + i2];
            int i4 = getDirectory$okio()[i2];
            instance.update(getSegments$okio()[i2], i3, i4 - i);
            i = i4;
        }
        byte[] digest = instance.digest();
        Intrinsics.checkNotNullExpressionValue(digest, "digest.digest()");
        return new ByteString(digest);
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof ByteString) {
            return ((ByteString) other).size() == size() && rangeEquals(0, (ByteString) other, 0, size());
        }
        return false;
    }

    public final int[] getDirectory$okio() {
        return this.directory;
    }

    public final byte[][] getSegments$okio() {
        return this.segments;
    }

    public int getSize$okio() {
        return getDirectory$okio()[((Object[]) getSegments$okio()).length - 1];
    }

    public int hashCode() {
        int hashCode$okio = getHashCode$okio();
        if (hashCode$okio == 0) {
            hashCode$okio = 1;
            int length = ((Object[]) getSegments$okio()).length;
            int i = 0;
            for (int i2 = 0; i2 < length; i2++) {
                int i3 = getDirectory$okio()[length + i2];
                int i4 = getDirectory$okio()[i2];
                byte[] bArr = getSegments$okio()[i2];
                int i5 = i3;
                int i6 = i5 + (i4 - i);
                for (int i7 = i5; i7 < i6; i7++) {
                    hashCode$okio = (hashCode$okio * 31) + bArr[i7];
                }
                i = i4;
            }
            setHashCode$okio(hashCode$okio);
        }
        return hashCode$okio;
    }

    public String hex() {
        return toByteString().hex();
    }

    public ByteString hmac$okio(String algorithm, ByteString key) {
        Intrinsics.checkNotNullParameter(algorithm, "algorithm");
        Intrinsics.checkNotNullParameter(key, "key");
        try {
            Mac instance = Mac.getInstance(algorithm);
            instance.init(new SecretKeySpec(key.toByteArray(), algorithm));
            int length = ((Object[]) getSegments$okio()).length;
            int i = 0;
            for (int i2 = 0; i2 < length; i2++) {
                int i3 = getDirectory$okio()[length + i2];
                int i4 = getDirectory$okio()[i2];
                instance.update(getSegments$okio()[i2], i3, i4 - i);
                i = i4;
            }
            byte[] doFinal = instance.doFinal();
            Intrinsics.checkNotNullExpressionValue(doFinal, "mac.doFinal()");
            return new ByteString(doFinal);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public int indexOf(byte[] other, int fromIndex) {
        Intrinsics.checkNotNullParameter(other, "other");
        return toByteString().indexOf(other, fromIndex);
    }

    public byte[] internalArray$okio() {
        return toByteArray();
    }

    public byte internalGet$okio(int pos) {
        Util.checkOffsetAndCount((long) getDirectory$okio()[((Object[]) getSegments$okio()).length - 1], (long) pos, 1);
        int segment = SegmentedByteStringKt.segment(this, pos);
        return getSegments$okio()[segment][(pos - (segment == 0 ? 0 : getDirectory$okio()[segment - 1])) + getDirectory$okio()[((Object[]) getSegments$okio()).length + segment]];
    }

    public int lastIndexOf(byte[] other, int fromIndex) {
        Intrinsics.checkNotNullParameter(other, "other");
        return toByteString().lastIndexOf(other, fromIndex);
    }

    public boolean rangeEquals(int offset, ByteString other, int otherOffset, int byteCount) {
        int i = offset;
        ByteString byteString = other;
        Intrinsics.checkNotNullParameter(byteString, "other");
        SegmentedByteString segmentedByteString = this;
        if (i < 0) {
            SegmentedByteString segmentedByteString2 = segmentedByteString;
        } else if (i > segmentedByteString.size() - byteCount) {
            SegmentedByteString segmentedByteString3 = segmentedByteString;
        } else {
            int i2 = otherOffset;
            int i3 = i + byteCount;
            SegmentedByteString segmentedByteString4 = segmentedByteString;
            int segment = SegmentedByteStringKt.segment(segmentedByteString4, i);
            int i4 = offset;
            while (i4 < i3) {
                int i5 = segment == 0 ? 0 : segmentedByteString4.getDirectory$okio()[segment - 1];
                int i6 = segmentedByteString4.getDirectory$okio()[((Object[]) segmentedByteString4.getSegments$okio()).length + segment];
                int min = Math.min(i3, i5 + (segmentedByteString4.getDirectory$okio()[segment] - i5)) - i4;
                int i7 = min;
                SegmentedByteString segmentedByteString5 = segmentedByteString;
                if (!byteString.rangeEquals(i2, segmentedByteString4.getSegments$okio()[segment], (i4 - i5) + i6, i7)) {
                    return false;
                }
                i2 += i7;
                i4 += min;
                segment++;
                int i8 = offset;
                segmentedByteString = segmentedByteString5;
            }
            SegmentedByteString segmentedByteString6 = segmentedByteString;
            return true;
        }
        return false;
    }

    public boolean rangeEquals(int offset, byte[] other, int otherOffset, int byteCount) {
        int i = offset;
        byte[] bArr = other;
        int i2 = otherOffset;
        Intrinsics.checkNotNullParameter(bArr, "other");
        if (i < 0 || i > size() - byteCount || i2 < 0 || i2 > bArr.length - byteCount) {
            return false;
        }
        int i3 = otherOffset;
        int i4 = i + byteCount;
        int segment = SegmentedByteStringKt.segment(this, i);
        int i5 = offset;
        while (i5 < i4) {
            int i6 = segment == 0 ? 0 : getDirectory$okio()[segment - 1];
            int i7 = getDirectory$okio()[((Object[]) getSegments$okio()).length + segment];
            int min = Math.min(i4, i6 + (getDirectory$okio()[segment] - i6)) - i5;
            int i8 = min;
            if (!Util.arrayRangeEquals(getSegments$okio()[segment], i7 + (i5 - i6), bArr, i3, i8)) {
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

    public String string(Charset charset) {
        Intrinsics.checkNotNullParameter(charset, "charset");
        return toByteString().string(charset);
    }

    public ByteString substring(int beginIndex, int endIndex) {
        int i;
        int i2 = 0;
        boolean z = true;
        if (beginIndex >= 0) {
            if (endIndex <= size()) {
                int i3 = endIndex - beginIndex;
                if (i3 < 0) {
                    z = false;
                }
                if (!z) {
                    throw new IllegalArgumentException(("endIndex=" + endIndex + " < beginIndex=" + beginIndex).toString());
                } else if (beginIndex == 0 && endIndex == size()) {
                    return this;
                } else {
                    if (beginIndex == endIndex) {
                        return ByteString.EMPTY;
                    }
                    int segment = SegmentedByteStringKt.segment(this, beginIndex);
                    int segment2 = SegmentedByteStringKt.segment(this, endIndex - 1);
                    byte[][] bArr = (byte[][]) ArraysKt.copyOfRange((T[]) (Object[]) getSegments$okio(), segment, segment2 + 1);
                    int[] iArr = new int[(((Object[]) bArr).length * 2)];
                    int i4 = 0;
                    if (segment <= segment2) {
                        int i5 = segment;
                        while (true) {
                            iArr[i4] = Math.min(getDirectory$okio()[i5] - beginIndex, i3);
                            i = i4 + 1;
                            iArr[i4 + ((Object[]) bArr).length] = getDirectory$okio()[((Object[]) getSegments$okio()).length + i5];
                            if (i5 == segment2) {
                                break;
                            }
                            i5++;
                            i4 = i;
                        }
                        int i6 = i;
                    }
                    if (segment != 0) {
                        i2 = getDirectory$okio()[segment - 1];
                    }
                    int length = ((Object[]) bArr).length;
                    iArr[length] = iArr[length] + (beginIndex - i2);
                    return new SegmentedByteString(bArr, iArr);
                }
            } else {
                throw new IllegalArgumentException(("endIndex=" + endIndex + " > length(" + size() + ')').toString());
            }
        } else {
            throw new IllegalArgumentException(("beginIndex=" + beginIndex + " < 0").toString());
        }
    }

    public ByteString toAsciiLowercase() {
        return toByteString().toAsciiLowercase();
    }

    public ByteString toAsciiUppercase() {
        return toByteString().toAsciiUppercase();
    }

    public byte[] toByteArray() {
        byte[] bArr = new byte[size()];
        int i = 0;
        int length = ((Object[]) getSegments$okio()).length;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            int i4 = getDirectory$okio()[length + i3];
            int i5 = getDirectory$okio()[i3];
            int i6 = i5 - i2;
            int i7 = i4;
            ArraysKt.copyInto(getSegments$okio()[i3], bArr, i, i7, i7 + i6);
            i += i6;
            i2 = i5;
        }
        return bArr;
    }

    public String toString() {
        return toByteString().toString();
    }

    public void write(OutputStream out) throws IOException {
        Intrinsics.checkNotNullParameter(out, "out");
        int length = ((Object[]) getSegments$okio()).length;
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = getDirectory$okio()[length + i2];
            int i4 = getDirectory$okio()[i2];
            out.write(getSegments$okio()[i2], i3, i4 - i);
            i = i4;
        }
    }

    public void write$okio(Buffer buffer, int offset, int byteCount) {
        Buffer buffer2 = buffer;
        int i = offset;
        Intrinsics.checkNotNullParameter(buffer2, "buffer");
        int i2 = i + byteCount;
        int segment = SegmentedByteStringKt.segment(this, i);
        int i3 = offset;
        while (i3 < i2) {
            int i4 = segment == 0 ? 0 : getDirectory$okio()[segment - 1];
            int i5 = getDirectory$okio()[((Object[]) getSegments$okio()).length + segment];
            int min = Math.min(i2, i4 + (getDirectory$okio()[segment] - i4)) - i3;
            int i6 = (i3 - i4) + i5;
            Segment segment2 = new Segment(getSegments$okio()[segment], i6, i6 + min, true, false);
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
        buffer2.setSize$okio(buffer.size() + ((long) size()));
    }
}

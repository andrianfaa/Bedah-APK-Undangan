package kotlin;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import mt.Log1F380D;

@Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0001ø\u0001\u0000¢\u0006\u0002\u0010\u0004\u001a\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u0003H\u0001ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\tH\u0001\u001a\"\u0010\f\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u0001H\u0001ø\u0001\u0000¢\u0006\u0004\b\r\u0010\u000e\u001a\"\u0010\u000f\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u0001H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0010\u0010\u000e\u001a\u0010\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\tH\u0001\u001a\u0018\u0010\u0012\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00132\u0006\u0010\u000b\u001a\u00020\u0013H\u0001\u001a\"\u0010\u0014\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016\u001a\"\u0010\u0017\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0018\u0010\u0016\u001a\u0010\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0013H\u0001\u001a\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0002\u001a\u00020\u0013H\u0000\u001a\u0018\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0002\u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\tH\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006\u001d"}, d2 = {"doubleToUInt", "Lkotlin/UInt;", "v", "", "(D)I", "doubleToULong", "Lkotlin/ULong;", "(D)J", "uintCompare", "", "v1", "v2", "uintDivide", "uintDivide-J1ME1BU", "(II)I", "uintRemainder", "uintRemainder-J1ME1BU", "uintToDouble", "ulongCompare", "", "ulongDivide", "ulongDivide-eb3DHEI", "(JJ)J", "ulongRemainder", "ulongRemainder-eb3DHEI", "ulongToDouble", "ulongToString", "", "base", "kotlin-stdlib"}, k = 2, mv = {1, 7, 1}, xi = 48)
/* compiled from: 011F */
public final class UnsignedKt {
    public static final int doubleToUInt(double v) {
        if (Double.isNaN(v)) {
            return 0;
        }
        if (v <= uintToDouble(0)) {
            return 0;
        }
        if (v >= uintToDouble(-1)) {
            return -1;
        }
        return v <= 2.147483647E9d ? UInt.m130constructorimpl((int) v) : UInt.m130constructorimpl(UInt.m130constructorimpl((int) (v - ((double) Integer.MAX_VALUE))) + UInt.m130constructorimpl(Integer.MAX_VALUE));
    }

    public static final long doubleToULong(double v) {
        if (Double.isNaN(v)) {
            return 0;
        }
        if (v <= ulongToDouble(0)) {
            return 0;
        }
        if (v >= ulongToDouble(-1)) {
            return -1;
        }
        return v < 9.223372036854776E18d ? ULong.m208constructorimpl((long) v) : ULong.m208constructorimpl(ULong.m208constructorimpl((long) (v - 9.223372036854776E18d)) - Long.MIN_VALUE);
    }

    public static final int uintCompare(int v1, int v2) {
        return Intrinsics.compare(v1 ^ Integer.MIN_VALUE, Integer.MIN_VALUE ^ v2);
    }

    /* renamed from: uintDivide-J1ME1BU  reason: not valid java name */
    public static final int m383uintDivideJ1ME1BU(int v1, int v2) {
        return UInt.m130constructorimpl((int) ((((long) v1) & 4294967295L) / (4294967295L & ((long) v2))));
    }

    /* renamed from: uintRemainder-J1ME1BU  reason: not valid java name */
    public static final int m384uintRemainderJ1ME1BU(int v1, int v2) {
        return UInt.m130constructorimpl((int) ((((long) v1) & 4294967295L) % (4294967295L & ((long) v2))));
    }

    public static final double uintToDouble(int v) {
        return ((double) (Integer.MAX_VALUE & v)) + (((double) ((v >>> 31) << 30)) * ((double) 2));
    }

    public static final int ulongCompare(long v1, long v2) {
        return Intrinsics.compare(v1 ^ Long.MIN_VALUE, Long.MIN_VALUE ^ v2);
    }

    /* renamed from: ulongDivide-eb3DHEI  reason: not valid java name */
    public static final long m385ulongDivideeb3DHEI(long v1, long v2) {
        long j = v1;
        long j2 = v2;
        long j3 = 0;
        if (j2 < 0) {
            if (ulongCompare(v1, v2) >= 0) {
                j3 = 1;
            }
            return ULong.m208constructorimpl(j3);
        } else if (j >= 0) {
            return ULong.m208constructorimpl(j / j2);
        } else {
            int i = 1;
            long j4 = ((j >>> 1) / j2) << 1;
            if (ulongCompare(ULong.m208constructorimpl(j - (j4 * j2)), ULong.m208constructorimpl(j2)) < 0) {
                i = 0;
            }
            return ULong.m208constructorimpl(((long) i) + j4);
        }
    }

    /* renamed from: ulongRemainder-eb3DHEI  reason: not valid java name */
    public static final long m386ulongRemaindereb3DHEI(long v1, long v2) {
        long j = v1;
        long j2 = v2;
        long j3 = 0;
        if (j2 < 0) {
            return ulongCompare(v1, v2) < 0 ? v1 : ULong.m208constructorimpl(v1 - v2);
        }
        if (j >= 0) {
            return ULong.m208constructorimpl(j % j2);
        }
        long j4 = j - ((((j >>> 1) / j2) << 1) * j2);
        if (ulongCompare(ULong.m208constructorimpl(j4), ULong.m208constructorimpl(j2)) >= 0) {
            j3 = j2;
        }
        return ULong.m208constructorimpl(j4 - j3);
    }

    public static final double ulongToDouble(long v) {
        return (((double) (v >>> 11)) * ((double) 2048)) + ((double) (2047 & v));
    }

    public static final String ulongToString(long v) {
        String ulongToString = ulongToString(v, 10);
        Log1F380D.a((Object) ulongToString);
        return ulongToString;
    }

    public static final String ulongToString(long v, int base) {
        if (v >= 0) {
            String l = Long.toString(v, CharsKt.checkRadix(base));
            Log1F380D.a((Object) l);
            Intrinsics.checkNotNullExpressionValue(l, "toString(this, checkRadix(radix))");
            return l;
        }
        long j = ((v >>> 1) / ((long) base)) << 1;
        long j2 = v - (((long) base) * j);
        if (j2 >= ((long) base)) {
            j2 -= (long) base;
            j++;
        }
        StringBuilder sb = new StringBuilder();
        String l2 = Long.toString(j, CharsKt.checkRadix(base));
        Log1F380D.a((Object) l2);
        Intrinsics.checkNotNullExpressionValue(l2, "toString(this, checkRadix(radix))");
        StringBuilder append = sb.append(l2);
        String l3 = Long.toString(j2, CharsKt.checkRadix(base));
        Log1F380D.a((Object) l3);
        Intrinsics.checkNotNullExpressionValue(l3, "toString(this, checkRadix(radix))");
        return append.append(l3).toString();
    }
}

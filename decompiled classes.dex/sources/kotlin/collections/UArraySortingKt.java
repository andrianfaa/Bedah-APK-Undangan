package kotlin.collections;

import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UIntArray;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.UnsignedKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0010\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0007\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\t\u0010\n\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\f\u0010\r\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0010\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0013\u0010\u0014\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003ø\u0001\u0000¢\u0006\u0004\b\u0019\u0010\u001a\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u0014\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001ø\u0001\u0000¢\u0006\u0004\b\u001f\u0010\u0016\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001ø\u0001\u0000¢\u0006\u0004\b \u0010\u0018\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001ø\u0001\u0000¢\u0006\u0004\b!\u0010\u001a\u0002\u0004\n\u0002\b\u0019¨\u0006\""}, d2 = {"partition", "", "array", "Lkotlin/UByteArray;", "left", "right", "partition-4UcCI2c", "([BII)I", "Lkotlin/UIntArray;", "partition-oBK06Vg", "([III)I", "Lkotlin/ULongArray;", "partition--nroSd4", "([JII)I", "Lkotlin/UShortArray;", "partition-Aa5vz7o", "([SII)I", "quickSort", "", "quickSort-4UcCI2c", "([BII)V", "quickSort-oBK06Vg", "([III)V", "quickSort--nroSd4", "([JII)V", "quickSort-Aa5vz7o", "([SII)V", "sortArray", "fromIndex", "toIndex", "sortArray-4UcCI2c", "sortArray-oBK06Vg", "sortArray--nroSd4", "sortArray-Aa5vz7o", "kotlin-stdlib"}, k = 2, mv = {1, 7, 1}, xi = 48)
/* compiled from: UArraySorting.kt */
public final class UArraySortingKt {
    /* renamed from: partition--nroSd4  reason: not valid java name */
    private static final int m483partitionnroSd4(long[] array, int left, int right) {
        int i = left;
        int i2 = right;
        long j = ULongArray.m267getsVKNKU(array, (left + right) / 2);
        while (i <= i2) {
            while (UnsignedKt.ulongCompare(ULongArray.m267getsVKNKU(array, i), j) < 0) {
                i++;
            }
            while (UnsignedKt.ulongCompare(ULongArray.m267getsVKNKU(array, i2), j) > 0) {
                i2--;
            }
            if (i <= i2) {
                long j2 = ULongArray.m267getsVKNKU(array, i);
                ULongArray.m272setk8EXiF4(array, i, ULongArray.m267getsVKNKU(array, i2));
                ULongArray.m272setk8EXiF4(array, i2, j2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* renamed from: partition-4UcCI2c  reason: not valid java name */
    private static final int m484partition4UcCI2c(byte[] array, int left, int right) {
        int i = left;
        int i2 = right;
        byte b = UByteArray.m111getw2LRezQ(array, (left + right) / 2);
        while (i <= i2) {
            while (Intrinsics.compare((int) UByteArray.m111getw2LRezQ(array, i) & UByte.MAX_VALUE, (int) b & UByte.MAX_VALUE) < 0) {
                i++;
            }
            while (Intrinsics.compare((int) UByteArray.m111getw2LRezQ(array, i2) & UByte.MAX_VALUE, (int) b & UByte.MAX_VALUE) > 0) {
                i2--;
            }
            if (i <= i2) {
                byte b2 = UByteArray.m111getw2LRezQ(array, i);
                UByteArray.m116setVurrAj0(array, i, UByteArray.m111getw2LRezQ(array, i2));
                UByteArray.m116setVurrAj0(array, i2, b2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* renamed from: partition-Aa5vz7o  reason: not valid java name */
    private static final int m485partitionAa5vz7o(short[] array, int left, int right) {
        int i = left;
        int i2 = right;
        short s = UShortArray.m371getMh2AYeg(array, (left + right) / 2);
        while (i <= i2) {
            while (Intrinsics.compare((int) UShortArray.m371getMh2AYeg(array, i) & UShort.MAX_VALUE, (int) s & UShort.MAX_VALUE) < 0) {
                i++;
            }
            while (Intrinsics.compare((int) UShortArray.m371getMh2AYeg(array, i2) & UShort.MAX_VALUE, (int) s & UShort.MAX_VALUE) > 0) {
                i2--;
            }
            if (i <= i2) {
                short s2 = UShortArray.m371getMh2AYeg(array, i);
                UShortArray.m376set01HTLdE(array, i, UShortArray.m371getMh2AYeg(array, i2));
                UShortArray.m376set01HTLdE(array, i2, s2);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* renamed from: partition-oBK06Vg  reason: not valid java name */
    private static final int m486partitionoBK06Vg(int[] array, int left, int right) {
        int i = left;
        int i2 = right;
        int i3 = UIntArray.m189getpVg5ArA(array, (left + right) / 2);
        while (i <= i2) {
            while (UnsignedKt.uintCompare(UIntArray.m189getpVg5ArA(array, i), i3) < 0) {
                i++;
            }
            while (UnsignedKt.uintCompare(UIntArray.m189getpVg5ArA(array, i2), i3) > 0) {
                i2--;
            }
            if (i <= i2) {
                int i4 = UIntArray.m189getpVg5ArA(array, i);
                UIntArray.m194setVXSXFK8(array, i, UIntArray.m189getpVg5ArA(array, i2));
                UIntArray.m194setVXSXFK8(array, i2, i4);
                i++;
                i2--;
            }
        }
        return i;
    }

    /* renamed from: quickSort--nroSd4  reason: not valid java name */
    private static final void m487quickSortnroSd4(long[] array, int left, int right) {
        int r0 = m483partitionnroSd4(array, left, right);
        if (left < r0 - 1) {
            m487quickSortnroSd4(array, left, r0 - 1);
        }
        if (r0 < right) {
            m487quickSortnroSd4(array, r0, right);
        }
    }

    /* renamed from: quickSort-4UcCI2c  reason: not valid java name */
    private static final void m488quickSort4UcCI2c(byte[] array, int left, int right) {
        int r0 = m484partition4UcCI2c(array, left, right);
        if (left < r0 - 1) {
            m488quickSort4UcCI2c(array, left, r0 - 1);
        }
        if (r0 < right) {
            m488quickSort4UcCI2c(array, r0, right);
        }
    }

    /* renamed from: quickSort-Aa5vz7o  reason: not valid java name */
    private static final void m489quickSortAa5vz7o(short[] array, int left, int right) {
        int r0 = m485partitionAa5vz7o(array, left, right);
        if (left < r0 - 1) {
            m489quickSortAa5vz7o(array, left, r0 - 1);
        }
        if (r0 < right) {
            m489quickSortAa5vz7o(array, r0, right);
        }
    }

    /* renamed from: quickSort-oBK06Vg  reason: not valid java name */
    private static final void m490quickSortoBK06Vg(int[] array, int left, int right) {
        int r0 = m486partitionoBK06Vg(array, left, right);
        if (left < r0 - 1) {
            m490quickSortoBK06Vg(array, left, r0 - 1);
        }
        if (r0 < right) {
            m490quickSortoBK06Vg(array, r0, right);
        }
    }

    /* renamed from: sortArray--nroSd4  reason: not valid java name */
    public static final void m491sortArraynroSd4(long[] array, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter(array, "array");
        m487quickSortnroSd4(array, fromIndex, toIndex - 1);
    }

    /* renamed from: sortArray-4UcCI2c  reason: not valid java name */
    public static final void m492sortArray4UcCI2c(byte[] array, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter(array, "array");
        m488quickSort4UcCI2c(array, fromIndex, toIndex - 1);
    }

    /* renamed from: sortArray-Aa5vz7o  reason: not valid java name */
    public static final void m493sortArrayAa5vz7o(short[] array, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter(array, "array");
        m489quickSortAa5vz7o(array, fromIndex, toIndex - 1);
    }

    /* renamed from: sortArray-oBK06Vg  reason: not valid java name */
    public static final void m494sortArrayoBK06Vg(int[] array, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter(array, "array");
        m490quickSortoBK06Vg(array, fromIndex, toIndex - 1);
    }
}

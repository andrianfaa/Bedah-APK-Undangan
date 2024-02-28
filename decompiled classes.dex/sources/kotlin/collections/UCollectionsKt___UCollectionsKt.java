package kotlin.collections;

import java.util.Collection;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.UIntArray;
import kotlin.ULong;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u0000F\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u0002H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0007*\b\u0012\u0004\u0012\u00020\u00070\u0002H\u0007ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\n0\u0002H\u0007ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\u0005\u001a\u001a\u0010\f\u001a\u00020\r*\b\u0012\u0004\u0012\u00020\u00030\u000eH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a\u001a\u0010\u0010\u001a\u00020\u0011*\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a\u001a\u0010\u0013\u001a\u00020\u0014*\b\u0012\u0004\u0012\u00020\u00070\u000eH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0015\u001a\u001a\u0010\u0016\u001a\u00020\u0017*\b\u0012\u0004\u0012\u00020\n0\u000eH\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0018\u0002\u0004\n\u0002\b\u0019¨\u0006\u0019"}, d2 = {"sum", "Lkotlin/UInt;", "", "Lkotlin/UByte;", "sumOfUByte", "(Ljava/lang/Iterable;)I", "sumOfUInt", "Lkotlin/ULong;", "sumOfULong", "(Ljava/lang/Iterable;)J", "Lkotlin/UShort;", "sumOfUShort", "toUByteArray", "Lkotlin/UByteArray;", "", "(Ljava/util/Collection;)[B", "toUIntArray", "Lkotlin/UIntArray;", "(Ljava/util/Collection;)[I", "toULongArray", "Lkotlin/ULongArray;", "(Ljava/util/Collection;)[J", "toUShortArray", "Lkotlin/UShortArray;", "(Ljava/util/Collection;)[S", "kotlin-stdlib"}, k = 5, mv = {1, 7, 1}, xi = 49, xs = "kotlin/collections/UCollectionsKt")
/* compiled from: _UCollections.kt */
class UCollectionsKt___UCollectionsKt {
    public static final int sumOfUByte(Iterable<UByte> $this$sum) {
        Intrinsics.checkNotNullParameter($this$sum, "<this>");
        int i = 0;
        for (UByte r2 : $this$sum) {
            i = UInt.m130constructorimpl(UInt.m130constructorimpl(r2.m103unboximpl() & UByte.MAX_VALUE) + i);
        }
        return i;
    }

    public static final int sumOfUInt(Iterable<UInt> $this$sum) {
        Intrinsics.checkNotNullParameter($this$sum, "<this>");
        int i = 0;
        for (UInt r2 : $this$sum) {
            i = UInt.m130constructorimpl(i + r2.m181unboximpl());
        }
        return i;
    }

    public static final long sumOfULong(Iterable<ULong> $this$sum) {
        Intrinsics.checkNotNullParameter($this$sum, "<this>");
        long j = 0;
        for (ULong r3 : $this$sum) {
            j = ULong.m208constructorimpl(j + r3.m259unboximpl());
        }
        return j;
    }

    public static final int sumOfUShort(Iterable<UShort> $this$sum) {
        Intrinsics.checkNotNullParameter($this$sum, "<this>");
        int i = 0;
        for (UShort r2 : $this$sum) {
            i = UInt.m130constructorimpl(UInt.m130constructorimpl(65535 & r2.m363unboximpl()) + i);
        }
        return i;
    }

    public static final byte[] toUByteArray(Collection<UByte> $this$toUByteArray) {
        Intrinsics.checkNotNullParameter($this$toUByteArray, "<this>");
        byte[] r0 = UByteArray.m105constructorimpl($this$toUByteArray.size());
        int i = 0;
        for (UByte r3 : $this$toUByteArray) {
            UByteArray.m116setVurrAj0(r0, i, r3.m103unboximpl());
            i++;
        }
        return r0;
    }

    public static final int[] toUIntArray(Collection<UInt> $this$toUIntArray) {
        Intrinsics.checkNotNullParameter($this$toUIntArray, "<this>");
        int[] r0 = UIntArray.m183constructorimpl($this$toUIntArray.size());
        int i = 0;
        for (UInt r3 : $this$toUIntArray) {
            UIntArray.m194setVXSXFK8(r0, i, r3.m181unboximpl());
            i++;
        }
        return r0;
    }

    public static final long[] toULongArray(Collection<ULong> $this$toULongArray) {
        Intrinsics.checkNotNullParameter($this$toULongArray, "<this>");
        long[] r0 = ULongArray.m261constructorimpl($this$toULongArray.size());
        int i = 0;
        for (ULong r3 : $this$toULongArray) {
            ULongArray.m272setk8EXiF4(r0, i, r3.m259unboximpl());
            i++;
        }
        return r0;
    }

    public static final short[] toUShortArray(Collection<UShort> $this$toUShortArray) {
        Intrinsics.checkNotNullParameter($this$toUShortArray, "<this>");
        short[] r0 = UShortArray.m365constructorimpl($this$toUShortArray.size());
        int i = 0;
        for (UShort r3 : $this$toUShortArray) {
            UShortArray.m376set01HTLdE(r0, i, r3.m363unboximpl());
            i++;
        }
        return r0;
    }
}

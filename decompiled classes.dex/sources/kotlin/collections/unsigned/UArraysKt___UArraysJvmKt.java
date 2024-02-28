package kotlin.collections.unsigned;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.UIntArray;
import kotlin.ULong;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.UnsignedKt;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.collections.IntIterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

@Metadata(d1 = {"\u0000h\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b \n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00060\u0001*\u00020\u0007H\u0007ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\n0\u0001*\u00020\u000bH\u0007ø\u0001\u0000¢\u0006\u0004\b\f\u0010\r\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0001*\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0004\b\u0010\u0010\u0011\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00022\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0017\u0010\u0018\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00062\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0019\u0010\u001a\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\n2\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007ø\u0001\u0000¢\u0006\u0004\b\u001b\u0010\u001c\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u000e2\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007ø\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u001e\u001a\u001f\u0010\u001f\u001a\u00020\u0002*\u00020\u00032\u0006\u0010 \u001a\u00020\u0013H\bø\u0001\u0000¢\u0006\u0004\b!\u0010\"\u001a\u001f\u0010\u001f\u001a\u00020\u0006*\u00020\u00072\u0006\u0010 \u001a\u00020\u0013H\bø\u0001\u0000¢\u0006\u0004\b#\u0010$\u001a\u001f\u0010\u001f\u001a\u00020\n*\u00020\u000b2\u0006\u0010 \u001a\u00020\u0013H\bø\u0001\u0000¢\u0006\u0004\b%\u0010&\u001a\u001f\u0010\u001f\u001a\u00020\u000e*\u00020\u000f2\u0006\u0010 \u001a\u00020\u0013H\bø\u0001\u0000¢\u0006\u0004\b'\u0010(\u001a\u0018\u0010)\u001a\u0004\u0018\u00010\u0002*\u00020\u0003H\u0007ø\u0001\u0000¢\u0006\u0004\b*\u0010+\u001a\u0018\u0010)\u001a\u0004\u0018\u00010\u0006*\u00020\u0007H\u0007ø\u0001\u0000¢\u0006\u0004\b,\u0010-\u001a\u0018\u0010)\u001a\u0004\u0018\u00010\n*\u00020\u000bH\u0007ø\u0001\u0000¢\u0006\u0004\b.\u0010/\u001a\u0018\u0010)\u001a\u0004\u0018\u00010\u000e*\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0004\b0\u00101\u001a@\u00102\u001a\u0004\u0018\u00010\u0002\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H306H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b7\u00108\u001a@\u00102\u001a\u0004\u0018\u00010\u0006\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H306H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b9\u0010:\u001a@\u00102\u001a\u0004\u0018\u00010\n\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u0002H306H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b;\u0010<\u001a@\u00102\u001a\u0004\u0018\u00010\u000e\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u0002H306H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b=\u0010>\u001a4\u0010?\u001a\u0004\u0018\u00010\u0002*\u00020\u00032\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00020Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0002`BH\u0007ø\u0001\u0000¢\u0006\u0004\bC\u0010D\u001a4\u0010?\u001a\u0004\u0018\u00010\u0006*\u00020\u00072\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00060Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0006`BH\u0007ø\u0001\u0000¢\u0006\u0004\bE\u0010F\u001a4\u0010?\u001a\u0004\u0018\u00010\n*\u00020\u000b2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\n0Aj\n\u0012\u0006\b\u0000\u0012\u00020\n`BH\u0007ø\u0001\u0000¢\u0006\u0004\bG\u0010H\u001a4\u0010?\u001a\u0004\u0018\u00010\u000e*\u00020\u000f2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u000e0Aj\n\u0012\u0006\b\u0000\u0012\u00020\u000e`BH\u0007ø\u0001\u0000¢\u0006\u0004\bI\u0010J\u001a\u0018\u0010K\u001a\u0004\u0018\u00010\u0002*\u00020\u0003H\u0007ø\u0001\u0000¢\u0006\u0004\bL\u0010+\u001a\u0018\u0010K\u001a\u0004\u0018\u00010\u0006*\u00020\u0007H\u0007ø\u0001\u0000¢\u0006\u0004\bM\u0010-\u001a\u0018\u0010K\u001a\u0004\u0018\u00010\n*\u00020\u000bH\u0007ø\u0001\u0000¢\u0006\u0004\bN\u0010/\u001a\u0018\u0010K\u001a\u0004\u0018\u00010\u000e*\u00020\u000fH\u0007ø\u0001\u0000¢\u0006\u0004\bO\u00101\u001a@\u0010P\u001a\u0004\u0018\u00010\u0002\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H306H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bQ\u00108\u001a@\u0010P\u001a\u0004\u0018\u00010\u0006\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H306H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bR\u0010:\u001a@\u0010P\u001a\u0004\u0018\u00010\n\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u0002H306H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bS\u0010<\u001a@\u0010P\u001a\u0004\u0018\u00010\u000e\"\u000e\b\u0000\u00103*\b\u0012\u0004\u0012\u0002H304*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u0002H306H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\bT\u0010>\u001a4\u0010U\u001a\u0004\u0018\u00010\u0002*\u00020\u00032\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00020Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0002`BH\u0007ø\u0001\u0000¢\u0006\u0004\bV\u0010D\u001a4\u0010U\u001a\u0004\u0018\u00010\u0006*\u00020\u00072\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00060Aj\n\u0012\u0006\b\u0000\u0012\u00020\u0006`BH\u0007ø\u0001\u0000¢\u0006\u0004\bW\u0010F\u001a4\u0010U\u001a\u0004\u0018\u00010\n*\u00020\u000b2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\n0Aj\n\u0012\u0006\b\u0000\u0012\u00020\n`BH\u0007ø\u0001\u0000¢\u0006\u0004\bX\u0010H\u001a4\u0010U\u001a\u0004\u0018\u00010\u000e*\u00020\u000f2\u001a\u0010@\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u000e0Aj\n\u0012\u0006\b\u0000\u0012\u00020\u000e`BH\u0007ø\u0001\u0000¢\u0006\u0004\bY\u0010J\u001a.\u0010Z\u001a\u00020[*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020[06H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b\\\u0010]\u001a.\u0010Z\u001a\u00020^*\u00020\u00032\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020^06H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b_\u0010`\u001a.\u0010Z\u001a\u00020[*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020[06H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b\\\u0010a\u001a.\u0010Z\u001a\u00020^*\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020^06H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b_\u0010b\u001a.\u0010Z\u001a\u00020[*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020[06H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b\\\u0010c\u001a.\u0010Z\u001a\u00020^*\u00020\u000b2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020^06H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b_\u0010d\u001a.\u0010Z\u001a\u00020[*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020[06H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b\\\u0010e\u001a.\u0010Z\u001a\u00020^*\u00020\u000f2\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020^06H\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b_\u0010f\u0002\u000b\n\u0002\b\u0019\n\u0005\b20\u0001¨\u0006g"}, d2 = {"asList", "", "Lkotlin/UByte;", "Lkotlin/UByteArray;", "asList-GBYM_sE", "([B)Ljava/util/List;", "Lkotlin/UInt;", "Lkotlin/UIntArray;", "asList--ajY-9A", "([I)Ljava/util/List;", "Lkotlin/ULong;", "Lkotlin/ULongArray;", "asList-QwZRm1k", "([J)Ljava/util/List;", "Lkotlin/UShort;", "Lkotlin/UShortArray;", "asList-rL5Bavg", "([S)Ljava/util/List;", "binarySearch", "", "element", "fromIndex", "toIndex", "binarySearch-WpHrYlw", "([BBII)I", "binarySearch-2fe2U9s", "([IIII)I", "binarySearch-K6DWlUc", "([JJII)I", "binarySearch-EtDCXyQ", "([SSII)I", "elementAt", "index", "elementAt-PpDY95g", "([BI)B", "elementAt-qFRl0hI", "([II)I", "elementAt-r7IrZao", "([JI)J", "elementAt-nggk6HY", "([SI)S", "max", "max-GBYM_sE", "([B)Lkotlin/UByte;", "max--ajY-9A", "([I)Lkotlin/UInt;", "max-QwZRm1k", "([J)Lkotlin/ULong;", "max-rL5Bavg", "([S)Lkotlin/UShort;", "maxBy", "R", "", "selector", "Lkotlin/Function1;", "maxBy-JOV_ifY", "([BLkotlin/jvm/functions/Function1;)Lkotlin/UByte;", "maxBy-jgv0xPQ", "([ILkotlin/jvm/functions/Function1;)Lkotlin/UInt;", "maxBy-MShoTSo", "([JLkotlin/jvm/functions/Function1;)Lkotlin/ULong;", "maxBy-xTcfx_M", "([SLkotlin/jvm/functions/Function1;)Lkotlin/UShort;", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "maxWith-XMRcp5o", "([BLjava/util/Comparator;)Lkotlin/UByte;", "maxWith-YmdZ_VM", "([ILjava/util/Comparator;)Lkotlin/UInt;", "maxWith-zrEWJaI", "([JLjava/util/Comparator;)Lkotlin/ULong;", "maxWith-eOHTfZs", "([SLjava/util/Comparator;)Lkotlin/UShort;", "min", "min-GBYM_sE", "min--ajY-9A", "min-QwZRm1k", "min-rL5Bavg", "minBy", "minBy-JOV_ifY", "minBy-jgv0xPQ", "minBy-MShoTSo", "minBy-xTcfx_M", "minWith", "minWith-XMRcp5o", "minWith-YmdZ_VM", "minWith-zrEWJaI", "minWith-eOHTfZs", "sumOf", "Ljava/math/BigDecimal;", "sumOfBigDecimal", "([BLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "Ljava/math/BigInteger;", "sumOfBigInteger", "([BLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([ILkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([ILkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([JLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([JLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([SLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([SLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "kotlin-stdlib"}, k = 5, mv = {1, 7, 1}, pn = "kotlin.collections", xi = 49, xs = "kotlin/collections/unsigned/UArraysKt")
/* compiled from: _UArraysJvm.kt */
class UArraysKt___UArraysJvmKt {
    /* renamed from: asList--ajY-9A  reason: not valid java name */
    public static final List<UInt> m495asListajY9A(int[] $this$asList_u2d_u2dajY_u2d9A) {
        Intrinsics.checkNotNullParameter($this$asList_u2d_u2dajY_u2d9A, "$this$asList");
        return new UArraysKt___UArraysJvmKt$asList$1($this$asList_u2d_u2dajY_u2d9A);
    }

    /* renamed from: asList-GBYM_sE  reason: not valid java name */
    public static final List<UByte> m496asListGBYM_sE(byte[] $this$asList_u2dGBYM_sE) {
        Intrinsics.checkNotNullParameter($this$asList_u2dGBYM_sE, "$this$asList");
        return new UArraysKt___UArraysJvmKt$asList$3($this$asList_u2dGBYM_sE);
    }

    /* renamed from: asList-QwZRm1k  reason: not valid java name */
    public static final List<ULong> m497asListQwZRm1k(long[] $this$asList_u2dQwZRm1k) {
        Intrinsics.checkNotNullParameter($this$asList_u2dQwZRm1k, "$this$asList");
        return new UArraysKt___UArraysJvmKt$asList$2($this$asList_u2dQwZRm1k);
    }

    /* renamed from: asList-rL5Bavg  reason: not valid java name */
    public static final List<UShort> m498asListrL5Bavg(short[] $this$asList_u2drL5Bavg) {
        Intrinsics.checkNotNullParameter($this$asList_u2drL5Bavg, "$this$asList");
        return new UArraysKt___UArraysJvmKt$asList$4($this$asList_u2drL5Bavg);
    }

    /* renamed from: binarySearch-2fe2U9s  reason: not valid java name */
    public static final int m499binarySearch2fe2U9s(int[] $this$binarySearch_u2d2fe2U9s, int element, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter($this$binarySearch_u2d2fe2U9s, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, UIntArray.m190getSizeimpl($this$binarySearch_u2d2fe2U9s));
        int i = element;
        int i2 = fromIndex;
        int i3 = toIndex - 1;
        while (i2 <= i3) {
            int i4 = (i2 + i3) >>> 1;
            int uintCompare = UnsignedKt.uintCompare($this$binarySearch_u2d2fe2U9s[i4], i);
            if (uintCompare < 0) {
                i2 = i4 + 1;
            } else if (uintCompare <= 0) {
                return i4;
            } else {
                i3 = i4 - 1;
            }
        }
        return -(i2 + 1);
    }

    /* renamed from: binarySearch-2fe2U9s$default  reason: not valid java name */
    public static /* synthetic */ int m500binarySearch2fe2U9s$default(int[] iArr, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i2 = 0;
        }
        if ((i4 & 4) != 0) {
            i3 = UIntArray.m190getSizeimpl(iArr);
        }
        return UArraysKt.m499binarySearch2fe2U9s(iArr, i, i2, i3);
    }

    /* renamed from: binarySearch-EtDCXyQ  reason: not valid java name */
    public static final int m501binarySearchEtDCXyQ(short[] $this$binarySearch_u2dEtDCXyQ, short element, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter($this$binarySearch_u2dEtDCXyQ, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, UShortArray.m372getSizeimpl($this$binarySearch_u2dEtDCXyQ));
        short s = 65535 & element;
        int i = fromIndex;
        int i2 = toIndex - 1;
        while (i <= i2) {
            int i3 = (i + i2) >>> 1;
            int uintCompare = UnsignedKt.uintCompare($this$binarySearch_u2dEtDCXyQ[i3], s);
            if (uintCompare < 0) {
                i = i3 + 1;
            } else if (uintCompare <= 0) {
                return i3;
            } else {
                i2 = i3 - 1;
            }
        }
        return -(i + 1);
    }

    /* renamed from: binarySearch-EtDCXyQ$default  reason: not valid java name */
    public static /* synthetic */ int m502binarySearchEtDCXyQ$default(short[] sArr, short s, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = UShortArray.m372getSizeimpl(sArr);
        }
        return UArraysKt.m501binarySearchEtDCXyQ(sArr, s, i, i2);
    }

    /* renamed from: binarySearch-K6DWlUc  reason: not valid java name */
    public static final int m503binarySearchK6DWlUc(long[] $this$binarySearch_u2dK6DWlUc, long element, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter($this$binarySearch_u2dK6DWlUc, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, ULongArray.m268getSizeimpl($this$binarySearch_u2dK6DWlUc));
        long j = element;
        int i = fromIndex;
        int i2 = toIndex - 1;
        while (i <= i2) {
            int i3 = (i + i2) >>> 1;
            int ulongCompare = UnsignedKt.ulongCompare($this$binarySearch_u2dK6DWlUc[i3], j);
            if (ulongCompare < 0) {
                i = i3 + 1;
            } else if (ulongCompare <= 0) {
                return i3;
            } else {
                i2 = i3 - 1;
            }
        }
        return -(i + 1);
    }

    /* renamed from: binarySearch-K6DWlUc$default  reason: not valid java name */
    public static /* synthetic */ int m504binarySearchK6DWlUc$default(long[] jArr, long j, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = ULongArray.m268getSizeimpl(jArr);
        }
        return UArraysKt.m503binarySearchK6DWlUc(jArr, j, i, i2);
    }

    /* renamed from: binarySearch-WpHrYlw  reason: not valid java name */
    public static final int m505binarySearchWpHrYlw(byte[] $this$binarySearch_u2dWpHrYlw, byte element, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter($this$binarySearch_u2dWpHrYlw, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, UByteArray.m112getSizeimpl($this$binarySearch_u2dWpHrYlw));
        byte b = element & UByte.MAX_VALUE;
        int i = fromIndex;
        int i2 = toIndex - 1;
        while (i <= i2) {
            int i3 = (i + i2) >>> 1;
            int uintCompare = UnsignedKt.uintCompare($this$binarySearch_u2dWpHrYlw[i3], b);
            if (uintCompare < 0) {
                i = i3 + 1;
            } else if (uintCompare <= 0) {
                return i3;
            } else {
                i2 = i3 - 1;
            }
        }
        return -(i + 1);
    }

    /* renamed from: binarySearch-WpHrYlw$default  reason: not valid java name */
    public static /* synthetic */ int m506binarySearchWpHrYlw$default(byte[] bArr, byte b, int i, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = UByteArray.m112getSizeimpl(bArr);
        }
        return UArraysKt.m505binarySearchWpHrYlw(bArr, b, i, i2);
    }

    /* renamed from: elementAt-PpDY95g  reason: not valid java name */
    private static final byte m507elementAtPpDY95g(byte[] $this$elementAt_u2dPpDY95g, int index) {
        Intrinsics.checkNotNullParameter($this$elementAt_u2dPpDY95g, "$this$elementAt");
        return UByteArray.m111getw2LRezQ($this$elementAt_u2dPpDY95g, index);
    }

    /* renamed from: elementAt-nggk6HY  reason: not valid java name */
    private static final short m508elementAtnggk6HY(short[] $this$elementAt_u2dnggk6HY, int index) {
        Intrinsics.checkNotNullParameter($this$elementAt_u2dnggk6HY, "$this$elementAt");
        return UShortArray.m371getMh2AYeg($this$elementAt_u2dnggk6HY, index);
    }

    /* renamed from: elementAt-qFRl0hI  reason: not valid java name */
    private static final int m509elementAtqFRl0hI(int[] $this$elementAt_u2dqFRl0hI, int index) {
        Intrinsics.checkNotNullParameter($this$elementAt_u2dqFRl0hI, "$this$elementAt");
        return UIntArray.m189getpVg5ArA($this$elementAt_u2dqFRl0hI, index);
    }

    /* renamed from: elementAt-r7IrZao  reason: not valid java name */
    private static final long m510elementAtr7IrZao(long[] $this$elementAt_u2dr7IrZao, int index) {
        Intrinsics.checkNotNullParameter($this$elementAt_u2dr7IrZao, "$this$elementAt");
        return ULongArray.m267getsVKNKU($this$elementAt_u2dr7IrZao, index);
    }

    @Deprecated(message = "Use maxByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: maxBy-JOV_ifY  reason: not valid java name */
    private static final /* synthetic */ <R extends Comparable<? super R>> UByte m515maxByJOV_ifY(byte[] $this$maxBy_u2dJOV_ifY, Function1<? super UByte, ? extends R> selector) {
        Intrinsics.checkNotNullParameter($this$maxBy_u2dJOV_ifY, "$this$maxBy");
        Intrinsics.checkNotNullParameter(selector, "selector");
        if (UByteArray.m114isEmptyimpl($this$maxBy_u2dJOV_ifY)) {
            return null;
        }
        byte b = UByteArray.m111getw2LRezQ($this$maxBy_u2dJOV_ifY, 0);
        int lastIndex = ArraysKt.getLastIndex($this$maxBy_u2dJOV_ifY);
        if (lastIndex != 0) {
            Comparable comparable = (Comparable) selector.invoke(UByte.m48boximpl(b));
            IntIterator it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                byte b2 = UByteArray.m111getw2LRezQ($this$maxBy_u2dJOV_ifY, it.nextInt());
                Comparable comparable2 = (Comparable) selector.invoke(UByte.m48boximpl(b2));
                if (comparable.compareTo(comparable2) < 0) {
                    b = b2;
                    comparable = comparable2;
                }
            }
        }
        return UByte.m48boximpl(b);
    }

    @Deprecated(message = "Use maxByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: maxBy-MShoTSo  reason: not valid java name */
    private static final /* synthetic */ <R extends Comparable<? super R>> ULong m516maxByMShoTSo(long[] $this$maxBy_u2dMShoTSo, Function1<? super ULong, ? extends R> selector) {
        Intrinsics.checkNotNullParameter($this$maxBy_u2dMShoTSo, "$this$maxBy");
        Intrinsics.checkNotNullParameter(selector, "selector");
        if (ULongArray.m270isEmptyimpl($this$maxBy_u2dMShoTSo)) {
            return null;
        }
        long j = ULongArray.m267getsVKNKU($this$maxBy_u2dMShoTSo, 0);
        int lastIndex = ArraysKt.getLastIndex($this$maxBy_u2dMShoTSo);
        if (lastIndex != 0) {
            Comparable comparable = (Comparable) selector.invoke(ULong.m202boximpl(j));
            IntIterator it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                long j2 = ULongArray.m267getsVKNKU($this$maxBy_u2dMShoTSo, it.nextInt());
                Comparable comparable2 = (Comparable) selector.invoke(ULong.m202boximpl(j2));
                if (comparable.compareTo(comparable2) < 0) {
                    j = j2;
                    comparable = comparable2;
                }
            }
        }
        return ULong.m202boximpl(j);
    }

    @Deprecated(message = "Use maxByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: maxBy-jgv0xPQ  reason: not valid java name */
    private static final /* synthetic */ <R extends Comparable<? super R>> UInt m517maxByjgv0xPQ(int[] $this$maxBy_u2djgv0xPQ, Function1<? super UInt, ? extends R> selector) {
        Intrinsics.checkNotNullParameter($this$maxBy_u2djgv0xPQ, "$this$maxBy");
        Intrinsics.checkNotNullParameter(selector, "selector");
        if (UIntArray.m192isEmptyimpl($this$maxBy_u2djgv0xPQ)) {
            return null;
        }
        int i = UIntArray.m189getpVg5ArA($this$maxBy_u2djgv0xPQ, 0);
        int lastIndex = ArraysKt.getLastIndex($this$maxBy_u2djgv0xPQ);
        if (lastIndex != 0) {
            Comparable comparable = (Comparable) selector.invoke(UInt.m124boximpl(i));
            IntIterator it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                int i2 = UIntArray.m189getpVg5ArA($this$maxBy_u2djgv0xPQ, it.nextInt());
                Comparable comparable2 = (Comparable) selector.invoke(UInt.m124boximpl(i2));
                if (comparable.compareTo(comparable2) < 0) {
                    i = i2;
                    comparable = comparable2;
                }
            }
        }
        return UInt.m124boximpl(i);
    }

    @Deprecated(message = "Use maxByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: maxBy-xTcfx_M  reason: not valid java name */
    private static final /* synthetic */ <R extends Comparable<? super R>> UShort m518maxByxTcfx_M(short[] $this$maxBy_u2dxTcfx_M, Function1<? super UShort, ? extends R> selector) {
        Intrinsics.checkNotNullParameter($this$maxBy_u2dxTcfx_M, "$this$maxBy");
        Intrinsics.checkNotNullParameter(selector, "selector");
        if (UShortArray.m374isEmptyimpl($this$maxBy_u2dxTcfx_M)) {
            return null;
        }
        short s = UShortArray.m371getMh2AYeg($this$maxBy_u2dxTcfx_M, 0);
        int lastIndex = ArraysKt.getLastIndex($this$maxBy_u2dxTcfx_M);
        if (lastIndex != 0) {
            Comparable comparable = (Comparable) selector.invoke(UShort.m308boximpl(s));
            IntIterator it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                short s2 = UShortArray.m371getMh2AYeg($this$maxBy_u2dxTcfx_M, it.nextInt());
                Comparable comparable2 = (Comparable) selector.invoke(UShort.m308boximpl(s2));
                if (comparable.compareTo(comparable2) < 0) {
                    s = s2;
                    comparable = comparable2;
                }
            }
        }
        return UShort.m308boximpl(s);
    }

    @Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: maxWith-XMRcp5o  reason: not valid java name */
    public static final /* synthetic */ UByte m519maxWithXMRcp5o(byte[] $this$maxWith_u2dXMRcp5o, Comparator comparator) {
        Intrinsics.checkNotNullParameter($this$maxWith_u2dXMRcp5o, "$this$maxWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return UArraysKt.m899maxWithOrNullXMRcp5o($this$maxWith_u2dXMRcp5o, comparator);
    }

    @Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: maxWith-YmdZ_VM  reason: not valid java name */
    public static final /* synthetic */ UInt m520maxWithYmdZ_VM(int[] $this$maxWith_u2dYmdZ_VM, Comparator comparator) {
        Intrinsics.checkNotNullParameter($this$maxWith_u2dYmdZ_VM, "$this$maxWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return UArraysKt.m900maxWithOrNullYmdZ_VM($this$maxWith_u2dYmdZ_VM, comparator);
    }

    @Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: maxWith-eOHTfZs  reason: not valid java name */
    public static final /* synthetic */ UShort m521maxWitheOHTfZs(short[] $this$maxWith_u2deOHTfZs, Comparator comparator) {
        Intrinsics.checkNotNullParameter($this$maxWith_u2deOHTfZs, "$this$maxWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return UArraysKt.m901maxWithOrNulleOHTfZs($this$maxWith_u2deOHTfZs, comparator);
    }

    @Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: maxWith-zrEWJaI  reason: not valid java name */
    public static final /* synthetic */ ULong m522maxWithzrEWJaI(long[] $this$maxWith_u2dzrEWJaI, Comparator comparator) {
        Intrinsics.checkNotNullParameter($this$maxWith_u2dzrEWJaI, "$this$maxWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return UArraysKt.m902maxWithOrNullzrEWJaI($this$maxWith_u2dzrEWJaI, comparator);
    }

    @Deprecated(message = "Use minByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: minBy-JOV_ifY  reason: not valid java name */
    private static final /* synthetic */ <R extends Comparable<? super R>> UByte m527minByJOV_ifY(byte[] $this$minBy_u2dJOV_ifY, Function1<? super UByte, ? extends R> selector) {
        Intrinsics.checkNotNullParameter($this$minBy_u2dJOV_ifY, "$this$minBy");
        Intrinsics.checkNotNullParameter(selector, "selector");
        if (UByteArray.m114isEmptyimpl($this$minBy_u2dJOV_ifY)) {
            return null;
        }
        byte b = UByteArray.m111getw2LRezQ($this$minBy_u2dJOV_ifY, 0);
        int lastIndex = ArraysKt.getLastIndex($this$minBy_u2dJOV_ifY);
        if (lastIndex != 0) {
            Comparable comparable = (Comparable) selector.invoke(UByte.m48boximpl(b));
            IntIterator it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                byte b2 = UByteArray.m111getw2LRezQ($this$minBy_u2dJOV_ifY, it.nextInt());
                Comparable comparable2 = (Comparable) selector.invoke(UByte.m48boximpl(b2));
                if (comparable.compareTo(comparable2) > 0) {
                    b = b2;
                    comparable = comparable2;
                }
            }
        }
        return UByte.m48boximpl(b);
    }

    @Deprecated(message = "Use minByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: minBy-MShoTSo  reason: not valid java name */
    private static final /* synthetic */ <R extends Comparable<? super R>> ULong m528minByMShoTSo(long[] $this$minBy_u2dMShoTSo, Function1<? super ULong, ? extends R> selector) {
        Intrinsics.checkNotNullParameter($this$minBy_u2dMShoTSo, "$this$minBy");
        Intrinsics.checkNotNullParameter(selector, "selector");
        if (ULongArray.m270isEmptyimpl($this$minBy_u2dMShoTSo)) {
            return null;
        }
        long j = ULongArray.m267getsVKNKU($this$minBy_u2dMShoTSo, 0);
        int lastIndex = ArraysKt.getLastIndex($this$minBy_u2dMShoTSo);
        if (lastIndex != 0) {
            Comparable comparable = (Comparable) selector.invoke(ULong.m202boximpl(j));
            IntIterator it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                long j2 = ULongArray.m267getsVKNKU($this$minBy_u2dMShoTSo, it.nextInt());
                Comparable comparable2 = (Comparable) selector.invoke(ULong.m202boximpl(j2));
                if (comparable.compareTo(comparable2) > 0) {
                    j = j2;
                    comparable = comparable2;
                }
            }
        }
        return ULong.m202boximpl(j);
    }

    @Deprecated(message = "Use minByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: minBy-jgv0xPQ  reason: not valid java name */
    private static final /* synthetic */ <R extends Comparable<? super R>> UInt m529minByjgv0xPQ(int[] $this$minBy_u2djgv0xPQ, Function1<? super UInt, ? extends R> selector) {
        Intrinsics.checkNotNullParameter($this$minBy_u2djgv0xPQ, "$this$minBy");
        Intrinsics.checkNotNullParameter(selector, "selector");
        if (UIntArray.m192isEmptyimpl($this$minBy_u2djgv0xPQ)) {
            return null;
        }
        int i = UIntArray.m189getpVg5ArA($this$minBy_u2djgv0xPQ, 0);
        int lastIndex = ArraysKt.getLastIndex($this$minBy_u2djgv0xPQ);
        if (lastIndex != 0) {
            Comparable comparable = (Comparable) selector.invoke(UInt.m124boximpl(i));
            IntIterator it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                int i2 = UIntArray.m189getpVg5ArA($this$minBy_u2djgv0xPQ, it.nextInt());
                Comparable comparable2 = (Comparable) selector.invoke(UInt.m124boximpl(i2));
                if (comparable.compareTo(comparable2) > 0) {
                    i = i2;
                    comparable = comparable2;
                }
            }
        }
        return UInt.m124boximpl(i);
    }

    @Deprecated(message = "Use minByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: minBy-xTcfx_M  reason: not valid java name */
    private static final /* synthetic */ <R extends Comparable<? super R>> UShort m530minByxTcfx_M(short[] $this$minBy_u2dxTcfx_M, Function1<? super UShort, ? extends R> selector) {
        Intrinsics.checkNotNullParameter($this$minBy_u2dxTcfx_M, "$this$minBy");
        Intrinsics.checkNotNullParameter(selector, "selector");
        if (UShortArray.m374isEmptyimpl($this$minBy_u2dxTcfx_M)) {
            return null;
        }
        short s = UShortArray.m371getMh2AYeg($this$minBy_u2dxTcfx_M, 0);
        int lastIndex = ArraysKt.getLastIndex($this$minBy_u2dxTcfx_M);
        if (lastIndex != 0) {
            Comparable comparable = (Comparable) selector.invoke(UShort.m308boximpl(s));
            IntIterator it = new IntRange(1, lastIndex).iterator();
            while (it.hasNext()) {
                short s2 = UShortArray.m371getMh2AYeg($this$minBy_u2dxTcfx_M, it.nextInt());
                Comparable comparable2 = (Comparable) selector.invoke(UShort.m308boximpl(s2));
                if (comparable.compareTo(comparable2) > 0) {
                    s = s2;
                    comparable = comparable2;
                }
            }
        }
        return UShort.m308boximpl(s);
    }

    @Deprecated(message = "Use minWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: minWith-XMRcp5o  reason: not valid java name */
    public static final /* synthetic */ UByte m531minWithXMRcp5o(byte[] $this$minWith_u2dXMRcp5o, Comparator comparator) {
        Intrinsics.checkNotNullParameter($this$minWith_u2dXMRcp5o, "$this$minWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return UArraysKt.m955minWithOrNullXMRcp5o($this$minWith_u2dXMRcp5o, comparator);
    }

    @Deprecated(message = "Use minWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: minWith-YmdZ_VM  reason: not valid java name */
    public static final /* synthetic */ UInt m532minWithYmdZ_VM(int[] $this$minWith_u2dYmdZ_VM, Comparator comparator) {
        Intrinsics.checkNotNullParameter($this$minWith_u2dYmdZ_VM, "$this$minWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return UArraysKt.m956minWithOrNullYmdZ_VM($this$minWith_u2dYmdZ_VM, comparator);
    }

    @Deprecated(message = "Use minWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: minWith-eOHTfZs  reason: not valid java name */
    public static final /* synthetic */ UShort m533minWitheOHTfZs(short[] $this$minWith_u2deOHTfZs, Comparator comparator) {
        Intrinsics.checkNotNullParameter($this$minWith_u2deOHTfZs, "$this$minWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return UArraysKt.m957minWithOrNulleOHTfZs($this$minWith_u2deOHTfZs, comparator);
    }

    @Deprecated(message = "Use minWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    /* renamed from: minWith-zrEWJaI  reason: not valid java name */
    public static final /* synthetic */ ULong m534minWithzrEWJaI(long[] $this$minWith_u2dzrEWJaI, Comparator comparator) {
        Intrinsics.checkNotNullParameter($this$minWith_u2dzrEWJaI, "$this$minWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return UArraysKt.m958minWithOrNullzrEWJaI($this$minWith_u2dzrEWJaI, comparator);
    }

    private static final BigDecimal sumOfBigDecimal(byte[] $this$sumOf_u2dJOV_ifY, Function1<? super UByte, ? extends BigDecimal> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf_u2dJOV_ifY, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigDecimal valueOf = BigDecimal.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        int r1 = UByteArray.m112getSizeimpl($this$sumOf_u2dJOV_ifY);
        for (int i = 0; i < r1; i++) {
            BigDecimal add = valueOf.add((BigDecimal) selector.invoke(UByte.m48boximpl(UByteArray.m111getw2LRezQ($this$sumOf_u2dJOV_ifY, i))));
            Intrinsics.checkNotNullExpressionValue(add, "this.add(other)");
            valueOf = add;
        }
        return valueOf;
    }

    private static final BigDecimal sumOfBigDecimal(int[] $this$sumOf_u2djgv0xPQ, Function1<? super UInt, ? extends BigDecimal> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf_u2djgv0xPQ, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigDecimal valueOf = BigDecimal.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        int r1 = UIntArray.m190getSizeimpl($this$sumOf_u2djgv0xPQ);
        for (int i = 0; i < r1; i++) {
            BigDecimal add = valueOf.add((BigDecimal) selector.invoke(UInt.m124boximpl(UIntArray.m189getpVg5ArA($this$sumOf_u2djgv0xPQ, i))));
            Intrinsics.checkNotNullExpressionValue(add, "this.add(other)");
            valueOf = add;
        }
        return valueOf;
    }

    private static final BigDecimal sumOfBigDecimal(long[] $this$sumOf_u2dMShoTSo, Function1<? super ULong, ? extends BigDecimal> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf_u2dMShoTSo, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigDecimal valueOf = BigDecimal.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        int r1 = ULongArray.m268getSizeimpl($this$sumOf_u2dMShoTSo);
        for (int i = 0; i < r1; i++) {
            BigDecimal add = valueOf.add((BigDecimal) selector.invoke(ULong.m202boximpl(ULongArray.m267getsVKNKU($this$sumOf_u2dMShoTSo, i))));
            Intrinsics.checkNotNullExpressionValue(add, "this.add(other)");
            valueOf = add;
        }
        return valueOf;
    }

    private static final BigDecimal sumOfBigDecimal(short[] $this$sumOf_u2dxTcfx_M, Function1<? super UShort, ? extends BigDecimal> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf_u2dxTcfx_M, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigDecimal valueOf = BigDecimal.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        int r1 = UShortArray.m372getSizeimpl($this$sumOf_u2dxTcfx_M);
        for (int i = 0; i < r1; i++) {
            BigDecimal add = valueOf.add((BigDecimal) selector.invoke(UShort.m308boximpl(UShortArray.m371getMh2AYeg($this$sumOf_u2dxTcfx_M, i))));
            Intrinsics.checkNotNullExpressionValue(add, "this.add(other)");
            valueOf = add;
        }
        return valueOf;
    }

    private static final BigInteger sumOfBigInteger(byte[] $this$sumOf_u2dJOV_ifY, Function1<? super UByte, ? extends BigInteger> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf_u2dJOV_ifY, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigInteger valueOf = BigInteger.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        int r1 = UByteArray.m112getSizeimpl($this$sumOf_u2dJOV_ifY);
        for (int i = 0; i < r1; i++) {
            BigInteger add = valueOf.add((BigInteger) selector.invoke(UByte.m48boximpl(UByteArray.m111getw2LRezQ($this$sumOf_u2dJOV_ifY, i))));
            Intrinsics.checkNotNullExpressionValue(add, "this.add(other)");
            valueOf = add;
        }
        return valueOf;
    }

    private static final BigInteger sumOfBigInteger(int[] $this$sumOf_u2djgv0xPQ, Function1<? super UInt, ? extends BigInteger> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf_u2djgv0xPQ, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigInteger valueOf = BigInteger.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        int r1 = UIntArray.m190getSizeimpl($this$sumOf_u2djgv0xPQ);
        for (int i = 0; i < r1; i++) {
            BigInteger add = valueOf.add((BigInteger) selector.invoke(UInt.m124boximpl(UIntArray.m189getpVg5ArA($this$sumOf_u2djgv0xPQ, i))));
            Intrinsics.checkNotNullExpressionValue(add, "this.add(other)");
            valueOf = add;
        }
        return valueOf;
    }

    private static final BigInteger sumOfBigInteger(long[] $this$sumOf_u2dMShoTSo, Function1<? super ULong, ? extends BigInteger> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf_u2dMShoTSo, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigInteger valueOf = BigInteger.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        int r1 = ULongArray.m268getSizeimpl($this$sumOf_u2dMShoTSo);
        for (int i = 0; i < r1; i++) {
            BigInteger add = valueOf.add((BigInteger) selector.invoke(ULong.m202boximpl(ULongArray.m267getsVKNKU($this$sumOf_u2dMShoTSo, i))));
            Intrinsics.checkNotNullExpressionValue(add, "this.add(other)");
            valueOf = add;
        }
        return valueOf;
    }

    private static final BigInteger sumOfBigInteger(short[] $this$sumOf_u2dxTcfx_M, Function1<? super UShort, ? extends BigInteger> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf_u2dxTcfx_M, "$this$sumOf");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigInteger valueOf = BigInteger.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        int r1 = UShortArray.m372getSizeimpl($this$sumOf_u2dxTcfx_M);
        for (int i = 0; i < r1; i++) {
            BigInteger add = valueOf.add((BigInteger) selector.invoke(UShort.m308boximpl(UShortArray.m371getMh2AYeg($this$sumOf_u2dxTcfx_M, i))));
            Intrinsics.checkNotNullExpressionValue(add, "this.add(other)");
            valueOf = add;
        }
        return valueOf;
    }
}

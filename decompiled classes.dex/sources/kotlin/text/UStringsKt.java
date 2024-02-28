package kotlin.text;

import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.UnsignedKt;
import kotlin.jvm.internal.Intrinsics;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000,\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\n2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0004\b\u000b\u0010\f\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\r2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0004\b\u000e\u0010\u000f\u001a\u0014\u0010\u0010\u001a\u00020\u0002*\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0011\u001a\u001c\u0010\u0010\u001a\u00020\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a\u0011\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u0001H\u0007ø\u0001\u0000\u001a\u0019\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000\u001a\u0014\u0010\u0014\u001a\u00020\u0007*\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0015\u001a\u001c\u0010\u0014\u001a\u00020\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0016\u001a\u0011\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u0001H\u0007ø\u0001\u0000\u001a\u0019\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000\u001a\u0014\u0010\u0018\u001a\u00020\n*\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0019\u001a\u001c\u0010\u0018\u001a\u00020\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u001a\u001a\u0011\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u0001H\u0007ø\u0001\u0000\u001a\u0019\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000\u001a\u0014\u0010\u001c\u001a\u00020\r*\u00020\u0001H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u001d\u001a\u001c\u0010\u001c\u001a\u00020\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u001e\u001a\u0011\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u0001H\u0007ø\u0001\u0000\u001a\u0019\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007ø\u0001\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006 "}, d2 = {"toString", "", "Lkotlin/UByte;", "radix", "", "toString-LxnNnR4", "(BI)Ljava/lang/String;", "Lkotlin/UInt;", "toString-V7xB4Y4", "(II)Ljava/lang/String;", "Lkotlin/ULong;", "toString-JSWoG40", "(JI)Ljava/lang/String;", "Lkotlin/UShort;", "toString-olVBNx4", "(SI)Ljava/lang/String;", "toUByte", "(Ljava/lang/String;)B", "(Ljava/lang/String;I)B", "toUByteOrNull", "toUInt", "(Ljava/lang/String;)I", "(Ljava/lang/String;I)I", "toUIntOrNull", "toULong", "(Ljava/lang/String;)J", "(Ljava/lang/String;I)J", "toULongOrNull", "toUShort", "(Ljava/lang/String;)S", "(Ljava/lang/String;I)S", "toUShortOrNull", "kotlin-stdlib"}, k = 2, mv = {1, 7, 1}, xi = 48)
/* compiled from: 0164 */
public final class UStringsKt {
    /* renamed from: toString-JSWoG40  reason: not valid java name */
    public static final String m1331toStringJSWoG40(long $this$toString_u2dJSWoG40, int radix) {
        String ulongToString = UnsignedKt.ulongToString($this$toString_u2dJSWoG40, CharsKt.checkRadix(radix));
        Log1F380D.a((Object) ulongToString);
        return ulongToString;
    }

    public static final byte toUByte(String $this$toUByte) {
        Intrinsics.checkNotNullParameter($this$toUByte, "<this>");
        UByte uByteOrNull = toUByteOrNull($this$toUByte);
        if (uByteOrNull != null) {
            return uByteOrNull.m103unboximpl();
        }
        StringsKt.numberFormatError($this$toUByte);
        throw new KotlinNothingValueException();
    }

    public static final byte toUByte(String $this$toUByte, int radix) {
        Intrinsics.checkNotNullParameter($this$toUByte, "<this>");
        UByte uByteOrNull = toUByteOrNull($this$toUByte, radix);
        if (uByteOrNull != null) {
            return uByteOrNull.m103unboximpl();
        }
        StringsKt.numberFormatError($this$toUByte);
        throw new KotlinNothingValueException();
    }

    public static final UByte toUByteOrNull(String $this$toUByteOrNull) {
        Intrinsics.checkNotNullParameter($this$toUByteOrNull, "<this>");
        return toUByteOrNull($this$toUByteOrNull, 10);
    }

    public static final UByte toUByteOrNull(String $this$toUByteOrNull, int radix) {
        Intrinsics.checkNotNullParameter($this$toUByteOrNull, "<this>");
        UInt uIntOrNull = toUIntOrNull($this$toUByteOrNull, radix);
        if (uIntOrNull == null) {
            return null;
        }
        int r0 = uIntOrNull.m181unboximpl();
        if (UnsignedKt.uintCompare(r0, UInt.m130constructorimpl(255)) > 0) {
            return null;
        }
        return UByte.m48boximpl(UByte.m54constructorimpl((byte) r0));
    }

    public static final int toUInt(String $this$toUInt) {
        Intrinsics.checkNotNullParameter($this$toUInt, "<this>");
        UInt uIntOrNull = toUIntOrNull($this$toUInt);
        if (uIntOrNull != null) {
            return uIntOrNull.m181unboximpl();
        }
        StringsKt.numberFormatError($this$toUInt);
        throw new KotlinNothingValueException();
    }

    public static final int toUInt(String $this$toUInt, int radix) {
        Intrinsics.checkNotNullParameter($this$toUInt, "<this>");
        UInt uIntOrNull = toUIntOrNull($this$toUInt, radix);
        if (uIntOrNull != null) {
            return uIntOrNull.m181unboximpl();
        }
        StringsKt.numberFormatError($this$toUInt);
        throw new KotlinNothingValueException();
    }

    public static final UInt toUIntOrNull(String $this$toUIntOrNull) {
        Intrinsics.checkNotNullParameter($this$toUIntOrNull, "<this>");
        return toUIntOrNull($this$toUIntOrNull, 10);
    }

    public static final UInt toUIntOrNull(String $this$toUIntOrNull, int radix) {
        int i;
        Intrinsics.checkNotNullParameter($this$toUIntOrNull, "<this>");
        CharsKt.checkRadix(radix);
        int length = $this$toUIntOrNull.length();
        if (length == 0) {
            return null;
        }
        char charAt = $this$toUIntOrNull.charAt(0);
        if (Intrinsics.compare((int) charAt, 48) >= 0) {
            i = 0;
        } else if (length == 1 || charAt != '+') {
            return null;
        } else {
            i = 1;
        }
        int i2 = 119304647;
        int r7 = UInt.m130constructorimpl(radix);
        int i3 = 0;
        for (int i4 = i; i4 < length; i4++) {
            int digitOf = CharsKt.digitOf($this$toUIntOrNull.charAt(i4), radix);
            if (digitOf < 0) {
                return null;
            }
            if (UnsignedKt.uintCompare(i3, i2) > 0) {
                if (i2 != 119304647) {
                    return null;
                }
                i2 = UnsignedKt.m383uintDivideJ1ME1BU(-1, r7);
                if (UnsignedKt.uintCompare(i3, i2) > 0) {
                    return null;
                }
            }
            int r8 = UInt.m130constructorimpl(i3 * r7);
            int i5 = r8;
            i3 = UInt.m130constructorimpl(UInt.m130constructorimpl(digitOf) + r8);
            if (UnsignedKt.uintCompare(i3, i5) < 0) {
                return null;
            }
        }
        return UInt.m124boximpl(i3);
    }

    public static final long toULong(String $this$toULong) {
        Intrinsics.checkNotNullParameter($this$toULong, "<this>");
        ULong uLongOrNull = toULongOrNull($this$toULong);
        if (uLongOrNull != null) {
            return uLongOrNull.m259unboximpl();
        }
        StringsKt.numberFormatError($this$toULong);
        throw new KotlinNothingValueException();
    }

    public static final long toULong(String $this$toULong, int radix) {
        Intrinsics.checkNotNullParameter($this$toULong, "<this>");
        ULong uLongOrNull = toULongOrNull($this$toULong, radix);
        if (uLongOrNull != null) {
            return uLongOrNull.m259unboximpl();
        }
        StringsKt.numberFormatError($this$toULong);
        throw new KotlinNothingValueException();
    }

    public static final ULong toULongOrNull(String $this$toULongOrNull) {
        Intrinsics.checkNotNullParameter($this$toULongOrNull, "<this>");
        return toULongOrNull($this$toULongOrNull, 10);
    }

    public static final ULong toULongOrNull(String $this$toULongOrNull, int radix) {
        int i;
        String str = $this$toULongOrNull;
        int i2 = radix;
        Intrinsics.checkNotNullParameter(str, "<this>");
        CharsKt.checkRadix(radix);
        int length = $this$toULongOrNull.length();
        if (length == 0) {
            return null;
        }
        char charAt = str.charAt(0);
        if (Intrinsics.compare((int) charAt, 48) >= 0) {
            i = 0;
        } else if (length == 1 || charAt != '+') {
            return null;
        } else {
            i = 1;
        }
        long j = 512409557603043100L;
        long r12 = ULong.m208constructorimpl((long) i2);
        long j2 = 0;
        int i3 = i;
        while (i3 < length) {
            int i4 = length;
            int digitOf = CharsKt.digitOf(str.charAt(i3), i2);
            if (digitOf < 0) {
                return null;
            }
            if (UnsignedKt.ulongCompare(j2, j) > 0) {
                if (j != 512409557603043100L) {
                    return null;
                }
                j = UnsignedKt.m385ulongDivideeb3DHEI(-1, r12);
                if (UnsignedKt.ulongCompare(j2, j) > 0) {
                    return null;
                }
            }
            long r14 = ULong.m208constructorimpl(j2 * r12);
            long j3 = r14;
            j2 = ULong.m208constructorimpl(ULong.m208constructorimpl(((long) UInt.m130constructorimpl(digitOf)) & 4294967295L) + r14);
            if (UnsignedKt.ulongCompare(j2, j3) < 0) {
                return null;
            }
            i3++;
            str = $this$toULongOrNull;
            i2 = radix;
            length = i4;
        }
        return ULong.m202boximpl(j2);
    }

    public static final short toUShort(String $this$toUShort) {
        Intrinsics.checkNotNullParameter($this$toUShort, "<this>");
        UShort uShortOrNull = toUShortOrNull($this$toUShort);
        if (uShortOrNull != null) {
            return uShortOrNull.m363unboximpl();
        }
        StringsKt.numberFormatError($this$toUShort);
        throw new KotlinNothingValueException();
    }

    public static final short toUShort(String $this$toUShort, int radix) {
        Intrinsics.checkNotNullParameter($this$toUShort, "<this>");
        UShort uShortOrNull = toUShortOrNull($this$toUShort, radix);
        if (uShortOrNull != null) {
            return uShortOrNull.m363unboximpl();
        }
        StringsKt.numberFormatError($this$toUShort);
        throw new KotlinNothingValueException();
    }

    public static final UShort toUShortOrNull(String $this$toUShortOrNull) {
        Intrinsics.checkNotNullParameter($this$toUShortOrNull, "<this>");
        return toUShortOrNull($this$toUShortOrNull, 10);
    }

    public static final UShort toUShortOrNull(String $this$toUShortOrNull, int radix) {
        Intrinsics.checkNotNullParameter($this$toUShortOrNull, "<this>");
        UInt uIntOrNull = toUIntOrNull($this$toUShortOrNull, radix);
        if (uIntOrNull == null) {
            return null;
        }
        int r0 = uIntOrNull.m181unboximpl();
        if (UnsignedKt.uintCompare(r0, UInt.m130constructorimpl(65535)) > 0) {
            return null;
        }
        return UShort.m308boximpl(UShort.m314constructorimpl((short) r0));
    }

    /* renamed from: toString-LxnNnR4  reason: not valid java name */
    public static final String m1332toStringLxnNnR4(byte $this$toString_u2dLxnNnR4, int radix) {
        String num = Integer.toString($this$toString_u2dLxnNnR4 & UByte.MAX_VALUE, CharsKt.checkRadix(radix));
        Log1F380D.a((Object) num);
        Intrinsics.checkNotNullExpressionValue(num, "toString(this, checkRadix(radix))");
        return num;
    }

    /* renamed from: toString-V7xB4Y4  reason: not valid java name */
    public static final String m1333toStringV7xB4Y4(int $this$toString_u2dV7xB4Y4, int radix) {
        String l = Long.toString(((long) $this$toString_u2dV7xB4Y4) & 4294967295L, CharsKt.checkRadix(radix));
        Log1F380D.a((Object) l);
        Intrinsics.checkNotNullExpressionValue(l, "toString(this, checkRadix(radix))");
        return l;
    }

    /* renamed from: toString-olVBNx4  reason: not valid java name */
    public static final String m1334toStringolVBNx4(short $this$toString_u2dolVBNx4, int radix) {
        String num = Integer.toString(65535 & $this$toString_u2dolVBNx4, CharsKt.checkRadix(radix));
        Log1F380D.a((Object) num);
        Intrinsics.checkNotNullExpressionValue(num, "toString(this, checkRadix(radix))");
        return num;
    }
}

package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u0000.\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000\u001a\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0006\u001a\u001b\u0010\u0004\u001a\u0004\u0018\u00010\u0005*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\t\u001a\u0013\u0010\n\u001a\u0004\u0018\u00010\b*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u000b\u001a\u001b\u0010\n\u001a\u0004\u0018\u00010\b*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\f\u001a\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u000f\u001a\u001b\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\u0010\u001a\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u0012*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0013\u001a\u001b\u0010\u0011\u001a\u0004\u0018\u00010\u0012*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\u0014¨\u0006\u0015"}, d2 = {"numberFormatError", "", "input", "", "toByteOrNull", "", "(Ljava/lang/String;)Ljava/lang/Byte;", "radix", "", "(Ljava/lang/String;I)Ljava/lang/Byte;", "toIntOrNull", "(Ljava/lang/String;)Ljava/lang/Integer;", "(Ljava/lang/String;I)Ljava/lang/Integer;", "toLongOrNull", "", "(Ljava/lang/String;)Ljava/lang/Long;", "(Ljava/lang/String;I)Ljava/lang/Long;", "toShortOrNull", "", "(Ljava/lang/String;)Ljava/lang/Short;", "(Ljava/lang/String;I)Ljava/lang/Short;", "kotlin-stdlib"}, k = 5, mv = {1, 7, 1}, xi = 49, xs = "kotlin/text/StringsKt")
/* compiled from: StringNumberConversions.kt */
class StringsKt__StringNumberConversionsKt extends StringsKt__StringNumberConversionsJVMKt {
    public static final Void numberFormatError(String input) {
        Intrinsics.checkNotNullParameter(input, "input");
        throw new NumberFormatException("Invalid number format: '" + input + '\'');
    }

    public static final Byte toByteOrNull(String $this$toByteOrNull) {
        Intrinsics.checkNotNullParameter($this$toByteOrNull, "<this>");
        return StringsKt.toByteOrNull($this$toByteOrNull, 10);
    }

    public static final Byte toByteOrNull(String $this$toByteOrNull, int radix) {
        int intValue;
        Intrinsics.checkNotNullParameter($this$toByteOrNull, "<this>");
        Integer intOrNull = StringsKt.toIntOrNull($this$toByteOrNull, radix);
        if (intOrNull == null || (intValue = intOrNull.intValue()) < -128 || intValue > 127) {
            return null;
        }
        return Byte.valueOf((byte) intValue);
    }

    public static final Integer toIntOrNull(String $this$toIntOrNull) {
        Intrinsics.checkNotNullParameter($this$toIntOrNull, "<this>");
        return StringsKt.toIntOrNull($this$toIntOrNull, 10);
    }

    public static final Integer toIntOrNull(String $this$toIntOrNull, int radix) {
        int i;
        boolean z;
        int i2;
        int i3;
        Intrinsics.checkNotNullParameter($this$toIntOrNull, "<this>");
        CharsKt.checkRadix(radix);
        int length = $this$toIntOrNull.length();
        if (length == 0) {
            return null;
        }
        char charAt = $this$toIntOrNull.charAt(0);
        if (Intrinsics.compare((int) charAt, 48) >= 0) {
            i2 = 0;
            z = false;
            i = -2147483647;
        } else if (length == 1) {
            return null;
        } else {
            i2 = 1;
            if (charAt == '-') {
                z = true;
                i = Integer.MIN_VALUE;
            } else if (charAt != '+') {
                return null;
            } else {
                z = false;
                i = -2147483647;
            }
        }
        int i4 = -59652323;
        int i5 = 0;
        for (int i6 = i2; i6 < length; i6++) {
            int digitOf = CharsKt.digitOf($this$toIntOrNull.charAt(i6), radix);
            if (digitOf < 0) {
                return null;
            }
            if ((i5 < i4 && (i4 != -59652323 || i5 < (i4 = i / radix))) || (i3 = i5 * radix) < i + digitOf) {
                return null;
            }
            i5 = i3 - digitOf;
        }
        return z ? Integer.valueOf(i5) : Integer.valueOf(-i5);
    }

    public static final Long toLongOrNull(String $this$toLongOrNull) {
        Intrinsics.checkNotNullParameter($this$toLongOrNull, "<this>");
        return StringsKt.toLongOrNull($this$toLongOrNull, 10);
    }

    public static final Long toLongOrNull(String $this$toLongOrNull, int radix) {
        long j;
        boolean z;
        int i;
        long j2;
        char c;
        String str = $this$toLongOrNull;
        int i2 = radix;
        Intrinsics.checkNotNullParameter(str, "<this>");
        CharsKt.checkRadix(radix);
        int length = $this$toLongOrNull.length();
        if (length == 0) {
            return null;
        }
        char charAt = str.charAt(0);
        if (Intrinsics.compare((int) charAt, 48) >= 0) {
            i = 0;
            z = false;
            j = -9223372036854775807L;
        } else if (length == 1) {
            return null;
        } else {
            i = 1;
            if (charAt == '-') {
                z = true;
                j = Long.MIN_VALUE;
            } else if (charAt != '+') {
                return null;
            } else {
                z = false;
                j = -9223372036854775807L;
            }
        }
        long j3 = -256204778801521550L;
        long j4 = -256204778801521550L;
        long j5 = 0;
        int i3 = i;
        while (i3 < length) {
            int digitOf = CharsKt.digitOf(str.charAt(i3), i2);
            if (digitOf < 0) {
                return null;
            }
            if (j5 >= j4) {
                c = charAt;
                j2 = j3;
            } else if (j4 != j3) {
                return null;
            } else {
                c = charAt;
                j2 = j3;
                long j6 = j / ((long) i2);
                if (j5 < j6) {
                    return null;
                }
                j4 = j6;
            }
            long j7 = j5 * ((long) i2);
            if (j7 < ((long) digitOf) + j) {
                return null;
            }
            j5 = j7 - ((long) digitOf);
            i3++;
            charAt = c;
            j3 = j2;
        }
        char c2 = charAt;
        long j8 = j3;
        return z ? Long.valueOf(j5) : Long.valueOf(-j5);
    }

    public static final Short toShortOrNull(String $this$toShortOrNull) {
        Intrinsics.checkNotNullParameter($this$toShortOrNull, "<this>");
        return StringsKt.toShortOrNull($this$toShortOrNull, 10);
    }

    public static final Short toShortOrNull(String $this$toShortOrNull, int radix) {
        int intValue;
        Intrinsics.checkNotNullParameter($this$toShortOrNull, "<this>");
        Integer intOrNull = StringsKt.toIntOrNull($this$toShortOrNull, radix);
        if (intOrNull == null || (intValue = intOrNull.intValue()) < -32768 || intValue > 32767) {
            return null;
        }
        return Short.valueOf((short) intValue);
    }
}

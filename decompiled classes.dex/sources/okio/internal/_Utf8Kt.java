package okio.internal;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.Unit;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import mt.Log1F380D;
import okio.Utf8;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0012\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002\u001a\u001e\u0010\u0003\u001a\u00020\u0002*\u00020\u00012\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0005¨\u0006\u0007"}, d2 = {"commonAsUtf8ToByteArray", "", "", "commonToUtf8String", "beginIndex", "", "endIndex", "okio"}, k = 2, mv = {1, 4, 0})
/* compiled from: 01F3 */
public final class _Utf8Kt {
    public static final byte[] commonAsUtf8ToByteArray(String $this$commonAsUtf8ToByteArray) {
        char charAt;
        Intrinsics.checkNotNullParameter($this$commonAsUtf8ToByteArray, "$this$commonAsUtf8ToByteArray");
        byte[] bArr = new byte[($this$commonAsUtf8ToByteArray.length() * 4)];
        int length = $this$commonAsUtf8ToByteArray.length();
        for (int i = 0; i < length; i++) {
            char charAt2 = $this$commonAsUtf8ToByteArray.charAt(i);
            if (Intrinsics.compare((int) charAt2, 128) >= 0) {
                int i2 = i;
                int length2 = $this$commonAsUtf8ToByteArray.length();
                String str = $this$commonAsUtf8ToByteArray;
                int i3 = i;
                while (i3 < length2) {
                    char charAt3 = str.charAt(i3);
                    if (Intrinsics.compare((int) charAt3, 128) < 0) {
                        int i4 = i2 + 1;
                        bArr[i2] = (byte) charAt3;
                        i3++;
                        while (i3 < length2 && Intrinsics.compare((int) str.charAt(i3), 128) < 0) {
                            bArr[i4] = (byte) str.charAt(i3);
                            i3++;
                            i4++;
                        }
                        i2 = i4;
                    } else if (Intrinsics.compare((int) charAt3, 2048) < 0) {
                        int i5 = i2 + 1;
                        bArr[i2] = (byte) ((charAt3 >> 6) | 192);
                        bArr[i5] = (byte) ((charAt3 & '?') | 128);
                        i3++;
                        i2 = i5 + 1;
                    } else if (55296 > charAt3 || 57343 < charAt3) {
                        int i6 = i2 + 1;
                        bArr[i2] = (byte) ((charAt3 >> 12) | 224);
                        int i7 = i6 + 1;
                        bArr[i6] = (byte) (((charAt3 >> 6) & 63) | 128);
                        bArr[i7] = (byte) ((charAt3 & '?') | 128);
                        i3++;
                        i2 = i7 + 1;
                    } else if (Intrinsics.compare((int) charAt3, 56319) > 0 || length2 <= i3 + 1 || 56320 > (charAt = str.charAt(i3 + 1)) || 57343 < charAt) {
                        bArr[i2] = Utf8.REPLACEMENT_BYTE;
                        i3++;
                        i2++;
                    } else {
                        int charAt4 = ((charAt3 << 10) + str.charAt(i3 + 1)) - 56613888;
                        int i8 = i2 + 1;
                        bArr[i2] = (byte) ((charAt4 >> 18) | 240);
                        int i9 = i8 + 1;
                        bArr[i8] = (byte) (((charAt4 >> 12) & 63) | 128);
                        int i10 = i9 + 1;
                        bArr[i9] = (byte) (((charAt4 >> 6) & 63) | 128);
                        bArr[i10] = (byte) ((charAt4 & 63) | 128);
                        i3 += 2;
                        i2 = i10 + 1;
                    }
                }
                byte[] copyOf = Arrays.copyOf(bArr, i2);
                Intrinsics.checkNotNullExpressionValue(copyOf, "java.util.Arrays.copyOf(this, newSize)");
                return copyOf;
            }
            bArr[i] = (byte) charAt2;
        }
        byte[] copyOf2 = Arrays.copyOf(bArr, $this$commonAsUtf8ToByteArray.length());
        Intrinsics.checkNotNullExpressionValue(copyOf2, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf2;
    }

    public static final String commonToUtf8String(byte[] $this$commonToUtf8String, int beginIndex, int endIndex) {
        boolean z;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        byte[] bArr = $this$commonToUtf8String;
        int i14 = beginIndex;
        int i15 = endIndex;
        Intrinsics.checkNotNullParameter(bArr, "$this$commonToUtf8String");
        if (i14 < 0 || i15 > bArr.length || i14 > i15) {
            throw new ArrayIndexOutOfBoundsException("size=" + bArr.length + " beginIndex=" + i14 + " endIndex=" + i15);
        }
        char[] cArr = new char[(i15 - i14)];
        int i16 = 0;
        byte[] bArr2 = $this$commonToUtf8String;
        boolean z2 = false;
        int i17 = beginIndex;
        while (i17 < i15) {
            byte b = bArr2[i17];
            if (b >= 0) {
                int i18 = i16 + 1;
                cArr[i16] = (char) b;
                i17++;
                while (i17 < i15 && bArr2[i17] >= 0) {
                    cArr[i18] = (char) bArr2[i17];
                    i17++;
                    i18++;
                }
                z = z2;
                i16 = i18;
            } else if ((b >> 5) == -2) {
                byte[] bArr3 = bArr2;
                if (i15 <= i17 + 1) {
                    int i19 = i16 + 1;
                    cArr[i16] = (char) Utf8.REPLACEMENT_CODE_POINT;
                    Unit unit = Unit.INSTANCE;
                    i11 = i19;
                    i12 = 1;
                    z = z2;
                } else {
                    byte b2 = bArr3[i17];
                    byte b3 = bArr3[i17 + 1];
                    if (!((b3 & 192) == 128)) {
                        i11 = i16 + 1;
                        cArr[i16] = (char) Utf8.REPLACEMENT_CODE_POINT;
                        Unit unit2 = Unit.INSTANCE;
                        z = z2;
                        i12 = 1;
                    } else {
                        byte b4 = (b3 ^ ByteCompanionObject.MIN_VALUE) ^ (b2 << 6);
                        if (b4 < 128) {
                            z = z2;
                            i13 = i16 + 1;
                            cArr[i16] = (char) Utf8.REPLACEMENT_CODE_POINT;
                        } else {
                            z = z2;
                            i13 = i16 + 1;
                            cArr[i16] = (char) b4;
                        }
                        Unit unit3 = Unit.INSTANCE;
                        i11 = i13;
                        i12 = 2;
                    }
                }
                i17 += i12;
                i16 = i11;
            } else {
                z = z2;
                if ((b >> 4) == -2) {
                    byte[] bArr4 = bArr2;
                    if (i15 <= i17 + 2) {
                        i8 = i16 + 1;
                        cArr[i16] = (char) Utf8.REPLACEMENT_CODE_POINT;
                        Unit unit4 = Unit.INSTANCE;
                        if (i15 > i17 + 1) {
                            if ((192 & bArr4[i17 + 1]) == 128) {
                                i9 = 2;
                            }
                        }
                        i9 = 1;
                    } else {
                        byte b5 = bArr4[i17];
                        byte b6 = bArr4[i17 + 1];
                        if (!((b6 & 192) == 128)) {
                            int i20 = i16 + 1;
                            cArr[i16] = (char) Utf8.REPLACEMENT_CODE_POINT;
                            Unit unit5 = Unit.INSTANCE;
                            i8 = i20;
                            i9 = 1;
                        } else {
                            byte b7 = bArr4[i17 + 2];
                            if (!((b7 & 192) == 128)) {
                                int i21 = i16 + 1;
                                cArr[i16] = (char) Utf8.REPLACEMENT_CODE_POINT;
                                Unit unit6 = Unit.INSTANCE;
                                i8 = i21;
                                i9 = 2;
                            } else {
                                byte b8 = ((-123008 ^ b7) ^ (b6 << 6)) ^ (b5 << 12);
                                if (b8 < 2048) {
                                    i10 = i16 + 1;
                                    cArr[i16] = (char) Utf8.REPLACEMENT_CODE_POINT;
                                } else if (55296 <= b8 && 57343 >= b8) {
                                    i10 = i16 + 1;
                                    cArr[i16] = (char) Utf8.REPLACEMENT_CODE_POINT;
                                } else {
                                    i10 = i16 + 1;
                                    cArr[i16] = (char) b8;
                                }
                                Unit unit7 = Unit.INSTANCE;
                                i8 = i10;
                                i9 = 3;
                            }
                        }
                    }
                    i17 += i9;
                    i16 = i8;
                } else if ((b >> 3) == -2) {
                    byte[] bArr5 = bArr2;
                    if (i15 <= i17 + 3) {
                        if (65533 != 65533) {
                            int i22 = i16 + 1;
                            cArr[i16] = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                            i = i22 + 1;
                            cArr[i22] = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                        } else {
                            cArr[i16] = Utf8.REPLACEMENT_CHARACTER;
                            i = i16 + 1;
                        }
                        Unit unit8 = Unit.INSTANCE;
                        if (i15 > i17 + 1) {
                            if ((192 & bArr5[i17 + 1]) == 128) {
                                if (i15 > i17 + 2) {
                                    if ((192 & bArr5[i17 + 2]) == 128) {
                                        i2 = 3;
                                    }
                                }
                                i2 = 2;
                            }
                        }
                        i2 = 1;
                    } else {
                        byte b9 = bArr5[i17];
                        byte b10 = bArr5[i17 + 1];
                        if (!((b10 & 192) == 128)) {
                            if (65533 != 65533) {
                                int i23 = i16 + 1;
                                cArr[i16] = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                i7 = i23 + 1;
                                cArr[i23] = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                            } else {
                                cArr[i16] = Utf8.REPLACEMENT_CHARACTER;
                                i7 = i16 + 1;
                            }
                            Unit unit9 = Unit.INSTANCE;
                            i2 = 1;
                        } else {
                            byte b11 = bArr5[i17 + 2];
                            if (!((b11 & 192) == 128)) {
                                if (65533 != 65533) {
                                    int i24 = i16 + 1;
                                    cArr[i16] = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                    cArr[i24] = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                    i6 = i24 + 1;
                                } else {
                                    cArr[i16] = Utf8.REPLACEMENT_CHARACTER;
                                    i6 = i16 + 1;
                                }
                                Unit unit10 = Unit.INSTANCE;
                                i2 = 2;
                            } else {
                                byte b12 = bArr5[i17 + 3];
                                if (!((b12 & 192) == 128)) {
                                    if (65533 != 65533) {
                                        int i25 = i16 + 1;
                                        cArr[i16] = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                        i5 = i25 + 1;
                                        cArr[i25] = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                    } else {
                                        cArr[i16] = Utf8.REPLACEMENT_CHARACTER;
                                        i5 = i16 + 1;
                                    }
                                    Unit unit11 = Unit.INSTANCE;
                                    i = i5;
                                    i2 = 3;
                                } else {
                                    byte b13 = (((3678080 ^ b12) ^ (b11 << 6)) ^ (b10 << 12)) ^ (b9 << 18);
                                    if (b13 > 1114111) {
                                        if (65533 != 65533) {
                                            int i26 = i16 + 1;
                                            cArr[i16] = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                            i4 = i26 + 1;
                                            cArr[i26] = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                            Unit unit12 = Unit.INSTANCE;
                                            i2 = 4;
                                            i = i4;
                                        } else {
                                            i3 = i16 + 1;
                                            cArr[i16] = Utf8.REPLACEMENT_CHARACTER;
                                        }
                                    } else if (55296 <= b13 && 57343 >= b13) {
                                        if (65533 != 65533) {
                                            int i27 = i16 + 1;
                                            cArr[i16] = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                            i4 = i27 + 1;
                                            cArr[i27] = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                            Unit unit122 = Unit.INSTANCE;
                                            i2 = 4;
                                            i = i4;
                                        } else {
                                            i3 = i16 + 1;
                                            cArr[i16] = Utf8.REPLACEMENT_CHARACTER;
                                        }
                                    } else if (b13 >= 65536) {
                                        byte b14 = b13;
                                        if (b14 != 65533) {
                                            int i28 = i16 + 1;
                                            cArr[i16] = (char) ((b14 >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                            i4 = i28 + 1;
                                            cArr[i28] = (char) ((b14 & UByte.MAX_VALUE) + Utf8.LOG_SURROGATE_HEADER);
                                            Unit unit1222 = Unit.INSTANCE;
                                            i2 = 4;
                                            i = i4;
                                        } else {
                                            i3 = i16 + 1;
                                            cArr[i16] = Utf8.REPLACEMENT_CHARACTER;
                                        }
                                    } else if (65533 != 65533) {
                                        int i29 = i16 + 1;
                                        cArr[i16] = (char) ((Utf8.REPLACEMENT_CODE_POINT >>> 10) + Utf8.HIGH_SURROGATE_HEADER);
                                        i4 = i29 + 1;
                                        cArr[i29] = (char) ((65533 & 1023) + Utf8.LOG_SURROGATE_HEADER);
                                        Unit unit12222 = Unit.INSTANCE;
                                        i2 = 4;
                                        i = i4;
                                    } else {
                                        i3 = i16 + 1;
                                        cArr[i16] = Utf8.REPLACEMENT_CHARACTER;
                                    }
                                    i4 = i3;
                                    Unit unit122222 = Unit.INSTANCE;
                                    i2 = 4;
                                    i = i4;
                                }
                            }
                        }
                    }
                    i17 += i2;
                    i16 = i;
                } else {
                    cArr[i16] = Utf8.REPLACEMENT_CHARACTER;
                    i17++;
                    i16++;
                }
            }
            z2 = z;
        }
        boolean z3 = z2;
        String str = new String(cArr, 0, i16);
        Log1F380D.a((Object) str);
        return str;
    }

    public static /* synthetic */ String commonToUtf8String$default(byte[] bArr, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = bArr.length;
        }
        String commonToUtf8String = commonToUtf8String(bArr, i, i2);
        Log1F380D.a((Object) commonToUtf8String);
        return commonToUtf8String;
    }
}

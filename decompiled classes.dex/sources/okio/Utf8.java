package okio;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\u001a\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0001H\b\u001a\u0011\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0007H\b\u001a4\u0010\u0010\u001a\u00020\u0001*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\bø\u0001\u0000\u001a4\u0010\u0017\u001a\u00020\u0001*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\bø\u0001\u0000\u001a4\u0010\u0018\u001a\u00020\u0001*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\bø\u0001\u0000\u001a4\u0010\u0019\u001a\u00020\u0016*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u00160\u0015H\bø\u0001\u0000\u001a4\u0010\u001a\u001a\u00020\u0016*\u00020\u001b2\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00160\u0015H\bø\u0001\u0000\u001a4\u0010\u001c\u001a\u00020\u0016*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u0015H\bø\u0001\u0000\u001a%\u0010\u001d\u001a\u00020\u001e*\u00020\u001b2\b\b\u0002\u0010\u0012\u001a\u00020\u00012\b\b\u0002\u0010\u0013\u001a\u00020\u0001H\u0007¢\u0006\u0002\b\u001f\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\tXT¢\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\u0002\u0007\n\u0005\b20\u0001¨\u0006 "}, d2 = {"HIGH_SURROGATE_HEADER", "", "LOG_SURROGATE_HEADER", "MASK_2BYTES", "MASK_3BYTES", "MASK_4BYTES", "REPLACEMENT_BYTE", "", "REPLACEMENT_CHARACTER", "", "REPLACEMENT_CODE_POINT", "isIsoControl", "", "codePoint", "isUtf8Continuation", "byte", "process2Utf8Bytes", "", "beginIndex", "endIndex", "yield", "Lkotlin/Function1;", "", "process3Utf8Bytes", "process4Utf8Bytes", "processUtf16Chars", "processUtf8Bytes", "", "processUtf8CodePoints", "utf8Size", "", "size", "okio"}, k = 2, mv = {1, 4, 0})
/* compiled from: Utf8.kt */
public final class Utf8 {
    public static final int HIGH_SURROGATE_HEADER = 55232;
    public static final int LOG_SURROGATE_HEADER = 56320;
    public static final int MASK_2BYTES = 3968;
    public static final int MASK_3BYTES = -123008;
    public static final int MASK_4BYTES = 3678080;
    public static final byte REPLACEMENT_BYTE = 63;
    public static final char REPLACEMENT_CHARACTER = '�';
    public static final int REPLACEMENT_CODE_POINT = 65533;

    public static final boolean isIsoControl(int codePoint) {
        return (codePoint >= 0 && 31 >= codePoint) || (127 <= codePoint && 159 >= codePoint);
    }

    public static final boolean isUtf8Continuation(byte b) {
        return (192 & b) == 128;
    }

    public static final int process2Utf8Bytes(byte[] $this$process2Utf8Bytes, int beginIndex, int endIndex, Function1<? super Integer, Unit> yield) {
        Intrinsics.checkNotNullParameter($this$process2Utf8Bytes, "$this$process2Utf8Bytes");
        Intrinsics.checkNotNullParameter(yield, "yield");
        int i = beginIndex + 1;
        Integer valueOf = Integer.valueOf(REPLACEMENT_CODE_POINT);
        if (endIndex <= i) {
            yield.invoke(valueOf);
            return 1;
        }
        byte b = $this$process2Utf8Bytes[beginIndex];
        byte b2 = $this$process2Utf8Bytes[beginIndex + 1];
        if (!((192 & b2) == 128)) {
            yield.invoke(valueOf);
            return 1;
        }
        byte b3 = (b2 ^ ByteCompanionObject.MIN_VALUE) ^ (b << 6);
        if (b3 < 128) {
            yield.invoke(valueOf);
            return 2;
        }
        yield.invoke(Integer.valueOf(b3));
        return 2;
    }

    public static final int process3Utf8Bytes(byte[] $this$process3Utf8Bytes, int beginIndex, int endIndex, Function1<? super Integer, Unit> yield) {
        byte[] bArr = $this$process3Utf8Bytes;
        int i = endIndex;
        Function1<? super Integer, Unit> function1 = yield;
        Intrinsics.checkNotNullParameter(bArr, "$this$process3Utf8Bytes");
        Intrinsics.checkNotNullParameter(function1, "yield");
        int i2 = beginIndex + 2;
        boolean z = false;
        Integer valueOf = Integer.valueOf(REPLACEMENT_CODE_POINT);
        if (i <= i2) {
            function1.invoke(valueOf);
            if (i > beginIndex + 1) {
                if ((192 & bArr[beginIndex + 1]) == 128) {
                    z = true;
                }
                return !z ? 1 : 2;
            }
        }
        byte b = bArr[beginIndex];
        byte b2 = bArr[beginIndex + 1];
        if (!((192 & b2) == 128)) {
            function1.invoke(valueOf);
            return 1;
        }
        byte b3 = bArr[beginIndex + 2];
        if ((192 & b3) == 128) {
            z = true;
        }
        if (!z) {
            function1.invoke(valueOf);
            return 2;
        }
        byte b4 = ((-123008 ^ b3) ^ (b2 << 6)) ^ (b << 12);
        if (b4 < 2048) {
            function1.invoke(valueOf);
            return 3;
        } else if (55296 <= b4 && 57343 >= b4) {
            function1.invoke(valueOf);
            return 3;
        } else {
            function1.invoke(Integer.valueOf(b4));
            return 3;
        }
    }

    public static final int process4Utf8Bytes(byte[] $this$process4Utf8Bytes, int beginIndex, int endIndex, Function1<? super Integer, Unit> yield) {
        byte[] bArr = $this$process4Utf8Bytes;
        int i = endIndex;
        Function1<? super Integer, Unit> function1 = yield;
        Intrinsics.checkNotNullParameter(bArr, "$this$process4Utf8Bytes");
        Intrinsics.checkNotNullParameter(function1, "yield");
        int i2 = beginIndex + 3;
        boolean z = false;
        Integer valueOf = Integer.valueOf(REPLACEMENT_CODE_POINT);
        if (i <= i2) {
            function1.invoke(valueOf);
            if (i > beginIndex + 1) {
                if ((192 & bArr[beginIndex + 1]) == 128) {
                    if (i > beginIndex + 2) {
                        if ((192 & bArr[beginIndex + 2]) == 128) {
                            z = true;
                        }
                        return !z ? 2 : 3;
                    }
                }
            }
            return 1;
        }
        byte b = bArr[beginIndex];
        byte b2 = bArr[beginIndex + 1];
        if (!((192 & b2) == 128)) {
            function1.invoke(valueOf);
            return 1;
        }
        byte b3 = bArr[beginIndex + 2];
        if (!((192 & b3) == 128)) {
            function1.invoke(valueOf);
            return 2;
        }
        byte b4 = bArr[beginIndex + 3];
        if ((192 & b4) == 128) {
            z = true;
        }
        if (!z) {
            function1.invoke(valueOf);
            return 3;
        }
        byte b5 = (((3678080 ^ b4) ^ (b3 << 6)) ^ (b2 << 12)) ^ (b << 18);
        if (b5 > 1114111) {
            function1.invoke(valueOf);
            return 4;
        } else if (55296 <= b5 && 57343 >= b5) {
            function1.invoke(valueOf);
            return 4;
        } else if (b5 < 65536) {
            function1.invoke(valueOf);
            return 4;
        } else {
            function1.invoke(Integer.valueOf(b5));
            return 4;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:119:0x02bb, code lost:
        if (65533(0xfffd, float:9.1831E-41) != 65533(0xfffd, float:9.1831E-41)) goto L_0x02bd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:121:0x02d5, code lost:
        r2.invoke(java.lang.Character.valueOf(REPLACEMENT_CHARACTER));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:127:0x02ec, code lost:
        if (65533(0xfffd, float:9.1831E-41) != 65533(0xfffd, float:9.1831E-41)) goto L_0x02bd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:131:0x02f7, code lost:
        if (65533(0xfffd, float:9.1831E-41) != 65533(0xfffd, float:9.1831E-41)) goto L_0x02bd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:133:0x02fc, code lost:
        if (r12 != 65533) goto L_0x02bd;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void processUtf16Chars(byte[] r24, int r25, int r26, kotlin.jvm.functions.Function1<? super java.lang.Character, kotlin.Unit> r27) {
        /*
            r0 = r24
            r1 = r26
            r2 = r27
            r3 = 0
            java.lang.String r4 = "$this$processUtf16Chars"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r0, r4)
            java.lang.String r4 = "yield"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r4)
            r4 = r25
        L_0x0014:
            if (r4 >= r1) goto L_0x0310
            byte r5 = r0[r4]
            if (r5 < 0) goto L_0x0039
            char r6 = (char) r5
            java.lang.Character r6 = java.lang.Character.valueOf(r6)
            r2.invoke(r6)
            int r4 = r4 + 1
        L_0x0025:
            if (r4 >= r1) goto L_0x030d
            byte r6 = r0[r4]
            if (r6 < 0) goto L_0x030d
            int r6 = r4 + 1
            byte r4 = r0[r4]
            char r4 = (char) r4
            java.lang.Character r4 = java.lang.Character.valueOf(r4)
            r2.invoke(r4)
            r4 = r6
            goto L_0x0025
        L_0x0039:
            r6 = 5
            r7 = r5
            r8 = 0
            int r6 = r7 >> r6
            r7 = -2
            r9 = 128(0x80, float:1.794E-43)
            if (r6 != r7) goto L_0x00a2
            r6 = r24
            r7 = 0
            int r12 = r4 + 1
            if (r1 > r12) goto L_0x005a
            r8 = 65533(0xfffd, float:9.1831E-41)
            r9 = 0
            char r10 = (char) r8
            java.lang.Character r10 = java.lang.Character.valueOf(r10)
            r2.invoke(r10)
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            r10 = 1
            goto L_0x009f
        L_0x005a:
            byte r12 = r6[r4]
            int r13 = r4 + 1
            byte r13 = r6[r13]
            r14 = 0
            r15 = 192(0xc0, float:2.69E-43)
            r16 = r13
            r17 = 0
            r15 = r16 & r15
            if (r15 != r9) goto L_0x006d
            r8 = 1
            goto L_0x006e
        L_0x006d:
            r8 = 0
        L_0x006e:
            if (r8 != 0) goto L_0x0081
            r8 = 65533(0xfffd, float:9.1831E-41)
            r9 = 0
            char r10 = (char) r8
            java.lang.Character r10 = java.lang.Character.valueOf(r10)
            r2.invoke(r10)
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            r10 = 1
            goto L_0x009f
        L_0x0081:
            r8 = r13 ^ 3968(0xf80, float:5.56E-42)
            int r11 = r12 << 6
            r8 = r8 ^ r11
            if (r8 >= r9) goto L_0x0091
            r9 = 65533(0xfffd, float:9.1831E-41)
            r11 = 0
            goto L_0x0093
        L_0x0091:
            r9 = r8
            r11 = 0
        L_0x0093:
            char r14 = (char) r9
            java.lang.Character r14 = java.lang.Character.valueOf(r14)
            r2.invoke(r14)
            kotlin.Unit r9 = kotlin.Unit.INSTANCE
            r10 = 2
        L_0x009f:
            int r4 = r4 + r10
            goto L_0x030d
        L_0x00a2:
            r6 = 4
            r12 = r5
            r13 = 0
            int r6 = r12 >> r6
            r12 = 55296(0xd800, float:7.7486E-41)
            r13 = 57343(0xdfff, float:8.0355E-41)
            if (r6 != r7) goto L_0x0164
            r6 = r24
            r7 = 0
            int r15 = r4 + 2
            if (r1 > r15) goto L_0x00e2
            r12 = 65533(0xfffd, float:9.1831E-41)
            r13 = 0
            char r14 = (char) r12
            java.lang.Character r14 = java.lang.Character.valueOf(r14)
            r2.invoke(r14)
            kotlin.Unit r12 = kotlin.Unit.INSTANCE
            int r12 = r4 + 1
            if (r1 <= r12) goto L_0x00df
            int r12 = r4 + 1
            byte r12 = r6[r12]
            r13 = 0
            r14 = 192(0xc0, float:2.69E-43)
            r15 = r12
            r16 = 0
            r14 = r14 & r15
            if (r14 != r9) goto L_0x00d7
            r8 = 1
            goto L_0x00d8
        L_0x00d7:
            r8 = 0
        L_0x00d8:
            if (r8 != 0) goto L_0x00dc
            goto L_0x00df
        L_0x00dc:
            r10 = 2
            goto L_0x0161
        L_0x00df:
            r10 = 1
            goto L_0x0161
        L_0x00e2:
            byte r15 = r6[r4]
            int r16 = r4 + 1
            byte r16 = r6[r16]
            r17 = 0
            r18 = 192(0xc0, float:2.69E-43)
            r19 = r16
            r20 = 0
            r8 = r19 & r18
            if (r8 != r9) goto L_0x00f6
            r8 = 1
            goto L_0x00f7
        L_0x00f6:
            r8 = 0
        L_0x00f7:
            if (r8 != 0) goto L_0x010a
            r8 = 65533(0xfffd, float:9.1831E-41)
            r9 = 0
            char r10 = (char) r8
            java.lang.Character r10 = java.lang.Character.valueOf(r10)
            r2.invoke(r10)
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            r10 = 1
            goto L_0x0161
        L_0x010a:
            int r8 = r4 + 2
            byte r8 = r6[r8]
            r17 = 0
            r18 = 192(0xc0, float:2.69E-43)
            r19 = r8
            r20 = 0
            r10 = r19 & r18
            if (r10 != r9) goto L_0x011d
            r21 = 1
            goto L_0x011f
        L_0x011d:
            r21 = 0
        L_0x011f:
            if (r21 != 0) goto L_0x0132
            r9 = 65533(0xfffd, float:9.1831E-41)
            r10 = 0
            char r11 = (char) r9
            java.lang.Character r11 = java.lang.Character.valueOf(r11)
            r2.invoke(r11)
            kotlin.Unit r9 = kotlin.Unit.INSTANCE
            r10 = 2
            goto L_0x0161
        L_0x0132:
            r9 = -123008(0xfffffffffffe1f80, float:NaN)
            r9 = r9 ^ r8
            int r10 = r16 << 6
            r9 = r9 ^ r10
            int r10 = r15 << 12
            r9 = r9 ^ r10
            r10 = 2048(0x800, float:2.87E-42)
            if (r9 >= r10) goto L_0x0152
            r10 = 65533(0xfffd, float:9.1831E-41)
            r11 = 0
        L_0x0147:
            char r12 = (char) r10
            java.lang.Character r12 = java.lang.Character.valueOf(r12)
            r2.invoke(r12)
            kotlin.Unit r10 = kotlin.Unit.INSTANCE
            goto L_0x015f
        L_0x0152:
            if (r12 <= r9) goto L_0x0155
            goto L_0x015c
        L_0x0155:
            if (r13 < r9) goto L_0x015c
            r10 = 65533(0xfffd, float:9.1831E-41)
            r11 = 0
            goto L_0x0147
        L_0x015c:
            r10 = r9
            r11 = 0
            goto L_0x0147
        L_0x015f:
            r10 = 3
        L_0x0161:
            int r4 = r4 + r10
            goto L_0x030d
        L_0x0164:
            r6 = 3
            r8 = r5
            r10 = 0
            int r6 = r8 >> r6
            r8 = 65533(0xfffd, float:9.1831E-41)
            if (r6 != r7) goto L_0x0304
            r6 = r24
            r7 = 0
            int r10 = r4 + 3
            r15 = 56320(0xdc00, float:7.8921E-41)
            r16 = 55232(0xd7c0, float:7.7397E-41)
            if (r1 > r10) goto L_0x01dc
            r10 = 65533(0xfffd, float:9.1831E-41)
            r12 = 0
            if (r10 == r8) goto L_0x0199
            int r8 = r10 >>> 10
            int r8 = r8 + r16
            char r8 = (char) r8
            java.lang.Character r8 = java.lang.Character.valueOf(r8)
            r2.invoke(r8)
            r8 = r10 & 1023(0x3ff, float:1.434E-42)
            int r8 = r8 + r15
            char r8 = (char) r8
            java.lang.Character r8 = java.lang.Character.valueOf(r8)
            r2.invoke(r8)
            goto L_0x01a0
        L_0x0199:
            java.lang.Character r8 = java.lang.Character.valueOf(r8)
            r2.invoke(r8)
        L_0x01a0:
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            int r8 = r4 + 1
            if (r1 <= r8) goto L_0x01d9
            int r8 = r4 + 1
            byte r8 = r6[r8]
            r10 = 0
            r12 = 192(0xc0, float:2.69E-43)
            r13 = r8
            r15 = 0
            r12 = r12 & r13
            if (r12 != r9) goto L_0x01b5
            r12 = 1
            goto L_0x01b6
        L_0x01b5:
            r12 = 0
        L_0x01b6:
            if (r12 != 0) goto L_0x01ba
            goto L_0x01d9
        L_0x01ba:
            int r8 = r4 + 2
            if (r1 <= r8) goto L_0x01d6
            int r8 = r4 + 2
            byte r8 = r6[r8]
            r10 = 0
            r12 = 192(0xc0, float:2.69E-43)
            r13 = r8
            r15 = 0
            r12 = r12 & r13
            if (r12 != r9) goto L_0x01cd
            r21 = 1
            goto L_0x01cf
        L_0x01cd:
            r21 = 0
        L_0x01cf:
            if (r21 != 0) goto L_0x01d3
            goto L_0x01d6
        L_0x01d3:
            r10 = 3
            goto L_0x0302
        L_0x01d6:
            r10 = 2
            goto L_0x0302
        L_0x01d9:
            r10 = 1
            goto L_0x0302
        L_0x01dc:
            byte r10 = r6[r4]
            int r17 = r4 + 1
            byte r17 = r6[r17]
            r18 = 0
            r19 = 192(0xc0, float:2.69E-43)
            r20 = r17
            r23 = 0
            r11 = r20 & r19
            if (r11 != r9) goto L_0x01f0
            r11 = 1
            goto L_0x01f1
        L_0x01f0:
            r11 = 0
        L_0x01f1:
            if (r11 != 0) goto L_0x021f
            r9 = 65533(0xfffd, float:9.1831E-41)
            r11 = 0
            if (r9 == r8) goto L_0x0212
            int r8 = r9 >>> 10
            int r8 = r8 + r16
            char r8 = (char) r8
            java.lang.Character r8 = java.lang.Character.valueOf(r8)
            r2.invoke(r8)
            r8 = r9 & 1023(0x3ff, float:1.434E-42)
            int r8 = r8 + r15
            char r8 = (char) r8
            java.lang.Character r8 = java.lang.Character.valueOf(r8)
            r2.invoke(r8)
            goto L_0x0219
        L_0x0212:
            java.lang.Character r8 = java.lang.Character.valueOf(r8)
            r2.invoke(r8)
        L_0x0219:
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            r10 = 1
            goto L_0x0302
        L_0x021f:
            int r11 = r4 + 2
            byte r11 = r6[r11]
            r18 = 0
            r19 = 192(0xc0, float:2.69E-43)
            r20 = r11
            r23 = 0
            r14 = r20 & r19
            if (r14 != r9) goto L_0x0231
            r14 = 1
            goto L_0x0232
        L_0x0231:
            r14 = 0
        L_0x0232:
            if (r14 != 0) goto L_0x0260
            r9 = 65533(0xfffd, float:9.1831E-41)
            r12 = 0
            if (r9 == r8) goto L_0x0253
            int r8 = r9 >>> 10
            int r8 = r8 + r16
            char r8 = (char) r8
            java.lang.Character r8 = java.lang.Character.valueOf(r8)
            r2.invoke(r8)
            r8 = r9 & 1023(0x3ff, float:1.434E-42)
            int r8 = r8 + r15
            char r8 = (char) r8
            java.lang.Character r8 = java.lang.Character.valueOf(r8)
            r2.invoke(r8)
            goto L_0x025a
        L_0x0253:
            java.lang.Character r8 = java.lang.Character.valueOf(r8)
            r2.invoke(r8)
        L_0x025a:
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            r10 = 2
            goto L_0x0302
        L_0x0260:
            int r14 = r4 + 3
            byte r14 = r6[r14]
            r18 = 0
            r19 = 192(0xc0, float:2.69E-43)
            r20 = r14
            r22 = 0
            r13 = r20 & r19
            if (r13 != r9) goto L_0x0273
            r21 = 1
            goto L_0x0275
        L_0x0273:
            r21 = 0
        L_0x0275:
            if (r21 != 0) goto L_0x02a2
            r9 = 65533(0xfffd, float:9.1831E-41)
            r12 = 0
            if (r9 == r8) goto L_0x0296
            int r8 = r9 >>> 10
            int r8 = r8 + r16
            char r8 = (char) r8
            java.lang.Character r8 = java.lang.Character.valueOf(r8)
            r2.invoke(r8)
            r8 = r9 & 1023(0x3ff, float:1.434E-42)
            int r8 = r8 + r15
            char r8 = (char) r8
            java.lang.Character r8 = java.lang.Character.valueOf(r8)
            r2.invoke(r8)
            goto L_0x029d
        L_0x0296:
            java.lang.Character r8 = java.lang.Character.valueOf(r8)
            r2.invoke(r8)
        L_0x029d:
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            r10 = 3
            goto L_0x0302
        L_0x02a2:
            r9 = 3678080(0x381f80, float:5.154088E-39)
            r9 = r9 ^ r14
            int r13 = r11 << 6
            r9 = r9 ^ r13
            int r13 = r17 << 12
            r9 = r9 ^ r13
            int r13 = r10 << 18
            r9 = r9 ^ r13
            r13 = 1114111(0x10ffff, float:1.561202E-39)
            if (r9 <= r13) goto L_0x02e0
            r12 = 65533(0xfffd, float:9.1831E-41)
            r13 = 0
            if (r12 == r8) goto L_0x02d5
        L_0x02bd:
            int r8 = r12 >>> 10
            int r8 = r8 + r16
            char r8 = (char) r8
            java.lang.Character r8 = java.lang.Character.valueOf(r8)
            r2.invoke(r8)
            r8 = r12 & 1023(0x3ff, float:1.434E-42)
            int r8 = r8 + r15
            char r8 = (char) r8
            java.lang.Character r8 = java.lang.Character.valueOf(r8)
            r2.invoke(r8)
            goto L_0x02dc
        L_0x02d5:
            java.lang.Character r8 = java.lang.Character.valueOf(r8)
            r2.invoke(r8)
        L_0x02dc:
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            goto L_0x02ff
        L_0x02e0:
            if (r12 <= r9) goto L_0x02e3
            goto L_0x02ef
        L_0x02e3:
            r12 = 57343(0xdfff, float:8.0355E-41)
            if (r12 < r9) goto L_0x02ef
            r12 = 65533(0xfffd, float:9.1831E-41)
            r13 = 0
            if (r12 == r8) goto L_0x02d5
            goto L_0x02bd
        L_0x02ef:
            r12 = 65536(0x10000, float:9.18355E-41)
            if (r9 >= r12) goto L_0x02fa
            r12 = 65533(0xfffd, float:9.1831E-41)
            r13 = 0
            if (r12 == r8) goto L_0x02d5
            goto L_0x02bd
        L_0x02fa:
            r12 = r9
            r13 = 0
            if (r12 == r8) goto L_0x02d5
            goto L_0x02bd
        L_0x02ff:
            r8 = 4
            r10 = r8
        L_0x0302:
            int r4 = r4 + r10
            goto L_0x030d
        L_0x0304:
            java.lang.Character r6 = java.lang.Character.valueOf(r8)
            r2.invoke(r6)
            int r4 = r4 + 1
        L_0x030d:
            goto L_0x0014
        L_0x0310:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Utf8.processUtf16Chars(byte[], int, int, kotlin.jvm.functions.Function1):void");
    }

    public static final void processUtf8Bytes(String $this$processUtf8Bytes, int beginIndex, int endIndex, Function1<? super Byte, Unit> yield) {
        char charAt;
        Intrinsics.checkNotNullParameter($this$processUtf8Bytes, "$this$processUtf8Bytes");
        Intrinsics.checkNotNullParameter(yield, "yield");
        int i = beginIndex;
        while (i < endIndex) {
            char charAt2 = $this$processUtf8Bytes.charAt(i);
            if (Intrinsics.compare((int) charAt2, 128) < 0) {
                yield.invoke(Byte.valueOf((byte) charAt2));
                i++;
                while (i < endIndex && Intrinsics.compare((int) $this$processUtf8Bytes.charAt(i), 128) < 0) {
                    yield.invoke(Byte.valueOf((byte) $this$processUtf8Bytes.charAt(i)));
                    i++;
                }
            } else if (Intrinsics.compare((int) charAt2, 2048) < 0) {
                yield.invoke(Byte.valueOf((byte) ((charAt2 >> 6) | 192)));
                yield.invoke(Byte.valueOf((byte) (128 | (charAt2 & '?'))));
                i++;
            } else if (55296 > charAt2 || 57343 < charAt2) {
                yield.invoke(Byte.valueOf((byte) ((charAt2 >> 12) | 224)));
                yield.invoke(Byte.valueOf((byte) (((charAt2 >> 6) & 63) | 128)));
                yield.invoke(Byte.valueOf((byte) (128 | (charAt2 & '?'))));
                i++;
            } else if (Intrinsics.compare((int) charAt2, 56319) > 0 || endIndex <= i + 1 || 56320 > (charAt = $this$processUtf8Bytes.charAt(i + 1)) || 57343 < charAt) {
                yield.invoke(Byte.valueOf(REPLACEMENT_BYTE));
                i++;
            } else {
                int charAt3 = ((charAt2 << 10) + $this$processUtf8Bytes.charAt(i + 1)) - 56613888;
                yield.invoke(Byte.valueOf((byte) ((charAt3 >> 18) | 240)));
                yield.invoke(Byte.valueOf((byte) (((charAt3 >> 12) & 63) | 128)));
                yield.invoke(Byte.valueOf((byte) ((63 & (charAt3 >> 6)) | 128)));
                yield.invoke(Byte.valueOf((byte) (128 | (charAt3 & 63))));
                i += 2;
            }
        }
    }

    public static final void processUtf8CodePoints(byte[] $this$processUtf8CodePoints, int beginIndex, int endIndex, Function1<? super Integer, Unit> yield) {
        int i;
        int i2;
        int i3;
        byte[] bArr = $this$processUtf8CodePoints;
        int i4 = endIndex;
        Function1<? super Integer, Unit> function1 = yield;
        Intrinsics.checkNotNullParameter(bArr, "$this$processUtf8CodePoints");
        Intrinsics.checkNotNullParameter(function1, "yield");
        int i5 = beginIndex;
        while (i5 < i4) {
            byte b = bArr[i5];
            if (b >= 0) {
                function1.invoke(Integer.valueOf(b));
                i5++;
                while (i5 < i4 && bArr[i5] >= 0) {
                    function1.invoke(Integer.valueOf(bArr[i5]));
                    i5++;
                }
            } else if ((b >> 5) == -2) {
                byte[] bArr2 = $this$processUtf8CodePoints;
                if (i4 <= i5 + 1) {
                    function1.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                    Unit unit = Unit.INSTANCE;
                    i3 = 1;
                } else {
                    byte b2 = bArr2[i5];
                    byte b3 = bArr2[i5 + 1];
                    if (!((b3 & 192) == 128)) {
                        function1.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                        Unit unit2 = Unit.INSTANCE;
                        i3 = 1;
                    } else {
                        byte b4 = (b3 ^ ByteCompanionObject.MIN_VALUE) ^ (b2 << 6);
                        function1.invoke(Integer.valueOf(b4 < 128 ? 65533 : b4));
                        Unit unit3 = Unit.INSTANCE;
                        i3 = 2;
                    }
                }
                i5 += i3;
            } else if ((b >> 4) == -2) {
                byte[] bArr3 = $this$processUtf8CodePoints;
                if (i4 <= i5 + 2) {
                    function1.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                    Unit unit4 = Unit.INSTANCE;
                    if (i4 > i5 + 1) {
                        if ((192 & bArr3[i5 + 1]) == 128) {
                            i2 = 2;
                        }
                    }
                    i2 = 1;
                } else {
                    byte b5 = bArr3[i5];
                    byte b6 = bArr3[i5 + 1];
                    if (!((b6 & 192) == 128)) {
                        function1.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                        Unit unit5 = Unit.INSTANCE;
                        i2 = 1;
                    } else {
                        byte b7 = bArr3[i5 + 2];
                        if (!((b7 & 192) == 128)) {
                            function1.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                            Unit unit6 = Unit.INSTANCE;
                            i2 = 2;
                        } else {
                            byte b8 = ((-123008 ^ b7) ^ (b6 << 6)) ^ (b5 << 12);
                            function1.invoke(Integer.valueOf(b8 < 2048 ? 65533 : (55296 <= b8 && 57343 >= b8) ? 65533 : b8));
                            Unit unit7 = Unit.INSTANCE;
                            i2 = 3;
                        }
                    }
                }
                i5 += i2;
            } else if ((b >> 3) == -2) {
                byte[] bArr4 = $this$processUtf8CodePoints;
                if (i4 <= i5 + 3) {
                    function1.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                    Unit unit8 = Unit.INSTANCE;
                    if (i4 > i5 + 1) {
                        if ((192 & bArr4[i5 + 1]) == 128) {
                            if (i4 > i5 + 2) {
                                if ((192 & bArr4[i5 + 2]) == 128) {
                                    i = 3;
                                }
                            }
                            i = 2;
                        }
                    }
                    i = 1;
                } else {
                    byte b9 = bArr4[i5];
                    byte b10 = bArr4[i5 + 1];
                    if (!((b10 & 192) == 128)) {
                        function1.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                        Unit unit9 = Unit.INSTANCE;
                        i = 1;
                    } else {
                        byte b11 = bArr4[i5 + 2];
                        if (!((b11 & 192) == 128)) {
                            function1.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                            Unit unit10 = Unit.INSTANCE;
                            i = 2;
                        } else {
                            byte b12 = bArr4[i5 + 3];
                            if (!((b12 & 192) == 128)) {
                                function1.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                                Unit unit11 = Unit.INSTANCE;
                                i = 3;
                            } else {
                                byte b13 = (((3678080 ^ b12) ^ (b11 << 6)) ^ (b10 << 12)) ^ (b9 << 18);
                                function1.invoke(Integer.valueOf(b13 > 1114111 ? 65533 : (55296 <= b13 && 57343 >= b13) ? 65533 : b13 < 65536 ? 65533 : b13));
                                Unit unit12 = Unit.INSTANCE;
                                i = 4;
                            }
                        }
                    }
                }
                i5 += i;
            } else {
                function1.invoke(Integer.valueOf(REPLACEMENT_CODE_POINT));
                i5++;
            }
        }
    }

    public static final long size(String str) {
        return size$default(str, 0, 0, 3, (Object) null);
    }

    public static final long size(String str, int i) {
        return size$default(str, i, 0, 2, (Object) null);
    }

    public static final long size(String $this$utf8Size, int beginIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$utf8Size, "$this$utf8Size");
        boolean z = true;
        if (beginIndex >= 0) {
            if (endIndex >= beginIndex) {
                if (endIndex > $this$utf8Size.length()) {
                    z = false;
                }
                if (z) {
                    long j = 0;
                    int i = beginIndex;
                    while (i < endIndex) {
                        char charAt = $this$utf8Size.charAt(i);
                        if (charAt < 128) {
                            j++;
                            i++;
                        } else if (charAt < 2048) {
                            j += (long) 2;
                            i++;
                        } else if (charAt < 55296 || charAt > 57343) {
                            j += (long) 3;
                            i++;
                        } else {
                            char charAt2 = i + 1 < endIndex ? $this$utf8Size.charAt(i + 1) : 0;
                            if (charAt > 56319 || charAt2 < 56320 || charAt2 > 57343) {
                                j++;
                                i++;
                            } else {
                                j += (long) 4;
                                i += 2;
                            }
                        }
                    }
                    return j;
                }
                throw new IllegalArgumentException(("endIndex > string.length: " + endIndex + " > " + $this$utf8Size.length()).toString());
            }
            throw new IllegalArgumentException(("endIndex < beginIndex: " + endIndex + " < " + beginIndex).toString());
        }
        throw new IllegalArgumentException(("beginIndex < 0: " + beginIndex).toString());
    }

    public static /* synthetic */ long size$default(String str, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = str.length();
        }
        return size(str, i, i2);
    }
}

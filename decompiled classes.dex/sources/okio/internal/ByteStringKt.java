package okio.internal;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import mt.Log1F380D;
import okio.Base64;
import okio.Buffer;
import okio.ByteString;
import okio.Platform;
import okio.Util;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000P\n\u0000\n\u0002\u0010\u0019\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0018\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0005H\u0002\u001a\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0007H\b\u001a\u0010\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u000eH\u0002\u001a\r\u0010\u000f\u001a\u00020\u0010*\u00020\nH\b\u001a\r\u0010\u0011\u001a\u00020\u0010*\u00020\nH\b\u001a\u0015\u0010\u0012\u001a\u00020\u0005*\u00020\n2\u0006\u0010\u0013\u001a\u00020\nH\b\u001a\u000f\u0010\u0014\u001a\u0004\u0018\u00010\n*\u00020\u0010H\b\u001a\r\u0010\u0015\u001a\u00020\n*\u00020\u0010H\b\u001a\r\u0010\u0016\u001a\u00020\n*\u00020\u0010H\b\u001a\u0015\u0010\u0017\u001a\u00020\u0018*\u00020\n2\u0006\u0010\u0019\u001a\u00020\u0007H\b\u001a\u0015\u0010\u0017\u001a\u00020\u0018*\u00020\n2\u0006\u0010\u0019\u001a\u00020\nH\b\u001a\u0017\u0010\u001a\u001a\u00020\u0018*\u00020\n2\b\u0010\u0013\u001a\u0004\u0018\u00010\u001bH\b\u001a\u0015\u0010\u001c\u001a\u00020\u001d*\u00020\n2\u0006\u0010\u001e\u001a\u00020\u0005H\b\u001a\r\u0010\u001f\u001a\u00020\u0005*\u00020\nH\b\u001a\r\u0010 \u001a\u00020\u0005*\u00020\nH\b\u001a\r\u0010!\u001a\u00020\u0010*\u00020\nH\b\u001a\u001d\u0010\"\u001a\u00020\u0005*\u00020\n2\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010#\u001a\u00020\u0005H\b\u001a\r\u0010$\u001a\u00020\u0007*\u00020\nH\b\u001a\u001d\u0010%\u001a\u00020\u0005*\u00020\n2\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010#\u001a\u00020\u0005H\b\u001a\u001d\u0010%\u001a\u00020\u0005*\u00020\n2\u0006\u0010\u0013\u001a\u00020\n2\u0006\u0010#\u001a\u00020\u0005H\b\u001a-\u0010&\u001a\u00020\u0018*\u00020\n2\u0006\u0010'\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010(\u001a\u00020\u00052\u0006\u0010)\u001a\u00020\u0005H\b\u001a-\u0010&\u001a\u00020\u0018*\u00020\n2\u0006\u0010'\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\n2\u0006\u0010(\u001a\u00020\u00052\u0006\u0010)\u001a\u00020\u0005H\b\u001a\u0015\u0010*\u001a\u00020\u0018*\u00020\n2\u0006\u0010+\u001a\u00020\u0007H\b\u001a\u0015\u0010*\u001a\u00020\u0018*\u00020\n2\u0006\u0010+\u001a\u00020\nH\b\u001a\u001d\u0010,\u001a\u00020\n*\u00020\n2\u0006\u0010-\u001a\u00020\u00052\u0006\u0010.\u001a\u00020\u0005H\b\u001a\r\u0010/\u001a\u00020\n*\u00020\nH\b\u001a\r\u00100\u001a\u00020\n*\u00020\nH\b\u001a\r\u00101\u001a\u00020\u0007*\u00020\nH\b\u001a\u001d\u00102\u001a\u00020\n*\u00020\u00072\u0006\u0010'\u001a\u00020\u00052\u0006\u0010)\u001a\u00020\u0005H\b\u001a\r\u00103\u001a\u00020\u0010*\u00020\nH\b\u001a\r\u00104\u001a\u00020\u0010*\u00020\nH\b\u001a$\u00105\u001a\u000206*\u00020\n2\u0006\u00107\u001a\u0002082\u0006\u0010'\u001a\u00020\u00052\u0006\u0010)\u001a\u00020\u0005H\u0000\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003¨\u00069"}, d2 = {"HEX_DIGIT_CHARS", "", "getHEX_DIGIT_CHARS", "()[C", "codePointIndexToCharIndex", "", "s", "", "codePointCount", "commonOf", "Lokio/ByteString;", "data", "decodeHexDigit", "c", "", "commonBase64", "", "commonBase64Url", "commonCompareTo", "other", "commonDecodeBase64", "commonDecodeHex", "commonEncodeUtf8", "commonEndsWith", "", "suffix", "commonEquals", "", "commonGetByte", "", "pos", "commonGetSize", "commonHashCode", "commonHex", "commonIndexOf", "fromIndex", "commonInternalArray", "commonLastIndexOf", "commonRangeEquals", "offset", "otherOffset", "byteCount", "commonStartsWith", "prefix", "commonSubstring", "beginIndex", "endIndex", "commonToAsciiLowercase", "commonToAsciiUppercase", "commonToByteArray", "commonToByteString", "commonToString", "commonUtf8", "commonWrite", "", "buffer", "Lokio/Buffer;", "okio"}, k = 2, mv = {1, 4, 0})
/* compiled from: 01F1 */
public final class ByteStringKt {
    private static final char[] HEX_DIGIT_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x0185, code lost:
        if (31 < 65533(0xfffd, float:9.1831E-41)) goto L_0x018a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x0191, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x0195;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:120:0x0193, code lost:
        r16 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:138:0x01c8, code lost:
        if (31 < r14) goto L_0x01cd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:143:0x01d4, code lost:
        if (159 < r14) goto L_0x01d8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:144:0x01d6, code lost:
        r16 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:186:0x025b, code lost:
        if (r16 == false) goto L_0x0260;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:203:0x029b, code lost:
        if (31 < 65533(0xfffd, float:9.1831E-41)) goto L_0x02a0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:208:0x02a7, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x02ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:209:0x02a9, code lost:
        r16 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:232:0x02fb, code lost:
        if (31 < 65533(0xfffd, float:9.1831E-41)) goto L_0x0300;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:237:0x0307, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x030b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:238:0x0309, code lost:
        r16 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:258:0x0357, code lost:
        if (31 < 65533(0xfffd, float:9.1831E-41)) goto L_0x035c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:263:0x0363, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x0367;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:264:0x0365, code lost:
        r16 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:287:0x03a7, code lost:
        if (31 < 65533(0xfffd, float:9.1831E-41)) goto L_0x03ac;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:292:0x03b3, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x03b7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:293:0x03b5, code lost:
        r16 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:298:0x03c2, code lost:
        if (65533(0xfffd, float:9.1831E-41) < 65536(0x10000, float:9.18355E-41)) goto L_0x03fd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:309:0x03e0, code lost:
        if (31 < r15) goto L_0x03e5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:314:0x03ec, code lost:
        if (159 < r15) goto L_0x03f0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:315:0x03ee, code lost:
        r16 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:320:0x03fb, code lost:
        if (r15 < 65536) goto L_0x03fd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:383:0x04cc, code lost:
        if (31 < 65533(0xfffd, float:9.1831E-41)) goto L_0x04d1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:388:0x04d8, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x04dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:389:0x04da, code lost:
        r16 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:412:0x052a, code lost:
        if (31 < 65533(0xfffd, float:9.1831E-41)) goto L_0x052f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:417:0x0536, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x053a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:418:0x0538, code lost:
        r16 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:441:0x058b, code lost:
        if (31 < 65533(0xfffd, float:9.1831E-41)) goto L_0x0590;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:446:0x0597, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x059b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:447:0x0599, code lost:
        r16 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:467:0x05ec, code lost:
        if (31 < 65533(0xfffd, float:9.1831E-41)) goto L_0x05f1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:472:0x05f8, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x05fc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:473:0x05fa, code lost:
        r16 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:496:0x063e, code lost:
        if (31 < 65533(0xfffd, float:9.1831E-41)) goto L_0x0643;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:501:0x064a, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x064e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:502:0x064c, code lost:
        r16 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:507:0x0659, code lost:
        if (65533(0xfffd, float:9.1831E-41) < 65536(0x10000, float:9.18355E-41)) goto L_0x065b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:522:0x0682, code lost:
        if (31 < 65533(0xfffd, float:9.1831E-41)) goto L_0x0687;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:527:0x068e, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x0692;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:528:0x0690, code lost:
        r16 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:533:0x069d, code lost:
        if (65533(0xfffd, float:9.1831E-41) < 65536(0x10000, float:9.18355E-41)) goto L_0x065b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:544:0x06bb, code lost:
        if (31 < r15) goto L_0x06c0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:549:0x06c7, code lost:
        if (159 < r15) goto L_0x06cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:550:0x06c9, code lost:
        r16 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:555:0x06d6, code lost:
        if (r15 < 65536) goto L_0x065b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x012d, code lost:
        if (31 < 65533(0xfffd, float:9.1831E-41)) goto L_0x0132;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x0139, code lost:
        if (159(0x9f, float:2.23E-43) < 65533(0xfffd, float:9.1831E-41)) goto L_0x013d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x013b, code lost:
        r16 = true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final int codePointIndexToCharIndex(byte[] r29, int r30) {
        /*
            r0 = r30
            r1 = 0
            r2 = 0
            r3 = 0
            r4 = r29
            int r5 = r4.length
            r6 = r29
            r7 = 0
            r8 = r3
        L_0x000c:
            if (r8 >= r5) goto L_0x0721
            byte r9 = r6[r8]
            r10 = 127(0x7f, float:1.78E-43)
            r11 = 159(0x9f, float:2.23E-43)
            r12 = 31
            r14 = 13
            r13 = 10
            r15 = 65536(0x10000, float:9.18355E-41)
            r16 = 0
            r17 = 2
            r18 = 1
            if (r9 < 0) goto L_0x00a0
            r19 = r9
            r20 = 0
            int r21 = r2 + 1
            if (r2 != r0) goto L_0x002e
            return r1
        L_0x002e:
            r2 = r19
            if (r2 == r13) goto L_0x0048
            if (r2 == r14) goto L_0x0048
            r19 = 0
            if (r2 < 0) goto L_0x003c
            if (r12 >= r2) goto L_0x0041
        L_0x003c:
            if (r10 <= r2) goto L_0x003f
            goto L_0x0044
        L_0x003f:
            if (r11 < r2) goto L_0x0044
        L_0x0041:
            r19 = r18
            goto L_0x0046
        L_0x0044:
            r19 = r16
        L_0x0046:
            if (r19 != 0) goto L_0x004d
        L_0x0048:
            r11 = 65533(0xfffd, float:9.1831E-41)
            if (r2 != r11) goto L_0x004f
        L_0x004d:
            r10 = -1
            return r10
        L_0x004f:
            if (r2 >= r15) goto L_0x0054
            r11 = r18
            goto L_0x0056
        L_0x0054:
            r11 = r17
        L_0x0056:
            int r1 = r1 + r11
            int r8 = r8 + 1
            r2 = r21
        L_0x005c:
            if (r8 >= r5) goto L_0x009c
            byte r11 = r6[r8]
            if (r11 < 0) goto L_0x009c
            int r11 = r8 + 1
            byte r8 = r6[r8]
            r20 = 0
            int r21 = r2 + 1
            if (r2 != r0) goto L_0x006d
            return r1
        L_0x006d:
            if (r8 == r13) goto L_0x0086
            if (r8 == r14) goto L_0x0086
            r2 = 0
            if (r8 < 0) goto L_0x0078
            if (r12 >= r8) goto L_0x007f
        L_0x0078:
            if (r10 <= r8) goto L_0x007b
            goto L_0x0082
        L_0x007b:
            r10 = 159(0x9f, float:2.23E-43)
            if (r10 < r8) goto L_0x0082
        L_0x007f:
            r2 = r18
            goto L_0x0084
        L_0x0082:
            r2 = r16
        L_0x0084:
            if (r2 != 0) goto L_0x008b
        L_0x0086:
            r2 = 65533(0xfffd, float:9.1831E-41)
            if (r8 != r2) goto L_0x008d
        L_0x008b:
            r2 = -1
            return r2
        L_0x008d:
            if (r8 >= r15) goto L_0x0092
            r2 = r18
            goto L_0x0094
        L_0x0092:
            r2 = r17
        L_0x0094:
            int r1 = r1 + r2
            r8 = r11
            r2 = r21
            r10 = 127(0x7f, float:1.78E-43)
            goto L_0x005c
        L_0x009c:
            r26 = r3
            goto L_0x071a
        L_0x00a0:
            r10 = 5
            r11 = r9
            r20 = 0
            int r10 = r11 >> r10
            r11 = -2
            if (r10 != r11) goto L_0x01f7
            r10 = r6
            r11 = 0
            int r15 = r8 + 1
            if (r5 > r15) goto L_0x00ef
            r15 = 65533(0xfffd, float:9.1831E-41)
            r21 = 0
            r22 = r15
            r23 = 0
            int r24 = r2 + 1
            if (r2 != r0) goto L_0x00bd
            return r1
        L_0x00bd:
            r2 = r22
            if (r2 == r13) goto L_0x00d7
            if (r2 == r14) goto L_0x00d7
            r13 = 0
            if (r2 < 0) goto L_0x00ca
            if (r12 >= r2) goto L_0x00d3
        L_0x00ca:
            r12 = 127(0x7f, float:1.78E-43)
            if (r12 <= r2) goto L_0x00cf
            goto L_0x00d5
        L_0x00cf:
            r12 = 159(0x9f, float:2.23E-43)
            if (r12 < r2) goto L_0x00d5
        L_0x00d3:
            r16 = r18
        L_0x00d5:
            if (r16 != 0) goto L_0x00dc
        L_0x00d7:
            r12 = 65533(0xfffd, float:9.1831E-41)
            if (r2 != r12) goto L_0x00de
        L_0x00dc:
            r12 = -1
            return r12
        L_0x00de:
            r12 = 65536(0x10000, float:9.18355E-41)
            if (r2 >= r12) goto L_0x00e4
            r17 = r18
        L_0x00e4:
            int r1 = r1 + r17
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
            r26 = r3
            r17 = r18
            goto L_0x01f1
        L_0x00ef:
            byte r15 = r10[r8]
            int r22 = r8 + 1
            byte r12 = r10[r22]
            r22 = 0
            r24 = 192(0xc0, float:2.69E-43)
            r25 = r12
            r26 = 0
            r14 = r25 & r24
            r13 = 128(0x80, float:1.794E-43)
            if (r14 != r13) goto L_0x0106
            r13 = r18
            goto L_0x0108
        L_0x0106:
            r13 = r16
        L_0x0108:
            if (r13 != 0) goto L_0x0157
            r13 = 65533(0xfffd, float:9.1831E-41)
            r14 = 0
            r21 = r13
            r22 = 0
            int r25 = r2 + 1
            if (r2 != r0) goto L_0x0118
            return r1
        L_0x0118:
            r26 = r3
            r2 = r21
            r3 = 10
            if (r2 == r3) goto L_0x013f
            r3 = 13
            if (r2 == r3) goto L_0x013f
            r3 = 0
            if (r2 < 0) goto L_0x0130
            r21 = r3
            r3 = 31
            if (r3 >= r2) goto L_0x013b
            goto L_0x0132
        L_0x0130:
            r21 = r3
        L_0x0132:
            r3 = 127(0x7f, float:1.78E-43)
            if (r3 <= r2) goto L_0x0137
            goto L_0x013d
        L_0x0137:
            r3 = 159(0x9f, float:2.23E-43)
            if (r3 < r2) goto L_0x013d
        L_0x013b:
            r16 = r18
        L_0x013d:
            if (r16 != 0) goto L_0x0144
        L_0x013f:
            r3 = 65533(0xfffd, float:9.1831E-41)
            if (r2 != r3) goto L_0x0146
        L_0x0144:
            r3 = -1
            return r3
        L_0x0146:
            r3 = 65536(0x10000, float:9.18355E-41)
            if (r2 >= r3) goto L_0x014c
            r17 = r18
        L_0x014c:
            int r1 = r1 + r17
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
            r17 = r18
            r24 = r25
            goto L_0x01f1
        L_0x0157:
            r26 = r3
            r3 = r12 ^ 3968(0xf80, float:5.56E-42)
            int r13 = r15 << 6
            r3 = r3 ^ r13
            r13 = 128(0x80, float:1.794E-43)
            if (r3 >= r13) goto L_0x01ad
            r13 = 65533(0xfffd, float:9.1831E-41)
            r14 = 0
            r21 = r13
            r22 = 0
            int r25 = r2 + 1
            if (r2 != r0) goto L_0x0172
            return r1
        L_0x0172:
            r2 = r21
            r4 = 10
            if (r2 == r4) goto L_0x0197
            r4 = 13
            if (r2 == r4) goto L_0x0197
            r4 = 0
            if (r2 < 0) goto L_0x0188
            r21 = r4
            r4 = 31
            if (r4 >= r2) goto L_0x0193
            goto L_0x018a
        L_0x0188:
            r21 = r4
        L_0x018a:
            r4 = 127(0x7f, float:1.78E-43)
            if (r4 <= r2) goto L_0x018f
            goto L_0x0195
        L_0x018f:
            r4 = 159(0x9f, float:2.23E-43)
            if (r4 < r2) goto L_0x0195
        L_0x0193:
            r16 = r18
        L_0x0195:
            if (r16 != 0) goto L_0x019c
        L_0x0197:
            r4 = 65533(0xfffd, float:9.1831E-41)
            if (r2 != r4) goto L_0x019e
        L_0x019c:
            r4 = -1
            return r4
        L_0x019e:
            r4 = 65536(0x10000, float:9.18355E-41)
            if (r2 >= r4) goto L_0x01a3
            goto L_0x01a5
        L_0x01a3:
            r18 = r17
        L_0x01a5:
            int r1 = r1 + r18
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
            r24 = r25
            goto L_0x01ef
        L_0x01ad:
            r4 = r3
            r13 = 0
            r14 = r4
            r21 = 0
            int r22 = r2 + 1
            if (r2 != r0) goto L_0x01b7
            return r1
        L_0x01b7:
            r2 = 10
            if (r14 == r2) goto L_0x01da
            r2 = 13
            if (r14 == r2) goto L_0x01da
            r2 = 0
            if (r14 < 0) goto L_0x01cb
            r24 = r2
            r2 = 31
            if (r2 >= r14) goto L_0x01d6
            goto L_0x01cd
        L_0x01cb:
            r24 = r2
        L_0x01cd:
            r2 = 127(0x7f, float:1.78E-43)
            if (r2 <= r14) goto L_0x01d2
            goto L_0x01d8
        L_0x01d2:
            r2 = 159(0x9f, float:2.23E-43)
            if (r2 < r14) goto L_0x01d8
        L_0x01d6:
            r16 = r18
        L_0x01d8:
            if (r16 != 0) goto L_0x01df
        L_0x01da:
            r2 = 65533(0xfffd, float:9.1831E-41)
            if (r14 != r2) goto L_0x01e1
        L_0x01df:
            r2 = -1
            return r2
        L_0x01e1:
            r2 = 65536(0x10000, float:9.18355E-41)
            if (r14 >= r2) goto L_0x01e6
            goto L_0x01e8
        L_0x01e6:
            r18 = r17
        L_0x01e8:
            int r1 = r1 + r18
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
            r24 = r22
        L_0x01ef:
        L_0x01f1:
            int r8 = r8 + r17
            r2 = r24
            goto L_0x071a
        L_0x01f7:
            r26 = r3
            r3 = 4
            r4 = r9
            r10 = 0
            int r3 = r4 >> r3
            if (r3 != r11) goto L_0x040e
            r3 = r6
            r11 = 0
            int r13 = r8 + 2
            if (r5 > r13) goto L_0x0264
            r4 = 65533(0xfffd, float:9.1831E-41)
            r10 = 0
            r12 = r4
            r13 = 0
            int r14 = r2 + 1
            if (r2 != r0) goto L_0x0211
            return r1
        L_0x0211:
            r2 = 10
            if (r12 == r2) goto L_0x0232
            r2 = 13
            if (r12 == r2) goto L_0x0232
            r2 = 0
            if (r12 < 0) goto L_0x0222
            r15 = 31
            if (r15 >= r12) goto L_0x022b
        L_0x0222:
            r15 = 127(0x7f, float:1.78E-43)
            if (r15 <= r12) goto L_0x0227
            goto L_0x022e
        L_0x0227:
            r15 = 159(0x9f, float:2.23E-43)
            if (r15 < r12) goto L_0x022e
        L_0x022b:
            r2 = r18
            goto L_0x0230
        L_0x022e:
            r2 = r16
        L_0x0230:
            if (r2 != 0) goto L_0x0237
        L_0x0232:
            r2 = 65533(0xfffd, float:9.1831E-41)
            if (r12 != r2) goto L_0x0239
        L_0x0237:
            r2 = -1
            return r2
        L_0x0239:
            r2 = 65536(0x10000, float:9.18355E-41)
            if (r12 >= r2) goto L_0x0240
            r2 = r18
            goto L_0x0242
        L_0x0240:
            r2 = r17
        L_0x0242:
            int r1 = r1 + r2
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
            int r2 = r8 + 1
            if (r5 <= r2) goto L_0x0260
            int r2 = r8 + 1
            byte r2 = r3[r2]
            r4 = 0
            r10 = 192(0xc0, float:2.69E-43)
            r12 = r2
            r13 = 0
            r10 = r10 & r12
            r12 = 128(0x80, float:1.794E-43)
            if (r10 != r12) goto L_0x025a
            r16 = r18
        L_0x025a:
            if (r16 != 0) goto L_0x025e
            goto L_0x0260
        L_0x025e:
            goto L_0x0409
        L_0x0260:
            r17 = r18
            goto L_0x0409
        L_0x0264:
            byte r13 = r3[r8]
            int r14 = r8 + 1
            byte r14 = r3[r14]
            r15 = 0
            r22 = 192(0xc0, float:2.69E-43)
            r25 = r14
            r27 = 0
            r12 = r25 & r22
            r10 = 128(0x80, float:1.794E-43)
            if (r12 != r10) goto L_0x027a
            r10 = r18
            goto L_0x027c
        L_0x027a:
            r10 = r16
        L_0x027c:
            if (r10 != 0) goto L_0x02c5
            r4 = 65533(0xfffd, float:9.1831E-41)
            r10 = 0
            r12 = r4
            r15 = 0
            int r21 = r2 + 1
            if (r2 != r0) goto L_0x028a
            return r1
        L_0x028a:
            r2 = 10
            if (r12 == r2) goto L_0x02ad
            r2 = 13
            if (r12 == r2) goto L_0x02ad
            r2 = 0
            if (r12 < 0) goto L_0x029e
            r22 = r2
            r2 = 31
            if (r2 >= r12) goto L_0x02a9
            goto L_0x02a0
        L_0x029e:
            r22 = r2
        L_0x02a0:
            r2 = 127(0x7f, float:1.78E-43)
            if (r2 <= r12) goto L_0x02a5
            goto L_0x02ab
        L_0x02a5:
            r2 = 159(0x9f, float:2.23E-43)
            if (r2 < r12) goto L_0x02ab
        L_0x02a9:
            r16 = r18
        L_0x02ab:
            if (r16 != 0) goto L_0x02b2
        L_0x02ad:
            r2 = 65533(0xfffd, float:9.1831E-41)
            if (r12 != r2) goto L_0x02b4
        L_0x02b2:
            r2 = -1
            return r2
        L_0x02b4:
            r2 = 65536(0x10000, float:9.18355E-41)
            if (r12 >= r2) goto L_0x02ba
            r17 = r18
        L_0x02ba:
            int r1 = r1 + r17
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
            r17 = r18
            r14 = r21
            goto L_0x0409
        L_0x02c5:
            int r10 = r8 + 2
            byte r10 = r3[r10]
            r12 = 0
            r15 = 192(0xc0, float:2.69E-43)
            r25 = r10
            r27 = 0
            r15 = r25 & r15
            r4 = 128(0x80, float:1.794E-43)
            if (r15 != r4) goto L_0x02d9
            r4 = r18
            goto L_0x02db
        L_0x02d9:
            r4 = r16
        L_0x02db:
            if (r4 != 0) goto L_0x0324
            r4 = 65533(0xfffd, float:9.1831E-41)
            r12 = 0
            r15 = r4
            r21 = 0
            int r22 = r2 + 1
            if (r2 != r0) goto L_0x02ea
            return r1
        L_0x02ea:
            r2 = 10
            if (r15 == r2) goto L_0x030d
            r2 = 13
            if (r15 == r2) goto L_0x030d
            r2 = 0
            if (r15 < 0) goto L_0x02fe
            r24 = r2
            r2 = 31
            if (r2 >= r15) goto L_0x0309
            goto L_0x0300
        L_0x02fe:
            r24 = r2
        L_0x0300:
            r2 = 127(0x7f, float:1.78E-43)
            if (r2 <= r15) goto L_0x0305
            goto L_0x030b
        L_0x0305:
            r2 = 159(0x9f, float:2.23E-43)
            if (r2 < r15) goto L_0x030b
        L_0x0309:
            r16 = r18
        L_0x030b:
            if (r16 != 0) goto L_0x0312
        L_0x030d:
            r2 = 65533(0xfffd, float:9.1831E-41)
            if (r15 != r2) goto L_0x0314
        L_0x0312:
            r2 = -1
            return r2
        L_0x0314:
            r2 = 65536(0x10000, float:9.18355E-41)
            if (r15 >= r2) goto L_0x0319
            goto L_0x031b
        L_0x0319:
            r18 = r17
        L_0x031b:
            int r1 = r1 + r18
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
            r14 = r22
            goto L_0x0409
        L_0x0324:
            r4 = -123008(0xfffffffffffe1f80, float:NaN)
            r4 = r4 ^ r10
            int r12 = r14 << 6
            r4 = r4 ^ r12
            int r12 = r13 << 12
            r4 = r4 ^ r12
            r12 = 2048(0x800, float:2.87E-42)
            if (r4 >= r12) goto L_0x037d
            r12 = 65533(0xfffd, float:9.1831E-41)
            r15 = 0
            r21 = r12
            r22 = 0
            int r25 = r2 + 1
            if (r2 != r0) goto L_0x0342
            return r1
        L_0x0342:
            r2 = r21
            r21 = r3
            r3 = 10
            if (r2 == r3) goto L_0x0369
            r3 = 13
            if (r2 == r3) goto L_0x0369
            r3 = 0
            if (r2 < 0) goto L_0x035a
            r24 = r3
            r3 = 31
            if (r3 >= r2) goto L_0x0365
            goto L_0x035c
        L_0x035a:
            r24 = r3
        L_0x035c:
            r3 = 127(0x7f, float:1.78E-43)
            if (r3 <= r2) goto L_0x0361
            goto L_0x0367
        L_0x0361:
            r3 = 159(0x9f, float:2.23E-43)
            if (r3 < r2) goto L_0x0367
        L_0x0365:
            r16 = r18
        L_0x0367:
            if (r16 != 0) goto L_0x036e
        L_0x0369:
            r3 = 65533(0xfffd, float:9.1831E-41)
            if (r2 != r3) goto L_0x0370
        L_0x036e:
            r3 = -1
            return r3
        L_0x0370:
            r3 = 65536(0x10000, float:9.18355E-41)
            if (r2 >= r3) goto L_0x0376
            r17 = r18
        L_0x0376:
            int r1 = r1 + r17
        L_0x0379:
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
            goto L_0x0404
        L_0x037d:
            r21 = r3
            r3 = 55296(0xd800, float:7.7486E-41)
            if (r3 <= r4) goto L_0x0385
            goto L_0x03c5
        L_0x0385:
            r3 = 57343(0xdfff, float:8.0355E-41)
            if (r3 < r4) goto L_0x03c5
            r3 = 65533(0xfffd, float:9.1831E-41)
            r12 = 0
            r15 = r3
            r22 = 0
            int r25 = r2 + 1
            if (r2 != r0) goto L_0x0396
            return r1
        L_0x0396:
            r2 = 10
            if (r15 == r2) goto L_0x03b9
            r2 = 13
            if (r15 == r2) goto L_0x03b9
            r2 = 0
            if (r15 < 0) goto L_0x03aa
            r24 = r2
            r2 = 31
            if (r2 >= r15) goto L_0x03b5
            goto L_0x03ac
        L_0x03aa:
            r24 = r2
        L_0x03ac:
            r2 = 127(0x7f, float:1.78E-43)
            if (r2 <= r15) goto L_0x03b1
            goto L_0x03b7
        L_0x03b1:
            r2 = 159(0x9f, float:2.23E-43)
            if (r2 < r15) goto L_0x03b7
        L_0x03b5:
            r16 = r18
        L_0x03b7:
            if (r16 != 0) goto L_0x03be
        L_0x03b9:
            r2 = 65533(0xfffd, float:9.1831E-41)
            if (r15 != r2) goto L_0x03c0
        L_0x03be:
            r2 = -1
            return r2
        L_0x03c0:
            r2 = 65536(0x10000, float:9.18355E-41)
            if (r15 >= r2) goto L_0x03ff
            goto L_0x03fd
        L_0x03c5:
            r3 = r4
            r12 = 0
            r15 = r3
            r22 = 0
            int r25 = r2 + 1
            if (r2 != r0) goto L_0x03cf
            return r1
        L_0x03cf:
            r2 = 10
            if (r15 == r2) goto L_0x03f2
            r2 = 13
            if (r15 == r2) goto L_0x03f2
            r2 = 0
            if (r15 < 0) goto L_0x03e3
            r24 = r2
            r2 = 31
            if (r2 >= r15) goto L_0x03ee
            goto L_0x03e5
        L_0x03e3:
            r24 = r2
        L_0x03e5:
            r2 = 127(0x7f, float:1.78E-43)
            if (r2 <= r15) goto L_0x03ea
            goto L_0x03f0
        L_0x03ea:
            r2 = 159(0x9f, float:2.23E-43)
            if (r2 < r15) goto L_0x03f0
        L_0x03ee:
            r16 = r18
        L_0x03f0:
            if (r16 != 0) goto L_0x03f7
        L_0x03f2:
            r2 = 65533(0xfffd, float:9.1831E-41)
            if (r15 != r2) goto L_0x03f9
        L_0x03f7:
            r2 = -1
            return r2
        L_0x03f9:
            r2 = 65536(0x10000, float:9.18355E-41)
            if (r15 >= r2) goto L_0x03ff
        L_0x03fd:
            r17 = r18
        L_0x03ff:
            int r1 = r1 + r17
            goto L_0x0379
        L_0x0404:
            r14 = r25
            r17 = 3
        L_0x0409:
            int r8 = r8 + r17
            r2 = r14
            goto L_0x071a
        L_0x040e:
            r3 = 3
            r4 = r9
            r10 = 0
            int r3 = r4 >> r3
            if (r3 != r11) goto L_0x06e0
            r3 = r6
            r4 = 0
            int r10 = r8 + 3
            if (r5 > r10) goto L_0x0498
            r10 = 65533(0xfffd, float:9.1831E-41)
            r11 = 0
            r12 = r10
            r13 = 0
            int r14 = r2 + 1
            if (r2 != r0) goto L_0x0426
            return r1
        L_0x0426:
            r2 = 10
            if (r12 == r2) goto L_0x0447
            r2 = 13
            if (r12 == r2) goto L_0x0447
            r2 = 0
            if (r12 < 0) goto L_0x0437
            r15 = 31
            if (r15 >= r12) goto L_0x0440
        L_0x0437:
            r15 = 127(0x7f, float:1.78E-43)
            if (r15 <= r12) goto L_0x043c
            goto L_0x0443
        L_0x043c:
            r15 = 159(0x9f, float:2.23E-43)
            if (r15 < r12) goto L_0x0443
        L_0x0440:
            r2 = r18
            goto L_0x0445
        L_0x0443:
            r2 = r16
        L_0x0445:
            if (r2 != 0) goto L_0x044c
        L_0x0447:
            r2 = 65533(0xfffd, float:9.1831E-41)
            if (r12 != r2) goto L_0x044e
        L_0x044c:
            r2 = -1
            return r2
        L_0x044e:
            r2 = 65536(0x10000, float:9.18355E-41)
            if (r12 >= r2) goto L_0x0455
            r2 = r18
            goto L_0x0457
        L_0x0455:
            r2 = r17
        L_0x0457:
            int r1 = r1 + r2
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
            int r2 = r8 + 1
            if (r5 <= r2) goto L_0x0494
            int r2 = r8 + 1
            byte r2 = r3[r2]
            r10 = 0
            r11 = 192(0xc0, float:2.69E-43)
            r12 = r2
            r13 = 0
            r11 = r11 & r12
            r12 = 128(0x80, float:1.794E-43)
            if (r11 != r12) goto L_0x0470
            r11 = r18
            goto L_0x0472
        L_0x0470:
            r11 = r16
        L_0x0472:
            if (r11 != 0) goto L_0x0476
            goto L_0x0494
        L_0x0476:
            int r2 = r8 + 2
            if (r5 <= r2) goto L_0x0492
            int r2 = r8 + 2
            byte r2 = r3[r2]
            r10 = 0
            r11 = 192(0xc0, float:2.69E-43)
            r12 = r2
            r13 = 0
            r11 = r11 & r12
            r12 = 128(0x80, float:1.794E-43)
            if (r11 != r12) goto L_0x048a
            r16 = r18
        L_0x048a:
            if (r16 != 0) goto L_0x048e
            goto L_0x0492
        L_0x048e:
            r17 = 3
            goto L_0x06dc
        L_0x0492:
            goto L_0x06dc
        L_0x0494:
            r17 = r18
            goto L_0x06dc
        L_0x0498:
            byte r10 = r3[r8]
            int r11 = r8 + 1
            byte r11 = r3[r11]
            r12 = 0
            r13 = 192(0xc0, float:2.69E-43)
            r14 = r11
            r15 = 0
            r13 = r13 & r14
            r14 = 128(0x80, float:1.794E-43)
            if (r13 != r14) goto L_0x04ab
            r13 = r18
            goto L_0x04ad
        L_0x04ab:
            r13 = r16
        L_0x04ad:
            if (r13 != 0) goto L_0x04f6
            r12 = 65533(0xfffd, float:9.1831E-41)
            r13 = 0
            r14 = r12
            r15 = 0
            int r21 = r2 + 1
            if (r2 != r0) goto L_0x04bb
            return r1
        L_0x04bb:
            r2 = 10
            if (r14 == r2) goto L_0x04de
            r2 = 13
            if (r14 == r2) goto L_0x04de
            r2 = 0
            if (r14 < 0) goto L_0x04cf
            r22 = r2
            r2 = 31
            if (r2 >= r14) goto L_0x04da
            goto L_0x04d1
        L_0x04cf:
            r22 = r2
        L_0x04d1:
            r2 = 127(0x7f, float:1.78E-43)
            if (r2 <= r14) goto L_0x04d6
            goto L_0x04dc
        L_0x04d6:
            r2 = 159(0x9f, float:2.23E-43)
            if (r2 < r14) goto L_0x04dc
        L_0x04da:
            r16 = r18
        L_0x04dc:
            if (r16 != 0) goto L_0x04e3
        L_0x04de:
            r2 = 65533(0xfffd, float:9.1831E-41)
            if (r14 != r2) goto L_0x04e5
        L_0x04e3:
            r2 = -1
            return r2
        L_0x04e5:
            r2 = 65536(0x10000, float:9.18355E-41)
            if (r14 >= r2) goto L_0x04eb
            r17 = r18
        L_0x04eb:
            int r1 = r1 + r17
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
            r17 = r18
            r14 = r21
            goto L_0x06dc
        L_0x04f6:
            int r12 = r8 + 2
            byte r12 = r3[r12]
            r13 = 0
            r14 = 192(0xc0, float:2.69E-43)
            r15 = r12
            r27 = 0
            r14 = r14 & r15
            r15 = 128(0x80, float:1.794E-43)
            if (r14 != r15) goto L_0x0508
            r14 = r18
            goto L_0x050a
        L_0x0508:
            r14 = r16
        L_0x050a:
            if (r14 != 0) goto L_0x0553
            r13 = 65533(0xfffd, float:9.1831E-41)
            r14 = 0
            r15 = r13
            r21 = 0
            int r22 = r2 + 1
            if (r2 != r0) goto L_0x0519
            return r1
        L_0x0519:
            r2 = 10
            if (r15 == r2) goto L_0x053c
            r2 = 13
            if (r15 == r2) goto L_0x053c
            r2 = 0
            if (r15 < 0) goto L_0x052d
            r24 = r2
            r2 = 31
            if (r2 >= r15) goto L_0x0538
            goto L_0x052f
        L_0x052d:
            r24 = r2
        L_0x052f:
            r2 = 127(0x7f, float:1.78E-43)
            if (r2 <= r15) goto L_0x0534
            goto L_0x053a
        L_0x0534:
            r2 = 159(0x9f, float:2.23E-43)
            if (r2 < r15) goto L_0x053a
        L_0x0538:
            r16 = r18
        L_0x053a:
            if (r16 != 0) goto L_0x0541
        L_0x053c:
            r2 = 65533(0xfffd, float:9.1831E-41)
            if (r15 != r2) goto L_0x0543
        L_0x0541:
            r2 = -1
            return r2
        L_0x0543:
            r2 = 65536(0x10000, float:9.18355E-41)
            if (r15 >= r2) goto L_0x0548
            goto L_0x054a
        L_0x0548:
            r18 = r17
        L_0x054a:
            int r1 = r1 + r18
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
            r14 = r22
            goto L_0x06dc
        L_0x0553:
            int r13 = r8 + 3
            byte r13 = r3[r13]
            r14 = 0
            r15 = 192(0xc0, float:2.69E-43)
            r27 = r13
            r28 = 0
            r15 = r27 & r15
            r27 = r3
            r3 = 128(0x80, float:1.794E-43)
            if (r15 != r3) goto L_0x0569
            r3 = r18
            goto L_0x056b
        L_0x0569:
            r3 = r16
        L_0x056b:
            if (r3 != 0) goto L_0x05b5
            r3 = 65533(0xfffd, float:9.1831E-41)
            r14 = 0
            r15 = r3
            r21 = 0
            int r22 = r2 + 1
            if (r2 != r0) goto L_0x057a
            return r1
        L_0x057a:
            r2 = 10
            if (r15 == r2) goto L_0x059d
            r2 = 13
            if (r15 == r2) goto L_0x059d
            r2 = 0
            if (r15 < 0) goto L_0x058e
            r24 = r2
            r2 = 31
            if (r2 >= r15) goto L_0x0599
            goto L_0x0590
        L_0x058e:
            r24 = r2
        L_0x0590:
            r2 = 127(0x7f, float:1.78E-43)
            if (r2 <= r15) goto L_0x0595
            goto L_0x059b
        L_0x0595:
            r2 = 159(0x9f, float:2.23E-43)
            if (r2 < r15) goto L_0x059b
        L_0x0599:
            r16 = r18
        L_0x059b:
            if (r16 != 0) goto L_0x05a2
        L_0x059d:
            r2 = 65533(0xfffd, float:9.1831E-41)
            if (r15 != r2) goto L_0x05a4
        L_0x05a2:
            r2 = -1
            return r2
        L_0x05a4:
            r2 = 65536(0x10000, float:9.18355E-41)
            if (r15 >= r2) goto L_0x05aa
            r17 = r18
        L_0x05aa:
            int r1 = r1 + r17
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
            r14 = r22
            r17 = 3
            goto L_0x06dc
        L_0x05b5:
            r3 = 3678080(0x381f80, float:5.154088E-39)
            r3 = r3 ^ r13
            int r14 = r12 << 6
            r3 = r3 ^ r14
            int r14 = r11 << 12
            r3 = r3 ^ r14
            int r14 = r10 << 18
            r3 = r3 ^ r14
            r14 = 1114111(0x10ffff, float:1.561202E-39)
            if (r3 <= r14) goto L_0x0614
            r14 = 65533(0xfffd, float:9.1831E-41)
            r15 = 0
            r21 = r14
            r22 = 0
            int r25 = r2 + 1
            if (r2 != r0) goto L_0x05d7
            return r1
        L_0x05d7:
            r2 = r21
            r21 = r4
            r4 = 10
            if (r2 == r4) goto L_0x05fe
            r4 = 13
            if (r2 == r4) goto L_0x05fe
            r4 = 0
            if (r2 < 0) goto L_0x05ef
            r24 = r4
            r4 = 31
            if (r4 >= r2) goto L_0x05fa
            goto L_0x05f1
        L_0x05ef:
            r24 = r4
        L_0x05f1:
            r4 = 127(0x7f, float:1.78E-43)
            if (r4 <= r2) goto L_0x05f6
            goto L_0x05fc
        L_0x05f6:
            r4 = 159(0x9f, float:2.23E-43)
            if (r4 < r2) goto L_0x05fc
        L_0x05fa:
            r16 = r18
        L_0x05fc:
            if (r16 != 0) goto L_0x0603
        L_0x05fe:
            r4 = 65533(0xfffd, float:9.1831E-41)
            if (r2 != r4) goto L_0x0605
        L_0x0603:
            r4 = -1
            return r4
        L_0x0605:
            r4 = 65536(0x10000, float:9.18355E-41)
            if (r2 >= r4) goto L_0x060b
            r17 = r18
        L_0x060b:
            int r1 = r1 + r17
        L_0x060e:
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
            r14 = r25
            goto L_0x06d9
        L_0x0614:
            r21 = r4
            r4 = 55296(0xd800, float:7.7486E-41)
            if (r4 <= r3) goto L_0x061c
            goto L_0x0661
        L_0x061c:
            r4 = 57343(0xdfff, float:8.0355E-41)
            if (r4 < r3) goto L_0x0661
            r4 = 65533(0xfffd, float:9.1831E-41)
            r14 = 0
            r15 = r4
            r22 = 0
            int r25 = r2 + 1
            if (r2 != r0) goto L_0x062d
            return r1
        L_0x062d:
            r2 = 10
            if (r15 == r2) goto L_0x0650
            r2 = 13
            if (r15 == r2) goto L_0x0650
            r2 = 0
            if (r15 < 0) goto L_0x0641
            r24 = r2
            r2 = 31
            if (r2 >= r15) goto L_0x064c
            goto L_0x0643
        L_0x0641:
            r24 = r2
        L_0x0643:
            r2 = 127(0x7f, float:1.78E-43)
            if (r2 <= r15) goto L_0x0648
            goto L_0x064e
        L_0x0648:
            r2 = 159(0x9f, float:2.23E-43)
            if (r2 < r15) goto L_0x064e
        L_0x064c:
            r16 = r18
        L_0x064e:
            if (r16 != 0) goto L_0x0655
        L_0x0650:
            r2 = 65533(0xfffd, float:9.1831E-41)
            if (r15 != r2) goto L_0x0657
        L_0x0655:
            r2 = -1
            return r2
        L_0x0657:
            r2 = 65536(0x10000, float:9.18355E-41)
            if (r15 >= r2) goto L_0x065d
        L_0x065b:
            r17 = r18
        L_0x065d:
            int r1 = r1 + r17
            goto L_0x060e
        L_0x0661:
            r4 = 65536(0x10000, float:9.18355E-41)
            if (r3 >= r4) goto L_0x06a0
            r4 = 65533(0xfffd, float:9.1831E-41)
            r14 = 0
            r15 = r4
            r22 = 0
            int r25 = r2 + 1
            if (r2 != r0) goto L_0x0671
            return r1
        L_0x0671:
            r2 = 10
            if (r15 == r2) goto L_0x0694
            r2 = 13
            if (r15 == r2) goto L_0x0694
            r2 = 0
            if (r15 < 0) goto L_0x0685
            r24 = r2
            r2 = 31
            if (r2 >= r15) goto L_0x0690
            goto L_0x0687
        L_0x0685:
            r24 = r2
        L_0x0687:
            r2 = 127(0x7f, float:1.78E-43)
            if (r2 <= r15) goto L_0x068c
            goto L_0x0692
        L_0x068c:
            r2 = 159(0x9f, float:2.23E-43)
            if (r2 < r15) goto L_0x0692
        L_0x0690:
            r16 = r18
        L_0x0692:
            if (r16 != 0) goto L_0x0699
        L_0x0694:
            r2 = 65533(0xfffd, float:9.1831E-41)
            if (r15 != r2) goto L_0x069b
        L_0x0699:
            r2 = -1
            return r2
        L_0x069b:
            r2 = 65536(0x10000, float:9.18355E-41)
            if (r15 >= r2) goto L_0x065d
            goto L_0x065b
        L_0x06a0:
            r4 = r3
            r14 = 0
            r15 = r4
            r22 = 0
            int r25 = r2 + 1
            if (r2 != r0) goto L_0x06aa
            return r1
        L_0x06aa:
            r2 = 10
            if (r15 == r2) goto L_0x06cd
            r2 = 13
            if (r15 == r2) goto L_0x06cd
            r2 = 0
            if (r15 < 0) goto L_0x06be
            r24 = r2
            r2 = 31
            if (r2 >= r15) goto L_0x06c9
            goto L_0x06c0
        L_0x06be:
            r24 = r2
        L_0x06c0:
            r2 = 127(0x7f, float:1.78E-43)
            if (r2 <= r15) goto L_0x06c5
            goto L_0x06cb
        L_0x06c5:
            r2 = 159(0x9f, float:2.23E-43)
            if (r2 < r15) goto L_0x06cb
        L_0x06c9:
            r16 = r18
        L_0x06cb:
            if (r16 != 0) goto L_0x06d2
        L_0x06cd:
            r2 = 65533(0xfffd, float:9.1831E-41)
            if (r15 != r2) goto L_0x06d4
        L_0x06d2:
            r2 = -1
            return r2
        L_0x06d4:
            r2 = 65536(0x10000, float:9.18355E-41)
            if (r15 >= r2) goto L_0x065d
            goto L_0x065b
        L_0x06d9:
            r17 = 4
        L_0x06dc:
            int r8 = r8 + r17
            r2 = r14
            goto L_0x071a
        L_0x06e0:
            r3 = 65533(0xfffd, float:9.1831E-41)
            r4 = 0
            int r10 = r2 + 1
            if (r2 != r0) goto L_0x06e9
            return r1
        L_0x06e9:
            r2 = 10
            if (r3 == r2) goto L_0x0707
            r2 = 13
            if (r3 == r2) goto L_0x0707
            r2 = 0
            if (r3 < 0) goto L_0x06fa
            r11 = 31
            if (r11 >= r3) goto L_0x0703
        L_0x06fa:
            r11 = 127(0x7f, float:1.78E-43)
            if (r11 <= r3) goto L_0x06ff
            goto L_0x0705
        L_0x06ff:
            r11 = 159(0x9f, float:2.23E-43)
            if (r11 < r3) goto L_0x0705
        L_0x0703:
            r16 = r18
        L_0x0705:
            if (r16 != 0) goto L_0x070c
        L_0x0707:
            r2 = 65533(0xfffd, float:9.1831E-41)
            if (r3 != r2) goto L_0x070e
        L_0x070c:
            r2 = -1
            return r2
        L_0x070e:
            r2 = 65536(0x10000, float:9.18355E-41)
            if (r3 >= r2) goto L_0x0714
            r17 = r18
        L_0x0714:
            int r1 = r1 + r17
            int r8 = r8 + 1
            r2 = r10
        L_0x071a:
            r4 = r29
            r3 = r26
            goto L_0x000c
        L_0x0721:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.internal.ByteStringKt.codePointIndexToCharIndex(byte[], int):int");
    }

    public static final String commonBase64(ByteString $this$commonBase64) {
        Intrinsics.checkNotNullParameter($this$commonBase64, "$this$commonBase64");
        String encodeBase64$default = Base64.encodeBase64$default($this$commonBase64.getData$okio(), (byte[]) null, 1, (Object) null);
        Log1F380D.a((Object) encodeBase64$default);
        return encodeBase64$default;
    }

    public static final int commonCompareTo(ByteString $this$commonCompareTo, ByteString other) {
        Intrinsics.checkNotNullParameter($this$commonCompareTo, "$this$commonCompareTo");
        Intrinsics.checkNotNullParameter(other, "other");
        int size = $this$commonCompareTo.size();
        int size2 = other.size();
        int min = Math.min(size, size2);
        for (int i = 0; i < min; i++) {
            byte b = $this$commonCompareTo.getByte(i) & UByte.MAX_VALUE;
            byte b2 = other.getByte(i) & UByte.MAX_VALUE;
            if (b != b2) {
                return b < b2 ? -1 : 1;
            }
        }
        if (size == size2) {
            return 0;
        }
        return size < size2 ? -1 : 1;
    }

    public static final ByteString commonDecodeBase64(String $this$commonDecodeBase64) {
        Intrinsics.checkNotNullParameter($this$commonDecodeBase64, "$this$commonDecodeBase64");
        byte[] decodeBase64ToArray = Base64.decodeBase64ToArray($this$commonDecodeBase64);
        if (decodeBase64ToArray != null) {
            return new ByteString(decodeBase64ToArray);
        }
        return null;
    }

    public static final ByteString commonDecodeHex(String $this$commonDecodeHex) {
        Intrinsics.checkNotNullParameter($this$commonDecodeHex, "$this$commonDecodeHex");
        if ($this$commonDecodeHex.length() % 2 == 0) {
            byte[] bArr = new byte[($this$commonDecodeHex.length() / 2)];
            int length = bArr.length;
            for (int i = 0; i < length; i++) {
                bArr[i] = (byte) ((decodeHexDigit($this$commonDecodeHex.charAt(i * 2)) << 4) + decodeHexDigit($this$commonDecodeHex.charAt((i * 2) + 1)));
            }
            return new ByteString(bArr);
        }
        throw new IllegalArgumentException(("Unexpected hex string: " + $this$commonDecodeHex).toString());
    }

    public static final ByteString commonEncodeUtf8(String $this$commonEncodeUtf8) {
        Intrinsics.checkNotNullParameter($this$commonEncodeUtf8, "$this$commonEncodeUtf8");
        ByteString byteString = new ByteString(Platform.asUtf8ToByteArray($this$commonEncodeUtf8));
        byteString.setUtf8$okio($this$commonEncodeUtf8);
        return byteString;
    }

    public static final boolean commonEndsWith(ByteString $this$commonEndsWith, ByteString suffix) {
        Intrinsics.checkNotNullParameter($this$commonEndsWith, "$this$commonEndsWith");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        return $this$commonEndsWith.rangeEquals($this$commonEndsWith.size() - suffix.size(), suffix, 0, suffix.size());
    }

    public static final boolean commonEndsWith(ByteString $this$commonEndsWith, byte[] suffix) {
        Intrinsics.checkNotNullParameter($this$commonEndsWith, "$this$commonEndsWith");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        return $this$commonEndsWith.rangeEquals($this$commonEndsWith.size() - suffix.length, suffix, 0, suffix.length);
    }

    public static final boolean commonEquals(ByteString $this$commonEquals, Object other) {
        Intrinsics.checkNotNullParameter($this$commonEquals, "$this$commonEquals");
        if (other == $this$commonEquals) {
            return true;
        }
        if (other instanceof ByteString) {
            return ((ByteString) other).size() == $this$commonEquals.getData$okio().length && ((ByteString) other).rangeEquals(0, $this$commonEquals.getData$okio(), 0, $this$commonEquals.getData$okio().length);
        }
        return false;
    }

    public static final byte commonGetByte(ByteString $this$commonGetByte, int pos) {
        Intrinsics.checkNotNullParameter($this$commonGetByte, "$this$commonGetByte");
        return $this$commonGetByte.getData$okio()[pos];
    }

    public static final int commonGetSize(ByteString $this$commonGetSize) {
        Intrinsics.checkNotNullParameter($this$commonGetSize, "$this$commonGetSize");
        return $this$commonGetSize.getData$okio().length;
    }

    public static final int commonHashCode(ByteString $this$commonHashCode) {
        Intrinsics.checkNotNullParameter($this$commonHashCode, "$this$commonHashCode");
        int hashCode$okio = $this$commonHashCode.getHashCode$okio();
        if (hashCode$okio != 0) {
            return hashCode$okio;
        }
        int hashCode = Arrays.hashCode($this$commonHashCode.getData$okio());
        $this$commonHashCode.setHashCode$okio(hashCode);
        return hashCode;
    }

    public static final int commonIndexOf(ByteString $this$commonIndexOf, byte[] other, int fromIndex) {
        Intrinsics.checkNotNullParameter($this$commonIndexOf, "$this$commonIndexOf");
        Intrinsics.checkNotNullParameter(other, "other");
        int length = $this$commonIndexOf.getData$okio().length - other.length;
        int max = Math.max(fromIndex, 0);
        if (max > length) {
            return -1;
        }
        while (!Util.arrayRangeEquals($this$commonIndexOf.getData$okio(), max, other, 0, other.length)) {
            if (max == length) {
                return -1;
            }
            max++;
        }
        return max;
    }

    public static final byte[] commonInternalArray(ByteString $this$commonInternalArray) {
        Intrinsics.checkNotNullParameter($this$commonInternalArray, "$this$commonInternalArray");
        return $this$commonInternalArray.getData$okio();
    }

    public static final int commonLastIndexOf(ByteString $this$commonLastIndexOf, ByteString other, int fromIndex) {
        Intrinsics.checkNotNullParameter($this$commonLastIndexOf, "$this$commonLastIndexOf");
        Intrinsics.checkNotNullParameter(other, "other");
        return $this$commonLastIndexOf.lastIndexOf(other.internalArray$okio(), fromIndex);
    }

    public static final int commonLastIndexOf(ByteString $this$commonLastIndexOf, byte[] other, int fromIndex) {
        Intrinsics.checkNotNullParameter($this$commonLastIndexOf, "$this$commonLastIndexOf");
        Intrinsics.checkNotNullParameter(other, "other");
        for (int min = Math.min(fromIndex, $this$commonLastIndexOf.getData$okio().length - other.length); min >= 0; min--) {
            if (Util.arrayRangeEquals($this$commonLastIndexOf.getData$okio(), min, other, 0, other.length)) {
                return min;
            }
        }
        return -1;
    }

    public static final ByteString commonOf(byte[] data) {
        Intrinsics.checkNotNullParameter(data, "data");
        byte[] copyOf = Arrays.copyOf(data, data.length);
        Intrinsics.checkNotNullExpressionValue(copyOf, "java.util.Arrays.copyOf(this, size)");
        return new ByteString(copyOf);
    }

    public static final boolean commonRangeEquals(ByteString $this$commonRangeEquals, int offset, ByteString other, int otherOffset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRangeEquals, "$this$commonRangeEquals");
        Intrinsics.checkNotNullParameter(other, "other");
        return other.rangeEquals(otherOffset, $this$commonRangeEquals.getData$okio(), offset, byteCount);
    }

    public static final boolean commonRangeEquals(ByteString $this$commonRangeEquals, int offset, byte[] other, int otherOffset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonRangeEquals, "$this$commonRangeEquals");
        Intrinsics.checkNotNullParameter(other, "other");
        return offset >= 0 && offset <= $this$commonRangeEquals.getData$okio().length - byteCount && otherOffset >= 0 && otherOffset <= other.length - byteCount && Util.arrayRangeEquals($this$commonRangeEquals.getData$okio(), offset, other, otherOffset, byteCount);
    }

    public static final boolean commonStartsWith(ByteString $this$commonStartsWith, ByteString prefix) {
        Intrinsics.checkNotNullParameter($this$commonStartsWith, "$this$commonStartsWith");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        return $this$commonStartsWith.rangeEquals(0, prefix, 0, prefix.size());
    }

    public static final boolean commonStartsWith(ByteString $this$commonStartsWith, byte[] prefix) {
        Intrinsics.checkNotNullParameter($this$commonStartsWith, "$this$commonStartsWith");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        return $this$commonStartsWith.rangeEquals(0, prefix, 0, prefix.length);
    }

    public static final ByteString commonSubstring(ByteString $this$commonSubstring, int beginIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$commonSubstring, "$this$commonSubstring");
        boolean z = true;
        if (beginIndex >= 0) {
            if (endIndex <= $this$commonSubstring.getData$okio().length) {
                if (endIndex - beginIndex < 0) {
                    z = false;
                }
                if (z) {
                    return (beginIndex == 0 && endIndex == $this$commonSubstring.getData$okio().length) ? $this$commonSubstring : new ByteString(ArraysKt.copyOfRange($this$commonSubstring.getData$okio(), beginIndex, endIndex));
                }
                throw new IllegalArgumentException("endIndex < beginIndex".toString());
            }
            throw new IllegalArgumentException(("endIndex > length(" + $this$commonSubstring.getData$okio().length + ')').toString());
        }
        throw new IllegalArgumentException("beginIndex < 0".toString());
    }

    public static final ByteString commonToAsciiLowercase(ByteString $this$commonToAsciiLowercase) {
        byte b;
        Intrinsics.checkNotNullParameter($this$commonToAsciiLowercase, "$this$commonToAsciiLowercase");
        int i = 0;
        while (i < $this$commonToAsciiLowercase.getData$okio().length) {
            byte b2 = $this$commonToAsciiLowercase.getData$okio()[i];
            byte b3 = (byte) 65;
            if (b2 < b3 || b2 > (b = (byte) 90)) {
                i++;
            } else {
                byte[] data$okio = $this$commonToAsciiLowercase.getData$okio();
                byte[] copyOf = Arrays.copyOf(data$okio, data$okio.length);
                Intrinsics.checkNotNullExpressionValue(copyOf, "java.util.Arrays.copyOf(this, size)");
                int i2 = i + 1;
                copyOf[i] = (byte) (b2 + 32);
                while (i2 < copyOf.length) {
                    byte b4 = copyOf[i2];
                    if (b4 < b3 || b4 > b) {
                        i2++;
                    } else {
                        copyOf[i2] = (byte) (b4 + 32);
                        i2++;
                    }
                }
                return new ByteString(copyOf);
            }
        }
        return $this$commonToAsciiLowercase;
    }

    public static final ByteString commonToAsciiUppercase(ByteString $this$commonToAsciiUppercase) {
        byte b;
        Intrinsics.checkNotNullParameter($this$commonToAsciiUppercase, "$this$commonToAsciiUppercase");
        int i = 0;
        while (i < $this$commonToAsciiUppercase.getData$okio().length) {
            byte b2 = $this$commonToAsciiUppercase.getData$okio()[i];
            byte b3 = (byte) 97;
            if (b2 < b3 || b2 > (b = (byte) 122)) {
                i++;
            } else {
                byte[] data$okio = $this$commonToAsciiUppercase.getData$okio();
                byte[] copyOf = Arrays.copyOf(data$okio, data$okio.length);
                Intrinsics.checkNotNullExpressionValue(copyOf, "java.util.Arrays.copyOf(this, size)");
                int i2 = i + 1;
                copyOf[i] = (byte) (b2 - 32);
                while (i2 < copyOf.length) {
                    byte b4 = copyOf[i2];
                    if (b4 < b3 || b4 > b) {
                        i2++;
                    } else {
                        copyOf[i2] = (byte) (b4 - 32);
                        i2++;
                    }
                }
                return new ByteString(copyOf);
            }
        }
        return $this$commonToAsciiUppercase;
    }

    public static final byte[] commonToByteArray(ByteString $this$commonToByteArray) {
        Intrinsics.checkNotNullParameter($this$commonToByteArray, "$this$commonToByteArray");
        byte[] data$okio = $this$commonToByteArray.getData$okio();
        byte[] copyOf = Arrays.copyOf(data$okio, data$okio.length);
        Intrinsics.checkNotNullExpressionValue(copyOf, "java.util.Arrays.copyOf(this, size)");
        return copyOf;
    }

    public static final ByteString commonToByteString(byte[] $this$commonToByteString, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonToByteString, "$this$commonToByteString");
        Util.checkOffsetAndCount((long) $this$commonToByteString.length, (long) offset, (long) byteCount);
        return new ByteString(ArraysKt.copyOfRange($this$commonToByteString, offset, offset + byteCount));
    }

    public static final void commonWrite(ByteString $this$commonWrite, Buffer buffer, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter($this$commonWrite, "$this$commonWrite");
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        buffer.write($this$commonWrite.getData$okio(), offset, byteCount);
    }

    /* access modifiers changed from: private */
    public static final int decodeHexDigit(char c) {
        if ('0' <= c && '9' >= c) {
            return c - '0';
        }
        if ('a' <= c && 'f' >= c) {
            return (c - 'a') + 10;
        }
        if ('A' <= c && 'F' >= c) {
            return (c - 'A') + 10;
        }
        throw new IllegalArgumentException("Unexpected hex digit: " + c);
    }

    public static final char[] getHEX_DIGIT_CHARS() {
        return HEX_DIGIT_CHARS;
    }

    public static final String commonBase64Url(ByteString $this$commonBase64Url) {
        Intrinsics.checkNotNullParameter($this$commonBase64Url, "$this$commonBase64Url");
        String encodeBase64 = Base64.encodeBase64($this$commonBase64Url.getData$okio(), Base64.getBASE64_URL_SAFE());
        Log1F380D.a((Object) encodeBase64);
        return encodeBase64;
    }

    public static final String commonHex(ByteString $this$commonHex) {
        Intrinsics.checkNotNullParameter($this$commonHex, "$this$commonHex");
        char[] cArr = new char[($this$commonHex.getData$okio().length * 2)];
        int i = 0;
        for (byte b : $this$commonHex.getData$okio()) {
            int i2 = i + 1;
            cArr[i] = getHEX_DIGIT_CHARS()[(b >> 4) & 15];
            i = i2 + 1;
            cArr[i2] = getHEX_DIGIT_CHARS()[15 & b];
        }
        String str = new String(cArr);
        Log1F380D.a((Object) str);
        return str;
    }

    public static final String commonToString(ByteString $this$commonToString) {
        Intrinsics.checkNotNullParameter($this$commonToString, "$this$commonToString");
        boolean z = true;
        if ($this$commonToString.getData$okio().length == 0) {
            return "[size=0]";
        }
        int access$codePointIndexToCharIndex = codePointIndexToCharIndex($this$commonToString.getData$okio(), 64);
        if (access$codePointIndexToCharIndex != -1) {
            String utf8 = $this$commonToString.utf8();
            if (utf8 != null) {
                String substring = utf8.substring(0, access$codePointIndexToCharIndex);
                Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                String replace$default = StringsKt.replace$default(substring, "\\", "\\\\", false, 4, (Object) null);
                Log1F380D.a((Object) replace$default);
                String replace$default2 = StringsKt.replace$default(replace$default, "\n", "\\n", false, 4, (Object) null);
                Log1F380D.a((Object) replace$default2);
                String replace$default3 = StringsKt.replace$default(replace$default2, "\r", "\\r", false, 4, (Object) null);
                Log1F380D.a((Object) replace$default3);
                return access$codePointIndexToCharIndex < utf8.length() ? "[size=" + $this$commonToString.getData$okio().length + " text=" + replace$default3 + "…]" : "[text=" + replace$default3 + ']';
            }
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        } else if ($this$commonToString.getData$okio().length <= 64) {
            return "[hex=" + $this$commonToString.hex() + ']';
        } else {
            StringBuilder append = new StringBuilder().append("[size=").append($this$commonToString.getData$okio().length).append(" hex=");
            ByteString byteString = $this$commonToString;
            if (64 <= byteString.getData$okio().length) {
                if (64 - 0 < 0) {
                    z = false;
                }
                if (z) {
                    if (64 != byteString.getData$okio().length) {
                        byteString = new ByteString(ArraysKt.copyOfRange(byteString.getData$okio(), 0, 64));
                    }
                    return append.append(byteString.hex()).append("…]").toString();
                }
                throw new IllegalArgumentException("endIndex < beginIndex".toString());
            }
            throw new IllegalArgumentException(("endIndex > length(" + byteString.getData$okio().length + ')').toString());
        }
    }

    public static final String commonUtf8(ByteString $this$commonUtf8) {
        Intrinsics.checkNotNullParameter($this$commonUtf8, "$this$commonUtf8");
        String utf8$okio = $this$commonUtf8.getUtf8$okio();
        if (utf8$okio != null) {
            return utf8$okio;
        }
        String utf8String = Platform.toUtf8String($this$commonUtf8.internalArray$okio());
        Log1F380D.a((Object) utf8String);
        $this$commonUtf8.setUtf8$okio(utf8String);
        return utf8String;
    }
}

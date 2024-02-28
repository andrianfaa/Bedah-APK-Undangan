package okio;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.jvm.internal.Intrinsics;
import mt.Log1F380D;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\u001a\u000e\u0010\u0006\u001a\u0004\u0018\u00010\u0001*\u00020\u0007H\u0000\u001a\u0016\u0010\b\u001a\u00020\u0007*\u00020\u00012\b\b\u0002\u0010\t\u001a\u00020\u0001H\u0000\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u0014\u0010\u0004\u001a\u00020\u0001X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0003¨\u0006\n"}, d2 = {"BASE64", "", "getBASE64", "()[B", "BASE64_URL_SAFE", "getBASE64_URL_SAFE", "decodeBase64ToArray", "", "encodeBase64", "map", "okio"}, k = 2, mv = {1, 4, 0})
/* renamed from: okio.-Base64  reason: invalid class name */
/* compiled from: 01E8 */
public final class Base64 {
    private static final byte[] BASE64 = ByteString.Companion.encodeUtf8("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/").getData$okio();
    private static final byte[] BASE64_URL_SAFE = ByteString.Companion.encodeUtf8("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_").getData$okio();

    public static final byte[] decodeBase64ToArray(String $this$decodeBase64ToArray) {
        int i;
        Intrinsics.checkNotNullParameter($this$decodeBase64ToArray, "$this$decodeBase64ToArray");
        int length = $this$decodeBase64ToArray.length();
        while (length > 0 && ((r5 = $this$decodeBase64ToArray.charAt(length - 1)) == '=' || r5 == 10 || r5 == 13 || r5 == ' ' || r5 == 9)) {
            length--;
        }
        byte[] bArr = new byte[((int) ((((long) length) * 6) / 8))];
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < length; i5++) {
            char charAt = $this$decodeBase64ToArray.charAt(i5);
            if ('A' <= charAt && 'Z' >= charAt) {
                i = charAt - 'A';
            } else if ('a' <= charAt && 'z' >= charAt) {
                i = charAt - 'G';
            } else if ('0' <= charAt && '9' >= charAt) {
                i = charAt + 4;
            } else if (charAt == '+' || charAt == '-') {
                i = 62;
            } else if (charAt == '/' || charAt == '_') {
                i = 63;
            } else {
                if (!(charAt == 10 || charAt == 13 || charAt == ' ' || charAt == 9)) {
                    return null;
                }
            }
            i4 = (i4 << 6) | i;
            i3++;
            if (i3 % 4 == 0) {
                int i6 = i2 + 1;
                bArr[i2] = (byte) (i4 >> 16);
                int i7 = i6 + 1;
                bArr[i6] = (byte) (i4 >> 8);
                bArr[i7] = (byte) i4;
                i2 = i7 + 1;
            }
        }
        switch (i3 % 4) {
            case 1:
                return null;
            case 2:
                bArr[i2] = (byte) ((i4 << 12) >> 16);
                i2++;
                break;
            case 3:
                int i8 = i4 << 6;
                int i9 = i2 + 1;
                bArr[i2] = (byte) (i8 >> 16);
                i2 = i9 + 1;
                bArr[i9] = (byte) (i8 >> 8);
                break;
        }
        if (i2 == bArr.length) {
            return bArr;
        }
        byte[] copyOf = Arrays.copyOf(bArr, i2);
        Intrinsics.checkNotNullExpressionValue(copyOf, "java.util.Arrays.copyOf(this, newSize)");
        return copyOf;
    }

    public static final String encodeBase64(byte[] $this$encodeBase64, byte[] map) {
        Intrinsics.checkNotNullParameter($this$encodeBase64, "$this$encodeBase64");
        Intrinsics.checkNotNullParameter(map, "map");
        byte[] bArr = new byte[((($this$encodeBase64.length + 2) / 3) * 4)];
        int i = 0;
        int length = $this$encodeBase64.length - ($this$encodeBase64.length % 3);
        int i2 = 0;
        while (i2 < length) {
            int i3 = i2 + 1;
            byte b = $this$encodeBase64[i2];
            int i4 = i3 + 1;
            byte b2 = $this$encodeBase64[i3];
            int i5 = i4 + 1;
            byte b3 = $this$encodeBase64[i4];
            int i6 = i + 1;
            bArr[i] = map[(b & UByte.MAX_VALUE) >> 2];
            int i7 = i6 + 1;
            bArr[i6] = map[((b & 3) << 4) | ((b2 & UByte.MAX_VALUE) >> 4)];
            int i8 = i7 + 1;
            bArr[i7] = map[((b2 & 15) << 2) | ((b3 & UByte.MAX_VALUE) >> 6)];
            i = i8 + 1;
            bArr[i8] = map[b3 & Utf8.REPLACEMENT_BYTE];
            i2 = i5;
        }
        switch ($this$encodeBase64.length - length) {
            case 1:
                byte b4 = $this$encodeBase64[i2];
                int i9 = i + 1;
                bArr[i] = map[(b4 & UByte.MAX_VALUE) >> 2];
                int i10 = i9 + 1;
                bArr[i9] = map[(b4 & 3) << 4];
                int i11 = i10 + 1;
                byte b5 = (byte) 61;
                bArr[i10] = b5;
                bArr[i11] = b5;
                int i12 = i11;
                break;
            case 2:
                int i13 = i2 + 1;
                byte b6 = $this$encodeBase64[i2];
                byte b7 = $this$encodeBase64[i13];
                int i14 = i + 1;
                bArr[i] = map[(b6 & UByte.MAX_VALUE) >> 2];
                int i15 = i14 + 1;
                bArr[i14] = map[((b6 & 3) << 4) | ((b7 & UByte.MAX_VALUE) >> 4)];
                int i16 = i15 + 1;
                bArr[i15] = map[(b7 & 15) << 2];
                bArr[i16] = (byte) 61;
                int i17 = i13;
                int i18 = i16;
                break;
        }
        String utf8String = Platform.toUtf8String(bArr);
        Log1F380D.a((Object) utf8String);
        return utf8String;
    }

    public static final byte[] getBASE64() {
        return BASE64;
    }

    public static final byte[] getBASE64_URL_SAFE() {
        return BASE64_URL_SAFE;
    }

    public static /* synthetic */ String encodeBase64$default(byte[] bArr, byte[] bArr2, int i, Object obj) {
        if ((i & 1) != 0) {
            bArr2 = BASE64;
        }
        String encodeBase64 = encodeBase64(bArr, bArr2);
        Log1F380D.a((Object) encodeBase64);
        return encodeBase64;
    }
}

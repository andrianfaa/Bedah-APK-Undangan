package okhttp3.internal;

import java.net.IDN;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import mt.Log1F380D;
import okio.Buffer;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a0\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0005H\u0002\u001a\"\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0002\u001a\u0010\u0010\f\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0002\u001a\f\u0010\r\u001a\u00020\u0001*\u00020\u0003H\u0002\u001a\f\u0010\u000e\u001a\u0004\u0018\u00010\u0003*\u00020\u0003¨\u0006\u000f"}, d2 = {"decodeIpv4Suffix", "", "input", "", "pos", "", "limit", "address", "", "addressOffset", "decodeIpv6", "Ljava/net/InetAddress;", "inet6AddressToAscii", "containsInvalidHostnameAsciiCodes", "toCanonicalHost", "okhttp"}, k = 2, mv = {1, 4, 0})
/* compiled from: 01C1 */
public final class HostnamesKt {
    private static final boolean containsInvalidHostnameAsciiCodes(String $this$containsInvalidHostnameAsciiCodes) {
        int length = $this$containsInvalidHostnameAsciiCodes.length();
        for (int i = 0; i < length; i++) {
            char charAt = $this$containsInvalidHostnameAsciiCodes.charAt(i);
            if (Intrinsics.compare((int) charAt, 31) <= 0 || Intrinsics.compare((int) charAt, 127) >= 0 || StringsKt.indexOf$default((CharSequence) " #%/:?@[\\]", charAt, 0, false, 6, (Object) null) != -1) {
                return true;
            }
        }
        return false;
    }

    private static final boolean decodeIpv4Suffix(String input, int pos, int limit, byte[] address, int addressOffset) {
        int i = addressOffset;
        int i2 = pos;
        while (i2 < limit) {
            if (i == address.length) {
                return false;
            }
            if (i != addressOffset) {
                if (input.charAt(i2) != '.') {
                    return false;
                }
                i2++;
            }
            int i3 = 0;
            int i4 = i2;
            while (i2 < limit) {
                char charAt = input.charAt(i2);
                if (Intrinsics.compare((int) charAt, 48) < 0 || Intrinsics.compare((int) charAt, 57) > 0) {
                    break;
                } else if ((i3 == 0 && i4 != i2) || ((i3 * 10) + charAt) - 48 > 255) {
                    return false;
                } else {
                    i2++;
                }
            }
            if (i2 - i4 == 0) {
                return false;
            }
            address[i] = (byte) i3;
            i++;
        }
        return i == addressOffset + 4;
    }

    private static final InetAddress decodeIpv6(String input, int pos, int limit) {
        byte[] bArr = new byte[16];
        int i = 0;
        int i2 = -1;
        int i3 = -1;
        int i4 = pos;
        while (true) {
            if (i4 >= limit) {
                break;
            } else if (i == bArr.length) {
                return null;
            } else {
                if (i4 + 2 <= limit && StringsKt.startsWith$default(input, "::", i4, false, 4, (Object) null)) {
                    if (i2 == -1) {
                        i4 += 2;
                        i += 2;
                        i2 = i;
                        if (i4 == limit) {
                            break;
                        }
                    } else {
                        return null;
                    }
                } else if (i != 0) {
                    if (StringsKt.startsWith$default(input, ":", i4, false, 4, (Object) null)) {
                        i4++;
                    } else if (!StringsKt.startsWith$default(input, ".", i4, false, 4, (Object) null) || !decodeIpv4Suffix(input, i3, limit, bArr, i - 2)) {
                        return null;
                    } else {
                        i += 2;
                    }
                }
                int i5 = 0;
                i3 = i4;
                while (i4 < limit) {
                    int parseHexDigit = Util.parseHexDigit(input.charAt(i4));
                    if (parseHexDigit == -1) {
                        break;
                    }
                    i5 = (i5 << 4) + parseHexDigit;
                    i4++;
                }
                int i6 = i4 - i3;
                if (i6 == 0 || i6 > 4) {
                    return null;
                }
                int i7 = i + 1;
                bArr[i] = (byte) ((i5 >>> 8) & 255);
                i = i7 + 1;
                bArr[i7] = (byte) (i5 & 255);
            }
        }
        if (i != bArr.length) {
            if (i2 == -1) {
                return null;
            }
            System.arraycopy(bArr, i2, bArr, bArr.length - (i - i2), i - i2);
            Arrays.fill(bArr, i2, (bArr.length - i) + i2, (byte) 0);
        }
        return InetAddress.getByAddress(bArr);
    }

    private static final String inet6AddressToAscii(byte[] address) {
        int i = -1;
        int i2 = 0;
        int i3 = 0;
        while (i3 < address.length) {
            int i4 = i3;
            while (i3 < 16 && address[i3] == 0 && address[i3 + 1] == 0) {
                i3 += 2;
            }
            int i5 = i3 - i4;
            if (i5 > i2 && i5 >= 4) {
                i = i4;
                i2 = i5;
            }
            i3 += 2;
        }
        Buffer buffer = new Buffer();
        int i6 = 0;
        while (i6 < address.length) {
            if (i6 == i) {
                buffer.writeByte(58);
                i6 += i2;
                if (i6 == 16) {
                    buffer.writeByte(58);
                }
            } else {
                if (i6 > 0) {
                    buffer.writeByte(58);
                }
                buffer.writeHexadecimalUnsignedLong((long) ((Util.and(address[i6], 255) << 8) | Util.and(address[i6 + 1], 255)));
                i6 += 2;
            }
        }
        return buffer.readUtf8();
    }

    public static final String toCanonicalHost(String $this$toCanonicalHost) {
        Intrinsics.checkNotNullParameter($this$toCanonicalHost, "$this$toCanonicalHost");
        String str = $this$toCanonicalHost;
        boolean z = false;
        if (StringsKt.contains$default((CharSequence) str, (CharSequence) ":", false, 2, (Object) null)) {
            InetAddress decodeIpv6 = (!StringsKt.startsWith$default(str, "[", false, 2, (Object) null) || !StringsKt.endsWith$default(str, "]", false, 2, (Object) null)) ? decodeIpv6(str, 0, str.length()) : decodeIpv6(str, 1, str.length() - 1);
            if (decodeIpv6 == null) {
                return null;
            }
            byte[] address = decodeIpv6.getAddress();
            if (address.length == 16) {
                Intrinsics.checkNotNullExpressionValue(address, "address");
                String inet6AddressToAscii = inet6AddressToAscii(address);
                Log1F380D.a((Object) inet6AddressToAscii);
                return inet6AddressToAscii;
            } else if (address.length == 4) {
                return decodeIpv6.getHostAddress();
            } else {
                throw new AssertionError("Invalid IPv6 address: '" + str + '\'');
            }
        } else {
            try {
                String ascii = IDN.toASCII(str);
                Log1F380D.a((Object) ascii);
                Intrinsics.checkNotNullExpressionValue(ascii, "IDN.toASCII(host)");
                Locale locale = Locale.US;
                Intrinsics.checkNotNullExpressionValue(locale, "Locale.US");
                if (ascii != null) {
                    String lowerCase = ascii.toLowerCase(locale);
                    Intrinsics.checkNotNullExpressionValue(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
                    if (lowerCase.length() == 0) {
                        z = true;
                    }
                    if (z) {
                        return null;
                    }
                    if (containsInvalidHostnameAsciiCodes(lowerCase)) {
                        return null;
                    }
                    return lowerCase;
                }
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}

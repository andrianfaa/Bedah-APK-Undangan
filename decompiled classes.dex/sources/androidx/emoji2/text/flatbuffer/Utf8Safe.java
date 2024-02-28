package androidx.emoji2.text.flatbuffer;

import androidx.emoji2.text.flatbuffer.Utf8;
import java.nio.ByteBuffer;
import mt.Log1F380D;

/* compiled from: 0073 */
public final class Utf8Safe extends Utf8 {

    static class UnpairedSurrogateException extends IllegalArgumentException {
        UnpairedSurrogateException(int index, int length) {
            super("Unpaired surrogate at index " + index + " of " + length);
        }
    }

    private static int computeEncodedLength(CharSequence sequence) {
        int length = sequence.length();
        int i = length;
        int i2 = 0;
        while (i2 < length && sequence.charAt(i2) < 128) {
            i2++;
        }
        while (true) {
            if (i2 < length) {
                char charAt = sequence.charAt(i2);
                if (charAt >= 2048) {
                    i += encodedLengthGeneral(sequence, i2);
                    break;
                }
                i += (127 - charAt) >>> 31;
                i2++;
            } else {
                break;
            }
        }
        if (i >= length) {
            return i;
        }
        throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (((long) i) + 4294967296L));
    }

    public static String decodeUtf8Array(byte[] bytes, int index, int size) {
        int i;
        if ((index | size | ((bytes.length - index) - size)) >= 0) {
            int i2 = index;
            int i3 = i2 + size;
            char[] cArr = new char[size];
            int i4 = 0;
            while (i < i3) {
                byte b = bytes[i];
                if (!Utf8.DecodeUtil.isOneByte(b)) {
                    break;
                }
                i2 = i + 1;
                Utf8.DecodeUtil.handleOneByte(b, cArr, i4);
                i4++;
            }
            int i5 = i4;
            while (i < i3) {
                int i6 = i + 1;
                byte b2 = bytes[i];
                if (Utf8.DecodeUtil.isOneByte(b2)) {
                    int i7 = i5 + 1;
                    Utf8.DecodeUtil.handleOneByte(b2, cArr, i5);
                    while (i6 < i3) {
                        byte b3 = bytes[i6];
                        if (!Utf8.DecodeUtil.isOneByte(b3)) {
                            break;
                        }
                        i6++;
                        Utf8.DecodeUtil.handleOneByte(b3, cArr, i7);
                        i7++;
                    }
                    i = i6;
                    i5 = i7;
                } else if (Utf8.DecodeUtil.isTwoBytes(b2)) {
                    if (i6 < i3) {
                        Utf8.DecodeUtil.handleTwoBytes(b2, bytes[i6], cArr, i5);
                        i = i6 + 1;
                        i5++;
                    } else {
                        throw new IllegalArgumentException("Invalid UTF-8");
                    }
                } else if (Utf8.DecodeUtil.isThreeBytes(b2)) {
                    if (i6 < i3 - 1) {
                        int i8 = i6 + 1;
                        Utf8.DecodeUtil.handleThreeBytes(b2, bytes[i6], bytes[i8], cArr, i5);
                        i = i8 + 1;
                        i5++;
                    } else {
                        throw new IllegalArgumentException("Invalid UTF-8");
                    }
                } else if (i6 < i3 - 2) {
                    int i9 = i6 + 1;
                    byte b4 = bytes[i6];
                    int i10 = i9 + 1;
                    Utf8.DecodeUtil.handleFourBytes(b2, b4, bytes[i9], bytes[i10], cArr, i5);
                    i = i10 + 1;
                    i5 = i5 + 1 + 1;
                } else {
                    throw new IllegalArgumentException("Invalid UTF-8");
                }
            }
            String str = new String(cArr, 0, i5);
            Log1F380D.a((Object) str);
            return str;
        }
        String format = String.format("buffer length=%d, index=%d, size=%d", new Object[]{Integer.valueOf(bytes.length), Integer.valueOf(index), Integer.valueOf(size)});
        Log1F380D.a((Object) format);
        throw new ArrayIndexOutOfBoundsException(format);
    }

    private static int encodeUtf8Array(CharSequence in, byte[] out, int offset, int length) {
        int length2 = in.length();
        int i = offset;
        int i2 = 0;
        int i3 = offset + length;
        while (i2 < length2 && i2 + i < i3) {
            char charAt = in.charAt(i2);
            char c = charAt;
            if (charAt >= 128) {
                break;
            }
            out[i + i2] = (byte) c;
            i2++;
        }
        if (i2 == length2) {
            return i + length2;
        }
        int i4 = i + i2;
        while (i2 < length2) {
            char charAt2 = in.charAt(i2);
            if (charAt2 < 128 && i4 < i3) {
                out[i4] = (byte) charAt2;
                i4++;
            } else if (charAt2 < 2048 && i4 <= i3 - 2) {
                int i5 = i4 + 1;
                out[i4] = (byte) ((charAt2 >>> 6) | 960);
                i4 = i5 + 1;
                out[i5] = (byte) ((charAt2 & '?') | 128);
            } else if ((charAt2 < 55296 || 57343 < charAt2) && i4 <= i3 - 3) {
                int i6 = i4 + 1;
                out[i4] = (byte) ((charAt2 >>> 12) | 480);
                int i7 = i6 + 1;
                out[i6] = (byte) (((charAt2 >>> 6) & 63) | 128);
                out[i7] = (byte) ((charAt2 & '?') | 128);
                i4 = i7 + 1;
            } else if (i4 <= i3 - 4) {
                if (i2 + 1 != in.length()) {
                    i2++;
                    char charAt3 = in.charAt(i2);
                    char c2 = charAt3;
                    if (Character.isSurrogatePair(charAt2, charAt3)) {
                        int codePoint = Character.toCodePoint(charAt2, c2);
                        int i8 = i4 + 1;
                        out[i4] = (byte) ((codePoint >>> 18) | 240);
                        int i9 = i8 + 1;
                        out[i8] = (byte) (((codePoint >>> 12) & 63) | 128);
                        int i10 = i9 + 1;
                        out[i9] = (byte) (((codePoint >>> 6) & 63) | 128);
                        i4 = i10 + 1;
                        out[i10] = (byte) ((codePoint & 63) | 128);
                    }
                }
                throw new UnpairedSurrogateException(i2 - 1, length2);
            } else if (55296 > charAt2 || charAt2 > 57343 || (i2 + 1 != in.length() && Character.isSurrogatePair(charAt2, in.charAt(i2 + 1)))) {
                throw new ArrayIndexOutOfBoundsException("Failed writing " + charAt2 + " at index " + i4);
            } else {
                throw new UnpairedSurrogateException(i2, length2);
            }
            i2++;
        }
        return i4;
    }

    private static void encodeUtf8Buffer(CharSequence in, ByteBuffer out) {
        int i;
        int length = in.length();
        int position = out.position();
        int i2 = 0;
        while (i2 < length) {
            try {
                char charAt = in.charAt(i2);
                char c = charAt;
                if (charAt >= 128) {
                    break;
                }
                out.put(position + i2, (byte) c);
                i2++;
            } catch (IndexOutOfBoundsException e) {
                throw new ArrayIndexOutOfBoundsException("Failed writing " + in.charAt(i2) + " at index " + (out.position() + Math.max(i2, (position - out.position()) + 1)));
            }
        }
        if (i2 == length) {
            out.position(position + i2);
            return;
        }
        position += i2;
        while (i2 < length) {
            char charAt2 = in.charAt(i2);
            if (charAt2 < 128) {
                out.put(position, (byte) charAt2);
            } else if (charAt2 < 2048) {
                i = position + 1;
                try {
                    out.put(position, (byte) ((charAt2 >>> 6) | 192));
                    out.put(i, (byte) ((charAt2 & '?') | 128));
                    position = i;
                } catch (IndexOutOfBoundsException e2) {
                    position = i;
                    throw new ArrayIndexOutOfBoundsException("Failed writing " + in.charAt(i2) + " at index " + (out.position() + Math.max(i2, (position - out.position()) + 1)));
                }
            } else if (charAt2 < 55296 || 57343 < charAt2) {
                i = position + 1;
                out.put(position, (byte) ((charAt2 >>> 12) | 224));
                position = i + 1;
                out.put(i, (byte) (((charAt2 >>> 6) & 63) | 128));
                out.put(position, (byte) ((charAt2 & '?') | 128));
            } else {
                if (i2 + 1 != length) {
                    i2++;
                    char charAt3 = in.charAt(i2);
                    char c2 = charAt3;
                    if (Character.isSurrogatePair(charAt2, charAt3)) {
                        int codePoint = Character.toCodePoint(charAt2, c2);
                        int i3 = position + 1;
                        try {
                            out.put(position, (byte) ((codePoint >>> 18) | 240));
                            position = i3 + 1;
                            out.put(i3, (byte) (((codePoint >>> 12) & 63) | 128));
                            i3 = position + 1;
                            out.put(position, (byte) (((codePoint >>> 6) & 63) | 128));
                            out.put(i3, (byte) ((codePoint & 63) | 128));
                            position = i3;
                        } catch (IndexOutOfBoundsException e3) {
                            position = i3;
                            throw new ArrayIndexOutOfBoundsException("Failed writing " + in.charAt(i2) + " at index " + (out.position() + Math.max(i2, (position - out.position()) + 1)));
                        }
                    }
                }
                throw new UnpairedSurrogateException(i2, length);
            }
            i2++;
            position++;
        }
        out.position(position);
    }

    private static int encodedLengthGeneral(CharSequence sequence, int start) {
        int length = sequence.length();
        int i = 0;
        int i2 = start;
        while (i2 < length) {
            char charAt = sequence.charAt(i2);
            if (charAt < 2048) {
                i += (127 - charAt) >>> 31;
            } else {
                i += 2;
                if (55296 <= charAt && charAt <= 57343) {
                    if (Character.codePointAt(sequence, i2) >= 65536) {
                        i2++;
                    } else {
                        throw new UnpairedSurrogateException(i2, length);
                    }
                }
            }
            i2++;
        }
        return i;
    }

    public void encodeUtf8(CharSequence in, ByteBuffer out) {
        if (out.hasArray()) {
            int arrayOffset = out.arrayOffset();
            out.position(encodeUtf8Array(in, out.array(), out.position() + arrayOffset, out.remaining()) - arrayOffset);
            return;
        }
        encodeUtf8Buffer(in, out);
    }

    public int encodedLength(CharSequence in) {
        return computeEncodedLength(in);
    }

    public static String decodeUtf8Buffer(ByteBuffer buffer, int offset, int length) {
        if ((offset | length | ((buffer.limit() - offset) - length)) >= 0) {
            int i = offset + length;
            char[] cArr = new char[length];
            int i2 = 0;
            while (offset < i) {
                byte b = buffer.get(offset);
                if (!Utf8.DecodeUtil.isOneByte(b)) {
                    break;
                }
                offset = offset + 1;
                Utf8.DecodeUtil.handleOneByte(b, cArr, i2);
                i2++;
            }
            int i3 = i2;
            while (offset < i) {
                int i4 = offset + 1;
                byte b2 = buffer.get(offset);
                if (Utf8.DecodeUtil.isOneByte(b2)) {
                    int i5 = i3 + 1;
                    Utf8.DecodeUtil.handleOneByte(b2, cArr, i3);
                    while (i4 < i) {
                        byte b3 = buffer.get(i4);
                        if (!Utf8.DecodeUtil.isOneByte(b3)) {
                            break;
                        }
                        i4++;
                        Utf8.DecodeUtil.handleOneByte(b3, cArr, i5);
                        i5++;
                    }
                    offset = i4;
                    i3 = i5;
                } else if (Utf8.DecodeUtil.isTwoBytes(b2)) {
                    if (i4 < i) {
                        Utf8.DecodeUtil.handleTwoBytes(b2, buffer.get(i4), cArr, i3);
                        offset = i4 + 1;
                        i3++;
                    } else {
                        throw new IllegalArgumentException("Invalid UTF-8");
                    }
                } else if (Utf8.DecodeUtil.isThreeBytes(b2)) {
                    if (i4 < i - 1) {
                        int i6 = i4 + 1;
                        Utf8.DecodeUtil.handleThreeBytes(b2, buffer.get(i4), buffer.get(i6), cArr, i3);
                        offset = i6 + 1;
                        i3++;
                    } else {
                        throw new IllegalArgumentException("Invalid UTF-8");
                    }
                } else if (i4 < i - 2) {
                    int i7 = i4 + 1;
                    byte b4 = buffer.get(i4);
                    int i8 = i7 + 1;
                    Utf8.DecodeUtil.handleFourBytes(b2, b4, buffer.get(i7), buffer.get(i8), cArr, i3);
                    offset = i8 + 1;
                    i3 = i3 + 1 + 1;
                } else {
                    throw new IllegalArgumentException("Invalid UTF-8");
                }
            }
            String str = new String(cArr, 0, i3);
            Log1F380D.a((Object) str);
            return str;
        }
        String format = String.format("buffer limit=%d, index=%d, limit=%d", new Object[]{Integer.valueOf(buffer.limit()), Integer.valueOf(offset), Integer.valueOf(length)});
        Log1F380D.a((Object) format);
        throw new ArrayIndexOutOfBoundsException(format);
    }

    public String decodeUtf8(ByteBuffer buffer, int offset, int length) throws IllegalArgumentException {
        if (buffer.hasArray()) {
            String decodeUtf8Array = decodeUtf8Array(buffer.array(), buffer.arrayOffset() + offset, length);
            Log1F380D.a((Object) decodeUtf8Array);
            return decodeUtf8Array;
        }
        String decodeUtf8Buffer = decodeUtf8Buffer(buffer, offset, length);
        Log1F380D.a((Object) decodeUtf8Buffer);
        return decodeUtf8Buffer;
    }
}

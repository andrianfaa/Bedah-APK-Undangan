package okhttp3.internal.ws;

import java.io.IOException;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import mt.Log1F380D;
import okhttp3.Headers;
import okhttp3.internal.Util;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0015\n\u0002\u0010\u000e\n\u0002\b\u0002\b\b\u0018\u0000 \u001c2\u00020\u0001:\u0001\u001cBE\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003¢\u0006\u0002\u0010\nJ\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\u0010\u0010\r\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u000eJ\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u000eJ\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003JN\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u0003HÆ\u0001¢\u0006\u0002\u0010\u0014J\u0013\u0010\u0015\u001a\u00020\u00032\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0017\u001a\u00020\u0005HÖ\u0001J\u000e\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u0003J\t\u0010\u001a\u001a\u00020\u001bHÖ\u0001R\u0014\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006X\u0004¢\u0006\u0004\n\u0002\u0010\u000bR\u0010\u0010\u0006\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u0004\u0018\u00010\u00058\u0006X\u0004¢\u0006\u0004\n\u0002\u0010\u000bR\u0010\u0010\b\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lokhttp3/internal/ws/WebSocketExtensions;", "", "perMessageDeflate", "", "clientMaxWindowBits", "", "clientNoContextTakeover", "serverMaxWindowBits", "serverNoContextTakeover", "unknownValues", "(ZLjava/lang/Integer;ZLjava/lang/Integer;ZZ)V", "Ljava/lang/Integer;", "component1", "component2", "()Ljava/lang/Integer;", "component3", "component4", "component5", "component6", "copy", "(ZLjava/lang/Integer;ZLjava/lang/Integer;ZZ)Lokhttp3/internal/ws/WebSocketExtensions;", "equals", "other", "hashCode", "noContextTakeover", "clientOriginated", "toString", "", "Companion", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: WebSocketExtensions.kt */
public final class WebSocketExtensions {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String HEADER_WEB_SOCKET_EXTENSION = "Sec-WebSocket-Extensions";
    public final Integer clientMaxWindowBits;
    public final boolean clientNoContextTakeover;
    public final boolean perMessageDeflate;
    public final Integer serverMaxWindowBits;
    public final boolean serverNoContextTakeover;
    public final boolean unknownValues;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lokhttp3/internal/ws/WebSocketExtensions$Companion;", "", "()V", "HEADER_WEB_SOCKET_EXTENSION", "", "parse", "Lokhttp3/internal/ws/WebSocketExtensions;", "responseHeaders", "Lokhttp3/Headers;", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: 01E6 */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        public final WebSocketExtensions parse(Headers responseHeaders) throws IOException {
            boolean z;
            String str;
            boolean z2;
            String str2;
            boolean z3;
            boolean z4;
            Headers headers = responseHeaders;
            Intrinsics.checkNotNullParameter(headers, "responseHeaders");
            boolean z5 = false;
            Integer num = null;
            boolean z6 = false;
            Integer num2 = null;
            boolean z7 = false;
            boolean z8 = false;
            int size = responseHeaders.size();
            int i = 0;
            while (i < size) {
                boolean z9 = true;
                if (StringsKt.equals(headers.name(i), WebSocketExtensions.HEADER_WEB_SOCKET_EXTENSION, true)) {
                    String value = headers.value(i);
                    int i2 = 0;
                    while (i2 < value.length()) {
                        int delimiterOffset$default = Util.delimiterOffset$default(value, ',', i2, 0, 4, (Object) null);
                        int delimiterOffset = Util.delimiterOffset(value, ';', i2, delimiterOffset$default);
                        String trimSubstring = Util.trimSubstring(value, i2, delimiterOffset);
                        Log1F380D.a((Object) trimSubstring);
                        i2 = delimiterOffset + 1;
                        if (StringsKt.equals(trimSubstring, "permessage-deflate", z9)) {
                            if (z5) {
                                z8 = true;
                            }
                            z5 = true;
                            while (i2 < delimiterOffset$default) {
                                int delimiterOffset2 = Util.delimiterOffset(value, ';', i2, delimiterOffset$default);
                                int delimiterOffset3 = Util.delimiterOffset(value, '=', i2, delimiterOffset2);
                                String trimSubstring2 = Util.trimSubstring(value, i2, delimiterOffset3);
                                Log1F380D.a((Object) trimSubstring2);
                                if (delimiterOffset3 < delimiterOffset2) {
                                    z2 = z5;
                                    String trimSubstring3 = Util.trimSubstring(value, delimiterOffset3 + 1, delimiterOffset2);
                                    Log1F380D.a((Object) trimSubstring3);
                                    str = trimSubstring;
                                    str2 = StringsKt.removeSurrounding(trimSubstring3, (CharSequence) "\"");
                                    Log1F380D.a((Object) str2);
                                } else {
                                    z2 = z5;
                                    str = trimSubstring;
                                    str2 = null;
                                }
                                i2 = delimiterOffset2 + 1;
                                boolean z10 = z8;
                                if (StringsKt.equals(trimSubstring2, "client_max_window_bits", true)) {
                                    boolean z11 = num != null ? true : z10;
                                    Integer intOrNull = str2 != null ? StringsKt.toIntOrNull(str2) : null;
                                    if (intOrNull == null) {
                                        z3 = true;
                                        num = intOrNull;
                                        z4 = true;
                                    } else {
                                        num = intOrNull;
                                        z4 = z11;
                                        z3 = true;
                                    }
                                } else if (StringsKt.equals(trimSubstring2, "client_no_context_takeover", true)) {
                                    boolean z12 = z6 ? true : z10;
                                    if (str2 != null) {
                                        z12 = true;
                                    }
                                    z6 = true;
                                    z4 = z12;
                                    z3 = true;
                                } else if (StringsKt.equals(trimSubstring2, "server_max_window_bits", true)) {
                                    boolean z13 = num2 != null ? true : z10;
                                    Integer intOrNull2 = str2 != null ? StringsKt.toIntOrNull(str2) : null;
                                    if (intOrNull2 == null) {
                                        z3 = true;
                                        num2 = intOrNull2;
                                        z4 = true;
                                    } else {
                                        num2 = intOrNull2;
                                        z4 = z13;
                                        z3 = true;
                                    }
                                } else {
                                    z3 = true;
                                    if (StringsKt.equals(trimSubstring2, "server_no_context_takeover", true)) {
                                        z4 = z7 ? true : z10;
                                        if (str2 != null) {
                                            z4 = true;
                                        }
                                        z7 = true;
                                    } else {
                                        z4 = true;
                                    }
                                }
                                Headers headers2 = responseHeaders;
                                z9 = z3;
                                z5 = z2;
                                z8 = z4;
                                trimSubstring = str;
                            }
                            boolean z14 = z5;
                            String str3 = trimSubstring;
                            z = z9;
                            z8 = z8;
                        } else {
                            String str4 = trimSubstring;
                            z = z9;
                            z8 = true;
                        }
                        z9 = z;
                        Headers headers3 = responseHeaders;
                    }
                }
                i++;
                headers = responseHeaders;
            }
            return new WebSocketExtensions(z5, num, z6, num2, z7, z8);
        }
    }

    public WebSocketExtensions() {
        this(false, (Integer) null, false, (Integer) null, false, false, 63, (DefaultConstructorMarker) null);
    }

    public WebSocketExtensions(boolean perMessageDeflate2, Integer clientMaxWindowBits2, boolean clientNoContextTakeover2, Integer serverMaxWindowBits2, boolean serverNoContextTakeover2, boolean unknownValues2) {
        this.perMessageDeflate = perMessageDeflate2;
        this.clientMaxWindowBits = clientMaxWindowBits2;
        this.clientNoContextTakeover = clientNoContextTakeover2;
        this.serverMaxWindowBits = serverMaxWindowBits2;
        this.serverNoContextTakeover = serverNoContextTakeover2;
        this.unknownValues = unknownValues2;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ WebSocketExtensions(boolean r6, java.lang.Integer r7, boolean r8, java.lang.Integer r9, boolean r10, boolean r11, int r12, kotlin.jvm.internal.DefaultConstructorMarker r13) {
        /*
            r5 = this;
            r13 = r12 & 1
            r0 = 0
            if (r13 == 0) goto L_0x0007
            r13 = r0
            goto L_0x0008
        L_0x0007:
            r13 = r6
        L_0x0008:
            r6 = r12 & 2
            r1 = 0
            if (r6 == 0) goto L_0x0012
            r6 = r1
            java.lang.Integer r6 = (java.lang.Integer) r6
            r2 = r1
            goto L_0x0013
        L_0x0012:
            r2 = r7
        L_0x0013:
            r6 = r12 & 4
            if (r6 == 0) goto L_0x0019
            r3 = r0
            goto L_0x001a
        L_0x0019:
            r3 = r8
        L_0x001a:
            r6 = r12 & 8
            if (r6 == 0) goto L_0x0022
            r6 = r1
            java.lang.Integer r6 = (java.lang.Integer) r6
            goto L_0x0023
        L_0x0022:
            r1 = r9
        L_0x0023:
            r6 = r12 & 16
            if (r6 == 0) goto L_0x0029
            r4 = r0
            goto L_0x002a
        L_0x0029:
            r4 = r10
        L_0x002a:
            r6 = r12 & 32
            if (r6 == 0) goto L_0x0030
            r12 = r0
            goto L_0x0031
        L_0x0030:
            r12 = r11
        L_0x0031:
            r6 = r5
            r7 = r13
            r8 = r2
            r9 = r3
            r10 = r1
            r11 = r4
            r6.<init>(r7, r8, r9, r10, r11, r12)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.ws.WebSocketExtensions.<init>(boolean, java.lang.Integer, boolean, java.lang.Integer, boolean, boolean, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public static /* synthetic */ WebSocketExtensions copy$default(WebSocketExtensions webSocketExtensions, boolean z, Integer num, boolean z2, Integer num2, boolean z3, boolean z4, int i, Object obj) {
        if ((i & 1) != 0) {
            z = webSocketExtensions.perMessageDeflate;
        }
        if ((i & 2) != 0) {
            num = webSocketExtensions.clientMaxWindowBits;
        }
        Integer num3 = num;
        if ((i & 4) != 0) {
            z2 = webSocketExtensions.clientNoContextTakeover;
        }
        boolean z5 = z2;
        if ((i & 8) != 0) {
            num2 = webSocketExtensions.serverMaxWindowBits;
        }
        Integer num4 = num2;
        if ((i & 16) != 0) {
            z3 = webSocketExtensions.serverNoContextTakeover;
        }
        boolean z6 = z3;
        if ((i & 32) != 0) {
            z4 = webSocketExtensions.unknownValues;
        }
        return webSocketExtensions.copy(z, num3, z5, num4, z6, z4);
    }

    public final boolean component1() {
        return this.perMessageDeflate;
    }

    public final Integer component2() {
        return this.clientMaxWindowBits;
    }

    public final boolean component3() {
        return this.clientNoContextTakeover;
    }

    public final Integer component4() {
        return this.serverMaxWindowBits;
    }

    public final boolean component5() {
        return this.serverNoContextTakeover;
    }

    public final boolean component6() {
        return this.unknownValues;
    }

    public final WebSocketExtensions copy(boolean z, Integer num, boolean z2, Integer num2, boolean z3, boolean z4) {
        return new WebSocketExtensions(z, num, z2, num2, z3, z4);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WebSocketExtensions)) {
            return false;
        }
        WebSocketExtensions webSocketExtensions = (WebSocketExtensions) obj;
        return this.perMessageDeflate == webSocketExtensions.perMessageDeflate && Intrinsics.areEqual((Object) this.clientMaxWindowBits, (Object) webSocketExtensions.clientMaxWindowBits) && this.clientNoContextTakeover == webSocketExtensions.clientNoContextTakeover && Intrinsics.areEqual((Object) this.serverMaxWindowBits, (Object) webSocketExtensions.serverMaxWindowBits) && this.serverNoContextTakeover == webSocketExtensions.serverNoContextTakeover && this.unknownValues == webSocketExtensions.unknownValues;
    }

    public int hashCode() {
        boolean z = this.perMessageDeflate;
        boolean z2 = true;
        if (z) {
            z = true;
        }
        int i = (z ? 1 : 0) * true;
        Integer num = this.clientMaxWindowBits;
        int i2 = 0;
        int hashCode = (i + (num != null ? num.hashCode() : 0)) * 31;
        boolean z3 = this.clientNoContextTakeover;
        if (z3) {
            z3 = true;
        }
        int i3 = (hashCode + (z3 ? 1 : 0)) * 31;
        Integer num2 = this.serverMaxWindowBits;
        if (num2 != null) {
            i2 = num2.hashCode();
        }
        int i4 = (i3 + i2) * 31;
        boolean z4 = this.serverNoContextTakeover;
        if (z4) {
            z4 = true;
        }
        int i5 = (i4 + (z4 ? 1 : 0)) * 31;
        boolean z5 = this.unknownValues;
        if (!z5) {
            z2 = z5;
        }
        return i5 + (z2 ? 1 : 0);
    }

    public final boolean noContextTakeover(boolean clientOriginated) {
        return clientOriginated ? this.clientNoContextTakeover : this.serverNoContextTakeover;
    }

    public String toString() {
        return "WebSocketExtensions(perMessageDeflate=" + this.perMessageDeflate + ", clientMaxWindowBits=" + this.clientMaxWindowBits + ", clientNoContextTakeover=" + this.clientNoContextTakeover + ", serverMaxWindowBits=" + this.serverMaxWindowBits + ", serverNoContextTakeover=" + this.serverNoContextTakeover + ", unknownValues=" + this.unknownValues + ")";
    }
}

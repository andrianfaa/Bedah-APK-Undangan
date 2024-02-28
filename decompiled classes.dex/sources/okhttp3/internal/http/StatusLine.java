package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.HttpUrl;
import okhttp3.Protocol;
import okhttp3.Response;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u0000 \n2\u00020\u0001:\u0001\nB\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\b\u0010\t\u001a\u00020\u0007H\u0016R\u0010\u0010\u0004\u001a\u00020\u00058\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u00020\u00078\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lokhttp3/internal/http/StatusLine;", "", "protocol", "Lokhttp3/Protocol;", "code", "", "message", "", "(Lokhttp3/Protocol;ILjava/lang/String;)V", "toString", "Companion", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: StatusLine.kt */
public final class StatusLine {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final int HTTP_CONTINUE = 100;
    public static final int HTTP_MISDIRECTED_REQUEST = 421;
    public static final int HTTP_PERM_REDIRECT = 308;
    public static final int HTTP_TEMP_REDIRECT = 307;
    public final int code;
    public final String message;
    public final Protocol protocol;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u000eR\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lokhttp3/internal/http/StatusLine$Companion;", "", "()V", "HTTP_CONTINUE", "", "HTTP_MISDIRECTED_REQUEST", "HTTP_PERM_REDIRECT", "HTTP_TEMP_REDIRECT", "get", "Lokhttp3/internal/http/StatusLine;", "response", "Lokhttp3/Response;", "parse", "statusLine", "", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: StatusLine.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        public final StatusLine get(Response response) {
            Intrinsics.checkNotNullParameter(response, "response");
            return new StatusLine(response.protocol(), response.code(), response.message());
        }

        public final StatusLine parse(String statusLine) throws IOException {
            Protocol protocol;
            int i;
            Protocol protocol2;
            Intrinsics.checkNotNullParameter(statusLine, "statusLine");
            if (StringsKt.startsWith$default(statusLine, "HTTP/1.", false, 2, (Object) null)) {
                if (statusLine.length() < 9 || statusLine.charAt(8) != ' ') {
                    throw new ProtocolException("Unexpected status line: " + statusLine);
                }
                int charAt = statusLine.charAt(7) - '0';
                i = 9;
                if (charAt == 0) {
                    protocol2 = Protocol.HTTP_1_0;
                } else if (charAt == 1) {
                    protocol2 = Protocol.HTTP_1_1;
                } else {
                    throw new ProtocolException("Unexpected status line: " + statusLine);
                }
                protocol = protocol2;
            } else if (StringsKt.startsWith$default(statusLine, "ICY ", false, 2, (Object) null)) {
                protocol = Protocol.HTTP_1_0;
                i = 4;
            } else {
                throw new ProtocolException("Unexpected status line: " + statusLine);
            }
            if (statusLine.length() >= i + 3) {
                try {
                    String substring = statusLine.substring(i, i + 3);
                    Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    int parseInt = Integer.parseInt(substring);
                    String str = HttpUrl.FRAGMENT_ENCODE_SET;
                    if (statusLine.length() > i + 3) {
                        if (statusLine.charAt(i + 3) == ' ') {
                            String substring2 = statusLine.substring(i + 4);
                            Intrinsics.checkNotNullExpressionValue(substring2, "(this as java.lang.String).substring(startIndex)");
                            str = substring2;
                        } else {
                            throw new ProtocolException("Unexpected status line: " + statusLine);
                        }
                    }
                    return new StatusLine(protocol, parseInt, str);
                } catch (NumberFormatException e) {
                    throw new ProtocolException("Unexpected status line: " + statusLine);
                }
            } else {
                throw new ProtocolException("Unexpected status line: " + statusLine);
            }
        }
    }

    public StatusLine(Protocol protocol2, int code2, String message2) {
        Intrinsics.checkNotNullParameter(protocol2, "protocol");
        Intrinsics.checkNotNullParameter(message2, "message");
        this.protocol = protocol2;
        this.code = code2;
        this.message = message2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = sb;
        if (this.protocol == Protocol.HTTP_1_0) {
            sb2.append("HTTP/1.0");
        } else {
            sb2.append("HTTP/1.1");
        }
        sb2.append(' ').append(this.code);
        sb2.append(' ').append(this.message);
        String sb3 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb3, "StringBuilder().apply(builderAction).toString()");
        return sb3;
    }
}

package okhttp3;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\b\u0001\u0018\u0000 \f2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\fB\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\r\u0010\u0002\u001a\u00020\u0003H\u0007¢\u0006\u0002\b\u0006R\u0013\u0010\u0002\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0005j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000b¨\u0006\r"}, d2 = {"Lokhttp3/TlsVersion;", "", "javaName", "", "(Ljava/lang/String;ILjava/lang/String;)V", "()Ljava/lang/String;", "-deprecated_javaName", "TLS_1_3", "TLS_1_2", "TLS_1_1", "TLS_1_0", "SSL_3_0", "Companion", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: TlsVersion.kt */
public enum TlsVersion {
    TLS_1_3("TLSv1.3"),
    TLS_1_2("TLSv1.2"),
    TLS_1_1("TLSv1.1"),
    TLS_1_0("TLSv1"),
    SSL_3_0("SSLv3");
    
    public static final Companion Companion = null;
    private final String javaName;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007¨\u0006\u0007"}, d2 = {"Lokhttp3/TlsVersion$Companion;", "", "()V", "forJavaName", "Lokhttp3/TlsVersion;", "javaName", "", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: TlsVersion.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        @JvmStatic
        public final TlsVersion forJavaName(String javaName) {
            Intrinsics.checkNotNullParameter(javaName, "javaName");
            switch (javaName.hashCode()) {
                case -503070503:
                    if (javaName.equals("TLSv1.1")) {
                        return TlsVersion.TLS_1_1;
                    }
                    break;
                case -503070502:
                    if (javaName.equals("TLSv1.2")) {
                        return TlsVersion.TLS_1_2;
                    }
                    break;
                case -503070501:
                    if (javaName.equals("TLSv1.3")) {
                        return TlsVersion.TLS_1_3;
                    }
                    break;
                case 79201641:
                    if (javaName.equals("SSLv3")) {
                        return TlsVersion.SSL_3_0;
                    }
                    break;
                case 79923350:
                    if (javaName.equals("TLSv1")) {
                        return TlsVersion.TLS_1_0;
                    }
                    break;
            }
            throw new IllegalArgumentException("Unexpected TLS version: " + javaName);
        }
    }

    static {
        Companion = new Companion((DefaultConstructorMarker) null);
    }

    private TlsVersion(String javaName2) {
        this.javaName = javaName2;
    }

    @JvmStatic
    public static final TlsVersion forJavaName(String str) {
        return Companion.forJavaName(str);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "javaName", imports = {}))
    /* renamed from: -deprecated_javaName  reason: not valid java name */
    public final String m1723deprecated_javaName() {
        return this.javaName;
    }

    public final String javaName() {
        return this.javaName;
    }
}

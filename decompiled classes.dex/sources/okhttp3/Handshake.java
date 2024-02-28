package okhttp3;

import java.io.IOException;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.net.ssl.SSLSession;
import javax.security.auth.x500.X500Principal;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.Util;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\u0018\u0000 &2\u00020\u0001:\u0001&B9\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0012\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\n¢\u0006\u0002\u0010\u000bJ\r\u0010\u0004\u001a\u00020\u0005H\u0007¢\u0006\u0002\b\u001aJ\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u0002J\b\u0010\u001e\u001a\u00020\u001fH\u0016J\u0013\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0007¢\u0006\u0002\b J\u000f\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0007¢\u0006\u0002\b!J\u0013\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0007¢\u0006\u0002\b\"J\u000f\u0010\u0014\u001a\u0004\u0018\u00010\u000fH\u0007¢\u0006\u0002\b#J\r\u0010\u0002\u001a\u00020\u0003H\u0007¢\u0006\u0002\b$J\b\u0010%\u001a\u00020\u0017H\u0016R\u0013\u0010\u0004\u001a\u00020\u00058\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\fR\u0019\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00078\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\rR\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u000f8G¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u0010R!\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\b0\u00078GX\u0002¢\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0011\u0010\rR\u0013\u0010\u0014\u001a\u0004\u0018\u00010\u000f8G¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0010R\u0013\u0010\u0002\u001a\u00020\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0015R\u0018\u0010\u0016\u001a\u00020\u0017*\u00020\b8BX\u0004¢\u0006\u0006\u001a\u0004\b\u0018\u0010\u0019¨\u0006'"}, d2 = {"Lokhttp3/Handshake;", "", "tlsVersion", "Lokhttp3/TlsVersion;", "cipherSuite", "Lokhttp3/CipherSuite;", "localCertificates", "", "Ljava/security/cert/Certificate;", "peerCertificatesFn", "Lkotlin/Function0;", "(Lokhttp3/TlsVersion;Lokhttp3/CipherSuite;Ljava/util/List;Lkotlin/jvm/functions/Function0;)V", "()Lokhttp3/CipherSuite;", "()Ljava/util/List;", "localPrincipal", "Ljava/security/Principal;", "()Ljava/security/Principal;", "peerCertificates", "peerCertificates$delegate", "Lkotlin/Lazy;", "peerPrincipal", "()Lokhttp3/TlsVersion;", "name", "", "getName", "(Ljava/security/cert/Certificate;)Ljava/lang/String;", "-deprecated_cipherSuite", "equals", "", "other", "hashCode", "", "-deprecated_localCertificates", "-deprecated_localPrincipal", "-deprecated_peerCertificates", "-deprecated_peerPrincipal", "-deprecated_tlsVersion", "toString", "Companion", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: Handshake.kt */
public final class Handshake {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private final CipherSuite cipherSuite;
    private final List<Certificate> localCertificates;
    private final Lazy peerCertificates$delegate;
    private final TlsVersion tlsVersion;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0015\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007¢\u0006\u0002\b\u0007J4\u0010\u0003\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH\u0007J\u0011\u0010\u0010\u001a\u00020\u0004*\u00020\u0006H\u0007¢\u0006\u0002\b\u0003J!\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u000e0\r*\f\u0012\u0006\b\u0001\u0012\u00020\u000e\u0018\u00010\u0012H\u0002¢\u0006\u0002\u0010\u0013¨\u0006\u0014"}, d2 = {"Lokhttp3/Handshake$Companion;", "", "()V", "get", "Lokhttp3/Handshake;", "sslSession", "Ljavax/net/ssl/SSLSession;", "-deprecated_get", "tlsVersion", "Lokhttp3/TlsVersion;", "cipherSuite", "Lokhttp3/CipherSuite;", "peerCertificates", "", "Ljava/security/cert/Certificate;", "localCertificates", "handshake", "toImmutableList", "", "([Ljava/security/cert/Certificate;)Ljava/util/List;", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: Handshake.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private final List<Certificate> toImmutableList(Certificate[] $this$toImmutableList) {
            return $this$toImmutableList != null ? Util.immutableListOf((Certificate[]) Arrays.copyOf($this$toImmutableList, $this$toImmutableList.length)) : CollectionsKt.emptyList();
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "moved to extension function", replaceWith = @ReplaceWith(expression = "sslSession.handshake()", imports = {}))
        /* renamed from: -deprecated_get  reason: not valid java name */
        public final Handshake m1637deprecated_get(SSLSession sslSession) throws IOException {
            Intrinsics.checkNotNullParameter(sslSession, "sslSession");
            return get(sslSession);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0040, code lost:
            r1 = okhttp3.CipherSuite.Companion.forJavaName(r0);
            r2 = r9.getProtocol();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x004b, code lost:
            if (r2 == null) goto L_0x0092;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0053, code lost:
            if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) "NONE", (java.lang.Object) r2) != false) goto L_0x0087;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0055, code lost:
            r3 = okhttp3.TlsVersion.Companion.forJavaName(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
            r4 = toImmutableList(r9.getPeerCertificates());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0069, code lost:
            r4 = kotlin.collections.CollectionsKt.emptyList();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0091, code lost:
            throw new java.io.IOException("tlsVersion == NONE");
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x00a2, code lost:
            throw new java.lang.IllegalStateException("tlsVersion == null".toString());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:5:0x0019, code lost:
            if (r0.equals("SSL_NULL_WITH_NULL_NULL") == false) goto L_0x0040;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:7:0x0022, code lost:
            if (r0.equals("TLS_NULL_WITH_NULL_NULL") == false) goto L_0x0040;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x003f, code lost:
            throw new java.io.IOException("cipherSuite == " + r0);
         */
        @kotlin.jvm.JvmStatic
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final okhttp3.Handshake get(javax.net.ssl.SSLSession r9) throws java.io.IOException {
            /*
                r8 = this;
                java.lang.String r0 = "$this$handshake"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r9, r0)
                java.lang.String r0 = r9.getCipherSuite()
                if (r0 == 0) goto L_0x00a3
                int r1 = r0.hashCode()
                switch(r1) {
                    case 1019404634: goto L_0x001c;
                    case 1208658923: goto L_0x0013;
                    default: goto L_0x0012;
                }
            L_0x0012:
                goto L_0x0040
            L_0x0013:
                java.lang.String r1 = "SSL_NULL_WITH_NULL_NULL"
                boolean r1 = r0.equals(r1)
                if (r1 != 0) goto L_0x0025
                goto L_0x0024
            L_0x001c:
                java.lang.String r1 = "TLS_NULL_WITH_NULL_NULL"
                boolean r1 = r0.equals(r1)
                if (r1 != 0) goto L_0x0025
            L_0x0024:
                goto L_0x0040
            L_0x0025:
                java.io.IOException r1 = new java.io.IOException
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "cipherSuite == "
                java.lang.StringBuilder r2 = r2.append(r3)
                java.lang.StringBuilder r2 = r2.append(r0)
                java.lang.String r2 = r2.toString()
                r1.<init>(r2)
                java.lang.Throwable r1 = (java.lang.Throwable) r1
                throw r1
            L_0x0040:
                okhttp3.CipherSuite$Companion r1 = okhttp3.CipherSuite.Companion
                okhttp3.CipherSuite r1 = r1.forJavaName(r0)
                java.lang.String r2 = r9.getProtocol()
                if (r2 == 0) goto L_0x0092
                java.lang.String r3 = "NONE"
                boolean r3 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r3, (java.lang.Object) r2)
                if (r3 != 0) goto L_0x0087
                okhttp3.TlsVersion$Companion r3 = okhttp3.TlsVersion.Companion
                okhttp3.TlsVersion r3 = r3.forJavaName(r2)
                r4 = r8
                okhttp3.Handshake$Companion r4 = (okhttp3.Handshake.Companion) r4     // Catch:{ SSLPeerUnverifiedException -> 0x0068 }
                java.security.cert.Certificate[] r5 = r9.getPeerCertificates()     // Catch:{ SSLPeerUnverifiedException -> 0x0068 }
                java.util.List r4 = r4.toImmutableList(r5)     // Catch:{ SSLPeerUnverifiedException -> 0x0068 }
                goto L_0x006e
            L_0x0068:
                r4 = move-exception
                java.util.List r5 = kotlin.collections.CollectionsKt.emptyList()
                r4 = r5
            L_0x006e:
                okhttp3.Handshake r5 = new okhttp3.Handshake
                r6 = r8
                okhttp3.Handshake$Companion r6 = (okhttp3.Handshake.Companion) r6
                java.security.cert.Certificate[] r7 = r9.getLocalCertificates()
                java.util.List r6 = r6.toImmutableList(r7)
                okhttp3.Handshake$Companion$handshake$1 r7 = new okhttp3.Handshake$Companion$handshake$1
                r7.<init>(r4)
                kotlin.jvm.functions.Function0 r7 = (kotlin.jvm.functions.Function0) r7
                r5.<init>(r3, r1, r6, r7)
                return r5
            L_0x0087:
                java.io.IOException r3 = new java.io.IOException
                java.lang.String r4 = "tlsVersion == NONE"
                r3.<init>(r4)
                java.lang.Throwable r3 = (java.lang.Throwable) r3
                throw r3
            L_0x0092:
                r2 = 0
                java.lang.IllegalStateException r2 = new java.lang.IllegalStateException
                java.lang.String r3 = "tlsVersion == null"
                java.lang.String r3 = r3.toString()
                r2.<init>(r3)
                java.lang.Throwable r2 = (java.lang.Throwable) r2
                throw r2
            L_0x00a3:
                r0 = 0
                java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
                java.lang.String r1 = "cipherSuite == null"
                java.lang.String r1 = r1.toString()
                r0.<init>(r1)
                java.lang.Throwable r0 = (java.lang.Throwable) r0
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.Handshake.Companion.get(javax.net.ssl.SSLSession):okhttp3.Handshake");
        }

        @JvmStatic
        public final Handshake get(TlsVersion tlsVersion, CipherSuite cipherSuite, List<? extends Certificate> peerCertificates, List<? extends Certificate> localCertificates) {
            Intrinsics.checkNotNullParameter(tlsVersion, "tlsVersion");
            Intrinsics.checkNotNullParameter(cipherSuite, "cipherSuite");
            Intrinsics.checkNotNullParameter(peerCertificates, "peerCertificates");
            Intrinsics.checkNotNullParameter(localCertificates, "localCertificates");
            return new Handshake(tlsVersion, cipherSuite, Util.toImmutableList(localCertificates), new Handshake$Companion$get$1(Util.toImmutableList(peerCertificates)));
        }
    }

    public Handshake(TlsVersion tlsVersion2, CipherSuite cipherSuite2, List<? extends Certificate> localCertificates2, Function0<? extends List<? extends Certificate>> peerCertificatesFn) {
        Intrinsics.checkNotNullParameter(tlsVersion2, "tlsVersion");
        Intrinsics.checkNotNullParameter(cipherSuite2, "cipherSuite");
        Intrinsics.checkNotNullParameter(localCertificates2, "localCertificates");
        Intrinsics.checkNotNullParameter(peerCertificatesFn, "peerCertificatesFn");
        this.tlsVersion = tlsVersion2;
        this.cipherSuite = cipherSuite2;
        this.localCertificates = localCertificates2;
        this.peerCertificates$delegate = LazyKt.lazy(new Handshake$peerCertificates$2(peerCertificatesFn));
    }

    @JvmStatic
    public static final Handshake get(SSLSession sSLSession) throws IOException {
        return Companion.get(sSLSession);
    }

    @JvmStatic
    public static final Handshake get(TlsVersion tlsVersion2, CipherSuite cipherSuite2, List<? extends Certificate> list, List<? extends Certificate> list2) {
        return Companion.get(tlsVersion2, cipherSuite2, list, list2);
    }

    private final String getName(Certificate $this$name) {
        if ($this$name instanceof X509Certificate) {
            return ((X509Certificate) $this$name).getSubjectDN().toString();
        }
        String type = $this$name.getType();
        Intrinsics.checkNotNullExpressionValue(type, "type");
        return type;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "cipherSuite", imports = {}))
    /* renamed from: -deprecated_cipherSuite  reason: not valid java name */
    public final CipherSuite m1631deprecated_cipherSuite() {
        return this.cipherSuite;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "localCertificates", imports = {}))
    /* renamed from: -deprecated_localCertificates  reason: not valid java name */
    public final List<Certificate> m1632deprecated_localCertificates() {
        return this.localCertificates;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "localPrincipal", imports = {}))
    /* renamed from: -deprecated_localPrincipal  reason: not valid java name */
    public final Principal m1633deprecated_localPrincipal() {
        return localPrincipal();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "peerCertificates", imports = {}))
    /* renamed from: -deprecated_peerCertificates  reason: not valid java name */
    public final List<Certificate> m1634deprecated_peerCertificates() {
        return peerCertificates();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "peerPrincipal", imports = {}))
    /* renamed from: -deprecated_peerPrincipal  reason: not valid java name */
    public final Principal m1635deprecated_peerPrincipal() {
        return peerPrincipal();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "tlsVersion", imports = {}))
    /* renamed from: -deprecated_tlsVersion  reason: not valid java name */
    public final TlsVersion m1636deprecated_tlsVersion() {
        return this.tlsVersion;
    }

    public final CipherSuite cipherSuite() {
        return this.cipherSuite;
    }

    public boolean equals(Object other) {
        return (other instanceof Handshake) && ((Handshake) other).tlsVersion == this.tlsVersion && Intrinsics.areEqual((Object) ((Handshake) other).cipherSuite, (Object) this.cipherSuite) && Intrinsics.areEqual((Object) ((Handshake) other).peerCertificates(), (Object) peerCertificates()) && Intrinsics.areEqual((Object) ((Handshake) other).localCertificates, (Object) this.localCertificates);
    }

    public int hashCode() {
        return (((((((17 * 31) + this.tlsVersion.hashCode()) * 31) + this.cipherSuite.hashCode()) * 31) + peerCertificates().hashCode()) * 31) + this.localCertificates.hashCode();
    }

    public final List<Certificate> localCertificates() {
        return this.localCertificates;
    }

    public final Principal localPrincipal() {
        Object firstOrNull = CollectionsKt.firstOrNull(this.localCertificates);
        X500Principal x500Principal = null;
        if (!(firstOrNull instanceof X509Certificate)) {
            firstOrNull = null;
        }
        X509Certificate x509Certificate = (X509Certificate) firstOrNull;
        if (x509Certificate != null) {
            x500Principal = x509Certificate.getSubjectX500Principal();
        }
        return x500Principal;
    }

    public final List<Certificate> peerCertificates() {
        return (List) this.peerCertificates$delegate.getValue();
    }

    public final Principal peerPrincipal() {
        Object firstOrNull = CollectionsKt.firstOrNull(peerCertificates());
        X500Principal x500Principal = null;
        if (!(firstOrNull instanceof X509Certificate)) {
            firstOrNull = null;
        }
        X509Certificate x509Certificate = (X509Certificate) firstOrNull;
        if (x509Certificate != null) {
            x500Principal = x509Certificate.getSubjectX500Principal();
        }
        return x500Principal;
    }

    public final TlsVersion tlsVersion() {
        return this.tlsVersion;
    }

    public String toString() {
        Iterable<Certificate> peerCertificates = peerCertificates();
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(peerCertificates, 10));
        for (Certificate name : peerCertificates) {
            arrayList.add(getName(name));
        }
        StringBuilder append = new StringBuilder().append("Handshake{").append("tlsVersion=").append(this.tlsVersion).append(' ').append("cipherSuite=").append(this.cipherSuite).append(' ').append("peerCertificates=").append(((List) arrayList).toString()).append(' ').append("localCertificates=");
        Iterable<Certificate> iterable = this.localCertificates;
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (Certificate name2 : iterable) {
            arrayList2.add(getName(name2));
        }
        return append.append((List) arrayList2).append('}').toString();
    }
}

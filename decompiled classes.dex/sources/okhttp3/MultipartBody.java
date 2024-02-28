package okhttp3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Typography;
import okhttp3.Headers;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u0000 #2\u00020\u0001:\u0003\"#$B%\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007¢\u0006\u0002\u0010\tJ\r\u0010\n\u001a\u00020\u000bH\u0007¢\u0006\u0002\b\u0015J\b\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\u0005H\u0016J\u000e\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\u0012J\u0013\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0007¢\u0006\u0002\b\u0018J\r\u0010\u0011\u001a\u00020\u0012H\u0007¢\u0006\u0002\b\u0019J\r\u0010\u0004\u001a\u00020\u0005H\u0007¢\u0006\u0002\b\u001aJ\u001a\u0010\u001b\u001a\u00020\u000e2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u0010\u0010 \u001a\u00020!2\u0006\u0010\u001c\u001a\u00020\u001dH\u0016R\u0011\u0010\n\u001a\u00020\u000b8G¢\u0006\u0006\u001a\u0004\b\n\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0019\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00078\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0010R\u0011\u0010\u0011\u001a\u00020\u00128G¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0013R\u0013\u0010\u0004\u001a\u00020\u00058\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0014¨\u0006%"}, d2 = {"Lokhttp3/MultipartBody;", "Lokhttp3/RequestBody;", "boundaryByteString", "Lokio/ByteString;", "type", "Lokhttp3/MediaType;", "parts", "", "Lokhttp3/MultipartBody$Part;", "(Lokio/ByteString;Lokhttp3/MediaType;Ljava/util/List;)V", "boundary", "", "()Ljava/lang/String;", "contentLength", "", "contentType", "()Ljava/util/List;", "size", "", "()I", "()Lokhttp3/MediaType;", "-deprecated_boundary", "part", "index", "-deprecated_parts", "-deprecated_size", "-deprecated_type", "writeOrCountBytes", "sink", "Lokio/BufferedSink;", "countBytes", "", "writeTo", "", "Builder", "Companion", "Part", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: MultipartBody.kt */
public final class MultipartBody extends RequestBody {
    public static final MediaType ALTERNATIVE = MediaType.Companion.get("multipart/alternative");
    private static final byte[] COLONSPACE = {(byte) 58, (byte) 32};
    private static final byte[] CRLF = {(byte) 13, (byte) 10};
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final byte[] DASHDASH;
    public static final MediaType DIGEST = MediaType.Companion.get("multipart/digest");
    public static final MediaType FORM = MediaType.Companion.get("multipart/form-data");
    public static final MediaType MIXED = MediaType.Companion.get("multipart/mixed");
    public static final MediaType PARALLEL = MediaType.Companion.get("multipart/parallel");
    private final ByteString boundaryByteString;
    private long contentLength = -1;
    private final MediaType contentType;
    private final List<Part> parts;
    private final MediaType type;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u000b\u001a\u00020\u00002\u0006\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u0003J \u0010\u000b\u001a\u00020\u00002\u0006\u0010\f\u001a\u00020\u00032\b\u0010\u000e\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u000f\u001a\u00020\u0010J\u0018\u0010\u0011\u001a\u00020\u00002\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\u00002\u0006\u0010\u0014\u001a\u00020\bJ\u000e\u0010\u0011\u001a\u00020\u00002\u0006\u0010\u000f\u001a\u00020\u0010J\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0002\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lokhttp3/MultipartBody$Builder;", "", "boundary", "", "(Ljava/lang/String;)V", "Lokio/ByteString;", "parts", "", "Lokhttp3/MultipartBody$Part;", "type", "Lokhttp3/MediaType;", "addFormDataPart", "name", "value", "filename", "body", "Lokhttp3/RequestBody;", "addPart", "headers", "Lokhttp3/Headers;", "part", "build", "Lokhttp3/MultipartBody;", "setType", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: MultipartBody.kt */
    public static final class Builder {
        private final ByteString boundary;
        private final List<Part> parts;
        private MediaType type;

        public Builder() {
            this((String) null, 1, (DefaultConstructorMarker) null);
        }

        public Builder(String boundary2) {
            Intrinsics.checkNotNullParameter(boundary2, "boundary");
            this.boundary = ByteString.Companion.encodeUtf8(boundary2);
            this.type = MultipartBody.MIXED;
            this.parts = new ArrayList();
        }

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public /* synthetic */ Builder(java.lang.String r1, int r2, kotlin.jvm.internal.DefaultConstructorMarker r3) {
            /*
                r0 = this;
                r2 = r2 & 1
                if (r2 == 0) goto L_0x0011
                java.util.UUID r1 = java.util.UUID.randomUUID()
                java.lang.String r1 = r1.toString()
                java.lang.String r2 = "UUID.randomUUID().toString()"
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r1, r2)
            L_0x0011:
                r0.<init>(r1)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.MultipartBody.Builder.<init>(java.lang.String, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
        }

        public final Builder addFormDataPart(String name, String value) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(value, "value");
            addPart(Part.Companion.createFormData(name, value));
            return this;
        }

        public final Builder addFormDataPart(String name, String filename, RequestBody body) {
            Intrinsics.checkNotNullParameter(name, "name");
            Intrinsics.checkNotNullParameter(body, "body");
            addPart(Part.Companion.createFormData(name, filename, body));
            return this;
        }

        public final Builder addPart(Headers headers, RequestBody body) {
            Intrinsics.checkNotNullParameter(body, "body");
            addPart(Part.Companion.create(headers, body));
            return this;
        }

        public final Builder addPart(Part part) {
            Intrinsics.checkNotNullParameter(part, "part");
            this.parts.add(part);
            return this;
        }

        public final Builder addPart(RequestBody body) {
            Intrinsics.checkNotNullParameter(body, "body");
            addPart(Part.Companion.create(body));
            return this;
        }

        public final MultipartBody build() {
            if (!this.parts.isEmpty()) {
                return new MultipartBody(this.boundary, this.type, Util.toImmutableList(this.parts));
            }
            throw new IllegalStateException("Multipart body must have at least one part.".toString());
        }

        public final Builder setType(MediaType type2) {
            Intrinsics.checkNotNullParameter(type2, "type");
            Builder builder = this;
            if (Intrinsics.areEqual((Object) type2.type(), (Object) "multipart")) {
                builder.type = type2;
                return this;
            }
            throw new IllegalArgumentException(("multipart != " + type2).toString());
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001d\u0010\r\u001a\u00020\u000e*\u00060\u000fj\u0002`\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0000¢\u0006\u0002\b\u0013R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u00020\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u00020\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u00020\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u00020\u00048\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lokhttp3/MultipartBody$Companion;", "", "()V", "ALTERNATIVE", "Lokhttp3/MediaType;", "COLONSPACE", "", "CRLF", "DASHDASH", "DIGEST", "FORM", "MIXED", "PARALLEL", "appendQuotedString", "", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "key", "", "appendQuotedString$okhttp", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: MultipartBody.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        public final void appendQuotedString$okhttp(StringBuilder $this$appendQuotedString, String key) {
            Intrinsics.checkNotNullParameter($this$appendQuotedString, "$this$appendQuotedString");
            Intrinsics.checkNotNullParameter(key, "key");
            $this$appendQuotedString.append(Typography.quote);
            int length = key.length();
            for (int i = 0; i < length; i++) {
                char charAt = key.charAt(i);
                char c = charAt;
                switch (charAt) {
                    case 10:
                        $this$appendQuotedString.append("%0A");
                        break;
                    case 13:
                        $this$appendQuotedString.append("%0D");
                        break;
                    case '\"':
                        $this$appendQuotedString.append("%22");
                        break;
                    default:
                        $this$appendQuotedString.append(c);
                        break;
                }
            }
            $this$appendQuotedString.append(Typography.quote);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 \u000b2\u00020\u0001:\u0001\u000bB\u0019\b\u0002\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\r\u0010\u0004\u001a\u00020\u0005H\u0007¢\u0006\u0002\b\tJ\u000f\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u0007¢\u0006\u0002\b\nR\u0013\u0010\u0004\u001a\u00020\u00058\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0007R\u0015\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\b¨\u0006\f"}, d2 = {"Lokhttp3/MultipartBody$Part;", "", "headers", "Lokhttp3/Headers;", "body", "Lokhttp3/RequestBody;", "(Lokhttp3/Headers;Lokhttp3/RequestBody;)V", "()Lokhttp3/RequestBody;", "()Lokhttp3/Headers;", "-deprecated_body", "-deprecated_headers", "Companion", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: MultipartBody.kt */
    public static final class Part {
        public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
        private final RequestBody body;
        private final Headers headers;

        @Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0007J\u0018\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0007J\"\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\r\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0007¨\u0006\u000e"}, d2 = {"Lokhttp3/MultipartBody$Part$Companion;", "", "()V", "create", "Lokhttp3/MultipartBody$Part;", "headers", "Lokhttp3/Headers;", "body", "Lokhttp3/RequestBody;", "createFormData", "name", "", "value", "filename", "okhttp"}, k = 1, mv = {1, 4, 0})
        /* compiled from: MultipartBody.kt */
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
                this();
            }

            @JvmStatic
            public final Part create(Headers headers, RequestBody body) {
                Intrinsics.checkNotNullParameter(body, "body");
                boolean z = true;
                if ((headers != null ? headers.get("Content-Type") : null) == null) {
                    if ((headers != null ? headers.get("Content-Length") : null) != null) {
                        z = false;
                    }
                    if (z) {
                        return new Part(headers, body, (DefaultConstructorMarker) null);
                    }
                    throw new IllegalArgumentException("Unexpected header: Content-Length".toString());
                }
                throw new IllegalArgumentException("Unexpected header: Content-Type".toString());
            }

            @JvmStatic
            public final Part create(RequestBody body) {
                Intrinsics.checkNotNullParameter(body, "body");
                return create((Headers) null, body);
            }

            @JvmStatic
            public final Part createFormData(String name, String value) {
                Intrinsics.checkNotNullParameter(name, "name");
                Intrinsics.checkNotNullParameter(value, "value");
                return createFormData(name, (String) null, RequestBody.Companion.create$default(RequestBody.Companion, value, (MediaType) null, 1, (Object) null));
            }

            @JvmStatic
            public final Part createFormData(String name, String filename, RequestBody body) {
                Intrinsics.checkNotNullParameter(name, "name");
                Intrinsics.checkNotNullParameter(body, "body");
                StringBuilder sb = new StringBuilder();
                StringBuilder sb2 = sb;
                sb2.append("form-data; name=");
                MultipartBody.Companion.appendQuotedString$okhttp(sb2, name);
                if (filename != null) {
                    sb2.append("; filename=");
                    MultipartBody.Companion.appendQuotedString$okhttp(sb2, filename);
                }
                String sb3 = sb.toString();
                Intrinsics.checkNotNullExpressionValue(sb3, "StringBuilder().apply(builderAction).toString()");
                return create(new Headers.Builder().addUnsafeNonAscii("Content-Disposition", sb3).build(), body);
            }
        }

        private Part(Headers headers2, RequestBody body2) {
            this.headers = headers2;
            this.body = body2;
        }

        public /* synthetic */ Part(Headers headers2, RequestBody body2, DefaultConstructorMarker $constructor_marker) {
            this(headers2, body2);
        }

        @JvmStatic
        public static final Part create(Headers headers2, RequestBody requestBody) {
            return Companion.create(headers2, requestBody);
        }

        @JvmStatic
        public static final Part create(RequestBody requestBody) {
            return Companion.create(requestBody);
        }

        @JvmStatic
        public static final Part createFormData(String str, String str2) {
            return Companion.createFormData(str, str2);
        }

        @JvmStatic
        public static final Part createFormData(String str, String str2, RequestBody requestBody) {
            return Companion.createFormData(str, str2, requestBody);
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "body", imports = {}))
        /* renamed from: -deprecated_body  reason: not valid java name */
        public final RequestBody m1672deprecated_body() {
            return this.body;
        }

        @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "headers", imports = {}))
        /* renamed from: -deprecated_headers  reason: not valid java name */
        public final Headers m1673deprecated_headers() {
            return this.headers;
        }

        public final RequestBody body() {
            return this.body;
        }

        public final Headers headers() {
            return this.headers;
        }
    }

    static {
        byte b = (byte) 45;
        DASHDASH = new byte[]{b, b};
    }

    public MultipartBody(ByteString boundaryByteString2, MediaType type2, List<Part> parts2) {
        Intrinsics.checkNotNullParameter(boundaryByteString2, "boundaryByteString");
        Intrinsics.checkNotNullParameter(type2, "type");
        Intrinsics.checkNotNullParameter(parts2, "parts");
        this.boundaryByteString = boundaryByteString2;
        this.type = type2;
        this.parts = parts2;
        this.contentType = MediaType.Companion.get(type2 + "; boundary=" + boundary());
    }

    private final long writeOrCountBytes(BufferedSink sink, boolean countBytes) throws IOException {
        BufferedSink bufferedSink = sink;
        long j = 0;
        Buffer buffer = null;
        if (countBytes) {
            buffer = new Buffer();
            bufferedSink = buffer;
        }
        int size = this.parts.size();
        for (int i = 0; i < size; i++) {
            Part part = this.parts.get(i);
            Headers headers = part.headers();
            RequestBody body = part.body();
            Intrinsics.checkNotNull(bufferedSink);
            bufferedSink.write(DASHDASH);
            bufferedSink.write(this.boundaryByteString);
            bufferedSink.write(CRLF);
            if (headers != null) {
                int size2 = headers.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    bufferedSink.writeUtf8(headers.name(i2)).write(COLONSPACE).writeUtf8(headers.value(i2)).write(CRLF);
                }
            }
            MediaType contentType2 = body.contentType();
            if (contentType2 != null) {
                bufferedSink.writeUtf8("Content-Type: ").writeUtf8(contentType2.toString()).write(CRLF);
            }
            long contentLength2 = body.contentLength();
            if (contentLength2 != -1) {
                bufferedSink.writeUtf8("Content-Length: ").writeDecimalLong(contentLength2).write(CRLF);
            } else if (countBytes) {
                Intrinsics.checkNotNull(buffer);
                buffer.clear();
                return -1;
            }
            byte[] bArr = CRLF;
            bufferedSink.write(bArr);
            if (countBytes) {
                j += contentLength2;
            } else {
                body.writeTo(bufferedSink);
            }
            bufferedSink.write(bArr);
        }
        Intrinsics.checkNotNull(bufferedSink);
        byte[] bArr2 = DASHDASH;
        bufferedSink.write(bArr2);
        bufferedSink.write(this.boundaryByteString);
        bufferedSink.write(bArr2);
        bufferedSink.write(CRLF);
        if (!countBytes) {
            return j;
        }
        Intrinsics.checkNotNull(buffer);
        long size3 = j + buffer.size();
        buffer.clear();
        return size3;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "boundary", imports = {}))
    /* renamed from: -deprecated_boundary  reason: not valid java name */
    public final String m1668deprecated_boundary() {
        return boundary();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "parts", imports = {}))
    /* renamed from: -deprecated_parts  reason: not valid java name */
    public final List<Part> m1669deprecated_parts() {
        return this.parts;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "size", imports = {}))
    /* renamed from: -deprecated_size  reason: not valid java name */
    public final int m1670deprecated_size() {
        return size();
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "moved to val", replaceWith = @ReplaceWith(expression = "type", imports = {}))
    /* renamed from: -deprecated_type  reason: not valid java name */
    public final MediaType m1671deprecated_type() {
        return this.type;
    }

    public final String boundary() {
        return this.boundaryByteString.utf8();
    }

    public long contentLength() throws IOException {
        long j = this.contentLength;
        if (j != -1) {
            return j;
        }
        long writeOrCountBytes = writeOrCountBytes((BufferedSink) null, true);
        this.contentLength = writeOrCountBytes;
        return writeOrCountBytes;
    }

    public MediaType contentType() {
        return this.contentType;
    }

    public final Part part(int index) {
        return this.parts.get(index);
    }

    public final List<Part> parts() {
        return this.parts;
    }

    public final int size() {
        return this.parts.size();
    }

    public final MediaType type() {
        return this.type;
    }

    public void writeTo(BufferedSink sink) throws IOException {
        Intrinsics.checkNotNullParameter(sink, "sink");
        writeOrCountBytes(sink, false);
    }
}

package okio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.logging.Logger;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000L\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\n\u0010\n\u001a\u00020\u000b*\u00020\f\u001a\u0016\u0010\r\u001a\u00020\u000b*\u00020\f2\b\b\u0002\u0010\u000e\u001a\u00020\u0006H\u0007\u001a\n\u0010\r\u001a\u00020\u000b*\u00020\u000f\u001a\n\u0010\r\u001a\u00020\u000b*\u00020\u0010\u001a%\u0010\r\u001a\u00020\u000b*\u00020\u00112\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0007¢\u0006\u0002\u0010\u0015\u001a\n\u0010\u0016\u001a\u00020\u0017*\u00020\f\u001a\n\u0010\u0016\u001a\u00020\u0017*\u00020\u0018\u001a\n\u0010\u0016\u001a\u00020\u0017*\u00020\u0010\u001a%\u0010\u0016\u001a\u00020\u0017*\u00020\u00112\u0012\u0010\u0012\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140\u0013\"\u00020\u0014H\u0007¢\u0006\u0002\u0010\u0019\"\u001c\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u0001X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0003\u0010\u0004\"\u001c\u0010\u0005\u001a\u00020\u0006*\u00060\u0007j\u0002`\b8@X\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\t¨\u0006\u001a"}, d2 = {"logger", "Ljava/util/logging/Logger;", "kotlin.jvm.PlatformType", "getLogger$Okio__JvmOkioKt", "()Ljava/util/logging/Logger;", "isAndroidGetsocknameError", "", "Ljava/lang/AssertionError;", "Lkotlin/AssertionError;", "(Ljava/lang/AssertionError;)Z", "appendingSink", "Lokio/Sink;", "Ljava/io/File;", "sink", "append", "Ljava/io/OutputStream;", "Ljava/net/Socket;", "Ljava/nio/file/Path;", "options", "", "Ljava/nio/file/OpenOption;", "(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Lokio/Sink;", "source", "Lokio/Source;", "Ljava/io/InputStream;", "(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Lokio/Source;", "okio"}, k = 5, mv = {1, 4, 0}, xs = "okio/Okio")
/* compiled from: JvmOkio.kt */
final /* synthetic */ class Okio__JvmOkioKt {
    /* access modifiers changed from: private */
    public static final Logger logger = Logger.getLogger("okio.Okio");

    public static final Sink appendingSink(File $this$appendingSink) throws FileNotFoundException {
        Intrinsics.checkNotNullParameter($this$appendingSink, "$this$appendingSink");
        return Okio.sink((OutputStream) new FileOutputStream($this$appendingSink, true));
    }

    private static final Logger getLogger$Okio__JvmOkioKt() {
        return logger;
    }

    public static final boolean isAndroidGetsocknameError(AssertionError $this$isAndroidGetsocknameError) {
        Intrinsics.checkNotNullParameter($this$isAndroidGetsocknameError, "$this$isAndroidGetsocknameError");
        if ($this$isAndroidGetsocknameError.getCause() == null) {
            return false;
        }
        String message = $this$isAndroidGetsocknameError.getMessage();
        return message != null ? StringsKt.contains$default((CharSequence) message, (CharSequence) "getsockname failed", false, 2, (Object) null) : false;
    }

    public static final Sink sink(File file) throws FileNotFoundException {
        return sink$default(file, false, 1, (Object) null);
    }

    public static final Sink sink(File $this$sink, boolean append) throws FileNotFoundException {
        Intrinsics.checkNotNullParameter($this$sink, "$this$sink");
        return Okio.sink((OutputStream) new FileOutputStream($this$sink, append));
    }

    public static final Sink sink(OutputStream $this$sink) {
        Intrinsics.checkNotNullParameter($this$sink, "$this$sink");
        return new OutputStreamSink($this$sink, new Timeout());
    }

    public static final Sink sink(Socket $this$sink) throws IOException {
        Intrinsics.checkNotNullParameter($this$sink, "$this$sink");
        SocketAsyncTimeout socketAsyncTimeout = new SocketAsyncTimeout($this$sink);
        OutputStream outputStream = $this$sink.getOutputStream();
        Intrinsics.checkNotNullExpressionValue(outputStream, "getOutputStream()");
        return socketAsyncTimeout.sink(new OutputStreamSink(outputStream, socketAsyncTimeout));
    }

    public static final Sink sink(Path $this$sink, OpenOption... options) throws IOException {
        Intrinsics.checkNotNullParameter($this$sink, "$this$sink");
        Intrinsics.checkNotNullParameter(options, "options");
        OutputStream newOutputStream = Files.newOutputStream($this$sink, (OpenOption[]) Arrays.copyOf(options, options.length));
        Intrinsics.checkNotNullExpressionValue(newOutputStream, "Files.newOutputStream(this, *options)");
        return Okio.sink(newOutputStream);
    }

    public static /* synthetic */ Sink sink$default(File file, boolean z, int i, Object obj) throws FileNotFoundException {
        if ((i & 1) != 0) {
            z = false;
        }
        return Okio.sink(file, z);
    }

    public static final Source source(File $this$source) throws FileNotFoundException {
        Intrinsics.checkNotNullParameter($this$source, "$this$source");
        return Okio.source((InputStream) new FileInputStream($this$source));
    }

    public static final Source source(InputStream $this$source) {
        Intrinsics.checkNotNullParameter($this$source, "$this$source");
        return new InputStreamSource($this$source, new Timeout());
    }

    public static final Source source(Socket $this$source) throws IOException {
        Intrinsics.checkNotNullParameter($this$source, "$this$source");
        SocketAsyncTimeout socketAsyncTimeout = new SocketAsyncTimeout($this$source);
        InputStream inputStream = $this$source.getInputStream();
        Intrinsics.checkNotNullExpressionValue(inputStream, "getInputStream()");
        return socketAsyncTimeout.source(new InputStreamSource(inputStream, socketAsyncTimeout));
    }

    public static final Source source(Path $this$source, OpenOption... options) throws IOException {
        Intrinsics.checkNotNullParameter($this$source, "$this$source");
        Intrinsics.checkNotNullParameter(options, "options");
        InputStream newInputStream = Files.newInputStream($this$source, (OpenOption[]) Arrays.copyOf(options, options.length));
        Intrinsics.checkNotNullExpressionValue(newInputStream, "Files.newInputStream(this, *options)");
        return Okio.source(newInputStream);
    }
}

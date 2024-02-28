package androidx.core.util;

import android.util.AtomicFile;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000.\n\u0000\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\b\u001a\u0016\u0010\u0003\u001a\u00020\u0004*\u00020\u00022\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0007\u001a3\u0010\u0007\u001a\u00020\b*\u00020\u00022!\u0010\t\u001a\u001d\u0012\u0013\u0012\u00110\u000b¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u00020\b0\nH\bø\u0001\u0000\u001a\u0014\u0010\u000f\u001a\u00020\b*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u0001H\u0007\u001a\u001e\u0010\u0011\u001a\u00020\b*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0007\u0002\u0007\n\u0005\b20\u0001¨\u0006\u0013"}, d2 = {"readBytes", "", "Landroid/util/AtomicFile;", "readText", "", "charset", "Ljava/nio/charset/Charset;", "tryWrite", "", "block", "Lkotlin/Function1;", "Ljava/io/FileOutputStream;", "Lkotlin/ParameterName;", "name", "out", "writeBytes", "array", "writeText", "text", "core-ktx_release"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: 005A */
public final class AtomicFileKt {
    public static final byte[] readBytes(AtomicFile $this$readBytes) {
        Intrinsics.checkNotNullParameter($this$readBytes, "<this>");
        byte[] readFully = $this$readBytes.readFully();
        Intrinsics.checkNotNullExpressionValue(readFully, "readFully()");
        return readFully;
    }

    public static final String readText(AtomicFile $this$readText, Charset charset) {
        Intrinsics.checkNotNullParameter($this$readText, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        byte[] readFully = $this$readText.readFully();
        Intrinsics.checkNotNullExpressionValue(readFully, "readFully()");
        String str = new String(readFully, charset);
        Log1F380D.a((Object) str);
        return str;
    }

    public static final void tryWrite(AtomicFile $this$tryWrite, Function1<? super FileOutputStream, Unit> block) {
        Intrinsics.checkNotNullParameter($this$tryWrite, "<this>");
        Intrinsics.checkNotNullParameter(block, "block");
        FileOutputStream startWrite = $this$tryWrite.startWrite();
        try {
            Intrinsics.checkNotNullExpressionValue(startWrite, "stream");
            block.invoke(startWrite);
            InlineMarker.finallyStart(1);
            $this$tryWrite.finishWrite(startWrite);
            InlineMarker.finallyEnd(1);
        } catch (Throwable th) {
            InlineMarker.finallyStart(1);
            $this$tryWrite.failWrite(startWrite);
            InlineMarker.finallyEnd(1);
            throw th;
        }
    }

    public static final void writeBytes(AtomicFile $this$writeBytes, byte[] array) {
        Intrinsics.checkNotNullParameter($this$writeBytes, "<this>");
        Intrinsics.checkNotNullParameter(array, "array");
        AtomicFile atomicFile = $this$writeBytes;
        FileOutputStream startWrite = atomicFile.startWrite();
        try {
            Intrinsics.checkNotNullExpressionValue(startWrite, "stream");
            startWrite.write(array);
            atomicFile.finishWrite(startWrite);
        } catch (Throwable th) {
            atomicFile.failWrite(startWrite);
            throw th;
        }
    }

    public static final void writeText(AtomicFile $this$writeText, String text, Charset charset) {
        Intrinsics.checkNotNullParameter($this$writeText, "<this>");
        Intrinsics.checkNotNullParameter(text, "text");
        Intrinsics.checkNotNullParameter(charset, "charset");
        byte[] bytes = text.getBytes(charset);
        Intrinsics.checkNotNullExpressionValue(bytes, "this as java.lang.String).getBytes(charset)");
        writeBytes($this$writeText, bytes);
    }

    public static /* synthetic */ void writeText$default(AtomicFile atomicFile, String str, Charset charset, int i, Object obj) {
        if ((i & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        writeText(atomicFile, str, charset);
    }

    public static /* synthetic */ String readText$default(AtomicFile atomicFile, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        String readText = readText(atomicFile, charset);
        Log1F380D.a((Object) readText);
        return readText;
    }
}

package androidx.core.text;

import android.text.TextUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u000b\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0000\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\b\u001a\r\u0010\u0003\u001a\u00020\u0004*\u00020\u0002H\b¨\u0006\u0005"}, d2 = {"isDigitsOnly", "", "", "trimmedLength", "", "core-ktx_release"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: CharSequence.kt */
public final class CharSequenceKt {
    public static final boolean isDigitsOnly(CharSequence $this$isDigitsOnly) {
        Intrinsics.checkNotNullParameter($this$isDigitsOnly, "<this>");
        return TextUtils.isDigitsOnly($this$isDigitsOnly);
    }

    public static final int trimmedLength(CharSequence $this$trimmedLength) {
        Intrinsics.checkNotNullParameter($this$trimmedLength, "<this>");
        return TextUtils.getTrimmedLength($this$trimmedLength);
    }
}

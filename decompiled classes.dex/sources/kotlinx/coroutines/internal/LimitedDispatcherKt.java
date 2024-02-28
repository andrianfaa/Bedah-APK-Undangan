package kotlinx.coroutines.internal;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0002\u0010\b\n\u0000\u001a\f\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0000¨\u0006\u0003"}, d2 = {"checkParallelism", "", "", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: 019F */
public final class LimitedDispatcherKt {
    public static final void checkParallelism(int $this$checkParallelism) {
        boolean z = true;
        if ($this$checkParallelism < 1) {
            z = false;
        }
        if (!z) {
            String stringPlus = Intrinsics.stringPlus("Expected positive parallelism level, but got ", Integer.valueOf($this$checkParallelism));
            Log1F380D.a((Object) stringPlus);
            throw new IllegalArgumentException(stringPlus.toString());
        }
    }
}

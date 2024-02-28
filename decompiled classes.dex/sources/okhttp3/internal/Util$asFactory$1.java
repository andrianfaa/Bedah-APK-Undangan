package okhttp3.internal;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Call;
import okhttp3.EventListener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "Lokhttp3/EventListener;", "it", "Lokhttp3/Call;", "create"}, k = 3, mv = {1, 4, 0})
/* compiled from: Util.kt */
final class Util$asFactory$1 implements EventListener.Factory {
    final /* synthetic */ EventListener $this_asFactory;

    Util$asFactory$1(EventListener eventListener) {
        this.$this_asFactory = eventListener;
    }

    public final EventListener create(Call it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return this.$this_asFactory;
    }
}

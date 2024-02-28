package androidx.core.os;

import android.os.Handler;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u0000$\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u001a4\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u000e\b\u0004\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\bø\u0001\u0000\u001a4\u0010\n\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00042\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u000e\b\u0004\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\bø\u0001\u0000\u0002\u0007\n\u0005\b20\u0001¨\u0006\f"}, d2 = {"postAtTime", "Ljava/lang/Runnable;", "Landroid/os/Handler;", "uptimeMillis", "", "token", "", "action", "Lkotlin/Function0;", "", "postDelayed", "delayInMillis", "core-ktx_release"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: Handler.kt */
public final class HandlerKt {
    public static final Runnable postAtTime(Handler $this$postAtTime, long uptimeMillis, Object token, Function0<Unit> action) {
        Intrinsics.checkNotNullParameter($this$postAtTime, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        Runnable handlerKt$postAtTime$runnable$1 = new HandlerKt$postAtTime$runnable$1(action);
        $this$postAtTime.postAtTime(handlerKt$postAtTime$runnable$1, token, uptimeMillis);
        return handlerKt$postAtTime$runnable$1;
    }

    public static /* synthetic */ Runnable postAtTime$default(Handler $this$postAtTime_u24default, long uptimeMillis, Object token, Function0 action, int i, Object obj) {
        if ((i & 2) != 0) {
            token = null;
        }
        Intrinsics.checkNotNullParameter($this$postAtTime_u24default, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        Runnable handlerKt$postAtTime$runnable$1 = new HandlerKt$postAtTime$runnable$1(action);
        $this$postAtTime_u24default.postAtTime(handlerKt$postAtTime$runnable$1, token, uptimeMillis);
        return handlerKt$postAtTime$runnable$1;
    }

    public static final Runnable postDelayed(Handler $this$postDelayed, long delayInMillis, Object token, Function0<Unit> action) {
        Intrinsics.checkNotNullParameter($this$postDelayed, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        Runnable handlerKt$postDelayed$runnable$1 = new HandlerKt$postDelayed$runnable$1(action);
        if (token == null) {
            $this$postDelayed.postDelayed(handlerKt$postDelayed$runnable$1, delayInMillis);
        } else {
            HandlerCompat.postDelayed($this$postDelayed, handlerKt$postDelayed$runnable$1, token, delayInMillis);
        }
        return handlerKt$postDelayed$runnable$1;
    }

    public static /* synthetic */ Runnable postDelayed$default(Handler $this$postDelayed_u24default, long delayInMillis, Object token, Function0 action, int i, Object obj) {
        if ((i & 2) != 0) {
            token = null;
        }
        Intrinsics.checkNotNullParameter($this$postDelayed_u24default, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        Runnable handlerKt$postDelayed$runnable$1 = new HandlerKt$postDelayed$runnable$1(action);
        if (token == null) {
            $this$postDelayed_u24default.postDelayed(handlerKt$postDelayed$runnable$1, delayInMillis);
        } else {
            HandlerCompat.postDelayed($this$postDelayed_u24default, handlerKt$postDelayed$runnable$1, token, delayInMillis);
        }
        return handlerKt$postDelayed$runnable$1;
    }
}

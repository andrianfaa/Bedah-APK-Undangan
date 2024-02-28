package kotlinx.coroutines.android;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.time.DurationKt;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.Delay;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.DisposableHandle;
import kotlinx.coroutines.JobKt;
import kotlinx.coroutines.NonDisposableHandle;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u001b\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0002\u0010\u0007B!\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u001c\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\n\u0010\u0013\u001a\u00060\u0014j\u0002`\u0015H\u0002J\u001c\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\n\u0010\u0013\u001a\u00060\u0014j\u0002`\u0015H\u0016J\u0013\u0010\u0017\u001a\u00020\t2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0002J\b\u0010\u001a\u001a\u00020\u001bH\u0016J$\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\n\u0010\u0013\u001a\u00060\u0014j\u0002`\u00152\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010 \u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u001e\u0010!\u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020\u001f2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00100#H\u0016J\b\u0010$\u001a\u00020\u0006H\u0016R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u0000X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\u0000X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006%"}, d2 = {"Lkotlinx/coroutines/android/HandlerContext;", "Lkotlinx/coroutines/android/HandlerDispatcher;", "Lkotlinx/coroutines/Delay;", "handler", "Landroid/os/Handler;", "name", "", "(Landroid/os/Handler;Ljava/lang/String;)V", "invokeImmediately", "", "(Landroid/os/Handler;Ljava/lang/String;Z)V", "_immediate", "immediate", "getImmediate", "()Lkotlinx/coroutines/android/HandlerContext;", "cancelOnRejection", "", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "dispatch", "equals", "other", "", "hashCode", "", "invokeOnTimeout", "Lkotlinx/coroutines/DisposableHandle;", "timeMillis", "", "isDispatchNeeded", "scheduleResumeAfterDelay", "continuation", "Lkotlinx/coroutines/CancellableContinuation;", "toString", "kotlinx-coroutines-android"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: 017D */
public final class HandlerContext extends HandlerDispatcher implements Delay {
    private volatile HandlerContext _immediate;
    /* access modifiers changed from: private */
    public final Handler handler;
    private final HandlerContext immediate;
    private final boolean invokeImmediately;
    private final String name;

    public HandlerContext(Handler handler2, String name2) {
        this(handler2, name2, false);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ HandlerContext(Handler handler2, String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(handler2, (i & 2) != 0 ? null : str);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    private HandlerContext(Handler handler2, String name2, boolean invokeImmediately2) {
        super((DefaultConstructorMarker) null);
        HandlerContext handlerContext = null;
        this.handler = handler2;
        this.name = name2;
        this.invokeImmediately = invokeImmediately2;
        this._immediate = invokeImmediately2 ? this : handlerContext;
        HandlerContext handlerContext2 = this._immediate;
        if (handlerContext2 == null) {
            handlerContext2 = new HandlerContext(handler2, name2, true);
            this._immediate = handlerContext2;
        }
        this.immediate = handlerContext2;
    }

    private final void cancelOnRejection(CoroutineContext context, Runnable block) {
        JobKt.cancel(context, new CancellationException("The task was rejected, the handler underlying the dispatcher '" + this + "' was closed"));
        Dispatchers.getIO().dispatch(context, block);
    }

    /* access modifiers changed from: private */
    /* renamed from: invokeOnTimeout$lambda-3  reason: not valid java name */
    public static final void m1520invokeOnTimeout$lambda3(HandlerContext this$0, Runnable $block) {
        this$0.handler.removeCallbacks($block);
    }

    public void dispatch(CoroutineContext context, Runnable block) {
        if (!this.handler.post(block)) {
            cancelOnRejection(context, block);
        }
    }

    public boolean equals(Object other) {
        return (other instanceof HandlerContext) && ((HandlerContext) other).handler == this.handler;
    }

    public HandlerContext getImmediate() {
        return this.immediate;
    }

    public int hashCode() {
        return System.identityHashCode(this.handler);
    }

    public DisposableHandle invokeOnTimeout(long timeMillis, Runnable block, CoroutineContext context) {
        if (this.handler.postDelayed(block, RangesKt.coerceAtMost(timeMillis, (long) DurationKt.MAX_MILLIS))) {
            return new HandlerContext$$ExternalSyntheticLambda0(this, block);
        }
        cancelOnRejection(context, block);
        return NonDisposableHandle.INSTANCE;
    }

    public boolean isDispatchNeeded(CoroutineContext context) {
        return !this.invokeImmediately || !Intrinsics.areEqual((Object) Looper.myLooper(), (Object) this.handler.getLooper());
    }

    public void scheduleResumeAfterDelay(long timeMillis, CancellableContinuation<? super Unit> continuation) {
        Runnable handlerContext$scheduleResumeAfterDelay$$inlined$Runnable$1 = new HandlerContext$scheduleResumeAfterDelay$$inlined$Runnable$1(continuation, this);
        if (this.handler.postDelayed(handlerContext$scheduleResumeAfterDelay$$inlined$Runnable$1, RangesKt.coerceAtMost(timeMillis, (long) DurationKt.MAX_MILLIS))) {
            continuation.invokeOnCancellation(new HandlerContext$scheduleResumeAfterDelay$1(this, handlerContext$scheduleResumeAfterDelay$$inlined$Runnable$1));
        } else {
            cancelOnRejection(continuation.getContext(), handlerContext$scheduleResumeAfterDelay$$inlined$Runnable$1);
        }
    }

    public String toString() {
        String stringInternalImpl = toStringInternalImpl();
        if (stringInternalImpl != null) {
            return stringInternalImpl;
        }
        HandlerContext handlerContext = this;
        String str = handlerContext.name;
        if (str == null) {
            str = handlerContext.handler.toString();
        }
        if (!handlerContext.invokeImmediately) {
            return str;
        }
        String stringPlus = Intrinsics.stringPlus(str, ".immediate");
        Log1F380D.a((Object) stringPlus);
        return stringPlus;
    }
}

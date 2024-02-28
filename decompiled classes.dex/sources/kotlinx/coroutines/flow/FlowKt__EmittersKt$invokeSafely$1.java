package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function3;

@Metadata(k = 3, mv = {1, 6, 0}, xi = 48)
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__EmittersKt", f = "Emitters.kt", i = {0}, l = {216}, m = "invokeSafely$FlowKt__EmittersKt", n = {"cause"}, s = {"L$0"})
/* compiled from: Emitters.kt */
final class FlowKt__EmittersKt$invokeSafely$1<T> extends ContinuationImpl {
    Object L$0;
    int label;
    /* synthetic */ Object result;

    FlowKt__EmittersKt$invokeSafely$1(Continuation<? super FlowKt__EmittersKt$invokeSafely$1> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return FlowKt__EmittersKt.invokeSafely$FlowKt__EmittersKt((FlowCollector) null, (Function3) null, (Throwable) null, this);
    }
}

package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;

@Metadata(d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u0005\u001a\u0002H\u0002H@"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/flow/FlowCollector;", "it"}, k = 3, mv = {1, 6, 0}, xi = 48)
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__MergeKt$mapLatest$1", f = "Merge.kt", i = {}, l = {214, 214}, m = "invokeSuspend", n = {}, s = {})
/* compiled from: Merge.kt */
final class FlowKt__MergeKt$mapLatest$1 extends SuspendLambda implements Function3<FlowCollector<? super R>, T, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2<T, Continuation<? super R>, Object> $transform;
    private /* synthetic */ Object L$0;
    /* synthetic */ Object L$1;
    int label;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    FlowKt__MergeKt$mapLatest$1(Function2<? super T, ? super Continuation<? super R>, ? extends Object> function2, Continuation<? super FlowKt__MergeKt$mapLatest$1> continuation) {
        super(3, continuation);
        this.$transform = function2;
    }

    public final Object invoke(FlowCollector<? super R> flowCollector, T t, Continuation<? super Unit> continuation) {
        FlowKt__MergeKt$mapLatest$1 flowKt__MergeKt$mapLatest$1 = new FlowKt__MergeKt$mapLatest$1(this.$transform, continuation);
        flowKt__MergeKt$mapLatest$1.L$0 = flowCollector;
        flowKt__MergeKt$mapLatest$1.L$1 = t;
        return flowKt__MergeKt$mapLatest$1.invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        FlowCollector flowCollector;
        FlowKt__MergeKt$mapLatest$1 flowKt__MergeKt$mapLatest$1;
        Object obj2;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                FlowCollector flowCollector2 = (FlowCollector) this.L$0;
                Object obj3 = this.L$1;
                Function2<T, Continuation<? super R>, Object> function2 = this.$transform;
                this.L$0 = flowCollector2;
                this.label = 1;
                Object invoke = function2.invoke(obj3, this);
                if (invoke != coroutine_suspended) {
                    obj2 = obj;
                    obj = invoke;
                    flowCollector = flowCollector2;
                    flowKt__MergeKt$mapLatest$1 = this;
                    break;
                } else {
                    return coroutine_suspended;
                }
            case 1:
                ResultKt.throwOnFailure(obj);
                flowCollector = (FlowCollector) this.L$0;
                flowKt__MergeKt$mapLatest$1 = this;
                obj2 = obj;
                break;
            case 2:
                ResultKt.throwOnFailure(obj);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        flowKt__MergeKt$mapLatest$1.L$0 = null;
        flowKt__MergeKt$mapLatest$1.label = 2;
        if (flowCollector.emit(obj, flowKt__MergeKt$mapLatest$1) == coroutine_suspended) {
            return coroutine_suspended;
        }
        Object obj4 = obj2;
        FlowKt__MergeKt$mapLatest$1 flowKt__MergeKt$mapLatest$12 = flowKt__MergeKt$mapLatest$1;
        return Unit.INSTANCE;
    }
}

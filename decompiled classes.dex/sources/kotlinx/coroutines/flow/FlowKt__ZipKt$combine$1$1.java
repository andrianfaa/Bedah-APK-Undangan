package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function3;

@Metadata(d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00052\u000e\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0007H@"}, d2 = {"<anonymous>", "", "T1", "T2", "R", "Lkotlinx/coroutines/flow/FlowCollector;", "it", "", ""}, k = 3, mv = {1, 6, 0}, xi = 48)
@DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ZipKt$combine$1$1", f = "Zip.kt", i = {}, l = {33, 33}, m = "invokeSuspend", n = {}, s = {})
/* compiled from: Zip.kt */
final class FlowKt__ZipKt$combine$1$1 extends SuspendLambda implements Function3<FlowCollector<? super R>, Object[], Continuation<? super Unit>, Object> {
    final /* synthetic */ Function3<T1, T2, Continuation<? super R>, Object> $transform;
    private /* synthetic */ Object L$0;
    /* synthetic */ Object L$1;
    int label;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    FlowKt__ZipKt$combine$1$1(Function3<? super T1, ? super T2, ? super Continuation<? super R>, ? extends Object> function3, Continuation<? super FlowKt__ZipKt$combine$1$1> continuation) {
        super(3, continuation);
        this.$transform = function3;
    }

    public final Object invoke(FlowCollector<? super R> flowCollector, Object[] objArr, Continuation<? super Unit> continuation) {
        FlowKt__ZipKt$combine$1$1 flowKt__ZipKt$combine$1$1 = new FlowKt__ZipKt$combine$1$1(this.$transform, continuation);
        flowKt__ZipKt$combine$1$1.L$0 = flowCollector;
        flowKt__ZipKt$combine$1$1.L$1 = objArr;
        return flowKt__ZipKt$combine$1$1.invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        FlowCollector flowCollector;
        FlowKt__ZipKt$combine$1$1 flowKt__ZipKt$combine$1$1;
        Object obj2;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                FlowCollector flowCollector2 = (FlowCollector) this.L$0;
                Object[] objArr = (Object[]) this.L$1;
                Function3<T1, T2, Continuation<? super R>, Object> function3 = this.$transform;
                Object obj3 = objArr[0];
                Object obj4 = objArr[1];
                this.L$0 = flowCollector2;
                this.label = 1;
                Object invoke = function3.invoke(obj3, obj4, this);
                if (invoke != coroutine_suspended) {
                    obj2 = obj;
                    obj = invoke;
                    flowCollector = flowCollector2;
                    flowKt__ZipKt$combine$1$1 = this;
                    break;
                } else {
                    return coroutine_suspended;
                }
            case 1:
                ResultKt.throwOnFailure(obj);
                flowCollector = (FlowCollector) this.L$0;
                flowKt__ZipKt$combine$1$1 = this;
                obj2 = obj;
                break;
            case 2:
                ResultKt.throwOnFailure(obj);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        flowKt__ZipKt$combine$1$1.L$0 = null;
        flowKt__ZipKt$combine$1$1.label = 2;
        if (flowCollector.emit(obj, flowKt__ZipKt$combine$1$1) == coroutine_suspended) {
            return coroutine_suspended;
        }
        Object obj5 = obj2;
        FlowKt__ZipKt$combine$1$1 flowKt__ZipKt$combine$1$12 = flowKt__ZipKt$combine$1$1;
        return Unit.INSTANCE;
    }
}

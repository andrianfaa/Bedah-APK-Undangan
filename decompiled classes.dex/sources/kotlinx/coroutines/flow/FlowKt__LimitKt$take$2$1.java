package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.jvm.internal.SuspendFunction;
import kotlin.jvm.internal.Ref;

@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002H@¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "T", "value", "emit", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"}, k = 3, mv = {1, 6, 0}, xi = 48)
/* compiled from: Limit.kt */
final class FlowKt__LimitKt$take$2$1<T> implements FlowCollector, SuspendFunction {
    final /* synthetic */ Ref.IntRef $consumed;
    final /* synthetic */ int $count;
    final /* synthetic */ FlowCollector<T> $this_unsafeFlow;

    FlowKt__LimitKt$take$2$1(Ref.IntRef intRef, int i, FlowCollector<? super T> flowCollector) {
        this.$consumed = intRef;
        this.$count = i;
        this.$this_unsafeFlow = flowCollector;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0031  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object emit(T r7, kotlin.coroutines.Continuation<? super kotlin.Unit> r8) {
        /*
            r6 = this;
            boolean r0 = r8 instanceof kotlinx.coroutines.flow.FlowKt__LimitKt$take$2$1$emit$1
            if (r0 == 0) goto L_0x0014
            r0 = r8
            kotlinx.coroutines.flow.FlowKt__LimitKt$take$2$1$emit$1 r0 = (kotlinx.coroutines.flow.FlowKt__LimitKt$take$2$1$emit$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L_0x0014
            int r8 = r0.label
            int r8 = r8 - r2
            r0.label = r8
            goto L_0x0019
        L_0x0014:
            kotlinx.coroutines.flow.FlowKt__LimitKt$take$2$1$emit$1 r0 = new kotlinx.coroutines.flow.FlowKt__LimitKt$take$2$1$emit$1
            r0.<init>(r6, r8)
        L_0x0019:
            r8 = r0
            java.lang.Object r0 = r8.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r8.label
            switch(r2) {
                case 0: goto L_0x0031;
                case 1: goto L_0x002d;
                case 2: goto L_0x002d;
                default: goto L_0x0025;
            }
        L_0x0025:
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
            java.lang.String r8 = "call to 'resume' before 'invoke' with coroutine"
            r7.<init>(r8)
            throw r7
        L_0x002d:
            kotlin.ResultKt.throwOnFailure(r0)
            goto L_0x0050
        L_0x0031:
            kotlin.ResultKt.throwOnFailure(r0)
            r2 = r6
            kotlin.jvm.internal.Ref$IntRef r3 = r2.$consumed
            int r4 = r3.element
            r5 = 1
            int r4 = r4 + r5
            r3.element = r4
            kotlin.jvm.internal.Ref$IntRef r3 = r2.$consumed
            int r3 = r3.element
            int r4 = r2.$count
            if (r3 >= r4) goto L_0x0053
            kotlinx.coroutines.flow.FlowCollector<T> r3 = r2.$this_unsafeFlow
            r8.label = r5
            java.lang.Object r7 = r3.emit(r7, r8)
            if (r7 != r1) goto L_0x0050
            return r1
        L_0x0050:
            kotlin.Unit r7 = kotlin.Unit.INSTANCE
            return r7
        L_0x0053:
            kotlinx.coroutines.flow.FlowCollector<T> r3 = r2.$this_unsafeFlow
            r4 = 2
            r8.label = r4
            java.lang.Object r7 = kotlinx.coroutines.flow.FlowKt__LimitKt.emitAbort$FlowKt__LimitKt(r3, r7, r8)
            if (r7 != r1) goto L_0x005f
            return r1
        L_0x005f:
            goto L_0x0050
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__LimitKt$take$2$1.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
    }
}

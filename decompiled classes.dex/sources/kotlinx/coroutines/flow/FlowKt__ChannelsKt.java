package kotlinx.coroutines.flow;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.BroadcastChannel;
import kotlinx.coroutines.channels.BufferOverflow;
import kotlinx.coroutines.channels.ReceiveChannel;
import kotlinx.coroutines.flow.internal.ChannelFlowKt;

@Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001e\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0007\u001a\u001c\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0005\u001a/\u0010\u0006\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H@ø\u0001\u0000¢\u0006\u0002\u0010\n\u001a9\u0010\u000b\u001a\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00020\u00052\u0006\u0010\f\u001a\u00020\rH@ø\u0001\u0000¢\u0006\u0004\b\u000e\u0010\u000f\u001a&\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u0012H\u0007\u001a\u001c\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0005\u0002\u0004\n\u0002\b\u0019¨\u0006\u0014"}, d2 = {"asFlow", "Lkotlinx/coroutines/flow/Flow;", "T", "Lkotlinx/coroutines/channels/BroadcastChannel;", "consumeAsFlow", "Lkotlinx/coroutines/channels/ReceiveChannel;", "emitAll", "", "Lkotlinx/coroutines/flow/FlowCollector;", "channel", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "emitAllImpl", "consume", "", "emitAllImpl$FlowKt__ChannelsKt", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlinx/coroutines/channels/ReceiveChannel;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "produceIn", "scope", "Lkotlinx/coroutines/CoroutineScope;", "receiveAsFlow", "kotlinx-coroutines-core"}, k = 5, mv = {1, 6, 0}, xi = 48, xs = "kotlinx/coroutines/flow/FlowKt")
/* compiled from: Channels.kt */
final /* synthetic */ class FlowKt__ChannelsKt {
    @Deprecated(level = DeprecationLevel.WARNING, message = "'BroadcastChannel' is obsolete and all corresponding operators are deprecated in the favour of StateFlow and SharedFlow")
    public static final <T> Flow<T> asFlow(BroadcastChannel<T> $this$asFlow) {
        return new FlowKt__ChannelsKt$asFlow$$inlined$unsafeFlow$1($this$asFlow);
    }

    public static final <T> Flow<T> consumeAsFlow(ReceiveChannel<? extends T> $this$consumeAsFlow) {
        return new ChannelAsFlow<>($this$consumeAsFlow, true, (CoroutineContext) null, 0, (BufferOverflow) null, 28, (DefaultConstructorMarker) null);
    }

    public static final <T> Object emitAll(FlowCollector<? super T> $this$emitAll, ReceiveChannel<? extends T> channel, Continuation<? super Unit> $completion) {
        Object emitAllImpl$FlowKt__ChannelsKt = emitAllImpl$FlowKt__ChannelsKt($this$emitAll, channel, true, $completion);
        return emitAllImpl$FlowKt__ChannelsKt == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? emitAllImpl$FlowKt__ChannelsKt : Unit.INSTANCE;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r10.L$0 = r2;
        r10.L$1 = r9;
        r10.Z$0 = r7;
        r10.label = 1;
        r4 = r9.m1553receiveCatchingJP2dKIU(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x007c, code lost:
        if (r4 != r1) goto L_0x007f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x007e, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x007f, code lost:
        r6 = r8;
        r8 = r7;
        r3 = r2;
        r2 = r9;
        r9 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0085, code lost:
        r7 = r9;
        r9 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x008c, code lost:
        if (kotlinx.coroutines.channels.ChannelResult.m1542isClosedimpl(r9) == false) goto L_0x00a2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x008e, code lost:
        r1 = kotlinx.coroutines.channels.ChannelResult.m1538exceptionOrNullimpl(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0092, code lost:
        if (r1 != null) goto L_0x009e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0095, code lost:
        if (r8 == false) goto L_0x009a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0097, code lost:
        kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r2, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x009d, code lost:
        return kotlin.Unit.INSTANCE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
        throw r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00a2, code lost:
        r4 = kotlinx.coroutines.channels.ChannelResult.m1540getOrThrowimpl(r9);
        r10.L$0 = r3;
        r10.L$1 = r2;
        r10.Z$0 = r8;
        r10.label = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00b3, code lost:
        if (r3.emit(r4, r10) != r1) goto L_0x00b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00b5, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00b6, code lost:
        r9 = r2;
        r2 = r3;
        r6 = r8;
        r8 = r7;
        r7 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00bc, code lost:
        r1 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00bd, code lost:
        r9 = r2;
        r2 = r3;
        r6 = r8;
        r8 = r7;
        r7 = r6;
     */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0040  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0060  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final <T> java.lang.Object emitAllImpl$FlowKt__ChannelsKt(kotlinx.coroutines.flow.FlowCollector<? super T> r7, kotlinx.coroutines.channels.ReceiveChannel<? extends T> r8, boolean r9, kotlin.coroutines.Continuation<? super kotlin.Unit> r10) {
        /*
            boolean r0 = r10 instanceof kotlinx.coroutines.flow.FlowKt__ChannelsKt$emitAllImpl$1
            if (r0 == 0) goto L_0x0014
            r0 = r10
            kotlinx.coroutines.flow.FlowKt__ChannelsKt$emitAllImpl$1 r0 = (kotlinx.coroutines.flow.FlowKt__ChannelsKt$emitAllImpl$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L_0x0014
            int r10 = r0.label
            int r10 = r10 - r2
            r0.label = r10
            goto L_0x0019
        L_0x0014:
            kotlinx.coroutines.flow.FlowKt__ChannelsKt$emitAllImpl$1 r0 = new kotlinx.coroutines.flow.FlowKt__ChannelsKt$emitAllImpl$1
            r0.<init>(r10)
        L_0x0019:
            r10 = r0
            java.lang.Object r0 = r10.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r10.label
            switch(r2) {
                case 0: goto L_0x0060;
                case 1: goto L_0x0040;
                case 2: goto L_0x002d;
                default: goto L_0x0025;
            }
        L_0x0025:
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
            java.lang.String r8 = "call to 'resume' before 'invoke' with coroutine"
            r7.<init>(r8)
            throw r7
        L_0x002d:
            boolean r7 = r10.Z$0
            r8 = 0
            java.lang.Object r9 = r10.L$1
            kotlinx.coroutines.channels.ReceiveChannel r9 = (kotlinx.coroutines.channels.ReceiveChannel) r9
            java.lang.Object r2 = r10.L$0
            kotlinx.coroutines.flow.FlowCollector r2 = (kotlinx.coroutines.flow.FlowCollector) r2
            kotlin.ResultKt.throwOnFailure(r0)     // Catch:{ all -> 0x003d }
            goto L_0x00bb
        L_0x003d:
            r1 = move-exception
            goto L_0x00c2
        L_0x0040:
            r7 = 0
            boolean r8 = r10.Z$0
            r9 = 0
            java.lang.Object r2 = r10.L$1
            kotlinx.coroutines.channels.ReceiveChannel r2 = (kotlinx.coroutines.channels.ReceiveChannel) r2
            java.lang.Object r3 = r10.L$0
            kotlinx.coroutines.flow.FlowCollector r3 = (kotlinx.coroutines.flow.FlowCollector) r3
            kotlin.ResultKt.throwOnFailure(r0)     // Catch:{ all -> 0x0057 }
            r4 = r0
            kotlinx.coroutines.channels.ChannelResult r4 = (kotlinx.coroutines.channels.ChannelResult) r4     // Catch:{ all -> 0x0057 }
            java.lang.Object r4 = r4.m1546unboximpl()     // Catch:{ all -> 0x0057 }
            goto L_0x0085
        L_0x0057:
            r1 = move-exception
            r7 = r9
            r9 = r2
            r2 = r3
            r6 = r8
            r8 = r7
            r7 = r6
            goto L_0x00c2
        L_0x0060:
            kotlin.ResultKt.throwOnFailure(r0)
            kotlinx.coroutines.flow.FlowKt.ensureActive(r7)
            r2 = 0
            r6 = r2
            r2 = r7
            r7 = r9
            r9 = r8
            r8 = r6
        L_0x006c:
            r3 = 0
            r10.L$0 = r2     // Catch:{ all -> 0x003d }
            r10.L$1 = r9     // Catch:{ all -> 0x003d }
            r10.Z$0 = r7     // Catch:{ all -> 0x003d }
            r4 = 1
            r10.label = r4     // Catch:{ all -> 0x003d }
            java.lang.Object r4 = r9.m1553receiveCatchingJP2dKIU(r10)     // Catch:{ all -> 0x003d }
            if (r4 != r1) goto L_0x007f
            return r1
        L_0x007f:
            r6 = r8
            r8 = r7
            r7 = r3
            r3 = r2
            r2 = r9
            r9 = r6
        L_0x0085:
            r7 = r9
            r9 = r4
            boolean r4 = kotlinx.coroutines.channels.ChannelResult.m1542isClosedimpl(r9)     // Catch:{ all -> 0x00bc }
            if (r4 == 0) goto L_0x00a2
            java.lang.Throwable r1 = kotlinx.coroutines.channels.ChannelResult.m1538exceptionOrNullimpl(r9)     // Catch:{ all -> 0x00bc }
            if (r1 != 0) goto L_0x009e
            if (r8 == 0) goto L_0x009a
            kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r2, r7)
        L_0x009a:
            kotlin.Unit r9 = kotlin.Unit.INSTANCE
            return r9
        L_0x009e:
            r9 = r1
            r1 = 0
            throw r9     // Catch:{ all -> 0x00bc }
        L_0x00a2:
            java.lang.Object r4 = kotlinx.coroutines.channels.ChannelResult.m1540getOrThrowimpl(r9)     // Catch:{ all -> 0x00bc }
            r10.L$0 = r3     // Catch:{ all -> 0x00bc }
            r10.L$1 = r2     // Catch:{ all -> 0x00bc }
            r10.Z$0 = r8     // Catch:{ all -> 0x00bc }
            r5 = 2
            r10.label = r5     // Catch:{ all -> 0x00bc }
            java.lang.Object r4 = r3.emit(r4, r10)     // Catch:{ all -> 0x00bc }
            if (r4 != r1) goto L_0x00b6
            return r1
        L_0x00b6:
            r9 = r2
            r2 = r3
            r6 = r8
            r8 = r7
            r7 = r6
        L_0x00bb:
            goto L_0x006c
        L_0x00bc:
            r1 = move-exception
            r9 = r2
            r2 = r3
            r6 = r8
            r8 = r7
            r7 = r6
        L_0x00c2:
            r8 = r1
            throw r1     // Catch:{ all -> 0x00c5 }
        L_0x00c5:
            r1 = move-exception
            if (r7 == 0) goto L_0x00cb
            kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r9, r8)
        L_0x00cb:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__ChannelsKt.emitAllImpl$FlowKt__ChannelsKt(kotlinx.coroutines.flow.FlowCollector, kotlinx.coroutines.channels.ReceiveChannel, boolean, kotlin.coroutines.Continuation):java.lang.Object");
    }

    public static final <T> ReceiveChannel<T> produceIn(Flow<? extends T> $this$produceIn, CoroutineScope scope) {
        return ChannelFlowKt.asChannelFlow($this$produceIn).produceImpl(scope);
    }

    public static final <T> Flow<T> receiveAsFlow(ReceiveChannel<? extends T> $this$receiveAsFlow) {
        return new ChannelAsFlow<>($this$receiveAsFlow, false, (CoroutineContext) null, 0, (BufferOverflow) null, 28, (DefaultConstructorMarker) null);
    }
}

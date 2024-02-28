package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.channels.BufferOverflow;
import kotlinx.coroutines.flow.internal.ChannelFlowOperatorImpl;
import kotlinx.coroutines.flow.internal.FusibleFlow;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000&\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u0015\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0002¢\u0006\u0002\b\u0004\u001a(\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00062\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001a0\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00062\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b\u001a\u001c\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u0006\u001a\u001c\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u0006\u001a$\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0006\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00062\u0006\u0010\u0002\u001a\u00020\u0003¨\u0006\u000f"}, d2 = {"checkFlowContext", "", "context", "Lkotlin/coroutines/CoroutineContext;", "checkFlowContext$FlowKt__ContextKt", "buffer", "Lkotlinx/coroutines/flow/Flow;", "T", "capacity", "", "onBufferOverflow", "Lkotlinx/coroutines/channels/BufferOverflow;", "cancellable", "conflate", "flowOn", "kotlinx-coroutines-core"}, k = 5, mv = {1, 6, 0}, xi = 48, xs = "kotlinx/coroutines/flow/FlowKt")
/* compiled from: 0193 */
final /* synthetic */ class FlowKt__ContextKt {
    public static final <T> Flow<T> buffer(Flow<? extends T> $this$buffer, int capacity, BufferOverflow onBufferOverflow) {
        boolean z = false;
        if (capacity >= 0 || capacity == -2 || capacity == -1) {
            if (capacity != -1 || onBufferOverflow == BufferOverflow.SUSPEND) {
                z = true;
            }
            if (z) {
                int i = capacity;
                BufferOverflow bufferOverflow = onBufferOverflow;
                if (i == -1) {
                    i = 0;
                    bufferOverflow = BufferOverflow.DROP_OLDEST;
                }
                return $this$buffer instanceof FusibleFlow ? FusibleFlow.DefaultImpls.fuse$default((FusibleFlow) $this$buffer, (CoroutineContext) null, i, bufferOverflow, 1, (Object) null) : new ChannelFlowOperatorImpl<>($this$buffer, (CoroutineContext) null, i, bufferOverflow, 2, (DefaultConstructorMarker) null);
            }
            throw new IllegalArgumentException("CONFLATED capacity cannot be used with non-default onBufferOverflow".toString());
        }
        String stringPlus = Intrinsics.stringPlus("Buffer size should be non-negative, BUFFERED, or CONFLATED, but was ", Integer.valueOf(capacity));
        Log1F380D.a((Object) stringPlus);
        throw new IllegalArgumentException(stringPlus.toString());
    }

    public static /* synthetic */ Flow buffer$default(Flow flow, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = -2;
        }
        return buffer$default(flow, i, (BufferOverflow) null, 2, (Object) null);
    }

    public static /* synthetic */ Flow buffer$default(Flow flow, int i, BufferOverflow bufferOverflow, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = -2;
        }
        if ((i2 & 2) != 0) {
            bufferOverflow = BufferOverflow.SUSPEND;
        }
        return FlowKt.buffer(flow, i, bufferOverflow);
    }

    public static final <T> Flow<T> cancellable(Flow<? extends T> $this$cancellable) {
        return $this$cancellable instanceof CancellableFlow ? $this$cancellable : new CancellableFlowImpl<>($this$cancellable);
    }

    public static final <T> Flow<T> conflate(Flow<? extends T> $this$conflate) {
        return buffer$default($this$conflate, -1, (BufferOverflow) null, 2, (Object) null);
    }

    public static final <T> Flow<T> flowOn(Flow<? extends T> $this$flowOn, CoroutineContext context) {
        checkFlowContext$FlowKt__ContextKt(context);
        return Intrinsics.areEqual((Object) context, (Object) EmptyCoroutineContext.INSTANCE) ? $this$flowOn : $this$flowOn instanceof FusibleFlow ? FusibleFlow.DefaultImpls.fuse$default((FusibleFlow) $this$flowOn, context, 0, (BufferOverflow) null, 6, (Object) null) : new ChannelFlowOperatorImpl<>($this$flowOn, context, 0, (BufferOverflow) null, 12, (DefaultConstructorMarker) null);
    }

    private static final void checkFlowContext$FlowKt__ContextKt(CoroutineContext context) {
        if (!(context.get(Job.Key) == null)) {
            String stringPlus = Intrinsics.stringPlus("Flow context cannot contain job in it. Had ", context);
            Log1F380D.a((Object) stringPlus);
            throw new IllegalArgumentException(stringPlus.toString());
        }
    }
}

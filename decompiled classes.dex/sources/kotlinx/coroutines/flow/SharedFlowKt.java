package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.channels.BufferOverflow;
import kotlinx.coroutines.flow.internal.ChannelFlowOperatorImpl;
import kotlinx.coroutines.internal.Symbol;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000L\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u001a0\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0005\"\u0004\b\u0000\u0010\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\u000b\u001a6\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00060\r\"\u0004\b\u0000\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00060\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bH\u0000\u001a#\u0010\u0012\u001a\u0004\u0018\u00010\u0013*\n\u0012\u0006\u0012\u0004\u0018\u00010\u00130\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0002¢\u0006\u0002\u0010\u0017\u001a+\u0010\u0018\u001a\u00020\u0019*\n\u0012\u0006\u0012\u0004\u0018\u00010\u00130\u00142\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u001a\u001a\u0004\u0018\u00010\u0013H\u0002¢\u0006\u0002\u0010\u001b\"\u0016\u0010\u0000\u001a\u00020\u00018\u0000X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0002\u0010\u0003¨\u0006\u001c"}, d2 = {"NO_VALUE", "Lkotlinx/coroutines/internal/Symbol;", "getNO_VALUE$annotations", "()V", "MutableSharedFlow", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "T", "replay", "", "extraBufferCapacity", "onBufferOverflow", "Lkotlinx/coroutines/channels/BufferOverflow;", "fuseSharedFlow", "Lkotlinx/coroutines/flow/Flow;", "Lkotlinx/coroutines/flow/SharedFlow;", "context", "Lkotlin/coroutines/CoroutineContext;", "capacity", "getBufferAt", "", "", "index", "", "([Ljava/lang/Object;J)Ljava/lang/Object;", "setBufferAt", "", "item", "([Ljava/lang/Object;JLjava/lang/Object;)V", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: 0198 */
public final class SharedFlowKt {
    public static final Symbol NO_VALUE = new Symbol("NO_VALUE");

    public static final <T> MutableSharedFlow<T> MutableSharedFlow(int replay, int extraBufferCapacity, BufferOverflow onBufferOverflow) {
        boolean z = true;
        if (replay >= 0) {
            if (extraBufferCapacity >= 0) {
                if (replay <= 0 && extraBufferCapacity <= 0 && onBufferOverflow != BufferOverflow.SUSPEND) {
                    z = false;
                }
                if (z) {
                    int i = replay + extraBufferCapacity;
                    return new SharedFlowImpl<>(replay, i < 0 ? Integer.MAX_VALUE : i, onBufferOverflow);
                }
                String stringPlus = Intrinsics.stringPlus("replay or extraBufferCapacity must be positive with non-default onBufferOverflow strategy ", onBufferOverflow);
                Log1F380D.a((Object) stringPlus);
                throw new IllegalArgumentException(stringPlus.toString());
            }
            String stringPlus2 = Intrinsics.stringPlus("extraBufferCapacity cannot be negative, but was ", Integer.valueOf(extraBufferCapacity));
            Log1F380D.a((Object) stringPlus2);
            throw new IllegalArgumentException(stringPlus2.toString());
        }
        String stringPlus3 = Intrinsics.stringPlus("replay cannot be negative, but was ", Integer.valueOf(replay));
        Log1F380D.a((Object) stringPlus3);
        throw new IllegalArgumentException(stringPlus3.toString());
    }

    public static /* synthetic */ MutableSharedFlow MutableSharedFlow$default(int i, int i2, BufferOverflow bufferOverflow, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = 0;
        }
        if ((i3 & 4) != 0) {
            bufferOverflow = BufferOverflow.SUSPEND;
        }
        return MutableSharedFlow(i, i2, bufferOverflow);
    }

    public static final <T> Flow<T> fuseSharedFlow(SharedFlow<? extends T> $this$fuseSharedFlow, CoroutineContext context, int capacity, BufferOverflow onBufferOverflow) {
        return ((capacity == 0 || capacity == -3) && onBufferOverflow == BufferOverflow.SUSPEND) ? $this$fuseSharedFlow : new ChannelFlowOperatorImpl<>($this$fuseSharedFlow, context, capacity, onBufferOverflow);
    }

    /* access modifiers changed from: private */
    public static final Object getBufferAt(Object[] $this$getBufferAt, long index) {
        return $this$getBufferAt[((int) index) & ($this$getBufferAt.length - 1)];
    }

    public static /* synthetic */ void getNO_VALUE$annotations() {
    }

    /* access modifiers changed from: private */
    public static final void setBufferAt(Object[] $this$setBufferAt, long index, Object item) {
        $this$setBufferAt[((int) index) & ($this$setBufferAt.length - 1)] = item;
    }
}

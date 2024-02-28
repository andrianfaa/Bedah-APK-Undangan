package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.channels.BufferOverflow;
import kotlinx.coroutines.flow.internal.NullSurrogateKt;
import kotlinx.coroutines.internal.Symbol;

@Metadata(d1 = {"\u0000@\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u001a\u001f\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b2\u0006\u0010\t\u001a\u0002H\b¢\u0006\u0002\u0010\n\u001a6\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\b0\f\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0000\u001a2\u0010\u0014\u001a\u0002H\b\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\u00072\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u0002H\b\u0012\u0004\u0012\u0002H\b0\u0016H\b¢\u0006\u0002\u0010\u0017\u001a-\u0010\u0018\u001a\u00020\u0019\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\u00072\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u0002H\b\u0012\u0004\u0012\u0002H\b0\u0016H\b\u001a2\u0010\u001a\u001a\u0002H\b\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\u00072\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u0002H\b\u0012\u0004\u0012\u0002H\b0\u0016H\b¢\u0006\u0002\u0010\u0017\"\u0016\u0010\u0000\u001a\u00020\u00018\u0002X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0002\u0010\u0003\"\u0016\u0010\u0004\u001a\u00020\u00018\u0002X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0005\u0010\u0003¨\u0006\u001b"}, d2 = {"NONE", "Lkotlinx/coroutines/internal/Symbol;", "getNONE$annotations", "()V", "PENDING", "getPENDING$annotations", "MutableStateFlow", "Lkotlinx/coroutines/flow/MutableStateFlow;", "T", "value", "(Ljava/lang/Object;)Lkotlinx/coroutines/flow/MutableStateFlow;", "fuseStateFlow", "Lkotlinx/coroutines/flow/Flow;", "Lkotlinx/coroutines/flow/StateFlow;", "context", "Lkotlin/coroutines/CoroutineContext;", "capacity", "", "onBufferOverflow", "Lkotlinx/coroutines/channels/BufferOverflow;", "getAndUpdate", "function", "Lkotlin/Function1;", "(Lkotlinx/coroutines/flow/MutableStateFlow;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "update", "", "updateAndGet", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: StateFlow.kt */
public final class StateFlowKt {
    /* access modifiers changed from: private */
    public static final Symbol NONE = new Symbol("NONE");
    /* access modifiers changed from: private */
    public static final Symbol PENDING = new Symbol("PENDING");

    public static final <T> MutableStateFlow<T> MutableStateFlow(T value) {
        return new StateFlowImpl<>(value == null ? NullSurrogateKt.NULL : value);
    }

    public static final <T> Flow<T> fuseStateFlow(StateFlow<? extends T> $this$fuseStateFlow, CoroutineContext context, int capacity, BufferOverflow onBufferOverflow) {
        boolean z = true;
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(capacity != -1)) {
                throw new AssertionError();
            }
        }
        if (capacity < 0 || capacity >= 2) {
            z = false;
        }
        return ((z || capacity == -2) && onBufferOverflow == BufferOverflow.DROP_OLDEST) ? $this$fuseStateFlow : SharedFlowKt.fuseSharedFlow($this$fuseStateFlow, context, capacity, onBufferOverflow);
    }

    public static final <T> T getAndUpdate(MutableStateFlow<T> $this$getAndUpdate, Function1<? super T, ? extends T> function) {
        T value;
        do {
            value = $this$getAndUpdate.getValue();
        } while (!$this$getAndUpdate.compareAndSet(value, function.invoke(value)));
        return value;
    }

    private static /* synthetic */ void getNONE$annotations() {
    }

    private static /* synthetic */ void getPENDING$annotations() {
    }

    public static final <T> void update(MutableStateFlow<T> $this$update, Function1<? super T, ? extends T> function) {
        T value;
        do {
            value = $this$update.getValue();
        } while (!$this$update.compareAndSet(value, function.invoke(value)));
    }

    public static final <T> T updateAndGet(MutableStateFlow<T> $this$updateAndGet, Function1<? super T, ? extends T> function) {
        T value;
        T invoke;
        do {
            value = $this$updateAndGet.getValue();
            invoke = function.invoke(value);
        } while (!$this$updateAndGet.compareAndSet(value, invoke));
        return invoke;
    }
}

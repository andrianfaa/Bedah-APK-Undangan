package kotlinx.coroutines.flow.internal;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.internal.AbstractSharedFlowSlot;

@Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b \u0018\u0000*\f\b\u0000\u0010\u0001*\u0006\u0012\u0002\b\u00030\u00022\u00060\u0003j\u0002`\u0004B\u0005¢\u0006\u0002\u0010\u0005J\r\u0010\u0018\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010\u0019J\r\u0010\u001a\u001a\u00028\u0000H$¢\u0006\u0002\u0010\u0019J\u001d\u0010\u001b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u000e2\u0006\u0010\u001c\u001a\u00020\tH$¢\u0006\u0002\u0010\u001dJ\u001d\u0010\u001e\u001a\u00020\u001f2\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u001f0!H\bJ\u0015\u0010\"\u001a\u00020\u001f2\u0006\u0010#\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010$R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R:\u0010\u000f\u001a\f\u0012\u0006\u0012\u0004\u0018\u00018\u0000\u0018\u00010\u000e2\u0010\u0010\b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00018\u0000\u0018\u00010\u000e@BX\u000e¢\u0006\u0010\n\u0002\u0010\u0013\u0012\u0004\b\u0010\u0010\u0005\u001a\u0004\b\u0011\u0010\u0012R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\t0\u00158F¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017¨\u0006%"}, d2 = {"Lkotlinx/coroutines/flow/internal/AbstractSharedFlow;", "S", "Lkotlinx/coroutines/flow/internal/AbstractSharedFlowSlot;", "", "Lkotlinx/coroutines/internal/SynchronizedObject;", "()V", "_subscriptionCount", "Lkotlinx/coroutines/flow/internal/SubscriptionCountStateFlow;", "<set-?>", "", "nCollectors", "getNCollectors", "()I", "nextIndex", "", "slots", "getSlots$annotations", "getSlots", "()[Lkotlinx/coroutines/flow/internal/AbstractSharedFlowSlot;", "[Lkotlinx/coroutines/flow/internal/AbstractSharedFlowSlot;", "subscriptionCount", "Lkotlinx/coroutines/flow/StateFlow;", "getSubscriptionCount", "()Lkotlinx/coroutines/flow/StateFlow;", "allocateSlot", "()Lkotlinx/coroutines/flow/internal/AbstractSharedFlowSlot;", "createSlot", "createSlotArray", "size", "(I)[Lkotlinx/coroutines/flow/internal/AbstractSharedFlowSlot;", "forEachSlotLocked", "", "block", "Lkotlin/Function1;", "freeSlot", "slot", "(Lkotlinx/coroutines/flow/internal/AbstractSharedFlowSlot;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: AbstractSharedFlow.kt */
public abstract class AbstractSharedFlow<S extends AbstractSharedFlowSlot<?>> {
    private SubscriptionCountStateFlow _subscriptionCount;
    /* access modifiers changed from: private */
    public int nCollectors;
    private int nextIndex;
    /* access modifiers changed from: private */
    public S[] slots;

    protected static /* synthetic */ void getSlots$annotations() {
    }

    /* access modifiers changed from: protected */
    public final S allocateSlot() {
        S s;
        SubscriptionCountStateFlow subscriptionCountStateFlow;
        synchronized (this) {
            S[] slots2 = getSlots();
            if (slots2 == null) {
                S[] createSlotArray = createSlotArray(2);
                this.slots = createSlotArray;
                slots2 = createSlotArray;
            } else if (getNCollectors() >= slots2.length) {
                S[] copyOf = Arrays.copyOf(slots2, slots2.length * 2);
                Intrinsics.checkNotNullExpressionValue(copyOf, "copyOf(this, newSize)");
                this.slots = (AbstractSharedFlowSlot[]) copyOf;
                slots2 = (AbstractSharedFlowSlot[]) copyOf;
            }
            int i = this.nextIndex;
            do {
                S s2 = slots2[i];
                if (s2 == null) {
                    s2 = createSlot();
                    slots2[i] = s2;
                }
                s = s2;
                i++;
                if (i >= slots2.length) {
                    i = 0;
                }
            } while (!s.allocateLocked(this));
            this.nextIndex = i;
            this.nCollectors = getNCollectors() + 1;
            subscriptionCountStateFlow = this._subscriptionCount;
        }
        S s3 = s;
        if (subscriptionCountStateFlow != null) {
            subscriptionCountStateFlow.increment(1);
        }
        return s3;
    }

    /* access modifiers changed from: protected */
    public abstract S createSlot();

    /* access modifiers changed from: protected */
    public abstract S[] createSlotArray(int i);

    /* access modifiers changed from: protected */
    public final void forEachSlotLocked(Function1<? super S, Unit> block) {
        AbstractSharedFlowSlot[] access$getSlots;
        if (this.nCollectors != 0 && (access$getSlots = this.slots) != null) {
            int i = 0;
            int length = access$getSlots.length;
            while (i < length) {
                AbstractSharedFlowSlot abstractSharedFlowSlot = access$getSlots[i];
                i++;
                AbstractSharedFlowSlot abstractSharedFlowSlot2 = abstractSharedFlowSlot;
                if (abstractSharedFlowSlot2 != null) {
                    block.invoke(abstractSharedFlowSlot2);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void freeSlot(S slot) {
        SubscriptionCountStateFlow subscriptionCountStateFlow;
        int i;
        Continuation[] freeLocked;
        synchronized (this) {
            this.nCollectors = getNCollectors() - 1;
            subscriptionCountStateFlow = this._subscriptionCount;
            i = 0;
            if (getNCollectors() == 0) {
                this.nextIndex = 0;
            }
            freeLocked = slot.freeLocked(this);
        }
        Continuation[] continuationArr = freeLocked;
        int length = continuationArr.length;
        while (i < length) {
            Continuation continuation = continuationArr[i];
            i++;
            if (continuation != null) {
                Result.Companion companion = Result.Companion;
                continuation.resumeWith(Result.m36constructorimpl(Unit.INSTANCE));
            }
        }
        if (subscriptionCountStateFlow != null) {
            subscriptionCountStateFlow.increment(-1);
        }
    }

    /* access modifiers changed from: protected */
    public final int getNCollectors() {
        return this.nCollectors;
    }

    /* access modifiers changed from: protected */
    public final S[] getSlots() {
        return this.slots;
    }

    public final StateFlow<Integer> getSubscriptionCount() {
        SubscriptionCountStateFlow subscriptionCountStateFlow;
        synchronized (this) {
            subscriptionCountStateFlow = this._subscriptionCount;
            if (subscriptionCountStateFlow == null) {
                subscriptionCountStateFlow = new SubscriptionCountStateFlow(getNCollectors());
                this._subscriptionCount = subscriptionCountStateFlow;
            }
        }
        return subscriptionCountStateFlow;
    }
}

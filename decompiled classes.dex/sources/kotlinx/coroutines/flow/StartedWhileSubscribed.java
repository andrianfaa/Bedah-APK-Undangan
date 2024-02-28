package kotlinx.coroutines.flow;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function1;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\u001c\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0016J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0002J\b\u0010\u0010\u001a\u00020\u000bH\u0017J\b\u0010\u0011\u001a\u00020\u0012H\u0016R\u000e\u0010\u0004\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lkotlinx/coroutines/flow/StartedWhileSubscribed;", "Lkotlinx/coroutines/flow/SharingStarted;", "stopTimeout", "", "replayExpiration", "(JJ)V", "command", "Lkotlinx/coroutines/flow/Flow;", "Lkotlinx/coroutines/flow/SharingCommand;", "subscriptionCount", "Lkotlinx/coroutines/flow/StateFlow;", "", "equals", "", "other", "", "hashCode", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: 0199 */
final class StartedWhileSubscribed implements SharingStarted {
    /* access modifiers changed from: private */
    public final long replayExpiration;
    /* access modifiers changed from: private */
    public final long stopTimeout;

    public StartedWhileSubscribed(long stopTimeout2, long replayExpiration2) {
        this.stopTimeout = stopTimeout2;
        this.replayExpiration = replayExpiration2;
        boolean z = true;
        if (stopTimeout2 >= 0) {
            if (!(replayExpiration2 < 0 ? false : z)) {
                throw new IllegalArgumentException(("replayExpiration(" + replayExpiration2 + " ms) cannot be negative").toString());
            }
            return;
        }
        throw new IllegalArgumentException(("stopTimeout(" + stopTimeout2 + " ms) cannot be negative").toString());
    }

    public Flow<SharingCommand> command(StateFlow<Integer> subscriptionCount) {
        return FlowKt.distinctUntilChanged(FlowKt.dropWhile(FlowKt.transformLatest(subscriptionCount, new StartedWhileSubscribed$command$1(this, (Continuation<? super StartedWhileSubscribed$command$1>) null)), new StartedWhileSubscribed$command$2((Continuation<? super StartedWhileSubscribed$command$2>) null)));
    }

    public boolean equals(Object other) {
        return (other instanceof StartedWhileSubscribed) && this.stopTimeout == ((StartedWhileSubscribed) other).stopTimeout && this.replayExpiration == ((StartedWhileSubscribed) other).replayExpiration;
    }

    public int hashCode() {
        return (Long.hashCode(this.stopTimeout) * 31) + Long.hashCode(this.replayExpiration);
    }

    public String toString() {
        List createListBuilder = CollectionsKt.createListBuilder(2);
        List list = createListBuilder;
        if (this.stopTimeout > 0) {
            list.add("stopTimeout=" + this.stopTimeout + "ms");
        }
        if (this.replayExpiration < Long.MAX_VALUE) {
            list.add("replayExpiration=" + this.replayExpiration + "ms");
        }
        List build = CollectionsKt.build(createListBuilder);
        StringBuilder append = new StringBuilder().append("SharingStarted.WhileSubscribed(");
        String joinToString$default = CollectionsKt.joinToString$default(build, (CharSequence) null, (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 63, (Object) null);
        Log1F380D.a((Object) joinToString$default);
        return append.append(joinToString$default).append(')').toString();
    }
}

package kotlin.sequences;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;

@Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H@"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlin/sequences/SequenceScope;"}, k = 3, mv = {1, 7, 1}, xi = 48)
@DebugMetadata(c = "kotlin.sequences.SequencesKt___SequencesKt$zipWithNext$2", f = "_Sequences.kt", i = {0, 0, 0}, l = {2864}, m = "invokeSuspend", n = {"$this$result", "iterator", "next"}, s = {"L$0", "L$1", "L$2"})
/* compiled from: _Sequences.kt */
final class SequencesKt___SequencesKt$zipWithNext$2 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Sequence<T> $this_zipWithNext;
    final /* synthetic */ Function2<T, T, R> $transform;
    private /* synthetic */ Object L$0;
    Object L$1;
    Object L$2;
    int label;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    SequencesKt___SequencesKt$zipWithNext$2(Sequence<? extends T> sequence, Function2<? super T, ? super T, ? extends R> function2, Continuation<? super SequencesKt___SequencesKt$zipWithNext$2> continuation) {
        super(2, continuation);
        this.$this_zipWithNext = sequence;
        this.$transform = function2;
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        SequencesKt___SequencesKt$zipWithNext$2 sequencesKt___SequencesKt$zipWithNext$2 = new SequencesKt___SequencesKt$zipWithNext$2(this.$this_zipWithNext, this.$transform, continuation);
        sequencesKt___SequencesKt$zipWithNext$2.L$0 = obj;
        return sequencesKt___SequencesKt$zipWithNext$2;
    }

    public final Object invoke(SequenceScope<? super R> sequenceScope, Continuation<? super Unit> continuation) {
        return ((SequencesKt___SequencesKt$zipWithNext$2) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        SequenceScope sequenceScope;
        Iterator<T> it;
        T t;
        SequencesKt___SequencesKt$zipWithNext$2 sequencesKt___SequencesKt$zipWithNext$2;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                sequencesKt___SequencesKt$zipWithNext$2 = this;
                SequenceScope sequenceScope2 = (SequenceScope) sequencesKt___SequencesKt$zipWithNext$2.L$0;
                it = sequencesKt___SequencesKt$zipWithNext$2.$this_zipWithNext.iterator();
                if (it.hasNext()) {
                    sequenceScope = sequenceScope2;
                    t = it.next();
                    break;
                } else {
                    return Unit.INSTANCE;
                }
            case 1:
                sequencesKt___SequencesKt$zipWithNext$2 = this;
                t = sequencesKt___SequencesKt$zipWithNext$2.L$2;
                it = (Iterator) sequencesKt___SequencesKt$zipWithNext$2.L$1;
                sequenceScope = (SequenceScope) sequencesKt___SequencesKt$zipWithNext$2.L$0;
                ResultKt.throwOnFailure(obj);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        while (it.hasNext()) {
            T next = it.next();
            sequencesKt___SequencesKt$zipWithNext$2.L$0 = sequenceScope;
            sequencesKt___SequencesKt$zipWithNext$2.L$1 = it;
            sequencesKt___SequencesKt$zipWithNext$2.L$2 = next;
            sequencesKt___SequencesKt$zipWithNext$2.label = 1;
            if (sequenceScope.yield(sequencesKt___SequencesKt$zipWithNext$2.$transform.invoke(t, next), sequencesKt___SequencesKt$zipWithNext$2) == coroutine_suspended) {
                return coroutine_suspended;
            }
            t = next;
        }
        return Unit.INSTANCE;
    }
}

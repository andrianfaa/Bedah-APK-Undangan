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
@DebugMetadata(c = "kotlin.sequences.SequencesKt___SequencesKt$runningFold$1", f = "_Sequences.kt", i = {0, 1, 1}, l = {2286, 2290}, m = "invokeSuspend", n = {"$this$sequence", "$this$sequence", "accumulator"}, s = {"L$0", "L$0", "L$1"})
/* compiled from: _Sequences.kt */
final class SequencesKt___SequencesKt$runningFold$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ R $initial;
    final /* synthetic */ Function2<R, T, R> $operation;
    final /* synthetic */ Sequence<T> $this_runningFold;
    private /* synthetic */ Object L$0;
    Object L$1;
    Object L$2;
    int label;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    SequencesKt___SequencesKt$runningFold$1(R r, Sequence<? extends T> sequence, Function2<? super R, ? super T, ? extends R> function2, Continuation<? super SequencesKt___SequencesKt$runningFold$1> continuation) {
        super(2, continuation);
        this.$initial = r;
        this.$this_runningFold = sequence;
        this.$operation = function2;
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        SequencesKt___SequencesKt$runningFold$1 sequencesKt___SequencesKt$runningFold$1 = new SequencesKt___SequencesKt$runningFold$1(this.$initial, this.$this_runningFold, this.$operation, continuation);
        sequencesKt___SequencesKt$runningFold$1.L$0 = obj;
        return sequencesKt___SequencesKt$runningFold$1;
    }

    public final Object invoke(SequenceScope<? super R> sequenceScope, Continuation<? super Unit> continuation) {
        return ((SequencesKt___SequencesKt$runningFold$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        SequenceScope sequenceScope;
        R r;
        Iterator<T> it;
        SequencesKt___SequencesKt$runningFold$1 sequencesKt___SequencesKt$runningFold$1;
        SequenceScope sequenceScope2;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                sequencesKt___SequencesKt$runningFold$1 = this;
                sequenceScope2 = (SequenceScope) sequencesKt___SequencesKt$runningFold$1.L$0;
                sequencesKt___SequencesKt$runningFold$1.L$0 = sequenceScope2;
                sequencesKt___SequencesKt$runningFold$1.label = 1;
                if (sequenceScope2.yield(sequencesKt___SequencesKt$runningFold$1.$initial, sequencesKt___SequencesKt$runningFold$1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                sequencesKt___SequencesKt$runningFold$1 = this;
                sequenceScope2 = (SequenceScope) sequencesKt___SequencesKt$runningFold$1.L$0;
                ResultKt.throwOnFailure(obj);
                break;
            case 2:
                sequencesKt___SequencesKt$runningFold$1 = this;
                it = (Iterator) sequencesKt___SequencesKt$runningFold$1.L$2;
                r = sequencesKt___SequencesKt$runningFold$1.L$1;
                sequenceScope = (SequenceScope) sequencesKt___SequencesKt$runningFold$1.L$0;
                ResultKt.throwOnFailure(obj);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        r = sequencesKt___SequencesKt$runningFold$1.$initial;
        sequenceScope = sequenceScope2;
        it = sequencesKt___SequencesKt$runningFold$1.$this_runningFold.iterator();
        while (it.hasNext()) {
            r = sequencesKt___SequencesKt$runningFold$1.$operation.invoke(r, it.next());
            sequencesKt___SequencesKt$runningFold$1.L$0 = sequenceScope;
            sequencesKt___SequencesKt$runningFold$1.L$1 = r;
            sequencesKt___SequencesKt$runningFold$1.L$2 = it;
            sequencesKt___SequencesKt$runningFold$1.label = 2;
            if (sequenceScope.yield(r, sequencesKt___SequencesKt$runningFold$1) == coroutine_suspended) {
                return coroutine_suspended;
            }
        }
        return Unit.INSTANCE;
    }
}

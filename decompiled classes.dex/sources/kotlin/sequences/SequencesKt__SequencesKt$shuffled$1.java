package kotlin.sequences;

import java.util.List;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.random.Random;

@Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H@"}, d2 = {"<anonymous>", "", "T", "Lkotlin/sequences/SequenceScope;"}, k = 3, mv = {1, 7, 1}, xi = 48)
@DebugMetadata(c = "kotlin.sequences.SequencesKt__SequencesKt$shuffled$1", f = "Sequences.kt", i = {0, 0}, l = {145}, m = "invokeSuspend", n = {"$this$sequence", "buffer"}, s = {"L$0", "L$1"})
/* compiled from: Sequences.kt */
final class SequencesKt__SequencesKt$shuffled$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super T>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Random $random;
    final /* synthetic */ Sequence<T> $this_shuffled;
    private /* synthetic */ Object L$0;
    Object L$1;
    int label;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    SequencesKt__SequencesKt$shuffled$1(Sequence<? extends T> sequence, Random random, Continuation<? super SequencesKt__SequencesKt$shuffled$1> continuation) {
        super(2, continuation);
        this.$this_shuffled = sequence;
        this.$random = random;
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        SequencesKt__SequencesKt$shuffled$1 sequencesKt__SequencesKt$shuffled$1 = new SequencesKt__SequencesKt$shuffled$1(this.$this_shuffled, this.$random, continuation);
        sequencesKt__SequencesKt$shuffled$1.L$0 = obj;
        return sequencesKt__SequencesKt$shuffled$1;
    }

    public final Object invoke(SequenceScope<? super T> sequenceScope, Continuation<? super Unit> continuation) {
        return ((SequencesKt__SequencesKt$shuffled$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        SequenceScope sequenceScope;
        List<T> list;
        SequencesKt__SequencesKt$shuffled$1 sequencesKt__SequencesKt$shuffled$1;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                sequencesKt__SequencesKt$shuffled$1 = this;
                List<T> mutableList = SequencesKt.toMutableList(sequencesKt__SequencesKt$shuffled$1.$this_shuffled);
                sequenceScope = (SequenceScope) sequencesKt__SequencesKt$shuffled$1.L$0;
                list = mutableList;
                break;
            case 1:
                sequencesKt__SequencesKt$shuffled$1 = this;
                list = (List) sequencesKt__SequencesKt$shuffled$1.L$1;
                sequenceScope = (SequenceScope) sequencesKt__SequencesKt$shuffled$1.L$0;
                ResultKt.throwOnFailure(obj);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        while (!list.isEmpty()) {
            int nextInt = sequencesKt__SequencesKt$shuffled$1.$random.nextInt(list.size());
            T removeLast = CollectionsKt.removeLast(list);
            if (nextInt < list.size()) {
                removeLast = list.set(nextInt, removeLast);
            }
            sequencesKt__SequencesKt$shuffled$1.L$0 = sequenceScope;
            sequencesKt__SequencesKt$shuffled$1.L$1 = list;
            sequencesKt__SequencesKt$shuffled$1.label = 1;
            if (sequenceScope.yield(removeLast, sequencesKt__SequencesKt$shuffled$1) == coroutine_suspended) {
                return coroutine_suspended;
            }
        }
        return Unit.INSTANCE;
    }
}

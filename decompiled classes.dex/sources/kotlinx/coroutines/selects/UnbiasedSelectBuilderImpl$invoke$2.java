package kotlinx.coroutines.selects;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;

@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0006\b\u0001\u0010\u0003 \u0000H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "Q", "R", "invoke"}, k = 3, mv = {1, 6, 0}, xi = 48)
/* compiled from: SelectUnbiased.kt */
final class UnbiasedSelectBuilderImpl$invoke$2 extends Lambda implements Function0<Unit> {
    final /* synthetic */ Function2<Q, Continuation<? super R>, Object> $block;
    final /* synthetic */ SelectClause1<Q> $this_invoke;
    final /* synthetic */ UnbiasedSelectBuilderImpl<R> this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    UnbiasedSelectBuilderImpl$invoke$2(SelectClause1<? extends Q> selectClause1, UnbiasedSelectBuilderImpl<? super R> unbiasedSelectBuilderImpl, Function2<? super Q, ? super Continuation<? super R>, ? extends Object> function2) {
        super(0);
        this.$this_invoke = selectClause1;
        this.this$0 = unbiasedSelectBuilderImpl;
        this.$block = function2;
    }

    public final void invoke() {
        this.$this_invoke.registerSelectClause1(this.this$0.getInstance(), this.$block);
    }
}

package androidx.core.view;

import android.view.View;
import android.view.ViewGroup;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.SequenceScope;

@Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H@"}, d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", "Landroid/view/View;"}, k = 3, mv = {1, 6, 0}, xi = 48)
@DebugMetadata(c = "androidx.core.view.ViewKt$allViews$1", f = "View.kt", i = {0}, l = {406, 408}, m = "invokeSuspend", n = {"$this$sequence"}, s = {"L$0"})
/* compiled from: View.kt */
final class ViewKt$allViews$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super View>, Continuation<? super Unit>, Object> {
    final /* synthetic */ View $this_allViews;
    private /* synthetic */ Object L$0;
    int label;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ViewKt$allViews$1(View view, Continuation<? super ViewKt$allViews$1> continuation) {
        super(2, continuation);
        this.$this_allViews = view;
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        ViewKt$allViews$1 viewKt$allViews$1 = new ViewKt$allViews$1(this.$this_allViews, continuation);
        viewKt$allViews$1.L$0 = obj;
        return viewKt$allViews$1;
    }

    public final Object invoke(SequenceScope<? super View> sequenceScope, Continuation<? super Unit> continuation) {
        return ((ViewKt$allViews$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        ViewKt$allViews$1 viewKt$allViews$1;
        SequenceScope sequenceScope;
        ViewKt$allViews$1 viewKt$allViews$12;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure(obj);
                viewKt$allViews$12 = this;
                sequenceScope = (SequenceScope) viewKt$allViews$12.L$0;
                viewKt$allViews$12.L$0 = sequenceScope;
                viewKt$allViews$12.label = 1;
                if (sequenceScope.yield(viewKt$allViews$12.$this_allViews, viewKt$allViews$12) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                viewKt$allViews$12 = this;
                sequenceScope = (SequenceScope) viewKt$allViews$12.L$0;
                ResultKt.throwOnFailure(obj);
                break;
            case 2:
                viewKt$allViews$1 = this;
                ResultKt.throwOnFailure(obj);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        View view = viewKt$allViews$12.$this_allViews;
        if (view instanceof ViewGroup) {
            viewKt$allViews$12.L$0 = null;
            viewKt$allViews$12.label = 2;
            if (sequenceScope.yieldAll(ViewGroupKt.getDescendants((ViewGroup) view), (Continuation<? super Unit>) viewKt$allViews$12) == coroutine_suspended) {
                return coroutine_suspended;
            }
            viewKt$allViews$1 = viewKt$allViews$12;
            ViewKt$allViews$1 viewKt$allViews$13 = viewKt$allViews$1;
        }
        return Unit.INSTANCE;
    }
}

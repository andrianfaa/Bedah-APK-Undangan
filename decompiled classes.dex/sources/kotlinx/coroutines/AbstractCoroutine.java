package kotlinx.coroutines;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Typography;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b'\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00020\u00022\u00020\u00032\b\u0012\u0004\u0012\u0002H\u00010\u00042\u00020\u0005B\u001d\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t¢\u0006\u0002\u0010\u000bJ\u0012\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0014J\b\u0010\u0019\u001a\u00020\u001aH\u0014J\u0015\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u001c\u001a\u00020\u001dH\u0000¢\u0006\u0002\b\u001eJ\r\u0010\u001f\u001a\u00020\u001aH\u0010¢\u0006\u0002\b J\u0018\u0010!\u001a\u00020\u00162\u0006\u0010\"\u001a\u00020\u001d2\u0006\u0010#\u001a\u00020\tH\u0014J\u0015\u0010$\u001a\u00020\u00162\u0006\u0010%\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010&J\u0012\u0010'\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0004J\u001c\u0010(\u001a\u00020\u00162\f\u0010)\u001a\b\u0012\u0004\u0012\u00028\u00000*ø\u0001\u0000¢\u0006\u0002\u0010&JM\u0010+\u001a\u00020\u0016\"\u0004\b\u0001\u0010,2\u0006\u0010+\u001a\u00020-2\u0006\u0010.\u001a\u0002H,2'\u0010/\u001a#\b\u0001\u0012\u0004\u0012\u0002H,\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u001800¢\u0006\u0002\b1ø\u0001\u0000¢\u0006\u0002\u00102R\u0017\u0010\f\u001a\u00020\u0007¢\u0006\u000e\n\u0000\u0012\u0004\b\r\u0010\u000e\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0011\u001a\u00020\u00078VX\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0010R\u0014\u0010\u0013\u001a\u00020\t8VX\u0004¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014\u0002\u0004\n\u0002\b\u0019¨\u00063"}, d2 = {"Lkotlinx/coroutines/AbstractCoroutine;", "T", "Lkotlinx/coroutines/JobSupport;", "Lkotlinx/coroutines/Job;", "Lkotlin/coroutines/Continuation;", "Lkotlinx/coroutines/CoroutineScope;", "parentContext", "Lkotlin/coroutines/CoroutineContext;", "initParentJob", "", "active", "(Lkotlin/coroutines/CoroutineContext;ZZ)V", "context", "getContext$annotations", "()V", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "coroutineContext", "getCoroutineContext", "isActive", "()Z", "afterResume", "", "state", "", "cancellationExceptionMessage", "", "handleOnCompletionException", "exception", "", "handleOnCompletionException$kotlinx_coroutines_core", "nameString", "nameString$kotlinx_coroutines_core", "onCancelled", "cause", "handled", "onCompleted", "value", "(Ljava/lang/Object;)V", "onCompletionInternal", "resumeWith", "result", "Lkotlin/Result;", "start", "R", "Lkotlinx/coroutines/CoroutineStart;", "receiver", "block", "Lkotlin/Function2;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/CoroutineStart;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: 016C */
public abstract class AbstractCoroutine<T> extends JobSupport implements Job, Continuation<T>, CoroutineScope {
    private final CoroutineContext context;

    public AbstractCoroutine(CoroutineContext parentContext, boolean initParentJob, boolean active) {
        super(active);
        if (initParentJob) {
            initParentJob((Job) parentContext.get(Job.Key));
        }
        this.context = parentContext.plus(this);
    }

    public static /* synthetic */ void getContext$annotations() {
    }

    /* access modifiers changed from: protected */
    public void afterResume(Object state) {
        afterCompletion(state);
    }

    /* access modifiers changed from: protected */
    public String cancellationExceptionMessage() {
        String classSimpleName = DebugStringsKt.getClassSimpleName(this);
        Log1F380D.a((Object) classSimpleName);
        String stringPlus = Intrinsics.stringPlus(classSimpleName, " was cancelled");
        Log1F380D.a((Object) stringPlus);
        return stringPlus;
    }

    public final CoroutineContext getContext() {
        return this.context;
    }

    public CoroutineContext getCoroutineContext() {
        return this.context;
    }

    public final void handleOnCompletionException$kotlinx_coroutines_core(Throwable exception) {
        CoroutineExceptionHandlerKt.handleCoroutineException(this.context, exception);
    }

    public boolean isActive() {
        return super.isActive();
    }

    /* access modifiers changed from: protected */
    public void onCancelled(Throwable cause, boolean handled) {
    }

    /* access modifiers changed from: protected */
    public void onCompleted(T value) {
    }

    /* access modifiers changed from: protected */
    public final void onCompletionInternal(Object state) {
        if (state instanceof CompletedExceptionally) {
            onCancelled(((CompletedExceptionally) state).cause, ((CompletedExceptionally) state).getHandled());
        } else {
            onCompleted(state);
        }
    }

    public final void resumeWith(Object result) {
        Object makeCompletingOnce$kotlinx_coroutines_core = makeCompletingOnce$kotlinx_coroutines_core(CompletionStateKt.toState$default(result, (Function1) null, 1, (Object) null));
        if (makeCompletingOnce$kotlinx_coroutines_core != JobSupportKt.COMPLETING_WAITING_CHILDREN) {
            afterResume(makeCompletingOnce$kotlinx_coroutines_core);
        }
    }

    public final <R> void start(CoroutineStart start, R receiver, Function2<? super R, ? super Continuation<? super T>, ? extends Object> block) {
        start.invoke(block, receiver, this);
    }

    public String nameString$kotlinx_coroutines_core() {
        String coroutineName = CoroutineContextKt.getCoroutineName(this.context);
        Log1F380D.a((Object) coroutineName);
        return coroutineName == null ? super.nameString$kotlinx_coroutines_core() : Typography.quote + coroutineName + "\":" + super.nameString$kotlinx_coroutines_core();
    }
}

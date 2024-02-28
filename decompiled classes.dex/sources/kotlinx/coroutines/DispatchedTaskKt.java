package kotlinx.coroutines;

import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.InlineMarker;
import kotlinx.coroutines.internal.DispatchedContinuation;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.internal.ThreadContextKt;

@Metadata(d1 = {"\u0000<\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a \u0010\f\u001a\u00020\r\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\u000f2\u0006\u0010\u0010\u001a\u00020\u0001H\u0000\u001a.\u0010\u0011\u001a\u00020\r\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\u000f2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u00132\u0006\u0010\u0014\u001a\u00020\tH\u0000\u001a\u0010\u0010\u0015\u001a\u00020\r*\u0006\u0012\u0002\b\u00030\u000fH\u0002\u001a\u0019\u0010\u0016\u001a\u00020\r*\u0006\u0012\u0002\b\u00030\u00132\u0006\u0010\u0017\u001a\u00020\u0018H\b\u001a'\u0010\u0019\u001a\u00020\r*\u0006\u0012\u0002\b\u00030\u000f2\u0006\u0010\u001a\u001a\u00020\u001b2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\r0\u001dH\b\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u0016\u0010\u0002\u001a\u00020\u00018\u0000XT¢\u0006\b\n\u0000\u0012\u0004\b\u0003\u0010\u0004\"\u000e\u0010\u0005\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u0018\u0010\b\u001a\u00020\t*\u00020\u00018@X\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\n\"\u0018\u0010\u000b\u001a\u00020\t*\u00020\u00018@X\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\n¨\u0006\u001e"}, d2 = {"MODE_ATOMIC", "", "MODE_CANCELLABLE", "getMODE_CANCELLABLE$annotations", "()V", "MODE_CANCELLABLE_REUSABLE", "MODE_UNDISPATCHED", "MODE_UNINITIALIZED", "isCancellableMode", "", "(I)Z", "isReusableMode", "dispatch", "", "T", "Lkotlinx/coroutines/DispatchedTask;", "mode", "resume", "delegate", "Lkotlin/coroutines/Continuation;", "undispatched", "resumeUnconfined", "resumeWithStackTrace", "exception", "", "runUnconfinedEventLoop", "eventLoop", "Lkotlinx/coroutines/EventLoop;", "block", "Lkotlin/Function0;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: DispatchedTask.kt */
public final class DispatchedTaskKt {
    public static final int MODE_ATOMIC = 0;
    public static final int MODE_CANCELLABLE = 1;
    public static final int MODE_CANCELLABLE_REUSABLE = 2;
    public static final int MODE_UNDISPATCHED = 4;
    public static final int MODE_UNINITIALIZED = -1;

    public static final <T> void dispatch(DispatchedTask<? super T> $this$dispatch, int mode) {
        boolean z = true;
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(mode != -1)) {
                throw new AssertionError();
            }
        }
        Continuation<? super T> delegate$kotlinx_coroutines_core = $this$dispatch.getDelegate$kotlinx_coroutines_core();
        if (mode != 4) {
            z = false;
        }
        if (z || !(delegate$kotlinx_coroutines_core instanceof DispatchedContinuation) || isCancellableMode(mode) != isCancellableMode($this$dispatch.resumeMode)) {
            resume($this$dispatch, delegate$kotlinx_coroutines_core, z);
            return;
        }
        CoroutineDispatcher coroutineDispatcher = ((DispatchedContinuation) delegate$kotlinx_coroutines_core).dispatcher;
        CoroutineContext context = delegate$kotlinx_coroutines_core.getContext();
        if (coroutineDispatcher.isDispatchNeeded(context)) {
            coroutineDispatcher.dispatch(context, $this$dispatch);
        } else {
            resumeUnconfined($this$dispatch);
        }
    }

    public static /* synthetic */ void getMODE_CANCELLABLE$annotations() {
    }

    public static final boolean isCancellableMode(int $this$isCancellableMode) {
        return $this$isCancellableMode == 1 || $this$isCancellableMode == 2;
    }

    public static final boolean isReusableMode(int $this$isReusableMode) {
        return $this$isReusableMode == 2;
    }

    public static final <T> void resume(DispatchedTask<? super T> $this$resume, Continuation<? super T> delegate, boolean undispatched) {
        UndispatchedCoroutine<?> undispatchedCoroutine;
        Object takeState$kotlinx_coroutines_core = $this$resume.takeState$kotlinx_coroutines_core();
        Throwable exceptionalResult$kotlinx_coroutines_core = $this$resume.getExceptionalResult$kotlinx_coroutines_core(takeState$kotlinx_coroutines_core);
        Result.Companion companion = Result.Companion;
        Object r2 = Result.m36constructorimpl(exceptionalResult$kotlinx_coroutines_core != null ? ResultKt.createFailure(exceptionalResult$kotlinx_coroutines_core) : $this$resume.getSuccessfulResult$kotlinx_coroutines_core(takeState$kotlinx_coroutines_core));
        if (undispatched) {
            DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) delegate;
            Continuation<T> continuation = dispatchedContinuation.continuation;
            Object obj = dispatchedContinuation.countOrElement;
            CoroutineContext context = continuation.getContext();
            Object updateThreadContext = ThreadContextKt.updateThreadContext(context, obj);
            if (updateThreadContext != ThreadContextKt.NO_THREAD_ELEMENTS) {
                undispatchedCoroutine = CoroutineContextKt.updateUndispatchedCompletion(continuation, context, updateThreadContext);
            } else {
                undispatchedCoroutine = null;
                UndispatchedCoroutine undispatchedCoroutine2 = undispatchedCoroutine;
            }
            try {
                dispatchedContinuation.continuation.resumeWith(r2);
                Unit unit = Unit.INSTANCE;
            } finally {
                if (undispatchedCoroutine == null || undispatchedCoroutine.clearThreadContext()) {
                    ThreadContextKt.restoreThreadContext(context, updateThreadContext);
                }
            }
        } else {
            delegate.resumeWith(r2);
        }
    }

    private static final void resumeUnconfined(DispatchedTask<?> $this$resumeUnconfined) {
        EventLoop eventLoop$kotlinx_coroutines_core = ThreadLocalEventLoop.INSTANCE.getEventLoop$kotlinx_coroutines_core();
        if (eventLoop$kotlinx_coroutines_core.isUnconfinedLoopActive()) {
            eventLoop$kotlinx_coroutines_core.dispatchUnconfined($this$resumeUnconfined);
            return;
        }
        DispatchedTask<?> dispatchedTask = $this$resumeUnconfined;
        eventLoop$kotlinx_coroutines_core.incrementUseCount(true);
        try {
            resume($this$resumeUnconfined, $this$resumeUnconfined.getDelegate$kotlinx_coroutines_core(), true);
            do {
            } while (eventLoop$kotlinx_coroutines_core.processUnconfinedEvent());
        } catch (Throwable th) {
            eventLoop$kotlinx_coroutines_core.decrementUseCount(true);
            throw th;
        }
        eventLoop$kotlinx_coroutines_core.decrementUseCount(true);
    }

    public static final void resumeWithStackTrace(Continuation<?> $this$resumeWithStackTrace, Throwable exception) {
        Result.Companion companion = Result.Companion;
        $this$resumeWithStackTrace.resumeWith(Result.m36constructorimpl(ResultKt.createFailure((!DebugKt.getRECOVER_STACK_TRACES() || !($this$resumeWithStackTrace instanceof CoroutineStackFrame)) ? exception : StackTraceRecoveryKt.recoverFromStackFrame(exception, (CoroutineStackFrame) $this$resumeWithStackTrace))));
    }

    public static final void runUnconfinedEventLoop(DispatchedTask<?> $this$runUnconfinedEventLoop, EventLoop eventLoop, Function0<Unit> block) {
        eventLoop.incrementUseCount(true);
        try {
            block.invoke();
            do {
            } while (eventLoop.processUnconfinedEvent());
            InlineMarker.finallyStart(1);
        } catch (Throwable th) {
            InlineMarker.finallyStart(1);
            eventLoop.decrementUseCount(true);
            InlineMarker.finallyEnd(1);
            throw th;
        }
        eventLoop.decrementUseCount(true);
        InlineMarker.finallyEnd(1);
    }
}

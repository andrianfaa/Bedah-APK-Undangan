package kotlinx.coroutines.internal;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CompletedWithCancellation;
import kotlinx.coroutines.CompletionStateKt;
import kotlinx.coroutines.CoroutineContextKt;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.DebugStringsKt;
import kotlinx.coroutines.DispatchedTask;
import kotlinx.coroutines.EventLoop;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.ThreadLocalEventLoop;
import kotlinx.coroutines.UndispatchedCoroutine;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\b\u0000\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\b\u0012\u0004\u0012\u00028\u00000O2\u00060?j\u0002`@2\b\u0012\u0004\u0012\u00028\u00000\u0004B\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\u0004\b\u0006\u0010\u0007J\r\u0010\t\u001a\u00020\b¢\u0006\u0004\b\t\u0010\nJ!\u0010\u0011\u001a\u00020\b2\b\u0010\f\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u000e\u001a\u00020\rH\u0010¢\u0006\u0004\b\u000f\u0010\u0010J\u0015\u0010\u0013\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0012¢\u0006\u0004\b\u0013\u0010\u0014J\u001f\u0010\u001a\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00028\u0000H\u0000¢\u0006\u0004\b\u0018\u0010\u0019J\u0017\u0010\u001d\u001a\n\u0018\u00010\u001bj\u0004\u0018\u0001`\u001cH\u0016¢\u0006\u0004\b\u001d\u0010\u001eJ\r\u0010 \u001a\u00020\u001f¢\u0006\u0004\b \u0010!J\u0015\u0010\"\u001a\u00020\u001f2\u0006\u0010\u000e\u001a\u00020\r¢\u0006\u0004\b\"\u0010#J\r\u0010$\u001a\u00020\b¢\u0006\u0004\b$\u0010\nJH\u0010+\u001a\u00020\b2\f\u0010&\u001a\b\u0012\u0004\u0012\u00028\u00000%2%\b\b\u0010*\u001a\u001f\u0012\u0013\u0012\u00110\r¢\u0006\f\b(\u0012\b\b)\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u00020\b\u0018\u00010'H\bø\u0001\u0000¢\u0006\u0004\b+\u0010,J\u001a\u0010.\u001a\u00020\u001f2\b\u0010-\u001a\u0004\u0018\u00010\u000bH\b¢\u0006\u0004\b.\u0010/J!\u00100\u001a\u00020\b2\f\u0010&\u001a\b\u0012\u0004\u0012\u00028\u00000%H\bø\u0001\u0000¢\u0006\u0004\b0\u00101J \u00102\u001a\u00020\b2\f\u0010&\u001a\b\u0012\u0004\u0012\u00028\u00000%H\u0016ø\u0001\u0000¢\u0006\u0004\b2\u00101J\u0011\u00105\u001a\u0004\u0018\u00010\u000bH\u0010¢\u0006\u0004\b3\u00104J\u000f\u00107\u001a\u000206H\u0016¢\u0006\u0004\b7\u00108J\u001b\u0010:\u001a\u0004\u0018\u00010\r2\n\u0010\u0005\u001a\u0006\u0012\u0002\b\u000309¢\u0006\u0004\b:\u0010;R\u001e\u0010<\u001a\u0004\u0018\u00010\u000b8\u0000@\u0000X\u000e¢\u0006\f\n\u0004\b<\u0010=\u0012\u0004\b>\u0010\nR\u001c\u0010C\u001a\n\u0018\u00010?j\u0004\u0018\u0001`@8VX\u0004¢\u0006\u0006\u001a\u0004\bA\u0010BR\u0014\u0010\u0016\u001a\u00020\u00158\u0016X\u0005¢\u0006\u0006\u001a\u0004\bD\u0010ER\u001a\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u00048\u0006X\u0004¢\u0006\u0006\n\u0004\b\u0005\u0010FR\u0014\u0010G\u001a\u00020\u000b8\u0000X\u0004¢\u0006\u0006\n\u0004\bG\u0010=R\u001a\u0010J\u001a\b\u0012\u0004\u0012\u00028\u00000\u00048PX\u0004¢\u0006\u0006\u001a\u0004\bH\u0010IR\u0014\u0010\u0003\u001a\u00020\u00028\u0006X\u0004¢\u0006\u0006\n\u0004\b\u0003\u0010KR\u001a\u0010M\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u00128BX\u0004¢\u0006\u0006\u001a\u0004\bL\u0010\u0014\u0002\u0004\n\u0002\b\u0019¨\u0006N"}, d2 = {"Lkotlinx/coroutines/internal/DispatchedContinuation;", "T", "Lkotlinx/coroutines/CoroutineDispatcher;", "dispatcher", "Lkotlin/coroutines/Continuation;", "continuation", "<init>", "(Lkotlinx/coroutines/CoroutineDispatcher;Lkotlin/coroutines/Continuation;)V", "", "awaitReusability", "()V", "", "takenState", "", "cause", "cancelCompletedResult$kotlinx_coroutines_core", "(Ljava/lang/Object;Ljava/lang/Throwable;)V", "cancelCompletedResult", "Lkotlinx/coroutines/CancellableContinuationImpl;", "claimReusableCancellableContinuation", "()Lkotlinx/coroutines/CancellableContinuationImpl;", "Lkotlin/coroutines/CoroutineContext;", "context", "value", "dispatchYield$kotlinx_coroutines_core", "(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;)V", "dispatchYield", "Ljava/lang/StackTraceElement;", "Lkotlinx/coroutines/internal/StackTraceElement;", "getStackTraceElement", "()Ljava/lang/StackTraceElement;", "", "isReusable", "()Z", "postponeCancellation", "(Ljava/lang/Throwable;)Z", "release", "Lkotlin/Result;", "result", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "onCancellation", "resumeCancellableWith", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "state", "resumeCancelled", "(Ljava/lang/Object;)Z", "resumeUndispatchedWith", "(Ljava/lang/Object;)V", "resumeWith", "takeState$kotlinx_coroutines_core", "()Ljava/lang/Object;", "takeState", "", "toString", "()Ljava/lang/String;", "Lkotlinx/coroutines/CancellableContinuation;", "tryReleaseClaimedContinuation", "(Lkotlinx/coroutines/CancellableContinuation;)Ljava/lang/Throwable;", "_state", "Ljava/lang/Object;", "get_state$kotlinx_coroutines_core$annotations", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "Lkotlinx/coroutines/internal/CoroutineStackFrame;", "getCallerFrame", "()Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "callerFrame", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "Lkotlin/coroutines/Continuation;", "countOrElement", "getDelegate$kotlinx_coroutines_core", "()Lkotlin/coroutines/Continuation;", "delegate", "Lkotlinx/coroutines/CoroutineDispatcher;", "getReusableCancellableContinuation", "reusableCancellableContinuation", "kotlinx-coroutines-core", "Lkotlinx/coroutines/DispatchedTask;"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: 019C */
public final class DispatchedContinuation<T> extends DispatchedTask<T> implements CoroutineStackFrame, Continuation<T> {
    private static final /* synthetic */ AtomicReferenceFieldUpdater _reusableCancellableContinuation$FU = AtomicReferenceFieldUpdater.newUpdater(DispatchedContinuation.class, Object.class, "_reusableCancellableContinuation");
    private volatile /* synthetic */ Object _reusableCancellableContinuation = null;
    public Object _state = DispatchedContinuationKt.UNDEFINED;
    public final Continuation<T> continuation;
    public final Object countOrElement = ThreadContextKt.threadContextElements(getContext());
    public final CoroutineDispatcher dispatcher;

    public DispatchedContinuation(CoroutineDispatcher dispatcher2, Continuation<? super T> continuation2) {
        super(-1);
        this.dispatcher = dispatcher2;
        this.continuation = continuation2;
    }

    private final CancellableContinuationImpl<?> getReusableCancellableContinuation() {
        Object obj = this._reusableCancellableContinuation;
        if (obj instanceof CancellableContinuationImpl) {
            return (CancellableContinuationImpl) obj;
        }
        return null;
    }

    public static /* synthetic */ void get_state$kotlinx_coroutines_core$annotations() {
    }

    public final void awaitReusability() {
        do {
        } while (this._reusableCancellableContinuation == DispatchedContinuationKt.REUSABLE_CLAIMED);
    }

    public void cancelCompletedResult$kotlinx_coroutines_core(Object takenState, Throwable cause) {
        if (takenState instanceof CompletedWithCancellation) {
            ((CompletedWithCancellation) takenState).onCancellation.invoke(cause);
        }
    }

    public final CancellableContinuationImpl<T> claimReusableCancellableContinuation() {
        while (true) {
            Object obj = this._reusableCancellableContinuation;
            if (obj == null) {
                this._reusableCancellableContinuation = DispatchedContinuationKt.REUSABLE_CLAIMED;
                return null;
            } else if (obj instanceof CancellableContinuationImpl) {
                if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_reusableCancellableContinuation$FU, this, obj, DispatchedContinuationKt.REUSABLE_CLAIMED)) {
                    return (CancellableContinuationImpl) obj;
                }
            } else if (obj != DispatchedContinuationKt.REUSABLE_CLAIMED && !(obj instanceof Throwable)) {
                String stringPlus = Intrinsics.stringPlus("Inconsistent state ", obj);
                Log1F380D.a((Object) stringPlus);
                throw new IllegalStateException(stringPlus.toString());
            }
        }
    }

    public final void dispatchYield$kotlinx_coroutines_core(CoroutineContext context, T value) {
        this._state = value;
        this.resumeMode = 1;
        this.dispatcher.dispatchYield(context, this);
    }

    public CoroutineStackFrame getCallerFrame() {
        Continuation<T> continuation2 = this.continuation;
        if (continuation2 instanceof CoroutineStackFrame) {
            return (CoroutineStackFrame) continuation2;
        }
        return null;
    }

    public CoroutineContext getContext() {
        return this.continuation.getContext();
    }

    public Continuation<T> getDelegate$kotlinx_coroutines_core() {
        return this;
    }

    public StackTraceElement getStackTraceElement() {
        return null;
    }

    public final boolean isReusable() {
        return this._reusableCancellableContinuation != null;
    }

    public final boolean postponeCancellation(Throwable cause) {
        while (true) {
            Object obj = this._reusableCancellableContinuation;
            if (Intrinsics.areEqual(obj, (Object) DispatchedContinuationKt.REUSABLE_CLAIMED)) {
                if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_reusableCancellableContinuation$FU, this, DispatchedContinuationKt.REUSABLE_CLAIMED, cause)) {
                    return true;
                }
            } else if (obj instanceof Throwable) {
                return true;
            } else {
                if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_reusableCancellableContinuation$FU, this, obj, (Object) null)) {
                    return false;
                }
            }
        }
    }

    public final void release() {
        awaitReusability();
        CancellableContinuationImpl<?> reusableCancellableContinuation = getReusableCancellableContinuation();
        if (reusableCancellableContinuation != null) {
            reusableCancellableContinuation.detachChild$kotlinx_coroutines_core();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x012c A[Catch:{ all -> 0x0109, all -> 0x013f }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void resumeCancellableWith(java.lang.Object r24, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> r25) {
        /*
            r23 = this;
            r1 = r23
            r2 = 0
            java.lang.Object r3 = kotlinx.coroutines.CompletionStateKt.toState((java.lang.Object) r24, (kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit>) r25)
            kotlinx.coroutines.CoroutineDispatcher r0 = r1.dispatcher
            kotlin.coroutines.CoroutineContext r4 = r23.getContext()
            boolean r0 = r0.isDispatchNeeded(r4)
            r4 = 1
            if (r0 == 0) goto L_0x002c
            r1._state = r3
            r1.resumeMode = r4
            kotlinx.coroutines.CoroutineDispatcher r0 = r1.dispatcher
            kotlin.coroutines.CoroutineContext r4 = r23.getContext()
            r5 = r1
            java.lang.Runnable r5 = (java.lang.Runnable) r5
            r0.dispatch(r4, r5)
            r20 = r2
            r22 = r3
            r3 = r24
            goto L_0x015a
        L_0x002c:
            r5 = 1
            r6 = r23
            r7 = 0
            r8 = 0
            boolean r0 = kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()
            if (r0 == 0) goto L_0x003a
            r0 = 0
        L_0x003a:
            kotlinx.coroutines.ThreadLocalEventLoop r0 = kotlinx.coroutines.ThreadLocalEventLoop.INSTANCE
            kotlinx.coroutines.EventLoop r9 = r0.getEventLoop$kotlinx_coroutines_core()
            boolean r0 = r9.isUnconfinedLoopActive()
            if (r0 == 0) goto L_0x0059
            r6._state = r3
            r6.resumeMode = r5
            r0 = r6
            kotlinx.coroutines.DispatchedTask r0 = (kotlinx.coroutines.DispatchedTask) r0
            r9.dispatchUnconfined(r0)
            r20 = r2
            r22 = r3
            r3 = r24
            goto L_0x0159
        L_0x0059:
            r10 = r6
            kotlinx.coroutines.DispatchedTask r10 = (kotlinx.coroutines.DispatchedTask) r10
            r11 = 0
            r9.incrementUseCount(r4)
            r12 = 0
            r0 = r23
            r13 = 0
            kotlin.coroutines.CoroutineContext r15 = r0.getContext()     // Catch:{ all -> 0x0141 }
            kotlinx.coroutines.Job$Key r16 = kotlinx.coroutines.Job.Key     // Catch:{ all -> 0x0141 }
            r4 = r16
            kotlin.coroutines.CoroutineContext$Key r4 = (kotlin.coroutines.CoroutineContext.Key) r4     // Catch:{ all -> 0x0141 }
            kotlin.coroutines.CoroutineContext$Element r4 = r15.get(r4)     // Catch:{ all -> 0x0141 }
            kotlinx.coroutines.Job r4 = (kotlinx.coroutines.Job) r4     // Catch:{ all -> 0x0141 }
            if (r4 == 0) goto L_0x00a8
            boolean r15 = r4.isActive()     // Catch:{ all -> 0x009f }
            if (r15 != 0) goto L_0x00a8
            java.util.concurrent.CancellationException r15 = r4.getCancellationException()     // Catch:{ all -> 0x009f }
            r14 = r15
            java.lang.Throwable r14 = (java.lang.Throwable) r14     // Catch:{ all -> 0x009f }
            r0.cancelCompletedResult$kotlinx_coroutines_core(r3, r14)     // Catch:{ all -> 0x009f }
            r14 = r0
            kotlin.coroutines.Continuation r14 = (kotlin.coroutines.Continuation) r14     // Catch:{ all -> 0x009f }
            kotlin.Result$Companion r18 = kotlin.Result.Companion     // Catch:{ all -> 0x009f }
            r18 = r15
            java.lang.Throwable r18 = (java.lang.Throwable) r18     // Catch:{ all -> 0x009f }
            java.lang.Object r18 = kotlin.ResultKt.createFailure(r18)     // Catch:{ all -> 0x009f }
            r19 = r0
            java.lang.Object r0 = kotlin.Result.m36constructorimpl(r18)     // Catch:{ all -> 0x009f }
            r14.resumeWith(r0)     // Catch:{ all -> 0x009f }
            r0 = 1
            goto L_0x00ab
        L_0x009f:
            r0 = move-exception
            r20 = r2
            r22 = r3
            r3 = r24
            goto L_0x0148
        L_0x00a8:
            r19 = r0
            r0 = 0
        L_0x00ab:
            if (r0 != 0) goto L_0x012c
            r4 = r23
            r13 = 0
            kotlin.coroutines.Continuation<T> r0 = r4.continuation     // Catch:{ all -> 0x0141 }
            java.lang.Object r14 = r4.countOrElement     // Catch:{ all -> 0x0141 }
            r15 = r0
            r18 = 0
            kotlin.coroutines.CoroutineContext r0 = r15.getContext()     // Catch:{ all -> 0x0141 }
            r19 = r0
            r1 = r19
            java.lang.Object r0 = kotlinx.coroutines.internal.ThreadContextKt.updateThreadContext(r1, r14)     // Catch:{ all -> 0x0141 }
            r19 = r0
            kotlinx.coroutines.internal.Symbol r0 = kotlinx.coroutines.internal.ThreadContextKt.NO_THREAD_ELEMENTS     // Catch:{ all -> 0x0141 }
            r20 = r2
            r2 = r19
            if (r2 == r0) goto L_0x00d9
            kotlinx.coroutines.UndispatchedCoroutine r0 = kotlinx.coroutines.CoroutineContextKt.updateUndispatchedCompletion(r15, r1, r2)     // Catch:{ all -> 0x00d2 }
            goto L_0x00e0
        L_0x00d2:
            r0 = move-exception
            r22 = r3
            r3 = r24
            goto L_0x0148
        L_0x00d9:
            r16 = 0
            r0 = r16
            kotlinx.coroutines.UndispatchedCoroutine r0 = (kotlinx.coroutines.UndispatchedCoroutine) r0     // Catch:{ all -> 0x0126 }
            r0 = 0
        L_0x00e0:
            r19 = r0
            r0 = 0
            r21 = r0
            kotlin.coroutines.Continuation<T> r0 = r4.continuation     // Catch:{ all -> 0x010b }
            r22 = r3
            r3 = r24
            r0.resumeWith(r3)     // Catch:{ all -> 0x0109 }
            kotlin.Unit r0 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x0109 }
            r17 = 1
            kotlin.jvm.internal.InlineMarker.finallyStart(r17)     // Catch:{ all -> 0x013f }
            if (r19 == 0) goto L_0x00ff
            boolean r0 = r19.clearThreadContext()     // Catch:{ all -> 0x013f }
            if (r0 == 0) goto L_0x0102
        L_0x00ff:
            kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(r1, r2)     // Catch:{ all -> 0x013f }
        L_0x0102:
            r17 = 1
            kotlin.jvm.internal.InlineMarker.finallyEnd(r17)     // Catch:{ all -> 0x013f }
            goto L_0x0132
        L_0x0109:
            r0 = move-exception
            goto L_0x0110
        L_0x010b:
            r0 = move-exception
            r22 = r3
            r3 = r24
        L_0x0110:
            r17 = 1
            kotlin.jvm.internal.InlineMarker.finallyStart(r17)     // Catch:{ all -> 0x013f }
            if (r19 == 0) goto L_0x011d
            boolean r21 = r19.clearThreadContext()     // Catch:{ all -> 0x013f }
            if (r21 == 0) goto L_0x0120
        L_0x011d:
            kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(r1, r2)     // Catch:{ all -> 0x013f }
        L_0x0120:
            r17 = 1
            kotlin.jvm.internal.InlineMarker.finallyEnd(r17)     // Catch:{ all -> 0x013f }
            throw r0     // Catch:{ all -> 0x013f }
        L_0x0126:
            r0 = move-exception
            r22 = r3
            r3 = r24
            goto L_0x0148
        L_0x012c:
            r20 = r2
            r22 = r3
            r3 = r24
        L_0x0132:
        L_0x0133:
            boolean r0 = r9.processUnconfinedEvent()     // Catch:{ all -> 0x013f }
            if (r0 != 0) goto L_0x0133
            r1 = 1
            kotlin.jvm.internal.InlineMarker.finallyStart(r1)
            goto L_0x0150
        L_0x013f:
            r0 = move-exception
            goto L_0x0148
        L_0x0141:
            r0 = move-exception
            r20 = r2
            r22 = r3
            r3 = r24
        L_0x0148:
            r1 = 0
            r10.handleFatalException(r0, r1)     // Catch:{ all -> 0x015b }
            r1 = 1
            kotlin.jvm.internal.InlineMarker.finallyStart(r1)
        L_0x0150:
            r9.decrementUseCount(r1)
            kotlin.jvm.internal.InlineMarker.finallyEnd(r1)
        L_0x0159:
        L_0x015a:
            return
        L_0x015b:
            r0 = move-exception
            r1 = r0
            r2 = 1
            kotlin.jvm.internal.InlineMarker.finallyStart(r2)
            r9.decrementUseCount(r2)
            kotlin.jvm.internal.InlineMarker.finallyEnd(r2)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.DispatchedContinuation.resumeCancellableWith(java.lang.Object, kotlin.jvm.functions.Function1):void");
    }

    public final boolean resumeCancelled(Object state) {
        Job job = (Job) getContext().get(Job.Key);
        if (job == null || job.isActive()) {
            return false;
        }
        CancellationException cancellationException = job.getCancellationException();
        cancelCompletedResult$kotlinx_coroutines_core(state, cancellationException);
        Result.Companion companion = Result.Companion;
        resumeWith(Result.m36constructorimpl(ResultKt.createFailure(cancellationException)));
        return true;
    }

    public final void resumeUndispatchedWith(Object result) {
        UndispatchedCoroutine<?> undispatchedCoroutine;
        Continuation<T> continuation2 = this.continuation;
        Object obj = this.countOrElement;
        CoroutineContext context = continuation2.getContext();
        Object updateThreadContext = ThreadContextKt.updateThreadContext(context, obj);
        if (updateThreadContext != ThreadContextKt.NO_THREAD_ELEMENTS) {
            undispatchedCoroutine = CoroutineContextKt.updateUndispatchedCompletion(continuation2, context, updateThreadContext);
        } else {
            undispatchedCoroutine = null;
            UndispatchedCoroutine undispatchedCoroutine2 = undispatchedCoroutine;
        }
        try {
            this.continuation.resumeWith(result);
            Unit unit = Unit.INSTANCE;
        } finally {
            InlineMarker.finallyStart(1);
            if (undispatchedCoroutine == null || undispatchedCoroutine.clearThreadContext()) {
                ThreadContextKt.restoreThreadContext(context, updateThreadContext);
            }
            InlineMarker.finallyEnd(1);
        }
    }

    public void resumeWith(Object result) {
        CoroutineContext context;
        Object updateThreadContext;
        Object obj = result;
        CoroutineContext context2 = this.continuation.getContext();
        Object state$default = CompletionStateKt.toState$default(obj, (Function1) null, 1, (Object) null);
        if (this.dispatcher.isDispatchNeeded(context2)) {
            this._state = state$default;
            this.resumeMode = 0;
            this.dispatcher.dispatch(context2, this);
            return;
        }
        if (DebugKt.getASSERTIONS_ENABLED()) {
        }
        EventLoop eventLoop$kotlinx_coroutines_core = ThreadLocalEventLoop.INSTANCE.getEventLoop$kotlinx_coroutines_core();
        if (eventLoop$kotlinx_coroutines_core.isUnconfinedLoopActive()) {
            this._state = state$default;
            this.resumeMode = 0;
            eventLoop$kotlinx_coroutines_core.dispatchUnconfined(this);
            return;
        }
        DispatchedTask dispatchedTask = this;
        eventLoop$kotlinx_coroutines_core.incrementUseCount(true);
        try {
            context = getContext();
            updateThreadContext = ThreadContextKt.updateThreadContext(context, this.countOrElement);
            this.continuation.resumeWith(obj);
            Unit unit = Unit.INSTANCE;
            ThreadContextKt.restoreThreadContext(context, updateThreadContext);
            do {
            } while (eventLoop$kotlinx_coroutines_core.processUnconfinedEvent());
        } catch (Throwable th) {
            try {
                dispatchedTask.handleFatalException(th, (Throwable) null);
            } catch (Throwable th2) {
                Throwable th3 = th2;
                eventLoop$kotlinx_coroutines_core.decrementUseCount(true);
                throw th3;
            }
        }
        eventLoop$kotlinx_coroutines_core.decrementUseCount(true);
    }

    public Object takeState$kotlinx_coroutines_core() {
        Object obj = this._state;
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(obj != DispatchedContinuationKt.UNDEFINED)) {
                throw new AssertionError();
            }
        }
        this._state = DispatchedContinuationKt.UNDEFINED;
        return obj;
    }

    public String toString() {
        StringBuilder append = new StringBuilder().append("DispatchedContinuation[").append(this.dispatcher).append(", ");
        String debugString = DebugStringsKt.toDebugString(this.continuation);
        Log1F380D.a((Object) debugString);
        return append.append(debugString).append(']').toString();
    }

    public final Throwable tryReleaseClaimedContinuation(CancellableContinuation<?> continuation2) {
        do {
            Object obj = this._reusableCancellableContinuation;
            if (obj != DispatchedContinuationKt.REUSABLE_CLAIMED) {
                if (!(obj instanceof Throwable)) {
                    String stringPlus = Intrinsics.stringPlus("Inconsistent state ", obj);
                    Log1F380D.a((Object) stringPlus);
                    throw new IllegalStateException(stringPlus.toString());
                } else if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_reusableCancellableContinuation$FU, this, obj, (Object) null)) {
                    return (Throwable) obj;
                } else {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
            }
        } while (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_reusableCancellableContinuation$FU, this, DispatchedContinuationKt.REUSABLE_CLAIMED, continuation2));
        return null;
    }
}

package kotlinx.coroutines;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.internal.DispatchedContinuation;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.internal.Symbol;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000¶\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0001\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\b\u0011\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\t\u0012\u0004\u0012\u00028\u00000\u00012\t\u0012\u0004\u0012\u00028\u00000\u00012\u00060tj\u0002`uB\u001d\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0004¢\u0006\u0004\b\u0006\u0010\u0007J\u0019\u0010\u000b\u001a\u00020\n2\b\u0010\t\u001a\u0004\u0018\u00010\bH\u0002¢\u0006\u0004\b\u000b\u0010\fJ\u001f\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u000e\u001a\u00020\r2\b\u0010\u0010\u001a\u0004\u0018\u00010\u000f¢\u0006\u0004\b\u0012\u0010\u0013JB\u0010\u0012\u001a\u00020\u00112'\u0010\u000e\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u000f¢\u0006\f\b\u0015\u0012\b\b\u0016\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u00110\u0014j\u0002`\u00172\b\u0010\u0010\u001a\u0004\u0018\u00010\u000fH\u0002¢\u0006\u0004\b\u0012\u0010\u0018J\u001e\u0010\u001b\u001a\u00020\u00112\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00110\u0019H\b¢\u0006\u0004\b\u001b\u0010\u001cJ8\u0010\u001e\u001a\u00020\u00112!\u0010\u001d\u001a\u001d\u0012\u0013\u0012\u00110\u000f¢\u0006\f\b\u0015\u0012\b\b\u0016\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u00110\u00142\u0006\u0010\u0010\u001a\u00020\u000f¢\u0006\u0004\b\u001e\u0010\u0018J\u0019\u0010 \u001a\u00020\u001f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u000fH\u0016¢\u0006\u0004\b \u0010!J!\u0010%\u001a\u00020\u00112\b\u0010\"\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0010\u001a\u00020\u000fH\u0010¢\u0006\u0004\b#\u0010$J\u0017\u0010&\u001a\u00020\u001f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0002¢\u0006\u0004\b&\u0010!J\u0017\u0010(\u001a\u00020\u00112\u0006\u0010'\u001a\u00020\bH\u0016¢\u0006\u0004\b(\u0010)J\u000f\u0010,\u001a\u00020\u0011H\u0000¢\u0006\u0004\b*\u0010+J\u000f\u0010-\u001a\u00020\u0011H\u0002¢\u0006\u0004\b-\u0010+J\u0017\u0010/\u001a\u00020\u00112\u0006\u0010.\u001a\u00020\u0004H\u0002¢\u0006\u0004\b/\u00100J\u0017\u00103\u001a\u00020\u000f2\u0006\u00102\u001a\u000201H\u0016¢\u0006\u0004\b3\u00104J\u001b\u00108\u001a\u0004\u0018\u00010\u000f2\b\u00105\u001a\u0004\u0018\u00010\bH\u0010¢\u0006\u0004\b6\u00107J\u0011\u00109\u001a\u0004\u0018\u00010\bH\u0001¢\u0006\u0004\b9\u0010:J\u0017\u0010=\u001a\n\u0018\u00010;j\u0004\u0018\u0001`<H\u0016¢\u0006\u0004\b=\u0010>J\u001f\u0010A\u001a\u00028\u0001\"\u0004\b\u0001\u0010\u00012\b\u00105\u001a\u0004\u0018\u00010\bH\u0010¢\u0006\u0004\b?\u0010@J\u000f\u0010B\u001a\u00020\u0011H\u0016¢\u0006\u0004\bB\u0010+J\u0011\u0010D\u001a\u0004\u0018\u00010CH\u0002¢\u0006\u0004\bD\u0010EJ8\u0010F\u001a\u00020\u00112'\u0010\u000e\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u000f¢\u0006\f\b\u0015\u0012\b\b\u0016\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u00110\u0014j\u0002`\u0017H\u0016¢\u0006\u0004\bF\u0010GJ\u000f\u0010H\u001a\u00020\u001fH\u0002¢\u0006\u0004\bH\u0010IJ8\u0010J\u001a\u00020\r2'\u0010\u000e\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u000f¢\u0006\f\b\u0015\u0012\b\b\u0016\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u00110\u0014j\u0002`\u0017H\u0002¢\u0006\u0004\bJ\u0010KJB\u0010L\u001a\u00020\u00112'\u0010\u000e\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u000f¢\u0006\f\b\u0015\u0012\b\b\u0016\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u00110\u0014j\u0002`\u00172\b\u00105\u001a\u0004\u0018\u00010\bH\u0002¢\u0006\u0004\bL\u0010MJ\u000f\u0010O\u001a\u00020NH\u0014¢\u0006\u0004\bO\u0010PJ\u0017\u0010S\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u000fH\u0000¢\u0006\u0004\bQ\u0010RJ\u000f\u0010T\u001a\u00020\u0011H\u0002¢\u0006\u0004\bT\u0010+J\u000f\u0010U\u001a\u00020\u001fH\u0001¢\u0006\u0004\bU\u0010IJ<\u0010W\u001a\u00020\u00112\u0006\u0010V\u001a\u00028\u00002#\u0010\u001d\u001a\u001f\u0012\u0013\u0012\u00110\u000f¢\u0006\f\b\u0015\u0012\b\b\u0016\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u0011\u0018\u00010\u0014H\u0016¢\u0006\u0004\bW\u0010XJH\u0010Y\u001a\u00020\u00112\b\u0010\t\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0005\u001a\u00020\u00042%\b\u0002\u0010\u001d\u001a\u001f\u0012\u0013\u0012\u00110\u000f¢\u0006\f\b\u0015\u0012\b\b\u0016\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u0011\u0018\u00010\u0014H\u0002¢\u0006\u0004\bY\u0010ZJ \u0010]\u001a\u00020\u00112\f\u0010\\\u001a\b\u0012\u0004\u0012\u00028\u00000[H\u0016ø\u0001\u0000¢\u0006\u0004\b]\u0010)JZ\u0010`\u001a\u0004\u0018\u00010\b2\u0006\u00105\u001a\u00020^2\b\u0010\t\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0005\u001a\u00020\u00042#\u0010\u001d\u001a\u001f\u0012\u0013\u0012\u00110\u000f¢\u0006\f\b\u0015\u0012\b\b\u0016\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u0011\u0018\u00010\u00142\b\u0010_\u001a\u0004\u0018\u00010\bH\u0002¢\u0006\u0004\b`\u0010aJ\u0011\u0010c\u001a\u0004\u0018\u00010\bH\u0010¢\u0006\u0004\bb\u0010:J\u000f\u0010d\u001a\u00020NH\u0016¢\u0006\u0004\bd\u0010PJ\u000f\u0010e\u001a\u00020\u001fH\u0002¢\u0006\u0004\be\u0010IJ#\u0010e\u001a\u0004\u0018\u00010\b2\u0006\u0010V\u001a\u00028\u00002\b\u0010_\u001a\u0004\u0018\u00010\bH\u0016¢\u0006\u0004\be\u0010fJH\u0010e\u001a\u0004\u0018\u00010\b2\u0006\u0010V\u001a\u00028\u00002\b\u0010_\u001a\u0004\u0018\u00010\b2#\u0010\u001d\u001a\u001f\u0012\u0013\u0012\u00110\u000f¢\u0006\f\b\u0015\u0012\b\b\u0016\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u0011\u0018\u00010\u0014H\u0016¢\u0006\u0004\be\u0010gJJ\u0010i\u001a\u0004\u0018\u00010h2\b\u0010\t\u001a\u0004\u0018\u00010\b2\b\u0010_\u001a\u0004\u0018\u00010\b2#\u0010\u001d\u001a\u001f\u0012\u0013\u0012\u00110\u000f¢\u0006\f\b\u0015\u0012\b\b\u0016\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u0011\u0018\u00010\u0014H\u0002¢\u0006\u0004\bi\u0010jJ\u0019\u0010l\u001a\u0004\u0018\u00010\b2\u0006\u0010k\u001a\u00020\u000fH\u0016¢\u0006\u0004\bl\u0010mJ\u000f\u0010n\u001a\u00020\u001fH\u0002¢\u0006\u0004\bn\u0010IJ\u001b\u0010p\u001a\u00020\u0011*\u00020o2\u0006\u0010V\u001a\u00028\u0000H\u0016¢\u0006\u0004\bp\u0010qJ\u001b\u0010r\u001a\u00020\u0011*\u00020o2\u0006\u0010k\u001a\u00020\u000fH\u0016¢\u0006\u0004\br\u0010sR\u001c\u0010x\u001a\n\u0018\u00010tj\u0004\u0018\u0001`u8VX\u0004¢\u0006\u0006\u001a\u0004\bv\u0010wR\u001a\u0010z\u001a\u00020y8\u0016X\u0004¢\u0006\f\n\u0004\bz\u0010{\u001a\u0004\b|\u0010}R!\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u00028\u0000X\u0004¢\u0006\r\n\u0004\b\u0003\u0010~\u001a\u0005\b\u0010\u0001R\u0016\u0010\u0001\u001a\u00020\u001f8VX\u0004¢\u0006\u0007\u001a\u0005\b\u0001\u0010IR\u0016\u0010\u0001\u001a\u00020\u001f8VX\u0004¢\u0006\u0007\u001a\u0005\b\u0001\u0010IR\u0016\u0010\u0001\u001a\u00020\u001f8VX\u0004¢\u0006\u0007\u001a\u0005\b\u0001\u0010IR\u001b\u0010\u0001\u001a\u0004\u0018\u00010C8\u0002@\u0002X\u000e¢\u0006\b\n\u0006\b\u0001\u0010\u0001R\u0017\u00105\u001a\u0004\u0018\u00010\b8@X\u0004¢\u0006\u0007\u001a\u0005\b\u0001\u0010:R\u0016\u0010\u0001\u001a\u00020N8BX\u0004¢\u0006\u0007\u001a\u0005\b\u0001\u0010P\u0002\u0004\n\u0002\b\u0019¨\u0006\u0001"}, d2 = {"Lkotlinx/coroutines/CancellableContinuationImpl;", "T", "Lkotlin/coroutines/Continuation;", "delegate", "", "resumeMode", "<init>", "(Lkotlin/coroutines/Continuation;I)V", "", "proposedUpdate", "", "alreadyResumedError", "(Ljava/lang/Object;)Ljava/lang/Void;", "Lkotlinx/coroutines/CancelHandler;", "handler", "", "cause", "", "callCancelHandler", "(Lkotlinx/coroutines/CancelHandler;Ljava/lang/Throwable;)V", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "Lkotlinx/coroutines/CompletionHandler;", "(Lkotlin/jvm/functions/Function1;Ljava/lang/Throwable;)V", "Lkotlin/Function0;", "block", "callCancelHandlerSafely", "(Lkotlin/jvm/functions/Function0;)V", "onCancellation", "callOnCancellation", "", "cancel", "(Ljava/lang/Throwable;)Z", "takenState", "cancelCompletedResult$kotlinx_coroutines_core", "(Ljava/lang/Object;Ljava/lang/Throwable;)V", "cancelCompletedResult", "cancelLater", "token", "completeResume", "(Ljava/lang/Object;)V", "detachChild$kotlinx_coroutines_core", "()V", "detachChild", "detachChildIfNonResuable", "mode", "dispatchResume", "(I)V", "Lkotlinx/coroutines/Job;", "parent", "getContinuationCancellationCause", "(Lkotlinx/coroutines/Job;)Ljava/lang/Throwable;", "state", "getExceptionalResult$kotlinx_coroutines_core", "(Ljava/lang/Object;)Ljava/lang/Throwable;", "getExceptionalResult", "getResult", "()Ljava/lang/Object;", "Ljava/lang/StackTraceElement;", "Lkotlinx/coroutines/internal/StackTraceElement;", "getStackTraceElement", "()Ljava/lang/StackTraceElement;", "getSuccessfulResult$kotlinx_coroutines_core", "(Ljava/lang/Object;)Ljava/lang/Object;", "getSuccessfulResult", "initCancellability", "Lkotlinx/coroutines/DisposableHandle;", "installParentHandle", "()Lkotlinx/coroutines/DisposableHandle;", "invokeOnCancellation", "(Lkotlin/jvm/functions/Function1;)V", "isReusable", "()Z", "makeCancelHandler", "(Lkotlin/jvm/functions/Function1;)Lkotlinx/coroutines/CancelHandler;", "multipleHandlersError", "(Lkotlin/jvm/functions/Function1;Ljava/lang/Object;)V", "", "nameString", "()Ljava/lang/String;", "parentCancelled$kotlinx_coroutines_core", "(Ljava/lang/Throwable;)V", "parentCancelled", "releaseClaimedReusableContinuation", "resetStateReusable", "value", "resume", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "resumeImpl", "(Ljava/lang/Object;ILkotlin/jvm/functions/Function1;)V", "Lkotlin/Result;", "result", "resumeWith", "Lkotlinx/coroutines/NotCompleted;", "idempotent", "resumedState", "(Lkotlinx/coroutines/NotCompleted;Ljava/lang/Object;ILkotlin/jvm/functions/Function1;Ljava/lang/Object;)Ljava/lang/Object;", "takeState$kotlinx_coroutines_core", "takeState", "toString", "tryResume", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "(Ljava/lang/Object;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "Lkotlinx/coroutines/internal/Symbol;", "tryResumeImpl", "(Ljava/lang/Object;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Lkotlinx/coroutines/internal/Symbol;", "exception", "tryResumeWithException", "(Ljava/lang/Throwable;)Ljava/lang/Object;", "trySuspend", "Lkotlinx/coroutines/CoroutineDispatcher;", "resumeUndispatched", "(Lkotlinx/coroutines/CoroutineDispatcher;Ljava/lang/Object;)V", "resumeUndispatchedWithException", "(Lkotlinx/coroutines/CoroutineDispatcher;Ljava/lang/Throwable;)V", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "Lkotlinx/coroutines/internal/CoroutineStackFrame;", "getCallerFrame", "()Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "callerFrame", "Lkotlin/coroutines/CoroutineContext;", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "Lkotlin/coroutines/Continuation;", "getDelegate$kotlinx_coroutines_core", "()Lkotlin/coroutines/Continuation;", "isActive", "isCancelled", "isCompleted", "parentHandle", "Lkotlinx/coroutines/DisposableHandle;", "getState$kotlinx_coroutines_core", "getStateDebugRepresentation", "stateDebugRepresentation", "kotlinx-coroutines-core", "Lkotlinx/coroutines/DispatchedTask;", "Lkotlinx/coroutines/CancellableContinuation;"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: 0170 */
public class CancellableContinuationImpl<T> extends DispatchedTask<T> implements CancellableContinuation<T>, CoroutineStackFrame {
    private static final /* synthetic */ AtomicIntegerFieldUpdater _decision$FU;
    private static final /* synthetic */ AtomicReferenceFieldUpdater _state$FU;
    private volatile /* synthetic */ int _decision;
    private volatile /* synthetic */ Object _state;
    private final CoroutineContext context;
    private final Continuation<T> delegate;
    private DisposableHandle parentHandle;

    static {
        Class<CancellableContinuationImpl> cls = CancellableContinuationImpl.class;
        _decision$FU = AtomicIntegerFieldUpdater.newUpdater(cls, "_decision");
        _state$FU = AtomicReferenceFieldUpdater.newUpdater(cls, Object.class, "_state");
    }

    public CancellableContinuationImpl(Continuation<? super T> delegate2, int resumeMode) {
        super(resumeMode);
        this.delegate = delegate2;
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(resumeMode != -1)) {
                throw new AssertionError();
            }
        }
        this.context = delegate2.getContext();
        this._decision = 0;
        this._state = Active.INSTANCE;
    }

    private final Void alreadyResumedError(Object proposedUpdate) {
        String stringPlus = Intrinsics.stringPlus("Already resumed, but proposed with update ", proposedUpdate);
        Log1F380D.a((Object) stringPlus);
        throw new IllegalStateException(stringPlus.toString());
    }

    private final boolean cancelLater(Throwable cause) {
        if (!isReusable()) {
            return false;
        }
        return ((DispatchedContinuation) this.delegate).postponeCancellation(cause);
    }

    private final void detachChildIfNonResuable() {
        if (!isReusable()) {
            detachChild$kotlinx_coroutines_core();
        }
    }

    private final void dispatchResume(int mode) {
        if (!tryResume()) {
            DispatchedTaskKt.dispatch(this, mode);
        }
    }

    private final String getStateDebugRepresentation() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        return state$kotlinx_coroutines_core instanceof NotCompleted ? "Active" : state$kotlinx_coroutines_core instanceof CancelledContinuation ? "Cancelled" : "Completed";
    }

    private final DisposableHandle installParentHandle() {
        Job job = (Job) getContext().get(Job.Key);
        if (job == null) {
            return null;
        }
        DisposableHandle invokeOnCompletion$default = Job.DefaultImpls.invokeOnCompletion$default(job, true, false, new ChildContinuation(this), 2, (Object) null);
        this.parentHandle = invokeOnCompletion$default;
        return invokeOnCompletion$default;
    }

    private final boolean isReusable() {
        return DispatchedTaskKt.isReusableMode(this.resumeMode) && ((DispatchedContinuation) this.delegate).isReusable();
    }

    private final CancelHandler makeCancelHandler(Function1<? super Throwable, Unit> handler) {
        return handler instanceof CancelHandler ? (CancelHandler) handler : new InvokeOnCancel(handler);
    }

    private final void multipleHandlersError(Function1<? super Throwable, Unit> handler, Object state) {
        throw new IllegalStateException(("It's prohibited to register multiple handlers, tried to register " + handler + ", already has " + state).toString());
    }

    private final void releaseClaimedReusableContinuation() {
        Continuation<T> continuation = this.delegate;
        Throwable th = null;
        DispatchedContinuation dispatchedContinuation = continuation instanceof DispatchedContinuation ? (DispatchedContinuation) continuation : null;
        if (dispatchedContinuation != null) {
            th = dispatchedContinuation.tryReleaseClaimedContinuation(this);
        }
        if (th != null) {
            detachChild$kotlinx_coroutines_core();
            cancel(th);
        }
    }

    private final void resumeImpl(Object proposedUpdate, int resumeMode, Function1<? super Throwable, Unit> onCancellation) {
        Object obj;
        do {
            obj = this._state;
            if (obj instanceof NotCompleted) {
            } else if (!(obj instanceof CancelledContinuation) || !((CancelledContinuation) obj).makeResumed()) {
                alreadyResumedError(proposedUpdate);
                throw new KotlinNothingValueException();
            } else if (onCancellation != null) {
                callOnCancellation(onCancellation, ((CancelledContinuation) obj).cause);
                return;
            } else {
                return;
            }
        } while (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, obj, resumedState((NotCompleted) obj, proposedUpdate, resumeMode, onCancellation, (Object) null)));
        detachChildIfNonResuable();
        dispatchResume(resumeMode);
    }

    static /* synthetic */ void resumeImpl$default(CancellableContinuationImpl cancellableContinuationImpl, Object obj, int i, Function1 function1, int i2, Object obj2) {
        if (obj2 == null) {
            if ((i2 & 4) != 0) {
                function1 = null;
            }
            cancellableContinuationImpl.resumeImpl(obj, i, function1);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: resumeImpl");
    }

    private final Object resumedState(NotCompleted state, Object proposedUpdate, int resumeMode, Function1<? super Throwable, Unit> onCancellation, Object idempotent) {
        if (proposedUpdate instanceof CompletedExceptionally) {
            boolean z = true;
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (!(idempotent == null)) {
                    throw new AssertionError();
                }
            }
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (onCancellation != null) {
                    z = false;
                }
                if (!z) {
                    throw new AssertionError();
                }
            }
        } else if ((DispatchedTaskKt.isCancellableMode(resumeMode) || idempotent != null) && (onCancellation != null || (((state instanceof CancelHandler) && !(state instanceof BeforeResumeCancelHandler)) || idempotent != null))) {
            return new CompletedContinuation(proposedUpdate, state instanceof CancelHandler ? (CancelHandler) state : null, onCancellation, idempotent, (Throwable) null, 16, (DefaultConstructorMarker) null);
        }
        return proposedUpdate;
    }

    private final boolean tryResume() {
        do {
            switch (this._decision) {
                case 0:
                    break;
                case 1:
                    return false;
                default:
                    throw new IllegalStateException("Already resumed".toString());
            }
        } while (!_decision$FU.compareAndSet(this, 0, 2));
        return true;
    }

    private final Symbol tryResumeImpl(Object proposedUpdate, Object idempotent, Function1<? super Throwable, Unit> onCancellation) {
        Object obj;
        do {
            obj = this._state;
            if (obj instanceof NotCompleted) {
            } else if (!(obj instanceof CompletedContinuation)) {
                return null;
            } else {
                if (idempotent == null || ((CompletedContinuation) obj).idempotentResume != idempotent) {
                    Symbol symbol = null;
                    return null;
                } else if (!DebugKt.getASSERTIONS_ENABLED() || Intrinsics.areEqual(((CompletedContinuation) obj).result, proposedUpdate)) {
                    return CancellableContinuationImplKt.RESUME_TOKEN;
                } else {
                    throw new AssertionError();
                }
            }
        } while (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, obj, resumedState((NotCompleted) obj, proposedUpdate, this.resumeMode, onCancellation, idempotent)));
        detachChildIfNonResuable();
        return CancellableContinuationImplKt.RESUME_TOKEN;
    }

    private final boolean trySuspend() {
        do {
            switch (this._decision) {
                case 0:
                    break;
                case 2:
                    return false;
                default:
                    throw new IllegalStateException("Already suspended".toString());
            }
        } while (!_decision$FU.compareAndSet(this, 0, 1));
        return true;
    }

    public boolean cancel(Throwable cause) {
        Object obj;
        do {
            obj = this._state;
            if (!(obj instanceof NotCompleted)) {
                return false;
            }
        } while (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, obj, new CancelledContinuation(this, cause, obj instanceof CancelHandler)));
        CancelHandler cancelHandler = obj instanceof CancelHandler ? (CancelHandler) obj : null;
        if (cancelHandler != null) {
            callCancelHandler(cancelHandler, cause);
        }
        detachChildIfNonResuable();
        dispatchResume(this.resumeMode);
        return true;
    }

    public void cancelCompletedResult$kotlinx_coroutines_core(Object takenState, Throwable cause) {
        while (true) {
            Object obj = this._state;
            if (obj instanceof NotCompleted) {
                Throwable th = cause;
                throw new IllegalStateException("Not completed".toString());
            } else if (!(obj instanceof CompletedExceptionally)) {
                if (!(obj instanceof CompletedContinuation)) {
                    Throwable th2 = cause;
                    if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, obj, new CompletedContinuation(obj, (CancelHandler) null, (Function1) null, (Object) null, cause, 14, (DefaultConstructorMarker) null))) {
                        return;
                    }
                } else if (!((CompletedContinuation) obj).getCancelled()) {
                    if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, obj, CompletedContinuation.copy$default((CompletedContinuation) obj, (Object) null, (CancelHandler) null, (Function1) null, (Object) null, cause, 15, (Object) null))) {
                        ((CompletedContinuation) obj).invokeHandlers(this, cause);
                        return;
                    }
                    Throwable th3 = cause;
                } else {
                    Throwable th4 = cause;
                    throw new IllegalStateException("Must be called at most once".toString());
                }
            } else {
                return;
            }
        }
    }

    public void completeResume(Object token) {
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(token == CancellableContinuationImplKt.RESUME_TOKEN)) {
                throw new AssertionError();
            }
        }
        dispatchResume(this.resumeMode);
    }

    public final void detachChild$kotlinx_coroutines_core() {
        DisposableHandle disposableHandle = this.parentHandle;
        if (disposableHandle != null) {
            disposableHandle.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
    }

    public CoroutineStackFrame getCallerFrame() {
        Continuation<T> continuation = this.delegate;
        if (continuation instanceof CoroutineStackFrame) {
            return (CoroutineStackFrame) continuation;
        }
        return null;
    }

    public CoroutineContext getContext() {
        return this.context;
    }

    public Throwable getContinuationCancellationCause(Job parent) {
        return parent.getCancellationException();
    }

    public final Continuation<T> getDelegate$kotlinx_coroutines_core() {
        return this.delegate;
    }

    public Throwable getExceptionalResult$kotlinx_coroutines_core(Object state) {
        Throwable exceptionalResult$kotlinx_coroutines_core = super.getExceptionalResult$kotlinx_coroutines_core(state);
        if (exceptionalResult$kotlinx_coroutines_core == null) {
            return null;
        }
        Continuation delegate$kotlinx_coroutines_core = getDelegate$kotlinx_coroutines_core();
        return (!DebugKt.getRECOVER_STACK_TRACES() || !(delegate$kotlinx_coroutines_core instanceof CoroutineStackFrame)) ? exceptionalResult$kotlinx_coroutines_core : StackTraceRecoveryKt.recoverFromStackFrame(exceptionalResult$kotlinx_coroutines_core, (CoroutineStackFrame) delegate$kotlinx_coroutines_core);
    }

    public final Object getResult() {
        Job job;
        boolean isReusable = isReusable();
        if (trySuspend()) {
            if (this.parentHandle == null) {
                installParentHandle();
            }
            if (isReusable) {
                releaseClaimedReusableContinuation();
            }
            return IntrinsicsKt.getCOROUTINE_SUSPENDED();
        }
        if (isReusable) {
            releaseClaimedReusableContinuation();
        }
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            Throwable th = ((CompletedExceptionally) state$kotlinx_coroutines_core).cause;
            if (DebugKt.getRECOVER_STACK_TRACES() && (this instanceof CoroutineStackFrame)) {
                th = StackTraceRecoveryKt.recoverFromStackFrame(th, this);
            }
            throw th;
        } else if (!DispatchedTaskKt.isCancellableMode(this.resumeMode) || (job = (Job) getContext().get(Job.Key)) == null || job.isActive()) {
            return getSuccessfulResult$kotlinx_coroutines_core(state$kotlinx_coroutines_core);
        } else {
            CancellationException cancellationException = job.getCancellationException();
            cancelCompletedResult$kotlinx_coroutines_core(state$kotlinx_coroutines_core, cancellationException);
            throw ((!DebugKt.getRECOVER_STACK_TRACES() || !(this instanceof CoroutineStackFrame)) ? cancellationException : StackTraceRecoveryKt.recoverFromStackFrame(cancellationException, this));
        }
    }

    public StackTraceElement getStackTraceElement() {
        return null;
    }

    public final Object getState$kotlinx_coroutines_core() {
        return this._state;
    }

    public <T> T getSuccessfulResult$kotlinx_coroutines_core(Object state) {
        return state instanceof CompletedContinuation ? ((CompletedContinuation) state).result : state;
    }

    public void initCancellability() {
        DisposableHandle installParentHandle = installParentHandle();
        if (installParentHandle != null && isCompleted()) {
            installParentHandle.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
    }

    public void invokeOnCancellation(Function1<? super Throwable, Unit> handler) {
        CancelHandler makeCancelHandler = makeCancelHandler(handler);
        while (true) {
            Object obj = this._state;
            if (obj instanceof Active) {
                if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, obj, makeCancelHandler)) {
                    return;
                }
            } else if (obj instanceof CancelHandler) {
                multipleHandlersError(handler, obj);
            } else if (obj instanceof CompletedExceptionally) {
                if (!((CompletedExceptionally) obj).makeHandled()) {
                    multipleHandlersError(handler, obj);
                }
                if (obj instanceof CancelledContinuation) {
                    Throwable th = null;
                    CompletedExceptionally completedExceptionally = obj instanceof CompletedExceptionally ? (CompletedExceptionally) obj : null;
                    if (completedExceptionally != null) {
                        th = completedExceptionally.cause;
                    }
                    callCancelHandler(handler, th);
                    return;
                }
                return;
            } else if (obj instanceof CompletedContinuation) {
                if (((CompletedContinuation) obj).cancelHandler != null) {
                    multipleHandlersError(handler, obj);
                }
                if (!(makeCancelHandler instanceof BeforeResumeCancelHandler)) {
                    if (((CompletedContinuation) obj).getCancelled()) {
                        callCancelHandler(handler, ((CompletedContinuation) obj).cancelCause);
                        return;
                    } else {
                        if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, obj, CompletedContinuation.copy$default((CompletedContinuation) obj, (Object) null, makeCancelHandler, (Function1) null, (Object) null, (Throwable) null, 29, (Object) null))) {
                            return;
                        }
                    }
                } else {
                    return;
                }
            } else if (!(makeCancelHandler instanceof BeforeResumeCancelHandler)) {
                if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, obj, new CompletedContinuation(obj, makeCancelHandler, (Function1) null, (Object) null, (Throwable) null, 28, (DefaultConstructorMarker) null))) {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public boolean isActive() {
        return getState$kotlinx_coroutines_core() instanceof NotCompleted;
    }

    public boolean isCancelled() {
        return getState$kotlinx_coroutines_core() instanceof CancelledContinuation;
    }

    public boolean isCompleted() {
        return !(getState$kotlinx_coroutines_core() instanceof NotCompleted);
    }

    /* access modifiers changed from: protected */
    public String nameString() {
        return "CancellableContinuation";
    }

    public final void parentCancelled$kotlinx_coroutines_core(Throwable cause) {
        if (!cancelLater(cause)) {
            cancel(cause);
            detachChildIfNonResuable();
        }
    }

    public final boolean resetStateReusable() {
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(this.resumeMode == 2)) {
                throw new AssertionError();
            }
        }
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(this.parentHandle != NonDisposableHandle.INSTANCE)) {
                throw new AssertionError();
            }
        }
        Object obj = this._state;
        if (DebugKt.getASSERTIONS_ENABLED() && !(!(obj instanceof NotCompleted))) {
            throw new AssertionError();
        } else if (!(obj instanceof CompletedContinuation) || ((CompletedContinuation) obj).idempotentResume == null) {
            this._decision = 0;
            this._state = Active.INSTANCE;
            return true;
        } else {
            detachChild$kotlinx_coroutines_core();
            return false;
        }
    }

    public void resume(T value, Function1<? super Throwable, Unit> onCancellation) {
        resumeImpl(value, this.resumeMode, onCancellation);
    }

    public void resumeUndispatched(CoroutineDispatcher $this$resumeUndispatched, T value) {
        Continuation<T> continuation = this.delegate;
        CoroutineDispatcher coroutineDispatcher = null;
        DispatchedContinuation dispatchedContinuation = continuation instanceof DispatchedContinuation ? (DispatchedContinuation) continuation : null;
        if (dispatchedContinuation != null) {
            coroutineDispatcher = dispatchedContinuation.dispatcher;
        }
        resumeImpl$default(this, value, coroutineDispatcher == $this$resumeUndispatched ? 4 : this.resumeMode, (Function1) null, 4, (Object) null);
    }

    public void resumeUndispatchedWithException(CoroutineDispatcher $this$resumeUndispatchedWithException, Throwable exception) {
        Continuation<T> continuation = this.delegate;
        CoroutineDispatcher coroutineDispatcher = null;
        DispatchedContinuation dispatchedContinuation = continuation instanceof DispatchedContinuation ? (DispatchedContinuation) continuation : null;
        CompletedExceptionally completedExceptionally = new CompletedExceptionally(exception, false, 2, (DefaultConstructorMarker) null);
        if (dispatchedContinuation != null) {
            coroutineDispatcher = dispatchedContinuation.dispatcher;
        }
        resumeImpl$default(this, completedExceptionally, coroutineDispatcher == $this$resumeUndispatchedWithException ? 4 : this.resumeMode, (Function1) null, 4, (Object) null);
    }

    public void resumeWith(Object result) {
        resumeImpl$default(this, CompletionStateKt.toState(result, (CancellableContinuation<?>) this), this.resumeMode, (Function1) null, 4, (Object) null);
    }

    public Object takeState$kotlinx_coroutines_core() {
        return getState$kotlinx_coroutines_core();
    }

    public Object tryResume(T value, Object idempotent) {
        return tryResumeImpl(value, idempotent, (Function1<? super Throwable, Unit>) null);
    }

    public Object tryResume(T value, Object idempotent, Function1<? super Throwable, Unit> onCancellation) {
        return tryResumeImpl(value, idempotent, onCancellation);
    }

    public Object tryResumeWithException(Throwable exception) {
        return tryResumeImpl(new CompletedExceptionally(exception, false, 2, (DefaultConstructorMarker) null), (Object) null, (Function1<? super Throwable, Unit>) null);
    }

    private final void callCancelHandler(Function1<? super Throwable, Unit> handler, Throwable cause) {
        try {
            handler.invoke(cause);
        } catch (Throwable th) {
            CoroutineContext context2 = getContext();
            String stringPlus = Intrinsics.stringPlus("Exception in invokeOnCancellation handler for ", this);
            Log1F380D.a((Object) stringPlus);
            CoroutineExceptionHandlerKt.handleCoroutineException(context2, new CompletionHandlerException(stringPlus, th));
        }
    }

    private final void callCancelHandlerSafely(Function0<Unit> block) {
        try {
            block.invoke();
        } catch (Throwable th) {
            CoroutineContext context2 = getContext();
            String stringPlus = Intrinsics.stringPlus("Exception in invokeOnCancellation handler for ", this);
            Log1F380D.a((Object) stringPlus);
            CoroutineExceptionHandlerKt.handleCoroutineException(context2, new CompletionHandlerException(stringPlus, th));
        }
    }

    public final void callCancelHandler(CancelHandler handler, Throwable cause) {
        try {
            handler.invoke(cause);
        } catch (Throwable th) {
            CoroutineContext context2 = getContext();
            String stringPlus = Intrinsics.stringPlus("Exception in invokeOnCancellation handler for ", this);
            Log1F380D.a((Object) stringPlus);
            CoroutineExceptionHandlerKt.handleCoroutineException(context2, new CompletionHandlerException(stringPlus, th));
        }
    }

    public final void callOnCancellation(Function1<? super Throwable, Unit> onCancellation, Throwable cause) {
        try {
            onCancellation.invoke(cause);
        } catch (Throwable th) {
            CoroutineContext context2 = getContext();
            String stringPlus = Intrinsics.stringPlus("Exception in resume onCancellation handler for ", this);
            Log1F380D.a((Object) stringPlus);
            CoroutineExceptionHandlerKt.handleCoroutineException(context2, new CompletionHandlerException(stringPlus, th));
        }
    }

    public String toString() {
        StringBuilder append = new StringBuilder().append(nameString()).append('(');
        String debugString = DebugStringsKt.toDebugString(this.delegate);
        Log1F380D.a((Object) debugString);
        StringBuilder append2 = append.append(debugString).append("){").append(getStateDebugRepresentation()).append("}@");
        String hexAddress = DebugStringsKt.getHexAddress(this);
        Log1F380D.a((Object) hexAddress);
        return append2.append(hexAddress).toString();
    }
}

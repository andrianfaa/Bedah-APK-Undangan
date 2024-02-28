package kotlinx.coroutines.internal;

import java.util.ArrayDeque;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.CopyableThrowable;
import kotlinx.coroutines.DebugKt;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000f\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u0001\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\u001a\u0014\u0010\u0006\u001a\u00060\u0007j\u0002`\b2\u0006\u0010\t\u001a\u00020\u0001H\u0007\u001a9\u0010\n\u001a\u0002H\u000b\"\b\b\u0000\u0010\u000b*\u00020\f2\u0006\u0010\r\u001a\u0002H\u000b2\u0006\u0010\u000e\u001a\u0002H\u000b2\u0010\u0010\u000f\u001a\f\u0012\b\u0012\u00060\u0007j\u0002`\b0\u0010H\u0002¢\u0006\u0002\u0010\u0011\u001a\u001e\u0010\u0012\u001a\f\u0012\b\u0012\u00060\u0007j\u0002`\b0\u00102\n\u0010\u0013\u001a\u00060\u0014j\u0002`\u0015H\u0002\u001a1\u0010\u0016\u001a\u00020\u00172\u0010\u0010\u0018\u001a\f\u0012\b\u0012\u00060\u0007j\u0002`\b0\u00192\u0010\u0010\u000e\u001a\f\u0012\b\u0012\u00060\u0007j\u0002`\b0\u0010H\u0002¢\u0006\u0002\u0010\u001a\u001a\u0019\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\fHHø\u0001\u0000¢\u0006\u0002\u0010\u001e\u001a+\u0010\u001f\u001a\u0002H\u000b\"\b\b\u0000\u0010\u000b*\u00020\f2\u0006\u0010\u001d\u001a\u0002H\u000b2\n\u0010\u0013\u001a\u00060\u0014j\u0002`\u0015H\u0002¢\u0006\u0002\u0010 \u001a\u001f\u0010!\u001a\u0002H\u000b\"\b\b\u0000\u0010\u000b*\u00020\f2\u0006\u0010\u001d\u001a\u0002H\u000bH\u0000¢\u0006\u0002\u0010\"\u001a,\u0010!\u001a\u0002H\u000b\"\b\b\u0000\u0010\u000b*\u00020\f2\u0006\u0010\u001d\u001a\u0002H\u000b2\n\u0010\u0013\u001a\u0006\u0012\u0002\b\u00030#H\b¢\u0006\u0002\u0010$\u001a!\u0010%\u001a\u0004\u0018\u0001H\u000b\"\b\b\u0000\u0010\u000b*\u00020\f2\u0006\u0010\u001d\u001a\u0002H\u000bH\u0002¢\u0006\u0002\u0010\"\u001a \u0010&\u001a\u0002H\u000b\"\b\b\u0000\u0010\u000b*\u00020\f2\u0006\u0010\u001d\u001a\u0002H\u000bH\b¢\u0006\u0002\u0010\"\u001a\u001f\u0010'\u001a\u0002H\u000b\"\b\b\u0000\u0010\u000b*\u00020\f2\u0006\u0010\u001d\u001a\u0002H\u000bH\u0000¢\u0006\u0002\u0010\"\u001a1\u0010(\u001a\u0018\u0012\u0004\u0012\u0002H\u000b\u0012\u000e\u0012\f\u0012\b\u0012\u00060\u0007j\u0002`\b0\u00190)\"\b\b\u0000\u0010\u000b*\u00020\f*\u0002H\u000bH\u0002¢\u0006\u0002\u0010*\u001a\u001c\u0010+\u001a\u00020,*\u00060\u0007j\u0002`\b2\n\u0010-\u001a\u00060\u0007j\u0002`\bH\u0002\u001a#\u0010.\u001a\u00020/*\f\u0012\b\u0012\u00060\u0007j\u0002`\b0\u00192\u0006\u00100\u001a\u00020\u0001H\u0002¢\u0006\u0002\u00101\u001a\u0014\u00102\u001a\u00020\u0017*\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0000\u001a\u0010\u00103\u001a\u00020,*\u00060\u0007j\u0002`\bH\u0000\u001a\u001b\u00104\u001a\u0002H\u000b\"\b\b\u0000\u0010\u000b*\u00020\f*\u0002H\u000bH\u0002¢\u0006\u0002\u0010\"\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u0016\u0010\u0002\u001a\n \u0003*\u0004\u0018\u00010\u00010\u0001X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u0016\u0010\u0005\u001a\n \u0003*\u0004\u0018\u00010\u00010\u0001X\u0004¢\u0006\u0002\n\u0000*\f\b\u0000\u00105\"\u00020\u00142\u00020\u0014*\f\b\u0000\u00106\"\u00020\u00072\u00020\u0007\u0002\u0004\n\u0002\b\u0019¨\u00067"}, d2 = {"baseContinuationImplClass", "", "baseContinuationImplClassName", "kotlin.jvm.PlatformType", "stackTraceRecoveryClass", "stackTraceRecoveryClassName", "artificialFrame", "Ljava/lang/StackTraceElement;", "Lkotlinx/coroutines/internal/StackTraceElement;", "message", "createFinalException", "E", "", "cause", "result", "resultStackTrace", "Ljava/util/ArrayDeque;", "(Ljava/lang/Throwable;Ljava/lang/Throwable;Ljava/util/ArrayDeque;)Ljava/lang/Throwable;", "createStackTrace", "continuation", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "Lkotlinx/coroutines/internal/CoroutineStackFrame;", "mergeRecoveredTraces", "", "recoveredStacktrace", "", "([Ljava/lang/StackTraceElement;Ljava/util/ArrayDeque;)V", "recoverAndThrow", "", "exception", "(Ljava/lang/Throwable;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "recoverFromStackFrame", "(Ljava/lang/Throwable;Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;)Ljava/lang/Throwable;", "recoverStackTrace", "(Ljava/lang/Throwable;)Ljava/lang/Throwable;", "Lkotlin/coroutines/Continuation;", "(Ljava/lang/Throwable;Lkotlin/coroutines/Continuation;)Ljava/lang/Throwable;", "tryCopyAndVerify", "unwrap", "unwrapImpl", "causeAndStacktrace", "Lkotlin/Pair;", "(Ljava/lang/Throwable;)Lkotlin/Pair;", "elementWiseEquals", "", "e", "frameIndex", "", "methodName", "([Ljava/lang/StackTraceElement;Ljava/lang/String;)I", "initCause", "isArtificial", "sanitizeStackTrace", "CoroutineStackFrame", "StackTraceElement", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: 01A5 */
public final class StackTraceRecoveryKt {
    private static final String baseContinuationImplClass = "kotlin.coroutines.jvm.internal.BaseContinuationImpl";
    private static final String baseContinuationImplClassName;
    private static final String stackTraceRecoveryClass = "kotlinx.coroutines.internal.StackTraceRecoveryKt";
    private static final String stackTraceRecoveryClassName;

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v10, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v9, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v12, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v12, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v13, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v13, resolved type: java.lang.String} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v14, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    static {
        /*
            java.lang.String r0 = "kotlinx.coroutines.internal.StackTraceRecoveryKt"
            java.lang.String r1 = "kotlin.coroutines.jvm.internal.BaseContinuationImpl"
            kotlin.Result$Companion r2 = kotlin.Result.Companion     // Catch:{ all -> 0x0014 }
            r2 = 0
            java.lang.Class r3 = java.lang.Class.forName(r1)     // Catch:{ all -> 0x0014 }
            java.lang.String r3 = r3.getCanonicalName()     // Catch:{ all -> 0x0014 }
            java.lang.Object r2 = kotlin.Result.m36constructorimpl(r3)     // Catch:{ all -> 0x0014 }
            goto L_0x001f
        L_0x0014:
            r2 = move-exception
            kotlin.Result$Companion r3 = kotlin.Result.Companion
            java.lang.Object r2 = kotlin.ResultKt.createFailure(r2)
            java.lang.Object r2 = kotlin.Result.m36constructorimpl(r2)
        L_0x001f:
            java.lang.Throwable r3 = kotlin.Result.m39exceptionOrNullimpl(r2)
            if (r3 != 0) goto L_0x0027
            r1 = r2
            goto L_0x002a
        L_0x0027:
            r2 = r3
            r3 = 0
        L_0x002a:
            java.lang.String r1 = (java.lang.String) r1
            baseContinuationImplClassName = r1
            kotlin.Result$Companion r1 = kotlin.Result.Companion     // Catch:{ all -> 0x003f }
            r1 = 0
            java.lang.Class r2 = java.lang.Class.forName(r0)     // Catch:{ all -> 0x003f }
            java.lang.String r2 = r2.getCanonicalName()     // Catch:{ all -> 0x003f }
            java.lang.Object r1 = kotlin.Result.m36constructorimpl(r2)     // Catch:{ all -> 0x003f }
            goto L_0x004a
        L_0x003f:
            r1 = move-exception
            kotlin.Result$Companion r2 = kotlin.Result.Companion
            java.lang.Object r1 = kotlin.ResultKt.createFailure(r1)
            java.lang.Object r1 = kotlin.Result.m36constructorimpl(r1)
        L_0x004a:
            java.lang.Throwable r2 = kotlin.Result.m39exceptionOrNullimpl(r1)
            if (r2 != 0) goto L_0x0052
            r0 = r1
            goto L_0x0055
        L_0x0052:
            r1 = r2
            r2 = 0
        L_0x0055:
            java.lang.String r0 = (java.lang.String) r0
            stackTraceRecoveryClassName = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.StackTraceRecoveryKt.<clinit>():void");
    }

    public static /* synthetic */ void CoroutineStackFrame$annotations() {
    }

    public static /* synthetic */ void StackTraceElement$annotations() {
    }

    public static final StackTraceElement artificialFrame(String message) {
        String stringPlus = Intrinsics.stringPlus("\b\b\b(", message);
        Log1F380D.a((Object) stringPlus);
        return new StackTraceElement(stringPlus, "\b", "\b", -1);
    }

    private static final <E extends Throwable> Pair<E, StackTraceElement[]> causeAndStacktrace(E $this$causeAndStacktrace) {
        boolean z;
        Throwable cause = $this$causeAndStacktrace.getCause();
        if (cause == null || !Intrinsics.areEqual((Object) cause.getClass(), (Object) $this$causeAndStacktrace.getClass())) {
            return TuplesKt.to($this$causeAndStacktrace, new StackTraceElement[0]);
        }
        StackTraceElement[] stackTrace = $this$causeAndStacktrace.getStackTrace();
        StackTraceElement[] stackTraceElementArr = stackTrace;
        int length = stackTraceElementArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = false;
                break;
            }
            StackTraceElement stackTraceElement = stackTraceElementArr[i];
            i++;
            if (isArtificial(stackTraceElement)) {
                z = true;
                break;
            }
        }
        return z ? TuplesKt.to(cause, stackTrace) : TuplesKt.to($this$causeAndStacktrace, new StackTraceElement[0]);
    }

    private static final <E extends Throwable> E createFinalException(E cause, E result, ArrayDeque<StackTraceElement> resultStackTrace) {
        resultStackTrace.addFirst(artificialFrame("Coroutine boundary"));
        StackTraceElement[] stackTrace = cause.getStackTrace();
        int frameIndex = frameIndex(stackTrace, baseContinuationImplClassName);
        int i = 0;
        if (frameIndex == -1) {
            Object[] array = resultStackTrace.toArray(new StackTraceElement[0]);
            if (array != null) {
                result.setStackTrace((StackTraceElement[]) array);
                return result;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        }
        StackTraceElement[] stackTraceElementArr = new StackTraceElement[(resultStackTrace.size() + frameIndex)];
        int i2 = 0;
        while (i2 < frameIndex) {
            int i3 = i2;
            i2++;
            stackTraceElementArr[i3] = stackTrace[i3];
        }
        Iterator<StackTraceElement> it = resultStackTrace.iterator();
        while (it.hasNext()) {
            int i4 = i;
            i++;
            stackTraceElementArr[frameIndex + i4] = it.next();
        }
        result.setStackTrace(stackTraceElementArr);
        return result;
    }

    private static final ArrayDeque<StackTraceElement> createStackTrace(CoroutineStackFrame continuation) {
        ArrayDeque<StackTraceElement> arrayDeque = new ArrayDeque<>();
        StackTraceElement stackTraceElement = continuation.getStackTraceElement();
        if (stackTraceElement != null) {
            arrayDeque.add(stackTraceElement);
        }
        CoroutineStackFrame coroutineStackFrame = continuation;
        while (true) {
            CoroutineStackFrame coroutineStackFrame2 = null;
            CoroutineStackFrame coroutineStackFrame3 = coroutineStackFrame instanceof CoroutineStackFrame ? coroutineStackFrame : null;
            if (coroutineStackFrame3 != null) {
                coroutineStackFrame2 = coroutineStackFrame3.getCallerFrame();
            }
            if (coroutineStackFrame2 == null) {
                return arrayDeque;
            }
            coroutineStackFrame = coroutineStackFrame2;
            StackTraceElement stackTraceElement2 = coroutineStackFrame.getStackTraceElement();
            if (stackTraceElement2 != null) {
                arrayDeque.add(stackTraceElement2);
            }
        }
    }

    private static final boolean elementWiseEquals(StackTraceElement $this$elementWiseEquals, StackTraceElement e) {
        return $this$elementWiseEquals.getLineNumber() == e.getLineNumber() && Intrinsics.areEqual((Object) $this$elementWiseEquals.getMethodName(), (Object) e.getMethodName()) && Intrinsics.areEqual((Object) $this$elementWiseEquals.getFileName(), (Object) e.getFileName()) && Intrinsics.areEqual((Object) $this$elementWiseEquals.getClassName(), (Object) e.getClassName());
    }

    private static final int frameIndex(StackTraceElement[] $this$frameIndex, String methodName) {
        StackTraceElement[] stackTraceElementArr = $this$frameIndex;
        int length = stackTraceElementArr.length;
        int i = 0;
        while (i < length) {
            int i2 = i;
            i++;
            if (Intrinsics.areEqual((Object) methodName, (Object) stackTraceElementArr[i2].getClassName())) {
                return i2;
            }
        }
        return -1;
    }

    public static final void initCause(Throwable $this$initCause, Throwable cause) {
        $this$initCause.initCause(cause);
    }

    public static final boolean isArtificial(StackTraceElement $this$isArtificial) {
        return StringsKt.startsWith$default($this$isArtificial.getClassName(), "\b\b\b", false, 2, (Object) null);
    }

    private static final void mergeRecoveredTraces(StackTraceElement[] recoveredStacktrace, ArrayDeque<StackTraceElement> result) {
        int i;
        int i2;
        StackTraceElement[] stackTraceElementArr = recoveredStacktrace;
        int length = stackTraceElementArr.length;
        int i3 = 0;
        while (true) {
            if (i3 >= length) {
                i = -1;
                break;
            }
            i = i3;
            i3++;
            if (isArtificial(stackTraceElementArr[i])) {
                break;
            }
        }
        int i4 = i + 1;
        int length2 = recoveredStacktrace.length - 1;
        if (i4 <= length2) {
            int i5 = length2;
            do {
                i2 = i5;
                i5--;
                if (elementWiseEquals(recoveredStacktrace[i2], result.getLast())) {
                    result.removeLast();
                }
                result.addFirst(recoveredStacktrace[i2]);
            } while (i2 != i4);
        }
    }

    public static final Object recoverAndThrow(Throwable exception, Continuation<?> $completion) {
        if (DebugKt.getRECOVER_STACK_TRACES()) {
            Continuation<?> continuation = $completion;
            if (!(continuation instanceof CoroutineStackFrame)) {
                throw exception;
            }
            throw recoverFromStackFrame(exception, (CoroutineStackFrame) continuation);
        }
        throw exception;
    }

    private static final Object recoverAndThrow$$forInline(Throwable exception, Continuation<?> $completion) {
        if (DebugKt.getRECOVER_STACK_TRACES()) {
            InlineMarker.mark(0);
            Continuation<?> continuation = $completion;
            if (!(continuation instanceof CoroutineStackFrame)) {
                throw exception;
            }
            throw recoverFromStackFrame(exception, (CoroutineStackFrame) continuation);
        }
        throw exception;
    }

    /* access modifiers changed from: private */
    public static final <E extends Throwable> E recoverFromStackFrame(E exception, CoroutineStackFrame continuation) {
        Pair causeAndStacktrace = causeAndStacktrace(exception);
        E e = (Throwable) causeAndStacktrace.component1();
        StackTraceElement[] stackTraceElementArr = (StackTraceElement[]) causeAndStacktrace.component2();
        Throwable tryCopyAndVerify = tryCopyAndVerify(e);
        if (tryCopyAndVerify == null) {
            return exception;
        }
        ArrayDeque createStackTrace = createStackTrace(continuation);
        if (createStackTrace.isEmpty()) {
            return exception;
        }
        if (e != exception) {
            mergeRecoveredTraces(stackTraceElementArr, createStackTrace);
        }
        return createFinalException(e, tryCopyAndVerify, createStackTrace);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0007, code lost:
        r0 = tryCopyAndVerify(r2);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final <E extends java.lang.Throwable> E recoverStackTrace(E r2) {
        /*
            boolean r0 = kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES()
            if (r0 != 0) goto L_0x0007
            return r2
        L_0x0007:
            java.lang.Throwable r0 = tryCopyAndVerify(r2)
            if (r0 != 0) goto L_0x000e
            return r2
        L_0x000e:
            java.lang.Throwable r1 = sanitizeStackTrace(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverStackTrace(java.lang.Throwable):java.lang.Throwable");
    }

    public static final <E extends Throwable> E recoverStackTrace(E exception, Continuation<?> continuation) {
        return (!DebugKt.getRECOVER_STACK_TRACES() || !(continuation instanceof CoroutineStackFrame)) ? exception : recoverFromStackFrame(exception, (CoroutineStackFrame) continuation);
    }

    private static final <E extends Throwable> E sanitizeStackTrace(E $this$sanitizeStackTrace) {
        StackTraceElement[] stackTrace = $this$sanitizeStackTrace.getStackTrace();
        int length = stackTrace.length;
        int frameIndex = frameIndex(stackTrace, stackTraceRecoveryClassName);
        int i = frameIndex + 1;
        int frameIndex2 = frameIndex(stackTrace, baseContinuationImplClassName);
        int i2 = 0;
        int i3 = (length - frameIndex) - (frameIndex2 == -1 ? 0 : length - frameIndex2);
        StackTraceElement[] stackTraceElementArr = new StackTraceElement[i3];
        while (i2 < i3) {
            stackTraceElementArr[i2] = i2 == 0 ? artificialFrame("Coroutine boundary") : stackTrace[(i + i2) - 1];
            i2++;
        }
        $this$sanitizeStackTrace.setStackTrace(stackTraceElementArr);
        return $this$sanitizeStackTrace;
    }

    private static final <E extends Throwable> E tryCopyAndVerify(E exception) {
        E tryCopyException = ExceptionsConstructorKt.tryCopyException(exception);
        if (tryCopyException == null) {
            return null;
        }
        if ((exception instanceof CopyableThrowable) || Intrinsics.areEqual((Object) tryCopyException.getMessage(), (Object) exception.getMessage())) {
            return tryCopyException;
        }
        return null;
    }

    public static final <E extends Throwable> E unwrap(E exception) {
        return !DebugKt.getRECOVER_STACK_TRACES() ? exception : unwrapImpl(exception);
    }

    public static final <E extends Throwable> E unwrapImpl(E exception) {
        E cause = exception.getCause();
        if (cause == null || !Intrinsics.areEqual((Object) cause.getClass(), (Object) exception.getClass())) {
            return exception;
        }
        StackTraceElement[] stackTrace = exception.getStackTrace();
        int length = stackTrace.length;
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            StackTraceElement stackTraceElement = stackTrace[i];
            i++;
            if (isArtificial(stackTraceElement)) {
                z = true;
                break;
            }
        }
        return z ? cause : exception;
    }
}

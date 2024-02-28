package kotlinx.coroutines.debug.internal;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import kotlin.KotlinVersion;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.concurrent.ThreadsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;
import kotlin.text.Typography;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobSupport;
import kotlinx.coroutines.internal.ScopeCoroutine;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000Ö\u0001\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u0003\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u001b\bÀ\u0002\u0018\u00002\u00020\u0014:\u0002\u0001B\t\b\u0002¢\u0006\u0004\b\u0001\u0010\u0002J3\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\"\u0004\b\u0000\u0010\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u00042\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0002¢\u0006\u0004\b\b\u0010\tJ\u0015\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\n¢\u0006\u0004\b\r\u0010\u000eJ\u0013\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f¢\u0006\u0004\b\u0011\u0010\u0012J\u0013\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013¢\u0006\u0004\b\u0015\u0010\u0016J@\u0010\u001c\u001a\b\u0012\u0004\u0012\u00028\u00000\u000f\"\b\b\u0000\u0010\u0017*\u00020\u00142\u001e\b\u0004\u0010\u001b\u001a\u0018\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0019\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00028\u00000\u0018H\b¢\u0006\u0004\b\u001c\u0010\u001dJ\u0017\u0010\u001e\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\nH\u0002¢\u0006\u0004\b\u001e\u0010\u000eJ\u0013\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001f0\u000f¢\u0006\u0004\b \u0010\u0012J)\u0010$\u001a\b\u0012\u0004\u0012\u00020\"0\u000f2\u0006\u0010!\u001a\u00020\u00102\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\"0\u000f¢\u0006\u0004\b$\u0010%J\u0015\u0010'\u001a\u00020&2\u0006\u0010!\u001a\u00020\u0010¢\u0006\u0004\b'\u0010(J5\u0010,\u001a\b\u0012\u0004\u0012\u00020\"0\u000f2\u0006\u0010)\u001a\u00020&2\b\u0010+\u001a\u0004\u0018\u00010*2\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\"0\u000fH\u0002¢\u0006\u0004\b,\u0010-J?\u00102\u001a\u000e\u0012\u0004\u0012\u00020.\u0012\u0004\u0012\u00020.012\u0006\u0010/\u001a\u00020.2\f\u00100\u001a\b\u0012\u0004\u0012\u00020\"0\u00132\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\"0\u000fH\u0002¢\u0006\u0004\b2\u00103J3\u00105\u001a\u00020.2\u0006\u00104\u001a\u00020.2\f\u00100\u001a\b\u0012\u0004\u0012\u00020\"0\u00132\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\"0\u000fH\u0002¢\u0006\u0004\b5\u00106J\u001d\u00109\u001a\u0010\u0012\u0004\u0012\u000208\u0012\u0004\u0012\u00020\f\u0018\u000107H\u0002¢\u0006\u0004\b9\u0010:J\u0015\u0010=\u001a\u00020&2\u0006\u0010<\u001a\u00020;¢\u0006\u0004\b=\u0010>J\r\u0010?\u001a\u00020\f¢\u0006\u0004\b?\u0010\u0002J%\u0010A\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\n2\f\u0010@\u001a\b\u0012\u0004\u0012\u00020\"0\u000fH\u0002¢\u0006\u0004\bA\u0010BJ\u001b\u0010D\u001a\u00020\f2\n\u0010C\u001a\u0006\u0012\u0002\b\u00030\u0019H\u0002¢\u0006\u0004\bD\u0010EJ)\u0010H\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\"\u0004\b\u0000\u0010\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0000¢\u0006\u0004\bF\u0010GJ\u001b\u0010K\u001a\u00020\f2\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u0004H\u0000¢\u0006\u0004\bI\u0010JJ\u001b\u0010M\u001a\u00020\f2\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u0004H\u0000¢\u0006\u0004\bL\u0010JJ'\u0010P\u001a\b\u0012\u0004\u0012\u00020\"0\u000f\"\b\b\u0000\u0010\u0003*\u00020N2\u0006\u0010O\u001a\u00028\u0000H\u0002¢\u0006\u0004\bP\u0010QJ\u000f\u0010R\u001a\u00020\fH\u0002¢\u0006\u0004\bR\u0010\u0002J\u000f\u0010S\u001a\u00020\fH\u0002¢\u0006\u0004\bS\u0010\u0002J\r\u0010T\u001a\u00020\f¢\u0006\u0004\bT\u0010\u0002J\u001f\u0010V\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020U2\u0006\u0010)\u001a\u00020&H\u0002¢\u0006\u0004\bV\u0010WJ#\u0010X\u001a\u00020\f2\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u00042\u0006\u0010)\u001a\u00020&H\u0002¢\u0006\u0004\bX\u0010YJ/\u0010X\u001a\u00020\f2\n\u0010C\u001a\u0006\u0012\u0002\b\u00030\u00192\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u00042\u0006\u0010)\u001a\u00020&H\u0002¢\u0006\u0004\bX\u0010ZJ;\u0010b\u001a\u00020\f*\u00020;2\u0012\u0010]\u001a\u000e\u0012\u0004\u0012\u00020;\u0012\u0004\u0012\u00020\\0[2\n\u0010`\u001a\u00060^j\u0002`_2\u0006\u0010a\u001a\u00020&H\u0002¢\u0006\u0004\bb\u0010cJ\u0017\u0010d\u001a\u000208*\u0006\u0012\u0002\b\u00030\u0019H\u0002¢\u0006\u0004\bd\u0010eJ\u001d\u0010C\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u0019*\u0006\u0012\u0002\b\u00030\u0004H\u0002¢\u0006\u0004\bC\u0010fJ\u001a\u0010C\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u0019*\u00020UH\u0010¢\u0006\u0004\bC\u0010gJ\u0016\u0010h\u001a\u0004\u0018\u00010U*\u00020UH\u0010¢\u0006\u0004\bh\u0010iJ\u001b\u0010j\u001a\u0004\u0018\u00010\u0006*\b\u0012\u0004\u0012\u00020\"0\u000fH\u0002¢\u0006\u0004\bj\u0010kJ\u0013\u0010l\u001a\u00020&*\u00020\u0014H\u0002¢\u0006\u0004\bl\u0010mR\u0014\u0010n\u001a\u00020&8\u0002XT¢\u0006\u0006\n\u0004\bn\u0010oR \u0010q\u001a\u000e\u0012\u0004\u0012\u00020U\u0012\u0004\u0012\u00020\\0p8\u0002X\u0004¢\u0006\u0006\n\u0004\bq\u0010rR\u001e\u0010v\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00190s8BX\u0004¢\u0006\u0006\u001a\u0004\bt\u0010uR$\u0010w\u001a\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0019\u0012\u0004\u0012\u0002080p8\u0002X\u0004¢\u0006\u0006\n\u0004\bw\u0010rR\u0014\u0010y\u001a\u00020x8\u0002X\u0004¢\u0006\u0006\n\u0004\by\u0010zR\u0014\u0010|\u001a\u00020{8\u0002X\u0004¢\u0006\u0006\n\u0004\b|\u0010}R\"\u0010~\u001a\u0010\u0012\u0004\u0012\u000208\u0012\u0004\u0012\u00020\f\u0018\u0001078\u0002X\u0004¢\u0006\u0006\n\u0004\b~\u0010R)\u0010\u0001\u001a\u0002088\u0006@\u0006X\u000e¢\u0006\u0018\n\u0006\b\u0001\u0010\u0001\u001a\u0006\b\u0001\u0010\u0001\"\u0006\b\u0001\u0010\u0001R\u0019\u0010\u0001\u001a\u00020.8\u0002@\u0002X\u000e¢\u0006\b\n\u0006\b\u0001\u0010\u0001R\u0017\u0010\u0001\u001a\u0002088@X\u0004¢\u0006\b\u001a\u0006\b\u0001\u0010\u0001R)\u0010\u0001\u001a\u0002088\u0006@\u0006X\u000e¢\u0006\u0018\n\u0006\b\u0001\u0010\u0001\u001a\u0006\b\u0001\u0010\u0001\"\u0006\b\u0001\u0010\u0001R\u001b\u0010\u0001\u001a\u0004\u0018\u00010*8\u0002@\u0002X\u000e¢\u0006\b\n\u0006\b\u0001\u0010\u0001R\"\u0010\u0001\u001a\u00020&*\u00020;8BX\u0004¢\u0006\u000f\u0012\u0006\b\u0001\u0010\u0001\u001a\u0005\b\u0001\u0010>R\u001b\u0010\u0001\u001a\u000208*\u00020\"8BX\u0004¢\u0006\b\u001a\u0006\b\u0001\u0010\u0001¨\u0006\u0001"}, d2 = {"Lkotlinx/coroutines/debug/internal/DebugProbesImpl;", "<init>", "()V", "T", "Lkotlin/coroutines/Continuation;", "completion", "Lkotlinx/coroutines/debug/internal/StackTraceFrame;", "frame", "createOwner", "(Lkotlin/coroutines/Continuation;Lkotlinx/coroutines/debug/internal/StackTraceFrame;)Lkotlin/coroutines/Continuation;", "Ljava/io/PrintStream;", "out", "", "dumpCoroutines", "(Ljava/io/PrintStream;)V", "", "Lkotlinx/coroutines/debug/internal/DebugCoroutineInfo;", "dumpCoroutinesInfo", "()Ljava/util/List;", "", "", "dumpCoroutinesInfoAsJsonAndReferences", "()[Ljava/lang/Object;", "R", "Lkotlin/Function2;", "Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;", "Lkotlin/coroutines/CoroutineContext;", "create", "dumpCoroutinesInfoImpl", "(Lkotlin/jvm/functions/Function2;)Ljava/util/List;", "dumpCoroutinesSynchronized", "Lkotlinx/coroutines/debug/internal/DebuggerInfo;", "dumpDebuggerInfo", "info", "Ljava/lang/StackTraceElement;", "coroutineTrace", "enhanceStackTraceWithThreadDump", "(Lkotlinx/coroutines/debug/internal/DebugCoroutineInfo;Ljava/util/List;)Ljava/util/List;", "", "enhanceStackTraceWithThreadDumpAsJson", "(Lkotlinx/coroutines/debug/internal/DebugCoroutineInfo;)Ljava/lang/String;", "state", "Ljava/lang/Thread;", "thread", "enhanceStackTraceWithThreadDumpImpl", "(Ljava/lang/String;Ljava/lang/Thread;Ljava/util/List;)Ljava/util/List;", "", "indexOfResumeWith", "actualTrace", "Lkotlin/Pair;", "findContinuationStartIndex", "(I[Ljava/lang/StackTraceElement;Ljava/util/List;)Lkotlin/Pair;", "frameIndex", "findIndexOfFrame", "(I[Ljava/lang/StackTraceElement;Ljava/util/List;)I", "Lkotlin/Function1;", "", "getDynamicAttach", "()Lkotlin/jvm/functions/Function1;", "Lkotlinx/coroutines/Job;", "job", "hierarchyToString", "(Lkotlinx/coroutines/Job;)Ljava/lang/String;", "install", "frames", "printStackTrace", "(Ljava/io/PrintStream;Ljava/util/List;)V", "owner", "probeCoroutineCompleted", "(Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;)V", "probeCoroutineCreated$kotlinx_coroutines_core", "(Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "probeCoroutineCreated", "probeCoroutineResumed$kotlinx_coroutines_core", "(Lkotlin/coroutines/Continuation;)V", "probeCoroutineResumed", "probeCoroutineSuspended$kotlinx_coroutines_core", "probeCoroutineSuspended", "", "throwable", "sanitizeStackTrace", "(Ljava/lang/Throwable;)Ljava/util/List;", "startWeakRefCleanerThread", "stopWeakRefCleanerThread", "uninstall", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "updateRunningState", "(Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;Ljava/lang/String;)V", "updateState", "(Lkotlin/coroutines/Continuation;Ljava/lang/String;)V", "(Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;Lkotlin/coroutines/Continuation;Ljava/lang/String;)V", "", "Lkotlinx/coroutines/debug/internal/DebugCoroutineInfoImpl;", "map", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "builder", "indent", "build", "(Lkotlinx/coroutines/Job;Ljava/util/Map;Ljava/lang/StringBuilder;Ljava/lang/String;)V", "isFinished", "(Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;)Z", "(Lkotlin/coroutines/Continuation;)Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;", "(Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;)Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;", "realCaller", "(Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;)Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "toStackTraceFrame", "(Ljava/util/List;)Lkotlinx/coroutines/debug/internal/StackTraceFrame;", "toStringWithQuotes", "(Ljava/lang/Object;)Ljava/lang/String;", "ARTIFICIAL_FRAME_MESSAGE", "Ljava/lang/String;", "Lkotlinx/coroutines/debug/internal/ConcurrentWeakMap;", "callerInfoCache", "Lkotlinx/coroutines/debug/internal/ConcurrentWeakMap;", "", "getCapturedCoroutines", "()Ljava/util/Set;", "capturedCoroutines", "capturedCoroutinesMap", "Ljava/util/concurrent/locks/ReentrantReadWriteLock;", "coroutineStateLock", "Ljava/util/concurrent/locks/ReentrantReadWriteLock;", "Ljava/text/SimpleDateFormat;", "dateFormat", "Ljava/text/SimpleDateFormat;", "dynamicAttach", "Lkotlin/jvm/functions/Function1;", "enableCreationStackTraces", "Z", "getEnableCreationStackTraces", "()Z", "setEnableCreationStackTraces", "(Z)V", "installations", "I", "isInstalled$kotlinx_coroutines_core", "isInstalled", "sanitizeStackTraces", "getSanitizeStackTraces", "setSanitizeStackTraces", "weakRefCleanerThread", "Ljava/lang/Thread;", "getDebugString", "getDebugString$annotations", "(Lkotlinx/coroutines/Job;)V", "debugString", "isInternalMethod", "(Ljava/lang/StackTraceElement;)Z", "CoroutineOwner", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: 0190 */
public final class DebugProbesImpl {
    private static final String ARTIFICIAL_FRAME_MESSAGE = "Coroutine creation stacktrace";
    public static final DebugProbesImpl INSTANCE;
    /* access modifiers changed from: private */
    public static final ConcurrentWeakMap<CoroutineStackFrame, DebugCoroutineInfoImpl> callerInfoCache = new ConcurrentWeakMap<>(true);
    private static final ConcurrentWeakMap<CoroutineOwner<?>, Boolean> capturedCoroutinesMap = new ConcurrentWeakMap<>(false, 1, (DefaultConstructorMarker) null);
    private static final ReentrantReadWriteLock coroutineStateLock = new ReentrantReadWriteLock();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final /* synthetic */ SequenceNumberRefVolatile debugProbesImpl$SequenceNumberRefVolatile = new SequenceNumberRefVolatile(0);
    private static final Function1<Boolean, Unit> dynamicAttach;
    private static boolean enableCreationStackTraces = true;
    private static volatile int installations;
    private static boolean sanitizeStackTraces = true;
    private static final /* synthetic */ AtomicLongFieldUpdater sequenceNumber$FU = AtomicLongFieldUpdater.newUpdater(SequenceNumberRefVolatile.class, "sequenceNumber");
    private static Thread weakRefCleanerThread;

    @Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00020\u0003B%\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\bJ\n\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0016J\u001e\u0010\u0012\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00028\u00000\u0015H\u0016ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u0016\u0010\t\u001a\u0004\u0018\u00010\u00038VX\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0012\u0010\f\u001a\u00020\rX\u0005¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u00028\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006\u0019"}, d2 = {"Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;", "T", "Lkotlin/coroutines/Continuation;", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "delegate", "info", "Lkotlinx/coroutines/debug/internal/DebugCoroutineInfoImpl;", "frame", "(Lkotlin/coroutines/Continuation;Lkotlinx/coroutines/debug/internal/DebugCoroutineInfoImpl;Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;)V", "callerFrame", "getCallerFrame", "()Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "getStackTraceElement", "Ljava/lang/StackTraceElement;", "resumeWith", "", "result", "Lkotlin/Result;", "(Ljava/lang/Object;)V", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: DebugProbesImpl.kt */
    private static final class CoroutineOwner<T> implements Continuation<T>, CoroutineStackFrame {
        public final Continuation<T> delegate;
        private final CoroutineStackFrame frame;
        public final DebugCoroutineInfoImpl info;

        public CoroutineOwner(Continuation<? super T> delegate2, DebugCoroutineInfoImpl info2, CoroutineStackFrame frame2) {
            this.delegate = delegate2;
            this.info = info2;
            this.frame = frame2;
        }

        public CoroutineStackFrame getCallerFrame() {
            CoroutineStackFrame coroutineStackFrame = this.frame;
            if (coroutineStackFrame == null) {
                return null;
            }
            return coroutineStackFrame.getCallerFrame();
        }

        public CoroutineContext getContext() {
            return this.delegate.getContext();
        }

        public StackTraceElement getStackTraceElement() {
            CoroutineStackFrame coroutineStackFrame = this.frame;
            if (coroutineStackFrame == null) {
                return null;
            }
            return coroutineStackFrame.getStackTraceElement();
        }

        public void resumeWith(Object result) {
            DebugProbesImpl.INSTANCE.probeCoroutineCompleted(this);
            this.delegate.resumeWith(result);
        }

        public String toString() {
            return this.delegate.toString();
        }
    }

    /* synthetic */ class SequenceNumberRefVolatile {
        volatile long sequenceNumber;

        public SequenceNumberRefVolatile(long j) {
            this.sequenceNumber = j;
        }
    }

    static {
        DebugProbesImpl debugProbesImpl = new DebugProbesImpl();
        INSTANCE = debugProbesImpl;
        dynamicAttach = debugProbesImpl.getDynamicAttach();
    }

    private DebugProbesImpl() {
    }

    private final void build(Job $this$build, Map<Job, DebugCoroutineInfoImpl> map, StringBuilder builder, String indent) {
        String str;
        DebugCoroutineInfoImpl debugCoroutineInfoImpl = map.get($this$build);
        if (debugCoroutineInfoImpl != null) {
            builder.append(indent + getDebugString($this$build) + ", continuation is " + debugCoroutineInfoImpl.getState() + " at line " + ((StackTraceElement) CollectionsKt.firstOrNull(debugCoroutineInfoImpl.lastObservedStackTrace())) + 10);
            str = Intrinsics.stringPlus(indent, "\t");
            Log1F380D.a((Object) str);
        } else if (!($this$build instanceof ScopeCoroutine)) {
            builder.append(indent + getDebugString($this$build) + 10);
            str = Intrinsics.stringPlus(indent, "\t");
            Log1F380D.a((Object) str);
        } else {
            str = indent;
        }
        for (Job build : $this$build.getChildren()) {
            build(build, map, builder, str);
        }
    }

    private final <T> Continuation<T> createOwner(Continuation<? super T> completion, StackTraceFrame frame) {
        if (!isInstalled$kotlinx_coroutines_core()) {
            return completion;
        }
        CoroutineOwner coroutineOwner = new CoroutineOwner(completion, new DebugCoroutineInfoImpl(completion.getContext(), frame, sequenceNumber$FU.incrementAndGet(debugProbesImpl$SequenceNumberRefVolatile)), frame);
        ConcurrentWeakMap<CoroutineOwner<?>, Boolean> concurrentWeakMap = capturedCoroutinesMap;
        concurrentWeakMap.put(coroutineOwner, true);
        if (!isInstalled$kotlinx_coroutines_core()) {
            concurrentWeakMap.clear();
        }
        return coroutineOwner;
    }

    /*  JADX ERROR: StackOverflow in pass: MarkFinallyVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    private final <R> java.util.List<R> dumpCoroutinesInfoImpl(kotlin.jvm.functions.Function2<? super kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?>, ? super kotlin.coroutines.CoroutineContext, ? extends R> r11) {
        /*
            r10 = this;
            r0 = 0
            java.util.concurrent.locks.ReentrantReadWriteLock r1 = coroutineStateLock
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r2 = r1.readLock()
            int r3 = r1.getWriteHoldCount()
            r4 = 0
            if (r3 != 0) goto L_0x0013
            int r3 = r1.getReadHoldCount()
            goto L_0x0014
        L_0x0013:
            r3 = r4
        L_0x0014:
            r5 = r4
        L_0x0015:
            if (r5 >= r3) goto L_0x001d
            int r5 = r5 + 1
            r2.unlock()
            goto L_0x0015
        L_0x001d:
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r1 = r1.writeLock()
            r1.lock()
            r5 = 0
            r6 = 1
            kotlinx.coroutines.debug.internal.DebugProbesImpl r7 = INSTANCE     // Catch:{ all -> 0x0073 }
            boolean r8 = r7.isInstalled$kotlinx_coroutines_core()     // Catch:{ all -> 0x0073 }
            if (r8 == 0) goto L_0x0066
            java.util.Set r7 = r7.getCapturedCoroutines()     // Catch:{ all -> 0x0073 }
            java.lang.Iterable r7 = (java.lang.Iterable) r7     // Catch:{ all -> 0x0073 }
            kotlin.sequences.Sequence r7 = kotlin.collections.CollectionsKt.asSequence(r7)     // Catch:{ all -> 0x0073 }
            r8 = 0
            kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$lambda-12$$inlined$sortedBy$1 r9 = new kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$lambda-12$$inlined$sortedBy$1     // Catch:{ all -> 0x0073 }
            r9.<init>()     // Catch:{ all -> 0x0073 }
            java.util.Comparator r9 = (java.util.Comparator) r9     // Catch:{ all -> 0x0073 }
            kotlin.sequences.Sequence r9 = kotlin.sequences.SequencesKt.sortedWith(r7, r9)     // Catch:{ all -> 0x0073 }
            kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$1$3 r7 = new kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$1$3     // Catch:{ all -> 0x0073 }
            r7.<init>(r11)     // Catch:{ all -> 0x0073 }
            kotlin.jvm.functions.Function1 r7 = (kotlin.jvm.functions.Function1) r7     // Catch:{ all -> 0x0073 }
            kotlin.sequences.Sequence r7 = kotlin.sequences.SequencesKt.mapNotNull(r9, r7)     // Catch:{ all -> 0x0073 }
            java.util.List r7 = kotlin.sequences.SequencesKt.toList(r7)     // Catch:{ all -> 0x0073 }
            kotlin.jvm.internal.InlineMarker.finallyStart(r6)
        L_0x0057:
            if (r4 >= r3) goto L_0x005f
            int r4 = r4 + 1
            r2.lock()
            goto L_0x0057
        L_0x005f:
            r1.unlock()
            kotlin.jvm.internal.InlineMarker.finallyEnd(r6)
            return r7
        L_0x0066:
            r7 = 0
            java.lang.String r8 = "Debug probes are not installed"
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0073 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x0073 }
            r7.<init>(r8)     // Catch:{ all -> 0x0073 }
            throw r7     // Catch:{ all -> 0x0073 }
        L_0x0073:
            r5 = move-exception
            kotlin.jvm.internal.InlineMarker.finallyStart(r6)
        L_0x0077:
            if (r4 >= r3) goto L_0x007f
            int r4 = r4 + 1
            r2.lock()
            goto L_0x0077
        L_0x007f:
            r1.unlock()
            kotlin.jvm.internal.InlineMarker.finallyEnd(r6)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.debug.internal.DebugProbesImpl.dumpCoroutinesInfoImpl(kotlin.jvm.functions.Function2):java.util.List");
    }

    private final List<StackTraceElement> enhanceStackTraceWithThreadDumpImpl(String state, Thread thread, List<StackTraceElement> coroutineTrace) {
        Object obj;
        int i;
        boolean z;
        List<StackTraceElement> list = coroutineTrace;
        if (!Intrinsics.areEqual((Object) state, (Object) DebugCoroutineInfoImplKt.RUNNING) || thread == null) {
            return list;
        }
        try {
            Result.Companion companion = Result.Companion;
            DebugProbesImpl debugProbesImpl = this;
            obj = Result.m36constructorimpl(thread.getStackTrace());
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.m36constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m42isFailureimpl(obj)) {
            obj = null;
        }
        StackTraceElement[] stackTraceElementArr = (StackTraceElement[]) obj;
        if (stackTraceElementArr == null) {
            return list;
        }
        StackTraceElement[] stackTraceElementArr2 = stackTraceElementArr;
        int length = stackTraceElementArr2.length;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i3 >= length) {
                i = -1;
                break;
            }
            i = i3;
            i3++;
            StackTraceElement stackTraceElement = stackTraceElementArr2[i];
            if (!Intrinsics.areEqual((Object) stackTraceElement.getClassName(), (Object) "kotlin.coroutines.jvm.internal.BaseContinuationImpl") || !Intrinsics.areEqual((Object) stackTraceElement.getMethodName(), (Object) "resumeWith") || !Intrinsics.areEqual((Object) stackTraceElement.getFileName(), (Object) "ContinuationImpl.kt")) {
                z = false;
                continue;
            } else {
                z = true;
                continue;
            }
            if (z) {
                break;
            }
        }
        int i4 = i;
        Pair<Integer, Integer> findContinuationStartIndex = findContinuationStartIndex(i4, stackTraceElementArr, list);
        int intValue = findContinuationStartIndex.component1().intValue();
        int intValue2 = findContinuationStartIndex.component2().intValue();
        if (intValue == -1) {
            return list;
        }
        ArrayList arrayList = new ArrayList((((coroutineTrace.size() + i4) - intValue) - 1) - intValue2);
        int i5 = i4 - intValue2;
        while (i2 < i5) {
            int i6 = i2;
            i2++;
            arrayList.add(stackTraceElementArr[i6]);
        }
        int i7 = intValue + 1;
        int size = coroutineTrace.size();
        while (i7 < size) {
            int i8 = i7;
            i7++;
            arrayList.add(list.get(i8));
        }
        return arrayList;
    }

    private final Pair<Integer, Integer> findContinuationStartIndex(int indexOfResumeWith, StackTraceElement[] actualTrace, List<StackTraceElement> coroutineTrace) {
        int i = 0;
        while (i < 3) {
            int i2 = i + 1;
            int findIndexOfFrame = INSTANCE.findIndexOfFrame((indexOfResumeWith - 1) - i, actualTrace, coroutineTrace);
            if (findIndexOfFrame != -1) {
                return TuplesKt.to(Integer.valueOf(findIndexOfFrame), Integer.valueOf(i));
            }
            i = i2;
        }
        return TuplesKt.to(-1, 0);
    }

    private final int findIndexOfFrame(int frameIndex, StackTraceElement[] actualTrace, List<StackTraceElement> coroutineTrace) {
        StackTraceElement stackTraceElement = (StackTraceElement) ArraysKt.getOrNull((T[]) actualTrace, frameIndex);
        if (stackTraceElement == null) {
            return -1;
        }
        int i = 0;
        for (StackTraceElement next : coroutineTrace) {
            if (Intrinsics.areEqual((Object) next.getFileName(), (Object) stackTraceElement.getFileName()) && Intrinsics.areEqual((Object) next.getClassName(), (Object) stackTraceElement.getClassName()) && Intrinsics.areEqual((Object) next.getMethodName(), (Object) stackTraceElement.getMethodName())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private final Set<CoroutineOwner<?>> getCapturedCoroutines() {
        return capturedCoroutinesMap.keySet();
    }

    private final String getDebugString(Job $this$debugString) {
        return $this$debugString instanceof JobSupport ? ((JobSupport) $this$debugString).toDebugString() : $this$debugString.toString();
    }

    private static /* synthetic */ void getDebugString$annotations(Job job) {
    }

    private final Function1<Boolean, Unit> getDynamicAttach() {
        Object obj;
        try {
            Result.Companion companion = Result.Companion;
            DebugProbesImpl debugProbesImpl = this;
            Object newInstance = Class.forName("kotlinx.coroutines.debug.internal.ByteBuddyDynamicAttach").getConstructors()[0].newInstance(new Object[0]);
            if (newInstance != null) {
                obj = Result.m36constructorimpl((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(newInstance, 1));
                if (Result.m42isFailureimpl(obj)) {
                    obj = null;
                }
                return (Function1) obj;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Function1<kotlin.Boolean, kotlin.Unit>");
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.m36constructorimpl(ResultKt.createFailure(th));
        }
    }

    /* access modifiers changed from: private */
    public final boolean isFinished(CoroutineOwner<?> $this$isFinished) {
        CoroutineContext context = $this$isFinished.info.getContext();
        Job job = context == null ? null : (Job) context.get(Job.Key);
        if (job == null || !job.isCompleted()) {
            return false;
        }
        capturedCoroutinesMap.remove($this$isFinished);
        return true;
    }

    private final boolean isInternalMethod(StackTraceElement $this$isInternalMethod) {
        return StringsKt.startsWith$default($this$isInternalMethod.getClassName(), "kotlinx.coroutines", false, 2, (Object) null);
    }

    private final CoroutineOwner<?> owner(Continuation<?> $this$owner) {
        CoroutineStackFrame coroutineStackFrame = $this$owner instanceof CoroutineStackFrame ? (CoroutineStackFrame) $this$owner : null;
        if (coroutineStackFrame == null) {
            return null;
        }
        return owner(coroutineStackFrame);
    }

    private final CoroutineOwner<?> owner(CoroutineStackFrame $this$owner) {
        CoroutineStackFrame coroutineStackFrame = $this$owner;
        while (!(coroutineStackFrame instanceof CoroutineOwner)) {
            coroutineStackFrame = coroutineStackFrame.getCallerFrame();
            if (coroutineStackFrame == null) {
                return null;
            }
        }
        return (CoroutineOwner) coroutineStackFrame;
    }

    /* access modifiers changed from: private */
    public final void probeCoroutineCompleted(CoroutineOwner<?> owner) {
        capturedCoroutinesMap.remove(owner);
        CoroutineStackFrame lastObservedFrame$kotlinx_coroutines_core = owner.info.getLastObservedFrame$kotlinx_coroutines_core();
        CoroutineStackFrame realCaller = lastObservedFrame$kotlinx_coroutines_core == null ? null : realCaller(lastObservedFrame$kotlinx_coroutines_core);
        if (realCaller != null) {
            callerInfoCache.remove(realCaller);
        }
    }

    private final CoroutineStackFrame realCaller(CoroutineStackFrame $this$realCaller) {
        CoroutineStackFrame coroutineStackFrame = $this$realCaller;
        do {
            coroutineStackFrame = coroutineStackFrame.getCallerFrame();
            if (coroutineStackFrame == null) {
                return null;
            }
        } while (coroutineStackFrame.getStackTraceElement() == null);
        return coroutineStackFrame;
    }

    private final <T extends Throwable> List<StackTraceElement> sanitizeStackTrace(T throwable) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        int length = stackTrace.length;
        StackTraceElement[] stackTraceElementArr = stackTrace;
        int i = -1;
        int length2 = stackTraceElementArr.length - 1;
        if (length2 >= 0) {
            while (true) {
                int i2 = length2;
                length2--;
                if (!Intrinsics.areEqual((Object) stackTraceElementArr[i2].getClassName(), (Object) "kotlin.coroutines.jvm.internal.DebugProbesKt")) {
                    if (length2 < 0) {
                        break;
                    }
                } else {
                    i = i2;
                    break;
                }
            }
        }
        int i3 = i;
        if (!sanitizeStackTraces) {
            int i4 = length - i3;
            ArrayList arrayList = new ArrayList(i4);
            int i5 = 0;
            while (i5 < i4) {
                int i6 = i5 + 1;
                arrayList.add(i5 == 0 ? StackTraceRecoveryKt.artificialFrame(ARTIFICIAL_FRAME_MESSAGE) : stackTrace[i5 + i3]);
                i5 = i6;
            }
            return arrayList;
        }
        ArrayList arrayList2 = new ArrayList((length - i3) + 1);
        arrayList2.add(StackTraceRecoveryKt.artificialFrame(ARTIFICIAL_FRAME_MESSAGE));
        int i7 = i3 + 1;
        while (i7 < length) {
            if (isInternalMethod(stackTrace[i7])) {
                arrayList2.add(stackTrace[i7]);
                int i8 = i7 + 1;
                while (i8 < length && isInternalMethod(stackTrace[i8])) {
                    i8++;
                }
                int i9 = i8 - 1;
                while (i9 > i7 && stackTrace[i9].getFileName() == null) {
                    i9--;
                }
                if (i9 > i7 && i9 < i8 - 1) {
                    arrayList2.add(stackTrace[i9]);
                }
                arrayList2.add(stackTrace[i8 - 1]);
                i7 = i8;
            } else {
                arrayList2.add(stackTrace[i7]);
                i7++;
            }
        }
        return arrayList2;
    }

    private final void startWeakRefCleanerThread() {
        weakRefCleanerThread = ThreadsKt.thread$default(false, true, (ClassLoader) null, "Coroutines Debugger Cleaner", 0, DebugProbesImpl$startWeakRefCleanerThread$1.INSTANCE, 21, (Object) null);
    }

    private final void stopWeakRefCleanerThread() {
        Thread thread = weakRefCleanerThread;
        if (thread != null) {
            weakRefCleanerThread = null;
            thread.interrupt();
            thread.join();
        }
    }

    private final StackTraceFrame toStackTraceFrame(List<StackTraceElement> $this$toStackTraceFrame) {
        List<StackTraceElement> list = $this$toStackTraceFrame;
        StackTraceFrame stackTraceFrame = null;
        if (!list.isEmpty()) {
            ListIterator<StackTraceElement> listIterator = list.listIterator(list.size());
            while (listIterator.hasPrevious()) {
                stackTraceFrame = new StackTraceFrame(stackTraceFrame, listIterator.previous());
            }
        }
        return stackTraceFrame;
    }

    private final String toStringWithQuotes(Object $this$toStringWithQuotes) {
        return new StringBuilder().append(Typography.quote).append($this$toStringWithQuotes).append(Typography.quote).toString();
    }

    private final void updateRunningState(CoroutineStackFrame frame, String state) {
        DebugCoroutineInfoImpl debugCoroutineInfoImpl;
        ReentrantReadWriteLock.ReadLock readLock = coroutineStateLock.readLock();
        readLock.lock();
        try {
            DebugProbesImpl debugProbesImpl = INSTANCE;
            if (debugProbesImpl.isInstalled$kotlinx_coroutines_core()) {
                ConcurrentWeakMap<CoroutineStackFrame, DebugCoroutineInfoImpl> concurrentWeakMap = callerInfoCache;
                DebugCoroutineInfoImpl remove = concurrentWeakMap.remove(frame);
                if (remove != null) {
                    debugCoroutineInfoImpl = remove;
                } else {
                    CoroutineOwner<?> owner = debugProbesImpl.owner(frame);
                    CoroutineStackFrame coroutineStackFrame = null;
                    DebugCoroutineInfoImpl debugCoroutineInfoImpl2 = owner == null ? null : owner.info;
                    if (debugCoroutineInfoImpl2 == null) {
                        readLock.unlock();
                        return;
                    }
                    debugCoroutineInfoImpl = debugCoroutineInfoImpl2;
                    CoroutineStackFrame lastObservedFrame$kotlinx_coroutines_core = debugCoroutineInfoImpl.getLastObservedFrame$kotlinx_coroutines_core();
                    if (lastObservedFrame$kotlinx_coroutines_core != null) {
                        coroutineStackFrame = debugProbesImpl.realCaller(lastObservedFrame$kotlinx_coroutines_core);
                    }
                    CoroutineStackFrame coroutineStackFrame2 = coroutineStackFrame;
                    if (coroutineStackFrame2 != null) {
                        concurrentWeakMap.remove(coroutineStackFrame2);
                    }
                }
                debugCoroutineInfoImpl.updateState$kotlinx_coroutines_core(state, (Continuation) frame);
                CoroutineStackFrame realCaller = debugProbesImpl.realCaller(frame);
                if (realCaller == null) {
                    readLock.unlock();
                    return;
                }
                concurrentWeakMap.put(realCaller, debugCoroutineInfoImpl);
                Unit unit = Unit.INSTANCE;
                readLock.unlock();
            }
        } finally {
            readLock.unlock();
        }
    }

    private final void updateState(Continuation<?> frame, String state) {
        if (isInstalled$kotlinx_coroutines_core()) {
            if (!Intrinsics.areEqual((Object) state, (Object) DebugCoroutineInfoImplKt.RUNNING) || !KotlinVersion.CURRENT.isAtLeast(1, 3, 30)) {
                CoroutineOwner<?> owner = owner(frame);
                if (owner != null) {
                    updateState(owner, frame, state);
                    return;
                }
                return;
            }
            CoroutineStackFrame coroutineStackFrame = frame instanceof CoroutineStackFrame ? (CoroutineStackFrame) frame : null;
            if (coroutineStackFrame != null) {
                updateRunningState(coroutineStackFrame, state);
            }
        }
    }

    private final void updateState(CoroutineOwner<?> owner, Continuation<?> frame, String state) {
        ReentrantReadWriteLock.ReadLock readLock = coroutineStateLock.readLock();
        readLock.lock();
        try {
            if (INSTANCE.isInstalled$kotlinx_coroutines_core()) {
                owner.info.updateState$kotlinx_coroutines_core(state, frame);
                Unit unit = Unit.INSTANCE;
                readLock.unlock();
            }
        } finally {
            readLock.unlock();
        }
    }

    public final void dumpCoroutines(PrintStream out) {
        synchronized (out) {
            INSTANCE.dumpCoroutinesSynchronized(out);
            Unit unit = Unit.INSTANCE;
        }
    }

    /*  JADX ERROR: StackOverflow in pass: MarkFinallyVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public final java.util.List<kotlinx.coroutines.debug.internal.DebugCoroutineInfo> dumpCoroutinesInfo() {
        /*
            r10 = this;
            r0 = r10
            r1 = 0
            java.util.concurrent.locks.ReentrantReadWriteLock r2 = coroutineStateLock
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r3 = r2.readLock()
            int r4 = r2.getWriteHoldCount()
            r5 = 0
            if (r4 != 0) goto L_0x0014
            int r4 = r2.getReadHoldCount()
            goto L_0x0015
        L_0x0014:
            r4 = r5
        L_0x0015:
            r6 = r5
        L_0x0016:
            if (r6 >= r4) goto L_0x001e
            int r6 = r6 + 1
            r3.unlock()
            goto L_0x0016
        L_0x001e:
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r2 = r2.writeLock()
            r2.lock()
            r6 = 0
            kotlinx.coroutines.debug.internal.DebugProbesImpl r7 = INSTANCE     // Catch:{ all -> 0x006f }
            boolean r8 = r7.isInstalled$kotlinx_coroutines_core()     // Catch:{ all -> 0x006f }
            if (r8 == 0) goto L_0x0062
            java.util.Set r7 = r7.getCapturedCoroutines()     // Catch:{ all -> 0x006f }
            java.lang.Iterable r7 = (java.lang.Iterable) r7     // Catch:{ all -> 0x006f }
            kotlin.sequences.Sequence r7 = kotlin.collections.CollectionsKt.asSequence(r7)     // Catch:{ all -> 0x006f }
            r8 = 0
            kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$lambda-12$$inlined$sortedBy$1 r9 = new kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$lambda-12$$inlined$sortedBy$1     // Catch:{ all -> 0x006f }
            r9.<init>()     // Catch:{ all -> 0x006f }
            java.util.Comparator r9 = (java.util.Comparator) r9     // Catch:{ all -> 0x006f }
            kotlin.sequences.Sequence r9 = kotlin.sequences.SequencesKt.sortedWith(r7, r9)     // Catch:{ all -> 0x006f }
            kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfo$$inlined$dumpCoroutinesInfoImpl$1 r7 = new kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfo$$inlined$dumpCoroutinesInfoImpl$1     // Catch:{ all -> 0x006f }
            r7.<init>()     // Catch:{ all -> 0x006f }
            kotlin.jvm.functions.Function1 r7 = (kotlin.jvm.functions.Function1) r7     // Catch:{ all -> 0x006f }
            kotlin.sequences.Sequence r7 = kotlin.sequences.SequencesKt.mapNotNull(r9, r7)     // Catch:{ all -> 0x006f }
            java.util.List r7 = kotlin.sequences.SequencesKt.toList(r7)     // Catch:{ all -> 0x006f }
        L_0x0055:
            if (r5 >= r4) goto L_0x005d
            int r5 = r5 + 1
            r3.lock()
            goto L_0x0055
        L_0x005d:
            r2.unlock()
            return r7
        L_0x0062:
            r7 = 0
            java.lang.String r8 = "Debug probes are not installed"
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException     // Catch:{ all -> 0x006f }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x006f }
            r7.<init>(r8)     // Catch:{ all -> 0x006f }
            throw r7     // Catch:{ all -> 0x006f }
        L_0x006f:
            r6 = move-exception
        L_0x0070:
            if (r5 >= r4) goto L_0x0078
            int r5 = r5 + 1
            r3.lock()
            goto L_0x0070
        L_0x0078:
            r2.unlock()
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.debug.internal.DebugProbesImpl.dumpCoroutinesInfo():java.util.List");
    }

    /*  JADX ERROR: StackOverflow in pass: MarkFinallyVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public final java.util.List<kotlinx.coroutines.debug.internal.DebuggerInfo> dumpDebuggerInfo() {
        /*
            r10 = this;
            r0 = r10
            r1 = 0
            java.util.concurrent.locks.ReentrantReadWriteLock r2 = coroutineStateLock
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r3 = r2.readLock()
            int r4 = r2.getWriteHoldCount()
            r5 = 0
            if (r4 != 0) goto L_0x0014
            int r4 = r2.getReadHoldCount()
            goto L_0x0015
        L_0x0014:
            r4 = r5
        L_0x0015:
            r6 = r5
        L_0x0016:
            if (r6 >= r4) goto L_0x001e
            int r6 = r6 + 1
            r3.unlock()
            goto L_0x0016
        L_0x001e:
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r2 = r2.writeLock()
            r2.lock()
            r6 = 0
            kotlinx.coroutines.debug.internal.DebugProbesImpl r7 = INSTANCE     // Catch:{ all -> 0x006f }
            boolean r8 = r7.isInstalled$kotlinx_coroutines_core()     // Catch:{ all -> 0x006f }
            if (r8 == 0) goto L_0x0062
            java.util.Set r7 = r7.getCapturedCoroutines()     // Catch:{ all -> 0x006f }
            java.lang.Iterable r7 = (java.lang.Iterable) r7     // Catch:{ all -> 0x006f }
            kotlin.sequences.Sequence r7 = kotlin.collections.CollectionsKt.asSequence(r7)     // Catch:{ all -> 0x006f }
            r8 = 0
            kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$lambda-12$$inlined$sortedBy$1 r9 = new kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$lambda-12$$inlined$sortedBy$1     // Catch:{ all -> 0x006f }
            r9.<init>()     // Catch:{ all -> 0x006f }
            java.util.Comparator r9 = (java.util.Comparator) r9     // Catch:{ all -> 0x006f }
            kotlin.sequences.Sequence r9 = kotlin.sequences.SequencesKt.sortedWith(r7, r9)     // Catch:{ all -> 0x006f }
            kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpDebuggerInfo$$inlined$dumpCoroutinesInfoImpl$1 r7 = new kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpDebuggerInfo$$inlined$dumpCoroutinesInfoImpl$1     // Catch:{ all -> 0x006f }
            r7.<init>()     // Catch:{ all -> 0x006f }
            kotlin.jvm.functions.Function1 r7 = (kotlin.jvm.functions.Function1) r7     // Catch:{ all -> 0x006f }
            kotlin.sequences.Sequence r7 = kotlin.sequences.SequencesKt.mapNotNull(r9, r7)     // Catch:{ all -> 0x006f }
            java.util.List r7 = kotlin.sequences.SequencesKt.toList(r7)     // Catch:{ all -> 0x006f }
        L_0x0055:
            if (r5 >= r4) goto L_0x005d
            int r5 = r5 + 1
            r3.lock()
            goto L_0x0055
        L_0x005d:
            r2.unlock()
            return r7
        L_0x0062:
            r7 = 0
            java.lang.String r8 = "Debug probes are not installed"
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException     // Catch:{ all -> 0x006f }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x006f }
            r7.<init>(r8)     // Catch:{ all -> 0x006f }
            throw r7     // Catch:{ all -> 0x006f }
        L_0x006f:
            r6 = move-exception
        L_0x0070:
            if (r5 >= r4) goto L_0x0078
            int r5 = r5 + 1
            r3.lock()
            goto L_0x0070
        L_0x0078:
            r2.unlock()
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.debug.internal.DebugProbesImpl.dumpDebuggerInfo():java.util.List");
    }

    public final List<StackTraceElement> enhanceStackTraceWithThreadDump(DebugCoroutineInfo info, List<StackTraceElement> coroutineTrace) {
        return enhanceStackTraceWithThreadDumpImpl(info.getState(), info.getLastObservedThread(), coroutineTrace);
    }

    public final boolean getEnableCreationStackTraces() {
        return enableCreationStackTraces;
    }

    public final boolean getSanitizeStackTraces() {
        return sanitizeStackTraces;
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x00f0 A[LOOP:4: B:41:0x00ee->B:42:0x00f0, LOOP_END] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.String hierarchyToString(kotlinx.coroutines.Job r17) {
        /*
            r16 = this;
            java.util.concurrent.locks.ReentrantReadWriteLock r0 = coroutineStateLock
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r1 = r0.readLock()
            int r2 = r0.getWriteHoldCount()
            if (r2 != 0) goto L_0x0011
            int r2 = r0.getReadHoldCount()
            goto L_0x0012
        L_0x0011:
            r2 = 0
        L_0x0012:
            r4 = 0
        L_0x0013:
            if (r4 >= r2) goto L_0x001b
            int r4 = r4 + 1
            r1.unlock()
            goto L_0x0013
        L_0x001b:
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r4 = r0.writeLock()
            r4.lock()
            r0 = 0
            kotlinx.coroutines.debug.internal.DebugProbesImpl r5 = INSTANCE     // Catch:{ all -> 0x00ea }
            boolean r6 = r5.isInstalled$kotlinx_coroutines_core()     // Catch:{ all -> 0x00ea }
            if (r6 == 0) goto L_0x00d9
            java.util.Set r5 = r5.getCapturedCoroutines()     // Catch:{ all -> 0x00ea }
            java.lang.Iterable r5 = (java.lang.Iterable) r5     // Catch:{ all -> 0x00ea }
            r6 = 0
            java.util.ArrayList r7 = new java.util.ArrayList     // Catch:{ all -> 0x00ea }
            r7.<init>()     // Catch:{ all -> 0x00ea }
            java.util.Collection r7 = (java.util.Collection) r7     // Catch:{ all -> 0x00ea }
            r8 = r5
            r9 = 0
            java.util.Iterator r10 = r8.iterator()     // Catch:{ all -> 0x00ea }
        L_0x0040:
            boolean r11 = r10.hasNext()     // Catch:{ all -> 0x00ea }
            if (r11 == 0) goto L_0x0067
            java.lang.Object r11 = r10.next()     // Catch:{ all -> 0x00ea }
            r12 = r11
            kotlinx.coroutines.debug.internal.DebugProbesImpl$CoroutineOwner r12 = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) r12     // Catch:{ all -> 0x00ea }
            r13 = 0
            kotlin.coroutines.Continuation<T> r14 = r12.delegate     // Catch:{ all -> 0x00ea }
            kotlin.coroutines.CoroutineContext r14 = r14.getContext()     // Catch:{ all -> 0x00ea }
            kotlinx.coroutines.Job$Key r15 = kotlinx.coroutines.Job.Key     // Catch:{ all -> 0x00ea }
            kotlin.coroutines.CoroutineContext$Key r15 = (kotlin.coroutines.CoroutineContext.Key) r15     // Catch:{ all -> 0x00ea }
            kotlin.coroutines.CoroutineContext$Element r14 = r14.get(r15)     // Catch:{ all -> 0x00ea }
            if (r14 == 0) goto L_0x0060
            r14 = 1
            goto L_0x0061
        L_0x0060:
            r14 = 0
        L_0x0061:
            if (r14 == 0) goto L_0x0040
            r7.add(r11)     // Catch:{ all -> 0x00ea }
            goto L_0x0040
        L_0x0067:
            java.util.List r7 = (java.util.List) r7     // Catch:{ all -> 0x00ea }
            java.lang.Iterable r7 = (java.lang.Iterable) r7     // Catch:{ all -> 0x00ea }
            r5 = r7
            r6 = 0
            r7 = 10
            int r7 = kotlin.collections.CollectionsKt.collectionSizeOrDefault(r5, r7)     // Catch:{ all -> 0x00ea }
            int r7 = kotlin.collections.MapsKt.mapCapacity(r7)     // Catch:{ all -> 0x00ea }
            r8 = 16
            int r7 = kotlin.ranges.RangesKt.coerceAtLeast((int) r7, (int) r8)     // Catch:{ all -> 0x00ea }
            java.util.LinkedHashMap r8 = new java.util.LinkedHashMap     // Catch:{ all -> 0x00ea }
            r8.<init>(r7)     // Catch:{ all -> 0x00ea }
            java.util.Map r8 = (java.util.Map) r8     // Catch:{ all -> 0x00ea }
            r9 = r5
            r10 = 0
            java.util.Iterator r11 = r9.iterator()     // Catch:{ all -> 0x00ea }
        L_0x008c:
            boolean r12 = r11.hasNext()     // Catch:{ all -> 0x00ea }
            if (r12 == 0) goto L_0x00ae
            java.lang.Object r12 = r11.next()     // Catch:{ all -> 0x00ea }
            r13 = r12
            kotlinx.coroutines.debug.internal.DebugProbesImpl$CoroutineOwner r13 = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) r13     // Catch:{ all -> 0x00ea }
            r14 = 0
            kotlin.coroutines.Continuation<T> r15 = r13.delegate     // Catch:{ all -> 0x00ea }
            kotlin.coroutines.CoroutineContext r15 = r15.getContext()     // Catch:{ all -> 0x00ea }
            kotlinx.coroutines.Job r15 = kotlinx.coroutines.JobKt.getJob(r15)     // Catch:{ all -> 0x00ea }
            r13 = r12
            kotlinx.coroutines.debug.internal.DebugProbesImpl$CoroutineOwner r13 = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) r13     // Catch:{ all -> 0x00ea }
            r14 = 0
            kotlinx.coroutines.debug.internal.DebugCoroutineInfoImpl r3 = r13.info     // Catch:{ all -> 0x00ea }
            r8.put(r15, r3)     // Catch:{ all -> 0x00ea }
            goto L_0x008c
        L_0x00ae:
            r3 = r8
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ea }
            r5.<init>()     // Catch:{ all -> 0x00ea }
            r6 = r5
            r7 = 0
            kotlinx.coroutines.debug.internal.DebugProbesImpl r8 = INSTANCE     // Catch:{ all -> 0x00ea }
            java.lang.String r9 = ""
            r10 = r17
            r8.build(r10, r3, r6, r9)     // Catch:{ all -> 0x00e8 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x00e8 }
            java.lang.String r6 = "StringBuilder().apply(builderAction).toString()"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r5, r6)     // Catch:{ all -> 0x00e8 }
            r3 = 0
        L_0x00cd:
            if (r3 >= r2) goto L_0x00d5
            int r3 = r3 + 1
            r1.lock()
            goto L_0x00cd
        L_0x00d5:
            r4.unlock()
            return r5
        L_0x00d9:
            r10 = r17
            r3 = 0
            java.lang.String r5 = "Debug probes are not installed"
            java.lang.IllegalStateException r3 = new java.lang.IllegalStateException     // Catch:{ all -> 0x00e8 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x00e8 }
            r3.<init>(r5)     // Catch:{ all -> 0x00e8 }
            throw r3     // Catch:{ all -> 0x00e8 }
        L_0x00e8:
            r0 = move-exception
            goto L_0x00ed
        L_0x00ea:
            r0 = move-exception
            r10 = r17
        L_0x00ed:
            r3 = 0
        L_0x00ee:
            if (r3 >= r2) goto L_0x00f6
            int r3 = r3 + 1
            r1.lock()
            goto L_0x00ee
        L_0x00f6:
            r4.unlock()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.debug.internal.DebugProbesImpl.hierarchyToString(kotlinx.coroutines.Job):java.lang.String");
    }

    /*  JADX ERROR: StackOverflow in pass: MarkFinallyVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public final void install() {
        /*
            r8 = this;
            java.util.concurrent.locks.ReentrantReadWriteLock r0 = coroutineStateLock
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r1 = r0.readLock()
            int r2 = r0.getWriteHoldCount()
            r3 = 0
            if (r2 != 0) goto L_0x0012
            int r2 = r0.getReadHoldCount()
            goto L_0x0013
        L_0x0012:
            r2 = r3
        L_0x0013:
            r4 = r3
        L_0x0014:
            if (r4 >= r2) goto L_0x001c
            int r4 = r4 + 1
            r1.unlock()
            goto L_0x0014
        L_0x001c:
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r0 = r0.writeLock()
            r0.lock()
            r4 = 0
            kotlinx.coroutines.debug.internal.DebugProbesImpl r5 = INSTANCE     // Catch:{ all -> 0x0070 }
            int r6 = installations     // Catch:{ all -> 0x0070 }
            r7 = 1
            int r6 = r6 + r7
            installations = r6     // Catch:{ all -> 0x0070 }
            int r6 = installations     // Catch:{ all -> 0x0070 }
            if (r6 <= r7) goto L_0x003c
        L_0x0030:
            if (r3 >= r2) goto L_0x0038
            int r3 = r3 + 1
            r1.lock()
            goto L_0x0030
        L_0x0038:
            r0.unlock()
            return
        L_0x003c:
            r5.startWeakRefCleanerThread()     // Catch:{ all -> 0x0070 }
            kotlinx.coroutines.debug.internal.AgentInstallationType r5 = kotlinx.coroutines.debug.internal.AgentInstallationType.INSTANCE     // Catch:{ all -> 0x0070 }
            boolean r5 = r5.isInstalledStatically$kotlinx_coroutines_core()     // Catch:{ all -> 0x0070 }
            if (r5 == 0) goto L_0x0053
        L_0x0047:
            if (r3 >= r2) goto L_0x004f
            int r3 = r3 + 1
            r1.lock()
            goto L_0x0047
        L_0x004f:
            r0.unlock()
            return
        L_0x0053:
            kotlin.jvm.functions.Function1<java.lang.Boolean, kotlin.Unit> r5 = dynamicAttach     // Catch:{ all -> 0x0070 }
            if (r5 != 0) goto L_0x0058
        L_0x0057:
            goto L_0x0060
        L_0x0058:
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r7)     // Catch:{ all -> 0x0070 }
            r5.invoke(r6)     // Catch:{ all -> 0x0070 }
            goto L_0x0057
        L_0x0060:
            kotlin.Unit r4 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x0070 }
        L_0x0064:
            if (r3 >= r2) goto L_0x006c
            int r3 = r3 + 1
            r1.lock()
            goto L_0x0064
        L_0x006c:
            r0.unlock()
            return
        L_0x0070:
            r4 = move-exception
        L_0x0071:
            if (r3 >= r2) goto L_0x0079
            int r3 = r3 + 1
            r1.lock()
            goto L_0x0071
        L_0x0079:
            r0.unlock()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.debug.internal.DebugProbesImpl.install():void");
    }

    public final boolean isInstalled$kotlinx_coroutines_core() {
        return installations > 0;
    }

    public final <T> Continuation<T> probeCoroutineCreated$kotlinx_coroutines_core(Continuation<? super T> completion) {
        StackTraceFrame stackTraceFrame;
        if (!isInstalled$kotlinx_coroutines_core() || owner((Continuation<?>) completion) != null) {
            return completion;
        }
        if (enableCreationStackTraces) {
            stackTraceFrame = toStackTraceFrame(sanitizeStackTrace(new Exception()));
        } else {
            stackTraceFrame = null;
            StackTraceFrame stackTraceFrame2 = null;
        }
        return createOwner(completion, stackTraceFrame);
    }

    public final void probeCoroutineResumed$kotlinx_coroutines_core(Continuation<?> frame) {
        updateState(frame, DebugCoroutineInfoImplKt.RUNNING);
    }

    public final void probeCoroutineSuspended$kotlinx_coroutines_core(Continuation<?> frame) {
        updateState(frame, DebugCoroutineInfoImplKt.SUSPENDED);
    }

    public final void setEnableCreationStackTraces(boolean z) {
        enableCreationStackTraces = z;
    }

    public final void setSanitizeStackTraces(boolean z) {
        sanitizeStackTraces = z;
    }

    /*  JADX ERROR: StackOverflow in pass: MarkFinallyVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public final void uninstall() {
        /*
            r7 = this;
            java.util.concurrent.locks.ReentrantReadWriteLock r0 = coroutineStateLock
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r1 = r0.readLock()
            int r2 = r0.getWriteHoldCount()
            r3 = 0
            if (r2 != 0) goto L_0x0012
            int r2 = r0.getReadHoldCount()
            goto L_0x0013
        L_0x0012:
            r2 = r3
        L_0x0013:
            r4 = r3
        L_0x0014:
            if (r4 >= r2) goto L_0x001c
            int r4 = r4 + 1
            r1.unlock()
            goto L_0x0014
        L_0x001c:
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r0 = r0.writeLock()
            r0.lock()
            r4 = 0
            kotlinx.coroutines.debug.internal.DebugProbesImpl r5 = INSTANCE     // Catch:{ all -> 0x008d }
            boolean r6 = r5.isInstalled$kotlinx_coroutines_core()     // Catch:{ all -> 0x008d }
            if (r6 == 0) goto L_0x0080
            int r6 = installations     // Catch:{ all -> 0x008d }
            int r6 = r6 + -1
            installations = r6     // Catch:{ all -> 0x008d }
            int r6 = installations     // Catch:{ all -> 0x008d }
            if (r6 == 0) goto L_0x0042
        L_0x0036:
            if (r3 >= r2) goto L_0x003e
            int r3 = r3 + 1
            r1.lock()
            goto L_0x0036
        L_0x003e:
            r0.unlock()
            return
        L_0x0042:
            r5.stopWeakRefCleanerThread()     // Catch:{ all -> 0x008d }
            kotlinx.coroutines.debug.internal.ConcurrentWeakMap<kotlinx.coroutines.debug.internal.DebugProbesImpl$CoroutineOwner<?>, java.lang.Boolean> r5 = capturedCoroutinesMap     // Catch:{ all -> 0x008d }
            r5.clear()     // Catch:{ all -> 0x008d }
            kotlinx.coroutines.debug.internal.ConcurrentWeakMap<kotlin.coroutines.jvm.internal.CoroutineStackFrame, kotlinx.coroutines.debug.internal.DebugCoroutineInfoImpl> r5 = callerInfoCache     // Catch:{ all -> 0x008d }
            r5.clear()     // Catch:{ all -> 0x008d }
            kotlinx.coroutines.debug.internal.AgentInstallationType r5 = kotlinx.coroutines.debug.internal.AgentInstallationType.INSTANCE     // Catch:{ all -> 0x008d }
            boolean r5 = r5.isInstalledStatically$kotlinx_coroutines_core()     // Catch:{ all -> 0x008d }
            if (r5 == 0) goto L_0x0063
        L_0x0057:
            if (r3 >= r2) goto L_0x005f
            int r3 = r3 + 1
            r1.lock()
            goto L_0x0057
        L_0x005f:
            r0.unlock()
            return
        L_0x0063:
            kotlin.jvm.functions.Function1<java.lang.Boolean, kotlin.Unit> r5 = dynamicAttach     // Catch:{ all -> 0x008d }
            if (r5 != 0) goto L_0x0068
        L_0x0067:
            goto L_0x0070
        L_0x0068:
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r3)     // Catch:{ all -> 0x008d }
            r5.invoke(r6)     // Catch:{ all -> 0x008d }
            goto L_0x0067
        L_0x0070:
            kotlin.Unit r4 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x008d }
        L_0x0074:
            if (r3 >= r2) goto L_0x007c
            int r3 = r3 + 1
            r1.lock()
            goto L_0x0074
        L_0x007c:
            r0.unlock()
            return
        L_0x0080:
            r5 = 0
            java.lang.String r6 = "Agent was not installed"
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException     // Catch:{ all -> 0x008d }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x008d }
            r5.<init>(r6)     // Catch:{ all -> 0x008d }
            throw r5     // Catch:{ all -> 0x008d }
        L_0x008d:
            r4 = move-exception
        L_0x008e:
            if (r3 >= r2) goto L_0x0096
            int r3 = r3 + 1
            r1.lock()
            goto L_0x008e
        L_0x0096:
            r0.unlock()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.debug.internal.DebugProbesImpl.uninstall():void");
    }

    /* JADX INFO: finally extract failed */
    private final void dumpCoroutinesSynchronized(PrintStream out) {
        String str;
        PrintStream printStream = out;
        ReentrantReadWriteLock reentrantReadWriteLock = coroutineStateLock;
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        int readHoldCount = reentrantReadWriteLock.getWriteHoldCount() == 0 ? reentrantReadWriteLock.getReadHoldCount() : 0;
        int i = 0;
        while (i < readHoldCount) {
            i++;
            readLock.unlock();
        }
        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
        writeLock.lock();
        boolean z = false;
        try {
            DebugProbesImpl debugProbesImpl = INSTANCE;
            if (debugProbesImpl.isInstalled$kotlinx_coroutines_core()) {
                String stringPlus = Intrinsics.stringPlus("Coroutines dump ", dateFormat.format(Long.valueOf(System.currentTimeMillis())));
                Log1F380D.a((Object) stringPlus);
                printStream.print(stringPlus);
                Sequence<CoroutineOwner> sortedWith = SequencesKt.sortedWith(SequencesKt.filter(CollectionsKt.asSequence(debugProbesImpl.getCapturedCoroutines()), DebugProbesImpl$dumpCoroutinesSynchronized$1$2.INSTANCE), new DebugProbesImpl$dumpCoroutinesSynchronized$lambda19$$inlined$sortedBy$1());
                for (CoroutineOwner coroutineOwner : sortedWith) {
                    DebugCoroutineInfoImpl debugCoroutineInfoImpl = coroutineOwner.info;
                    List<StackTraceElement> lastObservedStackTrace = debugCoroutineInfoImpl.lastObservedStackTrace();
                    DebugProbesImpl debugProbesImpl2 = INSTANCE;
                    List<StackTraceElement> enhanceStackTraceWithThreadDumpImpl = debugProbesImpl2.enhanceStackTraceWithThreadDumpImpl(debugCoroutineInfoImpl.getState(), debugCoroutineInfoImpl.lastObservedThread, lastObservedStackTrace);
                    boolean z2 = z;
                    if (!Intrinsics.areEqual((Object) debugCoroutineInfoImpl.getState(), (Object) DebugCoroutineInfoImplKt.RUNNING) || enhanceStackTraceWithThreadDumpImpl != lastObservedStackTrace) {
                        str = debugCoroutineInfoImpl.getState();
                    } else {
                        str = Intrinsics.stringPlus(debugCoroutineInfoImpl.getState(), " (Last suspension stacktrace, not an actual stacktrace)");
                        Log1F380D.a((Object) str);
                    }
                    Sequence sequence = sortedWith;
                    printStream.print("\n\nCoroutine " + coroutineOwner.delegate + ", state: " + str);
                    if (lastObservedStackTrace.isEmpty()) {
                        String stringPlus2 = Intrinsics.stringPlus("\n\tat ", StackTraceRecoveryKt.artificialFrame(ARTIFICIAL_FRAME_MESSAGE));
                        Log1F380D.a((Object) stringPlus2);
                        printStream.print(stringPlus2);
                        debugProbesImpl2.printStackTrace(printStream, debugCoroutineInfoImpl.getCreationStackTrace());
                    } else {
                        debugProbesImpl2.printStackTrace(printStream, enhanceStackTraceWithThreadDumpImpl);
                    }
                    z = z2;
                    sortedWith = sequence;
                }
                boolean z3 = z;
                Sequence sequence2 = sortedWith;
                Unit unit = Unit.INSTANCE;
                int i2 = 0;
                while (i2 < readHoldCount) {
                    i2++;
                    readLock.lock();
                }
                writeLock.unlock();
                return;
            }
            throw new IllegalStateException("Debug probes are not installed".toString());
        } catch (Throwable th) {
            int i3 = 0;
            while (i3 < readHoldCount) {
                i3++;
                readLock.lock();
            }
            writeLock.unlock();
            throw th;
        }
    }

    private final void printStackTrace(PrintStream out, List<StackTraceElement> frames) {
        for (StackTraceElement stringPlus : frames) {
            String stringPlus2 = Intrinsics.stringPlus("\n\tat ", stringPlus);
            Log1F380D.a((Object) stringPlus2);
            out.print(stringPlus2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x003c, code lost:
        r9 = r9.getName();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object[] dumpCoroutinesInfoAsJsonAndReferences() {
        /*
            r17 = this;
            r0 = r17
            java.util.List r1 = r17.dumpCoroutinesInfo()
            int r2 = r1.size()
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>(r2)
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>(r2)
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>(r2)
            java.util.Iterator r6 = r1.iterator()
        L_0x001d:
            boolean r7 = r6.hasNext()
            if (r7 == 0) goto L_0x00d9
            java.lang.Object r7 = r6.next()
            kotlinx.coroutines.debug.internal.DebugCoroutineInfo r7 = (kotlinx.coroutines.debug.internal.DebugCoroutineInfo) r7
            kotlin.coroutines.CoroutineContext r8 = r7.getContext()
            kotlinx.coroutines.CoroutineName$Key r9 = kotlinx.coroutines.CoroutineName.Key
            kotlin.coroutines.CoroutineContext$Key r9 = (kotlin.coroutines.CoroutineContext.Key) r9
            kotlin.coroutines.CoroutineContext$Element r9 = r8.get(r9)
            kotlinx.coroutines.CoroutineName r9 = (kotlinx.coroutines.CoroutineName) r9
            r10 = 0
            if (r9 != 0) goto L_0x003c
        L_0x003a:
            r9 = r10
            goto L_0x0047
        L_0x003c:
            java.lang.String r9 = r9.getName()
            if (r9 != 0) goto L_0x0043
            goto L_0x003a
        L_0x0043:
            java.lang.String r9 = r0.toStringWithQuotes(r9)
        L_0x0047:
            kotlinx.coroutines.CoroutineDispatcher$Key r11 = kotlinx.coroutines.CoroutineDispatcher.Key
            kotlin.coroutines.CoroutineContext$Key r11 = (kotlin.coroutines.CoroutineContext.Key) r11
            kotlin.coroutines.CoroutineContext$Element r11 = r8.get(r11)
            kotlinx.coroutines.CoroutineDispatcher r11 = (kotlinx.coroutines.CoroutineDispatcher) r11
            if (r11 != 0) goto L_0x0055
            r11 = r10
            goto L_0x0059
        L_0x0055:
            java.lang.String r11 = r0.toStringWithQuotes(r11)
        L_0x0059:
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "\n                {\n                    \"name\": "
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.StringBuilder r12 = r12.append(r9)
            java.lang.String r13 = ",\n                    \"id\": "
            java.lang.StringBuilder r12 = r12.append(r13)
            kotlinx.coroutines.CoroutineId$Key r13 = kotlinx.coroutines.CoroutineId.Key
            kotlin.coroutines.CoroutineContext$Key r13 = (kotlin.coroutines.CoroutineContext.Key) r13
            kotlin.coroutines.CoroutineContext$Element r13 = r8.get(r13)
            kotlinx.coroutines.CoroutineId r13 = (kotlinx.coroutines.CoroutineId) r13
            if (r13 != 0) goto L_0x007e
            goto L_0x0086
        L_0x007e:
            long r13 = r13.getId()
            java.lang.Long r10 = java.lang.Long.valueOf(r13)
        L_0x0086:
            java.lang.StringBuilder r10 = r12.append(r10)
            java.lang.String r12 = ",\n                    \"dispatcher\": "
            java.lang.StringBuilder r10 = r10.append(r12)
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r12 = ",\n                    \"sequenceNumber\": "
            java.lang.StringBuilder r10 = r10.append(r12)
            long r12 = r7.getSequenceNumber()
            java.lang.StringBuilder r10 = r10.append(r12)
            java.lang.String r12 = ",\n                    \"state\": \""
            java.lang.StringBuilder r10 = r10.append(r12)
            java.lang.String r12 = r7.getState()
            java.lang.StringBuilder r10 = r10.append(r12)
            java.lang.String r12 = "\"\n                } \n                "
            java.lang.StringBuilder r10 = r10.append(r12)
            java.lang.String r10 = r10.toString()
            java.lang.String r10 = kotlin.text.StringsKt.trimIndent(r10)
            mt.Log1F380D.a((java.lang.Object) r10)
            r5.add(r10)
            kotlin.coroutines.jvm.internal.CoroutineStackFrame r10 = r7.getLastObservedFrame()
            r4.add(r10)
            java.lang.Thread r10 = r7.getLastObservedThread()
            r3.add(r10)
            goto L_0x001d
        L_0x00d9:
            r6 = 4
            java.lang.Object[] r6 = new java.lang.Object[r6]
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            r8 = 91
            java.lang.StringBuilder r7 = r7.append(r8)
            r8 = r5
            java.lang.Iterable r8 = (java.lang.Iterable) r8
            r9 = 0
            r10 = 0
            r11 = 0
            r12 = 0
            r13 = 0
            r14 = 0
            r15 = 63
            r16 = 0
            java.lang.String r8 = kotlin.collections.CollectionsKt.joinToString$default(r8, r9, r10, r11, r12, r13, r14, r15, r16)
            mt.Log1F380D.a((java.lang.Object) r8)
            java.lang.StringBuilder r7 = r7.append(r8)
            r8 = 93
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r7 = r7.toString()
            r8 = 0
            r6[r8] = r7
            r7 = 1
            r9 = r3
            java.util.Collection r9 = (java.util.Collection) r9
            r10 = 0
            r11 = r9
            java.lang.Thread[] r12 = new java.lang.Thread[r8]
            java.lang.Object[] r12 = r11.toArray(r12)
            java.lang.String r13 = "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>"
            if (r12 == 0) goto L_0x014f
            r6[r7] = r12
            r7 = 2
            r9 = r4
            java.util.Collection r9 = (java.util.Collection) r9
            r10 = 0
            r11 = r9
            kotlin.coroutines.jvm.internal.CoroutineStackFrame[] r12 = new kotlin.coroutines.jvm.internal.CoroutineStackFrame[r8]
            java.lang.Object[] r12 = r11.toArray(r12)
            if (r12 == 0) goto L_0x0149
            r6[r7] = r12
            r7 = 3
            r9 = r1
            java.util.Collection r9 = (java.util.Collection) r9
            r10 = 0
            r11 = r9
            kotlinx.coroutines.debug.internal.DebugCoroutineInfo[] r8 = new kotlinx.coroutines.debug.internal.DebugCoroutineInfo[r8]
            java.lang.Object[] r8 = r11.toArray(r8)
            if (r8 == 0) goto L_0x0143
            r6[r7] = r8
            return r6
        L_0x0143:
            java.lang.NullPointerException r6 = new java.lang.NullPointerException
            r6.<init>(r13)
            throw r6
        L_0x0149:
            java.lang.NullPointerException r6 = new java.lang.NullPointerException
            r6.<init>(r13)
            throw r6
        L_0x014f:
            java.lang.NullPointerException r6 = new java.lang.NullPointerException
            r6.<init>(r13)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.debug.internal.DebugProbesImpl.dumpCoroutinesInfoAsJsonAndReferences():java.lang.Object[]");
    }

    public final String enhanceStackTraceWithThreadDumpAsJson(DebugCoroutineInfo info) {
        List<StackTraceElement> enhanceStackTraceWithThreadDump = enhanceStackTraceWithThreadDump(info, info.lastObservedStackTrace());
        List arrayList = new ArrayList();
        for (StackTraceElement next : enhanceStackTraceWithThreadDump) {
            StringBuilder append = new StringBuilder().append("\n                {\n                    \"declaringClass\": \"").append(next.getClassName()).append("\",\n                    \"methodName\": \"").append(next.getMethodName()).append("\",\n                    \"fileName\": ");
            String fileName = next.getFileName();
            String trimIndent = StringsKt.trimIndent(append.append(fileName == null ? null : toStringWithQuotes(fileName)).append(",\n                    \"lineNumber\": ").append(next.getLineNumber()).append("\n                }\n                ").toString());
            Log1F380D.a((Object) trimIndent);
            arrayList.add(trimIndent);
        }
        StringBuilder append2 = new StringBuilder().append('[');
        String joinToString$default = CollectionsKt.joinToString$default(arrayList, (CharSequence) null, (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 63, (Object) null);
        Log1F380D.a((Object) joinToString$default);
        return append2.append(joinToString$default).append(']').toString();
    }
}

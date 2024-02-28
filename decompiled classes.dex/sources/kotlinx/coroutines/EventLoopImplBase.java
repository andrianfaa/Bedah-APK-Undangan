package kotlinx.coroutines;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.time.DurationKt;
import kotlinx.coroutines.Delay;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import kotlinx.coroutines.internal.ThreadSafeHeap;
import kotlinx.coroutines.internal.ThreadSafeHeapNode;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\u0018\u0002\b \u0018\u00002\u0002092\u00020::\u00044567B\u0007¢\u0006\u0004\b\u0001\u0010\u0002J\u000f\u0010\u0004\u001a\u00020\u0003H\u0002¢\u0006\u0004\b\u0004\u0010\u0002J\u0017\u0010\u0007\u001a\n\u0018\u00010\u0005j\u0004\u0018\u0001`\u0006H\u0002¢\u0006\u0004\b\u0007\u0010\bJ!\u0010\f\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\t2\n\u0010\u000b\u001a\u00060\u0005j\u0002`\u0006¢\u0006\u0004\b\f\u0010\rJ\u001b\u0010\u000f\u001a\u00020\u00032\n\u0010\u000e\u001a\u00060\u0005j\u0002`\u0006H\u0016¢\u0006\u0004\b\u000f\u0010\u0010J\u001b\u0010\u0012\u001a\u00020\u00112\n\u0010\u000e\u001a\u00060\u0005j\u0002`\u0006H\u0002¢\u0006\u0004\b\u0012\u0010\u0013J\u000f\u0010\u0015\u001a\u00020\u0014H\u0016¢\u0006\u0004\b\u0015\u0010\u0016J\u000f\u0010\u0017\u001a\u00020\u0003H\u0002¢\u0006\u0004\b\u0017\u0010\u0002J\u000f\u0010\u0018\u001a\u00020\u0003H\u0004¢\u0006\u0004\b\u0018\u0010\u0002J\u001d\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u001a¢\u0006\u0004\b\u001c\u0010\u001dJ\u001f\u0010\u001f\u001a\u00020\u001e2\u0006\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u001aH\u0002¢\u0006\u0004\b\u001f\u0010 J#\u0010#\u001a\u00020\"2\u0006\u0010!\u001a\u00020\u00142\n\u0010\u000b\u001a\u00060\u0005j\u0002`\u0006H\u0004¢\u0006\u0004\b#\u0010$J%\u0010'\u001a\u00020\u00032\u0006\u0010!\u001a\u00020\u00142\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00030%H\u0016¢\u0006\u0004\b'\u0010(J\u0017\u0010)\u001a\u00020\u00112\u0006\u0010\u000e\u001a\u00020\u001aH\u0002¢\u0006\u0004\b)\u0010*J\u000f\u0010+\u001a\u00020\u0003H\u0016¢\u0006\u0004\b+\u0010\u0002R$\u0010-\u001a\u00020\u00112\u0006\u0010,\u001a\u00020\u00118B@BX\u000e¢\u0006\f\u001a\u0004\b-\u0010.\"\u0004\b/\u00100R\u0014\u00101\u001a\u00020\u00118TX\u0004¢\u0006\u0006\u001a\u0004\b1\u0010.R\u0014\u00103\u001a\u00020\u00148TX\u0004¢\u0006\u0006\u001a\u0004\b2\u0010\u0016¨\u00068"}, d2 = {"Lkotlinx/coroutines/EventLoopImplBase;", "<init>", "()V", "", "closeQueue", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "dequeue", "()Ljava/lang/Runnable;", "Lkotlin/coroutines/CoroutineContext;", "context", "block", "dispatch", "(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Runnable;)V", "task", "enqueue", "(Ljava/lang/Runnable;)V", "", "enqueueImpl", "(Ljava/lang/Runnable;)Z", "", "processNextEvent", "()J", "rescheduleAllDelayed", "resetAll", "now", "Lkotlinx/coroutines/EventLoopImplBase$DelayedTask;", "delayedTask", "schedule", "(JLkotlinx/coroutines/EventLoopImplBase$DelayedTask;)V", "", "scheduleImpl", "(JLkotlinx/coroutines/EventLoopImplBase$DelayedTask;)I", "timeMillis", "Lkotlinx/coroutines/DisposableHandle;", "scheduleInvokeOnTimeout", "(JLjava/lang/Runnable;)Lkotlinx/coroutines/DisposableHandle;", "Lkotlinx/coroutines/CancellableContinuation;", "continuation", "scheduleResumeAfterDelay", "(JLkotlinx/coroutines/CancellableContinuation;)V", "shouldUnpark", "(Lkotlinx/coroutines/EventLoopImplBase$DelayedTask;)Z", "shutdown", "value", "isCompleted", "()Z", "setCompleted", "(Z)V", "isEmpty", "getNextTime", "nextTime", "DelayedResumeTask", "DelayedRunnableTask", "DelayedTask", "DelayedTaskQueue", "kotlinx-coroutines-core", "Lkotlinx/coroutines/EventLoopImplPlatform;", "Lkotlinx/coroutines/Delay;"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: EventLoop.common.kt */
public abstract class EventLoopImplBase extends EventLoopImplPlatform implements Delay {
    private static final /* synthetic */ AtomicReferenceFieldUpdater _delayed$FU;
    private static final /* synthetic */ AtomicReferenceFieldUpdater _queue$FU;
    private volatile /* synthetic */ Object _delayed = null;
    private volatile /* synthetic */ int _isCompleted = 0;
    private volatile /* synthetic */ Object _queue = null;

    @Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\b\u0010\b\u001a\u00020\u0006H\u0016J\b\u0010\t\u001a\u00020\nH\u0016R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lkotlinx/coroutines/EventLoopImplBase$DelayedResumeTask;", "Lkotlinx/coroutines/EventLoopImplBase$DelayedTask;", "nanoTime", "", "cont", "Lkotlinx/coroutines/CancellableContinuation;", "", "(Lkotlinx/coroutines/EventLoopImplBase;JLkotlinx/coroutines/CancellableContinuation;)V", "run", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: 0175 */
    private final class DelayedResumeTask extends DelayedTask {
        private final CancellableContinuation<Unit> cont;

        public DelayedResumeTask(long nanoTime, CancellableContinuation<? super Unit> cont2) {
            super(nanoTime);
            this.cont = cont2;
        }

        public void run() {
            this.cont.resumeUndispatched(EventLoopImplBase.this, Unit.INSTANCE);
        }

        public String toString() {
            String stringPlus = Intrinsics.stringPlus(super.toString(), this.cont);
            Log1F380D.a((Object) stringPlus);
            return stringPlus;
        }
    }

    @Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\u0010\u0004\u001a\u00060\u0005j\u0002`\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\b\u001a\u00020\tH\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016R\u0012\u0010\u0004\u001a\u00060\u0005j\u0002`\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/EventLoopImplBase$DelayedRunnableTask;", "Lkotlinx/coroutines/EventLoopImplBase$DelayedTask;", "nanoTime", "", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "(JLjava/lang/Runnable;)V", "run", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: 0176 */
    private static final class DelayedRunnableTask extends DelayedTask {
        private final Runnable block;

        public DelayedRunnableTask(long nanoTime, Runnable block2) {
            super(nanoTime);
            this.block = block2;
        }

        public void run() {
            this.block.run();
        }

        public String toString() {
            String stringPlus = Intrinsics.stringPlus(super.toString(), this.block);
            Log1F380D.a((Object) stringPlus);
            return stringPlus;
        }
    }

    @Metadata(d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\b \u0018\u00002\u00060\u0001j\u0002`\u00022\b\u0012\u0004\u0012\u00020\u00000\u00032\u00020\u00042\u00020\u0005B\r\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0011\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0000H\u0002J\u0006\u0010\u001a\u001a\u00020\u001bJ\u001e\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u00072\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!J\u000e\u0010\"\u001a\u00020#2\u0006\u0010\u001d\u001a\u00020\u0007J\b\u0010$\u001a\u00020%H\u0016R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u0002\n\u0000R0\u0010\r\u001a\b\u0012\u0002\b\u0003\u0018\u00010\f2\f\u0010\u000b\u001a\b\u0012\u0002\b\u0003\u0018\u00010\f8V@VX\u000e¢\u0006\f\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u0012\u0010\u0006\u001a\u00020\u00078\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006&"}, d2 = {"Lkotlinx/coroutines/EventLoopImplBase$DelayedTask;", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "", "Lkotlinx/coroutines/DisposableHandle;", "Lkotlinx/coroutines/internal/ThreadSafeHeapNode;", "nanoTime", "", "(J)V", "_heap", "", "value", "Lkotlinx/coroutines/internal/ThreadSafeHeap;", "heap", "getHeap", "()Lkotlinx/coroutines/internal/ThreadSafeHeap;", "setHeap", "(Lkotlinx/coroutines/internal/ThreadSafeHeap;)V", "index", "", "getIndex", "()I", "setIndex", "(I)V", "compareTo", "other", "dispose", "", "scheduleTask", "now", "delayed", "Lkotlinx/coroutines/EventLoopImplBase$DelayedTaskQueue;", "eventLoop", "Lkotlinx/coroutines/EventLoopImplBase;", "timeToExecute", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: EventLoop.common.kt */
    public static abstract class DelayedTask implements Runnable, Comparable<DelayedTask>, DisposableHandle, ThreadSafeHeapNode {
        private Object _heap;
        private int index = -1;
        public long nanoTime;

        public DelayedTask(long nanoTime2) {
            this.nanoTime = nanoTime2;
        }

        public int compareTo(DelayedTask other) {
            long j = this.nanoTime - other.nanoTime;
            if (j > 0) {
                return 1;
            }
            return j < 0 ? -1 : 0;
        }

        public final synchronized void dispose() {
            Object obj = this._heap;
            if (obj != EventLoop_commonKt.DISPOSED_TASK) {
                DelayedTaskQueue delayedTaskQueue = obj instanceof DelayedTaskQueue ? (DelayedTaskQueue) obj : null;
                if (delayedTaskQueue != null) {
                    delayedTaskQueue.remove(this);
                }
                this._heap = EventLoop_commonKt.DISPOSED_TASK;
            }
        }

        public ThreadSafeHeap<?> getHeap() {
            Object obj = this._heap;
            if (obj instanceof ThreadSafeHeap) {
                return (ThreadSafeHeap) obj;
            }
            return null;
        }

        public int getIndex() {
            return this.index;
        }

        public final synchronized int scheduleTask(long now, DelayedTaskQueue delayed, EventLoopImplBase eventLoop) {
            long j = now;
            DelayedTaskQueue delayedTaskQueue = delayed;
            synchronized (this) {
                if (this._heap == EventLoop_commonKt.DISPOSED_TASK) {
                    return 2;
                }
                ThreadSafeHeap threadSafeHeap = delayedTaskQueue;
                synchronized (threadSafeHeap) {
                    DelayedTask delayedTask = (DelayedTask) threadSafeHeap.firstImpl();
                    if (eventLoop.isCompleted()) {
                        return 1;
                    }
                    if (delayedTask == null) {
                        delayedTaskQueue.timeNow = j;
                    } else {
                        long j2 = delayedTask.nanoTime;
                        long j3 = j2 - j >= 0 ? j : j2;
                        if (j3 - delayedTaskQueue.timeNow > 0) {
                            delayedTaskQueue.timeNow = j3;
                        }
                    }
                    if (this.nanoTime - delayedTaskQueue.timeNow < 0) {
                        this.nanoTime = delayedTaskQueue.timeNow;
                    }
                    threadSafeHeap.addImpl(this);
                    return 0;
                }
            }
        }

        public void setHeap(ThreadSafeHeap<?> value) {
            if (this._heap != EventLoop_commonKt.DISPOSED_TASK) {
                this._heap = value;
                return;
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }

        public void setIndex(int i) {
            this.index = i;
        }

        public final boolean timeToExecute(long now) {
            return now - this.nanoTime >= 0;
        }

        public String toString() {
            return "Delayed[nanos=" + this.nanoTime + ']';
        }
    }

    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\b\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005R\u0012\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lkotlinx/coroutines/EventLoopImplBase$DelayedTaskQueue;", "Lkotlinx/coroutines/internal/ThreadSafeHeap;", "Lkotlinx/coroutines/EventLoopImplBase$DelayedTask;", "timeNow", "", "(J)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: EventLoop.common.kt */
    public static final class DelayedTaskQueue extends ThreadSafeHeap<DelayedTask> {
        public long timeNow;

        public DelayedTaskQueue(long timeNow2) {
            this.timeNow = timeNow2;
        }
    }

    static {
        Class<EventLoopImplBase> cls = EventLoopImplBase.class;
        _queue$FU = AtomicReferenceFieldUpdater.newUpdater(cls, Object.class, "_queue");
        _delayed$FU = AtomicReferenceFieldUpdater.newUpdater(cls, Object.class, "_delayed");
    }

    private final void closeQueue() {
        if (!DebugKt.getASSERTIONS_ENABLED() || isCompleted()) {
            while (true) {
                Object obj = this._queue;
                if (obj == null) {
                    if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_queue$FU, this, (Object) null, EventLoop_commonKt.CLOSED_EMPTY)) {
                        return;
                    }
                } else if (obj instanceof LockFreeTaskQueueCore) {
                    ((LockFreeTaskQueueCore) obj).close();
                    return;
                } else if (obj != EventLoop_commonKt.CLOSED_EMPTY) {
                    LockFreeTaskQueueCore lockFreeTaskQueueCore = new LockFreeTaskQueueCore(8, true);
                    if (obj != null) {
                        lockFreeTaskQueueCore.addLast((Runnable) obj);
                        if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_queue$FU, this, obj, lockFreeTaskQueueCore)) {
                            return;
                        }
                    } else {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.Runnable{ kotlinx.coroutines.RunnableKt.Runnable }");
                    }
                } else {
                    return;
                }
            }
        } else {
            throw new AssertionError();
        }
    }

    private final Runnable dequeue() {
        while (true) {
            Object obj = this._queue;
            if (obj == null) {
                return null;
            }
            if (obj instanceof LockFreeTaskQueueCore) {
                if (obj != null) {
                    Object removeFirstOrNull = ((LockFreeTaskQueueCore) obj).removeFirstOrNull();
                    if (removeFirstOrNull != LockFreeTaskQueueCore.REMOVE_FROZEN) {
                        return (Runnable) removeFirstOrNull;
                    }
                    AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_queue$FU, this, obj, ((LockFreeTaskQueueCore) obj).next());
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type kotlinx.coroutines.internal.LockFreeTaskQueueCore<java.lang.Runnable{ kotlinx.coroutines.RunnableKt.Runnable }>{ kotlinx.coroutines.EventLoop_commonKt.Queue<java.lang.Runnable{ kotlinx.coroutines.RunnableKt.Runnable }> }");
                }
            } else if (obj == EventLoop_commonKt.CLOSED_EMPTY) {
                return null;
            } else {
                if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_queue$FU, this, obj, (Object) null)) {
                    if (obj != null) {
                        return (Runnable) obj;
                    }
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.Runnable{ kotlinx.coroutines.RunnableKt.Runnable }");
                }
            }
        }
    }

    private final boolean enqueueImpl(Runnable task) {
        while (true) {
            Object obj = this._queue;
            if (isCompleted()) {
                return false;
            }
            if (obj == null) {
                if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_queue$FU, this, (Object) null, task)) {
                    return true;
                }
            } else if (obj instanceof LockFreeTaskQueueCore) {
                if (obj != null) {
                    switch (((LockFreeTaskQueueCore) obj).addLast(task)) {
                        case 0:
                            return true;
                        case 1:
                            AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_queue$FU, this, obj, ((LockFreeTaskQueueCore) obj).next());
                            break;
                        case 2:
                            return false;
                    }
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type kotlinx.coroutines.internal.LockFreeTaskQueueCore<java.lang.Runnable{ kotlinx.coroutines.RunnableKt.Runnable }>{ kotlinx.coroutines.EventLoop_commonKt.Queue<java.lang.Runnable{ kotlinx.coroutines.RunnableKt.Runnable }> }");
                }
            } else if (obj == EventLoop_commonKt.CLOSED_EMPTY) {
                return false;
            } else {
                LockFreeTaskQueueCore lockFreeTaskQueueCore = new LockFreeTaskQueueCore(8, true);
                if (obj != null) {
                    lockFreeTaskQueueCore.addLast((Runnable) obj);
                    lockFreeTaskQueueCore.addLast(task);
                    if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_queue$FU, this, obj, lockFreeTaskQueueCore)) {
                        return true;
                    }
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.Runnable{ kotlinx.coroutines.RunnableKt.Runnable }");
                }
            }
        }
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [int, boolean] */
    /* access modifiers changed from: private */
    public final boolean isCompleted() {
        return this._isCompleted;
    }

    private final void rescheduleAllDelayed() {
        AbstractTimeSource timeSource = AbstractTimeSourceKt.getTimeSource();
        Long valueOf = timeSource == null ? null : Long.valueOf(timeSource.nanoTime());
        long nanoTime = valueOf == null ? System.nanoTime() : valueOf.longValue();
        while (true) {
            DelayedTaskQueue delayedTaskQueue = (DelayedTaskQueue) this._delayed;
            DelayedTask delayedTask = delayedTaskQueue == null ? null : (DelayedTask) delayedTaskQueue.removeFirstOrNull();
            if (delayedTask != null) {
                reschedule(nanoTime, delayedTask);
            } else {
                return;
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: kotlinx.coroutines.EventLoopImplBase$DelayedTaskQueue} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int scheduleImpl(long r6, kotlinx.coroutines.EventLoopImplBase.DelayedTask r8) {
        /*
            r5 = this;
            boolean r0 = r5.isCompleted()
            if (r0 == 0) goto L_0x0008
            r0 = 1
            return r0
        L_0x0008:
            java.lang.Object r0 = r5._delayed
            kotlinx.coroutines.EventLoopImplBase$DelayedTaskQueue r0 = (kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue) r0
            if (r0 != 0) goto L_0x0026
            r0 = r5
            kotlinx.coroutines.EventLoopImplBase r0 = (kotlinx.coroutines.EventLoopImplBase) r0
            r1 = 0
            java.util.concurrent.atomic.AtomicReferenceFieldUpdater r2 = _delayed$FU
            r3 = 0
            kotlinx.coroutines.EventLoopImplBase$DelayedTaskQueue r4 = new kotlinx.coroutines.EventLoopImplBase$DelayedTaskQueue
            r4.<init>(r6)
            androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(r2, r0, r3, r4)
            java.lang.Object r2 = r0._delayed
            kotlin.jvm.internal.Intrinsics.checkNotNull(r2)
            r0 = r2
            kotlinx.coroutines.EventLoopImplBase$DelayedTaskQueue r0 = (kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue) r0
        L_0x0026:
            int r1 = r8.scheduleTask(r6, r0, r5)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.EventLoopImplBase.scheduleImpl(long, kotlinx.coroutines.EventLoopImplBase$DelayedTask):int");
    }

    private final void setCompleted(boolean value) {
        this._isCompleted = value;
    }

    private final boolean shouldUnpark(DelayedTask task) {
        DelayedTaskQueue delayedTaskQueue = (DelayedTaskQueue) this._delayed;
        return (delayedTaskQueue == null ? null : (DelayedTask) delayedTaskQueue.peek()) == task;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Deprecated without replacement as an internal method never intended for public use")
    public Object delay(long time, Continuation<? super Unit> $completion) {
        return Delay.DefaultImpls.delay(this, time, $completion);
    }

    public final void dispatch(CoroutineContext context, Runnable block) {
        enqueue(block);
    }

    public void enqueue(Runnable task) {
        if (enqueueImpl(task)) {
            unpark();
        } else {
            DefaultExecutor.INSTANCE.enqueue(task);
        }
    }

    /* access modifiers changed from: protected */
    public long getNextTime() {
        if (super.getNextTime() == 0) {
            return 0;
        }
        Object obj = this._queue;
        if (obj != null) {
            if (!(obj instanceof LockFreeTaskQueueCore)) {
                return obj == EventLoop_commonKt.CLOSED_EMPTY ? Long.MAX_VALUE : 0;
            }
            if (!((LockFreeTaskQueueCore) obj).isEmpty()) {
                return 0;
            }
        }
        DelayedTaskQueue delayedTaskQueue = (DelayedTaskQueue) this._delayed;
        Long l = null;
        DelayedTask delayedTask = delayedTaskQueue == null ? null : (DelayedTask) delayedTaskQueue.peek();
        if (delayedTask == null) {
            return Long.MAX_VALUE;
        }
        long j = delayedTask.nanoTime;
        AbstractTimeSource timeSource = AbstractTimeSourceKt.getTimeSource();
        if (timeSource != null) {
            l = Long.valueOf(timeSource.nanoTime());
        }
        return RangesKt.coerceAtLeast(j - (l == null ? System.nanoTime() : l.longValue()), 0);
    }

    public DisposableHandle invokeOnTimeout(long timeMillis, Runnable block, CoroutineContext context) {
        return Delay.DefaultImpls.invokeOnTimeout(this, timeMillis, block, context);
    }

    /* access modifiers changed from: protected */
    public boolean isEmpty() {
        if (!isUnconfinedQueueEmpty()) {
            return false;
        }
        DelayedTaskQueue delayedTaskQueue = (DelayedTaskQueue) this._delayed;
        if (delayedTaskQueue != null && !delayedTaskQueue.isEmpty()) {
            return false;
        }
        Object obj = this._queue;
        if (obj == null) {
            return true;
        }
        return obj instanceof LockFreeTaskQueueCore ? ((LockFreeTaskQueueCore) obj).isEmpty() : obj == EventLoop_commonKt.CLOSED_EMPTY;
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x0074  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x007a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long processNextEvent() {
        /*
            r15 = this;
            boolean r0 = r15.processUnconfinedEvent()
            r1 = 0
            if (r0 == 0) goto L_0x0009
            return r1
        L_0x0009:
            java.lang.Object r0 = r15._delayed
            kotlinx.coroutines.EventLoopImplBase$DelayedTaskQueue r0 = (kotlinx.coroutines.EventLoopImplBase.DelayedTaskQueue) r0
            if (r0 == 0) goto L_0x006e
            boolean r3 = r0.isEmpty()
            if (r3 != 0) goto L_0x006e
            kotlinx.coroutines.AbstractTimeSource r3 = kotlinx.coroutines.AbstractTimeSourceKt.getTimeSource()
            r4 = 0
            if (r3 != 0) goto L_0x001e
            r3 = r4
            goto L_0x0026
        L_0x001e:
            long r5 = r3.nanoTime()
            java.lang.Long r3 = java.lang.Long.valueOf(r5)
        L_0x0026:
            if (r3 != 0) goto L_0x002d
            long r5 = java.lang.System.nanoTime()
            goto L_0x0031
        L_0x002d:
            long r5 = r3.longValue()
        L_0x0031:
            r3 = r0
            kotlinx.coroutines.internal.ThreadSafeHeap r3 = (kotlinx.coroutines.internal.ThreadSafeHeap) r3
            r7 = 0
            r8 = 0
            monitor-enter(r3)
            r9 = 0
            kotlinx.coroutines.internal.ThreadSafeHeapNode r10 = r3.firstImpl()     // Catch:{ all -> 0x006b }
            if (r10 != 0) goto L_0x0042
            monitor-exit(r3)
            r11 = r4
            goto L_0x0066
        L_0x0042:
            r11 = r10
            kotlinx.coroutines.EventLoopImplBase$DelayedTask r11 = (kotlinx.coroutines.EventLoopImplBase.DelayedTask) r11     // Catch:{ all -> 0x006b }
            r12 = 0
            boolean r13 = r11.timeToExecute(r5)     // Catch:{ all -> 0x006b }
            r14 = 0
            if (r13 == 0) goto L_0x0055
            r13 = r11
            java.lang.Runnable r13 = (java.lang.Runnable) r13     // Catch:{ all -> 0x006b }
            boolean r13 = r15.enqueueImpl(r13)     // Catch:{ all -> 0x006b }
            goto L_0x0056
        L_0x0055:
            r13 = r14
        L_0x0056:
            if (r13 == 0) goto L_0x005e
            kotlinx.coroutines.internal.ThreadSafeHeapNode r11 = r3.removeAtImpl(r14)     // Catch:{ all -> 0x006b }
            goto L_0x0062
        L_0x005e:
            r11 = r4
            kotlinx.coroutines.internal.ThreadSafeHeapNode r11 = (kotlinx.coroutines.internal.ThreadSafeHeapNode) r11     // Catch:{ all -> 0x006b }
            r11 = r4
        L_0x0062:
            monitor-exit(r3)
        L_0x0066:
            kotlinx.coroutines.EventLoopImplBase$DelayedTask r11 = (kotlinx.coroutines.EventLoopImplBase.DelayedTask) r11
            if (r11 != 0) goto L_0x0031
            goto L_0x006e
        L_0x006b:
            r1 = move-exception
            monitor-exit(r3)
            throw r1
        L_0x006e:
            java.lang.Runnable r3 = r15.dequeue()
            if (r3 == 0) goto L_0x007a
            r4 = 0
            r5 = 0
            r3.run()
            return r1
        L_0x007a:
            long r1 = r15.getNextTime()
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.EventLoopImplBase.processNextEvent():long");
    }

    /* access modifiers changed from: protected */
    public final void resetAll() {
        this._queue = null;
        this._delayed = null;
    }

    public final void schedule(long now, DelayedTask delayedTask) {
        switch (scheduleImpl(now, delayedTask)) {
            case 0:
                if (shouldUnpark(delayedTask)) {
                    unpark();
                    return;
                }
                return;
            case 1:
                reschedule(now, delayedTask);
                return;
            case 2:
                return;
            default:
                throw new IllegalStateException("unexpected result".toString());
        }
    }

    /* access modifiers changed from: protected */
    public final DisposableHandle scheduleInvokeOnTimeout(long timeMillis, Runnable block) {
        long delayToNanos = EventLoop_commonKt.delayToNanos(timeMillis);
        if (delayToNanos >= DurationKt.MAX_MILLIS) {
            return NonDisposableHandle.INSTANCE;
        }
        AbstractTimeSource timeSource = AbstractTimeSourceKt.getTimeSource();
        Long valueOf = timeSource == null ? null : Long.valueOf(timeSource.nanoTime());
        long nanoTime = valueOf == null ? System.nanoTime() : valueOf.longValue();
        DelayedRunnableTask delayedRunnableTask = new DelayedRunnableTask(nanoTime + delayToNanos, block);
        schedule(nanoTime, delayedRunnableTask);
        return delayedRunnableTask;
    }

    public void scheduleResumeAfterDelay(long timeMillis, CancellableContinuation<? super Unit> continuation) {
        long delayToNanos = EventLoop_commonKt.delayToNanos(timeMillis);
        if (delayToNanos < DurationKt.MAX_MILLIS) {
            AbstractTimeSource timeSource = AbstractTimeSourceKt.getTimeSource();
            Long valueOf = timeSource == null ? null : Long.valueOf(timeSource.nanoTime());
            long nanoTime = valueOf == null ? System.nanoTime() : valueOf.longValue();
            DelayedResumeTask delayedResumeTask = new DelayedResumeTask(nanoTime + delayToNanos, continuation);
            CancellableContinuationKt.disposeOnCancellation(continuation, delayedResumeTask);
            schedule(nanoTime, delayedResumeTask);
        }
    }

    public void shutdown() {
        ThreadLocalEventLoop.INSTANCE.resetEventLoop$kotlinx_coroutines_core();
        setCompleted(true);
        closeQueue();
        do {
        } while (processNextEvent() <= 0);
        rescheduleAllDelayed();
    }
}

package kotlinx.coroutines.sync;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CancellableContinuationImplKt;
import kotlinx.coroutines.CancellableContinuationKt;
import kotlinx.coroutines.DisposableHandle;
import kotlinx.coroutines.internal.AtomicDesc;
import kotlinx.coroutines.internal.AtomicKt;
import kotlinx.coroutines.internal.AtomicOp;
import kotlinx.coroutines.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.OpDescriptor;
import kotlinx.coroutines.intrinsics.CancellableKt;
import kotlinx.coroutines.intrinsics.UndispatchedKt;
import kotlinx.coroutines.selects.SelectClause2;
import kotlinx.coroutines.selects.SelectInstance;
import kotlinx.coroutines.selects.SelectKt;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\n\b\u0000\u0018\u00002\u00020\u00112\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0005\u0012\u0004\u0012\u00020\u00110 :\u0006$%&'()B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0001¢\u0006\u0004\b\u0003\u0010\u0004J\u0017\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0005H\u0016¢\u0006\u0004\b\u0007\u0010\bJ\u001d\u0010\n\u001a\u00020\t2\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005H@ø\u0001\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u001d\u0010\f\u001a\u00020\t2\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005H@ø\u0001\u0000¢\u0006\u0004\b\f\u0010\u000bJT\u0010\u0014\u001a\u00020\t\"\u0004\b\u0000\u0010\r2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00028\u00000\u000e2\b\u0010\u0006\u001a\u0004\u0018\u00010\u00052\"\u0010\u0013\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\u0011\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0010H\u0016ø\u0001\u0000¢\u0006\u0004\b\u0014\u0010\u0015J\u000f\u0010\u0017\u001a\u00020\u0016H\u0016¢\u0006\u0004\b\u0017\u0010\u0018J\u0019\u0010\u0019\u001a\u00020\u00012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005H\u0016¢\u0006\u0004\b\u0019\u0010\bJ\u0019\u0010\u001a\u001a\u00020\t2\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005H\u0016¢\u0006\u0004\b\u001a\u0010\u001bR\u0014\u0010\u001c\u001a\u00020\u00018VX\u0004¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u001dR\u0014\u0010\u001f\u001a\u00020\u00018@X\u0004¢\u0006\u0006\u001a\u0004\b\u001e\u0010\u001dR\"\u0010#\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0005\u0012\u0004\u0012\u00020\u00110 8VX\u0004¢\u0006\u0006\u001a\u0004\b!\u0010\"\u0002\u0004\n\u0002\b\u0019¨\u0006*"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl;", "", "locked", "<init>", "(Z)V", "", "owner", "holdsLock", "(Ljava/lang/Object;)Z", "", "lock", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "lockSuspend", "R", "Lkotlinx/coroutines/selects/SelectInstance;", "select", "Lkotlin/Function2;", "Lkotlinx/coroutines/sync/Mutex;", "Lkotlin/coroutines/Continuation;", "block", "registerSelectClause2", "(Lkotlinx/coroutines/selects/SelectInstance;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V", "", "toString", "()Ljava/lang/String;", "tryLock", "unlock", "(Ljava/lang/Object;)V", "isLocked", "()Z", "isLockedEmptyQueueState$kotlinx_coroutines_core", "isLockedEmptyQueueState", "Lkotlinx/coroutines/selects/SelectClause2;", "getOnLock", "()Lkotlinx/coroutines/selects/SelectClause2;", "onLock", "LockCont", "LockSelect", "LockWaiter", "LockedQueue", "TryLockDesc", "UnlockOp", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: 01AE */
public final class MutexImpl implements Mutex, SelectClause2<Object, Mutex> {
    static final /* synthetic */ AtomicReferenceFieldUpdater _state$FU = AtomicReferenceFieldUpdater.newUpdater(MutexImpl.class, Object.class, "_state");
    volatile /* synthetic */ Object _state;

    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B\u001d\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006¢\u0006\u0002\u0010\bJ\b\u0010\t\u001a\u00020\u0007H\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0016R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl$LockCont;", "Lkotlinx/coroutines/sync/MutexImpl$LockWaiter;", "Lkotlinx/coroutines/sync/MutexImpl;", "owner", "", "cont", "Lkotlinx/coroutines/CancellableContinuation;", "", "(Lkotlinx/coroutines/sync/MutexImpl;Ljava/lang/Object;Lkotlinx/coroutines/CancellableContinuation;)V", "completeResumeLockWaiter", "toString", "", "tryResumeLockWaiter", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: Mutex.kt */
    private final class LockCont extends LockWaiter {
        private final CancellableContinuation<Unit> cont;

        public LockCont(Object owner, CancellableContinuation<? super Unit> cont2) {
            super(owner);
            this.cont = cont2;
        }

        public void completeResumeLockWaiter() {
            this.cont.completeResume(CancellableContinuationImplKt.RESUME_TOKEN);
        }

        public String toString() {
            return "LockCont[" + this.owner + ", " + this.cont + "] for " + MutexImpl.this;
        }

        public boolean tryResumeLockWaiter() {
            return take() && this.cont.tryResume(Unit.INSTANCE, (Object) null, new MutexImpl$LockCont$tryResumeLockWaiter$1(MutexImpl.this, this)) != null;
        }
    }

    @Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0004\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00060\u0002R\u00020\u0003BD\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007\u0012\"\u0010\b\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u00050\tø\u0001\u0000¢\u0006\u0002\u0010\fJ\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0012\u001a\u00020\u0013H\u0016R1\u0010\b\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u00050\t8\u0006X\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\rR\u0016\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u00078\u0006X\u0004¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006\u0014"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl$LockSelect;", "R", "Lkotlinx/coroutines/sync/MutexImpl$LockWaiter;", "Lkotlinx/coroutines/sync/MutexImpl;", "owner", "", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/sync/Mutex;", "Lkotlin/coroutines/Continuation;", "(Lkotlinx/coroutines/sync/MutexImpl;Ljava/lang/Object;Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "Lkotlin/jvm/functions/Function2;", "completeResumeLockWaiter", "", "toString", "", "tryResumeLockWaiter", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: Mutex.kt */
    private final class LockSelect<R> extends LockWaiter {
        public final Function2<Mutex, Continuation<? super R>, Object> block;
        public final SelectInstance<R> select;

        public LockSelect(Object owner, SelectInstance<? super R> select2, Function2<? super Mutex, ? super Continuation<? super R>, ? extends Object> block2) {
            super(owner);
            this.select = select2;
            this.block = block2;
        }

        public void completeResumeLockWaiter() {
            CancellableKt.startCoroutineCancellable(this.block, MutexImpl.this, this.select.getCompletion(), new MutexImpl$LockSelect$completeResumeLockWaiter$1(MutexImpl.this, this));
        }

        public String toString() {
            return "LockSelect[" + this.owner + ", " + this.select + "] for " + MutexImpl.this;
        }

        public boolean tryResumeLockWaiter() {
            return take() && this.select.trySelect();
        }
    }

    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\b¢\u0004\u0018\u00002\u00020\u000f2\u00020\u0010B\u0011\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0001¢\u0006\u0004\b\u0003\u0010\u0004J\u000f\u0010\u0006\u001a\u00020\u0005H&¢\u0006\u0004\b\u0006\u0010\u0007J\r\u0010\b\u001a\u00020\u0005¢\u0006\u0004\b\b\u0010\u0007J\r\u0010\n\u001a\u00020\t¢\u0006\u0004\b\n\u0010\u000bJ\u000f\u0010\f\u001a\u00020\tH&¢\u0006\u0004\b\f\u0010\u000bR\u0016\u0010\u0002\u001a\u0004\u0018\u00010\u00018\u0006X\u0004¢\u0006\u0006\n\u0004\b\u0002\u0010\r¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl$LockWaiter;", "", "owner", "<init>", "(Lkotlinx/coroutines/sync/MutexImpl;Ljava/lang/Object;)V", "", "completeResumeLockWaiter", "()V", "dispose", "", "take", "()Z", "tryResumeLockWaiter", "Ljava/lang/Object;", "kotlinx-coroutines-core", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/DisposableHandle;"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: Mutex.kt */
    private abstract class LockWaiter extends LockFreeLinkedListNode implements DisposableHandle {
        private static final /* synthetic */ AtomicIntegerFieldUpdater isTaken$FU = AtomicIntegerFieldUpdater.newUpdater(LockWaiter.class, "isTaken");
        private volatile /* synthetic */ int isTaken = 0;
        public final Object owner;

        public LockWaiter(Object owner2) {
            this.owner = owner2;
        }

        public abstract void completeResumeLockWaiter();

        public final void dispose() {
            remove();
        }

        public final boolean take() {
            return isTaken$FU.compareAndSet(this, 0, 1);
        }

        public abstract boolean tryResumeLockWaiter();
    }

    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016R\u0012\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl$LockedQueue;", "Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "owner", "", "(Ljava/lang/Object;)V", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: Mutex.kt */
    private static final class LockedQueue extends LockFreeLinkedListHead {
        public Object owner;

        public LockedQueue(Object owner2) {
            this.owner = owner2;
        }

        public String toString() {
            return "LockedQueue[" + this.owner + ']';
        }
    }

    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001:\u0001\rB\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u001e\u0010\u0007\u001a\u00020\b2\n\u0010\t\u001a\u0006\u0012\u0002\b\u00030\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\u0005H\u0016J\u0016\u0010\f\u001a\u0004\u0018\u00010\u00052\n\u0010\t\u001a\u0006\u0012\u0002\b\u00030\nH\u0016R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl$TryLockDesc;", "Lkotlinx/coroutines/internal/AtomicDesc;", "mutex", "Lkotlinx/coroutines/sync/MutexImpl;", "owner", "", "(Lkotlinx/coroutines/sync/MutexImpl;Ljava/lang/Object;)V", "complete", "", "op", "Lkotlinx/coroutines/internal/AtomicOp;", "failure", "prepare", "PrepareOp", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: Mutex.kt */
    private static final class TryLockDesc extends AtomicDesc {
        public final MutexImpl mutex;
        public final Object owner;

        @Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0011\u0012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003¢\u0006\u0002\u0010\u0004J\u0014\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\bH\u0016R\u0018\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\n"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl$TryLockDesc$PrepareOp;", "Lkotlinx/coroutines/internal/OpDescriptor;", "atomicOp", "Lkotlinx/coroutines/internal/AtomicOp;", "(Lkotlinx/coroutines/sync/MutexImpl$TryLockDesc;Lkotlinx/coroutines/internal/AtomicOp;)V", "getAtomicOp", "()Lkotlinx/coroutines/internal/AtomicOp;", "perform", "", "affected", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
        /* compiled from: Mutex.kt */
        private final class PrepareOp extends OpDescriptor {
            private final AtomicOp<?> atomicOp;

            public PrepareOp(AtomicOp<?> atomicOp2) {
                this.atomicOp = atomicOp2;
            }

            public AtomicOp<?> getAtomicOp() {
                return this.atomicOp;
            }

            public Object perform(Object affected) {
                Object access$getEMPTY_UNLOCKED$p = getAtomicOp().isDecided() ? MutexKt.EMPTY_UNLOCKED : getAtomicOp();
                if (affected != null) {
                    AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(MutexImpl._state$FU, (MutexImpl) affected, this, access$getEMPTY_UNLOCKED$p);
                    return null;
                }
                throw new NullPointerException("null cannot be cast to non-null type kotlinx.coroutines.sync.MutexImpl");
            }
        }

        public TryLockDesc(MutexImpl mutex2, Object owner2) {
            this.mutex = mutex2;
            this.owner = owner2;
        }

        public void complete(AtomicOp<?> op, Object failure) {
            AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(MutexImpl._state$FU, this.mutex, op, failure != null ? MutexKt.EMPTY_UNLOCKED : this.owner == null ? MutexKt.EMPTY_LOCKED : new Empty(this.owner));
        }

        public Object prepare(AtomicOp<?> op) {
            PrepareOp prepareOp = new PrepareOp(op);
            return !AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(MutexImpl._state$FU, this.mutex, MutexKt.EMPTY_UNLOCKED, prepareOp) ? MutexKt.LOCK_FAIL : prepareOp.perform(this.mutex);
        }
    }

    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u001a\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00022\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016J\u0012\u0010\u000b\u001a\u0004\u0018\u00010\n2\u0006\u0010\b\u001a\u00020\u0002H\u0016R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/sync/MutexImpl$UnlockOp;", "Lkotlinx/coroutines/internal/AtomicOp;", "Lkotlinx/coroutines/sync/MutexImpl;", "queue", "Lkotlinx/coroutines/sync/MutexImpl$LockedQueue;", "(Lkotlinx/coroutines/sync/MutexImpl$LockedQueue;)V", "complete", "", "affected", "failure", "", "prepare", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: Mutex.kt */
    private static final class UnlockOp extends AtomicOp<MutexImpl> {
        public final LockedQueue queue;

        public UnlockOp(LockedQueue queue2) {
            this.queue = queue2;
        }

        public void complete(MutexImpl affected, Object failure) {
            AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(MutexImpl._state$FU, affected, this, failure == null ? MutexKt.EMPTY_UNLOCKED : this.queue);
        }

        public Object prepare(MutexImpl affected) {
            if (this.queue.isEmpty()) {
                return null;
            }
            return MutexKt.UNLOCK_FAIL;
        }
    }

    public MutexImpl(boolean locked) {
        this._state = locked ? MutexKt.EMPTY_LOCKED : MutexKt.EMPTY_UNLOCKED;
    }

    /* access modifiers changed from: private */
    public final Object lockSuspend(Object owner, Continuation<? super Unit> $completion) {
        Object obj = owner;
        CancellableContinuationImpl<? super Unit> orCreateCancellableContinuation = CancellableContinuationKt.getOrCreateCancellableContinuation(IntrinsicsKt.intercepted($completion));
        CancellableContinuation cancellableContinuation = orCreateCancellableContinuation;
        LockCont lockCont = new LockCont(obj, cancellableContinuation);
        while (true) {
            Object obj2 = this._state;
            if (obj2 instanceof Empty) {
                if (((Empty) obj2).locked != MutexKt.UNLOCKED) {
                    AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, obj2, new LockedQueue(((Empty) obj2).locked));
                } else {
                    if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, obj2, obj == null ? MutexKt.EMPTY_LOCKED : new Empty(obj))) {
                        cancellableContinuation.resume(Unit.INSTANCE, new MutexImpl$lockSuspend$2$1$1(this, obj));
                        break;
                    }
                }
            } else if (obj2 instanceof LockedQueue) {
                if (((LockedQueue) obj2).owner != obj) {
                    ((LockedQueue) obj2).addLast(lockCont);
                    if (this._state == obj2 || !lockCont.take()) {
                        CancellableContinuationKt.removeOnCancellation(cancellableContinuation, lockCont);
                    } else {
                        lockCont = new LockCont(obj, cancellableContinuation);
                    }
                } else {
                    String stringPlus = Intrinsics.stringPlus("Already locked by ", obj);
                    Log1F380D.a((Object) stringPlus);
                    throw new IllegalStateException(stringPlus.toString());
                }
            } else if (obj2 instanceof OpDescriptor) {
                ((OpDescriptor) obj2).perform(this);
            } else {
                String stringPlus2 = Intrinsics.stringPlus("Illegal state ", obj2);
                Log1F380D.a((Object) stringPlus2);
                throw new IllegalStateException(stringPlus2.toString());
            }
        }
        CancellableContinuationKt.removeOnCancellation(cancellableContinuation, lockCont);
        Object result = orCreateCancellableContinuation.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended($completion);
        }
        return result == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? result : Unit.INSTANCE;
    }

    public SelectClause2<Object, Mutex> getOnLock() {
        return this;
    }

    public boolean holdsLock(Object owner) {
        Object obj = this._state;
        if (obj instanceof Empty) {
            return ((Empty) obj).locked == owner;
        }
        if (obj instanceof LockedQueue) {
            return ((LockedQueue) obj).owner == owner;
        }
        return false;
    }

    public final boolean isLockedEmptyQueueState$kotlinx_coroutines_core() {
        Object obj = this._state;
        return (obj instanceof LockedQueue) && ((LockedQueue) obj).isEmpty();
    }

    public Object lock(Object owner, Continuation<? super Unit> $completion) {
        if (tryLock(owner)) {
            return Unit.INSTANCE;
        }
        Object lockSuspend = lockSuspend(owner, $completion);
        return lockSuspend == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? lockSuspend : Unit.INSTANCE;
    }

    public boolean isLocked() {
        while (true) {
            Object obj = this._state;
            if (obj instanceof Empty) {
                return ((Empty) obj).locked != MutexKt.UNLOCKED;
            }
            if (obj instanceof LockedQueue) {
                return true;
            }
            if (obj instanceof OpDescriptor) {
                ((OpDescriptor) obj).perform(this);
            } else {
                String stringPlus = Intrinsics.stringPlus("Illegal state ", obj);
                Log1F380D.a((Object) stringPlus);
                throw new IllegalStateException(stringPlus.toString());
            }
        }
    }

    public <R> void registerSelectClause2(SelectInstance<? super R> select, Object owner, Function2<? super Mutex, ? super Continuation<? super R>, ? extends Object> block) {
        while (!select.isSelected()) {
            Object obj = this._state;
            if (obj instanceof Empty) {
                if (((Empty) obj).locked != MutexKt.UNLOCKED) {
                    AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, obj, new LockedQueue(((Empty) obj).locked));
                } else {
                    Object performAtomicTrySelect = select.performAtomicTrySelect(new TryLockDesc(this, owner));
                    if (performAtomicTrySelect == null) {
                        UndispatchedKt.startCoroutineUnintercepted(block, this, select.getCompletion());
                        return;
                    } else if (performAtomicTrySelect != SelectKt.getALREADY_SELECTED()) {
                        if (!(performAtomicTrySelect == MutexKt.LOCK_FAIL || performAtomicTrySelect == AtomicKt.RETRY_ATOMIC)) {
                            String stringPlus = Intrinsics.stringPlus("performAtomicTrySelect(TryLockDesc) returned ", performAtomicTrySelect);
                            Log1F380D.a((Object) stringPlus);
                            throw new IllegalStateException(stringPlus.toString());
                        }
                    } else {
                        return;
                    }
                }
            } else if (obj instanceof LockedQueue) {
                if (((LockedQueue) obj).owner != owner) {
                    LockSelect lockSelect = new LockSelect(owner, select, block);
                    ((LockedQueue) obj).addLast(lockSelect);
                    if (this._state == obj || !lockSelect.take()) {
                        select.disposeOnSelect(lockSelect);
                        return;
                    }
                } else {
                    String stringPlus2 = Intrinsics.stringPlus("Already locked by ", owner);
                    Log1F380D.a((Object) stringPlus2);
                    throw new IllegalStateException(stringPlus2.toString());
                }
            } else if (obj instanceof OpDescriptor) {
                ((OpDescriptor) obj).perform(this);
            } else {
                String stringPlus3 = Intrinsics.stringPlus("Illegal state ", obj);
                Log1F380D.a((Object) stringPlus3);
                throw new IllegalStateException(stringPlus3.toString());
            }
        }
    }

    public String toString() {
        while (true) {
            Object obj = this._state;
            if (obj instanceof Empty) {
                return "Mutex[" + ((Empty) obj).locked + ']';
            }
            if (obj instanceof OpDescriptor) {
                ((OpDescriptor) obj).perform(this);
            } else if (obj instanceof LockedQueue) {
                return "Mutex[" + ((LockedQueue) obj).owner + ']';
            } else {
                String stringPlus = Intrinsics.stringPlus("Illegal state ", obj);
                Log1F380D.a((Object) stringPlus);
                throw new IllegalStateException(stringPlus.toString());
            }
        }
    }

    public boolean tryLock(Object owner) {
        while (true) {
            Object obj = this._state;
            boolean z = true;
            if (obj instanceof Empty) {
                if (((Empty) obj).locked != MutexKt.UNLOCKED) {
                    return false;
                }
                if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, obj, owner == null ? MutexKt.EMPTY_LOCKED : new Empty(owner))) {
                    return true;
                }
            } else if (obj instanceof LockedQueue) {
                if (((LockedQueue) obj).owner == owner) {
                    z = false;
                }
                if (z) {
                    return false;
                }
                String stringPlus = Intrinsics.stringPlus("Already locked by ", owner);
                Log1F380D.a((Object) stringPlus);
                throw new IllegalStateException(stringPlus.toString());
            } else if (obj instanceof OpDescriptor) {
                ((OpDescriptor) obj).perform(this);
            } else {
                String stringPlus2 = Intrinsics.stringPlus("Illegal state ", obj);
                Log1F380D.a((Object) stringPlus2);
                throw new IllegalStateException(stringPlus2.toString());
            }
        }
    }

    public void unlock(Object owner) {
        while (true) {
            Object obj = this._state;
            boolean z = true;
            if (obj instanceof Empty) {
                if (owner == null) {
                    if (((Empty) obj).locked == MutexKt.UNLOCKED) {
                        z = false;
                    }
                    if (!z) {
                        throw new IllegalStateException("Mutex is not locked".toString());
                    }
                } else {
                    if (((Empty) obj).locked != owner) {
                        z = false;
                    }
                    if (!z) {
                        throw new IllegalStateException(("Mutex is locked by " + ((Empty) obj).locked + " but expected " + owner).toString());
                    }
                }
                if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, obj, MutexKt.EMPTY_UNLOCKED)) {
                    return;
                }
            } else if (obj instanceof OpDescriptor) {
                ((OpDescriptor) obj).perform(this);
            } else if (obj instanceof LockedQueue) {
                if (owner != null) {
                    if (((LockedQueue) obj).owner != owner) {
                        z = false;
                    }
                    if (!z) {
                        throw new IllegalStateException(("Mutex is locked by " + ((LockedQueue) obj).owner + " but expected " + owner).toString());
                    }
                }
                LockFreeLinkedListNode removeFirstOrNull = ((LockedQueue) obj).removeFirstOrNull();
                if (removeFirstOrNull == null) {
                    UnlockOp unlockOp = new UnlockOp((LockedQueue) obj);
                    if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_state$FU, this, obj, unlockOp) && unlockOp.perform(this) == null) {
                        return;
                    }
                } else if (((LockWaiter) removeFirstOrNull).tryResumeLockWaiter()) {
                    LockedQueue lockedQueue = (LockedQueue) obj;
                    Object obj2 = ((LockWaiter) removeFirstOrNull).owner;
                    if (obj2 == null) {
                        obj2 = MutexKt.LOCKED;
                    }
                    lockedQueue.owner = obj2;
                    ((LockWaiter) removeFirstOrNull).completeResumeLockWaiter();
                    return;
                }
            } else {
                String stringPlus = Intrinsics.stringPlus("Illegal state ", obj);
                Log1F380D.a((Object) stringPlus);
                throw new IllegalStateException(stringPlus.toString());
            }
        }
    }
}

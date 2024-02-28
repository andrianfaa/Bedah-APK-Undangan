package kotlinx.coroutines.internal;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlinx.coroutines.DebugKt;

@Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0016\b\u0000\u0018\u0000 /*\b\b\u0000\u0010\u0002*\u00020\u00012\u00020\u0001:\u0002/0B\u0017\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0005¢\u0006\u0004\b\u0007\u0010\bJ\u0015\u0010\n\u001a\u00020\u00032\u0006\u0010\t\u001a\u00028\u0000¢\u0006\u0004\b\n\u0010\u000bJ'\u0010\u000f\u001a\u0012\u0012\u0004\u0012\u00028\u00000\u0000j\b\u0012\u0004\u0012\u00028\u0000`\u000e2\u0006\u0010\r\u001a\u00020\fH\u0002¢\u0006\u0004\b\u000f\u0010\u0010J'\u0010\u0011\u001a\u0012\u0012\u0004\u0012\u00028\u00000\u0000j\b\u0012\u0004\u0012\u00028\u0000`\u000e2\u0006\u0010\r\u001a\u00020\fH\u0002¢\u0006\u0004\b\u0011\u0010\u0010J\r\u0010\u0012\u001a\u00020\u0005¢\u0006\u0004\b\u0012\u0010\u0013J3\u0010\u0015\u001a\u0016\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\u000e2\u0006\u0010\u0014\u001a\u00020\u00032\u0006\u0010\t\u001a\u00028\u0000H\u0002¢\u0006\u0004\b\u0015\u0010\u0016J\r\u0010\u0017\u001a\u00020\u0005¢\u0006\u0004\b\u0017\u0010\u0013J-\u0010\u001c\u001a\b\u0012\u0004\u0012\u00028\u00010\u001b\"\u0004\b\u0001\u0010\u00182\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0019¢\u0006\u0004\b\u001c\u0010\u001dJ\u000f\u0010\u001e\u001a\u00020\fH\u0002¢\u0006\u0004\b\u001e\u0010\u001fJ\u0013\u0010 \u001a\b\u0012\u0004\u0012\u00028\u00000\u0000¢\u0006\u0004\b \u0010!J\u000f\u0010\"\u001a\u0004\u0018\u00010\u0001¢\u0006\u0004\b\"\u0010#J3\u0010&\u001a\u0016\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\u000e2\u0006\u0010$\u001a\u00020\u00032\u0006\u0010%\u001a\u00020\u0003H\u0002¢\u0006\u0004\b&\u0010'R\u0014\u0010\u0004\u001a\u00020\u00038\u0002X\u0004¢\u0006\u0006\n\u0004\b\u0004\u0010(R\u0011\u0010)\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b)\u0010\u0013R\u0014\u0010*\u001a\u00020\u00038\u0002X\u0004¢\u0006\u0006\n\u0004\b*\u0010(R\u0014\u0010\u0006\u001a\u00020\u00058\u0002X\u0004¢\u0006\u0006\n\u0004\b\u0006\u0010+R\u0011\u0010.\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b,\u0010-¨\u00061"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;", "", "E", "", "capacity", "", "singleConsumer", "<init>", "(IZ)V", "element", "addLast", "(Ljava/lang/Object;)I", "", "state", "Lkotlinx/coroutines/internal/Core;", "allocateNextCopy", "(J)Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;", "allocateOrGetNextCopy", "close", "()Z", "index", "fillPlaceholder", "(ILjava/lang/Object;)Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;", "isClosed", "R", "Lkotlin/Function1;", "transform", "", "map", "(Lkotlin/jvm/functions/Function1;)Ljava/util/List;", "markFrozen", "()J", "next", "()Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;", "removeFirstOrNull", "()Ljava/lang/Object;", "oldHead", "newHead", "removeSlowPath", "(II)Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;", "I", "isEmpty", "mask", "Z", "getSize", "()I", "size", "Companion", "Placeholder", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: LockFreeTaskQueue.kt */
public final class LockFreeTaskQueueCore<E> {
    public static final int ADD_CLOSED = 2;
    public static final int ADD_FROZEN = 1;
    public static final int ADD_SUCCESS = 0;
    public static final int CAPACITY_BITS = 30;
    public static final long CLOSED_MASK = 2305843009213693952L;
    public static final int CLOSED_SHIFT = 61;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final long FROZEN_MASK = 1152921504606846976L;
    public static final int FROZEN_SHIFT = 60;
    public static final long HEAD_MASK = 1073741823;
    public static final int HEAD_SHIFT = 0;
    public static final int INITIAL_CAPACITY = 8;
    public static final int MAX_CAPACITY_MASK = 1073741823;
    public static final int MIN_ADD_SPIN_CAPACITY = 1024;
    public static final Symbol REMOVE_FROZEN = new Symbol("REMOVE_FROZEN");
    public static final long TAIL_MASK = 1152921503533105152L;
    public static final int TAIL_SHIFT = 30;
    private static final /* synthetic */ AtomicReferenceFieldUpdater _next$FU;
    private static final /* synthetic */ AtomicLongFieldUpdater _state$FU;
    private volatile /* synthetic */ Object _next = null;
    private volatile /* synthetic */ long _state = 0;
    private /* synthetic */ AtomicReferenceArray array;
    private final int capacity;
    private final int mask;
    private final boolean singleConsumer;

    @Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\n\u0010\u0016\u001a\u00020\u0004*\u00020\tJ\u0012\u0010\u0017\u001a\u00020\t*\u00020\t2\u0006\u0010\u0018\u001a\u00020\u0004J\u0012\u0010\u0019\u001a\u00020\t*\u00020\t2\u0006\u0010\u001a\u001a\u00020\u0004JP\u0010\u001b\u001a\u0002H\u001c\"\u0004\b\u0001\u0010\u001c*\u00020\t26\u0010\u001d\u001a2\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u001f\u0012\b\b \u0012\u0004\b\b(!\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u001f\u0012\b\b \u0012\u0004\b\b(\"\u0012\u0004\u0012\u0002H\u001c0\u001eH\b¢\u0006\u0002\u0010#J\u0015\u0010$\u001a\u00020\t*\u00020\t2\u0006\u0010%\u001a\u00020\tH\u0004R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tXT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tXT¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\tXT¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u00020\u00138\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\tXT¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006&"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeTaskQueueCore$Companion;", "", "()V", "ADD_CLOSED", "", "ADD_FROZEN", "ADD_SUCCESS", "CAPACITY_BITS", "CLOSED_MASK", "", "CLOSED_SHIFT", "FROZEN_MASK", "FROZEN_SHIFT", "HEAD_MASK", "HEAD_SHIFT", "INITIAL_CAPACITY", "MAX_CAPACITY_MASK", "MIN_ADD_SPIN_CAPACITY", "REMOVE_FROZEN", "Lkotlinx/coroutines/internal/Symbol;", "TAIL_MASK", "TAIL_SHIFT", "addFailReason", "updateHead", "newHead", "updateTail", "newTail", "withState", "T", "block", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "head", "tail", "(JLkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "wo", "other", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: LockFreeTaskQueue.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final int addFailReason(long $this$addFailReason) {
            return (LockFreeTaskQueueCore.CLOSED_MASK & $this$addFailReason) != 0 ? 2 : 1;
        }

        public final long updateHead(long $this$updateHead, int newHead) {
            return wo($this$updateHead, LockFreeTaskQueueCore.HEAD_MASK) | (((long) newHead) << 0);
        }

        public final long updateTail(long $this$updateTail, int newTail) {
            return wo($this$updateTail, LockFreeTaskQueueCore.TAIL_MASK) | (((long) newTail) << 30);
        }

        public final <T> T withState(long $this$withState, Function2<? super Integer, ? super Integer, ? extends T> block) {
            return block.invoke(Integer.valueOf((int) ((LockFreeTaskQueueCore.HEAD_MASK & $this$withState) >> 0)), Integer.valueOf((int) ((LockFreeTaskQueueCore.TAIL_MASK & $this$withState) >> 30)));
        }

        public final long wo(long $this$wo, long other) {
            return (~other) & $this$wo;
        }
    }

    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeTaskQueueCore$Placeholder;", "", "index", "", "(I)V", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: LockFreeTaskQueue.kt */
    public static final class Placeholder {
        public final int index;

        public Placeholder(int index2) {
            this.index = index2;
        }
    }

    static {
        Class<LockFreeTaskQueueCore> cls = LockFreeTaskQueueCore.class;
        _next$FU = AtomicReferenceFieldUpdater.newUpdater(cls, Object.class, "_next");
        _state$FU = AtomicLongFieldUpdater.newUpdater(cls, "_state");
    }

    public LockFreeTaskQueueCore(int capacity2, boolean singleConsumer2) {
        this.capacity = capacity2;
        this.singleConsumer = singleConsumer2;
        int i = capacity2 - 1;
        this.mask = i;
        this.array = new AtomicReferenceArray(capacity2);
        boolean z = false;
        if (i <= 1073741823) {
            if (!((i & capacity2) == 0 ? true : z)) {
                throw new IllegalStateException("Check failed.".toString());
            }
            return;
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    private final LockFreeTaskQueueCore<E> allocateNextCopy(long state) {
        LockFreeTaskQueueCore<E> lockFreeTaskQueueCore = new LockFreeTaskQueueCore<>(this.capacity * 2, this.singleConsumer);
        Companion companion = Companion;
        long j = state;
        int i = (int) ((TAIL_MASK & j) >> 30);
        int i2 = (int) ((HEAD_MASK & j) >> 0);
        while (true) {
            int i3 = this.mask;
            if ((i2 & i3) != (i & i3)) {
                Object obj = this.array.get(i3 & i2);
                if (obj == null) {
                    obj = new Placeholder(i2);
                }
                lockFreeTaskQueueCore.array.set(lockFreeTaskQueueCore.mask & i2, obj);
                i2++;
            } else {
                Companion companion2 = companion;
                long j2 = j;
                lockFreeTaskQueueCore._state = Companion.wo(state, FROZEN_MASK);
                return lockFreeTaskQueueCore;
            }
        }
    }

    private final LockFreeTaskQueueCore<E> allocateOrGetNextCopy(long state) {
        while (true) {
            LockFreeTaskQueueCore<E> lockFreeTaskQueueCore = (LockFreeTaskQueueCore) this._next;
            if (lockFreeTaskQueueCore != null) {
                return lockFreeTaskQueueCore;
            }
            AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_next$FU, this, (Object) null, allocateNextCopy(state));
        }
    }

    private final LockFreeTaskQueueCore<E> fillPlaceholder(int index, E element) {
        Object obj = this.array.get(this.mask & index);
        if (!(obj instanceof Placeholder) || ((Placeholder) obj).index != index) {
            return null;
        }
        this.array.set(this.mask & index, element);
        return this;
    }

    private final long markFrozen() {
        long j;
        long j2;
        do {
            j = this._state;
            long j3 = j;
            if ((j3 & FROZEN_MASK) != 0) {
                return j3;
            }
            j2 = j3 | FROZEN_MASK;
        } while (!_state$FU.compareAndSet(this, j, j2));
        return j2;
    }

    private final LockFreeTaskQueueCore<E> removeSlowPath(int oldHead, int newHead) {
        LockFreeTaskQueueCore lockFreeTaskQueueCore = this;
        while (true) {
            long j = lockFreeTaskQueueCore._state;
            Companion companion = Companion;
            long j2 = j;
            boolean z = false;
            int i = (int) ((HEAD_MASK & j2) >> 0);
            int i2 = (int) ((TAIL_MASK & j2) >> 30);
            int i3 = i;
            int i4 = i2;
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (i3 == oldHead) {
                    z = true;
                }
                if (!z) {
                    throw new AssertionError();
                }
            } else {
                int i5 = oldHead;
            }
            if ((j & FROZEN_MASK) != 0) {
                return next();
            }
            LockFreeTaskQueueCore lockFreeTaskQueueCore2 = lockFreeTaskQueueCore;
            int i6 = i3;
            int i7 = i;
            int i8 = i2;
            if (_state$FU.compareAndSet(this, j, Companion.updateHead(j, newHead))) {
                this.array.set(i6 & this.mask, (Object) null);
                return null;
            }
            lockFreeTaskQueueCore = lockFreeTaskQueueCore2;
        }
    }

    public final int addLast(E element) {
        LockFreeTaskQueueCore fillPlaceholder;
        E e = element;
        while (true) {
            long j = this._state;
            if ((3458764513820540928L & j) != 0) {
                return Companion.addFailReason(j);
            }
            Companion companion = Companion;
            long j2 = j;
            int i = (int) ((j2 & HEAD_MASK) >> 0);
            int i2 = (int) ((j2 & TAIL_MASK) >> 30);
            int i3 = i;
            int i4 = i2;
            int i5 = this.mask;
            if (((i4 + 2) & i5) == (i3 & i5)) {
                return 1;
            }
            if (this.singleConsumer || this.array.get(i4 & i5) == null) {
                int i6 = (i4 + 1) & MAX_CAPACITY_MASK;
                int i7 = i4;
                int i8 = i5;
                int i9 = i;
                int i10 = i2;
                if (_state$FU.compareAndSet(this, j, Companion.updateTail(j, i6))) {
                    this.array.set(i7 & i8, e);
                    LockFreeTaskQueueCore lockFreeTaskQueueCore = this;
                    while ((lockFreeTaskQueueCore._state & FROZEN_MASK) != 0 && (fillPlaceholder = lockFreeTaskQueueCore.next().fillPlaceholder(i7, e)) != null) {
                        lockFreeTaskQueueCore = fillPlaceholder;
                    }
                    return 0;
                }
            } else {
                int i11 = this.capacity;
                if (i11 < 1024 || (1073741823 & (i4 - i3)) > (i11 >> 1)) {
                    return 1;
                }
            }
        }
        return 1;
    }

    public final boolean close() {
        long j;
        long j2;
        do {
            j = this._state;
            j2 = j;
            if ((j2 & CLOSED_MASK) != 0) {
                return true;
            }
            if ((FROZEN_MASK & j2) != 0) {
                return false;
            }
        } while (!_state$FU.compareAndSet(this, j, j2 | CLOSED_MASK));
        return true;
    }

    public final int getSize() {
        Companion companion = Companion;
        long j = this._state;
        return (((int) ((TAIL_MASK & j) >> 30)) - ((int) ((HEAD_MASK & j) >> 0))) & MAX_CAPACITY_MASK;
    }

    public final boolean isClosed() {
        return (this._state & CLOSED_MASK) != 0;
    }

    public final boolean isEmpty() {
        Companion companion = Companion;
        long j = this._state;
        return ((int) ((HEAD_MASK & j) >> 0)) == ((int) ((TAIL_MASK & j) >> 30));
    }

    public final <R> List<R> map(Function1<? super E, ? extends R> transform) {
        ArrayList arrayList = new ArrayList(this.capacity);
        Companion companion = Companion;
        long j = this._state;
        int i = (int) ((TAIL_MASK & j) >> 30);
        int i2 = (int) ((HEAD_MASK & j) >> 0);
        while (true) {
            int i3 = this.mask;
            if ((i2 & i3) == (i & i3)) {
                return arrayList;
            }
            Object obj = this.array.get(i3 & i2);
            if (obj != null && !(obj instanceof Placeholder)) {
                arrayList.add(transform.invoke(obj));
            }
            i2++;
        }
    }

    public final LockFreeTaskQueueCore<E> next() {
        return allocateOrGetNextCopy(markFrozen());
    }

    public final Object removeFirstOrNull() {
        boolean z;
        LockFreeTaskQueueCore lockFreeTaskQueueCore = this;
        boolean z2 = false;
        while (true) {
            long j = lockFreeTaskQueueCore._state;
            if ((FROZEN_MASK & j) != 0) {
                return REMOVE_FROZEN;
            }
            Companion companion = Companion;
            long j2 = j;
            int i = (int) ((HEAD_MASK & j2) >> 0);
            int i2 = (int) ((TAIL_MASK & j2) >> 30);
            int i3 = i;
            int i4 = this.mask;
            LockFreeTaskQueueCore lockFreeTaskQueueCore2 = lockFreeTaskQueueCore;
            if ((i2 & i4) == (i3 & i4)) {
                return null;
            }
            Object obj = this.array.get(i4 & i3);
            if (obj == null) {
                if (this.singleConsumer) {
                    return null;
                }
                z = z2;
            } else if (obj instanceof Placeholder) {
                return null;
            } else {
                int i5 = 1073741823 & (i3 + 1);
                int i6 = i5;
                Object obj2 = obj;
                z = z2;
                int i7 = i3;
                int i8 = i;
                int i9 = i2;
                if (_state$FU.compareAndSet(this, j, Companion.updateHead(j, i5))) {
                    this.array.set(this.mask & i7, (Object) null);
                    return obj2;
                } else if (this.singleConsumer) {
                    LockFreeTaskQueueCore lockFreeTaskQueueCore3 = this;
                    while (true) {
                        LockFreeTaskQueueCore removeSlowPath = lockFreeTaskQueueCore3.removeSlowPath(i7, i6);
                        if (removeSlowPath == null) {
                            return obj2;
                        }
                        lockFreeTaskQueueCore3 = removeSlowPath;
                    }
                }
            }
            lockFreeTaskQueueCore = lockFreeTaskQueueCore2;
            z2 = z;
        }
    }
}

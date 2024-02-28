package kotlinx.coroutines.channels;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.CancellableContinuationImplKt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.internal.ConcurrentKt;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.selects.SelectInstance;
import kotlinx.coroutines.selects.SelectKt;

@Metadata(d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00028\u00000L2\b\u0012\u0004\u0012\u00028\u00000M:\u0001JB\u000f\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u0004\u0010\u0005J\u0019\u0010\t\u001a\u00020\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0017¢\u0006\u0004\b\t\u0010\nJ\u001f\u0010\t\u001a\u00020\r2\u000e\u0010\u0007\u001a\n\u0018\u00010\u000bj\u0004\u0018\u0001`\fH\u0016¢\u0006\u0004\b\t\u0010\u000eJ\u0019\u0010\u000f\u001a\u00020\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0002¢\u0006\u0004\b\u000f\u0010\nJ\u000f\u0010\u0010\u001a\u00020\rH\u0002¢\u0006\u0004\b\u0010\u0010\u0011J\u0019\u0010\u0012\u001a\u00020\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0016¢\u0006\u0004\b\u0012\u0010\nJ\u000f\u0010\u0014\u001a\u00020\u0013H\u0002¢\u0006\u0004\b\u0014\u0010\u0015J\u0017\u0010\u0017\u001a\u00028\u00002\u0006\u0010\u0016\u001a\u00020\u0013H\u0002¢\u0006\u0004\b\u0017\u0010\u0018J\u0017\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u0019\u001a\u00028\u0000H\u0014¢\u0006\u0004\b\u001b\u0010\u001cJ#\u0010\u001f\u001a\u00020\u001a2\u0006\u0010\u0019\u001a\u00028\u00002\n\u0010\u001e\u001a\u0006\u0012\u0002\b\u00030\u001dH\u0014¢\u0006\u0004\b\u001f\u0010 J\u0015\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000!H\u0016¢\u0006\u0004\b\"\u0010#J4\u0010'\u001a\u00020\r2\u0010\b\u0002\u0010%\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010$2\u0010\b\u0002\u0010&\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010$H\u0010¢\u0006\u0004\b'\u0010(R\u001c\u0010*\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001a0)8\u0002X\u0004¢\u0006\u0006\n\u0004\b*\u0010+R\u0014\u0010/\u001a\u00020,8TX\u0004¢\u0006\u0006\u001a\u0004\b-\u0010.R\u0018\u00102\u001a\u000600j\u0002`18\u0002X\u0004¢\u0006\u0006\n\u0004\b2\u00103R\u0017\u0010\u0003\u001a\u00020\u00028\u0006¢\u0006\f\n\u0004\b\u0003\u00104\u001a\u0004\b5\u00106R$\u0010;\u001a\u00020\u00132\u0006\u00107\u001a\u00020\u00138B@BX\u000e¢\u0006\f\u001a\u0004\b8\u0010\u0015\"\u0004\b9\u0010:R\u0014\u0010<\u001a\u00020\b8TX\u0004¢\u0006\u0006\u001a\u0004\b<\u0010=R\u0014\u0010>\u001a\u00020\b8TX\u0004¢\u0006\u0006\u001a\u0004\b>\u0010=R$\u0010A\u001a\u00020\u00022\u0006\u00107\u001a\u00020\u00028B@BX\u000e¢\u0006\f\u001a\u0004\b?\u00106\"\u0004\b@\u0010\u0005R6\u0010D\u001a\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000$0Bj\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000$`C8\u0002X\u0004¢\u0006\f\n\u0004\bD\u0010E\u0012\u0004\bF\u0010\u0011R$\u0010I\u001a\u00020\u00132\u0006\u00107\u001a\u00020\u00138B@BX\u000e¢\u0006\f\u001a\u0004\bG\u0010\u0015\"\u0004\bH\u0010:¨\u0006K"}, d2 = {"Lkotlinx/coroutines/channels/ArrayBroadcastChannel;", "E", "", "capacity", "<init>", "(I)V", "", "cause", "", "cancel", "(Ljava/lang/Throwable;)Z", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "", "(Ljava/util/concurrent/CancellationException;)V", "cancelInternal", "checkSubOffers", "()V", "close", "", "computeMinHead", "()J", "index", "elementAt", "(J)Ljava/lang/Object;", "element", "", "offerInternal", "(Ljava/lang/Object;)Ljava/lang/Object;", "Lkotlinx/coroutines/selects/SelectInstance;", "select", "offerSelectInternal", "(Ljava/lang/Object;Lkotlinx/coroutines/selects/SelectInstance;)Ljava/lang/Object;", "Lkotlinx/coroutines/channels/ReceiveChannel;", "openSubscription", "()Lkotlinx/coroutines/channels/ReceiveChannel;", "Lkotlinx/coroutines/channels/ArrayBroadcastChannel$Subscriber;", "addSub", "removeSub", "updateHead", "(Lkotlinx/coroutines/channels/ArrayBroadcastChannel$Subscriber;Lkotlinx/coroutines/channels/ArrayBroadcastChannel$Subscriber;)V", "", "buffer", "[Ljava/lang/Object;", "", "getBufferDebugString", "()Ljava/lang/String;", "bufferDebugString", "Ljava/util/concurrent/locks/ReentrantLock;", "Lkotlinx/coroutines/internal/ReentrantLock;", "bufferLock", "Ljava/util/concurrent/locks/ReentrantLock;", "I", "getCapacity", "()I", "value", "getHead", "setHead", "(J)V", "head", "isBufferAlwaysFull", "()Z", "isBufferFull", "getSize", "setSize", "size", "", "Lkotlinx/coroutines/internal/SubscribersList;", "subscribers", "Ljava/util/List;", "getSubscribers$annotations", "getTail", "setTail", "tail", "Subscriber", "kotlinx-coroutines-core", "Lkotlinx/coroutines/channels/AbstractSendChannel;", "Lkotlinx/coroutines/channels/BroadcastChannel;"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: ArrayBroadcastChannel.kt */
public final class ArrayBroadcastChannel<E> extends AbstractSendChannel<E> implements BroadcastChannel<E> {
    private volatile /* synthetic */ long _head;
    private volatile /* synthetic */ int _size;
    private volatile /* synthetic */ long _tail;
    private final Object[] buffer;
    private final ReentrantLock bufferLock;
    private final int capacity;
    private final List<Subscriber<E>> subscribers;

    @Metadata(d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u00028\u00010'2\b\u0012\u0004\u0012\u00028\u00010(B\u0015\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0002¢\u0006\u0004\b\u0004\u0010\u0005J\r\u0010\u0007\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bJ\u0019\u0010\u000b\u001a\u00020\u00062\b\u0010\n\u001a\u0004\u0018\u00010\tH\u0016¢\u0006\u0004\b\u000b\u0010\fJ\u000f\u0010\r\u001a\u00020\u0006H\u0002¢\u0006\u0004\b\r\u0010\bJ\u0011\u0010\u000f\u001a\u0004\u0018\u00010\u000eH\u0002¢\u0006\u0004\b\u000f\u0010\u0010J\u0011\u0010\u0011\u001a\u0004\u0018\u00010\u000eH\u0014¢\u0006\u0004\b\u0011\u0010\u0010J\u001d\u0010\u0014\u001a\u0004\u0018\u00010\u000e2\n\u0010\u0013\u001a\u0006\u0012\u0002\b\u00030\u0012H\u0014¢\u0006\u0004\b\u0014\u0010\u0015R\u001a\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u00028\u0002X\u0004¢\u0006\u0006\n\u0004\b\u0003\u0010\u0016R\u0014\u0010\u0017\u001a\u00020\u00068TX\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\bR\u0014\u0010\u0018\u001a\u00020\u00068TX\u0004¢\u0006\u0006\u001a\u0004\b\u0018\u0010\bR\u0014\u0010\u0019\u001a\u00020\u00068TX\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\bR\u0014\u0010\u001a\u001a\u00020\u00068TX\u0004¢\u0006\u0006\u001a\u0004\b\u001a\u0010\bR$\u0010!\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001b8F@FX\u000e¢\u0006\f\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u0018\u0010$\u001a\u00060\"j\u0002`#8\u0002X\u0004¢\u0006\u0006\n\u0004\b$\u0010%¨\u0006&"}, d2 = {"Lkotlinx/coroutines/channels/ArrayBroadcastChannel$Subscriber;", "E", "Lkotlinx/coroutines/channels/ArrayBroadcastChannel;", "broadcastChannel", "<init>", "(Lkotlinx/coroutines/channels/ArrayBroadcastChannel;)V", "", "checkOffer", "()Z", "", "cause", "close", "(Ljava/lang/Throwable;)Z", "needsToCheckOfferWithoutLock", "", "peekUnderLock", "()Ljava/lang/Object;", "pollInternal", "Lkotlinx/coroutines/selects/SelectInstance;", "select", "pollSelectInternal", "(Lkotlinx/coroutines/selects/SelectInstance;)Ljava/lang/Object;", "Lkotlinx/coroutines/channels/ArrayBroadcastChannel;", "isBufferAlwaysEmpty", "isBufferAlwaysFull", "isBufferEmpty", "isBufferFull", "", "value", "getSubHead", "()J", "setSubHead", "(J)V", "subHead", "Ljava/util/concurrent/locks/ReentrantLock;", "Lkotlinx/coroutines/internal/ReentrantLock;", "subLock", "Ljava/util/concurrent/locks/ReentrantLock;", "kotlinx-coroutines-core", "Lkotlinx/coroutines/channels/AbstractChannel;", "Lkotlinx/coroutines/channels/ReceiveChannel;"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: ArrayBroadcastChannel.kt */
    private static final class Subscriber<E> extends AbstractChannel<E> implements ReceiveChannel<E> {
        private volatile /* synthetic */ long _subHead = 0;
        private final ArrayBroadcastChannel<E> broadcastChannel;
        private final ReentrantLock subLock = new ReentrantLock();

        public Subscriber(ArrayBroadcastChannel<E> broadcastChannel2) {
            super((Function1) null);
            this.broadcastChannel = broadcastChannel2;
        }

        private final boolean needsToCheckOfferWithoutLock() {
            if (getClosedForReceive() != null) {
                return false;
            }
            return !isBufferEmpty() || this.broadcastChannel.getClosedForReceive() != null;
        }

        private final Object peekUnderLock() {
            long subHead = getSubHead();
            Closed<?> closedForReceive = this.broadcastChannel.getClosedForReceive();
            if (subHead >= this.broadcastChannel.getTail()) {
                Closed<?> closedForReceive2 = closedForReceive == null ? getClosedForReceive() : closedForReceive;
                return closedForReceive2 == null ? AbstractChannelKt.POLL_FAILED : closedForReceive2;
            }
            Object access$elementAt = this.broadcastChannel.elementAt(subHead);
            Closed<?> closedForReceive3 = getClosedForReceive();
            return closedForReceive3 != null ? closedForReceive3 : access$elementAt;
        }

        public final boolean checkOffer() {
            boolean z = false;
            Closed closed = null;
            while (true) {
                if (!needsToCheckOfferWithoutLock() || !this.subLock.tryLock()) {
                    break;
                }
                try {
                    Object peekUnderLock = peekUnderLock();
                    if (peekUnderLock != AbstractChannelKt.POLL_FAILED) {
                        if (peekUnderLock instanceof Closed) {
                            closed = (Closed) peekUnderLock;
                            break;
                        }
                        ReceiveOrClosed takeFirstReceiveOrPeekClosed = takeFirstReceiveOrPeekClosed();
                        if (takeFirstReceiveOrPeekClosed == null) {
                            break;
                        }
                        ReceiveOrClosed receiveOrClosed = takeFirstReceiveOrPeekClosed;
                        if (receiveOrClosed instanceof Closed) {
                            break;
                        }
                        Symbol tryResumeReceive = receiveOrClosed.tryResumeReceive(peekUnderLock, (LockFreeLinkedListNode.PrepareOp) null);
                        if (tryResumeReceive != null) {
                            if (DebugKt.getASSERTIONS_ENABLED()) {
                                if (!(tryResumeReceive == CancellableContinuationImplKt.RESUME_TOKEN)) {
                                    throw new AssertionError();
                                }
                            }
                            setSubHead(1 + getSubHead());
                            z = true;
                            this.subLock.unlock();
                            receiveOrClosed.completeResumeReceive(peekUnderLock);
                        }
                    }
                } finally {
                    this.subLock.unlock();
                }
            }
            this.subLock.unlock();
            if (closed != null) {
                close(closed.closeCause);
            }
            return z;
        }

        public boolean close(Throwable cause) {
            boolean close = super.close(cause);
            if (close) {
                ArrayBroadcastChannel.updateHead$default(this.broadcastChannel, (Subscriber) null, this, 1, (Object) null);
                Lock lock = this.subLock;
                lock.lock();
                try {
                    setSubHead(this.broadcastChannel.getTail());
                    Unit unit = Unit.INSTANCE;
                } finally {
                    lock.unlock();
                }
            }
            return close;
        }

        public final long getSubHead() {
            return this._subHead;
        }

        /* access modifiers changed from: protected */
        public boolean isBufferAlwaysEmpty() {
            return false;
        }

        /* access modifiers changed from: protected */
        public boolean isBufferAlwaysFull() {
            throw new IllegalStateException("Should not be used".toString());
        }

        /* access modifiers changed from: protected */
        public boolean isBufferEmpty() {
            return getSubHead() >= this.broadcastChannel.getTail();
        }

        /* access modifiers changed from: protected */
        public boolean isBufferFull() {
            throw new IllegalStateException("Should not be used".toString());
        }

        /* JADX INFO: finally extract failed */
        /* access modifiers changed from: protected */
        public Object pollInternal() {
            boolean z = false;
            Lock lock = this.subLock;
            lock.lock();
            try {
                Object peekUnderLock = peekUnderLock();
                if (!(peekUnderLock instanceof Closed)) {
                    if (peekUnderLock != AbstractChannelKt.POLL_FAILED) {
                        setSubHead(1 + getSubHead());
                        z = true;
                    }
                }
                lock.unlock();
                Object obj = peekUnderLock;
                Closed closed = obj instanceof Closed ? (Closed) obj : null;
                if (closed != null) {
                    close(closed.closeCause);
                }
                if (checkOffer()) {
                    z = true;
                }
                if (z) {
                    ArrayBroadcastChannel.updateHead$default(this.broadcastChannel, (Subscriber) null, (Subscriber) null, 3, (Object) null);
                }
                return obj;
            } catch (Throwable th) {
                lock.unlock();
                throw th;
            }
        }

        /* JADX INFO: finally extract failed */
        /* access modifiers changed from: protected */
        public Object pollSelectInternal(SelectInstance<?> select) {
            boolean z = false;
            Lock lock = this.subLock;
            lock.lock();
            try {
                Object peekUnderLock = peekUnderLock();
                if (!(peekUnderLock instanceof Closed)) {
                    if (peekUnderLock != AbstractChannelKt.POLL_FAILED) {
                        if (!select.trySelect()) {
                            peekUnderLock = SelectKt.getALREADY_SELECTED();
                        } else {
                            setSubHead(1 + getSubHead());
                            z = true;
                        }
                    }
                }
                lock.unlock();
                Object obj = peekUnderLock;
                Closed closed = obj instanceof Closed ? (Closed) obj : null;
                if (closed != null) {
                    close(closed.closeCause);
                }
                if (checkOffer()) {
                    z = true;
                }
                if (z) {
                    ArrayBroadcastChannel.updateHead$default(this.broadcastChannel, (Subscriber) null, (Subscriber) null, 3, (Object) null);
                }
                return obj;
            } catch (Throwable th) {
                lock.unlock();
                throw th;
            }
        }

        public final void setSubHead(long value) {
            this._subHead = value;
        }
    }

    public ArrayBroadcastChannel(int capacity2) {
        super((Function1) null);
        this.capacity = capacity2;
        if (capacity2 < 1 ? false : true) {
            this.bufferLock = new ReentrantLock();
            this.buffer = new Object[capacity2];
            this._head = 0;
            this._tail = 0;
            this._size = 0;
            this.subscribers = ConcurrentKt.subscriberList();
            return;
        }
        throw new IllegalArgumentException(("ArrayBroadcastChannel capacity must be at least 1, but " + getCapacity() + " was specified").toString());
    }

    /* access modifiers changed from: private */
    /* renamed from: cancelInternal */
    public final boolean cancel(Throwable cause) {
        boolean close = close(cause);
        boolean z = close;
        for (Subscriber<E> cancelInternal$kotlinx_coroutines_core : this.subscribers) {
            cancelInternal$kotlinx_coroutines_core.cancel(cause);
        }
        return close;
    }

    private final void checkSubOffers() {
        boolean z = false;
        boolean z2 = false;
        for (Subscriber<E> checkOffer : this.subscribers) {
            z2 = true;
            if (checkOffer.checkOffer()) {
                z = true;
            }
        }
        if (z || !z2) {
            updateHead$default(this, (Subscriber) null, (Subscriber) null, 3, (Object) null);
        }
    }

    private final long computeMinHead() {
        long j = Long.MAX_VALUE;
        for (Subscriber<E> subHead : this.subscribers) {
            j = RangesKt.coerceAtMost(j, subHead.getSubHead());
        }
        return j;
    }

    /* access modifiers changed from: private */
    public final E elementAt(long index) {
        return this.buffer[(int) (index % ((long) this.capacity))];
    }

    private final long getHead() {
        return this._head;
    }

    private final int getSize() {
        return this._size;
    }

    private static /* synthetic */ void getSubscribers$annotations() {
    }

    /* access modifiers changed from: private */
    public final long getTail() {
        return this._tail;
    }

    private final void setHead(long value) {
        this._head = value;
    }

    private final void setSize(int value) {
        this._size = value;
    }

    private final void setTail(long value) {
        this._tail = value;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00b1, code lost:
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() == false) goto L_0x00c7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00b7, code lost:
        if (r2 != kotlinx.coroutines.CancellableContinuationImplKt.RESUME_TOKEN) goto L_0x00bc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00b9, code lost:
        r21 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00bc, code lost:
        r21 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00be, code lost:
        if (r21 == false) goto L_0x00c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00c6, code lost:
        throw new java.lang.AssertionError();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:?, code lost:
        r22 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00cf, code lost:
        r23 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:?, code lost:
        r1.buffer[(int) (r9 % ((long) getCapacity()))] = r4.getPollResult();
        setSize(r15 + 1);
        setTail(r9 + 1);
        r0 = kotlin.Unit.INSTANCE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00fc, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0114, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0115, code lost:
        r23 = r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void updateHead(kotlinx.coroutines.channels.ArrayBroadcastChannel.Subscriber<E> r26, kotlinx.coroutines.channels.ArrayBroadcastChannel.Subscriber<E> r27) {
        /*
            r25 = this;
            r1 = r25
            r2 = r26
            r3 = r27
        L_0x0006:
            r4 = 0
            java.util.concurrent.locks.ReentrantLock r5 = r1.bufferLock
            r6 = 0
            r7 = r5
            java.util.concurrent.locks.Lock r7 = (java.util.concurrent.locks.Lock) r7
            r7.lock()
            r8 = 0
            if (r2 == 0) goto L_0x0032
            long r9 = r25.getTail()     // Catch:{ all -> 0x002b }
            r2.setSubHead(r9)     // Catch:{ all -> 0x002b }
            java.util.List<kotlinx.coroutines.channels.ArrayBroadcastChannel$Subscriber<E>> r9 = r1.subscribers     // Catch:{ all -> 0x002b }
            boolean r9 = r9.isEmpty()     // Catch:{ all -> 0x002b }
            java.util.List<kotlinx.coroutines.channels.ArrayBroadcastChannel$Subscriber<E>> r10 = r1.subscribers     // Catch:{ all -> 0x002b }
            r10.add(r2)     // Catch:{ all -> 0x002b }
            if (r9 != 0) goto L_0x0032
            r7.unlock()
            return
        L_0x002b:
            r0 = move-exception
            r23 = r5
            r24 = r6
            goto L_0x0119
        L_0x0032:
            if (r3 == 0) goto L_0x0049
            java.util.List<kotlinx.coroutines.channels.ArrayBroadcastChannel$Subscriber<E>> r2 = r1.subscribers     // Catch:{ all -> 0x002b }
            r2.remove(r3)     // Catch:{ all -> 0x002b }
            long r9 = r25.getHead()     // Catch:{ all -> 0x002b }
            long r2 = r3.getSubHead()     // Catch:{ all -> 0x002b }
            int r2 = (r9 > r2 ? 1 : (r9 == r2 ? 0 : -1))
            if (r2 == 0) goto L_0x0049
            r7.unlock()
            return
        L_0x0049:
            long r2 = r25.computeMinHead()     // Catch:{ all -> 0x0114 }
            long r9 = r25.getTail()     // Catch:{ all -> 0x0114 }
            long r11 = r25.getHead()     // Catch:{ all -> 0x0114 }
            long r13 = kotlin.ranges.RangesKt.coerceAtMost((long) r2, (long) r9)     // Catch:{ all -> 0x0114 }
            int r15 = (r13 > r11 ? 1 : (r13 == r11 ? 0 : -1))
            if (r15 > 0) goto L_0x0061
            r7.unlock()
            return
        L_0x0061:
            int r15 = r25.getSize()     // Catch:{ all -> 0x0114 }
        L_0x0065:
            int r16 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r16 >= 0) goto L_0x010f
            java.lang.Object[] r0 = r1.buffer     // Catch:{ all -> 0x0114 }
            r17 = r2
            int r2 = r25.getCapacity()     // Catch:{ all -> 0x0114 }
            long r2 = (long) r2     // Catch:{ all -> 0x0114 }
            long r2 = r11 % r2
            int r2 = (int) r2     // Catch:{ all -> 0x0114 }
            r3 = 0
            r0[r2] = r3     // Catch:{ all -> 0x0114 }
            int r0 = r25.getCapacity()     // Catch:{ all -> 0x0114 }
            if (r15 < r0) goto L_0x0080
            r0 = 1
            goto L_0x0081
        L_0x0080:
            r0 = 0
        L_0x0081:
            r19 = 1
            long r11 = r11 + r19
            r1.setHead(r11)     // Catch:{ all -> 0x0114 }
            int r15 = r15 + -1
            r1.setSize(r15)     // Catch:{ all -> 0x0114 }
            if (r0 == 0) goto L_0x0105
        L_0x008f:
            kotlinx.coroutines.channels.Send r21 = r25.takeFirstSendOrPeekClosed()     // Catch:{ all -> 0x0114 }
            if (r21 != 0) goto L_0x0099
            r2 = r17
            goto L_0x0065
        L_0x0099:
            r4 = r21
            boolean r2 = r4 instanceof kotlinx.coroutines.channels.Closed     // Catch:{ all -> 0x0114 }
            if (r2 == 0) goto L_0x00a2
            r2 = r17
            goto L_0x0065
        L_0x00a2:
            r2 = 0
            kotlinx.coroutines.internal.Symbol r16 = r4.tryResumeSend(r2)     // Catch:{ all -> 0x0114 }
            r22 = r16
            r2 = r22
            if (r2 == 0) goto L_0x00fe
            boolean r22 = kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()     // Catch:{ all -> 0x0114 }
            if (r22 == 0) goto L_0x00c7
            r22 = 0
            kotlinx.coroutines.internal.Symbol r3 = kotlinx.coroutines.CancellableContinuationImplKt.RESUME_TOKEN     // Catch:{ all -> 0x002b }
            if (r2 != r3) goto L_0x00bc
            r21 = 1
            goto L_0x00be
        L_0x00bc:
            r21 = 0
        L_0x00be:
            if (r21 == 0) goto L_0x00c1
            goto L_0x00c7
        L_0x00c1:
            java.lang.AssertionError r3 = new java.lang.AssertionError     // Catch:{ all -> 0x002b }
            r3.<init>()     // Catch:{ all -> 0x002b }
            throw r3     // Catch:{ all -> 0x002b }
        L_0x00c7:
            java.lang.Object[] r3 = r1.buffer     // Catch:{ all -> 0x0114 }
            r22 = r0
            int r0 = r25.getCapacity()     // Catch:{ all -> 0x0114 }
            r23 = r5
            r24 = r6
            long r5 = (long) r0
            long r5 = r9 % r5
            int r0 = (int) r5     // Catch:{ all -> 0x00fc }
            java.lang.Object r5 = r4.getPollResult()     // Catch:{ all -> 0x00fc }
            r3[r0] = r5     // Catch:{ all -> 0x00fc }
            int r0 = r15 + 1
            r1.setSize(r0)     // Catch:{ all -> 0x00fc }
            long r5 = r9 + r19
            r1.setTail(r5)     // Catch:{ all -> 0x00fc }
            kotlin.Unit r0 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x00fc }
            r7.unlock()
            r4.completeResumeSend()
            r25.checkSubOffers()
            r2 = 0
            r3 = 0
            goto L_0x0006
        L_0x00fc:
            r0 = move-exception
            goto L_0x0119
        L_0x00fe:
            r22 = r0
            r23 = r5
            r24 = r6
            goto L_0x008f
        L_0x0105:
            r22 = r0
            r23 = r5
            r24 = r6
            r2 = r17
            goto L_0x0065
        L_0x010f:
            r7.unlock()
            return
        L_0x0114:
            r0 = move-exception
            r23 = r5
            r24 = r6
        L_0x0119:
            r7.unlock()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ArrayBroadcastChannel.updateHead(kotlinx.coroutines.channels.ArrayBroadcastChannel$Subscriber, kotlinx.coroutines.channels.ArrayBroadcastChannel$Subscriber):void");
    }

    static /* synthetic */ void updateHead$default(ArrayBroadcastChannel arrayBroadcastChannel, Subscriber subscriber, Subscriber subscriber2, int i, Object obj) {
        if ((i & 1) != 0) {
            subscriber = null;
        }
        if ((i & 2) != 0) {
            subscriber2 = null;
        }
        arrayBroadcastChannel.updateHead(subscriber, subscriber2);
    }

    public void cancel(CancellationException cause) {
        cancel(cause);
    }

    public boolean close(Throwable cause) {
        if (!super.close(cause)) {
            return false;
        }
        checkSubOffers();
        return true;
    }

    /* access modifiers changed from: protected */
    public String getBufferDebugString() {
        return "(buffer:capacity=" + this.buffer.length + ",size=" + getSize() + ')';
    }

    public final int getCapacity() {
        return this.capacity;
    }

    /* access modifiers changed from: protected */
    public boolean isBufferAlwaysFull() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isBufferFull() {
        return getSize() >= this.capacity;
    }

    /* access modifiers changed from: protected */
    public Object offerInternal(E element) {
        Lock lock = this.bufferLock;
        lock.lock();
        try {
            Closed<?> closedForSend = getClosedForSend();
            if (closedForSend == null) {
                int size = getSize();
                if (size >= getCapacity()) {
                    return AbstractChannelKt.OFFER_FAILED;
                }
                long tail = getTail();
                this.buffer[(int) (tail % ((long) getCapacity()))] = element;
                setSize(size + 1);
                setTail(1 + tail);
                Unit unit = Unit.INSTANCE;
                lock.unlock();
                checkSubOffers();
                return AbstractChannelKt.OFFER_SUCCESS;
            }
            lock.unlock();
            return closedForSend;
        } finally {
            lock.unlock();
        }
    }

    /* access modifiers changed from: protected */
    public Object offerSelectInternal(E element, SelectInstance<?> select) {
        Lock lock = this.bufferLock;
        lock.lock();
        try {
            Closed<?> closedForSend = getClosedForSend();
            if (closedForSend == null) {
                int size = getSize();
                if (size >= getCapacity()) {
                    return AbstractChannelKt.OFFER_FAILED;
                }
                if (!select.trySelect()) {
                    Object already_selected = SelectKt.getALREADY_SELECTED();
                    lock.unlock();
                    return already_selected;
                }
                long tail = getTail();
                this.buffer[(int) (tail % ((long) getCapacity()))] = element;
                setSize(size + 1);
                setTail(1 + tail);
                Unit unit = Unit.INSTANCE;
                lock.unlock();
                checkSubOffers();
                return AbstractChannelKt.OFFER_SUCCESS;
            }
            lock.unlock();
            return closedForSend;
        } finally {
            lock.unlock();
        }
    }

    public ReceiveChannel<E> openSubscription() {
        Subscriber subscriber = new Subscriber(this);
        updateHead$default(this, subscriber, (Subscriber) null, 2, (Object) null);
        return subscriber;
    }
}

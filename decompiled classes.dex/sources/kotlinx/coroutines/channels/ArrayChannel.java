package kotlinx.coroutines.channels;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuationImplKt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.OnUndeliveredElementKt;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.UndeliveredElementException;

@Metadata(d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\b\u0010\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00028\u00000BB9\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012 \u0010\t\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0006j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\b¢\u0006\u0004\b\n\u0010\u000bJ\u001f\u0010\u000e\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00022\u0006\u0010\r\u001a\u00028\u0000H\u0002¢\u0006\u0004\b\u000e\u0010\u000fJ\u001d\u0010\u0013\u001a\u00020\u00122\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00028\u00000\u0010H\u0014¢\u0006\u0004\b\u0013\u0010\u0014J\u0019\u0010\u0018\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0016\u001a\u00020\u0015H\u0014¢\u0006\u0004\b\u0018\u0010\u0019J\u0017\u0010\u001a\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0002H\u0002¢\u0006\u0004\b\u001a\u0010\u001bJ\u0017\u0010\u001c\u001a\u00020\u00172\u0006\u0010\r\u001a\u00028\u0000H\u0014¢\u0006\u0004\b\u001c\u0010\u001dJ#\u0010 \u001a\u00020\u00172\u0006\u0010\r\u001a\u00028\u00002\n\u0010\u001f\u001a\u0006\u0012\u0002\b\u00030\u001eH\u0014¢\u0006\u0004\b \u0010!J\u0017\u0010#\u001a\u00020\u00072\u0006\u0010\"\u001a\u00020\u0012H\u0014¢\u0006\u0004\b#\u0010$J\u0011\u0010%\u001a\u0004\u0018\u00010\u0017H\u0014¢\u0006\u0004\b%\u0010&J\u001d\u0010'\u001a\u0004\u0018\u00010\u00172\n\u0010\u001f\u001a\u0006\u0012\u0002\b\u00030\u001eH\u0014¢\u0006\u0004\b'\u0010(J\u0019\u0010*\u001a\u0004\u0018\u00010)2\u0006\u0010\f\u001a\u00020\u0002H\u0002¢\u0006\u0004\b*\u0010+R\u001e\u0010-\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00170,8\u0002@\u0002X\u000e¢\u0006\u0006\n\u0004\b-\u0010.R\u0014\u00102\u001a\u00020/8TX\u0004¢\u0006\u0006\u001a\u0004\b0\u00101R\u0014\u0010\u0003\u001a\u00020\u00028\u0002X\u0004¢\u0006\u0006\n\u0004\b\u0003\u00103R\u0016\u00104\u001a\u00020\u00028\u0002@\u0002X\u000e¢\u0006\u0006\n\u0004\b4\u00103R\u0014\u00105\u001a\u00020\u00128DX\u0004¢\u0006\u0006\u001a\u0004\b5\u00106R\u0014\u00107\u001a\u00020\u00128DX\u0004¢\u0006\u0006\u001a\u0004\b7\u00106R\u0014\u00108\u001a\u00020\u00128DX\u0004¢\u0006\u0006\u001a\u0004\b8\u00106R\u0014\u00109\u001a\u00020\u00128DX\u0004¢\u0006\u0006\u001a\u0004\b9\u00106R\u0014\u0010:\u001a\u00020\u00128VX\u0004¢\u0006\u0006\u001a\u0004\b:\u00106R\u0014\u0010;\u001a\u00020\u00128VX\u0004¢\u0006\u0006\u001a\u0004\b;\u00106R\u0018\u0010>\u001a\u00060<j\u0002`=8\u0002X\u0004¢\u0006\u0006\n\u0004\b>\u0010?R\u0014\u0010\u0005\u001a\u00020\u00048\u0002X\u0004¢\u0006\u0006\n\u0004\b\u0005\u0010@¨\u0006A"}, d2 = {"Lkotlinx/coroutines/channels/ArrayChannel;", "E", "", "capacity", "Lkotlinx/coroutines/channels/BufferOverflow;", "onBufferOverflow", "Lkotlin/Function1;", "", "Lkotlinx/coroutines/internal/OnUndeliveredElement;", "onUndeliveredElement", "<init>", "(ILkotlinx/coroutines/channels/BufferOverflow;Lkotlin/jvm/functions/Function1;)V", "currentSize", "element", "enqueueElement", "(ILjava/lang/Object;)V", "Lkotlinx/coroutines/channels/Receive;", "receive", "", "enqueueReceiveInternal", "(Lkotlinx/coroutines/channels/Receive;)Z", "Lkotlinx/coroutines/channels/Send;", "send", "", "enqueueSend", "(Lkotlinx/coroutines/channels/Send;)Ljava/lang/Object;", "ensureCapacity", "(I)V", "offerInternal", "(Ljava/lang/Object;)Ljava/lang/Object;", "Lkotlinx/coroutines/selects/SelectInstance;", "select", "offerSelectInternal", "(Ljava/lang/Object;Lkotlinx/coroutines/selects/SelectInstance;)Ljava/lang/Object;", "wasClosed", "onCancelIdempotent", "(Z)V", "pollInternal", "()Ljava/lang/Object;", "pollSelectInternal", "(Lkotlinx/coroutines/selects/SelectInstance;)Ljava/lang/Object;", "Lkotlinx/coroutines/internal/Symbol;", "updateBufferSize", "(I)Lkotlinx/coroutines/internal/Symbol;", "", "buffer", "[Ljava/lang/Object;", "", "getBufferDebugString", "()Ljava/lang/String;", "bufferDebugString", "I", "head", "isBufferAlwaysEmpty", "()Z", "isBufferAlwaysFull", "isBufferEmpty", "isBufferFull", "isClosedForReceive", "isEmpty", "Ljava/util/concurrent/locks/ReentrantLock;", "Lkotlinx/coroutines/internal/ReentrantLock;", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "Lkotlinx/coroutines/channels/BufferOverflow;", "kotlinx-coroutines-core", "Lkotlinx/coroutines/channels/AbstractChannel;"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: 0187 */
public class ArrayChannel<E> extends AbstractChannel<E> {
    private Object[] buffer;
    private final int capacity;
    private int head;
    private final ReentrantLock lock;
    private final BufferOverflow onBufferOverflow;
    private volatile /* synthetic */ int size;

    @Metadata(k = 3, mv = {1, 6, 0}, xi = 48)
    /* compiled from: ArrayChannel.kt */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[BufferOverflow.values().length];
            iArr[BufferOverflow.SUSPEND.ordinal()] = 1;
            iArr[BufferOverflow.DROP_LATEST.ordinal()] = 2;
            iArr[BufferOverflow.DROP_OLDEST.ordinal()] = 3;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public ArrayChannel(int capacity2, BufferOverflow onBufferOverflow2, Function1<? super E, Unit> onUndeliveredElement) {
        super(onUndeliveredElement);
        this.capacity = capacity2;
        this.onBufferOverflow = onBufferOverflow2;
        if (capacity2 < 1 ? false : true) {
            this.lock = new ReentrantLock();
            Object[] objArr = new Object[Math.min(capacity2, 8)];
            ArraysKt.fill$default(objArr, (Object) AbstractChannelKt.EMPTY, 0, 0, 6, (Object) null);
            this.buffer = objArr;
            this.size = 0;
            return;
        }
        throw new IllegalArgumentException(("ArrayChannel capacity must be at least 1, but " + capacity2 + " was specified").toString());
    }

    private final void enqueueElement(int currentSize, E element) {
        if (currentSize < this.capacity) {
            ensureCapacity(currentSize);
            Object[] objArr = this.buffer;
            objArr[(this.head + currentSize) % objArr.length] = element;
            return;
        }
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(this.onBufferOverflow == BufferOverflow.DROP_OLDEST)) {
                throw new AssertionError();
            }
        }
        Object[] objArr2 = this.buffer;
        int i = this.head;
        objArr2[i % objArr2.length] = null;
        objArr2[(i + currentSize) % objArr2.length] = element;
        this.head = (i + 1) % objArr2.length;
    }

    private final void ensureCapacity(int currentSize) {
        Object[] objArr = this.buffer;
        if (currentSize >= objArr.length) {
            int min = Math.min(objArr.length * 2, this.capacity);
            Object[] objArr2 = new Object[min];
            int i = 0;
            while (i < currentSize) {
                int i2 = i;
                i++;
                Object[] objArr3 = this.buffer;
                objArr2[i2] = objArr3[(this.head + i2) % objArr3.length];
            }
            ArraysKt.fill((T[]) objArr2, AbstractChannelKt.EMPTY, currentSize, min);
            this.buffer = objArr2;
            this.head = 0;
        }
    }

    private final Symbol updateBufferSize(int currentSize) {
        if (currentSize < this.capacity) {
            this.size = currentSize + 1;
            return null;
        }
        switch (WhenMappings.$EnumSwitchMapping$0[this.onBufferOverflow.ordinal()]) {
            case 1:
                return AbstractChannelKt.OFFER_FAILED;
            case 2:
                return AbstractChannelKt.OFFER_SUCCESS;
            case 3:
                return null;
            default:
                throw new NoWhenBranchMatchedException();
        }
    }

    /* access modifiers changed from: protected */
    public boolean enqueueReceiveInternal(Receive<? super E> receive) {
        Lock lock2 = this.lock;
        lock2.lock();
        try {
            return super.enqueueReceiveInternal(receive);
        } finally {
            lock2.unlock();
        }
    }

    /* access modifiers changed from: protected */
    public Object enqueueSend(Send send) {
        Lock lock2 = this.lock;
        lock2.lock();
        try {
            return super.enqueueSend(send);
        } finally {
            lock2.unlock();
        }
    }

    /* access modifiers changed from: protected */
    public String getBufferDebugString() {
        return "(buffer:capacity=" + this.capacity + ",size=" + this.size + ')';
    }

    /* access modifiers changed from: protected */
    public final boolean isBufferAlwaysEmpty() {
        return false;
    }

    /* access modifiers changed from: protected */
    public final boolean isBufferAlwaysFull() {
        return false;
    }

    /* access modifiers changed from: protected */
    public final boolean isBufferEmpty() {
        return this.size == 0;
    }

    /* access modifiers changed from: protected */
    public final boolean isBufferFull() {
        return this.size == this.capacity && this.onBufferOverflow == BufferOverflow.SUSPEND;
    }

    public boolean isClosedForReceive() {
        Lock lock2 = this.lock;
        lock2.lock();
        try {
            return super.isClosedForReceive();
        } finally {
            lock2.unlock();
        }
    }

    public boolean isEmpty() {
        Lock lock2 = this.lock;
        lock2.lock();
        try {
            return isEmptyImpl();
        } finally {
            lock2.unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: protected */
    public Object offerInternal(E element) {
        ReceiveOrClosed receiveOrClosed;
        Symbol tryResumeReceive;
        Lock lock2 = this.lock;
        lock2.lock();
        try {
            int i = this.size;
            Closed<?> closedForSend = getClosedForSend();
            if (closedForSend == null) {
                Symbol updateBufferSize = updateBufferSize(i);
                if (updateBufferSize == null) {
                    if (i == 0) {
                        do {
                            ReceiveOrClosed takeFirstReceiveOrPeekClosed = takeFirstReceiveOrPeekClosed();
                            if (takeFirstReceiveOrPeekClosed != null) {
                                receiveOrClosed = takeFirstReceiveOrPeekClosed;
                                if (receiveOrClosed instanceof Closed) {
                                    this.size = i;
                                    lock2.unlock();
                                    return receiveOrClosed;
                                }
                                tryResumeReceive = receiveOrClosed.tryResumeReceive(element, (LockFreeLinkedListNode.PrepareOp) null);
                            }
                        } while (tryResumeReceive == null);
                        if (DebugKt.getASSERTIONS_ENABLED()) {
                            if (!(tryResumeReceive == CancellableContinuationImplKt.RESUME_TOKEN)) {
                                throw new AssertionError();
                            }
                        }
                        this.size = i;
                        Unit unit = Unit.INSTANCE;
                        lock2.unlock();
                        receiveOrClosed.completeResumeReceive(element);
                        return receiveOrClosed.getOfferResult();
                    }
                    enqueueElement(i, element);
                    Symbol symbol = AbstractChannelKt.OFFER_SUCCESS;
                    lock2.unlock();
                    return symbol;
                }
                lock2.unlock();
                return updateBufferSize;
            }
            lock2.unlock();
            return closedForSend;
        } catch (Throwable th) {
            lock2.unlock();
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0057, code lost:
        if (r7 == kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED()) goto L_0x0071;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005b, code lost:
        if ((r7 instanceof kotlinx.coroutines.channels.Closed) == false) goto L_0x005e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x005e, code lost:
        r9 = kotlin.jvm.internal.Intrinsics.stringPlus("performAtomicTrySelect(describeTryOffer) returned ", r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0066, code lost:
        mt.Log1F380D.a((java.lang.Object) r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0070, code lost:
        throw new java.lang.IllegalStateException(r9.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0071, code lost:
        r10.size = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0077, code lost:
        return r7;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object offerSelectInternal(E r11, kotlinx.coroutines.selects.SelectInstance<?> r12) {
        /*
            r10 = this;
            r0 = 0
            java.util.concurrent.locks.ReentrantLock r1 = r10.lock
            r2 = 0
            r3 = r1
            java.util.concurrent.locks.Lock r3 = (java.util.concurrent.locks.Lock) r3
            r3.lock()
            r4 = 0
            int r5 = r10.size     // Catch:{ all -> 0x009d }
            kotlinx.coroutines.channels.Closed r6 = r10.getClosedForSend()     // Catch:{ all -> 0x009d }
            if (r6 != 0) goto L_0x0097
            kotlinx.coroutines.internal.Symbol r6 = r10.updateBufferSize(r5)     // Catch:{ all -> 0x009d }
            if (r6 != 0) goto L_0x0091
            if (r5 != 0) goto L_0x0078
        L_0x001b:
            kotlinx.coroutines.channels.AbstractSendChannel$TryOfferDesc r6 = r10.describeTryOffer(r11)     // Catch:{ all -> 0x009d }
            r7 = r6
            kotlinx.coroutines.internal.AtomicDesc r7 = (kotlinx.coroutines.internal.AtomicDesc) r7     // Catch:{ all -> 0x009d }
            java.lang.Object r7 = r12.performAtomicTrySelect(r7)     // Catch:{ all -> 0x009d }
            if (r7 != 0) goto L_0x0049
            r10.size = r5     // Catch:{ all -> 0x009d }
            java.lang.Object r8 = r6.getResult()     // Catch:{ all -> 0x009d }
            r0 = r8
            kotlin.Unit r4 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x009d }
            r3.unlock()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            r1 = r0
            kotlinx.coroutines.channels.ReceiveOrClosed r1 = (kotlinx.coroutines.channels.ReceiveOrClosed) r1
            r1.completeResumeReceive(r11)
            r1 = r0
            kotlinx.coroutines.channels.ReceiveOrClosed r1 = (kotlinx.coroutines.channels.ReceiveOrClosed) r1
            java.lang.Object r1 = r1.getOfferResult()
            return r1
        L_0x0049:
            kotlinx.coroutines.internal.Symbol r8 = kotlinx.coroutines.channels.AbstractChannelKt.OFFER_FAILED     // Catch:{ all -> 0x009d }
            if (r7 != r8) goto L_0x004e
            goto L_0x0078
        L_0x004e:
            java.lang.Object r8 = kotlinx.coroutines.internal.AtomicKt.RETRY_ATOMIC     // Catch:{ all -> 0x009d }
            if (r7 != r8) goto L_0x0053
            goto L_0x001b
        L_0x0053:
            java.lang.Object r8 = kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED()     // Catch:{ all -> 0x009d }
            if (r7 == r8) goto L_0x0071
            boolean r8 = r7 instanceof kotlinx.coroutines.channels.Closed     // Catch:{ all -> 0x009d }
            if (r8 == 0) goto L_0x005e
            goto L_0x0071
        L_0x005e:
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException     // Catch:{ all -> 0x009d }
            java.lang.String r9 = "performAtomicTrySelect(describeTryOffer) returned "
            java.lang.String r9 = kotlin.jvm.internal.Intrinsics.stringPlus(r9, r7)     // Catch:{ all -> 0x009d }
            mt.Log1F380D.a((java.lang.Object) r9)
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x009d }
            r8.<init>(r9)     // Catch:{ all -> 0x009d }
            throw r8     // Catch:{ all -> 0x009d }
        L_0x0071:
            r10.size = r5     // Catch:{ all -> 0x009d }
            r3.unlock()
            return r7
        L_0x0078:
            boolean r6 = r12.trySelect()     // Catch:{ all -> 0x009d }
            if (r6 != 0) goto L_0x0088
            r10.size = r5     // Catch:{ all -> 0x009d }
            java.lang.Object r6 = kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED()     // Catch:{ all -> 0x009d }
            r3.unlock()
            return r6
        L_0x0088:
            r10.enqueueElement(r5, r11)     // Catch:{ all -> 0x009d }
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.channels.AbstractChannelKt.OFFER_SUCCESS     // Catch:{ all -> 0x009d }
            r3.unlock()
            return r6
        L_0x0091:
            r7 = 0
            r3.unlock()
            return r6
        L_0x0097:
            r7 = 0
            r3.unlock()
            return r6
        L_0x009d:
            r4 = move-exception
            r3.unlock()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ArrayChannel.offerSelectInternal(java.lang.Object, kotlinx.coroutines.selects.SelectInstance):java.lang.Object");
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: protected */
    public void onCancelIdempotent(boolean wasClosed) {
        Function1 function1 = this.onUndeliveredElement;
        UndeliveredElementException undeliveredElementException = null;
        Lock lock2 = this.lock;
        lock2.lock();
        try {
            int i = this.size;
            int i2 = 0;
            while (i2 < i) {
                int i3 = i2 + 1;
                Object obj = this.buffer[this.head];
                if (!(function1 == null || obj == AbstractChannelKt.EMPTY)) {
                    undeliveredElementException = OnUndeliveredElementKt.callUndeliveredElementCatchingException(function1, obj, undeliveredElementException);
                }
                this.buffer[this.head] = AbstractChannelKt.EMPTY;
                this.head = (this.head + 1) % this.buffer.length;
                i2 = i3;
            }
            this.size = 0;
            Unit unit = Unit.INSTANCE;
            lock2.unlock();
            super.onCancelIdempotent(wasClosed);
            if (undeliveredElementException != null) {
                throw undeliveredElementException;
            }
        } catch (Throwable th) {
            lock2.unlock();
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public Object pollInternal() {
        Send send = null;
        boolean z = false;
        Lock lock2 = this.lock;
        lock2.lock();
        try {
            int i = this.size;
            if (i == 0) {
                Object closedForSend = getClosedForSend();
                if (closedForSend == null) {
                    closedForSend = AbstractChannelKt.POLL_FAILED;
                }
                return closedForSend;
            }
            Object[] objArr = this.buffer;
            int i2 = this.head;
            Object obj = objArr[i2];
            objArr[i2] = null;
            this.size = i - 1;
            Object obj2 = AbstractChannelKt.POLL_FAILED;
            if (i == this.capacity) {
                while (true) {
                    Send takeFirstSendOrPeekClosed = takeFirstSendOrPeekClosed();
                    if (takeFirstSendOrPeekClosed == null) {
                        break;
                    }
                    send = takeFirstSendOrPeekClosed;
                    Symbol tryResumeSend = send.tryResumeSend((LockFreeLinkedListNode.PrepareOp) null);
                    if (tryResumeSend != null) {
                        if (DebugKt.getASSERTIONS_ENABLED()) {
                            if (!(tryResumeSend == CancellableContinuationImplKt.RESUME_TOKEN)) {
                                throw new AssertionError();
                            }
                        }
                        z = true;
                        obj2 = send.getPollResult();
                    } else {
                        send.undeliveredElement();
                    }
                }
            }
            if (obj2 != AbstractChannelKt.POLL_FAILED && !(obj2 instanceof Closed)) {
                this.size = i;
                Object[] objArr2 = this.buffer;
                objArr2[(this.head + i) % objArr2.length] = obj2;
            }
            this.head = (this.head + 1) % this.buffer.length;
            Unit unit = Unit.INSTANCE;
            lock2.unlock();
            if (z) {
                Intrinsics.checkNotNull(send);
                send.completeResumeSend();
            }
            return obj;
        } finally {
            lock2.unlock();
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0060, code lost:
        if (r10 != kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED()) goto L_0x006f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0062, code lost:
        r13.size = r7;
        r13.buffer[r13.head] = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x006a, code lost:
        r5.unlock();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x006e, code lost:
        return r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0071, code lost:
        if ((r10 instanceof kotlinx.coroutines.channels.Closed) == false) goto L_0x0077;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0073, code lost:
        r0 = r10;
        r1 = true;
        r8 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0077, code lost:
        r12 = kotlin.jvm.internal.Intrinsics.stringPlus("performAtomicTrySelect(describeTryOffer) returned ", r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x007f, code lost:
        mt.Log1F380D.a((java.lang.Object) r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0089, code lost:
        throw new java.lang.IllegalStateException(r12.toString());
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object pollSelectInternal(kotlinx.coroutines.selects.SelectInstance<?> r14) {
        /*
            r13 = this;
            r0 = 0
            r1 = 0
            r2 = 0
            java.util.concurrent.locks.ReentrantLock r3 = r13.lock
            r4 = 0
            r5 = r3
            java.util.concurrent.locks.Lock r5 = (java.util.concurrent.locks.Lock) r5
            r5.lock()
            r6 = 0
            int r7 = r13.size     // Catch:{ all -> 0x00d1 }
            if (r7 != 0) goto L_0x001d
            kotlinx.coroutines.channels.Closed r8 = r13.getClosedForSend()     // Catch:{ all -> 0x00d1 }
            if (r8 != 0) goto L_0x0019
            kotlinx.coroutines.internal.Symbol r8 = kotlinx.coroutines.channels.AbstractChannelKt.POLL_FAILED     // Catch:{ all -> 0x00d1 }
        L_0x0019:
            r5.unlock()
            return r8
        L_0x001d:
            java.lang.Object[] r8 = r13.buffer     // Catch:{ all -> 0x00d1 }
            int r9 = r13.head     // Catch:{ all -> 0x00d1 }
            r10 = r8[r9]     // Catch:{ all -> 0x00d1 }
            r2 = r10
            r10 = 0
            r8[r9] = r10     // Catch:{ all -> 0x00d1 }
            int r8 = r7 + -1
            r13.size = r8     // Catch:{ all -> 0x00d1 }
            kotlinx.coroutines.internal.Symbol r8 = kotlinx.coroutines.channels.AbstractChannelKt.POLL_FAILED     // Catch:{ all -> 0x00d1 }
            int r9 = r13.capacity     // Catch:{ all -> 0x00d1 }
            if (r7 != r9) goto L_0x008a
        L_0x0031:
            kotlinx.coroutines.channels.AbstractChannel$TryPollDesc r9 = r13.describeTryPoll()     // Catch:{ all -> 0x00d1 }
            r10 = r9
            kotlinx.coroutines.internal.AtomicDesc r10 = (kotlinx.coroutines.internal.AtomicDesc) r10     // Catch:{ all -> 0x00d1 }
            java.lang.Object r10 = r14.performAtomicTrySelect(r10)     // Catch:{ all -> 0x00d1 }
            if (r10 != 0) goto L_0x0052
            java.lang.Object r11 = r9.getResult()     // Catch:{ all -> 0x00d1 }
            r0 = r11
            r1 = 1
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)     // Catch:{ all -> 0x00d1 }
            r11 = r0
            kotlinx.coroutines.channels.Send r11 = (kotlinx.coroutines.channels.Send) r11     // Catch:{ all -> 0x00d1 }
            java.lang.Object r11 = r11.getPollResult()     // Catch:{ all -> 0x00d1 }
            r8 = r11
            goto L_0x008a
        L_0x0052:
            kotlinx.coroutines.internal.Symbol r11 = kotlinx.coroutines.channels.AbstractChannelKt.POLL_FAILED     // Catch:{ all -> 0x00d1 }
            if (r10 != r11) goto L_0x0057
            goto L_0x008a
        L_0x0057:
            java.lang.Object r11 = kotlinx.coroutines.internal.AtomicKt.RETRY_ATOMIC     // Catch:{ all -> 0x00d1 }
            if (r10 != r11) goto L_0x005c
            goto L_0x0031
        L_0x005c:
            java.lang.Object r11 = kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED()     // Catch:{ all -> 0x00d1 }
            if (r10 != r11) goto L_0x006f
            r13.size = r7     // Catch:{ all -> 0x00d1 }
            java.lang.Object[] r11 = r13.buffer     // Catch:{ all -> 0x00d1 }
            int r12 = r13.head     // Catch:{ all -> 0x00d1 }
            r11[r12] = r2     // Catch:{ all -> 0x00d1 }
            r5.unlock()
            return r10
        L_0x006f:
            boolean r11 = r10 instanceof kotlinx.coroutines.channels.Closed     // Catch:{ all -> 0x00d1 }
            if (r11 == 0) goto L_0x0077
            r0 = r10
            r1 = 1
            r8 = r10
            goto L_0x008a
        L_0x0077:
            java.lang.IllegalStateException r11 = new java.lang.IllegalStateException     // Catch:{ all -> 0x00d1 }
            java.lang.String r12 = "performAtomicTrySelect(describeTryOffer) returned "
            java.lang.String r12 = kotlin.jvm.internal.Intrinsics.stringPlus(r12, r10)     // Catch:{ all -> 0x00d1 }
            mt.Log1F380D.a((java.lang.Object) r12)
            java.lang.String r12 = r12.toString()     // Catch:{ all -> 0x00d1 }
            r11.<init>(r12)     // Catch:{ all -> 0x00d1 }
            throw r11     // Catch:{ all -> 0x00d1 }
        L_0x008a:
            kotlinx.coroutines.internal.Symbol r9 = kotlinx.coroutines.channels.AbstractChannelKt.POLL_FAILED     // Catch:{ all -> 0x00d1 }
            if (r8 == r9) goto L_0x009e
            boolean r9 = r8 instanceof kotlinx.coroutines.channels.Closed     // Catch:{ all -> 0x00d1 }
            if (r9 != 0) goto L_0x009e
            r13.size = r7     // Catch:{ all -> 0x00d1 }
            java.lang.Object[] r9 = r13.buffer     // Catch:{ all -> 0x00d1 }
            int r10 = r13.head     // Catch:{ all -> 0x00d1 }
            int r10 = r10 + r7
            int r11 = r9.length     // Catch:{ all -> 0x00d1 }
            int r10 = r10 % r11
            r9[r10] = r8     // Catch:{ all -> 0x00d1 }
            goto L_0x00b4
        L_0x009e:
            boolean r9 = r14.trySelect()     // Catch:{ all -> 0x00d1 }
            if (r9 != 0) goto L_0x00b4
            r13.size = r7     // Catch:{ all -> 0x00d1 }
            java.lang.Object[] r9 = r13.buffer     // Catch:{ all -> 0x00d1 }
            int r10 = r13.head     // Catch:{ all -> 0x00d1 }
            r9[r10] = r2     // Catch:{ all -> 0x00d1 }
            java.lang.Object r9 = kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED()     // Catch:{ all -> 0x00d1 }
            r5.unlock()
            return r9
        L_0x00b4:
            int r9 = r13.head     // Catch:{ all -> 0x00d1 }
            int r9 = r9 + 1
            java.lang.Object[] r10 = r13.buffer     // Catch:{ all -> 0x00d1 }
            int r10 = r10.length     // Catch:{ all -> 0x00d1 }
            int r9 = r9 % r10
            r13.head = r9     // Catch:{ all -> 0x00d1 }
            kotlin.Unit r6 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x00d1 }
            r5.unlock()
            if (r1 == 0) goto L_0x00d0
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            r3 = r0
            kotlinx.coroutines.channels.Send r3 = (kotlinx.coroutines.channels.Send) r3
            r3.completeResumeSend()
        L_0x00d0:
            return r2
        L_0x00d1:
            r6 = move-exception
            r5.unlock()
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ArrayChannel.pollSelectInternal(kotlinx.coroutines.selects.SelectInstance):java.lang.Object");
    }
}

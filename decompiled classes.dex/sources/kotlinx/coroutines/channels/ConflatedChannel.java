package kotlinx.coroutines.channels;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.CancellableContinuationImplKt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.OnUndeliveredElementKt;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.UndeliveredElementException;
import kotlinx.coroutines.selects.SelectInstance;
import kotlinx.coroutines.selects.SelectKt;

@Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\b\u0010\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B'\u0012 \u0010\u0003\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\u0006¢\u0006\u0002\u0010\u0007J\u0016\u0010\u0018\u001a\u00020\r2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00028\u00000\u001aH\u0014J\u0015\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010\u001dJ!\u0010\u001e\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00028\u00002\n\u0010\u001f\u001a\u0006\u0012\u0002\b\u00030 H\u0014¢\u0006\u0002\u0010!J\u0010\u0010\"\u001a\u00020\u00052\u0006\u0010#\u001a\u00020\rH\u0014J\n\u0010$\u001a\u0004\u0018\u00010\u0017H\u0014J\u0016\u0010%\u001a\u0004\u0018\u00010\u00172\n\u0010\u001f\u001a\u0006\u0012\u0002\b\u00030 H\u0014J\u0014\u0010&\u001a\u0004\u0018\u00010'2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0017H\u0002R\u0014\u0010\b\u001a\u00020\t8TX\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\r8DX\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\u000eR\u0014\u0010\u000f\u001a\u00020\r8DX\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u000eR\u0014\u0010\u0010\u001a\u00020\r8DX\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u000eR\u0014\u0010\u0011\u001a\u00020\r8DX\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u000eR\u0014\u0010\u0012\u001a\u00020\r8VX\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u000eR\u0012\u0010\u0013\u001a\u00060\u0014j\u0002`\u0015X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u000e¢\u0006\u0002\n\u0000¨\u0006("}, d2 = {"Lkotlinx/coroutines/channels/ConflatedChannel;", "E", "Lkotlinx/coroutines/channels/AbstractChannel;", "onUndeliveredElement", "Lkotlin/Function1;", "", "Lkotlinx/coroutines/internal/OnUndeliveredElement;", "(Lkotlin/jvm/functions/Function1;)V", "bufferDebugString", "", "getBufferDebugString", "()Ljava/lang/String;", "isBufferAlwaysEmpty", "", "()Z", "isBufferAlwaysFull", "isBufferEmpty", "isBufferFull", "isEmpty", "lock", "Ljava/util/concurrent/locks/ReentrantLock;", "Lkotlinx/coroutines/internal/ReentrantLock;", "value", "", "enqueueReceiveInternal", "receive", "Lkotlinx/coroutines/channels/Receive;", "offerInternal", "element", "(Ljava/lang/Object;)Ljava/lang/Object;", "offerSelectInternal", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "(Ljava/lang/Object;Lkotlinx/coroutines/selects/SelectInstance;)Ljava/lang/Object;", "onCancelIdempotent", "wasClosed", "pollInternal", "pollSelectInternal", "updateValueLocked", "Lkotlinx/coroutines/internal/UndeliveredElementException;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: 018B */
public class ConflatedChannel<E> extends AbstractChannel<E> {
    private final ReentrantLock lock = new ReentrantLock();
    private Object value = AbstractChannelKt.EMPTY;

    public ConflatedChannel(Function1<? super E, Unit> onUndeliveredElement) {
        super(onUndeliveredElement);
    }

    private final UndeliveredElementException updateValueLocked(Object element) {
        Function1 function1;
        Object obj = this.value;
        UndeliveredElementException undeliveredElementException = null;
        if (!(obj == AbstractChannelKt.EMPTY || (function1 = this.onUndeliveredElement) == null)) {
            undeliveredElementException = OnUndeliveredElementKt.callUndeliveredElementCatchingException$default(function1, obj, (UndeliveredElementException) null, 2, (Object) null);
        }
        UndeliveredElementException undeliveredElementException2 = undeliveredElementException;
        this.value = element;
        return undeliveredElementException2;
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
    public String getBufferDebugString() {
        return "(value=" + this.value + ')';
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
        return this.value == AbstractChannelKt.EMPTY;
    }

    /* access modifiers changed from: protected */
    public final boolean isBufferFull() {
        return false;
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
            Closed<?> closedForSend = getClosedForSend();
            if (closedForSend == null) {
                if (this.value == AbstractChannelKt.EMPTY) {
                    do {
                        ReceiveOrClosed takeFirstReceiveOrPeekClosed = takeFirstReceiveOrPeekClosed();
                        if (takeFirstReceiveOrPeekClosed != null) {
                            receiveOrClosed = takeFirstReceiveOrPeekClosed;
                            if (receiveOrClosed instanceof Closed) {
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
                    Unit unit = Unit.INSTANCE;
                    lock2.unlock();
                    receiveOrClosed.completeResumeReceive(element);
                    return receiveOrClosed.getOfferResult();
                }
                UndeliveredElementException updateValueLocked = updateValueLocked(element);
                if (updateValueLocked == null) {
                    Symbol symbol = AbstractChannelKt.OFFER_SUCCESS;
                    lock2.unlock();
                    return symbol;
                }
                throw updateValueLocked;
            }
            lock2.unlock();
            return closedForSend;
        } catch (Throwable th) {
            lock2.unlock();
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0051, code lost:
        if (r6 == kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED()) goto L_0x006b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0055, code lost:
        if ((r6 instanceof kotlinx.coroutines.channels.Closed) == false) goto L_0x0058;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0058, code lost:
        r8 = kotlin.jvm.internal.Intrinsics.stringPlus("performAtomicTrySelect(describeTryOffer) returned ", r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0060, code lost:
        mt.Log1F380D.a((java.lang.Object) r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x006a, code lost:
        throw new java.lang.IllegalStateException(r8.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x006e, code lost:
        return r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object offerSelectInternal(E r10, kotlinx.coroutines.selects.SelectInstance<?> r11) {
        /*
            r9 = this;
            r0 = 0
            java.util.concurrent.locks.ReentrantLock r1 = r9.lock
            r2 = 0
            r3 = r1
            java.util.concurrent.locks.Lock r3 = (java.util.concurrent.locks.Lock) r3
            r3.lock()
            r4 = 0
            kotlinx.coroutines.channels.Closed r5 = r9.getClosedForSend()     // Catch:{ all -> 0x0092 }
            if (r5 != 0) goto L_0x008c
            java.lang.Object r5 = r9.value     // Catch:{ all -> 0x0092 }
            kotlinx.coroutines.internal.Symbol r6 = kotlinx.coroutines.channels.AbstractChannelKt.EMPTY     // Catch:{ all -> 0x0092 }
            if (r5 != r6) goto L_0x006f
        L_0x0017:
            kotlinx.coroutines.channels.AbstractSendChannel$TryOfferDesc r5 = r9.describeTryOffer(r10)     // Catch:{ all -> 0x0092 }
            r6 = r5
            kotlinx.coroutines.internal.AtomicDesc r6 = (kotlinx.coroutines.internal.AtomicDesc) r6     // Catch:{ all -> 0x0092 }
            java.lang.Object r6 = r11.performAtomicTrySelect(r6)     // Catch:{ all -> 0x0092 }
            if (r6 != 0) goto L_0x0043
            java.lang.Object r7 = r5.getResult()     // Catch:{ all -> 0x0092 }
            r0 = r7
            kotlin.Unit r4 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x0092 }
            r3.unlock()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            r1 = r0
            kotlinx.coroutines.channels.ReceiveOrClosed r1 = (kotlinx.coroutines.channels.ReceiveOrClosed) r1
            r1.completeResumeReceive(r10)
            r1 = r0
            kotlinx.coroutines.channels.ReceiveOrClosed r1 = (kotlinx.coroutines.channels.ReceiveOrClosed) r1
            java.lang.Object r1 = r1.getOfferResult()
            return r1
        L_0x0043:
            kotlinx.coroutines.internal.Symbol r7 = kotlinx.coroutines.channels.AbstractChannelKt.OFFER_FAILED     // Catch:{ all -> 0x0092 }
            if (r6 != r7) goto L_0x0048
            goto L_0x006f
        L_0x0048:
            java.lang.Object r7 = kotlinx.coroutines.internal.AtomicKt.RETRY_ATOMIC     // Catch:{ all -> 0x0092 }
            if (r6 != r7) goto L_0x004d
            goto L_0x0017
        L_0x004d:
            java.lang.Object r7 = kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED()     // Catch:{ all -> 0x0092 }
            if (r6 == r7) goto L_0x006b
            boolean r7 = r6 instanceof kotlinx.coroutines.channels.Closed     // Catch:{ all -> 0x0092 }
            if (r7 == 0) goto L_0x0058
            goto L_0x006b
        L_0x0058:
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0092 }
            java.lang.String r8 = "performAtomicTrySelect(describeTryOffer) returned "
            java.lang.String r8 = kotlin.jvm.internal.Intrinsics.stringPlus(r8, r6)     // Catch:{ all -> 0x0092 }
            mt.Log1F380D.a((java.lang.Object) r8)
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x0092 }
            r7.<init>(r8)     // Catch:{ all -> 0x0092 }
            throw r7     // Catch:{ all -> 0x0092 }
        L_0x006b:
            r3.unlock()
            return r6
        L_0x006f:
            boolean r5 = r11.trySelect()     // Catch:{ all -> 0x0092 }
            if (r5 != 0) goto L_0x007d
            java.lang.Object r5 = kotlinx.coroutines.selects.SelectKt.getALREADY_SELECTED()     // Catch:{ all -> 0x0092 }
            r3.unlock()
            return r5
        L_0x007d:
            kotlinx.coroutines.internal.UndeliveredElementException r5 = r9.updateValueLocked(r10)     // Catch:{ all -> 0x0092 }
            if (r5 != 0) goto L_0x0089
            kotlinx.coroutines.internal.Symbol r5 = kotlinx.coroutines.channels.AbstractChannelKt.OFFER_SUCCESS     // Catch:{ all -> 0x0092 }
            r3.unlock()
            return r5
        L_0x0089:
            r6 = 0
            throw r5     // Catch:{ all -> 0x0092 }
        L_0x008c:
            r6 = 0
            r3.unlock()
            return r5
        L_0x0092:
            r4 = move-exception
            r3.unlock()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.ConflatedChannel.offerSelectInternal(java.lang.Object, kotlinx.coroutines.selects.SelectInstance):java.lang.Object");
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: protected */
    public void onCancelIdempotent(boolean wasClosed) {
        Lock lock2 = this.lock;
        lock2.lock();
        try {
            UndeliveredElementException updateValueLocked = updateValueLocked(AbstractChannelKt.EMPTY);
            Unit unit = Unit.INSTANCE;
            lock2.unlock();
            super.onCancelIdempotent(wasClosed);
            if (updateValueLocked != null) {
                throw updateValueLocked;
            }
        } catch (Throwable th) {
            lock2.unlock();
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public Object pollInternal() {
        Lock lock2 = this.lock;
        lock2.lock();
        try {
            if (this.value == AbstractChannelKt.EMPTY) {
                Object closedForSend = getClosedForSend();
                if (closedForSend == null) {
                    closedForSend = AbstractChannelKt.POLL_FAILED;
                }
                return closedForSend;
            }
            Object obj = this.value;
            this.value = AbstractChannelKt.EMPTY;
            Unit unit = Unit.INSTANCE;
            lock2.unlock();
            return obj;
        } finally {
            lock2.unlock();
        }
    }

    /* access modifiers changed from: protected */
    public Object pollSelectInternal(SelectInstance<?> select) {
        Lock lock2 = this.lock;
        lock2.lock();
        try {
            if (this.value == AbstractChannelKt.EMPTY) {
                Object closedForSend = getClosedForSend();
                if (closedForSend == null) {
                    closedForSend = AbstractChannelKt.POLL_FAILED;
                }
                return closedForSend;
            } else if (!select.trySelect()) {
                Object already_selected = SelectKt.getALREADY_SELECTED();
                lock2.unlock();
                return already_selected;
            } else {
                Object obj = this.value;
                this.value = AbstractChannelKt.EMPTY;
                Unit unit = Unit.INSTANCE;
                lock2.unlock();
                return obj;
            }
        } finally {
            lock2.unlock();
        }
    }
}

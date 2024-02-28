package kotlinx.coroutines.channels;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CancellableContinuationImplKt;
import kotlinx.coroutines.CancellableContinuationKt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.DebugStringsKt;
import kotlinx.coroutines.DisposableHandle;
import kotlinx.coroutines.channels.SendChannel;
import kotlinx.coroutines.internal.AtomicKt;
import kotlinx.coroutines.internal.InlineList;
import kotlinx.coroutines.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.LockFreeLinkedList_commonKt;
import kotlinx.coroutines.internal.OnUndeliveredElementKt;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.UndeliveredElementException;
import kotlinx.coroutines.intrinsics.CancellableKt;
import kotlinx.coroutines.intrinsics.UndispatchedKt;
import kotlinx.coroutines.selects.SelectClause2;
import kotlinx.coroutines.selects.SelectInstance;
import kotlinx.coroutines.selects.SelectKt;
import mt.Log1F380D;
import okhttp3.HttpUrl;

@Metadata(d1 = {"\u0000¦\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\b \u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u00028\u000006:\u0004defgB)\u0012 \u0010\u0005\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u0002j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\u0004¢\u0006\u0004\b\u0006\u0010\u0007J\u0019\u0010\u000b\u001a\u00020\n2\b\u0010\t\u001a\u0004\u0018\u00010\bH\u0016¢\u0006\u0004\b\u000b\u0010\fJ\u000f\u0010\u000e\u001a\u00020\rH\u0002¢\u0006\u0004\b\u000e\u0010\u000fJ#\u0010\u0013\u001a\u000e\u0012\u0002\b\u00030\u0011j\u0006\u0012\u0002\b\u0003`\u00122\u0006\u0010\u0010\u001a\u00028\u0000H\u0004¢\u0006\u0004\b\u0013\u0010\u0014J\u001d\u0010\u0016\u001a\b\u0012\u0004\u0012\u00028\u00000\u00152\u0006\u0010\u0010\u001a\u00028\u0000H\u0004¢\u0006\u0004\b\u0016\u0010\u0017J\u0019\u0010\u001b\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u0019\u001a\u00020\u0018H\u0014¢\u0006\u0004\b\u001b\u0010\u001cJ\u001b\u0010\u001f\u001a\u00020\u00032\n\u0010\u001e\u001a\u0006\u0012\u0002\b\u00030\u001dH\u0002¢\u0006\u0004\b\u001f\u0010 J#\u0010!\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00028\u00002\n\u0010\u001e\u001a\u0006\u0012\u0002\b\u00030\u001dH\u0002¢\u0006\u0004\b!\u0010\"J\u001b\u0010!\u001a\u00020\b2\n\u0010\u001e\u001a\u0006\u0012\u0002\b\u00030\u001dH\u0002¢\u0006\u0004\b!\u0010#J)\u0010&\u001a\u00020\u00032\u0018\u0010%\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010\b\u0012\u0004\u0012\u00020\u00030\u0002j\u0002`$H\u0016¢\u0006\u0004\b&\u0010\u0007J\u0019\u0010'\u001a\u00020\u00032\b\u0010\t\u001a\u0004\u0018\u00010\bH\u0002¢\u0006\u0004\b'\u0010(J\u0017\u0010)\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00028\u0000H\u0016¢\u0006\u0004\b)\u0010*J\u0017\u0010+\u001a\u00020\u001a2\u0006\u0010\u0010\u001a\u00028\u0000H\u0014¢\u0006\u0004\b+\u0010,J#\u0010/\u001a\u00020\u001a2\u0006\u0010\u0010\u001a\u00028\u00002\n\u0010.\u001a\u0006\u0012\u0002\b\u00030-H\u0014¢\u0006\u0004\b/\u00100J\u0017\u00102\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u000201H\u0014¢\u0006\u0004\b2\u00103JX\u00109\u001a\u00020\u0003\"\u0004\b\u0001\u001042\f\u0010.\u001a\b\u0012\u0004\u0012\u00028\u00010-2\u0006\u0010\u0010\u001a\u00028\u00002(\u00108\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u000006\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u000107\u0012\u0006\u0012\u0004\u0018\u00010\u001a05H\u0002ø\u0001\u0000¢\u0006\u0004\b9\u0010:J\u001b\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0004\b\u0019\u0010;J\u001d\u0010=\u001a\b\u0012\u0002\b\u0003\u0018\u00010<2\u0006\u0010\u0010\u001a\u00028\u0000H\u0004¢\u0006\u0004\b=\u0010>J\u001b\u0010?\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0004\b?\u0010;J\u0017\u0010@\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010<H\u0014¢\u0006\u0004\b@\u0010AJ\u0011\u0010B\u001a\u0004\u0018\u00010\u0018H\u0004¢\u0006\u0004\bB\u0010CJ\u000f\u0010E\u001a\u00020DH\u0016¢\u0006\u0004\bE\u0010FJ$\u0010I\u001a\b\u0012\u0004\u0012\u00020\u00030G2\u0006\u0010\u0010\u001a\u00028\u0000ø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\bH\u0010,J+\u0010J\u001a\u00020\u0003*\u0006\u0012\u0002\b\u0003072\u0006\u0010\u0010\u001a\u00028\u00002\n\u0010\u001e\u001a\u0006\u0012\u0002\b\u00030\u001dH\u0002¢\u0006\u0004\bJ\u0010KR\u0014\u0010M\u001a\u00020D8TX\u0004¢\u0006\u0006\u001a\u0004\bL\u0010FR\u001a\u0010P\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u001d8DX\u0004¢\u0006\u0006\u001a\u0004\bN\u0010OR\u001a\u0010R\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u001d8DX\u0004¢\u0006\u0006\u001a\u0004\bQ\u0010OR\u0014\u0010S\u001a\u00020\n8$X¤\u0004¢\u0006\u0006\u001a\u0004\bS\u0010TR\u0014\u0010U\u001a\u00020\n8$X¤\u0004¢\u0006\u0006\u001a\u0004\bU\u0010TR\u0011\u0010V\u001a\u00020\n8F¢\u0006\u0006\u001a\u0004\bV\u0010TR\u0014\u0010W\u001a\u00020\n8BX\u0004¢\u0006\u0006\u001a\u0004\bW\u0010TR#\u0010[\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u0000060X8F¢\u0006\u0006\u001a\u0004\bY\u0010ZR.\u0010\u0005\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u0002j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\u00048\u0004X\u0004¢\u0006\u0006\n\u0004\b\u0005\u0010\\R\u001a\u0010^\u001a\u00020]8\u0004X\u0004¢\u0006\f\n\u0004\b^\u0010_\u001a\u0004\b`\u0010aR\u0014\u0010c\u001a\u00020D8BX\u0004¢\u0006\u0006\u001a\u0004\bb\u0010F\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b¡\u001e0\u0001¨\u0006h"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel;", "E", "Lkotlin/Function1;", "", "Lkotlinx/coroutines/internal/OnUndeliveredElement;", "onUndeliveredElement", "<init>", "(Lkotlin/jvm/functions/Function1;)V", "", "cause", "", "close", "(Ljava/lang/Throwable;)Z", "", "countQueueSize", "()I", "element", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AddLastDesc;", "Lkotlinx/coroutines/internal/AddLastDesc;", "describeSendBuffered", "(Ljava/lang/Object;)Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AddLastDesc;", "Lkotlinx/coroutines/channels/AbstractSendChannel$TryOfferDesc;", "describeTryOffer", "(Ljava/lang/Object;)Lkotlinx/coroutines/channels/AbstractSendChannel$TryOfferDesc;", "Lkotlinx/coroutines/channels/Send;", "send", "", "enqueueSend", "(Lkotlinx/coroutines/channels/Send;)Ljava/lang/Object;", "Lkotlinx/coroutines/channels/Closed;", "closed", "helpClose", "(Lkotlinx/coroutines/channels/Closed;)V", "helpCloseAndGetSendException", "(Ljava/lang/Object;Lkotlinx/coroutines/channels/Closed;)Ljava/lang/Throwable;", "(Lkotlinx/coroutines/channels/Closed;)Ljava/lang/Throwable;", "Lkotlinx/coroutines/channels/Handler;", "handler", "invokeOnClose", "invokeOnCloseHandler", "(Ljava/lang/Throwable;)V", "offer", "(Ljava/lang/Object;)Z", "offerInternal", "(Ljava/lang/Object;)Ljava/lang/Object;", "Lkotlinx/coroutines/selects/SelectInstance;", "select", "offerSelectInternal", "(Ljava/lang/Object;Lkotlinx/coroutines/selects/SelectInstance;)Ljava/lang/Object;", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "onClosedIdempotent", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;)V", "R", "Lkotlin/Function2;", "Lkotlinx/coroutines/channels/SendChannel;", "Lkotlin/coroutines/Continuation;", "block", "registerSelectSend", "(Lkotlinx/coroutines/selects/SelectInstance;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Lkotlinx/coroutines/channels/ReceiveOrClosed;", "sendBuffered", "(Ljava/lang/Object;)Lkotlinx/coroutines/channels/ReceiveOrClosed;", "sendSuspend", "takeFirstReceiveOrPeekClosed", "()Lkotlinx/coroutines/channels/ReceiveOrClosed;", "takeFirstSendOrPeekClosed", "()Lkotlinx/coroutines/channels/Send;", "", "toString", "()Ljava/lang/String;", "Lkotlinx/coroutines/channels/ChannelResult;", "trySend-JP2dKIU", "trySend", "helpCloseAndResumeWithSendException", "(Lkotlin/coroutines/Continuation;Ljava/lang/Object;Lkotlinx/coroutines/channels/Closed;)V", "getBufferDebugString", "bufferDebugString", "getClosedForReceive", "()Lkotlinx/coroutines/channels/Closed;", "closedForReceive", "getClosedForSend", "closedForSend", "isBufferAlwaysFull", "()Z", "isBufferFull", "isClosedForSend", "isFullImpl", "Lkotlinx/coroutines/selects/SelectClause2;", "getOnSend", "()Lkotlinx/coroutines/selects/SelectClause2;", "onSend", "Lkotlin/jvm/functions/Function1;", "Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "queue", "Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "getQueue", "()Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "getQueueDebugStateString", "queueDebugStateString", "SendBuffered", "SendBufferedDesc", "SendSelect", "TryOfferDesc", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: 0181 */
public abstract class AbstractSendChannel<E> implements SendChannel<E> {
    private static final /* synthetic */ AtomicReferenceFieldUpdater onCloseHandler$FU = AtomicReferenceFieldUpdater.newUpdater(AbstractSendChannel.class, Object.class, "onCloseHandler");
    private volatile /* synthetic */ Object onCloseHandler = null;
    protected final Function1<E, Unit> onUndeliveredElement;
    private final LockFreeLinkedListHead queue = new LockFreeLinkedListHead();

    @Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00028\u0001¢\u0006\u0002\u0010\u0004J\b\u0010\n\u001a\u00020\u000bH\u0016J\u0014\u0010\f\u001a\u00020\u000b2\n\u0010\r\u001a\u0006\u0012\u0002\b\u00030\u000eH\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\u0014\u0010\u0011\u001a\u0004\u0018\u00010\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016R\u0012\u0010\u0003\u001a\u00028\u00018\u0006X\u0004¢\u0006\u0004\n\u0002\u0010\u0005R\u0016\u0010\u0006\u001a\u0004\u0018\u00010\u00078VX\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u0015"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel$SendBuffered;", "E", "Lkotlinx/coroutines/channels/Send;", "element", "(Ljava/lang/Object;)V", "Ljava/lang/Object;", "pollResult", "", "getPollResult", "()Ljava/lang/Object;", "completeResumeSend", "", "resumeSendClosed", "closed", "Lkotlinx/coroutines/channels/Closed;", "toString", "", "tryResumeSend", "Lkotlinx/coroutines/internal/Symbol;", "otherOp", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$PrepareOp;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: 0183 */
    public static final class SendBuffered<E> extends Send {
        public final E element;

        public SendBuffered(E element2) {
            this.element = element2;
        }

        public void completeResumeSend() {
        }

        public Object getPollResult() {
            return this.element;
        }

        public void resumeSendClosed(Closed<?> closed) {
            if (DebugKt.getASSERTIONS_ENABLED()) {
                throw new AssertionError();
            }
        }

        public String toString() {
            StringBuilder append = new StringBuilder().append("SendBuffered@");
            String hexAddress = DebugStringsKt.getHexAddress(this);
            Log1F380D.a((Object) hexAddress);
            return append.append(hexAddress).append('(').append(this.element).append(')').toString();
        }

        public Symbol tryResumeSend(LockFreeLinkedListNode.PrepareOp otherOp) {
            Symbol symbol = CancellableContinuationImplKt.RESUME_TOKEN;
            Symbol symbol2 = symbol;
            if (otherOp != null) {
                otherOp.finishPrepare();
            }
            return symbol;
        }
    }

    @Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0012\u0018\u0000*\u0004\b\u0001\u0010\u00012\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00030\u0002j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0003`\u0004B\u0015\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00028\u0001¢\u0006\u0002\u0010\bJ\u0012\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\fH\u0014¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel$SendBufferedDesc;", "E", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AddLastDesc;", "Lkotlinx/coroutines/channels/AbstractSendChannel$SendBuffered;", "Lkotlinx/coroutines/internal/AddLastDesc;", "queue", "Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "element", "(Lkotlinx/coroutines/internal/LockFreeLinkedListHead;Ljava/lang/Object;)V", "failure", "", "affected", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: AbstractChannel.kt */
    private static class SendBufferedDesc<E> extends LockFreeLinkedListNode.AddLastDesc<SendBuffered<? extends E>> {
        public SendBufferedDesc(LockFreeLinkedListHead queue, E element) {
            super(queue, new SendBuffered(element));
        }

        /* access modifiers changed from: protected */
        public Object failure(LockFreeLinkedListNode affected) {
            if (affected instanceof Closed) {
                return affected;
            }
            if (affected instanceof ReceiveOrClosed) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            return null;
        }
    }

    @Metadata(d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u0001*\u0004\b\u0002\u0010\u00022\u00020\u00032\u00020\u0004BV\u0012\u0006\u0010\u0005\u001a\u00028\u0001\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00010\u0007\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00020\t\u0012(\u0010\n\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\f\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\u000bø\u0001\u0000¢\u0006\u0002\u0010\u000fJ\b\u0010\u0014\u001a\u00020\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u0015H\u0016J\u0014\u0010\u0017\u001a\u00020\u00152\n\u0010\u0018\u001a\u0006\u0012\u0002\b\u00030\u0019H\u0016J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\u0014\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0016J\b\u0010 \u001a\u00020\u0015H\u0016R7\u0010\n\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\f\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\u000b8\u0006X\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0010R\u0016\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00010\u00078\u0006X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\u00028\u0001X\u0004¢\u0006\n\n\u0002\u0010\u0013\u001a\u0004\b\u0011\u0010\u0012R\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00020\t8\u0006X\u0004¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006!"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel$SendSelect;", "E", "R", "Lkotlinx/coroutines/channels/Send;", "Lkotlinx/coroutines/DisposableHandle;", "pollResult", "channel", "Lkotlinx/coroutines/channels/AbstractSendChannel;", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/channels/SendChannel;", "Lkotlin/coroutines/Continuation;", "", "(Ljava/lang/Object;Lkotlinx/coroutines/channels/AbstractSendChannel;Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "Lkotlin/jvm/functions/Function2;", "getPollResult", "()Ljava/lang/Object;", "Ljava/lang/Object;", "completeResumeSend", "", "dispose", "resumeSendClosed", "closed", "Lkotlinx/coroutines/channels/Closed;", "toString", "", "tryResumeSend", "Lkotlinx/coroutines/internal/Symbol;", "otherOp", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$PrepareOp;", "undeliveredElement", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: 0184 */
    private static final class SendSelect<E, R> extends Send implements DisposableHandle {
        public final Function2<SendChannel<? super E>, Continuation<? super R>, Object> block;
        public final AbstractSendChannel<E> channel;
        private final E pollResult;
        public final SelectInstance<R> select;

        public SendSelect(E pollResult2, AbstractSendChannel<E> channel2, SelectInstance<? super R> select2, Function2<? super SendChannel<? super E>, ? super Continuation<? super R>, ? extends Object> block2) {
            this.pollResult = pollResult2;
            this.channel = channel2;
            this.select = select2;
            this.block = block2;
        }

        public void completeResumeSend() {
            CancellableKt.startCoroutineCancellable$default(this.block, this.channel, this.select.getCompletion(), (Function1) null, 4, (Object) null);
        }

        public void dispose() {
            if (remove()) {
                undeliveredElement();
            }
        }

        public E getPollResult() {
            return this.pollResult;
        }

        public void resumeSendClosed(Closed<?> closed) {
            if (this.select.trySelect()) {
                this.select.resumeSelectWithException(closed.getSendException());
            }
        }

        public String toString() {
            StringBuilder append = new StringBuilder().append("SendSelect@");
            String hexAddress = DebugStringsKt.getHexAddress(this);
            Log1F380D.a((Object) hexAddress);
            return append.append(hexAddress).append('(').append(getPollResult()).append(")[").append(this.channel).append(", ").append(this.select).append(']').toString();
        }

        public Symbol tryResumeSend(LockFreeLinkedListNode.PrepareOp otherOp) {
            return (Symbol) this.select.trySelectOther(otherOp);
        }

        public void undeliveredElement() {
            Function1<E, Unit> function1 = this.channel.onUndeliveredElement;
            if (function1 != null) {
                OnUndeliveredElementKt.callUndeliveredElement(function1, getPollResult(), this.select.getCompletion().getContext());
            }
        }
    }

    @Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0004\u0018\u0000*\u0004\b\u0001\u0010\u00012\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00030\u0002j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0003`\u0004B\u0015\u0012\u0006\u0010\u0005\u001a\u00028\u0001\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0012\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\rH\u0014J\u0016\u0010\u000e\u001a\u0004\u0018\u00010\u000b2\n\u0010\u000f\u001a\u00060\u0010j\u0002`\u0011H\u0016R\u0012\u0010\u0005\u001a\u00028\u00018\u0006X\u0004¢\u0006\u0004\n\u0002\u0010\t¨\u0006\u0012"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel$TryOfferDesc;", "E", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$RemoveFirstDesc;", "Lkotlinx/coroutines/channels/ReceiveOrClosed;", "Lkotlinx/coroutines/internal/RemoveFirstDesc;", "element", "queue", "Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "(Ljava/lang/Object;Lkotlinx/coroutines/internal/LockFreeLinkedListHead;)V", "Ljava/lang/Object;", "failure", "", "affected", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "onPrepare", "prepareOp", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$PrepareOp;", "Lkotlinx/coroutines/internal/PrepareOp;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: AbstractChannel.kt */
    protected static final class TryOfferDesc<E> extends LockFreeLinkedListNode.RemoveFirstDesc<ReceiveOrClosed<? super E>> {
        public final E element;

        public TryOfferDesc(E element2, LockFreeLinkedListHead queue) {
            super(queue);
            this.element = element2;
        }

        /* access modifiers changed from: protected */
        public Object failure(LockFreeLinkedListNode affected) {
            if (affected instanceof Closed) {
                return affected;
            }
            if (!(affected instanceof ReceiveOrClosed)) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            return null;
        }

        public Object onPrepare(LockFreeLinkedListNode.PrepareOp prepareOp) {
            Symbol tryResumeReceive = ((ReceiveOrClosed) prepareOp.affected).tryResumeReceive(this.element, prepareOp);
            if (tryResumeReceive == null) {
                return LockFreeLinkedList_commonKt.REMOVE_PREPARED;
            }
            if (tryResumeReceive == AtomicKt.RETRY_ATOMIC) {
                return AtomicKt.RETRY_ATOMIC;
            }
            if (!DebugKt.getASSERTIONS_ENABLED()) {
                return null;
            }
            if (tryResumeReceive == CancellableContinuationImplKt.RESUME_TOKEN) {
                return null;
            }
            throw new AssertionError();
        }
    }

    public AbstractSendChannel(Function1<? super E, Unit> onUndeliveredElement2) {
        this.onUndeliveredElement = onUndeliveredElement2;
    }

    private final int countQueueSize() {
        int i = 0;
        LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
        for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) lockFreeLinkedListHead.getNext(); !Intrinsics.areEqual((Object) lockFreeLinkedListNode, (Object) lockFreeLinkedListHead); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
            if (lockFreeLinkedListNode instanceof LockFreeLinkedListNode) {
                LockFreeLinkedListNode lockFreeLinkedListNode2 = lockFreeLinkedListNode;
                i++;
            }
        }
        return i;
    }

    private final String getQueueDebugStateString() {
        String str;
        LockFreeLinkedListNode nextNode = this.queue.getNextNode();
        if (nextNode == this.queue) {
            return "EmptyQueue";
        }
        if (nextNode instanceof Closed) {
            str = nextNode.toString();
        } else if (nextNode instanceof Receive) {
            str = "ReceiveQueued";
        } else if (nextNode instanceof Send) {
            str = "SendQueued";
        } else {
            str = Intrinsics.stringPlus("UNEXPECTED:", nextNode);
            Log1F380D.a((Object) str);
        }
        LockFreeLinkedListNode prevNode = this.queue.getPrevNode();
        if (prevNode == nextNode) {
            return str;
        }
        String str2 = str + ",queueSize=" + countQueueSize();
        return prevNode instanceof Closed ? str2 + ",closedForSend=" + prevNode : str2;
    }

    private final void helpClose(Closed<?> closed) {
        Object r2 = InlineList.m1571constructorimpl$default((Object) null, 1, (DefaultConstructorMarker) null);
        while (true) {
            LockFreeLinkedListNode prevNode = closed.getPrevNode();
            Receive receive = prevNode instanceof Receive ? (Receive) prevNode : null;
            if (receive == null) {
                break;
            } else if (!receive.remove()) {
                receive.helpRemove();
            } else {
                r2 = InlineList.m1576plusFjFbRPM(r2, receive);
            }
        }
        if (r2 != null) {
            if (!(r2 instanceof ArrayList)) {
                ((Receive) r2).resumeReceiveClosed(closed);
            } else if (r2 != null) {
                ArrayList arrayList = (ArrayList) r2;
                int size = arrayList.size() - 1;
                if (size >= 0) {
                    do {
                        int i = size;
                        size--;
                        ((Receive) arrayList.get(i)).resumeReceiveClosed(closed);
                    } while (size >= 0);
                }
            } else {
                throw new NullPointerException("null cannot be cast to non-null type java.util.ArrayList<E of kotlinx.coroutines.internal.InlineList>{ kotlin.collections.TypeAliasesKt.ArrayList<E of kotlinx.coroutines.internal.InlineList> }");
            }
        }
        onClosedIdempotent(closed);
    }

    private final Throwable helpCloseAndGetSendException(E element, Closed<?> closed) {
        UndeliveredElementException callUndeliveredElementCatchingException$default;
        helpClose(closed);
        Function1<E, Unit> function1 = this.onUndeliveredElement;
        if (function1 == null || (callUndeliveredElementCatchingException$default = OnUndeliveredElementKt.callUndeliveredElementCatchingException$default(function1, element, (UndeliveredElementException) null, 2, (Object) null)) == null) {
            return closed.getSendException();
        }
        ExceptionsKt.addSuppressed(callUndeliveredElementCatchingException$default, closed.getSendException());
        throw callUndeliveredElementCatchingException$default;
    }

    private final Throwable helpCloseAndGetSendException(Closed<?> closed) {
        helpClose(closed);
        return closed.getSendException();
    }

    /* access modifiers changed from: private */
    public final void helpCloseAndResumeWithSendException(Continuation<?> $this$helpCloseAndResumeWithSendException, E element, Closed<?> closed) {
        UndeliveredElementException callUndeliveredElementCatchingException$default;
        helpClose(closed);
        Throwable sendException = closed.getSendException();
        Function1<E, Unit> function1 = this.onUndeliveredElement;
        if (function1 == null || (callUndeliveredElementCatchingException$default = OnUndeliveredElementKt.callUndeliveredElementCatchingException$default(function1, element, (UndeliveredElementException) null, 2, (Object) null)) == null) {
            Result.Companion companion = Result.Companion;
            $this$helpCloseAndResumeWithSendException.resumeWith(Result.m36constructorimpl(ResultKt.createFailure(sendException)));
            return;
        }
        ExceptionsKt.addSuppressed(callUndeliveredElementCatchingException$default, sendException);
        Result.Companion companion2 = Result.Companion;
        $this$helpCloseAndResumeWithSendException.resumeWith(Result.m36constructorimpl(ResultKt.createFailure(callUndeliveredElementCatchingException$default)));
    }

    private final void invokeOnCloseHandler(Throwable cause) {
        Object obj = this.onCloseHandler;
        if (obj != null && obj != AbstractChannelKt.HANDLER_INVOKED && AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(onCloseHandler$FU, this, obj, AbstractChannelKt.HANDLER_INVOKED)) {
            ((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(obj, 1)).invoke(cause);
        }
    }

    /* access modifiers changed from: private */
    public final boolean isFullImpl() {
        return !(this.queue.getNextNode() instanceof ReceiveOrClosed) && isBufferFull();
    }

    public boolean close(Throwable cause) {
        boolean z;
        Closed closed = new Closed(cause);
        LockFreeLinkedListNode lockFreeLinkedListNode = this.queue;
        while (true) {
            LockFreeLinkedListNode prevNode = lockFreeLinkedListNode.getPrevNode();
            z = true;
            if (!(prevNode instanceof Closed)) {
                if (prevNode.addNext(closed, lockFreeLinkedListNode)) {
                    break;
                }
            } else {
                z = false;
                break;
            }
        }
        boolean z2 = z;
        helpClose(z2 ? closed : (Closed) this.queue.getPrevNode());
        if (z2) {
            invokeOnCloseHandler(cause);
        }
        return z2;
    }

    /* access modifiers changed from: protected */
    public final LockFreeLinkedListNode.AddLastDesc<?> describeSendBuffered(E element) {
        return new SendBufferedDesc<>(this.queue, element);
    }

    /* access modifiers changed from: protected */
    public final TryOfferDesc<E> describeTryOffer(E element) {
        return new TryOfferDesc<>(element, this.queue);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0035 A[LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object enqueueSend(kotlinx.coroutines.channels.Send r8) {
        /*
            r7 = this;
            boolean r0 = r7.isBufferAlwaysFull()
            if (r0 == 0) goto L_0x0022
            kotlinx.coroutines.internal.LockFreeLinkedListHead r0 = r7.queue
            kotlinx.coroutines.internal.LockFreeLinkedListNode r0 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r0
            r1 = 0
        L_0x000b:
            kotlinx.coroutines.internal.LockFreeLinkedListNode r2 = r0.getPrevNode()
            r3 = r2
            r4 = 0
            boolean r5 = r3 instanceof kotlinx.coroutines.channels.ReceiveOrClosed
            if (r5 == 0) goto L_0x0017
            return r3
        L_0x0017:
            r3 = r8
            kotlinx.coroutines.internal.LockFreeLinkedListNode r3 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r3
            boolean r3 = r2.addNext(r3, r0)
            if (r3 == 0) goto L_0x000b
            goto L_0x0055
        L_0x0022:
            kotlinx.coroutines.internal.LockFreeLinkedListHead r0 = r7.queue
            kotlinx.coroutines.internal.LockFreeLinkedListNode r0 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r0
            r1 = 0
            r2 = r0
            r3 = 0
            kotlinx.coroutines.channels.AbstractSendChannel$enqueueSend$$inlined$addLastIfPrevAndIf$1 r4 = new kotlinx.coroutines.channels.AbstractSendChannel$enqueueSend$$inlined$addLastIfPrevAndIf$1
            r5 = r8
            kotlinx.coroutines.internal.LockFreeLinkedListNode r5 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r5
            r4.<init>(r5, r7)
            kotlinx.coroutines.internal.LockFreeLinkedListNode$CondAddOp r4 = (kotlinx.coroutines.internal.LockFreeLinkedListNode.CondAddOp) r4
            r2 = r4
        L_0x0035:
            kotlinx.coroutines.internal.LockFreeLinkedListNode r3 = r0.getPrevNode()
            r4 = r3
            r5 = 0
            boolean r6 = r4 instanceof kotlinx.coroutines.channels.ReceiveOrClosed
            if (r6 == 0) goto L_0x0041
            return r4
        L_0x0041:
            r4 = r8
            kotlinx.coroutines.internal.LockFreeLinkedListNode r4 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r4
            int r4 = r3.tryCondAddNext(r4, r0, r2)
            switch(r4) {
                case 1: goto L_0x004f;
                case 2: goto L_0x004d;
                default: goto L_0x004c;
            }
        L_0x004c:
            goto L_0x0035
        L_0x004d:
            r4 = 0
            goto L_0x0050
        L_0x004f:
            r4 = 1
        L_0x0050:
            if (r4 != 0) goto L_0x0055
            kotlinx.coroutines.internal.Symbol r0 = kotlinx.coroutines.channels.AbstractChannelKt.ENQUEUE_FAILED
            return r0
        L_0x0055:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.AbstractSendChannel.enqueueSend(kotlinx.coroutines.channels.Send):java.lang.Object");
    }

    /* access modifiers changed from: protected */
    public String getBufferDebugString() {
        return HttpUrl.FRAGMENT_ENCODE_SET;
    }

    /* access modifiers changed from: protected */
    public final Closed<?> getClosedForReceive() {
        LockFreeLinkedListNode nextNode = this.queue.getNextNode();
        Closed<?> closed = nextNode instanceof Closed ? (Closed) nextNode : null;
        if (closed == null) {
            return null;
        }
        helpClose(closed);
        return closed;
    }

    /* access modifiers changed from: protected */
    public final Closed<?> getClosedForSend() {
        LockFreeLinkedListNode prevNode = this.queue.getPrevNode();
        Closed<?> closed = prevNode instanceof Closed ? (Closed) prevNode : null;
        if (closed == null) {
            return null;
        }
        helpClose(closed);
        return closed;
    }

    public final SelectClause2<E, SendChannel<E>> getOnSend() {
        return new AbstractSendChannel$onSend$1(this);
    }

    /* access modifiers changed from: protected */
    public final LockFreeLinkedListHead getQueue() {
        return this.queue;
    }

    /* access modifiers changed from: protected */
    public abstract boolean isBufferAlwaysFull();

    /* access modifiers changed from: protected */
    public abstract boolean isBufferFull();

    public final boolean isClosedForSend() {
        return getClosedForSend() != null;
    }

    public boolean offer(E element) {
        UndeliveredElementException callUndeliveredElementCatchingException$default;
        try {
            return SendChannel.DefaultImpls.offer(this, element);
        } catch (Throwable th) {
            Function1<E, Unit> function1 = this.onUndeliveredElement;
            if (function1 == null || (callUndeliveredElementCatchingException$default = OnUndeliveredElementKt.callUndeliveredElementCatchingException$default(function1, element, (UndeliveredElementException) null, 2, (Object) null)) == null) {
                throw th;
            }
            ExceptionsKt.addSuppressed(callUndeliveredElementCatchingException$default, th);
            throw callUndeliveredElementCatchingException$default;
        }
    }

    /* access modifiers changed from: protected */
    public Object offerInternal(E element) {
        ReceiveOrClosed takeFirstReceiveOrPeekClosed;
        Symbol tryResumeReceive;
        do {
            takeFirstReceiveOrPeekClosed = takeFirstReceiveOrPeekClosed();
            if (takeFirstReceiveOrPeekClosed == null) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            tryResumeReceive = takeFirstReceiveOrPeekClosed.tryResumeReceive(element, (LockFreeLinkedListNode.PrepareOp) null);
        } while (tryResumeReceive == null);
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(tryResumeReceive == CancellableContinuationImplKt.RESUME_TOKEN)) {
                throw new AssertionError();
            }
        }
        takeFirstReceiveOrPeekClosed.completeResumeReceive(element);
        return takeFirstReceiveOrPeekClosed.getOfferResult();
    }

    /* access modifiers changed from: protected */
    public Object offerSelectInternal(E element, SelectInstance<?> select) {
        TryOfferDesc describeTryOffer = describeTryOffer(element);
        Object performAtomicTrySelect = select.performAtomicTrySelect(describeTryOffer);
        if (performAtomicTrySelect != null) {
            return performAtomicTrySelect;
        }
        ReceiveOrClosed receiveOrClosed = (ReceiveOrClosed) describeTryOffer.getResult();
        receiveOrClosed.completeResumeReceive(element);
        return receiveOrClosed.getOfferResult();
    }

    /* access modifiers changed from: protected */
    public void onClosedIdempotent(LockFreeLinkedListNode closed) {
    }

    public final Object send(E element, Continuation<? super Unit> $completion) {
        if (offerInternal(element) == AbstractChannelKt.OFFER_SUCCESS) {
            return Unit.INSTANCE;
        }
        Object sendSuspend = sendSuspend(element, $completion);
        return sendSuspend == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? sendSuspend : Unit.INSTANCE;
    }

    /* access modifiers changed from: protected */
    public final ReceiveOrClosed<?> sendBuffered(E element) {
        LockFreeLinkedListNode prevNode;
        LockFreeLinkedListNode lockFreeLinkedListNode = this.queue;
        LockFreeLinkedListNode sendBuffered = new SendBuffered(element);
        do {
            prevNode = lockFreeLinkedListNode.getPrevNode();
            LockFreeLinkedListNode lockFreeLinkedListNode2 = prevNode;
            if (lockFreeLinkedListNode2 instanceof ReceiveOrClosed) {
                return (ReceiveOrClosed) lockFreeLinkedListNode2;
            }
        } while (!prevNode.addNext(sendBuffered, lockFreeLinkedListNode));
        return null;
    }

    /* access modifiers changed from: protected */
    public ReceiveOrClosed<E> takeFirstReceiveOrPeekClosed() {
        LockFreeLinkedListNode lockFreeLinkedListNode;
        LockFreeLinkedListNode removeOrNext;
        LockFreeLinkedListNode lockFreeLinkedListNode2 = this.queue;
        while (true) {
            lockFreeLinkedListNode = (LockFreeLinkedListNode) lockFreeLinkedListNode2.getNext();
            if (lockFreeLinkedListNode != lockFreeLinkedListNode2) {
                if (lockFreeLinkedListNode instanceof ReceiveOrClosed) {
                    if (((((ReceiveOrClosed) lockFreeLinkedListNode) instanceof Closed) && !lockFreeLinkedListNode.isRemoved()) || (removeOrNext = lockFreeLinkedListNode.removeOrNext()) == null) {
                        break;
                    }
                    removeOrNext.helpRemovePrev();
                } else {
                    lockFreeLinkedListNode = null;
                    break;
                }
            } else {
                lockFreeLinkedListNode = null;
                break;
            }
        }
        return (ReceiveOrClosed) lockFreeLinkedListNode;
    }

    /* access modifiers changed from: protected */
    public final Send takeFirstSendOrPeekClosed() {
        LockFreeLinkedListNode lockFreeLinkedListNode;
        LockFreeLinkedListNode removeOrNext;
        LockFreeLinkedListNode lockFreeLinkedListNode2 = this.queue;
        while (true) {
            lockFreeLinkedListNode = (LockFreeLinkedListNode) lockFreeLinkedListNode2.getNext();
            if (lockFreeLinkedListNode != lockFreeLinkedListNode2) {
                if (lockFreeLinkedListNode instanceof Send) {
                    if (((((Send) lockFreeLinkedListNode) instanceof Closed) && !lockFreeLinkedListNode.isRemoved()) || (removeOrNext = lockFreeLinkedListNode.removeOrNext()) == null) {
                        break;
                    }
                    removeOrNext.helpRemovePrev();
                } else {
                    lockFreeLinkedListNode = null;
                    break;
                }
            } else {
                lockFreeLinkedListNode = null;
                break;
            }
        }
        return (Send) lockFreeLinkedListNode;
    }

    /* access modifiers changed from: private */
    public final <R> void registerSelectSend(SelectInstance<? super R> select, E element, Function2<? super SendChannel<? super E>, ? super Continuation<? super R>, ? extends Object> block) {
        while (!select.isSelected()) {
            if (isFullImpl()) {
                SendSelect sendSelect = new SendSelect(element, this, select, block);
                Object enqueueSend = enqueueSend(sendSelect);
                if (enqueueSend == null) {
                    select.disposeOnSelect(sendSelect);
                    return;
                } else if (enqueueSend instanceof Closed) {
                    throw StackTraceRecoveryKt.recoverStackTrace(helpCloseAndGetSendException(element, (Closed) enqueueSend));
                } else if (enqueueSend != AbstractChannelKt.ENQUEUE_FAILED && !(enqueueSend instanceof Receive)) {
                    throw new IllegalStateException(("enqueueSend returned " + enqueueSend + ' ').toString());
                }
            }
            Object offerSelectInternal = offerSelectInternal(element, select);
            if (offerSelectInternal != SelectKt.getALREADY_SELECTED()) {
                if (offerSelectInternal != AbstractChannelKt.OFFER_FAILED && offerSelectInternal != AtomicKt.RETRY_ATOMIC) {
                    if (offerSelectInternal == AbstractChannelKt.OFFER_SUCCESS) {
                        UndispatchedKt.startCoroutineUnintercepted(block, this, select.getCompletion());
                        return;
                    } else if (offerSelectInternal instanceof Closed) {
                        throw StackTraceRecoveryKt.recoverStackTrace(helpCloseAndGetSendException(element, (Closed) offerSelectInternal));
                    } else {
                        String stringPlus = Intrinsics.stringPlus("offerSelectInternal returned ", offerSelectInternal);
                        Log1F380D.a((Object) stringPlus);
                        throw new IllegalStateException(stringPlus.toString());
                    }
                }
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: private */
    public final Object sendSuspend(E element, Continuation<? super Unit> $completion) {
        CancellableContinuationImpl<? super Unit> orCreateCancellableContinuation = CancellableContinuationKt.getOrCreateCancellableContinuation(IntrinsicsKt.intercepted($completion));
        CancellableContinuation cancellableContinuation = orCreateCancellableContinuation;
        while (true) {
            if (isFullImpl()) {
                SendElement sendElement = this.onUndeliveredElement == null ? new SendElement(element, cancellableContinuation) : new SendElementWithUndeliveredHandler(element, cancellableContinuation, this.onUndeliveredElement);
                Object enqueueSend = enqueueSend(sendElement);
                if (enqueueSend == null) {
                    CancellableContinuationKt.removeOnCancellation(cancellableContinuation, sendElement);
                    break;
                } else if (enqueueSend instanceof Closed) {
                    helpCloseAndResumeWithSendException(cancellableContinuation, element, (Closed) enqueueSend);
                    break;
                } else if (enqueueSend != AbstractChannelKt.ENQUEUE_FAILED && !(enqueueSend instanceof Receive)) {
                    String stringPlus = Intrinsics.stringPlus("enqueueSend returned ", enqueueSend);
                    Log1F380D.a((Object) stringPlus);
                    throw new IllegalStateException(stringPlus.toString());
                }
            }
            Object offerInternal = offerInternal(element);
            if (offerInternal == AbstractChannelKt.OFFER_SUCCESS) {
                Result.Companion companion = Result.Companion;
                cancellableContinuation.resumeWith(Result.m36constructorimpl(Unit.INSTANCE));
                break;
            } else if (offerInternal != AbstractChannelKt.OFFER_FAILED) {
                if (offerInternal instanceof Closed) {
                    helpCloseAndResumeWithSendException(cancellableContinuation, element, (Closed) offerInternal);
                } else {
                    String stringPlus2 = Intrinsics.stringPlus("offerInternal returned ", offerInternal);
                    Log1F380D.a((Object) stringPlus2);
                    throw new IllegalStateException(stringPlus2.toString());
                }
            }
        }
        Object result = orCreateCancellableContinuation.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended($completion);
        }
        return result == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? result : Unit.INSTANCE;
    }

    public void invokeOnClose(Function1<? super Throwable, Unit> handler) {
        AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = onCloseHandler$FU;
        if (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(atomicReferenceFieldUpdater, this, (Object) null, handler)) {
            Object obj = this.onCloseHandler;
            if (obj == AbstractChannelKt.HANDLER_INVOKED) {
                throw new IllegalStateException("Another handler was already registered and successfully invoked");
            }
            String stringPlus = Intrinsics.stringPlus("Another handler was already registered: ", obj);
            Log1F380D.a((Object) stringPlus);
            throw new IllegalStateException(stringPlus);
        }
        Closed<?> closedForSend = getClosedForSend();
        if (closedForSend != null && AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(atomicReferenceFieldUpdater, this, handler, AbstractChannelKt.HANDLER_INVOKED)) {
            handler.invoke(closedForSend.closeCause);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String classSimpleName = DebugStringsKt.getClassSimpleName(this);
        Log1F380D.a((Object) classSimpleName);
        StringBuilder append = sb.append(classSimpleName).append('@');
        String hexAddress = DebugStringsKt.getHexAddress(this);
        Log1F380D.a((Object) hexAddress);
        return append.append(hexAddress).append('{').append(getQueueDebugStateString()).append('}').append(getBufferDebugString()).toString();
    }

    /* renamed from: trySend-JP2dKIU  reason: not valid java name */
    public final Object m1525trySendJP2dKIU(E element) {
        Object offerInternal = offerInternal(element);
        if (offerInternal == AbstractChannelKt.OFFER_SUCCESS) {
            return ChannelResult.Companion.m1549successJP2dKIU(Unit.INSTANCE);
        }
        if (offerInternal == AbstractChannelKt.OFFER_FAILED) {
            Closed<?> closedForSend = getClosedForSend();
            return closedForSend == null ? ChannelResult.Companion.m1548failurePtdJZtk() : ChannelResult.Companion.m1547closedJP2dKIU(helpCloseAndGetSendException(closedForSend));
        } else if (offerInternal instanceof Closed) {
            return ChannelResult.Companion.m1547closedJP2dKIU(helpCloseAndGetSendException((Closed) offerInternal));
        } else {
            String stringPlus = Intrinsics.stringPlus("trySend returned ", offerInternal);
            Log1F380D.a((Object) stringPlus);
            throw new IllegalStateException(stringPlus.toString());
        }
    }
}

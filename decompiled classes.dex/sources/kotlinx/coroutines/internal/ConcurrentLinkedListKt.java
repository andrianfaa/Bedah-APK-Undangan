package kotlinx.coroutines.internal;

import kotlin.Metadata;
import kotlin.jvm.functions.Function2;

@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\u001a#\u0010\u0002\u001a\u00028\u0000\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u00028\u00000\u0000*\u00028\u0000H\u0000¢\u0006\u0004\b\u0002\u0010\u0003\u001ao\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\r\"\u000e\b\u0000\u0010\u0005*\b\u0012\u0004\u0012\u00028\u00000\u0004*\u00028\u00002\u0006\u0010\u0007\u001a\u00020\u000628\u0010\f\u001a4\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\t\u0012\b\b\n\u0012\u0004\b\b(\u0007\u0012\u0015\u0012\u0013\u0018\u00018\u0000¢\u0006\f\b\t\u0012\b\b\n\u0012\u0004\b\b(\u000b\u0012\u0004\u0012\u00028\u00000\bH\bø\u0001\u0000¢\u0006\u0004\b\u000e\u0010\u000f\"\u001a\u0010\u0011\u001a\u00020\u00108\u0002X\u0004¢\u0006\f\n\u0004\b\u0011\u0010\u0012\u0012\u0004\b\u0013\u0010\u0014\"\u0014\u0010\u0016\u001a\u00020\u00158\u0002XT¢\u0006\u0006\n\u0004\b\u0016\u0010\u0017\u0002\u0004\n\u0002\b\u0019¨\u0006\u0018"}, d2 = {"Lkotlinx/coroutines/internal/ConcurrentLinkedListNode;", "N", "close", "(Lkotlinx/coroutines/internal/ConcurrentLinkedListNode;)Lkotlinx/coroutines/internal/ConcurrentLinkedListNode;", "Lkotlinx/coroutines/internal/Segment;", "S", "", "id", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "prev", "createNewSegment", "Lkotlinx/coroutines/internal/SegmentOrClosed;", "findSegmentInternal", "(Lkotlinx/coroutines/internal/Segment;JLkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "Lkotlinx/coroutines/internal/Symbol;", "CLOSED", "Lkotlinx/coroutines/internal/Symbol;", "getCLOSED$annotations", "()V", "", "POINTERS_SHIFT", "I", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: ConcurrentLinkedList.kt */
public final class ConcurrentLinkedListKt {
    /* access modifiers changed from: private */
    public static final Symbol CLOSED = new Symbol("CLOSED");
    private static final int POINTERS_SHIFT = 16;

    public static final <N extends ConcurrentLinkedListNode<N>> N close(N $this$close) {
        N n = $this$close;
        while (true) {
            N access$getNextOrClosed = n.getNextOrClosed();
            if (access$getNextOrClosed == CLOSED) {
                return n;
            }
            N n2 = (ConcurrentLinkedListNode) access$getNextOrClosed;
            if (n2 != null) {
                n = n2;
            } else if (n.markAsClosed()) {
                return n;
            }
        }
    }

    private static final <S extends Segment<S>> Object findSegmentInternal(S $this$findSegmentInternal, long id, Function2<? super Long, ? super S, ? extends S> createNewSegment) {
        S s = $this$findSegmentInternal;
        while (true) {
            if (s.getId() >= id && !s.getRemoved()) {
                return SegmentOrClosed.m1580constructorimpl(s);
            }
            S access$getNextOrClosed = ((ConcurrentLinkedListNode) s).getNextOrClosed();
            if (access$getNextOrClosed == CLOSED) {
                return SegmentOrClosed.m1580constructorimpl(CLOSED);
            }
            S s2 = (Segment) ((ConcurrentLinkedListNode) access$getNextOrClosed);
            if (s2 != null) {
                s = s2;
            } else {
                S s3 = (Segment) createNewSegment.invoke(Long.valueOf(s.getId() + 1), s);
                if (s.trySetNext((ConcurrentLinkedListNode) s3)) {
                    if (s.getRemoved()) {
                        s.remove();
                    }
                    s = s3;
                }
            }
        }
    }

    private static /* synthetic */ void getCLOSED$annotations() {
    }
}

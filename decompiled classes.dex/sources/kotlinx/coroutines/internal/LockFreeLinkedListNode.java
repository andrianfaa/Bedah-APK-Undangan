package kotlinx.coroutines.internal;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.DebugStringsKt;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0000\n\u0002\b\f\b\u0017\u0018\u00002\u00020C:\u0005JKLMNB\u0007¢\u0006\u0004\b\u0001\u0010\u0002J\u0019\u0010\u0006\u001a\u00020\u00052\n\u0010\u0004\u001a\u00060\u0000j\u0002`\u0003¢\u0006\u0004\b\u0006\u0010\u0007J,\u0010\u000b\u001a\u00020\t2\n\u0010\u0004\u001a\u00060\u0000j\u0002`\u00032\u000e\b\u0004\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\bH\b¢\u0006\u0004\b\u000b\u0010\fJ4\u0010\u000f\u001a\u00020\t2\n\u0010\u0004\u001a\u00060\u0000j\u0002`\u00032\u0016\u0010\u000e\u001a\u0012\u0012\b\u0012\u00060\u0000j\u0002`\u0003\u0012\u0004\u0012\u00020\t0\rH\b¢\u0006\u0004\b\u000f\u0010\u0010JD\u0010\u0011\u001a\u00020\t2\n\u0010\u0004\u001a\u00060\u0000j\u0002`\u00032\u0016\u0010\u000e\u001a\u0012\u0012\b\u0012\u00060\u0000j\u0002`\u0003\u0012\u0004\u0012\u00020\t0\r2\u000e\b\u0004\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\bH\b¢\u0006\u0004\b\u0011\u0010\u0012J'\u0010\u0014\u001a\u00020\t2\n\u0010\u0004\u001a\u00060\u0000j\u0002`\u00032\n\u0010\u0013\u001a\u00060\u0000j\u0002`\u0003H\u0001¢\u0006\u0004\b\u0014\u0010\u0015J\u0019\u0010\u0016\u001a\u00020\t2\n\u0010\u0004\u001a\u00060\u0000j\u0002`\u0003¢\u0006\u0004\b\u0016\u0010\u0017J\"\u0010\u001a\u001a\n\u0018\u00010\u0000j\u0004\u0018\u0001`\u00032\b\u0010\u0019\u001a\u0004\u0018\u00010\u0018H\u0010¢\u0006\u0004\b\u001a\u0010\u001bJ)\u0010\u001e\u001a\b\u0012\u0004\u0012\u00028\u00000\u001d\"\f\b\u0000\u0010\u001c*\u00060\u0000j\u0002`\u00032\u0006\u0010\u0004\u001a\u00028\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u0017\u0010!\u001a\f\u0012\b\u0012\u00060\u0000j\u0002`\u00030 ¢\u0006\u0004\b!\u0010\"J \u0010$\u001a\u00060\u0000j\u0002`\u00032\n\u0010#\u001a\u00060\u0000j\u0002`\u0003H\u0010¢\u0006\u0004\b$\u0010%J\u001b\u0010&\u001a\u00020\u00052\n\u0010\u0013\u001a\u00060\u0000j\u0002`\u0003H\u0002¢\u0006\u0004\b&\u0010\u0007J\r\u0010'\u001a\u00020\u0005¢\u0006\u0004\b'\u0010\u0002J\u000f\u0010(\u001a\u00020\u0005H\u0001¢\u0006\u0004\b(\u0010\u0002J,\u0010*\u001a\u00020)2\n\u0010\u0004\u001a\u00060\u0000j\u0002`\u00032\u000e\b\u0004\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\bH\b¢\u0006\u0004\b*\u0010+J\u0017\u0010,\u001a\n\u0018\u00010\u0000j\u0004\u0018\u0001`\u0003H\u0014¢\u0006\u0004\b,\u0010-J\u000f\u0010.\u001a\u00020\tH\u0016¢\u0006\u0004\b.\u0010/J.\u00100\u001a\u0004\u0018\u00018\u0000\"\u0006\b\u0000\u0010\u001c\u0018\u00012\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\t0\rH\b¢\u0006\u0004\b0\u00101J\u0015\u00102\u001a\n\u0018\u00010\u0000j\u0004\u0018\u0001`\u0003¢\u0006\u0004\b2\u0010-J\u0017\u00103\u001a\n\u0018\u00010\u0000j\u0004\u0018\u0001`\u0003H\u0001¢\u0006\u0004\b3\u0010-J\u000f\u00105\u001a\u000204H\u0002¢\u0006\u0004\b5\u00106J\u000f\u00108\u001a\u000207H\u0016¢\u0006\u0004\b8\u00109J/\u0010<\u001a\u00020;2\n\u0010\u0004\u001a\u00060\u0000j\u0002`\u00032\n\u0010\u0013\u001a\u00060\u0000j\u0002`\u00032\u0006\u0010:\u001a\u00020)H\u0001¢\u0006\u0004\b<\u0010=J'\u0010A\u001a\u00020\u00052\n\u0010>\u001a\u00060\u0000j\u0002`\u00032\n\u0010\u0013\u001a\u00060\u0000j\u0002`\u0003H\u0000¢\u0006\u0004\b?\u0010@R\u0014\u0010B\u001a\u00020\t8VX\u0004¢\u0006\u0006\u001a\u0004\bB\u0010/R\u0011\u0010\u0013\u001a\u00020C8F¢\u0006\u0006\u001a\u0004\bD\u0010ER\u0015\u0010G\u001a\u00060\u0000j\u0002`\u00038F¢\u0006\u0006\u001a\u0004\bF\u0010-R\u0015\u0010I\u001a\u00060\u0000j\u0002`\u00038F¢\u0006\u0006\u001a\u0004\bH\u0010-¨\u0006O"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "<init>", "()V", "Lkotlinx/coroutines/internal/Node;", "node", "", "addLast", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;)V", "Lkotlin/Function0;", "", "condition", "addLastIf", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;Lkotlin/jvm/functions/Function0;)Z", "Lkotlin/Function1;", "predicate", "addLastIfPrev", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;Lkotlin/jvm/functions/Function1;)Z", "addLastIfPrevAndIf", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;)Z", "next", "addNext", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;Lkotlinx/coroutines/internal/LockFreeLinkedListNode;)Z", "addOneIfEmpty", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;)Z", "Lkotlinx/coroutines/internal/OpDescriptor;", "op", "correctPrev", "(Lkotlinx/coroutines/internal/OpDescriptor;)Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "T", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AddLastDesc;", "describeAddLast", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;)Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AddLastDesc;", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$RemoveFirstDesc;", "describeRemoveFirst", "()Lkotlinx/coroutines/internal/LockFreeLinkedListNode$RemoveFirstDesc;", "current", "findPrevNonRemoved", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;)Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "finishAdd", "helpRemove", "helpRemovePrev", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$CondAddOp;", "makeCondAddOp", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;Lkotlin/jvm/functions/Function0;)Lkotlinx/coroutines/internal/LockFreeLinkedListNode$CondAddOp;", "nextIfRemoved", "()Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "remove", "()Z", "removeFirstIfIsInstanceOfOrPeekIf", "(Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "removeFirstOrNull", "removeOrNext", "Lkotlinx/coroutines/internal/Removed;", "removed", "()Lkotlinx/coroutines/internal/Removed;", "", "toString", "()Ljava/lang/String;", "condAdd", "", "tryCondAddNext", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;Lkotlinx/coroutines/internal/LockFreeLinkedListNode;Lkotlinx/coroutines/internal/LockFreeLinkedListNode$CondAddOp;)I", "prev", "validateNode$kotlinx_coroutines_core", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;Lkotlinx/coroutines/internal/LockFreeLinkedListNode;)V", "validateNode", "isRemoved", "", "getNext", "()Ljava/lang/Object;", "getNextNode", "nextNode", "getPrevNode", "prevNode", "AbstractAtomicDesc", "AddLastDesc", "CondAddOp", "PrepareOp", "RemoveFirstDesc", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: 016D */
public class LockFreeLinkedListNode {
    static final /* synthetic */ AtomicReferenceFieldUpdater _next$FU;
    static final /* synthetic */ AtomicReferenceFieldUpdater _prev$FU;
    private static final /* synthetic */ AtomicReferenceFieldUpdater _removedRef$FU;
    volatile /* synthetic */ Object _next = this;
    volatile /* synthetic */ Object _prev = this;
    private volatile /* synthetic */ Object _removedRef = null;

    @Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001c\u0010\n\u001a\u00020\u000b2\n\u0010\f\u001a\u0006\u0012\u0002\b\u00030\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fJ\u0016\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\n\u0010\u0010\u001a\u00060\u0004j\u0002`\u0005H\u0014J \u0010\u0011\u001a\u00020\u000b2\n\u0010\u0010\u001a\u00060\u0004j\u0002`\u00052\n\u0010\u0012\u001a\u00060\u0004j\u0002`\u0005H$J\u0010\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\u0015H&J\u0012\u0010\u0016\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u0014\u0010\u0017\u001a\u00020\u000b2\n\u0010\u0010\u001a\u00060\u0004j\u0002`\u0005H\u0016J\u0014\u0010\u0018\u001a\u0004\u0018\u00010\u000f2\n\u0010\f\u001a\u0006\u0012\u0002\b\u00030\rJ\u001c\u0010\u0019\u001a\u00020\u001a2\n\u0010\u0010\u001a\u00060\u0004j\u0002`\u00052\u0006\u0010\u0012\u001a\u00020\u000fH\u0014J\u0018\u0010\u001b\u001a\n\u0018\u00010\u0004j\u0004\u0018\u0001`\u00052\u0006\u0010\f\u001a\u00020\u001cH\u0014J \u0010\u001d\u001a\u00020\u000f2\n\u0010\u0010\u001a\u00060\u0004j\u0002`\u00052\n\u0010\u0012\u001a\u00060\u0004j\u0002`\u0005H&R\u001a\u0010\u0003\u001a\n\u0018\u00010\u0004j\u0004\u0018\u0001`\u0005X¤\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\n\u0018\u00010\u0004j\u0004\u0018\u0001`\u0005X¤\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\u0007¨\u0006\u001e"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AbstractAtomicDesc;", "Lkotlinx/coroutines/internal/AtomicDesc;", "()V", "affectedNode", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/internal/Node;", "getAffectedNode", "()Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "originalNext", "getOriginalNext", "complete", "", "op", "Lkotlinx/coroutines/internal/AtomicOp;", "failure", "", "affected", "finishOnSuccess", "next", "finishPrepare", "prepareOp", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$PrepareOp;", "onPrepare", "onRemoved", "prepare", "retry", "", "takeAffectedNode", "Lkotlinx/coroutines/internal/OpDescriptor;", "updatedNext", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: LockFreeLinkedList.kt */
    public static abstract class AbstractAtomicDesc extends AtomicDesc {
        public final void complete(AtomicOp<?> op, Object failure) {
            boolean z = true;
            boolean z2 = failure == null;
            LockFreeLinkedListNode affectedNode = getAffectedNode();
            if (affectedNode == null) {
                AbstractAtomicDesc abstractAtomicDesc = this;
                if (DebugKt.getASSERTIONS_ENABLED()) {
                    if (z2) {
                        z = false;
                    }
                    if (!z) {
                        throw new AssertionError();
                    }
                    return;
                }
                return;
            }
            LockFreeLinkedListNode originalNext = getOriginalNext();
            if (originalNext == null) {
                AbstractAtomicDesc abstractAtomicDesc2 = this;
                if (DebugKt.getASSERTIONS_ENABLED()) {
                    if (z2) {
                        z = false;
                    }
                    if (!z) {
                        throw new AssertionError();
                    }
                    return;
                }
                return;
            }
            LockFreeLinkedListNode lockFreeLinkedListNode = originalNext;
            if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(LockFreeLinkedListNode._next$FU, affectedNode, op, z2 ? updatedNext(affectedNode, lockFreeLinkedListNode) : lockFreeLinkedListNode) && z2) {
                finishOnSuccess(affectedNode, lockFreeLinkedListNode);
            }
        }

        /* access modifiers changed from: protected */
        public Object failure(LockFreeLinkedListNode affected) {
            return null;
        }

        /* access modifiers changed from: protected */
        public abstract void finishOnSuccess(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2);

        public abstract void finishPrepare(PrepareOp prepareOp);

        /* access modifiers changed from: protected */
        public abstract LockFreeLinkedListNode getAffectedNode();

        /* access modifiers changed from: protected */
        public abstract LockFreeLinkedListNode getOriginalNext();

        public Object onPrepare(PrepareOp prepareOp) {
            finishPrepare(prepareOp);
            return null;
        }

        public void onRemoved(LockFreeLinkedListNode affected) {
        }

        public final Object prepare(AtomicOp<?> op) {
            while (true) {
                LockFreeLinkedListNode takeAffectedNode = takeAffectedNode(op);
                if (takeAffectedNode == null) {
                    return AtomicKt.RETRY_ATOMIC;
                }
                Object obj = takeAffectedNode._next;
                if (obj == op || op.isDecided()) {
                    return null;
                }
                if (!(obj instanceof OpDescriptor)) {
                    Object failure = failure(takeAffectedNode);
                    if (failure != null) {
                        return failure;
                    }
                    if (retry(takeAffectedNode, obj)) {
                        continue;
                    } else {
                        PrepareOp prepareOp = new PrepareOp(takeAffectedNode, (LockFreeLinkedListNode) obj, this);
                        if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(LockFreeLinkedListNode._next$FU, takeAffectedNode, obj, prepareOp)) {
                            try {
                                Object perform = prepareOp.perform(takeAffectedNode);
                                if (perform != LockFreeLinkedList_commonKt.REMOVE_PREPARED) {
                                    if (DebugKt.getASSERTIONS_ENABLED()) {
                                        if (!(perform == null)) {
                                            throw new AssertionError();
                                        }
                                    }
                                    return null;
                                }
                            } catch (Throwable th) {
                                AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(LockFreeLinkedListNode._next$FU, takeAffectedNode, prepareOp, obj);
                                throw th;
                            }
                        } else {
                            continue;
                        }
                    }
                } else if (op.isEarlierThan((OpDescriptor) obj)) {
                    return AtomicKt.RETRY_ATOMIC;
                } else {
                    ((OpDescriptor) obj).perform(takeAffectedNode);
                }
            }
        }

        /* access modifiers changed from: protected */
        public boolean retry(LockFreeLinkedListNode affected, Object next) {
            return false;
        }

        /* access modifiers changed from: protected */
        public LockFreeLinkedListNode takeAffectedNode(OpDescriptor op) {
            LockFreeLinkedListNode affectedNode = getAffectedNode();
            Intrinsics.checkNotNull(affectedNode);
            return affectedNode;
        }

        public abstract Object updatedNext(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2);
    }

    @Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\b\u0016\u0018\u0000*\f\b\u0000\u0010\u0003*\u00060\u0001j\u0002`\u00022\u00020!B\u001b\u0012\n\u0010\u0004\u001a\u00060\u0001j\u0002`\u0002\u0012\u0006\u0010\u0005\u001a\u00028\u0000¢\u0006\u0004\b\u0006\u0010\u0007J'\u0010\u000b\u001a\u00020\n2\n\u0010\b\u001a\u00060\u0001j\u0002`\u00022\n\u0010\t\u001a\u00060\u0001j\u0002`\u0002H\u0014¢\u0006\u0004\b\u000b\u0010\u0007J\u0017\u0010\u000e\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\fH\u0016¢\u0006\u0004\b\u000e\u0010\u000fJ#\u0010\u0012\u001a\u00020\u00112\n\u0010\b\u001a\u00060\u0001j\u0002`\u00022\u0006\u0010\t\u001a\u00020\u0010H\u0014¢\u0006\u0004\b\u0012\u0010\u0013J\u001f\u0010\u0016\u001a\n\u0018\u00010\u0001j\u0004\u0018\u0001`\u00022\u0006\u0010\u0015\u001a\u00020\u0014H\u0004¢\u0006\u0004\b\u0016\u0010\u0017J'\u0010\u0018\u001a\u00020\u00102\n\u0010\b\u001a\u00060\u0001j\u0002`\u00022\n\u0010\t\u001a\u00060\u0001j\u0002`\u0002H\u0016¢\u0006\u0004\b\u0018\u0010\u0019R\u001c\u0010\u001c\u001a\n\u0018\u00010\u0001j\u0004\u0018\u0001`\u00028DX\u0004¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u0014\u0010\u0005\u001a\u00028\u00008\u0006X\u0004¢\u0006\u0006\n\u0004\b\u0005\u0010\u001dR\u0018\u0010\u001f\u001a\u00060\u0001j\u0002`\u00028DX\u0004¢\u0006\u0006\u001a\u0004\b\u001e\u0010\u001bR\u0018\u0010\u0004\u001a\u00060\u0001j\u0002`\u00028\u0006X\u0004¢\u0006\u0006\n\u0004\b\u0004\u0010\u001d¨\u0006 "}, d2 = {"Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AddLastDesc;", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/internal/Node;", "T", "queue", "node", "<init>", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;Lkotlinx/coroutines/internal/LockFreeLinkedListNode;)V", "affected", "next", "", "finishOnSuccess", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$PrepareOp;", "prepareOp", "finishPrepare", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode$PrepareOp;)V", "", "", "retry", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;Ljava/lang/Object;)Z", "Lkotlinx/coroutines/internal/OpDescriptor;", "op", "takeAffectedNode", "(Lkotlinx/coroutines/internal/OpDescriptor;)Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "updatedNext", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;Lkotlinx/coroutines/internal/LockFreeLinkedListNode;)Ljava/lang/Object;", "getAffectedNode", "()Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "affectedNode", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "getOriginalNext", "originalNext", "kotlinx-coroutines-core", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AbstractAtomicDesc;"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: LockFreeLinkedList.kt */
    public static class AddLastDesc<T extends LockFreeLinkedListNode> extends AbstractAtomicDesc {
        private static final /* synthetic */ AtomicReferenceFieldUpdater _affectedNode$FU = AtomicReferenceFieldUpdater.newUpdater(AddLastDesc.class, Object.class, "_affectedNode");
        private volatile /* synthetic */ Object _affectedNode;
        public final T node;
        public final LockFreeLinkedListNode queue;

        public AddLastDesc(LockFreeLinkedListNode queue2, T node2) {
            this.queue = queue2;
            this.node = node2;
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (!(node2._next == node2 && node2._prev == node2)) {
                    throw new AssertionError();
                }
            }
            this._affectedNode = null;
        }

        /* access modifiers changed from: protected */
        public void finishOnSuccess(LockFreeLinkedListNode affected, LockFreeLinkedListNode next) {
            this.node.finishAdd(this.queue);
        }

        public void finishPrepare(PrepareOp prepareOp) {
            AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_affectedNode$FU, this, (Object) null, prepareOp.affected);
        }

        /* access modifiers changed from: protected */
        public final LockFreeLinkedListNode getAffectedNode() {
            return (LockFreeLinkedListNode) this._affectedNode;
        }

        /* access modifiers changed from: protected */
        public final LockFreeLinkedListNode getOriginalNext() {
            return this.queue;
        }

        /* access modifiers changed from: protected */
        public boolean retry(LockFreeLinkedListNode affected, Object next) {
            return next != this.queue;
        }

        /* access modifiers changed from: protected */
        public final LockFreeLinkedListNode takeAffectedNode(OpDescriptor op) {
            return this.queue.correctPrev(op);
        }

        public Object updatedNext(LockFreeLinkedListNode affected, LockFreeLinkedListNode next) {
            AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(LockFreeLinkedListNode._prev$FU, this.node, this.node, affected);
            AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(LockFreeLinkedListNode._next$FU, this.node, this.node, this.queue);
            return this.node;
        }
    }

    @Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\b!\u0018\u00002\f\u0012\b\u0012\u00060\u0002j\u0002`\u00030\u0001B\u0011\u0012\n\u0010\u0004\u001a\u00060\u0002j\u0002`\u0003¢\u0006\u0002\u0010\u0005J\u001e\u0010\u0007\u001a\u00020\b2\n\u0010\t\u001a\u00060\u0002j\u0002`\u00032\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0016R\u0014\u0010\u0004\u001a\u00060\u0002j\u0002`\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\n\u0018\u00010\u0002j\u0004\u0018\u0001`\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeLinkedListNode$CondAddOp;", "Lkotlinx/coroutines/internal/AtomicOp;", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/internal/Node;", "newNode", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;)V", "oldNext", "complete", "", "affected", "failure", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: LockFreeLinkedList.kt */
    public static abstract class CondAddOp extends AtomicOp<LockFreeLinkedListNode> {
        public final LockFreeLinkedListNode newNode;
        public LockFreeLinkedListNode oldNext;

        public CondAddOp(LockFreeLinkedListNode newNode2) {
            this.newNode = newNode2;
        }

        public void complete(LockFreeLinkedListNode affected, Object failure) {
            boolean z = failure == null;
            LockFreeLinkedListNode lockFreeLinkedListNode = z ? this.newNode : this.oldNext;
            if (lockFreeLinkedListNode != null && AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(LockFreeLinkedListNode._next$FU, affected, this, lockFreeLinkedListNode) && z) {
                LockFreeLinkedListNode lockFreeLinkedListNode2 = this.newNode;
                LockFreeLinkedListNode lockFreeLinkedListNode3 = this.oldNext;
                Intrinsics.checkNotNull(lockFreeLinkedListNode3);
                lockFreeLinkedListNode2.finishAdd(lockFreeLinkedListNode3);
            }
        }
    }

    @Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B%\u0012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004\u0012\n\u0010\u0005\u001a\u00060\u0003j\u0002`\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0006\u0010\r\u001a\u00020\u000eJ\u0014\u0010\u000f\u001a\u0004\u0018\u00010\u00102\b\u0010\u0002\u001a\u0004\u0018\u00010\u0010H\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0016R\u0014\u0010\u0002\u001a\u00060\u0003j\u0002`\u00048\u0006X\u0004¢\u0006\u0002\n\u0000R\u0018\u0010\t\u001a\u0006\u0012\u0002\b\u00030\n8VX\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0010\u0010\u0006\u001a\u00020\u00078\u0006X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00060\u0003j\u0002`\u00048\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeLinkedListNode$PrepareOp;", "Lkotlinx/coroutines/internal/OpDescriptor;", "affected", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/internal/Node;", "next", "desc", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AbstractAtomicDesc;", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;Lkotlinx/coroutines/internal/LockFreeLinkedListNode;Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AbstractAtomicDesc;)V", "atomicOp", "Lkotlinx/coroutines/internal/AtomicOp;", "getAtomicOp", "()Lkotlinx/coroutines/internal/AtomicOp;", "finishPrepare", "", "perform", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: LockFreeLinkedList.kt */
    public static final class PrepareOp extends OpDescriptor {
        public final LockFreeLinkedListNode affected;
        public final AbstractAtomicDesc desc;
        public final LockFreeLinkedListNode next;

        public PrepareOp(LockFreeLinkedListNode affected2, LockFreeLinkedListNode next2, AbstractAtomicDesc desc2) {
            this.affected = affected2;
            this.next = next2;
            this.desc = desc2;
        }

        public final void finishPrepare() {
            this.desc.finishPrepare(this);
        }

        public AtomicOp<?> getAtomicOp() {
            return this.desc.getAtomicOp();
        }

        public Object perform(Object affected2) {
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (!(affected2 == this.affected)) {
                    throw new AssertionError();
                }
            }
            if (affected2 != null) {
                LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) affected2;
                Object onPrepare = this.desc.onPrepare(this);
                if (onPrepare == LockFreeLinkedList_commonKt.REMOVE_PREPARED) {
                    LockFreeLinkedListNode lockFreeLinkedListNode2 = this.next;
                    if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(LockFreeLinkedListNode._next$FU, (LockFreeLinkedListNode) affected2, this, lockFreeLinkedListNode2.removed())) {
                        this.desc.onRemoved((LockFreeLinkedListNode) affected2);
                        LockFreeLinkedListNode unused = lockFreeLinkedListNode2.correctPrev((OpDescriptor) null);
                    }
                    return LockFreeLinkedList_commonKt.REMOVE_PREPARED;
                }
                Object decide = onPrepare != null ? getAtomicOp().decide(onPrepare) : getAtomicOp().getConsensus();
                AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(LockFreeLinkedListNode._next$FU, (LockFreeLinkedListNode) affected2, this, decide == AtomicKt.NO_DECISION ? getAtomicOp() : decide == null ? this.desc.updatedNext((LockFreeLinkedListNode) affected2, this.next) : this.next);
                return null;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlinx.coroutines.internal.LockFreeLinkedListNode{ kotlinx.coroutines.internal.LockFreeLinkedListKt.Node }");
        }

        public String toString() {
            return "PrepareOp(op=" + getAtomicOp() + ')';
        }
    }

    @Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\b\u0016\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020(B\u0013\u0012\n\u0010\u0004\u001a\u00060\u0002j\u0002`\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\u001d\u0010\t\u001a\u0004\u0018\u00010\b2\n\u0010\u0007\u001a\u00060\u0002j\u0002`\u0003H\u0014¢\u0006\u0004\b\t\u0010\nJ'\u0010\r\u001a\u00020\f2\n\u0010\u0007\u001a\u00060\u0002j\u0002`\u00032\n\u0010\u000b\u001a\u00060\u0002j\u0002`\u0003H\u0004¢\u0006\u0004\b\r\u0010\u000eJ\u0017\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016¢\u0006\u0004\b\u0011\u0010\u0012J#\u0010\u0014\u001a\u00020\u00132\n\u0010\u0007\u001a\u00060\u0002j\u0002`\u00032\u0006\u0010\u000b\u001a\u00020\bH\u0004¢\u0006\u0004\b\u0014\u0010\u0015J\u001f\u0010\u0018\u001a\n\u0018\u00010\u0002j\u0004\u0018\u0001`\u00032\u0006\u0010\u0017\u001a\u00020\u0016H\u0004¢\u0006\u0004\b\u0018\u0010\u0019J%\u0010\u001a\u001a\u00020\b2\n\u0010\u0007\u001a\u00060\u0002j\u0002`\u00032\n\u0010\u000b\u001a\u00060\u0002j\u0002`\u0003¢\u0006\u0004\b\u001a\u0010\u001bR\u001c\u0010\u001e\u001a\n\u0018\u00010\u0002j\u0004\u0018\u0001`\u00038DX\u0004¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u001dR\u001c\u0010 \u001a\n\u0018\u00010\u0002j\u0004\u0018\u0001`\u00038DX\u0004¢\u0006\u0006\u001a\u0004\b\u001f\u0010\u001dR\u0018\u0010\u0004\u001a\u00060\u0002j\u0002`\u00038\u0006X\u0004¢\u0006\u0006\n\u0004\b\u0004\u0010!R\u0017\u0010&\u001a\u00028\u00008F¢\u0006\f\u0012\u0004\b$\u0010%\u001a\u0004\b\"\u0010#¨\u0006'"}, d2 = {"Lkotlinx/coroutines/internal/LockFreeLinkedListNode$RemoveFirstDesc;", "T", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/internal/Node;", "queue", "<init>", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;)V", "affected", "", "failure", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;)Ljava/lang/Object;", "next", "", "finishOnSuccess", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;Lkotlinx/coroutines/internal/LockFreeLinkedListNode;)V", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$PrepareOp;", "prepareOp", "finishPrepare", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode$PrepareOp;)V", "", "retry", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;Ljava/lang/Object;)Z", "Lkotlinx/coroutines/internal/OpDescriptor;", "op", "takeAffectedNode", "(Lkotlinx/coroutines/internal/OpDescriptor;)Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "updatedNext", "(Lkotlinx/coroutines/internal/LockFreeLinkedListNode;Lkotlinx/coroutines/internal/LockFreeLinkedListNode;)Ljava/lang/Object;", "getAffectedNode", "()Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "affectedNode", "getOriginalNext", "originalNext", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "getResult", "()Ljava/lang/Object;", "getResult$annotations", "()V", "result", "kotlinx-coroutines-core", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AbstractAtomicDesc;"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* compiled from: LockFreeLinkedList.kt */
    public static class RemoveFirstDesc<T> extends AbstractAtomicDesc {
        private static final /* synthetic */ AtomicReferenceFieldUpdater _affectedNode$FU;
        private static final /* synthetic */ AtomicReferenceFieldUpdater _originalNext$FU;
        private volatile /* synthetic */ Object _affectedNode = null;
        private volatile /* synthetic */ Object _originalNext = null;
        public final LockFreeLinkedListNode queue;

        static {
            Class<RemoveFirstDesc> cls = RemoveFirstDesc.class;
            _affectedNode$FU = AtomicReferenceFieldUpdater.newUpdater(cls, Object.class, "_affectedNode");
            _originalNext$FU = AtomicReferenceFieldUpdater.newUpdater(cls, Object.class, "_originalNext");
        }

        public RemoveFirstDesc(LockFreeLinkedListNode queue2) {
            this.queue = queue2;
        }

        public static /* synthetic */ void getResult$annotations() {
        }

        /* access modifiers changed from: protected */
        public Object failure(LockFreeLinkedListNode affected) {
            if (affected == this.queue) {
                return LockFreeLinkedListKt.getLIST_EMPTY();
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public final void finishOnSuccess(LockFreeLinkedListNode affected, LockFreeLinkedListNode next) {
            LockFreeLinkedListNode unused = next.correctPrev((OpDescriptor) null);
        }

        public void finishPrepare(PrepareOp prepareOp) {
            AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_affectedNode$FU, this, (Object) null, prepareOp.affected);
            AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_originalNext$FU, this, (Object) null, prepareOp.next);
        }

        /* access modifiers changed from: protected */
        public final LockFreeLinkedListNode getAffectedNode() {
            return (LockFreeLinkedListNode) this._affectedNode;
        }

        /* access modifiers changed from: protected */
        public final LockFreeLinkedListNode getOriginalNext() {
            return (LockFreeLinkedListNode) this._originalNext;
        }

        public final T getResult() {
            T affectedNode = getAffectedNode();
            Intrinsics.checkNotNull(affectedNode);
            return (Object) affectedNode;
        }

        /* access modifiers changed from: protected */
        public final boolean retry(LockFreeLinkedListNode affected, Object next) {
            if (!(next instanceof Removed)) {
                return false;
            }
            ((Removed) next).ref.helpRemovePrev();
            return true;
        }

        /* access modifiers changed from: protected */
        public final LockFreeLinkedListNode takeAffectedNode(OpDescriptor op) {
            LockFreeLinkedListNode lockFreeLinkedListNode = this.queue;
            while (true) {
                Object obj = lockFreeLinkedListNode._next;
                if (!(obj instanceof OpDescriptor)) {
                    return (LockFreeLinkedListNode) obj;
                }
                if (op.isEarlierThan((OpDescriptor) obj)) {
                    return null;
                }
                ((OpDescriptor) obj).perform(this.queue);
            }
        }

        public final Object updatedNext(LockFreeLinkedListNode affected, LockFreeLinkedListNode next) {
            return next.removed();
        }
    }

    static {
        Class<LockFreeLinkedListNode> cls = LockFreeLinkedListNode.class;
        _next$FU = AtomicReferenceFieldUpdater.newUpdater(cls, Object.class, "_next");
        _prev$FU = AtomicReferenceFieldUpdater.newUpdater(cls, Object.class, "_prev");
        _removedRef$FU = AtomicReferenceFieldUpdater.newUpdater(cls, Object.class, "_removedRef");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v0, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v4, resolved type: kotlinx.coroutines.internal.OpDescriptor} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v6, resolved type: kotlinx.coroutines.internal.OpDescriptor} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: kotlinx.coroutines.internal.LockFreeLinkedListNode} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v10, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: kotlinx.coroutines.internal.LockFreeLinkedListNode} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v1, resolved type: kotlinx.coroutines.internal.Removed} */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final kotlinx.coroutines.internal.LockFreeLinkedListNode correctPrev(kotlinx.coroutines.internal.OpDescriptor r7) {
        /*
            r6 = this;
        L_0x0001:
            java.lang.Object r0 = r6._prev
            kotlinx.coroutines.internal.LockFreeLinkedListNode r0 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r0
            r1 = r0
            r2 = 0
        L_0x0007:
            java.lang.Object r3 = r1._next
            if (r3 != r6) goto L_0x001a
            if (r0 != r1) goto L_0x0010
            return r1
        L_0x0010:
            java.util.concurrent.atomic.AtomicReferenceFieldUpdater r4 = _prev$FU
            boolean r4 = androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(r4, r6, r0, r1)
            if (r4 != 0) goto L_0x0019
            goto L_0x0001
        L_0x0019:
            return r1
        L_0x001a:
            boolean r4 = r6.isRemoved()
            r5 = 0
            if (r4 == 0) goto L_0x0022
            return r5
        L_0x0022:
            if (r3 != r7) goto L_0x0025
            return r1
        L_0x0025:
            boolean r4 = r3 instanceof kotlinx.coroutines.internal.OpDescriptor
            if (r4 == 0) goto L_0x003c
            if (r7 == 0) goto L_0x0035
            r4 = r3
            kotlinx.coroutines.internal.OpDescriptor r4 = (kotlinx.coroutines.internal.OpDescriptor) r4
            boolean r4 = r7.isEarlierThan(r4)
            if (r4 == 0) goto L_0x0035
            return r5
        L_0x0035:
            r4 = r3
            kotlinx.coroutines.internal.OpDescriptor r4 = (kotlinx.coroutines.internal.OpDescriptor) r4
            r4.perform(r1)
            goto L_0x0001
        L_0x003c:
            boolean r4 = r3 instanceof kotlinx.coroutines.internal.Removed
            if (r4 == 0) goto L_0x0059
            if (r2 == 0) goto L_0x0053
            java.util.concurrent.atomic.AtomicReferenceFieldUpdater r4 = _next$FU
            r5 = r3
            kotlinx.coroutines.internal.Removed r5 = (kotlinx.coroutines.internal.Removed) r5
            kotlinx.coroutines.internal.LockFreeLinkedListNode r5 = r5.ref
            boolean r4 = androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(r4, r2, r1, r5)
            if (r4 != 0) goto L_0x0050
            goto L_0x0001
        L_0x0050:
            r1 = r2
            r2 = 0
            goto L_0x0007
        L_0x0053:
            java.lang.Object r4 = r1._prev
            r1 = r4
            kotlinx.coroutines.internal.LockFreeLinkedListNode r1 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r1
            goto L_0x0007
        L_0x0059:
            r2 = r1
            r1 = r3
            kotlinx.coroutines.internal.LockFreeLinkedListNode r1 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r1
            goto L_0x0007
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.LockFreeLinkedListNode.correctPrev(kotlinx.coroutines.internal.OpDescriptor):kotlinx.coroutines.internal.LockFreeLinkedListNode");
    }

    private final LockFreeLinkedListNode findPrevNonRemoved(LockFreeLinkedListNode current) {
        LockFreeLinkedListNode lockFreeLinkedListNode = current;
        while (lockFreeLinkedListNode.isRemoved()) {
            lockFreeLinkedListNode = (LockFreeLinkedListNode) lockFreeLinkedListNode._prev;
        }
        return lockFreeLinkedListNode;
    }

    /* access modifiers changed from: private */
    public final void finishAdd(LockFreeLinkedListNode next) {
        LockFreeLinkedListNode lockFreeLinkedListNode;
        LockFreeLinkedListNode lockFreeLinkedListNode2 = next;
        do {
            lockFreeLinkedListNode = (LockFreeLinkedListNode) lockFreeLinkedListNode2._prev;
            if (getNext() != next) {
                return;
            }
        } while (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_prev$FU, next, lockFreeLinkedListNode, this));
        if (isRemoved()) {
            next.correctPrev((OpDescriptor) null);
        }
    }

    /* access modifiers changed from: private */
    public final Removed removed() {
        Removed removed = (Removed) this._removedRef;
        if (removed != null) {
            return removed;
        }
        Removed removed2 = new Removed(this);
        _removedRef$FU.lazySet(this, removed2);
        return removed2;
    }

    public final void addLast(LockFreeLinkedListNode node) {
        do {
        } while (!getPrevNode().addNext(node, this));
    }

    /* JADX WARNING: Removed duplicated region for block: B:1:0x000c A[LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean addLastIf(kotlinx.coroutines.internal.LockFreeLinkedListNode r5, kotlin.jvm.functions.Function0<java.lang.Boolean> r6) {
        /*
            r4 = this;
            r0 = 0
            r1 = r4
            r2 = 0
            kotlinx.coroutines.internal.LockFreeLinkedListNode$makeCondAddOp$1 r3 = new kotlinx.coroutines.internal.LockFreeLinkedListNode$makeCondAddOp$1
            r3.<init>(r5, r6)
            kotlinx.coroutines.internal.LockFreeLinkedListNode$CondAddOp r3 = (kotlinx.coroutines.internal.LockFreeLinkedListNode.CondAddOp) r3
            r1 = r3
        L_0x000c:
            kotlinx.coroutines.internal.LockFreeLinkedListNode r2 = r4.getPrevNode()
            int r3 = r2.tryCondAddNext(r5, r4, r1)
            switch(r3) {
                case 1: goto L_0x001b;
                case 2: goto L_0x0019;
                default: goto L_0x0018;
            }
        L_0x0018:
            goto L_0x000c
        L_0x0019:
            r3 = 0
            return r3
        L_0x001b:
            r3 = 1
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.LockFreeLinkedListNode.addLastIf(kotlinx.coroutines.internal.LockFreeLinkedListNode, kotlin.jvm.functions.Function0):boolean");
    }

    public final boolean addLastIfPrev(LockFreeLinkedListNode node, Function1<? super LockFreeLinkedListNode, Boolean> predicate) {
        LockFreeLinkedListNode prevNode;
        do {
            prevNode = getPrevNode();
            if (!predicate.invoke(prevNode).booleanValue()) {
                return false;
            }
        } while (!prevNode.addNext(node, this));
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:1:0x000c A[LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean addLastIfPrevAndIf(kotlinx.coroutines.internal.LockFreeLinkedListNode r6, kotlin.jvm.functions.Function1<? super kotlinx.coroutines.internal.LockFreeLinkedListNode, java.lang.Boolean> r7, kotlin.jvm.functions.Function0<java.lang.Boolean> r8) {
        /*
            r5 = this;
            r0 = 0
            r1 = r5
            r2 = 0
            kotlinx.coroutines.internal.LockFreeLinkedListNode$makeCondAddOp$1 r3 = new kotlinx.coroutines.internal.LockFreeLinkedListNode$makeCondAddOp$1
            r3.<init>(r6, r8)
            kotlinx.coroutines.internal.LockFreeLinkedListNode$CondAddOp r3 = (kotlinx.coroutines.internal.LockFreeLinkedListNode.CondAddOp) r3
            r1 = r3
        L_0x000c:
            kotlinx.coroutines.internal.LockFreeLinkedListNode r2 = r5.getPrevNode()
            java.lang.Object r3 = r7.invoke(r2)
            java.lang.Boolean r3 = (java.lang.Boolean) r3
            boolean r3 = r3.booleanValue()
            r4 = 0
            if (r3 != 0) goto L_0x001f
            return r4
        L_0x001f:
            int r3 = r2.tryCondAddNext(r6, r5, r1)
            switch(r3) {
                case 1: goto L_0x0028;
                case 2: goto L_0x0027;
                default: goto L_0x0026;
            }
        L_0x0026:
            goto L_0x000c
        L_0x0027:
            return r4
        L_0x0028:
            r3 = 1
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.LockFreeLinkedListNode.addLastIfPrevAndIf(kotlinx.coroutines.internal.LockFreeLinkedListNode, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function0):boolean");
    }

    public final boolean addNext(LockFreeLinkedListNode node, LockFreeLinkedListNode next) {
        _prev$FU.lazySet(node, this);
        AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = _next$FU;
        atomicReferenceFieldUpdater.lazySet(node, next);
        if (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(atomicReferenceFieldUpdater, this, next, node)) {
            return false;
        }
        node.finishAdd(next);
        return true;
    }

    public final boolean addOneIfEmpty(LockFreeLinkedListNode node) {
        _prev$FU.lazySet(node, this);
        _next$FU.lazySet(node, this);
        while (getNext() == this) {
            if (AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_next$FU, this, this, node)) {
                node.finishAdd(this);
                return true;
            }
        }
        return false;
    }

    public final <T extends LockFreeLinkedListNode> AddLastDesc<T> describeAddLast(T node) {
        return new AddLastDesc<>(this, node);
    }

    public final RemoveFirstDesc<LockFreeLinkedListNode> describeRemoveFirst() {
        return new RemoveFirstDesc<>(this);
    }

    public final Object getNext() {
        while (true) {
            Object obj = this._next;
            if (!(obj instanceof OpDescriptor)) {
                return obj;
            }
            ((OpDescriptor) obj).perform(this);
        }
    }

    public final LockFreeLinkedListNode getNextNode() {
        return LockFreeLinkedListKt.unwrap(getNext());
    }

    public final LockFreeLinkedListNode getPrevNode() {
        LockFreeLinkedListNode correctPrev = correctPrev((OpDescriptor) null);
        return correctPrev == null ? findPrevNonRemoved((LockFreeLinkedListNode) this._prev) : correctPrev;
    }

    public final void helpRemove() {
        ((Removed) getNext()).ref.helpRemovePrev();
    }

    public final void helpRemovePrev() {
        LockFreeLinkedListNode lockFreeLinkedListNode = this;
        while (true) {
            Object next = lockFreeLinkedListNode.getNext();
            if (!(next instanceof Removed)) {
                lockFreeLinkedListNode.correctPrev((OpDescriptor) null);
                return;
            }
            lockFreeLinkedListNode = ((Removed) next).ref;
        }
    }

    public boolean isRemoved() {
        return getNext() instanceof Removed;
    }

    public final CondAddOp makeCondAddOp(LockFreeLinkedListNode node, Function0<Boolean> condition) {
        return new LockFreeLinkedListNode$makeCondAddOp$1(node, condition);
    }

    /* access modifiers changed from: protected */
    public LockFreeLinkedListNode nextIfRemoved() {
        Object next = getNext();
        Removed removed = next instanceof Removed ? (Removed) next : null;
        if (removed == null) {
            return null;
        }
        return removed.ref;
    }

    public boolean remove() {
        return removeOrNext() == null;
    }

    public final /* synthetic */ <T> T removeFirstIfIsInstanceOfOrPeekIf(Function1<? super T, Boolean> predicate) {
        LockFreeLinkedListNode removeOrNext;
        while (true) {
            T t = (LockFreeLinkedListNode) getNext();
            if (t == this) {
                return null;
            }
            Intrinsics.reifiedOperationMarker(3, "T");
            if (!(t instanceof Object)) {
                return null;
            }
            if ((predicate.invoke(t).booleanValue() && !t.isRemoved()) || (removeOrNext = t.removeOrNext()) == null) {
                return t;
            }
            removeOrNext.helpRemovePrev();
        }
    }

    public final LockFreeLinkedListNode removeFirstOrNull() {
        while (true) {
            LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) getNext();
            if (lockFreeLinkedListNode == this) {
                return null;
            }
            if (lockFreeLinkedListNode.remove()) {
                return lockFreeLinkedListNode;
            }
            lockFreeLinkedListNode.helpRemove();
        }
    }

    public final LockFreeLinkedListNode removeOrNext() {
        Object next;
        do {
            next = getNext();
            if (next instanceof Removed) {
                return ((Removed) next).ref;
            }
            if (next == this) {
                return (LockFreeLinkedListNode) next;
            }
        } while (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(_next$FU, this, next, ((LockFreeLinkedListNode) next).removed()));
        ((LockFreeLinkedListNode) next).correctPrev((OpDescriptor) null);
        return null;
    }

    public String toString() {
        StringBuilder append = new StringBuilder().append(new LockFreeLinkedListNode$toString$1(this)).append('@');
        String hexAddress = DebugStringsKt.getHexAddress(this);
        Log1F380D.a((Object) hexAddress);
        return append.append(hexAddress).toString();
    }

    public final int tryCondAddNext(LockFreeLinkedListNode node, LockFreeLinkedListNode next, CondAddOp condAdd) {
        _prev$FU.lazySet(node, this);
        AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = _next$FU;
        atomicReferenceFieldUpdater.lazySet(node, next);
        condAdd.oldNext = next;
        if (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(atomicReferenceFieldUpdater, this, next, condAdd)) {
            return 0;
        }
        return condAdd.perform(this) == null ? 1 : 2;
    }

    public final void validateNode$kotlinx_coroutines_core(LockFreeLinkedListNode prev, LockFreeLinkedListNode next) {
        boolean z = true;
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(prev == this._prev)) {
                throw new AssertionError();
            }
        }
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (next != this._next) {
                z = false;
            }
            if (!z) {
                throw new AssertionError();
            }
        }
    }
}

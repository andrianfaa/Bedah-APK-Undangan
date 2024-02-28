package kotlinx.coroutines.internal;

import kotlin.Metadata;
import kotlinx.coroutines.DebugStringsKt;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\b&\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0000J\u0014\u0010\n\u001a\u0004\u0018\u00010\u00012\b\u0010\u000b\u001a\u0004\u0018\u00010\u0001H&J\b\u0010\f\u001a\u00020\rH\u0016R\u0018\u0010\u0003\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/internal/OpDescriptor;", "", "()V", "atomicOp", "Lkotlinx/coroutines/internal/AtomicOp;", "getAtomicOp", "()Lkotlinx/coroutines/internal/AtomicOp;", "isEarlierThan", "", "that", "perform", "affected", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: 017A */
public abstract class OpDescriptor {
    public abstract AtomicOp<?> getAtomicOp();

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0008, code lost:
        r2 = r8.getAtomicOp();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean isEarlierThan(kotlinx.coroutines.internal.OpDescriptor r8) {
        /*
            r7 = this;
            kotlinx.coroutines.internal.AtomicOp r0 = r7.getAtomicOp()
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            kotlinx.coroutines.internal.AtomicOp r2 = r8.getAtomicOp()
            if (r2 != 0) goto L_0x000f
            return r1
        L_0x000f:
            long r3 = r0.getOpSequence()
            long r5 = r2.getOpSequence()
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 >= 0) goto L_0x001c
            r1 = 1
        L_0x001c:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.OpDescriptor.isEarlierThan(kotlinx.coroutines.internal.OpDescriptor):boolean");
    }

    public abstract Object perform(Object obj);

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String classSimpleName = DebugStringsKt.getClassSimpleName(this);
        Log1F380D.a((Object) classSimpleName);
        StringBuilder append = sb.append(classSimpleName).append('@');
        String hexAddress = DebugStringsKt.getHexAddress(this);
        Log1F380D.a((Object) hexAddress);
        return append.append(hexAddress).toString();
    }
}

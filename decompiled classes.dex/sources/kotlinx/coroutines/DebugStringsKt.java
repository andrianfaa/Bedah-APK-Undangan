package kotlinx.coroutines;

import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.internal.DispatchedContinuation;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0007\u001a\u00020\u0001*\u0006\u0012\u0002\b\u00030\bH\u0000\"\u0018\u0010\u0000\u001a\u00020\u0001*\u00020\u00028@X\u0004¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0018\u0010\u0005\u001a\u00020\u0001*\u00020\u00028@X\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0004¨\u0006\t"}, d2 = {"classSimpleName", "", "", "getClassSimpleName", "(Ljava/lang/Object;)Ljava/lang/String;", "hexAddress", "getHexAddress", "toDebugString", "Lkotlin/coroutines/Continuation;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: 0174 */
public final class DebugStringsKt {
    public static final String getClassSimpleName(Object $this$classSimpleName) {
        return $this$classSimpleName.getClass().getSimpleName();
    }

    public static final String getHexAddress(Object $this$hexAddress) {
        String hexString = Integer.toHexString(System.identityHashCode($this$hexAddress));
        Log1F380D.a((Object) hexString);
        return hexString;
    }

    public static final String toDebugString(Continuation<?> $this$toDebugString) {
        String str;
        if ($this$toDebugString instanceof DispatchedContinuation) {
            return $this$toDebugString.toString();
        }
        try {
            Result.Companion companion = Result.Companion;
            Continuation<?> continuation = $this$toDebugString;
            StringBuilder append = new StringBuilder().append(continuation).append('@');
            String hexAddress = getHexAddress(continuation);
            Log1F380D.a((Object) hexAddress);
            str = Result.m36constructorimpl(append.append(hexAddress).toString());
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            str = Result.m36constructorimpl(ResultKt.createFailure(th));
        }
        Throwable r2 = Result.m39exceptionOrNullimpl(str);
        if (r2 != null) {
            Throwable th2 = r2;
            StringBuilder append2 = new StringBuilder().append($this$toDebugString.getClass().getName()).append('@');
            String hexAddress2 = getHexAddress($this$toDebugString);
            Log1F380D.a((Object) hexAddress2);
            str = append2.append(hexAddress2).toString();
        }
        return (String) str;
    }
}

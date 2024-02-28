package kotlin.concurrent;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u0000:\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001aJ\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u001a3\u0010\u000e\u001a\u0002H\u000f\"\b\b\u0000\u0010\u000f*\u00020\u0010*\b\u0012\u0004\u0012\u0002H\u000f0\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u000f0\fH\bø\u0001\u0000¢\u0006\u0002\u0010\u0013\u0002\u0007\n\u0005\b20\u0001¨\u0006\u0014"}, d2 = {"thread", "Ljava/lang/Thread;", "start", "", "isDaemon", "contextClassLoader", "Ljava/lang/ClassLoader;", "name", "", "priority", "", "block", "Lkotlin/Function0;", "", "getOrSet", "T", "", "Ljava/lang/ThreadLocal;", "default", "(Ljava/lang/ThreadLocal;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "kotlin-stdlib"}, k = 2, mv = {1, 7, 1}, xi = 48)
/* compiled from: Thread.kt */
public final class ThreadsKt {
    private static final <T> T getOrSet(ThreadLocal<T> $this$getOrSet, Function0<? extends T> function0) {
        Intrinsics.checkNotNullParameter($this$getOrSet, "<this>");
        Intrinsics.checkNotNullParameter(function0, "default");
        T t = $this$getOrSet.get();
        if (t != null) {
            return t;
        }
        T invoke = function0.invoke();
        $this$getOrSet.set(invoke);
        return invoke;
    }

    public static final Thread thread(boolean start, boolean isDaemon, ClassLoader contextClassLoader, String name, int priority, Function0<Unit> block) {
        Intrinsics.checkNotNullParameter(block, "block");
        ThreadsKt$thread$thread$1 threadsKt$thread$thread$1 = new ThreadsKt$thread$thread$1(block);
        if (isDaemon) {
            threadsKt$thread$thread$1.setDaemon(true);
        }
        if (priority > 0) {
            threadsKt$thread$thread$1.setPriority(priority);
        }
        if (name != null) {
            threadsKt$thread$thread$1.setName(name);
        }
        if (contextClassLoader != null) {
            threadsKt$thread$thread$1.setContextClassLoader(contextClassLoader);
        }
        if (start) {
            threadsKt$thread$thread$1.start();
        }
        return threadsKt$thread$thread$1;
    }

    public static /* synthetic */ Thread thread$default(boolean z, boolean z2, ClassLoader classLoader, String str, int i, Function0 function0, int i2, Object obj) {
        return thread((i2 & 1) != 0 ? true : z, (i2 & 2) != 0 ? false : z2, (i2 & 4) != 0 ? null : classLoader, (i2 & 8) != 0 ? null : str, (i2 & 16) != 0 ? -1 : i, function0);
    }
}

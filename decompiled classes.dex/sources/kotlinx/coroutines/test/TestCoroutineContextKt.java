package kotlinx.coroutines.test;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CancellationException;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a+\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0017\u0010\u0004\u001a\u0013\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u0005¢\u0006\u0002\b\u0006H\u0007¨\u0006\u0007"}, d2 = {"withTestContext", "", "testContext", "Lkotlinx/coroutines/test/TestCoroutineContext;", "testBody", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: 01B1 */
public final class TestCoroutineContextKt {
    @Deprecated(level = DeprecationLevel.ERROR, message = "This API has been deprecated to integrate with Structured Concurrency.", replaceWith = @ReplaceWith(expression = "testContext.runBlockingTest(testBody)", imports = {"kotlin.coroutines.test"}))
    public static final void withTestContext(TestCoroutineContext testContext, Function1<? super TestCoroutineContext, Unit> testBody) {
        TestCoroutineContext testCoroutineContext = testContext;
        testBody.invoke(testCoroutineContext);
        Iterable exceptions = testCoroutineContext.getExceptions();
        boolean z = true;
        if (!(exceptions instanceof Collection) || !((Collection) exceptions).isEmpty()) {
            Iterator it = exceptions.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (!(((Throwable) it.next()) instanceof CancellationException)) {
                        z = false;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        if (!z) {
            String stringPlus = Intrinsics.stringPlus("Coroutine encountered unhandled exceptions:\n", testCoroutineContext.getExceptions());
            Log1F380D.a((Object) stringPlus);
            throw new AssertionError(stringPlus);
        }
    }

    public static /* synthetic */ void withTestContext$default(TestCoroutineContext testCoroutineContext, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            testCoroutineContext = new TestCoroutineContext((String) null, 1, (DefaultConstructorMarker) null);
        }
        withTestContext(testCoroutineContext, function1);
    }
}

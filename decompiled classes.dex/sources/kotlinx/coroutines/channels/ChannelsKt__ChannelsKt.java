package kotlinx.coroutines.channels;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.channels.ChannelResult;

@Metadata(d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a%\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u0007¢\u0006\u0002\u0010\u0005\u001a,\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0004\u001a\u0002H\u0002ø\u0001\u0000¢\u0006\u0002\u0010\b\u0002\u0004\n\u0002\b\u0019¨\u0006\t"}, d2 = {"sendBlocking", "", "E", "Lkotlinx/coroutines/channels/SendChannel;", "element", "(Lkotlinx/coroutines/channels/SendChannel;Ljava/lang/Object;)V", "trySendBlocking", "Lkotlinx/coroutines/channels/ChannelResult;", "(Lkotlinx/coroutines/channels/SendChannel;Ljava/lang/Object;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 5, mv = {1, 6, 0}, xi = 48, xs = "kotlinx/coroutines/channels/ChannelsKt")
/* compiled from: Channels.kt */
final /* synthetic */ class ChannelsKt__ChannelsKt {
    @Deprecated(level = DeprecationLevel.ERROR, message = "Deprecated in the favour of 'trySendBlocking'. Consider handling the result of 'trySendBlocking' explicitly and rethrow exception if necessary", replaceWith = @ReplaceWith(expression = "trySendBlocking(element)", imports = {}))
    public static final <E> void sendBlocking(SendChannel<? super E> $this$sendBlocking, E element) {
        if (!ChannelResult.m1544isSuccessimpl($this$sendBlocking.m1556trySendJP2dKIU(element))) {
            Object unused = BuildersKt__BuildersKt.runBlocking$default((CoroutineContext) null, new ChannelsKt__ChannelsKt$sendBlocking$1($this$sendBlocking, element, (Continuation<? super ChannelsKt__ChannelsKt$sendBlocking$1>) null), 1, (Object) null);
        }
    }

    public static final <E> Object trySendBlocking(SendChannel<? super E> $this$trySendBlocking, E element) {
        Object r0 = $this$trySendBlocking.m1556trySendJP2dKIU(element);
        if (r0 instanceof ChannelResult.Failed) {
            return ((ChannelResult) BuildersKt__BuildersKt.runBlocking$default((CoroutineContext) null, new ChannelsKt__ChannelsKt$trySendBlocking$2($this$trySendBlocking, element, (Continuation<? super ChannelsKt__ChannelsKt$trySendBlocking$2>) null), 1, (Object) null)).m1546unboximpl();
        }
        Unit unit = (Unit) r0;
        return ChannelResult.Companion.m1549successJP2dKIU(Unit.INSTANCE);
    }
}

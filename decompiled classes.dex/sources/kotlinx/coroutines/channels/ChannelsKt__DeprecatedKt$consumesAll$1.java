package kotlinx.coroutines.channels;

import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "cause", "", "invoke"}, k = 3, mv = {1, 6, 0}, xi = 48)
/* compiled from: Deprecated.kt */
final class ChannelsKt__DeprecatedKt$consumesAll$1 extends Lambda implements Function1<Throwable, Unit> {
    final /* synthetic */ ReceiveChannel<?>[] $channels;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ChannelsKt__DeprecatedKt$consumesAll$1(ReceiveChannel<?>[] receiveChannelArr) {
        super(1);
        this.$channels = receiveChannelArr;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object p1) {
        invoke((Throwable) p1);
        return Unit.INSTANCE;
    }

    public final void invoke(Throwable cause) {
        Throwable th = null;
        ReceiveChannel<?>[] receiveChannelArr = this.$channels;
        int length = receiveChannelArr.length;
        int i = 0;
        while (i < length) {
            ReceiveChannel<?> receiveChannel = receiveChannelArr[i];
            i++;
            try {
                ChannelsKt.cancelConsumed(receiveChannel, cause);
            } catch (Throwable th2) {
                if (th == null) {
                    th = th2;
                } else {
                    ExceptionsKt.addSuppressed(th, th2);
                }
            }
        }
        if (th != null) {
            throw th;
        }
    }
}

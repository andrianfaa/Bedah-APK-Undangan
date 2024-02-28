package kotlin.collections;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\r\n\u0002\b\u0004\u0010\u0000\u001a\u00020\u0001\"\u0006\b\u0000\u0010\u0002 \u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "E", "it", "invoke", "(Ljava/lang/Object;)Ljava/lang/CharSequence;"}, k = 3, mv = {1, 7, 1}, xi = 48)
/* compiled from: 0120 */
final class AbstractCollection$toString$1 extends Lambda implements Function1<E, CharSequence> {
    final /* synthetic */ AbstractCollection<E> this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    AbstractCollection$toString$1(AbstractCollection<? extends E> abstractCollection) {
        super(1);
        this.this$0 = abstractCollection;
    }

    public final CharSequence invoke(E it) {
        String str;
        if (it == this.this$0) {
            str = "(this Collection)";
        } else {
            str = String.valueOf(it);
            Log1F380D.a((Object) str);
        }
        return str;
    }
}

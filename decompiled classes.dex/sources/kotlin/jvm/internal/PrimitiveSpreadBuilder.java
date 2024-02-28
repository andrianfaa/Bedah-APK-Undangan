package kotlin.jvm.internal;

import kotlin.Metadata;
import kotlin.collections.IntIterator;
import kotlin.ranges.IntRange;

@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\t\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0013\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00028\u0000¢\u0006\u0002\u0010\u0012J\b\u0010\u0003\u001a\u00020\u0004H\u0004J\u001d\u0010\u0013\u001a\u00028\u00002\u0006\u0010\u0014\u001a\u00028\u00002\u0006\u0010\u0015\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010\u0016J\u0011\u0010\u0017\u001a\u00020\u0004*\u00028\u0000H$¢\u0006\u0002\u0010\u0018R\u001a\u0010\u0006\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\u0005R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u000bX\u0004¢\u0006\n\n\u0002\u0010\u000e\u0012\u0004\b\f\u0010\r¨\u0006\u0019"}, d2 = {"Lkotlin/jvm/internal/PrimitiveSpreadBuilder;", "T", "", "size", "", "(I)V", "position", "getPosition", "()I", "setPosition", "spreads", "", "getSpreads$annotations", "()V", "[Ljava/lang/Object;", "addSpread", "", "spreadArgument", "(Ljava/lang/Object;)V", "toArray", "values", "result", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "getSize", "(Ljava/lang/Object;)I", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
/* compiled from: PrimitiveSpreadBuilders.kt */
public abstract class PrimitiveSpreadBuilder<T> {
    private int position;
    private final int size;
    private final T[] spreads;

    public PrimitiveSpreadBuilder(int size2) {
        this.size = size2;
        this.spreads = new Object[size2];
    }

    private static /* synthetic */ void getSpreads$annotations() {
    }

    public final void addSpread(T spreadArgument) {
        Intrinsics.checkNotNullParameter(spreadArgument, "spreadArgument");
        T[] tArr = this.spreads;
        int i = this.position;
        this.position = i + 1;
        tArr[i] = spreadArgument;
    }

    /* access modifiers changed from: protected */
    public final int getPosition() {
        return this.position;
    }

    /* access modifiers changed from: protected */
    public abstract int getSize(T t);

    /* access modifiers changed from: protected */
    public final void setPosition(int i) {
        this.position = i;
    }

    /* access modifiers changed from: protected */
    public final int size() {
        int i = 0;
        IntIterator it = new IntRange(0, this.size - 1).iterator();
        while (it.hasNext()) {
            T t = this.spreads[it.nextInt()];
            i += t != null ? getSize(t) : 1;
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public final T toArray(T values, T result) {
        Intrinsics.checkNotNullParameter(values, "values");
        Intrinsics.checkNotNullParameter(result, "result");
        int i = 0;
        int i2 = 0;
        IntIterator it = new IntRange(0, this.size - 1).iterator();
        while (it.hasNext()) {
            int nextInt = it.nextInt();
            T t = this.spreads[nextInt];
            if (t != null) {
                if (i2 < nextInt) {
                    System.arraycopy(values, i2, result, i, nextInt - i2);
                    i += nextInt - i2;
                }
                int size2 = getSize(t);
                System.arraycopy(t, 0, result, i, size2);
                i += size2;
                i2 = nextInt + 1;
            }
        }
        int i3 = this.size;
        if (i2 < i3) {
            System.arraycopy(values, i2, result, i, i3 - i2);
        }
        return result;
    }
}

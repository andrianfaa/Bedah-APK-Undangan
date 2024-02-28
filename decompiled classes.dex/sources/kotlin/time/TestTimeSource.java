package kotlin.time;

import kotlin.Metadata;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001a\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002ø\u0001\u0000¢\u0006\u0004\b\t\u0010\nJ\u001b\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002ø\u0001\u0000¢\u0006\u0004\b\f\u0010\nJ\b\u0010\r\u001a\u00020\u0004H\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006\u000e"}, d2 = {"Lkotlin/time/TestTimeSource;", "Lkotlin/time/AbstractLongTimeSource;", "()V", "reading", "", "overflow", "", "duration", "Lkotlin/time/Duration;", "overflow-LRDsOJo", "(J)V", "plusAssign", "plusAssign-LRDsOJo", "read", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
/* compiled from: 0168 */
public final class TestTimeSource extends AbstractLongTimeSource {
    private long reading;

    public TestTimeSource() {
        super(DurationUnit.NANOSECONDS);
    }

    /* renamed from: overflow-LRDsOJo  reason: not valid java name */
    private final void m1482overflowLRDsOJo(long duration) {
        StringBuilder append = new StringBuilder().append("TestTimeSource will overflow if its reading ").append(this.reading).append("ns is advanced by ");
        String r2 = Duration.m1398toStringimpl(duration);
        Log1F380D.a((Object) r2);
        throw new IllegalStateException(append.append(r2).append('.').toString());
    }

    /* renamed from: plusAssign-LRDsOJo  reason: not valid java name */
    public final void m1483plusAssignLRDsOJo(long duration) {
        long j;
        long r0 = Duration.m1395toLongimpl(duration, getUnit());
        if (r0 == Long.MIN_VALUE || r0 == Long.MAX_VALUE) {
            double r4 = ((double) this.reading) + Duration.m1392toDoubleimpl(duration, getUnit());
            if (r4 > 9.223372036854776E18d || r4 < -9.223372036854776E18d) {
                m1482overflowLRDsOJo(duration);
            }
            j = (long) r4;
        } else {
            long j2 = this.reading;
            j = j2 + r0;
            if ((j2 ^ r0) >= 0 && (j2 ^ j) < 0) {
                m1482overflowLRDsOJo(duration);
            }
        }
        this.reading = j;
    }

    /* access modifiers changed from: protected */
    public long read() {
        return this.reading;
    }
}

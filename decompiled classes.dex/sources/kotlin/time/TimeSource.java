package kotlin.time;

import kotlin.Metadata;
import kotlin.jvm.JvmInline;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bg\u0018\u0000 \u00042\u00020\u0001:\u0002\u0004\u0005J\b\u0010\u0002\u001a\u00020\u0003H&¨\u0006\u0006"}, d2 = {"Lkotlin/time/TimeSource;", "", "markNow", "Lkotlin/time/TimeMark;", "Companion", "Monotonic", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
/* compiled from: TimeSource.kt */
public interface TimeSource {
    public static final Companion Companion = Companion.$$INSTANCE;

    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lkotlin/time/TimeSource$Companion;", "", "()V", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: TimeSource.kt */
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = new Companion();

        private Companion() {
        }
    }

    @Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\tB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0015\u0010\u0003\u001a\u00020\u0004H\u0016ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\u0005\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016\u0002\b\n\u0002\b!\n\u0002\b\u0019¨\u0006\n"}, d2 = {"Lkotlin/time/TimeSource$Monotonic;", "Lkotlin/time/TimeSource;", "()V", "markNow", "Lkotlin/time/TimeSource$Monotonic$ValueTimeMark;", "markNow-z9LOYto", "()J", "toString", "", "ValueTimeMark", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: TimeSource.kt */
    public static final class Monotonic implements TimeSource {
        public static final Monotonic INSTANCE = new Monotonic();

        @JvmInline
        @Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\u0003\b@\u0018\u00002\u00020\u0001B\u0018\b\u0000\u0012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006J\u0015\u0010\u0007\u001a\u00020\bH\u0016ø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b\t\u0010\u0006J\u001a\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rHÖ\u0003¢\u0006\u0004\b\u000e\u0010\u000fJ\u000f\u0010\u0010\u001a\u00020\u000bH\u0016¢\u0006\u0004\b\u0011\u0010\u0012J\u000f\u0010\u0013\u001a\u00020\u000bH\u0016¢\u0006\u0004\b\u0014\u0010\u0012J\u0010\u0010\u0015\u001a\u00020\u0016HÖ\u0001¢\u0006\u0004\b\u0017\u0010\u0018J\u001b\u0010\u0019\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\bH\u0002ø\u0001\u0000¢\u0006\u0004\b\u001b\u0010\u001cJ\u001b\u0010\u001d\u001a\u00020\u00002\u0006\u0010\u001a\u001a\u00020\bH\u0002ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001cJ\u0010\u0010\u001f\u001a\u00020 HÖ\u0001¢\u0006\u0004\b!\u0010\"R\u0012\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004X\u0004¢\u0006\u0002\n\u0000\u0001\u0002\u0001\u00060\u0003j\u0002`\u0004ø\u0001\u0000\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u0006#"}, d2 = {"Lkotlin/time/TimeSource$Monotonic$ValueTimeMark;", "Lkotlin/time/TimeMark;", "reading", "", "Lkotlin/time/ValueTimeMarkReading;", "constructor-impl", "(J)J", "elapsedNow", "Lkotlin/time/Duration;", "elapsedNow-UwyO8pc", "equals", "", "other", "", "equals-impl", "(JLjava/lang/Object;)Z", "hasNotPassedNow", "hasNotPassedNow-impl", "(J)Z", "hasPassedNow", "hasPassedNow-impl", "hashCode", "", "hashCode-impl", "(J)I", "minus", "duration", "minus-LRDsOJo", "(JJ)J", "plus", "plus-LRDsOJo", "toString", "", "toString-impl", "(J)Ljava/lang/String;", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
        /* compiled from: 0169 */
        public static final class ValueTimeMark implements TimeMark {
            private final long reading;

            private /* synthetic */ ValueTimeMark(long reading2) {
                this.reading = reading2;
            }

            /* renamed from: box-impl  reason: not valid java name */
            public static final /* synthetic */ ValueTimeMark m1490boximpl(long j) {
                return new ValueTimeMark(j);
            }

            /* renamed from: constructor-impl  reason: not valid java name */
            public static long m1491constructorimpl(long j) {
                return j;
            }

            /* renamed from: elapsedNow-UwyO8pc  reason: not valid java name */
            public static long m1492elapsedNowUwyO8pc(long arg0) {
                return MonotonicTimeSource.INSTANCE.m1480elapsedFrom6eNON_k(arg0);
            }

            /* renamed from: equals-impl  reason: not valid java name */
            public static boolean m1493equalsimpl(long j, Object obj) {
                return (obj instanceof ValueTimeMark) && j == ((ValueTimeMark) obj).m1506unboximpl();
            }

            /* renamed from: equals-impl0  reason: not valid java name */
            public static final boolean m1494equalsimpl0(long j, long j2) {
                return j == j2;
            }

            /* renamed from: hasNotPassedNow-impl  reason: not valid java name */
            public static boolean m1495hasNotPassedNowimpl(long arg0) {
                return Duration.m1382isNegativeimpl(m1492elapsedNowUwyO8pc(arg0));
            }

            /* renamed from: hasPassedNow-impl  reason: not valid java name */
            public static boolean m1496hasPassedNowimpl(long arg0) {
                return !Duration.m1382isNegativeimpl(m1492elapsedNowUwyO8pc(arg0));
            }

            /* renamed from: hashCode-impl  reason: not valid java name */
            public static int m1497hashCodeimpl(long j) {
                return (int) ((j >>> 32) ^ j);
            }

            /* renamed from: minus-LRDsOJo  reason: not valid java name */
            public static long m1498minusLRDsOJo(long arg0, long duration) {
                return MonotonicTimeSource.INSTANCE.m1479adjustReading6QKq23U(arg0, Duration.m1401unaryMinusUwyO8pc(duration));
            }

            /* renamed from: plus-LRDsOJo  reason: not valid java name */
            public static long m1499plusLRDsOJo(long arg0, long duration) {
                return MonotonicTimeSource.INSTANCE.m1479adjustReading6QKq23U(arg0, duration);
            }

            /* renamed from: toString-impl  reason: not valid java name */
            public static String m1500toStringimpl(long j) {
                return "ValueTimeMark(reading=" + j + ')';
            }

            /* renamed from: elapsedNow-UwyO8pc  reason: not valid java name */
            public long m1501elapsedNowUwyO8pc() {
                return m1492elapsedNowUwyO8pc(this.reading);
            }

            public boolean equals(Object obj) {
                return m1493equalsimpl(this.reading, obj);
            }

            public boolean hasNotPassedNow() {
                return m1495hasNotPassedNowimpl(this.reading);
            }

            public boolean hasPassedNow() {
                return m1496hasPassedNowimpl(this.reading);
            }

            public int hashCode() {
                return m1497hashCodeimpl(this.reading);
            }

            /* renamed from: minus-LRDsOJo  reason: not valid java name */
            public long m1502minusLRDsOJo(long duration) {
                return m1498minusLRDsOJo(this.reading, duration);
            }

            /* renamed from: plus-LRDsOJo  reason: not valid java name */
            public long m1504plusLRDsOJo(long duration) {
                return m1499plusLRDsOJo(this.reading, duration);
            }

            public String toString() {
                String r0 = m1500toStringimpl(this.reading);
                Log1F380D.a((Object) r0);
                return r0;
            }

            /* renamed from: unbox-impl  reason: not valid java name */
            public final /* synthetic */ long m1506unboximpl() {
                return this.reading;
            }
        }

        private Monotonic() {
        }

        public /* bridge */ /* synthetic */ TimeMark markNow() {
            return ValueTimeMark.m1490boximpl(m1489markNowz9LOYto());
        }

        /* renamed from: markNow-z9LOYto  reason: not valid java name */
        public long m1489markNowz9LOYto() {
            return MonotonicTimeSource.INSTANCE.m1481markNowz9LOYto();
        }

        public String toString() {
            return MonotonicTimeSource.INSTANCE.toString();
        }
    }

    TimeMark markNow();
}

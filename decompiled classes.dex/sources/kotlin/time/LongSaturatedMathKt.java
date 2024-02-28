package kotlin.time;

import kotlin.Metadata;
import kotlin.time.Duration;

@Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0001H\u0002ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0007\u001a\"\u0010\b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0000ø\u0001\u0000¢\u0006\u0004\b\t\u0010\n\u001a\"\u0010\u000b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0002ø\u0001\u0000¢\u0006\u0004\b\f\u0010\n\u001a \u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0000ø\u0001\u0000¢\u0006\u0002\u0010\n\u0002\u0004\n\u0002\b\u0019¨\u0006\u0010"}, d2 = {"checkInfiniteSumDefined", "", "longNs", "duration", "Lkotlin/time/Duration;", "durationNs", "checkInfiniteSumDefined-PjuGub4", "(JJJ)J", "saturatingAdd", "saturatingAdd-pTJri5U", "(JJ)J", "saturatingAddInHalves", "saturatingAddInHalves-pTJri5U", "saturatingDiff", "valueNs", "originNs", "kotlin-stdlib"}, k = 2, mv = {1, 7, 1}, xi = 48)
/* compiled from: longSaturatedMath.kt */
public final class LongSaturatedMathKt {
    /* renamed from: checkInfiniteSumDefined-PjuGub4  reason: not valid java name */
    private static final long m1476checkInfiniteSumDefinedPjuGub4(long longNs, long duration, long durationNs) {
        if (!Duration.m1381isInfiniteimpl(duration) || (longNs ^ durationNs) >= 0) {
            return longNs;
        }
        throw new IllegalArgumentException("Summing infinities of different signs");
    }

    /* renamed from: saturatingAdd-pTJri5U  reason: not valid java name */
    public static final long m1477saturatingAddpTJri5U(long longNs, long duration) {
        long r6 = Duration.m1369getInWholeNanosecondsimpl(duration);
        if (((longNs - 1) | 1) == Long.MAX_VALUE) {
            return m1476checkInfiniteSumDefinedPjuGub4(longNs, duration, r6);
        }
        if ((1 | (r6 - 1)) == Long.MAX_VALUE) {
            return m1478saturatingAddInHalvespTJri5U(longNs, duration);
        }
        long j = longNs + r6;
        return ((longNs ^ j) & (r6 ^ j)) < 0 ? longNs < 0 ? Long.MIN_VALUE : Long.MAX_VALUE : j;
    }

    /* renamed from: saturatingAddInHalves-pTJri5U  reason: not valid java name */
    private static final long m1478saturatingAddInHalvespTJri5U(long longNs, long duration) {
        long r0 = Duration.m1352divUwyO8pc(duration, 2);
        return ((Duration.m1369getInWholeNanosecondsimpl(r0) - 1) | 1) == Long.MAX_VALUE ? (long) (((double) longNs) + Duration.m1392toDoubleimpl(duration, DurationUnit.NANOSECONDS)) : m1477saturatingAddpTJri5U(m1477saturatingAddpTJri5U(longNs, r0), r0);
    }

    public static final long saturatingDiff(long valueNs, long originNs) {
        if ((1 | (originNs - 1)) == Long.MAX_VALUE) {
            return Duration.m1401unaryMinusUwyO8pc(DurationKt.toDuration(originNs, DurationUnit.DAYS));
        }
        long j = valueNs - originNs;
        if (((j ^ valueNs) & (~(j ^ originNs))) < 0) {
            long j2 = (long) DurationKt.NANOS_IN_MILLIS;
            long j3 = (valueNs / j2) - (originNs / j2);
            long j4 = (valueNs % j2) - (originNs % j2);
            Duration.Companion companion = Duration.Companion;
            long duration = DurationKt.toDuration(j3, DurationUnit.MILLISECONDS);
            Duration.Companion companion2 = Duration.Companion;
            return Duration.m1385plusLRDsOJo(duration, DurationKt.toDuration(j4, DurationUnit.NANOSECONDS));
        }
        Duration.Companion companion3 = Duration.Companion;
        return DurationKt.toDuration(j, DurationUnit.NANOSECONDS);
    }
}

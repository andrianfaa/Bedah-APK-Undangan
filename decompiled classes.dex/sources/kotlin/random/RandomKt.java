package kotlin.random;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.LongRange;
import mt.Log1F380D;

@Metadata(d1 = {"\u0000:\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0007\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0004H\u0007\u001a\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\fH\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0003H\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004H\u0000\u001a\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0003H\u0000\u001a\u0014\u0010\u000f\u001a\u00020\u0003*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0011H\u0007\u001a\u0014\u0010\u0012\u001a\u00020\u0004*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0013H\u0007\u001a\u0014\u0010\u0014\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0003H\u0000¨\u0006\u0016"}, d2 = {"Random", "Lkotlin/random/Random;", "seed", "", "", "boundsErrorMessage", "", "from", "", "until", "checkRangeBounds", "", "", "fastLog2", "value", "nextInt", "range", "Lkotlin/ranges/IntRange;", "nextLong", "Lkotlin/ranges/LongRange;", "takeUpperBits", "bitCount", "kotlin-stdlib"}, k = 2, mv = {1, 7, 1}, xi = 48)
/* compiled from: 014A */
public final class RandomKt {
    public static final Random Random(int seed) {
        return new XorWowRandom(seed, seed >> 31);
    }

    public static final Random Random(long seed) {
        return new XorWowRandom((int) seed, (int) (seed >> 32));
    }

    public static final String boundsErrorMessage(Object from, Object until) {
        Intrinsics.checkNotNullParameter(from, TypedValues.TransitionType.S_FROM);
        Intrinsics.checkNotNullParameter(until, "until");
        return "Random range is empty: [" + from + ", " + until + ").";
    }

    public static final void checkRangeBounds(double from, double until) {
        if (!(until > from)) {
            String boundsErrorMessage = boundsErrorMessage(Double.valueOf(from), Double.valueOf(until));
            Log1F380D.a((Object) boundsErrorMessage);
            throw new IllegalArgumentException(boundsErrorMessage.toString());
        }
    }

    public static final int fastLog2(int value) {
        return 31 - Integer.numberOfLeadingZeros(value);
    }

    public static final int nextInt(Random $this$nextInt, IntRange range) {
        Intrinsics.checkNotNullParameter($this$nextInt, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        if (!range.isEmpty()) {
            return range.getLast() < Integer.MAX_VALUE ? $this$nextInt.nextInt(range.getFirst(), range.getLast() + 1) : range.getFirst() > Integer.MIN_VALUE ? $this$nextInt.nextInt(range.getFirst() - 1, range.getLast()) + 1 : $this$nextInt.nextInt();
        }
        throw new IllegalArgumentException("Cannot get random in empty range: " + range);
    }

    public static final long nextLong(Random $this$nextLong, LongRange range) {
        Intrinsics.checkNotNullParameter($this$nextLong, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        if (!range.isEmpty()) {
            return range.getLast() < Long.MAX_VALUE ? $this$nextLong.nextLong(range.getFirst(), range.getLast() + 1) : range.getFirst() > Long.MIN_VALUE ? $this$nextLong.nextLong(range.getFirst() - 1, range.getLast()) + 1 : $this$nextLong.nextLong();
        }
        throw new IllegalArgumentException("Cannot get random in empty range: " + range);
    }

    public static final int takeUpperBits(int $this$takeUpperBits, int bitCount) {
        return ($this$takeUpperBits >>> (32 - bitCount)) & ((-bitCount) >> 31);
    }

    public static final void checkRangeBounds(int from, int until) {
        if (!(until > from)) {
            String boundsErrorMessage = boundsErrorMessage(Integer.valueOf(from), Integer.valueOf(until));
            Log1F380D.a((Object) boundsErrorMessage);
            throw new IllegalArgumentException(boundsErrorMessage.toString());
        }
    }

    public static final void checkRangeBounds(long from, long until) {
        if (!(until > from)) {
            String boundsErrorMessage = boundsErrorMessage(Long.valueOf(from), Long.valueOf(until));
            Log1F380D.a((Object) boundsErrorMessage);
            throw new IllegalArgumentException(boundsErrorMessage.toString());
        }
    }
}

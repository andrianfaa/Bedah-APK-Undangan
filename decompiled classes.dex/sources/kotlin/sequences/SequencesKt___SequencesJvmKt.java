package kotlin.sequences;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u0000V\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001f\n\u0002\b\u0004\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a(\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\u001aA\u0010\u0005\u001a\u0002H\u0006\"\u0010\b\u0000\u0010\u0006*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0007\"\u0004\b\u0001\u0010\u0002*\u0006\u0012\u0002\b\u00030\u00012\u0006\u0010\b\u001a\u0002H\u00062\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004¢\u0006\u0002\u0010\t\u001a)\u0010\n\u001a\u0004\u0018\u0001H\u000b\"\u000e\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\f*\b\u0012\u0004\u0012\u0002H\u000b0\u0001H\u0007¢\u0006\u0002\u0010\r\u001a\u0019\u0010\n\u001a\u0004\u0018\u00010\u000e*\b\u0012\u0004\u0012\u00020\u000e0\u0001H\u0007¢\u0006\u0002\u0010\u000f\u001a\u0019\u0010\n\u001a\u0004\u0018\u00010\u0010*\b\u0012\u0004\u0012\u00020\u00100\u0001H\u0007¢\u0006\u0002\u0010\u0011\u001aG\u0010\u0012\u001a\u0004\u0018\u0001H\u000b\"\u0004\b\u0000\u0010\u000b\"\u000e\b\u0001\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u0002H\u00020\u0014H\bø\u0001\u0000¢\u0006\u0002\u0010\u0015\u001a;\u0010\u0016\u001a\u0004\u0018\u0001H\u000b\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000b0\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u000b`\u0019H\u0007¢\u0006\u0002\u0010\u001a\u001a)\u0010\u001b\u001a\u0004\u0018\u0001H\u000b\"\u000e\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\f*\b\u0012\u0004\u0012\u0002H\u000b0\u0001H\u0007¢\u0006\u0002\u0010\r\u001a\u0019\u0010\u001b\u001a\u0004\u0018\u00010\u000e*\b\u0012\u0004\u0012\u00020\u000e0\u0001H\u0007¢\u0006\u0002\u0010\u000f\u001a\u0019\u0010\u001b\u001a\u0004\u0018\u00010\u0010*\b\u0012\u0004\u0012\u00020\u00100\u0001H\u0007¢\u0006\u0002\u0010\u0011\u001aG\u0010\u001c\u001a\u0004\u0018\u0001H\u000b\"\u0004\b\u0000\u0010\u000b\"\u000e\b\u0001\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\f*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u0002H\u00020\u0014H\bø\u0001\u0000¢\u0006\u0002\u0010\u0015\u001a;\u0010\u001d\u001a\u0004\u0018\u0001H\u000b\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000b0\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u000b`\u0019H\u0007¢\u0006\u0002\u0010\u001a\u001a5\u0010\u001e\u001a\u00020\u001f\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u00020\u001f0\u0014H\bø\u0001\u0000¢\u0006\u0002\b \u001a5\u0010\u001e\u001a\u00020!\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u00020!0\u0014H\bø\u0001\u0000¢\u0006\u0002\b\"\u001a&\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u000b0$\"\u000e\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\f*\b\u0012\u0004\u0012\u0002H\u000b0\u0001\u001a8\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u000b0$\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00012\u001a\u0010\u0017\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000b0\u0018j\n\u0012\u0006\b\u0000\u0012\u0002H\u000b`\u0019\u0002\u0007\n\u0005\b20\u0001¨\u0006%"}, d2 = {"filterIsInstance", "Lkotlin/sequences/Sequence;", "R", "klass", "Ljava/lang/Class;", "filterIsInstanceTo", "C", "", "destination", "(Lkotlin/sequences/Sequence;Ljava/util/Collection;Ljava/lang/Class;)Ljava/util/Collection;", "max", "T", "", "(Lkotlin/sequences/Sequence;)Ljava/lang/Comparable;", "", "(Lkotlin/sequences/Sequence;)Ljava/lang/Double;", "", "(Lkotlin/sequences/Sequence;)Ljava/lang/Float;", "maxBy", "selector", "Lkotlin/Function1;", "(Lkotlin/sequences/Sequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Lkotlin/sequences/Sequence;Ljava/util/Comparator;)Ljava/lang/Object;", "min", "minBy", "minWith", "sumOf", "Ljava/math/BigDecimal;", "sumOfBigDecimal", "Ljava/math/BigInteger;", "sumOfBigInteger", "toSortedSet", "Ljava/util/SortedSet;", "kotlin-stdlib"}, k = 5, mv = {1, 7, 1}, xi = 49, xs = "kotlin/sequences/SequencesKt")
/* compiled from: _SequencesJvm.kt */
class SequencesKt___SequencesJvmKt extends SequencesKt__SequencesKt {
    public static final <R> Sequence<R> filterIsInstance(Sequence<?> $this$filterIsInstance, Class<R> klass) {
        Intrinsics.checkNotNullParameter($this$filterIsInstance, "<this>");
        Intrinsics.checkNotNullParameter(klass, "klass");
        Sequence<R> filter = SequencesKt.filter($this$filterIsInstance, new SequencesKt___SequencesJvmKt$filterIsInstance$1(klass));
        Intrinsics.checkNotNull(filter, "null cannot be cast to non-null type kotlin.sequences.Sequence<R of kotlin.sequences.SequencesKt___SequencesJvmKt.filterIsInstance>");
        return filter;
    }

    public static final <C extends Collection<? super R>, R> C filterIsInstanceTo(Sequence<?> $this$filterIsInstanceTo, C destination, Class<R> klass) {
        Intrinsics.checkNotNullParameter($this$filterIsInstanceTo, "<this>");
        Intrinsics.checkNotNullParameter(destination, "destination");
        Intrinsics.checkNotNullParameter(klass, "klass");
        for (Object next : $this$filterIsInstanceTo) {
            if (klass.isInstance(next)) {
                destination.add(next);
            }
        }
        return destination;
    }

    @Deprecated(message = "Use maxByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ <T, R extends Comparable<? super R>> T maxBy(Sequence<? extends T> $this$maxBy, Function1<? super T, ? extends R> selector) {
        Intrinsics.checkNotNullParameter($this$maxBy, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterator<? extends T> it = $this$maxBy.iterator();
        if (!it.hasNext()) {
            return null;
        }
        T next = it.next();
        if (!it.hasNext()) {
            return next;
        }
        Comparable comparable = (Comparable) selector.invoke(next);
        do {
            T next2 = it.next();
            Comparable comparable2 = (Comparable) selector.invoke(next2);
            if (comparable.compareTo(comparable2) < 0) {
                next = next2;
                comparable = comparable2;
            }
        } while (it.hasNext());
        return next;
    }

    @Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ Object maxWith(Sequence $this$maxWith, Comparator comparator) {
        Intrinsics.checkNotNullParameter($this$maxWith, "<this>");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return SequencesKt.maxWithOrNull($this$maxWith, comparator);
    }

    @Deprecated(message = "Use minByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ <T, R extends Comparable<? super R>> T minBy(Sequence<? extends T> $this$minBy, Function1<? super T, ? extends R> selector) {
        Intrinsics.checkNotNullParameter($this$minBy, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        Iterator<? extends T> it = $this$minBy.iterator();
        if (!it.hasNext()) {
            return null;
        }
        T next = it.next();
        if (!it.hasNext()) {
            return next;
        }
        Comparable comparable = (Comparable) selector.invoke(next);
        do {
            T next2 = it.next();
            Comparable comparable2 = (Comparable) selector.invoke(next2);
            if (comparable.compareTo(comparable2) > 0) {
                next = next2;
                comparable = comparable2;
            }
        } while (it.hasNext());
        return next;
    }

    @Deprecated(message = "Use minWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ Object minWith(Sequence $this$minWith, Comparator comparator) {
        Intrinsics.checkNotNullParameter($this$minWith, "<this>");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return SequencesKt.minWithOrNull($this$minWith, comparator);
    }

    private static final <T> BigDecimal sumOfBigDecimal(Sequence<? extends T> $this$sumOf, Function1<? super T, ? extends BigDecimal> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigDecimal valueOf = BigDecimal.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        for (Object invoke : $this$sumOf) {
            BigDecimal add = valueOf.add((BigDecimal) selector.invoke(invoke));
            Intrinsics.checkNotNullExpressionValue(add, "this.add(other)");
            valueOf = add;
        }
        return valueOf;
    }

    private static final <T> BigInteger sumOfBigInteger(Sequence<? extends T> $this$sumOf, Function1<? super T, ? extends BigInteger> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigInteger valueOf = BigInteger.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        for (Object invoke : $this$sumOf) {
            BigInteger add = valueOf.add((BigInteger) selector.invoke(invoke));
            Intrinsics.checkNotNullExpressionValue(add, "this.add(other)");
            valueOf = add;
        }
        return valueOf;
    }

    public static final <T extends Comparable<? super T>> SortedSet<T> toSortedSet(Sequence<? extends T> $this$toSortedSet) {
        Intrinsics.checkNotNullParameter($this$toSortedSet, "<this>");
        return (SortedSet) SequencesKt.toCollection($this$toSortedSet, new TreeSet());
    }

    public static final <T> SortedSet<T> toSortedSet(Sequence<? extends T> $this$toSortedSet, Comparator<? super T> comparator) {
        Intrinsics.checkNotNullParameter($this$toSortedSet, "<this>");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return (SortedSet) SequencesKt.toCollection($this$toSortedSet, new TreeSet(comparator));
    }
}

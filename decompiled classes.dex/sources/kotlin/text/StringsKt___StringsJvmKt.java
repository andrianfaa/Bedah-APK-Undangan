package kotlin.text;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.IntIterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

@Metadata(d1 = {"\u0000B\n\u0000\n\u0002\u0010\f\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\b\u001a\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0001*\u00020\u0002H\u0007¢\u0006\u0002\u0010\u0006\u001a;\u0010\u0007\u001a\u0004\u0018\u00010\u0001\"\u000e\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\t*\u00020\u00022\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\b0\u000bH\bø\u0001\u0000¢\u0006\u0002\u0010\f\u001a/\u0010\r\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u001a\u0010\u000e\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00010\u000fj\n\u0012\u0006\b\u0000\u0012\u00020\u0001`\u0010H\u0007¢\u0006\u0002\u0010\u0011\u001a\u0013\u0010\u0012\u001a\u0004\u0018\u00010\u0001*\u00020\u0002H\u0007¢\u0006\u0002\u0010\u0006\u001a;\u0010\u0013\u001a\u0004\u0018\u00010\u0001\"\u000e\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\t*\u00020\u00022\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u0002H\b0\u000bH\bø\u0001\u0000¢\u0006\u0002\u0010\f\u001a/\u0010\u0014\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u001a\u0010\u000e\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00010\u000fj\n\u0012\u0006\b\u0000\u0012\u00020\u0001`\u0010H\u0007¢\u0006\u0002\u0010\u0011\u001a)\u0010\u0015\u001a\u00020\u0016*\u00020\u00022\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00160\u000bH\bø\u0001\u0000¢\u0006\u0002\b\u0017\u001a)\u0010\u0015\u001a\u00020\u0018*\u00020\u00022\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00180\u000bH\bø\u0001\u0000¢\u0006\u0002\b\u0019\u001a\u0010\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00010\u001b*\u00020\u0002\u0002\u0007\n\u0005\b20\u0001¨\u0006\u001c"}, d2 = {"elementAt", "", "", "index", "", "max", "(Ljava/lang/CharSequence;)Ljava/lang/Character;", "maxBy", "R", "", "selector", "Lkotlin/Function1;", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/Character;", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/lang/CharSequence;Ljava/util/Comparator;)Ljava/lang/Character;", "min", "minBy", "minWith", "sumOf", "Ljava/math/BigDecimal;", "sumOfBigDecimal", "Ljava/math/BigInteger;", "sumOfBigInteger", "toSortedSet", "Ljava/util/SortedSet;", "kotlin-stdlib"}, k = 5, mv = {1, 7, 1}, xi = 49, xs = "kotlin/text/StringsKt")
/* compiled from: _StringsJvm.kt */
class StringsKt___StringsJvmKt extends StringsKt__StringsKt {
    private static final char elementAt(CharSequence $this$elementAt, int index) {
        Intrinsics.checkNotNullParameter($this$elementAt, "<this>");
        return $this$elementAt.charAt(index);
    }

    @Deprecated(message = "Use maxByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ <R extends Comparable<? super R>> Character maxBy(CharSequence $this$maxBy, Function1<? super Character, ? extends R> selector) {
        Intrinsics.checkNotNullParameter($this$maxBy, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        CharSequence charSequence = $this$maxBy;
        if (charSequence.length() == 0) {
            return null;
        }
        char charAt = charSequence.charAt(0);
        int lastIndex = StringsKt.getLastIndex(charSequence);
        if (lastIndex == 0) {
            return Character.valueOf(charAt);
        }
        Comparable comparable = (Comparable) selector.invoke(Character.valueOf(charAt));
        IntIterator it = new IntRange(1, lastIndex).iterator();
        while (it.hasNext()) {
            char charAt2 = charSequence.charAt(it.nextInt());
            Comparable comparable2 = (Comparable) selector.invoke(Character.valueOf(charAt2));
            if (comparable.compareTo(comparable2) < 0) {
                charAt = charAt2;
                comparable = comparable2;
            }
        }
        return Character.valueOf(charAt);
    }

    @Deprecated(message = "Use maxWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.maxWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ Character maxWith(CharSequence $this$maxWith, Comparator comparator) {
        Intrinsics.checkNotNullParameter($this$maxWith, "<this>");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return StringsKt.maxWithOrNull($this$maxWith, comparator);
    }

    @Deprecated(message = "Use minByOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minByOrNull(selector)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ <R extends Comparable<? super R>> Character minBy(CharSequence $this$minBy, Function1<? super Character, ? extends R> selector) {
        Intrinsics.checkNotNullParameter($this$minBy, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        CharSequence charSequence = $this$minBy;
        if (charSequence.length() == 0) {
            return null;
        }
        char charAt = charSequence.charAt(0);
        int lastIndex = StringsKt.getLastIndex(charSequence);
        if (lastIndex == 0) {
            return Character.valueOf(charAt);
        }
        Comparable comparable = (Comparable) selector.invoke(Character.valueOf(charAt));
        IntIterator it = new IntRange(1, lastIndex).iterator();
        while (it.hasNext()) {
            char charAt2 = charSequence.charAt(it.nextInt());
            Comparable comparable2 = (Comparable) selector.invoke(Character.valueOf(charAt2));
            if (comparable.compareTo(comparable2) > 0) {
                charAt = charAt2;
                comparable = comparable2;
            }
        }
        return Character.valueOf(charAt);
    }

    @Deprecated(message = "Use minWithOrNull instead.", replaceWith = @ReplaceWith(expression = "this.minWithOrNull(comparator)", imports = {}))
    @DeprecatedSinceKotlin(errorSince = "1.5", hiddenSince = "1.6", warningSince = "1.4")
    public static final /* synthetic */ Character minWith(CharSequence $this$minWith, Comparator comparator) {
        Intrinsics.checkNotNullParameter($this$minWith, "<this>");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return StringsKt.minWithOrNull($this$minWith, comparator);
    }

    private static final BigDecimal sumOfBigDecimal(CharSequence $this$sumOf, Function1<? super Character, ? extends BigDecimal> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigDecimal valueOf = BigDecimal.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        for (int i = 0; i < $this$sumOf.length(); i++) {
            BigDecimal add = valueOf.add((BigDecimal) selector.invoke(Character.valueOf($this$sumOf.charAt(i))));
            Intrinsics.checkNotNullExpressionValue(add, "this.add(other)");
            valueOf = add;
        }
        return valueOf;
    }

    private static final BigInteger sumOfBigInteger(CharSequence $this$sumOf, Function1<? super Character, ? extends BigInteger> selector) {
        Intrinsics.checkNotNullParameter($this$sumOf, "<this>");
        Intrinsics.checkNotNullParameter(selector, "selector");
        BigInteger valueOf = BigInteger.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(valueOf, "valueOf(this.toLong())");
        for (int i = 0; i < $this$sumOf.length(); i++) {
            BigInteger add = valueOf.add((BigInteger) selector.invoke(Character.valueOf($this$sumOf.charAt(i))));
            Intrinsics.checkNotNullExpressionValue(add, "this.add(other)");
            valueOf = add;
        }
        return valueOf;
    }

    public static final SortedSet<Character> toSortedSet(CharSequence $this$toSortedSet) {
        Intrinsics.checkNotNullParameter($this$toSortedSet, "<this>");
        return (SortedSet) StringsKt.toCollection($this$toSortedSet, new TreeSet());
    }
}

package kotlin.collections;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

@Metadata(d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\"\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a,\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0002¢\u0006\u0002\u0010\u0004\u001a4\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0006H\u0002¢\u0006\u0002\u0010\u0007\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0002\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0002\u001a,\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\b¢\u0006\u0002\u0010\u0004\u001a,\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\u0002¢\u0006\u0002\u0010\u0004\u001a4\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0006H\u0002¢\u0006\u0002\u0010\u0007\u001a-\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\bH\u0002\u001a-\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0002\u001a,\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\b¢\u0006\u0002\u0010\u0004¨\u0006\r"}, d2 = {"minus", "", "T", "element", "(Ljava/util/Set;Ljava/lang/Object;)Ljava/util/Set;", "elements", "", "(Ljava/util/Set;[Ljava/lang/Object;)Ljava/util/Set;", "", "Lkotlin/sequences/Sequence;", "minusElement", "plus", "plusElement", "kotlin-stdlib"}, k = 5, mv = {1, 7, 1}, xi = 49, xs = "kotlin/collections/SetsKt")
/* compiled from: _Sets.kt */
class SetsKt___SetsKt extends SetsKt__SetsKt {
    public static final <T> Set<T> minus(Set<? extends T> $this$minus, Iterable<? extends T> elements) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(elements, "elements");
        Collection<? extends T> convertToSetForSetOperationWith = BrittleContainsOptimizationKt.convertToSetForSetOperationWith(elements, $this$minus);
        if (convertToSetForSetOperationWith.isEmpty()) {
            return CollectionsKt.toSet($this$minus);
        }
        if (convertToSetForSetOperationWith instanceof Set) {
            Collection linkedHashSet = new LinkedHashSet();
            for (Object next : $this$minus) {
                if (!convertToSetForSetOperationWith.contains(next)) {
                    linkedHashSet.add(next);
                }
            }
            return (Set) linkedHashSet;
        }
        LinkedHashSet linkedHashSet2 = new LinkedHashSet($this$minus);
        linkedHashSet2.removeAll(convertToSetForSetOperationWith);
        return linkedHashSet2;
    }

    public static final <T> Set<T> minus(Set<? extends T> $this$minus, T element) {
        boolean z;
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        LinkedHashSet linkedHashSet = new LinkedHashSet(MapsKt.mapCapacity($this$minus.size()));
        boolean z2 = false;
        for (Object next : $this$minus) {
            Object obj = next;
            if (z2 || !Intrinsics.areEqual(obj, (Object) element)) {
                z = true;
            } else {
                z2 = true;
                z = false;
            }
            if (z) {
                linkedHashSet.add(next);
            }
        }
        return linkedHashSet;
    }

    public static final <T> Set<T> minus(Set<? extends T> $this$minus, Sequence<? extends T> elements) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(elements, "elements");
        LinkedHashSet linkedHashSet = new LinkedHashSet($this$minus);
        CollectionsKt.removeAll(linkedHashSet, elements);
        return linkedHashSet;
    }

    public static final <T> Set<T> minus(Set<? extends T> $this$minus, T[] elements) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(elements, "elements");
        LinkedHashSet linkedHashSet = new LinkedHashSet($this$minus);
        CollectionsKt.removeAll(linkedHashSet, elements);
        return linkedHashSet;
    }

    private static final <T> Set<T> minusElement(Set<? extends T> $this$minusElement, T element) {
        Intrinsics.checkNotNullParameter($this$minusElement, "<this>");
        return SetsKt.minus($this$minusElement, element);
    }

    public static final <T> Set<T> plus(Set<? extends T> $this$plus, Iterable<? extends T> elements) {
        int i;
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(elements, "elements");
        Integer collectionSizeOrNull = CollectionsKt.collectionSizeOrNull(elements);
        if (collectionSizeOrNull != null) {
            i = $this$plus.size() + collectionSizeOrNull.intValue();
        } else {
            i = $this$plus.size() * 2;
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet(MapsKt.mapCapacity(i));
        linkedHashSet.addAll($this$plus);
        CollectionsKt.addAll(linkedHashSet, elements);
        return linkedHashSet;
    }

    public static final <T> Set<T> plus(Set<? extends T> $this$plus, T element) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        LinkedHashSet linkedHashSet = new LinkedHashSet(MapsKt.mapCapacity($this$plus.size() + 1));
        linkedHashSet.addAll($this$plus);
        linkedHashSet.add(element);
        return linkedHashSet;
    }

    public static final <T> Set<T> plus(Set<? extends T> $this$plus, Sequence<? extends T> elements) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(elements, "elements");
        LinkedHashSet linkedHashSet = new LinkedHashSet(MapsKt.mapCapacity($this$plus.size() * 2));
        linkedHashSet.addAll($this$plus);
        CollectionsKt.addAll(linkedHashSet, elements);
        return linkedHashSet;
    }

    public static final <T> Set<T> plus(Set<? extends T> $this$plus, T[] elements) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(elements, "elements");
        LinkedHashSet linkedHashSet = new LinkedHashSet(MapsKt.mapCapacity($this$plus.size() + elements.length));
        linkedHashSet.addAll($this$plus);
        CollectionsKt.addAll(linkedHashSet, elements);
        return linkedHashSet;
    }

    private static final <T> Set<T> plusElement(Set<? extends T> $this$plusElement, T element) {
        Intrinsics.checkNotNullParameter($this$plusElement, "<this>");
        return SetsKt.plus($this$plusElement, element);
    }
}

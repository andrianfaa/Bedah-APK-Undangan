package kotlin.collections;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.TypeIntrinsics;

@Metadata(d1 = {"\u0000&\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010&\n\u0000\u001a0\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00030\u0001\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0002*\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u0002H\u00020\u0005H\u0007\u001aZ\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\t\"\u0004\b\u0002\u0010\b*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\u00072\u001e\u0010\n\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\t0\f\u0012\u0004\u0012\u0002H\b0\u000bH\bø\u0001\u0000\u0002\u0007\n\u0005\b20\u0001¨\u0006\r"}, d2 = {"eachCount", "", "K", "", "T", "Lkotlin/collections/Grouping;", "mapValuesInPlace", "", "R", "V", "f", "Lkotlin/Function1;", "", "kotlin-stdlib"}, k = 5, mv = {1, 7, 1}, xi = 49, xs = "kotlin/collections/GroupingKt")
/* compiled from: GroupingJVM.kt */
class GroupingKt__GroupingJVMKt {
    public static final <T, K> Map<K, Integer> eachCount(Grouping<T, ? extends K> $this$eachCount) {
        Intrinsics.checkNotNullParameter($this$eachCount, "<this>");
        Map linkedHashMap = new LinkedHashMap();
        Grouping<T, ? extends K> grouping = $this$eachCount;
        Grouping<T, ? extends K> grouping2 = grouping;
        Iterator<T> sourceIterator = grouping2.sourceIterator();
        while (sourceIterator.hasNext()) {
            T next = sourceIterator.next();
            Object keyOf = grouping2.keyOf(next);
            Object obj = linkedHashMap.get(keyOf);
            Object obj2 = keyOf;
            T t = next;
            Ref.IntRef intRef = (Ref.IntRef) (obj == null && !linkedHashMap.containsKey(keyOf) ? new Ref.IntRef() : obj);
            intRef.element++;
            linkedHashMap.put(keyOf, intRef);
            Grouping<T, ? extends K> grouping3 = $this$eachCount;
            grouping = grouping;
        }
        Grouping<T, ? extends K> grouping4 = grouping;
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            Intrinsics.checkNotNull(entry, "null cannot be cast to non-null type kotlin.collections.MutableMap.MutableEntry<K of kotlin.collections.GroupingKt__GroupingJVMKt.mapValuesInPlace$lambda-4, R of kotlin.collections.GroupingKt__GroupingJVMKt.mapValuesInPlace$lambda-4>");
            TypeIntrinsics.asMutableMapEntry(entry).setValue(Integer.valueOf(((Ref.IntRef) entry.getValue()).element));
        }
        return TypeIntrinsics.asMutableMap(linkedHashMap);
    }

    private static final <K, V, R> Map<K, R> mapValuesInPlace(Map<K, V> $this$mapValuesInPlace, Function1<? super Map.Entry<? extends K, ? extends V>, ? extends R> f) {
        Intrinsics.checkNotNullParameter($this$mapValuesInPlace, "<this>");
        Intrinsics.checkNotNullParameter(f, "f");
        for (Map.Entry entry : $this$mapValuesInPlace.entrySet()) {
            Intrinsics.checkNotNull(entry, "null cannot be cast to non-null type kotlin.collections.MutableMap.MutableEntry<K of kotlin.collections.GroupingKt__GroupingJVMKt.mapValuesInPlace$lambda-4, R of kotlin.collections.GroupingKt__GroupingJVMKt.mapValuesInPlace$lambda-4>");
            TypeIntrinsics.asMutableMapEntry(entry).setValue(f.invoke(entry));
        }
        return TypeIntrinsics.asMutableMap($this$mapValuesInPlace);
    }
}

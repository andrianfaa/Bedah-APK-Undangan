package androidx.core.util;

import android.util.SparseLongArray;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.IntIterator;
import kotlin.collections.LongIterator;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u0000D\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\u001a\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0001H\n\u001a\u0015\u0010\b\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0001H\b\u001a\u0015\u0010\t\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\n\u001a\u00020\u000bH\b\u001aH\u0010\f\u001a\u00020\r*\u00020\u000226\u0010\u000e\u001a2\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b\u0010\u0012\b\b\u0011\u0012\u0004\b\b(\u0007\u0012\u0013\u0012\u00110\u000b¢\u0006\f\b\u0010\u0012\b\b\u0011\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\r0\u000fH\bø\u0001\u0000\u001a\u001d\u0010\u0012\u001a\u00020\u000b*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u000bH\b\u001a&\u0010\u0014\u001a\u00020\u000b*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00012\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0015H\bø\u0001\u0000\u001a\r\u0010\u0016\u001a\u00020\u0006*\u00020\u0002H\b\u001a\r\u0010\u0017\u001a\u00020\u0006*\u00020\u0002H\b\u001a\f\u0010\u0018\u001a\u00020\u0019*\u00020\u0002H\u0007\u001a\u0015\u0010\u001a\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002H\u0002\u001a\u0014\u0010\u001c\u001a\u00020\r*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002H\u0007\u001a\u001c\u0010\u001d\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000bH\u0007\u001a\u001d\u0010\u001e\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000bH\n\u001a\f\u0010\u001f\u001a\u00020 *\u00020\u0002H\u0007\"\u0016\u0010\u0000\u001a\u00020\u0001*\u00020\u00028Ç\u0002¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\u0002\u0007\n\u0005\b20\u0001¨\u0006!"}, d2 = {"size", "", "Landroid/util/SparseLongArray;", "getSize", "(Landroid/util/SparseLongArray;)I", "contains", "", "key", "containsKey", "containsValue", "value", "", "forEach", "", "action", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "getOrDefault", "defaultValue", "getOrElse", "Lkotlin/Function0;", "isEmpty", "isNotEmpty", "keyIterator", "Lkotlin/collections/IntIterator;", "plus", "other", "putAll", "remove", "set", "valueIterator", "Lkotlin/collections/LongIterator;", "core-ktx_release"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: SparseLongArray.kt */
public final class SparseLongArrayKt {
    public static final boolean contains(SparseLongArray $this$contains, int key) {
        Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.indexOfKey(key) >= 0;
    }

    public static final boolean containsKey(SparseLongArray $this$containsKey, int key) {
        Intrinsics.checkNotNullParameter($this$containsKey, "<this>");
        return $this$containsKey.indexOfKey(key) >= 0;
    }

    public static final boolean containsValue(SparseLongArray $this$containsValue, long value) {
        Intrinsics.checkNotNullParameter($this$containsValue, "<this>");
        return $this$containsValue.indexOfValue(value) >= 0;
    }

    public static final void forEach(SparseLongArray $this$forEach, Function2<? super Integer, ? super Long, Unit> action) {
        Intrinsics.checkNotNullParameter($this$forEach, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        int size = $this$forEach.size();
        for (int i = 0; i < size; i++) {
            action.invoke(Integer.valueOf($this$forEach.keyAt(i)), Long.valueOf($this$forEach.valueAt(i)));
        }
    }

    public static final long getOrDefault(SparseLongArray $this$getOrDefault, int key, long defaultValue) {
        Intrinsics.checkNotNullParameter($this$getOrDefault, "<this>");
        return $this$getOrDefault.get(key, defaultValue);
    }

    public static final long getOrElse(SparseLongArray $this$getOrElse, int key, Function0<Long> defaultValue) {
        Intrinsics.checkNotNullParameter($this$getOrElse, "<this>");
        Intrinsics.checkNotNullParameter(defaultValue, "defaultValue");
        int indexOfKey = $this$getOrElse.indexOfKey(key);
        return indexOfKey >= 0 ? $this$getOrElse.valueAt(indexOfKey) : defaultValue.invoke().longValue();
    }

    public static final int getSize(SparseLongArray $this$size) {
        Intrinsics.checkNotNullParameter($this$size, "<this>");
        return $this$size.size();
    }

    public static final boolean isEmpty(SparseLongArray $this$isEmpty) {
        Intrinsics.checkNotNullParameter($this$isEmpty, "<this>");
        return $this$isEmpty.size() == 0;
    }

    public static final boolean isNotEmpty(SparseLongArray $this$isNotEmpty) {
        Intrinsics.checkNotNullParameter($this$isNotEmpty, "<this>");
        return $this$isNotEmpty.size() != 0;
    }

    public static final IntIterator keyIterator(SparseLongArray $this$keyIterator) {
        Intrinsics.checkNotNullParameter($this$keyIterator, "<this>");
        return new SparseLongArrayKt$keyIterator$1($this$keyIterator);
    }

    public static final SparseLongArray plus(SparseLongArray $this$plus, SparseLongArray other) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        SparseLongArray sparseLongArray = new SparseLongArray($this$plus.size() + other.size());
        putAll(sparseLongArray, $this$plus);
        putAll(sparseLongArray, other);
        return sparseLongArray;
    }

    public static final void putAll(SparseLongArray $this$putAll, SparseLongArray other) {
        Intrinsics.checkNotNullParameter($this$putAll, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        SparseLongArray sparseLongArray = other;
        int size = sparseLongArray.size();
        for (int i = 0; i < size; i++) {
            $this$putAll.put(sparseLongArray.keyAt(i), sparseLongArray.valueAt(i));
        }
    }

    public static final boolean remove(SparseLongArray $this$remove, int key, long value) {
        Intrinsics.checkNotNullParameter($this$remove, "<this>");
        int indexOfKey = $this$remove.indexOfKey(key);
        if (indexOfKey < 0 || value != $this$remove.valueAt(indexOfKey)) {
            return false;
        }
        $this$remove.removeAt(indexOfKey);
        return true;
    }

    public static final void set(SparseLongArray $this$set, int key, long value) {
        Intrinsics.checkNotNullParameter($this$set, "<this>");
        $this$set.put(key, value);
    }

    public static final LongIterator valueIterator(SparseLongArray $this$valueIterator) {
        Intrinsics.checkNotNullParameter($this$valueIterator, "<this>");
        return new SparseLongArrayKt$valueIterator$1($this$valueIterator);
    }
}

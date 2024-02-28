package androidx.core.util;

import android.util.SparseBooleanArray;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.BooleanIterator;
import kotlin.collections.IntIterator;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u0000>\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\u001a\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0001H\n\u001a\u0015\u0010\b\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0001H\b\u001a\u0015\u0010\t\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\n\u001a\u00020\u0006H\b\u001aH\u0010\u000b\u001a\u00020\f*\u00020\u000226\u0010\r\u001a2\u0012\u0013\u0012\u00110\u0001¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\u0007\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u000f\u0012\b\b\u0010\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\f0\u000eH\bø\u0001\u0000\u001a\u001d\u0010\u0011\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0012\u001a\u00020\u0006H\b\u001a&\u0010\u0013\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00012\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00060\u0014H\bø\u0001\u0000\u001a\r\u0010\u0015\u001a\u00020\u0006*\u00020\u0002H\b\u001a\r\u0010\u0016\u001a\u00020\u0006*\u00020\u0002H\b\u001a\n\u0010\u0017\u001a\u00020\u0018*\u00020\u0002\u001a\u0015\u0010\u0019\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002H\u0002\u001a\u0012\u0010\u001b\u001a\u00020\f*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002\u001a\u001a\u0010\u001c\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u0006\u001a\u001d\u0010\u001d\u001a\u00020\f*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u0006H\n\u001a\n\u0010\u001e\u001a\u00020\u001f*\u00020\u0002\"\u0016\u0010\u0000\u001a\u00020\u0001*\u00020\u00028Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\u0002\u0007\n\u0005\b20\u0001¨\u0006 "}, d2 = {"size", "", "Landroid/util/SparseBooleanArray;", "getSize", "(Landroid/util/SparseBooleanArray;)I", "contains", "", "key", "containsKey", "containsValue", "value", "forEach", "", "action", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "getOrDefault", "defaultValue", "getOrElse", "Lkotlin/Function0;", "isEmpty", "isNotEmpty", "keyIterator", "Lkotlin/collections/IntIterator;", "plus", "other", "putAll", "remove", "set", "valueIterator", "Lkotlin/collections/BooleanIterator;", "core-ktx_release"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* compiled from: SparseBooleanArray.kt */
public final class SparseBooleanArrayKt {
    public static final boolean contains(SparseBooleanArray $this$contains, int key) {
        Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.indexOfKey(key) >= 0;
    }

    public static final boolean containsKey(SparseBooleanArray $this$containsKey, int key) {
        Intrinsics.checkNotNullParameter($this$containsKey, "<this>");
        return $this$containsKey.indexOfKey(key) >= 0;
    }

    public static final boolean containsValue(SparseBooleanArray $this$containsValue, boolean value) {
        Intrinsics.checkNotNullParameter($this$containsValue, "<this>");
        return $this$containsValue.indexOfValue(value) >= 0;
    }

    public static final void forEach(SparseBooleanArray $this$forEach, Function2<? super Integer, ? super Boolean, Unit> action) {
        Intrinsics.checkNotNullParameter($this$forEach, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        int size = $this$forEach.size();
        for (int i = 0; i < size; i++) {
            action.invoke(Integer.valueOf($this$forEach.keyAt(i)), Boolean.valueOf($this$forEach.valueAt(i)));
        }
    }

    public static final boolean getOrDefault(SparseBooleanArray $this$getOrDefault, int key, boolean defaultValue) {
        Intrinsics.checkNotNullParameter($this$getOrDefault, "<this>");
        return $this$getOrDefault.get(key, defaultValue);
    }

    public static final boolean getOrElse(SparseBooleanArray $this$getOrElse, int key, Function0<Boolean> defaultValue) {
        Intrinsics.checkNotNullParameter($this$getOrElse, "<this>");
        Intrinsics.checkNotNullParameter(defaultValue, "defaultValue");
        int indexOfKey = $this$getOrElse.indexOfKey(key);
        return indexOfKey >= 0 ? $this$getOrElse.valueAt(indexOfKey) : defaultValue.invoke().booleanValue();
    }

    public static final int getSize(SparseBooleanArray $this$size) {
        Intrinsics.checkNotNullParameter($this$size, "<this>");
        return $this$size.size();
    }

    public static final boolean isEmpty(SparseBooleanArray $this$isEmpty) {
        Intrinsics.checkNotNullParameter($this$isEmpty, "<this>");
        return $this$isEmpty.size() == 0;
    }

    public static final boolean isNotEmpty(SparseBooleanArray $this$isNotEmpty) {
        Intrinsics.checkNotNullParameter($this$isNotEmpty, "<this>");
        return $this$isNotEmpty.size() != 0;
    }

    public static final IntIterator keyIterator(SparseBooleanArray $this$keyIterator) {
        Intrinsics.checkNotNullParameter($this$keyIterator, "<this>");
        return new SparseBooleanArrayKt$keyIterator$1($this$keyIterator);
    }

    public static final SparseBooleanArray plus(SparseBooleanArray $this$plus, SparseBooleanArray other) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray($this$plus.size() + other.size());
        putAll(sparseBooleanArray, $this$plus);
        putAll(sparseBooleanArray, other);
        return sparseBooleanArray;
    }

    public static final void putAll(SparseBooleanArray $this$putAll, SparseBooleanArray other) {
        Intrinsics.checkNotNullParameter($this$putAll, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        SparseBooleanArray sparseBooleanArray = other;
        int size = sparseBooleanArray.size();
        for (int i = 0; i < size; i++) {
            $this$putAll.put(sparseBooleanArray.keyAt(i), sparseBooleanArray.valueAt(i));
        }
    }

    public static final boolean remove(SparseBooleanArray $this$remove, int key, boolean value) {
        Intrinsics.checkNotNullParameter($this$remove, "<this>");
        int indexOfKey = $this$remove.indexOfKey(key);
        if (indexOfKey < 0 || value != $this$remove.valueAt(indexOfKey)) {
            return false;
        }
        $this$remove.delete(key);
        return true;
    }

    public static final void set(SparseBooleanArray $this$set, int key, boolean value) {
        Intrinsics.checkNotNullParameter($this$set, "<this>");
        $this$set.put(key, value);
    }

    public static final BooleanIterator valueIterator(SparseBooleanArray $this$valueIterator) {
        Intrinsics.checkNotNullParameter($this$valueIterator, "<this>");
        return new SparseBooleanArrayKt$valueIterator$1($this$valueIterator);
    }
}

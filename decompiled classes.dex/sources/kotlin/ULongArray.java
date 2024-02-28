package kotlin;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmInline;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import mt.Log1F380D;

@JvmInline
@Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0016\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\f\n\u0002\u0010(\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u00012B\u0014\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006B\u0014\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\bø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\tJ\u001b\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0002ø\u0001\u0000¢\u0006\u0004\b\u0011\u0010\u0012J \u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016ø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016J\u001a\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019HÖ\u0003¢\u0006\u0004\b\u001a\u0010\u001bJ\u001e\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0004H\u0002ø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u0010\u0010 \u001a\u00020\u0004HÖ\u0001¢\u0006\u0004\b!\u0010\u000bJ\u000f\u0010\"\u001a\u00020\u000fH\u0016¢\u0006\u0004\b#\u0010$J\u0019\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00020&H\u0002ø\u0001\u0000¢\u0006\u0004\b'\u0010(J#\u0010)\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0002H\u0002ø\u0001\u0000¢\u0006\u0004\b,\u0010-J\u0010\u0010.\u001a\u00020/HÖ\u0001¢\u0006\u0004\b0\u00101R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\f\u0010\r\u0001\u0007\u0001\u00020\bø\u0001\u0000\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u00063"}, d2 = {"Lkotlin/ULongArray;", "", "Lkotlin/ULong;", "size", "", "constructor-impl", "(I)[J", "storage", "", "([J)[J", "getSize-impl", "([J)I", "getStorage$annotations", "()V", "contains", "", "element", "contains-VKZWuLQ", "([JJ)Z", "containsAll", "elements", "containsAll-impl", "([JLjava/util/Collection;)Z", "equals", "other", "", "equals-impl", "([JLjava/lang/Object;)Z", "get", "index", "get-s-VKNKU", "([JI)J", "hashCode", "hashCode-impl", "isEmpty", "isEmpty-impl", "([J)Z", "iterator", "", "iterator-impl", "([J)Ljava/util/Iterator;", "set", "", "value", "set-k8EXiF4", "([JIJ)V", "toString", "", "toString-impl", "([J)Ljava/lang/String;", "Iterator", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
/* compiled from: 011A */
public final class ULongArray implements Collection<ULong>, KMappedMarker {
    private final long[] storage;

    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0016\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\t\u0010\b\u001a\u00020\tH\u0002J\u0016\u0010\n\u001a\u00020\u0002H\u0002ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\u000b\u0010\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000ø\u0001\u0001\u0002\b\n\u0002\b!\n\u0002\b\u0019¨\u0006\r"}, d2 = {"Lkotlin/ULongArray$Iterator;", "", "Lkotlin/ULong;", "array", "", "([J)V", "index", "", "hasNext", "", "next", "next-s-VKNKU", "()J", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: 0119 */
    private static final class Iterator implements java.util.Iterator<ULong>, KMappedMarker {
        private final long[] array;
        private int index;

        public Iterator(long[] array2) {
            Intrinsics.checkNotNullParameter(array2, "array");
            this.array = array2;
        }

        public boolean hasNext() {
            return this.index < this.array.length;
        }

        public /* bridge */ /* synthetic */ Object next() {
            return ULong.m202boximpl(m277nextsVKNKU());
        }

        /* renamed from: next-s-VKNKU  reason: not valid java name */
        public long m277nextsVKNKU() {
            int i = this.index;
            long[] jArr = this.array;
            if (i < jArr.length) {
                this.index = i + 1;
                return ULong.m208constructorimpl(jArr[i]);
            }
            String valueOf = String.valueOf(this.index);
            Log1F380D.a((Object) valueOf);
            throw new NoSuchElementException(valueOf);
        }

        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    private /* synthetic */ ULongArray(long[] storage2) {
        this.storage = storage2;
    }

    /* renamed from: box-impl  reason: not valid java name */
    public static final /* synthetic */ ULongArray m260boximpl(long[] jArr) {
        return new ULongArray(jArr);
    }

    /* renamed from: constructor-impl  reason: not valid java name */
    public static long[] m261constructorimpl(int size) {
        return m262constructorimpl(new long[size]);
    }

    /* renamed from: constructor-impl  reason: not valid java name */
    public static long[] m262constructorimpl(long[] jArr) {
        Intrinsics.checkNotNullParameter(jArr, "storage");
        return jArr;
    }

    /* renamed from: contains-VKZWuLQ  reason: not valid java name */
    public static boolean m263containsVKZWuLQ(long[] arg0, long element) {
        return ArraysKt.contains(arg0, element);
    }

    /* renamed from: containsAll-impl  reason: not valid java name */
    public static boolean m264containsAllimpl(long[] arg0, Collection<ULong> elements) {
        boolean z;
        Intrinsics.checkNotNullParameter(elements, "elements");
        Iterable iterable = elements;
        if (((Collection) iterable).isEmpty()) {
            return true;
        }
        for (Object next : iterable) {
            if (!(next instanceof ULong) || !ArraysKt.contains(arg0, ((ULong) next).m259unboximpl())) {
                z = false;
                continue;
            } else {
                z = true;
                continue;
            }
            if (!z) {
                return false;
            }
        }
        return true;
    }

    /* renamed from: equals-impl  reason: not valid java name */
    public static boolean m265equalsimpl(long[] jArr, Object obj) {
        return (obj instanceof ULongArray) && Intrinsics.areEqual((Object) jArr, (Object) ((ULongArray) obj).m276unboximpl());
    }

    /* renamed from: equals-impl0  reason: not valid java name */
    public static final boolean m266equalsimpl0(long[] jArr, long[] jArr2) {
        return Intrinsics.areEqual((Object) jArr, (Object) jArr2);
    }

    /* renamed from: get-s-VKNKU  reason: not valid java name */
    public static final long m267getsVKNKU(long[] arg0, int index) {
        return ULong.m208constructorimpl(arg0[index]);
    }

    /* renamed from: getSize-impl  reason: not valid java name */
    public static int m268getSizeimpl(long[] arg0) {
        return arg0.length;
    }

    public static /* synthetic */ void getStorage$annotations() {
    }

    /* renamed from: hashCode-impl  reason: not valid java name */
    public static int m269hashCodeimpl(long[] jArr) {
        return Arrays.hashCode(jArr);
    }

    /* renamed from: isEmpty-impl  reason: not valid java name */
    public static boolean m270isEmptyimpl(long[] arg0) {
        return arg0.length == 0;
    }

    /* renamed from: iterator-impl  reason: not valid java name */
    public static java.util.Iterator<ULong> m271iteratorimpl(long[] arg0) {
        return new Iterator(arg0);
    }

    /* renamed from: set-k8EXiF4  reason: not valid java name */
    public static final void m272setk8EXiF4(long[] arg0, int index, long value) {
        arg0[index] = value;
    }

    /* renamed from: toString-impl  reason: not valid java name */
    public static String m273toStringimpl(long[] jArr) {
        StringBuilder append = new StringBuilder().append("ULongArray(storage=");
        String arrays = Arrays.toString(jArr);
        Log1F380D.a((Object) arrays);
        return append.append(arrays).append(')').toString();
    }

    public /* bridge */ /* synthetic */ boolean add(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* renamed from: add-VKZWuLQ  reason: not valid java name */
    public boolean m274addVKZWuLQ(long j) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean addAll(Collection<? extends ULong> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public final /* bridge */ boolean contains(Object element) {
        if (!(element instanceof ULong)) {
            return false;
        }
        return m275containsVKZWuLQ(((ULong) element).m259unboximpl());
    }

    /* renamed from: contains-VKZWuLQ  reason: not valid java name */
    public boolean m275containsVKZWuLQ(long element) {
        return m263containsVKZWuLQ(this.storage, element);
    }

    public boolean containsAll(Collection<? extends Object> elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        return m264containsAllimpl(this.storage, elements);
    }

    public boolean equals(Object obj) {
        return m265equalsimpl(this.storage, obj);
    }

    /* renamed from: getSize */
    public int size() {
        return m268getSizeimpl(this.storage);
    }

    public int hashCode() {
        return m269hashCodeimpl(this.storage);
    }

    public boolean isEmpty() {
        return m270isEmptyimpl(this.storage);
    }

    public java.util.Iterator<ULong> iterator() {
        return m271iteratorimpl(this.storage);
    }

    public boolean remove(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean removeAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean retainAll(Collection<? extends Object> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public Object[] toArray() {
        return CollectionToArray.toArray(this);
    }

    public <T> T[] toArray(T[] tArr) {
        Intrinsics.checkNotNullParameter(tArr, "array");
        return CollectionToArray.toArray(this, tArr);
    }

    /* renamed from: unbox-impl  reason: not valid java name */
    public final /* synthetic */ long[] m276unboximpl() {
        return this.storage;
    }

    public String toString() {
        String r0 = m273toStringimpl(this.storage);
        Log1F380D.a((Object) r0);
        return r0;
    }
}

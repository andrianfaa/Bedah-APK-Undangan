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
@Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\f\n\u0002\u0010(\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u00012B\u0014\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006B\u0014\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\bø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\tJ\u001b\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0002ø\u0001\u0000¢\u0006\u0004\b\u0011\u0010\u0012J \u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016ø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016J\u001a\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019HÖ\u0003¢\u0006\u0004\b\u001a\u0010\u001bJ\u001e\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0004H\u0002ø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u0010\u0010 \u001a\u00020\u0004HÖ\u0001¢\u0006\u0004\b!\u0010\u000bJ\u000f\u0010\"\u001a\u00020\u000fH\u0016¢\u0006\u0004\b#\u0010$J\u0019\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00020&H\u0002ø\u0001\u0000¢\u0006\u0004\b'\u0010(J#\u0010)\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0002H\u0002ø\u0001\u0000¢\u0006\u0004\b,\u0010-J\u0010\u0010.\u001a\u00020/HÖ\u0001¢\u0006\u0004\b0\u00101R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\f\u0010\r\u0001\u0007\u0001\u00020\bø\u0001\u0000\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u00063"}, d2 = {"Lkotlin/UIntArray;", "", "Lkotlin/UInt;", "size", "", "constructor-impl", "(I)[I", "storage", "", "([I)[I", "getSize-impl", "([I)I", "getStorage$annotations", "()V", "contains", "", "element", "contains-WZ4Q5Ns", "([II)Z", "containsAll", "elements", "containsAll-impl", "([ILjava/util/Collection;)Z", "equals", "other", "", "equals-impl", "([ILjava/lang/Object;)Z", "get", "index", "get-pVg5ArA", "([II)I", "hashCode", "hashCode-impl", "isEmpty", "isEmpty-impl", "([I)Z", "iterator", "", "iterator-impl", "([I)Ljava/util/Iterator;", "set", "", "value", "set-VXSXFK8", "([III)V", "toString", "", "toString-impl", "([I)Ljava/lang/String;", "Iterator", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
/* compiled from: 0117 */
public final class UIntArray implements Collection<UInt>, KMappedMarker {
    private final int[] storage;

    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\t\u0010\b\u001a\u00020\tH\u0002J\u0016\u0010\n\u001a\u00020\u0002H\u0002ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\u000b\u0010\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000ø\u0001\u0001\u0002\b\n\u0002\b!\n\u0002\b\u0019¨\u0006\r"}, d2 = {"Lkotlin/UIntArray$Iterator;", "", "Lkotlin/UInt;", "array", "", "([I)V", "index", "", "hasNext", "", "next", "next-pVg5ArA", "()I", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: 0116 */
    private static final class Iterator implements java.util.Iterator<UInt>, KMappedMarker {
        private final int[] array;
        private int index;

        public Iterator(int[] array2) {
            Intrinsics.checkNotNullParameter(array2, "array");
            this.array = array2;
        }

        public boolean hasNext() {
            return this.index < this.array.length;
        }

        public /* bridge */ /* synthetic */ Object next() {
            return UInt.m124boximpl(m199nextpVg5ArA());
        }

        /* renamed from: next-pVg5ArA  reason: not valid java name */
        public int m199nextpVg5ArA() {
            int i = this.index;
            int[] iArr = this.array;
            if (i < iArr.length) {
                this.index = i + 1;
                return UInt.m130constructorimpl(iArr[i]);
            }
            String valueOf = String.valueOf(this.index);
            Log1F380D.a((Object) valueOf);
            throw new NoSuchElementException(valueOf);
        }

        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    private /* synthetic */ UIntArray(int[] storage2) {
        this.storage = storage2;
    }

    /* renamed from: box-impl  reason: not valid java name */
    public static final /* synthetic */ UIntArray m182boximpl(int[] iArr) {
        return new UIntArray(iArr);
    }

    /* renamed from: constructor-impl  reason: not valid java name */
    public static int[] m183constructorimpl(int size) {
        return m184constructorimpl(new int[size]);
    }

    /* renamed from: constructor-impl  reason: not valid java name */
    public static int[] m184constructorimpl(int[] iArr) {
        Intrinsics.checkNotNullParameter(iArr, "storage");
        return iArr;
    }

    /* renamed from: contains-WZ4Q5Ns  reason: not valid java name */
    public static boolean m185containsWZ4Q5Ns(int[] arg0, int element) {
        return ArraysKt.contains(arg0, element);
    }

    /* renamed from: containsAll-impl  reason: not valid java name */
    public static boolean m186containsAllimpl(int[] arg0, Collection<UInt> elements) {
        boolean z;
        Intrinsics.checkNotNullParameter(elements, "elements");
        Iterable iterable = elements;
        if (((Collection) iterable).isEmpty()) {
            return true;
        }
        for (Object next : iterable) {
            if (!(next instanceof UInt) || !ArraysKt.contains(arg0, ((UInt) next).m181unboximpl())) {
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
    public static boolean m187equalsimpl(int[] iArr, Object obj) {
        return (obj instanceof UIntArray) && Intrinsics.areEqual((Object) iArr, (Object) ((UIntArray) obj).m198unboximpl());
    }

    /* renamed from: equals-impl0  reason: not valid java name */
    public static final boolean m188equalsimpl0(int[] iArr, int[] iArr2) {
        return Intrinsics.areEqual((Object) iArr, (Object) iArr2);
    }

    /* renamed from: get-pVg5ArA  reason: not valid java name */
    public static final int m189getpVg5ArA(int[] arg0, int index) {
        return UInt.m130constructorimpl(arg0[index]);
    }

    /* renamed from: getSize-impl  reason: not valid java name */
    public static int m190getSizeimpl(int[] arg0) {
        return arg0.length;
    }

    public static /* synthetic */ void getStorage$annotations() {
    }

    /* renamed from: hashCode-impl  reason: not valid java name */
    public static int m191hashCodeimpl(int[] iArr) {
        return Arrays.hashCode(iArr);
    }

    /* renamed from: isEmpty-impl  reason: not valid java name */
    public static boolean m192isEmptyimpl(int[] arg0) {
        return arg0.length == 0;
    }

    /* renamed from: iterator-impl  reason: not valid java name */
    public static java.util.Iterator<UInt> m193iteratorimpl(int[] arg0) {
        return new Iterator(arg0);
    }

    /* renamed from: set-VXSXFK8  reason: not valid java name */
    public static final void m194setVXSXFK8(int[] arg0, int index, int value) {
        arg0[index] = value;
    }

    /* renamed from: toString-impl  reason: not valid java name */
    public static String m195toStringimpl(int[] iArr) {
        StringBuilder append = new StringBuilder().append("UIntArray(storage=");
        String arrays = Arrays.toString(iArr);
        Log1F380D.a((Object) arrays);
        return append.append(arrays).append(')').toString();
    }

    public /* bridge */ /* synthetic */ boolean add(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* renamed from: add-WZ4Q5Ns  reason: not valid java name */
    public boolean m196addWZ4Q5Ns(int i) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean addAll(Collection<? extends UInt> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public final /* bridge */ boolean contains(Object element) {
        if (!(element instanceof UInt)) {
            return false;
        }
        return m197containsWZ4Q5Ns(((UInt) element).m181unboximpl());
    }

    /* renamed from: contains-WZ4Q5Ns  reason: not valid java name */
    public boolean m197containsWZ4Q5Ns(int element) {
        return m185containsWZ4Q5Ns(this.storage, element);
    }

    public boolean containsAll(Collection<? extends Object> elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        return m186containsAllimpl(this.storage, elements);
    }

    public boolean equals(Object obj) {
        return m187equalsimpl(this.storage, obj);
    }

    /* renamed from: getSize */
    public int size() {
        return m190getSizeimpl(this.storage);
    }

    public int hashCode() {
        return m191hashCodeimpl(this.storage);
    }

    public boolean isEmpty() {
        return m192isEmptyimpl(this.storage);
    }

    public java.util.Iterator<UInt> iterator() {
        return m193iteratorimpl(this.storage);
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
    public final /* synthetic */ int[] m198unboximpl() {
        return this.storage;
    }

    public String toString() {
        String r0 = m195toStringimpl(this.storage);
        Log1F380D.a((Object) r0);
        return r0;
    }
}

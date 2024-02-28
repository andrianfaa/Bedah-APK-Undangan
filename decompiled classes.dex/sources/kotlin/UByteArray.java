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
@Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0000\n\u0002\b\f\n\u0002\u0010(\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\b@\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u00012B\u0014\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006B\u0014\b\u0001\u0012\u0006\u0010\u0007\u001a\u00020\bø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\tJ\u001b\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0002H\u0002ø\u0001\u0000¢\u0006\u0004\b\u0011\u0010\u0012J \u0010\u0013\u001a\u00020\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0016ø\u0001\u0000¢\u0006\u0004\b\u0015\u0010\u0016J\u001a\u0010\u0017\u001a\u00020\u000f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019HÖ\u0003¢\u0006\u0004\b\u001a\u0010\u001bJ\u001e\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u0004H\u0002ø\u0001\u0001ø\u0001\u0000¢\u0006\u0004\b\u001e\u0010\u001fJ\u0010\u0010 \u001a\u00020\u0004HÖ\u0001¢\u0006\u0004\b!\u0010\u000bJ\u000f\u0010\"\u001a\u00020\u000fH\u0016¢\u0006\u0004\b#\u0010$J\u0019\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00020&H\u0002ø\u0001\u0000¢\u0006\u0004\b'\u0010(J#\u0010)\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0002H\u0002ø\u0001\u0000¢\u0006\u0004\b,\u0010-J\u0010\u0010.\u001a\u00020/HÖ\u0001¢\u0006\u0004\b0\u00101R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0000X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\f\u0010\r\u0001\u0007\u0001\u00020\bø\u0001\u0000\u0002\b\n\u0002\b\u0019\n\u0002\b!¨\u00063"}, d2 = {"Lkotlin/UByteArray;", "", "Lkotlin/UByte;", "size", "", "constructor-impl", "(I)[B", "storage", "", "([B)[B", "getSize-impl", "([B)I", "getStorage$annotations", "()V", "contains", "", "element", "contains-7apg3OU", "([BB)Z", "containsAll", "elements", "containsAll-impl", "([BLjava/util/Collection;)Z", "equals", "other", "", "equals-impl", "([BLjava/lang/Object;)Z", "get", "index", "get-w2LRezQ", "([BI)B", "hashCode", "hashCode-impl", "isEmpty", "isEmpty-impl", "([B)Z", "iterator", "", "iterator-impl", "([B)Ljava/util/Iterator;", "set", "", "value", "set-VurrAj0", "([BIB)V", "toString", "", "toString-impl", "([B)Ljava/lang/String;", "Iterator", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
/* compiled from: 0114 */
public final class UByteArray implements Collection<UByte>, KMappedMarker {
    private final byte[] storage;

    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\t\u0010\b\u001a\u00020\tH\u0002J\u0016\u0010\n\u001a\u00020\u0002H\u0002ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\u000b\u0010\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000ø\u0001\u0001\u0002\b\n\u0002\b!\n\u0002\b\u0019¨\u0006\r"}, d2 = {"Lkotlin/UByteArray$Iterator;", "", "Lkotlin/UByte;", "array", "", "([B)V", "index", "", "hasNext", "", "next", "next-w2LRezQ", "()B", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: 0113 */
    private static final class Iterator implements java.util.Iterator<UByte>, KMappedMarker {
        private final byte[] array;
        private int index;

        public Iterator(byte[] array2) {
            Intrinsics.checkNotNullParameter(array2, "array");
            this.array = array2;
        }

        public boolean hasNext() {
            return this.index < this.array.length;
        }

        public /* bridge */ /* synthetic */ Object next() {
            return UByte.m48boximpl(m121nextw2LRezQ());
        }

        /* renamed from: next-w2LRezQ  reason: not valid java name */
        public byte m121nextw2LRezQ() {
            int i = this.index;
            byte[] bArr = this.array;
            if (i < bArr.length) {
                this.index = i + 1;
                return UByte.m54constructorimpl(bArr[i]);
            }
            String valueOf = String.valueOf(this.index);
            Log1F380D.a((Object) valueOf);
            throw new NoSuchElementException(valueOf);
        }

        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }

    private /* synthetic */ UByteArray(byte[] storage2) {
        this.storage = storage2;
    }

    /* renamed from: box-impl  reason: not valid java name */
    public static final /* synthetic */ UByteArray m104boximpl(byte[] bArr) {
        return new UByteArray(bArr);
    }

    /* renamed from: constructor-impl  reason: not valid java name */
    public static byte[] m105constructorimpl(int size) {
        return m106constructorimpl(new byte[size]);
    }

    /* renamed from: constructor-impl  reason: not valid java name */
    public static byte[] m106constructorimpl(byte[] bArr) {
        Intrinsics.checkNotNullParameter(bArr, "storage");
        return bArr;
    }

    /* renamed from: contains-7apg3OU  reason: not valid java name */
    public static boolean m107contains7apg3OU(byte[] arg0, byte element) {
        return ArraysKt.contains(arg0, element);
    }

    /* renamed from: containsAll-impl  reason: not valid java name */
    public static boolean m108containsAllimpl(byte[] arg0, Collection<UByte> elements) {
        boolean z;
        Intrinsics.checkNotNullParameter(elements, "elements");
        Iterable iterable = elements;
        if (((Collection) iterable).isEmpty()) {
            return true;
        }
        for (Object next : iterable) {
            if (!(next instanceof UByte) || !ArraysKt.contains(arg0, ((UByte) next).m103unboximpl())) {
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
    public static boolean m109equalsimpl(byte[] bArr, Object obj) {
        return (obj instanceof UByteArray) && Intrinsics.areEqual((Object) bArr, (Object) ((UByteArray) obj).m120unboximpl());
    }

    /* renamed from: equals-impl0  reason: not valid java name */
    public static final boolean m110equalsimpl0(byte[] bArr, byte[] bArr2) {
        return Intrinsics.areEqual((Object) bArr, (Object) bArr2);
    }

    /* renamed from: get-w2LRezQ  reason: not valid java name */
    public static final byte m111getw2LRezQ(byte[] arg0, int index) {
        return UByte.m54constructorimpl(arg0[index]);
    }

    /* renamed from: getSize-impl  reason: not valid java name */
    public static int m112getSizeimpl(byte[] arg0) {
        return arg0.length;
    }

    public static /* synthetic */ void getStorage$annotations() {
    }

    /* renamed from: hashCode-impl  reason: not valid java name */
    public static int m113hashCodeimpl(byte[] bArr) {
        return Arrays.hashCode(bArr);
    }

    /* renamed from: isEmpty-impl  reason: not valid java name */
    public static boolean m114isEmptyimpl(byte[] arg0) {
        return arg0.length == 0;
    }

    /* renamed from: iterator-impl  reason: not valid java name */
    public static java.util.Iterator<UByte> m115iteratorimpl(byte[] arg0) {
        return new Iterator(arg0);
    }

    /* renamed from: set-VurrAj0  reason: not valid java name */
    public static final void m116setVurrAj0(byte[] arg0, int index, byte value) {
        arg0[index] = value;
    }

    /* renamed from: toString-impl  reason: not valid java name */
    public static String m117toStringimpl(byte[] bArr) {
        StringBuilder append = new StringBuilder().append("UByteArray(storage=");
        String arrays = Arrays.toString(bArr);
        Log1F380D.a((Object) arrays);
        return append.append(arrays).append(')').toString();
    }

    public /* bridge */ /* synthetic */ boolean add(Object obj) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    /* renamed from: add-7apg3OU  reason: not valid java name */
    public boolean m118add7apg3OU(byte b) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public boolean addAll(Collection<? extends UByte> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public final /* bridge */ boolean contains(Object element) {
        if (!(element instanceof UByte)) {
            return false;
        }
        return m119contains7apg3OU(((UByte) element).m103unboximpl());
    }

    /* renamed from: contains-7apg3OU  reason: not valid java name */
    public boolean m119contains7apg3OU(byte element) {
        return m107contains7apg3OU(this.storage, element);
    }

    public boolean containsAll(Collection<? extends Object> elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        return m108containsAllimpl(this.storage, elements);
    }

    public boolean equals(Object obj) {
        return m109equalsimpl(this.storage, obj);
    }

    /* renamed from: getSize */
    public int size() {
        return m112getSizeimpl(this.storage);
    }

    public int hashCode() {
        return m113hashCodeimpl(this.storage);
    }

    public boolean isEmpty() {
        return m114isEmptyimpl(this.storage);
    }

    public java.util.Iterator<UByte> iterator() {
        return m115iteratorimpl(this.storage);
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
    public final /* synthetic */ byte[] m120unboximpl() {
        return this.storage;
    }

    public String toString() {
        String r0 = m117toStringimpl(this.storage);
        Log1F380D.a((Object) r0);
        return r0;
    }
}

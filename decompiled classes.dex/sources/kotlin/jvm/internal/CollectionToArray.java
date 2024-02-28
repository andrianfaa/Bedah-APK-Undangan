package kotlin.jvm.internal;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

@Metadata(d1 = {"\u00002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a#\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\bH\u0007¢\u0006\u0004\b\t\u0010\n\u001a5\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0010\u0010\u000b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0018\u00010\u0001H\u0007¢\u0006\u0004\b\t\u0010\f\u001a~\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0014\u0010\u000e\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u000f2\u001a\u0010\u0010\u001a\u0016\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u00112(\u0010\u0012\u001a$\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u0013H\b¢\u0006\u0002\u0010\u0014\"\u0018\u0010\u0000\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001X\u0004¢\u0006\u0004\n\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"EMPTY", "", "", "[Ljava/lang/Object;", "MAX_SIZE", "", "collectionToArray", "collection", "", "toArray", "(Ljava/util/Collection;)[Ljava/lang/Object;", "a", "(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;", "toArrayImpl", "empty", "Lkotlin/Function0;", "alloc", "Lkotlin/Function1;", "trim", "Lkotlin/Function2;", "(Ljava/util/Collection;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)[Ljava/lang/Object;", "kotlin-stdlib"}, k = 2, mv = {1, 7, 1}, xi = 48)
/* compiled from: CollectionToArray.kt */
public final class CollectionToArray {
    private static final Object[] EMPTY = new Object[0];
    private static final int MAX_SIZE = 2147483645;

    public static final Object[] toArray(Collection<?> collection) {
        Intrinsics.checkNotNullParameter(collection, "collection");
        int size = collection.size();
        if (size == 0) {
            return EMPTY;
        }
        Iterator<?> it = collection.iterator();
        if (!it.hasNext()) {
            return EMPTY;
        }
        Object[] objArr = new Object[size];
        int i = 0;
        while (true) {
            int i2 = i + 1;
            objArr[i] = it.next();
            if (i2 >= objArr.length) {
                if (!it.hasNext()) {
                    return objArr;
                }
                int i3 = ((i2 * 3) + 1) >>> 1;
                if (i3 <= i2) {
                    if (i2 < MAX_SIZE) {
                        i3 = MAX_SIZE;
                    } else {
                        throw new OutOfMemoryError();
                    }
                }
                Object[] copyOf = Arrays.copyOf(objArr, i3);
                Intrinsics.checkNotNullExpressionValue(copyOf, "copyOf(result, newSize)");
                objArr = copyOf;
                i = i2;
            } else if (!it.hasNext()) {
                Object[] copyOf2 = Arrays.copyOf(objArr, i2);
                Intrinsics.checkNotNullExpressionValue(copyOf2, "copyOf(result, size)");
                return copyOf2;
            } else {
                i = i2;
            }
        }
    }

    public static final Object[] toArray(Collection<?> collection, Object[] a) {
        Object[] objArr;
        Object[] objArr2;
        Intrinsics.checkNotNullParameter(collection, "collection");
        if (a != null) {
            int size = collection.size();
            if (size != 0) {
                Iterator<?> it = collection.iterator();
                if (it.hasNext()) {
                    int i = size;
                    if (i <= a.length) {
                        objArr = a;
                    } else {
                        Object newInstance = Array.newInstance(a.getClass().getComponentType(), i);
                        Intrinsics.checkNotNull(newInstance, "null cannot be cast to non-null type kotlin.Array<kotlin.Any?>");
                        objArr = (Object[]) newInstance;
                    }
                    Object[] objArr3 = objArr;
                    int i2 = 0;
                    while (true) {
                        int i3 = i2 + 1;
                        objArr3[i2] = it.next();
                        if (i3 >= objArr3.length) {
                            if (!it.hasNext()) {
                                return objArr3;
                            }
                            int i4 = ((i3 * 3) + 1) >>> 1;
                            if (i4 <= i3) {
                                if (i3 < MAX_SIZE) {
                                    i4 = MAX_SIZE;
                                } else {
                                    throw new OutOfMemoryError();
                                }
                            }
                            Object[] copyOf = Arrays.copyOf(objArr3, i4);
                            Intrinsics.checkNotNullExpressionValue(copyOf, "copyOf(result, newSize)");
                            objArr3 = copyOf;
                            i2 = i3;
                        } else if (!it.hasNext()) {
                            Object[] objArr4 = objArr3;
                            int i5 = i3;
                            if (objArr4 == a) {
                                a[i5] = null;
                                objArr2 = a;
                            } else {
                                objArr2 = Arrays.copyOf(objArr4, i5);
                                Intrinsics.checkNotNullExpressionValue(objArr2, "copyOf(result, size)");
                            }
                            return objArr2;
                        } else {
                            i2 = i3;
                        }
                    }
                } else if (a.length > 0) {
                    a[0] = null;
                }
            } else if (a.length > 0) {
                a[0] = null;
            }
            return a;
        }
        throw new NullPointerException();
    }

    private static final Object[] toArrayImpl(Collection<?> collection, Function0<Object[]> empty, Function1<? super Integer, Object[]> alloc, Function2<? super Object[], ? super Integer, Object[]> trim) {
        int size = collection.size();
        if (size == 0) {
            return empty.invoke();
        }
        Iterator<?> it = collection.iterator();
        if (!it.hasNext()) {
            return empty.invoke();
        }
        Object[] invoke = alloc.invoke(Integer.valueOf(size));
        int i = 0;
        while (true) {
            int i2 = i + 1;
            invoke[i] = it.next();
            if (i2 >= invoke.length) {
                if (!it.hasNext()) {
                    return invoke;
                }
                int i3 = ((i2 * 3) + 1) >>> 1;
                if (i3 <= i2) {
                    if (i2 < MAX_SIZE) {
                        i3 = MAX_SIZE;
                    } else {
                        throw new OutOfMemoryError();
                    }
                }
                Object[] copyOf = Arrays.copyOf(invoke, i3);
                Intrinsics.checkNotNullExpressionValue(copyOf, "copyOf(result, newSize)");
                invoke = copyOf;
                i = i2;
            } else if (!it.hasNext()) {
                return trim.invoke(invoke, Integer.valueOf(i2));
            } else {
                i = i2;
            }
        }
    }
}

package okio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\b\u0018\u0000 \u00152\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004:\u0001\u0015B\u001f\b\u0002\u0012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\u0011\u0010\u0013\u001a\u00020\u00022\u0006\u0010\u0014\u001a\u00020\u000eH\u0002R\u001e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u0006X\u0004¢\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\r\u001a\u00020\u000e8VX\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012¨\u0006\u0016"}, d2 = {"Lokio/Options;", "Lkotlin/collections/AbstractList;", "Lokio/ByteString;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "byteStrings", "", "trie", "", "([Lokio/ByteString;[I)V", "getByteStrings$okio", "()[Lokio/ByteString;", "[Lokio/ByteString;", "size", "", "getSize", "()I", "getTrie$okio", "()[I", "get", "index", "Companion", "okio"}, k = 1, mv = {1, 4, 0})
/* compiled from: Options.kt */
public final class Options extends AbstractList<ByteString> implements RandomAccess {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private final ByteString[] byteStrings;
    private final int[] trie;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002JT\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\b\b\u0002\u0010\u0011\u001a\u00020\r2\b\b\u0002\u0010\u0012\u001a\u00020\r2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\r0\u000fH\u0002J!\u0010\u0014\u001a\u00020\u00152\u0012\u0010\u000e\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00100\u0016\"\u00020\u0010H\u0007¢\u0006\u0002\u0010\u0017R\u0018\u0010\u0003\u001a\u00020\u0004*\u00020\u00058BX\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0018"}, d2 = {"Lokio/Options$Companion;", "", "()V", "intCount", "", "Lokio/Buffer;", "getIntCount", "(Lokio/Buffer;)J", "buildTrieRecursive", "", "nodeOffset", "node", "byteStringOffset", "", "byteStrings", "", "Lokio/ByteString;", "fromIndex", "toIndex", "indexes", "of", "Lokio/Options;", "", "([Lokio/ByteString;)Lokio/Options;", "okio"}, k = 1, mv = {1, 4, 0})
    /* compiled from: Options.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private final void buildTrieRecursive(long nodeOffset, Buffer node, int byteStringOffset, List<? extends ByteString> byteStrings, int fromIndex, int toIndex, List<Integer> indexes) {
            int i;
            ByteString byteString;
            int i2;
            ByteString byteString2;
            ByteString byteString3;
            Buffer buffer;
            int i3;
            int i4;
            int i5;
            int i6;
            Buffer buffer2 = node;
            int i7 = byteStringOffset;
            List<? extends ByteString> list = byteStrings;
            int i8 = toIndex;
            List<Integer> list2 = indexes;
            boolean z = false;
            int i9 = fromIndex;
            if (i9 < i8) {
                int i10 = i9;
                while (i10 < i8) {
                    if (((ByteString) list.get(i10)).size() >= i7) {
                        i10++;
                    } else {
                        throw new IllegalArgumentException("Failed requirement.".toString());
                    }
                }
                int i11 = fromIndex;
                ByteString byteString4 = (ByteString) list.get(i11);
                ByteString byteString5 = (ByteString) list.get(i8 - 1);
                if (i7 == byteString4.size()) {
                    int intValue = list2.get(i11).intValue();
                    int i12 = i11 + 1;
                    i = i12;
                    byteString = (ByteString) list.get(i12);
                    i2 = intValue;
                } else {
                    i = i11;
                    byteString = byteString4;
                    i2 = -1;
                }
                if (byteString.getByte(i7) != byteString5.getByte(i7)) {
                    int i13 = i + 1;
                    int i14 = 1;
                    while (i13 < i8) {
                        int i15 = i13;
                        if (((ByteString) list.get(i15 - 1)).getByte(i7) != ((ByteString) list.get(i15)).getByte(i7)) {
                            i14++;
                        }
                        i13 = i15 + 1;
                    }
                    long intCount = nodeOffset + getIntCount(buffer2) + ((long) 2) + ((long) (i14 * 2));
                    buffer2.writeInt(i14);
                    buffer2.writeInt(i2);
                    for (int i16 = i; i16 < i8; i16++) {
                        byte b = ((ByteString) list.get(i16)).getByte(i7);
                        if (i16 == i || b != ((ByteString) list.get(i16 - 1)).getByte(i7)) {
                            buffer2.writeInt((int) 255 & b);
                        }
                    }
                    Buffer buffer3 = new Buffer();
                    int i17 = i;
                    while (i17 < i8) {
                        byte b2 = ((ByteString) list.get(i17)).getByte(i7);
                        int i18 = toIndex;
                        int i19 = i17 + 1;
                        while (true) {
                            if (i19 >= i8) {
                                i19 = i18;
                                break;
                            }
                            int i20 = i18;
                            if (b2 != ((ByteString) list.get(i19)).getByte(i7)) {
                                int i21 = i19;
                                break;
                            } else {
                                i19++;
                                i18 = i20;
                            }
                        }
                        if (i17 + 1 == i19) {
                            byte b3 = b2;
                            if (i7 + 1 == ((ByteString) list.get(i17)).size()) {
                                buffer2.writeInt(list2.get(i17).intValue());
                                i3 = i19;
                                int i22 = i17;
                                buffer = buffer3;
                                i4 = i14;
                                i5 = i2;
                                byteString3 = byteString;
                                i6 = i;
                                i17 = i3;
                                i = i6;
                                i2 = i5;
                                i14 = i4;
                                buffer3 = buffer;
                                byteString = byteString3;
                                int i23 = fromIndex;
                                list2 = indexes;
                            }
                        } else {
                            byte b4 = b2;
                        }
                        buffer2.writeInt(((int) (intCount + getIntCount(buffer3))) * -1);
                        i3 = i19;
                        buffer = buffer3;
                        i4 = i14;
                        i5 = i2;
                        byteString3 = byteString;
                        i6 = i;
                        buildTrieRecursive(intCount, buffer3, i7 + 1, byteStrings, i17, i3, indexes);
                        i17 = i3;
                        i = i6;
                        i2 = i5;
                        i14 = i4;
                        buffer3 = buffer;
                        byteString = byteString3;
                        int i232 = fromIndex;
                        list2 = indexes;
                    }
                    int i24 = i17;
                    int i25 = i14;
                    int i26 = i2;
                    buffer2.writeAll(buffer3);
                    int i27 = i;
                    ByteString byteString6 = byteString;
                    List<Integer> list3 = indexes;
                    return;
                }
                int i28 = i2;
                ByteString byteString7 = byteString;
                int i29 = i;
                int min = Math.min(byteString7.size(), byteString5.size());
                int i30 = 0;
                int i31 = i7;
                while (true) {
                    if (i31 >= min) {
                        byteString2 = byteString7;
                        break;
                    }
                    byteString2 = byteString7;
                    if (byteString2.getByte(i31) != byteString5.getByte(i31)) {
                        break;
                    }
                    i30++;
                    i31++;
                    byteString7 = byteString2;
                }
                long intCount2 = nodeOffset + getIntCount(buffer2) + ((long) 2) + ((long) i30) + 1;
                buffer2.writeInt(-i30);
                buffer2.writeInt(i28);
                int i32 = i7 + i30;
                for (int i33 = i7; i33 < i32; i33++) {
                    buffer2.writeInt((int) byteString2.getByte(i33) & UByte.MAX_VALUE);
                }
                if (i29 + 1 == i8) {
                    if (i7 + i30 == ((ByteString) list.get(i29)).size()) {
                        z = true;
                    }
                    if (z) {
                        int i34 = i29;
                        buffer2.writeInt(indexes.get(i34).intValue());
                        int i35 = i34;
                        ByteString byteString8 = byteString2;
                        return;
                    }
                    throw new IllegalStateException("Check failed.".toString());
                }
                int i36 = i29;
                List<Integer> list4 = indexes;
                Buffer buffer4 = new Buffer();
                buffer2.writeInt(((int) (intCount2 + getIntCount(buffer4))) * -1);
                int i37 = i36;
                ByteString byteString9 = byteString2;
                int i38 = i30;
                buildTrieRecursive(intCount2, buffer4, i7 + i30, byteStrings, i36, toIndex, indexes);
                buffer2.writeAll(buffer4);
                return;
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }

        static /* synthetic */ void buildTrieRecursive$default(Companion companion, long j, Buffer buffer, int i, List list, int i2, int i3, List list2, int i4, Object obj) {
            companion.buildTrieRecursive((i4 & 1) != 0 ? 0 : j, buffer, (i4 & 4) != 0 ? 0 : i, list, (i4 & 16) != 0 ? 0 : i2, (i4 & 32) != 0 ? list.size() : i3, list2);
        }

        private final long getIntCount(Buffer $this$intCount) {
            return $this$intCount.size() / ((long) 4);
        }

        @JvmStatic
        public final Options of(ByteString... byteStrings) {
            ByteString[] byteStringArr = byteStrings;
            Intrinsics.checkNotNullParameter(byteStringArr, "byteStrings");
            if (byteStringArr.length == 0) {
                return new Options(new ByteString[0], new int[]{0, -1}, (DefaultConstructorMarker) null);
            }
            List mutableList = ArraysKt.toMutableList((T[]) byteStrings);
            CollectionsKt.sort(mutableList);
            ByteString[] byteStringArr2 = byteStrings;
            Collection arrayList = new ArrayList(byteStringArr2.length);
            for (ByteString byteString : byteStringArr2) {
                arrayList.add(-1);
            }
            Object[] array = ((List) arrayList).toArray(new Integer[0]);
            if (array != null) {
                Integer[] numArr = (Integer[]) array;
                List mutableListOf = CollectionsKt.mutableListOf((Integer[]) Arrays.copyOf(numArr, numArr.length));
                ByteString[] byteStringArr3 = byteStrings;
                int i = 0;
                int length = byteStringArr3.length;
                int i2 = 0;
                while (i2 < length) {
                    mutableListOf.set(CollectionsKt.binarySearch$default(mutableList, (Comparable) byteStringArr3[i2], 0, 0, 6, (Object) null), Integer.valueOf(i));
                    i2++;
                    i++;
                }
                if (((ByteString) mutableList.get(0)).size() > 0) {
                    int i3 = 0;
                    while (i3 < mutableList.size()) {
                        ByteString byteString2 = (ByteString) mutableList.get(i3);
                        int i4 = i3 + 1;
                        while (i4 < mutableList.size()) {
                            ByteString byteString3 = (ByteString) mutableList.get(i4);
                            if (!byteString3.startsWith(byteString2)) {
                                continue;
                                break;
                            }
                            if (!(byteString3.size() != byteString2.size())) {
                                throw new IllegalArgumentException(("duplicate option: " + byteString3).toString());
                            } else if (((Number) mutableListOf.get(i4)).intValue() > ((Number) mutableListOf.get(i3)).intValue()) {
                                mutableList.remove(i4);
                                mutableListOf.remove(i4);
                            } else {
                                i4++;
                            }
                        }
                        i3++;
                    }
                    Buffer buffer = new Buffer();
                    int i5 = i3;
                    List list = mutableListOf;
                    buildTrieRecursive$default(this, 0, buffer, 0, mutableList, 0, 0, mutableListOf, 53, (Object) null);
                    int[] iArr = new int[((int) getIntCount(buffer))];
                    int i6 = 0;
                    while (!buffer.exhausted()) {
                        iArr[i6] = buffer.readInt();
                        i6++;
                    }
                    Object[] copyOf = Arrays.copyOf(byteStringArr, byteStringArr.length);
                    Intrinsics.checkNotNullExpressionValue(copyOf, "java.util.Arrays.copyOf(this, size)");
                    return new Options((ByteString[]) copyOf, iArr, (DefaultConstructorMarker) null);
                }
                throw new IllegalArgumentException("the empty byte string is not a supported option".toString());
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }
    }

    private Options(ByteString[] byteStrings2, int[] trie2) {
        this.byteStrings = byteStrings2;
        this.trie = trie2;
    }

    public /* synthetic */ Options(ByteString[] byteStrings2, int[] trie2, DefaultConstructorMarker $constructor_marker) {
        this(byteStrings2, trie2);
    }

    @JvmStatic
    public static final Options of(ByteString... byteStringArr) {
        return Companion.of(byteStringArr);
    }

    public final /* bridge */ boolean contains(Object obj) {
        if (obj instanceof ByteString) {
            return contains((ByteString) obj);
        }
        return false;
    }

    public /* bridge */ boolean contains(ByteString byteString) {
        return super.contains(byteString);
    }

    public ByteString get(int index) {
        return this.byteStrings[index];
    }

    public final ByteString[] getByteStrings$okio() {
        return this.byteStrings;
    }

    public int getSize() {
        return this.byteStrings.length;
    }

    public final int[] getTrie$okio() {
        return this.trie;
    }

    public final /* bridge */ int indexOf(Object obj) {
        if (obj instanceof ByteString) {
            return indexOf((ByteString) obj);
        }
        return -1;
    }

    public /* bridge */ int indexOf(ByteString byteString) {
        return super.indexOf(byteString);
    }

    public final /* bridge */ int lastIndexOf(Object obj) {
        if (obj instanceof ByteString) {
            return lastIndexOf((ByteString) obj);
        }
        return -1;
    }

    public /* bridge */ int lastIndexOf(ByteString byteString) {
        return super.lastIndexOf(byteString);
    }
}

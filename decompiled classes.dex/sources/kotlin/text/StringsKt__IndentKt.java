package kotlin.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;
import mt.Log1F380D;
import okhttp3.HttpUrl;

@Metadata(d1 = {"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u000b\u001a!\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0002¢\u0006\u0002\b\u0004\u001a\u0011\u0010\u0005\u001a\u00020\u0006*\u00020\u0002H\u0002¢\u0006\u0002\b\u0007\u001a\u0014\u0010\b\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0002\u001aJ\u0010\t\u001a\u00020\u0002*\b\u0012\u0004\u0012\u00020\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0014\u0010\r\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001H\b¢\u0006\u0002\b\u000e\u001a\u0014\u0010\u000f\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u0002\u001a\u001e\u0010\u0011\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002\u001a\n\u0010\u0013\u001a\u00020\u0002*\u00020\u0002\u001a\u0014\u0010\u0014\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002¨\u0006\u0015"}, d2 = {"getIndentFunction", "Lkotlin/Function1;", "", "indent", "getIndentFunction$StringsKt__IndentKt", "indentWidth", "", "indentWidth$StringsKt__IndentKt", "prependIndent", "reindent", "", "resultSizeEstimate", "indentAddFunction", "indentCutFunction", "reindent$StringsKt__IndentKt", "replaceIndent", "newIndent", "replaceIndentByMargin", "marginPrefix", "trimIndent", "trimMargin", "kotlin-stdlib"}, k = 5, mv = {1, 7, 1}, xi = 49, xs = "kotlin/text/StringsKt")
/* compiled from: 015C */
class StringsKt__IndentKt extends StringsKt__AppendableKt {
    private static final Function1<String, String> getIndentFunction$StringsKt__IndentKt(String indent) {
        return indent.length() == 0 ? StringsKt__IndentKt$getIndentFunction$1.INSTANCE : new StringsKt__IndentKt$getIndentFunction$2(indent);
    }

    private static final int indentWidth$StringsKt__IndentKt(String $this$indentWidth) {
        CharSequence charSequence = $this$indentWidth;
        int i = 0;
        int length = charSequence.length();
        while (true) {
            if (i >= length) {
                i = -1;
                break;
            } else if (!CharsKt.isWhitespace(charSequence.charAt(i))) {
                break;
            } else {
                i++;
            }
        }
        int i2 = i;
        return i2 == -1 ? $this$indentWidth.length() : i2;
    }

    public static final String prependIndent(String $this$prependIndent, String indent) {
        Intrinsics.checkNotNullParameter($this$prependIndent, "<this>");
        Intrinsics.checkNotNullParameter(indent, "indent");
        String joinToString$default = SequencesKt.joinToString$default(SequencesKt.map(StringsKt.lineSequence($this$prependIndent), new StringsKt__IndentKt$prependIndent$1(indent)), "\n", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null);
        Log1F380D.a((Object) joinToString$default);
        return joinToString$default;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x006d, code lost:
        if (r0 == null) goto L_0x0074;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static final java.lang.String reindent$StringsKt__IndentKt(java.util.List<java.lang.String> r21, int r22, kotlin.jvm.functions.Function1<? super java.lang.String, java.lang.String> r23, kotlin.jvm.functions.Function1<? super java.lang.String, java.lang.String> r24) {
        /*
            r0 = 0
            int r1 = kotlin.collections.CollectionsKt.getLastIndex(r21)
            r2 = r21
            java.lang.Iterable r2 = (java.lang.Iterable) r2
            r3 = 0
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
            java.util.Collection r4 = (java.util.Collection) r4
            r5 = r2
            r6 = 0
            r7 = r5
            r8 = 0
            r9 = 0
            java.util.Iterator r10 = r7.iterator()
        L_0x001a:
            boolean r11 = r10.hasNext()
            if (r11 == 0) goto L_0x0084
            java.lang.Object r11 = r10.next()
            int r12 = r9 + 1
            if (r9 >= 0) goto L_0x002b
            kotlin.collections.CollectionsKt.throwIndexOverflow()
        L_0x002b:
            r13 = r11
            r14 = 0
            r15 = r13
            java.lang.String r15 = (java.lang.String) r15
            r16 = r9
            r17 = r16
            r16 = 0
            r18 = r0
            r0 = r17
            if (r0 == 0) goto L_0x003e
            if (r0 != r1) goto L_0x0055
        L_0x003e:
            r17 = r15
            java.lang.CharSequence r17 = (java.lang.CharSequence) r17
            boolean r17 = kotlin.text.StringsKt.isBlank(r17)
            if (r17 == 0) goto L_0x0055
            r17 = 0
            r19 = r1
            r1 = r23
            r20 = r17
            r17 = r0
            r0 = r20
            goto L_0x0075
        L_0x0055:
            r17 = r0
            r0 = r24
            java.lang.Object r19 = r0.invoke(r15)
            r0 = r19
            java.lang.String r0 = (java.lang.String) r0
            if (r0 == 0) goto L_0x0070
            r19 = r1
            r1 = r23
            java.lang.Object r0 = r1.invoke(r0)
            java.lang.String r0 = (java.lang.String) r0
            if (r0 != 0) goto L_0x0075
            goto L_0x0074
        L_0x0070:
            r19 = r1
            r1 = r23
        L_0x0074:
            r0 = r15
        L_0x0075:
            if (r0 == 0) goto L_0x007d
            r15 = 0
            r4.add(r0)
            goto L_0x007e
        L_0x007d:
        L_0x007e:
            r9 = r12
            r0 = r18
            r1 = r19
            goto L_0x001a
        L_0x0084:
            r18 = r0
            r0 = r4
            java.util.List r0 = (java.util.List) r0
            r4 = r0
            java.lang.Iterable r4 = (java.lang.Iterable) r4
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r2 = r22
            r0.<init>(r2)
            r5 = r0
            java.lang.Appendable r5 = (java.lang.Appendable) r5
            java.lang.String r0 = "\n"
            r6 = r0
            java.lang.CharSequence r6 = (java.lang.CharSequence) r6
            r7 = 0
            r8 = 0
            r9 = 0
            r10 = 0
            r11 = 0
            r12 = 124(0x7c, float:1.74E-43)
            r13 = 0
            java.lang.Appendable r0 = kotlin.collections.CollectionsKt.joinTo$default(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13)
            java.lang.StringBuilder r0 = (java.lang.StringBuilder) r0
            java.lang.String r0 = r0.toString()
            java.lang.String r3 = "mapIndexedNotNull { inde…\"\\n\")\n        .toString()"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.text.StringsKt__IndentKt.reindent$StringsKt__IndentKt(java.util.List, int, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function1):java.lang.String");
    }

    public static final String replaceIndentByMargin(String $this$replaceIndentByMargin, String newIndent, String marginPrefix) {
        Collection collection;
        String str;
        String str2 = $this$replaceIndentByMargin;
        String str3 = marginPrefix;
        Intrinsics.checkNotNullParameter(str2, "<this>");
        Intrinsics.checkNotNullParameter(newIndent, "newIndent");
        Intrinsics.checkNotNullParameter(str3, "marginPrefix");
        if (!StringsKt.isBlank(str3)) {
            List<String> lines = StringsKt.lines(str2);
            int length = $this$replaceIndentByMargin.length() + (newIndent.length() * lines.size());
            Function1<String, String> indentFunction$StringsKt__IndentKt = getIndentFunction$StringsKt__IndentKt(newIndent);
            List<String> list = lines;
            int lastIndex = CollectionsKt.getLastIndex(list);
            Collection arrayList = new ArrayList();
            int i = 0;
            for (Object next : list) {
                int i2 = i + 1;
                if (i < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                String str4 = (String) next;
                int i3 = i;
                String str5 = null;
                if ((i3 == 0 || i3 == lastIndex) && StringsKt.isBlank(str4)) {
                    int i4 = i3;
                    collection = arrayList;
                } else {
                    String str6 = str4;
                    CharSequence charSequence = str6;
                    boolean z = false;
                    int i5 = 0;
                    int length2 = charSequence.length();
                    while (true) {
                        boolean z2 = z;
                        if (i5 >= length2) {
                            i5 = -1;
                            break;
                        } else if (!CharsKt.isWhitespace(charSequence.charAt(i5))) {
                            break;
                        } else {
                            i5++;
                            z = z2;
                        }
                    }
                    int i6 = i5;
                    if (i6 == -1) {
                        String str7 = str6;
                        int i7 = i3;
                        collection = arrayList;
                        str = null;
                    } else {
                        String str8 = str6;
                        int i8 = i3;
                        collection = arrayList;
                        if (StringsKt.startsWith$default(str6, marginPrefix, i6, false, 4, (Object) null)) {
                            String str9 = str8;
                            Intrinsics.checkNotNull(str9, "null cannot be cast to non-null type java.lang.String");
                            str = str9.substring(marginPrefix.length() + i6);
                            Intrinsics.checkNotNullExpressionValue(str, "this as java.lang.String).substring(startIndex)");
                        } else {
                            String str10 = str8;
                            str = null;
                        }
                    }
                    if (str == null || (str5 = indentFunction$StringsKt__IndentKt.invoke(str)) == null) {
                        str5 = str4;
                    }
                }
                if (str5 != null) {
                    collection.add(str5);
                }
                String str11 = $this$replaceIndentByMargin;
                arrayList = collection;
                i = i2;
                String str12 = marginPrefix;
            }
            String sb = ((StringBuilder) CollectionsKt.joinTo$default((List) arrayList, new StringBuilder(length), "\n", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 124, (Object) null)).toString();
            Intrinsics.checkNotNullExpressionValue(sb, "mapIndexedNotNull { inde…\"\\n\")\n        .toString()");
            return sb;
        }
        throw new IllegalArgumentException("marginPrefix must be non-blank string.".toString());
    }

    public static /* synthetic */ String prependIndent$default(String str, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            str2 = "    ";
        }
        String prependIndent = StringsKt.prependIndent(str, str2);
        Log1F380D.a((Object) prependIndent);
        return prependIndent;
    }

    public static final String replaceIndent(String $this$replaceIndent, String newIndent) {
        String str;
        String str2 = $this$replaceIndent;
        Intrinsics.checkNotNullParameter(str2, "<this>");
        Intrinsics.checkNotNullParameter(newIndent, "newIndent");
        List<String> lines = StringsKt.lines(str2);
        Collection arrayList = new ArrayList();
        for (Object next : lines) {
            if (!StringsKt.isBlank((String) next)) {
                arrayList.add(next);
            }
        }
        Iterable<String> iterable = (List) arrayList;
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (String indentWidth$StringsKt__IndentKt : iterable) {
            arrayList2.add(Integer.valueOf(indentWidth$StringsKt__IndentKt(indentWidth$StringsKt__IndentKt)));
        }
        Integer num = (Integer) CollectionsKt.minOrNull((List) arrayList2);
        int intValue = num != null ? num.intValue() : 0;
        int length = $this$replaceIndent.length() + (newIndent.length() * lines.size());
        Function1<String, String> indentFunction$StringsKt__IndentKt = getIndentFunction$StringsKt__IndentKt(newIndent);
        List<String> list = lines;
        int lastIndex = CollectionsKt.getLastIndex(list);
        Collection arrayList3 = new ArrayList();
        int i = 0;
        for (Object next2 : list) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            String str3 = (String) next2;
            int i3 = i;
            if ((i3 == 0 || i3 == lastIndex) && StringsKt.isBlank(str3)) {
                str = null;
                int i4 = i3;
            } else {
                int i5 = i3;
                String drop = StringsKt.drop(str3, intValue);
                Log1F380D.a((Object) drop);
                if (drop == null || (str = indentFunction$StringsKt__IndentKt.invoke(drop)) == null) {
                    str = str3;
                }
            }
            if (str != null) {
                arrayList3.add(str);
            }
            String str4 = $this$replaceIndent;
            i = i2;
        }
        String sb = ((StringBuilder) CollectionsKt.joinTo$default((List) arrayList3, new StringBuilder(length), "\n", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 124, (Object) null)).toString();
        Intrinsics.checkNotNullExpressionValue(sb, "mapIndexedNotNull { inde…\"\\n\")\n        .toString()");
        return sb;
    }

    public static /* synthetic */ String replaceIndent$default(String str, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            str2 = HttpUrl.FRAGMENT_ENCODE_SET;
        }
        String replaceIndent = StringsKt.replaceIndent(str, str2);
        Log1F380D.a((Object) replaceIndent);
        return replaceIndent;
    }

    public static /* synthetic */ String replaceIndentByMargin$default(String str, String str2, String str3, int i, Object obj) {
        if ((i & 1) != 0) {
            str2 = HttpUrl.FRAGMENT_ENCODE_SET;
        }
        if ((i & 2) != 0) {
            str3 = "|";
        }
        String replaceIndentByMargin = StringsKt.replaceIndentByMargin(str, str2, str3);
        Log1F380D.a((Object) replaceIndentByMargin);
        return replaceIndentByMargin;
    }

    public static final String trimIndent(String $this$trimIndent) {
        Intrinsics.checkNotNullParameter($this$trimIndent, "<this>");
        String replaceIndent = StringsKt.replaceIndent($this$trimIndent, HttpUrl.FRAGMENT_ENCODE_SET);
        Log1F380D.a((Object) replaceIndent);
        return replaceIndent;
    }

    public static final String trimMargin(String $this$trimMargin, String marginPrefix) {
        Intrinsics.checkNotNullParameter($this$trimMargin, "<this>");
        Intrinsics.checkNotNullParameter(marginPrefix, "marginPrefix");
        String replaceIndentByMargin = StringsKt.replaceIndentByMargin($this$trimMargin, HttpUrl.FRAGMENT_ENCODE_SET, marginPrefix);
        Log1F380D.a((Object) replaceIndentByMargin);
        return replaceIndentByMargin;
    }

    public static /* synthetic */ String trimMargin$default(String str, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            str2 = "|";
        }
        String trimMargin = StringsKt.trimMargin(str, str2);
        Log1F380D.a((Object) trimMargin);
        return trimMargin;
    }
}

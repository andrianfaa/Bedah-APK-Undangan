package okhttp3.internal.publicsuffix;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.IDN;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;
import mt.Log1F380D;
import okhttp3.HttpUrl;
import okhttp3.internal.Util;
import okhttp3.internal.platform.Platform;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u0005¢\u0006\u0002\u0010\u0002J\u001c\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0002J\u0010\u0010\u000e\u001a\u0004\u0018\u00010\f2\u0006\u0010\u000f\u001a\u00020\fJ\b\u0010\u0010\u001a\u00020\u0011H\u0002J\b\u0010\u0012\u001a\u00020\u0011H\u0002J\u0016\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0006J\u0016\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\u000f\u001a\u00020\fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X.¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"}, d2 = {"Lokhttp3/internal/publicsuffix/PublicSuffixDatabase;", "", "()V", "listRead", "Ljava/util/concurrent/atomic/AtomicBoolean;", "publicSuffixExceptionListBytes", "", "publicSuffixListBytes", "readCompleteLatch", "Ljava/util/concurrent/CountDownLatch;", "findMatchingRule", "", "", "domainLabels", "getEffectiveTldPlusOne", "domain", "readTheList", "", "readTheListUninterruptibly", "setListBytes", "splitDomain", "Companion", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: 01E2 */
public final class PublicSuffixDatabase {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final char EXCEPTION_MARKER = '!';
    private static final List<String> PREVAILING_RULE = CollectionsKt.listOf("*");
    public static final String PUBLIC_SUFFIX_RESOURCE = "publicsuffixes.gz";
    private static final byte[] WILDCARD_LABEL = {(byte) 42};
    /* access modifiers changed from: private */
    public static final PublicSuffixDatabase instance = new PublicSuffixDatabase();
    private final AtomicBoolean listRead = new AtomicBoolean(false);
    private byte[] publicSuffixExceptionListBytes;
    /* access modifiers changed from: private */
    public byte[] publicSuffixListBytes;
    private final CountDownLatch readCompleteLatch = new CountDownLatch(1);

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\f\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\r\u001a\u00020\fJ)\u0010\u000e\u001a\u0004\u0018\u00010\u0007*\u00020\n2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002¢\u0006\u0002\u0010\u0013R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lokhttp3/internal/publicsuffix/PublicSuffixDatabase$Companion;", "", "()V", "EXCEPTION_MARKER", "", "PREVAILING_RULE", "", "", "PUBLIC_SUFFIX_RESOURCE", "WILDCARD_LABEL", "", "instance", "Lokhttp3/internal/publicsuffix/PublicSuffixDatabase;", "get", "binarySearch", "labels", "", "labelIndex", "", "([B[[BI)Ljava/lang/String;", "okhttp"}, k = 1, mv = {1, 4, 0})
    /* compiled from: 01E1 */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        /* access modifiers changed from: private */
        public final String binarySearch(byte[] $this$binarySearch, byte[][] labels, int labelIndex) {
            int i;
            int and;
            int i2;
            byte[] bArr = $this$binarySearch;
            int i3 = 0;
            int length = bArr.length;
            String str = null;
            while (i3 < length) {
                int i4 = (i3 + length) / 2;
                while (i4 > -1 && bArr[i4] != ((byte) 10)) {
                    i4--;
                }
                int i5 = i4 + 1;
                int i6 = 1;
                while (bArr[i5 + i6] != ((byte) 10)) {
                    i6++;
                }
                int i7 = (i5 + i6) - i5;
                int i8 = labelIndex;
                int i9 = 0;
                int i10 = 0;
                boolean z = false;
                while (true) {
                    if (z) {
                        i = 46;
                        z = false;
                    } else {
                        i = Util.and(labels[i8][i9], 255);
                    }
                    and = i - Util.and(bArr[i5 + i10], 255);
                    if (and == 0) {
                        i10++;
                        i9++;
                        if (i10 == i7) {
                            break;
                        }
                        if (labels[i8].length != i9) {
                            i2 = i3;
                        } else if (i8 == ((Object[]) labels).length - 1) {
                            break;
                        } else {
                            i2 = i3;
                            i8++;
                            z = true;
                            i9 = -1;
                        }
                        i3 = i2;
                    } else {
                        break;
                    }
                }
                if (and < 0) {
                    length = i5 - 1;
                } else if (and > 0) {
                    i3 = i5 + i6 + 1;
                } else {
                    int i11 = i7 - i10;
                    int length2 = labels[i8].length - i9;
                    int i12 = i8 + 1;
                    int length3 = ((Object[]) labels).length;
                    while (i12 < length3) {
                        length2 += labels[i12].length;
                        i12++;
                        i3 = i3;
                    }
                    int i13 = i3;
                    if (length2 < i11) {
                        length = i5 - 1;
                        i3 = i13;
                    } else if (length2 > i11) {
                        i3 = i5 + i6 + 1;
                    } else {
                        Charset charset = StandardCharsets.UTF_8;
                        Intrinsics.checkNotNullExpressionValue(charset, "UTF_8");
                        String str2 = new String(bArr, i5, i7, charset);
                        Log1F380D.a((Object) str2);
                        return str2;
                    }
                }
            }
            int i14 = i3;
            return str;
        }

        public final PublicSuffixDatabase get() {
            return PublicSuffixDatabase.instance;
        }
    }

    public static final /* synthetic */ byte[] access$getPublicSuffixListBytes$p(PublicSuffixDatabase $this) {
        byte[] bArr = $this.publicSuffixListBytes;
        if (bArr == null) {
            Intrinsics.throwUninitializedPropertyAccessException("publicSuffixListBytes");
        }
        return bArr;
    }

    private final List<String> findMatchingRule(List<String> domainLabels) {
        List<String> list;
        List<String> list2;
        if (this.listRead.get() || !this.listRead.compareAndSet(false, true)) {
            try {
                this.readCompleteLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            readTheListUninterruptibly();
        }
        if (this.publicSuffixListBytes != null) {
            int size = domainLabels.size();
            byte[][] bArr = new byte[size][];
            int i = 0;
            while (i < size) {
                String str = domainLabels.get(i);
                Charset charset = StandardCharsets.UTF_8;
                Intrinsics.checkNotNullExpressionValue(charset, "UTF_8");
                if (str != null) {
                    byte[] bytes = str.getBytes(charset);
                    Intrinsics.checkNotNullExpressionValue(bytes, "(this as java.lang.String).getBytes(charset)");
                    bArr[i] = bytes;
                    i++;
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
            }
            byte[][] bArr2 = bArr;
            String str2 = null;
            int length = bArr2.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                Companion companion = Companion;
                byte[] bArr3 = this.publicSuffixListBytes;
                if (bArr3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("publicSuffixListBytes");
                }
                String access$binarySearch = companion.binarySearch(bArr3, bArr2, i2);
                Log1F380D.a((Object) access$binarySearch);
                if (access$binarySearch != null) {
                    str2 = access$binarySearch;
                    break;
                }
                i2++;
            }
            String str3 = null;
            if (((Object[]) bArr2).length > 1) {
                byte[][] bArr4 = (byte[][]) ((Object[]) bArr2).clone();
                int length2 = ((Object[]) bArr4).length - 1;
                int i3 = 0;
                while (true) {
                    if (i3 >= length2) {
                        break;
                    }
                    bArr4[i3] = WILDCARD_LABEL;
                    Companion companion2 = Companion;
                    byte[] bArr5 = this.publicSuffixListBytes;
                    if (bArr5 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("publicSuffixListBytes");
                    }
                    String access$binarySearch2 = companion2.binarySearch(bArr5, bArr4, i3);
                    Log1F380D.a((Object) access$binarySearch2);
                    if (access$binarySearch2 != null) {
                        str3 = access$binarySearch2;
                        break;
                    }
                    i3++;
                }
            }
            String str4 = null;
            if (str3 != null) {
                int length3 = ((Object[]) bArr2).length - 1;
                int i4 = 0;
                while (true) {
                    if (i4 >= length3) {
                        break;
                    }
                    Companion companion3 = Companion;
                    byte[] bArr6 = this.publicSuffixExceptionListBytes;
                    if (bArr6 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("publicSuffixExceptionListBytes");
                    }
                    String access$binarySearch3 = companion3.binarySearch(bArr6, bArr2, i4);
                    Log1F380D.a((Object) access$binarySearch3);
                    if (access$binarySearch3 != null) {
                        str4 = access$binarySearch3;
                        break;
                    }
                    i4++;
                }
            }
            if (str4 != null) {
                return StringsKt.split$default((CharSequence) EXCEPTION_MARKER + str4, new char[]{'.'}, false, 0, 6, (Object) null);
            } else if (str2 == null && str3 == null) {
                return PREVAILING_RULE;
            } else {
                if (str2 == null || (list = StringsKt.split$default((CharSequence) str2, new char[]{'.'}, false, 0, 6, (Object) null)) == null) {
                    list = CollectionsKt.emptyList();
                }
                if (str3 == null || (list2 = StringsKt.split$default((CharSequence) str3, new char[]{'.'}, false, 0, 6, (Object) null)) == null) {
                    list2 = CollectionsKt.emptyList();
                }
                return list.size() > list2.size() ? list : list2;
            }
        } else {
            throw new IllegalStateException("Unable to load publicsuffixes.gz resource from the classpath.".toString());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x005b, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005c, code lost:
        kotlin.io.CloseableKt.closeFinally(r3, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005f, code lost:
        throw r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void readTheList() throws java.io.IOException {
        /*
            r11 = this;
            r0 = 0
            r1 = 0
            java.lang.Class<okhttp3.internal.publicsuffix.PublicSuffixDatabase> r2 = okhttp3.internal.publicsuffix.PublicSuffixDatabase.class
            java.lang.String r3 = "publicsuffixes.gz"
            java.io.InputStream r2 = r2.getResourceAsStream(r3)
            if (r2 == 0) goto L_0x0060
            okio.GzipSource r3 = new okio.GzipSource
            okio.Source r4 = okio.Okio.source((java.io.InputStream) r2)
            r3.<init>(r4)
            okio.Source r3 = (okio.Source) r3
            okio.BufferedSource r3 = okio.Okio.buffer((okio.Source) r3)
            java.io.Closeable r3 = (java.io.Closeable) r3
            r4 = 0
            r5 = r4
            java.lang.Throwable r5 = (java.lang.Throwable) r5
            r5 = r3
            okio.BufferedSource r5 = (okio.BufferedSource) r5     // Catch:{ all -> 0x0059 }
            r6 = 0
            int r7 = r5.readInt()     // Catch:{ all -> 0x0059 }
            long r8 = (long) r7     // Catch:{ all -> 0x0059 }
            byte[] r8 = r5.readByteArray(r8)     // Catch:{ all -> 0x0059 }
            r0 = r8
            int r8 = r5.readInt()     // Catch:{ all -> 0x0059 }
            long r9 = (long) r8     // Catch:{ all -> 0x0059 }
            byte[] r9 = r5.readByteArray(r9)     // Catch:{ all -> 0x0059 }
            r1 = r9
            kotlin.Unit r5 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x0059 }
            kotlin.io.CloseableKt.closeFinally(r3, r4)
            monitor-enter(r11)
            r3 = 0
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)     // Catch:{ all -> 0x0056 }
            r11.publicSuffixListBytes = r0     // Catch:{ all -> 0x0056 }
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)     // Catch:{ all -> 0x0056 }
            r11.publicSuffixExceptionListBytes = r1     // Catch:{ all -> 0x0056 }
            kotlin.Unit r3 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x0056 }
            monitor-exit(r11)
            java.util.concurrent.CountDownLatch r3 = r11.readCompleteLatch
            r3.countDown()
            return
        L_0x0056:
            r3 = move-exception
            monitor-exit(r11)
            throw r3
        L_0x0059:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x005b }
        L_0x005b:
            r5 = move-exception
            kotlin.io.CloseableKt.closeFinally(r3, r4)
            throw r5
        L_0x0060:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.publicsuffix.PublicSuffixDatabase.readTheList():void");
    }

    private final void readTheListUninterruptibly() {
        boolean z = false;
        while (true) {
            try {
                readTheList();
                break;
            } catch (InterruptedIOException e) {
                Thread.interrupted();
                z = true;
            } catch (IOException e2) {
                Platform.Companion.get().log("Failed to read public suffix list", 5, e2);
                if (z) {
                    Thread.currentThread().interrupt();
                    return;
                }
                return;
            } catch (Throwable th) {
                if (z) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (z) {
            Thread.currentThread().interrupt();
        }
    }

    private final List<String> splitDomain(String domain) {
        List<String> split$default = StringsKt.split$default((CharSequence) domain, new char[]{'.'}, false, 0, 6, (Object) null);
        return Intrinsics.areEqual((Object) (String) CollectionsKt.last(split$default), (Object) HttpUrl.FRAGMENT_ENCODE_SET) ? CollectionsKt.dropLast(split$default, 1) : split$default;
    }

    public final void setListBytes(byte[] publicSuffixListBytes2, byte[] publicSuffixExceptionListBytes2) {
        Intrinsics.checkNotNullParameter(publicSuffixListBytes2, "publicSuffixListBytes");
        Intrinsics.checkNotNullParameter(publicSuffixExceptionListBytes2, "publicSuffixExceptionListBytes");
        this.publicSuffixListBytes = publicSuffixListBytes2;
        this.publicSuffixExceptionListBytes = publicSuffixExceptionListBytes2;
        this.listRead.set(true);
        this.readCompleteLatch.countDown();
    }

    public final String getEffectiveTldPlusOne(String domain) {
        Intrinsics.checkNotNullParameter(domain, "domain");
        String unicode = IDN.toUnicode(domain);
        Log1F380D.a((Object) unicode);
        Intrinsics.checkNotNullExpressionValue(unicode, "unicodeDomain");
        List<String> splitDomain = splitDomain(unicode);
        List<String> findMatchingRule = findMatchingRule(splitDomain);
        if (splitDomain.size() == findMatchingRule.size() && findMatchingRule.get(0).charAt(0) != '!') {
            return null;
        }
        String joinToString$default = SequencesKt.joinToString$default(SequencesKt.drop(CollectionsKt.asSequence(splitDomain(domain)), findMatchingRule.get(0).charAt(0) == '!' ? splitDomain.size() - findMatchingRule.size() : splitDomain.size() - (findMatchingRule.size() + 1)), ".", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null);
        Log1F380D.a((Object) joinToString$default);
        return joinToString$default;
    }
}

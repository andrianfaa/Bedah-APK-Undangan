package okhttp3;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMutableIterator;
import okhttp3.internal.cache.DiskLruCache;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000'\n\u0000\n\u0002\u0010)\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\t\u0010\t\u001a\u00020\u0004H\u0002J\t\u0010\n\u001a\u00020\u0002H\u0002J\b\u0010\u000b\u001a\u00020\fH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u0018\u0010\u0005\u001a\f\u0012\b\u0012\u00060\u0006R\u00020\u00070\u0001X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\u0002X\u000e¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"okhttp3/Cache$urls$1", "", "", "canRemove", "", "delegate", "Lokhttp3/internal/cache/DiskLruCache$Snapshot;", "Lokhttp3/internal/cache/DiskLruCache;", "nextUrl", "hasNext", "next", "remove", "", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: Cache.kt */
public final class Cache$urls$1 implements Iterator<String>, KMutableIterator {
    private boolean canRemove;
    private final Iterator<DiskLruCache.Snapshot> delegate;
    private String nextUrl;
    final /* synthetic */ Cache this$0;

    Cache$urls$1(Cache this$02) {
        this.this$0 = this$02;
        this.delegate = this$02.getCache$okhttp().snapshots();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0037, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0038, code lost:
        continue;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        kotlin.io.CloseableKt.closeFinally(r2, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003b, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x003c, code lost:
        continue;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hasNext() {
        /*
            r8 = this;
            java.lang.String r0 = r8.nextUrl
            r1 = 1
            if (r0 == 0) goto L_0x0006
            return r1
        L_0x0006:
            r0 = 0
            r8.canRemove = r0
        L_0x0009:
            java.util.Iterator<okhttp3.internal.cache.DiskLruCache$Snapshot> r2 = r8.delegate
            boolean r2 = r2.hasNext()
            if (r2 == 0) goto L_0x003f
            java.util.Iterator<okhttp3.internal.cache.DiskLruCache$Snapshot> r2 = r8.delegate     // Catch:{ IOException -> 0x003c }
            java.lang.Object r2 = r2.next()     // Catch:{ IOException -> 0x003c }
            java.io.Closeable r2 = (java.io.Closeable) r2     // Catch:{ IOException -> 0x003c }
            r3 = 0
            r4 = r3
            java.lang.Throwable r4 = (java.lang.Throwable) r4     // Catch:{ IOException -> 0x003c }
            r4 = r2
            okhttp3.internal.cache.DiskLruCache$Snapshot r4 = (okhttp3.internal.cache.DiskLruCache.Snapshot) r4     // Catch:{ all -> 0x0035 }
            r5 = 0
            okio.Source r6 = r4.getSource(r0)     // Catch:{ all -> 0x0035 }
            okio.BufferedSource r6 = okio.Okio.buffer((okio.Source) r6)     // Catch:{ all -> 0x0035 }
            java.lang.String r7 = r6.readUtf8LineStrict()     // Catch:{ all -> 0x0035 }
            r8.nextUrl = r7     // Catch:{ all -> 0x0035 }
            kotlin.io.CloseableKt.closeFinally(r2, r3)     // Catch:{ IOException -> 0x003c }
            return r1
        L_0x0035:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x0037 }
        L_0x0037:
            r4 = move-exception
            kotlin.io.CloseableKt.closeFinally(r2, r3)     // Catch:{ IOException -> 0x003c }
            throw r4     // Catch:{ IOException -> 0x003c }
        L_0x003c:
            r2 = move-exception
            goto L_0x0009
        L_0x003f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.Cache$urls$1.hasNext():boolean");
    }

    public String next() {
        if (hasNext()) {
            String str = this.nextUrl;
            Intrinsics.checkNotNull(str);
            String str2 = null;
            this.nextUrl = null;
            this.canRemove = true;
            return str;
        }
        throw new NoSuchElementException();
    }

    public void remove() {
        if (this.canRemove) {
            this.delegate.remove();
            return;
        }
        throw new IllegalStateException("remove() before next()".toString());
    }
}

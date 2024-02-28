package okhttp3.internal.cache;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.internal.concurrent.Task;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, d2 = {"okhttp3/internal/cache/DiskLruCache$cleanupTask$1", "Lokhttp3/internal/concurrent/Task;", "runOnce", "", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: DiskLruCache.kt */
public final class DiskLruCache$cleanupTask$1 extends Task {
    final /* synthetic */ DiskLruCache this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    DiskLruCache$cleanupTask$1(DiskLruCache this$02, String $super_call_param$1) {
        super($super_call_param$1, false, 2, (DefaultConstructorMarker) null);
        this.this$0 = this$02;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0054, code lost:
        return -1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long runOnce() {
        /*
            r7 = this;
            okhttp3.internal.cache.DiskLruCache r0 = r7.this$0
            monitor-enter(r0)
            r1 = 0
            okhttp3.internal.cache.DiskLruCache r2 = r7.this$0     // Catch:{ all -> 0x0055 }
            boolean r2 = r2.initialized     // Catch:{ all -> 0x0055 }
            r3 = -1
            if (r2 == 0) goto L_0x0052
            okhttp3.internal.cache.DiskLruCache r2 = r7.this$0     // Catch:{ all -> 0x0055 }
            boolean r2 = r2.getClosed$okhttp()     // Catch:{ all -> 0x0055 }
            if (r2 == 0) goto L_0x0017
            goto L_0x0052
        L_0x0017:
            r2 = 1
            okhttp3.internal.cache.DiskLruCache r5 = r7.this$0     // Catch:{ IOException -> 0x001f }
            r5.trimToSize()     // Catch:{ IOException -> 0x001f }
            goto L_0x0025
        L_0x001f:
            r5 = move-exception
            okhttp3.internal.cache.DiskLruCache r6 = r7.this$0     // Catch:{ all -> 0x0055 }
            r6.mostRecentTrimFailed = r2     // Catch:{ all -> 0x0055 }
        L_0x0025:
            okhttp3.internal.cache.DiskLruCache r5 = r7.this$0     // Catch:{ IOException -> 0x003b }
            boolean r5 = r5.journalRebuildRequired()     // Catch:{ IOException -> 0x003b }
            if (r5 == 0) goto L_0x004e
            okhttp3.internal.cache.DiskLruCache r5 = r7.this$0     // Catch:{ IOException -> 0x003b }
            r5.rebuildJournal$okhttp()     // Catch:{ IOException -> 0x003b }
            okhttp3.internal.cache.DiskLruCache r5 = r7.this$0     // Catch:{ IOException -> 0x003b }
            r6 = 0
            r5.redundantOpCount = r6     // Catch:{ IOException -> 0x003b }
            goto L_0x004e
        L_0x003b:
            r5 = move-exception
            okhttp3.internal.cache.DiskLruCache r6 = r7.this$0     // Catch:{ all -> 0x0055 }
            r6.mostRecentRebuildFailed = r2     // Catch:{ all -> 0x0055 }
            okhttp3.internal.cache.DiskLruCache r2 = r7.this$0     // Catch:{ all -> 0x0055 }
            okio.Sink r6 = okio.Okio.blackhole()     // Catch:{ all -> 0x0055 }
            okio.BufferedSink r6 = okio.Okio.buffer((okio.Sink) r6)     // Catch:{ all -> 0x0055 }
            r2.journalWriter = r6     // Catch:{ all -> 0x0055 }
        L_0x004e:
            monitor-exit(r0)
            return r3
        L_0x0052:
            monitor-exit(r0)
            return r3
        L_0x0055:
            r1 = move-exception
            monitor-exit(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache$cleanupTask$1.runOnce():long");
    }
}

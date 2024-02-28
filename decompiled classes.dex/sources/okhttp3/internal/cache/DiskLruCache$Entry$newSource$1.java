package okhttp3.internal.cache;

import kotlin.Metadata;
import kotlin.Unit;
import okhttp3.internal.cache.DiskLruCache;
import okio.ForwardingSource;
import okio.Source;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0004\u001a\u00020\u0005H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"okhttp3/internal/cache/DiskLruCache$Entry$newSource$1", "Lokio/ForwardingSource;", "closed", "", "close", "", "okhttp"}, k = 1, mv = {1, 4, 0})
/* compiled from: DiskLruCache.kt */
public final class DiskLruCache$Entry$newSource$1 extends ForwardingSource {
    final /* synthetic */ Source $fileSource;
    private boolean closed;
    final /* synthetic */ DiskLruCache.Entry this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    DiskLruCache$Entry$newSource$1(DiskLruCache.Entry this$02, Source $captured_local_variable$1, Source $super_call_param$2) {
        super($super_call_param$2);
        this.this$0 = this$02;
        this.$fileSource = $captured_local_variable$1;
    }

    public void close() {
        super.close();
        if (!this.closed) {
            this.closed = true;
            synchronized (this.this$0.this$0) {
                DiskLruCache.Entry entry = this.this$0;
                entry.setLockingSourceCount$okhttp(entry.getLockingSourceCount$okhttp() - 1);
                if (this.this$0.getLockingSourceCount$okhttp() == 0 && this.this$0.getZombie$okhttp()) {
                    this.this$0.this$0.removeEntry$okhttp(this.this$0);
                }
                Unit unit = Unit.INSTANCE;
            }
        }
    }
}

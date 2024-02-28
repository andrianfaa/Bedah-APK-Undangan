package androidx.lifecycle;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public abstract class ViewModel {
    private final Map<String, Object> mBagOfTags;
    private volatile boolean mCleared;
    private final Set<Closeable> mCloseables;

    public ViewModel() {
        this.mBagOfTags = new HashMap();
        this.mCloseables = new LinkedHashSet();
        this.mCleared = false;
    }

    public ViewModel(Closeable... closeables) {
        this.mBagOfTags = new HashMap();
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        this.mCloseables = linkedHashSet;
        this.mCleared = false;
        linkedHashSet.addAll(Arrays.asList(closeables));
    }

    private static void closeWithRuntimeException(Object obj) {
        if (obj instanceof Closeable) {
            try {
                ((Closeable) obj).close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addCloseable(Closeable closeable) {
        Set<Closeable> set = this.mCloseables;
        if (set != null) {
            synchronized (set) {
                this.mCloseables.add(closeable);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void clear() {
        this.mCleared = true;
        Map<String, Object> map = this.mBagOfTags;
        if (map != null) {
            synchronized (map) {
                for (Object closeWithRuntimeException : this.mBagOfTags.values()) {
                    closeWithRuntimeException(closeWithRuntimeException);
                }
            }
        }
        Set<Closeable> set = this.mCloseables;
        if (set != null) {
            synchronized (set) {
                for (Closeable closeWithRuntimeException2 : this.mCloseables) {
                    closeWithRuntimeException(closeWithRuntimeException2);
                }
            }
        }
        onCleared();
    }

    /* access modifiers changed from: package-private */
    public <T> T getTag(String key) {
        T t;
        Map<String, Object> map = this.mBagOfTags;
        if (map == null) {
            return null;
        }
        synchronized (map) {
            t = this.mBagOfTags.get(key);
        }
        return t;
    }

    /* access modifiers changed from: protected */
    public void onCleared() {
    }

    /* access modifiers changed from: package-private */
    public <T> T setTagIfAbsent(String key, T t) {
        T t2;
        synchronized (this.mBagOfTags) {
            t2 = this.mBagOfTags.get(key);
            if (t2 == null) {
                this.mBagOfTags.put(key, t);
            }
        }
        T t3 = t2 == null ? t : t2;
        if (this.mCleared) {
            closeWithRuntimeException(t3);
        }
        return t3;
    }
}

package androidx.work;

import android.content.Context;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import mt.Log1F380D;

/* compiled from: 00A6 */
public class DelegatingWorkerFactory extends WorkerFactory {
    private static final String TAG;
    private final List<WorkerFactory> mFactories = new CopyOnWriteArrayList();

    static {
        String tagWithPrefix = Logger.tagWithPrefix("DelegatingWkrFctry");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public final void addFactory(WorkerFactory workerFactory) {
        this.mFactories.add(workerFactory);
    }

    /* access modifiers changed from: package-private */
    public List<WorkerFactory> getFactories() {
        return this.mFactories;
    }

    public final ListenableWorker createWorker(Context appContext, String workerClassName, WorkerParameters workerParameters) {
        for (WorkerFactory createWorker : this.mFactories) {
            try {
                ListenableWorker createWorker2 = createWorker.createWorker(appContext, workerClassName, workerParameters);
                if (createWorker2 != null) {
                    return createWorker2;
                }
            } catch (Throwable th) {
                String format = String.format("Unable to instantiate a ListenableWorker (%s)", new Object[]{workerClassName});
                Log1F380D.a((Object) format);
                Logger.get().error(TAG, format, th);
                throw th;
            }
        }
        return null;
    }
}

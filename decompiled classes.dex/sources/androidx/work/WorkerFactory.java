package androidx.work;

import android.content.Context;
import mt.Log1F380D;

/* compiled from: 00A5 */
public abstract class WorkerFactory {
    private static final String TAG;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("WorkerFactory");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public static WorkerFactory getDefaultWorkerFactory() {
        return new WorkerFactory() {
            public ListenableWorker createWorker(Context appContext, String workerClassName, WorkerParameters workerParameters) {
                return null;
            }
        };
    }

    public abstract ListenableWorker createWorker(Context context, String str, WorkerParameters workerParameters);

    public final ListenableWorker createWorkerWithDefaultFallback(Context appContext, String workerClassName, WorkerParameters workerParameters) {
        ListenableWorker createWorker = createWorker(appContext, workerClassName, workerParameters);
        if (createWorker == null) {
            Class<? extends U> cls = null;
            try {
                cls = Class.forName(workerClassName).asSubclass(ListenableWorker.class);
            } catch (Throwable th) {
                Logger.get().error(TAG, "Invalid class: " + workerClassName, th);
            }
            if (cls != null) {
                try {
                    createWorker = (ListenableWorker) cls.getDeclaredConstructor(new Class[]{Context.class, WorkerParameters.class}).newInstance(new Object[]{appContext, workerParameters});
                } catch (Throwable th2) {
                    Logger.get().error(TAG, "Could not instantiate " + workerClassName, th2);
                }
            }
        }
        if (createWorker == null || !createWorker.isUsed()) {
            return createWorker;
        }
        String format = String.format("WorkerFactory (%s) returned an instance of a ListenableWorker (%s) which has already been invoked. createWorker() must always return a new instance of a ListenableWorker.", new Object[]{getClass().getName(), workerClassName});
        Log1F380D.a((Object) format);
        throw new IllegalStateException(format);
    }
}

package androidx.work.impl.constraints.trackers;

import android.content.Context;
import androidx.work.Logger;
import androidx.work.impl.constraints.ConstraintListener;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import java.util.LinkedHashSet;
import java.util.Set;
import mt.Log1F380D;

/* compiled from: 00C9 */
public abstract class ConstraintTracker<T> {
    private static final String TAG;
    protected final Context mAppContext;
    T mCurrentState;
    private final Set<ConstraintListener<T>> mListeners = new LinkedHashSet();
    private final Object mLock = new Object();
    protected final TaskExecutor mTaskExecutor;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("ConstraintTracker");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    ConstraintTracker(Context context, TaskExecutor taskExecutor) {
        this.mAppContext = context.getApplicationContext();
        this.mTaskExecutor = taskExecutor;
    }

    public abstract T getInitialState();

    public void removeListener(ConstraintListener<T> constraintListener) {
        synchronized (this.mLock) {
            if (this.mListeners.remove(constraintListener) && this.mListeners.isEmpty()) {
                stopTracking();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002a, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setState(T r5) {
        /*
            r4 = this;
            java.lang.Object r0 = r4.mLock
            monitor-enter(r0)
            T r1 = r4.mCurrentState     // Catch:{ all -> 0x002b }
            if (r1 == r5) goto L_0x0029
            if (r1 == 0) goto L_0x0010
            boolean r1 = r1.equals(r5)     // Catch:{ all -> 0x002b }
            if (r1 == 0) goto L_0x0010
            goto L_0x0029
        L_0x0010:
            r4.mCurrentState = r5     // Catch:{ all -> 0x002b }
            java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ all -> 0x002b }
            java.util.Set<androidx.work.impl.constraints.ConstraintListener<T>> r2 = r4.mListeners     // Catch:{ all -> 0x002b }
            r1.<init>(r2)     // Catch:{ all -> 0x002b }
            androidx.work.impl.utils.taskexecutor.TaskExecutor r2 = r4.mTaskExecutor     // Catch:{ all -> 0x002b }
            java.util.concurrent.Executor r2 = r2.getMainThreadExecutor()     // Catch:{ all -> 0x002b }
            androidx.work.impl.constraints.trackers.ConstraintTracker$1 r3 = new androidx.work.impl.constraints.trackers.ConstraintTracker$1     // Catch:{ all -> 0x002b }
            r3.<init>(r1)     // Catch:{ all -> 0x002b }
            r2.execute(r3)     // Catch:{ all -> 0x002b }
            monitor-exit(r0)     // Catch:{ all -> 0x002b }
            return
        L_0x0029:
            monitor-exit(r0)     // Catch:{ all -> 0x002b }
            return
        L_0x002b:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002b }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.work.impl.constraints.trackers.ConstraintTracker.setState(java.lang.Object):void");
    }

    public abstract void startTracking();

    public abstract void stopTracking();

    public void addListener(ConstraintListener<T> constraintListener) {
        synchronized (this.mLock) {
            if (this.mListeners.add(constraintListener)) {
                if (this.mListeners.size() == 1) {
                    this.mCurrentState = getInitialState();
                    Logger logger = Logger.get();
                    String str = TAG;
                    String format = String.format("%s: initial state = %s", new Object[]{getClass().getSimpleName(), this.mCurrentState});
                    Log1F380D.a((Object) format);
                    logger.debug(str, format, new Throwable[0]);
                    startTracking();
                }
                constraintListener.onConstraintChanged(this.mCurrentState);
            }
        }
    }
}

package androidx.work.impl.background.greedy;

import androidx.work.Logger;
import androidx.work.RunnableScheduler;
import androidx.work.impl.model.WorkSpec;
import java.util.HashMap;
import java.util.Map;
import mt.Log1F380D;

/* compiled from: 00B4 */
public class DelayedWorkTracker {
    static final String TAG;
    final GreedyScheduler mGreedyScheduler;
    private final RunnableScheduler mRunnableScheduler;
    private final Map<String, Runnable> mRunnables = new HashMap();

    static {
        String tagWithPrefix = Logger.tagWithPrefix("DelayedWorkTracker");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public DelayedWorkTracker(GreedyScheduler scheduler, RunnableScheduler runnableScheduler) {
        this.mGreedyScheduler = scheduler;
        this.mRunnableScheduler = runnableScheduler;
    }

    public void schedule(final WorkSpec workSpec) {
        Runnable remove = this.mRunnables.remove(workSpec.id);
        if (remove != null) {
            this.mRunnableScheduler.cancel(remove);
        }
        AnonymousClass1 r1 = new Runnable() {
            public void run() {
                Logger logger = Logger.get();
                String str = DelayedWorkTracker.TAG;
                String format = String.format("Scheduling work %s", new Object[]{workSpec.id});
                Log1F380D.a((Object) format);
                logger.debug(str, format, new Throwable[0]);
                DelayedWorkTracker.this.mGreedyScheduler.schedule(workSpec);
            }
        };
        this.mRunnables.put(workSpec.id, r1);
        long currentTimeMillis = System.currentTimeMillis();
        this.mRunnableScheduler.scheduleWithDelay(workSpec.calculateNextRunTime() - currentTimeMillis, r1);
    }

    public void unschedule(String workSpecId) {
        Runnable remove = this.mRunnables.remove(workSpecId);
        if (remove != null) {
            this.mRunnableScheduler.cancel(remove);
        }
    }
}

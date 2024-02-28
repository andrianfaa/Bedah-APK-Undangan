package androidx.work;

import android.content.Context;
import androidx.startup.Initializer;
import androidx.work.Configuration;
import java.util.Collections;
import java.util.List;
import mt.Log1F380D;

/* compiled from: 00A8 */
public final class WorkManagerInitializer implements Initializer<WorkManager> {
    private static final String TAG;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("WrkMgrInitializer");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public WorkManager create(Context context) {
        Logger.get().debug(TAG, "Initializing WorkManager with default configuration.", new Throwable[0]);
        WorkManager.initialize(context, new Configuration.Builder().build());
        return WorkManager.getInstance(context);
    }

    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}

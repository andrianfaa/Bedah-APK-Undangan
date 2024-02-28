package androidx.core.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Iterator;

public final class TaskStackBuilder implements Iterable<Intent> {
    private static final String TAG = "TaskStackBuilder";
    private final ArrayList<Intent> mIntents = new ArrayList<>();
    private final Context mSourceContext;

    static class Api16Impl {
        private Api16Impl() {
        }

        static PendingIntent getActivities(Context context, int requestCode, Intent[] intents, int flags, Bundle options) {
            return PendingIntent.getActivities(context, requestCode, intents, flags, options);
        }
    }

    public interface SupportParentable {
        Intent getSupportParentActivityIntent();
    }

    private TaskStackBuilder(Context a) {
        this.mSourceContext = a;
    }

    public static TaskStackBuilder create(Context context) {
        return new TaskStackBuilder(context);
    }

    @Deprecated
    public static TaskStackBuilder from(Context context) {
        return create(context);
    }

    public TaskStackBuilder addNextIntent(Intent nextIntent) {
        this.mIntents.add(nextIntent);
        return this;
    }

    public TaskStackBuilder addNextIntentWithParentStack(Intent nextIntent) {
        ComponentName component = nextIntent.getComponent();
        if (component == null) {
            component = nextIntent.resolveActivity(this.mSourceContext.getPackageManager());
        }
        if (component != null) {
            addParentStack(component);
        }
        addNextIntent(nextIntent);
        return this;
    }

    public TaskStackBuilder addParentStack(Activity sourceActivity) {
        Intent intent = null;
        if (sourceActivity instanceof SupportParentable) {
            intent = ((SupportParentable) sourceActivity).getSupportParentActivityIntent();
        }
        if (intent == null) {
            intent = NavUtils.getParentActivityIntent(sourceActivity);
        }
        if (intent != null) {
            ComponentName component = intent.getComponent();
            if (component == null) {
                component = intent.resolveActivity(this.mSourceContext.getPackageManager());
            }
            addParentStack(component);
            addNextIntent(intent);
        }
        return this;
    }

    public TaskStackBuilder addParentStack(ComponentName sourceActivityName) {
        int size = this.mIntents.size();
        try {
            Intent parentActivityIntent = NavUtils.getParentActivityIntent(this.mSourceContext, sourceActivityName);
            while (parentActivityIntent != null) {
                this.mIntents.add(size, parentActivityIntent);
                parentActivityIntent = NavUtils.getParentActivityIntent(this.mSourceContext, parentActivityIntent.getComponent());
            }
            return this;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Bad ComponentName while traversing activity parent metadata");
            throw new IllegalArgumentException(e);
        }
    }

    public TaskStackBuilder addParentStack(Class<?> cls) {
        return addParentStack(new ComponentName(this.mSourceContext, cls));
    }

    public Intent editIntentAt(int index) {
        return this.mIntents.get(index);
    }

    @Deprecated
    public Intent getIntent(int index) {
        return editIntentAt(index);
    }

    public int getIntentCount() {
        return this.mIntents.size();
    }

    public Intent[] getIntents() {
        Intent[] intentArr = new Intent[this.mIntents.size()];
        if (intentArr.length == 0) {
            return intentArr;
        }
        intentArr[0] = new Intent(this.mIntents.get(0)).addFlags(268484608);
        for (int i = 1; i < intentArr.length; i++) {
            intentArr[i] = new Intent(this.mIntents.get(i));
        }
        return intentArr;
    }

    public PendingIntent getPendingIntent(int requestCode, int flags) {
        return getPendingIntent(requestCode, flags, (Bundle) null);
    }

    public PendingIntent getPendingIntent(int requestCode, int flags, Bundle options) {
        if (!this.mIntents.isEmpty()) {
            Intent[] intentArr = (Intent[]) this.mIntents.toArray(new Intent[0]);
            intentArr[0] = new Intent(intentArr[0]).addFlags(268484608);
            return Build.VERSION.SDK_INT >= 16 ? Api16Impl.getActivities(this.mSourceContext, requestCode, intentArr, flags, options) : PendingIntent.getActivities(this.mSourceContext, requestCode, intentArr, flags);
        }
        throw new IllegalStateException("No intents added to TaskStackBuilder; cannot getPendingIntent");
    }

    @Deprecated
    public Iterator<Intent> iterator() {
        return this.mIntents.iterator();
    }

    public void startActivities() {
        startActivities((Bundle) null);
    }

    public void startActivities(Bundle options) {
        if (!this.mIntents.isEmpty()) {
            Intent[] intentArr = (Intent[]) this.mIntents.toArray(new Intent[0]);
            intentArr[0] = new Intent(intentArr[0]).addFlags(268484608);
            if (!ContextCompat.startActivities(this.mSourceContext, intentArr, options)) {
                Intent intent = new Intent(intentArr[intentArr.length - 1]);
                intent.addFlags(268435456);
                this.mSourceContext.startActivity(intent);
                return;
            }
            return;
        }
        throw new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
    }
}

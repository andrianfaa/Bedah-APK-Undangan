package androidx.activity.result;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.core.app.ActivityOptionsCompat;

public abstract class ActivityResultLauncher<I> {
    public abstract ActivityResultContract<I, ?> getContract();

    public void launch(I i) {
        launch(i, (ActivityOptionsCompat) null);
    }

    public abstract void launch(I i, ActivityOptionsCompat activityOptionsCompat);

    public abstract void unregister();
}

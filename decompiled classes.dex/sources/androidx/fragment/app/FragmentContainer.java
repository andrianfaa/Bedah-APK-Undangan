package androidx.fragment.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

public abstract class FragmentContainer {
    @Deprecated
    public Fragment instantiate(Context context, String className, Bundle arguments) {
        return Fragment.instantiate(context, className, arguments);
    }

    public abstract View onFindViewById(int i);

    public abstract boolean onHasView();
}

package androidx.core.content;

import android.content.ContentProvider;
import android.content.Context;

public final class ContentProviderCompat {
    private ContentProviderCompat() {
    }

    public static Context requireContext(ContentProvider provider) {
        Context context = provider.getContext();
        if (context != null) {
            return context;
        }
        throw new IllegalStateException("Cannot find context from the provider.");
    }
}

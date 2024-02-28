package androidx.core.os;

import android.os.Build;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import mt.Log1F380D;

/* compiled from: 0049 */
public final class EnvironmentCompat {
    public static final String MEDIA_UNKNOWN = "unknown";
    private static final String TAG = "EnvironmentCompat";

    /* compiled from: 0047 */
    static class Api19Impl {
        private Api19Impl() {
        }

        static String getStorageState(File path) {
            String storageState = Environment.getStorageState(path);
            Log1F380D.a((Object) storageState);
            return storageState;
        }
    }

    /* compiled from: 0048 */
    static class Api21Impl {
        private Api21Impl() {
        }

        static String getExternalStorageState(File path) {
            String externalStorageState = Environment.getExternalStorageState(path);
            Log1F380D.a((Object) externalStorageState);
            return externalStorageState;
        }
    }

    private EnvironmentCompat() {
    }

    public static String getStorageState(File path) {
        if (Build.VERSION.SDK_INT >= 21) {
            String externalStorageState = Api21Impl.getExternalStorageState(path);
            Log1F380D.a((Object) externalStorageState);
            return externalStorageState;
        } else if (Build.VERSION.SDK_INT >= 19) {
            String storageState = Api19Impl.getStorageState(path);
            Log1F380D.a((Object) storageState);
            return storageState;
        } else {
            try {
                if (!path.getCanonicalPath().startsWith(Environment.getExternalStorageDirectory().getCanonicalPath())) {
                    return MEDIA_UNKNOWN;
                }
                String externalStorageState2 = Environment.getExternalStorageState();
                Log1F380D.a((Object) externalStorageState2);
                return externalStorageState2;
            } catch (IOException e) {
                Log.w(TAG, "Failed to resolve canonical path: " + e);
                return MEDIA_UNKNOWN;
            }
        }
    }
}

package androidx.core.os;

import android.content.Context;
import android.os.Build;
import android.os.UserManager;

public class UserManagerCompat {

    static class Api24Impl {
        private Api24Impl() {
        }

        static boolean isUserUnlocked(Context context) {
            return ((UserManager) context.getSystemService(UserManager.class)).isUserUnlocked();
        }
    }

    private UserManagerCompat() {
    }

    public static boolean isUserUnlocked(Context context) {
        if (Build.VERSION.SDK_INT >= 24) {
            return Api24Impl.isUserUnlocked(context);
        }
        return true;
    }
}

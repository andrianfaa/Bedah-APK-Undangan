package androidx.core.os;

import android.os.Build;
import android.os.Message;

public final class MessageCompat {
    private static boolean sTryIsAsynchronous = true;
    private static boolean sTrySetAsynchronous = true;

    static class Api22Impl {
        private Api22Impl() {
        }

        static boolean isAsynchronous(Message message) {
            return message.isAsynchronous();
        }

        static void setAsynchronous(Message message, boolean async) {
            message.setAsynchronous(async);
        }
    }

    private MessageCompat() {
    }

    public static boolean isAsynchronous(Message message) {
        if (Build.VERSION.SDK_INT >= 22) {
            return Api22Impl.isAsynchronous(message);
        }
        if (sTryIsAsynchronous && Build.VERSION.SDK_INT >= 16) {
            try {
                return Api22Impl.isAsynchronous(message);
            } catch (NoSuchMethodError e) {
                sTryIsAsynchronous = false;
            }
        }
        return false;
    }

    public static void setAsynchronous(Message message, boolean async) {
        if (Build.VERSION.SDK_INT >= 22) {
            Api22Impl.setAsynchronous(message, async);
        } else if (sTrySetAsynchronous && Build.VERSION.SDK_INT >= 16) {
            try {
                Api22Impl.setAsynchronous(message, async);
            } catch (NoSuchMethodError e) {
                sTrySetAsynchronous = false;
            }
        }
    }
}

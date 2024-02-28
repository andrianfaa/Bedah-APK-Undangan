package androidx.core.os;

import android.os.Build;
import android.os.Process;
import android.os.UserHandle;
import java.lang.reflect.Method;

public final class ProcessCompat {

    static class Api16Impl {
        private static Method sMethodUserIdIsAppMethod;
        private static boolean sResolved;
        private static final Object sResolvedLock = new Object();

        private Api16Impl() {
        }

        static boolean isApplicationUid(int uid) {
            try {
                synchronized (sResolvedLock) {
                    if (!sResolved) {
                        sResolved = true;
                        sMethodUserIdIsAppMethod = Class.forName("android.os.UserId").getDeclaredMethod("isApp", new Class[]{Integer.TYPE});
                    }
                }
                Method method = sMethodUserIdIsAppMethod;
                if (method != null) {
                    Boolean bool = (Boolean) method.invoke((Object) null, new Object[]{Integer.valueOf(uid)});
                    if (bool != null) {
                        return bool.booleanValue();
                    }
                    throw new NullPointerException();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    static class Api17Impl {
        private static Method sMethodUserHandleIsAppMethod;
        private static boolean sResolved;
        private static final Object sResolvedLock = new Object();

        private Api17Impl() {
        }

        static boolean isApplicationUid(int uid) {
            try {
                synchronized (sResolvedLock) {
                    if (!sResolved) {
                        sResolved = true;
                        sMethodUserHandleIsAppMethod = UserHandle.class.getDeclaredMethod("isApp", new Class[]{Integer.TYPE});
                    }
                }
                Method method = sMethodUserHandleIsAppMethod;
                if (method != null) {
                    Boolean bool = (Boolean) method.invoke((Object) null, new Object[]{Integer.valueOf(uid)});
                    if (bool != null) {
                        return bool.booleanValue();
                    }
                    throw new NullPointerException();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    static class Api24Impl {
        private Api24Impl() {
        }

        static boolean isApplicationUid(int uid) {
            return Process.isApplicationUid(uid);
        }
    }

    private ProcessCompat() {
    }

    public static boolean isApplicationUid(int uid) {
        if (Build.VERSION.SDK_INT >= 24) {
            return Api24Impl.isApplicationUid(uid);
        }
        if (Build.VERSION.SDK_INT >= 17) {
            return Api17Impl.isApplicationUid(uid);
        }
        if (Build.VERSION.SDK_INT == 16) {
            return Api16Impl.isApplicationUid(uid);
        }
        return true;
    }
}

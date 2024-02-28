package androidx.core.os;

import android.os.Build;
import android.os.UserHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UserHandleCompat {
    private static Method sGetUserIdMethod;
    private static Constructor<UserHandle> sUserHandleConstructor;

    private static class Api24Impl {
        private Api24Impl() {
        }

        static UserHandle getUserHandleForUid(int uid) {
            return UserHandle.getUserHandleForUid(uid);
        }
    }

    private UserHandleCompat() {
    }

    private static Method getGetUserIdMethod() throws NoSuchMethodException {
        if (sGetUserIdMethod == null) {
            Method declaredMethod = UserHandle.class.getDeclaredMethod("getUserId", new Class[]{Integer.TYPE});
            sGetUserIdMethod = declaredMethod;
            declaredMethod.setAccessible(true);
        }
        return sGetUserIdMethod;
    }

    private static Constructor<UserHandle> getUserHandleConstructor() throws NoSuchMethodException {
        if (sUserHandleConstructor == null) {
            Constructor<UserHandle> declaredConstructor = UserHandle.class.getDeclaredConstructor(new Class[]{Integer.TYPE});
            sUserHandleConstructor = declaredConstructor;
            declaredConstructor.setAccessible(true);
        }
        return sUserHandleConstructor;
    }

    public static UserHandle getUserHandleForUid(int uid) {
        if (Build.VERSION.SDK_INT >= 24) {
            return Api24Impl.getUserHandleForUid(uid);
        }
        try {
            Method getUserIdMethod = getGetUserIdMethod();
            Object[] objArr = {Integer.valueOf(uid)};
            return getUserHandleConstructor().newInstance(new Object[]{(Integer) getUserIdMethod.invoke((Object) null, objArr)});
        } catch (NoSuchMethodException e) {
            NoSuchMethodError noSuchMethodError = new NoSuchMethodError();
            noSuchMethodError.initCause(e);
            throw noSuchMethodError;
        } catch (IllegalAccessException e2) {
            IllegalAccessError illegalAccessError = new IllegalAccessError();
            illegalAccessError.initCause(e2);
            throw illegalAccessError;
        } catch (InstantiationException e3) {
            InstantiationError instantiationError = new InstantiationError();
            instantiationError.initCause(e3);
            throw instantiationError;
        } catch (InvocationTargetException e4) {
            throw new RuntimeException(e4);
        }
    }
}

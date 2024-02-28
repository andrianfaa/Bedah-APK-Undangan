package androidx.core.telephony;

import android.os.Build;
import android.telephony.TelephonyManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import mt.Log1F380D;

/* compiled from: 0051 */
public class TelephonyManagerCompat {
    private static Method sGetDeviceIdMethod;
    private static Method sGetSubIdMethod;

    private static class Api23Impl {
        private Api23Impl() {
        }

        static String getDeviceId(TelephonyManager telephonyManager, int slotIndex) {
            return telephonyManager.getDeviceId(slotIndex);
        }
    }

    private static class Api26Impl {
        private Api26Impl() {
        }

        static String getImei(TelephonyManager telephonyManager) {
            return telephonyManager.getImei();
        }
    }

    private static class Api30Impl {
        private Api30Impl() {
        }

        static int getSubscriptionId(TelephonyManager telephonyManager) {
            return telephonyManager.getSubscriptionId();
        }
    }

    private TelephonyManagerCompat() {
    }

    public static String getImei(TelephonyManager telephonyManager) {
        int subscriptionId;
        if (Build.VERSION.SDK_INT >= 26) {
            String imei = Api26Impl.getImei(telephonyManager);
            Log1F380D.a((Object) imei);
            return imei;
        } else if (Build.VERSION.SDK_INT < 22 || (subscriptionId = getSubscriptionId(telephonyManager)) == Integer.MAX_VALUE || subscriptionId == -1) {
            return telephonyManager.getDeviceId();
        } else {
            int slotIndex = SubscriptionManagerCompat.getSlotIndex(subscriptionId);
            if (Build.VERSION.SDK_INT >= 23) {
                String deviceId = Api23Impl.getDeviceId(telephonyManager, slotIndex);
                Log1F380D.a((Object) deviceId);
                return deviceId;
            }
            try {
                if (sGetDeviceIdMethod == null) {
                    Method declaredMethod = TelephonyManager.class.getDeclaredMethod("getDeviceId", new Class[]{Integer.TYPE});
                    sGetDeviceIdMethod = declaredMethod;
                    declaredMethod.setAccessible(true);
                }
                return (String) sGetDeviceIdMethod.invoke(telephonyManager, new Object[]{Integer.valueOf(slotIndex)});
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                return null;
            }
        }
    }

    public static int getSubscriptionId(TelephonyManager telephonyManager) {
        if (Build.VERSION.SDK_INT >= 30) {
            return Api30Impl.getSubscriptionId(telephonyManager);
        }
        if (Build.VERSION.SDK_INT < 22) {
            return Integer.MAX_VALUE;
        }
        try {
            if (sGetSubIdMethod == null) {
                Method declaredMethod = TelephonyManager.class.getDeclaredMethod("getSubId", new Class[0]);
                sGetSubIdMethod = declaredMethod;
                declaredMethod.setAccessible(true);
            }
            Integer num = (Integer) sGetSubIdMethod.invoke(telephonyManager, new Object[0]);
            if (num == null || num.intValue() == -1) {
                return Integer.MAX_VALUE;
            }
            return num.intValue();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return Integer.MAX_VALUE;
        }
    }
}

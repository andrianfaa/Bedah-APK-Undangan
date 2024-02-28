package androidx.work.impl.utils;

import android.content.ComponentName;
import android.content.Context;
import androidx.work.Logger;
import mt.Log1F380D;

/* compiled from: 00D7 */
public class PackageManagerHelper {
    private static final String TAG;

    static {
        String tagWithPrefix = Logger.tagWithPrefix("PackageManagerHelper");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    private PackageManagerHelper() {
    }

    public static boolean isComponentExplicitlyEnabled(Context context, Class<?> cls) {
        return isComponentExplicitlyEnabled(context, cls.getName());
    }

    public static boolean isComponentExplicitlyEnabled(Context context, String className) {
        return context.getPackageManager().getComponentEnabledSetting(new ComponentName(context, className)) == 1;
    }

    public static void setComponentEnabled(Context context, Class<?> cls, boolean enabled) {
        String str = "enabled";
        try {
            context.getPackageManager().setComponentEnabledSetting(new ComponentName(context, cls.getName()), enabled ? 1 : 2, 1);
            Logger logger = Logger.get();
            String str2 = TAG;
            Object[] objArr = new Object[2];
            objArr[0] = cls.getName();
            objArr[1] = enabled ? str : "disabled";
            String format = String.format("%s %s", objArr);
            Log1F380D.a((Object) format);
            logger.debug(str2, format, new Throwable[0]);
        } catch (Exception e) {
            Logger logger2 = Logger.get();
            String str3 = TAG;
            Object[] objArr2 = new Object[2];
            objArr2[0] = cls.getName();
            if (!enabled) {
                str = "disabled";
            }
            objArr2[1] = str;
            String format2 = String.format("%s could not be %s", objArr2);
            Log1F380D.a((Object) format2);
            logger2.debug(str3, format2, e);
        }
    }
}

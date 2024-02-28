package androidx.core.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import mt.Log1F380D;

/* compiled from: 002D */
public final class NavUtils {
    public static final String PARENT_ACTIVITY = "android.support.PARENT_ACTIVITY";
    private static final String TAG = "NavUtils";

    static class Api16Impl {
        private Api16Impl() {
        }

        static Intent getParentActivityIntent(Activity activity) {
            return activity.getParentActivityIntent();
        }

        static boolean navigateUpTo(Activity activity, Intent upIntent) {
            return activity.navigateUpTo(upIntent);
        }

        static boolean shouldUpRecreateTask(Activity activity, Intent targetIntent) {
            return activity.shouldUpRecreateTask(targetIntent);
        }
    }

    private NavUtils() {
    }

    public static Intent getParentActivityIntent(Activity sourceActivity) {
        Intent parentActivityIntent;
        if (Build.VERSION.SDK_INT >= 16 && (parentActivityIntent = Api16Impl.getParentActivityIntent(sourceActivity)) != null) {
            return parentActivityIntent;
        }
        String parentActivityName = getParentActivityName(sourceActivity);
        Log1F380D.a((Object) parentActivityName);
        if (parentActivityName == null) {
            return null;
        }
        ComponentName componentName = new ComponentName(sourceActivity, parentActivityName);
        try {
            String parentActivityName2 = getParentActivityName(sourceActivity, componentName);
            Log1F380D.a((Object) parentActivityName2);
            return parentActivityName2 == null ? Intent.makeMainActivity(componentName) : new Intent().setComponent(componentName);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getParentActivityIntent: bad parentActivityName '" + parentActivityName + "' in manifest");
            return null;
        }
    }

    public static String getParentActivityName(Context context, ComponentName componentName) throws PackageManager.NameNotFoundException {
        String string;
        String str;
        PackageManager packageManager = context.getPackageManager();
        int i = Build.VERSION.SDK_INT >= 24 ? 128 | 512 : 128 | 512;
        if (Build.VERSION.SDK_INT >= 29) {
            i |= 269221888;
        } else if (Build.VERSION.SDK_INT >= 24) {
            i |= 786432;
        }
        ActivityInfo activityInfo = packageManager.getActivityInfo(componentName, i);
        if (Build.VERSION.SDK_INT >= 16 && (str = activityInfo.parentActivityName) != null) {
            return str;
        }
        if (activityInfo.metaData == null || (string = activityInfo.metaData.getString(PARENT_ACTIVITY)) == null) {
            return null;
        }
        return string.charAt(0) == '.' ? context.getPackageName() + string : string;
    }

    public static void navigateUpFromSameTask(Activity sourceActivity) {
        Intent parentActivityIntent = getParentActivityIntent(sourceActivity);
        if (parentActivityIntent != null) {
            navigateUpTo(sourceActivity, parentActivityIntent);
            return;
        }
        throw new IllegalArgumentException("Activity " + sourceActivity.getClass().getSimpleName() + " does not have a parent activity name specified. (Did you forget to add the android.support.PARENT_ACTIVITY <meta-data>  element in your manifest?)");
    }

    public static void navigateUpTo(Activity sourceActivity, Intent upIntent) {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.navigateUpTo(sourceActivity, upIntent);
            return;
        }
        upIntent.addFlags(67108864);
        sourceActivity.startActivity(upIntent);
        sourceActivity.finish();
    }

    public static boolean shouldUpRecreateTask(Activity sourceActivity, Intent targetIntent) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Api16Impl.shouldUpRecreateTask(sourceActivity, targetIntent);
        }
        String action = sourceActivity.getIntent().getAction();
        return action != null && !action.equals("android.intent.action.MAIN");
    }

    public static Intent getParentActivityIntent(Context context, ComponentName componentName) throws PackageManager.NameNotFoundException {
        String parentActivityName = getParentActivityName(context, componentName);
        Log1F380D.a((Object) parentActivityName);
        if (parentActivityName == null) {
            return null;
        }
        ComponentName componentName2 = new ComponentName(componentName.getPackageName(), parentActivityName);
        String parentActivityName2 = getParentActivityName(context, componentName2);
        Log1F380D.a((Object) parentActivityName2);
        return parentActivityName2 == null ? Intent.makeMainActivity(componentName2) : new Intent().setComponent(componentName2);
    }

    public static Intent getParentActivityIntent(Context context, Class<?> cls) throws PackageManager.NameNotFoundException {
        String parentActivityName = getParentActivityName(context, new ComponentName(context, cls));
        Log1F380D.a((Object) parentActivityName);
        if (parentActivityName == null) {
            return null;
        }
        ComponentName componentName = new ComponentName(context, parentActivityName);
        String parentActivityName2 = getParentActivityName(context, componentName);
        Log1F380D.a((Object) parentActivityName2);
        return parentActivityName2 == null ? Intent.makeMainActivity(componentName) : new Intent().setComponent(componentName);
    }

    public static String getParentActivityName(Activity sourceActivity) {
        try {
            String parentActivityName = getParentActivityName(sourceActivity, sourceActivity.getComponentName());
            Log1F380D.a((Object) parentActivityName);
            return parentActivityName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

package androidx.core.content;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.concurrent.futures.ResolvableFuture;
import androidx.core.os.UserManagerCompat;
import com.google.common.util.concurrent.ListenableFuture;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;
import java.util.concurrent.Executors;
import mt.Log1F380D;

/* compiled from: 0039 */
public final class PackageManagerCompat {
    public static final String ACTION_PERMISSION_REVOCATION_SETTINGS = "android.intent.action.AUTO_REVOKE_PERMISSIONS";
    public static final String LOG_TAG = "PackageManagerCompat";

    private static class Api30Impl {
        private Api30Impl() {
        }

        static boolean areUnusedAppRestrictionsEnabled(Context context) {
            return !context.getPackageManager().isAutoRevokeWhitelisted();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface UnusedAppRestrictionsStatus {
    }

    private PackageManagerCompat() {
    }

    public static boolean areUnusedAppRestrictionsAvailable(PackageManager packageManager) {
        boolean z = Build.VERSION.SDK_INT >= 30;
        boolean z2 = Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT < 30;
        String permissionRevocationVerifierApp = getPermissionRevocationVerifierApp(packageManager);
        Log1F380D.a((Object) permissionRevocationVerifierApp);
        boolean z3 = permissionRevocationVerifierApp != null;
        if (!z) {
            return z2 && z3;
        }
        return true;
    }

    public static String getPermissionRevocationVerifierApp(PackageManager packageManager) {
        String str = null;
        for (ResolveInfo resolveInfo : packageManager.queryIntentActivities(new Intent(ACTION_PERMISSION_REVOCATION_SETTINGS).setData(Uri.fromParts("package", "com.example", (String) null)), 0)) {
            String str2 = resolveInfo.activityInfo.packageName;
            if (packageManager.checkPermission("android.permission.PACKAGE_VERIFICATION_AGENT", str2) == 0) {
                if (str != null) {
                    return str;
                }
                str = str2;
            }
        }
        return str;
    }

    public static ListenableFuture<Integer> getUnusedAppRestrictionsStatus(Context context) {
        ResolvableFuture create = ResolvableFuture.create();
        if (!UserManagerCompat.isUserUnlocked(context)) {
            create.set(0);
            Log.e(LOG_TAG, "User is in locked direct boot mode");
            return create;
        } else if (!areUnusedAppRestrictionsAvailable(context.getPackageManager())) {
            create.set(1);
            return create;
        } else {
            int i = context.getApplicationInfo().targetSdkVersion;
            if (i < 30) {
                create.set(0);
                Log.e(LOG_TAG, "Target SDK version below API 30");
                return create;
            }
            int i2 = 4;
            if (Build.VERSION.SDK_INT >= 31) {
                if (Api30Impl.areUnusedAppRestrictionsEnabled(context)) {
                    if (i >= 31) {
                        i2 = 5;
                    }
                    create.set(Integer.valueOf(i2));
                } else {
                    create.set(2);
                }
                return create;
            } else if (Build.VERSION.SDK_INT == 30) {
                if (!Api30Impl.areUnusedAppRestrictionsEnabled(context)) {
                    i2 = 2;
                }
                create.set(Integer.valueOf(i2));
                return create;
            } else {
                UnusedAppRestrictionsBackportServiceConnection unusedAppRestrictionsBackportServiceConnection = new UnusedAppRestrictionsBackportServiceConnection(context);
                Objects.requireNonNull(unusedAppRestrictionsBackportServiceConnection);
                create.addListener(new PackageManagerCompat$$ExternalSyntheticLambda0(unusedAppRestrictionsBackportServiceConnection), Executors.newSingleThreadExecutor());
                unusedAppRestrictionsBackportServiceConnection.connectAndFetchResult(create);
                return create;
            }
        }
    }
}

package androidx.core.app;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.Display;
import android.view.DragEvent;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.content.LocusIdCompat;
import androidx.core.view.DragAndDropPermissionsCompat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import mt.Log1F380D;

/* compiled from: 002A */
public class ActivityCompat extends ContextCompat {
    private static PermissionCompatDelegate sDelegate;

    static class Api16Impl {
        private Api16Impl() {
        }

        static void finishAffinity(Activity activity) {
            activity.finishAffinity();
        }

        static void startActivityForResult(Activity activity, Intent intent, int requestCode, Bundle options) {
            activity.startActivityForResult(intent, requestCode, options);
        }

        static void startIntentSenderForResult(Activity activity, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
            activity.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
        }
    }

    static class Api21Impl {
        private Api21Impl() {
        }

        static void finishAfterTransition(Activity activity) {
            activity.finishAfterTransition();
        }

        static void postponeEnterTransition(Activity activity) {
            activity.postponeEnterTransition();
        }

        static void setEnterSharedElementCallback(Activity activity, SharedElementCallback callback) {
            activity.setEnterSharedElementCallback(callback);
        }

        static void setExitSharedElementCallback(Activity activity, SharedElementCallback callback) {
            activity.setExitSharedElementCallback(callback);
        }

        static void startPostponedEnterTransition(Activity activity) {
            activity.startPostponedEnterTransition();
        }
    }

    static class Api22Impl {
        private Api22Impl() {
        }

        static Uri getReferrer(Activity activity) {
            return activity.getReferrer();
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        /* access modifiers changed from: package-private */
        public static void onSharedElementsReady(Object onSharedElementsReadyListener) {
            ((SharedElementCallback.OnSharedElementsReadyListener) onSharedElementsReadyListener).onSharedElementsReady();
        }

        static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
            activity.requestPermissions(permissions, requestCode);
        }

        static boolean shouldShowRequestPermissionRationale(Activity activity, String permission) {
            return activity.shouldShowRequestPermissionRationale(permission);
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static <T> T requireViewById(Activity activity, int id) {
            return activity.requireViewById(id);
        }
    }

    static class Api30Impl {
        private Api30Impl() {
        }

        static Display getDisplay(ContextWrapper contextWrapper) {
            return contextWrapper.getDisplay();
        }

        static void setLocusContext(Activity activity, LocusIdCompat locusId, Bundle bundle) {
            activity.setLocusContext(locusId == null ? null : locusId.toLocusId(), bundle);
        }
    }

    static class Api31Impl {
        private Api31Impl() {
        }

        static boolean isLaunchedFromBubble(Activity activity) {
            return activity.isLaunchedFromBubble();
        }
    }

    public interface OnRequestPermissionsResultCallback {
        void onRequestPermissionsResult(int i, String[] strArr, int[] iArr);
    }

    public interface PermissionCompatDelegate {
        boolean onActivityResult(Activity activity, int i, int i2, Intent intent);

        boolean requestPermissions(Activity activity, String[] strArr, int i);
    }

    public interface RequestPermissionsRequestCodeValidator {
        void validateRequestPermissionsRequestCode(int i);
    }

    static class SharedElementCallback21Impl extends SharedElementCallback {
        private final SharedElementCallback mCallback;

        SharedElementCallback21Impl(SharedElementCallback callback) {
            this.mCallback = callback;
        }

        public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
            return this.mCallback.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
        }

        public View onCreateSnapshotView(Context context, Parcelable snapshot) {
            return this.mCallback.onCreateSnapshotView(context, snapshot);
        }

        public void onMapSharedElements(List<String> list, Map<String, View> map) {
            this.mCallback.onMapSharedElements(list, map);
        }

        public void onRejectSharedElements(List<View> list) {
            this.mCallback.onRejectSharedElements(list);
        }

        public void onSharedElementEnd(List<String> list, List<View> list2, List<View> list3) {
            this.mCallback.onSharedElementEnd(list, list2, list3);
        }

        public void onSharedElementStart(List<String> list, List<View> list2, List<View> list3) {
            this.mCallback.onSharedElementStart(list, list2, list3);
        }

        public void onSharedElementsArrived(List<String> list, List<View> list2, SharedElementCallback.OnSharedElementsReadyListener listener) {
            this.mCallback.onSharedElementsArrived(list, list2, new ActivityCompat$SharedElementCallback21Impl$$ExternalSyntheticLambda0(listener));
        }
    }

    protected ActivityCompat() {
    }

    public static void finishAffinity(Activity activity) {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.finishAffinity(activity);
        } else {
            activity.finish();
        }
    }

    public static void finishAfterTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.finishAfterTransition(activity);
        } else {
            activity.finish();
        }
    }

    public static PermissionCompatDelegate getPermissionCompatDelegate() {
        return sDelegate;
    }

    public static Uri getReferrer(Activity activity) {
        if (Build.VERSION.SDK_INT >= 22) {
            return Api22Impl.getReferrer(activity);
        }
        Intent intent = activity.getIntent();
        Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.REFERRER");
        if (uri != null) {
            return uri;
        }
        String stringExtra = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
        if (stringExtra != null) {
            return Uri.parse(stringExtra);
        }
        return null;
    }

    @Deprecated
    public static boolean invalidateOptionsMenu(Activity activity) {
        activity.invalidateOptionsMenu();
        return true;
    }

    public static boolean isLaunchedFromBubble(Activity activity) {
        if (Build.VERSION.SDK_INT >= 31) {
            return Api31Impl.isLaunchedFromBubble(activity);
        }
        if (Build.VERSION.SDK_INT == 30) {
            return (Api30Impl.getDisplay(activity) == null || Api30Impl.getDisplay(activity).getDisplayId() == 0) ? false : true;
        }
        if (Build.VERSION.SDK_INT == 29) {
            return (activity.getWindowManager().getDefaultDisplay() == null || activity.getWindowManager().getDefaultDisplay().getDisplayId() == 0) ? false : true;
        }
        return false;
    }

    static /* synthetic */ void lambda$recreate$0(Activity activity) {
        if (!activity.isFinishing() && !ActivityRecreator.recreate(activity)) {
            activity.recreate();
        }
    }

    public static void postponeEnterTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.postponeEnterTransition(activity);
        }
    }

    public static void recreate(Activity activity) {
        if (Build.VERSION.SDK_INT >= 28) {
            activity.recreate();
        } else {
            new Handler(activity.getMainLooper()).post(new ActivityCompat$$ExternalSyntheticLambda0(activity));
        }
    }

    public static DragAndDropPermissionsCompat requestDragAndDropPermissions(Activity activity, DragEvent dragEvent) {
        return DragAndDropPermissionsCompat.request(activity, dragEvent);
    }

    public static void requestPermissions(final Activity activity, final String[] permissions, final int requestCode) {
        PermissionCompatDelegate permissionCompatDelegate = sDelegate;
        if (permissionCompatDelegate == null || !permissionCompatDelegate.requestPermissions(activity, permissions, requestCode)) {
            int length = permissions.length;
            int i = 0;
            while (i < length) {
                if (!TextUtils.isEmpty(permissions[i])) {
                    i++;
                } else {
                    StringBuilder append = new StringBuilder().append("Permission request for permissions ");
                    String arrays = Arrays.toString(permissions);
                    Log1F380D.a((Object) arrays);
                    throw new IllegalArgumentException(append.append(arrays).append(" must not contain null or empty values").toString());
                }
            }
            if (Build.VERSION.SDK_INT >= 23) {
                if (activity instanceof RequestPermissionsRequestCodeValidator) {
                    ((RequestPermissionsRequestCodeValidator) activity).validateRequestPermissionsRequestCode(requestCode);
                }
                Api23Impl.requestPermissions(activity, permissions, requestCode);
            } else if (activity instanceof OnRequestPermissionsResultCallback) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        int[] iArr = new int[permissions.length];
                        PackageManager packageManager = activity.getPackageManager();
                        String packageName = activity.getPackageName();
                        int length = permissions.length;
                        for (int i = 0; i < length; i++) {
                            iArr[i] = packageManager.checkPermission(permissions[i], packageName);
                        }
                        ((OnRequestPermissionsResultCallback) activity).onRequestPermissionsResult(requestCode, permissions, iArr);
                    }
                });
            }
        }
    }

    public static <T extends View> T requireViewById(Activity activity, int id) {
        if (Build.VERSION.SDK_INT >= 28) {
            return (View) Api28Impl.requireViewById(activity, id);
        }
        T findViewById = activity.findViewById(id);
        if (findViewById != null) {
            return findViewById;
        }
        throw new IllegalArgumentException("ID does not reference a View inside this Activity");
    }

    public static void setEnterSharedElementCallback(Activity activity, SharedElementCallback callback) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setEnterSharedElementCallback(activity, callback != null ? new SharedElementCallback21Impl(callback) : null);
        }
    }

    public static void setExitSharedElementCallback(Activity activity, SharedElementCallback callback) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.setExitSharedElementCallback(activity, callback != null ? new SharedElementCallback21Impl(callback) : null);
        }
    }

    public static void setLocusContext(Activity activity, LocusIdCompat locusId, Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 30) {
            Api30Impl.setLocusContext(activity, locusId, bundle);
        }
    }

    public static void setPermissionCompatDelegate(PermissionCompatDelegate delegate) {
        sDelegate = delegate;
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Api23Impl.shouldShowRequestPermissionRationale(activity, permission);
        }
        return false;
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode, Bundle options) {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.startActivityForResult(activity, intent, requestCode, options);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static void startIntentSenderForResult(Activity activity, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        if (Build.VERSION.SDK_INT >= 16) {
            Api16Impl.startIntentSenderForResult(activity, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
        } else {
            activity.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags);
        }
    }

    public static void startPostponedEnterTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            Api21Impl.startPostponedEnterTransition(activity);
        }
    }
}

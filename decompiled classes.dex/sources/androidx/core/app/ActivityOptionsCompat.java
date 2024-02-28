package androidx.core.app;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

public class ActivityOptionsCompat {
    public static final String EXTRA_USAGE_TIME_REPORT = "android.activity.usage_time";
    public static final String EXTRA_USAGE_TIME_REPORT_PACKAGES = "android.usage_time_packages";

    private static class ActivityOptionsCompatImpl extends ActivityOptionsCompat {
        private final ActivityOptions mActivityOptions;

        ActivityOptionsCompatImpl(ActivityOptions activityOptions) {
            this.mActivityOptions = activityOptions;
        }

        public Rect getLaunchBounds() {
            if (Build.VERSION.SDK_INT < 24) {
                return null;
            }
            return Api24Impl.getLaunchBounds(this.mActivityOptions);
        }

        public void requestUsageTimeReport(PendingIntent receiver) {
            if (Build.VERSION.SDK_INT >= 23) {
                Api23Impl.requestUsageTimeReport(this.mActivityOptions, receiver);
            }
        }

        public ActivityOptionsCompat setLaunchBounds(Rect screenSpacePixelRect) {
            return Build.VERSION.SDK_INT < 24 ? this : new ActivityOptionsCompatImpl(Api24Impl.setLaunchBounds(this.mActivityOptions, screenSpacePixelRect));
        }

        public Bundle toBundle() {
            return this.mActivityOptions.toBundle();
        }

        public void update(ActivityOptionsCompat otherOptions) {
            if (otherOptions instanceof ActivityOptionsCompatImpl) {
                this.mActivityOptions.update(((ActivityOptionsCompatImpl) otherOptions).mActivityOptions);
            }
        }
    }

    static class Api16Impl {
        private Api16Impl() {
        }

        static ActivityOptions makeCustomAnimation(Context context, int enterResId, int exitResId) {
            return ActivityOptions.makeCustomAnimation(context, enterResId, exitResId);
        }

        static ActivityOptions makeScaleUpAnimation(View source, int startX, int startY, int width, int height) {
            return ActivityOptions.makeScaleUpAnimation(source, startX, startY, width, height);
        }

        static ActivityOptions makeThumbnailScaleUpAnimation(View source, Bitmap thumbnail, int startX, int startY) {
            return ActivityOptions.makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY);
        }
    }

    static class Api21Impl {
        private Api21Impl() {
        }

        static ActivityOptions makeSceneTransitionAnimation(Activity activity, View sharedElement, String sharedElementName) {
            return ActivityOptions.makeSceneTransitionAnimation(activity, sharedElement, sharedElementName);
        }

        @SafeVarargs
        static ActivityOptions makeSceneTransitionAnimation(Activity activity, Pair<View, String>... pairArr) {
            return ActivityOptions.makeSceneTransitionAnimation(activity, pairArr);
        }

        static ActivityOptions makeTaskLaunchBehind() {
            return ActivityOptions.makeTaskLaunchBehind();
        }
    }

    static class Api23Impl {
        private Api23Impl() {
        }

        static ActivityOptions makeBasic() {
            return ActivityOptions.makeBasic();
        }

        static ActivityOptions makeClipRevealAnimation(View source, int startX, int startY, int width, int height) {
            return ActivityOptions.makeClipRevealAnimation(source, startX, startY, width, height);
        }

        static void requestUsageTimeReport(ActivityOptions activityOptions, PendingIntent receiver) {
            activityOptions.requestUsageTimeReport(receiver);
        }
    }

    static class Api24Impl {
        private Api24Impl() {
        }

        static Rect getLaunchBounds(ActivityOptions activityOptions) {
            return activityOptions.getLaunchBounds();
        }

        static ActivityOptions setLaunchBounds(ActivityOptions activityOptions, Rect screenSpacePixelRect) {
            return activityOptions.setLaunchBounds(screenSpacePixelRect);
        }
    }

    protected ActivityOptionsCompat() {
    }

    public static ActivityOptionsCompat makeBasic() {
        return Build.VERSION.SDK_INT >= 23 ? new ActivityOptionsCompatImpl(Api23Impl.makeBasic()) : new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeClipRevealAnimation(View source, int startX, int startY, int width, int height) {
        return Build.VERSION.SDK_INT >= 23 ? new ActivityOptionsCompatImpl(Api23Impl.makeClipRevealAnimation(source, startX, startY, width, height)) : new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeCustomAnimation(Context context, int enterResId, int exitResId) {
        return Build.VERSION.SDK_INT >= 16 ? new ActivityOptionsCompatImpl(Api16Impl.makeCustomAnimation(context, enterResId, exitResId)) : new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeScaleUpAnimation(View source, int startX, int startY, int startWidth, int startHeight) {
        return Build.VERSION.SDK_INT >= 16 ? new ActivityOptionsCompatImpl(Api16Impl.makeScaleUpAnimation(source, startX, startY, startWidth, startHeight)) : new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity activity, View sharedElement, String sharedElementName) {
        return Build.VERSION.SDK_INT >= 21 ? new ActivityOptionsCompatImpl(Api21Impl.makeSceneTransitionAnimation(activity, sharedElement, sharedElementName)) : new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity activity, androidx.core.util.Pair<View, String>... pairArr) {
        if (Build.VERSION.SDK_INT < 21) {
            return new ActivityOptionsCompat();
        }
        Pair[] pairArr2 = null;
        if (pairArr != null) {
            pairArr2 = new Pair[pairArr.length];
            for (int i = 0; i < pairArr.length; i++) {
                pairArr2[i] = Pair.create((View) pairArr[i].first, (String) pairArr[i].second);
            }
        }
        return new ActivityOptionsCompatImpl(Api21Impl.makeSceneTransitionAnimation(activity, pairArr2));
    }

    public static ActivityOptionsCompat makeTaskLaunchBehind() {
        return Build.VERSION.SDK_INT >= 21 ? new ActivityOptionsCompatImpl(Api21Impl.makeTaskLaunchBehind()) : new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(View source, Bitmap thumbnail, int startX, int startY) {
        return Build.VERSION.SDK_INT >= 16 ? new ActivityOptionsCompatImpl(Api16Impl.makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY)) : new ActivityOptionsCompat();
    }

    public Rect getLaunchBounds() {
        return null;
    }

    public void requestUsageTimeReport(PendingIntent receiver) {
    }

    public ActivityOptionsCompat setLaunchBounds(Rect screenSpacePixelRect) {
        return this;
    }

    public Bundle toBundle() {
        return null;
    }

    public void update(ActivityOptionsCompat otherOptions) {
    }
}

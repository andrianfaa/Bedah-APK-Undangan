package androidx.core.view.animation;

import android.graphics.Path;
import android.os.Build;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

public final class PathInterpolatorCompat {

    static class Api21Impl {
        private Api21Impl() {
        }

        static PathInterpolator createPathInterpolator(float controlX, float controlY) {
            return new PathInterpolator(controlX, controlY);
        }

        static PathInterpolator createPathInterpolator(float controlX1, float controlY1, float controlX2, float controlY2) {
            return new PathInterpolator(controlX1, controlY1, controlX2, controlY2);
        }

        static PathInterpolator createPathInterpolator(Path path) {
            return new PathInterpolator(path);
        }
    }

    private PathInterpolatorCompat() {
    }

    public static Interpolator create(float controlX, float controlY) {
        return Build.VERSION.SDK_INT >= 21 ? Api21Impl.createPathInterpolator(controlX, controlY) : new PathInterpolatorApi14(controlX, controlY);
    }

    public static Interpolator create(float controlX1, float controlY1, float controlX2, float controlY2) {
        return Build.VERSION.SDK_INT >= 21 ? Api21Impl.createPathInterpolator(controlX1, controlY1, controlX2, controlY2) : new PathInterpolatorApi14(controlX1, controlY1, controlX2, controlY2);
    }

    public static Interpolator create(Path path) {
        return Build.VERSION.SDK_INT >= 21 ? Api21Impl.createPathInterpolator(path) : new PathInterpolatorApi14(path);
    }
}

package androidx.transition;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.util.Property;
import android.view.View;
import androidx.core.view.ViewCompat;

class ViewUtils {
    static final Property<View, Rect> CLIP_BOUNDS = new Property<View, Rect>(Rect.class, "clipBounds") {
        public Rect get(View view) {
            return ViewCompat.getClipBounds(view);
        }

        public void set(View view, Rect clipBounds) {
            ViewCompat.setClipBounds(view, clipBounds);
        }
    };
    private static final ViewUtilsBase IMPL;
    private static final String TAG = "ViewUtils";
    static final Property<View, Float> TRANSITION_ALPHA = new Property<View, Float>(Float.class, "translationAlpha") {
        public Float get(View view) {
            return Float.valueOf(ViewUtils.getTransitionAlpha(view));
        }

        public void set(View view, Float alpha) {
            ViewUtils.setTransitionAlpha(view, alpha.floatValue());
        }
    };

    static {
        if (Build.VERSION.SDK_INT >= 29) {
            IMPL = new ViewUtilsApi29();
        } else if (Build.VERSION.SDK_INT >= 23) {
            IMPL = new ViewUtilsApi23();
        } else if (Build.VERSION.SDK_INT >= 22) {
            IMPL = new ViewUtilsApi22();
        } else if (Build.VERSION.SDK_INT >= 21) {
            IMPL = new ViewUtilsApi21();
        } else if (Build.VERSION.SDK_INT >= 19) {
            IMPL = new ViewUtilsApi19();
        } else {
            IMPL = new ViewUtilsBase();
        }
    }

    private ViewUtils() {
    }

    static void clearNonTransitionAlpha(View view) {
        IMPL.clearNonTransitionAlpha(view);
    }

    static ViewOverlayImpl getOverlay(View view) {
        return Build.VERSION.SDK_INT >= 18 ? new ViewOverlayApi18(view) : ViewOverlayApi14.createFrom(view);
    }

    static float getTransitionAlpha(View view) {
        return IMPL.getTransitionAlpha(view);
    }

    static WindowIdImpl getWindowId(View view) {
        return Build.VERSION.SDK_INT >= 18 ? new WindowIdApi18(view) : new WindowIdApi14(view.getWindowToken());
    }

    static void saveNonTransitionAlpha(View view) {
        IMPL.saveNonTransitionAlpha(view);
    }

    static void setAnimationMatrix(View v, Matrix m) {
        IMPL.setAnimationMatrix(v, m);
    }

    static void setLeftTopRightBottom(View v, int left, int top, int right, int bottom) {
        IMPL.setLeftTopRightBottom(v, left, top, right, bottom);
    }

    static void setTransitionAlpha(View view, float alpha) {
        IMPL.setTransitionAlpha(view, alpha);
    }

    static void setTransitionVisibility(View view, int visibility) {
        IMPL.setTransitionVisibility(view, visibility);
    }

    static void transformMatrixToGlobal(View view, Matrix matrix) {
        IMPL.transformMatrixToGlobal(view, matrix);
    }

    static void transformMatrixToLocal(View view, Matrix matrix) {
        IMPL.transformMatrixToLocal(view, matrix);
    }
}

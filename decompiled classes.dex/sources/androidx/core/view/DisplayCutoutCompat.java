package androidx.core.view;

import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Build;
import android.view.DisplayCutout;
import androidx.core.util.ObjectsCompat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DisplayCutoutCompat {
    private final DisplayCutout mDisplayCutout;

    static class Api28Impl {
        private Api28Impl() {
        }

        static DisplayCutout createDisplayCutout(Rect safeInsets, List<Rect> list) {
            return new DisplayCutout(safeInsets, list);
        }

        static List<Rect> getBoundingRects(DisplayCutout displayCutout) {
            return displayCutout.getBoundingRects();
        }

        static int getSafeInsetBottom(DisplayCutout displayCutout) {
            return displayCutout.getSafeInsetBottom();
        }

        static int getSafeInsetLeft(DisplayCutout displayCutout) {
            return displayCutout.getSafeInsetLeft();
        }

        static int getSafeInsetRight(DisplayCutout displayCutout) {
            return displayCutout.getSafeInsetRight();
        }

        static int getSafeInsetTop(DisplayCutout displayCutout) {
            return displayCutout.getSafeInsetTop();
        }
    }

    static class Api29Impl {
        private Api29Impl() {
        }

        static DisplayCutout createDisplayCutout(Insets safeInsets, Rect boundLeft, Rect boundTop, Rect boundRight, Rect boundBottom) {
            return new DisplayCutout(safeInsets, boundLeft, boundTop, boundRight, boundBottom);
        }
    }

    static class Api30Impl {
        private Api30Impl() {
        }

        static DisplayCutout createDisplayCutout(Insets safeInsets, Rect boundLeft, Rect boundTop, Rect boundRight, Rect boundBottom, Insets waterfallInsets) {
            return new DisplayCutout(safeInsets, boundLeft, boundTop, boundRight, boundBottom, waterfallInsets);
        }

        static Insets getWaterfallInsets(DisplayCutout displayCutout) {
            return displayCutout.getWaterfallInsets();
        }
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public DisplayCutoutCompat(Rect safeInsets, List<Rect> list) {
        this(Build.VERSION.SDK_INT >= 28 ? Api28Impl.createDisplayCutout(safeInsets, list) : null);
    }

    private DisplayCutoutCompat(DisplayCutout displayCutout) {
        this.mDisplayCutout = displayCutout;
    }

    public DisplayCutoutCompat(androidx.core.graphics.Insets safeInsets, Rect boundLeft, Rect boundTop, Rect boundRight, Rect boundBottom, androidx.core.graphics.Insets waterfallInsets) {
        this(constructDisplayCutout(safeInsets, boundLeft, boundTop, boundRight, boundBottom, waterfallInsets));
    }

    private static DisplayCutout constructDisplayCutout(androidx.core.graphics.Insets safeInsets, Rect boundLeft, Rect boundTop, Rect boundRight, Rect boundBottom, androidx.core.graphics.Insets waterfallInsets) {
        if (Build.VERSION.SDK_INT >= 30) {
            return Api30Impl.createDisplayCutout(safeInsets.toPlatformInsets(), boundLeft, boundTop, boundRight, boundBottom, waterfallInsets.toPlatformInsets());
        } else if (Build.VERSION.SDK_INT >= 29) {
            return Api29Impl.createDisplayCutout(safeInsets.toPlatformInsets(), boundLeft, boundTop, boundRight, boundBottom);
        } else {
            if (Build.VERSION.SDK_INT < 28) {
                return null;
            }
            Rect rect = new Rect(safeInsets.left, safeInsets.top, safeInsets.right, safeInsets.bottom);
            ArrayList arrayList = new ArrayList();
            if (boundLeft != null) {
                arrayList.add(boundLeft);
            }
            if (boundTop != null) {
                arrayList.add(boundTop);
            }
            if (boundRight != null) {
                arrayList.add(boundRight);
            }
            if (boundBottom != null) {
                arrayList.add(boundBottom);
            }
            return Api28Impl.createDisplayCutout(rect, arrayList);
        }
    }

    static DisplayCutoutCompat wrap(DisplayCutout displayCutout) {
        if (displayCutout == null) {
            return null;
        }
        return new DisplayCutoutCompat(displayCutout);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return ObjectsCompat.equals(this.mDisplayCutout, ((DisplayCutoutCompat) o).mDisplayCutout);
    }

    public List<Rect> getBoundingRects() {
        return Build.VERSION.SDK_INT >= 28 ? Api28Impl.getBoundingRects(this.mDisplayCutout) : Collections.emptyList();
    }

    public int getSafeInsetBottom() {
        if (Build.VERSION.SDK_INT >= 28) {
            return Api28Impl.getSafeInsetBottom(this.mDisplayCutout);
        }
        return 0;
    }

    public int getSafeInsetLeft() {
        if (Build.VERSION.SDK_INT >= 28) {
            return Api28Impl.getSafeInsetLeft(this.mDisplayCutout);
        }
        return 0;
    }

    public int getSafeInsetRight() {
        if (Build.VERSION.SDK_INT >= 28) {
            return Api28Impl.getSafeInsetRight(this.mDisplayCutout);
        }
        return 0;
    }

    public int getSafeInsetTop() {
        if (Build.VERSION.SDK_INT >= 28) {
            return Api28Impl.getSafeInsetTop(this.mDisplayCutout);
        }
        return 0;
    }

    public androidx.core.graphics.Insets getWaterfallInsets() {
        return Build.VERSION.SDK_INT >= 30 ? androidx.core.graphics.Insets.toCompatInsets(Api30Impl.getWaterfallInsets(this.mDisplayCutout)) : androidx.core.graphics.Insets.NONE;
    }

    public int hashCode() {
        DisplayCutout displayCutout = this.mDisplayCutout;
        if (displayCutout == null) {
            return 0;
        }
        return displayCutout.hashCode();
    }

    public String toString() {
        return "DisplayCutoutCompat{" + this.mDisplayCutout + "}";
    }

    /* access modifiers changed from: package-private */
    public DisplayCutout unwrap() {
        return this.mDisplayCutout;
    }
}

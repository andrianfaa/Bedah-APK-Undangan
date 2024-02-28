package androidx.transition;

import android.graphics.Matrix;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

class GhostViewUtils {
    private GhostViewUtils() {
    }

    static GhostView addGhost(View view, ViewGroup viewGroup, Matrix matrix) {
        return Build.VERSION.SDK_INT == 28 ? GhostViewPlatform.addGhost(view, viewGroup, matrix) : GhostViewPort.addGhost(view, viewGroup, matrix);
    }

    static void removeGhost(View view) {
        if (Build.VERSION.SDK_INT == 28) {
            GhostViewPlatform.removeGhost(view);
        } else {
            GhostViewPort.removeGhost(view);
        }
    }
}

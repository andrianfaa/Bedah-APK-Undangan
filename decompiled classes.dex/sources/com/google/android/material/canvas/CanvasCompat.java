package com.google.android.material.canvas;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Build;

public class CanvasCompat {
    private CanvasCompat() {
    }

    public static int saveLayerAlpha(Canvas canvas, float left, float top, float right, float bottom, int alpha) {
        return Build.VERSION.SDK_INT > 21 ? canvas.saveLayerAlpha(left, top, right, bottom, alpha) : canvas.saveLayerAlpha(left, top, right, bottom, alpha, 31);
    }

    public static int saveLayerAlpha(Canvas canvas, RectF bounds, int alpha) {
        return Build.VERSION.SDK_INT > 21 ? canvas.saveLayerAlpha(bounds, alpha) : canvas.saveLayerAlpha(bounds, alpha, 31);
    }
}

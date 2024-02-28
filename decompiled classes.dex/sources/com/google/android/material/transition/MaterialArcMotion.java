package com.google.android.material.transition;

import android.graphics.Path;
import android.graphics.PointF;
import androidx.transition.PathMotion;

public final class MaterialArcMotion extends PathMotion {
    private static PointF getControlPoint(float startX, float startY, float endX, float endY) {
        return startY > endY ? new PointF(endX, startY) : new PointF(startX, endY);
    }

    public Path getPath(float startX, float startY, float endX, float endY) {
        Path path = new Path();
        path.moveTo(startX, startY);
        PointF controlPoint = getControlPoint(startX, startY, endX, endY);
        path.quadTo(controlPoint.x, controlPoint.y, endX, endY);
        return path;
    }
}

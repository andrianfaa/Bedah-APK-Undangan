package com.google.android.material.shape;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;

public class ShapeAppearancePathProvider {
    private final Path boundsPath = new Path();
    private final Path cornerPath = new Path();
    private final ShapePath[] cornerPaths = new ShapePath[4];
    private final Matrix[] cornerTransforms = new Matrix[4];
    private boolean edgeIntersectionCheckEnabled = true;
    private final Path edgePath = new Path();
    private final Matrix[] edgeTransforms = new Matrix[4];
    private final Path overlappedEdgePath = new Path();
    private final PointF pointF = new PointF();
    private final float[] scratch = new float[2];
    private final float[] scratch2 = new float[2];
    private final ShapePath shapePath = new ShapePath();

    private static class Lazy {
        static final ShapeAppearancePathProvider INSTANCE = new ShapeAppearancePathProvider();

        private Lazy() {
        }
    }

    public interface PathListener {
        void onCornerPathCreated(ShapePath shapePath, Matrix matrix, int i);

        void onEdgePathCreated(ShapePath shapePath, Matrix matrix, int i);
    }

    static final class ShapeAppearancePathSpec {
        public final RectF bounds;
        public final float interpolation;
        public final Path path;
        public final PathListener pathListener;
        public final ShapeAppearanceModel shapeAppearanceModel;

        ShapeAppearancePathSpec(ShapeAppearanceModel shapeAppearanceModel2, float interpolation2, RectF bounds2, PathListener pathListener2, Path path2) {
            this.pathListener = pathListener2;
            this.shapeAppearanceModel = shapeAppearanceModel2;
            this.interpolation = interpolation2;
            this.bounds = bounds2;
            this.path = path2;
        }
    }

    public ShapeAppearancePathProvider() {
        for (int i = 0; i < 4; i++) {
            this.cornerPaths[i] = new ShapePath();
            this.cornerTransforms[i] = new Matrix();
            this.edgeTransforms[i] = new Matrix();
        }
    }

    private float angleOfEdge(int index) {
        return (float) ((index + 1) * 90);
    }

    private void appendCornerPath(ShapeAppearancePathSpec spec, int index) {
        this.scratch[0] = this.cornerPaths[index].getStartX();
        this.scratch[1] = this.cornerPaths[index].getStartY();
        this.cornerTransforms[index].mapPoints(this.scratch);
        if (index == 0) {
            Path path = spec.path;
            float[] fArr = this.scratch;
            path.moveTo(fArr[0], fArr[1]);
        } else {
            Path path2 = spec.path;
            float[] fArr2 = this.scratch;
            path2.lineTo(fArr2[0], fArr2[1]);
        }
        this.cornerPaths[index].applyToPath(this.cornerTransforms[index], spec.path);
        if (spec.pathListener != null) {
            spec.pathListener.onCornerPathCreated(this.cornerPaths[index], this.cornerTransforms[index], index);
        }
    }

    private void appendEdgePath(ShapeAppearancePathSpec spec, int index) {
        int i = (index + 1) % 4;
        this.scratch[0] = this.cornerPaths[index].getEndX();
        this.scratch[1] = this.cornerPaths[index].getEndY();
        this.cornerTransforms[index].mapPoints(this.scratch);
        this.scratch2[0] = this.cornerPaths[i].getStartX();
        this.scratch2[1] = this.cornerPaths[i].getStartY();
        this.cornerTransforms[i].mapPoints(this.scratch2);
        float[] fArr = this.scratch;
        float f = fArr[0];
        float[] fArr2 = this.scratch2;
        float max = Math.max(((float) Math.hypot((double) (f - fArr2[0]), (double) (fArr[1] - fArr2[1]))) - 0.001f, 0.0f);
        float edgeCenterForIndex = getEdgeCenterForIndex(spec.bounds, index);
        this.shapePath.reset(0.0f, 0.0f);
        EdgeTreatment edgeTreatmentForIndex = getEdgeTreatmentForIndex(index, spec.shapeAppearanceModel);
        edgeTreatmentForIndex.getEdgePath(max, edgeCenterForIndex, spec.interpolation, this.shapePath);
        this.edgePath.reset();
        this.shapePath.applyToPath(this.edgeTransforms[index], this.edgePath);
        if (!this.edgeIntersectionCheckEnabled || Build.VERSION.SDK_INT < 19 || (!edgeTreatmentForIndex.forceIntersection() && !pathOverlapsCorner(this.edgePath, index) && !pathOverlapsCorner(this.edgePath, i))) {
            this.shapePath.applyToPath(this.edgeTransforms[index], spec.path);
        } else {
            Path path = this.edgePath;
            path.op(path, this.boundsPath, Path.Op.DIFFERENCE);
            this.scratch[0] = this.shapePath.getStartX();
            this.scratch[1] = this.shapePath.getStartY();
            this.edgeTransforms[index].mapPoints(this.scratch);
            Path path2 = this.overlappedEdgePath;
            float[] fArr3 = this.scratch;
            path2.moveTo(fArr3[0], fArr3[1]);
            this.shapePath.applyToPath(this.edgeTransforms[index], this.overlappedEdgePath);
        }
        if (spec.pathListener != null) {
            spec.pathListener.onEdgePathCreated(this.shapePath, this.edgeTransforms[index], index);
        }
    }

    private void getCoordinatesOfCorner(int index, RectF bounds, PointF pointF2) {
        switch (index) {
            case 1:
                pointF2.set(bounds.right, bounds.bottom);
                return;
            case 2:
                pointF2.set(bounds.left, bounds.bottom);
                return;
            case 3:
                pointF2.set(bounds.left, bounds.top);
                return;
            default:
                pointF2.set(bounds.right, bounds.top);
                return;
        }
    }

    private CornerSize getCornerSizeForIndex(int index, ShapeAppearanceModel shapeAppearanceModel) {
        switch (index) {
            case 1:
                return shapeAppearanceModel.getBottomRightCornerSize();
            case 2:
                return shapeAppearanceModel.getBottomLeftCornerSize();
            case 3:
                return shapeAppearanceModel.getTopLeftCornerSize();
            default:
                return shapeAppearanceModel.getTopRightCornerSize();
        }
    }

    private CornerTreatment getCornerTreatmentForIndex(int index, ShapeAppearanceModel shapeAppearanceModel) {
        switch (index) {
            case 1:
                return shapeAppearanceModel.getBottomRightCorner();
            case 2:
                return shapeAppearanceModel.getBottomLeftCorner();
            case 3:
                return shapeAppearanceModel.getTopLeftCorner();
            default:
                return shapeAppearanceModel.getTopRightCorner();
        }
    }

    private float getEdgeCenterForIndex(RectF bounds, int index) {
        this.scratch[0] = this.cornerPaths[index].endX;
        this.scratch[1] = this.cornerPaths[index].endY;
        this.cornerTransforms[index].mapPoints(this.scratch);
        switch (index) {
            case 1:
            case 3:
                return Math.abs(bounds.centerX() - this.scratch[0]);
            default:
                return Math.abs(bounds.centerY() - this.scratch[1]);
        }
    }

    private EdgeTreatment getEdgeTreatmentForIndex(int index, ShapeAppearanceModel shapeAppearanceModel) {
        switch (index) {
            case 1:
                return shapeAppearanceModel.getBottomEdge();
            case 2:
                return shapeAppearanceModel.getLeftEdge();
            case 3:
                return shapeAppearanceModel.getTopEdge();
            default:
                return shapeAppearanceModel.getRightEdge();
        }
    }

    public static ShapeAppearancePathProvider getInstance() {
        return Lazy.INSTANCE;
    }

    private boolean pathOverlapsCorner(Path edgePath2, int index) {
        this.cornerPath.reset();
        this.cornerPaths[index].applyToPath(this.cornerTransforms[index], this.cornerPath);
        RectF rectF = new RectF();
        edgePath2.computeBounds(rectF, true);
        this.cornerPath.computeBounds(rectF, true);
        edgePath2.op(this.cornerPath, Path.Op.INTERSECT);
        edgePath2.computeBounds(rectF, true);
        if (rectF.isEmpty()) {
            return rectF.width() > 1.0f && rectF.height() > 1.0f;
        }
        return true;
    }

    private void setCornerPathAndTransform(ShapeAppearancePathSpec spec, int index) {
        getCornerTreatmentForIndex(index, spec.shapeAppearanceModel).getCornerPath(this.cornerPaths[index], 90.0f, spec.interpolation, spec.bounds, getCornerSizeForIndex(index, spec.shapeAppearanceModel));
        float angleOfEdge = angleOfEdge(index);
        this.cornerTransforms[index].reset();
        getCoordinatesOfCorner(index, spec.bounds, this.pointF);
        this.cornerTransforms[index].setTranslate(this.pointF.x, this.pointF.y);
        this.cornerTransforms[index].preRotate(angleOfEdge);
    }

    private void setEdgePathAndTransform(int index) {
        this.scratch[0] = this.cornerPaths[index].getEndX();
        this.scratch[1] = this.cornerPaths[index].getEndY();
        this.cornerTransforms[index].mapPoints(this.scratch);
        float angleOfEdge = angleOfEdge(index);
        this.edgeTransforms[index].reset();
        Matrix matrix = this.edgeTransforms[index];
        float[] fArr = this.scratch;
        matrix.setTranslate(fArr[0], fArr[1]);
        this.edgeTransforms[index].preRotate(angleOfEdge);
    }

    public void calculatePath(ShapeAppearanceModel shapeAppearanceModel, float interpolation, RectF bounds, Path path) {
        calculatePath(shapeAppearanceModel, interpolation, bounds, (PathListener) null, path);
    }

    public void calculatePath(ShapeAppearanceModel shapeAppearanceModel, float interpolation, RectF bounds, PathListener pathListener, Path path) {
        path.rewind();
        this.overlappedEdgePath.rewind();
        this.boundsPath.rewind();
        this.boundsPath.addRect(bounds, Path.Direction.CW);
        ShapeAppearancePathSpec shapeAppearancePathSpec = new ShapeAppearancePathSpec(shapeAppearanceModel, interpolation, bounds, pathListener, path);
        for (int i = 0; i < 4; i++) {
            setCornerPathAndTransform(shapeAppearancePathSpec, i);
            setEdgePathAndTransform(i);
        }
        for (int i2 = 0; i2 < 4; i2++) {
            appendCornerPath(shapeAppearancePathSpec, i2);
            appendEdgePath(shapeAppearancePathSpec, i2);
        }
        path.close();
        this.overlappedEdgePath.close();
        if (Build.VERSION.SDK_INT >= 19 && !this.overlappedEdgePath.isEmpty()) {
            path.op(this.overlappedEdgePath, Path.Op.UNION);
        }
    }

    /* access modifiers changed from: package-private */
    public void setEdgeIntersectionCheckEnable(boolean enable) {
        this.edgeIntersectionCheckEnabled = enable;
    }
}

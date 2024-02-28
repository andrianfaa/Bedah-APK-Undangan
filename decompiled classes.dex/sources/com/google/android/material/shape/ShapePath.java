package com.google.android.material.shape;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import com.google.android.material.shadow.ShadowRenderer;
import java.util.ArrayList;
import java.util.List;

public class ShapePath {
    protected static final float ANGLE_LEFT = 180.0f;
    private static final float ANGLE_UP = 270.0f;
    private boolean containsIncompatibleShadowOp;
    @Deprecated
    public float currentShadowAngle;
    @Deprecated
    public float endShadowAngle;
    @Deprecated
    public float endX;
    @Deprecated
    public float endY;
    private final List<PathOperation> operations = new ArrayList();
    private final List<ShadowCompatOperation> shadowCompatOperations = new ArrayList();
    @Deprecated
    public float startX;
    @Deprecated
    public float startY;

    static class ArcShadowOperation extends ShadowCompatOperation {
        private final PathArcOperation operation;

        public ArcShadowOperation(PathArcOperation operation2) {
            this.operation = operation2;
        }

        public void draw(Matrix transform, ShadowRenderer shadowRenderer, int shadowElevation, Canvas canvas) {
            float access$800 = this.operation.getStartAngle();
            float access$900 = this.operation.getSweepAngle();
            shadowRenderer.drawCornerShadow(canvas, transform, new RectF(this.operation.getLeft(), this.operation.getTop(), this.operation.getRight(), this.operation.getBottom()), shadowElevation, access$800, access$900);
        }
    }

    static class LineShadowOperation extends ShadowCompatOperation {
        private final PathLineOperation operation;
        private final float startX;
        private final float startY;

        public LineShadowOperation(PathLineOperation operation2, float startX2, float startY2) {
            this.operation = operation2;
            this.startX = startX2;
            this.startY = startY2;
        }

        public void draw(Matrix transform, ShadowRenderer shadowRenderer, int shadowElevation, Canvas canvas) {
            RectF rectF = new RectF(0.0f, 0.0f, (float) Math.hypot((double) (this.operation.y - this.startY), (double) (this.operation.x - this.startX)), 0.0f);
            Matrix matrix = new Matrix(transform);
            matrix.preTranslate(this.startX, this.startY);
            matrix.preRotate(getAngle());
            shadowRenderer.drawEdgeShadow(canvas, matrix, rectF, shadowElevation);
        }

        /* access modifiers changed from: package-private */
        public float getAngle() {
            return (float) Math.toDegrees(Math.atan((double) ((this.operation.y - this.startY) / (this.operation.x - this.startX))));
        }
    }

    public static class PathArcOperation extends PathOperation {
        private static final RectF rectF = new RectF();
        @Deprecated
        public float bottom;
        @Deprecated
        public float left;
        @Deprecated
        public float right;
        @Deprecated
        public float startAngle;
        @Deprecated
        public float sweepAngle;
        @Deprecated
        public float top;

        public PathArcOperation(float left2, float top2, float right2, float bottom2) {
            setLeft(left2);
            setTop(top2);
            setRight(right2);
            setBottom(bottom2);
        }

        /* access modifiers changed from: private */
        public float getBottom() {
            return this.bottom;
        }

        /* access modifiers changed from: private */
        public float getLeft() {
            return this.left;
        }

        /* access modifiers changed from: private */
        public float getRight() {
            return this.right;
        }

        /* access modifiers changed from: private */
        public float getStartAngle() {
            return this.startAngle;
        }

        /* access modifiers changed from: private */
        public float getSweepAngle() {
            return this.sweepAngle;
        }

        /* access modifiers changed from: private */
        public float getTop() {
            return this.top;
        }

        private void setBottom(float bottom2) {
            this.bottom = bottom2;
        }

        private void setLeft(float left2) {
            this.left = left2;
        }

        private void setRight(float right2) {
            this.right = right2;
        }

        /* access modifiers changed from: private */
        public void setStartAngle(float startAngle2) {
            this.startAngle = startAngle2;
        }

        /* access modifiers changed from: private */
        public void setSweepAngle(float sweepAngle2) {
            this.sweepAngle = sweepAngle2;
        }

        private void setTop(float top2) {
            this.top = top2;
        }

        public void applyToPath(Matrix transform, Path path) {
            Matrix matrix = this.matrix;
            transform.invert(matrix);
            path.transform(matrix);
            RectF rectF2 = rectF;
            rectF2.set(getLeft(), getTop(), getRight(), getBottom());
            path.arcTo(rectF2, getStartAngle(), getSweepAngle(), false);
            path.transform(transform);
        }
    }

    public static class PathCubicOperation extends PathOperation {
        private float controlX1;
        private float controlX2;
        private float controlY1;
        private float controlY2;
        private float endX;
        private float endY;

        public PathCubicOperation(float controlX12, float controlY12, float controlX22, float controlY22, float endX2, float endY2) {
            setControlX1(controlX12);
            setControlY1(controlY12);
            setControlX2(controlX22);
            setControlY2(controlY22);
            setEndX(endX2);
            setEndY(endY2);
        }

        private float getControlX1() {
            return this.controlX1;
        }

        private float getControlX2() {
            return this.controlX2;
        }

        private float getControlY1() {
            return this.controlY1;
        }

        private float getControlY2() {
            return this.controlY1;
        }

        private float getEndX() {
            return this.endX;
        }

        private float getEndY() {
            return this.endY;
        }

        private void setControlX1(float controlX12) {
            this.controlX1 = controlX12;
        }

        private void setControlX2(float controlX22) {
            this.controlX2 = controlX22;
        }

        private void setControlY1(float controlY12) {
            this.controlY1 = controlY12;
        }

        private void setControlY2(float controlY22) {
            this.controlY2 = controlY22;
        }

        private void setEndX(float endX2) {
            this.endX = endX2;
        }

        private void setEndY(float endY2) {
            this.endY = endY2;
        }

        public void applyToPath(Matrix transform, Path path) {
            Matrix matrix = this.matrix;
            transform.invert(matrix);
            path.transform(matrix);
            path.cubicTo(this.controlX1, this.controlY1, this.controlX2, this.controlY2, this.endX, this.endY);
            path.transform(transform);
        }
    }

    public static class PathLineOperation extends PathOperation {
        /* access modifiers changed from: private */
        public float x;
        /* access modifiers changed from: private */
        public float y;

        public void applyToPath(Matrix transform, Path path) {
            Matrix matrix = this.matrix;
            transform.invert(matrix);
            path.transform(matrix);
            path.lineTo(this.x, this.y);
            path.transform(transform);
        }
    }

    public static abstract class PathOperation {
        protected final Matrix matrix = new Matrix();

        public abstract void applyToPath(Matrix matrix2, Path path);
    }

    public static class PathQuadOperation extends PathOperation {
        @Deprecated
        public float controlX;
        @Deprecated
        public float controlY;
        @Deprecated
        public float endX;
        @Deprecated
        public float endY;

        private float getControlX() {
            return this.controlX;
        }

        private float getControlY() {
            return this.controlY;
        }

        private float getEndX() {
            return this.endX;
        }

        private float getEndY() {
            return this.endY;
        }

        /* access modifiers changed from: private */
        public void setControlX(float controlX2) {
            this.controlX = controlX2;
        }

        /* access modifiers changed from: private */
        public void setControlY(float controlY2) {
            this.controlY = controlY2;
        }

        /* access modifiers changed from: private */
        public void setEndX(float endX2) {
            this.endX = endX2;
        }

        /* access modifiers changed from: private */
        public void setEndY(float endY2) {
            this.endY = endY2;
        }

        public void applyToPath(Matrix transform, Path path) {
            Matrix matrix = this.matrix;
            transform.invert(matrix);
            path.transform(matrix);
            path.quadTo(getControlX(), getControlY(), getEndX(), getEndY());
            path.transform(transform);
        }
    }

    static abstract class ShadowCompatOperation {
        static final Matrix IDENTITY_MATRIX = new Matrix();

        ShadowCompatOperation() {
        }

        public abstract void draw(Matrix matrix, ShadowRenderer shadowRenderer, int i, Canvas canvas);

        public final void draw(ShadowRenderer shadowRenderer, int shadowElevation, Canvas canvas) {
            draw(IDENTITY_MATRIX, shadowRenderer, shadowElevation, canvas);
        }
    }

    public ShapePath() {
        reset(0.0f, 0.0f);
    }

    public ShapePath(float startX2, float startY2) {
        reset(startX2, startY2);
    }

    private void addConnectingShadowIfNecessary(float nextShadowAngle) {
        if (getCurrentShadowAngle() != nextShadowAngle) {
            float currentShadowAngle2 = ((nextShadowAngle - getCurrentShadowAngle()) + 360.0f) % 360.0f;
            if (currentShadowAngle2 <= ANGLE_LEFT) {
                PathArcOperation pathArcOperation = new PathArcOperation(getEndX(), getEndY(), getEndX(), getEndY());
                pathArcOperation.setStartAngle(getCurrentShadowAngle());
                pathArcOperation.setSweepAngle(currentShadowAngle2);
                this.shadowCompatOperations.add(new ArcShadowOperation(pathArcOperation));
                setCurrentShadowAngle(nextShadowAngle);
            }
        }
    }

    private void addShadowCompatOperation(ShadowCompatOperation shadowOperation, float startShadowAngle, float endShadowAngle2) {
        addConnectingShadowIfNecessary(startShadowAngle);
        this.shadowCompatOperations.add(shadowOperation);
        setCurrentShadowAngle(endShadowAngle2);
    }

    private float getCurrentShadowAngle() {
        return this.currentShadowAngle;
    }

    private float getEndShadowAngle() {
        return this.endShadowAngle;
    }

    private void setCurrentShadowAngle(float currentShadowAngle2) {
        this.currentShadowAngle = currentShadowAngle2;
    }

    private void setEndShadowAngle(float endShadowAngle2) {
        this.endShadowAngle = endShadowAngle2;
    }

    private void setEndX(float endX2) {
        this.endX = endX2;
    }

    private void setEndY(float endY2) {
        this.endY = endY2;
    }

    private void setStartX(float startX2) {
        this.startX = startX2;
    }

    private void setStartY(float startY2) {
        this.startY = startY2;
    }

    public void addArc(float left, float top, float right, float bottom, float startAngle, float sweepAngle) {
        float f = left;
        float f2 = top;
        float f3 = right;
        float f4 = bottom;
        float f5 = startAngle;
        float f6 = sweepAngle;
        PathArcOperation pathArcOperation = new PathArcOperation(f, f2, f3, f4);
        pathArcOperation.setStartAngle(f5);
        pathArcOperation.setSweepAngle(f6);
        this.operations.add(pathArcOperation);
        ArcShadowOperation arcShadowOperation = new ArcShadowOperation(pathArcOperation);
        float f7 = f5 + f6;
        boolean z = f6 < 0.0f;
        addShadowCompatOperation(arcShadowOperation, z ? (f5 + ANGLE_LEFT) % 360.0f : f5, z ? (ANGLE_LEFT + f7) % 360.0f : f7);
        setEndX(((f + f3) * 0.5f) + (((f3 - f) / 2.0f) * ((float) Math.cos(Math.toRadians((double) (f5 + f6))))));
        setEndY(((f2 + f4) * 0.5f) + (((f4 - f2) / 2.0f) * ((float) Math.sin(Math.toRadians((double) (f5 + f6))))));
    }

    public void applyToPath(Matrix transform, Path path) {
        int size = this.operations.size();
        for (int i = 0; i < size; i++) {
            this.operations.get(i).applyToPath(transform, path);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean containsIncompatibleShadowOp() {
        return this.containsIncompatibleShadowOp;
    }

    /* access modifiers changed from: package-private */
    public ShadowCompatOperation createShadowCompatOperation(Matrix transform) {
        addConnectingShadowIfNecessary(getEndShadowAngle());
        final Matrix matrix = new Matrix(transform);
        final ArrayList arrayList = new ArrayList(this.shadowCompatOperations);
        return new ShadowCompatOperation() {
            public void draw(Matrix matrix, ShadowRenderer shadowRenderer, int shadowElevation, Canvas canvas) {
                for (ShadowCompatOperation draw : arrayList) {
                    draw.draw(matrix, shadowRenderer, shadowElevation, canvas);
                }
            }
        };
    }

    public void cubicToPoint(float controlX1, float controlY1, float controlX2, float controlY2, float toX, float toY) {
        this.operations.add(new PathCubicOperation(controlX1, controlY1, controlX2, controlY2, toX, toY));
        this.containsIncompatibleShadowOp = true;
        setEndX(toX);
        setEndY(toY);
    }

    /* access modifiers changed from: package-private */
    public float getEndX() {
        return this.endX;
    }

    /* access modifiers changed from: package-private */
    public float getEndY() {
        return this.endY;
    }

    /* access modifiers changed from: package-private */
    public float getStartX() {
        return this.startX;
    }

    /* access modifiers changed from: package-private */
    public float getStartY() {
        return this.startY;
    }

    public void lineTo(float x, float y) {
        PathLineOperation pathLineOperation = new PathLineOperation();
        float unused = pathLineOperation.x = x;
        float unused2 = pathLineOperation.y = y;
        this.operations.add(pathLineOperation);
        LineShadowOperation lineShadowOperation = new LineShadowOperation(pathLineOperation, getEndX(), getEndY());
        addShadowCompatOperation(lineShadowOperation, lineShadowOperation.getAngle() + ANGLE_UP, lineShadowOperation.getAngle() + ANGLE_UP);
        setEndX(x);
        setEndY(y);
    }

    public void quadToPoint(float controlX, float controlY, float toX, float toY) {
        PathQuadOperation pathQuadOperation = new PathQuadOperation();
        pathQuadOperation.setControlX(controlX);
        pathQuadOperation.setControlY(controlY);
        pathQuadOperation.setEndX(toX);
        pathQuadOperation.setEndY(toY);
        this.operations.add(pathQuadOperation);
        this.containsIncompatibleShadowOp = true;
        setEndX(toX);
        setEndY(toY);
    }

    public void reset(float startX2, float startY2) {
        reset(startX2, startY2, ANGLE_UP, 0.0f);
    }

    public void reset(float startX2, float startY2, float shadowStartAngle, float shadowSweepAngle) {
        setStartX(startX2);
        setStartY(startY2);
        setEndX(startX2);
        setEndY(startY2);
        setCurrentShadowAngle(shadowStartAngle);
        setEndShadowAngle((shadowStartAngle + shadowSweepAngle) % 360.0f);
        this.operations.clear();
        this.shadowCompatOperations.clear();
        this.containsIncompatibleShadowOp = false;
    }
}

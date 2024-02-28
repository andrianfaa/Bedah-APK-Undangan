package com.google.android.material.shape;

public class CutCornerTreatment extends CornerTreatment {
    float size = -1.0f;

    public CutCornerTreatment() {
    }

    @Deprecated
    public CutCornerTreatment(float size2) {
        this.size = size2;
    }

    public void getCornerPath(ShapePath shapePath, float angle, float interpolation, float radius) {
        shapePath.reset(0.0f, radius * interpolation, 180.0f, 180.0f - angle);
        shapePath.lineTo((float) (Math.sin(Math.toRadians((double) angle)) * ((double) radius) * ((double) interpolation)), (float) (Math.sin(Math.toRadians((double) (90.0f - angle))) * ((double) radius) * ((double) interpolation)));
    }
}

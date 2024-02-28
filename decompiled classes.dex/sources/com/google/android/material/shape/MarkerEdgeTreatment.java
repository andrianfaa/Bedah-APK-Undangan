package com.google.android.material.shape;

public final class MarkerEdgeTreatment extends EdgeTreatment {
    private final float radius;

    public MarkerEdgeTreatment(float radius2) {
        this.radius = radius2 - 0.001f;
    }

    /* access modifiers changed from: package-private */
    public boolean forceIntersection() {
        return true;
    }

    public void getEdgePath(float length, float center, float interpolation, ShapePath shapePath) {
        float sqrt = (float) ((((double) this.radius) * Math.sqrt(2.0d)) / 2.0d);
        float sqrt2 = (float) Math.sqrt(Math.pow((double) this.radius, 2.0d) - Math.pow((double) sqrt, 2.0d));
        shapePath.reset(center - sqrt, ((float) (-((((double) this.radius) * Math.sqrt(2.0d)) - ((double) this.radius)))) + sqrt2);
        shapePath.lineTo(center, (float) (-((((double) this.radius) * Math.sqrt(2.0d)) - ((double) this.radius))));
        shapePath.lineTo(center + sqrt, ((float) (-((((double) this.radius) * Math.sqrt(2.0d)) - ((double) this.radius)))) + sqrt2);
    }
}

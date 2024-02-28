package com.google.android.material.shape;

import android.graphics.RectF;
import java.util.Arrays;

public final class RelativeCornerSize implements CornerSize {
    private final float percent;

    public RelativeCornerSize(float percent2) {
        this.percent = percent2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RelativeCornerSize)) {
            return false;
        }
        return this.percent == ((RelativeCornerSize) o).percent;
    }

    public float getCornerSize(RectF bounds) {
        return this.percent * bounds.height();
    }

    public float getRelativePercent() {
        return this.percent;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Float.valueOf(this.percent)});
    }
}

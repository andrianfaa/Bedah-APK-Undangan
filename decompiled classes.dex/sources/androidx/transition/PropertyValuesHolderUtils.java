package androidx.transition;

import android.animation.PropertyValuesHolder;
import android.animation.TypeConverter;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.util.Property;

class PropertyValuesHolderUtils {
    private PropertyValuesHolderUtils() {
    }

    static PropertyValuesHolder ofPointF(Property<?, PointF> property, Path path) {
        return Build.VERSION.SDK_INT >= 21 ? PropertyValuesHolder.ofObject(property, (TypeConverter) null, path) : PropertyValuesHolder.ofFloat(new PathProperty(property, path), new float[]{0.0f, 1.0f});
    }
}

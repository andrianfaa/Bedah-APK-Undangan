package androidx.dynamicanimation.animation;

import android.util.FloatProperty;

public abstract class FloatPropertyCompat<T> {
    final String mPropertyName;

    public FloatPropertyCompat(String name) {
        this.mPropertyName = name;
    }

    public static <T> FloatPropertyCompat<T> createFloatPropertyCompat(final FloatProperty<T> floatProperty) {
        return new FloatPropertyCompat<T>(floatProperty.getName()) {
            public float getValue(T t) {
                return ((Float) floatProperty.get(t)).floatValue();
            }

            public void setValue(T t, float value) {
                floatProperty.setValue(t, value);
            }
        };
    }

    public abstract float getValue(T t);

    public abstract void setValue(T t, float f);
}

package com.google.android.material.animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.util.Property;
import androidx.collection.SimpleArrayMap;
import java.util.ArrayList;
import java.util.List;
import mt.Log1F380D;

/* compiled from: 00E5 */
public class MotionSpec {
    private static final String TAG = "MotionSpec";
    private final SimpleArrayMap<String, PropertyValuesHolder[]> propertyValues = new SimpleArrayMap<>();
    private final SimpleArrayMap<String, MotionTiming> timings = new SimpleArrayMap<>();

    private static void addInfoFromAnimator(MotionSpec spec, Animator animator) {
        if (animator instanceof ObjectAnimator) {
            ObjectAnimator objectAnimator = (ObjectAnimator) animator;
            spec.setPropertyValues(objectAnimator.getPropertyName(), objectAnimator.getValues());
            spec.setTiming(objectAnimator.getPropertyName(), MotionTiming.createFromAnimator(objectAnimator));
            return;
        }
        throw new IllegalArgumentException("Animator must be an ObjectAnimator: " + animator);
    }

    private PropertyValuesHolder[] clonePropertyValuesHolder(PropertyValuesHolder[] values) {
        PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[values.length];
        for (int i = 0; i < values.length; i++) {
            propertyValuesHolderArr[i] = values[i].clone();
        }
        return propertyValuesHolderArr;
    }

    public static MotionSpec createFromAttribute(Context context, TypedArray attributes, int index) {
        int resourceId;
        if (!attributes.hasValue(index) || (resourceId = attributes.getResourceId(index, 0)) == 0) {
            return null;
        }
        return createFromResource(context, resourceId);
    }

    public static MotionSpec createFromResource(Context context, int id) {
        try {
            Animator loadAnimator = AnimatorInflater.loadAnimator(context, id);
            if (loadAnimator instanceof AnimatorSet) {
                return createSpecFromAnimators(((AnimatorSet) loadAnimator).getChildAnimations());
            }
            if (loadAnimator == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(loadAnimator);
            return createSpecFromAnimators(arrayList);
        } catch (Exception e) {
            StringBuilder append = new StringBuilder().append("Can't load animation resource ID #0x");
            String hexString = Integer.toHexString(id);
            Log1F380D.a((Object) hexString);
            Log.w(TAG, append.append(hexString).toString(), e);
            return null;
        }
    }

    private static MotionSpec createSpecFromAnimators(List<Animator> list) {
        MotionSpec motionSpec = new MotionSpec();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            addInfoFromAnimator(motionSpec, list.get(i));
        }
        return motionSpec;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MotionSpec)) {
            return false;
        }
        return this.timings.equals(((MotionSpec) o).timings);
    }

    public <T> ObjectAnimator getAnimator(String name, T t, Property<T, ?> property) {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(t, getPropertyValues(name));
        ofPropertyValuesHolder.setProperty(property);
        getTiming(name).apply(ofPropertyValuesHolder);
        return ofPropertyValuesHolder;
    }

    public PropertyValuesHolder[] getPropertyValues(String name) {
        if (hasPropertyValues(name)) {
            return clonePropertyValuesHolder(this.propertyValues.get(name));
        }
        throw new IllegalArgumentException();
    }

    public MotionTiming getTiming(String name) {
        if (hasTiming(name)) {
            return this.timings.get(name);
        }
        throw new IllegalArgumentException();
    }

    public long getTotalDuration() {
        long j = 0;
        int size = this.timings.size();
        for (int i = 0; i < size; i++) {
            MotionTiming valueAt = this.timings.valueAt(i);
            j = Math.max(j, valueAt.getDelay() + valueAt.getDuration());
        }
        return j;
    }

    public boolean hasPropertyValues(String name) {
        return this.propertyValues.get(name) != null;
    }

    public boolean hasTiming(String name) {
        return this.timings.get(name) != null;
    }

    public int hashCode() {
        return this.timings.hashCode();
    }

    public void setPropertyValues(String name, PropertyValuesHolder[] values) {
        this.propertyValues.put(name, values);
    }

    public void setTiming(String name, MotionTiming timing) {
        this.timings.put(name, timing);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(10);
        sb.append(getClass().getName());
        sb.append('{');
        String hexString = Integer.toHexString(System.identityHashCode(this));
        Log1F380D.a((Object) hexString);
        sb.append(hexString);
        sb.append(" timings: ");
        sb.append(this.timings);
        sb.append("}\n");
        return sb.toString();
    }
}

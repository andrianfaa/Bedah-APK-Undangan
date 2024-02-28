package com.google.android.material.animation;

import android.util.Property;
import android.view.ViewGroup;
import com.google.android.material.R;

public class ChildrenAlphaProperty extends Property<ViewGroup, Float> {
    public static final Property<ViewGroup, Float> CHILDREN_ALPHA = new ChildrenAlphaProperty("childrenAlpha");

    private ChildrenAlphaProperty(String name) {
        super(Float.class, name);
    }

    public Float get(ViewGroup object) {
        Float f = (Float) object.getTag(R.id.mtrl_internal_children_alpha_tag);
        return f != null ? f : Float.valueOf(1.0f);
    }

    public void set(ViewGroup object, Float value) {
        float floatValue = value.floatValue();
        object.setTag(R.id.mtrl_internal_children_alpha_tag, Float.valueOf(floatValue));
        int childCount = object.getChildCount();
        for (int i = 0; i < childCount; i++) {
            object.getChildAt(i).setAlpha(floatValue);
        }
    }
}

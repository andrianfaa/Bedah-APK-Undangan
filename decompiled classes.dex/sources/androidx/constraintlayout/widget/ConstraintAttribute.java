package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.View;
import androidx.core.view.ViewCompat;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;

public class ConstraintAttribute {
    private static final String TAG = "TransitionLayout";
    boolean mBooleanValue;
    private int mColorValue;
    private float mFloatValue;
    private int mIntegerValue;
    private boolean mMethod = false;
    String mName;
    private String mStringValue;
    private AttributeType mType;

    public enum AttributeType {
        INT_TYPE,
        FLOAT_TYPE,
        COLOR_TYPE,
        COLOR_DRAWABLE_TYPE,
        STRING_TYPE,
        BOOLEAN_TYPE,
        DIMENSION_TYPE,
        REFERENCE_TYPE
    }

    public ConstraintAttribute(ConstraintAttribute source, Object value) {
        this.mName = source.mName;
        this.mType = source.mType;
        setValue(value);
    }

    public ConstraintAttribute(String name, AttributeType attributeType) {
        this.mName = name;
        this.mType = attributeType;
    }

    public ConstraintAttribute(String name, AttributeType attributeType, Object value, boolean method) {
        this.mName = name;
        this.mType = attributeType;
        this.mMethod = method;
        setValue(value);
    }

    private static int clamp(int c) {
        int c2 = (c & (~(c >> 31))) - 255;
        return (c2 & (c2 >> 31)) + 255;
    }

    public static HashMap<String, ConstraintAttribute> extractAttributes(HashMap<String, ConstraintAttribute> hashMap, View view) {
        HashMap<String, ConstraintAttribute> hashMap2 = new HashMap<>();
        Class<?> cls = view.getClass();
        for (String next : hashMap.keySet()) {
            ConstraintAttribute constraintAttribute = hashMap.get(next);
            try {
                if (next.equals("BackgroundColor")) {
                    hashMap2.put(next, new ConstraintAttribute(constraintAttribute, (Object) Integer.valueOf(((ColorDrawable) view.getBackground()).getColor())));
                } else {
                    hashMap2.put(next, new ConstraintAttribute(constraintAttribute, cls.getMethod("getMap" + next, new Class[0]).invoke(view, new Object[0])));
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        }
        return hashMap2;
    }

    public static void parse(Context context, XmlPullParser parser, HashMap<String, ConstraintAttribute> hashMap) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(parser), R.styleable.CustomAttribute);
        String str = null;
        boolean z = false;
        Object obj = null;
        AttributeType attributeType = null;
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = obtainStyledAttributes.getIndex(i);
            if (index == R.styleable.CustomAttribute_attributeName) {
                str = obtainStyledAttributes.getString(index);
                if (str != null && str.length() > 0) {
                    str = Character.toUpperCase(str.charAt(0)) + str.substring(1);
                }
            } else if (index == R.styleable.CustomAttribute_methodName) {
                z = true;
                str = obtainStyledAttributes.getString(index);
            } else if (index == R.styleable.CustomAttribute_customBoolean) {
                obj = Boolean.valueOf(obtainStyledAttributes.getBoolean(index, false));
                attributeType = AttributeType.BOOLEAN_TYPE;
            } else if (index == R.styleable.CustomAttribute_customColorValue) {
                attributeType = AttributeType.COLOR_TYPE;
                obj = Integer.valueOf(obtainStyledAttributes.getColor(index, 0));
            } else if (index == R.styleable.CustomAttribute_customColorDrawableValue) {
                attributeType = AttributeType.COLOR_DRAWABLE_TYPE;
                obj = Integer.valueOf(obtainStyledAttributes.getColor(index, 0));
            } else if (index == R.styleable.CustomAttribute_customPixelDimension) {
                attributeType = AttributeType.DIMENSION_TYPE;
                obj = Float.valueOf(TypedValue.applyDimension(1, obtainStyledAttributes.getDimension(index, 0.0f), context.getResources().getDisplayMetrics()));
            } else if (index == R.styleable.CustomAttribute_customDimension) {
                attributeType = AttributeType.DIMENSION_TYPE;
                obj = Float.valueOf(obtainStyledAttributes.getDimension(index, 0.0f));
            } else if (index == R.styleable.CustomAttribute_customFloatValue) {
                attributeType = AttributeType.FLOAT_TYPE;
                obj = Float.valueOf(obtainStyledAttributes.getFloat(index, Float.NaN));
            } else if (index == R.styleable.CustomAttribute_customIntegerValue) {
                attributeType = AttributeType.INT_TYPE;
                obj = Integer.valueOf(obtainStyledAttributes.getInteger(index, -1));
            } else if (index == R.styleable.CustomAttribute_customStringValue) {
                attributeType = AttributeType.STRING_TYPE;
                obj = obtainStyledAttributes.getString(index);
            } else if (index == R.styleable.CustomAttribute_customReference) {
                attributeType = AttributeType.REFERENCE_TYPE;
                int resourceId = obtainStyledAttributes.getResourceId(index, -1);
                if (resourceId == -1) {
                    resourceId = obtainStyledAttributes.getInt(index, -1);
                }
                obj = Integer.valueOf(resourceId);
            }
        }
        if (!(str == null || obj == null)) {
            hashMap.put(str, new ConstraintAttribute(str, attributeType, obj, z));
        }
        obtainStyledAttributes.recycle();
    }

    public static void setAttributes(View view, HashMap<String, ConstraintAttribute> hashMap) {
        Class<?> cls = view.getClass();
        for (String next : hashMap.keySet()) {
            ConstraintAttribute constraintAttribute = hashMap.get(next);
            String str = next;
            if (!constraintAttribute.mMethod) {
                str = "set" + str;
            }
            try {
                switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[constraintAttribute.mType.ordinal()]) {
                    case 1:
                        cls.getMethod(str, new Class[]{Integer.TYPE}).invoke(view, new Object[]{Integer.valueOf(constraintAttribute.mIntegerValue)});
                        break;
                    case 2:
                        cls.getMethod(str, new Class[]{Boolean.TYPE}).invoke(view, new Object[]{Boolean.valueOf(constraintAttribute.mBooleanValue)});
                        break;
                    case 3:
                        cls.getMethod(str, new Class[]{CharSequence.class}).invoke(view, new Object[]{constraintAttribute.mStringValue});
                        break;
                    case 4:
                        cls.getMethod(str, new Class[]{Integer.TYPE}).invoke(view, new Object[]{Integer.valueOf(constraintAttribute.mColorValue)});
                        break;
                    case 5:
                        Method method = cls.getMethod(str, new Class[]{Drawable.class});
                        ColorDrawable colorDrawable = new ColorDrawable();
                        colorDrawable.setColor(constraintAttribute.mColorValue);
                        method.invoke(view, new Object[]{colorDrawable});
                        break;
                    case 6:
                        cls.getMethod(str, new Class[]{Integer.TYPE}).invoke(view, new Object[]{Integer.valueOf(constraintAttribute.mIntegerValue)});
                        break;
                    case 7:
                        cls.getMethod(str, new Class[]{Float.TYPE}).invoke(view, new Object[]{Float.valueOf(constraintAttribute.mFloatValue)});
                        break;
                    case 8:
                        cls.getMethod(str, new Class[]{Float.TYPE}).invoke(view, new Object[]{Float.valueOf(constraintAttribute.mFloatValue)});
                        break;
                }
            } catch (NoSuchMethodException e) {
                Log.e(TAG, e.getMessage());
                Log.e(TAG, " Custom Attribute \"" + next + "\" not found on " + cls.getName());
                Log.e(TAG, cls.getName() + " must have a method " + str);
            } catch (IllegalAccessException e2) {
                Log.e(TAG, " Custom Attribute \"" + next + "\" not found on " + cls.getName());
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                Log.e(TAG, " Custom Attribute \"" + next + "\" not found on " + cls.getName());
                e3.printStackTrace();
            }
        }
    }

    public void applyCustom(View view) {
        Class<?> cls = view.getClass();
        String str = this.mName;
        String str2 = str;
        if (!this.mMethod) {
            str2 = "set" + str2;
        }
        try {
            switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()]) {
                case 1:
                case 6:
                    cls.getMethod(str2, new Class[]{Integer.TYPE}).invoke(view, new Object[]{Integer.valueOf(this.mIntegerValue)});
                    return;
                case 2:
                    cls.getMethod(str2, new Class[]{Boolean.TYPE}).invoke(view, new Object[]{Boolean.valueOf(this.mBooleanValue)});
                    return;
                case 3:
                    cls.getMethod(str2, new Class[]{CharSequence.class}).invoke(view, new Object[]{this.mStringValue});
                    return;
                case 4:
                    cls.getMethod(str2, new Class[]{Integer.TYPE}).invoke(view, new Object[]{Integer.valueOf(this.mColorValue)});
                    return;
                case 5:
                    Method method = cls.getMethod(str2, new Class[]{Drawable.class});
                    ColorDrawable colorDrawable = new ColorDrawable();
                    colorDrawable.setColor(this.mColorValue);
                    method.invoke(view, new Object[]{colorDrawable});
                    return;
                case 7:
                    cls.getMethod(str2, new Class[]{Float.TYPE}).invoke(view, new Object[]{Float.valueOf(this.mFloatValue)});
                    return;
                case 8:
                    cls.getMethod(str2, new Class[]{Float.TYPE}).invoke(view, new Object[]{Float.valueOf(this.mFloatValue)});
                    return;
                default:
                    return;
            }
        } catch (NoSuchMethodException e) {
            Log.e(TAG, e.getMessage());
            Log.e(TAG, " Custom Attribute \"" + str + "\" not found on " + cls.getName());
            Log.e(TAG, cls.getName() + " must have a method " + str2);
        } catch (IllegalAccessException e2) {
            Log.e(TAG, " Custom Attribute \"" + str + "\" not found on " + cls.getName());
            e2.printStackTrace();
        } catch (InvocationTargetException e3) {
            Log.e(TAG, " Custom Attribute \"" + str + "\" not found on " + cls.getName());
            e3.printStackTrace();
        }
    }

    public boolean diff(ConstraintAttribute constraintAttribute) {
        if (constraintAttribute == null || this.mType != constraintAttribute.mType) {
            return false;
        }
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()]) {
            case 1:
            case 6:
                return this.mIntegerValue == constraintAttribute.mIntegerValue;
            case 2:
                return this.mBooleanValue == constraintAttribute.mBooleanValue;
            case 3:
                return this.mIntegerValue == constraintAttribute.mIntegerValue;
            case 4:
            case 5:
                return this.mColorValue == constraintAttribute.mColorValue;
            case 7:
                return this.mFloatValue == constraintAttribute.mFloatValue;
            case 8:
                return this.mFloatValue == constraintAttribute.mFloatValue;
            default:
                return false;
        }
    }

    public int getColorValue() {
        return this.mColorValue;
    }

    public float getFloatValue() {
        return this.mFloatValue;
    }

    public int getIntegerValue() {
        return this.mIntegerValue;
    }

    public String getName() {
        return this.mName;
    }

    public String getStringValue() {
        return this.mStringValue;
    }

    public AttributeType getType() {
        return this.mType;
    }

    public float getValueToInterpolate() {
        switch (this.mType) {
            case BOOLEAN_TYPE:
                return this.mBooleanValue ? 1.0f : 0.0f;
            case STRING_TYPE:
                throw new RuntimeException("Cannot interpolate String");
            case COLOR_TYPE:
            case COLOR_DRAWABLE_TYPE:
                throw new RuntimeException("Color does not have a single color to interpolate");
            case INT_TYPE:
                return (float) this.mIntegerValue;
            case FLOAT_TYPE:
                return this.mFloatValue;
            case DIMENSION_TYPE:
                return this.mFloatValue;
            default:
                return Float.NaN;
        }
    }

    public void getValuesToInterpolate(float[] ret) {
        switch (this.mType) {
            case BOOLEAN_TYPE:
                ret[0] = this.mBooleanValue ? 1.0f : 0.0f;
                return;
            case STRING_TYPE:
                throw new RuntimeException("Color does not have a single color to interpolate");
            case COLOR_TYPE:
            case COLOR_DRAWABLE_TYPE:
                int i = this.mColorValue;
                ret[0] = (float) Math.pow((double) (((float) ((i >> 16) & 255)) / 255.0f), 2.2d);
                ret[1] = (float) Math.pow((double) (((float) ((i >> 8) & 255)) / 255.0f), 2.2d);
                ret[2] = (float) Math.pow((double) (((float) (i & 255)) / 255.0f), 2.2d);
                ret[3] = ((float) ((i >> 24) & 255)) / 255.0f;
                return;
            case INT_TYPE:
                ret[0] = (float) this.mIntegerValue;
                return;
            case FLOAT_TYPE:
                ret[0] = this.mFloatValue;
                return;
            case DIMENSION_TYPE:
                ret[0] = this.mFloatValue;
                return;
            default:
                return;
        }
    }

    public boolean isBooleanValue() {
        return this.mBooleanValue;
    }

    public boolean isContinuous() {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()]) {
            case 1:
            case 2:
            case 3:
                return false;
            default:
                return true;
        }
    }

    public boolean isMethod() {
        return this.mMethod;
    }

    public int numberOfInterpolatedValues() {
        switch (this.mType) {
            case COLOR_TYPE:
            case COLOR_DRAWABLE_TYPE:
                return 4;
            default:
                return 1;
        }
    }

    public void setColorValue(int value) {
        this.mColorValue = value;
    }

    public void setFloatValue(float value) {
        this.mFloatValue = value;
    }

    public void setIntValue(int value) {
        this.mIntegerValue = value;
    }

    public void setStringValue(String value) {
        this.mStringValue = value;
    }

    public void setValue(Object value) {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()]) {
            case 1:
            case 6:
                this.mIntegerValue = ((Integer) value).intValue();
                return;
            case 2:
                this.mBooleanValue = ((Boolean) value).booleanValue();
                return;
            case 3:
                this.mStringValue = (String) value;
                return;
            case 4:
            case 5:
                this.mColorValue = ((Integer) value).intValue();
                return;
            case 7:
                this.mFloatValue = ((Float) value).floatValue();
                return;
            case 8:
                this.mFloatValue = ((Float) value).floatValue();
                return;
            default:
                return;
        }
    }

    public void setValue(float[] value) {
        boolean z = false;
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()]) {
            case 1:
            case 6:
                this.mIntegerValue = (int) value[0];
                return;
            case 2:
                if (((double) value[0]) > 0.5d) {
                    z = true;
                }
                this.mBooleanValue = z;
                return;
            case 3:
                throw new RuntimeException("Color does not have a single color to interpolate");
            case 4:
            case 5:
                int HSVToColor = Color.HSVToColor(value);
                this.mColorValue = HSVToColor;
                this.mColorValue = (HSVToColor & ViewCompat.MEASURED_SIZE_MASK) | (clamp((int) (value[3] * 255.0f)) << 24);
                return;
            case 7:
                this.mFloatValue = value[0];
                return;
            case 8:
                this.mFloatValue = value[0];
                return;
            default:
                return;
        }
    }
}

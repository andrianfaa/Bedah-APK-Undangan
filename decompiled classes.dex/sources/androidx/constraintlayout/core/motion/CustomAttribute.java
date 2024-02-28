package androidx.constraintlayout.core.motion;

import androidx.core.view.ViewCompat;

public class CustomAttribute {
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

    public CustomAttribute(CustomAttribute source, Object value) {
        this.mName = source.mName;
        this.mType = source.mType;
        setValue(value);
    }

    public CustomAttribute(String name, AttributeType attributeType) {
        this.mName = name;
        this.mType = attributeType;
    }

    public CustomAttribute(String name, AttributeType attributeType, Object value, boolean method) {
        this.mName = name;
        this.mType = attributeType;
        this.mMethod = method;
        setValue(value);
    }

    private static int clamp(int c) {
        int c2 = (c & (~(c >> 31))) - 255;
        return (c2 & (c2 >> 31)) + 255;
    }

    public static int hsvToRgb(float hue, float saturation, float value) {
        int i = (int) (hue * 6.0f);
        float f = (6.0f * hue) - ((float) i);
        int i2 = (int) ((value * 255.0f * (1.0f - saturation)) + 0.5f);
        int i3 = (int) ((value * 255.0f * (1.0f - (f * saturation))) + 0.5f);
        int i4 = (int) ((value * 255.0f * (1.0f - ((1.0f - f) * saturation))) + 0.5f);
        int i5 = (int) ((255.0f * value) + 0.5f);
        switch (i) {
            case 0:
                return -16777216 | ((i5 << 16) + (i4 << 8) + i2);
            case 1:
                return -16777216 | ((i3 << 16) + (i5 << 8) + i2);
            case 2:
                return -16777216 | ((i2 << 16) + (i5 << 8) + i4);
            case 3:
                return -16777216 | ((i2 << 16) + (i3 << 8) + i5);
            case 4:
                return -16777216 | ((i4 << 16) + (i2 << 8) + i5);
            case 5:
                return -16777216 | ((i5 << 16) + (i2 << 8) + i3);
            default:
                return 0;
        }
    }

    public boolean diff(CustomAttribute CustomAttribute) {
        if (CustomAttribute == null || this.mType != CustomAttribute.mType) {
            return false;
        }
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$motion$CustomAttribute$AttributeType[this.mType.ordinal()]) {
            case 1:
            case 6:
                return this.mIntegerValue == CustomAttribute.mIntegerValue;
            case 2:
                return this.mBooleanValue == CustomAttribute.mBooleanValue;
            case 3:
                return this.mIntegerValue == CustomAttribute.mIntegerValue;
            case 4:
            case 5:
                return this.mColorValue == CustomAttribute.mColorValue;
            case 7:
                return this.mFloatValue == CustomAttribute.mFloatValue;
            case 8:
                return this.mFloatValue == CustomAttribute.mFloatValue;
            default:
                return false;
        }
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

    public boolean isContinuous() {
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$motion$CustomAttribute$AttributeType[this.mType.ordinal()]) {
            case 1:
            case 2:
            case 3:
                return false;
            default:
                return true;
        }
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
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$motion$CustomAttribute$AttributeType[this.mType.ordinal()]) {
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
        boolean z = true;
        switch (AnonymousClass1.$SwitchMap$androidx$constraintlayout$core$motion$CustomAttribute$AttributeType[this.mType.ordinal()]) {
            case 1:
            case 6:
                this.mIntegerValue = (int) value[0];
                return;
            case 2:
                if (((double) value[0]) <= 0.5d) {
                    z = false;
                }
                this.mBooleanValue = z;
                return;
            case 3:
                throw new RuntimeException("Color does not have a single color to interpolate");
            case 4:
            case 5:
                int hsvToRgb = hsvToRgb(value[0], value[1], value[2]);
                this.mColorValue = hsvToRgb;
                this.mColorValue = (hsvToRgb & ViewCompat.MEASURED_SIZE_MASK) | (clamp((int) (value[3] * 255.0f)) << 24);
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

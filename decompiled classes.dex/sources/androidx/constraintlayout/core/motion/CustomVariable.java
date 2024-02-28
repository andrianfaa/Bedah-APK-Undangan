package androidx.constraintlayout.core.motion;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import mt.Log1F380D;

/* compiled from: 000E */
public class CustomVariable {
    private static final String TAG = "TransitionLayout";
    boolean mBooleanValue;
    private float mFloatValue = Float.NaN;
    private int mIntegerValue = Integer.MIN_VALUE;
    String mName;
    private String mStringValue = null;
    private int mType;

    public CustomVariable(CustomVariable c) {
        this.mName = c.mName;
        this.mType = c.mType;
        this.mIntegerValue = c.mIntegerValue;
        this.mFloatValue = c.mFloatValue;
        this.mStringValue = c.mStringValue;
        this.mBooleanValue = c.mBooleanValue;
    }

    public CustomVariable(CustomVariable source, Object value) {
        this.mName = source.mName;
        this.mType = source.mType;
        setValue(value);
    }

    public CustomVariable(String name, int attributeType) {
        this.mName = name;
        this.mType = attributeType;
    }

    public CustomVariable(String name, int type, float value) {
        this.mName = name;
        this.mType = type;
        this.mFloatValue = value;
    }

    public CustomVariable(String name, int type, int value) {
        this.mName = name;
        this.mType = type;
        if (type == 901) {
            this.mFloatValue = (float) value;
        } else {
            this.mIntegerValue = value;
        }
    }

    public CustomVariable(String name, int attributeType, Object value) {
        this.mName = name;
        this.mType = attributeType;
        setValue(value);
    }

    public CustomVariable(String name, int type, String value) {
        this.mName = name;
        this.mType = type;
        this.mStringValue = value;
    }

    public CustomVariable(String name, int type, boolean value) {
        this.mName = name;
        this.mType = type;
        this.mBooleanValue = value;
    }

    private static int clamp(int c) {
        int c2 = (c & (~(c >> 31))) - 255;
        return (c2 & (c2 >> 31)) + 255;
    }

    public static String colorString(int v) {
        StringBuilder append = new StringBuilder().append("00000000");
        String hexString = Integer.toHexString(v);
        Log1F380D.a((Object) hexString);
        String sb = append.append(hexString).toString();
        return "#" + sb.substring(sb.length() - 8);
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

    public static int rgbaTocColor(float r, float g, float b, float a) {
        int clamp = clamp((int) (r * 255.0f));
        int clamp2 = clamp((int) (g * 255.0f));
        return (clamp((int) (255.0f * a)) << 24) | (clamp << 16) | (clamp2 << 8) | clamp((int) (b * 255.0f));
    }

    public void applyToWidget(MotionWidget view) {
        int i = this.mType;
        switch (i) {
            case TypedValues.Custom.TYPE_INT /*900*/:
            case TypedValues.Custom.TYPE_COLOR /*902*/:
            case TypedValues.Custom.TYPE_REFERENCE /*906*/:
                view.setCustomAttribute(this.mName, i, this.mIntegerValue);
                return;
            case TypedValues.Custom.TYPE_FLOAT /*901*/:
            case TypedValues.Custom.TYPE_DIMENSION /*905*/:
                view.setCustomAttribute(this.mName, i, this.mFloatValue);
                return;
            case TypedValues.Custom.TYPE_STRING /*903*/:
                view.setCustomAttribute(this.mName, i, this.mStringValue);
                return;
            case TypedValues.Custom.TYPE_BOOLEAN /*904*/:
                view.setCustomAttribute(this.mName, i, this.mBooleanValue);
                return;
            default:
                return;
        }
    }

    public CustomVariable copy() {
        return new CustomVariable(this);
    }

    public boolean diff(CustomVariable CustomAttribute) {
        int i;
        if (CustomAttribute == null || (i = this.mType) != CustomAttribute.mType) {
            return false;
        }
        switch (i) {
            case TypedValues.Custom.TYPE_INT /*900*/:
            case TypedValues.Custom.TYPE_REFERENCE /*906*/:
                return this.mIntegerValue == CustomAttribute.mIntegerValue;
            case TypedValues.Custom.TYPE_FLOAT /*901*/:
                return this.mFloatValue == CustomAttribute.mFloatValue;
            case TypedValues.Custom.TYPE_COLOR /*902*/:
                return this.mIntegerValue == CustomAttribute.mIntegerValue;
            case TypedValues.Custom.TYPE_STRING /*903*/:
                return this.mIntegerValue == CustomAttribute.mIntegerValue;
            case TypedValues.Custom.TYPE_BOOLEAN /*904*/:
                return this.mBooleanValue == CustomAttribute.mBooleanValue;
            case TypedValues.Custom.TYPE_DIMENSION /*905*/:
                return this.mFloatValue == CustomAttribute.mFloatValue;
            default:
                return false;
        }
    }

    public boolean getBooleanValue() {
        return this.mBooleanValue;
    }

    public int getColorValue() {
        return this.mIntegerValue;
    }

    public float getFloatValue() {
        return this.mFloatValue;
    }

    public int getIntegerValue() {
        return this.mIntegerValue;
    }

    public int getInterpolatedColor(float[] value) {
        int clamp = clamp((int) (((float) Math.pow((double) value[0], 0.45454545454545453d)) * 255.0f));
        int clamp2 = clamp((int) (((float) Math.pow((double) value[1], 0.45454545454545453d)) * 255.0f));
        return (clamp((int) (value[3] * 255.0f)) << 24) | (clamp << 16) | (clamp2 << 8) | clamp((int) (((float) Math.pow((double) value[2], 0.45454545454545453d)) * 255.0f));
    }

    public String getName() {
        return this.mName;
    }

    public String getStringValue() {
        return this.mStringValue;
    }

    public int getType() {
        return this.mType;
    }

    public float getValueToInterpolate() {
        switch (this.mType) {
            case TypedValues.Custom.TYPE_INT /*900*/:
                return (float) this.mIntegerValue;
            case TypedValues.Custom.TYPE_FLOAT /*901*/:
                return this.mFloatValue;
            case TypedValues.Custom.TYPE_COLOR /*902*/:
                throw new RuntimeException("Color does not have a single color to interpolate");
            case TypedValues.Custom.TYPE_STRING /*903*/:
                throw new RuntimeException("Cannot interpolate String");
            case TypedValues.Custom.TYPE_BOOLEAN /*904*/:
                return this.mBooleanValue ? 1.0f : 0.0f;
            case TypedValues.Custom.TYPE_DIMENSION /*905*/:
                return this.mFloatValue;
            default:
                return Float.NaN;
        }
    }

    public void getValuesToInterpolate(float[] ret) {
        switch (this.mType) {
            case TypedValues.Custom.TYPE_INT /*900*/:
                ret[0] = (float) this.mIntegerValue;
                return;
            case TypedValues.Custom.TYPE_FLOAT /*901*/:
                ret[0] = this.mFloatValue;
                return;
            case TypedValues.Custom.TYPE_COLOR /*902*/:
                int i = this.mIntegerValue;
                ret[0] = (float) Math.pow((double) (((float) ((i >> 16) & 255)) / 255.0f), 2.2d);
                ret[1] = (float) Math.pow((double) (((float) ((i >> 8) & 255)) / 255.0f), 2.2d);
                ret[2] = (float) Math.pow((double) (((float) (i & 255)) / 255.0f), 2.2d);
                ret[3] = ((float) ((i >> 24) & 255)) / 255.0f;
                return;
            case TypedValues.Custom.TYPE_STRING /*903*/:
                throw new RuntimeException("Cannot interpolate String");
            case TypedValues.Custom.TYPE_BOOLEAN /*904*/:
                ret[0] = this.mBooleanValue ? 1.0f : 0.0f;
                return;
            case TypedValues.Custom.TYPE_DIMENSION /*905*/:
                ret[0] = this.mFloatValue;
                return;
            default:
                return;
        }
    }

    public boolean isContinuous() {
        switch (this.mType) {
            case TypedValues.Custom.TYPE_STRING /*903*/:
            case TypedValues.Custom.TYPE_BOOLEAN /*904*/:
            case TypedValues.Custom.TYPE_REFERENCE /*906*/:
                return false;
            default:
                return true;
        }
    }

    public int numberOfInterpolatedValues() {
        switch (this.mType) {
            case TypedValues.Custom.TYPE_COLOR /*902*/:
                return 4;
            default:
                return 1;
        }
    }

    public void setBooleanValue(boolean value) {
        this.mBooleanValue = value;
    }

    public void setFloatValue(float value) {
        this.mFloatValue = value;
    }

    public void setIntValue(int value) {
        this.mIntegerValue = value;
    }

    public void setInterpolatedValue(MotionWidget view, float[] value) {
        int i = this.mType;
        boolean z = true;
        switch (i) {
            case TypedValues.Custom.TYPE_INT /*900*/:
                view.setCustomAttribute(this.mName, i, (int) value[0]);
                return;
            case TypedValues.Custom.TYPE_FLOAT /*901*/:
            case TypedValues.Custom.TYPE_DIMENSION /*905*/:
                view.setCustomAttribute(this.mName, i, value[0]);
                return;
            case TypedValues.Custom.TYPE_COLOR /*902*/:
                int clamp = clamp((int) (((float) Math.pow((double) value[0], 0.45454545454545453d)) * 255.0f));
                int clamp2 = clamp((int) (((float) Math.pow((double) value[1], 0.45454545454545453d)) * 255.0f));
                view.setCustomAttribute(this.mName, this.mType, (clamp((int) (value[3] * 255.0f)) << 24) | (clamp << 16) | (clamp2 << 8) | clamp((int) (((float) Math.pow((double) value[2], 0.45454545454545453d)) * 255.0f)));
                return;
            case TypedValues.Custom.TYPE_STRING /*903*/:
            case TypedValues.Custom.TYPE_REFERENCE /*906*/:
                throw new RuntimeException("unable to interpolate " + this.mName);
            case TypedValues.Custom.TYPE_BOOLEAN /*904*/:
                String str = this.mName;
                if (value[0] <= 0.5f) {
                    z = false;
                }
                view.setCustomAttribute(str, i, z);
                return;
            default:
                return;
        }
    }

    public void setStringValue(String value) {
        this.mStringValue = value;
    }

    public void setValue(Object value) {
        switch (this.mType) {
            case TypedValues.Custom.TYPE_INT /*900*/:
            case TypedValues.Custom.TYPE_REFERENCE /*906*/:
                this.mIntegerValue = ((Integer) value).intValue();
                return;
            case TypedValues.Custom.TYPE_FLOAT /*901*/:
                this.mFloatValue = ((Float) value).floatValue();
                return;
            case TypedValues.Custom.TYPE_COLOR /*902*/:
                this.mIntegerValue = ((Integer) value).intValue();
                return;
            case TypedValues.Custom.TYPE_STRING /*903*/:
                this.mStringValue = (String) value;
                return;
            case TypedValues.Custom.TYPE_BOOLEAN /*904*/:
                this.mBooleanValue = ((Boolean) value).booleanValue();
                return;
            case TypedValues.Custom.TYPE_DIMENSION /*905*/:
                this.mFloatValue = ((Float) value).floatValue();
                return;
            default:
                return;
        }
    }

    public void setValue(float[] value) {
        boolean z = true;
        switch (this.mType) {
            case TypedValues.Custom.TYPE_INT /*900*/:
            case TypedValues.Custom.TYPE_REFERENCE /*906*/:
                this.mIntegerValue = (int) value[0];
                return;
            case TypedValues.Custom.TYPE_FLOAT /*901*/:
            case TypedValues.Custom.TYPE_DIMENSION /*905*/:
                this.mFloatValue = value[0];
                return;
            case TypedValues.Custom.TYPE_COLOR /*902*/:
                this.mIntegerValue = ((Math.round(value[3] * 255.0f) & 255) << 24) | ((Math.round(((float) Math.pow((double) value[0], 0.5d)) * 255.0f) & 255) << 16) | ((Math.round(((float) Math.pow((double) value[1], 0.5d)) * 255.0f) & 255) << 8) | (Math.round(((float) Math.pow((double) value[2], 0.5d)) * 255.0f) & 255);
                return;
            case TypedValues.Custom.TYPE_STRING /*903*/:
                throw new RuntimeException("Cannot interpolate String");
            case TypedValues.Custom.TYPE_BOOLEAN /*904*/:
                if (((double) value[0]) <= 0.5d) {
                    z = false;
                }
                this.mBooleanValue = z;
                return;
            default:
                return;
        }
    }

    public String toString() {
        String str = this.mName + ':';
        switch (this.mType) {
            case TypedValues.Custom.TYPE_INT /*900*/:
                return str + this.mIntegerValue;
            case TypedValues.Custom.TYPE_FLOAT /*901*/:
                return str + this.mFloatValue;
            case TypedValues.Custom.TYPE_COLOR /*902*/:
                StringBuilder append = new StringBuilder().append(str);
                String colorString = colorString(this.mIntegerValue);
                Log1F380D.a((Object) colorString);
                return append.append(colorString).toString();
            case TypedValues.Custom.TYPE_STRING /*903*/:
                return str + this.mStringValue;
            case TypedValues.Custom.TYPE_BOOLEAN /*904*/:
                return str + Boolean.valueOf(this.mBooleanValue);
            case TypedValues.Custom.TYPE_DIMENSION /*905*/:
                return str + this.mFloatValue;
            default:
                return str + "????";
        }
    }
}

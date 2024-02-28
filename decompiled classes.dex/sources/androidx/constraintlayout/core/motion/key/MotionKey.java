package androidx.constraintlayout.core.motion.key;

import androidx.constraintlayout.core.motion.CustomVariable;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import java.util.HashMap;
import java.util.HashSet;

public abstract class MotionKey implements TypedValues {
    public static final String ALPHA = "alpha";
    public static final String CUSTOM = "CUSTOM";
    public static final String ELEVATION = "elevation";
    public static final String ROTATION = "rotationZ";
    public static final String ROTATION_X = "rotationX";
    public static final String SCALE_X = "scaleX";
    public static final String SCALE_Y = "scaleY";
    public static final String TRANSITION_PATH_ROTATE = "transitionPathRotate";
    public static final String TRANSLATION_X = "translationX";
    public static final String TRANSLATION_Y = "translationY";
    public static int UNSET = -1;
    public static final String VISIBILITY = "visibility";
    public HashMap<String, CustomVariable> mCustom;
    public int mFramePosition;
    int mTargetId;
    String mTargetString = null;
    public int mType;

    public MotionKey() {
        int i = UNSET;
        this.mFramePosition = i;
        this.mTargetId = i;
    }

    public abstract void addValues(HashMap<String, SplineSet> hashMap);

    public abstract MotionKey clone();

    public MotionKey copy(MotionKey src) {
        this.mFramePosition = src.mFramePosition;
        this.mTargetId = src.mTargetId;
        this.mTargetString = src.mTargetString;
        this.mType = src.mType;
        return this;
    }

    public abstract void getAttributeNames(HashSet<String> hashSet);

    public int getFramePosition() {
        return this.mFramePosition;
    }

    /* access modifiers changed from: package-private */
    public boolean matches(String constraintTag) {
        String str = this.mTargetString;
        if (str == null || constraintTag == null) {
            return false;
        }
        return constraintTag.matches(str);
    }

    public void setCustomAttribute(String name, int type, float value) {
        this.mCustom.put(name, new CustomVariable(name, type, value));
    }

    public void setCustomAttribute(String name, int type, int value) {
        this.mCustom.put(name, new CustomVariable(name, type, value));
    }

    public void setCustomAttribute(String name, int type, String value) {
        this.mCustom.put(name, new CustomVariable(name, type, value));
    }

    public void setCustomAttribute(String name, int type, boolean value) {
        this.mCustom.put(name, new CustomVariable(name, type, value));
    }

    public void setFramePosition(int pos) {
        this.mFramePosition = pos;
    }

    public void setInterpolation(HashMap<String, Integer> hashMap) {
    }

    public boolean setValue(int type, float value) {
        return false;
    }

    public boolean setValue(int type, int value) {
        switch (type) {
            case 100:
                this.mFramePosition = value;
                return true;
            default:
                return false;
        }
    }

    public boolean setValue(int type, String value) {
        switch (type) {
            case TypedValues.TYPE_TARGET /*101*/:
                this.mTargetString = value;
                return true;
            default:
                return false;
        }
    }

    public boolean setValue(int type, boolean value) {
        return false;
    }

    public MotionKey setViewId(int id) {
        this.mTargetId = id;
        return this;
    }

    /* access modifiers changed from: package-private */
    public boolean toBoolean(Object value) {
        return value instanceof Boolean ? ((Boolean) value).booleanValue() : Boolean.parseBoolean(value.toString());
    }

    /* access modifiers changed from: package-private */
    public float toFloat(Object value) {
        return value instanceof Float ? ((Float) value).floatValue() : Float.parseFloat(value.toString());
    }

    /* access modifiers changed from: package-private */
    public int toInt(Object value) {
        return value instanceof Integer ? ((Integer) value).intValue() : Integer.parseInt(value.toString());
    }
}

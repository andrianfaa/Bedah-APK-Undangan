package androidx.constraintlayout.core.motion;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.core.state.WidgetFrame;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import java.util.Set;

public class MotionWidget implements TypedValues {
    public static final int FILL_PARENT = -1;
    public static final int GONE_UNSET = Integer.MIN_VALUE;
    private static final int INTERNAL_MATCH_CONSTRAINT = -3;
    private static final int INTERNAL_MATCH_PARENT = -1;
    private static final int INTERNAL_WRAP_CONTENT = -2;
    private static final int INTERNAL_WRAP_CONTENT_CONSTRAINED = -4;
    public static final int INVISIBLE = 0;
    public static final int MATCH_CONSTRAINT = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    public static final int MATCH_PARENT = -1;
    public static final int PARENT_ID = 0;
    public static final int ROTATE_LEFT_OF_PORTRATE = 4;
    public static final int ROTATE_NONE = 0;
    public static final int ROTATE_PORTRATE_OF_LEFT = 2;
    public static final int ROTATE_PORTRATE_OF_RIGHT = 1;
    public static final int ROTATE_RIGHT_OF_PORTRATE = 3;
    public static final int UNSET = -1;
    public static final int VISIBILITY_MODE_IGNORE = 1;
    public static final int VISIBILITY_MODE_NORMAL = 0;
    public static final int VISIBLE = 4;
    public static final int WRAP_CONTENT = -2;
    private float mProgress;
    float mTransitionPathRotate;
    Motion motion = new Motion();
    PropertySet propertySet = new PropertySet();
    WidgetFrame widgetFrame = new WidgetFrame();

    public static class Motion {
        private static final int INTERPOLATOR_REFERENCE_ID = -2;
        private static final int INTERPOLATOR_UNDEFINED = -3;
        private static final int SPLINE_STRING = -1;
        public int mAnimateCircleAngleTo = 0;
        public int mAnimateRelativeTo = -1;
        public int mDrawPath = 0;
        public float mMotionStagger = Float.NaN;
        public int mPathMotionArc = -1;
        public float mPathRotate = Float.NaN;
        public int mPolarRelativeTo = -1;
        public int mQuantizeInterpolatorID = -1;
        public String mQuantizeInterpolatorString = null;
        public int mQuantizeInterpolatorType = -3;
        public float mQuantizeMotionPhase = Float.NaN;
        public int mQuantizeMotionSteps = -1;
        public String mTransitionEasing = null;
    }

    public static class PropertySet {
        public float alpha = 1.0f;
        public float mProgress = Float.NaN;
        public int mVisibilityMode = 0;
        public int visibility = 4;
    }

    public MotionWidget() {
    }

    public MotionWidget(WidgetFrame f) {
        this.widgetFrame = f;
    }

    public MotionWidget findViewById(int mTransformPivotTarget) {
        return null;
    }

    public float getAlpha() {
        return this.propertySet.alpha;
    }

    public int getBottom() {
        return this.widgetFrame.bottom;
    }

    public CustomVariable getCustomAttribute(String name) {
        return this.widgetFrame.getCustomAttribute(name);
    }

    public Set<String> getCustomAttributeNames() {
        return this.widgetFrame.getCustomAttributeNames();
    }

    public int getHeight() {
        return this.widgetFrame.bottom - this.widgetFrame.top;
    }

    public int getId(String name) {
        int id = TypedValues.AttributesType.getId(name);
        return id != -1 ? id : TypedValues.MotionType.getId(name);
    }

    public int getLeft() {
        return this.widgetFrame.left;
    }

    public String getName() {
        return this.widgetFrame.getId();
    }

    public MotionWidget getParent() {
        return null;
    }

    public float getPivotX() {
        return this.widgetFrame.pivotX;
    }

    public float getPivotY() {
        return this.widgetFrame.pivotY;
    }

    public int getRight() {
        return this.widgetFrame.right;
    }

    public float getRotationX() {
        return this.widgetFrame.rotationX;
    }

    public float getRotationY() {
        return this.widgetFrame.rotationY;
    }

    public float getRotationZ() {
        return this.widgetFrame.rotationZ;
    }

    public float getScaleX() {
        return this.widgetFrame.scaleX;
    }

    public float getScaleY() {
        return this.widgetFrame.scaleY;
    }

    public int getTop() {
        return this.widgetFrame.top;
    }

    public float getTranslationX() {
        return this.widgetFrame.translationX;
    }

    public float getTranslationY() {
        return this.widgetFrame.translationY;
    }

    public float getTranslationZ() {
        return this.widgetFrame.translationZ;
    }

    public float getValueAttributes(int id) {
        switch (id) {
            case 303:
                return this.widgetFrame.alpha;
            case 304:
                return this.widgetFrame.translationX;
            case 305:
                return this.widgetFrame.translationY;
            case 306:
                return this.widgetFrame.translationZ;
            case 308:
                return this.widgetFrame.rotationX;
            case 309:
                return this.widgetFrame.rotationY;
            case 310:
                return this.widgetFrame.rotationZ;
            case 311:
                return this.widgetFrame.scaleX;
            case 312:
                return this.widgetFrame.scaleY;
            case 313:
                return this.widgetFrame.pivotX;
            case 314:
                return this.widgetFrame.pivotY;
            case 315:
                return this.mProgress;
            case TypedValues.AttributesType.TYPE_PATH_ROTATE:
                return this.mTransitionPathRotate;
            default:
                return Float.NaN;
        }
    }

    public int getVisibility() {
        return this.propertySet.visibility;
    }

    public WidgetFrame getWidgetFrame() {
        return this.widgetFrame;
    }

    public int getWidth() {
        return this.widgetFrame.right - this.widgetFrame.left;
    }

    public int getX() {
        return this.widgetFrame.left;
    }

    public int getY() {
        return this.widgetFrame.top;
    }

    public void layout(int l, int t, int r, int b) {
        setBounds(l, t, r, b);
    }

    public void setBounds(int left, int top, int right, int bottom) {
        if (this.widgetFrame == null) {
            ConstraintWidget constraintWidget = null;
            this.widgetFrame = new WidgetFrame((ConstraintWidget) null);
        }
        this.widgetFrame.top = top;
        this.widgetFrame.left = left;
        this.widgetFrame.right = right;
        this.widgetFrame.bottom = bottom;
    }

    public void setCustomAttribute(String name, int type, float value) {
        this.widgetFrame.setCustomAttribute(name, type, value);
    }

    public void setCustomAttribute(String name, int type, int value) {
        this.widgetFrame.setCustomAttribute(name, type, value);
    }

    public void setCustomAttribute(String name, int type, String value) {
        this.widgetFrame.setCustomAttribute(name, type, value);
    }

    public void setCustomAttribute(String name, int type, boolean value) {
        this.widgetFrame.setCustomAttribute(name, type, value);
    }

    public void setInterpolatedValue(CustomAttribute attribute, float[] mCache) {
        this.widgetFrame.setCustomAttribute(attribute.mName, (int) TypedValues.Custom.TYPE_FLOAT, mCache[0]);
    }

    public void setPivotX(float px) {
        this.widgetFrame.pivotX = px;
    }

    public void setPivotY(float py) {
        this.widgetFrame.pivotY = py;
    }

    public void setRotationX(float rotationX) {
        this.widgetFrame.rotationX = rotationX;
    }

    public void setRotationY(float rotationY) {
        this.widgetFrame.rotationY = rotationY;
    }

    public void setRotationZ(float rotationZ) {
        this.widgetFrame.rotationZ = rotationZ;
    }

    public void setScaleX(float scaleX) {
        this.widgetFrame.scaleX = scaleX;
    }

    public void setScaleY(float scaleY) {
        this.widgetFrame.scaleY = scaleY;
    }

    public void setTranslationX(float translationX) {
        this.widgetFrame.translationX = translationX;
    }

    public void setTranslationY(float translationY) {
        this.widgetFrame.translationY = translationY;
    }

    public void setTranslationZ(float tz) {
        this.widgetFrame.translationZ = tz;
    }

    public boolean setValue(int id, float value) {
        if (setValueAttributes(id, value)) {
            return true;
        }
        return setValueMotion(id, value);
    }

    public boolean setValue(int id, int value) {
        return setValueAttributes(id, (float) value);
    }

    public boolean setValue(int id, String value) {
        return setValueMotion(id, value);
    }

    public boolean setValue(int id, boolean value) {
        return false;
    }

    public boolean setValueAttributes(int id, float value) {
        switch (id) {
            case 303:
                this.widgetFrame.alpha = value;
                return true;
            case 304:
                this.widgetFrame.translationX = value;
                return true;
            case 305:
                this.widgetFrame.translationY = value;
                return true;
            case 306:
                this.widgetFrame.translationZ = value;
                return true;
            case 308:
                this.widgetFrame.rotationX = value;
                return true;
            case 309:
                this.widgetFrame.rotationY = value;
                return true;
            case 310:
                this.widgetFrame.rotationZ = value;
                return true;
            case 311:
                this.widgetFrame.scaleX = value;
                return true;
            case 312:
                this.widgetFrame.scaleY = value;
                return true;
            case 313:
                this.widgetFrame.pivotX = value;
                return true;
            case 314:
                this.widgetFrame.pivotY = value;
                return true;
            case 315:
                this.mProgress = value;
                return true;
            case TypedValues.AttributesType.TYPE_PATH_ROTATE:
                this.mTransitionPathRotate = value;
                return true;
            default:
                return false;
        }
    }

    public boolean setValueMotion(int id, float value) {
        switch (id) {
            case 600:
                this.motion.mMotionStagger = value;
                return true;
            case 601:
                this.motion.mPathRotate = value;
                return true;
            case TypedValues.MotionType.TYPE_QUANTIZE_MOTION_PHASE:
                this.motion.mQuantizeMotionPhase = value;
                return true;
            default:
                return false;
        }
    }

    public boolean setValueMotion(int id, int value) {
        switch (id) {
            case TypedValues.MotionType.TYPE_ANIMATE_RELATIVE_TO:
                this.motion.mAnimateRelativeTo = value;
                return true;
            case TypedValues.MotionType.TYPE_ANIMATE_CIRCLEANGLE_TO:
                this.motion.mAnimateCircleAngleTo = value;
                return true;
            case TypedValues.MotionType.TYPE_PATHMOTION_ARC:
                this.motion.mPathMotionArc = value;
                return true;
            case TypedValues.MotionType.TYPE_DRAW_PATH:
                this.motion.mDrawPath = value;
                return true;
            case TypedValues.MotionType.TYPE_POLAR_RELATIVETO:
                this.motion.mPolarRelativeTo = value;
                return true;
            case TypedValues.MotionType.TYPE_QUANTIZE_MOTIONSTEPS:
                this.motion.mQuantizeMotionSteps = value;
                return true;
            case TypedValues.MotionType.TYPE_QUANTIZE_INTERPOLATOR_TYPE:
                this.motion.mQuantizeInterpolatorType = value;
                return true;
            case TypedValues.MotionType.TYPE_QUANTIZE_INTERPOLATOR_ID:
                this.motion.mQuantizeInterpolatorID = value;
                return true;
            default:
                return false;
        }
    }

    public boolean setValueMotion(int id, String value) {
        switch (id) {
            case TypedValues.MotionType.TYPE_EASING:
                this.motion.mTransitionEasing = value;
                return true;
            case TypedValues.MotionType.TYPE_QUANTIZE_INTERPOLATOR:
                this.motion.mQuantizeInterpolatorString = value;
                return true;
            default:
                return false;
        }
    }

    public void setVisibility(int visibility) {
        this.propertySet.visibility = visibility;
    }

    public String toString() {
        return this.widgetFrame.left + ", " + this.widgetFrame.top + ", " + this.widgetFrame.right + ", " + this.widgetFrame.bottom;
    }
}

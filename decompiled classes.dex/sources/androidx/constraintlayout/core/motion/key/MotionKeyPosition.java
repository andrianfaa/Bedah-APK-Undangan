package androidx.constraintlayout.core.motion.key;

import androidx.constraintlayout.core.motion.MotionWidget;
import androidx.constraintlayout.core.motion.utils.FloatRect;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import java.util.HashMap;
import java.util.HashSet;

public class MotionKeyPosition extends MotionKey {
    static final int KEY_TYPE = 2;
    static final String NAME = "KeyPosition";
    protected static final float SELECTION_SLOPE = 20.0f;
    public static final int TYPE_CARTESIAN = 0;
    public static final int TYPE_PATH = 1;
    public static final int TYPE_SCREEN = 2;
    public float mAltPercentX = Float.NaN;
    public float mAltPercentY = Float.NaN;
    private float mCalculatedPositionX = Float.NaN;
    private float mCalculatedPositionY = Float.NaN;
    public int mCurveFit = UNSET;
    public int mDrawPath = 0;
    public int mPathMotionArc = UNSET;
    public float mPercentHeight = Float.NaN;
    public float mPercentWidth = Float.NaN;
    public float mPercentX = Float.NaN;
    public float mPercentY = Float.NaN;
    public int mPositionType = 0;
    public String mTransitionEasing = null;

    public MotionKeyPosition() {
        this.mType = 2;
    }

    private void calcCartesianPosition(float start_x, float start_y, float end_x, float end_y) {
        float f = end_x - start_x;
        float f2 = end_y - start_y;
        float f3 = 0.0f;
        float f4 = Float.isNaN(this.mPercentX) ? 0.0f : this.mPercentX;
        float f5 = Float.isNaN(this.mAltPercentY) ? 0.0f : this.mAltPercentY;
        float f6 = Float.isNaN(this.mPercentY) ? 0.0f : this.mPercentY;
        if (!Float.isNaN(this.mAltPercentX)) {
            f3 = this.mAltPercentX;
        }
        this.mCalculatedPositionX = (float) ((int) ((f * f4) + start_x + (f2 * f3)));
        this.mCalculatedPositionY = (float) ((int) ((f * f5) + start_y + (f2 * f6)));
    }

    private void calcPathPosition(float start_x, float start_y, float end_x, float end_y) {
        float f = end_x - start_x;
        float f2 = end_y - start_y;
        float f3 = this.mPercentX;
        float f4 = this.mPercentY;
        this.mCalculatedPositionX = (f * f3) + start_x + ((-f2) * f4);
        this.mCalculatedPositionY = (f3 * f2) + start_y + (f4 * f);
    }

    private void calcScreenPosition(int layoutWidth, int layoutHeight) {
        float f = this.mPercentX;
        this.mCalculatedPositionX = (((float) (layoutWidth - 0)) * f) + ((float) (0 / 2));
        this.mCalculatedPositionY = (((float) (layoutHeight - 0)) * f) + ((float) (0 / 2));
    }

    public void addValues(HashMap<String, SplineSet> hashMap) {
    }

    /* access modifiers changed from: package-private */
    public void calcPosition(int layoutWidth, int layoutHeight, float start_x, float start_y, float end_x, float end_y) {
        switch (this.mPositionType) {
            case 1:
                calcPathPosition(start_x, start_y, end_x, end_y);
                return;
            case 2:
                calcScreenPosition(layoutWidth, layoutHeight);
                return;
            default:
                calcCartesianPosition(start_x, start_y, end_x, end_y);
                return;
        }
    }

    public MotionKey clone() {
        return new MotionKeyPosition().copy(this);
    }

    public MotionKey copy(MotionKey src) {
        super.copy(src);
        MotionKeyPosition motionKeyPosition = (MotionKeyPosition) src;
        this.mTransitionEasing = motionKeyPosition.mTransitionEasing;
        this.mPathMotionArc = motionKeyPosition.mPathMotionArc;
        this.mDrawPath = motionKeyPosition.mDrawPath;
        this.mPercentWidth = motionKeyPosition.mPercentWidth;
        this.mPercentHeight = Float.NaN;
        this.mPercentX = motionKeyPosition.mPercentX;
        this.mPercentY = motionKeyPosition.mPercentY;
        this.mAltPercentX = motionKeyPosition.mAltPercentX;
        this.mAltPercentY = motionKeyPosition.mAltPercentY;
        this.mCalculatedPositionX = motionKeyPosition.mCalculatedPositionX;
        this.mCalculatedPositionY = motionKeyPosition.mCalculatedPositionY;
        return this;
    }

    public void getAttributeNames(HashSet<String> hashSet) {
    }

    public int getId(String name) {
        return TypedValues.PositionType.getId(name);
    }

    /* access modifiers changed from: package-private */
    public float getPositionX() {
        return this.mCalculatedPositionX;
    }

    /* access modifiers changed from: package-private */
    public float getPositionY() {
        return this.mCalculatedPositionY;
    }

    public boolean intersects(int layoutWidth, int layoutHeight, FloatRect start, FloatRect end, float x, float y) {
        calcPosition(layoutWidth, layoutHeight, start.centerX(), start.centerY(), end.centerX(), end.centerY());
        return Math.abs(x - this.mCalculatedPositionX) < SELECTION_SLOPE && Math.abs(y - this.mCalculatedPositionY) < SELECTION_SLOPE;
    }

    public void positionAttributes(MotionWidget view, FloatRect start, FloatRect end, float x, float y, String[] attribute, float[] value) {
        switch (this.mPositionType) {
            case 1:
                positionPathAttributes(start, end, x, y, attribute, value);
                return;
            case 2:
                positionScreenAttributes(view, start, end, x, y, attribute, value);
                return;
            default:
                positionCartAttributes(start, end, x, y, attribute, value);
                return;
        }
    }

    /* access modifiers changed from: package-private */
    public void positionCartAttributes(FloatRect start, FloatRect end, float x, float y, String[] attribute, float[] value) {
        float centerX = start.centerX();
        float centerY = start.centerY();
        float centerX2 = end.centerX() - centerX;
        float centerY2 = end.centerY() - centerY;
        if (attribute[0] == null) {
            attribute[0] = "percentX";
            value[0] = (x - centerX) / centerX2;
            attribute[1] = "percentY";
            value[1] = (y - centerY) / centerY2;
        } else if ("percentX".equals(attribute[0])) {
            value[0] = (x - centerX) / centerX2;
            value[1] = (y - centerY) / centerY2;
        } else {
            value[1] = (x - centerX) / centerX2;
            value[0] = (y - centerY) / centerY2;
        }
    }

    /* access modifiers changed from: package-private */
    public void positionPathAttributes(FloatRect start, FloatRect end, float x, float y, String[] attribute, float[] value) {
        float centerX = start.centerX();
        float centerY = start.centerY();
        float centerX2 = end.centerX() - centerX;
        float centerY2 = end.centerY() - centerY;
        float hypot = (float) Math.hypot((double) centerX2, (double) centerY2);
        if (((double) hypot) < 1.0E-4d) {
            System.out.println("distance ~ 0");
            value[0] = 0.0f;
            value[1] = 0.0f;
            return;
        }
        float f = centerX2 / hypot;
        float f2 = centerY2 / hypot;
        float f3 = (((y - centerY) * f) - ((x - centerX) * f2)) / hypot;
        float f4 = (((x - centerX) * f) + ((y - centerY) * f2)) / hypot;
        if (attribute[0] == null) {
            attribute[0] = "percentX";
            attribute[1] = "percentY";
            value[0] = f4;
            value[1] = f3;
        } else if ("percentX".equals(attribute[0])) {
            value[0] = f4;
            value[1] = f3;
        }
    }

    /* access modifiers changed from: package-private */
    public void positionScreenAttributes(MotionWidget view, FloatRect start, FloatRect end, float x, float y, String[] attribute, float[] value) {
        float centerX = start.centerX();
        float centerY = start.centerY();
        float centerX2 = end.centerX() - centerX;
        float centerY2 = end.centerY() - centerY;
        MotionWidget parent = view.getParent();
        int width = parent.getWidth();
        int height = parent.getHeight();
        if (attribute[0] == null) {
            attribute[0] = "percentX";
            value[0] = x / ((float) width);
            attribute[1] = "percentY";
            value[1] = y / ((float) height);
        } else if ("percentX".equals(attribute[0])) {
            value[0] = x / ((float) width);
            value[1] = y / ((float) height);
        } else {
            value[1] = x / ((float) width);
            value[0] = y / ((float) height);
        }
    }

    public boolean setValue(int type, float value) {
        switch (type) {
            case TypedValues.PositionType.TYPE_PERCENT_WIDTH /*503*/:
                this.mPercentWidth = value;
                return true;
            case TypedValues.PositionType.TYPE_PERCENT_HEIGHT /*504*/:
                this.mPercentHeight = value;
                return true;
            case TypedValues.PositionType.TYPE_SIZE_PERCENT /*505*/:
                this.mPercentWidth = value;
                this.mPercentHeight = value;
                return true;
            case TypedValues.PositionType.TYPE_PERCENT_X /*506*/:
                this.mPercentX = value;
                return true;
            case TypedValues.PositionType.TYPE_PERCENT_Y /*507*/:
                this.mPercentY = value;
                return true;
            default:
                return super.setValue(type, value);
        }
    }

    public boolean setValue(int type, int value) {
        switch (type) {
            case 100:
                this.mFramePosition = value;
                return true;
            case TypedValues.PositionType.TYPE_CURVE_FIT /*508*/:
                this.mCurveFit = value;
                return true;
            case TypedValues.PositionType.TYPE_POSITION_TYPE /*510*/:
                this.mPositionType = value;
                return true;
            default:
                return super.setValue(type, value);
        }
    }

    public boolean setValue(int type, String value) {
        switch (type) {
            case TypedValues.PositionType.TYPE_TRANSITION_EASING /*501*/:
                this.mTransitionEasing = value.toString();
                return true;
            default:
                return super.setValue(type, value);
        }
    }
}

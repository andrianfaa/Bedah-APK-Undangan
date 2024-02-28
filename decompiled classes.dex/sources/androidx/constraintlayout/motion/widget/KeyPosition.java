package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.motion.utils.ViewSpline;
import androidx.constraintlayout.widget.R;
import java.util.HashMap;
import mt.Log1F380D;

public class KeyPosition extends KeyPositionBase {
    public static final String DRAWPATH = "drawPath";
    static final int KEY_TYPE = 2;
    static final String NAME = "KeyPosition";
    public static final String PERCENT_HEIGHT = "percentHeight";
    public static final String PERCENT_WIDTH = "percentWidth";
    public static final String PERCENT_X = "percentX";
    public static final String PERCENT_Y = "percentY";
    public static final String SIZE_PERCENT = "sizePercent";
    private static final String TAG = "KeyPosition";
    public static final String TRANSITION_EASING = "transitionEasing";
    public static final int TYPE_CARTESIAN = 0;
    public static final int TYPE_PATH = 1;
    public static final int TYPE_SCREEN = 2;
    float mAltPercentX = Float.NaN;
    float mAltPercentY = Float.NaN;
    private float mCalculatedPositionX = Float.NaN;
    private float mCalculatedPositionY = Float.NaN;
    int mDrawPath = 0;
    int mPathMotionArc = UNSET;
    float mPercentHeight = Float.NaN;
    float mPercentWidth = Float.NaN;
    float mPercentX = Float.NaN;
    float mPercentY = Float.NaN;
    int mPositionType = 0;
    String mTransitionEasing = null;

    /* compiled from: 001C */
    private static class Loader {
        private static final int CURVE_FIT = 4;
        private static final int DRAW_PATH = 5;
        private static final int FRAME_POSITION = 2;
        private static final int PATH_MOTION_ARC = 10;
        private static final int PERCENT_HEIGHT = 12;
        private static final int PERCENT_WIDTH = 11;
        private static final int PERCENT_X = 6;
        private static final int PERCENT_Y = 7;
        private static final int SIZE_PERCENT = 8;
        private static final int TARGET_ID = 1;
        private static final int TRANSITION_EASING = 3;
        private static final int TYPE = 9;
        private static SparseIntArray mAttrMap;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            mAttrMap = sparseIntArray;
            sparseIntArray.append(R.styleable.KeyPosition_motionTarget, 1);
            mAttrMap.append(R.styleable.KeyPosition_framePosition, 2);
            mAttrMap.append(R.styleable.KeyPosition_transitionEasing, 3);
            mAttrMap.append(R.styleable.KeyPosition_curveFit, 4);
            mAttrMap.append(R.styleable.KeyPosition_drawPath, 5);
            mAttrMap.append(R.styleable.KeyPosition_percentX, 6);
            mAttrMap.append(R.styleable.KeyPosition_percentY, 7);
            mAttrMap.append(R.styleable.KeyPosition_keyPositionType, 9);
            mAttrMap.append(R.styleable.KeyPosition_sizePercent, 8);
            mAttrMap.append(R.styleable.KeyPosition_percentWidth, 11);
            mAttrMap.append(R.styleable.KeyPosition_percentHeight, 12);
            mAttrMap.append(R.styleable.KeyPosition_pathMotionArc, 10);
        }

        private Loader() {
        }

        /* access modifiers changed from: private */
        public static void read(KeyPosition c, TypedArray a) {
            int indexCount = a.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = a.getIndex(i);
                switch (mAttrMap.get(index)) {
                    case 1:
                        if (!MotionLayout.IS_IN_EDIT_MODE) {
                            if (a.peekValue(index).type != 3) {
                                c.mTargetId = a.getResourceId(index, c.mTargetId);
                                break;
                            } else {
                                c.mTargetString = a.getString(index);
                                break;
                            }
                        } else {
                            c.mTargetId = a.getResourceId(index, c.mTargetId);
                            if (c.mTargetId != -1) {
                                break;
                            } else {
                                c.mTargetString = a.getString(index);
                                break;
                            }
                        }
                    case 2:
                        c.mFramePosition = a.getInt(index, c.mFramePosition);
                        break;
                    case 3:
                        if (a.peekValue(index).type != 3) {
                            c.mTransitionEasing = Easing.NAMED_EASING[a.getInteger(index, 0)];
                            break;
                        } else {
                            c.mTransitionEasing = a.getString(index);
                            break;
                        }
                    case 4:
                        c.mCurveFit = a.getInteger(index, c.mCurveFit);
                        break;
                    case 5:
                        c.mDrawPath = a.getInt(index, c.mDrawPath);
                        break;
                    case 6:
                        c.mPercentX = a.getFloat(index, c.mPercentX);
                        break;
                    case 7:
                        c.mPercentY = a.getFloat(index, c.mPercentY);
                        break;
                    case 8:
                        float f = a.getFloat(index, c.mPercentHeight);
                        c.mPercentWidth = f;
                        c.mPercentHeight = f;
                        break;
                    case 9:
                        c.mPositionType = a.getInt(index, c.mPositionType);
                        break;
                    case 10:
                        c.mPathMotionArc = a.getInt(index, c.mPathMotionArc);
                        break;
                    case 11:
                        c.mPercentWidth = a.getFloat(index, c.mPercentWidth);
                        break;
                    case 12:
                        c.mPercentHeight = a.getFloat(index, c.mPercentHeight);
                        break;
                    default:
                        StringBuilder append = new StringBuilder().append("unused attribute 0x");
                        String hexString = Integer.toHexString(index);
                        Log1F380D.a((Object) hexString);
                        Log.e(TypedValues.PositionType.NAME, append.append(hexString).append("   ").append(mAttrMap.get(index)).toString());
                        break;
                }
            }
            if (c.mFramePosition == -1) {
                Log.e(TypedValues.PositionType.NAME, "no frame position");
            }
        }
    }

    public KeyPosition() {
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

    public void addValues(HashMap<String, ViewSpline> hashMap) {
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

    public Key clone() {
        return new KeyPosition().copy(this);
    }

    public Key copy(Key src) {
        super.copy(src);
        KeyPosition keyPosition = (KeyPosition) src;
        this.mTransitionEasing = keyPosition.mTransitionEasing;
        this.mPathMotionArc = keyPosition.mPathMotionArc;
        this.mDrawPath = keyPosition.mDrawPath;
        this.mPercentWidth = keyPosition.mPercentWidth;
        this.mPercentHeight = Float.NaN;
        this.mPercentX = keyPosition.mPercentX;
        this.mPercentY = keyPosition.mPercentY;
        this.mAltPercentX = keyPosition.mAltPercentX;
        this.mAltPercentY = keyPosition.mAltPercentY;
        this.mCalculatedPositionX = keyPosition.mCalculatedPositionX;
        this.mCalculatedPositionY = keyPosition.mCalculatedPositionY;
        return this;
    }

    /* access modifiers changed from: package-private */
    public float getPositionX() {
        return this.mCalculatedPositionX;
    }

    /* access modifiers changed from: package-private */
    public float getPositionY() {
        return this.mCalculatedPositionY;
    }

    public boolean intersects(int layoutWidth, int layoutHeight, RectF start, RectF end, float x, float y) {
        calcPosition(layoutWidth, layoutHeight, start.centerX(), start.centerY(), end.centerX(), end.centerY());
        return Math.abs(x - this.mCalculatedPositionX) < 20.0f && Math.abs(y - this.mCalculatedPositionY) < 20.0f;
    }

    public void load(Context context, AttributeSet attrs) {
        Loader.read(this, context.obtainStyledAttributes(attrs, R.styleable.KeyPosition));
    }

    public void positionAttributes(View view, RectF start, RectF end, float x, float y, String[] attribute, float[] value) {
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
    public void positionCartAttributes(RectF start, RectF end, float x, float y, String[] attribute, float[] value) {
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
    public void positionPathAttributes(RectF start, RectF end, float x, float y, String[] attribute, float[] value) {
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
    public void positionScreenAttributes(View view, RectF start, RectF end, float x, float y, String[] attribute, float[] value) {
        float centerX = start.centerX();
        float centerY = start.centerY();
        float centerX2 = end.centerX() - centerX;
        float centerY2 = end.centerY() - centerY;
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        int width = viewGroup.getWidth();
        int height = viewGroup.getHeight();
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

    public void setType(int type) {
        this.mPositionType = type;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setValue(java.lang.String r2, java.lang.Object r3) {
        /*
            r1 = this;
            int r0 = r2.hashCode()
            switch(r0) {
                case -1812823328: goto L_0x0045;
                case -1127236479: goto L_0x003b;
                case -1017587252: goto L_0x0031;
                case -827014263: goto L_0x0027;
                case -200259324: goto L_0x001c;
                case 428090547: goto L_0x0012;
                case 428090548: goto L_0x0008;
                default: goto L_0x0007;
            }
        L_0x0007:
            goto L_0x0050
        L_0x0008:
            java.lang.String r0 = "percentY"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 6
            goto L_0x0051
        L_0x0012:
            java.lang.String r0 = "percentX"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 5
            goto L_0x0051
        L_0x001c:
            java.lang.String r0 = "sizePercent"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 4
            goto L_0x0051
        L_0x0027:
            java.lang.String r0 = "drawPath"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 1
            goto L_0x0051
        L_0x0031:
            java.lang.String r0 = "percentHeight"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 3
            goto L_0x0051
        L_0x003b:
            java.lang.String r0 = "percentWidth"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 2
            goto L_0x0051
        L_0x0045:
            java.lang.String r0 = "transitionEasing"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 0
            goto L_0x0051
        L_0x0050:
            r0 = -1
        L_0x0051:
            switch(r0) {
                case 0: goto L_0x0081;
                case 1: goto L_0x007a;
                case 2: goto L_0x0073;
                case 3: goto L_0x006c;
                case 4: goto L_0x0063;
                case 5: goto L_0x005c;
                case 6: goto L_0x0055;
                default: goto L_0x0054;
            }
        L_0x0054:
            goto L_0x0088
        L_0x0055:
            float r0 = r1.toFloat(r3)
            r1.mPercentY = r0
            goto L_0x0088
        L_0x005c:
            float r0 = r1.toFloat(r3)
            r1.mPercentX = r0
            goto L_0x0088
        L_0x0063:
            float r0 = r1.toFloat(r3)
            r1.mPercentWidth = r0
            r1.mPercentHeight = r0
            goto L_0x0088
        L_0x006c:
            float r0 = r1.toFloat(r3)
            r1.mPercentHeight = r0
            goto L_0x0088
        L_0x0073:
            float r0 = r1.toFloat(r3)
            r1.mPercentWidth = r0
            goto L_0x0088
        L_0x007a:
            int r0 = r1.toInt(r3)
            r1.mDrawPath = r0
            goto L_0x0088
        L_0x0081:
            java.lang.String r0 = r3.toString()
            r1.mTransitionEasing = r0
        L_0x0088:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.KeyPosition.setValue(java.lang.String, java.lang.Object):void");
    }
}

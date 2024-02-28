package androidx.constraintlayout.core.motion.key;

import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import java.util.HashMap;
import java.util.HashSet;

public class MotionKeyTimeCycle extends MotionKey {
    public static final int KEY_TYPE = 3;
    static final String NAME = "KeyTimeCycle";
    private static final String TAG = "KeyTimeCycle";
    private float mAlpha = Float.NaN;
    private int mCurveFit = -1;
    private String mCustomWaveShape = null;
    private float mElevation = Float.NaN;
    private float mProgress = Float.NaN;
    private float mRotation = Float.NaN;
    private float mRotationX = Float.NaN;
    private float mRotationY = Float.NaN;
    private float mScaleX = Float.NaN;
    private float mScaleY = Float.NaN;
    private String mTransitionEasing;
    private float mTransitionPathRotate = Float.NaN;
    private float mTranslationX = Float.NaN;
    private float mTranslationY = Float.NaN;
    private float mTranslationZ = Float.NaN;
    private float mWaveOffset = 0.0f;
    private float mWavePeriod = Float.NaN;
    private int mWaveShape = 0;

    public MotionKeyTimeCycle() {
        this.mType = 3;
        this.mCustom = new HashMap();
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00d5, code lost:
        if (r1.equals("rotationX") != false) goto L_0x00d9;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addTimeValues(java.util.HashMap<java.lang.String, androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet> r12) {
        /*
            r11 = this;
            java.util.Set r0 = r12.keySet()
            java.util.Iterator r0 = r0.iterator()
        L_0x0008:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0218
            java.lang.Object r1 = r0.next()
            java.lang.String r1 = (java.lang.String) r1
            java.lang.Object r2 = r12.get(r1)
            androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet r2 = (androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet) r2
            if (r2 != 0) goto L_0x001d
            goto L_0x0008
        L_0x001d:
            java.lang.String r3 = "CUSTOM"
            boolean r4 = r1.startsWith(r3)
            r5 = 1
            if (r4 == 0) goto L_0x0049
            int r3 = r3.length()
            int r3 = r3 + r5
            java.lang.String r3 = r1.substring(r3)
            java.util.HashMap r4 = r11.mCustom
            java.lang.Object r4 = r4.get(r3)
            androidx.constraintlayout.core.motion.CustomVariable r4 = (androidx.constraintlayout.core.motion.CustomVariable) r4
            if (r4 == 0) goto L_0x0008
            r5 = r2
            androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet$CustomVarSet r5 = (androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet.CustomVarSet) r5
            int r6 = r11.mFramePosition
            float r8 = r11.mWavePeriod
            int r9 = r11.mWaveShape
            float r10 = r11.mWaveOffset
            r7 = r4
            r5.setPoint((int) r6, (androidx.constraintlayout.core.motion.CustomVariable) r7, (float) r8, (int) r9, (float) r10)
            goto L_0x0008
        L_0x0049:
            r3 = -1
            int r4 = r1.hashCode()
            switch(r4) {
                case -1249320806: goto L_0x00ce;
                case -1249320805: goto L_0x00c3;
                case -1249320804: goto L_0x00b8;
                case -1225497657: goto L_0x00ad;
                case -1225497656: goto L_0x00a1;
                case -1225497655: goto L_0x0095;
                case -1001078227: goto L_0x008a;
                case -908189618: goto L_0x007f;
                case -908189617: goto L_0x0074;
                case -4379043: goto L_0x0069;
                case 92909918: goto L_0x005e;
                case 803192288: goto L_0x0053;
                default: goto L_0x0051;
            }
        L_0x0051:
            goto L_0x00d8
        L_0x0053:
            java.lang.String r4 = "pathRotate"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 4
            goto L_0x00d9
        L_0x005e:
            java.lang.String r4 = "alpha"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 0
            goto L_0x00d9
        L_0x0069:
            java.lang.String r4 = "elevation"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 10
            goto L_0x00d9
        L_0x0074:
            java.lang.String r4 = "scaleY"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 6
            goto L_0x00d9
        L_0x007f:
            java.lang.String r4 = "scaleX"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 5
            goto L_0x00d9
        L_0x008a:
            java.lang.String r4 = "progress"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 11
            goto L_0x00d9
        L_0x0095:
            java.lang.String r4 = "translationZ"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 9
            goto L_0x00d9
        L_0x00a1:
            java.lang.String r4 = "translationY"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 8
            goto L_0x00d9
        L_0x00ad:
            java.lang.String r4 = "translationX"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 7
            goto L_0x00d9
        L_0x00b8:
            java.lang.String r4 = "rotationZ"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 3
            goto L_0x00d9
        L_0x00c3:
            java.lang.String r4 = "rotationY"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 2
            goto L_0x00d9
        L_0x00ce:
            java.lang.String r4 = "rotationX"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            goto L_0x00d9
        L_0x00d8:
            r5 = r3
        L_0x00d9:
            switch(r5) {
                case 0: goto L_0x0200;
                case 1: goto L_0x01e9;
                case 2: goto L_0x01d2;
                case 3: goto L_0x01bb;
                case 4: goto L_0x01a4;
                case 5: goto L_0x018c;
                case 6: goto L_0x0174;
                case 7: goto L_0x015c;
                case 8: goto L_0x0144;
                case 9: goto L_0x012c;
                case 10: goto L_0x0114;
                case 11: goto L_0x00fc;
                default: goto L_0x00dc;
            }
        L_0x00dc:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "UNKNOWN addValues \""
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r1)
            java.lang.String r4 = "\""
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            java.lang.String r4 = "KeyTimeCycles"
            androidx.constraintlayout.core.motion.utils.Utils.loge(r4, r3)
            goto L_0x0216
        L_0x00fc:
            float r3 = r11.mProgress
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0216
            int r4 = r11.mFramePosition
            float r5 = r11.mProgress
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0216
        L_0x0114:
            float r3 = r11.mTranslationZ
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0216
            int r4 = r11.mFramePosition
            float r5 = r11.mTranslationZ
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0216
        L_0x012c:
            float r3 = r11.mTranslationZ
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0216
            int r4 = r11.mFramePosition
            float r5 = r11.mTranslationZ
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0216
        L_0x0144:
            float r3 = r11.mTranslationY
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0216
            int r4 = r11.mFramePosition
            float r5 = r11.mTranslationY
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0216
        L_0x015c:
            float r3 = r11.mTranslationX
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0216
            int r4 = r11.mFramePosition
            float r5 = r11.mTranslationX
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0216
        L_0x0174:
            float r3 = r11.mScaleY
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0216
            int r4 = r11.mFramePosition
            float r5 = r11.mScaleY
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0216
        L_0x018c:
            float r3 = r11.mScaleX
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0216
            int r4 = r11.mFramePosition
            float r5 = r11.mScaleX
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0216
        L_0x01a4:
            float r3 = r11.mTransitionPathRotate
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0216
            int r4 = r11.mFramePosition
            float r5 = r11.mTransitionPathRotate
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0216
        L_0x01bb:
            float r3 = r11.mRotation
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0216
            int r4 = r11.mFramePosition
            float r5 = r11.mRotation
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0216
        L_0x01d2:
            float r3 = r11.mRotationY
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0216
            int r4 = r11.mFramePosition
            float r5 = r11.mRotationY
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0216
        L_0x01e9:
            float r3 = r11.mRotationX
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0216
            int r4 = r11.mFramePosition
            float r5 = r11.mRotationX
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0216
        L_0x0200:
            float r3 = r11.mAlpha
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0216
            int r4 = r11.mFramePosition
            float r5 = r11.mAlpha
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
        L_0x0216:
            goto L_0x0008
        L_0x0218:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.motion.key.MotionKeyTimeCycle.addTimeValues(java.util.HashMap):void");
    }

    public void addValues(HashMap<String, SplineSet> hashMap) {
    }

    public MotionKey clone() {
        return new MotionKeyTimeCycle().copy((MotionKey) this);
    }

    public MotionKeyTimeCycle copy(MotionKey src) {
        super.copy(src);
        MotionKeyTimeCycle motionKeyTimeCycle = (MotionKeyTimeCycle) src;
        this.mTransitionEasing = motionKeyTimeCycle.mTransitionEasing;
        this.mCurveFit = motionKeyTimeCycle.mCurveFit;
        this.mWaveShape = motionKeyTimeCycle.mWaveShape;
        this.mWavePeriod = motionKeyTimeCycle.mWavePeriod;
        this.mWaveOffset = motionKeyTimeCycle.mWaveOffset;
        this.mProgress = motionKeyTimeCycle.mProgress;
        this.mAlpha = motionKeyTimeCycle.mAlpha;
        this.mElevation = motionKeyTimeCycle.mElevation;
        this.mRotation = motionKeyTimeCycle.mRotation;
        this.mTransitionPathRotate = motionKeyTimeCycle.mTransitionPathRotate;
        this.mRotationX = motionKeyTimeCycle.mRotationX;
        this.mRotationY = motionKeyTimeCycle.mRotationY;
        this.mScaleX = motionKeyTimeCycle.mScaleX;
        this.mScaleY = motionKeyTimeCycle.mScaleY;
        this.mTranslationX = motionKeyTimeCycle.mTranslationX;
        this.mTranslationY = motionKeyTimeCycle.mTranslationY;
        this.mTranslationZ = motionKeyTimeCycle.mTranslationZ;
        return this;
    }

    public void getAttributeNames(HashSet<String> hashSet) {
        if (!Float.isNaN(this.mAlpha)) {
            hashSet.add("alpha");
        }
        if (!Float.isNaN(this.mElevation)) {
            hashSet.add("elevation");
        }
        if (!Float.isNaN(this.mRotation)) {
            hashSet.add("rotationZ");
        }
        if (!Float.isNaN(this.mRotationX)) {
            hashSet.add("rotationX");
        }
        if (!Float.isNaN(this.mRotationY)) {
            hashSet.add("rotationY");
        }
        if (!Float.isNaN(this.mScaleX)) {
            hashSet.add("scaleX");
        }
        if (!Float.isNaN(this.mScaleY)) {
            hashSet.add("scaleY");
        }
        if (!Float.isNaN(this.mTransitionPathRotate)) {
            hashSet.add("pathRotate");
        }
        if (!Float.isNaN(this.mTranslationX)) {
            hashSet.add("translationX");
        }
        if (!Float.isNaN(this.mTranslationY)) {
            hashSet.add("translationY");
        }
        if (!Float.isNaN(this.mTranslationZ)) {
            hashSet.add("translationZ");
        }
        if (this.mCustom.size() > 0) {
            for (String str : this.mCustom.keySet()) {
                hashSet.add("CUSTOM," + str);
            }
        }
    }

    public int getId(String name) {
        return TypedValues.CycleType.getId(name);
    }

    public boolean setValue(int type, float value) {
        switch (type) {
            case 304:
                this.mTranslationX = toFloat(Float.valueOf(value));
                return true;
            case 305:
                this.mTranslationY = toFloat(Float.valueOf(value));
                return true;
            case 306:
                this.mTranslationZ = toFloat(Float.valueOf(value));
                return true;
            case 307:
                this.mElevation = toFloat(Float.valueOf(value));
                return true;
            case 308:
                this.mRotationX = toFloat(Float.valueOf(value));
                return true;
            case 309:
                this.mRotationY = toFloat(Float.valueOf(value));
                return true;
            case 310:
                this.mRotation = toFloat(Float.valueOf(value));
                return true;
            case 311:
                this.mScaleX = toFloat(Float.valueOf(value));
                return true;
            case 312:
                this.mScaleY = toFloat(Float.valueOf(value));
                return true;
            case 315:
                this.mProgress = toFloat(Float.valueOf(value));
                return true;
            case TypedValues.CycleType.TYPE_CURVE_FIT /*401*/:
                this.mCurveFit = toInt(Float.valueOf(value));
                return true;
            case TypedValues.CycleType.TYPE_ALPHA /*403*/:
                this.mAlpha = value;
                return true;
            case TypedValues.CycleType.TYPE_PATH_ROTATE /*416*/:
                this.mTransitionPathRotate = toFloat(Float.valueOf(value));
                return true;
            case TypedValues.CycleType.TYPE_WAVE_PERIOD /*423*/:
                this.mWavePeriod = toFloat(Float.valueOf(value));
                return true;
            case TypedValues.CycleType.TYPE_WAVE_OFFSET /*424*/:
                this.mWaveOffset = toFloat(Float.valueOf(value));
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
            case 421:
                this.mWaveShape = value;
                return true;
            default:
                return super.setValue(type, value);
        }
    }

    public boolean setValue(int type, String value) {
        switch (type) {
            case TypedValues.CycleType.TYPE_EASING /*420*/:
                this.mTransitionEasing = value;
                return true;
            case 421:
                this.mWaveShape = 7;
                this.mCustomWaveShape = value;
                return true;
            default:
                return super.setValue(type, value);
        }
    }

    public boolean setValue(int type, boolean value) {
        return super.setValue(type, value);
    }
}

package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import androidx.constraintlayout.motion.utils.ViewSpline;
import androidx.constraintlayout.widget.R;
import java.util.HashMap;
import java.util.HashSet;
import mt.Log1F380D;

public class KeyTimeCycle extends Key {
    public static final int KEY_TYPE = 3;
    static final String NAME = "KeyTimeCycle";
    public static final int SHAPE_BOUNCE = 6;
    public static final int SHAPE_COS_WAVE = 5;
    public static final int SHAPE_REVERSE_SAW_WAVE = 4;
    public static final int SHAPE_SAW_WAVE = 3;
    public static final int SHAPE_SIN_WAVE = 0;
    public static final int SHAPE_SQUARE_WAVE = 1;
    public static final int SHAPE_TRIANGLE_WAVE = 2;
    private static final String TAG = "KeyTimeCycle";
    public static final String WAVE_OFFSET = "waveOffset";
    public static final String WAVE_PERIOD = "wavePeriod";
    public static final String WAVE_SHAPE = "waveShape";
    /* access modifiers changed from: private */
    public float mAlpha = Float.NaN;
    /* access modifiers changed from: private */
    public int mCurveFit = -1;
    /* access modifiers changed from: private */
    public String mCustomWaveShape = null;
    /* access modifiers changed from: private */
    public float mElevation = Float.NaN;
    /* access modifiers changed from: private */
    public float mProgress = Float.NaN;
    /* access modifiers changed from: private */
    public float mRotation = Float.NaN;
    /* access modifiers changed from: private */
    public float mRotationX = Float.NaN;
    /* access modifiers changed from: private */
    public float mRotationY = Float.NaN;
    /* access modifiers changed from: private */
    public float mScaleX = Float.NaN;
    /* access modifiers changed from: private */
    public float mScaleY = Float.NaN;
    /* access modifiers changed from: private */
    public String mTransitionEasing;
    /* access modifiers changed from: private */
    public float mTransitionPathRotate = Float.NaN;
    /* access modifiers changed from: private */
    public float mTranslationX = Float.NaN;
    /* access modifiers changed from: private */
    public float mTranslationY = Float.NaN;
    /* access modifiers changed from: private */
    public float mTranslationZ = Float.NaN;
    /* access modifiers changed from: private */
    public float mWaveOffset = 0.0f;
    /* access modifiers changed from: private */
    public float mWavePeriod = Float.NaN;
    /* access modifiers changed from: private */
    public int mWaveShape = 0;

    /* compiled from: 001D */
    private static class Loader {
        private static final int ANDROID_ALPHA = 1;
        private static final int ANDROID_ELEVATION = 2;
        private static final int ANDROID_ROTATION = 4;
        private static final int ANDROID_ROTATION_X = 5;
        private static final int ANDROID_ROTATION_Y = 6;
        private static final int ANDROID_SCALE_X = 7;
        private static final int ANDROID_SCALE_Y = 14;
        private static final int ANDROID_TRANSLATION_X = 15;
        private static final int ANDROID_TRANSLATION_Y = 16;
        private static final int ANDROID_TRANSLATION_Z = 17;
        private static final int CURVE_FIT = 13;
        private static final int FRAME_POSITION = 12;
        private static final int PROGRESS = 18;
        private static final int TARGET_ID = 10;
        private static final int TRANSITION_EASING = 9;
        private static final int TRANSITION_PATH_ROTATE = 8;
        private static final int WAVE_OFFSET = 21;
        private static final int WAVE_PERIOD = 20;
        private static final int WAVE_SHAPE = 19;
        private static SparseIntArray mAttrMap;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            mAttrMap = sparseIntArray;
            sparseIntArray.append(R.styleable.KeyTimeCycle_android_alpha, 1);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_elevation, 2);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_rotation, 4);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_rotationX, 5);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_rotationY, 6);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_scaleX, 7);
            mAttrMap.append(R.styleable.KeyTimeCycle_transitionPathRotate, 8);
            mAttrMap.append(R.styleable.KeyTimeCycle_transitionEasing, 9);
            mAttrMap.append(R.styleable.KeyTimeCycle_motionTarget, 10);
            mAttrMap.append(R.styleable.KeyTimeCycle_framePosition, 12);
            mAttrMap.append(R.styleable.KeyTimeCycle_curveFit, 13);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_scaleY, 14);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_translationX, 15);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_translationY, 16);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_translationZ, 17);
            mAttrMap.append(R.styleable.KeyTimeCycle_motionProgress, 18);
            mAttrMap.append(R.styleable.KeyTimeCycle_wavePeriod, 20);
            mAttrMap.append(R.styleable.KeyTimeCycle_waveOffset, 21);
            mAttrMap.append(R.styleable.KeyTimeCycle_waveShape, 19);
        }

        private Loader() {
        }

        public static void read(KeyTimeCycle c, TypedArray a) {
            int indexCount = a.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = a.getIndex(i);
                switch (mAttrMap.get(index)) {
                    case 1:
                        float unused = c.mAlpha = a.getFloat(index, c.mAlpha);
                        break;
                    case 2:
                        float unused2 = c.mElevation = a.getDimension(index, c.mElevation);
                        break;
                    case 4:
                        float unused3 = c.mRotation = a.getFloat(index, c.mRotation);
                        break;
                    case 5:
                        float unused4 = c.mRotationX = a.getFloat(index, c.mRotationX);
                        break;
                    case 6:
                        float unused5 = c.mRotationY = a.getFloat(index, c.mRotationY);
                        break;
                    case 7:
                        float unused6 = c.mScaleX = a.getFloat(index, c.mScaleX);
                        break;
                    case 8:
                        float unused7 = c.mTransitionPathRotate = a.getFloat(index, c.mTransitionPathRotate);
                        break;
                    case 9:
                        String unused8 = c.mTransitionEasing = a.getString(index);
                        break;
                    case 10:
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
                    case 12:
                        c.mFramePosition = a.getInt(index, c.mFramePosition);
                        break;
                    case 13:
                        int unused9 = c.mCurveFit = a.getInteger(index, c.mCurveFit);
                        break;
                    case 14:
                        float unused10 = c.mScaleY = a.getFloat(index, c.mScaleY);
                        break;
                    case 15:
                        float unused11 = c.mTranslationX = a.getDimension(index, c.mTranslationX);
                        break;
                    case 16:
                        float unused12 = c.mTranslationY = a.getDimension(index, c.mTranslationY);
                        break;
                    case 17:
                        if (Build.VERSION.SDK_INT < 21) {
                            break;
                        } else {
                            float unused13 = c.mTranslationZ = a.getDimension(index, c.mTranslationZ);
                            break;
                        }
                    case 18:
                        float unused14 = c.mProgress = a.getFloat(index, c.mProgress);
                        break;
                    case 19:
                        if (a.peekValue(index).type != 3) {
                            int unused15 = c.mWaveShape = a.getInt(index, c.mWaveShape);
                            break;
                        } else {
                            String unused16 = c.mCustomWaveShape = a.getString(index);
                            int unused17 = c.mWaveShape = 7;
                            break;
                        }
                    case 20:
                        float unused18 = c.mWavePeriod = a.getFloat(index, c.mWavePeriod);
                        break;
                    case 21:
                        if (a.peekValue(index).type != 5) {
                            float unused19 = c.mWaveOffset = a.getFloat(index, c.mWaveOffset);
                            break;
                        } else {
                            float unused20 = c.mWaveOffset = a.getDimension(index, c.mWaveOffset);
                            break;
                        }
                    default:
                        StringBuilder append = new StringBuilder().append("unused attribute 0x");
                        String hexString = Integer.toHexString(index);
                        Log1F380D.a((Object) hexString);
                        Log.e("KeyTimeCycle", append.append(hexString).append("   ").append(mAttrMap.get(index)).toString());
                        break;
                }
            }
        }
    }

    public KeyTimeCycle() {
        this.mType = 3;
        this.mCustomConstraints = new HashMap();
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0070, code lost:
        if (r1.equals("elevation") != false) goto L_0x00da;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addTimeValues(java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.utils.ViewTimeCycle> r12) {
        /*
            r11 = this;
            java.util.Set r0 = r12.keySet()
            java.util.Iterator r0 = r0.iterator()
        L_0x0008:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0219
            java.lang.Object r1 = r0.next()
            java.lang.String r1 = (java.lang.String) r1
            java.lang.Object r2 = r12.get(r1)
            androidx.constraintlayout.motion.utils.ViewTimeCycle r2 = (androidx.constraintlayout.motion.utils.ViewTimeCycle) r2
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
            java.util.HashMap r4 = r11.mCustomConstraints
            java.lang.Object r4 = r4.get(r3)
            androidx.constraintlayout.widget.ConstraintAttribute r4 = (androidx.constraintlayout.widget.ConstraintAttribute) r4
            if (r4 == 0) goto L_0x0008
            r5 = r2
            androidx.constraintlayout.motion.utils.ViewTimeCycle$CustomSet r5 = (androidx.constraintlayout.motion.utils.ViewTimeCycle.CustomSet) r5
            int r6 = r11.mFramePosition
            float r8 = r11.mWavePeriod
            int r9 = r11.mWaveShape
            float r10 = r11.mWaveOffset
            r7 = r4
            r5.setPoint((int) r6, (androidx.constraintlayout.widget.ConstraintAttribute) r7, (float) r8, (int) r9, (float) r10)
            goto L_0x0008
        L_0x0049:
            r3 = -1
            int r4 = r1.hashCode()
            switch(r4) {
                case -1249320806: goto L_0x00ce;
                case -1249320805: goto L_0x00c3;
                case -1225497657: goto L_0x00b7;
                case -1225497656: goto L_0x00ab;
                case -1225497655: goto L_0x009f;
                case -1001078227: goto L_0x0094;
                case -908189618: goto L_0x0089;
                case -908189617: goto L_0x007e;
                case -40300674: goto L_0x0073;
                case -4379043: goto L_0x006a;
                case 37232917: goto L_0x005e;
                case 92909918: goto L_0x0053;
                default: goto L_0x0051;
            }
        L_0x0051:
            goto L_0x00d9
        L_0x0053:
            java.lang.String r4 = "alpha"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 0
            goto L_0x00da
        L_0x005e:
            java.lang.String r4 = "transitionPathRotate"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 5
            goto L_0x00da
        L_0x006a:
            java.lang.String r4 = "elevation"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            goto L_0x00da
        L_0x0073:
            java.lang.String r4 = "rotation"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 2
            goto L_0x00da
        L_0x007e:
            java.lang.String r4 = "scaleY"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 7
            goto L_0x00da
        L_0x0089:
            java.lang.String r4 = "scaleX"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 6
            goto L_0x00da
        L_0x0094:
            java.lang.String r4 = "progress"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 11
            goto L_0x00da
        L_0x009f:
            java.lang.String r4 = "translationZ"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 10
            goto L_0x00da
        L_0x00ab:
            java.lang.String r4 = "translationY"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 9
            goto L_0x00da
        L_0x00b7:
            java.lang.String r4 = "translationX"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 8
            goto L_0x00da
        L_0x00c3:
            java.lang.String r4 = "rotationY"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 4
            goto L_0x00da
        L_0x00ce:
            java.lang.String r4 = "rotationX"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x0051
            r5 = 3
            goto L_0x00da
        L_0x00d9:
            r5 = r3
        L_0x00da:
            switch(r5) {
                case 0: goto L_0x0201;
                case 1: goto L_0x01ea;
                case 2: goto L_0x01d3;
                case 3: goto L_0x01bc;
                case 4: goto L_0x01a5;
                case 5: goto L_0x018d;
                case 6: goto L_0x0175;
                case 7: goto L_0x015d;
                case 8: goto L_0x0145;
                case 9: goto L_0x012d;
                case 10: goto L_0x0115;
                case 11: goto L_0x00fd;
                default: goto L_0x00dd;
            }
        L_0x00dd:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "UNKNOWN addValues \""
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r1)
            java.lang.String r4 = "\""
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            java.lang.String r4 = "KeyTimeCycles"
            android.util.Log.e(r4, r3)
            goto L_0x0217
        L_0x00fd:
            float r3 = r11.mProgress
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0217
            int r4 = r11.mFramePosition
            float r5 = r11.mProgress
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0217
        L_0x0115:
            float r3 = r11.mTranslationZ
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0217
            int r4 = r11.mFramePosition
            float r5 = r11.mTranslationZ
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0217
        L_0x012d:
            float r3 = r11.mTranslationY
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0217
            int r4 = r11.mFramePosition
            float r5 = r11.mTranslationY
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0217
        L_0x0145:
            float r3 = r11.mTranslationX
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0217
            int r4 = r11.mFramePosition
            float r5 = r11.mTranslationX
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0217
        L_0x015d:
            float r3 = r11.mScaleY
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0217
            int r4 = r11.mFramePosition
            float r5 = r11.mScaleY
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0217
        L_0x0175:
            float r3 = r11.mScaleX
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0217
            int r4 = r11.mFramePosition
            float r5 = r11.mScaleX
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0217
        L_0x018d:
            float r3 = r11.mTransitionPathRotate
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0217
            int r4 = r11.mFramePosition
            float r5 = r11.mTransitionPathRotate
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0217
        L_0x01a5:
            float r3 = r11.mRotationY
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0217
            int r4 = r11.mFramePosition
            float r5 = r11.mRotationY
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0217
        L_0x01bc:
            float r3 = r11.mRotationX
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0217
            int r4 = r11.mFramePosition
            float r5 = r11.mRotationX
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0217
        L_0x01d3:
            float r3 = r11.mRotation
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0217
            int r4 = r11.mFramePosition
            float r5 = r11.mRotation
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0217
        L_0x01ea:
            float r3 = r11.mElevation
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0217
            int r4 = r11.mFramePosition
            float r5 = r11.mElevation
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
            goto L_0x0217
        L_0x0201:
            float r3 = r11.mAlpha
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x0217
            int r4 = r11.mFramePosition
            float r5 = r11.mAlpha
            float r6 = r11.mWavePeriod
            int r7 = r11.mWaveShape
            float r8 = r11.mWaveOffset
            r3 = r2
            r3.setPoint(r4, r5, r6, r7, r8)
        L_0x0217:
            goto L_0x0008
        L_0x0219:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.KeyTimeCycle.addTimeValues(java.util.HashMap):void");
    }

    public void addValues(HashMap<String, ViewSpline> hashMap) {
        throw new IllegalArgumentException(" KeyTimeCycles do not support SplineSet");
    }

    public Key clone() {
        return new KeyTimeCycle().copy(this);
    }

    public Key copy(Key src) {
        super.copy(src);
        KeyTimeCycle keyTimeCycle = (KeyTimeCycle) src;
        this.mTransitionEasing = keyTimeCycle.mTransitionEasing;
        this.mCurveFit = keyTimeCycle.mCurveFit;
        this.mWaveShape = keyTimeCycle.mWaveShape;
        this.mWavePeriod = keyTimeCycle.mWavePeriod;
        this.mWaveOffset = keyTimeCycle.mWaveOffset;
        this.mProgress = keyTimeCycle.mProgress;
        this.mAlpha = keyTimeCycle.mAlpha;
        this.mElevation = keyTimeCycle.mElevation;
        this.mRotation = keyTimeCycle.mRotation;
        this.mTransitionPathRotate = keyTimeCycle.mTransitionPathRotate;
        this.mRotationX = keyTimeCycle.mRotationX;
        this.mRotationY = keyTimeCycle.mRotationY;
        this.mScaleX = keyTimeCycle.mScaleX;
        this.mScaleY = keyTimeCycle.mScaleY;
        this.mTranslationX = keyTimeCycle.mTranslationX;
        this.mTranslationY = keyTimeCycle.mTranslationY;
        this.mTranslationZ = keyTimeCycle.mTranslationZ;
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
            hashSet.add(Key.ROTATION);
        }
        if (!Float.isNaN(this.mRotationX)) {
            hashSet.add("rotationX");
        }
        if (!Float.isNaN(this.mRotationY)) {
            hashSet.add("rotationY");
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
        if (!Float.isNaN(this.mTransitionPathRotate)) {
            hashSet.add("transitionPathRotate");
        }
        if (!Float.isNaN(this.mScaleX)) {
            hashSet.add("scaleX");
        }
        if (!Float.isNaN(this.mScaleY)) {
            hashSet.add("scaleY");
        }
        if (!Float.isNaN(this.mProgress)) {
            hashSet.add("progress");
        }
        if (this.mCustomConstraints.size() > 0) {
            for (String str : this.mCustomConstraints.keySet()) {
                hashSet.add("CUSTOM," + str);
            }
        }
    }

    public void load(Context context, AttributeSet attrs) {
        Loader.read(this, context.obtainStyledAttributes(attrs, R.styleable.KeyTimeCycle));
    }

    public void setInterpolation(HashMap<String, Integer> hashMap) {
        if (this.mCurveFit != -1) {
            if (!Float.isNaN(this.mAlpha)) {
                hashMap.put("alpha", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mElevation)) {
                hashMap.put("elevation", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mRotation)) {
                hashMap.put(Key.ROTATION, Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mRotationX)) {
                hashMap.put("rotationX", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mRotationY)) {
                hashMap.put("rotationY", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mTranslationX)) {
                hashMap.put("translationX", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mTranslationY)) {
                hashMap.put("translationY", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mTranslationZ)) {
                hashMap.put("translationZ", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mTransitionPathRotate)) {
                hashMap.put("transitionPathRotate", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mScaleX)) {
                hashMap.put("scaleX", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mScaleX)) {
                hashMap.put("scaleY", Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mProgress)) {
                hashMap.put("progress", Integer.valueOf(this.mCurveFit));
            }
            if (this.mCustomConstraints.size() > 0) {
                for (String str : this.mCustomConstraints.keySet()) {
                    hashMap.put("CUSTOM," + str, Integer.valueOf(this.mCurveFit));
                }
            }
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setValue(java.lang.String r3, java.lang.Object r4) {
        /*
            r2 = this;
            int r0 = r3.hashCode()
            r1 = 7
            switch(r0) {
                case -1913008125: goto L_0x00c8;
                case -1812823328: goto L_0x00bc;
                case -1249320806: goto L_0x00b1;
                case -1249320805: goto L_0x00a6;
                case -1225497657: goto L_0x009a;
                case -1225497656: goto L_0x008e;
                case -1225497655: goto L_0x0082;
                case -908189618: goto L_0x0077;
                case -908189617: goto L_0x006b;
                case -40300674: goto L_0x005f;
                case -4379043: goto L_0x0054;
                case 37232917: goto L_0x0047;
                case 92909918: goto L_0x003c;
                case 156108012: goto L_0x002f;
                case 184161818: goto L_0x0022;
                case 579057826: goto L_0x0017;
                case 1532805160: goto L_0x000a;
                default: goto L_0x0008;
            }
        L_0x0008:
            goto L_0x00d2
        L_0x000a:
            java.lang.String r0 = "waveShape"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 16
            goto L_0x00d3
        L_0x0017:
            java.lang.String r0 = "curveFit"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 1
            goto L_0x00d3
        L_0x0022:
            java.lang.String r0 = "wavePeriod"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 14
            goto L_0x00d3
        L_0x002f:
            java.lang.String r0 = "waveOffset"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 15
            goto L_0x00d3
        L_0x003c:
            java.lang.String r0 = "alpha"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 0
            goto L_0x00d3
        L_0x0047:
            java.lang.String r0 = "transitionPathRotate"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 10
            goto L_0x00d3
        L_0x0054:
            java.lang.String r0 = "elevation"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 2
            goto L_0x00d3
        L_0x005f:
            java.lang.String r0 = "rotation"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 4
            goto L_0x00d3
        L_0x006b:
            java.lang.String r0 = "scaleY"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 8
            goto L_0x00d3
        L_0x0077:
            java.lang.String r0 = "scaleX"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = r1
            goto L_0x00d3
        L_0x0082:
            java.lang.String r0 = "translationZ"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 13
            goto L_0x00d3
        L_0x008e:
            java.lang.String r0 = "translationY"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 12
            goto L_0x00d3
        L_0x009a:
            java.lang.String r0 = "translationX"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 11
            goto L_0x00d3
        L_0x00a6:
            java.lang.String r0 = "rotationY"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 6
            goto L_0x00d3
        L_0x00b1:
            java.lang.String r0 = "rotationX"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 5
            goto L_0x00d3
        L_0x00bc:
            java.lang.String r0 = "transitionEasing"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 9
            goto L_0x00d3
        L_0x00c8:
            java.lang.String r0 = "motionProgress"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 3
            goto L_0x00d3
        L_0x00d2:
            r0 = -1
        L_0x00d3:
            switch(r0) {
                case 0: goto L_0x0158;
                case 1: goto L_0x0151;
                case 2: goto L_0x014a;
                case 3: goto L_0x0143;
                case 4: goto L_0x013c;
                case 5: goto L_0x0135;
                case 6: goto L_0x012e;
                case 7: goto L_0x0127;
                case 8: goto L_0x0120;
                case 9: goto L_0x0119;
                case 10: goto L_0x0112;
                case 11: goto L_0x010b;
                case 12: goto L_0x0104;
                case 13: goto L_0x00fd;
                case 14: goto L_0x00f6;
                case 15: goto L_0x00ee;
                case 16: goto L_0x00d8;
                default: goto L_0x00d6;
            }
        L_0x00d6:
            goto L_0x015f
        L_0x00d8:
            boolean r0 = r4 instanceof java.lang.Integer
            if (r0 == 0) goto L_0x00e4
            int r0 = r2.toInt(r4)
            r2.mWaveShape = r0
            goto L_0x015f
        L_0x00e4:
            r2.mWaveShape = r1
            java.lang.String r0 = r4.toString()
            r2.mCustomWaveShape = r0
            goto L_0x015f
        L_0x00ee:
            float r0 = r2.toFloat(r4)
            r2.mWaveOffset = r0
            goto L_0x015f
        L_0x00f6:
            float r0 = r2.toFloat(r4)
            r2.mWavePeriod = r0
            goto L_0x015f
        L_0x00fd:
            float r0 = r2.toFloat(r4)
            r2.mTranslationZ = r0
            goto L_0x015f
        L_0x0104:
            float r0 = r2.toFloat(r4)
            r2.mTranslationY = r0
            goto L_0x015f
        L_0x010b:
            float r0 = r2.toFloat(r4)
            r2.mTranslationX = r0
            goto L_0x015f
        L_0x0112:
            float r0 = r2.toFloat(r4)
            r2.mTransitionPathRotate = r0
            goto L_0x015f
        L_0x0119:
            java.lang.String r0 = r4.toString()
            r2.mTransitionEasing = r0
            goto L_0x015f
        L_0x0120:
            float r0 = r2.toFloat(r4)
            r2.mScaleY = r0
            goto L_0x015f
        L_0x0127:
            float r0 = r2.toFloat(r4)
            r2.mScaleX = r0
            goto L_0x015f
        L_0x012e:
            float r0 = r2.toFloat(r4)
            r2.mRotationY = r0
            goto L_0x015f
        L_0x0135:
            float r0 = r2.toFloat(r4)
            r2.mRotationX = r0
            goto L_0x015f
        L_0x013c:
            float r0 = r2.toFloat(r4)
            r2.mRotation = r0
            goto L_0x015f
        L_0x0143:
            float r0 = r2.toFloat(r4)
            r2.mProgress = r0
            goto L_0x015f
        L_0x014a:
            float r0 = r2.toFloat(r4)
            r2.mElevation = r0
            goto L_0x015f
        L_0x0151:
            int r0 = r2.toInt(r4)
            r2.mCurveFit = r0
            goto L_0x015f
        L_0x0158:
            float r0 = r2.toFloat(r4)
            r2.mAlpha = r0
        L_0x015f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.KeyTimeCycle.setValue(java.lang.String, java.lang.Object):void");
    }
}

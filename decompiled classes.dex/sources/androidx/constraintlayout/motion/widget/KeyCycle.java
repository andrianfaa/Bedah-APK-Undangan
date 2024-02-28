package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.motion.utils.ViewOscillator;
import androidx.constraintlayout.motion.utils.ViewSpline;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.R;
import java.util.HashMap;
import java.util.HashSet;
import mt.Log1F380D;

public class KeyCycle extends Key {
    public static final int KEY_TYPE = 4;
    static final String NAME = "KeyCycle";
    public static final int SHAPE_BOUNCE = 6;
    public static final int SHAPE_COS_WAVE = 5;
    public static final int SHAPE_REVERSE_SAW_WAVE = 4;
    public static final int SHAPE_SAW_WAVE = 3;
    public static final int SHAPE_SIN_WAVE = 0;
    public static final int SHAPE_SQUARE_WAVE = 1;
    public static final int SHAPE_TRIANGLE_WAVE = 2;
    private static final String TAG = "KeyCycle";
    public static final String WAVE_OFFSET = "waveOffset";
    public static final String WAVE_PERIOD = "wavePeriod";
    public static final String WAVE_PHASE = "wavePhase";
    public static final String WAVE_SHAPE = "waveShape";
    /* access modifiers changed from: private */
    public float mAlpha = Float.NaN;
    /* access modifiers changed from: private */
    public int mCurveFit = 0;
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
    public String mTransitionEasing = null;
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
    public float mWavePhase = 0.0f;
    /* access modifiers changed from: private */
    public int mWaveShape = -1;
    /* access modifiers changed from: private */
    public int mWaveVariesBy = -1;

    /* compiled from: 001B */
    private static class Loader {
        private static final int ANDROID_ALPHA = 9;
        private static final int ANDROID_ELEVATION = 10;
        private static final int ANDROID_ROTATION = 11;
        private static final int ANDROID_ROTATION_X = 12;
        private static final int ANDROID_ROTATION_Y = 13;
        private static final int ANDROID_SCALE_X = 15;
        private static final int ANDROID_SCALE_Y = 16;
        private static final int ANDROID_TRANSLATION_X = 17;
        private static final int ANDROID_TRANSLATION_Y = 18;
        private static final int ANDROID_TRANSLATION_Z = 19;
        private static final int CURVE_FIT = 4;
        private static final int FRAME_POSITION = 2;
        private static final int PROGRESS = 20;
        private static final int TARGET_ID = 1;
        private static final int TRANSITION_EASING = 3;
        private static final int TRANSITION_PATH_ROTATE = 14;
        private static final int WAVE_OFFSET = 7;
        private static final int WAVE_PERIOD = 6;
        private static final int WAVE_PHASE = 21;
        private static final int WAVE_SHAPE = 5;
        private static final int WAVE_VARIES_BY = 8;
        private static SparseIntArray mAttrMap;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            mAttrMap = sparseIntArray;
            sparseIntArray.append(R.styleable.KeyCycle_motionTarget, 1);
            mAttrMap.append(R.styleable.KeyCycle_framePosition, 2);
            mAttrMap.append(R.styleable.KeyCycle_transitionEasing, 3);
            mAttrMap.append(R.styleable.KeyCycle_curveFit, 4);
            mAttrMap.append(R.styleable.KeyCycle_waveShape, 5);
            mAttrMap.append(R.styleable.KeyCycle_wavePeriod, 6);
            mAttrMap.append(R.styleable.KeyCycle_waveOffset, 7);
            mAttrMap.append(R.styleable.KeyCycle_waveVariesBy, 8);
            mAttrMap.append(R.styleable.KeyCycle_android_alpha, 9);
            mAttrMap.append(R.styleable.KeyCycle_android_elevation, 10);
            mAttrMap.append(R.styleable.KeyCycle_android_rotation, 11);
            mAttrMap.append(R.styleable.KeyCycle_android_rotationX, 12);
            mAttrMap.append(R.styleable.KeyCycle_android_rotationY, 13);
            mAttrMap.append(R.styleable.KeyCycle_transitionPathRotate, 14);
            mAttrMap.append(R.styleable.KeyCycle_android_scaleX, 15);
            mAttrMap.append(R.styleable.KeyCycle_android_scaleY, 16);
            mAttrMap.append(R.styleable.KeyCycle_android_translationX, 17);
            mAttrMap.append(R.styleable.KeyCycle_android_translationY, 18);
            mAttrMap.append(R.styleable.KeyCycle_android_translationZ, 19);
            mAttrMap.append(R.styleable.KeyCycle_motionProgress, 20);
            mAttrMap.append(R.styleable.KeyCycle_wavePhase, 21);
        }

        private Loader() {
        }

        /* access modifiers changed from: private */
        public static void read(KeyCycle c, TypedArray a) {
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
                        String unused = c.mTransitionEasing = a.getString(index);
                        break;
                    case 4:
                        int unused2 = c.mCurveFit = a.getInteger(index, c.mCurveFit);
                        break;
                    case 5:
                        if (a.peekValue(index).type != 3) {
                            int unused3 = c.mWaveShape = a.getInt(index, c.mWaveShape);
                            break;
                        } else {
                            String unused4 = c.mCustomWaveShape = a.getString(index);
                            int unused5 = c.mWaveShape = 7;
                            break;
                        }
                    case 6:
                        float unused6 = c.mWavePeriod = a.getFloat(index, c.mWavePeriod);
                        break;
                    case 7:
                        if (a.peekValue(index).type != 5) {
                            float unused7 = c.mWaveOffset = a.getFloat(index, c.mWaveOffset);
                            break;
                        } else {
                            float unused8 = c.mWaveOffset = a.getDimension(index, c.mWaveOffset);
                            break;
                        }
                    case 8:
                        int unused9 = c.mWaveVariesBy = a.getInt(index, c.mWaveVariesBy);
                        break;
                    case 9:
                        float unused10 = c.mAlpha = a.getFloat(index, c.mAlpha);
                        break;
                    case 10:
                        float unused11 = c.mElevation = a.getDimension(index, c.mElevation);
                        break;
                    case 11:
                        float unused12 = c.mRotation = a.getFloat(index, c.mRotation);
                        break;
                    case 12:
                        float unused13 = c.mRotationX = a.getFloat(index, c.mRotationX);
                        break;
                    case 13:
                        float unused14 = c.mRotationY = a.getFloat(index, c.mRotationY);
                        break;
                    case 14:
                        float unused15 = c.mTransitionPathRotate = a.getFloat(index, c.mTransitionPathRotate);
                        break;
                    case 15:
                        float unused16 = c.mScaleX = a.getFloat(index, c.mScaleX);
                        break;
                    case 16:
                        float unused17 = c.mScaleY = a.getFloat(index, c.mScaleY);
                        break;
                    case 17:
                        float unused18 = c.mTranslationX = a.getDimension(index, c.mTranslationX);
                        break;
                    case 18:
                        float unused19 = c.mTranslationY = a.getDimension(index, c.mTranslationY);
                        break;
                    case 19:
                        if (Build.VERSION.SDK_INT < 21) {
                            break;
                        } else {
                            float unused20 = c.mTranslationZ = a.getDimension(index, c.mTranslationZ);
                            break;
                        }
                    case 20:
                        float unused21 = c.mProgress = a.getFloat(index, c.mProgress);
                        break;
                    case 21:
                        float unused22 = c.mWavePhase = a.getFloat(index, c.mWavePhase) / 360.0f;
                        break;
                    default:
                        StringBuilder append = new StringBuilder().append("unused attribute 0x");
                        String hexString = Integer.toHexString(index);
                        Log1F380D.a((Object) hexString);
                        Log.e(TypedValues.CycleType.NAME, append.append(hexString).append("   ").append(mAttrMap.get(index)).toString());
                        break;
                }
            }
        }
    }

    public KeyCycle() {
        this.mType = 4;
        this.mCustomConstraints = new HashMap();
    }

    public void addCycleValues(HashMap<String, ViewOscillator> hashMap) {
        ViewOscillator viewOscillator;
        ViewOscillator viewOscillator2;
        HashMap<String, ViewOscillator> hashMap2 = hashMap;
        for (String next : hashMap.keySet()) {
            if (next.startsWith("CUSTOM")) {
                ConstraintAttribute constraintAttribute = (ConstraintAttribute) this.mCustomConstraints.get(next.substring("CUSTOM".length() + 1));
                if (!(constraintAttribute == null || constraintAttribute.getType() != ConstraintAttribute.AttributeType.FLOAT_TYPE || (viewOscillator2 = hashMap2.get(next)) == null)) {
                    viewOscillator2.setPoint(this.mFramePosition, this.mWaveShape, this.mCustomWaveShape, this.mWaveVariesBy, this.mWavePeriod, this.mWaveOffset, this.mWavePhase, constraintAttribute.getValueToInterpolate(), constraintAttribute);
                }
            } else {
                float value = getValue(next);
                if (!Float.isNaN(value) && (viewOscillator = hashMap2.get(next)) != null) {
                    viewOscillator.setPoint(this.mFramePosition, this.mWaveShape, this.mCustomWaveShape, this.mWaveVariesBy, this.mWavePeriod, this.mWaveOffset, this.mWavePhase, value);
                }
            }
        }
    }

    public void addValues(HashMap<String, ViewSpline> hashMap) {
        Debug.logStack(TypedValues.CycleType.NAME, "add " + hashMap.size() + " values", 2);
        for (String next : hashMap.keySet()) {
            SplineSet splineSet = hashMap.get(next);
            if (splineSet != null) {
                char c = 65535;
                switch (next.hashCode()) {
                    case -1249320806:
                        if (next.equals("rotationX")) {
                            c = 3;
                            break;
                        }
                        break;
                    case -1249320805:
                        if (next.equals("rotationY")) {
                            c = 4;
                            break;
                        }
                        break;
                    case -1225497657:
                        if (next.equals("translationX")) {
                            c = 8;
                            break;
                        }
                        break;
                    case -1225497656:
                        if (next.equals("translationY")) {
                            c = 9;
                            break;
                        }
                        break;
                    case -1225497655:
                        if (next.equals("translationZ")) {
                            c = 10;
                            break;
                        }
                        break;
                    case -1001078227:
                        if (next.equals("progress")) {
                            c = 13;
                            break;
                        }
                        break;
                    case -908189618:
                        if (next.equals("scaleX")) {
                            c = 6;
                            break;
                        }
                        break;
                    case -908189617:
                        if (next.equals("scaleY")) {
                            c = 7;
                            break;
                        }
                        break;
                    case -40300674:
                        if (next.equals(Key.ROTATION)) {
                            c = 2;
                            break;
                        }
                        break;
                    case -4379043:
                        if (next.equals("elevation")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 37232917:
                        if (next.equals("transitionPathRotate")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 92909918:
                        if (next.equals("alpha")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 156108012:
                        if (next.equals("waveOffset")) {
                            c = 11;
                            break;
                        }
                        break;
                    case 1530034690:
                        if (next.equals("wavePhase")) {
                            c = 12;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        splineSet.setPoint(this.mFramePosition, this.mAlpha);
                        break;
                    case 1:
                        splineSet.setPoint(this.mFramePosition, this.mElevation);
                        break;
                    case 2:
                        splineSet.setPoint(this.mFramePosition, this.mRotation);
                        break;
                    case 3:
                        splineSet.setPoint(this.mFramePosition, this.mRotationX);
                        break;
                    case 4:
                        splineSet.setPoint(this.mFramePosition, this.mRotationY);
                        break;
                    case 5:
                        splineSet.setPoint(this.mFramePosition, this.mTransitionPathRotate);
                        break;
                    case 6:
                        splineSet.setPoint(this.mFramePosition, this.mScaleX);
                        break;
                    case 7:
                        splineSet.setPoint(this.mFramePosition, this.mScaleY);
                        break;
                    case 8:
                        splineSet.setPoint(this.mFramePosition, this.mTranslationX);
                        break;
                    case 9:
                        splineSet.setPoint(this.mFramePosition, this.mTranslationY);
                        break;
                    case 10:
                        splineSet.setPoint(this.mFramePosition, this.mTranslationZ);
                        break;
                    case 11:
                        splineSet.setPoint(this.mFramePosition, this.mWaveOffset);
                        break;
                    case 12:
                        splineSet.setPoint(this.mFramePosition, this.mWavePhase);
                        break;
                    case 13:
                        splineSet.setPoint(this.mFramePosition, this.mProgress);
                        break;
                    default:
                        if (next.startsWith("CUSTOM")) {
                            break;
                        } else {
                            Log.v("WARNING KeyCycle", "  UNKNOWN  " + next);
                            break;
                        }
                }
            }
        }
    }

    public Key clone() {
        return new KeyCycle().copy(this);
    }

    public Key copy(Key src) {
        super.copy(src);
        KeyCycle keyCycle = (KeyCycle) src;
        this.mTransitionEasing = keyCycle.mTransitionEasing;
        this.mCurveFit = keyCycle.mCurveFit;
        this.mWaveShape = keyCycle.mWaveShape;
        this.mCustomWaveShape = keyCycle.mCustomWaveShape;
        this.mWavePeriod = keyCycle.mWavePeriod;
        this.mWaveOffset = keyCycle.mWaveOffset;
        this.mWavePhase = keyCycle.mWavePhase;
        this.mProgress = keyCycle.mProgress;
        this.mWaveVariesBy = keyCycle.mWaveVariesBy;
        this.mAlpha = keyCycle.mAlpha;
        this.mElevation = keyCycle.mElevation;
        this.mRotation = keyCycle.mRotation;
        this.mTransitionPathRotate = keyCycle.mTransitionPathRotate;
        this.mRotationX = keyCycle.mRotationX;
        this.mRotationY = keyCycle.mRotationY;
        this.mScaleX = keyCycle.mScaleX;
        this.mScaleY = keyCycle.mScaleY;
        this.mTranslationX = keyCycle.mTranslationX;
        this.mTranslationY = keyCycle.mTranslationY;
        this.mTranslationZ = keyCycle.mTranslationZ;
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
        if (!Float.isNaN(this.mScaleX)) {
            hashSet.add("scaleX");
        }
        if (!Float.isNaN(this.mScaleY)) {
            hashSet.add("scaleY");
        }
        if (!Float.isNaN(this.mTransitionPathRotate)) {
            hashSet.add("transitionPathRotate");
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
        if (this.mCustomConstraints.size() > 0) {
            for (String str : this.mCustomConstraints.keySet()) {
                hashSet.add("CUSTOM," + str);
            }
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public float getValue(java.lang.String r3) {
        /*
            r2 = this;
            int r0 = r3.hashCode()
            switch(r0) {
                case -1249320806: goto L_0x009f;
                case -1249320805: goto L_0x0094;
                case -1225497657: goto L_0x0088;
                case -1225497656: goto L_0x007c;
                case -1225497655: goto L_0x0070;
                case -1001078227: goto L_0x0065;
                case -908189618: goto L_0x005a;
                case -908189617: goto L_0x004f;
                case -40300674: goto L_0x0044;
                case -4379043: goto L_0x003a;
                case 37232917: goto L_0x002e;
                case 92909918: goto L_0x0023;
                case 156108012: goto L_0x0016;
                case 1530034690: goto L_0x0009;
                default: goto L_0x0007;
            }
        L_0x0007:
            goto L_0x00aa
        L_0x0009:
            java.lang.String r0 = "wavePhase"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 12
            goto L_0x00ab
        L_0x0016:
            java.lang.String r0 = "waveOffset"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 11
            goto L_0x00ab
        L_0x0023:
            java.lang.String r0 = "alpha"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 0
            goto L_0x00ab
        L_0x002e:
            java.lang.String r0 = "transitionPathRotate"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 5
            goto L_0x00ab
        L_0x003a:
            java.lang.String r0 = "elevation"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 1
            goto L_0x00ab
        L_0x0044:
            java.lang.String r0 = "rotation"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 2
            goto L_0x00ab
        L_0x004f:
            java.lang.String r0 = "scaleY"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 7
            goto L_0x00ab
        L_0x005a:
            java.lang.String r0 = "scaleX"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 6
            goto L_0x00ab
        L_0x0065:
            java.lang.String r0 = "progress"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 13
            goto L_0x00ab
        L_0x0070:
            java.lang.String r0 = "translationZ"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 10
            goto L_0x00ab
        L_0x007c:
            java.lang.String r0 = "translationY"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 9
            goto L_0x00ab
        L_0x0088:
            java.lang.String r0 = "translationX"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 8
            goto L_0x00ab
        L_0x0094:
            java.lang.String r0 = "rotationY"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 4
            goto L_0x00ab
        L_0x009f:
            java.lang.String r0 = "rotationX"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 3
            goto L_0x00ab
        L_0x00aa:
            r0 = -1
        L_0x00ab:
            switch(r0) {
                case 0: goto L_0x00f6;
                case 1: goto L_0x00f3;
                case 2: goto L_0x00f0;
                case 3: goto L_0x00ed;
                case 4: goto L_0x00ea;
                case 5: goto L_0x00e7;
                case 6: goto L_0x00e4;
                case 7: goto L_0x00e1;
                case 8: goto L_0x00de;
                case 9: goto L_0x00db;
                case 10: goto L_0x00d8;
                case 11: goto L_0x00d5;
                case 12: goto L_0x00d2;
                case 13: goto L_0x00cf;
                default: goto L_0x00ae;
            }
        L_0x00ae:
            java.lang.String r0 = "CUSTOM"
            boolean r0 = r3.startsWith(r0)
            if (r0 != 0) goto L_0x00f9
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "  UNKNOWN  "
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.StringBuilder r0 = r0.append(r3)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "WARNING! KeyCycle"
            android.util.Log.v(r1, r0)
            goto L_0x00f9
        L_0x00cf:
            float r0 = r2.mProgress
            return r0
        L_0x00d2:
            float r0 = r2.mWavePhase
            return r0
        L_0x00d5:
            float r0 = r2.mWaveOffset
            return r0
        L_0x00d8:
            float r0 = r2.mTranslationZ
            return r0
        L_0x00db:
            float r0 = r2.mTranslationY
            return r0
        L_0x00de:
            float r0 = r2.mTranslationX
            return r0
        L_0x00e1:
            float r0 = r2.mScaleY
            return r0
        L_0x00e4:
            float r0 = r2.mScaleX
            return r0
        L_0x00e7:
            float r0 = r2.mTransitionPathRotate
            return r0
        L_0x00ea:
            float r0 = r2.mRotationY
            return r0
        L_0x00ed:
            float r0 = r2.mRotationX
            return r0
        L_0x00f0:
            float r0 = r2.mRotation
            return r0
        L_0x00f3:
            float r0 = r2.mElevation
            return r0
        L_0x00f6:
            float r0 = r2.mAlpha
            return r0
        L_0x00f9:
            r0 = 2143289344(0x7fc00000, float:NaN)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.KeyCycle.getValue(java.lang.String):float");
    }

    public void load(Context context, AttributeSet attrs) {
        Loader.read(this, context.obtainStyledAttributes(attrs, R.styleable.KeyCycle));
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setValue(java.lang.String r3, java.lang.Object r4) {
        /*
            r2 = this;
            int r0 = r3.hashCode()
            r1 = 7
            switch(r0) {
                case -1913008125: goto L_0x00d5;
                case -1812823328: goto L_0x00c9;
                case -1249320806: goto L_0x00be;
                case -1249320805: goto L_0x00b3;
                case -1225497657: goto L_0x00a7;
                case -1225497656: goto L_0x009b;
                case -1225497655: goto L_0x008f;
                case -908189618: goto L_0x0084;
                case -908189617: goto L_0x0078;
                case -40300674: goto L_0x006c;
                case -4379043: goto L_0x0061;
                case 37232917: goto L_0x0054;
                case 92909918: goto L_0x0049;
                case 156108012: goto L_0x003c;
                case 184161818: goto L_0x002f;
                case 579057826: goto L_0x0024;
                case 1530034690: goto L_0x0017;
                case 1532805160: goto L_0x000a;
                default: goto L_0x0008;
            }
        L_0x0008:
            goto L_0x00df
        L_0x000a:
            java.lang.String r0 = "waveShape"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 17
            goto L_0x00e0
        L_0x0017:
            java.lang.String r0 = "wavePhase"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 16
            goto L_0x00e0
        L_0x0024:
            java.lang.String r0 = "curveFit"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 1
            goto L_0x00e0
        L_0x002f:
            java.lang.String r0 = "wavePeriod"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 14
            goto L_0x00e0
        L_0x003c:
            java.lang.String r0 = "waveOffset"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 15
            goto L_0x00e0
        L_0x0049:
            java.lang.String r0 = "alpha"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 0
            goto L_0x00e0
        L_0x0054:
            java.lang.String r0 = "transitionPathRotate"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 10
            goto L_0x00e0
        L_0x0061:
            java.lang.String r0 = "elevation"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 2
            goto L_0x00e0
        L_0x006c:
            java.lang.String r0 = "rotation"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 4
            goto L_0x00e0
        L_0x0078:
            java.lang.String r0 = "scaleY"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 8
            goto L_0x00e0
        L_0x0084:
            java.lang.String r0 = "scaleX"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = r1
            goto L_0x00e0
        L_0x008f:
            java.lang.String r0 = "translationZ"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 13
            goto L_0x00e0
        L_0x009b:
            java.lang.String r0 = "translationY"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 12
            goto L_0x00e0
        L_0x00a7:
            java.lang.String r0 = "translationX"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 11
            goto L_0x00e0
        L_0x00b3:
            java.lang.String r0 = "rotationY"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 6
            goto L_0x00e0
        L_0x00be:
            java.lang.String r0 = "rotationX"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 5
            goto L_0x00e0
        L_0x00c9:
            java.lang.String r0 = "transitionEasing"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 9
            goto L_0x00e0
        L_0x00d5:
            java.lang.String r0 = "motionProgress"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 3
            goto L_0x00e0
        L_0x00df:
            r0 = -1
        L_0x00e0:
            switch(r0) {
                case 0: goto L_0x016d;
                case 1: goto L_0x0166;
                case 2: goto L_0x015f;
                case 3: goto L_0x0158;
                case 4: goto L_0x0151;
                case 5: goto L_0x014a;
                case 6: goto L_0x0143;
                case 7: goto L_0x013c;
                case 8: goto L_0x0135;
                case 9: goto L_0x012e;
                case 10: goto L_0x0127;
                case 11: goto L_0x0120;
                case 12: goto L_0x0119;
                case 13: goto L_0x0112;
                case 14: goto L_0x010b;
                case 15: goto L_0x0103;
                case 16: goto L_0x00fb;
                case 17: goto L_0x00e5;
                default: goto L_0x00e3;
            }
        L_0x00e3:
            goto L_0x0174
        L_0x00e5:
            boolean r0 = r4 instanceof java.lang.Integer
            if (r0 == 0) goto L_0x00f1
            int r0 = r2.toInt(r4)
            r2.mWaveShape = r0
            goto L_0x0174
        L_0x00f1:
            r2.mWaveShape = r1
            java.lang.String r0 = r4.toString()
            r2.mCustomWaveShape = r0
            goto L_0x0174
        L_0x00fb:
            float r0 = r2.toFloat(r4)
            r2.mWavePhase = r0
            goto L_0x0174
        L_0x0103:
            float r0 = r2.toFloat(r4)
            r2.mWaveOffset = r0
            goto L_0x0174
        L_0x010b:
            float r0 = r2.toFloat(r4)
            r2.mWavePeriod = r0
            goto L_0x0174
        L_0x0112:
            float r0 = r2.toFloat(r4)
            r2.mTranslationZ = r0
            goto L_0x0174
        L_0x0119:
            float r0 = r2.toFloat(r4)
            r2.mTranslationY = r0
            goto L_0x0174
        L_0x0120:
            float r0 = r2.toFloat(r4)
            r2.mTranslationX = r0
            goto L_0x0174
        L_0x0127:
            float r0 = r2.toFloat(r4)
            r2.mTransitionPathRotate = r0
            goto L_0x0174
        L_0x012e:
            java.lang.String r0 = r4.toString()
            r2.mTransitionEasing = r0
            goto L_0x0174
        L_0x0135:
            float r0 = r2.toFloat(r4)
            r2.mScaleY = r0
            goto L_0x0174
        L_0x013c:
            float r0 = r2.toFloat(r4)
            r2.mScaleX = r0
            goto L_0x0174
        L_0x0143:
            float r0 = r2.toFloat(r4)
            r2.mRotationY = r0
            goto L_0x0174
        L_0x014a:
            float r0 = r2.toFloat(r4)
            r2.mRotationX = r0
            goto L_0x0174
        L_0x0151:
            float r0 = r2.toFloat(r4)
            r2.mRotation = r0
            goto L_0x0174
        L_0x0158:
            float r0 = r2.toFloat(r4)
            r2.mProgress = r0
            goto L_0x0174
        L_0x015f:
            float r0 = r2.toFloat(r4)
            r2.mElevation = r0
            goto L_0x0174
        L_0x0166:
            int r0 = r2.toInt(r4)
            r2.mCurveFit = r0
            goto L_0x0174
        L_0x016d:
            float r0 = r2.toFloat(r4)
            r2.mAlpha = r0
        L_0x0174:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.KeyCycle.setValue(java.lang.String, java.lang.Object):void");
    }
}

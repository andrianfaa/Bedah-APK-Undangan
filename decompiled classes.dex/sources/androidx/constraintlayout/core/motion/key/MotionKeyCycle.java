package androidx.constraintlayout.core.motion.key;

import androidx.constraintlayout.core.motion.CustomVariable;
import androidx.constraintlayout.core.motion.utils.KeyCycleOscillator;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.core.motion.utils.Utils;
import java.util.HashMap;
import java.util.HashSet;

public class MotionKeyCycle extends MotionKey {
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
    private float mAlpha = Float.NaN;
    private int mCurveFit = 0;
    private String mCustomWaveShape = null;
    private float mElevation = Float.NaN;
    private float mProgress = Float.NaN;
    private float mRotation = Float.NaN;
    private float mRotationX = Float.NaN;
    private float mRotationY = Float.NaN;
    private float mScaleX = Float.NaN;
    private float mScaleY = Float.NaN;
    private String mTransitionEasing = null;
    private float mTransitionPathRotate = Float.NaN;
    private float mTranslationX = Float.NaN;
    private float mTranslationY = Float.NaN;
    private float mTranslationZ = Float.NaN;
    private float mWaveOffset = 0.0f;
    private float mWavePeriod = Float.NaN;
    private float mWavePhase = 0.0f;
    private int mWaveShape = -1;

    public MotionKeyCycle() {
        this.mType = 4;
        this.mCustom = new HashMap();
    }

    public void addCycleValues(HashMap<String, KeyCycleOscillator> hashMap) {
        KeyCycleOscillator keyCycleOscillator;
        KeyCycleOscillator keyCycleOscillator2;
        HashMap<String, KeyCycleOscillator> hashMap2 = hashMap;
        for (String next : hashMap.keySet()) {
            if (next.startsWith("CUSTOM")) {
                CustomVariable customVariable = (CustomVariable) this.mCustom.get(next.substring("CUSTOM".length() + 1));
                if (!(customVariable == null || customVariable.getType() != 901 || (keyCycleOscillator2 = hashMap2.get(next)) == null)) {
                    keyCycleOscillator2.setPoint(this.mFramePosition, this.mWaveShape, this.mCustomWaveShape, -1, this.mWavePeriod, this.mWaveOffset, this.mWavePhase, customVariable.getValueToInterpolate(), customVariable);
                }
            } else {
                float value = getValue(next);
                if (!Float.isNaN(value) && (keyCycleOscillator = hashMap2.get(next)) != null) {
                    keyCycleOscillator.setPoint(this.mFramePosition, this.mWaveShape, this.mCustomWaveShape, -1, this.mWavePeriod, this.mWaveOffset, this.mWavePhase, value);
                }
            }
        }
    }

    public void addValues(HashMap<String, SplineSet> hashMap) {
    }

    public MotionKey clone() {
        return null;
    }

    public void dump() {
        System.out.println("MotionKeyCycle{mWaveShape=" + this.mWaveShape + ", mWavePeriod=" + this.mWavePeriod + ", mWaveOffset=" + this.mWaveOffset + ", mWavePhase=" + this.mWavePhase + ", mRotation=" + this.mRotation + '}');
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

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getId(java.lang.String r3) {
        /*
            r2 = this;
            int r0 = r3.hashCode()
            r1 = -1
            switch(r0) {
                case -1581616630: goto L_0x00f3;
                case -1310311125: goto L_0x00e8;
                case -1249320806: goto L_0x00dd;
                case -1249320805: goto L_0x00d2;
                case -1249320804: goto L_0x00c6;
                case -1225497657: goto L_0x00bb;
                case -1225497656: goto L_0x00b0;
                case -1225497655: goto L_0x00a5;
                case -1019779949: goto L_0x009a;
                case -1001078227: goto L_0x008f;
                case -991726143: goto L_0x0083;
                case -987906986: goto L_0x0077;
                case -987906985: goto L_0x006b;
                case -908189618: goto L_0x005e;
                case -908189617: goto L_0x0051;
                case 92909918: goto L_0x0046;
                case 106629499: goto L_0x003a;
                case 579057826: goto L_0x002f;
                case 803192288: goto L_0x0023;
                case 1532805160: goto L_0x0016;
                case 1941332754: goto L_0x000a;
                default: goto L_0x0008;
            }
        L_0x0008:
            goto L_0x00fe
        L_0x000a:
            java.lang.String r0 = "visibility"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 1
            goto L_0x00ff
        L_0x0016:
            java.lang.String r0 = "waveShape"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 17
            goto L_0x00ff
        L_0x0023:
            java.lang.String r0 = "pathRotate"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 14
            goto L_0x00ff
        L_0x002f:
            java.lang.String r0 = "curveFit"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 0
            goto L_0x00ff
        L_0x003a:
            java.lang.String r0 = "phase"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 18
            goto L_0x00ff
        L_0x0046:
            java.lang.String r0 = "alpha"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 2
            goto L_0x00ff
        L_0x0051:
            java.lang.String r0 = "scaleY"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 10
            goto L_0x00ff
        L_0x005e:
            java.lang.String r0 = "scaleX"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 9
            goto L_0x00ff
        L_0x006b:
            java.lang.String r0 = "pivotY"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 12
            goto L_0x00ff
        L_0x0077:
            java.lang.String r0 = "pivotX"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 11
            goto L_0x00ff
        L_0x0083:
            java.lang.String r0 = "period"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 16
            goto L_0x00ff
        L_0x008f:
            java.lang.String r0 = "progress"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 13
            goto L_0x00ff
        L_0x009a:
            java.lang.String r0 = "offset"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 19
            goto L_0x00ff
        L_0x00a5:
            java.lang.String r0 = "translationZ"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 5
            goto L_0x00ff
        L_0x00b0:
            java.lang.String r0 = "translationY"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 4
            goto L_0x00ff
        L_0x00bb:
            java.lang.String r0 = "translationX"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 3
            goto L_0x00ff
        L_0x00c6:
            java.lang.String r0 = "rotationZ"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 8
            goto L_0x00ff
        L_0x00d2:
            java.lang.String r0 = "rotationY"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 7
            goto L_0x00ff
        L_0x00dd:
            java.lang.String r0 = "rotationX"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 6
            goto L_0x00ff
        L_0x00e8:
            java.lang.String r0 = "easing"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 15
            goto L_0x00ff
        L_0x00f3:
            java.lang.String r0 = "customWave"
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x0008
            r0 = 20
            goto L_0x00ff
        L_0x00fe:
            r0 = r1
        L_0x00ff:
            switch(r0) {
                case 0: goto L_0x013f;
                case 1: goto L_0x013c;
                case 2: goto L_0x0139;
                case 3: goto L_0x0136;
                case 4: goto L_0x0133;
                case 5: goto L_0x0130;
                case 6: goto L_0x012d;
                case 7: goto L_0x012a;
                case 8: goto L_0x0127;
                case 9: goto L_0x0124;
                case 10: goto L_0x0121;
                case 11: goto L_0x011e;
                case 12: goto L_0x011b;
                case 13: goto L_0x0118;
                case 14: goto L_0x0115;
                case 15: goto L_0x0112;
                case 16: goto L_0x010f;
                case 17: goto L_0x010c;
                case 18: goto L_0x0109;
                case 19: goto L_0x0106;
                case 20: goto L_0x0103;
                default: goto L_0x0102;
            }
        L_0x0102:
            return r1
        L_0x0103:
            r0 = 422(0x1a6, float:5.91E-43)
            return r0
        L_0x0106:
            r0 = 424(0x1a8, float:5.94E-43)
            return r0
        L_0x0109:
            r0 = 425(0x1a9, float:5.96E-43)
            return r0
        L_0x010c:
            r0 = 421(0x1a5, float:5.9E-43)
            return r0
        L_0x010f:
            r0 = 423(0x1a7, float:5.93E-43)
            return r0
        L_0x0112:
            r0 = 420(0x1a4, float:5.89E-43)
            return r0
        L_0x0115:
            r0 = 416(0x1a0, float:5.83E-43)
            return r0
        L_0x0118:
            r0 = 315(0x13b, float:4.41E-43)
            return r0
        L_0x011b:
            r0 = 314(0x13a, float:4.4E-43)
            return r0
        L_0x011e:
            r0 = 313(0x139, float:4.39E-43)
            return r0
        L_0x0121:
            r0 = 312(0x138, float:4.37E-43)
            return r0
        L_0x0124:
            r0 = 311(0x137, float:4.36E-43)
            return r0
        L_0x0127:
            r0 = 310(0x136, float:4.34E-43)
            return r0
        L_0x012a:
            r0 = 309(0x135, float:4.33E-43)
            return r0
        L_0x012d:
            r0 = 308(0x134, float:4.32E-43)
            return r0
        L_0x0130:
            r0 = 306(0x132, float:4.29E-43)
            return r0
        L_0x0133:
            r0 = 305(0x131, float:4.27E-43)
            return r0
        L_0x0136:
            r0 = 304(0x130, float:4.26E-43)
            return r0
        L_0x0139:
            r0 = 403(0x193, float:5.65E-43)
            return r0
        L_0x013c:
            r0 = 402(0x192, float:5.63E-43)
            return r0
        L_0x013f:
            r0 = 401(0x191, float:5.62E-43)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.motion.key.MotionKeyCycle.getId(java.lang.String):int");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public float getValue(java.lang.String r2) {
        /*
            r1 = this;
            int r0 = r2.hashCode()
            switch(r0) {
                case -1249320806: goto L_0x009c;
                case -1249320805: goto L_0x0091;
                case -1249320804: goto L_0x0086;
                case -1225497657: goto L_0x007a;
                case -1225497656: goto L_0x006e;
                case -1225497655: goto L_0x0062;
                case -1019779949: goto L_0x0057;
                case -1001078227: goto L_0x004c;
                case -908189618: goto L_0x0041;
                case -908189617: goto L_0x0036;
                case -4379043: goto L_0x002b;
                case 92909918: goto L_0x0020;
                case 106629499: goto L_0x0014;
                case 803192288: goto L_0x0009;
                default: goto L_0x0007;
            }
        L_0x0007:
            goto L_0x00a7
        L_0x0009:
            java.lang.String r0 = "pathRotate"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 5
            goto L_0x00a8
        L_0x0014:
            java.lang.String r0 = "phase"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 12
            goto L_0x00a8
        L_0x0020:
            java.lang.String r0 = "alpha"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 0
            goto L_0x00a8
        L_0x002b:
            java.lang.String r0 = "elevation"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 1
            goto L_0x00a8
        L_0x0036:
            java.lang.String r0 = "scaleY"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 7
            goto L_0x00a8
        L_0x0041:
            java.lang.String r0 = "scaleX"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 6
            goto L_0x00a8
        L_0x004c:
            java.lang.String r0 = "progress"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 13
            goto L_0x00a8
        L_0x0057:
            java.lang.String r0 = "offset"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 11
            goto L_0x00a8
        L_0x0062:
            java.lang.String r0 = "translationZ"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 10
            goto L_0x00a8
        L_0x006e:
            java.lang.String r0 = "translationY"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 9
            goto L_0x00a8
        L_0x007a:
            java.lang.String r0 = "translationX"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 8
            goto L_0x00a8
        L_0x0086:
            java.lang.String r0 = "rotationZ"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 2
            goto L_0x00a8
        L_0x0091:
            java.lang.String r0 = "rotationY"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 4
            goto L_0x00a8
        L_0x009c:
            java.lang.String r0 = "rotationX"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 3
            goto L_0x00a8
        L_0x00a7:
            r0 = -1
        L_0x00a8:
            switch(r0) {
                case 0: goto L_0x00d5;
                case 1: goto L_0x00d2;
                case 2: goto L_0x00cf;
                case 3: goto L_0x00cc;
                case 4: goto L_0x00c9;
                case 5: goto L_0x00c6;
                case 6: goto L_0x00c3;
                case 7: goto L_0x00c0;
                case 8: goto L_0x00bd;
                case 9: goto L_0x00ba;
                case 10: goto L_0x00b7;
                case 11: goto L_0x00b4;
                case 12: goto L_0x00b1;
                case 13: goto L_0x00ae;
                default: goto L_0x00ab;
            }
        L_0x00ab:
            r0 = 2143289344(0x7fc00000, float:NaN)
            return r0
        L_0x00ae:
            float r0 = r1.mProgress
            return r0
        L_0x00b1:
            float r0 = r1.mWavePhase
            return r0
        L_0x00b4:
            float r0 = r1.mWaveOffset
            return r0
        L_0x00b7:
            float r0 = r1.mTranslationZ
            return r0
        L_0x00ba:
            float r0 = r1.mTranslationY
            return r0
        L_0x00bd:
            float r0 = r1.mTranslationX
            return r0
        L_0x00c0:
            float r0 = r1.mScaleY
            return r0
        L_0x00c3:
            float r0 = r1.mScaleX
            return r0
        L_0x00c6:
            float r0 = r1.mTransitionPathRotate
            return r0
        L_0x00c9:
            float r0 = r1.mRotationY
            return r0
        L_0x00cc:
            float r0 = r1.mRotationX
            return r0
        L_0x00cf:
            float r0 = r1.mRotation
            return r0
        L_0x00d2:
            float r0 = r1.mElevation
            return r0
        L_0x00d5:
            float r0 = r1.mAlpha
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.motion.key.MotionKeyCycle.getValue(java.lang.String):float");
    }

    public void printAttributes() {
        HashSet hashSet = new HashSet();
        getAttributeNames(hashSet);
        Utils.log(" ------------- " + this.mFramePosition + " -------------");
        Utils.log("MotionKeyCycle{Shape=" + this.mWaveShape + ", Period=" + this.mWavePeriod + ", Offset=" + this.mWaveOffset + ", Phase=" + this.mWavePhase + '}');
        String[] strArr = (String[]) hashSet.toArray(new String[0]);
        for (int i = 0; i < strArr.length; i++) {
            int id = TypedValues.AttributesType.getId(strArr[i]);
            Utils.log(strArr[i] + ":" + getValue(strArr[i]));
        }
    }

    public boolean setValue(int type, float value) {
        switch (type) {
            case 304:
                this.mTranslationX = value;
                return true;
            case 305:
                this.mTranslationY = value;
                return true;
            case 306:
                this.mTranslationZ = value;
                return true;
            case 307:
                this.mElevation = value;
                return true;
            case 308:
                this.mRotationX = value;
                return true;
            case 309:
                this.mRotationY = value;
                return true;
            case 310:
                this.mRotation = value;
                return true;
            case 311:
                this.mScaleX = value;
                return true;
            case 312:
                this.mScaleY = value;
                return true;
            case 315:
                this.mProgress = value;
                return true;
            case TypedValues.CycleType.TYPE_ALPHA /*403*/:
                this.mAlpha = value;
                return true;
            case TypedValues.CycleType.TYPE_PATH_ROTATE /*416*/:
                this.mTransitionPathRotate = value;
                return true;
            case TypedValues.CycleType.TYPE_WAVE_PERIOD /*423*/:
                this.mWavePeriod = value;
                return true;
            case TypedValues.CycleType.TYPE_WAVE_OFFSET /*424*/:
                this.mWaveOffset = value;
                return true;
            case TypedValues.CycleType.TYPE_WAVE_PHASE /*425*/:
                this.mWavePhase = value;
                return true;
            default:
                return super.setValue(type, value);
        }
    }

    public boolean setValue(int type, int value) {
        switch (type) {
            case TypedValues.CycleType.TYPE_CURVE_FIT /*401*/:
                this.mCurveFit = value;
                return true;
            case 421:
                this.mWaveShape = value;
                return true;
            default:
                if (setValue(type, (float) value)) {
                    return true;
                }
                return super.setValue(type, value);
        }
    }

    public boolean setValue(int type, String value) {
        switch (type) {
            case TypedValues.CycleType.TYPE_EASING /*420*/:
                this.mTransitionEasing = value;
                return true;
            case TypedValues.CycleType.TYPE_CUSTOM_WAVE_SHAPE /*422*/:
                this.mCustomWaveShape = value;
                return true;
            default:
                return super.setValue(type, value);
        }
    }
}

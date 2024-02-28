package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import androidx.constraintlayout.widget.R;
import java.util.HashMap;
import java.util.HashSet;
import mt.Log1F380D;

public class KeyAttributes extends Key {
    private static final boolean DEBUG = false;
    public static final int KEY_TYPE = 1;
    static final String NAME = "KeyAttribute";
    private static final String TAG = "KeyAttributes";
    /* access modifiers changed from: private */
    public float mAlpha = Float.NaN;
    /* access modifiers changed from: private */
    public int mCurveFit = -1;
    /* access modifiers changed from: private */
    public float mElevation = Float.NaN;
    /* access modifiers changed from: private */
    public float mPivotX = Float.NaN;
    /* access modifiers changed from: private */
    public float mPivotY = Float.NaN;
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
    private boolean mVisibility = false;

    /* compiled from: 001A */
    private static class Loader {
        private static final int ANDROID_ALPHA = 1;
        private static final int ANDROID_ELEVATION = 2;
        private static final int ANDROID_PIVOT_X = 19;
        private static final int ANDROID_PIVOT_Y = 20;
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
        private static SparseIntArray mAttrMap;

        static {
            SparseIntArray sparseIntArray = new SparseIntArray();
            mAttrMap = sparseIntArray;
            sparseIntArray.append(R.styleable.KeyAttribute_android_alpha, 1);
            mAttrMap.append(R.styleable.KeyAttribute_android_elevation, 2);
            mAttrMap.append(R.styleable.KeyAttribute_android_rotation, 4);
            mAttrMap.append(R.styleable.KeyAttribute_android_rotationX, 5);
            mAttrMap.append(R.styleable.KeyAttribute_android_rotationY, 6);
            mAttrMap.append(R.styleable.KeyAttribute_android_transformPivotX, 19);
            mAttrMap.append(R.styleable.KeyAttribute_android_transformPivotY, 20);
            mAttrMap.append(R.styleable.KeyAttribute_android_scaleX, 7);
            mAttrMap.append(R.styleable.KeyAttribute_transitionPathRotate, 8);
            mAttrMap.append(R.styleable.KeyAttribute_transitionEasing, 9);
            mAttrMap.append(R.styleable.KeyAttribute_motionTarget, 10);
            mAttrMap.append(R.styleable.KeyAttribute_framePosition, 12);
            mAttrMap.append(R.styleable.KeyAttribute_curveFit, 13);
            mAttrMap.append(R.styleable.KeyAttribute_android_scaleY, 14);
            mAttrMap.append(R.styleable.KeyAttribute_android_translationX, 15);
            mAttrMap.append(R.styleable.KeyAttribute_android_translationY, 16);
            mAttrMap.append(R.styleable.KeyAttribute_android_translationZ, 17);
            mAttrMap.append(R.styleable.KeyAttribute_motionProgress, 18);
        }

        private Loader() {
        }

        public static void read(KeyAttributes c, TypedArray a) {
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
                        float unused15 = c.mPivotX = a.getDimension(index, c.mPivotX);
                        break;
                    case 20:
                        float unused16 = c.mPivotY = a.getDimension(index, c.mPivotY);
                        break;
                    default:
                        StringBuilder append = new StringBuilder().append("unused attribute 0x");
                        String hexString = Integer.toHexString(index);
                        Log1F380D.a((Object) hexString);
                        Log.e(KeyAttributes.NAME, append.append(hexString).append("   ").append(mAttrMap.get(index)).toString());
                        break;
                }
            }
        }
    }

    public KeyAttributes() {
        this.mType = 1;
        this.mCustomConstraints = new HashMap();
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0069, code lost:
        if (r1.equals("elevation") != false) goto L_0x00ee;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addValues(java.util.HashMap<java.lang.String, androidx.constraintlayout.motion.utils.ViewSpline> r8) {
        /*
            r7 = this;
            java.util.Set r0 = r8.keySet()
            java.util.Iterator r0 = r0.iterator()
        L_0x0008:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x01db
            java.lang.Object r1 = r0.next()
            java.lang.String r1 = (java.lang.String) r1
            java.lang.Object r2 = r8.get(r1)
            androidx.constraintlayout.core.motion.utils.SplineSet r2 = (androidx.constraintlayout.core.motion.utils.SplineSet) r2
            if (r2 != 0) goto L_0x001d
            goto L_0x0008
        L_0x001d:
            java.lang.String r3 = "CUSTOM"
            boolean r4 = r1.startsWith(r3)
            r5 = 1
            if (r4 == 0) goto L_0x0042
            int r3 = r3.length()
            int r3 = r3 + r5
            java.lang.String r3 = r1.substring(r3)
            java.util.HashMap r4 = r7.mCustomConstraints
            java.lang.Object r4 = r4.get(r3)
            androidx.constraintlayout.widget.ConstraintAttribute r4 = (androidx.constraintlayout.widget.ConstraintAttribute) r4
            if (r4 == 0) goto L_0x0008
            r5 = r2
            androidx.constraintlayout.motion.utils.ViewSpline$CustomSet r5 = (androidx.constraintlayout.motion.utils.ViewSpline.CustomSet) r5
            int r6 = r7.mFramePosition
            r5.setPoint((int) r6, (androidx.constraintlayout.widget.ConstraintAttribute) r4)
            goto L_0x0008
        L_0x0042:
            r3 = -1
            int r4 = r1.hashCode()
            switch(r4) {
                case -1249320806: goto L_0x00e2;
                case -1249320805: goto L_0x00d7;
                case -1225497657: goto L_0x00cb;
                case -1225497656: goto L_0x00bf;
                case -1225497655: goto L_0x00b3;
                case -1001078227: goto L_0x00a8;
                case -908189618: goto L_0x009c;
                case -908189617: goto L_0x0090;
                case -760884510: goto L_0x0085;
                case -760884509: goto L_0x0079;
                case -40300674: goto L_0x006d;
                case -4379043: goto L_0x0063;
                case 37232917: goto L_0x0057;
                case 92909918: goto L_0x004c;
                default: goto L_0x004a;
            }
        L_0x004a:
            goto L_0x00ed
        L_0x004c:
            java.lang.String r4 = "alpha"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x004a
            r5 = 0
            goto L_0x00ee
        L_0x0057:
            java.lang.String r4 = "transitionPathRotate"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x004a
            r5 = 7
            goto L_0x00ee
        L_0x0063:
            java.lang.String r4 = "elevation"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x004a
            goto L_0x00ee
        L_0x006d:
            java.lang.String r4 = "rotation"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x004a
            r5 = 2
            goto L_0x00ee
        L_0x0079:
            java.lang.String r4 = "transformPivotY"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x004a
            r5 = 6
            goto L_0x00ee
        L_0x0085:
            java.lang.String r4 = "transformPivotX"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x004a
            r5 = 5
            goto L_0x00ee
        L_0x0090:
            java.lang.String r4 = "scaleY"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x004a
            r5 = 9
            goto L_0x00ee
        L_0x009c:
            java.lang.String r4 = "scaleX"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x004a
            r5 = 8
            goto L_0x00ee
        L_0x00a8:
            java.lang.String r4 = "progress"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x004a
            r5 = 13
            goto L_0x00ee
        L_0x00b3:
            java.lang.String r4 = "translationZ"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x004a
            r5 = 12
            goto L_0x00ee
        L_0x00bf:
            java.lang.String r4 = "translationY"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x004a
            r5 = 11
            goto L_0x00ee
        L_0x00cb:
            java.lang.String r4 = "translationX"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x004a
            r5 = 10
            goto L_0x00ee
        L_0x00d7:
            java.lang.String r4 = "rotationY"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x004a
            r5 = 4
            goto L_0x00ee
        L_0x00e2:
            java.lang.String r4 = "rotationX"
            boolean r4 = r1.equals(r4)
            if (r4 == 0) goto L_0x004a
            r5 = 3
            goto L_0x00ee
        L_0x00ed:
            r5 = r3
        L_0x00ee:
            switch(r5) {
                case 0: goto L_0x01ca;
                case 1: goto L_0x01ba;
                case 2: goto L_0x01aa;
                case 3: goto L_0x019a;
                case 4: goto L_0x018a;
                case 5: goto L_0x017a;
                case 6: goto L_0x016a;
                case 7: goto L_0x0159;
                case 8: goto L_0x0148;
                case 9: goto L_0x0137;
                case 10: goto L_0x0126;
                case 11: goto L_0x0115;
                case 12: goto L_0x0104;
                case 13: goto L_0x00f3;
                default: goto L_0x00f1;
            }
        L_0x00f1:
            goto L_0x01d9
        L_0x00f3:
            float r3 = r7.mProgress
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x01d9
            int r3 = r7.mFramePosition
            float r4 = r7.mProgress
            r2.setPoint(r3, r4)
            goto L_0x01d9
        L_0x0104:
            float r3 = r7.mTranslationZ
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x01d9
            int r3 = r7.mFramePosition
            float r4 = r7.mTranslationZ
            r2.setPoint(r3, r4)
            goto L_0x01d9
        L_0x0115:
            float r3 = r7.mTranslationY
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x01d9
            int r3 = r7.mFramePosition
            float r4 = r7.mTranslationY
            r2.setPoint(r3, r4)
            goto L_0x01d9
        L_0x0126:
            float r3 = r7.mTranslationX
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x01d9
            int r3 = r7.mFramePosition
            float r4 = r7.mTranslationX
            r2.setPoint(r3, r4)
            goto L_0x01d9
        L_0x0137:
            float r3 = r7.mScaleY
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x01d9
            int r3 = r7.mFramePosition
            float r4 = r7.mScaleY
            r2.setPoint(r3, r4)
            goto L_0x01d9
        L_0x0148:
            float r3 = r7.mScaleX
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x01d9
            int r3 = r7.mFramePosition
            float r4 = r7.mScaleX
            r2.setPoint(r3, r4)
            goto L_0x01d9
        L_0x0159:
            float r3 = r7.mTransitionPathRotate
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x01d9
            int r3 = r7.mFramePosition
            float r4 = r7.mTransitionPathRotate
            r2.setPoint(r3, r4)
            goto L_0x01d9
        L_0x016a:
            float r3 = r7.mRotationY
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x01d9
            int r3 = r7.mFramePosition
            float r4 = r7.mPivotY
            r2.setPoint(r3, r4)
            goto L_0x01d9
        L_0x017a:
            float r3 = r7.mRotationX
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x01d9
            int r3 = r7.mFramePosition
            float r4 = r7.mPivotX
            r2.setPoint(r3, r4)
            goto L_0x01d9
        L_0x018a:
            float r3 = r7.mRotationY
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x01d9
            int r3 = r7.mFramePosition
            float r4 = r7.mRotationY
            r2.setPoint(r3, r4)
            goto L_0x01d9
        L_0x019a:
            float r3 = r7.mRotationX
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x01d9
            int r3 = r7.mFramePosition
            float r4 = r7.mRotationX
            r2.setPoint(r3, r4)
            goto L_0x01d9
        L_0x01aa:
            float r3 = r7.mRotation
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x01d9
            int r3 = r7.mFramePosition
            float r4 = r7.mRotation
            r2.setPoint(r3, r4)
            goto L_0x01d9
        L_0x01ba:
            float r3 = r7.mElevation
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x01d9
            int r3 = r7.mFramePosition
            float r4 = r7.mElevation
            r2.setPoint(r3, r4)
            goto L_0x01d9
        L_0x01ca:
            float r3 = r7.mAlpha
            boolean r3 = java.lang.Float.isNaN(r3)
            if (r3 != 0) goto L_0x01d9
            int r3 = r7.mFramePosition
            float r4 = r7.mAlpha
            r2.setPoint(r3, r4)
        L_0x01d9:
            goto L_0x0008
        L_0x01db:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.KeyAttributes.addValues(java.util.HashMap):void");
    }

    public Key clone() {
        return new KeyAttributes().copy(this);
    }

    public Key copy(Key src) {
        super.copy(src);
        KeyAttributes keyAttributes = (KeyAttributes) src;
        this.mCurveFit = keyAttributes.mCurveFit;
        this.mVisibility = keyAttributes.mVisibility;
        this.mAlpha = keyAttributes.mAlpha;
        this.mElevation = keyAttributes.mElevation;
        this.mRotation = keyAttributes.mRotation;
        this.mRotationX = keyAttributes.mRotationX;
        this.mRotationY = keyAttributes.mRotationY;
        this.mPivotX = keyAttributes.mPivotX;
        this.mPivotY = keyAttributes.mPivotY;
        this.mTransitionPathRotate = keyAttributes.mTransitionPathRotate;
        this.mScaleX = keyAttributes.mScaleX;
        this.mScaleY = keyAttributes.mScaleY;
        this.mTranslationX = keyAttributes.mTranslationX;
        this.mTranslationY = keyAttributes.mTranslationY;
        this.mTranslationZ = keyAttributes.mTranslationZ;
        this.mProgress = keyAttributes.mProgress;
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
        if (!Float.isNaN(this.mPivotX)) {
            hashSet.add(Key.PIVOT_X);
        }
        if (!Float.isNaN(this.mPivotY)) {
            hashSet.add(Key.PIVOT_Y);
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

    /* access modifiers changed from: package-private */
    public int getCurveFit() {
        return this.mCurveFit;
    }

    public void load(Context context, AttributeSet attrs) {
        Loader.read(this, context.obtainStyledAttributes(attrs, R.styleable.KeyAttribute));
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
            if (!Float.isNaN(this.mPivotX)) {
                hashMap.put(Key.PIVOT_X, Integer.valueOf(this.mCurveFit));
            }
            if (!Float.isNaN(this.mPivotY)) {
                hashMap.put(Key.PIVOT_Y, Integer.valueOf(this.mCurveFit));
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
            if (!Float.isNaN(this.mScaleY)) {
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
    public void setValue(java.lang.String r2, java.lang.Object r3) {
        /*
            r1 = this;
            int r0 = r2.hashCode()
            switch(r0) {
                case -1913008125: goto L_0x00c6;
                case -1812823328: goto L_0x00ba;
                case -1249320806: goto L_0x00af;
                case -1249320805: goto L_0x00a4;
                case -1225497657: goto L_0x0098;
                case -1225497656: goto L_0x008c;
                case -1225497655: goto L_0x0080;
                case -908189618: goto L_0x0074;
                case -908189617: goto L_0x0068;
                case -760884510: goto L_0x005d;
                case -760884509: goto L_0x0050;
                case -40300674: goto L_0x0044;
                case -4379043: goto L_0x0039;
                case 37232917: goto L_0x002c;
                case 92909918: goto L_0x0021;
                case 579057826: goto L_0x0016;
                case 1941332754: goto L_0x0009;
                default: goto L_0x0007;
            }
        L_0x0007:
            goto L_0x00d0
        L_0x0009:
            java.lang.String r0 = "visibility"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 12
            goto L_0x00d1
        L_0x0016:
            java.lang.String r0 = "curveFit"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 1
            goto L_0x00d1
        L_0x0021:
            java.lang.String r0 = "alpha"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 0
            goto L_0x00d1
        L_0x002c:
            java.lang.String r0 = "transitionPathRotate"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 13
            goto L_0x00d1
        L_0x0039:
            java.lang.String r0 = "elevation"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 2
            goto L_0x00d1
        L_0x0044:
            java.lang.String r0 = "rotation"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 4
            goto L_0x00d1
        L_0x0050:
            java.lang.String r0 = "transformPivotY"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 8
            goto L_0x00d1
        L_0x005d:
            java.lang.String r0 = "transformPivotX"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 7
            goto L_0x00d1
        L_0x0068:
            java.lang.String r0 = "scaleY"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 10
            goto L_0x00d1
        L_0x0074:
            java.lang.String r0 = "scaleX"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 9
            goto L_0x00d1
        L_0x0080:
            java.lang.String r0 = "translationZ"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 16
            goto L_0x00d1
        L_0x008c:
            java.lang.String r0 = "translationY"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 15
            goto L_0x00d1
        L_0x0098:
            java.lang.String r0 = "translationX"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 14
            goto L_0x00d1
        L_0x00a4:
            java.lang.String r0 = "rotationY"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 6
            goto L_0x00d1
        L_0x00af:
            java.lang.String r0 = "rotationX"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 5
            goto L_0x00d1
        L_0x00ba:
            java.lang.String r0 = "transitionEasing"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 11
            goto L_0x00d1
        L_0x00c6:
            java.lang.String r0 = "motionProgress"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 3
            goto L_0x00d1
        L_0x00d0:
            r0 = -1
        L_0x00d1:
            switch(r0) {
                case 0: goto L_0x0148;
                case 1: goto L_0x0141;
                case 2: goto L_0x013a;
                case 3: goto L_0x0133;
                case 4: goto L_0x012c;
                case 5: goto L_0x0125;
                case 6: goto L_0x011e;
                case 7: goto L_0x0117;
                case 8: goto L_0x0110;
                case 9: goto L_0x0109;
                case 10: goto L_0x0102;
                case 11: goto L_0x00fb;
                case 12: goto L_0x00f4;
                case 13: goto L_0x00ed;
                case 14: goto L_0x00e6;
                case 15: goto L_0x00de;
                case 16: goto L_0x00d6;
                default: goto L_0x00d4;
            }
        L_0x00d4:
            goto L_0x014f
        L_0x00d6:
            float r0 = r1.toFloat(r3)
            r1.mTranslationZ = r0
            goto L_0x014f
        L_0x00de:
            float r0 = r1.toFloat(r3)
            r1.mTranslationY = r0
            goto L_0x014f
        L_0x00e6:
            float r0 = r1.toFloat(r3)
            r1.mTranslationX = r0
            goto L_0x014f
        L_0x00ed:
            float r0 = r1.toFloat(r3)
            r1.mTransitionPathRotate = r0
            goto L_0x014f
        L_0x00f4:
            boolean r0 = r1.toBoolean(r3)
            r1.mVisibility = r0
            goto L_0x014f
        L_0x00fb:
            java.lang.String r0 = r3.toString()
            r1.mTransitionEasing = r0
            goto L_0x014f
        L_0x0102:
            float r0 = r1.toFloat(r3)
            r1.mScaleY = r0
            goto L_0x014f
        L_0x0109:
            float r0 = r1.toFloat(r3)
            r1.mScaleX = r0
            goto L_0x014f
        L_0x0110:
            float r0 = r1.toFloat(r3)
            r1.mPivotY = r0
            goto L_0x014f
        L_0x0117:
            float r0 = r1.toFloat(r3)
            r1.mPivotX = r0
            goto L_0x014f
        L_0x011e:
            float r0 = r1.toFloat(r3)
            r1.mRotationY = r0
            goto L_0x014f
        L_0x0125:
            float r0 = r1.toFloat(r3)
            r1.mRotationX = r0
            goto L_0x014f
        L_0x012c:
            float r0 = r1.toFloat(r3)
            r1.mRotation = r0
            goto L_0x014f
        L_0x0133:
            float r0 = r1.toFloat(r3)
            r1.mProgress = r0
            goto L_0x014f
        L_0x013a:
            float r0 = r1.toFloat(r3)
            r1.mElevation = r0
            goto L_0x014f
        L_0x0141:
            int r0 = r1.toInt(r3)
            r1.mCurveFit = r0
            goto L_0x014f
        L_0x0148:
            float r0 = r1.toFloat(r3)
            r1.mAlpha = r0
        L_0x014f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.KeyAttributes.setValue(java.lang.String, java.lang.Object):void");
    }
}

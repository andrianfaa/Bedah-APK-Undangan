package androidx.constraintlayout.motion.utils;

import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.constraintlayout.core.motion.utils.CurveFit;
import androidx.constraintlayout.core.motion.utils.KeyCache;
import androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintAttribute;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class ViewTimeCycle extends TimeCycleSplineSet {
    private static final String TAG = "ViewTimeCycle";

    static class AlphaSet extends ViewTimeCycle {
        AlphaSet() {
        }

        public boolean setProperty(View view, float t, long time, KeyCache cache) {
            view.setAlpha(get(t, time, view, cache));
            return this.mContinue;
        }
    }

    public static class CustomSet extends ViewTimeCycle {
        String mAttributeName;
        float[] mCache;
        SparseArray<ConstraintAttribute> mConstraintAttributeList;
        float[] mTempValues;
        SparseArray<float[]> mWaveProperties = new SparseArray<>();

        public CustomSet(String attribute, SparseArray<ConstraintAttribute> sparseArray) {
            this.mAttributeName = attribute.split(",")[1];
            this.mConstraintAttributeList = sparseArray;
        }

        public void setPoint(int position, float value, float period, int shape, float offset) {
            throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute,...)");
        }

        public void setPoint(int position, ConstraintAttribute value, float period, int shape, float offset) {
            this.mConstraintAttributeList.append(position, value);
            this.mWaveProperties.append(position, new float[]{period, offset});
            this.mWaveShape = Math.max(this.mWaveShape, shape);
        }

        public boolean setProperty(View view, float t, long time, KeyCache cache) {
            View view2 = view;
            long j = time;
            this.mCurveFit.getPos((double) t, this.mTempValues);
            float[] fArr = this.mTempValues;
            float f = fArr[fArr.length - 2];
            float f2 = fArr[fArr.length - 1];
            long j2 = j - this.last_time;
            if (Float.isNaN(this.last_cycle)) {
                this.last_cycle = cache.getFloatValue(view2, this.mAttributeName, 0);
                if (Float.isNaN(this.last_cycle)) {
                    this.last_cycle = 0.0f;
                }
            } else {
                KeyCache keyCache = cache;
            }
            this.last_cycle = (float) ((((double) this.last_cycle) + ((((double) j2) * 1.0E-9d) * ((double) f))) % 1.0d);
            this.last_time = j;
            float calcWave = calcWave(this.last_cycle);
            this.mContinue = false;
            for (int i = 0; i < this.mCache.length; i++) {
                this.mContinue |= ((double) this.mTempValues[i]) != 0.0d;
                this.mCache[i] = (this.mTempValues[i] * calcWave) + f2;
            }
            CustomSupport.setInterpolatedValue(this.mConstraintAttributeList.valueAt(0), view2, this.mCache);
            if (f != 0.0f) {
                this.mContinue = true;
            }
            return this.mContinue;
        }

        public void setup(int curveType) {
            int size = this.mConstraintAttributeList.size();
            int numberOfInterpolatedValues = this.mConstraintAttributeList.valueAt(0).numberOfInterpolatedValues();
            double[] dArr = new double[size];
            this.mTempValues = new float[(numberOfInterpolatedValues + 2)];
            this.mCache = new float[numberOfInterpolatedValues];
            int[] iArr = new int[2];
            iArr[1] = numberOfInterpolatedValues + 2;
            iArr[0] = size;
            double[][] dArr2 = (double[][]) Array.newInstance(Double.TYPE, iArr);
            for (int i = 0; i < size; i++) {
                int keyAt = this.mConstraintAttributeList.keyAt(i);
                float[] valueAt = this.mWaveProperties.valueAt(i);
                dArr[i] = ((double) keyAt) * 0.01d;
                this.mConstraintAttributeList.valueAt(i).getValuesToInterpolate(this.mTempValues);
                int i2 = 0;
                while (true) {
                    float[] fArr = this.mTempValues;
                    if (i2 >= fArr.length) {
                        break;
                    }
                    dArr2[i][i2] = (double) fArr[i2];
                    i2++;
                }
                dArr2[i][numberOfInterpolatedValues] = (double) valueAt[0];
                dArr2[i][numberOfInterpolatedValues + 1] = (double) valueAt[1];
            }
            this.mCurveFit = CurveFit.get(curveType, dArr, dArr2);
        }
    }

    static class ElevationSet extends ViewTimeCycle {
        ElevationSet() {
        }

        public boolean setProperty(View view, float t, long time, KeyCache cache) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setElevation(get(t, time, view, cache));
            }
            return this.mContinue;
        }
    }

    public static class PathRotate extends ViewTimeCycle {
        public boolean setPathRotate(View view, KeyCache cache, float t, long time, double dx, double dy) {
            view.setRotation(get(t, time, view, cache) + ((float) Math.toDegrees(Math.atan2(dy, dx))));
            return this.mContinue;
        }

        public boolean setProperty(View view, float t, long time, KeyCache cache) {
            return this.mContinue;
        }
    }

    static class ProgressSet extends ViewTimeCycle {
        boolean mNoMethod = false;

        ProgressSet() {
        }

        public boolean setProperty(View view, float t, long time, KeyCache cache) {
            Method method;
            View view2 = view;
            if (view2 instanceof MotionLayout) {
                ((MotionLayout) view2).setProgress(get(t, time, view, cache));
            } else if (this.mNoMethod) {
                return false;
            } else {
                try {
                    method = view.getClass().getMethod("setProgress", new Class[]{Float.TYPE});
                } catch (NoSuchMethodException e) {
                    this.mNoMethod = true;
                    method = null;
                }
                if (method != null) {
                    try {
                        method.invoke(view, new Object[]{Float.valueOf(get(t, time, view, cache))});
                    } catch (IllegalAccessException e2) {
                        Log.e(ViewTimeCycle.TAG, "unable to setProgress", e2);
                    } catch (InvocationTargetException e3) {
                        Log.e(ViewTimeCycle.TAG, "unable to setProgress", e3);
                    }
                }
            }
            return this.mContinue;
        }
    }

    static class RotationSet extends ViewTimeCycle {
        RotationSet() {
        }

        public boolean setProperty(View view, float t, long time, KeyCache cache) {
            view.setRotation(get(t, time, view, cache));
            return this.mContinue;
        }
    }

    static class RotationXset extends ViewTimeCycle {
        RotationXset() {
        }

        public boolean setProperty(View view, float t, long time, KeyCache cache) {
            view.setRotationX(get(t, time, view, cache));
            return this.mContinue;
        }
    }

    static class RotationYset extends ViewTimeCycle {
        RotationYset() {
        }

        public boolean setProperty(View view, float t, long time, KeyCache cache) {
            view.setRotationY(get(t, time, view, cache));
            return this.mContinue;
        }
    }

    static class ScaleXset extends ViewTimeCycle {
        ScaleXset() {
        }

        public boolean setProperty(View view, float t, long time, KeyCache cache) {
            view.setScaleX(get(t, time, view, cache));
            return this.mContinue;
        }
    }

    static class ScaleYset extends ViewTimeCycle {
        ScaleYset() {
        }

        public boolean setProperty(View view, float t, long time, KeyCache cache) {
            view.setScaleY(get(t, time, view, cache));
            return this.mContinue;
        }
    }

    static class TranslationXset extends ViewTimeCycle {
        TranslationXset() {
        }

        public boolean setProperty(View view, float t, long time, KeyCache cache) {
            view.setTranslationX(get(t, time, view, cache));
            return this.mContinue;
        }
    }

    static class TranslationYset extends ViewTimeCycle {
        TranslationYset() {
        }

        public boolean setProperty(View view, float t, long time, KeyCache cache) {
            view.setTranslationY(get(t, time, view, cache));
            return this.mContinue;
        }
    }

    static class TranslationZset extends ViewTimeCycle {
        TranslationZset() {
        }

        public boolean setProperty(View view, float t, long time, KeyCache cache) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setTranslationZ(get(t, time, view, cache));
            }
            return this.mContinue;
        }
    }

    public static ViewTimeCycle makeCustomSpline(String str, SparseArray<ConstraintAttribute> sparseArray) {
        return new CustomSet(str, sparseArray);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static androidx.constraintlayout.motion.utils.ViewTimeCycle makeSpline(java.lang.String r1, long r2) {
        /*
            int r0 = r1.hashCode()
            switch(r0) {
                case -1249320806: goto L_0x0085;
                case -1249320805: goto L_0x007a;
                case -1225497657: goto L_0x006e;
                case -1225497656: goto L_0x0062;
                case -1225497655: goto L_0x0056;
                case -1001078227: goto L_0x004b;
                case -908189618: goto L_0x0040;
                case -908189617: goto L_0x0035;
                case -40300674: goto L_0x002a;
                case -4379043: goto L_0x0020;
                case 37232917: goto L_0x0014;
                case 92909918: goto L_0x0009;
                default: goto L_0x0007;
            }
        L_0x0007:
            goto L_0x0090
        L_0x0009:
            java.lang.String r0 = "alpha"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 0
            goto L_0x0091
        L_0x0014:
            java.lang.String r0 = "transitionPathRotate"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 5
            goto L_0x0091
        L_0x0020:
            java.lang.String r0 = "elevation"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 1
            goto L_0x0091
        L_0x002a:
            java.lang.String r0 = "rotation"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 2
            goto L_0x0091
        L_0x0035:
            java.lang.String r0 = "scaleY"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 7
            goto L_0x0091
        L_0x0040:
            java.lang.String r0 = "scaleX"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 6
            goto L_0x0091
        L_0x004b:
            java.lang.String r0 = "progress"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 11
            goto L_0x0091
        L_0x0056:
            java.lang.String r0 = "translationZ"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 10
            goto L_0x0091
        L_0x0062:
            java.lang.String r0 = "translationY"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 9
            goto L_0x0091
        L_0x006e:
            java.lang.String r0 = "translationX"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 8
            goto L_0x0091
        L_0x007a:
            java.lang.String r0 = "rotationY"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 4
            goto L_0x0091
        L_0x0085:
            java.lang.String r0 = "rotationX"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 3
            goto L_0x0091
        L_0x0090:
            r0 = -1
        L_0x0091:
            switch(r0) {
                case 0: goto L_0x00d8;
                case 1: goto L_0x00d2;
                case 2: goto L_0x00cc;
                case 3: goto L_0x00c6;
                case 4: goto L_0x00c0;
                case 5: goto L_0x00ba;
                case 6: goto L_0x00b4;
                case 7: goto L_0x00ae;
                case 8: goto L_0x00a8;
                case 9: goto L_0x00a2;
                case 10: goto L_0x009c;
                case 11: goto L_0x0096;
                default: goto L_0x0094;
            }
        L_0x0094:
            r0 = 0
            return r0
        L_0x0096:
            androidx.constraintlayout.motion.utils.ViewTimeCycle$ProgressSet r0 = new androidx.constraintlayout.motion.utils.ViewTimeCycle$ProgressSet
            r0.<init>()
            goto L_0x00de
        L_0x009c:
            androidx.constraintlayout.motion.utils.ViewTimeCycle$TranslationZset r0 = new androidx.constraintlayout.motion.utils.ViewTimeCycle$TranslationZset
            r0.<init>()
            goto L_0x00de
        L_0x00a2:
            androidx.constraintlayout.motion.utils.ViewTimeCycle$TranslationYset r0 = new androidx.constraintlayout.motion.utils.ViewTimeCycle$TranslationYset
            r0.<init>()
            goto L_0x00de
        L_0x00a8:
            androidx.constraintlayout.motion.utils.ViewTimeCycle$TranslationXset r0 = new androidx.constraintlayout.motion.utils.ViewTimeCycle$TranslationXset
            r0.<init>()
            goto L_0x00de
        L_0x00ae:
            androidx.constraintlayout.motion.utils.ViewTimeCycle$ScaleYset r0 = new androidx.constraintlayout.motion.utils.ViewTimeCycle$ScaleYset
            r0.<init>()
            goto L_0x00de
        L_0x00b4:
            androidx.constraintlayout.motion.utils.ViewTimeCycle$ScaleXset r0 = new androidx.constraintlayout.motion.utils.ViewTimeCycle$ScaleXset
            r0.<init>()
            goto L_0x00de
        L_0x00ba:
            androidx.constraintlayout.motion.utils.ViewTimeCycle$PathRotate r0 = new androidx.constraintlayout.motion.utils.ViewTimeCycle$PathRotate
            r0.<init>()
            goto L_0x00de
        L_0x00c0:
            androidx.constraintlayout.motion.utils.ViewTimeCycle$RotationYset r0 = new androidx.constraintlayout.motion.utils.ViewTimeCycle$RotationYset
            r0.<init>()
            goto L_0x00de
        L_0x00c6:
            androidx.constraintlayout.motion.utils.ViewTimeCycle$RotationXset r0 = new androidx.constraintlayout.motion.utils.ViewTimeCycle$RotationXset
            r0.<init>()
            goto L_0x00de
        L_0x00cc:
            androidx.constraintlayout.motion.utils.ViewTimeCycle$RotationSet r0 = new androidx.constraintlayout.motion.utils.ViewTimeCycle$RotationSet
            r0.<init>()
            goto L_0x00de
        L_0x00d2:
            androidx.constraintlayout.motion.utils.ViewTimeCycle$ElevationSet r0 = new androidx.constraintlayout.motion.utils.ViewTimeCycle$ElevationSet
            r0.<init>()
            goto L_0x00de
        L_0x00d8:
            androidx.constraintlayout.motion.utils.ViewTimeCycle$AlphaSet r0 = new androidx.constraintlayout.motion.utils.ViewTimeCycle$AlphaSet
            r0.<init>()
        L_0x00de:
            r0.setStartTime(r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.utils.ViewTimeCycle.makeSpline(java.lang.String, long):androidx.constraintlayout.motion.utils.ViewTimeCycle");
    }

    public float get(float pos, long time, View view, KeyCache cache) {
        long j = time;
        View view2 = view;
        KeyCache keyCache = cache;
        this.mCurveFit.getPos((double) pos, this.mCache);
        float f = this.mCache[1];
        boolean z = false;
        if (f == 0.0f) {
            this.mContinue = false;
            return this.mCache[2];
        }
        if (Float.isNaN(this.last_cycle)) {
            this.last_cycle = keyCache.getFloatValue(view2, this.mType, 0);
            if (Float.isNaN(this.last_cycle)) {
                this.last_cycle = 0.0f;
            }
        }
        this.last_cycle = (float) ((((double) this.last_cycle) + ((((double) (j - this.last_time)) * 1.0E-9d) * ((double) f))) % 1.0d);
        keyCache.setFloatValue(view2, this.mType, 0, this.last_cycle);
        this.last_time = j;
        float f2 = this.mCache[0];
        float calcWave = (f2 * calcWave(this.last_cycle)) + this.mCache[2];
        if (!(f2 == 0.0f && f == 0.0f)) {
            z = true;
        }
        this.mContinue = z;
        return calcWave;
    }

    public abstract boolean setProperty(View view, float f, long j, KeyCache keyCache);
}

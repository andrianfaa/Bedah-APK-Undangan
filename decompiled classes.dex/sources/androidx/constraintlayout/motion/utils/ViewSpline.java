package androidx.constraintlayout.motion.utils;

import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.constraintlayout.core.motion.utils.CurveFit;
import androidx.constraintlayout.core.motion.utils.SplineSet;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintAttribute;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class ViewSpline extends SplineSet {
    private static final String TAG = "ViewSpline";

    static class AlphaSet extends ViewSpline {
        AlphaSet() {
        }

        public void setProperty(View view, float t) {
            view.setAlpha(get(t));
        }
    }

    public static class CustomSet extends ViewSpline {
        String mAttributeName;
        SparseArray<ConstraintAttribute> mConstraintAttributeList;
        float[] mTempValues;

        public CustomSet(String attribute, SparseArray<ConstraintAttribute> sparseArray) {
            this.mAttributeName = attribute.split(",")[1];
            this.mConstraintAttributeList = sparseArray;
        }

        public void setPoint(int position, float value) {
            throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute)");
        }

        public void setPoint(int position, ConstraintAttribute value) {
            this.mConstraintAttributeList.append(position, value);
        }

        public void setProperty(View view, float t) {
            this.mCurveFit.getPos((double) t, this.mTempValues);
            CustomSupport.setInterpolatedValue(this.mConstraintAttributeList.valueAt(0), view, this.mTempValues);
        }

        public void setup(int curveType) {
            int size = this.mConstraintAttributeList.size();
            int numberOfInterpolatedValues = this.mConstraintAttributeList.valueAt(0).numberOfInterpolatedValues();
            double[] dArr = new double[size];
            this.mTempValues = new float[numberOfInterpolatedValues];
            int[] iArr = new int[2];
            iArr[1] = numberOfInterpolatedValues;
            iArr[0] = size;
            double[][] dArr2 = (double[][]) Array.newInstance(Double.TYPE, iArr);
            for (int i = 0; i < size; i++) {
                dArr[i] = ((double) this.mConstraintAttributeList.keyAt(i)) * 0.01d;
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
            }
            this.mCurveFit = CurveFit.get(curveType, dArr, dArr2);
        }
    }

    static class ElevationSet extends ViewSpline {
        ElevationSet() {
        }

        public void setProperty(View view, float t) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setElevation(get(t));
            }
        }
    }

    public static class PathRotate extends ViewSpline {
        public void setPathRotate(View view, float t, double dx, double dy) {
            view.setRotation(get(t) + ((float) Math.toDegrees(Math.atan2(dy, dx))));
        }

        public void setProperty(View view, float t) {
        }
    }

    static class PivotXset extends ViewSpline {
        PivotXset() {
        }

        public void setProperty(View view, float t) {
            view.setPivotX(get(t));
        }
    }

    static class PivotYset extends ViewSpline {
        PivotYset() {
        }

        public void setProperty(View view, float t) {
            view.setPivotY(get(t));
        }
    }

    static class ProgressSet extends ViewSpline {
        boolean mNoMethod = false;

        ProgressSet() {
        }

        public void setProperty(View view, float t) {
            if (view instanceof MotionLayout) {
                ((MotionLayout) view).setProgress(get(t));
            } else if (!this.mNoMethod) {
                Method method = null;
                try {
                    method = view.getClass().getMethod("setProgress", new Class[]{Float.TYPE});
                } catch (NoSuchMethodException e) {
                    this.mNoMethod = true;
                }
                if (method != null) {
                    try {
                        method.invoke(view, new Object[]{Float.valueOf(get(t))});
                    } catch (IllegalAccessException e2) {
                        Log.e(ViewSpline.TAG, "unable to setProgress", e2);
                    } catch (InvocationTargetException e3) {
                        Log.e(ViewSpline.TAG, "unable to setProgress", e3);
                    }
                }
            }
        }
    }

    static class RotationSet extends ViewSpline {
        RotationSet() {
        }

        public void setProperty(View view, float t) {
            view.setRotation(get(t));
        }
    }

    static class RotationXset extends ViewSpline {
        RotationXset() {
        }

        public void setProperty(View view, float t) {
            view.setRotationX(get(t));
        }
    }

    static class RotationYset extends ViewSpline {
        RotationYset() {
        }

        public void setProperty(View view, float t) {
            view.setRotationY(get(t));
        }
    }

    static class ScaleXset extends ViewSpline {
        ScaleXset() {
        }

        public void setProperty(View view, float t) {
            view.setScaleX(get(t));
        }
    }

    static class ScaleYset extends ViewSpline {
        ScaleYset() {
        }

        public void setProperty(View view, float t) {
            view.setScaleY(get(t));
        }
    }

    static class TranslationXset extends ViewSpline {
        TranslationXset() {
        }

        public void setProperty(View view, float t) {
            view.setTranslationX(get(t));
        }
    }

    static class TranslationYset extends ViewSpline {
        TranslationYset() {
        }

        public void setProperty(View view, float t) {
            view.setTranslationY(get(t));
        }
    }

    static class TranslationZset extends ViewSpline {
        TranslationZset() {
        }

        public void setProperty(View view, float t) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setTranslationZ(get(t));
            }
        }
    }

    public static ViewSpline makeCustomSpline(String str, SparseArray<ConstraintAttribute> sparseArray) {
        return new CustomSet(str, sparseArray);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static androidx.constraintlayout.motion.utils.ViewSpline makeSpline(java.lang.String r1) {
        /*
            int r0 = r1.hashCode()
            switch(r0) {
                case -1249320806: goto L_0x00ba;
                case -1249320805: goto L_0x00af;
                case -1225497657: goto L_0x00a3;
                case -1225497656: goto L_0x0097;
                case -1225497655: goto L_0x008b;
                case -1001078227: goto L_0x0080;
                case -908189618: goto L_0x0074;
                case -908189617: goto L_0x0068;
                case -797520672: goto L_0x005c;
                case -760884510: goto L_0x0050;
                case -760884509: goto L_0x0044;
                case -40300674: goto L_0x0038;
                case -4379043: goto L_0x002d;
                case 37232917: goto L_0x0021;
                case 92909918: goto L_0x0016;
                case 156108012: goto L_0x0009;
                default: goto L_0x0007;
            }
        L_0x0007:
            goto L_0x00c5
        L_0x0009:
            java.lang.String r0 = "waveOffset"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 10
            goto L_0x00c6
        L_0x0016:
            java.lang.String r0 = "alpha"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 0
            goto L_0x00c6
        L_0x0021:
            java.lang.String r0 = "transitionPathRotate"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 7
            goto L_0x00c6
        L_0x002d:
            java.lang.String r0 = "elevation"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 1
            goto L_0x00c6
        L_0x0038:
            java.lang.String r0 = "rotation"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 2
            goto L_0x00c6
        L_0x0044:
            java.lang.String r0 = "transformPivotY"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 6
            goto L_0x00c6
        L_0x0050:
            java.lang.String r0 = "transformPivotX"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 5
            goto L_0x00c6
        L_0x005c:
            java.lang.String r0 = "waveVariesBy"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 11
            goto L_0x00c6
        L_0x0068:
            java.lang.String r0 = "scaleY"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 9
            goto L_0x00c6
        L_0x0074:
            java.lang.String r0 = "scaleX"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 8
            goto L_0x00c6
        L_0x0080:
            java.lang.String r0 = "progress"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 15
            goto L_0x00c6
        L_0x008b:
            java.lang.String r0 = "translationZ"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 14
            goto L_0x00c6
        L_0x0097:
            java.lang.String r0 = "translationY"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 13
            goto L_0x00c6
        L_0x00a3:
            java.lang.String r0 = "translationX"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 12
            goto L_0x00c6
        L_0x00af:
            java.lang.String r0 = "rotationY"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 4
            goto L_0x00c6
        L_0x00ba:
            java.lang.String r0 = "rotationX"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0007
            r0 = 3
            goto L_0x00c6
        L_0x00c5:
            r0 = -1
        L_0x00c6:
            switch(r0) {
                case 0: goto L_0x0125;
                case 1: goto L_0x011f;
                case 2: goto L_0x0119;
                case 3: goto L_0x0113;
                case 4: goto L_0x010d;
                case 5: goto L_0x0107;
                case 6: goto L_0x0101;
                case 7: goto L_0x00fb;
                case 8: goto L_0x00f5;
                case 9: goto L_0x00ef;
                case 10: goto L_0x00e9;
                case 11: goto L_0x00e3;
                case 12: goto L_0x00dd;
                case 13: goto L_0x00d7;
                case 14: goto L_0x00d1;
                case 15: goto L_0x00cb;
                default: goto L_0x00c9;
            }
        L_0x00c9:
            r0 = 0
            return r0
        L_0x00cb:
            androidx.constraintlayout.motion.utils.ViewSpline$ProgressSet r0 = new androidx.constraintlayout.motion.utils.ViewSpline$ProgressSet
            r0.<init>()
            return r0
        L_0x00d1:
            androidx.constraintlayout.motion.utils.ViewSpline$TranslationZset r0 = new androidx.constraintlayout.motion.utils.ViewSpline$TranslationZset
            r0.<init>()
            return r0
        L_0x00d7:
            androidx.constraintlayout.motion.utils.ViewSpline$TranslationYset r0 = new androidx.constraintlayout.motion.utils.ViewSpline$TranslationYset
            r0.<init>()
            return r0
        L_0x00dd:
            androidx.constraintlayout.motion.utils.ViewSpline$TranslationXset r0 = new androidx.constraintlayout.motion.utils.ViewSpline$TranslationXset
            r0.<init>()
            return r0
        L_0x00e3:
            androidx.constraintlayout.motion.utils.ViewSpline$AlphaSet r0 = new androidx.constraintlayout.motion.utils.ViewSpline$AlphaSet
            r0.<init>()
            return r0
        L_0x00e9:
            androidx.constraintlayout.motion.utils.ViewSpline$AlphaSet r0 = new androidx.constraintlayout.motion.utils.ViewSpline$AlphaSet
            r0.<init>()
            return r0
        L_0x00ef:
            androidx.constraintlayout.motion.utils.ViewSpline$ScaleYset r0 = new androidx.constraintlayout.motion.utils.ViewSpline$ScaleYset
            r0.<init>()
            return r0
        L_0x00f5:
            androidx.constraintlayout.motion.utils.ViewSpline$ScaleXset r0 = new androidx.constraintlayout.motion.utils.ViewSpline$ScaleXset
            r0.<init>()
            return r0
        L_0x00fb:
            androidx.constraintlayout.motion.utils.ViewSpline$PathRotate r0 = new androidx.constraintlayout.motion.utils.ViewSpline$PathRotate
            r0.<init>()
            return r0
        L_0x0101:
            androidx.constraintlayout.motion.utils.ViewSpline$PivotYset r0 = new androidx.constraintlayout.motion.utils.ViewSpline$PivotYset
            r0.<init>()
            return r0
        L_0x0107:
            androidx.constraintlayout.motion.utils.ViewSpline$PivotXset r0 = new androidx.constraintlayout.motion.utils.ViewSpline$PivotXset
            r0.<init>()
            return r0
        L_0x010d:
            androidx.constraintlayout.motion.utils.ViewSpline$RotationYset r0 = new androidx.constraintlayout.motion.utils.ViewSpline$RotationYset
            r0.<init>()
            return r0
        L_0x0113:
            androidx.constraintlayout.motion.utils.ViewSpline$RotationXset r0 = new androidx.constraintlayout.motion.utils.ViewSpline$RotationXset
            r0.<init>()
            return r0
        L_0x0119:
            androidx.constraintlayout.motion.utils.ViewSpline$RotationSet r0 = new androidx.constraintlayout.motion.utils.ViewSpline$RotationSet
            r0.<init>()
            return r0
        L_0x011f:
            androidx.constraintlayout.motion.utils.ViewSpline$ElevationSet r0 = new androidx.constraintlayout.motion.utils.ViewSpline$ElevationSet
            r0.<init>()
            return r0
        L_0x0125:
            androidx.constraintlayout.motion.utils.ViewSpline$AlphaSet r0 = new androidx.constraintlayout.motion.utils.ViewSpline$AlphaSet
            r0.<init>()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.utils.ViewSpline.makeSpline(java.lang.String):androidx.constraintlayout.motion.utils.ViewSpline");
    }

    public abstract void setProperty(View view, float f);
}

package androidx.vectordrawable.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.InflateException;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.PathParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import mt.Log1F380D;
import okhttp3.HttpUrl;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: 0098 */
public class AnimatorInflaterCompat {
    private static final boolean DBG_ANIMATOR_INFLATER = false;
    private static final int MAX_NUM_POINTS = 100;
    private static final String TAG = "AnimatorInflater";
    private static final int TOGETHER = 0;
    private static final int VALUE_TYPE_COLOR = 3;
    private static final int VALUE_TYPE_FLOAT = 0;
    private static final int VALUE_TYPE_INT = 1;
    private static final int VALUE_TYPE_PATH = 2;
    private static final int VALUE_TYPE_UNDEFINED = 4;

    private static class PathDataEvaluator implements TypeEvaluator<PathParser.PathDataNode[]> {
        private PathParser.PathDataNode[] mNodeArray;

        PathDataEvaluator() {
        }

        PathDataEvaluator(PathParser.PathDataNode[] nodeArray) {
            this.mNodeArray = nodeArray;
        }

        public PathParser.PathDataNode[] evaluate(float fraction, PathParser.PathDataNode[] startPathData, PathParser.PathDataNode[] endPathData) {
            if (PathParser.canMorph(startPathData, endPathData)) {
                if (!PathParser.canMorph(this.mNodeArray, startPathData)) {
                    this.mNodeArray = PathParser.deepCopyNodes(startPathData);
                }
                for (int i = 0; i < startPathData.length; i++) {
                    this.mNodeArray[i].interpolatePathDataNode(startPathData[i], endPathData[i], fraction);
                }
                return this.mNodeArray;
            }
            throw new IllegalArgumentException("Can't interpolate between two incompatible pathData");
        }
    }

    private AnimatorInflaterCompat() {
    }

    private static Animator createAnimatorFromXml(Context context, Resources res, Resources.Theme theme, XmlPullParser parser, float pixelSize) throws XmlPullParserException, IOException {
        return createAnimatorFromXml(context, res, theme, parser, Xml.asAttributeSet(parser), (AnimatorSet) null, 0, pixelSize);
    }

    private static Animator createAnimatorFromXml(Context context, Resources res, Resources.Theme theme, XmlPullParser parser, AttributeSet attrs, AnimatorSet parent, int sequenceOrdering, float pixelSize) throws XmlPullParserException, IOException {
        Resources resources = res;
        Resources.Theme theme2 = theme;
        XmlPullParser xmlPullParser = parser;
        AnimatorSet animatorSet = parent;
        int depth = parser.getDepth();
        ObjectAnimator objectAnimator = null;
        ArrayList arrayList = null;
        while (true) {
            int next = parser.next();
            int i = next;
            if (next != 3 || parser.getDepth() > depth) {
                if (i == 1) {
                    Context context2 = context;
                    break;
                } else if (i == 2) {
                    String name = parser.getName();
                    boolean z = false;
                    if (name.equals("objectAnimator")) {
                        Context context3 = context;
                        objectAnimator = loadObjectAnimator(context, res, theme, attrs, pixelSize, parser);
                    } else if (name.equals("animator")) {
                        Context context4 = context;
                        objectAnimator = loadAnimator(context, res, theme, attrs, (ValueAnimator) null, pixelSize, parser);
                    } else if (name.equals("set")) {
                        AnimatorSet animatorSet2 = new AnimatorSet();
                        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme2, attrs, AndroidResources.STYLEABLE_ANIMATOR_SET);
                        createAnimatorFromXml(context, res, theme, parser, attrs, animatorSet2, TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser, "ordering", 0, 0), pixelSize);
                        obtainAttributes.recycle();
                        Context context5 = context;
                        objectAnimator = animatorSet2;
                    } else if (name.equals("propertyValuesHolder")) {
                        PropertyValuesHolder[] loadValues = loadValues(context, resources, theme2, xmlPullParser, Xml.asAttributeSet(parser));
                        if (loadValues != null && (objectAnimator instanceof ValueAnimator)) {
                            objectAnimator.setValues(loadValues);
                        }
                        z = true;
                    } else {
                        Context context6 = context;
                        throw new RuntimeException("Unknown animator name: " + parser.getName());
                    }
                    if (animatorSet != null && !z) {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(objectAnimator);
                    }
                }
            } else {
                Context context7 = context;
                break;
            }
        }
        if (!(animatorSet == null || arrayList == null)) {
            Animator[] animatorArr = new Animator[arrayList.size()];
            int i2 = 0;
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                animatorArr[i2] = (Animator) it.next();
                i2++;
            }
            if (sequenceOrdering == 0) {
                animatorSet.playTogether(animatorArr);
            } else {
                animatorSet.playSequentially(animatorArr);
            }
        }
        return objectAnimator;
    }

    private static Keyframe createNewKeyframe(Keyframe sampleKeyframe, float fraction) {
        return sampleKeyframe.getType() == Float.TYPE ? Keyframe.ofFloat(fraction) : sampleKeyframe.getType() == Integer.TYPE ? Keyframe.ofInt(fraction) : Keyframe.ofObject(fraction);
    }

    private static void distributeKeyframes(Keyframe[] keyframes, float gap, int startIndex, int endIndex) {
        float f = gap / ((float) ((endIndex - startIndex) + 2));
        for (int i = startIndex; i <= endIndex; i++) {
            keyframes[i].setFraction(keyframes[i - 1].getFraction() + f);
        }
    }

    private static void dumpKeyframes(Object[] keyframes, String header) {
        if (keyframes != null && keyframes.length != 0) {
            Log.d(TAG, header);
            int length = keyframes.length;
            for (int i = 0; i < length; i++) {
                Keyframe keyframe = keyframes[i];
                Object obj = "null";
                StringBuilder append = new StringBuilder().append("Keyframe ").append(i).append(": fraction ").append(keyframe.getFraction() < 0.0f ? obj : Float.valueOf(keyframe.getFraction())).append(", , value : ");
                if (keyframe.hasValue()) {
                    obj = keyframe.getValue();
                }
                Log.d(TAG, append.append(obj).toString());
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v7, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v6, resolved type: java.lang.Object[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v11, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.animation.PropertyValuesHolder getPVH(android.content.res.TypedArray r22, int r23, int r24, int r25, java.lang.String r26) {
        /*
            r0 = r22
            r1 = r24
            r2 = r25
            r3 = r26
            android.util.TypedValue r4 = r0.peekValue(r1)
            if (r4 == 0) goto L_0x0010
            r7 = 1
            goto L_0x0011
        L_0x0010:
            r7 = 0
        L_0x0011:
            if (r7 == 0) goto L_0x0016
            int r8 = r4.type
            goto L_0x0017
        L_0x0016:
            r8 = 0
        L_0x0017:
            android.util.TypedValue r9 = r0.peekValue(r2)
            if (r9 == 0) goto L_0x001f
            r10 = 1
            goto L_0x0020
        L_0x001f:
            r10 = 0
        L_0x0020:
            if (r10 == 0) goto L_0x0025
            int r11 = r9.type
            goto L_0x0026
        L_0x0025:
            r11 = 0
        L_0x0026:
            r12 = 4
            r13 = r23
            if (r13 != r12) goto L_0x003f
            if (r7 == 0) goto L_0x0033
            boolean r12 = isColorType(r8)
            if (r12 != 0) goto L_0x003b
        L_0x0033:
            if (r10 == 0) goto L_0x003d
            boolean r12 = isColorType(r11)
            if (r12 == 0) goto L_0x003d
        L_0x003b:
            r12 = 3
            goto L_0x0040
        L_0x003d:
            r12 = 0
            goto L_0x0040
        L_0x003f:
            r12 = r13
        L_0x0040:
            if (r12 != 0) goto L_0x0044
            r13 = 1
            goto L_0x0045
        L_0x0044:
            r13 = 0
        L_0x0045:
            r14 = 0
            r15 = 2
            if (r12 != r15) goto L_0x00f3
            java.lang.String r5 = r0.getString(r1)
            java.lang.String r6 = r0.getString(r2)
            androidx.core.graphics.PathParser$PathDataNode[] r15 = androidx.core.graphics.PathParser.createNodesFromPathData(r5)
            r18 = r4
            androidx.core.graphics.PathParser$PathDataNode[] r4 = androidx.core.graphics.PathParser.createNodesFromPathData(r6)
            if (r15 != 0) goto L_0x006a
            if (r4 == 0) goto L_0x0062
            goto L_0x006a
        L_0x0062:
            r19 = r9
            r20 = r11
            r21 = r14
            goto L_0x00eb
        L_0x006a:
            if (r15 == 0) goto L_0x00d3
            androidx.vectordrawable.graphics.drawable.AnimatorInflaterCompat$PathDataEvaluator r19 = new androidx.vectordrawable.graphics.drawable.AnimatorInflaterCompat$PathDataEvaluator
            r19.<init>()
            r20 = r19
            if (r4 == 0) goto L_0x00bf
            boolean r19 = androidx.core.graphics.PathParser.canMorph(r15, r4)
            if (r19 == 0) goto L_0x0094
            r19 = r9
            r9 = 2
            java.lang.Object[] r9 = new java.lang.Object[r9]
            r17 = 0
            r9[r17] = r15
            r16 = 1
            r9[r16] = r4
            r21 = r14
            r14 = r20
            android.animation.PropertyValuesHolder r9 = android.animation.PropertyValuesHolder.ofObject(r3, r14, r9)
            r14 = r9
            r20 = r11
            goto L_0x00d2
        L_0x0094:
            r19 = r9
            r21 = r14
            r14 = r20
            android.view.InflateException r9 = new android.view.InflateException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r20 = r11
            java.lang.String r11 = " Can't morph from "
            java.lang.StringBuilder r2 = r2.append(r11)
            java.lang.StringBuilder r2 = r2.append(r5)
            java.lang.String r11 = " to "
            java.lang.StringBuilder r2 = r2.append(r11)
            java.lang.StringBuilder r2 = r2.append(r6)
            java.lang.String r2 = r2.toString()
            r9.<init>(r2)
            throw r9
        L_0x00bf:
            r19 = r9
            r21 = r14
            r14 = r20
            r20 = r11
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r9 = 0
            r2[r9] = r15
            android.animation.PropertyValuesHolder r2 = android.animation.PropertyValuesHolder.ofObject(r3, r14, r2)
            r14 = r2
        L_0x00d2:
            goto L_0x00ed
        L_0x00d3:
            r19 = r9
            r20 = r11
            r21 = r14
            if (r4 == 0) goto L_0x00eb
            androidx.vectordrawable.graphics.drawable.AnimatorInflaterCompat$PathDataEvaluator r2 = new androidx.vectordrawable.graphics.drawable.AnimatorInflaterCompat$PathDataEvaluator
            r2.<init>()
            r9 = 1
            java.lang.Object[] r9 = new java.lang.Object[r9]
            r11 = 0
            r9[r11] = r4
            android.animation.PropertyValuesHolder r14 = android.animation.PropertyValuesHolder.ofObject(r3, r2, r9)
            goto L_0x00ed
        L_0x00eb:
            r14 = r21
        L_0x00ed:
            r11 = r25
            r9 = r20
            goto L_0x01e8
        L_0x00f3:
            r18 = r4
            r19 = r9
            r20 = r11
            r21 = r14
            r2 = 0
            r4 = 3
            if (r12 != r4) goto L_0x0103
            androidx.vectordrawable.graphics.drawable.ArgbEvaluator r2 = androidx.vectordrawable.graphics.drawable.ArgbEvaluator.getInstance()
        L_0x0103:
            r4 = 5
            r5 = 0
            if (r13 == 0) goto L_0x0162
            if (r7 == 0) goto L_0x0146
            if (r8 != r4) goto L_0x0110
            float r6 = r0.getDimension(r1, r5)
            goto L_0x0114
        L_0x0110:
            float r6 = r0.getFloat(r1, r5)
        L_0x0114:
            if (r10 == 0) goto L_0x0136
            r9 = r20
            if (r9 != r4) goto L_0x0121
            r11 = r25
            float r4 = r0.getDimension(r11, r5)
            goto L_0x0127
        L_0x0121:
            r11 = r25
            float r4 = r0.getFloat(r11, r5)
        L_0x0127:
            r5 = 2
            float[] r5 = new float[r5]
            r14 = 0
            r5[r14] = r6
            r15 = 1
            r5[r15] = r4
            android.animation.PropertyValuesHolder r5 = android.animation.PropertyValuesHolder.ofFloat(r3, r5)
            r14 = r5
            goto L_0x0160
        L_0x0136:
            r11 = r25
            r9 = r20
            r14 = 0
            r15 = 1
            float[] r4 = new float[r15]
            r4[r14] = r6
            android.animation.PropertyValuesHolder r4 = android.animation.PropertyValuesHolder.ofFloat(r3, r4)
            r14 = r4
            goto L_0x0160
        L_0x0146:
            r11 = r25
            r9 = r20
            if (r9 != r4) goto L_0x0151
            float r4 = r0.getDimension(r11, r5)
            goto L_0x0155
        L_0x0151:
            float r4 = r0.getFloat(r11, r5)
        L_0x0155:
            r5 = 1
            float[] r5 = new float[r5]
            r6 = 0
            r5[r6] = r4
            android.animation.PropertyValuesHolder r5 = android.animation.PropertyValuesHolder.ofFloat(r3, r5)
            r14 = r5
        L_0x0160:
            goto L_0x01e1
        L_0x0162:
            r11 = r25
            r9 = r20
            if (r7 == 0) goto L_0x01b8
            if (r8 != r4) goto L_0x0170
            float r6 = r0.getDimension(r1, r5)
            int r6 = (int) r6
            goto L_0x0183
        L_0x0170:
            boolean r6 = isColorType(r8)
            if (r6 == 0) goto L_0x017d
            r6 = 0
            int r14 = r0.getColor(r1, r6)
            r6 = r14
            goto L_0x0183
        L_0x017d:
            r6 = 0
            int r14 = r0.getInt(r1, r6)
            r6 = r14
        L_0x0183:
            if (r10 == 0) goto L_0x01ad
            if (r9 != r4) goto L_0x018f
            float r4 = r0.getDimension(r11, r5)
            int r4 = (int) r4
            r5 = r4
            r4 = 0
            goto L_0x01a0
        L_0x018f:
            boolean r4 = isColorType(r9)
            if (r4 == 0) goto L_0x019b
            r4 = 0
            int r5 = r0.getColor(r11, r4)
            goto L_0x01a0
        L_0x019b:
            r4 = 0
            int r5 = r0.getInt(r11, r4)
        L_0x01a0:
            r14 = 2
            int[] r14 = new int[r14]
            r14[r4] = r6
            r15 = 1
            r14[r15] = r5
            android.animation.PropertyValuesHolder r14 = android.animation.PropertyValuesHolder.ofInt(r3, r14)
            goto L_0x01e1
        L_0x01ad:
            r4 = 0
            r15 = 1
            int[] r5 = new int[r15]
            r5[r4] = r6
            android.animation.PropertyValuesHolder r14 = android.animation.PropertyValuesHolder.ofInt(r3, r5)
            goto L_0x01e1
        L_0x01b8:
            if (r10 == 0) goto L_0x01df
            if (r9 != r4) goto L_0x01c4
            float r4 = r0.getDimension(r11, r5)
            int r4 = (int) r4
            r5 = r4
            r4 = 0
            goto L_0x01d5
        L_0x01c4:
            boolean r4 = isColorType(r9)
            if (r4 == 0) goto L_0x01d0
            r4 = 0
            int r5 = r0.getColor(r11, r4)
            goto L_0x01d5
        L_0x01d0:
            r4 = 0
            int r5 = r0.getInt(r11, r4)
        L_0x01d5:
            r6 = 1
            int[] r6 = new int[r6]
            r6[r4] = r5
            android.animation.PropertyValuesHolder r14 = android.animation.PropertyValuesHolder.ofInt(r3, r6)
            goto L_0x01e1
        L_0x01df:
            r14 = r21
        L_0x01e1:
            if (r14 == 0) goto L_0x01e8
            if (r2 == 0) goto L_0x01e8
            r14.setEvaluator(r2)
        L_0x01e8:
            return r14
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.vectordrawable.graphics.drawable.AnimatorInflaterCompat.getPVH(android.content.res.TypedArray, int, int, int, java.lang.String):android.animation.PropertyValuesHolder");
    }

    private static int inferValueTypeFromValues(TypedArray styledAttributes, int valueFromId, int valueToId) {
        TypedValue peekValue = styledAttributes.peekValue(valueFromId);
        boolean z = true;
        int i = 0;
        boolean z2 = peekValue != null;
        int i2 = z2 ? peekValue.type : 0;
        TypedValue peekValue2 = styledAttributes.peekValue(valueToId);
        if (peekValue2 == null) {
            z = false;
        }
        if (z) {
            i = peekValue2.type;
        }
        return ((!z2 || !isColorType(i2)) && (!z || !isColorType(i))) ? 0 : 3;
    }

    private static int inferValueTypeOfKeyframe(Resources res, Resources.Theme theme, AttributeSet attrs, XmlPullParser parser) {
        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_KEYFRAME);
        boolean z = false;
        TypedValue peekNamedValue = TypedArrayUtils.peekNamedValue(obtainAttributes, parser, "value", 0);
        if (peekNamedValue != null) {
            z = true;
        }
        int i = (!z || !isColorType(peekNamedValue.type)) ? 0 : 3;
        obtainAttributes.recycle();
        return i;
    }

    private static boolean isColorType(int type) {
        return type >= 28 && type <= 31;
    }

    public static Animator loadAnimator(Context context, int id) throws Resources.NotFoundException {
        return Build.VERSION.SDK_INT >= 24 ? AnimatorInflater.loadAnimator(context, id) : loadAnimator(context, context.getResources(), context.getTheme(), id);
    }

    public static Animator loadAnimator(Context context, Resources resources, Resources.Theme theme, int id) throws Resources.NotFoundException {
        return loadAnimator(context, resources, theme, id, 1.0f);
    }

    public static Animator loadAnimator(Context context, Resources resources, Resources.Theme theme, int id, float pathErrorScale) throws Resources.NotFoundException {
        XmlResourceParser xmlResourceParser = null;
        try {
            XmlResourceParser animation = resources.getAnimation(id);
            Animator createAnimatorFromXml = createAnimatorFromXml(context, resources, theme, animation, pathErrorScale);
            if (animation != null) {
                animation.close();
            }
            return createAnimatorFromXml;
        } catch (XmlPullParserException e) {
            StringBuilder append = new StringBuilder().append("Can't load animation resource ID #0x");
            String hexString = Integer.toHexString(id);
            Log1F380D.a((Object) hexString);
            Resources.NotFoundException notFoundException = new Resources.NotFoundException(append.append(hexString).toString());
            notFoundException.initCause(e);
            throw notFoundException;
        } catch (IOException e2) {
            StringBuilder append2 = new StringBuilder().append("Can't load animation resource ID #0x");
            String hexString2 = Integer.toHexString(id);
            Log1F380D.a((Object) hexString2);
            Resources.NotFoundException notFoundException2 = new Resources.NotFoundException(append2.append(hexString2).toString());
            notFoundException2.initCause(e2);
            throw notFoundException2;
        } catch (Throwable th) {
            if (xmlResourceParser != null) {
                xmlResourceParser.close();
            }
            throw th;
        }
    }

    private static ValueAnimator loadAnimator(Context context, Resources res, Resources.Theme theme, AttributeSet attrs, ValueAnimator anim, float pathErrorScale, XmlPullParser parser) throws Resources.NotFoundException {
        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_ANIMATOR);
        TypedArray obtainAttributes2 = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_PROPERTY_ANIMATOR);
        if (anim == null) {
            anim = new ValueAnimator();
        }
        parseAnimatorFromTypeArray(anim, obtainAttributes, obtainAttributes2, pathErrorScale, parser);
        int namedResourceId = TypedArrayUtils.getNamedResourceId(obtainAttributes, parser, "interpolator", 0, 0);
        if (namedResourceId > 0) {
            anim.setInterpolator(AnimationUtilsCompat.loadInterpolator(context, namedResourceId));
        }
        obtainAttributes.recycle();
        if (obtainAttributes2 != null) {
            obtainAttributes2.recycle();
        }
        return anim;
    }

    private static Keyframe loadKeyframe(Context context, Resources res, Resources.Theme theme, AttributeSet attrs, int valueType, XmlPullParser parser) throws XmlPullParserException, IOException {
        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_KEYFRAME);
        Keyframe keyframe = null;
        float namedFloat = TypedArrayUtils.getNamedFloat(obtainAttributes, parser, "fraction", 3, -1.0f);
        TypedValue peekNamedValue = TypedArrayUtils.peekNamedValue(obtainAttributes, parser, "value", 0);
        boolean z = peekNamedValue != null;
        if (valueType == 4) {
            valueType = (!z || !isColorType(peekNamedValue.type)) ? 0 : 3;
        }
        if (z) {
            switch (valueType) {
                case 0:
                    keyframe = Keyframe.ofFloat(namedFloat, TypedArrayUtils.getNamedFloat(obtainAttributes, parser, "value", 0, 0.0f));
                    break;
                case 1:
                case 3:
                    keyframe = Keyframe.ofInt(namedFloat, TypedArrayUtils.getNamedInt(obtainAttributes, parser, "value", 0, 0));
                    break;
            }
        } else {
            keyframe = valueType == 0 ? Keyframe.ofFloat(namedFloat) : Keyframe.ofInt(namedFloat);
        }
        int namedResourceId = TypedArrayUtils.getNamedResourceId(obtainAttributes, parser, "interpolator", 1, 0);
        if (namedResourceId > 0) {
            keyframe.setInterpolator(AnimationUtilsCompat.loadInterpolator(context, namedResourceId));
        }
        obtainAttributes.recycle();
        return keyframe;
    }

    private static ObjectAnimator loadObjectAnimator(Context context, Resources res, Resources.Theme theme, AttributeSet attrs, float pathErrorScale, XmlPullParser parser) throws Resources.NotFoundException {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        loadAnimator(context, res, theme, attrs, objectAnimator, pathErrorScale, parser);
        return objectAnimator;
    }

    private static PropertyValuesHolder loadPvh(Context context, Resources res, Resources.Theme theme, XmlPullParser parser, String propertyName, int valueType) throws XmlPullParserException, IOException {
        int i;
        PropertyValuesHolder propertyValuesHolder;
        Object obj;
        int i2;
        float f;
        ArrayList arrayList;
        Object obj2 = null;
        ArrayList arrayList2 = null;
        int i3 = valueType;
        while (true) {
            int next = parser.next();
            i = next;
            if (next == 3 || i == 1) {
                Resources resources = res;
                Resources.Theme theme2 = theme;
                XmlPullParser xmlPullParser = parser;
            } else if (parser.getName().equals("keyframe")) {
                if (i3 == 4) {
                    i3 = inferValueTypeOfKeyframe(res, theme, Xml.asAttributeSet(parser), parser);
                } else {
                    Resources resources2 = res;
                    Resources.Theme theme3 = theme;
                    XmlPullParser xmlPullParser2 = parser;
                }
                Keyframe loadKeyframe = loadKeyframe(context, res, theme, Xml.asAttributeSet(parser), i3, parser);
                if (loadKeyframe != null) {
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList();
                    }
                    arrayList2.add(loadKeyframe);
                }
                parser.next();
            } else {
                Resources resources3 = res;
                Resources.Theme theme4 = theme;
                XmlPullParser xmlPullParser3 = parser;
            }
        }
        Resources resources4 = res;
        Resources.Theme theme22 = theme;
        XmlPullParser xmlPullParser4 = parser;
        if (arrayList2 != null) {
            int size = arrayList2.size();
            int i4 = size;
            if (size > 0) {
                Keyframe keyframe = (Keyframe) arrayList2.get(0);
                Keyframe keyframe2 = (Keyframe) arrayList2.get(i4 - 1);
                float fraction = keyframe2.getFraction();
                float f2 = 0.0f;
                if (fraction < 1.0f) {
                    if (fraction < 0.0f) {
                        keyframe2.setFraction(1.0f);
                    } else {
                        arrayList2.add(arrayList2.size(), createNewKeyframe(keyframe2, 1.0f));
                        i4++;
                    }
                }
                float fraction2 = keyframe.getFraction();
                if (fraction2 != 0.0f) {
                    if (fraction2 < 0.0f) {
                        keyframe.setFraction(0.0f);
                    } else {
                        arrayList2.add(0, createNewKeyframe(keyframe, 0.0f));
                        i4++;
                    }
                }
                Keyframe[] keyframeArr = new Keyframe[i4];
                arrayList2.toArray(keyframeArr);
                int i5 = 0;
                while (i5 < i4) {
                    Keyframe keyframe3 = keyframeArr[i5];
                    if (keyframe3.getFraction() >= f2) {
                        obj = obj2;
                        arrayList = arrayList2;
                        i2 = i;
                        f = f2;
                    } else if (i5 == 0) {
                        keyframe3.setFraction(f2);
                        obj = obj2;
                        arrayList = arrayList2;
                        i2 = i;
                        f = f2;
                    } else if (i5 == i4 - 1) {
                        keyframe3.setFraction(1.0f);
                        obj = obj2;
                        arrayList = arrayList2;
                        i2 = i;
                        f = 0.0f;
                    } else {
                        int i6 = i5;
                        obj = obj2;
                        int i7 = i6 + 1;
                        arrayList = arrayList2;
                        int i8 = i5;
                        while (true) {
                            i2 = i;
                            if (i7 >= i4 - 1) {
                                f = 0.0f;
                                break;
                            }
                            f = 0.0f;
                            if (keyframeArr[i7].getFraction() >= 0.0f) {
                                break;
                            }
                            i8 = i7;
                            i7++;
                            i = i2;
                        }
                        distributeKeyframes(keyframeArr, keyframeArr[i8 + 1].getFraction() - keyframeArr[i6 - 1].getFraction(), i6, i8);
                    }
                    i5++;
                    arrayList2 = arrayList;
                    f2 = f;
                    i = i2;
                    obj2 = obj;
                }
                Object obj3 = obj2;
                ArrayList arrayList3 = arrayList2;
                int i9 = i;
                PropertyValuesHolder ofKeyframe = PropertyValuesHolder.ofKeyframe(propertyName, keyframeArr);
                if (i3 != 3) {
                    return ofKeyframe;
                }
                ofKeyframe.setEvaluator(ArgbEvaluator.getInstance());
                return ofKeyframe;
            }
            propertyValuesHolder = null;
            ArrayList arrayList4 = arrayList2;
            int i10 = i;
            String str = propertyName;
        } else {
            propertyValuesHolder = null;
            ArrayList arrayList5 = arrayList2;
            int i11 = i;
            String str2 = propertyName;
        }
        return propertyValuesHolder;
    }

    private static void parseAnimatorFromTypeArray(ValueAnimator anim, TypedArray arrayAnimator, TypedArray arrayObjectAnimator, float pixelSize, XmlPullParser parser) {
        long namedInt = (long) TypedArrayUtils.getNamedInt(arrayAnimator, parser, TypedValues.TransitionType.S_DURATION, 1, 300);
        long namedInt2 = (long) TypedArrayUtils.getNamedInt(arrayAnimator, parser, "startOffset", 2, 0);
        int namedInt3 = TypedArrayUtils.getNamedInt(arrayAnimator, parser, "valueType", 7, 4);
        if (TypedArrayUtils.hasAttribute(parser, "valueFrom") && TypedArrayUtils.hasAttribute(parser, "valueTo")) {
            if (namedInt3 == 4) {
                namedInt3 = inferValueTypeFromValues(arrayAnimator, 5, 6);
            }
            PropertyValuesHolder pvh = getPVH(arrayAnimator, namedInt3, 5, 6, HttpUrl.FRAGMENT_ENCODE_SET);
            if (pvh != null) {
                anim.setValues(new PropertyValuesHolder[]{pvh});
            }
        }
        anim.setDuration(namedInt);
        anim.setStartDelay(namedInt2);
        anim.setRepeatCount(TypedArrayUtils.getNamedInt(arrayAnimator, parser, "repeatCount", 3, 0));
        anim.setRepeatMode(TypedArrayUtils.getNamedInt(arrayAnimator, parser, "repeatMode", 4, 1));
        if (arrayObjectAnimator != null) {
            setupObjectAnimator(anim, arrayObjectAnimator, namedInt3, pixelSize, parser);
        }
    }

    private static void setupPathMotion(Path path, ObjectAnimator oa, float precision, String propertyXName, String propertyYName) {
        Path path2 = path;
        ObjectAnimator objectAnimator = oa;
        String str = propertyXName;
        String str2 = propertyYName;
        PathMeasure pathMeasure = new PathMeasure(path2, false);
        float f = 0.0f;
        ArrayList arrayList = new ArrayList();
        arrayList.add(Float.valueOf(0.0f));
        while (true) {
            f += pathMeasure.getLength();
            arrayList.add(Float.valueOf(f));
            if (!pathMeasure.nextContour()) {
                break;
            }
            path2 = path;
        }
        PathMeasure pathMeasure2 = new PathMeasure(path2, false);
        int min = Math.min(100, ((int) (f / precision)) + 1);
        float[] fArr = new float[min];
        float[] fArr2 = new float[min];
        float[] fArr3 = new float[2];
        int i = 0;
        float f2 = f / ((float) (min - 1));
        float f3 = 0.0f;
        int i2 = 0;
        while (i2 < min) {
            pathMeasure2.getPosTan(f3 - ((Float) arrayList.get(i)).floatValue(), fArr3, (float[]) null);
            fArr[i2] = fArr3[0];
            fArr2[i2] = fArr3[1];
            f3 += f2;
            if (i + 1 < arrayList.size() && f3 > ((Float) arrayList.get(i + 1)).floatValue()) {
                i++;
                pathMeasure2.nextContour();
            }
            i2++;
            Path path3 = path;
        }
        PropertyValuesHolder propertyValuesHolder = null;
        PropertyValuesHolder propertyValuesHolder2 = null;
        if (str != null) {
            propertyValuesHolder = PropertyValuesHolder.ofFloat(str, fArr);
        }
        if (str2 != null) {
            propertyValuesHolder2 = PropertyValuesHolder.ofFloat(str2, fArr2);
        }
        if (propertyValuesHolder == null) {
            objectAnimator.setValues(new PropertyValuesHolder[]{propertyValuesHolder2});
        } else if (propertyValuesHolder2 == null) {
            objectAnimator.setValues(new PropertyValuesHolder[]{propertyValuesHolder});
        } else {
            objectAnimator.setValues(new PropertyValuesHolder[]{propertyValuesHolder, propertyValuesHolder2});
        }
    }

    private static PropertyValuesHolder[] loadValues(Context context, Resources res, Resources.Theme theme, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser = parser;
        ArrayList arrayList = null;
        while (true) {
            int eventType = parser.getEventType();
            int i = eventType;
            if (eventType == 3 || i == 1) {
                Resources resources = res;
                Resources.Theme theme2 = theme;
                AttributeSet attributeSet = attrs;
                PropertyValuesHolder[] propertyValuesHolderArr = null;
            } else if (i != 2) {
                parser.next();
            } else {
                if (parser.getName().equals("propertyValuesHolder")) {
                    TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_PROPERTY_VALUES_HOLDER);
                    String namedString = TypedArrayUtils.getNamedString(obtainAttributes, xmlPullParser, "propertyName", 3);
                    Log1F380D.a((Object) namedString);
                    int namedInt = TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser, "valueType", 2, 4);
                    int i2 = namedInt;
                    PropertyValuesHolder loadPvh = loadPvh(context, res, theme, parser, namedString, namedInt);
                    if (loadPvh == null) {
                        loadPvh = getPVH(obtainAttributes, i2, 0, 1, namedString);
                    } else {
                        int i3 = i2;
                    }
                    if (loadPvh != null) {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(loadPvh);
                    }
                    obtainAttributes.recycle();
                } else {
                    Resources resources2 = res;
                    Resources.Theme theme3 = theme;
                    AttributeSet attributeSet2 = attrs;
                }
                parser.next();
            }
        }
        Resources resources3 = res;
        Resources.Theme theme22 = theme;
        AttributeSet attributeSet3 = attrs;
        PropertyValuesHolder[] propertyValuesHolderArr2 = null;
        if (arrayList != null) {
            int size = arrayList.size();
            propertyValuesHolderArr2 = new PropertyValuesHolder[size];
            for (int i4 = 0; i4 < size; i4++) {
                propertyValuesHolderArr2[i4] = (PropertyValuesHolder) arrayList.get(i4);
            }
        }
        return propertyValuesHolderArr2;
    }

    private static void setupObjectAnimator(ValueAnimator anim, TypedArray arrayObjectAnimator, int valueType, float pixelSize, XmlPullParser parser) {
        ObjectAnimator objectAnimator = (ObjectAnimator) anim;
        String namedString = TypedArrayUtils.getNamedString(arrayObjectAnimator, parser, "pathData", 1);
        Log1F380D.a((Object) namedString);
        if (namedString != null) {
            String namedString2 = TypedArrayUtils.getNamedString(arrayObjectAnimator, parser, "propertyXName", 2);
            Log1F380D.a((Object) namedString2);
            String namedString3 = TypedArrayUtils.getNamedString(arrayObjectAnimator, parser, "propertyYName", 3);
            Log1F380D.a((Object) namedString3);
            if (valueType == 2 || valueType == 4) {
            }
            if (namedString2 == null && namedString3 == null) {
                throw new InflateException(arrayObjectAnimator.getPositionDescription() + " propertyXName or propertyYName is needed for PathData");
            }
            setupPathMotion(PathParser.createPathFromPathData(namedString), objectAnimator, 0.5f * pixelSize, namedString2, namedString3);
            return;
        }
        String namedString4 = TypedArrayUtils.getNamedString(arrayObjectAnimator, parser, "propertyName", 0);
        Log1F380D.a((Object) namedString4);
        objectAnimator.setPropertyName(namedString4);
    }
}

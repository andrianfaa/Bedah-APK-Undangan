package androidx.transition;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.content.res.TypedArrayUtils;
import java.io.IOException;
import java.lang.reflect.Constructor;
import mt.Log1F380D;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: 0095 */
public class TransitionInflater {
    private static final ArrayMap<String, Constructor<?>> CONSTRUCTORS = new ArrayMap<>();
    private static final Class<?>[] CONSTRUCTOR_SIGNATURE = {Context.class, AttributeSet.class};
    private final Context mContext;

    private TransitionInflater(Context context) {
        this.mContext = context;
    }

    private Object createCustom(AttributeSet attrs, Class<?> cls, String tag) {
        Object newInstance;
        Class<? extends U> asSubclass;
        String attributeValue = attrs.getAttributeValue((String) null, "class");
        if (attributeValue != null) {
            try {
                ArrayMap<String, Constructor<?>> arrayMap = CONSTRUCTORS;
                synchronized (arrayMap) {
                    Constructor<? extends U> constructor = arrayMap.get(attributeValue);
                    if (constructor == null && (asSubclass = Class.forName(attributeValue, false, this.mContext.getClassLoader()).asSubclass(cls)) != null) {
                        constructor = asSubclass.getConstructor(CONSTRUCTOR_SIGNATURE);
                        constructor.setAccessible(true);
                        arrayMap.put(attributeValue, constructor);
                    }
                    newInstance = constructor.newInstance(new Object[]{this.mContext, attrs});
                }
                return newInstance;
            } catch (Exception e) {
                throw new InflateException("Could not instantiate " + cls + " class " + attributeValue, e);
            }
        } else {
            throw new InflateException(tag + " tag must have a 'class' attribute");
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v39, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: androidx.transition.Transition} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private androidx.transition.Transition createTransitionFromXml(org.xmlpull.v1.XmlPullParser r9, android.util.AttributeSet r10, androidx.transition.Transition r11) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            r8 = this;
            r0 = 0
            int r1 = r9.getDepth()
            boolean r2 = r11 instanceof androidx.transition.TransitionSet
            if (r2 == 0) goto L_0x000d
            r2 = r11
            androidx.transition.TransitionSet r2 = (androidx.transition.TransitionSet) r2
            goto L_0x000e
        L_0x000d:
            r2 = 0
        L_0x000e:
            int r3 = r9.next()
            r4 = r3
            r5 = 3
            if (r3 != r5) goto L_0x001c
            int r3 = r9.getDepth()
            if (r3 <= r1) goto L_0x0190
        L_0x001c:
            r3 = 1
            if (r4 == r3) goto L_0x0190
            r3 = 2
            if (r4 == r3) goto L_0x0023
            goto L_0x000e
        L_0x0023:
            java.lang.String r3 = r9.getName()
            java.lang.String r5 = "fade"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x0039
            androidx.transition.Fade r5 = new androidx.transition.Fade
            android.content.Context r6 = r8.mContext
            r5.<init>(r6, r10)
            r0 = r5
            goto L_0x014c
        L_0x0039:
            java.lang.String r5 = "changeBounds"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x004b
            androidx.transition.ChangeBounds r5 = new androidx.transition.ChangeBounds
            android.content.Context r6 = r8.mContext
            r5.<init>(r6, r10)
            r0 = r5
            goto L_0x014c
        L_0x004b:
            java.lang.String r5 = "slide"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x005e
            androidx.transition.Slide r5 = new androidx.transition.Slide
            android.content.Context r6 = r8.mContext
            r5.<init>(r6, r10)
            r0 = r5
            goto L_0x014c
        L_0x005e:
            java.lang.String r5 = "explode"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x0070
            androidx.transition.Explode r5 = new androidx.transition.Explode
            android.content.Context r6 = r8.mContext
            r5.<init>(r6, r10)
            r0 = r5
            goto L_0x014c
        L_0x0070:
            java.lang.String r5 = "changeImageTransform"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x0082
            androidx.transition.ChangeImageTransform r5 = new androidx.transition.ChangeImageTransform
            android.content.Context r6 = r8.mContext
            r5.<init>(r6, r10)
            r0 = r5
            goto L_0x014c
        L_0x0082:
            java.lang.String r5 = "changeTransform"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x0094
            androidx.transition.ChangeTransform r5 = new androidx.transition.ChangeTransform
            android.content.Context r6 = r8.mContext
            r5.<init>(r6, r10)
            r0 = r5
            goto L_0x014c
        L_0x0094:
            java.lang.String r5 = "changeClipBounds"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x00a6
            androidx.transition.ChangeClipBounds r5 = new androidx.transition.ChangeClipBounds
            android.content.Context r6 = r8.mContext
            r5.<init>(r6, r10)
            r0 = r5
            goto L_0x014c
        L_0x00a6:
            java.lang.String r5 = "autoTransition"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x00b8
            androidx.transition.AutoTransition r5 = new androidx.transition.AutoTransition
            android.content.Context r6 = r8.mContext
            r5.<init>(r6, r10)
            r0 = r5
            goto L_0x014c
        L_0x00b8:
            java.lang.String r5 = "changeScroll"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x00ca
            androidx.transition.ChangeScroll r5 = new androidx.transition.ChangeScroll
            android.content.Context r6 = r8.mContext
            r5.<init>(r6, r10)
            r0 = r5
            goto L_0x014c
        L_0x00ca:
            java.lang.String r5 = "transitionSet"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x00dd
            androidx.transition.TransitionSet r5 = new androidx.transition.TransitionSet
            android.content.Context r6 = r8.mContext
            r5.<init>(r6, r10)
            r0 = r5
            goto L_0x014c
        L_0x00dd:
            java.lang.String r5 = "transition"
            boolean r6 = r5.equals(r3)
            if (r6 == 0) goto L_0x00f0
            java.lang.Class<androidx.transition.Transition> r6 = androidx.transition.Transition.class
            java.lang.Object r5 = r8.createCustom(r10, r6, r5)
            r0 = r5
            androidx.transition.Transition r0 = (androidx.transition.Transition) r0
            goto L_0x014c
        L_0x00f0:
            java.lang.String r5 = "targets"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x00fd
            r8.getTargetIds(r9, r10, r11)
            goto L_0x014c
        L_0x00fd:
            java.lang.String r5 = "arcMotion"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x011a
            if (r11 == 0) goto L_0x0112
            androidx.transition.ArcMotion r5 = new androidx.transition.ArcMotion
            android.content.Context r6 = r8.mContext
            r5.<init>(r6, r10)
            r11.setPathMotion(r5)
            goto L_0x014c
        L_0x0112:
            java.lang.RuntimeException r5 = new java.lang.RuntimeException
            java.lang.String r6 = "Invalid use of arcMotion element"
            r5.<init>(r6)
            throw r5
        L_0x011a:
            java.lang.String r5 = "pathMotion"
            boolean r6 = r5.equals(r3)
            if (r6 == 0) goto L_0x0138
            if (r11 == 0) goto L_0x0130
            java.lang.Class<androidx.transition.PathMotion> r6 = androidx.transition.PathMotion.class
            java.lang.Object r5 = r8.createCustom(r10, r6, r5)
            androidx.transition.PathMotion r5 = (androidx.transition.PathMotion) r5
            r11.setPathMotion(r5)
            goto L_0x014c
        L_0x0130:
            java.lang.RuntimeException r5 = new java.lang.RuntimeException
            java.lang.String r6 = "Invalid use of pathMotion element"
            r5.<init>(r6)
            throw r5
        L_0x0138:
            java.lang.String r5 = "patternPathMotion"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x0173
            if (r11 == 0) goto L_0x016b
            androidx.transition.PatternPathMotion r5 = new androidx.transition.PatternPathMotion
            android.content.Context r6 = r8.mContext
            r5.<init>(r6, r10)
            r11.setPathMotion(r5)
        L_0x014c:
            if (r0 == 0) goto L_0x0169
            boolean r5 = r9.isEmptyElementTag()
            if (r5 != 0) goto L_0x0157
            r8.createTransitionFromXml(r9, r10, r0)
        L_0x0157:
            if (r2 == 0) goto L_0x015e
            r2.addTransition(r0)
            r0 = 0
            goto L_0x0169
        L_0x015e:
            if (r11 != 0) goto L_0x0161
            goto L_0x0169
        L_0x0161:
            android.view.InflateException r5 = new android.view.InflateException
            java.lang.String r6 = "Could not add transition to another transition."
            r5.<init>(r6)
            throw r5
        L_0x0169:
            goto L_0x000e
        L_0x016b:
            java.lang.RuntimeException r5 = new java.lang.RuntimeException
            java.lang.String r6 = "Invalid use of patternPathMotion element"
            r5.<init>(r6)
            throw r5
        L_0x0173:
            java.lang.RuntimeException r5 = new java.lang.RuntimeException
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Unknown scene name: "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r7 = r9.getName()
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            r5.<init>(r6)
            throw r5
        L_0x0190:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.transition.TransitionInflater.createTransitionFromXml(org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, androidx.transition.Transition):androidx.transition.Transition");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005a, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private androidx.transition.TransitionManager createTransitionManagerFromXml(org.xmlpull.v1.XmlPullParser r8, android.util.AttributeSet r9, android.view.ViewGroup r10) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            r7 = this;
            int r0 = r8.getDepth()
            r1 = 0
        L_0x0005:
            int r2 = r8.next()
            r3 = r2
            r4 = 3
            if (r2 != r4) goto L_0x0013
            int r2 = r8.getDepth()
            if (r2 <= r0) goto L_0x005a
        L_0x0013:
            r2 = 1
            if (r3 == r2) goto L_0x005a
            r2 = 2
            if (r3 == r2) goto L_0x001a
            goto L_0x0005
        L_0x001a:
            java.lang.String r2 = r8.getName()
            java.lang.String r4 = "transitionManager"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L_0x002e
            androidx.transition.TransitionManager r4 = new androidx.transition.TransitionManager
            r4.<init>()
            r1 = r4
            goto L_0x003c
        L_0x002e:
            java.lang.String r4 = "transition"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L_0x003d
            if (r1 == 0) goto L_0x003d
            r7.loadTransition(r9, r8, r10, r1)
        L_0x003c:
            goto L_0x0005
        L_0x003d:
            java.lang.RuntimeException r4 = new java.lang.RuntimeException
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Unknown scene name: "
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = r8.getName()
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r5 = r5.toString()
            r4.<init>(r5)
            throw r4
        L_0x005a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.transition.TransitionInflater.createTransitionManagerFromXml(org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.view.ViewGroup):androidx.transition.TransitionManager");
    }

    public static TransitionInflater from(Context context) {
        return new TransitionInflater(context);
    }

    private void getTargetIds(XmlPullParser parser, AttributeSet attrs, Transition transition) throws XmlPullParserException, IOException {
        int depth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int i = next;
            if ((next == 3 && parser.getDepth() <= depth) || i == 1) {
                return;
            }
            if (i == 2) {
                if (parser.getName().equals(TypedValues.AttributesType.S_TARGET)) {
                    TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(attrs, Styleable.TRANSITION_TARGET);
                    int namedResourceId = TypedArrayUtils.getNamedResourceId(obtainStyledAttributes, parser, "targetId", 1, 0);
                    if (namedResourceId != 0) {
                        transition.addTarget(namedResourceId);
                    } else {
                        int namedResourceId2 = TypedArrayUtils.getNamedResourceId(obtainStyledAttributes, parser, "excludeId", 2, 0);
                        int i2 = namedResourceId2;
                        if (namedResourceId2 != 0) {
                            transition.excludeTarget(i2, true);
                        } else {
                            String namedString = TypedArrayUtils.getNamedString(obtainStyledAttributes, parser, "targetName", 4);
                            Log1F380D.a((Object) namedString);
                            String str = namedString;
                            if (namedString != null) {
                                transition.addTarget(str);
                            } else {
                                String namedString2 = TypedArrayUtils.getNamedString(obtainStyledAttributes, parser, "excludeName", 5);
                                Log1F380D.a((Object) namedString2);
                                String str2 = namedString2;
                                if (namedString2 != null) {
                                    transition.excludeTarget(str2, true);
                                } else {
                                    String namedString3 = TypedArrayUtils.getNamedString(obtainStyledAttributes, parser, "excludeClass", 3);
                                    Log1F380D.a((Object) namedString3);
                                    if (namedString3 != null) {
                                        try {
                                            transition.excludeTarget(Class.forName(namedString3), true);
                                        } catch (ClassNotFoundException e) {
                                            obtainStyledAttributes.recycle();
                                            throw new RuntimeException("Could not create " + namedString3, e);
                                        }
                                    } else {
                                        String namedString4 = TypedArrayUtils.getNamedString(obtainStyledAttributes, parser, "targetClass", 0);
                                        Log1F380D.a((Object) namedString4);
                                        namedString3 = namedString4;
                                        if (namedString4 != null) {
                                            transition.addTarget(Class.forName(namedString3));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    obtainStyledAttributes.recycle();
                } else {
                    throw new RuntimeException("Unknown scene name: " + parser.getName());
                }
            }
        }
    }

    private void loadTransition(AttributeSet attrs, XmlPullParser parser, ViewGroup sceneRoot, TransitionManager transitionManager) throws Resources.NotFoundException {
        Transition inflateTransition;
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(attrs, Styleable.TRANSITION_MANAGER);
        int namedResourceId = TypedArrayUtils.getNamedResourceId(obtainStyledAttributes, parser, "transition", 2, -1);
        int namedResourceId2 = TypedArrayUtils.getNamedResourceId(obtainStyledAttributes, parser, "fromScene", 0, -1);
        Scene scene = null;
        Scene sceneForLayout = namedResourceId2 < 0 ? null : Scene.getSceneForLayout(sceneRoot, namedResourceId2, this.mContext);
        int namedResourceId3 = TypedArrayUtils.getNamedResourceId(obtainStyledAttributes, parser, "toScene", 1, -1);
        if (namedResourceId3 >= 0) {
            scene = Scene.getSceneForLayout(sceneRoot, namedResourceId3, this.mContext);
        }
        if (namedResourceId >= 0 && (inflateTransition = inflateTransition(namedResourceId)) != null) {
            if (scene == null) {
                throw new RuntimeException("No toScene for transition ID " + namedResourceId);
            } else if (sceneForLayout == null) {
                transitionManager.setTransition(scene, inflateTransition);
            } else {
                transitionManager.setTransition(sceneForLayout, scene, inflateTransition);
            }
        }
        obtainStyledAttributes.recycle();
    }

    public Transition inflateTransition(int resource) {
        XmlResourceParser xml = this.mContext.getResources().getXml(resource);
        try {
            Transition createTransitionFromXml = createTransitionFromXml(xml, Xml.asAttributeSet(xml), (Transition) null);
            xml.close();
            return createTransitionFromXml;
        } catch (XmlPullParserException e) {
            throw new InflateException(e.getMessage(), e);
        } catch (IOException e2) {
            throw new InflateException(xml.getPositionDescription() + ": " + e2.getMessage(), e2);
        } catch (Throwable th) {
            xml.close();
            throw th;
        }
    }

    public TransitionManager inflateTransitionManager(int resource, ViewGroup sceneRoot) {
        XmlResourceParser xml = this.mContext.getResources().getXml(resource);
        try {
            TransitionManager createTransitionManagerFromXml = createTransitionManagerFromXml(xml, Xml.asAttributeSet(xml), sceneRoot);
            xml.close();
            return createTransitionManagerFromXml;
        } catch (XmlPullParserException e) {
            InflateException inflateException = new InflateException(e.getMessage());
            inflateException.initCause(e);
            throw inflateException;
        } catch (IOException e2) {
            InflateException inflateException2 = new InflateException(xml.getPositionDescription() + ": " + e2.getMessage());
            inflateException2.initCause(e2);
            throw inflateException2;
        } catch (Throwable th) {
            xml.close();
            throw th;
        }
    }
}

package androidx.constraintlayout.core.state;

import androidx.constraintlayout.core.motion.CustomAttribute;
import androidx.constraintlayout.core.motion.CustomVariable;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.core.parser.CLElement;
import androidx.constraintlayout.core.parser.CLKey;
import androidx.constraintlayout.core.parser.CLNumber;
import androidx.constraintlayout.core.parser.CLObject;
import androidx.constraintlayout.core.parser.CLParsingException;
import androidx.constraintlayout.core.state.Transition;
import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.core.os.EnvironmentCompat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import mt.Log1F380D;

/* compiled from: 0016 */
public class WidgetFrame {
    private static final boolean OLD_SYSTEM = true;
    public static float phone_orientation = Float.NaN;
    public float alpha = Float.NaN;
    public int bottom = 0;
    public float interpolatedPos = Float.NaN;
    public int left = 0;
    public final HashMap<String, CustomVariable> mCustom = new HashMap<>();
    public String name = null;
    public float pivotX = Float.NaN;
    public float pivotY = Float.NaN;
    public int right = 0;
    public float rotationX = Float.NaN;
    public float rotationY = Float.NaN;
    public float rotationZ = Float.NaN;
    public float scaleX = Float.NaN;
    public float scaleY = Float.NaN;
    public int top = 0;
    public float translationX = Float.NaN;
    public float translationY = Float.NaN;
    public float translationZ = Float.NaN;
    public int visibility = 0;
    public ConstraintWidget widget = null;

    public WidgetFrame() {
    }

    public WidgetFrame(WidgetFrame frame) {
        this.widget = frame.widget;
        this.left = frame.left;
        this.top = frame.top;
        this.right = frame.right;
        this.bottom = frame.bottom;
        updateAttributes(frame);
    }

    public WidgetFrame(ConstraintWidget widget2) {
        this.widget = widget2;
    }

    private static void add(StringBuilder s, String title, float value) {
        if (!Float.isNaN(value)) {
            s.append(title);
            s.append(": ");
            s.append(value);
            s.append(",\n");
        }
    }

    private static void add(StringBuilder s, String title, int value) {
        s.append(title);
        s.append(": ");
        s.append(value);
        s.append(",\n");
    }

    private static float interpolate(float start, float end, float defaultValue, float progress) {
        boolean isNaN = Float.isNaN(start);
        boolean isNaN2 = Float.isNaN(end);
        if (isNaN && isNaN2) {
            return Float.NaN;
        }
        if (isNaN) {
            start = defaultValue;
        }
        if (isNaN2) {
            end = defaultValue;
        }
        return ((end - start) * progress) + start;
    }

    public static void interpolate(int parentWidth, int parentHeight, WidgetFrame frame, WidgetFrame start, WidgetFrame end, Transition transition, float progress) {
        int i;
        int i2;
        float f;
        int i3;
        int i4;
        int i5;
        int i6;
        float f2;
        Iterator<String> it;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11 = parentWidth;
        int i12 = parentHeight;
        WidgetFrame widgetFrame = frame;
        WidgetFrame widgetFrame2 = start;
        WidgetFrame widgetFrame3 = end;
        Transition transition2 = transition;
        int i13 = (int) (progress * 100.0f);
        int i14 = widgetFrame2.left;
        int i15 = widgetFrame2.top;
        int i16 = widgetFrame3.left;
        int i17 = widgetFrame3.top;
        int i18 = widgetFrame2.right - widgetFrame2.left;
        int i19 = widgetFrame2.bottom - widgetFrame2.top;
        int i20 = widgetFrame3.right - widgetFrame3.left;
        int i21 = i18;
        int i22 = widgetFrame3.bottom - widgetFrame3.top;
        float f3 = progress;
        float f4 = widgetFrame2.alpha;
        int i23 = i19;
        float f5 = widgetFrame3.alpha;
        if (widgetFrame2.visibility == 8) {
            i14 = (int) (((float) i14) - (((float) i20) / 2.0f));
            i15 = (int) (((float) i15) - (((float) i22) / 2.0f));
            int i24 = i20;
            i2 = i22;
            if (Float.isNaN(f4)) {
                i = i24;
                f = 0.0f;
            } else {
                float f6 = f4;
                i = i24;
                f = f6;
            }
        } else {
            f = f4;
            i = i21;
            i2 = i23;
        }
        int i25 = i22;
        int i26 = i14;
        if (widgetFrame3.visibility == 8) {
            i16 = (int) (((float) i16) - (((float) i) / 2.0f));
            i17 = (int) (((float) i17) - (((float) i2) / 2.0f));
            i20 = i;
            i3 = i2;
            if (Float.isNaN(f5)) {
                f5 = 0.0f;
            }
        } else {
            i3 = i25;
        }
        if (Float.isNaN(f) && !Float.isNaN(f5)) {
            f = 1.0f;
        }
        if (!Float.isNaN(f) && Float.isNaN(f5)) {
            f5 = 1.0f;
        }
        float f7 = widgetFrame2.visibility == 4 ? 0.0f : f;
        int i27 = i15;
        if (widgetFrame3.visibility == 4) {
            f5 = 0.0f;
        }
        if (widgetFrame.widget == null || !transition.hasPositionKeyframes()) {
            int i28 = parentHeight;
            f2 = progress;
            int i29 = i13;
            i5 = i27;
            i6 = i26;
            i4 = i16;
        } else {
            Transition.KeyPosition findPreviousPosition = transition2.findPreviousPosition(widgetFrame.widget.stringId, i13);
            Transition.KeyPosition findNextPosition = transition2.findNextPosition(widgetFrame.widget.stringId, i13);
            if (findPreviousPosition == findNextPosition) {
                findNextPosition = null;
            }
            int i30 = 100;
            if (findPreviousPosition != null) {
                int i31 = i13;
                i26 = (int) (findPreviousPosition.x * ((float) i11));
                i7 = i16;
                i9 = parentHeight;
                i8 = findPreviousPosition.frame;
                i27 = (int) (findPreviousPosition.y * ((float) i9));
            } else {
                i9 = parentHeight;
                int i32 = i13;
                i7 = i16;
                i8 = 0;
            }
            if (findNextPosition != null) {
                Transition.KeyPosition keyPosition = findPreviousPosition;
                i10 = (int) (findNextPosition.x * ((float) i11));
                i17 = (int) (findNextPosition.y * ((float) i9));
                i30 = findNextPosition.frame;
            } else {
                Transition.KeyPosition keyPosition2 = findPreviousPosition;
                i10 = i7;
            }
            f2 = progress;
            f3 = ((100.0f * f2) - ((float) i8)) / ((float) (i30 - i8));
            i4 = i10;
            i5 = i27;
            i6 = i26;
        }
        widgetFrame.widget = widgetFrame2.widget;
        int i33 = (int) (((float) i6) + (((float) (i4 - i6)) * f3));
        widgetFrame.left = i33;
        int i34 = i6;
        int i35 = (int) (((float) i5) + (((float) (i17 - i5)) * f3));
        widgetFrame.top = i35;
        int i36 = i5;
        widgetFrame.right = i33 + ((int) (((1.0f - f2) * ((float) i)) + (((float) i20) * f2)));
        widgetFrame.bottom = i35 + ((int) (((1.0f - f2) * ((float) i2)) + (((float) i3) * f2)));
        int i37 = i2;
        widgetFrame.pivotX = interpolate(widgetFrame2.pivotX, widgetFrame3.pivotX, 0.5f, f2);
        widgetFrame.pivotY = interpolate(widgetFrame2.pivotY, widgetFrame3.pivotY, 0.5f, f2);
        widgetFrame.rotationX = interpolate(widgetFrame2.rotationX, widgetFrame3.rotationX, 0.0f, f2);
        widgetFrame.rotationY = interpolate(widgetFrame2.rotationY, widgetFrame3.rotationY, 0.0f, f2);
        widgetFrame.rotationZ = interpolate(widgetFrame2.rotationZ, widgetFrame3.rotationZ, 0.0f, f2);
        widgetFrame.scaleX = interpolate(widgetFrame2.scaleX, widgetFrame3.scaleX, 1.0f, f2);
        widgetFrame.scaleY = interpolate(widgetFrame2.scaleY, widgetFrame3.scaleY, 1.0f, f2);
        widgetFrame.translationX = interpolate(widgetFrame2.translationX, widgetFrame3.translationX, 0.0f, f2);
        widgetFrame.translationY = interpolate(widgetFrame2.translationY, widgetFrame3.translationY, 0.0f, f2);
        widgetFrame.translationZ = interpolate(widgetFrame2.translationZ, widgetFrame3.translationZ, 0.0f, f2);
        widgetFrame.alpha = interpolate(f7, f5, 1.0f, f2);
        Set<String> keySet = widgetFrame3.mCustom.keySet();
        widgetFrame.mCustom.clear();
        Iterator<String> it2 = keySet.iterator();
        while (it2.hasNext()) {
            String next = it2.next();
            Set<String> set = keySet;
            if (widgetFrame2.mCustom.containsKey(next)) {
                CustomVariable customVariable = widgetFrame2.mCustom.get(next);
                CustomVariable customVariable2 = widgetFrame3.mCustom.get(next);
                CustomVariable customVariable3 = new CustomVariable(customVariable);
                it = it2;
                widgetFrame.mCustom.put(next, customVariable3);
                if (customVariable.numberOfInterpolatedValues() == 1) {
                    String str = next;
                    customVariable3.setValue((Object) Float.valueOf(interpolate(customVariable.getValueToInterpolate(), customVariable2.getValueToInterpolate(), 0.0f, f2)));
                } else {
                    String str2 = next;
                    int numberOfInterpolatedValues = customVariable.numberOfInterpolatedValues();
                    float[] fArr = new float[numberOfInterpolatedValues];
                    float[] fArr2 = new float[numberOfInterpolatedValues];
                    customVariable.getValuesToInterpolate(fArr);
                    customVariable2.getValuesToInterpolate(fArr2);
                    CustomVariable customVariable4 = customVariable;
                    int i38 = 0;
                    while (i38 < numberOfInterpolatedValues) {
                        fArr[i38] = interpolate(fArr[i38], fArr2[i38], 0.0f, f2);
                        customVariable3.setValue(fArr);
                        i38++;
                        numberOfInterpolatedValues = numberOfInterpolatedValues;
                        customVariable2 = customVariable2;
                        fArr2 = fArr2;
                    }
                    int i39 = numberOfInterpolatedValues;
                    CustomVariable customVariable5 = customVariable2;
                    float[] fArr3 = fArr2;
                }
            } else {
                it = it2;
                String str3 = next;
            }
            widgetFrame = frame;
            widgetFrame2 = start;
            widgetFrame3 = end;
            keySet = set;
            it2 = it;
        }
    }

    private void serializeAnchor(StringBuilder ret, ConstraintAnchor.Type type) {
        ConstraintAnchor anchor = this.widget.getAnchor(type);
        if (anchor != null && anchor.mTarget != null) {
            ret.append("Anchor");
            ret.append(type.name());
            ret.append(": ['");
            String str = anchor.mTarget.getOwner().stringId;
            ret.append(str == null ? "#PARENT" : str);
            ret.append("', '");
            ret.append(anchor.mTarget.getType().name());
            ret.append("', '");
            ret.append(anchor.mMargin);
            ret.append("'],\n");
        }
    }

    public void addCustomColor(String name2, int color) {
        setCustomAttribute(name2, (int) TypedValues.Custom.TYPE_COLOR, color);
    }

    public void addCustomFloat(String name2, float value) {
        setCustomAttribute(name2, (int) TypedValues.Custom.TYPE_FLOAT, value);
    }

    public float centerX() {
        int i = this.left;
        return ((float) i) + (((float) (this.right - i)) / 2.0f);
    }

    public float centerY() {
        int i = this.top;
        return ((float) i) + (((float) (this.bottom - i)) / 2.0f);
    }

    public CustomVariable getCustomAttribute(String name2) {
        return this.mCustom.get(name2);
    }

    public Set<String> getCustomAttributeNames() {
        return this.mCustom.keySet();
    }

    public int getCustomColor(String name2) {
        if (this.mCustom.containsKey(name2)) {
            return this.mCustom.get(name2).getColorValue();
        }
        return -21880;
    }

    public float getCustomFloat(String name2) {
        if (this.mCustom.containsKey(name2)) {
            return this.mCustom.get(name2).getFloatValue();
        }
        return Float.NaN;
    }

    public String getId() {
        ConstraintWidget constraintWidget = this.widget;
        return constraintWidget == null ? EnvironmentCompat.MEDIA_UNKNOWN : constraintWidget.stringId;
    }

    public int height() {
        return Math.max(0, this.bottom - this.top);
    }

    public boolean isDefaultTransform() {
        if (!Float.isNaN(this.rotationX) || !Float.isNaN(this.rotationY) || !Float.isNaN(this.rotationZ) || !Float.isNaN(this.translationX) || !Float.isNaN(this.translationY) || !Float.isNaN(this.translationZ) || !Float.isNaN(this.scaleX) || !Float.isNaN(this.scaleY) || !Float.isNaN(this.alpha)) {
            return false;
        }
        return OLD_SYSTEM;
    }

    /* access modifiers changed from: package-private */
    public void logv(String str) {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        String str2 = (".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ") " + stackTraceElement.getMethodName()) + " " + (hashCode() % 1000);
        System.out.println((this.widget != null ? str2 + "/" + (this.widget.hashCode() % 1000) : str2 + "/NULL") + " " + str);
    }

    /* access modifiers changed from: package-private */
    public void parseCustom(CLElement custom) throws CLParsingException {
        CLObject cLObject = (CLObject) custom;
        int size = cLObject.size();
        for (int i = 0; i < size; i++) {
            CLKey cLKey = (CLKey) cLObject.get(i);
            String content = cLKey.content();
            CLElement value = cLKey.getValue();
            String content2 = value.content();
            if (content2.matches("#[0-9a-fA-F]+")) {
                setCustomAttribute(cLKey.content(), (int) TypedValues.Custom.TYPE_COLOR, Integer.parseInt(content2.substring(1), 16));
            } else if (value instanceof CLNumber) {
                setCustomAttribute(cLKey.content(), (int) TypedValues.Custom.TYPE_FLOAT, value.getFloat());
            } else {
                setCustomAttribute(cLKey.content(), (int) TypedValues.Custom.TYPE_STRING, content2);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void printCustomAttributes() {
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        String str = (".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ") " + stackTraceElement.getMethodName()) + " " + (hashCode() % 1000);
        String str2 = this.widget != null ? str + "/" + (this.widget.hashCode() % 1000) + " " : str + "/NULL ";
        HashMap<String, CustomVariable> hashMap = this.mCustom;
        if (hashMap != null) {
            for (String str3 : hashMap.keySet()) {
                System.out.println(str2 + this.mCustom.get(str3).toString());
            }
        }
    }

    public StringBuilder serialize(StringBuilder ret) {
        return serialize(ret, false);
    }

    public StringBuilder serialize(StringBuilder ret, boolean sendPhoneOrientation) {
        ret.append("{\n");
        add(ret, "left", this.left);
        add(ret, "top", this.top);
        add(ret, "right", this.right);
        add(ret, "bottom", this.bottom);
        add(ret, "pivotX", this.pivotX);
        add(ret, "pivotY", this.pivotY);
        add(ret, "rotationX", this.rotationX);
        add(ret, "rotationY", this.rotationY);
        add(ret, "rotationZ", this.rotationZ);
        add(ret, "translationX", this.translationX);
        add(ret, "translationY", this.translationY);
        add(ret, "translationZ", this.translationZ);
        add(ret, "scaleX", this.scaleX);
        add(ret, "scaleY", this.scaleY);
        add(ret, "alpha", this.alpha);
        add(ret, "visibility", this.visibility);
        add(ret, "interpolatedPos", this.interpolatedPos);
        if (this.widget != null) {
            for (ConstraintAnchor.Type serializeAnchor : ConstraintAnchor.Type.values()) {
                serializeAnchor(ret, serializeAnchor);
            }
        }
        if (sendPhoneOrientation) {
            add(ret, "phone_orientation", phone_orientation);
        }
        if (sendPhoneOrientation) {
            add(ret, "phone_orientation", phone_orientation);
        }
        if (this.mCustom.size() != 0) {
            ret.append("custom : {\n");
            for (String next : this.mCustom.keySet()) {
                CustomVariable customVariable = this.mCustom.get(next);
                ret.append(next);
                ret.append(": ");
                switch (customVariable.getType()) {
                    case TypedValues.Custom.TYPE_INT:
                        ret.append(customVariable.getIntegerValue());
                        ret.append(",\n");
                        break;
                    case TypedValues.Custom.TYPE_FLOAT:
                    case TypedValues.Custom.TYPE_DIMENSION:
                        ret.append(customVariable.getFloatValue());
                        ret.append(",\n");
                        break;
                    case TypedValues.Custom.TYPE_COLOR:
                        ret.append("'");
                        String colorString = CustomVariable.colorString(customVariable.getIntegerValue());
                        Log1F380D.a((Object) colorString);
                        ret.append(colorString);
                        ret.append("',\n");
                        break;
                    case TypedValues.Custom.TYPE_STRING:
                        ret.append("'");
                        ret.append(customVariable.getStringValue());
                        ret.append("',\n");
                        break;
                    case TypedValues.Custom.TYPE_BOOLEAN:
                        ret.append("'");
                        ret.append(customVariable.getBooleanValue());
                        ret.append("',\n");
                        break;
                }
            }
            ret.append("}\n");
        }
        ret.append("}\n");
        return ret;
    }

    public void setCustomAttribute(String name2, int type, float value) {
        if (this.mCustom.containsKey(name2)) {
            this.mCustom.get(name2).setFloatValue(value);
        } else {
            this.mCustom.put(name2, new CustomVariable(name2, type, value));
        }
    }

    public void setCustomAttribute(String name2, int type, int value) {
        if (this.mCustom.containsKey(name2)) {
            this.mCustom.get(name2).setIntValue(value);
        } else {
            this.mCustom.put(name2, new CustomVariable(name2, type, value));
        }
    }

    public void setCustomAttribute(String name2, int type, String value) {
        if (this.mCustom.containsKey(name2)) {
            this.mCustom.get(name2).setStringValue(value);
        } else {
            this.mCustom.put(name2, new CustomVariable(name2, type, value));
        }
    }

    public void setCustomAttribute(String name2, int type, boolean value) {
        if (this.mCustom.containsKey(name2)) {
            this.mCustom.get(name2).setBooleanValue(value);
        } else {
            this.mCustom.put(name2, new CustomVariable(name2, type, value));
        }
    }

    public void setCustomValue(CustomAttribute valueAt, float[] mTempValues) {
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setValue(java.lang.String r4, androidx.constraintlayout.core.parser.CLElement r5) throws androidx.constraintlayout.core.parser.CLParsingException {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            r1 = 1
            r2 = 0
            switch(r0) {
                case -1881940865: goto L_0x00d0;
                case -1383228885: goto L_0x00c5;
                case -1349088399: goto L_0x00ba;
                case -1249320806: goto L_0x00af;
                case -1249320805: goto L_0x00a4;
                case -1249320804: goto L_0x0099;
                case -1225497657: goto L_0x008e;
                case -1225497656: goto L_0x0083;
                case -1225497655: goto L_0x0078;
                case -987906986: goto L_0x006e;
                case -987906985: goto L_0x0063;
                case -908189618: goto L_0x0056;
                case -908189617: goto L_0x0049;
                case 115029: goto L_0x003c;
                case 3317767: goto L_0x0030;
                case 92909918: goto L_0x0024;
                case 108511772: goto L_0x0017;
                case 642850769: goto L_0x000b;
                default: goto L_0x0009;
            }
        L_0x0009:
            goto L_0x00db
        L_0x000b:
            java.lang.String r0 = "interpolatedPos"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = 11
            goto L_0x00dc
        L_0x0017:
            java.lang.String r0 = "right"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = 15
            goto L_0x00dc
        L_0x0024:
            java.lang.String r0 = "alpha"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = 10
            goto L_0x00dc
        L_0x0030:
            java.lang.String r0 = "left"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = 14
            goto L_0x00dc
        L_0x003c:
            java.lang.String r0 = "top"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = 13
            goto L_0x00dc
        L_0x0049:
            java.lang.String r0 = "scaleY"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = 9
            goto L_0x00dc
        L_0x0056:
            java.lang.String r0 = "scaleX"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = 8
            goto L_0x00dc
        L_0x0063:
            java.lang.String r0 = "pivotY"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = r1
            goto L_0x00dc
        L_0x006e:
            java.lang.String r0 = "pivotX"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = r2
            goto L_0x00dc
        L_0x0078:
            java.lang.String r0 = "translationZ"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = 7
            goto L_0x00dc
        L_0x0083:
            java.lang.String r0 = "translationY"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = 6
            goto L_0x00dc
        L_0x008e:
            java.lang.String r0 = "translationX"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = 5
            goto L_0x00dc
        L_0x0099:
            java.lang.String r0 = "rotationZ"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = 4
            goto L_0x00dc
        L_0x00a4:
            java.lang.String r0 = "rotationY"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = 3
            goto L_0x00dc
        L_0x00af:
            java.lang.String r0 = "rotationX"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = 2
            goto L_0x00dc
        L_0x00ba:
            java.lang.String r0 = "custom"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = 17
            goto L_0x00dc
        L_0x00c5:
            java.lang.String r0 = "bottom"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = 16
            goto L_0x00dc
        L_0x00d0:
            java.lang.String r0 = "phone_orientation"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0009
            r0 = 12
            goto L_0x00dc
        L_0x00db:
            r0 = -1
        L_0x00dc:
            switch(r0) {
                case 0: goto L_0x0157;
                case 1: goto L_0x0150;
                case 2: goto L_0x0149;
                case 3: goto L_0x0142;
                case 4: goto L_0x013b;
                case 5: goto L_0x0134;
                case 6: goto L_0x012d;
                case 7: goto L_0x0126;
                case 8: goto L_0x011f;
                case 9: goto L_0x0118;
                case 10: goto L_0x0111;
                case 11: goto L_0x010a;
                case 12: goto L_0x0103;
                case 13: goto L_0x00fc;
                case 14: goto L_0x00f5;
                case 15: goto L_0x00ed;
                case 16: goto L_0x00e5;
                case 17: goto L_0x00e0;
                default: goto L_0x00df;
            }
        L_0x00df:
            return r2
        L_0x00e0:
            r3.parseCustom(r5)
            goto L_0x015e
        L_0x00e5:
            int r0 = r5.getInt()
            r3.bottom = r0
            goto L_0x015e
        L_0x00ed:
            int r0 = r5.getInt()
            r3.right = r0
            goto L_0x015e
        L_0x00f5:
            int r0 = r5.getInt()
            r3.left = r0
            goto L_0x015e
        L_0x00fc:
            int r0 = r5.getInt()
            r3.top = r0
            goto L_0x015e
        L_0x0103:
            float r0 = r5.getFloat()
            phone_orientation = r0
            goto L_0x015e
        L_0x010a:
            float r0 = r5.getFloat()
            r3.interpolatedPos = r0
            goto L_0x015e
        L_0x0111:
            float r0 = r5.getFloat()
            r3.alpha = r0
            goto L_0x015e
        L_0x0118:
            float r0 = r5.getFloat()
            r3.scaleY = r0
            goto L_0x015e
        L_0x011f:
            float r0 = r5.getFloat()
            r3.scaleX = r0
            goto L_0x015e
        L_0x0126:
            float r0 = r5.getFloat()
            r3.translationZ = r0
            goto L_0x015e
        L_0x012d:
            float r0 = r5.getFloat()
            r3.translationY = r0
            goto L_0x015e
        L_0x0134:
            float r0 = r5.getFloat()
            r3.translationX = r0
            goto L_0x015e
        L_0x013b:
            float r0 = r5.getFloat()
            r3.rotationZ = r0
            goto L_0x015e
        L_0x0142:
            float r0 = r5.getFloat()
            r3.rotationY = r0
            goto L_0x015e
        L_0x0149:
            float r0 = r5.getFloat()
            r3.rotationX = r0
            goto L_0x015e
        L_0x0150:
            float r0 = r5.getFloat()
            r3.pivotY = r0
            goto L_0x015e
        L_0x0157:
            float r0 = r5.getFloat()
            r3.pivotX = r0
        L_0x015e:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.core.state.WidgetFrame.setValue(java.lang.String, androidx.constraintlayout.core.parser.CLElement):boolean");
    }

    public WidgetFrame update() {
        ConstraintWidget constraintWidget = this.widget;
        if (constraintWidget != null) {
            this.left = constraintWidget.getLeft();
            this.top = this.widget.getTop();
            this.right = this.widget.getRight();
            this.bottom = this.widget.getBottom();
            updateAttributes(this.widget.frame);
        }
        return this;
    }

    public WidgetFrame update(ConstraintWidget widget2) {
        if (widget2 == null) {
            return this;
        }
        this.widget = widget2;
        update();
        return this;
    }

    public void updateAttributes(WidgetFrame frame) {
        this.pivotX = frame.pivotX;
        this.pivotY = frame.pivotY;
        this.rotationX = frame.rotationX;
        this.rotationY = frame.rotationY;
        this.rotationZ = frame.rotationZ;
        this.translationX = frame.translationX;
        this.translationY = frame.translationY;
        this.translationZ = frame.translationZ;
        this.scaleX = frame.scaleX;
        this.scaleY = frame.scaleY;
        this.alpha = frame.alpha;
        this.visibility = frame.visibility;
        this.mCustom.clear();
        if (frame != null) {
            for (CustomVariable next : frame.mCustom.values()) {
                this.mCustom.put(next.getName(), next.copy());
            }
        }
    }

    public int width() {
        return Math.max(0, this.right - this.left);
    }
}

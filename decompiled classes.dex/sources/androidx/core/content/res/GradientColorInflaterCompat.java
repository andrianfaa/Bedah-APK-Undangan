package androidx.core.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import androidx.core.R;
import java.io.IOException;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

final class GradientColorInflaterCompat {
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_MIRROR = 2;
    private static final int TILE_MODE_REPEAT = 1;

    static final class ColorStops {
        final int[] mColors;
        final float[] mOffsets;

        ColorStops(int startColor, int endColor) {
            this.mColors = new int[]{startColor, endColor};
            this.mOffsets = new float[]{0.0f, 1.0f};
        }

        ColorStops(int startColor, int centerColor, int endColor) {
            this.mColors = new int[]{startColor, centerColor, endColor};
            this.mOffsets = new float[]{0.0f, 0.5f, 1.0f};
        }

        ColorStops(List<Integer> list, List<Float> list2) {
            int size = list.size();
            this.mColors = new int[size];
            this.mOffsets = new float[size];
            for (int i = 0; i < size; i++) {
                this.mColors[i] = list.get(i).intValue();
                this.mOffsets[i] = list2.get(i).floatValue();
            }
        }
    }

    private GradientColorInflaterCompat() {
    }

    private static ColorStops checkColors(ColorStops colorItems, int startColor, int endColor, boolean hasCenterColor, int centerColor) {
        return colorItems != null ? colorItems : hasCenterColor ? new ColorStops(startColor, centerColor, endColor) : new ColorStops(startColor, endColor);
    }

    /* JADX WARNING: Removed duplicated region for block: B:6:0x0012  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0017  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static android.graphics.Shader createFromXml(android.content.res.Resources r4, org.xmlpull.v1.XmlPullParser r5, android.content.res.Resources.Theme r6) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            android.util.AttributeSet r0 = android.util.Xml.asAttributeSet(r5)
        L_0x0004:
            int r1 = r5.next()
            r2 = r1
            r3 = 2
            if (r1 == r3) goto L_0x0010
            r1 = 1
            if (r2 == r1) goto L_0x0010
            goto L_0x0004
        L_0x0010:
            if (r2 != r3) goto L_0x0017
            android.graphics.Shader r1 = createFromXmlInner(r4, r5, r0, r6)
            return r1
        L_0x0017:
            org.xmlpull.v1.XmlPullParserException r1 = new org.xmlpull.v1.XmlPullParserException
            java.lang.String r3 = "No start tag found"
            r1.<init>(r3)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.res.GradientColorInflaterCompat.createFromXml(android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.content.res.Resources$Theme):android.graphics.Shader");
    }

    static Shader createFromXmlInner(Resources resources, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws IOException, XmlPullParserException {
        XmlPullParser xmlPullParser = parser;
        String name = parser.getName();
        if (name.equals("gradient")) {
            Resources.Theme theme2 = theme;
            TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme2, attrs, R.styleable.GradientColor);
            float namedFloat = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "startX", R.styleable.GradientColor_android_startX, 0.0f);
            float namedFloat2 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "startY", R.styleable.GradientColor_android_startY, 0.0f);
            float namedFloat3 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "endX", R.styleable.GradientColor_android_endX, 0.0f);
            float namedFloat4 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "endY", R.styleable.GradientColor_android_endY, 0.0f);
            float namedFloat5 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "centerX", R.styleable.GradientColor_android_centerX, 0.0f);
            float namedFloat6 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "centerY", R.styleable.GradientColor_android_centerY, 0.0f);
            int namedInt = TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser, "type", R.styleable.GradientColor_android_type, 0);
            int namedColor = TypedArrayUtils.getNamedColor(obtainAttributes, xmlPullParser, "startColor", R.styleable.GradientColor_android_startColor, 0);
            boolean hasAttribute = TypedArrayUtils.hasAttribute(xmlPullParser, "centerColor");
            int namedColor2 = TypedArrayUtils.getNamedColor(obtainAttributes, xmlPullParser, "centerColor", R.styleable.GradientColor_android_centerColor, 0);
            int namedColor3 = TypedArrayUtils.getNamedColor(obtainAttributes, xmlPullParser, "endColor", R.styleable.GradientColor_android_endColor, 0);
            int namedInt2 = TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser, "tileMode", R.styleable.GradientColor_android_tileMode, 0);
            float namedFloat7 = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "gradientRadius", R.styleable.GradientColor_android_gradientRadius, 0.0f);
            obtainAttributes.recycle();
            ColorStops checkColors = checkColors(inflateChildElements(resources, parser, attrs, theme), namedColor, namedColor3, hasAttribute, namedColor2);
            switch (namedInt) {
                case 1:
                    if (namedFloat7 > 0.0f) {
                        int[] iArr = checkColors.mColors;
                        return new RadialGradient(namedFloat5, namedFloat6, namedFloat7, iArr, checkColors.mOffsets, parseTileMode(namedInt2));
                    }
                    throw new XmlPullParserException("<gradient> tag requires 'gradientRadius' attribute with radial type");
                case 2:
                    return new SweepGradient(namedFloat5, namedFloat6, checkColors.mColors, checkColors.mOffsets);
                default:
                    int[] iArr2 = checkColors.mColors;
                    int i = namedColor2;
                    int[] iArr3 = iArr2;
                    boolean z = hasAttribute;
                    int i2 = namedColor;
                    float f = namedFloat6;
                    int[] iArr4 = iArr3;
                    float f2 = namedFloat5;
                    return new LinearGradient(namedFloat, namedFloat2, namedFloat3, namedFloat4, iArr4, checkColors.mOffsets, parseTileMode(namedInt2));
            }
        } else {
            Resources.Theme theme3 = theme;
            throw new XmlPullParserException(parser.getPositionDescription() + ": invalid gradient color tag " + name);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0089, code lost:
        throw new org.xmlpull.v1.XmlPullParserException(r13.getPositionDescription() + ": <item> tag requires a 'color' attribute and a 'offset' attribute!");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static androidx.core.content.res.GradientColorInflaterCompat.ColorStops inflateChildElements(android.content.res.Resources r12, org.xmlpull.v1.XmlPullParser r13, android.util.AttributeSet r14, android.content.res.Resources.Theme r15) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            int r0 = r13.getDepth()
            r1 = 1
            int r0 = r0 + r1
            java.util.ArrayList r2 = new java.util.ArrayList
            r3 = 20
            r2.<init>(r3)
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>(r3)
            r3 = r4
        L_0x0013:
            int r4 = r13.next()
            r5 = r4
            if (r4 == r1) goto L_0x008a
            int r4 = r13.getDepth()
            r6 = r4
            if (r4 >= r0) goto L_0x0024
            r4 = 3
            if (r5 == r4) goto L_0x008a
        L_0x0024:
            r4 = 2
            if (r5 == r4) goto L_0x0028
            goto L_0x0013
        L_0x0028:
            if (r6 > r0) goto L_0x0013
            java.lang.String r4 = r13.getName()
            java.lang.String r7 = "item"
            boolean r4 = r4.equals(r7)
            if (r4 != 0) goto L_0x0037
            goto L_0x0013
        L_0x0037:
            int[] r4 = androidx.core.R.styleable.GradientColorItem
            android.content.res.TypedArray r4 = androidx.core.content.res.TypedArrayUtils.obtainAttributes(r12, r15, r14, r4)
            int r7 = androidx.core.R.styleable.GradientColorItem_android_color
            boolean r7 = r4.hasValue(r7)
            int r8 = androidx.core.R.styleable.GradientColorItem_android_offset
            boolean r8 = r4.hasValue(r8)
            if (r7 == 0) goto L_0x006d
            if (r8 == 0) goto L_0x006d
            int r9 = androidx.core.R.styleable.GradientColorItem_android_color
            r10 = 0
            int r9 = r4.getColor(r9, r10)
            int r10 = androidx.core.R.styleable.GradientColorItem_android_offset
            r11 = 0
            float r10 = r4.getFloat(r10, r11)
            r4.recycle()
            java.lang.Integer r11 = java.lang.Integer.valueOf(r9)
            r3.add(r11)
            java.lang.Float r11 = java.lang.Float.valueOf(r10)
            r2.add(r11)
            goto L_0x0013
        L_0x006d:
            org.xmlpull.v1.XmlPullParserException r1 = new org.xmlpull.v1.XmlPullParserException
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = r13.getPositionDescription()
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.String r10 = ": <item> tag requires a 'color' attribute and a 'offset' attribute!"
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.String r9 = r9.toString()
            r1.<init>(r9)
            throw r1
        L_0x008a:
            int r1 = r3.size()
            if (r1 <= 0) goto L_0x0096
            androidx.core.content.res.GradientColorInflaterCompat$ColorStops r1 = new androidx.core.content.res.GradientColorInflaterCompat$ColorStops
            r1.<init>((java.util.List<java.lang.Integer>) r3, (java.util.List<java.lang.Float>) r2)
            return r1
        L_0x0096:
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.res.GradientColorInflaterCompat.inflateChildElements(android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.content.res.Resources$Theme):androidx.core.content.res.GradientColorInflaterCompat$ColorStops");
    }

    private static Shader.TileMode parseTileMode(int tileMode) {
        switch (tileMode) {
            case 1:
                return Shader.TileMode.REPEAT;
            case 2:
                return Shader.TileMode.MIRROR;
            default:
                return Shader.TileMode.CLAMP;
        }
    }
}

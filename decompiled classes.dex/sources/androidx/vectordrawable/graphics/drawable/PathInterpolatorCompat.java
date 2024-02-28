package androidx.vectordrawable.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.animation.Interpolator;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.PathParser;
import mt.Log1F380D;
import org.xmlpull.v1.XmlPullParser;

/* compiled from: 0099 */
public class PathInterpolatorCompat implements Interpolator {
    public static final double EPSILON = 1.0E-5d;
    public static final int MAX_NUM_POINTS = 3000;
    private static final float PRECISION = 0.002f;
    private float[] mX;
    private float[] mY;

    public PathInterpolatorCompat(Context context, AttributeSet attrs, XmlPullParser parser) {
        this(context.getResources(), context.getTheme(), attrs, parser);
    }

    public PathInterpolatorCompat(Resources res, Resources.Theme theme, AttributeSet attrs, XmlPullParser parser) {
        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(res, theme, attrs, AndroidResources.STYLEABLE_PATH_INTERPOLATOR);
        parseInterpolatorFromTypeArray(obtainAttributes, parser);
        obtainAttributes.recycle();
    }

    private void initCubic(float x1, float y1, float x2, float y2) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(x1, y1, x2, y2, 1.0f, 1.0f);
        initPath(path);
    }

    private void initPath(Path path) {
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float length = pathMeasure.getLength();
        int min = Math.min(MAX_NUM_POINTS, ((int) (length / 0.002f)) + 1);
        if (min > 0) {
            this.mX = new float[min];
            this.mY = new float[min];
            float[] fArr = new float[2];
            for (int i = 0; i < min; i++) {
                pathMeasure.getPosTan((((float) i) * length) / ((float) (min - 1)), fArr, (float[]) null);
                this.mX[i] = fArr[0];
                this.mY[i] = fArr[1];
            }
            if (((double) Math.abs(this.mX[0])) > 1.0E-5d || ((double) Math.abs(this.mY[0])) > 1.0E-5d || ((double) Math.abs(this.mX[min - 1] - 1.0f)) > 1.0E-5d || ((double) Math.abs(this.mY[min - 1] - 1.0f)) > 1.0E-5d) {
                throw new IllegalArgumentException("The Path must start at (0,0) and end at (1,1) start: " + this.mX[0] + "," + this.mY[0] + " end:" + this.mX[min - 1] + "," + this.mY[min - 1]);
            }
            float f = 0.0f;
            int i2 = 0;
            int i3 = 0;
            while (i3 < min) {
                float[] fArr2 = this.mX;
                int i4 = i2 + 1;
                float f2 = fArr2[i2];
                if (f2 >= f) {
                    fArr2[i3] = f2;
                    f = f2;
                    i3++;
                    i2 = i4;
                } else {
                    throw new IllegalArgumentException("The Path cannot loop back on itself, x :" + f2);
                }
            }
            if (pathMeasure.nextContour()) {
                throw new IllegalArgumentException("The Path should be continuous, can't have 2+ contours");
            }
            return;
        }
        throw new IllegalArgumentException("The Path has a invalid length " + length);
    }

    private void initQuad(float controlX, float controlY) {
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.quadTo(controlX, controlY, 1.0f, 1.0f);
        initPath(path);
    }

    private void parseInterpolatorFromTypeArray(TypedArray a, XmlPullParser parser) {
        if (TypedArrayUtils.hasAttribute(parser, "pathData")) {
            String namedString = TypedArrayUtils.getNamedString(a, parser, "pathData", 4);
            Log1F380D.a((Object) namedString);
            Path createPathFromPathData = PathParser.createPathFromPathData(namedString);
            if (createPathFromPathData != null) {
                initPath(createPathFromPathData);
                return;
            }
            throw new InflateException("The path is null, which is created from " + namedString);
        } else if (!TypedArrayUtils.hasAttribute(parser, "controlX1")) {
            throw new InflateException("pathInterpolator requires the controlX1 attribute");
        } else if (TypedArrayUtils.hasAttribute(parser, "controlY1")) {
            float namedFloat = TypedArrayUtils.getNamedFloat(a, parser, "controlX1", 0, 0.0f);
            float namedFloat2 = TypedArrayUtils.getNamedFloat(a, parser, "controlY1", 1, 0.0f);
            boolean hasAttribute = TypedArrayUtils.hasAttribute(parser, "controlX2");
            if (hasAttribute != TypedArrayUtils.hasAttribute(parser, "controlY2")) {
                throw new InflateException("pathInterpolator requires both controlX2 and controlY2 for cubic Beziers.");
            } else if (!hasAttribute) {
                initQuad(namedFloat, namedFloat2);
            } else {
                initCubic(namedFloat, namedFloat2, TypedArrayUtils.getNamedFloat(a, parser, "controlX2", 2, 0.0f), TypedArrayUtils.getNamedFloat(a, parser, "controlY2", 3, 0.0f));
            }
        } else {
            throw new InflateException("pathInterpolator requires the controlY1 attribute");
        }
    }

    public float getInterpolation(float t) {
        if (t <= 0.0f) {
            return 0.0f;
        }
        if (t >= 1.0f) {
            return 1.0f;
        }
        int i = 0;
        int length = this.mX.length - 1;
        while (length - i > 1) {
            int i2 = (i + length) / 2;
            if (t < this.mX[i2]) {
                length = i2;
            } else {
                i = i2;
            }
        }
        float[] fArr = this.mX;
        float f = fArr[length];
        float f2 = fArr[i];
        float f3 = f - f2;
        if (f3 == 0.0f) {
            return this.mY[i];
        }
        float[] fArr2 = this.mY;
        float f4 = fArr2[i];
        return ((fArr2[length] - f4) * ((t - f2) / f3)) + f4;
    }
}

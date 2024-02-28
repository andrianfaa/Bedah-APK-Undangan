package androidx.transition;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;
import org.xmlpull.v1.XmlPullParser;

public class ArcMotion extends PathMotion {
    private static final float DEFAULT_MAX_ANGLE_DEGREES = 70.0f;
    private static final float DEFAULT_MAX_TANGENT = ((float) Math.tan(Math.toRadians(35.0d)));
    private static final float DEFAULT_MIN_ANGLE_DEGREES = 0.0f;
    private float mMaximumAngle = DEFAULT_MAX_ANGLE_DEGREES;
    private float mMaximumTangent = DEFAULT_MAX_TANGENT;
    private float mMinimumHorizontalAngle = 0.0f;
    private float mMinimumHorizontalTangent = 0.0f;
    private float mMinimumVerticalAngle = 0.0f;
    private float mMinimumVerticalTangent = 0.0f;

    public ArcMotion() {
    }

    public ArcMotion(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, Styleable.ARC_MOTION);
        XmlPullParser xmlPullParser = (XmlPullParser) attrs;
        setMinimumVerticalAngle(TypedArrayUtils.getNamedFloat(obtainStyledAttributes, xmlPullParser, "minimumVerticalAngle", 1, 0.0f));
        setMinimumHorizontalAngle(TypedArrayUtils.getNamedFloat(obtainStyledAttributes, xmlPullParser, "minimumHorizontalAngle", 0, 0.0f));
        setMaximumAngle(TypedArrayUtils.getNamedFloat(obtainStyledAttributes, xmlPullParser, "maximumAngle", 2, DEFAULT_MAX_ANGLE_DEGREES));
        obtainStyledAttributes.recycle();
    }

    private static float toTangent(float arcInDegrees) {
        if (arcInDegrees >= 0.0f && arcInDegrees <= 90.0f) {
            return (float) Math.tan(Math.toRadians((double) (arcInDegrees / 2.0f)));
        }
        throw new IllegalArgumentException("Arc must be between 0 and 90 degrees");
    }

    public float getMaximumAngle() {
        return this.mMaximumAngle;
    }

    public float getMinimumHorizontalAngle() {
        return this.mMinimumHorizontalAngle;
    }

    public float getMinimumVerticalAngle() {
        return this.mMinimumVerticalAngle;
    }

    public Path getPath(float startX, float startY, float endX, float endY) {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8 = startX;
        float f9 = startY;
        Path path = new Path();
        path.moveTo(f8, f9);
        float f10 = endX - f8;
        float f11 = endY - f9;
        float f12 = (f10 * f10) + (f11 * f11);
        float f13 = (f8 + endX) / 2.0f;
        float f14 = (f9 + endY) / 2.0f;
        float f15 = f12 * 0.25f;
        boolean z = f9 > endY;
        if (Math.abs(f10) < Math.abs(f11)) {
            float abs = Math.abs(f12 / (f11 * 2.0f));
            if (z) {
                f3 = endY + abs;
                f2 = endX;
            } else {
                f3 = f9 + abs;
                f2 = startX;
            }
            float f16 = this.mMinimumVerticalTangent;
            f = f15 * f16 * f16;
        } else {
            float f17 = f12 / (f10 * 2.0f);
            if (z) {
                f6 = f8 + f17;
                f7 = startY;
            } else {
                f6 = endX - f17;
                f7 = endY;
            }
            float f18 = this.mMinimumHorizontalTangent;
            f = f15 * f18 * f18;
        }
        float f19 = f13 - f2;
        float f20 = f14 - f3;
        float f21 = (f19 * f19) + (f20 * f20);
        float f22 = this.mMaximumTangent;
        float f23 = f15 * f22 * f22;
        float f24 = f21 < f ? f : f21 > f23 ? f23 : 0.0f;
        if (f24 != 0.0f) {
            float sqrt = (float) Math.sqrt((double) (f24 / f21));
            f5 = f14 + ((f3 - f14) * sqrt);
            f4 = f13 + ((f2 - f13) * sqrt);
        } else {
            f5 = f3;
            f4 = f2;
        }
        path.cubicTo((f8 + f4) / 2.0f, (f9 + f5) / 2.0f, (f4 + endX) / 2.0f, (f5 + endY) / 2.0f, endX, endY);
        return path;
    }

    public void setMaximumAngle(float angleInDegrees) {
        this.mMaximumAngle = angleInDegrees;
        this.mMaximumTangent = toTangent(angleInDegrees);
    }

    public void setMinimumHorizontalAngle(float angleInDegrees) {
        this.mMinimumHorizontalAngle = angleInDegrees;
        this.mMinimumHorizontalTangent = toTangent(angleInDegrees);
    }

    public void setMinimumVerticalAngle(float angleInDegrees) {
        this.mMinimumVerticalAngle = angleInDegrees;
        this.mMinimumVerticalTangent = toTangent(angleInDegrees);
    }
}

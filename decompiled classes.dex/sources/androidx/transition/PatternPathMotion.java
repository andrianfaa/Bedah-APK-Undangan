package androidx.transition;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.PathParser;
import mt.Log1F380D;
import org.xmlpull.v1.XmlPullParser;

/* compiled from: 0094 */
public class PatternPathMotion extends PathMotion {
    private Path mOriginalPatternPath;
    private final Path mPatternPath;
    private final Matrix mTempMatrix;

    public PatternPathMotion() {
        Path path = new Path();
        this.mPatternPath = path;
        this.mTempMatrix = new Matrix();
        path.lineTo(1.0f, 0.0f);
        this.mOriginalPatternPath = path;
    }

    public PatternPathMotion(Context context, AttributeSet attrs) {
        this.mPatternPath = new Path();
        this.mTempMatrix = new Matrix();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, Styleable.PATTERN_PATH_MOTION);
        try {
            String namedString = TypedArrayUtils.getNamedString(obtainStyledAttributes, (XmlPullParser) attrs, "patternPathData", 0);
            Log1F380D.a((Object) namedString);
            if (namedString != null) {
                setPatternPath(PathParser.createPathFromPathData(namedString));
                return;
            }
            throw new RuntimeException("pathData must be supplied for patternPathMotion");
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public PatternPathMotion(Path patternPath) {
        this.mPatternPath = new Path();
        this.mTempMatrix = new Matrix();
        setPatternPath(patternPath);
    }

    private static float distance(float x, float y) {
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    public Path getPath(float startX, float startY, float endX, float endY) {
        float f = endX - startX;
        float f2 = endY - startY;
        float distance = distance(f, f2);
        double atan2 = Math.atan2((double) f2, (double) f);
        this.mTempMatrix.setScale(distance, distance);
        this.mTempMatrix.postRotate((float) Math.toDegrees(atan2));
        this.mTempMatrix.postTranslate(startX, startY);
        Path path = new Path();
        this.mPatternPath.transform(this.mTempMatrix, path);
        return path;
    }

    public Path getPatternPath() {
        return this.mOriginalPatternPath;
    }

    public void setPatternPath(Path patternPath) {
        Path path = patternPath;
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float length = pathMeasure.getLength();
        float[] fArr = new float[2];
        pathMeasure.getPosTan(length, fArr, (float[]) null);
        float f = fArr[0];
        float f2 = fArr[1];
        pathMeasure.getPosTan(0.0f, fArr, (float[]) null);
        float f3 = fArr[0];
        float f4 = fArr[1];
        if (f3 == f && f4 == f2) {
            throw new IllegalArgumentException("pattern must not end at the starting point");
        }
        this.mTempMatrix.setTranslate(-f3, -f4);
        float f5 = f - f3;
        float f6 = f2 - f4;
        float distance = 1.0f / distance(f5, f6);
        this.mTempMatrix.postScale(distance, distance);
        PathMeasure pathMeasure2 = pathMeasure;
        float f7 = f3;
        float f8 = length;
        float[] fArr2 = fArr;
        this.mTempMatrix.postRotate((float) Math.toDegrees(-Math.atan2((double) f6, (double) f5)));
        path.transform(this.mTempMatrix, this.mPatternPath);
        this.mOriginalPatternPath = path;
    }
}

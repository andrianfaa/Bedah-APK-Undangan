package androidx.core.graphics;

import android.graphics.Path;
import android.graphics.PointF;
import java.util.ArrayList;
import java.util.Collection;

public final class PathUtils {

    static class Api26Impl {
        private Api26Impl() {
        }

        static float[] approximate(Path path, float acceptableError) {
            return path.approximate(acceptableError);
        }
    }

    private PathUtils() {
    }

    public static Collection<PathSegment> flatten(Path path) {
        return flatten(path, 0.5f);
    }

    public static Collection<PathSegment> flatten(Path path, float error) {
        float[] approximate = Api26Impl.approximate(path, error);
        int length = approximate.length / 3;
        ArrayList arrayList = new ArrayList(length);
        for (int i = 1; i < length; i++) {
            int i2 = i * 3;
            int i3 = (i - 1) * 3;
            float f = approximate[i2];
            float f2 = approximate[i2 + 1];
            float f3 = approximate[i2 + 2];
            float f4 = approximate[i3];
            float f5 = approximate[i3 + 1];
            float f6 = approximate[i3 + 2];
            if (!(f == f4 || (f2 == f5 && f3 == f6))) {
                arrayList.add(new PathSegment(new PointF(f5, f6), f4, new PointF(f2, f3), f));
            }
        }
        return arrayList;
    }
}

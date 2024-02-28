package androidx.core.content.res;

import android.graphics.Color;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.ViewCompat;

final class CamUtils {
    static final float[][] CAM16RGB_TO_XYZ = {new float[]{1.8620678f, -1.0112547f, 0.14918678f}, new float[]{0.38752654f, 0.62144744f, -0.00897398f}, new float[]{-0.0158415f, -0.03412294f, 1.0499644f}};
    static final float[][] SRGB_TO_XYZ = {new float[]{0.41233894f, 0.35762063f, 0.18051042f}, new float[]{0.2126f, 0.7152f, 0.0722f}, new float[]{0.01932141f, 0.11916382f, 0.9503448f}};
    static final float[] WHITE_POINT_D65 = {95.047f, 100.0f, 108.883f};
    static final float[][] XYZ_TO_CAM16RGB = {new float[]{0.401288f, 0.650173f, -0.051461f}, new float[]{-0.250268f, 1.204414f, 0.045854f}, new float[]{-0.002079f, 0.048952f, 0.953127f}};

    private CamUtils() {
    }

    static int intFromLStar(float lStar) {
        if (lStar < 1.0f) {
            return ViewCompat.MEASURED_STATE_MASK;
        }
        if (lStar > 99.0f) {
            return -1;
        }
        float f = (lStar + 16.0f) / 116.0f;
        float f2 = f;
        float f3 = f;
        float f4 = (lStar > 8.0f ? 1 : (lStar == 8.0f ? 0 : -1)) > 0 ? f * f * f : lStar / 903.2963f;
        boolean z = (f * f) * f > 0.008856452f;
        float f5 = z ? f3 * f3 * f3 : ((f3 * 116.0f) - 16.0f) / 903.2963f;
        float f6 = z ? f2 * f2 * f2 : ((116.0f * f3) - 16.0f) / 903.2963f;
        float[] fArr = WHITE_POINT_D65;
        float f7 = f6;
        float f8 = f;
        return ColorUtils.XYZToColor((double) (fArr[0] * f5), (double) (fArr[1] * f4), (double) (fArr[2] * f6));
    }

    static float lStarFromInt(int argb) {
        return lStarFromY(yFromInt(argb));
    }

    static float lStarFromY(float y) {
        float y2 = y / 100.0f;
        return y2 <= 0.008856452f ? 903.2963f * y2 : (116.0f * ((float) Math.cbrt((double) y2))) - 16.0f;
    }

    static float lerp(float start, float stop, float amount) {
        return ((stop - start) * amount) + start;
    }

    static float linearized(int rgbComponent) {
        float f = ((float) rgbComponent) / 255.0f;
        return f <= 0.04045f ? (f / 12.92f) * 100.0f : ((float) Math.pow((double) ((0.055f + f) / 1.055f), 2.4000000953674316d)) * 100.0f;
    }

    static float[] xyzFromInt(int argb) {
        float linearized = linearized(Color.red(argb));
        float linearized2 = linearized(Color.green(argb));
        float linearized3 = linearized(Color.blue(argb));
        float[][] fArr = SRGB_TO_XYZ;
        return new float[]{(fArr[0][0] * linearized) + (fArr[0][1] * linearized2) + (fArr[0][2] * linearized3), (fArr[1][0] * linearized) + (fArr[1][1] * linearized2) + (fArr[1][2] * linearized3), (fArr[2][0] * linearized) + (fArr[2][1] * linearized2) + (fArr[2][2] * linearized3)};
    }

    static float yFromInt(int argb) {
        float linearized = linearized(Color.red(argb));
        float linearized2 = linearized(Color.green(argb));
        float linearized3 = linearized(Color.blue(argb));
        float[][] fArr = SRGB_TO_XYZ;
        return (fArr[1][0] * linearized) + (fArr[1][1] * linearized2) + (fArr[1][2] * linearized3);
    }

    static float yFromLStar(float lstar) {
        return lstar > 8.0f ? ((float) Math.pow((((double) lstar) + 16.0d) / 116.0d, 3.0d)) * 100.0f : (lstar / 903.2963f) * 100.0f;
    }
}

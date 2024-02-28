package com.google.android.material.color;

import androidx.core.view.ViewCompat;
import java.util.Arrays;
import mt.Log1F380D;

/* compiled from: 00EA */
final class ColorUtils {
    private static final float[] WHITE_POINT_D65 = {95.047f, 100.0f, 108.883f};

    private ColorUtils() {
    }

    public static int blueFromInt(int argb) {
        return argb & 255;
    }

    public static float delinearized(float rgb) {
        return rgb <= 0.0031308f ? 12.92f * rgb : (((float) Math.pow((double) rgb, 0.4166666567325592d)) * 1.055f) - 0.055f;
    }

    public static int greenFromInt(int argb) {
        return (65280 & argb) >> 8;
    }

    public static String hexFromInt(int argb) {
        int redFromInt = redFromInt(argb);
        int blueFromInt = blueFromInt(argb);
        String format = String.format("#%02x%02x%02x", new Object[]{Integer.valueOf(redFromInt), Integer.valueOf(greenFromInt(argb)), Integer.valueOf(blueFromInt)});
        Log1F380D.a((Object) format);
        return format;
    }

    public static int intFromLab(double l, double a, double b) {
        double d = (l + 16.0d) / 116.0d;
        double d2 = (a / 500.0d) + d;
        double d3 = d - (b / 200.0d);
        double d4 = d2 * d2 * d2;
        double d5 = d4 > 0.008856451679035631d ? d4 : ((d2 * 116.0d) - 16.0d) / 903.2962962962963d;
        double d6 = l > 8.0d ? d * d * d : l / 903.2962962962963d;
        double d7 = d3 * d3 * d3;
        double d8 = d7 > 0.008856451679035631d ? d7 : ((116.0d * d3) - 16.0d) / 903.2962962962963d;
        float[] fArr = WHITE_POINT_D65;
        double d9 = ((double) fArr[0]) * d5;
        double d10 = d9;
        return intFromXyzComponents((float) d9, (float) (((double) fArr[1]) * d6), (float) (((double) fArr[2]) * d8));
    }

    public static int intFromLstar(float lstar) {
        float f = (lstar + 16.0f) / 116.0f;
        float f2 = f;
        float f3 = f;
        boolean z = (f * f) * f > 0.008856452f;
        float f4 = (lstar > 8.0f ? 1 : (lstar == 8.0f ? 0 : -1)) > 0 ? f * f * f : lstar / 903.2963f;
        float f5 = z ? f3 * f3 * f3 : ((f3 * 116.0f) - 16.0f) / 903.2963f;
        float f6 = z ? f2 * f2 * f2 : ((116.0f * f3) - 16.0f) / 903.2963f;
        float[] fArr = WHITE_POINT_D65;
        return intFromXyz(new float[]{fArr[0] * f5, fArr[1] * f4, fArr[2] * f6});
    }

    public static int intFromRgb(int r, int g, int b) {
        return (((((r & 255) << 16) | ViewCompat.MEASURED_STATE_MASK) | ((g & 255) << 8)) | (b & 255)) >>> 0;
    }

    public static int intFromXyz(float[] xyz) {
        return intFromXyzComponents(xyz[0], xyz[1], xyz[2]);
    }

    public static int intFromXyzComponents(float x, float y, float z) {
        float x2 = x / 100.0f;
        float y2 = y / 100.0f;
        float z2 = z / 100.0f;
        return intFromRgb(Math.max(Math.min(255, Math.round(delinearized((3.2406f * x2) + (-1.5372f * y2) + (-0.4986f * z2)) * 255.0f)), 0), Math.max(Math.min(255, Math.round(delinearized((-0.9689f * x2) + (1.8758f * y2) + (0.0415f * z2)) * 255.0f)), 0), Math.max(Math.min(255, Math.round(255.0f * delinearized((0.0557f * x2) + (-0.204f * y2) + (1.057f * z2)))), 0));
    }

    public static double[] labFromInt(int argb) {
        double d;
        double d2;
        float[] xyzFromInt = xyzFromInt(argb);
        float f = xyzFromInt[1];
        float[] fArr = WHITE_POINT_D65;
        double d3 = (double) (f / fArr[1]);
        double cbrt = d3 > 0.008856451679035631d ? Math.cbrt(d3) : ((d3 * 903.2962962962963d) + 16.0d) / 116.0d;
        float[] fArr2 = fArr;
        double d4 = (double) (xyzFromInt[0] / fArr[0]);
        double cbrt2 = d4 > 0.008856451679035631d ? Math.cbrt(d4) : ((d4 * 903.2962962962963d) + 16.0d) / 116.0d;
        double d5 = (double) (xyzFromInt[2] / fArr2[2]);
        if (d5 > 0.008856451679035631d) {
            d = Math.cbrt(d5);
            d2 = 116.0d;
        } else {
            d2 = 116.0d;
            d = ((903.2962962962963d * d5) + 16.0d) / 116.0d;
        }
        return new double[]{(d2 * cbrt) - 16.0d, (cbrt2 - cbrt) * 500.0d, (cbrt - d) * 200.0d};
    }

    public static float linearized(float rgb) {
        return rgb <= 0.04045f ? rgb / 12.92f : (float) Math.pow((double) ((0.055f + rgb) / 1.055f), 2.4000000953674316d);
    }

    public static float lstarFromInt(int argb) {
        return (float) labFromInt(argb)[0];
    }

    public static int redFromInt(int argb) {
        return (16711680 & argb) >> 16;
    }

    public static final float[] whitePointD65() {
        return Arrays.copyOf(WHITE_POINT_D65, 3);
    }

    public static float[] xyzFromInt(int argb) {
        float linearized = linearized(((float) redFromInt(argb)) / 255.0f) * 100.0f;
        float linearized2 = linearized(((float) greenFromInt(argb)) / 255.0f) * 100.0f;
        float linearized3 = linearized(((float) blueFromInt(argb)) / 255.0f) * 100.0f;
        return new float[]{(0.41233894f * linearized) + (0.35762063f * linearized2) + (0.18051042f * linearized3), (0.2126f * linearized) + (0.7152f * linearized2) + (0.0722f * linearized3), (0.01932141f * linearized) + (0.11916382f * linearized2) + (0.9503448f * linearized3)};
    }

    public static float yFromLstar(float lstar) {
        return lstar > 8.0f ? ((float) Math.pow((((double) lstar) + 16.0d) / 116.0d, 3.0d)) * 100.0f : (lstar / 903.2963f) * 100.0f;
    }
}

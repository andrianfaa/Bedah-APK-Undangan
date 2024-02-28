package com.google.android.material.slider;

import java.util.Locale;
import mt.Log1F380D;

/* compiled from: 00FC */
public final class BasicLabelFormatter implements LabelFormatter {
    private static final int BILLION = 1000000000;
    private static final int MILLION = 1000000;
    private static final int THOUSAND = 1000;
    private static final long TRILLION = 1000000000000L;

    public String getFormattedValue(float value) {
        if (value >= 1.0E12f) {
            String format = String.format(Locale.US, "%.1fT", new Object[]{Float.valueOf(value / 1.0E12f)});
            Log1F380D.a((Object) format);
            return format;
        } else if (value >= 1.0E9f) {
            String format2 = String.format(Locale.US, "%.1fB", new Object[]{Float.valueOf(value / 1.0E9f)});
            Log1F380D.a((Object) format2);
            return format2;
        } else if (value >= 1000000.0f) {
            String format3 = String.format(Locale.US, "%.1fM", new Object[]{Float.valueOf(value / 1000000.0f)});
            Log1F380D.a((Object) format3);
            return format3;
        } else if (value >= 1000.0f) {
            String format4 = String.format(Locale.US, "%.1fK", new Object[]{Float.valueOf(value / 1000.0f)});
            Log1F380D.a((Object) format4);
            return format4;
        } else {
            String format5 = String.format(Locale.US, "%.0f", new Object[]{Float.valueOf(value)});
            Log1F380D.a((Object) format5);
            return format5;
        }
    }
}

package androidx.appcompat.app;

class TwilightCalculator {
    private static final float ALTIDUTE_CORRECTION_CIVIL_TWILIGHT = -0.10471976f;
    private static final float C1 = 0.0334196f;
    private static final float C2 = 3.49066E-4f;
    private static final float C3 = 5.236E-6f;
    public static final int DAY = 0;
    private static final float DEGREES_TO_RADIANS = 0.017453292f;
    private static final float J0 = 9.0E-4f;
    public static final int NIGHT = 1;
    private static final float OBLIQUITY = 0.4092797f;
    private static final long UTC_2000 = 946728000000L;
    private static TwilightCalculator sInstance;
    public int state;
    public long sunrise;
    public long sunset;

    TwilightCalculator() {
    }

    static TwilightCalculator getInstance() {
        if (sInstance == null) {
            sInstance = new TwilightCalculator();
        }
        return sInstance;
    }

    public void calculateTwilight(long time, double latitude, double longitude) {
        float f = ((float) (time - UTC_2000)) / 8.64E7f;
        float f2 = (0.01720197f * f) + 6.24006f;
        double sin = ((double) f2) + (Math.sin((double) f2) * 0.03341960161924362d) + (Math.sin((double) (2.0f * f2)) * 3.4906598739326E-4d) + (Math.sin((double) (3.0f * f2)) * 5.236000106378924E-6d);
        double d = 1.796593063d + sin + 3.141592653589793d;
        double d2 = (-longitude) / 360.0d;
        double d3 = sin;
        double round = ((double) (J0 + ((float) Math.round(((double) (f - J0)) - d2)))) + d2 + (Math.sin((double) f2) * 0.0053d) + (Math.sin(2.0d * d) * -0.0069d);
        double asin = Math.asin(Math.sin(d) * Math.sin(0.4092797040939331d));
        double d4 = 0.01745329238474369d * latitude;
        double sin2 = (Math.sin(-0.10471975803375244d) - (Math.sin(d4) * Math.sin(asin))) / (Math.cos(d4) * Math.cos(asin));
        float f3 = f;
        float f4 = f2;
        if (sin2 >= 1.0d) {
            this.state = 1;
            this.sunset = -1;
            this.sunrise = -1;
        } else if (sin2 <= -1.0d) {
            this.state = 0;
            this.sunset = -1;
            this.sunrise = -1;
        } else {
            float acos = (float) (Math.acos(sin2) / 6.283185307179586d);
            double d5 = asin;
            this.sunset = Math.round((((double) acos) + round) * 8.64E7d) + UTC_2000;
            long round2 = Math.round((round - ((double) acos)) * 8.64E7d) + UTC_2000;
            this.sunrise = round2;
            if (round2 >= time || this.sunset <= time) {
                this.state = 1;
            } else {
                this.state = 0;
            }
        }
    }
}

package androidx.core.location;

import android.location.LocationRequest;
import android.os.Build;
import androidx.core.util.Preconditions;
import androidx.core.util.TimeUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class LocationRequestCompat {
    private static final long IMPLICIT_MIN_UPDATE_INTERVAL = -1;
    public static final long PASSIVE_INTERVAL = Long.MAX_VALUE;
    public static final int QUALITY_BALANCED_POWER_ACCURACY = 102;
    public static final int QUALITY_HIGH_ACCURACY = 100;
    public static final int QUALITY_LOW_POWER = 104;
    final long mDurationMillis;
    final long mIntervalMillis;
    final long mMaxUpdateDelayMillis;
    final int mMaxUpdates;
    final float mMinUpdateDistanceMeters;
    final long mMinUpdateIntervalMillis;
    final int mQuality;

    private static class Api19Impl {
        private static Method sCreateFromDeprecatedProviderMethod;
        private static Class<?> sLocationRequestClass;
        private static Method sSetExpireInMethod;
        private static Method sSetFastestIntervalMethod;
        private static Method sSetNumUpdatesMethod;
        private static Method sSetQualityMethod;

        private Api19Impl() {
        }

        public static Object toLocationRequest(LocationRequestCompat obj, String provider) {
            if (Build.VERSION.SDK_INT >= 19) {
                try {
                    if (sLocationRequestClass == null) {
                        sLocationRequestClass = Class.forName("android.location.LocationRequest");
                    }
                    if (sCreateFromDeprecatedProviderMethod == null) {
                        Method declaredMethod = sLocationRequestClass.getDeclaredMethod("createFromDeprecatedProvider", new Class[]{String.class, Long.TYPE, Float.TYPE, Boolean.TYPE});
                        sCreateFromDeprecatedProviderMethod = declaredMethod;
                        declaredMethod.setAccessible(true);
                    }
                    Object invoke = sCreateFromDeprecatedProviderMethod.invoke((Object) null, new Object[]{provider, Long.valueOf(obj.getIntervalMillis()), Float.valueOf(obj.getMinUpdateDistanceMeters()), false});
                    if (invoke == null) {
                        return null;
                    }
                    if (sSetQualityMethod == null) {
                        Method declaredMethod2 = sLocationRequestClass.getDeclaredMethod("setQuality", new Class[]{Integer.TYPE});
                        sSetQualityMethod = declaredMethod2;
                        declaredMethod2.setAccessible(true);
                    }
                    sSetQualityMethod.invoke(invoke, new Object[]{Integer.valueOf(obj.getQuality())});
                    if (sSetFastestIntervalMethod == null) {
                        Method declaredMethod3 = sLocationRequestClass.getDeclaredMethod("setFastestInterval", new Class[]{Long.TYPE});
                        sSetFastestIntervalMethod = declaredMethod3;
                        declaredMethod3.setAccessible(true);
                    }
                    sSetFastestIntervalMethod.invoke(invoke, new Object[]{Long.valueOf(obj.getMinUpdateIntervalMillis())});
                    if (obj.getMaxUpdates() < Integer.MAX_VALUE) {
                        if (sSetNumUpdatesMethod == null) {
                            Method declaredMethod4 = sLocationRequestClass.getDeclaredMethod("setNumUpdates", new Class[]{Integer.TYPE});
                            sSetNumUpdatesMethod = declaredMethod4;
                            declaredMethod4.setAccessible(true);
                        }
                        sSetNumUpdatesMethod.invoke(invoke, new Object[]{Integer.valueOf(obj.getMaxUpdates())});
                    }
                    if (obj.getDurationMillis() < Long.MAX_VALUE) {
                        if (sSetExpireInMethod == null) {
                            Method declaredMethod5 = sLocationRequestClass.getDeclaredMethod("setExpireIn", new Class[]{Long.TYPE});
                            sSetExpireInMethod = declaredMethod5;
                            declaredMethod5.setAccessible(true);
                        }
                        sSetExpireInMethod.invoke(invoke, new Object[]{Long.valueOf(obj.getDurationMillis())});
                    }
                    return invoke;
                } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                }
            }
            return null;
        }
    }

    private static class Api31Impl {
        private Api31Impl() {
        }

        public static LocationRequest toLocationRequest(LocationRequestCompat obj) {
            return new LocationRequest.Builder(obj.getIntervalMillis()).setQuality(obj.getQuality()).setMinUpdateIntervalMillis(obj.getMinUpdateIntervalMillis()).setDurationMillis(obj.getDurationMillis()).setMaxUpdates(obj.getMaxUpdates()).setMinUpdateDistanceMeters(obj.getMinUpdateDistanceMeters()).setMaxUpdateDelayMillis(obj.getMaxUpdateDelayMillis()).build();
        }
    }

    public static final class Builder {
        private long mDurationMillis;
        private long mIntervalMillis;
        private long mMaxUpdateDelayMillis;
        private int mMaxUpdates;
        private float mMinUpdateDistanceMeters;
        private long mMinUpdateIntervalMillis;
        private int mQuality;

        public Builder(long intervalMillis) {
            setIntervalMillis(intervalMillis);
            this.mQuality = LocationRequestCompat.QUALITY_BALANCED_POWER_ACCURACY;
            this.mDurationMillis = Long.MAX_VALUE;
            this.mMaxUpdates = Integer.MAX_VALUE;
            this.mMinUpdateIntervalMillis = -1;
            this.mMinUpdateDistanceMeters = 0.0f;
            this.mMaxUpdateDelayMillis = 0;
        }

        public Builder(LocationRequestCompat locationRequest) {
            this.mIntervalMillis = locationRequest.mIntervalMillis;
            this.mQuality = locationRequest.mQuality;
            this.mDurationMillis = locationRequest.mDurationMillis;
            this.mMaxUpdates = locationRequest.mMaxUpdates;
            this.mMinUpdateIntervalMillis = locationRequest.mMinUpdateIntervalMillis;
            this.mMinUpdateDistanceMeters = locationRequest.mMinUpdateDistanceMeters;
            this.mMaxUpdateDelayMillis = locationRequest.mMaxUpdateDelayMillis;
        }

        public LocationRequestCompat build() {
            Preconditions.checkState((this.mIntervalMillis == Long.MAX_VALUE && this.mMinUpdateIntervalMillis == -1) ? false : true, "passive location requests must have an explicit minimum update interval");
            long j = this.mIntervalMillis;
            return new LocationRequestCompat(j, this.mQuality, this.mDurationMillis, this.mMaxUpdates, Math.min(this.mMinUpdateIntervalMillis, j), this.mMinUpdateDistanceMeters, this.mMaxUpdateDelayMillis);
        }

        public Builder clearMinUpdateIntervalMillis() {
            this.mMinUpdateIntervalMillis = -1;
            return this;
        }

        public Builder setDurationMillis(long durationMillis) {
            this.mDurationMillis = Preconditions.checkArgumentInRange(durationMillis, 1, Long.MAX_VALUE, "durationMillis");
            return this;
        }

        public Builder setIntervalMillis(long intervalMillis) {
            this.mIntervalMillis = Preconditions.checkArgumentInRange(intervalMillis, 0, Long.MAX_VALUE, "intervalMillis");
            return this;
        }

        public Builder setMaxUpdateDelayMillis(long maxUpdateDelayMillis) {
            this.mMaxUpdateDelayMillis = maxUpdateDelayMillis;
            this.mMaxUpdateDelayMillis = Preconditions.checkArgumentInRange(maxUpdateDelayMillis, 0, Long.MAX_VALUE, "maxUpdateDelayMillis");
            return this;
        }

        public Builder setMaxUpdates(int maxUpdates) {
            this.mMaxUpdates = Preconditions.checkArgumentInRange(maxUpdates, 1, Integer.MAX_VALUE, "maxUpdates");
            return this;
        }

        public Builder setMinUpdateDistanceMeters(float minUpdateDistanceMeters) {
            this.mMinUpdateDistanceMeters = minUpdateDistanceMeters;
            this.mMinUpdateDistanceMeters = Preconditions.checkArgumentInRange(minUpdateDistanceMeters, 0.0f, Float.MAX_VALUE, "minUpdateDistanceMeters");
            return this;
        }

        public Builder setMinUpdateIntervalMillis(long minUpdateIntervalMillis) {
            this.mMinUpdateIntervalMillis = Preconditions.checkArgumentInRange(minUpdateIntervalMillis, 0, Long.MAX_VALUE, "minUpdateIntervalMillis");
            return this;
        }

        public Builder setQuality(int quality) {
            Preconditions.checkArgument(quality == 104 || quality == 102 || quality == 100, "quality must be a defined QUALITY constant, not %d", Integer.valueOf(quality));
            this.mQuality = quality;
            return this;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Quality {
    }

    LocationRequestCompat(long intervalMillis, int quality, long durationMillis, int maxUpdates, long minUpdateIntervalMillis, float minUpdateDistanceMeters, long maxUpdateDelayMillis) {
        this.mIntervalMillis = intervalMillis;
        this.mQuality = quality;
        this.mMinUpdateIntervalMillis = minUpdateIntervalMillis;
        this.mDurationMillis = durationMillis;
        this.mMaxUpdates = maxUpdates;
        this.mMinUpdateDistanceMeters = minUpdateDistanceMeters;
        this.mMaxUpdateDelayMillis = maxUpdateDelayMillis;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationRequestCompat)) {
            return false;
        }
        LocationRequestCompat locationRequestCompat = (LocationRequestCompat) o;
        return this.mQuality == locationRequestCompat.mQuality && this.mIntervalMillis == locationRequestCompat.mIntervalMillis && this.mMinUpdateIntervalMillis == locationRequestCompat.mMinUpdateIntervalMillis && this.mDurationMillis == locationRequestCompat.mDurationMillis && this.mMaxUpdates == locationRequestCompat.mMaxUpdates && Float.compare(locationRequestCompat.mMinUpdateDistanceMeters, this.mMinUpdateDistanceMeters) == 0 && this.mMaxUpdateDelayMillis == locationRequestCompat.mMaxUpdateDelayMillis;
    }

    public long getDurationMillis() {
        return this.mDurationMillis;
    }

    public long getIntervalMillis() {
        return this.mIntervalMillis;
    }

    public long getMaxUpdateDelayMillis() {
        return this.mMaxUpdateDelayMillis;
    }

    public int getMaxUpdates() {
        return this.mMaxUpdates;
    }

    public float getMinUpdateDistanceMeters() {
        return this.mMinUpdateDistanceMeters;
    }

    public long getMinUpdateIntervalMillis() {
        long j = this.mMinUpdateIntervalMillis;
        return j == -1 ? this.mIntervalMillis : j;
    }

    public int getQuality() {
        return this.mQuality;
    }

    public int hashCode() {
        long j = this.mIntervalMillis;
        long j2 = this.mMinUpdateIntervalMillis;
        return (((this.mQuality * 31) + ((int) (j ^ (j >>> 32)))) * 31) + ((int) (j2 ^ (j2 >>> 32)));
    }

    public LocationRequest toLocationRequest() {
        return Api31Impl.toLocationRequest(this);
    }

    public LocationRequest toLocationRequest(String provider) {
        return Build.VERSION.SDK_INT >= 31 ? toLocationRequest() : (LocationRequest) Api19Impl.toLocationRequest(this, provider);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Request[");
        if (this.mIntervalMillis != Long.MAX_VALUE) {
            sb.append("@");
            TimeUtils.formatDuration(this.mIntervalMillis, sb);
            switch (this.mQuality) {
                case 100:
                    sb.append(" HIGH_ACCURACY");
                    break;
                case QUALITY_BALANCED_POWER_ACCURACY /*102*/:
                    sb.append(" BALANCED");
                    break;
                case QUALITY_LOW_POWER /*104*/:
                    sb.append(" LOW_POWER");
                    break;
            }
        } else {
            sb.append("PASSIVE");
        }
        if (this.mDurationMillis != Long.MAX_VALUE) {
            sb.append(", duration=");
            TimeUtils.formatDuration(this.mDurationMillis, sb);
        }
        if (this.mMaxUpdates != Integer.MAX_VALUE) {
            sb.append(", maxUpdates=").append(this.mMaxUpdates);
        }
        long j = this.mMinUpdateIntervalMillis;
        if (j != -1 && j < this.mIntervalMillis) {
            sb.append(", minUpdateInterval=");
            TimeUtils.formatDuration(this.mMinUpdateIntervalMillis, sb);
        }
        if (((double) this.mMinUpdateDistanceMeters) > 0.0d) {
            sb.append(", minUpdateDistance=").append(this.mMinUpdateDistanceMeters);
        }
        if (this.mMaxUpdateDelayMillis / 2 > this.mIntervalMillis) {
            sb.append(", maxUpdateDelay=");
            TimeUtils.formatDuration(this.mMaxUpdateDelayMillis, sb);
        }
        sb.append(']');
        return sb.toString();
    }
}

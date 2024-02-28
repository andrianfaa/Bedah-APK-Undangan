package androidx.core.location;

import android.location.GnssStatus;
import android.os.Build;
import androidx.core.util.Preconditions;

class GnssStatusWrapper extends GnssStatusCompat {
    private final GnssStatus mWrapped;

    static class Api26Impl {
        private Api26Impl() {
        }

        static float getCarrierFrequencyHz(GnssStatus gnssStatus, int satelliteIndex) {
            return gnssStatus.getCarrierFrequencyHz(satelliteIndex);
        }

        static boolean hasCarrierFrequencyHz(GnssStatus gnssStatus, int satelliteIndex) {
            return gnssStatus.hasCarrierFrequencyHz(satelliteIndex);
        }
    }

    static class Api30Impl {
        private Api30Impl() {
        }

        static float getBasebandCn0DbHz(GnssStatus gnssStatus, int satelliteIndex) {
            return gnssStatus.getBasebandCn0DbHz(satelliteIndex);
        }

        static boolean hasBasebandCn0DbHz(GnssStatus gnssStatus, int satelliteIndex) {
            return gnssStatus.hasBasebandCn0DbHz(satelliteIndex);
        }
    }

    GnssStatusWrapper(Object gnssStatus) {
        this.mWrapped = (GnssStatus) Preconditions.checkNotNull((GnssStatus) gnssStatus);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GnssStatusWrapper)) {
            return false;
        }
        return this.mWrapped.equals(((GnssStatusWrapper) o).mWrapped);
    }

    public float getAzimuthDegrees(int satelliteIndex) {
        return this.mWrapped.getAzimuthDegrees(satelliteIndex);
    }

    public float getBasebandCn0DbHz(int satelliteIndex) {
        if (Build.VERSION.SDK_INT >= 30) {
            return Api30Impl.getBasebandCn0DbHz(this.mWrapped, satelliteIndex);
        }
        throw new UnsupportedOperationException();
    }

    public float getCarrierFrequencyHz(int satelliteIndex) {
        if (Build.VERSION.SDK_INT >= 26) {
            return Api26Impl.getCarrierFrequencyHz(this.mWrapped, satelliteIndex);
        }
        throw new UnsupportedOperationException();
    }

    public float getCn0DbHz(int satelliteIndex) {
        return this.mWrapped.getCn0DbHz(satelliteIndex);
    }

    public int getConstellationType(int satelliteIndex) {
        return this.mWrapped.getConstellationType(satelliteIndex);
    }

    public float getElevationDegrees(int satelliteIndex) {
        return this.mWrapped.getElevationDegrees(satelliteIndex);
    }

    public int getSatelliteCount() {
        return this.mWrapped.getSatelliteCount();
    }

    public int getSvid(int satelliteIndex) {
        return this.mWrapped.getSvid(satelliteIndex);
    }

    public boolean hasAlmanacData(int satelliteIndex) {
        return this.mWrapped.hasAlmanacData(satelliteIndex);
    }

    public boolean hasBasebandCn0DbHz(int satelliteIndex) {
        if (Build.VERSION.SDK_INT >= 30) {
            return Api30Impl.hasBasebandCn0DbHz(this.mWrapped, satelliteIndex);
        }
        return false;
    }

    public boolean hasCarrierFrequencyHz(int satelliteIndex) {
        if (Build.VERSION.SDK_INT >= 26) {
            return Api26Impl.hasCarrierFrequencyHz(this.mWrapped, satelliteIndex);
        }
        return false;
    }

    public boolean hasEphemerisData(int satelliteIndex) {
        return this.mWrapped.hasEphemerisData(satelliteIndex);
    }

    public int hashCode() {
        return this.mWrapped.hashCode();
    }

    public boolean usedInFix(int satelliteIndex) {
        return this.mWrapped.usedInFix(satelliteIndex);
    }
}

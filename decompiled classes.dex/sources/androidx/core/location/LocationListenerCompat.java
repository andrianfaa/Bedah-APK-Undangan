package androidx.core.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import java.util.List;

public interface LocationListenerCompat extends LocationListener {
    void onFlushComplete(int requestCode) {
    }

    void onLocationChanged(List<Location> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            onLocationChanged(list.get(i));
        }
    }

    void onProviderDisabled(String provider) {
    }

    void onProviderEnabled(String provider) {
    }

    void onStatusChanged(String provider, int status, Bundle extras) {
    }
}

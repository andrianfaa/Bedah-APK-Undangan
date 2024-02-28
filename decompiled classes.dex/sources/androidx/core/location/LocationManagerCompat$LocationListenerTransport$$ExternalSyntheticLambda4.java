package androidx.core.location;

import android.location.Location;
import androidx.core.location.LocationManagerCompat;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ LocationManagerCompat.LocationListenerTransport f$0;
    public final /* synthetic */ Location f$1;

    public /* synthetic */ LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda4(LocationManagerCompat.LocationListenerTransport locationListenerTransport, Location location) {
        this.f$0 = locationListenerTransport;
        this.f$1 = location;
    }

    public final void run() {
        this.f$0.m14lambda$onLocationChanged$0$androidxcorelocationLocationManagerCompat$LocationListenerTransport(this.f$1);
    }
}

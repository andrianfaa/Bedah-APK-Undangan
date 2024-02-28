package androidx.core.location;

import androidx.core.location.LocationManagerCompat;
import java.util.concurrent.Executor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LocationManagerCompat$GpsStatusTransport$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ LocationManagerCompat.GpsStatusTransport f$0;
    public final /* synthetic */ Executor f$1;
    public final /* synthetic */ GnssStatusCompat f$2;

    public /* synthetic */ LocationManagerCompat$GpsStatusTransport$$ExternalSyntheticLambda3(LocationManagerCompat.GpsStatusTransport gpsStatusTransport, Executor executor, GnssStatusCompat gnssStatusCompat) {
        this.f$0 = gpsStatusTransport;
        this.f$1 = executor;
        this.f$2 = gnssStatusCompat;
    }

    public final void run() {
        this.f$0.m12lambda$onGpsStatusChanged$3$androidxcorelocationLocationManagerCompat$GpsStatusTransport(this.f$1, this.f$2);
    }
}

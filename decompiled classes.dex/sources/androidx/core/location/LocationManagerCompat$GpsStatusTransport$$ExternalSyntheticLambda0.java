package androidx.core.location;

import androidx.core.location.LocationManagerCompat;
import java.util.concurrent.Executor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LocationManagerCompat$GpsStatusTransport$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ LocationManagerCompat.GpsStatusTransport f$0;
    public final /* synthetic */ Executor f$1;

    public /* synthetic */ LocationManagerCompat$GpsStatusTransport$$ExternalSyntheticLambda0(LocationManagerCompat.GpsStatusTransport gpsStatusTransport, Executor executor) {
        this.f$0 = gpsStatusTransport;
        this.f$1 = executor;
    }

    public final void run() {
        this.f$0.m9lambda$onGpsStatusChanged$0$androidxcorelocationLocationManagerCompat$GpsStatusTransport(this.f$1);
    }
}

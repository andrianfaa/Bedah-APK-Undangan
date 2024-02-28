package androidx.core.location;

import androidx.core.location.LocationManagerCompat;
import java.util.concurrent.Executor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LocationManagerCompat$GpsStatusTransport$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ LocationManagerCompat.GpsStatusTransport f$0;
    public final /* synthetic */ Executor f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ LocationManagerCompat$GpsStatusTransport$$ExternalSyntheticLambda2(LocationManagerCompat.GpsStatusTransport gpsStatusTransport, Executor executor, int i) {
        this.f$0 = gpsStatusTransport;
        this.f$1 = executor;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.m11lambda$onGpsStatusChanged$2$androidxcorelocationLocationManagerCompat$GpsStatusTransport(this.f$1, this.f$2);
    }
}

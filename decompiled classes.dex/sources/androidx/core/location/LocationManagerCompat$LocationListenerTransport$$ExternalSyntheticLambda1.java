package androidx.core.location;

import androidx.core.location.LocationManagerCompat;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ LocationManagerCompat.LocationListenerTransport f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda1(LocationManagerCompat.LocationListenerTransport locationListenerTransport, int i) {
        this.f$0 = locationListenerTransport;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.m13lambda$onFlushComplete$2$androidxcorelocationLocationManagerCompat$LocationListenerTransport(this.f$1);
    }
}

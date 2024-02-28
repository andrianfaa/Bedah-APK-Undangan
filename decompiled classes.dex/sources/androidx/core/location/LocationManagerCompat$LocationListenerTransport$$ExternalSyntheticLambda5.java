package androidx.core.location;

import android.os.Bundle;
import androidx.core.location.LocationManagerCompat;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ LocationManagerCompat.LocationListenerTransport f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ Bundle f$3;

    public /* synthetic */ LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda5(LocationManagerCompat.LocationListenerTransport locationListenerTransport, String str, int i, Bundle bundle) {
        this.f$0 = locationListenerTransport;
        this.f$1 = str;
        this.f$2 = i;
        this.f$3 = bundle;
    }

    public final void run() {
        this.f$0.m18lambda$onStatusChanged$3$androidxcorelocationLocationManagerCompat$LocationListenerTransport(this.f$1, this.f$2, this.f$3);
    }
}

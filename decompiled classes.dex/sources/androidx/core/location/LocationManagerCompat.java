package androidx.core.location;

import android.content.Context;
import android.location.GnssStatus;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.collection.SimpleArrayMap;
import androidx.core.location.GnssStatusCompat;
import androidx.core.os.CancellationSignal;
import androidx.core.os.ExecutorCompat;
import androidx.core.util.Consumer;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import mt.Log1F380D;

/* compiled from: 0045 */
public final class LocationManagerCompat {
    private static final long GET_CURRENT_LOCATION_TIMEOUT_MS = 30000;
    private static final long MAX_CURRENT_LOCATION_AGE_MS = 10000;
    private static final long PRE_N_LOOPER_TIMEOUT_S = 5;
    private static Field sContextField;
    static final WeakHashMap<LocationListenerKey, WeakReference<LocationListenerTransport>> sLocationListeners = new WeakHashMap<>();

    static class Api19Impl {
        private static Class<?> sLocationRequestClass;
        private static Method sRequestLocationUpdatesLooperMethod;

        private Api19Impl() {
        }

        static boolean tryRequestLocationUpdates(LocationManager locationManager, String provider, LocationRequestCompat locationRequest, LocationListenerCompat listener, Looper looper) {
            if (Build.VERSION.SDK_INT >= 19) {
                try {
                    if (sLocationRequestClass == null) {
                        sLocationRequestClass = Class.forName("android.location.LocationRequest");
                    }
                    if (sRequestLocationUpdatesLooperMethod == null) {
                        Method declaredMethod = LocationManager.class.getDeclaredMethod("requestLocationUpdates", new Class[]{sLocationRequestClass, LocationListener.class, Looper.class});
                        sRequestLocationUpdatesLooperMethod = declaredMethod;
                        declaredMethod.setAccessible(true);
                    }
                    LocationRequest locationRequest2 = locationRequest.toLocationRequest(provider);
                    if (locationRequest2 != null) {
                        sRequestLocationUpdatesLooperMethod.invoke(locationManager, new Object[]{locationRequest2, listener, looper});
                        return true;
                    }
                } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | UnsupportedOperationException | InvocationTargetException e) {
                }
            }
            return false;
        }

        static boolean tryRequestLocationUpdates(LocationManager locationManager, String provider, LocationRequestCompat locationRequest, LocationListenerTransport transport) {
            if (Build.VERSION.SDK_INT >= 19) {
                try {
                    if (sLocationRequestClass == null) {
                        sLocationRequestClass = Class.forName("android.location.LocationRequest");
                    }
                    if (sRequestLocationUpdatesLooperMethod == null) {
                        Method declaredMethod = LocationManager.class.getDeclaredMethod("requestLocationUpdates", new Class[]{sLocationRequestClass, LocationListener.class, Looper.class});
                        sRequestLocationUpdatesLooperMethod = declaredMethod;
                        declaredMethod.setAccessible(true);
                    }
                    LocationRequest locationRequest2 = locationRequest.toLocationRequest(provider);
                    if (locationRequest2 != null) {
                        synchronized (LocationManagerCompat.sLocationListeners) {
                            sRequestLocationUpdatesLooperMethod.invoke(locationManager, new Object[]{locationRequest2, transport, Looper.getMainLooper()});
                            LocationManagerCompat.registerLocationListenerTransport(locationManager, transport);
                        }
                        return true;
                    }
                } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | UnsupportedOperationException | InvocationTargetException e) {
                }
            }
            return false;
        }
    }

    static class Api24Impl {
        private Api24Impl() {
        }

        static boolean registerGnssStatusCallback(LocationManager locationManager, Handler baseHandler, Executor executor, GnssStatusCompat.Callback callback) {
            Preconditions.checkArgument(baseHandler != null);
            synchronized (GnssLazyLoader.sGnssStatusListeners) {
                PreRGnssStatusTransport preRGnssStatusTransport = (PreRGnssStatusTransport) GnssLazyLoader.sGnssStatusListeners.get(callback);
                if (preRGnssStatusTransport == null) {
                    preRGnssStatusTransport = new PreRGnssStatusTransport(callback);
                } else {
                    preRGnssStatusTransport.unregister();
                }
                preRGnssStatusTransport.register(executor);
                if (!locationManager.registerGnssStatusCallback(preRGnssStatusTransport, baseHandler)) {
                    return false;
                }
                GnssLazyLoader.sGnssStatusListeners.put(callback, preRGnssStatusTransport);
                return true;
            }
        }

        static void unregisterGnssStatusCallback(LocationManager locationManager, Object callback) {
            if (callback instanceof PreRGnssStatusTransport) {
                ((PreRGnssStatusTransport) callback).unregister();
            }
            locationManager.unregisterGnssStatusCallback((GnssStatus.Callback) callback);
        }
    }

    private static class Api28Impl {
        private Api28Impl() {
        }

        static String getGnssHardwareModelName(LocationManager locationManager) {
            return locationManager.getGnssHardwareModelName();
        }

        static int getGnssYearOfHardware(LocationManager locationManager) {
            return locationManager.getGnssYearOfHardware();
        }

        static boolean isLocationEnabled(LocationManager locationManager) {
            return locationManager.isLocationEnabled();
        }
    }

    private static class Api30Impl {
        private static Class<?> sLocationRequestClass;
        private static Method sRequestLocationUpdatesExecutorMethod;

        private Api30Impl() {
        }

        static void getCurrentLocation(LocationManager locationManager, String provider, CancellationSignal cancellationSignal, Executor executor, Consumer<Location> consumer) {
            android.os.CancellationSignal cancellationSignal2 = cancellationSignal != null ? (android.os.CancellationSignal) cancellationSignal.getCancellationSignalObject() : null;
            Objects.requireNonNull(consumer);
            locationManager.getCurrentLocation(provider, cancellationSignal2, executor, new LocationManagerCompat$Api30Impl$$ExternalSyntheticLambda0(consumer));
        }

        public static boolean registerGnssStatusCallback(LocationManager locationManager, Handler baseHandler, Executor executor, GnssStatusCompat.Callback callback) {
            synchronized (GnssLazyLoader.sGnssStatusListeners) {
                GnssStatusTransport gnssStatusTransport = (GnssStatusTransport) GnssLazyLoader.sGnssStatusListeners.get(callback);
                if (gnssStatusTransport == null) {
                    gnssStatusTransport = new GnssStatusTransport(callback);
                }
                if (!locationManager.registerGnssStatusCallback(executor, gnssStatusTransport)) {
                    return false;
                }
                GnssLazyLoader.sGnssStatusListeners.put(callback, gnssStatusTransport);
                return true;
            }
        }

        public static boolean tryRequestLocationUpdates(LocationManager locationManager, String provider, LocationRequestCompat locationRequest, Executor executor, LocationListenerCompat listener) {
            if (Build.VERSION.SDK_INT >= 30) {
                try {
                    if (sLocationRequestClass == null) {
                        sLocationRequestClass = Class.forName("android.location.LocationRequest");
                    }
                    if (sRequestLocationUpdatesExecutorMethod == null) {
                        Method declaredMethod = LocationManager.class.getDeclaredMethod("requestLocationUpdates", new Class[]{sLocationRequestClass, Executor.class, LocationListener.class});
                        sRequestLocationUpdatesExecutorMethod = declaredMethod;
                        declaredMethod.setAccessible(true);
                    }
                    LocationRequest locationRequest2 = locationRequest.toLocationRequest(provider);
                    if (locationRequest2 != null) {
                        sRequestLocationUpdatesExecutorMethod.invoke(locationManager, new Object[]{locationRequest2, executor, listener});
                        return true;
                    }
                } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | UnsupportedOperationException | InvocationTargetException e) {
                }
            }
            return false;
        }
    }

    private static class Api31Impl {
        private Api31Impl() {
        }

        static boolean hasProvider(LocationManager locationManager, String provider) {
            return locationManager.hasProvider(provider);
        }

        static void requestLocationUpdates(LocationManager locationManager, String provider, LocationRequest locationRequest, Executor executor, LocationListener listener) {
            locationManager.requestLocationUpdates(provider, locationRequest, executor, listener);
        }
    }

    private static final class CancellableLocationListener implements LocationListener {
        private Consumer<Location> mConsumer;
        private final Executor mExecutor;
        private final LocationManager mLocationManager;
        private final Handler mTimeoutHandler = new Handler(Looper.getMainLooper());
        Runnable mTimeoutRunnable;
        private boolean mTriggered;

        CancellableLocationListener(LocationManager locationManager, Executor executor, Consumer<Location> consumer) {
            this.mLocationManager = locationManager;
            this.mExecutor = executor;
            this.mConsumer = consumer;
        }

        private void cleanup() {
            this.mConsumer = null;
            this.mLocationManager.removeUpdates(this);
            Runnable runnable = this.mTimeoutRunnable;
            if (runnable != null) {
                this.mTimeoutHandler.removeCallbacks(runnable);
                this.mTimeoutRunnable = null;
            }
        }

        public void cancel() {
            synchronized (this) {
                if (!this.mTriggered) {
                    this.mTriggered = true;
                    cleanup();
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$startTimeout$0$androidx-core-location-LocationManagerCompat$CancellableLocationListener  reason: not valid java name */
        public /* synthetic */ void m8lambda$startTimeout$0$androidxcorelocationLocationManagerCompat$CancellableLocationListener() {
            this.mTimeoutRunnable = null;
            Location location = null;
            onLocationChanged((Location) null);
        }

        public void onLocationChanged(Location location) {
            synchronized (this) {
                if (!this.mTriggered) {
                    this.mTriggered = true;
                    this.mExecutor.execute(new LocationManagerCompat$CancellableLocationListener$$ExternalSyntheticLambda1(this.mConsumer, location));
                    cleanup();
                }
            }
        }

        public void onProviderDisabled(String p) {
            Location location = null;
            onLocationChanged((Location) null);
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void startTimeout(long timeoutMs) {
            synchronized (this) {
                if (!this.mTriggered) {
                    LocationManagerCompat$CancellableLocationListener$$ExternalSyntheticLambda0 locationManagerCompat$CancellableLocationListener$$ExternalSyntheticLambda0 = new LocationManagerCompat$CancellableLocationListener$$ExternalSyntheticLambda0(this);
                    this.mTimeoutRunnable = locationManagerCompat$CancellableLocationListener$$ExternalSyntheticLambda0;
                    this.mTimeoutHandler.postDelayed(locationManagerCompat$CancellableLocationListener$$ExternalSyntheticLambda0, timeoutMs);
                }
            }
        }
    }

    private static class GnssLazyLoader {
        static final SimpleArrayMap<Object, Object> sGnssStatusListeners = new SimpleArrayMap<>();

        private GnssLazyLoader() {
        }
    }

    private static class GnssStatusTransport extends GnssStatus.Callback {
        final GnssStatusCompat.Callback mCallback;

        GnssStatusTransport(GnssStatusCompat.Callback callback) {
            Preconditions.checkArgument(callback != null, "invalid null callback");
            this.mCallback = callback;
        }

        public void onFirstFix(int ttffMillis) {
            this.mCallback.onFirstFix(ttffMillis);
        }

        public void onSatelliteStatusChanged(GnssStatus status) {
            this.mCallback.onSatelliteStatusChanged(GnssStatusCompat.wrap(status));
        }

        public void onStarted() {
            this.mCallback.onStarted();
        }

        public void onStopped() {
            this.mCallback.onStopped();
        }
    }

    private static class GpsStatusTransport implements GpsStatus.Listener {
        final GnssStatusCompat.Callback mCallback;
        volatile Executor mExecutor;
        private final LocationManager mLocationManager;

        GpsStatusTransport(LocationManager locationManager, GnssStatusCompat.Callback callback) {
            Preconditions.checkArgument(callback != null, "invalid null callback");
            this.mLocationManager = locationManager;
            this.mCallback = callback;
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onGpsStatusChanged$0$androidx-core-location-LocationManagerCompat$GpsStatusTransport  reason: not valid java name */
        public /* synthetic */ void m9lambda$onGpsStatusChanged$0$androidxcorelocationLocationManagerCompat$GpsStatusTransport(Executor executor) {
            if (this.mExecutor == executor) {
                this.mCallback.onStarted();
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onGpsStatusChanged$1$androidx-core-location-LocationManagerCompat$GpsStatusTransport  reason: not valid java name */
        public /* synthetic */ void m10lambda$onGpsStatusChanged$1$androidxcorelocationLocationManagerCompat$GpsStatusTransport(Executor executor) {
            if (this.mExecutor == executor) {
                this.mCallback.onStopped();
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onGpsStatusChanged$2$androidx-core-location-LocationManagerCompat$GpsStatusTransport  reason: not valid java name */
        public /* synthetic */ void m11lambda$onGpsStatusChanged$2$androidxcorelocationLocationManagerCompat$GpsStatusTransport(Executor executor, int ttff) {
            if (this.mExecutor == executor) {
                this.mCallback.onFirstFix(ttff);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onGpsStatusChanged$3$androidx-core-location-LocationManagerCompat$GpsStatusTransport  reason: not valid java name */
        public /* synthetic */ void m12lambda$onGpsStatusChanged$3$androidxcorelocationLocationManagerCompat$GpsStatusTransport(Executor executor, GnssStatusCompat gnssStatus) {
            if (this.mExecutor == executor) {
                this.mCallback.onSatelliteStatusChanged(gnssStatus);
            }
        }

        public void onGpsStatusChanged(int event) {
            Executor executor = this.mExecutor;
            if (executor != null) {
                switch (event) {
                    case 1:
                        executor.execute(new LocationManagerCompat$GpsStatusTransport$$ExternalSyntheticLambda0(this, executor));
                        return;
                    case 2:
                        executor.execute(new LocationManagerCompat$GpsStatusTransport$$ExternalSyntheticLambda1(this, executor));
                        return;
                    case 3:
                        GpsStatus gpsStatus = this.mLocationManager.getGpsStatus((GpsStatus) null);
                        if (gpsStatus != null) {
                            executor.execute(new LocationManagerCompat$GpsStatusTransport$$ExternalSyntheticLambda2(this, executor, gpsStatus.getTimeToFirstFix()));
                            return;
                        }
                        return;
                    case 4:
                        GpsStatus gpsStatus2 = this.mLocationManager.getGpsStatus((GpsStatus) null);
                        if (gpsStatus2 != null) {
                            executor.execute(new LocationManagerCompat$GpsStatusTransport$$ExternalSyntheticLambda3(this, executor, GnssStatusCompat.wrap(gpsStatus2)));
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }

        public void register(Executor executor) {
            Preconditions.checkState(this.mExecutor == null);
            this.mExecutor = executor;
        }

        public void unregister() {
            this.mExecutor = null;
        }
    }

    private static final class InlineHandlerExecutor implements Executor {
        private final Handler mHandler;

        InlineHandlerExecutor(Handler handler) {
            this.mHandler = (Handler) Preconditions.checkNotNull(handler);
        }

        public void execute(Runnable command) {
            if (Looper.myLooper() == this.mHandler.getLooper()) {
                command.run();
            } else if (!this.mHandler.post((Runnable) Preconditions.checkNotNull(command))) {
                throw new RejectedExecutionException(this.mHandler + " is shutting down");
            }
        }
    }

    private static class LocationListenerKey {
        final LocationListenerCompat mListener;
        final String mProvider;

        LocationListenerKey(String provider, LocationListenerCompat listener) {
            this.mProvider = (String) ObjectsCompat.requireNonNull(provider, "invalid null provider");
            this.mListener = (LocationListenerCompat) ObjectsCompat.requireNonNull(listener, "invalid null listener");
        }

        public boolean equals(Object o) {
            if (!(o instanceof LocationListenerKey)) {
                return false;
            }
            LocationListenerKey locationListenerKey = (LocationListenerKey) o;
            return this.mProvider.equals(locationListenerKey.mProvider) && this.mListener.equals(locationListenerKey.mListener);
        }

        public int hashCode() {
            return ObjectsCompat.hash(this.mProvider, this.mListener);
        }
    }

    private static class LocationListenerTransport implements LocationListener {
        final Executor mExecutor;
        volatile LocationListenerKey mKey;

        LocationListenerTransport(LocationListenerKey key, Executor executor) {
            this.mKey = key;
            this.mExecutor = executor;
        }

        public LocationListenerKey getKey() {
            return (LocationListenerKey) ObjectsCompat.requireNonNull(this.mKey);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onFlushComplete$2$androidx-core-location-LocationManagerCompat$LocationListenerTransport  reason: not valid java name */
        public /* synthetic */ void m13lambda$onFlushComplete$2$androidxcorelocationLocationManagerCompat$LocationListenerTransport(int requestCode) {
            LocationListenerKey locationListenerKey = this.mKey;
            if (locationListenerKey != null) {
                locationListenerKey.mListener.onFlushComplete(requestCode);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onLocationChanged$0$androidx-core-location-LocationManagerCompat$LocationListenerTransport  reason: not valid java name */
        public /* synthetic */ void m14lambda$onLocationChanged$0$androidxcorelocationLocationManagerCompat$LocationListenerTransport(Location location) {
            LocationListenerKey locationListenerKey = this.mKey;
            if (locationListenerKey != null) {
                locationListenerKey.mListener.onLocationChanged(location);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onLocationChanged$1$androidx-core-location-LocationManagerCompat$LocationListenerTransport  reason: not valid java name */
        public /* synthetic */ void m15lambda$onLocationChanged$1$androidxcorelocationLocationManagerCompat$LocationListenerTransport(List locations) {
            LocationListenerKey locationListenerKey = this.mKey;
            if (locationListenerKey != null) {
                locationListenerKey.mListener.onLocationChanged(locations);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onProviderDisabled$5$androidx-core-location-LocationManagerCompat$LocationListenerTransport  reason: not valid java name */
        public /* synthetic */ void m16lambda$onProviderDisabled$5$androidxcorelocationLocationManagerCompat$LocationListenerTransport(String provider) {
            LocationListenerKey locationListenerKey = this.mKey;
            if (locationListenerKey != null) {
                locationListenerKey.mListener.onProviderDisabled(provider);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onProviderEnabled$4$androidx-core-location-LocationManagerCompat$LocationListenerTransport  reason: not valid java name */
        public /* synthetic */ void m17lambda$onProviderEnabled$4$androidxcorelocationLocationManagerCompat$LocationListenerTransport(String provider) {
            LocationListenerKey locationListenerKey = this.mKey;
            if (locationListenerKey != null) {
                locationListenerKey.mListener.onProviderEnabled(provider);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onStatusChanged$3$androidx-core-location-LocationManagerCompat$LocationListenerTransport  reason: not valid java name */
        public /* synthetic */ void m18lambda$onStatusChanged$3$androidxcorelocationLocationManagerCompat$LocationListenerTransport(String provider, int status, Bundle extras) {
            LocationListenerKey locationListenerKey = this.mKey;
            if (locationListenerKey != null) {
                locationListenerKey.mListener.onStatusChanged(provider, status, extras);
            }
        }

        public void onFlushComplete(int requestCode) {
            if (this.mKey != null) {
                this.mExecutor.execute(new LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda1(this, requestCode));
            }
        }

        public void onLocationChanged(Location location) {
            if (this.mKey != null) {
                this.mExecutor.execute(new LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda4(this, location));
            }
        }

        public void onLocationChanged(List<Location> list) {
            if (this.mKey != null) {
                this.mExecutor.execute(new LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda2(this, list));
            }
        }

        public void onProviderDisabled(String provider) {
            if (this.mKey != null) {
                this.mExecutor.execute(new LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda3(this, provider));
            }
        }

        public void onProviderEnabled(String provider) {
            if (this.mKey != null) {
                this.mExecutor.execute(new LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda0(this, provider));
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (this.mKey != null) {
                this.mExecutor.execute(new LocationManagerCompat$LocationListenerTransport$$ExternalSyntheticLambda5(this, provider, status, extras));
            }
        }

        public void unregister() {
            this.mKey = null;
        }
    }

    private static class PreRGnssStatusTransport extends GnssStatus.Callback {
        final GnssStatusCompat.Callback mCallback;
        volatile Executor mExecutor;

        PreRGnssStatusTransport(GnssStatusCompat.Callback callback) {
            Preconditions.checkArgument(callback != null, "invalid null callback");
            this.mCallback = callback;
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onFirstFix$2$androidx-core-location-LocationManagerCompat$PreRGnssStatusTransport  reason: not valid java name */
        public /* synthetic */ void m19lambda$onFirstFix$2$androidxcorelocationLocationManagerCompat$PreRGnssStatusTransport(Executor executor, int ttffMillis) {
            if (this.mExecutor == executor) {
                this.mCallback.onFirstFix(ttffMillis);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onSatelliteStatusChanged$3$androidx-core-location-LocationManagerCompat$PreRGnssStatusTransport  reason: not valid java name */
        public /* synthetic */ void m20lambda$onSatelliteStatusChanged$3$androidxcorelocationLocationManagerCompat$PreRGnssStatusTransport(Executor executor, GnssStatus status) {
            if (this.mExecutor == executor) {
                this.mCallback.onSatelliteStatusChanged(GnssStatusCompat.wrap(status));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onStarted$0$androidx-core-location-LocationManagerCompat$PreRGnssStatusTransport  reason: not valid java name */
        public /* synthetic */ void m21lambda$onStarted$0$androidxcorelocationLocationManagerCompat$PreRGnssStatusTransport(Executor executor) {
            if (this.mExecutor == executor) {
                this.mCallback.onStarted();
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onStopped$1$androidx-core-location-LocationManagerCompat$PreRGnssStatusTransport  reason: not valid java name */
        public /* synthetic */ void m22lambda$onStopped$1$androidxcorelocationLocationManagerCompat$PreRGnssStatusTransport(Executor executor) {
            if (this.mExecutor == executor) {
                this.mCallback.onStopped();
            }
        }

        public void onFirstFix(int ttffMillis) {
            Executor executor = this.mExecutor;
            if (executor != null) {
                executor.execute(new LocationManagerCompat$PreRGnssStatusTransport$$ExternalSyntheticLambda2(this, executor, ttffMillis));
            }
        }

        public void onSatelliteStatusChanged(GnssStatus status) {
            Executor executor = this.mExecutor;
            if (executor != null) {
                executor.execute(new LocationManagerCompat$PreRGnssStatusTransport$$ExternalSyntheticLambda1(this, executor, status));
            }
        }

        public void onStarted() {
            Executor executor = this.mExecutor;
            if (executor != null) {
                executor.execute(new LocationManagerCompat$PreRGnssStatusTransport$$ExternalSyntheticLambda0(this, executor));
            }
        }

        public void onStopped() {
            Executor executor = this.mExecutor;
            if (executor != null) {
                executor.execute(new LocationManagerCompat$PreRGnssStatusTransport$$ExternalSyntheticLambda3(this, executor));
            }
        }

        public void register(Executor executor) {
            boolean z = true;
            Preconditions.checkArgument(executor != null, "invalid null executor");
            if (this.mExecutor != null) {
                z = false;
            }
            Preconditions.checkState(z);
            this.mExecutor = executor;
        }

        public void unregister() {
            this.mExecutor = null;
        }
    }

    private LocationManagerCompat() {
    }

    public static void getCurrentLocation(LocationManager locationManager, String provider, CancellationSignal cancellationSignal, Executor executor, Consumer<Location> consumer) {
        if (Build.VERSION.SDK_INT >= 30) {
            Api30Impl.getCurrentLocation(locationManager, provider, cancellationSignal, executor, consumer);
            return;
        }
        if (cancellationSignal != null) {
            cancellationSignal.throwIfCanceled();
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
        if (lastKnownLocation == null || SystemClock.elapsedRealtime() - LocationCompat.getElapsedRealtimeMillis(lastKnownLocation) >= 10000) {
            CancellableLocationListener cancellableLocationListener = new CancellableLocationListener(locationManager, executor, consumer);
            locationManager.requestLocationUpdates(provider, 0, 0.0f, cancellableLocationListener, Looper.getMainLooper());
            if (cancellationSignal != null) {
                Objects.requireNonNull(cancellableLocationListener);
                cancellationSignal.setOnCancelListener(new LocationManagerCompat$$ExternalSyntheticLambda2(cancellableLocationListener));
            }
            cancellableLocationListener.startTimeout(30000);
            return;
        }
        executor.execute(new LocationManagerCompat$$ExternalSyntheticLambda1(consumer, lastKnownLocation));
    }

    public static String getGnssHardwareModelName(LocationManager locationManager) {
        if (Build.VERSION.SDK_INT < 28) {
            return null;
        }
        String gnssHardwareModelName = Api28Impl.getGnssHardwareModelName(locationManager);
        Log1F380D.a((Object) gnssHardwareModelName);
        return gnssHardwareModelName;
    }

    public static int getGnssYearOfHardware(LocationManager locationManager) {
        if (Build.VERSION.SDK_INT >= 28) {
            return Api28Impl.getGnssYearOfHardware(locationManager);
        }
        return 0;
    }

    public static boolean hasProvider(LocationManager locationManager, String provider) {
        if (Build.VERSION.SDK_INT >= 31) {
            return Api31Impl.hasProvider(locationManager, provider);
        }
        if (locationManager.getAllProviders().contains(provider)) {
            return true;
        }
        try {
            return locationManager.getProvider(provider) != null;
        } catch (SecurityException e) {
            return false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:39:0x008f, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x009b, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean registerGnssStatusCallback(android.location.LocationManager r18, android.os.Handler r19, java.util.concurrent.Executor r20, androidx.core.location.GnssStatusCompat.Callback r21) {
        /*
            r1 = r18
            r2 = r19
            r3 = r21
            int r0 = android.os.Build.VERSION.SDK_INT
            r4 = 30
            if (r0 < r4) goto L_0x0011
            boolean r0 = androidx.core.location.LocationManagerCompat.Api30Impl.registerGnssStatusCallback(r18, r19, r20, r21)
            return r0
        L_0x0011:
            int r0 = android.os.Build.VERSION.SDK_INT
            r4 = 24
            if (r0 < r4) goto L_0x001c
            boolean r0 = androidx.core.location.LocationManagerCompat.Api24Impl.registerGnssStatusCallback(r18, r19, r20, r21)
            return r0
        L_0x001c:
            r4 = 1
            r5 = 0
            if (r2 == 0) goto L_0x0022
            r0 = r4
            goto L_0x0023
        L_0x0022:
            r0 = r5
        L_0x0023:
            androidx.core.util.Preconditions.checkArgument(r0)
            androidx.collection.SimpleArrayMap<java.lang.Object, java.lang.Object> r6 = androidx.core.location.LocationManagerCompat.GnssLazyLoader.sGnssStatusListeners
            monitor-enter(r6)
            androidx.collection.SimpleArrayMap<java.lang.Object, java.lang.Object> r0 = androidx.core.location.LocationManagerCompat.GnssLazyLoader.sGnssStatusListeners     // Catch:{ all -> 0x010a }
            java.lang.Object r0 = r0.get(r3)     // Catch:{ all -> 0x010a }
            androidx.core.location.LocationManagerCompat$GpsStatusTransport r0 = (androidx.core.location.LocationManagerCompat.GpsStatusTransport) r0     // Catch:{ all -> 0x010a }
            if (r0 != 0) goto L_0x003a
            androidx.core.location.LocationManagerCompat$GpsStatusTransport r7 = new androidx.core.location.LocationManagerCompat$GpsStatusTransport     // Catch:{ all -> 0x010a }
            r7.<init>(r1, r3)     // Catch:{ all -> 0x010a }
            r0 = r7
            goto L_0x003e
        L_0x003a:
            r0.unregister()     // Catch:{ all -> 0x010a }
            r7 = r0
        L_0x003e:
            r8 = r20
            r7.register(r8)     // Catch:{ all -> 0x010f }
            r9 = r7
            java.util.concurrent.FutureTask r0 = new java.util.concurrent.FutureTask     // Catch:{ all -> 0x010f }
            androidx.core.location.LocationManagerCompat$$ExternalSyntheticLambda0 r10 = new androidx.core.location.LocationManagerCompat$$ExternalSyntheticLambda0     // Catch:{ all -> 0x010f }
            r10.<init>(r1, r9)     // Catch:{ all -> 0x010f }
            r0.<init>(r10)     // Catch:{ all -> 0x010f }
            r10 = r0
            android.os.Looper r0 = android.os.Looper.myLooper()     // Catch:{ all -> 0x010f }
            android.os.Looper r11 = r19.getLooper()     // Catch:{ all -> 0x010f }
            if (r0 != r11) goto L_0x005d
            r10.run()     // Catch:{ all -> 0x010f }
            goto L_0x0063
        L_0x005d:
            boolean r0 = r2.post(r10)     // Catch:{ all -> 0x010f }
            if (r0 == 0) goto L_0x00f1
        L_0x0063:
            r11 = 0
            java.util.concurrent.TimeUnit r0 = java.util.concurrent.TimeUnit.SECONDS     // Catch:{ ExecutionException -> 0x00c1, TimeoutException -> 0x00a7 }
            r12 = 5
            long r12 = r0.toNanos(r12)     // Catch:{ ExecutionException -> 0x00c1, TimeoutException -> 0x00a7 }
            long r14 = java.lang.System.nanoTime()     // Catch:{ ExecutionException -> 0x00c1, TimeoutException -> 0x00a7 }
            long r14 = r14 + r12
        L_0x0071:
            java.util.concurrent.TimeUnit r0 = java.util.concurrent.TimeUnit.NANOSECONDS     // Catch:{ InterruptedException -> 0x009c }
            java.lang.Object r0 = r10.get(r12, r0)     // Catch:{ InterruptedException -> 0x009c }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ InterruptedException -> 0x009c }
            boolean r0 = r0.booleanValue()     // Catch:{ InterruptedException -> 0x009c }
            if (r0 == 0) goto L_0x0090
            androidx.collection.SimpleArrayMap<java.lang.Object, java.lang.Object> r0 = androidx.core.location.LocationManagerCompat.GnssLazyLoader.sGnssStatusListeners     // Catch:{ InterruptedException -> 0x009c }
            r0.put(r3, r9)     // Catch:{ InterruptedException -> 0x009c }
            if (r11 == 0) goto L_0x008e
            java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x010f }
            r0.interrupt()     // Catch:{ all -> 0x010f }
        L_0x008e:
            monitor-exit(r6)     // Catch:{ all -> 0x010f }
            return r4
        L_0x0090:
            if (r11 == 0) goto L_0x009a
            java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x010f }
            r0.interrupt()     // Catch:{ all -> 0x010f }
        L_0x009a:
            monitor-exit(r6)     // Catch:{ all -> 0x010f }
            return r5
        L_0x009c:
            r0 = move-exception
            r11 = 1
            long r16 = java.lang.System.nanoTime()     // Catch:{ ExecutionException -> 0x00c1, TimeoutException -> 0x00a7 }
            long r12 = r14 - r16
            goto L_0x0071
        L_0x00a5:
            r0 = move-exception
            goto L_0x00e6
        L_0x00a7:
            r0 = move-exception
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch:{ all -> 0x00a5 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x00a5 }
            r5.<init>()     // Catch:{ all -> 0x00a5 }
            java.lang.StringBuilder r5 = r5.append(r2)     // Catch:{ all -> 0x00a5 }
            java.lang.String r12 = " appears to be blocked, please run registerGnssStatusCallback() directly on a Looper thread or ensure the main Looper is not blocked by this thread"
            java.lang.StringBuilder r5 = r5.append(r12)     // Catch:{ all -> 0x00a5 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x00a5 }
            r4.<init>(r5, r0)     // Catch:{ all -> 0x00a5 }
            throw r4     // Catch:{ all -> 0x00a5 }
        L_0x00c1:
            r0 = move-exception
            java.lang.Throwable r4 = r0.getCause()     // Catch:{ all -> 0x00a5 }
            boolean r4 = r4 instanceof java.lang.RuntimeException     // Catch:{ all -> 0x00a5 }
            if (r4 != 0) goto L_0x00df
            java.lang.Throwable r4 = r0.getCause()     // Catch:{ all -> 0x00a5 }
            boolean r4 = r4 instanceof java.lang.Error     // Catch:{ all -> 0x00a5 }
            if (r4 == 0) goto L_0x00d9
            java.lang.Throwable r4 = r0.getCause()     // Catch:{ all -> 0x00a5 }
            java.lang.Error r4 = (java.lang.Error) r4     // Catch:{ all -> 0x00a5 }
            throw r4     // Catch:{ all -> 0x00a5 }
        L_0x00d9:
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch:{ all -> 0x00a5 }
            r4.<init>(r0)     // Catch:{ all -> 0x00a5 }
            throw r4     // Catch:{ all -> 0x00a5 }
        L_0x00df:
            java.lang.Throwable r4 = r0.getCause()     // Catch:{ all -> 0x00a5 }
            java.lang.RuntimeException r4 = (java.lang.RuntimeException) r4     // Catch:{ all -> 0x00a5 }
            throw r4     // Catch:{ all -> 0x00a5 }
        L_0x00e6:
            if (r11 == 0) goto L_0x00ef
            java.lang.Thread r4 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x010f }
            r4.interrupt()     // Catch:{ all -> 0x010f }
        L_0x00ef:
            throw r0     // Catch:{ all -> 0x010f }
        L_0x00f1:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException     // Catch:{ all -> 0x010f }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x010f }
            r4.<init>()     // Catch:{ all -> 0x010f }
            java.lang.StringBuilder r4 = r4.append(r2)     // Catch:{ all -> 0x010f }
            java.lang.String r5 = " is shutting down"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x010f }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x010f }
            r0.<init>(r4)     // Catch:{ all -> 0x010f }
            throw r0     // Catch:{ all -> 0x010f }
        L_0x010a:
            r0 = move-exception
            r8 = r20
        L_0x010d:
            monitor-exit(r6)     // Catch:{ all -> 0x010f }
            throw r0
        L_0x010f:
            r0 = move-exception
            goto L_0x010d
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.location.LocationManagerCompat.registerGnssStatusCallback(android.location.LocationManager, android.os.Handler, java.util.concurrent.Executor, androidx.core.location.GnssStatusCompat$Callback):boolean");
    }

    public static boolean registerGnssStatusCallback(LocationManager locationManager, GnssStatusCompat.Callback callback, Handler handler) {
        return Build.VERSION.SDK_INT >= 30 ? registerGnssStatusCallback(locationManager, ExecutorCompat.create(handler), callback) : registerGnssStatusCallback(locationManager, (Executor) new InlineHandlerExecutor(handler), callback);
    }

    public static boolean registerGnssStatusCallback(LocationManager locationManager, Executor executor, GnssStatusCompat.Callback callback) {
        if (Build.VERSION.SDK_INT >= 30) {
            return registerGnssStatusCallback(locationManager, (Handler) null, executor, callback);
        }
        Looper myLooper = Looper.myLooper();
        if (myLooper == null) {
            myLooper = Looper.getMainLooper();
        }
        return registerGnssStatusCallback(locationManager, new Handler(myLooper), executor, callback);
    }

    static void registerLocationListenerTransport(LocationManager locationManager, LocationListenerTransport transport) {
        WeakReference put = sLocationListeners.put(transport.getKey(), new WeakReference(transport));
        LocationListenerTransport locationListenerTransport = put != null ? (LocationListenerTransport) put.get() : null;
        if (locationListenerTransport != null) {
            locationListenerTransport.unregister();
            locationManager.removeUpdates(locationListenerTransport);
        }
    }

    public static void removeUpdates(LocationManager locationManager, LocationListenerCompat listener) {
        WeakHashMap<LocationListenerKey, WeakReference<LocationListenerTransport>> weakHashMap = sLocationListeners;
        synchronized (weakHashMap) {
            ArrayList arrayList = null;
            for (WeakReference<LocationListenerTransport> weakReference : weakHashMap.values()) {
                LocationListenerTransport locationListenerTransport = (LocationListenerTransport) weakReference.get();
                if (locationListenerTransport != null) {
                    LocationListenerKey key = locationListenerTransport.getKey();
                    if (key.mListener == listener) {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(key);
                        locationListenerTransport.unregister();
                        locationManager.removeUpdates(locationListenerTransport);
                    }
                }
            }
            if (arrayList != null) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    sLocationListeners.remove((LocationListenerKey) it.next());
                }
            }
        }
        locationManager.removeUpdates(listener);
    }

    public static void requestLocationUpdates(LocationManager locationManager, String provider, LocationRequestCompat locationRequest, LocationListenerCompat listener, Looper looper) {
        if (Build.VERSION.SDK_INT >= 31) {
            Api31Impl.requestLocationUpdates(locationManager, provider, locationRequest.toLocationRequest(), ExecutorCompat.create(new Handler(looper)), listener);
        } else if (Build.VERSION.SDK_INT < 19 || !Api19Impl.tryRequestLocationUpdates(locationManager, provider, locationRequest, listener, looper)) {
            locationManager.requestLocationUpdates(provider, locationRequest.getIntervalMillis(), locationRequest.getMinUpdateDistanceMeters(), listener, looper);
        }
    }

    public static void requestLocationUpdates(LocationManager locationManager, String provider, LocationRequestCompat locationRequest, Executor executor, LocationListenerCompat listener) {
        if (Build.VERSION.SDK_INT >= 31) {
            Api31Impl.requestLocationUpdates(locationManager, provider, locationRequest.toLocationRequest(), executor, listener);
        } else if (Build.VERSION.SDK_INT < 30 || !Api30Impl.tryRequestLocationUpdates(locationManager, provider, locationRequest, executor, listener)) {
            LocationListenerTransport locationListenerTransport = new LocationListenerTransport(new LocationListenerKey(provider, listener), executor);
            if (Build.VERSION.SDK_INT < 19 || !Api19Impl.tryRequestLocationUpdates(locationManager, provider, locationRequest, locationListenerTransport)) {
                synchronized (sLocationListeners) {
                    locationManager.requestLocationUpdates(provider, locationRequest.getIntervalMillis(), locationRequest.getMinUpdateDistanceMeters(), locationListenerTransport, Looper.getMainLooper());
                    registerLocationListenerTransport(locationManager, locationListenerTransport);
                }
            }
        }
    }

    public static void unregisterGnssStatusCallback(LocationManager locationManager, GnssStatusCompat.Callback callback) {
        if (Build.VERSION.SDK_INT >= 24) {
            synchronized (GnssLazyLoader.sGnssStatusListeners) {
                Object remove = GnssLazyLoader.sGnssStatusListeners.remove(callback);
                if (remove != null) {
                    Api24Impl.unregisterGnssStatusCallback(locationManager, remove);
                }
            }
            return;
        }
        synchronized (GnssLazyLoader.sGnssStatusListeners) {
            GpsStatusTransport gpsStatusTransport = (GpsStatusTransport) GnssLazyLoader.sGnssStatusListeners.remove(callback);
            if (gpsStatusTransport != null) {
                gpsStatusTransport.unregister();
                locationManager.removeGpsStatusListener(gpsStatusTransport);
            }
        }
    }

    public static boolean isLocationEnabled(LocationManager locationManager) {
        if (Build.VERSION.SDK_INT >= 28) {
            return Api28Impl.isLocationEnabled(locationManager);
        }
        if (Build.VERSION.SDK_INT <= 19) {
            try {
                if (sContextField == null) {
                    Field declaredField = LocationManager.class.getDeclaredField("mContext");
                    sContextField = declaredField;
                    declaredField.setAccessible(true);
                }
                Context context = (Context) sContextField.get(locationManager);
                if (context != null) {
                    if (Build.VERSION.SDK_INT == 19) {
                        return Settings.Secure.getInt(context.getContentResolver(), "location_mode", 0) != 0;
                    }
                    String string = Settings.Secure.getString(context.getContentResolver(), "location_providers_allowed");
                    Log1F380D.a((Object) string);
                    return !TextUtils.isEmpty(string);
                }
            } catch (ClassCastException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            }
        }
        return locationManager.isProviderEnabled("network") || locationManager.isProviderEnabled("gps");
    }
}

package androidx.work.impl.constraints.trackers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import androidx.core.net.ConnectivityManagerCompat;
import androidx.work.Logger;
import androidx.work.impl.constraints.NetworkState;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import mt.Log1F380D;

/* compiled from: 00CE */
public class NetworkStateTracker extends ConstraintTracker<NetworkState> {
    static final String TAG;
    private NetworkStateBroadcastReceiver mBroadcastReceiver;
    private final ConnectivityManager mConnectivityManager = ((ConnectivityManager) this.mAppContext.getSystemService("connectivity"));
    private NetworkStateCallback mNetworkCallback;

    private class NetworkStateBroadcastReceiver extends BroadcastReceiver {
        NetworkStateBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null && intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                Logger.get().debug(NetworkStateTracker.TAG, "Network broadcast received", new Throwable[0]);
                NetworkStateTracker networkStateTracker = NetworkStateTracker.this;
                networkStateTracker.setState(networkStateTracker.getActiveNetworkState());
            }
        }
    }

    /* compiled from: 00CD */
    private class NetworkStateCallback extends ConnectivityManager.NetworkCallback {
        NetworkStateCallback() {
        }

        public void onCapabilitiesChanged(Network network, NetworkCapabilities capabilities) {
            Logger logger = Logger.get();
            String str = NetworkStateTracker.TAG;
            String format = String.format("Network capabilities changed: %s", new Object[]{capabilities});
            Log1F380D.a((Object) format);
            logger.debug(str, format, new Throwable[0]);
            NetworkStateTracker networkStateTracker = NetworkStateTracker.this;
            networkStateTracker.setState(networkStateTracker.getActiveNetworkState());
        }

        public void onLost(Network network) {
            Logger.get().debug(NetworkStateTracker.TAG, "Network connection lost", new Throwable[0]);
            NetworkStateTracker networkStateTracker = NetworkStateTracker.this;
            networkStateTracker.setState(networkStateTracker.getActiveNetworkState());
        }
    }

    static {
        String tagWithPrefix = Logger.tagWithPrefix("NetworkStateTracker");
        Log1F380D.a((Object) tagWithPrefix);
        TAG = tagWithPrefix;
    }

    public NetworkStateTracker(Context context, TaskExecutor taskExecutor) {
        super(context, taskExecutor);
        if (isNetworkCallbackSupported()) {
            this.mNetworkCallback = new NetworkStateCallback();
        } else {
            this.mBroadcastReceiver = new NetworkStateBroadcastReceiver();
        }
    }

    private static boolean isNetworkCallbackSupported() {
        return Build.VERSION.SDK_INT >= 24;
    }

    /* access modifiers changed from: package-private */
    public NetworkState getActiveNetworkState() {
        NetworkInfo activeNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
        boolean z = true;
        boolean z2 = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        boolean isActiveNetworkValidated = isActiveNetworkValidated();
        boolean isActiveNetworkMetered = ConnectivityManagerCompat.isActiveNetworkMetered(this.mConnectivityManager);
        if (activeNetworkInfo == null || activeNetworkInfo.isRoaming()) {
            z = false;
        }
        return new NetworkState(z2, isActiveNetworkValidated, isActiveNetworkMetered, z);
    }

    public NetworkState getInitialState() {
        return getActiveNetworkState();
    }

    /* access modifiers changed from: package-private */
    public boolean isActiveNetworkValidated() {
        if (Build.VERSION.SDK_INT < 23) {
            return false;
        }
        try {
            NetworkCapabilities networkCapabilities = this.mConnectivityManager.getNetworkCapabilities(this.mConnectivityManager.getActiveNetwork());
            return networkCapabilities != null && networkCapabilities.hasCapability(16);
        } catch (SecurityException e) {
            Logger.get().error(TAG, "Unable to validate active network", e);
            return false;
        }
    }

    public void startTracking() {
        if (isNetworkCallbackSupported()) {
            try {
                Logger.get().debug(TAG, "Registering network callback", new Throwable[0]);
                this.mConnectivityManager.registerDefaultNetworkCallback(this.mNetworkCallback);
            } catch (IllegalArgumentException | SecurityException e) {
                Logger.get().error(TAG, "Received exception while registering network callback", e);
            }
        } else {
            Logger.get().debug(TAG, "Registering broadcast receiver", new Throwable[0]);
            this.mAppContext.registerReceiver(this.mBroadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
    }

    public void stopTracking() {
        if (isNetworkCallbackSupported()) {
            try {
                Logger.get().debug(TAG, "Unregistering network callback", new Throwable[0]);
                this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
            } catch (IllegalArgumentException | SecurityException e) {
                Logger.get().error(TAG, "Received exception while unregistering network callback", e);
            }
        } else {
            Logger.get().debug(TAG, "Unregistering broadcast receiver", new Throwable[0]);
            this.mAppContext.unregisterReceiver(this.mBroadcastReceiver);
        }
    }
}

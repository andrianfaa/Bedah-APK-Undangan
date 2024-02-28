package androidx.room;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import androidx.room.IMultiInstanceInvalidationCallback;
import androidx.room.IMultiInstanceInvalidationService;
import androidx.room.InvalidationTracker;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

class MultiInstanceInvalidationClient {
    final Context mAppContext;
    final IMultiInstanceInvalidationCallback mCallback = new IMultiInstanceInvalidationCallback.Stub() {
        public void onInvalidation(final String[] tables) {
            MultiInstanceInvalidationClient.this.mExecutor.execute(new Runnable() {
                public void run() {
                    MultiInstanceInvalidationClient.this.mInvalidationTracker.notifyObserversByTableNames(tables);
                }
            });
        }
    };
    int mClientId;
    final Executor mExecutor;
    final InvalidationTracker mInvalidationTracker;
    final String mName;
    final InvalidationTracker.Observer mObserver;
    final Runnable mRemoveObserverRunnable;
    IMultiInstanceInvalidationService mService;
    final ServiceConnection mServiceConnection;
    final Runnable mSetUpRunnable;
    final AtomicBoolean mStopped = new AtomicBoolean(false);
    private final Runnable mTearDownRunnable;

    MultiInstanceInvalidationClient(Context context, String name, InvalidationTracker invalidationTracker, Executor executor) {
        AnonymousClass2 r0 = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder service) {
                MultiInstanceInvalidationClient.this.mService = IMultiInstanceInvalidationService.Stub.asInterface(service);
                MultiInstanceInvalidationClient.this.mExecutor.execute(MultiInstanceInvalidationClient.this.mSetUpRunnable);
            }

            public void onServiceDisconnected(ComponentName name) {
                MultiInstanceInvalidationClient.this.mExecutor.execute(MultiInstanceInvalidationClient.this.mRemoveObserverRunnable);
                MultiInstanceInvalidationClient.this.mService = null;
            }
        };
        this.mServiceConnection = r0;
        this.mSetUpRunnable = new Runnable() {
            public void run() {
                try {
                    IMultiInstanceInvalidationService iMultiInstanceInvalidationService = MultiInstanceInvalidationClient.this.mService;
                    if (iMultiInstanceInvalidationService != null) {
                        MultiInstanceInvalidationClient multiInstanceInvalidationClient = MultiInstanceInvalidationClient.this;
                        multiInstanceInvalidationClient.mClientId = iMultiInstanceInvalidationService.registerCallback(multiInstanceInvalidationClient.mCallback, MultiInstanceInvalidationClient.this.mName);
                        MultiInstanceInvalidationClient.this.mInvalidationTracker.addObserver(MultiInstanceInvalidationClient.this.mObserver);
                    }
                } catch (RemoteException e) {
                    Log.w("ROOM", "Cannot register multi-instance invalidation callback", e);
                }
            }
        };
        this.mRemoveObserverRunnable = new Runnable() {
            public void run() {
                MultiInstanceInvalidationClient.this.mInvalidationTracker.removeObserver(MultiInstanceInvalidationClient.this.mObserver);
            }
        };
        this.mTearDownRunnable = new Runnable() {
            public void run() {
                MultiInstanceInvalidationClient.this.mInvalidationTracker.removeObserver(MultiInstanceInvalidationClient.this.mObserver);
                try {
                    IMultiInstanceInvalidationService iMultiInstanceInvalidationService = MultiInstanceInvalidationClient.this.mService;
                    if (iMultiInstanceInvalidationService != null) {
                        iMultiInstanceInvalidationService.unregisterCallback(MultiInstanceInvalidationClient.this.mCallback, MultiInstanceInvalidationClient.this.mClientId);
                    }
                } catch (RemoteException e) {
                    Log.w("ROOM", "Cannot unregister multi-instance invalidation callback", e);
                }
                MultiInstanceInvalidationClient.this.mAppContext.unbindService(MultiInstanceInvalidationClient.this.mServiceConnection);
            }
        };
        Context applicationContext = context.getApplicationContext();
        this.mAppContext = applicationContext;
        this.mName = name;
        this.mInvalidationTracker = invalidationTracker;
        this.mExecutor = executor;
        this.mObserver = new InvalidationTracker.Observer((String[]) invalidationTracker.mTableIdLookup.keySet().toArray(new String[0])) {
            /* access modifiers changed from: package-private */
            public boolean isRemote() {
                return true;
            }

            public void onInvalidated(Set<String> set) {
                if (!MultiInstanceInvalidationClient.this.mStopped.get()) {
                    try {
                        IMultiInstanceInvalidationService iMultiInstanceInvalidationService = MultiInstanceInvalidationClient.this.mService;
                        if (iMultiInstanceInvalidationService != null) {
                            iMultiInstanceInvalidationService.broadcastInvalidation(MultiInstanceInvalidationClient.this.mClientId, (String[]) set.toArray(new String[0]));
                        }
                    } catch (RemoteException e) {
                        Log.w("ROOM", "Cannot broadcast invalidation", e);
                    }
                }
            }
        };
        applicationContext.bindService(new Intent(applicationContext, MultiInstanceInvalidationService.class), r0, 1);
    }

    /* access modifiers changed from: package-private */
    public void stop() {
        if (this.mStopped.compareAndSet(false, true)) {
            this.mExecutor.execute(this.mTearDownRunnable);
        }
    }
}

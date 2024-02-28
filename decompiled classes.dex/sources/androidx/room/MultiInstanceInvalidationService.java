package androidx.room;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import androidx.room.IMultiInstanceInvalidationService;
import java.util.HashMap;

public class MultiInstanceInvalidationService extends Service {
    private final IMultiInstanceInvalidationService.Stub mBinder = new IMultiInstanceInvalidationService.Stub() {
        public void broadcastInvalidation(int clientId, String[] tables) {
            synchronized (MultiInstanceInvalidationService.this.mCallbackList) {
                String str = MultiInstanceInvalidationService.this.mClientNames.get(Integer.valueOf(clientId));
                if (str == null) {
                    Log.w("ROOM", "Remote invalidation client ID not registered");
                    return;
                }
                int beginBroadcast = MultiInstanceInvalidationService.this.mCallbackList.beginBroadcast();
                for (int i = 0; i < beginBroadcast; i++) {
                    try {
                        int intValue = ((Integer) MultiInstanceInvalidationService.this.mCallbackList.getBroadcastCookie(i)).intValue();
                        String str2 = MultiInstanceInvalidationService.this.mClientNames.get(Integer.valueOf(intValue));
                        if (clientId != intValue && str.equals(str2)) {
                            MultiInstanceInvalidationService.this.mCallbackList.getBroadcastItem(i).onInvalidation(tables);
                        }
                    } catch (RemoteException e) {
                        Log.w("ROOM", "Error invoking a remote callback", e);
                    } catch (Throwable th) {
                        MultiInstanceInvalidationService.this.mCallbackList.finishBroadcast();
                        throw th;
                    }
                }
                MultiInstanceInvalidationService.this.mCallbackList.finishBroadcast();
            }
        }

        public int registerCallback(IMultiInstanceInvalidationCallback callback, String name) {
            if (name == null) {
                return 0;
            }
            synchronized (MultiInstanceInvalidationService.this.mCallbackList) {
                MultiInstanceInvalidationService multiInstanceInvalidationService = MultiInstanceInvalidationService.this;
                int i = multiInstanceInvalidationService.mMaxClientId + 1;
                multiInstanceInvalidationService.mMaxClientId = i;
                int i2 = i;
                if (MultiInstanceInvalidationService.this.mCallbackList.register(callback, Integer.valueOf(i2))) {
                    MultiInstanceInvalidationService.this.mClientNames.put(Integer.valueOf(i2), name);
                    return i2;
                }
                MultiInstanceInvalidationService multiInstanceInvalidationService2 = MultiInstanceInvalidationService.this;
                multiInstanceInvalidationService2.mMaxClientId--;
                return 0;
            }
        }

        public void unregisterCallback(IMultiInstanceInvalidationCallback callback, int clientId) {
            synchronized (MultiInstanceInvalidationService.this.mCallbackList) {
                MultiInstanceInvalidationService.this.mCallbackList.unregister(callback);
                MultiInstanceInvalidationService.this.mClientNames.remove(Integer.valueOf(clientId));
            }
        }
    };
    final RemoteCallbackList<IMultiInstanceInvalidationCallback> mCallbackList = new RemoteCallbackList<IMultiInstanceInvalidationCallback>() {
        public void onCallbackDied(IMultiInstanceInvalidationCallback callback, Object cookie) {
            MultiInstanceInvalidationService.this.mClientNames.remove(Integer.valueOf(((Integer) cookie).intValue()));
        }
    };
    final HashMap<Integer, String> mClientNames = new HashMap<>();
    int mMaxClientId = 0;

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }
}

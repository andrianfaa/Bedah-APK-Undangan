package androidx.core.app.unusedapprestrictions;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IUnusedAppRestrictionsBackportCallback extends IInterface {

    public static class Default implements IUnusedAppRestrictionsBackportCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onIsPermissionRevocationEnabledForAppResult(boolean success, boolean isEnabled) throws RemoteException {
        }
    }

    public static abstract class Stub extends Binder implements IUnusedAppRestrictionsBackportCallback {
        private static final String DESCRIPTOR = "androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportCallback";
        static final int TRANSACTION_onIsPermissionRevocationEnabledForAppResult = 1;

        private static class Proxy implements IUnusedAppRestrictionsBackportCallback {
            public static IUnusedAppRestrictionsBackportCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void onIsPermissionRevocationEnabledForAppResult(boolean success, boolean isEnabled) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    obtain.writeInt(success ? 1 : 0);
                    if (isEnabled) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    if (this.mRemote.transact(1, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().onIsPermissionRevocationEnabledForAppResult(success, isEnabled);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IUnusedAppRestrictionsBackportCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface queryLocalInterface = obj.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IUnusedAppRestrictionsBackportCallback)) ? new Proxy(obj) : (IUnusedAppRestrictionsBackportCallback) queryLocalInterface;
        }

        public static IUnusedAppRestrictionsBackportCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(IUnusedAppRestrictionsBackportCallback impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (impl == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = impl;
                return true;
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    boolean z = false;
                    boolean z2 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        z = true;
                    }
                    onIsPermissionRevocationEnabledForAppResult(z2, z);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void onIsPermissionRevocationEnabledForAppResult(boolean z, boolean z2) throws RemoteException;
}

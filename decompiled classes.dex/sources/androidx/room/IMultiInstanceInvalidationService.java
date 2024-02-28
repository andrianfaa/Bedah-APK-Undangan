package androidx.room;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import androidx.room.IMultiInstanceInvalidationCallback;

public interface IMultiInstanceInvalidationService extends IInterface {

    public static abstract class Stub extends Binder implements IMultiInstanceInvalidationService {
        private static final String DESCRIPTOR = "androidx.room.IMultiInstanceInvalidationService";
        static final int TRANSACTION_broadcastInvalidation = 3;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_unregisterCallback = 2;

        private static class Proxy implements IMultiInstanceInvalidationService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void broadcastInvalidation(int clientId, String[] tables) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(clientId);
                    obtain.writeStringArray(tables);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public int registerCallback(IMultiInstanceInvalidationCallback callback, String name) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    obtain.writeString(name);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterCallback(IMultiInstanceInvalidationCallback callback, int clientId) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    obtain.writeInt(clientId);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMultiInstanceInvalidationService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface queryLocalInterface = obj.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IMultiInstanceInvalidationService)) ? new Proxy(obj) : (IMultiInstanceInvalidationService) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int registerCallback = registerCallback(IMultiInstanceInvalidationCallback.Stub.asInterface(data.readStrongBinder()), data.readString());
                    reply.writeNoException();
                    reply.writeInt(registerCallback);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterCallback(IMultiInstanceInvalidationCallback.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    broadcastInvalidation(data.readInt(), data.createStringArray());
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void broadcastInvalidation(int i, String[] strArr) throws RemoteException;

    int registerCallback(IMultiInstanceInvalidationCallback iMultiInstanceInvalidationCallback, String str) throws RemoteException;

    void unregisterCallback(IMultiInstanceInvalidationCallback iMultiInstanceInvalidationCallback, int i) throws RemoteException;
}

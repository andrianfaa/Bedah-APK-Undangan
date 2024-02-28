package androidx.work.multiprocess;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import androidx.work.multiprocess.IWorkManagerImplCallback;

public interface IWorkManagerImpl extends IInterface {

    public static class Default implements IWorkManagerImpl {
        public IBinder asBinder() {
            return null;
        }

        public void cancelAllWork(IWorkManagerImplCallback callback) throws RemoteException {
        }

        public void cancelAllWorkByTag(String tag, IWorkManagerImplCallback callback) throws RemoteException {
        }

        public void cancelUniqueWork(String name, IWorkManagerImplCallback callback) throws RemoteException {
        }

        public void cancelWorkById(String id, IWorkManagerImplCallback callback) throws RemoteException {
        }

        public void enqueueContinuation(byte[] request, IWorkManagerImplCallback callback) throws RemoteException {
        }

        public void enqueueWorkRequests(byte[] request, IWorkManagerImplCallback callback) throws RemoteException {
        }

        public void queryWorkInfo(byte[] request, IWorkManagerImplCallback callback) throws RemoteException {
        }

        public void setProgress(byte[] request, IWorkManagerImplCallback callback) throws RemoteException {
        }
    }

    public static abstract class Stub extends Binder implements IWorkManagerImpl {
        private static final String DESCRIPTOR = "androidx.work.multiprocess.IWorkManagerImpl";
        static final int TRANSACTION_cancelAllWork = 6;
        static final int TRANSACTION_cancelAllWorkByTag = 4;
        static final int TRANSACTION_cancelUniqueWork = 5;
        static final int TRANSACTION_cancelWorkById = 3;
        static final int TRANSACTION_enqueueContinuation = 2;
        static final int TRANSACTION_enqueueWorkRequests = 1;
        static final int TRANSACTION_queryWorkInfo = 7;
        static final int TRANSACTION_setProgress = 8;

        private static class Proxy implements IWorkManagerImpl {
            public static IWorkManagerImpl sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void cancelAllWork(IWorkManagerImplCallback callback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(6, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().cancelAllWork(callback);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void cancelAllWorkByTag(String tag, IWorkManagerImplCallback callback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(tag);
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(4, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().cancelAllWorkByTag(tag, callback);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void cancelUniqueWork(String name, IWorkManagerImplCallback callback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(name);
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(5, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().cancelUniqueWork(name, callback);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void cancelWorkById(String id, IWorkManagerImplCallback callback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(id);
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(3, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().cancelWorkById(id, callback);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void enqueueContinuation(byte[] request, IWorkManagerImplCallback callback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(request);
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(2, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().enqueueContinuation(request, callback);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void enqueueWorkRequests(byte[] request, IWorkManagerImplCallback callback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(request);
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(1, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().enqueueWorkRequests(request, callback);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void queryWorkInfo(byte[] request, IWorkManagerImplCallback callback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(request);
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(7, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().queryWorkInfo(request, callback);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void setProgress(byte[] request, IWorkManagerImplCallback callback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(request);
                    obtain.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(8, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().setProgress(request, callback);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWorkManagerImpl asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface queryLocalInterface = obj.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IWorkManagerImpl)) ? new Proxy(obj) : (IWorkManagerImpl) queryLocalInterface;
        }

        public static IWorkManagerImpl getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }

        public static boolean setDefaultImpl(IWorkManagerImpl impl) {
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
                    enqueueWorkRequests(data.createByteArray(), IWorkManagerImplCallback.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    enqueueContinuation(data.createByteArray(), IWorkManagerImplCallback.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    cancelWorkById(data.readString(), IWorkManagerImplCallback.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    cancelAllWorkByTag(data.readString(), IWorkManagerImplCallback.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    cancelUniqueWork(data.readString(), IWorkManagerImplCallback.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    cancelAllWork(IWorkManagerImplCallback.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    queryWorkInfo(data.createByteArray(), IWorkManagerImplCallback.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    setProgress(data.createByteArray(), IWorkManagerImplCallback.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void cancelAllWork(IWorkManagerImplCallback iWorkManagerImplCallback) throws RemoteException;

    void cancelAllWorkByTag(String str, IWorkManagerImplCallback iWorkManagerImplCallback) throws RemoteException;

    void cancelUniqueWork(String str, IWorkManagerImplCallback iWorkManagerImplCallback) throws RemoteException;

    void cancelWorkById(String str, IWorkManagerImplCallback iWorkManagerImplCallback) throws RemoteException;

    void enqueueContinuation(byte[] bArr, IWorkManagerImplCallback iWorkManagerImplCallback) throws RemoteException;

    void enqueueWorkRequests(byte[] bArr, IWorkManagerImplCallback iWorkManagerImplCallback) throws RemoteException;

    void queryWorkInfo(byte[] bArr, IWorkManagerImplCallback iWorkManagerImplCallback) throws RemoteException;

    void setProgress(byte[] bArr, IWorkManagerImplCallback iWorkManagerImplCallback) throws RemoteException;
}

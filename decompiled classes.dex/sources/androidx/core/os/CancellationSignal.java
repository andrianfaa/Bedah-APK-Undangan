package androidx.core.os;

import android.os.Build;

public final class CancellationSignal {
    private boolean mCancelInProgress;
    private Object mCancellationSignalObj;
    private boolean mIsCanceled;
    private OnCancelListener mOnCancelListener;

    static class Api16Impl {
        private Api16Impl() {
        }

        static void cancel(Object cancellationSignal) {
            ((android.os.CancellationSignal) cancellationSignal).cancel();
        }

        static android.os.CancellationSignal createCancellationSignal() {
            return new android.os.CancellationSignal();
        }
    }

    public interface OnCancelListener {
        void onCancel();
    }

    private void waitForCancelFinishedLocked() {
        while (this.mCancelInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        r0.onCancel();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0018, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001a, code lost:
        if (r1 == null) goto L_0x0031;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0020, code lost:
        if (android.os.Build.VERSION.SDK_INT < 16) goto L_0x0031;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0022, code lost:
        androidx.core.os.CancellationSignal.Api16Impl.cancel(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0026, code lost:
        monitor-enter(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        r5.mCancelInProgress = false;
        notifyAll();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x002d, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0031, code lost:
        monitor-enter(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        r5.mCancelInProgress = false;
        notifyAll();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0037, code lost:
        monitor-exit(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0039, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0012, code lost:
        if (r0 == null) goto L_0x001a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void cancel() {
        /*
            r5 = this;
            monitor-enter(r5)
            boolean r0 = r5.mIsCanceled     // Catch:{ all -> 0x003d }
            if (r0 == 0) goto L_0x0007
            monitor-exit(r5)     // Catch:{ all -> 0x003d }
            return
        L_0x0007:
            r0 = 1
            r5.mIsCanceled = r0     // Catch:{ all -> 0x003d }
            r5.mCancelInProgress = r0     // Catch:{ all -> 0x003d }
            androidx.core.os.CancellationSignal$OnCancelListener r0 = r5.mOnCancelListener     // Catch:{ all -> 0x003d }
            java.lang.Object r1 = r5.mCancellationSignalObj     // Catch:{ all -> 0x003d }
            monitor-exit(r5)     // Catch:{ all -> 0x003d }
            r2 = 0
            if (r0 == 0) goto L_0x001a
            r0.onCancel()     // Catch:{ all -> 0x0018 }
            goto L_0x001a
        L_0x0018:
            r3 = move-exception
            goto L_0x0026
        L_0x001a:
            if (r1 == 0) goto L_0x0031
            int r3 = android.os.Build.VERSION.SDK_INT     // Catch:{ all -> 0x0018 }
            r4 = 16
            if (r3 < r4) goto L_0x0031
            androidx.core.os.CancellationSignal.Api16Impl.cancel(r1)     // Catch:{ all -> 0x0018 }
            goto L_0x0031
        L_0x0026:
            monitor-enter(r5)
            r5.mCancelInProgress = r2     // Catch:{ all -> 0x002e }
            r5.notifyAll()     // Catch:{ all -> 0x002e }
            monitor-exit(r5)     // Catch:{ all -> 0x002e }
            throw r3
        L_0x002e:
            r2 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x002e }
            throw r2
        L_0x0031:
            monitor-enter(r5)
            r5.mCancelInProgress = r2     // Catch:{ all -> 0x003a }
            r5.notifyAll()     // Catch:{ all -> 0x003a }
            monitor-exit(r5)     // Catch:{ all -> 0x003a }
            return
        L_0x003a:
            r2 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x003a }
            throw r2
        L_0x003d:
            r0 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x003d }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.os.CancellationSignal.cancel():void");
    }

    public Object getCancellationSignalObject() {
        Object obj;
        if (Build.VERSION.SDK_INT < 16) {
            return null;
        }
        synchronized (this) {
            if (this.mCancellationSignalObj == null) {
                android.os.CancellationSignal createCancellationSignal = Api16Impl.createCancellationSignal();
                this.mCancellationSignalObj = createCancellationSignal;
                if (this.mIsCanceled) {
                    Api16Impl.cancel(createCancellationSignal);
                }
            }
            obj = this.mCancellationSignalObj;
        }
        return obj;
    }

    public boolean isCanceled() {
        boolean z;
        synchronized (this) {
            z = this.mIsCanceled;
        }
        return z;
    }

    public void setOnCancelListener(OnCancelListener listener) {
        synchronized (this) {
            waitForCancelFinishedLocked();
            if (this.mOnCancelListener != listener) {
                this.mOnCancelListener = listener;
                if (this.mIsCanceled) {
                    if (listener != null) {
                        listener.onCancel();
                    }
                }
            }
        }
    }

    public void throwIfCanceled() {
        if (isCanceled()) {
            throw new OperationCanceledException();
        }
    }
}

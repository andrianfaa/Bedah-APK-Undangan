package androidx.core.hardware.fingerprint;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;

@Deprecated
public class FingerprintManagerCompat {
    private final Context mContext;

    static class Api23Impl {
        private Api23Impl() {
        }

        static void authenticate(Object fingerprintManager, Object crypto, CancellationSignal cancel, int flags, Object callback, Handler handler) {
            ((FingerprintManager) fingerprintManager).authenticate((FingerprintManager.CryptoObject) crypto, cancel, flags, (FingerprintManager.AuthenticationCallback) callback, handler);
        }

        static FingerprintManager.CryptoObject getCryptoObject(Object authenticationResult) {
            return ((FingerprintManager.AuthenticationResult) authenticationResult).getCryptoObject();
        }

        public static FingerprintManager getFingerprintManagerOrNull(Context context) {
            if (Build.VERSION.SDK_INT == 23) {
                return (FingerprintManager) context.getSystemService(FingerprintManager.class);
            }
            if (Build.VERSION.SDK_INT <= 23 || !context.getPackageManager().hasSystemFeature("android.hardware.fingerprint")) {
                return null;
            }
            return (FingerprintManager) context.getSystemService(FingerprintManager.class);
        }

        static boolean hasEnrolledFingerprints(Object fingerprintManager) {
            return ((FingerprintManager) fingerprintManager).hasEnrolledFingerprints();
        }

        static boolean isHardwareDetected(Object fingerprintManager) {
            return ((FingerprintManager) fingerprintManager).isHardwareDetected();
        }

        public static CryptoObject unwrapCryptoObject(Object cryptoObjectObj) {
            FingerprintManager.CryptoObject cryptoObject = (FingerprintManager.CryptoObject) cryptoObjectObj;
            if (cryptoObject == null) {
                return null;
            }
            if (cryptoObject.getCipher() != null) {
                return new CryptoObject(cryptoObject.getCipher());
            }
            if (cryptoObject.getSignature() != null) {
                return new CryptoObject(cryptoObject.getSignature());
            }
            if (cryptoObject.getMac() != null) {
                return new CryptoObject(cryptoObject.getMac());
            }
            return null;
        }

        public static FingerprintManager.CryptoObject wrapCryptoObject(CryptoObject cryptoObject) {
            if (cryptoObject == null) {
                return null;
            }
            if (cryptoObject.getCipher() != null) {
                return new FingerprintManager.CryptoObject(cryptoObject.getCipher());
            }
            if (cryptoObject.getSignature() != null) {
                return new FingerprintManager.CryptoObject(cryptoObject.getSignature());
            }
            if (cryptoObject.getMac() != null) {
                return new FingerprintManager.CryptoObject(cryptoObject.getMac());
            }
            return null;
        }
    }

    public static abstract class AuthenticationCallback {
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
        }

        public void onAuthenticationFailed() {
        }

        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        }

        public void onAuthenticationSucceeded(AuthenticationResult result) {
        }
    }

    public static final class AuthenticationResult {
        private final CryptoObject mCryptoObject;

        public AuthenticationResult(CryptoObject crypto) {
            this.mCryptoObject = crypto;
        }

        public CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }
    }

    public static class CryptoObject {
        private final Cipher mCipher;
        private final Mac mMac;
        private final Signature mSignature;

        public CryptoObject(Signature signature) {
            this.mSignature = signature;
            this.mCipher = null;
            this.mMac = null;
        }

        public CryptoObject(Cipher cipher) {
            this.mCipher = cipher;
            this.mSignature = null;
            this.mMac = null;
        }

        public CryptoObject(Mac mac) {
            this.mMac = mac;
            this.mCipher = null;
            this.mSignature = null;
        }

        public Cipher getCipher() {
            return this.mCipher;
        }

        public Mac getMac() {
            return this.mMac;
        }

        public Signature getSignature() {
            return this.mSignature;
        }
    }

    private FingerprintManagerCompat(Context context) {
        this.mContext = context;
    }

    public static FingerprintManagerCompat from(Context context) {
        return new FingerprintManagerCompat(context);
    }

    private static FingerprintManager getFingerprintManagerOrNull(Context context) {
        return Api23Impl.getFingerprintManagerOrNull(context);
    }

    static CryptoObject unwrapCryptoObject(FingerprintManager.CryptoObject cryptoObject) {
        return Api23Impl.unwrapCryptoObject(cryptoObject);
    }

    private static FingerprintManager.AuthenticationCallback wrapCallback(final AuthenticationCallback callback) {
        return new FingerprintManager.AuthenticationCallback() {
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                AuthenticationCallback.this.onAuthenticationError(errMsgId, errString);
            }

            public void onAuthenticationFailed() {
                AuthenticationCallback.this.onAuthenticationFailed();
            }

            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                AuthenticationCallback.this.onAuthenticationHelp(helpMsgId, helpString);
            }

            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                AuthenticationCallback.this.onAuthenticationSucceeded(new AuthenticationResult(FingerprintManagerCompat.unwrapCryptoObject(Api23Impl.getCryptoObject(result))));
            }
        };
    }

    private static FingerprintManager.CryptoObject wrapCryptoObject(CryptoObject cryptoObject) {
        return Api23Impl.wrapCryptoObject(cryptoObject);
    }

    public void authenticate(CryptoObject crypto, int flags, androidx.core.os.CancellationSignal cancel, AuthenticationCallback callback, Handler handler) {
        FingerprintManager fingerprintManagerOrNull;
        if (Build.VERSION.SDK_INT >= 23 && (fingerprintManagerOrNull = getFingerprintManagerOrNull(this.mContext)) != null) {
            Api23Impl.authenticate(fingerprintManagerOrNull, wrapCryptoObject(crypto), cancel != null ? (CancellationSignal) cancel.getCancellationSignalObject() : null, flags, wrapCallback(callback), handler);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0007, code lost:
        r0 = getFingerprintManagerOrNull(r3.mContext);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hasEnrolledFingerprints() {
        /*
            r3 = this;
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 0
            r2 = 23
            if (r0 < r2) goto L_0x0017
            android.content.Context r0 = r3.mContext
            android.hardware.fingerprint.FingerprintManager r0 = getFingerprintManagerOrNull(r0)
            if (r0 == 0) goto L_0x0016
            boolean r2 = androidx.core.hardware.fingerprint.FingerprintManagerCompat.Api23Impl.hasEnrolledFingerprints(r0)
            if (r2 == 0) goto L_0x0016
            r1 = 1
        L_0x0016:
            return r1
        L_0x0017:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.hardware.fingerprint.FingerprintManagerCompat.hasEnrolledFingerprints():boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0007, code lost:
        r0 = getFingerprintManagerOrNull(r3.mContext);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isHardwareDetected() {
        /*
            r3 = this;
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 0
            r2 = 23
            if (r0 < r2) goto L_0x0017
            android.content.Context r0 = r3.mContext
            android.hardware.fingerprint.FingerprintManager r0 = getFingerprintManagerOrNull(r0)
            if (r0 == 0) goto L_0x0016
            boolean r2 = androidx.core.hardware.fingerprint.FingerprintManagerCompat.Api23Impl.isHardwareDetected(r0)
            if (r2 == 0) goto L_0x0016
            r1 = 1
        L_0x0016:
            return r1
        L_0x0017:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.hardware.fingerprint.FingerprintManagerCompat.isHardwareDetected():boolean");
    }
}
